package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.citizen.datahub.impl.Citizen;
import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizenworkspace.message.impl.CITIZENDATAHUBExceptionCreator;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.workspaceservices.util.impl.LogHelper;
import java.util.List;

public class BDMLifeEventPersonalViewProcessor
  implements CustomViewProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleAddressDAO concernRoleAddressDAO;

  @Inject
  LogHelper logHelper;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  @Inject
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  protected BDMLifeEventPersonalViewProcessor() {

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
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private Element setPersonEntityAttributes(Element personEl,
    final Document citizenData, final Citizen citizen)
    throws AppException, InformationalException {

    final ConcernRole primaryConcernRole = concernRoleDAO
      .get(Long.valueOf(Long.parseLong(citizen.getIdentifier())));
    personEl.setAttribute("isPrimaryParticipant", "true");
    personEl.setAttribute("localID", primaryConcernRole.getID().toString());
    if (!primaryConcernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON))
      return personEl;

    final Long concernRoleID = primaryConcernRole.getID();
    final Person person = this.personDAO.get(primaryConcernRole.getID());
    personEl.setAttribute("personID", person.getID().toString());

    final BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil =
      new BDMLifeEventCustomProcessorUtil();

    // Get DOB and evidenceID from dyn evidence
    personEl = bdmLifeEventCustomProcessorUtil
      .getDOBValueObjectForConcernRoleID(concernRoleID, personEl);

    // Get SIN and evidenceID from dyn evidence
    personEl = bdmLifeEventCustomProcessorUtil
      .getSINValueObjectForConcernRoleID(concernRoleID, personEl);

    // Get firstName, middlename, lastname and evidenceID from dyn evidence
    personEl = bdmLifeEventCustomProcessorUtil
      .getNameValueObjectForConcernRoleID(concernRoleID, personEl);

    // Get preferred name and evidenceID from dyn evidence
    personEl = bdmLifeEventCustomProcessorUtil.getPreferredName(concernRoleID,
      personEl);

    return personEl;
  }

}
