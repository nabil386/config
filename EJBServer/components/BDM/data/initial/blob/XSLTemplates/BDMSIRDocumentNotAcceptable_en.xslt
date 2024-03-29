<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" version="1.0">
	<xsl:attribute-set name="Normal_1" foa:class="block">
		<xsl:attribute name="font-size">11.0pt</xsl:attribute>
		<xsl:attribute name="line-height">20mm</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>
  	<xsl:attribute-set name="Normal_2" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<!--xsl:attribute name="text-indent">100mm</xsl:attribute-->
	<xsl:attribute name="text-align">right</xsl:attribute>
	<xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
	</xsl:attribute-set>	  
	<xsl:attribute-set name="Normal_3" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<!--xsl:attribute name="text-indent">100mm</xsl:attribute-->
	<xsl:attribute name="text-align">right</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    </xsl:attribute-set>
    <!-- TASK 26501 -->
    <xsl:attribute-set name="Normal_3_bold" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
     <xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
     </xsl:attribute-set>
     <!-- TASK 26501 -->
    <xsl:attribute-set name="Normal_4" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">10mm</xsl:attribute>
	<xsl:attribute name="text-indent">100mm</xsl:attribute>
	<xsl:attribute name="text-align">right</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_5" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
    <xsl:attribute name="line-height">5mm</xsl:attribute>
  </xsl:attribute-set>
    <xsl:attribute-set name="Normal_6" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">5mm</xsl:attribute>
    <xsl:attribute name="white-space-collapse">false</xsl:attribute>
    <xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
    <xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="Normal_7" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="space-before.optimum">10mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_8" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">6mm</xsl:attribute>
	<xsl:attribute name="space-before.optimum">6mm</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="Normal_9" foa:class="block">
    <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	<xsl:attribute name="line-height">6mm</xsl:attribute>
	<xsl:attribute name="space-before.optimum">8mm</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="highlight" foa:class="block">
		<xsl:attribute name="background-color">#FFFF00</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="optional_text" foa:class="block">
      <xsl:attribute name="font-size">11.0pt</xsl:attribute>
	  <xsl:attribute name="space-before.optimum">10mm</xsl:attribute>
	  <xsl:attribute name="start-indent">10mm</xsl:attribute>
	  <xsl:attribute name="line-height">6mm</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="header_image" foa:class="block">
      <xsl:attribute name="text-align">left</xsl:attribute>
	  <xsl:attribute name="space-after.optimum">20mm</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="footer_text_small" foa:class="block">
      <xsl:attribute name="font-size">8.0pt</xsl:attribute>
	  <xsl:attribute name="text-align">left</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="footer_page_num" foa:class="block">
      <xsl:attribute name="font-size">8.0pt</xsl:attribute>
	  <xsl:attribute name="text-align">center</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="footer_text" foa:class="block">
      <xsl:attribute name="font-size">8.0pt</xsl:attribute>
	  <xsl:attribute name="text-align">left</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="footer_image" foa:class="block">
      <xsl:attribute name="text-align">right</xsl:attribute>
    </xsl:attribute-set>
	<xsl:attribute-set name="bold" foa:class="block">
		<!--xsl:attribute name="background-color">#FFFF00</xsl:attribute-->
    </xsl:attribute-set>
	<xsl:attribute-set name="hyperlink" foa:class="block">
		<xsl:attribute name="font-weight">bolder</xsl:attribute>
		<xsl:attribute name="text-decoration">underline</xsl:attribute>
    </xsl:attribute-set>
		<!-- END, CR00352142 -->
	<xsl:template match="DOCUMENT">
		<!--Explicitly select DATA to ensure META element is ignored.-->
		<xsl:apply-templates select="DATA" />
	</xsl:template>
	<xsl:template match="DATA">
		<!--Explicitly select the STRUCT to avoid processing anything else.-->
		<!-- TASK 14146 match BDMProFormaDocumentData -->
		<xsl:apply-templates select="STRUCT[SNAME='BDMProFormaDocumentData']" />
	</xsl:template>
	<xsl:template match="STRUCT">
	<fo:root>
	  <fo:layout-master-set>
		<fo:simple-page-master master-name="only" page-height="297mm" page-width="210mm" margin-top="10mm" margin-bottom="10mm" margin-left="20mm" margin-right="20mm">
          <fo:region-body region-name="xsl-region-body" margin-top="20mm" margin-bottom="20mm" />
		  <fo:region-before region-name="xsl-region-before" extent="20mm" margin-left="20mm" margin-right="20mm" />
          <fo:region-after region-name="xsl-region-after" extent="20mm" margin-left="20mm" margin-right="20mm" />
		</fo:simple-page-master>
      </fo:layout-master-set>
    <fo:page-sequence master-reference="only" id="page-sequence">
	    <fo:static-content flow-name="xsl-region-before">
          <fo:block xsl:use-attribute-sets="header_image">
			<fo:external-graphic src="./img/header_logo.png"  content-height="scale-to-fit" scaling="non-uniform"/>
		  </fo:block>
        </fo:static-content>
        <fo:static-content flow-name="xsl-region-after">
		  <fo:block>
            <fo:block xsl:use-attribute-sets="footer_text_small"><fo:inline font-weight="bold">
			  Service Canada delivers Employment and Social Development Canada programs and services for the Government of Canada.</fo:inline>
			</fo:block>
			<fo:table border="0"><fo:table-body><fo:table-row>
			  <fo:table-cell>
				<fo:block xsl:use-attribute-sets="footer_text">
				  <fo:inline >
				    <xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='alternateID']" />
				    <xsl:if test="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='caseReference' and VALUE!='']" >
					  -<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='caseReference']" />
				    </xsl:if>
				  </fo:inline>
				</fo:block>
				<fo:block xsl:use-attribute-sets="footer_text">
				  <fo:inline xsl:use-attribute-sets="footer_text">S010001-E</fo:inline>
				</fo:block>
			  </fo:table-cell>
			  <fo:table-cell display-align="after">
				<fo:block xsl:use-attribute-sets="footer_page_num">
				Page <fo:page-number /> of <fo:page-number-citation-last ref-id="page-sequence"/>
				</fo:block>
			  </fo:table-cell>
			  <fo:table-cell display-align="after">
				<fo:block xsl:use-attribute-sets="footer_image">
				  <fo:external-graphic src="./img/footer_logo.png"  content-height="scale-to-fit" scaling="non-uniform"/>
				</fo:block>
			  </fo:table-cell>
			</fo:table-row></fo:table-body></fo:table>
		  </fo:block>
        </fo:static-content>
        <fo:flow flow-name="xsl-region-body" font-family="Arial,sans-serif">
        <!--fo:block xsl:use-attribute-sets="Normal_1">DIRECT DEPOSIT LETTER (extracted from Q000000 letter template)</fo:block-->
		
		<fo:table border="0"><fo:table-body>
			<fo:table-row>
				<fo:table-cell>
				   <fo:block xsl:use-attribute-sets="Normal_5">
						<fo:inline xsl:use-attribute-sets="bold">
							<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='userFullName']"/>
						</fo:inline>
				   </fo:block>
				   <fo:block xsl:use-attribute-sets="Normal_6">
						<fo:inline xsl:use-attribute-sets="bold">
							<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='userAddress']" />
						</fo:inline>
				   </fo:block>
				</fo:table-cell>
				<fo:table-cell>
				<fo:block xsl:use-attribute-sets="Normal_3_bold">Protected B</fo:block>
					<fo:block xsl:use-attribute-sets="Normal_3">
					<xsl:apply-templates select="./FIELD[FNAME='processCtrPhoneDetails']/STRUCT[SNAME='BDMProcessingCenterPhoneNumbers']/FIELD[FNAME='phoneNumberEnquiries']" /> (Enquiries)
					<xsl:apply-templates select="./FIELD[FNAME='processCtrPhoneDetails']/STRUCT[SNAME='BDMProcessingCenterPhoneNumbers']/FIELD[FNAME='phoneNumberTTY']" /> (TTY)
					<xsl:apply-templates select="./FIELD[FNAME='processCtrPhoneDetails']/STRUCT[SNAME='BDMProcessingCenterPhoneNumbers']/FIELD[FNAME='phoneNumIntl']" /> (International only)
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body></fo:table>
	<fo:block xsl:use-attribute-sets="Normal_9"></fo:block>
		<fo:table border="0"><fo:table-body>
			<fo:table-row>
				<fo:table-cell>
				<fo:block xsl:use-attribute-sets="Normal_5">
					<fo:inline xsl:use-attribute-sets="bold">
						<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='concernRoleName']" />
					</fo:inline>
				</fo:block>
				<fo:block xsl:use-attribute-sets="Normal_6">
					<fo:inline xsl:use-attribute-sets="bold">
						<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='concernRoleAddress']" />
					</fo:inline>
				</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="Normal_3">
						<fo:inline xsl:use-attribute-sets="bold">
							<xsl:apply-templates select="FIELD[FNAME='currentDateString']" />
						</fo:inline>
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</fo:table-body></fo:table>
		<fo:block xsl:use-attribute-sets="Normal_7">
		Dear 
		<fo:inline xsl:use-attribute-sets="bold">
			<xsl:apply-templates select="./FIELD[FNAME='proFormaDocumentData']/STRUCT[SNAME='ProFormaDocumentData']/FIELD[FNAME='concernRoleName']" />
		</fo:inline>
    </fo:block>
   <fo:block xsl:use-attribute-sets="Normal_8">
     We have received your application for Sample benefits, but we need additional proof of identity. When you apply for Sample benefits, your personal information is matched to the information in your Social Insurance Number record. This information is required to validate your electronic signature and verify your identity. The personal information you provided in your application does not match the information in your Social Insurance Number record.
	 </fo:block>
	 <fo:block xsl:use-attribute-sets="Normal_8">
      The identity document submitted to support your identity are not acceptable. Please see below for a description of acceptable identity documents. Please upload acceptable identity documentation to your client account.
</fo:block>
<fo:block xsl:use-attribute-sets="Normal_8">
Original primary identity documents such as your Birth Certificate issued by a Provincial/Territorial Vital Statistics Office, Certificate of Canadian Citizenship, Permanent Resident Card, Work permit, or Student permit may be used as personal identification.
</fo:block>
<fo:block xsl:use-attribute-sets="Normal_8">
If the name on your primary document is different than the name on your SIN record, you will need to include a supporting document such as a Marriage Certificate, Divorce Decree or Change of Legal Name. 
</fo:block>
<fo:block xsl:use-attribute-sets="Normal_8">
For a complete listing of acceptable primary and supporting documents please visit our website at<fo:inline text-decoration="underline"> www.canada.ca/en/employment-social-development/services/sin.html.</fo:inline>
</fo:block>


		<fo:block xsl:use-attribute-sets="Normal_8">Yours sincerely,</fo:block>
		<fo:block xsl:use-attribute-sets="Normal_8">Service Canada</fo:block>
	</fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <xsl:template match="FIELD">
    <xsl:value-of select="VALUE" />
  </xsl:template>
</xsl:stylesheet>

