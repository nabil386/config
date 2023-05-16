//>>built
require({cache:{"url:curam/widget/templates/CustomMenu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable mobileSelect\" role=\"menu\" tabIndex=\"${tabIndex}\"\n\t   cellspacing=\"0\">\n\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"containerNode\"></tbody>\n</table>","url:curam/widget/templates/Select.html":"<table class=\"dijit dijitReset dijitInline dijitLeft codetable dijitTextBox dijitComboBox\"\n\t     data-dojo-attach-point=\"_buttonNode,tableNode,focusNode,_popupStateNode\"\n\t     cellspacing='0' cellpadding='0'\n\t     role=\"listbox\" aria-haspopup=\"true\" tabIndex=\"0\" aria-label=${title}>\n\t<tbody role=\"presentation\">\n\t  <tr class=\"mobileSelect\" role=\"presentation\">\n\t\t<td class=\"dijitReset dijitStretch dijitButtonContents\" role=\"presentation\">\n\t\t\t<div class=\"dijitReset dijitInputField dijitButtonText\"\n\t\t\t      data-dojo-attach-point=\"containerNode,textDirNode\" role=\"presentation\"></div>\n\t\t\t <div class=\"dijitReset dijitValidationContainer\">\n\t\t\t\t<input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\"\n\t\t\t\t       value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"/>\n\t\t   </div>\n\t\t\t<input class=\"selectHook\" type=\"hidden\" ${!nameAttrSetting} data-dojo-attach-point=\"valueNode\" value=\"${value}\" aria-hidden=\"true\"/>\n\t\t</td>\n\t\t<td class=\"dijitReset dijitRight dijitButtonNode dijitArrowButton\n\t\t             dijitDownArrowButton dijitArrowButtonContainer\" \n\t\t\t    data-dojo-attach-point=\"titleNode\" aria-hidden=\"true\" role=\"presentation\">\n\t\t\t<input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660;\"\n\t\t\t        type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t\t\t\t        ${_buttonInputDisabled} />\n\t\t</td>\n\t</tr>\n  </tbody>\n</table>","url:curam/widget/templates/ReadableSeparator.html":"<tr class=\"dijitMenuSeparator\"\n  data-dojo-attach-point=\"focusNode,containerNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitMenuSeparatorIconCell\" data-dojo-attach-point=\"iconNode\" role=\"presentation\">\n\t\t<div class=\"dijitMenuSeparatorTop\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n\t<td colspan=\"3\" class=\"dijitMenuSeparatorLabelCell\" role=\"presentation\">\n\t\t<div class=\"dijitMenuSeparatorTop dijitMenuSeparatorLabel\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n</tr>\n"}});
define("curam/widget/Select",["dijit/DropDownMenu","dijit/registry","dijit/form/Select","dijit/MenuItem","dijit/MenuSeparator","dojo/aspect","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dojo/on","dojo/query","dojo/text!curam/widget/templates/Select.html","dojo/text!curam/widget/templates/CustomMenu.html","dojo/text!curam/widget/templates/ReadableSeparator.html","curam/widget/_FormSelectWidget"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,on,_f,_10,_11,_12,_13){
var _14=_7("curam._SelectMenu",_1,{autoFocus:true,buildRendering:function(){
this.inherited(arguments);
this.domNode.setAttribute("role","listbox");
},postCreate:function(){
this.inherited(arguments);
var _15=this.parentWidget;
this.own(on(this.domNode,"selectstart",function(evt){
evt.preventDefault();
evt.stopPropagation();
}));
},focus:function(){
var _16=[],val=this.parentWidget.value;
this.focusChild(this.parentWidget._getChildren()[1]);
if(val){
val.length&&val.map&&(val=val[val.length-1]);
_16=this.parentWidget._getChildren().filter(function(_17){
return _17.option&&(val===_17.option.value);
});
_16[0]&&this.focusChild(_16[0],false);
}
!_16[0]&&this.inherited(arguments);
},focusFirst:function(){
this.focusChild(this.parentWidget._getChildren()[1]);
},focusLast:function(){
var chs=this.parentWidget._getChildren(),l=chs.length;
this.focusChild(this.parentWidget._getChildren()[l-2]);
}});
var _18=_7("curam.ReadableSeparator",_4,{templateString:_12,baseClass:"dijitMenuSeparator",parent:null,first:false,last:false,focus:function(){
this.focusNode.focus();
if(this.parent){
this.first&&this.parent.focusLast();
this.last&&this.parent.focusFirst();
}
},_setSelected:function(_19){
},onClick:function(e){
}});
var _1a=_7("curam.widget.Select",[_3,_13],{templateString:_10,blankComment:"<!--__o3_BLANK-->",blankDescription:"No Selection",isBlankVisible:false,goBackReadout:"You are out of the list, click to close or go back",goForwardReadout:"You are out of the list, click to close or go forward",enterKeyOnOpenDropDown:false,postMixInProperties:function(){
if(this.srcNodeRef){
if(!this.store){
if(dojo.query("> option",this.srcNodeRef)[0]==undefined){
this._getMenuItemForOption({label:this.blankComment});
}
}
if(!this.get("store")&&this.srcNodeRef.value==""){
var _1b=dojo.query("> option[value='']",this.srcNodeRef);
if(_1b.length&&_1b[0].innerHTML!=this.blankComment){
this.displayedValue=dojo.trim(_1b[0].innerHTML);
}
}
}
this.inherited(arguments);
},postCreate:function(){
this.own(on(this.focusNode,"keydown",function(e){
var _1c=_2.byId(e.target.id);
if(e.keyCode==dojo.keys.ENTER&&_1c._opened){
_1c.enterKeyOnOpenDropDown=true;
dojo.stopEvent(e);
this.dropDown.focus();
}
}));
_6.after(this,"_loadChildren",function(_1d){
var dd=this.dropDown;
var _1e=new _18({parent:dd,"first":true,"aria-label":this.goForwardReadout});
dd.addChild(_1e,0);
var _1f=new _18({parent:dd,"last":true,"aria-label":this.goBackReadout});
dd.addChild(_1f);
_e.set(_1e.containerNode,"opacity","0");
_e.set(_1f.containerNode,"opacity","0");
});
this.titleNode.setAttribute("tabindex","-1");
this.inherited(arguments);
},_fillContent:function(){
if(!this.options){
this.options=this.srcNodeRef?_f("> *",this.srcNodeRef).map(function(_20){
if(_20.getAttribute("type")==="separator"){
return {value:"",label:"",selected:false,disabled:false};
}
return {value:(_20.getAttribute("data-"+_8._scopeName+"-value")||_20.getAttribute("value")),label:String(_20.innerHTML),selected:_20.getAttribute("selected")||false,disabled:_20.getAttribute("disabled")||false};
},this):[];
}
this.dropDown=new _14({id:this.id+"_menu",parentWidget:this,templateString:_11});
if(!this.value){
this._set("value",this._getValueFromOpts());
}
if(this.options.length&&!this.value&&this.srcNodeRef){
var si=this.srcNodeRef.selectedIndex;
this._set("value",this.options[si>=0?si:0].value);
}
_c.add(this.dropDown.domNode,this.baseClass.replace(/\s+|$/g,"Menu "));
},_getMenuItemForOption:function(_21){
var lbl=_21.label;
var _22=false;
if(lbl==this.blankComment){
lbl=this.blankDescription;
_22=true;
}
var _23=_9.hitch(this,"_setValueAttr",_21);
var _24=new _4({option:_21,label:(this.labelType==="text"?(lbl||"").toString().replace(/&/g,"&amp;").replace(/</g,"&lt;"):lbl)||this.emptyLabel,onClick:_23,ownerDocument:this.ownerDocument,dir:this.dir,textDir:this.textDir,disabled:_21.disabled||false});
_24.focusNode.setAttribute("role","option");
_22&&!this.isBlankVisible&&_e.set(_24.containerNode,"opacity","0");
return _24;
},_setDisplay:function(_25){
var _26=(_25==this.blankComment);
_26&&(_25=this.blankDescription);
var lbl=(this.labelType==="text"?(_25||"").replace(/&/g,"&amp;").replace(/</g,"&lt;"):_25)||this.emptyLabel;
var _27=_d.create("div",{"role":"option","class":"dijitReset dijitInline"+this.baseClass.replace(/\s+|$/g,"Label "),"innerHTML":lbl,"aria-selected":"true","aria-label":this.title+":"+lbl},this.containerNode,"only");
_26&&!this.isBlankVisible&&_e.set(_27,"opacity","0");
this.containerNode.focus();
},loadDropDown:function(_28){
this.inherited(arguments);
this.dropDown.focus();
}});
_1a._Menu=_14;
return _1a;
});
