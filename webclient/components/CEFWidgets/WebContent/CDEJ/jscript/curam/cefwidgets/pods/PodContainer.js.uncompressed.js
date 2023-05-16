/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012, 2019. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Copyright 2011-2012 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/**
 * Modification History
 * --------------------
 * 01-Apr-2019  CM [RTC243006]   Add new method updateImage to updateRefresh and Customize buttons.
 * 05-Dec-2012  SPD[CR00356184]  BIRT reporting pod fix. Add call to curam.util.getTopmostWindow() to
 *                               correct fire the publish event for listeners.
 * 03-Oct-2012  DG [CR00348020]  Fix for online help button.
 * 03-Oct-2012  BD [CR00345824]  Dojo 1.7 Upgrade. Migrate code. Moved under the curam.cefwidgets.pods 
 *                               module. Changed reference to cefwidgets.pods.*. Changes around the 
 *                               processing of lazy loaded Pods. Reacts to an event raised when all
 *                               Pods are loaded to enable drag and drop. Bug fix to suppress an error 
 *                               loading the PodSettings widget.
 * 21-Jun-2012  DG [CR00329440]  Updated the openHelpPage function to handle different locales.
 * 07-Jun-2012  SD [CR00329393]  CS-11476, add a copy of the default record to the database. This
 *                               record will contain all updates by the user to the pod container. 
 *                               Upon selecting the reset button, the user record will be deleted
 *                               and replaced with a fresh copy of the default record.
 * 05-Sep-2011  BD [CR00286425]  Fixed issue where the button processing function 
 *                               was causing the action to be submitted twice and
 *                               a page expiration issue was resulting.
 *                               Removed some residual console log statements and
 *                               updated the logging when a XHR request fails or 
 *                               succeeds. 
 * 04-Sep-2011  BD [CR00285772]  Updated the pageRefresh function to call the new
 *                               CDEJ page refresh function. 
 * 04-May-2011  BD [CR00265795]  Updated the showRemainingPods function to 
 *                               transfer new parameters that set the 'alt' 
 *                               text on the Pod buttons. 
 *                               Updated the cancelPodConfig and invokePodSave
 *                               functions to facilitate calling them from the
 *                               processButtonEvents function
 * 28-Apr-2011  BD [CR00264942]  Added a centralized button event processing 
 *                               method to handle styling. The button actions 
 *                               have been moved into seperate functions which
 *                               are called from the central function.
 * 14-Apr-2011  BD [CR00264396]  Created an array of all event listeners which
 *                               are disconnected when the page is unloaded to
 *                               ensure no memory leaks.
 *                               Published 'pods.fullyloaded' topic when the list of
 *                               lazy loaded pods is empty. 
 * 24-Feb-2011  BD [CR00251957]  Initial Version!!
 *                               This file was previously UsabilityWorkspace.js but has been
 *                               renamed to the more appropriate PodContainer.js as all the 
 *                               functionality is directly related to the PodContainer.
 *
 *
 *
 * 24-Jan-2011  BD [CR00243768]  Added functionality to support lazy loading of Pods.
 * 24 Nov 2010  BD [CR00231927]  Changed from reading the frame node by id to 
 *                               using the CDEJ curam.tab.getContentPanelIframe
 *                               function. This fixes a bug introduced by CDEJ 
 *                               changing the id of the node.
 * 10 Nov 2010  BD [CR00230549]  Removed reset script because we no longer confirm the reset. 
 * 30-Oct-2010  BD [CR00224983]  Added updateListInfo function. It updates the "Displaying X of Y" text on
 *                               a list if a list change event has occurred.
 * 16-Sep-2010  BD [CR00219621]  Added the 'jsDefaultRecord' parameter to the call to recordPodPosChange function
 *                               when the page loads. This parameter will be set to true if the page is loaded using
 *                               a default record. It will indicate to the recordPodPosChange function to 
 *                               call the save function on this page which will create a new record for the
 *                               current user. 
 * 09-Sep-2010  CH [CR00219164]  Added a function, uncheckCheckboxContainer, that unchecks the associated 
 *                               check box in the pod selection panel when a pod is closed.
 * 03-Sep-2010  BD [CR00219099]  Added context to AJAX call for savePodPositions. Removes console error. 
 * 02-Sep-2010  CH [CR00218087]  Added a listener for a pod close event which then saves the remaining pod positions.
 * 25-Aug-2010  BD [CR00217234]  Added listener for list size changes in Pods.
 *                               The listener will invoke the new recordPodListSizeChange function which 
 *                               will store the list size for the current user and page.
 *                               Quick fix for disappearing Pods in IE7. This is occuring when the customize
 *                               panel is opened. The fix is to force a size change on the container. The fix
 *                               returns the Pods to the page, but the problem is still very noticeable. Better
 *                               fix required.
 * 05-Aug-2010  BD [CR00211774]  Updates to rectify impacts from JDE014 take-on which changed the structure of the page.
 * 23-Jul-2010  BD [CR00211393]  Fixed drag and drop issues where Pods where disappearing by forcing a resize on the 
 *                               Container by changing its height by 1 pixel. 
 *                               Removed custom code for features like drag and drop and expand/collapse which are 
 *                               now handled by the Dojo widgets.
 *                               Added comments to all functions in the proper JS Doc format.
 * 20-Jul-2010  BD [CR00211207]  Fix drag and drop issues for Pods by disabling scrollbars when the Pod drag starts.
 *                               Introduce new listener for drag/start and new functions disableScrollbars and 
 *                               revertScrollbars to manage scrollbars.
 * 15-Jul-2010  BD [CR00210431]  Refactor to reflect move to using dojox.layout.GridContainerLayer for the
 *                               Portlets. The new GridContainer offers performance enhancements for drag and
 *                               drop.
 */

/**
 * This file manages the general functionality incorporated into a Pods page such as
 * drag and drop, automatic saves, onClick events and other miscellaneous event 
 * management requirements.
 */
 
dojo.provide("curam.cefwidgets.pods.PodContainer");

require(["curam/cefwidgets/GridContainer/dojox/layout/GridContainerLayer"]);

var uw = {
  
  // Initialise the columns on the page, setting up Drag and Drop
  columns: [],
  podHeights:{},
  eventSubscriptions: [],
  eventConnections: [],

  init: function(){

     // summary:
     //
     //    Initialise the Pod Container by setting up listeners for
     //    various events that must be reacted to.
  

    // Listen for Calendar events
    uw.calendarMonthSelect = dojo.subscribe("monthCalendarStack-selectChild", uw.selected);
    uw.eventSubscriptions.push(uw.calendarMonthSelect);
    
    // Listen for a Pod being dragged and react.
    uw.podDrag = dojo.subscribe("/dojox/mdnd/drag/start", function(){
    
      // ************  DRAG AND DROP ISSUES ***********

      // TODO: JDE updates should remove scrollbars from all but one of the elements in the
      //       frame. When this happens we should only need to disable scrollbars on that 
      //       particular element.
      // TODO: Get proper fix from Dojo for these issues and remove all this nonsense.
    
      // Disable the scrollbars when dragging of Pod starts.
      // Why? 
      //
      // Issue1: In IE7 a bug causes the browser to freeze up if the scrollbars
      // on the page change while dragging Pods. This seemes to be caused by updates 
      // to the dimension of the page which confuses the drag and drop handler.
      if (dojo.isIE < 8) {
        uw.temporarilyDisableScrollBarsOnContentPanel();
        uw.temporarilyDisableBodyElementScrollbars();
        uw.temporarilyDisableHtmlElementScrollbars();
        uw.temporarilyDisableIframeScrollbars();
      }
    });
    
    uw.eventSubscriptions.push(uw.podDrag);
    
    // Listen for a Pod being closed and record the 
    // new position of remaining pods.
    uw.podCloseListener = dojo.subscribe("/dojox/mdnd/close", function(){
      uw.recordPodPosChange(true);
    });
    uw.eventSubscriptions.push(uw.podCloseListener);
    
    // Listen for a Pod being dropped and record the new position.
    uw.podDropListener = dojo.subscribe("/dojox/mdnd/drop", function(){
      uw.recordPodPosChange(true);
      // The next call is important because it rectifies the situation where the
      // chart pod loses its content when moved.
      uw.toggleSize();
    });
    uw.eventSubscriptions.push(uw.podDropListener);
    
    // Listen for a Pod list size being changed.
    uw.podListSizeChangeListener = dojo.subscribe("/curam/list/changed", function(list){
    
      uw.recordPodListSizeChange(list.podId, list.listId, list.visibleItems);
    });
    uw.eventSubscriptions.push(uw.podListSizeChangeListener);
    
    // When a Pod is dropped the Pods in that column disappear. A redraw
    // is required to make them reappear. The redraw is forced by adding a class 
    // to the iframe after a delay to ensure the scrollbars have been redrawn.
    uw.inc = true;
    uw.resizeConnection = dojo.connect(window, "onresize", uw, "toggleSize");
    uw.eventConnections.push(uw.resizeConnection);
    
    // Call the record Pod Position function to initialize the container.
    // If the container is being loaded from the default record call save to 
    // create a new record for the user.
    uw.recordPodPosChange(jsDefaultRecord);

    uw.finishedLoadingPods = 
	  curam.util.getTopmostWindow().dojo.subscribe("pods.fullyloaded", uw , function(publishedData){

      // enable drag and drop again to ensure all drag areas and items are registered
      publishedData.gridContainer.enableDnd();
      
      // Save the positions of the Pods after the last Pod is dropped. This handles the scenario
      // where new Pods are added to the page. The pods are not assigned a position when they 
      // are loaded so a save is required to record where they have been dropped.
      publishedData.uw.recordPodPosChange();
      
      // disconnect listener.
      dojo.disconnect(uw.finishedLoadingPods);
    });

    uw.readyForMorePodsHandler = dojo.subscribe("pods.readyformore", uw , function(gridContainer){

      dojo.unsubscribe(uw.readyForMorePodsHandler);
      
      uw.addMorePodsHandler = dojo.subscribe("pods.addmore", uw, function(gridContainer){
      
        dojo.unsubscribe(uw.addMorePodsHandler);
        uw.showRemainingPods(gridContainer); 
      });
      
      uw.eventSubscriptions.push(uw.addMorePodsHandler);
      
      setTimeout(function(){
       dojo.publish("pods.addmore",[gridContainer]);}
       ,gridContainer.podDropDelay);
      
      // Disconnect any active listeners if the window gets unloaded.
      dojo.addOnWindowUnload(function(){
        // Disconnect all added listeners (added below) when the tab is closed.
        dojo.forEach(uw.eventSubscriptions, dojo.unsubscribe);
        dojo.forEach(uw.eventConnections, dojo.disconnect);
        dojo.disconnect(uw.destoryListeners);
      });
      
    });
    
  },
  
  toggleSize: function(){
  
    // summary:
    //      Toggle the height of the container by 1 pixel.
    //      This will force a redraw of the container and fixes the issue
    //      with Pods disappearing in IE7&8
  
    var podContainer = dojo.byId("podContainer");
    
    if(podContainer){
      var height = dojo.marginBox(podContainer).h;
    
      dojo.style(podContainer, "height", (height + (uw.inc ? 1 : -1)) + "px");
      uw.inc = !uw.inc;
    }
  },
  
  temporarilyDisableIframeScrollbars: function(){

    // summary:
    //     Disable the scrollbars on the IFRAME element while dragging a Pod, but create
    //     a listener which will reinstate them when the Pod is dropped.
    //     Add a class to the iframe after the scrollbars are redrawn to force a redraw of
    //     the container. This fixes the issue where the Pods disappear in column where
    //     the dragged Pod was dropped.

    //console.log("disableIframeScrollbars");

    uw.iframe = curam.tab.getContentPanelIframe();
    
    var scrollValue = dojo.attr(uw.iframe, "scrolling");

    dojo.attr(uw.iframe , "scrolling","no");

    // Create a listener to reinstate scrollbars as before    
    uw.dropListenerForIFrameScrollbars = 
      dojo.subscribe("/dojox/mdnd/drop", 
        function(){
        
          dojo.attr(uw.iframe , "scrolling", scrollValue);
          dojo.unsubscribe(uw.dropListenerForIFrameScrollbars);
          uw.toggleSize();

    });
    uw.eventSubscriptions.push(uw.dropListenerForIFrameScrollbars);
  },

  
  temporarilyDisableHtmlElementScrollbars: function(){
  
    // summary:
    //     Disable the scrollbars on the HTML element while dragging a Pod, but create
    //     a listener which will reinstate them when the Pod is dropped.

    //console.log("temporarilyDisableHtmlElementScrollbars");

    //Get Element
    var element = document.getElementsByTagName("html")[0];

    // Get Values of Element    
    var overflow = dojo.style(element, "overflow");
    var overflowX = dojo.style(element, "overflowX");
    var overflowY = dojo.style(element, "overflowY");

    // Hide all scrollabrs    
    uw.modifyScrollbar(element, null, "hidden");
    uw.modifyScrollbar(element, "X", "hidden");
    uw.modifyScrollbar(element, "Y", "hidden");

    // Create a listener to reinstate scrollbars as before    
    uw.dropListenerForContentPanelScrollbars = 
      dojo.subscribe("/dojox/mdnd/drop", 
        function(){
        
          uw.modifyScrollbar(element, null, overflow);
          uw.modifyScrollbar(element, "X", overflowX);
          uw.modifyScrollbar(element, "Y", overflowY);
          dojo.unsubscribe(uw.dropListenerForContentPanelScrollbars);
          
    });
    uw.eventSubscriptions.push(uw.dropListenerForContentPanelScrollbars);
    
    
  },
  
  temporarilyDisableBodyElementScrollbars: function(){
  
    // summary:
    //     Disable the scrollbars on the HTML element while dragging a Pod, but create
    //     a listener which will reinstate them when the Pod is dropped.

    //console.log("temporarilyDisableBodyElementScrollbars");

    var element = dojo.body();
    //Get Values of Element    
    var overflow = dojo.style(element, "overflow");
    var overflowX = dojo.style(element, "overflowX");
    var overflowY = dojo.style(element, "overflowY");

    // Hide all scrollabrs    
    uw.modifyScrollbar(element, null, "hidden");
    uw.modifyScrollbar(element, "X", "hidden");
    uw.modifyScrollbar(element, "Y", "hidden");

    // Create a listener to reinstate scrollbars as before    
    uw.dropListenerForBodyElementScrollbars = 
      dojo.subscribe("/dojox/mdnd/drop", 
        function(){
        
          uw.modifyScrollbar(element, null, overflow);
          uw.modifyScrollbar(element, "X", overflowX);
          uw.modifyScrollbar(element, "Y", overflowY);
          dojo.unsubscribe(uw.dropListenerForBodyElementScrollbars);
          
    });
    uw.eventSubscriptions.push(uw.dropListenerForBodyElementScrollbars);
    
  },  
  
  temporarilyDisableScrollBarsOnContentPanel: function(){

    // summary:
    //     Disable the scrollbars on the content panel element while dragging a Pod, but create
    //     a listener which will reinstate them when the Pod is dropped.
  
  
    //console.log("temporarilyDisableScrollBarsOnContentPanel");

    // Get Element
    var element = dojo.byId("content");

    // Get Values of Element    
    var overflow = dojo.style(element, "overflow");
    var overflowX = dojo.style(element, "overflowX");
    var overflowY = dojo.style(element, "overflowY");

    // Hide all scrollabrs    
    uw.modifyScrollbar(element, null, "hidden");
    uw.modifyScrollbar(element, "X", "hidden");
    uw.modifyScrollbar(element, "Y", "hidden");

    // Create a listener to reinstate scrollbars as before    
    uw.dropListenerForContentPanelScrollbars = 
      dojo.subscribe("/dojox/mdnd/drop", 
        function(){
        
          uw.modifyScrollbar(element, null, overflow);
          uw.modifyScrollbar(element, "X", overflowX);
          uw.modifyScrollbar(element, "Y", overflowY);
          dojo.unsubscribe(uw.dropListenerForContentPanelScrollbars);
          
    });
    uw.eventSubscriptions.push(uw.dropListenerForContentPanelScrollbars);
  } , 
  

  modifyScrollbar: function(element, axis, value){
  
    // summary:
    //     update the overflow value on an element.
    //
    // parameter: element
    //     The document Node that will be updated.
    // parameter: axis
    //     "X" or "Y" to indicate a particular axis, or blank to apply to X and Y axis.
    // parameter: value
    //     The modified value of the overflow attribute.
    
  
    //console.log("Modifying overflow" + axis + " value from '" + element.style.overflowY + "' to '" + value + "'.");
    if (axis==="y" || axis==="Y") {
     
      dojo.style(element, "overflowY", value);
      
    } else if (axis==="y" || axis==="Y") { 
    
      dojo.style(element, "overflowX", value);
      
    } else if (!axis || axis === null || axis === "") {
    
      dojo.style(element, "overflow", value);
    }
  
  },

  showRemainingPods: function(/*Object*/ gridContainer){

   // summary:
   //   Find all deliberately hidden Pods, add them to the PodContainer and 
   //   display them in a staggered fashion.
   //   This is a performance fix to prevent the scenario where too many Pods
   //   loading at once results in a long period where the user sees a blank
   //   page. Lazy loading allows us to show a number of Pods in a relatively
   //   short time, and then introduce the other Pods one by one. This allows 
   //   the user to see some content quickly and also to use the Application 
   //   quicker because they do not have to wait for all Pods to be loaded.
   
    // Enable drag and drop after the first phase of the page 
    // load(pre-lazyloading). This ensures existing layout is registered
    // with the AreaManager. 
    gridContainer.enableDnd();
    
    var hiddenPods = dojo.query(".hidden-pod", gridContainer.domNode);
    var lazyLoadedPods = [];
    
    dojo.forEach( hiddenPods, function(hiddenPod){

      var podContents = hiddenPod.childNodes;
    
      // Remove the PodSettings widget from the registry before it tries to add it again.
      //
      // The "parseonload" has already processed the PodSettings widget on the first pass. Now that
      // we are adding the "hidden" Pod that contains this widget Dijit will attempt to 
      // process it again  and add it to the registry. If it finds the entry there already 
      // it will fail and drop the Node from the "content" attribute of the PodWidget. 
      // To avoid this we remove the entry from the registry to trick Dijit.
      dojo.forEach( podContents, function(podContentNode){
        var declaredClass = dojo.attr(podContentNode, "dojoType"); 
        if (declaredClass && declaredClass === "curam.cefwidgets.pods.PodSettings"){
          dijit.registry.remove(dojo.attr(podContentNode, "widgetId"));       
        }
      });
    
      // Add a "dragHandle" class to make this item draggable.
      var classList = dojo.attr(hiddenPod, "class");
      classList += " dragHandle";
      
      // Copy all attributes off the hidden Pod and use them in the constructor.
      var closeable = dojo.attr(hiddenPod, "closable");
      var column = parseInt(dojo.attr(hiddenPod, "column"), 10);
      var dndType = dojo.attr(hiddenPod, "dndType");
      var dragRestriction = dojo.attr(hiddenPod, "dragRestriction");
      var id = dojo.attr(hiddenPod, "id");
      var title = dojo.attr(hiddenPod, "title");
      var toggleable = dojo.attr(hiddenPod, "toggleable");
      //BEGIN, CR00444525, YF
      var podFilterExpandButtonAltText = dojo.attr(hiddenPod, "podFilterExpandButtonAltText");
      var podFilterCollapseButtonAltText = dojo.attr(hiddenPod, "podFilterCollapseButtonAltText");
      var podCloseButtonAltText = dojo.attr(hiddenPod, "podCloseButtonAltText");
      var podToggleExpandButtonAltText = dojo.attr(hiddenPod, "podToggleExpandButtonAltText");
      var podToggleCollapseButtonAltText = dojo.attr(hiddenPod, "podToggleCollapseButtonAltText");

      var podWidget = new curam.cefwidgets.pods.Pod({
        id: id,
        title: title,
        closeable: closeable,
        column: column,
        dndType: dndType,
        dragRestriction: dragRestriction,
        toggleable: toggleable,
        podCloseButtonAltText: podCloseButtonAltText,
        podFilterExpandButtonAltText: podFilterExpandButtonAltText,
        podFilterCollapseButtonAltText: podFilterCollapseButtonAltText,
        podToggleExpandButtonAltText: podToggleExpandButtonAltText,
        podToggleCollapseButtonAltText: podToggleCollapseButtonAltText,
        'class': classList,
        content: podContents
      });
      //END, CR00444525, YF
      // Manually add the setttings widget because we have broken the normal
      // flow.
      podWidget._placeSettingsWidgets();
      // Remove the Node that the Pod widget was created from from the DOM.
      hiddenPod.parentNode.removeChild(hiddenPod);
      // Add the Pod to an array of lazy loaded Pods
      lazyLoadedPods.push(podWidget);
    });

    // Start at a row that is unlikely to be populated, as long as it is higher than
    // any existing row the Pod will be placed in the next position.
    var row=99;
    
    // The interval does not represent the time between each call to setTimeout
    // but rather the time from the setup of the timeout to its function being 
    // invoked. Because the timeouts are setup in a loop, they are essentially all
    // created at the same time. So to have for example a 1 second delay between 
    // each Pod drop we must increase the interval by 1000ms each time. 
    // e.g. 1000ms, 2000ms, 3000ms etc...
    var interval=gridContainer.podDropDelay;
  
    // If not adding lazy loaded Pods, then enable the Dnd now. 
    if (lazyLoadedPods.length === 0) {
      curam.util.getTopmostWindow().dojo.publish(
	    "pods.fullyloaded", [{gridContainer:gridContainer, uw:uw}]);
    }
  
    var podArray = []
    dojo.forEach(lazyLoadedPods, function(podWidget){
      podWidget.row = row;
      uwForTimeout = uw;
      podArray.push(podWidget.id);
      setTimeout(function(){
        //console.log("PodContainer ::: showRemaningPods > Add Pod " + podWidget.id + "(" + podWidget.title + ") to location [" + ((podWidget.column)-1) + "," + podWidget.row + "]");
        gridContainer.addChild(podWidget, (podWidget.column)-1, podWidget.row);
        podArray.pop(podWidget.id);
        if (podArray.length == 0) {
          curam.util.getTopmostWindow().dojo.publish(
		    "pods.fullyloaded", [{gridContainer:gridContainer, uw:uwForTimeout}]);
        }
      },
      interval);
      row++;
      interval+=gridContainer.podDropDelay;
    });
  },
  
  recordPodPosChange: function(sendMsg){
   
    // summary:
    //
    //    Record Pod Position Changes.
    //    When a Pod is moved or a Pod is selected/deselected, post a document to 
    //    the server describing the new layout of the Pods in the GridContainer.
    // 
    // parameter: sendMsg 
    //
    //    boolean indicating whether a message should be sent to the server for 
    //    this invocation of the function
   
    // Determine if the XML is being read from a default record. If so, add an 
    // attribute to indicate to the pod container manager on the server side
    // that a copy of this default XML is to me made and inserted into the
    // database for the user to update.
    if(jsDefaultRecord) {
      var xml = '<user-page-config loadedFromDefault="true">';
      
      // After the initial page load, this variable needs to be set to false so
      // that updates based on javascript events (DnD and pod closing) are applied
      // to the user record.
      jsDefaultRecord=false;
    } else {
      var xml = "<user-page-config>";
    }
    
    var gridContainer = 
      dijit.registry.byClass("dojox.layout.GridContainer").toArray()[0];
    
    var cells = gridContainer._grid;
  
    // Update the list of pods for all columns on the page.
    var colIndex = 0;
    for(colIndex = 0; colIndex < cells.length; colIndex++) {

      var col = cells[colIndex];
      var colInputId = uw.columns[colIndex];
      var colInput = dojo.byId(colInputId);

      if(!colInput) {
       return;
      }
      
      var podIds = [], id;
      var widgetNodes = dojo.query("> div", dojo.byId(col.node.id));

      // Build the string which is a comma separated list of pod ids in a single column
      var i = 0;
      for(i = 0; i < widgetNodes.length; i++) {
        var closed = dojo.attr(widgetNodes[i],"closed");
        id = dojo.attr(widgetNodes[i], "id");
        if(!closed){
          if(id) {
            podIds.push(id);
          }
        } else if (closed) {
          uw.uncheckCheckboxContainer(id);
        }
      }
      
      
      colInput.value = podIds.join(",");
      
      // Create an XML entry for each Pod, containing their current position.
      var rowIndex = 0;
      for (rowIndex = 0; rowIndex < podIds.length; rowIndex++) {
        xml += '<pod-config';
        xml += ' col="' + colIndex + '"';
        xml += ' row="' + rowIndex + '"';
        xml += ' id ="' + podIds[rowIndex] + '"';
        xml += '/>';
      }
    }
    
    xml += "</user-page-config>";
          
    // Call the dummy Page to trigger a XMLHTTP update in the background.
    if (sendMsg) {
      var context = "4096";
      dojo.xhrPost({
        url: "SavePodPositionsPage.do",
        load: function(type, data, evt){ 
          //console.log("Successful post to SavePodPositionsPage.do");
         },
        error: function(type, error){ 
          console.log("Error during Pod postition update. See PodContainer.js");
        },
        content: {o3ctx: context, podPositions: xml, pageID: jsPageID},
        mimetype: "text/json"
      });
     }
    
  },
  
  uncheckCheckboxContainer: function(podId){
   
  // summary:
  //
  //    Remove a Pod from the Pod Selector Panel checked list.
  //    When a Pod is closed it must also be removed from the list of 
  //    selected pods in the selector panel. This is achieved by setting the 
  //    "checked" and the "defaultChecked" attributes from the input element 
  //    associated with the pod to false.
  // 
  // parameter: podId 
  //
  //    the id of the Pod to be unchecked

 var checkBoxContainerId = "cbc_" + podId;
 var checkBoxContainerNode = dojo.byId(checkBoxContainerId);
 var input = checkBoxContainerNode.childNodes[0];
 
 input.checked = false;
 input.defaultChecked = false;

  },
   
  recordPodListSizeChange: function(podId, listId, size){
    
    // summary:
    //
    //    Record Pod Position Changes.
    //    When a Pod is moved or a Pod is selected/deselected, post a document to 
    //    the server describing the new layout of the Pods in the GridContainer.
    // 
   
    var data = podId + "," + listId + "," + size;
          
    var context = "4096";
    // Call the dummy Page to trigger a XMLHTTP update in the background.
    dojo.xhrPost({
      url: "SavePodListSizePage.do",
      load: function(type, data, evt){ 
        //console.log("Successful post to SavePodListSizePage.do");
      },
      error: function(type, error){ 
        console.log("Error on post size update. See PodContainer.js");
      },
      content: {o3ctx: context, podListSize : data, pageID: jsPageID},
        mimetype: "text/json"
      });
  
  },
  
  
  registerColumn: function(idx, id) {
  
    // summary: registers the columns for the GridContainer.
    //    The renderer responsible for the GridContainer will call this function
    //    for each column it defines in the GridContainer. The list of columns will
    //    be used to keep track of the Pod positions.
    //
    // parameter: idx
    //    Index of the column.
    //
    // parameter: id
    //    Id of the Node representing the column.
    
    uw.columns[Number(idx)] = id;
  },

  cancelPodConfig: function(button, event, args){

    // summary: Cancel the Pod Configuration.
    //
    //    Triggered when the 'Cancel' action is taken on a Pod. The Pod Settings pane 
    //    is wiped out and the selections are reset to the initial values from when
    //    the page was loaded.
    //
    // parameter: name
    //    Value of the 'name' attribute of the div representing the Portlet Settings.  
    
    var findPodSettingsNode = "div[name='" + args[0] + "']";
    var divNode = dojo.query(findPodSettingsNode)[0];
 
    if (divNode._anim){
      return;
    }
    if (divNode.isOpen === undefined) {
      divNode.isOpen = dojo.style(divNode, "display") != "none";
    }
    divNode._anim = dojo.fx.wipeOut({
      node: divNode,
      duration: 500,
      onEnd: function(){
        divNode.isOpen = false;
        divNode._anim = null;
        dojo.publish("wipeCompleted", [divNode]);
        uw.resetSelections(divNode.id);
      }
    });
    divNode._anim.play();
  },
 
  toggleWipe: function(id){

    // summary:
    //    Toggle the wipe in / wipe out of the element identified by the id.
    //
    // parameter:id
    //    Id of the element to be wiped.

    var divNode = dojo.byId(id);
    if (divNode._anim){
      return;
    }
    if (divNode.isOpen === undefined) {
      divNode.isOpen = dojo.style(divNode, "display") != "none";
    }
    divNode._anim = dojo.fx[divNode.isOpen ?   "wipeOut" : "wipeIn"]({
      node: divNode,
      duration: 500,
      onEnd: function(){
        divNode.isOpen = divNode.isOpen ? false : true;
        uw.toggleSize();
        divNode._anim = null;
        dojo.publish("wipeCompleted", [divNode]);
      }
    });
    divNode._anim.play();
  },

  invokePodSave: function(button, event, args) {

    // summary: Invoke the Save function on a Pod.
    // 
    //    Given an ID that maps to a hidden input contol for the Pod save, set the 
    //    value to true and submit the form.
    // 
    //    All form items will be submitted, but the inputID will be used to identify
    //    this action as a Pod save for a specific Pod.
    // 
    // parameter: inputId
    //    Id of the clicked button, i.e. which Pod Save button was clicked.
   
    var hidden_input = dojo.byId(args[0]);
      
    hidden_input.value = 'true';

    dojo.byId("mainForm").submit();
  },

  processButtonEvent: function(button, event, action, args) {

    // summary: React to the help button being clicked.
    // 
    //    This opens Curam Guides with the Help linked to the Pod Page
    // 
    // parameter: button
    //    the document element that the event occurred on.
    // parameter: event
    //    the event that occured on the button parameter.
    // parameter: action
    //    a function that will be the action taken as a result of an onclick
    //    or 'enter' key press on the button. The action must be a function
    //    that is a member of this object.
  
    
    // If this was keypress check it was on the 'enter' key. If not - ignore.
    if (event.type === "keyup" || event.type === "keydown") {
      if (CEFUtils.enterKeyPress(event) !== true) {
        return false;
      } 
    }
    //console.log("->" + event.type);
    if (event.type === "mouseover" || event.type === "focus") {
      dojo.addClass(button, 'hover');
      
    } else if (event.type === "mouseout" || event.type === "blur") {
      dojo.removeClass(button, 'hover');
      dojo.removeClass(button, 'selected');
      
    } else if ((event.type === "mousedown") || event.type === "keydown") {
      button.className=button.className + ' selected';
      
    }else if (event.type === "mouseup") {
    
      dojo.removeClass(button, 'selected');
      
    } else if (event.type === "click" || event.type === "keyup") {
      
      // Call the function pass in the 'action' parameter.
      uw[action](button, event, args);
    }
  },

  openCustomizePanel: function(button, event, args) {

    // summary: Open the customize panel.
    
    if (button.className.indexOf('opened-console') === -1) { 
      button.className = 'customize-button opened-console selected'; 
    } else {
      button.className = 'customize-button closed-console';
    }
    uw.toggleWipe('podSelectPane');
  },
  
  savePage: function(button, event, args) {
    curam.util.clickButton('__o3btn.CTL1');  
  },
  
  cancelCustomizePanel: function(button, event, args) {
    uw.resetSelections('podSelectPane');
    uw.toggleWipe('podSelectPane');    
  },
  
  openHelpPage: function(button, event, args) {
    // summary: Open the help page.
    dojo.stopEvent(event);
    var locale=curam.config?curam.config.locale:jsL;
    var hrefString;
    if(locale.indexOf("en")!=-1){
      hrefString="/help/index.html?"+jsPageID;
    }else{
      hrefString="/help_"+locale+"/index.html?"+jsPageID;
    }
    window.open(hrefString);
  },
  
  pageRefresh: function(button, event, args) {
    
    // summary: Refresh the page
    curam.util.Refresh.refreshPage(event);
  },  
  
  pageReset: function(button, event, args) {
    
    // reset the page.
    //console.log("reset");
    var hidden_input = dojo.byId(args[0]); 
    hidden_input.value=true;
    dojo.byId('mainForm').submit();

  },

  showTableRow: function(control, container) {    

    // summary: Show a table row.
    
    //    The function is specific to the ListRenderer produced table. It will have
    //    an add/remove widget in the form of 2 buttons. 1 button will add rows. 
    //    When the button is clicked the next hidden row is displayed.
    //
    //    When the maximum number is reached the add button is disbled and grayed 
    //    out.
    //
    //  parameter: control
    //    The Node representing the add item control.
    //
    //  parameter: container
    //    The Node representing the container for the table.
     
    var tableBody = container.getElementsByTagName("tbody")[0];

    for (var i = 0; i < tableBody.rows.length; i++) {
      if (dojo.hasClass(tableBody.rows[i], "blocked")) {
        dojo.removeClass(tableBody.rows[i], "blocked");

        if (i == tableBody.rows.length - 1) {
          dojo._setOpacity(control, 0.3);
          dojo._setOpacity(control.firstChild, 0.3);
          dojo.style(control, "cursor", "default");
        } else {
          dojo._setOpacity(control, 1);
          dojo._setOpacity(control.firstChild, 1);
          dojo.style(control, "cursor", "pointer");
        }

        var minus = control.nextSibling;
        dojo._setOpacity(minus, 1);
        dojo._setOpacity(minus.firstChild, 1);
        dojo.style(minus, "cursor", "pointer");

        break;
      }
    }
  },


  hideTableRow: function(control, container) {
  
    // summary: Hide a table row.
    
    //    The function is specific to the ListRenderer produced table. It will have
    //    an add/remove widget in the form of 2 buttons. 1 button will hide rows. 
    //    When the button is clicked the last displayed row is hidden.
    //
    //    When the minimum number is reached the remove button is disbled and grayed 
    //    out.
    //
    //  parameter: control
    //    The Node representing the remove item control.
    //
    //  parameter: container
    //    The Node representing the container for the table.
    
    var tableBody = container.getElementsByTagName("tbody")[0];
    var i;
    for (i = tableBody.rows.length -1; i > -1; i--) {

      if (i == 1) {                
        dojo._setOpacity(control, 0.3);
        dojo._setOpacity(control.firstChild, 0.3);
        dojo.style(control, "cursor", "default");

      } 

      if (!dojo.hasClass(tableBody.rows[i], "blocked")) {
        dojo.addClass(tableBody.rows[i], "blocked");
        var plus = control.previousSibling;

        dojo._setOpacity(plus, 1);
        dojo._setOpacity(plus.firstChild, 1);
        dojo.style(plus, "cursor", "pointer");

        break;
      }
      if (i == 1) {
        return false;
      }
    }
    if (i > i) {
      dojo._setOpacity(control, 1);
      dojo._setOpacity(control.firstChild, 1);
      dojo.style(control, "cursor", "pointer");
    }
  },  


  resetSelections: function (selectionGroupID){

    // summary: Reset selection inputs to their initial value.  
    //
    //   Find all check box or radio button elements which are descendants of the 
    //   Node identified by the 'selctionGroupID'. Reset their values to 
    //   the initial value when the page was loaded.
    //
    //   Note: All descendants of the Node are processed, not just the child 
    //   elements. 
    //
    // parameter: selectionGroupID
    //   Id of the container for the selection group.
    
    uw.toggleWipe(selectionGroupID);
    var inputControlList = dojo.query('input', selectionGroupID);
    
    for (i = 0; i < inputControlList.length; i++) {
      inputelem = inputControlList[i];
      if (inputelem.nodeType == 1 &&  
        (inputelem.type == 'checkbox'||inputelem.type == 'radio')) {
        var att = null;
        att = inputelem.defaultChecked
        if (att==null||att==false){
          inputelem.checked = false;
        } else {
          inputelem.checked = true;
        }
      }
    }
  },
  
  selected: function (page){
  
    // TODO: Document this function. It is related to the My Calendar widget.
    
    console.debug("page selected " + page.id);
    var widget=dijit.byId("myStackContainer");
    dijit.byId("previous").attr("disabled", page.isFirstChild);
    dijit.byId("next").attr("disabled", page.isLastChild);
  },
  
  updateListInfo: function(/*Object*/ listInfo){
    
    // summary:
    //  update a list info display
    //
    // description:
    //  if the number of items has changed in a list, update the display to the
    //  new value. This can occur if the list controller widget is used. It can 
    //  also occur if items are removed from the list using the RemoveItems 
    //  widget
    //
    // parameter: listInfo
    //   listInfo is an Object broadcast by the list change event.
    
    var list = dojo.byId(listInfo.listId);
    var xValue = dojo.query(".visible-items", list)[0];
    if (xValue) {
     xValue.innerHTML = listInfo.visibleItems;
     if (listInfo.removedItems){
       var yValue = dojo.query(".total-items", list)[0];
       var totalItems = yValue.innerHTML;
       var totalItems = totalItems - listInfo.removedItems;
       yValue.innerHTML = totalItems;
     }
    }
  },

  /**
   * Updates the button image on focus and blur event actions.
   *
   * @param button
   * 	The button element.
   * @param imagePath
   * 	The relative path of the new image source.
   */
  updateImage: function(/*Element*/ button, /*String*/ imagePath) {
	  button.children[0].src= imagePath;

  }
};