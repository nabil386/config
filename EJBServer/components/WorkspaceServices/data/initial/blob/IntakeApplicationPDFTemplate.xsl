<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2021. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">
  <xsl:variable name="fo:layout-master-set">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
        <fo:region-body margin-bottom="0.79in" margin-top="1.8in"/>
        <fo:region-before extent="1.5in" display-align="after"/>
      </fo:simple-page-master>
    </fo:layout-master-set>
  </xsl:variable>
  
    <!-- global parameter and variable used when indenting. -->
  <xsl:param name="level-min" select="1"/>  
  <xsl:variable name="workingPageWidth" select="7"/>

  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA"/>
  </xsl:template>
  
  <xsl:template match="DATA">
    <!--Explicitly select the entity to avoid processing anything else.-->
    <fo:root>
      <xsl:copy-of select="$fo:layout-master-set"/>
      <!-- SUMMARY-PDF declaration and bookmark -->
      <xsl:copy-of select="$fo:declarations"/>
      <xsl:copy-of select="$fo:bookmark-tree"/>

      <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
        <fo:title><xsl:value-of select="//entity[@name='Application']/@description"/></fo:title>

        <!-- Header for customization -->
        <fo:static-content flow-name="xsl-region-before" font-family="IBMPlexSans" font-size="8pt">
          <xsl:call-template name="header" />
          <fo:block>
            <fo:leader leader-pattern="space"/>
          </fo:block>

          <fo:block padding-left="6pt" padding-right="6pt" background-color="{$bg-color-main}" font-size="1pt">
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
          </fo:block>
        </fo:static-content>

        <!-- Main content area -->
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:block font-family="IBMPlexSans" space-before.optimum="1pt" space-after.optimum="2pt">
              <!-- Start content -->
               <fo:block font-size="28px"  line-height="37px" space-before="20px" space-after="10px" role="{$header-role-page}">
              	<xsl:value-of select="//entity[@name='Application']/@description"/>
              </fo:block>
               <!-- SUMMARY-PDF  Evaluates if the script contains a summary page -->
              <xsl:if test="$isSummaryPagePresent"> 
                <!-- SUMMARY-PDF If yes, generate new  summary pdf template -->
                <xsl:apply-templates select="summary-page//cluster | summary-page//list | summary-page//relationship-summary-list" />
              </xsl:if>
              <xsl:if test="not($isSummaryPagePresent)"> 
               <!-- SUMMARY-PDF If not, generate previous PDF, processing only the entities -->
                  <xsl:apply-templates select="entity"/>
              </xsl:if>
            </fo:block>
          </fo:block>
          <!-- end of document marker for total pages -->
          <fo:block id="terminator"/>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  
<!-- ************************************************************-->
<!-- * BEGIN - Compatibility with WSXSLTEMPLATEINST001          *-->
<!-- ************************************************************-->

  <xsl:template name="selectAttributesForProcessing">
    <xsl:param name="parentEntityLevel"/>
    <xsl:param name="numColumns"/>
    
    <xsl:variable name="numAttributes" select="count(attributes/attribute)"/>
    <xsl:variable name="numRefAttributes" select="count(attributes/attribute[@keyrefAttribute])"/>
    <xsl:variable name="numKeyAttributes" select="count(attributes/attribute[not(@keyrefAttribute) and @name = ../../@key])"/>
    <xsl:variable name="numHiddenAttributes" select="count(attributes/attribute[@hidden != 'false'])"/>
    <xsl:variable name="totalAttsToDisplay">
      <xsl:number value="$numAttributes - ($numRefAttributes + $numKeyAttributes + $numHiddenAttributes)"/>
    </xsl:variable>
    
    <fo:table-body>       
      <!-- fill table body with attributes, except key attributes, keyrefs and hidden attributes -->
      <xsl:choose>
        <xsl:when test="@key">
          <xsl:call-template name="processValidAttributes">
            <xsl:with-param name="numberOfColumns" select="$numColumns"/>
            <xsl:with-param name="parentEntityLevelCount" select="$parentEntityLevel"/>
            <xsl:with-param name="validAttributesToDisplay" select="attributes/attribute[not(@keyrefAttribute) and @name != ../../@key and @hidden = 'false']"/>
            <xsl:with-param name="totalAttributesToDisplay" select="$totalAttsToDisplay"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <!-- no key attributes for this entity -->
          <xsl:call-template name="processValidAttributes">
            <xsl:with-param name="numberOfColumns" select="$numColumns"/>
            <xsl:with-param name="parentEntityLevelCount" select="$parentEntityLevel"/>
            <xsl:with-param name="validAttributesToDisplay" select="attributes/attribute[not(@keyrefAttribute) and @hidden = 'false']"/>
            <xsl:with-param name="totalAttributesToDisplay" select="$totalAttsToDisplay"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
      
      <fo:table-row>
        <!-- empty row to stop errors -->
          <fo:table-cell padding="2pt" border-color="white" number-columns-spanned="{$parentEntityLevel}">
            <fo:block>
              <fo:leader leader-pattern="space"/>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
    </fo:table-body>    
    
  </xsl:template>
  
  <xsl:template name="processValidAttributes">
    <xsl:param name="parentEntityLevelCount"/>
    <xsl:param name="numberOfColumns"/>    
    <xsl:param name="validAttributesToDisplay"/>
    <xsl:param name="totalAttributesToDisplay"/>
    
    <xsl:for-each select="$validAttributesToDisplay">
      <xsl:choose>
        <!-- only print every 2nd position as we process attributes in pairs as we have 2 columns -->
        <xsl:when test="(position() mod 2) = 1">
          <fo:table-row>
            <!-- empty cells to indent table if needed -->
            <xsl:if test="$parentEntityLevelCount &gt; $level-min">
              <fo:table-cell padding="2pt" border-color="white" number-columns-spanned="{$parentEntityLevelCount}">
                <fo:block>
                  <fo:leader leader-pattern="space"/>
                </fo:block>
              </fo:table-cell>
            </xsl:if>
            
            <!-- print first column -->
            <fo:table-cell padding="2pt" border="none" background-color="#ecedee" display-align="after">
              <fo:block font-family="Times" text-align="right">
                <fo:inline font-weight="bold">
                  <xsl:variable name="textWrap" select="@description"/>
                  <xsl:call-template name="textWrapper">
                   <xsl:with-param name="wrappedText" select="$textWrap"/>
                  </xsl:call-template>
                  <xsl:text>:</xsl:text>
                </fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding="2pt" border="none" background-color="#ecedee" display-align="after">
              <fo:block font-family="Times">
                <xsl:choose>
                  <!-- convert true to Yes, or false to No -->
                  <xsl:when test="(@value = 'true') or (@value = 'false')">
                    <xsl:choose>
                      <xsl:when test="@value = 'true'">
                        Yes
                      </xsl:when>
                      <xsl:otherwise>
                        No
                      </xsl:otherwise>
                    </xsl:choose>                
                  </xsl:when>
                  <xsl:otherwise>
                    <!-- Use wrappable value -->
                    <xsl:variable name="textWrap" select="@value"/>
	                <xsl:call-template name="textWrapper">
	                   <xsl:with-param name="wrappedText" select="$textWrap"/>
	                </xsl:call-template>
                  </xsl:otherwise>
                </xsl:choose>
              </fo:block>
            </fo:table-cell>
            
            <!-- print SECOND column -->
            <xsl:variable name="nextPosition" select="position()+1"/>
            <xsl:choose>
              <xsl:when test="$nextPosition &lt;= $totalAttributesToDisplay">
                <fo:table-cell padding="2pt" border="none" background-color="#ecedee" display-align="after">
                  <fo:block font-family="Times" text-align="right">
                    <fo:inline font-weight="bold" wrap-option="wrap">
                      <!-- xsl:apply-templates select="$validAttributesToDisplay[$nextPosition]/@description"/-->
                      <xsl:variable name="textWrap" select="$validAttributesToDisplay[$nextPosition]/@description"/>
                  <xsl:call-template name="textWrapper">
                   <xsl:with-param name="wrappedText" select="$textWrap"/>                   
                  </xsl:call-template>                    
                   <xsl:text>:</xsl:text>   
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell padding="2pt" border="none" background-color="#ecedee" display-align="after">                
                  <fo:block font-family="Times">
                    <xsl:choose>
                      <!-- convert true to Yes, or false to No -->
                      <xsl:when test="($validAttributesToDisplay[$nextPosition]/@value = 'true') or ($validAttributesToDisplay[$nextPosition]/@value = 'false')">
                        <xsl:choose>
                          <xsl:when test="$validAttributesToDisplay[$nextPosition]/@value = 'true'">
                            Yes
                          </xsl:when>
                          <xsl:otherwise>
                            No
                          </xsl:otherwise>
                        </xsl:choose>                
                      </xsl:when>
                      <xsl:otherwise>
                        <!-- Use wrappable value -->
	                    <xsl:variable name="textWrap" select="$validAttributesToDisplay[$nextPosition]/@value"/>
		                <xsl:call-template name="textWrapper">
		                   <xsl:with-param name="wrappedText" select="$textWrap"/>
		                </xsl:call-template>
                      </xsl:otherwise>
                    </xsl:choose>
                  </fo:block>
                </fo:table-cell>
              </xsl:when>
              
              <xsl:otherwise>
                <!-- last cell is empty so pad it out -->
                <fo:table-cell padding="2pt" border="none" background-color="#ecedee">
                
                  <fo:block text-align="right">
                    <fo:inline font-weight="bold">
                      <fo:leader leader-pattern="space"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell padding="2pt" border="none" background-color="#ecedee">
                
                  <fo:block text-align="right">
                    <fo:inline font-weight="bold">
                      <fo:leader leader-pattern="space"/>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </xsl:otherwise>
            </xsl:choose>
          </fo:table-row>
        </xsl:when>
        
        
      </xsl:choose>      
    </xsl:for-each>
    
  </xsl:template>
  
  <!-- child entities -->
  <xsl:template match="entities">
    <!--Process each entity -->
    <xsl:for-each select="entity[@hidden = 'false']">
      <xsl:call-template name="processEntity"/>
    </xsl:for-each>
  </xsl:template>
  
  <!-- initital entity encountered -->
  <xsl:template match="entity">
    <xsl:choose>
      <!-- if there are attributes, process as normal -->
      <xsl:when test="count(attributes/attribute) &gt; 0">
        <xsl:call-template name="processEntity"/>
      </xsl:when>
      <!-- if there are no attributes, print heading and process child entities -->
      <xsl:otherwise>        
        <fo:block text-align="left" font-family="Times" font-size="16pt" font-weight="bold" background-color="#dfe8f2" color="#3276ad">
          <fo:leader leader-pattern="space"/>
          <fo:leader leader-pattern="space"/>
          <xsl:value-of select="@description"/>
          <fo:leader leader-pattern="space"/>-
          <fo:leader leader-pattern="space"/>
          <xsl:value-of select="@reference"/>
        </fo:block>
        
        <fo:block>
          <fo:leader leader-pattern="space"/>
        </fo:block>
        
        <!-- process child entities -->
        <xsl:apply-templates select="entities"/>
      </xsl:otherwise>
    </xsl:choose>   
    
  </xsl:template>
  
  <!-- process a single entity -->
  <xsl:template name="processEntity">
    <!-- what level is this entity at? -->
    <xsl:variable name="level" select="count(

      ancestor-or-self::entity)-1"/>      
            
    <xsl:variable name="numCols" select="4"/>
    <xsl:variable name="colWidth" select="$workingPageWidth div $numCols"/>    
    
    <xsl:variable name="numAtts" select="count(attributes/attribute)"/>
    <xsl:variable name="numRefAttributes" select="count(attributes/attribute[@keyrefAttribute])"/>
    <xsl:variable name="numKeyAttributes" select="count(attributes/attribute[not(@keyrefAttribute) and @name = ../../@key and @hidden = 'false'])"/>
    <xsl:variable name="numHiddenAttributes" select="count(attributes/attribute[@hidden != 'false'])"/>
    <xsl:variable name="totalAttsToDisplay">
      <xsl:number value="$numAtts - ($numRefAttributes + $numKeyAttributes + $numHiddenAttributes)"/>
    </xsl:variable>
   
    <xsl:choose>
      <xsl:when test="$totalAttsToDisplay &gt; 0">

      <fo:table table-layout="fixed" width="100%">
        <!-- apply indents if needed -->
        <xsl:if test="$level &gt; $level-min">
          <fo:table-column column-width="0.2in" number-columns-repeated="{$level}"/>
        </xsl:if>
        <fo:table-column column-width="{$colWidth}in" number-columns-repeated="{$numCols}"/>
        
        <fo:table-header>
          <fo:table-row>
            
            <!-- empty cells to indent table -->
            <xsl:if test="$level &gt; $level-min">
              <fo:table-cell padding="2pt" border="none" number-columns-spanned="{$level}">
                <fo:block>
                  <fo:leader leader-pattern="space"/>
                </fo:block>
              </fo:table-cell>
            </xsl:if>
            
            <!-- content header -->
            <fo:table-cell number-columns-spanned="{$numCols}" padding="2pt" border="none">
              <xsl:choose>
                <xsl:when test="$level = 1">
                  <fo:block font-family="Times" font-weight="bold" font-size="14pt" color="#3276ad">
                    <xsl:apply-templates select="@description"/>
                  </fo:block>
                </xsl:when>
                <xsl:otherwise>
                  <fo:block font-family="Times" font-weight="bold" font-size="12pt" color="#3276ad">
                    <xsl:apply-templates select="@description"/>
                  </fo:block>
                </xsl:otherwise>
              </xsl:choose>            
            </fo:table-cell>
          </fo:table-row>
        </fo:table-header>
        
        <!-- process attributes -->
        <xsl:call-template name="selectAttributesForProcessing">
          <xsl:with-param name="parentEntityLevel" select="$level"/>
          <xsl:with-param name="numColumns" select="$numCols"/>
        </xsl:call-template>
      </fo:table>
  
      </xsl:when>
      
      <xsl:otherwise>        
        <!-- if there are visible child entities we need to print the description -->
        <xsl:if test="count(entities/entity[@hidden = 'false']) &gt; 0">
          
          <!-- indent description if needed -->
          <xsl:if test="$level &gt; $level-min">
            <fo:leader leader-pattern="space" leader-length="{$level}"/>
          </xsl:if>
          <fo:block font-family="Times" text-align="right" font-size="14pt" font-weight="bold" color="#3276ad">
            <xsl:apply-templates select="@description"/>
          </fo:block>        
        </xsl:if>        

      </xsl:otherwise>
    </xsl:choose>
    
    <!-- check for refAttributes now -->
    <xsl:if test="$numRefAttributes &gt; 0">
      <xsl:call-template name="checkForRefAttrbutes">
        <xsl:with-param name="parentEntityLevel" select="$level"/>
      </xsl:call-template>
    </xsl:if>
    
    <fo:block>
      <fo:leader leader-pattern="space"/>
    </fo:block>
    
    <!-- process child entities -->
    <xsl:apply-templates select="entities"/>
      
  </xsl:template>
  
  <xsl:template name="checkForRefAttrbutes">
    <xsl:param name="parentEntityLevel"/>
    
    <xsl:for-each select="attributes/attribute">
      <xsl:if test="@keyrefAttribute">
        <xsl:call-template name="findRefEntity">
          <xsl:with-param name="parentEntityLevel" select="$parentEntityLevel"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template name="findRefEntity">
    <xsl:param name="parentEntityLevel"/>
    <xsl:variable name="refEntityName" select="@keyrefEntity"/>
    <xsl:variable name="refAttributeName" select="@keyrefAttribute"/>
    <xsl:variable name="refAttributeValue" select="@value"/> 
    
    <!-- find entities who match the ref entity name and who have attributes who's name and value match the ref attribute name and value -->
    <xsl:for-each select="/DOCUMENT/DATA//entity[@name=$refEntityName and attributes/attribute/@name=$refAttributeName and attributes/attribute/@value=$refAttributeValue]">
      
      <xsl:variable name="numCols" select="4"/>
      <xsl:variable name="colWidth" select="$workingPageWidth div $numCols"/> 
      
      <fo:block font-family="Times" font-size="7pt" font-style="italic">
        <fo:leader leader-pattern="space"/>
        <fo:table table-layout="fixed" width="100%">
          <!-- apply indents if needed -->
          <xsl:if test="$parentEntityLevel &gt; $level-min">
            <fo:table-column column-width="0.2in" number-columns-repeated="{$parentEntityLevel}"/>
          </xsl:if>
          <fo:table-column column-width="{$colWidth}in" number-columns-repeated="{$numCols}"/>
          
          <fo:table-header>
            <fo:table-row>
              
              <!-- empty cells to indent table -->
              <xsl:if test="$parentEntityLevel &gt; $level-min">
                <fo:table-cell padding="2pt" border="none" number-columns-spanned="{$parentEntityLevel}">
                  <fo:block>
                    <fo:leader leader-pattern="space"/>
                  </fo:block>
                </fo:table-cell>
              </xsl:if>
              
              <!-- content header -->
              <fo:table-cell number-columns-spanned="{$numCols}" background-color="#ecedee" padding="2pt" border="none">
                <fo:block font-family="Times" font-weight="bold" font-size="10pt" color="#3276ad">
                  <xsl:value-of select="@description"/>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-header>
          
          
          <!-- process attributes -->
          <xsl:call-template name="selectAttributesForProcessing">
            <xsl:with-param name="parentEntityLevel" select="$parentEntityLevel"/>
            <xsl:with-param name="numColumns" select="$numCols"/>
          </xsl:call-template>
        </fo:table>
      </fo:block>
    </xsl:for-each>
    
  </xsl:template>
  
<!-- ************************************************************-->
<!-- * END - Compatibility with WSXSLTEMPLATEINST001            *-->
<!-- ************************************************************-->

<!-- ************************************************************-->
<!-- * BEGIN - Common functions                                 *-->
<!-- ************************************************************-->

  <!-- Text wrapping functionality for wrapping the text within the columns.This adds a zero width character(&#x200b) after every character.
       On doing this once it comes near the end of the column it automatically wraps the contents exceeding this column to the next column.
       Doing this stops the overflowing of the contents from one column to the other column.
       
       Please do not make it recursive as it fails to print some contents if you do so.
   -->
   <xsl:template name="textWrapper">
	<xsl:param name="wrappedText"/>
	<xsl:param name="counter" select="0"/>
	 
	<xsl:choose>
    <xsl:when test="$counter &lt;= string-length($wrappedText)">	
	<xsl:value-of select='concat(substring($wrappedText,$counter,1),"&#x200b;")'/>
	 <xsl:call-template name="textWrapper2">
   	       <xsl:with-param name="data" select="$wrappedText"/>
   	       <xsl:with-param name="counter" select="$counter+1"/>
  	  </xsl:call-template>
    </xsl:when>   
    <xsl:otherwise>
    </xsl:otherwise>
  </xsl:choose>             
</xsl:template>


<xsl:template name="textWrapper2">
<xsl:param name="data"/>
<xsl:param name="counter"/>
   <xsl:value-of select='concat(substring($data,$counter,1),"&#x200b;")'/>
   <xsl:call-template name="textWrapper">
    <xsl:with-param name="wrappedText" select="$data"/>
    <xsl:with-param name="counter" select="$counter+1"/>
  </xsl:call-template>
</xsl:template>
  


<!-- ************************************************************-->
<!-- * BEGIN - SUMMARY-PDF  Template                            *-->
<!-- ************************************************************-->

<!-- SUMMARY-PDF Template variables -->
  <xsl:variable name="isSummaryPagePresent" select="not(not(/DOCUMENT/DATA/summary-page))"/>
  <xsl:variable name="hiddenConditionalElements" select="/DOCUMENT/DATA//conditionalElement[@visible='false']"/>
  <xsl:variable name="bg-color-main">#cacaca</xsl:variable>
  <xsl:variable name="bg-color-section">#eaeaea</xsl:variable>
  <xsl:variable name="bg-color-badge">#B8C1C1</xsl:variable>
  <xsl:variable name="header-role-page">H1</xsl:variable>
  <xsl:variable name="header-role-section">H2</xsl:variable>

  <xsl:attribute-set name="questionValueAttSet">
    <xsl:attribute name="font-size">12px</xsl:attribute>
    <xsl:attribute name="line-height">16px</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="questionLabelAttSet">
    <xsl:attribute name="font-size">10px</xsl:attribute>
    <xsl:attribute name="line-height">14px</xsl:attribute>
  </xsl:attribute-set>

<xsl:variable name="fo:bookmark-tree">
  <xsl:if test="$isSummaryPagePresent">
    <fo:bookmark-tree>
      <xsl:for-each select="/DOCUMENT/DATA/summary-page//cluster | /DOCUMENT/DATA/summary-page//list | /DOCUMENT/DATA/summary-page//relationship-summary-list">
          <xsl:variable name="internal-id" select="@internal-id"/>
          <xsl:variable name="grouping-id" select="@grouping-id"/>
          <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])" />
          <xsl:variable name="isClusterGrouped" select="boolean(@grouping-id)" />
          <xsl:variable name="isClusterGroupLead" select="contains(ancestor::*/cluster[@grouping-id=$grouping-id][1]/@internal-id, current()/@internal-id)" />
          <xsl:if test="not($isHiddenElement) and (not($isClusterGrouped) or $isClusterGroupLead) and not(not(./title))">
            <fo:bookmark internal-destination="{$internal-id}">
            <fo:bookmark-title> <xsl:value-of select="./title[text()]" /></fo:bookmark-title> 
          </fo:bookmark>
          </xsl:if>
      </xsl:for-each>
    </fo:bookmark-tree>  
  </xsl:if>
</xsl:variable>

  <xsl:variable name="fo:declarations">
 	  <fo:declarations>
 	  <x:xmpmeta xmlns:x="adobe:ns:meta/">
 	    <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
 	      <rdf:Description rdf:about="" xmlns:dc="http://purl.org/dc/elements/1.1/">
 	      	<dc:title> 
  	      		<rdf:Alt>
 	      			<rdf:li xml:lang="x-default"><xsl:value-of select="//entity[@name='Application']/@description"/></rdf:li>
 	      		</rdf:Alt>
 	      	</dc:title>
 	      </rdf:Description>
 	    </rdf:RDF>
 	  </x:xmpmeta>
       <pdf:catalog xmlns:pdf="http://xmlgraphics.apache.org/fop/extensions/pdf">
           <pdf:dictionary type="normal" key="ViewerPreferences">
               <pdf:boolean key="DisplayDocTitle">true</pdf:boolean>
           </pdf:dictionary>
       </pdf:catalog> 	  
 	</fo:declarations>
 </xsl:variable>


  <xsl:template name="header">
    <fo:table table-layout="fixed" width="100%">
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell display-align="center">
            <fo:block xsl:use-attribute-sets="questionValueAttSet" text-align="left" padding-bottom="2pt">
              <xsl:value-of select="//application-ref/label[text()]"/>
              <xsl:text> </xsl:text>
              <fo:inline font-weight="bold">
                <xsl:value-of select="//application-ref/value[text()]"/>
              </fo:inline>
            </fo:block>
            <fo:block xsl:use-attribute-sets="questionValueAttSet" text-align="left" padding-top="2pt">
              <xsl:value-of select="//submission-date/label[text()]"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="//submission-date/value[text()]"/>
            </fo:block>
          </fo:table-cell>

          <!-- Logo -->
          <xsl:if test="//logo">
            <fo:table-cell>
              <fo:block text-align="end">
                <xsl:element name="fo:external-graphic">
                  <xsl:attribute name="src">
                    <xsl:text>url('data:</xsl:text>
                    <xsl:value-of select="//logo-type[text()]"/>
                    <xsl:text>;base64,</xsl:text>
                    <xsl:apply-templates select="//logo"/>
                    <xsl:text>')</xsl:text>
                  </xsl:attribute>
                  <xsl:attribute name="vertical-align">middle</xsl:attribute>
                  <xsl:attribute name="content-width">200px</xsl:attribute>
                  <xsl:attribute name="content-height">50px</xsl:attribute>
                  <xsl:attribute name="fox:alt-text"><xsl:value-of select="//logo-alt-text[text()]"/></xsl:attribute>
                </xsl:element>
              </fo:block>
            </fo:table-cell>
          </xsl:if>

        </fo:table-row>
      </fo:table-body>
    </fo:table>

  </xsl:template>

  <xsl:template match="cluster">
    <xsl:variable name="internal-id" select="@internal-id"/>
    <xsl:variable name="grouping-id" select="@grouping-id"/>
    <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])" />
    <xsl:variable name="isClusterGrouped" select="boolean(@grouping-id)" />
    <xsl:variable name="isClusterGroupLead" select="contains(ancestor::*/cluster[@grouping-id=$grouping-id][1]/@internal-id, current()/@internal-id)" />

    <xsl:if test="not($isHiddenElement) and (not($isClusterGrouped) or $isClusterGroupLead)">
      <xsl:call-template name="sectionTitle">
          <xsl:with-param name="internal-id" select="$internal-id"/>
          <xsl:with-param name="title" select="title"/>
        </xsl:call-template>

        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
          <fo:table-body>
            <xsl:call-template name="processClusters">
              <xsl:with-param name="clusters" select=". | //cluster[@grouping-id=$grouping-id]"/>
            </xsl:call-template>
          </fo:table-body>
        </fo:table>
    </xsl:if>
  </xsl:template>

  <xsl:template name="processClusters">
    <xsl:param name="clusters" />
    <xsl:for-each select="$clusters">
    <!-- filter cluster children to apply templates -->
      <xsl:variable name="clusterChildren" select="./child::container | ./child::question | ./child::list-question"/>
      <xsl:variable name="cluster-internal-id" select="./@internal-id"/>
      <xsl:for-each select="$clusterChildren[position() mod 2 != 0]">
        <fo:table-row>
	        <xsl:apply-templates select=".">
	          <xsl:with-param name="internal-id" select="$cluster-internal-id"/>
	        </xsl:apply-templates>
	        <xsl:if test="./following-sibling::*">
		        <xsl:apply-templates select="./following-sibling::*[1]">
		          <xsl:with-param name="internal-id" select="$cluster-internal-id"/>
		        </xsl:apply-templates>
	        </xsl:if>
        </fo:table-row>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="sectionTitle">
    <xsl:param name="internal-id" />
    <xsl:param name="title" />
    <xsl:choose>
      <xsl:when test="title and title[text()] != ''">
        <fo:block space-before="10px" space-after="10px" line-height="20px" font-size="14px" padding="6px" 
          background-color="{$bg-color-section}" role="{$header-role-section}"
          id="{$internal-id}">
          <xsl:value-of select="title[text()]" />
        </fo:block>
      </xsl:when>
      <xsl:otherwise>
        <fo:block id="{$internal-id}">
          <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- It is not used-->
  <xsl:template name="sectionDescription">
      <xsl:param name="description" />
      <fo:block space-before="10px" space-after="10px" line-height="20px" font-size="14px" padding="6px" background-color="{$bg-color-section}">
        <xsl:value-of select="description[text()]" />
      </fo:block>
  </xsl:template>

  <xsl:template match="list">
    <xsl:variable name="internal-id" select="@internal-id"/>
    <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])" />
    <xsl:variable name="isNestedList" select="boolean(contains(local-name(parent::*),'list'))" />
    <xsl:if test="not($isHiddenElement) and not($isNestedList) ">
      <xsl:call-template name="sectionTitle">
        <xsl:with-param name="internal-id" select="$internal-id"/>
        <xsl:with-param name="title" select="title"/>
      </xsl:call-template>
      <xsl:variable name="columns" select=".//column"/>
      <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
        <xsl:call-template name="column-header">
          <xsl:with-param name="columns" select="$columns"/>
        </xsl:call-template>
        <fo:table-body>
          <xsl:call-template name="addRows">
            <xsl:with-param name="columns" select="$columns"/>
            <xsl:with-param name="max" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id][last()]/@item-index"/>
            <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index='0' and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index='0']/@loopindex)][1]/@loopindex"/>
            <xsl:with-param name="internal-id" select="$internal-id"/>
          </xsl:call-template>
        </fo:table-body>
      </fo:table>
    </xsl:if>
  </xsl:template>

  <xsl:template match="relationship-summary-list">

    <xsl:variable name="internal-id" select="@internal-id"/>

    <xsl:if test="not($hiddenConditionalElements[@internal-id=$internal-id])">
      <xsl:call-template name="sectionTitle">
        <xsl:with-param name="internal-id" select="$internal-id"/>
        <xsl:with-param name="title" select="title"/>
      </xsl:call-template>
      <fo:table table-layout="fixed" width="100%">
        <!-- relationship-body -->
        <fo:table-body>
          <xsl:call-template name="addRows-relationship">
            <xsl:with-param name="columns" select="column"/>
            <xsl:with-param name="internal-id" select="$internal-id"/>
            <xsl:with-param name="max" select="count(//relationship-summary-list-data/relationships-list/relationships)"/>
            <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index='0' and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index='0']/@loopindex)][1]/@loopindex"/>
          </xsl:call-template>
        </fo:table-body>
      </fo:table>
    </xsl:if>
  </xsl:template>

  <xsl:template name="cell-header">
    <xsl:param name="columns"/>
    <xsl:for-each select="$columns">
      <fo:table-cell role="TH">
        <fo:block xsl:use-attribute-sets="questionLabelAttSet">
          <xsl:variable name="textWrap" select="title[text()]"/>
          <xsl:call-template name="textWrapper">
            <xsl:with-param name="wrappedText" select="$textWrap"/>
          </xsl:call-template>
        </fo:block>
      </fo:table-cell>
    </xsl:for-each>
  </xsl:template>


  <xsl:template name="column-header">
    <xsl:param name="columns"/>
    <xsl:for-each select="$columns">
    	<fo:table-column xmlns:fox="http://xmlgraphics.apache.org/fop/extensions" fox:header="true" column-width="proportional-column-width(1)"/>
    </xsl:for-each>    
    <fo:table-header>
      <fo:table-row>
        <xsl:call-template name="cell-header">
          <xsl:with-param name="columns" select="$columns"/>
        </xsl:call-template>
      </fo:table-row>
    </fo:table-header>
  </xsl:template>

 <!--  Add list rows
       - columns (Elements): Columns of each row.
       - internal-id (long): Internal id of the list.
       - max (integer):  Maximum number of rows.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list.
  -->
  <xsl:template name="addRows">
    <xsl:param name="columns"/>
    <xsl:param name="internal-id"/>
    <xsl:param name="max"/>
    <xsl:param name="maxLoopindex"/>
    <xsl:choose>
      <!-- No information entered : max won't have a numerical value -->
      <xsl:when test="not($max &lt;= 0 or $max &gt;= 0)">
	     <fo:table-row>
		     <xsl:for-each select="$columns">
		      <fo:table-cell role="TD">
		        <fo:block xsl:use-attribute-sets="questionValueAttSet">
		        	<xsl:text>--</xsl:text>
		        </fo:block>
		      </fo:table-cell>
	    	</xsl:for-each>
	    </fo:table-row>        
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="addRow">
          <xsl:with-param name="columns" select="$columns"/>
          <xsl:with-param name="index" select="0"/>
          <xsl:with-param name="internal-id" select="$internal-id"/>
          <xsl:with-param name="max" select="$max"/>
          <xsl:with-param name="loopindex" select="0"/>
          <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

 <!--
       - columns (Elements): Columns of each row.
       - index   (integer):  Curent index of the list.
       - internal-id (long): Internal id of the parent list.
       - max (integer):  Maximum number of rows.
       - loopindex (integer): Loopindex of the row, when adding a row of a nested list loopindex is bigger than 10000.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list.
  -->
  <xsl:template name="addRow">
    <xsl:param name="columns"/>
    <xsl:param name="index" />
    <xsl:param name="internal-id" />
    <xsl:param name="max"/>
    <xsl:param name="loopindex"/>
    <xsl:param name="maxLoopindex"/>

    <xsl:variable name="nestedListIndex" select="10000"/>
    <xsl:variable name="isNestedRow" select="$loopindex &gt;= $nestedListIndex"/>
    <xsl:variable name="nestedIndex" select="(($index + 1)  * ($nestedListIndex))"/>
    <xsl:variable name="parentListValue" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$columns[1]/@id and @item-index=$index and @loopindex=$index]/text()" />
     <xsl:if test="$index &lt;= $max">
      <!-- evaluate if the parent list has no values ie holes in data, if yes , skip that row -->
      <xsl:if test="not(not($parentListValue) or $parentListValue=' ')">
      <fo:table-row>
        <xsl:for-each select="$columns">
          <xsl:variable name="isNestedListColumn" select="not(boolean(normalize-space(../@internal-id)=normalize-space($internal-id)))"/>

          <xsl:variable name="loopIdx">
            <!-- loopindex to get the answer of the column, it changes on the based it is a nested column, nested row -->
            <xsl:choose>
              <xsl:when test="$isNestedListColumn and not($isNestedRow)">
                <xsl:value-of select="($index + 1)  * ($nestedListIndex)"/>
              </xsl:when>
              <xsl:when test="$isNestedListColumn and $isNestedRow">
                <xsl:value-of select="$loopindex"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$index"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>

          <xsl:variable name="id" select="@id"/>
          <xsl:variable name="answer" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$id and @item-index=$index and @loopindex=$loopIdx]" />
          <xsl:variable name="value" select="$answer/text()" />
          <xsl:variable name="rowCount">
            <xsl:choose>
              <xsl:when test="$answer/@row-count">
              <xsl:value-of select="number($answer/@row-count)" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="0" />
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:variable name="cellBlock">
            <fo:block xsl:use-attribute-sets="questionValueAttSet" >
              <xsl:choose>
                <xsl:when test="not($value) or $value=' '">
                  <xsl:text>--</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:variable name="textWrap" select="$value"/>
                  <xsl:call-template name="textWrapper">
                    <xsl:with-param name="wrappedText" select="$textWrap"/>
                  </xsl:call-template>
                </xsl:otherwise>
              </xsl:choose>
            </fo:block>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="not($isNestedListColumn) and  not($isNestedRow)">
              <fo:table-cell border-width="0.5mm" number-rows-spanned="{$rowCount}" role="TD">
                <xsl:copy-of select="$cellBlock" />
              </fo:table-cell>
            </xsl:when>
            <xsl:when test="not($isNestedListColumn) and  $isNestedRow">
            <!-- Do nothing , non nested columns should not be considered for nested rows -->
            </xsl:when>
            <xsl:otherwise>
              <fo:table-cell border-width="0.5mm" role="TD">
                <xsl:copy-of select="$cellBlock" />
              </fo:table-cell>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>
      </fo:table-row>
      </xsl:if>
      <xsl:choose>
        <!-- Evaluate if next row is for nested elements -->
        <xsl:when test="$loopindex &lt; $maxLoopindex and $nestedIndex &lt; $maxLoopindex ">
          <xsl:variable name="loopIdx">
            <xsl:choose>
              <xsl:when test="not($isNestedRow)">
                <xsl:value-of select="(($index + 1)  * ($nestedListIndex)) + 1"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="$loopindex + 1"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <xsl:call-template name="addRow">
            <xsl:with-param name="index" 		    select="$index"/>
            <xsl:with-param name="max" 			    select="$max"/>
            <xsl:with-param name="loopindex" 	  select="$loopIdx"/>
            <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
            <xsl:with-param name="columns" 		  select="$columns"/>
            <xsl:with-param name="internal-id" 	select="$internal-id"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="itemIndex" select="$index + 1"/>
          <xsl:call-template name="addRow">
            <xsl:with-param name="index" 		    select="$itemIndex"/>
            <xsl:with-param name="max" 			    select="$max"/>
            <!-- loopindex and item-index have the same value for non-nested lists -->
            <xsl:with-param name="loopindex" 	  select="$itemIndex"/>
            <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index=$itemIndex and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index=$itemIndex]/@loopindex)][1]/@loopindex"/>
            <xsl:with-param name="columns" 		  select="$columns"/>
            <xsl:with-param name="internal-id" 	select="$internal-id"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <xsl:template name="addRows-relationship">
    <xsl:param name="columns"/>
    <xsl:param name="internal-id"/>
    <xsl:param name="max"/>
    <xsl:param name="maxLoopindex"/>
    
      <!-- No information entered : max won't have a numerical value or it is 0 -->
	  <xsl:choose>
      <xsl:when test="not($max &lt;= 0 or $max &gt;= 0) or $max = 0">
	     <fo:table-row>
        <fo:table-cell role="TD">
          <fo:block xsl:use-attribute-sets="questionValueAttSet">
            <xsl:text>--</xsl:text>
          </fo:block>
        </fo:table-cell>
	    </fo:table-row>        
      </xsl:when>
      <xsl:otherwise>
	    <xsl:call-template name="addRow-relationship">
	      <xsl:with-param name="columns" select="$columns"/>
	      <xsl:with-param name="internal-id" select="$internal-id"/>
	      <xsl:with-param name="index" select="0"/>
	      <xsl:with-param name="loopindex" select="10000"/>
	      <xsl:with-param name="max" select="$max"/>
	      <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
	    </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--
       - columns (Elements): Columns of each row (optional).
       - index   (integer):  Curent index of the relationships/relationship.
       - internal-id (long): Internal id of the relationship list.
       - max (integer):  Maximum number of rows relationships/relationship.
       - loopindex (integer): Loopindex of the row, when adding a row of a nested list loopindex is bigger than 10000.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list, (optional).
  -->
  <xsl:template name="addRow-relationship">
    <xsl:param name="columns"/>
    <xsl:param name="internal-id" />
    <xsl:param name="index" />
    <xsl:param name="loopindex"/>
    <xsl:param name="max"/>
    <xsl:param name="maxLoopindex"/>

    <xsl:variable name="nestedListIndex"    select="10000"/>
    <xsl:variable name="relationshipindex"  select="(1 + $loopindex - ($nestedListIndex * ($index + 1)))"/>
    <!-- When dynamic columns are not present, ignore maxloopindex as there will not be any reponse/answer related to the relationships -->
    <xsl:variable name="dynamicColumns"   select="$columns[not(@id='caretakerInd')]"/>
    <xsl:variable name="relationships"    select="//relationship-summary-list-data/relationships-list/relationships[@loopindex=$index]"/>
    <xsl:variable name="relationship"     select="$relationships/relationship[$relationshipindex]"/>
    <xsl:variable name="maxCount"         select="count($relationships/relationship)"/>

    <xsl:if test="$index &lt; $max and ($loopindex &lt;= $maxLoopindex or not($dynamicColumns)) ">
      <fo:table-row height="36px">
        <!-- Static Columns -->
        <!-- From -->
        <fo:table-cell padding-right="5px" role="TD">
          <fo:block>
            <fo:inline-container inline-progression-dimension="39.9%">
              <fo:block xsl:use-attribute-sets="questionValueAttSet">
                <xsl:call-template name="textWrapper">
                  <xsl:with-param name="wrappedText" select="//relationship-summary-list-data/columns/column[contains(@id,'from')]/title"/>
                </xsl:call-template>
              </fo:block>
              <fo:block xsl:use-attribute-sets="questionValueAttSet">
                <xsl:choose>
                  <xsl:when test="not($relationships/@person-name)">
                    <xsl:text>--</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <!-- Use wrappable value -->
                    <xsl:call-template name="textWrapper">
                      <xsl:with-param name="wrappedText" select="$relationships/@person-name"/>
                    </xsl:call-template>
                  </xsl:otherwise>
                </xsl:choose>
              </fo:block>
            </fo:inline-container>
            <fo:inline-container inline-progression-dimension="59.9%">
              <fo:block xsl:use-attribute-sets="questionValueAttSet">
                <xsl:text> &#160; </xsl:text>
              </fo:block>
              <xsl:choose>
                <xsl:when test="//summary-page//relationship-summary-list/column[contains(@id,'caretakerInd')] and $relationship/@caretaker-ind='true'">
                  <fo:block xsl:use-attribute-sets="questionValueAttSet">
                    <fo:inline background-color="{$bg-color-badge}" color="#000000">
                      <xsl:value-of select="//summary-page//relationship-summary-list/column[contains(@id,'caretakerInd')]/title"/>
                    </fo:inline>
                  </fo:block>
                </xsl:when>
                <xsl:otherwise>
                  <fo:block xsl:use-attribute-sets="questionValueAttSet">
                    <xsl:text> &#160; </xsl:text>
                  </fo:block>
                </xsl:otherwise>
              </xsl:choose>
            </fo:inline-container>
          </fo:block>
        </fo:table-cell>

        <!-- Type , To  -->
        <xsl:call-template name="cell2Blocks">
          <xsl:with-param name="label" select="$relationship/@type"/>
          <xsl:with-param name="value" select="$relationship/@person-name"/>
        </xsl:call-template>
      </fo:table-row>
      <!-- For each dynamic column -->
      <xsl:if test="$dynamicColumns">
        <xsl:apply-templates select="$dynamicColumns[position() mod 2 != 0]">
          <xsl:with-param name="internal-id" select="$internal-id"/>
          <xsl:with-param name="index" select="$index"/>
          <xsl:with-param name="loopindex" select="$loopindex"/>
        </xsl:apply-templates>
      </xsl:if>
      <fo:table-row border-top-style="solid" border-top-color="{$bg-color-main}" border-top-width="medium" height="5px">
        <fo:table-cell>
          <fo:block></fo:block>
        </fo:table-cell>
        <fo:table-cell>
          <fo:block></fo:block>
        </fo:table-cell>
      </fo:table-row>
      <xsl:choose>
        <!-- Evaluate if next row is for nested elements -->
        <xsl:when test="$relationshipindex &lt; $maxCount">
          <xsl:call-template name="addRow-relationship">
            <xsl:with-param name="columns" select="$columns"/>
            <xsl:with-param name="internal-id" select="$internal-id"/>
            <xsl:with-param name="index" select="$index"/>
            <xsl:with-param name="loopindex" select="$loopindex + 1"/>
            <xsl:with-param name="max" select="$max"/>
            <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="itemIndex" select="$index + 1"/>
          <xsl:call-template name="addRow-relationship">
            <xsl:with-param name="columns" select="$columns"/>
            <xsl:with-param name="internal-id" select="$internal-id"/>
            <xsl:with-param name="index" select="$itemIndex"/>
            <xsl:with-param name="loopindex" select="($itemIndex + 1)  * ($nestedListIndex)"/>
            <xsl:with-param name="max" select="$max"/>
            <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index=$itemIndex and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index=$itemIndex]/@loopindex)][1]/@loopindex"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>


  <xsl:template match="column" name="relationship">
    <xsl:param name="internal-id" />
    <xsl:param name="index" />
    <xsl:param name="loopindex"/>
    <xsl:variable name="id" select="@id"/>
    <xsl:variable name="value" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$id and @item-index=$index and @loopindex=$loopindex]/text()"/>
    <fo:table-row height="36px">
      <xsl:call-template name="cell2Blocks">
        <xsl:with-param name="label" select="./title"/>
        <xsl:with-param name="value" select="$value"/>
      </xsl:call-template>
      <xsl:if test="./following-sibling::column">
        <xsl:variable name="column-id" select="./following-sibling::column/@id"/>
        <xsl:variable name="nextValue" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$column-id and @item-index=$index and @loopindex=$loopindex]/text()"/>
        <xsl:call-template name="cell2Blocks">
          <xsl:with-param name="label" select="./following-sibling::column/title"/>
          <xsl:with-param name="value" select="$nextValue"/>
        </xsl:call-template>
      </xsl:if>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="question">
    <xsl:param name="internal-id"/>
    <xsl:call-template name="cell">
      	<xsl:with-param name="internal-id" select="$internal-id"/>
	      <xsl:with-param name="question" select="."/>
	  </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="container">
    <xsl:param name="internal-id"/>
    <xsl:variable name="value">
      <xsl:call-template name="containerValues">
      	<xsl:with-param name="internal-id" select="$internal-id"/>
	  	  <xsl:with-param name="children" select="./child::*"/>
		</xsl:call-template>
		</xsl:variable>
      <xsl:call-template name="cell2Blocks">
        <xsl:with-param name="label" select="./title"/>
        <xsl:with-param name="value" select="$value"/>
      </xsl:call-template>
  </xsl:template>

	<xsl:template match="list-question">
    <xsl:param name="internal-id"/>
      <xsl:call-template name="cellLQ">
        <xsl:with-param name="internal-id" select="@internal-id"/>
        <xsl:with-param name="list-question" select="."/>
      </xsl:call-template>
  </xsl:template>

  <xsl:template name="cellLQ">
    <xsl:param name="internal-id"/>
    <xsl:param name="list-question"/>

    <xsl:variable name="id" select="$list-question/@id"/>
    <xsl:variable name="answers" select="/DOCUMENT/DATA//list-question[@internal-id=$internal-id]/item[@item-value='true']"/>
    <xsl:variable name="value">
      <xsl:call-template name="answerValuesLQ">
        <xsl:with-param name="answers" select="$answers"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:call-template name="cell2Blocks">
      <xsl:with-param name="label" select="$list-question/label[text()]"/>
      <xsl:with-param name="value" select="$value"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="answerValuesLQ">
    <xsl:param name="answers"/>

    <xsl:for-each select="$answers">
      <xsl:variable name="childCount" select="count($answers)"/>
			<xsl:variable name="position" select="position()"/>

      <xsl:for-each select=".">
        <xsl:value-of select="@item-label"/>
        <xsl:if test="not($position=$childCount)">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>

    <xsl:template name="cell2Blocks">
    <xsl:param name="label"/>
    <xsl:param name="value"/>
    <fo:table-cell padding-right="5px" >
      <fo:block xsl:use-attribute-sets="questionLabelAttSet">
        <xsl:call-template name="textWrapper">
          <xsl:with-param name="wrappedText" select="$label"/>
        </xsl:call-template>
      </fo:block>
      <fo:block xsl:use-attribute-sets="questionValueAttSet">
        <xsl:choose>
          <xsl:when test="not($value) or $value=' '">
            <xsl:text>--</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <!-- Use wrappable value -->
            <xsl:call-template name="textWrapper">
              <xsl:with-param name="wrappedText" select="$value"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </fo:table-cell>
  </xsl:template>

  <xsl:template name="cell">
    <xsl:param name="question"/>
    <xsl:param name="internal-id"/>

    <xsl:variable name="id" select="$question/@id"/>
    <xsl:variable name="answers" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and (substring-after(@question-id,'.')=$id or @question-id=$id)]"/>
    <xsl:variable name="value">
      <xsl:call-template name="answerValues">
        <xsl:with-param name="answers" select="$answers"/>
      </xsl:call-template>
    </xsl:variable>

    <xsl:call-template name="cell2Blocks">
      <xsl:with-param name="label" select="$question/label[text()]"/>
      <xsl:with-param name="value" select="$value"/>
    </xsl:call-template>
  </xsl:template>

    <xsl:template name="answerValues">
    <xsl:param name="answers"/>

    <xsl:for-each select="$answers">
      <xsl:variable name="childCount" select="count($answers)"/>
			<xsl:variable name="position" select="position()"/>

      <xsl:for-each select=".">
        <xsl:value-of select="./text()"/>
        <xsl:if test="not($position=$childCount)">
          <xsl:text>, </xsl:text>
        </xsl:if>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>
  
  <!--
    Concatenates all values of a container, questions and dividers. 
    If there is no divider between questions an empty space is added.
  -->  
  <xsl:template name="containerValues">
    <xsl:param name="internal-id"/>
    <xsl:param name="children"/>

    <xsl:for-each select="$children">
      <xsl:for-each select=".">
        <xsl:if test="local-name(.)='divider'">
          <xsl:value-of select="./text()"/>
        </xsl:if>
        <xsl:if test="local-name(.)='question'">
		  <xsl:variable name="id" select="./@id"/>
		  <xsl:variable name="answer" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and (substring-after(@question-id,'.')=$id or @question-id=$id)]"/>        
          <xsl:value-of select="$answer/text()"/>
          <xsl:if test="not(local-name(./following-sibling::*)='divider') and (local-name(./following-sibling::*)='question')">
            <xsl:text> </xsl:text>
	      </xsl:if>
        </xsl:if>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>  
<!-- ************************************************************-->
<!-- * END  - SUMMARY-PDF  Template                             *-->
<!-- ************************************************************-->

</xsl:stylesheet>