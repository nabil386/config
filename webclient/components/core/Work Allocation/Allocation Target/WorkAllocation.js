/*
 * Copyright 2008 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

 /*
  * The identifier of the button to clear the allocation target item identifier 
  * on the Add Allocation Target Item Page.
  */
 var allocationTargetIdentifierClearPopupID = "ACTION$key$dtls$identifier_clear";
 
 /**
  * Clears the allocation target item identifier value on the Add Allocation Target 
  * Item Page.
  */
 function clearAllocationTargetIdentifier() {
   var clearElement = document.getElementById(allocationTargetIdentifierClearPopupID);
   clearPopup(clearElement);
 }