package curam.ca.gc.bdm.communication.impl;

import com.google.inject.ImplementedBy;
import curam.core.sl.entity.struct.DocumentTemplateDtls;
import curam.core.sl.struct.MSWordCommunicationDetails1;
import curam.core.sl.struct.TemplateDataDetails;
import curam.core.sl.struct.WordTemplateDocumentAndDataDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Blob;

@ImplementedBy(BDMMSWordDocumentsImpl.class)
public interface BDMMSWordDocuments {

  TemplateDataDetails
    getTemplateData(MSWordCommunicationDetails1 wordCommDetails)
      throws AppException, InformationalException;

  WordTemplateDocumentAndDataDetails getWordTemplateDocumentAndData(
    MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException;

  DocumentTemplateDtls getDocumentTemplateByID(String docTemplateID)
    throws AppException, InformationalException;

  Blob convertToPDF(final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException;

  Blob updateMSWordCustomProperties(
    final MSWordCommunicationDetails1 wordCommDetails)
    throws AppException, InformationalException;

}
