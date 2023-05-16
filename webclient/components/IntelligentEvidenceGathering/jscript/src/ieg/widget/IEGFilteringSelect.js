/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2016, 2021.
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */

define(["dojo/dom-construct", "dojo/dom-attr", "dijit/registry", "dojo/on", "dojo/sniff", "dojo/text!ieg/widget/templates/IEGComboBox.html",
        "idx/form/FilteringSelect"
        ], function(domConstruct, domAttr, registry, on, has, template) {


    /**
     * @name ieg.widget.IEGFilteringSelect
     * @namespace Override of the Dojo oneui ComboBox in order to make the oneui Combobox
     *              accessible using screen reader.
     * 
     * 
     */
    var IEGFilteringSelect = dojo.declare("ieg.widget.IEGFilteringSelect", idx.oneui.form.FilteringSelect,
    /**
     * @lends ieg.widget.IEGFilteringSelect.prototype
     */
    {
      templateString: template,

      postCreate : function() {
        // Attached a keyboard event listener so that we can set a flag
        // indicating enter is pressed in open state of dropdown
        
        on(this.focusNode, "click",function(e){
          var widget = registry.byNode(dojo.byId("widget_" + e.target.id)); 
          var dropdownExpand = widget.get('data-ieg-dropdown-expand');
          
          if(dropdownExpand == "true") {
            if(!widget._opened) {
              widget.loadAndOpenDropDown();
            }
          }
        });

        on(this.focusNode, "keydown",function(e){
          var widget = registry.byNode(dojo.byId("widget_" + e.target.id)); 

          if((e.keyCode == dojo.keys.TAB ||e.keyCode == dojo.keys.ENTER) && widget._opened) {
            e.preventDefault();
            widget.focus();
          }
           
        });
       
        this.inherited(arguments);
      },
      
      startup : function(){
        // We have to change role to combobox as Jaws do not work correctly
        // in Edge Chromium when role is listbox. A role of listbox is
        // required for IE11.
        if (!has('trident')) {
          this.stateNode.setAttribute("role", "combobox");
        } else {
          this.stateNode.setAttribute("role", "listbox");
        }
        this.inherited(arguments);
      },
      
      getValueOfFocusedDropdownItem : function() {
        // Returns the text of the item in the drop down menu which has focus.
        var itemInDropDownFocused = dojo.query('.dijitMenuItemSelected')[0];
        if(itemInDropDownFocused !== undefined) {
          var deatailsOfDropdownItems = this.dropDown.items; 
          var focusedItemNum = itemInDropDownFocused.getAttribute("item");
          var textInFocusedDropdownItem = deatailsOfDropdownItems[focusedItemNum].name;
          return textInFocusedDropdownItem;
        }
      },
      _selectOption : function () {
        // Overwriting function so --Please Select-- will not be read
        // when the IEG Filtering Select has a preselected value.
    	var itemInDropDownFocused = this.getValueOfFocusedDropdownItem();
    	var idOfFilteringSelectInfocus = domAttr.get(this.textbox, "id");
    	var idOfSpanContainingHint = idOfFilteringSelectInfocus + "_hint_inside";
    	this.inherited(arguments);
    	
    	if (itemInDropDownFocused != "") {
          domAttr.set(this.textbox, "aria-describedby", "");
    	}
    	else {
    	  domAttr.set(this.textbox, "aria-describedby", idOfSpanContainingHint);
        }
      },
      _announceOption : function() {
        // The value to be read for blank options in the drop down menu is
    	// attached to a span, each time a blank option is focused the span
    	// is added to a hidden aria-live element so its value can be 
    	// accessed by a screen reader.
        this.inherited(arguments);
        
        var textInFocusedDropdownItem = this.getValueOfFocusedDropdownItem();
        var blankNotificationContainer = dojo.query("#blankDropdownNotification")[0];
        if(textInFocusedDropdownItem == "") {
          // Get the property to be read by a screen reader for a blank drop down entry
      	  // and set its value in the aria-live element so it can be read out.
      	  require(["dojo/ready", "curam/util/LocalConfig"], function(ready, localConfig) {
      	    ready(function() {
      	      var textToBeReadForBlankEntry = 
      		    localConfig.readOption("iegConfig_PROP_BLANK_HIDDEN_LABEL_PROP");
		      var blankValueLabelContainer = document.createElement("span"); 
		      blankValueLabelContainer.innerHTML = textToBeReadForBlankEntry;
		      blankNotificationContainer.appendChild(blankValueLabelContainer);
    	    });
    	  });
        }
        else {
          // Remove the element containing the text to be read
          // for a blank drop down entry.
          if (blankNotificationContainer.hasChildNodes()) {
            domConstruct.empty(blankNotificationContainer);
          }
        }
        
        // Dynamically set an aria-activedescendant attribute on the focusNode
        // whose value points at the current selected option in the dropdown.
        // This prevents JAWS from repeating the option value.
        if (!has('trident')) {
          var itemInDropDownFocused = dojo.query('.dijitMenuItemSelected')[0];
          if (itemInDropDownFocused) {
            var itemInDropDownFocusedID = itemInDropDownFocused.getAttribute("id");
            this.focusNode.setAttribute("aria-activedescendant", itemInDropDownFocusedID);
          }
        }
        
      },
      closeDropDown : function() {
        this.inherited(arguments);
    	
    	// Remove element containing the text to be read for a 
    	// blank drop down entry when the widget is closed.
        var blankOptionInDropDown = dojo.query("#blankDropdownNotification")[0];
        if (blankOptionInDropDown.hasChildNodes()) {
        	domConstruct.empty(blankOptionInDropDown); 
    	}
      }
    });
    
    return IEGFilteringSelect;
  });