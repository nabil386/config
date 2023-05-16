<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright  2008, 2010 Curam Software Ltd.                              -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page allows users to select sharing source and target for         -->
<!-- evidence sharing.                                                      -->
<?curam-deprecated Since Curam 7.0.2.0, this functionality has been replaced with the Advanced Evidence Sharing component.?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00205401, GYH -->
  <SERVER_INTERFACE
    CLASS="EvidenceBrokerAdmin"
    NAME="DISPLAY"
    OPERATION="listEvidenceSharingConfigSourceAndTarget"
    PHASE="DISPLAY"
  />
  <!-- END, CR00205401 -->


  <SERVER_INTERFACE
    CLASS="EvidenceBrokerAdmin"
    NAME="ACTION"
    OPERATION="selectSourceAndTarget"
    PHASE="ACTION"
  />


  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="false"
  >
    <LIST TITLE="List.Label.Source">
      <CONTAINER
        LABEL="Container.Label.Action"
        WIDTH="14"
      >
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="sourceList$listDetails$sharingPartyID"
              />
            </CONNECT>
            <!-- BEGIN, CR00205401, GYH -->
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="targetList$listDetails$partyName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="targetList$listDetails$sharingPartyCaseType"
              />
            </CONNECT>
            <!-- END, CR00205401 -->
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="sourceParty$sharingPartyString"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD
        LABEL="Field.Label.Source"
        WIDTH="50"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sourceList$listDetails$partyName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.SourceType"
        WIDTH="36"
      >
        <!-- BEGIN, CR00205401, GYH -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sourceList$listDetails$caseTypeDescription"
          />
        </CONNECT>
        <!-- END, CR00205401 -->
      </FIELD>
    </LIST>


    <LIST TITLE="List.Label.Target">


      <CONTAINER
        LABEL="Container.Label.Action"
        WIDTH="14"
      >
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="targetList$listDetails$sharingPartyID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="targetList$listDetails$partyName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="targetList$listDetails$sharingPartyCaseType"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="targetParty$sharingPartyString"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="targetParty$sharingPartyString"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD
        LABEL="Field.Label.Target"
        WIDTH="50"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="targetList$listDetails$partyName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.TargetType"
        WIDTH="36"
      >
        <!-- BEGIN, CR00205401, GYH -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="targetList$listDetails$caseTypeDescription"
          />
        </CONNECT>
        <!-- END, CR00205401 -->
      </FIELD>
    </LIST>
  </CLUSTER>
</VIEW>
