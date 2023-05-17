<?xml version="1.0" encoding="UTF-8"?>
 <!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
   <!-- END, CR00357205 -->
<!-- BEGIN, CR00352142, PB -->
  <xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">20mm</xsl:attribute>
  </xsl:attribute-set>
  	<xsl:attribute-set name="Normal_2" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">100mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
	</xsl:attribute-set>	  
	<xsl:attribute-set name="Normal_3" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">100mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_4" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">10mm</xsl:attribute>
	<xsl:attribute name="text-indent">100mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_5" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <xsl:attribute name="line-height">5mm</xsl:attribute>
  </xsl:attribute-set>
    <xsl:attribute-set name="Normal_6" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="Normal_7" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="space-before.optimum">10mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_8" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">6mm</xsl:attribute>
	<xsl:attribute name="space-before.optimum">6mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_9" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">6mm</xsl:attribute>
	<xsl:attribute name="space-before.optimum">8mm</xsl:attribute>
    </xsl:attribute-set>
	<!-- END, CR00352142 -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything
 else.-->
    <xsl:apply-templates select="STRUCT[SNAME='ProFormaDocumentData']" />
  </xsl:template>
  <xsl:template match="STRUCT">
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
		<fo:block xsl:use-attribute-sets="Normal_1">Registration Acknowledgement</fo:block>
   <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='userFullName']" />
   </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_3">
            <xsl:apply-templates select="FIELD[FNAME='userAddress']" />
   </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_4">
            <xsl:apply-templates select="FIELD[FNAME='currentDate']" />
   </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_5">
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
          </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_6">
            <xsl:apply-templates select="FIELD[FNAME='concernRoleAddress']" />
          </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_7">
            Dear
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
          </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_8">
     This is to inform you that you have been registered
     as a(n)
            <xsl:apply-templates select="FIELD[FNAME='concernRoleTypeDesc']" />
     with the
            <xsl:apply-templates select="FIELD[FNAME='organisationName']" />
            .
          </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_8">
     The following reference number
            <xsl:apply-templates select="FIELD[FNAME='alternateID']" />
     has been allocated to you and should be used in all
     communication with this organization.
   </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_9">Yours truly,</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_9">
            <xsl:apply-templates select="FIELD[FNAME='userFullName']" />
   </fo:block>
 </fo:flow>
 <!-- END, CR00352142 -->
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
</xsl:stylesheet>

