<?xml version="1.0"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2004,2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
  
  This XSLT merges a codetable file; the main codetable file, with a delta codetable file;
  the merge file, to produce a new codetable file. Duplicate codetable names are copied
  depending on precedence; the main codetable file has the highest precedence.
  All locales are merged, any duplicates in the merge file are ignored. All the views 
  and codes are  merged avoiding duplicates. Customers are allowed to customize the OOTB
  codetables by setting the overwrite indicator on the custom codetable (main codetable). 
  If the overwrite indicator is set to true on a view in the custom codetable file, the 
  view overwrite the views in delta and merge files that has the same context thus leaving
  the finally merged codetable with the view as in custom codetable. The other views in the
  custom codetable that do not have overwrite indicator set are merged normally without 
  overwriting the child codes. 
  
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:param name="mergeFileName"/>
  <xsl:preserve-space elements="codetables locale codetable code annotation codetabledata description comments views view"/>
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

  <!-- Assign the main source file -->
  <xsl:variable name="mainfileroot" select="/"/>

  <!--Load the merge file -->
  <xsl:variable name="mergefile" select="document($mergeFileName)"/>

  <!-- Assign the list of codetable names from the main codetable file -->
  <xsl:variable name="mainCodetableList" select="$mainfileroot/codetables/codetable"/>

  <!-- Start to combine the files -->
  <xsl:template match="codetables">
    <xsl:copy>
      <!-- Copy the attributes from the main codetable file codetables tag including the schema -->
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*|*">
    <xsl:copy-of select="."/>
  </xsl:template>

  <!-- Need to test for displaynames element not existing in the mainfile but existing in the merge
       file. If it exists in the merge file, then copy the details over. -->

  <xsl:template name="new_displaynames">
    <xsl:param name="this-codetable"/>
    <xsl:if test="not(displaynames) and $mergefile//codetables/codetable[@name = $this-codetable/@name]/displaynames">
      <xsl:copy-of select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/displaynames"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="codetable" name="codetable">
    <!-- Copy the codetable tag and its attributes in the main file. -->
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates select="displaynames">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:apply-templates>
      <xsl:call-template name="new_displaynames">
         <xsl:with-param name="this-codetable" select="."/>
      </xsl:call-template>
      <xsl:apply-templates select="codetabledata">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:apply-templates>
       <xsl:call-template name="new_codetabledata">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:call-template>
      <xsl:apply-templates select="code">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:apply-templates>
      <xsl:call-template name="new_code">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:call-template>
      <xsl:apply-templates select="views">
        <xsl:with-param name="this-codetable" select="."/>
      </xsl:apply-templates>      
    </xsl:copy>
  </xsl:template>


 <!-- Copy the display names elements from the main file. -->
  <xsl:template match="displaynames">
    <xsl:param name="this-codetable"/>

    <xsl:copy>
      <xsl:apply-templates select="name|locale"/>
      <xsl:call-template name="new_name">
        <xsl:with-param name="this-codetable" select="$this-codetable"/>
        <xsl:with-param name="this-displayname" select="."/>
      </xsl:call-template>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="name">
    <xsl:copy-of select = "."/>
  </xsl:template>

  <!-- Search for new names within the mergefile and copy to the mainfile if they dont already
       exist. -->

  <xsl:template name="new_name">
    <xsl:param name="this-codetable"/>
    <xsl:param name="this-displayname"/>

    <!-- iterate through each name element and select if the language attribute is not equal to
         the language attribute in the mainfile OR if the language attributes are equal, then the
         country attribute must be present and not equal. -->

    <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/displaynames/name[(not(@language = $this-codetable/displaynames/name/@language)) or ((@language = $this-codetable/displaynames/name/@language) and (not(@country = $this-codetable/displaynames/name/@country) and (@country)))]
        | $mergefile//codetables/codetable[@name = $this-codetable/@name]/displaynames/locale[(not(@language = $this-codetable/displaynames/locale/@language)) or ((@language = $this-codetable/displaynames/locale/@language) and (not(@country = $this-codetable/displaynames/locale/@country) and (@country)))]">
      <xsl:copy-of select="."/>

    </xsl:for-each>
  </xsl:template>


  <xsl:template match="code">
    <xsl:param name="this-codetable"/>
    <xsl:variable name="this-code" select="." />

    <xsl:copy>
      <xsl:copy-of select="@*"/>

      <!-- Add every locale in a code from the main file. -->
      <xsl:for-each select="$this-code/locale">
        <xsl:copy-of select="."/>
      </xsl:for-each>

      <!-- Search for codes in the merge file of the same name, in the same codetable,
           and add any new locale entries that exist in them, only if the locale does not exist
           already in the main file. -->
      <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/code[@value = $this-code/@value]/locale">
        <!-- Test to see if the locale is not already in the main file -->
        <xsl:if test="not($this-code/locale[((@language = current()/@language)
            and ((not(@country) and not(current()/@country)) or
                 (not(@country) and current()/@country='') or
                 (@country='' and not(current()/@country)) or
                 (@country = current()/@country)))])">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

    </xsl:copy>
  </xsl:template>

  <!-- Search for new codes in the merge file and add them. -->
  <xsl:template name="new_code">
    <!-- Pass in the codetable in which the new code will be added -->
    <xsl:param name="this-codetable"/>

    <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/code[not(@value = $this-codetable/code/@value)]">
      <xsl:copy>
        <!-- Only copy the default attribute if there isn't already a default attribute of true
             in the codetable or if the default is false -->
        <xsl:if test="((current()[@default = 'true'])
                         and (not($this-codetable/code[@default = 'true'])))
                       or (current()[@default = 'false'])">
          <xsl:copy-of select="@default"/>
        </xsl:if>

        <xsl:copy-of select="@java_identifier"/>
        <xsl:copy-of select="@status"/>
        <xsl:copy-of select="@value"/>
        <xsl:copy-of select="@removed"/>
        <xsl:copy-of select="@parent_code"/>

        <xsl:for-each select="./locale">
          <xsl:copy-of select="."/>
        </xsl:for-each>
      </xsl:copy>
    </xsl:for-each>
  </xsl:template>

  <!-- This gets executed if the main file has codetabledata element. -->

  <xsl:template match="codetabledata">
    <xsl:param name="this-codetable"/>
    <xsl:variable name="this-codetabledata" select="." />
    <xsl:copy>
    <!-- Add every locale in a codetabledata from the main file. -->
      <xsl:for-each select="$this-codetabledata/locale">
        <xsl:copy-of select="."/>
      </xsl:for-each>
      <!-- Search for new locales in the merge file,
           and add any new locale entries that exist in them, only if the locale does not exist
           already in the main file. -->
      <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/codetabledata/locale">
        <!-- Test to see if the locale is not already in the main file -->
        <xsl:if test="not($this-codetabledata/locale[((@language = current()/@language)
          and ((not(@country) and not(current()/@country)) or (@country = current()/@country)))])">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

    </xsl:copy>
  </xsl:template>

  <!-- Need to test for codetabledata element not existing in the mainfile but existing in the merge
       file. If it exists in the merge file, then copy the details over. -->

  <xsl:template name="new_codetabledata">
    <xsl:param name="this-codetable"/>
    <xsl:if test="not(codetabledata) and $mergefile//codetables/codetable[@name = $this-codetable/@name]/codetabledata">
      <xsl:copy-of select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/codetabledata"/>
    </xsl:if>
  </xsl:template>
    
  <!-- Copy the views from the main file. -->
  <xsl:template match="views">
    <xsl:param name="this-codetable"/>
    
    <xsl:variable name="this-views" select="." />
    <xsl:variable name="this-view" select="$this-views/view" />
    
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      
      <!-- Iterate through each view in the main file and copy along with the code elements. -->
      <xsl:for-each select="$this-views/view">
        
        <xsl:copy>
          <xsl:copy-of select="@*"/>
          
          <xsl:variable name="currentView" select="." />
          
          <xsl:variable name="mergeView" 
            select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/views/view[@context = $currentView/@context]"/>
         
          <!-- Checking if a view in the main file does not contain overwrite set to true 
          and similar view in the merge file set to true, set the overwrite to true for the view in
          the main file and continue to add child codes for this pass-->
          <xsl:if test="not($currentView[@overwrite = 'true']) and 
            $mergeView[@overwrite = 'true']">
            <xsl:attribute name="overwrite">true</xsl:attribute>
          </xsl:if>

          <!-- Check if a view in the main file does not have overwrite set to true and the
            default_code sttribute is not set, then copy the default_code from the merge file.-->
          <xsl:if test="not($currentView[@overwrite = 'true']) and 
            not($currentView/@default_code) and $mergeView/@default_code">
            <xsl:copy-of select="$mergeView/@default_code"/>
          </xsl:if>

          <xsl:copy-of select="./code"/>
         
          <!-- Check if the overwrite indicator is set to true on the view in the main codetable and only
          perform merging the codes with from the merge codetable the if its set to false -->
          <xsl:if test="not($currentView[@overwrite = 'true'])">
            <xsl:apply-templates select=".">
              <xsl:with-param name="this-codetable" select="$this-codetable"/>
              <xsl:with-param name="this-view" select="."/>
            </xsl:apply-templates>
          </xsl:if>
        </xsl:copy>
      </xsl:for-each>
      
      <!-- Call the template that checks for the views that do not exist in the mainfile but exist in the merge
      file. If any exist in the merge file, then copy them over. -->
      <xsl:call-template name="new_view">
        <xsl:with-param name="this-codetable" select="$this-codetable"/>
      </xsl:call-template>
    </xsl:copy>
  </xsl:template>

  <!-- Search the view for new codes in the merge codetable.-->
  <xsl:template match="view">
    <xsl:param name="this-codetable"/>
    <xsl:param name="this-view"/>
    
    <!-- Search for the new codes in the view in the merge file that do not 
     exist in the main file and copy them over. -->
    <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/views/view[@context = $this-view/@context]/code">
      <xsl:if test="not($this-view/code[@value = current()/@value])">
        <xsl:copy-of select="."/>
      </xsl:if>
    </xsl:for-each>  
  </xsl:template>

   <!-- Check for the views that do not exist in the mainfile but exist in the merge
       file. If any exist in the merge file, then copy them over. -->
  <xsl:template name="new_view">
    <xsl:param name="this-codetable"/>
    <xsl:for-each select="$mergefile//codetables/codetable[@name = $this-codetable/@name]/views/view[not(@context = $this-codetable/views/view/@context)]">
      <xsl:copy-of select="."/>
    </xsl:for-each>  
  </xsl:template>

</xsl:stylesheet>
