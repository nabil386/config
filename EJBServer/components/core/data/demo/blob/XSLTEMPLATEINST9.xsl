<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

 <!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
   <!-- END, CR00357205 -->
  <!-- BEGIN, CR00352142, PB -->
<xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">110mm</xsl:attribute>
   </xsl:attribute-set>
  	  		    <xsl:attribute-set name="Normal_2" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">110mm</xsl:attribute>
<xsl:attribute name="white-space-collapse">false</xsl:attribute>
<xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
<xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
<xsl:attribute name="text-align">right</xsl:attribute>
</xsl:attribute-set>
  <xsl:attribute-set name="Normal_3" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_4" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="line-height">15mm</xsl:attribute>
	<xsl:attribute name="text-indent">0mm</xsl:attribute>
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
  <xsl:template match="STRUCT">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="only" page-height="297mm" page-width="210mm" margin-top="30mm" margin-bottom="30mm" margin-left="30mm" margin-right="30mm">
          <fo:region-body />
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="only">
	    <!-- START NON-TRANSLATABLE -->
        <fo:flow flow-name="xsl-region-body" font-family="WT Sans">
        <!-- END NON-TRANSLATABLE -->
<!-- BEGIN, CR00352142, PB -->
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='concernRoleAddress']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="FIELD[FNAME='currentDate']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_4">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">Please confirm that the following information that you provided
            to this agency is correct.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_4">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Client Name:
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Last Employer:
            <xsl:apply-templates select="FIELD[FNAME='lastEmployerName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Last Worked:
            <xsl:apply-templates select="FIELD[FNAME='dateLastWorked']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Unemployment Reason:
            <xsl:apply-templates select="FIELD[FNAME='lastReasonCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Next to Last Employer:
            <xsl:apply-templates select="FIELD[FNAME='nextToLastEmployerName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Unemployment Reason:
            <xsl:apply-templates select="FIELD[FNAME='nextToLastReasonCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Available for Work:
            <xsl:apply-templates select="FIELD[FNAME='availableForWorkInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Capable of work:
            <xsl:apply-templates select="FIELD[FNAME='capableOfWorkInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            In Receipt of Other Benefits:
            <xsl:apply-templates select="FIELD[FNAME='receiptOfOtherBenefitsInd']" />
          </fo:block>
          <xsl:if test="FIELD[FNAME='receiptOfOtherBenefitsInd' and VALUE='1']">
            <fo:block xsl:use-attribute-sets="Normal_6">
              Details:
              <xsl:apply-templates select="FIELD[FNAME='otherBenefitDescription']" />
            </fo:block>
            <fo:block xsl:use-attribute-sets="Normal_6">
              Amount:
              <xsl:apply-templates select="FIELD[FNAME='otherBenefitAmount']" />
            </fo:block>
          </xsl:if>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Applied for Benefit in Another State:
            <xsl:apply-templates select="FIELD[FNAME='appliedInAnotherStateInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            State:
            <xsl:apply-templates select="FIELD[FNAME='stateCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Resident of This State:
            <xsl:apply-templates select="FIELD[FNAME='clientResidentInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Ethnicity:
            <xsl:apply-templates select="FIELD[FNAME='ethnicityCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Veteran:
            <xsl:apply-templates select="FIELD[FNAME='veteranInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Union Member:
            <xsl:apply-templates select="FIELD[FNAME='unionMemberInd']" />
          </fo:block>
          <xsl:if test="FIELD[FNAME='unionMemberInd' and VALUE='1']">
            <fo:block xsl:use-attribute-sets="Normal_6">
              Union Details:
              <xsl:apply-templates select="FIELD[FNAME='unionMemberDescription']" />
            </fo:block>
          </xsl:if>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">I declare that all the details supplied are true and complete
             and that I undertake to notify the department if I get employed
             or if there is any change in the details given.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">Please sign here:</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
<!-- END, CR00352142 -->
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:choose>
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
</xsl:stylesheet>

