<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

<!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
	<!-- END, CR00357205 -->
	<!-- BEGIN, CR00352142, PB -->
  	<xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
	<xsl:attribute name="margin-left">0.0pt</xsl:attribute>
	<xsl:attribute name="space-after">0.0pt</xsl:attribute>
	<xsl:attribute name="space-before">0.0pt</xsl:attribute>
	<xsl:attribute name="text-align">center</xsl:attribute>
	<xsl:attribute name="text-indent">0.0pt</xsl:attribute>
   </xsl:attribute-set>
        <xsl:attribute-set name="Normal_2" foa:class="inline">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
<xsl:attribute name="font-weight">bold</xsl:attribute>
  </xsl:attribute-set>
          <xsl:attribute-set name="Normal_3" foa:class="inline">
    <xsl:attribute name="font-size">8.0pt</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
  </xsl:attribute-set>
            <xsl:attribute-set name="Normal_4" foa:class="inline">
    <xsl:attribute name="font-size">8.0pt</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_5" foa:class="inline">
    <xsl:attribute name="font-size">8.0pt</xsl:attribute>
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
    <xsl:apply-templates select="STRUCT[SNAME='ActionPlanPrintDetails']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ActionPlanPrintDetails']">
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
		<xsl:apply-templates select="FIELD[FNAME='actionPlanDtls']/STRUCT[SNAME='ActionPlanDtls']" />
          <xsl:apply-templates select="FIELD[FNAME='situationDtlsList']" />
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ActionPlanDtls']">
<!-- BEGIN, CR00352142, PB -->
    <fo:block xsl:use-attribute-sets="Normal_1">
      <fo:inline xsl:use-attribute-sets="Normal_2">Action Plan</fo:inline>
    </fo:block>
    <fo:block font-size="20.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:table table-layout="fixed" width="100%">
      <fo:table-column column-width="240pt" />
      <fo:table-column column-width="240pt" />
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Name:
                <xsl:apply-templates select="FIELD[FNAME='name']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
			  <!-- BEGIN, CR00357029, PB -->
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Participants In Agreement:
                <xsl:apply-templates select="FIELD[FNAME='participantsInAgreementInd']" />
                  </fo:inline>
                </fo:block>
				<!-- END, CR00357029 -->
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Start Date:
                <xsl:apply-templates select="FIELD[FNAME='startDate']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Review Date:
                <xsl:apply-templates select="FIELD[FNAME='reviewDate']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Expected End Date:
                <xsl:apply-templates select="FIELD[FNAME='expectedEndDate']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Actual End Date:
                <xsl:apply-templates select="FIELD[FNAME='actualEndDate']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                Outcome:
                <xsl:apply-templates select="FIELD[FNAME='outcome']" />
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block margin-left="5.4pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_4">
        Comments:
        <xsl:apply-templates select="FIELD[FNAME='comments']" />
      </fo:inline>
    </fo:block>
  </xsl:template>
<xsl:template match="FIELD[FNAME='situationDtlsList']">
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <xsl:if test="count(STRUCT_LIST/STRUCT) &gt; 0">
      <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='SituationPrintDetails']" />
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='SituationPrintDetails']"> 
    <fo:block>
      <fo:leader leader-pattern="rule" leader-length="15cm" rule-thickness=".5pt" />
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Situation Category:
        <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='situationCategory']" />
      </fo:inline>
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Situation Requiring Action:
        <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='situationRequiringAction']" />
      </fo:inline>
    </fo:block>
    <fo:block font-size="20.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:table table-layout="fixed" width="100%">
      <fo:table-column column-width="100pt" />
      <fo:table-column column-width="100pt" />
      <fo:table-column column-width="100pt" />
      <fo:table-column column-width="150pt" />
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_3">Expected End Date</fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_3">Actual End Date</fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_3">Outcome</fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_3">Concerning Participants</fo:inline>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='expectedEndDate']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='actualEndDate']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='outcome']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='situationConcernNameList']/STRUCT[SNAME='SituationConcernNameDtlsList']/FIELD[FNAME='concernDtls']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
    <fo:block font-size="20.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <xsl:apply-templates select="FIELD[FNAME='allegationDtlsList']" />
    <xsl:apply-templates select="FIELD[FNAME='actionDtlsList']" />
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Comments:
        <xsl:apply-templates select="FIELD[FNAME='situationDtls']/STRUCT[SNAME='SituationDtls']/FIELD[FNAME='comments']" />
      </fo:inline>
    </fo:block>
  </xsl:template>
  <xsl:template match="FIELD[FNAME='concernDtls']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) &gt; 0">
      <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='SituationConcernNameDtls']" />
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='SituationConcernNameDtls']">    
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_4">
        <xsl:apply-templates select="FIELD[FNAME='situationConcernName']" />
      </fo:inline>
    </fo:block>
    </xsl:template>    
  <xsl:template match="FIELD[FNAME='allegationDtlsList']">
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <xsl:if test="count(STRUCT_LIST/STRUCT) &gt; 0">
      <fo:table table-layout="fixed" width="100%">
        <fo:table-column column-width="150pt" />
        <fo:table-column column-width="150pt" />
        <fo:table-column column-width="150pt" />
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Allegation Type</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Allegation Date</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Allegation Location</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
      <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='AllegationDtls']" />
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='AllegationDtls']">   
    <fo:table table-layout="fixed" width="100%">
      <fo:table-column column-width="150pt" />
      <fo:table-column column-width="150pt" />
      <fo:table-column column-width="150pt" />
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='type']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='allegationDateTime']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" border-start-style="solid" border-start-width="0.5pt" border-end-style="solid" border-end-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
            <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
              <fo:inline xsl:use-attribute-sets="Normal_4">
                <xsl:apply-templates select="FIELD[FNAME='location']" />
              </fo:inline>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
     </fo:table-body>
     </fo:table>
    </xsl:template>
  <xsl:template match="FIELD[FNAME='actionDtlsList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) &gt; 0">
      <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ActionPrintDetails']" />
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ActionPrintDetails']">               
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Action:
        <xsl:apply-templates select="FIELD[FNAME='actionDtls']/STRUCT[SNAME='ActionIDActionDtls']/FIELD[FNAME='action']" />
      </fo:inline>
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Action Owners:
        <xsl:apply-templates select="FIELD[FNAME='ActionOwnersString']" />
      </fo:inline>
    </fo:block>
    <!-- END, CR00352142 -->
  </xsl:template>
              <xsl:template match="FIELD">
    <xsl:choose>
      <xsl:when test="TYPE='SVR_BOOLEAN'">
        <xsl:choose>
          <xsl:when test="VALUE='false'">No</xsl:when>
          <xsl:otherwise>Yes</xsl:otherwise>
        </xsl:choose>
          </xsl:when>
      <xsl:when test="TYPE='SVR_DATE'">
        <xsl:choose>
          <xsl:when test="VALUE='0001-01-01'" />
          <xsl:otherwise>
            <xsl:value-of select="VALUE" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="VALUE" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>

