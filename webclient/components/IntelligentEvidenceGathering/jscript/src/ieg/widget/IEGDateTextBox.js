/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2021.
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */
/*
 * Modification History
 * --------------------
 * 11-May-2021  BD [RTC270700]  Updated _getDisplayedValueAttr function
 * to return the value.
 */	

define([ "dojo/dom-attr", "dojo/sniff", "dojo/query", 'dojo/ready', 'dojo/on',
        "dijit/form/DateTextBox"
        ], function(domAttr, has, query,  ready, on) {


  /**
   * @name ieg.widget.IEGDateTextBox
   * @namespace Override of the Dojo dijit/form/DateTextBox in order to make the widget
   *              accessible using screen reader.
   * 
   * 
  */
  var IEGDateTextBox = dojo.declare("ieg.widget.IEGDateTextBox", dijit.form.DateTextBox,
  /**
   * @lends ieg.widget.IEGDateTextBox.prototype
  */
  {
	
 _getDisplayedValueAttr: function (){
	this.inherited(arguments);
    if(!has('trident') && dojo.byId(this.id)){
	  var spanHintID = this.domNode.id + "_hint";
	  if(this.textbox.value){
		 domAttr.remove(this.id, 'aria-describedby');
	  }else{
		 domAttr.set(this.id, 'aria-describedby',spanHintID);
	  }
	}
	return this.textbox.value;
	
  },
    
  postCreate : function(){
    var widgetID = this.domNode.id;
    var textBoxID = this.focusNode.id;
    // We have to change role to combobox as Jaws do not work correctly
    // in Edge Chromium when role is listbox. A role of listbox is
    // required for IE11.
    ready(function(){
	if(document.getElementById(widgetID)){
      if (has('trident')) {
	    domAttr.set(widgetID, 'role', 'listbox');  
      }else {
        var selectorWidgetContainsDivSpan = '#'+ widgetID + '> div span.dijitPlaceHolder.dijitInputField';
        domAttr.set(widgetID,  'role', 'combobox'); 
        domAttr.set(widgetID,  'aria-labelledby', textBoxID ); 
        var spanHintID = widgetID + "_hint";
        var nodeSpanSelected = query(selectorWidgetContainsDivSpan); 
        nodeSpanSelected && domAttr.set(nodeSpanSelected[0], 'id', spanHintID);   
      }
    }
	});
    this.inherited(arguments);
  },

  });
  return IEGDateTextBox;
});