/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 /* Modifications
 * -------------
 * 05-Oct-2012 BD [CR00346152] Dojo 1.7 Upgrade 
 */
dojo.provide("Advisor");
dojo.require("curam.util.UimDialog");
dojo.require("curam.tab");

/*
 * Holds the parameters formatted in a fashion suitable for sending 
 * to the server
 */
var formattedParameters = "";

/*
 * Identifies the advice context for which we are currently 
 * displaying Advice
 */
var adviceContextID = "";

/*
 * The timestamp that we loaded the page at. Used by Advisor 
 * to determine if the Advice has changed when the ping command executes.
 */
var timeStamp = "";

/*
 * A variable containing the delay which should be applied between 
 * pings of the server
 */
var refreshIntervalInMilliSec;

/*
 * Advisor polling frequency
 */
var advisorpollingfrequency;

/*
 * The data which represents the current Advice Context in terms of the page we 
 * are looking at and the parameters on it.
 */
var gData;

/*
 * The advice item types which we are not interested in displaying, even if 
 * they are returned from the server.
 */
var excludedAdviceItemTypes;

/*
 * The localized alt text to display when presenting the Advisor 
 * 'loading' image
 */
var altTextForLoadingIcon;

/*
 * The refresh functionality for Advisor will periodically ping 
 * the server to see if the Advice has changed. This variable holds 
 * the variable identifying the deferred ping routine, and can be used to cancel the 
 * ping when the selected page changes.
 */
var timerIdentifier;

var counter = 0;

/*
 * Namespace for Advisor functions.
 */
var Advisor = {


/*
 * This method registers for the smart panel events. When the smart panel
 * is notified that the content pane has changed, the function that is
 * subscribed to the event will be called and will load the relevant
 * Advice
 */
onLoadHandler :  function() {
  var topic = "contentPane.targetSmartPanel";
    
  var contentPanelSubscribe = 
    dojo.subscribe(topic, function(data) {

      gData = data;
    	
      formattedParameters = "";
      for (param in data.parameters) {
        
        if (formattedParameters.length != 0) {
          formattedParameters += ",";        
        }
        
        formattedParameters += param;
        formattedParameters += "=";
        formattedParameters += data.parameters[param];
      
      }
      
      
      /*
       * Page changed - load the advice for this page.
       */
      Advisor.loadAdvice();

  });
  
  // BEGIN,CR00267740,GG
  curam.tab.publishSmartPanelContentReady();
  // END,CR00267740
  
},

/*
 * This method pings the server to check if the advice has changed since 
 * it was loaded
 */
ping : function() {

  var servletURL = '/Curam/servlet/PathResolver?r=j&p=/smartpanel/data/advisor/ping[' 
    + adviceContextID + "]["+ timeStamp +"]";
	 
  //Only ping the server if we have valid values
  if(adviceContextID !== "" && timeStamp !== "") {
    dojo.xhrPost({
      url: servletURL,
      headers: { "Content-Type": "text/json" },
      handleAs: 'json',
      load: function(response, args) {
      if (counter < advisorpollingfrequency) {
      
        if(response.hasAdviceChanged == "true") {
          Advisor.loadAdvice();
        } else {
        	counter = counter + 1;
          Advisor.setUpPing();
        }
      }
      },
      error: function(error, args) {
        // Add the error to the log.
        console.log("Error invoking the PathResolverServlet : " + error + " args: "
          + args);
      }
    });
  }
},

/**
 * This method loads the advice and also displays the loading icon.
 */
loadAdvice: function() {

  /*
   * Construct url to access the advisor data
   */
  var servletURL = '/Curam/servlet/PathResolver?r=j&p=/smartpanel/data/advisor/get[' + 
    gData.pageId + "]["+ formattedParameters +"]["+ excludedAdviceItemTypes +"]";
    
   
    
  var advisorCluster = dojo.byId("advisor");

  /*
   * Render an animated gif to indicate loading
   */
  advisorCluster.innerHTML = 
  "<CENTER><img src='../Images/loading_icon.gif' alt='" + altTextForLoadingIcon + "' align='middle'/></CENTER>";
  
  dojo.xhrPost({
    url: servletURL,
    headers: { "Content-Type": "text/json" },
    handleAs: 'json',
    load: function(response, args) {
      /*
       * Set the context id and time stamp in variables for later reference when 
       * determing if a change has occurred.
       */
      adviceContextID = response.adviceContextID;
      timeStamp = response.timeStamp;
      /*
       * How long to wait between polls to the server whilst checking for changes.
       */
      
      refreshIntervalInMilliSec = response.refreshIntervalInMilliSec;
      advisorpollingfrequency = response.advisorpollingfrequency;
      
      // Update the html with the actual Advice for display
      advisorCluster.innerHTML = response.adviceHTML;
       
     
      	Advisor.setUpPing();
     
    },
    error: function(error, args) {
      // Add the error to the log.
      console.log("Error invoking the PathResolverServlet : " + error + " args: "
            + args);
    }

  });  
},

/*
 * This method sets the excluded advice items types. It will be set from the
 * renderer which is got from the domain configuration.
 * 
 */
setExcludedAdviceItemTypes : function(_excludedAdviceItemTypes) {
  excludedAdviceItemTypes = _excludedAdviceItemTypes;
},

/*
 * This method sets the alternate text for the loading icon. This info is
 * set from the renderer.
 */
setAltTextForLoadingIcon : function (_altTextForLoadingIcon) {
  altTextForLoadingIcon = _altTextForLoadingIcon;
},

/*
 * When the page is unloaded de-register the subscription to the events.
 */
 unLoad : function() {
   clearTimeout(timerIdentifier);
 },

/*
 * Clear a timer if it is already set and set a new one to ping.
 */
setUpPing : function() {
  clearTimeout(timerIdentifier);
  timerIdentifier = setTimeout("Advisor.ping()", refreshIntervalInMilliSec);
},

/* Constructs and opens a modal url. */
constructAndOpenModalURL:function(url) {

url = url || window.event; // IE doesn't pass event as argument.
  var tgt = url.target || event.srcElement; // IE doesn't use .target
  dojo.stopEvent(url); // stop the original event and directly call the modal. 
  curam.util.UimDialog.openUrl(tgt.href);  
  
},

getLastPathSegmentWithQueryString:function(url) {
  var pathAndParams = url.split("?");
  var pathComponents = pathAndParams[0].split("/");
  return pathComponents[pathComponents.length - 1]
    + (pathAndParams[1] ? "?" + pathAndParams[1] : "");
  
}
}