<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2008, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- ====================================================================== -->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW
  POPUP_PAGE="true"
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
    CLASS="LocalizableText"
    NAME="DISPLAY"
    OPERATION="viewLocalizableText"
  />
  <PAGE_PARAMETER NAME="localizableTextID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="localizableTextID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$localizableTextID"
    />
  </CONNECT>
  <LIST TITLE="List.Title.Translations">


    <!-- BEGIN, CR00360494, PS -->
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="20"
    >
      <!-- END, CR00360494 -->


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="LocalizableText_resolveModifyTextTranslation"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="textTranslationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="textTranslationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="richTextInd"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="richTextInd"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="Field.Title.Language"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="localeCode"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00360494, PS -->
    <FIELD
      LABEL="Field.Title.Text"
      WIDTH="60"
    >
      <!-- END, CR00360494 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="richText"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
