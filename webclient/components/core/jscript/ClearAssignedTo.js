/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2009,2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

 /*
  * The identifier of the "Assigned To" clear button on the Create Task Pages.
  */
 var assignedToClearPopupID = "ACTION$dtls$dtls$assignDtls$assignmentID_clear";
 var taskMgtClearAssignedToID = "ACTION$createTaskDetails$taskDetails$assignedTo_clear";
 
 /**
  * Clears the "Assigned To" value on the Create Task Pages. The method is used by
  * TaskManagement.create() and WorkAllocation.createManualTask(). Both of these methods
  * take a different struct, therefore the HTML element ID will be different depending
  * on which page is currently displayed in the browser.
  */
 function clearAssignedTo() {
   var clearElement = document.getElementById(assignedToClearPopupID);
   if (clearElement == null) {
     clearElement = document.getElementById(taskMgtClearAssignedToID);
   }
   if (clearElement != null) {
     clearPopup(clearElement);
   }
 }