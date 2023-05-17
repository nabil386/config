package curam.ca.gc.bdm.communication.impl;

import com.google.inject.ImplementedBy;
import curam.common.util.xml.dom.Element;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.TemplateAndDocumentDataKey;
import curam.core.sl.struct.TemplateDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import java.util.Map;

@ImplementedBy(BDMMSWordTemplateDataImpl.class)
public interface BDMMSWordTemplateData {

  Element createFIELDElement(String attributeName, String attributeValue);

  Map<String, String> getClientAndAddressTemplateData(
    MSWordCommunicationDetails1 mSWordCommunicationDetails)
    throws AppException, InformationalException;

  Map<String, String> getProcessingCenterAddressMapByAddressID(long addressID)
    throws AppException, InformationalException;

  TemplateDataDetails getCommonTemplateData(
    MSWordCommunicationDetails1 mSWordCommunicationDetails)
    throws AppException, InformationalException;

  TemplateDataDetails
    getCommonTemplateBlobContent(TemplateAndDocumentDataKey key)
      throws AppException, InformationalException;

  byte[] converMaptoWordXMLProperties(Map<String, String> propMap);

  Map<String, String>
    getCommonDataMap(MSWordCommunicationDetails1 mSWordCommunicationDetails)
      throws AppException, InformationalException;

}
