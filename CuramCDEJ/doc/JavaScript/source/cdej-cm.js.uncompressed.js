require({cache:{
'cm/_base/_behavior':function(){

/*
 * Modification History
 * --------------------
 * 24-Jul-2012 MV  [CR00336202] Stop relying on Dojo nonpublic API.  
 */

define(["dojo/behavior"
        ], function() {
  
  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {
    behaviors: {},
    addedBehaviors: {},

    addBehavior: function(name) {
      // summary: Adds a previously registered behavior to the page.
    //          If the behavior with the given name has not been 
    //          registered, no action is taken.
      var b = cm.behaviors[name];
      
      //Only add any particular behavior once.
      if(b && !cm.addedBehaviors[name]) {
        dojo.behavior.add(b);
        cm.addedBehaviors[name] = true;
        dojo.behavior.apply();
      }
    },
    
    registerBehavior: function(name, behavior) {
      cm.behaviors[name] = behavior;
    }
  });
  
  return cm;
});

},
'cm/_base/_validation':function(){
define([], function() {
  /*
    This file contains validation methods.  These methods do not 
    attach themselves to any particular events.  They are designed
    to be reused by other code, which can invoke them when an event
    occurs.
  */
  
  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {validation: {
    messages: {},
    strings: {},
    validationFns: {},
    
    addLocalizedString: function(id, str){
      cm.validation.strings[id] = str;
    },
    
    getLocalizedString: function(id){ 
      return cm.validation.strings[id] || "";
    },
  
    addMessage: function(inputName, message, isMsgCode){
      inputName = cm.validation._getInputName(inputName);
      var label = cm.getFormItems().getLabel(inputName);
      if(!label){
        return;
      }
      if(isMsgCode) {
        message = cm.validation.getLocalizedString(message);
      }
      cm.validation.messages[inputName] = cm.validation.replaceTokens(message, label);
      dojo.publish(cm.topics.VALIDATION_MSG_ADDED, [{name: inputName, msg: message}]);
    },
    
    removeMessage: function(inputName) {
      inputName = cm.validation._getInputName(inputName);
      if(!cm.getFormItems().getLabel(inputName)){
        return;
      }
      delete cm.validation.messages[inputName];
      dojo.publish(cm.topics.VALIDATION_MSG_REMOVED, [inputName]);
    },
    
    getMessage: function(inputName, refresh) {
      inputName = cm.validation._getInputName(inputName);
      if(refresh) {
        cm.validation._refreshValidation(inputName);
      }
      return cm.validation.messages[inputName];
    },
    
    getAllMessages: function(includeInputs, refresh){
      var msgs = [];
      var allMsgs = cm.validation.messages;
      
      if (refresh) {
        for(var name in allMsgs) {
          cm.validation._refreshValidation(name);
        }
      }
      
      for(var name in allMsgs) {
        if (includeInputs) {
          msgs.push({
            input: dojo.query("input[name='" + name + "'],select[name='" + name + "']")[0],
            msg: allMsgs[name]
          });
        } else {
          msgs.push(allMsgs[name]);
        }
      }
      return msgs;
    },
    
    registerValidation: function(name, fn, node) {
      cm.validation.validationFns[name] = {fn: fn, node: node};
    },
    
    validateMandatory: function(input, type) {
      var n = input;
      var v = cm.validation;
      v._checkMandatory(input);
      switch(type) {
        case "text": 
        case "password":
          if(n._mandatory && (!n.value || dojo.trim(n.value).length == 0)) {
            v.addMessage(n, "MANDATORY_FIELD", true);
          } else {
            v.removeMessage(n);
          }
          break;
        case "radio":
          if(!n._siblings) {
            var siblings = dojo.query("input[name='"+ n.getAttribute("name") +"']");
            
            siblings.forEach(function(input){
              input._siblings = siblings;
            });
          }
          if(n.checked) {
            v.removeMessage(n);
          } else {
            var checked = false;
            n._siblings.forEach(function(input){
              if(input.checked) {
                checked = true;
              }
            });
            if(!checked) {
              v.addMessage(n, "MANDATORY_FIELD", true);
            }
          }
          break;
        case "checkbox":
          if(n._mandatory && !n.checked) {
            v.addMessage(n, "MANDATORY_FIELD", true);
          } else {
            v.removeMessage(n);
          }
          break;
        case "select":
          if(n._mandatory && (!n.value || dojo.trim(n.value).length == 0)) {
            v.addMessage(n, "MANDATORY_FIELD", true);
          } else {
            v.removeMessage(n);
          }
          break;   
      }
    },
    
    replaceTokens: function(str) {
      for(var i = 0; i < arguments.length; i++) {
        tok = "%" + i + "s";
        str = str.split(tok).join('<span class="val-msg">' + arguments[i + 1] + '</span>');
      }
      return str;
    },
    
    _refreshValidation: function(name) {
      var valFn = cm.validation.validationFns[name];
      if(valFn) {
        valFn.fn(valFn.node);
      }
    },
    
    _checkMandatory: function(node) {
      if(typeof(node._mandatory) != "undefined"){return;}
      node._mandatory = cm.getFormItems().isMandatory(node.getAttribute("name"));
    },
    
    _getInputName: function(input) {
      return dojo.isString(input) ? input : input.getAttribute("name");
    }
  }});

  return cm.validation;
});

},
'cm/_base/_pageBehaviors':function(){
define(["cm/_base/_behavior"
        ], function() {

  /*
  This file contains behaviors that can be added to the page
  using the cm.addBehavior function.
  */

  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  cm.registerBehavior("FORM_SINGLE_SUBMIT", {
    "form" : {
      "onsubmit" : function(evt) {
        if (cm.wasFormSubmitted(evt.target)) {
          try {dojo.stopEvent(evt);}catch(e){}
          return false;
        }
        cm.setFormSubmitted(evt.target, true);
      }
    }
  });

  function getVal(type){
    return function(evt) {
      cm.validation.validateMandatory(evt.target ? evt.target : evt, type);
    };
  };

  function getValWithEvts(type, events) {
    var obj = {};
    var fn = getVal(type);
    dojo.forEach(events, function(evt) {
      obj[evt] = fn;
    });
    obj.found = function(node) {
      // Register the validation with the system.
      cm.validation.registerValidation(node.getAttribute("name"), fn, node);
      // Run the validation on each input when the page loads.
      fn(node);
    };
    return obj;
  };

  cm.registerBehavior("MANDATORY_FIELD_VALIDATION", {
    
    "input[type='text'],input[type='password']" :getValWithEvts("text", ["blur", "onkeyup"]),
    
    "input[type='checkbox']" :getValWithEvts("checkbox", ["blur", "onclick"]),
    
    "select": getValWithEvts("select", ["blur", "onchange"]),
    
    "input[type='radio']" : getValWithEvts("radio", ["blur", "onclick"])
  });
  
  return cm;
});

},
'dojo/behavior':function(){
define(["./_base/kernel", "./_base/lang", "./_base/array", "./_base/connect", "./query", "./domReady"],
function(dojo, lang, darray, connect, query, domReady){

// module:
//		dojo/behavior

dojo.deprecated("dojo.behavior", "Use dojo/on with event delegation (on.selector())");

var Behavior = function(){
	// summary:
	//		Deprecated.   dojo/behavior's functionality can be achieved using event delegation using dojo/on
	//		and on.selector().
	// description:
	//		A very simple, lightweight mechanism for applying code to
	//		existing documents, based around `dojo/query` (CSS3 selectors) for node selection,
	//		and a simple two-command API: `add()` and `apply()`;
	//
	//		Behaviors apply to a given page, and are registered following the syntax
	//		options described by `add()` to match nodes to actions, or "behaviors".
	//
	//		Added behaviors are applied to the current DOM when .apply() is called,
	//		matching only new nodes found since .apply() was last called.

	function arrIn(obj, name){
		if(!obj[name]){ obj[name] = []; }
		return obj[name];
	}

	var _inc = 0;

	function forIn(obj, scope, func){
		var tmpObj = {};
		for(var x in obj){
			if(typeof tmpObj[x] == "undefined"){
				if(!func){
					scope(obj[x], x);
				}else{
					func.call(scope, obj[x], x);
				}
			}
		}
	}

	// FIXME: need a better test so we don't exclude nightly Safari's!
	this._behaviors = {};
	this.add = function(/* Object */behaviorObj){
		// summary:
		//		Add the specified behavior to the list of behaviors, ignoring existing
		//		matches.
		// behaviorObj: Object
		//		The behavior object that will be added to behaviors list. The behaviors
		//		in the list will be applied the next time apply() is called.
		// description:
		//		Add the specified behavior to the list of behaviors which will
		//		be applied the next time apply() is called. Calls to add() for
		//		an already existing behavior do not replace the previous rules,
		//		but are instead additive. New nodes which match the rule will
		//		have all add()-ed behaviors applied to them when matched.
		//
		//		The "found" method is a generalized handler that's called as soon
		//		as the node matches the selector. Rules for values that follow also
		//		apply to the "found" key.
		//
		//		The "on*" handlers are attached with `dojo.connect()`, using the
		//		matching node
		//
		//		If the value corresponding to the ID key is a function and not a
		//		list, it's treated as though it was the value of "found".
		//
		//		dojo/behavior.add() can be called any number of times before
		//		the DOM is ready. `dojo/behavior.apply()` is called automatically
		//		by `dojo.addOnLoad`, though can be called to re-apply previously added
		//		behaviors anytime the DOM changes.
		//
		//		There are a variety of formats permitted in the behaviorObject
		//
		// example:
		//		Simple list of properties. "found" is special. "Found" is assumed if
		//		no property object for a given selector, and property is a function.
		//
		//	|	behavior.add({
		//	|		"#id": {
		//	|			"found": function(element){
		//	|				// node match found
		//	|			},
		//	|			"onclick": function(evt){
		//	|				// register onclick handler for found node
		//	|			}
		//	|		},
		// 	|		"#otherid": function(element){
		//	|			// assumes "found" with this syntax
		//	|		}
		//	|	});
		//
		// example:
		//		 If property is a string, a dojo.publish will be issued on the channel:
		//
		//	|	behavior.add({
		//	|		// topic.publish() whenever class="noclick" found on anchors
		//	|		"a.noclick": "/got/newAnchor",
		//	|		"div.wrapper": {
		//	|			"onclick": "/node/wasClicked"
		//	|		}
		//	|	});
		//	|	topic.subscribe("/got/newAnchor", function(node){
		//	|		// handle node finding when dojo/behavior.apply() is called,
		//	|		// provided a newly matched node is found.
		//	|	});
		//
		// example:
		//		Scoping can be accomplished by passing an object as a property to
		//		a connection handle (on*):
		//
		//	|	behavior.add({
		//	|		 	"#id": {
		//	|				// like calling dojo.hitch(foo,"bar"). execute foo.bar() in scope of foo
		//	|				"onmouseenter": { targetObj: foo, targetFunc: "bar" },
		//	|				"onmouseleave": { targetObj: foo, targetFunc: "baz" }
		//	|			}
		//	|	});
		//
		// example:
		//		Behaviors match on CSS3 Selectors, powered by dojo/query. Example selectors:
		//
		//	|	behavior.add({
		//	|		// match all direct descendants
		//	|		"#id4 > *": function(element){
		//	|			// ...
		//	|		},
		//	|
		//	|		// match the first child node that's an element
		//	|		"#id4 > :first-child": { ... },
		//	|
		//	|		// match the last child node that's an element
		//	|		"#id4 > :last-child":  { ... },
		//	|
		//	|		// all elements of type tagname
		//	|		"tagname": {
		//	|			// ...
		//	|		},
		//	|
		//	|		"tagname1 tagname2 tagname3": {
		//	|			// ...
		//	|		},
		//	|
		//	|		".classname": {
		//	|			// ...
		//	|		},
		//	|
		//	|		"tagname.classname": {
		//	|			// ...
		//	|		}
		//	|	});
		//

		forIn(behaviorObj, this, function(behavior, name){
			var tBehavior = arrIn(this._behaviors, name);
			if(typeof tBehavior["id"] != "number"){
				tBehavior.id = _inc++;
			}
			var cversion = [];
			tBehavior.push(cversion);
			if((lang.isString(behavior))||(lang.isFunction(behavior))){
				behavior = { found: behavior };
			}
			forIn(behavior, function(rule, ruleName){
				arrIn(cversion, ruleName).push(rule);
			});
		});
	};

	var _applyToNode = function(node, action, ruleSetName){
		if(lang.isString(action)){
			if(ruleSetName == "found"){
				connect.publish(action, [ node ]);
			}else{
				connect.connect(node, ruleSetName, function(){
					connect.publish(action, arguments);
				});
			}
		}else if(lang.isFunction(action)){
			if(ruleSetName == "found"){
				action(node);
			}else{
				connect.connect(node, ruleSetName, action);
			}
		}
	};

	this.apply = function(){
		// summary:
		//		Applies all currently registered behaviors to the document.
		//
		// description:
		//		Applies all currently registered behaviors to the document,
		//		taking care to ensure that only incremental updates are made
		//		since the last time add() or apply() were called.
		//
		//		If new matching nodes have been added, all rules in a behavior will be
		//		applied to that node. For previously matched nodes, only
		//		behaviors which have been added since the last call to apply()
		//		will be added to the nodes.
		//
		//		apply() is called once automatically by `dojo.addOnLoad`, so
		//		registering behaviors with `dojo/behavior.add()` before the DOM is
		//		ready is acceptable, provided the dojo.behavior module is ready.
		//
		//		Calling appy() manually after manipulating the DOM is required
		//		to rescan the DOM and apply newly .add()ed behaviors, or to match
		//		nodes that match existing behaviors when those nodes are added to
		//		the DOM.
		//
		forIn(this._behaviors, function(tBehavior, id){
			query(id).forEach(
				function(elem){
					var runFrom = 0;
					var bid = "_dj_behavior_"+tBehavior.id;
					if(typeof elem[bid] == "number"){
						runFrom = elem[bid];
						if(runFrom == (tBehavior.length)){
							return;
						}
					}
					// run through the versions, applying newer rules at each step

					for(var x=runFrom, tver; tver = tBehavior[x]; x++){
						forIn(tver, function(ruleSet, ruleSetName){
							if(lang.isArray(ruleSet)){
								darray.forEach(ruleSet, function(action){
									_applyToNode(elem, action, ruleSetName);
								});
							}
						});
					}

					// ensure that re-application only adds new rules to the node
					elem[bid] = tBehavior.length;
				}
			);
		});
	};
};

dojo.behavior = new Behavior();

domReady( function(){ dojo.behavior.apply(); } );

return dojo.behavior;
});

},
'cm/_base/_dom':function(){
define(["dojo/dom", 
        "dojo/dom-style",
        "dojo/dom-class"], function(dom, domStyle, domClass) {
  
/*
  This file includes generic functions for use with the DOM.
*/

/*
 * Modification History
 * --------------------
 * 24-Mar-2010 BD  [CR00191575] Added exit function to getParentByType() when 
 *                              the document root is reached. Handles the 
 *                              iframe scenario.
 */

  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {
    nextSibling: function(node, tagName) {
      //  summary:
            //            Returns the next sibling element matching tagName
      return cm._findSibling(node, tagName, true);
    },
    
    prevSibling: function(node, tagName) {
      //  summary:
            //            Returns the previous sibling element matching tagName
      return cm._findSibling(node, tagName, false);
    },
    
    getInput: function(name, multiple) {
      if(!dojo.isString(name)){
        return name;
      }
      var inputs = dojo.query("input[name='" + name + "'],select[name='" + name + "']");
      return multiple ? (inputs.length > 0 ? inputs : null) 
                                                                                  : (inputs.length > 0 ? inputs[0]:null);
    },
    
    getParentByClass: function(node, classStr) {
      // summary:
      //   Returns the first parent of the node that has the require class
      node = node.parentNode;
      while (node) {
        if(domClass.contains(node, classStr)){
          return node;
        }
        node = node.parentNode;
      }
      return null;
    },
    
    getParentByType: function(node, type) {
      // summary:
      //   Returns the first parent of the node that has the require class
      node = node.parentNode;
      type = type.toLowerCase();
      var docRoot = "html";
      while (node) {
        // Give up when you reach the root of the doc,
        // applies to iframes
        if(node.tagName.toLowerCase() == docRoot){
          break;
        }
        if(node.tagName.toLowerCase() == type){
          return node;
        }
        node = node.parentNode;
      }
      return null;
    },
  
    replaceClass: function(node, newCls, oldCls) {
      // summary:
      //   Replaces a single css class with another.
      //   node:   The node to operate on.
      //   newCls: The class to be added
      //   oldCls: The class to be removed
      domClass.remove(node, oldCls);
      domClass.add(node, newCls);
    },
    
    setClass: function(/* HTMLElement */node, /* string */classStr){
                  //      summary
                  //      Clobbers the existing list of classes for the node, replacing it with
                  //      the list given in the 2nd argument. Returns true or false
                  //      indicating success or failure.
                  node = dom.byId(node);
                  var cs = new String(classStr);
                  try{
                          if(typeof node.className == "string"){
                                  node.className = cs;
                          }else if(node.setAttribute){
                                  node.setAttribute("class", classStr);
                                  node.className = cs;
                          }else{
                                  return false;
                          }
                  }catch(e){
                          dojo.debug("dojo.html.setClass() failed", e);
                  }
                  return true;
          },
  
    _findSibling: function(node, tagName, forward) {
      
      if(!node) { return null; }
      if(tagName) { tagName = tagName.toLowerCase(); }
      var param = forward ? "nextSibling":"previousSibling";
            do {
                    node = node[param];
            } while(node && node.nodeType != 1);
  
            if(node && tagName && tagName != node.tagName.toLowerCase()) {
                    return cm[forward ? "nextSibling":"prevSibling"](node, tagName);
            }
            return node;  //      Element
    },
    
    getViewport: function(){
                  // summary: returns a viewport size (visible part of the window)
          
                  // FIXME: need more docs!!
                  var d = dojo.doc, dd = d.documentElement, w = window, b = dojo.body();
                  if(dojo.isMozilla){
                          return {w: dd.clientWidth, h: w.innerHeight};   // Object
                  }else if(!dojo.isOpera && w.innerWidth){
                          return {w: w.innerWidth, h: w.innerHeight};             // Object
                  }else if (!dojo.isOpera && dd && dd.clientWidth){
                          return {w: dd.clientWidth, h: dd.clientHeight}; // Object
                  }else if (b.clientWidth){
                          return {w: b.clientWidth, h: b.clientHeight};   // Object
                  }
                  return null;    // Object
          },
          
          toggleDisplay: function(node) {
            domStyle.set(node, "display", domStyle.get(node, "display") == "none" ? "": "none");
          },
          
          
          
          endsWith: function(/*string*/str, /*string*/end, /*boolean*/ignoreCase){
                  // summary:
                  //      Returns true if 'str' ends with 'end'
          
                  if(ignoreCase){
                          str = str.toLowerCase();
                          end = end.toLowerCase();
                  }
                  if((str.length - end.length) < 0){
                          return false; // boolean
                  }
                  return str.lastIndexOf(end) == str.length - end.length; // boolean
          },
          
          hide: function(n){
                  domStyle.set(n, "display", "none");
          },
          
          show: function(n){
                  domStyle.set(n, "display", "");
          }
  });
  
  return cm;
});

},
'cm/_base/_topics':function(){
define([], function() {
  
 /*
  This file lists the names of named "topics" that can be subscribed to 
  using dojo.subscribe
 */

  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {topics: {
   // Fired when a form submit has been aborted due to a mandatory
   // field not being filled in.  The application is expected to
   // subscribe to this topic and display the error message in 
   // whatever way it sees fit.  The single argument is an array
   // containing JSON objects that have both the label of the input,
   // and the input itself. e.g.
   // [{label: "First Name", input: [DOM Object]}]
   MANDATORY_FIELD_VALIDATION: "topic_mandatory_field_validation",
   
   // Fired when an input fails a validation check.  The data
   // published is a JSON object, with a name and a message, e.g.
   // {name:'o3id1', msg:'First name must be filled in'}
   VALIDATION_MSG_ADDED: "topic_validation_msg_added",  
   
   // Fired when an input passes a validation check. The name
   // of the input is published as a string.
   VALIDATION_MSG_REMOVED: "topic_validation_msg_removed"
 }});

 return cm.topics;
});

},
'cm/_base/_form':function(){
define([], function() {
  
/*
  This file provides common functions that are used
  to manipulate HTML forms.
*/

  var cm = dojo.global.cm || {};
  dojo.global.cm = cm;

  dojo.mixin(cm, {
  
    checkAll: function(/*Boolean*/value, /*Node*/fromNode) {
      // summary: Sets the check state of one or more checkboxes 
      //           to the value specified. All checkboxes inside the
      //           node 'fromNode' are affected.
      cm.query("input[type='checkbox']", fromNode)
        .forEach("item.checked = " + (value ? "true" : "false"));
    },
    
    setFormSubmitted: function(form, wasSubmitted) {
      // summary: Sets the flag on the form to state whether or not it was 
      //           previously submitted. If any onSubmit handler for a form 
      //           (e.g. validation) cancels the onSubmit event, it should 
      //           call this method, passing false as the second parameter.
      form._alreadySubmitted = wasSubmitted;
    },
  
    wasFormSubmitted: function(form) {
      // summary: returns true if the form was previously submitted, false otherwise.
      return form._alreadySubmitted;
    },
    
    getFormItems: function() {
      // summary: Returns an object that provides access to information on
      //           the form elements on the page. The object contains the
      //           following methods:
      //            length(): returns the number of inputs on the page. Takes
      //                       no parameters
      //            getNames(): returns an array of strings, each element is a
      //                          form element name. Takes no parameters.
      //            getInputs(boolean): returns an array of DOM input elements.
      //                        If the boolean 'true' is passed to the method,
      //                        it only returns inputs that are mandatory.
      //            getTargetPath(index): returns the target path of an input.
      //                        The parameter to the function can be either
      //                        the string name of the input, or an integer index.
      //            getLabel(index): returns the string label for the input.
      //                        The parameter to the function can be either
      //                        the string name of the input, or an integer index.
      //            getDomain(index): returns the data Domain for the input.
      //                        The parameter to the function can be either
      //                        the string name of the input, or an integer index.
      //            isMandatory(index): returns true if the input is mandatory. Otherwise
      //                        false is returned.
      //                        The parameter to the function can be either
      //                        the string name of the input, or an integer index.
      if(cm._formItems) {
        return cm._formItems;
      }
      
      // The form metadata is stored in the input with the name "__o3fmeta"
      var formItemsInput = dojo.query("input[name='__o3fmeta']");
      var data = formItemsInput.length > 0 ? 
                                                  dojo.fromJson(formItemsInput[0].value) : {};
      var names = [];
      for(var x in data) {
        names.push(x);
      }
      
      // Create a function that has child functions.  This accesses the 
      // 'data' and 'names' arrays above.
      cm._formItems = new function() {
        this.length = function(){
          return names.length;
        };
        this.getNames = function(){
          return names;
        };
        // List the inputs on the page. If the mandatory flag is set to true,
        // only mandatory inputs are returned.
        this.getInputs = function(mandatory) {
          var inputs = [];
          dojo.forEach(names, function(name, index){
            if(!mandatory || this.isMandatory(index)){
              inputs.push("[name='" + name + "']");
            } 
          }, this);
          return inputs.length > 0 ? dojo.query(inputs.join(",")) : [];
        };
        function fn(dataIdx) {
          return function(index) {
            var d = data[dojo.isString(index) ? index : names[index]];
            return d ?  d[dataIdx]:null;
          };
        }
        this.getTargetPath = fn(0);
        this.getLabel = fn(1);
        this.getDomain = fn(2);
        this.isMandatory = fn(3);
      };
      return cm._formItems;
    }
  });
  
  return cm;
});

}}});
define("dojo/cdej-cm", [], 1);
