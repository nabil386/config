<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">
  
  <xsl:output method="xml" indent="yes"/>

  <xsl:attribute-set name="Doc_Title" fo:class="block">
    <xsl:attribute name="color">rgb(227,86,0)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">36.0pt</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>

  <xsl:attribute-set name="Section_Title" fo:class="block">
    <xsl:attribute name="color">rgb(227,86,0)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">14.0pt</xsl:attribute>    
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>

  <xsl:attribute-set name="Doc_Submitted" fo:class="block">
    <xsl:attribute name="color">rgb(50,50,50)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma,Verdana</xsl:attribute>
    <xsl:attribute name="font-style">italic</xsl:attribute>    
    <xsl:attribute name="font-size">14.0pt</xsl:attribute>    
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>

  <xsl:attribute-set name="Field_Label" fo:class="block">
    <xsl:attribute name="color">rgb(50,50,50)</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>    
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>
  
  <xsl:attribute-set name="Field_Hint" fo:class="block">
    <xsl:attribute name="color">rgb(50,50,50)</xsl:attribute>
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">10.0pt</xsl:attribute>    
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>
  
  <xsl:attribute-set name="Field_Content" fo:class="block">
    <xsl:attribute name="color">rgb(26,26,26)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>    
    <xsl:attribute name="text-align">left</xsl:attribute>    
  </xsl:attribute-set>
  
  <xsl:attribute-set name="Next_Steps_Heading" fo:class="block">
    <xsl:attribute name="color">rgb(50,50,50)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">14.0pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>    
    <xsl:attribute name="margin-top">2.0mm</xsl:attribute>    
    <xsl:attribute name="margin-left">2.0mm</xsl:attribute>        
    <xsl:attribute name="margin-right">2.0mm</xsl:attribute>        
  </xsl:attribute-set>

  <xsl:attribute-set name="Next_Steps_Content_Heading" fo:class="block">       
    <xsl:attribute name="color">rgb(50,50,50)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
    <xsl:attribute name="margin-left">2.0mm</xsl:attribute>        
    <xsl:attribute name="margin-right">2.0mm</xsl:attribute>        
  </xsl:attribute-set>

  <xsl:attribute-set name="Next_Steps_Content" fo:class="block">
    <xsl:attribute name="color">rgb(26,26,26)</xsl:attribute>    
    <xsl:attribute name="font-family">Tahoma</xsl:attribute>
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
    <xsl:attribute name="margin-left">2.0mm</xsl:attribute>        
    <xsl:attribute name="margin-right">2.0mm</xsl:attribute>        
  </xsl:attribute-set>

  <xsl:template match="/">
    <fo:root>
      <fo:layout-master-set>
        <fo:simple-page-master master-name="US-Letter" page-height="279.4mm" page-width="215.9mm" margin="12.7mm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="US-Letter">
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:table>
              <fo:table-column column-width="34.716mm"/>
              <fo:table-column column-width="34.716mm"/>
              <fo:table-column column-width="34.716mm"/>
              <fo:table-column column-width="34.716mm"/>
              <fo:table-column column-width="34.716mm"/>
              <fo:table-body>              
                <fo:table-row>
                   <fo:table-cell number-columns-spanned="5">
                      <fo:block padding-bottom="10mm" text-align="right">
                        <fo:external-graphic src="url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPUAAADICAMAAAAHi1ckAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYxIDY0LjE0MDk0OSwgMjAxMC8xMi8wNy0xMDo1NzowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNS4xIFdpbmRvd3MiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QzkwREQ1MDZEMDUwMTFFM0E3NkFDRjZGODM0RjFFMUIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QzkwREQ1MDdEMDUwMTFFM0E3NkFDRjZGODM0RjFFMUIiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpDOTBERDUwNEQwNTAxMUUzQTc2QUNGNkY4MzRGMUUxQiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDOTBERDUwNUQwNTAxMUUzQTc2QUNGNkY4MzRGMUUxQiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PjElD5UAAAMAUExURexrsvW2TyqM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')"/>
                      </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block xsl:use-attribute-sets="Doc_Title">Request for Reconsideration</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block padding-bottom="10mm" xsl:use-attribute-sets="Doc_Submitted">Submitted on <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Metadata']/attributes/attribute[@name='submissionDate']/@value"/></fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block padding-top="6mm" xsl:use-attribute-sets="Section_Title">Your Details</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="1mm" xsl:use-attribute-sets="Field_Content">Please review your information below:</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block text-align="left" font-size="11pt" font-family="Tahoma" color="rgb(26,26,26)">
                      <fo:table>
                        <fo:table-column column-width="34.716mm"/>
                        <fo:table-column column-width="34.716mm"/>
                        <fo:table-column column-width="34.716mm"/>
                        <fo:table-column column-width="34.716mm"/>
                        <fo:table-body>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="1">
                              <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">First Name</fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="1">
                                <fo:block/>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="1">
                              <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Last Name</fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="1">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='firstName']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="1">
                                <fo:block/>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="1">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='lastName']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                              <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Mailing Address</fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='mailingAddress']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                          </fo:table-row>                            
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                              <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Preferred Phone Number</fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='preferredPhoneNumber']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell padding-top="6mm" number-columns-spanned="2">
                              <fo:block xsl:use-attribute-sets="Field_Label">Preferred Email</fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='preferredEmail']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell padding-top="6mm" number-columns-spanned="2">
                              <fo:block xsl:use-attribute-sets="Field_Label">Language of Communication</fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                          <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                              <fo:block xsl:use-attribute-sets="Field_Content">
                                <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']/attributes/attribute[@name='languageCommunication']/@value"/>
                              </fo:block>
                            </fo:table-cell>
                          </fo:table-row>
                        </fo:table-body>
                      </fo:table>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block padding-top="8mm" xsl:use-attribute-sets="Section_Title">Confirmation</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="2mm" xsl:use-attribute-sets="Field_Content">Please confirm and proceed by clicking Next if the above information is correct. If not, please click on Delete <![CDATA[&]]> exit and return to the client account to update.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell padding-top="6mm" number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Content">Yes</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block page-break-before="always"/>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block padding-top="8mm" xsl:use-attribute-sets="Section_Title">Request Details</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="3mm" xsl:use-attribute-sets="Field_Label">Benefit</fo:block>
                  </fo:table-cell>                  
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Hint">Select at least one</fo:block>
                  </fo:table-cell>                  
                </fo:table-row>
                <xsl:for-each select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='ApplicationDetails']/entities/entity[@name='BenefitDetails']">
                    <xsl:variable name="isBenefitSelected" select="attributes/attribute[@name='benefitSelected']/@value"/>
                    <xsl:if test="$isBenefitSelected='true'">
                        <fo:table-row>
                          <fo:table-cell number-columns-spanned="1">
                            <fo:block/>
                          </fo:table-cell>
                          <fo:table-cell number-columns-spanned="4">
                            <fo:block xsl:use-attribute-sets="Field_Content">
                              <xsl:value-of select="attributes/attribute[@name='name']/@value"/> 
                            </fo:block>
                          </fo:table-cell>
                        </fo:table-row>
                    </xsl:if>
                </xsl:for-each>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Date of Decision</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Hint">Please provide the date when you became aware of the decision</fo:block>
                  </fo:table-cell>                  
                </fo:table-row>                
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell padding-top="1mm" number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Content">
                      <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='decisionDate']/@value"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>                
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="7mm" xsl:use-attribute-sets="Field_Label">Reason For Request for Reconsideration</fo:block>
                  </fo:table-cell>
                </fo:table-row>                
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Hint">Please explain the reason(s) you disagree with the decision and any relevant information that was not previously provided.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell padding-top="1mm" number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Content">
                      <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='reconsiderationReason']/@value"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <xsl:variable name="lateReconsiderationValue" select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='lateReconsiderationReason']/@value"/>
                <xsl:if test="$lateReconsiderationValue!=''"> 
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1">
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell number-columns-spanned="4">
                        <fo:block padding-top="7mm" xsl:use-attribute-sets="Field_Label">Late Request for Reconsideration</fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1">
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell number-columns-spanned="4">
                        <fo:block xsl:use-attribute-sets="Field_Hint">Provide your reason(s) for delay in submitting this request</fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1">
                        <fo:block/>
                      </fo:table-cell>
                      <fo:table-cell padding-top="1mm" number-columns-spanned="4">
                        <fo:block xsl:use-attribute-sets="Field_Content">
                          <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='lateReconsiderationReason']/@value"/>
                        </fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block padding-top="8mm" xsl:use-attribute-sets="Section_Title">Phone Number</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="3mm" xsl:use-attribute-sets="Field_Hint">Provide the telephone number where you can be contacted in the next 2 weeks</fo:block>
                  </fo:table-cell>                  
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Country Code</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Content">
                      <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='countryCode']/@value"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Phone Number</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <xsl:variable name="phoneNumberValue" select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='phoneNumber']/@value"/>
                  <xsl:if test="$phoneNumberValue!=''">
                    <fo:table-cell number-columns-spanned="4">
                      <fo:block xsl:use-attribute-sets="Field_Content">
                        <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='phoneAreaCode']/@value"/>
                        <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='phoneNumber']/@value"/>
                        <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='phoneExt']/@value"/>
                      </fo:block>
                    </fo:table-cell>
                    </xsl:if>
                    <xsl:if test="$phoneNumberValue=''">
                        <fo:table-cell number-columns-spanned="1">
                            <fo:block xsl:use-attribute-sets="Field_Hint">Data not provided</fo:block>
                    </fo:table-cell>
                    </xsl:if>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="6mm" xsl:use-attribute-sets="Field_Label">Type of Phone Number</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block xsl:use-attribute-sets="Field_Content">
                      <xsl:value-of select="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'RFRDetails']/attributes/attribute[@name='phoneType']/@value"/>
                    </fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block page-break-before="always"/>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block padding-top="8mm" xsl:use-attribute-sets="Section_Title">Attestation</fo:block>
                  </fo:table-cell>
                  <fo:table-cell number-columns-spanned="4">
                    <fo:block padding-top="10mm" xsl:use-attribute-sets="Field_Content">I hereby give notice that I disagree with a decision regarding my claim for benefits and wish to exercise my right to request a reconsideration of this decision. I declare that the information on this form is true and accurate.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="1">
                    <fo:block/>
                  </fo:table-cell>
                    <fo:table-cell number-columns-spanned="4">
                      <fo:block padding-top="2mm" xsl:use-attribute-sets="Field_Content">Yes</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block page-break-before="always" xsl:use-attribute-sets="Section_Title">Next Steps</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row >
                  <fo:table-cell number-columns-spanned="5" padding-top="5mm">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content_Heading">1. Review and informal resolution</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content">We may be able to resolve your appeal informally by reviewing all of your information and discussing it with you. If you’re satisfied with this informal resolution decision, we will implement the decision and close your appeal. In this case, you wouldn’t have a hearing.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row >
                  <fo:table-cell number-columns-spanned="5" padding-top="5mm">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content_Heading">2. Hearing</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row >
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content">If you disagree with the informal resolution decision, you can continue your appeal at a hearing. A hearing is a formal meeting involving you and a hearing officer where you can explain why you think we made a mistake with your eligibility determination. Your hearing may take place over the phone. You can participate in the hearing by yourself or have someone participate in your hearing with you.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row >
                  <fo:table-cell number-columns-spanned="5" padding-top="5mm">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content_Heading">3. Notice of decision</fo:block>
                  </fo:table-cell>
                </fo:table-row>
                <fo:table-row >
                  <fo:table-cell number-columns-spanned="5">
                    <fo:block xsl:use-attribute-sets="Next_Steps_Content">After the hearing, we’ll review all your information and make a final decision about your appeal, which will be mailed to you.</fo:block>
                  </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name = 'Person']">
    <fo:table-row>
      <fo:table-cell number-columns-spanned="1">
        <fo:block xsl:use-attribute-sets="Field_Content">
          <xsl:value-of select="attributes/attribute[@name='firstName']/@value"/><xsl:text> </xsl:text><xsl:value-of select="attributes/attribute[@name='lastName']/@value"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
</xsl:stylesheet>

