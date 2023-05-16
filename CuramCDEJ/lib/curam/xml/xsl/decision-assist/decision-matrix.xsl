<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:domain-info="http://xml.apache.org/xalan/java/curam.util.client.domain.util.DomainUtils"
  xmlns:code-table="http://xml.apache.org/xalan/java/curam.omega3.codetable.CodeTableRepository"
  xmlns:xalan="http://xml.apache.org/xalan"
  extension-element-prefixes="xalan"
  exclude-result-prefixes="domain-info code-table">
  <xsl:import href="../common/ui-field.xsl"/>
  <xsl:import href="../dynamic-menu/build-menu.xsl"/>
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes" />

  <xsl:param name="label-add-questions" />
  <xsl:param name="label-add-contradictions" />
  <xsl:param name="label-add-score" />
  <xsl:param name="label-add-outcome" />
  <xsl:param name="label-add-priority" />
  <xsl:param name="label-specific-value" />
  <xsl:param name="label-min" />
  <xsl:param name="label-max" />
  <xsl:param name="header-answer-value" />
  <xsl:param name="header-questions" />
  <xsl:param name="header-contradictions" />
  <xsl:param name="header-outcome" />
  <xsl:param name="header-priority" />
  <xsl:param name="header-score" />
  <xsl:param name="validation-one-populate" />
        <xsl:param name="validation-overlap" />
        <xsl:param name="validation-duplicates" />
  <xsl:param name="validation-max-min" />
  <xsl:param name="validation-contradictions-single" />
  <xsl:param name="validation-contradictions-question" />
  <xsl:param name="validation-contradictions-warning" />
  <xsl:param name="validation-contradictions-error" />
  <xsl:param name="validation-contradictions-few" />
  <xsl:param name="validation-outcome-warning" />
  <xsl:param name="validation-outcome-error" />
  <xsl:param name="validation-outcome-copy" />
  <xsl:param name="validation-questions-empty" />

  <xsl:param name="control-value" />
  <xsl:param name="control-minmax" />
  <xsl:param name="control-delete-ans" />

  <xsl:param name="mx-prefix" />
  <xsl:param name="outcome-popup-page" />
  <xsl:param name="outcome-popup-page-width" />
  <xsl:param name="outcome-popup-page-height" />
  <xsl:param name="question-popup-page" />
  <xsl:param name="question-popup-page-width" />
  <xsl:param name="question-popup-page-height" />

  <xsl:param name="control-add-combination" />
  <xsl:param name="control-delete-combination" />
  <xsl:param name="control-copy-combination" />
  <xsl:param name="control-paste-combination" />
  <xsl:param name="control-add-outcome" />
  <xsl:param name="control-delete-outcome" />
  <xsl:param name="control-set-messages" />
  <xsl:param name="control-delete" />
  <xsl:param name="control-delete-question" />
  <xsl:param name="control-add-answer" />
  <xsl:param name="validation-message-empty-delete" />
  <xsl:param name="validation-message-empty" />
  <xsl:param name="boolean-true" />
  <xsl:param name="boolean-false" />


  <xsl:variable name="naming">
    <COLUMN name="C" />
    <COLUMN name="D" />
    <COLUMN name="E" />
    <COLUMN name="F" />
    <COLUMN name="G" />
    <COLUMN name="H" />
    <COLUMN name="I" />
    <COLUMN name="J" />
    <COLUMN name="K" />
    <COLUMN name="L" />
    <COLUMN name="M" />
    <COLUMN name="N" />
    <COLUMN name="O" />
    <COLUMN name="P" />
    <COLUMN name="Q" />
    <COLUMN name="R" />
    <COLUMN name="S" />
    <COLUMN name="T" />
    <COLUMN name="U" />
    <COLUMN name="V" />
    <COLUMN name="W" />
    <COLUMN name="X" />
    <COLUMN name="Y" />
    <COLUMN name="Z" />
  </xsl:variable>

  <xsl:variable name="prioritiesExist" select="/DECISION_MATRIX/@PRIORITY_COLUMN='Yes'"></xsl:variable>
  <xsl:variable name="scoresExist" select="/DECISION_MATRIX/@SCORING_COLUMN='Yes'"></xsl:variable>
  <xsl:variable name="contradictionsExist" select="(/DECISION_MATRIX/@CONTRADICTION_COLUMN='Yes') and (/DECISION_MATRIX/CONTRADICTIONS/COMBINATION)"></xsl:variable>
  <xsl:variable name="outcomesExist" select="/DECISION_MATRIX/OUTCOMES/OUTCOME"></xsl:variable>
  <xsl:variable name="questionsExist" select="/DECISION_MATRIX/QUESTIONS/QUESTION"></xsl:variable>

  <xsl:template match="DECISION_MATRIX">
    <xsl:call-template name="output-menus" />
    <xsl:call-template name="create-temp-elements" />
    <xsl:call-template name="create-starting-questions-outcomes" />
    <xsl:call-template name="set-matrix-id" />

    <div id="container" class="matrix-container matrix-container-eval">
      <div id="matrix" class="matrix-eval">
        <xsl:call-template name="draw-left-header" />
        <xsl:call-template name="draw-right-header" />
        <div id="bottom-left" class="bottom-left bottom-left-eval bottom-eval">
          <div id="bottom-left-main" class="bottom-left-main bottom-left-main-eval bottom-left-eval" >
            <xsl:choose>
              <xsl:when test="$questionsExist">
                <xsl:apply-templates select="QUESTIONS" mode="left" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:comment>c</xsl:comment>
              </xsl:otherwise>
            </xsl:choose>
          </div>
          <div id="bottom-left-filler" class="bottom-left-filler bottom-left-filler-eval">
              <div id="bottom-left-filler-number" class="bottom-left-number-filler number-col-eval bottom-left-number-filler-eval" >
                <xsl:comment>c</xsl:comment>
              </div>
              <div id="bottom-left-filler-main">
                <xsl:comment>c</xsl:comment>
              </div>
          </div>
        </div>
        <div id="bottom-right" class="bottom-right bottom-right-eval bottom-eval">
          <xsl:choose>
              <xsl:when test="$questionsExist">
                <xsl:apply-templates select="QUESTIONS" mode="right" />
              </xsl:when>
              <xsl:otherwise>
                <xsl:comment>c</xsl:comment>
              </xsl:otherwise>
            </xsl:choose>
        </div>

        <input type="hidden" id="{concat($mx-prefix, 'deletedQuestions')}" value=""/>
        <input type="hidden" id="{concat($mx-prefix, 'deletedOutcomes')}" value=""/>
        <input type="hidden" id="{concat($mx-prefix, 'questionOrder')}"  value=""/>
        <input type="hidden" id="{concat($mx-prefix, 'outcomeOrder')}"  value=""/>
      </div>
      <div id="buttons" class="buttons">
         <span>
           <a id="addQuestions" href="#" class="link">
            <xsl:attribute name="title">
               <xsl:value-of select="$label-add-questions" />
             </xsl:attribute>
             <xsl:attribute name="onclick">
               <xsl:text>curam.matrix.Constants.container.matrix.openAddQuestionsPopupWindow('</xsl:text>
               <xsl:value-of select="$question-popup-page"/>
               <xsl:text>','</xsl:text>
               <xsl:value-of select="@ID" />
               <xsl:text>','</xsl:text>
               <xsl:value-of select="$question-popup-page-width" />
               <xsl:text>','</xsl:text>
               <xsl:value-of select="$question-popup-page-height" />
               <xsl:text>');</xsl:text>
             </xsl:attribute>
             <xsl:value-of select="$label-add-questions" />
           </a>
         </span>
         <span>
           <a onclick="curam.matrix.Constants.container.matrix.addContradictionColumn();" id="addContradictions" href="#" class="link">
            <xsl:attribute name="title">
               <xsl:value-of select="$label-add-contradictions" />
             </xsl:attribute>
            <xsl:value-of select="$label-add-contradictions" /></a>
         </span>
         <span>
           <a onclick="curam.matrix.Constants.container.matrix.addPriorityColumn();" id="addPriority" href="#" class="link">
              <xsl:attribute name="title">
               <xsl:value-of select="$label-add-priority" />
             </xsl:attribute>
             <xsl:value-of select="$label-add-priority" />
           </a>
         </span>
         <span>
         <a onclick="curam.matrix.Constants.container.matrix.addScoreColumn();" id="addScore" href="#"  class="link">
            <xsl:attribute name="title">
              <xsl:value-of select="$label-add-score" />
          </xsl:attribute>
         <xsl:value-of select="$label-add-score" />
         </a>
         </span>
         <span><a id="addOutcomes" href="#" title="Add Outcomes" class="link">
           <xsl:attribute name="onclick">
             <xsl:text>curam.matrix.Constants.container.matrix.openAddOutomesPopupWindow('</xsl:text>
             <xsl:value-of select="$outcome-popup-page"/>
             <xsl:text>','</xsl:text>
             <xsl:value-of select="@ID" />
             <xsl:text>','</xsl:text>
             <xsl:value-of select="$outcome-popup-page-width" />
             <xsl:text>','</xsl:text>
             <xsl:value-of select="$outcome-popup-page-height" />
             <xsl:text>');</xsl:text>
           </xsl:attribute>
           <xsl:value-of select="$label-add-outcome" /></a></span>
       </div>
      <div id="validation" class="validation">
        <div id="validation-text" class="hidden-validation"><xsl:comment>c</xsl:comment></div>
      </div>
    </div>

    <xsl:call-template name="set-script-variables" />
        <script type="text/javascript">
          var localeList = new String();
          _mov=function(){};
            dojo.addOnLoad(function() {
            var msgs = {};
            msgs.addQuestions = '<xsl:value-of select="$label-add-questions" />';
            msgs.contradictionsSingleWarningMsg = '<xsl:value-of select="$validation-contradictions-single" />';
            msgs.contradictionsQuestionMsg = '<xsl:value-of select="$validation-contradictions-question" />';
            msgs.contradictionsWarningMsg = '<xsl:value-of select="$validation-contradictions-warning" />';
            msgs.contradictionsErrorMsg = '<xsl:value-of select="$validation-contradictions-error" />';
            msgs.contradictionsTooFewQuestions = '<xsl:value-of select="$validation-contradictions-few" />';
            msgs.outcomeWarningMsg = '<xsl:value-of select="$validation-outcome-warning" />';
            msgs.outcomeErrorMsg = '<xsl:value-of select="$validation-outcome-error" />';
            msgs.outcomeCopyErrorMsg = '<xsl:value-of select="$validation-outcome-copy" />';
            msgs.questionEmptyMatrix = '<xsl:value-of select="$validation-questions-empty" />';
            msgs.headerContradictions = '<xsl:value-of select="$header-contradictions" />';
            msgs.headerScore = '<xsl:value-of select="$header-score" />';
            msgs.headerPriority = '<xsl:value-of select="$header-priority" />';
            msgs.labelSpecificValue = '<xsl:value-of select="$label-specific-value" />';
            msgs.labelMin = '<xsl:value-of select="$label-min" />';
            msgs.labelMax = '<xsl:value-of select="$label-max" />';
            msgs.emptyMsgDelete = '<xsl:value-of select="$validation-message-empty-delete" />';
            msgs.emptyMsg= '<xsl:value-of select="$validation-message-empty" />';
            msgs.controlDelete= '<xsl:value-of select="$control-delete" />';
            msgs.onePopulate= '<xsl:value-of select="$validation-one-populate" />';
            msgs.overlapMsg= '<xsl:value-of select="$validation-overlap" />';
                                                msgs.duplicateMsg= '<xsl:value-of select="$validation-duplicates" />';
            msgs.maxMin= '<xsl:value-of select="$validation-max-min" />';
            msgs.booleanTrue= '<xsl:value-of select="$boolean-true" />';
            msgs.booleanFalse= '<xsl:value-of select="$boolean-false" />';

            curam.matrix.Constants.container = new curam.Container('container', 'matrix', opts, msgs);
            _mov = curam.matrix.util.buttonMouseOver;
            if (!jsScreenContext.hasContextBits('MODAL')) {
              curam.matrix.Constants.container.layout();
            }
          });
    </script>
  </xsl:template>

  <xsl:template name="set-matrix-id">
    <input type="hidden">
      <xsl:variable name="id" select="concat($mx-prefix, 'matrix-id')" />
      <xsl:attribute name="id">
        <xsl:value-of select="$id" />
      </xsl:attribute>
      <xsl:attribute name="value">
        <xsl:value-of select="@ID" />
      </xsl:attribute>
    </input>
  </xsl:template>

  <xsl:template name="set-script-variables">
    <script type="text/javascript">
      var opts = {};opts['inputPrefix'] = '<xsl:value-of select="$mx-prefix"/>';
      <xsl:choose>
        <xsl:when test="$prioritiesExist">opts['priorityExists'] = true;</xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$scoresExist">opts['scoreExists'] = true;</xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$contradictionsExist">opts['contradictionsExist'] = true;</xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$outcomesExist">opts['outcomesExist'] = true;</xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$prioritiesExist">opts['prioritiesExist'] = true;</xsl:when>
      </xsl:choose>
    </script>
  </xsl:template>



  <xsl:template name="draw-left-header">
    <div id="top-left-filler" class="top-left-filler top-left-filler-eval number-col-eval">
    <xsl:comment>c</xsl:comment>
    </div>
    <div id="top-left" class="top-left top-eval top-left-eval">
        <div id="top-left-top" class="top-left-eval top-top-eval">
            <div id="column-id-a" class="column-id-a column-eval qt-col-eval">
                <div>A</div>
            </div>
            <div id="column-id-b" class="column-id-b column-eval ans-col-eval">
                <div>B</div>
            </div>
        </div>
        <div id="top-left-bottom" class="top-left-eval top-bottom-eval">
            <div id="heading-questions" class="column-heading column-eval qt-col-eval">
                <div><xsl:value-of select="$header-questions" /></div>
            </div>
            <div id="heading-answers" class="column-heading column-eval ans-col-eval">
                <div><xsl:value-of select="$header-answer-value" /></div>
            </div>
        </div>
    </div>
  </xsl:template>


  <xsl:template name="draw-right-header">
    <div id="top-right" class="top-right top-right-eval top-eval">
      <div id="top-right-top" class="right-eval top-top-eval">
        <xsl:choose>
          <xsl:when test="$questionsExist">
            <xsl:if test="$prioritiesExist">
              <div id="column-id-pri" class="column-id column-eval pri-col-eval"
                   onmouseover="_mov(arguments[0]);">
                <div>C</div>
              </div>
            </xsl:if>
            <xsl:if test="$scoresExist">
              <div id="column-id-scr" class="column-id column-eval pri-col-eval"
                 onmouseover="_mov(arguments[0]);">
                <div>
                  <xsl:choose>
                    <xsl:when test="$prioritiesExist">D</xsl:when>
                    <xsl:otherwise>C</xsl:otherwise>
                  </xsl:choose>
                </div>
              </div>
            </xsl:if>
            <xsl:if test="$contradictionsExist">
              <div id="column-id-contr" class="column-id column-eval contr-col-eval"
                 onmouseover="_mov(arguments[0]);">
                <div>
                  <xsl:choose>
                    <xsl:when test="$prioritiesExist and $scoresExist">E</xsl:when>
                    <xsl:when test="$prioritiesExist or $scoresExist">D</xsl:when>
                    <xsl:otherwise>C</xsl:otherwise>
                  </xsl:choose>
                </div>
                <xsl:call-template name="contradiction-combinations" />
              </div>
            </xsl:if>
            <xsl:apply-templates select="OUTCOMES/OUTCOME" mode="top-right-top">
              <xsl:with-param name="letter-pos">
                <xsl:choose>
                  <xsl:when test="$contradictionsExist and $prioritiesExist and $scoresExist">
                    <xsl:value-of select="3" />
                  </xsl:when>
                  <xsl:when test="($contradictionsExist and $prioritiesExist and not($scoresExist))
                                 or ($contradictionsExist and $scoresExist and not($prioritiesExist))
                                 or ($prioritiesExist and $scoresExist and not($contradictionsExist))">
                    <xsl:value-of select="2" />
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="1" />
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:with-param>
            </xsl:apply-templates>
          </xsl:when>
          <xsl:otherwise>
            <xsl:comment>c</xsl:comment>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:comment>c</xsl:comment>
      </div>
      <div id="top-right-bottom" class="right-eval top-bottom-eval">
        <xsl:comment>c</xsl:comment>
        <xsl:choose>
          <xsl:when test="$questionsExist">
            <xsl:if test="$prioritiesExist">
              <div id="heading-pri" class="column-heading column-eval pri-col-eval">
                  <div>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$header-priority" />
                    </xsl:attribute>
                    <xsl:value-of select="$header-priority" />
                  </div>
              </div>
            </xsl:if>
            <xsl:if test="$scoresExist">
               <div id="heading-scr" class="column-heading column-eval pri-col-eval">
                  <div>
                    <xsl:attribute name="title">
                      <xsl:value-of select="$header-score" />
                    </xsl:attribute>
                    <xsl:value-of select="$header-score" />
                  </div>
               </div>
            </xsl:if>
            <xsl:if test="$contradictionsExist">
              <div id="heading-contr" class="column-heading column-eval contr-col-eval">
                <a>
                  <xsl:attribute name="title">
                    <xsl:value-of select="'Contradictions'" />
                  </xsl:attribute>
                  <xsl:value-of select="$header-contradictions" />
                </a>
              </div>
            </xsl:if>
            <xsl:apply-templates select="OUTCOMES/OUTCOME" mode="top-right-bottom" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:comment>c</xsl:comment>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </div>
    <div id="top-right-filler" class="top-right-filler top-eval top-right-filler-eval">
        <div class="top-right-filler-top column-eval top-right-filler-contents-eval">
          <xsl:comment>c</xsl:comment>
        </div>
        <div class="top-right-filler-bottom column-eval top-right-filler-contents-eval">
          <xsl:comment>c</xsl:comment>
        </div>
    </div>
  </xsl:template>



  <xsl:template name="contradiction-combinations">
    <xsl:apply-templates select="CONTRADICTIONS/COMBINATION" mode="contradictions" />
    <xsl:apply-templates select="CONTRADICTIONS/COMBINATION/MESSAGE" mode="contradiction-messages" />
  </xsl:template>

  <xsl:template match="COMBINATION" mode="contradictions">
    <input type="hidden" id="{concat($mx-prefix, 'contrcombid.', position())}" value="{@COMBINATION_ID}"/>
    <xsl:apply-templates select="MESSAGE" mode="contradiction-message">
      <xsl:with-param name="comb-id" select="position()" />
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="MESSAGE" mode="contradiction-message">
    <xsl:param name="comb-id" />
    <input type="hidden" id="{concat($mx-prefix, 'contrmsg.', $comb-id, '.', @LOCALE)}" value="{@TEXT}"/>
  </xsl:template>

  <xsl:template match="OUTCOME" mode="top-right-top">
    <xsl:param name="letter-pos" />
    <xsl:variable name="outcome-position" select="position()"/>
    <xsl:variable name="id" select="@OUTCOME_ID" />
    <xsl:variable name="pos" select="$letter-pos + $outcome-position" />
    <xsl:variable name="prefix">
      <xsl:choose>
      <xsl:when test="$pos &lt;= 24">
       <xsl:value-of select="xalan:nodeset($naming)/COLUMN[position() = $pos]/@name" />
     </xsl:when>
     <xsl:otherwise>
       <xsl:value-of select="xalan:nodeset($naming)/COLUMN[position() = ($pos mod 23)]/@name" />
       <xsl:value-of select="floor($pos div 23)" />
     </xsl:otherwise>
     </xsl:choose>
    </xsl:variable>
    <div id="column-id-{$id}" class="column-id column-eval out-{$id}-col-eval"
         onmouseover="_mov(arguments[0]);">
      <div><xsl:value-of select="$prefix" /></div>
      <xsl:apply-templates select="COMBINATION" mode="combinations">
        <xsl:with-param name="out-id" select="@OUTCOME_ID" />
      </xsl:apply-templates>
    </div>
  </xsl:template>

  <xsl:template match="COMBINATION" mode="combinations">
    <xsl:param name="out-id" />
    <input type="hidden" id="{concat($mx-prefix, 'outcombid.', $out-id, '.', position())}" value="{@COMBINATION_ID}"/>
  </xsl:template>

  <xsl:template match="OUTCOME" mode="top-right-bottom">
    <xsl:variable name="id" select="@OUTCOME_ID" />
    <div id="heading-{$id}" class="column-heading column-eval out-{$id}-col-eval">
      <a>
  <xsl:attribute name="title">
    <xsl:value-of select="@LABEL" />
  </xsl:attribute>
  <xsl:choose>
    <xsl:when test="@LABEL">
      <xsl:value-of select="@LABEL" />
    </xsl:when>
    <xsl:otherwise>
      <xsl:comment>c</xsl:comment>
    </xsl:otherwise>
        </xsl:choose>
      </a>
    </div>
  </xsl:template>

  <xsl:template match="QUESTIONS" mode="left">
    <xsl:apply-templates select="QUESTION" mode="left-side" />
  </xsl:template>

  <xsl:template match="QUESTIONS" mode="right">
    <xsl:apply-templates select="QUESTION" mode="right-side" />
  </xsl:template>

  <xsl:template match="QUESTION" mode="left-side">
    <xsl:apply-templates select="." mode="normal">
      <xsl:with-param name="q-id" select="@QUESTION_ID" />
      <xsl:with-param name="domain" select="@SUB_TYPE" />
      <xsl:with-param name="q-num" select="position()" />
    </xsl:apply-templates>
  </xsl:template>

  <xsl:template match="QUESTION" mode="right-side">
    <div id="qr-{@QUESTION_ID}" class="{@QUESTION_ID}-eval right-eval">
      <xsl:variable name="ques-id" select="@QUESTION_ID"/>
      <xsl:variable name="ques-num" select="position()"/>
      <xsl:comment>c</xsl:comment>
      <xsl:if test="$prioritiesExist">
        <div id="pri-group-{@QUESTION_ID}" class="q-ct pri-col-eval q-ct-eval-{@QUESTION_ID}">
          <xsl:apply-templates select="ANSWER" mode="priority">
            <xsl:with-param name="ques-id" select="$ques-id" />
            <xsl:with-param name="ques-num" select="$ques-num"/>
          </xsl:apply-templates>
        </div>
      </xsl:if>
      <xsl:if test="$scoresExist">
        <div id="scr-group-{@QUESTION_ID}" class="q-ct pri-col-eval q-ct-eval-{@QUESTION_ID}">
          <xsl:apply-templates select="ANSWER" mode="score">
            <xsl:with-param name="ques-id" select="$ques-id" />
            <xsl:with-param name="ques-num" select="$ques-num"/>
          </xsl:apply-templates>
        </div>
      </xsl:if>
      <xsl:if test="$contradictionsExist">
        <div id="contr-group-{@QUESTION_ID}" class="q-ct q-ct-eval-{@QUESTION_ID} contr-col-eval">
          <xsl:apply-templates select="ANSWER" mode="contradiction-combinations">
            <xsl:with-param name="ques-id" select="$ques-id" />
            <xsl:with-param name="ques-num" select="$ques-num" />
          </xsl:apply-templates>
        </div>
      </xsl:if>

      <xsl:if test="$outcomesExist">
        <xsl:variable name="ques-context" select="."/>
        <xsl:for-each select="../../OUTCOMES/OUTCOME">
          <xsl:variable name="out-id" select="@OUTCOME_ID"/>
          <div id="out-{$out-id}-{$ques-id}" class="q-ct q-ct-eval-{$ques-id} out-{$out-id}-col-eval">
            <xsl:apply-templates select="$ques-context/ANSWER" mode="outcome-combinations">
              <xsl:with-param name="ques-id" select="$ques-id" />
              <xsl:with-param name="ques-num" select="$ques-num" />
              <xsl:with-param name="out-id" select="$out-id" />
            </xsl:apply-templates>
          </div>
        </xsl:for-each>
      </xsl:if>
    </div>
  </xsl:template>

  <xsl:template match="ANSWER" mode="priority">
    <xsl:param name="ques-id" />
    <xsl:param name="ques-num" />
    <xsl:variable name="ans-num" select="position()" />
    <xsl:variable name="cell-style">
      <xsl:choose>
        <xsl:when test="$ans-num = 1"></xsl:when>
        <xsl:otherwise>ans</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="first-row-override">
      <xsl:choose>
        <xsl:when test="($ans-num = 1) and ($ques-num = 1)">-with-menu</xsl:when>
        <xsl:otherwise></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <div id="pri-{$ques-id}-{position()}" class="{$cell-style} ans-eval{$first-row-override} pri-col-eval">
      <div id="pri-val-{$ques-id}-{position()}" class="pri-val pri-val-eval ans-str-val-eval{$first-row-override}">
       <input type="text" value="{@PRIORITY}" class="pri-input-eval" onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
         <xsl:attribute name="id">
           <xsl:value-of select="concat($mx-prefix, 'priority.s.s.', $ques-id, '.', position())" />
         </xsl:attribute>
       </input>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="ANSWER" mode="score">
    <xsl:param name="ques-id" />
    <xsl:param name="ques-num" />
    <xsl:variable name="ans-num" select="position()" />
    <xsl:variable name="cell-style">
      <xsl:choose>
        <xsl:when test="$ans-num = 1"></xsl:when>
        <xsl:otherwise>ans</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="first-row-override">
      <xsl:choose>
        <xsl:when test="($ans-num = 1) and ($ques-num = 1)">-with-menu</xsl:when>
        <xsl:otherwise></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <div id="scr-{$ques-id}-{position()}" class="{$cell-style} ans-eval{$first-row-override} pri-col-eval">
      <div id="scr-val-{$ques-id}-{position()}" class="pri-val pri-val-eval ans-str-val-eval{$first-row-override}">
       <input type="text" value="{@SCORE}" class="pri-input-eval" onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
         <xsl:attribute name="id">
           <xsl:value-of select="concat($mx-prefix, 'score.s.s.', $ques-id, '.', position())" />
         </xsl:attribute>
       </input>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="QUESTION" mode="normal">
    <xsl:param name="q-id" />
    <xsl:param name="domain" />
    <xsl:param name="q-num" />
    <xsl:variable name="domain-nodeset" select="domain-info:getDomainNodeSet($domain)" />
    <xsl:variable name="root-domain-name" select="$domain-nodeset/@root-domain" />
    <xsl:variable name="ct-name" select="$domain-nodeset/@code-table-name" />
    <xsl:variable name="q-prefix" select="concat('ql-',$q-id)" />
    <div id="{$q-prefix}" class="bottom-left-eval id-eval">
      <xsl:variable name="answer-type">
        <xsl:choose>
          <xsl:when test="$root-domain-name = 'SVR_BOOLEAN'">boolean</xsl:when>
          <xsl:when test="$domain = 'SVR_BOOLEAN'">boolean</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_INT8'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_INT8'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_INT16'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_INT16'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_INT32'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_INT32'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_INT64'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_INT64'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_FLOAT'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_FLOAT'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_DOUBLE'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_DOUBLE'">numeric</xsl:when>
          <xsl:when test="$root-domain-name = 'SVR_MONEY'">numeric</xsl:when>
          <xsl:when test="$domain = 'SVR_MONEY'">numeric</xsl:when>
          <xsl:when test="$ct-name != ''">codetable-domain</xsl:when>
          <xsl:when test="$root-domain-name = 'SHORT_CODETABLE_CODE'">codetable-domain</xsl:when>
          <xsl:when test="$domain = 'SHORT_CODETABLE_CODE'">codetable-domain</xsl:when>
          <xsl:when test="@TYPE='CODETABLE'">codetable-name</xsl:when>
          <xsl:otherwise>string</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="answer-value">
        <xsl:choose>
          <xsl:when test="$answer-type = 'codetable-name'">codetable</xsl:when>
          <xsl:when test="$answer-type = 'codetable-domain'">codetable</xsl:when>
          <xsl:otherwise><xsl:value-of select="$answer-type" /></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <div id="num-{$q-id}" class="number number-col-eval q-ct-eval-{$q-id}"
            onmouseover="_mov(arguments[0]);">
        <div class="number-text number-text-{$q-id}-eval">
          <xsl:value-of select="$q-num" />
        </div>
      </div>
      <div id="ques-{$q-id}" class="q-ct q-ct-eval-{$q-id} qt-col-eval">
        <div class="qt-text qt-text-{$q-id}-eval">
          <xsl:choose>
            <xsl:when test="@LABEL">
              <a>
                <xsl:attribute name="title">
                  <xsl:value-of select="@LABEL" />
                </xsl:attribute>
                <xsl:value-of select="@LABEL" />
              </a>
            </xsl:when>
            <xsl:otherwise>
              <xsl:comment>c</xsl:comment>
            </xsl:otherwise>
          </xsl:choose>
        </div>
      </div>
      <div id="ans-group-{$q-id}" class="q-ct q-ct-eval-{$q-id} ans-col-eval">
        <xsl:apply-templates select="ANSWER" mode="main">
          <xsl:with-param name="q-id" select="$q-id" />
          <xsl:with-param name="answer-type" select="$answer-type" />
          <xsl:with-param name="q-num" select="$q-num" />
          <xsl:with-param name="sub-type" select="@SUB_TYPE" />
          <xsl:with-param name="ct-name" select="$ct-name" />
        </xsl:apply-templates>
      </div>
    </div>
  </xsl:template>

  <xsl:template match="ANSWER" mode="main">
    <xsl:param name="answer-type" />
    <xsl:param name="q-id" />
    <xsl:param name="q-num"/>
    <xsl:param name="sub-type" />
    <xsl:param name="ct-name" />
    <xsl:variable name="a-prefix" select="concat('ans-',$q-id,'-',position())" />
    <xsl:variable name="cell-style">
      <xsl:choose>
        <xsl:when test="position() = 1"></xsl:when>
        <xsl:otherwise>ans</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="first-cell-correction">
      <xsl:choose>
        <xsl:when test="position() = 1 and ($q-num = 1)">-with-menu</xsl:when>
       <xsl:otherwise></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <div id="{$a-prefix}" class="{$cell-style} ans-col-eval ans-eval{$first-cell-correction}">
      <div id="ans-val-{$q-id}-{position()}">
        <xsl:attribute name="class">
          <xsl:choose>
            <xsl:when test="$answer-type = 'codetable-name' or $answer-type = 'codetable-domain'">
              <xsl:value-of select="concat('ans-val ans-val-eval ans-ct-val', $first-cell-correction)" />
            </xsl:when>
            <xsl:when test="$answer-type = 'boolean'">
              <xsl:value-of select="concat('ans-val ans-val-eval ans-bool-val-eval',$first-cell-correction)" />
            </xsl:when>
            <xsl:when test="$answer-type = 'string'">
              <xsl:value-of select="concat('ans-val ans-val-eval ans-str-val-eval',$first-cell-correction)" />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="concat('ans-val ans-val-eval ans-num-val-eval',$first-cell-correction)" />
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:choose>
          <xsl:when test="$answer-type = 'codetable-name'">
            <xsl:variable name="ctName" select="$sub-type"/>
            <xsl:variable name="code-table" select="code-table:getCodeTableNodeSet($ctName, $locale)" />
            <xsl:call-template name="gen-code-table-list-field">
              <xsl:with-param name="name" select="concat($mx-prefix, 'value.s.s.', $q-id,'.', position())" />
              <xsl:with-param name="value" select="@VALUE"/>
              <xsl:with-param name="code-table" select="$code-table"/>
              <xsl:with-param name="style" select="'answer-input-eval'" />
              <xsl:with-param name="no-blank" select="'true'" />
            </xsl:call-template>
          </xsl:when>
          <xsl:when test="$answer-type = 'codetable-domain'">
            <xsl:variable name="code-table" select="code-table:getCodeTableNodeSet($ct-name, $locale)"/>
            <xsl:call-template name="gen-code-table-list-field">
              <xsl:with-param name="name" select="concat($mx-prefix, 'value.s.s.', $q-id,'.', position())" />
              <xsl:with-param name="value" select="@VALUE"/>
              <xsl:with-param name="code-table" select="$code-table"/>
              <xsl:with-param name="style" select="'answer-input-eval'" />
              <xsl:with-param name="no-blank" select="'true'" />
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$answer-type = 'boolean'">
                <xsl:value-of select="@VALUE"/>
                <input type="hidden" value="{@VALUE}">
                  <xsl:attribute name="id">
                    <xsl:value-of select="concat($mx-prefix, 'value.s.s.', $q-id,'.', position())" />
                  </xsl:attribute>
                  <xsl:comment>c</xsl:comment>
                </input>
              </xsl:when>
              <xsl:when test="$answer-type = 'string'">
                <input type="text" value="{@VALUE}" class="answer-input-eval"  onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
                  <xsl:attribute name="id">
                    <xsl:value-of
                      select="concat($mx-prefix, 'value.s.s.', $q-id,'.', position())" />
                  </xsl:attribute>
                  <xsl:comment>c</xsl:comment>
                </input>
              </xsl:when>
              <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="@VALUE">
                    <div class="label-specific-value">
                      <xsl:value-of select="$label-specific-value" />
                      <xsl:text>:</xsl:text>
                     </div>
                    <input type="text" value="{@VALUE}" class="numeric-input-eval"  onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
                      <xsl:attribute name="id">
                        <xsl:value-of select="concat($mx-prefix, 'value.s.s.', $q-id,'.', position())" />
                      </xsl:attribute>
                      <xsl:attribute name="title">
                        <xsl:value-of select="$label-specific-value" />
                      </xsl:attribute>                      
                      <xsl:comment>c</xsl:comment>
                    </input>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:if test="@MIN or @MAX">
                    <div class="label-min-max">
                      <xsl:value-of select="$label-min" />
                      <xsl:text>:</xsl:text>
                    </div>
                      <input type="text" class="numeric-input-eval" onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
                        <xsl:attribute name="id">
                          <xsl:value-of select="concat($mx-prefix, 'min.s.s.', $q-id,'.', position())" />
                        </xsl:attribute>
                        <xsl:if test="@MIN">
                          <xsl:attribute name="value">
                            <xsl:value-of select="@MIN" />
                          </xsl:attribute>
                          <xsl:attribute name="title">
                            <xsl:value-of select="$label-min" />
                          </xsl:attribute>                        
                        </xsl:if>
                        <xsl:comment>c</xsl:comment>
                      </input>
                      <xsl:text> </xsl:text>
                      <div class="label-min-max">
                        <xsl:value-of select="$label-max" />
                        <xsl:text>:</xsl:text>
                      </div>
                      <input type="text" class="numeric-input-eval" onfocus="curam.matrix.Constants.container.matrix.cf(arguments);">
                        <xsl:attribute name="id">
                          <xsl:value-of select="concat($mx-prefix, 'max.s.s.', $q-id,'.', position())" />
                        </xsl:attribute>
                          <xsl:attribute name="title">
                            <xsl:value-of select="$label-max" />
                          </xsl:attribute>
                        <xsl:if test="@MAX">
                          <xsl:attribute name="value">
                            <xsl:value-of select="@MAX" />
                          </xsl:attribute>
                        </xsl:if>
                        <xsl:comment>c</xsl:comment>
                      </input>
                    </xsl:if>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </div>
      <div class="image"><xsl:comment>c</xsl:comment></div>
    </div>
  </xsl:template>


  <xsl:template match="ANSWER" mode="contradiction-combinations">
    <xsl:param name="ques-id" />
    <xsl:param name="ques-num" />
    <xsl:variable name="ans-num" select="position()" />

    <div id="contr-row-{$ques-id}-{$ans-num}" class="contr-col-eval">
      <xsl:for-each select="../../../CONTRADICTIONS/COMBINATION">
        <xsl:variable name="cell-num" select="position()" />
        <xsl:variable name="cell-style">
          <xsl:choose>
            <xsl:when test="($ans-num = 1) and (position() != last())">cell-first-row</xsl:when>
            <xsl:when test="($ans-num = 1) and position() = last()">cell-first-row cell-no-border</xsl:when>
            <xsl:when test="($ans-num &gt; 1) and (position() = last())">cell-last-col</xsl:when>
            <xsl:otherwise>cell</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="first-row-override">
          <xsl:choose>
            <xsl:when test="($ans-num = 1) and ($ques-num = 1)">-with-menu</xsl:when>
            <xsl:otherwise></xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <div id="contr-cell-{$ques-id}-{$ans-num}-{$cell-num}" class="{$cell-style} ans-eval{$first-row-override}">
          <input type="checkbox" class="cbox-eval{$first-row-override} contr-cbox-eval">
            <xsl:attribute name="id">
                <xsl:value-of select="concat($mx-prefix,'contrCell.',position(),'.s.', $ques-id, '.', $ans-num)" />
            </xsl:attribute>
            <xsl:attribute name="onclick">
              <xsl:text>curam.matrix.Constants.container.matrix.scv('</xsl:text>
              <xsl:value-of select="$cell-num"/>
              <xsl:text>',this,arguments[0],'</xsl:text>
              <xsl:value-of select="$ques-id" />
              <xsl:text>'); return true;</xsl:text>
            </xsl:attribute>
            <xsl:if test="SELECTION[@QUESTION_ID = $ques-id][@ANSWER_ID = $ans-num]">
              <xsl:attribute name="checked">
                  <xsl:value-of select="'checked'" />
              </xsl:attribute>
            </xsl:if>
          </input>
          <xsl:choose>
            <xsl:when test="($ques-num = 1) and ($ans-num = 1)">
              <div id="contr-img-{$ques-id}-{$ans-num}-{$cell-num}" class="image"><xsl:comment>c</xsl:comment></div>
            </xsl:when>
          </xsl:choose>
        </div>
      </xsl:for-each>
    </div>
  </xsl:template>


  <xsl:template match="ANSWER" mode="outcome-combinations">
    <xsl:param name="ques-id" />
    <xsl:param name="ques-num" />
    <xsl:param name="out-id" />
    <xsl:variable name="ans-num" select="position()" />
    <div id="out-{$out-id}-row-{$ques-id}-{$ans-num}" class="out-{$out-id}-col-eval">
      <xsl:for-each select="../../../OUTCOMES/OUTCOME[@OUTCOME_ID = $out-id]/COMBINATION" >
        <xsl:variable name="cell-style">
          <xsl:choose>
            <xsl:when test="($ans-num = 1) and (position() != last())">cell-first-row</xsl:when>
            <xsl:when test="($ans-num = 1) and position() = last()">cell-first-row cell-no-border</xsl:when>
            <xsl:when test="($ans-num &gt; 1) and (position() = last())">cell-last-col</xsl:when>
            <xsl:otherwise>cell</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <xsl:variable name="first-row-override">
          <xsl:choose>
            <xsl:when test="($ans-num = 1) and ($ques-num = 1)">-with-menu</xsl:when>
            <xsl:otherwise></xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <div id="out-{$out-id}-cell-{$ques-id}-{$ans-num}-{position()}" class="{$cell-style} ans-eval{$first-row-override}">
          <input type="checkbox" class="cbox-eval{$first-row-override}">
            <xsl:attribute name="id">
                <xsl:value-of select="concat($mx-prefix, 'outCell.', $out-id, '.', $ques-id, '.', $ans-num, '.', position())" />
            </xsl:attribute>
            <xsl:attribute name="onclick">
              <xsl:text>curam.matrix.Constants.container.matrix.sov('</xsl:text>
              <xsl:value-of select="$out-id"/>
              <xsl:text>',</xsl:text>
              <xsl:value-of select="position()" />
              <xsl:text>,this,arguments[0]); return true;</xsl:text>
            </xsl:attribute>
            <xsl:if test="SELECTION[@QUESTION_ID = $ques-id][@ANSWER_ID = $ans-num]">
              <xsl:attribute name="checked">
                  <xsl:value-of select="'checked'" />
              </xsl:attribute>
            </xsl:if>
          </input>
          <xsl:choose>
            <xsl:when test="($ques-num = 1) and ($ans-num = 1)">
              <div id="out-{$out-id}-img-{$ques-id}-{$ans-num}-{position()}" class="image"><xsl:comment>c</xsl:comment></div>
            </xsl:when>
          </xsl:choose>
        </div>
      </xsl:for-each>
    </div>
  </xsl:template>


  <xsl:template name="create-temp-elements">
    <div id="temp-elements" class="matrix-container">
      <div id="scroll" style="width:50px;height:50px;overflow:scroll"><xsl:comment>c</xsl:comment></div>
      <div>
        <span id="num-width">888</span>
        <span id="ques-text-width">Question Question Question Question</span>
        <span id="ans-values-width">Answer Values Answer Values</span>
        <span id="num-ans" style="font-weight:bold">Min: Max:</span>
        <span id="priority-heading" style="font-weight:bold;padding:5px;border-right:1px solid white;">Priority</span>
      </div>
      <div id="ct-ans">
        <div id="ct-ans-val" class="ans-val ans-ct-val">
          <select id="select" name="" >
            <option value="" selected="selected">Option</option>
          </select>
        </div>
        <div id="image" class="image"><xsl:comment>c</xsl:comment></div>
      </div>
      <div id="str-ans" class="ans-val">
        <input type="text"><xsl:comment>c</xsl:comment></input>
      </div>
      <div id="bool-ans" class="ans-val">
        <xsl:value-of select="$boolean-true" />
      </div>
      <div id="cell" class="cell-first-row">
        <input id="cell-input" type="checkbox" style="position:absolute;float:left"/>
        <div class="image"><xsl:comment>c</xsl:comment></div>
      </div>
      <div id="priority">
        <div id="pri-val" class="pri-val">
          <input id="pri-input" type="text" value=""  />
        </div>
      </div>
    </div>
  </xsl:template>


  <xsl:template name="output-menus">
    <div dojoType="curam.widget.MatrixPopupMenu" id="QuestionOptions" container="container"
      toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deleteQuestion"
        onclick="curam.matrix.Constants.container.matrix.deleteQuestion(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-delete-question" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="addAnswer"
        onclick="curam.matrix.Constants.container.matrix.addAnswer(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-add-answer" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

    <div dojoType="curam.widget.MatrixPopupMenu" id="AnswerOptions" container="container"
      toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deleteAnswer"
        onclick="curam.matrix.Constants.container.matrix.deleteAnswer(activeMenuID)">
       <xsl:attribute name="label"><xsl:value-of select="$control-delete-ans" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem" id="useValue"
        onclick="curam.matrix.Constants.container.matrix.changeNumericAnswer(activeMenuID, 'specificvalue')">
        <xsl:attribute name="label"><xsl:value-of select="$control-value" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="minmax"
        onclick="curam.matrix.Constants.container.matrix.changeNumericAnswer(activeMenuID, 'minmax')">
        <xsl:attribute name="label"><xsl:value-of select="$control-minmax" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

    <div dojoType="curam.widget.MatrixPopupMenu" id="OutcomeOptions" container="container"
      toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deleteOutcome"
        onclick="curam.matrix.Constants.container.matrix.deleteOutcome(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-delete-outcome" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="addCombination"
        onclick="curam.matrix.Constants.container.matrix.addCombination(activeMenuID, false)">
        <xsl:attribute name="label"><xsl:value-of select="$control-add-combination" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="pasteCombination"
        onclick="curam.matrix.Constants.container.matrix.addCombination(activeMenuID, true)">
        <xsl:attribute name="label"><xsl:value-of select="$control-paste-combination" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

    <div dojoType="curam.widget.MatrixPopupMenu" id="CombinationOptions" container="container"
      toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deleteCombination"
        onclick="curam.matrix.Constants.container.matrix.deleteCombination(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-delete-combination" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="copyCombination"
        onclick="curam.matrix.Constants.container.matrix.copyCombination(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-copy-combination" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="dijit.MenuSeparator">
        <xsl:comment>c</xsl:comment>
      </div>
      <div dojoType="curam.widget.MatrixMenuItem"  id="addMessage"
           onclick="curam.matrix.Constants.container.matrix.addMessages(activeMenuID)">
        <xsl:attribute name="label"><xsl:value-of select="$control-set-messages" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

    <div dojoType="curam.widget.MatrixPopupMenu" id="PriorityOptions" container="container" toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deletePriorityColumn"
        onclick="curam.matrix.Constants.container.matrix.deletePriorityColumn()">
        <xsl:attribute name="label"><xsl:value-of select="$control-delete" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

    <div dojoType="curam.widget.MatrixPopupMenu" id="ScoreOptions" container="container" toggle="wipe">
      <div dojoType="curam.widget.MatrixMenuItem"  id="deleteScoreColumn"
        onclick="curam.matrix.Constants.container.matrix.deleteScoreColumn()">
        <xsl:attribute name="label"><xsl:value-of select="$control-delete" /></xsl:attribute>
        <xsl:comment>c</xsl:comment>
      </div>
    </div>

  </xsl:template>

  <xsl:template name="create-starting-questions-outcomes">

    <xsl:call-template name="create-question-input">
      <xsl:with-param name="num-ques" select="count(QUESTIONS/QUESTION)"/>
      <xsl:with-param name="cur-ques" select="1"/>
      <xsl:with-param name="ids" select="''"/>
    </xsl:call-template>

    <xsl:call-template name="create-outcome-input">
      <xsl:with-param name="num-out" select="count(OUTCOMES/OUTCOME)"/>
      <xsl:with-param name="cur-out" select="1"/>
      <xsl:with-param name="ids" select="''"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="create-question-input">
    <xsl:param name="num-ques" />
    <xsl:param name="cur-ques" />
    <xsl:param name="ids" />

    <xsl:variable name="new-ids">
      <xsl:value-of select="concat($ids, QUESTIONS/QUESTION[position() = $cur-ques]/@QUESTION_ID, '|')"   />
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$cur-ques = $num-ques">
        <input type="hidden" value="{$new-ids}"  id="{concat($mx-prefix, 'startingQuestions')}"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="$cur-ques &lt; $num-ques">
          <xsl:call-template name="create-question-input">
            <xsl:with-param name="num-ques" select="$num-ques"/>
            <xsl:with-param name="cur-ques" select="$cur-ques+1"/>
            <xsl:with-param name="ids" select="$new-ids"/>
          </xsl:call-template>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template name="create-outcome-input">
    <xsl:param name="num-out" />
    <xsl:param name="cur-out" />
    <xsl:param name="ids" />

    <xsl:variable name="new-ids">
      <xsl:value-of select="concat($ids, OUTCOMES/OUTCOME[position() = $cur-out]/@OUTCOME_ID, '|')"   />
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$cur-out = $num-out">
        <input type="hidden" value="{$new-ids}" id="{$mx-prefix}startingOutcomes"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="$cur-out &lt; $num-out">
          <xsl:call-template name="create-outcome-input">
            <xsl:with-param name="num-out" select="$num-out"/>
            <xsl:with-param name="cur-out" select="$cur-out+1"/>
            <xsl:with-param name="ids" select="$new-ids"/>
          </xsl:call-template>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>