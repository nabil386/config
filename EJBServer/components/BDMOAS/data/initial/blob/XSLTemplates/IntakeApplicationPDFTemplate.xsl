<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">
    <xsl:variable name="appType" select="/DOCUMENT/DATA//entity[@name='IntakeApplicationType']/attributes/attribute[@name='intakeApplicationTypeID']/@value"/>
    <xsl:variable name="fo:layout-master-set">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="default-page" page-height="11in" page-width="8.5in" margin-left="0.6in" margin-right="0.6in">
                <fo:region-body margin-bottom="0.79in" margin-top="1.8in"/>
                <fo:region-before extent="1.5in" display-align="after"/>
            </fo:simple-page-master>
        </fo:layout-master-set>
    </xsl:variable>
    <xsl:variable name="fo:declarations">
        <fo:declarations>
            <x:xmpmeta xmlns:x="adobe:ns:meta/">
                <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
                    <rdf:Description rdf:about="" xmlns:dc="http://purl.org/dc/elements/1.1/">
                        <dc:title>
                            <rdf:Alt>
                                <rdf:li xml:lang="x-default">
                                    <xsl:value-of select="//entity[@name='Application']/@description"/>                                    
                                </rdf:li>
                            </rdf:Alt>
                        </dc:title>
                    </rdf:Description>
                </rdf:RDF>
            </x:xmpmeta>
            <pdf:catalog xmlns:pdf="http://xmlgraphics.apache.org/fop/extensions/pdf">
                <pdf:dictionary type="normal" key="ViewerPreferences">
                    <pdf:boolean key="DisplayDocTitle">true</pdf:boolean>
                </pdf:dictionary>
            </pdf:catalog>
        </fo:declarations>
    </xsl:variable>
    <xsl:variable name="fo:bookmark-tree">
        <fo:bookmark-tree>
            <xsl:variable name="applSource" select="//entity[@name='Application']/attributes/attribute[@name='applicationChannel']/@value"/>
            <xsl:if test="$applSource='UA'">
                <!-- bookmark for Getting Started Section-->
                <fo:bookmark internal-destination="{8000000010}">
                    <fo:bookmark-title>
                        <xsl:value-of select="'Overview'"/>
                    </fo:bookmark-title>
                </fo:bookmark>
            </xsl:if>
            <xsl:if test="$applSource='CURAM'">
                <!-- bookmark for Getting Started Section-->
                <fo:bookmark internal-destination="{8000000009}">
                    <fo:bookmark-title>
                        <xsl:value-of select="'Getting Started'"/>
                    </fo:bookmark-title>
                </fo:bookmark>
            </xsl:if>
            <xsl:for-each select="/DOCUMENT/DATA/summary-page//cluster | /DOCUMENT/DATA/summary-page//list | /DOCUMENT/DATA/summary-page//relationship-summary-list">
                <xsl:variable name="internal-id" select="@internal-id"/>
                <xsl:variable name="grouping-id" select="@grouping-id"/>
                <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])"/>
                <xsl:variable name="isClusterGrouped" select="boolean(@grouping-id)"/>
                <xsl:variable name="isClusterGroupLead" select="contains(ancestor::*/cluster[@grouping-id=$grouping-id][1]/@internal-id, current()/@internal-id)"/>
                <xsl:if test="not($isHiddenElement) and (not($isClusterGrouped) or $isClusterGroupLead) and not(not(./title))">
                    <fo:bookmark internal-destination="{$internal-id}">
                        <fo:bookmark-title>
                            <xsl:value-of select="./title[text()]"/>
                        </fo:bookmark-title>
                    </fo:bookmark>
                </xsl:if>
            </xsl:for-each>
            <!-- bookmark for attestation page -->
            <fo:bookmark internal-destination="{8000000001}">
                <fo:bookmark-title>
                    <xsl:value-of select="'Attestation'"/>
                </fo:bookmark-title>
            </fo:bookmark>
        </fo:bookmark-tree>
    </xsl:variable>
    <!-- global parameter and variable used when indenting. -->
    <xsl:param name="level-min" select="1"/>
    <xsl:variable name="workingPageWidth" select="7"/>
    <xsl:template match="DOCUMENT">
        <!--Explicitly select DATA to ensure META element is ignored.-->
        <xsl:apply-templates select="DATA"/>
    </xsl:template>
    <xsl:template match="DATA">
        <!--Explicitly select the entity to avoid processing anything else.-->
        <fo:root>
            <xsl:copy-of select="$fo:layout-master-set"/>
            <!-- PDF declaration and bookmark -->
            <xsl:copy-of select="$fo:declarations"/>
            <xsl:copy-of select="$fo:bookmark-tree"/>
            <fo:page-sequence master-reference="default-page" initial-page-number="1" format="1">
                <fo:title>
                    <xsl:value-of select="//entity[@name='Application']/@description"/>
                </fo:title>
                <!-- Header for customization -->
                <fo:static-content flow-name="xsl-region-before" font-family="IBMPlexSans" font-size="8pt">
                    <xsl:call-template name="header"/>
                    <fo:block>
                        <fo:leader leader-pattern="space"/>
                    </fo:block>
                    <fo:block padding-left="6pt" padding-right="6pt" background-color="{$bg-color-main}" font-size="1pt">
                        <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
                    </fo:block>
                </fo:static-content>                
                <!-- Main content area -->
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:block font-family="IBMPlexSans" space-before.optimum="1pt" space-after.optimum="2pt">
                            <!-- Start content -->
                            <fo:block font-size="18px" line-height="37px" space-before="20px" space-after="10px" role="{$header-role-page}">
                                <!-- xsl:value-of select="//entity[@name='Application']/@description"/ -->
                            
                            <xsl:if test="$appType='910,003'">
                            	<xsl:text>Application for Guaranteed Income Supplement</xsl:text>
                           	</xsl:if>
                            <xsl:if test="$appType='910,002'">
                            	<xsl:text>Application for Old Age Security Pension and Guaranteed Income Supplement</xsl:text>
                           	</xsl:if>
                            </fo:block>
                            <!--xsl:apply-templates select="summary-page//cluster | summary-page//list | summary-page//relationship-summary-list" / -->
						  <xsl:call-template name="applicationScriptPages"/> 
						    <xsl:variable name="userType" select="//entity[@name='Application']/attributes/attribute[@name='userType']/@value"/>
                            <fo:block break-after="page"/>
                            <!-- <xsl:if test="$userType='EXTERNAL'">
            					<xsl:call-template name="clientAttestationPage"/>
        					</xsl:if>
        					<xsl:if test="$userType!='EXTERNAL'">
            					<xsl:call-template name="attestationPage"/>
        					</xsl:if> --> 
                        </fo:block>
                    </fo:block>
                    <!-- end of document marker for total pages -->
                    <fo:block id="terminator"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

   <xsl:template name="applicationScriptPages">
    <xsl:variable name="appType" select="/DOCUMENT/DATA//entity[@name='IntakeApplicationType']/attributes/attribute[@name='intakeApplicationTypeID']/@value"/>
    <xsl:variable name="applicationChannel" select="//entity[@name='Application']/attributes/attribute[@name='applicationChannel']/@value"/>
 	<xsl:variable name="userType" select="//entity[@name='Application']/attributes/attribute[@name='userType']/@value"/>

	<!-- BEGIN: Template for Agent Portal OAS GIS Combined Application script pages  -->
 	<xsl:if test="$appType='910,002'">       
        <xsl:call-template name="OASGISApplicationAPScriptPages"/>      
	</xsl:if>
	<!-- END: Template for Agent Portal OAS GIS Combined Application script pages  -->
	
	<!-- BEGIN: Template for Agent Portal GIS Application script pages -->
 	<xsl:if test="$appType='910,003'">      
        <xsl:call-template name="GISApplicationAPScriptPages"/>      
	</xsl:if>
	<!-- END: Template for Agent Portal GIS Application script pages -->
	
	<!-- BEGIN: Template for UA Client Portal GIS Application script pages -->
 	<xsl:if test="$appType='910,004'">       
        <xsl:call-template name="GISApplicationCPScriptPages"/>      
	</xsl:if> 
	<!-- END : Template for UA Client Portal GIS Application script pages -->             
</xsl:template>   

<!-- BEGIN: Template for Agent Portal OAS GIS Combined Application script pages  -->
<xsl:template name="OASGISApplicationAPScriptPages">
	<xsl:for-each select="//entities/entity[@name='Person']">
		<xsl:variable name="isPrimaryParticipant"
			select="attributes/attribute[@name='isPrimaryParticipant']/@value" />

		<xsl:if test="$isPrimaryParticipant='true'">
			<xsl:call-template name="personalInformation"/>                             
		    <xsl:call-template name="addressInformation"/> 
		    <xsl:call-template name="communicationPreferences"/>
		    <xsl:call-template name="contactInformation"/>
		    <xsl:call-template name="OASPension"/>
		    <xsl:call-template name="LegalStatus"/> 
		    <xsl:call-template name="ResidenceHistory"/>     
		    <xsl:call-template name="AbsenceDetails"/>
		    <xsl:call-template name="GIS"/>	
		    <xsl:call-template name="RetirementIncome"/> 
		    <xsl:call-template name="ForeignIncome"/>                                       
		    <xsl:call-template name="MaritalStatusInfo"/>
		    <xsl:call-template name="paymentInformation"/>
			<xsl:call-template name="taxInformation"/> 
		    <xsl:call-template name="Declaration"/>  
		</xsl:if>
	</xsl:for-each> 			         
</xsl:template> 
<!-- END: Template for Agent Portal OAS GIS Combined Application script pages  -->

<!-- BEGIN: Template for Agent Portal GIS Application script pages -->
<xsl:template name="GISApplicationAPScriptPages">
	<xsl:for-each select="//entities/entity[@name='Person']">
		<xsl:variable name="isPrimaryParticipant"
			select="attributes/attribute[@name='isPrimaryParticipant']/@value" />

		<xsl:if test="$isPrimaryParticipant='true'">
		    <xsl:call-template name="personalInformation"/>                             
		    <xsl:call-template name="addressInformation"/> 
		    <xsl:call-template name="communicationPreferences"/>
		    <xsl:call-template name="contactInformation"/>            
		    <xsl:call-template name="GISLegalStatus"/>   
		    <xsl:call-template name="GISResidencePeriod"/>           
		    <xsl:call-template name="RetirementIncome"/> 
		    <xsl:call-template name="ForeignIncome"/>         
		    <xsl:call-template name="GISManualIncome"/>                              
		    <xsl:call-template name="MaritalStatusInfo"/>		
		    <xsl:call-template name="Declaration"/>   
		</xsl:if>	        
	</xsl:for-each>    
</xsl:template> 
<!-- END: Template for Agent Portal GIS Application script pages -->

<!-- BEGIN: Template for UA Client Portal GIS Application script pages -->
<xsl:template name="GISApplicationCPScriptPages">
	<xsl:for-each select="//entities/entity[@name='Person']">
		<xsl:variable name="isPrimaryParticipant"
			select="attributes/attribute[@name='isPrimaryParticipant']/@value" />

		<xsl:if test="$isPrimaryParticipant='true'">
			<xsl:call-template name="personalInformationUA" />
			<xsl:call-template name="addressInformationUA" />
			<xsl:call-template name="contactInformationUA" />
			<xsl:call-template name="residenceHistoryUA" />			
			<xsl:call-template name="retirementIncomeUA" />
			<xsl:call-template name="foreignIncomeUA"/> 
			<xsl:call-template name="maritalStatusInfoUA" />						
		</xsl:if>				
	</xsl:for-each>

	<xsl:for-each select="//entities/entity[@name='Person']">
		<xsl:variable name="isPrimaryParticipant"
			select="attributes/attribute[@name='isPrimaryParticipant']/@value" />	
		<xsl:if test="$isPrimaryParticipant='false'">
			<xsl:call-template name="partnerInformationUA" />
			<xsl:call-template name="addressInformationPartnerUA" />			
			<xsl:call-template name="residenceHistoryPartnerUA" />			
			<xsl:call-template name="retirementIncomePartnerUA" />
			<xsl:call-template name="foreignIncomePartnerUA"/> 						
		</xsl:if> 			
	</xsl:for-each>

	<xsl:for-each select="//entities/entity[@name='Person']">
		<xsl:variable name="isPrimaryParticipant"
			select="attributes/attribute[@name='isPrimaryParticipant']/@value" />		
		<xsl:if test="$isPrimaryParticipant='true'">
			<xsl:call-template name="sponsorshipInfoUA" />						
		</xsl:if>				
	</xsl:for-each>
</xsl:template> 
<!-- END: Template for UA Client Portal GIS Application script pages --> 
 
    <xsl:template name="overview">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="'8000000010'"/>
            <xsl:with-param name="title" select="'Overview'"/>
        </xsl:call-template>
        <fo:block font-size="8pt" font-weight="bold">
            <fo:block>
                <xsl:text>You are applying for the:</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>Sample benefit program</xsl:text>
            </fo:block>
            
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>Please be sure to understand the following steps before continuing: </xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="6pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>1</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>Prepare</fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block>
                <xsl:text>When you apply, you'll need to provide the following information:</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="15pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Your Social Insurance Number (SIN)</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Your banking Information</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Your total net income for the previous tax year</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Information about your dependent that are under the age of 18, including their SIN</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="6pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>2</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Apply</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>            
            <fo:block>
                <xsl:text>Application time: To complete the application will take approximately 15 minutes online</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>If you have an account, you have 30 days to access the application through your client account and submit the application for sample benefit. Once you complete the application a notification will be sent to your account.</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>If you do not have a client account then you can complete the application using the 'Start application' button. Once you have completed and submitted the application you will be presented with a screen which will provide your confirmation number.</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="6pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>3</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Proof</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block>
                <xsl:text>You may need to provide us the following documents as proof:</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="15pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Identification</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Proof of Address</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>&#x02022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Notice of Assessment</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:list-block provisional-distance-between-starts="6pt">
                <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                        <fo:block>4</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                        <fo:block>
                            <fo:block>Decision</fo:block>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </fo:list-block>
            <fo:block>
                <xsl:text>If you have a Client Account then a notification will be issued to your account when a decision is made. You can view the decision by reviewing the details in your account and a digital copy of a decision letter will be mailed to you.</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>If you do not wish to take advantage of a Client Account, then please call 1-800-123-4567 and speak with a Representative to obtain the decision on your application. A decision letter will be mailed to you.</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>
            <fo:block>
                <xsl:text>We will complete a decision on your application with in 28-days.</xsl:text>
            </fo:block>
            <fo:block>
                <fo:leader leader-pattern="space"/>
            </fo:block>           
        </fo:block>
    </xsl:template>

    
	<!-- BEGIN: FEATURE 94380  -->
	<xsl:template name="personalInformationUA">
	<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='Person']/@internal-id"/>
            <xsl:with-param name="title" select="'Personal Information'"/>
        </xsl:call-template>
         -->
                  
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Personal Information'"/>
        </xsl:call-template>                
       
        <fo:block>
        	<xsl:text>Applicant Details</xsl:text>
        </fo:block>
        
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         
        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">    
            <fo:table-body>
                <fo:table-row>
					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Social Insurance Number'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='sinRaw']/@value"/>
                    </xsl:call-template>                
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Title'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='personTitle']/@value"/>
                    </xsl:call-template>                
                 </fo:table-row>   
                 <fo:table-row>  
                      <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'First name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='firstName']/@value"/>
                    </xsl:call-template> 
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Middle Name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='middleInitial']/@value"/>
                    </xsl:call-template>                     
                 </fo:table-row> 
                 <fo:table-row>                                     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='lastName']/@value"/>
                    </xsl:call-template>  
 					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name at birth (if different from Last name)'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='LastNameAtBirth']/@value"/>
                    </xsl:call-template>                                      
                </fo:table-row>
                <fo:table-row>          
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of birth'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='dateOfBirth']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Gender'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='genderDropdown']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>          
            </fo:table-body>
        </fo:table>
    </xsl:template>    

	<xsl:template name="partnerInformationUA">
	<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='Person']/@internal-id"/>
            <xsl:with-param name="title" select="'Personal Information'"/>
        </xsl:call-template>
         -->
                  
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Spouse/Common-Law Partner'"/>
        </xsl:call-template>                
       
        <fo:block>
        	<xsl:text>Spouse/Common-Law Partner Details</xsl:text>
        </fo:block>
        
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         
        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">    
            <fo:table-body>
                <fo:table-row>
					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Social Insurance Number'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='sinRaw']/@value"/>
                    </xsl:call-template>                
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Title'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='personTitle']/@value"/>
                    </xsl:call-template>                
                 </fo:table-row>   
                 <fo:table-row>  
                      <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'First name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='firstName']/@value"/>
                    </xsl:call-template> 
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Middle Name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='middleInitial']/@value"/>
                    </xsl:call-template>                     
                 </fo:table-row> 
                 <fo:table-row>                                     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='lastName']/@value"/>
                    </xsl:call-template>  
 					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name at birth (if different from Last name)'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='LastNameAtBirth']/@value"/>
                    </xsl:call-template>                                      
                </fo:table-row>
                <fo:table-row>          
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of birth'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='dateOfBirth']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Gender'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='genderDropdown']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>          
            </fo:table-body>
        </fo:table>
    </xsl:template>   
	<!-- END: FEATURE 94380  -->
    
      <!-- Personal Info -->
     <xsl:template name="personalInformation">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='Person']/@internal-id"/>
            <xsl:with-param name="title" select="'Personal Information'"/>
        </xsl:call-template>
        
        <fo:block>
        	<xsl:text>Applicant Details</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         
        <fo:block>
              <xsl:text>Intake Details</xsl:text>
        </fo:block>
        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">    
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Title'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='personTitle']/@value"/>
                    </xsl:call-template>                
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Social Insurance Number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='sinRaw']/@value"/>
                    </xsl:call-template>
                 </fo:table-row>   
                 <fo:table-row>  
                       <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'OAS Account Number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='personID']/@value"/>
                    </xsl:call-template>
                      <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'First name'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='firstName']/@value"/>
                    </xsl:call-template> 
                 </fo:table-row> 
                 <fo:table-row>                  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Middle Name'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='middleInitial']/@value"/>
                    </xsl:call-template>                                       
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='lastName']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Last name at birth (if different from last name)'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='LastNameAtBirth']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of birth'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='dateOfBirth']/@value"/>
                    </xsl:call-template>
                </fo:table-row>          
            </fo:table-body>
        </fo:table>
    </xsl:template>
    
      <xsl:template name="addressInformation">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='ResidentialAddress']/@internal-id"/>
            <xsl:with-param name="title" select="'Address Information'"/>
        </xsl:call-template>      
        <xsl:text>Residential Address</xsl:text>       
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street name'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <xsl:variable name="resAddressCountry" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                <xsl:if test="$resAddressCountry='Canada'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Province or Territory'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='province']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Postal code'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='postalCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='state']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry!='Canada' and $resAddressCountry!='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State/Province/Region'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='addressLine4']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Is your mailing address different from your residential address?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']/attributes/attribute[@name='isMailingAddressUpdate']/@value"/>
                    </xsl:call-template>
                </fo:table-row>			          
            </fo:table-body>
        </fo:table>
        <!-- check if mailing addresss is entered -->
          
        <xsl:for-each select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Person']">
            <xsl:variable name="isPrimaryParticipant" select="attributes/attribute[@name='isPrimaryParticipant']/@value"/>
            <xsl:variable name="mailingAddressSame" select="attributes/attribute[@name='sameMailingAddress']/@value"/>
            <xsl:variable name="mailingCountryVal" select="attributes/attribute[@name='mailingCountry']/@value"/>
            <xsl:if test="$isPrimaryParticipant='true' and $mailingAddressSame='false' and $mailingCountryVal='Canada'">
                <xsl:call-template name="mailingAddressCanada"/>
            </xsl:if>
            <xsl:if test="$isPrimaryParticipant='true' and $mailingAddressSame='false' and $mailingCountryVal!='Canada'">
                <xsl:call-template name="mailingAddressInformation"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="mailingAddressInformation">
       <xsl:text>Mailing Address</xsl:text> 
          <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 1'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 2'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'PO BOX/Rural Route/General Delivery'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine3']/@value"/>
                    </xsl:call-template> 
                </fo:table-row>
                <fo:table-row>                                                       
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'State'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='state']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Zip Code'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='zipCode']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='IntlMailAddress']/attributes/attribute[@name='country']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <xsl:template name="mailingAddressCanada">
      <xsl:text>Mailing Address</xsl:text> 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 1'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='addressLine1']/@value"/>                    
                    </xsl:call-template>               
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 2'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'PO Box/Rural Route/General Delivery'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='addressLine3']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Province or Territory'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='province']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Postal code'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='postalCode']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MailingAddress']/attributes/attribute[@name='country']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>

	<!-- BEGIN: FEATURE 94380  -->
      <xsl:template name="addressInformationUA">
      <!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='ResidentialAddress']/@internal-id"/>
            <xsl:with-param name="title" select="'Address Information'"/>
        </xsl:call-template>  
         -->  
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Address Information'"/>
        </xsl:call-template>  
                  
        <xsl:text>Residential Address</xsl:text>       
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street name'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <xsl:variable name="resAddressCountry" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                <xsl:if test="$resAddressCountry='Canada'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Province or Territory'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='province']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Postal code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='postalCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='state']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry!='Canada' and $resAddressCountry!='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State/Province/Region'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='addressLine4']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Is your mailing address different from your residential address?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isMailingAddressUpdate']/@value"/>
                    </xsl:call-template>
                </fo:table-row>			          
            </fo:table-body>
        </fo:table>
        <!-- check if mailing addresss is entered -->
          
            <xsl:variable name="isPrimaryParticipant" select="attributes/attribute[@name='isPrimaryParticipant']/@value"/>
            <xsl:variable name="mailingAddressSame" select="attributes/attribute[@name='sameMailingAddress']/@value"/>
            <xsl:variable name="mailingCountryVal" select="attributes/attribute[@name='mailingCountry']/@value"/>
            <xsl:if test="$isPrimaryParticipant='true' and $mailingAddressSame='false' and $mailingCountryVal='Canada'">
                <xsl:call-template name="mailingAddressCanadaUA"/>
            </xsl:if>
            <xsl:if test="$isPrimaryParticipant='true' and $mailingAddressSame='false' and $mailingCountryVal!='Canada'">
                <xsl:call-template name="mailingAddressInformationUA"/>
            </xsl:if>			
    </xsl:template>

      <xsl:template name="addressInformationPartnerUA">                 
        <xsl:text>Residential Address</xsl:text>       
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street name'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <xsl:variable name="resAddressCountry" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                <xsl:if test="$resAddressCountry='Canada'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Province or Territory'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='province']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Postal code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='postalCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='ResidentialAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='state']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="$resAddressCountry!='Canada' and $resAddressCountry!='United States'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State/Province/Region'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='addressLine4']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="entities/entity[@name='IntlResAddress']/attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Is your mailing address different from your residential address?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isMailingAddressUpdate']/@value"/>
                    </xsl:call-template>
                </fo:table-row>			          
            </fo:table-body>
        </fo:table>    			
    </xsl:template>
    
    <xsl:template name="mailingAddressInformationUA">
       <xsl:text>Mailing Address</xsl:text> 
          <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 1'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 2'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'PO BOX/Rural Route/General Delivery'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='addressLine3']/@value"/>
                    </xsl:call-template> 
                </fo:table-row>
                <fo:table-row>                                                       
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'State'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='state']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Zip Code'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='zipCode']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='IntlMailAddress']/attributes/attribute[@name='country']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <xsl:template name="mailingAddressCanadaUA">
      <xsl:text>Mailing Address</xsl:text> 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='suiteNum']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 1'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='addressLine1']/@value"/>                    
                    </xsl:call-template>               
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street 2'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'PO Box/Rural Route/General Delivery'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='addressLine3']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Province or Territory'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='province']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Postal code'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='postalCode']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='MailingAddress']/attributes/attribute[@name='country']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>	
	<!--END: FEATURE 94380   -->	

	<!-- UA Communication Preferences UA -->
   <xsl:template name="communicationPreferencesUA">
		   <!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationPref']/@internal-id"/>
            <xsl:with-param name="title" select="'Contact Information'"/>
        </xsl:call-template>
         -->
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Contact Information'"/>
        </xsl:call-template>         

 		<fo:block>
       		<xsl:text>Preferred Contact Information</xsl:text> 
         </fo:block>
                      
         <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>   

 		<fo:block>
             <xsl:text>Language Preference</xsl:text>  
         </fo:block>          
       
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Do you want Service Canada to communicate with you in English or in French? '"/>
                        <xsl:with-param name="value" select="entities/entity[@name='CommunicationPref']/attributes/attribute[@name='languageS']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    

	<!-- Communication Preferences -->
   <xsl:template name="communicationPreferences">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationPref']/@internal-id"/>
            <xsl:with-param name="title" select="'Contact Information'"/>
        </xsl:call-template>
       <xsl:text>Preferred Contact Information</xsl:text>
         <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>   
 		<fo:block>
             <xsl:text>Language Preference</xsl:text>  
         </fo:block>          
       
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Preferred Language of communication'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='CommunicationPref']/attributes/attribute[@name='languageS']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    
    <!-- Contact Information -->
    <xsl:template name="contactInformation">
        <xsl:for-each select="/DOCUMENT/DATA//entity[@name='CommunicationDetails']">
            <xsl:variable name="isPrimaryCommunication" select="attributes/attribute[@name='isPrimaryCommunication']/@value"/>
            <xsl:if test="$isPrimaryCommunication='true'">
                <xsl:call-template name="sectionTitle">
                    <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationPrimary']/@internal-id"/>
                    <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationPrimary']/title[text()]"/>
                </xsl:call-template>
                
             <fo:block>
             	<xsl:text>Preferred Phone Number</xsl:text>  
             </fo:block> 

                <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                    <fo:table-body>
                        <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone Type'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneType']/@value"/>
                            </xsl:call-template>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Country Code'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='countryCode']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Area code (555)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneAreaCode']/@value"/>
                            </xsl:call-template>                        
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone number (5551212)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='fullPhoneNumber']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Extension'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneExt']/@value"/>
                            </xsl:call-template>                        
                        </fo:table-row>                                   
                    </fo:table-body>
                </fo:table>
            </xsl:if>
            <xsl:if test="$isPrimaryCommunication='false'">
                <xsl:call-template name="sectionTitle">
                    <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationAlternate']/@internal-id"/>
                    <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationAlternate']/title[text()]"/>
                </xsl:call-template>
                <xsl:text>Alternate Contact Information</xsl:text>
                <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                    <fo:table-body>
                        <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone Type'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneType']/@value"/>
                            </xsl:call-template>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Country Code'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='countryCode']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Area code (555)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneAreaCode']/@value"/>
                            </xsl:call-template>                        
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone number (5551212)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='fullPhoneNumber']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Extension'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneExt']/@value"/>
                            </xsl:call-template>                        
                        </fo:table-row>                       
                    </fo:table-body>
                </fo:table>
            </xsl:if>
        </xsl:for-each>
       
    <!--     <xsl:text>Alert Preferences</xsl:text>
              <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
              <xsl:variable name="hasAlerts" select="/DOCUMENT/DATA//entity[@name='AlertPreferences']/attributes/attribute[@name='receiveAlerts']/@value"/>
                  <fo:table-body>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Would you like to receive alerts?'"/>
                                <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='AlertPreferences']/attributes/attribute[@name='receiveAlerts']/@value"/>
                            </xsl:call-template>
                      </fo:table-row>
                      <xsl:if test="$hasAlerts='yes'">
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'How would you like to receive text alerts?'"/>
                                <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='AlertPreferences']/attributes/attribute[@name='fullPhoneNumber']/@value"/>
                            </xsl:call-template>                        
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'How many alerts would you prefer to receive?'"/>
                                <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='AlertPreferences']/attributes/attribute[@name='alertOccur']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                        </xsl:if>
                      </fo:table-body>
                     </fo:table> --> 
    </xsl:template>

	<!--BEGIN FEATURE 94380  -->
    <xsl:template name="contactInformationUA">
		 <!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationPref']/@internal-id"/>
            <xsl:with-param name="title" select="'Contact Information'"/>
        </xsl:call-template>
         -->
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Contact Information'"/>
        </xsl:call-template>         

 		<fo:block>
       		<xsl:text>Preferred Contact Information</xsl:text> 
         </fo:block>
                      
         <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>   

 		<fo:block>
             <xsl:text>Language Preference</xsl:text>  
         </fo:block>          
       
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Do you want Service Canada to communicate with you in English or in French? '"/>
                        <xsl:with-param name="value" select="entities/entity[@name='CommunicationPref']/attributes/attribute[@name='languageS']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        
        <xsl:for-each select="entities/entity[@name='CommunicationDetails']">
            <xsl:variable name="isPrimaryCommunication" select="attributes/attribute[@name='isPrimaryCommunication']/@value"/>
            <xsl:if test="$isPrimaryCommunication='true'">
            	<!-- 
                <xsl:call-template name="sectionTitle">
                    <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationPrimary']/@internal-id"/>
                    <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationPrimary']/title[text()]"/>
                </xsl:call-template>
                 -->                 
                
             <fo:block>
             	<xsl:text>Preferred Phone Number</xsl:text>  
             </fo:block> 

                <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                    <fo:table-body>
                        <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone Type'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneType']/@value"/>
                            </xsl:call-template>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Country Code'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='countryCode']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Area code (555)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneAreaCode']/@value"/>
                            </xsl:call-template>                        
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone number (5551212)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='fullPhoneNumber']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Extension'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneExt']/@value"/>
                            </xsl:call-template>                        
                        </fo:table-row>                                   
                    </fo:table-body>
                </fo:table>
            </xsl:if>
            <xsl:if test="$isPrimaryCommunication='false'">
				<!-- 
                <xsl:call-template name="sectionTitle">
                    <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationAlternate']/@internal-id"/>
                    <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/cluster[@entity='CommunicationDetails' and @grouping-id='CommunicationAlternate']/title[text()]"/>
                </xsl:call-template>
                 -->
		        <fo:block>
		        	<xsl:text>Alternate Contact Information</xsl:text>
		        </fo:block>
                
		         <fo:block>
		            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
		        </fo:block>  
                        
                <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                    <fo:table-body>
                        <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone Type'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneType']/@value"/>
                            </xsl:call-template>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Country Code'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='countryCode']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Area code (555)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneAreaCode']/@value"/>
                            </xsl:call-template>                        
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Phone number (5551212)'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='fullPhoneNumber']/@value"/>
                            </xsl:call-template>
                        </fo:table-row>
                      <fo:table-row>
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Extension'"/>
                                <xsl:with-param name="value" select="attributes/attribute[@name='phoneExt']/@value"/>
                            </xsl:call-template>                        
                        </fo:table-row>                       
                    </fo:table-body>
                </fo:table>
            </xsl:if>
        </xsl:for-each>
       
        <fo:block>
        	<xsl:text>Alert Preferences</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         

		<fo:table table-layout="fixed" width="100%"
			border-collapse="separate" border-spacing="0px 10px">
			<xsl:variable name="hasAlerts"
				select="entities/entity[@name='AlertPreferences']/attributes/attribute[@name='receiveAlerts']/@value" />
			<fo:table-body>
				<fo:table-row>
					<xsl:call-template name="cell2Blocks">
						<xsl:with-param name="label"
							select="'Would you like to receive alerts?'" />
						<xsl:with-param name="value"
							select="entities/entity[@name='AlertPreferences']/attributes/attribute[@name='receiveAlerts']/@value" />
					</xsl:call-template>
				</fo:table-row>
				<xsl:if test="$hasAlerts='Yes'">
					<fo:table-row>
						<xsl:call-template name="cell2Blocks">
							<xsl:with-param name="label"
								select="'How would you like to receive text alerts?'" />
							<xsl:with-param name="value"
								select="entities/entity[@name='AlertPreferences']/attributes/attribute[@name='fullPhoneNumber']/@value" />
						</xsl:call-template>
						<xsl:call-template name="cell2Blocks">
							<xsl:with-param name="label"
								select="'How many alerts would you prefer to receive?'" />
							<xsl:with-param name="value"
								select="entities/entity[@name='AlertPreferences']/attributes/attribute[@name='alertOccur']/@value" />
						</xsl:call-template>
					</fo:table-row>
				</xsl:if>
			</fo:table-body>
		</fo:table> 
    </xsl:template>
	<!-- END FEATURE 94380 -->    

 <!-- OAS Pension -->
    <xsl:template name="OASPension">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='OASPension']/@internal-id"/>
            <xsl:with-param name="title" select="'Old Age Security Pension'"/>
        </xsl:call-template>
		<xsl:text>Old Age Security Pension Start Date</xsl:text>
		
		 <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 
		
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'When does applicant want their Old Age Security pension to start?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='OASPension']/attributes/attribute[@name='reqPensionStartOption']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Requested pension start date'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='OASPension']/attributes/attribute[@name='reqPensionStartDate']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template> 
    
   <!-- Legal Status -->    
    <xsl:template name="LegalStatus">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='LegalStatus']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='LegalStatus']/title[text()]"/>
        </xsl:call-template>  
       
       	<fo:block> 
			<xsl:text>Legal Status</xsl:text>  
		</fo:block>
 		<fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>  		
		<fo:block>
          <xsl:text>Country of Birth</xsl:text>  
		</fo:block>
		
		       
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px"> 
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant indicate they were born outside of Canada?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='isOutOfCABorned']/@value"/>
                    </xsl:call-template>
                </fo:table-row>                         
                <fo:table-row>                                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Applicant is living in Canada and is a'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='inCALegalStatus']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'If applicant entered Canada from another country, were they admitted into Canada under a sponsorship agreement?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='SponsorshipInfo']/attributes/attribute[@name='isSpouseOrCLPartnrSponsored']/@value"/>
                    </xsl:call-template>                                     
                </fo:table-row>
            </fo:table-body>
        </fo:table> 
  <!-- First entry into Canada -->
  		<xsl:text>First entry into Canada</xsl:text>
           <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>                                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'On what date did the applicant first enter Canada?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='canadaFirstEntryDate']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'What type of immigration document did the applicant have when they entered Canada?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='immigrationDocType']/@value"/>
                    </xsl:call-template>                                     
                </fo:table-row>            
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has the applicant always lived in Canada since age 18?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='isLivedInCASinceAge18']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
            </fo:table-body>
        </fo:table>                 
    </xsl:template> 

	<!-- BEGIN: FEATURE 94380  -->
   <xsl:template name="residenceHistoryUA">
   		<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/title[text()]"/>
        </xsl:call-template>  
         -->

        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Residence Period'"/>
        </xsl:call-template> 
              
		<fo:block>
        	<xsl:text>Residence</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 
                		        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Have you lived in Canada for the last two years?'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='LegalStatus']/attributes/attribute[@name='isAplcntLivedInCAForLast2Yr']/@value"/>
                    </xsl:call-template>                                                                                                                 
                </fo:table-row>
            </fo:table-body>
        </fo:table>  

		<fo:block>
        	<xsl:text>Residence History</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         

        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
            <xsl:for-each select="entities/entity[@name='ResidenceHistory']">
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistCountry']/@value"/>
                    </xsl:call-template>                                   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'From'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistStartDate']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'To'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistEndDate']/@value"/>
                    </xsl:call-template> 
                   <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did you also work in this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='didWorkInCountry']/@value"/>
                    </xsl:call-template>   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Have you applied for a benefit from this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='didApplyForBenfInCountry']/@value"/>
                    </xsl:call-template>            
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Under which insurance or identification number in this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='foreignCountryIDNumber']/@value"/>
                    </xsl:call-template>                                                                                        
                </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>                       
    </xsl:template>        

   <xsl:template name="residenceHistoryPartnerUA">
   		<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/title[text()]"/>
        </xsl:call-template>  
         -->

        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Spouse/Common-Law Partner Residence Period'"/>
        </xsl:call-template> 
              
		<fo:block>
        	<xsl:text>Residence</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 
                		        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has your spouse or common-law partner lived in Canada for the last two years?'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='LegalStatus']/attributes/attribute[@name='isPartnerLivedInCAForLast2Yr']/@value"/>
                    </xsl:call-template>                                                                                                                 
                </fo:table-row>
            </fo:table-body>
        </fo:table>  

		<fo:block>
        	<xsl:text>Residence History</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         

        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
            <xsl:for-each select="entities/entity[@name='ResidenceHistory']">
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistCountry']/@value"/>
                    </xsl:call-template>                                   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'From'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistStartDate']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'To'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='residenceHistEndDate']/@value"/>
                    </xsl:call-template> 
                   <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did you also work in this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='didWorkInCountry']/@value"/>
                    </xsl:call-template>   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Have you applied for a benefit from this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='didApplyForBenfInCountry']/@value"/>
                    </xsl:call-template>            
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Under which insurance or identification number in this country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='foreignCountryIDNumber']/@value"/>
                    </xsl:call-template>                                                                                        
                </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>                       
    </xsl:template>   
	<!-- END: FEATURE 94380  -->
	         
	<!-- Residence History -->
   <xsl:template name="ResidenceHistory">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/title[text()]"/>
        </xsl:call-template>  
		<xsl:text>Residence History</xsl:text>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistCountry']/@value"/>
                    </xsl:call-template>                                   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Start'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistStartDate']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'End'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistEndDate']/@value"/>
                    </xsl:call-template> 
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did applicant indicate they worked in this country?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='didWorkInCountry']/@value"/>
                    </xsl:call-template>   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did applicant indicate they have applied for a benefit from this country?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='didApplyForBenfInCountry']/@value"/>
                    </xsl:call-template>            
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Insurance or identification number from this country'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='foreignCountryIDNumber']/@value"/>
                    </xsl:call-template>                                                                                       
                </fo:table-row>
            </fo:table-body>
        </fo:table>              
    </xsl:template>  
    
     <xsl:template name="sponsorshipInfoUA">

        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Sponsorship Information'"/>
        </xsl:call-template> 

        <fo:block>
        	<xsl:text>Sponsorship Details</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 
		
 		<fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>                                    
  					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did you enter Canada under a sponsorship agreement?'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='SponsorshipInfo']/attributes/attribute[@name='isApplicantSponsored']/@value"/>
                    </xsl:call-template>                       
                </fo:table-row>
                <fo:table-row>                                    
  					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Was your spouse/common-law partner admitted to Canada under a sponsorship agreement?'"/>
                        <xsl:with-param name="value" select="entities/entity[@name='SponsorshipInfo']/attributes/attribute[@name='isSpouseOrCLPartnrSponsored']/@value"/>
                    </xsl:call-template>                       
                </fo:table-row>                
            </fo:table-body>
        </fo:table>                 		
	</xsl:template>	      
    
<!-- Nature of Absences -->   
   <xsl:template name="AbsenceDetails">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='AbsenceDetails']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='AbsenceDetails']/title[text()]"/>
        </xsl:call-template> 
		<xsl:text>Nature of Absences</xsl:text>                
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Nature of Absence (if applicant indicated any)'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='AbsenceDetails']/attributes/attribute[@name='absenceReason']/@value"/>
                    </xsl:call-template>  
                </fo:table-row>
            </fo:table-body>
        </fo:table>              
    </xsl:template>
    
    <!-- GIS FOR COMBINED -->     
   <xsl:template name="GIS">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='GIS']/@internal-id"/>
            <xsl:with-param name="title" select="'Guaranteed Income Supplement'"/>
        </xsl:call-template> 
		<xsl:text>Guaranteed Income Supplement Application</xsl:text>                   
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Does the applicant want to apply for Guaranteed Income Supplement?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='GIS']/attributes/attribute[@name='applyingForGISInd']/@value"/>
                    </xsl:call-template>                   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant complete any Guaranteed Income Supplement related sections?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='GIS']/attributes/attribute[@name='isAnyGISSecCompleted']/@value"/>
                    </xsl:call-template>                      
                </fo:table-row>
            </fo:table-body>
        </fo:table>              
    </xsl:template>
    
    <!-- GIS Legal Status -->
 <xsl:template name="GISLegalStatus">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='LegalStatus']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='LegalStatus']/title[text()]"/>
        </xsl:call-template>  
       
       	<fo:block> 
			<xsl:text>Legal Status</xsl:text>  
		</fo:block>
		
 <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>                                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has the applicant lived in Canada for the last two years?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='LegalStatus']/attributes/attribute[@name='isAplcntLivedInCAForLast2Yr']/@value"/>
                    </xsl:call-template>
  					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'If applicant entered Canada from another country, were they admitted into Canada under a sponsorship agreement?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='SponsorshipInfo']/attributes/attribute[@name='isApplicantSponsored']/@value"/>
                    </xsl:call-template>                       
                </fo:table-row>
            </fo:table-body>
        </fo:table>                 		
	</xsl:template>	 
	
    <!-- GIS Residence Period -->	 
 <xsl:template name="GISResidencePeriod">     
       <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ResidenceHistory']/title[text()]"/>
        </xsl:call-template> 
         
		<xsl:text>Residence History</xsl:text>              
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Country'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistCountry']/@value"/>
                    </xsl:call-template>                                   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Start'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistStartDate']/@value"/>
                    </xsl:call-template>     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'End'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ResidenceHistory']/attributes/attribute[@name='residenceHistEndDate']/@value"/>
                    </xsl:call-template>  
               </fo:table-row>
            </fo:table-body>
        </fo:table>       
	</xsl:template>	      

	<!-- BEGIN: FEATURE 94380  -->  
     <xsl:template name="retirementIncomeUA">
		<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/title[text()]"/>
            <xsl:with-param name="title" select="'Income'"/>            
        </xsl:call-template> 
		 -->
	     <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Income'"/>
        </xsl:call-template>
		
		 <xsl:text>Retirement Income details</xsl:text>
		                 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
			<xsl:for-each select="entities/entity[@name='RetirementIncome']">
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did you retire in the last two years, or do you expect to retire in the next two years?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isRetirdOrRetirng2Yrs']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Retirement Date'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='retirementDate']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>               
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has your pension income dropped in the last two years or do you expect it to drop in the next two years?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isPensionReduced2Yrs']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of reduction'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='reductionDate']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>                
				</xsl:for-each>				
            </fo:table-body>
        </fo:table>              
    </xsl:template>
     <xsl:template name="retirementIncomePartnerUA">
 		<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/@internal-id"/>
			<xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/title[text()]"/> 
            <xsl:with-param name="title" select="'Income'"/>            
        </xsl:call-template> 
         -->

	     <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Spouse/Common-Law Partner Income Details'"/>
        </xsl:call-template>

        <fo:block>
        	<xsl:text>Income Details</xsl:text>
        </fo:block>
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 

		 <xsl:text>Retirement Income details</xsl:text>
		                 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
			<xsl:for-each select="entities/entity[@name='RetirementIncome']">
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did your spouse/common-law partner retire in the last two years, or that they expect to retire in the next two years?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isRetirdOrRetirng2Yrs']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Retirement Date'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='retirementDate']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>               
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has your spouse/common-law partner's pension income dropped in the last two years or does your  spouse/common-law partner expect it to drop in the next two years?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='isPensionReduced2Yrs']/@value"/>
                    </xsl:call-template>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of reduction'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='reductionDate']/@value"/>
                    </xsl:call-template>                    
                </fo:table-row>                
				</xsl:for-each>				
            </fo:table-body>
        </fo:table>              
    </xsl:template>    
	<!-- END: FEATURE 94380  -->	   
	
<!-- Applicant's Income -->   
   <xsl:template name="RetirementIncome">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='RetirementIncome']/title[text()]"/>
        </xsl:call-template> 
		<xsl:text>Applicant's Income</xsl:text>               
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant retire from work in the last two years, or do they expect to retire in the next two years?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='RetirementIncome']/attributes/attribute[@name='isRetirdOrRetirng2Yrs']/@value"/>
                    </xsl:call-template>                    
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Retirement Date'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='RetirementIncome']/attributes/attribute[@name='retirementDate']/@value"/>
                    </xsl:call-template>                      
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Has the applicants pension income been reduced in the last two years or do they expect it will be reduced in the next two years?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='RetirementIncome']/attributes/attribute[@name='isPensionReduced2Yrs']/@value"/>
                    </xsl:call-template>                     
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date of reduction'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='RetirementIncome']/attributes/attribute[@name='reductionDate']/@value"/>
                    </xsl:call-template>                      
                </fo:table-row>               
            </fo:table-body>
        </fo:table>              
    </xsl:template>
    
    
    <!-- UA Foreign Income -->                    
	<xsl:template name="foreignIncomeUA">
		<!-- 	
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/title[text()]"/>
        </xsl:call-template>  
		 -->
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Foreign Income'"/>
        </xsl:call-template> 

		<xsl:text>Foreign Income</xsl:text>                

        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         
        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">    
            <fo:table-body>
                <fo:table-row>
					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Do you receive income from another country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='hasForeignIncome']/@value"/>
                    </xsl:call-template>                             
                 </fo:table-row>         
            </fo:table-body>
        </fo:table>

		<xsl:text>Foreign Income Details</xsl:text>    
						                 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
               <fo:table-row font-size="8pt" font-weight="bold">
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Country</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Year</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Amount reported</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Type of Income</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Amount reported in Canadian Dollars</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Currency</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>                                                                                                    
                </fo:table-row>            
			<xsl:for-each select="entities/entity[@name='ForeignIncome']">
                        <fo:table-row font-size="8pt">
                        	<fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeCountry']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeYear']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeAmount']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeType']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>                            
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline> 
	                    				<!-- TODO: No field for amount in Canadian dollar -->                                		
                                		<xsl:text>NA </xsl:text>
                                		 <!--                                    
                                        <xsl:value-of select="attributes/attribute[@name='incomeType']/@value"/>
                                         -->
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>                                                        
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeCurrency']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>         
				</xsl:for-each>				
            </fo:table-body>
        </fo:table>              
    </xsl:template>

    <!-- UA Foreign Income -->                    
	<xsl:template name="foreignIncomePartnerUA">
		<!-- 	
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/title[text()]"/>
        </xsl:call-template>  
		 -->
        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Spouse/Common-Law Partner Foreign Income'"/>
        </xsl:call-template> 

		<xsl:text>Foreign Income Details</xsl:text>                

        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>         
        
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">    
            <fo:table-body>
                <fo:table-row>
					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Does your spouse/common-law partner receive income from another country?'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='hasForeignIncome']/@value"/>
                    </xsl:call-template>                             
                 </fo:table-row>         
            </fo:table-body>
        </fo:table>

		<xsl:text>Foreign Income Details</xsl:text>    
						                 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
               <fo:table-row font-size="8pt" font-weight="bold">
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Country</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Year</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Amount reported</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Type of Income</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Amount reported in Canadian Dollars</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                	 <fo:table-cell>
                        <fo:block>
                            <fo:inline>
                                <xsl:text>Currency</xsl:text>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>                                                                                                    
                </fo:table-row>            
			<xsl:for-each select="entities/entity[@name='ForeignIncome']">
                        <fo:table-row font-size="8pt">
                        	<fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeCountry']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeYear']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeAmount']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeType']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>                            
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline> 
	                    				<!-- TODO: No field for amount in Canadian dollar -->                                		
                                		<xsl:text>NA </xsl:text>
                                		 <!--                                    
                                        <xsl:value-of select="attributes/attribute[@name='incomeType']/@value"/>
                                         -->
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>                                                        
                            <fo:table-cell>
                                <fo:block>
                                    <fo:inline>
                                        <xsl:value-of select="attributes/attribute[@name='incomeCurrency']/@value"/>
                                    </fo:inline>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>         
				</xsl:for-each>				
            </fo:table-body>
        </fo:table>              
    </xsl:template>
           
    <!-- Foreign Income -->                    
	<xsl:template name="ForeignIncome">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='ForeignIncome']/title[text()]"/>
        </xsl:call-template>  
		<xsl:text>Foreign Income Details</xsl:text>                
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant receive income from another country?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='ForeignIncome']/attributes/attribute[@name='isReceivingForeignIncome']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row>               
            </fo:table-body>
        </fo:table>              
    </xsl:template>   
    
    <!-- GIS MANUAL INCOME -->
	<xsl:template name="GISManualIncome">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='Income']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='Income']/title[text()]"/>
        </xsl:call-template>  
              
       	<fo:block> 
			<xsl:text>Manually Reported Income</xsl:text>  
		</fo:block>   
		
       	<fo:block> 
			<xsl:text>Income Details for Applicant</xsl:text>  
		</fo:block> 		     
 	           
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>                    
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Year'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='incomeYear']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row> 
                                                   
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Canada Pension Plan or Quebec Pension Plan benefits'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='cppOrQPPAmount']/@value"/>
                    </xsl:call-template>   
 					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Foreign pension income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='frgnPensionAmount']/@value"/>
                    </xsl:call-template>                                       
                </fo:table-row>  
                                 
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Employment Insurance'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='eiAmount']/@value"/>
                    </xsl:call-template>   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Workers compensation benefits'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='wcbAmount']/@value"/>
                    </xsl:call-template>                                      
                </fo:table-row>   
                                               
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Interest and other investment income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='interestAndInvstAmount']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row> 
                 
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Eligible and other than eligible dividends'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='dividendsAmount']/@value"/>
                    </xsl:call-template> 
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Capital Gains'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='capitalGainsAmount']/@value"/>
                    </xsl:call-template>                                        
                </fo:table-row>                  
                
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Net rental income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='netRentalAmount']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row>                  
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Net employment income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='netEmploymentAmount']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row>  
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Net self-employment income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='netSelfEmploymentAmount']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row>  
                                
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Source'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='incomeSource']/@value"/>
                    </xsl:call-template>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Other Income'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Income']/attributes/attribute[@name='otherIncmAmount']/@value"/>
                    </xsl:call-template>                                       
                </fo:table-row>                                                                                    
            </fo:table-body>
        </fo:table>              
    </xsl:template>       
 
<!-- Marital Status Info -->                    
	<xsl:template name="MaritalStatusInfo">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='MaritalStatusInfo']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='MaritalStatusInfo']/title[text()]"/>
        </xsl:call-template>           
        <fo:block padding-bottom="2pt"> 
		<xsl:text>Marital Status Information</xsl:text>
		</fo:block>	
 		<fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>     		
		<fo:block>
			<xsl:text>Marital Status</xsl:text>  
		</fo:block>		
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>   
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Current Marital Status'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='currentMaritalStatus']/@value"/>
                    </xsl:call-template>                   
                </fo:table-row>               
            </fo:table-body>
        </fo:table>              
    </xsl:template> 
    
<!-- UA Marital Status Info -->                    
	<xsl:template name="maritalStatusInfoUA">
 		<!-- 
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='MaritalStatusInfo']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='MaritalStatusInfo']/title[text()]"/>
        </xsl:call-template>
           -->

        <xsl:call-template name="sectionTitleOAS">
            <xsl:with-param name="title" select="'Marital Status'"/>
        </xsl:call-template>            
                    
        <fo:block padding-bottom="2pt"> 
		<xsl:text>Marital Status Information</xsl:text>
		</fo:block>	
 		<fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block>     		
		<fo:block>
			<xsl:text>Marital Status</xsl:text>  
		</fo:block>	
		
 		<xsl:variable name="maritalStatus" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='currentMaritalStatus']/@value" />
        <xsl:variable name="isSeparated" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='isSeparated']/@value" />
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>   
                <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Marital Status'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='currentMaritalStatus']/@value"/>
                    </xsl:call-template> 
                     <xsl:if test="$maritalStatus!='&quot;MS1&quot;'">
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Date'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='maritalDate']/@value" />
                        </xsl:call-template>   
                       </xsl:if>               
                </fo:table-row>  
                <xsl:if test="$maritalStatus='Married' or $maritalStatus='Separated' or $maritalStatus='Common Law'">
                 <fo:table-row>  
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Are you and your spouse/common-law partner living apart for reasons beyond your control?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='isSeparated']/@value"/>
                    </xsl:call-template> 
                     <xsl:if test="$isSeparated">
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Date'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='MaritalStatusInfo']/attributes/attribute[@name='seperationDate']/@value" />
                        </xsl:call-template>   
                       </xsl:if>               
                </fo:table-row>  
                </xsl:if>              
            </fo:table-body>
        </fo:table>              
    </xsl:template>

<!-- Payment Info -->   
    <xsl:template name="paymentInformation">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/condition/cluster[@entity='Payment']/@internal-id" />
            <xsl:with-param name="title" select="'Payment Information'" />
        </xsl:call-template>
		<xsl:text>Payment Information</xsl:text>      

        <!-- check method of payment -->
        <xsl:variable name="prefPaymentMethodValue" select="/DOCUMENT/DATA//entity[@name='Person']/attributes/attribute[@name='preferredPaymentMethod']/@value" />
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'How would the applicant like to receive their payments?'" />
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Person']/attributes/attribute[@name='preferredPaymentMethod']/@value" />
                    </xsl:call-template>           
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Name(s) on the acount'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Payment']/attributes/attribute[@name='bankAccountName']/@value" />
                        </xsl:call-template>                 
                </fo:table-row>
                <xsl:if test="$prefPaymentMethodValue='Direct Deposit'">
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Branch Transit Number'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Payment']/attributes/attribute[@name='bankBranchNum']/@value" />
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Financial Institution Number'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Payment']/attributes/attribute[@name='bankInstNum']/@value" />
                        </xsl:call-template>
                    </fo:table-row>
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Account number'" />
                            <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Payment']/attributes/attribute[@name='accountNumber']/@value" />
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:if>
            </fo:table-body>
        </fo:table>
    </xsl:template>                   
    
    <!-- Taxation Info -->
     
      <xsl:template name="taxInformation">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='TaxInfo']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/summary-page/cluster[@entity='TaxInfo']/title[text()]"/>
        </xsl:call-template>
        
		<xsl:text>Recovery Tax Information</xsl:text> 
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant indicate that they are not a resident of Canada for tax purposes and their net world income is more than the threshold?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='WorldIncome']/attributes/attribute[@name='isNonResidntNdIncmLTThrshld']/@value"/>
                    </xsl:call-template> 
                 </fo:table-row>
          </fo:table-body>
          </fo:table>
                
              <xsl:variable name="dedFedTaxValue" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dedFedTax']/@value"/>   
              <xsl:text>Voluntary Tax Withold</xsl:text>
                <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">   
                  <fo:table-body>
               		<fo:table-row>   
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Does the applicant want Federal income tax deducted from their monthly payment?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dedFedTax']/@value"/>
                    </xsl:call-template>
                    
                <xsl:if test="$dedFedTaxValue='Yes'">
					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Does the applicant want a percentage or a dollar amount for deduction?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dedMethod']/@value"/>
                    </xsl:call-template> 
        		 </xsl:if> 
               </fo:table-row>

               <xsl:variable name="dedMethodValue" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dedMethod']/@value"/>
                <xsl:if test="$dedFedTaxValue='Yes'">
                 <fo:table-row>                                                    
                    <xsl:if test="$dedMethodValue='Dollar amount' and $dedMethodValue!= ''">
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Indicate a dollar amount applicant wants deducted for income tax'"/>
                                <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dollarAmount']/@value"/>
                            </xsl:call-template>
                    </xsl:if>       
                    <xsl:if test="$dedMethodValue!='Dollar amount' and $dedMethodValue!= ''">
                            <xsl:call-template name="cell2Blocks">
                                <xsl:with-param name="label" select="'Indicate a percentage applicant wants deducted for income tax (for example: 20)'"/>
                                <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='TaxInfo']/attributes/attribute[@name='dollarPerct']/@value"/>
                            </xsl:call-template>
    				 </xsl:if>                           
                 </fo:table-row> 
                </xsl:if>          
            </fo:table-body>
        </fo:table>
    </xsl:template> 
    
 <!--  Declaration and Signature -->
 
     <xsl:template name="Declaration">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='Declaration']/@internal-id"/>
            <xsl:with-param name="title" select="'Declaration and Signature'"/>
        </xsl:call-template>
		<xsl:text>Declaration and Signature</xsl:text> 
        <fo:block>
            <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
        </fo:block> 		
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
            <fo:table-body>
                <fo:table-row>
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant sign the declaration?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='didAplicntSigned']/@value"/>
                    </xsl:call-template> 
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the applicant sign the declaration with a mark?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='didAplicntSignedWithMark']/@value"/>
                    </xsl:call-template>                     
                    </fo:table-row>
                <fo:table-row>     
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date Signed'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='aplicntSignedDate']/@value"/>
                    </xsl:call-template>                                   
                 </fo:table-row> 
			</fo:table-body>
		</fo:table>
		 
		<xsl:variable name="didThirdPartySignedAsWitness" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='didTPSignedAsWitness']/@value"/>
		<xsl:if test="$didThirdPartySignedAsWitness='Yes'">
			<xsl:call-template name="thirdParty"/>
		</xsl:if>			 
	</xsl:template>

	<xsl:template name="thirdParty">
		<xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="/DOCUMENT/DATA/summary-page/cluster[@entity='Declaration']/entities/entity[@name='MailingAddresss']/@internal-id"/>
            <xsl:with-param name="title" select="/DOCUMENT/DATA/entity[@name='Declaration']/entities/entity[@name='MailingAddresss']/title[text()]"/>
        </xsl:call-template>
        
	 <xsl:text>Witness or Authorized Persons declaration and signature</xsl:text>  	                
         <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
          <fo:table-body> 
                <fo:table-row>
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Did the Third Party sign as the witness?'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='didTPSignedAsWitness']/@value"/>
                    </xsl:call-template>    
               <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Date Signed'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='thirdPartySignedDate']/@value"/>
                    </xsl:call-template>                     
                </fo:table-row>            
        
              <fo:table-row>
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Name'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='thirdPartyName']/@value"/>
                    </xsl:call-template>                      
                <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Relationship to applicant'"/>
                        <xsl:with-param name="value" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='relationshipToApplicant']/@value"/>
                    </xsl:call-template>                                                 
       		 </fo:table-row>
       		</fo:table-body>
       	</fo:table>
         <xsl:variable name="isAddressCanada" select="/DOCUMENT/DATA//entity[@name='Declaration']/attributes/attribute[@name='mailingCountry']/@value"/>
            <xsl:if test="$isAddressCanada='Canada'">     
              <xsl:call-template name = 'thirdPartyCanada'>
                </xsl:call-template>	
            </xsl:if>
            <xsl:if test="$isAddressCanada!='Canada'">     
              <xsl:call-template name = 'thirdPartyIntl'>
                </xsl:call-template>	
            </xsl:if>   
	</xsl:template>   
	               
     <xsl:template name="thirdPartyCanada">       		 
		<xsl:text>Witness or Authorized Person's Mailing Address</xsl:text>  
        <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
          <fo:table-body>             
      		<xsl:for-each select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Declaration']/entities/entity[@name='MailingAddress']">
           	 <fo:table-row>
               <xsl:call-template name="cell2Blocks">
                     <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                     <xsl:with-param name="value" select="attributes/attribute[@name='suiteNum']/@value"/>
               </xsl:call-template>	
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street number'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
                   <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'PO BOX/Rural Route/General Delivery'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='addressLine3']/@value"/>
                    </xsl:call-template>  
                    </fo:table-row>                                       
                    <fo:table-row>
 					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>                    
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Province or Territory'"/>
                            <xsl:with-param name="value" select="attributes/attribute[@name='province']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                     <fo:table-row>                   
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Postal code'"/>
                            <xsl:with-param name="value" select="attributes/attribute[@name='postalCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                </xsl:for-each>                  
        	 </fo:table-body>
       </fo:table>    
	</xsl:template>        
                    
     <xsl:template name="thirdPartyIntl">
       <xsl:text>Witness or Authorized Person's Mailing Address</xsl:text>  
         <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
          <fo:table-body>         
                <xsl:for-each select="/DOCUMENT/DATA/entity[@name='Application']/entities/entity[@name='Declaration']/entities/entity[@name='IntlMailAddress']">
           	 <fo:table-row>
               <xsl:call-template name="cell2Blocks">
                     <xsl:with-param name="label" select="'Unit/apt/suite number'"/>
                     <xsl:with-param name="value" select="attributes/attribute[@name='suiteNum']/@value"/>
               </xsl:call-template>	
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street number'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='addressLine1']/@value"/>
                    </xsl:call-template>
                </fo:table-row>
                <fo:table-row>
                    <xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'Street name'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='addressLine2']/@value"/>
                    </xsl:call-template>
 					<xsl:call-template name="cell2Blocks">
                        <xsl:with-param name="label" select="'City, Town or Village'"/>
                        <xsl:with-param name="value" select="attributes/attribute[@name='city']/@value"/>
                    </xsl:call-template>                     
                   </fo:table-row> 
                    <fo:table-row>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'State/Province/Region'"/>
                            <xsl:with-param name="value" select="attributes/attribute[@name='state']/@value"/>
                        </xsl:call-template>
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Zip Code'"/>
                            <xsl:with-param name="value" select="attributes/attribute[@name='zipCode']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>
                     <fo:table-row>  
                        <xsl:call-template name="cell2Blocks">
                            <xsl:with-param name="label" select="'Country'"/>
                            <xsl:with-param name="value" select="attributes/attribute[@name='country']/@value"/>
                        </xsl:call-template>
                    </fo:table-row>                    
               </xsl:for-each> 
          </fo:table-body>
       </fo:table>
    </xsl:template>	
    

    <xsl:template name="bdm-cell-header">
        <xsl:param name="headerText"/>
        <fo:table-cell role="TH">
            <fo:block>
                <xsl:variable name="textWrap" select="title[text()]"/>
                <xsl:call-template name="textWrapper">
                    <xsl:with-param name="wrappedText" select="$headerText"/>
                </xsl:call-template>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template name="sectionTitle">
        <xsl:param name="internal-id"/>
        <xsl:param name="title"/>
        <xsl:choose>
            <xsl:when test="$title != ''">
                <fo:block space-before="10px" space-after="10px" line-height="20px" font-size="14px" padding="6px" background-color="{$bg-color-section}" role="{$header-role-section}" id="{$internal-id}">
                    <xsl:value-of select="$title"/>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:block id="{$internal-id}">
                    <fo:leader leader-pattern="rule" leader-length="100%" rule-style="solid" rule-thickness="1px" color="{$bg-color-main}"/>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="sectionTitleOAS">
        <xsl:param name="title"/>
         <fo:block space-before="10px" space-after="10px" line-height="20px" font-size="14px" padding="6px" background-color="{$bg-color-section}" role="{$header-role-section}">
            <xsl:value-of select="$title"/>
         </fo:block>
    </xsl:template>
        
    <xsl:template name="attestationPage">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="'8000000001'"/>
            <xsl:with-param name="title" select="'Attestation'"/>
        </xsl:call-template>
        <xsl:variable name="attestationAttribute" select="/DOCUMENT/DATA//entity[@name='Submission']/attributes/attribute[@name='acceptAttestation']/@value"/>
        <xsl:if test="$attestationAttribute='Yes'">
            <fo:block font-size="8pt" font-weight="bold">
                <fo:block>
                    <xsl:text>I agree that if my circumstances or situation changes I will report it to ESDC.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I declare that the information given to the questions on this application is truthful and accurately reflects my situation.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I understand this information will be used to determine my eligibility for this sample benefit, the supplement benefit and the dependant benefit.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I understand that the information provided is subject to verification.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I understand that if I owe money to this department or other government departments they maybe deducted directly from any benefits received.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I have read and understand the above requirements.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>Personal information on this form is collected under the authority of the Sample Benefit Act. This information will be used to assess your application for benefit. The information you provide on this form will be retained in a Personal Information Bank titled the Sample Benefit Claim File. Your personal information is protected and accessible under the Privacy Act and the Access to Information Act. Instructions for accessing your personal information are giving in the Info Source publication at infosource.gc.ca or at your Service Canada Center.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>This information will not be disclosed to any other person or organization without your approval, except where authorized or required by law.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
            </fo:block>
            <fo:block font-size="8pt">
                <fo:block font-weight="bold">
                    <xsl:text>Do you accept the above attestation</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="$attestationAttribute"/>
                </fo:block>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <xsl:template name="clientAttestationPage">
        <xsl:call-template name="sectionTitle">
            <xsl:with-param name="internal-id" select="'8000000001'"/>
            <xsl:with-param name="title" select="'Attestation'"/>
        </xsl:call-template>
        <xsl:variable name="attestationAttribute" select="/DOCUMENT/DATA//entity[@name='Submission']/attributes/attribute[@name='acceptAttestation']/@value"/>
        <xsl:if test="$attestationAttribute='Yes'">
            <fo:block font-size="8pt" font-weight="bold">
                <fo:block>
                    <xsl:text>As the Attestee, I agree that if the circumstances or situation changes it will be reported to ESDC.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>As the Attestee, I understand that this information will be used to determine eligibility and entitlement for Sample benefits, the supplement benefit and the dependent benefit.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>As the Attestee, I confirm the information I provided is true and complete and subject to verification.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>As the Attestee, I understand that if money is owed to this department or other government departments it may be deducted directly from any benefits received.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>This information will not be disclosed to any other person or organization without your approval, except where authorized or required.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>As the Attestee, I accept the above attestation and wish to submit this information.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>I have read and understand the above requirements.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>Personal information on this form is collected under the authority of the Sample Benefit Act. This information will be used to assess your application for benefit. The information you provide on this form will be retained in a Personal Information Bank titled the Sample Benefit Claim File. Your personal information is protected and accessible under the Privacy Act and the Access to Information Act. Instructions for accessing your personal information are giving in the Info Source publication at infosource.gc.ca or at your Service Canada Center.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:text>This information will not be disclosed to any other person or organization without your approval, except where authorized or required by law.</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
            </fo:block>
            <fo:block font-size="8pt">
                <fo:block font-weight="bold">
                    <xsl:text>Do you accept the above attestation</xsl:text>
                </fo:block>
                <fo:block>
                    <fo:leader leader-pattern="space"/>
                </fo:block>
                <fo:block>
                    <xsl:value-of select="$attestationAttribute"/>
                </fo:block>
            </fo:block>
        </xsl:if>
    </xsl:template>
    <!-- ************************************************************-->
    <!-- * BEGIN - Common functions                                 *-->
    <!-- ************************************************************-->
    <!-- Text wrapping functionality for wrapping the text within the columns.This adds a zero width character(&#x200b) after every character.
       On doing this once it comes near the end of the column it automatically wraps the contents exceeding this column to the next column.
       Doing this stops the overflowing of the contents from one column to the other column.
       
       Please do not make it recursive as it fails to print some contents if you do so.
   -->
    <xsl:template name="textWrapper">
        <xsl:param name="wrappedText"/>
        <xsl:param name="counter" select="0"/>
        <xsl:choose>
            <xsl:when test="$counter &lt;= string-length($wrappedText)">
                <xsl:value-of select='concat(substring($wrappedText,$counter,1),"&#x200b;")'/>
                <xsl:call-template name="textWrapper2">
                    <xsl:with-param name="data" select="$wrappedText"/>
                    <xsl:with-param name="counter" select="$counter+1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
    </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="textWrapper2">
        <xsl:param name="data"/>
        <xsl:param name="counter"/>
        <xsl:value-of select='concat(substring($data,$counter,1),"&#x200b;")'/>
        <xsl:call-template name="textWrapper">
            <xsl:with-param name="wrappedText" select="$data"/>
            <xsl:with-param name="counter" select="$counter+1"/>
        </xsl:call-template>
    </xsl:template>
    <!-- ************************************************************-->
    <!-- * BEGIN - SUMMARY-PDF  Template                            *-->
    <!-- ************************************************************-->
    <!-- SUMMARY-PDF Template variables -->
    <xsl:variable name="isSummaryPagePresent" select="not(not(/DOCUMENT/DATA/summary-page))"/>
    <xsl:variable name="hiddenConditionalElements" select="/DOCUMENT/DATA//conditionalElement[@visible='false']"/>
    <xsl:variable name="bg-color-main">#cacaca</xsl:variable>
    <xsl:variable name="bg-color-section">#eaeaea</xsl:variable>
    <xsl:variable name="bg-color-badge">#B8C1C1</xsl:variable>
    <xsl:variable name="header-role-page">H1</xsl:variable>
    <xsl:variable name="header-role-section">H2</xsl:variable>
    <xsl:attribute-set name="questionValueAttSet">
        <xsl:attribute name="font-size">8px</xsl:attribute>
        <xsl:attribute name="line-height">16px</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="questionLabelAttSet">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="font-size">8px</xsl:attribute>
        <xsl:attribute name="line-height">14px</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template name="header">
        <fo:table table-layout="fixed" width="100%">
            <fo:table-body>
                <fo:table-row>
                        <!-- Logo -->
                        <fo:table-cell display-align="center">
                          <xsl:if test="//logo">
                            <fo:block text-align="left">
                                <xsl:element name="fo:external-graphic">
                                    <xsl:attribute name="src">
                                        <xsl:text>url('data:</xsl:text>
                                        <xsl:value-of select="//logo-type[text()]"/>
                                        <xsl:text>;base64,</xsl:text>
                                        <xsl:apply-templates select="//logo"/>
                                        <xsl:text>')</xsl:text>
                                    </xsl:attribute>
                                    <xsl:attribute name="vertical-align">middle</xsl:attribute>
                                    <xsl:attribute name="content-width">75px</xsl:attribute>
                                    <xsl:attribute name="content-height">50px</xsl:attribute>
                                    <xsl:attribute name="fox:alt-text">
                                        <xsl:value-of select="//logo-alt-text[text()]"/>
                                    </xsl:attribute>
                                </xsl:element>
                            </fo:block>
                           </xsl:if>
                             <fo:block xsl:use-attribute-sets="questionValueAttSet" text-align="left" padding-bottom="2pt">
                            <xsl:value-of select="//application-ref/label[text()]"/>
                            <xsl:text> </xsl:text>
                            <fo:inline font-weight="bold">
                                 <xsl:value-of select="//application-ref/value[text()]"/>
                            </fo:inline>							
                        </fo:block>
                        <fo:block xsl:use-attribute-sets="questionValueAttSet" text-align="left" padding-top="2pt">
                            <xsl:value-of select="//submission-date/label[text()]"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="//submission-date/value[text()]"/>
                        </fo:block>
                        </fo:table-cell>
  
                     <fo:table-cell display-align="center">
                     	<fo:block text-align="end" padding-bottom="2pt">
                            <fo:inline font-weight="bold">
                                 <xsl:text> PROTECTED B (when completed) </xsl:text>
                            </fo:inline>							
                        </fo:block>
                     </fo:table-cell>           
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <xsl:template match="cluster">
        <xsl:variable name="internal-id" select="@internal-id"/>
        <xsl:variable name="grouping-id" select="@grouping-id"/>
        <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])"/>
        <xsl:variable name="isClusterGrouped" select="boolean(@grouping-id)"/>
        <xsl:variable name="isClusterGroupLead" select="contains(ancestor::*/cluster[@grouping-id=$grouping-id][1]/@internal-id, current()/@internal-id)"/>
        <xsl:if test="not($isHiddenElement) and (not($isClusterGrouped) or $isClusterGroupLead)">
            <xsl:call-template name="sectionTitle">
                <xsl:with-param name="internal-id" select="$internal-id"/>
                <xsl:with-param name="title" select="title"/>
            </xsl:call-template>
            <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                <fo:table-body>
                    <xsl:call-template name="processClusters">
                        <xsl:with-param name="clusters" select=". | //cluster[@grouping-id=$grouping-id]"/>
                    </xsl:call-template>
                </fo:table-body>
            </fo:table>
        </xsl:if>
    </xsl:template>
    <xsl:template name="processClusters">
        <xsl:param name="clusters"/>
        <xsl:for-each select="$clusters">
            <!-- filter cluster children to apply templates -->
            <xsl:variable name="clusterChildren" select="./child::container | ./child::question | ./child::list-question"/>
            <xsl:variable name="cluster-internal-id" select="./@internal-id"/>
            <xsl:for-each select="$clusterChildren[position() mod 2 != 0]">
                <fo:table-row>
                    <xsl:apply-templates select=".">
                        <xsl:with-param name="internal-id" select="$cluster-internal-id"/>
                    </xsl:apply-templates>
                    <xsl:if test="./following-sibling::*">
                        <xsl:apply-templates select="./following-sibling::*[1]">
                            <xsl:with-param name="internal-id" select="$cluster-internal-id"/>
                        </xsl:apply-templates>
                    </xsl:if>
                </fo:table-row>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <!-- It is not used-->
    <xsl:template name="sectionDescription">
        <xsl:param name="description"/>
        <fo:block space-before="10px" space-after="10px" line-height="20px" font-size="14px" padding="6px" background-color="{$bg-color-section}">
            <xsl:value-of select="description[text()]"/>
        </fo:block>
    </xsl:template>
    <xsl:template match="list">
        <xsl:variable name="internal-id" select="@internal-id"/>
        <xsl:variable name="isHiddenElement" select="boolean($hiddenConditionalElements[@internal-id=$internal-id])"/>
        <xsl:variable name="isNestedList" select="boolean(contains(local-name(parent::*),'list'))"/>
        <xsl:if test="not($isHiddenElement) and not($isNestedList) ">
            <xsl:call-template name="sectionTitle">
                <xsl:with-param name="internal-id" select="$internal-id"/>
                <xsl:with-param name="title" select="title"/>
            </xsl:call-template>
            <xsl:variable name="columns" select=".//column"/>
            <fo:table table-layout="fixed" width="100%" border-collapse="separate" border-spacing="0px 10px">
                <xsl:call-template name="column-header">
                    <xsl:with-param name="columns" select="$columns"/>
                </xsl:call-template>
                <fo:table-body>
                    <xsl:call-template name="addRows">
                        <xsl:with-param name="columns" select="$columns"/>
                        <xsl:with-param name="max" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id][last()]/@item-index"/>
                        <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index='0' and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index='0']/@loopindex)][1]/@loopindex"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                    </xsl:call-template>
                </fo:table-body>
            </fo:table>
        </xsl:if>
    </xsl:template>
    <xsl:template match="relationship-summary-list">
        <xsl:variable name="internal-id" select="@internal-id"/>
        <xsl:if test="not($hiddenConditionalElements[@internal-id=$internal-id])">
            <xsl:call-template name="sectionTitle">
                <xsl:with-param name="internal-id" select="$internal-id"/>
                <xsl:with-param name="title" select="title"/>
            </xsl:call-template>
            <fo:table table-layout="fixed" width="100%">
                <!-- relationship-body -->
                <fo:table-body>
                    <xsl:call-template name="addRows-relationship">
                        <xsl:with-param name="columns" select="column"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                        <xsl:with-param name="max" select="count(//relationship-summary-list-data/relationships-list/relationships)"/>
                        <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index='0' and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index='0']/@loopindex)][1]/@loopindex"/>
                    </xsl:call-template>
                </fo:table-body>
            </fo:table>
        </xsl:if>
    </xsl:template>
    <xsl:template name="cell-header">
        <xsl:param name="columns"/>
        <xsl:for-each select="$columns">
            <fo:table-cell role="TH">
                <fo:block xsl:use-attribute-sets="questionLabelAttSet">
                    <xsl:variable name="textWrap" select="title[text()]"/>
                    <xsl:call-template name="textWrapper">
                        <xsl:with-param name="wrappedText" select="$textWrap"/>
                    </xsl:call-template>
                </fo:block>
            </fo:table-cell>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="column-header">
        <xsl:param name="columns"/>
        <xsl:for-each select="$columns">
            <fo:table-column xmlns:fox="http://xmlgraphics.apache.org/fop/extensions" fox:header="true" column-width="proportional-column-width(1)"/>
        </xsl:for-each>
        <fo:table-header>
            <fo:table-row>
                <xsl:call-template name="cell-header">
                    <xsl:with-param name="columns" select="$columns"/>
                </xsl:call-template>
            </fo:table-row>
        </fo:table-header>
    </xsl:template>
    <!--  Add list rows
       - columns (Elements): Columns of each row.
       - internal-id (long): Internal id of the list.
       - max (integer):  Maximum number of rows.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list.
  -->
    <xsl:template name="addRows">
        <xsl:param name="columns"/>
        <xsl:param name="internal-id"/>
        <xsl:param name="max"/>
        <xsl:param name="maxLoopindex"/>
        <xsl:choose>
            <!-- No information entered : max won't have a numerical value -->
            <xsl:when test="not($max &lt;= 0 or $max &gt;= 0)">
                <fo:table-row>
                    <xsl:for-each select="$columns">
                        <fo:table-cell role="TD">
                            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                <xsl:text>--</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                    </xsl:for-each>
                </fo:table-row>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="addRow">
                    <xsl:with-param name="columns" select="$columns"/>
                    <xsl:with-param name="index" select="0"/>
                    <xsl:with-param name="internal-id" select="$internal-id"/>
                    <xsl:with-param name="max" select="$max"/>
                    <xsl:with-param name="loopindex" select="0"/>
                    <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
       - columns (Elements): Columns of each row.
       - index   (integer):  Curent index of the list.
       - internal-id (long): Internal id of the parent list.
       - max (integer):  Maximum number of rows.
       - loopindex (integer): Loopindex of the row, when adding a row of a nested list loopindex is bigger than 10000.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list.
  -->
    <xsl:template name="addRow">
        <xsl:param name="columns"/>
        <xsl:param name="index"/>
        <xsl:param name="internal-id"/>
        <xsl:param name="max"/>
        <xsl:param name="loopindex"/>
        <xsl:param name="maxLoopindex"/>
        <xsl:variable name="nestedListIndex" select="10000"/>
        <xsl:variable name="isNestedRow" select="$loopindex &gt;= $nestedListIndex"/>
        <xsl:variable name="nestedIndex" select="(($index + 1)  * ($nestedListIndex))"/>
        <xsl:variable name="parentListValue" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$columns[1]/@id and @item-index=$index and @loopindex=$index]/text()"/>
        <xsl:if test="$index &lt;= $max">
            <!-- evaluate if the parent list has no values ie holes in data, if yes , skip that row -->
            <xsl:if test="not(not($parentListValue) or $parentListValue=' ')">
                <fo:table-row>
                    <xsl:for-each select="$columns">
                        <xsl:variable name="isNestedListColumn" select="not(boolean(normalize-space(../@internal-id)=normalize-space($internal-id)))"/>
                        <xsl:variable name="loopIdx">
                            <!-- loopindex to get the answer of the column, it changes on the based it is a nested column, nested row -->
                            <xsl:choose>
                                <xsl:when test="$isNestedListColumn and not($isNestedRow)">
                                    <xsl:value-of select="($index + 1)  * ($nestedListIndex)"/>
                                </xsl:when>
                                <xsl:when test="$isNestedListColumn and $isNestedRow">
                                    <xsl:value-of select="$loopindex"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$index"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:variable name="id" select="@id"/>
                        <xsl:variable name="answer" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$id and @item-index=$index and @loopindex=$loopIdx]"/>
                        <xsl:variable name="value" select="$answer/text()"/>
                        <xsl:comment>
                            <xsl:value-of select="$value"/>
                        </xsl:comment>
                        <xsl:variable name="rowCount">
                            <xsl:choose>
                                <xsl:when test="$answer/@row-count">
                                    <xsl:value-of select="number($answer/@row-count)"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="0"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:variable name="cellBlock">
                            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                <xsl:choose>
                                    <xsl:when test="not($value) or $value=' '">
                                        <xsl:text>--</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:variable name="textWrap" select="$value"/>
                                        <xsl:call-template name="textWrapper">
                                            <xsl:with-param name="wrappedText" select="$textWrap"/>
                                        </xsl:call-template>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </xsl:variable>
                        <xsl:choose>
                            <xsl:when test="not($isNestedListColumn) and  not($isNestedRow)">
                                <fo:table-cell border-width="0.5mm" number-rows-spanned="{$rowCount}" role="TD">
                                    <xsl:copy-of select="$cellBlock"/>
                                </fo:table-cell>
                            </xsl:when>
                            <xsl:when test="not($isNestedListColumn) and  $isNestedRow">
                                <!-- Do nothing , non nested columns should not be considered for nested rows -->
                            </xsl:when>
                            <xsl:otherwise>
                                <fo:table-cell border-width="0.5mm" role="TD">
                                    <xsl:copy-of select="$cellBlock"/>
                                </fo:table-cell>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                </fo:table-row>
            </xsl:if>
            <xsl:choose>
                <!-- Evaluate if next row is for nested elements -->
                <xsl:when test="$loopindex &lt; $maxLoopindex and $nestedIndex &lt; $maxLoopindex ">
                    <xsl:variable name="loopIdx">
                        <xsl:choose>
                            <xsl:when test="not($isNestedRow)">
                                <xsl:value-of select="(($index + 1)  * ($nestedListIndex)) + 1"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="$loopindex + 1"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:call-template name="addRow">
                        <xsl:with-param name="index" select="$index"/>
                        <xsl:with-param name="max" select="$max"/>
                        <xsl:with-param name="loopindex" select="$loopIdx"/>
                        <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
                        <xsl:with-param name="columns" select="$columns"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="itemIndex" select="$index + 1"/>
                    <xsl:call-template name="addRow">
                        <xsl:with-param name="index" select="$itemIndex"/>
                        <xsl:with-param name="max" select="$max"/>
                        <!-- loopindex and item-index have the same value for non-nested lists -->
                        <xsl:with-param name="loopindex" select="$itemIndex"/>
                        <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index=$itemIndex and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index=$itemIndex]/@loopindex)][1]/@loopindex"/>
                        <xsl:with-param name="columns" select="$columns"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <xsl:template name="addRows-relationship">
        <xsl:param name="columns"/>
        <xsl:param name="internal-id"/>
        <xsl:param name="max"/>
        <xsl:param name="maxLoopindex"/>
        <!-- No information entered : max won't have a numerical value or it is 0 -->
        <xsl:choose>
            <xsl:when test="not($max &lt;= 0 or $max &gt;= 0) or $max = 0">
                <fo:table-row>
                    <fo:table-cell role="TD">
                        <fo:block xsl:use-attribute-sets="questionValueAttSet">
                            <xsl:text>--</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="addRow-relationship">
                    <xsl:with-param name="columns" select="$columns"/>
                    <xsl:with-param name="internal-id" select="$internal-id"/>
                    <xsl:with-param name="index" select="0"/>
                    <xsl:with-param name="loopindex" select="10000"/>
                    <xsl:with-param name="max" select="$max"/>
                    <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!--
       - columns (Elements): Columns of each row (optional).
       - index   (integer):  Curent index of the relationships/relationship.
       - internal-id (long): Internal id of the relationship list.
       - max (integer):  Maximum number of rows relationships/relationship.
       - loopindex (integer): Loopindex of the row, when adding a row of a nested list loopindex is bigger than 10000.
       - maxLoopindex (integer): Maximum loopindex of the answers related to the list, (optional).
  -->
    <xsl:template name="addRow-relationship">
        <xsl:param name="columns"/>
        <xsl:param name="internal-id"/>
        <xsl:param name="index"/>
        <xsl:param name="loopindex"/>
        <xsl:param name="max"/>
        <xsl:param name="maxLoopindex"/>
        <xsl:variable name="nestedListIndex" select="10000"/>
        <xsl:variable name="relationshipindex" select="(1 + $loopindex - ($nestedListIndex * ($index + 1)))"/>
        <!-- When dynamic columns are not present, ignore maxloopindex as there will not be any reponse/answer related to the relationships -->
        <xsl:variable name="dynamicColumns" select="$columns[not(@id='caretakerInd')]"/>
        <xsl:variable name="relationships" select="//relationship-summary-list-data/relationships-list/relationships[@loopindex=$index]"/>
        <xsl:variable name="relationship" select="$relationships/relationship[$relationshipindex]"/>
        <xsl:variable name="maxCount" select="count($relationships/relationship)"/>
        <xsl:if test="$index &lt; $max and ($loopindex &lt;= $maxLoopindex or not($dynamicColumns)) ">
            <fo:table-row height="36px">
                <!-- Static Columns -->
                <!-- From -->
                <fo:table-cell padding-right="5px" role="TD">
                    <fo:block>
                        <fo:inline-container inline-progression-dimension="39.9%">
                            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                <xsl:call-template name="textWrapper">
                                    <xsl:with-param name="wrappedText" select="//relationship-summary-list-data/columns/column[contains(@id,'from')]/title"/>
                                </xsl:call-template>
                            </fo:block>
                            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                <xsl:choose>
                                    <xsl:when test="not($relationships/@person-name)">
                                        <xsl:text>--</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <!-- Use wrappable value -->
                                        <xsl:call-template name="textWrapper">
                                            <xsl:with-param name="wrappedText" select="$relationships/@person-name"/>
                                        </xsl:call-template>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:inline-container>
                        <fo:inline-container inline-progression-dimension="59.9%">
                            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                <xsl:text> &#160; </xsl:text>
                            </fo:block>
                            <xsl:choose>
                                <xsl:when test="//summary-page//relationship-summary-list/column[contains(@id,'caretakerInd')] and $relationship/@caretaker-ind='true'">
                                    <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                        <fo:inline background-color="{$bg-color-badge}" color="#000000">
                                            <xsl:value-of select="//summary-page//relationship-summary-list/column[contains(@id,'caretakerInd')]/title"/>
                                        </fo:inline>
                                    </fo:block>
                                </xsl:when>
                                <xsl:otherwise>
                                    <fo:block xsl:use-attribute-sets="questionValueAttSet">
                                        <xsl:text> &#160; </xsl:text>
                                    </fo:block>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:inline-container>
                    </fo:block>
                </fo:table-cell>
                <!-- Type , To  -->
                <xsl:call-template name="cell2Blocks">
                    <xsl:with-param name="label" select="$relationship/@type"/>
                    <xsl:with-param name="value" select="$relationship/@person-name"/>
                </xsl:call-template>
            </fo:table-row>
            <!-- For each dynamic column -->
            <xsl:if test="$dynamicColumns">
                <xsl:apply-templates select="$dynamicColumns[position() mod 2 != 0]">
                    <xsl:with-param name="internal-id" select="$internal-id"/>
                    <xsl:with-param name="index" select="$index"/>
                    <xsl:with-param name="loopindex" select="$loopindex"/>
                </xsl:apply-templates>
            </xsl:if>
            <fo:table-row border-top-style="solid" border-top-color="{$bg-color-main}" border-top-width="medium" height="5px">
                <fo:table-cell>
                    <fo:block/>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block/>
                </fo:table-cell>
            </fo:table-row>
            <xsl:choose>
                <!-- Evaluate if next row is for nested elements -->
                <xsl:when test="$relationshipindex &lt; $maxCount">
                    <xsl:call-template name="addRow-relationship">
                        <xsl:with-param name="columns" select="$columns"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                        <xsl:with-param name="index" select="$index"/>
                        <xsl:with-param name="loopindex" select="$loopindex + 1"/>
                        <xsl:with-param name="max" select="$max"/>
                        <xsl:with-param name="maxLoopindex" select="$maxLoopindex"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="itemIndex" select="$index + 1"/>
                    <xsl:call-template name="addRow-relationship">
                        <xsl:with-param name="columns" select="$columns"/>
                        <xsl:with-param name="internal-id" select="$internal-id"/>
                        <xsl:with-param name="index" select="$itemIndex"/>
                        <xsl:with-param name="loopindex" select="($itemIndex + 1)  * ($nestedListIndex)"/>
                        <xsl:with-param name="max" select="$max"/>
                        <xsl:with-param name="maxLoopindex" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and @item-index=$itemIndex and not(@loopindex &lt; ../answer[@internal-id=$internal-id and @item-index=$itemIndex]/@loopindex)][1]/@loopindex"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>
    <xsl:template match="column" name="relationship">
        <xsl:param name="internal-id"/>
        <xsl:param name="index"/>
        <xsl:param name="loopindex"/>
        <xsl:variable name="id" select="@id"/>
        <xsl:variable name="value" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$id and @item-index=$index and @loopindex=$loopindex]/text()"/>
        <fo:table-row height="36px">
            <xsl:call-template name="cell2Blocks">
                <xsl:with-param name="label" select="./title"/>
                <xsl:with-param name="value" select="$value"/>
            </xsl:call-template>
            <xsl:if test="./following-sibling::column">
                <xsl:variable name="column-id" select="./following-sibling::column/@id"/>
                <xsl:variable name="nextValue" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and substring-after(@question-id,'.')=$column-id and @item-index=$index and @loopindex=$loopindex]/text()"/>
                <xsl:call-template name="cell2Blocks">
                    <xsl:with-param name="label" select="./following-sibling::column/title"/>
                    <xsl:with-param name="value" select="$nextValue"/>
                </xsl:call-template>
            </xsl:if>
        </fo:table-row>
    </xsl:template>
    <xsl:template match="question">
        <xsl:param name="internal-id"/>
        <xsl:call-template name="cell">
            <xsl:with-param name="internal-id" select="$internal-id"/>
            <xsl:with-param name="question" select="."/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="container">
        <xsl:param name="internal-id"/>
        <xsl:variable name="value">
            <xsl:call-template name="containerValues">
                <xsl:with-param name="internal-id" select="$internal-id"/>
                <xsl:with-param name="children" select="./child::*"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="cell2Blocks">
            <xsl:with-param name="label" select="./title"/>
            <xsl:with-param name="value" select="$value"/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="list-question">
        <xsl:param name="internal-id"/>
        <xsl:call-template name="cellLQ">
            <xsl:with-param name="internal-id" select="@internal-id"/>
            <xsl:with-param name="list-question" select="."/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="cellLQ">
        <xsl:param name="internal-id"/>
        <xsl:param name="list-question"/>
        <xsl:variable name="id" select="$list-question/@id"/>
        <xsl:variable name="answers" select="/DOCUMENT/DATA//list-question[@internal-id=$internal-id]/item[@item-value='true']"/>
        <xsl:variable name="value">
            <xsl:call-template name="answerValuesLQ">
                <xsl:with-param name="answers" select="$answers"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="cell2Blocks">
            <xsl:with-param name="label" select="$list-question/label[text()]"/>
            <xsl:with-param name="value" select="$value"/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="answerValuesLQ">
        <xsl:param name="answers"/>
        <xsl:for-each select="$answers">
            <xsl:variable name="childCount" select="count($answers)"/>
            <xsl:variable name="position" select="position()"/>
            <xsl:for-each select=".">
                <xsl:value-of select="@item-label"/>
                <xsl:if test="not($position=$childCount)">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <xsl:template name="cell2Blocks">
        <xsl:param name="label"/>
        <xsl:param name="value"/>
        <fo:table-cell padding-right="5px">
            <fo:block xsl:use-attribute-sets="questionLabelAttSet">
                <fo:inline font-weight="bold">
                    <xsl:value-of select="$label"/>
                </fo:inline>
                <!--xsl:call-template name="textWrapper">
                    <xsl:with-param name="wrappedText" select="$label"/>
                </xsl:call-template -->
            </fo:block>
            <fo:block xsl:use-attribute-sets="questionValueAttSet">
                <xsl:choose>
                    <xsl:when test="not($value) or $value=' '">
                        <xsl:text>--</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:inline>
                            <xsl:value-of select="$value"/>
                        </fo:inline>
                        <!-- Use wrappable value -->
                        <!--xsl:call-template name="textWrapper">
                            <xsl:with-param name="wrappedText" select="$value"/>
                        </xsl:call-template -->
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
        </fo:table-cell>
    </xsl:template>
    <xsl:template name="cell">
        <xsl:param name="question"/>
        <xsl:param name="internal-id"/>
        <xsl:variable name="id" select="$question/@id"/>
        <xsl:variable name="answers" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and (substring-after(@question-id,'.')=$id or @question-id=$id)]"/>
        <xsl:variable name="value">
            <xsl:call-template name="answerValues">
                <xsl:with-param name="answers" select="$answers"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="cell2Blocks">
            <xsl:with-param name="label" select="$question/label[text()]"/>
            <xsl:with-param name="value" select="$value"/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template name="answerValues">
        <xsl:param name="answers"/>
        <xsl:for-each select="$answers">
            <xsl:variable name="childCount" select="count($answers)"/>
            <xsl:variable name="position" select="position()"/>
            <xsl:for-each select=".">
                <xsl:value-of select="./text()"/>
                <xsl:if test="not($position=$childCount)">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <!--
    Concatenates all values of a container, questions and dividers. 
    If there is no divider between questions an empty space is added.
  -->
    <xsl:template name="containerValues">
        <xsl:param name="internal-id"/>
        <xsl:param name="children"/>
        <xsl:for-each select="$children">
            <xsl:for-each select=".">
                <xsl:if test="local-name(.)='divider'">
                    <xsl:value-of select="./text()"/>
                </xsl:if>
                <xsl:if test="local-name(.)='question'">
                    <xsl:variable name="id" select="./@id"/>
                    <xsl:variable name="answer" select="/DOCUMENT/DATA//answer[@internal-id=$internal-id and (substring-after(@question-id,'.')=$id or @question-id=$id)]"/>
                    <xsl:value-of select="$answer/text()"/>
                    <xsl:if test="not(local-name(./following-sibling::*)='divider') and (local-name(./following-sibling::*)='question')">
                        <xsl:text> </xsl:text>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
    <!-- ************************************************************-->
    <!-- * END  - SUMMARY-PDF  Template                             *-->
    <!-- ************************************************************-->
</xsl:stylesheet>

