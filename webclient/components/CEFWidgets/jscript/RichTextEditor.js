
var RichText = {
  doFixes: function(widgetId) {
    RichText.fixBodySize(widgetId);
    if (RichText.fontSizeFixed){return;}

    dojo.widget.Editor2ToolbarFontNameSelect.prototype = dojo.widget.Editor2ToolbarFormatBlockPlainSelect.prototype;

    RichText.setRefreshFn(dojo.widget.Editor2ToolbarFontNameSelect.prototype);
    RichText.setFontSizePrototype();
    RichText.fontSizeFixed = true;
  },

  //Make the inner DIV fit the Iframe, in IE.
  fixBodySize: function(widgetId) {
    if(!dojo.render.html.ie){
      return;
     }
    dojo.addOnLoad(function(){
      var widget = dojo.widget.byId(widgetId);
      if(!widget){
        return;
      }
      dojo.html.setStyle(widget.editNode, "height", "100%");
    });
  },

  setFontSizePrototype: function() {
    var p = dojo.widget.Editor2ToolbarFontSizeSelect.prototype;

    dojo.widget.Editor2ToolbarFontSizeSelect.prototype = dojo.widget.Editor2ToolbarFormatBlockPlainSelect.prototype;
    RichText.setRefreshFn(p);
  },

  //Override the refresh function so that it selects the default option when
  //no style is specified.
  setRefreshFn: function(proto) {
    proto.refreshState = function () {
      if(!this._defaultOption) {
        for(var i = 0; i < this._domNode.options.length; i++) {
	        if(this._domNode.options[i].selected){
	          this._defaultOption = this._domNode.options[i];
	          break;
	        }
	      }
      }
      if (this._domNode) {
         dojo.widget.Editor2ToolbarFormatBlockPlainSelect.superclass.refreshState.call(this);
         var curInst = dojo.widget.Editor2Manager.getCurrentInstance();
         if (curInst) {
             var _command = curInst.getCommand(this._name);
             if (_command) {
                 var format = _command.getValue();
                 if (!format || format == "") {
                     this._defaultOption.selected = true;
                     return;
                 }
                 format = String(format)
                 var item, value;
                 for(var i = 0; i < this._domNode.options.length; i++) {
                   item = this._domNode.options[i];
                   value = String(item.value);
                   if (value.toLowerCase() == format.toLowerCase()) {
                 		 item.selected = true;
                 		 break;
                 	 }
                 }
             }
         }
      }
    }
  }
};