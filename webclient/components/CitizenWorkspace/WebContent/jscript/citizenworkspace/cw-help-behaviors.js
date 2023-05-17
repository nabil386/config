/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2014. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
var modules = [
  "dojo/domReady",
  "dojo/ready",
  "dojo/fx", 
  "dojo/number",  
  "dojo/behavior",
  "dojo/dom",
  "dojo/parser",
  "dojo/html",
  "dijit/Dialog",
  "dijit/focus",
  "dijit/registry",  
  "dojox/layout/ContentPane",
  "dojox/widget/Standby",
  "cm/_base/_form", 
  "cm/_base/_behavior", 
  "cm/_base/_dom", 
  "cm/_base/_topics", 
  "cm/_base/_pageBehaviors", 
  "cm/_base/_validation", 
  "curam/util", 
  "curam/util/Dialog"];
 
require(modules, function(domReady, ready) {

    domReady(function() {
      console.log("dojo/domReady !!");
    });
    
    ready(function() {
      console.log("dojo/ready !!");
      
      // fire the page loaded event
      dojo.publish("ieg-page-loaded");
    });
});



/**
 * Behaviour related to the onClick event of the Collapsible Help Panels
 */
cm.registerBehavior("collapsibleHelp", {
  ".bhv-open" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var wrapper = cm.getParentByClass(evt.target, "cluster") 
        || cm.getParentByClass(evt.target, "list");

      dojo.query(".bhv-collapsible", wrapper)
        .forEach(function(collapsible){
          if (dojo.style(collapsible, "display") == 'none')
          {
            dojo.style(collapsible, "display", "block");
            if (!collapsible._shrunk && dojo.isIE == 6) {
              collapsible._shrunk = true;
              var width = dojo.style(collapsible.parentNode, "width");
              dojo.style(collapsible, "width", (width - 4) + "px");
            }   
            dojo.style(collapsible, "height", "1px");
            dojo.fx.wipeIn({node:collapsible, onEnd: function(){
              // Transfer the focus to the the matching anchor tag for a11y
              var href = evt.target.getAttribute("href");
              if (href != null && href.length > 1) {
                dojo.query("a[name=" + href.substring(1) + "]").
                  forEach("item.focus()");
              }
            }}).play();
            dojo.removeClass(collapsible, "ifScript-hide");
            dojo.removeClass(evt.target, "openHelpLink");
            dojo.addClass(evt.target, "openHelpLinkSelected");
            
            // flag to screen readers that the cluster is visible 
            dojo.attr(evt.target, "aria-expanded", "true");
          }
          else
          {
            dojo.fx.wipeOut({node:collapsible, onEnd:function(){
              dojo.style(collapsible, "display", "none");
              dojo.removeClass(evt.target, "openHelpLinkSelected");
              dojo.addClass(evt.target, "openHelpLink");
              
              // flag to screen readers that the cluster is invisible 
              dojo.attr(evt.target, "aria-expanded", "false");
          }}).play();
        }
      });
      dojo.publish("/anim/toggle");
    }
  },
  ".bhv-close" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var input = evt.target;
      var wrapper = cm.getParentByClass(evt.target, "cluster") 
        || cm.getParentByClass(evt.target, "list");
      var collapsible = dojo.query(".bhv-collapsible", wrapper)[0];
      dojo.style(collapsible, "overflowY", "hidden");
      dojo.fx.combine([
        dojo.fx.wipeOut({node: collapsible})
      ]).play();

      var openLink = dojo.query(".bhv-open", wrapper)[0];
      dojo.removeClass(openLink, "openHelpLinkSelected");
      dojo.addClass(openLink, "openHelpLink");
      
      // flag to screen readers that the cluster is invisible 
      dojo.attr(openLink, "aria-expanded", "false");
      
      // reset the focus to the help link.
      dijit.focus(openLink);

      dojo.publish("/anim/toggle");
    }
  }
});




