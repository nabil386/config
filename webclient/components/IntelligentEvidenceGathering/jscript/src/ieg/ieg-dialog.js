/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2014,2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 13-Oct-2017  JD  [RTC210511] Add a title attribute to external network dialog.
 * 12-Oct-2017  GB  [RTC210511] Adding a new class to the network error dialog div.
 * 29-Aug-2017  JD  [RTC206064] Updated how the network connection error dialog
 * retrieves localized text.
 * 28-Aug-2017  JD  [RTC205795] Added functionality to display a dialog when a
 * network connection error is detected.
 * 26-July-2017 JD [RTC202895] Move import of oneui module so it's only
 * required by confirmActionExternal.
 * 19-June-2017 JD [RTC199030] Refactored all dojo calls to use AMD modules.
 * 09-September-2015  AB  [CR00466429] Dynamic browser tab titles
 * 29-September-2014  ROR [CR00445822] Initial Version.
 */

/**
 * Collections of functions related to setting focus on an IEG script page.
 */
define(["dojo/dom",
"dojo/_base/event",
"dojo/_base/connect",
"dojo/keys",
"curam/debug",
"curam/util",
"dijit/Dialog",
"dijit/form/Button",
"curam/util/LocalConfig"
], function(dom, event, connect, keys, debug, util, Dialog, Button, localConfig) {

    // PUBLIC METHODS...
    return {
      /**
	   * An IEG modal dialog Widget used with the internal IEG style
	   *
	   * Pops up a modal dialog window, blocking access to the screen
	   * and also graying out the screen. This dialog is extended from
	   * dijit.Dialog.
	   *
	   * @param title
	   * @param message
	   * @param okButtonText
	   * @param cancelButtonText
	   * @param node
	   * @param nodeID
	   * @param evt
	   */
	  confirmAction: function(title, message, okButtonText, cancelButtonText,
	    node, nodeID, evt) {

	    event.stop(evt);

	    var dojoPanel=document.createElement("div");

	    var contentPanel = document.createElement("div");
	    contentPanel.setAttribute("class", "confirmContentPanelDiv");
	    contentPanel.setAttribute("className", "confirmContentPanelDiv");

	    var headingDiv = document.createElement("div");
	    headingDiv.setAttribute("class", "confirmHeadingDiv");
	    headingDiv.setAttribute("className", "confirmHeadingDiv");

	    var titleDiv = document.createElement("div");
	    titleDiv.setAttribute("class", "confirmTitleDiv");
	    titleDiv.setAttribute("className", "confirmTitleDiv");
	    titleDiv.innerHTML = title;

        // Create function to restore title
        var oldTitle;
        var pageTitleHeading = dom.byId("pageTitleHeading");
        if(pageTitleHeading && pageTitleHeading.textContent) {
          oldTitle = pageTitleHeading.textContent;
        }
        var restoreTitle = function() {
          debug.log("ieg.ieg-dialog.confirmAction.restoreTitle calling curam.util.setBrowserTabTitle");
          util.getTopmostWindow().curam.util.setBrowserTabTitle(oldTitle);
        };
        // Set browser tab title to use modal window title
        debug.log("ieg.ieg-dialog.confirmAction calling curam.util.setBrowserTabTitle");
        util.getTopmostWindow().curam.util.setBrowserTabTitle(title);

	    var closeLink = document.createElement("a");
	    closeLink.setAttribute("id", "close");
	    closeLink.setAttribute("title", "Close");
	    closeLink.href = "#";
	    closeLink.setAttribute("class", "confirmCloseLink");
	    closeLink.setAttribute("className", "confirmCloseLink");

	    var closeDiv = document.createElement("div");
	    closeDiv.setAttribute("class", "confirmCloseDiv");
	    closeDiv.setAttribute("className", "confirmCloseDiv");

	    closeDiv.appendChild(closeLink);
	    headingDiv.appendChild(titleDiv);
	    headingDiv.appendChild(closeDiv);
	    contentPanel.appendChild(headingDiv);

	    var lineDiv = document.createElement("div");
	    lineDiv.setAttribute("class", "confirmLineDiv");
	    lineDiv.setAttribute("className", "confirmLineDiv");
	    contentPanel.appendChild(lineDiv);

	    var contentDiv = document.createElement("div");
	    contentDiv.setAttribute("class", "confirmContentDiv");
	    contentDiv.setAttribute("className", "confirmContentDiv");

	    var okButtonContainerSpan = document.createElement("span");
	    okButtonContainerSpan.setAttribute("class","confirmButtonLeft");
	    var okButtonSpan = document.createElement("span");
	    okButtonSpan.setAttribute("class","confirmButtonRight");

	    var okButton = document.createElement("span");
	    var okButtonText = document.createTextNode(okButtonText);
	    okButton.appendChild(okButtonText);
	    okButton.setAttribute("id", "okButton");
	    okButton.href = "#";
	    okButton.setAttribute("class", "confirmButtonDiv");
	    okButton.setAttribute("type", "button");
	    okButton.setAttribute("className", "confirmButtonDiv");
	    okButton.setAttribute("tabIndex", "0");

	    okButtonSpan.appendChild(okButton)
	    okButtonContainerSpan.appendChild(okButtonSpan);

	    var cancelButtonContainerSpan = document.createElement("span");
	    cancelButtonContainerSpan.setAttribute("class","confirmButtonLeft");
	    var cancelButtonSpan = document.createElement("span");
	    cancelButtonSpan.setAttribute("class","confirmButtonRight");

	    var cancelButton = document.createElement("span");
	    var cancelButtonText = document.createTextNode(cancelButtonText);
	    cancelButton.appendChild(cancelButtonText);
	    cancelButton.setAttribute("id", "cancelButton");
	    cancelButton.href = "#";
	    cancelButton.setAttribute("class", "confirmButtonDiv");
	    cancelButton.setAttribute("type", "button");
	    cancelButton.setAttribute("className", "confirmButtonDiv");
	    cancelButton.setAttribute("tabIndex", "0");

	    cancelButtonSpan.appendChild(cancelButton)
	    cancelButtonContainerSpan.appendChild(cancelButtonSpan);

	    var messageParagraph = document.createElement("p");
	    var messageText = document.createTextNode(message);
	    messageParagraph.appendChild(messageText);

	    contentDiv.appendChild(messageParagraph);
	    contentDiv.appendChild(okButtonContainerSpan);
	    contentDiv.appendChild(cancelButtonContainerSpan);

	    contentPanel.appendChild(contentDiv);
	    dojoPanel.appendChild(contentPanel);

	    var confirmDialog = new Dialog({},dojoPanel);
	    confirmDialog.setAttribute("id", "confirmDialog");
	    confirmDialog.setAttribute("tabIndex", "0");

	    // hook up AJAX calls
	    require(['ieg/ieg-xhr'], function(iegXHR) {
	    	connect.connect(okButton, 'onclick', function(){
		      confirmDialog.focused = false;
		      confirmDialog.hide();
		      iegXHR.iegLink(evt, nodeID);
              restoreTitle();
		    } ,false);

	    	// allow for access using keyboard
	        connect.connect(okButton, 'onkeypress', function(e){
	          if(e.keyCode == keys.ENTER) {
	            confirmDialog.focused = false;
	            confirmDialog.hide();
	            iegXHR.iegLink(evt, nodeID);
	            restoreTitle();
	          }
	        } ,false);
	    });

	    // allow for access using keyboard
	    connect.connect(cancelButton, 'onclick', function(){
	      confirmDialog.focused = false;
	      confirmDialog.hide();
	      confirmDialog.destroyRecursive();
	      restoreTitle();
	    } ,false);

	    // allow for access using keyboard
	    connect.connect(cancelButton, 'onkeypress', function(e){
	      if(e.keyCode == keys.ENTER) {
	        confirmDialog.focused = false;
	        confirmDialog.hide();
	        confirmDialog.destroyRecursive();
            restoreTitle();
	      }
	    } ,false);

	    connect.connect(closeLink, 'onclick', function(){
	      confirmDialog.focused = false;
	      confirmDialog.hide();
	      confirmDialog.destroyRecursive();
          restoreTitle();
	    } ,false);

	    confirmDialog.show();
	  },

	  /**
	   * A modal dialog Widget used with the external IEG style
	   *
	   * Pops up a modal dialog window, blocking access to the screen
	   *    and also graying out the screen. This dialog uses the OneUI
	   *    dialog.
	   *
	   * @param title
	   * @param message
	   * @param okButtonText
	   * @param cancelButtonText
	   * @param node
	   * @param nodeID
	   * @param evt
	   */
	  confirmActionExternal: function(title, message, okButtonText,
	    cancelButtonText, node, nodeID, evt) {

      // Create function to restore title
      var oldTitle;
      var pageTitleHeading = dom.byId("pageTitleHeading");
      if(pageTitleHeading && pageTitleHeading.textContent) {
        oldTitle = pageTitleHeading.textContent;
      }
      var restoreTitle = function() {
        debug.log("ieg.ieg-dialog.confirmActionExternal.restoreTitle calling curam.util.setBrowserTabTitle");
        util.getTopmostWindow().curam.util.setBrowserTabTitle(oldTitle);
      };
      // Set browser tab title to use modal window title
      debug.log("ieg.ieg-dialog.confirmActionExternal calling curam.util.setBrowserTabTitle");
      util.getTopmostWindow().curam.util.setBrowserTabTitle(title);

	    event.stop(evt);

	    var randomNumber=Math.floor(Math.random()*1001);

	    require(["idx/widget/Dialog"], function(idxDialog) {

  	  var confirmDialog = new idxDialog({
  			id: "confirmDialog-" + randomNumber,
  			title: title,
  			content: "<span class=\"confirmContentDiv\">"+message+"</span>",
  			buttons: [new Button({
  			  label: okButtonText,
  			  onKeyDown: function(evt) {
  				if (evt.keyCode == keys.ENTER) {
  					confirmDialog.hide();
  	                  restoreTitle();
  					require(['ieg/ieg-xhr'], function(iegXHR) {
  						return iegXHR.iegLink(evt, nodeID);
  					});
  				}
  			  },
  			  onClick: function(evt) {
  				confirmDialog.hide();
  	              restoreTitle();
  				require(['ieg/ieg-xhr'], function(iegXHR) {
  					return iegXHR.iegLink(evt, nodeID);
  				});
  			  }})],
  			  closeButtonLabel: cancelButtonText,
  			  hide: function() { restoreTitle(); confirmDialog.destroy(); }
  	  	    }, "confirmDialog");
  	  	confirmDialog.show();
        });
	  },

    /**
     * An IEG modal dialog Widget used with the internal IEG style.
     *
     * Pops up a modal dialog window when a network connection error is detected,
     * blocking access to the screen and also graying out the screen. This dialog
     * is extended from dijit.Dialog.
     *
     */
    showNetworkConnectionErrorDialogInternal: function() {

      var dojoPanel=document.createElement("div");

      var contentPanel = document.createElement("div");
      contentPanel.setAttribute("class", "confirmContentPanelDiv");
      contentPanel.setAttribute("className", "confirmContentPanelDiv");

      var headingDiv = document.createElement("div");
      headingDiv.setAttribute("class", "confirmHeadingDiv");
      headingDiv.setAttribute("className", "confirmHeadingDiv");

      var titleDiv = document.createElement("div");
      titleDiv.setAttribute("class", "confirmTitleDiv networkErrorDialogTitle");
      titleDiv.setAttribute("className", "confirmTitleDiv");

      // Retrieve the value of the title text that was retrieved from a property
      // and stored in LocalConfig.
      var networkConnectionErrorDialogTitleText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_TITLE_TEXT", "Network Connection Error");

      titleDiv.innerHTML = networkConnectionErrorDialogTitleText;
      titleDiv.setAttribute("title", networkConnectionErrorDialogTitleText);

      // Create function to restore title
      var oldTitle;
      var pageTitleHeading = dom.byId("pageTitleHeading");
      if(pageTitleHeading && pageTitleHeading.textContent) {
        oldTitle = pageTitleHeading.textContent;
      }
      var restoreTitle = function() {
        debug.log("ieg.ieg-dialog.confirmAction.restoreTitle calling curam.util.setBrowserTabTitle");
        util.getTopmostWindow().curam.util.setBrowserTabTitle(oldTitle);
      };
      // Set browser tab title to use modal window title
      debug.log("ieg.ieg-dialog.confirmAction calling curam.util.setBrowserTabTitle");
      util.getTopmostWindow().curam.util.setBrowserTabTitle(networkConnectionErrorDialogTitleText);

      headingDiv.appendChild(titleDiv);
      contentPanel.appendChild(headingDiv);

      var lineDiv = document.createElement("div");
      lineDiv.setAttribute("class", "confirmLineDiv");
      lineDiv.setAttribute("className", "confirmLineDiv");
      contentPanel.appendChild(lineDiv);

      var contentDiv = document.createElement("div");
      contentDiv.setAttribute("class", "confirmContentDiv networkErrorDialog");
      contentDiv.setAttribute("className", "confirmContentDiv");

      var okButtonContainerSpan = document.createElement("span");
      okButtonContainerSpan.setAttribute("class","confirmButtonLeft");
      var okButtonSpan = document.createElement("span");
      okButtonSpan.setAttribute("class","confirmButtonRight");

      var okButton = document.createElement("span");

      // Retrieve the value of the button text that was retrieved from a property
      // and stored in LocalConfig.
      var networkConnectionErrorDialogOkButtonText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_OK_BUTTON_TEXT", "OK");

      var okButtonText = document.createTextNode(networkConnectionErrorDialogOkButtonText);
      okButton.appendChild(okButtonText);
      okButton.setAttribute("id", "okButton");
      okButton.href = "#";
      okButton.setAttribute("class", "confirmButtonDiv");
      okButton.setAttribute("type", "button");
      okButton.setAttribute("className", "confirmButtonDiv");
      okButton.setAttribute("tabIndex", "0");

      okButtonSpan.appendChild(okButton)
      okButtonContainerSpan.appendChild(okButtonSpan);

      var messageParagraph = document.createElement("p");

      // Retrieve the value of the message text that was retrieved from a property
      // and stored in LocalConfig.
      var networkConnectionErrorDialogMessageText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_MESSAGE_TEXT",
        "A network error occurred while your request was being processed. Check your internet connection and try again.");

      var messageText = document.createTextNode(networkConnectionErrorDialogMessageText);
      messageParagraph.appendChild(messageText);

      contentDiv.appendChild(messageParagraph);
      contentDiv.appendChild(okButtonContainerSpan);

      contentPanel.appendChild(contentDiv);
      dojoPanel.appendChild(contentPanel);

      var confirmDialog = new Dialog({},dojoPanel);
      confirmDialog.setAttribute("id", "confirmDialog");
      confirmDialog.setAttribute("tabIndex", "0");

      connect.connect(okButton, 'onclick', function(){
        confirmDialog.focused = false;
        confirmDialog.destroyRecursive();
        restoreTitle();
      } ,false);

      // allow for access using keyboard
      connect.connect(okButton, 'onkeypress', function(e){
        if(e.keyCode == keys.ENTER) {
          confirmDialog.focused = false;
          confirmDialog.destroyRecursive();
          restoreTitle();
        }
      } ,false);

      confirmDialog.show();
    },

    /**
     * An IEG modal dialog Widget used with the external IEG style.
     *
     * Pops up a modal dialog window when a network connection error is detected,
     * blocking access to the screen and also graying out the screen. This dialog
     * uses the OneUI dialog.
     *
     */
    showNetworkConnectionErrorDialogExternal: function() {

      // Create function to restore title
      var oldTitle;
      var pageTitleHeading = dom.byId("pageTitleHeading");

      // Populate the text variables that will be used in the dialog. These values were
      // retrieved from properties and stored in LocalConfig.
      var networkConnectionErrorDialogTitleText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_TITLE_TEXT", "Network Connection Error");
      var networkConnectionErrorDialogMessageText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_MESSAGE_TEXT",
        "A network error occurred while your request was being processed. Check your internet connection and try again.");
      var networkConnectionErrorDialogOkButtonText = localConfig.readOption(
        "iegConfig_PROP_NETWORK_CONNECTION_ERROR_DIALOG_OK_BUTTON_TEXT", "OK");

      if(pageTitleHeading && pageTitleHeading.textContent) {
        oldTitle = pageTitleHeading.textContent;
      }
      var restoreTitle = function() {
        debug.log("ieg.ieg-dialog.confirmActionExternal.restoreTitle calling curam.util.setBrowserTabTitle");
        util.getTopmostWindow().curam.util.setBrowserTabTitle(oldTitle);
      };
      // Set browser tab title to use modal window title
      debug.log("ieg.ieg-dialog.confirmActionExternal calling curam.util.setBrowserTabTitle");
      util.getTopmostWindow().curam.util.setBrowserTabTitle(networkConnectionErrorDialogTitleText);

      var randomNumber = Math.floor(Math.random()*1001);

      require(["idx/widget/Dialog", "dijit/a11y", "dojo/dom-attr"], function(idxDialog, a11y, domAttr) {

        var confirmDialog = new idxDialog({
          id: "confirmDialog-" + randomNumber,
          baseClass: "idxDialog networkConnectionErrorDialog",
          title: networkConnectionErrorDialogTitleText,
          content: "<span class=\"confirmContentDiv networkErrorDialogExternal\">"+networkConnectionErrorDialogMessageText+"</span>",
          closeButtonLabel: networkConnectionErrorDialogOkButtonText,
          hide: function() {
            restoreTitle();
            confirmDialog.destroy();
          },
          _getFocusItems: function(){
            if(this._firstFocusItem){
              this._firstFocusItem = this._getFirstItem();
              if(!this._firstFocusItem){
                var elems = a11y._getTabNavigable(this.containerNode);
                this._firstFocusItem = elems.lowest || elems.first || this.closeButton.focusNode || this.domNode;
              }
              return;
            }
            var elems = a11y._getTabNavigable(this.containerNode);
            this._firstFocusItem = elems.lowest || elems.first || this.closeButton.focusNode;
            this._lastFocusItem = this.closeButton.focusNode;
          },
  		  _setTitleAttr: function(title){
			this.titleNode.innerHTML = title;
			domAttr.set(this.titleNode, "tabindex", title ? 0 : -1);
			domAttr.set(this.titleNode, "title", title ? title : "");
		  }
        }, "confirmDialog");
        
        confirmDialog.show();

        });
      
      }

    };

});
