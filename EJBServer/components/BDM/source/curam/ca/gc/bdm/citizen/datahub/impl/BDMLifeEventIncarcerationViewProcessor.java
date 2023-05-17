package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.citizen.datahub.impl.Citizen;
import curam.citizen.datahub.impl.CustomViewProcessor;
import curam.citizenworkspace.message.impl.CITIZENDATAHUBExceptionCreator;
import curam.codetable.impl.CONCERNROLETYPEEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.fact.PersonFactory;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.piwrapper.participantmanager.impl.ConcernRoleAddressDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.type.NotFoundIndicator;
import curam.workspaceservices.util.impl.DateTimeTools;
import curam.workspaceservices.util.impl.LogHelper;
import java.util.List;

public class BDMLifeEventIncarcerationViewProcessor
  implements CustomViewProcessor {

  @Inject
  PersonDAO personDAO;

  @Inject
  ConcernRoleAddressDAO concernRoleAddressDAO;

  @Inject
  LogHelper logHelper;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  protected BDMLifeEventIncarcerationViewProcessor() {

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
      retval = setIncarcerationEntityAttributes(retval, citizenData, citizen);
    } else {
      retval = new Element("Person");
      retval = setIncarcerationEntityAttributes(retval, citizenData, citizen);
    }
    return retval;
  }

  /**
   * This method is used to populate the data store with person incarceration
   * details
   *
   * @param citizenData
   * @param citizen
   * @throws AppException
   * @throws InformationalException
   */
  private Element setIncarcerationEntityAttributes(Element retval,
    final Document citizenData, final Citizen citizen)
    throws AppException, InformationalException {

    final ConcernRole primaryConcernRole = this.concernRoleDAO
      .get(Long.valueOf(Long.parseLong(citizen.getIdentifier())));
    retval.setAttribute(BDMLifeEventDatastoreConstants.IS_PRIMARY_PARTICIPANT,
      "true");
    retval.setAttribute("localID", primaryConcernRole.getID().toString());
    if (!primaryConcernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON)) {
      return retval;
    }
    final Person person = this.personDAO.get(primaryConcernRole.getID());
    retval.setAttribute("personID", person.getID().toString());
    retval.setAttribute(BDMLifeEventDatastoreConstants.FULL_NAME,
      primaryConcernRole.getName());

    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

    // Get PersonDtls to see whether person exists
    final curam.core.intf.Person personObj = PersonFactory.newInstance();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = primaryConcernRole.getID();
    final PersonDtls personDtls =
      personObj.read(notFoundIndicator, personKey);
    retval.setAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH,
      DateTimeTools.formatDateISO(personDtls.dateOfBirth));

    if (notFoundIndicator.isNotFound()) {
      Trace.kTopLevelLogger
        .warn(BDMConstants.BDM_LOGS_PREFIX + "Person could not be found");
    } else {
      final BDMLifeEventCustomProcessorUtil bdmLifeEventCustomProcessorUtil =
        new BDMLifeEventCustomProcessorUtil();
      retval = bdmLifeEventCustomProcessorUtil
        .getIncarcerations(personKey.concernRoleID, retval);
    }

    return retval;
  }

}
