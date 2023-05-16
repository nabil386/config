<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2006, 2021. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan" xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="../UserInterface/Create/CreateUtilityTemplates.xslt"/>

    <!-- Main facade template, calls templates to build Facade Layer Inf  -->
    <xsl:template name="buildFacadeLayerInf">

        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>


        <!-- build Facade Struct first -->
        <xsl:call-template name="buildFacadeLayerStruct">

            <xsl:with-param select="$product" name="product"/>
            <xsl:with-param select="$prefix" name="prefix"/>

        </xsl:call-template>

        <!-- Class name -->

        <xsl:text>&#10;</xsl:text>
        <CLASS COMPONENT="{$product}" STEREOTYPE="process" PARENT_QUID="{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade::{$prefix}EvidenceMaintenance"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::{$prefix}EvidenceMaintenance"
            NAME="{$prefix}EvidenceMaintenance" >

            <!-- build common facade methods as these are common to all entities we don't call this in
                the entity loop -->

            <xsl:call-template name="buildFacadeHeader">

                <xsl:with-param select="$prefix" name="prefix"/>
                <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

            </xsl:call-template>

            <xsl:for-each select="EvidenceEntity">
            	<!-- BEGIN, 191675, JAY -->
            	<xsl:variable name="entityElement" select="."/>
    			<xsl:variable name="endDateWizardApplicable">
    				<xsl:call-template name="Utilities-Entity-EndDateWizardApplicable">
    					<xsl:with-param name="entityElement" select="$entityElement"/>
    				</xsl:call-template>
    			</xsl:variable>

                <xsl:call-template name="buildFacadeLayer">

					<xsl:with-param select="$endDateWizardApplicable" name="endDateWizardApplicable"/>
                    <xsl:with-param select="@name" name="capName"/>
                    <xsl:with-param select="$prefix" name="prefix"/>
                    <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
                    <xsl:with-param select="$product" name="product"/>

                </xsl:call-template>
				<!-- END, 191675, JAY -->
            </xsl:for-each>

        </CLASS>

    </xsl:template>

     <!-- Template calls all other facade related templates in a specific order,
          all output is in Curam.xml format -->
    <xsl:template name="buildFacadeLayer">

		<xsl:param name="endDateWizardApplicable"/>
        <xsl:param name="capName"/>
        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>
        <xsl:param name="product"/>

        <xsl:variable name="EntityName">
          <xsl:value-of select="$capName"/>
        </xsl:variable>

        <xsl:variable name="EntityNameVariable">
          <xsl:value-of select="$modelEvidenceCodePath"/>
          <xsl:value-of select="$capName"/>
        </xsl:variable>

        <xsl:call-template name="buildFacadeLayerGetDefaultCreateDetailsOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerCreateOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerReadOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <!-- BEGIN, CR00219910, CD -->
        <xsl:call-template name="buildFacadeLayerReadAsObjectOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>
        <!-- END, CR00219910 -->

        <xsl:call-template name="buildFacadeLayerModifyOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerIncomingAddToCaseOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerReadForIncomingModifyOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerIncomingModifyOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

         <!-- BEGIN, 191675, JAY -->
         <xsl:call-template name="buildFacadeLayerAutoEndDateOperation">

			<xsl:with-param select="$endDateWizardApplicable" name="endDateWizardApplicable"/>
            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$EntityNameVariable" name="EntityNameVariable"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>
        </xsl:call-template>
        <!-- END, 191675, JAY -->

        <xsl:call-template name="buildFacadeLayerRelatedAttributesOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>
            <xsl:with-param select="$modelEvidenceCodePath" name="modelEvidenceCodePath"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerCreateAssociationOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerAssociationOperation">

            <xsl:with-param select="$EntityName" name="EntityName"/>

        </xsl:call-template>

        <xsl:call-template name="buildFacadeLayerOptionalParticipantOperations">

            <xsl:with-param select="$EntityName" name="EntityName"/>

        </xsl:call-template>

    </xsl:template>

    <!-- Template to output create operation -->
    <xsl:template name="buildFacadeLayerCreateOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION VISIBILITY="Public"
            TYPE="core.sl.ReturnEvidenceDetails" STEREOTYPE="" QUID=""
            NAME="create{$EntityName}Evidence">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE="">

                <xsl:variable name="resultOfMandatoryFieldProcessing">
                  <xsl:call-template name="getMandatoryFields">
                    <xsl:with-param name="EntityName" select="$EntityName"/>
                  </xsl:call-template>
                </xsl:variable>
                <!-- BEGIN, CR00101870, POB -->
                <xsl:variable name="parentMandatoryFields">
                    <xsl:call-template name="getMandatoryParentFields">
                        <xsl:with-param name="EntityName" select="$EntityName"/>
                    </xsl:call-template>
                </xsl:variable>

                <!-- BEGIN, CR00099784, POB -->
                <CLASS.OPERATION.PARAMETER.OPTION NAME="MANDATORY_FIELDS">
                    <xsl:attribute name="VALUE">descriptor.receivedDate,<xsl:value-of select="$parentMandatoryFields"/><xsl:value-of select="$resultOfMandatoryFieldProcessing"/></xsl:attribute>
                </CLASS.OPERATION.PARAMETER.OPTION>
                 <!-- END, CR00099784 -->
                <!-- END, CR00101870 -->
            </CLASS.OPERATION.PARAMETER>
            <!-- BEGIN, CR00105312, POB -->
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for creating <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
            <!-- END, CR00105312 -->
        </CLASS.OPERATION>

    </xsl:template>

    <!-- Template to output get default create details operation -->
    <xsl:template name="buildFacadeLayerGetDefaultCreateDetailsOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION VISIBILITY="Public"
            TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" QUID=""
            NAME="getDefaultCreate{$EntityName}Details">
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for getting the default creation details for <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>

    </xsl:template>

    <!-- BEGIN, CR00101870, POB -->
    <!--
        Template to pass back a list of Mandatory Parent fields to be added to the mandatory
        fields list for the facade create method

        @param EntityName The Name of the Entity being processed
    -->
    <xsl:template name="getMandatoryParentFields">
        <xsl:param name="EntityName"/>
        <xsl:for-each select="/EvidenceEntities/EvidenceEntity[@name=$EntityName]/Relationships/MandatoryParents/Parent">
            <xsl:variable name="lcParentName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
<xsl:value-of select="$lcParentName"/>ParEvKey.evidenceID,</xsl:for-each>
    </xsl:template>
    <!-- END, CR00101870 -->

    <xsl:template name="getMandatoryFields">

      <xsl:param name="EntityName"/>

      <!-- Initialise variables to be passed into buildMandatoryAttributesLoop template -->
      <xsl:variable name="mandatoryFields"/>
      <xsl:variable name="commaLoopCounter">1</xsl:variable>

      <xsl:call-template name="buildMandatoryAttributesLoop">
          <xsl:with-param select="UserInterfaceLayer/Cluster/Field[not(@metatype) or (not(@fullCreateParticipant) or @fullCreateParticipant='No') or (@metatype!=$metatypeRelatedEntityAttribute and @metatype!=$metatypeCaseParticipantSearch and @metatype!=$metatypeEmployerCaseParticipant and @metatype!=$metatypeParentCaseParticipant)]" name="attributeData"/>
          <xsl:with-param name="mandatoryFields" select="$mandatoryFields"/>
          <xsl:with-param name="commaLoopCounter" select="1"/>
      </xsl:call-template>
      <xsl:for-each select="Relationships/Child[@coCreate='Yes']">
        <xsl:variable name="childName" select="@name"/>
        <xsl:call-template name="buildMandatoryAttributesLoop">
          <xsl:with-param select="/EvidenceEntities/EvidenceEntity[@name=$childName]/UserInterfaceLayer/Cluster/Field[not(@metatype) or (@metatype!=$metatypeRelatedEntityAttribute and @metatype!=$metatypeCaseParticipantSearch and @metatype!=$metatypeEmployerCaseParticipant and @metatype!=$metatypeParentCaseParticipant)]" name="attributeData"/>
              <xsl:with-param name="EntityNameVariable"><xsl:value-of select="translate(substring($childName, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring($childName, 2)"/>.</xsl:with-param>
              <xsl:with-param name="mandatoryFields" select="$mandatoryFields"/>
              <xsl:with-param name="commaLoopCounter" select="2"/>
        </xsl:call-template>
      </xsl:for-each>
    </xsl:template>

    <!-- Template to output read operation -->
    <xsl:template name="buildFacadeLayerReadOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION NAME="read{$EntityName}Evidence" QUID=""
            STEREOTYPE=""
            TYPE="{$modelEvidenceCodePath}.entity.Read{$EntityName}EvidenceDetails" VISIBILITY="Public">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for reading <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>

    </xsl:template>

    <!-- BEGIN, CR00219910, CD -->
    <!-- Template to output read business object operation -->
    <xsl:template name="buildFacadeLayerReadAsObjectOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION NAME="read{$EntityName}Object" QUID=""
            STEREOTYPE=""
            TYPE="{$modelEvidenceCodePath}.entity.Read{$EntityName}EvidenceDetails" VISIBILITY="Public">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.SuccessionID" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for reading <xsl:value-of select="$EntityName"/> Evidence as a Business Object. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>

    </xsl:template>
    <!-- END, CR00219910 -->

    <!-- Template to output modify operation -->
    <xsl:template name="buildFacadeLayerModifyOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="modify{$EntityName}Evidence" >
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

    <!-- Template to output Incoming Add To Case operation -->
    <xsl:template name="buildFacadeLayerIncomingAddToCaseOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="incomingAddToCase{$EntityName}Evidence" >
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

    <!-- Template to output read for incoming modify -->
    <xsl:template name="buildFacadeLayerReadForIncomingModifyOperation">
        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <CLASS.OPERATION NAME="read{$EntityName}EvidenceForIncomingModify" QUID=""
            STEREOTYPE=""
            TYPE="{$modelEvidenceCodePath}.entity.Read{$EntityName}EvidenceDetails" VISIBILITY="Public">
            <CLASS.OPERATION.PARAMETER TYPE="guidedchanges.facade.WizardKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for reading <xsl:value-of select="$EntityName"/> Evidence to be modifed by the incoming evidence screens. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
    </xsl:template>

    <!-- Template to output incoming modify -->
    <xsl:template name="buildFacadeLayerIncomingModifyOperation">
        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
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

    <!-- BEGIN, 191675, JAY -->
       <!-- Template to output modify operation -->
    <xsl:template name="buildFacadeLayerAutoEndDateOperation">

		<xsl:param name="endDateWizardApplicable"/>
        <xsl:param name="EntityName"/>
        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="modelEvidenceCodePath"/>

		<xsl:if test="$endDateWizardApplicable='true'">
		 <CLASS.OPERATION VISIBILITY="Public"
            TYPE="core.sl.ReturnAutoEndDateEvidenceDetails" STEREOTYPE="" QUID=""
            NAME="createAutoEndDate{$EntityName}Evidence">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}EvidenceDetails" STEREOTYPE="" NAME="dtls" MODE="">

                <xsl:variable name="resultOfMandatoryFieldProcessing">
                  <xsl:call-template name="getMandatoryFields">
                    <xsl:with-param name="EntityName" select="$EntityName"/>
                  </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="parentMandatoryFields">
                    <xsl:call-template name="getMandatoryParentFields">
                        <xsl:with-param name="EntityName" select="$EntityName"/>
                    </xsl:call-template>
                </xsl:variable>

                <CLASS.OPERATION.PARAMETER.OPTION NAME="MANDATORY_FIELDS">
                    <xsl:attribute name="VALUE">descriptor.receivedDate,<xsl:value-of select="$parentMandatoryFields"/><xsl:value-of select="$resultOfMandatoryFieldProcessing"/></xsl:attribute>
                </CLASS.OPERATION.PARAMETER.OPTION>
            </CLASS.OPERATION.PARAMETER>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for creating <xsl:value-of select="$EntityName"/> Evidence with Auto End Date Wizard operation. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>

        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.facade.infrastructure.AutoEndDateReturnEvidenceDetails"
             STEREOTYPE="" QUID="" NAME="autoEndDate{$EntityName}Evidence" >
            <CLASS.OPERATION.PARAMETER TYPE="core.facade.infrastructure.AutoEndDateEvidenceDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for end dating <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
		</xsl:if>
    </xsl:template>
    <!-- END, 191675, JAY -->

    <!-- Template to output readRelatedAttributes operation -->
    <xsl:template name="buildFacadeLayerRelatedAttributesOperation">

        <xsl:param name="EntityName"/>
        <xsl:param name="modelEvidenceCodePath"/>

      <xsl:variable name="keyStruct">
        <xsl:choose>
          <xsl:when test="Relationships/@preAssociation='Yes' and Relationships/@mulitplePreAssociationID='Yes'">core.sl.MultiplePreAssocCaseEvKey</xsl:when>
          <xsl:when test="Relationships/@preAssociation='Yes'">core.sl.PreAssocCaseEvKey</xsl:when>
          <xsl:otherwise>core.sl.EvidenceCaseKey</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

        <!-- check if Related Attributes need to be retrieved for the client -->
        <xsl:if test="Relationships/@exposeOperation='Yes'">

            <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.entity.{$EntityName}RelatedEntityAttributesDetails"
                 STEREOTYPE="" QUID="" NAME="get{$EntityName}RelatedEntityAttributes">
                  <CLASS.OPERATION.PARAMETER TYPE="{$keyStruct}" STEREOTYPE="" NAME="key" MODE=""/>
                <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for reading Related Entity Attributes of  <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
                </CLASS.OPERATION>

        </xsl:if>

    </xsl:template>

    <!-- Template to output readRelatedAttributes operation -->
    <xsl:template name="buildFacadeLayerCreateAssociationOperation">

        <xsl:param name="EntityName"/>

        <!-- check if Related Attributes need to be retrieved for the client -->
        <xsl:if test="Relationships/@association='Yes' and Relationships/Association[@to!='']">

            <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ReturnEvidenceDetails"
              STEREOTYPE="" QUID="" NAME="create{$EntityName}Association">
              <CLASS.OPERATION.PARAMETER TYPE="core.sl.AssociationDetails" STEREOTYPE="" NAME="dtls" MODE=""/>
                <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for creating an association to <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
            </CLASS.OPERATION>

        </xsl:if>

    </xsl:template>

    <!-- Template to output list<EntityName>WithoutAssociations operation -->
    <xsl:template name="buildFacadeLayerAssociationOperation">

        <xsl:param name="EntityName"/>

        <!-- check if more that 1 Associations exist,and displayInHierarchy is equal to Yes-->
        <xsl:if test="Relationships/@association='Yes' and count(Relationships/Association[@from!='' and @displayInHierarchy='Yes'])>0">

                <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ListEvidenceDetails"
                    STEREOTYPE="" QUID="" NAME="list{$EntityName}EvidenceWithoutAssociations">
                    <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceListKey" STEREOTYPE="" NAME="key" MODE=""/>
                    <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Method for listing records of <xsl:value-of select="$EntityName"/> Evidence without any associations. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
                </CLASS.OPERATION>

        </xsl:if>

    </xsl:template>

    <!-- Template to output get<EntityName><ParticipantShortName>OptionalParticipantDetails operation -->
    <xsl:template name="buildFacadeLayerOptionalParticipantOperations">

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
                  <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns details of optional participants for <xsl:value-of select="$EntityName"/> Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
              </CLASS.OPERATION>

         </xsl:for-each>

       </xsl:for-each>

    </xsl:template>

    <!-- Template called recursively, checks all attributes of entity to see if they are
         mandatory, if they are it appends them to the mandatoryFields variable and passes
         the new value back into the template until all attributes have been checked -->
    <xsl:template name="buildMandatoryAttributesLoop">

        <xsl:param name="EntityNameVariable"/>
        <xsl:param name="mandatoryFields"/>
        <xsl:param name="attributeData"/>
        <xsl:param name="commaLoopCounter"/>

        <xsl:variable name="currentNode" select="$attributeData[1]"/>

        <!-- BEGIN, 25/02/2008, CD -->
        <!-- BEGIN, CR00098722, CD -->
        <xsl:variable name="aggregation">
          <xsl:choose>
            <xsl:when test="$currentNode/@notOnEntity='Yes'"><xsl:value-of select="$EntityNameVariable"/>nonEvidenceDetails</xsl:when>
            <xsl:otherwise><xsl:value-of select="$EntityNameVariable"/>dtls</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <!-- END, CR00098722 -->
        <!-- END, 25/02/2008, CD -->

        <xsl:choose>
            <!-- Test if there are still attributes before proceeding -->
            <xsl:when test="$attributeData" >

                <!-- test if current attribute is mandatory, if it is need to append it to mandatory fields -->
                <xsl:choose>
                    <!-- If the loop counter is 1, then this is our first visit and therefore we do not prepend
                         a comma to the attribute value -->
                    <xsl:when test="$commaLoopCounter=1">

                        <xsl:choose>
                            <!-- Check that the current attribute is mandatory -->
                            <xsl:when test="$currentNode/@mandatory='Yes'">

                                <!-- No need to append to mandatory fields as this is first iteration, therefore no value
                                     currently present in mandatoryFields -->
                                <!-- BEGIN, 25/02/2008, CD -->
                                <xsl:variable name="columnName"><xsl:value-of select="$aggregation"/>.<xsl:value-of select="$currentNode/@columnName"/>
                                </xsl:variable>
                                <!-- END, 25/02/2008, CD -->
                                <xsl:call-template name="buildMandatoryAttributesLoop">

                                    <xsl:with-param select="$attributeData[position()!=1]" name="attributeData"/>
                                    <xsl:with-param name="EntityNameVariable" select="$EntityNameVariable"/>
                                    <xsl:with-param name="mandatoryFields" select="$columnName"/>
                                    <!-- First value has been added, increment counter so next field will have a
                                         comma prepended -->
                                    <xsl:with-param name="commaLoopCounter" select="$commaLoopCounter+1"/>

                                </xsl:call-template>

                            </xsl:when>

                            <xsl:otherwise>

                                <!-- Attribute is not mandatory, recursively call back into template passing next
                                     attribute in nodelist and mandatoryFields as is. Also do not increment commaLoopCounter
                                     as nothing has been added -->
                                <xsl:call-template name="buildMandatoryAttributesLoop">

                                    <xsl:with-param select="$attributeData[position()!=1]" name="attributeData"/>
                                    <xsl:with-param name="EntityNameVariable" select="$EntityNameVariable"/>
                                    <xsl:with-param name="mandatoryFields" select="$mandatoryFields"/>
                                    <xsl:with-param name="commaLoopCounter" select="$commaLoopCounter"/>

                                </xsl:call-template>

                            </xsl:otherwise>

                        </xsl:choose>

                    </xsl:when>

                    <xsl:otherwise>

                        <xsl:choose>
                            <!-- Check that the current attribute is mandatory -->
                            <xsl:when test="$currentNode/@mandatory='Yes'">

                                <!-- Append current attribute value to existing mandatory fields adding comma
                                     beforehand. -->
                                <!-- BEGIN, 25/02/2008, CD -->
                                <xsl:variable name="columnName"><xsl:value-of select="$mandatoryFields"/>, <xsl:value-of select="$aggregation"/>.<xsl:value-of select="$currentNode/@columnName"/></xsl:variable>
                                <!-- END, 25/02/2008, CD -->
                                <xsl:call-template name="buildMandatoryAttributesLoop">

                                    <xsl:with-param select="$attributeData[position()!=1]" name="attributeData"/>
                                    <xsl:with-param name="EntityNameVariable" select="$EntityNameVariable"/>
                                    <xsl:with-param name="mandatoryFields" select="$columnName"/>
                                    <xsl:with-param name="commaLoopCounter" select="$commaLoopCounter+1"/>

                                </xsl:call-template>

                            </xsl:when>

                            <xsl:otherwise>
                                <!-- Attribute is not mandatory, recursively call back into template passing next
                                    attribute in nodelist and mandatoryFields as is. Also do not increment commaLoopCounter
                                    as nothing has been added -->
                                <xsl:call-template name="buildMandatoryAttributesLoop">

                                    <xsl:with-param select="$attributeData[position()!=1]" name="attributeData"/>
                                    <xsl:with-param name="EntityNameVariable" select="$EntityNameVariable"/>
                                    <xsl:with-param name="mandatoryFields" select="$mandatoryFields"/>
                                    <xsl:with-param name="commaLoopCounter" select="$commaLoopCounter"/>

                                </xsl:call-template>

                            </xsl:otherwise>

                        </xsl:choose>

                    </xsl:otherwise>

                </xsl:choose>

            </xsl:when>
            <!-- iteration of attribute list finished -->
            <xsl:otherwise>

                <!-- Check if any mandatory attribute elements were found, if so output to file -->
                <xsl:if test="$mandatoryFields!=&apos;&apos;">

                    <xsl:value-of select="$mandatoryFields"/>

                </xsl:if>

            </xsl:otherwise>

        </xsl:choose>

    </xsl:template>

    <!-- Add common facade class methods -->
    <xsl:template name="buildFacadeHeader">

        <xsl:param name="prefix"/>
        <xsl:param name="modelEvidenceCodePath"/>

        <xsl:text>&#10;</xsl:text>
        <CLASS.OPERATION NAME="getEvidenceAndCaseFromSuccession" QUID=""
            STEREOTYPE=""
            TYPE="core.sl.EvidenceCaseKey" VISIBILITY="Public">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.SuccessionID" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns the evidence id, type and case id for a succession. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.ConcernRoleDetails"
            STEREOTYPE="" QUID="" NAME="getPrimaryCaseParticipantConcernRoleID">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.CaseIDKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns the Concern Role ID of the Primary Client. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetailsList"
            STEREOTYPE="" QUID="" NAME="listCaseParticipant">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.SearchCaseParticipantDetailsKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Searches for Case Participants registered on a given case. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
<CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetailsList"
            STEREOTYPE="" QUID="" NAME="listCaseParticipantWithoutDuplicates">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.SearchCaseParticipantDetailsKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Searches for Case Participants registered on a given case. This will return the participants that have a uniquie concernRoleID. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.facade.ReadEmploymentDetailsList"
            STEREOTYPE="" QUID="" NAME="listEmploymentsForCaseParticipant">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.entity.CaseParticipantRoleKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Reads Employments for a given Case Participant. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.facade.{$prefix}RelatedEmploymentDetails"
            STEREOTYPE="" QUID="" NAME="readRelatedCoreEmployment">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.facade.{$prefix}RelatedEmploymentKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Reads extra details for an Employment. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.CreateWizardEmploymentDetailsList"
            STEREOTYPE="" QUID="" NAME="listEmploymentsForCaseParticipantTypeList">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.SearchCaseParticipantDetailsKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Reads Employments for a given Case Participant type list. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
            STEREOTYPE="" QUID="" NAME="readNameByCaseParticipantRole">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.entity.CaseParticipantRoleKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.PARAMETER TYPE="core.CaseKey" STEREOTYPE="" NAME="caseKey" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns the participant name for a specific Case Participant Role. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
            STEREOTYPE="" QUID="" NAME="readCaseParticipant">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.SearchCaseParticipantDetailsKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns details of a specific Case Participant. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
            STEREOTYPE="" QUID="" NAME="readCaseParticipantByEvidenceID">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceCaseKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns the details of a Case Paricipant associated with a given piece of Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
            STEREOTYPE="" QUID="" NAME="{$facade_createPage_GetAssociatedCaseParticipantReadMethod}">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns details of a Case Participant associated with a specifc piece of Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.SearchCaseParticipantDetails"
            STEREOTYPE="" QUID="" NAME="{$facade_modifyViewPage_GetAssociatedCaseParticipantReadMethod}">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="key" MODE=""/>
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns details of a Case Participant associated with the parent of a specifc piece of Evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!-- DKenny this method should be a standardised method, not in generator.
            TODO check with Vinny
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.EvidenceContextDescriptionDetails"
            STEREOTYPE="" QUID="" NAME="getContextDescription">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.CaseIDKey" STEREOTYPE="" NAME="key" MODE=""/>
        </CLASS.OPERATION>-->
        <!-- CDuffy moved to Evidence facade
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.EvidenceSiteMapDetailsList"
            STEREOTYPE="" QUID="" NAME="readEvidenceSiteMapDetails">
            <CLASS.OPERATION.PARAMETER TYPE="core.CaseKey" STEREOTYPE="" NAME="key" MODE="" />
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.EvidenceTypeAndDescList"
            STEREOTYPE="" QUID="" NAME="listTopLevelEvidenceTypes">
            <CLASS.OPERATION.PARAMETER TYPE="core.CaseKey" STEREOTYPE="" NAME="key" MODE="" />
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Protected" TYPE="core.sl.infrastructure.EvidenceTypeDtlsList"
            STEREOTYPE="" QUID="" NAME="getCrossCaseTypeTopLevelEvidenceList"/>
        -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.facade.ReadEmploymentDetailsList"
            STEREOTYPE="" QUID="" NAME="listEmploymentByEvidenceKey">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of Employments for the Case Participant associated with the given evidence. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.EmploymentDetails"
            STEREOTYPE="" QUID="" NAME="readEmploymentByCaseParticipant">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.CaseParticipantEmployerConcernRoleKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Reads a given Employment for Case Participant. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.ECParentDtls"
            STEREOTYPE="" QUID="" NAME="getParentEvidenceList">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.CaseIDAndEvidenceTypeKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of Parent Evidence for the supplied Child. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.ECParentDtls"
            STEREOTYPE="" QUID="" NAME="getEvidenceListByType">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.CaseIDAndEvidenceTypeKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of evidence records of a given type. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.EvidenceTypeDtlsList"
            STEREOTYPE="" QUID="" NAME="getParentTypeList">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EvidenceTypeDtls" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of Parent Evidence types for a supplied child type. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!--
        <CLASS.OPERATION VISIBILITY="Public" TYPE="{$modelEvidenceCodePath}.facade.EvidenceTypeDetails"
            STEREOTYPE="" QUID="" NAME="getEvidenceType">
            <CLASS.OPERATION.PARAMETER TYPE="{$modelEvidenceCodePath}.facade.EvidenceTypeDetails" STEREOTYPE="" NAME="key" MODE="" />
        </CLASS.OPERATION>
        -->
        <!-- DKenny ignoring anything to do with ParticipantRelationships for the moment
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ParticipantRelSelectDetailsList"
             STEREOTYPE="" QUID="" NAME="listAllClaimParticipantAndDependentEvidenceByCaseID">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.CaseIDKey" STEREOTYPE="" NAME="key" MODE=""/>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ParticipantRelSelectDetailsList"
            STEREOTYPE="" QUID="" NAME="listParticipantRelationshipDetails">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceKey" STEREOTYPE="" NAME="key" MODE=""/>
            </CLASS.OPERATION> -->

        <!-- BEGIN, CR00100662, POB -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.ECParentDtls"
            STEREOTYPE="" QUID="" NAME="listAssociatedEvidence">
            <CLASS.OPERATION.PARAMETER TYPE="core.facade.infrastructure.EvidenceListKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of Associated Evidence for the given evidenceID, evidenceType and caseID supplied. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!-- END, CR00100662 -->
        <!-- BEGIN, CR00101875, POB -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.ParentEvidenceLink"
            STEREOTYPE="" QUID="" NAME="getParentEvidenceLink">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.EIEvidenceKey" STEREOTYPE="" NAME="eiEvidenceKey" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns display text for a UIM page link to a given evidence record]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!-- END, CR00101875 -->
        <!-- BEGIN, CR00114649, POB -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.EvidenceTypeAndDescriptionList"
            STEREOTYPE="" QUID="" NAME="getEvidenceTypeListForPreAssociation">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.EvidenceTypeKey" STEREOTYPE="" NAME="evidenceTypeKey" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of evidence types for PreAssociation with the specified type]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!-- END, CR00114649 -->
        <!-- BEGIN, CR00118883, POB -->
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.infrastructure.ECParentDtls"
            STEREOTYPE="" QUID="" NAME="getEvidenceListByTypeWithCPR">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.infrastructure.entity.CaseIDAndEvidenceTypeKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of evidence records of a given type switching the participantID for a valid caseParticipantID. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <CLASS.OPERATION VISIBILITY="Public" TYPE="core.sl.CaseParticipantIDNameAndDOBDetailsList"
            STEREOTYPE="" QUID="" NAME="listCaseParticipantsExtraDetails">
            <CLASS.OPERATION.PARAMETER TYPE="core.sl.SearchCaseParticipantDetailsKey" STEREOTYPE="" NAME="key" MODE="" />
            <CLASS.OPERATION.DOCUMENTATION><xsl:text disable-output-escaping="yes">&lt;</xsl:text>![CDATA[Returns a list of case participants with extra details for each. This method was generated by the Evidence Generator]]<xsl:text disable-output-escaping="yes">&gt;</xsl:text></CLASS.OPERATION.DOCUMENTATION>
        </CLASS.OPERATION>
        <!-- END, CR00118883 -->
    </xsl:template>

    <!-- Read<Product>SiteMapDetails Struct, return struct containing SiteMap details for this struct -->
    <xsl:template name="buildFacadeLayerStruct">

        <xsl:param name="prefix"/>
        <xsl:param name="product"/>

        <xsl:text>&#10;</xsl:text>

        <CLASS COMPONENT="{$product}" STEREOTYPE="struct" PARENT_QUID="{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade::{$prefix}RelatedEmploymentKey"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::{$prefix}RelatedEmploymentKey"
            NAME="{$prefix}RelatedEmploymentKey">
            <CLASS.ATTRIBUTE TYPE="CASE_ID" STEREOTYPE="" QUID="" NAME="caseID" />
            <CLASS.ATTRIBUTE TYPE="EMPLOYMENT_ID" STEREOTYPE="" QUID="" NAME="employmentID" />
            <CLASS.ATTRIBUTE TYPE="CODETABLE_LIST" STEREOTYPE="" QUID="" NAME="caseParticipantRoleTypeList" />
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        <CLASS COMPONENT="{$product}" STEREOTYPE="struct" PARENT_QUID="{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade::{$prefix}RelatedEmploymentDetails"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::{$prefix}RelatedEmploymentDetails"
            NAME="{$prefix}RelatedEmploymentDetails">
            <CLASS.ATTRIBUTE TYPE="CASE_ID" STEREOTYPE="" QUID="" NAME="caseID" />
            <CLASS.ATTRIBUTE TYPE="EMPLOYMENT_ID" STEREOTYPE="" QUID="" NAME="employmentID" />
            <CLASS.ATTRIBUTE TYPE="CONCERN_ROLE_ID" STEREOTYPE="" QUID="" NAME="employerConcernRoleID" />
            <CLASS.ATTRIBUTE TYPE="CASE_PARTICIPANT_ROLE_ID" STEREOTYPE="" QUID="" NAME="caseParticipantRoleID" />
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        <!--
        <CLASS STEREOTYPE="struct" PARENT_QUID="{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade::EvidenceTypeDetails"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::EvidenceTypeDetails"
            NAME="EvidenceTypeDetails">
            <CLASS.ATTRIBUTE NAME="evidenceType" QUID=""
                STEREOTYPE="" TYPE="EVIDENCE_TYPE_CODE"/>
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        -->
        <!--
        <CLASS STEREOTYPE="struct" PARENT_QUID="{$product}::Evidence::Facade" QUID="{$product}::Evidence::Facade::ReadSiteMapDetails"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::ReadSiteMapDetails"
            NAME="ReadSiteMapDetails">
            <CLASS.ATTRIBUTE TYPE="CONTEXT_DESCRIPTION" STEREOTYPE="" QUID=""
                NAME="contextDescription" />
            <CLASS.ATTRIBUTE TYPE="XML_MENU_DATA" STEREOTYPE="" QUID="" NAME="menuData" />
            <CLASS.ATTRIBUTE TYPE="CURAM_INDICATOR_ACTIVE" STEREOTYPE="" QUID="" NAME="active"/>
            <CLASS.ATTRIBUTE TYPE="CURAM_INDICATOR_INEDIT" STEREOTYPE="" QUID="" NAME="inEdit"/>
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        -->
        <!--
        <CLASS NAME="ReadSiteMapItemDetails" PARENT_QUID="{$product}::Evidence::Facade"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::ReadSiteMapItemDetails"
            QUID="{$product}::Evidence::Facade::ReadSiteMapItemDetails" STEREOTYPE="struct">
            <CLASS.ATTRIBUTE NAME="evidenceType" QUID=""
                STEREOTYPE="" TYPE="EVIDENCE_TYPE_CODE"/>
            <CLASS.ATTRIBUTE NAME="evidenceLinkText" QUID=""
                STEREOTYPE="" TYPE="SUBJECT_TEXT"/>
            <CLASS.ATTRIBUTE NAME="active" QUID="" STEREOTYPE="" TYPE="CURAM_INDICATOR_ACTIVE"/>
            <CLASS.ATTRIBUTE NAME="inEdit" QUID="" STEREOTYPE="" TYPE="CURAM_INDICATOR_INEDIT"/>
            <CLASS.ASSOCIATION NAME="" QUID="" STEREOTYPE="">
                <CLASS.ASSOCIATION.ROLE AGGREGATE="false" CARDINALITY="0..n"
                    CLASS="ReadSiteMapItemDetails" CONTAINMENT="ByValue"
                    NAME="itemDtls" NAVIGABLE="true"
                    QUALIFIEDCLASS="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::ReadSiteMapItemDetails"
                    QUID="" QUIDU="" STEREOTYPE=""/>
                <CLASS.ASSOCIATION.ROLE AGGREGATE="true" CARDINALITY="1"
                    CLASS="ReadSiteMapDetails" CONTAINMENT="ByValue" NAME=""
                    NAVIGABLE="true"
                    QUALIFIEDCLASS="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::ReadSiteMapDetails"
                    QUID="" QUIDU="" STEREOTYPE=""/>
                <CLASS.ASSOCIATION.ORIGINALFILE NAME=""/>
            </CLASS.ASSOCIATION>
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        <CLASS NAME="ReadSiteMapDetails" PARENT_QUID="{$product}::Evidence::Facade"
            QUALIFIEDNAME="Logical View::MetaModel::Curam::{$product}::Evidence::Facade::ReadSiteMapDetails"
            QUID="{$product}::Evidence::Facade::ReadSiteMapDetails" STEREOTYPE="struct">
            <CLASS.ATTRIBUTE NAME="contextDescription" QUID=""
              STEREOTYPE="" TYPE="CONTEXT_DESCRIPTION"/>
            <CLASS.ATTRIBUTE NAME="menuData" QUID="" STEREOTYPE="" TYPE="XML_MENU_DATA"/>
            <CLASS.ORIGINALFILE NAME="Insert metadata file location here"/>
        </CLASS>
        -->

    </xsl:template>

</xsl:stylesheet>