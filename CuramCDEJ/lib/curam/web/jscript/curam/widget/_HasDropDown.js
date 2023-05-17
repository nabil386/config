//>>built
define("curam/widget/_HasDropDown",["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","dijit/registry","dijit/focus","dijit/popup","dijit/_FocusMixin","dijit/a11y","dijit/_HasDropDown","curam/util"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,on,_b,_c,_d,_e,_f,_10){
return _1("curam.widget._HasDropDown",[dijit._HasDropDown],{openDropDown:function(){
var _11=this.dropDown,_12=_11.domNode,_13=this._aroundNode||this.domNode,_14=this;
var _15=_e.open({parent:this,popup:_11,around:_13,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
_14.closeDropDown(true);
},onCancel:function(evt){
_14.closeDropDown(evt,true);
},onClose:function(){
_4.set(_14._popupStateNode,"popupActive",false);
_5.remove(_14._popupStateNode,"dijitHasDropDownOpen");
_14._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_13.offsetWidth>_11._popupWrapper.offsetWidth)){
var _16=_13.offsetWidth-_11._popupWrapper.offsetWidth;
var _17={w:_11.domNode.offsetWidth+_16};
this._origStyle=_12.style.cssText;
if(_a.isFunction(_11.resize)){
_11.resize(_17);
}else{
_6.setMarginBox(_12,_17);
}
if(_15.corner[1]=="R"){
_11._popupWrapper.style.left=(_11._popupWrapper.style.left.replace("px","")-_16)+"px";
}
}
_4.set(this._popupStateNode,"popupActive","true");
_5.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_11.id);
if(_12.getAttribute("role")!=="presentation"&&!_12.getAttribute("aria-labelledby")){
_12.setAttribute("aria-labelledby",this.id);
}
return _15;
},closeDropDown:function(evt,_18){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(_18&&this.focus){
var _19=this._getNextFocusableNode(evt,this.ownerDocument,this.focusNode);
if(_19.focus){
_19.focus();
}else{
this.focus();
}
}
_e.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
},_getNextFocusableNode:function(evt,_1a,_1b){
var _1c=_1b;
var _1d=curam.util.getTopmostWindow();
if(evt&&evt.keyCode==_9.TAB){
var _1e=this._findFocusableElementsInDocument(_1a);
var _1f=_1e.length;
var _20=_1e.indexOf(_1b);
if(_20>-1){
if(!evt.shiftKey){
_20++;
var _21=null;
for(var i=_20;i<_1e.length;i++){
if(_1e[i].offsetParent!==null&&!_5.contains(_1e[i],"dijitMenuItem")){
_21=_1e[i];
break;
}
}
if(_21&&_21.nodeName.toLowerCase()!=="iframe"){
_1c=_21;
}else{
if(_21&&_21.nodeName.toLowerCase()==="iframe"){
_21=this._findFocusableElementInIframe(_21,evt.shiftKey);
if(_21){
_1c=_21;
}
}else{
if(this.ownerDocument!==_1d.document){
var _22=this.ownerDocument;
while(_22!==_1d.document&&!_21){
var _23=window.parent;
var _24=_23.document||_23.contentDocument||_23.contentWindow.document;
_21=this._findFocusableElementInParentDocument(_24,evt.shiftKey);
_22=_24;
}
if(_21){
_1c=_21;
}
}
}
}
}else{
_20--;
var _25=null;
for(var i=_20;i>-1;i--){
if(_1e[i].offsetParent!==null){
_25=_1e[i];
break;
}
}
if(_25&&_25.nodeName.toLowerCase()!=="iframe"){
_1c=_25;
}else{
if(_25&&_25.nodeName.toLowerCase()==="iframe"){
_25=this._findFocusableElementInIframe(_25,evt.shiftKey);
if(_25){
_1c=_25;
}
}else{
if(this.ownerDocument!==_1d.document){
var _22=this.ownerDocument;
while(_22!==_1d.document&&!_25){
var _23=window.parent;
var _24=_23.document||_23.contentDocument||_23.contentWindow.document;
_25=this._findFocusableElementInParentDocument(_24,evt.shiftKey);
_22=_24;
}
if(_25){
_1c=_25;
}
}
}
}
}
}
}
return _1c;
},_findFocusableElementsInDocument:function(_26){
var _27=_26.querySelectorAll("button:not(.dijitTabCloseButton), [href], input, select, object, iframe, area, textarea, [tabindex]:not([tabindex=\"-1\"])");
var _28=[];
for(var i=0;i<_27.length;i++){
if(_10._isElementShown(_27[i])){
if(_10.isTabNavigable(_27[i])){
_28.push(_27[i]);
}else{
if(_27[i].nodeName.toLowerCase()==="iframe"){
_28.push(_27[i]);
}
}
}
}
return _28;
},_findFocusableElementInParentDocument:function(_29,_2a){
var _2b=this._findFocusableElementsInDocument(_29);
var _2c=false;
var _2d=0;
var _2e=null;
if(!_2a){
for(var i=0;i<_2b.length;i++){
if(_2b[i].nodeName.toLowerCase()==="iframe"){
if(_2b[i].contentDocument===this.ownerDocument){
_2c=true;
_2d=i;
break;
}
}
}
}else{
for(var i=_2b.length-1;i>-1;i--){
if(_2b[i].nodeName.toLowerCase()==="iframe"){
if(_2b[i].contentDocument===this.ownerDocument){
_2c=true;
_2d=i;
break;
}
}
}
}
if(_2c){
if(!_2a){
_2d++;
for(var i=_2d;i<_2b.length;i++){
if(_2b[i].offsetParent!==null){
_2e=_2b[i];
break;
}
}
}else{
_2d--;
for(var i=_2d;i>-1;i--){
if(_2b[i].offsetParent!==null){
_2e=_2b[i];
break;
}
}
}
if(_2e&&_2e.nodeName.toLowerCase()==="iframe"){
_2e=this._findFocusableElementInIframe(_2e,_2a);
}
}
return _2e;
},_findFocusableElementInIframe:function(_2f,_30){
var _31=_2f;
while(_31&&_31.nodeName.toLowerCase()==="iframe"){
var _32=_31.contentDocument||_31.contentWindow.document;
var _33=this._findFocusableElementsInDocument(_32);
if(_33.length>0){
if(!_30){
indexOfNode=0;
for(var i=indexOfNode;i<_33.length;i++){
if(_33[i].offsetParent!==null){
_31=_33[i];
break;
}
}
}else{
indexOfNode=_33.length-1;
for(var i=indexOfNode;i>-1;i--){
if(_33[i].offsetParent!==null){
_31=_33[i];
break;
}
}
}
}else{
_31=null;
}
}
return _31;
}});
});
