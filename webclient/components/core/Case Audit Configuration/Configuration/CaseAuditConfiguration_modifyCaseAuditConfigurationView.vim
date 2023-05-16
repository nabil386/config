<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2009, 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify an existing case audit configuration.-->

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
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="CaseAuditConfiguration"
    NAME="DISPLAY"
    OPERATION="populateFocusAreasAndQueries"
  />


  <SERVER_INTERFACE
    CLASS="CaseAuditConfiguration"
    NAME="ACTION"
    OPERATION="modifyAuditCaseConfiguration"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseType"/>
  <PAGE_PARAMETER NAME="caseCategory"/>
  <PAGE_PARAMETER NAME="auditCaseConfigID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseType"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseCategory"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseCategory"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$configDtls$dtls$caseType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseCategory"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$configDtls$dtls$caseCategory"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$selectedFocusAreas"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$initialFocusAreas"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$selectedFixedQueries"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$initialFixedQueries"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="auditCaseConfigID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$configDtls$dtls$auditCaseConfigID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$dtls$dtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$configDtls$dtls$versionNo"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="1">


    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CaseCategory">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$selectedCategory"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Algorithm"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$dtls$auditAlgorithm"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$configDtls$dtls$auditAlgorithm"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="27.5"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.CaseAuditTypeSID"
        WIDTH="55"
      >
        <CONNECT>
          <INITIAL
            NAME="DISPLAY"
            PROPERTY="result$dtls$dtls$caseAuditTypeSID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$dtls$caseAuditTypeSID"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$configDtls$dtls$caseAuditTypeSID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>

    <!-- BEGIN, CR00332478, GA -->
    <CLUSTER STYLE="cluster-cpr-no-border" LABEL_WIDTH="50">
    <!-- END, CR00332478 -->
      <FIELD LABEL="Field.Label.AllowManSelectInd">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$dtls$manCaseSelectInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$configDtls$dtls$manCaseSelectInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <CLUSTER>
    <!-- BEGIN, CR00212784, GD -->
    <LIST
      PAGINATED="false"
      
      TITLE="List.DynamicQueries.Title"
    >
      <!-- END, CR00212784 -->


      <CONTAINER WIDTH="5">
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$availableDynamicQueries$dtls$queryID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="details$dynamicQueryID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_INITIAL">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$selectedDynamicQueryID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD LABEL="Field.Label.DynamicQuery">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$availableDynamicQueries$dtls$queryName"
          />
        </CONNECT>
      </FIELD>
    </LIST>


    <!-- BEGIN, CR00212784, GD -->
    <LIST
      PAGINATED="false"
      
      TITLE="List.FixedQueries.Title"
    >
      <!-- END, CR00212784 -->


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$displayFixedQueries"
        />
      </CONDITION>


      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="SelectionQuery_listCriteria">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$availableFixedQueries$dtls$queryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="selectionQueryID"
            />
          </CONNECT>
        </INLINE_PAGE>
      </DETAILS_ROW>


      <CONTAINER WIDTH="5">
        <WIDGET TYPE="MULTISELECT">
          <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$availableFixedQueries$dtls$queryID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="details$updatedFixedQueries"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="MULTI_SELECT_INITIAL">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$selectedFixedQueries"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>


      <FIELD LABEL="Field.Label.FixedQuery">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$availableFixedQueries$dtls$queryName"
          />
        </CONNECT>
      </FIELD>


    </LIST>
  </CLUSTER>


  <CLUSTER NUM_COLS="1">


    <CLUSTER
      LABEL_WIDTH="19.5"
      STYLE="cluster-cpr-no-border"
      TITLE="Cluster.Title.SelectFocusAreas"
    >


      <!-- BEGIN, CR00212784, GD -->
      <LIST
        PAGINATED="false"
        
      >
        <CONTAINER
          ALIGNMENT="CENTER"
          WIDTH="5"
        >
          <WIDGET TYPE="MULTISELECT">
            <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
              <CONNECT>
                <SOURCE
                  NAME="DISPLAY"
                  PROPERTY="focusAreaCode"
                />
              </CONNECT>
            </WIDGET_PARAMETER>


            <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
              <CONNECT>
                <TARGET
                  NAME="ACTION"
                  PROPERTY="details$updatedFocusAreas"
                />
              </CONNECT>
            </WIDGET_PARAMETER>


            <WIDGET_PARAMETER NAME="MULTI_SELECT_INITIAL">
              <CONNECT>
                <SOURCE
                  NAME="DISPLAY"
                  PROPERTY="result$selectedFocusAreas"
                />
              </CONNECT>
            </WIDGET_PARAMETER>


          </WIDGET>


        </CONTAINER>


        <FIELD LABEL="Field.Label.FocusArea">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="focusAreaCode"
            />
          </CONNECT>
        </FIELD>


      </LIST>
      <!-- END, CR00212784 -->


    </CLUSTER>


  </CLUSTER>


</VIEW>
