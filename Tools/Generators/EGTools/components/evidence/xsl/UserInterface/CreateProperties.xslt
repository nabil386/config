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
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0"
  xmlns:dyn="http://exslt.org/dynamic"
  extension-element-prefixes="dyn"
  xmlns:xalan="http://xml.apache.org/xslt"
>
  <xsl:output method="xml" omit-xml-declaration="yes"/>
  
  <!--
      Template to get the localized display name of an entity via it's associated Label nodes
  -->
  <xsl:template name="getEntityLabelForPropertiesFile">
    <xsl:param name="locale"/>      
    <xsl:param name="evidenceNode"/>    
	<xsl:choose>
	  <xsl:when test="$evidenceNode/Label[$locale=@locale]"><xsl:value-of select="$evidenceNode/Label[$locale=@locale]/@value"/></xsl:when>
	  <xsl:otherwise><xsl:value-of select="$evidenceNode/@displayName"/></xsl:otherwise>
	</xsl:choose>    
  </xsl:template>
  
    <!--<xsl:include href="UICommon.xslt"/>-->
    <!-- 
        Main template thats called from other xslts. Opens propertyMappings.xml, initialises counter
        for recursion counts. Also checks if a runtime metatype value swap is to occur and bypasses
        the templates that do this if negative (i.e. property has a static value) proceeding directly 
        to the help property generation.
        
        Parameter    propertyNode         Nodeset containing property value and it's associated Help attribute
                                          if present. 
        Parameter    evidenceNode         EvidenceEntities node that contains the metatype value that will
                                          replace the occurence of the corressponding placeholder. Empty if 
                                          property has static value.                 
    --> 
              
  <xsl:template name="generateProperty">
    <xsl:param name="propertyNode"/>
    <xsl:param name="altPropertyName"/>
    <xsl:param name="evidenceNode"/>
    <xsl:param name="propertyMappings"/>
        <!--<xsl:variable name="file">propertyMappings.xml</xsl:variable>
        <xsl:variable name="propertyMappings" select="document($file)/mappings"/>-->     
		        
        <xsl:choose>
            <!-- 
                If evidenceNode not empty, it means we need to map and replace a placeholder with 
                a metadata value from the evidenceNode.
             -->
            <xsl:when test="$evidenceNode">
                
                <xsl:variable name="counter">1</xsl:variable>    
                <xsl:variable name="propertyValue">
                    <xsl:value-of select="$propertyNode/@value"/>
                    <!-- 
                        If help node present add to string to be transformed as there maybe
                        placeholders here also. Insert | as delimiter between the 2 values.
                        NOTE: may need to change this to a more proprietary symbol to ensure
                        it's not something present in a property value entered by a user.
                    -->
                    <xsl:if test="$propertyNode/Help/@value!=&apos;&apos;">|<xsl:value-of select="$propertyNode/Help/@value"/>                       
                    </xsl:if>
                </xsl:variable>     
                    <xsl:call-template name="resolveMetaType">
                        <xsl:with-param name="stringToTransform" select="$propertyValue"/>
                        <xsl:with-param name="propertyNode" select="$propertyNode"/>
                        <xsl:with-param name="counter" select="$counter"/>
                        <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                        <xsl:with-param name="placeholders" select="$propertyMappings/placeholder"/>
                        <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
                    </xsl:call-template>
                
            </xsl:when>
            
            <!-- This is a standard (static) property, retrieve from property node -->
            <xsl:otherwise>
                
                <xsl:call-template name="addProperty">
                    <xsl:with-param name="propertyNode" select="$propertyNode"/> 
                    <xsl:with-param name="altPropertyName" select="$altPropertyName"/>                    
                    <xsl:with-param name="transformedPropertyValue" select="&apos;&apos;"/>                                                        
                </xsl:call-template>
                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
   
    <!-- 
        Template that resolves a placeholder value with the actual metatype value. Will
        resolve multiple placeholder values that occur in string.
        
        Parameter    propertyNode         Nodeset containing property value and it's associated Help attribute
                                          if present. 
        Parameter    stringToTransform    Property value string to search for occurences of placeholder value 
                                          that will be replaced. This string is retrieved from the properties
                                          values in the EvidenceEntities.xml.
        Parameter    counter              Integer variable incremented on each recursion of template.
                                          Used to calculate which node of placeholder nodeset template
                                          is now working with.
        Parameter    evidenceNode         EvidenceEntities node that contains the metatype value that will
                                          replace the occurence of the corresponding placeholder.
        Parameter    placeholders         Nodeset of placeholder values, obtained from an external xml file.         
    --> 
    <xsl:template name="resolveMetaType">
        <xsl:param name="stringToTransform"/>
        <xsl:param name="counter"/>
        <xsl:param name="evidenceNode"/>
        <xsl:param name="placeholders"/>
        <xsl:param name="propertyNode"/>
        <xsl:param name="altPropertyName"/>
        
        
        <xsl:variable name="currentValue" select="$placeholders/@value"/>				
        <xsl:variable name="locale" select="$propertyNode/../../@locale"/>
        
        <xsl:choose>
            
        <xsl:when test="$placeholders">
        
         <!-- 
             Placeholder used for displayName metatype, retrieve displayable name of Entity.
         --> 
         <xsl:if test="($counter=1 and contains($stringToTransform,$currentValue) )">  

            <xsl:variable name="finalMetaType">
                <xsl:call-template name="getEntityLabelForPropertiesFile">
					<xsl:with-param name="locale" select="$locale"/>
					<xsl:with-param name="evidenceNode" select="$evidenceNode"/>
                </xsl:call-template>
            </xsl:variable>
            
             <xsl:variable name="transformedString">
                 
                <xsl:call-template name="replaceAll">
                    <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                    <xsl:with-param name="target" select="$currentValue"/>
                    <xsl:with-param name="replacement" select="$finalMetaType"/>
                </xsl:call-template>
            
            </xsl:variable>
            
             <xsl:call-template name="resolveMetaType">
                 <xsl:with-param name="stringToTransform" select="$transformedString"/>
                 <xsl:with-param name="propertyNode" select="$propertyNode"/>
                 <xsl:with-param name="counter" select="$counter+1"/>
                 <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                 <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
             </xsl:call-template>
             
          </xsl:if>
        
          <!-- 
            Placeholder used for name metatype, retrieve name of Entity. Note: may
            also be used further down the nodetree for a different name metatype, 
            i.e. CreateRelatedCaseParticipantUI/name
          --> 
         <xsl:if test="($counter=2 and contains($stringToTransform,$currentValue) )">  
            
            <xsl:variable name="finalMetaType">
                <xsl:value-of select="$evidenceNode/@name"/>
            </xsl:variable>
            
             <xsl:variable name="transformedString">
                 
                 <xsl:call-template name="replaceAll">
                     <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                     <xsl:with-param name="target" select="$currentValue"/>
                     <xsl:with-param name="replacement" select="$finalMetaType"/>
                 </xsl:call-template>
                 
             </xsl:variable>
             
             <xsl:call-template name="resolveMetaType">
                 <xsl:with-param name="stringToTransform" select="$transformedString"/>
                 <xsl:with-param name="propertyNode" select="$propertyNode"/>
                 <xsl:with-param name="counter" select="$counter+1"/>
                 <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                 <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
             </xsl:call-template>
             
          </xsl:if>
          <!-- 
            Placeholder used for from metatype, retrieve Entity name of current entity is
            associated from.
          --> 
         <xsl:if test="($counter=3 and contains($stringToTransform,$currentValue) )">  
           
            <xsl:variable name="finalMetaType">
                <xsl:value-of select="$evidenceNode/@from"/>
            </xsl:variable>
            
             <xsl:variable name="transformedString">
                 
                 <xsl:call-template name="replaceAll">
                     <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                     <xsl:with-param name="target" select="$currentValue"/>
                     <xsl:with-param name="replacement" select="$finalMetaType"/>
                 </xsl:call-template>
                 
             </xsl:variable>
             
             <xsl:call-template name="resolveMetaType">
                 <xsl:with-param name="stringToTransform" select="$transformedString"/>
                 <xsl:with-param name="propertyNode" select="$propertyNode"/>
                 <xsl:with-param name="counter" select="$counter+1"/>
                 <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                 <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
             </xsl:call-template>
             
          </xsl:if>
        
          <!-- 
            Placeholder used for to metatype, retrieve Entity name of current entity is
            associated to.
          -->
         <xsl:if test="($counter=4 and contains($stringToTransform,$currentValue) )">            
            
            <xsl:variable name="finalMetaType">
                <xsl:value-of select="$evidenceNode/@to"/>
            </xsl:variable>
            
             <xsl:variable name="transformedString">
                 
                 <xsl:call-template name="replaceAll">
                     <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                     <xsl:with-param name="target" select="$currentValue"/>
                     <xsl:with-param name="replacement" select="$finalMetaType"/>
                 </xsl:call-template>
                 
             </xsl:variable>
             
             <xsl:call-template name="resolveMetaType">
                 <xsl:with-param name="stringToTransform" select="$transformedString"/>
                 <xsl:with-param name="propertyNode" select="$propertyNode"/>
                 <xsl:with-param name="counter" select="$counter+1"/>
                 <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                 <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
             </xsl:call-template>
             
          </xsl:if>
          <!-- 
            Placeholder used for ampersand, need to disable-output-escaping to prevent
            ampersand escape characters being displayed. Note: may not need this now
            as disable-output-escaping is carried out in addProperty before returning
            value. Need to have this in both dynamic and static propery conditional
            returns to test.            
          -->
         <xsl:if test="($counter=5 and contains($stringToTransform,$currentValue) )"> 
             
             <xsl:variable name="disabledOutputValue">
                 <xsl:value-of select="$stringToTransform" disable-output-escaping="yes"/>
             </xsl:variable>
             
             <xsl:call-template name="addProperty">
                 <xsl:with-param name="propertyNode" select="$propertyNode"/> 
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>                     
                 <xsl:with-param name="transformedPropertyValue" select="$stringToTransform"/>                                                        
             </xsl:call-template>
             
            <!-- <xsl:value-of select="$stringToTransform" disable-output-escaping="yes"/> -->
         </xsl:if>
            
            <!-- 
                Placeholder used for to metatype, retrieve Entity name of current entity is
                related to.
            -->
            <xsl:if test="($counter=6 and contains($stringToTransform,$currentValue) )">            
                
                <xsl:variable name="finalMetaType">
                    <xsl:value-of select="$evidenceNode/@relatedRelationshipType"/>
                </xsl:variable>
                
                <xsl:variable name="transformedString">
                    
                    <xsl:call-template name="replaceAll">
                        <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                        <xsl:with-param name="target" select="$currentValue"/>
                        <xsl:with-param name="replacement" select="$finalMetaType"/>
                    </xsl:call-template>
                    
                </xsl:variable>
                
                <xsl:call-template name="resolveMetaType">
                    <xsl:with-param name="stringToTransform" select="$transformedString"/>
                    <xsl:with-param name="propertyNode" select="$propertyNode"/>
                    <xsl:with-param name="counter" select="$counter+1"/>
                    <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                    <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                    <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
                </xsl:call-template>
                
            </xsl:if>
        
         <xsl:if test="not(contains($stringToTransform,$currentValue))"> 
             
             <xsl:call-template name="resolveMetaType">
                 <xsl:with-param name="stringToTransform" select="$stringToTransform"/>
                 <xsl:with-param name="propertyNode" select="$propertyNode"/>
                 <xsl:with-param name="counter" select="$counter+1"/>
                 <xsl:with-param name="evidenceNode" select="$evidenceNode"/>                
                 <xsl:with-param name="placeholders" select="$placeholders[position() > 1]"/>
                 <xsl:with-param name="altPropertyName" select="$altPropertyName"/>  
             </xsl:call-template>
             
         </xsl:if>
         
        </xsl:when> 
            <!-- All placeholders in property value have been replaced by metadata values -->
            <xsl:otherwise>
            
                <xsl:call-template name="addProperty">
                    <xsl:with-param name="propertyNode" select="$propertyNode"/> 
                    <xsl:with-param name="altPropertyName" select="$altPropertyName"/>                     
                    <xsl:with-param name="transformedPropertyValue" select="$stringToTransform"/>                                                        
                </xsl:call-template>
                
                <!--<xsl:value-of select="$stringToTransform"/>-->
            </xsl:otherwise>
                     
        </xsl:choose>
        
    </xsl:template>
    
    <!-- 
        Template that replaces all occurences of a substring value within
        a string with a replacement string.
        
        Parameter    stringToTransform    String to search for occurences of substring that will 
                                          be replaced.
        Parameter    target               Substring to search for in stringToTransform.
        Parameter    replacement          String that replaces target.
    --> 
    <xsl:template name="replaceAll">
        <xsl:param name="stringToTransform"/>
        <xsl:param name="target"/>
        <xsl:param name="replacement"/>
        
        <xsl:choose>
            
            <xsl:when test="contains($stringToTransform,$target)">                   
                <xsl:value-of select=
                    "concat(substring-before($stringToTransform,$target),
                    $replacement)"/>
                <xsl:call-template name="replaceAll">
                    <xsl:with-param name="stringToTransform" 
                        select="substring-after($stringToTransform,$target)"/>
                    <xsl:with-param name="target" select="$target"/>
                    <xsl:with-param name="replacement" 
                        select="$replacement"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$stringToTransform"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- 
        Template that adds properties for corresponding property that has been generated.
        
        Parameter    propertyNode              Nodeset containing property value and it's associated Help 
                                               attribute if present. 
        Parameter    transformedPropertyValue  String containing transformed property value if there was 
                                               placeholders present.        
    --> 
    <xsl:template name="addProperty">
        <xsl:param name="propertyNode"/>
        <xsl:param name="altPropertyName"/>
        <xsl:param name="transformedPropertyValue"/>        
        
        <xsl:variable name="page.title">Page.Title</xsl:variable>
        <xsl:variable name="help.description">Help.PageDescription</xsl:variable>
        
        <xsl:choose>
            <!--
                Check if a transformed property value is present. If so use this in the generated
                property.
            -->    
            <xsl:when test="$transformedPropertyValue!=&apos;&apos;">
                
                <xsl:variable name="propertyTag">
                  <xsl:choose>
                    <xsl:when test="$altPropertyName!=''">
                      <xsl:value-of select="$altPropertyName"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="name($propertyNode)"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:variable>
		
				<xsl:variable name="delimiter">|</xsl:variable>
				
				<xsl:choose>
					<!--
						If transformed value contains a | it means as well as a property 
						value there is also a transformed help value present in the string.
						This needs to be split into the 2 respective values.
					-->
					<xsl:when test="contains($transformedPropertyValue,$delimiter)">   
					   
						<xsl:variable name="propertyValue">
							<xsl:value-of select="$propertyTag"/>=<xsl:value-of select="substring-before($transformedPropertyValue,$delimiter)"/>
							<xsl:text>&#10;</xsl:text>
						</xsl:variable>

						<xsl:variable name="transformedHelpValue">
						  <xsl:value-of select="substring-after($transformedPropertyValue,$delimiter)"/>
						</xsl:variable>								
						
<xsl:value-of select="$propertyValue" disable-output-escaping="yes"/>
						
						<xsl:if 
						  test="not(contains($propertyTag,$page.title)) and 
								not(contains($propertyTag,$help.description))">
						<xsl:variable name="helpValue"><xsl:call-template name="appendHelp">                                        
							<xsl:with-param name="helpIdentifier" select="$propertyNode"/>    
							<xsl:with-param name="helpTag" select="concat($propertyTag,'.Help')"/>                  
							<xsl:with-param name="transformedHelpValue" select="$transformedHelpValue"/>                                        
						</xsl:call-template></xsl:variable>								
<xsl:value-of select="$helpValue" disable-output-escaping="yes"/>
						</xsl:if>
					   
					</xsl:when>
					
					<!-- 
					  No transformed help value is present so we can simply retrieve the help
					  value present in the Help node of this property.
					-->
					<xsl:otherwise>
					 
						<xsl:variable name="propertyValue"><xsl:value-of select="$propertyTag"/>=<xsl:value-of select="$transformedPropertyValue"/></xsl:variable>
						
<xsl:value-of select="$propertyValue" disable-output-escaping="yes"/>
<xsl:text>&#10;</xsl:text>
						
						<xsl:if 
						  test="not(contains($propertyTag,$page.title)) and 
								not(contains($propertyTag,$help.description))">
						<xsl:variable name="helpValue"><xsl:call-template name="appendHelp">
							<xsl:with-param name="helpIdentifier" select="$propertyNode"/>   
							<xsl:with-param name="helpTag" select="concat($propertyTag,'.Help')"/>  
						</xsl:call-template></xsl:variable>								
<xsl:value-of select="$helpValue" disable-output-escaping="yes"/>
						</xsl:if>
						   
					</xsl:otherwise>
				</xsl:choose>
                        
                                
            </xsl:when>
            <!--
                No transformed property value is present. This means the value in the property node is
                a static value and as such can be used with no additional processing on it other than 
                disable-output-escaping="yes".
            -->
            <xsl:otherwise>
                
                <xsl:variable name="propertyTag">
                    <xsl:choose>
                        <xsl:when test="$altPropertyName!=''">
                            <xsl:value-of select="$altPropertyName"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="name($propertyNode)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                
                     
				<xsl:variable name="propertyValue"><xsl:value-of select="$propertyTag"/>=<xsl:value-of select="$propertyNode/@value"/></xsl:variable>
                
<xsl:value-of select="$propertyValue" disable-output-escaping="yes"/>
<xsl:text>&#10;</xsl:text>
	
				<xsl:if 
				  test="not(contains($propertyTag,$page.title)) and 
						not(contains($propertyTag,$help.description))">
					<xsl:variable name="helpValue"><xsl:call-template name="appendHelp">
						<xsl:with-param name="helpIdentifier" select="$propertyNode"/>  
						<xsl:with-param name="helpTag" select="concat($propertyTag,'.Help')"/>                                          
					</xsl:call-template></xsl:variable>								
<xsl:value-of select="$helpValue" disable-output-escaping="yes"/>
				</xsl:if>
                 
           
            </xsl:otherwise>
         
        </xsl:choose>
        
        
    </xsl:template>
    
    <!-- 
        Template that adds help properties for corressponding property that has been generated.
        
        Parameter    helpIdentifier    String used to infer and retrieve help property.        
    --> 
    <xsl:template name="appendHelp">
        <xsl:param name="helpIdentifier"/>
        <xsl:param name="helpTag"/>
        <xsl:param name="transformedHelpValue"/>
        
        <xsl:if test="$helpIdentifier/Help">    
        
        <xsl:choose>
          
          <xsl:when test="$transformedHelpValue!=&apos;&apos;">
          
            <xsl:variable name="helpValue"><xsl:value-of select="$helpTag"/>=<xsl:value-of select="$transformedHelpValue"/></xsl:variable>        
          
            <xsl:variable name="finalValue">                  
              <xsl:value-of select="$helpValue"/>
              <xsl:text>&#10;</xsl:text>
            </xsl:variable>
            <xsl:value-of select="$finalValue"/>     
          </xsl:when>
        
          <xsl:otherwise>
            <xsl:variable name="helpValue"><xsl:value-of select="$helpTag"/>=<xsl:value-of select="$helpIdentifier/Help/@value"/></xsl:variable>        
         
            <xsl:variable name="finalValue">                  
                <xsl:value-of select="$helpValue"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:variable>
        
            <xsl:value-of select="$finalValue"/> 
          </xsl:otherwise>
        </xsl:choose>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>