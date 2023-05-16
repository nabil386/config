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
    <xsl:attribute name="font-size">10.0pt</xsl:attribute>
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
    <xsl:apply-templates select="FIELD[FNAME='isDetails']"/>
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='isDetails']">
    <xsl:apply-templates select="STRUCT[SNAME='IncomeSupportDetails']"/>
  </xsl:template>

  
  
  <xsl:template match="STRUCT[SNAME='IncomeSupportDetails']">
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
        <!-- BEGIN, CR00352142, PB -->
          <fo:block xsl:use-attribute-sets="Normal_1" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="center" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_4">Demande d'informations pour vérifier l'éligibilité</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_3">Madame, Monsieur, <xsl:apply-templates select="FIELD[FNAME='applicantTitle']"/>&nbsp;<xsl:apply-templates select="FIELD[FNAME='applicantSurname']"/>,</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_3">Pour avoir droit à des prestations, vous devez nous fournir les documents de vérification ci-dessous pour </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_4">vous </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_3">et </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_4">toutes les personnes pour lesquelles vous faites la demande</fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_3">. Si vous avez des questions ou besoin d'aide pour obtenir les preuves, appelez-moi.  Merci.</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12pt">
            <fo:table>
              <fo:table-column column-width="50mm"/>
              <fo:table-column column-width="50mm"/>
              <fo:table-column column-width="50mm"/>
              <fo:table-body>
                <fo:table-row>
                  <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_4">REVENU</fo:inline>
                    </fo:block>
                    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      &nbsp;
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Bulletin de paie</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Reçus pour dépenses professionnelles</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Déclaration de revenus estimés</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Déclarations fiscales</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Bulletin/Chèque de pension</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Vérification de prestation la plus récente ou copie </fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Déposition de sponsors étrangers</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Casiers judiciaires</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Déclaration de la personne/l'agence fournissant l'argent ou effectuant le paiement pour le client</fo:inline>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_4">PREUVE D'IDENTITE</fo:inline>
                    </fo:block>
                    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      &nbsp;
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Permis de conduire</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Acte de naissance</fo:inline>
                    </fo:block>
					<!-- BEGIN, CR00357029, PB -->
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Acte de mariage/Jugement de divorce</fo:inline>
                    </fo:block>
					<!-- END, CR00357029 -->
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Passeport américain</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Papiers ou dossiers d'adoption</fo:inline>
                    </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Carte d'enregistrement de l'électeur</fo:inline>
                   </fo:block>
                 </fo:table-cell>
                 <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">PREUVES DES DEPENSES LES PLUS RECENTES</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Déclaration écrite/factures des fournisseurs</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Carte d'assurance-maladie, cartes d'assurance santé et primes actuelles</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Factures médicales sans recours</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Preuve d'invalidité</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Location, hypothèque</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Accord de location</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Factures de gaz, d'électricité et de téléphone</fo:inline>
                   </fo:block>
                 </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                 <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">ASSIDUITE SCOLAIRE</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Ecole</fo:inline>
                     <fo:inline xsl:use-attribute-sets="Normal_2"> Enregistrements du registre de présence</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Carte de rapport actuelle</fo:inline>
                   </fo:block>
                 </fo:table-cell>
                 <fo:table-cell border-bottom-style="none" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="justify" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">ACTIFS</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="justify" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Extraits de compte</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Vérification du compte, du compte d'épargne et des enregistrements de mutuelle</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Actions et obligations</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Déclaration d'un fonctionnaire de la banque, d'un agent d'assurance</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Evaluation de l'assurance/d'imposition</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Police d'assurance</fo:inline>
                   </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Reçus de vente récents</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="none" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_4">URGENCE </fo:inline>
                    <fo:inline xsl:use-attribute-sets="Normal_4">ASSISTANCE</fo:inline>
                  </fo:block>
                  <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    &nbsp;
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Avis d'expulsion du propriétaire</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Avis écrit de résiliation des services collectifs par le prestataire</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Déclaration signée de l'institution de crédit</fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_4">N° S.S.</fo:inline>
                  </fo:block>
                  <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    &nbsp;
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Carte de sécurité sociale</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Copie de la demande de numéro S.S.</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Imprimés Bendex ou SDX</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="none" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="none" padding-left="5.4pt" padding-right="5.4pt">
				<!-- BEGIN, CR00357029, PB -->
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
                  </fo:block>
				  <!-- ENd, CR00357029 -->
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
          </fo:block>
          
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>  
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_2">*IMPORTANT : ces preuves doivent inclure le nom, l'adresse et le numéro de téléphone de la personne qui fait la déclaration.</fo:inline>
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
