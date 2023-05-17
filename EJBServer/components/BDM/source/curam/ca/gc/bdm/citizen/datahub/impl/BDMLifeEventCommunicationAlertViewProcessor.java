package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
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

/**
 * @author vipresh.sharma
 * @since 9010 Life Events Update preference and alerts
 */
public class BDMLifeEventCommunicationAlertViewProcessor
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
  BDMPhoneEvidence bdmPhoneEvidence;

  @Inject
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  protected BDMLifeEventCommunicationAlertViewProcessor() {

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
  private Element setPersonEntityAttributes(final Element personEl,
    final Document citizenData, final Citizen citizen)
    throws AppException, InformationalException {

    final ConcernRole primaryConcernRole = this.concernRoleDAO
      .get(Long.valueOf(Long.parseLong(citizen.getIdentifier())));
    personEl.setAttribute("isPrimaryParticipant", "true");
    personEl.setAttribute("localID", primaryConcernRole.getID().toString());
    if (!primaryConcernRole.getConcernRoleType()
      .equals(CONCERNROLETYPEEntry.PERSON))
      return personEl;

    final Long concernRoleID = primaryConcernRole.getID();
    final Person person = this.personDAO.get(primaryConcernRole.getID());
    personEl.setAttribute("personID", person.getID().toString());

    // Get email dyn evidence list
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmEmailEvidenceVOList.isEmpty()) {
      for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
        // Get email and evidenceID dyn evidence
        Element communicationDetails = null;
        communicationDetails = new Element("CommunicationDetails");
        // Get email and evidenceID from dyn evidence
        final String emailAdr = bdmEmailEvidenceVO.getEmailAddress();
        final String emailType = bdmEmailEvidenceVO.getEmailAddressType();
        final Boolean isPreferredCommunication =
          bdmEmailEvidenceVO.isPreferredInd();
        final Boolean alertPrefEmail = bdmEmailEvidenceVO.isUseForAlertsInd();
        final Long emailEvidenceID = bdmEmailEvidenceVO.getEvidenceID();
        // Set last name at birth, parents last name at birth, DOB and
        // evidenceID into datastore
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.EMAIL_ADDRESS, emailAdr);
        communicationDetails
          .setAttribute(BDMLifeEventDatastoreConstants.EMAIL_TYPE, emailType);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
          isPreferredCommunication.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL,
          alertPrefEmail.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID,
          emailEvidenceID.toString());
        // add email to datastore
        personEl.addContent(communicationDetails);
      }
    }

    // Get final phone and evidenceID final from dyn evidence
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmPhoneEvidenceVOList.isEmpty()) {
      for (final BDMPhoneEvidenceVO bdmPhoneEvidenceVO : bdmPhoneEvidenceVOList) {
        // Get phone and evidenceID from datastore
        Element communicationDetails = null;
        communicationDetails = new Element("CommunicationDetails");
        // Get phone and evidenceID from dyn evidence
        final String phoneCountryCode =
          bdmPhoneEvidenceVO.getPhoneCountryCode();
        final String phoneAreaCode = bdmPhoneEvidenceVO.getPhoneAreaCode();
        final String phoneNumber = bdmPhoneEvidenceVO.getPhoneNumber();
        final String fullPhoneNumber = phoneAreaCode + "-" + phoneNumber;
        final String phoneExtension =
          bdmPhoneEvidenceVO.getPhoneExtension() == null ? ""
            : bdmPhoneEvidenceVO.getPhoneExtension();
        bdmPhoneEvidenceVO.getPhoneExtension();
        final String phoneType = bdmPhoneEvidenceVO.getPhoneType();
        final Boolean preferredInd = bdmPhoneEvidenceVO.isPreferredInd();
        final Boolean useForAlertsInd =
          bdmPhoneEvidenceVO.isUseForAlertsInd();
        final Boolean morningInd = bdmPhoneEvidenceVO.isMorningInd();
        final Boolean afternoonInd = bdmPhoneEvidenceVO.isAfternoonInd();
        final Boolean eveningInd = bdmPhoneEvidenceVO.isEveningInd();
        final Long evidenceID = bdmPhoneEvidenceVO.getEvidenceID();
        // Set phone and evidenceID into datastore
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.COUNTRY_CODE, phoneCountryCode);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_AREA_CODE, phoneAreaCode);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_NUMBER, phoneNumber);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.FULL_PHONE_NUMBER, fullPhoneNumber);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_EXT, phoneExtension);
        communicationDetails
          .setAttribute(BDMLifeEventDatastoreConstants.PHONE_TYPE, phoneType);
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
          preferredInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_ALT_PREF,
          useForAlertsInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_MOR, morningInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_AFTNOON,
          afternoonInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_EVE, eveningInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID,
          evidenceID.toString());
        // add phone to datastore
        personEl.addContent(communicationDetails);
      }
    }

    // Get final phone and evidenceID final from dyn evidence
    final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
      bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmContactPreferenceEvidenceVOList.isEmpty()) {
      for (final BDMContactPreferenceEvidenceVO bdmContactPreferenceEvidenceVO : bdmContactPreferenceEvidenceVOList) {
        // Get CommunicationPref from dyn evidence
        // CommunicationPref Entity
        Element communicationPref = null;
        communicationPref = new Element("CommunicationPref");
        final String recieveCorrespondPref =
          bdmContactPreferenceEvidenceVO.getPreferredCommunication();
        final String preferredLanguage =
          bdmContactPreferenceEvidenceVO.getPreferredLanguage();
        final String languageS =
          bdmContactPreferenceEvidenceVO.getPreferredOralLanguage();
        final String languageW =
          bdmContactPreferenceEvidenceVO.getPreferredWrittenLanguage();
        final Long communicationPrefEvidenceID =
          bdmContactPreferenceEvidenceVO.getEvidenceID();
        // Set CommunicationPref into datastore
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.RECEIVE_CORRESPONDENCE,
          recieveCorrespondPref);
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_SPOKEN_LANGUAGE,
          languageS);
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.PREFERRED_WRITTEN_LANGUAGE,
          languageW);
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.COMM_PREF_EVIDENCE_ID,
          communicationPrefEvidenceID.toString());
        communicationPref.setAttribute(
          BDMLifeEventDatastoreConstants.PARTICIPANT,
          Long.toString(bdmContactPreferenceEvidenceVO.getParticipant()));
        personEl.addContent(communicationPref);
      }
    }
    return personEl;
  }
}
