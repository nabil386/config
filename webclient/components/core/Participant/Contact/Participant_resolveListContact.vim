<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2008, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view an activity from the Application     -->
<!-- home page.                                                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <JSP_SCRIPTLET>
        
        curam.omega3.request.RequestHandler 
        rh = curam.omega3.request.RequestHandlerFactory.getRequestHandler(request);
        
        String context = request.getContextPath() + "/";
        context += curam.omega3.user.UserPreferencesFactory.getUserPreferences(pageContext.getSession()).getLocale() + "/"; 
        String concernRoleID = request.getParameter("concernRoleID");

        String concernRoleType = "";
        String url = "";
        //BEGIN, CR00280979, PB
        String resolvePage ="Participant_listCompanyContact";
        //END, CR00280979
        if (concernRoleID != null){        
        
        //creating the Participant text helper to read the concernRoleType 
        curam.interfaces.ParticipantPkg.Participant_readConcernRoleType_TH 
        th = new curam.interfaces.ParticipantPkg.Participant_readConcernRoleType_TH();
        
        //Setting concernRoleID as Key                             
        th.setFieldValue(th.key$readConcernRoleKey$concernRoleID_idx,concernRoleID);
        th.callServer();
               
        concernRoleType=th.getFieldValue(th.result$concernRoleType_idx);       
        }
        
        //Get the description here and pass as page parameter, because the pages are expecting it to be passed.
        //BEGIN, CR00280979, PB
        if (concernRoleType.equals("RL1")
        ||concernRoleType.equals("RL7")) {        
        resolvePage = "Participant_listContact";
        }
        //END, CR00280979
        // BEGIN, CR00372377, AC
        url= context+resolvePage+"Page.do?concernRoleID="+curam.omega3.request.RequestUtils.escapeURL(concernRoleID);
        url += "&amp;&amp;" + rh.getSystemParameters();
       // END, CR00372377 
        response.sendRedirect(response.encodeRedirectURL(url));
        
        
    </JSP_SCRIPTLET>
</VIEW>
