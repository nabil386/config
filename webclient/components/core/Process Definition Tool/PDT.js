/*
 * Copyright 2006-2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

 /*
  * The identifier of the allocation strategy clear button on the edit 
  * allocation strategy page.
  */
 var allocationStrategyClearPopupID = "ACTION$allocationModifyDetails$allocationStrategyValue_clear";
 
 /**
  * Clears the allocation strategy value on the Modify Allocation strategy 
  * Page.
  */
 function clearAllocationStrategy() {
   var clearElement = document.getElementById(allocationStrategyClearPopupID);
   clearPopup(clearElement);
 }
 
 /**
  * Closes the dialog and refreshes the parent page. This is currently
  * used when a BPO method is being selected for use in the PDT and
  * the system needs to determine whether the method is overloaded or not.
  */
function closeDialogRefresh(event) {
  dojo.stopEvent(event);
  dojo.require('curam.util.UimDialog');
  curam.util.UimDialog.ready(function() {
    var dialogObject = curam.util.UimDialog.get();
    dialogObject.close(true);
  });

  return false;
} 