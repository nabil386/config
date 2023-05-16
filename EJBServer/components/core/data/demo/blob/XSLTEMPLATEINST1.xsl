<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

 <!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
   <!-- END, CR00357205 -->
  <!-- BEGIN, CR00352142, PB -->
	<xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
  	  		    <xsl:attribute-set name="Normal_2" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">110mm</xsl:attribute>
<xsl:attribute name="white-space-collapse">false</xsl:attribute>
<xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
<xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
</xsl:attribute-set>
  <xsl:attribute-set name="Normal_3" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
  </xsl:attribute-set>
    <xsl:attribute-set name="Normal_4" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">15mm</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_5" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">7mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
<xsl:attribute name="white-space-collapse">false</xsl:attribute>
<xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
<xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
</xsl:attribute-set>
    <xsl:attribute-set name="Normal_6" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">7mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
  </xsl:attribute-set>
      <xsl:attribute-set name="Normal_7" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="text-indent">2mm</xsl:attribute>
  </xsl:attribute-set>
        <xsl:attribute-set name="Normal_8" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
  </xsl:attribute-set>
      <xsl:attribute-set name="Normal_9" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">2mm</xsl:attribute>
  </xsl:attribute-set>
      <xsl:attribute-set name="Normal_10" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
  </xsl:attribute-set> 
  <!-- END, CR00352142 -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <!-- to do: change SNAME name -->
    <xsl:apply-templates select="STRUCT[SNAME='ProFormaDocumentData']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ProFormaDocumentData']">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="only" page-height="297mm" page-width="210mm" margin-top="30mm" margin-bottom="30mm" margin-left="30mm" margin-right="30mm">
          <fo:region-body />
         </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="only">
        <!-- BEGIN, CR00352142, PB -->
        <!-- START NON-TRANSLATABLE -->
        <fo:flow flow-name="xsl-region-body" font-family="WT Sans">
        <!-- END NON-TRANSLATABLE -->
          <fo:block  xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="FIELD[FNAME='organisationName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='userAddress']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_4">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_4">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_5">
            <xsl:apply-templates select="FIELD[FNAME='concernRoleAddress']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Dear
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            This contract is an agreement between the client
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
            and the
            <xsl:apply-templates select="FIELD[FNAME='organisationName']" />
            .
            The client agrees to perform the following tasks in return
            for benefits under his/her Integrated Case with ID
            <xsl:apply-templates select="FIELD[FNAME='caseID']" />
            .
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:table width="100%" table-layout="fixed">
            <fo:table-column column-number="1" column-width="proportional-column-width(20)" />
            <fo:table-column column-number="2" column-width="proportional-column-width(70)" />
            <fo:table-column column-number="3" column-width="proportional-column-width(50)" />
            <fo:table-body>
              <fo:table-row line-height="5mm">
                <fo:table-cell column-number="1">
                  <fo:block xsl:use-attribute-sets="Normal_7">Task ID</fo:block>
                </fo:table-cell>
                <fo:table-cell column-number="2">
                  <fo:block xsl:use-attribute-sets="Normal_8">Task Description</fo:block>
                </fo:table-cell>
                <fo:table-cell column-number="3">
                  <fo:block xsl:use-attribute-sets="Normal_8">Due Date</fo:block>
                </fo:table-cell>
              </fo:table-row>
              <xsl:apply-templates select="FIELD[FNAME='dtls']" />
            </fo:table-body>
          </fo:table>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            <xsl:apply-templates select="FIELD[FNAME='userFullName']" />
            : ___________________
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
            : ___________________
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">Sincerely,</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            <xsl:apply-templates select="FIELD[FNAME='userFullName']" />
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
<xsl:template match="STRUCT_LIST">
    <xsl:apply-templates select="STRUCT[SNAME='CaseContractTaskDtls']" />
</xsl:template>
<xsl:template match="STRUCT[SNAME='CaseContractTaskDtls']">
  <fo:table-row line-height="5mm">
    <fo:table-cell column-number="1">
        <fo:block xsl:use-attribute-sets="Normal_9">
          <xsl:apply-templates select="FIELD[FNAME='taskID']" />
      </fo:block>
    </fo:table-cell>
    <fo:table-cell column-number="2">
      <fo:block xsl:use-attribute-sets="Normal_10">
          <xsl:apply-templates select="FIELD[FNAME='taskDescription']" />
      </fo:block>
    </fo:table-cell>
    <fo:table-cell column-number="3">
      <fo:block xsl:use-attribute-sets="Normal_10">
          <xsl:apply-templates select="FIELD[FNAME='dueDate']" />
      </fo:block>
    </fo:table-cell>
  </fo:table-row>
</xsl:template>
<!-- END, CR00352142 -->
 <xsl:template match="FIELD">
    <xsl:choose>
      <xsl:when test="FNAME='dtls'">
        <xsl:apply-templates select="STRUCT_LIST" />
      </xsl:when>
      <xsl:when test="TYPE='SVR_BOOLEAN'">
        <xsl:choose>
          <xsl:when test="VALUE='false'">No</xsl:when>
          <xsl:otherwise>Yes</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="VALUE" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="NewTemplate">
    <xsl:apply-templates />
  </xsl:template>
</xsl:stylesheet>

