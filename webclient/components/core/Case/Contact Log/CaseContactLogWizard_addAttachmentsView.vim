<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011,2020. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- This page allows the user to create a contact log for a case.          -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <CLUSTER LABEL_WIDTH="14">
    
    <WIDGET
      LABEL="Field.Label.File"
      TYPE="FILE_UPLOAD"
    >
      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="fileContents"
          />
        </CONNECT>
      </WIDGET_PARAMETER>


      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="fileName"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
    </WIDGET>
  </CLUSTER>

  <CLUSTER
    BEHAVIOR="NONE"
    LABEL_WIDTH="28"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.FileLocation"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.FileType"
      USE_BLANK="true"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.FileReference"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00311583, VT -->
    <FIELD
      LABEL="Field.Label.ReceiptDate"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00311583 -->
  </CLUSTER>


  <CLUSTER LABEL_WIDTH="14">
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Description"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileDescription"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
