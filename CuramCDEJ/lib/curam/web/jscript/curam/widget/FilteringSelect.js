//>>built
define("curam/widget/FilteringSelect",["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/dom-construct","dojo/keys","dojo/sniff","dijit/form/FilteringSelect","curam/widget/ComboBoxMixin"],function(_1,_2,on,_3,_4,_5,_6){
var _7=_2("curam.widget.FilteringSelect",[dijit.form.FilteringSelect,curam.widget.ComboBoxMixin],{enterKeyOnOpenDropDown:false,required:false,postMixInProperties:function(){
if(!this.store){
if(dojo.query("> option",this.srcNodeRef)[0]==undefined){
_4.create("option",{innerHTML:"<!--__o3_BLANK-->"},this.srcNodeRef);
}
}
if(!this.get("store")&&this.srcNodeRef.value==""){
var _8=this.srcNodeRef,_9=dojo.query("> option[value='']",_8);
if(_9.length&&_9[0].innerHTML!="<!--__o3_BLANK-->"){
this.displayedValue=dojo.trim(_9[0].innerHTML);
}
}
this.inherited(arguments);
},postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _a=_1.byNode(_3.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_a._opened){
_a.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
},startup:function(){
if(_6("trident")){
this.domNode.setAttribute("role","listbox");
}
this.inherited(arguments);
},_callbackSetLabel:function(_b,_c,_d,_e){
if((_c&&_c[this.searchAttr]!==this._lastQuery)||(!_c&&_b.length&&this.get("store").getIdentity(_b[0])!=this._lastQuery)){
return;
}
if(!_b.length){
this.set("value","__o3_INVALID",_e||(_e===undefined&&!this.focused),this.textbox.value,null);
}else{
this.set("item",_b[0],_e);
}
},_onKey:function(_f){
if(_f.charCode>=32){
return;
}
var key=_f.charCode||_f.keyCode;
if(key==_5.ALT||key==_5.CTRL||key==_5.META||key==_5.SHIFT){
return;
}
var pw=this.dropDown;
var _10=null;
this._abortQuery();
this.inherited(arguments);
if(_f.altKey||_f.ctrlKey||_f.metaKey){
return;
}
if(this._opened){
_10=pw.getHighlightedOption();
}
switch(key){
case _5.PAGE_DOWN:
case _5.DOWN_ARROW:
case _5.PAGE_UP:
case _5.UP_ARROW:
if(this._opened){
this._announceOption(_10);
}
_f.stopPropagation();
_f.preventDefault();
break;
case _5.ENTER:
if(_10){
if(_10==pw.nextButton){
this._nextSearch(1);
_f.stopPropagation();
_f.preventDefault();
break;
}else{
if(_10==pw.previousButton){
this._nextSearch(-1);
_f.stopPropagation();
_f.preventDefault();
break;
}
}
_f.stopPropagation();
_f.preventDefault();
}else{
this._setBlurValue();
this._setCaretPos(this.focusNode,0);
var _11=this._resetValue;
var _12=this.displayedValue;
if(_11!=_12){
_f.stopPropagation();
_f.preventDefault();
}
}
case _5.TAB:
var _12=this.get("displayedValue");
if(pw&&(_12==pw._messages["previousMessage"]||_12==pw._messages["nextMessage"])){
break;
}
if(_10){
this._selectOption(_10);
}
case _5.ESCAPE:
if(this._opened){
this._lastQuery=null;
this.closeDropDown();
}
break;
}
},_selectOption:function(_13){
this.closeDropDown();
if(_13){
this._announceOption(_13);
}
this._setCaretPos(this.focusNode,0);
this._handleOnChange(this.value,true);
this.focusNode.removeAttribute("aria-activedescendant");
}});
return _7;
});
