package curam.ca.gc.bdm.gcnotify.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMALERTOCCUR;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYALERTTYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYINTERFACETYPE;
import curam.ca.gc.bdm.codetable.BDMGCNOTIFYNOTIFICATIONSTATUS;
import curam.ca.gc.bdm.codetable.BDMGCNotifyTemplateType;
import curam.ca.gc.bdm.codetable.BDMLANGUAGE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.communication.impl.BDMPropertiesUtil;
import curam.ca.gc.bdm.entity.bdmgcnotify.fact.BDMGcNotifyRequestDataFactory;
import curam.ca.gc.bdm.entity.bdmgcnotify.intf.BDMGcNotifyRequestData;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyConcernRoleKey;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtls;
import curam.ca.gc.bdm.entity.bdmgcnotify.struct.BDMGcNotifyRequestDataDtlsList;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.BDMGCNotifyInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.gcnotify.impl.gcnotifypojos.BDMGCNotifyRealTimePersonalizeData;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.io.IOException;
import java.util.ArrayList;

public class BDMGCNotifyHelper {

  private static final String kEmail = "email";

  private static final String kSMS = "sms";

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  BDMGCNotifyInterfaceImpl bdmGCNotifyInterfaceImpl;

  final BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  BDMPropertiesUtil propUtil = new BDMPropertiesUtil();

  public BDMGCNotifyHelper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Create the API Staging Data record and inesrt to the table
   *
   * @param API Staging data details
   **/

  public long insertGCNotifyRequestData(
    final BDMGcNotifyRequestDataDtls gcNotifyReqDataDtls)
    throws AppException, InformationalException {

    final BDMGcNotifyRequestData gcNotifyReqData =
      BDMGcNotifyRequestDataFactory.newInstance();
    // assign new primary key
    gcNotifyReqDataDtls.recordID = UniqueIDFactory.newInstance().getNextID();

    // insert the data to API Staging for API Batch
    gcNotifyReqData.insert(gcNotifyReqDataDtls);
    // return reference ID
    return gcNotifyReqDataDtls.recordID;
  }

  /**
   * read and populate the alert frequency from evidence data and populate the
   * gc notify request data interface type as per the alert frequency. PERINFO
   * -> GC Notify RealTime
   * PERDAY -> GC Notify Batch
   *
   * @param DynamicEvidenceDataDetails
   * @param BDMGcNotifyRequestDataDtls
   **/
  public void populateAlertFrequency(
    final DynamicEvidenceDataDetails evidenceDataDetails,
    final BDMGcNotifyRequestDataDtls gcNotifyReqDataDtls) {

    final CodeTableItem alertFreq =
      (CodeTableItem) DynamicEvidenceTypeConverter.convert(evidenceDataDetails
        .getAttribute(BDMConstants.kEvidenceAttrAlertFrequency));
    if (alertFreq.code().equals(BDMALERTOCCUR.PERINFO)) {
      gcNotifyReqDataDtls.interfaceType =
        BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_REALTIME;
    } else if (alertFreq.code().equals(BDMALERTOCCUR.PERDAY)) {
      gcNotifyReqDataDtls.interfaceType =
        BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH;
    }
  }

  /**
   * read and populate the alert frequency from evidence data and populate the
   * gc notify request data interface type as per the alert frequency. PERINFO
   * -> GC Notify RealTime
   * PERDAY -> GC Notify Batch
   *
   * @param DynamicEvidenceDataDetails
   * @param BDMGcNotifyRequestDataDtls
   **/
  public BDMGcNotifyRequestDataDtlsList
    verifyInsertGCNotifyRequestDataForConcernRole(
      final BDMGcNotifyRequestDataDtls stagingDataDtls)
      throws AppException, InformationalException {

    final BDMGcNotifyRequestData gcNotifyReqData =
      BDMGcNotifyRequestDataFactory.newInstance();

    final BDMGcNotifyConcernRoleKey gcNotifyCRIDKey =
      new BDMGcNotifyConcernRoleKey();
    gcNotifyCRIDKey.concernRoleID = stagingDataDtls.concernRoleID;
    // insert the data to API Staging for API Batch
    final BDMGcNotifyRequestDataDtlsList gcNotifyReqDataList =
      gcNotifyReqData.readMultiByConcernRoleID(gcNotifyCRIDKey);
    for (final BDMGcNotifyRequestDataDtls gcNotifyReqDataDtls : gcNotifyReqDataList.dtls
      .items()) {
      if (Date.getCurrentDate().getDateTime()
        .before(gcNotifyReqDataDtls.creationDateTime)
        && stagingDataDtls.alertType.equals(gcNotifyReqDataDtls.alertType)
        && stagingDataDtls.interfaceType
          .equals(gcNotifyReqDataDtls.interfaceType)
        && stagingDataDtls.templateType
          .equals(gcNotifyReqDataDtls.templateType)) {
        continue;
      } else {
        gcNotifyReqDataList.dtls.remove(gcNotifyReqDataDtls);
      }
    }
    return gcNotifyReqDataList;
  }

  /**
   * Look up Concern ROle Email and Phone Evidence data to gather receive GC
   * Notify alert attribute
   *
   * @param correspondent ConcernRole ID
   *
   **/
  public ArrayList<DynamicEvidenceDataDetails>
    getReceiveAlertPreference(final Long correspondentConcernRoleID) {

    final ArrayList<DynamicEvidenceDataDetails> receiveAlertEvidenceList =
      new ArrayList<DynamicEvidenceDataDetails>();

    try {
      // read the contact preference for the email address
      final DynamicEvidenceDataDetails[] evidenceDataDetailsList =
        bdmCommHelper.getPersonEvidenceByType(correspondentConcernRoleID,
          PDCConst.PDCPHONENUMBER + CuramConst.gkPipeDelimiter
            + PDCConst.PDCEMAILADDRESS);

      // BEGIN TASK 16487 Modified For Notification Message
      for (final DynamicEvidenceDataDetails evidenceDataDetails : evidenceDataDetailsList) {
        // END TASK 16487 Modified For Notification Message
        final boolean receiveAlertInd =
          (Boolean) DynamicEvidenceTypeConverter.convert(evidenceDataDetails
            .getAttribute(BDMConstants.kEvidenceAttrUseForAlert));
        if (receiveAlertInd)
          receiveAlertEvidenceList.add(evidenceDataDetails);
      }
    } catch (final Exception e) {
    }
    return receiveAlertEvidenceList;
  }

  /**
   * Create the Alert request data for the realtime or once per day API data
   * based on the concernroleID and the notification property associated to the
   * GC Notify template ID
   *
   * @param correspondent ConcernRole ID
   * @param GC Notify Template Property Name
   **/
  public BDMGcNotifyRequestDataDtls createAlertData(
    final Long correspondentConcernRoleID, final String templateType,
    final DynamicEvidenceDataDetails receiveAlertEvid) {

    final BDMGcNotifyRequestDataDtls gcNotifyReqDataDtls =
      new BDMGcNotifyRequestDataDtls();

    gcNotifyReqDataDtls.metaDataID = CuramConst.kLongZero;
    gcNotifyReqDataDtls.concernRoleID = correspondentConcernRoleID;
    gcNotifyReqDataDtls.templateType = templateType;
    final ConcernRole concernRole =
      concernRoleDAO.get(correspondentConcernRoleID);
    gcNotifyReqDataDtls.fullName = concernRole.getName();
    gcNotifyReqDataDtls.alternateID = concernRole.getPrimaryAlternateID();
    gcNotifyReqDataDtls.creationDateTime = DateTime.getCurrentDateTime();

    try {
      // read the contact preference for written language
      gcNotifyReqDataDtls.language = bdmCommHelper
        .getPreferredWrittenLanguageCTI(correspondentConcernRoleID).code();
      // read the contact preference for the email address

      // BEGIN TASK 16487 Modified For Notification Message
      if (receiveAlertEvid.getEvidenceTypeVersionDefinition()
        .getEvidenceTypeDef().getEvidenceTypeCode()
        .equals(PDCConst.PDCEMAILADDRESS)) {
        // END TASK 16487 Modified For Notification Message

        // append the preferred Email address to the api request body
        final String email =
          (String) DynamicEvidenceTypeConverter.convert(receiveAlertEvid
            .getAttribute(BDMConstants.kEvidenceAttrEmailAddress));
        // populate the email address to the api request url and append the GC
        // Notify Email TemplateID
        gcNotifyReqDataDtls.alertType =
          BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_EMAIL;
        gcNotifyReqDataDtls.emailAddress = email;
        gcNotifyReqDataDtls.templateID = getEmailTemplateID(
          gcNotifyReqDataDtls.language, gcNotifyReqDataDtls.templateType);
        populateAlertFrequency(receiveAlertEvid, gcNotifyReqDataDtls);
        return gcNotifyReqDataDtls;
      } else if (receiveAlertEvid.getEvidenceTypeVersionDefinition()
        .getEvidenceTypeDef().getEvidenceTypeCode()
        .equals(PDCConst.PDCPHONENUMBER)) {
        // If not email is prefered then check the phone number preference

        // BEGIN TASK 16487 Modified For Notification Message

        final CodeTableItem phoneAreaCodeCT =
          (CodeTableItem) DynamicEvidenceTypeConverter
            .convert(receiveAlertEvid
              .getAttribute(BDMConstants.kEvidenceAttrPhoneCountryCode));
        final String phoneAreaCode = phoneAreaCodeCT.toString().substring(0,
          phoneAreaCodeCT.toString().indexOf(CuramConst.gkSpace));
        final String phoneNumberFull = phoneAreaCode
          + (String) DynamicEvidenceTypeConverter.convert(receiveAlertEvid
            .getAttribute(BDMConstants.kEvidenceAttrPhoneAreaCode))
          + (String) DynamicEvidenceTypeConverter.convert(receiveAlertEvid
            .getAttribute(BDMConstants.kEvidenceAttrPhoneNumber));
        gcNotifyReqDataDtls.alertType =
          BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE;
        gcNotifyReqDataDtls.phoneNumber = phoneNumberFull;
        // populate the phone number to the api request url and append the
        // GC Notify SMS TemplateID

        gcNotifyReqDataDtls.templateID = getSMSTemplateID(
          gcNotifyReqDataDtls.language, gcNotifyReqDataDtls.templateType);
        populateAlertFrequency(receiveAlertEvid, gcNotifyReqDataDtls);
      }

    } catch (final Exception e) {
    }
    return gcNotifyReqDataDtls;
  }

  /**
   * populate the template ID from the GC Notify SMS Template ID property for
   * the corresponding channel and the corresponding URL for the GC Notify
   * SMS channel
   * body
   *
   * @param API Staging data details
   **/
  public String getSMSTemplateID(final String language,
    final String templateType) throws AppException, InformationalException {

    String templateIDPropName = "";
    if (templateType.equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
      if (language.equals(BDMLANGUAGE.ENGLISHL)) {
        templateIDPropName =
          EnvVars.BDM_GCNOTIFY_ENGLISH_CORRESPONDENCE_SMS_TEMPLATE_ID;
      } else {
        templateIDPropName =
          EnvVars.BDM_GCNOTIFY_FRENCH_CORRESPONDENCE_SMS_TEMPLATE_ID;
      }
    } else if (templateType.equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
      if (language.equals(BDMLANGUAGE.ENGLISHL)) {
        templateIDPropName =
          EnvVars.BDM_GCNOTIFY_ENGLISH_NOTIFICATION_SMS_TEMPLATE_ID;
      } else {
        templateIDPropName =
          EnvVars.BDM_GCNOTIFY_FRENCH_NOTIFICATION_SMS_TEMPLATE_ID;
      }
    }

    final String gcNotifyTemplateID =
      Configuration.getProperty(templateIDPropName);

    return gcNotifyTemplateID;
  }

  /**
   * populate the template ID from the GC Notify Email Template ID property for
   * the corresponding channel and the corresponding URL for the GC Notify Email
   * channel then append the template to the api request
   * body
   *
   * @param API Staging data details
   **/
  public String getEmailTemplateID(final String language,
    final String templateType) throws AppException, InformationalException {

    // Read the list of property with the expected channel pattern
    // (Correspondence/notification)
    String templateID = "";
    if (templateType.equals(BDMGCNotifyTemplateType.BDM_GC_COR)) {
      if (language.equals(BDMLANGUAGE.ENGLISHL)) {
        templateID =
          EnvVars.BDM_GCNOTIFY_ENGLISH_CORRESPONDENCE_EMAIL_TEMPLATE_ID;
      } else {
        templateID =
          EnvVars.BDM_GCNOTIFY_FRENCH_CORRESPONDENCE_EMAIL_TEMPLATE_ID;
      }
    } else if (templateType.equals(BDMGCNotifyTemplateType.BDM_GC_NF)) {
      if (language.equals(BDMLANGUAGE.ENGLISHL)) {
        templateID =
          EnvVars.BDM_GCNOTIFY_ENGLISH_NOTIFICATION_EMAIL_TEMPLATE_ID;
      } else {
        templateID =
          EnvVars.BDM_GCNOTIFY_FRENCH_NOTIFICATION_EMAIL_TEMPLATE_ID;
      }
    }

    final String gcNotifyTemplateID = Configuration.getProperty(templateID);

    return gcNotifyTemplateID;
  }

  /**
   * Read the personalize data for the GC Notify message and add it to the
   * request.
   *
   * @param Concern Role ID
   **/
  public BDMGCNotifyRealTimePersonalizeData getPersonaliseDataForTemplate(
    final BDMGcNotifyRequestDataDtls requestData) {

    final BDMGCNotifyRealTimePersonalizeData personalisation =
      new BDMGCNotifyRealTimePersonalizeData();
    personalisation.setAlternateId(requestData.alternateID);
    personalisation.setFullName(requestData.fullName);
    return personalisation;
  }

  /**
   * send the request to GC Notify with the specified URL and the request object
   *
   * @param Concern Role ID
   **/
  public String
    sendGCNotifyRequest(final BDMGcNotifyRequestDataDtls requestData)
      throws AppException, InformationalException, IOException {

    if (requestData.alertType.equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_EMAIL))
      return bdmGCNotifyInterfaceImpl.sendGCNotifyRTEmailRequest(requestData);
    else if (requestData.alertType
      .equals(BDMGCNOTIFYALERTTYPE.BDM_GCNOTIFY_PHONE))
      return bdmGCNotifyInterfaceImpl.sendGCNotifyRTSMSRequest(requestData);
    else
      return CuramConst.gkEmpty;

  }

  public void sendGCNotifyRealTimeAlert(final Long concernRoleID,
    final String gcNotifyTemplateType)
    throws AppException, InformationalException, IOException {

    final ArrayList<DynamicEvidenceDataDetails> receiveAlertEvidList =
      this.getReceiveAlertPreference(concernRoleID);
    for (final DynamicEvidenceDataDetails receiveAlertEvid : receiveAlertEvidList) {
      final BDMGcNotifyRequestDataDtls gcNotifyRequestData =
        this.createAlertData(concernRoleID, gcNotifyTemplateType,
          receiveAlertEvid);
      // BEGIN TASK 16487 Modified For Notification Message
      if (!StringUtil.isNullOrEmpty(gcNotifyRequestData.interfaceType)) {
        // END TASK 16487 Modified For Notification Message
        if (gcNotifyRequestData.interfaceType
          .equals(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_REALTIME)) {
          this.sendGCNotifyRequest(gcNotifyRequestData);
        } else if (gcNotifyRequestData.interfaceType
          .equals(BDMGCNOTIFYINTERFACETYPE.BDM_GCNOTIFY_BATCH)) {
          gcNotifyRequestData.status = BDMGCNOTIFYNOTIFICATIONSTATUS.UN_PROC;
          this.insertGCNotifyRequestData(gcNotifyRequestData);
        }
      }
    }

  }

}
