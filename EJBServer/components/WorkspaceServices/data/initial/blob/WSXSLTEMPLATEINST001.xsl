<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
  <xsl:variable name="workingPageWidth" select="7"/>
  <xsl:variable name="fo:layout-master-set">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
        <fo:region-body margin-top="0.79in" margin-bottom="1.5in"/>
        <fo:region-after extent="1.5in" display-align="before"/>
      </fo:simple-page-master>
    </fo:layout-master-set>
  </xsl:variable>
  
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA"/>
  </xsl:template>
  
  <xsl:template match="DATA">
    <!--Explicitly select the entity to avoid processing anything else.-->
    <fo:root>
      <xsl:copy-of select="$fo:layout-master-set"/>
      <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
        
        <!-- Footer for page number -->
        <fo:static-content flow-name="xsl-region-after" font-family="Times" font-size="8pt">
          <fo:block space-before="5mm" text-align="center">
            Page <fo:page-number/> of <fo:page-number-citation ref-id="terminator"/>
          </fo:block>
        </fo:static-content>
        
        <!-- Main content area -->
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:block font-family="Times" space-before.optimum="1pt" space-after.optimum="2pt">
              <!-- Start content -->
              <xsl:apply-templates select="entity"/>
            </fo:block>
          </fo:block>
          <!-- end of document marker for total pages -->
          <fo:block id="terminator"/>
        </fo:flow>        
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  
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
  
  <!-- global parameter and variable used when indenting. -->
  <xsl:param name="level-min" select="1"/>  
</xsl:stylesheet>