<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2006, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to display a list of events for a product        -->
<!-- delivery on an integrated case.                                        -->
<?curam-deprecated Since Curam 6.0, replaced with ICProduct_listEventView1.vim.
  This page does not return the indicators used to display the approriate list
  row actions. ?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <!-- BEGIN, CR00236544, AK -->
  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listProductDeliveryEvent"
  />


  <MENU>
    <ACTION_CONTROL LABEL="ActionControl.Label.NewTask">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_createTask"
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
  </MENU>


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00050298, MR -->
  <!-- BEGIN, HARP 64908, SP -->
  <LIST>
    <!-- END, HARP 64908 -->
    <!-- END, CR00050298 -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_resolveEventView">
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$recurringInd"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="recurringInd"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$attendeeInd"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="attendeeInd"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$acceptanceInd"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="acceptableInd"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$relatedID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="eventID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$typeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="eventType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK PAGE_ID="Case_resolveEventModify">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$relatedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="eventID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$typeCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="eventType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$recurringInd"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="recurringInd"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$attendeeInd"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attendeeInd"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$acceptanceInd"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="acceptableInd"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Event"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$dtls$typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$dtls$startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$dtls$endDate"
        />
      </CONNECT>
    </FIELD>


  </LIST>
  <!-- END, CR00236544 -->


</VIEW>
