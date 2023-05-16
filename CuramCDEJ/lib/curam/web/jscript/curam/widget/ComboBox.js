//>>built
require({cache:{"url:curam/widget/templates/ComboBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\"\n    role=\"listbox\"\n\taria-haspopup=\"true\"\n\tdata-dojo-attach-point=\"_popupStateNode\"\n\t><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer'\n\t\tdata-dojo-attach-point=\"_buttonNode\" role=\"presentation\"\n\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"button presentation\" aria-hidden=\"true\"\n\t\t\t${_buttonInputDisabled}\n\t/></div\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n    ><div class=\"dijitReset dijitInputField dijitInputContainer\" role=\"listbox\"\n\t\t><input class='dijitReset dijitInputInner' ${!nameAttrSetting} type=\"text\" autocomplete=\"off\"\n\t\t\tdata-dojo-attach-point=\"textbox,focusNode\" role=\"textbox\"\n\t/></div\n></div>\n"}});
define("curam/widget/ComboBox",["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/text!curam/widget/templates/ComboBox.html","dijit/form/ComboBox","curam/widget/ComboBoxMixin"],function(_1,_2,on,_3,_4){
var _5=_2("curam.widget.ComboBox",[dijit.form.ComboBox,curam.widget.ComboBoxMixin],{templateString:_4,enterKeyOnOpenDropDown:false,postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _6=_1.byNode(_3.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_6._opened){
_6.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
}});
return _5;
});
