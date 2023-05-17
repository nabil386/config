package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidence;
import curam.citizen.datahub.impl.Citizen;
import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizenworkspace.message.impl.CITIZENDATAHUBExceptionCreator;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.impl.CASESTATUSEntry;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.facade.fact.IntegratedCaseFactory;
import curam.core.facade.intf.IntegratedCase;
import curam.core.facade.struct.ActiveCasesConcernRoleIDAndTypeKey;
import curam.core.facade.struct.ICProductDeliveryDetails;
import curam.core.facade.struct.IntegratedCaseIDKey;
import curam.core.facade.struct.ListICProductDeliveryDetails;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.type.Date;
import curam.workspaceservices.util.impl.DateTimeTools;
import java.util.List;

public class BDMLifeEventTaxWithholdViewProcessor
  implements CustomViewProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  BDMVoluntaryTaxWithholdEvidence bdmVoluntaryTaxWithholdEvidence;

  protected BDMLifeEventTaxWithholdViewProcessor() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public void processView(final Citizen citizen, final String context,
    final Document citizenData) throws AppException, InformationalException {

    final Element person = getPrimaryNode(citizenData, citizen);
  }

  private Element getPrimaryNode(final Document citizenData,
    final Citizen citizen) throws AppException, InformationalException {

    Element retval = null;
    boolean foundPrimary = false;
    try {
      final XPath handlerExpression =
        new XPath("//Person[@isPrimaryParticipant='true']");
      final List nodes = handlerExpression.selectNodes(citizenData);
      if (!nodes.isEmpty()) {
        foundPrimary = true;
        retval = (Element) nodes.get(0);
      }
    } catch (final DOMException e) {
      final AppException err =
        CITIZENDATAHUBExceptionCreator.ERR_EVIDENCE_READ_FAILURE(
          Long.valueOf(Long.parseLong(citizen.getIdentifier())));
      err.initCause(e);
      throw err;
    }
    if (foundPrimary) {
      retval = setPersonEntityAttributes(retval, citizenData, citizen);
    } else {
      retval = new Element("Person");
      retval = setPersonEntityAttributes(retval, citizenData, citizen);
    }
    return retval;
  }

  /**
   *
   * @param personEl
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private Element setPersonEntityAttributes(Element personEl,
    final Document citizenData, final Citizen citizen)
    throws AppException, InformationalException {

    final ConcernRole primaryConcernRole = this.concernRoleDAO
      .get(Long.valueOf(Long.parseLong(citizen.getIdentifier())));
    personEl.setAttribute(
      BDMLifeEventDatastoreConstants.IS_PRIMARY_PARTICIPANT, "true");
    personEl.setAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID,
      primaryConcernRole.getID().toString());
    if (!primaryConcernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON))
      return personEl;
    final Long concernRoleID = primaryConcernRole.getID();
    final Person person = this.personDAO.get(primaryConcernRole.getID());
    personEl.setAttribute(BDMLifeEventDatastoreConstants.PERSON_ID,
      person.getID().toString());
    personEl.setAttribute(BDMLifeEventDatastoreConstants.TODAY,
      DateTimeTools.formatDateISO(Date.getCurrentDate()));

    final BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil =
      new BDMLifeEventCustomProcessorUtil();

    final ActiveCasesConcernRoleIDAndTypeKey activeCasesConcernRoleIDAndTypeKey =
      new ActiveCasesConcernRoleIDAndTypeKey();
    activeCasesConcernRoleIDAndTypeKey.dtls.concernRoleID = concernRoleID;
    activeCasesConcernRoleIDAndTypeKey.dtls.caseTypeCode =
      CASETYPECODE.INTEGRATEDCASE;
    activeCasesConcernRoleIDAndTypeKey.dtls.statusCode = CASESTATUS.OPEN;
    try {
      // search for all the programs (integrated case).Only displays programs
      // that have at least one active benefit.
      final IntegratedCase integratedcase =
        IntegratedCaseFactory.newInstance();
      IntegratedCaseIDKey integratedCaseIDKey = null;

      final List<curam.piwrapper.caseheader.impl.CaseHeader> clientCaseList =
        this.caseHeaderDAO.searchByParticipant(primaryConcernRole);

      for (final curam.piwrapper.caseheader.impl.CaseHeader caseDetails : clientCaseList) {

        if (caseDetails.getCaseType().equals(CASETYPECODEEntry.INTEGRATEDCASE)
          && !caseDetails.getStatus().equals(CASESTATUSEntry.CLOSED)) {
          boolean existsActiveBenefts = false;
          integratedCaseIDKey = new IntegratedCaseIDKey();
          integratedCaseIDKey.caseID = caseDetails.getID();
          final ListICProductDeliveryDetails listICProductDeliveryDetails =
            integratedcase.listProduct(integratedCaseIDKey);
          for (final ICProductDeliveryDetails icProductDeliveryDetails : listICProductDeliveryDetails.dtls) {
            if (icProductDeliveryDetails.statusCode
              .equals(CASESTATUS.ACTIVE)) {
              existsActiveBenefts = true;
              break;
            }
          }
          if (existsActiveBenefts) {
            // when existing active product delivery case, add the Program
            // IC caseID to the program entity
            personEl.setAttribute(
              BDMLifeEventDatastoreConstants.PERSON_ACTIVE_PROGRAMS, "true");
            final Element programsEntity =
              new Element(BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY);
            programsEntity.setAttribute(
              BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_NAME,
              caseDetails.getAdminCaseConfiguration()
                .getCaseConfigurationName());
            programsEntity.setAttribute(
              BDMLifeEventDatastoreConstants.PROGRAM_CASE_ID,
              String.valueOf(caseDetails.getID()));
            programsEntity.setAttribute(
              BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED,
              "false");
            personEl.addContent(programsEntity);
          }
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
    // Get tax withholds from dyn evidence
    personEl = bdmLifeEventCustomProcessorUtil.getTaxWithholds(concernRoleID,
      personEl);
    return personEl;
  }
}
