<?xml version='1.0' encoding="UTF-8" standalone='yes'?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>
<!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  version='1.0' xmlns:foa="http://fabio">
  <!-- END, CR00357205 -->
<!-- BEGIN, CR00352142, PB -->
      <xsl:attribute-set name="Normal_1" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  
     <xsl:attribute-set name="Normal_2" foa:class="inline">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">14.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  
       <xsl:attribute-set name="Normal_3" foa:class="inline">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->

  </xsl:attribute-set>
   
       <xsl:attribute-set name="Normal_4" foa:class="inline">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <!-- END, CR00352142 -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA"/>
  </xsl:template>
  
  
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='IncomeSupportDocumentData']"/>
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='IncomeSupportDocumentData']">
  
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      
      <fo:layout-master-set>
        <fo:simple-page-master master-name="only"
                               page-height="297mm"
                               page-width="210mm"
                               margin-top="30mm"
                               margin-bottom="30mm"
                               margin-left="30mm"
                               margin-right="30mm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
        
      <fo:page-sequence master-reference="only">
        
        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates select="FIELD[FNAME='isDetails']"/>
          <xsl:apply-templates select="FIELD[FNAME='householdEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='hHoldRshipEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='hHoldBenefitEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='medicaidEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='incomeEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='resourceEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='medicalExpenseEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='shelterExpenseEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='depCareExpEvidenceDataList']"/>
          <xsl:apply-templates select="FIELD[FNAME='eaEvidenceData']"/>
        </fo:flow>
                
      </fo:page-sequence>
      
    </fo:root>  
  
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='isDetails']">
    <xsl:apply-templates select="STRUCT[SNAME='IncomeSupportDetails']"/>
  </xsl:template>

  
  
  <xsl:template match="STRUCT[SNAME='IncomeSupportDetails']">
  <!-- BEGIN, CR00352142, PB -->
    <fo:block xsl:use-attribute-sets="Normal_1" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="center" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_2">Rapport de l'interview avec <xsl:apply-templates select="FIELD[FNAME='applicantTitle']"/>&nbsp;<xsl:apply-templates select="FIELD[FNAME='applicantFirstName']"/>&nbsp;<xsl:apply-templates select="FIELD[FNAME='applicantSurname']"/></fo:inline>
    </fo:block>
    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      &nbsp;
    </fo:block>
    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      <fo:inline xsl:use-attribute-sets="Normal_3">Vous avez déclaré les informations suivantes :-</fo:inline>
    </fo:block>
      
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='householdEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="70mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="3" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Membres du ménage</fo:inline>
                </fo:block>
               </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Statut matrimonial</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Date de naissance</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISHouseholdEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
    
    </xsl:if>
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISHouseholdEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='maritalStatus']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='dateOfBirth']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
              
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='hHoldRshipEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="20mm"/>
          <fo:table-column column-width="50mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="50mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Relations du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Tuteur non parent</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type de relation</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
      
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISHholdRshipEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISHholdRshipEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='nonParentCaretakerRelInd']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='relationshipType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='relParticipantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
              
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='hHoldBenefitEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="70mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="3" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Prestations du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type de prestation</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Numéro de dossier</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISHholdBenefitEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
    
    </xsl:if>
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISHholdBenefitEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='benefitType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='benefitCaseNo']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='medicaidEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="60mm"/>
          <fo:table-column column-width="45mm"/>
          <fo:table-column column-width="45mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="3" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Détails relatifs à l'aide médicale</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nouveau-né d'une mère recevant une aide médicale</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Perte du régime d'assurance-maladie partie A</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
      
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISMedicaidEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>    
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISMedicaidEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='newbornToMAMotherInd']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='lostMedicarePartAInd']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='incomeEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="50mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Revenus du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Fréquence</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Montant ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
           
           <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISIncomeEvidenceData']"/>
           
         </fo:table-body>
       </fo:table>
     </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISIncomeEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='incomeType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='incomeFrequency']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='incomeAmount']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='resourceEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="50mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Ressources du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Valeur totale ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Total gagné ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISResourceEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISResourceEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='assetType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='resourceAmount']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='resourceAmountOwed']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='medicalExpenseEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
      
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="50mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Dépenses médicales du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Fréquence</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Montant ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISMedicalExpenseEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISMedicalExpenseEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='medicalExpenseType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='medicalExpenseFrequency']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='medicalExpenseAmount']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='shelterExpenseEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="50mm"/>
          <fo:table-column column-width="40mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Dépenses de logement du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Type</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Fréquence</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Montant ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
      
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISShelterExpenseEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISShelterExpenseEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='shelterExpenseType']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='shelterExpenseFrequency']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='shelterExpenseAmount']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='depCareExpEvidenceDataList']">
    <xsl:if test="count(STRUCT_LIST/STRUCT) > 0">
    
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
        &nbsp;
      </fo:block>
      <fo:block font-size="12pt">
        <fo:table>
          <fo:table-column column-width="45mm"/>
          <fo:table-column column-width="45mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-column column-width="30mm"/>
          <fo:table-body>
            <fo:table-row>
              <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="4" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Dépenses de soins de dépendance du ménage</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Nom</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Dépendant</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Fréquence</fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
                  <fo:inline xsl:use-attribute-sets="Normal_4">Montant ($)</fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
      
            <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='ISDepCareExpEvidenceData']"/>
            
          </fo:table-body>
        </fo:table>
      </fo:block>
      
    </xsl:if>  
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='ISDepCareExpEvidenceData']">
  
    <fo:table-row>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='participantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='depParticipantRoleName']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='depCareExpenseFrequency']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
        <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="end" text-indent="0.0pt">
          <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='depCareExpenseAmount']"/>&nbsp;</fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
    
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='eaEvidenceData']">
    <xsl:apply-templates select="STRUCT[SNAME='ISEAEvidenceData']"/>
  </xsl:template>

  
  
  <xsl:template match="STRUCT[SNAME='ISEAEvidenceData']">
  
    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      &nbsp;
    </fo:block>
    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
      &nbsp;
    </fo:block>
    <fo:block font-size="12pt">
      <fo:table>
        <fo:table-column column-width="130mm"/>
        <fo:table-column column-width="20mm"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" number-columns-spanned="2" padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_4">Besoin(s) d'urgence applicables au ménage</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <fo:table-row>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Empêcher l'expulsion</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='preventEvictionInd']"/>&nbsp;</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <fo:table-row>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Assistance locative à court terme</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='shortTermRentInd']"/>&nbsp;</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <fo:table-row>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Empêcher la coupure des services collectifs</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='utilityShutOffInd']"/>&nbsp;</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <fo:table-row>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Assistance à l'hébergement d'urgence</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='homelessShelterInd']"/>&nbsp;</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          
          <fo:table-row>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3">Hébergement temporaire pour sans abris</fo:inline>
              </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-left="5.4pt" padding-right="5.4pt">
              <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                <fo:inline xsl:use-attribute-sets="Normal_3"><xsl:apply-templates select="FIELD[FNAME='housingAssistInd']"/>&nbsp;</fo:inline>
              </fo:block>
            </fo:table-cell>
          </fo:table-row>
          <!-- END, CR00352142 -->
        </fo:table-body>
      </fo:table>
    </fo:block>  
  
  </xsl:template>
  
  
  <xsl:template match="FIELD">
    <xsl:choose>
      <xsl:when test="TYPE='SVR_BOOLEAN'">
    
        <xsl:choose>
          <xsl:when test="VALUE='false'">
            Non
          </xsl:when>
          <xsl:otherwise>
            Oui
          </xsl:otherwise>
        </xsl:choose>
    
      </xsl:when>
    
      <xsl:otherwise>
        <xsl:value-of select="VALUE"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  

</xsl:stylesheet>
