package curam.ca.gc.bdm.communication.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.facade.participant.fact.BDMParticipantFactory;
import curam.ca.gc.bdm.facade.participant.intf.BDMParticipant;
import curam.ca.gc.bdm.facade.participant.struct.BDMReadParticipantAddressDetails;
import curam.ca.gc.bdm.sl.communication.struct.BDMProFormaDocumentData;
import curam.codetable.CASESTATUS;
import curam.codetable.CASETYPECODE;
import curam.commonintake.impl.ApplicationCaseDAO;
import curam.core.facade.struct.ReadParticipantAddressListKey;
import curam.core.fact.CaseStatusFactory;
import curam.core.impl.ConcernRoleDocuments;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.CaseStatus;
import curam.core.sl.struct.ProFormaReturnDocDetails;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.CaseStatusDtls;
import curam.core.struct.CaseStatusDtlsList;
import curam.core.struct.CaseStatusSearchByCaseIDKey;
import curam.core.struct.ClosureDtls;
import curam.core.struct.ConcernRoleCommunicationDtls;
import curam.core.struct.ConcernRoleCommunicationKey;
import curam.core.struct.ConcernRoleDocumentDetails;
import curam.core.struct.ConcernRoleDocumentKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.CurrentCaseStatusKey;
import curam.core.struct.ProFormaDocumentData;
import curam.core.struct.ReadCaseClosureKey;
import curam.core.struct.SystemUserDtls;
import curam.piwrapper.caseheader.impl.CaseHeaderDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.internal.xml.impl.XMLPrintStreamConstants;
import curam.util.persistence.GuiceWrapper;
import curam.util.persistence.TransactionScope;
import curam.util.resources.Configuration;
import curam.util.resources.Locale;
import curam.util.type.AccessLevel;
import curam.util.type.AccessLevelType;
import curam.util.type.Blob;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.Implementable;
import curam.util.xml.impl.XMLDocument;
import curam.util.xml.impl.XMLEncodingConstants;
import curam.util.xml.impl.XMLPrintStream;
import curam.workspaceservices.util.impl.ResourceStoreHelper;
import java.io.ByteArrayOutputStream;

@Implementable
@AccessLevel(AccessLevelType.EXTERNAL)
@TransactionScope
public class BDMConcernRoleDocumentsImpl extends ConcernRoleDocuments {

  @Inject
  BDMConcernRoleDocumentGenerationImpl crDocumentGeneration;

  @Inject
  BDMCorrespondenceProcessingCenters processingCenters;

  @Inject
  private CaseHeaderDAO caseHeaderDAO;

  @Inject
  private ApplicationCaseDAO applicationCaseDAO;

  @Inject
  private ResourceStoreHelper resourceStoreHelper;

  BDMCommunicationHelper commHelper = new BDMCommunicationHelper();

  public BDMConcernRoleDocumentsImpl() {

    super();
    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Proforma Doc Details preview document
   */
  @Override
  public ProFormaReturnDocDetails previewDocument(
    final ConcernRoleDocumentKey key,
    final ConcernRoleDocumentDetails details)
    throws AppException, InformationalException {

    // default implementation for BDM
    return new ProFormaReturnDocDetails();
  }

  /**
   * Get ProForma data
   *
   * @param key Concern Role Document Key
   * @param details Concern Role document details
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  public ProFormaDocumentData getProformaData(
    final ConcernRoleDocumentKey key,
    final ConcernRoleDocumentDetails details)
    throws AppException, InformationalException {

    // pro forma document data to be populated
    ProFormaDocumentData proFormaDocumentData = new ProFormaDocumentData();

    // get the closure details
    final curam.core.intf.MaintainCaseClosure maintainCaseClosureObj =
      curam.core.fact.MaintainCaseClosureFactory.newInstance();
    final ReadCaseClosureKey readCaseClosureKey = new ReadCaseClosureKey();

    final curam.core.intf.CaseHeader caseHeaderObj =
      curam.core.fact.CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    if (key.caseID != 0) {

      caseHeaderKey.caseID = key.caseID;
      final CaseHeaderDtls caseHeaderDtls = caseHeaderObj.read(caseHeaderKey);
      readCaseClosureKey.caseID = key.caseID;

      // BEGIN TASK 22414
      Date caseClosureDate = Date.kZeroDate;

      if (caseHeaderDtls.caseTypeCode.equals(CASETYPECODE.APPLICATION_CASE)) {
        caseClosureDate = this.getClosedCaseStatusDetails(key.caseID);
      } else {
        ClosureDtls closureDtls = new ClosureDtls();

        if (caseHeaderDtls.statusCode
          .equals(curam.codetable.CASESTATUS.CLOSED)
          || caseHeaderDtls.statusCode
            .equals(curam.codetable.CASESTATUS.PENDINGCLOSURE)) {
          try {
            closureDtls =
              maintainCaseClosureObj.readCaseClosure(readCaseClosureKey);
          } catch (final Exception e) {
            final CurrentCaseStatusKey caseKey = new CurrentCaseStatusKey();
            caseKey.caseID = key.caseID;
            final CaseStatusDtls caseStatus = CaseStatusFactory.newInstance()
              .readCurrentStatusByCaseID1(caseKey);
            if (caseStatus.statusCode.equals(CASESTATUS.CLOSED))
              caseClosureDate = caseStatus.startDate;
          }
          caseClosureDate = closureDtls.closureDate;
        }
      }

      proFormaDocumentData.caseClosureDate = caseClosureDate;
      // END TASK 22414

      proFormaDocumentData.caseID = key.caseID;
      proFormaDocumentData.caseReference = caseHeaderDtls.caseReference;
      // getRuleCriteriaResultMap(key.caseID);

    }

    // populate the concern role data (if it exists)
    if (key.concernRoleID != 0) {

      final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

      // populate the user data
      final curam.core.intf.ConcernRoleDocumentGeneration concernRoleDocumentGenerationObj =
        curam.core.fact.ConcernRoleDocumentGenerationFactory.newInstance();

      // if this it a service supplier communication for a case closure the
      // details returned are for the service supplier.
      concernRoleKey.concernRoleID = key.concernRoleID;
      concernRoleDocumentGenerationObj.getConcernRoleData(concernRoleKey,
        proFormaDocumentData);
    }

    // Read the communication an obtain required address.
    if (details.communicationID != 0) {

      // Concern role communication object and access structures
      final curam.core.intf.ConcernRoleCommunication concernRoleCommObj =
        curam.core.fact.ConcernRoleCommunicationFactory.newInstance();
      final ConcernRoleCommunicationKey concernRoleCommunicationKey =
        new ConcernRoleCommunicationKey();

      concernRoleCommunicationKey.communicationID = details.communicationID;
      final ConcernRoleCommunicationDtls concernRoleCommDtls =
        concernRoleCommObj.read(concernRoleCommunicationKey);

      final BDMParticipant bdmParticipantObj =
        BDMParticipantFactory.newInstance();

      final ReadParticipantAddressListKey participantKey =
        new ReadParticipantAddressListKey();
      participantKey.maintainAddressKey.concernRoleID =
        concernRoleCommDtls.correspondentConcernRoleID;
      final BDMReadParticipantAddressDetails participantAddrDetails =
        bdmParticipantObj.readMailingAddress(participantKey);

      proFormaDocumentData.concernRoleAddress =
        participantAddrDetails.dtls.addressDetails.formattedAddressData;

      proFormaDocumentData.userFullName = "";
      proFormaDocumentData.userAddress = "";

      concernRoleCommDtls.addressID = participantAddrDetails.addressID;

      final String processingCenterAddressData = processingCenters
        .getProcessingLocationByAddressID(concernRoleCommDtls.addressID);

      proFormaDocumentData.userFullName =
        processingCenters.getProcessingCenterName();

      proFormaDocumentData.userAddress = processingCenterAddressData;

    }

    if (key.caseID != 0) {
      proFormaDocumentData =
        super.populateCaseDetails(key, details, proFormaDocumentData);

    }

    return proFormaDocumentData;
  }

  // TASK 14146 : Update date format CAll generateAndPreviewXMLDocument by
  // passing custom Struct

  /**
   * Generates an XML document from the specified XSL template and previews that
   * document.
   *
   * @param details
   * details of the document to be used
   * @param data
   * the data to be entered into the document
   *
   * @return the document details to be previewed
   */

  public ProFormaReturnDocDetails generateAndPreviewXMLDocument(
    final ConcernRoleDocumentDetails details,
    final BDMProFormaDocumentData data)
    throws AppException, InformationalException {

    /// populate the user data
    final curam.core.intf.ConcernRoleDocumentGeneration concernRoleDocumentGenerationObj =
      curam.core.fact.ConcernRoleDocumentGenerationFactory.newInstance();

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

      if (data.proFormaDocumentData.printerName.length() > 0) {

        final String printerName = data.proFormaDocumentData.printerName;

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

      final XMLDocument xmlDocument = new XMLDocument(
        printStreamObj.getStream(), XMLEncodingConstants.kEncodeUTF8);

      // Set data to print the document
      String userName = CuramConst.gkEmpty;

      if (data.proFormaDocumentData.caseClosureDate != null) {
        systemUserDtls = concernRoleDocumentGenerationObj
          .getLoggedInUser(data.proFormaDocumentData);
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
      xmlDocument.open(userName, generatedDate, versionNo, comments);

      // Add data to document
      xmlDocument.add(data);

      // Close document and print stream objects
      xmlDocument.close();
      printStreamObj.close();
    }

    proFormaReturnDocDetails.fileName = CuramConst.kProFormaDocumentPreview;
    proFormaReturnDocDetails.fileDate = new Blob(previewStream.toByteArray());

    return proFormaReturnDocDetails;
  }

  /**
   * Method to get the case closure date.
   *
   * @param caseID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private Date getClosedCaseStatusDetails(final long caseID)
    throws AppException, InformationalException {

    Date closureDate = Date.kZeroDate;

    final CaseStatus caseStatusObj = CaseStatusFactory.newInstance();

    final CaseStatusSearchByCaseIDKey caseStatusSearchByCaseIDKey =
      new CaseStatusSearchByCaseIDKey();

    caseStatusSearchByCaseIDKey.caseID = caseID;

    final CaseStatusDtlsList caseStatusDtlsList =
      caseStatusObj.searchByCaseID(caseStatusSearchByCaseIDKey);

    for (final CaseStatusDtls caseStatusDtls : caseStatusDtlsList.dtls) {
      if (caseStatusDtls.statusCode.equals(CASESTATUS.CLOSED)) {
        closureDate = caseStatusDtls.startDate;
      }
    }
    return closureDate;
  }
}
