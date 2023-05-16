package curam.ca.gc.bdm.citizen.datahub.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.codetable.BDMDEDMETHOD;
import curam.ca.gc.bdm.entity.financial.fact.BDMCodeTableComboFactory;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableDetails;
import curam.ca.gc.bdm.entity.financial.struct.BDMReadGovernCodeBySubOrdTableKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMDateofBirthEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIdentificationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMIncarcerationEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMNamesEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMVoluntaryTaxWithholdEvidenceVO;
import curam.codetable.ALTERNATENAMETYPE;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.codetable.IEGYESNO;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.xpath.XPath;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.EvidenceCaseKey;
import curam.core.sl.struct.EvidenceKey;
import curam.core.sl.struct.EvidenceTypeKey;
import curam.core.struct.CaseByConcernRoleIDStatusAndTypeKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderDtlsList;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.DatastoreFactory;
import curam.datastore.impl.Entity;
import curam.datastore.impl.NoSuchSchemaException;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.sl.impl.EvidenceGenericSLFactory;
import curam.dynamicevidence.sl.impl.EvidenceServiceInterface;
import curam.dynamicevidence.sl.struct.impl.ReadEvidenceDetails;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.facade.struct.PDCEvidenceDetails;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.Money;
import curam.workspaceservices.util.impl.DateTimeTools;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * Util Class to implement generic View and Update Processor logic for BDM
 * Project
 *
 *
 */
public class BDMLifeEventCustomProcessorUtil {

  @Inject
  private EvidenceTypeDefDAO etDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO etVerDefDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  CaseHeaderDAO caseHeaderDAO;

  @Inject
  BDMDateofBirthEvidence bdmDateofBirthEvidence;

  @Inject
  BDMIdentificationEvidence bdmIdentificationEvidence;

  @Inject
  BDMNamesEvidence bdmNamesEvidence;

  @Inject
  BDMPhoneEvidence bdmPhoneEvidence;

  @Inject
  BDMEmailEvidence bdmEmailEvidence;

  @Inject
  BDMVoluntaryTaxWithholdEvidence bdmVoluntaryTaxWithholdEvidence;

  @Inject
  BDMIncarcerationEvidence bdmIncarcerationEvidence;

  public BDMLifeEventCustomProcessorUtil() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  /**
   * This method will return the PARTICIPANTDATACASE caseID based on
   * participantRoleID and case type.
   *
   * @param participantRoleID
   * @return caseID
   *
   * @throws AppException
   * - Generic Curam Exception
   * @throws InformationalException
   * - Generic Curam Exception
   */
  public static long getParticipantDataCaseID(final long participantRoleID)
    throws AppException, InformationalException {

    final CaseByConcernRoleIDStatusAndTypeKey caseByConcernRoleIDStatusAndTypeKey =
      new CaseByConcernRoleIDStatusAndTypeKey();
    caseByConcernRoleIDStatusAndTypeKey.caseTypeCode =
      CASETYPECODE.PARTICIPANTDATACASE;
    caseByConcernRoleIDStatusAndTypeKey.concernRoleID = participantRoleID;
    caseByConcernRoleIDStatusAndTypeKey.statusCode = CASESTATUS.OPEN;

    final CaseHeaderDtlsList caseHeaderDtlsList =
      CaseHeaderFactory.newInstance().searchByConcernRoleIDStatusAndType(
        caseByConcernRoleIDStatusAndTypeKey);
    for (final CaseHeaderDtls caseHeaderDtls : caseHeaderDtlsList.dtls) {

      return caseHeaderDtls.caseID;
    }

    return 0l;
  }

  /**
   * gets the dynamic evidence details
   *
   * @param pdcEvidenceDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static DynamicEvidenceDataDetails
    getDynamicEvidenceDataDetails(final PDCEvidenceDetails pdcEvidenceDetails)
      throws AppException, InformationalException {

    final ReadEvidenceDetails readEvidenceDetails =
      readPDCEvidence(pdcEvidenceDetails);
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      readEvidenceDetails.dtls;
    return dynamicEvidenceDataDetails;
  }

  /**
   * retrieves evidence details
   *
   * @param pdcEvidenceDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static ReadEvidenceDetails
    readPDCEvidence(final PDCEvidenceDetails pdcEvidenceDetails)
      throws AppException, InformationalException {

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();

    evidenceTypeKey.evidenceType = pdcEvidenceDetails.evidenceType;

    final EvidenceServiceInterface evidenceServiceInterface =
      EvidenceGenericSLFactory.instance(evidenceTypeKey,
        Date.getCurrentDate());

    final EvidenceKey evidenceKey = new EvidenceKey();
    evidenceKey.evidenceID = pdcEvidenceDetails.evidenceID;
    evidenceKey.evType = pdcEvidenceDetails.evidenceType;

    final CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = pdcEvidenceDetails.caseID;

    final EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
    evidenceCaseKey.caseIDKey = caseIDKey;
    evidenceCaseKey.evidenceKey = evidenceKey;

    final ReadEvidenceDetails readEvidenceDetails =
      evidenceServiceInterface.readEvidence(evidenceCaseKey);

    return readEvidenceDetails;
  }

  /**
   * Get datastore update type for the passed entity
   *
   * @param datastoreDifferenceXML
   * @param entityType
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public static String getDataStoreUpdateTypeForEntity(
    final Document datastoreDifferenceXML, final String entityType)
    throws AppException, InformationalException {

    Element datastoreElement = null;
    final String datastoreChangeType = "";
    boolean foundPrimary = false;

    try {
      final XPath handlerExpression =
        new XPath("//Person[@isPrimaryParticipant='true']");
      final List nodes =
        handlerExpression.selectNodes(datastoreDifferenceXML);
      if (!nodes.isEmpty()) {
        foundPrimary = true;
        datastoreElement = (Element) nodes.get(0);
      }
    } catch (final DOMException e) {
    }
    if (foundPrimary) {
    } else {
    }

    return datastoreChangeType;
  }

  /**
   * Get DOB and evidenceID from dyn evidence
   *
   * @param concernRoleID
   * @param personEl
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Element getDOBValueObjectForConcernRoleID(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    final List<BDMDateofBirthEvidenceVO> bdmDateofBirthEvidenceVOList =
      bdmDateofBirthEvidence.getDOBEvidenceValueObject(concernRoleID);
    if (!bdmDateofBirthEvidenceVOList.isEmpty()) {
      for (final BDMDateofBirthEvidenceVO bdmDateofBirthEvidenceVO : bdmDateofBirthEvidenceVOList) {
        // Get last name at birth, parents last name at birth, DOB and
        // evidenceID from dyn evidence
        final String lastNameAtBirth =
          bdmDateofBirthEvidenceVO.getBirthLastName();
        final String parentsLastNameAtBirth =
          bdmDateofBirthEvidenceVO.getMothersBirthLastName();
        final Date dateOfBirth = bdmDateofBirthEvidenceVO.getDateOfBirth();
        final Long dobEvidenceID = bdmDateofBirthEvidenceVO.getEvidenceID();
        // Set last name at birth, parents last name at birth, DOB and
        // evidenceID into datastore
        personEl.setAttribute(
          BDMLifeEventDatastoreConstants.LAST_NAME_AT_BIRTH, lastNameAtBirth);
        personEl.setAttribute(
          BDMLifeEventDatastoreConstants.PARENT_LAST_NAME_AT_BIRTH,
          parentsLastNameAtBirth);
        personEl.setAttribute(BDMLifeEventDatastoreConstants.DATE_OF_BIRTH,
          DateTimeTools.formatDateISO(dateOfBirth));
        personEl.setAttribute(BDMLifeEventDatastoreConstants.DOB_EVIDENCE_ID,
          dobEvidenceID.toString());
        // Take only first dob evidence in List and ignore rest
        break;
      }
    }
    return personEl;
  }

  /**
   * Get SIN and evidenceID from dyn evidence
   *
   * @param concernRoleID
   * @param personEl
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Element getSINValueObjectForConcernRoleID(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    final BDMIdentificationEvidenceVO bdmIdentificationEvidenceVO =
      bdmIdentificationEvidence.getSINEvidenceValueObject(concernRoleID);
    final Long sinEvidenceID = bdmIdentificationEvidenceVO.getEvidenceID();
    final String sin = bdmIdentificationEvidenceVO.getAlternateID();
    // Set SIN and evidenceID into datastore
    if (sin != null) {
      personEl.setAttribute(BDMLifeEventDatastoreConstants.SIN, sin);
    } else {
      personEl.setAttribute(BDMLifeEventDatastoreConstants.SIN, "");
    }
    personEl.setAttribute(BDMLifeEventDatastoreConstants.SIN_EVIDENCE_ID,
      sinEvidenceID.toString());
    return personEl;
  }

  /**
   * Get firstName, middlename, lastname and evidenceID from dyn evidence
   *
   * @param concernRoleID
   * @param personEl
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public Element getNameValueObjectForConcernRoleID(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);
    if (!bdmNamesEvidenceVOList.isEmpty()) {
      for (final BDMNamesEvidenceVO bdmNamesEvidenceVO : bdmNamesEvidenceVOList) {
        if (bdmNamesEvidenceVO.getNameType()
          .equals(ALTERNATENAMETYPE.REGISTERED)) {
          // Get first, middle, and last name and evidenceID from dyn evidence
          final String firstname = bdmNamesEvidenceVO.getFirstName();
          final String middlename = bdmNamesEvidenceVO.getMiddleName();
          final String lastname = bdmNamesEvidenceVO.getLastName();
          final Long nameEvidenceID = bdmNamesEvidenceVO.getEvidenceID();
          // Set firstName, middlename, lastname and evidenceID into datastore
          personEl.setAttribute(BDMLifeEventDatastoreConstants.FIRST_NAME,
            firstname);
          personEl.setAttribute(BDMLifeEventDatastoreConstants.MIDDLE_NAME,
            middlename == null ? "" : middlename);
          personEl.setAttribute(BDMLifeEventDatastoreConstants.LAST_NAME,
            lastname);
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.NAME_EVIDENCE_ID,
            nameEvidenceID.toString());
          // Take only first name evidence in List and ignore rest
          break;
        }
      }
    }
    return personEl;
  }

  /**
   * Gets the preferred name info for the person
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public Element getPreferredName(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    final List<BDMNamesEvidenceVO> bdmNamesEvidenceVOList =
      bdmNamesEvidence.getNamesEvidenceValueObject(concernRoleID);
    if (!bdmNamesEvidenceVOList.isEmpty()) {
      for (final BDMNamesEvidenceVO bdmNamesEvidenceVO : bdmNamesEvidenceVOList) {
        if (bdmNamesEvidenceVO.getNameType()
          .equals(ALTERNATENAMETYPE.PREFERRED)) {
          final String prefFirstname = bdmNamesEvidenceVO.getFirstName();
          final Long prefNameEvidenceID = bdmNamesEvidenceVO.getEvidenceID();

          try {
            personEl.setAttribute(
              BDMLifeEventDatastoreConstants.PREFERRED_NAME, prefFirstname);
          } catch (final Exception e) {
            personEl.setAttribute(
              BDMLifeEventDatastoreConstants.PREFERRED_NAME, "");
          }
          personEl.setAttribute(
            BDMLifeEventDatastoreConstants.PREF_NAME_EVIDENCE_ID,
            prefNameEvidenceID.toString());
          return personEl;
        }
      }
    }
    return personEl;
  }

  /**
   * Gets the phone number info for the person
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public Element getPhoneNumbers(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {
    /*
     * Get phone number info from DB and populate into datastore
     * "phoneType" type="CT_PHONETYPE"
     * "countryCode" type="CT_COUNTRYCODE" default="BDMPC80035"
     * "phoneAreaCode" type="BDM_PHONE_AREA_CODE"
     * "phoneNumber" type="BDM_PHONE_NUMBER" default=""
     * "phoneExt" type="BDM_PHONE_EXT_CODE"
     * "isMor" type="IEG_BOOLEAN"
     * "isAftr" type="IEG_BOOLEAN"
     * "isEve" type="IEG_BOOLEAN"
     * "isPreferredCommunication" type="IEG_YES_NO"
     * "receiveAlerts" type="IEG_YES_NO"
     * "communicationDateChange" type="IEG_DATE"
     * "phoneNumberDesc" type="IEG_STRING"
     * "phoneNumberSeleced" type="IEG_BOOLEAN"
     * "alertFrequency" type="CT_ALERTOCCUR"
     */

    final List<BDMPhoneEvidenceVO> phoneEvidenceList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    final ListIterator<BDMPhoneEvidenceVO> listIter =
      phoneEvidenceList.listIterator();
    while (listIter.hasNext()) {
      final BDMPhoneEvidenceVO phoneEvidenceVO = listIter.next();
      // only retrieve phone number with "from date" is equal to today or in
      // the past; "to date" is blank or in the future
      if (!phoneEvidenceVO.getFromDate().after(Date.getCurrentDate())
        && (phoneEvidenceVO.getToDate() == null
          || phoneEvidenceVO.getToDate().isZero()
          || phoneEvidenceVO.getFromDate().after(Date.getCurrentDate()))) {
        final Element communicationDetails =
          new Element("CommunicationDetails");

        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_EVIDENCE_ID,
          Long.toString(phoneEvidenceVO.getEvidenceID()));
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.COUNTRY_CODE,
          phoneEvidenceVO.getPhoneCountryCode());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_TYPE,
          phoneEvidenceVO.getPhoneType());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_AREA_CODE,
          phoneEvidenceVO.getPhoneAreaCode());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_NUMBER,
          phoneEvidenceVO.getPhoneNumber());
        final String phoneExt = phoneEvidenceVO.getPhoneExtension();
        if (phoneExt != null) {
          communicationDetails
            .setAttribute(BDMLifeEventDatastoreConstants.PHONE_EXT, phoneExt);
        } else {
          communicationDetails
            .setAttribute(BDMLifeEventDatastoreConstants.PHONE_EXT, "");
        }
        if (phoneEvidenceVO.isPreferredInd()) {
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
            IEGYESNO.YES);
        } else {
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
            IEGYESNO.NO);
        }

        final StringBuffer phoneNumberDesc = new StringBuffer();
        phoneNumberDesc.append(phoneEvidenceVO.getPhoneAreaCode());
        phoneNumberDesc.append(CuramConst.gkDash);
        phoneNumberDesc.append(phoneEvidenceVO.getPhoneNumber());
        if (phoneEvidenceVO.isUseForAlertsInd()) {
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.YES);
          phoneNumberDesc.append(CuramConst.gkSpace);
          phoneNumberDesc.append("(You are receiving alerts to this number)");
        } else {
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.NO);
        }

        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_NUMBER_DESC,
          phoneNumberDesc.toString());

        final Boolean isMorningInd = phoneEvidenceVO.isMorningInd();
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_MOR,
          isMorningInd.toString());

        final Boolean isAfternoonInd = phoneEvidenceVO.isAfternoonInd();
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_AFTNOON,
          isAfternoonInd.toString());

        final Boolean isEveningInd = phoneEvidenceVO.isEveningInd();
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_IS_EVE,
          isEveningInd.toString());
        communicationDetails.setAttribute(
          BDMLifeEventDatastoreConstants.PHONE_NUMBER_SELECTED, "false");

        final String alertFrequency = phoneEvidenceVO.getAlertFrequency();
        if (alertFrequency != null) {
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.ALERT_FREQUENCY, alertFrequency);
        } else {
          communicationDetails
            .setAttribute(BDMLifeEventDatastoreConstants.ALERT_FREQUENCY, "");
        }
        personEl.addContent(communicationDetails);
      }
    }
    return personEl;
  }

  /**
   * Gets the email info for the person
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public Element getEmails(final long concernRoleID, final Element personEl)
    throws AppException, InformationalException {

    // Get email dyn evidence list
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmEmailEvidenceVOList.isEmpty()) {
      for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
        // only retrieve email address with "from date" is equal to today or in
        // the past; "to date" is blank or in the future
        if (!bdmEmailEvidenceVO.getFromDate().after(Date.getCurrentDate())
          && (bdmEmailEvidenceVO.getToDate() == null
            || bdmEmailEvidenceVO.getToDate().isZero() || bdmEmailEvidenceVO
              .getFromDate().after(Date.getCurrentDate()))) {
          // Get email and evidenceID dyn evidence
          Element communicationDetails = null;
          communicationDetails = new Element("CommunicationDetails");
          // Get email and evidenceID from dyn evidence
          final String emailAdr = bdmEmailEvidenceVO.getEmailAddress();
          final String emailType = bdmEmailEvidenceVO.getEmailAddressType();
          final Boolean isPreferredCommunication =
            bdmEmailEvidenceVO.isPreferredInd();
          // add alertFrequency attribute
          final String alertFrequency =
            bdmEmailEvidenceVO.getAlertFrequency();
          final Long emailEvidenceID = bdmEmailEvidenceVO.getEvidenceID();

          // Set email and evidenceID into datastore
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_ADDRESS_VALID, IEGYESNO.YES);
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_ADDRESS, emailAdr);
          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_TYPE, emailType);
          if (isPreferredCommunication) {
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
              IEGYESNO.YES);
          } else {
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.IS_PREFERRED_COMMUNICATION,
              IEGYESNO.NO);
          }

          final StringBuffer emailAddressDesc = new StringBuffer();
          emailAddressDesc.append(bdmEmailEvidenceVO.getEmailAddress());
          if (bdmEmailEvidenceVO.isUseForAlertsInd()) {
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.YES);
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL, IEGYESNO.YES);
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.ALERT_FREQUENCY, alertFrequency);
            emailAddressDesc.append(CuramConst.gkSpace);
            emailAddressDesc
              .append("(You are receiving alerts to this number)");
          } else {
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.RECEIVE_ALERT, IEGYESNO.NO);
            communicationDetails.setAttribute(
              BDMLifeEventDatastoreConstants.ALT_PREF_EMAIL, IEGYESNO.NO);
          }

          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_ADDRESS_DESC,
            emailAddressDesc.toString());

          communicationDetails.setAttribute(
            BDMLifeEventDatastoreConstants.EMAIL_EVIDENCE_ID,
            emailEvidenceID.toString());
          // add email to datastore
          personEl.addContent(communicationDetails);
        }
      }
    }
    return personEl;
  }

  /**
   * Check whether there exists email receiving alert
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public boolean existEmailReceivingAlert(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    // Get email dyn evidence list
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      bdmEmailEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmEmailEvidenceVOList.isEmpty()) {
      for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
        if (bdmEmailEvidenceVO.isUseForAlertsInd())
          return true;
      }
    }
    return false;
  }

  /**
   * Check whether there exists text receiving alert
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public boolean existTextReceivingAlert(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    // Get phone dyn evidence list
    final List<BDMPhoneEvidenceVO> bdmPhoneEvidenceVOList =
      bdmPhoneEvidence.getEvidenceValueObject(concernRoleID);
    if (!bdmPhoneEvidenceVOList.isEmpty()) {
      for (final BDMPhoneEvidenceVO bdmPhoneEvidenceVO : bdmPhoneEvidenceVOList) {
        if (bdmPhoneEvidenceVO.isUseForAlertsInd())
          return true;
      }
    }
    return false;
  }

  /**
   * Gets the incarceration info for the person
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public Element getIncarcerations(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    /*
     * Get incarceration info from DB and populate into datastore
     * "incarQuestion" type="IEG_YES_NO"
     * "instName" type="IEG_STRING"
     * "startDateInca" type="IEG_DATE"
     * "endDateInca" type="IEG_DATE"
     *
     * "localID" type="IEG_STRING" default=""
     * "curamDataStoreUniqueID" type="IEG_STRING" default=""
     * "incarcerationEvidenceID" type="IEG_STRING" default=""
     * "incarcerationPeriodDesc" type="IEG_STRING" default=""
     * "incarcerationSelected" type="IEG_BOOLEAN" default="false"
     */

    final List<BDMIncarcerationEvidenceVO> incarcerationEvidenceList =
      bdmIncarcerationEvidence.getEvidenceValueObject(concernRoleID);
    final ListIterator<BDMIncarcerationEvidenceVO> listIter =
      incarcerationEvidenceList.listIterator();
    while (listIter.hasNext()) {
      final BDMIncarcerationEvidenceVO incarcerationEvidenceVO =
        listIter.next();
      final Element incarceration = new Element("Incarceration");
      incarceration.setAttribute(
        BDMLifeEventDatastoreConstants.INCARCERATION_EVIDENCE_ID,
        Long.toString(incarcerationEvidenceVO.getEvidenceID()));
      incarceration.setAttribute(
        BDMLifeEventDatastoreConstants.INCARCERATION_Name,
        incarcerationEvidenceVO.getInstitutionName());
      incarceration.setAttribute(
        BDMLifeEventDatastoreConstants.INCARCERATION_START_DATE,
        DateTimeTools.formatDateISO(incarcerationEvidenceVO.getStartDate()));
      if (incarcerationEvidenceVO.getEndDate().isZero()) {
        incarceration.setAttribute(
          BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE, "");
      } else {
        incarceration.setAttribute(
          BDMLifeEventDatastoreConstants.INCARCERATION_END_DATE,
          DateTimeTools.formatDateISO(incarcerationEvidenceVO.getEndDate()));
      }
      incarceration.setAttribute(
        BDMLifeEventDatastoreConstants.INCARCERATION_SELECTED, "false");
      final StringBuffer incarcerationPeriodDesc = new StringBuffer();
      incarcerationPeriodDesc
        .append(incarcerationEvidenceVO.getInstitutionName());
      incarcerationPeriodDesc.append(CuramConst.gkSpace);
      incarcerationPeriodDesc.append(CuramConst.gkRoundOpeningBracket);
      incarcerationPeriodDesc
        .append(curam.util.resources.Locale.getFormattedDate(
          incarcerationEvidenceVO.getStartDate(), BDMConstants.MMMM_d_YYYY));
      incarcerationPeriodDesc.append(CuramConst.gkSpace);
      incarcerationPeriodDesc.append(CuramConst.gkDash);
      if (!incarcerationEvidenceVO.getEndDate().isZero()) {
        incarcerationPeriodDesc.append(CuramConst.gkSpace);
        incarcerationPeriodDesc
          .append(curam.util.resources.Locale.getFormattedDate(
            incarcerationEvidenceVO.getEndDate(), BDMConstants.MMMM_d_YYYY));
      }
      incarcerationPeriodDesc.append(CuramConst.gkSpace);
      incarcerationPeriodDesc.append(CuramConst.gkRoundClosingBracket);
      incarceration.setAttribute(
        BDMLifeEventDatastoreConstants.INCARCERATION_PERIOD__DESC,
        incarcerationPeriodDesc.toString());

      personEl.addContent(incarceration);
    }
    return personEl;
  }

  /**
   * Gets the tax withhold info for the person
   *
   * @param concernRoleID
   * @param personEl
   * @throws AppException
   * @throws InformationalException
   */
  public Element getTaxWithholds(final long concernRoleID,
    final Element personEl) throws AppException, InformationalException {

    final List<BDMVoluntaryTaxWithholdEvidenceVO> bdmVoluntaryTaxWithholdEvidenceVOList =
      bdmVoluntaryTaxWithholdEvidence
        .getVoluntaryTaxWithholdEvidenceValueObjectList(concernRoleID);
    final ListIterator<BDMVoluntaryTaxWithholdEvidenceVO> listIter =
      bdmVoluntaryTaxWithholdEvidenceVOList.listIterator();
    while (listIter.hasNext()) {
      final BDMVoluntaryTaxWithholdEvidenceVO bdmVoluntaryTaxWithholdEvidenceVO =
        listIter.next();
      personEl.setAttribute(
        BDMLifeEventDatastoreConstants.TAX_ACTIVE_TAX_WITHHOLDS, "true");
      final Element taxInfo =
        new Element(BDMLifeEventDatastoreConstants.TAX_INFO);
      if (bdmVoluntaryTaxWithholdEvidenceVO.getEndDate() == null
        || bdmVoluntaryTaxWithholdEvidenceVO.getEndDate().isZero()) {
        taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE, "");
      } else if (bdmVoluntaryTaxWithholdEvidenceVO.getEndDate()
        .before(Date.getCurrentDate())) {
        // Dont add not active Tax Withholds
        continue;
      } else {
        taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_END_DATE,
          DateTimeTools
            .formatDateISO(bdmVoluntaryTaxWithholdEvidenceVO.getEndDate()));
      }
      taxInfo.setAttribute(
        BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_EVIDENCE_ID,
        Long.toString(bdmVoluntaryTaxWithholdEvidenceVO.getEvidenceID()));
      taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_DED_METHOD,
        bdmVoluntaryTaxWithholdEvidenceVO.getVoluntaryTaxWithholdType());
      taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_START_DATE,
        DateTimeTools
          .formatDateISO(bdmVoluntaryTaxWithholdEvidenceVO.getStartDate()));
      taxInfo.setAttribute(
        BDMLifeEventDatastoreConstants.TAX_ORIGINAL_START_DATE, DateTimeTools
          .formatDateISO(bdmVoluntaryTaxWithholdEvidenceVO.getStartDate()));
      taxInfo.setAttribute(
        BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_SELECTED, "false");

      boolean isPercentage = false;
      final StringBuffer taxInfoPeriodDesc = new StringBuffer();
      if (bdmVoluntaryTaxWithholdEvidenceVO.getAmount() != null) {
        final Double amountNumber =
          Double.valueOf(bdmVoluntaryTaxWithholdEvidenceVO.getAmount());
        if (amountNumber != null && amountNumber == 0) {
          // Account for case that withhold is created on SPM side (it comes
          // in as 0.0)
          isPercentage = true;
        } else {
          taxInfoPeriodDesc.append(CuramConst.gkDollar);
          final Money amount =
            new Money(bdmVoluntaryTaxWithholdEvidenceVO.getAmount());
          taxInfoPeriodDesc.append(amount.toString());
          taxInfo.setAttribute(
            BDMLifeEventDatastoreConstants.TAX_DOLLAR_AMOUNT,
            amount.toString());
          taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_DED_METHOD,
            BDMDEDMETHOD.DOLLAR);
        }
      } else {
        isPercentage = true;
      }
      if (isPercentage) {
        taxInfoPeriodDesc
          .append(bdmVoluntaryTaxWithholdEvidenceVO.getPercentageValue());
        taxInfoPeriodDesc.append(CuramConst.gkPercentage);
        taxInfo.setAttribute(
          BDMLifeEventDatastoreConstants.TAX_DOLLAR_PERCENT,
          bdmVoluntaryTaxWithholdEvidenceVO.getPercentageValue());
        taxInfo.setAttribute(BDMLifeEventDatastoreConstants.TAX_DED_METHOD,
          BDMDEDMETHOD.PERCT);
      }
      taxInfoPeriodDesc.append(CuramConst.gkSpace);
      taxInfoPeriodDesc.append(CuramConst.gkRoundOpeningBracket);
      taxInfoPeriodDesc.append(curam.util.resources.Locale.getFormattedDate(
        bdmVoluntaryTaxWithholdEvidenceVO.getStartDate(),
        BDMConstants.MMMM_d_YYYY));
      taxInfoPeriodDesc.append(CuramConst.gkSpace);
      taxInfoPeriodDesc.append(CuramConst.gkDash);
      if (bdmVoluntaryTaxWithholdEvidenceVO.getEndDate() == null
        || bdmVoluntaryTaxWithholdEvidenceVO.getEndDate().isZero()) {
        taxInfoPeriodDesc.append(CuramConst.gkSpace);
        taxInfoPeriodDesc.append(CuramConst.gkRoundClosingBracket);
      } else {
        taxInfoPeriodDesc.append(CuramConst.gkSpace);
        taxInfoPeriodDesc.append(curam.util.resources.Locale.getFormattedDate(
          bdmVoluntaryTaxWithholdEvidenceVO.getEndDate(),
          BDMConstants.MMMM_d_YYYY));
        taxInfoPeriodDesc.append(CuramConst.gkRoundClosingBracket);
      }
      taxInfo.setAttribute(
        BDMLifeEventDatastoreConstants.TAX_TAX_WITHHOLD_PERIOD_DESC,
        taxInfoPeriodDesc.toString());
      personEl.addContent(taxInfo);
    }
    return personEl;

  }

  /**
   * Gets the benefit program type (BDMBenefitProgramType) selected in a life
   * event
   *
   * @param rootEntity
   * @throws AppException
   * @throws InformationalException
   */
  public String getSelectedBenefitProgramType(final Entity rootEntity)
    throws AppException, InformationalException, NoSuchSchemaException {

    long programCaseID = 0;
    final Datastore datastore = DatastoreFactory.newInstance()
      .openDatastore(BDMLifeEventDatastoreConstants.BDM_LIFE_EVENT_SCHEMA);
    final Entity personEntity = rootEntity.getChildEntities(
      datastore.getEntityType(BDMLifeEventDatastoreConstants.PERSON))[0];
    final ConcernRole primaryConcernRole =
      this.concernRoleDAO.get(Long.valueOf(Long.parseLong(
        personEntity.getAttribute(BDMLifeEventDatastoreConstants.LOCAL_ID))));

    final Entity[] programsEntities = personEntity.getChildEntities(datastore
      .getEntityType(BDMLifeEventDatastoreConstants.PROGRAMS_ENTITY));
    // find the programs to map to.
    for (final Entity programsEntity : programsEntities) {
      if (!StringUtil.isNullOrEmpty(programsEntity.getAttribute(
        BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED))) {
        final boolean activeProgramSelected =
          Boolean.parseBoolean(programsEntity.getAttribute(
            BDMLifeEventDatastoreConstants.PROGRAMS_ACTIVE_PROGRAM_SELECTED));
        if (activeProgramSelected) {
          if (!StringUtil.isNullOrEmpty(programsEntity
            .getAttribute(BDMLifeEventDatastoreConstants.PROGRAM_CASE_ID))) {
            programCaseID = Long.parseLong(programsEntity
              .getAttribute(BDMLifeEventDatastoreConstants.PROGRAM_CASE_ID));
          }
        }
      }
    }
    if (programCaseID != 0) {
      final List<curam.piwrapper.caseheader.impl.CaseHeader> clientCaseList =
        this.caseHeaderDAO.searchByParticipant(primaryConcernRole);

      for (final curam.piwrapper.caseheader.impl.CaseHeader caseDetails : clientCaseList) {

        if (caseDetails.getCaseType().equals(CASETYPECODEEntry.INTEGRATEDCASE)
          && caseDetails.getID() == programCaseID) {
          final BDMReadGovernCodeBySubOrdTableKey bdmReadGovernCodeBySubOrdTableKey =
            new BDMReadGovernCodeBySubOrdTableKey();
          bdmReadGovernCodeBySubOrdTableKey.governTableName =
            BDMBENEFITPROGRAMTYPE.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.recordStatusCode =
            RECORDSTATUS.NORMAL;
          bdmReadGovernCodeBySubOrdTableKey.subOrdTableName =
            PRODUCTCATEGORY.TABLENAME;
          bdmReadGovernCodeBySubOrdTableKey.subOrdCode =
            caseDetails.getIntegratedCaseType().getCode();
          final BDMReadGovernCodeBySubOrdTableDetails bdmReadGovernCodeBySubOrdTableDetails =
            BDMCodeTableComboFactory.newInstance()
              .readGovernCodeBySubOrdTable(bdmReadGovernCodeBySubOrdTableKey);
          return bdmReadGovernCodeBySubOrdTableDetails.governCode;
        }
      }
    }
    return "";
  }

}
