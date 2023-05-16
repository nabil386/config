<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>

 <!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
   <!-- END, CR00357205 -->
  <!-- BEGIN, CR00352142, PB -->
<xsl:attribute-set name="Normal_1" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">5mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">110mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
   </xsl:attribute-set>
  	  		    <xsl:attribute-set name="Normal_2" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">5mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">110mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-align">right</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
</xsl:attribute-set>
  <xsl:attribute-set name="Normal_3" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">5mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">0mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_4" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">15mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">0mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
    <xsl:attribute-set name="Normal_5" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">7mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">0mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
</xsl:attribute-set>
  <xsl:attribute-set name="Normal_6" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="line-height">7mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
	<!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-indent">0mm</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
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
          <fo:block xsl:use-attribute-sets="Normal_6">Veuillez confirmer que les informations suivantes que vous avez fournies
            à cette agence sont correctes.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_4">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Nom du client : <xsl:apply-templates select="FIELD[FNAME='concernRoleName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Dernier employeur :
            <xsl:apply-templates select="FIELD[FNAME='lastEmployerName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Dernier emploi occupé :
            <xsl:apply-templates select="FIELD[FNAME='dateLastWorked']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Motif du chômage :
            <xsl:apply-templates select="FIELD[FNAME='lastReasonCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            A côté de Dernier employeur :
            <xsl:apply-templates select="FIELD[FNAME='nextToLastEmployerName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Motif du chômage :
            <xsl:apply-templates select="FIELD[FNAME='nextToLastReasonCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Disponible pour travailler :
            <xsl:apply-templates select="FIELD[FNAME='availableForWorkInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Apte à travailler :
            <xsl:apply-templates select="FIELD[FNAME='capableOfWorkInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Bénéficiaire d'autres prestations :
            <xsl:apply-templates select="FIELD[FNAME='receiptOfOtherBenefitsInd']" />
          </fo:block>
          <xsl:if test="FIELD[FNAME='receiptOfOtherBenefitsInd' and VALUE='1']">
            <fo:block xsl:use-attribute-sets="Normal_6">
              Détails :
              <xsl:apply-templates select="FIELD[FNAME='otherBenefitDescription']" />
            </fo:block>
            <fo:block xsl:use-attribute-sets="Normal_6">
              Montant :
              <xsl:apply-templates select="FIELD[FNAME='otherBenefitAmount']" />
            </fo:block>
          </xsl:if>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Demande de prestations effectuée dans un autre Etat :
            <xsl:apply-templates select="FIELD[FNAME='appliedInAnotherStateInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Etat :
            <xsl:apply-templates select="FIELD[FNAME='stateCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Résident de cet Etat :
            <xsl:apply-templates select="FIELD[FNAME='clientResidentInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Ethnicité :
            <xsl:apply-templates select="FIELD[FNAME='ethnicityCode']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Vétéran :
            <xsl:apply-templates select="FIELD[FNAME='veteranInd']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_6">
            Membre de l'Union :
            <xsl:apply-templates select="FIELD[FNAME='unionMemberInd']" />
          </fo:block>
          <xsl:if test="FIELD[FNAME='unionMemberInd' and VALUE='1']">
            <fo:block xsl:use-attribute-sets="Normal_6">
              Détails de l'Union :
              <xsl:apply-templates select="FIELD[FNAME='unionMemberDescription']" />
            </fo:block>
          </xsl:if>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">Je déclare que tous les détails fournis sont vrais et complets,
             et que je m'engage à notifier le département si j'obtiens un emploi
             ou en dossier de modification des détails donnés.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">&nbsp;</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_3">Veuillez signer ici :</fo:block>
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
          <xsl:when test="VALUE='false'">Non</xsl:when>
          <xsl:otherwise>Oui</xsl:otherwise>
        </xsl:choose>
          </xsl:when>
          <xsl:otherwise>
        <xsl:value-of select="VALUE" />
          </xsl:otherwise>
        </xsl:choose>
  </xsl:template>
</xsl:stylesheet>


