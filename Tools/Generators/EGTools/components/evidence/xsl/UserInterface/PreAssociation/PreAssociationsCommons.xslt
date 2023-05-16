<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2014. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.
    
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
    
    <!-- Global Variables -->
    <xsl:import href="../UICommon.xslt"/>
    
    <!-- 
        This template handles writing the cancel button for the pre association page
        
        @param entityName The name of the Entity being generated
    -->
    <xsl:template name="PreAssociations_selectPreAssociationsPage_cancelButton">
        
        <xsl:param name="entityName"/>
    
        <!-- The resolve script to call in case the cancel button is chosen -->
        <xsl:variable name="resolveListWorkspacePage"><xsl:value-of select="$prefix"/>_resolve<xsl:value-of select="$entityName"/><xsl:value-of select="$caseType"/>List</xsl:variable>
        
        <!-- the child level number for this entity, i.e. standalone(0), child(1), grandchild(2) etc --> 
        <xsl:variable name="childLevelNo"><xsl:call-template name="GetChildLevel">
            <xsl:with-param name="capName" select="$entityName"/>
        </xsl:call-template></xsl:variable>
        
        <ACTION_CONTROL
            IMAGE="CancelButton"
            LABEL="ActionControl.Label.Cancel"
            >
            <LINK PAGE_ID="{$resolveListWorkspacePage}">
                <CONNECT>
                    <SOURCE
                        NAME="PAGE"
                        PROPERTY="caseID"
                    />
                    <TARGET
                        NAME="PAGE"
                        PROPERTY="caseID"
                    />
                </CONNECT>
                <CONNECT>
                    <SOURCE
                        NAME="PAGE"
                        PROPERTY="contextDescription"
                    />
                    <TARGET
                        NAME="PAGE"
                        PROPERTY="contextDescription"
                    />
                </CONNECT>
                <xsl:if test="$childLevelNo>0">
                    <CONNECT>
                        <SOURCE
                            NAME="PAGE"
                            PROPERTY="parEvID"
                        />
                        <TARGET
                            NAME="PAGE"
                            PROPERTY="evidenceID"
                        />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE
                            NAME="PAGE"
                            PROPERTY="parEvType"
                        />
                        <TARGET
                            NAME="PAGE"
                            PROPERTY="evidenceType"
                        />
                    </CONNECT>
                </xsl:if>
                <xsl:if test="$childLevelNo>1">
                    <CONNECT>
                        <SOURCE
                            NAME="PAGE"
                            PROPERTY="grandParEvID"
                        />
                        <TARGET
                            NAME="PAGE"
                            PROPERTY="parEvID"
                        />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE
                            NAME="PAGE"
                            PROPERTY="grandParEvType"
                        />
                        <TARGET
                            NAME="PAGE"
                            PROPERTY="parEvType"
                        />
                    </CONNECT>
                </xsl:if>
            </LINK>
        </ACTION_CONTROL>
        
    </xsl:template>
</xsl:stylesheet>
