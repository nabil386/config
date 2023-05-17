/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2014. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

 /*
  * The identifier of the "Assigned To" clear button on the create and edit application case admin Pages.
  */
 
 var ClearAssignedToIDForInternalOwner = "ACTION$details$internalOwnerOrgObjectIdentifier_clear";
 
 var ClearAssignedToIDForOnlineOwner = "ACTION$details$onlineOwnerOrgObjectIdentifier_clear"
 
 var ClearAssignedToIDForIssueOwner= "ACTION$details$issueOwnerOrgObjectIdentifier_clear"
 
 var ClearAssignedToIDForExternalOwner  ="ACTION$details$externalOwnerOrgObjectIdentifier_clear"
 /**
  * Clears the "Assigned To" value  for the owner type on the create and edit
  * application case admin pages. 
  */
  
 function clearAssignedForInternalOwner() {
   var clearElement = document.getElementById(ClearAssignedToIDForInternalOwner);
   
   if (clearElement != null) {
     clearPopup(clearElement);
   }
 }
 
 
 function clearAssignedForOnlineOwner() {
 
  var clearElement = document.getElementById(ClearAssignedToIDForOnlineOwner);
   
   if (clearElement != null) {
     clearPopup(clearElement);
   }
 }
 
 
 function clearAssignedForIssueOwner() {
 
  var clearElement = document.getElementById(ClearAssignedToIDForIssueOwner);
   
   if (clearElement != null) {
     clearPopup(clearElement);
   }
 }
 
 function clearAssignedForExternalOwner() {
 
  var clearElement = document.getElementById(ClearAssignedToIDForExternalOwner);
   
   if (clearElement != null) {
     clearPopup(clearElement);
   }
 }
 
 
 

 