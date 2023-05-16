<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xml.apache.org/fop/extensions" xmlns:foa="http://fabio" version="1.0">
  <!-- Produced by WH2FO 0.3.1 -->
  <xsl:attribute-set name="Table_1" foa:class="table" use-attribute-sets="Table">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-style">none</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-collapse">collapse</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Cell_2" foa:class="table-cell" use-attribute-sets="Cell">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-style">solid</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-bottom">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-width">1.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-left-style">none</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-top">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-left">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-color">windowtext</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-right">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_2" foa:class="inline" use-attribute-sets="Normal" />
  <xsl:attribute-set name="bold" foa:class="inline">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Cell_4" foa:class="table-cell" use-attribute-sets="Cell">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-bottom">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-right-width">1.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-left-style">none</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-top-style">none</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-top">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-left">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-right">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-bottom-width">1.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Cell_3" foa:class="table-cell" use-attribute-sets="Cell">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-style">solid</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-bottom">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-width">1.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-top-style">none</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-top">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-left">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-color">windowtext</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-right">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Table" foa:class="table" />
  <xsl:attribute-set name="Cell_1" foa:class="table-cell" use-attribute-sets="Cell">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-style">solid</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-bottom">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-width">1.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-top">0in</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-right">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="border-color">windowtext</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="padding-left">5.4pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_3" foa:class="block" use-attribute-sets="Normal">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-align">end</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Normal_1" foa:class="inline" use-attribute-sets="Normal">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">14.5pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="text-align">center</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <xsl:attribute-set name="Cell" foa:class="table-cell" />
  <xsl:attribute-set name="Normal" foa:class="block">
    <!-- START NON-TRANSLATABLE -->
    <xsl:attribute name="font-size">10.0pt</xsl:attribute>
    <!-- END NON-TRANSLATABLE -->
  </xsl:attribute-set>
  <!-- Added for Curam -->
  <xsl:template match="DOCUMENT">
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <!-- Added for Curam -->
  <xsl:template match="DATA">
    <xsl:apply-templates select="STRUCT[SNAME='ScheduleHearingData']" />
  </xsl:template>
  <!-- Added for Curam -->
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
  <!-- Header specification -->
  <xsl:template name="HeaderRest">
    <fo:block font-size="7pt" />
  </xsl:template>
  <xsl:template name="HeaderFirst">
    <fo:block font-size="7pt" />
  </xsl:template>
  <!-- Footer specification -->
  <xsl:template name="TheFooter">
    <fo:block />
  </xsl:template>
  <xsl:output method="xml" encoding="UTF-8" />
  <xsl:strip-space elements="row" />
  <xsl:template match="STRUCT">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master page-width="8.5in" page-height="11.0in" master-name="Section1-rest" margin-top="54.0pt" margin-bottom="54.0pt" margin-left="75.0pt" margin-right="75.0pt">
          <fo:region-body margin-top="54.0pt" margin-bottom="54.0pt" />
          <fo:region-before region-name="xsl-region-before-rest" extent="54.0pt" />
          <fo:region-after extent="54.0pt" />
        </fo:simple-page-master>
        <fo:simple-page-master page-width="8.5in" page-height="11.0in" master-name="Section1-first" margin-top="54.0pt" margin-bottom="54.0pt" margin-left="75.0pt" margin-right="75.0pt">
          <fo:region-body margin-top="54.0pt" margin-bottom="54.0pt" />
          <fo:region-before region-name="xsl-region-before-first" extent="54.0pt" />
          <fo:region-after extent="54.0pt" />
        </fo:simple-page-master>
        <fo:page-sequence-master master-name="Section1-ps">
          <fo:repeatable-page-master-alternatives>
            <fo:conditional-page-master-reference master-reference="Section1-first" page-position="first" />
            <fo:conditional-page-master-reference master-reference="Section1-rest" page-position="rest" />
          </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="Section1-ps">
        <fo:static-content flow-name="xsl-region-before-rest">
          <xsl:call-template name="HeaderRest" />
        </fo:static-content>
        <fo:static-content flow-name="xsl-region-before-first">
          <xsl:call-template name="HeaderFirst" />
        </fo:static-content>
        <fo:static-content flow-name="xsl-region-after">
          <xsl:call-template name="TheFooter" />
        </fo:static-content>
        <!-- START NON-TRANSLATABLE -->
        <fo:flow flow-name="xsl-region-body" font-family="WT Sans">
          <!-- END NON-TRANSLATABLE -->
          <fo:block text-align="center">
            <fo:inline font-weight="bold">
              <fo:inline xsl:use-attribute-sets="Normal_1">Sommation d'audience</fo:inline>
            </fo:inline>
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='correspondentName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='correspondentAddress']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='issueDate']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">A qui de droit :</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">Votre présence est requise dans l'affaire concernant la ou les personnes suivantes :</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='legalParticipantName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
                                                  Vous avez reçu l'ordre de témoigner au nom de
                                                  <xsl:apply-templates select="FIELD[FNAME='behalfOfName']" />
                                                  aux date et heure mentionnées ci-dessous :
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="Table_1">
            <fo:table-column column-number="1" column-width="proportional-column-width(30)" />
            <fo:table-column column-number="2" column-width="proportional-column-width(70)" />
            <fo:table-body>
              <fo:table-row>
                <fo:table-cell xsl:use-attribute-sets="Cell_1">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">Numéro de dossier :</fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell xsl:use-attribute-sets="Cell_2">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">
                        <xsl:apply-templates select="FIELD[FNAME='caseReference']" />
                      </fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell xsl:use-attribute-sets="Cell_3">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">Date :</fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell xsl:use-attribute-sets="Cell_4">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">
                        <xsl:apply-templates select="FIELD[FNAME='scheduledDate']" />
                      </fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell xsl:use-attribute-sets="Cell_3">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">Heure :</fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell xsl:use-attribute-sets="Cell_4">
                  <fo:block xsl:use-attribute-sets="Normal_2">
                    <fo:inline xsl:use-attribute-sets="Normal_2">
                      <fo:inline xsl:use-attribute-sets="Normal_3">
                        <xsl:apply-templates select="FIELD[FNAME='scheduledTime']" />
                      </fo:inline>
                    </fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            Cette audience a été planifiée pour le
            <xsl:apply-templates select="FIELD[FNAME='scheduledDate']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <fo:inline font-weight="bold">
              <fo:inline xsl:use-attribute-sets="Normal_2">Instructions</fo:inline>
            </fo:inline>
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">Vous devez prendre contact avec :</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">
            <xsl:apply-templates select="FIELD[FNAME='organizationName']" />
          </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">avant la date de l'audience et fournir le numéro de téléphone auquel vous pouvez être joint à l'heure susmentionnée. Vous êtes prié de répondre au numéro que vous avez fourni pour participer à une audience téléphonique en rapport avec cette affaire.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal_2">Si vous avez une raison valable de ne pas comparaître à cette audience, vous devrez en rendre compte auprès du responsable de l'audience. En cas de non-comparution, sachez que la cour peut intenter des actions en justice supplémentaires.</fo:block>
          <fo:block xsl:use-attribute-sets="Normal"> </fo:block>
          <fo:block xsl:use-attribute-sets="Normal"> </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>


