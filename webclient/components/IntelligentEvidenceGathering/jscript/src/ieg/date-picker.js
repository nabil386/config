/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013,2016. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
dojo.require("dijit.dijit");
dojo.require("dojo.date.locale");
dojo.require("dijit._Calendar");
dojo.require("dojox.widget.Calendar");
dojo.require("dojo.query");

/**
 * IEG internal style date picker.
 */
dojo.declare("cm.DateIcon", dijit._Widget, {
  targetId:"",
  datePattern: "MMM.d.yyyy",
  
  today: "Today",

  constraints: {fullYear: true, selector: "date"},
  
  /**
   * Called after the widget has been created.
   */
  postCreate: function() {
    var _this = this;
    this.target = dojo.byId(this.targetId);

    dojo.connect(this.domNode, "onclick", function(){
      _this[_this._opened ? "_close" : "_open"]();
    });
    
    dojo.connect(this.domNode, "onkeypress", function(evt){
      if(evt.keyCode == dojo.keys.ENTER) {
      	_this[_this._opened ? "_close" : "_open"]();
      }
    });
    
    dojo.connect(this.target, "onfocus", dojo.hitch(this, "_close"));
    
    dojo.subscribe("/anim/toggle", dojo.hitch(this, "_close"));
    
    if (!isExternal()) {
    	
	    dojo.connect(this.domNode, "onmouseover", function() {
			_this.domNode.src = "../servlet/resource?r=calendar-hover.png";
		});
	    
	    dojo.connect(this.domNode, "onmouseout", function() {
			_this.domNode.src = "../servlet/resource?r=calendar.png";
		});
	    
	    dojo.connect(this.domNode, "onfocus", function() {
			_this.domNode.src = "../servlet/resource?r=calendar-hover.png";
		});
	    
	    dojo.connect(this.domNode, "onblur", function() {
	    	_this.domNode.src = "../servlet/resource?r=calendar.png";
		});
	}
  },

  /**
   * Format the date value.
   */
  format: function( value,  constraints){
    //      summary: formats the value as a Date, according to constraints
    if(!value){ return ''; }
    return dojo.date.locale.format(value, 
      constraints ? dojo.mixin(dojo.clone(this.constraints), constraints) 
                  : this.constraints);
  },

  /**
   * Parse the date value.
   */
  parse: function(value,  constraints){
    //      summary: parses the value as a Date, according to constraints
    return dojo.date.locale.parse(value, 
      constraints ? dojo.mixin(dojo.clone(this.constraints), constraints) 
                  : this.constraints) || undefined;
  },

  /**
   * On open of the date widget.
   */
  _open: function(){
    // summary:
    //      opens the TimePicker, and sets the onValueSelected value
    var _this = this;
    if(this.target.disabled == false) {
      var pickerCreated = false;
      if(!this._picker){
        this._picker = new dojox.widget.Calendar({
          today: this.today,
          // focus the textbox before the popup closes to 
          // avoid reopening the popup
          onValueSelected: function(value){
            _this.target.focus(); 
            
            // allow focus time to take
            setTimeout(dojo.hitch(_this, "_close"), 1);        
  
            _this.target.value = 
              _this.format(value, {datePattern: _this.datePattern});
			require(['ieg/dynamic-behaviour'], function(ieg) {
					return ieg.checkExpression(_this.target);
			});
          }
        });
        pickerCreated = true;
      }
      
      if(this.target.value != null && this.target.value != "") {
         try {
           var userDate = 
             this.parse(_this.target.value, {datePattern: _this.datePattern});
           if (userDate) {
             this._picker.attr("value", userDate);
           }
         } catch(e) {
           console.log("error, invalid date", e);
         }
      }
      
      if(!this._opened){
        dijit.popup.open({
          parent: this,
          popup: this._picker,
          around: this.target,
          onCancel: dojo.hitch(this, this._close),
          onClose: function(){ _this._opened=false; }
        });

        this._picker.updateView();

        if(pickerCreated){
          this._picker.startup();
        }
        this._opened=true;
        var popupNode = this._picker.domNode.parentNode;
        var width = dojo.style(popupNode, "width");
        dojo.style(popupNode, "width", (width + 1) + "px");
      }
    }
  },

  /**
   * On close of the date widget.
   */
  _close: function(){
    if(this._opened){
      dijit.popup.close(this._picker);
      this._opened=false;
    }
  }
});

/**
 * Verify if the running application is external. 
 * 
 * @return Boolean True if it is an external application. 
 */
function isExternal() { 
	return dojo.query("body.curam-internal-app").length === 0;
}