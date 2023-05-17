<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
  <xsl:variable name="fo:layout-master-set">
    <fo:layout-master-set>
      <fo:simple-page-master margin-left="0.6in" margin-right="0.6in" master-name="default-page" page-height="11in" page-width="8.5in">
        <fo:region-body margin-bottom="0.79in" margin-top="0.79in" />
      </fo:simple-page-master>
    </fo:layout-master-set>
  </xsl:variable>
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='AppealReceiptData']" />
  </xsl:template>
  <xsl:template match="STRUCT">
    <fo:root>
      <xsl:copy-of select="$fo:layout-master-set" />
      <fo:page-sequence format="1" initial-page-number="1" master-reference="default-page">
        <!-- START NON-TRANSLATABLE -->
        <fo:flow flow-name="xsl-region-body" font-family="WT Sans">
          <!-- END NON-TRANSLATABLE -->
          <fo:block>
            <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
              <fo:block text-align="center">
                <fo:inline font-size="14pt" font-weight="bold">Accusé de réception de l'examen judiciaire et confirmation du statut de l'assistance</fo:inline>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:inline font-size="12pt" font-weight="bold">Le présent document n'est pas une notification de programmation.</fo:inline>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <xsl:text />
                </fo:block>
                <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                  <fo:block text-align="center">
                    <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                      <fo:block text-align="center">
                        <fo:inline font-size="12pt">
                          <xsl:apply-templates select="FIELD[FNAME='appellantName']" />
                        </fo:inline>
                        <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                          <fo:block linefeed-treatment="preserve" text-align="center" white-space-collapse="false" white-space-treatment="preserve">
                            <fo:inline font-size="12pt">
                              <xsl:apply-templates select="FIELD[FNAME='appellantAddress']" />
                            </fo:inline>
                            <fo:block>
                              <xsl:text />
                            </fo:block>
                            <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                              <fo:block text-align="left">
                                <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                                  <fo:block>
                                    <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                                      <fo:block>
                                        <fo:block>
                                          <xsl:text />
                                        </fo:block>
                                        <fo:table space-after.optimum="2pt" space-before.optimum="1pt" width="100%">
                                          <fo:table-column column-width="160pt" />
                                          <fo:table-column />
                                          <fo:table-body>
                                            <fo:table-row>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="160pt">
                                                <fo:block>
                                                  <fo:inline font-size="12pt" font-weight="normal">Numéro de dossier :</fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start">
                                                <fo:block>
                                                  <fo:inline font-size="12pt" font-weight="normal">
                                                    <xsl:apply-templates select="FIELD[FNAME='caseReference']" />
                                                  </fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                            </fo:table-row>
                                          </fo:table-body>
                                        </fo:table>
                                        <fo:block linefeed-treatment="preserve" space-after.optimum="2pt" space-before.optimum="1pt" white-space-collapse="false" white-space-treatment="preserve">
                                          <fo:block text-align="left">
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:inline font-size="12pt">
                                              Nous vous informons qu'une demande d'examen d'audience judiciaire a été reçue de la part de <xsl:apply-templates select="FIELD[FNAME='appellantName']" /> concernant les problèmes répertoriés ci-après.
                                            </fo:inline>
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:inline font-size="12pt" font-weight="bold">Problèmes à examiner</fo:inline>
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <!-- BEGIN, CR00333554, SSK  -->
                                            <fo:inline font-size="12pt" font-weight="bold">le bureau local a reçu l'ordre de continuer à vous assister jusqu'à ce que la décision d'audience au sujet des problèmes ci-dessous soit rendue :</fo:inline>
                                            <!-- END, CR00333554 -->
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                          </fo:block>
                                        </fo:block>
                                        <fo:table space-after.optimum="2pt" space-before.optimum="1pt" width="100%">
                                          <fo:table-column column-width="280pt" />
                                          <fo:table-column column-width="100pt" />
                                          <fo:table-column column-width="80pt" />
                                          <fo:table-column column-width="60pt" />
                                          <fo:table-body>
                                            <fo:table-row>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="280pt">
                                                <fo:block>
                                                  <fo:inline font-size="12pt" font-weight="normal">Motif</fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="100pt">
                                                <fo:block>
                                                  <fo:inline font-size="12pt">Date de réception</fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="80pt">
                                                <fo:block>
                                                  <fo:inline font-size="12pt">Date d'entrée en vigueur</fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                              <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="60pt">
                                                <fo:block>
                                                  <fo:inline font-size="12pt">Poursuivre les prestations</fo:inline>
                                                </fo:block>
                                              </fo:table-cell>
                                            </fo:table-row>
                                            <xsl:apply-templates select="FIELD[FNAME='appealReceiptNoticeData']" />
                                          </fo:table-body>
                                        </fo:table>
                                        <fo:block>
                                          <xsl:text />
                                        </fo:block>
                                        <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                                          <fo:block text-align="justify">
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:block>
                                              <fo:leader leader-pattern="space" />
                                            </fo:block>
                                            <fo:inline font-size="12pt" font-weight="bold">Si votre représentant à l'audience n'est pas avocat, vous devez lui fournir une autorisation écrite l'autorisant à vous représenter et à examiner le dossier du dossier.</fo:inline>
                                            <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                                              <fo:block>
                                                <fo:block space-after.optimum="2pt" space-before.optimum="1pt" text-align="left">
                                                  <fo:block>
                                                    <fo:block>
                                                      <fo:leader leader-pattern="space" />
                                                    </fo:block>
                                                    <fo:inline font-size="12pt">Si vous avez besoin de contacter ce bureau au sujet de cette requête ou du statut de l'assistance de votre dossier, envoyez un courrier à l'adresse ci-dessous :</fo:inline>
                                                    <fo:block>
                                                      <fo:leader leader-pattern="space" />
                                                    </fo:block>
                                                    <fo:inline font-size="12pt">
                                                      <xsl:apply-templates select="FIELD[FNAME='organizationName']" />
                                                    </fo:inline>
                                                    <fo:block>
                                                      <xsl:text />
                                                    </fo:block>
                                                    <fo:block linefeed-treatment="preserve" space-after.optimum="2pt" space-before.optimum="1pt" white-space-collapse="false" white-space-treatment="preserve">
                                                      <fo:block text-align="left">
                                                        <fo:inline font-size="12pt">
                                                          <xsl:apply-templates select="FIELD[FNAME='organizationAddress']" />
                                                        </fo:inline>
                                                        <fo:block text-align="left">
                                                          <fo:leader leader-pattern="space" />
                                                        </fo:block>
                                                        <fo:block>
                                                          <xsl:text />
                                                        </fo:block>
                                                        <fo:block space-after.optimum="2pt" space-before.optimum="1pt">
                                                          <fo:block>
                                                            <fo:block>
                                                              <fo:leader leader-pattern="space" />
                                                            </fo:block>
                                                            <fo:inline font-size="12pt">Vous recevrez une autre notification vous informant de la date, de l'heure et du lieu de votre audience.</fo:inline>
                                                            <fo:block>
                                                              <xsl:text />
                                                            </fo:block>
                                                          </fo:block>
                                                        </fo:block>
                                                      </fo:block>
                                                    </fo:block>
                                                  </fo:block>
                                                </fo:block>
                                              </fo:block>
                                            </fo:block>
                                          </fo:block>
                                        </fo:block>
                                      </fo:block>
                                    </fo:block>
                                  </fo:block>
                                </fo:block>
                              </fo:block>
                            </fo:block>
                          </fo:block>
                        </fo:block>
                      </fo:block>
                    </fo:block>
                  </fo:block>
                </fo:block>
              </fo:block>
            </fo:block>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD[FNAME='appealReasonData']">
    <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='AppealReasonData']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='AppealReasonData']">
    <fo:block font-size="12pt" font-weight="normal">
      <xsl:apply-templates select="FIELD[FNAME='appealReasonCode']" />
    </fo:block>
  </xsl:template>
  <xsl:template match="FIELD[FNAME='appealReceiptNoticeData']">
    <xsl:apply-templates select="STRUCT_LIST/STRUCT[SNAME='AppealReceiptNoticeData']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='AppealReceiptNoticeData']">
    <fo:table-row>
      <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="280pt">
        <fo:block>
          <fo:inline font-size="12pt">
            <xsl:apply-templates select="FIELD[FNAME='reasonCode']" />
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="100pt">
        <fo:block>
          <fo:inline font-size="12pt">
            <xsl:apply-templates select="FIELD[FNAME='receivedDate']" />
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="80pt">
        <fo:block>
          <fo:inline font-size="12pt">
            <xsl:apply-templates select="FIELD[FNAME='effectiveDate']" />
          </fo:inline>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell border-color="black" border-style="solid" border-width="1pt" display-align="center" padding-after="3pt" padding-before="3pt" padding-end="3pt" padding-start="3pt" text-align="start" width="60pt">
        <fo:block>
          <fo:inline font-size="12pt">
            <xsl:apply-templates select="FIELD[FNAME='continueBenefitsString']" />
          </fo:inline>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
</xsl:stylesheet>


