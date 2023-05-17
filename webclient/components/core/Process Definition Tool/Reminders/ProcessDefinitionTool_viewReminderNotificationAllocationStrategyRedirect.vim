<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- ================                                                       -->
<!-- This jsp redirects the user to the appropriate allocation strategy     -->
<!-- page for a reminder notification based on the specified allocation     -->
<!-- type and activity type. The types include function, rule or direct.    -->
<VIEW
  PAGE_ID="ProcessDefinitionTool_viewReminderNotificationAllocationStrategyRedirect"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <JSP_SCRIPTLET>
    
     
      String processID = request.getParameter("processID");
      String processVersionNo = request.getParameter("processVersionNo");
      String activityID = request.getParameter("activityID");
      String activityType = request.getParameter("activityType");
      String reminderID = request.getParameter("reminderID");
      String strategyParent = "REMINDER_ALLOCATION_STRATEGY";
      
      curam.omega3.request.RequestHandler 
        rh = curam.omega3.request.
          RequestHandlerFactory.getRequestHandler(request);

      String context = request.getContextPath() + "/";
      context += curam.omega3.user.UserPreferencesFactory
        .getUserPreferences(pageContext.getSession()).getLocale() + "/";
       
      String pageID = null;
      String url = null;
          
      curam.interfaces.AllocationStrategyAdminPkg.
        AllocationStrategyAdmin_readAllocationStrategy_TH allocationStrategy
          = new curam.interfaces.AllocationStrategyAdminPkg.
              AllocationStrategyAdmin_readAllocationStrategy_TH();
     
      allocationStrategy.setFieldValue(
        allocationStrategy.allocationStrategyReadKey$processID_idx, processID);
      allocationStrategy.setFieldValue(
        allocationStrategy.allocationStrategyReadKey$processVersionNo_idx, processVersionNo);
      allocationStrategy.setFieldValue(
        allocationStrategy.allocationStrategyReadKey$activityID_idx, activityID);
      allocationStrategy.setFieldValue(
        allocationStrategy.allocationStrategyReadKey$strategyParent_idx, strategyParent);  
         allocationStrategy.setFieldValue(
        allocationStrategy.allocationStrategyReadKey$reminderID_idx, reminderID);       
      // Call the method.
      allocationStrategy.callServer();

      String allocationType = allocationStrategy.getFieldValue(
        allocationStrategy.result$allocationStrategyType_idx);
        
      curam.interfaces.ParallelActivityAdminPkg.
        ParallelActivityAdmin_readParallelActivityTypeDetails_TH parallelActivityAdmin
          = new curam.interfaces.ParallelActivityAdminPkg.
            ParallelActivityAdmin_readParallelActivityTypeDetails_TH();
     
      parallelActivityAdmin.setFieldValue(
        parallelActivityAdmin.activityKey$processID_idx, processID);
      parallelActivityAdmin.setFieldValue(
        parallelActivityAdmin.activityKey$processVersionNo_idx, 
          processVersionNo);
      parallelActivityAdmin.setFieldValue(
        parallelActivityAdmin.activityKey$activityID_idx, 
          activityID);          
      // Call the method.
      parallelActivityAdmin.callServer();

      String parallelActivityType = parallelActivityAdmin.getFieldValue(
        parallelActivityAdmin.result$parallelActivityType_idx);        
        
      String activity = "";
      
       if (activityType.equals("AT1")) {
         activity = "ManualActivity";
       } else if (activityType.equals("AT6")) {
       activity = "EventWaitActivity";
       } else if (activityType.equals("AT10")) {
       activity = "DecisionActivity";
       } else if (activityType.equals("AT12")) {
         if (parallelActivityType.equals("PAT1")) {
           activity = "ParallelManualActivity";
         } else if (parallelActivityType.equals("PAT2")) {
           activity = "ParallelDecisionActivity";      
         }
       }
        
      if (allocationType.equals("AS1")) {     
       
        pageID = "ProcessDefinitionTool_view" + activity + "ReminderNotificationTargetAllocationStrategyPage.do";
      } else if (allocationType.equals("AS2")) {    
       
        pageID = "ProcessDefinitionTool_view" + activity + "ReminderNotificationFunctionAllocationStrategyPage.do";
      } else if (allocationType.equals("AS3")) {   
         
        pageID = "ProcessDefinitionTool_view" + activity + "ReminderNotificationRuleAllocationStrategyPage.do";
       } else if (allocationType.equals("AS4")) {      
        
        pageID =  "ProcessDefinitionTool_view" + activity + "ReminderNotificationCERSetAllocationStrategyPage.do"; 
      }else {
      
        pageID = "ProcessDefinitionTool_viewUnsetReminderAllocationStrategyPage.do";
      }

      url = context + pageID + "?processID=" + curam.omega3.request.RequestUtils.escapeURL(processID) 
        + "&amp;processVersionNo=" + curam.omega3.request.RequestUtils.escapeURL(processVersionNo)
        + "&amp;activityID=" + curam.omega3.request.RequestUtils.escapeURL(activityID)
        + "&amp;activityType=" + curam.omega3.request.RequestUtils.escapeURL(activityType)
        + "&amp;strategyParent=" + curam.omega3.request.RequestUtils.escapeURL(strategyParent)        
        + "&amp;reminderID=" + curam.omega3.request.RequestUtils.escapeURL(reminderID);
        
      url += "&amp;" + rh.getSystemParameters();  
      response.sendRedirect(response.encodeRedirectURL(url));
    
    
  </JSP_SCRIPTLET>
</VIEW>
