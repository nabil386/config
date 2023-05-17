/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2014,2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 15-Oct-2019      CM  [RTC252937]  Set variable to true for a modal
 *                  running an IEG script.
 * 17-October-2014  ROR [CR00445363] Refactored to AMD module.
 */
 
/**
 * Collections of functions related to running the IEG Player in a modal.
 */
define(["dojo/dom"], 
    function(dom) {
	// IEG Player running in a modal?
	var isModal = false;
	
    // PUBLIC METHODS...
    return {
      /**
	   * Initialise a modal dialog.
	   * 
	   * Call to infrastructure ocde to initialise the modal dialog.
	   */   
	  initDialog: function () {
		curam.util.setExitingIEGScriptInModalWindowVariable();
		curam.util.Dialog.initModalWithIEGScript();
	    isModal = true;
	  },
	  
	  /**
	   * Utility function that allows us to check if the script
	   * is being run in a modal.
	   */
	  isInModal: function() {
	    return isModal;
	  },
	  
	  /**
	   * Set the initial modal title. 
	   * This is called on the first page of a script running in a modal. 
	   * 
	   * @param windowTitle The new dialog title.
	   */
	  registerTitle: function (windowTitle) {
	    var func = function() { return windowTitle;};
	    curam.util.Dialog.registerGetTitleFunc(func);
	  },
	  
	  /**
	   * Update the modal title on subsequent pages
	   *
	   * @param windowTitle The new modal page title.
	   */
	  setModalTitle:function(windowTitle) {
	    if(this.isInModal()) {
	      curam.dialog.getParentWindow().dijit.byId(curam.util.Dialog._id)
	        .titleNode.innerHTML = windowTitle;
	    }
	  },
	      
	  /**
	   * Register the modal size.
	   * 
	   * Register the modal size with the modal infrastructure.
	   * 
	   * @param dialogWidth   The dialogs width.
	   * @param dialogHieght  The dialogs height.
	   */
	  registerSize: function(dialogWidth, dialogHeight) {
	    size = new Object();
	    size.width = dialogWidth;
	    size.height = dialogHeight;
	    var func = function() { return size;};
	    curam.util.Dialog.registerGetSizeFunc(func);
	  },
	      
	  /**
	   * Modals - Call the infrastructure loadFinished on page load.
	   * 
	   * This function should only be called once. It sets up listeners for the
	   * modal dialog.
	   */
	  loadFinished: function() {
	    curam.util.Dialog.pageLoadFinished();
	  },
	  
	  /**
	   * 
	   */
	  closeDialog: function(/*optional*/ refreshParent, 
	    /*optional*/ newPageIdOrFullUrl, /*optional*/ pageParameters) {
	    console.warn("This function has been deprecated");
	    curam.util.Dialog.close(refreshParent, newPageIdOrFullUrl, 
	      pageParameters);
	  }
    };
});