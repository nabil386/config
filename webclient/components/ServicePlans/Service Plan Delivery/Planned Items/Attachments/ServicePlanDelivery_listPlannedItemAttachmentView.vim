<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is the included view used to display a list of attachments for a  -->
<!-- Planned Item.                                                          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!--BEGIN CR00120067, GBA-->
  <!-- Page level information -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <!-- Page Parameters used in this page -->
  <PAGE_PARAMETER NAME="plannedItemID"/>
  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="plannedItemAttachmentLinkID"/>
  <!--END CR00120067-->


  <!-- Display Phase server access beans used in this page -->
  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="listAttachments"
    PHASE="DISPLAY"
  />


  <!-- Page Level Connect statements used in this page -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="plannedItemKey$plannedItemID"
    />
  </CONNECT>


  <!-- Action Elements used in this page -->
  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ServicePlanDelivery_createPlannedItemAttachment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <!-- Fields used in this page to list attachments -->
  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_modifyPlannedItemAttachmentFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="plannedItemAttachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemAttachmentLinkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_cancelPlannedItemAttachment"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="plannedItemAttachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemAttachmentLinkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Description"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptDate"
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
          PROPERTY="attachmentStatus"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ServicePlanDelivery_viewAttachmentForPlannedItem">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="attachmentID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="plannedItemAttachmentLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemAttachmentLinkID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
