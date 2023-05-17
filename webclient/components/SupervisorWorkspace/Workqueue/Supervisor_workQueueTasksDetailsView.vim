<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for work queue assigned tasks.                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorWorkQueues"
    NAME="DISPLAYWQ"
    OPERATION="readWorkQueueWorkspaceDtls"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorWorkQueues"
    NAME="ACTION"
    OPERATION="readWorkQueueWorkspaceDtls"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="workQueueID"
    />
    <TARGET
      NAME="DISPLAYWQ"
      PROPERTY="key$workQueueID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskOptionCode"
    />
    <TARGET
      NAME="DISPLAYWQ"
      PROPERTY="key$taskOption"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="workQueueID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$workQueueID"
    />
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.SubscribeNewUser">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="WorkAllocation_createWorkQueueSubscription"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.TasksDueNextWeek"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="Supervisor_resolveWQWorkspace">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.TasksDueNextMonth"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="Supervisor_resolveWQWorkspace">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="workQueueID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption2"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <LIST TITLE="List.Title.Subscribers">
      <ACTION_SET TYPE="LIST_ROW_MENU">


        <ACTION_CONTROL LABEL="ActionControl.Label.Unsubscribe">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="Supervisor_unsubscribeOrgObjectFromWorkQueue"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="result$details$subscriptionDetailsList$dtlsList$userName"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="userName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="result$details$workQueueDetails$workQueueID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="workQueueID"
              />
            </CONNECT>
            <!-- BEGIN, CR00161962, NR -->
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="dtlsList$subscriberType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="subscriberType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="result$details$subscriptionDetailsList$dtlsList$userFullName"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="userFullName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="name"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="workQueueName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="dtlsList$unsubscribePageText"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="unsubscribePageText"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="dtlsList$pageTitle"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageTitle"
              />
            </CONNECT>
            <!-- END, CR00161962 -->
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>
      <FIELD
        LABEL="Field.Label.Name"
        WIDTH="30"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="result$details$subscriptionDetailsList$dtlsList$userFullName"
          />
        </CONNECT>
        <LINK PAGE_ID="Supervisor_userWorkspace">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYWQ"
              PROPERTY="result$details$subscriptionDetailsList$dtlsList$userName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="supervisor.viewTaskOption"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taskOptionCode"
            />
          </CONNECT>
        </LINK>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Email"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="result$details$subscriptionDetailsList$dtlsList$userEmailID"
          />
        </CONNECT>
        <LINK
          URI_SOURCE_NAME="DISPLAYWQ"
          URI_SOURCE_PROPERTY="result$details$subscriptionDetailsList$dtlsList$userEmailLink"
        />
      </FIELD>
      <FIELD
        LABEL="Field.Label.PhoneNumber"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="result$details$subscriptionDetailsList$dtlsList$userPhoneNumber"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00161962, BK -->
      <FIELD
        LABEL="Field.Label.SubscriptionDate"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="result$details$subscriptionDetailsList$dtlsList$subscriptionDateTime"
          />
        </CONNECT>
      </FIELD>
    </LIST>


    <!-- BEGIN - Cluster to display list of org object subscribers of the workqueue  -->
    <LIST TITLE="List.Title.OrgObjectSubscribers">
      <ACTION_SET TYPE="LIST_ROW_MENU">


        <ACTION_CONTROL LABEL="ActionControl.Label.Unsubscribe">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="Supervisor_unsubscribeOrgObjectFromWorkQueue"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="orgDtlsList$subscriberID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="subscriberID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="orgDtlsList$subscriberType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="subscriberType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="result$details$workQueueDetails$workQueueID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="workQueueID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="name"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="workQueueName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="orgDtlsList$unsubscribePageText"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="unsubscribePageText"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYWQ"
                PROPERTY="orgDtlsList$pageTitle"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageTitle"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>


      <FIELD
        LABEL="Field.Label.Name"
        WIDTH="55"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="orgFullName"
          />
        </CONNECT>
        <LINK PAGE_ID="Supervisor_resolveOrgObjectHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYWQ"
              PROPERTY="orgDtlsList$subscriberID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="subscriberID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYWQ"
              PROPERTY="orgDtlsList$organisationStructureID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="orgStructureID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYWQ"
              PROPERTY="orgDtlsList$organisationUnitID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="organisationUnitID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYWQ"
              PROPERTY="orgDtlsList$objectType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="objectType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="supervisor.viewTaskOption"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="taskOptionCode"
            />
          </CONNECT>
        </LINK>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Type"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="orgDtlsList$subscriberType"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.SubscriptionDate"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWQ"
            PROPERTY="orgDtlsList$subscriptionDateTime"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>


</VIEW>
