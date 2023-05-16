<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:output method="text"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />
  <xsl:include href="UserInterface/JavaID2CodeValue.xslt"/>
  <xsl:include href="UserInterface/Create/CreateUtilityTemplates.xslt"/>

  <xsl:param name="productEvidenceHome"/>
  <xsl:param name="date"/>

  <xsl:template match="EvidenceEntities">

    <xsl:variable name="product"><xsl:value-of select="//EvidenceEntities/@product"/></xsl:variable>


    <!-- Open facade files for writing and write in header  -->
<!--
    When multiple Facade Classes are needed the commented out FOR-EACH loops were used.
    Used when some, but not all, Facade Entities required specific methods.
    In this case the metadata for each entity specified WHICH facadeclass it belonged too,
    Only then could that code be placed into the Facade Class file being created.

    NOTE : Each "FOR-EACH" loop needed to reset the filename to open and write to.
               Each entities metadata for Facade Layer "capName" specified the name of Facade class to be put into.
               They used the EvidenceEnttiy/FacadeLayer/@name to set the FileName value.
-->

    <xsl:variable name="generalError">ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>

    <xsl:variable name="className"><xsl:value-of select="$prefix"/>EvidenceMaintenance</xsl:variable>

    <xsl:variable name="filename"><xsl:value-of select="$className"/>.java</xsl:variable>

    <redirect:open select="$filename" method="text" append="false" />
    <redirect:write select="$filename">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

package <xsl:value-of select="$javaEvidenceCodePath"/>.facade.impl;

<xsl:call-template name="GetSortedImports"/>

/**
 * Process class for <xsl:value-of select="$className"/> facade.
 */
@SuppressWarnings("all")
@curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
public abstract class <xsl:value-of select="$className"/> extends <xsl:value-of select="$javaEvidenceCodePath"/>.facade.base.<xsl:value-of select="$className"/> {
  <!-- BEGIN, 191675, JAY -->
    @Inject(optional = true)
  protected AutoEndDateEvidenceHook autoEndDateEvidenceHook;

   @Inject
  protected AutoEndDateEvidenceOperations autoEndDateEvidenceOperations;

  @Inject
  protected IncomingEvidenceUtil incomingEvidenceUtil;

   public <xsl:value-of select="$className"/>() {
    GuiceWrapper.getInjector().injectMembers(this);
  }
  <!-- END, 191675, JAY -->

  /**
  * Resolve the evidence identifier and type as well as the case identifier
  * from a given evidence descriptor succession identifier.
  *
  * @param key
  *          The succession identifier.
  * @return The evidence identifier and type as well as the case identifier.
  * @throws AppException
  *           Generic Exception Signature.
  * @throws InformationalException
  *           Generic Exception Signature.
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public EvidenceCaseKey getEvidenceAndCaseFromSuccession(SuccessionID key)
    throws AppException, InformationalException {

    if (key.successionID == 0) {
      throw new AppException(<xsl:value-of select="$generalError"/>.ERR_FV_EVIDENCE_SELECTION_REQUIRED);
    }
    return EvidenceFactory.newInstance().getEvidenceAndCaseFromSuccession(key);
  }

  //____________________________________________________________________________
  /**
  * Method to read details about a core employment to help when relating an
  * evidence record to a core employment.
  *
  * @param key Employment ID.
  *
  * @return Details about the employment.
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public <xsl:value-of select="$prefix"/>RelatedEmploymentDetails readRelatedCoreEmployment(
      <xsl:value-of select="$prefix"/>RelatedEmploymentKey key) throws AppException, InformationalException {

      if (key.employmentID == 0) {
        throw new AppException(<xsl:value-of select="$generalError"/>.ERR_FV_EMPLOYMENT_SELECTION_REQUIRED);
      }

      <xsl:value-of select="$prefix"/>RelatedEmploymentDetails returnStruct = new <xsl:value-of select="$prefix"/>RelatedEmploymentDetails();

      SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
        new SearchCaseParticipantDetailsList();

      SearchCaseParticipantDetailsKey searchKey = new SearchCaseParticipantDetailsKey();
      searchKey.caseParticipantTypeList = key.caseParticipantRoleTypeList;
      searchKey.caseID = key.caseID;

      searchCaseParticipantDetailsList = listCaseParticipant(searchKey);

      CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();

      for(int i = 0; i &lt; searchCaseParticipantDetailsList.searchCaseParticipantDetails.size(); i++){

      SearchCaseParticipantDetails searchCaseParticipantDetails =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i);

      caseParticipantRoleKey.caseParticipantRoleID = searchCaseParticipantDetails.caseParticipantRoleID;

      ReadEmploymentDetailsList detailsList = listEmploymentsForCaseParticipant(caseParticipantRoleKey);

    for (int j = 0; j &lt; detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.size(); j++) {
      if (detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).employmentID==key.employmentID) {
      returnStruct.employmentID = key.employmentID;
      returnStruct.caseID = key.caseID;
      returnStruct.caseParticipantRoleID=caseParticipantRoleKey.caseParticipantRoleID;
      returnStruct.employerConcernRoleID=detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).employerConcernRoleID;
      }
    }
    }
    return returnStruct;
  }

  //___________________________________________________________________________
  /**
  * Retrieves the Employment list for the Case Participant types
  *
  * @param key Contains the case identifier and participant type list
  * @return List of employments
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public CreateWizardEmploymentDetailsList listEmploymentsForCaseParticipantTypeList(SearchCaseParticipantDetailsKey key)
    throws AppException, InformationalException {

    SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
      new SearchCaseParticipantDetailsList();

      CreateWizardEmploymentDetailsList createWizardEmploymentDetailsList = new CreateWizardEmploymentDetailsList();

      searchCaseParticipantDetailsList = listCaseParticipantWithoutDuplicates(key);

      CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();

      for(int i = 0; i &lt; searchCaseParticipantDetailsList.searchCaseParticipantDetails.size(); i++){

      SearchCaseParticipantDetails searchCaseParticipantDetails =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i);

        caseParticipantRoleKey.caseParticipantRoleID = searchCaseParticipantDetails.caseParticipantRoleID;

        ReadEmploymentDetailsList detailsList = listEmploymentsForCaseParticipant(caseParticipantRoleKey);

        if(detailsList != null) {

          for (int j = 0; j &lt; detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.size(); j++) {

            CreateWizardEmploymentDetails createWizardEmploymentDetails = new CreateWizardEmploymentDetails();
            createWizardEmploymentDetails.caseParticipantRoleID = searchCaseParticipantDetails.caseParticipantRoleID;
            createWizardEmploymentDetails.caseParticipantFullName = searchCaseParticipantDetails.name;
			createWizardEmploymentDetails.nameAndAgeOpt = searchCaseParticipantDetails.nameAndAgeOpt;
            createWizardEmploymentDetails.employmentID = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).employmentID;
            createWizardEmploymentDetails.employerName = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).employerName;
            createWizardEmploymentDetails.fromDate = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).fromDate;
            createWizardEmploymentDetails.toDate = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).toDate;
            createWizardEmploymentDetails.occupationType = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).occupationType;
            createWizardEmploymentDetails.primaryCurrentInd = detailsList.readMultiByConcernRoleIDEmplResult.details.dtls.item(j).primaryCurrentInd;

            createWizardEmploymentDetailsList.dtls.addRef(createWizardEmploymentDetails);
          }
        }
      }

      return createWizardEmploymentDetailsList;
  }
 <!-- BEGIN, CR00118883, POB -->
 //___________________________________________________________________________
 /**
 * Retrieves the Case Participants by Case
 *
 * @param key Contains the case identifier
 * @return List of Case Participants
 *
 * @throws AppException
 *           Generic AppException Signature
 * @throws InformationalException
 *           Generic InformationalException Signature
 */
 @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
 public CaseParticipantIDNameAndDOBDetailsList listCaseParticipantsExtraDetails(
   SearchCaseParticipantDetailsKey key)
     throws AppException, InformationalException {

   SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
     new SearchCaseParticipantDetailsList();

   CaseParticipantIDNameAndDOBDetailsList caseParticipantIDNameAndDOBDetailsList =
     new CaseParticipantIDNameAndDOBDetailsList();

   searchCaseParticipantDetailsList = listCaseParticipant(key);

   CaseParticipantRoleKey caseParticipantRoleKey =
     new CaseParticipantRoleKey();

   CaseParticipantRole caseParticipantRoleObj =
     CaseParticipantRoleFactory.newInstance();

   Person personObj = PersonFactory.newInstance();

   PersonKey personKey = new PersonKey();

   for(int i = 0; i &lt; searchCaseParticipantDetailsList.searchCaseParticipantDetails.size(); i++){

     CaseParticipantIDNameAndDOBDetails caseParticipantIDNameAndDOBDetails =
       new CaseParticipantIDNameAndDOBDetails();

     SearchCaseParticipantDetails searchCaseParticipantDetails =
       searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i);

     caseParticipantRoleKey.caseParticipantRoleID =
       searchCaseParticipantDetails.caseParticipantRoleID;

     CaseParticipantRoleDtls caseParticipantRoleDtls =
       caseParticipantRoleObj.read(caseParticipantRoleKey);

     personKey.concernRoleID = caseParticipantRoleDtls.participantRoleID;

     NotFoundIndicator notFoundInd = new NotFoundIndicator();

     PersonDtls personDtls = personObj.read(notFoundInd, personKey);

     caseParticipantIDNameAndDOBDetails.caseParticipantRoleID =
       searchCaseParticipantDetails.caseParticipantRoleID;
     caseParticipantIDNameAndDOBDetails.name =
       searchCaseParticipantDetails.name;
     caseParticipantIDNameAndDOBDetails.type =
       caseParticipantRoleDtls.typeCode;

     if(notFoundInd.isNotFound()){
       caseParticipantIDNameAndDOBDetails.dateOfBirth =
         Date.kZeroDate;
     }else{
       caseParticipantIDNameAndDOBDetails.dateOfBirth =
         personDtls.dateOfBirth;
     }

     caseParticipantIDNameAndDOBDetailsList.dtls.addRef(caseParticipantIDNameAndDOBDetails);
   }

   return caseParticipantIDNameAndDOBDetailsList;
 }

  // ___________________________________________________________________________
  /**
  * Gets a list of all evidence records of a given type on a case. The returned list
  * combines the active and in edit lists.
  *
  * @param key The evidence type and the case identifier.
  *
  * @return The list of evidence records.
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ECParentDtls getEvidenceListByTypeWithCPR(CaseIDAndEvidenceTypeKey key)
  throws AppException, InformationalException {

  curam.core.sl.entity.intf.CaseParticipantRole caseParticipantRoleObj =
  curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance();

  ECParentDtls parentDtls = getEvidenceListByType(key);

  // for each record replace the participantID with
  // an appropriate caseParticipantRoleID
  for(int i = 0; i &lt; parentDtls.parentList.dtls.size(); i++){

  long participantID = parentDtls.parentList.dtls.item(i).participantID;

  curam.core.sl.entity.struct.CaseIDParticipantRoleKey caseIDParticipantRoleKey =
    new curam.core.sl.entity.struct.CaseIDParticipantRoleKey();
  caseIDParticipantRoleKey.caseID =
    key.caseID;
  caseIDParticipantRoleKey.participantRoleID =
    participantID;

  parentDtls.parentList.dtls.item(i).participantID =
    caseParticipantRoleObj.readCaseParticipantRoleID(caseIDParticipantRoleKey).caseParticipantRoleID;

  }

  // return the list of records
  return parentDtls;
  }
      <!-- END, CR00118883 -->
  <!-- BEGIN, CR00114649, POB -->
  //___________________________________________________________________________
  /**
  * Returns a list of evidence types for PreAssociation to the specified type
  *
  * @param evidenceTypeKey The type of evidence being created
  *
  * @return List of possible types for the PreAssociation
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public EvidenceTypeAndDescriptionList getEvidenceTypeListForPreAssociation(EvidenceTypeKey
    evidenceTypeKey) throws AppException, InformationalException{

    EvidenceTypeAndDescriptionList evidenceTypeAndDescriptionList =
      new EvidenceTypeAndDescriptionList();

    <xsl:for-each select="EvidenceEntity/Relationships">

    <xsl:if test="count(PreAssociation) &gt; 0">
      <xsl:variable name="currentTypeName" select="../@name"/>
      <xsl:variable name="ucCurrentTypeName"><xsl:value-of select="translate($currentTypeName, $lcletters, $ucletters)"/></xsl:variable>
      if(evidenceTypeKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$ucCurrentTypeName"/>)){

      EvidenceTypeAndDescription evidenceTypeAndDescription;

      <xsl:for-each select="PreAssociation">
        <xsl:variable name="preAssociationTypeName" select="@to"/>
        <xsl:variable name="ucPreAssociationTypeName"><xsl:value-of select="translate($preAssociationTypeName, $lcletters, $ucletters)"/></xsl:variable>

      evidenceTypeAndDescription = new EvidenceTypeAndDescription();
      evidenceTypeAndDescription.evidenceType =
        CASEEVIDENCE.<xsl:value-of select="$ucPreAssociationTypeName"/>;
      evidenceTypeAndDescription.description =
      CodeTable.getOneItem(CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$ucPreAssociationTypeName"/>);

      evidenceTypeAndDescriptionList.dtls.addRef(evidenceTypeAndDescription);
      </xsl:for-each>
    }
    </xsl:if>
    </xsl:for-each>
    return evidenceTypeAndDescriptionList;
  }
  <!-- END, CR00114649 -->
  <!-- BEGIN, CR00101875, POB -->
 // ___________________________________________________________________________
  /**
  * Returns the summary details of the specified evidence for use in a
  * UIM page link
  *
  * @param eiEvidenceKey Details of the evidence to search for
  *
  * @return Details for use in the UIM page link
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ParentEvidenceLink getParentEvidenceLink(EIEvidenceKey eiEvidenceKey)
    throws AppException, InformationalException{

    ParentEvidenceLink parentEvidenceLink = new ParentEvidenceLink();

    EIFieldsForListDisplayDtls eiFieldsForListDisplayDtls =
      EvidenceControllerFactory.newInstance()
        .getEvidenceSummaryDetails(eiEvidenceKey);

    parentEvidenceLink.parentID = eiEvidenceKey.evidenceID;
    parentEvidenceLink.parentType = eiEvidenceKey.evidenceType;
    parentEvidenceLink.displayText =
      curam.core.sl.impl.LocalizableXMLStringHelper
      .toClientFormattedText(eiFieldsForListDisplayDtls.summary);

    return parentEvidenceLink;

  }
  <!-- END, CR00101875 -->
  <!-- BEGIN, CR00100662, POB -->
 // ___________________________________________________________________________
  /**
  * Returns a list of Associated Evidence for the given evidenceID, evidenceType and caseID
  *
  * @param key Details of the evidence whose associations we are searching for
  *
  * @return List of Associated Evidence
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ECParentDtls listAssociatedEvidence(EvidenceListKey key) throws
    AppException, InformationalException{

  Evidence evidenceObj = EvidenceFactory.newInstance();
  EvidenceListDetails evidenceListDetails = evidenceObj.listEvidence(key);

  ECParentDtls ecParentDtls = new ECParentDtls();
  ECParentEvidenceDtls ecParentEvidenceDtls;

  for (int i = 0; i &lt; evidenceListDetails.dtls.activeList.dtls.size(); i++){

  ecParentEvidenceDtls = new ECParentEvidenceDtls();
  ecParentEvidenceDtls.assign(evidenceListDetails.dtls.activeList.dtls.item(i));

  ecParentDtls.parentList.dtls.addRef(ecParentEvidenceDtls);
  }

  for (int i = 0; i &lt; evidenceListDetails.dtls.newAndUpdateList.dtls.size(); i++){

  ecParentEvidenceDtls = new ECParentEvidenceDtls();
  ecParentEvidenceDtls.assign(evidenceListDetails.dtls.newAndUpdateList.dtls.item(i));

  ecParentDtls.parentList.dtls.addRef(ecParentEvidenceDtls);
  }

  return ecParentDtls;
  }
  <!-- END, CR00100662 -->
  // ___________________________________________________________________________
  /**
   * Gets the primary case participant concern role id.
   *
   * @param key Case ID key
   *
   * @return Concern Role ID.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
   @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ConcernRoleDetails getPrimaryCaseParticipantConcernRoleID(
    CaseIDKey key) throws AppException, InformationalException {

    // Return struct
    ConcernRoleDetails concernRoleDetails =
      new ConcernRoleDetails();

    // CaseHeader manipulation variables
    CaseHeader caseHeaderObj =
      CaseHeaderFactory.newInstance();
    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();

    caseHeaderKey.caseID = key.caseID;

    ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
      caseHeaderObj.readParticipantRoleID(caseHeaderKey);

    concernRoleDetails.concernRoleID = readParticipantRoleIDDetails.concernRoleID;

    return concernRoleDetails;

  }

   //___________________________________________________________________________
   /**
    * Retrieves the Case Participants that have a unique concernRoleID by Case.
    *
    * @param key Contains the case identifier
    * @return List of Case Participants
    *
    * @throws AppException
    *           Generic AppException Signature
    * @throws InformationalException
    *           Generic InformationalException Signature
    */
   public SearchCaseParticipantDetailsList listCaseParticipantWithoutDuplicates(
     SearchCaseParticipantDetailsKey key)
     throws AppException, InformationalException {
     SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
       new SearchCaseParticipantDetailsList();
     curam.core.sl.infrastructure.intf.GeneralUtility generalUtilityObj =
       GeneralUtilityFactory.newInstance();
     searchCaseParticipantDetailsList =
       generalUtilityObj.listActiveCaseParticipantsByCaseIDTypeFilteredByConcern(key);

     return listCaseParticipantCommon(searchCaseParticipantDetailsList, key);
   }

  //___________________________________________________________________________
  /**
   * Retrieves the Case Participants by Case
   *
   * @param key Contains the case identifier
   * @return List of Case Participants
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public SearchCaseParticipantDetailsList listCaseParticipant(
    SearchCaseParticipantDetailsKey key)
    throws AppException, InformationalException {

    SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
      new SearchCaseParticipantDetailsList();

    curam.core.sl.infrastructure.intf.GeneralUtility generalUtilityObj =
      GeneralUtilityFactory.newInstance();

    searchCaseParticipantDetailsList =
      generalUtilityObj.listActiveCaseParticipantsByCaseIDType(key);

    return listCaseParticipantCommon(searchCaseParticipantDetailsList, key);
  }

  //___________________________________________________________________________
  /**
   * Private method that contains common code fore listCaseParticipant methods.
   * @param searchCaseParticipantDetailsList
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private SearchCaseParticipantDetailsList listCaseParticipantCommon(
    SearchCaseParticipantDetailsList searchCaseParticipantDetailsList, SearchCaseParticipantDetailsKey key)
    throws AppException, InformationalException {
    
    final curam.core.sl.infrastructure.intf.GeneralUtility generalUtilityObj =
      GeneralUtilityFactory.newInstance();
    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();
    curam.core.sl.intf.CaseParticipantRole caseParticipantRoleObj =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance();
    CaseParticipantRoleKey caseParticipantRoleKey;
    ParticipantRoleIDAndNameDetails
      participantRoleIDAndName;
    ReadConcernRoleKey readConcernRoleKey;
    ReadConcernRoleDetails readConcernRole;
    ConcernRolesDetails concernRolesDetails;
    
    <xsl:if test="@displayAltID='Yes'">

    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
    concatenateNameAndAlternateIDSSNKey.caseID = key.caseID;

    </xsl:if>
    
    // Process the list, setting name to Name and Alternate ID
    int listSize = searchCaseParticipantDetailsList.searchCaseParticipantDetails.size();

     for (int i = 0; i &lt; listSize; i++) {

      caseParticipantRoleKey = new CaseParticipantRoleKey();
      caseParticipantRoleKey.caseParticipantRoleID =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i).caseParticipantRoleID;
      participantRoleIDAndName =
        caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

      readConcernRoleKey = new ReadConcernRoleKey();
      readConcernRoleKey.concernRoleID = participantRoleIDAndName.participantRoleID;
      readConcernRole =
        maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

      concernRolesDetails = new ConcernRolesDetails();
      concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
      concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
      concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

      searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i).name =
        maintainConcernRoleDetailsObj.appendAgeToName(
          concernRolesDetails ).concernRoleName;
      
      <xsl:if test="@displayAltID='Yes'">

      concatenateNameAndAlternateIDSSNKey.name =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i).name;
      concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i)
          .caseParticipantRoleID;

      ConcatenateNameAndAlternateIDSSNDetails concatenateNameAndAlternateIDSSNDetails =
        generalUtilityObj.concatenateNameAndAlternateIDSSN(
          concatenateNameAndAlternateIDSSNKey);

      searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(i)
        .name = concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
      </xsl:if>
      
    }

    return searchCaseParticipantDetailsList;
  }

  <!-- VM -->
  //___________________________________________________________________________
  /**
   * Reads Case Participant details by Case Participant Role
   *
   * @param key Contains the Case Participant Role identifier
   * @return Case Participant details
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReadEmploymentDetailsList listEmploymentsForCaseParticipant(
    CaseParticipantRoleKey key) throws AppException, InformationalException {

    ReadEmploymentDetailsList readEmploymentDetailsList =
      new ReadEmploymentDetailsList();

    curam.core.struct.MaintainPersonEmploymentKey maintainPersonEmploymentKey =
      new curam.core.struct.MaintainPersonEmploymentKey();

    curam.core.sl.entity.struct.CaseParticipantRoleDtls dtls =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance().read(key);

    // populate the read employment key
    maintainPersonEmploymentKey.concernRoleID = dtls.participantRoleID;

    // read list of employments
    readEmploymentDetailsList.readMultiByConcernRoleIDEmplResult =
      curam.core.fact.MaintainPersonEmploymentFactory.newInstance().readmultiByConcernRoleID(
        maintainPersonEmploymentKey);

    return readEmploymentDetailsList;
  }

  //___________________________________________________________________________
  /**
   * Reads Participant name by Case Participant Role
   *
   * @param key Contains the Case Participant Role identifier
   * @return Participant name details
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public SearchCaseParticipantDetails readNameByCaseParticipantRole(
    CaseParticipantRoleKey key, CaseKey caseKey)
    throws AppException, InformationalException {

    SearchCaseParticipantDetails searchCaseParticipantDetails =
      new SearchCaseParticipantDetails();

    curam.core.sl.entity.struct.CaseParticipantRoleDtls dtls =
      curam.core.sl.entity.fact.CaseParticipantRoleFactory.newInstance().read(key);

    curam.core.struct.ConcernRoleKey concernRoleKey =
      new curam.core.struct.ConcernRoleKey();

    concernRoleKey.concernRoleID = dtls.participantRoleID;

    curam.core.struct.ConcernRoleDtls concernRoleDtls =
      curam.core.fact.ConcernRoleFactory.newInstance().read(concernRoleKey);

    searchCaseParticipantDetails.name = concernRoleDtls.concernRoleName;
    searchCaseParticipantDetails.caseParticipantRoleID =
      key.caseParticipantRoleID;

    // Create the general utility object
    GeneralUtility generalUtilityObj = GeneralUtilityFactory.newInstance();

    // append age to the name
    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();

    curam.core.sl.intf.CaseParticipantRole caseParticipantRoleObj =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;
    ParticipantRoleIDAndNameDetails participantRoleIDAndName =
      caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

    ReadConcernRoleKey readConcernRoleKey = new ReadConcernRoleKey();
    readConcernRoleKey.concernRoleID = participantRoleIDAndName.participantRoleID;
    ReadConcernRoleDetails readConcernRole =
      maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

    ConcernRolesDetails concernRolesDetails = new ConcernRolesDetails();
    concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
    concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
    concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

    searchCaseParticipantDetails.nameAndAgeOpt = curam.core.sl.impl.LocalizableXMLStringHelper
        .toClientFormattedText(maintainConcernRoleDetailsObj
            .appendAgeToName(concernRolesDetails).concernRoleName);

    <xsl:if test="@displayAltID='Yes'">

    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj = CachedCaseHeaderFactory.newInstance();
    CaseHeaderKey CaseHeaderkey = new CaseHeaderKey();

    CaseHeaderkey.caseID = caseKey.caseID;

    if (caseHeaderObj.read(CaseHeaderkey).integratedCaseID != 0) {
      caseKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }

    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
    concatenateNameAndAlternateIDSSNKey.caseID = caseKey.caseID;
    concatenateNameAndAlternateIDSSNKey.name =
      searchCaseParticipantDetails.name;
    concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;

    // Use Utility to work out Participants Full Name
    ConcatenateNameAndAlternateIDSSNDetails concatenateNameAndAlternateIDSSNDetails =
      generalUtilityObj.concatenateNameAndAlternateIDSSN(
        concatenateNameAndAlternateIDSSNKey);

    searchCaseParticipantDetails.name =
      concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
    </xsl:if>

    return searchCaseParticipantDetails;
  }
  <!-- END, VM -->

  //___________________________________________________________________________
  /**
   * Reads Case Participant details by Case
   *
   * @param key Contains the case identfier
   * @return Case Participant details
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public SearchCaseParticipantDetails readCaseParticipant(SearchCaseParticipantDetailsKey key)
    throws AppException, InformationalException {

    // Create a return structure
    SearchCaseParticipantDetails searchCaseParticipantDetails = null;

    // Call SearchByCaseID
    SearchCaseParticipantDetailsList searchCaseParticipantDetailsList =
      listCaseParticipant(key);

    // Process the list, setting name to Name and Alternate ID
    if (!searchCaseParticipantDetailsList.searchCaseParticipantDetails.isEmpty()) {
      searchCaseParticipantDetails =
        searchCaseParticipantDetailsList.searchCaseParticipantDetails.item(0);
    // BEGIN, CR00443631, JAF
    } else {
      // If no results are found, alert user;
      // "There are no matching items based on the Search Criteria entered."
      throw new AppException(curam.message.GENERALSEARCH.INF_SEARCH_NORECORDSFOUND);
    }
    // END, CR00443631

    // Create the general utility object
    GeneralUtility generalUtilityObj = GeneralUtilityFactory.newInstance();

    // append age to the name
    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();

    curam.core.sl.intf.CaseParticipantRole caseParticipantRoleObj =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;
    ParticipantRoleIDAndNameDetails participantRoleIDAndName =
      caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

    ReadConcernRoleKey readConcernRoleKey = new ReadConcernRoleKey();
    readConcernRoleKey.concernRoleID = participantRoleIDAndName.participantRoleID;
    ReadConcernRoleDetails readConcernRole =
      maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

    ConcernRolesDetails concernRolesDetails = new ConcernRolesDetails();
    concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
    concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
    concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

    searchCaseParticipantDetails.name =
      maintainConcernRoleDetailsObj.appendAgeToName(
        concernRolesDetails).concernRoleName;
    <xsl:if test="@displayAltID='Yes'">

    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj = CachedCaseHeaderFactory.newInstance();
    CaseHeaderKey CaseHeaderkey = new CaseHeaderKey();
    CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseID;
    CaseHeaderkey.caseID = key.caseID;

    if (caseHeaderObj.read(CaseHeaderkey).integratedCaseID != 0) {
      key.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }

    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
    concatenateNameAndAlternateIDSSNKey.caseID = key.caseID;
    concatenateNameAndAlternateIDSSNKey.name =
      searchCaseParticipantDetails.name;
    concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;

    // Use Utility to work out Participants Full Name
    ConcatenateNameAndAlternateIDSSNDetails concatenateNameAndAlternateIDSSNDetails =
      generalUtilityObj.concatenateNameAndAlternateIDSSN(
        concatenateNameAndAlternateIDSSNKey);

    searchCaseParticipantDetails.name =
      concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
    </xsl:if>

    // return result
    return searchCaseParticipantDetails;
  }

  //___________________________________________________________________________
  /**
   * Reads Case Participant details by evidenceID
   *
   * @param key Contains the evidence identifier
   * @return Case Participant details
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public SearchCaseParticipantDetails readCaseParticipantByEvidenceID(EvidenceCaseKey key)
    throws AppException, InformationalException {

    // Create the general utility object to preform the search
    GeneralUtility generalUtilityObj = GeneralUtilityFactory.newInstance();

    // Create a return structure
    SearchCaseParticipantDetails searchCaseParticipantDetails =
      generalUtilityObj.readCaseParticipantByEvidenceID(key);

    // append age to the name
    MaintainConcernRoleDetails maintainConcernRoleDetailsObj =
      MaintainConcernRoleDetailsFactory.newInstance();

    curam.core.sl.intf.CaseParticipantRole caseParticipantRoleObj =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();
    caseParticipantRoleKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;
    ParticipantRoleIDAndNameDetails participantRoleIDAndName =
      caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(caseParticipantRoleKey);

    ReadConcernRoleKey readConcernRoleKey = new ReadConcernRoleKey();
    readConcernRoleKey.concernRoleID = participantRoleIDAndName.participantRoleID;
    ReadConcernRoleDetails readConcernRole =
      maintainConcernRoleDetailsObj.readConcernRole(readConcernRoleKey);

    ConcernRolesDetails concernRolesDetails = new ConcernRolesDetails();
    concernRolesDetails.concernRoleID = readConcernRole.concernRoleID;
    concernRolesDetails.concernRoleType = readConcernRole.concernRoleType;
    concernRolesDetails.concernRoleName = readConcernRole.concernRoleName;

    searchCaseParticipantDetails.name =
      maintainConcernRoleDetailsObj.appendAgeToName(
        concernRolesDetails).concernRoleName;
    <xsl:if test="@displayAltID='Yes'">

    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj = CachedCaseHeaderFactory.newInstance();
    CaseHeaderKey CaseHeaderkey = new CaseHeaderKey();
    CaseKey caseKey = new CaseKey();

    caseKey.caseID = key.caseIDKey.caseID;
    CaseHeaderkey.caseID = key.caseIDKey.caseID;

    if (caseHeaderObj.read(CaseHeaderkey).integratedCaseID != 0) {
      key.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }

    ConcatenateNameAndAlternateIDSSNKey concatenateNameAndAlternateIDSSNKey =
      new ConcatenateNameAndAlternateIDSSNKey();

    concatenateNameAndAlternateIDSSNKey.type =
      CONCERNROLEALTERNATEID.getDefaultCode();
    concatenateNameAndAlternateIDSSNKey.status = RECORDSTATUS.NORMAL;
    concatenateNameAndAlternateIDSSNKey.caseID = key.caseIDKey.caseID;
    concatenateNameAndAlternateIDSSNKey.name =
      searchCaseParticipantDetails.name;
    concatenateNameAndAlternateIDSSNKey.caseParticipantRoleID =
      searchCaseParticipantDetails.caseParticipantRoleID;

    // Use Utility to work out Participants Full Name
    ConcatenateNameAndAlternateIDSSNDetails concatenateNameAndAlternateIDSSNDetails =
      generalUtilityObj.concatenateNameAndAlternateIDSSN(
        concatenateNameAndAlternateIDSSNKey);

    searchCaseParticipantDetails.name =
      concatenateNameAndAlternateIDSSNDetails.nameAndAlternateID;
    </xsl:if>

    return searchCaseParticipantDetails;
  }

  //___________________________________________________________________________
  /**
   * Function to list Core employments based on an evidence id and type
   * pair
   *
   * @param key Contains the evidence id and evidence type
   * @return List of Core employments
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReadEmploymentDetailsList listEmploymentByEvidenceKey(EIEvidenceKey key)
      throws AppException, InformationalException {

    return GeneralUtilityFactory.newInstance().
      listEmploymentByEvidenceKey(key);
  }

  //___________________________________________________________________________
  /**
   * Reads a Employment record.
   *
   * @param key contains ID of record to read.
   * @return Employment evidence details read.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public EmploymentDetails readEmploymentByCaseParticipant
    (CaseParticipantEmployerConcernRoleKey key)
      throws AppException, InformationalException {

    return GeneralUtilityFactory.newInstance().
      readEmploymentByCaseParticipant(key);
  }


  // ___________________________________________________________________________
  /**
   * Gets a list of potential parents when creating a child record without the
   * help of a parent identifier.
   *
   * @param key The child's evidence type and the case identifier.
   *
   * @return The list of potential parent evidence details.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ECParentDtls getParentEvidenceList(CaseIDAndEvidenceTypeKey key)
    throws AppException, InformationalException {

    ECParentDtls parentDtls = new ECParentDtls();

    // get the potential parent types to search for
    EvidenceTypeDtls evidenceTypeDtls = new EvidenceTypeDtls();

    evidenceTypeDtls.evidenceType = key.evidenceType;
    EvidenceTypeDtlsList evidenceTypeDtlsList =
      getParentTypeList(evidenceTypeDtls);

    EIListCaseEvidenceKey eiListCaseEvidenceKey = new EIListCaseEvidenceKey();

    eiListCaseEvidenceKey.caseID = key.caseID;

    // for each parent type and the case id, get the active and in-edit lists
    for (int i = 0; i &lt; evidenceTypeDtlsList.dtls.size(); i++) {

      eiListCaseEvidenceKey.evidenceType =
        evidenceTypeDtlsList.dtls.item(i).evidenceType;

      ECEvidenceForListPageDtls ecEvidenceForListPageDtls =
        EvidenceControllerFactory.newInstance().listAllForEvidenceListPage(
          eiListCaseEvidenceKey);

      // only add the active and in-edit lists to the return struct as we're not
      // interested in pending removal or verification lists on the create screen
      ECParentEvidenceDtls ecParentEvidenceDtls;

      for (int j = 0; j &lt; ecEvidenceForListPageDtls.activeList.dtls.size(); j++) {
        ecParentEvidenceDtls = new ECParentEvidenceDtls();
        ecParentEvidenceDtls.assign(
          ecEvidenceForListPageDtls.activeList.dtls.item(j));
        parentDtls.parentList.dtls.add(ecParentEvidenceDtls);
      }
      for (int j = 0; j
        &lt; ecEvidenceForListPageDtls.newAndUpdateList.dtls.size(); j++) {
        ecParentEvidenceDtls = new ECParentEvidenceDtls();
        ecParentEvidenceDtls.assign(
          ecEvidenceForListPageDtls.newAndUpdateList.dtls.item(j));
        parentDtls.parentList.dtls.add(ecParentEvidenceDtls);
      }
    }

    // return the list of parent details
    return parentDtls;
  }

  // ___________________________________________________________________________
  /**
  * Gets a list of all evidence records of a given type on a case. The returned list
  * combines the active and in edit lists.
  *
  * @param key The evidence type and the case identifier.
  *
  * @return The list of evidence records.
  *
  * @throws AppException
  *           Generic AppException Signature
  * @throws InformationalException
  *           Generic InformationalException Signature
  */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ECParentDtls getEvidenceListByType(CaseIDAndEvidenceTypeKey key)
    throws AppException, InformationalException {

    ECParentDtls parentDtls = new ECParentDtls();

    EIListCaseEvidenceKey eiListCaseEvidenceKey = new EIListCaseEvidenceKey();

    eiListCaseEvidenceKey.caseID = key.caseID;

    eiListCaseEvidenceKey.evidenceType =
    key.evidenceType;

    ECEvidenceForListPageDtls ecEvidenceForListPageDtls =
    EvidenceControllerFactory.newInstance().listAllForEvidenceListPage(
    eiListCaseEvidenceKey);

    // only add the active and in-edit lists to the return struct as we're not
    // interested in pending removal or verification lists on the create screen
    ECParentEvidenceDtls ecParentEvidenceDtls;

    for (int j = 0; j &lt; ecEvidenceForListPageDtls.activeList.dtls.size(); j++) {
    ecParentEvidenceDtls = new ECParentEvidenceDtls();
    ecParentEvidenceDtls.assign(
    ecEvidenceForListPageDtls.activeList.dtls.item(j));
    parentDtls.parentList.dtls.add(ecParentEvidenceDtls);
    }
    for (int j = 0; j
    &lt; ecEvidenceForListPageDtls.newAndUpdateList.dtls.size(); j++) {
    ecParentEvidenceDtls = new ECParentEvidenceDtls();
    ecParentEvidenceDtls.assign(
    ecEvidenceForListPageDtls.newAndUpdateList.dtls.item(j));
    parentDtls.parentList.dtls.add(ecParentEvidenceDtls);
    }

    // return the list of recorsd
    return parentDtls;
  }

  // ___________________________________________________________________________
  /**
   * Accessor function to return the caseParticipantRoleID for any evidence
   * type. This function is implemented by all evidence types. If the entity
   * does not store the caseParticipantRoleID it simply requests it from it's
   * parent.
   *
   * @param key EIEvidenceKey containing an evidence ID and type
   * @return CaseParticipantRoleID Struct containing the caseParticipantRoleID
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public SearchCaseParticipantDetails <xsl:value-of select="$facade_createPage_GetAssociatedCaseParticipantReadMethod"/> (EIEvidenceKey key)
    throws AppException, InformationalException {

    <!-- BEGIN, CR00101870, POB -->
    <!--
      For safety check the evidenceID received is not 0
    -->
    if(key.evidenceID == 0){
      return new SearchCaseParticipantDetails();
    }
    <!-- END, CR00101870 -->
    <!-- BEGIN, CR00098592, CD -->
    <xsl:for-each select="EvidenceEntity">
    <xsl:if test="position()>1">else </xsl:if>if(key.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)) {

      return <xsl:value-of select="$javaEvidenceCodePath"/>.service.fact.<xsl:value-of select="@name"/>Factory.
        newInstance().getCaseParticipantRoleID(key);

    }</xsl:for-each>

    <xsl:if test="count(EvidenceEntity)>0">else </xsl:if>return new SearchCaseParticipantDetails();
    <!-- END, CR00098592 -->
  }

  // ___________________________________________________________________________
    /**
     * Accessor function to return the caseParticipantRoleID for any evidence
     * type. This function is implemented by all evidence types. If the entity
     * does not store the caseParticipantRoleID it simply requests it from it's
     * parent.
     *
     * @param key EIEvidenceKey containing an evidence ID and type
     * @return CaseParticipantRoleID Struct containing the caseParticipantRoleID
     *
     * @throws AppException
     *           Generic AppException Signature
     * @throws InformationalException
     *           Generic InformationalException Signature
     */
    @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
    public SearchCaseParticipantDetails <xsl:value-of select="$facade_modifyViewPage_GetAssociatedCaseParticipantReadMethod"/> (EIEvidenceKey key)
      throws AppException, InformationalException {

      <!--
        For safety check the evidenceID received is not 0
      -->
      if(key.evidenceID == 0){
        return new SearchCaseParticipantDetails();
      }

      ChildKey childKey = new ChildKey();
      // populate the search key
      childKey.childID = key.evidenceID;
      childKey.childType = key.evidenceType;

      EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();
      ParentKeyList parentList = new ParentKeyList();

      // read the list of parents
      parentList = EvidenceRelationshipFactory.newInstance().searchByChild(childKey);

      if (parentList.dtls.size() == 0) {
        return new SearchCaseParticipantDetails();
      }

      for (int i = 0; i &lt; parentList.dtls.size(); i++) {

        eiEvidenceKey.evidenceID = parentList.dtls.item(0).parentID;
        eiEvidenceKey.evidenceType = parentList.dtls.item(0).parentType;

        <xsl:for-each select="EvidenceEntity">
        <xsl:if test="position()>1">else </xsl:if>if(key.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)) {

          <xsl:for-each select="Relationships/Parent">
          if (eiEvidenceKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)) {
            return getCaseParticipantRoleID(eiEvidenceKey);
          }
          </xsl:for-each>

        }</xsl:for-each>

      }

      return new SearchCaseParticipantDetails();
  }


  // ___________________________________________________________________________
  /**
   * Gets a list of parent evidence types that may relate to a child evidence
   * type.
   *
   * @param key The child's evidence type details.
   *
   * @return The list of parent evidence types.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public EvidenceTypeDtlsList getParentTypeList(EvidenceTypeDtls key)
    throws AppException, InformationalException {

    // create the return struct
    EvidenceTypeDtlsList parentTypeDtlsList = new EvidenceTypeDtlsList();
    EvidenceTypeDtls evidenceTypeDtls;

    // get the parent types based on the child type
    <xsl:for-each select="//EvidenceEntities/EvidenceEntity[count(Relationships/Parent)>0]">
      <xsl:if test="position()>1">
    else </xsl:if>if (key.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>)) {
      <xsl:for-each select="Relationships/Parent">
      evidenceTypeDtls = new EvidenceTypeDtls();
      evidenceTypeDtls.evidenceType = CASEEVIDENCE.<xsl:value-of select="translate(@name, $lcletters, $ucletters)"/>;
      parentTypeDtlsList.dtls.add(evidenceTypeDtls);</xsl:for-each>
    }
    </xsl:for-each>

    return parentTypeDtlsList;
  }



      </redirect:write>
<!--
    </xsl:for-each>
-->
    <xsl:for-each select="EvidenceEntity">

      <xsl:variable name="entityElement" select="."/>
      <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
      <xsl:variable name="ucName"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>
      <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>

<!--
      <xsl:variable name="filename"><xsl:value-of select="//FacadeLayer/@name"/>.java</xsl:variable>
-->

      <redirect:write select="$filename">
  // ___________________________________________________________________________
  /**
   * Gets the default creation details for <xsl:value-of select="$EntityName"/>.
   *
   * @return The default creation details for <xsl:value-of select="$EntityName"/>.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
   @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   public <xsl:value-of select="$EntityName"/>EvidenceDetails getDefaultCreate<xsl:value-of select="$EntityName"/>Details()
    throws AppException, InformationalException {

    return new <xsl:value-of select="$EntityName"/>EvidenceDetails();

   }

  // ___________________________________________________________________________
  /**
   * Creates a <xsl:value-of select="$EntityName"/> evidence record.
   *
   * @param dtls Details of the new evidence record to be created.
   *
   * @return The details of the created record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
   @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   public ReturnEvidenceDetails create<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setInformationalManager();
    <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = dtls.caseIDKey.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = dtls.caseIDKey.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      dtls.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>
    // <xsl:value-of select="$EntityName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // create the <xsl:value-of select="$EntityName"/> record and populate the return details
    ReturnEvidenceDetails createdEvidenceDetails =
      evidenceObj.create<xsl:value-of select="$EntityName"/>Evidence(dtls);

    <xsl:for-each select="Relationships/Child[@coCreate='Yes']">

      <xsl:variable name="childEntityName" select="@name"/>
      <xsl:variable name="lcChildEntityName"><xsl:value-of select="translate(substring($childEntityName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($childEntityName, 2)"/></xsl:variable>

      <xsl:value-of select="$childEntityName"/>EvidenceDetails <xsl:value-of select="$lcChildEntityName"/> = new <xsl:value-of select="$childEntityName"/>EvidenceDetails();

      <xsl:value-of select="$lcChildEntityName"/>.dtls.assign(dtls.<xsl:value-of select="$lcChildEntityName"/>.dtls);
      <xsl:value-of select="$lcChildEntityName"/>.caseIDKey.assign(dtls.caseIDKey);
      <xsl:value-of select="$lcChildEntityName"/>.parEvKey.assign(createdEvidenceDetails.evidenceKey);
      <xsl:value-of select="$lcChildEntityName"/>.descriptor.receivedDate = dtls.descriptor.receivedDate;

      <xsl:value-of select="$childEntityName"/><xsl:text> </xsl:text><xsl:value-of select="$lcChildEntityName"/>EvidenceObj = <xsl:value-of select="$childEntityName"/>Factory.newInstance();

      <xsl:value-of select="$lcChildEntityName"/>EvidenceObj.create<xsl:value-of select="$childEntityName"/>Evidence(<xsl:value-of select="$lcChildEntityName"/>);

    </xsl:for-each>

    createdEvidenceDetails.warnings =
      EvidenceControllerFactory.newInstance().getWarnings();

    return createdEvidenceDetails;
  }

  // ___________________________________________________________________________
  /**
   * Reads <xsl:value-of select="$EntityName"/> evidence record.
   *
   * @param key Identifies the evidence record to read.
   *
   * @return The details of the evidence record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
   @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   public Read<xsl:value-of select="$EntityName"/>EvidenceDetails read<xsl:value-of select="$EntityName"/>Evidence(EvidenceCaseKey key)
    throws AppException, InformationalException {
    <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = key.caseIDKey.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseIDKey.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      key.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>
    // <xsl:value-of select="$EntityName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // return object
    Read<xsl:value-of select="$EntityName"/>EvidenceDetails readEvidenceDetails =
      new Read<xsl:value-of select="$EntityName"/>EvidenceDetails();

    // read the <xsl:value-of select="$EntityName"/> record and populate the return details
    readEvidenceDetails =
      evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(key);

    <!-- BEGIN, CR00119218, CD -->
    <!-- Moved code to populate case participant name and ssn to service layer -->
    <!-- END, CR00119218 -->

    return readEvidenceDetails;

  }

  // ___________________________________________________________________________
  /**
   * Adds an incoming <xsl:value-of select="$EntityName"/> evidence record to the case.
   *
   * @param dtls The incoming evidence details.
   *
   * @return The details of the evidence record added to the case.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReturnEvidenceDetails incomingAddToCase<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setInformationalManager();
    <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = dtls.caseIDKey.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = dtls.caseIDKey.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      dtls.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>
    // <xsl:value-of select="$capName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // modify the <xsl:value-of select="$EntityName"/> record and populate the return details
    ReturnEvidenceDetails incomingEvidenceDetails =
      evidenceObj.incomingAddToCase<xsl:value-of select="$EntityName"/>Evidence(dtls);

    final Event evidenceApprovedEvent = new Event();
    evidenceApprovedEvent.eventKey.eventClass = "TASK";
    evidenceApprovedEvent.eventKey.eventType = "CLOSED";
    evidenceApprovedEvent.primaryEventData = dtls.descriptor.relatedID;
    EventService.raiseEvent(evidenceApprovedEvent);

    incomingEvidenceDetails.warnings =
      EvidenceControllerFactory.newInstance().getWarnings();

    return incomingEvidenceDetails;
  }

  // ___________________________________________________________________________
  /**
   * Reads an <xsl:value-of select="$EntityName"/> evidence to be modified by the incoming evidence screens.
   *
   * @param key Wizard Persisted State ID
   *
   * @return The details of the evidence record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public Read<xsl:value-of select="$EntityName"/>EvidenceDetails read<xsl:value-of select="$EntityName"/>EvidenceForIncomingModify(
    curam.guidedchanges.facade.struct.WizardKey key)
    throws AppException, InformationalException {

    final curam.aes.facade.struct.IncomingModifyWizardStoreDetails store = (curam.aes.facade.struct.IncomingModifyWizardStoreDetails) new curam.wizardpersistence.impl.WizardPersistentState()
        .read(key.wizardStateID);

    final curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey evidenceDescriptorKey = new curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey();
    evidenceDescriptorKey.evidenceDescriptorID = store.incomingEvidenceDescriptorID;
    final EvidenceDescriptorDtls incomingEvidenceDescriptorDtls = EvidenceDescriptorFactory
        .newInstance().read(evidenceDescriptorKey);

    evidenceDescriptorKey.evidenceDescriptorID = store.existingEvidenceDescriptorID;
    final EvidenceDescriptorDtls existingEvidenceDescriptorDtls = EvidenceDescriptorFactory
        .newInstance().read(evidenceDescriptorKey);

    Read<xsl:value-of select="$EntityName"/>EvidenceDetails readEvidenceDetails = null;

    if (curam.aes.codetable.AESINCOMINGMODIFYTYPE.UPDATEWITHINCOMING.equals(store.type)) {

      final EvidenceCaseKey caseEvidenceKey = new EvidenceCaseKey();
      caseEvidenceKey.caseIDKey.caseID = incomingEvidenceDescriptorDtls.caseID;
      caseEvidenceKey.evidenceKey.evidenceID = incomingEvidenceDescriptorDtls.relatedID;
      caseEvidenceKey.evidenceKey.evType = incomingEvidenceDescriptorDtls.evidenceType;
      readEvidenceDetails = read<xsl:value-of select="$EntityName"/>Evidence(caseEvidenceKey);

      readEvidenceDetails.descriptor.assign(existingEvidenceDescriptorDtls);
      readEvidenceDetails.descriptor.effectiveFrom = incomingEvidenceUtil.getModifyEvidenceEffectiveDate(store);

    } else if (curam.aes.codetable.AESINCOMINGMODIFYTYPE.RETAINEXISTING.equals(store.type)) {

      final EvidenceCaseKey caseEvidenceKey = new EvidenceCaseKey();
      caseEvidenceKey.caseIDKey.caseID = existingEvidenceDescriptorDtls.caseID;
      caseEvidenceKey.evidenceKey.evidenceID = existingEvidenceDescriptorDtls.relatedID;
      caseEvidenceKey.evidenceKey.evType = existingEvidenceDescriptorDtls.evidenceType;
      readEvidenceDetails = read<xsl:value-of select="$EntityName"/>Evidence(caseEvidenceKey);

      readEvidenceDetails.descriptor.effectiveFrom = incomingEvidenceUtil.getModifyEvidenceEffectiveDate(store);
    }
    return readEvidenceDetails;
  }

    // ___________________________________________________________________________
  /**
   * Performs an Incoming Evidence Modify. Method will perform different actions
   * depends on the data stored in the wizard. i.e Update With Incoming vs
   * Retain Existing, New Version vs Correction.
   *
   * <P>
   * Update with Incoming - New Version will take the incoming evidence and add
   * it into the succession set of the selected existing evidence using an
   * effective date selected by the caseworker.
   * </P>
   *
   * <P>
   * Update with Incoming - Correction will use the values stored on the
   * incoming evidence to correct the existing evidence.
   * </P>
   *
   * @param wizardKey Wizard Persisted State ID
   * @param dtls Evidence dtls object
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReturnEvidenceDetails incomingModify<xsl:value-of select="$EntityName"/>Evidence(
    curam.guidedchanges.facade.struct.WizardKey wizardKey, <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {

    // <xsl:value-of select="$capName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // modify the <xsl:value-of select="$EntityName"/> record and populate the return details
    return evidenceObj.incomingModify<xsl:value-of select="$EntityName"/>Evidence(wizardKey, dtls);

  }


  <!-- BEGIN, CR00219910, CD -->
  // ___________________________________________________________________________
  /**
   * Reads <xsl:value-of select="$EntityName"/> evidence business object.
   *
   * @param key Identifies the evidence business object to read.
   *
   * @return The details of the evidence record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public Read<xsl:value-of select="$EntityName"/>EvidenceDetails read<xsl:value-of select="$EntityName"/>Object(SuccessionID key)
    throws AppException, InformationalException {

    Evidence evidenceObj = EvidenceFactory.newInstance();
    EvidenceCaseKey evidenceCaseKey = evidenceObj.getEvidenceAndCaseFromSuccession(key);
    return read<xsl:value-of select="$EntityName"/>Evidence(evidenceCaseKey);
  }
  <!-- END, CR00219910 -->


  // ___________________________________________________________________________
  /**
   * Modifies a <xsl:value-of select="$EntityName"/> evidence record.
   *
   * @param dtls The modified evidence details.
   *
   * @return The details of the modified evidence record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReturnEvidenceDetails modify<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setInformationalManager();
    <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = dtls.caseIDKey.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = dtls.caseIDKey.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      dtls.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>
    // <xsl:value-of select="$capName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // modify the <xsl:value-of select="$EntityName"/> record and populate the return details
    ReturnEvidenceDetails modifiedEvidenceDetails =
      evidenceObj.modify<xsl:value-of select="$EntityName"/>Evidence(dtls);

    modifiedEvidenceDetails.warnings =
      EvidenceControllerFactory.newInstance().getWarnings();

    return modifiedEvidenceDetails;
  }

  <!-- BEGIN, 191675, JAY -->
  <xsl:variable name="endDateWizardApplicable">
  <xsl:call-template name="Utilities-Entity-EndDateWizardApplicable">
  	<xsl:with-param name="entityElement" select="$entityElement"/>
  </xsl:call-template>
  </xsl:variable>
  <xsl:if test="$endDateWizardApplicable='true'">

  // ___________________________________________________________________________
  /**
   * Creates a <xsl:value-of select="$EntityName"/> evidence record.
   *
   * @param dtls Details of the new evidence record to be created.
   *
   * @return The details of the created record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
   @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   public ReturnAutoEndDateEvidenceDetails createAutoEndDate<xsl:value-of select="$EntityName"/>Evidence(
    <xsl:value-of select="$EntityName"/>EvidenceDetails dtls)
    throws AppException, InformationalException {

    	final ReturnAutoEndDateEvidenceDetails returnDetails = new ReturnAutoEndDateEvidenceDetails();

    	// Set returnEvidenceDetails
    	final ReturnEvidenceDetails createEvidenceDetails = create<xsl:value-of select="$EntityName"/>Evidence(dtls);

    	returnDetails.evidenceID = createEvidenceDetails.evidenceKey.evidenceID;

    	// Set startDate of the evidence;
    	final Date startDate = dtls.dtls.<xsl:value-of select="$entityElement/BusinessDates/@startDate"/>;
    	returnDetails.startDateStr = Locale.getFormattedDate(startDate, Locale.Date_ymd);

    	<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
    	final CaseParticipantRole caseParticipantRole = CaseParticipantRoleFactory.newInstance();
      	final CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();

      	caseParticipantRoleKey.caseParticipantRoleID = dtls.dtls.<xsl:value-of select="$entityElement/@relateEvidenceParticipantID"/>;

      	// Set participantID
    	returnDetails.participantID = caseParticipantRole.read(caseParticipantRoleKey).participantRoleID;
    	</xsl:if>

    	return returnDetails;

    }
   // ___________________________________________________________________________
  /**
   * End date a <xsl:value-of select="$EntityName"/> evidence record.
   *
   * @param dtls The modified evidence details.
   *
   * @return The details of the modified evidence record.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public AutoEndDateReturnEvidenceDetails autoEndDate<xsl:value-of select="$EntityName"/>Evidence(
    AutoEndDateEvidenceDetails dtls)
    throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setFacadeScopeObject(CuramConst.gkMeoTransactionKey,
        MultiFailOperation.ValidationType.STATICEVIDENCE);

     // Validate evidence auto end date details
    autoEndDateEvidenceOperations
      .validateAutoEndDateEvidenceDetails(dtls);

     if (autoEndDateEvidenceHook != null) {
        autoEndDateEvidenceHook.preAutoEndDateEvidence(dtls);
      }

    //Return struct
    AutoEndDateReturnEvidenceDetails returnEvidenceDetails = new AutoEndDateReturnEvidenceDetails();

     CaseIDKey caseIDKey = new CaseIDKey();
     caseIDKey.caseID = dtls.caseID;

     EvidenceKey evidenceKey = new EvidenceKey();

      EvidenceCaseKey eviCaseKey = new EvidenceCaseKey();
      eviCaseKey.caseIDKey = caseIDKey;
      eviCaseKey.evidenceKey = evidenceKey;

       <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = dtls.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = dtls.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>
    // <xsl:value-of select="$capName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

        // EvidenceDescriptor object
    	final EvidenceDescriptor evidenceDescriptorObj =
      		EvidenceDescriptorFactory.newInstance();

      final StringList evidenceList =
      StringUtil.tabText2StringList(dtls.evidenceList.multiSelectStr);

      for(String evidenceDtls : evidenceList) {

       	final StringList evidenceDescriptorIDAndVersionNoList =
          StringUtil.delimitedText2StringList(evidenceDtls,
            CuramConst.gkPipeDelimiterChar);

       	final EvidenceDescriptorKey evidenceDescriptorKey = new EvidenceDescriptorKey();
    	evidenceDescriptorKey.evidenceDescriptorID =
      		Long.parseLong(evidenceDescriptorIDAndVersionNoList.item(0));

		// Get evidence descriptor details for the evidence
    	final EvidenceDescriptorDtls evidenceDescriptorDtls =
      		evidenceDescriptorObj.read(evidenceDescriptorKey);

      	// Set evidenceID on EvidenceCaseKey
    	eviCaseKey.evidenceKey.evidenceID = evidenceDescriptorDtls.relatedID;

    	  // read the <xsl:value-of select="$EntityName"/> record and populate the return details
    	Read<xsl:value-of select="$EntityName"/>EvidenceDetails readEvidenceDetails =
      		evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(eviCaseKey);

      	<xsl:value-of select="$EntityName"/>EvidenceDetails modifyEvidenceDetails = new <xsl:value-of select="$EntityName"/>EvidenceDetails();

      	   // assign the modify values
   		modifyEvidenceDetails.caseIDKey = caseIDKey;
      	modifyEvidenceDetails.assign(readEvidenceDetails);

      // Set end date on the evidence
      modifyEvidenceDetails.dtls.<xsl:value-of select="$entityElement/BusinessDates/@endDate"/> = dtls.endDate;
      modifyEvidenceDetails.descriptor.changeReason = dtls.changeReason;

      	 // modify the <xsl:value-of select="$EntityName"/> record and populate the return details
    	ReturnEvidenceDetails modifiedEvidenceDetails =
      		evidenceObj.modify<xsl:value-of select="$EntityName"/>Evidence(modifyEvidenceDetails);

      	//	Add evidence modified to return struct
      	returnEvidenceDetails.evidenceKeyList.addRef(modifiedEvidenceDetails.evidenceKey);
      	returnEvidenceDetails.warnings.dtls.addAll(modifiedEvidenceDetails.warnings.dtls);
      }

       if (autoEndDateEvidenceHook != null) {
        autoEndDateEvidenceHook.postAutoEndDateEvidence(dtls,
          returnEvidenceDetails);
      }

	MultiFailOperation.checkForInformationals();
	TransactionInfo.setFacadeScopeObject(CuramConst.gkMeoTransactionKey,
        null);

    return returnEvidenceDetails;
  }
  </xsl:if>
  <!-- END, 191675, JAY -->
  <xsl:for-each select="FacadeLayer/method">
  // ___________________________________________________________________________
  /**
   * // TODO Add <xsl:value-of select="@name"/> method comments
   *
   * @param details // TODO add comments
   *
   * @return //TODO add comments
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public <xsl:value-of select="@returnType"/> <xsl:text> </xsl:text> <xsl:value-of select="@name"/>
    (<xsl:value-of select="@argumentType"/> details)
    throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setInformationalManager();

    // <xsl:value-of select="EntityName"/>  evidence manipulation object
    <xsl:value-of select="@name"/> evidenceObj =
      <xsl:value-of select="@name"/>Factory.newInstance();

    // Return object
    <xsl:value-of select="@returnType"/> returnEvidenceDetails =
      new <xsl:value-of select="@returnType"/>();

    returnEvidenceDetails.<xsl:value-of select="@returnAggregation"/> =
      evidenceObj.<xsl:value-of select="@slMethodName"/>(
        details.<xsl:value-of select="@argumentAggregation"/>);

    return returnEvidenceDetails;
  }
  </xsl:for-each>

  <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">
  //  ___________________________________________________________________________
  /**
   *  Reads a list of not associated <xsl:value-of select="$capName"/> records.
   *
   * @param key contains case ID for which <xsl:value-of select="$capName"/> records
   * must be read.
   *
   * @return List of not associated <xsl:value-of select="$capName"/> evidence records read.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ListEvidenceDetails list<xsl:value-of select="$EntityName"/>EvidenceWithoutAssociations
    (EvidenceListKey key) throws AppException, InformationalException {

    // <xsl:value-of select="$capName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    // GeneralUtility evidence manipulation object
    curam.core.sl.infrastructure.intf.GeneralUtility evidenceUtilityObj =
      curam.core.sl.infrastructure.fact.GeneralUtilityFactory.newInstance();

    // return object
    ListEvidenceDetails readEvidenceListDetailsList =
      new ListEvidenceDetails();

    <xsl:if test="@caseType='Integrated'">
    // Get the  Integrated CaseID
    CachedCaseHeader caseHeaderObj =
      CachedCaseHeaderFactory.newInstance();

    CaseKey caseKey = new CaseKey();
    caseKey.caseID = key.caseIDKey.caseID;

    CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseIDKey.caseID;

    if(caseHeaderObj.read(caseHeaderKey).integratedCaseID!=0) {
      key.caseIDKey.caseID =
        caseHeaderObj.readIntegratedCaseIDByCaseID(caseKey).integratedCaseID;
    }
    </xsl:if>

    // list the <xsl:value-of select="$capName"/> records and populate the return details
    readEvidenceListDetailsList =
      evidenceObj.list<xsl:value-of select="$EntityName"/>EvidenceWithoutAssociations(key);

    // Read in the Evidence Details
    readEvidenceListDetailsList.evListDtls =
      evidenceUtilityObj.updateListsWithNameAndSSN(
        readEvidenceListDetailsList.evListDtls);

    // Create the <xsl:value-of select="$EntityName"/> context description key
    CaseIDKey contextDescriptionKey = new CaseIDKey();

    contextDescriptionKey.caseID = key.caseIDKey.caseID;

    // Retrieve the context description
    readEvidenceListDetailsList.evidenceContextDescriptionDetails =
      getContextDescription(contextDescriptionKey);

    return readEvidenceListDetailsList;
  }

  </xsl:if>
  <!-- Check if necessary to create facade operation to retrieve related entity attributes -->
  <xsl:if test="Relationships/@exposeOperation='Yes'">
  //  ___________________________________________________________________________
  /**
   *  Returns the related entity attributes for <xsl:value-of select="$capName"/> records.
   *
   * @param key contains case ID for which <xsl:value-of select="$capName"/> records
   * must be read, and the parent evidence ID for the related entity
   *
   * @return List of not associated <xsl:value-of select="$capName"/> evidence records read.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
   <!-- if this is a non standard read on the entity, the struct returned is modelled on the entity layer
        otherwise it is a generated struct on the service layer -->
  <xsl:choose>
    <xsl:when test="Relationships/@preAssociation='Yes' and Relationships/@mulitplePreAssociationID='Yes'">
  public <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EntityName"/>RelatedEntityAttributesDetails get<xsl:value-of select="$EntityName"/>RelatedEntityAttributes(curam.core.sl.struct.MultiplePreAssocCaseEvKey key)
    throws AppException, InformationalException{
    </xsl:when>
    <xsl:when test="Relationships/@preAssociation='Yes'">
  public <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EntityName"/>RelatedEntityAttributesDetails get<xsl:value-of select="$EntityName"/>RelatedEntityAttributes(curam.core.sl.struct.PreAssocCaseEvKey key)
    throws AppException, InformationalException{
    </xsl:when>
    <xsl:otherwise>
  public <xsl:value-of select="$javaEvidenceCodePath"/>.entity.struct.<xsl:value-of select="$EntityName"/>RelatedEntityAttributesDetails get<xsl:value-of select="$EntityName"/>RelatedEntityAttributes(EvidenceCaseKey key)
    throws AppException, InformationalException{
    </xsl:otherwise>
  </xsl:choose>
    //Instantiate <xsl:value-of select="$EntityName"/>RelatedEntityAttributes object

    <xsl:value-of select="$javaEvidenceCodePath"/>.relatedattribute.intf.<xsl:value-of select="$EntityName"/>RelatedEntityAttributes getRelatedEntityAttributesObj =
      <xsl:value-of select="$javaEvidenceCodePath"/>.relatedattribute.fact.<xsl:value-of select="$EntityName"/>RelatedEntityAttributesFactory.newInstance();

    return getRelatedEntityAttributesObj.getRelatedEntityAttributes(key);

  }
  </xsl:if>

  <xsl:if test="Relationships/@association='Yes' and Relationships/Association[@to!='']">
  <xsl:variable name="assocStartDateField"><xsl:value-of select="//EvidenceEntities/EvidenceEntity[@name=$capName]/UserInterfaceLayer/Cluster/Field[@metatype=$metatypeAssocStartDate]/@columnName"/></xsl:variable>
  <xsl:variable name="assocEndDateField"><xsl:value-of select="//EvidenceEntities/EvidenceEntity[@name=$capName]/UserInterfaceLayer/Cluster/Field[@metatype=$metatypeAssocEndDate]/@columnName"/></xsl:variable>
  //___________________________________________________________________________
  /**
   * Creates an association. The association is stored on the EvidenceRelationship table and
   * the association dates are stored on the Entity table.
   *
   * @param dtls Struct containing the keys of the associated records and the association
   *                  business dates
   *
   * @return ReturnEvidenceDetails Struct containing the key of the entity and any
   *                                           warnings.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public ReturnEvidenceDetails create<xsl:value-of select="$capName"/>Association
    (curam.core.sl.struct.AssociationDetails details) throws AppException,
    InformationalException{
    <xsl:if test="$assocStartDateField!='' or $assocEndDateField!=''">
    // read key
    EvidenceCaseKey key = new EvidenceCaseKey();

    // Adoption evidence manipulation object
    <xsl:value-of select="$capName"/> evidenceObj = <xsl:value-of select="$capName"/>Factory.newInstance();

    // store the business dates on the entity, we update the existing record
    Read<xsl:value-of select="$capName"/>EvidenceDetails readDetails = new Read<xsl:value-of select="$capName"/>EvidenceDetails();
    <xsl:value-of select="$capName"/>EvidenceDetails modifyDetails = new <xsl:value-of select="$capName"/>EvidenceDetails();

    // set the key to read the entity record
    key.evidenceKey.evidenceID = details.evidenceID;
    key.evidenceKey.evType = details.evidenceType;

    // read the record
    readDetails = evidenceObj.read<xsl:value-of select="$capName"/>Evidence(key);

    // assign the modify values
    modifyDetails.dtls = readDetails.dtls;
    modifyDetails.descriptor = readDetails.descriptor;

    <xsl:if test="$assocStartDateField!=''">
    // update the start date
    modifyDetails.dtls.<xsl:value-of select="$assocStartDateField"/> = details.associationStartDate;
    </xsl:if>
    <xsl:if test="$assocEndDateField!=''">
    // update the end date
    modifyDetails.dtls.<xsl:value-of select="$assocEndDateField"/> = details.associationEndDate;
    </xsl:if>
    // perform the modify
    evidenceObj.modify<xsl:value-of select="$capName"/>Evidence(modifyDetails);
    </xsl:if>

    // Use the EvidenceAPI to create the association and return
    return EvidenceControllerFactory.newInstance().createAssociation(details);
  }
  </xsl:if>

  <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']">

    <xsl:variable name="rPcolumnName" select="@columnName"/>

    <xsl:variable name="shortName">
      <xsl:call-template name="capitalize">
        <xsl:with-param name="convertThis" select="@name"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:for-each select="../../UserInterfaceLayer/Cluster/Field[@columnName=$rPcolumnName and (not(@mandatory) or @mandatory!='Yes')]">
  //___________________________________________________________________________
  /**
   * This function provides a means for a client modify screen to determine
   * whether an optional participant on the record has already been created.
   * It returns two booleans which decide which of two conditional clusters
   * should be displayed - one contains a link to a participant, the other
   * contains the required data entry fields to create the participant.
   *
   * @param key key containing evidence ID and caseID
   * @return OptionalParticipantDetails struct two booleans used for conditional
   * clusters on the client.
   *
   * @throws AppException
   *           Generic AppException Signature
   * @throws InformationalException
   *           Generic InformationalException Signature
   */
  @curam.util.type.AccessLevel(curam.util.type.AccessLevelType.INTERNAL)
  public OptionalParticipantDetails get<xsl:value-of select="$EntityName"/><xsl:value-of select="$shortName"/>OptionalParticipantDetails(
    EvidenceCaseKey key) throws AppException, InformationalException {

    // set the informational manager for the transaction
    TransactionInfo.setInformationalManager();

    // <xsl:value-of select="$EntityName"/> evidence manipulation object
    <xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    return evidenceObj.get<xsl:value-of select="$EntityName"/><xsl:value-of select="$shortName"/>OptionalParticipantDetails(key);
  }

    </xsl:for-each>
  </xsl:for-each>


      </redirect:write>
    </xsl:for-each>

      <redirect:write select="$filename">
}
      </redirect:write>
      <redirect:close select="$filename"/>

  </xsl:template>

  <!-- GetSortedImports and GetUnsortedImports makes -->
  <!-- organising the imports a lot easier.          -->
  <xsl:template name="GetSortedImports">
    <xsl:variable name="file"><xsl:value-of select="$productEvidenceHome"/>/gen.tmp/imports/facade/Facade.imports</xsl:variable>
    <xsl:variable name="unsorted" select="document($file)/imports"/>
    <xsl:for-each select="$unsorted/import"><xsl:sort select="@value"/>
  <xsl:value-of select="@value"/><xsl:text>&#10;</xsl:text>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
