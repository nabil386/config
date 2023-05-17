<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2006,2021. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright 2006-2008 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:output method="text"/>
  <xsl:key name="Attribute" match="Attributes/Attribute" use="@name"/>

  <!-- Global Variables -->
  <xsl:include href="EvidenceCommon.xslt" />

  <xsl:param name="productEvidenceHome"/>
  <xsl:param name="date"/>

  <xsl:template match="EvidenceEntity[EntityLayer]">

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="ucName"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>
    <xsl:variable name="lcName"><xsl:value-of select="translate($capName, $ucletters, $lcletters)"/></xsl:variable>
    <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>
    <xsl:variable name="generalError">ENT<xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>
    <xsl:variable name="generalErrorMessage"><xsl:value-of select="$ucPrefix"/>GENERALERROR</xsl:variable>

    <xsl:variable name="startDate">
      <xsl:choose>
        <xsl:when test="Attributes/Attribute[@metatype=$metatypeStartDate]/@name!=''">
          <xsl:value-of select="Attributes/Attribute[@metatype=$metatypeStartDate]/@name"/>
        </xsl:when>
        <xsl:when test="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeStartDate]/@name!=''">
          <xsl:value-of select="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeStartDate]/@name"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="endDate">
      <xsl:choose>
        <xsl:when test="Attributes/Attribute[@metatype=$metatypeEndDate]/@name!=''">
          <xsl:value-of select="Attributes/Attribute[@metatype=$metatypeEndDate]/@name"/>
        </xsl:when>
        <xsl:when test="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeEndDate]/@name!=''">
          <xsl:value-of select="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeEndDate]/@name"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="evidenceDescriptorReceivedDate">
      <xsl:choose>
        <xsl:when test="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeStartDate]/@evidenceDescriptorReceivedDate='Yes'">
          <xsl:value-of select="EntityLayer/ReadDetails/Attribute[@metatype=$metatypeStartDate]/@evidenceDescriptorReceivedDate"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <!-- BEGIN, CR00100405, CD -->
    <!-- BEGIN, CR00120824, CD -->
    <xsl:variable name="returnType"><xsl:value-of select="$EntityName"/>Dtls</xsl:variable>
    <!-- END, CR00120824 -->

    <xsl:variable name="evidenceTypeCode"><xsl:value-of select="$ucName"/></xsl:variable>

    <xsl:variable name="argumentType"><xsl:value-of select="$EntityName"/>Key</xsl:variable>
    <!-- END, CR00100405 -->

    <xsl:variable name="cachingEnabled"><xsl:value-of select="@cachingEnabled"/></xsl:variable>
    <xsl:variable name="caseType"><xsl:value-of select="@caseType"/></xsl:variable>

    <!-- BEGIN, CR00222637, POB -->
    <!--
      variables containing java code for accessing the business start and end dates off the entities Dtls struct,
      or null if none were specified
    -->
    <xsl:variable name="businessStartDate">
      <xsl:choose>
        <xsl:when test="BusinessDates/@startDate!=''">((<xsl:value-of select="$returnType"/>)readEvidence(evKey)).<xsl:value-of select="BusinessDates/@startDate"/></xsl:when>
        <xsl:otherwise>null</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="businessEndDate">
      <xsl:choose>
        <xsl:when test="BusinessDates/@endDate!=''">((<xsl:value-of select="$returnType"/>)readEvidence(evKey)).<xsl:value-of select="BusinessDates/@endDate"/></xsl:when>
        <xsl:otherwise>null</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- END, CR00222637 -->

    <redirect:write select="concat($EntityName, '.java')">
<xsl:call-template name="printJavaCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

package <xsl:value-of select="$javaEvidenceCodePath"/>.entity.impl;

<xsl:call-template name="GetSortedImports">
  <xsl:with-param name="entity" select="$EntityName"/>
</xsl:call-template>

@SuppressWarnings("all")
public abstract class <xsl:value-of select="$EntityName"/> extends <xsl:value-of select="$javaEvidenceCodePath"/>.entity.base.<xsl:value-of select="$EntityName"/> implements curam.core.sl.infrastructure.impl.EvidenceInterface {

<!-- BEGIN, 25/02/2008, CH -->
<xsl:if test="count(EntityLayer/CachedOperation)>0">
  /**
   * Caches <xsl:value-of select="$capName"/>  records.
   */

  protected class <xsl:value-of select="$capName"/>Cache {
    public <xsl:value-of select="$capName"/>Cache() {
      map = new java.util.HashMap();
      transactionID = 0;

    }

    java.util.HashMap map;
    int transactionID;
  }

  protected static ThreadLocal cachedReadTL = new ThreadLocal();

  // ___________________________________________________________________________
  /**
   * Read <xsl:value-of select="$capName"/> details
   * This is a cached read - intended to improve performance on read
   * operations.
   *
   * @param key entity key containing the ID
   *
   * @return details from cache (or from database if cache is empty)
   *
   * @throws AppException, InformationalException
   */

  public <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/><xsl:text>&#xA;</xsl:text><xsl:value-of select="EntityLayer/CachedOperation/@operationName"/>(<xsl:value-of select="$capName"/>Key key)
    throws AppException, InformationalException {

    <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/> dtls = new <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/>();

    <xsl:value-of select="$capName"/>Cache cache = (<xsl:value-of select="$capName"/>Cache) cachedReadTL.get();

    TransactionInfo.TransactionType transactionType =
      TransactionInfo.getTransactionType();

    if (cache != null) {

      // if this is a batch transaction
      if ((transactionType.equals(TransactionInfo.TransactionType.kBatch))
        || ((transactionType.equals(TransactionInfo.TransactionType.kDeferred))
          || (transactionType.equals(TransactionInfo.TransactionType.kOnline)
            &amp;&amp; curam.util.resources.Configuration.getBooleanProperty(
              curam.core.impl.EnvVars.ENV_ONLINE_CACHING_ENABLED))
                &amp;&amp; TransactionInfo.getIdentifierForThisThread()
                  == cache.transactionID)) {

        // try to find the data in the cache
        dtls = (<xsl:value-of select="EntityLayer/CachedOperation/@returnType"/>) cache.map.get(new Long(key.evidenceID));

        if (dtls != null) {

          <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/> returnedDtls =
            new <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/> ();

          returnedDtls.assign(dtls);

          return returnedDtls;
        }
      }
    }

    // read from the database
    dtls  = <xsl:value-of select="EntityLayer/CachedOperation/@databaseRead"/>(key);

    // if this was a cache miss, refresh the cache
    // if this is a batch or deferred process transaction

    if ((transactionType.equals(TransactionInfo.TransactionType.kBatch))
      || (transactionType.equals(TransactionInfo.TransactionType.kDeferred))
      || (transactionType.equals(TransactionInfo.TransactionType.kOnline)
      &amp;&amp; curam.util.resources.Configuration.getBooleanProperty(curam.core.impl.EnvVars.ENV_ONLINE_CACHING_ENABLED))) {

      // create the cache if it hasn't been created yet
      if (cache == null) {

        cache = new <xsl:value-of select="$capName"/>Cache();

        cachedReadTL.set(cache);
      }

      // clear the cache if the transaction is different
      if (cache.transactionID != TransactionInfo.getIdentifierForThisThread()) {

        cache.map.clear();

        cache.transactionID = TransactionInfo.getIdentifierForThisThread();
      }

      // add to the cache as it's a cache miss
      <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/> cachedDtls =
        new <xsl:value-of select="EntityLayer/CachedOperation/@returnType"/> ();

      cachedDtls.assign(dtls);

      cache.map.put(new Long(key.evidenceID), cachedDtls);
    }

    return dtls;
  }
  <!-- END, 25/02/2008, CH -->

  // ___________________________________________________________________________
  /**
   * Clear cached data in cachedReadTL
   */
  protected void clearCaches() {

    <xsl:value-of select="$capName"/>Cache cache = (<xsl:value-of select="$capName"/>Cache) cachedReadTL.get();

    if (cache != null) {

      cache.map.clear();
    } else {

      cache = new <xsl:value-of select="$capName"/>Cache();

      cachedReadTL.set(cache);
    }

    cache.transactionID = TransactionInfo.getIdentifierForThisThread();
  }

</xsl:if>
  //___________________________________________________________________________
  /**
   * Calculate the attribution dates for the case
   *
   * @param caseKey provides the case identifier
   * @param evKey holds the evidenceID and evidenceType
   *
   * @return the calculated attribution dates for this case
   */
  public AttributedDateDetails calcAttributionDatesForCase(CaseKey caseKey,
    EIEvidenceKey evKey) throws AppException, InformationalException {

    return
      <xsl:value-of select="$EntityName"/>HookFactory.newInstance()
      .calcAttributionDatesForCase(caseKey, evKey);

  }


  //___________________________________________________________________________
  /**
   * Get evidence details for the list display
   *
   * @param key Evidence key containing the evidenceID and evidenceType
   *
   * @return Evidence details to be displayed on the list page
   */
  public EIFieldsForListDisplayDtls getDetailsForListDisplay(EIEvidenceKey key)
    throws AppException, InformationalException {

    return
      <xsl:value-of select="$EntityName"/>HookFactory.newInstance()
      .getDetailsForListDisplay(key);
  }


  //___________________________________________________________________________
  /**
   * Insert <xsl:value-of select="$EntityName"/> evidence
   *
   * @param dtls Details to create a <xsl:value-of select="$EntityName"/> evidence record
   * @param parentKey The evidence ID and evidence type
   *
   * @return The evidenceID and evidenceType
   */
  public EIEvidenceKey insertEvidence(Object dtls, EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    // Return object
    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

    // Insert the record
    insert((<xsl:value-of select="$returnType"/>) dtls);

    eiEvidenceKey.evidenceID = ((<xsl:value-of select="$returnType"/>) dtls).evidenceID;
    eiEvidenceKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    // Return details
    return eiEvidenceKey;
  }


  //___________________________________________________________________________
  /**
   * Insert <xsl:value-of select="$EntityName"/> evidence on modification. This is called when
   * an Active evidence record is modified so a new record must be inserted.
   *
   * @param dtls The modified evidence details
   * @param origKey The original evidence key details
   * @param parentKey The evidence key for the parent evidence
   *
   * @return The new evidenceID and evidenceType
   */
  public EIEvidenceKey insertEvidenceOnModify(Object dtls,
    EIEvidenceKey origKey, EIEvidenceKey parentKey)
    throws AppException, InformationalException {

    // Return object
    EIEvidenceKey eiEvidenceKey = new EIEvidenceKey();

    // Insert the record
    insert((<xsl:value-of select="$returnType"/>) dtls);

    eiEvidenceKey.evidenceID =
      ((<xsl:value-of select="$returnType"/>) dtls).evidenceID;

    eiEvidenceKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    // Return details
    return eiEvidenceKey;

  }


  //___________________________________________________________________________
  /**
   * Modify <xsl:value-of select="$EntityName"/> evidence
   *
   * @param key Evidence key for the evidence to be modified
   * @param dtls The modified evidence details
   */
  public void modifyEvidence(EIEvidenceKey key, Object dtls)
    throws AppException, InformationalException {

    // <xsl:value-of select="$argumentType"/> entity key
    <xsl:value-of select="$argumentType"/> entityKey = new <xsl:value-of select="$argumentType"/>();

    // Set entity key for modify
    entityKey.evidenceID = key.evidenceID;

    // Modify details
    modify(entityKey, (<xsl:value-of select="$returnType"/>) dtls);

  }


  //___________________________________________________________________________
  /**
   * Read all child record identifiers.
   *
   * @param key Evidence key for the parent evidence record
   *
   * @return A list of child record evidence keys for the parent
   */
  public EIEvidenceKeyList readAllByParentID(EIEvidenceKey key)
    throws AppException, InformationalException {
    <xsl:choose>
      <xsl:when test="count(Relationships/PreAssociation) &gt; 0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/Parent)&gt;0 or count(Relationships/Association[@from!=''])>0">
    // Return object
    EIEvidenceKeyList eiEvidenceKeyList = new EIEvidenceKeyList();

    // Create the link entity object
    EvidenceRelationship evidenceRelationshipObj =
      EvidenceRelationshipFactory.newInstance();

    // parent entity key
    ParentKey parentKey = new ParentKey();
    parentKey.parentID = key.evidenceID;
    parentKey.parentType = key.evidenceType;

    // Reads all relationship details for the specified parent
    ChildKeyList childKeyList =
      evidenceRelationshipObj.searchByParent(parentKey);

    // Iterate through the link details list
    for (int i = 0; i &lt; childKeyList.dtls.size(); i++) {

      if (childKeyList.dtls.item(i).childType.equals(
        CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>)) {

        EIEvidenceKey listEvidenceKey = new EIEvidenceKey();

        listEvidenceKey.evidenceID =
          childKeyList.dtls.item(i).childID;
        listEvidenceKey.evidenceType =
          childKeyList.dtls.item(i).childType;

        eiEvidenceKeyList.dtls.addRef(listEvidenceKey);
      }
    }

    return eiEvidenceKeyList;
      </xsl:when>
      <xsl:otherwise>
    // <xsl:value-of select="$EntityName"/> has no related child evidence
    return new EIEvidenceKeyList();
      </xsl:otherwise>
    </xsl:choose>
  }


  //___________________________________________________________________________
  /**
   * Read <xsl:value-of select="$EntityName"/> evidence
   *
   * @param key Details to retrieve an evidence record
   *
   * @return <xsl:value-of select="$EntityName"/> evidence details
   */
  public Object readEvidence(EIEvidenceKey key)
    throws AppException, InformationalException {

    // <xsl:value-of select="$EntityName"/> entity key
    <xsl:value-of select="$argumentType"/> readEvidenceKey =
      new <xsl:value-of select="$argumentType"/>();

    // Set key to read <xsl:value-of select="$EntityName"/> Details
    readEvidenceKey.evidenceID = key.evidenceID;

    // Read <xsl:value-of select="$EntityName"/> Evidence
    <xsl:value-of select="$returnType"/> readEvidenceDetails =
      read(readEvidenceKey);

    return readEvidenceDetails;

  }


  //___________________________________________________________________________
  /**
   * Selects all the records for validations
   *
   * @param evKey Contains an evidenceID / evidenceType pairing
   *
   * @return List of evidenceID / evidenceType pairings
   */
  public EIEvidenceKeyList selectForValidation(EIEvidenceKey evKey)
    throws AppException, InformationalException {

    EIEvidenceKeyList eiEvidenceKeyList = new EIEvidenceKeyList();
    <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/Child)&gt;0 or count(Relationships/Association)&gt;0">
    // EvidenceRelationship Link entity
    EvidenceRelationship evidenceRelationshipObj =
      EvidenceRelationshipFactory.newInstance();
   <xsl:if test="count(Relationships/Child)&gt;0 or count(Relationships/Association[@to!=''])>0">
    // if parent evidence
    ParentKey parentKey = new ParentKey();
    parentKey.parentID = evKey.evidenceID;
    parentKey.parentType = evKey.evidenceType;

    ChildKeyList childKeyList =
      evidenceRelationshipObj.searchByParent(parentKey);

    for (int i = 0; i &lt; childKeyList.dtls.size(); i++) {

      EIEvidenceKey listEvidenceKey = new EIEvidenceKey();

      listEvidenceKey.evidenceID =
       childKeyList.dtls.item(i).childID;
      listEvidenceKey.evidenceType =
       childKeyList.dtls.item(i).childType;

      eiEvidenceKeyList.dtls.addRef(listEvidenceKey);
    }
      </xsl:if>
      <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/Association[@from!=''])>0">
    // if child evidence
    ChildKey childKey = new ChildKey();
    childKey.childID = evKey.evidenceID;
    childKey.childType = evKey.evidenceType;

    ParentKeyList parentKeyList =
      evidenceRelationshipObj.searchByChild(childKey);

    for (int i = 0; i &lt; parentKeyList.dtls.size(); i++) {

      EIEvidenceKey listEvidenceKey = new EIEvidenceKey();

      listEvidenceKey.evidenceID =
        parentKeyList.dtls.item(i).parentID;
      listEvidenceKey.evidenceType =
        parentKeyList.dtls.item(i).parentType;

      eiEvidenceKeyList.dtls.addRef(listEvidenceKey);
    }
      </xsl:if>
    </xsl:if>
    eiEvidenceKeyList.dtls.add(0, evKey);

    return eiEvidenceKeyList;

  }

<!-- TODO determine from Curam.xml whether these are required - how do we know what the implementation should be? -->
  //___________________________________________________________________________
  /**
   * Performs processing prior to the insertion of the adoption record.
   * Calls the validation method to check that the details being inserted are
   * correct.
   *
   * @param details The details to be inserted
   *
   */
  protected void preinsert(<xsl:value-of select="$returnType"/> details) throws AppException, InformationalException {

    if (EvidenceControllerFactory.newInstance().areAllStaticEntityValidationsSuppressed()) {
      Validate<xsl:value-of select="$EntityName"/> validate<xsl:value-of select="$EntityName"/>
        = Validate<xsl:value-of select="$EntityName"/>Factory.newInstance();
      validate<xsl:value-of select="$EntityName"/>.preInsertValidate(details);
    }

  }


  //___________________________________________________________________________
  /**
   * Sets up the <xsl:value-of select="$EntityName"/> details before they are modified.
   *
   * @param key The <xsl:value-of select="$capName"/> ID
   * @param details The details to be modified.
   *
   */
  protected void premodify(<xsl:value-of select="$argumentType"/> key, <xsl:value-of select="$returnType"/> details) throws AppException, InformationalException {

    if (EvidenceControllerFactory.newInstance().areAllStaticEntityValidationsSuppressed()) {
      Validate<xsl:value-of select="$EntityName"/> validate<xsl:value-of select="$EntityName"/>
        = Validate<xsl:value-of select="$EntityName"/>Factory.newInstance();
      validate<xsl:value-of select="$EntityName"/>.preModifyValidate(details);
    }

  }


  //___________________________________________________________________________
  /**
   * Validates evidence details
   *
   * @param evKey Evidence key
   * @param evKeyList Evidence key list
   * @param mode Validate mode (insert, delete, applyChanges, modify)
   *
   */
  public void validate(EIEvidenceKey evKey, EIEvidenceKeyList evKeyList,
    ValidateMode mode) throws AppException, InformationalException {

    // Get the informational manager
    InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Get the Details Structure for Validations
    <xsl:value-of select="$EntityName"/>EvidenceDetails evidenceDetails = new <xsl:value-of select="$EntityName"/>EvidenceDetails();

    //
    // Read the evidence descriptor
    //

    RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
      new RelatedIDAndEvidenceTypeKey();
    relatedIDAndEvidenceTypeKey.relatedID = evKey.evidenceID;
    relatedIDAndEvidenceTypeKey.evidenceType = evKey.evidenceType;

    EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();

    EvidenceDescriptorDtls evidenceDescriptorDtls =
      evidenceDescriptorObj.readByRelatedIDAndType(relatedIDAndEvidenceTypeKey);

    //
    // Read the evidence details
    //

    <xsl:value-of select="$argumentType"/> evidenceKey = new <xsl:value-of select="$argumentType"/>();

    evidenceKey.evidenceID = evKey.evidenceID;
    <xsl:value-of select="$returnType"/> evidenceDtls = read(evidenceKey);

    CaseIDKey caseIDKey = new CaseIDKey();
    caseIDKey.caseID = evidenceDescriptorDtls.caseID;

    EvidenceDescriptorDetails evidenceDescriptorDetails =
      new EvidenceDescriptorDetails();
    evidenceDescriptorDetails.assign(evidenceDescriptorDtls);

    evidenceDetails.dtls = evidenceDtls;
    evidenceDetails.descriptor = evidenceDescriptorDetails;
    evidenceDetails.caseIDKey = caseIDKey;

    <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
    fillInRelatedDetails(evidenceDetails);
    </xsl:if>

    <xsl:if test="count(Relationships/Child)&gt;0 or (Relationships/@association='Yes' and count(Relationships/Association[@to!=''])>0) or (Relationships/@reciprocal='Yes' and count(Relationships/Child)>0)">
    if (evidenceDescriptorDtls.statusCode.equals(
        EVIDENCEDESCRIPTORSTATUS.CANCELED) ||
        (mode.validateChanges &amp;&amp;
         evidenceDescriptorDtls.pendingRemovalInd)) {

      // Key used for reading <xsl:value-of select="$capName"/> record from the database
      ParentKey parentKey = new ParentKey();
      parentKey.parentID = evKey.evidenceID;
      parentKey.parentType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

      EvidenceRelationship evidenceRelationshipObj =
        EvidenceRelationshipFactory.newInstance();

      ChildKeyList childKeyList =
        evidenceRelationshipObj.searchByParent(parentKey);

      EvidenceController evidenceControllerObj =
        EvidenceControllerFactory.newInstance();

      // Create a list of evidence keys
      EIEvidenceKeyList eiEvidenceKeyList = new EIEvidenceKeyList();

      <!-- BEGIN, CR00243480, POB -->
      for (int i = 0; i &lt; childKeyList.dtls.size(); i++) {

        boolean childToAdd = true;

        ParentKeyList parentKeyList =
          EvidenceRelationshipFactory.newInstance().searchByChild(childKeyList.dtls.item(i));

        for (int j = 0; j &lt; parentKeyList.dtls.size(); j++) {

          ParentKey otherKey = parentKeyList.dtls.item(j);

          if (!otherKey.parentType.equals(evKey.evidenceType)) {
            childToAdd = true;
            break;
          }

          // if it is the same type, not the current record then check to
          // see if its the same succession
          if (otherKey.parentID != evKey.evidenceID
              &amp;&amp; otherKey.parentType.equals(
                  evKey.evidenceType)) {

            RelatedIDAndEvidenceTypeKey otherIDAndEvidenceTypeKey =
              new RelatedIDAndEvidenceTypeKey();
            otherIDAndEvidenceTypeKey.evidenceType = otherKey.parentType;
            otherIDAndEvidenceTypeKey.relatedID = otherKey.parentID;

            EvidenceDescriptorDtls otherDescriptorDtls =
              EvidenceDescriptorFactory.newInstance().readByRelatedIDAndType(
                  otherIDAndEvidenceTypeKey);

            if (otherDescriptorDtls.successionID ==
              evidenceDescriptorDtls.successionID) {
              childToAdd = false;
            }
          }
        }
        if (childToAdd) {
          EIEvidenceKey listEvidenceKey = new EIEvidenceKey();

          listEvidenceKey.evidenceID =
            childKeyList.dtls.item(i).childID;
          listEvidenceKey.evidenceType =
            childKeyList.dtls.item(i).childType;

          eiEvidenceKeyList.dtls.addRef(listEvidenceKey);

        }

      }
      <!-- END, CR00243480 -->

      eiEvidenceKeyList =
        evidenceControllerObj.filterActiveInEditChanges(eiEvidenceKeyList);

      for (int i = 0; i &lt; eiEvidenceKeyList.dtls.size(); i++) {

       <xsl:choose>
          <xsl:when test="count(Relationships/Child)&gt;0">
        AppException appException = new AppException(
          <xsl:value-of select="$generalError"/>.ERR_XRV_CHILD_EXISTS_FOR_PARENT_TO_DISCARD);

        // parent
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));

        // child
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          eiEvidenceKeyList.dtls.item(i).evidenceType));

        // parent
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));

        // child
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          eiEvidenceKeyList.dtls.item(i).evidenceType));
          </xsl:when>
          <xsl:when test="Relationships/@association='Yes'">
        AppException appException = new AppException(
          <xsl:value-of select="$generalError"/>.ERR_FV_REMOVE_RECORD_ASSOCIATED);

        // child
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          eiEvidenceKeyList.dtls.item(i).evidenceType));

        // parent
        appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
          CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));
          </xsl:when>
        </xsl:choose>
        if (mode.applyChanges) {
          informationalManager.addInformationalMsg(appException, "",
            InformationalElement.InformationalType.kError);
        } else {
          informationalManager.addInformationalMsg(appException, "",
            InformationalElement.InformationalType.kWarning);
        }
      }
    }
    </xsl:if>

  <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">


    if (!(mode.applyChanges &amp;&amp;
      evidenceDescriptorDtls.statusCode.equals(
      EVIDENCEDESCRIPTORSTATUS.CANCELED)) ||
      (mode.validateChanges &amp;&amp;
      evidenceDescriptorDtls.pendingRemovalInd)) {

      <xsl:for-each select="Relationships/MandatoryParents/Parent">
        <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
      if(evidenceDetails.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID == 0 &amp;&amp; !mode.insert){

        AppException appException = new AppException(
          <xsl:value-of select="$generalError"/>.ERR_FV_NO_PARENT_RECORD);

        // child
        appException.arg(curam.util.type.CodeTable.getOneItem(
          CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));

        // parent
        appException.arg(curam.util.type.CodeTable.getOneItem(
          CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="translate($lcParentName, $lcletters, $ucletters)"/>));

        if (mode.applyChanges) {
          informationalManager.addInformationalMsg(appException, "",
            InformationalElement.InformationalType.kError);
        } else {
          informationalManager.addInformationalMsg(appException, "",
            InformationalElement.InformationalType.kWarning);
        }
      }
      </xsl:for-each>
    }

    if (mode.applyChanges || mode.validateChanges) {

      <xsl:for-each select="Relationships/MandatoryParents/Parent">
        <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
      if (evidenceDetails.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID != 0) {

        // check to see if parent evidence is active
        RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey2
          = new RelatedIDAndEvidenceTypeKey();

        relatedIDAndEvidenceTypeKey2.relatedID
          = evidenceDetails.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID;
        relatedIDAndEvidenceTypeKey2.evidenceType
          = evidenceDetails.<xsl:value-of select="$lcParentName"/>ParEvKey.evType;
        curam.core.sl.infrastructure.entity.struct.
          EvidenceDescriptorStatusDtls evidenceDescriptorStatusDtls
            = evidenceDescriptorObj.readStatus(relatedIDAndEvidenceTypeKey2);

        if (!evidenceDescriptorStatusDtls
          .statusCode.equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)) {

          AppException appException = new AppException(
            <xsl:value-of select="$generalErrorMessage"/>.ERR_FV_NO_ACTIVE_PARENT_RECORD);

          // child
          appException.arg(curam.util.type.CodeTable.getOneItem(
            CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));
          appException.arg(curam.util.type.CodeTable.getOneItem(
            CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));

          // parent
          appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME, evidenceDetails.<xsl:value-of select="$lcParentName"/>ParEvKey.evType));

          if (mode.applyChanges) {
            informationalManager.addInformationalMsg(appException, "",
              InformationalElement.InformationalType.kError);
          } else {
            informationalManager.addInformationalMsg(appException, "",
              InformationalElement.InformationalType.kWarning);
          }
        }
      }
      </xsl:for-each>
    }

  </xsl:if>

  <xsl:if test="count(Relationships/Parent)&gt;0">
    if (!(mode.applyChanges &amp;&amp;
          evidenceDescriptorDtls.statusCode.equals(
          EVIDENCEDESCRIPTORSTATUS.CANCELED)) ||
         (mode.validateChanges &amp;&amp;
          evidenceDescriptorDtls.pendingRemovalInd)) {

          if (evidenceDetails.parEvKey.evidenceID == 0 &amp;&amp; !mode.insert) {

            AppException appException = new AppException(
              <xsl:value-of select="$generalError"/>.ERR_FV_NO_PARENT_RECORD);

            // child
            appException.arg(curam.util.type.CodeTable.getOneItem(
              CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));
            <xsl:variable name="propValue"><xsl:for-each select="Relationships/Parent">curam.util.type.CodeTable.getOneItem(
              CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="translate(@type, $lcletters, $ucletters)"/>)<xsl:text> + ", " + </xsl:text></xsl:for-each></xsl:variable>
            // parent
            appException.arg(<xsl:value-of select="substring($propValue,0,(string-length($propValue) - 8))"/>);

            if (mode.applyChanges) {
              informationalManager.addInformationalMsg(appException, "",
                InformationalElement.InformationalType.kError);
            } else {
              informationalManager.addInformationalMsg(appException, "",
                InformationalElement.InformationalType.kWarning);
            }

      }

    }

    if (mode.applyChanges || mode.validateChanges) {

      if (evidenceDetails.parEvKey.evidenceID != 0) {

        // check to see if parent evidence is active
        RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey2
          = new RelatedIDAndEvidenceTypeKey();

        relatedIDAndEvidenceTypeKey2.relatedID
          = evidenceDetails.parEvKey.evidenceID;
        relatedIDAndEvidenceTypeKey2.evidenceType
          = evidenceDetails.parEvKey.evType;
        curam.core.sl.infrastructure.entity.struct.
          EvidenceDescriptorStatusDtls evidenceDescriptorStatusDtls
            = evidenceDescriptorObj.readStatus(relatedIDAndEvidenceTypeKey2);

        if (!evidenceDescriptorStatusDtls
          .statusCode.equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)) {

          AppException appException = new AppException(
            <xsl:value-of select="$generalErrorMessage"/>.ERR_FV_NO_ACTIVE_PARENT_RECORD);

          // child
          appException.arg(curam.util.type.CodeTable.getOneItem(
            CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));
          appException.arg(curam.util.type.CodeTable.getOneItem(
            CASEEVIDENCE.TABLENAME, CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>));

          // parent
          appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME, evidenceDetails.parEvKey.evType));

          if (mode.applyChanges) {
            informationalManager.addInformationalMsg(appException, "",
              InformationalElement.InformationalType.kError);
          } else {
            informationalManager.addInformationalMsg(appException, "",
              InformationalElement.InformationalType.kWarning);
          }
        }
      }
    }
    </xsl:if>

    Validate<xsl:value-of select="$EntityName"/> validate<xsl:value-of select="$EntityName"/> =
      Validate<xsl:value-of select="$EntityName"/>Factory.newInstance();
    validate<xsl:value-of select="$EntityName"/>.validate(mode, evidenceDetails);

    <xsl:if test="count(EntityLayer/Event)>0">
    //
    // Entity Event Sending
    //

    if (mode.applyChanges &amp;&amp;
        evidenceDescriptorDtls.statusCode.equals(
        EVIDENCEDESCRIPTORSTATUS.ACTIVE) &amp;&amp;
        !evidenceDescriptorDtls.pendingRemovalInd) {

    Event evidenceEvent = new Event();
    Long correctionSetID = new Long(evidenceDescriptorDtls.correctionSetID);

    evidenceEvent.primaryEventData = correctionSetID.longValue();
    evidenceEvent.secondaryEventData = evidenceDescriptorDtls.caseID;
      <xsl:for-each select="EntityLayer/Event">
        <xsl:choose>
          <xsl:when test="@type=$eventTypeActivate">
    // Entity Event Sending for Activate
    evidenceEvent.eventKey =  <xsl:value-of select="$prefix"/>Evidence.<xsl:value-of select="$capName"/>Activated;
    EventService.raiseEvent(evidenceEvent);
          </xsl:when>
        </xsl:choose>
      </xsl:for-each>
    }
    </xsl:if>

  }

 // ___________________________________________________________________________
  /**
   * Reads all CaseParticipantRoles associated with a given evidence instance.
   *
   * @param key Contains the evidenceID / evidenceType pairings of the
   * evidence
   *
   * @returns A list of all CaseParticipantRoleDtls.
   */
  public CaseParticipantRoleDtlsList getCaseParticipantRoles(EIEvidenceKey key)
    throws AppException, InformationalException {

    CaseParticipantRoleDtlsList caseParticipantRoleDtlsList = new CaseParticipantRoleDtlsList();
    <xsl:choose>
      <xsl:when
        test="count(UserInterfaceLayer/Cluster/Field[
              (not(@notOnEntity) or @notOnEntity='No') and (
              @metatype=$metatypeEmployerCaseParticipant or
              @metatype=$metatypeCaseParticipantSearch or
              @metatype=$metatypeDisplayCaseParticipant)]) &gt; 0">
    CaseParticipantRoleDtls caseParticipantRoleDtls = new CaseParticipantRoleDtls();
    CaseParticipantRoleKey caseParticipantRoleKey = new CaseParticipantRoleKey();
    CaseParticipantRole caseParticipantRoleObj = CaseParticipantRoleFactory.newInstance();
    <xsl:value-of select="$returnType"/><xsl:text xml:space="preserve"> </xsl:text>evidenceDtls = (<xsl:value-of select="$returnType"/>)readEvidence(key);
    <xsl:for-each select="UserInterfaceLayer/Cluster/Field[
          (not(@notOnEntity) or @notOnEntity='No') and (
          @metatype=$metatypeEmployerCaseParticipant or
          @metatype=$metatypeCaseParticipantSearch or
          @metatype=$metatypeDisplayCaseParticipant)]">

    if (evidenceDtls.<xsl:value-of select="@columnName"/> != 0L) {

      // Find the ParticipantRoleID by using the existing CaseParticipantRoleID
      caseParticipantRoleKey.caseParticipantRoleID =
        evidenceDtls.<xsl:value-of select="@columnName"/>;

      caseParticipantRoleDtls =
        caseParticipantRoleObj.read(caseParticipantRoleKey);

      caseParticipantRoleDtlsList.dtls.add(caseParticipantRoleDtls);
    }
    </xsl:for-each>

    </xsl:when>
      <xsl:otherwise>
    // Only implemented for evidence with Case Participant Roles
    // <xsl:value-of select="$EntityName"/> has none
      </xsl:otherwise>
    </xsl:choose>

    return caseParticipantRoleDtlsList;
  }


  // ___________________________________________________________________________
  /*
   * Method that does any entity adjustments for moving the evidence record
   * to a new caseID
   *
   * @param details Contains the evidenceID / evidenceType pairings of the
   * evidence to be transferred
   * @param fromCaseKey The case from which the evidence is being transferred
   * @param toCaseKey The case to which the evidence is being transferred
   */
  public void transferEvidence(EvidenceTransferDetails details,
    CaseHeaderKey fromCaseKey, CaseHeaderKey toCaseKey)
    throws AppException, InformationalException {
    <!-- BEGIN, CR00120824, CD -->
    <xsl:choose>
      <xsl:when
        test="count(UserInterfaceLayer/Cluster/Field[
              (not(@notOnEntity) or @notOnEntity='No') and (
              @metatype=$metatypeEmployerCaseParticipant or
              @metatype=$metatypeCaseParticipantSearch or
              @metatype=$metatypeDisplayCaseParticipant)]) &gt; 0">
    // Get all the structs need for the operation
    EIEvidenceKey key = new EIEvidenceKey();

    <xsl:value-of select="$returnType"/><xsl:text xml:space="preserve"> </xsl:text>from<xsl:value-of select="$returnType"/>, to<xsl:value-of select="$returnType"/>;
    <xsl:value-of select="$EntityName"/>Key <xsl:value-of select="$lcName"/>Key = new <xsl:value-of select="$EntityName"/>Key();

    CaseParticipantRoleKey caseParticipantRoleKey =
      new CaseParticipantRoleKey();
    CaseParticipantRoleDtls caseParticipantRoleDtls;
    CaseIDParticipantRoleKey caseIDParticipantRoleKey =
      new CaseIDParticipantRoleKey();

    CaseParticipantRoleDtlsList caseParticipantRoleDtlsList;

    // Get the caseParticipantRole Obj
    CaseParticipantRole caseParticipantRoleObj =
      CaseParticipantRoleFactory.newInstance();

    //
    // Read the from Evidence entity details
    //
    key.evidenceID = details.fromEvidenceID;
    key.evidenceType = details.fromEvidenceType;
    from<xsl:value-of select="$returnType"/> = (<xsl:value-of select="$returnType"/>)readEvidence(key);

    //
    // Read the to evidence entity details
    //
    key.evidenceID = details.toEvidenceID;
    key.evidenceType = details.toEvidenceType;
    to<xsl:value-of select="$returnType"/> = (<xsl:value-of select="$returnType"/>)readEvidence(key);

    //
    // Get the case participant details
    //
    curam.core.sl.intf.CaseParticipantRole caseParticipantServiceLayerObj =
      curam.core.sl.fact.CaseParticipantRoleFactory.newInstance();

    CaseParticipantRoleDetails caseParticipantRoleDetails =
      new CaseParticipantRoleDetails();

    caseParticipantRoleDetails.dtls.caseID = toCaseKey.caseID;
    caseParticipantRoleDetails.dtls.fromDate = Date.getCurrentDate();
    caseParticipantRoleDetails.dtls.recordStatus = RECORDSTATUS.NORMAL;
    caseIDParticipantRoleKey.caseID = toCaseKey.caseID;

    <xsl:for-each select="UserInterfaceLayer/Cluster/Field[
          (not(@notOnEntity) or @notOnEntity='No') and (
          @metatype=$metatypeEmployerCaseParticipant or
          @metatype=$metatypeCaseParticipantSearch or
          @metatype=$metatypeDisplayCaseParticipant)]">
    <!-- BEGIN, CR00121996, CD -->
    if (from<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/> != 0L) {

      // Find the ParticipantRoleID by using the existing CaseParticipantRoleID
      caseParticipantRoleKey.caseParticipantRoleID =
        from<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/>;

      caseParticipantRoleDtls =
        caseParticipantRoleObj.read(caseParticipantRoleKey);

      // Need to search for the CaseParticipantRole that have the to CaseID
      // and the existing ParicipantRoleID. There should only be one.
      caseIDParticipantRoleKey.participantRoleID =
        caseParticipantRoleDtls.participantRoleID;

      caseParticipantRoleDtlsList = caseParticipantRoleObj
        .searchByParticipantRoleAndCase(caseIDParticipantRoleKey);

      caseParticipantRoleDetails.dtls.participantRoleID =
        caseParticipantRoleDtls.participantRoleID;

      // If the list is empty, it means the participant to whom the evidence
      // belongs is not a CPR on the toCase
      if (caseParticipantRoleDtlsList.dtls.isEmpty()) {

        <!-- BEGIN, CR00350369, GYH -->
        if (CASEPARTICIPANTROLETYPE.MEMBER.equals(caseParticipantRoleDtls.typeCode)) {

          curam.core.struct.CaseSearchKey caseSearchKey
            = new curam.core.struct.CaseSearchKey();
          caseSearchKey.caseID = toCaseKey.caseID;

          curam.core.struct.ConcernRoleKey concernRoleKey
            = new curam.core.struct.ConcernRoleKey();
          concernRoleKey.concernRoleID = caseParticipantRoleDtls.participantRoleID;

          AppException appException = new AppException(curam.message.<xsl:value-of select="$generalErrorMessage"/>.
            ERR_EVIDENCE_CANNOT_BE_SHARED_AS_RELATED_CASE_MEMBER_DOES_NOT_EXIST_ON_TARGET_CASE);

          appException.arg(curam.util.type.CodeTable.getOneItem(CASEEVIDENCE.TABLENAME,
            details.fromEvidenceType));
          appException.arg(curam.core.sl.impl.LocalizableXMLStringHelper.toPlainText(
            getDetailsForListDisplay(key).summary));
          appException.arg(curam.core.fact.CaseHeaderFactory.newInstance().
            readCaseReferenceByCaseID(caseSearchKey).caseReference);
          appException.arg(curam.core.fact.ConcernRoleFactory.newInstance().
            readConcernRoleName(concernRoleKey).concernRoleName);

          throw appException;
        }
        <!-- END, CR00350369 -->

        // never create a PRIMARY in transferEvidence
        if (caseParticipantRoleDtls.typeCode.equals(CASEPARTICIPANTROLETYPE.PRIMARY)) {
          caseParticipantRoleDetails.dtls.typeCode = CASEPARTICIPANTROLETYPE.MEMBER;
        } else {
          // use the 'from' type
          caseParticipantRoleDetails.dtls.typeCode =
            caseParticipantRoleDtls.typeCode;
        }

        // create a new record
        caseParticipantServiceLayerObj.insertCaseParticipantRole(
          caseParticipantRoleDetails);

        to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/> =
          caseParticipantRoleDetails.dtls.caseParticipantRoleID;

      } else {

        // the list is populated
        <!-- BEGIN, CR00127326, CD -->
        // attempt to reuse an existing CPR
        <xsl:variable name="fCN" select="@columnName"/>
        <xsl:variable
          name="createRoleType"
          select="../../../ServiceLayer/RelatedParticipantDetails[
                  @columnName=$fCN and
                  @createParticipant!='No' and
                  @createRoleType!='No']/@createRoleType"/>
        <xsl:variable name="targetType">
          <xsl:choose>
            <xsl:when test="$createRoleType!=''"><xsl:value-of select="$createRoleType"/></xsl:when>
            <xsl:otherwise>MEMBER</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <xsl:if test="$targetType!='MEMBER'">
        // a case participant with a type matching the 'from' type takes precedence
        for (int i=0; i&lt;caseParticipantRoleDtlsList.dtls.size(); i++) {
          if (caseParticipantRoleDtlsList.dtls.item(i).typeCode.equals(caseParticipantRoleDtls.typeCode)) {
            to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/> =
              caseParticipantRoleDtlsList.dtls.item(i).caseParticipantRoleID;
            break;
          }
        }
        // if no matches were found above
        </xsl:if>
        // <xsl:value-of select="$targetType"/> takes precedence
        if (from<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/>
            == to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/>) {
          for (int i=0; i&lt;caseParticipantRoleDtlsList.dtls.size(); i++) {
            if (caseParticipantRoleDtlsList.dtls.item(i).typeCode.equals(CASEPARTICIPANTROLETYPE.<xsl:value-of select="$targetType"/>)<xsl:if test="$targetType='MEMBER'"> ||
                caseParticipantRoleDtlsList.dtls.item(i).typeCode.equals(CASEPARTICIPANTROLETYPE.PRIMARY)</xsl:if>) {
              to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/> =
                caseParticipantRoleDtlsList.dtls.item(i).caseParticipantRoleID;
              break;
            }
          }
        }
        // if there are still no matches, use the <xsl:value-of select="$targetType"/> type to create a new record
        if (from<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/>
            == to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/>) {
          caseParticipantRoleDetails.dtls.typeCode = CASEPARTICIPANTROLETYPE.<xsl:value-of select="$targetType"/>;

          caseParticipantServiceLayerObj.insertCaseParticipantRole(
            caseParticipantRoleDetails);

          to<xsl:value-of select="$returnType"/>.<xsl:value-of select="@columnName"/> =
            caseParticipantRoleDetails.dtls.caseParticipantRoleID;
        }
        <!-- END, CR00127326 -->
      }

    }
    <!-- END, CR00121996, CD -->
        </xsl:for-each>

    <xsl:value-of select="$lcName"/>Key.evidenceID = details.toEvidenceID;
    modify(<xsl:value-of select="$lcName"/>Key, to<xsl:value-of select="$returnType"/>);
    <!-- END, CR00120824 -->
      </xsl:when>
      <xsl:otherwise>
    // Only implemented for evidence with Case Participant Roles
    // <xsl:value-of select="$EntityName"/> has none
      </xsl:otherwise>
    </xsl:choose>
  }

  <!-- BEGIN, CR00220936, CD -->
  // ___________________________________________________________________________
  /**
   * Method to get the business start date for this evidence record.
   *
   * @param evKey Contains an evidenceID / evidenceType pairing
   *
   * @return The start date for this evidence
   */
  public Date getStartDate(EIEvidenceKey evKey)
    throws AppException, InformationalException {

	<!-- BEGIN, CR00431339, SD -->
    <xsl:choose>
	  <xsl:when test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity='true'">
		<xsl:variable name="SLReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
	  // Evidence manipulation object
	  curam.evidence.service.intf.<xsl:value-of select="$EntityName"/> evidenceObj =
	    <xsl:value-of select="$EntityName"/>Factory.newInstance();

      // Return details
	  <xsl:value-of select="$SLReadEvidenceDetails"/> readEvidence =
        new <xsl:value-of select="$SLReadEvidenceDetails"/>();

	  EvidenceDescriptor descriptor = EvidenceDescriptorFactory.newInstance();
	  RelatedIDAndEvidenceTypeKey descriptorKey = new RelatedIDAndEvidenceTypeKey();
	  descriptorKey.relatedID = evKey.evidenceID;
	  descriptorKey.evidenceType = evKey.evidenceType;
	  EvidenceDescriptorDtls readByRelatedIDAndType = descriptor.readByRelatedIDAndType(descriptorKey);

	  EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
	  evidenceCaseKey.caseIDKey.caseID = readByRelatedIDAndType.caseID;
	  evidenceCaseKey.evidenceKey.evidenceID = evKey.evidenceID;
	  evidenceCaseKey.evidenceKey.evType = evKey.evidenceType;

	  return evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(evidenceCaseKey).nonEvidenceDetails.<xsl:value-of select="BusinessDates/@startDate"/>;</xsl:when>
	  <xsl:when test="BusinessDates/@startDate!=''">return ((<xsl:value-of select="$returnType"/>)readEvidence(evKey)).<xsl:value-of select="BusinessDates/@startDate"/>;</xsl:when>
      <xsl:otherwise>return null;</xsl:otherwise>
    </xsl:choose>
	<!-- END, CR00431339 -->
  }


  // ___________________________________________________________________________
  /**
   * Method to get the business end date for this evidence record.
   *
   * @param evKey Contains an evidenceID / evidenceType pairing
   *
   * @return The end date for this evidence
   */
  public Date getEndDate(EIEvidenceKey evKey)
    throws AppException, InformationalException {

	<!-- BEGIN, CR00431339, SD -->
	<xsl:choose>
	  <xsl:when test="BusinessDates/@endDate!='' and BusinessDates/@endDateNotOnEntity='true'">
		<xsl:variable name="SLReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
	  // Evidence manipulation object
	  curam.evidence.service.intf.<xsl:value-of select="$EntityName"/> evidenceObj =
	    <xsl:value-of select="$EntityName"/>Factory.newInstance();

      // Return details
	  <xsl:value-of select="$SLReadEvidenceDetails"/> readEvidence =
        new <xsl:value-of select="$SLReadEvidenceDetails"/>();

	  EvidenceDescriptor descriptor = EvidenceDescriptorFactory.newInstance();
	  RelatedIDAndEvidenceTypeKey descriptorKey = new RelatedIDAndEvidenceTypeKey();
	  descriptorKey.relatedID = evKey.evidenceID;
	  descriptorKey.evidenceType = evKey.evidenceType;
	  EvidenceDescriptorDtls readByRelatedIDAndType = descriptor.readByRelatedIDAndType(descriptorKey);

	  EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
	  evidenceCaseKey.caseIDKey.caseID = readByRelatedIDAndType.caseID;
	  evidenceCaseKey.evidenceKey.evidenceID = evKey.evidenceID;
	  evidenceCaseKey.evidenceKey.evType = evKey.evidenceType;

	  return evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(evidenceCaseKey).nonEvidenceDetails.<xsl:value-of select="BusinessDates/@endDate"/>;</xsl:when>
	  <xsl:when test="BusinessDates/@endDate!=''">return ((<xsl:value-of select="$returnType"/>)readEvidence(evKey)).<xsl:value-of select="BusinessDates/@endDate"/>;</xsl:when>
      <xsl:otherwise>return null;</xsl:otherwise>
    </xsl:choose>
	<!-- END, CR00431339 -->
  }
  <!-- END, CR00220936, CD -->


  /**
   * Method to set the business end date for the give evidence object.
   *
   * @param dtls
   *          The evidence object to be updated
   * @param date
   *          The value which the evidence object's end date should be set with
   *
   * @throws AppException
   *           {@link BPOEVIDENCEMESSAGE#ERR_ENT_EVIDENCE_ERROR_ENDDATE_FV_ERRORMSG_NOT_SET}
   *           thrown if the evidence type has no End Date attribute
   * @throws InformationalException
   *           Generic Exception Message
   */
  public void setEndDate(Object dtls, Date date)
    throws AppException, InformationalException {
  <xsl:choose>
    <xsl:when test="BusinessDates/@endDate!='' and BusinessDates/@endDateNotOnEntity='true'">
    <xsl:variable name="SLReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
    // Evidence manipulation object
    curam.evidence.service.intf.<xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

    final long evidenceID = ((<xsl:value-of select="$returnType"/>) dtls).evidenceID;
    final String evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    EvidenceDescriptor descriptor = EvidenceDescriptorFactory.newInstance();
    RelatedIDAndEvidenceTypeKey descriptorKey = new RelatedIDAndEvidenceTypeKey();
    descriptorKey.relatedID = evidenceID;
    descriptorKey.evidenceType = evidenceType;
    EvidenceDescriptorDtls readByRelatedIDAndType = descriptor.readByRelatedIDAndType(descriptorKey);

    EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
    evidenceCaseKey.caseIDKey.caseID = readByRelatedIDAndType.caseID;
    evidenceCaseKey.evidenceKey.evidenceID = evidenceID;
    evidenceCaseKey.evidenceKey.evType = evidenceType;

    evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(evidenceCaseKey).nonEvidenceDetails.<xsl:value-of select="BusinessDates/@endDate"/> = date;</xsl:when>
    <xsl:when test="BusinessDates/@endDate!=''">((<xsl:value-of select="$returnType"/>) dtls).<xsl:value-of select="BusinessDates/@endDate"/> = date;</xsl:when>
      <xsl:otherwise>// No End Date attribute exists on this evidence type
      throw curam.message.impl.BPOEVIDENCEExceptionCreator
          .ERR_ENT_EVIDENCE_ERROR_ENDDATE_FV_ERRORMSG_NOT_SET(CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>);</xsl:otherwise>
    </xsl:choose>
  }

  /**
   * Method to set the business start date for the give evidence object.
   *
   * @param dtls
   *          The evidence object to be updated
   * @param date
   *          The value which the evidence object's start date should be set with
   *
   * @throws AppException
   *           {@link BPOEVIDENCEMESSAGE#ERR_ENT_EVIDENCE_ERROR_STARTDATE_FV_ERRORMSG_NOT_SET}
   *           thrown if the evidence type has no Start Date attribute
   * @throws InformationalException
   *           Generic Exception Message
   */
  public void setStartDate(Object dtls, Date date)
    throws AppException, InformationalException {
  <xsl:choose>
    <xsl:when test="BusinessDates/@startDate!='' and BusinessDates/@startDateNotOnEntity='true'">
    <xsl:variable name="SLReadEvidenceDetails">Read<xsl:value-of select="$EntityName"/>EvidenceDetails</xsl:variable>
    // Evidence manipulation object
    curam.evidence.service.intf.<xsl:value-of select="$EntityName"/> evidenceObj =
      <xsl:value-of select="$EntityName"/>Factory.newInstance();

      final long evidenceID = ((<xsl:value-of select="$returnType"/>) dtls).evidenceID;
      final String evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    EvidenceDescriptor descriptor = EvidenceDescriptorFactory.newInstance();
    RelatedIDAndEvidenceTypeKey descriptorKey = new RelatedIDAndEvidenceTypeKey();
    descriptorKey.relatedID = evidenceID;
    descriptorKey.evidenceType = evidenceType;
    EvidenceDescriptorDtls readByRelatedIDAndType = descriptor.readByRelatedIDAndType(descriptorKey);

    EvidenceCaseKey evidenceCaseKey = new EvidenceCaseKey();
    evidenceCaseKey.caseIDKey.caseID = readByRelatedIDAndType.caseID;
    evidenceCaseKey.evidenceKey.evidenceID = evidenceID;
    evidenceCaseKey.evidenceKey.evType = evidenceType;

    evidenceObj.read<xsl:value-of select="$EntityName"/>Evidence(evidenceCaseKey).nonEvidenceDetails.<xsl:value-of select="BusinessDates/@startDate"/> = date;</xsl:when>
    <xsl:when test="BusinessDates/@startDate!=''">((<xsl:value-of select="$returnType"/>) dtls).<xsl:value-of select="BusinessDates/@startDate"/> = date;</xsl:when>
      <xsl:otherwise>// No Start Date attribute exists on this evidence type
      throw curam.message.impl.BPOEVIDENCEExceptionCreator
          .ERR_ENT_EVIDENCE_ERROR_STARTDATE_FV_ERRORMSG_NOT_SET(CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>);</xsl:otherwise>
    </xsl:choose>
  }

  <!-- BEGIN, CR00244854, POB -->
  <xsl:if test="count(Relationships/Parent)&gt;0 or count(Relationships/MandatoryParents/Parent)&gt;0 or count(Relationships/PreAssociation[@to!=''])&gt;0">
  private void fillInRelatedDetails(<xsl:value-of select="$EntityName"/>EvidenceDetails details)
    throws AppException, InformationalException {

    <xsl:variable name="evidenceTypeCode"><xsl:value-of select="translate($capName, $lcletters, $ucletters)"/></xsl:variable>

    EIEvidenceKey childKey = new EIEvidenceKey();
    childKey.evidenceID = details.dtls.evidenceID;
    childKey.evidenceType = CASEEVIDENCE.<xsl:value-of select="$evidenceTypeCode"/>;

    <!-- BEGIN, CR00411874, ELG -->
    final EIEvidenceKeyList incomingParentEvidenceKeyList = new EIEvidenceKeyList();

    if(details.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.IDENTICALINEDIT)){

      final ParentList incomingParentList
        = curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory.newInstance().getIncomingParentKeyList(childKey);

      for (int i = 0; i &lt; incomingParentList.list.dtls.size(); i++) {

        EIEvidenceKey eiParentEvidenceKey = new EIEvidenceKey();

        eiParentEvidenceKey.evidenceID =
          incomingParentList.list.dtls.item(i).parentID;
        eiParentEvidenceKey.evidenceType =
          incomingParentList.list.dtls.item(i).parentType;

        incomingParentEvidenceKeyList.dtls.addRef(eiParentEvidenceKey);

      }

    }

    final ParentList parentList
      = curam.core.sl.infrastructure.fact.EvidenceRelationshipFactory.newInstance().getParentKeyList(childKey);

    final EIEvidenceKeyList parentEvidenceKeyList = new EIEvidenceKeyList();

    for (int j = 0; j &lt; parentList.list.dtls.size(); j++) {

      EIEvidenceKey eiParentEvidenceKey = new EIEvidenceKey();

      eiParentEvidenceKey.evidenceID =
        parentList.list.dtls.item(j).parentID;
      eiParentEvidenceKey.evidenceType =
        parentList.list.dtls.item(j).parentType;

      parentEvidenceKeyList.dtls.addRef(eiParentEvidenceKey);

    }

    // Filter the list for active and in-edit
    final EIEvidenceKeyList filteredParentEvidenceKeyList
      = EvidenceControllerFactory.newInstance().filterActiveInEditChanges(parentEvidenceKeyList);
    EIEvidenceKey parentKey = new EIEvidenceKey();

    if(details.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)
         || details.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.INEDIT) || details.descriptor.statusCode
            .equals(EVIDENCEDESCRIPTORSTATUS.CANCELED)){

      parentKey = findAndSetParentKey(filteredParentEvidenceKeyList, details);

    }  else if (details.descriptor.statusCode.equals(EVIDENCEDESCRIPTORSTATUS.IDENTICALINEDIT)) {

      final EIEvidenceKey incomingParentKey
        = findAndSetParentKey(incomingParentEvidenceKeyList, details);

      if (incomingParentKey.evidenceID == 0) {
        parentKey = findAndSetParentKey(filteredParentEvidenceKeyList, details);
      }

    }
    <!-- END, CR00411874 -->

  }

  <!-- BEGIN, CR00411874, ELG -->
  // ___________________________________________________________________________
  /**
   * Method to find and set parent evidence key.
   *
   * @param parentEvidenceKeyList Contains parent evidence key list.
   *
   * @return The key for parent evidence.
   */
  private EIEvidenceKey findAndSetParentKey(final EIEvidenceKeyList parentEvidenceKeyList, <xsl:value-of select="$EntityName"/>EvidenceDetails details)
    throws AppException, InformationalException {

    EIEvidenceKey parentKey = new EIEvidenceKey();

    if (parentEvidenceKeyList.dtls.size() > 0) {

      for (int i = 0; i &lt; parentEvidenceKeyList.dtls.size(); i++) {

        parentKey = parentEvidenceKeyList.dtls.item(i);

        // get the parent period
        final EvidenceControllerInterface evidenceControllerObj = (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

        final EIEvidenceKey evidenceKey = new EIEvidenceKey();
        evidenceKey.evidenceID = parentEvidenceKeyList.dtls.item(i).evidenceID;
        evidenceKey.evidenceType = parentEvidenceKeyList.dtls.item(i).evidenceType;
        // get the evidence period for the parent(This should be the
        // startDate/Effective date to the end date/Virtual end date)
        final EvidencePeriod parentEvidencePeriod = evidenceControllerObj.getPeriodForEvidenceRecord(evidenceKey);

        // get the child evidence period
        evidenceKey.evidenceID = details.dtls.evidenceID;
        evidenceKey.evidenceType = details.descriptor.evidenceType;
        final EvidencePeriod childEvidencePeriod = evidenceControllerObj.getPeriodForEvidenceRecord(evidenceKey);

        // check if the child evidence period overlaps with the parent period. If there is only 1 parent then use that regardless if the periods cross over or not.
        if (childEvidencePeriod.startDate.before(parentEvidencePeriod.endDate)
            &amp;&amp; (parentEvidencePeriod.startDate
                .before(childEvidencePeriod.endDate)
                || childEvidencePeriod.endDate.isZero())
            || parentEvidencePeriod.endDate.isZero()
                &amp;&amp; childEvidencePeriod.endDate.isZero()
            || parentEvidencePeriod.endDate.isZero()
                &amp;&amp; childEvidencePeriod.endDate
                    .after(parentEvidencePeriod.startDate)) {

        <xsl:if test="count(Relationships/Parent)&gt;0">
        <xsl:for-each select="Relationships/Parent">
        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.parEvKey.evidenceID = parentKey.evidenceID;
          details.parEvKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

        <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
        <xsl:for-each select="Relationships/MandatoryParents/Parent">

        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
        <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>

        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID = parentKey.evidenceID;
          details.<xsl:value-of select="$lcParentName"/>ParEvKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

        <xsl:if test="count(Relationships/PreAssociation[@to!=''])&gt;0">
        <xsl:for-each select="Relationships/PreAssociation[@to!='']">
        <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@to, $lcletters, $ucletters)"/></xsl:variable>
        if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
          details.preAssocKey.evidenceID = parentKey.evidenceID;
          details.preAssocKey.evType = parentKey.evidenceType;
        }
        </xsl:for-each>
        </xsl:if>

      }
     }
     // if the parent details are not set after checking evidence periods then
     // fall back to old way of setting the parent.
     <xsl:if test="count(Relationships/Parent)&gt;0">
     <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
     if (details.parEvKey.evidenceID == 0) {

      for (int i = 0; i &lt; parentEvidenceKeyList.dtls.size(); i++) {

       parentKey = parentEvidenceKeyList.dtls.item(i);

       <xsl:if test="count(Relationships/Parent)&gt;0">
       <xsl:for-each select="Relationships/Parent">
       <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
       if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
         details.parEvKey.evidenceID = parentKey.evidenceID;
         details.parEvKey.evType = parentKey.evidenceType;
       }
      </xsl:for-each>
      </xsl:if>

      }
     }
     </xsl:if>

     <xsl:if test="count(Relationships/MandatoryParents/Parent)&gt;0">
     <xsl:for-each select="Relationships/MandatoryParents/Parent">
     <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@name, $lcletters, $ucletters)"/></xsl:variable>
     <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
     if (details.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID == 0) {

     for (int i = 0; i &lt; parentEvidenceKeyList.dtls.size(); i++) {

      parentKey = parentEvidenceKeyList.dtls.item(i);

       if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
        details.<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID = parentKey.evidenceID;
        details.<xsl:value-of select="$lcParentName"/>ParEvKey.evType = parentKey.evidenceType;
       }
      }
     }
     </xsl:for-each>
     </xsl:if>

     <xsl:if test="count(Relationships/PreAssociation[@to!=''])&gt;0">
     <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@to, $lcletters, $ucletters)"/></xsl:variable>
     if (details.preAssocKey.evidenceID == 0) {

     for (int i = 0; i &lt; parentEvidenceKeyList.dtls.size(); i++) {

      parentKey = parentEvidenceKeyList.dtls.item(i);

      <xsl:if test="count(Relationships/PreAssociation[@to!=''])&gt;0">
      <xsl:for-each select="Relationships/PreAssociation[@to!='']">
      <xsl:variable name="parentTypeCode"><xsl:value-of select="translate(@to, $lcletters, $ucletters)"/></xsl:variable>
      if (parentKey.evidenceType.equals(CASEEVIDENCE.<xsl:value-of select="$parentTypeCode"/>)) {
        details.preAssocKey.evidenceID = parentKey.evidenceID;
        details.preAssocKey.evType = parentKey.evidenceType;
      }
      </xsl:for-each>
      </xsl:if>
     }
    }
    </xsl:if>

   }

    return parentKey;
  }
  <!-- END, CR00411874 -->

  </xsl:if>
  <!-- END, CR00244854 -->

}

    </redirect:write>

    <xsl:value-of select="$capName"/><xsl:text>
</xsl:text>
</xsl:template>

  <xsl:template match="text()"/>

  <!-- GetSortedImports and GetUnsortedImports makes -->
  <!-- organising the imports a lot easier.          -->
  <xsl:template name="GetSortedImports">
    <xsl:param name="entity"/>
    <xsl:variable name="file"><xsl:value-of select="$productEvidenceHome"/>/gen.tmp/imports/entity/<xsl:value-of select="$entity"/>.imports</xsl:variable>
    <xsl:variable name="unsorted" select="document($file)/imports"/>
    <xsl:for-each select="$unsorted/import"><xsl:sort select="@value"/>
<xsl:value-of select="@value"/><xsl:text>&#10;</xsl:text>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
