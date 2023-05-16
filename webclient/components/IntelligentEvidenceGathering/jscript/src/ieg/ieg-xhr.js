/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2013,2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 28-Sep-2017  CMC [RTC206900]  Re-enabled hidden disabled elements after loss of 
 * connectivity modal is displayed.
 * 05-Sept-2017 JD  [RTC206664]  Refactored code to use AMD module so it can be tested
 * using Intern.
 * 28-Aug-2017  JD  [RTC205795]  Modified XHR error functions to display a popup
 * dialog when a loss of network is detected.
 * 01-Dec-2016  LD  [RTC180705]  Modified the page transitions for the internal app.
 * 14-Sep-2016  CMC [170520]     Updated loading spinner to Progress Spinner.
 * 07-May-2014  ROR [CR00430747] Refactored to AMD module.
 */

/**
 * Functions related to navigating an IEG script.
 */
define(["dojo/dom", "ieg/update-manager", "ieg/mandatory-behaviour",
          "curam/widget/ProgressSpinner", "ieg/ieg-dialog", "dojo/dom-attr", "dojo/_base/html"],
            function(dom, updateManager, mandatory, progressSpinner, iegDialog, domAttr, html) {
    /* Array used to gather old page data to be removed */
    var oldPageData = {};
    /* Variables used for delay page spinner */
    var pageSpinner;
    var loadingTimeout;
    /* The time it takes for a page to transition */
    var transitionDuration = 1000;

    // PUBLIC METHODS...
    return {
      /**
      * Handle IEG Links.
      *
      * @param evt   The event that calls this function
      * @param nodeID  The DOM node that was clicked on.
      */
      iegLink: function(evt, nodeID) {
        dojo.stopEvent(evt);

        // get the form to submit.
        // false is returned here if the form has already been submitted
        var form = getForm();
        if(!form) return;

        // get the url to submit to from the node
        var node = dom.byId(nodeID);
        var href = domAttr.get(node, "href");

        // Only submit if a valid href exists.
        // If not, clear the form submitted flag.
        if (href == null) {
          setFormSubmitted(form, false);
          return;
        }

        // gather the old page data to be destroyed later
        getOldPageData();

        // build the xhr request object
        var xhrArgs = {
          preventCache: true,
          url: href,
          handleAs: "text",
          // Handle onLoad
          load: function(response) {
            handleAJAXSuccess('Link', form, response);
          },
          // Handle Errors
          error: function(e, args){
            handleAjaxError(e, args, form, evt);
          }
        }

        // send request
        dojo.xhrGet(xhrArgs);
      },

      /**
      * Handle clicking of the Back button.
      *
      * @param evt   The event that called this function
      * @param href  The URL to follow
      */
      doBack: function(href) {
        // get the form to submit.
        // false is returned here if the form has already been submitted
        var form = getForm();
        if(!form) return;

        // update the url
        href = href.replace(/&amp;/g, "&");

        // gather the old page data to be destroyed later
        getOldPageData();

        // build the arguments to pass as part of the XHR request
        var wasPersonTabsInput = dojo.query("input[name='wasPersonTabs']")[0];
        var wasPersonTabs =
          wasPersonTabsInput !== undefined && wasPersonTabsInput.value == "true";
        var xhrArgsContent = wasPersonTabs ? {'wasPersonTabs':'true'}: {};

        // build XHR request object
        var xhrArgs = {
          url: href,
          preventCache: true,
          handleAs: "text",
          content: xhrArgsContent,
          // Handle onLoad
          load: function(response) {
            handleAJAXSuccess('Back', form, response);
          },
          // Handle Errors
          error: function(e, args) {
            handleAjaxError(e, args, form, null);
          }
        }

        // send request
        dojo.xhrGet(xhrArgs);
        return false;
      },

      /**
      * Submit the form - i.e. Next.
      *
      * @param evt   The event that called this function.
      * @param node  Not used
      */
      doSubmit: function(evt, node) {
        dojo.stopEvent(evt);

        // get the form to submit.
        // false is returned here if the form has already been submitted
        var form = getForm();
        if(!form) return;

        // set the action of the form
        var formAction = dojo.attr(form, "id");
        dojo.attr(form, "action", formAction);
		var disabeledElements = mandatory.conditionalMandatoryHandling();

        // gather the old page data to be destroyed later
        getOldPageData();

        //set up AJAX request object
        var xhrArgs = {
          form: form,
          handleAs: "text",
          // Handle onLoad
          load: function(response) {
            handleAJAXSuccess('Next', form, response);
          },
          // Handle Errors
          error: function(e, args){
            for (var i = 0; i < disabeledElements.length; i++) {
              // Re-enable hidden disabled elements
              if (dijit.byId(disabeledElements[i])) {
                dijit.byId(disabeledElements[i]).set('disabled', false);
              } else {
                domAttr.remove(dojo.byId(disabeledElements[i]), "disabled");
              }
            }
            handleAjaxError(e, args, form, null);
          }
        }
        
        // send the request
        dojo.xhrPost(xhrArgs);
        

        // setup the delay spinner
        setWaitingCursor(true);

        return false;
      },

      /**
      * Submit the form to the server for saving/validation and get back
      * the exit page for redirection to.
      *
      * @param evt   The event that called this function.
      * @param node  Not used.
      */
      doSaveAndExit: function(evt, node) {
        dojo.stopEvent(evt);

        // get the form to submit.
        // false is returned here if the form has already been submitted
        var form = getForm();
        if(!form) return;

        // set the action of the form
        var formAction = dojo.attr(form, "id");
        formAction += "&action=save_and_exit";
        dojo.attr(form, "action", formAction);
        var disabeledElements = mandatory.conditionalMandatoryHandling();

        // gather the old page data to be destroyed later
        getOldPageData();

        // build the XHR object
        var xhrArgs = {
          form: form,
          handleAs: "text",
          // Handle onLoad
          load: function(response) {
            handleAJAXSuccess('SaveAndExit', form, response);
          },
          // Handle Errors
          error: function(e, args) {
            for (var i = 0; i < disabeledElements.length; i++) {
              // Re-enable hidden disabled elements
              if (dijit.byId(disabeledElements[i])) {
                dijit.byId(disabeledElements[i]).set('disabled', false);
              } else {
                domAttr.remove(dojo.byId(disabeledElements[i]), "disabled");
              }
            }	  
            handleAjaxError(e, args, form, null);
          }
        }

        // send the request
        dojo.xhrPost(xhrArgs);

        // setup the delay spinner
        setWaitingCursor(true);

        return false;
      },

      /**
      * Submit the form to the server and get back the
      * exit page specified for the script.
      *
      * @param evt   The event that called this function.
      * @param node  Not used.
      */
      doExit: function(evt, node) {
        dojo.stopEvent(evt);

        // get the form to submit.
        // false is returned here if the form has already been submitted
        var form = getForm();
        if(!form) return;

        // set the form action
        var formAction = dojo.attr(form, "id");
        formAction += "&action=exit";
        dojo.attr(form, "action", formAction);

        // request arguments
        var xhrArgs = {
          form: form,
          handleAs: "text",
          load: function(response) {
            // check the response
            if(!checkResponse(response))
              return;
          },
          error: function(e, args) {
            handleAjaxError(e, args, form, null);
          }
        }

        // send request
        dojo.xhrPost(xhrArgs);
        return false;
      }
    };

    // PRIVATE METHODS...
    /**
    * Returns the form for page submission.
    * Returns false if the form/page has already been submitted.
    */
    function getForm() {
      var form = dojo.query("form")[0];
      if (wasFormSubmitted(form)) {
        return false;
      } else {
        setFormSubmitted(form, true);
        return form;
      }
    }

    /**
    * Function used to control submission of pages/forms.
    *
    * @param form The IEG page form.
    */
    function wasFormSubmitted(form) {
      return form._alreadySubmitted;
    }

    /**
    * Specifies if the form has already been submitted.
    *
    * @param form           The IEG page form.
    * @param wasSubmitted   Submitted flag.
    */
    function setFormSubmitted(form, wasSubmitted) {
      if (typeof(form) == 'undefined') {
        form = dojo.query("form")[0];
      }

      // Sets the flag on the form to state whether or
      // not it was previously submitted.
      // If any onSubmit handler for a form (e.g. validation)
      // cancels the onSubmit event, it should call this method,
      // passing false as the second parameter.
      form._alreadySubmitted = wasSubmitted;
    }

    /**
    * This functions gathers various data from the page that has just been visited.
    * Some of this data is later destroyed when a new page has been moved into place.
    */
    function getOldPageData() {
      oldPageData.oldMainWrapper = dojo.query('.ieg-main-wrapper')[0];
      oldPageData.oldMainContent = dojo.query('#main')[0];
      oldPageData.oldFunctions = dojo.query('#ieg-page-functions')[0];
      oldPageData.oldSyncToken = dojo.query("input[name=__o3synch]")[0];
      oldPageData.oldMetaData = dojo.query("input[name=__o3fmeta]")[0];
      oldPageData.oldMandatoryMetaData = dojo.query("input[name=__o3fmeta_mandatory_data]")[0];
      oldPageData.oldCoordsMain = html.coords(oldPageData.oldMainContent);
      oldPageData.oldCoordsMainWrapper = html.coords(oldPageData.oldMainWrapper);
      oldPageData.oldPersonTabs = dojo.query(".personTabsTable")[0];
      oldPageData.oldPageTitle = dojo.query(".pageHeadingTable")[0];

      // the main content panes
      oldPageData.mainContentPane = dojo.query('#ieg-main-content-pane')[0];
      oldPageData.mainContentPaneNested = dojo.query('#ieg-main-content-pane-nested')[0];

      // is there an actions panel?
      oldPageData.actionsPanel = dijit.byId("ieg-actions-panel");

      // update the back button
      var oldBackButton = dojo.byId("ieg-back-button");
      if (oldBackButton) {
        dojo.attr(oldBackButton, "id", "ieg-back-button-old");
      }
    }

    /**
    * Function used to display the Progress
    * Spinner when a large request has been sent
    * which may cause the system to appear
    * as unresponsive to the end user.
    *
    * @param show Used to determine whether the spinner
    *             is being shown or hidden.
    */
    function setWaitingCursor(show) {
      // show/hide the Progress Spinner
      if(show == true) {
    	var spinner = progressSpinner.getSpinner("PlayerBody");
    	if (spinner != null) {
          // displays the Progress Spinner after certain threshold
    	  // defined in miliseconds.
          progressSpinner.show(spinner, null);
        }
      }
      else {
    	  progressSpinner.dismissSpinner(window.frames);
      }
    }

    /**
    * Handle a successful AJAX response.
    *
    * @param type      The transition type - next, back etc...
    * @param form      The HTML form to submit
    * @param response  The server response
    */
    function handleAJAXSuccess(type, form, response) {
      require(["dojo/dom", "dojo/parser", "dojo/dom-construct",
               "dojo/_base/array", "dojo/topic", "ieg/update-manager"],
      function(dom, parser, domConstruct, array, topic, updateManager) {
        // check the response
        if(!checkResponse(response)) return;

        // destroy any existing widgets found
        destroyContentPaneWidgets(getResponseMainPane(response));

        // parse the response
        var mainHTML = getResponseMainHTML(response);
        var mainHTMLNode = domConstruct.toDom(mainHTML);

        // position of where new content will transition from.
        // next scrolls content up, back scrolls content down
        var position = type == 'Back' ? "first" : "last";

        // parse the response, initialising any widgets
        // and place it in the DOM
        try {
          parser.parse(mainHTMLNode);
          domConstruct.place(mainHTMLNode,
            getResponseMainPane(response), position);
        } catch (e) {
          // CANT SEE THIS CODE GETTING EXECUTED! if actions panel?
          // destroy any widgets found
          destroyContentPaneWidgets(dom.byId("ieg-main-content-pane"));

          if (oldPageData.actionsPanel) {
            mainHTMLNode = domConstruct.toDom(mainHTML);
          } else {
            mainHTMLNode = domConstruct.toDom(response);
          }

          parser.parse(mainHTMLNode);
          domConstruct.place(mainHTMLNode,
            oldPageData.mainContentPane, position);
        }

        // reset the form flag & re-apply the page behaviours when the new
        // page has transitioned in to place
		var handle = topic.subscribe("ieg-page-loaded", function(){
		  var form = dojo.query("form")[0];
	      if(form) {
	        setFormSubmitted(form, false);
	      }
	      dojo.behavior.apply();
	      handle.remove();
		});

		// parse out the JSON object
	    updateManager.parseJSONUpdateData();

        // do the page transitions
        doPageTransitions(type, form, response, mainHTML);
      });
    }

    /**
    * Handle AJAX request errors.
    * If a network connection error is detected it will show a popup dialog informing
    * the user.
    * If an AJAX call fail, this function injects the
    * error page html into the DOM in place of the exitsing body.
    *
    * @param error The error message supplied by DOJO.
    * @param args  The error arguments supplied by DOJO.
    */
    function handleAjaxError(error, args, form, evt) {
      console.log('IEG Error: ' + error.name + ': ' + error.message);
      console.dir(error);
      console.dir(args);

      // clear the delay spinner
      setWaitingCursor(false);

      // Check for a network connection error and display a popup dialog informing
      // the user of it.

      if(args.xhr.status == 0 && args.xhr.responseText == "") {

        // Set the form to be not submitted.
        setFormSubmitted(form, false);

        /*
          When there is a list add-link with a corresponding select-dropdown, if this add-link
          is clicked it will add the string "&ieglidx=" plus the value of the select to the href.
          If the add-link is clicked offline, it will still append the string but a server call will
          not be made. In order for this to work when the network connection has been restored, the
          appended string will need to be removed.
        */
        if(evt != null) {
          var id = evt.target.getAttribute("id");

          if(id != null) {
            var select = dojo.byId(id.substring(0, id.length - 2) + "-list");

            if(select != null) {
              var loopindex = select.value;
              var stringToRemove = "&ieglidx=" + loopindex;
              var href = evt.target.getAttribute("href");

              if(href.indexOf(stringToRemove) !== -1) {
                var reg = new RegExp(stringToRemove, 'g');
                var newHref = href.replace(reg,'');
                var linkNode = dojo.byId(id);
                dojo.attr(linkNode, "href", newHref);
              }
            }
          }

        }

        // Show the appropriate dialog depending on internal or external mode.
        if(updateManager.isExternal()){
          iegDialog.showNetworkConnectionErrorDialogExternal();
        } else {
          iegDialog.showNetworkConnectionErrorDialogInternal();
        }

      } else {

        // set the body of our document to the contents of the body
        // tag of the error page returned from the server
        var playerBody = dojo.byId("PlayerBody");
        var message = "Response: "+ args.xhr.responseText;
        if(playerBody && message) {
          dojo.byId("PlayerBody").innerHTML =
            /<body.*?>([\s\S]*)<\/body>/.exec(args.xhr.responseText)[1];
        }
      }
    }

    /**
    * Check the IEG Response.
    *
    * @param response The AJAX response to check.
    *
    * @return boolean Indicates if response is valid.
    */
    function checkResponse(response) {
      // clear the delay spinner
      setWaitingCursor(false);

      // Redirect?
      if (response.indexOf("URL:") == 0) {
        console.log("Exiting IEG...");
        window.location.replace(response.substring(4));
        return false;
      }
      // Sanity check that this is a valid IEG response
      // if not a valid response then inject the response into our Dojox
      // Content Pane so that any javascript in the response gets executed.
      // This is needed for UA redirects for example which are done via
      // javascript returned in the HTML.
      else if (response.indexOf("ieg-main-wrapper") == -1) {
        console.log("Not an IEG Response. Redirecting...");
        console.log("Response: "+response);

        var contentPane = dijit.byId("ieg-main-content-pane");
        contentPane.setContent(response);
        return false;
      }
      return true;
    }

    /**
    * Destroy any existing widgets in the main content pane.
    *
    * Existing widgets need to be removed before we inject the new content.
    *
    * @param node The node to search for widgets to destroy.
    */
    function destroyContentPaneWidgets(node) {
      // Theres an issue where the CSS state for the "onmouseout" event is still
      // hooked up for the OneUI Widgets after the widgets have been
      // destroyed. This causes Dojo to try to apply the CSS styles even though
      // the widgets no longer exist, resulting in console errors.
      // Explicitly remove the cssState to prevent Dojo from trying to update
      // the state on mouse out.
      dojo.query("[widgetid]").forEach(function(node) {
        node._cssState = null;
      });

      // find and destroy any widgets in this node
      var widgets = dijit.findWidgets(node);
      dojo.forEach(widgets, function(w) {
    	w._cssState = null;
        w.destroyRecursive(true);
      });
    }

    /**
    * Check the response to see which content pane is the main content pane
    * and return it.
    *
    * When Person Tabs are being displayed, only the nested content pane is
    * updated. The person tabs are in the outer content pane and are updated
    * serarately.
    *
    * @param response The new AJAX response received.
    */
    function getResponseMainPane(response) {
      // does the response contain a new nested pane widget?
      var node;
      if(response.indexOf("ieg-main-content-pane-nested") > 0) {
        node = dojo.byId("ieg-main-content-pane");
      } else {
        node = dojo.byId("ieg-main-content-pane-nested");
      }
      return node;
    }

    /**
    * Returns the main html element from the AJAX response.
    *
    * The main element depends on what is being returned.
    * i.e. actions-panel indicates the response is for a modal.
    *
    * @param response The AJAX response.
    */
    function getResponseMainHTML(response) {
      // parse the response
      var mainHTML = response;
      if (oldPageData.actionsPanel) {
        mainHTML = response.substring(0,
        response.indexOf("<div class=\"actions-panel\""));
        var actionsPanelHTML = response.substring(
          response.indexOf("<div class=\"actions-panel\""));
        var actionsPanelNode = dojo.toDom(actionsPanelHTML);
        dojo.parser.parse(actionsPanelNode);
        var newActionSet = dojo.query(
          "div[class~='action-set']", actionsPanelNode);
        oldPageData.actionsPanel.setContent(newActionSet);
      }
      return mainHTML;
    }

    /**
    * This function does the actual page transitions.
    *
    * @param type     The transition type - next, back etc...
    * @param form     The page form.
    * @param response The new AJAX response received.
    * @param mainHTML Used when the actions panel is being shown.
    */
    function doPageTransitions(type, form, response, mainHTML) {
      // TODO more refactoring of this function required...

      // have person tabs?
      var personTabsJSONData =
        updateManager.getJSONUpdateData().persontabs;

      // get page wrapper
      var index = type == 'Back' ? 0 : 1;
      var newMainWrapper = dojo.query("div[class='ieg-main-wrapper']",
        oldPageData.mainContentPane)[index];
      var newMain = dojo.query("div[id='main']",
        oldPageData.mainContentPane)[index];

      // set some initial styling for the page transitions
      if(type === 'Back') {
        if (personTabsJSONData !== undefined) {
          var newBox = dojo.marginBox(newMain);
          dojo.style(newMain, "top", (- newBox.h) + "px");
          dojo.style(oldPageData.oldMainContent, "top", (- newBox.h) + "px");
        } else {
          var newBox = dojo.marginBox(newMainWrapper);
          dojo.style(newMainWrapper, "top", (- newBox.h) + "px");
          dojo.style(oldPageData.oldMainWrapper, "top", (- newBox.h) + "px");
        }
      } else if(type === 'Next'){
        if (personTabsJSONData !== undefined) {
          dojo.style(newMain, "visibility", "hidden");
        } else {
          dojo.style(newMainWrapper, "visibility", "hidden");
        }
      } else if(type == 'Link') {
        // use a different main for Link transitions
        newMain = dojo.query("div[class='ieg-main-wrapper']",
        getResponseMainPane(response))[1];

        // this doesn't work in IE8...
        dojo.style(newMain, "opacity", 0);

        // update old & new content
        var oldBox = dojo.marginBox(oldPageData.oldMainWrapper);
        dojo.style(newMain, "top", (-oldBox.h) + "px");
      }

      // are transition animations enabled?
      var transitionsActive = transitionsEnabled(response, type);
      if(transitionsActive) {
        // set the animation duration
        var duration = transitionDuration;

        // update the persons tab. this call updates the person tabs and sets
        // the transition animation to run
        var personTabsAnimation = [];
        if (personTabsJSONData !== undefined) {
         personTabsAnimation = updateManager.getPersonTabsAnimation(
           duration, oldPageData.oldPersonTabs);
        }

        // other page section animations
        var pageTitleAnimation = updateManager.getPageTitleAnimation(
            duration, oldPageData.oldPageTitle);
        var progressAnimation =
          updateManager.getProgressBarAnimation(duration);
        var sectionAnimation =
          updateManager.getSectionsPanelAnimation(duration);

        // the actual animation for this page transition
        var pageAnimation;
        var isExternal = updateManager.isExternal();
        if(type === 'Back') {
          pageAnimation = getBackPageAnimation(
            personTabsJSONData, newMain, newMainWrapper, isExternal);
        } else if(type === 'Next') {
          pageAnimation = getNextPageAnimation(
            personTabsJSONData, newMain, newMainWrapper, isExternal);
        } else if(type === 'Link') {
          pageAnimation = getLinkPageAnimation(
            newMain, oldBox, duration);
        }

        // combine the various animations into one to be fired
        var trans = dojo.fx.combine(pageAnimation.concat(progressAnimation,
          sectionAnimation, personTabsAnimation, pageTitleAnimation));

        // connect to the onEnd event of the transition
        dojo.connect(trans, 'onEnd', function() {
          // clean up person tabs
    	  if(personTabsJSONData !== undefined) {
      	    dojo.destroy(oldPageData.oldPersonTabs);
            var personTabsNode = dojo.query(".personTabsTable")[0];
            dojo.attr(personTabsNode, "style", "");
            dojo.removeAttr(personTabsNode, "style");
          }

          // handle page transition finish
          onPageTransitionComplete(form, response, personTabsJSONData, transitionsActive);
        });

        // start the transition animations
        trans.play();
      } else {
        // get the new content
        var activeContent = response;
        if (oldPageData.actionsPanel) {
          activeContent = mainHTML;
        }

        // handle page transition finish
        onPageTransitionComplete(form, activeContent, personTabsJSONData, transitionsActive);
      }
    }

    /**
     *
     */
    function onPageTransitionComplete(form, activeContent, personTabsJSONData, transitionsActive) {
    	// active content pane?
    	var contentPane = dijit.byId("ieg-main-content-pane");
        if (personTabsJSONData !== undefined) {
          contentPane = dijit.byId("ieg-main-content-pane-nested");
        }

        // TODO use dojo/on for onLoad. However dojo/on was not picking up the
        // onLoad event of the contentPane when trialed after Dojo 1.9 upgrade
        var handle = dojo.connect(contentPane, "onLoad", function(){
          require(["dojo/topic"], function(topic) {
            topic.publish("ieg-page-loaded");
            dojo.disconnect(handle);
      	  });
        });

        // set the content
        contentPane.setContent(activeContent);

    	// destroy the old page data
        destroyOldPageData(form, personTabsJSONData, transitionsActive);
    }

    /**
    * Check if transitions are enabled
    *
    * @param response  The server response.
    * @param type      The transition type - next, back etc...
    */
    function transitionsEnabled(response, type) {
      var transition = updateManager.getJSONUpdateData().transition;
      if (response.indexOf("class=\"messages-container\"") > -1) {
        transition = false;
      } else if(type == 'SaveAndExit') {
    	transition = false;
      }
      return transition;
    }

    /**
    * Destroy the data from the page that has transitioned out.
    *
    * @param form	The page form to reset.
    * @param personTabsJSONData	Flag denoting if person tabs present.
    * @param transitionsActive Flag denoting if transitions are enabled.
    */
    function destroyOldPageData(form, personTabsJSONData, transitionsActive) {
      // update page title?
      if(transitionsActive) {
    	  dojo.destroy(oldPageData.oldPageTitle);
          var pageTitleNode = dojo.query(".pageHeadingTable")[0];
          dojo.attr(pageTitleNode, "style", "");
          dojo.removeAttr(pageTitleNode, "style");
      }

      // destroy old data
      dojo.destroy(oldPageData.oldMetaData);
      dojo.destroy(oldPageData.oldMandatoryMetaData);
      dojo.destroy(oldPageData.oldSyncToken);
      dojo.destroy(oldPageData.oldFunctions);

      // reset form action
      if(form) dojo.attr(form, "action", "#");
    }

    /**
    * Sets up the page animations for moving to the next page.
    *
    * @param personTabsJSONData  Used to denote if person tabs are present.
    * @param newMain             The new main content.
    * @param newMainWrapper      The new main content.
    * @param isExternal			 Running in an external application.
    */
    function getNextPageAnimation(personTabsJSONData, newMain, newMainWrapper, isExternal) {
      // get page title height
      var pageTitlesHeightDifference = getPageTitlesHeightDifference();

      var pageAnimation;
      if (personTabsJSONData !== undefined) {
        var oldCoords = oldPageData.oldCoordsMain;
        var newPersonTabs = dojo.query(".personTabsTable")[1];

        if(isExternal){
          pageAnimation = [
           dojo.fx.wipeOut({node:oldPageData.oldMainContent,
             duration:transitionDuration}),
           dojo.fx.slideTo({node:oldPageData.oldMainContent, left:'0',
             top:-oldCoords.h, duration:transitionDuration}),
           dojo.fx.slideTo({node:newMain, left:'0',
             top:-pageTitlesHeightDifference, duration:transitionDuration}),
           dojo.fx.slideTo({node:newPersonTabs, left:dojo.coords(newPersonTabs).l,
             top:dojo.coords(newPersonTabs).t-pageTitlesHeightDifference,
             duration:transitionDuration}),
           dojo.fx.wipeIn({node:newMain, duration:transitionDuration})
         ];
        }else{
            pageAnimation = [
	          // wipe out the old content from bottom to top and slide it up
    		  dojo.fx.wipeOut({node:oldPageData.oldMainContent, duration:transitionDuration}),
    		  dojo.fx.wipeIn({node:newMain, duration:transitionDuration}),
    		  dojo.fx.wipeIn({node:newPersonTabs, duration:transitionDuration})
	         ];
        }

      } else {
    	if(isExternal){
		  var oldCoords = oldPageData.oldCoordsMainWrapper;
          pageAnimation = [
	        // wipe out the old content from bottom to top and slide it up
	        dojo.fx.wipeOut({node:oldPageData.oldMainWrapper,
	          duration:transitionDuration}),
	        dojo.fx.slideTo({node:oldPageData.oldMainWrapper, left:'0',
	          top:-oldCoords.h, duration:transitionDuration}),
	        // slide in new content and wipe it in from top to bottom
	        dojo.fx.slideTo({node:newMainWrapper, left:'0',
	          top:-pageTitlesHeightDifference, duration:transitionDuration}),
	        dojo.fx.wipeIn({node:newMainWrapper, duration:transitionDuration})
	      ];
    	}else{
    	  pageAnimation = [
    	    // wipe out the old content from bottom to top and slide it up
    		dojo.fx.wipeOut({node:oldPageData.oldMainWrapper, duration:transitionDuration}),
    		dojo.fx.wipeIn({node:newMainWrapper, duration:transitionDuration})
          ];
    	}


      }
      return pageAnimation;
    }

    /**
    * Sets up the page animations for moving backwards.
    *
    * @param personTabsJSONData  Used to denote if person tabs are present.
    * @param newMain             The new main content.
    * @param newMainWrapper      The new main content.
    * @param isExternal			 Running in an external application.
    */
    function getBackPageAnimation(personTabsJSONData, newMain, newMainWrapper, isExternal) {
      // get page title height
      var pageTitlesHeightDifference = getPageTitlesHeightDifference();

      var pageAnimation;
      if (personTabsJSONData !== undefined) {
    	if(isExternal){
          var newPersonTabs = dojo.query(".personTabsTable")[1];
          pageAnimation = [
            dojo.fx.wipeOut({node:oldPageData.oldMainContent,
              duration:transitionDuration}),
            dojo.fx.slideTo({node:newMain, left:'0',
              top:-pageTitlesHeightDifference, duration:transitionDuration}),
            dojo.fx.slideTo({node:newPersonTabs, left:dojo.coords(newPersonTabs).l,
              top:dojo.coords(newPersonTabs).t-pageTitlesHeightDifference,
              duration:transitionDuration}),
            dojo.fx.slideTo({node:oldPageData.oldMainContent,
              left:'0', top:'0', duration:transitionDuration})
           ];
    	}else{
    		pageAnimation = [
     	       dojo.fx.wipeOut({node:oldPageData.oldMainContent, duration:transitionDuration})
     		];
    	}

      } else {
    	if(isExternal){
          pageAnimation = [
            dojo.fx.wipeOut({node:oldPageData.oldMainWrapper,
              duration:transitionDuration}),
            dojo.fx.slideTo({node:newMainWrapper,
              left:'0', top:-pageTitlesHeightDifference,
              duration:transitionDuration}),
            dojo.fx.slideTo({node:oldPageData.oldMainWrapper,
              left:'0', top:'0', duration:transitionDuration})
          ];
    	}else{
    	  newMainWrapper.style.display='none';
    	  pageAnimation = [
    	    dojo.fx.wipeOut({node:oldPageData.oldMainWrapper, duration:transitionDuration}),
    		dojo.fx.wipeIn({node:newMainWrapper, duration:transitionDuration})
    	  ];
    	}
      }
      return pageAnimation;
    }

    /**
    * Sets up the page animations for moving between pages using a Link.
    *
    * @param newMain   The new main page element.
    * @param duration  The durationof the animation.
    */
    function getLinkPageAnimation(newMain, oldBox, duration) {
      // get page title height
      var pageTitlesHeightDifference = getPageTitlesHeightDifference();

      // set position of new content
      dojo.style(newMain, "top",
        (-oldBox.h-pageTitlesHeightDifference) + "px");

      // set animation properties
      var pageAnimation = [
        dojo.fadeIn({node:newMain, duration:duration}),
        dojo.fadeOut({node:oldPageData.oldMainWrapper, duration:duration})
      ];

      return pageAnimation;
    }

    /**
    * Utility function for calculating the height difference when transitioning
    * to a new page.
    */
    function getPageTitlesHeightDifference() {
      var pageTitles = dojo.query(".pageHeadingTable");
      var pageTitlesHeightDifference =
        dojo.coords(pageTitles[0]).h - dojo.coords(pageTitles[1]).h;
      return pageTitlesHeightDifference;
    }
});
