/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2020. All Rights Reserved. 
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Register the Page Behaviours.
 * These behaviours are used to hook up navigation buttons for example. 
 */ 

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

/**
 * Script Notes handling.
 */
cm.registerBehavior("collapsibleNotes", {
  ".bhv-notes-open" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var node = dojo.byId("notesDiv");
      
      if (dojo.style(node, "display") == 'none')
      {
        dojo.style(node, "display", "block");
        dojo.style(node, "height", "1px");
        dojo.fx.wipeIn({node: node}).play();
      }  
      else
      {
        dojo.fx.wipeOut({node: node, onEnd:function(){
          dojo.style(node, "display", "none");
        }}).play();
      }
    }
  },
  ".bhv-notes-close" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
      var node = dojo.byId("notesDiv");
      dojo.fx.wipeOut({node: node, onEnd:function(){
        dojo.style(node, "display", "none");
      }}).play();
    }
  }
});

/**
 * Handle onClick of the Next button
 */
cm.registerBehavior("next-button", {
  // hook up next button
  ".bhv-next-button" :
  {
	 "onclick": function(evt) {
      require(["ieg/ieg-xhr"], function(iegXHR){
        return iegXHR.doSubmit(evt, this);
      });
    }
  },
  // check the following form items...
  "input[type='text'],select,input[type='checkbox'],input[type='radio'],input[type='password']": {
    // submit form on pressing enter on these form items
    "onkeypress" : function(evt) {  
      if(evt.keyCode == dojo.keys.ENTER) {	    
    	// Check if the input field is a dropdown
    	// Added a check so that form is not submitted when "Enter" key
        // is pressed in open state of dropdown.
        if(evt.target.parentNode.getAttribute("role") == "listbox") {
      	  var input = dijit.registry.byId(evt.target.id);
          
          if (typeof input != "undefined") {
            var dropdownWidget = dijit.registry.byNode(dojo.byId("widget_" + evt.target.id));
         	if(dropdownWidget && dropdownWidget._opened) {
  	          return false;
      	  	}
      	  }
        }
        
		// Check if focused input is a combobox and prevent parent form
		// submitting upon hitting Enter key to select a combobox value.
		if(evt.target.id.indexOf("curam_widget_FilteringSelect") > -1) {
		  if (evt.target.nodeName == 'INPUT' && evt.target.type == 'text') {
		    evt.preventDefault();
		    return false;
    	  }
    	}

        // else submit the form on enter key
        var next = dojo.query(".bhv-next-button .next")[0];
        require(["ieg/ieg-xhr"], function(iegXHR){
          return iegXHR.doSubmit(evt, next ? next.parentNode : null);
        });
      }
      return true;
    },
    // check any expressions on these form items
    "onkeyup" : function(evt) {
	  require(["ieg/dynamic-behaviour"], function(dynamicBehaviour){
		  evt.target._timer && clearTimeout(evt.target._timer);
	      evt.target._timer = setTimeout(function(){
	    	dynamicBehaviour.checkExpression(evt.target);
	        evt.target._timer = null;
	      }, 200);
      });
    }
  }
});

/**
 * Handle onClick of the Save & Exit button
 */
cm.registerBehavior("save-exit-button", {
  ".bhv-save-exit-button" :
  {
    "onclick": function(evt) {
      require(["ieg/ieg-xhr"], function(iegXHR){
        return iegXHR.doSaveAndExit(evt, this);
      });      
    }
  }
});

/**
 * Handle onClick of the Exit button
 */
cm.registerBehavior("exit-button", {
  ".bhv-exit-button" :
  {
    "onclick": function(evt) {
      require(["ieg/ieg-xhr"], function(iegXHR){
        return iegXHR.doExit(evt, this);
      }); 
    }
  }
});

cm.registerBehavior("scroll-person-tabs", {
  "a.personTabsDiv" :
  {
    "found": function(node) {
      
      var currentPeople = dojo.query('.currentPerson');

      if (currentPeople.length < 1) {return;}

      var currentPerson = currentPeople[0];
      var pos = dojo.position(currentPerson);
      var size = dojo.marginBox(node);

      if (pos.x > size.w - 50) {
        node.scrollLeft = pos.x - size.w;
      }
    }
  }
});

/**
 * This page behaviour is used by the list "Add" link.
 * For example, if there is a grouped/nested list, there is a dropdown 
 * containing values which are used to add items (i.e. incomes). 
 * This code submits the link to add items fro the value selected in the 
 * dropdown. 
 */
cm.registerBehavior("IEG_GROUPED_HEAD_SELECT", {
  ".bhv-grouped-head-select" : {
    onclick: function(evt) {
      var id = evt.target.getAttribute("id");
      var select = dojo.byId(id.substring(0, id.length - 2) + "-list");
    
      var linkNode = dojo.byId(id);

      var loopindex = select.value;
      var href = evt.target.getAttribute("href");
      href += "&ieglidx=" + loopindex;

      dojo.attr(linkNode, "href", href);
      require(["ieg/ieg-xhr"], function(iegXHR){
    	  return iegXHR.iegLink(evt, linkNode);
      });
    }
  }
});

/**
 * Behaviour for IEG Links
 */
cm.registerBehavior("IEG_LINKS", {
  "a.bhv-ieg-link" :
  {
    "onclick": function(evt) {
      dojo.stopEvent(evt);
    }
  }
});

/**
 * Behaviour for IEG Print Link
 */
cm.registerBehavior("PRINT", {
  ".printLink" :
  {
    "onclick": function(evt) {	
      require(["dojo/dom-construct", "dojo/query", "dojo/dom-attr", "dojo/dom-class"], 
        function(domConstruct, query, domAttr, domClass){
    	  dojo.forEach(query("textarea[class*=text]"),
    	    function(textArea) {
    		  // Creates a printable representation of each textarea on the page, the original 
    		  // textarea's are hidden by CSS during printing.
    		  var textAreaPrintableDiv = query(".printableTextArea", textArea.parentNode)[0];
    		  if (!textAreaPrintableDiv) {
        	    textAreaPrintableDiv = domConstruct.create("div");
        	    domAttr.set(textAreaPrintableDiv, "class", "printableTextArea");
        	    domConstruct.place(textAreaPrintableDiv, textArea, "after");
        	  } else {
                textAreaPrintableDiv.innerHTML = '';  
        	  }
        	  var textAreaPrintableDivTextElement = domConstruct.create("pre");
        	  domAttr.set(textAreaPrintableDivTextElement, "class", "printableText");
        	  textAreaPrintableDivTextElement.innerHTML = textArea.value;
        	  domConstruct.place(textAreaPrintableDivTextElement, textAreaPrintableDiv);
        	  domAttr.set(textAreaPrintableDiv, "style", "min-height: " + 
        	    textArea.clientHeight + "px;");
              
        	  // Add or remove diabled styling to or from the printable textarea representation
        	  if (domAttr.get(textArea, 'disabled')) {
                domAttr.set(textAreaPrintableDiv, "class", "printableTextArea disabled");
              } else if (domClass.contains(textAreaPrintableDiv, "disabled")) {
                domClass.remove(textAreaPrintableDiv, "disabled");  
              }
    	    }
    	   );
    	});
      window.print();
    }
  }
});