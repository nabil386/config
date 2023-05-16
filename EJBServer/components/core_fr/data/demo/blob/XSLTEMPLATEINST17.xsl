<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

<!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
<!-- END, CR00357205 -->
  <xsl:output method="xml" indent="yes" />
  <!-- BEGIN, CR00352142, PB -->
  	<xsl:attribute-set name="Normal_1" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">6mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">0mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
   </xsl:attribute-set>
   <!-- END, CR00352142 -->
   <!-- Handles the formatting of the External Party Closure Notification.  -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='ExternalPartyStatusUpdateData']" />
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
           <!-- Build address strings -->
          <xsl:variable name="number">
            <xsl:value-of select="./FIELD[FNAME='externalPartyAddressDtls']/STRUCT[SNAME='DocumentAddress']/FIELD[FNAME='addressLine1']/VALUE" />
          </xsl:variable>
          <xsl:variable name="street">
            <xsl:value-of select="./FIELD[FNAME='externalPartyAddressDtls']/STRUCT[SNAME='DocumentAddress']/FIELD[FNAME='addressLine2']/VALUE" />
          </xsl:variable>
          <xsl:variable name="numberStreet">
            <xsl:value-of select="concat($number,' ',$street)" />
          </xsl:variable>
<!-- BEGIN, CR00352142, PB -->
          <!-- Claimant Name and Address -->
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="./FIELD[FNAME='externalPartyDtls']/STRUCT[SNAME='ConcernRoleNameAndAlternateID']/FIELD[FNAME='concernRoleName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:value-of select="$numberStreet" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="./FIELD[FNAME='externalPartyAddressDtls']/STRUCT[SNAME='DocumentAddress']/FIELD[FNAME='addressLine3']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="./FIELD[FNAME='externalPartyAddressDtls']/STRUCT[SNAME='DocumentAddress']/FIELD[FNAME='city']" />
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
          <!-- Dear .. -->
          <fo:block xsl:use-attribute-sets="Normal_1">
            Madame, Monsieur,
              <xsl:apply-templates select="./FIELD[FNAME='externalPartyDtls']/STRUCT[SNAME='ConcernRoleNameAndAlternateID']/FIELD[FNAME='concernRoleName']" />
            ,
          </fo:block>
          <fo:block linefeed-treatment="preserve" font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
          <!-- Main body -->
          <fo:block xsl:use-attribute-sets="Normal_1">
            Nous souhaitons vous informer que vous n'êtes plus un groupe externe actif auprès de notre agence sous la référence No.
            <xsl:apply-templates select="./FIELD[FNAME='externalPartyDtls']/STRUCT[SNAME='ConcernRoleNameAndAlternateID']/FIELD[FNAME='primaryAlternateID']" />
            à compter du <xsl:apply-templates select="./FIELD[FNAME='modifyDate']" />.
          </fo:block>
          <fo:block linefeed-treatment="preserve" font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
          <!-- Closing block -->
          <fo:block xsl:use-attribute-sets="Normal_1">Cordialement,</fo:block>
          <fo:block linefeed-treatment="preserve" font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_1">
            <xsl:apply-templates select="./FIELD[FNAME='organisationName']" />
          </fo:block>
<!-- END, CR00352142 -->
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:choose>
      <xsl:when test="TYPE='SVR_BOOLEAN'">
        <xsl:choose>
          <xsl:when test="VALUE='false'">Non</xsl:when>
          <xsl:otherwise>Oui</xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="TYPE='SVR_DATE'">
        <xsl:call-template name="format-date">
          <xsl:with-param name="date" select="VALUE" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
        <xsl:value-of select="VALUE" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
  <xsl:template name="format-date">
    <xsl:param name="date" />
    <xsl:variable name="day" select="substring-after(substring-after($date, '-'), '-')" />
    <xsl:variable name="month" select="substring-before(substring-after($date, '-'), '-')" />
    <xsl:variable name="year" select="substring-before($date, '-')" />
    <xsl:choose>
      <xsl:when test="$year!='0001'">
        <xsl:value-of select="concat($month, '/', $day, '/', $year)" />
      </xsl:when>
      <xsl:otherwise>&nbsp;</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>


