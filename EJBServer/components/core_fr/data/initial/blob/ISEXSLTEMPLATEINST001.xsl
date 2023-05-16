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
    <xsl:apply-templates select="STRUCT[SNAME='MeetingMinutesSummaryDetails']" />
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
              <fo:block space-before.optimum="1pt" space-after.optimum="2pt">
                <fo:block text-align="justify">
                  <fo:block>
                    <fo:leader leader-pattern="space" />
                  </fo:block>
                  <fo:block>
                    <fo:leader leader-pattern="space" />
                  </fo:block>
                  <fo:block text-align="center">
                    <fo:inline font-size="16pt" font-weight="bold">
                      Minutes pour
                      <xsl:apply-templates select="FIELD[FNAME='minutesDtls']/STRUCT['MeetingMinutesDtls']/FIELD[FNAME='subject']" />
                      mises en attente le <xsl:apply-templates select="FIELD[FNAME='startDate']" /> à <xsl:apply-templates select="FIELD[FNAME='startTime']" />
                    </fo:inline>
                  </fo:block>
                  <fo:block>
                    <fo:leader leader-pattern="space" />
                  </fo:block>
                  <!-- Details Table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell number-columns-spanned="4" background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Détails</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Objet :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell number-columns-spanned="3" padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='minutesDtls']/STRUCT['MeetingMinutesDtls']/FIELD[FNAME='subject']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Emplacement :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell number-columns-spanned="3" padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='minutesDtls']/STRUCT['MeetingMinutesDtls']/FIELD[FNAME='location']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Heure de début :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block text-align="left">
                            <fo:inline>
                              <xsl:apply-templates select="FIELD[FNAME='startDate']" />
                              <fo:leader leader-pattern="space" />
                              <xsl:apply-templates select="FIELD[FNAME='startTime']" />
                            </fo:inline>
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Heure de fin :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block text-align="left">
                            <fo:inline>
                              <xsl:apply-templates select="FIELD[FNAME='endDate']" />
                              <fo:leader leader-pattern="space" />
                              <xsl:apply-templates select="FIELD[FNAME='endTime']" />
                            </fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Durée :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='duration']" />
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Dure toute la journée :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='allDayMeeting']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Organisateur :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black" text-align="left">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='organizerFullName']" />
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Minuté par :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black" text-align="left">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='minuteTakerFullName']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>Type de réunion :</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <xsl:apply-templates select="FIELD[FNAME='meetingType']" />
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block>
                            <fo:leader leader-pattern="space" />
                          </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block text-align="left">
                            <fo:inline>
                              <fo:leader leader-pattern="space" />
                            </fo:inline>
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-body>
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Agenda table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="100%" />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Ordre du jour</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block linefeed-treatment="preserve" white-space-collapse="false" wrap-option="wrap" white-space-treatment="preserve">
                            <xsl:apply-templates select="FIELD[FNAME='minutesDtls']/STRUCT['MeetingMinutesDtls']/FIELD[FNAME='meetingAgenda']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-body>
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Decisions table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="100%" />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Décisions</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block linefeed-treatment="preserve" white-space-collapse="false" wrap-option="wrap" white-space-treatment="preserve">
                            <xsl:apply-templates select="FIELD[FNAME='decisions']/STRUCT['MeetingDecisionsDtls']/FIELD[FNAME='decisions']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-body>
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Attendees table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="200pt" />
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-column column-width="60pt" />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell number-columns-spanned="4" background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Participants</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Nom</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Rôle</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Invitation acceptée</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Présents</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <xsl:for-each select="FIELD[FNAME='attendeeSummaryDetailsList']/STRUCT_LIST/STRUCT['MeetingAttendeeSummaryDetails']">
                        <xsl:call-template name="AttendeesList" />
                      </xsl:for-each>                      
                    </fo:table-body>                    
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Notes table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="100%" />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Notes</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block linefeed-treatment="preserve" white-space-collapse="false" wrap-option="wrap" white-space-treatment="preserve">
                            <xsl:apply-templates select="FIELD[FNAME='notes']/STRUCT['MeetingNotesDtls']/FIELD[FNAME='notes']" />
                          </fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-body>
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Files table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell number-columns-spanned="2" background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Fichiers</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Nom</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Commentaire</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <xsl:for-each select="FIELD[FNAME='attachmentLinkSummaryList']/STRUCT_LIST/STRUCT['MeetingAttachmentLinkSummaryDetails']">
                        <xsl:call-template name="AttachmentList" />
                      </xsl:for-each>                      
                    </fo:table-body>                    
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Actions table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell number-columns-spanned="4" background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Actions</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Objet</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Affecté à</fo:block>
                        </fo:table-cell>
                         <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Date d'échéance</fo:block>
                        </fo:table-cell>
                         <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Statut</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <xsl:for-each select="FIELD[FNAME='actionSummaryDetailsList']/STRUCT_LIST/STRUCT['MeetingActionsSummaryDetails']">
                        <xsl:call-template name="ActionList" />
                      </xsl:for-each>                      
                    </fo:table-body>                    
                  </fo:table>
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <fo:leader leader-pattern="space" />
                </fo:block>
                <fo:block>
                  <!-- Cases table -->
                  <fo:table table-layout="fixed" width="100%">
                    <fo:table-column />
                    <fo:table-column />
                    <fo:table-header>
                      <fo:table-row>
                        <fo:table-cell number-columns-spanned="2" background-color="#E0E0E0" padding="2pt" border="0.5pt solid black">
                          <fo:block font-weight="bold">Dossiers abordés</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
                      <fo:table-row>                         
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Référence</fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding="2pt" border="0.5pt solid black">
                          <fo:block color="#969696">Type</fo:block>
                        </fo:table-cell>
                      </fo:table-row>
                      <xsl:for-each select="FIELD[FNAME='meetingLinkSummaryDetailsList']/STRUCT_LIST/STRUCT['MeetingLinkSummaryDetails']">
                        <xsl:call-template name="CaseList" />
                      </xsl:for-each>                      
                    </fo:table-body>                    
                  </fo:table>
                </fo:block>
              </fo:block>
            </fo:block>
          </fo:block>     
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template name="AttendeesList">
    <fo:table-row>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='attendeeFullName']" />
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='attendeeRole']" />
        </fo:block>
      </fo:table-cell>                   
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='attendeeAccepted']" />
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='attendeeAttended']" />
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  <xsl:template name="AttachmentList">
    <fo:table-row>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='fileName']" />
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='comments']" />
        </fo:block>
      </fo:table-cell>                   
    </fo:table-row>
  </xsl:template>
  <xsl:template name="ActionList">
    <fo:table-row>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='subject']" />
        </fo:block>
      </fo:table-cell> 
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='actionAssignees']" />
        </fo:block>
      </fo:table-cell>
          <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='dueDate']" />
        </fo:block>
      </fo:table-cell>   
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='status']" />
        </fo:block>
      </fo:table-cell>             
    </fo:table-row>
  </xsl:template>
  <xsl:template name="CaseList">
    <fo:table-row>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='caseReference']" />
        </fo:block>
      </fo:table-cell>
      <fo:table-cell padding="2pt" border="0.5pt solid black">
        <fo:block>
          <xsl:apply-templates select="FIELD[FNAME='caseType']" />
        </fo:block>
      </fo:table-cell>                   
    </fo:table-row>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
</xsl:stylesheet>


