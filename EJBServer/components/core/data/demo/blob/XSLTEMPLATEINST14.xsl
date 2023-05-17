<?xml version='1.0' encoding="UTF-8" standalone='yes'?>
<!DOCTYPE xsl:stylesheet [<!ENTITY nbsp "&#160;">]>
<!-- BEGIN, CR00357205, PB -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  version='1.0' xmlns:foa="http://fabio">
  <!-- END, CR00357205 -->
<!-- BEGIN, CR00352142, PB -->
      <xsl:attribute-set name="Normal_1" foa:class="block">
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
  </xsl:attribute-set>
  
     <xsl:attribute-set name="Normal_2" foa:class="inline">
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <xsl:attribute name="font-size">10.0pt</xsl:attribute>
 </xsl:attribute-set>
  
       <xsl:attribute-set name="Normal_3" foa:class="inline">
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>

  </xsl:attribute-set>
   
       <xsl:attribute-set name="Normal_4" foa:class="inline">
    <xsl:attribute name="font-family">WT Sans</xsl:attribute>
    <xsl:attribute name="font-size">12.0pt</xsl:attribute>
<xsl:attribute name="font-weight">bold</xsl:attribute>
  </xsl:attribute-set>
  <!-- END, CR00352142 -->
  <xsl:template match="DOCUMENT">
    <!--Explicitly select DATA to ensure META element is ignored.-->
    <xsl:apply-templates select="DATA"/>
  </xsl:template>
  
  
  <xsl:template match="DATA">
    <!--Explicitly select the STRUCT to avoid processing anything else.-->
    <xsl:apply-templates select="STRUCT[SNAME='IncomeSupportDocumentData']"/>
  </xsl:template>
  
  
  <xsl:template match="STRUCT[SNAME='IncomeSupportDocumentData']">
    <xsl:apply-templates select="FIELD[FNAME='isDetails']"/>
  </xsl:template>
  
  
  <xsl:template match="FIELD[FNAME='isDetails']">
    <xsl:apply-templates select="STRUCT[SNAME='IncomeSupportDetails']"/>
  </xsl:template>

  
  
  <xsl:template match="STRUCT[SNAME='IncomeSupportDetails']">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="only"
                                       page-height="297mm"
                                       page-width="210mm"
                                       margin-top="30mm"
                                       margin-bottom="30mm"
                                       margin-left="30mm"
                                       margin-right="30mm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      
      <fo:page-sequence master-reference="only">
      
        <fo:flow flow-name="xsl-region-body">
        <!-- BEGIN, CR00352142, PB -->
          <fo:block xsl:use-attribute-sets="Normal_1" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="center" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_4">Request for Information to Verify Eligibility</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_3">Dear <xsl:apply-templates select="FIELD[FNAME='applicantTitle']"/>&nbsp;<xsl:apply-templates select="FIELD[FNAME='applicantSurname']"/>,</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_3">To get benefits you must provide us with the verification documents below for </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_4">you </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_3">and </fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_4">all persons for whom you are applying</fo:inline>
            <fo:inline xsl:use-attribute-sets="Normal_3">. If you have any questions or need help to get the proofs, please call me.  Thank you.</fo:inline>
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>
          <fo:block font-size="12pt">
            <fo:table>
              <fo:table-column column-width="50mm"/>
              <fo:table-column column-width="50mm"/>
              <fo:table-column column-width="50mm"/>
              <fo:table-body>
                <fo:table-row>
                  <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_4">INCOME</fo:inline>
                    </fo:block>
                    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      &nbsp;
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Pay Stub</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Receipts for Business Expenses</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Statement of Estimated Earnings</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Tax Returns</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Pension Check/Stub</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Most recent benefit check or copy </fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Affidavit from aliens sponsor</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Court Records</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Statement from person/agency providing the money or making payment for the client</fo:inline>
                    </fo:block>
                  </fo:table-cell>
                  <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_4">PROOF OF IDENTITY</fo:inline>
                    </fo:block>
                    <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      &nbsp;
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Drivers license</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Hospital or birth records</fo:inline>
                    </fo:block>
					<!-- BEGIN, CR00357029, PB -->
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Marriage License/Divorce Decree</fo:inline>
                    </fo:block>
					<!-- END, CR00357029 -->
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">US Passport</fo:inline>
                    </fo:block>
                    <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                      <fo:inline xsl:use-attribute-sets="Normal_2">Adoption papers or records</fo:inline>
                    </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Voter registration card</fo:inline>
                   </fo:block>
                 </fo:table-cell>
                 <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">MOST RECENT PROOF OF EXPENSES</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Written statement/bills from providers</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Medicare Card, Health Insurance Cards and current premiums</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Out-of-pocket medical bills</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Proof of disability</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Rent, Mortgage receipt</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Lease Agreement</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Gas, electric and telephone bills</fo:inline>
                   </fo:block>
                 </fo:table-cell>
               </fo:table-row>
               <fo:table-row>
                 <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">SCHOOL ATTENDENCE</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">School</fo:inline>
                     <fo:inline xsl:use-attribute-sets="Normal_2"> attendance registrar records</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Current Report Card</fo:inline>
                   </fo:block>
                 </fo:table-cell>
                 <fo:table-cell border-bottom-style="none" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="justify" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_4">ASSETS</fo:inline>
                   </fo:block>
                   <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="justify" text-indent="0.0pt">
                     &nbsp;
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Bank statements</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Checking account, savings account and credit union records</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Stocks and Bonds</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Statement from bank official, insurance agent</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Insurance/Tax Appraisals</fo:inline>
                   </fo:block>
                   <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                     <fo:inline xsl:use-attribute-sets="Normal_2">Insurance Policy</fo:inline>
                   </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Recent sales slips</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="none" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_4">EMERGENCY </fo:inline>
                    <fo:inline xsl:use-attribute-sets="Normal_4">ASSISTANCE</fo:inline>
                  </fo:block>
                  <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    &nbsp;
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Eviction statement from landlord</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Written notice of utility termination from provider</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Signed statement from lending institute</fo:inline>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
              <fo:table-row>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="solid" border-top-width="0.5pt" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_4">SSN</fo:inline>
                  </fo:block>
                  <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    &nbsp;
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Social Security Card</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Copy of SSN Application</fo:inline>
                  </fo:block>
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_2">Bendex or SDX printouts</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="none" padding-left="5.4pt" padding-right="5.4pt">
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
                  </fo:block>
                </fo:table-cell>
                <fo:table-cell border-bottom-style="solid" border-bottom-width="0.5pt" border-left-style="solid" border-left-width="0.5pt" border-right-style="solid" border-right-width="0.5pt" border-top-style="none" padding-left="5.4pt" padding-right="5.4pt">
				<!-- BEGIN, CR00357029, PB -->
                  <fo:block margin-left="0.0pt" space-after="5.4pt" space-before="5.4pt" text-align="start" text-indent="0.0pt">
                    <fo:inline xsl:use-attribute-sets="Normal_3">&nbsp;</fo:inline>
                  </fo:block>
				  <!-- ENd, CR00357029 -->
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
          </fo:block>
          
          <fo:block font-size="12.0pt" margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            &nbsp;
          </fo:block>  
          <fo:block margin-left="0.0pt" space-after="0.0pt" space-before="0.0pt" text-align="start" text-indent="0.0pt">
            <fo:inline xsl:use-attribute-sets="Normal_2">*IMPORTANT: These proofs must include the name, address, and telephone number of the person making the statement.</fo:inline>
          </fo:block>
        <!-- END, CR00352142 -->
        </fo:flow>
        
      </fo:page-sequence>
      
    </fo:root>

  </xsl:template>
  
  
  <xsl:template match="FIELD">
      <xsl:choose>
        <xsl:when test="TYPE='SVR_BOOLEAN'">
  
          <xsl:choose>
            <xsl:when test="VALUE='false'">
              No
            </xsl:when>
            <xsl:otherwise>
              Yes
            </xsl:otherwise>
          </xsl:choose>
  
        </xsl:when>
  
        <xsl:otherwise>
          <xsl:value-of select="VALUE"/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>


</xsl:stylesheet>
