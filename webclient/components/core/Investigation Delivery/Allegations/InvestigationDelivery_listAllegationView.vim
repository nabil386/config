<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page is used to display a list of allegations for an              -->
<!-- investigation case                                                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="listAllegation"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="investigationCaseID$caseID"
    />
  </CONNECT>
  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="InvestigationDelivery_createAllegation"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="InvestigationDelivery_modifyAllegationFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="allegationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="InvestigationDelivery_cancelAllegation"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="allegationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      
      <SEPARATOR/>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.AddFinding">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_createFinding"
          >
            <CONNECT>
              <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="allegationID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        
        <!--<ACTION_CONTROL LABEL="ActionControl.Label.ModifyFinding">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_resolveModifyFinding"
          >
            <CONNECT>
              <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="allegationID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="findingID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="findingID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>-->
        
        <!--<ACTION_CONTROL LABEL="ActionControl.Label.OverrideFinding">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_resolveOverrideFinding"
          >
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="allegationID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="findingID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="findingID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>-->
        
        <ACTION_CONTROL LABEL="ActionControl.Label.FindingHistory">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_listFindingHistory"
          >
            <CONNECT>
              <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="allegationID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        
       <SEPARATOR/>
       
        <ACTION_CONTROL LABEL="ActionControl.Label.AddParticipant">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_createAllegationRole"
          >
            <CONNECT>
              <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="allegationID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        
       <SEPARATOR/>        
                               
         <ACTION_CONTROL
          IMAGE="AddAttachmentButton"
          LABEL="ActionControl.Label.AddAttachment"
        >
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="InvestigationDelivery_createAllegationAttachment"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="allegationID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="description"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>     
      
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AllegationDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allegationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Location"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="InvestigationDelivery_viewAllegation">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="allegationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="allegationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
