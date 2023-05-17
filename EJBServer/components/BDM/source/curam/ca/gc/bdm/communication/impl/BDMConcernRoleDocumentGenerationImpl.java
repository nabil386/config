package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.communication.struct.BDMBenefitDenialOptions;
import curam.core.impl.ConcernRoleDocumentGeneration;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.struct.ProFormaReturnDocDetails;
import curam.core.struct.ConcernRoleDocumentDetails;
import curam.core.struct.ProFormaDocumentData;
import curam.core.struct.SystemUserDtls;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.xml.impl.XMLPrintStreamConstants;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.Locale;
import curam.util.type.Blob;
import curam.util.type.DateTime;
import curam.util.xml.impl.XMLDocument;
import curam.util.xml.impl.XMLEncodingConstants;
import curam.util.xml.impl.XMLPrintStream;
import java.io.ByteArrayOutputStream;

public class BDMConcernRoleDocumentGenerationImpl
  extends ConcernRoleDocumentGeneration {

  @Inject
  BDMCorrespondenceProcessingCenters processingCenters;

  public BDMConcernRoleDocumentGenerationImpl() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Generation of Benefit Denial XML Document
   *
   * @param details Concern role document detail
   * @param data ProForma document data
   * @param templateOption Denial Option Template
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ProFormaReturnDocDetails generateAndPreviewBenefitDenialXMLDocument(
    final ConcernRoleDocumentDetails details, final ProFormaDocumentData data,
    final BDMBenefitDenialOptions templateOption)
    throws AppException, InformationalException {

    final curam.core.intf.SystemUser systemUserObj =
      curam.core.fact.SystemUserFactory.newInstance();
    SystemUserDtls systemUserDtls;

    // Return type
    final ProFormaReturnDocDetails proFormaReturnDocDetails =
      new ProFormaReturnDocDetails();

    // Create Preview Stream
    final ByteArrayOutputStream previewStream =
      new java.io.ByteArrayOutputStream();

    // Create XMLPrintStream object

    final XMLPrintStream printStreamObj = new XMLPrintStream();

    final curam.util.administration.struct.XSLTemplateInstanceKey xslTemplateInstanceKey =
      new curam.util.administration.struct.XSLTemplateInstanceKey();

    // Set up XSL template instance
    xslTemplateInstanceKey.templateID = details.documentID;
    xslTemplateInstanceKey.templateVersion = details.versionNo;

    xslTemplateInstanceKey.locale = details.localeIdentifier;

    if (!Configuration.getBooleanProperty(
      EnvVars.ENV_XMLSERVER_DISABLE_METHOD_CALLS,
      Configuration.getBooleanProperty(
        EnvVars.ENV_XMLSERVER_DISABLE_METHOD_CALLS_DEFAULT))) {

      if (data.printerName.length() > 0) {

        final String printerName = data.printerName;

        printStreamObj.setPrinterName(printerName);
      }

      printStreamObj.setPreviewStream(previewStream);
      printStreamObj.setJobType(XMLPrintStreamConstants.kJobTypePDF);

      try {
        printStreamObj.open(xslTemplateInstanceKey);

      } catch (final AppException ex) {

        // an error occurred - was the document not in valid XML format?
        if (ex.getCatEntry().equals(
          curam.util.message.CURAMXML.ERR_PRINT_STREAM_BAD_RESPONSE)) {

          // the pro-forma form is not a valid XML document -
          // convert this to a more meaningful message for the user
          throw new AppException(
            curam.message.BPOCONCERNROLEDOCUMENTGENERATION.ERR_INVALID_FORMAT_NOT_PRINTABLE,
            ex);

        } else {

          // we can't do anything with it -
          // just pass it on up to the calling method
          throw ex;
        }
      }

      final XMLDocument documentObj = new XMLDocument(
        printStreamObj.getStream(), XMLEncodingConstants.kEncodeUTF8);

      // Set data to print the document
      String userName = CuramConst.gkEmpty;

      if (data.caseClosureDate != null) {
        systemUserDtls = super.getLoggedInUser(data);
        userName = systemUserDtls.userName;
      } else {
        systemUserDtls = systemUserObj.getUserDetails();
        userName = systemUserDtls.userName;
      }

      final String generatedDate =
        Locale.getFormattedTime(DateTime.getCurrentDateTime());

      final String versionNo = String.valueOf(details.versionNo);
      final String comments = details.comments;

      // Open document
      documentObj.open(userName, generatedDate, versionNo, comments);

      // Add data to document
      // templateOption.processCtrPhoneDetails =
      // processingCenters.readProcessingCenterPhoneNumbers();
      // templateOption.proformaData = data;
      documentObj.add(templateOption);
      // documentObj.add(data);

      // Close document and print stream objects
      documentObj.close();
      printStreamObj.close();
    }

    proFormaReturnDocDetails.fileName = CuramConst.kProFormaDocumentPreview;
    proFormaReturnDocDetails.fileDate = new Blob(previewStream.toByteArray());

    return proFormaReturnDocDetails;
  }
}
