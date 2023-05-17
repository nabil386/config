dojo.require("dojo/NodeList-traverse");
dojo.provide("cw.behaviors");


dojo.mixin(cw, {
  getInputsNodes: function(input) {
    var arr = [input.parentNode];
    var sibling = cm.nextSibling(input.parentNode, "div");
    if(sibling) {
      arr.push(sibling);
    }
    return arr;
  },

  selectAll: function(evt) {
      var input = evt.target;
      var contextNode;
      var checked = input.checked;
      //var parentId;

      //  Martin Fitz need to verafy changes with Ciaran.

      /*if(dojo.hasClass(input, "bhv-in-grandparent")) {
        contextNode = input.parentNode.parentNode;
      } else if(dojo.hasClass(input, "bhv-next-list")) {
        parentId = input.parentNode.id;
        if (!parentId || parentId == "") {
          parentId = "tempId_" + (new Date()).getTime();
          input.parentNode.id = parentId;
        }
        contextNode = dojo.query("#" + parentId + " + .list-wrapper", input.parentNode)[0];
      } else if(dojo.hasClass(input, "bhv-in-cluster")) {
        contextNode = input.parentNode;
        while(!dojo.hasClass(contextNode, "cluster") && contextNode){
          contextNode = contextNode.parentNode;
        }
      } */
     // var tttt = dojo.query(".cluster <", document.body);

      var parentNode = function(findClassName, startNodePoint)
      {
    	  var outPut = null;
    	  if(startNodePoint != null)
    	  {
    		  var transentNode = startNodePoint;
    		  var keepLooping = true;
    		  while(keepLooping) {

    			  if(transentNode.tagName=="BODY")
    			  {
    				  keepLooping = false;
    			  }
    			  else if (transentNode.className != null &&
    					  transentNode.className == findClassName)
    			  {
    				  keepLooping = false;
    				  outPut = transentNode;
    			  }
    			  else
    			  {
    				  transentNode = transentNode.parentNode;
    			  }
    		  }
    	  }
    	  return outPut;
      };


      contextNode = parentNode("cluster", input);

      if(contextNode == null){
    	  console.log("No context node found, cannot select checkboxes");
      }
      else
      {

    	  // find all widget-dom-nodes in a div, convert them to dijit reference:
    	  var widgets = dojo.query("[widgetId]", contextNode).map(dijit.byNode);
    	  // now iterate over that array making each checked / unchecked in dijit-land:
    	  dojo.forEach(widgets, function(w){

    		  if(w.id != input.id)
    		  {
    			  w.set("checked", checked);

    		  }

    	  });
      }



      /*

      dojo.query("input[type='checkbox']", contextNode)
          .forEach(function(input){
            if(evt.target == input){return;}
            input.checked = checked ? true : false;

            dojo.style(input, {"outlineColor": "lightblue",
                "outlineStyle": "solid", "outlineWidth": "2px"});

            dojo.animateProperty(
              {node: input, properties:
                { outlineWidth: 0,
                  duration: 200, onEnd: function(){
                  dojo.style(input, "outline", "none");
                }
               }}).play();
          });
      */

    }
});

// Add an onclick event to all "select all" check boxes.
cm.registerBehavior("CW_SELECT_ALL", {
  "input[id*='select_all_']": {
    "onclick" : cw.selectAll,
    "onchange": cw.selectAll
  }
});

// Behavior modifies form submission behaviour in pageplayer wizard pages
cm.registerBehavior("CW_PAGEPLAYER_NEXT_BUTTON", {
    "input[type='text'],select,input[type='checkbox'],input[type='radio'],input[type='password']": {
        "onkeypress" : function(evt) { 
            if(evt.keyCode == dojo.keys.ENTER) {
                var nextButtons = document.getElementsByName("__o3btn.PLAYER_NEXT");
                if(nextButtons.length > 0) {
                    nextButtons[0].click(); 
                    return false; 
                }
            }
            return true;
        }
    }   
});

cm.registerBehavior("CW_CLOSE_WINDOW", {
  ".bhv-close-window" : {
    "onclick" : function(evt) {
      window.close();
    }
  }
});

cm.registerBehavior("CW_REPLACE_SUBMIT_BTNS", {
  "input.bhv-nav-button" : {
	  "found" : function(node) {
	    var id = node.getAttribute("id");
	    var a = dojo.byId(id + "_a");
	    node.setAttribute("id", id + "_input");
	    a.setAttribute("id", id);
	    dojo.style(node, "display", "none");
	    dojo.style(a, "display", "");
	  }
  }
});

cm.registerBehavior("CW_PROGRAM_SELECT_VALIDATOR", {
  ".bhv-program-container" :{
    "found": function(node){
      var v = cm.validation;
      var inputs = dojo.query("input[type='checkbox']", cm.getParentByClass(node, "list"));

      v.registerValidation(node.getAttribute("name"), doCheck, node);

      function doCheck(){
        if(!dojo.some(inputs, "return item.checked")) {
          v.addMessage(node, "ERR_PROGRAM_NOT_SELECTED", true);
        } else {
          v.removeMessage(node);
        }
      }
      inputs.onclick(doCheck);
      inputs.onblur(doCheck);
    }
  }
});

cm.registerBehavior("CW_LINK_NEW_TARGET", {
  ".bhv-local-nav" : {
    "onclick" : function(evt) {
      evt.target._localNavLink = true;
    }
  },
  "body": {
    "onclick" : function(evt) {
      if(evt.target._localNavLink){return true;}

      var a = evt.target.tagName.toLowerCase() == "a" ?
        evt.target : evt.target.parentNode;
      if (a.tagName.toLowerCase() == "a" && a.getAttribute("href") != null
            && !dojo.hasClass(a, "nav-button")
            && !dojo.hasClass(evt.target, "nav-button")) {
        dojo.stopEvent(evt);
        if (a.getAttribute("href") != "#") {
          window.open(a.href);
        }
        return false;
      }
      return true;
    }
  }
});

// Makes any link with the 'bhv-popup-link' class open in a popup window.
cm.registerBehavior("CW_POPUP_LINK", {
  ".bhv-popup-link" : {
    "onclick" : function(evt) {
      dojo.stopEvent(evt);

      var a = evt.target;
      var width = Number(a.getAttribute("popupWidth") || 800);
      var height = Number(a.getAttribute("popupHeight") || 600);
      var defaultOptions = "location=no, menubar=no, status=no, toolbar=no, "
                         + "scrollbars=yes, resizable=no";
      var offsetLeft = (screen.width - width) / 2;
      var offsetTop = (screen.height - height) / 2;

      height = offsetTop < 0 ? screen.height : height;
      offsetTop = Math.max(0, offsetTop);

      width = offsetLeft < 0 ? screen.width : width;
      offsetLeft = Math.max(0, offsetLeft);

      var left = "left", top = "top";
      if(dojo.isMozilla) {
         left = "screenX", top = "screenY";
      }

      var newWin = window.open(a.href, "name_" + (new Date()).getTime() ,
        'width=' + width + 'px, height=' + height + 'px, ' +
        left + '=' + offsetLeft + ',' + top + '=' + offsetTop + ',' +
            defaultOptions );
      return false;
    }
  }
});

