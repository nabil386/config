/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2014,2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 03-Aug-2021  JH  [RTC 272077] Reset ieg.widget.IEGDateTextBox field when conditional 
 								 cluster is hidden.
 * 10-Jul-2020  JH  [RTC 255481] Reset Curam Filtering Select Widget when it is disabled.
 * 08-Feb-2019  CMC [RTC 225732] Ensured dropdown values within collapsed conditional  
 * 								 clusters are always cleared.
 * 30-Apr-2018  CMC [RTC 225764] Fixing issue with opening nested conditional clusters.
 * 09-Apr-2018  CMC [RTC 225367] Fixing issue with closing nested conditional clusters.
 * 19-Oct-2017  CMC [RTC 211363] Fixing issues with dropdown control questions 
 *                               not opening conditional clusters.
 * 07-Sep-2017  CMC [RTC 206672] Fixing mandatory dropdown issue. 
 * 11-Aug-2017  CMC [RTC 200064] Switching internal IEG to use Curam 
 * 							     Filtering Select. 
 * 26-Apr-2017  AZ  [RTC 193795] Use plain select element on mobile devices.
 * 17-Oct-2014  ROR [CR00447271] Refactored to AMD module.
 */
 
/**
 * Collections of functions related to controlling dynamic behaviour on IEG
 * Player pages such as conditional clusters etc...
 */
define(["dojo/dom", "dojo/dom-attr", "dojo/has", "dojo/sniff", "dojo/NodeList-dom"], 
    function(dom, domAttr, has) {
    // PUBLIC METHODS...
    return {
	  /**
	   * Get the value of an input field or widget.
	   * 
	   * This function gets the value of each of the various input fields and 
	   * widgets. An optional second parameter can be passed in which denotes the
	   * type of field being looked up.
	   * 
	   * TODO: This function needs to be refactored. Maybe split out into  
	   *    internal & external functions.
	   * 
	   * @param name  The name of the field to get the value for.
	   * @param type  (Optional) An optional parameter defining the field type.
	   * 
	   * @return The fields value.
	   */
	  getValue: function(name, type) {
	    meta = dojo.fromJson(dojo.query("input[name='__o3fmeta']")[0].value);
	  
	    if (type == "3fielddate") {
			return getThreeFieldDateValue(name, meta);
		}
				
	    id = "";
	    
	    for (var i in meta) {
	      var index = meta[i][0].indexOf("/" + name);
	      if (index > -1) {
	        if (meta[i][0].substr(index + 1).length == name.length) {
	          id = i;
	          break;
	        }
	      }
	    }
	    
	    var node = dojo.byId(id);
	    var curamFilteringSelect = false;
	    
	    if (type == "codetable" && node === null) {
	    	// When conditional element is a Curam Filtering Select.
	    	
	    	if (dojo.query("select[name='" + id + "']")[0] !== undefined) {
	    		// For initial page load
	    		node = dojo.query("select[name='" + id + "']")[0];
	    		var curamFilteringSelect = true;
	    	}
	    	else if (dojo.query("input[name='" + id + "']")[0] !== undefined) {
	          // For change in drop down value
	    	  var node = dojo.query(
	   		    "input[name='" + id + "']").siblings("input[type=text]");
	    	  node = node[0];
		      var curamFilteringSelect = true;
	    	}  
	    }
	    
	    // If node is null, this is a radio button in external style
	    if (node == null) {
	      var buttons = dojo.query("input[name='" + id + "']");
	      var returnVal = null;
	      for (var j in buttons) {
	        if (buttons[j].checked) {
	          returnVal = buttons[j].value; 
	          break;
	        }
	      }
	      return returnVal;
	    } else if (node.tagName == "INPUT" && node.type == "checkbox") {
	      return node.checked;
	    } else if (node.tagName == "SELECT") {
	      var option = node.options[node.selectedIndex];
	      if (option) {
	        return option.value;
	      }
	    } else if (node.tagName == "INPUT" && node.type == "text") {
	      if (type == "number") {
	        return dojo.number.parse(node.value);
	      } else if (type == "date") {
	        var date =
	          dojo.date.locale.parse(node.value, 
	            {strict:false, selector: "date", 
	              datePattern:dojo.attr(node,"datePattern")});
	        if (date == null || date.getFullYear() < 1000) {
	          date = new Date(0,0,0);
	        }
	        return date;
	      } else if(type == "codetable") {
	        // if the input type is text and the type is "codetable" then
	        // this is a filtering select widget
	    	if (has("ios") || has("android")) {
	    		return dojo.byId(id).value;
	    	} else {
	    		if (curamFilteringSelect == true) {
	    		  // If the conditional node is a Curam Filtering Select element
	    		  var curamFilteringSelectWidget =  dojo.query("input[name='" + id + "']")[0];
	    		  return dojo.attr(curamFilteringSelectWidget, "value");
	    		} else {
	    		  return dijit.byId(id).attr('value');
	    		}
	    	}
	      } else {
	        return node.value;
	      }
	    } else if (node.tagName == "INPUT" && node.type == "radio") {
	      var buttons = dojo.query("input[name='" + id + "']");
	      var returnVal = null;
	      for (var j in buttons) {
	        if (buttons[j].checked) {
	          returnVal = buttons[j].value; 
	          break;
	        }
	      }
	      return returnVal;
	    } else if (node.tagName == "TABLE") {
	      // If the tag name is TABLE, this is an external select widget
	      var nodeValue = dijit.byId(id).attr('value');
	      return nodeValue;
	    }
	    
	    return null;
	  },
	  
	  /**
	   * Check a custom expression.
	   * 
	   * @param target
	   */
	  checkExpression: function(target) {
	    if(target.onchange) {
	      return target.onchange();
	    }
	    return true;
	  },
	  
	  /**
	   * Show/Hide conditional clusters.
	   * 
	   * This function is used to show/hide conditional clusters.
	   * 
	   * @param clusterId   The id of the cluster to show/hide.
	   * @param functionName  
	   */
	  setVisibleCSS: function(clusterId, functionName) {
	      var clusterDiv = dojo.byId(clusterId);
	      try {
	        if (dojo.hitch(null, functionName)()) {
	          dojo.addClass(clusterDiv, "visible");
	          if (dojo.hasClass(clusterDiv, "hidden")) {
	            dojo.removeClass(clusterDiv, "hidden");
	          }
	        } else {
	          dojo.addClass(clusterDiv, "hidden");
	          if (dojo.hasClass(clusterDiv, "visible")) {
	            dojo.removeClass(clusterDiv, "visible");
	          }
	        }
	      } catch (e) {
	        //Do nothing if an error occurs while evaluating func
	      }
	  },
	  
	  /**
	   * Enable read-only questions.
	   * 
	   * This function is used to enable static read-only questions.
	   * 
	   * @param internalId     The internal id of the field to enable.
	   * @param functionName
	   */
	  setStaticReadOnly: function(internalId, functionName) {
	    var internalIdAttr = 'data-ieg-internalid';
	    try {
	      if (dojo.hitch(null, functionName)()) {
	        dojo.query("[" + internalIdAttr + "=" + internalId + "]").
	          forEach("item.setAttribute('disabled', 'disabled');");
	      } else {
	        dojo.query("[" + internalIdAttr + "="+ internalId + "]").
	          forEach("item.removeAttribute('disabled', 0);");
	      }
	    } catch (e) {
	      // Do nothing if an error occurs while evaluating func
	    }
	  },
	  
	   /**
	   * Enable read-only questions.
	   * 
	   * This function is used to enable dynamic read-only questions.
	   * 
	   * @param internalId The internal id of the field to enable.
	   * @param functionName
	   */
	  setDynamicReadOnly: function(node, internalId, direct, func, clear) {
	    var internalIdAttr = 'data-ieg-internalid';
	    var initialValueAttr = 'data-ieg-initialvalue';
	    var typeAttr = 'data-ieg-type';
	    var internalIds;
	    var directControlValues;
	    var functions;
	    var changed = false;
	    
	    if (internalId.indexOf(",") > 0) {
	      internalIds = internalId.split(",");
	      directControlValues = direct.split(",");
	      functions = func.split(",");
	    } else {
	      internalIds = new Array(internalId);
	      directControlValues = new Array(direct);
	      functions = new Array(func);
	    }
	      
	    var i;
	    for (i in internalIds) {
	      internalId = internalIds[i];

	      try {
	        if(bootstrap.isExternalMode()) {
	          
	          var inputFields = dojo.query("[" + internalIdAttr + "=" + internalId + "]");
	           
	          var enable;
	          if (dojo.hitch(null, functions[i])()) {
	            enable = false;
	          } else {
	            enable = true;
	          }
	          
        	  for(var counter = 0; counter < inputFields.length; counter++) {
        		var isDijit = true; // assume that we can find Dijit widgets
  	            var widget = dijit.byId(inputFields[counter].id);
  	            var initialValue;
  	            
  	            if (typeof widget === 'undefined') {
  	            	widget = dojo.byId(inputFields[counter].id);
  	            	initialValue = domAttr.get(widget, initialValueAttr);
  	            	isDijit = false;
  	            } else {
  	            	initialValue = widget.get(initialValueAttr);
  	            }
  	            
  	            if (enable == false) {
  	              var widgetType;
  	              if (isDijit) {
  	            	widget.set("disabled", true);
  	            	widgetType = widget.get('type');
  	              } else {
  	            	widget.disabled = "disabled";
  	            	widgetType = widget.type;
  	              }
  	              
  	              if(widgetType == 'checkbox' || widgetType == 'radio') {
  	                // If the clear flag is set to true, set the initial value to false.
  	                if (clear) {
  	                  initialValue = false;
  	                } else {
  	                  if(initialValue == "true" || initialValue == "checked") {
  	                    initialValue = true;
  	                  } else if (initialValue == "false" || initialValue == "unchecked"){
  	                    initialValue = false;
  	                  }
  	                }
  	                
  	                if (isDijit) {
  	                	widget.set("checked", initialValue);
  	                } else {
  	                	domAttr.set(widget, "checked", initialValue);
  	                }
  	                
  	              } else if(widget.get(typeAttr) == 'dateTextBox') {
  	                if (clear) {
  	                  // TODO: Need to check is calling this function actually resets the 
  	                  // calendar widget to the default date or the original date.
  	                  widget.reset();
  	                } else {
  	                  if (initialValue == '') {
  	                    widget.reset();
  	                  } else {
  	                    widget.set("value", initialValue);
  	                  }
  	                }
  	              } else if(dojo.hasAttr(widget, "value")) {
  	                if (clear) {
  	                  // Cannot call the reset() function here as this is setting the value
  	                  // of the widget back to what it originally was rather than clearing
  	                  // it which is what is required.
  	                  if (isDijit) {
  	                	widget.set("value", '');
  	                  } else {
  	                	domAttr.set(widget, "value", '');
  	                  }
  	                 
  	                } else {
  	                  // The third parameter sets the "priorityChange" param explicitly 
  	                  // to false to prevent the onChange event from firing when the 
  	                  // value of a dropdown is changed programatically
  	                  if (isDijit) {
  	                	widget.set("value", initialValue, false);
  	                  } else {
  	                	widget.value = initialValue;
  	                  }
  	                }
  	              }
  	            } else {
  	              if (isDijit) {
  	            	widget.set("disabled", false);
  	              } else {
  	            	domAttr.remove(widget, "disabled");
  	              }
  	              
  	            }
  	          }
	          
	       } else {
	          if (dojo.hitch(null, functions[i])()) {
	            if (clear) {
	              // disable fields and clearing the value
	              dojo.query("textarea["+ internalIdAttr + "=" + internalId + "]").forEach("item.setAttribute('disabled', 'disabled');");
	              dojo.query("select[" + internalIdAttr + "=" + internalId + "]").forEach("item.setAttribute('disabled', 'disabled');");
	              dojo.query("input[" + internalIdAttr + "=" + internalId + "]").forEach("item.setAttribute('disabled', 'disabled');");
	            } else {
	            	
	              // disable fields and not clearing the value.
	              dojo.query("textarea["+ internalIdAttr + "=" + internalId + "]").forEach("item.value = item.getAttribute('" + initialValueAttr + "'); item.setAttribute('disabled', 'disabled');");
	              dojo.query("select[" + internalIdAttr + "=" + internalId + "]").forEach("item.selectedIndex = item.getAttribute('" + initialValueAttr + "'); item.setAttribute('disabled', 'disabled');");
	              dojo.query("input[" + internalIdAttr + "=" + internalId + "]").forEach("if (item.type=='text'){item.value = item.getAttribute('" + initialValueAttr + "');} else {if (item.getAttribute('" + initialValueAttr + "')=='false'){item.checked = false;} else {item.checked = true;} } item.setAttribute('disabled', 'disabled');" +
	            		  	"var input = dojo.byId(item.id); " + disableCuramFilteringSelectWidget() + _resetCuramFilteringSelectWidget());
		              		
	            }
	          } else {
	            // enable fields
	            dojo.query("[" + internalIdAttr + "=" + internalId + "]").forEach("item.removeAttribute('disabled', 0); " 
	              + enableCuramFilteringSelectWidget());
	         
	            // enable widget fields
	            var inputFields = dojo.query("[" + internalIdAttr + "=" + internalId + "]");
	            var enable;
		        if (dojo.hitch(null, functions[i])()) {
		          enable = false;
		        } else {
		          enable = true;
		        }
		          
		        if (enable ==true){
	        	  for(var counter = 0; counter < inputFields.length; counter++) {
	  	            var widget = dijit.byId(inputFields[counter].id);
	  	            widget.set("disabled", false);
	  	          }
		        }
	          }
	        }
	      } catch (e) {
	        //Do nothing if an error occurs while evaluating func
	      }
	    }
	    dojo.publish("/anim/toggle");
	    return changed;
	  },
	  
	  /**
	   * This function is used to show or hide conditional clusters.
	   * 
	   * param node, is the control that was used.
	   * param clusterId, is the id of the cluster that we are trying to show or hide.
	   * param func is the call back function.
	   */
	   hc: function(node, clusterId, direct, func) {

	      var clusterIds;
	      var directControlValues;
	      var functions;
	      var changed = false;
	      if (clusterId.indexOf(",") > 0) {
	        clusterIds = clusterId.split(",");
	        directControlValues = direct.split(",");
	        functions = func.split(",");
	      } else {
	        clusterIds = new Array(clusterId);
	        directControlValues = new Array(direct);
	        functions = new Array(func);
	      }
	      	      
	      var i;
	      for (i in clusterIds) {
	        clusterId = clusterIds[i];
	        var clusterDiv = dojo.byId(clusterId);
	        
	        try {
	          if (dojo.hitch(null, functions[i])()) {
	            if (directControlValues[i] == "true" && dojo.hasClass(clusterDiv, "hidden")) {
	                dojo.addClass(clusterDiv, "visible");
	                dojo.removeClass(clusterDiv, "hidden");
	                dojo.style(clusterDiv, "height", "1px");
	                dojo.fx.wipeIn({node:clusterDiv}).play();
	                changed = true;
	              }
	          } else if (dojo.hasClass(clusterDiv, "visible")) {
	              this.doWipeOut(clusterDiv);
	              changed = true;
	          }
	        } catch (e) {
	          // Do nothing if an error occurs while evaluating func
	        }
	        
	        dojo.attr(node, "aria-controls", clusterIds[i]);
	      }
	      dojo.publish("/anim/toggle");
	      
	      return changed;
	  },
	  
      doWipeOut: function(node) {
	    dojo.addClass(node, "hidden");
	    dojo.removeClass(node, "visible");
	    dojo.fx.wipeOut({node:node}).play();
	    this.clearClusterInputFields(node);
	  },
	  
      /**
	   * Clear various input fields in hidden clusters.
	   * 
	   * When a conditional cluster is hidden, the input fields contained in the
	   * cluster should be reset.
	   * 
	   * @param clusterDiv  The conditional cluster
	   */
	   clearClusterInputFields: function (clusterDiv) {
	     // external style widgets. in this case do a lookup of all widgets in the
	     // DOM node. If any found widget has a "value" attribute then reset its 
	     // value.
	    if(bootstrap.isExternalMode()) {    
	      var formWidgets = dijit.registry.findWidgets(clusterDiv);
	      dojo.forEach(formWidgets, function(item) {
	        var widgetType = item.get('type');
	        if(widgetType == 'checkbox' || widgetType == 'radio') {
	        item.setAttribute("checked", false);
	      } else if (item.declaredClass == "dijit.form.DateTextBox" || item.declaredClass == "ieg.widget.IEGDateTextBox") {
	        item.reset();
	      } else if(dojo.hasAttr(item, "value")) {
	        item.attr("value", "");
	      }
	    });
	  }
	  // internal style. look up the various input types and reset them. 
	  else {
	    dojo.query("textarea", clusterDiv).
	      forEach("item.value = ''");
	    dojo.query("select", clusterDiv).
	      forEach("item.options[0].selected = true");
	    dojo.query("input[type='text']", clusterDiv).
	      forEach("item.value = '';");
	    dojo.query("input[type='checkbox']", clusterDiv).
	       forEach("item.checked = false;");
	    dojo.query("input[type='radio']", clusterDiv).
	      forEach("item.checked = false;");
	    dojo.query("input[type='hidden']", clusterDiv).
	      forEach("item.value = '';");
	    
	    var clusterWidgets = dijit.registry.findWidgets(clusterDiv);
	    dojo.forEach(clusterWidgets, function(item) {
	      if (item.get('type') == 'text' && 
	        item.id.indexOf('curam_widget_FilteringSelect') != -1) {
	    	// Reset the value in Curam Filtering Select widgets when they are hidden
	    	item["_resetValue"] = "";
	    	item.reset();
			item["data-ieg-initialvalue"] = 0;
			item["value"] = "";
	      }
	    });
	  }
    }  
  };
    
    /**
	 * Dynamically disables a Curam Filtering Select widget
	 * to make it read only.
	 */
  	function disableCuramFilteringSelectWidget() {
  	  var addDisableClassToWidget = 
  		"if (item.id.indexOf('curam_widget_FilteringSelect') != -1) {" +
	      "input.value = item.getAttribute('value'); " +
  		  "var parent = input.parentNode.parentNode; " + 
  		  "dojo.addClass(parent, 'dijitComboBoxDisabled'); " +
		"} ";
  	  return addDisableClassToWidget;
	}
  	
    /**
	 * Dynamically enables a Curam Filtering Select widget
	 * to remove read only behavior.
	 */
  	function enableCuramFilteringSelectWidget() {
      var removeDiasbledClassFromWidget = 
        "if (item.id.indexOf('curam_widget_FilteringSelect') != -1) { " +
    	  "var curamFilteringSelect = item.parentNode.parentNode; " +
    	  "curamFilteringSelect.className = " +
    	    "curamFilteringSelect.className.replace('dijitComboBoxDisabled',''); " + 
        "}"
      return removeDiasbledClassFromWidget;
  	}
  	
  	/**
	 * Reset Curam Filtering Select widget
	 */
    function _resetCuramFilteringSelectWidget() {
      var resetCuramFilteringSelectWidget = 
        "if (item.id.indexOf('curam_widget_FilteringSelect') != -1) { " +
    	  "var enclosingWidget = dijit.registry.getEnclosingWidget(dojo.byId('widget_' + item.id));" +
    	  "if (enclosingWidget){" +
    	    "enclosingWidget.reset();" + 
    	  "}" +
    	"}";
    	return resetCuramFilteringSelectWidget;
    }
  	
	function getThreeFieldDateValue(name, meta) {
	  
		  var year = getOneFieldDate(name, meta, "yy");
		  var month = getOneFieldDate(name, meta, "mm");
		  var day = getOneFieldDate(name, meta, "dd");
		  
		  var date = new Date(year, month - 1, day);
			if (date == null || date.getFullYear() < 1000) {
	          date = new Date(0,0,0);
	        }
	        return date;
	}
	
	function getOneFieldDate(name, meta, fieldPart) {
		var node;
		for (var i in meta) {
			var index = meta[i][0].indexOf("/" + name + "/" + fieldPart);
			if (index > -1) {
				if (meta[i][0].substr(index + 1).length == name.length + 3) {
					node = dojo.byId(i);
					break;
				}
			}
		}
		if (node.tagName == "SELECT") {
	      var option = node.options[node.selectedIndex];
	      if (option) {
	        return option.value;
	      } else {
		    return dayNode.value;
		  }
		} else {
		  //external
			return dojo.query("input[name='" + i + "']")[0].value;
		}
	}
});