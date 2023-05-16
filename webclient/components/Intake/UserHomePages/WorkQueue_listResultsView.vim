<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST>


    <TITLE>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="List.Title.Results"
        />
      </CONNECT>
    </TITLE>


    <FIELD LABEL="Field.Label.TaskID">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$taskID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ClientName">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$daysForDeadline"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Reference">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$taskID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Subject"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$priority"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProcessingDeadline">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$daysForDeadline"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
