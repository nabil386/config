<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
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
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="message"
      />
    </CONNECT>
  </INFORMATIONAL>
  <PAGE_PARAMETER NAME="processID"/>
  <PAGE_PARAMETER NAME="processVersionNo"/>
  <PAGE_PARAMETER NAME="processName"/>
  <PAGE_PARAMETER NAME="validateStructureOnly"/>
</VIEW>