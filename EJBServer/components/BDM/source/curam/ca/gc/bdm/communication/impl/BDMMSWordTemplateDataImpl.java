package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.address.fact.BDMAddressFactory;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.sl.communication.struct.BDMProcessingCenterPhoneNumbers;
import curam.codetable.CASETYPECODE;
import curam.common.util.xml.dom.DOMException;
import curam.common.util.xml.dom.Document;
import curam.common.util.xml.dom.Element;
import curam.common.util.xml.dom.input.SAXBuilder;
import curam.common.util.xml.dom.output.XMLOutputter;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.struct.DocumentTemplateDtls;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.TemplateAndDocumentDataKey;
import curam.core.sl.struct.TemplateDataDetails;
import curam.core.struct.CaseKey;
import curam.core.struct.CaseTypeCode;
import curam.core.struct.OtherAddressData;
import curam.message.BPOADDRESS;
import curam.participant.impl.ConcernRoleDAO;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Locale;
import curam.util.resources.StringUtil;
import curam.util.type.Blob;
import curam.util.type.DateTime;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BDMMSWordTemplateDataImpl implements BDMMSWordTemplateData {

  private static final int addressLineMax = 5;

  @Inject
  private BDMCorrespondenceProcessingCenters processingCenters;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private BDMMSWordDocuments bdmWordDocImpl;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  BDMCommunicationHelper bdmCommHelper = new BDMCommunicationHelper();

  public BDMMSWordTemplateDataImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  @Override
  public TemplateDataDetails
    getCommonTemplateBlobContent(final TemplateAndDocumentDataKey key)
      throws AppException, InformationalException {

    final TemplateDataDetails templateDataDetails = new TemplateDataDetails();
    final DocumentTemplateDtls dtDtls =
      bdmWordDocImpl.getDocumentTemplateByID(key.templateID);
    templateDataDetails.documentData = dtDtls.contents;
    return templateDataDetails;
  }

  @Override
  public TemplateDataDetails getCommonTemplateData(
    final MSWordCommunicationDetails1 mSWordCommunicationDetails)
    throws AppException, InformationalException {

    final TemplateDataDetails templateDetails = new TemplateDataDetails();

    Map<String, String> propertyMap =
      getCommonDataMap(mSWordCommunicationDetails);

    final DocumentTemplateDtls docTemplateDtls = bdmWordDocImpl
      .getDocumentTemplateByID(mSWordCommunicationDetails.documentTemplateID);

    if (docTemplateDtls.documentTemplateID
      .startsWith(BDMCorrespondenceConstants.kSIR_S010003)
      || docTemplateDtls.documentTemplateID
        .startsWith(BDMCorrespondenceConstants.kSIR_S010004)) {
      propertyMap =
        getSIRNonMatchData(mSWordCommunicationDetails, propertyMap);
    }

    templateDetails.documentData =
      new Blob(converMaptoWordXMLProperties(propertyMap));

    return templateDetails;
  }

  /** Get custom data for application case document templates **/
  private Map<String, String> getSIRNonMatchData(
    final MSWordCommunicationDetails1 mSWordCommunicationDetails,
    final Map<String, String> propertyMap)
    throws AppException, InformationalException {

    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseKey caseKey = new CaseKey();

    caseKey.caseID = mSWordCommunicationDetails.caseID;
    // IF case is Application case
    final CaseTypeCode caseTypeCode = caseHeaderObj.readCaseTypeCode(caseKey);
    if (caseTypeCode.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)) {

      propertyMap.put(BDMCorrespondenceConstants.kProgramType,
        BDMCorrespondenceConstants.BDM_PROGRAMTYPE);

      final curam.commonintake.impl.ApplicationCase applicationCase =
        this.applicationCaseDAO.get(mSWordCommunicationDetails.caseID);

      final DateTime appSubmissionDateTime =
        applicationCase.getSubmittedDateTime();

      final curam.util.type.Date utilDate1 =
        new curam.util.type.Date(appSubmissionDateTime.getCalendar());

      // format application submission date to MMMM, dd YYYY
      final String formattedDate = curam.util.resources.Locale
        .getFormattedDate(utilDate1, BDMConstants.MMMM_d_YYYY);

      propertyMap.put(BDMCorrespondenceConstants.kAppSubmissionDate,
        formattedDate);
    }
    return propertyMap;
  }

  @Override
  public Map<String, String> getCommonDataMap(
    final MSWordCommunicationDetails1 mSWordCommunicationDetails)
    throws AppException, InformationalException {

    final Map<String, String> propertyMap =
      getClientAndAddressTemplateData(mSWordCommunicationDetails);

    if (mSWordCommunicationDetails.addressID != 0l) {
      final Map<String, String> processingCenterAddrMap =
        getProcessingCenterAddressMapByAddressID(
          mSWordCommunicationDetails.addressID);
      if (processingCenterAddrMap.size() != 0) {
        final String[] mapKeys = new String[processingCenterAddrMap.size()];
        processingCenterAddrMap.keySet().toArray(mapKeys);
        for (final String mapKey : mapKeys) {
          propertyMap.put(mapKey, processingCenterAddrMap.get(mapKey));
        }
      }
    }

    return propertyMap;
  }

  public byte[] appendFieldToXMLDocument(final byte[] xmlByteArray,
    final String attribute, final String value) {

    // Output the XML as a string and assign it to the return object
    final SAXBuilder sax = new SAXBuilder();
    final XMLOutputter outputter = new XMLOutputter();
    byte[] returnObj = null;
    try {
      final InputStream is = new ByteArrayInputStream(xmlByteArray);

      final Document doc = sax.build(is);

      final Element rootElement = doc.getRootElement();
      final Element fieldsElement = rootElement.getChild(CuramConst.gkFields);
      fieldsElement.addContent(this.createFIELDElement(attribute, value));
      rootElement.addContent(fieldsElement);

      returnObj = outputter.outputString(rootElement).getBytes();

    } catch (final DOMException e) {
      e.printStackTrace();
    }

    return returnObj;
  }

  @Override
  public byte[]
    converMaptoWordXMLProperties(final Map<String, String> propMap) {

    // Output the XML as a string and assign it to the return object
    final XMLOutputter outputter = new XMLOutputter();

    final Element rootElement = new Element(CuramConst.gkRoot);
    final Element fieldsElement = new Element(CuramConst.gkFields);

    final String[] keys = new String[propMap.size()];
    propMap.keySet().toArray(keys);
    for (final String key : keys) {
      fieldsElement
        .addContent(this.createFIELDElement(key, propMap.get(key)));
    }

    rootElement.addContent(fieldsElement);

    return outputter.outputString(rootElement).getBytes();
  }

  /**
   * Get the client and assoicated data for template
   *
   * @param Template and Document data key
   * @return Template data details
   */
  @Override
  public Map<String, String> getClientAndAddressTemplateData(
    final MSWordCommunicationDetails1 mSWordCommunicationDetails)
    throws AppException, InformationalException {

    final Map<String, String> propertyMap = new HashMap<String, String>();

    curam.core.fact.AddressFactory.newInstance();
    final OtherAddressData otherAddressData = new OtherAddressData();

    // Output the XML as a string and assign it to the return object
    // final XMLOutputter outputter = new XMLOutputter();

    final curam.core.struct.AddressKey addressKey =
      new curam.core.struct.AddressKey();

    addressKey.addressID = mSWordCommunicationDetails.addressID;

    otherAddressData.addressData = BDMAddressFactory.newInstance()
      .getCanadaPostAddressFormat(addressKey, null).addressData;

    final DocumentTemplateDtls docTemplateDtls = bdmWordDocImpl
      .getDocumentTemplateByID(mSWordCommunicationDetails.documentTemplateID);

    // if no address information from client
    if (StringUtil.isNullOrEmpty(otherAddressData.addressData)
      || otherAddressData.addressData
        .contains(BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE
          .getMessageText(docTemplateDtls.localeIdentifier))) {
      // populate the address unavailable text using the template locale
      otherAddressData.addressData =
        new curam.util.exception.LocalisableString(
          curam.message.BPOADDRESS.TEXT_ADDRESS_UNAVAILABLE)
            .getMessage(docTemplateDtls.localeIdentifier);
    }

    // populate Case Reference and Delimiter if document is created under case
    if (mSWordCommunicationDetails.caseID != 0l) {
      final curam.piwrapper.caseheader.impl.CaseHeader caseHeader =
        caseHeaderDAO.get(mSWordCommunicationDetails.caseID);
      propertyMap.put(BDMConstants.gkCaseReference,
        caseHeader.getCaseReference());

    } else {
      // make the fields empty when caseID is not present
      propertyMap.put(BDMConstants.gkCaseReference, CuramConst.gkEmpty);
      propertyMap.put(BDMConstants.gkCaseReferenceDelimiter,
        CuramConst.gkEmpty);
    }

    // Populate person name and alternateID
    propertyMap.put(CuramConst.gkPersonName,
      mSWordCommunicationDetails.correspondentName);

    propertyMap.put(BDMConstants.kAlternateID,
      concernRoleDAO
        .get(mSWordCommunicationDetails.correspondentParticipantRoleID)
        .getPrimaryAlternateID());

    final String[] addressDataElm =
      otherAddressData.addressData.split(CuramConst.gkNewLine);
    for (int i = 0; i < addressLineMax; i++) {
      if (i < addressDataElm.length) {
        // fieldsElement.addContent(this.createFIELDElement(
        // BDMConstants.kAddressLine + (i + 1), addressDataElm[i]));
        propertyMap.put(BDMConstants.kAddressLine + (i + 1),
          addressDataElm[i]);
      } else {
        // fieldsElement.addContent(
        // this.createFIELDElement(BDMConstants.kAddressLine + (i + 1), ""));
        propertyMap.put(BDMConstants.kAddressLine + (i + 1), "");
      }
    }

    final String formattedCurrentDate =
      Locale.getFormattedDate(DateTime.getCurrentDateTime());

    // fieldsElement.addContent(this
    // .createFIELDElement(BDMConstants.kCurrentDate, formattedCurrentDate));
    propertyMap.put(BDMConstants.kCurrentDate, formattedCurrentDate);

    final BDMProcessingCenterPhoneNumbers processingCenterPhoneNum =
      processingCenters.readProcessingCenterPhoneNumbers();

    /*
     * fieldsElement
     * .addContent(this.createFIELDElement(BDMConstants.kPhoneNumEnquiries,
     * processingCenterPhoneNum.phoneNumberEnquiries));
     *
     * fieldsElement.addContent(this.createFIELDElement(
     * BDMConstants.kPhoneNumTTY, processingCenterPhoneNum.phoneNumberTTY));
     *
     * fieldsElement.addContent(this.createFIELDElement(
     * BDMConstants.kPhoneNumINTL, processingCenterPhoneNum.phoneNumIntl));
     *
     * fieldsElement.addContent(
     * this.createFIELDElement(BDMConstants.kTemplateID, key.templateID));
     */

    propertyMap.put(BDMConstants.kPhoneNumEnquiries,
      processingCenterPhoneNum.phoneNumberEnquiries);
    propertyMap.put(BDMConstants.kPhoneNumTTY,
      processingCenterPhoneNum.phoneNumberTTY);
    propertyMap.put(BDMConstants.kPhoneNumINTL,
      processingCenterPhoneNum.phoneNumIntl);
    propertyMap.put(BDMConstants.kTemplateID,
      mSWordCommunicationDetails.documentTemplateID);

    final String processingCenterName =
      processingCenters.getProcessingCenterName();

    propertyMap.put(BDMConstants.kOfficeName, processingCenterName);

    // rootElement.addContent(fieldsElement);

    // templateDataDetails.documentData = new curam.util.type.Blob(
    // outputter.outputString(rootElement).getBytes());

    return propertyMap;
  }

  // Append MS Word regional office details by address id
  @Override
  public Map<String, String> getProcessingCenterAddressMapByAddressID(
    final long addressID) throws AppException, InformationalException {

    final Map<String, String> propertyMap = new HashMap<String, String>();

    final String processingCenterAddressData =
      processingCenters.getProcessingLocationByAddressID(addressID);

    final String[] addressDataElm =
      processingCenterAddressData.split(CuramConst.gkNewLine);
    for (int i = 0; i < addressLineMax; i++) {
      if (i < addressDataElm.length)
        propertyMap.put(BDMConstants.kOfficeAddressLine + (i + 1),
          addressDataElm[i]);
      else
        propertyMap.put(BDMConstants.kOfficeAddressLine + (i + 1), "");
    }
    return propertyMap;
  }

  @Override
  public Element createFIELDElement(final String attributeName,
    final String attributeValue) {

    final Element elm = new Element(CuramConst.gkField);
    elm.setAttribute(CuramConst.gkName, attributeName);
    elm.setAttribute(CuramConst.gkValue, attributeValue);
    return elm;
  }

}
