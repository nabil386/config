/*
 * Copyright 2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */
dojo.provide("birtPod");

/* Modification History
 * ====================
 * 14-Apr-2011  BD [CR00264396]  Created an array of all event listeners which
 */

/**
 * Used to lazy load the BIRT Iframe when in a Pod. Loading the IFrame at the 
 * same time as the Pods was causing issues which were determined to be timing
 * related. Waiting for the pods to fully loaded before setting iframe works
 * around these issues.
 * 
 */
var birtPod = {

  eventSubscriptions:[],
    
  lazyLoad: function(frameId, url) {
  
    var topDojo = curam.util.getTopmostWindow().dojo;
    
    birtPod.podsFullyLoadSubscription = topDojo.subscribe('pods.fullyloaded', function(){
      
      setTimeout(function(){
        var reportUrl = decodeURIComponent(url);
        dojo.attr(dojo.byId(frameId),'src',reportUrl);
        
        // Remove subscription after events is processed.
        curam.util.getTopmostWindow().dojo.unsubscribe(birtPod.podLoadSubscription);
        
      }, 200);// end timeout
    }); // end subscribe
    
    // Tidy up all event listeners in the event they are not naturally killed.
    birtPod.eventSubscriptions.push(birtPod.podsFullyLoadSubscription);
    dojo.addOnWindowUnload(function(){
      // Disconnect all added listeners
      dojo.forEach(birtPod.eventSubscriptions, topDojo.unsubscribe);
    });
  } // end lazyLoad() function
};