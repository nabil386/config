<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure view page to be included in the client page    -->
<!-- for listing In Edit case evidence                                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="successionID"/>


  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="listActiveEvdInstanceChanges"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="successionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$successionID"
    />
  </CONNECT>


  <LIST>
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="result$evdBrokerEnabled"
      />
    </CONDITION>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.ViewVerifications">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="verificationsExist"
          />
        </CONDITION>
        <LINK
          PAGE_ID="Evidence_resolveObjectVerificationsFromMetaData"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="successionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="successionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ViewIssues">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="issuesExist"
          />
        </CONDITION>
        <LINK
          PAGE_ID="Evidence_resolveObjectIssuesFromMetaData"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="successionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="successionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ContinueEdit">
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_resolveModifyEvidencePage"
          SAVE_LINK="TRUE"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Discard">
        <CONDITION>
         <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtlsList$editableInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Evidence_deleteInEdit"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.delete">
        <CONDITION>
           <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtlsList$activeInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_removeActive"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD WIDTH="5">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$verificationOrIssuesExist"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.ChangeSummary"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Source"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="source"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.UpdatedBy"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="latestActivity"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <LIST>
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="result$evdBrokerEnabled"
      />
    </CONDITION>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.ViewVerifications">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="verificationsExist"
          />
        </CONDITION>
        <LINK
          PAGE_ID="Evidence_resolveObjectVerificationsFromMetaData"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="successionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="successionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ViewIssues">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="issuesExist"
          />
        </CONDITION>
        <LINK
          PAGE_ID="Evidence_resolveObjectIssuesFromMetaData"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="successionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="successionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ContinueEdit">
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_resolveModifyEvidencePage"
          SAVE_LINK="TRUE"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
       <ACTION_CONTROL LABEL="ActionControl.Label.Discard">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtlsList$editableInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Evidence_deleteInEdit"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.delete">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="dtlsList$activeInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_removeActive"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtlsList$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD WIDTH="5">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$verificationOrIssuesExist"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.ChangeSummary"
      WIDTH="33"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="32"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.UpdatedBy"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="latestActivity"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
