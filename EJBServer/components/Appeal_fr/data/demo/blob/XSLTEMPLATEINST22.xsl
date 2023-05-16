<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
  <xsl:variable name="fo:layout-master-set">
    <fo:layout-master-set>
      <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
        <fo:region-body margin-top="0.79in" margin-bottom="0.79in" />
      </fo:simple-page-master>
    </fo:layout-master-set>
  </xsl:variable>
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA" />
  </xsl:template>
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='CourtPetitionData']" />
  </xsl:template>
  <xsl:template match="STRUCT">
    <fo:root>
      <xsl:copy-of select="$fo:layout-master-set" />
      <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
        <!-- START NON-TRANSLATABLE -->
        <fo:flow flow-name="xsl-region-body" font-family="WT Sans">
          <!-- END NON-TRANSLATABLE -->
          <fo:block>
            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
              <fo:block text-align="center">
                <fo:inline font-size="14pt" font-weight="bold">Demande au tribunal</fo:inline>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <xsl:text />
                </fo:block>
                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                  <fo:block text-align="center">
                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                      <fo:block text-align="center">
                        <!-- BEGIN, HARP-30848, BR -->
                        <fo:inline font-size="12pt">
                          <xsl:apply-templates select="FIELD[FNAME='organizationName']" />
                          (Demandeur)
                        </fo:inline>
                        <!-- END, HARP-30848 -->
                        <fo:block>
                          <fo:leader leader-pattern="space" />
                        </fo:block>
                        <fo:block>
                          <xsl:text />
                        </fo:block>
                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                          <fo:block>
                            <fo:inline font-size="12pt">contre</fo:inline>
                            <fo:block>
                              <fo:leader leader-pattern="space" />
                            </fo:block>
                            <fo:block>
                              <xsl:text />
                            </fo:block>
                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                              <fo:block text-align="center">
                                <!-- BEGIN, HARP-30848, BR -->
                                <fo:inline font-size="12pt">
                                  <xsl:apply-templates select="FIELD[FNAME='respondentName']" />
                                  (Défendeur)
                                </fo:inline>
                                <!-- END, HARP-30848 -->
                                <fo:block>
                                  <fo:leader leader-pattern="space" />
                                </fo:block>
                                <fo:block>
                                  <fo:leader leader-pattern="space" />
                                </fo:block>
                                <fo:block>
                                  <fo:leader leader-pattern="space" />
                                </fo:block>
                                <fo:inline font-size="14pt" font-weight="bold">Demande en vue d'un examen judiciaire</fo:inline>
                                <fo:block>
                                  <xsl:text />
                                </fo:block>
                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                  <fo:block text-align="left">
                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                      <fo:block>
                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                          <fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                              <fo:block text-align="justify">
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                Le demandeur précité porte à l'attention de la cour les faits suivants :
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                1. Le demandeur formule une réclamation suite à une décision d'examen d'audience, <xsl:apply-templates select="FIELD[FNAME='priorHearingReference']" />, établie le <xsl:apply-templates select="FIELD[FNAME='decisionDate']" />.
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                2. Le demandeur demande que la cour révise cette décision d'examen d'audience. En outre, le demandeur cherche à démontrer à la cour que cette décision :
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (a) est en violation avec les dispositions de la Constitution ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (b) outrepasse le pouvoir légal et la juridiction du comité d'examen d'audience ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (c) a été rendue en se fondant sur des procédures illégales ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (d) est affectée par d'autres erreurs de droit ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (e) est clairement erronée au vu de l'intégralité de l'enregistrement soumis et la politique publique reflétée par la loi de la législature ayant rendu la décision en question ; et/ou
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                (f) ne repose pas sur des faits établis par des informations collectées irréfutables.
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                En conséquence, le demandeur demande respectueusement :
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                1. l'annulation de la décision d'examen d'audience ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                2. si nécessaire, le renvoi du dossier devant le comité d'examen d'audience pour une nouvelle audience ou une autre procédure appropriée ;
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                3. toute autre mesure semblable qui soit juste et équitable.
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                _____________________________________
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <xsl:apply-templates select="FIELD[FNAME='userFullName']" />
                                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                  <fo:block>
                                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                      <fo:block text-align="justify">
                                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                          <fo:block>
                                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                              <fo:block>
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
            </fo:block>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
</xsl:stylesheet>


