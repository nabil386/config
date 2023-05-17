package curam.ca.gc.bdm.sl.interfaces.bdmcct.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTFolderFactory;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.bdmcct.intf.BDMCCTFolder;
import curam.ca.gc.bdm.entity.bdmcct.intf.BDMCCTTemplate;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTFolderDtls;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateDtls;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.impl.BDMInterfaceConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCancelPrintRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetCompletedPDFResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeFolder;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetFolderTreeTemplate;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTGetWorkItemStatusResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTInterfaceErrorResponse;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTUpdateCorrespondenceRequest;
import curam.ca.gc.bdm.util.rest.impl.BDMInterfaceLogger;
import curam.ca.gc.bdm.util.rest.impl.BDMOutboundCloudEventDetails;
import curam.ca.gc.bdm.util.rest.impl.BDMRestHttpClient;
import curam.ca.gc.bdm.util.rest.impl.BDMRestMethod;
import curam.ca.gc.bdm.util.rest.impl.BDMRestRequest;
import curam.ca.gc.bdm.util.rest.impl.BDMRestResponse;
import curam.ca.gc.bdm.util.rest.impl.BDMRestUtil;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.message.CatEntry;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.type.Date;
import curam.util.type.DateTime;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.httpclient.HttpStatus;
import org.jsoup.Jsoup;

/**
 * implementation of GetFolderTree, CreateCorrespondence, GetWorkItemStatus,
 * GetCompletedPDF, and CancelPrint API calls
 *
 */
public class BDMCCTOutboundInterfaceImpl {

  @Inject
  BDMInterfaceLogger interfaceLogger;

  @Inject
  BDMRestUtil bdmRestUtil;

  public BDMCCTOutboundInterfaceImpl() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * This method is to retrieve the active templates from CCT
   * and save them in the Cúram database. Must initially purge
   * the previous data from the Cúram tables before adding the new data.
   *
   * @param templateReq
   * @param processingDate
   * @return boolean - success indicator
   * @throws AppException
   * @throws InformationalException
   *
   */
  public boolean getAndSaveFolderTree(
    final BDMCCTGetFolderTreeRequest templateReq, final Date processingDate)
    throws AppException, InformationalException {

    validateTemplateRequest(templateReq);

    final BDMRestHttpClient client = new BDMRestHttpClient();

    try {
      BDMRestRequest req = setGetFolderConnection(templateReq);

      // BEGIN, ADO-107574 , Audit enablement for APIs
      req.setRelatedID(templateReq.getUserID());
      req.setInvokingMethod(
        "BDMCCTOutboundInterfaceImpl.getAndSaveFolderTree");
      req.setRequestTransactionDateTime(DateTime.getCurrentDateTime());
      // END, ADO-107574 , Audit enablement for APIs

      // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
      req = getAndAddCloudEventHeaders(req);
      Trace.kTopLevelLogger.info(req.getInvokingMethod()
        + ": CorrelationID = " + req.getCorrelationID());
      // END, ADO-117413 , CloudEvent, CorrelationID Implementation

      final BDMRestResponse response = client.callAPI(req);

      if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
        Trace.kTopLevelLogger
          .info("ResponseCode :" + response.getStatusLine().getStatusCode());
        return false;
      }

      // if the response code is success, insert the response into the database
      createTemplateAndFolder(response.getBody().toString(), processingDate);
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      throw new AppException(BDMBPOCCT.ERR_CCT_NOT_AVAILABLE);
    }

    return true;
  }

  /**
   * This method is to send the Create Correspondence Request to CCT and return
   * back the response
   *
   * @param templateReq
   * @return BDMCCTCreateCorrespondenceResponse
   * @throws AppException
   * @throws InformationalException
   */
  public BDMCCTCreateCorrespondenceResponse
    createCorrespondence(final BDMCCTCreateCorrespondenceRequest templateReq)
      throws AppException, InformationalException {

    final BDMRestHttpClient client = new BDMRestHttpClient();

    try {
      BDMRestRequest req = setCorrespondenceConnection(templateReq);

      // BEGIN, ADO-107574 , Audit enablement for APIs
      req.setRelatedID(templateReq.getTemplateID());
      req.setInvokingMethod(
        "BDMCCTOutboundInterfaceImpl.createCorrespondence");
      req.setRequestTransactionDateTime(DateTime.getCurrentDateTime());
      // END, ADO-107574 , Audit enablement for APIs

      // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
      req = getAndAddCloudEventHeaders(req);
      Trace.kTopLevelLogger.info(req.getInvokingMethod()
        + ": CorrelationID = " + req.getCorrelationID());
      // END, ADO-117413 , CloudEvent, CorrelationID Implementation

      final BDMRestResponse response = client.callAPI(req);

      if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
        throw processErrorResponse(response,
          BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
      } else {
        return BDMRestUtil.convertFromJSON(response.getBody(),
          BDMCCTCreateCorrespondenceResponse.class);
      }

    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }

  }

  /**
   * This method is validate the mandatory fields of the Request.
   *
   * @param templateReq
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateTemplateRequest(final BDMCCTGetFolderTreeRequest templateReq)
      throws AppException, InformationalException {

    // validate the mandatory fields
    if (templateReq.getUserID().isEmpty()) {
      throw new AppException(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    }

    if (templateReq.getCommunity().isEmpty()) {
      throw new AppException(BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    }
  }

  /**
   * This method is to set the CCT connection for GetFolderTree Interface.
   *
   * @param templateReq
   * @return BDMRestRequest
   * @throws AppException
   * @throws InformationalException
   * @throws MalformedURLException
   */
  protected BDMRestRequest
    setGetFolderConnection(final BDMCCTGetFolderTreeRequest templateReq)
      throws AppException, InformationalException, MalformedURLException {

    final BDMRestRequest req = new BDMRestRequest();

    req.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_GET_FOLDER_TREE_URL));

    req.addQueryParam(BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
      templateReq.getUserID());
    req.addQueryParam(BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM,
      templateReq.getCommunity());
    req.addQueryParam("includetemplates",
      String.valueOf(templateReq.getIncludeTemplatesInd()));
    req.addQueryParam("includetemplatefields",
      String.valueOf(templateReq.getIncludeTemplateFieldsInd()));

    req.setMethod(BDMRestMethod.GET);

    // add the authentication header
    addEncryptedHeaders(req);

    return req;
  }

  /**
   * This method is to set the CCT connection for /CreateCorrespondence
   *
   * @param templateReq
   * @return Request object for connection
   * @throws AppException
   * @throws InformationalException
   * @throws MalformedURLException
   */
  protected BDMRestRequest setCorrespondenceConnection(
    final BDMCCTCreateCorrespondenceRequest createCorrespondenceReq)
    throws AppException, InformationalException, MalformedURLException,
    UnsupportedEncodingException {

    final BDMRestRequest req = new BDMRestRequest();

    req.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_CREATE_CORRESPONDENCE_URL));

    if (!createCorrespondenceReq.getCommunity().isEmpty()) {
      req.addQueryParam("Community",
        URLEncoder.encode(createCorrespondenceReq.getCommunity(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_COMMUNITY_NAME);
    }

    if (!createCorrespondenceReq.getUserID().isEmpty()) {
      req.addQueryParam("UserID",
        URLEncoder.encode(createCorrespondenceReq.getUserID(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_USER_NAME);
    }

    if (!createCorrespondenceReq.getDataXML().isEmpty()) {
      req.addQueryParam("DataXML",
        URLEncoder.encode(createCorrespondenceReq.getDataXML(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_DATA_XML);
    }

    if (!createCorrespondenceReq.getDataMapName().isEmpty()) {
      req.addQueryParam("DataMapName",
        URLEncoder.encode(createCorrespondenceReq.getDataMapName(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_DATA_MAP_NAME);
    }
    // Change to > 0 once the template is fixed
    if (createCorrespondenceReq.getTemplateID() >= 0) {
      req.addQueryParam("TemplateID",
        URLEncoder.encode(
          Long.toString(createCorrespondenceReq.getTemplateID()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_INCORRECT_TEMPLATE_ID_VALUE);
    }

    if (!createCorrespondenceReq.getTemplateFullPath().isEmpty()) {
      req.addQueryParam("TemplateFullPath",
        URLEncoder.encode(createCorrespondenceReq.getTemplateFullPath(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_TEMPLATE_FULL_PATH);
    }

    req.setMethod(BDMRestMethod.POST);

    // add the authentication header
    addEncryptedHeaders(req);

    req.setContentType(BDMInterfaceConstants.BDM_INTERFACE_APPLICATION_JSON);

    // set body
    req.setBody(BDMRestUtil.convertToJSON(createCorrespondenceReq));

    return req;
  }

  /**
   * This method purges the previous template and folder records from the
   * database
   * and saves the new active templates and folders.
   *
   * @param responseBody
   * @param processingDate
   * @throws AppException
   * @throws InformationalException
   */
  protected void createTemplateAndFolder(final String responseBody,
    final Date processingDate) throws AppException, InformationalException {

    try {
      final BDMCCTFolder cctFolderObj = BDMCCTFolderFactory.newInstance();
      final BDMCCTTemplate cctTemplateObj =
        BDMCCTTemplateFactory.newInstance();

      // purge the database tables
      cctTemplateObj.removeAll();
      cctFolderObj.removeAll();

    } catch (final RecordNotFoundException ex) {
      Trace.kTopLevelLogger.error(ex);
    }

    // JSON to BDMCCTGetFolderTreeResponse
    final BDMCCTGetFolderTreeResponse response = BDMRestUtil
      .convertFromJSON(responseBody, BDMCCTGetFolderTreeResponse.class);

    final BDMCCTGetFolderTreeFolder data = response.getData();
    final List<BDMCCTGetFolderTreeFolder> parentFolderList =
      data.getFolders();
    final List<BDMCCTGetFolderTreeTemplate> parentTemplateList =
      data.getTemplates();

    // loop through the parent folders
    for (int size = 0; size < parentFolderList.size(); size++) {

      final BDMCCTGetFolderTreeFolder folder = parentFolderList.get(size);
      createFolders(folder, 0L, processingDate);
    }

    if (!parentTemplateList.isEmpty()) {
      createTemplates(parentTemplateList, 0L, processingDate);
    }
  }

  /**
   * This method saves the folders and associated templates.
   * It also checks if any child folder is present, if so,
   * it saves the child folder details as well.
   *
   * @param folder
   * @param parentFolderID
   * @param processingDate
   * @throws AppException
   * @throws InformationalException
   */
  private void createFolders(final BDMCCTGetFolderTreeFolder folder,
    final long parentFolderID, final Date processingDate)
    throws AppException, InformationalException {

    final BDMCCTFolder cctFolderObj = BDMCCTFolderFactory.newInstance();
    final BDMCCTFolderDtls folderDtls = new BDMCCTFolderDtls();

    folderDtls.folderID = Long.parseLong(folder.getID());
    // BUG-92764, Start
    folderDtls.folderDesc = Jsoup.parse(folder.getDescription()).text();
    folderDtls.folderName = Jsoup.parse(folder.getName()).text();
    // BUG-92764, End
    folderDtls.parentFolderID = parentFolderID;
    folderDtls.createdOn = processingDate;

    // insert the folder details
    cctFolderObj.insert(folderDtls);

    // check for templates
    if (!folder.getTemplates().isEmpty()) {

      createTemplates(folder.getTemplates(), folderDtls.folderID,
        processingDate);
    }

    // check for child folders
    if (!folder.getFolders().isEmpty()) {
      for (int size = 0; size < folder.getFolders().size(); size++) {
        // create child folders
        createFolders(folder.getFolders().get(size), folderDtls.folderID,
          processingDate);
      }
    }
  }

  /**
   * This method creates the template details.
   *
   * @param templateList
   * @param folderID
   * @param processingDate
   * @throws AppException
   * @throws InformationalException
   */
  private void createTemplates(
    final List<BDMCCTGetFolderTreeTemplate> templateList, final long folderID,
    final Date processingDate) throws AppException, InformationalException {

    final BDMCCTTemplate cctTemplateObj = BDMCCTTemplateFactory.newInstance();

    for (int size = 0; size < templateList.size(); size++) {
      final BDMCCTGetFolderTreeTemplate template = templateList.get(size);
      final BDMCCTTemplateDtls templateDtls = new BDMCCTTemplateDtls();

      templateDtls.templateID = Long.parseLong(template.getID());
      // BUG-92764, Start
      templateDtls.templateName = Jsoup.parse(template.getName()).text();
      templateDtls.templateDesc =
        Jsoup.parse(template.getDescription()).text();
      templateDtls.templatePath = Jsoup.parse(template.getPath()).text();
      // BUG-92764, End
      templateDtls.folderID = folderID;
      templateDtls.effectiveDate =
        getEffectiveDate(template.getEffectiveDateSelection());
      templateDtls.templateVersion = template.getVersion();
      templateDtls.createdOn = processingDate;

      templateDtls.templateFields =
        getCommaSeparatedTemplateFields(template.getFields());

      // insert into the template table
      cctTemplateObj.insert(templateDtls);
    }
  }

  /**
   * This method forms a comma separated string for the template fields
   *
   * @param templateFields
   * @return String - formatted String
   * @throws AppException
   * @throws InformationalException
   */
  private String
    getCommaSeparatedTemplateFields(final List<String> templateFields)
      throws AppException, InformationalException {

    return String.join(CuramConst.gkComma, templateFields);

  }

  /**
   * This method gets the date format of a string passed.
   * If the string passed is "CURRENT_DATE", then it returns back the current
   * date, else it returns zero date.
   *
   * @param effectiveDateStr
   * @return Date - effective date
   * @throws AppException
   * @throws InformationalException
   */
  private Date getEffectiveDate(final String effectiveDateStr)
    throws AppException, InformationalException {

    return BDMConstants.kCurrentDateStr.equalsIgnoreCase(effectiveDateStr)
      ? Date.getCurrentDate() : Date.kZeroDate;
  }

  /**
   * Create BDMRestRequest object consisting of the elements required for the
   * GET
   * /CancelPrint API call to CCT.
   *
   * @param cancelPrintRequest - POJO representing parameters for
   * /CancelPrint
   * @return Request object representing API call
   * @throws AppException
   * @throws InformationalException
   * @throws UnsupportedEncodingException
   */
  protected BDMRestRequest setCancelPrintConnection(
    final BDMCCTCancelPrintRequest cancelPrintRequest) throws AppException,
    InformationalException, UnsupportedEncodingException {

    final BDMRestRequest request = new BDMRestRequest();

    // Set up URL (including query params) for request, encode the query
    // parameters
    request.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_CANCEL_PRINT_URL));

    if (!cancelPrintRequest.getUserID().isEmpty()) {
      request.addQueryParam(BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
        URLEncoder.encode(cancelPrintRequest.getUserID(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    }

    if (!cancelPrintRequest.getCommunity().isEmpty()) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM,
        URLEncoder.encode(cancelPrintRequest.getCommunity(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    }

    if (cancelPrintRequest.getWorkItemID() > 0) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_WORKITEMID_QUERY_PARAM,
        URLEncoder.encode(Long.toString(cancelPrintRequest.getWorkItemID()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(
        BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    }

    request.setMethod(BDMRestMethod.DELETE);

    // Set headers for request
    addEncryptedHeaders(request);

    request.addHeader(BDMInterfaceConstants.BDM_INTERFACE_CONTENT_TYPE,
      BDMInterfaceConstants.BDM_INTERFACE_APPLICATION_JSON);

    return request;
  }

  /**
   * Interface method to make /CancelPrint call to CCT to cancel/void the
   * specified work item.
   *
   * @param cancelPrintRequest - POJO representing parameters for
   * /CancelPrint
   * @return Indicator of success of cancel print operation
   * @throws AppException
   * @throws InformationalException
   */
  public BDMRestResponse
    cancelPrint(final BDMCCTCancelPrintRequest cancelPrintRequest)
      throws AppException, InformationalException {

    // BUG-92561, Start
    // Return the response for downstream processing.
    BDMRestResponse response;
    try {
      // Make HTTP call to CCT
      final BDMRestHttpClient httpClient = new BDMRestHttpClient();
      BDMRestRequest request = setCancelPrintConnection(cancelPrintRequest);

      // BEGIN, ADO-107574 , Audit enablement for APIs
      request.setRelatedID(cancelPrintRequest.getWorkItemID());
      request.setInvokingMethod("BDMCCTOutboundInterfaceImpl.cancelPrint");
      request.setRequestTransactionDateTime(DateTime.getCurrentDateTime());
      // END, ADO-107574 , Audit enablement for APIs

      // START: BUG 114096
      httpClient.setCallAndSocketTimeouts(180000, 180000);
      httpClient.setTimeout(180000);
      // END: BUG 114096

      // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
      request = getAndAddCloudEventHeaders(request);
      Trace.kTopLevelLogger.info(request.getInvokingMethod()
        + ": CorrelationID = " + request.getCorrelationID());
      // END, ADO-117413 , CloudEvent, CorrelationID Implementation

      response = httpClient.callAPI(request);

      // Check HTTP response code for a successful response
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw processErrorResponse(response,
          BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      throw new AppException(BDMBPOCCT.ERR_DOC_CANNOT_BE_CANCELLED);
    }
    return response;
    // BUG-92561, End
  }

  /**
   * Create BDMRestRequest object consisting of the elements required for the
   * GET
   * /GetWorkItemStatus API call to CCT.
   *
   * @param workItemStatusRequest - POJO representing parameters for
   * /GetWorkItemStatus
   * @return Request object representing API call
   * @throws AppException
   * @throws InformationalException
   * @throws UnsupportedEncodingException
   */
  protected BDMRestRequest setWorkItemStatusConnection(
    final BDMCCTGetWorkItemStatusRequest workItemStatusRequest)
    throws AppException, InformationalException,
    UnsupportedEncodingException {

    final BDMRestRequest request = new BDMRestRequest();

    // Set up URL (including query params) for request, encode the query
    // parameters
    request.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_GET_WORKITEM_STATUS_URL));

    if (!workItemStatusRequest.getUserID().isEmpty()) {
      request.addQueryParam(BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
        URLEncoder.encode(workItemStatusRequest.getUserID(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    }

    if (!workItemStatusRequest.getCommunity().isEmpty()) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM,
        URLEncoder.encode(workItemStatusRequest.getCommunity(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    }

    if (workItemStatusRequest.getWorkItemId() > 0) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_WORKITEMID_QUERY_PARAM,
        URLEncoder.encode(
          Long.toString(workItemStatusRequest.getWorkItemId()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(
        BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    }

    if (!(workItemStatusRequest.getCorrespondenceMode() == null
      || workItemStatusRequest.getCorrespondenceMode().isEmpty())
      && workItemStatusRequest.getCorrespondenceMode().matches("[01]")) {
      request.addQueryParam("correspondencemode",
        workItemStatusRequest.getCorrespondenceMode());
    }

    request.setMethod(BDMRestMethod.GET);

    // Set headers for request
    addEncryptedHeaders(request);

    request.addHeader(BDMInterfaceConstants.BDM_INTERFACE_CONTENT_TYPE,
      BDMInterfaceConstants.BDM_INTERFACE_APPLICATION_JSON);

    return request;

  }

  /**
   * Interface method to make /GetWorkItemStatus call to CCT to retrieve the
   * work
   * item status for a given work item.
   *
   * @param workItemStatusRequest - POJO representing parameters for
   * /GetWorkItemStatus
   * @return POJO representing response body of API call
   * @throws AppException
   * @throws InformationalException
   */
  public BDMCCTGetWorkItemStatusResponse getWorkItemStatus(
    final BDMCCTGetWorkItemStatusRequest workItemStatusRequest)
    throws AppException, InformationalException {

    try {
      // Make HTTP call to CCT
      final BDMRestHttpClient httpClient = new BDMRestHttpClient();
      final BDMRestRequest request =
        setWorkItemStatusConnection(workItemStatusRequest);
      final BDMRestResponse response = httpClient.callAPI(request);

      // Check HTTP response code for a successful response
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw processErrorResponse(response,
          BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
      } else {
        // On successful HTTP call, convert response body to POJO
        return BDMRestUtil.convertFromJSON(response.getBody(),
          BDMCCTGetWorkItemStatusResponse.class);
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e);
      throw new AppException(BDMBPOCCT.ERR_DOC_CANNOT_BE_FOUND);
    }

  }

  /**
   * Interface method to make /UpdateCorrespondence call to CCT to
   * update a correspondence record.
   *
   * @param updateCorrespondenceRequest - POJO representing parameters for
   * /UpdateCorrespondence
   * @return boolean - success indicator
   * @throws AppException
   * @throws IOException
   * @throws InformationalException
   */
  public boolean updateCorrespondence(
    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest)
    throws AppException, IOException, InformationalException {

    final String kMethod_updateCorrespondence =
      "BDMCCTOutboundInterfaceImpl.updateCorrespondence";

    // Record the time of the call
    final long startTime = System.currentTimeMillis();

    try {

      // Create connection to CCT API and validate request parameters
      final BDMRestHttpClient httpClient = new BDMRestHttpClient();
      BDMRestRequest request =
        setUpdateCorrespondenceConnection(updateCorrespondenceRequest);

      // Set Audit related parameters
      request.setRelatedID(updateCorrespondenceRequest.getWorkItemId());
      request.setInvokingMethod(kMethod_updateCorrespondence);
      request.setRequestTransactionDateTime(DateTime.getCurrentDateTime());

      // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
      request = getAndAddCloudEventHeaders(request);
      Trace.kTopLevelLogger.info(request.getInvokingMethod()
        + ": CorrelationID = " + request.getCorrelationID());
      // END, ADO-117413 , CloudEvent, CorrelationID Implementation

      // Make HTTP call to CCT
      // START: BUG 114096
      httpClient.setCallAndSocketTimeouts(180000, 180000);
      httpClient.setTimeout(180000);
      // END: BUG 114096
      final BDMRestResponse response = httpClient.callAPI(request);

      // Log the response object
      interfaceLogger.logResponse(kMethod_updateCorrespondence,
        Long.toString(updateCorrespondenceRequest.getWorkItemId()), response);

      // Check HTTP response code for a successful response
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        Trace.kTopLevelLogger.error(processErrorResponse(response,
          BDMBPOCCT.ERR_UNABLE_TO_UPDATE_CORRESPONDENCE).getMessage());
        return false;
      }

    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      throw e;
    }

    // Log the performance time in milliseconds
    interfaceLogger.logRestAPIPerf(kMethod_updateCorrespondence, startTime,
      Long.toString(updateCorrespondenceRequest.getWorkItemId()));

    return true;

  }

  /**
   * This method is to set the CCT connection for /CreateCorrespondence
   *
   * @param templateReq
   * @return Request object for connection
   * @throws AppException
   * @throws InformationalException
   * @throws MalformedURLException
   */
  protected BDMRestRequest setUpdateCorrespondenceConnection(
    final BDMCCTUpdateCorrespondenceRequest updateCorrespondenceRequest)
    throws AppException, UnsupportedEncodingException {

    final BDMRestRequest req = new BDMRestRequest();

    req.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_UPDATE_CORRESPONDENCE_URL));

    if (!updateCorrespondenceRequest.getCommunity().isEmpty()) {
      req.addQueryParam("Community",
        URLEncoder.encode(updateCorrespondenceRequest.getCommunity(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_COMMUNITY_NAME);
    }

    if (!updateCorrespondenceRequest.getUserId().isEmpty()) {
      req.addQueryParam("UserId",
        URLEncoder.encode(updateCorrespondenceRequest.getUserId(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_USER_NAME);
    }

    if (updateCorrespondenceRequest.getWorkItemId() >= 0) {
      req.addQueryParam("WorkItemId",
        URLEncoder.encode(
          Long.toString(updateCorrespondenceRequest.getWorkItemId()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_WORK_ITEM_ID);
    }

    if (!updateCorrespondenceRequest.getDataMapName().isEmpty()) {
      req.addQueryParam("DataMapName",
        URLEncoder.encode(updateCorrespondenceRequest.getDataMapName(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_DATA_MAP_NAME);
    }

    if (!updateCorrespondenceRequest.getDataXML().isEmpty()) {
      req.addQueryParam("DataXML",
        URLEncoder.encode(updateCorrespondenceRequest.getDataXML(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_MISSING_DATA_XML);
    }

    req.setMethod(BDMRestMethod.POST);

    // add the authentication header
    addEncryptedHeaders(req);

    req.setContentType(BDMInterfaceConstants.BDM_INTERFACE_APPLICATION_JSON);

    // set body
    req.setBody(BDMRestUtil.convertToJSON(updateCorrespondenceRequest));

    return req;
  }

  /**
   * This method is to send the getCompletedPDF Request to CCT and return
   * back the response
   *
   * @param getCompletedPDFRequest
   * @return BDMCCTGetCompletedPDFResponse
   * @throws AppException
   * @throws InformationalException
   */
  public BDMCCTGetCompletedPDFResponse
    getCompletedPDF(final BDMCCTGetCompletedPDFRequest getCompletedPdfRequest)
      throws AppException, InformationalException {

    try {
      // Make HTTP call to CCT
      final BDMRestHttpClient httpClient = new BDMRestHttpClient();
      BDMRestRequest request =
        setGetCompletedPDFConnection(getCompletedPdfRequest);

      // BEGIN, ADO-107574 , Audit enablement for APIs
      request.setRelatedID(getCompletedPdfRequest.getWorkItemID());
      request
        .setInvokingMethod("BDMCCTOutboundInterfaceImpl.getCompletedPDF");
      request.setRequestTransactionDateTime(DateTime.getCurrentDateTime());
      // END, ADO-107574 , Audit enablement for APIs

      // START: BUG 114096
      httpClient.setCallAndSocketTimeouts(180000, 180000);
      httpClient.setTimeout(180000);
      // END: BUG 114096

      // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation
      request = getAndAddCloudEventHeaders(request);
      Trace.kTopLevelLogger.info(request.getInvokingMethod()
        + ": CorrelationID = " + request.getCorrelationID());
      // END, ADO-117413 , CloudEvent, CorrelationID Implementation

      final BDMRestResponse response = httpClient.callAPI(request);

      // Check HTTP response code for a successful response
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        throw processErrorResponse(response,
          BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
      } else {
        // On successful HTTP call, deserialize response body to POJO
        return BDMRestUtil.convertFromJSON(response.getBody(),
          BDMCCTGetCompletedPDFResponse.class);
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.error(e.getMessage());
      throw new AppException(BDMBPOCCT.ERR_DOC_CANNOT_BE_DOWNLOADED);
    }
  }

  /**
   * Create BDMRestRequest object consisting of the elements required for the
   * GET
   * /GetCompletedPDF API call to CCT.
   *
   * @param getCompletedPDFRequest - POJO representing parameters for
   * /GetCompletedPDF
   * @return Request object representing API call
   * @throws AppException
   * @throws InformationalException
   * @throws UnsupportedEncodingException
   */
  protected BDMRestRequest setGetCompletedPDFConnection(
    final BDMCCTGetCompletedPDFRequest getCompletedPDFRequest)
    throws AppException, InformationalException,
    UnsupportedEncodingException {

    final BDMRestRequest request = new BDMRestRequest();

    // Set up URL (including query params) for request, encode the query
    // parameters
    request.setEndpointURI(
      Configuration.getProperty(EnvVars.BDM_CCT_GET_COMPLETED_PDF_URL));

    if (!getCompletedPDFRequest.getUserID().isEmpty()) {
      request.addQueryParam(BDMInterfaceConstants.BDM_CCT_USERID_QUERY_PARAM,
        URLEncoder.encode(getCompletedPDFRequest.getUserID(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_USERID_MUST_BE_ENTERED);
    }

    if (!getCompletedPDFRequest.getCommunity().isEmpty()) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_COMMUNITY_QUERY_PARAM,
        URLEncoder.encode(getCompletedPDFRequest.getCommunity(),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_COMMUNITY_MUST_BE_ENTERED);
    }
    if (getCompletedPDFRequest.getVaultLetterID() != 0) {
      request.addQueryParam("vaultletterid",
        URLEncoder.encode(
          String.valueOf(getCompletedPDFRequest.getVaultLetterID()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(BDMBPOCCT.ERR_VAULT_LETTER_ID_MUST_BE_ENTERED);
    }

    if (getCompletedPDFRequest.getWorkItemID() > 0) {
      request.addQueryParam(
        BDMInterfaceConstants.BDM_CCT_WORKITEMID_QUERY_PARAM,
        URLEncoder.encode(
          Long.toString(getCompletedPDFRequest.getWorkItemID()),
          StandardCharsets.UTF_8.toString()));
    } else {
      throw new AppException(
        BDMBPOCCT.ERR_ID_NON_ZERO_POSITIVE_MUST_BE_ENTERED);
    }

    request.setMethod(BDMRestMethod.GET);

    // Set headers for request
    addEncryptedHeaders(request);

    return request;

  }

  /**
   * Process the HTTP error response and throw the correct error
   *
   * @param response - BDMRestResponse object which contains error information
   * @return AppException to throw
   */
  protected AppException processErrorResponse(final BDMRestResponse response,
    final CatEntry serverResponseMessage) {

    Trace.kTopLevelLogger.info(String.format("HTTP Response Code: %d",
      response.getStatusLine().getStatusCode()));

    // If the error code is 401 then throw an unauthorized error, otherwise
    // throw
    // the 400/500 error with the CCT error message as the argument
    if (response.getStatusLine()
      .getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
      return new AppException(BDMBPOCCT.ERR_CCT_HTTP_401_UNAUTHORIZED);
    } else if (response.getStatusLine()
      .getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
      return new AppException(serverResponseMessage);
    } else if (response.getStatusLine()
      .getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
      return new AppException(serverResponseMessage);

    } else {
      final BDMCCTInterfaceErrorResponse errorResponse =
        BDMRestUtil.convertFromJSON(response.getBody(),
          BDMCCTInterfaceErrorResponse.class);
      final AppException exception =
        new AppException(BDMBPOCCT.ERR_CCT_HTTP_400_BAD_REQUEST);
      exception.arg(errorResponse.getMessage());
      return exception;
    }

  }

  /**
   * Helper function to add encrypted x-api-key and Ocp-Apim-Subscription-Key to
   * the set of headers part of the request.
   *
   * @param request - Created outbound request
   * @throws AppException
   */
  private void addEncryptedHeaders(final BDMRestRequest request)
    throws AppException {

    // Set headers for request

    // START, BUG-97097: APIM Subscription Key for Outbound API
    String decryptedSubscriptionValue = CuramConst.gkEmpty;
    final String propertySubscriptionValue =
      Configuration.getProperty(EnvVars.BDM_OCP_APIM_SUBSCRIPTION_KEY);
    if (!StringUtil.isNullOrEmpty(propertySubscriptionValue)) {
      decryptedSubscriptionValue =
        BDMRestUtil.decryptKeys(propertySubscriptionValue);
    }
    request.addHeader(BDMInterfaceConstants.BDM_APIM_SUBSCRIPTION_HEADER_KEY,
      decryptedSubscriptionValue);
    // END, BUG-97097: APIM Subscription Key for Outbound API
  }

  // BEGIN, ADO-117413 , CloudEvent, CorrelationID Implementation

  /***
   * Helper method to get the cloudevennt struct populated and add the values to
   * headers for CCT.
   *
   * @param BDMRestRequest
   * @return BDMRestRequest
   *
   */
  private BDMRestRequest getAndAddCloudEventHeaders(BDMRestRequest request) {

    if (bdmRestUtil.isCloudEventEnabled(request.getInvokingMethod())) {
      final BDMOutboundCloudEventDetails cloudEventDetails =
        bdmRestUtil.getCloudEventDetailsForCCT(request);
      request =
        bdmRestUtil.addCloudEventHeaders(request, cloudEventDetails, false);
    }
    return request;
  }
  // END, ADO-117413 , CloudEvent, CorrelationID Implementation
}
