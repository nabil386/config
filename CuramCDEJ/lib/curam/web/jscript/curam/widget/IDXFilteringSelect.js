//>>built
require({cache:{"url:curam/widget/templates/IDXComboBox.html":"<div id=\"widget_${id}\" class=\"dijitReset dijitInline idxComposite\" \n  ><div class=\"idxLabel dijitInline dijitHidden\" dojoAttachPoint=\"labelWrap\"\n    ><span class=\"idxRequiredIcon\">*&nbsp</span\n    ><label for=\"widget_${id}\" dojoAttachPoint=\"compLabelNode\"\n    ></label\n  ></div\n  ><div class=\"dijitInline\"\n    ><div class=\"dijit dijitInline dijitReset dijitInlineTable dijitLeft\" role=\"listbox\" dojoAttachPoint=\"stateNode,oneuiBaseNode,_aroundNode,_popupStateNode\"\n      ><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer' dojoAttachPoint=\"_buttonNode\" role=\"presentation\"\n      ><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n      ${_buttonInputDisabled}\n      /></div\n      ><div class=\"dijitReset dijitInputField dijitInputContainer\" role=\"listbox\" dojoAttachPoint=\"inputContainer\" dojoAttachEvent=\"onmouseenter: _onInputContainerEnter, onmouseleave: _onInputContainerLeave\"\n        ><input class='dijitReset dijitInputInner' ${!nameAttrSetting}  type=\"text\" autocomplete=\"off\" dojoAttachPoint=\"textbox,focusNode\" role=\"textbox\" aria-haspopup=\"true\" \n      /></div\n    ></div\n    ><div class=\"idxUnit dijitInline dijitHidden\" dojoAttachPoint=\"compUnitNode\"\n    ></div\n    ><div class='dijitReset dijitValidationContainer dijitInline' dojoAttachPoint=\"iconNode\"\n      ><div class=\"dijitValidationIcon\"\n      ><input class=\"dijitReset dijitInputField  dijitValidationInner\" value=\"&#935;\" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"/\n    ></div></div\n    ><div class=\"dijitHidden idxHintOutside\" dojoAttachPoint=\"compHintNode\"></div\n  ></div\n></div>"}});
define("curam/widget/IDXFilteringSelect",["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/text!curam/widget/templates/IDXComboBox.html","idx/form/FilteringSelect"],function(_1,_2,on,_3){
var _4=_2("curam.widget.IDXFilteringSelect",idx.oneui.form.FilteringSelect,{templateString:_3,enterKeyOnOpenDropDown:false,postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _5=_1.byNode(dom.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_5._opened){
_5.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
}});
return _4;
});
