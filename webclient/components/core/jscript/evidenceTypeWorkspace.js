/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2020. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


/**
 * Create focusin listener in the main form to submit
 * the form. The modal dialog infrastructure has to be loaded 
 * before submitting the form.
 * 
 */
function enactHiddenActionControl_onFirstLoad() {

  // This disables the delay in setting focus in modals in IE and Edge.
  sessionStorage.setItem("suppressCuramModalFocusTimeout", "true");
  
  // Note: This check depends upon the Cúram naming
  // scheme for the page which may change in the future.
  if (new String(window.location).indexOf('Action.do') == -1) {
    var form = document.getElementById("mainForm");
    form.addEventListener("focusin", submit );
    
  }
}

function enactHiddenActionControl_onChange() {
  submit();
}

function submit(){
  // Note: This depends on the Cúram naming scheme
  // for the form name which may change in the future.
  
  // Wrapping in a timeout of 0ms to prevent IE from throwing an error
  // of "code being executed in a freed script" due to setting suppressCuramModalFocusTimeout
  // property in sessionStorage.
  setTimeout(function() {document.forms['mainForm'].submit();}, 0);
}