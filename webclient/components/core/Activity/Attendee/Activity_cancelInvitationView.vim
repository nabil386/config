<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  WINDOW_OPTIONS="width=350"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.CancelInvitation"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Activity"
    NAME="ACTION"
    OPERATION="cancelInvitation"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="activityAttendeeID"/>
  <PAGE_PARAMETER NAME="attendeeName"/>
  <PAGE_PARAMETER NAME="activityAttendeeType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="activityID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityAttendeeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="activityAttendeeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityAttendeeType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="activityAttendeeType"
    />
  </CONNECT>


</VIEW>