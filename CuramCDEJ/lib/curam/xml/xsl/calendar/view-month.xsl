<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
    exclude-result-prefixes="tag request-utils"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:tag="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.CalendarTag"
    xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils">
  <xsl:import href="view-common.xsl"/>


  <!-- Calendar Tag calling this object -->
  <xsl:param name="CalendarTag" />

  
  <!-- The day to begin the current month on -->
  <xsl:param name="firstDay"/>
  <!-- The date to start the previous month on -->
  <xsl:param name="monthStart"/>
  <!-- The length of the current month -->
  <xsl:param name="monthLength"/>
  <!-- The current month as a string -->
  <xsl:param name="CurrentMonth"/>
  <!-- The current year as a string -->
  <xsl:param name="CurrentYear"/>
  <!-- The current day -->
  <xsl:param name="currentDay"/>

  <!-- More events icon location -->
  <xsl:param name="moreEventsIcon" />
  <!-- More events hover icon location -->
  <xsl:param name="moreEventsHoverIcon" />
  <!-- More events alt and title text -->
  <xsl:param name="moreEventsAltText" />

  <!-- Whether or not to display repeating event text -->
  <xsl:param name="showRepeatEventText" />
  <!-- Parameters for days of the week text -->
  <xsl:param name="Day1"/>
  <xsl:param name="Day2"/>
  <xsl:param name="Day3"/>
  <xsl:param name="Day4"/>
  <xsl:param name="Day5"/>
  <xsl:param name="Day6"/>
  <xsl:param name="Day7"/>
  
  <!-- Current day prompt for screen reader users -->
  <xsl:param name="todayPrompt"/>
  <!-- The Page Name variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="page-name" />
  <!-- The Hash Token Param variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="secure-urls-hash-token-param" />
  <!-- The is secure URLs solution enabled variable is supplied by the Transformer object in the calendar jsp  tag. -->
  <xsl:param name="is-secure-urls-solution-enabled" />
  <!-- The username variable is supplied by the Transformer object in the calendar jsp tag. -->
  <xsl:param name="username" />
  
  <!-- Attribute sets -->
  <xsl:attribute-set name="tbl" >
    <xsl:attribute name="width">100%</xsl:attribute>
    <xsl:attribute name="cellpadding">1</xsl:attribute>
    <xsl:attribute name="class">monthView</xsl:attribute>
    <xsl:attribute name="id">monthView</xsl:attribute>
    <xsl:attribute name="summary"><xsl:value-of select="$summary"/></xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="cell1" >
      <xsl:attribute name="valign">top</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="hyperlink">
    <xsl:attribute name="class">month-hyperlink</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="heading" >
      <xsl:attribute name="class">calendar-heading</xsl:attribute>
  </xsl:attribute-set>
  

  

  
  <!-- Main template -->
  <xsl:template match="/CURAM_CALENDAR_DATA">
    <div xsl:use-attribute-sets="dateBar">
      <xsl:value-of select="$DateBarText"/>
    </div>
  
    <table xsl:use-attribute-sets="tbl">
      <tbody>
        <tr>
          <th xsl:use-attribute-sets="heading">
            <xsl:attribute name="class">
              <xsl:text>calendar-heading</xsl:text>
              <xsl:text> first-calendar-header-field</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="$Day1"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:value-of select="$Day2"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:value-of select="$Day3"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:value-of select="$Day4"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:value-of select="$Day5"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:value-of select="$Day6"/>
          </th>
          <th xsl:use-attribute-sets="heading">
            <xsl:attribute name="class">
              <xsl:text>calendar-heading</xsl:text>
	      <xsl:text> last-calendar-header-field</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="$Day7"/>
          </th>
        </tr>
        <xsl:call-template name="weeks">
          <xsl:with-param name="counter" select="1"/>
        </xsl:call-template>
      </tbody>
    </table>
  </xsl:template>
  
  <!-- Template for localised text output -->
  <xsl:template name="localized-text">
    <xsl:param name="text" />
    <xsl:value-of select="$text" />
  </xsl:template>
  
  <!-- Template called for each week -->
  <xsl:template name="weeks" >
    <xsl:param name="counter"/>
    <xsl:if test="$counter &lt; 42">
      <tr>
        <xsl:call-template name="aWeek">
          <xsl:with-param name="dayNumber" select="$counter"/>
        </xsl:call-template>
      </tr>
      <xsl:call-template name="weeks">
        <xsl:with-param name="counter" select="$counter+7"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
  <!-- Template called for each cell -->
  <xsl:template name="aWeek">
    <xsl:param name="dayNumber"/>
    <xsl:param name="dayCount" select="1"/>
    
     <xsl:variable name="current-date" select="number(($dayNumber + 1) - $monthStart)"/>
    <xsl:if test="$dayCount &lt; 8">
      <td xsl:use-attribute-sets="cell1">
        <xsl:choose>
          <!-- This condition applies to days before current month-->
          <xsl:when test="$dayNumber &lt; $monthStart">
            <xsl:attribute name="class">
              <xsl:text>previous-month</xsl:text>
              <xsl:if test="$dayCount = 1">
	           <xsl:text> first-calendar-field</xsl:text>
	         </xsl:if>
	         <xsl:if test="$dayCount = 7">
	           <xsl:text> last-calendar-field</xsl:text>
	         </xsl:if>
            </xsl:attribute>
            <xsl:value-of select="$firstDay + ($dayNumber - 1)"/>
          </xsl:when>
          <!-- This condition applies to days during and after current month-->
          <xsl:when test="$dayNumber &gt;= $monthStart">
            <xsl:choose>
              <!-- after current month-->
              <xsl:when test="$dayNumber &gt;= $monthStart + $monthLength">
                <xsl:attribute name="class">
                  <xsl:text>next-month</xsl:text>
                    <xsl:if test="$dayCount = 1">
		      <xsl:text> first-calendar-field</xsl:text>
		    </xsl:if>
		    <xsl:if test="$dayCount = 7">
		      <xsl:text> last-calendar-field</xsl:text>
		    </xsl:if>
                </xsl:attribute>
                <xsl:value-of select="($dayNumber + 1) - ($monthStart + $monthLength)"/>
              </xsl:when>
              <!-- Current month-->
              <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$currentDay = $current-date">
                      <xsl:attribute name="class">
                        <xsl:text>current-month-selected</xsl:text>
                        <xsl:if test="$dayCount = 1">
			     <xsl:text> first-calendar-field</xsl:text>
			   </xsl:if>
			   <xsl:if test="$dayCount = 7">
			     <xsl:text> last-calendar-field</xsl:text>
			   </xsl:if>
                      </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                                        <xsl:attribute name="class">
                                        <xsl:text>current-month</xsl:text>
                                        <xsl:if test="$dayCount = 1">
                                          <xsl:text> first-calendar-field</xsl:text>
                                          </xsl:if>
                                          <xsl:if test="$dayCount = 7">
                                          <xsl:text> last-calendar-field</xsl:text>
                                          </xsl:if>
                                        
                                        </xsl:attribute>
                                </xsl:otherwise>
                </xsl:choose>
                <a>
                  <xsl:attribute name="href">
                    <xsl:choose>
                      <xsl:when test="$is-secure-urls-solution-enabled = 'true'">
                    
		                  <xsl:variable name="url-no-pagename">
		                    <xsl:call-template name="createLink">
		                      <xsl:with-param name="date" select="$current-date"/>
		                    </xsl:call-template>
		                  </xsl:variable>
		                    
		                  <xsl:variable name="url-no-hash">
		                    <xsl:value-of select="$page-name"/>
		                    <xsl:value-of select="$url-no-pagename"/>
		                  </xsl:variable>
		                
		                  <xsl:variable name="hash-token-value">
		                    <xsl:value-of select="request-utils:getHashOfURL($url-no-hash, $username)"/>
		                  </xsl:variable>
		                  
		                  <xsl:variable name="url-with-hash">
		                    <xsl:value-of select="$url-no-pagename"/>
		                    <xsl:value-of select="$secure-urls-hash-token-param"/>
		                    <xsl:value-of select="$hash-token-value"/>
		                  </xsl:variable>
		                
		                  <xsl:value-of select="$url-with-hash"/>
                      </xsl:when> 
                      <xsl:otherwise>
                        <xsl:call-template name="createLink">
		                  <xsl:with-param name="date" select="$current-date"/>
		                </xsl:call-template>
                      </xsl:otherwise> 
                    </xsl:choose> 
                  </xsl:attribute>
                 
                  <xsl:value-of select="$current-date"/>
                  <xsl:if test="$currentDay = $current-date">
                   <span class="hidden">
                      <xsl:text> - </xsl:text>
                      <xsl:value-of select="$todayPrompt"/>
                    </span>
                  </xsl:if>
                </a>
                <xsl:for-each select="SINGLE_DAY_EVENT[
                                              number(substring-before(DATE, '-')) = number($CurrentYear)
                                              and number(substring(DATE, 6, 2)) = number($CurrentMonth)
                                              and number(substring(DATE, 9, 2)) = number(($dayNumber + 1) - $monthStart)]
                                            | EVENT[
                                              number(substring-before(DATE, '-')) = number($CurrentYear)
                                              and number(substring(DATE, 6, 2)) = number($CurrentMonth)
                                              and number(substring(DATE, 9, 2)) = number(($dayNumber + 1) - $monthStart)]">
                  <xsl:call-template name="event-text">
                    <xsl:with-param name="day" select="$current-date"/>
                  </xsl:call-template>
                </xsl:for-each>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
        </xsl:choose>
      </td>
      <xsl:call-template name="aWeek">
        <xsl:with-param name="dayCount" select="$dayCount+1"/>
        <xsl:with-param name="dayNumber" select="$dayNumber+1"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="event-text">
  <xsl:param name="day"/>
    
   <xsl:variable name="event-description">
     <xsl:if test="ALL_DAY = 'false'">
     <xsl:apply-templates select="STARTTIME"/>
     <xsl:text> - </xsl:text>
    </xsl:if>
    <xsl:value-of select="DESCRIPTION"/>
   </xsl:variable>
   
  <a xsl:use-attribute-sets="hyperlink">
    <xsl:call-template name="add-href"/>
    <xsl:call-template name="add-onclick"/>
    <xsl:attribute name="title">
      <xsl:value-of select="$event-description"/>
    </xsl:attribute>
       <xsl:choose>
         <xsl:when test="@SPANNING = 'true'">
           <xsl:choose>
             <xsl:when test="POSITION = 'first'">
               <span class="first-span-event-text">
                 <xsl:value-of select="$event-description" />
               </span>
             </xsl:when>
             <xsl:when test="POSITION = 'last'">
               <xsl:choose>
                <xsl:when test="$showRepeatEventText = 'true'">
                 <span class="last-span-event-text">
                   <xsl:value-of select="$event-description" />
                 </span>
                </xsl:when>
                <xsl:otherwise>
                 <span class="last-span-event-text">
                   <xsl:value-of select="$event-description" /> 
                 </span>
                </xsl:otherwise>
              </xsl:choose>
             </xsl:when>
             <xsl:otherwise>
              <xsl:choose>
                <xsl:when test="$showRepeatEventText = 'true'">
                  <span class="span-event-text">
                   <xsl:value-of select="$event-description" />
                  </span>
                </xsl:when>
                <xsl:otherwise>
                  <span class="span-event-text">
                   <xsl:value-of select="$event-description" />
                  </span>
                </xsl:otherwise>
              </xsl:choose>
             </xsl:otherwise>
           </xsl:choose>
         </xsl:when>
         <xsl:otherwise>
           <span class="event-text">
            <xsl:value-of select="$event-description" />
           </span>
         </xsl:otherwise>
        </xsl:choose>
    </a>
     <xsl:if test="@LAST_ROW = 'true'">
       <div class="more-events">
         <a onfocus="this.childNodes[0].src = '{concat($static-content-server-url, '/', $moreEventsHoverIcon)}';"
         	onblur="this.childNodes[0].src = '{concat($static-content-server-url, '/', $moreEventsIcon)}';">
           <xsl:attribute name="href">
             <xsl:choose>
               <xsl:when test="$is-secure-urls-solution-enabled = 'true'">
	             <xsl:variable name="url-no-pagename">
	               <xsl:call-template name="createLink">
	                 <xsl:with-param name="date" select="$day"/>
	               </xsl:call-template>
	             </xsl:variable>
	               
	             <xsl:variable name="url-no-hash">
	               <xsl:value-of select="$page-name"/>
	               <xsl:value-of select="$url-no-pagename"/>
	             </xsl:variable>
	           
	             <xsl:variable name="hash-token-value">
	               <xsl:value-of select="request-utils:getHashOfURL($url-no-hash, $username)"/>
	             </xsl:variable>
	             
	             <xsl:variable name="url-with-hash">
	               <xsl:value-of select="$url-no-pagename"/>
	               <xsl:value-of select="$secure-urls-hash-token-param"/>
	               <xsl:value-of select="$hash-token-value"/>
	             </xsl:variable>
	           
	             <xsl:value-of select="$url-with-hash"/>
	           </xsl:when> 
               <xsl:otherwise>
                 <xsl:call-template name="createLink">
	               <xsl:with-param name="date" select="$day"/>
	             </xsl:call-template>
               </xsl:otherwise> 
             </xsl:choose> 
           </xsl:attribute>
         <img src="{concat($static-content-server-url, '/', $moreEventsIcon)}" 
         	onmouseover="this.src = '{concat($static-content-server-url, '/', $moreEventsHoverIcon)}';"
         	onmouseout="this.src = '{concat($static-content-server-url, '/', $moreEventsIcon)}';"
            alt="{$moreEventsAltText}"
            title="{$moreEventsAltText}"/>
          </a>
        </div>
     </xsl:if>
  </xsl:template>
  

  


  
   <!-- This template generates a hyperlink for creating a new event.-->
  <xsl:template name="createLink">
    <!-- date of this event -->
    <xsl:param name="date"/>
    <xsl:text>?startDate=</xsl:text>
    <xsl:value-of select="tag:getDate($CalendarTag, 
                          string($date), 
                          string($CurrentMonth),
                          string($CurrentYear))"/>
    <xsl:value-of select="$o3Parameters"/>
  </xsl:template>
  

</xsl:stylesheet>
