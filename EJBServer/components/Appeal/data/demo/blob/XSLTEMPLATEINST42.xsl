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
    <xsl:apply-templates select="STRUCT[SNAME='ScheduleHearingData']" />
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
                <fo:inline font-size="14pt" font-weight="bold">Notice of Telephone Hearing</fo:inline>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <xsl:text />
                </fo:block>
                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                  <fo:block text-align="left">
                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                      <fo:block text-align="right">
                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                          <fo:block>
                            <fo:block>
                              <xsl:text />
                            </fo:block>
                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                              <fo:block text-align="left">
                                <fo:inline font-size="12pt" font-weight="normal">
                                  <xsl:apply-templates select="FIELD[FNAME='correspondentName']" />
                                </fo:inline>
                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                  <fo:block>
                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt" white-space-collapse="false" linefeed-treatment="preserve" white-space-treatment="preserve">
                                      <fo:block>
                                        <fo:inline font-size="12pt">
                                          <xsl:apply-templates select="FIELD[FNAME='correspondentAddress']" />
                                        </fo:inline>
                                        <fo:block>
                                          <fo:leader leader-pattern="space" />
                                        </fo:block>
                                        <fo:block>
                                          <fo:leader leader-pattern="space" />
                                        </fo:block>
                                        <fo:inline font-size="12pt">You have been named as a third party in a hearing for:</fo:inline>
                                        <fo:block>
                                          <fo:leader leader-pattern="space" />
                                        </fo:block>
                                        <fo:block>
                                          <xsl:text />
                                        </fo:block>
                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                          <fo:block>
                                            <fo:inline font-size="12pt">
                                              <xsl:apply-templates select="FIELD[FNAME='appellantName']" />
                                            </fo:inline>
                                            <fo:block>
                                              <xsl:text />
                                            </fo:block>
                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt" white-space-collapse="false" linefeed-treatment="preserve" white-space-treatment="preserve">
                                              <fo:block>
                                                <fo:inline font-size="12pt">
                                                  <xsl:apply-templates select="FIELD[FNAME='appellantAddress']" />
                                                </fo:inline>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:inline font-size="12pt">The hearing will be held at the following time:</fo:inline>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:block>
                                                  <xsl:text />
                                                </fo:block>
                                                <fo:table width="100%" space-before.optimum="1pt" space-after.optimum="2pt">
                                                  <fo:table-column column-width="160pt" />
                                                  <fo:table-column />
                                                  <fo:table-body>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">Hearing No:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">
                                                            <xsl:apply-templates select="FIELD[FNAME='hearingReference']" />
                                                          </fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">Case No:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">
                                                            <xsl:apply-templates select="FIELD[FNAME='caseReference']" />
                                                          </fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">Date:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt" font-weight="normal">
                                                            <xsl:apply-templates select="FIELD[FNAME='scheduledDate']" />
                                                          </fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt">Time:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell text-align="left" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt">
                                                            <xsl:apply-templates select="FIELD[FNAME='scheduledTime']" />
                                                          </fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt">Requested:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell text-align="left" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt">
                                                            <xsl:apply-templates select="FIELD[FNAME='receivedDate']" />
                                                          </fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                      <fo:table-cell width="160pt" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" text-align="start" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:inline font-size="12pt">Issues:</fo:inline>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                      <fo:table-cell text-align="left" padding-start="3pt" padding-end="3pt" padding-before="3pt" padding-after="3pt" display-align="center" border-style="solid" border-width="1pt" border-color="black">
                                                        <fo:block>
                                                          <fo:list-block provisional-distance-between-starts="0mm" provisional-label-separation="2mm" start-indent="0mm" space-before.optimum="3pt" space-after.optimum="3pt">
                                                            <fo:list-item>
                                                              <fo:list-item-label end-indent="label-end()" font-size="12pt" font-weight="normal">
                                                                <fo:block />
                                                              </fo:list-item-label>
                                                              <fo:list-item-body start-indent="body-start()" font-size="12pt" font-weight="normal">
                                                                <xsl:apply-templates select="FIELD[FNAME='appealReasonData']" />
                                                              </fo:list-item-body>
                                                            </fo:list-item>
                                                          </fo:list-block>
                                                        </fo:block>
                                                      </fo:table-cell>
                                                    </fo:table-row>
                                                  </fo:table-body>
                                                </fo:table>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:inline font-size="12pt">
                                                  This hearing has been
                                                  <xsl:apply-templates select="FIELD[FNAME='scheduleString']" />
                                                  <xsl:apply-templates select="FIELD[FNAME='scheduledDate']" />
                                                  .
                                                </fo:inline>
                                                <fo:block>
                                                  <fo:leader leader-pattern="space" />
                                                </fo:block>
                                                <fo:inline font-size="10pt" font-weight="bold">Instructions</fo:inline>
                                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                  <fo:block text-align="justify">
                                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                      <fo:block text-align="justify">
                                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                          <fo:block>
                                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                              <fo:block text-align="justify">
                                                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                  <fo:block>
                                                                    <fo:inline font-size="10pt">You must contact:</fo:inline>
                                                                    <fo:block>
                                                                      <fo:leader leader-pattern="space" />
                                                                    </fo:block>
                                                                    <fo:block>
                                                                      <xsl:text />
                                                                    </fo:block>
                                                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                      <fo:block text-align="left">
                                                                        <fo:inline font-size="10pt">
                                                                          <xsl:apply-templates select="FIELD[FNAME='organizationName']" />
                                                                        </fo:inline>
                                                                        <fo:block>
                                                                          <xsl:text />
                                                                        </fo:block>
                                                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt" white-space-collapse="false" linefeed-treatment="preserve" white-space-treatment="preserve">
                                                                          <fo:block text-align="left">
                                                                            <fo:inline font-size="10pt">
                                                                              <xsl:apply-templates select="FIELD[FNAME='organizationAddress']" />
                                                                            </fo:inline>
                                                                            <fo:block>
                                                                              <fo:leader leader-pattern="space" />
                                                                            </fo:block>
                                                                            <fo:inline font-size="10pt">prior to the date of the hearing and provide the telephone number where you can be contacted at the time shown above. You are directed to be present at the number you provided to participate in a telephone hearing in connection with the hearing request filed in this matter. You may have such witnesses and evidence as you desire included in this hearing. You must provide the telephone numbers where such witnesses can be reached at the time of the hearing. You may be assisted at the hearing by a representative of your choice and you may question witnesses and examine evidence presented at the hearing. The hearing will be recorded.</fo:inline>
                                                                            <fo:block>
                                                                              <fo:leader leader-pattern="space" />
                                                                            </fo:block>
                                                                            <fo:inline font-size="10pt">If you are unable to participate at the scheduled time, you may request an adjournment. An adjournment will be granted only if you have a valid reason for the adjournment.</fo:inline>
                                                                            <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                              <fo:block>
                                                                                <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                                  <fo:block text-align="justify">
                                                                                    <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                                      <fo:block text-align="justify">
                                                                                        <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                                                                                          <fo:block text-align="justify">
                                                                                            <fo:inline font-size="10pt">
                                                                                              If you wish to have documents considered at the hearing, a copy of those documents must be received at least
                                                                                              <xsl:apply-templates select="FIELD[FNAME='statementTimeLimit']" />
                                                                                              days prior to the date of the hearing.
                                                                                            </fo:inline>
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
  <xsl:template match="STRUCT_LIST">
    <xsl:apply-templates select="STRUCT[SNAME='AppealReasonData']" />
  </xsl:template>
  <xsl:template match="STRUCT[SNAME='AppealReasonData']">
    <fo:block font-size="12pt" font-weight="normal">
      <xsl:apply-templates select="FIELD[FNAME='appealReasonCode']" />
    </fo:block>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:choose>
      <xsl:when test="FNAME='appealReasonData'">
        <xsl:apply-templates select="STRUCT_LIST" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="VALUE" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>

