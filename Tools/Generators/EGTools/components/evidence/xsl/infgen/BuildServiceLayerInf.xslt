<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2006, 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    Copyright (c) 2006-2008,2010 Curam Software Ltd.  All rights reserved.

    This software is the confidential and proprietary information of Curam
    Software, Ltd. (&quot;Confidential Information&quot;).  You shall not
    disclose such Confidential Information and shall use it only in accordance
    with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Main template, calls templates to build Service Layer Inf -->
    <xsl:template name="buildServiceLayerInf">

        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <!-- Build service layer process classes -->
        <xsl:for-each select="EvidenceEntity">

            <xsl:call-template name="buildServiceLayerProcessClass">
              <xsl:with-param select="@name" name="EntityName"/>
              <xsl:with-param select="$prefix" name="prefix"/>
              <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
              <xsl:with-param select="$product" name="product"/>
            </xsl:call-template>

            <!-- BEGIN, CR00100405, CD -->
            <xsl:call-template name="buildServiceLayerHookClass">
              <xsl:with-param select="@name" name="EntityName"/>
              <xsl:with-param select="$product" name="product"/>
            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerValidationClass">
                <xsl:with-param select="@name" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>
            </xsl:call-template>

            <xsl:if test="Relationships/@relatedEntityAttributes='Yes'">

              <xsl:call-template name="buildRelatedEntityAttributesProcessClass">
                  <xsl:with-param select="@name" name="EntityName"/>
                  <xsl:with-param select="$prefix" name="prefix"/>
                  <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                  <xsl:with-param select="$product" name="product"/>
                  <xsl:with-param select="Relationships/@preAssociation" name="preAssoc"/>
                  <xsl:with-param select="Relationships/@multiplePreAssociationID" name="multiplePreAssoc"/>
                  <xsl:with-param select="Relationships/@exposeOperation" name="exposeOperation"/>
              </xsl:call-template>

            </xsl:if>
            <!-- END, CR00100405 -->

            <xsl:call-template name="buildServiceLayerCustomiseClass">
          <xsl:with-param select="@name" name="EntityName"/>
          <xsl:with-param select="$prefix" name="prefix"/>
          <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
          <xsl:with-param select="$product" name="product"/>
            </xsl:call-template>

        </xsl:for-each>

        <xsl:call-template name="buildPackages">

          <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
          <xsl:with-param select="$product" name="product"/>

        </xsl:call-template>

        <xsl:call-template name="buildEvidenceRegistrarProcessClass">

          <xsl:with-param select="$prefix" name="prefix"/>
          <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
          <xsl:with-param select="$product" name="product"/>

        </xsl:call-template>


    </xsl:template>

    <!-- Template to add ServiceLayer class name, calls all operation builder templates,
         appends Class footer -->
    <xsl:template name="buildServiceLayerProcessClass">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <!-- Class Header -->
        <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::Service::{$EntityName}::{$EntityName}" PARENT_QUID="{$product}::Evidence::Service::{$EntityName}"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Service::{$EntityName}::{$EntityName}"
            NAME="{$EntityName}">

            <xsl:call-template name="buildServiceLayerCreateOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerReadOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerModifyOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerIncomingAddToCaseOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerIncomingModifyOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerGetCaseParticipantOperation">

              <xsl:with-param select="$EntityName" name="EntityName"/>
              <xsl:with-param select="$prefix" name="prefix"/>
              <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
              <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

          <!-- shouldn't need this anymore as it's wrapped in the standard read method

          <xsl:call-template name="buildServiceLayerRelatedAttributesOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template> -->

            <xsl:call-template name="buildServiceLayerAssociationOperation">

                <xsl:with-param select="$EntityName" name="EntityName"/>
                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                <xsl:with-param select="$product" name="product"/>

            </xsl:call-template>

            <xsl:call-template name="buildServiceLayerOptionalParticipantOperations">

                <xsl:with-param select="$EntityName" name="EntityName"/>

            </xsl:call-template>

            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>

    </xsl:template>

    <!-- Template to output create operation -->
    <xsl:template name="buildServiceLayerCreateOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>
        <!-- BEGIN, CR00127279, CD -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
            STEREOTYPE="" QUID="" NAME="create{$EntityName}Evidence">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>
        <!-- END, CR00127279 -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
            STEREOTYPE="" QUID="" NAME="create{$EntityName}Evidence">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.EvidenceDescriptorDtls" STEREOTYPE="" NAME="sourceEvidenceDescriptorDtls" MODE=""/>
            <CLASS.OPERATION.PARAMETER TYPE="core.CaseHeaderDtls" STEREOTYPE="" NAME="targetCase" MODE=""/>
            <CLASS.OPERATION.PARAMETER TYPE="CURAM_INDICATOR" STEREOTYPE="" NAME="sharingInd" MODE=""/>
        </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output read operation -->
    <xsl:template name="buildServiceLayerReadOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <CLASS.OPERATION VISIBILITY="Public"
            TYPE="{$modelEvidenceCodePath}.entity.Read{$EntityName}EvidenceDetails"
            STEREOTYPE="" QUID="" NAME="read{$EntityName}Evidence">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
        </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output modify operation -->
    <xsl:template name="buildServiceLayerModifyOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="modify{$EntityName}Evidence" >
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails"
             STEREOTYPE="" NAME="dtls" MODE="" />
        </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output Incoming Add To Case operation -->
    <xsl:template name="buildServiceLayerIncomingAddToCaseOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="incomingAddToCase{$EntityName}Evidence" >
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE="" >
            </CLASS.OPERATION.PARAMETER>
        </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output incoming modify -->
    <xsl:template name="buildServiceLayerIncomingModifyOperation">
        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="incomingModify{$EntityName}Evidence" >
            <CLASS.OPERATION.PARAMETER TYPE="guidedchanges.facade.WizardKey" STEREOTYPE="" NAME="wizardKey" MODE="" />
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE="" >

                <xsl:variable name="resultOfMandatoryFieldProcessing">
                  <xsl:call-template name="getMandatoryFields">
                    <xsl:with-param name="EntityName" select="$EntityName"/>
                  </xsl:call-template>
                </xsl:variable>

                <!-- BEGIN, CR00098722, CD -->
                <!-- BEGIN, CR00099784, POB -->
                <CLASS.OPERATION.PARAMETER.OPTION NAME="MANDATORY_FIELDS">
                    <xsl:attribute name="VALUE">descriptor.receivedDate,<xsl:value-of select="$resultOfMandatoryFieldProcessing"/></xsl:attribute>
                </CLASS.OPERATION.PARAMETER.OPTION>
                <!-- END, CR00098784 -->
                 <!-- END, CR00098722 -->


            </CLASS.OPERATION.PARAMETER>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for modifying <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
    </xsl:template>

    <xsl:template name="buildServiceLayerGetCaseParticipantOperation">

      <xsl:param name="EntityName"/>
      <xsl:param name="prefix"/>
      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>

      <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
         STEREOTYPE="" QUID="" NAME="getCaseParticipantRoleID" >
        <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey"
         STEREOTYPE="" NAME="key" MODE="" />
      </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output readRelatedAttributes operation -->
    <xsl:template name="buildServiceLayerRelatedAttributesOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <!-- Check if Related Attributes exist, if so iterate through them adding read operations -->
        <xsl:if test="Relationships/@relatedEntityAttributes='Yes'">

            <xsl:for-each select="Relationships/RelatedEntityAttribute">

                <xsl:choose>
                  <!-- check to see if ns operation,if it is use modelled struct specified in metadata as return type -->
                  <xsl:when test="count(Operation)&gt;0">

                      <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.entity.{Operation/@type}"
                            STEREOTYPE="" QUID="" NAME="{Operation/@name}">
                          <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
                    </CLASS.OPERATION>

                  </xsl:when>

                  <xsl:otherwise>
                    <!-- no ns operation is present, use generated Service layer struct -->
                    <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.service.{$EntityName}Related{@name}Attributes"
                        STEREOTYPE="" QUID="" NAME="read{$EntityName}Related{@name}Attributes">
                        <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
                    </CLASS.OPERATION>

                  </xsl:otherwise>

                </xsl:choose>

            </xsl:for-each>

        </xsl:if>

    </xsl:template>

    <!-- Template to output list<EntityName>WithoutAssociations operation -->
    <xsl:template name="buildServiceLayerAssociationOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <!-- check if more that 1 Associations exist,and displayInHierarchy is equal to Yes-->
        <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">

                <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ListEvidenceDetails"
                    STEREOTYPE="" QUID="" NAME="list{$EntityName}EvidenceWithoutAssociations">
                    <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceListKey" STEREOTYPE="" NAME="key" MODE=""/>
                </CLASS.OPERATION>

        </xsl:if>

    </xsl:template>

    <!-- Template to output get<EntityName><ParticipantShortName>OptionalParticipantDetails operation -->
    <xsl:template name="buildServiceLayerOptionalParticipantOperations">

      <xsl:param name="EntityName"/>

      <!-- Get all fields where a caseParticipantRole could possibly not have been created on the create screen
           i.e. mandatory!='Yes'.  Also, we're only interested in CaseParticipantRoles that can be created from
           the modify screen, i.e. ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes'.  Note, this
           modify attribute only means that it can be created from the modify screen.  It does not mean that it
           can be modified once entered.
       -->
      <xsl:for-each select="ServiceLayer/RelatedParticipantDetails[@modifyParticipant='Yes']">

        <xsl:variable name="rPcolumnName" select="@columnName"/>

        <xsl:variable name="shortName">
          <xsl:call-template name="capitalize">
            <xsl:with-param name="convertThis" select="@name"/>
          </xsl:call-template>
        </xsl:variable>

        <xsl:for-each select="../../UserInterfaceLayer/Cluster/Field[@columnName=$rPcolumnName and (not(@mandatory) or @mandatory!='Yes')]">

              <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.OptionalParticipantDetails"
                        STEREOTYPE="" QUID="" NAME="get{$EntityName}{$shortName}OptionalParticipantDetails">
                        <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
              </CLASS.OPERATION>

         </xsl:for-each>

       </xsl:for-each>

    </xsl:template>

    <xsl:template name="buildEvidenceRegistrarProcessClass">

      <xsl:param name="prefix"/>
      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>

      <!-- Class header -->
      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::Service::{$prefix}EvidenceRegistrar" PARENT_QUID="{$product}::Evidence::Service"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Service::{$prefix}EvidenceRegistrar"
        NAME="{$prefix}EvidenceRegistrar">

        <!-- Class footer -->
        <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        <CLASS.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[@deprecated Since Curam 6.0, replaced with {@link curam.<xsl:value-of select="$modelEvidenceCodePath"/>.service.impl.<xsl:value-of select="$prefix"/>RegistrarModule}.

  As part of registrar modernization, all type/object hook mappings of this class have  been moved to an AbstractModule class <xsl:value-of select="$prefix"/>RegistrarModule. See release note: CEF-586.

]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.DOCUMENTATION>
      </CLASS>
    </xsl:template>

    <!-- BEGIN, CR00100405, CD -->
    <!-- Removing Override callout class target -->
    <!-- END, CR00100405, CD -->

    <!-- Template to add the callout operation -->
    <xsl:template name="buildProcessCalloutOperation">

      <xsl:param name="EntityName"/>
      <xsl:param name="modelEvidenceCodePath"/>

      <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails"
        STEREOTYPE="" QUID="" NAME="processCallout">
        <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
      </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to add the Attribution class for each entity -->
    <xsl:template name="buildServiceLayerHookClass">
      <!-- BEGIN, CR00100405, CD -->
      <xsl:param name="EntityName"/>
      <xsl:param name="product"/>

      <xsl:variable name="methods">
        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.entity.AttributedDateDetails"
          STEREOTYPE="" QUID="" NAME="calcAttributionDatesForCase">
          <CLASS.OPERATION.PARAMETER TYPE="core.CaseKey" STEREOTYPE="" NAME="caseKey" MODE=""/>
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="evKey" MODE=""/>
        </CLASS.OPERATION>

        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.EIFieldsForListDisplayDtls"
          STEREOTYPE="" QUID="" NAME="getDetailsForListDisplay">
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="evKey" MODE=""/>
        </CLASS.OPERATION>
      </xsl:variable>
      <xsl:variable name="footer">
      <!-- Class Footer -->
        <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
      </xsl:variable>

      <!-- Class Header -->
      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::Hook::{$EntityName}Hook" PARENT_QUID="{$product}::Evidence::Hook"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Hook::{$EntityName}Hook"
        NAME="{$EntityName}Hook">
      <xsl:copy-of select="$methods"/>
      <xsl:copy-of select="$footer"/>
      </CLASS>


      <xsl:if test="EntityLayer/Override/@hook='Yes'">

      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="Custom::{$product}::Evidence::Hook::{$EntityName}Hook" PARENT_QUID="Custom::{$product}::Evidence::Hook"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Hook::{$EntityName}Hook"
        NAME="{$EntityName}Hook">
        <xsl:copy-of select="$methods"/>
        <CLASS.OPTION NAME="REPLACE_SUPERCLASS" VALUE="yes"/>
        <CLASS.INHERIT
          NAME="{$EntityName}Hook"
          QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Hook::{$EntityName}Hook" QUID="{$product}::Evidence::Hook::{$EntityName}Hook"
        />
        <xsl:copy-of select="$footer"/>
      </CLASS>
      </xsl:if>
      <!-- END, CR00100405 -->
    </xsl:template>

    <!-- Template to add the Customise class for each entity -->
    <xsl:template name="buildServiceLayerCustomiseClass">

      <xsl:param name="EntityName"/>
      <xsl:param name="prefix"/>
      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>
      <!-- BEGIN, CR00100405, CD -->
      <xsl:variable name="methods">
        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="preCreate">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>

        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="postCreate">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.ReturnEvidenceDetails" STEREOTYPE="" NAME="returnDtls" MODE=""/>
        </CLASS.OPERATION>

        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="preModify">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>

        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="postModify">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.ReturnEvidenceDetails" STEREOTYPE="" NAME="returnDtls" MODE=""/>
        </CLASS.OPERATION>

        <!-- BEGIN, 25/02/2008, CD -->
        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="preRead">
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
        </CLASS.OPERATION>

        <!-- Add the operation to which the custom code will be written -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="postRead">
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.Read{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="readEvidenceDetails" MODE=""/>
        </CLASS.OPERATION>
        <!-- END, 25/02/2008, CD -->
      </xsl:variable>
      <xsl:variable name="footer">
        <!-- Class Footer -->
        <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
     </xsl:variable>

     <!-- Class Header -->
     <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::Customise::Customise{$EntityName}" PARENT_QUID="{$product}::Evidence::Customise"
       QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Customise::Customise{$EntityName}"
        NAME="Customise{$EntityName}">
      <xsl:copy-of select="$methods"/>
      <xsl:copy-of select="$footer"/>
      </CLASS>

      <xsl:if test="EntityLayer/Override/@customize='Yes'">

      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="Custom::{$product}::Evidence::Customise::Customise{$EntityName}" PARENT_QUID="Custom::{$product}::Evidence::Customise"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Customise::Customise{$EntityName}"
        NAME="Customise{$EntityName}">
        <xsl:copy-of select="$methods"/>
        <CLASS.OPTION NAME="REPLACE_SUPERCLASS" VALUE="yes"/>
        <CLASS.INHERIT
          NAME="Customise{$EntityName}"
          QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Customise::Customise{$EntityName}" QUID="{$product}::Evidence::Customise::Customise{$EntityName}"
        />
        <xsl:copy-of select="$footer"/>
      </CLASS>
      </xsl:if>
      <!-- END, CR00100405 -->
    </xsl:template>

    <!-- Template to build packages for Hook, Validation, RelatedEntityAttributes and Customise classes -->
    <xsl:template name="buildPackages">

      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>

      <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Hook" QUID="{$product}::Evidence::Hook">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.hook"/>
      </PACKAGE>
      <!-- BEGIN, CR00100405, CD -->
      <xsl:if test="count(EvidenceEntity/EntityLayer/Override[@hook='Yes'])&gt;0">
      <PACKAGE NAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Hook" QUID="Custom::{$product}::Evidence::Hook">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$customModelEvidenceCodePath}.hook"/>
      </PACKAGE>
      </xsl:if>

      <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Validation" QUID="{$product}::Evidence::Validation">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.validation"/>
      </PACKAGE>

      <xsl:if test="count(EvidenceEntity/EntityLayer/Override[@validation='Yes'])&gt;0">
      <PACKAGE NAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Validation" QUID="Custom::{$product}::Evidence::Validation">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$customModelEvidenceCodePath}.validation"/>
      </PACKAGE>
      </xsl:if>

      <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::RelatedEntityAttributes" QUID="{$product}::Evidence::RelatedEntityAttributes">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.relatedattribute"/>
      </PACKAGE>

      <xsl:if test="count(EvidenceEntity/EntityLayer/Override[@relatedAttribute='Yes'])&gt;0">
      <PACKAGE NAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::RelatedEntityAttributes" QUID="Custom::{$product}::Evidence::RelatedEntityAttributes">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$customModelEvidenceCodePath}.relatedattribute"/>
      </PACKAGE>
      </xsl:if>

      <PACKAGE NAME="Logical View::MetaModel::Curam::{$product}::Evidence::Customise" QUID="{$product}::Evidence::Customise">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$modelEvidenceCodePath}.customise"/>
      </PACKAGE>

      <xsl:if test="count(EvidenceEntity/EntityLayer/Override[@customize='Yes'])&gt;0">
      <PACKAGE NAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Customise" QUID="Custom::{$product}::Evidence::Customise">
        <PACKAGE.OPTION NAME="CODE_PACKAGE" VALUE="{$customModelEvidenceCodePath}.customise"/>
      </PACKAGE>
      </xsl:if>
      <!-- END, CR00100405 -->
    </xsl:template>

    <xsl:template name="buildServiceLayerValidationClass">

      <xsl:param name="EntityName"/>
      <xsl:param name="prefix"/>
      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>
      <!-- BEGIN, CR00100405, CD -->
      <xsl:variable name="methods">
        <!-- Add the operations -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="preModifyValidate">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}Dtls" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="preInsertValidate">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}Dtls" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Protected" TYPE=""
          STEREOTYPE="" QUID="" NAME="validateDetails">
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}Dtls" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE=""
          STEREOTYPE="" QUID="" NAME="validate">
          <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.ValidateMode" STEREOTYPE="" NAME="mode" MODE=""/>
          <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
        </CLASS.OPERATION>
      </xsl:variable>
      <xsl:variable name="footer">
      <!-- Class Footer -->
        <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
      </xsl:variable>


      <!-- Class Header -->
      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::Validation::Validate{$EntityName}"
        PARENT_QUID="{$product}::Evidence::Validation"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Validation::Validate{$EntityName}"
        NAME="Validate{$EntityName}">
      <xsl:copy-of select="$methods"/>
      <xsl:copy-of select="$footer"/>
      </CLASS>

      <xsl:if test="EntityLayer/Override/@validation='Yes'">

      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="Custom::{$product}::Evidence::Validation::Validate{$EntityName}"
        PARENT_QUID="Custom::{$product}::Evidence::Validation"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::Validation::Validate{$EntityName}"
        NAME="Validate{$EntityName}">
        <xsl:copy-of select="$methods"/>
        <CLASS.OPTION NAME="REPLACE_SUPERCLASS" VALUE="yes"/>
        <CLASS.INHERIT
          NAME="Validate{$EntityName}"
          QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Validation::Validate{$EntityName}" QUID="{$product}::Evidence::Validation::Validate{$EntityName}"
        />
        <xsl:copy-of select="$footer"/>
      </CLASS>
      </xsl:if>
      <!-- END, CR00100405 -->
    </xsl:template>

    <xsl:template name="buildRelatedEntityAttributesProcessClass">

      <xsl:param name="EntityName"/>
      <xsl:param name="prefix"/>
      <xsl:param name="modelEvidenceCodePath"/>
      <xsl:param name="product"/>
      <xsl:param name="preAssoc"/>
      <xsl:param name="multiplePreAssoc"/>
      <xsl:param name="exposeOperation"/>

      <xsl:variable name="keyStruct">
        <xsl:choose>
          <xsl:when test="$preAssoc='Yes' and $multiplePreAssoc='Yes'">core.sl.MultiplePreAssocCaseEvKey</xsl:when>
          <xsl:when test="$preAssoc='Yes'">core.sl.PreAssocCaseEvKey</xsl:when>
          <xsl:otherwise>core.sl.EvidenceCaseKey</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <!-- BEGIN, CR00100405, CD -->
      <xsl:variable name="methods">
        <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}RelatedEntityAttributesDetails"
          STEREOTYPE="" QUID="" NAME="getRelatedEntityAttributes">
          <CLASS.OPERATION.PARAMETER TYPE="{$keyStruct}" STEREOTYPE="" NAME="key" MODE=""/>
        </CLASS.OPERATION>
      </xsl:variable>
      <xsl:variable name="footer">
        <!-- Class Footer -->
        <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
      </xsl:variable>


      <!-- Class Header -->
      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes"
        PARENT_QUID="{$product}::Evidence::RelatedEntityAttributes"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes"
        NAME="{$EntityName}RelatedEntityAttributes">
      <xsl:copy-of select="$methods"/>
      <xsl:copy-of select="$footer"/>
      </CLASS>

      <xsl:if test="EntityLayer/Override/@relatedAttribute='Yes'">

      <CLASS COMPONENT="{$product}" STEREOTYPE="process" QUID="Custom::{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes"
        PARENT_QUID="Custom::{$product}::Evidence::RelatedEntityAttributes"
        QUALIFIEDNAME="Logical View::MetaModel::Curam::Custom::{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes"
        NAME="{$EntityName}RelatedEntityAttributes">
        <xsl:copy-of select="$methods"/>
        <CLASS.OPTION NAME="REPLACE_SUPERCLASS" VALUE="yes"/>
        <CLASS.INHERIT
          NAME="{$EntityName}RelatedEntityAttributes"
          QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes" QUID="{$product}::Evidence::RelatedEntityAttributes::{$EntityName}RelatedEntityAttributes"
        />
        <xsl:copy-of select="$footer"/>
      </CLASS>
      </xsl:if>
      <!-- END, CR00100405 -->
    </xsl:template>


</xsl:stylesheet>
