<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>     
    <LIST TITLE="List.Title.InprogressList" DESCRIPTION="List.Title.InprogressList.Description">
        <FIELD LABEL="Field.Title.Application" WIDTH="25">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="inprogresAppDtls$applicationName" />
            </CONNECT>
            <LINK OPEN_NEW="true" URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="inprogresAppDtls$applicationUrl" />
        </FIELD>
        <FIELD LABEL="Field.Title.Programs" WIDTH="25">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="programListXML" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.DateCreated" WIDTH="20">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="createdDate" />
            </CONNECT>
        </FIELD>
        <CONTAINER WIDTH="30">
            <FIELD>
                <CONNECT>
                    <SOURCE NAME="DISPLAY" PROPERTY="resumeApplicationUrl" />
                </CONNECT>
            </FIELD>
            <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
                <LINK URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="discardPageURI" OPEN_MODAL="true" WINDOW_OPTIONS="width=400,height=200">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="discardObjectID" />
                        <TARGET NAME="PAGE" PROPERTY="discardObjectID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="inprogresAppDtls$applicationName" />
                        <TARGET NAME="PAGE" PROPERTY="applicationName" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="CONSTANT" PROPERTY="External.App.Context" />
                        <TARGET NAME="PAGE" PROPERTY="o3ctx" />
                    </CONNECT>                    
                </LINK>                
            </ACTION_CONTROL>
        </CONTAINER>
    </LIST>
    <LIST TITLE="List.Title.SubmittedList" DESCRIPTION="List.Title.SubmittedList.Description">
        <CONTAINER LABEL="Field.Title.Application" WIDTH="25">   	  
    	    <ACTION_CONTROL LABEL="ActionControl.Label.TextTranslation" TYPE="FILE_DOWNLOAD" IMAGE="PDF.Icon">
				<LINK>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="intakeApplicationID"/>
						<TARGET NAME="PAGE" PROPERTY="intakeApplicationID"/>
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
            <WIDGET TYPE="FILE_DOWNLOAD">
		        <WIDGET_PARAMETER NAME="LINK_TEXT">
		          <CONNECT>
		            <SOURCE NAME="DISPLAY" PROPERTY="submittedAppDtls$applicationName" />
		          </CONNECT>
		        </WIDGET_PARAMETER>
		        <WIDGET_PARAMETER NAME="PARAMS">
		          <CONNECT>
		            <SOURCE NAME="DISPLAY" PROPERTY="intakeApplicationID"/>
		            <TARGET NAME="PAGE" PROPERTY="intakeApplicationID"/>
		          </CONNECT>
		        </WIDGET_PARAMETER>
		    </WIDGET>	    
		</CONTAINER>
        <FIELD LABEL="Field.Title.Programs" WIDTH="23">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="programName" />
            </CONNECT>
            <LINK OPEN_NEW="true" URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="programUrl" />
        </FIELD>
        <FIELD LABEL="Field.Title.DateSubmitted" WIDTH="21">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="submittedDateTime" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ProgramStatus" WIDTH="15">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="programStatus" />
            </CONNECT>
        </FIELD>
        <CONTAINER SEPARATOR="Container.Separator" WIDTH="16">
            <ACTION_CONTROL LABEL="ActionControl.Label.Withdraw">
                <CONDITION>
                    <IS_TRUE NAME="DISPLAY" PROPERTY="withdrawInd" />
                </CONDITION>
                <LINK PAGE_ID="CitizenWorkspace_withdrawIntakeApplication" OPEN_MODAL="true" WINDOW_OPTIONS="width=700,height=570">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="intakeProgramApplicationID" />
                        <TARGET NAME="PAGE" PROPERTY="intakeProgramApplicationID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="programName" />
                        <TARGET NAME="PAGE" PROPERTY="programName" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Other">
                <CONDITION>
                    <IS_TRUE NAME="DISPLAY" PROPERTY="displayOtherActionInd" />
                </CONDITION>
                <LINK URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="otherActionURI"/>
            </ACTION_CONTROL>            
        </CONTAINER>
    </LIST>
</VIEW>