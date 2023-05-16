<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2008, 2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- This page contains the assessment action details like make decision,   -->
<!-- clone assessment etc.                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <CLUSTER SHOW_LABELS="false">
    <CLUSTER SHOW_LABELS="false">
      <CONDITION>
        <IS_FALSE
          NAME="COMMON_VIEW"
          PROPERTY="result$determinationConfigDtls$dtls$multipleSourceInd"
        />
      </CONDITION>
      <CLUSTER
        NUM_COLS="3"
        SHOW_LABELS="false"
        TITLE="Cluster.Title.Options"
      >
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistMakeDecision"
            LABEL="ActionControl.Label.MakeDecision"
          >
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_makeDecisionForAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.MakeDecision">
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_makeDecisionForAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistCloneAssessment"
            LABEL="ActionControl.Label.CloneAssessment"
          >
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cloneAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CloneAssessment">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cloneAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistComplete"
            LABEL="ActionControl.Label.CompleteAssessment"
          >
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_completeAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CompleteAssessment">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_completeAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL LABEL="ActionControl.Label.Blank"/>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistCancel"
            LABEL="ActionControl.Label.CancelAssessment"
          >
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cancelAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CancelAssessment">
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cancelAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
      </CLUSTER>
    </CLUSTER>
    <CLUSTER SHOW_LABELS="false">
      <CONDITION>
        <IS_TRUE
          NAME="COMMON_VIEW"
          PROPERTY="result$determinationConfigDtls$dtls$multipleSourceInd"
        />
      </CONDITION>
      <CLUSTER
        NUM_COLS="3"
        SHOW_LABELS="false"
        TITLE="Cluster.Title.Options"
      >
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistMakeDecision"
            LABEL="ActionControl.Label.MakeDecision"
          >
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_makeDecisionForAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.MakeDecision">
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_makeDecisionForAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistReqThirdPartyAnswer"
            LABEL="ActionControl.Label.RequestThirdPartyAnswers"
          >
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_createThirdParty"
            >
              <CONNECT>
                <SOURCE
                  NAME="COMMON_VIEW"
                  PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="determinationDeliveryID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.RequestThirdPartyAnswers">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_createThirdParty"
            >
              <CONNECT>
                <SOURCE
                  NAME="COMMON_VIEW"
                  PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="determinationDeliveryID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistComplete"
            LABEL="ActionControl.Label.CompleteAssessment"
          >
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_completeAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CompleteAssessment">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_completeAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistCloneAssessment"
            LABEL="ActionControl.Label.CloneAssessment"
          >
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cloneAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CloneAssessment">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cloneAssessment"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
        <CONTAINER>
          <ACTION_CONTROL
            IMAGE="DecisionAssistCancel"
            LABEL="ActionControl.Label.CancelAssessment"
          >
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cancelAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
          <ACTION_CONTROL LABEL="ActionControl.Label.CancelAssessment">
            <!-- BEGIN, CR00187459, SS -->
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="DecisionAssistApplication_cancelAssessment"
              WINDOW_OPTIONS="width=500"
            >
              <!-- END, CR00187459 -->
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="caseID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="assessmentCaseID"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </CONTAINER>
      </CLUSTER>
    </CLUSTER>
  </CLUSTER>
</VIEW>
