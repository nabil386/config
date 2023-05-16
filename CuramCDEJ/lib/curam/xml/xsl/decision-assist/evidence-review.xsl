<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:bidi-utils="http://xml.apache.org/xalan/java/curam.util.client.BidiUtils"
  exclude-result-prefixes="bidi-utils">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>   
  
  <xsl:param name="el-prefix"/>
 
  <xsl:param name="show-typical-picture-answers"/> 
  
  <xsl:param name="label-select-answer"/>
  <xsl:param name="label-question"/>
  <xsl:param name="label-question-answer"/>
  <xsl:param name="label-question-source"/>
  <xsl:param name="label-question-typical-answer"/>
  <xsl:param name="table-title-question-details"/>
  <xsl:param name="table-title-question-answers"/>
  <xsl:param name="table-column-question-select"/>
  <xsl:param name="table-column-question-source-name"/>
  <xsl:param name="table-column-question-source-desc"/>
  <xsl:param name="table-column-question-value"/>


  <xsl:template match="EVIDENCE_REVIEW">
    <xsl:variable name="node-id" select="concat('accordionDiv_',generate-id(.))"/>
    <div id="controlPanel"><span style="visibility:hidden">1</span></div>
    <div id="{$node-id}">
      <xsl:apply-templates/>
    </div>
    <script type="text/javascript">
    	<xsl:text>dojo.addOnLoad(function(){Widgets.registerAccordion("</xsl:text>
    	<xsl:value-of select="$node-id"/>
    	<xsl:text>");</xsl:text>
    	<xsl:text>disableClusterToggle("</xsl:text>
    	<xsl:value-of select="$node-id"/>
    	<xsl:text>");})</xsl:text>
    </script>
  </xsl:template>

  <xsl:template match="QUESTION">
    <xsl:variable name="chosen-answer-id" select="@CHOSEN_ANSWER_ID"/>
    <xsl:variable name="selected-answer" select="./ANSWERS/ANSWER[@ANSWER_ID=$chosen-answer-id]"/>
    <div class="accordionTab" id="panel_{@QUESTION_ID}">
      <div class="tabHeader" id="header_{@QUESTION_ID}">
        <span>
          <xsl:attribute name="dir">
            <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@LABEL)"/>
          </xsl:attribute>                
          <xsl:value-of select="@LABEL"/>
        </span>
        <span>
        <xsl:choose>
          <xsl:when test="$selected-answer">
            <xsl:value-of select="concat(' ', $selected-answer/@VALUE, ' (', $selected-answer/@NAME,',', $selected-answer/@DESCRIPTION,')')"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:if test="../@EDITABLE='true'">&lt; <xsl:value-of select="$label-select-answer"/> &gt;</xsl:if>
          </xsl:otherwise>
        </xsl:choose>
        </span>
      </div>
      <div class="tabContent" id="contents_{@QUESTION_ID}">
        <div class="tab-description-header"><xsl:value-of select="$table-title-question-details"/></div>
          <table class="tab-description" width="100%" role="presentation">
            <col width="25%"/><col width="25%"/><col width="25%"/><col width="25%"/>
            <tbody>
              <tr>
                <td class="tab-description-label">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(label-question)"/>
                </xsl:attribute>                                
                <xsl:value-of select="$label-question"/>
                </td>
                <td class="tab-description-value">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@LABEL)"/>
                </xsl:attribute>                                
                <xsl:value-of select="@LABEL"/>
                </td>
                <td class="tab-description-label">
                <xsl:attribute name="dir">
                  <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(label-question-source)"/>
                </xsl:attribute>                                
                <xsl:value-of select="$label-question-source"/>
                </td>
                <td class="tab-description-value" id="chosenSource_{@QUESTION_ID}">
                  <xsl:if test="$selected-answer">
	                <xsl:attribute name="dir">
    	              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(selected-answer/@NAME)"/>
        	        </xsl:attribute>                                                  
                    <xsl:value-of select="$selected-answer/@NAME"/>
                  </xsl:if>
                </td>
              </tr>
              <tr>
                <td class="tab-description-label">
	              <xsl:attribute name="dir">
    	            <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(label-question-answer)"/>
        	      </xsl:attribute>                                                                                    
                <xsl:value-of select="$label-question-answer"/>
                </td>
                <td class="tab-description-value" id="chosenAnswer_{@QUESTION_ID}">
                  <xsl:if test="$selected-answer">
	                <xsl:attribute name="dir">
    	              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(selected-answer/@VALUE)"/>
        	        </xsl:attribute>                                                                    
                    <xsl:value-of select="$selected-answer/@VALUE"/>
                  </xsl:if>
                </td>
                <xsl:if test="$show-typical-picture-answers = 'true'">
                  <td class="tab-description-label">
	                <xsl:attribute name="dir">
    	              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(label-question-typical-answer)"/>
        	        </xsl:attribute>                                                                                       
                   <xsl:value-of select="$label-question-typical-answer"/>
                  </td>
                  <td class="tab-description-value">
	                <xsl:attribute name="dir">
    	              <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@TYPICAL_ANSWER)"/>
        	        </xsl:attribute>                                                                                       
                  <xsl:value-of select="@TYPICAL_ANSWER"/>
                  </td>
                </xsl:if>
              </tr>
            </tbody>
          </table>
          <xsl:apply-templates select="ANSWERS">
            <xsl:with-param name="context-question" select="@QUESTION_ID"/>
          </xsl:apply-templates>
      </div>
    </div>
  </xsl:template>
  
  <xsl:template match="ANSWERS">
    <xsl:param name="context-question"/>
    <div class="tab-answers-header">
	  <xsl:attribute name="dir">
    	<xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-title-question-answers)"/>
      </xsl:attribute>                                                                                                 	    
      <xsl:value-of select="$table-title-question-answers"/>
    </div>
    <table class="tab-answers" width="100%">
      <thead>
        <tr>
          <xsl:if test="../../@EDITABLE='true'">
          	<th><xsl:value-of select="$table-column-question-select"/>
	          <xsl:attribute name="dir">
    	        <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-column-question-select)"/>
        	  </xsl:attribute>                                                                                                 	
          	</th>
          </xsl:if>
          <th>
	        <xsl:attribute name="dir">
    	      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-column-question-source-name)"/>
        	</xsl:attribute>                                                                                                 	          
            <xsl:value-of select="$table-column-question-source-name"/>
          </th>
          <th>
	        <xsl:attribute name="dir">
    	      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-column-question-source-desc)"/>
        	</xsl:attribute>                                                                                                 	                    
            <xsl:value-of select="$table-column-question-source-desc"/>
          </th>
          <th>
	        <xsl:attribute name="dir">
    	      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(table-column-question-value)"/>
        	</xsl:attribute>                                                                                                 	                                
            <xsl:value-of select="$table-column-question-value"/>
          </th>
        </tr>
      </thead>
      <tbody>
        <xsl:for-each select="ANSWER">
          <xsl:variable name="header-opt" select="concat(@VALUE,' (',@NAME,',',@DESCRIPTION,')')"/>
           <tr>
             <xsl:if test="../../../@EDITABLE='true'">
               <td>
                 <input type="radio"
                          name="{$el-prefix}{$context-question}"
                          value="{@ANSWER_ID}"
                          onclick="curam.util.updateHeader('{$context-question}', ' {$header-opt}', '{@VALUE}', '{@NAME}');">                   			         	            	                                                               
                   <xsl:if test="@ANSWER_ID=../../@CHOSEN_ANSWER_ID">
                      <xsl:attribute name="checked">checked</xsl:attribute>
                    </xsl:if>
                  </input>
               </td>
             </xsl:if>
             <td>
		        <xsl:attribute name="dir">
    		      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@NAME)"/>
        		</xsl:attribute>                                                                                                 	                                             
             <xsl:value-of select="@NAME"/>
             </td>
             <td>
		        <xsl:attribute name="dir">
    		      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@DESCRIPTION)"/>
        		</xsl:attribute>                                                                                                 	                                             
             <xsl:value-of select="@DESCRIPTION"/>
             </td>
             <td>
		        <xsl:attribute name="dir">
    		      <xsl:value-of select="bidi-utils:getResolvedBaseTextDirection(@VALUE)"/>
        		</xsl:attribute>                                                                                                 	                                             
             <xsl:value-of select="@VALUE"/>
             </td>
           </tr>
         </xsl:for-each>
       </tbody>
     </table>
  </xsl:template>

</xsl:stylesheet>
