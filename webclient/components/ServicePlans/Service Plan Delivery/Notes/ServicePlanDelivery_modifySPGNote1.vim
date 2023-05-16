<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2023. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify a service plan group delivery note.  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="SPGDeliveryNote"
    NAME="DISPLAY"
    OPERATION="viewNote"
  />
  <SERVER_INTERFACE
    CLASS="SPGDeliveryNote"
    NAME="ACTION"
    OPERATION="modifyNote"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="spgDeliveryNoteLinkId"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$spgDeliveryNoteLinkId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="servicePlanGroupDeliveryId"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$servicePlanGroupDeliveryId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="key$spgDeliveryNoteLinkId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="spgDeliveryNoteLinkId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="noteID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="noteID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="key$servicePlanGroupDeliveryId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="servicePlanGroupDeliveryId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="status"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="noteEditableInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="editLatestNoteTextIndOpt"
    />
  </CONNECT>
  
  <!-- A message is displayed if the note is temporarily locked against editing. -->
  <CLUSTER>
   <CONDITION>
      <IS_TRUE NAME="DISPLAY" PROPERTY="noteLockedInd" />
    </CONDITION>
    <CONTAINER>
      <IMAGE IMAGE="LockedIcon" LABEL="Icon.Locked"/>
      <FIELD>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="editableNoteMessageOpt" />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>
  
  <!-- All the regular fields are only displayed if the note is not temporarily locked against editing -->
  <CLUSTER>
    <CONDITION>
      <IS_FALSE NAME="DISPLAY" PROPERTY="noteLockedInd" />
    </CONDITION>
    
    <CLUSTER STYLE="cluster-cpr-no-internal-padding">
    
    <CLUSTER
      LABEL_WIDTH="17.5"
      STYLE="cluster-cpr-no-border"
      >
      <FIELD LABEL="Field.Label.Subject">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectText"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="subjectText"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>

  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >

    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priorityCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sensitivityCode"
        />
      </CONNECT>
    </FIELD>


   </CLUSTER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="true"
    STYLE="cluster-rte-no-padding"
  >
  
    <!-- conditional cluster that shows an icon and message if the note is editable -->
    <CLUSTER>
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="noteEditableInd"/>
      </CONDITION>
      <CONTAINER>
              <IMAGE IMAGE="UnlockedIcon" LABEL="Icon.Editable"/>
              <FIELD>
                    <CONNECT>
                      <SOURCE NAME="DISPLAY" PROPERTY="editableNoteMessageOpt" />
                    </CONNECT>
              </FIELD>
            </CONTAINER>
    </CLUSTER>
    
    <FIELD HEIGHT="200" >
      <LABEL>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="noteTextFieldLabelOpt" />
        </CONNECT>
      </LABEL>
      <CONNECT>
        <SOURCE
            NAME="DISPLAY"
            PROPERTY="latestNoteTextForEdit"
          />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  <!-- note history -->
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="RemoveFieldPadding"
  >
        <FIELD
          HEIGHT="4"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="noteHistoryWithoutUserLinksOpt"
            />
          </CONNECT>
        </FIELD>
  </CLUSTER> 
  
  </CLUSTER>


</VIEW>
