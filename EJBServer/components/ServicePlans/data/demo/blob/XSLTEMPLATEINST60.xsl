<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

<!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
	<!-- END, CR00357205 -->
  <!-- BEGIN, CR00352142, PB -->
	<xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
   </xsl:attribute-set>
 	<xsl:attribute-set name="Normal_2" foa:class="inline">
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
  </xsl:attribute-set>
 	<xsl:attribute-set name="Normal_3" foa:class="inline">
    <xsl:attribute name="font-size">8.0pt</xsl:attribute>
  </xsl:attribute-set>
 	<xsl:attribute-set name="Normal_4" foa:class="block">
    <xsl:attribute name="font-size">10.0pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
   </xsl:attribute-set>
    	<xsl:attribute-set name="Normal_5" foa:class="inline">
    <xsl:attribute name="font-size">8.0pt</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
   </xsl:attribute-set>
   <!-- END, CR00352142 -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='ServicePlanDocumentData']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ServicePlanDocumentData']">
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
          <fo:block>
            <xsl:apply-templates select="FIELD[FNAME='contractDetails']/STRUCT[SNAME='ServicePlanContractDetails']" />
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ServicePlanContractDetails']">
<!-- BEGIN, CR00352142, PB -->
    <fo:block xsl:use-attribute-sets="Normal_1" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="center" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_2">Service Plan Contract</fo:inline>
    </fo:block>
    <fo:block font-size="20.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block wrap-option="wrap" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Client Name:
        <xsl:apply-templates select="FIELD[FNAME='clientName']" />
      </fo:inline>
      <fo:inline xsl:use-attribute-sets="Normal_3">
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Social Security Number:
        <xsl:apply-templates select="FIELD[FNAME='ssnNumber']" />
      </fo:inline>
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Contract Date:
        <xsl:apply-templates select="FIELD[FNAME='contractDate']" />
      </fo:inline>
      <fo:inline xsl:use-attribute-sets="Normal_3">
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Plan Reference Number:
        <xsl:apply-templates select="FIELD[FNAME='caseReference']" />
      </fo:inline>
    </fo:block>
    <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        Plan Goal:
        <xsl:apply-templates select="FIELD[FNAME='plannedGoalName']" />
      </fo:inline>
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        <xsl:apply-templates select="FIELD[FNAME='goalContractText']" />
      </fo:inline>
      <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    </fo:block>
    <xsl:if test="count(FIELD[FNAME='ObjectiveSubGoalDetails']/STRUCT[SNAME='ObjectivePlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']/STRUCT_LIST/STRUCT) &gt; 0">
      <fo:block xsl:use-attribute-sets="Normal_4" border="1px solid blue" background-color="white" text-align="center">Objectives</fo:block>
      <fo:block font-size="5.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
      <xsl:apply-templates select="FIELD[FNAME='ObjectiveSubGoalDetails']/STRUCT[SNAME='ObjectivePlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']" />
    </xsl:if>
    <xsl:if test="count(FIELD[FNAME='BarrierSubGoalDetails']/STRUCT[SNAME='BarrierPlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']/STRUCT_LIST/STRUCT) &gt; 0">
	<!-- BEGIN, CR00357029, PB -->
      <fo:block xsl:use-attribute-sets="Normal_4" border="1px solid blue" background-color="white" text-align="center">Barriers</fo:block>
	  <!-- END, CR00357029 -->
      <fo:block font-size="5.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
      <xsl:apply-templates select="FIELD[FNAME='BarrierSubGoalDetails']/STRUCT[SNAME='BarrierPlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']" />
    </xsl:if>
    <xsl:if test="count(FIELD[FNAME='NeedSubGoalDetails']/STRUCT[SNAME='NeedPlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']/STRUCT_LIST/STRUCT) &gt; 0">
      <fo:block xsl:use-attribute-sets="Normal_4" border="1px solid blue" background-color="white" text-align="center">Needs</fo:block>
      <fo:block font-size="5.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
      <xsl:apply-templates select="FIELD[FNAME='NeedSubGoalDetails']/STRUCT[SNAME='NeedPlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']" />
    </xsl:if>
    <xsl:if test="count(FIELD[FNAME='OtherSubGoalDetails']/STRUCT[SNAME='OtherPlannedSubGoalDetails']/FIELD[FNAME='subGoalDtlsForContractList']/STRUCT_LIST/STRUCT) &gt; 0">
      <xsl:apply-templates select="FIELD[FNAME='OtherSubGoalDetails']/STRUCT[SNAME='OtherPlannedSubGoalDetails']" />
    </xsl:if>
    <fo:block font-size="15.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block font-size="15.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block font-size="15.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block font-size="15.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
  <!-- BEGIN, CR00123339, ANK -->
  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">    
      <xsl:if test="count(FIELD[FNAME='servicePlanSignatories']/STRUCT[SNAME='ServicePlanSignatoriesList']/FIELD[FNAME='concernRoleList']/STRUCT_LIST/STRUCT) &gt; 0">
          <fo:block font-size="12pt">
          <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="proportional-column-width(50)" />
            <fo:table-column column-width="proportional-column-width(50)" />
            <fo:table-body>
              <xsl:apply-templates select="FIELD[FNAME='servicePlanSignatories']/STRUCT[SNAME='ServicePlanSignatoriesList']/FIELD[FNAME='concernRoleList']/STRUCT_LIST/STRUCT[SNAME='ConcernRoleNameDetails']" />
            </fo:table-body>
          </fo:table>
        </fo:block>
      </xsl:if>
    </fo:block>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='ConcernRoleNameDetails']">
    <fo:table-row>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">Signature:___________________________</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">Date:____________________</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    <fo:table-row>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">
            (
            <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
            ) &nbsp;
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    <fo:table-row>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  <!-- END, CR00123339 -->
   <xsl:template match="STRUCT[SNAME='OtherPlannedSubGoalDetails']"> 
       <xsl:for-each select="FIELD[FNAME='subGoalDtlsForContractList']/STRUCT_LIST/STRUCT[SNAME='PlannedSubGoalDetailsListForContract'][not(FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalType']/VALUE=preceding-sibling::STRUCT[SNAME='PlannedSubGoalDetailsListForContract']/FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalType']/VALUE)]"> 
         <xsl:sort select="FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalType']/VALUE" /> 
      <fo:block xsl:use-attribute-sets="Normal_4" border="1px solid blue" background-color="white" text-align="center">
        <xsl:value-of select="FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalTypeName']/VALUE" />
      </fo:block>
      <fo:block font-size="5.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
         <xsl:for-each select="../node()[FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalType']/VALUE=current()/FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalType']/VALUE]"> 
        <xsl:apply-templates select="current()" />
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="FIELD[FNAME='subGoalDtlsForContractList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) &gt; 0">
      <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='PlannedSubGoalDetailsListForContract']" />
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='PlannedSubGoalDetailsListForContract']">
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">
        <xsl:apply-templates select="FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalName']" />
      </fo:inline>
      <fo:block font-size="1.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
      <fo:inline xsl:use-attribute-sets="Normal_3">
        <xsl:apply-templates select="FIELD[FNAME='plannedSubGoalDetails']/STRUCT[SNAME='PlannedSubGoalDetailsForContract']/FIELD[FNAME='subGoalContractText']" />
      </fo:inline>
    </fo:block>
    <fo:block font-size="8.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    <fo:block font-size="8.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <xsl:apply-templates select="FIELD[FNAME='planItemDtlsForContract']/STRUCT[SNAME='PlannedItemDtlsForContractList']" />
    </fo:block>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='PlannedItemDtlsForContractList']">
    <xsl:if test="count(FIELD[FNAME='dtls']/STRUCT_LIST/STRUCT) &gt; 0">
      <fo:block font-size="12pt">
        <fo:table table-layout="fixed" width="100%">
          <fo:table-column column-width="proportional-column-width(50)" />
          <fo:table-column column-width="proportional-column-width(50)" />
          <fo:table-column column-width="proportional-column-width(50)" />
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_5">Plan Item</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_5">Start</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_5">End</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <xsl:apply-templates select="FIELD[FNAME='dtls']/STRUCT_LIST/STRUCT[SNAME='PlannedItemDtlsForContract']" />
          </fo:table-body>
        </fo:table>
      </fo:block>
      <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
    </xsl:if>
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='PlannedItemDtlsForContract']">
    <fo:table-row>
      <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">
            <xsl:apply-templates select="FIELD[FNAME='plannedItemName']" />
            &nbsp;
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">
            <xsl:apply-templates select="FIELD[FNAME='plannedItemExpectedStartDate']" />
            &nbsp;
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3">
            <xsl:apply-templates select="FIELD[FNAME='plannedItemExpectedEndDate']" />
            &nbsp;
          </fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    <!-- BEGIN, CR00265232, CSH -->
    <fo:table-row>
      <fo:table-cell number-columns-spanned="3">  
        <fo:table table-layout="fixed" width="100%">
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt" number-columns-spanned="3">            
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_3">
                    <xsl:apply-templates select="FIELD[FNAME='planItemContractText']" />
                    &nbsp;
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" padding-left="2.4pt" padding-right="2.4pt" number-columns-spanned="3">            
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_5">Comments&nbsp;:&nbsp;</fo:inline>
                  <fo:inline xsl:use-attribute-sets="Normal_3">
                    <xsl:apply-templates select="FIELD[FNAME='comments']" />
                    &nbsp;
                  </fo:inline>
                  <fo:block font-size="10.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">&nbsp;</fo:block>
                </fo:block>
				<!-- END, CR00352142 -->
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
      </fo:table-cell>      
    </fo:table-row>    
    <!-- END, CR00265232 -->
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

