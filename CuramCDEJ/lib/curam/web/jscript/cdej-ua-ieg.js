//>>built
require({cache:{"idx/form/FilteringSelect":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/dom-class","dojo/dom-style","dojo/window","dojo/has","dijit/_WidgetsInTemplateMixin","dijit/form/FilteringSelect","idx/widget/HoverHelpTooltip","./_CompositeMixin","./_CssStateMixin","./_AutoCompleteA11yMixin","idx/has!#mobile?idx/_TemplatePlugableMixin:#platform-plugable?idx/_TemplatePlugableMixin","idx/has!#mobile?idx/PlatformPluginRegistry:#platform-plugable?idx/PlatformPluginRegistry","idx/has!#idx_form_FilteringSelect-desktop?dojo/text!./templates/ComboBox.html"+":#idx_form_FilteringSelect-mobile?"+":#desktop?dojo/text!./templates/ComboBox.html"+":#mobile?"+":dojo/text!./templates/ComboBox.html","idx/has!#idx_form_FilteringSelect-mobile?./plugins/phone/FilteringSelectPlugin"+":#idx_form_FilteringSelect-desktop?"+":#mobile?./plugins/phone/FilteringSelectPlugin"+":"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10){
var _11="idx.form.FilteringSelect";
if(_6("mobile")||_6("platform-plugable")){
_11=_11+"Base";
}
var _12=_2.getObject("idx.oneui.form",true);
_12.FilteringSelect=_1(_11,[_8,_a,_b,_c],{baseClass:"idxFilteringSelectWrap",oneuiBaseClass:"dijitTextBox dijitComboBox",templateString:_f,selectOnClick:true,cssStateNodes:{"_buttonNode":"dijitDownArrowButton"},_setPlaceHolderAttr:function(){
this._placeholder=false;
this.inherited(arguments);
},postCreate:function(){
this.dropDownClass=_8.prototype.dropDownClass;
this.inherited(arguments);
this.domNode.removeAttribute("aria-labelledby");
this.connect(this,"_onFocus",function(){
if(this.message&&this._hasBeenBlurred&&(!this._refocusing)){
this.displayMessage(this.message);
}
});
this._resize();
var _13=/^\s*$/.test(this.label);
if(_13){
this.oneuiBaseNode.removeAttribute("aria-labelledby");
this.compLabelNode.innerHTML="hidden";
}
},refocus:function(){
this._refocusing=true;
this.focus();
this._refocusing=false;
},_isEmpty:function(){
return (/^\s*$/.test(this.textbox.value||""));
},_onBlur:function(){
this.inherited(arguments);
this.validate(this.focused);
},_openResultList:function(_14,_15,_16){
this.inherited(arguments);
var _17=this.dropDown.containerNode.childNodes;
if(!this._lastInput&&this.focusNode.value&&this.dropDown.items){
for(var i=0;i<_17.length;i++){
var _18=this.dropDown.items[_17[i].getAttribute("item")];
if(_18){
var _19=this.store._oldAPI?this.store.getValue(_18,this.searchAttr):_18[this.searchAttr];
_19=_19.toString();
if(_19==this.displayedValue){
this.dropDown._setSelectedAttr(_17[i]);
_5.scrollIntoView(this.dropDown.selected);
break;
}
}
}
}
if(this.item===undefined){
this.validate(true);
}
},_onInputContainerEnter:function(){
_3.toggle(this.oneuiBaseNode,"dijitComboBoxInputContainerHover",true);
},_onInputContainerLeave:function(){
_3.toggle(this.oneuiBaseNode,"dijitComboBoxInputContainerHover",false);
},displayMessage:function(_1a,_1b){
if(_1a){
if(!this.messageTooltip){
this.messageTooltip=new _9({connectId:[this.iconNode],label:_1a,position:this.tooltipPosition,forceFocus:false});
}else{
this.messageTooltip.set("label",_1a);
}
if(this.focused||_1b){
var _1c=_4.get(this.iconNode,"visibility")=="hidden"?this.oneuiBaseNode:this.iconNode;
this.messageTooltip.open(_1c);
}
}else{
this.messageTooltip&&this.messageTooltip.close();
}
}});
if(_6("mobile")||_6("platform-plugable")){
var _1d=_e.register("idx/form/FilteringSelect",{desktop:"inherited",mobile:_10});
var _1e=_12.FilteringSelect.prototype.dropDownClass;
if(_10&&_10.prototype&&_10.prototype.dropDownClass){
_1e=_10.prototype.dropDownClass;
}
_12.FilteringSelect=_1("idx.form.FilteringSelect",[_12.FilteringSelect,_d,_7],{templatePath:require.toUrl("idx/form/templates/ComboBox.html"),pluginRegistry:_1d,inProcessInput:false,postCreate:function(){
this.dropDownClass=_1e;
return this.doWithPlatformPlugin(arguments,"postCreate","postCreate");
},onCloseButtonClick:function(){
return this.doWithPlatformPlugin(arguments,"onCloseButtonClick","onCloseButtonClick");
},onCheckStateChanged:function(_1f,_20){
return this.doWithPlatformPlugin(arguments,"onCheckStateChanged","onCheckStateChanged",_1f,_20);
},loadDropDown:function(){
return this.doWithPlatformPlugin(arguments,"loadDropDown","loadDropDown");
},_onBlur:function(){
return this.doWithPlatformPlugin(arguments,"_onBlur","_onBlur");
},openDropDown:function(){
return this.doWithPlatformPlugin(arguments,"openDropDown","openDropDown");
},closeDropDown:function(){
return this.doWithPlatformPlugin(arguments,"closeDropDown","closeDropDown");
},_createDropDown:function(){
return this.doWithPlatformPlugin(arguments,"_createDropDown","_createDropDown");
},displayMessage:function(_21){
return this.doWithPlatformPlugin(arguments,"displayMessage","displayMessage",_21);
},_setHelpAttr:function(_22){
return this.doWithPlatformPlugin(arguments,"_setHelpAttr","_setHelpAttr",_22);
}});
}
return _12.FilteringSelect;
});
},"dijit/form/TextBox":function(){
define(["dojo/_base/declare","dojo/dom-construct","dojo/dom-style","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/sniff","./_FormValueWidget","./_TextBoxMixin","dojo/text!./templates/TextBox.html","../main"],function(_23,_24,_25,_26,_27,on,has,_28,_29,_2a,_2b){
var _2c=_23("dijit.form.TextBox"+(has("dojo-bidi")?"_NoBidi":""),[_28,_29],{templateString:_2a,_singleNodeTemplate:"<input class=\"dijit dijitReset dijitLeft dijitInputField\" data-dojo-attach-point=\"textbox,focusNode\" autocomplete=\"off\" type=\"${type}\" ${!nameAttrSetting} />",_buttonInputDisabled:has("ie")?"disabled":"",baseClass:"dijitTextBox",postMixInProperties:function(){
var _2d=this.type.toLowerCase();
if(this.templateString&&this.templateString.toLowerCase()=="input"||((_2d=="hidden"||_2d=="file")&&this.templateString==this.constructor.prototype.templateString)){
this.templateString=this._singleNodeTemplate;
}
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
if(has("ie")<9){
this.defer(function(){
try{
var s=_25.getComputedStyle(this.domNode);
if(s){
var ff=s.fontFamily;
if(ff){
var _2e=this.domNode.getElementsByTagName("INPUT");
if(_2e){
for(var i=0;i<_2e.length;i++){
_2e[i].style.fontFamily=ff;
}
}
}
}
}
catch(e){
}
});
}
},_setPlaceHolderAttr:function(v){
this._set("placeHolder",v);
if(!this._phspan){
this._attachPoints.push("_phspan");
this._phspan=_24.create("span",{className:"dijitPlaceHolder dijitInputField"},this.textbox,"after");
this.own(on(this._phspan,"mousedown",function(evt){
evt.preventDefault();
}),on(this._phspan,"touchend, pointerup, MSPointerUp",_27.hitch(this,function(){
this.focus();
})));
}
this._phspan.innerHTML="";
this._phspan.appendChild(this._phspan.ownerDocument.createTextNode(v));
this._updatePlaceHolder();
},_onInput:function(evt){
this.inherited(arguments);
this._updatePlaceHolder();
},_updatePlaceHolder:function(){
if(this._phspan){
this._phspan.style.display=(this.placeHolder&&!this.textbox.value)?"":"none";
}
},_setValueAttr:function(_2f,_30,_31){
this.inherited(arguments);
this._updatePlaceHolder();
},getDisplayedValue:function(){
_26.deprecated(this.declaredClass+"::getDisplayedValue() is deprecated. Use get('displayedValue') instead.","","2.0");
return this.get("displayedValue");
},setDisplayedValue:function(_32){
_26.deprecated(this.declaredClass+"::setDisplayedValue() is deprecated. Use set('displayedValue', ...) instead.","","2.0");
this.set("displayedValue",_32);
},_onBlur:function(e){
if(this.disabled){
return;
}
this.inherited(arguments);
this._updatePlaceHolder();
if(has("mozilla")){
if(this.selectOnClick){
this.textbox.selectionStart=this.textbox.selectionEnd=undefined;
}
}
},_onFocus:function(by){
if(this.disabled||this.readOnly){
return;
}
this.inherited(arguments);
this._updatePlaceHolder();
}});
if(has("ie")<9){
_2c.prototype._isTextSelected=function(){
var _33=this.ownerDocument.selection.createRange();
var _34=_33.parentElement();
return _34==this.textbox&&_33.text.length>0;
};
_2b._setSelectionRange=_29._setSelectionRange=function(_35,_36,_37){
if(_35.createTextRange){
var r=_35.createTextRange();
r.collapse(true);
r.moveStart("character",-99999);
r.moveStart("character",_36);
r.moveEnd("character",_37-_36);
r.select();
}
};
}
if(has("dojo-bidi")){
_2c=_23("dijit.form.TextBox",_2c,{_setPlaceHolderAttr:function(v){
this.inherited(arguments);
this.applyTextDir(this._phspan);
}});
}
return _2c;
});
},"dijit/_base/scroll":function(){
define(["dojo/window","../main"],function(_38,_39){
_39.scrollIntoView=function(_3a,pos){
_38.scrollIntoView(_3a,pos);
};
});
},"dijit/_TemplatedMixin":function(){
define(["dojo/cache","dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/on","dojo/sniff","dojo/string","./_AttachMixin"],function(_3b,_3c,_3d,_3e,on,has,_3f,_40){
var _41=_3c("dijit._TemplatedMixin",_40,{templateString:null,templatePath:null,_skipNodeCache:false,searchContainerNode:true,_stringRepl:function(_42){
var _43=this.declaredClass,_44=this;
return _3f.substitute(_42,this,function(_45,key){
if(key.charAt(0)=="!"){
_45=_3e.getObject(key.substr(1),false,_44);
}
if(typeof _45=="undefined"){
throw new Error(_43+" template:"+key);
}
if(_45==null){
return "";
}
return key.charAt(0)=="!"?_45:this._escapeValue(""+_45);
},this);
},_escapeValue:function(val){
return val.replace(/["'<>&]/g,function(val){
return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;"}[val];
});
},buildRendering:function(){
if(!this._rendered){
if(!this.templateString){
this.templateString=_3b(this.templatePath,{sanitize:true});
}
var _46=_41.getCachedTemplate(this.templateString,this._skipNodeCache,this.ownerDocument);
var _47;
if(_3e.isString(_46)){
_47=_3d.toDom(this._stringRepl(_46),this.ownerDocument);
if(_47.nodeType!=1){
throw new Error("Invalid template: "+_46);
}
}else{
_47=_46.cloneNode(true);
}
this.domNode=_47;
}
this.inherited(arguments);
if(!this._rendered){
this._fillContent(this.srcNodeRef);
}
this._rendered=true;
},_fillContent:function(_48){
var _49=this.containerNode;
if(_48&&_49){
while(_48.hasChildNodes()){
_49.appendChild(_48.firstChild);
}
}
}});
_41._templateCache={};
_41.getCachedTemplate=function(_4a,_4b,doc){
var _4c=_41._templateCache;
var key=_4a;
var _4d=_4c[key];
if(_4d){
try{
if(!_4d.ownerDocument||_4d.ownerDocument==(doc||document)){
return _4d;
}
}
catch(e){
}
_3d.destroy(_4d);
}
_4a=_3f.trim(_4a);
if(_4b||_4a.match(/\$\{([^\}]+)\}/g)){
return (_4c[key]=_4a);
}else{
var _4e=_3d.toDom(_4a,doc);
if(_4e.nodeType!=1){
throw new Error("Invalid template: "+_4a);
}
return (_4c[key]=_4e);
}
};
if(has("ie")){
on(window,"unload",function(){
var _4f=_41._templateCache;
for(var key in _4f){
var _50=_4f[key];
if(typeof _50=="object"){
_3d.destroy(_50);
}
delete _4f[key];
}
});
}
return _41;
});
},"curam/util/UimDialog":function(){
define(["curam/util/RuntimeContext","curam/util/external","curam/util","curam/define","curam/dialog","curam/util/DialogObject"],function(_51,_52){
curam.define.singleton("curam.util.UimDialog",{open:function(_53,_54,_55){
var url=_53+curam.util.makeQueryString(_54);
return this.openUrl(url,_55);
},openUrl:function(url,_56){
var _57=curam.util.getCacheBusterParameter();
var _58=new curam.util.DialogObject(_57);
var _59=null;
if(_56){
_59="width="+_56.width+",height="+_56.height;
}
curam.util.openModalDialog({href:this._addRpu(url)},_59,null,null,_57);
return _58;
},_addRpu:function(url){
var _5a=url;
if(curam.tab.inTabbedUI()){
var _5b=curam.tab.getContentPanelIframe();
if(_5b){
_5a=curam.util.setRpu(url,new _51(_5b.contentWindow));
}
}else{
if(_52.inExternalApp()){
var _5c=_52.getUimParentWindow();
if(_5c){
_5a=curam.util.setRpu(url,new _51(_5c));
}
}
}
return _5a;
},get:function(){
if(curam.dialog._id==null){
throw "Dialog infrastructure not ready.";
}
return new curam.util.DialogObject(null,curam.dialog._id);
},ready:function(_5d){
if(curam.dialog._id==null){
dojo.subscribe("/curam/dialog/ready",_5d);
}else{
_5d();
}
},_getDialogFrameWindow:function(_5e){
var _5f=window.top.dijit.byId(_5e);
return _5f.uimController.getIFrame().contentWindow;
}});
return curam.util.UimDialog;
});
},"curam/util/DialogObject":function(){
define(["dojo/_base/declare","curam/dialog","curam/util"],function(_60){
var _61=_60("curam.util.DialogObject",null,{_id:null,constructor:function(_62,id){
if(!id){
var _63=window.top.dojo.subscribe("/curam/dialog/uim/opened/"+_62,this,function(_64){
this._id=_64;
window.top.dojo.unsubscribe(_63);
});
}else{
this._id=id;
}
},registerBeforeCloseHandler:function(_65){
var _66=window.top.dojo.subscribe("/curam/dialog/BeforeClose",this,function(_67){
if(_67==this._id){
window.top.dojo.unsubscribe(_66);
_65();
}
});
},registerOnDisplayHandler:function(_68){
if(curam.dialog._displayed==true){
_68(curam.dialog._size);
}else{
var ut=window.top.dojo.subscribe("/curam/dialog/displayed",this,function(_69,_6a){
if(_69==this._id){
window.top.dojo.unsubscribe(ut);
_68(_6a);
}
});
}
},close:function(_6b,_6c,_6d){
var win=curam.util.UimDialog._getDialogFrameWindow(this._id);
var _6e=win.curam.dialog.getParentWindow(win);
if(_6b&&!_6c){
win.curam.dialog.forceParentRefresh();
curam.dialog.doRedirect(_6e,null);
}else{
if(_6c){
var _6f=_6c;
if(_6c.indexOf("Page.do")==-1){
_6f=_6c+"Page.do"+curam.util.makeQueryString(_6d);
}
curam.dialog.doRedirect(_6e,_6f);
}
}
curam.dialog.closeModalDialog();
}});
return _61;
});
},"dijit/_CssStateMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/has","dojo/_base/lang","dojo/on","dojo/domReady","dojo/touch","dojo/_base/window","./a11yclick","./registry"],function(_70,_71,dom,_72,has,_73,on,_74,_75,win,_76,_77){
var _78=_71("dijit._CssStateMixin",[],{hovering:false,active:false,_applyAttributes:function(){
this.inherited(arguments);
_70.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","_opened"],function(_79){
this.watch(_79,_73.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes||{}){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.domNode,this.baseClass);
this._setStateClass();
},_cssMouseEvent:function(_7a){
if(!this.disabled){
switch(_7a.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
this._set("hovering",true);
this._set("active",this._mouseDown);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
this._set("hovering",false);
this._set("active",false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
this._set("active",true);
break;
case "mouseup":
case "dojotouchend":
case "MSPointerUp":
case "pointerup":
case "keyup":
this._set("active",false);
break;
}
}
},_setStateClass:function(){
var _7b=this.baseClass.split(" ");
function _7c(_7d){
_7b=_7b.concat(_70.map(_7b,function(c){
return c+_7d;
}),"dijit"+_7d);
};
if(!this.isLeftToRight()){
_7c("Rtl");
}
var _7e=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_7c(_7e);
}
if(this.state){
_7c(this.state);
}
if(this.selected){
_7c("Selected");
}
if(this._opened){
_7c("Opened");
}
if(this.disabled){
_7c("Disabled");
}else{
if(this.readOnly){
_7c("ReadOnly");
}else{
if(this.active){
_7c("Active");
}else{
if(this.hovering){
_7c("Hover");
}
}
}
}
if(this.focused){
_7c("Focused");
}
var tn=this.stateNode||this.domNode,_7f={};
_70.forEach(tn.className.split(" "),function(c){
_7f[c]=true;
});
if("_stateClasses" in this){
_70.forEach(this._stateClasses,function(c){
delete _7f[c];
});
}
_70.forEach(_7b,function(c){
_7f[c]=true;
});
var _80=[];
for(var c in _7f){
_80.push(c);
}
var cls=_80.join(" ");
if(cls!=tn.className){
tn.className=cls;
}
this._stateClasses=_7b;
},_subnodeCssMouseEvent:function(_81,_82,evt){
if(this.disabled||this.readOnly){
return;
}
function _83(_84){
_72.toggle(_81,_82+"Hover",_84);
};
function _85(_86){
_72.toggle(_81,_82+"Active",_86);
};
function _87(_88){
_72.toggle(_81,_82+"Focused",_88);
};
switch(evt.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
_83(true);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
_83(false);
_85(false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
_85(true);
break;
case "mouseup":
case "MSPointerUp":
case "pointerup":
case "dojotouchend":
case "keyup":
_85(false);
break;
case "focus":
case "focusin":
_87(true);
break;
case "blur":
case "focusout":
_87(false);
break;
}
},_trackMouseState:function(_89,_8a){
_89._cssState=_8a;
}});
_74(function(){
function _8b(evt,_8c,_8d){
if(_8d&&dom.isDescendant(_8d,_8c)){
return;
}
for(var _8e=_8c;_8e&&_8e!=_8d;_8e=_8e.parentNode){
if(_8e._cssState){
var _8f=_77.getEnclosingWidget(_8e);
if(_8f){
if(_8e==_8f.domNode){
_8f._cssMouseEvent(evt);
}else{
_8f._subnodeCssMouseEvent(_8e,_8e._cssState,evt);
}
}
}
}
};
var _90=win.body(),_91;
on(_90,_75.over,function(evt){
_8b(evt,evt.target,evt.relatedTarget);
});
on(_90,_75.out,function(evt){
_8b(evt,evt.target,evt.relatedTarget);
});
on(_90,_76.press,function(evt){
_91=evt.target;
_8b(evt,_91);
});
on(_90,_76.release,function(evt){
_8b(evt,_91);
_91=null;
});
on(_90,"focusin, focusout",function(evt){
var _92=evt.target;
if(_92._cssState&&!_92.getAttribute("widgetId")){
var _93=_77.getEnclosingWidget(_92);
if(_93){
_93._subnodeCssMouseEvent(_92,_92._cssState,evt);
}
}
});
});
return _78;
});
},"dijit/layout/ScrollingTabController":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/fx","dojo/_base/lang","dojo/on","dojo/query","dojo/dom-attr","curam/debug","dojo/sniff","../registry","dojo/text!./templates/ScrollingTabController.html","dojo/text!./templates/_ScrollingTabControllerButton.html","./TabController","./utils","../_WidgetsInTemplateMixin","../Menu","../MenuItem","../form/Button","../_HasDropDown","dojo/NodeList-dom","../a11yclick"],function(_94,_95,_96,_97,_98,fx,_99,on,_9a,_9b,_9c,has,_9d,_9e,_9f,_a0,_a1,_a2,_a3,_a4,_a5,_a6){
var _a7=_95("dijit.layout.ScrollingTabController",[_a0,_a2],{baseClass:"dijitTabController dijitScrollingTabController",templateString:_9e,useMenu:true,useSlider:true,tabStripClass:"",_minScroll:5,_setClassAttr:{node:"containerNode",type:"class"},_tabsWidth:-1,_tablistMenuItemIdSuffix:"_stcMi",buildRendering:function(){
this.inherited(arguments);
var n=this.domNode;
this.scrollNode=this.tablistWrapper;
this._initButtons();
if(!this.tabStripClass){
this.tabStripClass="dijitTabContainer"+this.tabPosition.charAt(0).toUpperCase()+this.tabPosition.substr(1).replace(/-.*/,"")+"None";
_96.add(n,"tabStrip-disabled");
}
_96.add(this.tablistWrapper,this.tabStripClass);
},onStartup:function(){
this.inherited(arguments);
this._postStartup=true;
this.own(on(this.containerNode,"attrmodified-label, attrmodified-iconclass",_99.hitch(this,function(evt){
if(this._dim){
this.resize(this._dim);
}
this.bustSizeCache=true;
this._tabsWidth=-1;
evt.detail.widget.domNode._width=0;
})));
},onAddChild:function(_a8,_a9){
this.inherited(arguments);
var _aa=_a8.id;
this.bustSizeCache=true;
this._tabsWidth=-1;
var _ab=function(pid,_ac){
var _ad=null;
if(_ac._menuBtn.dropDown){
var _ae=dojo.query(pid+_ac._tablistMenuItemIdSuffix,_ac._menuBtn.dropDown.domNode)[0];
if(_ae){
_ad=dijit.byNode(_ae);
}
}
return _ad;
};
this.pane2button(_aa).connect(this.pane2button(_aa),"_setCuramVisibleAttr",_99.hitch(this,function(){
var _af=_ab(_aa,this);
if(_af){
this._setCuramVisibility(_af,_aa);
}
}));
this.pane2button(_aa).connect(this.pane2button(_aa),"_setCuramDisabledAttr",_99.hitch(this,function(){
var _b0=_ab(_aa,this);
if(_b0){
this._setCuramAvailability(_b0,_aa);
}
}));
_98.set(this.containerNode,"width",(_98.get(this.containerNode,"width")+200)+"px");
this.containerNode._width=0;
},_setCuramVisibility:function(_b1,_b2){
var _b3=this.pane2button(_b2).curamVisible;
if(_b3){
dojo.replaceClass(_b1.domNode,"visible","hidden");
}else{
dojo.replaceClass(_b1.domNode,"hidden","visible");
}
},_setCuramAvailability:function(_b4,_b5){
var _b6=!this.pane2button(_b5).curamDisabled;
_b4.disabled=!_b6;
if(_b6){
dojo.replaceClass(_b4.domNode,"enabled","disabled");
}else{
dojo.replaceClass(_b4.domNode,"disabled","enabled");
}
},_getNodeWidth:function(_b7){
if(!_b7._width){
_b7._width=_98.get(_b7,"width");
}
return _b7._width;
},destroyRendering:function(_b8){
_94.forEach(this._attachPoints,function(_b9){
delete this[_b9];
},this);
this._attachPoints=[];
_94.forEach(this._attachEvents,this.disconnect,this);
this.attachEvents=[];
},destroy:function(){
if(this._menuBtn){
this._menuBtn._curamOwnerController=null;
}
this.inherited(arguments);
},onRemoveChild:function(_ba,_bb){
var _bc=this.pane2button(_ba.id);
if(this._selectedTab===_bc.domNode){
this._selectedTab=null;
}
this.inherited(arguments);
this.bustSizeCache=true;
this._tabsWidth=-1;
},_initButtons:function(){
this.subscribe("tab.title.name.finished",this._measureBtns);
this._btnWidth=0;
this._buttons=_9a("> .tabStripButton",this.domNode).filter(function(btn){
if((this.useMenu&&btn==this._menuBtn.domNode)||(this.useSlider&&(btn==this._rightBtn.domNode||btn==this._leftBtn.domNode))){
this._btnWidth+=_97.getMarginBoxSimple(btn).w;
_9b.set(this._menuBtn,"title",_9c.getProperty("dijit.layout.ScrollingTabController.navMenu.title"));
_9b.set(this._menuBtn,"role","presentation");
_9b.set(this._menuBtn,"tabindex",0);
return true;
}else{
_98.set(btn,"display","none");
return false;
}
},this);
this._menuBtn._curamOwnerController=this;
},_getTabsWidth:function(){
if(this._tabsWidth>-1){
return this._tabsWidth;
}
var _bd=this.getChildren();
if(_bd.length){
var _be=_bd[this.isLeftToRight()?_bd.length-1:0].domNode;
var _bf=this._getNodeWidth(_be);
if(this.isLeftToRight()){
this._tabsWidth=_be.offsetLeft+_bf;
}else{
var _c0=_bd[_bd.length-1].domNode;
this._tabsWidth=_be.offsetLeft+_bf-_c0.offsetLeft;
}
return this._tabsWidth;
}else{
return 0;
}
},_enableBtn:function(_c1){
var _c2=this._getTabsWidth();
_c1=_c1||_98.get(this.scrollNode,"width");
return _c2>0&&_c1<_c2;
},_measureBtns:function(){
if(this._enableBtn()&&this._rightBtn.domNode.style.display=="none"){
this.resize(this._dim);
if(this.isLeftToRight()){
this._rightBtn.set("disabled",true);
}else{
this._leftBtn.set("disabled",true);
}
}
},resize:function(dim){
if(dojo.query("> *",this.containerNode).length<1){
if(this.domNode.style.height!="1px"){
_98.set(this.domNode,"height","1px");
}
return;
}
if(!this.bustSizeCache&&this._dim&&dim&&this._dim.w==dim.w){
return;
}
this.bustSizeCache=false;
this.scrollNodeHeight=this.scrollNodeHeight||this.scrollNode.offsetHeight;
this._dim=dim;
this.scrollNode.style.height="auto";
var cb=this._contentBox=_a1.marginBox2contentBox(this.domNode,{h:0,w:dim.w});
cb.h=this.scrollNodeHeight;
_97.setContentSize(this.domNode,cb);
var _c3=this._enableBtn(this._contentBox.w);
this._buttons.style("display",_c3?"":"none");
this._leftBtn.region="left";
this._rightBtn.region="right";
this._menuBtn.region=this.isLeftToRight()?"right":"left";
_9b.set(this._leftBtn,"title",_9c.getProperty("dijit.layout.ScrollingTabController.navLeft.title"));
_9b.set(this._rightBtn,"title",_9c.getProperty("dijit.layout.ScrollingTabController.navRight.title"));
_9b.set(this._rightBtn,"role","presentation");
_9b.set(this._leftBtn,"role","presentation");
var _c4;
if(_c3){
_c4=dijit.layout.utils.layoutChildren(this.domNode,this._contentBox,[this._menuBtn,this._leftBtn,this._rightBtn,{domNode:this.scrollNode,layoutAlign:"client",fakeWidget:true}]);
}else{
_c4=dijit.layout.utils.layoutChildren(this.domNode,this._contentBox,[{domNode:this.scrollNode,layoutAlign:"client",fakeWidget:true}]);
}
this.scrollNode._width=_c4.client.w;
if(this._selectedTab){
if(this._anim&&this._anim.status()=="playing"){
this._anim.stop();
}
this.scrollNode.scrollLeft=this._convertToScrollLeft(this._getScrollForSelectedTab());
}
this._setButtonClass(this._getScroll());
this._postResize=true;
return {h:this._contentBox.h,w:dim.w};
},_getScroll:function(){
return (this.isLeftToRight()||has("ie")<8||(has("trident")&&has("quirks"))||has("webkit"))?this.scrollNode.scrollLeft:_98.get(this.containerNode,"width")-_98.get(this.scrollNode,"width")+(has("trident")||has("edge")?-1:1)*this.scrollNode.scrollLeft;
},_convertToScrollLeft:function(val){
if(this.isLeftToRight()||has("ie")<8||(has("trident")&&has("quirks"))||has("webkit")){
return val;
}else{
var _c5=_98.get(this.containerNode,"width")-_98.get(this.scrollNode,"width");
return (has("trident")||has("edge")?-1:1)*(val-_c5);
}
},onSelectChild:function(_c6,_c7){
var tab=this.pane2button(_c6.id);
if(!tab){
return;
}
var _c8=tab.domNode;
if(_c8!=this._selectedTab){
this._selectedTab=_c8;
if(this._postResize){
var _c9=this._getNodeWidth(this.scrollNode);
if(this._getTabsWidth()<_c9){
tab.onClick(null);
tab.focus();
}else{
var sl=this._getScroll();
if(sl>_c8.offsetLeft||sl+_c9<_c8.offsetLeft+this._getNodeWidth(_c8)){
var _ca=this.createSmoothScroll();
if(_c7){
_ca.onEnd=function(){
tab.focus();
};
}
_ca.play();
}else{
if(_c7){
tab.focus();
}
}
}
}
}
this.inherited(arguments);
var _cb=document.activeElement;
if(typeof _cb!=="undefined"&&_cb!=null){
if(_cb.className=="tabLabel"&&(_9a(_cb).closest(".nav-panel")).length>0){
curam.util.setTabButtonClicked(_cb);
}
}
},_getScrollBounds:function(){
var _cc=this.getChildren(),_cd=this._getNodeWidth(this.scrollNode),_ce=this._getNodeWidth(this.containerNode),_cf=_ce-_cd,_d0=this._getTabsWidth();
if(_cc.length&&_d0>_cd){
return {min:this.isLeftToRight()?0:_cc[_cc.length-1].domNode.offsetLeft,max:this.isLeftToRight()?_d0-_cd:_cf};
}else{
var _d1=this.isLeftToRight()?0:_cf;
return {min:_d1,max:_d1};
}
},_getScrollForSelectedTab:function(){
var w=this.scrollNode,n=this._selectedTab,_d2=_98.get(this.scrollNode,"width"),_d3=this._getScrollBounds();
var pos=(n.offsetLeft+_98.get(n,"width")/2)-_d2/2;
pos=Math.min(Math.max(pos,_d3.min),_d3.max);
return pos;
},createSmoothScroll:function(x){
if(arguments.length>0){
var _d4=this._getScrollBounds();
x=Math.min(Math.max(x,_d4.min),_d4.max);
}else{
x=this._getScrollForSelectedTab();
}
if(this._anim&&this._anim.status()=="playing"){
this._anim.stop();
}
var _d5=this,w=this.scrollNode,_d6=new fx.Animation({beforeBegin:function(){
if(this.curve){
delete this.curve;
}
var _d7=w.scrollLeft,_d8=_d5._convertToScrollLeft(x);
_d6.curve=new fx._Line(_d7,_d8);
},onAnimate:function(val){
w.scrollLeft=val;
}});
this._anim=_d6;
this._setButtonClass(x);
return _d6;
},_getBtnNode:function(e){
var n=e.target;
while(n&&!_96.contains(n,"tabStripButton")){
n=n.parentNode;
}
return n;
},doSlideRight:function(e){
this.doSlide(1,this._getBtnNode(e));
},doSlideLeft:function(e){
this.doSlide(-1,this._getBtnNode(e));
},doSlide:function(_d9,_da){
if(_da&&_96.contains(_da,"dijitTabDisabled")){
return;
}
var _db=_98.get(this.scrollNode,"width");
var d=(_db*0.75)*_d9;
var to=this._getScroll()+d;
this._setButtonClass(to);
this.createSmoothScroll(to).play();
},_setButtonClass:function(_dc){
var _dd=this._getScrollBounds();
this._leftBtn.set("disabled",_dc<=_dd.min);
this._rightBtn.set("disabled",_dc>=_dd.max);
}});
var _de=_95("dijit.layout._ScrollingTabControllerButtonMixin",null,{baseClass:"dijitTab tabStripButton",templateString:_9f,tabIndex:"",isFocusable:function(){
return false;
}});
_95("dijit.layout._ScrollingTabControllerButton",[_a5,_de]);
_95("dijit.layout._ScrollingTabControllerMenuButton",[_a5,_a6,_de],{containerId:"",tabIndex:"-1",isLoaded:function(){
return false;
},loadDropDown:function(_df){
this.dropDown=new _a3({id:this.containerId+"_menu",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir});
var _e0=_9d.byId(this.containerId);
_94.forEach(_e0.getChildren(),function(_e1){
var _e2=new _a4({id:_e1.id+"_stcMi",label:_e1.title,iconClass:_e1.iconClass,disabled:_e1.disabled,ownerDocument:this.ownerDocument,dir:_e1.dir,lang:_e1.lang,textDir:_e1.textDir||_e0.textDir,onClick:function(){
_e0.selectChild(_e1);
}});
this.dropDown.addChild(_e2);
},this);
dojo.forEach(this.dropDown.getChildren(),_99.hitch(this,function(_e3){
var _e4=_e3.id.split(this._curamOwnerController._tablistMenuItemIdSuffix)[0];
this._curamOwnerController._setCuramAvailability(_e3,_e4);
this._curamOwnerController._setCuramVisibility(_e3,_e4);
dojo.connect(_e3,"destroy",function(){
setDynState=null;
});
}));
_df();
},closeDropDown:function(_e5){
this.inherited(arguments);
if(this.dropDown){
this._popupStateNode.removeAttribute("aria-owns");
this.dropDown.destroyRecursive();
delete this.dropDown;
}
}});
return _a7;
});
},"dijit/place":function(){
define(["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","./Viewport","./main"],function(_e6,_e7,_e8,_e9,win,_ea,_eb){
function _ec(_ed,_ee,_ef,_f0){
var _f1=_ea.getEffectiveBox(_ed.ownerDocument);
if(!_ed.parentNode||String(_ed.parentNode.tagName).toLowerCase()!="body"){
win.body(_ed.ownerDocument).appendChild(_ed);
}
var _f2=null;
_e6.some(_ee,function(_f3){
var _f4=_f3.corner;
var pos=_f3.pos;
var _f5=0;
var _f6={w:{"L":_f1.l+_f1.w-pos.x,"R":pos.x-_f1.l,"M":_f1.w}[_f4.charAt(1)],h:{"T":_f1.t+_f1.h-pos.y,"B":pos.y-_f1.t,"M":_f1.h}[_f4.charAt(0)]};
var s=_ed.style;
s.left=s.right="auto";
if(_ef){
var res=_ef(_ed,_f3.aroundCorner,_f4,_f6,_f0);
_f5=typeof res=="undefined"?0:res;
}
var _f7=_ed.style;
var _f8=_f7.display;
var _f9=_f7.visibility;
if(_f7.display=="none"){
_f7.visibility="hidden";
_f7.display="";
}
var bb=_e7.position(_ed);
_f7.display=_f8;
_f7.visibility=_f9;
var _fa={"L":pos.x,"R":pos.x-bb.w,"M":Math.max(_f1.l,Math.min(_f1.l+_f1.w,pos.x+(bb.w>>1))-bb.w)}[_f4.charAt(1)],_fb={"T":pos.y,"B":pos.y-bb.h,"M":Math.max(_f1.t,Math.min(_f1.t+_f1.h,pos.y+(bb.h>>1))-bb.h)}[_f4.charAt(0)],_fc=Math.max(_f1.l,_fa),_fd=Math.max(_f1.t,_fb),_fe=Math.min(_f1.l+_f1.w,_fa+bb.w),_ff=Math.min(_f1.t+_f1.h,_fb+bb.h),_100=_fe-_fc,_101=_ff-_fd;
_f5+=(bb.w-_100)+(bb.h-_101);
if(_f2==null||_f5<_f2.overflow){
_f2={corner:_f4,aroundCorner:_f3.aroundCorner,x:_fc,y:_fd,w:_100,h:_101,overflow:_f5,spaceAvailable:_f6};
}
return !_f5;
});
if(_f2.overflow&&_ef){
_ef(_ed,_f2.aroundCorner,_f2.corner,_f2.spaceAvailable,_f0);
}
var top=_f2.y,side=_f2.x,body=win.body(_ed.ownerDocument);
if(/relative|absolute/.test(_e8.get(body,"position"))){
top-=_e8.get(body,"marginTop");
side-=_e8.get(body,"marginLeft");
}
var s=_ed.style;
s.top=top+"px";
s.left=side+"px";
s.right="auto";
return _f2;
};
var _102={"TL":"BR","TR":"BL","BL":"TR","BR":"TL"};
var _103={at:function(node,pos,_104,_105,_106){
var _107=_e6.map(_104,function(_108){
var c={corner:_108,aroundCorner:_102[_108],pos:{x:pos.x,y:pos.y}};
if(_105){
c.pos.x+=_108.charAt(1)=="L"?_105.x:-_105.x;
c.pos.y+=_108.charAt(0)=="T"?_105.y:-_105.y;
}
return c;
});
return _ec(node,_107,_106);
},around:function(node,_109,_10a,_10b,_10c){
var _10d;
if(typeof _109=="string"||"offsetWidth" in _109||"ownerSVGElement" in _109){
_10d=_e7.position(_109,true);
if(/^(above|below)/.test(_10a[0])){
var _10e=_e7.getBorderExtents(_109),_10f=_109.firstChild?_e7.getBorderExtents(_109.firstChild):{t:0,l:0,b:0,r:0},_110=_e7.getBorderExtents(node),_111=node.firstChild?_e7.getBorderExtents(node.firstChild):{t:0,l:0,b:0,r:0};
_10d.y+=Math.min(_10e.t+_10f.t,_110.t+_111.t);
_10d.h-=Math.min(_10e.t+_10f.t,_110.t+_111.t)+Math.min(_10e.b+_10f.b,_110.b+_111.b);
}
}else{
_10d=_109;
}
if(_109.parentNode){
var _112=_e8.getComputedStyle(_109).position=="absolute";
var _113=_109.parentNode;
while(_113&&_113.nodeType==1&&_113.nodeName!="BODY"){
var _114=_e7.position(_113,true),pcs=_e8.getComputedStyle(_113);
if(/relative|absolute/.test(pcs.position)){
_112=false;
}
if(!_112&&/hidden|auto|scroll/.test(pcs.overflow)){
var _115=Math.min(_10d.y+_10d.h,_114.y+_114.h);
var _116=Math.min(_10d.x+_10d.w,_114.x+_114.w);
_10d.x=Math.max(_10d.x,_114.x);
_10d.y=Math.max(_10d.y,_114.y);
_10d.h=_115-_10d.y;
_10d.w=_116-_10d.x;
}
if(pcs.position=="absolute"){
_112=true;
}
_113=_113.parentNode;
}
}
var x=_10d.x,y=_10d.y,_117="w" in _10d?_10d.w:(_10d.w=_10d.width),_118="h" in _10d?_10d.h:(_e9.deprecated("place.around: dijit/place.__Rectangle: { x:"+x+", y:"+y+", height:"+_10d.height+", width:"+_117+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_10d.height+", w:"+_117+" }","","2.0"),_10d.h=_10d.height);
var _119=[];
function push(_11a,_11b){
_119.push({aroundCorner:_11a,corner:_11b,pos:{x:{"L":x,"R":x+_117,"M":x+(_117>>1)}[_11a.charAt(1)],y:{"T":y,"B":y+_118,"M":y+(_118>>1)}[_11a.charAt(0)]}});
};
_e6.forEach(_10a,function(pos){
var ltr=_10b;
switch(pos){
case "above-centered":
push("TM","BM");
break;
case "below-centered":
push("BM","TM");
break;
case "after-centered":
ltr=!ltr;
case "before-centered":
push(ltr?"ML":"MR",ltr?"MR":"ML");
break;
case "after":
ltr=!ltr;
case "before":
push(ltr?"TL":"TR",ltr?"TR":"TL");
push(ltr?"BL":"BR",ltr?"BR":"BL");
break;
case "below-alt":
ltr=!ltr;
case "below":
push(ltr?"BL":"BR",ltr?"TL":"TR");
push(ltr?"BR":"BL",ltr?"TR":"TL");
break;
case "above-alt":
ltr=!ltr;
case "above":
push(ltr?"TL":"TR",ltr?"BL":"BR");
push(ltr?"TR":"TL",ltr?"BR":"BL");
break;
default:
push(pos.aroundCorner,pos.corner);
}
});
var _11c=_ec(node,_119,_10c,{w:_117,h:_118});
_11c.aroundNodePos=_10d;
return _11c;
}};
return _eb.place=_103;
});
},"dijit/_HasDropDown":function(){
define(["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","./registry","./focus","./popup","./_FocusMixin"],function(_11d,_11e,dom,_11f,_120,_121,_122,has,keys,lang,on,_123,_124,_125,_126,_127){
return _11d("dijit._HasDropDown",_127,{_buttonNode:null,_arrowWrapperNode:null,_popupStateNode:null,_aroundNode:null,dropDown:null,autoWidth:true,forceWidth:false,maxHeight:-1,dropDownPosition:["below","above"],_stopClickEvents:true,_onDropDownMouseDown:function(e){
if(this.disabled||this.readOnly){
return;
}
if(e.type!="MSPointerDown"&&e.type!="pointerdown"){
e.preventDefault();
}
this.own(on.once(this.ownerDocument,_123.release,lang.hitch(this,"_onDropDownMouseUp")));
this.toggleDropDown();
},_onDropDownMouseUp:function(e){
var _128=this.dropDown,_129=false;
if(e&&this._opened){
var c=_121.position(this._buttonNode,true);
if(!(e.pageX>=c.x&&e.pageX<=c.x+c.w)||!(e.pageY>=c.y&&e.pageY<=c.y+c.h)){
var t=e.target;
while(t&&!_129){
if(_120.contains(t,"dijitPopup")){
_129=true;
}else{
t=t.parentNode;
}
}
if(_129){
t=e.target;
if(_128.onItemClick){
var _12a;
while(t&&!(_12a=_124.byNode(t))){
t=t.parentNode;
}
if(_12a&&_12a.onClick&&_12a.getParent){
_12a.getParent().onItemClick(_12a,e);
}
}
return;
}
}
}
if(this._opened){
if(_128.focus&&(_128.autoFocus!==false||(e.type=="mouseup"&&!this.hovering))){
this._focusDropDownTimer=this.defer(function(){
_128.focus();
delete this._focusDropDownTimer;
});
}
}else{
if(this.focus){
this.defer("focus");
}
}
},_onDropDownClick:function(e){
if(this._stopClickEvents){
e.stopPropagation();
e.preventDefault();
}
},buildRendering:function(){
this.inherited(arguments);
this._buttonNode=this._buttonNode||this.focusNode||this.domNode;
this._popupStateNode=this._popupStateNode||this.focusNode||this._buttonNode;
var _12b={"after":this.isLeftToRight()?"Right":"Left","before":this.isLeftToRight()?"Left":"Right","above":"Up","below":"Down","left":"Left","right":"Right"}[this.dropDownPosition[0]]||this.dropDownPosition[0]||"Down";
_120.add(this._arrowWrapperNode||this._buttonNode,"dijit"+_12b+"ArrowButton");
},postCreate:function(){
this.inherited(arguments);
var _12c=this.focusNode||this.domNode;
this.own(on(this._buttonNode,_123.press,lang.hitch(this,"_onDropDownMouseDown")),on(this._buttonNode,"click",lang.hitch(this,"_onDropDownClick")),on(_12c,"keydown",lang.hitch(this,"_onKey")),on(_12c,"keyup",lang.hitch(this,"_onKeyUp")));
},destroy:function(){
if(this._opened){
this.closeDropDown(true);
}
if(this.dropDown){
if(!this.dropDown._destroyed){
this.dropDown.destroyRecursive();
}
delete this.dropDown;
}
this.inherited(arguments);
},_onKey:function(e){
if(this.disabled||this.readOnly){
return;
}
var d=this.dropDown,_12d=e.target;
if(d&&this._opened&&d.handleKey){
if(d.handleKey(e)===false){
e.stopPropagation();
e.preventDefault();
return;
}
}
if(d&&this._opened&&e.keyCode==keys.ESCAPE){
this.closeDropDown();
e.stopPropagation();
e.preventDefault();
}else{
if(!this._opened&&(e.keyCode==keys.DOWN_ARROW||((e.keyCode==keys.ENTER||(e.keyCode==keys.SPACE&&(!this._searchTimer||(e.ctrlKey||e.altKey||e.metaKey))))&&((_12d.tagName||"").toLowerCase()!=="input"||(_12d.type&&_12d.type.toLowerCase()!=="text"))))){
this._toggleOnKeyUp=true;
e.stopPropagation();
e.preventDefault();
}
}
},_onKeyUp:function(){
if(this._toggleOnKeyUp){
delete this._toggleOnKeyUp;
this.toggleDropDown();
var d=this.dropDown;
if(d&&d.focus){
this.defer(lang.hitch(d,"focus"),1);
}
}
},_onBlur:function(){
this.closeDropDown(false);
this.inherited(arguments);
},isLoaded:function(){
return true;
},loadDropDown:function(_12e){
_12e();
},loadAndOpenDropDown:function(){
var d=new _11e(),_12f=lang.hitch(this,function(){
this.openDropDown();
d.resolve(this.dropDown);
});
if(!this.isLoaded()){
this.loadDropDown(_12f);
}else{
_12f();
}
return d;
},toggleDropDown:function(){
if(this.disabled||this.readOnly){
return;
}
if(!this._opened){
this.loadAndOpenDropDown();
}else{
this.closeDropDown(true);
}
},openDropDown:function(){
var _130=this.dropDown,_131=_130.domNode,_132=this._aroundNode||this.domNode,self=this;
var _133=_126.open({parent:this,popup:_130,around:_132,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
self.closeDropDown(true);
},onCancel:function(){
self.closeDropDown(true);
},onClose:function(){
_11f.set(self._popupStateNode,"popupActive",false);
_120.remove(self._popupStateNode,"dijitHasDropDownOpen");
self._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_132.offsetWidth>_130._popupWrapper.offsetWidth)){
var _134=_132.offsetWidth-_130._popupWrapper.offsetWidth;
var _135={w:_130.domNode.offsetWidth+_134};
this._origStyle=_131.style.cssText;
if(lang.isFunction(_130.resize)){
_130.resize(_135);
}else{
_121.setMarginBox(_131,_135);
}
if(_133.corner[1]=="R"){
_130._popupWrapper.style.left=(_130._popupWrapper.style.left.replace("px","")-_134)+"px";
}
}
_11f.set(this._popupStateNode,"popupActive","true");
_120.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_130.id);
if(_131.getAttribute("role")!=="presentation"&&!_131.getAttribute("aria-labelledby")){
_131.setAttribute("aria-labelledby",this.id);
}
return _133;
},closeDropDown:function(_136){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(_136&&this.focus){
this.focus();
}
_126.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
}});
});
},"curam/util/Request":function(){
define(["dojo/_base/xhr","curam/debug","curam/util/ResourceBundle","curam/inspection/Layer","curam/util/LocalConfig"],function(xhr,_137,_138,_139,_13a){
var _13b=new _138("curam.application.Request");
_isLoginPage=null,isLoginPage=function(_13c){
if(_isLoginPage){
return _isLoginPage(_13c);
}else{
return _13c.responseText.indexOf("action=\"j_security_check\"")>0;
}
},errorDisplayHookpoint=function(err,_13d){
if(isLoginPage(_13d.xhr)){
_137.log(_13b.getProperty("sessionExpired"));
}else{
_137.log(_13b.getProperty("ajaxError"));
}
_137.log(err);
_137.log("HTTP status was: "+_13d.xhr.status);
},_xhr=function(_13e,args){
var _13f=_13a.readOption("ajaxDebugMode","false")=="true";
var _140=args.error;
if(_13f){
args.error=function(err,_141){
if(args.errorHandlerOverrideDefault!==true){
errorDisplayHookpoint(err,_141);
}
if(_140){
_140(err,_141);
}
};
}
var _142=_13e(args);
return _142;
};
var _143={post:function(args){
return _xhr(xhr.post,args);
},get:function(args){
return _xhr(xhr.get,args);
},setLoginPageDetector:function(_144){
_isLoginPage=_144;
},checkLoginPage:function(args){
return isLoginPage(args);
}};
_139.register("curam/util/Request",_143);
return _143;
});
},"dijit/_MenuBase":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/lang","dojo/mouse","dojo/on","dojo/window","./a11yclick","./registry","./_Widget","./_CssStateMixin","./_KeyNavContainer","./_TemplatedMixin"],function(_145,_146,dom,_147,_148,lang,_149,on,_14a,_14b,_14c,_14d,_14e,_14f,_150){
return _146("dijit._MenuBase",[_14d,_150,_14f,_14e],{selected:null,_setSelectedAttr:function(item){
if(this.selected!=item){
if(this.selected){
this.selected._setSelected(false);
this._onChildDeselect(this.selected);
}
if(item){
item._setSelected(true);
}
this._set("selected",item);
}
},activated:false,_setActivatedAttr:function(val){
_148.toggle(this.domNode,"dijitMenuActive",val);
_148.toggle(this.domNode,"dijitMenuPassive",!val);
this._set("activated",val);
},parentMenu:null,popupDelay:500,passivePopupDelay:Infinity,autoFocus:false,childSelector:function(node){
var _151=_14c.byNode(node);
return node.parentNode==this.containerNode&&_151&&_151.focus;
},postCreate:function(){
var self=this,_152=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.containerNode,on.selector(_152,_149.enter),function(){
self.onItemHover(_14c.byNode(this));
}),on(this.containerNode,on.selector(_152,_149.leave),function(){
self.onItemUnhover(_14c.byNode(this));
}),on(this.containerNode,on.selector(_152,_14b),function(evt){
self.onItemClick(_14c.byNode(this),evt);
evt.stopPropagation();
}),on(this.containerNode,on.selector(_152,"focusin"),function(){
self._onItemFocus(_14c.byNode(this));
}));
this.inherited(arguments);
},onKeyboardSearch:function(item,evt,_153,_154){
this.inherited(arguments);
if(!!item&&(_154==-1||(!!item.popup&&_154==1))){
this.onItemClick(item,evt);
}
},_keyboardSearchCompare:function(item,_155){
if(!!item.shortcutKey){
return _155==item.shortcutKey.toLowerCase()?-1:0;
}
return this.inherited(arguments)?1:0;
},onExecute:function(){
},onCancel:function(){
},_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}else{
var _156=this._getTopMenu();
if(_156&&_156._isMenuBar){
_156.focusNext();
}
}
},_onPopupHover:function(){
this.set("selected",this.currentPopupItem);
this._stopPendingCloseTimer();
},onItemHover:function(item){
if(this.activated){
this.set("selected",item);
if(item.popup&&!item.disabled&&!this.hover_timer){
this.hover_timer=this.defer(function(){
this._openItemPopup(item);
},this.popupDelay);
}
}else{
if(this.passivePopupDelay<Infinity){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.passive_hover_timer=this.defer(function(){
this.onItemClick(item,{type:"click"});
},this.passivePopupDelay);
}
}
this._hoveredChild=item;
item._set("hovering",true);
},_onChildDeselect:function(item){
this._stopPopupTimer();
if(this.currentPopupItem==item){
this._stopPendingCloseTimer();
this._pendingClose_timer=this.defer(function(){
this._pendingClose_timer=null;
this.currentPopupItem=null;
item._closePopup();
},this.popupDelay);
}
},onItemUnhover:function(item){
if(this._hoveredChild==item){
this._hoveredChild=null;
}
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
this.passive_hover_timer=null;
}
item._set("hovering",false);
},_stopPopupTimer:function(){
if(this.hover_timer){
this.hover_timer=this.hover_timer.remove();
}
},_stopPendingCloseTimer:function(){
if(this._pendingClose_timer){
this._pendingClose_timer=this._pendingClose_timer.remove();
}
},_getTopMenu:function(){
for(var top=this;top.parentMenu;top=top.parentMenu){
}
return top;
},onItemClick:function(item,evt){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.focusChild(item);
if(item.disabled){
return false;
}
if(item.popup){
this.set("selected",item);
this.set("activated",true);
var _157=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
this._openItemPopup(item,_157);
}else{
this.onExecute();
item._onClick?item._onClick(evt):item.onClick(evt);
}
},_openItemPopup:function(_158,_159){
if(_158==this.currentPopupItem){
return;
}
if(this.currentPopupItem){
this._stopPendingCloseTimer();
this.currentPopupItem._closePopup();
}
this._stopPopupTimer();
var _15a=_158.popup;
_15a.parentMenu=this;
this.own(this._mouseoverHandle=on.once(_15a.domNode,"mouseover",lang.hitch(this,"_onPopupHover")));
var self=this;
_158._openPopup({parent:this,orient:this._orient||["after","before"],onCancel:function(){
if(_159){
self.focusChild(_158);
}
self._cleanUp();
},onExecute:lang.hitch(this,"_cleanUp",true),onClose:function(){
if(self._mouseoverHandle){
self._mouseoverHandle.remove();
delete self._mouseoverHandle;
}
}},_159);
this.currentPopupItem=_158;
},onOpen:function(){
this.isShowingNow=true;
this.set("activated",true);
},onClose:function(){
this.set("activated",false);
this.set("selected",null);
this.isShowingNow=false;
this.parentMenu=null;
},_closeChild:function(){
this._stopPopupTimer();
if(this.currentPopupItem){
if(this.focused){
_147.set(this.selected.focusNode,"tabIndex",this.tabIndex);
this.selected.focusNode.focus();
}
this.currentPopupItem._closePopup();
this.currentPopupItem=null;
}
},_onItemFocus:function(item){
if(this._hoveredChild&&this._hoveredChild!=item){
this.onItemUnhover(this._hoveredChild);
}
this.set("selected",item);
},_onBlur:function(){
this._cleanUp(true);
this.inherited(arguments);
},_cleanUp:function(_15b){
this._closeChild();
if(typeof this.isShowingNow=="undefined"){
this.set("activated",false);
}
if(_15b){
this.set("selected",null);
}
}});
});
},"curam/dialog":function(){
define(["dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/on","dojo/sniff","curam/inspection/Layer","curam/util","curam/debug","curam/util/external","curam/util/Refresh","curam/tab","curam/util/RuntimeContext","curam/util/ScreenContext","curam/define","curam/util/onLoad","dojo/dom-class","dojo/query","dojo/NodeList-traverse"],function(dom,_15c,_15d,on,has,_15e,util,_15f,_160,_161,tab,_162,_163,_164,_165,_166,_167){
curam.define.singleton("curam.dialog",{MODAL_PREV_FLAG:"o3modalprev",MODAL_PREV_FLAG_INPUT:"curam_dialog_prev_marker",FORCE_CLOSE:false,ERROR_MESSAGES_HEADER:"error-messages-header",_hierarchy:[],_id:null,_displayedHandlerUnsToken:null,_displayed:false,_size:null,_justClose:false,_modalExitingIEGScript:false,validTargets:{"_top":true,"_self":true},initModal:function(_168,_169,_16a){
curam.dialog.pageId=_168;
curam.dialog.messagesExist=_169;
var _16b=false;
var p1;
util.extendXHR();
var _16c=util.getTopmostWindow();
var _16d=false;
var _16e=_16c.dojo.subscribe("/curam/dialog/SetId",this,function(_16f){
_15f.log("curam.dialog: "+_15f.getProperty("curam.dialog.id"),_16f);
curam.dialog._id=_16f;
_16d=true;
_16c.dojo.unsubscribe(_16e);
});
_16c.dojo.publish("/curam/dialog/init");
if(!_16d){
_15f.log("curam.dialog: "+_15f.getProperty("curam.dialog.no.id"));
_16c.dojo.unsubscribe(_16e);
}
if(curam.dialog.closeDialog(false,_16a)){
return;
}
curam.dialog._displayedHandlerUnsToken=util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",null,function(_170,size){
if(_170==curam.dialog._id){
curam.dialog._displayed=true;
curam.dialog._size=size;
util.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
});
if(_171==undefined){
var _171=this.jsScreenContext;
if(!_171){
_171=new _163();
_171.addContextBits("MODAL");
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_171.addContextBits("AGENDA");
}
curam.util.external.inExternalApp()&&_171.addContextBits("EXTAPP");
}
}
if(_171.hasContextBits("AGENDA")||_171.hasContextBits("TREE")){
dojo.addOnUnload(function(){
util.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
});
}
dojo.addOnLoad(function(){
util.connect(dojo.body(),"onclick",curam.dialog.modalEventHandler);
for(var i=0;i<document.forms.length;i++){
var form=document.forms[i];
curam.dialog.addFormInput(form,"hidden","o3frame","modal");
var _172=dom.byId("o3ctx");
var sc=new curam.util.ScreenContext(_171.getValue());
sc.addContextBits("ACTION|ERROR");
_172.value=sc.getValue();
util.connect(form,"onsubmit",curam.dialog.formSubmitHandler);
}
window.curamModal=true;
});
if(curam.util.isExitingIEGScriptInModalWindow()){
delete curam.util.getTopmostWindow().exitingIEGScript;
dojo.addOnUnload(function(){
util.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}else{
var _173=on(window,"unload",function(){
_173.remove();
util.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}
if(_16d){
dojo.publish("/curam/dialog/ready");
}
},setVariableForModalExitingIEGScript:function(){
_modalExitingIEGScript=true;
},closeDialog:function(_174,_175){
if(_174){
curam.dialog.forceClose();
}
var _176=curam.dialog.checkClose(curam.dialog.pageId,_175);
if(_176){
util.onLoad.addPublisher(function(_177){
_177.modalClosing=true;
});
if(curam.dialog.messagesExist){
dojo.addOnLoad(function(){
var _178=dom.byId(util.ERROR_MESSAGES_CONTAINER);
var _179=dom.byId(util.ERROR_MESSAGES_LIST);
var _17a=dom.byId(curam.dialog.ERROR_MESSAGES_HEADER);
if(_179&&_17a){
util.saveInformationalMsgs(_176);
util.disableInformationalLoad();
}else{
_176();
}
});
}else{
_176();
}
return true;
}
return false;
},addFormInput:function(form,type,name,_17b){
return _15d.create("input",{"type":type,"name":name,"value":_17b},form);
},checkClose:function(_17c,_17d){
if(curam.dialog._justClose){
return function(){
curam.dialog.closeModalDialog();
};
}
var _17e=curam.dialog.getParentWindow(window);
if(!_17e){
return false;
}
var href;
if(_17d){
href=curam.util.retrieveBaseURL()+_17d;
}else{
href=window.location.href;
}
var _17f=curam.dialog.MODAL_PREV_FLAG;
var _180=util.getUrlParamValue(href,_17f);
var _181=true;
if(_180){
if(_17e){
if(_180==_17c){
_181=false;
}
}
}else{
_181=false;
}
var _182=util.getUrlParamValue(href,"o3ctx");
if(_182){
var sc=new curam.util.ScreenContext();
sc.setContext(_182);
if(sc.hasContextBits("TREE|ACTION")){
_181=false;
}
}
if(_181||curam.dialog.FORCE_CLOSE){
if(!curam.dialog.FORCE_CLOSE){
if(_180=="user-prefs-editor"){
return function(){
if(_17e&&_17e.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_17e);
}
curam.dialog.closeModalDialog();
};
}
return function(){
var rp=util.removeUrlParam;
href=rp(rp(rp(href,_17f),"o3frame"),util.PREVENT_CACHE_FLAG);
href=util.adjustTargetContext(_17e,href);
if(_17e&&_17e.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_17e,href,true);
}else{
curam.tab.getTabController().handleLinkClick(href);
}
curam.dialog.closeModalDialog();
};
}else{
return function(){
if(_17e!==util.getTopmostWindow()){
_17e.curam.util.loadInformationalMsgs();
}
curam.dialog.closeModalDialog();
};
}
}
return false;
},getParentWindow:function(_183){
if(!_183){
_15f.log(["curam.dialog.getParentWindow():",_15f.getProperty("curam.dialog.no.child"),window.location?" "+window.location.href:"[no location]"].join(" "));
_15f.log("returning as parent = ",window.parent.location.href);
return window.parent;
}
var _184=curam.dialog._getDialogHierarchy();
if(_184){
for(var i=0;i<_184.length;i++){
if(_184[i]==_183){
var _185=(i>0)?_184[i-1]:_184[0];
_15f.log(["curam.dialog.getParentWindow():",_15f.getProperty("curam.dialog.parent.window"),_185.location?_185.location.href:"[no location]"].join(" "));
return _185;
}
}
var ret=_184.length>0?_184[_184.length-1]:undefined;
_15f.log(["curam.dialog.getParentWindow():",_15f.getProperty("curam.dialog.returning.parent"),ret?ret.location.href:"undefined"].join(" "));
return ret;
}
},_getDialogHierarchy:function(){
var _186=util.getTopmostWindow();
_186.require(["curam/dialog"]);
return _186.curam.dialog._hierarchy;
},pushOntoDialogHierarchy:function(_187){
var _188=curam.dialog._getDialogHierarchy();
if(_188&&dojo.indexOf(_188,_187)<0){
_188.push(_187);
_15f.log(_15f.getProperty("curam.dialog.add.hierarchy"),_187.location.href);
_15f.log(_15f.getProperty("curam.dialog.full.hierarchy")+_188.reduce(function(acc,hwin){
return acc+"["+(hwin.location.href||"-")+"]";
}),"");
}
},removeFromDialogHierarchy:function(_189){
var _18a=curam.dialog._getDialogHierarchy();
if(!_189||_18a[_18a.length-1]==_189){
_18a.pop();
}else{
_15f.log("curam.dialog.removeFromDialogHierarchy(): "+_15f.getProperty("curam.dialog.ignore.request"));
try{
_15f.log(_189.location.href);
}
catch(e){
_15f.log(e.message);
}
}
},stripPageOrActionFromUrl:function(url){
var idx=url.lastIndexOf("Page.do");
var len=7;
if(idx<0){
idx=url.lastIndexOf("Action.do");
len=9;
}
if(idx<0){
idx=url.lastIndexOf("Frame.do");
len=8;
}
if(idx>-1&&idx==url.length-len){
return url.substring(0,idx);
}
return url;
},_isSameBaseUrl:function(href,rtc,_18b){
if(href&&href.indexOf("#")==0){
return true;
}
var _18c=href.split("?");
var _18d=rtc.getHref().split("?");
if(_18c[0].indexOf("/")<0){
var _18e=_18d[0].split("/");
_18d[0]=_18e[_18e.length-1];
}
if(_18d[0].indexOf("/")<0){
var _18e=_18c[0].split("/");
_18c[0]=_18e[_18e.length-1];
}
if(_18b&&_18b==true){
_18c[0]=curam.dialog.stripPageOrActionFromUrl(_18c[0]);
_18d[0]=curam.dialog.stripPageOrActionFromUrl(_18d[0]);
}
if(_18c[0]==_18d[0]){
return true;
}
return false;
},modalEventHandler:function(_18f){
curam.dialog._doHandleModalEvent(_18f,new curam.util.RuntimeContext(window),curam.dialog.closeModalDialog,curam.dialog.doRedirect);
},_showSpinnerInDialog:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/spinner");
},_doHandleModalEvent:function(e,rtc,_190,_191){
var _192=e.target;
var u=util;
switch(_192.tagName){
case "INPUT":
if(_15c.get(_192,"type")=="submit"&&typeof _192.form!="undefined"){
_192.form.setAttribute("keepModal",_192.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
}
return true;
case "IMG":
case "SPAN":
case "DIV":
_192=_167(_192).closest("A")[0];
if(_192==null){
return;
}
case "A":
case "BUTTON":
if(_192._submitButton){
_192._submitButton.form.setAttribute("keepModal",_192._submitButton.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
return;
}
break;
default:
return true;
}
var _193=dojo.stopEvent;
var href=_192.getAttribute("href")||_192.getAttribute("data-href");
if(href==""){
_190();
return false;
}
if(!href){
return false;
}
if(href.indexOf("javascript")==0){
return false;
}
var ctx=jsScreenContext;
ctx.addContextBits("MODAL");
var _194=_192.getAttribute("target");
if(_194&&!curam.dialog.validTargets[_194]){
return true;
}
if(href&&href.indexOf("/servlet/FileDownload?")>-1){
var _195=_15d.create("iframe",{src:href},dojo.body());
_195.style.display="none";
_193(e);
return false;
}
if(_166.contains(_192,"external-link")){
return true;
}
if(util.isSameUrl(href,null,rtc)){
if(href.indexOf("#")<0){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_191(window,href);
return false;
}
return true;
}
if(href&&curam.dialog._isSameBaseUrl(href,rtc,true)&&!_192.getAttribute("keepModal")){
_192.setAttribute("keepModal","true");
}
var _196=curam.dialog.getParentWindow(rtc.contextObject());
if(_192&&_192.getAttribute){
_193(e);
if(_192.getAttribute("keepModal")=="true"){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_191(window,href);
}else{
if(_196){
href=u.removeUrlParam(href,"o3frame");
href=u.removeUrlParam(href,curam.dialog.MODAL_PREV_FLAG);
if(_196.location!==util.getTopmostWindow().location){
var _197=new curam.util.RuntimeContext(_196);
var _198=_197.getHref();
_198=u.removeUrlParam(_198,"o3frame");
if(util.isActionPage(_198)){
if(!curam.dialog._isSameBaseUrl(href,_197,true)){
href=u.adjustTargetContext(_196,href);
_191(_196,href);
}
}else{
if(!util.isSameUrl(href,_198)){
href=u.adjustTargetContext(_196,href);
curam.dialog.doRedirect(_196,href);
}
}
}else{
var _199=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_199.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_190();
}
}
return false;
}
if(_196&&typeof (_192)=="undefined"||_192==null||_192=="_self"||_192==""){
_193(e);
href=href.replace(/[&?]o3frame=modal/g,"").replace("%3Fo3frame%3Dmodal","").replace("?o3frame%3Dmodal","");
href=util.updateCtx(href);
if(_196.location!==util.getTopmostWindow().location){
_191(_196,href);
}else{
var _199=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_199.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_190();
return false;
}
return true;
},formSubmitHandler:function(e){
if(e.type=="submit"&&e.defaultPrevented){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
return false;
}
var _19a=curam.dialog.getParentWindow(window);
if(typeof _19a=="undefined"){
return true;
}
e.target.method="post";
e.target.setAttribute("target",window.name);
var _19b=e.target.action;
var _19c=curam.dialog.MODAL_PREV_FLAG;
var _19d=curam.dialog.MODAL_PREV_FLAG_INPUT;
var u=util;
var _19e=dom.byId(_19d);
if(_19e){
_19e.parentNode.removeChild(_19e);
}
if(e.target.getAttribute("keepModal")!="true"&&!jsScreenContext.hasContextBits("AGENDA")){
var _19f="multipart/form-data";
if(e.target.enctype==_19f||e.target.encoding==_19f){
e.target.action=u.removeUrlParam(_19b,_19c);
_19e=curam.dialog.addFormInput(e.target,"hidden",_19c,curam.dialog.pageId);
_19e.setAttribute("id",_19d);
_19e.id=_19d;
}else{
e.target.action=u.replaceUrlParam(_19b,_19c,curam.dialog.pageId);
}
}else{
e.target.action=u.removeUrlParam(_19b,_19c);
}
_19a.curam.util.invalidatePage();
if(!jsScreenContext.hasContextBits("EXTAPP")){
util.firePageSubmittedEvent("dialog");
}
return true;
},forceClose:function(){
curam.dialog.FORCE_CLOSE=true;
},forceParentRefresh:function(){
var _1a0=curam.dialog.getParentWindow(window);
if(!_1a0){
return;
}
_1a0.curam.util.FORCE_REFRESH=true;
},forceParentLocaleRefresh:function(){
var _1a1=curam.dialog.getParentWindow(window);
if(!_1a1){
return;
}
_1a1.curam.util.LOCALE_REFRESH=true;
},closeModalDialog:function(_1a2){
var _1a3=util.getTopmostWindow();
if(curam.dialog._displayedHandlerUnsToken!=null){
_1a3.dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
var _1a4=curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP");
if((typeof (curam.dialog._id)=="undefined"||curam.dialog._id==null||!_1a4)&&window.frameElement){
var _1a5=window.frameElement.id;
var _1a6=_1a5.substring(7);
curam.dialog._id=_1a6;
_15f.log("curam.dialog.closeModalDialog() "+_15f.getProperty("curam.dialog.modal.id")+_1a6);
}
util.getTopmostWindow().dojo.publish("/curam/dialog/close/appExitConfirmation",[curam.dialog._id]);
_15f.log("publishing /curam/dialog/close/appExitConfirmation for ",curam.dialog._id);
_15f.log("publishing /curam/dialog/close for ",curam.dialog._id);
util.getTopmostWindow().dojo.publish("/curam/dialog/close",[curam.dialog._id,_1a2]);
},parseWindowOptions:function(_1a7){
var opts={};
if(_1a7){
_15f.log("curam.dialog.parseWindowOptions "+_15f.getProperty("curam.dialog.parsing"),_1a7);
var _1a8=_1a7.split(",");
var _1a9;
for(var i=0;i<_1a8.length;i++){
_1a9=_1a8[i].split("=");
opts[_1a9[0]]=_1a9[1];
}
_15f.log("done:",dojo.toJson(opts));
}else{
_15f.log("curam.dialog.parseWindowOptions "+_15f.getProperty("curam.dialog.no.options"));
}
return opts;
},doRedirect:function(_1aa,href,_1ab,_1ac){
window.curamDialogRedirecting=true;
if(!curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP")){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/redirectingModal");
}
_1aa.curam.util.redirectWindow(href,_1ab,_1ac);
},_screenReaderAnnounceCurrentTabOnWizard:function(){
var _1ad=dom.byId("wizard-progress-bar");
if(_1ad){
var _1ae=dom.byId("hideAriaLiveElement");
if(typeof _1ae!=null){
this._createSpanContainingInformationOnCurrentWizardTab(_1ad,_1ae);
}
}
},_stylingAddedToMandatoryIconHelp:function(){
var _1af=dom.byId("wizard-progress-bar");
var _1b0=dojo.query(".mandatory-icon-help")[0];
if(_1af&&_1b0){
_166.add(_1b0,"wizard-progress-bar-exists");
}
},_createSpanContainingInformationOnCurrentWizardTab:function(_1b1,_1b2){
var _1b3=null;
var _1b4="";
var _1b5=" ";
var _1b6=_167(".title",_1b1)[0]&&_167(".title",_1b1)[0].innerText;
var desc=_167(".desc",_1b1)[0]&&_167(".desc",_1b1)[0].innerText;
if(_1b6&&_1b6!=""){
_1b4+=_1b6;
}
if(desc&&desc!=""){
_1b4!=""?_1b4+=_1b5:"";
_1b4+=desc;
}
var _1b7=dom.byId("content");
var _1b8=_167(".cluster,.list",_1b7)[0];
if(_1b8){
if(typeof _167(".collapse-title",_1b8)[0]=="undefined"||_167(".collapse-title",_1b8)[0].innerHTML==""){
if(typeof _167(".description",_1b8)[0]!="undefined"){
if(_167(".description",_1b8)[0].innerHTML!==""){
var _1b9=_167(".description",_1b8)[0];
if(_1b9&&_1b9.innerText!==""){
_1b4!=""?_1b4+=_1b5:"";
_1b4+=_167(".description",_1b8)[0].innerText;
}
}
}
}
}else{
var _1ba=_167("tr:first-child > td.field.last-cell",_1b7)[0];
if(_1ba&&_1ba.innerText!==""){
_1b4!=""?_1b4+=_1b5:"";
_1b4+=_1ba.innerText;
}
}
if(_1b4){
_1b3=_15d.create("span",{innerHTML:_1b4});
setTimeout(function(){
_15d.place(_1b3,_1b2);
},1000);
}
},closeGracefully:function(){
curam.dialog._justClose=true;
},});
_15e.register("curam/dialog",this);
return curam.dialog;
});
},"dijit/focus":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/domReady","dojo/sniff","dojo/Stateful","dojo/_base/window","dojo/window","./a11y","./registry","./main"],function(_1bb,_1bc,dom,_1bd,_1be,_1bf,_1c0,lang,on,_1c1,has,_1c2,win,_1c3,a11y,_1c4,_1c5){
var _1c6;
var _1c7;
var _1c8=_1bc([_1c2,_1c0],{curNode:null,activeStack:[],constructor:function(){
var _1c9=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_1bb.before(_1bf,"empty",_1c9);
_1bb.before(_1bf,"destroy",_1c9);
},registerIframe:function(_1ca){
return this.registerWin(_1ca.contentWindow,_1ca);
},registerWin:function(_1cb,_1cc){
var _1cd=this,body=_1cb.document&&_1cb.document.body;
if(body){
var _1ce=has("pointer-events")?"pointerdown":has("MSPointer")?"MSPointerDown":has("touch-events")?"mousedown, touchstart":"mousedown";
var mdh=on(_1cb.document,_1ce,function(evt){
if(evt&&evt.target&&evt.target.parentNode==null){
return;
}
_1cd._onTouchNode(_1cc||evt.target,"mouse");
});
var fih=on(body,"focusin",function(evt){
if(!evt.target.tagName){
return;
}
var tag=evt.target.tagName.toLowerCase();
if(tag=="#document"||tag=="body"){
return;
}
if(a11y.isFocusable(evt.target)){
_1cd._onFocusNode(_1cc||evt.target);
}else{
_1cd._onTouchNode(_1cc||evt.target);
}
});
var foh=on(body,"focusout",function(evt){
_1cd._onBlurNode(_1cc||evt.target);
});
return {remove:function(){
mdh.remove();
fih.remove();
foh.remove();
mdh=fih=foh=null;
body=null;
}};
}
},_onBlurNode:function(node){
var now=(new Date()).getTime();
if(now<_1c6+100){
return;
}
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
}
this._clearFocusTimer=setTimeout(lang.hitch(this,function(){
this.set("prevNode",this.curNode);
this.set("curNode",null);
}),0);
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
}
if(now<_1c7+100){
return;
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
}),0);
},_onTouchNode:function(node,by){
_1c7=(new Date()).getTime();
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_1be.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _1cf=[];
try{
while(node){
var _1d0=_1bd.get(node,"dijitPopupParent");
if(_1d0){
node=_1c4.byId(_1d0).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_1c3.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_1d1=id&&_1c4.byId(id);
if(_1d1&&!(by=="mouse"&&_1d1.get("disabled"))){
_1cf.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_1cf,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
_1c6=(new Date()).getTime();
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
delete this._clearFocusTimer;
}
this._onTouchNode(node);
if(node==this.curNode){
return;
}
this.set("prevNode",this.curNode);
this.set("curNode",node);
},_setStack:function(_1d2,by){
var _1d3=this.activeStack,_1d4=_1d3.length-1,_1d5=_1d2.length-1;
if(_1d2[_1d5]==_1d3[_1d4]){
return;
}
this.set("activeStack",_1d2);
var _1d6,i;
for(i=_1d4;i>=0&&_1d3[i]!=_1d2[i];i--){
_1d6=_1c4.byId(_1d3[i]);
if(_1d6){
_1d6._hasBeenBlurred=true;
_1d6.set("focused",false);
if(_1d6._focusManager==this){
_1d6._onBlur(by);
}
this.emit("widget-blur",_1d6,by);
}
}
for(i++;i<=_1d5;i++){
_1d6=_1c4.byId(_1d2[i]);
if(_1d6){
_1d6.set("focused",true);
if(_1d6._focusManager==this){
_1d6._onFocus(by);
}
this.emit("widget-focus",_1d6,by);
}
}
},focus:function(node){
if(node){
try{
node.focus();
}
catch(e){
}
}
}});
var _1d7=new _1c8();
_1c1(function(){
var _1d8=_1d7.registerWin(_1c3.get(document));
if(has("ie")){
on(window,"unload",function(){
if(_1d8){
_1d8.remove();
_1d8=null;
}
});
}
});
_1c5.focus=function(node){
_1d7.focus(node);
};
for(var attr in _1d7){
if(!/^_/.test(attr)){
_1c5.focus[attr]=typeof _1d7[attr]=="function"?lang.hitch(_1d7,attr):_1d7[attr];
}
}
_1d7.watch(function(attr,_1d9,_1da){
_1c5.focus[attr]=_1da;
});
return _1d7;
});
},"dojo/i18n":function(){
define(["./_base/kernel","require","./has","./_base/array","./_base/config","./_base/lang","./_base/xhr","./json","module"],function(dojo,_1db,has,_1dc,_1dd,lang,xhr,json,_1de){
has.add("dojo-preload-i18n-Api",1);
1||has.add("dojo-v1x-i18n-Api",1);
var _1df=dojo.i18n={},_1e0=/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,_1e1=function(root,_1e2,_1e3,_1e4){
for(var _1e5=[_1e3+_1e4],_1e6=_1e2.split("-"),_1e7="",i=0;i<_1e6.length;i++){
_1e7+=(_1e7?"-":"")+_1e6[i];
if(!root||root[_1e7]){
_1e5.push(_1e3+_1e7+"/"+_1e4);
_1e5.specificity=_1e7;
}
}
return _1e5;
},_1e8={},_1e9=function(_1ea,_1eb,_1ec){
_1ec=_1ec?_1ec.toLowerCase():dojo.locale;
_1ea=_1ea.replace(/\./g,"/");
_1eb=_1eb.replace(/\./g,"/");
return (/root/i.test(_1ec))?(_1ea+"/nls/"+_1eb):(_1ea+"/nls/"+_1ec+"/"+_1eb);
},_1ed=dojo.getL10nName=function(_1ee,_1ef,_1f0){
return _1ee=_1de.id+"!"+_1e9(_1ee,_1ef,_1f0);
},_1f1=function(_1f2,_1f3,_1f4,_1f5,_1f6,load){
_1f2([_1f3],function(root){
var _1f7=lang.clone(root.root||root.ROOT),_1f8=_1e1(!root._v1x&&root,_1f6,_1f4,_1f5);
_1f2(_1f8,function(){
for(var i=1;i<_1f8.length;i++){
_1f7=lang.mixin(lang.clone(_1f7),arguments[i]);
}
var _1f9=_1f3+"/"+_1f6;
_1e8[_1f9]=_1f7;
_1f7.$locale=_1f8.specificity;
load();
});
});
},_1fa=function(id,_1fb){
return /^\./.test(id)?_1fb(id):id;
},_1fc=function(_1fd){
var list=_1dd.extraLocale||[];
list=lang.isArray(list)?list:[list];
list.push(_1fd);
return list;
},load=function(id,_1fe,load){
if(has("dojo-preload-i18n-Api")){
var _1ff=id.split("*"),_200=_1ff[1]=="preload";
if(_200){
if(!_1e8[id]){
_1e8[id]=1;
_201(_1ff[2],json.parse(_1ff[3]),1,_1fe);
}
load(1);
}
if(_200||_202(id,_1fe,load)){
return;
}
}
var _203=_1e0.exec(id),_204=_203[1]+"/",_205=_203[5]||_203[4],_206=_204+_205,_207=(_203[5]&&_203[4]),_208=_207||dojo.locale||"",_209=_206+"/"+_208,_20a=_207?[_208]:_1fc(_208),_20b=_20a.length,_20c=function(){
if(!--_20b){
load(lang.delegate(_1e8[_209]));
}
};
_1dc.forEach(_20a,function(_20d){
var _20e=_206+"/"+_20d;
if(has("dojo-preload-i18n-Api")){
_20f(_20e);
}
if(!_1e8[_20e]){
_1f1(_1fe,_206,_204,_205,_20d,_20c);
}else{
_20c();
}
});
};
if(has("dojo-unit-tests")){
var _210=_1df.unitTests=[];
}
if(has("dojo-preload-i18n-Api")||1){
var _211=_1df.normalizeLocale=function(_212){
var _213=_212?_212.toLowerCase():dojo.locale;
return _213=="root"?"ROOT":_213;
},isXd=function(mid,_214){
return (1&&1)?_214.isXdUrl(_1db.toUrl(mid+".js")):true;
},_215=0,_216=[],_201=_1df._preloadLocalizations=function(_217,_218,_219,_21a){
_21a=_21a||_1db;
function _21b(mid,_21c){
if(isXd(mid,_21a)||_219){
_21a([mid],_21c);
}else{
_236([mid],_21c,_21a);
}
};
function _21d(_21e,func){
var _21f=_21e.split("-");
while(_21f.length){
if(func(_21f.join("-"))){
return;
}
_21f.pop();
}
func("ROOT");
};
function _220(){
_215++;
};
function _221(){
--_215;
while(!_215&&_216.length){
load.apply(null,_216.shift());
}
};
function _222(path,name,loc,_223){
return _223.toAbsMid(path+name+"/"+loc);
};
function _224(_225){
_225=_211(_225);
_21d(_225,function(loc){
if(_1dc.indexOf(_218,loc)>=0){
var mid=_217.replace(/\./g,"/")+"_"+loc;
_220();
_21b(mid,function(_226){
for(var p in _226){
var _227=_226[p],_228=p.match(/(.+)\/([^\/]+)$/),_229,_22a;
if(!_228){
continue;
}
_229=_228[2];
_22a=_228[1]+"/";
if(!_227._localized){
continue;
}
var _22b;
if(loc==="ROOT"){
var root=_22b=_227._localized;
delete _227._localized;
root.root=_227;
_1e8[_1db.toAbsMid(p)]=root;
}else{
_22b=_227._localized;
_1e8[_222(_22a,_229,loc,_1db)]=_227;
}
if(loc!==_225){
function _22c(_22d,_22e,_22f,_230){
var _231=[],_232=[];
_21d(_225,function(loc){
if(_230[loc]){
_231.push(_1db.toAbsMid(_22d+loc+"/"+_22e));
_232.push(_222(_22d,_22e,loc,_1db));
}
});
if(_231.length){
_220();
_21a(_231,function(){
for(var i=_231.length-1;i>=0;i--){
_22f=lang.mixin(lang.clone(_22f),arguments[i]);
_1e8[_232[i]]=_22f;
}
_1e8[_222(_22d,_22e,_225,_1db)]=lang.clone(_22f);
_221();
});
}else{
_1e8[_222(_22d,_22e,_225,_1db)]=_22f;
}
};
_22c(_22a,_229,_227,_22b);
}
}
_221();
});
return true;
}
return false;
});
};
_224();
_1dc.forEach(dojo.config.extraLocale,_224);
},_202=function(id,_233,load){
if(_215){
_216.push([id,_233,load]);
}
return _215;
},_20f=function(){
};
}
if(1){
var _234={},_235=new Function("__bundle","__checkForLegacyModules","__mid","__amdValue","var define = function(mid, factory){define.called = 1; __amdValue.result = factory || mid;},"+"\t   require = function(){define.called = 1;};"+"try{"+"define.called = 0;"+"eval(__bundle);"+"if(define.called==1)"+"return __amdValue;"+"if((__checkForLegacyModules = __checkForLegacyModules(__mid)))"+"return __checkForLegacyModules;"+"}catch(e){}"+"try{"+"return eval('('+__bundle+')');"+"}catch(e){"+"return e;"+"}"),_236=function(deps,_237,_238){
var _239=[];
_1dc.forEach(deps,function(mid){
var url=_238.toUrl(mid+".js");
function load(text){
var _23a=_235(text,_20f,mid,_234);
if(_23a===_234){
_239.push(_1e8[url]=_234.result);
}else{
if(_23a instanceof Error){
console.error("failed to evaluate i18n bundle; url="+url,_23a);
_23a={};
}
_239.push(_1e8[url]=(/nls\/[^\/]+\/[^\/]+$/.test(url)?_23a:{root:_23a,_v1x:1}));
}
};
if(_1e8[url]){
_239.push(_1e8[url]);
}else{
var _23b=_238.syncLoadNls(mid);
if(!_23b){
_23b=_20f(mid.replace(/nls\/([^\/]*)\/([^\/]*)$/,"nls/$2/$1"));
}
if(_23b){
_239.push(_23b);
}else{
if(!xhr){
try{
_238.getText(url,true,load);
}
catch(e){
_239.push(_1e8[url]={});
}
}else{
xhr.get({url:url,sync:true,load:load,error:function(){
_239.push(_1e8[url]={});
}});
}
}
}
});
_237&&_237.apply(null,_239);
};
_20f=function(_23c){
for(var _23d,_23e=_23c.split("/"),_23f=dojo.global[_23e[0]],i=1;_23f&&i<_23e.length-1;_23f=_23f[_23e[i++]]){
}
if(_23f){
_23d=_23f[_23e[i]];
if(!_23d){
_23d=_23f[_23e[i].replace(/-/g,"_")];
}
if(_23d){
_1e8[_23c]=_23d;
}
}
return _23d;
};
_1df.getLocalization=function(_240,_241,_242){
var _243,_244=_1e9(_240,_241,_242);
load(_244,(!isXd(_244,_1db)?function(deps,_245){
_236(deps,_245,_1db);
}:_1db),function(_246){
_243=_246;
});
return _243;
};
if(has("dojo-unit-tests")){
_210.push(function(doh){
doh.register("tests.i18n.unit",function(t){
var _247;
_247=_235("{prop:1}",_20f,"nonsense",_234);
t.is({prop:1},_247);
t.is(undefined,_247[1]);
_247=_235("({prop:1})",_20f,"nonsense",_234);
t.is({prop:1},_247);
t.is(undefined,_247[1]);
_247=_235("{'prop-x':1}",_20f,"nonsense",_234);
t.is({"prop-x":1},_247);
t.is(undefined,_247[1]);
_247=_235("({'prop-x':1})",_20f,"nonsense",_234);
t.is({"prop-x":1},_247);
t.is(undefined,_247[1]);
_247=_235("define({'prop-x':1})",_20f,"nonsense",_234);
t.is(_234,_247);
t.is({"prop-x":1},_234.result);
_247=_235("define('some/module', {'prop-x':1})",_20f,"nonsense",_234);
t.is(_234,_247);
t.is({"prop-x":1},_234.result);
_247=_235("this is total nonsense and should throw an error",_20f,"nonsense",_234);
t.is(_247 instanceof Error,true);
});
});
}
}
return lang.mixin(_1df,{dynamic:true,normalize:_1fa,load:load,cache:_1e8,getL10nName:_1ed});
});
},"dijit/hccss":function(){
define(["dojo/dom-class","dojo/hccss","dojo/domReady","dojo/_base/window"],function(_248,has,_249,win){
_249(function(){
if(has("highcontrast")){
_248.add(win.body(),"dijit_a11y");
}
});
return has;
});
},"curam/util/LocalConfig":function(){
define([],function(){
var _24a=function(name){
return "curam_util_LocalConfig_"+name;
},_24b=function(name,_24c){
var _24d=_24a(name);
if(typeof top[_24d]==="undefined"){
top[_24d]=_24c;
}
return top[_24d];
},_24e=function(name){
return top&&top!=null?top[_24a(name)]:undefined;
};
_24b("seedValues",{}),_24b("overrides",{});
var _24f=function(_250,_251){
if(typeof _250!=="undefined"&&typeof _250!=="string"){
throw new Error("Invalid "+_251+" type: "+typeof _250+"; expected string");
}
};
var _252={seedOption:function(name,_253,_254){
_24f(_253,"value");
_24f(_254,"defaultValue");
_24e("seedValues")[name]=(typeof _253!=="undefined")?_253:_254;
},overrideOption:function(name,_255){
_24f(_255,"value");
if(typeof (Storage)!=="undefined"){
localStorage[name]=_255;
}else{
_24e("overrides")[name]=_255;
}
},readOption:function(name,_256){
var _257=_24e("seedValues");
var _258=_24e("overrides");
_24f(_256,"defaultValue");
var _259=null;
if(typeof (Storage)!=="undefined"&&localStorage&&typeof localStorage[name]!=="undefined"){
_259=localStorage[name];
}else{
if(_258&&typeof _258[name]!=="undefined"){
_259=_258[name];
}else{
if(_257&&typeof _257[name]!=="undefined"){
_259=_257[name];
}else{
_259=_256;
}
}
}
return _259;
},clearOption:function(name){
if(typeof (Storage)!=="undefined"){
localStorage.removeItem(name);
}
delete _24e("overrides")[name];
delete _24e("seedValues")[name];
}};
return _252;
});
},"curam/widget/_HasDropDown":function(){
define(["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","dijit/registry","dijit/focus","dijit/popup","dijit/_FocusMixin","dijit/a11y","dijit/_HasDropDown","curam/util"],function(_25a,_25b,dom,_25c,_25d,_25e,_25f,has,keys,lang,on,_260,_261,_262,_263,_264,a11y){
return _25a("curam.widget._HasDropDown",[dijit._HasDropDown],{openDropDown:function(){
var _265=this.dropDown,_266=_265.domNode,_267=this._aroundNode||this.domNode,self=this;
var _268=_263.open({parent:this,popup:_265,around:_267,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
self.closeDropDown(true);
},onCancel:function(evt){
self.closeDropDown(evt,true);
},onClose:function(){
_25c.set(self._popupStateNode,"popupActive",false);
_25d.remove(self._popupStateNode,"dijitHasDropDownOpen");
self._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_267.offsetWidth>_265._popupWrapper.offsetWidth)){
var _269=_267.offsetWidth-_265._popupWrapper.offsetWidth;
var _26a={w:_265.domNode.offsetWidth+_269};
this._origStyle=_266.style.cssText;
if(lang.isFunction(_265.resize)){
_265.resize(_26a);
}else{
_25e.setMarginBox(_266,_26a);
}
if(_268.corner[1]=="R"){
_265._popupWrapper.style.left=(_265._popupWrapper.style.left.replace("px","")-_269)+"px";
}
}
_25c.set(this._popupStateNode,"popupActive","true");
_25d.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_265.id);
if(_266.getAttribute("role")!=="presentation"&&!_266.getAttribute("aria-labelledby")){
_266.setAttribute("aria-labelledby",this.id);
}
return _268;
},closeDropDown:function(evt,_26b){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(_26b&&this.focus){
var _26c=this._getNextFocusableNode(evt,this.ownerDocument,this.focusNode);
if(_26c.focus){
_26c.focus();
}else{
this.focus();
}
}
_263.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
},_getNextFocusableNode:function(evt,_26d,_26e){
var _26f=_26e;
var _270=curam.util.getTopmostWindow();
if(evt&&evt.keyCode==keys.TAB){
var _271=this._findFocusableElementsInDocument(_26d);
var _272=_271.length;
var _273=_271.indexOf(_26e);
if(_273>-1){
if(!evt.shiftKey){
_273++;
var _274=null;
for(var i=_273;i<_271.length;i++){
if(_271[i].offsetParent!==null&&!_25d.contains(_271[i],"dijitMenuItem")){
_274=_271[i];
break;
}
}
if(_274&&_274.nodeName.toLowerCase()!=="iframe"){
_26f=_274;
}else{
if(_274&&_274.nodeName.toLowerCase()==="iframe"){
_274=this._findFocusableElementInIframe(_274,evt.shiftKey);
if(_274){
_26f=_274;
}
}else{
if(this.ownerDocument!==_270.document){
var _275=this.ownerDocument;
while(_275!==_270.document&&!_274){
var _276=window.parent;
var _277=_276.document||_276.contentDocument||_276.contentWindow.document;
_274=this._findFocusableElementInParentDocument(_277,evt.shiftKey);
_275=_277;
}
if(_274){
_26f=_274;
}
}
}
}
}else{
_273--;
var _278=null;
for(var i=_273;i>-1;i--){
if(_271[i].offsetParent!==null){
_278=_271[i];
break;
}
}
if(_278&&_278.nodeName.toLowerCase()!=="iframe"){
_26f=_278;
}else{
if(_278&&_278.nodeName.toLowerCase()==="iframe"){
_278=this._findFocusableElementInIframe(_278,evt.shiftKey);
if(_278){
_26f=_278;
}
}else{
if(this.ownerDocument!==_270.document){
var _275=this.ownerDocument;
while(_275!==_270.document&&!_278){
var _276=window.parent;
var _277=_276.document||_276.contentDocument||_276.contentWindow.document;
_278=this._findFocusableElementInParentDocument(_277,evt.shiftKey);
_275=_277;
}
if(_278){
_26f=_278;
}
}
}
}
}
}
}
return _26f;
},_findFocusableElementsInDocument:function(_279){
var _27a=_279.querySelectorAll("button:not(.dijitTabCloseButton), [href], input, select, object, iframe, area, textarea, [tabindex]:not([tabindex=\"-1\"])");
var _27b=[];
for(var i=0;i<_27a.length;i++){
if(a11y._isElementShown(_27a[i])){
if(a11y.isTabNavigable(_27a[i])){
_27b.push(_27a[i]);
}else{
if(_27a[i].nodeName.toLowerCase()==="iframe"){
_27b.push(_27a[i]);
}
}
}
}
return _27b;
},_findFocusableElementInParentDocument:function(_27c,_27d){
var _27e=this._findFocusableElementsInDocument(_27c);
var _27f=false;
var _280=0;
var _281=null;
if(!_27d){
for(var i=0;i<_27e.length;i++){
if(_27e[i].nodeName.toLowerCase()==="iframe"){
if(_27e[i].contentDocument===this.ownerDocument){
_27f=true;
_280=i;
break;
}
}
}
}else{
for(var i=_27e.length-1;i>-1;i--){
if(_27e[i].nodeName.toLowerCase()==="iframe"){
if(_27e[i].contentDocument===this.ownerDocument){
_27f=true;
_280=i;
break;
}
}
}
}
if(_27f){
if(!_27d){
_280++;
for(var i=_280;i<_27e.length;i++){
if(_27e[i].offsetParent!==null){
_281=_27e[i];
break;
}
}
}else{
_280--;
for(var i=_280;i>-1;i--){
if(_27e[i].offsetParent!==null){
_281=_27e[i];
break;
}
}
}
if(_281&&_281.nodeName.toLowerCase()==="iframe"){
_281=this._findFocusableElementInIframe(_281,_27d);
}
}
return _281;
},_findFocusableElementInIframe:function(node,_282){
var _283=node;
while(_283&&_283.nodeName.toLowerCase()==="iframe"){
var _284=_283.contentDocument||_283.contentWindow.document;
var _285=this._findFocusableElementsInDocument(_284);
if(_285.length>0){
if(!_282){
indexOfNode=0;
for(var i=indexOfNode;i<_285.length;i++){
if(_285[i].offsetParent!==null){
_283=_285[i];
break;
}
}
}else{
indexOfNode=_285.length-1;
for(var i=indexOfNode;i>-1;i--){
if(_285[i].offsetParent!==null){
_283=_285[i];
break;
}
}
}
}else{
_283=null;
}
}
return _283;
}});
});
},"idx/form/_AutoCompleteA11yMixin":function(){
define(["dojo/_base/declare"],function(_286){
return _286("idx.form._AutoCompleteA11yMixin",[],{_showResultList:function(){
var temp=this.domNode;
this.domNode=this.oneuiBaseNode;
this.inherited(arguments);
this.domNode=temp;
},closeDropDown:function(){
var temp=this.domNode;
this.domNode=this.oneuiBaseNode;
this.inherited(arguments);
this.domNode=temp;
},_announceOption:function(){
this.inherited(arguments);
this.focusNode.removeAttribute("aria-activedescendant");
}});
});
},"dijit/form/_ComboBoxMenuMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/has","dojo/i18n","dojo/i18n!./nls/ComboBox"],function(_287,_288,_289,has,i18n){
var _28a=_288("dijit.form._ComboBoxMenuMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{_messages:null,postMixInProperties:function(){
this.inherited(arguments);
this._messages=i18n.getLocalization("dijit.form","ComboBox",this.lang);
},buildRendering:function(){
this.inherited(arguments);
this.previousButton.innerHTML=this._messages["previousMessage"];
this.nextButton.innerHTML=this._messages["nextMessage"];
},_setValueAttr:function(_28b){
this._set("value",_28b);
this.onChange(_28b);
},onClick:function(node){
if(node==this.previousButton){
this._setSelectedAttr(null);
this.onPage(-1);
}else{
if(node==this.nextButton){
this._setSelectedAttr(null);
this.onPage(1);
}else{
this.onChange(node);
}
}
},onChange:function(){
},onPage:function(){
},onClose:function(){
this._setSelectedAttr(null);
},_createOption:function(item,_28c){
var _28d=this._createMenuItem();
var _28e=_28c(item);
if(_28e.html){
_28d.innerHTML=_28e.label;
}else{
_28d.appendChild(_28d.ownerDocument.createTextNode(_28e.label));
}
if(_28d.innerHTML==""){
_28d.innerHTML="&#160;";
}
return _28d;
},createOptions:function(_28f,_290,_291){
this.items=_28f;
this.previousButton.style.display=(_290.start==0)?"none":"";
_289.set(this.previousButton,"id",this.id+"_prev");
_287.forEach(_28f,function(item,i){
var _292=this._createOption(item,_291);
_292.setAttribute("item",i);
_289.set(_292,"id",this.id+i);
this.nextButton.parentNode.insertBefore(_292,this.nextButton);
},this);
var _293=false;
if(_28f.total&&!_28f.total.then&&_28f.total!=-1){
if((_290.start+_290.count)<_28f.total){
_293=true;
}else{
if((_290.start+_290.count)>_28f.total&&_290.count==_28f.length){
_293=true;
}
}
}else{
if(_290.count==_28f.length){
_293=true;
}
}
this.nextButton.style.display=_293?"":"none";
_289.set(this.nextButton,"id",this.id+"_next");
},clearResultList:function(){
var _294=this.containerNode;
while(_294.childNodes.length>2){
_294.removeChild(_294.childNodes[_294.childNodes.length-2]);
}
this._setSelectedAttr(null);
},highlightFirstOption:function(){
this.selectFirstNode();
},highlightLastOption:function(){
this.selectLastNode();
},selectFirstNode:function(){
this.inherited(arguments);
if(this.getHighlightedOption()==this.previousButton){
this.selectNextNode();
}
},selectLastNode:function(){
this.inherited(arguments);
if(this.getHighlightedOption()==this.nextButton){
this.selectPreviousNode();
}
},getHighlightedOption:function(){
return this.selected;
}});
if(has("dojo-bidi")){
_28a=_288("dijit.form._ComboBoxMenuMixin",_28a,{_createOption:function(){
var _295=this.inherited(arguments);
this.applyTextDir(_295);
return _295;
}});
}
return _28a;
});
},"dijit/form/_SearchMixin":function(){
define(["dojo/_base/declare","dojo/keys","dojo/_base/lang","dojo/query","dojo/string","dojo/when","../registry"],function(_296,keys,lang,_297,_298,when,_299){
return _296("dijit.form._SearchMixin",null,{pageSize:Infinity,store:null,fetchProperties:{},query:{},list:"",_setListAttr:function(list){
this._set("list",list);
},searchDelay:200,searchAttr:"name",queryExpr:"${0}*",ignoreCase:true,_patternToRegExp:function(_29a){
return new RegExp("^"+_29a.replace(/(\\.)|(\*)|(\?)|\W/g,function(str,_29b,star,_29c){
return star?".*":_29c?".":_29b?_29b:"\\"+str;
})+"$",this.ignoreCase?"mi":"m");
},_abortQuery:function(){
if(this.searchTimer){
this.searchTimer=this.searchTimer.remove();
}
if(this._queryDeferHandle){
this._queryDeferHandle=this._queryDeferHandle.remove();
}
if(this._fetchHandle){
if(this._fetchHandle.abort){
this._cancelingQuery=true;
this._fetchHandle.abort();
this._cancelingQuery=false;
}
if(this._fetchHandle.cancel){
this._cancelingQuery=true;
this._fetchHandle.cancel();
this._cancelingQuery=false;
}
this._fetchHandle=null;
}
},_processInput:function(evt){
if(this.disabled||this.readOnly){
return;
}
var key=evt.charOrCode;
var _29d=false;
this._prev_key_backspace=false;
switch(key){
case keys.DELETE:
case keys.BACKSPACE:
this._prev_key_backspace=true;
this._maskValidSubsetError=true;
_29d=true;
break;
default:
_29d=typeof key=="string"||key==229;
}
if(_29d){
if(!this.store){
this.onSearch();
}else{
this.searchTimer=this.defer("_startSearchFromInput",1);
}
}
},onSearch:function(){
},_startSearchFromInput:function(){
this._startSearch(this.focusNode.value);
},_startSearch:function(text){
this._abortQuery();
var _29e=this,_297=lang.clone(this.query),_29f={start:0,count:this.pageSize,queryOptions:{ignoreCase:this.ignoreCase,deep:true}},qs=_298.substitute(this.queryExpr,[text.replace(/([\\\*\?])/g,"\\$1")]),q,_2a0=function(){
var _2a1=_29e._fetchHandle=_29e.store.query(_297,_29f);
if(_29e.disabled||_29e.readOnly||(q!==_29e._lastQuery)){
return;
}
when(_2a1,function(res){
_29e._fetchHandle=null;
if(!_29e.disabled&&!_29e.readOnly&&(q===_29e._lastQuery)){
when(_2a1.total,function(_2a2){
res.total=_2a2;
var _2a3=_29e.pageSize;
if(isNaN(_2a3)||_2a3>res.total){
_2a3=res.total;
}
res.nextPage=function(_2a4){
_29f.direction=_2a4=_2a4!==false;
_29f.count=_2a3;
if(_2a4){
_29f.start+=res.length;
if(_29f.start>=res.total){
_29f.count=0;
}
}else{
_29f.start-=_2a3;
if(_29f.start<0){
_29f.count=Math.max(_2a3+_29f.start,0);
_29f.start=0;
}
}
if(_29f.count<=0){
res.length=0;
_29e.onSearch(res,_297,_29f);
}else{
_2a0();
}
};
_29e.onSearch(res,_297,_29f);
});
}
},function(err){
_29e._fetchHandle=null;
if(!_29e._cancelingQuery){
console.error(_29e.declaredClass+" "+err.toString());
}
});
};
lang.mixin(_29f,this.fetchProperties);
if(this.store._oldAPI){
q=qs;
}else{
q=this._patternToRegExp(qs);
q.toString=function(){
return qs;
};
}
this._lastQuery=_297[this.searchAttr]=q;
this._queryDeferHandle=this.defer(_2a0,this.searchDelay);
},constructor:function(){
this.query={};
this.fetchProperties={};
},postMixInProperties:function(){
if(!this.store){
var list=this.list;
if(list){
this.store=_299.byId(list);
}
}
this.inherited(arguments);
}});
});
},"dojo/parser":function(){
define(["require","./_base/kernel","./_base/lang","./_base/array","./_base/config","./dom","./_base/window","./_base/url","./aspect","./promise/all","./date/stamp","./Deferred","./has","./query","./on","./ready"],function(_2a5,dojo,_2a6,_2a7,_2a8,dom,_2a9,_2aa,_2ab,all,_2ac,_2ad,has,_2ae,don,_2af){
new Date("X");
function _2b0(text){
return eval("("+text+")");
};
var _2b1=0;
_2ab.after(_2a6,"extend",function(){
_2b1++;
},true);
function _2b2(ctor){
var map=ctor._nameCaseMap,_2b3=ctor.prototype;
if(!map||map._extendCnt<_2b1){
map=ctor._nameCaseMap={};
for(var name in _2b3){
if(name.charAt(0)==="_"){
continue;
}
map[name.toLowerCase()]=name;
}
map._extendCnt=_2b1;
}
return map;
};
var _2b4={};
function _2b5(_2b6,_2b7){
var ts=_2b6.join();
if(!_2b4[ts]){
var _2b8=[];
for(var i=0,l=_2b6.length;i<l;i++){
var t=_2b6[i];
_2b8[_2b8.length]=(_2b4[t]=_2b4[t]||(_2a6.getObject(t)||(~t.indexOf("/")&&(_2b7?_2b7(t):_2a5(t)))));
}
var ctor=_2b8.shift();
_2b4[ts]=_2b8.length?(ctor.createSubclass?ctor.createSubclass(_2b8):ctor.extend.apply(ctor,_2b8)):ctor;
}
return _2b4[ts];
};
var _2b9={_clearCache:function(){
_2b1++;
_2b4={};
},_functionFromScript:function(_2ba,_2bb){
var _2bc="",_2bd="",_2be=(_2ba.getAttribute(_2bb+"args")||_2ba.getAttribute("args")),_2bf=_2ba.getAttribute("with");
var _2c0=(_2be||"").split(/\s*,\s*/);
if(_2bf&&_2bf.length){
_2a7.forEach(_2bf.split(/\s*,\s*/),function(part){
_2bc+="with("+part+"){";
_2bd+="}";
});
}
return new Function(_2c0,_2bc+_2ba.innerHTML+_2bd);
},instantiate:function(_2c1,_2c2,_2c3){
_2c2=_2c2||{};
_2c3=_2c3||{};
var _2c4=(_2c3.scope||dojo._scopeName)+"Type",_2c5="data-"+(_2c3.scope||dojo._scopeName)+"-",_2c6=_2c5+"type",_2c7=_2c5+"mixins";
var list=[];
_2a7.forEach(_2c1,function(node){
var type=_2c4 in _2c2?_2c2[_2c4]:node.getAttribute(_2c6)||node.getAttribute(_2c4);
if(type){
var _2c8=node.getAttribute(_2c7),_2c9=_2c8?[type].concat(_2c8.split(/\s*,\s*/)):[type];
list.push({node:node,types:_2c9});
}
});
return this._instantiate(list,_2c2,_2c3);
},_instantiate:function(_2ca,_2cb,_2cc,_2cd){
var _2ce=_2a7.map(_2ca,function(obj){
var ctor=obj.ctor||_2b5(obj.types,_2cc.contextRequire);
if(!ctor){
throw new Error("Unable to resolve constructor for: '"+obj.types.join()+"'");
}
return this.construct(ctor,obj.node,_2cb,_2cc,obj.scripts,obj.inherited);
},this);
function _2cf(_2d0){
if(!_2cb._started&&!_2cc.noStart){
_2a7.forEach(_2d0,function(_2d1){
if(typeof _2d1.startup==="function"&&!_2d1._started){
_2d1.startup();
}
});
}
return _2d0;
};
if(_2cd){
return all(_2ce).then(_2cf);
}else{
return _2cf(_2ce);
}
},construct:function(ctor,node,_2d2,_2d3,_2d4,_2d5){
var _2d6=ctor&&ctor.prototype;
_2d3=_2d3||{};
var _2d7={};
if(_2d3.defaults){
_2a6.mixin(_2d7,_2d3.defaults);
}
if(_2d5){
_2a6.mixin(_2d7,_2d5);
}
var _2d8;
if(has("dom-attributes-explicit")){
_2d8=node.attributes;
}else{
if(has("dom-attributes-specified-flag")){
_2d8=_2a7.filter(node.attributes,function(a){
return a.specified;
});
}else{
var _2d9=/^input$|^img$/i.test(node.nodeName)?node:node.cloneNode(false),_2da=_2d9.outerHTML.replace(/=[^\s"']+|="[^"]*"|='[^']*'/g,"").replace(/^\s*<[a-zA-Z0-9]*\s*/,"").replace(/\s*>.*$/,"");
_2d8=_2a7.map(_2da.split(/\s+/),function(name){
var _2db=name.toLowerCase();
return {name:name,value:(node.nodeName=="LI"&&name=="value")||_2db=="enctype"?node.getAttribute(_2db):node.getAttributeNode(_2db).value};
});
}
}
var _2dc=_2d3.scope||dojo._scopeName,_2dd="data-"+_2dc+"-",hash={};
if(_2dc!=="dojo"){
hash[_2dd+"props"]="data-dojo-props";
hash[_2dd+"type"]="data-dojo-type";
hash[_2dd+"mixins"]="data-dojo-mixins";
hash[_2dc+"type"]="dojoType";
hash[_2dd+"id"]="data-dojo-id";
}
var i=0,item,_2de=[],_2df,_2e0;
while(item=_2d8[i++]){
var name=item.name,_2e1=name.toLowerCase(),_2e2=item.value;
switch(hash[_2e1]||_2e1){
case "data-dojo-type":
case "dojotype":
case "data-dojo-mixins":
break;
case "data-dojo-props":
_2e0=_2e2;
break;
case "data-dojo-id":
case "jsid":
_2df=_2e2;
break;
case "data-dojo-attach-point":
case "dojoattachpoint":
_2d7.dojoAttachPoint=_2e2;
break;
case "data-dojo-attach-event":
case "dojoattachevent":
_2d7.dojoAttachEvent=_2e2;
break;
case "class":
_2d7["class"]=node.className;
break;
case "style":
_2d7["style"]=node.style&&node.style.cssText;
break;
default:
if(!(name in _2d6)){
var map=_2b2(ctor);
name=map[_2e1]||name;
}
if(name in _2d6){
switch(typeof _2d6[name]){
case "string":
_2d7[name]=_2e2;
break;
case "number":
_2d7[name]=_2e2.length?Number(_2e2):NaN;
break;
case "boolean":
_2d7[name]=_2e2.toLowerCase()!="false";
break;
case "function":
if(_2e2===""||_2e2.search(/[^\w\.]+/i)!=-1){
_2d7[name]=new Function(_2e2);
}else{
_2d7[name]=_2a6.getObject(_2e2,false)||new Function(_2e2);
}
_2de.push(name);
break;
default:
var pVal=_2d6[name];
_2d7[name]=(pVal&&"length" in pVal)?(_2e2?_2e2.split(/\s*,\s*/):[]):(pVal instanceof Date)?(_2e2==""?new Date(""):_2e2=="now"?new Date():_2ac.fromISOString(_2e2)):(pVal instanceof _2aa)?(dojo.baseUrl+_2e2):_2b0(_2e2);
}
}else{
_2d7[name]=_2e2;
}
}
}
for(var j=0;j<_2de.length;j++){
var _2e3=_2de[j].toLowerCase();
node.removeAttribute(_2e3);
node[_2e3]=null;
}
if(_2e0){
try{
_2e0=_2b0.call(_2d3.propsThis,"{"+_2e0+"}");
_2a6.mixin(_2d7,_2e0);
}
catch(e){
throw new Error(e.toString()+" in data-dojo-props='"+_2e0+"'");
}
}
_2a6.mixin(_2d7,_2d2);
if(!_2d4){
_2d4=(ctor&&(ctor._noScript||_2d6._noScript)?[]:_2ae("> script[type^='dojo/']",node));
}
var _2e4=[],_2e5=[],_2e6=[],ons=[];
if(_2d4){
for(i=0;i<_2d4.length;i++){
var _2e7=_2d4[i];
node.removeChild(_2e7);
var _2e8=(_2e7.getAttribute(_2dd+"event")||_2e7.getAttribute("event")),prop=_2e7.getAttribute(_2dd+"prop"),_2e9=_2e7.getAttribute(_2dd+"method"),_2ea=_2e7.getAttribute(_2dd+"advice"),_2eb=_2e7.getAttribute("type"),nf=this._functionFromScript(_2e7,_2dd);
if(_2e8){
if(_2eb=="dojo/connect"){
_2e4.push({method:_2e8,func:nf});
}else{
if(_2eb=="dojo/on"){
ons.push({event:_2e8,func:nf});
}else{
_2d7[_2e8]=nf;
}
}
}else{
if(_2eb=="dojo/aspect"){
_2e4.push({method:_2e9,advice:_2ea,func:nf});
}else{
if(_2eb=="dojo/watch"){
_2e6.push({prop:prop,func:nf});
}else{
_2e5.push(nf);
}
}
}
}
}
var _2ec=ctor.markupFactory||_2d6.markupFactory;
var _2ed=_2ec?_2ec(_2d7,node,ctor):new ctor(_2d7,node);
function _2ee(_2ef){
if(_2df){
_2a6.setObject(_2df,_2ef);
}
for(i=0;i<_2e4.length;i++){
_2ab[_2e4[i].advice||"after"](_2ef,_2e4[i].method,_2a6.hitch(_2ef,_2e4[i].func),true);
}
for(i=0;i<_2e5.length;i++){
_2e5[i].call(_2ef);
}
for(i=0;i<_2e6.length;i++){
_2ef.watch(_2e6[i].prop,_2e6[i].func);
}
for(i=0;i<ons.length;i++){
don(_2ef,ons[i].event,ons[i].func);
}
return _2ef;
};
if(_2ed.then){
return _2ed.then(_2ee);
}else{
return _2ee(_2ed);
}
},scan:function(root,_2f0){
var list=[],mids=[],_2f1={};
var _2f2=(_2f0.scope||dojo._scopeName)+"Type",_2f3="data-"+(_2f0.scope||dojo._scopeName)+"-",_2f4=_2f3+"type",_2f5=_2f3+"textdir",_2f6=_2f3+"mixins";
var node=root.firstChild;
var _2f7=_2f0.inherited;
if(!_2f7){
function _2f8(node,attr){
return (node.getAttribute&&node.getAttribute(attr))||(node.parentNode&&_2f8(node.parentNode,attr));
};
_2f7={dir:_2f8(root,"dir"),lang:_2f8(root,"lang"),textDir:_2f8(root,_2f5)};
for(var key in _2f7){
if(!_2f7[key]){
delete _2f7[key];
}
}
}
var _2f9={inherited:_2f7};
var _2fa;
var _2fb;
function _2fc(_2fd){
if(!_2fd.inherited){
_2fd.inherited={};
var node=_2fd.node,_2fe=_2fc(_2fd.parent);
var _2ff={dir:node.getAttribute("dir")||_2fe.dir,lang:node.getAttribute("lang")||_2fe.lang,textDir:node.getAttribute(_2f5)||_2fe.textDir};
for(var key in _2ff){
if(_2ff[key]){
_2fd.inherited[key]=_2ff[key];
}
}
}
return _2fd.inherited;
};
while(true){
if(!node){
if(!_2f9||!_2f9.node){
break;
}
node=_2f9.node.nextSibling;
_2fb=false;
_2f9=_2f9.parent;
_2fa=_2f9.scripts;
continue;
}
if(node.nodeType!=1){
node=node.nextSibling;
continue;
}
if(_2fa&&node.nodeName.toLowerCase()=="script"){
type=node.getAttribute("type");
if(type&&/^dojo\/\w/i.test(type)){
_2fa.push(node);
}
node=node.nextSibling;
continue;
}
if(_2fb){
node=node.nextSibling;
continue;
}
var type=node.getAttribute(_2f4)||node.getAttribute(_2f2);
var _300=node.firstChild;
if(!type&&(!_300||(_300.nodeType==3&&!_300.nextSibling))){
node=node.nextSibling;
continue;
}
var _301;
var ctor=null;
if(type){
var _302=node.getAttribute(_2f6),_303=_302?[type].concat(_302.split(/\s*,\s*/)):[type];
try{
ctor=_2b5(_303,_2f0.contextRequire);
}
catch(e){
}
if(!ctor){
_2a7.forEach(_303,function(t){
if(~t.indexOf("/")&&!_2f1[t]){
_2f1[t]=true;
mids[mids.length]=t;
}
});
}
var _304=ctor&&!ctor.prototype._noScript?[]:null;
_301={types:_303,ctor:ctor,parent:_2f9,node:node,scripts:_304};
_301.inherited=_2fc(_301);
list.push(_301);
}else{
_301={node:node,scripts:_2fa,parent:_2f9};
}
_2fa=_304;
_2fb=node.stopParser||(ctor&&ctor.prototype.stopParser&&!(_2f0.template));
_2f9=_301;
node=_300;
}
var d=new _2ad();
if(mids.length){
if(has("dojo-debug-messages")){
console.warn("WARNING: Modules being Auto-Required: "+mids.join(", "));
}
var r=_2f0.contextRequire||_2a5;
r(mids,function(){
d.resolve(_2a7.filter(list,function(_305){
if(!_305.ctor){
try{
_305.ctor=_2b5(_305.types,_2f0.contextRequire);
}
catch(e){
}
}
var _306=_305.parent;
while(_306&&!_306.types){
_306=_306.parent;
}
var _307=_305.ctor&&_305.ctor.prototype;
_305.instantiateChildren=!(_307&&_307.stopParser&&!(_2f0.template));
_305.instantiate=!_306||(_306.instantiate&&_306.instantiateChildren);
return _305.instantiate;
}));
});
}else{
d.resolve(list);
}
return d.promise;
},_require:function(_308,_309){
var hash=_2b0("{"+_308.innerHTML+"}"),vars=[],mids=[],d=new _2ad();
var _30a=(_309&&_309.contextRequire)||_2a5;
for(var name in hash){
vars.push(name);
mids.push(hash[name]);
}
_30a(mids,function(){
for(var i=0;i<vars.length;i++){
_2a6.setObject(vars[i],arguments[i]);
}
d.resolve(arguments);
});
return d.promise;
},_scanAmd:function(root,_30b){
var _30c=new _2ad(),_30d=_30c.promise;
_30c.resolve(true);
var self=this;
_2ae("script[type='dojo/require']",root).forEach(function(node){
_30d=_30d.then(function(){
return self._require(node,_30b);
});
node.parentNode.removeChild(node);
});
return _30d;
},parse:function(_30e,_30f){
if(_30e&&typeof _30e!="string"&&!("nodeType" in _30e)){
_30f=_30e;
_30e=_30f.rootNode;
}
var root=_30e?dom.byId(_30e):_2a9.body();
_30f=_30f||{};
var _310=_30f.template?{template:true}:{},_311=[],self=this;
var p=this._scanAmd(root,_30f).then(function(){
return self.scan(root,_30f);
}).then(function(_312){
return self._instantiate(_312,_310,_30f,true);
}).then(function(_313){
return _311=_311.concat(_313);
}).otherwise(function(e){
console.error("dojo/parser::parse() error",e);
throw e;
});
_2a6.mixin(_311,p);
return _311;
}};
if(1){
dojo.parser=_2b9;
}
if(_2a8.parseOnLoad){
_2af(100,_2b9,"parse");
}
return _2b9;
});
},"dijit/form/ToggleButton":function(){
define(["dojo/_base/declare","dojo/_base/kernel","./Button","./_ToggleButtonMixin"],function(_314,_315,_316,_317){
return _314("dijit.form.ToggleButton",[_316,_317],{baseClass:"dijitToggleButton",setChecked:function(_318){
_315.deprecated("setChecked("+_318+") is deprecated. Use set('checked',"+_318+") instead.","","2.0");
this.set("checked",_318);
}});
});
},"dojo/date/stamp":function(){
define(["../_base/lang","../_base/array"],function(lang,_319){
var _31a={};
lang.setObject("dojo.date.stamp",_31a);
_31a.fromISOString=function(_31b,_31c){
if(!_31a._isoRegExp){
_31a._isoRegExp=/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
}
var _31d=_31a._isoRegExp.exec(_31b),_31e=null;
if(_31d){
_31d.shift();
if(_31d[1]){
_31d[1]--;
}
if(_31d[6]){
_31d[6]*=1000;
}
if(_31c){
_31c=new Date(_31c);
_319.forEach(_319.map(["FullYear","Month","Date","Hours","Minutes","Seconds","Milliseconds"],function(prop){
return _31c["get"+prop]();
}),function(_31f,_320){
_31d[_320]=_31d[_320]||_31f;
});
}
_31e=new Date(_31d[0]||1970,_31d[1]||0,_31d[2]||1,_31d[3]||0,_31d[4]||0,_31d[5]||0,_31d[6]||0);
if(_31d[0]<100){
_31e.setFullYear(_31d[0]||1970);
}
var _321=0,_322=_31d[7]&&_31d[7].charAt(0);
if(_322!="Z"){
_321=((_31d[8]||0)*60)+(Number(_31d[9])||0);
if(_322!="-"){
_321*=-1;
}
}
if(_322){
_321-=_31e.getTimezoneOffset();
}
if(_321){
_31e.setTime(_31e.getTime()+_321*60000);
}
}
return _31e;
};
_31a.toISOString=function(_323,_324){
var _325=function(n){
return (n<10)?"0"+n:n;
};
_324=_324||{};
var _326=[],_327=_324.zulu?"getUTC":"get",date="";
if(_324.selector!="time"){
var year=_323[_327+"FullYear"]();
date=["0000".substr((year+"").length)+year,_325(_323[_327+"Month"]()+1),_325(_323[_327+"Date"]())].join("-");
}
_326.push(date);
if(_324.selector!="date"){
var time=[_325(_323[_327+"Hours"]()),_325(_323[_327+"Minutes"]()),_325(_323[_327+"Seconds"]())].join(":");
var _328=_323[_327+"Milliseconds"]();
if(_324.milliseconds){
time+="."+(_328<100?"0":"")+_325(_328);
}
if(_324.zulu){
time+="Z";
}else{
if(_324.selector!="time"){
var _329=_323.getTimezoneOffset();
var _32a=Math.abs(_329);
time+=(_329>0?"-":"+")+_325(Math.floor(_32a/60))+":"+_325(_32a%60);
}
}
_326.push(time);
}
return _326.join("T");
};
return _31a;
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_32b,lang,_32c,when){
return _32b("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_32d){
if(_32d){
this.set(_32d);
}
},_get:function(name,_32e){
return typeof this[_32e.g]==="function"?this[_32e.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,_32f){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var _330=this._getAttrNames(name),_331=this._get(name,_330),_332=this[_330.s],_333;
if(typeof _332==="function"){
_333=_332.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=_32f;
}
if(this._watchCallbacks){
var self=this;
when(_333,function(){
self._watchCallbacks(name,_331,_32f);
});
}
return this;
},_changeAttrValue:function(name,_334){
var _335=this.get(name);
this[name]=_334;
if(this._watchCallbacks){
this._watchCallbacks(name,_335,_334);
}
return this;
},watch:function(name,_336){
var _337=this._watchCallbacks;
if(!_337){
var self=this;
_337=this._watchCallbacks=function(name,_338,_339,_33a){
var _33b=function(_33c){
if(_33c){
_33c=_33c.slice();
for(var i=0,l=_33c.length;i<l;i++){
_33c[i].call(self,name,_338,_339);
}
}
};
_33b(_337["_"+name]);
if(!_33a){
_33b(_337["*"]);
}
};
}
if(!_336&&typeof name==="function"){
_336=name;
name="*";
}else{
name="_"+name;
}
var _33d=_337[name];
if(typeof _33d!=="object"){
_33d=_337[name]=[];
}
_33d.push(_336);
var _33e={};
_33e.unwatch=_33e.remove=function(){
var _33f=_32c.indexOf(_33d,_336);
if(_33f>-1){
_33d.splice(_33f,1);
}
};
return _33e;
}});
});
},"dijit/form/_AutoCompleterMixin":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/query","dojo/regexp","dojo/sniff","./DataList","./_TextBoxMixin","./_SearchMixin"],function(_340,_341,_342,keys,lang,_343,_344,has,_345,_346,_347){
var _348=_341("dijit.form._AutoCompleterMixin",_347,{item:null,autoComplete:true,highlightMatch:"first",labelAttr:"",labelType:"text",maxHeight:-1,_stopClickEvents:false,_getCaretPos:function(_349){
var pos=0;
if(typeof (_349.selectionStart)=="number"){
pos=_349.selectionStart;
}else{
if(has("ie")){
var tr=_349.ownerDocument.selection.createRange().duplicate();
var ntr=_349.createTextRange();
tr.move("character",0);
ntr.move("character",0);
try{
ntr.setEndPoint("EndToEnd",tr);
pos=String(ntr.text).replace(/\r/g,"").length;
}
catch(e){
}
}
}
return pos;
},_setCaretPos:function(_34a,_34b){
_34b=parseInt(_34b);
_346.selectInputText(_34a,_34b,_34b);
},_setDisabledAttr:function(_34c){
this.inherited(arguments);
this.domNode.setAttribute("aria-disabled",_34c?"true":"false");
},_onKey:function(evt){
if(evt.charCode>=32){
return;
}
var key=evt.charCode||evt.keyCode;
if(key==keys.ALT||key==keys.CTRL||key==keys.META||key==keys.SHIFT){
return;
}
var pw=this.dropDown;
var _34d=null;
this._abortQuery();
this.inherited(arguments);
if(evt.altKey||evt.ctrlKey||evt.metaKey){
return;
}
if(this._opened){
_34d=pw.getHighlightedOption();
}
switch(key){
case keys.PAGE_DOWN:
case keys.DOWN_ARROW:
case keys.PAGE_UP:
case keys.UP_ARROW:
if(this._opened){
this._announceOption(_34d);
}
evt.stopPropagation();
evt.preventDefault();
break;
case keys.ENTER:
if(_34d){
if(_34d==pw.nextButton){
this._nextSearch(1);
evt.stopPropagation();
evt.preventDefault();
break;
}else{
if(_34d==pw.previousButton){
this._nextSearch(-1);
evt.stopPropagation();
evt.preventDefault();
break;
}
}
evt.stopPropagation();
evt.preventDefault();
}else{
this._setBlurValue();
this._setCaretPos(this.focusNode,this.focusNode.value.length);
}
case keys.TAB:
var _34e=this.get("displayedValue");
if(pw&&(_34e==pw._messages["previousMessage"]||_34e==pw._messages["nextMessage"])){
break;
}
if(_34d){
this._selectOption(_34d);
}
case keys.ESCAPE:
if(this._opened){
this._lastQuery=null;
this.closeDropDown();
}
break;
}
},_autoCompleteText:function(text){
var fn=this.focusNode;
_346.selectInputText(fn,fn.value.length);
var _34f=this.ignoreCase?"toLowerCase":"substr";
if(text[_34f](0).indexOf(this.focusNode.value[_34f](0))==0){
var cpos=this.autoComplete?this._getCaretPos(fn):fn.value.length;
if((cpos+1)>fn.value.length){
fn.value=text;
_346.selectInputText(fn,cpos);
}
}else{
fn.value=text;
_346.selectInputText(fn);
}
},_openResultList:function(_350,_351,_352){
var _353=this.dropDown.getHighlightedOption();
this.dropDown.clearResultList();
if(!_350.length&&_352.start==0){
this.closeDropDown();
return;
}
this._nextSearch=this.dropDown.onPage=lang.hitch(this,function(_354){
_350.nextPage(_354!==-1);
this.focus();
});
this.dropDown.createOptions(_350,_352,lang.hitch(this,"_getMenuLabelFromItem"));
this._showResultList();
if("direction" in _352){
if(_352.direction){
this.dropDown.highlightFirstOption();
}else{
if(!_352.direction){
this.dropDown.highlightLastOption();
}
}
if(_353){
this._announceOption(this.dropDown.getHighlightedOption());
}
}else{
if(this.autoComplete&&!this._prev_key_backspace&&!/^[*]+$/.test(_351[this.searchAttr].toString())){
this._announceOption(this.dropDown.containerNode.firstChild.nextSibling);
}
}
},_showResultList:function(){
this.closeDropDown(true);
this.openDropDown();
this.domNode.setAttribute("aria-expanded","true");
},loadDropDown:function(){
this._startSearchAll();
},isLoaded:function(){
return false;
},closeDropDown:function(){
this._abortQuery();
if(this._opened){
this.inherited(arguments);
this.domNode.setAttribute("aria-expanded","false");
}
},_setBlurValue:function(){
var _355=this.get("displayedValue");
var pw=this.dropDown;
if(pw&&(_355==pw._messages["previousMessage"]||_355==pw._messages["nextMessage"])){
this._setValueAttr(this._lastValueReported,true);
}else{
if(typeof this.item=="undefined"){
this.item=null;
this.set("displayedValue",_355);
}else{
if(this.value!=this._lastValueReported){
this._handleOnChange(this.value,true);
}
this._refreshState();
}
}
this.focusNode.removeAttribute("aria-activedescendant");
},_setItemAttr:function(item,_356,_357){
var _358="";
if(item){
if(!_357){
_357=this.store._oldAPI?this.store.getValue(item,this.searchAttr):item[this.searchAttr];
}
_358=this._getValueField()!=this.searchAttr?this.store.getIdentity(item):_357;
}
this.set("value",_358,_356,_357,item);
},_announceOption:function(node){
if(!node){
return;
}
var _359;
if(node==this.dropDown.nextButton||node==this.dropDown.previousButton){
_359=node.innerHTML;
this.item=undefined;
this.value="";
}else{
var item=this.dropDown.items[node.getAttribute("item")];
_359=(this.store._oldAPI?this.store.getValue(item,this.searchAttr):item[this.searchAttr]).toString();
this.set("item",item,false,_359);
}
this.focusNode.value=this.focusNode.value.substring(0,this._lastInput.length);
this.focusNode.setAttribute("aria-activedescendant",_342.get(node,"id"));
this._autoCompleteText(_359);
},_selectOption:function(_35a){
this.closeDropDown();
if(_35a){
this._announceOption(_35a);
}
this._setCaretPos(this.focusNode,this.focusNode.value.length);
this._handleOnChange(this.value,true);
this.focusNode.removeAttribute("aria-activedescendant");
},_startSearchAll:function(){
this._startSearch("");
},_startSearchFromInput:function(){
this.item=undefined;
this.inherited(arguments);
},_startSearch:function(key){
if(!this.dropDown){
var _35b=this.id+"_popup",_35c=lang.isString(this.dropDownClass)?lang.getObject(this.dropDownClass,false):this.dropDownClass;
this.dropDown=new _35c({onChange:lang.hitch(this,this._selectOption),id:_35b,dir:this.dir,textDir:this.textDir});
}
this._lastInput=key;
this.inherited(arguments);
},_getValueField:function(){
return this.searchAttr;
},postMixInProperties:function(){
this.inherited(arguments);
if(!this.store&&this.srcNodeRef){
var _35d=this.srcNodeRef;
this.store=new _345({},_35d);
if(!("value" in this.params)){
var item=(this.item=this.store.fetchSelectedItem());
if(item){
var _35e=this._getValueField();
this.value=this.store._oldAPI?this.store.getValue(item,_35e):item[_35e];
}
}
}
},postCreate:function(){
var _35f=_343("label[for=\""+this.id+"\"]");
if(_35f.length){
if(!_35f[0].id){
_35f[0].id=this.id+"_label";
}
this.domNode.setAttribute("aria-labelledby",_35f[0].id);
}
this.inherited(arguments);
_340.after(this,"onSearch",lang.hitch(this,"_openResultList"),true);
},_getMenuLabelFromItem:function(item){
var _360=this.labelFunc(item,this.store),_361=this.labelType;
if(this.highlightMatch!="none"&&this.labelType=="text"&&this._lastInput){
_360=this.doHighlight(_360,this._lastInput);
_361="html";
}
return {html:_361=="html",label:_360};
},doHighlight:function(_362,find){
var _363=(this.ignoreCase?"i":"")+(this.highlightMatch=="all"?"g":""),i=this.queryExpr.indexOf("${0}");
find=_344.escapeString(find);
return this._escapeHtml(_362.replace(new RegExp((i==0?"^":"")+"("+find+")"+(i==(this.queryExpr.length-4)?"$":""),_363),"\uffff$1\uffff")).replace(/\uFFFF([^\uFFFF]+)\uFFFF/g,"<span class=\"dijitComboBoxHighlightMatch\">$1</span>");
},_escapeHtml:function(str){
str=String(str).replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
return str;
},reset:function(){
this.item=null;
this.inherited(arguments);
},labelFunc:function(item,_364){
return (_364._oldAPI?_364.getValue(item,this.labelAttr||this.searchAttr):item[this.labelAttr||this.searchAttr]).toString();
},_setValueAttr:function(_365,_366,_367,item){
this._set("item",item||null);
if(_365==null){
_365="";
}
this.inherited(arguments);
}});
if(has("dojo-bidi")){
_348.extend({_setTextDirAttr:function(_368){
this.inherited(arguments);
if(this.dropDown){
this.dropDown._set("textDir",_368);
}
}});
}
return _348;
});
},"dojo/NodeList-traverse":function(){
define(["./query","./_base/lang","./_base/array"],function(_369,lang,_36a){
var _36b=_369.NodeList;
lang.extend(_36b,{_buildArrayFromCallback:function(_36c){
var ary=[];
for(var i=0;i<this.length;i++){
var _36d=_36c.call(this[i],this[i],ary);
if(_36d){
ary=ary.concat(_36d);
}
}
return ary;
},_getUniqueAsNodeList:function(_36e){
var ary=[];
for(var i=0,node;node=_36e[i];i++){
if(node.nodeType==1&&_36a.indexOf(ary,node)==-1){
ary.push(node);
}
}
return this._wrap(ary,null,this._NodeListCtor);
},_getUniqueNodeListWithParent:function(_36f,_370){
var ary=this._getUniqueAsNodeList(_36f);
ary=(_370?_369._filterResult(ary,_370):ary);
return ary._stash(this);
},_getRelatedUniqueNodes:function(_371,_372){
return this._getUniqueNodeListWithParent(this._buildArrayFromCallback(_372),_371);
},children:function(_373){
return this._getRelatedUniqueNodes(_373,function(node,ary){
return lang._toArray(node.childNodes);
});
},closest:function(_374,root){
return this._getRelatedUniqueNodes(null,function(node,ary){
do{
if(_369._filterResult([node],_374,root).length){
return node;
}
}while(node!=root&&(node=node.parentNode)&&node.nodeType==1);
return null;
});
},parent:function(_375){
return this._getRelatedUniqueNodes(_375,function(node,ary){
return node.parentNode;
});
},parents:function(_376){
return this._getRelatedUniqueNodes(_376,function(node,ary){
var pary=[];
while(node.parentNode){
node=node.parentNode;
pary.push(node);
}
return pary;
});
},siblings:function(_377){
return this._getRelatedUniqueNodes(_377,function(node,ary){
var pary=[];
var _378=(node.parentNode&&node.parentNode.childNodes);
for(var i=0;i<_378.length;i++){
if(_378[i]!=node){
pary.push(_378[i]);
}
}
return pary;
});
},next:function(_379){
return this._getRelatedUniqueNodes(_379,function(node,ary){
var next=node.nextSibling;
while(next&&next.nodeType!=1){
next=next.nextSibling;
}
return next;
});
},nextAll:function(_37a){
return this._getRelatedUniqueNodes(_37a,function(node,ary){
var pary=[];
var next=node;
while((next=next.nextSibling)){
if(next.nodeType==1){
pary.push(next);
}
}
return pary;
});
},prev:function(_37b){
return this._getRelatedUniqueNodes(_37b,function(node,ary){
var prev=node.previousSibling;
while(prev&&prev.nodeType!=1){
prev=prev.previousSibling;
}
return prev;
});
},prevAll:function(_37c){
return this._getRelatedUniqueNodes(_37c,function(node,ary){
var pary=[];
var prev=node;
while((prev=prev.previousSibling)){
if(prev.nodeType==1){
pary.push(prev);
}
}
return pary;
});
},andSelf:function(){
return this.concat(this._parent);
},first:function(){
return this._wrap(((this[0]&&[this[0]])||[]),this);
},last:function(){
return this._wrap((this.length?[this[this.length-1]]:[]),this);
},even:function(){
return this.filter(function(item,i){
return i%2!=0;
});
},odd:function(){
return this.filter(function(item,i){
return i%2==0;
});
}});
return _36b;
});
},"idx/form/_CompositeMixin":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/aspect","dojo/dom-attr","dojo/dom","dojo/dom-construct","dojo/dom-geometry","dojo/i18n","dojo/query","dojo/dom-class","dojo/dom-style","dojo/on","dijit/_base/wai","idx/widget/HoverHelpTooltip","../util","../string"],function(_37d,lang,_37e,_37f,dom,_380,_381,i18n,_382,_383,_384,on,wai,_385,_386,_387,_388){
return _37d("idx.form._CompositeMixin",null,{labelAlignment:"horizontal",label:"",labelWidth:"",fieldWidth:"",hintPosition:"inside",hint:"",required:false,unit:"",help:"",_errorIconWidth:27,postMixInProperties:function(){
this.tooltipPosition=["after-centered","above"];
this.inherited(arguments);
},resize:function(){
if(this._holdResize()){
return;
}
_384.set(this.domNode,{visibility:"hidden"});
if(_386.isPercentage(this._styleWidth)){
_384.set(this.labelWrap,{width:""});
_384.set(this.oneuiBaseNode,{width:""});
}
if(this._resizeTimeout){
clearTimeout(this._resizeTimeout);
delete this._resizeTimeout;
}
this._resizeTimeout=setTimeout(lang.hitch(this,this._resize),250);
},_holdResize:function(_389){
if(!this.domNode){
return true;
}
if(!(this.labelWidth||this.fieldWidth||this._styleWidth)){
_384.set(this.labelWrap,{width:""});
_384.set(this.oneuiBaseNode,{width:""});
return true;
}
var _38a=(this._styleWidth&&!_386.isPercentage(this._styleWidth)),_38b=(this.fieldWidth&&!_386.isPercentage(this.fieldWidth)),_38c=(this.labelWidth&&!_386.isPercentage(this.labelWidth)),_38d=_38a||(_38b&&_38c),_389=_381.getContentBox(this.domNode).w<=0;
return (_38d&&this._resized)||(!_38d)&&_389;
},_deferResize:function(){
if(!this._started){
if(!this._resizeHandle){
this._resizeHandle=_37e.after(this,"startup",lang.hitch(this,this._resize));
}
return true;
}else{
if(this._resizeHandle){
this._resizeHandle.remove();
delete this._resizeHandle;
}
return false;
}
},_resize:function(){
if(this._resizeTimeout){
clearTimeout(this._resizeTimeout);
delete this._resizeTimeout;
}
var _38e=this._deferResize();
if(_38e){
return;
}
if(this._holdResize()){
return;
}
var _38f=this.labelWidth,_390=this.fieldWidth,_391=this._styleWidth;
var _392=_381.getContentBox(this.domNode).w,_392=((_392<=0)&&_391)?_386.normalizedLength(_391,this.domNode):_392;
if(!_391){
if(_386.isPercentage(_38f)){
_384.set(this.labelWrap,"width","");
}
if(_386.isPercentage(_390)){
_384.set(this.oneuiBaseNode,"width","");
}
}
if(this.label&&this.labelAlignment=="horizontal"){
if(_38f){
if(_386.isPercentage(_38f)){
_38f=Math.floor(_392*parseInt(_38f)/100)-10;
}
_384.set(this.labelWrap,"width",_386.normalizedLength(_38f,this.domNode)+"px");
}else{
_384.set(this.labelWrap,"width","");
}
if(_390){
var _393=_386.isPercentage(_390);
if(_393){
_390=Math.floor(_392*parseInt(_390)/100);
}
_384.set(this.oneuiBaseNode,"width",_386.normalizedLength(_390,this.domNode)-(_393?(this._errorIconWidth+2):0)+"px");
}else{
_384.set(this.oneuiBaseNode,"width","");
}
if(_391&&!_390){
var _394=_381.getMarginBox(this.labelWrap).w;
var _395=_392-this._errorIconWidth-_394-2;
if(_395>0){
_384.set(this.oneuiBaseNode,"width",_395+"px");
}
}
}else{
if(_391){
_384.set(this.oneuiBaseNode,"width",_392-this._errorIconWidth-2+"px");
}else{
if(_390&&!_386.isPercentage(_390)){
_384.set(this.oneuiBaseNode,"width",_386.normalizedLength(_390,this.domNode)+"px");
}else{
_384.set(this.oneuiBaseNode,"width","");
}
}
}
this._resizeHint();
_384.set(this.domNode,{visibility:""});
this._resized=true;
},_resizeHint:function(){
if(this.hint&&(this.hintPosition=="outside")&&this._created){
var _396=_384.get(this.fieldWrap||this.oneuiBaseNode,"width");
_384.set(this.compHintNode,"width",_396?_396+"px":"");
}
},_setStyleAttr:function(_397){
this.inherited(arguments);
this._styleWidth=_386.getValidCSSWidth(_397);
this._created&&this._resize();
},_setLabelWidthAttr:function(_398){
_384.set(this.labelWrap,"width",typeof _398==="number"?_398+"px":_398);
this._set("labelWidth",_398);
this._created&&this._resize();
},_setFieldWidthAttr:function(_399){
if(!_386.isPercentage(_399)){
_384.set(this.oneuiBaseNode,"width",typeof _399==="number"?_399+"px":_399);
}
this._set("fieldWidth",_399);
this._created&&this._resize();
},_setLabelAlignmentAttr:function(_39a){
var h=_39a=="horizontal";
_383.toggle(this.labelWrap,"dijitInline",h);
_382(".idxCompContainer",this.domNode).toggleClass("dijitInline",h);
this._set("labelAlignment",_39a);
this._resize();
},_setupHelpListener:function(){
if(this._helpListener){
return;
}
var _39b=function(e){
if(this.widget.helpTooltip){
this.widget.helpTooltip.close(true);
}
if(this.handle){
this.handle.remove();
delete this.handle;
this.widget._blurHandler=null;
}
};
var _39c=function(e){
var _39d=e.keyCode;
if(e.type=="keydown"){
if(e.ctrlKey&&e.shiftKey&&(_39d==191)&&this.helpTooltip&&this._help){
this.helpTooltip.open(this.helpNode);
if(e.target&&!this._blurHandler){
var _39e={handle:null,widget:this};
this._blurHandler=_39e.handle=on(e.target,"blur",lang.hitch(_39e,_39b));
}
}
}
};
var node=(this.oneuiBaseNode)?this.oneuiBaseNode:this.focusNode;
if(node){
this._keyListener=this.own(on(node,"keydown",lang.hitch(this,_39c)));
}
},_setHelpAttr:function(_39f){
this.help=_39f;
this._help=_39f=_387.nullTrim(_39f);
if(_39f){
if(!this.helpNode){
this.helpNode=_380.toDom("<div class='dijitInline idxTextBoxHoverHelp'><span class='idxHelpIconAltText'>?</span></div>");
_380.place(this.helpNode,this.compLabelNode,"after");
this.helpTooltip=new _385({connectId:[this.helpNode],label:_39f,position:["above","below"],forceFocus:false,textDir:this.textDir});
this._setupHelpListener();
}else{
this._setupHelpListener();
this.helpTooltip.set("label",_39f);
_383.remove(this.helpNode,"dijitHidden");
}
}else{
if(this.helpNode){
this.helpTooltip.set("label","");
_383.add(this.helpNode,"dijitHidden");
}
}
},_setLabelAttr:function(_3a0){
this.compLabelNode.innerHTML=_3a0;
var _3a1=/^\s*$/.test(_3a0);
_37f[_3a1?"remove":"set"](this.compLabelNode,"for",this.id);
_383.toggle(this.labelWrap,"dijitHidden",_3a1);
this._set("label",_3a0);
},_setRequiredAttr:function(_3a2){
wai.setWaiState(this.focusNode,"required",_3a2+"");
this._set("required",_3a2);
this._refreshState();
},_refreshState:function(){
if(!this._created){
return;
}
if(this.disabled){
this._set("state","");
}else{
if(this.required&&this._isEmpty(this.value)&&(this.state=="")){
this._set("state","Incomplete");
}
}
},_setValueAttr:function(){
this.inherited(arguments);
this.validate(this.focused);
if(this.focusNode&&(!this._hasBeenBlurred)){
_37f.remove(this.focusNode,"aria-invalid");
}
},_setHintPositionAttr:function(_3a3){
if(!this.compHintNode){
return;
}
this._set("hintPosition",_3a3);
_383.toggle(this.compHintNode,"dijitVisible",_3a3!="inside");
this.set("hint",this.hint);
this._resizeHint();
},_setHintAttr:function(hint){
this.set("placeHolder",this.hintPosition=="inside"?hint:"");
if(!this.compHintNode){
return;
}
this.compHintNode.innerHTML=this.hintPosition=="inside"?"":hint;
if(this.hintPosition=="outside"){
_37f.set(this.compHintNode,"id",this.id+"_hint_outside");
}
if(this._placeholder===undefined||this._placeholder===false){
wai.setWaiState(this.focusNode,"describedby",this.id+"_hint_"+this.hintPosition);
}
this._set("hint",hint);
this._resizeHint();
},_setPlaceHolderAttr:function(v){
this._set("placeHolder",v);
if(v===null||v===undefined){
v="";
}
if(this.focusNode.placeholder!==undefined&&this._placeholder!==false){
_37f.set(this.focusNode,"placeholder",v);
this._placeholder=v;
}else{
if(!this._phspan){
this._attachPoints.push("_phspan");
this._phspan=_380.create("span",{className:"dijitPlaceHolder dijitInputField",id:this.id+"_hint_inside"},this.focusNode,"after");
}
this._phspan.innerHTML="";
this._phspan.appendChild(document.createTextNode(v));
this._phspan.style.display=(this.placeHolder&&!this.focused&&!this.textbox.value)?"":"none";
}
},_setUnitAttr:function(unit){
if(!this.compUnitNode){
return;
}
this.compUnitNode.innerHTML=unit;
_383.toggle(this.compUnitNode,"dijitHidden",/^\s*$/.test(unit));
this._set("unit",unit);
},reset:function(){
this.set("state",this.required?"Incomplete":"");
this.message="";
this.inherited(arguments);
},validateAndFocus:function(){
if(this.validate&&!this.disabled){
var _3a4=this._hasBeenBlurred;
this._hasBeenBlurred=true;
var _3a5=this.validate();
if(!_3a5){
this.focus();
}
this._hasBeenBlurred=_3a4;
return _3a5;
}
return true;
}});
});
},"dijit/form/MappedTextBox":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/dom-construct","./ValidationTextBox"],function(_3a6,has,_3a7,_3a8){
return _3a6("dijit.form.MappedTextBox",_3a8,{postMixInProperties:function(){
this.inherited(arguments);
this.nameAttrSetting="";
},_setNameAttr:"valueNode",serialize:function(val){
return val.toString?val.toString():"";
},toString:function(){
var val=this.filter(this.get("value"));
return val!=null?(typeof val=="string"?val:this.serialize(val,this.constraints)):"";
},validate:function(){
this.valueNode.value=this.toString();
return this.inherited(arguments);
},buildRendering:function(){
this.inherited(arguments);
this.valueNode=_3a7.place("<input type='hidden'"+((this.name&&!has("msapp"))?" name=\""+this.name.replace(/"/g,"&quot;")+"\"":"")+"/>",this.textbox,"after");
},reset:function(){
this.valueNode.value="";
this.inherited(arguments);
}});
});
},"dijit/form/ComboBoxMixin":function(){
define(["dojo/_base/declare","dojo/Deferred","dojo/_base/kernel","dojo/_base/lang","dojo/store/util/QueryResults","./_AutoCompleterMixin","./_ComboBoxMenu","../_HasDropDown","dojo/text!./templates/DropDownBox.html"],function(_3a9,_3aa,_3ab,lang,_3ac,_3ad,_3ae,_3af,_3b0){
return _3a9("dijit.form.ComboBoxMixin",[_3af,_3ad],{dropDownClass:_3ae,hasDownArrow:true,templateString:_3b0,baseClass:"dijitTextBox dijitComboBox",cssStateNodes:{"_buttonNode":"dijitDownArrowButton"},_setHasDownArrowAttr:function(val){
this._set("hasDownArrow",val);
this._buttonNode.style.display=val?"":"none";
},_showResultList:function(){
this.displayMessage("");
this.inherited(arguments);
},_setStoreAttr:function(_3b1){
if(!_3b1.get){
lang.mixin(_3b1,{_oldAPI:true,get:function(id){
var _3b2=new _3aa();
this.fetchItemByIdentity({identity:id,onItem:function(_3b3){
_3b2.resolve(_3b3);
},onError:function(_3b4){
_3b2.reject(_3b4);
}});
return _3b2.promise;
},query:function(_3b5,_3b6){
var _3b7=new _3aa(function(){
_3b8.abort&&_3b8.abort();
});
_3b7.total=new _3aa();
var _3b8=this.fetch(lang.mixin({query:_3b5,onBegin:function(_3b9){
_3b7.total.resolve(_3b9);
},onComplete:function(_3ba){
_3b7.resolve(_3ba);
},onError:function(_3bb){
_3b7.reject(_3bb);
}},_3b6));
return _3ac(_3b7);
}});
}
this._set("store",_3b1);
},postMixInProperties:function(){
var _3bc=this.params.store||this.store;
if(_3bc){
this._setStoreAttr(_3bc);
}
this.inherited(arguments);
if(!this.params.store&&this.store&&!this.store._oldAPI){
var _3bd=this.declaredClass;
lang.mixin(this.store,{getValue:function(item,attr){
_3ab.deprecated(_3bd+".store.getValue(item, attr) is deprecated for builtin store.  Use item.attr directly","","2.0");
return item[attr];
},getLabel:function(item){
_3ab.deprecated(_3bd+".store.getLabel(item) is deprecated for builtin store.  Use item.label directly","","2.0");
return item.name;
},fetch:function(args){
_3ab.deprecated(_3bd+".store.fetch() is deprecated for builtin store.","Use store.query()","2.0");
var shim=["dojo/data/ObjectStore"];
require(shim,lang.hitch(this,function(_3be){
new _3be({objectStore:this}).fetch(args);
}));
}});
}
},buildRendering:function(){
this.inherited(arguments);
this.focusNode.setAttribute("aria-autocomplete",this.autoComplete?"both":"list");
}});
});
},"dijit/form/_TextBoxMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/sniff","dojo/keys","dojo/_base/lang","dojo/on","../main"],function(_3bf,_3c0,dom,has,keys,lang,on,_3c1){
var _3c2=_3c0("dijit.form._TextBoxMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{trim:false,uppercase:false,lowercase:false,propercase:false,maxLength:"",selectOnClick:false,placeHolder:"",_getValueAttr:function(){
return this.parse(this.get("displayedValue"),this.constraints);
},_setValueAttr:function(_3c3,_3c4,_3c5){
var _3c6;
if(_3c3!==undefined){
_3c6=this.filter(_3c3);
if(typeof _3c5!="string"){
if(_3c6!==null&&((typeof _3c6!="number")||!isNaN(_3c6))){
_3c5=this.filter(this.format(_3c6,this.constraints));
}else{
_3c5="";
}
if(this.compare(_3c6,this.filter(this.parse(_3c5,this.constraints)))!=0){
_3c5=null;
}
}
}
if(_3c5!=null&&((typeof _3c5)!="number"||!isNaN(_3c5))&&this.textbox.value!=_3c5){
this.textbox.value=_3c5;
this._set("displayedValue",this.get("displayedValue"));
}
this.inherited(arguments,[_3c6,_3c4]);
},displayedValue:"",_getDisplayedValueAttr:function(){
return this.filter(this.textbox.value);
},_setDisplayedValueAttr:function(_3c7){
if(_3c7==null){
_3c7="";
}else{
if(typeof _3c7!="string"){
_3c7=String(_3c7);
}
}
this.textbox.value=_3c7;
this._setValueAttr(this.get("value"),undefined);
this._set("displayedValue",this.get("displayedValue"));
},format:function(_3c8){
return _3c8==null?"":(_3c8.toString?_3c8.toString():_3c8);
},parse:function(_3c9){
return _3c9;
},_refreshState:function(){
},onInput:function(){
},_onInput:function(evt){
this._lastInputEventValue=this.textbox.value;
this._processInput(this._lastInputProducingEvent||evt);
delete this._lastInputProducingEvent;
if(this.intermediateChanges){
this._handleOnChange(this.get("value"),false);
}
},_processInput:function(){
this._refreshState();
this._set("displayedValue",this.get("displayedValue"));
},postCreate:function(){
this.textbox.setAttribute("value",this.textbox.value);
this.inherited(arguments);
function _3ca(e){
var _3cb;
if(e.type=="keydown"&&e.keyCode!=229){
_3cb=e.keyCode;
switch(_3cb){
case keys.SHIFT:
case keys.ALT:
case keys.CTRL:
case keys.META:
case keys.CAPS_LOCK:
case keys.NUM_LOCK:
case keys.SCROLL_LOCK:
return;
}
if(!e.ctrlKey&&!e.metaKey&&!e.altKey){
switch(_3cb){
case keys.NUMPAD_0:
case keys.NUMPAD_1:
case keys.NUMPAD_2:
case keys.NUMPAD_3:
case keys.NUMPAD_4:
case keys.NUMPAD_5:
case keys.NUMPAD_6:
case keys.NUMPAD_7:
case keys.NUMPAD_8:
case keys.NUMPAD_9:
case keys.NUMPAD_MULTIPLY:
case keys.NUMPAD_PLUS:
case keys.NUMPAD_ENTER:
case keys.NUMPAD_MINUS:
case keys.NUMPAD_PERIOD:
case keys.NUMPAD_DIVIDE:
return;
}
if((_3cb>=65&&_3cb<=90)||(_3cb>=48&&_3cb<=57)||_3cb==keys.SPACE){
return;
}
var _3cc=false;
for(var i in keys){
if(keys[i]===e.keyCode){
_3cc=true;
break;
}
}
if(!_3cc){
return;
}
}
}
_3cb=e.charCode>=32?String.fromCharCode(e.charCode):e.charCode;
if(!_3cb){
_3cb=(e.keyCode>=65&&e.keyCode<=90)||(e.keyCode>=48&&e.keyCode<=57)||e.keyCode==keys.SPACE?String.fromCharCode(e.keyCode):e.keyCode;
}
if(!_3cb){
_3cb=229;
}
if(e.type=="keypress"){
if(typeof _3cb!="string"){
return;
}
if((_3cb>="a"&&_3cb<="z")||(_3cb>="A"&&_3cb<="Z")||(_3cb>="0"&&_3cb<="9")||(_3cb===" ")){
if(e.ctrlKey||e.metaKey||e.altKey){
return;
}
}
}
var faux={faux:true},attr;
for(attr in e){
if(!/^(layer[XY]|returnValue|keyLocation)$/.test(attr)){
var v=e[attr];
if(typeof v!="function"&&typeof v!="undefined"){
faux[attr]=v;
}
}
}
lang.mixin(faux,{charOrCode:_3cb,_wasConsumed:false,preventDefault:function(){
faux._wasConsumed=true;
e.preventDefault();
},stopPropagation:function(){
e.stopPropagation();
}});
this._lastInputProducingEvent=faux;
if(this.onInput(faux)===false){
faux.preventDefault();
faux.stopPropagation();
}
if(faux._wasConsumed){
return;
}
if(has("ie")<=9){
switch(e.keyCode){
case keys.TAB:
case keys.ESCAPE:
case keys.DOWN_ARROW:
case keys.UP_ARROW:
case keys.LEFT_ARROW:
case keys.RIGHT_ARROW:
break;
default:
if(e.keyCode==keys.ENTER&&this.textbox.tagName.toLowerCase()!="textarea"){
break;
}
this.defer(function(){
if(this.textbox.value!==this._lastInputEventValue){
on.emit(this.textbox,"input",{bubbles:true});
}
});
}
}
};
this.own(on(this.textbox,"keydown, keypress, paste, cut, compositionend",lang.hitch(this,_3ca)),on(this.textbox,"input",lang.hitch(this,"_onInput")),on(this.domNode,"keypress",function(e){
e.stopPropagation();
}));
},_blankValue:"",filter:function(val){
if(val===null){
return this._blankValue;
}
if(typeof val!="string"){
return val;
}
if(this.trim){
val=lang.trim(val);
}
if(this.uppercase){
val=val.toUpperCase();
}
if(this.lowercase){
val=val.toLowerCase();
}
if(this.propercase){
val=val.replace(/[^\s]+/g,function(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
});
}
return val;
},_setBlurValue:function(){
this._setValueAttr(this.get("value"),true);
},_onBlur:function(e){
if(this.disabled){
return;
}
this._setBlurValue();
this.inherited(arguments);
},_isTextSelected:function(){
return this.textbox.selectionStart!=this.textbox.selectionEnd;
},_onFocus:function(by){
if(this.disabled||this.readOnly){
return;
}
if(this.selectOnClick&&by=="mouse"){
this._selectOnClickHandle=on.once(this.domNode,"mouseup, touchend",lang.hitch(this,function(evt){
if(!this._isTextSelected()){
_3c2.selectInputText(this.textbox);
}
}));
this.own(this._selectOnClickHandle);
this.defer(function(){
if(this._selectOnClickHandle){
this._selectOnClickHandle.remove();
this._selectOnClickHandle=null;
}
},500);
}
this.inherited(arguments);
this._refreshState();
},reset:function(){
this.textbox.value="";
this.inherited(arguments);
}});
if(has("dojo-bidi")){
_3c2=_3c0("dijit.form._TextBoxMixin",_3c2,{_setValueAttr:function(){
this.inherited(arguments);
this.applyTextDir(this.focusNode);
},_setDisplayedValueAttr:function(){
this.inherited(arguments);
this.applyTextDir(this.focusNode);
},_onInput:function(){
this.applyTextDir(this.focusNode);
this.inherited(arguments);
}});
}
_3c2._setSelectionRange=_3c1._setSelectionRange=function(_3cd,_3ce,stop){
if(_3cd.setSelectionRange){
_3cd.setSelectionRange(_3ce,stop);
}
};
_3c2.selectInputText=_3c1.selectInputText=function(_3cf,_3d0,stop){
_3cf=dom.byId(_3cf);
if(isNaN(_3d0)){
_3d0=0;
}
if(isNaN(stop)){
stop=_3cf.value?_3cf.value.length:0;
}
try{
_3cf.focus();
_3c2._setSelectionRange(_3cf,_3d0,stop);
}
catch(e){
}
};
return _3c2;
});
},"dijit/_base/window":function(){
define(["dojo/window","../main"],function(_3d1,_3d2){
_3d2.getDocumentWindow=function(doc){
return _3d1.get(doc);
};
});
},"curam/ajax":function(){
define(["curam/util/Request"],function(_3d3){
var _3d4=function(_3d5,_3d6){
this.target=_3d5;
this.inputProvider=_3d6||"null";
};
var _3d7={doRequest:function(_3d8,_3d9,_3da,_3db){
var _3dc="../servlet/JSONServlet";
var _3dd=this;
if(_3da){
_3dc="../"+_3dc;
}
var _3de={caller:this.target.id,operation:_3d8,inputProvider:this.inputProvider,args:_3d9};
function _3df(_3e0,_3e1){
_3e0=dojo.fromJson(_3e0);
if(_3e0 instanceof Array){
if(_3e0.length>1){
if(_3e1=="getCodeTableSubset"){
_3dd.fillCTWithBlank(_3e0);
}else{
_3dd.fillCT(_3e0);
}
}else{
if(_3e1=="getCodeTableSubset"){
_3dd.fillCTWithBlank(_3e0);
}else{
_3dd.fillSingle(_3e0,true);
}
}
}else{
_3dd.fillSingle(_3e0);
}
};
_3d3.post({url:_3dc,handleAs:"text",load:function(data,evt){
_3df(data,_3d8);
},error:function(){
alert("error");
},content:{"content":dojo.toJson(_3de)},preventCache:true,sync:_3db});
},fillCT:function(_3e2){
this.target.options.length=0;
for(var i=0;i<_3e2.length;i++){
this.target.options[i]=new Option(_3e2[i]["descr"],_3e2[i]["code"],_3e2[i]["default"]);
}
},fillCTWithBlank:function(_3e3){
this.target.options.length=0;
this.target.options[0]=new Option("");
for(var i=0;i<_3e3.length;i++){
this.target.options[i+1]=new Option(_3e3[i]["descr"],_3e3[i]["code"]);
}
},fillSingle:function(_3e4,_3e5){
if(_3e5){
this.target.value=_3e4[0]["value"];
}else{
this.target.value=_3e4["value"];
}
}};
dojo.mixin(_3d4.prototype,_3d7);
dojo.global.AJAXCall=_3d4;
return _3d4;
});
},"curam/util/Dialog":function(){
define(["curam/util","curam/define","curam/dialog","dojo/on","curam/util/onLoad","curam/debug"],function(util,_3e6,_3e7,on,_3e8,_3e9){
_3e6.singleton("curam.util.Dialog",{_id:null,_unsubscribes:[],_modalWithIEGScript:false,open:function(path,_3ea,_3eb){
var url=path+curam.util.makeQueryString(_3ea);
var _3ec={href:url};
var _3ed=null;
if(_3eb){
_3ed="width="+_3eb.width+",height="+_3eb.height;
}
window.jsModals=true;
curam.util.openModalDialog(_3ec,_3ed);
},init:function(){
var _3ee=curam.util.getTopmostWindow();
var _3ef=_3ee.dojo.subscribe("/curam/dialog/SetId",null,function(_3f0){
curam.util.Dialog._id=_3f0;
curam.debug.log(_3e9.getProperty("curam.util.Dialog.id.success"),curam.util.Dialog._id);
_3ee.dojo.unsubscribe(_3ef);
});
curam.util.Dialog._unsubscribes.push(_3ef);
_3ee.dojo.publish("/curam/dialog/init");
if(!curam.util.Dialog._id){
curam.debug.log(_3e9.getProperty("curam.util.Dialog.id.fail"));
}
if(curam.util.Dialog._modalWithIEGScript){
dojo.addOnUnload(function(){
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}else{
var _3f1=on(window,"unload",function(){
_3f1.remove();
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}
},initModalWithIEGScript:function(){
curam.util.Dialog._modalWithIEGScript=true;
curam.util.Dialog.init();
},registerGetTitleFunc:function(_3f2){
curam.util.onLoad.addPublisher(function(_3f3){
_3f3.title=_3f2();
});
},registerGetSizeFunc:function(_3f4){
curam.util.onLoad.addPublisher(function(_3f5){
_3f5.windowOptions=_3f4();
});
},registerAfterDisplayHandler:function(_3f6){
var _3f7=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_3f7.dojo.subscribe("/curam/dialog/AfterDisplay",null,function(_3f8){
if(_3f8==curam.util.Dialog._id){
_3f6();
}
}));
},registerBeforeCloseHandler:function(_3f9){
var _3fa=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_3fa.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_3fb){
if(_3fb===curam.util.Dialog._id){
_3f9();
}
}));
},pageLoadFinished:function(){
var _3fc=curam.util.getTopmostWindow();
curam.util.Dialog._unsTokenReleaseHandlers=_3fc.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_3fd){
if(_3fd==curam.util.Dialog._id){
curam.util.Dialog._releaseHandlers(_3fc);
}
});
curam.util.onLoad.execute();
},_releaseHandlers:function(_3fe){
var _3ff;
if(_3fe){
_3ff=_3fe;
}else{
_3ff=curam.util.getTopmostWindow();
}
dojo.forEach(curam.util.Dialog._unsubscribes,_3ff.dojo.unsubscribe);
curam.util.Dialog._unsubscribes=[];
_3ff.dojo.unsubscribe(curam.util.Dialog._unsTokenReleaseHandlers);
curam.util.Dialog._unsTokenReleaseHandlers=null;
},close:function(_400,_401,_402){
var _403=curam.dialog.getParentWindow(window);
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _404=window.frameElement.id;
var _405=_404.substring(7);
curam.util.Dialog._id=_405;
_3e9.log("curam.util.Dialog.closeAndSubmitParent() "+_3e9.getProperty("curam.dialog.modal.id")+_405);
}
if(_400&&!_401){
curam.dialog.forceParentRefresh();
_403.curam.util.redirectWindow(null);
}else{
if(_401){
var _406=_401;
if(_401.indexOf("Page.do")==-1&&_401.indexOf("Action.do")==-1){
_406=_401+"Page.do"+curam.util.makeQueryString(_402);
}
_403.curam.util.redirectWindow(_406);
}
}
var _407=curam.util.getTopmostWindow();
_407.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},closeAndSubmitParent:function(_408){
var _409=curam.dialog.getParentWindow(window);
var _40a=_409.document.forms["mainForm"];
var _40b=curam.util.getTopmostWindow();
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _40c=window.frameElement.id;
var _40d=_40c.substring(7);
curam.util.Dialog._id=_40d;
_3e9.log("curam.util.Dialog.closeAndSubmitParent() "+_3e9.getProperty("curam.dialog.modal.id")+_40d);
}
if(_40a==null||_40a==undefined){
_40b.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
return;
}
var _40e=function(_40f){
for(var _410 in _40f){
if(_40f.hasOwnProperty(_410)){
return false;
}
}
return true;
};
if(_408&&!_40e(_408)){
var _411=dojo.query("#"+_40a.id+" > "+"input[type=text]");
var _412=dojo.filter(_411,function(node){
return node.readOnly==false;
});
dojo.forEach(_412,function(node){
node.value="";
});
for(var _413 in _408){
var _414=_412[parseInt(_413)];
if(_414){
var _415=dojo.query("input[name="+_414.id+"]",_40a)[0];
if(_415){
_415.value=_408[_413];
}else{
_414.value=_408[_413];
}
}
}
}else{
}
_409.dojo.publish("/curam/page/refresh");
_40a.submit();
_40b.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},fileDownloadAnchorHandler:function(url){
return curam.util.fileDownloadAnchorHandler(url);
}});
});
},"dojo/hccss":function(){
define(["require","./_base/config","./dom-class","./dom-style","./has","./domReady","./_base/window"],function(_416,_417,_418,_419,has,_41a,win){
has.add("highcontrast",function(){
var div=win.doc.createElement("div");
div.style.cssText="border: 1px solid; border-color:red green; position: absolute; height: 5px; top: -999px;"+"background-image: url(\""+(_417.blankGif||_416.toUrl("./resources/blank.gif"))+"\");";
win.body().appendChild(div);
var cs=_419.getComputedStyle(div),_41b=cs.backgroundImage,hc=(cs.borderTopColor==cs.borderRightColor)||(_41b&&(_41b=="none"||_41b=="url(invalid-url:)"));
if(has("ie")<=8){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
return hc;
});
_41a(function(){
if(has("highcontrast")){
_418.add(win.body(),"dj_a11y");
}
});
return has;
});
},"dijit/_BidiMixin":function(){
define([],function(){
var _41c={LRM:"‎",LRE:"‪",PDF:"‬",RLM:"‏",RLE:"‫"};
return {textDir:"",getTextDir:function(text){
return this.textDir=="auto"?this._checkContextual(text):this.textDir;
},_checkContextual:function(text){
var fdc=/[A-Za-z\u05d0-\u065f\u066a-\u06ef\u06fa-\u07ff\ufb1d-\ufdff\ufe70-\ufefc]/.exec(text);
return fdc?(fdc[0]<="z"?"ltr":"rtl"):this.dir?this.dir:this.isLeftToRight()?"ltr":"rtl";
},applyTextDir:function(_41d,text){
if(this.textDir){
var _41e=this.textDir;
if(_41e=="auto"){
if(typeof text==="undefined"){
var _41f=_41d.tagName.toLowerCase();
text=(_41f=="input"||_41f=="textarea")?_41d.value:_41d.innerText||_41d.textContent||"";
}
_41e=this._checkContextual(text);
}
if(_41d.dir!=_41e){
_41d.dir=_41e;
}
}
},enforceTextDirWithUcc:function(_420,text){
if(this.textDir){
if(_420){
_420.originalText=text;
}
var dir=this.textDir=="auto"?this._checkContextual(text):this.textDir;
return (dir=="ltr"?_41c.LRE:_41c.RLE)+text+_41c.PDF;
}
return text;
},restoreOriginalText:function(_421){
if(_421.originalText){
_421.text=_421.originalText;
delete _421.originalText;
}
return _421;
},_setTextDirAttr:function(_422){
if(!this._created||this.textDir!=_422){
this._set("textDir",_422);
var node=null;
if(this.displayNode){
node=this.displayNode;
this.displayNode.align=this.dir=="rtl"?"right":"left";
}else{
node=this.textDirNode||this.focusNode||this.textbox;
}
if(node){
this.applyTextDir(node);
}
}
}};
});
},"curam/widget/_ComboBoxMenu":function(){
define(["dojo/_base/declare","dojo/dom-attr","curam/util/ResourceBundle","dijit/form/_ComboBoxMenu"],function(_423,_424,_425){
var _426=new _425("FilteringSelect");
var _427=_423("curam.widget._ComboBoxMenu",[dijit.form._ComboBoxMenu],{_createOption:function(item,_428){
var _429=this._createMenuItem();
var _42a=_428(item);
if(_42a.html){
_429.innerHTML=_42a.label;
}else{
_429.appendChild(_429.ownerDocument.createTextNode(_42a.label));
}
if(_429.innerHTML==""){
_424.set(_429,"aria-label",_426.getProperty("curam.select.option.blank"));
_424.set(_429,"title",_426.getProperty("curam.select.option.blank"));
}else{
var _42b=/<[a-zA-Z\/][^>]*>/g;
var _42c=_429.innerHTML;
_429.title=_42c.replace(_42b,"");
}
return _429;
}});
return _427;
});
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"curam/define":function(){
define(["dojo/_base/lang"],function(lang){
var _42d=this;
if(typeof (_42d.curam)=="undefined"){
_42d.curam={};
}
if(typeof (_42d.curam.define)=="undefined"){
lang.mixin(_42d.curam,{define:{}});
}
lang.mixin(_42d.curam.define,{singleton:function(_42e,_42f){
var _430=_42e.split(".");
var _431=window;
for(var i=0;i<_430.length;i++){
var part=_430[i];
if(typeof _431[part]=="undefined"){
_431[part]={};
}
_431=_431[part];
}
if(_42f){
lang.mixin(_431,_42f);
}
}});
return _42d.curam.define;
});
},"curam/util/external":function(){
define(["curam/util"],function(util){
curam.define.singleton("curam.util.external",{inExternalApp:function(){
return jsScreenContext.hasContextBits("EXTAPP");
},getUimParentWindow:function(){
if(util.getTopmostWindow()===dojo.global){
return null;
}else{
return dojo.global;
}
}});
return curam.util.external;
});
},"dijit/_OnDijitClickMixin":function(){
define(["dojo/on","dojo/_base/array","dojo/keys","dojo/_base/declare","dojo/has","./a11yclick"],function(on,_432,keys,_433,has,_434){
var ret=_433("dijit._OnDijitClickMixin",null,{connect:function(obj,_435,_436){
return this.inherited(arguments,[obj,_435=="ondijitclick"?_434:_435,_436]);
}});
ret.a11yclick=_434;
return ret;
});
},"curam/layout/ScrollingTabController":function(){
define(["dojo/_base/declare","dojo/dom-class","dijit/layout/ScrollingTabController","curam/inspection/Layer","curam/debug","curam/widget/_HasDropDown","dojo/text!curam/layout/resources/ScrollingTabController.html"],function(_437,_438,_439,_43a,_43b,_43c,_43d){
var _43e=_437("curam.layout.ScrollingTabController",_439,{templateString:_43d,onStartup:function(){
this.inherited(arguments);
_43a.register("curam/layout/ScrollingTabController",this);
},updateTabStyle:function(){
var kids=this.getChildren();
curam.debug.log("curam.layout.ScrollingTabController.updateTabStyle kids = ",this.domNode);
dojo.forEach(kids,function(_43f,_440,_441){
_438.remove(_43f.domNode,["first-class","last-class"]);
if(_440==0){
_438.add(_43f.domNode,"first");
}else{
if(_440==_441.length-1){
_438.add(_43f.domNode,"last");
}
}
});
var _442=dojo.query(".nowrapTabStrip",this.domNode)[0];
_438.replace(_442,"nowrapSecTabStrip","nowrapTabStrip");
var _443=document.createElement("div");
_438.add(_443,"block-slope");
_438.add(_443,"dijitTab");
_443.innerHTML="&#x200B;";
_442.appendChild(_443);
}});
_437("curam.layout._ScrollingTabControllerMenuButton",[dijit.layout._ScrollingTabControllerMenuButton,_43c],{closeDropDown:function(_444){
this.inherited(arguments);
if(this.dropDown){
this._popupStateNode.removeAttribute("aria-owns");
this.dropDown.destroyRecursive();
delete this.dropDown;
}
}});
return _43e;
});
},"dijit/_BidiSupport":function(){
define(["dojo/has","./_WidgetBase","./_BidiMixin"],function(has,_445,_446){
_445.extend(_446);
has.add("dojo-bidi",true);
return _445;
});
},"dijit/form/_ListMouseMixin":function(){
define(["dojo/_base/declare","dojo/on","dojo/touch","./_ListBase"],function(_447,on,_448,_449){
return _447("dijit.form._ListMouseMixin",_449,{postCreate:function(){
this.inherited(arguments);
this.domNode.dojoClick=true;
this._listConnect("click","_onClick");
this._listConnect("mousedown","_onMouseDown");
this._listConnect("mouseup","_onMouseUp");
this._listConnect("mouseover","_onMouseOver");
this._listConnect("mouseout","_onMouseOut");
},_onClick:function(evt,_44a){
this._setSelectedAttr(_44a,false);
if(this._deferredClick){
this._deferredClick.remove();
}
this._deferredClick=this.defer(function(){
this._deferredClick=null;
this.onClick(_44a);
});
},_onMouseDown:function(evt,_44b){
if(this._hoveredNode){
this.onUnhover(this._hoveredNode);
this._hoveredNode=null;
}
this._isDragging=true;
this._setSelectedAttr(_44b,false);
},_onMouseUp:function(evt,_44c){
this._isDragging=false;
var _44d=this.selected;
var _44e=this._hoveredNode;
if(_44d&&_44c==_44d){
this.defer(function(){
this._onClick(evt,_44d);
});
}else{
if(_44e){
this.defer(function(){
this._onClick(evt,_44e);
});
}
}
},_onMouseOut:function(evt,_44f){
if(this._hoveredNode){
this.onUnhover(this._hoveredNode);
this._hoveredNode=null;
}
if(this._isDragging){
this._cancelDrag=(new Date()).getTime()+1000;
}
},_onMouseOver:function(evt,_450){
if(this._cancelDrag){
var time=(new Date()).getTime();
if(time>this._cancelDrag){
this._isDragging=false;
}
this._cancelDrag=null;
}
this._hoveredNode=_450;
this.onHover(_450);
if(this._isDragging){
this._setSelectedAttr(_450,false);
}
}});
});
},"dojo/cookie":function(){
define(["./_base/kernel","./regexp"],function(dojo,_451){
dojo.cookie=function(name,_452,_453){
var c=document.cookie,ret;
if(arguments.length==1){
var _454=c.match(new RegExp("(?:^|; )"+_451.escapeString(name)+"=([^;]*)"));
ret=_454?decodeURIComponent(_454[1]):undefined;
}else{
_453=_453||{};
var exp=_453.expires;
if(typeof exp=="number"){
var d=new Date();
d.setTime(d.getTime()+exp*24*60*60*1000);
exp=_453.expires=d;
}
if(exp&&exp.toUTCString){
_453.expires=exp.toUTCString();
}
_452=encodeURIComponent(_452);
var _455=name+"="+_452,_456;
for(_456 in _453){
_455+="; "+_456;
var _457=_453[_456];
if(_457!==true){
_455+="="+_457;
}
}
document.cookie=_455;
}
return ret;
};
dojo.cookie.isSupported=function(){
if(!("cookieEnabled" in navigator)){
this("__djCookieTest__","CookiesAllowed");
navigator.cookieEnabled=this("__djCookieTest__")=="CookiesAllowed";
if(navigator.cookieEnabled){
this("__djCookieTest__","",{expires:-1});
}
}
return navigator.cookieEnabled;
};
return dojo.cookie;
});
},"idx/has":function(){
define(["dojo/_base/lang","dojo/has"],function(_458,dHas){
var iHas={};
_458.mixin(iHas,dHas);
iHas.normalize=function(id,_459){
var _45a=id.match(/[\?:]|[^:\?]*/g),i=0,get=function(skip){
var term=_45a[i++];
if(term==":"){
return 0;
}else{
if(_45a[i++]=="?"){
if(term.length>1&&term.charAt(0)=="#"){
term=term.substring(1);
}
if(!skip&&dHas(term)){
return get();
}else{
get(true);
return get(skip);
}
}
return term||0;
}
};
id=get();
return id&&_459(id);
};
return iHas;
});
},"dojo/cache":function(){
define(["./_base/kernel","./text"],function(dojo){
return dojo.cache;
});
},"curam/util/ui/refresh/TabRefreshController":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/debug","curam/util/ui/refresh/RefreshEvent"],function(_45b,_45c,_45d){
var _45e=_45b("curam.util.ui.refresh.TabRefreshController",null,{EVENT_REFRESH_MENU:"/curam/refresh/menu",EVENT_REFRESH_NAVIGATION:"/curam/refresh/navigation",EVENT_REFRESH_CONTEXT:"/curam/refresh/context",EVENT_REFRESH_MAIN:"/curam/refresh/main-content",_tabWidgetId:null,_configOnSubmit:null,_configOnLoad:null,_handler:null,_lastSubmitted:null,_currentlyRefreshing:null,_ignoreContextRefresh:true,_initialMenuAndNavRefreshDone:false,_nullController:null,constructor:function(_45f,_460){
this._configOnSubmit={};
this._configOnLoad={};
if(!_460){
this._nullController=true;
return;
}
this._tabWidgetId=_45f;
dojo.forEach(_460.config,dojo.hitch(this,function(item){
this._configOnSubmit[item.page]=item.onsubmit;
this._configOnLoad[item.page]=item.onload;
}));
_45c.register("curam/util/ui/refresh/TabRefreshController",this);
},pageSubmitted:function(_461,_462){
new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONSUBMIT,_462);
_45d.log("curam.util.ui.refresh.TabRefreshController: "+_45d.getProperty("curam.util.ui.refresh.TabRefreshController.submit",[_461,_462]));
dojo.publish("curam/form/submit",[_461]);
if(this._configOnSubmit[_461]){
this._lastSubmitted=_461;
_45d.log("curam.util.ui.refresh.TabRefreshController: "+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+"submit.notify"));
}
},pageLoaded:function(_463,_464){
var _465=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,_464);
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController.load",[_463,_464]));
if(this._currentlyRefreshing&&this._currentlyRefreshing.equals(_465)){
this._currentlyRefreshing=null;
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+"refresh"));
return;
}
var _466={};
if(_464==_465.SOURCE_CONTEXT_MAIN&&this._configOnLoad[_463]){
_466=this._configOnLoad[_463];
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".load.config"));
}
if(this._lastSubmitted){
var cfg=this._configOnSubmit[this._lastSubmitted];
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".submit.config",[this._lastSubmitted]));
_466.details=_466.details||cfg.details;
_466.menubar=_466.menubar||cfg.menubar;
_466.navigation=_466.navigation||cfg.navigation;
_466.mainContent=_466.mainContent||cfg.mainContent;
this._lastSubmitted=null;
}
if(!this._nullController){
this._fireRefreshEvents(_466,this._ignoreContextRefresh,!this._initialMenuAndNavRefreshDone);
}
if(this._ignoreContextRefresh&&_464==_465.SOURCE_CONTEXT_MAIN){
this._ignoreContextRefresh=false;
}
if(!this._initialMenuAndNavRefreshDone){
this._initialMenuAndNavRefreshDone=true;
}
},_fireRefreshEvents:function(cfg,_467,_468){
var _469=[];
if(cfg.details){
if(_467){
curam.debug.log("curam.util.ui.refresh.TabRefreshController: ignoring the first CONTEXT refresh request");
}else{
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.context"));
_469.push(this.EVENT_REFRESH_CONTEXT+"/"+this._tabWidgetId);
}
}else{
if(!_467){
dojo.publish("curam/tab/contextRefresh");
}
}
if(cfg.menubar||_468){
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.menu"));
_469.push(this.EVENT_REFRESH_MENU+"/"+this._tabWidgetId);
}
if(cfg.navigation||_468){
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.nav"));
_469.push(this.EVENT_REFRESH_NAVIGATION+"/"+this._tabWidgetId);
}
if(cfg.mainContent){
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.main"));
this._currentlyRefreshing=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN,null);
_469.push(this.EVENT_REFRESH_MAIN+"/"+this._tabWidgetId);
}
if(_469.length>0){
_45d.log("curam.util.ui.refresh.TabRefreshController:"+_45d.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.log",[_469.length,_469]));
this._handler(_469);
}
},setRefreshHandler:function(_46a){
this._handler=_46a;
},destroy:function(){
for(prop in this._configOnSubmit){
if(this._configOnSubmit.hasOwnProperty(prop)){
delete this._configOnSubmit[prop];
}
}
for(prop in this._configOnLoad){
if(this._configOnLoad.hasOwnProperty(prop)){
delete this._configOnLoad[prop];
}
}
this._configOnSubmit={};
this._configOnLoad={};
this._handler=null;
this._lastSubmitted=null;
this._currentlyRefreshing=null;
}});
return _45e;
});
},"dijit/_base/popup":function(){
define(["dojo/dom-class","dojo/_base/window","../popup","../BackgroundIframe"],function(_46b,win,_46c){
var _46d=_46c._createWrapper;
_46c._createWrapper=function(_46e){
if(!_46e.declaredClass){
_46e={_popupWrapper:(_46e.parentNode&&_46b.contains(_46e.parentNode,"dijitPopup"))?_46e.parentNode:null,domNode:_46e,destroy:function(){
},ownerDocument:_46e.ownerDocument,ownerDocumentBody:win.body(_46e.ownerDocument)};
}
return _46d.call(this,_46e);
};
var _46f=_46c.open;
_46c.open=function(args){
if(args.orient&&typeof args.orient!="string"&&!("length" in args.orient)){
var ary=[];
for(var key in args.orient){
ary.push({aroundCorner:key,corner:args.orient[key]});
}
args.orient=ary;
}
return _46f.call(this,args);
};
return _46c;
});
},"dojo/promise/all":function(){
define(["../_base/array","../Deferred","../when"],function(_470,_471,when){
"use strict";
var some=_470.some;
return function all(_472){
var _473,_470;
if(_472 instanceof Array){
_470=_472;
}else{
if(_472&&typeof _472==="object"){
_473=_472;
}
}
var _474;
var _475=[];
if(_473){
_470=[];
for(var key in _473){
if(Object.hasOwnProperty.call(_473,key)){
_475.push(key);
_470.push(_473[key]);
}
}
_474={};
}else{
if(_470){
_474=[];
}
}
if(!_470||!_470.length){
return new _471().resolve(_474);
}
var _476=new _471();
_476.promise.always(function(){
_474=_475=null;
});
var _477=_470.length;
some(_470,function(_478,_479){
if(!_473){
_475.push(_479);
}
when(_478,function(_47a){
if(!_476.isFulfilled()){
_474[_475[_479]]=_47a;
if(--_477===0){
_476.resolve(_474);
}
}
},_476.reject);
return _476.isFulfilled();
});
return _476.promise;
};
});
},"curam/widget/_TabButton":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-style","dojo/has","dojo/i18n","dojo/_base/lang","dojo/text!curam/widget/templates/_TabButton.html","dijit/registry","dojo/_base/connect","curam/inspection/Layer","dijit/layout/StackController","dijit/Menu","dijit/MenuItem","curam/widget/MenuItem","curam/tab"],function(_47b,dom,_47c,_47d,_47e,has,i18n,lang,_47f,_480,_481,_482){
_481.subscribe("/curam/tab/labelUpdated",function(){
var tabs;
var _483=dojo.query(".dijitTabContainerTop-tabs");
_483.forEach(function(_484){
tabs=dojo.query(".tabLabel",_484);
tabs.forEach(function(tab,_485){
var _486=tabs[_485].innerHTML;
tab.setAttribute("title",_486);
var _487=tab.nextSibling;
while(_487){
if(_47d.contains(_487,"dijitTabCloseButton")){
var _488=_487.getAttribute("title");
if(_488&&_488.indexOf(_486)<0){
_487.setAttribute("title",_488+" - "+_486);
}
break;
}
_487=_487.nextSibling;
}
});
});
});
var _489=_47b("curam.widget._TabButton",dijit.layout._StackButton,{templateString:_47f,_setNameAttr:"focusNode",scrollOnFocus:false,curamDisabled:false,curamVisible:true,baseClass:"dijitTab",postMixInProperties:function(){
if(!this.iconClass){
this.iconClass="dijitTabButtonIcon";
}
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.containerNode,false);
if(this.iconNode.className=="dijitTabButtonIcon"){
_47e.set(this.iconNode,"width","1px");
}
_47c.set(this.focusNode,"id",this.id);
_482.register("curam/widget/_TabButton",this);
},startup:function(){
dijit.layout._StackButton.prototype.startup.apply(this,arguments);
},_setCloseButtonAttr:function(disp){
this._set("closeButton",disp);
_47d.toggle(this.domNode,"dijitClosable",disp);
this.closeNode.style.display=disp?"":"none";
if(disp){
var _48a=i18n.getLocalization("dijit","common");
if(this.closeNode){
_47c.set(this.closeNode,"title",_48a.itemClose);
}
}else{
_47d.add(this.titleNode,"hasNoCloseButton");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
}
},_setCuramDisabledAttr:function(_48b){
this.curamDisabled=_48b;
this._swapState(this.domNode,this.curamDisabled,"disabled","enabled");
},_setCuramVisibleAttr:function(_48c){
this.curamVisible=_48c;
this._swapState(this.domNode,this.curamVisible,"visible","hidden");
},_swapState:function(node,_48d,_48e,_48f){
if(_48d){
_47d.replace(node,_48e,_48f);
}else{
_47d.replace(node,_48f,_48e);
}
},destroy:function(){
_481.publish("/curam/tab/labelUpdated");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
this.inherited(arguments);
}});
return _489;
});
},"dojo/_base/url":function(){
define(["./kernel"],function(dojo){
var ore=new RegExp("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$"),ire=new RegExp("^((([^\\[:]+):)?([^@]+)@)?(\\[([^\\]]+)\\]|([^\\[:]*))(:([0-9]+))?$"),_490=function(){
var n=null,_491=arguments,uri=[_491[0]];
for(var i=1;i<_491.length;i++){
if(!_491[i]){
continue;
}
var _492=new _490(_491[i]+""),_493=new _490(uri[0]+"");
if(_492.path==""&&!_492.scheme&&!_492.authority&&!_492.query){
if(_492.fragment!=n){
_493.fragment=_492.fragment;
}
_492=_493;
}else{
if(!_492.scheme){
_492.scheme=_493.scheme;
if(!_492.authority){
_492.authority=_493.authority;
if(_492.path.charAt(0)!="/"){
var path=_493.path.substring(0,_493.path.lastIndexOf("/")+1)+_492.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==(segs.length-1)){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_492.path=segs.join("/");
}
}
}
}
uri=[];
if(_492.scheme){
uri.push(_492.scheme,":");
}
if(_492.authority){
uri.push("//",_492.authority);
}
uri.push(_492.path);
if(_492.query){
uri.push("?",_492.query);
}
if(_492.fragment){
uri.push("#",_492.fragment);
}
}
this.uri=uri.join("");
var r=this.uri.match(ore);
this.scheme=r[2]||(r[1]?"":n);
this.authority=r[4]||(r[3]?"":n);
this.path=r[5];
this.query=r[7]||(r[6]?"":n);
this.fragment=r[9]||(r[8]?"":n);
if(this.authority!=n){
r=this.authority.match(ire);
this.user=r[3]||n;
this.password=r[4]||n;
this.host=r[6]||r[7];
this.port=r[9]||n;
}
};
_490.prototype.toString=function(){
return this.uri;
};
return dojo._Url=_490;
});
},"curam/widget/FilteringSelect":function(){
define(["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/dom-construct","dojo/keys","dojo/sniff","dijit/form/FilteringSelect","curam/widget/ComboBoxMixin"],function(_494,_495,on,dom,_496,keys,has){
var _497=_495("curam.widget.FilteringSelect",[dijit.form.FilteringSelect,curam.widget.ComboBoxMixin],{enterKeyOnOpenDropDown:false,required:false,postMixInProperties:function(){
if(!this.store){
if(dojo.query("> option",this.srcNodeRef)[0]==undefined){
_496.create("option",{innerHTML:"<!--__o3_BLANK-->"},this.srcNodeRef);
}
}
if(!this.get("store")&&this.srcNodeRef.value==""){
var _498=this.srcNodeRef,_499=dojo.query("> option[value='']",_498);
if(_499.length&&_499[0].innerHTML!="<!--__o3_BLANK-->"){
this.displayedValue=dojo.trim(_499[0].innerHTML);
}
}
this.inherited(arguments);
},postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _49a=_494.byNode(dom.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_49a._opened){
_49a.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
},startup:function(){
if(has("trident")){
this.domNode.setAttribute("role","listbox");
}
this.inherited(arguments);
},_callbackSetLabel:function(_49b,_49c,_49d,_49e){
if((_49c&&_49c[this.searchAttr]!==this._lastQuery)||(!_49c&&_49b.length&&this.get("store").getIdentity(_49b[0])!=this._lastQuery)){
return;
}
if(!_49b.length){
this.set("value","__o3_INVALID",_49e||(_49e===undefined&&!this.focused),this.textbox.value,null);
}else{
this.set("item",_49b[0],_49e);
}
},_onKey:function(evt){
if(evt.charCode>=32){
return;
}
var key=evt.charCode||evt.keyCode;
if(key==keys.ALT||key==keys.CTRL||key==keys.META||key==keys.SHIFT){
return;
}
var pw=this.dropDown;
var _49f=null;
this._abortQuery();
this.inherited(arguments);
if(evt.altKey||evt.ctrlKey||evt.metaKey){
return;
}
if(this._opened){
_49f=pw.getHighlightedOption();
}
switch(key){
case keys.PAGE_DOWN:
case keys.DOWN_ARROW:
case keys.PAGE_UP:
case keys.UP_ARROW:
if(this._opened){
this._announceOption(_49f);
}
evt.stopPropagation();
evt.preventDefault();
break;
case keys.ENTER:
if(_49f){
if(_49f==pw.nextButton){
this._nextSearch(1);
evt.stopPropagation();
evt.preventDefault();
break;
}else{
if(_49f==pw.previousButton){
this._nextSearch(-1);
evt.stopPropagation();
evt.preventDefault();
break;
}
}
evt.stopPropagation();
evt.preventDefault();
}else{
this._setBlurValue();
this._setCaretPos(this.focusNode,0);
var _4a0=this._resetValue;
var _4a1=this.displayedValue;
if(_4a0!=_4a1){
evt.stopPropagation();
evt.preventDefault();
}
}
case keys.TAB:
var _4a1=this.get("displayedValue");
if(pw&&(_4a1==pw._messages["previousMessage"]||_4a1==pw._messages["nextMessage"])){
break;
}
if(_49f){
this._selectOption(_49f);
}
case keys.ESCAPE:
if(this._opened){
this._lastQuery=null;
this.closeDropDown();
}
break;
}
},_selectOption:function(_4a2){
this.closeDropDown();
if(_4a2){
this._announceOption(_4a2);
}
this._setCaretPos(this.focusNode,0);
this._handleOnChange(this.value,true);
this.focusNode.removeAttribute("aria-activedescendant");
}});
return _497;
});
},"dojo/text":function(){
define(["./_base/kernel","require","./has","./request"],function(dojo,_4a3,has,_4a4){
var _4a5;
if(1){
_4a5=function(url,sync,load){
_4a4(url,{sync:!!sync,headers:{"X-Requested-With":null}}).then(load);
};
}else{
if(_4a3.getText){
_4a5=_4a3.getText;
}else{
console.error("dojo/text plugin failed to load because loader does not support getText");
}
}
var _4a6={},_4a7=function(text){
if(text){
text=text.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _4a8=text.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_4a8){
text=_4a8[1];
}
}else{
text="";
}
return text;
},_4a9={},_4aa={};
dojo.cache=function(_4ab,url,_4ac){
var key;
if(typeof _4ab=="string"){
if(/\//.test(_4ab)){
key=_4ab;
_4ac=url;
}else{
key=_4a3.toUrl(_4ab.replace(/\./g,"/")+(url?("/"+url):""));
}
}else{
key=_4ab+"";
_4ac=url;
}
var val=(_4ac!=undefined&&typeof _4ac!="string")?_4ac.value:_4ac,_4ad=_4ac&&_4ac.sanitize;
if(typeof val=="string"){
_4a6[key]=val;
return _4ad?_4a7(val):val;
}else{
if(val===null){
delete _4a6[key];
return null;
}else{
if(!(key in _4a6)){
_4a5(key,true,function(text){
_4a6[key]=text;
});
}
return _4ad?_4a7(_4a6[key]):_4a6[key];
}
}
};
return {dynamic:true,normalize:function(id,_4ae){
var _4af=id.split("!"),url=_4af[0];
return (/^\./.test(url)?_4ae(url):url)+(_4af[1]?"!"+_4af[1]:"");
},load:function(id,_4b0,load){
var _4b1=id.split("!"),_4b2=_4b1.length>1,_4b3=_4b1[0],url=_4b0.toUrl(_4b1[0]),_4b4="url:"+url,text=_4a9,_4b5=function(text){
load(_4b2?_4a7(text):text);
};
if(_4b3 in _4a6){
text=_4a6[_4b3];
}else{
if(_4b0.cache&&_4b4 in _4b0.cache){
text=_4b0.cache[_4b4];
}else{
if(url in _4a6){
text=_4a6[url];
}
}
}
if(text===_4a9){
if(_4aa[url]){
_4aa[url].push(_4b5);
}else{
var _4b6=_4aa[url]=[_4b5];
_4a5(url,!_4b0.async,function(text){
_4a6[_4b3]=_4a6[url]=text;
for(var i=0;i<_4b6.length;){
_4b6[i++](text);
}
delete _4aa[url];
});
}
}else{
_4b5(text);
}
}};
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./domReady","./sniff","./_base/window"],function(_4b7,lang,_4b8,has,_4b9){
var html=_4b9.doc.documentElement,ie=has("ie"),_4ba=has("trident"),_4bb=has("opera"),maj=Math.floor,ff=has("ff"),_4bc=_4b7.boxModel.replace(/-/,""),_4bd={"dj_quirks":has("quirks"),"dj_opera":_4bb,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_edge":has("edge"),"dj_gecko":has("mozilla"),"dj_ios":has("ios"),"dj_android":has("android")};
if(ie){
_4bd["dj_ie"]=true;
_4bd["dj_ie"+maj(ie)]=true;
_4bd["dj_iequirks"]=has("quirks");
}
if(_4ba){
_4bd["dj_trident"]=true;
_4bd["dj_trident"+maj(_4ba)]=true;
}
if(ff){
_4bd["dj_ff"+maj(ff)]=true;
}
_4bd["dj_"+_4bc]=true;
var _4be="";
for(var clz in _4bd){
if(_4bd[clz]){
_4be+=clz+" ";
}
}
html.className=lang.trim(html.className+" "+_4be);
_4b8(function(){
if(!_4b7.isBodyLtr()){
var _4bf="dj_rtl dijitRtl "+_4be.replace(/ /g,"-rtl ");
html.className=lang.trim(html.className+" "+_4bf+"dj_rtl dijitRtl "+_4be.replace(/ /g,"-rtl "));
}
});
return has;
});
},"dijit/Tooltip":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/fx","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","./_base/manager","./place","./_Widget","./_TemplatedMixin","./BackgroundIframe","dojo/text!./templates/Tooltip.html","./main"],function(_4c0,_4c1,fx,dom,_4c2,_4c3,_4c4,lang,_4c5,on,has,_4c6,_4c7,_4c8,_4c9,_4ca,_4cb,_4cc){
var _4cd=_4c1("dijit._MasterTooltip",[_4c8,_4c9],{duration:_4c6.defaultDuration,templateString:_4cb,postCreate:function(){
this.ownerDocumentBody.appendChild(this.domNode);
this.bgIframe=new _4ca(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
},show:function(_4ce,_4cf,_4d0,rtl,_4d1,_4d2,_4d3){
if(this.aroundNode&&this.aroundNode===_4cf&&this.containerNode.innerHTML==_4ce){
return;
}
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.containerNode.innerHTML=_4ce;
if(_4d1){
this.set("textDir",_4d1);
}
this.containerNode.align=rtl?"right":"left";
var pos=_4c7.around(this.domNode,_4cf,_4d0&&_4d0.length?_4d0:_4d4.defaultPosition,!rtl,lang.hitch(this,"orient"));
var _4d5=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_4d5.y+((_4d5.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_4d5.x+((_4d5.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}else{
this.connectorNode.style.left="";
this.connectorNode.style.top="";
}
}
_4c4.set(this.domNode,"opacity",0);
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_4cf;
this.onMouseEnter=_4d2||noop;
this.onMouseLeave=_4d3||noop;
},orient:function(node,_4d6,_4d7,_4d8,_4d9){
this.connectorNode.style.top="";
var _4da=_4d8.h,_4db=_4d8.w;
node.className="dijitTooltip "+{"MR-ML":"dijitTooltipRight","ML-MR":"dijitTooltipLeft","TM-BM":"dijitTooltipAbove","BM-TM":"dijitTooltipBelow","BL-TL":"dijitTooltipBelow dijitTooltipABLeft","TL-BL":"dijitTooltipAbove dijitTooltipABLeft","BR-TR":"dijitTooltipBelow dijitTooltipABRight","TR-BR":"dijitTooltipAbove dijitTooltipABRight","BR-BL":"dijitTooltipRight","BL-BR":"dijitTooltipLeft"}[_4d6+"-"+_4d7];
this.domNode.style.width="auto";
var size=_4c3.position(this.domNode);
if(has("ie")||has("trident")){
size.w+=2;
}
var _4dc=Math.min((Math.max(_4db,1)),size.w);
_4c3.setMarginBox(this.domNode,{w:_4dc});
if(_4d7.charAt(0)=="B"&&_4d6.charAt(0)=="B"){
var bb=_4c3.position(node);
var _4dd=this.connectorNode.offsetHeight;
if(bb.h>_4da){
var _4de=_4da-((_4d9.h+_4dd)>>1);
this.connectorNode.style.top=_4de+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(_4d9.h/2-_4dd/2,0),bb.h-_4dd)+"px";
this.connectorNode.style.top="";
}
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
return Math.max(0,size.w-_4db);
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
},hide:function(_4df){
if(this._onDeck&&this._onDeck[1]==_4df){
this._onDeck=null;
}else{
if(this.aroundNode===_4df){
this.fadeIn.stop();
this.isShowingNow=false;
this.aroundNode=null;
this.fadeOut.play();
}else{
}
}
this.onMouseEnter=this.onMouseLeave=noop;
},_onHide:function(){
this.domNode.style.cssText="";
this.containerNode.innerHTML="";
if(this._onDeck){
this.show.apply(this,this._onDeck);
this._onDeck=null;
}
}});
if(has("dojo-bidi")){
_4cd.extend({_setAutoTextDir:function(node){
this.applyTextDir(node);
_4c0.forEach(node.children,function(_4e0){
this._setAutoTextDir(_4e0);
},this);
},_setTextDirAttr:function(_4e1){
this._set("textDir",_4e1);
if(_4e1=="auto"){
this._setAutoTextDir(this.containerNode);
}else{
this.containerNode.dir=this.textDir;
}
}});
}
_4cc.showTooltip=function(_4e2,_4e3,_4e4,rtl,_4e5,_4e6,_4e7){
if(_4e4){
_4e4=_4c0.map(_4e4,function(val){
return {after:"after-centered",before:"before-centered"}[val]||val;
});
}
if(!_4d4._masterTT){
_4cc._masterTT=_4d4._masterTT=new _4cd();
}
return _4d4._masterTT.show(_4e2,_4e3,_4e4,rtl,_4e5,_4e6,_4e7);
};
_4cc.hideTooltip=function(_4e8){
return _4d4._masterTT&&_4d4._masterTT.hide(_4e8);
};
var _4e9="DORMANT",_4ea="SHOW TIMER",_4eb="SHOWING",_4ec="HIDE TIMER";
function noop(){
};
var _4d4=_4c1("dijit.Tooltip",_4c8,{label:"",showDelay:400,hideDelay:400,connectId:[],position:[],selector:"",_setConnectIdAttr:function(_4ed){
_4c0.forEach(this._connections||[],function(_4ee){
_4c0.forEach(_4ee,function(_4ef){
_4ef.remove();
});
},this);
this._connectIds=_4c0.filter(lang.isArrayLike(_4ed)?_4ed:(_4ed?[_4ed]:[]),function(id){
return dom.byId(id,this.ownerDocument);
},this);
this._connections=_4c0.map(this._connectIds,function(id){
var node=dom.byId(id,this.ownerDocument),_4f0=this.selector,_4f1=_4f0?function(_4f2){
return on.selector(_4f0,_4f2);
}:function(_4f3){
return _4f3;
},self=this;
return [on(node,_4f1(_4c5.enter),function(){
self._onHover(this);
}),on(node,_4f1("focusin"),function(){
self._onHover(this);
}),on(node,_4f1(_4c5.leave),lang.hitch(self,"_onUnHover")),on(node,_4f1("focusout"),lang.hitch(self,"set","state",_4e9))];
},this);
this._set("connectId",_4ed);
},addTarget:function(node){
var id=node.id||node;
if(_4c0.indexOf(this._connectIds,id)==-1){
this.set("connectId",this._connectIds.concat(id));
}
},removeTarget:function(node){
var id=node.id||node,idx=_4c0.indexOf(this._connectIds,id);
if(idx>=0){
this._connectIds.splice(idx,1);
this.set("connectId",this._connectIds);
}
},buildRendering:function(){
this.inherited(arguments);
_4c2.add(this.domNode,"dijitTooltipData");
},startup:function(){
this.inherited(arguments);
var ids=this.connectId;
_4c0.forEach(lang.isArrayLike(ids)?ids:[ids],this.addTarget,this);
},getContent:function(node){
return this.label||this.domNode.innerHTML;
},state:_4e9,_setStateAttr:function(val){
if(this.state==val||(val==_4ea&&this.state==_4eb)||(val==_4ec&&this.state==_4e9)){
return;
}
if(this._hideTimer){
this._hideTimer.remove();
delete this._hideTimer;
}
if(this._showTimer){
this._showTimer.remove();
delete this._showTimer;
}
switch(val){
case _4e9:
if(this._connectNode){
_4d4.hide(this._connectNode);
delete this._connectNode;
this.onHide();
}
break;
case _4ea:
if(this.state!=_4eb){
this._showTimer=this.defer(function(){
this.set("state",_4eb);
},this.showDelay);
}
break;
case _4eb:
var _4f4=this.getContent(this._connectNode);
if(!_4f4){
this.set("state",_4e9);
return;
}
_4d4.show(_4f4,this._connectNode,this.position,!this.isLeftToRight(),this.textDir,lang.hitch(this,"set","state",_4eb),lang.hitch(this,"set","state",_4ec));
this.onShow(this._connectNode,this.position);
break;
case _4ec:
this._hideTimer=this.defer(function(){
this.set("state",_4e9);
},this.hideDelay);
break;
}
this._set("state",val);
},_onHover:function(_4f5){
if(this._connectNode&&_4f5!=this._connectNode){
this.set("state",_4e9);
}
this._connectNode=_4f5;
this.set("state",_4ea);
},_onUnHover:function(_4f6){
this.set("state",_4ec);
},open:function(_4f7){
this.set("state",_4e9);
this._connectNode=_4f7;
this.set("state",_4eb);
},close:function(){
this.set("state",_4e9);
},onShow:function(){
},onHide:function(){
},destroy:function(){
this.set("state",_4e9);
_4c0.forEach(this._connections||[],function(_4f8){
_4c0.forEach(_4f8,function(_4f9){
_4f9.remove();
});
},this);
this.inherited(arguments);
}});
_4d4._MasterTooltip=_4cd;
_4d4.show=_4cc.showTooltip;
_4d4.hide=_4cc.hideTooltip;
_4d4.defaultPosition=["after-centered","before-centered"];
return _4d4;
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(_4fa,lang){
var _4fb=/[&<>'"\/]/g;
var _4fc={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;","/":"&#x2F;"};
var _4fd={};
lang.setObject("dojo.string",_4fd);
_4fd.escape=function(str){
if(!str){
return "";
}
return str.replace(_4fb,function(c){
return _4fc[c];
});
};
_4fd.rep=function(str,num){
if(num<=0||!str){
return "";
}
var buf=[];
for(;;){
if(num&1){
buf.push(str);
}
if(!(num>>=1)){
break;
}
str+=str;
}
return buf.join("");
};
_4fd.pad=function(text,size,ch,end){
if(!ch){
ch="0";
}
var out=String(text),pad=_4fd.rep(ch,Math.ceil((size-out.length)/ch.length));
return end?out+pad:pad+out;
};
_4fd.substitute=function(_4fe,map,_4ff,_500){
_500=_500||_4fa.global;
_4ff=_4ff?lang.hitch(_500,_4ff):function(v){
return v;
};
return _4fe.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_501,key,_502){
var _503=lang.getObject(key,false,map);
if(_502){
_503=lang.getObject(_502,false,_500).call(_500,_503,key);
}
return _4ff(_503,key).toString();
});
};
_4fd.trim=String.prototype.trim?lang.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return _4fd;
});
},"curam/util/ui/refresh/RefreshEvent":function(){
define(["dojo/_base/declare"],function(_504){
var _505=_504("curam.util.ui.refresh.RefreshEvent",null,{TYPE_ONLOAD:"onload",TYPE_ONSUBMIT:"onsubmit",SOURCE_CONTEXT_MAIN:"main-content",SOURCE_CONTEXT_DIALOG:"dialog",SOURCE_CONTEXT_INLINE:"inline",_type:null,_context:null,constructor:function(type,_506){
if(!type||!_506){
throw "Required parameters missing.";
}
if(!(type==this.TYPE_ONLOAD||type==this.TYPE_ONSUBMIT)){
throw "Unknown type: "+type;
}
if(!(_506==this.SOURCE_CONTEXT_DIALOG||_506==this.SOURCE_CONTEXT_INLINE||_506==this.SOURCE_CONTEXT_MAIN)){
throw "Unknown context: "+_506;
}
this._type=type;
this._context=_506;
},equals:function(_507){
if(typeof _507!="object"){
return false;
}
if(_507.declaredClass!=this.declaredClass){
return false;
}
return this._type===_507._type&&this._context===_507._context;
}});
return _505;
});
},"dijit/dijit":function(){
define(["./main","./_base","dojo/parser","./_Widget","./_TemplatedMixin","./_Container","./layout/_LayoutWidget","./form/_FormWidget","./form/_FormValueWidget"],function(_508){
return _508;
});
},"dijit/form/_FormValueMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","./_FormWidgetMixin"],function(_509,_50a,keys,lang,on,_50b){
return _509("dijit.form._FormValueMixin",_50b,{readOnly:false,_setReadOnlyAttr:function(_50c){
_50a.set(this.focusNode,"readOnly",_50c);
this._set("readOnly",_50c);
},postCreate:function(){
this.inherited(arguments);
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.value;
}
},_setValueAttr:function(_50d,_50e){
this._handleOnChange(_50d,_50e);
},_handleOnChange:function(_50f,_510){
this._set("value",_50f);
this.inherited(arguments);
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
}});
});
},"idx/form/_CssStateMixin":function(){
define(["dojo","dijit/dijit","dijit/_WidgetBase","dojo/_base/array","dojo/has","dojo/ready","dojo/on","dojo/_base/window","dijit/registry","dojo/dom","dojo/dom-class"],function(dojo,_511,_512,_513,has,_514,on,win,_515,dom,_516){
var _517=dojo.declare("idx.form._CssStateMixin",[],{cssStateNodes:{},hovering:false,active:false,_applyAttributes:function(){
_512.prototype._applyAttributes.apply(this,arguments);
dojo.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","required"],function(attr){
this.watch(attr,dojo.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.stateNode,this.oneuiBaseClass);
this._setStateClass();
},_cssMouseEvent:function(_518){
if(!this.disabled){
switch(_518.type){
case "mouseover":
this._set("hovering",true);
this._set("active",this._mouseDown);
break;
case "mouseout":
this._set("hovering",false);
this._set("active",false);
break;
case "mousedown":
case "touchstart":
this._set("active",true);
break;
case "mouseup":
case "touchend":
this._set("active",false);
break;
}
}
},_setStateClass:function(){
var _519=this._getModifiedClasses(this.oneuiBaseClass);
this._applyStateClass(this.stateNode,_519);
_519=this._getModifiedClasses(this.baseClass);
this._applyStateClass(this.domNode,_519);
},_getModifiedClasses:function(_51a){
var _51b=_51a.split(" ");
function _51c(_51d){
_51b=_51b.concat(dojo.map(_51b,function(c){
return c+_51d;
}),"dijit"+_51d);
};
if(!this.isLeftToRight()){
_51c("Rtl");
}
var _51e=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_51c(_51e);
}
if(this.state){
_51c(this.state);
}
if(this.selected){
_51c("Selected");
}
if(this.required){
_51c("Required");
}
if(this.disabled){
_51c("Disabled");
}else{
if(this.readOnly){
_51c("ReadOnly");
}else{
if(this.active){
_51c("Active");
}else{
if(this.hovering){
_51c("Hover");
}
}
}
}
if(this.focused){
_51c("Focused");
}
return _51b;
},_applyStateClass:function(node,_51f){
var _520={};
dojo.forEach(node.className.split(" "),function(c){
_520[c]=true;
});
if("_stateClasses" in node){
dojo.forEach(node._stateClasses,function(c){
delete _520[c];
});
}
dojo.forEach(_51f,function(c){
_520[c]=true;
});
var _521=[];
for(var c in _520){
_521.push(c);
}
node.className=_521.join(" ");
node._stateClasses=_51f;
},_subnodeCssMouseEvent:function(node,_522,evt){
if(this.disabled||this.readOnly){
return;
}
var _523=_522.split(/\s+/);
function _524(_525){
_513.forEach(_523,function(_526){
_516.toggle(node,_526+"Hover",_525);
});
};
function _527(_528){
_513.forEach(_523,function(_529){
_516.toggle(node,_529+"Active",_528);
});
};
function _52a(_52b){
_513.forEach(_523,function(_52c){
_516.toggle(node,_52c+"Focused",_52b);
});
};
switch(evt.type){
case "mouseover":
_524(true);
break;
case "mouseout":
_524(false);
_527(false);
break;
case "mousedown":
case "touchstart":
_527(true);
break;
case "mouseup":
case "touchend":
_527(false);
break;
case "focus":
case "focusin":
_52a(true);
break;
case "blur":
case "focusout":
_52a(false);
break;
}
},_trackMouseState:function(node,_52d){
node._cssState=_52d;
}});
_514(function(){
function _52e(evt){
if(!dom.isDescendant(evt.relatedTarget,evt.target)){
for(var node=evt.target;node&&node!=evt.relatedTarget;node=node.parentNode){
if(node._cssState){
var _52f=_515.getEnclosingWidget(node);
if(_52f){
if(node==_52f.oneuiBaseNode){
_52f._cssMouseEvent(evt);
}else{
if(node==_52f.domNode){
_52f._cssMouseEvent(evt);
}else{
_52f._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
}
}
}
}
};
function _530(evt){
evt.target=evt.srcElement;
_52e(evt);
};
var body=win.body(),_531=(has("touch")?[]:["mouseover","mouseout"]).concat(["mousedown","touchstart","mouseup","touchend"]);
_513.forEach(_531,function(type){
if(body.addEventListener){
body.addEventListener(type,_52e,true);
}else{
body.attachEvent("on"+type,_530);
}
});
on(body,"focusin, focusout",function(evt){
var node=evt.target;
if(node._cssState&&!node.getAttribute("widgetId")){
var _532=_515.getEnclosingWidget(node);
_532._subnodeCssMouseEvent(node,node._cssState,evt);
}
});
});
return _517;
});
},"dijit/form/_FormWidgetMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","dojo/window","../a11y"],function(_533,_534,_535,_536,lang,_537,on,has,_538,a11y){
return _534("dijit.form._FormWidgetMixin",null,{name:"",alt:"",value:"",type:"text","aria-label":"focusNode",tabIndex:"0",_setTabIndexAttr:"focusNode",disabled:false,intermediateChanges:false,scrollOnFocus:true,_setIdAttr:"focusNode",_setDisabledAttr:function(_539){
this._set("disabled",_539);
if(/^(button|input|select|textarea|optgroup|option|fieldset)$/i.test(this.focusNode.tagName)){
_535.set(this.focusNode,"disabled",_539);
}else{
this.focusNode.setAttribute("aria-disabled",_539?"true":"false");
}
if(this.valueNode){
_535.set(this.valueNode,"disabled",_539);
}
if(_539){
this._set("hovering",false);
this._set("active",false);
var _53a="tabIndex" in this.attributeMap?this.attributeMap.tabIndex:("_setTabIndexAttr" in this)?this._setTabIndexAttr:"focusNode";
_533.forEach(lang.isArray(_53a)?_53a:[_53a],function(_53b){
var node=this[_53b];
if(has("webkit")||a11y.hasDefaultTabStop(node)){
node.setAttribute("tabIndex","-1");
}else{
node.removeAttribute("tabIndex");
}
},this);
}else{
if(this.tabIndex!=""){
this.set("tabIndex",this.tabIndex);
}
}
},_onFocus:function(by){
if(by=="mouse"&&this.isFocusable()){
var _53c=this.own(on(this.focusNode,"focus",function(){
_53d.remove();
_53c.remove();
}))[0];
var _53e=has("pointer-events")?"pointerup":has("MSPointer")?"MSPointerUp":has("touch-events")?"touchend, mouseup":"mouseup";
var _53d=this.own(on(this.ownerDocumentBody,_53e,lang.hitch(this,function(evt){
_53d.remove();
_53c.remove();
if(this.focused){
if(evt.type=="touchend"){
this.defer("focus");
}else{
this.focus();
}
}
})))[0];
}
if(this.scrollOnFocus){
this.defer(function(){
_538.scrollIntoView(this.domNode);
});
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&this.focusNode&&(_536.get(this.domNode,"display")!="none");
},focus:function(){
if(!this.disabled&&this.focusNode.focus){
try{
this.focusNode.focus();
}
catch(e){
}
}
},compare:function(val1,val2){
if(typeof val1=="number"&&typeof val2=="number"){
return (isNaN(val1)&&isNaN(val2))?0:val1-val2;
}else{
if(val1>val2){
return 1;
}else{
if(val1<val2){
return -1;
}else{
return 0;
}
}
}
},onChange:function(){
},_onChangeActive:false,_handleOnChange:function(_53f,_540){
if(this._lastValueReported==undefined&&(_540===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_53f;
}
this._pendingOnChange=this._pendingOnChange||(typeof _53f!=typeof this._lastValueReported)||(this.compare(_53f,this._lastValueReported)!=0);
if((this.intermediateChanges||_540||_540===undefined)&&this._pendingOnChange){
this._lastValueReported=_53f;
this._pendingOnChange=false;
if(this._onChangeActive){
if(this._onChangeHandle){
this._onChangeHandle.remove();
}
this._onChangeHandle=this.defer(function(){
this._onChangeHandle=null;
this.onChange(_53f);
});
}
}
},create:function(){
this.inherited(arguments);
this._onChangeActive=true;
},destroy:function(){
if(this._onChangeHandle){
this._onChangeHandle.remove();
this.onChange(this._lastValueReported);
}
this.inherited(arguments);
}});
});
},"dijit/a11yclick":function(){
define(["dojo/keys","dojo/mouse","dojo/on","dojo/touch"],function(keys,_541,on,_542){
function _543(e){
if((e.keyCode===keys.ENTER||e.keyCode===keys.SPACE)&&!/input|button|textarea/i.test(e.target.nodeName)){
for(var node=e.target;node;node=node.parentNode){
if(node.dojoClick){
return true;
}
}
}
};
var _544;
on(document,"keydown",function(e){
if(_543(e)){
_544=e.target;
e.preventDefault();
}else{
_544=null;
}
});
on(document,"keyup",function(e){
if(_543(e)&&e.target==_544){
_544=null;
on.emit(e.target,"click",{cancelable:true,bubbles:true,ctrlKey:e.ctrlKey,shiftKey:e.shiftKey,metaKey:e.metaKey,altKey:e.altKey,_origType:e.type});
}
});
var _545=function(node,_546){
node.dojoClick=true;
return on(node,"click",_546);
};
_545.click=_545;
_545.press=function(node,_547){
var _548=on(node,_542.press,function(evt){
if(evt.type=="mousedown"&&!_541.isLeft(evt)){
return;
}
_547(evt);
}),_549=on(node,"keydown",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_547(evt);
}
});
return {remove:function(){
_548.remove();
_549.remove();
}};
};
_545.release=function(node,_54a){
var _54b=on(node,_542.release,function(evt){
if(evt.type=="mouseup"&&!_541.isLeft(evt)){
return;
}
_54a(evt);
}),_54c=on(node,"keyup",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_54a(evt);
}
});
return {remove:function(){
_54b.remove();
_54c.remove();
}};
};
_545.move=_542.move;
return _545;
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_54d,_54e,_54f){
return _54f("dijit.Destroyable",null,{destroy:function(_550){
this._destroyed=true;
},own:function(){
var _551=["destroyRecursive","destroy","remove"];
_54d.forEach(arguments,function(_552){
var _553;
var odh=_54e.before(this,"destroy",function(_554){
_552[_553](_554);
});
var hdhs=[];
function _555(){
odh.remove();
_54d.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_552.then){
_553="cancel";
_552.then(_555,_555);
}else{
_54d.forEach(_551,function(_556){
if(typeof _552[_556]==="function"){
if(!_553){
_553=_556;
}
hdhs.push(_54e.after(_552,_556,_555,true));
}
});
}
},this);
return arguments;
}});
});
},"curam/inspection/Layer":function(){
define(["curam/define"],function(def){
curam.define.singleton("curam.inspection.Layer",{register:function(_557,inst){
require(["curam/util"]);
var tWin=curam.util.getTopmostWindow();
return tWin.inspectionManager?tWin.inspectionManager.observe(_557,inst):null;
}});
var ref=curam.inspection.Layer;
require(["curam/util"]);
ref.tWin=curam.util.getTopmostWindow();
var _558=ref.tWin.inspectionManager?ref.tWin.inspectionManager.getDirects():[];
if(_558.length>0){
require(_558);
}
return ref;
});
},"dijit/WidgetSet":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","./registry"],function(_559,_55a,_55b,_55c){
var _55d=_55a("dijit.WidgetSet",null,{constructor:function(){
this._hash={};
this.length=0;
},add:function(_55e){
if(this._hash[_55e.id]){
throw new Error("Tried to register widget with id=="+_55e.id+" but that id is already registered");
}
this._hash[_55e.id]=_55e;
this.length++;
},remove:function(id){
if(this._hash[id]){
delete this._hash[id];
this.length--;
}
},forEach:function(func,_55f){
_55f=_55f||_55b.global;
var i=0,id;
for(id in this._hash){
func.call(_55f,this._hash[id],i++,this._hash);
}
return this;
},filter:function(_560,_561){
_561=_561||_55b.global;
var res=new _55d(),i=0,id;
for(id in this._hash){
var w=this._hash[id];
if(_560.call(_561,w,i++,this._hash)){
res.add(w);
}
}
return res;
},byId:function(id){
return this._hash[id];
},byClass:function(cls){
var res=new _55d(),id,_562;
for(id in this._hash){
_562=this._hash[id];
if(_562.declaredClass==cls){
res.add(_562);
}
}
return res;
},toArray:function(){
var ar=[];
for(var id in this._hash){
ar.push(this._hash[id]);
}
return ar;
},map:function(func,_563){
return _559.map(this.toArray(),func,_563);
},every:function(func,_564){
_564=_564||_55b.global;
var x=0,i;
for(i in this._hash){
if(!func.call(_564,this._hash[i],x++,this._hash)){
return false;
}
}
return true;
},some:function(func,_565){
_565=_565||_55b.global;
var x=0,i;
for(i in this._hash){
if(func.call(_565,this._hash[i],x++,this._hash)){
return true;
}
}
return false;
}});
_559.forEach(["forEach","filter","byClass","map","every","some"],function(func){
_55c[func]=_55d.prototype[func];
});
return _55d;
});
},"dojo/store/util/SimpleQueryEngine":function(){
define(["../../_base/array"],function(_566){
return function(_567,_568){
switch(typeof _567){
default:
throw new Error("Can not query with a "+typeof _567);
case "object":
case "undefined":
var _569=_567;
_567=function(_56a){
for(var key in _569){
var _56b=_569[key];
if(_56b&&_56b.test){
if(!_56b.test(_56a[key],_56a)){
return false;
}
}else{
if(_56b!=_56a[key]){
return false;
}
}
}
return true;
};
break;
case "string":
if(!this[_567]){
throw new Error("No filter function "+_567+" was found in store");
}
_567=this[_567];
case "function":
}
function _56c(_56d){
var _56e=_566.filter(_56d,_567);
var _56f=_568&&_568.sort;
if(_56f){
_56e.sort(typeof _56f=="function"?_56f:function(a,b){
for(var sort,i=0;sort=_56f[i];i++){
var _570=a[sort.attribute];
var _571=b[sort.attribute];
_570=_570!=null?_570.valueOf():_570;
_571=_571!=null?_571.valueOf():_571;
if(_570!=_571){
return !!sort.descending==(_570==null||_570>_571)?-1:1;
}
}
return 0;
});
}
if(_568&&(_568.start||_568.count)){
var _572=_56e.length;
_56e=_56e.slice(_568.start||0,(_568.start||0)+(_568.count||Infinity));
_56e.total=_572;
}
return _56e;
};
_56c.matches=_567;
return _56c;
};
});
},"dijit/typematic":function(){
define(["dojo/_base/array","dojo/_base/connect","dojo/_base/lang","dojo/on","dojo/sniff","./main"],function(_573,_574,lang,on,has,_575){
var _576=(_575.typematic={_fireEventAndReload:function(){
this._timer=null;
this._callback(++this._count,this._node,this._evt);
this._currentTimeout=Math.max(this._currentTimeout<0?this._initialDelay:(this._subsequentDelay>1?this._subsequentDelay:Math.round(this._currentTimeout*this._subsequentDelay)),this._minDelay);
this._timer=setTimeout(lang.hitch(this,"_fireEventAndReload"),this._currentTimeout);
},trigger:function(evt,_577,node,_578,obj,_579,_57a,_57b){
if(obj!=this._obj){
this.stop();
this._initialDelay=_57a||500;
this._subsequentDelay=_579||0.9;
this._minDelay=_57b||10;
this._obj=obj;
this._node=node;
this._currentTimeout=-1;
this._count=-1;
this._callback=lang.hitch(_577,_578);
this._evt={faux:true};
for(var attr in evt){
if(attr!="layerX"&&attr!="layerY"){
var v=evt[attr];
if(typeof v!="function"&&typeof v!="undefined"){
this._evt[attr]=v;
}
}
}
this._fireEventAndReload();
}
},stop:function(){
if(this._timer){
clearTimeout(this._timer);
this._timer=null;
}
if(this._obj){
this._callback(-1,this._node,this._evt);
this._obj=null;
}
},addKeyListener:function(node,_57c,_57d,_57e,_57f,_580,_581){
var type="keyCode" in _57c?"keydown":"charCode" in _57c?"keypress":_574._keypress,attr="keyCode" in _57c?"keyCode":"charCode" in _57c?"charCode":"charOrCode";
var _582=[on(node,type,lang.hitch(this,function(evt){
if(evt[attr]==_57c[attr]&&(_57c.ctrlKey===undefined||_57c.ctrlKey==evt.ctrlKey)&&(_57c.altKey===undefined||_57c.altKey==evt.altKey)&&(_57c.metaKey===undefined||_57c.metaKey==(evt.metaKey||false))&&(_57c.shiftKey===undefined||_57c.shiftKey==evt.shiftKey)){
evt.stopPropagation();
evt.preventDefault();
_576.trigger(evt,_57d,node,_57e,_57c,_57f,_580,_581);
}else{
if(_576._obj==_57c){
_576.stop();
}
}
})),on(node,"keyup",lang.hitch(this,function(){
if(_576._obj==_57c){
_576.stop();
}
}))];
return {remove:function(){
_573.forEach(_582,function(h){
h.remove();
});
}};
},addMouseListener:function(node,_583,_584,_585,_586,_587){
var _588=[on(node,"mousedown",lang.hitch(this,function(evt){
evt.preventDefault();
_576.trigger(evt,_583,node,_584,node,_585,_586,_587);
})),on(node,"mouseup",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_576.stop();
})),on(node,"mouseout",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_576.stop();
})),on(node,"dblclick",lang.hitch(this,function(evt){
evt.preventDefault();
if(has("ie")<9){
_576.trigger(evt,_583,node,_584,node,_585,_586,_587);
setTimeout(lang.hitch(this,_576.stop),50);
}
}))];
return {remove:function(){
_573.forEach(_588,function(h){
h.remove();
});
}};
},addListener:function(_589,_58a,_58b,_58c,_58d,_58e,_58f,_590){
var _591=[this.addKeyListener(_58a,_58b,_58c,_58d,_58e,_58f,_590),this.addMouseListener(_589,_58c,_58d,_58e,_58f,_590)];
return {remove:function(){
_573.forEach(_591,function(h){
h.remove();
});
}};
}});
return _576;
});
},"dijit/MenuItem":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/kernel","dojo/sniff","dojo/_base/lang","./_Widget","./_TemplatedMixin","./_Contained","./_CssStateMixin","dojo/text!./templates/MenuItem.html"],function(_592,dom,_593,_594,_595,has,lang,_596,_597,_598,_599,_59a){
var _59b=_592("dijit.MenuItem"+(has("dojo-bidi")?"_NoBidi":""),[_596,_597,_598,_599],{templateString:_59a,baseClass:"dijitMenuItem",label:"",_setLabelAttr:function(val){
this._set("label",val);
var _59c="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_59c=val.charAt(ndx+1);
var _59d=val.substr(0,ndx);
var _59e=val.substr(ndx+3);
text=_59d+_59c+_59e;
val=_59d+"<span class=\"dijitMenuItemShortcutKey\">"+_59c+"</span>"+_59e;
}else{
text=val;
}
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_59c);
},iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},accelKey:"",disabled:false,_fillContent:function(_59f){
if(_59f&&!("label" in this.params)){
this._set("label",_59f.innerHTML);
}
},buildRendering:function(){
this.inherited(arguments);
var _5a0=this.id+"_text";
_593.set(this.containerNode,"id",_5a0);
if(this.accelKeyNode){
_593.set(this.accelKeyNode,"id",this.id+"_accel");
}
dom.setSelectable(this.domNode,false);
},onClick:function(){
},focus:function(){
try{
if(has("ie")==8){
this.containerNode.focus();
}
this.focusNode.focus();
}
catch(e){
}
},_setSelected:function(_5a1){
_594.toggle(this.domNode,"dijitMenuItemSelected",_5a1);
},setLabel:function(_5a2){
_595.deprecated("dijit.MenuItem.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_5a2);
},setDisabled:function(_5a3){
_595.deprecated("dijit.Menu.setDisabled() is deprecated.  Use set('disabled', bool) instead.","","2.0");
this.set("disabled",_5a3);
},_setDisabledAttr:function(_5a4){
this.focusNode.setAttribute("aria-disabled",_5a4?"true":"false");
this._set("disabled",_5a4);
},_setAccelKeyAttr:function(_5a5){
if(this.accelKeyNode){
this.accelKeyNode.style.display=_5a5?"":"none";
this.accelKeyNode.innerHTML=_5a5;
_593.set(this.containerNode,"colSpan",_5a5?"1":"2");
}
this._set("accelKey",_5a5);
}});
if(has("dojo-bidi")){
_59b=_592("dijit.MenuItem",_59b,{_setLabelAttr:function(val){
this.inherited(arguments);
if(this.textDir==="auto"){
this.applyTextDir(this.textDirNode);
}
}});
}
return _59b;
});
},"dijit/layout/TabController":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/has","dojo/i18n","dojo/_base/lang","./StackController","../registry","../Menu","../MenuItem","curam/inspection/Layer","dojo/text!./templates/_TabButton.html","curam/widget/_TabButton","curam/widget/MenuItem","dojo/i18n!../nls/common"],function(_5a6,dom,_5a7,_5a8,has,i18n,lang,_5a9,_5aa,Menu,_5ab,lm,_5ac,_5ad,_5ae){
var _5af=_5ad;
if(has("dojo-bidi")){
_5af=_5a6("dijit.layout._TabButton",_5af,{_setLabelAttr:function(_5b0){
this.inherited(arguments);
this.applyTextDir(this.iconNode,this.iconNode.alt);
}});
}
var _5b1=_5a6("dijit.layout.TabController",_5a9,{baseClass:"dijitTabController",templateString:"<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'></div>",tabPosition:"top",buttonWidget:_5af,startup:function(){
this.inherited(arguments);
this.connect(this,"onAddChild",function(page,_5b2){
var _5b3=this;
page.controlButton._curamPageId=page.id;
page.controlButton.connect(page.controlButton,"_setCuramVisibleAttr",function(){
if(page.controlButton.curamVisible){
var _5b4=dojo.map(_5b3.getChildren(),function(btn){
return btn._curamPageId;
});
var _5b5=curam.tab.getTabWidgetId(curam.tab.getContainerTab(page.domNode));
var _5b6=curam.util.TabNavigation.getInsertIndex(_5b5,_5b4,page.id);
var _5b7=false;
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked&&(document.activeElement===curam.util.getTopmostWindow().curam.util.tabButtonClicked)){
_5b7=true;
}
_5b3.addChild(page.controlButton,_5b6);
if(_5b7&&(document.activeElement!==curam.util.getTopmostWindow().curam.util.tabButtonClicked)){
curam.util.getTopmostWindow().curam.util.tabButtonClicked.focus();
}
}else{
var _5b8=page.controlButton;
if(dojo.indexOf(_5b3.getChildren(),_5b8)!=-1){
_5b3.removeChild(_5b8);
}
}
});
});
},buttonWidgetCloseClass:"dijitTabCloseButton",postCreate:function(){
this.inherited(arguments);
lm.register("dijit/layout/TabController",this);
var _5b9=new Menu({id:this.id+"_Menu",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,targetNodeIds:[this.domNode],selector:function(node){
return _5a8.contains(node,"dijitClosable")&&!_5a8.contains(node,"dijitTabDisabled");
}});
this.own(_5b9);
var _5ba=i18n.getLocalization("dijit","common"),_5bb=this;
_5b9.addChild(new _5ab({label:_5ba.itemClose,ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,onClick:function(evt){
var _5bc=_5aa.byNode(this.getParent().currentTarget);
_5bb.onCloseButtonClick(_5bc.page);
}}));
var _5bd=i18n.getLocalization("curam.application","TabMenu"),_5be=new _5ae({onClickValue:"_onClickAll",label:_5bd["close.all.tabs.text"],dir:this.dir,lang:this.lang,textDir:this.textDir,onClick:function(evt){
this._onClickAll();
}});
_5b9.addChild(_5be);
},onButtonClick:function(page){
if(!page.controlButton.get("curamDisabled")){
var _5bf=dijit.byId(this.containerId);
_5bf.selectChild(page);
}
}});
_5b1.TabButton=_5af;
return _5b1;
});
},"idx/util":function(){
define(["dojo/_base/lang","idx/main","dojo/_base/kernel","dojo/has","dojo/aspect","dojo/_base/xhr","dojo/_base/window","dojo/_base/url","dojo/date/stamp","dojo/json","dojo/string","dojo/dom-class","dojo/dom-style","dojo/dom-attr","dojo/dom-construct","dojo/dom-geometry","dojo/io-query","dojo/query","dojo/NodeList-dom","dojo/Stateful","dijit/registry","dijit/form/_FormWidget","dijit/_WidgetBase","dojo/_base/sniff","dijit/_base/manager"],function(_5c0,_5c1,_5c2,dHas,_5c3,dXhr,_5c4,dURL,_5c5,_5c6,_5c7,_5c8,_5c9,_5ca,_5cb,_5cc,_5cd,_5ce,_5cf,_5d0,_5d1,_5d2,_5d3){
var _5d4=_5c0.getObject("util",true,_5c1);
_5d4.getVersion=function(full){
var _5d5={url:_5c2.moduleUrl("idx","version.txt"),showProgress:false,handleAs:"json",load:function(_5d6,_5d7){
var msg=_5d6.version;
if(full){
msg+="-";
msg+=_5d6.revision;
}
console.debug(msg);
},error:function(_5d8,_5d9){
console.debug(_5d8);
return;
}};
dXhr.xhrGet(_5d5);
};
_5d4.getOffsetPosition=function(node,root){
var body=_5c4.body();
root=root||body;
var n=node;
var l=0;
var t=0;
while(n!==root){
if(n===body){
throw "idx.util.getOffsetPosition: specified root is not ancestor of specified node";
}
l+=n.offsetLeft;
t+=n.offsetTop;
n=n.offsetParent;
}
return {l:l,t:t};
};
_5d4.typeOfObject=function(_5da){
if(_5c0.isString(_5da)){
return "string";
}
if(typeof _5da=="undefined"){
return "undefined";
}
if(typeof _5da=="number"){
return "number";
}
if(typeof _5da=="boolean"){
return "boolean";
}
if(_5c0.isFunction(_5da)){
return "function";
}
if(_5c0.isArray(_5da)){
return "array";
}
if(_5da instanceof Date){
return "date";
}
if(_5da instanceof dURL){
return "url";
}
return "object";
};
_5d4.parseObject=function(_5db,type){
var _5dc=0;
switch(type){
case "regex":
_5db=""+_5db;
_5dc=_5db.lastIndexOf("/");
if((_5db.length>2)&&(_5db.charAt(0)=="/")&&(_5dc>0)){
return new RegExp(_5db.substring(1,_5dc),((_5dc==_5db.length-1)?undefined:_5db.substring(_5dc+1)));
}else{
return new RegExp(_5db);
}
break;
case "null":
return null;
case "undefined":
return undefined;
case "string":
return ""+_5db;
case "number":
if(typeof _5db=="number"){
return _5db;
}
return _5db.length?Number(_5db):NaN;
case "boolean":
return (typeof _5db=="boolean")?_5db:!(_5db.toLowerCase()=="false");
case "function":
if(_5c0.isFunction(_5db)){
_5db=_5db.toString();
_5db=_5c0.trim(_5db.substring(_5db.indexOf("{")+1,_5db.length-1));
}
try{
if(_5db===""||_5db.search(/[^\w\.]+/i)!=-1){
return new Function(_5db);
}else{
return _5c0.getObject(_5db,false)||new Function(_5db);
}
}
catch(e){
return new Function();
}
case "array":
if(_5c0.isArray(_5db)){
return _5db;
}
return _5db?_5db.split(/\s*,\s*/):[];
case "date":
if(_5db instanceof Date){
return _5db;
}
switch(_5db){
case "":
return new Date("");
case "now":
return new Date();
default:
return _5c5.fromISOString(_5db);
}
case "url":
if(_5db instanceof dURL){
return _5db;
}
return _5c2.baseUrl+_5db;
default:
if(_5d4.typeOfObject(_5db)=="string"){
return _5c6.parse(_5db);
}else{
return _5db;
}
}
};
_5d4.getCSSOptions=function(_5dd,_5de,_5df,_5e0){
var body=_5c4.body();
if((!_5de)||(("canHaveHTML" in _5de)&&(!_5de.canHaveHTML))){
_5de=body;
}
var _5e1=false;
var trav=_5de;
while(trav&&(!_5e1)){
if(trav===body){
_5e1=true;
}
trav=trav.parentNode;
}
var root=null;
if(!_5e1){
trav=_5de;
var _5e2=[];
while(trav){
_5e2.push(_5ca.get(trav,"class"));
trav=trav.parentNode;
}
_5e2.reverse();
var root=_5cb.create("div",{style:"visibility: hidden; display: none;"},body,"last");
var trav=root;
for(var _5e3=0;_5e3<_5e2.length;_5e3++){
var _5e4=_5e2[_5e3];
if(_5e4){
_5e4=_5c7.trim(_5e4);
}
if(_5e4&&_5e4.length==0){
_5e4=null;
}
var _5e5=(_5e4)?{"class":_5e4}:null;
trav=_5cb.create("div",_5e5,trav,"last");
}
_5de=trav;
}
var _5e6=_5cb.create("div",null,_5de);
_5c8.add(_5e6,_5dd);
var _5e7=_5c9.getComputedStyle(_5e6);
var _5e8=null;
if(_5e7){
_5e8=""+_5e7.backgroundImage;
}
_5cb.destroy(_5e6);
if(root){
_5cb.destroy(root);
}
var _5e9=((!_5e8)||(_5e8.length<5)||(_5e8.toLowerCase().substring(0,4)!="url(")||(_5e8.charAt(_5e8.length-1)!=")"));
var _5ea=null;
if(_5e9&&(_5e8==null||_5e8=="none")&&_5e0&&(!_5c0.isString(_5e0))){
_5ea=_5e0;
}
if(!_5ea){
var _5eb=null;
if(_5e9&&(_5e8==null||_5e8=="none")&&_5e0&&_5c0.isString(_5e0)){
_5eb=_5e0;
}else{
if(!_5e9){
_5e8=_5e8.substring(4,_5e8.length-1);
if(_5e8.charAt(0)=="\""){
if(_5e8.length<2){
return null;
}
if(_5e8.charAt(_5e8.length-1)!="\""){
return null;
}
_5e8=_5e8.substring(1,_5e8.length-1);
}
var _5ec=_5e8.lastIndexOf("?");
var _5ed=_5e8.lastIndexOf("/");
if(_5ec<0){
return null;
}
_5eb=_5e8.substring(_5ec+1,_5e8.length);
}
}
if(_5eb==null){
return null;
}
if(_5eb.length==0){
return null;
}
_5ea=_5cd.queryToObject(_5eb);
}
return (_5df)?_5d4.mixin({},_5ea,_5df):_5ea;
};
_5d4.mixinCSSDefaults=function(_5ee,_5ef,_5f0){
if(!_5ee){
return null;
}
var opts=_5d4.getCSSOptions(_5ef,_5f0);
if(!opts){
return null;
}
_5d4.mixin(_5ee,opts);
return opts;
};
_5d4.mixin=function(_5f1,_5f2,_5f3){
if(!_5f1){
return null;
}
if(!_5f2){
return _5f1;
}
if(!_5f3){
_5f3=_5f1;
}
var src={};
for(var _5f4 in _5f2){
if(!(_5f4 in _5f3)){
continue;
}
var _5f5=_5d4.typeOfObject(_5f3[_5f4]);
src[_5f4]=_5d4.parseObject(_5f2[_5f4],_5f5);
}
_5c0.mixin(_5f1,src);
return _5f1;
};
_5d4.recursiveMixin=function(_5f6,_5f7,_5f8){
var _5f9=null;
var _5fa=null;
var _5fb=null;
if(_5f8){
_5f9=_5f8.clone;
_5fa=_5f8.controlField;
if("controlValue" in _5f8){
_5fb=_5f8.controlValue;
}else{
_5fb=true;
}
}
for(field in _5f7){
if(field in _5f6){
var _5fc=_5f6[field];
var _5fd=_5f7[field];
var _5fe=_5d4.typeOfObject(_5fc);
var _5ff=_5d4.typeOfObject(_5fd);
if((_5fe==_5ff)&&(_5fe=="object")&&((!_5fa)||(_5fc[_5fa]==_5fb))){
_5d4.recursiveMixin(_5fc,_5fd,_5f8);
}else{
_5f6[field]=(_5f9)?_5c0.clone(_5fd):_5fd;
}
}else{
_5f6[field]=(_5f9)?_5c0.clone(_5f7[field]):_5f7[field];
}
}
};
_5d4.nullify=function(_600,_601,_602){
var _603=0;
for(_603=0;_603<_602.length;_603++){
var prop=_602[_603];
if(!(prop in _600)){
continue;
}
if((_601)&&(prop in _601)){
continue;
}
_600[prop]=null;
}
};
_5d4._getNodeStyle=function(node){
var _604=_5ca.get(node,"style");
if(!_604){
return null;
}
var _605=null;
if(_5d4.typeOfObject(_604)=="string"){
_605={};
var _606=_604.split(";");
for(var _607=0;_607<_606.length;_607++){
var _608=_606[_607];
var _609=_608.indexOf(":");
if(_609<0){
continue;
}
var _60a=_608.substring(0,_609);
var _60b="";
if(_609<_608.length-1){
_60b=_608.substring(_609+1);
}
_605[_60a]=_60b;
}
}else{
_605=_604;
}
return _605;
};
_5d4._getNodePosition=function(node){
var _60c=_5d4._getNodeStyle(node);
if(!_60c){
return "";
}
if(!_60c.position){
return "";
}
return _60c.position;
};
_5d4.fitToWidth=function(_60d,_60e){
var pos=_5d4._getNodePosition(_60e);
_5c9.set(_60d,{width:"auto"});
_5c9.set(_60e,{position:"static"});
var dim=_5cc.getMarginBox(_60d);
_5c9.set(_60d,{width:dim.w+"px"});
_5c9.set(_60e,{position:pos});
return dim;
};
_5d4.fitToHeight=function(_60f,_610){
var pos=_5d4._getNodePosition(_610);
_5c9.set(_60f,{height:"auto"});
_5c9.set(_610,{position:"static"});
var dim=_5cc.getMarginBox(_60f);
_5c9.set(_60f,{height:dim.h+"px"});
_5c9.set(_610,{position:pos});
return dim;
};
_5d4.fitToSize=function(_611,_612){
var pos=_5d4._getNodePosition(_612);
_5c9.set(_611,{width:"auto",height:"auto"});
_5c9.set(_612,{position:"static"});
var dim=_5cc.getMarginBox(_611);
_5c9.set(_611,{width:dim.w+"px",height:dim.h+"px"});
_5c9.set(_612,{position:pos});
return dim;
};
_5d4.getStaticSize=function(node){
var _613=_5d4._getNodeStyle(node);
var pos=(_613&&_613.position)?_613.position:"";
var _614=(_613&&_613.width)?_613.width:"";
var _615=(_613&&_613.height)?_613.height:"";
_5c9.set(node,{position:"static",width:"auto",height:"auto"});
var dim=_5cc.getMarginBox(node);
_5c9.set(node,{position:pos,width:_614,height:_615});
return dim;
};
_5d4.reposition=function(node,_616){
var _617=_5d4._getNodePosition(node);
_5c9.set(node,{position:_616});
return _617;
};
_5d4.getParentWidget=function(_618,_619){
var _61a=(_618 instanceof _5d3)?_618.domNode:_618;
var _61b=_61a.parentNode;
if(_61b==null){
return null;
}
var _61c=_5d1.getEnclosingWidget(_61b);
while((_619)&&(_61c)&&(!(_61c instanceof _619))){
_61b=_61c.domNode.parentNode;
_61c=null;
if(_61b){
_61c=_5d1.getEnclosingWidget(_61b);
}
}
return _61c;
};
_5d4.getSiblingWidget=function(_61d,_61e,_61f){
var _620=(_61d instanceof _5d3)?_61d.domNode:_61d;
var _621=_620.parentNode;
if(_621==null){
return null;
}
var _622=_621.childNodes;
if(!_622){
return null;
}
var _623=0;
for(_623=0;_623<_622.length;_623++){
if(_622[_623]==_620){
break;
}
}
if(_623==_622.length){
return null;
}
var step=(_61e)?-1:1;
var _624=(_61e)?-1:_622.length;
var _625=0;
var _626=null;
for(_625=(_623+step);_625!=_624;(_625+=step)){
var _627=_622[_625];
var _628=_5d1.getEnclosingWidget(_627);
if(!_628){
continue;
}
if(_628.domNode==_627){
if((!_61f)||(_628 instanceof _61f)){
_626=_628;
break;
}
}
}
return _626;
};
_5d4.getChildWidget=function(_629,last,_62a){
if(!(_629 instanceof _5d3)){
var _62b=_5d1.getEnclosingWidget(_629);
if(_62b){
_629=_62b;
}
}
var _62c=null;
if(_629 instanceof _5d3){
_62c=_629.getChildren();
}else{
_62c=_629.childNodes;
}
if(!_62c){
return null;
}
if(_62c.length==0){
return null;
}
var _62d=(last)?(_62c.length-1):0;
var step=(last)?-1:1;
var _62e=(last)?-1:_62c.length;
var _62f=0;
var _630=null;
for(_62f=_62d;_62f!=_62e;(_62f+=step)){
var _62b=_62c[_62f];
if(!(_62b instanceof _5d3)){
var node=_62b;
_62b=_5d1.getEnclosingWidget(node);
if(!_62b){
continue;
}
if(_62b.domNode!=node){
continue;
}
}
if((!_62a)||(_62b instanceof _62a)){
_630=_62b;
break;
}
}
return _630;
};
_5d4.getFormWidget=function(_631,_632){
var _633=null;
if(!_632){
_632=_5c4.body();
}else{
if(_632 instanceof _5d3){
_633=_632.domNode;
}else{
_633=form;
}
}
var _634=null;
var _635=_5ce("[name='"+_631+"']",_633);
for(var _636=0;(!_634)&&(_636<_635.length);_636++){
var node=_635[_636];
var _637=_5d1.getEnclosingWidget(node);
if(!_637){
continue;
}
if(!(_637 instanceof _5d2)){
continue;
}
var name=_637.get("name");
if(name!=_631){
continue;
}
_634=_637;
}
return _634;
};
_5d4.isNodeOrElement=function(obj){
return ((obj.parentNode)&&(obj.childNodes)&&(obj.attributes))?true:false;
};
_5d4.debugObject=function(obj){
return _5d4._debugObject(obj,"/",[]);
};
_5d4._debugObject=function(obj,path,seen){
if(obj===null){
return "null";
}
var _638=_5d4.typeOfObject(obj);
switch(_638){
case "object":
for(var _639=0;_639<seen.length;_639++){
if(seen[_639].obj==obj){
return "CIRCULAR_REFERENCE[ "+seen[_639].path+" ]";
}
}
seen.push({obj:obj,path:path});
var _63a="{ ";
var _63b="";
for(field in obj){
_63a=(_63a+_63b+"\""+field+"\": "+_5d4._debugObject(obj[field],path+"/\""+field+"\"",seen));
_63b=", ";
}
_63a=_63a+" }";
return _63a;
case "date":
return "DATE[ "+_5c5.toISOString(obj)+" ]";
default:
return _5c6.stringify(obj);
}
};
_5d4.isBrowser=dHas("host-browser");
_5d4.isIE=dHas("ie");
_5d4.isFF=dHas("ff");
_5d4.isSafari=dHas("safari");
_5d4.isChrome=dHas("chrome");
_5d4.isMozilla=dHas("mozilla");
_5d4.isOpera=dHas("opera");
_5d4.isKhtml=dHas("khtml");
_5d4.isAIR=dHas("air");
_5d4.isQuirks=dHas("quirks");
_5d4.isWebKit=dHas("webkit");
_5d4.fromJson=typeof JSON!="undefined"?JSON.parse:(function(){
var _63c="(?:-?\\b(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?\\b)";
var _63d="(?:[^\\0-\\x08\\x0a-\\x1f\"\\\\]"+"|\\\\(?:[\"/\\\\bfnrt]|u[0-9A-Fa-f]{4}))";
var _63e="(?:\""+_63d+"*\")";
var _63f=new RegExp("(?:false|true|null|[\\{\\}\\[\\]]"+"|"+_63c+"|"+_63e+")","g");
var _640=new RegExp("\\\\(?:([^u])|u(.{4}))","g");
var _641={"\"":"\"","/":"/","\\":"\\","b":"\b","f":"\f","n":"\n","r":"\r","t":"\t"};
function _642(_643,ch,hex){
return ch?_641[ch]:String.fromCharCode(parseInt(hex,16));
};
var _644=new String("");
var _645="\\";
var _646={"{":Object,"[":Array};
var hop=Object.hasOwnProperty;
return function(json,_647){
var toks=json.match(_63f);
var _648;
var tok=toks[0];
var _649=false;
if("{"===tok){
_648={};
}else{
if("["===tok){
_648=[];
}else{
_648=[];
_649=true;
}
}
var key;
var _64a=[_648];
for(var i=1-_649,n=toks.length;i<n;++i){
tok=toks[i];
var cont;
switch(tok.charCodeAt(0)){
default:
cont=_64a[0];
cont[key||cont.length]=+(tok);
key=void 0;
break;
case 34:
tok=tok.substring(1,tok.length-1);
if(tok.indexOf(_645)!==-1){
tok=tok.replace(_640,_642);
}
cont=_64a[0];
if(!key){
if(cont instanceof Array){
key=cont.length;
}else{
key=tok||_644;
break;
}
}
cont[key]=tok;
key=void 0;
break;
case 91:
cont=_64a[0];
_64a.unshift(cont[key||cont.length]=[]);
key=void 0;
break;
case 93:
_64a.shift();
break;
case 102:
cont=_64a[0];
cont[key||cont.length]=false;
key=void 0;
break;
case 110:
cont=_64a[0];
cont[key||cont.length]=null;
key=void 0;
break;
case 116:
cont=_64a[0];
cont[key||cont.length]=true;
key=void 0;
break;
case 123:
cont=_64a[0];
_64a.unshift(cont[key||cont.length]={});
key=void 0;
break;
case 125:
_64a.shift();
break;
}
}
if(_649){
if(_64a.length!==1){
throw new Error();
}
_648=_648[0];
}else{
if(_64a.length){
throw new Error();
}
}
if(_647){
var walk=function(_64b,key){
var _64c=_64b[key];
if(_64c&&typeof _64c==="object"){
var _64d=null;
for(var k in _64c){
if(hop.call(_64c,k)&&_64c!==_64b){
var v=walk(_64c,k);
if(v!==void 0){
_64c[k]=v;
}else{
if(!_64d){
_64d=[];
}
_64d.push(k);
}
}
}
if(_64d){
for(var i=_64d.length;--i>=0;){
delete _64c[_64d[i]];
}
}
}
return _647.call(_64b,key,_64c);
};
_648=walk({"":_648},"");
}
return _648;
};
})();
function _64e(_64f,_650){
var _651=_64f;
if(!_651){
_651={"1em":-1,"1ex":-1,"100%":-1,"12pt":-1,"16px":-1,"xx-small":-1,"x-small":-1,"small":1,"medium":-1,"large":-1,"x-large":-1,"xx-large":-1};
}
if(_650&&(!(_650 in _651))){
_651[_650]=-1;
}
var p;
if(dHas("ie")){
_5c4.doc.documentElement.style.fontSize="100%";
}
var div=_5cb.create("div",{style:{position:"absolute",left:"0",top:"-100000px",width:"30px",height:"1000em",borderWidth:"0",margin:"0",padding:"0",outline:"none",lineHeight:"1",overflow:"hidden"}},_5c4.body());
for(p in _651){
if(_651[p]>=0){
continue;
}
_5c9.set(div,"fontSize",p);
_651[p]=Math.round(div.offsetHeight*12/16)*16/12/1000;
}
_5c4.body().removeChild(div);
return _651;
};
function _652(_653){
var _654=_5d4.typeOfObject(_653);
if((_654=="object")&&(_5d4.isNodeOrElement(_653))){
var _655=_5c9.getComputedStyle(_653);
_653=_655["fontSize"];
}else{
if(_654!="string"){
_653=""+_653;
}
}
return _653;
};
var _656=null;
function _657(_658,_659){
var _65a=_5d4.typeOfObject(_658);
if(_65a=="boolean"){
_659=_658;
_658=null;
}else{
_658=_652(_658);
}
if(_656&&_659){
if(_658){
if(_656[_658]){
delete _656[_658];
}
}else{
_656=null;
}
}
if(_659||!_656||(_658&&!_656[_658])){
_656=_64e(_656,_658);
}
return _656;
};
_5d4.normalizedLength=function(len,_65b){
if(len.length===0){
return 0;
}
if(!_65b){
_65b="12pt";
}else{
_65b=_652(_65b);
}
if(len.length>2){
var _65c=len.slice(-2);
var _65d=(_65c=="em"||_65c=="ex");
if(!_65d){
_65b="12pt";
}
var fm=_657(_65b);
var _65e=fm["12pt"]/12;
var _65f=fm[_65b];
var _660=_65f/2;
var val=parseFloat(len);
switch(len.slice(-2)){
case "px":
return val;
case "em":
return val*_65f;
case "ex":
return val*_660;
case "pt":
return val*_65e;
case "in":
return val*72*_65e;
case "pc":
return val*12*_65e;
case "mm":
return val*g.mm_in_pt*_65e;
case "cm":
return val*g.cm_in_pt*_65e;
}
}
return parseFloat(len);
};
_5d4.isPercentage=function(_661){
return /^\d+%$/.test(_661);
};
_5d4.includeValidCSSWidth=function(_662){
return /width:\s*\d+(%|px|pt|in|pc|mm|cm)/.test(_662);
};
_5d4.getValidCSSWidth=function(_663){
var _664=_5d4.typeOfObject(_663);
if(_664!="string"){
if("width" in _663){
_663="width: "+_663.width+";";
}else{
_663="width: "+_663+";";
}
}
return /width:\s*\d+(%|px|pt|in|pc|mm|cm|em|ex)/.test(_663)?_663.match(/width:\s*(\d+(%|px|pt|in|pc|mm|cm|em|ex))/)[1]:"";
};
_5d4.widgetEquals=function(w1,w2){
if(w1===w2){
return true;
}
if(!w1&&w2){
return false;
}
if(w1&&!w2){
return false;
}
if((!_5c0.isString(w1))&&("id" in w1)){
w1=w1.id;
}
if((!_5c0.isString(w2))&&("id" in w2)){
w2=w2.id;
}
return (w1==w2);
};
_5d4.watch=function(obj,attr,func){
if(!obj){
return null;
}
if(!attr){
return null;
}
if((!("watch" in obj))||(!_5c0.isFunction(obj.watch))){
return null;
}
attr=_5c7.trim(attr);
if(attr.length==0){
return null;
}
var uc=attr.charAt(0).toUpperCase();
if(attr.length>1){
uc=uc+attr.substring(1);
}
var _665="_set"+uc+"Attr";
if(_665 in obj){
return _5c3.around(obj,_665,function(_666){
return function(_667){
var _668=obj.get(attr);
_666.apply(obj,arguments);
var _669=obj.get(attr);
if(_668!=_669){
func.call(undefined,attr,_668,_669);
}
};
});
}else{
return obj.watch(attr,func);
}
};
return _5d4;
});
},"dijit/layout/_LayoutWidget":function(){
define(["dojo/_base/lang","../_Widget","../_Container","../_Contained","../Viewport","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style"],function(lang,_66a,_66b,_66c,_66d,_66e,_66f,_670,_671){
return _66e("dijit.layout._LayoutWidget",[_66a,_66b,_66c],{baseClass:"dijitLayoutContainer",isLayoutContainer:true,_setTitleAttr:null,buildRendering:function(){
this.inherited(arguments);
_66f.add(this.domNode,"dijitContainer");
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
var _672=this.getParent&&this.getParent();
if(!(_672&&_672.isLayoutContainer)){
this.resize();
this.own(_66d.on("resize",lang.hitch(this,"resize")));
}
},resize:function(_673,_674){
var node=this.domNode;
if(_673){
_670.setMarginBox(node,_673);
}
var mb=_674||{};
lang.mixin(mb,_673||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_670.getMarginBox(node),mb);
}
var cs=_671.getComputedStyle(node);
var me=_670.getMarginExtents(node,cs);
var be=_670.getBorderExtents(node,cs);
var bb=(this._borderBox={w:mb.w-(me.w+be.w),h:mb.h-(me.h+be.h)});
var pe=_670.getPadExtents(node,cs);
this._contentBox={l:_671.toPixelValue(node,cs.paddingLeft),t:_671.toPixelValue(node,cs.paddingTop),w:bb.w-pe.w,h:bb.h-pe.h};
this.layout();
},layout:function(){
},_setupChild:function(_675){
var cls=this.baseClass+"-child "+(_675.baseClass?this.baseClass+"-"+_675.baseClass:"");
_66f.add(_675.domNode,cls);
},addChild:function(_676,_677){
this.inherited(arguments);
if(this._started){
this._setupChild(_676);
}
},removeChild:function(_678){
var cls=this.baseClass+"-child"+(_678.baseClass?" "+this.baseClass+"-"+_678.baseClass:"");
_66f.remove(_678.domNode,cls);
this.inherited(arguments);
}});
});
},"dijit/popup":function(){
define("dijit/popup",["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./place","./BackgroundIframe","./Viewport","./main","dojo/touch"],function(_679,_67a,_67b,dom,_67c,_67d,_67e,_67f,_680,has,keys,lang,on,_681,_682,_683,_684){
function _685(){
if(this._popupWrapper){
_67e.destroy(this._popupWrapper);
delete this._popupWrapper;
}
};
var _686=_67b(null,{_stack:[],_beginZIndex:1000,_idGen:1,_repositionAll:function(){
if(this._firstAroundNode){
var _687=this._firstAroundPosition,_688=_67f.position(this._firstAroundNode,true),dx=_688.x-_687.x,dy=_688.y-_687.y;
if(dx||dy){
this._firstAroundPosition=_688;
for(var i=0;i<this._stack.length;i++){
var _689=this._stack[i].wrapper.style;
_689.top=(parseFloat(_689.top)+dy)+"px";
if(_689.right=="auto"){
_689.left=(parseFloat(_689.left)+dx)+"px";
}else{
_689.right=(parseFloat(_689.right)-dx)+"px";
}
}
}
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),dx||dy?10:50);
}
},_createWrapper:function(_68a){
var _68b=_68a._popupWrapper,node=_68a.domNode;
if(!_68b){
_68b=_67e.create("div",{"class":"dijitPopup",style:{display:"none"},role:"region","aria-label":_68a["aria-label"]||_68a.label||_68a.name||_68a.id},_68a.ownerDocumentBody);
_68b.appendChild(node);
var s=node.style;
s.display="";
s.visibility="";
s.position="";
s.top="0px";
_68a._popupWrapper=_68b;
_67a.after(_68a,"destroy",_685,true);
if("ontouchend" in document){
on(_68b,"touchend",function(evt){
if(!/^(input|button|textarea)$/i.test(evt.target.tagName)){
evt.preventDefault();
}
});
}
_68b.dojoClick=true;
}
return _68b;
},moveOffScreen:function(_68c){
var _68d=this._createWrapper(_68c);
var ltr=_67f.isBodyLtr(_68c.ownerDocument),_68e={visibility:"hidden",top:"-9999px",display:""};
_68e[ltr?"left":"right"]="-9999px";
_68e[ltr?"right":"left"]="auto";
_680.set(_68d,_68e);
return _68d;
},hide:function(_68f){
var _690=this._createWrapper(_68f);
if(dojo.hasClass(_690,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
_67c.set(_690,"aria-hidden","true");
_680.set(_690,{position:"absolute",overflow:"hidden",clip:"rect(0 0 0 0)",height:"1px",width:"1px",margin:"-1px",padding:"0",border:"0"});
setTimeout(function(){
if(_67c.get(_690,"aria-hidden","true")){
_680.set(_690,{display:"none",height:"auto",overflowY:"visible",border:"",position:"",overflow:"",clip:"",width:"",margin:"",padding:""});
}
},200,_690);
}else{
_680.set(_690,{display:"none",height:"auto",overflowY:"visible",border:""});
}
var node=_68f.domNode;
if("_originalStyle" in node){
node.style.cssText=node._originalStyle;
}
},getTopPopup:function(){
var _691=this._stack;
for(var pi=_691.length-1;pi>0&&_691[pi].parent===_691[pi-1].widget;pi--){
}
return _691[pi];
},open:function(args){
var _692=this._stack,_693=args.popup,node=_693.domNode,_694=args.orient||["below","below-alt","above","above-alt"],ltr=args.parent?args.parent.isLeftToRight():_67f.isBodyLtr(_693.ownerDocument),_695=args.around,id=(args.around&&args.around.id)?(args.around.id+"_dropdown"):("popup_"+this._idGen++);
while(_692.length&&(!args.parent||!dom.isDescendant(args.parent.domNode,_692[_692.length-1].widget.domNode))){
this.close(_692[_692.length-1].widget);
}
var _696=this.moveOffScreen(_693);
if(_693.startup&&!_693._started){
_693.startup();
}
var _697,_698=_67f.position(node);
if("maxHeight" in args&&args.maxHeight!=-1){
_697=args.maxHeight||Infinity;
}else{
var _699=_683.getEffectiveBox(this.ownerDocument),_69a=_695?_67f.position(_695,false):{y:args.y-(args.padding||0),h:(args.padding||0)*2};
_697=Math.floor(Math.max(_69a.y,_699.h-(_69a.y+_69a.h)));
}
if(_698.h>_697){
var cs=_680.getComputedStyle(node),_69b=cs.borderLeftWidth+" "+cs.borderLeftStyle+" "+cs.borderLeftColor;
_680.set(_696,{overflowY:"scroll",height:(_697-2)+"px",border:_69b});
node._originalStyle=node.style.cssText;
node.style.border="none";
}
_67c.set(_696,{id:id,style:{zIndex:this._beginZIndex+_692.length},"class":"dijitPopup "+(_693.baseClass||_693["class"]||"").split(" ")[0]+"Popup",dijitPopupParent:args.parent?args.parent.id:""});
if(dojo.hasClass(_696,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
if(_67c.get(_696,"aria-hidden")==="true"){
_680.set(_696,{position:"",clip:"",width:"",margin:"",padding:"",border:""});
}
_67c.set(_696,"aria-hidden","false");
}
if(_692.length==0&&_695){
this._firstAroundNode=_695;
this._firstAroundPosition=_67f.position(_695,true);
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),50);
}
if(has("config-bgIframe")&&!_693.bgIframe){
_693.bgIframe=new _682(_696);
}
var _69c=_693.orient?lang.hitch(_693,"orient"):null,best=_695?_681.around(_696,_695,_694,ltr,_69c):_681.at(_696,args,_694=="R"?["TR","BR","TL","BL"]:["TL","BL","TR","BR"],args.padding,_69c);
_696.style.visibility="visible";
node.style.visibility="visible";
var _69d=[];
_69d.push(on(_696,"keydown",lang.hitch(this,function(evt){
if(evt.keyCode==keys.ESCAPE&&args.onCancel){
evt.stopPropagation();
evt.preventDefault();
args.onCancel(evt);
}else{
if(evt.keyCode==keys.TAB){
evt.stopPropagation();
evt.preventDefault();
var _69e=this.getTopPopup();
if(_69e&&_69e.onCancel){
_69e.onCancel(evt);
}
}
}
})));
if(_693.onCancel&&args.onCancel){
_69d.push(_693.on("cancel",args.onCancel));
}
_69d.push(_693.on(_693.onExecute?"execute":"change",lang.hitch(this,function(){
var _69f=this.getTopPopup();
if(_69f&&_69f.onExecute){
_69f.onExecute();
}
})));
_692.push({widget:_693,wrapper:_696,parent:args.parent,onExecute:args.onExecute,onCancel:args.onCancel,onClose:args.onClose,handlers:_69d});
if(_693.onOpen){
_693.onOpen(best);
}
return best;
},close:function(_6a0){
var _6a1=this._stack;
while((_6a0&&_679.some(_6a1,function(elem){
return elem.widget==_6a0;
}))||(!_6a0&&_6a1.length)){
var top=_6a1.pop(),_6a2=top.widget,_6a3=top.onClose;
if(_6a2.bgIframe){
_6a2.bgIframe.destroy();
delete _6a2.bgIframe;
}
if(_6a2.onClose){
_6a2.onClose();
}
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_6a2&&_6a2.domNode){
this.hide(_6a2);
}
if(_6a3){
_6a3();
}
}
if(_6a1.length==0&&this._aroundMoveListener){
clearTimeout(this._aroundMoveListener);
this._firstAroundNode=this._firstAroundPosition=this._aroundMoveListener=null;
}
}});
return (_684.popup=new _686());
});
},"dijit/_base/manager":function(){
define(["dojo/_base/array","dojo/_base/config","dojo/_base/lang","../registry","../main"],function(_6a4,_6a5,lang,_6a6,_6a7){
var _6a8={};
_6a4.forEach(["byId","getUniqueId","findWidgets","_destroyAll","byNode","getEnclosingWidget"],function(name){
_6a8[name]=_6a6[name];
});
lang.mixin(_6a8,{defaultDuration:_6a5["defaultDuration"]||200});
lang.mixin(_6a7,_6a8);
return _6a7;
});
},"dijit/layout/StackController":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/keys","dojo/_base/lang","dojo/on","dojo/topic","../focus","../registry","../_Widget","../_TemplatedMixin","../_Container","../form/ToggleButton","dojo/touch"],function(_6a9,_6aa,_6ab,_6ac,keys,lang,on,_6ad,_6ae,_6af,_6b0,_6b1,_6b2,_6b3){
var _6b4=_6aa("dijit.layout._StackButton",_6b3,{tabIndex:"-1",closeButton:false,_aria_attr:"aria-selected",buildRendering:function(evt){
this.inherited(arguments);
(this.focusNode||this.domNode).setAttribute("role","tab");
}});
var _6b5=_6aa("dijit.layout.StackController",[_6b0,_6b1,_6b2],{baseClass:"dijitStackController",templateString:"<span role='tablist' data-dojo-attach-event='onkeydown'></span>",containerId:"",buttonWidget:_6b4,buttonWidgetCloseClass:"dijitStackCloseButton",pane2button:function(id){
return _6af.byId(this.id+"_"+id);
},postCreate:function(){
this.inherited(arguments);
this.own(_6ad.subscribe(this.containerId+"-startup",lang.hitch(this,"onStartup")),_6ad.subscribe(this.containerId+"-addChild",lang.hitch(this,"onAddChild")),_6ad.subscribe(this.containerId+"-removeChild",lang.hitch(this,"onRemoveChild")),_6ad.subscribe(this.containerId+"-selectChild",lang.hitch(this,"onSelectChild")),_6ad.subscribe(this.containerId+"-containerKeyDown",lang.hitch(this,"onContainerKeyDown")));
this.containerNode.dojoClick=true;
this.own(on(this.containerNode,"click",lang.hitch(this,function(evt){
var _6b6=_6af.getEnclosingWidget(evt.target);
if(_6b6!=this.containerNode&&!_6b6.disabled&&_6b6.page){
for(var _6b7=evt.target;_6b7!==this.containerNode;_6b7=_6b7.parentNode){
if(_6ab.contains(_6b7,this.buttonWidgetCloseClass)){
this.onCloseButtonClick(_6b6.page);
break;
}else{
if(_6b7==_6b6.domNode){
this.onButtonClick(_6b6.page);
break;
}
}
}
}
})));
},onStartup:function(info){
this.textDir=info.textDir;
_6a9.forEach(info.children,this.onAddChild,this);
if(info.selected){
this.onSelectChild(info.selected);
}
var _6b8=_6af.byId(this.containerId).containerNode,_6b9=lang.hitch(this,"pane2button"),_6ba={"title":"label","showtitle":"showLabel","iconclass":"iconClass","closable":"closeButton","tooltip":"title","disabled":"disabled","textdir":"textdir"},_6bb=function(attr,_6bc){
return on(_6b8,"attrmodified-"+attr,function(evt){
var _6bd=_6b9(evt.detail&&evt.detail.widget&&evt.detail.widget.id);
if(_6bd){
_6bd.set(_6bc,evt.detail.newValue);
}
});
};
for(var attr in _6ba){
this.own(_6bb(attr,_6ba[attr]));
}
},destroy:function(_6be){
this.destroyDescendants(_6be);
this.inherited(arguments);
},onAddChild:function(page,_6bf){
var Cls=lang.isString(this.buttonWidget)?lang.getObject(this.buttonWidget):this.buttonWidget;
var _6c0=new Cls({id:this.id+"_"+page.id,name:this.id+"_"+page.id,label:page.title,disabled:page.disabled,ownerDocument:this.ownerDocument,dir:page.dir,lang:page.lang,textDir:page.textDir||this.textDir,showLabel:page.showTitle,iconClass:page.iconClass,closeButton:page.closable,title:page.tooltip,page:page});
this.addChild(_6c0,_6bf);
page.controlButton=_6c0;
if(!this._currentChild){
this.onSelectChild(page);
}
var _6c1=page._wrapper.getAttribute("aria-labelledby")?page._wrapper.getAttribute("aria-labelledby")+" "+_6c0.id:_6c0.id;
page._wrapper.removeAttribute("aria-label");
page._wrapper.setAttribute("aria-labelledby",_6c1);
},onRemoveChild:function(page){
if(this._currentChild===page){
this._currentChild=null;
}
var _6c2=this.pane2button(page.id);
if(_6c2){
this.removeChild(_6c2);
_6c2.destroy();
}
delete page.controlButton;
},onSelectChild:function(page){
if(!page){
return;
}
if(this._currentChild){
var _6c3=this.pane2button(this._currentChild.id);
_6c3.set("checked",false);
_6c3.focusNode.setAttribute("tabIndex","-1");
}
var _6c4=this.pane2button(page.id);
_6c4.set("checked",true);
this._currentChild=page;
_6c4.focusNode.setAttribute("tabIndex","0");
var _6c5=_6af.byId(this.containerId);
},onButtonClick:function(page){
var _6c6=this.pane2button(page.id);
_6ae.focus(_6c6.focusNode);
if(this._currentChild&&this._currentChild.id===page.id){
_6c6.set("checked",true);
}
var _6c7=_6af.byId(this.containerId);
_6c7.selectChild(page);
},onCloseButtonClick:function(page){
var _6c8=_6af.byId(this.containerId);
_6c8.closeChild(page);
if(this._currentChild){
var b=this.pane2button(this._currentChild.id);
if(b){
_6ae.focus(b.focusNode||b.domNode);
}
}
},adjacent:function(_6c9){
if(!this.isLeftToRight()&&(!this.tabPosition||/top|bottom/.test(this.tabPosition))){
_6c9=!_6c9;
}
var _6ca=this.getChildren();
var idx=_6a9.indexOf(_6ca,this.pane2button(this._currentChild.id)),_6cb=_6ca[idx];
var _6cc;
do{
idx=(idx+(_6c9?1:_6ca.length-1))%_6ca.length;
_6cc=_6ca[idx];
}while(_6cc.disabled&&_6cc!=_6cb);
return _6cc;
},onkeydown:function(e,_6cd){
if(this.disabled||e.altKey){
return;
}
var _6ce=null;
if(e.ctrlKey||!e._djpage){
switch(e.keyCode){
case keys.LEFT_ARROW:
case keys.UP_ARROW:
if(!e._djpage){
_6ce=false;
}
break;
case keys.PAGE_UP:
if(e.ctrlKey){
_6ce=false;
}
break;
case keys.RIGHT_ARROW:
case keys.DOWN_ARROW:
if(!e._djpage){
_6ce=true;
}
break;
case keys.PAGE_DOWN:
if(e.ctrlKey){
_6ce=true;
}
break;
case keys.HOME:
var _6cf=this.getChildren();
for(var idx=0;idx<_6cf.length;idx++){
var _6d0=_6cf[idx];
if(!_6d0.disabled){
this.onButtonClick(_6d0.page);
break;
}
}
e.stopPropagation();
e.preventDefault();
break;
case keys.END:
var _6cf=this.getChildren();
for(var idx=_6cf.length-1;idx>=0;idx--){
var _6d0=_6cf[idx];
if(!_6d0.disabled){
this.onButtonClick(_6d0.page);
break;
}
}
e.stopPropagation();
e.preventDefault();
break;
case keys.DELETE:
case "W".charCodeAt(0):
if(this._currentChild.closable&&(e.keyCode==keys.DELETE||e.ctrlKey)){
this.onCloseButtonClick(this._currentChild);
e.stopPropagation();
e.preventDefault();
}
break;
case keys.TAB:
if(e.ctrlKey){
this.onButtonClick(this.adjacent(!e.shiftKey).page);
e.stopPropagation();
e.preventDefault();
}
break;
}
if(_6ce!==null){
this.onButtonClick(this.adjacent(_6ce).page);
e.stopPropagation();
e.preventDefault();
}
}
},onContainerKeyDown:function(info){
info.e._djpage=info.page;
this.onkeydown(info.e);
}});
_6b5.StackButton=_6b4;
return _6b5;
});
},"curam/util/onLoad":function(){
define(["curam/util","curam/define","curam/debug","dojo/dom-attr"],function(util,_6d1,_6d2,attr){
_6d1.singleton("curam.util.onLoad",{EVENT:"/curam/frame/load",publishers:[],subscribers:[],defaultGetIdFunction:function(_6d3){
var _6d4=attr.get(_6d3,"class").split(" ");
return dojo.filter(_6d4,function(_6d5){
return _6d5.indexOf("iframe-")==0;
})[0];
},addPublisher:function(_6d6){
curam.util.onLoad.publishers.push(_6d6);
},addSubscriber:function(_6d7,_6d8,_6d9){
curam.util.onLoad.subscribers.push({"getId":_6d9?_6d9:curam.util.onLoad.defaultGetIdFunction,"callback":_6d8,"iframeId":_6d7});
},removeSubscriber:function(_6da,_6db,_6dc){
curam.util.onLoad.subscribers=dojo.filter(curam.util.onLoad.subscribers,function(_6dd){
return !(_6dd.iframeId==_6da&&_6dd.callback==_6db);
});
},execute:function(){
if(window.parent==window){
curam.debug.log("curam.util.onLoad.execute(): "+_6d2.getProperty("curam.util.onLoad.exit"));
return;
}
var _6de={};
dojo.forEach(curam.util.onLoad.publishers,function(_6df){
_6df(_6de);
});
curam.util.onLoad.publishers=[];
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
window.parent.dojo.publish(curam.util.onLoad.EVENT,[window.frameElement,_6de]);
}});
curam.util.subscribe(curam.util.onLoad.EVENT,function(_6e0,_6e1){
dojo.forEach(curam.util.onLoad.subscribers,function(_6e2){
var _6e3=_6e2.getId(_6e0);
if(_6e2.iframeId==_6e3){
_6e2.callback(_6e3,_6e1);
}
});
});
return curam.util.onLoad;
});
},"dojo/request/default":function(){
define(["exports","require","../has"],function(_6e4,_6e5,has){
var _6e6=has("config-requestProvider"),_6e7;
if(1||has("host-webworker")){
_6e7="./xhr";
}else{
if(0){
_6e7="./node";
}
}
if(!_6e6){
_6e6=_6e7;
}
_6e4.getPlatformDefaultId=function(){
return _6e7;
};
_6e4.load=function(id,_6e8,_6e9,_6ea){
_6e5([id=="platform"?_6e7:_6e6],function(_6eb){
_6e9(_6eb);
});
};
});
},"dijit/layout/TabContainer":function(){
define(["dojo/_base/lang","dojo/_base/declare","./_TabContainerBase","./TabController","./ScrollingTabController"],function(lang,_6ec,_6ed,_6ee,_6ef){
return _6ec("dijit.layout.TabContainer",_6ed,{useMenu:true,useSlider:true,controllerWidget:"",_makeController:function(_6f0){
var cls=this.baseClass+"-tabs"+(this.doLayout?"":" dijitTabNoLayout"),_6ee=typeof this.controllerWidget=="string"?lang.getObject(this.controllerWidget):this.controllerWidget;
return new _6ee({id:this.id+"_tablist",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,tabPosition:this.tabPosition,doLayout:this.doLayout,containerId:this.id,"class":cls,nested:this.nested,useMenu:this.useMenu,useSlider:this.useSlider,tabStripClass:this.tabStrip?this.baseClass+(this.tabStrip?"":"No")+"Strip":null},_6f0);
},postMixInProperties:function(){
this.inherited(arguments);
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_6ef:_6ee;
}
}});
});
},"dijit/BackgroundIframe":function(){
define(["require","./main","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/sniff"],function(_6f1,_6f2,_6f3,_6f4,_6f5,lang,on,has){
has.add("config-bgIframe",(has("ie")&&!/IEMobile\/10\.0/.test(navigator.userAgent))||(has("trident")&&/Windows NT 6.[01]/.test(navigator.userAgent)));
var _6f6=new function(){
var _6f7=[];
this.pop=function(){
var _6f8;
if(_6f7.length){
_6f8=_6f7.pop();
_6f8.style.display="";
}else{
if(has("ie")<9){
var burl=_6f3["dojoBlankHtmlUrl"]||_6f1.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_6f8=document.createElement(html);
}else{
_6f8=_6f4.create("iframe");
_6f8.src="javascript:\"\"";
_6f8.className="dijitBackgroundIframe";
_6f8.setAttribute("role","presentation");
_6f5.set(_6f8,"opacity",0.1);
}
_6f8.tabIndex=-1;
}
return _6f8;
};
this.push=function(_6f9){
_6f9.style.display="none";
_6f7.push(_6f9);
};
}();
_6f2.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("config-bgIframe")){
var _6fa=(this.iframe=_6f6.pop());
node.appendChild(_6fa);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,"resize",node));
}else{
_6f5.set(_6fa,{width:"100%",height:"100%"});
}
}
};
lang.extend(_6f2.BackgroundIframe,{resize:function(node){
if(this.iframe){
_6f5.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
this.iframe.parentNode.removeChild(this.iframe);
_6f6.push(this.iframe);
delete this.iframe;
}
}});
return _6f2.BackgroundIframe;
});
},"curam/util/Constants":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.util.Constants",{RETURN_PAGE_PARAM:"__o3rpu"});
return curam.util.Constants;
});
},"dijit/form/Button":function(){
define(["require","dojo/_base/declare","dojo/dom-class","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/ready","./_FormWidget","./_ButtonMixin","dojo/text!./templates/Button.html","../a11yclick"],function(_6fb,_6fc,_6fd,has,_6fe,lang,_6ff,_700,_701,_702){
if(1){
_6ff(0,function(){
var _703=["dijit/form/DropDownButton","dijit/form/ComboButton","dijit/form/ToggleButton"];
_6fb(_703);
});
}
var _704=_6fc("dijit.form.Button"+(has("dojo-bidi")?"_NoBidi":""),[_700,_701],{showLabel:true,iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},baseClass:"dijitButton",templateString:_702,_setValueAttr:"valueNode",_setNameAttr:function(name){
if(this.valueNode){
this.valueNode.setAttribute("name",name);
}
},_fillContent:function(_705){
if(_705&&(!this.params||!("label" in this.params))){
var _706=lang.trim(_705.innerHTML);
if(_706){
this.label=_706;
}
}
},_setShowLabelAttr:function(val){
if(this.containerNode){
_6fd.toggle(this.containerNode,"dijitDisplayNone",!val);
}
this._set("showLabel",val);
},setLabel:function(_707){
_6fe.deprecated("dijit.form.Button.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_707);
},_setLabelAttr:function(_708){
this.inherited(arguments);
if(!this.showLabel&&!("title" in this.params)){
this.titleNode.title=lang.trim(this.containerNode.innerText||this.containerNode.textContent||"");
}
}});
if(has("dojo-bidi")){
_704=_6fc("dijit.form.Button",_704,{_setLabelAttr:function(_709){
this.inherited(arguments);
if(this.titleNode.title){
this.applyTextDir(this.titleNode,this.titleNode.title);
}
},_setTextDirAttr:function(_70a){
if(this._created&&this.textDir!=_70a){
this._set("textDir",_70a);
this._setLabelAttr(this.label);
}
}});
}
return _704;
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_70b,_70c,_70d,_70e,_70f,_710,dom,_711,_712,_713,_714,_715,has,_716,lang,on,_717,_718,_719,win,_71a,_71b,_71c){
var _71d=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_716.isAsync);
has.add("dojo-bidi",false);
if(1){
_717(0,function(){
var _71e=["dijit/_base/manager"];
_70b(_71e);
});
}
var _71f={};
function _720(obj){
var ret={};
for(var attr in obj){
ret[attr.toLowerCase()]=true;
}
return ret;
};
function _721(attr){
return function(val){
_711[val?"set":"remove"](this.domNode,attr,val);
this._set(attr,val);
};
};
function _722(a,b){
return a===b||(a!==a&&b!==b);
};
var _723=_710("dijit._WidgetBase",[_718,_71a],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_721("lang"),dir:"",_setDirAttr:_721("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_70e.blankGif||_70b.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var ctor=this.constructor;
if(!ctor._setterAttrs){
var _724=ctor.prototype,_725=ctor._setterAttrs=[],_726=(ctor._onMap={});
for(var name in _724.attributeMap){
_725.push(name);
}
for(name in _724){
if(/^on/.test(name)){
_726[name.substring(2).toLowerCase()]=name;
}
if(/^_set[A-Z](.*)Attr$/.test(name)){
name=name.charAt(4).toLowerCase()+name.substr(5,name.length-9);
if(!_724.attributeMap||!(name in _724.attributeMap)){
_725.push(name);
}
}
}
}
},postscript:function(_727,_728){
this.create(_727,_728);
},create:function(_729,_72a){
if(_71d){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_72a);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_729){
this.params=_729;
lang.mixin(this,_729);
}
this.postMixInProperties();
if(!this.id){
this.id=_71c.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_71c.add(this);
this.buildRendering();
var _72b;
if(this.domNode){
this._applyAttributes();
var _72c=this.srcNodeRef;
if(_72c&&_72c.parentNode&&this.domNode!==_72c){
_72c.parentNode.replaceChild(this.domNode,_72c);
_72b=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_72b){
delete this.srcNodeRef;
}
this._created=true;
if(_71d){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _72d={};
for(var key in this.params||{}){
_72d[key]=this._get(key);
}
_70c.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _72d)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _72d){
this.set(key,_72d[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _72e=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_72e=_72e.concat(_70c.map(_72e,function(name){
return name+"Rtl";
}));
}
_712.add(this.domNode,_72e);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_70c.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_72f){
this._beingDestroyed=true;
this.destroyDescendants(_72f);
this.destroy(_72f);
},destroy:function(_730){
this._beingDestroyed=true;
this.uninitialize();
function _731(w){
if(w.destroyRecursive){
w.destroyRecursive(_730);
}else{
if(w.destroy){
w.destroy(_730);
}
}
};
_70c.forEach(this._connects,lang.hitch(this,"disconnect"));
_70c.forEach(this._supportingWidgets,_731);
if(this.domNode){
_70c.forEach(_71c.findWidgets(this.domNode,this.containerNode),_731);
}
this.destroyRendering(_730);
_71c.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_732){
if(this.bgIframe){
this.bgIframe.destroy(_732);
delete this.bgIframe;
}
if(this.domNode){
if(_732){
_711.remove(this.domNode,"widgetId");
}else{
_713.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_732){
_713.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_733){
_70c.forEach(this.getChildren(),function(_734){
if(_734.destroyRecursive){
_734.destroyRecursive(_733);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_735){
var _736=this.domNode;
if(lang.isObject(_735)){
_715.set(_736,_735);
}else{
if(_736.style.cssText){
_736.style.cssText+="; "+_735;
}else{
_736.style.cssText=_735;
}
}
this._set("style",_735);
},_attrToDom:function(attr,_737,_738){
_738=arguments.length>=3?_738:this.attributeMap[attr];
_70c.forEach(lang.isArray(_738)?_738:[_738],function(_739){
var _73a=this[_739.node||_739||"domNode"];
var type=_739.type||"attribute";
switch(type){
case "attribute":
if(lang.isFunction(_737)){
_737=lang.hitch(this,_737);
}
var _73b=_739.attribute?_739.attribute:(/^on[A-Z][a-zA-Z]*$/.test(attr)?attr.toLowerCase():attr);
if(_73a.tagName){
_711.set(_73a,_73b,_737);
}else{
_73a.set(_73b,_737);
}
break;
case "innerText":
_73a.innerHTML="";
_73a.appendChild(this.ownerDocument.createTextNode(_737));
break;
case "innerHTML":
_73a.innerHTML=_737;
break;
case "class":
_712.replace(_73a,_737,this[attr]);
break;
}
},this);
},get:function(name){
var _73c=this._getAttrNames(name);
return this[_73c.g]?this[_73c.g]():this._get(name);
},set:function(name,_73d){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var _73e=this._getAttrNames(name),_73f=this[_73e.s];
if(lang.isFunction(_73f)){
var _740=_73f.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _741=this.focusNode&&!lang.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_741]&&this[_741].tagName,_742=tag&&(_71f[tag]||(_71f[tag]=_720(this[_741]))),map=name in this.attributeMap?this.attributeMap[name]:_73e.s in this?this[_73e.s]:((_742&&_73e.l in _742&&typeof _73d!="function")||/^aria-|^data-|^role$/.test(name))?_741:null;
if(map!=null){
this._attrToDom(name,_73d,map);
}
this._set(name,_73d);
}
return _740||this;
},_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
var uc=name.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[name]={n:name+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(name,_743){
var _744=this[name];
this[name]=_743;
if(this._created&&!_722(_744,_743)){
if(this._watchCallbacks){
this._watchCallbacks(name,_744,_743);
}
this.emit("attrmodified-"+name,{detail:{prevValue:_744,newValue:_743}});
}
},_get:function(name){
return this[name];
},emit:function(type,_745,_746){
_745=_745||{};
if(_745.bubbles===undefined){
_745.bubbles=true;
}
if(_745.cancelable===undefined){
_745.cancelable=true;
}
if(!_745.detail){
_745.detail={};
}
_745.detail.widget=this;
var ret,_747=this["on"+type];
if(_747){
ret=_747.apply(this,_746?_746:[_745]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,type.toLowerCase(),_745);
}
return ret;
},on:function(type,func){
var _748=this._onMap(type);
if(_748){
return _70d.after(this,_748,func,true);
}
return this.own(on(this.domNode,type,func))[0];
},_onMap:function(type){
var ctor=this.constructor,map=ctor._onMap;
if(!map){
map=(ctor._onMap={});
for(var attr in ctor.prototype){
if(/^on/.test(attr)){
map[attr.replace(/^on/,"").toLowerCase()]=attr;
}
}
}
return map[typeof type=="string"&&type.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_71c.findWidgets(this.containerNode):[];
},getParent:function(){
return _71c.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_749,_74a){
return this.own(_70f.connect(obj,_749,this,_74a))[0];
},disconnect:function(_74b){
_74b.remove();
},subscribe:function(t,_74c){
return this.own(_719.subscribe(t,lang.hitch(this,_74c)))[0];
},unsubscribe:function(_74d){
_74d.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_714.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_715.get(this.domNode,"display")!="none");
},placeAt:function(_74e,_74f){
var _750=!_74e.tagName&&_71c.byId(_74e);
if(_750&&_750.addChild&&(!_74f||typeof _74f==="number")){
_750.addChild(this,_74f);
}else{
var ref=_750&&("domNode" in _750)?(_750.containerNode&&!/after|before|replace/.test(_74f||"")?_750.containerNode:_750.domNode):dom.byId(_74e,this.ownerDocument);
_713.place(this.domNode,ref,_74f);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,_751){
var _752=setTimeout(lang.hitch(this,function(){
if(!_752){
return;
}
_752=null;
if(!this._destroyed){
lang.hitch(this,fcn)();
}
}),_751||0);
return {remove:function(){
if(_752){
clearTimeout(_752);
_752=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_723.extend(_71b);
}
return _723;
});
},"dijit/layout/_TabContainerBase":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","./StackContainer","./utils","../_TemplatedMixin","dojo/text!./templates/TabContainer.html"],function(_753,_754,_755,_756,_757,_758,_759,_75a){
return _753("dijit.layout._TabContainerBase",[_757,_759],{tabPosition:"top",baseClass:"dijitTabContainer",tabStrip:false,nested:false,templateString:_75a,postMixInProperties:function(){
this.baseClass+=this.tabPosition.charAt(0).toUpperCase()+this.tabPosition.substr(1).replace(/-.*/,"");
this.srcNodeRef&&_756.set(this.srcNodeRef,"visibility","hidden");
this.inherited(arguments);
},buildRendering:function(){
this.inherited(arguments);
this.tablist=this._makeController(this.tablistNode);
if(!this.doLayout){
_754.add(this.domNode,"dijitTabContainerNoLayout");
}
if(this.nested){
_754.add(this.domNode,"dijitTabContainerNested");
_754.add(this.tablist.containerNode,"dijitTabContainerTabListNested");
_754.add(this.tablistSpacer,"dijitTabContainerSpacerNested");
_754.add(this.containerNode,"dijitTabPaneWrapperNested");
}else{
_754.add(this.domNode,"tabStrip-"+(this.tabStrip?"enabled":"disabled"));
}
},_setupChild:function(tab){
_754.add(tab.domNode,"dijitTabPane");
this.inherited(arguments);
},startup:function(){
if(this._started){
return;
}
this.tablist.startup();
this.inherited(arguments);
},layout:function(){
if(!this._contentBox||typeof (this._contentBox.l)=="undefined"){
return;
}
var sc=this.selectedChildWidget;
if(this.doLayout){
var _75b=this.tabPosition.replace(/-h/,"");
this.tablist.region=_75b;
var _75c=[this.tablist,{domNode:this.tablistSpacer,region:_75b},{domNode:this.containerNode,region:"center"}];
_758.layoutChildren(this.domNode,this._contentBox,_75c);
this._containerContentBox=_758.marginBox2contentBox(this.containerNode,_75c[2]);
if(sc&&sc.resize){
sc.resize(this._containerContentBox);
}
}else{
if(this.tablist.resize){
var s=this.tablist.domNode.style;
s.width="0";
var _75d=_755.getContentBox(this.domNode).w;
s.width="";
this.tablist.resize({w:_75d});
}
if(sc&&sc.resize){
sc.resize();
}
}
},destroy:function(_75e){
if(this.tablist){
this.tablist.destroy(_75e);
}
this.inherited(arguments);
}});
});
},"curam/widget/ComboBoxMixin":function(){
define(["dojo/_base/declare","curam/widget/_ComboBoxMenu","dijit/form/ComboBoxMixin"],function(_75f,_760){
var _761=_75f("curam.widget.ComboBoxMixin",dijit.form.ComboBoxMixin,{dropDownClass:_760});
return _761;
});
},"idx/main":function(){
define(["require","dojo/_base/lang","dojo/_base/kernel","dojo/_base/window","dojo/ready","dojo/has","dojo/sniff","dojo/dom-class"],function(_762,_763,_764,_765,_766,dHas,_767,_768){
var _769=_763.getObject("idx",true);
var _76a=_764.version.major;
var _76b=_764.version.minor;
var _76c=function(){
var _76d=_765.body();
var _76e="idx_dojo_"+_764.version.major+"_"+_764.version.minor;
_768.add(_76d,_76e);
var _76f=_764.locale.toLowerCase();
if((_76f.indexOf("he")==0)||(_76f.indexOf("_il")>=0)){
_768.add(_76d,"idx_i18n_il");
}
if(dHas("ie")){
_768.add(_76d,"dj_ie");
_768.add(_76d,"dj_ie"+dHas("ie"));
}else{
if(dHas("ff")){
_768.add(_76d,"dj_ff");
_768.add(_76d,"dj_ff"+dHas("ff"));
}else{
if(dHas("safari")){
_768.add(_76d,"dj_safari");
_768.add(_76d,"dj_safari"+dHas("safari"));
}else{
if(dHas("chrome")){
_768.add(_76d,"dj_chrome");
_768.add(_76d,"dj_chrome"+dHas("chrome"));
}else{
if(dHas("webkit")){
_768.add(_76d,"dj_webkit");
}
}
}
}
}
};
if((_76a<2)&&(_76b<7)){
dojo.addOnLoad(_76c);
}else{
_766(_76c);
}
return _769;
});
},"curam/util/Refresh":function(){
define(["dijit/registry","dojo/dom-class","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/ResourceBundle","curam/util","curam/tab","curam/util/ContextPanel","curam/util/ui/refresh/TabRefreshController"],function(_770,_771,_772,_773,_774,_775,_776,_777){
_775.singleton("curam.util.Refresh",{submitted:false,pageSubmitted:"",refreshConfig:[],menuBarCallback:null,navigationCallback:null,_controllers:{},_pageRefreshButton:undefined,setMenuBarCallbacks:function(_778,_779){
if(!curam.util.Refresh.menuBarCallback){
curam.util.Refresh.menuBarCallback={updateMenuItemStates:_778,getRefreshParams:_779};
}
},setNavigationCallbacks:function(_77a,_77b){
if(!curam.util.Refresh.navigationCallback){
curam.util.Refresh.navigationCallback={updateNavItemStates:_77a,getRefreshParams:_77b};
}
},refreshMenuAndNavigation:function(_77c,_77d,_77e){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"tabWidgetId=%s, refreshMenuBar || refreshNavigation: %s || %s",_77c,_77d,_77e);
if(!_77d&&!_77e){
curam.debug.log(_776.getProperty("curam.util.Refresh.no.refresh"));
return;
}
var _77f={update:function(_780,_781,_782){
curam.debug.log(_776.getProperty("curam.util.Refresh.dynamic.refresh"),_781);
var ncb=curam.util.Refresh.navigationCallback;
curam.debug.log("refreshNavigation? ",_77e);
if(_77e&&_781.navData&&ncb){
ncb.updateNavItemStates(_780,_781);
}
var mcb=curam.util.Refresh.menuBarCallback;
curam.debug.log("refreshMenuBar? ",_77d);
if(_77d&&_781.menuData&&mcb){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"dynamic tab menu item update");
mcb.updateMenuItemStates(_780,_781);
}else{
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"no dynamic data, updating initially loaded tab action items to show"+"only those that should be inlined");
curam.util.TabActionsMenu.manageInlineTabMenuStates(_780);
}
},error:function(_783,_784){
curam.debug.log("========= "+_776.getProperty("curam.util.Refresh.dynamic.failure")+" ===========");
curam.debug.log(_776.getProperty("curam.util.Refresh.dynamic.error"),_783);
curam.debug.log(_776.getProperty("curam.util.Refresh.dynamic.args"),_784);
curam.debug.log("==================================================");
}};
var _785="servlet/JSONServlet?o3c=TAB_DYNAMIC_STATE_QUERY",_786=false;
var mcb=curam.util.Refresh.menuBarCallback;
if(_77d&&mcb){
var _787=mcb.getRefreshParams(_77c);
if(_787){
_785+="&"+_787;
_786=true;
}
}
var ncb=curam.util.Refresh.navigationCallback;
if(_77e&&ncb){
var _788=ncb.getRefreshParams(_77c);
if(_788){
_785+="&"+_788;
_786=true;
}
}
curam.debug.log(_776.getProperty("curam.util.Refresh.dynamic.refresh.req"));
if(_786){
_774.post({url:_785,handleAs:"json",preventCache:true,load:dojo.hitch(_77f,"update",_77c),error:dojo.hitch(_77f,"error")});
}else{
curam.util.TabActionsMenu.manageInlineTabMenuStates(_77c);
curam.debug.log(_776.getProperty("curam.util.Refresh.dynamic.refresh.no_dynamic_items"));
}
},addConfig:function(_789){
var _78a=false;
dojo.forEach(curam.util.Refresh.refreshConfig,function(_78b){
if(_78b.tab==_789.tab){
_78b.config=_789.config;
_78a=true;
}
});
if(!_78a){
curam.util.Refresh.refreshConfig.push(_789);
}
},setupRefreshController:function(_78c){
curam.debug.log("curam.util.Refresh.setupRefreshController "+_776.getProperty("curam.util.ExpandableLists.load.for"),_78c);
var _78d=_770.byId(_78c);
var _78e=_78d.tabDescriptor.tabID;
var _78f=dojo.filter(curam.util.Refresh.refreshConfig,function(item){
return item.tab==_78e;
});
if(_78f.length==1){
var _790=_78f[0];
var ctl=new curam.util.ui.refresh.TabRefreshController(_78c,_790);
curam.util.Refresh._controllers[_78c]=ctl;
ctl.setRefreshHandler(curam.util.Refresh.handleRefreshEvent);
}else{
if(_78f.length==0){
curam.debug.log(_776.getProperty("curam.util.Refresh.no.dynamic.refresh"),_78c);
var ctl=new curam.util.ui.refresh.TabRefreshController(_78c,null);
curam.util.Refresh._controllers[_78c]=ctl;
}else{
throw "curam.util.Refresh: multiple dynamic refresh "+"configurations found for tab "+_78c;
}
}
curam.tab.executeOnTabClose(function(){
curam.util.Refresh._controllers[_78c].destroy();
curam.util.Refresh._controllers[_78c]=undefined;
},_78c);
},getController:function(_791){
var ctl=curam.util.Refresh._controllers[_791];
if(!ctl){
throw "Refresh controller for tab '"+_791+"' not found!";
}
return ctl;
},handleOnloadNestedInlinePage:function(_792,_793){
curam.debug.log("curam.util.Refresh.handleOnloadNestedInlinePage "+_776.getProperty("curam.util.Refresh.iframe",[_792,_793]));
var _794=curam.util.getTopmostWindow();
var _795=undefined;
var _796=curam.tab.getSelectedTab();
if(_796){
_795=curam.tab.getTabWidgetId(_796);
}
if(_795){
curam.debug.log(_776.getProperty("curam.util.Refresh.parent"),_795);
_794.curam.util.Refresh.getController(_795).pageLoaded(_793.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
_794.dojo.publish("/curam/main-content/page/loaded",[_793.pageID,_795,_796]);
return true;
}
return false;
},handleRefreshEvent:function(_797){
var _798=function(_799){
curam.util.ContextPanel.refresh(_770.byId(_799));
};
var _79a=function(_79b){
curam.tab.refreshMainContentPanel(_770.byId(_79b));
};
var _79c=function(_79d,_79e,_79f){
curam.util.Refresh.refreshMenuAndNavigation(_79d,_79e,_79f);
};
curam.util.Refresh._doRefresh(_797,_798,_79a,_79c);
},_doRefresh:function(_7a0,_7a1,_7a2,_7a3){
var _7a4=null;
var _7a5=false;
var _7a6=false;
var _7a7=false;
var _7a8=false;
var trc=curam.util.ui.refresh.TabRefreshController.prototype;
dojo.forEach(_7a0,function(_7a9){
var _7aa=_7a9.lastIndexOf("/");
var _7ab=_7a9.slice(0,_7aa);
if(!_7a4){
_7a4=_7a9.slice(_7aa+1,_7a9.length);
}
if(_7ab==trc.EVENT_REFRESH_MENU){
_7a5=true;
}
if(_7ab==trc.EVENT_REFRESH_NAVIGATION){
_7a6=true;
}
if(_7ab==trc.EVENT_REFRESH_CONTEXT){
_7a7=true;
}
if(_7ab==trc.EVENT_REFRESH_MAIN){
_7a8=true;
}
});
if(_7a7){
_7a1(_7a4);
}
if(_7a8){
_7a2(_7a4);
}
_7a3(_7a4,_7a5,_7a6);
},setupRefreshButton:function(_7ac){
dojo.ready(function(){
var _7ad=dojo.query("."+_7ac)[0];
if(!_7ad){
throw "Refresh button not found: "+_7ac;
}
curam.util.Refresh._pageRefreshButton=_7ad;
var href=window.location.href;
if(curam.util.isActionPage(href)){
var _7ae=new _777("Refresh");
var _7af=_7ae.getProperty("refresh.button.disabled");
_771.add(_7ad,"disabled");
_772.set(_7ad,"title",_7af);
_772.set(_7ad,"aria-label",_7af);
_772.set(_7ad,"role","link");
_772.remove(_7ad,"href");
_772.set(_7ad,"aria-disabled","true");
curam.util.Refresh._pageRefreshButton._curamDisable=true;
if(_7ad.firstChild!=null){
_771.add(_7ad.firstChild,"refresh-disabled");
_772.set(_7ad.firstChild,"alt",_7af);
}
}else{
_771.add(_7ad,"enabled");
curam.util.Refresh._pageRefreshButton["_curamDisable"]=undefined;
}
curam.util.getTopmostWindow().curam.util.setupPreferencesLink(href);
});
},refreshPage:function(_7b0){
dojo.stopEvent(_7b0);
var href=window.location.href;
var _7b1=curam.util.Refresh._pageRefreshButton._curamDisable;
if(_7b1){
return;
}
curam.util.FORCE_REFRESH=true;
curam.util.redirectWindow(href,true);
}});
_773.register("curam/util/Refresh",curam.util.Refresh);
return curam.util.Refresh;
});
},"curam/util/ContextPanel":function(){
define(["dijit/registry","dojo/dom-attr","curam/inspection/Layer","curam/debug","curam/util/onLoad","curam/util","curam/tab","curam/define"],function(_7b2,_7b3,_7b4,_7b5,_7b6){
curam.define.singleton("curam.util.ContextPanel",{CONTENT_URL_ATTRIB:"data-content-url",setupLoadEventPublisher:function(_7b7,_7b8,_7b9){
curam.util.ContextPanel._doSetup(_7b7,_7b8,_7b9,function(_7ba){
return _7b2.byId(_7ba);
});
},_doSetup:function(_7bb,_7bc,_7bd,_7be){
var _7bf=curam.util.getTopmostWindow().dojo.subscribe(_7bb,function(){
var tab=_7be(_7bc);
var _7c0=curam.util.ContextPanel._getIframe(tab);
if(_7c0){
curam.tab.executeOnTabClose(function(){
var src=_7b3.get(_7c0,"src");
_7b3.set(_7c0,"src","");
curam.debug.log("curam.util.ContextPanel: Released iframe content for "+src);
},_7bc);
_7b5.log(_7b5.getProperty("curam.util.ContextPanel.loaded"));
curam.util.getTopmostWindow().dojo.publish("/curam/frame/detailsPanelLoaded",[{loaded:true},_7bc]);
_7c0._finishedLoading=true;
if(_7c0._scheduledRefresh){
curam.util.ContextPanel.refresh(tab);
_7c0._scheduledRefresh=false;
}
}
});
_7b6.addSubscriber(_7bd,curam.util.ContextPanel.addTitle);
curam.tab.unsubscribeOnTabClose(_7bf,_7bc);
curam.tab.executeOnTabClose(function(){
_7b6.removeSubscriber(_7bd,curam.util.ContextPanel.addTitle);
},_7bc);
},refresh:function(tab){
var _7c1=curam.util.ContextPanel._getIframe(tab);
if(_7c1){
curam.debug.log(_7b5.getProperty("curam.util.ContextPanel.refresh.prep"));
if(_7c1._finishedLoading){
curam.debug.log(_7b5.getProperty("curam.util.ContextPanel.refresh"));
_7c1._finishedLoading=false;
var doc=_7c1.contentDocument||_7c1.contentWindow.document;
doc.location.reload(false);
}else{
curam.debug.log(_7b5.getProperty("curam.util.ContextPanel.refresh.delay"));
_7c1._scheduledRefresh=true;
}
}
},_getIframe:function(tab){
if(tab){
var _7c2=dojo.query("iframe.detailsPanelFrame",tab.domNode);
return _7c2[0];
}
},addTitle:function(_7c3){
var _7c4=dojo.query("."+_7c3)[0];
var _7c5=_7c4.contentWindow.document.title;
_7c4.setAttribute("title",CONTEXT_PANEL_TITLE+" - "+_7c5);
},load:function(tab){
var _7c6=curam.util.ContextPanel._getIframe(tab);
if(_7c6){
var _7c7=_7b3.get(_7c6,curam.util.ContextPanel.CONTENT_URL_ATTRIB);
if(_7c7&&_7c7!="undefined"){
_7c6[curam.util.ContextPanel.CONTENT_URL_ATTRIB]=undefined;
_7b3.set(_7c6,"src",_7c7);
}
}
}});
var _7c8=curam.util.getTopmostWindow();
if(typeof _7c8._curamContextPanelTabReadyListenerRegistered!="boolean"){
_7c8.dojo.subscribe("/curam/application/tab/ready",null,function(_7c9){
curam.util.ContextPanel.load(_7c9);
});
_7c8._curamContextPanelTabReadyListenerRegistered=true;
}
_7b4.register("curam/util/ContextPanel",this);
return curam.util.ContextPanel;
});
},"curam/util":function(){
define(["dojo/dom","dijit/registry","dojo/dom-construct","dojo/ready","dojo/_base/window","dojo/dom-style","dojo/_base/array","dojo/dom-class","dojo/topic","dojo/_base/event","dojo/query","dojo/Deferred","dojo/has","dojo/_base/unload","dojo/dom-geometry","dojo/_base/json","dojo/dom-attr","dojo/_base/lang","dojo/on","dijit/_BidiSupport","curam/define","curam/debug","curam/util/RuntimeContext","curam/util/Constants","dojo/_base/sniff","cm/_base/_dom","curam/util/ResourceBundle","dojo/NodeList-traverse"],function(dom,_7ca,_7cb,_7cc,_7cd,_7ce,_7cf,_7d0,_7d1,_7d2,_7d3,_7d4,has,_7d5,geom,json,attr,lang,on,bidi,_7d6,_7d7,_7d8,_7d9,_7da,_7db,_7dc){
curam.define.singleton("curam.util",{PREVENT_CACHE_FLAG:"o3pc",INFORMATIONAL_MSGS_STORAGE_ID:"__informationals__",ERROR_MESSAGES_CONTAINER:"error-messages-container",ERROR_MESSAGES_LIST:"error-messages",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",PAGE_ID_PREFIX:"Curam_",msgLocaleSelectorActionPage:"$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",GENERIC_ERROR_MODAL_MAP:{},wrappersMap:[],lastOpenedTabButton:false,tabButtonClicked:false,secureURLsExemptParamName:"suep",secureURLsExemptParamsPrefix:"spm",secureURLsHashTokenParam:"suhtp",setTabButtonClicked:function(_7dd){
curam.util.getTopmostWindow().curam.util.tabButtonClicked=_7dd;
},getTabButtonClicked:function(){
var _7de=curam.util.getTopmostWindow().curam.util.tabButtonClicked;
curam.util.getTopmostWindow().curam.util.tabButtonClicked=false;
return _7de;
},setLastOpenedTabButton:function(_7df){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=_7df;
},getLastOpenedTabButton:function(){
var _7e0=curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=false;
return _7e0;
},insertCssText:function(_7e1,_7e2){
var id=_7e2?_7e2:"_runtime_stylesheet_";
var _7e3=dom.byId(id);
var _7e4;
if(_7e3){
if(_7e3.styleSheet){
_7e1=_7e3.styleSheet.cssText+_7e1;
_7e4=_7e3;
_7e4.setAttribute("id","_nodeToRm");
}else{
_7e3.appendChild(document.createTextNode(_7e1));
return;
}
}
var pa=document.getElementsByTagName("head")[0];
_7e3=_7cb.create("style",{type:"text/css",id:id});
if(_7e3.styleSheet){
_7e3.styleSheet.cssText=_7e1;
}else{
_7e3.appendChild(document.createTextNode(_7e1));
}
pa.appendChild(_7e3);
if(_7e4){
_7e4.parentNode.removeChild(_7e4);
}
},fireRefreshTreeEvent:function(){
if(dojo.global.parent&&dojo.global.parent.amIFrame){
var wpl=dojo.global.parent.loader;
}
if(wpl&&wpl.dojo){
wpl.dojo.publish("refreshTree");
}
},firePageSubmittedEvent:function(_7e5){
require(["curam/tab"],function(){
var _7e6=curam.tab.getSelectedTab();
if(_7e6){
var _7e7=curam.tab.getTabWidgetId(_7e6);
var _7e8=curam.util.getTopmostWindow();
var ctx=(_7e5=="dialog")?curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG:curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
_7e8.curam.util.Refresh.getController(_7e7).pageSubmitted(dojo.global.jsPageID,ctx);
_7e8.dojo.publish("/curam/main-content/page/submitted",[dojo.global.jsPageID,_7e7]);
}else{
curam.debug.log("/curam/main-content/page/submitted: "+_7d7.getProperty("curam.util.no.open"));
}
});
},fireTabOpenedEvent:function(_7e9){
curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",[dojo.global.jsPageID,_7e9]);
},setupSubmitEventPublisher:function(){
_7cc(function(){
var form=dom.byId("mainForm");
if(form){
curam.util.connect(form,"onsubmit",function(){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[curam.util.PAGE_ID_PREFIX+dojo.global.jsPageID]);
curam.util.firePageSubmittedEvent("main-content");
});
}
});
},getScrollbar:function(){
var _7ea=_7cb.create("div",{},_7cd.body());
_7ce.set(_7ea,{width:"100px",height:"100px",overflow:"scroll",position:"absolute",top:"-300px",left:"0px"});
var test=_7cb.create("div",{},_7ea);
_7ce.set(test,{width:"400px",height:"400px"});
var _7eb=_7ea.offsetWidth-_7ea.clientWidth;
_7cb.destroy(_7ea);
return {width:_7eb};
},isModalWindow:function(){
return (dojo.global.curamModal===undefined)?false:true;
},isExitingIEGScriptInModalWindow:function(_7ec){
return (curam.util.getTopmostWindow().exitingIEGScript===undefined)?false:true;
},setExitingIEGScriptInModalWindowVariable:function(){
curam.util.getTopmostWindow().exitingIEGScript=true;
},getTopmostWindow:function(){
if(typeof (dojo.global._curamTopmostWindow)=="undefined"){
var _7ed=dojo.global;
if(_7ed.__extAppTopWin){
dojo.global._curamTopmostWindow=_7ed;
}else{
while(_7ed.parent!=_7ed){
_7ed=_7ed.parent;
if(_7ed.__extAppTopWin){
break;
}
}
dojo.global._curamTopmostWindow=_7ed;
}
}
if(dojo.global._curamTopmostWindow.location.href.indexOf("AppController.do")<0&&typeof (dojo.global._curamTopmostWindow.__extAppTopWin)=="undefined"){
curam.debug.log(_7d7.getProperty("curam.util.wrong.window")+dojo.global._curamTopmostWindow.location.href);
}
return dojo.global._curamTopmostWindow;
},getUrlParamValue:function(url,_7ee){
var qPos=url.indexOf("?");
if(qPos<0){
return null;
}
var _7ef=url.substring(qPos+1,url.length);
function _7f0(_7f1){
var _7f2=_7ef.split(_7f1);
_7ee+="=";
for(var i=0;i<_7f2.length;i++){
if(_7f2[i].indexOf(_7ee)==0){
return _7f2[i].split("=")[1];
}
}
};
return _7f0("&")||_7f0("");
},addUrlParam:function(href,_7f3,_7f4,_7f5){
var hasQ=href.indexOf("?")>-1;
var _7f6=_7f5?_7f5:"undefined";
if(!hasQ||(_7f6==false)){
return href+(hasQ?"&":"?")+_7f3+"="+_7f4;
}else{
var _7f7=href.split("?");
href=_7f7[0]+"?"+_7f3+"="+_7f4+(_7f7[1]!=""?("&"+_7f7[1]):"");
return href;
}
},replaceUrlParam:function(href,_7f8,_7f9){
href=curam.util.removeUrlParam(href,_7f8);
return curam.util.addUrlParam(href,_7f8,_7f9);
},removeUrlParam:function(url,_7fa,_7fb){
var qPos=url.indexOf("?");
if(qPos<0){
return url;
}
if(url.indexOf(_7fa+"=")<0){
return url;
}
var _7fc=url.substring(qPos+1,url.length);
var _7fd=_7fc.split("&");
var _7fe;
var _7ff,_800;
for(var i=0;i<_7fd.length;i++){
if(_7fd[i].indexOf(_7fa+"=")==0){
_800=false;
if(_7fb){
_7ff=_7fd[i].split("=");
if(_7ff.length>1){
if(_7ff[1]==_7fb){
_800=true;
}
}else{
if(_7fb==""){
_800=true;
}
}
}else{
_800=true;
}
if(_800){
_7fd.splice(i,1);
i--;
}
}
}
return url.substring(0,qPos+1)+_7fd.join("&");
},stripHash:function(url){
var idx=url.indexOf("#");
if(idx<0){
return url;
}
return url.substring(0,url);
},isSameUrl:function(_801,_802,rtc){
if(!_802){
_802=rtc.getHref();
}
if(_801.indexOf("#")==0){
return true;
}
var _803=_801.indexOf("#");
if(_803>-1){
if(_803==0){
return true;
}
var _804=_801.split("#");
var _805=_802.indexOf("#");
if(_805>-1){
if(_805==0){
return true;
}
_802=_802.split("#")[0];
}
return _804[0]==_802;
}
var _806=function(url){
var idx=url.lastIndexOf("Page.do");
var len=7;
if(idx<0){
idx=url.lastIndexOf("Action.do");
len=9;
}
if(idx<0){
idx=url.lastIndexOf("Frame.do");
len=8;
}
if(idx>-1&&idx==url.length-len){
return url.substring(0,idx);
}
return url;
};
var rp=curam.util.removeUrlParam;
var here=curam.util.stripHash(rp(_802,curam.util.Constants.RETURN_PAGE_PARAM));
var _807=curam.util.stripHash(rp(_801,curam.util.Constants.RETURN_PAGE_PARAM));
var _808=_807.split("?");
var _809=here.split("?");
_809[0]=_806(_809[0]);
_808[0]=_806(_808[0]);
var _80a=(_809[0]==_808[0]||_809[0].match(_808[0]+"$")==_808[0]);
if(!_80a){
return false;
}
if(_809.length==1&&_808.length==1&&_80a){
return true;
}else{
var _80b;
var _80c;
if(typeof _809[1]!="undefined"&&_809[1]!=""){
_80b=_809[1].split("&");
}else{
_80b=new Array();
}
if(typeof _808[1]!="undefined"&&_808[1]!=""){
_80c=_808[1].split("&");
}else{
_80c=new Array();
}
curam.debug.log("curam.util.isSameUrl: paramsHere "+_7d7.getProperty("curam.util.before")+_80b.length);
_80b=_7cf.filter(_80b,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_7d7.getProperty("curam.util.after")+_80b.length);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_7d7.getProperty("curam.util.before")+_80c.length);
_80c=_7cf.filter(_80c,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_7d7.getProperty("curam.util.after")+_80c.length);
if(_80b.length!=_80c.length){
return false;
}
var _80d={};
var _80e;
for(var i=0;i<_80b.length;i++){
_80e=_80b[i].split("=");
_80e[0]=decodeURIComponent(_80e[0]);
_80e[1]=decodeURIComponent(_80e[1]);
_80d[_80e[0]]=_80e[1];
}
for(var i=0;i<_80c.length;i++){
_80e=_80c[i].split("=");
_80e[0]=decodeURIComponent(_80e[0]);
_80e[1]=decodeURIComponent(_80e[1]);
if(_80d[_80e[0]]!=_80e[1]){
curam.debug.log(_7d7.getProperty("curam.util.no.match",[_80e[0],_80e[1],_80d[_80e[0]]]));
return false;
}
}
}
return true;
},isNotCDEJParam:function(_80f){
return !((_80f.charAt(0)=="o"&&_80f.charAt(1)=="3")||(_80f.charAt(0)=="_"&&_80f.charAt(1)=="_"&&_80f.charAt(2)=="o"&&_80f.charAt(3)=="3"));
},setAttributes:function(node,map){
for(var x in map){
node.setAttribute(x,map[x]);
}
},invalidatePage:function(){
curam.PAGE_INVALIDATED=true;
var _810=dojo.global.dialogArguments?dojo.global.dialogArguments[0]:opener;
if(_810&&_810!=dojo.global){
try{
_810.curam.util.invalidatePage();
}
catch(e){
curam.debug.log(_7d7.getProperty("curam.util.error"),e);
}
}
},redirectWindow:function(href,_811,_812){
var rtc=new curam.util.RuntimeContext(dojo.global);
var _813=function(_814,_815,href,_816,_817){
curam.util.getFrameRoot(_814,_815).curam.util.redirectContentPanel(href,_816,_817);
};
curam.util._doRedirectWindow(href,_811,_812,dojo.global.jsScreenContext,rtc,curam.util.publishRefreshEvent,_813);
},_doRedirectWindow:function(href,_818,_819,_81a,rtc,_81b,_81c){
if(href&&curam.util.isActionPage(href)&&!curam.util.LOCALE_REFRESH){
curam.debug.log(_7d7.getProperty("curam.util.stopping"),href);
return;
}
var rpl=curam.util.replaceUrlParam;
var _81d=_81a.hasContextBits("TREE")||_81a.hasContextBits("AGENDA")||_81a.hasContextBits("ORG_TREE");
if(curam.util.LOCALE_REFRESH){
curam.util.publishRefreshEvent();
curam.util.getTopmostWindow().location.reload();
return;
}else{
if(curam.util.FORCE_REFRESH){
href=rpl(rtc.getHref(),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(curam.util.isModalWindow()||_81d){
_81b();
dojo.global.location.href=href;
}else{
if(_81a.hasContextBits("LIST_ROW_INLINE_PAGE")||_81a.hasContextBits("NESTED_UIM")){
curam.util._handleInlinePageRefresh(href);
}else{
_81b();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
require(["curam/tab"],function(){
_81c(dojo.global,curam.tab.getTabController().ROOT_OBJ,href,true,true);
});
}
}
}
return;
}
}
var u=curam.util;
var rtc=new curam.util.RuntimeContext(dojo.global);
if(!_81d&&!_818&&!curam.PAGE_INVALIDATED&&u.isSameUrl(href,null,rtc)){
return;
}
if(curam.util.isModalWindow()||_81d){
href=rpl(rpl(href,"o3frame","modal"),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
var form=_7cb.create("form",{action:href,method:"POST"});
if(!_81d){
if(!dom.byId("o3ctx")){
form.action=curam.util.removeUrlParam(form.action,"o3ctx");
var _81e=_7cb.create("input",{type:"hidden",id:"o3ctx",name:"o3ctx",value:_81a.getValue()},form);
}
_7cd.body().appendChild(form);
_81b();
form.submit();
}
if(!_819){
if(_81d){
curam.util.redirectFrame(href);
}
}
}else{
var _81f=sessionStorage.getItem("launchWordEdit");
if(!_81f&&(_81a.hasContextBits("LIST_ROW_INLINE_PAGE")||_81a.hasContextBits("NESTED_UIM"))){
curam.util._handleInlinePageRefresh(href);
}else{
if(_81f){
sessionStorage.removeItem("launchWordEdit");
}
_81b();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
if(_81a.hasContextBits("EXTAPP")){
var _820=window.top;
_820.dijit.byId("curam-app").updateMainContentIframe(href);
}else{
require(["curam/tab"],function(){
curam.util.getFrameRoot(dojo.global,curam.tab.getTabController().ROOT_OBJ).curam.util.redirectContentPanel(href,_818);
});
}
}
}
}
},_handleInlinePageRefresh:function(href){
curam.debug.log(_7d7.getProperty("curam.util.closing.modal"),href);
var _821=new curam.ui.PageRequest(href);
require(["curam/tab"],function(){
curam.tab.getTabController().checkPage(_821,function(_822){
curam.util.publishRefreshEvent();
window.location.reload(false);
});
});
},redirectContentPanel:function(url,_823,_824){
require(["curam/tab"],function(){
var _825=curam.tab.getContentPanelIframe();
var _826=url;
if(_825!=null){
var rpu=curam.util.Constants.RETURN_PAGE_PARAM;
var _827=null;
if(url.indexOf(rpu+"=")>=0){
curam.debug.log("curam.util.redirectContentPanel: "+_7d7.getProperty("curam.util.rpu"));
_827=decodeURIComponent(curam.util.getUrlParamValue(url,rpu));
}
if(_827){
_827=curam.util.removeUrlParam(_827,rpu);
_826=curam.util.replaceUrlParam(url,rpu,encodeURIComponent(_827));
}
}
var _828=new curam.ui.PageRequest(_826);
if(_823){
_828.forceLoad=true;
}
if(_824){
_828.justRefresh=true;
}
curam.tab.getTabController().handlePageRequest(_828);
});
},redirectFrame:function(href){
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
var _829=curam.util.getFrameRoot(dojo.global,"wizard").targetframe;
_829.curam.util.publishRefreshEvent();
_829.location.href=href;
}else{
if(dojo.global.jsScreenContext.hasContextBits("ORG_TREE")){
var _829=curam.util.getFrameRoot(dojo.global,"orgTreeRoot");
_829.curam.util.publishRefreshEvent();
_829.dojo.publish("orgTree.refreshContent",[href]);
}else{
var _82a=curam.util.getFrameRoot(dojo.global,"iegtree");
var _82b=_82a.navframe||_82a.frames[0];
var _82c=_82a.contentframe||_82a.frames["contentframe"];
_82c.curam.util.publishRefreshEvent();
if(curam.PAGE_INVALIDATED||_82b.curam.PAGE_INVALIDATED){
var _82d=curam.util.modifyUrlContext(href,"ACTION");
_82c.location.href=_82d;
}else{
_82c.location.href=href;
}
}
}
return true;
},publishRefreshEvent:function(){
_7d1.publish("/curam/page/refresh");
},openGenericErrorModalDialog:function(_82e,_82f,_830,_831,_832){
var _833=curam.util.getTopmostWindow();
_833.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_82e,"titleInfo":_82f,"msg":_830,"msgPlaceholder":_831,"errorModal":_832,"hasCancelButton":false};
var url="generic-modal-error.jspx";
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=true;
curam.util.openModalDialog({href:encodeURI(url)},_82e);
},openGenericErrorModalDialogYesNo:function(_834,_835,_836){
var sc=dojo.global.jsScreenContext;
var _837=curam.util.getTopmostWindow();
sc.addContextBits("MODAL");
_837.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_834,"titleInfo":_835,"msg":_836,"msgPlaceholder":"","errorModal":false,"hasCancelButton":true};
var url="generic-modal-error.jspx?"+sc.toRequestString();
curam.util.openModalDialog({href:encodeURI(url)},_834);
},addPlaceholderFocusClassToEventOrAnchorTag:function(_838,_839){
var _83a=curam.util.getTopmostWindow();
_83a.curam.util.PLACEHOLDER_WINDOW_LIST=!_83a.curam.util.PLACEHOLDER_WINDOW_LIST?[]:_83a.curam.util.PLACEHOLDER_WINDOW_LIST;
_7d0.add(_838,"placeholder-for-focus");
_83a.curam.util.PLACEHOLDER_WINDOW_LIST.push(_839);
},returnFocusToPopupActionAnchorElement:function(_83b){
var _83c=_83b.curam.util.PLACEHOLDER_WINDOW_LIST;
if(_83c&&_83c.length>0){
var _83d=_83c.pop();
var _83e=_83d&&_83d.document.activeElement;
var _83f=_83e&&dojo.query(".placeholder-for-focus",_83e);
if(_83f&&_83f.length==1){
_83f[0].focus();
_7d0.remove(_83f[0],"placeholder-for-focus");
}
_83b.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0,_83b.curam.util.PLACEHOLDER_WINDOW_LIST.length);
_83b.curam.util.PLACEHOLDER_WINDOW_LIST=null;
}
},openModalDialog:function(_840,_841,left,top,_842){
_840.event&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_840.event,_840.event.ownerDocument.defaultView.window);
var href;
if(!_840||!_840.href){
_840=_7d2.fix(_840);
var _843=_840.target;
while(_843.tagName!="A"&&_843!=_7cd.body()){
_843=_843.parentNode;
}
href=_843.href;
_843._isModal=true;
_7d2.stop(_840);
}else{
href=_840.href;
_840._isModal=true;
}
require(["curam/dialog"]);
var opts=curam.dialog.parseWindowOptions(_841);
curam.util.showModalDialog(href,_840,opts["width"],opts["height"],left,top,false,null,null,_842);
return true;
},showModalDialog:function(url,_844,_845,_846,left,top,_847,_848,_849,_84a){
var _84b=curam.util.getTopmostWindow();
if(dojo.global!=_84b){
curam.debug.log("curam.util.showModalDialog: "+_7d7.getProperty("curam.util.redirecting.modal"));
_84b.curam.util.showModalDialog(url,_844,_845,_846,left,top,_847,_848,dojo.global,_84a);
return;
}
var rup=curam.util.replaceUrlParam;
url=rup(url,"o3frame","modal");
url=curam.util.modifyUrlContext(url,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
url=rup(url,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
curam.debug.log(_7d7.getProperty("curam.util.modal.url"),url);
if(_845){
_845=typeof (_845)=="number"?_845:parseInt(_845);
}
if(_846){
_846=typeof (_846)=="number"?_846:parseInt(_846);
}
if(!curam.util._isModalCurrentlyOpening()){
if(url.indexOf("__o3rpu=about%3Ablank")>=0){
alert(curam_util_showModalDialog_pageStillLoading);
return;
}
curam.util._setModalCurrentlyOpening(true);
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
new curam.ModalDialog({href:url,width:_845,height:_846,openNode:(_844&&_844.target)?_844.target:null,parentWindow:_849,uimToken:_84a});
}else{
require(["curam/modal/CuramCarbonModal"]);
new curam.modal.CuramCarbonModal({href:url,width:_845,height:_846,openNode:(_844&&_844.target)?_844.target:null,parentWindow:_849,uimToken:_84a});
}
return true;
}
},showModalDialogWithRef:function(_84c,_84d,_84e){
var _84f=curam.util.getTopmostWindow();
if(dojo.global!=_84f){
return _84f.curam.util.showModalDialogWithRef(_84c,dojo.global);
}
var rup=curam.util.replaceUrlParam;
_84c=curam.util.modifyUrlContext(_84c,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
_84c=rup(_84c,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(!curam.util._isModalCurrentlyOpening()){
curam.util._setModalCurrentlyOpening(true);
var dl;
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
if(_84e){
dl=new curam.ModalDialog({href:_84c,width:_84e.width,height:_84e.height,parentWindow:_84d});
}else{
dl=new curam.ModalDialog({href:_84c,parentWindow:_84d});
}
}else{
require(["curam/modal/CuramCarbonModal"]);
if(_84e){
dl=new curam.modal.CuramCarbonModal({href:_84c,width:_84e.width,height:_84e.height,parentWindow:_84d});
}else{
dl=new curam.modal.CuramCarbonModal({href:_84c,parentWindow:_84d});
}
}
return dl;
}
},_isModalCurrentlyOpening:function(){
return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
},_setModalCurrentlyOpening:function(_850){
curam.util.getTopmostWindow().curam.util._modalOpenInProgress=_850;
},setupPreferencesLink:function(href){
_7cc(function(){
var _851=_7d3(".user-preferences")[0];
if(_851){
if(typeof (_851._disconnectToken)=="undefined"){
_851._disconnectToken=curam.util.connect(_851,"onclick",curam.util.openPreferences);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_7d7.getProperty("curam.util.no.setup"));
}
});
},setupLocaleLink:function(href){
_7cc(function(){
var _852=_7d3(".user-locale")[0];
if(_852){
if(typeof (_852._disconnectToken)=="undefined"){
_852._disconnectToken=curam.util.connect(_852,"onclick",curam.util.openLocaleNew);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_7d7.getProperty("curam.util.no.setup"));
}
});
},openPreferences:function(_853){
_7d2.stop(_853);
if(_853.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-prefs-editor.jspx",{dialogOptions:"width=605"});
});
},logout:function(_854){
var _855=curam.util.getTopmostWindow();
_855.dojo.publish("curam/redirect/logout");
window.location.href=jsBaseURL+"/logout.jsp";
},openAbout:function(_856){
_7d2.stop(_856);
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("about.jsp",{dialogOptions:"width=583,height=399"});
});
},addMinWidthCalendarCluster:function(id){
var _857=dom.byId(id);
var i=0;
function _858(evt){
_7cf.forEach(_857.childNodes,function(node){
if(_7d0.contains(node,"cluster")){
_7ce.set(node,"width","97%");
if(node.clientWidth<700){
_7ce.set(node,"width","700px");
}
}
});
};
if(has("ie")>6){
_7cf.forEach(_857.childNodes,function(node){
if(_7d0.contains(node,"cluster")){
_7ce.set(node,"minWidth","700px");
}
});
}else{
on(dojo.global,"resize",_858);
_7cc(_858);
}
},addPopupFieldListener:function(id){
if(!has("ie")||has("ie")>6){
return;
}
if(!curam.util._popupFields){
function _859(evt){
var _85a=0;
var j=0;
var x=0;
var arr=curam.util._popupFields;
_7cf.forEach(curam.util._popupFields,function(id){
var _85b=dom.byId(id);
_7d3("> .popup-actions",_85b).forEach(function(node){
_85a=node.clientWidth+30;
});
_7d3("> .desc",_85b).forEach(function(node){
_7ce.set(node,"width",Math.max(0,_85b.clientWidth-_85a)+"px");
});
});
};
curam.util._popupFields=[id];
on(dojo.global,"resize",_859);
_7cc(_859);
}else{
curam.util._popupFields.push(id);
}
},addContentWidthListener:function(id){
if(has("ie")>6){
return;
}
var _85c=_7ce.set;
var _85d=_7d0.contains;
function _85e(evt){
var i=0;
var _85f=dom.byId("content");
if(_85f){
var _860=_85f.clientWidth;
if(has("ie")==6&&dom.byId("footer")){
var _861=_7cd.body().clientHeight-100;
_85c(_85f,"height",_861+"px");
var _862=dom.byId("sidebar");
if(_862){
_85c(_862,"height",_861+"px");
}
}
try{
_7d3("> .page-title-bar",_85f).forEach(function(node){
var _863=geom.getMarginSize(node).w-geom.getContentBox(node).w;
if(!has("ie")){
_863+=1;
}
_860=_85f.clientWidth-_863;
_7ce.set(node,"width",_860+"px");
});
}
catch(e){
}
_7d3("> .page-description",_85f).style("width",_860+"px");
_7d3("> .in-page-navigation",_85f).style("width",_860+"px");
}
};
curam.util.subscribe("/clusterToggle",_85e);
curam.util.connect(dojo.global,"onresize",_85e);
_7cc(_85e);
},alterScrollableListBottomBorder:function(id,_864){
var _865=_864;
var _866="#"+id+" table";
function _867(){
var _868=_7d3(_866)[0];
if(_868.offsetHeight>=_865){
var _869=_7d3(".odd-last-row",_868)[0];
if(typeof _869!="undefined"){
_7d0.add(_869,"no-bottom-border");
}
}else{
if(_868.offsetHeight<_865){
var _869=_7d3(".even-last-row",_868)[0];
if(typeof _869!="undefined"){
_7d0.add(_869,"add-bottom-border");
}
}else{
curam.debug.log("curam.util.alterScrollableListBottomBorder: "+_7d7.getProperty("curam.util.code"));
}
}
};
_7cc(_867);
},addFileUploadResizeListener:function(code){
function _86a(evt){
if(_7d3(".widget")){
_7d3(".widget").forEach(function(_86b){
var _86c=_86b.clientWidth;
if(_7d3(".fileUpload",_86b)){
_7d3(".fileUpload",_86b).forEach(function(_86d){
fileUploadWidth=_86c/30;
if(fileUploadWidth<4){
_86d.size=1;
}else{
_86d.size=fileUploadWidth;
}
});
}
});
}
};
on(dojo.global,"resize",_86a);
_7cc(_86a);
},openCenteredNonModalWindow:function(url,_86e,_86f,name){
_86e=Number(_86e);
_86f=Number(_86f);
var _870=(screen.width-_86e)/2;
var _871=(screen.height-_86f)/2;
_86f=_871<0?screen.height:_86f;
_871=Math.max(0,_871);
_86e=_870<0?screen.width:_86e;
_870=Math.max(0,_870);
var left="left",top="top";
if(has("ff")){
left="screenX",top="screenY";
}
var _872="location=no, menubar=no, status=no, toolbar=no, "+"scrollbars=yes, resizable=no";
var _873=dojo.global.open(url,name||"name","width="+_86e+", height="+_86f+", "+left+"="+_870+","+top+"="+_871+","+_872);
_873.resizeTo(_86e,_86f);
_873.moveTo(_870,_871);
_873.focus();
},adjustTargetContext:function(win,href){
if(win&&win.dojo.global.jsScreenContext){
var _874=win.dojo.global.jsScreenContext;
_874.updateStates(dojo.global.jsScreenContext);
return curam.util.replaceUrlParam(href,"o3ctx",_874.getValue());
}
return href;
},modifyUrlContext:function(url,_875,_876){
var _877=url;
var ctx=new curam.util.ScreenContext();
var _878=curam.util.getUrlParamValue(url,"o3ctx");
if(_878){
ctx.setContext(_878);
}else{
ctx.clear();
}
if(_875){
ctx.addContextBits(_875);
}
if(_876){
ctx.clear(_876);
}
_877=curam.util.replaceUrlParam(url,"o3ctx",ctx.getValue());
return _877;
},updateCtx:function(_879){
var _87a=curam.util.getUrlParamValue(_879,"o3ctx");
if(!_87a){
return _879;
}
return curam.util.modifyUrlContext(_879,null,"MODAL");
},getFrameRoot:function(_87b,_87c){
var _87d=false;
var _87e=_87b;
if(_87e){
while(_87e!=top&&!_87e.rootObject){
_87e=_87e.parent;
}
if(_87e.rootObject){
_87d=(_87e.rootObject==_87c);
}
}
return _87d?_87e:null;
},saveInformationalMsgs:function(_87f){
try{
localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID]=json.toJson({pageID:_7cd.body().id,total:dom.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,listItems:dom.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML});
_87f();
}
catch(e){
curam.debug.log(_7d7.getProperty("curam.util.exception"),e);
}
},disableInformationalLoad:function(){
curam.util._informationalsDisabled=true;
},redirectDirectUrl:function(){
_7cc(function(){
if(dojo.global.parent==dojo.global){
var url=document.location.href;
var idx=url.lastIndexOf("/");
if(idx>-1){
if(idx<=url.length){
url=url.substring(idx+1);
}
}
dojo.global.location=jsBaseURL+"/AppController.do?o3gtu="+encodeURIComponent(url);
}
});
},loadInformationalMsgs:function(){
_7cc(function(){
if(dojo.global.jsScreenContext.hasContextBits("CONTEXT_PANEL")){
return;
}
if(curam.util._informationalsDisabled){
return;
}
var msgs=localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID];
if(msgs&&msgs!=""){
msgs=json.fromJson(msgs);
localStorage.removeItem(curam.util.INFORMATIONAL_MSGS_STORAGE_ID);
var div=dom.byId(curam.util.ERROR_MESSAGES_CONTAINER);
var list=dom.byId(curam.util.ERROR_MESSAGES_LIST);
if(msgs.pageID!=_7cd.body().id){
return;
}
if(list){
var _880=_7cb.create("ul",{innerHTML:msgs.listItems});
var _881=[];
for(var i=0;i<list.childNodes.length;i++){
if(list.childNodes[i].tagName=="LI"){
_881.push(list.childNodes[i]);
}
}
var skip=false;
var _882=_880.childNodes;
for(var i=0;i<_882.length;i++){
skip=false;
for(var j=0;j<_881.length;j++){
if(_882[i].innerHTML==_881[j].innerHTML){
skip=true;
break;
}
}
if(!skip){
list.appendChild(_882[i]);
i--;
}
}
}else{
if(div){
div.innerHTML=msgs.total;
}
}
}
var _883=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_883&&!dojo.global.jsScreenContext.hasContextBits("MODAL")){
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked){
curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
setTimeout(function(){
_883.innerHTML=_883.innerHTML+" ";
},500);
}else{
_883.focus();
}
}
var _884=dom.byId("error-messages-container-wrapper");
if(_884){
var _885=_7d3("#container-messages-ul",_884)[0];
if(_885){
_885.focus();
}
}
});
},_setFocusCurrentIframe:function(){
var _886=/Edg/.test(navigator.userAgent);
if(_886){
var _887=window.frameElement;
if(_887){
_887.setAttribute("tabindex","0");
_887.focus();
setTimeout(function(){
_887.removeAttribute("tabindex");
},10);
}
}
},setFocus:function(){
var _888;
if(window.document.getElementsByClassName("skeleton").length>0){
_888=setTimeout(function(){
curam.util.setFocus();
},300);
}else{
if(_888){
clearTimeout(_888);
}
var _889=curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
if(_889){
return;
}
curam.util._setFocusCurrentIframe();
var _88a=curam.util.getUrlParamValue(dojo.global.location.href,"o3frame")=="modal"||(curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==null&&curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==undefined);
if(!_88a){
_7cc(function(){
var _88b=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
var _88c=sessionStorage.getItem("curamDefaultActionId");
var _88d=null;
if(!_88b&&_88c){
sessionStorage.removeItem("curamDefaultActionId");
_88d=dojo.query(".curam-default-action")[0].previousSibling;
}else{
_88d=curam.util.doSetFocus();
}
if(_88d){
curam.util.setFocusOnField(_88d,false);
}else{
window.focus();
}
});
}
}
},setFocusOnField:function(_88e,_88f,_890){
if(has("IE")||has("trident")){
var _891=1000;
var _892=_88f?200:500;
curam.util._createHiddenInputField(_88e);
var _893=function(ff){
return function(){
var _894=ff.ownerDocument.activeElement;
if(_894.tagName==="INPUT"&&!_894.classList.contains("hidden-focus-input")||_894.tagName==="TEXTAREA"||(_894.tagName==="SPAN"&&_894.className=="fileUploadButton")||(_894.tagName==="A"&&_894.className=="popup-action")||(_894.tagName==="IFRAME"&&_894.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
if(_88f){
var _895=attr.get(_88e,"aria-label");
var _896="";
var _897=attr.get(_88e,"objid");
if(_897&&_897.indexOf("component")==0||_7d0.contains(_88e,"dijitReset dijitInputInner")){
_896=_88e.title;
}else{
_896=_890||"Modal Dialog";
}
if(_88e&&_88e.id!=="container-messages-ul"){
attr.set(_88e,"aria-label",_896);
}
var _898=function(_899){
return function(e){
_7d3("input|select[aria-label="+_896+"]").forEach(function(_89a){
_899&&attr.set(_89a,"aria-label",_899);
!_899&&attr.remove(_89a,"aria-label");
});
};
};
on(_88e,"blur",_898(_895));
}
if(_88e.tagName==="TEXTAREA"){
setTimeout(_893(_88e),_891);
}else{
if(_88e.tagName==="SELECT"||(_88e.tagName==="INPUT"&&attr.get(_88e,"type")==="text")){
setTimeout(_893(_88e),_892);
}else{
_88e.focus();
}
}
}else{
_88e.focus();
}
},_createHiddenInputField:function(_89b){
var _89c=_89b.ownerDocument.forms["mainForm"];
if(_89c&&(_89b.tagName==="SELECT"||(_89b.tagName==="INPUT"&&attr.get(_89b,"type")==="text"))){
var _89d=_7cb.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_7cb.place(_89d,_89c,"before");
_89d.focus();
on(_89d,"blur",function(){
_7cb.destroy(_89d);
});
}
},doSetFocus:function(){
try{
var _89e=curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
if(_89e!=false&&!curam.util.isModalWindow()){
return _89e;
}
var _89f=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_89f){
return _89f;
}
var form=document.forms[0];
if(!form){
return false;
}
var _8a0=form.querySelectorAll("button, output, input:not([type=\"image\"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton");
var _8a1=false;
var l=_8a0.length,el;
for(var i=0;i<l;i++){
el=_8a0[i];
if(!_8a1&&/selectHook/.test(el.className)){
_8a1=_7d3(el).closest("table")[0];
}
if(!_8a1&&!(el.style.visibility=="hidden")&&(/select-one|select-multiple|checkbox|radio|text/.test(el.type)||el.tagName=="TEXTAREA"||/popup-action|fileUploadButton/.test(el.className))&&!/dijitArrowButtonInner|dijitValidationInner/.test(el.className)){
_8a1=el;
}
if(el.tabIndex=="1"){
el.tabIndex=0;
_8a1=el;
break;
}
}
lastOpenedTabButton=curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
if(!_8a1&&lastOpenedTabButton){
return lastOpenedTabButton;
}
var _8a2=_8a1.classList.contains("bx--date-picker__input");
if(_8a2){
var _8a3=document.querySelector(".bx--uim-modal");
if(_8a3){
_8a1=_8a3;
}
}
return _8a1;
}
catch(e){
_7d7.log(_7d7.getProperty("curam.util.error.focus"),e.message);
return false;
}
return false;
},openLocaleSelector:function(_8a4){
_8a4=_7d2.fix(_8a4);
var _8a5=_8a4.target;
while(_8a5&&_8a5.tagName!="A"){
_8a5=_8a5.parentNode;
}
var loc=_8a5.href;
var rpu=curam.util.getUrlParamValue(loc,"__o3rpu");
rpu=curam.util.removeUrlParam(rpu,"__o3rpu");
var href="user-locale-selector.jspx"+"?__o3rpu="+rpu;
if(!curam.util.isActionPage(dojo.global.location.href)){
openModalDialog({href:href},"width=500,height=300",200,150);
}else{
alert(curam.util.msgLocaleSelectorActionPage);
}
return false;
},openLocaleNew:function(_8a6){
_7d2.stop(_8a6);
if(_8a6.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-locale-selector.jspx",{dialogOptions:"width=300"});
});
},isActionPage:function(url){
var _8a7=curam.util.getLastPathSegmentWithQueryString(url);
var _8a8=_8a7.split("?")[0];
return _8a8.indexOf("Action.do")>-1;
},closeLocaleSelector:function(_8a9){
_8a9=_7d2.fix(_8a9);
_7d2.stop(_8a9);
dojo.global.close();
return false;
},getSuffixFromClass:function(node,_8aa){
var _8ab=attr.get(node,"class").split(" ");
var _8ac=_7cf.filter(_8ab,function(_8ad){
return _8ad.indexOf(_8aa)==0;
});
if(_8ac.length>0){
return _8ac[0].split(_8aa)[1];
}else{
return null;
}
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},stripeTable:function(_8ae,_8af,_8b0){
var _8b1=_8ae.tBodies[0];
var _8b2=(_8af?2:1);
if(_8b1.rows.length<_8b2){
return;
}
var rows=_8b1.rows;
var _8b3=[],_8b4=[],_8b5=false,_8b6=[],_8b7="";
for(var i=0,l=rows.length;i<l;i+=_8b2){
var _8b8=(i%(2*_8b2)==0),_8b9=_8b8?_8b4:_8b3;
_7d0.remove(rows[i],["odd-last-row","even-last-row"]);
_8b9.push(rows[i]);
if(i==_8b0){
_8b6.push(rows[i]);
_8b7=_8b8?"odd":"even";
_8b5=true;
}
if(_8af&&rows[i+1]){
_7d0.remove(rows[i+1],["odd-last-row","even-last-row"]);
_8b9.push(rows[i+1]);
_8b5&&_8b6.push(rows[i+1]);
}
_8b5=false;
}
_8b4.forEach(function(_8ba){
_7d0.replace(_8ba,"odd","even");
});
_8b3.forEach(function(_8bb){
_7d0.replace(_8bb,"even","odd");
});
_8b6.forEach(function(_8bc){
_7d0.add(_8bc,_8b7+"-last-row");
});
},fillString:function(_8bd,_8be){
var _8bf="";
while(_8be>0){
_8bf+=_8bd;
_8be-=1;
}
return _8bf;
},updateHeader:function(qId,_8c0,_8c1,_8c2){
var _8c3=dom.byId("header_"+qId);
_8c3.firstChild.nextSibling.innerHTML=_8c0;
answerCell=dom.byId("chosenAnswer_"+qId);
answerCell.innerHTML=_8c1;
sourceCell=dom.byId("chosenSource_"+qId);
sourceCell.innerHTML=_8c2;
},search:function(_8c4,_8c5){
var _8c6=_7ca.byId(_8c4).get("value");
var _8c7=_7ca.byId(_8c5);
if(_8c7==null){
_8c7=dom.byId(_8c5);
}
var _8c8=null;
if(_8c7!=null){
if(_8c7.tagName==null){
_8c8=_8c7?_8c7.get("value"):null;
}else{
if(_8c7.tagName=="SELECT"){
var _8c9=_7d3(".multiple-search-banner select option");
_7cf.forEach(_8c9,function(elem){
if(elem.selected){
_8c8=elem.value;
}
});
}
}
}
var _8ca="";
var _8cb;
var _8cc;
if(_8c8){
_8cc=_8c8.split("|");
_8ca=_8cc[0];
_8cb=_8cc[1];
}
var _8cd=curam.util.defaultSearchPageID;
var _8ce="";
if(sessionStorage.getItem("appendSUEP")==="true"){
if(_8ca===""){
_8ce=_8cd+"Page.do?searchText="+encodeURIComponent(_8c6)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1");
}else{
_8ce=_8cb+"Page.do?searchText="+encodeURIComponent(_8c6)+"&searchType="+encodeURIComponent(_8ca)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1,"+curam.util.secureURLsExemptParamsPrefix+"_ST2");
}
}else{
if(_8ca===""){
_8ce=_8cd+"Page.do?searchText="+encodeURIComponent(_8c6);
}else{
_8ce=_8cb+"Page.do?searchText="+encodeURIComponent(_8c6)+"&searchType="+encodeURIComponent(_8ca);
}
}
var _8cf=new curam.ui.PageRequest(_8ce);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_8cf);
});
},updateDefaultSearchText:function(_8d0,_8d1){
var _8d2=_7ca.byId(_8d0);
var _8d3=_7ca.byId(_8d1);
var _8d4=_8d2?_8d2.get("value"):null;
var str=_8d4.split("|")[2];
_8d3.set("placeHolder",str);
_7d1.publish("curam/application-search/combobox-changed",_8d4);
},updateSearchBtnState:function(_8d5,_8d6){
var _8d7=_7ca.byId(_8d5);
var btn=dom.byId(_8d6);
var _8d8=_8d7.get("value");
if(!_8d8||lang.trim(_8d8).length<1){
_7d0.add(btn,"dijitDisabled");
}else{
_7d0.remove(btn,"dijitDisabled");
}
},furtherOptionsSearch:function(){
var _8d9=curam.util.furtherOptionsPageID+"Page.do";
var _8da=new curam.ui.PageRequest(_8d9);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_8da);
});
},searchButtonStatus:function(_8db){
var btn=dom.byId(_8db);
if(!_7d0.contains(btn,"dijitDisabled")){
return true;
}
},getPageHeight:function(){
var _8dc=400;
var _8dd=0;
if(_7d3("frameset").length>0){
curam.debug.log("curam.util.getPageHeight() "+_7d7.getProperty("curam.util.default.height"),_8dc);
_8dd=_8dc;
}else{
var _8de=function(node){
if(!node){
curam.debug.log(_7d7.getProperty("curam.util.node"));
return 0;
}
var mb=geom.getMarginSize(node);
var pos=geom.position(node);
return pos.y+mb.h;
};
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
var _8df=_7d3("div#content")[0];
var _8e0=_8de(_8df);
curam.debug.log(_7d7.getProperty("curam.util.page.height"),_8e0);
_8dd=_8e0;
}else{
var _8e1=dom.byId("content")||dom.byId("wizard-content");
var _8e2=_7d3("> *",_8e1).filter(function(n){
return n.tagName.indexOf("SCRIPT")<0&&_7ce.get(n,"visibility")!="hidden"&&_7ce.get(n,"display")!="none";
});
var _8e3=_8e2[0];
for(var i=1;i<_8e2.length;i++){
if(_8de(_8e2[i])>=_8de(_8e3)){
_8e3=_8e2[i];
}
}
_8dd=_8de(_8e3);
curam.debug.log("curam.util.getPageHeight() "+_7d7.getProperty("curam.util.base.height"),_8dd);
var _8e4=_7d3(".actions-panel",_7cd.body());
if(_8e4.length>0){
var _8e5=geom.getMarginBox(_8e4[0]).h;
curam.debug.log("curam.util.getPageHeight() "+_7d7.getProperty("curam.util.panel.height"));
_8dd+=_8e5;
_8dd+=10;
}
var _8e6=_7d3("body.details");
if(_8e6.length>0){
curam.debug.log("curam.util.getPageHeight() "+_7d7.getProperty("curam.util.bar.height"));
_8dd+=20;
}
}
}
curam.debug.log("curam.util.getPageHeight() "+_7d7.getProperty("curam.util.returning"),_8dd);
return _8dd;
},toCommaSeparatedList:function(_8e7){
var _8e8="";
for(var i=0;i<_8e7.length;i++){
_8e8+=_8e7[i];
if(i<_8e7.length-1){
_8e8+=",";
}
}
return _8e8;
},setupGenericKeyHandler:function(){
_7cc(function(){
var f=function(_8e9){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")&&_8e9.keyCode==27){
var ev=_7d2.fix(_8e9);
var _8ea=_7ca.byId(ev.target.id);
var _8eb=typeof _8ea!="undefined"&&_8ea.baseClass=="dijitTextBox dijitComboBox";
if(!_8eb){
curam.dialog.closeModalDialog();
}
}
if(_8e9.keyCode==13){
var ev=_7d2.fix(_8e9);
var _8ec=ev.target.type=="text";
var _8ed=ev.target.type=="radio";
var _8ee=ev.target.type=="checkbox";
var _8ef=ev.target.type=="select-multiple";
var _8f0=ev.target.type=="password";
var _8f1=_7ca.byId(ev.target.id);
if(typeof _8f1!="undefined"){
var _8f2=_7ca.byId(ev.target.id);
if(!_8f2){
_8f2=_7ca.byNode(dom.byId("widget_"+ev.target.id));
}
if(_8f2&&_8f2.enterKeyOnOpenDropDown){
_8f2.enterKeyOnOpenDropDown=false;
return false;
}
}
var _8f3=ev.target.getAttribute("data-carbon-attach-point");
if(_8f3&&_8f3==="carbon-menu"){
return false;
}
var _8f4=typeof _8f1!="undefined"&&_8f1.baseClass=="dijitComboBox";
if((!_8ec&&!_8ed&&!_8ee&&!_8ef&&!_8f0)||_8f4){
return true;
}
var _8f5=null;
var _8f6=_7d3(".curam-default-action");
if(_8f6.length>0){
_8f5=_8f6[0];
}else{
var _8f7=_7d3("input[type='submit']");
if(_8f7.length>0){
_8f5=_8f7[0];
}
}
if(_8f5!=null){
_7d2.stop(_7d2.fix(_8e9));
curam.util.clickButton(_8f5);
return false;
}
require(["curam/dateSelectorUtil"],function(_8f8){
var _8f9=dom.byId("year");
if(_8f9){
dojo.stopEvent(dojo.fixEvent(_8e9));
_8f8.updateCalendar();
}
});
}
return true;
};
curam.util.connect(_7cd.body(),"onkeyup",f);
});
},enterKeyPress:function(_8fa){
if(_8fa.keyCode==13){
return true;
}
},swapState:function(node,_8fb,_8fc,_8fd){
if(_8fb){
_7d0.replace(node,_8fc,_8fd);
}else{
_7d0.replace(node,_8fd,_8fc);
}
},makeQueryString:function(_8fe){
if(!_8fe||_8fe.length==0){
return "";
}
var _8ff=[];
for(var _900 in _8fe){
_8ff.push(_900+"="+encodeURIComponent(_8fe[_900]));
}
return "?"+_8ff.join("&");
},fileDownloadAnchorHandler:function(url){
var _901=curam.util.getTopmostWindow();
var _902=_901.dojo.subscribe("/curam/dialog/close",function(id,_903){
if(_903==="confirm"){
curam.util.clickHandlerForListActionMenu(url,false,false);
}
_901.dojo.unsubscribe(_902);
});
var _904=new _7dc("GenericModalError");
var _905=_904.getProperty("file.download.warning.dialog.width");
var _906=_904.getProperty("file.download.warning.dialog.height");
if(!_905){
_905=500;
}
if(!_906){
_906=225;
}
var _907=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_905+",height="+_906,"file.download.warning.title","file.download.warning."+_907);
return false;
},fileDownloadListActionHandler:function(url,_908,_909,_90a){
var _90b=curam.util.getTopmostWindow();
var _90c=_90b.dojo.subscribe("/curam/dialog/close",function(id,_90d){
if(_90d==="confirm"){
curam.util.clickHandlerForListActionMenu(url,_908,_909,_90a);
}
_90b.dojo.unsubscribe(_90c);
});
var _90e=new _7dc("GenericModalError");
var _90f=_90e.getProperty("file.download.warning.dialog.width");
var _910=_90e.getProperty("file.download.warning.dialog.height");
if(!_90f){
_90f=500;
}
if(!_910){
_910=225;
}
var _911=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_90f+",height="+_910,"file.download.warning.title","file.download.warning."+_911);
},_getBrowserName:function(){
var _912=has("trident");
var _913=dojo.isFF;
var _914=dojo.isChrome;
var _915=dojo.isSafari;
var _916=curam.util.getTopmostWindow();
var _917=curam.util.ExpandableLists._isExternalApp(_916);
if(_912!=undefined){
var _918=_912+4;
if(_918<8){
return "unknown.browser";
}else{
return "ie"+_918;
}
}else{
if(_913!=undefined&&_917){
return "firefox";
}else{
if(_914!=undefined){
return "chrome";
}else{
if(_915!=undefined&&_917){
return "safari";
}
}
}
}
return "unknown.browser";
},clickHandlerForListActionMenu:function(url,_919,_91a,_91b){
if(_919){
var href=curam.util.replaceUrlParam(url,"o3frame","modal");
var ctx=dojo.global.jsScreenContext;
ctx.addContextBits("MODAL");
href=curam.util.replaceUrlParam(href,"o3ctx",ctx.getValue());
curam.util.redirectWindow(href);
return;
}
var _91c={href:url};
require(["curam/ui/UIMPageAdaptor"]);
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_91c)){
if(_91c.href.indexOf("/servlet/FileDownload")){
sessionStorage.setItem("addOnUnloadTriggeredByFileDownload","true");
dojo.global.location=url;
sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
}else{
dojo.global.location=url;
}
return;
}
if(_91c!=null){
if(_91b){
_7d2.fix(_91b);
_7d2.stop(_91b);
}
if(!_91c.href||_91c.href.length==0){
return;
}
if(_91a&&!curam.util.isInternal(url)){
dojo.global.open(url);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_91c)){
var _91d=new curam.ui.PageRequest(_91c.href);
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")){
_91d.pageHolder=dojo.global;
}
require(["curam/tab"],function(){
var _91e=curam.tab.getTabController();
if(_91e){
_91e.handlePageRequest(_91d);
}
});
}
}
}
},clickHandlerForMailtoLinks:function(_91f,url){
dojo.stopEvent(_91f);
var _920=dojo.query("#mailto_frame")[0];
if(!_920){
_920=dojo.io.iframe.create("mailto_frame","");
}
_920.src=url;
return false;
},isInternal:function(url){
var path=url.split("?")[0];
var _921=path.match("Page.do");
if(_921!=null){
return true;
}
return false;
},getLastPathSegmentWithQueryString:function(url){
var _922=url.split("?");
var _923=_922[0].split("/");
return _923[_923.length-1]+(_922[1]?"?"+_922[1]:"");
},replaceSubmitButton:function(name,_924,_925,_926,_927){
if(curam.replacedButtons[name]=="true"){
return;
}
var _928="__o3btn."+name;
var _929;
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_929=_7d3("input[id='"+_928+"']");
}else{
_929=_7d3("input[name='"+_928+"']");
}
_929.forEach(function(_92a,_92b,_92c){
if(_924){
var _92d=_92c[1];
_92d.setAttribute("value",_924);
}
_92a.tabIndex=-1;
var _92e=_92a.parentNode;
var _92f;
var _930=curam.util.isInternalModal()&&curam.util.isModalFooter(_92a);
var _931="btn-id-"+_92b;
var _932="ac initially-hidden-widget "+_931;
if(_7d0.contains(_92a,"first-action-control")){
_932+=" first-action-control";
}
if(_930){
var _933=(_925&&!_927)?undefined:_92c[0];
var _934=_925?{"href":"","buttonid":_926}:{"buttonid":_926};
var _935=_92a.getAttribute("data-rawtestid");
if(_935){
_934.dataTestId=_935;
}
var _936=_7d0.contains(_92a,"curam-default-action")?true:false;
curam.util.addCarbonModalButton(_934,_92a.value,_933,_936);
}else{
curam.util.setupWidgetLoadMask("a."+_931);
var _932="ac initially-hidden-widget "+_931;
if(_7d0.contains(_92a,"first-action-control")){
_932+=" first-action-control";
}
var _92f=_7cb.create("a",{"class":_932,href:"#"},_92a,"before");
var _937=dojo.query(".page-level-menu")[0];
if(_937){
attr.set(_92f,"title",_92a.value);
}
_7cb.create("span",{"class":"filler"},_92f,"before");
var left=_7cb.create("span",{"class":"left-corner"},_92f);
var _938=_7cb.create("span",{"class":"right-corner"},left);
var _939=_7cb.create("span",{"class":"middle"},_938);
_939.appendChild(document.createTextNode(_92a.value));
curam.util.addActionControlClass(_92f);
}
if(_92f){
on(_92f,"click",function(_93a){
curam.util.clickButton(this._submitButton);
_7d2.stop(_93a);
});
_92f._submitButton=_92c[0];
}
_7d0.add(_92a,"hidden-button");
attr.set(_92a,"aria-hidden","true");
attr.set(_92a,"id",_92a.id+"_"+_92b);
});
curam.replacedButtons[name]="true";
},isInternalModal:function(){
return !dojo.global.jsScreenContext.hasContextBits("EXTAPP")&&dojo.global.jsScreenContext.hasContextBits("MODAL");
},isModalFooter:function(_93b){
if(_93b){
var _93c=_93b.parentNode.parentNode;
return _93c&&_93c.id=="actions-panel";
}
},addCarbonModalButton:function(_93d,_93e,_93f,_940){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton",[_93d,_93e,_93f,_940,window]);
},setupWidgetLoadMask:function(_941){
curam.util.subscribe("/curam/page/loaded",function(){
var _942=_7d3(_941)[0];
if(_942){
_7ce.set(_942,"visibility","visible");
}else{
curam.debug.log("setupButtonLoadMask: "+_7d7.getProperty("curam.util.not.found")+"'"+_941+"'"+_7d7.getProperty("curam.util.ignore.mask"));
}
});
},optReplaceSubmitButton:function(name){
if(curam.util.getFrameRoot(dojo.global,"wizard")==null){
curam.util.replaceSubmitButton(name);
return;
}
var _943=curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
if(_943.delegatesSubmit[jsPageID]!="assumed"){
curam.util.replaceSubmitButton(name);
}
},clickButton:function(_944){
var _945=dom.byId("mainForm");
var _946;
if(!_944){
curam.debug.log("curam.util.clickButton: "+_7d7.getProperty("curam.util..no.arg"));
return;
}
if(typeof (_944)=="string"){
var _947=_944;
curam.debug.log("curam.util.clickButton: "+_7d7.getProperty("curam.util.searching")+_7d7.getProperty("curam.util.id.of")+"'"+_947+"'.");
_944=_7d3("input[id='"+_947+"']")[0];
if(!_944){
_944=_7d3("input[name='"+_947+"']")[0];
}
if(!_944.form&&!_944.id){
curam.debug.log("curam.util.clickButton: "+_7d7.getProperty("curam.util.searched")+_7d7.getProperty("curam.util.id.of")+"'"+_947+_7d7.getProperty("curam.util.exiting"));
return;
}
}
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_946=_944;
}else{
_946=_7d3("input[id='"+_944.id+"']",_945)[0];
if(!_946){
_946=_7d3("input[name='"+_944.name+"']",_945)[0];
}
}
try{
if(attr.get(_945,"action").indexOf(jsPageID)==0){
curam.util.publishRefreshEvent();
}
_946.click();
}
catch(e){
curam.debug.log(_7d7.getProperty("curam.util.exception.clicking"));
}
},printPage:function(_948,_949){
_7d2.stop(_949);
var _94a=dojo.window.get(_949.currentTarget.ownerDocument);
if(_948===false){
curam.util._printMainAreaWindow(_94a);
return false;
}
var _94b=_94a.frameElement;
var _94c=_94b;
while(_94c&&!_7d0.contains(_94c,"tab-content-holder")){
_94c=_94c.parentNode;
}
var _94d=_94c;
var _94e=dojo.query(".detailsPanelFrame",_94d)[0];
var _94f=_94e!=undefined&&_94e!=null;
if(_94f){
var isIE=has("trident")||has("ie");
var _950=has("edge");
var _951=_7d0.contains(_94e.parentNode,"collapsed");
if(isIE&&_951){
_7ce.set(_94e.parentNode,"display","block");
}
_94e.contentWindow.focus();
_94e.contentWindow.print();
if(isIE&&_951){
_7ce.set(_94e.parentNode,"display","");
}
if(isIE||_950){
setTimeout(function(){
if(_950){
function _952(){
curam.util._printMainAreaWindow(_94a);
curam.util.getTopmostWindow().document.body.removeEventListener("mouseover",_952,true);
return false;
};
curam.util.getTopmostWindow().document.body.addEventListener("mouseover",_952,true);
}else{
if(isIE){
curam.util._printMainAreaWindow(_94a);
return false;
}
}
},2000);
}else{
curam.util._printMainAreaWindow(_94a);
return false;
}
}else{
curam.util._printMainAreaWindow(_94a);
return false;
}
},_printMainAreaWindow:function(_953){
var _954=_7d3(".list-details-row-toggle.expanded");
if(_954.length>0){
curam.util._prepareContentPrint(_953);
_953.focus();
_953.print();
curam.util._deletePrintVersion();
}else{
_953.focus();
_953.print();
}
},_prepareContentPrint:function(_955){
var _956=Array.prototype.slice.call(_955.document.querySelectorAll("body iframe"));
_956.forEach(function(_957){
curam.util._prepareContentPrint(_957.contentWindow);
var list=_957.contentWindow.document.querySelectorAll(".title-exists");
var _958=_957.contentWindow.document.querySelectorAll(".title-exists div.context-panel-wrapper");
if(list.length>0&&_958.length===0){
var _959=document.createElement("div");
_959.setAttribute("class","tempContentPanelFrameWrapper");
_959.innerHTML=list[0].innerHTML;
var _95a=_957.parentNode;
_95a.parentNode.appendChild(_959);
_95a.style.display="none";
curam.util.wrappersMap.push({tempDivWithIframeContent:_959,iframeParentElement:_95a});
}
});
},_deletePrintVersion:function(){
if(curam.util.wrappersMap){
curam.util.wrappersMap.forEach(function(_95b){
_95b.tempDivWithIframeContent.parentNode.removeChild(_95b.tempDivWithIframeContent);
_95b.iframeParentElement.style.display="block";
});
curam.util.wrappersMap=[];
}
},addSelectedClass:function(_95c){
_7d0.add(_95c.target,"selected");
},removeSelectedClass:function(_95d){
_7d0.remove(_95d.target,"selected");
},openHelpPage:function(_95e,_95f){
_7d2.stop(_95e);
dojo.global.open(_95f);
},connect:function(_960,_961,_962){
var h=function(_963){
_962(_7d2.fix(_963));
};
if(has("ie")&&has("ie")<9){
_960.attachEvent(_961,h);
_7d5.addOnWindowUnload(function(){
_960.detachEvent(_961,h);
});
return {object:_960,eventName:_961,handler:h};
}else{
var _964=_961;
if(_961.indexOf("on")==0){
_964=_961.slice(2);
}
var dt=on(_960,_964,h);
_7d5.addOnWindowUnload(function(){
dt.remove();
});
return dt;
}
},disconnect:function(_965){
if(has("ie")&&has("ie")<9){
_965.object.detachEvent(_965.eventName,_965.handler);
}else{
_965.remove();
}
},subscribe:function(_966,_967){
var st=_7d1.subscribe(_966,_967);
_7d5.addOnWindowUnload(function(){
st.remove();
});
return st;
},unsubscribe:function(_968){
_968.remove();
},addActionControlClickListener:function(_969){
var _96a=dom.byId(_969);
var _96b=_7d3(".ac",_96a);
if(_96b.length>0){
for(var i=0;i<_96b.length;i++){
var _96c=_96b[i];
curam.util.addActionControlClass(_96c);
}
}
this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
},_addAccessibilityMarkupInAddressClustersWhenContextIsMissing:function(){
var _96d=_7d3(".bx--accordion__content");
_96d.forEach(function(_96e){
var _96f=_7d3(".bx--address",_96e)[0];
if(typeof (_96f)!="undefined"){
var _970=new _7dc("util");
var _971=_96f.parentElement.parentElement.parentElement;
var _972=_971.parentElement.parentElement;
var _973=_7d3("h4, h3",_971).length==1?true:false;
var _974=attr.get(_972,"aria-label")!==null?true:false;
if(!_973&&!_974){
attr.set(_972,"role","group");
attr.set(_972,"aria-label",_970.getProperty("curam.address.header"));
}
}
});
},addActionControlClass:function(_975){
curam.util.connect(_975,"onmousedown",function(){
_7d0.add(_975,"selected-button");
curam.util.connect(_975,"onmouseout",function(){
_7d0.remove(_975,"selected-button");
});
});
},getClusterActionSet:function(){
var _976=dom.byId("content");
var _977=_7d3(".blue-action-set",_976);
if(_977.length>0){
for(var i=0;i<_977.length;i++){
curam.util.addActionControlClickListener(_977[i]);
}
}
},adjustActionButtonWidth:function(){
if(has("ie")==8){
_7cc(function(){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")){
_7d3(".action-set > a").forEach(function(node){
if(node.childNodes[0].offsetWidth>node.offsetWidth){
_7ce.set(node,"width",node.childNodes[0].offsetWidth+"px");
_7ce.set(node,"display","block");
_7ce.set(node,"display","inline-block");
}
});
}
});
}
},setRpu:function(url,rtc,_978){
if(!url||!rtc||!rtc.getHref()){
throw {name:"Unexpected values",message:"This value not allowed for url or rtc"};
}
var _979=curam.util.getLastPathSegmentWithQueryString(rtc.getHref());
_979=curam.util.removeUrlParam(_979,curam.util.Constants.RETURN_PAGE_PARAM);
if(_978){
var i;
for(i=0;i<_978.length;i++){
if(!_978[i].key||!_978[i].value){
throw {name:"undefined value error",message:"The object did not contain a valid key/value pair"};
}
_979=curam.util.replaceUrlParam(_979,_978[i].key,_978[i].value);
}
}
var _97a=curam.util.replaceUrlParam(url,curam.util.Constants.RETURN_PAGE_PARAM,encodeURIComponent(_979));
curam.debug.log("curam.util.setRpu "+_7d7.getProperty("curam.util.added.rpu")+_97a);
return _97a;
},retrieveBaseURL:function(){
return dojo.global.location.href.match(".*://[^/]*/[^/]*");
},removeRoleRegion:function(){
var body=dojo.query("body")[0];
attr.remove(body,"role");
},iframeTitleFallBack:function(){
var _97b=curam.tab.getContainerTab(curam.tab.getContentPanelIframe());
var _97c=dom.byId(curam.tab.getContentPanelIframe());
var _97d=_97c.contentWindow.document.title;
var _97e=dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
var _97f=dojo.query("span.tabLabel",_97e)[0];
var _980=dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked",_97b.domNode)[0];
var _981=dojo.query("span.tabLabel",_980)[0];
if(_97d=="undefined"){
return this.getPageTitleOnContentPanel();
}else{
if(_97d&&_97d!=""){
return _97d;
}else{
if(_980){
return _981.innerHTML;
}else{
return _97f.innerHTML;
}
}
}
},getPageTitleOnContentPanel:function(){
var _982;
var _983=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _984;
if(_983&&_983.length==1){
_984=_983[0].contentWindow.document;
_7cd.withDoc(_984,function(){
var _985=dojo.query("div.title h2 span:not(.hidden)");
if(_985&&_985.length==1&&_985[0].textContent){
_982=lang.trim(_985[0].textContent);
}
},this);
}
if(_982){
return _982;
}else{
return undefined;
}
},addClassToLastNodeInContentArea:function(){
var _986=_7d3("> div","content");
var _987=_986.length;
if(_987==0){
return "No need to add";
}
var _988=_986[--_987];
while(_7d0.contains(_988,"hidden-action-set")&&_988){
_988=_986[--_987];
}
_7d0.add(_988,"last-node");
},highContrastModeType:function(){
var _989=dojo.query("body.high-contrast")[0];
return _989;
},isRtlMode:function(){
var _98a=dojo.query("body.rtl")[0];
return _98a;
},processBidiContextual:function(_98b){
_98b.dir=bidi.prototype._checkContextual(_98b.value);
},getCookie:function(name){
var dc=document.cookie;
var _98c=name+"=";
var _98d=dc.indexOf("; "+_98c);
if(_98d==-1){
_98d=dc.indexOf(_98c);
if(_98d!=0){
return null;
}
}else{
_98d+=2;
}
var end=document.cookie.indexOf(";",_98d);
if(end==-1){
end=dc.length;
}
return unescape(dc.substring(_98d+_98c.length,end));
},getHeadingTitleForScreenReader:function(_98e){
var _98f=curam.util.getTopmostWindow();
var _990=_98f.dojo.global._tabTitle;
if(_990){
curam.util.getHeadingTitle(_990,_98e);
}else{
var _991=_98f.dojo.subscribe("/curam/_tabTitle",function(_992){
if(_992){
curam.util.getHeadingTitle(_992,_98e);
}
_98f.dojo.unsubscribe(_991);
});
}
},getHeadingTitle:function(_993,_994){
var _995=undefined;
if(_993&&_993.length>0){
_995=_993;
}else{
_995=_994;
}
var _996=dojo.query(".page-title-bar");
var _997=dojo.query("div h2",_996[0]);
if(_997){
var _998=dojo.query("span",_997[0]);
var span=undefined;
if(_998){
span=_998[0];
}
if(!span||(span&&(span.innerHTML.length==0))){
if(span){
attr.set(span,"class","hidden");
attr.set(span,"title",_995);
span.innerHTML=_995;
}else{
span=_7cb.create("span",{"class":"hidden","title":_995},_997[0]);
span.innerHTML=_995;
}
}
}
},_setupBrowserTabTitle:function(_999,_99a,_99b){
_999=_999.replace("\\n"," ");
curam.util._browserTabTitleData.staticTabTitle=_999;
curam.util._browserTabTitleData.separator=_99a;
curam.util._browserTabTitleData.appNameFirst=_99b;
},_browserTabTitleData:{},setBrowserTabTitle:function(_99c){
curam.debug.log("curam.util.setBrowserTabTitle(title = "+_99c+") called");
if(!_99c){
_99c=curam.util._findAppropriateDynamicTitle();
}
var _99d=curam.util._browserTabTitleData.staticTabTitle;
var _99e=curam.util._browserTabTitleData.separator;
var _99f=curam.util._browserTabTitleData.appNameFirst;
if(!_99d&&!_99e&&!_99f&&!_99c){
var _9a0=document.querySelectorAll("head title")[0];
if(_9a0){
document.title=_9a0.text;
}
}else{
if(!_99c){
document.title=_99d;
}else{
if(_99d){
if(_99f){
document.title=_99d+_99e+_99c;
}else{
document.title=_99c+_99e+_99d;
}
}
}
}
},toggleCheckboxedSelectStyle:function(e,div){
if(e.checked){
div.classList.remove("unchecked");
div.classList.add("checked");
}else{
div.classList.remove("checked");
div.classList.add("unchecked");
}
},_findAppropriateDynamicTitle:function(){
var i;
var one;
var _9a1=dojo.query("iframe.curam-active-modal").length;
if(_9a1>1){
var _9a2=dojo.query("iframe.curam-active-modal")[0];
if(_9a2){
var _9a3=_9a2.contentDocument;
if(_9a3){
var _9a4=_9a3.head.getElementsByTagName("title")[0];
if(_9a4){
if(_9a4.innerHTML!=""){
one=_9a2.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
}
}
}
}
}
if(one){
return one;
}
var two;
var _9a5=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _9a6;
if(_9a5&&_9a5.length==1){
_9a6=_9a5[0].contentWindow.document;
_7cd.withDoc(_9a6,function(){
var _9a7=dojo.query("div.title h2 span:not(.hidden)");
var _9a8=dom.byId("error-messages",_9a6);
if(_9a8){
two=_9a6.head.getElementsByTagName("title")[0].textContent;
}else{
if(_9a7&&_9a7.length==1&&_9a7[0].textContent){
two=lang.trim(_9a7[0].textContent);
curam.debug.log("(2) Page title for Content Panel = "+two);
}else{
if(_9a7&&_9a7.length>1){
two=this._checkForSubTitles(_9a7);
}else{
curam.debug.log("(2) Could not find page title for content panel: header = "+_9a7);
}
}
}
},this);
}else{
curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = "+_9a5);
}
if(two){
return two;
}
var _9a9;
var _9aa=dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
if(_9aa&&_9aa.length==1&&_9aa[0].textContent){
_9a9=lang.trim(_9aa[0].textContent);
curam.debug.log("(3) Selected navigation item = "+_9a9);
}else{
curam.debug.log("(3) Could not find selected navigation item: navItem = "+_9aa);
}
if(_9a9){
return _9a9;
}
var four;
var _9ab=dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
var _9ac;
for(i=0;i<_9ab.length;i++){
if(_9ab[i].getAttribute("aria-selected")==="true"){
_9ac=_9ab[i];
}
}
if(_9ac&&_9ac.textContent){
four=lang.trim(_9ac.textContent);
curam.debug.log("(4) Selected navigation bar tab = "+four);
}else{
curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = "+_9ac);
}
if(four){
return four;
}
var five;
var _9ad=dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
if(_9ad&&_9ad.length==1&&_9ad[0].textContent){
five=lang.trim(_9ad[0].textContent);
curam.debug.log("(5) Selected application tab title bar = "+five);
}else{
curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = "+_9ad);
}
if(five){
return five;
}
var six;
var _9ae=dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
var _9af;
for(i=0;i<_9ae.length;i++){
if(_9ae[i].getAttribute("aria-selected")==="true"){
_9af=_9ae[i];
break;
}
}
if(_9af&&_9af.textContent){
six=lang.trim(_9af.textContent);
curam.debug.log("(6) Selected section title = "+six);
}else{
curam.debug.log("(6) Could not find selected section title: sections = "+_9ae);
}
if(six){
return six;
}
var _9b0;
_9a5=dom.byId("curamUAIframe");
if(_9a5&&_9a5.contentWindow&&_9a5.contentWindow.document){
_9a6=_9a5.contentWindow.document;
_7cd.withDoc(_9a6,function(){
var _9b1=dojo.query("div.page-header > div.page-title-bar > div.title > h2 > span");
if(_9b1&&_9b1.length==1&&_9b1[0].textContent){
_9b0=lang.trim(_9b1[0].textContent);
curam.debug.log("(7) UIM page title for external application page = "+_9b0);
}else{
curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = "+_9b1);
}
},this);
}
if(_9b0){
return _9b0;
}
return undefined;
},_checkForSubTitles:function(_9b2){
var i;
if(!_9b2[0].textContent){
return undefined;
}
for(i=1;i<_9b2.length;i++){
var _9b3=_9b2[i].getAttribute("class");
if(_9b3.indexOf("sub-title")===-1||!_9b2[i].textContent){
curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
for(i=0;i<_9b2.length;i++){
curam.debug.log(_9b2[i]);
}
return undefined;
}
}
var ret=_9b2[0].textContent;
for(i=1;i<_9b2.length;i++){
ret+=_9b2[i].textContent;
}
return ret;
},_addContextToWidgetForScreenReader:function(_9b4){
var _9b5=false;
var _9b6=0;
var _9b7=dojo.query(".training-details-list");
if(_9b7.length==1){
var _9b8=_9b7[0].parentElement;
var _9b9=dojo.query("div.bx--cluster",_9b8);
var _9ba=Array.prototype.indexOf.call(_9b9,_9b7[0]);
if(_9ba>=0){
for(var i=_9ba;i>=0;i--){
if(dojo.query("h3",_9b9[i]).length==1){
_9b5=true;
_9b6=i;
break;
}
}
}
if(_9b5){
var _9bb=dojo.query("h3.bx--accordion__title",_9b9[_9b6]);
if(_9bb.length==1){
var _9bc=_9bb[0].className+"_id";
attr.set(_9bb[0],"id",_9bc);
var _9bd=dojo.byId(_9b4).parentElement;
attr.set(_9bd,"aria-labelledby",_9bc);
attr.set(_9bd,"role","region");
}
}
}
},setParentFocusByChild:function(_9be){
var win=curam.util.UimDialog._getDialogFrameWindow(_9be);
if(win){
var _9bf=curam.dialog.getParentWindow(win);
if(_9bf){
_9bf.focus();
}
}
},toClipboard:function(_9c0){
try{
navigator.clipboard.writeText(_9c0);
}
catch(err){
console.warn("Failed to copy into the clipboard.");
}
if(dojo.getObject("curam.dialog",false)!=null){
var pw=curam.dialog.getParentWindow(window);
pw&&pw.dojo.publish("/curam/clip/selected",[_9c0]);
}
return false;
},removeTopScrollForIos:function(){
if(has("ios")){
window.document.body.scrollTop=0;
}
},insertAriaLiveLabelRecordSearchItem:function(_9c1){
var span=dojo.query("[data-search-page]")[0];
if(span){
span.setAttribute("aria-live",has("ios")?"polite":"assertive");
setTimeout(function(){
var _9c2=span.firstChild.nodeValue;
var _9c3=_9c2+_9c1;
span.innerHTML=_9c3;
},10);
}
},removeSessionStorageProperty:function(_9c4){
sessionStorage.removeItem(_9c4);
},addLayoutStylingOnDateTimeWidgetOnZoom:function(){
var _9c5=dojo.query("table.input-cluster td.field table.date-time");
console.log("datetimetable from util.js: "+_9c5);
var _9c6=_9c5.length;
if(_9c5.length>0){
for(var i=0;i<_9c5.length;i++){
var _9c7=_9c5[i];
var _9c8=_9c7.parentNode.parentNode;
_9c8.setAttribute("class","date-time-exists");
}
}
},fileUploadOpenFileBrowser:function(e,_9c9){
if(e.keyCode==32||e.keyCode==13){
dom.byId(_9c9).click();
}
},setupControlledLists:function(){
var _9ca="curam.listControls",_9cb="curam.listTogglers";
var _9cc=_9cd(_9ca),_9ce=_9cd(_9cb),_9cf=[];
var _9d0=_9cc&&_7d3("*[data-control]"),_9d1=_9ce&&_7d3("a[data-toggler]");
if(_9cc||_9ce){
for(var _9d2 in _9cc){
_9d0.filter(function(item){
return attr.get(item,"data-control")==_9d2;
}).forEach(function(_9d3,ix){
var c=dom.byId(_9d3),tr=_7d3(_9d3).closest("tr")[0];
!tr.controls&&(tr.controls=new Array());
tr.controls.push(c);
if(!tr.visited){
tr.visited=true;
_9cc[_9d2].push(tr);
}
});
var _9d4=_9cd(_9ca+"."+_9d2);
if(_9d4&&_9d4.length&&_9d4.length>0){
_9cf.push(_9d2);
}else{
_a00(_9ca+"."+_9d2,false);
}
}
if(_9d1&&_9d1.length>0){
for(var _9d2 in _9ce){
_9d1.filter(function(item){
return attr.get(item,"data-toggler")==_9d2;
}).forEach(function(_9d5){
var tr=_7d3(_9d5).closest("tr")[0];
tr.hasToggler=_9d5;
tr.visited=true;
_9ce[_9d2].push(tr);
});
var _9d6=_9cd(_9cb+"."+_9d2);
if(_9d6&&_9d6.length&&_9d6.length>0){
(_9cf.indexOf(_9d2)==-1)&&_9cf.push(_9d2);
}else{
_a00(_9cb+"."+_9d2,false);
}
}
}
_9cf.forEach(function(_9d7){
var _9d8=_9cd(_9ca+"."+_9d7)||_9cd(_9cb+"."+_9d7);
cu.updateListControlReadings(_9d7,_9d8);
});
}
dojo.subscribe("curam/sort/earlyAware",function(_9d9){
cu.suppressPaginationUpdate=_9d9;
});
dojo.subscribe("curam/update/readings/sort",function(_9da,rows){
if(!has("trident")){
cu.updateListActionReadings(_9da);
cu.updateListControlReadings(_9da,rows);
cu.suppressPaginationUpdate=false;
}else{
var _9db=cu.getPageBreak(_9da),_9dc=Math.ceil(rows.length/_9db);
cu.listRangeUpdate(0,_9dc,_9da,rows,_9db);
}
});
dojo.subscribe("curam/update/readings/pagination",function(_9dd,_9de){
_a00("curam.pageBreak."+_9dd,_9de);
});
dojo.subscribe("curam/update/pagination/rows",function(_9df,_9e0){
cu.updateDeferred&&!cu.updateDeferred.isResolved()&&cu.updateDeferred.cancel("Superseeded");
if(cu.suppressPaginationUpdate&&cu.suppressPaginationUpdate==_9e0){
return;
}
var _9e1=_a0d("curam.listTogglers."+_9e0),_9e2=_a0d("curam.listControls."+_9e0),lms=_9cd("curam.listMenus."+_9e0),_9e3=lms&&(lms.length>0);
var _9e4=_9e2||_9e1;
if(!_9e4&&!_9e3){
return;
}
if(_9e4){
var _9e5=_9df.filter(function(aRow){
return (!aRow.visited||!aRow.done)&&attr.has(aRow,"data-lix");
});
_9e1&&_9e5.forEach(function(aRow){
var tgl=_7d3("a[data-toggler]",aRow)[0];
aRow.hasToggler=tgl;
aRow.visited=true;
curam.listTogglers[_9e0].push(aRow);
});
_9e2&&_9e5.forEach(function(aRow){
var _9e6=_7d3("*[data-control]",aRow),_9e7=new Array();
_9e6.forEach(function(cRef){
_9e7.push(dom.byId(cRef));
});
aRow.controls=_9e7;
curam.listControls[_9e0].push(aRow);
aRow.visited=true;
});
var _9e8=_9e2?curam.listControls[_9e0]:curam.listTogglers[_9e0];
cu.updateListControlReadings(_9e0,_9e8);
}
_9e3&&cu.updateListActionReadings(_9e0);
});
},listRangeUpdate:function(_9e9,_9ea,_9eb,rows,psz){
if(_9e9==_9ea){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
return;
}
var def=cu.updateDeferred=new _7d4(function(_9ec){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
});
def.then(function(pNum){
cu.listRangeUpdate(pNum,_9ea,_9eb,rows,psz);
},function(err){
});
var _9ed=(_9e9===0)?0:200;
setTimeout(function(){
var _9ee=_9e9+1,_9ef=[_9e9*psz,(_9ee*psz)];
cu.updateListActionReadings(_9eb,_9ef);
cu.updateListControlReadings(_9eb,rows,_9ef);
def.resolve(_9ee);
},_9ed);
},updateListControlReadings:function(_9f0,_9f1,_9f2){
var c0,psz=cu.getPageBreak(_9f0),_9f3=cu.getStartShift(_9f0,_9f1[0]||false),_9f4=_9f1;
_9f2&&(_9f4=_9f1.slice(_9f2[0],_9f2[1]));
for(var rix in _9f4){
var aRow=_9f4[rix],_9f5=parseInt(attr.get(aRow,_9f6)),lx=(_9f5%psz)+_9f3,_9f7=aRow.controls;
if(!_9f7){
var _9f8=_7d3("*[data-control]",aRow),_9f9=new Array();
_9f8.forEach(function(cRef){
_9f9.push(dom.byId(cRef));
});
aRow.controls=_9f9;
_9f7=aRow.controls;
}
if(_9f7){
for(var cix in _9f7){
var crtl=_9f7[cix],ttl=crtl.textContent||false,_9fa=ttl?ttl+",":"";
if(crtl.nodeName=="A"){
var _9fb=_7d3("img",crtl)[0];
if(_9fb&&_7d0.contains(crtl,"ac first-action-control external-link")){
var _9fc=attr.get(_9fb,"alt");
attr.set(crtl,_9fd,_9fc+","+[listcontrol.reading.anchors,lx].join(" "));
}else{
attr.set(crtl,_9fd,_9fa+[listcontrol.reading.anchors,lx].join(" "));
}
}else{
attr.set(crtl,_9fd,_9fa+[listcontrol.reading.selectors,lx].join(" "));
}
}
}
cu.updateToggler(aRow,lx);
aRow.done=true;
}
},initListActionReadings:function(_9fe){
var _9ff="curam.listMenus."+_9fe;
_a00(_9ff,[]);
dojo.subscribe("curam/listmenu/started",function(_a01,_a02){
var tr=_7d3(_a01.containerNode).closest("tr")[0],lix=parseInt(attr.get(tr,_9f6)),lx=(lix%cu.getPageBreak(_a02))+cu.getStartShift(_a02,tr);
_a01.set({"belongsTo":tr,"aria-labelledBy":"","aria-label":[listcontrol.reading.menus,lx].join(" ")});
_9cd(_9ff).push(_a01);
cu.updateToggler(tr,lx);
});
},updateToggler:function(_a03,_a04){
_a03.hasToggler&&attr.set(_a03.hasToggler,_9fd,[listcontrol.reading.togglers,_a04].join(" "));
},updateListActionReadings:function(_a05,_a06){
var _a07=_9cd("curam.listMenus."+_a05),psz=cu.getPageBreak(_a05),_a08=false,_a09=_a07;
_a06&&(_a09=_a07.slice(_a06[0],_a06[1]));
for(var ix in _a09){
var _a0a=_a09[ix],tr=_a0a.belongsTo,lix=parseInt(attr.get(tr,_9f6)),_a08=_a08||cu.getStartShift(_a05,tr),_a0b=(lix%psz)+_a08;
_a0a.set(_9fd,[listcontrol.reading.menus,_a0b].join(" "));
cu.updateToggler(tr,_a0b);
tr.done=true;
}
},getPageBreak:function(_a0c){
if(!_a0d("curam.list.isPaginated."+_a0c)){
return 1000;
}
if(_9cd("curam.shortlist."+_a0c)){
return 1000;
}
var psz=_9cd("curam.pageBreak."+_a0c)||_9cd("curam.pagination.defaultPageSize")||1000;
return psz;
},getStartShift:function(_a0e,_a0f){
if(!_a0f){
return 2;
}
var _a10="curam.listHeaderStep."+_a0e,_a11=_9cd(_a10);
if(_a11){
return _a11;
}
_a00(_a10,2);
var _a12=_7d3(_a0f).closest("table");
if(_a12.length==0){
return 2;
}
var _a13=_a12.children("thead")[0];
!_a13&&_a00(_a10,1);
return curam.listHeaderStep[_a0e];
},extendXHR:function(){
var _a14=XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open=function(){
this.addEventListener("load",function(){
if(typeof (Storage)!=="undefined"){
var _a15=this.getResponseHeader("sessionExpiry");
sessionStorage.setItem("sessionExpiry",_a15);
}
});
_a14.apply(this,arguments);
};
},suppressPaginationUpdate:false,updateDeferred:null});
var cu=curam.util,_9cd=dojo.getObject,_a00=dojo.setObject,_a0d=dojo.exists,_9fd="aria-label",_9f6="data-lix";
return curam.util;
});
},"dojo/store/Memory":function(){
define(["../_base/declare","./util/QueryResults","./util/SimpleQueryEngine"],function(_a16,_a17,_a18){
var base=null;
return _a16("dojo.store.Memory",base,{constructor:function(_a19){
for(var i in _a19){
this[i]=_a19[i];
}
this.setData(this.data||[]);
},data:null,idProperty:"id",index:null,queryEngine:_a18,get:function(id){
return this.data[this.index[id]];
},getIdentity:function(_a1a){
return _a1a[this.idProperty];
},put:function(_a1b,_a1c){
var data=this.data,_a1d=this.index,_a1e=this.idProperty;
var id=_a1b[_a1e]=(_a1c&&"id" in _a1c)?_a1c.id:_a1e in _a1b?_a1b[_a1e]:Math.random();
if(id in _a1d){
if(_a1c&&_a1c.overwrite===false){
throw new Error("Object already exists");
}
data[_a1d[id]]=_a1b;
}else{
_a1d[id]=data.push(_a1b)-1;
}
return id;
},add:function(_a1f,_a20){
(_a20=_a20||{}).overwrite=false;
return this.put(_a1f,_a20);
},remove:function(id){
var _a21=this.index;
var data=this.data;
if(id in _a21){
data.splice(_a21[id],1);
this.setData(data);
return true;
}
},query:function(_a22,_a23){
return _a17(this.queryEngine(_a22,_a23)(this.data));
},setData:function(data){
if(data.items){
this.idProperty=data.identifier||this.idProperty;
data=this.data=data.items;
}else{
this.data=data;
}
this.index={};
for(var i=0,l=data.length;i<l;i++){
this.index[data[i][this.idProperty]]=i;
}
}});
});
},"dijit/_base/sniff":function(){
define(["dojo/uacss"],function(){
});
},"dijit/layout/StackContainer":function(){
define(["dojo/_base/array","dojo/cookie","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/has","dojo/_base/lang","dojo/on","dojo/ready","dojo/topic","dojo/when","../registry","../_WidgetBase","./_LayoutWidget"],function(_a24,_a25,_a26,_a27,_a28,has,lang,on,_a29,_a2a,when,_a2b,_a2c,_a2d){
if(1){
_a29(0,function(){
var _a2e=["dijit/layout/StackController"];
require(_a2e);
});
}
var _a2f=_a26("dijit.layout.StackContainer",_a2d,{doLayout:true,persist:false,baseClass:"dijitStackContainer",buildRendering:function(){
this.inherited(arguments);
_a27.add(this.domNode,"dijitLayoutContainer");
},postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"keydown",lang.hitch(this,"_onKeyDown")));
},startup:function(){
if(this._started){
return;
}
var _a30=this.getChildren();
_a24.forEach(_a30,this._setupChild,this);
if(this.persist){
this.selectedChildWidget=_a2b.byId(_a25(this.id+"_selectedChild"));
}else{
_a24.some(_a30,function(_a31){
if(_a31.selected){
this.selectedChildWidget=_a31;
}
return _a31.selected;
},this);
}
var _a32=this.selectedChildWidget;
if(!_a32&&_a30[0]){
_a32=this.selectedChildWidget=_a30[0];
_a32.selected=true;
}
_a2a.publish(this.id+"-startup",{children:_a30,selected:_a32,textDir:this.textDir});
this.inherited(arguments);
},resize:function(){
if(!this._hasBeenShown){
this._hasBeenShown=true;
var _a33=this.selectedChildWidget;
if(_a33){
this._showChild(_a33);
}
}
this.inherited(arguments);
},_setupChild:function(_a34){
var _a35=_a34.domNode,_a36=_a28.place("<div role='tabpanel' class='"+this.baseClass+"ChildWrapper dijitHidden'>",_a34.domNode,"replace"),_a37=_a34["aria-label"]||_a34.title||_a34.label;
var _a38=_a36.parentElement;
if(_a38&&_a38.parentElement){
var _a39=_a38.parentElement.id+"_tablist";
var _a3a=dojo.byId(_a39);
if(_a3a&&_a3a.style.height.trim()=="0px"){
_a36.removeAttribute("role");
}
}
if(_a37){
_a36.setAttribute("aria-label",_a37);
}
_a28.place(_a35,_a36);
_a34._wrapper=_a36;
this.inherited(arguments);
if(_a35.style.display=="none"){
_a35.style.display="block";
}
_a34.domNode.removeAttribute("title");
},addChild:function(_a3b,_a3c){
this.inherited(arguments);
if(this._started){
_a2a.publish(this.id+"-addChild",_a3b,_a3c);
this.layout();
if(!this.selectedChildWidget){
this.selectChild(_a3b);
}
}
},removeChild:function(page){
var idx=_a24.indexOf(this.getChildren(),page);
this.inherited(arguments);
_a28.destroy(page._wrapper);
delete page._wrapper;
if(this._started){
_a2a.publish(this.id+"-removeChild",page);
}
if(this._descendantsBeingDestroyed){
return;
}
if(this.selectedChildWidget===page){
this.selectedChildWidget=undefined;
if(this._started){
var _a3d=this.getChildren();
if(_a3d.length){
this.selectChild(_a3d[Math.max(idx-1,0)]);
}
}
}
if(this._started){
this.layout();
}
},selectChild:function(page,_a3e){
var d;
page=_a2b.byId(page);
if(this.selectedChildWidget!=page){
d=this._transition(page,this.selectedChildWidget,_a3e);
this._set("selectedChildWidget",page);
_a2a.publish(this.id+"-selectChild",page,this._focused);
if(this.persist){
_a25(this.id+"_selectedChild",this.selectedChildWidget.id);
}
}else{
if(this._focused&&true==page.closable){
_a2a.publish(this.id+"-selectChild",page,this._focused);
}
}
return when(d||true);
},_transition:function(_a3f,_a40){
if(_a40){
this._hideChild(_a40);
}
var d=this._showChild(_a3f);
if(_a3f.resize){
if(this.doLayout){
_a3f.resize(this._containerContentBox||this._contentBox);
}else{
_a3f.resize();
}
}
return d;
},_adjacent:function(_a41){
var _a42=this.getChildren();
var _a43=_a24.indexOf(_a42,this.selectedChildWidget);
_a43+=_a41?1:_a42.length-1;
return _a42[_a43%_a42.length];
},forward:function(){
return this.selectChild(this._adjacent(true),true);
},back:function(){
return this.selectChild(this._adjacent(false),true);
},_onKeyDown:function(e){
_a2a.publish(this.id+"-containerKeyDown",{e:e,page:this});
},layout:function(){
var _a44=this.selectedChildWidget;
if(_a44&&_a44.resize){
if(this.doLayout){
_a44.resize(this._containerContentBox||this._contentBox);
}else{
_a44.resize();
}
}
},_showChild:function(page){
var _a45=this.getChildren();
page.isFirstChild=(page==_a45[0]);
page.isLastChild=(page==_a45[_a45.length-1]);
page._set("selected",true);
if(page._wrapper){
_a27.replace(page._wrapper,"dijitVisible","dijitHidden");
}
return (page._onShow&&page._onShow())||true;
},_hideChild:function(page){
page._set("selected",false);
if(page._wrapper){
_a27.replace(page._wrapper,"dijitHidden","dijitVisible");
}
page.onHide&&page.onHide();
},closeChild:function(page){
var _a46=page.onClose&&page.onClose(this,page);
if(_a46){
this.removeChild(page);
page.destroyRecursive();
}
},destroyDescendants:function(_a47){
this._descendantsBeingDestroyed=true;
this.selectedChildWidget=undefined;
_a24.forEach(this.getChildren(),function(_a48){
if(!_a47){
this.removeChild(_a48);
}
_a48.destroyRecursive(_a47);
},this);
this._descendantsBeingDestroyed=false;
}});
_a2f.ChildWidgetProperties={selected:false,disabled:false,closable:false,iconClass:"dijitNoIcon",showTitle:true};
lang.extend(_a2c,_a2f.ChildWidgetProperties);
return _a2f;
});
},"dojo/regexp":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
var _a49={};
lang.setObject("dojo.regexp",_a49);
_a49.escapeString=function(str,_a4a){
return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+\-^])/g,function(ch){
if(_a4a&&_a4a.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
_a49.buildGroupRE=function(arr,re,_a4b){
if(!(arr instanceof Array)){
return re(arr);
}
var b=[];
for(var i=0;i<arr.length;i++){
b.push(re(arr[i]));
}
return _a49.group(b.join("|"),_a4b);
};
_a49.group=function(_a4c,_a4d){
return "("+(_a4d?"?:":"")+_a4c+")";
};
return _a49;
});
},"curam/debug":function(){
define(["curam/define","curam/util/LocalConfig","dojo/ready","dojo/_base/lang","curam/util/ResourceBundle"],function(_a4e,_a4f,_a50,lang,_a51){
var _a52=new _a51("curam.application.Debug");
_a4e.singleton("curam.debug",{log:function(){
if(curam.debug.enabled()){
try{
var a=arguments;
if(!dojo.isIE){
console.log.apply(console,a);
}else{
var _a53=a.length;
var sa=curam.debug._serializeArgument;
switch(_a53){
case 1:
console.log(arguments[0]);
break;
case 2:
console.log(a[0],sa(a[1]));
break;
case 3:
console.log(a[0],sa(a[1]),sa(a[2]));
break;
case 4:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]));
break;
case 5:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]));
break;
case 6:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
break;
default:
console.log("[Incomplete message - "+(_a53-5)+" message a truncated] "+a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
}
}
}
catch(e){
console.log(e);
}
}
},getProperty:function(key,_a54){
return _a52.getProperty(key,_a54);
},_serializeArgument:function(arg){
if(typeof arg!="undefined"&&typeof arg.nodeType!="undefined"&&typeof arg.cloneNode!="undefined"){
return ""+arg;
}else{
if(curam.debug._isWindow(arg)){
return arg.location.href;
}else{
if(curam.debug._isArray(arg)&&curam.debug._isWindow(arg[0])){
return "[array of window objects, length "+arg.length+"]";
}else{
return dojo.toJson(arg);
}
}
}
},_isArray:function(arg){
return typeof arg!="undefined"&&(dojo.isArray(arg)||typeof arg.length!="undefined");
},_isWindow:function(arg){
var _a55=typeof arg!="undefined"&&typeof arg.closed!="undefined"&&arg.closed;
if(_a55){
return true;
}else{
return typeof arg!="undefined"&&typeof arg.location!="undefined"&&typeof arg.navigator!="undefined"&&typeof arg.document!="undefined"&&typeof arg.closed!="undefined";
}
},enabled:function(){
return _a4f.readOption("jsTraceLog","false")=="true";
},_setup:function(_a56){
_a4f.seedOption("jsTraceLog",_a56.trace,"false");
_a4f.seedOption("ajaxDebugMode",_a56.ajaxDebug,"false");
_a4f.seedOption("asyncProgressMonitor",_a56.asyncProgressMonitor,"false");
}});
return curam.debug;
});
},"dijit/DropDownMenu":function(){
define(["dojo/_base/declare","dojo/keys","dojo/text!./templates/Menu.html","./_MenuBase"],function(_a57,keys,_a58,_a59){
return _a57("dijit.DropDownMenu",_a59,{templateString:_a58,baseClass:"dijitMenu",_onUpArrow:function(){
this.focusPrev();
},_onDownArrow:function(){
this.focusNext();
},_onRightArrow:function(evt){
this._moveToPopup(evt);
evt.stopPropagation();
evt.preventDefault();
},_onLeftArrow:function(evt){
if(this.parentMenu){
if(this.parentMenu._isMenuBar){
this.parentMenu.focusPrev();
}else{
this.onCancel(false);
}
}else{
evt.stopPropagation();
evt.preventDefault();
}
}});
});
},"dijit/_AttachMixin":function(){
define(["require","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/mouse","dojo/on","dojo/touch","./_WidgetBase"],function(_a5a,_a5b,_a5c,_a5d,lang,_a5e,on,_a5f,_a60){
var _a61=lang.delegate(_a5f,{"mouseenter":_a5e.enter,"mouseleave":_a5e.leave,"keypress":_a5c._keypress});
var _a62;
var _a63=_a5d("dijit._AttachMixin",null,{constructor:function(){
this._attachPoints=[];
this._attachEvents=[];
},buildRendering:function(){
this.inherited(arguments);
this._attachTemplateNodes(this.domNode);
this._beforeFillContent();
},_beforeFillContent:function(){
},_attachTemplateNodes:function(_a64){
var node=_a64;
while(true){
if(node.nodeType==1&&(this._processTemplateNode(node,function(n,p){
return n.getAttribute(p);
},this._attach)||this.searchContainerNode)&&node.firstChild){
node=node.firstChild;
}else{
if(node==_a64){
return;
}
while(!node.nextSibling){
node=node.parentNode;
if(node==_a64){
return;
}
}
node=node.nextSibling;
}
}
},_processTemplateNode:function(_a65,_a66,_a67){
var ret=true;
var _a68=this.attachScope||this,_a69=_a66(_a65,"dojoAttachPoint")||_a66(_a65,"data-dojo-attach-point");
if(_a69){
var _a6a,_a6b=_a69.split(/\s*,\s*/);
while((_a6a=_a6b.shift())){
if(lang.isArray(_a68[_a6a])){
_a68[_a6a].push(_a65);
}else{
_a68[_a6a]=_a65;
}
ret=(_a6a!="containerNode");
this._attachPoints.push(_a6a);
}
}
var _a6c=_a66(_a65,"dojoAttachEvent")||_a66(_a65,"data-dojo-attach-event");
if(_a6c){
var _a6d,_a6e=_a6c.split(/\s*,\s*/);
var trim=lang.trim;
while((_a6d=_a6e.shift())){
if(_a6d){
var _a6f=null;
if(_a6d.indexOf(":")!=-1){
var _a70=_a6d.split(":");
_a6d=trim(_a70[0]);
_a6f=trim(_a70[1]);
}else{
_a6d=trim(_a6d);
}
if(!_a6f){
_a6f=_a6d;
}
this._attachEvents.push(_a67(_a65,_a6d,lang.hitch(_a68,_a6f)));
}
}
}
return ret;
},_attach:function(node,type,func){
type=type.replace(/^on/,"").toLowerCase();
if(type=="dijitclick"){
type=_a62||(_a62=_a5a("./a11yclick"));
}else{
type=_a61[type]||type;
}
return on(node,type,func);
},_detachTemplateNodes:function(){
var _a71=this.attachScope||this;
_a5b.forEach(this._attachPoints,function(_a72){
delete _a71[_a72];
});
this._attachPoints=[];
_a5b.forEach(this._attachEvents,function(_a73){
_a73.remove();
});
this._attachEvents=[];
},destroyRendering:function(){
this._detachTemplateNodes();
this.inherited(arguments);
}});
lang.extend(_a60,{dojoAttachEvent:"",dojoAttachPoint:""});
return _a63;
});
},"dijit/Menu":function(){
define(["require","dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/sniff","dojo/_base/window","dojo/window","./popup","./DropDownMenu","dojo/ready"],function(_a74,_a75,_a76,dom,_a77,_a78,_a79,keys,lang,on,has,win,_a7a,pm,_a7b,_a7c){
if(1){
_a7c(0,function(){
var _a7d=["dijit/MenuItem","dijit/PopupMenuItem","dijit/CheckedMenuItem","dijit/MenuSeparator"];
_a74(_a7d);
});
}
return _a76("dijit.Menu",_a7b,{constructor:function(){
this._bindings=[];
},targetNodeIds:[],selector:"",contextMenuForWindow:false,leftClickToOpen:false,refocus:true,postCreate:function(){
if(this.contextMenuForWindow){
this.bindDomNode(this.ownerDocumentBody);
}else{
_a75.forEach(this.targetNodeIds,this.bindDomNode,this);
}
this.inherited(arguments);
},_iframeContentWindow:function(_a7e){
return _a7a.get(this._iframeContentDocument(_a7e))||this._iframeContentDocument(_a7e)["__parent__"]||(_a7e.name&&document.frames[_a7e.name])||null;
},_iframeContentDocument:function(_a7f){
return _a7f.contentDocument||(_a7f.contentWindow&&_a7f.contentWindow.document)||(_a7f.name&&document.frames[_a7f.name]&&document.frames[_a7f.name].document)||null;
},bindDomNode:function(node){
node=dom.byId(node,this.ownerDocument);
var cn;
if(node.tagName.toLowerCase()=="iframe"){
var _a80=node,_a81=this._iframeContentWindow(_a80);
cn=win.body(_a81.document);
}else{
cn=(node==win.body(this.ownerDocument)?this.ownerDocument.documentElement:node);
}
var _a82={node:node,iframe:_a80};
_a77.set(node,"_dijitMenu"+this.id,this._bindings.push(_a82));
var _a83=lang.hitch(this,function(cn){
var _a84=this.selector,_a85=_a84?function(_a86){
return on.selector(_a84,_a86);
}:function(_a87){
return _a87;
},self=this;
return [on(cn,_a85(this.leftClickToOpen?"click":"contextmenu"),function(evt){
evt.stopPropagation();
evt.preventDefault();
if((new Date()).getTime()<self._lastKeyDown+500){
return;
}
self._scheduleOpen(this,_a80,{x:evt.pageX,y:evt.pageY},evt.target);
}),on(cn,_a85("keydown"),function(evt){
if(evt.keyCode==93||(evt.shiftKey&&evt.keyCode==keys.F10)||(self.leftClickToOpen&&evt.keyCode==keys.SPACE)){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_a80,null,evt.target);
self._lastKeyDown=(new Date()).getTime();
}
})];
});
_a82.connects=cn?_a83(cn):[];
if(_a80){
_a82.onloadHandler=lang.hitch(this,function(){
var _a88=this._iframeContentWindow(_a80),cn=win.body(_a88.document);
_a82.connects=_a83(cn);
});
if(_a80.addEventListener){
_a80.addEventListener("load",_a82.onloadHandler,false);
}else{
_a80.attachEvent("onload",_a82.onloadHandler);
}
}
},unBindDomNode:function(_a89){
var node;
try{
node=dom.byId(_a89,this.ownerDocument);
}
catch(e){
return;
}
var _a8a="_dijitMenu"+this.id;
if(node&&_a77.has(node,_a8a)){
var bid=_a77.get(node,_a8a)-1,b=this._bindings[bid],h;
while((h=b.connects.pop())){
h.remove();
}
var _a8b=b.iframe;
if(_a8b){
if(_a8b.removeEventListener){
_a8b.removeEventListener("load",b.onloadHandler,false);
}else{
_a8b.detachEvent("onload",b.onloadHandler);
}
}
_a77.remove(node,_a8a);
delete this._bindings[bid];
}
},_scheduleOpen:function(_a8c,_a8d,_a8e,_a8f){
if(!this._openTimer){
this._openTimer=this.defer(function(){
delete this._openTimer;
this._openMyself({target:_a8f,delegatedTarget:_a8c,iframe:_a8d,coords:_a8e});
},1);
}
},_openMyself:function(args){
var _a90=args.target,_a91=args.iframe,_a92=args.coords,_a93=!_a92;
this.currentTarget=args.delegatedTarget;
if(_a92){
if(_a91){
var ifc=_a78.position(_a91,true),_a94=this._iframeContentWindow(_a91),_a95=_a78.docScroll(_a94.document);
var cs=_a79.getComputedStyle(_a91),tp=_a79.toPixelValue,left=(has("ie")&&has("quirks")?0:tp(_a91,cs.paddingLeft))+(has("ie")&&has("quirks")?tp(_a91,cs.borderLeftWidth):0),top=(has("ie")&&has("quirks")?0:tp(_a91,cs.paddingTop))+(has("ie")&&has("quirks")?tp(_a91,cs.borderTopWidth):0);
_a92.x+=ifc.x+left-_a95.x;
_a92.y+=ifc.y+top-_a95.y;
}
}else{
_a92=_a78.position(_a90,true);
_a92.x+=10;
_a92.y+=10;
}
var self=this;
var _a96=this._focusManager.get("prevNode");
var _a97=this._focusManager.get("curNode");
var _a98=!_a97||(dom.isDescendant(_a97,this.domNode))?_a96:_a97;
function _a99(){
if(self.refocus&&_a98){
_a98.focus();
}
pm.close(self);
};
pm.open({popup:this,x:_a92.x,y:_a92.y,onExecute:_a99,onCancel:_a99,orient:this.isLeftToRight()?"L":"R"});
this.focus();
if(!_a93){
this.defer(function(){
this._cleanUp(true);
});
}
this._onBlur=function(){
this.inherited("_onBlur",arguments);
pm.close(this);
};
},destroy:function(){
_a75.forEach(this._bindings,function(b){
if(b){
this.unBindDomNode(b.node);
}
},this);
this.inherited(arguments);
}});
});
},"idx/widget/HoverHelpTooltip":function(){
define(["dojo/_base/declare","dojo/_base/fx","dojo/keys","dojo/_base/array","dojo/dom","dojo/on","dojo/aspect","dojo/when","dojo/Deferred","dojo/dom-attr","dojo/_base/lang","dojo/_base/sniff","dijit/popup","dijit/focus","dojo/_base/event","dojo/dom-geometry","dojo/dom-construct","dojo/dom-class","dijit/registry","dijit/place","dijit/a11y","dojo/dom-style","dojo/_base/window","dijit/_base/manager","dijit/_Widget","dijit/_TemplatedMixin","dijit/Tooltip","dojo/has!dojo-bidi?../bidi/widget/HoverHelpTooltip","dojo/text!./templates/HoverHelpTooltip.html","dijit/dijit","dojo/i18n!./nls/Dialog","dojo/i18n!./nls/HoverHelpTooltip"],function(_a9a,fx,keys,_a9b,dom,on,_a9c,when,_a9d,_a9e,lang,has,_a9f,_aa0,_aa1,_aa2,_aa3,_aa4,_aa5,_aa6,a11y,_aa7,win,_aa8,_aa9,_aaa,_aab,_aac,_aad,_aae,_aaf,_ab0){
var _ab1=lang.getObject("idx.oneui",true);
var _ab2=_a9a("idx.widget.HoverHelpTooltip",_aab,{showDelay:500,hideDelay:800,showLearnMore:false,learnMoreLinkValue:"#updateme",showCloseIcon:true,forceFocus:false,textDir:"auto",_onHover:function(e){
if(!_ab2._showTimer){
var _ab3=e.target;
_ab2._showTimer=setTimeout(lang.hitch(this,function(){
this.open(_ab3);
}),this.showDelay);
}
if(_ab2._hideTimer){
clearTimeout(_ab2._hideTimer);
delete _ab2._hideTimer;
}
},_onUnHover:function(){
if(_ab2._showTimer){
clearTimeout(_ab2._showTimer);
delete _ab2._showTimer;
}
if(!_ab2._hideTimer){
_ab2._hideTimer=setTimeout(lang.hitch(this,function(){
this.close();
}),this.hideDelay);
}
},open:function(_ab4){
if(_ab2._showTimer){
clearTimeout(_ab2._showTimer);
delete _ab2._showTimer;
}
if(!dom.byId(_ab4)){
return;
}
var _ab5=_a9e.get(this.domNode,"aria-label");
_ab2.show(this.content||this.label||this.domNode.innerHTML,_ab4,this.position,!this.isLeftToRight(),this.textDir,this.showLearnMore,this.learnMoreLinkValue,this.showCloseIcon,this.forceFocus,_ab5);
this._connectNode=_ab4;
this.onShow(_ab4,this.position);
},close:function(_ab6){
if(this._connectNode){
var anim=_ab2.hide(this._connectNode,_ab6);
delete this._connectNode;
this.onHide();
if(anim){
var d=new _a9d();
var _ab7=[];
var _ab8=function(){
if(d){
d.resolve();
}
_a9b.forEach(_ab7,function(_ab9){
if(_ab9){
_ab9.remove();
}
});
};
_ab7.push(_a9c.after(anim,"onEnd",_ab8));
_ab7.push(_a9c.after(anim,"onStop",_ab8));
if((anim.status()=="stopped")&&(!d.isResolved())){
_ab8();
}
return d;
}else{
return null;
}
}
if(_ab2._showTimer){
clearTimeout(_ab2._showTimer);
delete _ab2._showTimer;
}
},_setConnectIdAttr:function(_aba){
_a9b.forEach(this._connections||[],function(_abb){
_a9b.forEach(_abb,lang.hitch(this,"disconnect"));
},this);
this._connectIds=_a9b.filter(lang.isArrayLike(_aba)?_aba:(_aba?[_aba]:[]),function(id){
return dom.byId(id);
});
this._connections=_a9b.map(this._connectIds,function(id){
var node=dom.byId(id);
return [this.connect(node,"onmouseenter","_onHover"),this.connect(node,"onmouseleave","_onUnHover"),this.connect(node,"onclick","_onHover"),this.connect(node,"onkeypress","_onConnectIdKey")];
},this);
this._set("connectId",_aba);
},_onConnectIdKey:function(evt){
var node=evt.target;
if(evt.charOrCode==keys.ENTER||evt.charOrCode==keys.SPACE||evt.charOrCode==" "||evt.charOrCode==keys.F1){
_ab2._showTimer=setTimeout(lang.hitch(this,function(){
this.open(node);
}),this.showDelay);
_aa1.stop(evt);
}
}});
var _abc=has("dojo-bidi")?"idx.widget._MasterHoverHelpTooltip_":"idx.widget._MasterHoverHelpTooltip";
var _abd=_a9a(_abc,[_aa9,_aaa],{duration:_aa8.defaultDuration,templateString:_aad,learnMoreLabel:"",draggable:true,_firstFocusItem:null,_lastFocusItem:null,postMixInProperties:function(){
this.learnMoreLabel=_ab0.learnMoreLabel;
this.buttonClose=_aaf.closeButtonLabel;
},postCreate:function(){
win.body().appendChild(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
this.connect(this.domNode,"onkeypress","_onKey");
this.connect(this.domNode,"onmouseenter",lang.hitch(this,function(e){
if(_ab2._hideTimer){
clearTimeout(_ab2._hideTimer);
delete _ab2._hideTimer;
}
if(!this._onDeck){
if(this.forceFocus&&this.showLearnMore){
this.focus();
}
this._keepShowing=true;
if(this._prevState){
lang.mixin(this,this._prevState);
this._prevState=null;
}
this.fadeOut.stop();
this.fadeIn.play();
}else{
this._ignoreMouseLeave=true;
}
}));
this.connect(this.domNode,"onmouseleave",lang.hitch(this,function(e){
this._keepShowing=false;
if(this._ignoreMouseLeave){
delete this._ignoreMouseLeave;
return;
}
_ab2._hideTimer=setTimeout(lang.hitch(this,function(){
this.hide(this.aroundNode);
}),this.hideDelay);
}));
},show:function(_abe,_abf,_ac0,rtl,_ac1,_ac2,_ac3,_ac4,_ac5,_ac6){
this._lastFocusNode=_abf;
if(_ac2){
this.learnMoreNode.style.display="inline";
this.learnMoreNode.href=_ac3;
}else{
this.learnMoreNode.style.display="none";
}
if(_ac4||_ac4==null){
this.closeButtonNode.style.display="inline";
}else{
this.closeButtonNode.style.display="none";
}
this.connectorNode.hidden=false;
if(this.aroundNode&&this.aroundNode===_abf&&this.containerNode.innerHTML==_abe){
return;
}
this.domNode.width="auto";
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.containerNode.innerHTML=_abe;
if(_ac6){
_a9e.set(this.domNode,"aria-label",_ac6);
}
this.set("textDir",_ac1);
this.containerNode.align=rtl?"right":"left";
var pos=_aa6.around(this.domNode,_abf,_ac0&&_ac0.length?_ac0:_ab2.defaultPosition,!rtl,lang.hitch(this,"orient"));
var _ac7=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_ac7.y+((_ac7.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_ac7.x+((_ac7.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}
}
_aa7.set(this.domNode,"opacity",0);
_aa4.add(this.domNode,"dijitPopup");
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_abf;
var _ac8=_a9e.get(_abf,"id");
if(typeof _ac8=="string"){
_aae.setWaiState(this.containerNode,"labelledby",_ac8);
}
if(_ac5){
this.focus();
}
return;
},orient:function(node,_ac9,_aca,_acb,_acc){
this.connectorNode.style.top="";
var _acd=_acb.w-this.connectorNode.offsetWidth;
node.className="idxOneuiHoverHelpTooltip "+{"MR-ML":"idxOneuiHoverHelpTooltipRight","ML-MR":"idxOneuiHoverHelpTooltipLeft","TM-BM":"idxOneuiHoverHelpTooltipAbove","BM-TM":"idxOneuiHoverHelpTooltipBelow","BL-TL":"idxOneuiHoverHelpTooltipBelow idxOneuiHoverHelpTooltipABLeft","TL-BL":"idxOneuiHoverHelpTooltipAbove idxOneuiHoverHelpTooltipABLeft","BR-TR":"idxOneuiHoverHelpTooltipBelow idxOneuiHoverHelpTooltipABRight","TR-BR":"idxOneuiHoverHelpTooltipAbove idxOneuiHoverHelpTooltipABRight","BR-BL":"idxOneuiHoverHelpTooltipRight","BL-BR":"idxOneuiHoverHelpTooltipLeft","TR-TL":"idxOneuiHoverHelpTooltipRight"}[_ac9+"-"+_aca];
this.domNode.style.width="auto";
var size=_aa2.position(this.domNode);
var _ace=Math.min((Math.max(_acd,1)),size.w);
var _acf=_ace<size.w;
this.domNode.style.width=_ace+"px";
if(_acf){
this.containerNode.style.overflow="auto";
var _ad0=this.containerNode.scrollWidth;
this.containerNode.style.overflow="visible";
if(_ad0>_ace){
_ad0=_ad0+_aa7.get(this.domNode,"paddingLeft")+_aa7.get(this.domNode,"paddingRight");
this.domNode.style.width=_ad0+"px";
}
}
if(_aca.charAt(0)=="B"&&_ac9.charAt(0)=="B"){
var mb=_aa2.getMarginBox(node);
var _ad1=this.connectorNode.offsetHeight;
if(mb.h>_acb.h){
var _ad2=_acb.h-((_acc.h+_ad1)>>1);
this.connectorNode.style.top=_ad2+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(_acc.h/2-_ad1/2,0),mb.h-_ad1)+"px";
this.connectorNode.style.top="";
}
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
return Math.max(0,size.w-_acd);
},focus:function(){
if(this._focus){
return;
}
this._getFocusItems(this.outerContainerNode);
this._focus=true;
_aa0.focus(this._firstFocusItem);
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
_a9e.set(this.containerNode,"tabindex","0");
_a9e.set(this.learnMoreNode,"tabindex","0");
_a9e.set(this.closeButtonNode,"tabindex","0");
},hide:function(_ad3,_ad4){
if(this._keepShowing){
this._keepShowing=false;
if(!_ad4){
return;
}
}
if(this._onDeck&&this._onDeck[1]==_ad3){
this._onDeck=null;
}else{
if(this.aroundNode===_ad3){
return this._forceHide();
}
}
},hideOnClickClose:function(){
this._forceHide(true);
},_forceHide:function(_ad5){
if(_ad5&&this._lastFocusNode){
var _ad6=_aa5.getEnclosingWidget(this._lastFocusNode);
if(_ad6&&lang.isFunction(_ad6.refocus)){
_ad6.refocus();
}else{
_aa0.focus(this._lastFocusNode);
}
}
if((this.aroundNode)||(this._lastFocusNode)){
this._prevState={_lastFocusNode:this._lastFocusNode,_firstFocusItem:this._firstFocusItem,_lastFocusItem:this._lastFocusItem,_focus:this._focus,isShowingNow:this.isShowingNow,aroundNode:this.aroundNode};
}
this._lastFocusNode=null;
this._firstFocusItem=null;
this._lastFocusItem=null;
this._focus=false;
this.fadeIn.stop();
this.isShowingNow=false;
this.aroundNode=null;
return this.fadeOut.play();
},_getFocusItems:function(){
if(this._firstFocusItem){
this._firstFocusItem=this.closeButtonNode;
return;
}
var _ad7=a11y._getTabNavigable(this.containerNode),_ad8=_aa7.get(this.learnMoreNode,"display")=="none"?this.closeButtonNode:this.learnMoreNode;
this._firstFocusItem=_ad7.lowest||_ad7.first||_ad8;
this._lastFocusItem=_ad7.last||_ad7.highest||_ad8;
},_onKey:function(evt){
var node=evt.target;
if(evt.charOrCode===keys.TAB){
this._getFocusItems(this.outerContainerNode);
}
var _ad9=(this._firstFocusItem==this._lastFocusItem);
if(evt.charOrCode==keys.ESCAPE){
setTimeout(lang.hitch(this,"hideOnClickClose"),0);
_aa1.stop(evt);
}else{
if(node==this._firstFocusItem&&evt.shiftKey&&evt.charOrCode===keys.TAB){
if(!_ad9){
_aa0.focus(this._lastFocusItem);
}
_aa1.stop(evt);
}else{
if(node==this._lastFocusItem&&evt.charOrCode===keys.TAB&&!evt.shiftKey){
if(!_ad9){
_aa0.focus(this._firstFocusItem);
}
_aa1.stop(evt);
}else{
if(evt.charOrCode===keys.TAB){
evt.stopPropagation();
}
}
}
}
},_onHide:function(){
this._prevState=null;
this.domNode.style.cssText="";
this.containerNode.innerHTML="";
_a9e.remove(this.containerNode,"tabindex");
_a9e.remove(this.learnMoreNode,"tabindex");
_a9e.remove(this.closeButtonNode,"tabindex");
_a9e.remove(this.domNode,"aria-label");
if(this._onDeck){
var args=this._onDeck;
this._onDeck=null;
this.show.apply(this,args);
}
},onBlur:function(){
this._forceHide();
}});
_ab2._MasterHoverHelpTooltip=MasterHoverHelpTooltip=has("dojo-bidi")?_a9a("idx.widget._MasterHoverHelpTooltip",[_abd,_aac]):_abd;
_ab2.show=idx.widget.showHoverHelpTooltip=function(_ada,_adb,_adc,rtl,_add,_ade,_adf,_ae0,_ae1,_ae2){
if(!_ab2._masterTT){
idx.widget._masterTT=_ab2._masterTT=new MasterHoverHelpTooltip();
}
return _ab2._masterTT.show(_ada,_adb,_adc,rtl,_add,_ade,_adf,_ae0,_ae1,_ae2);
};
_ab2.hide=idx.widget.hideHoverHelpTooltip=function(_ae3){
return _ab2._masterTT&&_ab2._masterTT.hide(_ae3);
};
_ab2.defaultPosition=["after-centered","before-centered","below","above"];
_ab1.HoverHelpTooltip=_ab2;
return _ab2;
});
},"curam/util/RuntimeContext":function(){
define(["dojo/_base/declare"],function(_ae4){
var _ae5=_ae4("curam.util.RuntimeContext",null,{_window:null,constructor:function(_ae6){
this._window=_ae6;
},getHref:function(){
return this._window.location.href;
},getPathName:function(){
return this._window.location.pathName;
},contextObject:function(){
return this._window;
}});
return _ae5;
});
},"dijit/_KeyNavContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/_base/kernel","dojo/keys","dojo/_base/lang","./registry","./_Container","./_FocusMixin","./_KeyNavMixin"],function(_ae7,_ae8,_ae9,_aea,keys,lang,_aeb,_aec,_aed,_aee){
return _ae8("dijit._KeyNavContainer",[_aed,_aee,_aec],{connectKeyNavHandlers:function(_aef,_af0){
var _af1=(this._keyNavCodes={});
var prev=lang.hitch(this,"focusPrev");
var next=lang.hitch(this,"focusNext");
_ae7.forEach(_aef,function(code){
_af1[code]=prev;
});
_ae7.forEach(_af0,function(code){
_af1[code]=next;
});
_af1[keys.HOME]=lang.hitch(this,"focusFirstChild");
_af1[keys.END]=lang.hitch(this,"focusLastChild");
},startupKeyNavChildren:function(){
_aea.deprecated("startupKeyNavChildren() call no longer needed","","2.0");
},startup:function(){
this.inherited(arguments);
_ae7.forEach(this.getChildren(),lang.hitch(this,"_startupChild"));
},addChild:function(_af2,_af3){
this.inherited(arguments);
this._startupChild(_af2);
},_startupChild:function(_af4){
_af4.set("tabIndex","-1");
},_getFirst:function(){
var _af5=this.getChildren();
return _af5.length?_af5[0]:null;
},_getLast:function(){
var _af6=this.getChildren();
return _af6.length?_af6[_af6.length-1]:null;
},focusNext:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,1));
},focusPrev:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,-1),true);
},childSelector:function(node){
var node=_aeb.byNode(node);
return node&&node.getParent()==this;
}});
});
},"dijit/layout/utils":function(){
define(["dojo/_base/array","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang"],function(_af7,_af8,_af9,_afa,lang){
function _afb(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
};
function size(_afc,dim){
var _afd=_afc.resize?_afc.resize(dim):_af9.setMarginBox(_afc.domNode,dim);
if(_afc.fakeWidget){
return;
}
if(_afd){
lang.mixin(_afc,_afd);
}else{
lang.mixin(_afc,_af9.getMarginBoxSimple(_afc.domNode));
}
};
var _afe={marginBox2contentBox:function(node,mb){
var cs=_afa.getComputedStyle(node);
var me=_af9.getMarginExtents(node,cs);
var pb=_af9.getPadBorderExtents(node,cs);
return {l:_afa.toPixelValue(node,cs.paddingLeft),t:_afa.toPixelValue(node,cs.paddingTop),w:mb.w-(me.w+pb.w),h:mb.h-(me.h+pb.h)};
},layoutChildren:function(_aff,dim,_b00,_b01,_b02){
dim=lang.mixin({},dim);
_af8.add(_aff,"dijitLayoutContainer");
_b00=_af7.filter(_b00,function(item){
return item.region!="center"&&item.layoutAlign!="client";
}).concat(_af7.filter(_b00,function(item){
return item.region=="center"||item.layoutAlign=="client";
}));
var _b03={};
_af7.forEach(_b00,function(_b04){
var elm=_b04.domNode,pos=(_b04.region||_b04.layoutAlign);
if(!pos){
throw new Error("No region setting for "+_b04.id);
}
var _b05=elm.style;
_b05.left=dim.l+"px";
_b05.top=dim.t+"px";
_b05.position="absolute";
_af8.add(elm,"dijitAlign"+_afb(pos));
var _b06={};
if(_b01&&_b01==_b04.id){
_b06[_b04.region=="top"||_b04.region=="bottom"?"h":"w"]=_b02;
}
if(pos=="leading"){
pos=_b04.isLeftToRight()?"left":"right";
}
if(pos=="trailing"){
pos=_b04.isLeftToRight()?"right":"left";
}
if(pos=="top"||pos=="bottom"){
_b06.w=dim.w;
size(_b04,_b06);
dim.h-=_b04.h;
if(pos=="top"){
dim.t+=_b04.h;
}else{
_b05.top=dim.t+dim.h+"px";
}
}else{
if(pos=="left"||pos=="right"){
_b06.h=dim.h;
size(_b04,_b06);
if(_b04.isSplitter){
_b05.left=dim.l-_b04.w+"px";
dim.w+=_b04.w;
}else{
dim.w-=_b04.w;
}
if(pos=="left"){
if(!_b04.isSplitter){
dim.l+=_b04.w;
}
}else{
if(_b04.isSplitter){
_b05.left=dim.l+dim.w-_b04.w+"px";
dim.l+=_b04.w;
}else{
_b05.left=dim.l+dim.w+"px";
}
}
}else{
if(pos=="client"||pos=="center"){
size(_b04,dim);
}
}
}
_b03[pos]={w:dim.w,h:dim.h};
});
return _b03;
}};
lang.setObject("dijit.layout.utils",_afe);
return _afe;
});
},"dijit/_Contained":function(){
define(["dojo/_base/declare","./registry"],function(_b07,_b08){
return _b07("dijit._Contained",null,{_getSibling:function(_b09){
var p=this.getParent();
return (p&&p._getSiblingOfChild&&p._getSiblingOfChild(this,_b09=="previous"?-1:1))||null;
},getPreviousSibling:function(){
return this._getSibling("previous");
},getNextSibling:function(){
return this._getSibling("next");
},getIndexInParent:function(){
var p=this.getParent();
if(!p||!p.getIndexOfChild){
return -1;
}
return p.getIndexOfChild(this);
}});
});
},"dijit/form/DataList":function(){
define(["dojo/_base/declare","dojo/dom","dojo/_base/lang","dojo/query","dojo/store/Memory","../registry"],function(_b0a,dom,lang,_b0b,_b0c,_b0d){
function _b0e(_b0f){
return {id:_b0f.value,value:_b0f.value,name:lang.trim(_b0f.innerText||_b0f.textContent||"")};
};
return _b0a("dijit.form.DataList",_b0c,{constructor:function(_b10,_b11){
this.domNode=dom.byId(_b11);
lang.mixin(this,_b10);
if(this.id){
_b0d.add(this);
}
this.domNode.style.display="none";
this.inherited(arguments,[{data:_b0b("option",this.domNode).map(_b0e)}]);
},destroy:function(){
_b0d.remove(this.id);
},fetchSelectedItem:function(){
var _b12=_b0b("> option[selected]",this.domNode)[0]||_b0b("> option",this.domNode)[0];
return _b12&&_b0e(_b12);
}});
});
},"dijit/_Container":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","dojo/_base/kernel"],function(_b13,_b14,_b15,_b16){
return _b14("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_b17,_b18){
var _b19=this.containerNode;
if(_b18>0){
_b19=_b19.firstChild;
while(_b18>0){
if(_b19.nodeType==1){
_b18--;
}
_b19=_b19.nextSibling;
}
if(_b19){
_b18="before";
}else{
_b19=this.containerNode;
_b18="last";
}
}
_b15.place(_b17.domNode,_b19,_b18);
if(this._started&&!_b17._started){
_b17.startup();
}
},removeChild:function(_b1a){
if(typeof _b1a=="number"){
_b1a=this.getChildren()[_b1a];
}
if(_b1a){
var node=_b1a.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_b1b,dir){
var _b1c=this.getChildren(),idx=_b13.indexOf(_b1c,_b1b);
return _b1c[idx+dir];
},getIndexOfChild:function(_b1d){
return _b13.indexOf(this.getChildren(),_b1d);
}});
});
},"dijit/form/ValidationTextBox":function(){
define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/i18n","./TextBox","../Tooltip","dojo/text!./templates/ValidationTextBox.html","dojo/i18n!./nls/validate"],function(_b1e,_b1f,lang,i18n,_b20,_b21,_b22){
var _b23=_b1e("dijit.form.ValidationTextBox",_b20,{templateString:_b22,required:false,promptMessage:"",invalidMessage:"$_unset_$",missingMessage:"$_unset_$",message:"",constraints:{},pattern:".*",regExp:"",regExpGen:function(){
},state:"",tooltipPosition:[],_deprecateRegExp:function(attr,_b24){
if(_b24!=_b23.prototype[attr]){
_b1f.deprecated("ValidationTextBox id="+this.id+", set('"+attr+"', ...) is deprecated.  Use set('pattern', ...) instead.","","2.0");
this.set("pattern",_b24);
}
},_setRegExpGenAttr:function(_b25){
this._deprecateRegExp("regExpGen",_b25);
this._set("regExpGen",this._computeRegexp);
},_setRegExpAttr:function(_b26){
this._deprecateRegExp("regExp",_b26);
},_setValueAttr:function(){
this.inherited(arguments);
this._refreshState();
},validator:function(_b27,_b28){
return (new RegExp("^(?:"+this._computeRegexp(_b28)+")"+(this.required?"":"?")+"$")).test(_b27)&&(!this.required||!this._isEmpty(_b27))&&(this._isEmpty(_b27)||this.parse(_b27,_b28)!==undefined);
},_isValidSubset:function(){
return this.textbox.value.search(this._partialre)==0;
},isValid:function(){
return this.validator(this.textbox.value,this.get("constraints"));
},_isEmpty:function(_b29){
return (this.trim?/^\s*$/:/^$/).test(_b29);
},getErrorMessage:function(){
var _b2a=this.invalidMessage=="$_unset_$"?this.messages.invalidMessage:!this.invalidMessage?this.promptMessage:this.invalidMessage;
var _b2b=this.missingMessage=="$_unset_$"?this.messages.missingMessage:!this.missingMessage?_b2a:this.missingMessage;
return (this.required&&this._isEmpty(this.textbox.value))?_b2b:_b2a;
},getPromptMessage:function(){
return this.promptMessage;
},_maskValidSubsetError:true,validate:function(_b2c){
var _b2d="";
var _b2e=this.disabled||this.isValid(_b2c);
if(_b2e){
this._maskValidSubsetError=true;
}
var _b2f=this._isEmpty(this.textbox.value);
var _b30=!_b2e&&_b2c&&this._isValidSubset();
this._set("state",_b2e?"":(((((!this._hasBeenBlurred||_b2c)&&_b2f)||_b30)&&(this._maskValidSubsetError||(_b30&&!this._hasBeenBlurred&&_b2c)))?"Incomplete":"Error"));
this.focusNode.setAttribute("aria-invalid",this.state=="Error"?"true":"false");
if(this.state=="Error"){
this._maskValidSubsetError=_b2c&&_b30;
_b2d=this.getErrorMessage(_b2c);
}else{
if(this.state=="Incomplete"){
_b2d=this.getPromptMessage(_b2c);
this._maskValidSubsetError=!this._hasBeenBlurred||_b2c;
}else{
if(_b2f){
_b2d=this.getPromptMessage(_b2c);
}
}
}
this.set("message",_b2d);
return _b2e;
},displayMessage:function(_b31){
if(_b31&&this.focused){
_b21.show(_b31,this.domNode,this.tooltipPosition,!this.isLeftToRight());
}else{
_b21.hide(this.domNode);
}
},_refreshState:function(){
if(this._created){
this.validate(this.focused);
}
this.inherited(arguments);
},constructor:function(_b32){
this.constraints=lang.clone(this.constraints);
this.baseClass+=" dijitValidationTextBox";
},startup:function(){
this.inherited(arguments);
this._refreshState();
},_setConstraintsAttr:function(_b33){
if(!_b33.locale&&this.lang){
_b33.locale=this.lang;
}
this._set("constraints",_b33);
this._refreshState();
},_setPatternAttr:function(_b34){
this._set("pattern",_b34);
this._refreshState();
},_computeRegexp:function(_b35){
var p=this.pattern;
if(typeof p=="function"){
p=p.call(this,_b35);
}
if(p!=this._lastRegExp){
var _b36="";
this._lastRegExp=p;
if(p!=".*"){
p.replace(/\\.|\[\]|\[.*?[^\\]{1}\]|\{.*?\}|\(\?[=:!]|./g,function(re){
switch(re.charAt(0)){
case "{":
case "+":
case "?":
case "*":
case "^":
case "$":
case "|":
case "(":
_b36+=re;
break;
case ")":
_b36+="|$)";
break;
default:
_b36+="(?:"+re+"|$)";
break;
}
});
}
try{
"".search(_b36);
}
catch(e){
_b36=this.pattern;
console.warn("RegExp error in "+this.declaredClass+": "+this.pattern);
}
this._partialre="^(?:"+_b36+")$";
}
return p;
},postMixInProperties:function(){
this.inherited(arguments);
this.messages=i18n.getLocalization("dijit.form","validate",this.lang);
this._setConstraintsAttr(this.constraints);
},_setDisabledAttr:function(_b37){
this.inherited(arguments);
this._refreshState();
},_setRequiredAttr:function(_b38){
this._set("required",_b38);
this.focusNode.setAttribute("aria-required",_b38);
this._refreshState();
},_setMessageAttr:function(_b39){
this._set("message",_b39);
this.displayMessage(_b39);
},reset:function(){
this._maskValidSubsetError=true;
this.inherited(arguments);
},_onBlur:function(){
this.displayMessage("");
this.inherited(arguments);
},destroy:function(){
_b21.hide(this.domNode);
this.inherited(arguments);
}});
return _b23;
});
},"dijit/selection":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/sniff","dojo/_base/window","dijit/focus"],function(_b3a,dom,lang,has,_b3b,_b3c){
var _b3d=function(win){
var doc=win.document;
this.getType=function(){
if(doc.getSelection){
var _b3e="text";
var oSel;
try{
oSel=win.getSelection();
}
catch(e){
}
if(oSel&&oSel.rangeCount==1){
var _b3f=oSel.getRangeAt(0);
if((_b3f.startContainer==_b3f.endContainer)&&((_b3f.endOffset-_b3f.startOffset)==1)&&(_b3f.startContainer.nodeType!=3)){
_b3e="control";
}
}
return _b3e;
}else{
return doc.selection.type.toLowerCase();
}
};
this.getSelectedText=function(){
if(doc.getSelection){
var _b40=win.getSelection();
return _b40?_b40.toString():"";
}else{
if(this.getType()=="control"){
return null;
}
return doc.selection.createRange().text;
}
};
this.getSelectedHtml=function(){
if(doc.getSelection){
var _b41=win.getSelection();
if(_b41&&_b41.rangeCount){
var i;
var html="";
for(i=0;i<_b41.rangeCount;i++){
var frag=_b41.getRangeAt(i).cloneContents();
var div=doc.createElement("div");
div.appendChild(frag);
html+=div.innerHTML;
}
return html;
}
return null;
}else{
if(this.getType()=="control"){
return null;
}
return doc.selection.createRange().htmlText;
}
};
this.getSelectedElement=function(){
if(this.getType()=="control"){
if(doc.getSelection){
var _b42=win.getSelection();
return _b42.anchorNode.childNodes[_b42.anchorOffset];
}else{
var _b43=doc.selection.createRange();
if(_b43&&_b43.item){
return doc.selection.createRange().item(0);
}
}
}
return null;
};
this.getParentElement=function(){
if(this.getType()=="control"){
var p=this.getSelectedElement();
if(p){
return p.parentNode;
}
}else{
if(doc.getSelection){
var _b44=doc.getSelection();
if(_b44){
var node=_b44.anchorNode;
while(node&&(node.nodeType!=1)){
node=node.parentNode;
}
return node;
}
}else{
var r=doc.selection.createRange();
r.collapse(true);
return r.parentElement();
}
}
return null;
};
this.hasAncestorElement=function(_b45){
return this.getAncestorElement.apply(this,arguments)!=null;
};
this.getAncestorElement=function(_b46){
var node=this.getSelectedElement()||this.getParentElement();
return this.getParentOfType(node,arguments);
};
this.isTag=function(node,tags){
if(node&&node.tagName){
var _b47=node.tagName.toLowerCase();
for(var i=0;i<tags.length;i++){
var _b48=String(tags[i]).toLowerCase();
if(_b47==_b48){
return _b48;
}
}
}
return "";
};
this.getParentOfType=function(node,tags){
while(node){
if(this.isTag(node,tags).length){
return node;
}
node=node.parentNode;
}
return null;
};
this.collapse=function(_b49){
if(doc.getSelection){
var _b4a=win.getSelection();
if(_b4a.removeAllRanges){
if(_b49){
_b4a.collapseToStart();
}else{
_b4a.collapseToEnd();
}
}else{
_b4a.collapse(_b49);
}
}else{
var _b4b=doc.selection.createRange();
_b4b.collapse(_b49);
_b4b.select();
}
};
this.remove=function(){
var sel=doc.selection;
if(doc.getSelection){
sel=win.getSelection();
sel.deleteFromDocument();
return sel;
}else{
if(sel.type.toLowerCase()!="none"){
sel.clear();
}
return sel;
}
};
this.selectElementChildren=function(_b4c,_b4d){
var _b4e;
_b4c=dom.byId(_b4c);
if(doc.getSelection){
var _b4f=win.getSelection();
if(has("opera")){
if(_b4f.rangeCount){
_b4e=_b4f.getRangeAt(0);
}else{
_b4e=doc.createRange();
}
_b4e.setStart(_b4c,0);
_b4e.setEnd(_b4c,(_b4c.nodeType==3)?_b4c.length:_b4c.childNodes.length);
_b4f.addRange(_b4e);
}else{
_b4f.selectAllChildren(_b4c);
}
}else{
_b4e=_b4c.ownerDocument.body.createTextRange();
_b4e.moveToElementText(_b4c);
if(!_b4d){
try{
_b4e.select();
}
catch(e){
}
}
}
};
this.selectElement=function(_b50,_b51){
var _b52;
_b50=dom.byId(_b50);
if(doc.getSelection){
var _b53=doc.getSelection();
_b52=doc.createRange();
if(_b53.removeAllRanges){
if(has("opera")){
if(_b53.getRangeAt(0)){
_b52=_b53.getRangeAt(0);
}
}
_b52.selectNode(_b50);
_b53.removeAllRanges();
_b53.addRange(_b52);
}
}else{
try{
var tg=_b50.tagName?_b50.tagName.toLowerCase():"";
if(tg==="img"||tg==="table"){
_b52=_b3b.body(doc).createControlRange();
}else{
_b52=_b3b.body(doc).createRange();
}
_b52.addElement(_b50);
if(!_b51){
_b52.select();
}
}
catch(e){
this.selectElementChildren(_b50,_b51);
}
}
};
this.inSelection=function(node){
if(node){
var _b54;
var _b55;
if(doc.getSelection){
var sel=win.getSelection();
if(sel&&sel.rangeCount>0){
_b55=sel.getRangeAt(0);
}
if(_b55&&_b55.compareBoundaryPoints&&doc.createRange){
try{
_b54=doc.createRange();
_b54.setStart(node,0);
if(_b55.compareBoundaryPoints(_b55.START_TO_END,_b54)===1){
return true;
}
}
catch(e){
}
}
}else{
_b55=doc.selection.createRange();
try{
_b54=node.ownerDocument.body.createTextRange();
_b54.moveToElementText(node);
}
catch(e2){
}
if(_b55&&_b54){
if(_b55.compareEndPoints("EndToStart",_b54)===1){
return true;
}
}
}
}
return false;
};
this.getBookmark=function(){
var bm,rg,tg,sel=doc.selection,cf=_b3c.curNode;
if(doc.getSelection){
sel=win.getSelection();
if(sel){
if(sel.isCollapsed){
tg=cf?cf.tagName:"";
if(tg){
tg=tg.toLowerCase();
if(tg=="textarea"||(tg=="input"&&(!cf.type||cf.type.toLowerCase()=="text"))){
sel={start:cf.selectionStart,end:cf.selectionEnd,node:cf,pRange:true};
return {isCollapsed:(sel.end<=sel.start),mark:sel};
}
}
bm={isCollapsed:true};
if(sel.rangeCount){
bm.mark=sel.getRangeAt(0).cloneRange();
}
}else{
rg=sel.getRangeAt(0);
bm={isCollapsed:false,mark:rg.cloneRange()};
}
}
}else{
if(sel){
tg=cf?cf.tagName:"";
tg=tg.toLowerCase();
if(cf&&tg&&(tg=="button"||tg=="textarea"||tg=="input")){
if(sel.type&&sel.type.toLowerCase()=="none"){
return {isCollapsed:true,mark:null};
}else{
rg=sel.createRange();
return {isCollapsed:rg.text&&rg.text.length?false:true,mark:{range:rg,pRange:true}};
}
}
bm={};
try{
rg=sel.createRange();
bm.isCollapsed=!(sel.type=="Text"?rg.htmlText.length:rg.length);
}
catch(e){
bm.isCollapsed=true;
return bm;
}
if(sel.type.toUpperCase()=="CONTROL"){
if(rg.length){
bm.mark=[];
var i=0,len=rg.length;
while(i<len){
bm.mark.push(rg.item(i++));
}
}else{
bm.isCollapsed=true;
bm.mark=null;
}
}else{
bm.mark=rg.getBookmark();
}
}else{
console.warn("No idea how to store the current selection for this browser!");
}
}
return bm;
};
this.moveToBookmark=function(_b56){
var mark=_b56.mark;
if(mark){
if(doc.getSelection){
var sel=win.getSelection();
if(sel&&sel.removeAllRanges){
if(mark.pRange){
var n=mark.node;
n.selectionStart=mark.start;
n.selectionEnd=mark.end;
}else{
sel.removeAllRanges();
sel.addRange(mark);
}
}else{
console.warn("No idea how to restore selection for this browser!");
}
}else{
if(doc.selection&&mark){
var rg;
if(mark.pRange){
rg=mark.range;
}else{
if(lang.isArray(mark)){
rg=doc.body.createControlRange();
_b3a.forEach(mark,function(n){
rg.addElement(n);
});
}else{
rg=doc.body.createTextRange();
rg.moveToBookmark(mark);
}
}
rg.select();
}
}
}
};
this.isCollapsed=function(){
return this.getBookmark().isCollapsed;
};
};
var _b57=new _b3d(window);
_b57.SelectionManager=_b3d;
return _b57;
});
},"dijit/_base/typematic":function(){
define(["../typematic"],function(){
});
},"dijit/_base":function(){
define(["./main","./a11y","./WidgetSet","./_base/focus","./_base/manager","./_base/place","./_base/popup","./_base/scroll","./_base/sniff","./_base/typematic","./_base/wai","./_base/window"],function(_b58){
return _b58._base;
});
},"dojo/window":function(){
define(["./_base/lang","./sniff","./_base/window","./dom","./dom-geometry","./dom-style","./dom-construct"],function(lang,has,_b59,dom,geom,_b5a,_b5b){
has.add("rtl-adjust-position-for-verticalScrollBar",function(win,doc){
var body=_b59.body(doc),_b5c=_b5b.create("div",{style:{overflow:"scroll",overflowX:"visible",direction:"rtl",visibility:"hidden",position:"absolute",left:"0",top:"0",width:"64px",height:"64px"}},body,"last"),div=_b5b.create("div",{style:{overflow:"hidden",direction:"ltr"}},_b5c,"last"),ret=geom.position(div).x!=0;
_b5c.removeChild(div);
body.removeChild(_b5c);
return ret;
});
has.add("position-fixed-support",function(win,doc){
var body=_b59.body(doc),_b5d=_b5b.create("span",{style:{visibility:"hidden",position:"fixed",left:"1px",top:"1px"}},body,"last"),_b5e=_b5b.create("span",{style:{position:"fixed",left:"0",top:"0"}},_b5d,"last"),ret=geom.position(_b5e).x!=geom.position(_b5d).x;
_b5d.removeChild(_b5e);
body.removeChild(_b5d);
return ret;
});
var _b5f={getBox:function(doc){
doc=doc||_b59.doc;
var _b60=(doc.compatMode=="BackCompat")?_b59.body(doc):doc.documentElement,_b61=geom.docScroll(doc),w,h;
if(has("touch")){
var _b62=_b5f.get(doc);
w=_b62.innerWidth||_b60.clientWidth;
h=_b62.innerHeight||_b60.clientHeight;
}else{
w=_b60.clientWidth;
h=_b60.clientHeight;
}
return {l:_b61.x,t:_b61.y,w:w,h:h};
},get:function(doc){
if(has("ie")&&_b5f!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
},scrollIntoView:function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_b59.doc,body=_b59.body(doc),html=doc.documentElement||body.parentNode,isIE=has("ie")||has("trident"),isWK=has("webkit");
if(node==body||node==html){
return;
}
if(!(has("mozilla")||isIE||isWK||has("opera")||has("trident")||has("edge"))&&("scrollIntoView" in node)){
node.scrollIntoView(false);
return;
}
var _b63=doc.compatMode=="BackCompat",_b64=Math.min(body.clientWidth||html.clientWidth,html.clientWidth||body.clientWidth),_b65=Math.min(body.clientHeight||html.clientHeight,html.clientHeight||body.clientHeight),_b66=(isWK||_b63)?body:html,_b67=pos||geom.position(node),el=node.parentNode,_b68=function(el){
return (isIE<=6||(isIE==7&&_b63))?false:(has("position-fixed-support")&&(_b5a.get(el,"position").toLowerCase()=="fixed"));
},self=this,_b69=function(el,x,y){
if(el.tagName=="BODY"||el.tagName=="HTML"){
self.get(el.ownerDocument).scrollBy(x,y);
}else{
x&&(el.scrollLeft+=x);
y&&(el.scrollTop+=y);
}
};
if(_b68(node)){
return;
}
while(el){
if(el==body){
el=_b66;
}
var _b6a=geom.position(el),_b6b=_b68(el),rtl=_b5a.getComputedStyle(el).direction.toLowerCase()=="rtl";
if(el==_b66){
_b6a.w=_b64;
_b6a.h=_b65;
if(_b66==html&&(isIE||has("trident"))&&rtl){
_b6a.x+=_b66.offsetWidth-_b6a.w;
}
_b6a.x=0;
_b6a.y=0;
}else{
var pb=geom.getPadBorderExtents(el);
_b6a.w-=pb.w;
_b6a.h-=pb.h;
_b6a.x+=pb.l;
_b6a.y+=pb.t;
var _b6c=el.clientWidth,_b6d=_b6a.w-_b6c;
if(_b6c>0&&_b6d>0){
if(rtl&&has("rtl-adjust-position-for-verticalScrollBar")){
_b6a.x+=_b6d;
}
_b6a.w=_b6c;
}
_b6c=el.clientHeight;
_b6d=_b6a.h-_b6c;
if(_b6c>0&&_b6d>0){
_b6a.h=_b6c;
}
}
if(_b6b){
if(_b6a.y<0){
_b6a.h+=_b6a.y;
_b6a.y=0;
}
if(_b6a.x<0){
_b6a.w+=_b6a.x;
_b6a.x=0;
}
if(_b6a.y+_b6a.h>_b65){
_b6a.h=_b65-_b6a.y;
}
if(_b6a.x+_b6a.w>_b64){
_b6a.w=_b64-_b6a.x;
}
}
var l=_b67.x-_b6a.x,t=_b67.y-_b6a.y,r=l+_b67.w-_b6a.w,bot=t+_b67.h-_b6a.h;
var s,old;
if(r*l>0&&(!!el.scrollLeft||el==_b66||el.scrollWidth>el.offsetHeight)){
s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_b63)||has("trident")>=5)){
s=-s;
}
old=el.scrollLeft;
_b69(el,s,0);
s=el.scrollLeft-old;
_b67.x-=s;
}
if(bot*t>0&&(!!el.scrollTop||el==_b66||el.scrollHeight>el.offsetHeight)){
s=Math.ceil(Math[t<0?"max":"min"](t,bot));
old=el.scrollTop;
_b69(el,0,s);
s=el.scrollTop-old;
_b67.y-=s;
}
el=(el!=_b66)&&!_b6b&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
}};
1&&lang.setObject("dojo.window",_b5f);
return _b5f;
});
},"dijit/_FocusMixin":function(){
define(["./focus","./_WidgetBase","dojo/_base/declare","dojo/_base/lang"],function(_b6e,_b6f,_b70,lang){
lang.extend(_b6f,{focused:false,onFocus:function(){
},onBlur:function(){
},_onFocus:function(){
this.onFocus();
},_onBlur:function(){
this.onBlur();
}});
return _b70("dijit._FocusMixin",null,{_focusManager:_b6e});
});
},"dijit/_WidgetsInTemplateMixin":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/_base/lang","dojo/parser"],function(_b71,_b72,_b73,lang,_b74){
return _b73("dijit._WidgetsInTemplateMixin",null,{_earlyTemplatedStartup:false,widgetsInTemplate:true,contextRequire:null,_beforeFillContent:function(){
if(this.widgetsInTemplate){
var node=this.domNode;
if(this.containerNode&&!this.searchContainerNode){
this.containerNode.stopParser=true;
}
_b74.parse(node,{noStart:!this._earlyTemplatedStartup,template:true,inherited:{dir:this.dir,lang:this.lang,textDir:this.textDir},propsThis:this,contextRequire:this.contextRequire,scope:"dojo"}).then(lang.hitch(this,function(_b75){
this._startupWidgets=_b75;
for(var i=0;i<_b75.length;i++){
this._processTemplateNode(_b75[i],function(n,p){
return n[p];
},function(_b76,type,_b77){
if(type in _b76){
return _b76.connect(_b76,type,_b77);
}else{
return _b76.on(type,_b77,true);
}
});
}
if(this.containerNode&&this.containerNode.stopParser){
delete this.containerNode.stopParser;
}
}));
if(!this._startupWidgets){
throw new Error(this.declaredClass+": parser returned unfilled promise (probably waiting for module auto-load), "+"unsupported by _WidgetsInTemplateMixin.   Must pre-load all supporting widgets before instantiation.");
}
}
},_processTemplateNode:function(_b78,_b79,_b7a){
if(_b79(_b78,"dojoType")||_b79(_b78,"data-dojo-type")){
return true;
}
return this.inherited(arguments);
},startup:function(){
_b71.forEach(this._startupWidgets,function(w){
if(w&&!w._started&&w.startup){
w.startup();
}
});
this._startupWidgets=null;
this.inherited(arguments);
}});
});
},"dijit/form/FilteringSelect":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/when","./MappedTextBox","./ComboBoxMixin"],function(_b7b,lang,when,_b7c,_b7d){
return _b7b("dijit.form.FilteringSelect",[_b7c,_b7d],{required:true,_lastDisplayedValue:"",_isValidSubset:function(){
return this._opened;
},isValid:function(){
return !!this.item||(!this.required&&this.get("displayedValue")=="");
},_refreshState:function(){
if(!this.searchTimer){
this.inherited(arguments);
}
},_callbackSetLabel:function(_b7e,_b7f,_b80,_b81){
if((_b7f&&_b7f[this.searchAttr]!==this._lastQuery)||(!_b7f&&_b7e.length&&this.store.getIdentity(_b7e[0])!=this._lastQuery)){
return;
}
if(!_b7e.length){
this.set("value","",_b81||(_b81===undefined&&!this.focused),this.textbox.value,null);
}else{
this.set("item",_b7e[0],_b81);
}
},_openResultList:function(_b82,_b83,_b84){
if(_b83[this.searchAttr]!==this._lastQuery){
return;
}
this.inherited(arguments);
if(this.item===undefined){
this.validate(true);
}
},_getValueAttr:function(){
return this.valueNode.value;
},_getValueField:function(){
return "value";
},_setValueAttr:function(_b85,_b86,_b87,item){
if(!this._onChangeActive){
_b86=null;
}
if(item===undefined){
if(_b85===null||_b85===""){
_b85="";
if(!lang.isString(_b87)){
this._setDisplayedValueAttr(_b87||"",_b86);
return;
}
}
var self=this;
this._lastQuery=_b85;
when(this.store.get(_b85),function(item){
self._callbackSetLabel(item?[item]:[],undefined,undefined,_b86);
});
}else{
this.valueNode.value=_b85;
this.inherited(arguments,[_b85,_b86,_b87,item]);
}
},_setItemAttr:function(item,_b88,_b89){
this.inherited(arguments);
this._lastDisplayedValue=this.textbox.value;
},_getDisplayQueryString:function(text){
return text.replace(/([\\\*\?])/g,"\\$1");
},_setDisplayedValueAttr:function(_b8a,_b8b){
if(_b8a==null){
_b8a="";
}
if(!this._created){
if(!("displayedValue" in this.params)){
return;
}
_b8b=false;
}
if(this.store){
this.closeDropDown();
var _b8c=lang.clone(this.query);
var qs=this._getDisplayQueryString(_b8a),q;
if(this.store._oldAPI){
q=qs;
}else{
q=this._patternToRegExp(qs);
q.toString=function(){
return qs;
};
}
this._lastQuery=_b8c[this.searchAttr]=q;
this.textbox.value=_b8a;
this._lastDisplayedValue=_b8a;
this._set("displayedValue",_b8a);
var _b8d=this;
var _b8e={queryOptions:{ignoreCase:this.ignoreCase,deep:true}};
lang.mixin(_b8e,this.fetchProperties);
this._fetchHandle=this.store.query(_b8c,_b8e);
when(this._fetchHandle,function(_b8f){
_b8d._fetchHandle=null;
_b8d._callbackSetLabel(_b8f||[],_b8c,_b8e,_b8b);
},function(err){
_b8d._fetchHandle=null;
if(!_b8d._cancelingQuery){
console.error("dijit.form.FilteringSelect: "+err.toString());
}
});
}
},undo:function(){
this.set("displayedValue",this._lastDisplayedValue);
}});
});
},"dijit/form/_ButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom","dojo/has","../registry"],function(_b90,dom,has,_b91){
var _b92=_b90("dijit.form._ButtonMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{label:"",type:"button",__onClick:function(e){
e.stopPropagation();
e.preventDefault();
if(!this.disabled){
this.valueNode.click(e);
}
return false;
},_onClick:function(e){
if(this.disabled){
e.stopPropagation();
e.preventDefault();
return false;
}
if(this.onClick(e)===false){
e.preventDefault();
}
var _b93=e.defaultPrevented;
if(!_b93&&this.type=="submit"&&!(this.valueNode||this.focusNode).form){
for(var node=this.domNode;node.parentNode;node=node.parentNode){
var _b94=_b91.byNode(node);
if(_b94&&typeof _b94._onSubmit=="function"){
_b94._onSubmit(e);
e.preventDefault();
_b93=true;
break;
}
}
}
return !_b93;
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.focusNode,false);
},onClick:function(){
return true;
},_setLabelAttr:function(_b95){
this._set("label",_b95);
var _b96=this.containerNode||this.focusNode;
_b96.innerHTML=_b95;
}});
if(has("dojo-bidi")){
_b92=_b90("dijit.form._ButtonMixin",_b92,{_setLabelAttr:function(){
this.inherited(arguments);
var _b97=this.containerNode||this.focusNode;
this.applyTextDir(_b97);
}});
}
return _b92;
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_b98,win,_b99){
var _b9a={},hash={};
var _b9b={length:0,add:function(_b9c){
if(hash[_b9c.id]){
throw new Error("Tried to register widget with id=="+_b9c.id+" but that id is already registered");
}
hash[_b9c.id]=_b9c;
this.length++;
},remove:function(id){
if(hash[id]){
delete hash[id];
this.length--;
}
},byId:function(id){
return typeof id=="string"?hash[id]:id;
},byNode:function(node){
return hash[node.getAttribute("widgetId")];
},toArray:function(){
var ar=[];
for(var id in hash){
ar.push(hash[id]);
}
return ar;
},getUniqueId:function(_b9d){
var id;
do{
id=_b9d+"_"+(_b9d in _b9a?++_b9a[_b9d]:_b9a[_b9d]=0);
}while(hash[id]);
return _b99._scopeName=="dijit"?id:_b99._scopeName+"_"+id;
},findWidgets:function(root,_b9e){
var _b9f=[];
function _ba0(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _ba1=node.getAttribute("widgetId");
if(_ba1){
var _ba2=hash[_ba1];
if(_ba2){
_b9f.push(_ba2);
}
}else{
if(node!==_b9e){
_ba0(node);
}
}
}
}
};
_ba0(root);
return _b9f;
},_destroyAll:function(){
_b99._curFocus=null;
_b99._prevFocus=null;
_b99._activeStack=[];
_b98.forEach(_b9b.findWidgets(win.body()),function(_ba3){
if(!_ba3._destroyed){
if(_ba3.destroyRecursive){
_ba3.destroyRecursive();
}else{
if(_ba3.destroy){
_ba3.destroy();
}
}
}
});
},getEnclosingWidget:function(node){
while(node){
var id=node.nodeType==1&&node.getAttribute("widgetId");
if(id){
return hash[id];
}
node=node.parentNode;
}
return null;
},_hash:hash};
_b99.registry=_b9b;
return _b9b;
});
},"curam/layout/TabContainer":function(){
define(["dojo/_base/declare","dijit/layout/TabContainer","dojo/text!curam/layout/resources/TabContainer.html","curam/inspection/Layer","dojo/_base/connect","curam/layout/ScrollingTabController","dijit/layout/TabController"],function(_ba4,_ba5,_ba6,_ba7,_ba8,_ba9,_baa){
var _bab=_ba4("curam.layout.TabContainer",dijit.layout.TabContainer,{templateString:_ba6,_theSelectedTabIndex:0,_thePage:null,_theChildren:null,postCreate:function(){
this.inherited(arguments);
var tl=this.tablist;
this.connect(tl,"onRemoveChild","_changeTab");
this.connect(tl,"onSelectChild","updateTabTitle");
_ba7.register("curam/layout/TabContainer",this);
},selectChild:function(page,_bac){
this.inherited(arguments);
dojo.publish("curam/tab/selected",[page.id]);
},updateTabTitle:function(){
curam.util.setBrowserTabTitle();
},_changeTab:function(){
if(this._beingDestroyed){
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren==null){
return;
}
if(this._theChildren[this._theSelectedTabIndex]!=this._thePage){
this.selectChild(this._theChildren[this._theSelectedTabIndex]);
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren.length<1){
this._thePage=null;
return;
}else{
if(this._theChildren.length==1){
this.selectChild(this._theChildren[this._theChildren.length-1]);
this._thePage=null;
this._theChildren=null;
}else{
if(this._theSelectedTabIndex==(this._theChildren.length-1)){
this.selectChild(this._theChildren[this._theChildren.length-2]);
}else{
if(this._theSelectedTabIndex==0){
this.selectChild(this._theChildren[1]);
}else{
if(this._theChildren.length>2){
this.selectChild(this._theChildren[this._theSelectedTabIndex+1]);
}
}
}
this._thePage=null;
this._theChildren=null;
}
}
},removeChild:function(page){
if(this._started&&!this._beingDestroyed){
var _bad=this.getChildren();
var i=0;
var _bae=0;
for(i=0;i<_bad.length;i++){
if(_bad[i].get("selected")){
_bae=i;
break;
}
}
this._theSelectedTabIndex=_bae;
this._thePage=page;
this._theChildren=_bad;
var _baf=page.id;
_ba8.publish("/curam/tab/closing",[_baf]);
}
this.inherited(arguments);
},postMixInProperties:function(){
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_ba9:_baa;
}
this.inherited(arguments);
}});
return _bab;
});
},"dijit/_base/wai":function(){
define(["dojo/dom-attr","dojo/_base/lang","../main","../hccss"],function(_bb0,lang,_bb1){
var _bb2={hasWaiRole:function(elem,role){
var _bb3=this.getWaiRole(elem);
return role?(_bb3.indexOf(role)>-1):(_bb3.length>0);
},getWaiRole:function(elem){
return lang.trim((_bb0.get(elem,"role")||"").replace("wairole:",""));
},setWaiRole:function(elem,role){
_bb0.set(elem,"role",role);
},removeWaiRole:function(elem,role){
var _bb4=_bb0.get(elem,"role");
if(!_bb4){
return;
}
if(role){
var t=lang.trim((" "+_bb4+" ").replace(" "+role+" "," "));
_bb0.set(elem,"role",t);
}else{
elem.removeAttribute("role");
}
},hasWaiState:function(elem,_bb5){
return elem.hasAttribute?elem.hasAttribute("aria-"+_bb5):!!elem.getAttribute("aria-"+_bb5);
},getWaiState:function(elem,_bb6){
return elem.getAttribute("aria-"+_bb6)||"";
},setWaiState:function(elem,_bb7,_bb8){
elem.setAttribute("aria-"+_bb7,_bb8);
},removeWaiState:function(elem,_bb9){
elem.removeAttribute("aria-"+_bb9);
}};
lang.mixin(_bb1,_bb2);
return _bb1;
});
},"curam/util/ResourceBundle":function(){
define(["dojo/_base/declare","dojo/i18n","dojo/string"],function(_bba,i18n,_bbb){
var _bbc=_bba("curam.util.ResourceBundle",null,{_bundle:undefined,constructor:function(_bbd,_bbe){
var _bbf=_bbd.split(".");
var _bc0=_bbf[_bbf.length-1];
var _bc1=_bbf.length==1?"curam.application":_bbd.slice(0,_bbd.length-_bc0.length-1);
try{
var b=i18n.getLocalization(_bc1,_bc0,_bbe);
if(this._isEmpty(b)){
throw new Error("Empty resource bundle.");
}else{
this._bundle=b;
}
}
catch(e){
throw new Error("Unable to access resource bundle: "+_bc1+"."+_bc0+": "+e.message);
}
},_isEmpty:function(_bc2){
for(var prop in _bc2){
return false;
}
return true;
},getProperty:function(key,_bc3){
var msg=this._bundle[key];
var _bc4=msg;
if(_bc3){
_bc4=_bbb.substitute(msg,_bc3);
}
return _bc4;
}});
return _bbc;
});
},"dijit/_KeyNavMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","dijit/registry","dijit/_FocusMixin"],function(_bc5,_bc6,_bc7,keys,lang,on,_bc8,_bc9){
return _bc6("dijit._KeyNavMixin",_bc9,{tabIndex:"0",childSelector:null,postCreate:function(){
this.inherited(arguments);
_bc7.set(this.domNode,"tabIndex",this.tabIndex);
if(!this._keyNavCodes){
var _bca=this._keyNavCodes={};
_bca[keys.HOME]=lang.hitch(this,"focusFirstChild");
_bca[keys.END]=lang.hitch(this,"focusLastChild");
_bca[this.isLeftToRight()?keys.LEFT_ARROW:keys.RIGHT_ARROW]=lang.hitch(this,"_onLeftArrow");
_bca[this.isLeftToRight()?keys.RIGHT_ARROW:keys.LEFT_ARROW]=lang.hitch(this,"_onRightArrow");
_bca[keys.UP_ARROW]=lang.hitch(this,"_onUpArrow");
_bca[keys.DOWN_ARROW]=lang.hitch(this,"_onDownArrow");
}
var self=this,_bcb=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.domNode,"keypress",lang.hitch(this,"_onContainerKeypress")),on(this.domNode,"keydown",lang.hitch(this,"_onContainerKeydown")),on(this.domNode,"focus",lang.hitch(this,"_onContainerFocus")),on(this.containerNode,on.selector(_bcb,"focusin"),function(evt){
self._onChildFocus(_bc8.getEnclosingWidget(this),evt);
}));
},_onLeftArrow:function(){
},_onRightArrow:function(){
},_onUpArrow:function(){
},_onDownArrow:function(){
},focus:function(){
this.focusFirstChild();
},_getFirstFocusableChild:function(){
return this._getNextFocusableChild(null,1);
},_getLastFocusableChild:function(){
return this._getNextFocusableChild(null,-1);
},focusFirstChild:function(){
this.focusChild(this._getFirstFocusableChild());
},focusLastChild:function(){
this.focusChild(this._getLastFocusableChild());
},focusChild:function(_bcc,last){
if(!_bcc){
return;
}
if(this.focusedChild&&_bcc!==this.focusedChild){
this._onChildBlur(this.focusedChild);
}
_bcc.set("tabIndex",this.tabIndex);
_bcc.focus(last?"end":"start");
},_onContainerFocus:function(evt){
if(evt.target!==this.domNode||this.focusedChild){
return;
}
this.focus();
},_onFocus:function(){
_bc7.set(this.domNode,"tabIndex","-1");
this.inherited(arguments);
},_onBlur:function(evt){
_bc7.set(this.domNode,"tabIndex",this.tabIndex);
if(this.focusedChild){
this.focusedChild.set("tabIndex","-1");
this.lastFocusedChild=this.focusedChild;
this._set("focusedChild",null);
}
this.inherited(arguments);
},_onChildFocus:function(_bcd){
if(_bcd&&_bcd!=this.focusedChild){
if(this.focusedChild&&!this.focusedChild._destroyed){
this.focusedChild.set("tabIndex","-1");
}
_bcd.set("tabIndex",this.tabIndex);
this.lastFocused=_bcd;
this._set("focusedChild",_bcd);
}
},_searchString:"",multiCharSearchDuration:1000,onKeyboardSearch:function(item,evt,_bce,_bcf){
if(item){
this.focusChild(item);
}
},_keyboardSearchCompare:function(item,_bd0){
var _bd1=item.domNode,text=item.label||(_bd1.focusNode?_bd1.focusNode.label:"")||_bd1.innerText||_bd1.textContent||"",_bd2=text.replace(/^\s+/,"").substr(0,_bd0.length).toLowerCase();
return (!!_bd0.length&&_bd2==_bd0)?-1:0;
},_onContainerKeydown:function(evt){
var func=this._keyNavCodes[evt.keyCode];
if(func){
func(evt,this.focusedChild);
evt.stopPropagation();
evt.preventDefault();
this._searchString="";
}else{
if(evt.keyCode==keys.SPACE&&this._searchTimer&&!(evt.ctrlKey||evt.altKey||evt.metaKey)){
evt.stopImmediatePropagation();
evt.preventDefault();
this._keyboardSearch(evt," ");
}
}
},_onContainerKeypress:function(evt){
if(evt.charCode<=keys.SPACE||evt.ctrlKey||evt.altKey||evt.metaKey){
return;
}
evt.preventDefault();
evt.stopPropagation();
this._keyboardSearch(evt,String.fromCharCode(evt.charCode).toLowerCase());
},_keyboardSearch:function(evt,_bd3){
var _bd4=null,_bd5,_bd6=0,_bd7=lang.hitch(this,function(){
if(this._searchTimer){
this._searchTimer.remove();
}
this._searchString+=_bd3;
var _bd8=/^(.)\1*$/.test(this._searchString);
var _bd9=_bd8?1:this._searchString.length;
_bd5=this._searchString.substr(0,_bd9);
this._searchTimer=this.defer(function(){
this._searchTimer=null;
this._searchString="";
},this.multiCharSearchDuration);
var _bda=this.focusedChild||null;
if(_bd9==1||!_bda){
_bda=this._getNextFocusableChild(_bda,1);
if(!_bda){
return;
}
}
var stop=_bda;
do{
var rc=this._keyboardSearchCompare(_bda,_bd5);
if(!!rc&&_bd6++==0){
_bd4=_bda;
}
if(rc==-1){
_bd6=-1;
break;
}
_bda=this._getNextFocusableChild(_bda,1);
}while(_bda&&_bda!=stop);
});
_bd7();
this.onKeyboardSearch(_bd4,evt,_bd5,_bd6);
},_onChildBlur:function(){
},_getNextFocusableChild:function(_bdb,dir){
var _bdc=_bdb;
do{
if(!_bdb){
_bdb=this[dir>0?"_getFirst":"_getLast"]();
if(!_bdb){
break;
}
}else{
_bdb=this._getNext(_bdb,dir);
}
if(_bdb!=null&&_bdb!=_bdc&&_bdb.isFocusable()){
return _bdb;
}
}while(_bdb!=_bdc);
return null;
},_getFirst:function(){
return null;
},_getLast:function(){
return null;
},_getNext:function(_bdd,dir){
if(_bdd){
_bdd=_bdd.domNode;
while(_bdd){
_bdd=_bdd[dir<0?"previousSibling":"nextSibling"];
if(_bdd&&"getAttribute" in _bdd){
var w=_bc8.byNode(_bdd);
if(w){
return w;
}
}
}
}
return null;
}});
});
},"dojo/store/util/QueryResults":function(){
define(["../../_base/array","../../_base/lang","../../when"],function(_bde,lang,when){
var _bdf=function(_be0){
if(!_be0){
return _be0;
}
var _be1=!!_be0.then;
if(_be1){
_be0=lang.delegate(_be0);
}
function _be2(_be3){
_be0[_be3]=function(){
var args=arguments;
var _be4=when(_be0,function(_be5){
Array.prototype.unshift.call(args,_be5);
return _bdf(_bde[_be3].apply(_bde,args));
});
if(_be3!=="forEach"||_be1){
return _be4;
}
};
};
_be2("forEach");
_be2("filter");
_be2("map");
if(_be0.total==null){
_be0.total=when(_be0,function(_be6){
return _be6.length;
});
}
return _be0;
};
lang.setObject("dojo.store.util.QueryResults",_bdf);
return _bdf;
});
},"dijit/form/_ListBase":function(){
define(["dojo/_base/declare","dojo/on","dojo/window"],function(_be7,on,_be8){
return _be7("dijit.form._ListBase",null,{selected:null,_listConnect:function(_be9,_bea){
var self=this;
return self.own(on(self.containerNode,on.selector(function(_beb,_bec,_bed){
return _beb.parentNode==_bed;
},_be9),function(evt){
self[_bea](evt,this);
}));
},selectFirstNode:function(){
var _bee=this.containerNode.firstChild;
while(_bee&&_bee.style.display=="none"){
_bee=_bee.nextSibling;
}
this._setSelectedAttr(_bee,true);
},selectLastNode:function(){
var last=this.containerNode.lastChild;
while(last&&last.style.display=="none"){
last=last.previousSibling;
}
this._setSelectedAttr(last,true);
},selectNextNode:function(){
var _bef=this.selected;
if(!_bef){
this.selectFirstNode();
}else{
var next=_bef.nextSibling;
while(next&&next.style.display=="none"){
next=next.nextSibling;
}
if(!next){
this.selectFirstNode();
}else{
this._setSelectedAttr(next,true);
}
}
},selectPreviousNode:function(){
var _bf0=this.selected;
if(!_bf0){
this.selectLastNode();
}else{
var prev=_bf0.previousSibling;
while(prev&&prev.style.display=="none"){
prev=prev.previousSibling;
}
if(!prev){
this.selectLastNode();
}else{
this._setSelectedAttr(prev,true);
}
}
},_setSelectedAttr:function(node,_bf1){
if(this.selected!=node){
var _bf2=this.selected;
if(_bf2){
this.onDeselect(_bf2);
}
if(node){
if(_bf1){
_be8.scrollIntoView(node);
}
this.onSelect(node);
}
this._set("selected",node);
}else{
if(node){
this.onSelect(node);
}
}
}});
});
},"dijit/form/_FormWidget":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/_base/kernel","dojo/ready","../_Widget","../_CssStateMixin","../_TemplatedMixin","./_FormWidgetMixin"],function(_bf3,has,_bf4,_bf5,_bf6,_bf7,_bf8,_bf9){
if(1){
_bf5(0,function(){
var _bfa=["dijit/form/_FormValueWidget"];
require(_bfa);
});
}
return _bf3("dijit.form._FormWidget",[_bf6,_bf8,_bf7,_bf9],{setDisabled:function(_bfb){
_bf4.deprecated("setDisabled("+_bfb+") is deprecated. Use set('disabled',"+_bfb+") instead.","","2.0");
this.set("disabled",_bfb);
},setValue:function(_bfc){
_bf4.deprecated("dijit.form._FormWidget:setValue("+_bfc+") is deprecated.  Use set('value',"+_bfc+") instead.","","2.0");
this.set("value",_bfc);
},getValue:function(){
_bf4.deprecated(this.declaredClass+"::getValue() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},postMixInProperties:function(){
this.nameAttrSetting=(this.name&&!has("msapp"))?("name=\""+this.name.replace(/"/g,"&quot;")+"\""):"";
this.inherited(arguments);
}});
});
},"dijit/Viewport":function(){
define(["dojo/Evented","dojo/on","dojo/domReady","dojo/sniff","dojo/window"],function(_bfd,on,_bfe,has,_bff){
var _c00=new _bfd();
var _c01;
_bfe(function(){
var _c02=_bff.getBox();
_c00._rlh=on(window,"resize",function(){
var _c03=_bff.getBox();
if(_c02.h==_c03.h&&_c02.w==_c03.w){
return;
}
_c02=_c03;
_c00.emit("resize");
});
if(has("ie")==8){
var _c04=screen.deviceXDPI;
setInterval(function(){
if(screen.deviceXDPI!=_c04){
_c04=screen.deviceXDPI;
_c00.emit("resize");
}
},500);
}
if(has("ios")){
on(document,"focusin",function(evt){
_c01=evt.target;
});
on(document,"focusout",function(evt){
_c01=null;
});
}
});
_c00.getEffectiveBox=function(doc){
var box=_bff.getBox(doc);
var tag=_c01&&_c01.tagName&&_c01.tagName.toLowerCase();
if(has("ios")&&_c01&&!_c01.readOnly&&(tag=="textarea"||(tag=="input"&&/^(color|email|number|password|search|tel|text|url)$/.test(_c01.type)))){
box.h*=(orientation==0||orientation==180?0.66:0.4);
var rect=_c01.getBoundingClientRect();
box.h=Math.max(box.h,rect.top+rect.height);
}
return box;
};
return _c00;
});
},"curam/widget/MenuItem":function(){
define(["dijit/MenuItem","dojo/_base/declare","dojo/text!dijit/templates/MenuItem.html"],function(_c05,_c06,_c07){
var _c08=_c06("curam.widget.MenuItem",_c05,{templateString:_c07,onClickValue:"",_onClickAll:function(evt){
var _c09=curam.tab.getTabContainer();
var _c0a=_c09.getChildren();
for(var i=0;i<_c0a.length;i++){
if(_c0a[i].closable){
_c09.closeChild(_c0a[i]);
}
}
},_setLabelAttr:function(val){
var _c0b=/<[a-zA-Z\/][^>]*>/g;
var _c0c=val.replace(_c0b,"");
this._set("label",_c0c);
var _c0d="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_c0d=val.charAt(ndx+1);
var _c0e=val.substr(0,ndx);
var _c0f=val.substr(ndx+3);
text=_c0e+_c0d+_c0f;
val=_c0e+"<span class=\"dijitMenuItemShortcutKey\">"+_c0d+"</span>"+_c0f;
}else{
text=val;
}
text=text.replace(_c0b,"");
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_c0d);
}});
return _c08;
});
},"curam/tab":function(){
define(["dijit/registry","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-class","curam/inspection/Layer","curam/define","curam/util","curam/util/ScreenContext"],function(_c10,dom,_c11,_c12,_c13,_c14){
curam.define.singleton("curam.tab",{SECTION_TAB_CONTAINER_ID:"app-sections-container-dc",SMART_PANEL_IFRAME_ID:"curam_tab_SmartPanelIframe",toBeExecutedOnTabClose:[],_mockSelectedTab:null,getSelectedTab:function(_c15){
if(curam.tab._mockSelectedTab){
return curam.tab._mockSelectedTab;
}
if(curam.tab.getTabContainer(_c15)){
return curam.tab.getTabContainer(_c15).selectedChildWidget;
}
},getTabContainer:function(_c16){
return curam.tab.getTabContainerFromSectionID(_c16||curam.tab.getCurrentSectionId());
},getCurrentSectionId:function(_c17){
var _c18=curam.util.getTopmostWindow().dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
if(_c18){
var _c19=_c18.selectedChildWidget.domNode.id;
return _c19.substring(0,_c19.length-4);
}else{
if(!_c17){
throw new Error("curam.tab.getCurrentSectionId() - application section"+" tab container not found");
}
}
return null;
},inTabbedUI:function(){
return curam.tab.getCurrentSectionId(true)!=null;
},getTabContainerFromSectionID:function(_c1a){
var _c1b=_c10.byId(_c1a+"-stc");
if(!_c1b&&window.parent&&window.parent!=window){
_c1b=curam.util.getTopmostWindow().dijit.byId(_c1a+"-stc");
}
return _c1b;
},getTabWidgetId:function(tab){
return tab.id;
},getSelectedTabWidgetId:function(){
return curam.tab.getTabWidgetId(curam.tab.getSelectedTab());
},getContainerTab:function(node){
var _c1c=dijit.getEnclosingWidget(node);
if(_c1c&&!_c1c.tabDescriptor){
_c1c=curam.tab.getContainerTab(_c1c.domNode.parentNode);
}
if(!_c1c||!_c1c.tabDescriptor){
throw "Containing tab widget could not be found for node: "+node;
}
return _c1c;
},getContentPanelIframe:function(tab){
var _c1d=tab?tab:curam.tab.getSelectedTab(),_c1e=null;
if(_c1d){
_c1e=dojo.query("iframe",_c1d.domNode).filter(function(item){
return _c11.get(item,"iscpiframe")=="true";
})[0];
}
return _c1e?_c1e:null;
},refreshMainContentPanel:function(tab){
var _c1f=tab?tab:curam.tab.getSelectedTab();
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_c1f.domNode]);
var _c20=curam.tab.getContentPanelIframe(tab);
_c20.contentWindow.curam.util.publishRefreshEvent();
_c20.contentWindow.location.reload(false);
},getSmartPanelIframe:function(tab){
var _c21=tab?tab:curam.tab.getSelectedTab();
var _c22=dojo.query("iframe",_c21.domNode).filter(function(item){
return item.id==curam.tab.SMART_PANEL_IFRAME_ID;
})[0];
return _c22;
},unsubscribeOnTabClose:function(_c23,_c24){
curam.tab.toBeExecutedOnTabClose.push(function(_c25){
if(_c24==_c25){
dojo.unsubscribe(_c23);
return true;
}
return false;
});
},executeOnTabClose:function(func,_c26){
curam.tab.toBeExecutedOnTabClose.push(function(_c27){
if(_c26==_c27){
func();
return true;
}
return false;
});
},doExecuteOnTabClose:function(_c28){
var _c29=new Array();
for(var i=0;i<curam.tab.toBeExecutedOnTabClose.length;i++){
var func=curam.tab.toBeExecutedOnTabClose[i];
if(!func(_c28)){
_c29.push(func);
}
}
curam.tab.toBeExecutedOnTabClose=_c29;
},getHandlerForTab:function(_c2a,_c2b){
return function(_c2c,_c2d){
if(_c2d==_c2b){
_c2a(_c2c,_c2b);
}else{
}
};
},getTabController:function(){
return curam.util.getTopmostWindow().curam.ui.UIController;
},initTabLinks:function(_c2e){
dojo.query("a").forEach(function(link){
if(link.href.indexOf("#")!=0&&link.href.indexOf("javascript:")!=0&&(link.href.indexOf("Page.do")>-1||link.href.indexOf("Frame.do")>-1)){
if(link.href.indexOf("&o3ctx")<0&&link.href.indexOf("?o3ctx")<0){
var _c2f=(link.href.indexOf("?")>-1)?"&":"?";
link.href+=_c2f+jsScreenContext.toRequestString();
}
}
});
elements=document.forms;
for(var i=0;i<elements.length;++i){
elem=elements[i];
var _c30=dom.byId("o3ctx");
if(!_c30){
var ctx=new curam.util.ScreenContext();
ctx.setContextBits("ACTION");
_c12.create("input",{"type":"hidden","name":"o3ctx","value":ctx.getValue()},elem);
}
_c12.create("input",{"type":"hidden","name":"o3prv","value":jsPageID},elem);
}
if(elements.length>0){
curam.util.getTopmostWindow().dojo.publish("curam.fireNextRequest",[]);
}
},initContent:function(_c31,_c32){
var _c33=dom.byId("content");
_c13.remove(_c33,"hidden-panel");
return;
},setupSectionSelectionListener:function(){
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",curam.tab.onSectionSelected);
},onSectionSelected:function(_c34){
if(_c34.curamDefaultPageID){
var _c35;
if(_c34.id.substring(_c34.id.length-4,_c34.id.length)=="-sbc"){
var _c36=_c34.id.substring(0,_c34.id.length-4);
_c35=curam.tab.getTabContainer(_c36);
}else{
_c35=_c34;
}
if(_c35&&_c35.getChildren().length==0){
curam.tab.getTabController().handleUIMPageID(_c34.curamDefaultPageID,true);
}
return true;
}
return false;
},setSectionDefaultPage:function(_c37,_c38){
var _c39=_c10.byId(_c37);
if(_c39){
_c39.curamDefaultPageID=_c38;
}else{
throw "curam.tab.setSectionDefaultPage() - cannot find section dijit ID:"+_c37;
}
},publishSmartPanelContentReady:function(){
var _c3a="smartpanel.content.loaded";
var _c3b=window.frameElement;
_c3b.setAttribute("_SPContentLoaded","true");
curam.util.getTopmostWindow().dojo.publish(_c3a,[_c3b]);
}});
_c14.register("curam/tab",curam.tab);
return curam.tab;
});
},"dijit/_base/place":function(){
define(["dojo/_base/array","dojo/_base/lang","dojo/window","../place","../main"],function(_c3c,lang,_c3d,_c3e,_c3f){
var _c40={};
_c40.getViewport=function(){
return _c3d.getBox();
};
_c40.placeOnScreen=_c3e.at;
_c40.placeOnScreenAroundElement=function(node,_c41,_c42,_c43){
var _c44;
if(lang.isArray(_c42)){
_c44=_c42;
}else{
_c44=[];
for(var key in _c42){
_c44.push({aroundCorner:key,corner:_c42[key]});
}
}
return _c3e.around(node,_c41,_c44,true,_c43);
};
_c40.placeOnScreenAroundNode=_c40.placeOnScreenAroundElement;
_c40.placeOnScreenAroundRectangle=_c40.placeOnScreenAroundElement;
_c40.getPopupAroundAlignment=function(_c45,_c46){
var _c47={};
_c3c.forEach(_c45,function(pos){
var ltr=_c46;
switch(pos){
case "after":
_c47[_c46?"BR":"BL"]=_c46?"BL":"BR";
break;
case "before":
_c47[_c46?"BL":"BR"]=_c46?"BR":"BL";
break;
case "below-alt":
ltr=!ltr;
case "below":
_c47[ltr?"BL":"BR"]=ltr?"TL":"TR";
_c47[ltr?"BR":"BL"]=ltr?"TR":"TL";
break;
case "above-alt":
ltr=!ltr;
case "above":
default:
_c47[ltr?"TL":"TR"]=ltr?"BL":"BR";
_c47[ltr?"TR":"TL"]=ltr?"BR":"BL";
break;
}
});
return _c47;
};
lang.mixin(_c3f,_c40);
return _c3f;
});
},"dijit/form/_ComboBoxMenu":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/keys","../_WidgetBase","../_TemplatedMixin","./_ComboBoxMenuMixin","./_ListMouseMixin"],function(_c48,_c49,_c4a,keys,_c4b,_c4c,_c4d,_c4e){
return _c48("dijit.form._ComboBoxMenu",[_c4b,_c4c,_c4e,_c4d],{templateString:"<div class='dijitReset dijitMenu' data-dojo-attach-point='containerNode' style='overflow: auto; overflow-x: hidden;' role='listbox'>"+"<div class='dijitMenuItem dijitMenuPreviousButton' data-dojo-attach-point='previousButton' role='option'></div>"+"<div class='dijitMenuItem dijitMenuNextButton' data-dojo-attach-point='nextButton' role='option'></div>"+"</div>",baseClass:"dijitComboBoxMenu",postCreate:function(){
this.inherited(arguments);
if(!this.isLeftToRight()){
_c49.add(this.previousButton,"dijitMenuItemRtl");
_c49.add(this.nextButton,"dijitMenuItemRtl");
}
this.containerNode.setAttribute("role","listbox");
},_createMenuItem:function(){
var item=this.ownerDocument.createElement("div");
item.className="dijitReset dijitMenuItem"+(this.isLeftToRight()?"":" dijitMenuItemRtl");
item.setAttribute("role","option");
return item;
},onHover:function(node){
_c49.add(node,"dijitMenuItemHover");
},onUnhover:function(node){
_c49.remove(node,"dijitMenuItemHover");
},onSelect:function(node){
_c49.add(node,"dijitMenuItemSelected");
},onDeselect:function(node){
_c49.remove(node,"dijitMenuItemSelected");
},_page:function(up){
var _c4f=0;
var _c50=this.domNode.scrollTop;
var _c51=_c4a.get(this.domNode,"height");
if(!this.getHighlightedOption()){
this.selectNextNode();
}
while(_c4f<_c51){
var _c52=this.getHighlightedOption();
if(up){
if(!_c52.previousSibling||_c52.previousSibling.style.display=="none"){
break;
}
this.selectPreviousNode();
}else{
if(!_c52.nextSibling||_c52.nextSibling.style.display=="none"){
break;
}
this.selectNextNode();
}
var _c53=this.domNode.scrollTop;
_c4f+=(_c53-_c50)*(up?-1:1);
_c50=_c53;
}
},handleKey:function(evt){
switch(evt.keyCode){
case keys.DOWN_ARROW:
this.selectNextNode();
return false;
case keys.PAGE_DOWN:
this._page(false);
return false;
case keys.UP_ARROW:
this.selectPreviousNode();
return false;
case keys.PAGE_UP:
this._page(true);
return false;
default:
return true;
}
}});
});
},"cm/_base/_dom":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class"],function(dom,_c54,_c55){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(node,_c56){
return cm._findSibling(node,_c56,true);
},prevSibling:function(node,_c57){
return cm._findSibling(node,_c57,false);
},getInput:function(name,_c58){
if(!dojo.isString(name)){
return name;
}
var _c59=dojo.query("input[name='"+name+"'],select[name='"+name+"']");
return _c58?(_c59.length>0?_c59:null):(_c59.length>0?_c59[0]:null);
},getParentByClass:function(node,_c5a){
node=node.parentNode;
while(node){
if(_c55.contains(node,_c5a)){
return node;
}
node=node.parentNode;
}
return null;
},getParentByType:function(node,type){
node=node.parentNode;
type=type.toLowerCase();
var _c5b="html";
while(node){
if(node.tagName.toLowerCase()==_c5b){
break;
}
if(node.tagName.toLowerCase()==type){
return node;
}
node=node.parentNode;
}
return null;
},replaceClass:function(node,_c5c,_c5d){
_c55.remove(node,_c5d);
_c55.add(node,_c5c);
},setClass:function(node,_c5e){
node=dom.byId(node);
var cs=new String(_c5e);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_c5e);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
},_findSibling:function(node,_c5f,_c60){
if(!node){
return null;
}
if(_c5f){
_c5f=_c5f.toLowerCase();
}
var _c61=_c60?"nextSibling":"previousSibling";
do{
node=node[_c61];
}while(node&&node.nodeType!=1);
if(node&&_c5f&&_c5f!=node.tagName.toLowerCase()){
return cm[_c60?"nextSibling":"prevSibling"](node,_c5f);
}
return node;
},getViewport:function(){
var d=dojo.doc,dd=d.documentElement,w=window,b=dojo.body();
if(dojo.isMozilla){
return {w:dd.clientWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&w.innerWidth){
return {w:w.innerWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&dd&&dd.clientWidth){
return {w:dd.clientWidth,h:dd.clientHeight};
}else{
if(b.clientWidth){
return {w:b.clientWidth,h:b.clientHeight};
}
}
}
}
return null;
},toggleDisplay:function(node){
_c54.set(node,"display",_c54.get(node,"display")=="none"?"":"none");
},endsWith:function(str,end,_c62){
if(_c62){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_c54.set(n,"display","none");
},show:function(n){
_c54.set(n,"display","");
}});
return cm;
});
},"dijit/_base/focus":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/topic","dojo/_base/window","../focus","../selection","../main"],function(_c63,dom,lang,_c64,win,_c65,_c66,_c67){
var _c68={_curFocus:null,_prevFocus:null,isCollapsed:function(){
return _c67.getBookmark().isCollapsed;
},getBookmark:function(){
var sel=win.global==window?_c66:new _c66.SelectionManager(win.global);
return sel.getBookmark();
},moveToBookmark:function(_c69){
var sel=win.global==window?_c66:new _c66.SelectionManager(win.global);
return sel.moveToBookmark(_c69);
},getFocus:function(menu,_c6a){
var node=!_c65.curNode||(menu&&dom.isDescendant(_c65.curNode,menu.domNode))?_c67._prevFocus:_c65.curNode;
return {node:node,bookmark:node&&(node==_c65.curNode)&&win.withGlobal(_c6a||win.global,_c67.getBookmark),openedForWindow:_c6a};
},_activeStack:[],registerIframe:function(_c6b){
return _c65.registerIframe(_c6b);
},unregisterIframe:function(_c6c){
_c6c&&_c6c.remove();
},registerWin:function(_c6d,_c6e){
return _c65.registerWin(_c6d,_c6e);
},unregisterWin:function(_c6f){
_c6f&&_c6f.remove();
}};
_c65.focus=function(_c70){
if(!_c70){
return;
}
var node="node" in _c70?_c70.node:_c70,_c71=_c70.bookmark,_c72=_c70.openedForWindow,_c73=_c71?_c71.isCollapsed:false;
if(node){
var _c74=(node.tagName.toLowerCase()=="iframe")?node.contentWindow:node;
if(_c74&&_c74.focus){
try{
_c74.focus();
}
catch(e){
}
}
_c65._onFocusNode(node);
}
if(_c71&&win.withGlobal(_c72||win.global,_c67.isCollapsed)&&!_c73){
if(_c72){
_c72.focus();
}
try{
win.withGlobal(_c72||win.global,_c67.moveToBookmark,null,[_c71]);
}
catch(e2){
}
}
};
_c65.watch("curNode",function(name,_c75,_c76){
_c67._curFocus=_c76;
_c67._prevFocus=_c75;
if(_c76){
_c64.publish("focusNode",_c76);
}
});
_c65.watch("activeStack",function(name,_c77,_c78){
_c67._activeStack=_c78;
});
_c65.on("widget-blur",function(_c79,by){
_c64.publish("widgetBlur",_c79,by);
});
_c65.on("widget-focus",function(_c7a,by){
_c64.publish("widgetFocus",_c7a,by);
});
lang.mixin(_c67,_c68);
return _c67;
});
},"curam/util/ScreenContext":function(){
define(["dojo/_base/declare"],function(_c7b){
var _c7c={DEFAULT_CONTEXT:112,SAMPLE22:2,SAMPLE21:1,SAMPLE13:4,SAMPLE12:2,SAMPLE11:1,EXTAPP:1048576,SMART_PANEL:262144,NESTED_UIM:131072,ORG_TREE:65536,CONTEXT_PANEL:32768,LIST_ROW_INLINE_PAGE:8192,LIST_EVEN_ROW:16384,TAB:4096,TREE:2048,AGENDA:1024,POPUP:512,MODAL:256,HOME:128,HEADER:64,NAVIGATOR:32,FOOTER:16,OVAL:8,RESOLVE:4,ACTION:2,ERROR:1,EMPTY:0};
var _c7d=[["ERROR","ACTION","RESOLVE","OPT_VALIDATION","FOOTER","NAVIGATOR","HEADER","HOME_PAGE","MODAL","POPUP","AGENDA","TREE","TAB","LIST_EVEN_ROW","LIST_ROW_INLINE_PAGE","CONTEXT_PANEL","ORG_TREE","NESTED_UIM","SMART_PANEL","EXTAPP"],["SAMPLE11","SAMPLE12","SAMPLE13"],["SAMPLE21","SAMPLE22"]];
var _c7e=_c7b("curam.util.ScreenContext",null,{constructor:function(_c7f){
if(_c7f){
this.setContext(_c7f);
}else{
this.currentContext=[_c7c["DEFAULT_CONTEXT"]|_c7c["DEFAULT_CONTEXT"]];
}
},setContext:function(_c80){
var tmp=this.setup(_c80);
this.currentContext=((tmp==null)?([_c7c["DEFAULT_CONTEXT"]|_c7c["DEFAULT_CONTEXT"]]):(tmp));
},addContextBits:function(_c81,idx){
if(!_c81){
return;
}
var _c82=(idx)?idx:0;
var _c83=this.parseContext(_c81);
if(_c83!=null){
this.currentContext[_c82]|=_c83;
}
return this.currentContext[_c82];
},addAll:function(idx){
var _c84=(idx)?idx:0;
this.currentContext[_c84]=4294967295;
return this.currentContext[_c84];
},clear:function(_c85,idx){
if(!_c85){
this.clearAll();
return;
}
var _c86=(idx)?idx:0;
if(_c85==0){
return this.currentContext[_c86];
}
var _c87=this.parseContext(_c85);
if(_c87!=null){
var _c88=this.currentContext[_c86]&_c87;
this.currentContext[_c86]^=_c88;
}
return this.currentContext[_c86];
},clearAll:function(idx){
if(idx){
this.currentContext[idx]=0;
}else{
for(var i=0;i<this.currentContext.length;i++){
this.currentContext[i]=0;
}
}
},updateStates:function(_c89){
this.clear("ERROR|ACTION|RESOLVE");
this.currentContext[0]=this.currentContext[0]|(_c89&7);
},hasContextBits:function(_c8a,idx){
if(!_c8a){
return false;
}
var _c8b=(idx)?idx:0;
var _c8c=this.parseContext(_c8a);
if(_c8c!=null){
var _c8d=this.currentContext[_c8b]&_c8c;
return (_c8d==_c8c);
}
return false;
},getValue:function(){
var _c8e="";
for(var i=0;i<this.currentContext.length;i++){
_c8e+=this.currentContext[i]+"|";
}
return _c8e.substring(0,_c8e.length-1);
},toRequestString:function(){
return "o3ctx="+this.getValue();
},toBinary:function(){
var _c8f="";
for(var i=0;i<this.currentContext.length;i++){
_c8f+=this.currentContext[i].toString(2)+"|";
}
return _c8f.substring(0,_c8f.length-1);
},toString:function(){
var _c90="";
for(var i=0;i<this.currentContext.length;i++){
var _c91="";
var j=0;
while(j<_c7d[i].length){
if(((this.currentContext[i]>>j)&1)!=0){
_c91+=","+_c7d[i][j];
}
j++;
}
if(_c91==""){
return "{}";
}
_c90+="|"+_c91.replace(",","{")+((_c91.length==0)?"":"}");
}
return _c90.substring(1);
},parseContext:function(_c92){
var _c93=_c92.replace(/,/g,"|");
var _c94=_c93.split("|");
var tmp=isNaN(_c94[0])?parseInt(_c7c[_c94[0]]):_c94[0];
for(var i=1;i<_c94.length;i++){
tmp=tmp|(isNaN(_c94[i])?parseInt(_c7c[_c94[i]]):_c94[i]);
}
return (isNaN(tmp)?null:tmp);
},setup:function(_c95){
if(!_c95){
return null;
}
var _c96=(""+_c95).split("|");
var _c97=new Array(_c96.length);
for(var i=0;i<_c96.length;i++){
_c97[i]=this.parseContext(_c96[_c96.length-i-1]);
_c97[i]=_c97[i]|_c97[i];
if(!_c97[i]||isNaN(_c97[i])||_c97[i]>4294967295){
return null;
}
}
return _c97;
}});
return _c7e;
});
},"dijit/a11y":function(){
define(["dojo/_base/array","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/sniff","./main"],function(_c98,dom,_c99,_c9a,lang,has,_c9b){
var _c9c;
var a11y={_isElementShown:function(elem){
var s=_c9a.get(elem);
return (s.visibility!="hidden")&&(s.visibility!="collapsed")&&(s.display!="none")&&(_c99.get(elem,"type")!="hidden");
},hasDefaultTabStop:function(elem){
switch(elem.nodeName.toLowerCase()){
case "a":
return _c99.has(elem,"href");
case "area":
case "button":
case "input":
case "object":
case "select":
case "textarea":
return true;
case "iframe":
var body;
try{
var _c9d=elem.contentDocument;
if("designMode" in _c9d&&_c9d.designMode=="on"){
return true;
}
body=_c9d.body;
}
catch(e1){
try{
body=elem.contentWindow.document.body;
}
catch(e2){
return false;
}
}
return body&&(body.contentEditable=="true"||(body.firstChild&&body.firstChild.contentEditable=="true"));
default:
return elem.contentEditable=="true";
}
},effectiveTabIndex:function(elem){
if(_c99.get(elem,"disabled")){
return _c9c;
}else{
if(_c99.has(elem,"tabIndex")){
return +_c99.get(elem,"tabIndex");
}else{
return a11y.hasDefaultTabStop(elem)?0:_c9c;
}
}
},isTabNavigable:function(elem){
return a11y.effectiveTabIndex(elem)>=0;
},isFocusable:function(elem){
return a11y.effectiveTabIndex(elem)>=-1;
},_getTabNavigable:function(root){
var _c9e,last,_c9f,_ca0,_ca1,_ca2,_ca3={};
function _ca4(node){
return node&&node.tagName.toLowerCase()=="input"&&node.type&&node.type.toLowerCase()=="radio"&&node.name&&node.name.toLowerCase();
};
var _ca5=a11y._isElementShown,_ca6=a11y.effectiveTabIndex;
var _ca7=function(_ca8){
for(var _ca9=_ca8.firstChild;_ca9;_ca9=_ca9.nextSibling){
if(_ca9.nodeType!=1||(has("ie")<=9&&_ca9.scopeName!=="HTML")||!_ca5(_ca9)){
continue;
}
var _caa=_ca6(_ca9);
if(_caa>=0){
if(_caa==0){
if(!_c9e){
_c9e=_ca9;
}
last=_ca9;
}else{
if(_caa>0){
if(!_c9f||_caa<_ca0){
_ca0=_caa;
_c9f=_ca9;
}
if(!_ca1||_caa>=_ca2){
_ca2=_caa;
_ca1=_ca9;
}
}
}
var rn=_ca4(_ca9);
if(_c99.get(_ca9,"checked")&&rn){
_ca3[rn]=_ca9;
}
}
if(_ca9.nodeName.toUpperCase()!="SELECT"){
_ca7(_ca9);
}
}
};
if(_ca5(root)){
_ca7(root);
}
function rs(node){
return _ca3[_ca4(node)]||node;
};
return {first:rs(_c9e),last:rs(last),lowest:rs(_c9f),highest:rs(_ca1)};
},getFirstInTabbingOrder:function(root,doc){
var _cab=a11y._getTabNavigable(dom.byId(root,doc));
return _cab.lowest?_cab.lowest:_cab.first;
},getLastInTabbingOrder:function(root,doc){
var _cac=a11y._getTabNavigable(dom.byId(root,doc));
return _cac.last?_cac.last:_cac.highest;
}};
1&&lang.mixin(_c9b,a11y);
return a11y;
});
},"dijit/form/_ToggleButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr"],function(_cad,_cae){
return _cad("dijit.form._ToggleButtonMixin",null,{checked:false,_aria_attr:"aria-pressed",_onClick:function(evt){
var _caf=this.checked;
this._set("checked",!_caf);
var ret=this.inherited(arguments);
this.set("checked",ret?this.checked:_caf);
return ret;
},_setCheckedAttr:function(_cb0,_cb1){
this._set("checked",_cb0);
var node=this.focusNode||this.domNode;
if(this._created){
if(_cae.get(node,"checked")!=!!_cb0){
_cae.set(node,"checked",!!_cb0);
}
}
node.setAttribute(this._aria_attr,String(_cb0));
this._handleOnChange(_cb0,_cb1);
},postCreate:function(){
this.inherited(arguments);
var node=this.focusNode||this.domNode;
if(this.checked){
node.setAttribute("checked","checked");
}
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.checked;
}
},reset:function(){
this._hasBeenBlurred=false;
this.set("checked",this.params.checked||false);
}});
});
},"dijit/_Widget":function(){
define(["dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/ready","./registry","./_WidgetBase","./_OnDijitClickMixin","./_FocusMixin","dojo/uacss","./hccss"],function(_cb2,_cb3,_cb4,_cb5,has,_cb6,lang,_cb7,_cb8,_cb9,_cba,_cbb,_cbc){
function _cbd(){
};
function _cbe(_cbf){
return function(obj,_cc0,_cc1,_cc2){
if(obj&&typeof _cc0=="string"&&obj[_cc0]==_cbd){
return obj.on(_cc0.substring(2).toLowerCase(),lang.hitch(_cc1,_cc2));
}
return _cbf.apply(_cb4,arguments);
};
};
_cb2.around(_cb4,"connect",_cbe);
if(_cb6.connect){
_cb2.around(_cb6,"connect",_cbe);
}
var _cc3=_cb5("dijit._Widget",[_cba,_cbb,_cbc],{onClick:_cbd,onDblClick:_cbd,onKeyDown:_cbd,onKeyPress:_cbd,onKeyUp:_cbd,onMouseDown:_cbd,onMouseMove:_cbd,onMouseOut:_cbd,onMouseOver:_cbd,onMouseLeave:_cbd,onMouseEnter:_cbd,onMouseUp:_cbd,constructor:function(_cc4){
this._toConnect={};
for(var name in _cc4){
if(this[name]===_cbd){
this._toConnect[name.replace(/^on/,"").toLowerCase()]=_cc4[name];
delete _cc4[name];
}
}
},postCreate:function(){
this.inherited(arguments);
for(var name in this._toConnect){
this.on(name,this._toConnect[name]);
}
delete this._toConnect;
},on:function(type,func){
if(this[this._onMap(type)]===_cbd){
return _cb4.connect(this.domNode,type.toLowerCase(),this,func);
}
return this.inherited(arguments);
},_setFocusedAttr:function(val){
this._focused=val;
this._set("focused",val);
},setAttribute:function(attr,_cc5){
_cb6.deprecated(this.declaredClass+"::setAttribute(attr, value) is deprecated. Use set() instead.","","2.0");
this.set(attr,_cc5);
},attr:function(name,_cc6){
var args=arguments.length;
if(args>=2||typeof name==="object"){
return this.set.apply(this,arguments);
}else{
return this.get(name);
}
},getDescendants:function(){
_cb6.deprecated(this.declaredClass+"::getDescendants() is deprecated. Use getChildren() instead.","","2.0");
return this.containerNode?_cb7("[widgetId]",this.containerNode).map(_cb9.byNode):[];
},_onShow:function(){
this.onShow();
},onShow:function(){
},onHide:function(){
},onClose:function(){
return true;
}});
if(1){
_cb8(0,function(){
var _cc7=["dijit/_base"];
require(_cc7);
});
}
return _cc3;
});
},"dojo/touch":function(){
define(["./_base/kernel","./aspect","./dom","./dom-class","./_base/lang","./on","./has","./mouse","./domReady","./_base/window"],function(dojo,_cc8,dom,_cc9,lang,on,has,_cca,_ccb,win){
var ios4=has("ios")<5;
var _ccc=has("pointer-events")||has("MSPointer"),_ccd=(function(){
var _cce={};
for(var type in {down:1,move:1,up:1,cancel:1,over:1,out:1}){
_cce[type]=has("MSPointer")?"MSPointer"+type.charAt(0).toUpperCase()+type.slice(1):"pointer"+type;
}
return _cce;
})();
var _ccf=has("touch-events");
var _cd0,_cd1,_cd2=false,_cd3,_cd4,_cd5,_cd6,_cd7,_cd8;
var _cd9;
function _cda(_cdb,_cdc,_cdd){
if(_ccc&&_cdd){
return function(node,_cde){
return on(node,_cdd,_cde);
};
}else{
if(_ccf){
return function(node,_cdf){
var _ce0=on(node,_cdc,function(evt){
_cdf.call(this,evt);
_cd9=(new Date()).getTime();
}),_ce1=on(node,_cdb,function(evt){
if(!_cd9||(new Date()).getTime()>_cd9+1000){
_cdf.call(this,evt);
}
});
return {remove:function(){
_ce0.remove();
_ce1.remove();
}};
};
}else{
return function(node,_ce2){
return on(node,_cdb,_ce2);
};
}
}
};
function _ce3(node){
do{
if(node.dojoClick!==undefined){
return node;
}
}while(node=node.parentNode);
};
function _ce4(e,_ce5,_ce6){
if(_cca.isRight(e)){
return;
}
var _ce7=_ce3(e.target);
_cd1=!e.target.disabled&&_ce7&&_ce7.dojoClick;
if(_cd1){
_cd2=(_cd1=="useTarget");
_cd3=(_cd2?_ce7:e.target);
if(_cd2){
e.preventDefault();
}
_cd4=e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX;
_cd5=e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY;
_cd6=(typeof _cd1=="object"?_cd1.x:(typeof _cd1=="number"?_cd1:0))||4;
_cd7=(typeof _cd1=="object"?_cd1.y:(typeof _cd1=="number"?_cd1:0))||4;
if(!_cd0){
_cd0=true;
function _ce8(e){
if(_cd2){
_cd1=dom.isDescendant(win.doc.elementFromPoint((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX),(e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)),_cd3);
}else{
_cd1=_cd1&&(e.changedTouches?e.changedTouches[0].target:e.target)==_cd3&&Math.abs((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX)-_cd4)<=_cd6&&Math.abs((e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)-_cd5)<=_cd7;
}
};
win.doc.addEventListener(_ce5,function(e){
if(_cca.isRight(e)){
return;
}
_ce8(e);
if(_cd2){
e.preventDefault();
}
},true);
win.doc.addEventListener(_ce6,function(e){
if(_cca.isRight(e)){
return;
}
_ce8(e);
if(_cd1){
_cd8=(new Date()).getTime();
var _ce9=(_cd2?_cd3:e.target);
if(_ce9.tagName==="LABEL"){
_ce9=dom.byId(_ce9.getAttribute("for"))||_ce9;
}
var src=(e.changedTouches)?e.changedTouches[0]:e;
function _cea(type){
var evt=document.createEvent("MouseEvents");
evt._dojo_click=true;
evt.initMouseEvent(type,true,true,e.view,e.detail,src.screenX,src.screenY,src.clientX,src.clientY,e.ctrlKey,e.altKey,e.shiftKey,e.metaKey,0,null);
return evt;
};
var _ceb=_cea("mousedown");
var _cec=_cea("mouseup");
var _ced=_cea("click");
setTimeout(function(){
on.emit(_ce9,"mousedown",_ceb);
on.emit(_ce9,"mouseup",_cec);
on.emit(_ce9,"click",_ced);
_cd8=(new Date()).getTime();
},0);
}
},true);
function _cee(type){
win.doc.addEventListener(type,function(e){
if(_cd1&&!e._dojo_click&&(new Date()).getTime()<=_cd8+1000&&!(e.target.tagName=="INPUT"&&_cc9.contains(e.target,"dijitOffScreen"))){
e.stopPropagation();
e.stopImmediatePropagation&&e.stopImmediatePropagation();
if(type=="click"&&(e.target.tagName!="INPUT"||e.target.type=="radio"||e.target.type=="checkbox")&&e.target.tagName!="TEXTAREA"&&e.target.tagName!="AUDIO"&&e.target.tagName!="VIDEO"){
e.preventDefault();
}
}
},true);
};
_cee("click");
_cee("mousedown");
_cee("mouseup");
}
}
};
var _cef;
if(has("touch")){
if(_ccc){
_ccb(function(){
win.doc.addEventListener(_ccd.down,function(evt){
_ce4(evt,_ccd.move,_ccd.up);
},true);
});
}else{
_ccb(function(){
_cef=win.body();
win.doc.addEventListener("touchstart",function(evt){
_cd9=(new Date()).getTime();
var _cf0=_cef;
_cef=evt.target;
on.emit(_cf0,"dojotouchout",{relatedTarget:_cef,bubbles:true});
on.emit(_cef,"dojotouchover",{relatedTarget:_cf0,bubbles:true});
_ce4(evt,"touchmove","touchend");
},true);
function _cf1(evt){
var _cf2=lang.delegate(evt,{bubbles:true});
if(has("ios")>=6){
_cf2.touches=evt.touches;
_cf2.altKey=evt.altKey;
_cf2.changedTouches=evt.changedTouches;
_cf2.ctrlKey=evt.ctrlKey;
_cf2.metaKey=evt.metaKey;
_cf2.shiftKey=evt.shiftKey;
_cf2.targetTouches=evt.targetTouches;
}
return _cf2;
};
on(win.doc,"touchmove",function(evt){
_cd9=(new Date()).getTime();
var _cf3=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset));
if(_cf3){
if(_cef!==_cf3){
on.emit(_cef,"dojotouchout",{relatedTarget:_cf3,bubbles:true});
on.emit(_cf3,"dojotouchover",{relatedTarget:_cef,bubbles:true});
_cef=_cf3;
}
if(!on.emit(_cf3,"dojotouchmove",_cf1(evt))){
evt.preventDefault();
}
}
});
on(win.doc,"touchend",function(evt){
_cd9=(new Date()).getTime();
var node=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset))||win.body();
on.emit(node,"dojotouchend",_cf1(evt));
});
});
}
}
var _cf4={press:_cda("mousedown","touchstart",_ccd.down),move:_cda("mousemove","dojotouchmove",_ccd.move),release:_cda("mouseup","dojotouchend",_ccd.up),cancel:_cda(_cca.leave,"touchcancel",_ccc?_ccd.cancel:null),over:_cda("mouseover","dojotouchover",_ccd.over),out:_cda("mouseout","dojotouchout",_ccd.out),enter:_cca._eventHandler(_cda("mouseover","dojotouchover",_ccd.over)),leave:_cca._eventHandler(_cda("mouseout","dojotouchout",_ccd.out))};
1&&(dojo.touch=_cf4);
return _cf4;
});
},"idx/string":function(){
define(["dojo/_base/lang","idx/main","dojo/string"],function(_cf5,_cf6,_cf7){
var _cf8=_cf5.getObject("string",true,_cf6);
var _cf9="\\[]()^.+*{}?!=-";
var _cfa={};
var _cfb=0;
var _cfc=50;
_cf8.unescapedIndexesOf=function(text,_cfd,_cfe,_cff){
if(!_cfd||(_cfd.length==0)){
throw "One or more characters must be provided to search for unescaped characters: "+" chars=[ "+_cfd+" ], escaper=[ "+_cfe+" ], text=[ "+text+" ]";
}
if(!_cfe||_cfe.length==0){
_cfe="\\";
}
if(_cfe.length>1){
throw "The escaper must be a single character for unescaped character search: "+" escaper=[ "+_cfe+" ], chars=[ "+_cfd+" ], text=[ "+text+" ]";
}
if(_cfd.indexOf(_cfe)>=0){
throw "The escaping character cannot also be a search character for unescaped character search: "+" escaper=[ "+_cfe+" ], separators=[ "+separators+" ] text=[ "+text+" ]";
}
if((_cff===null)||(_cff===undefined)||(_cff===0)||(_cff===false)){
_cff=-1;
}
if(_cff<0){
_cff=0;
}
var _d00=[];
var _d01=0;
var _d02=false;
var _d03=null;
for(_d01=0;_d01<text.length;_d01++){
_d03=text.charAt(_d01);
if(_d02){
_d02=false;
continue;
}
if(_d03==_cfe){
_d02=true;
continue;
}
if(_cfd.indexOf(_d03)>=0){
_d00.push(_d01);
if(_cff&&(_d00.length==_cff)){
break;
}
}
}
return _d00;
};
_cf8.unescapedSplit=function(text,_d04,_d05,_d06){
if(!_d04||(_d04.length==0)){
_d04=",";
}
if(!_d05||_d05.length==0){
_d05="\\";
}
if(_d05.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_d05+" ], separators=[ "+_d04+" ], text=[ "+text+" ]";
}
if(_d04.indexOf(_d05)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_d05+" ], separators=[ "+_d04+" ] text=[ "+text+" ]";
}
if((_d06===null)||(_d06===undefined)||(_d06===0)||(_d06===false)){
_d06=-1;
}
if(_d06<0){
_d06=0;
}
var _d07=[];
var _d08=0;
var _d09=false;
var _d0a=null;
var _d0b=0;
for(_d08=0;_d08<text.length;_d08++){
_d0a=text.charAt(_d08);
if(_d09){
_d09=false;
continue;
}
if(_d0a==_d05){
_d09=true;
continue;
}
if(_d04.indexOf(_d0a)>=0){
if(_d0b==_d08){
_d07.push("");
}else{
_d07.push(text.substring(_d0b,_d08));
}
_d0b=_d08+1;
if(_d06&&(_d07.length==_d06)){
break;
}
}
}
if((!_d06)||(_d06&&_d07.length<_d06)){
if(_d0b==text.length){
_d07.push("");
}else{
_d07.push(text.substring(_d0b));
}
}
return _d07;
};
_cf8.parseTokens=function(text,_d0c,_d0d,_d0e){
if(!_d0c||(_d0c.length==0)){
_d0c=",";
}
if(!_d0d||_d0d.length==0){
_d0d="\\";
}
if(_d0d.length>1){
throw "The escaper must be a single character for token parsing: "+" escaper=[ "+_d0d+" ], separators=[ "+_d0c+" ], text=[ "+text+" ]";
}
if(_d0c.indexOf(_d0d)>=0){
throw "The escaping character cannot also be a separator for token parsing: "+" escaper=[ "+_d0d+" ], separators=[ "+_d0c+" ] text=[ "+text+" ]";
}
if(_d0e&&_d0e.length==0){
_d0e=null;
}
var _d0f=0;
var _d10=[];
var _d11=0;
var _d12=false;
var part="";
var end=-1;
var _d13=null;
for(_d0f=0;_d0f<text.length;_d0f++){
_d13=text.charAt(_d0f);
end=-1;
if(!_d12&&_d13==_d0d){
if(_d0e&&((_d0f+1)<text.length)&&(_d0e.indexOf(_d13)>=0)){
_d0f++;
continue;
}
part=part+text.substring(_d11,_d0f);
_d11=_d0f+1;
_d12=true;
continue;
}
if(_d12){
_d12=false;
continue;
}
if(_d0c.indexOf(_d13)>=0){
end=_d0f;
}
if(end>=0){
part=part+text.substring(_d11,end);
_d11=end+1;
_d10.push(part);
part="";
}
}
if((end<0)&&(_d11<text.length)){
part=part+text.substring(_d11);
_d10.push(part);
}else{
if((end>=0)||_d12){
_d10.push(part);
}
}
return _d10;
};
_cf8.escapeChars=function(text,_d14,_d15){
if(!_d14||(_d14.length==0)){
_d14="";
}
if(!_d15||_d15.length==0){
_d15="\\";
}
if(_d15.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_d15+" ], chars=[ "+_d14+" ], text=[ "+text+" ]";
}
if(_d14.indexOf(_d15)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_d15+" ], chars=[ "+_d14+" ] text=[ "+text+" ]";
}
var _d14=_d14+_d15;
var _d16=_cfa[_d14];
var _d17="";
if(!_d16){
_d17="([";
for(index=0;index<_d14.length;index++){
if(_cf9.indexOf(_d14.charAt(index))>=0){
_d17=_d17+"\\";
}
_d17=_d17+_d14.charAt(index);
}
_d17=_d17+"])";
try{
_d16=new RegExp(_d17,"g");
}
catch(e){
console.log("Pattern: "+_d17);
throw e;
}
if(_cfb<_cfc){
_cfa[_d14]=_d16;
_cfb++;
}
}
return text.replace(_d16,_d15+"$1");
};
_cf8.escapedJoin=function(arr,_d18,_d19){
if(!_d18||(_d18.length==0)){
_d18=",";
}
if(_d18.length>1){
throw "Only one separator character can be used in escapedJoin: ";
" separator=[ "+_d18+" ], text=[ "+text+" ]";
}
if(!_d19||_d19.length==0){
_d19="\\";
}
if(_d19.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_d19+" ], separator=[ "+_d18+" ], text=[ "+text+" ]";
}
if(_d18.indexOf(_d19)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_d19+" ], separator=[ "+_d18+" ] text=[ "+text+" ]";
}
var _d1a=0;
var _d1b="";
var part=null;
var _d1c="";
for(_d1a=0;_d1a<arr.length;_d1a++){
part=arr[_d1a];
part=_cf8.escapeChars(part,_d18,_d19);
_d1b=_d1b+_d1c+part;
_d1c=_d18;
}
return _d1b;
};
_cf8.startsWith=function(text,_d1d){
return (_cf5.isString(text)&&_cf5.isString(_d1d)&&text.indexOf(_d1d)===0);
};
_cf8.endsWith=function(text,_d1e){
return (_cf5.isString(text)&&_cf5.isString(_d1e)&&text.lastIndexOf(_d1e)===text.length-_d1e.length);
};
_cf8.equalsIgnoreCase=function(s1,s2){
return (_cf5.isString(s1)&&_cf5.isString(s2)&&s1.toLowerCase()===s2.toLowerCase());
};
_cf8.isNumber=function(n){
return (typeof n=="number"&&isFinite(n));
};
_cf8.nullTrim=function(str){
if(!str){
return null;
}
var _d1f=_cf7.trim(str);
return (_d1f.length==0)?null:_d1f;
};
_cf8.propToLabel=function(_d20){
if(!_d20){
return _d20;
}
_d20=_cf7.trim(_d20);
var _d21=_d20.toUpperCase();
var _d22=_d20.toLowerCase();
var _d23=0;
var _d24="";
var _d25="";
var _d26=false;
var _d27=false;
for(_d23=0;_d23<_d20.length;_d23++){
var _d28=_d21.charAt(_d23);
var _d29=_d22.charAt(_d23);
var _d2a=_d20.charAt(_d23);
var _d2b=((_d28==_d2a)&&(_d29!=_d2a));
var _d2c=((_d29==_d2a)&&(_d28!=_d2a));
if((_d2a=="_")||(_d2a==" ")){
if(_d25==" "){
continue;
}
_d25=" ";
_d26=_d2b;
_d27=_d2c;
_d24=_d24+_d25;
continue;
}
if(_d2a=="."){
_d25="/";
_d26=_d2b;
_d27=_d2c;
_d24=_d24+" "+_d25+" ";
continue;
}
if((_d23==0)||(_d25==" ")){
_d25=_d28;
_d26=_d2b;
_d27=_d2c;
_d24=_d24+_d25;
continue;
}
if((!_d2b)&&(!_d2c)){
if(_d26||_d27){
_d24=_d24+" ";
}
_d26=_d2b;
_d27=_d2c;
_d25=_d2a;
_d24=_d24+_d25;
continue;
}
if((!_d26)&&(!_d27)){
var _d2d=(_d25=="/");
_d26=_d2b;
_d27=_d2c;
_d25=_d28;
if(_d2d){
_d24=_d24+_d25;
}else{
_d24=_d24+" "+_d25;
}
continue;
}
if(_d2b&&_d27){
_d25=_d28;
_d26=_d2b;
_d27=_d2c;
_d24=_d24+" "+_d25;
continue;
}
_d25=_d29;
_d26=_d2b;
_d27=_d2c;
_d24=_d24+_d25;
}
return _d24;
};
return _cf8;
});
},"dojo/request":function(){
define(["./request/default!"],function(_d2e){
return _d2e;
});
},"curam/widget/IDXFilteringSelect":function(){
define(["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/text!curam/widget/templates/IDXComboBox.html","idx/form/FilteringSelect"],function(_d2f,_d30,on,_d31){
var _d32=_d30("curam.widget.IDXFilteringSelect",idx.oneui.form.FilteringSelect,{templateString:_d31,enterKeyOnOpenDropDown:false,postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _d33=_d2f.byNode(dom.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_d33._opened){
_d33.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
}});
return _d32;
});
},"dijit/form/_FormValueWidget":function(){
define(["dojo/_base/declare","dojo/sniff","./_FormWidget","./_FormValueMixin"],function(_d34,has,_d35,_d36){
return _d34("dijit.form._FormValueWidget",[_d35,_d36],{_layoutHackIE7:function(){
if(has("ie")==7){
var _d37=this.domNode;
var _d38=_d37.parentNode;
var _d39=_d37.firstChild||_d37;
var _d3a=_d39.style.filter;
var _d3b=this;
while(_d38&&_d38.clientHeight==0){
(function ping(){
var _d3c=_d3b.connect(_d38,"onscroll",function(){
_d3b.disconnect(_d3c);
_d39.style.filter=(new Date()).getMilliseconds();
_d3b.defer(function(){
_d39.style.filter=_d3a;
});
});
})();
_d38=_d38.parentNode;
}
}
}});
});
},"url:dijit/form/templates/Button.html":"<span class=\"dijit dijitReset dijitInline\" role=\"presentation\"\n\t><span class=\"dijitReset dijitInline dijitButtonNode\"\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" role=\"presentation\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"titleNode,focusNode\"\n\t\t\trole=\"button\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span\n\t\t\t><span class=\"dijitReset dijitToggleButtonIconChar\">&#x25CF;</span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode\"\n\t\t\t></span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\"\n\t\ttabIndex=\"-1\" aria-hidden=\"true\" data-dojo-attach-point=\"valueNode\"\n/></span>\n","url:dijit/templates/MenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,textDirNode\"\n\t\trole=\"presentation\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">\n\t\t<span data-dojo-attach-point=\"arrowWrapper\" style=\"visibility: hidden\">\n\t\t\t<span class=\"dijitInline dijitIcon dijitMenuExpand\"></span>\n\t\t\t<span class=\"dijitMenuExpandA11y\">+</span>\n\t\t</span>\n\t</td>\n</tr>\n","url:curam/layout/resources/TabContainer.html":"<div class=\"dijitTabContainer\">\n\t<div class=\"dijitTabListWrapper\" data-dojo-attach-point=\"tablistNode\"></div>\n\t<div data-dojo-attach-point=\"tablistSpacer\" class=\"dijitTabSpacer ${baseClass}-spacer dijitAlignTop\"></div>\n\t<div class=\"dijitTabPaneWrapper ${baseClass}-container dijitAlignClient\" data-dojo-attach-point=\"containerNode\"></div>\n</div>\n","url:dijit/form/templates/TextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner\" data-dojo-attach-point='textbox,focusNode' autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n","url:curam/widget/templates/_TabButton.html":"<div role=\"presentation\" data-dojo-attach-point=\"titleNode,innerDiv,tabContent\" class=\"dijitTab\" id=\"${id}_tabButtonContainer\">\n  <div role=\"presentation\" class='dijitTabInnerDiv'>\n    <div role=\"presentation\" class='dijitTabContent'>\n\t    <span role=\"presentation\" class=\"dijitInline dijitIcon dijitTabButtonIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t    <div role=\"presentation\" aria-labelledby='${id}'>\n\t\t    <span data-dojo-attach-point='containerNode,focusNode' class='tabLabel' id='${id}'></span>\n\t\t    <button class=\"dijitInline dijitTabCloseButton dijitTabCloseIcon\" data-dojo-attach-point='closeNode'\n\t\t\t        tabindex=\"-1\">\n\t\t\t    <span data-dojo-attach-point='closeText' class='dijitTabCloseText'>Close Tab</span>\n        \t</button>\n      </div>\n    </div>\n  </div>\n</div>\n","url:dijit/layout/templates/TabContainer.html":"<div class=\"dijitTabContainer\">\n\t<div class=\"dijitTabListWrapper\" data-dojo-attach-point=\"tablistNode\"></div>\n\t<div data-dojo-attach-point=\"tablistSpacer\" class=\"dijitTabSpacer ${baseClass}-spacer\"></div>\n\t<div class=\"dijitTabPaneWrapper ${baseClass}-container\" data-dojo-attach-point=\"containerNode\"></div>\n</div>\n","url:dijit/form/templates/ValidationTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\" role=\"presentation\"\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner\" data-dojo-attach-point='textbox,focusNode' autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n","url:idx/widget/templates/HoverHelpTooltip.html":"<div class=\"idxOneuiHoverHelpTooltip idxOneuiHoverHelpTooltipLeft\" role=\"alert\"\r\n\t><div\r\n\t\t><span data-dojo-attach-point=\"closeButtonNode\" class=\"idxOneuiHoverHelpTooltipCloseIcon\" \r\n\t\t\tdata-dojo-attach-event=\"ondijitclick: hideOnClickClose\" role=\"button\" aria-label=\"${buttonClose}\"\r\n\t\t\t><span class=\"idxHoverHelpCloseText\">x</span\r\n\t\t></span\r\n\t></div\r\n\t><div data-dojo-attach-point=\"outerContainerNode\" class=\"idxOneuiHoverHelpTooltipContainer idxOneuiHoverHelpTooltipContents\"\r\n\t\t><div data-dojo-attach-point=\"containerNode\" role=\"presentation\"></div\r\n\t\t><a target=\"_blank\" href=\"#updateme\" role=\"link\" class=\"idxOneuiHoverHelpTooltipLearnLink\" data-dojo-attach-point=\"learnMoreNode\"><span>${learnMoreLabel}</span></a\r\n\t></div\r\n\t><div class=\"idxOneuiHoverHelpTooltipConnector\" data-dojo-attach-point=\"connectorNode\"></div\r\n></div>","url:dijit/layout/templates/_ScrollingTabControllerButton.html":"<div data-dojo-attach-event=\"ondijitclick:_onClick\" data-dojo-attach-point=\"focusNode\" role=\"button\">\n  <div role=\"presentation\" class=\"dijitTabInnerDiv\">\n    <div role=\"presentation\" class=\"dijitTabContent dijitButtonContents\">\n\t<span role=\"presentation\" class=\"dijitInline dijitTabStripIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t<span data-dojo-attach-point=\"containerNode,titleNode\" class=\"dijitButtonText\"></span>\n</div>  </div>\n</div>","url:dijit/layout/templates/_TabButton.html":"<div role=\"presentation\" data-dojo-attach-point=\"titleNode,innerDiv,tabContent\" class=\"dijitTabInner dijitTabContent\">\n\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitTabButtonIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t<span data-dojo-attach-point='containerNode,focusNode' class='tabLabel'></span>\n\t<span class=\"dijitInline dijitTabCloseButton dijitTabCloseIcon\" data-dojo-attach-point='closeNode'\n\t\t  role=\"presentation\">\n\t\t<span data-dojo-attach-point='closeText' class='dijitTabCloseText'>[x]</span\n\t\t\t\t></span>\n</div>\n","url:dijit/form/templates/DropDownBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\"\n\trole=\"combobox\"\n\taria-haspopup=\"true\"\n\tdata-dojo-attach-point=\"_popupStateNode\"\n\t><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer'\n\t\tdata-dojo-attach-point=\"_buttonNode\" role=\"presentation\"\n\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"button presentation\" aria-hidden=\"true\"\n\t\t\t${_buttonInputDisabled}\n\t/></div\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class='dijitReset dijitInputInner' ${!nameAttrSetting} type=\"text\" autocomplete=\"off\"\n\t\t\tdata-dojo-attach-point=\"textbox,focusNode\" role=\"textbox\"\n\t/></div\n></div>\n","url:curam/layout/resources/ScrollingTabController.html":"<div class=\"dijitTabListContainer-${tabPosition} tabStrip-disabled dijitLayoutContainer\"><!-- CURAM-FIX: removed style=\"visibility:hidden, dd the tabStrip-disabled class by default.\" -->\n\t<div data-dojo-type=\"curam.layout._ScrollingTabControllerMenuButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_menuBtn\"\n\t\t data-dojo-props=\"containerId: '${containerId}', iconClass: 'dijitTabStripMenuIcon',\n\t\t\t\t\tdropDownPosition: ['below-alt', 'above-alt']\"\n\t\t data-dojo-attach-point=\"_menuBtn\" showLabel=\"false\" title=\"Navigation menu\">&#9660;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_leftBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideLeftIcon', showLabel:false, title:'Navigation left'\"\n\t\t data-dojo-attach-point=\"_leftBtn\" data-dojo-attach-event=\"onClick: doSlideLeft\">&#9664;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_rightBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideRightIcon', showLabel:false, title:'Navigation right'\"\n\t\t data-dojo-attach-point=\"_rightBtn\" data-dojo-attach-event=\"onClick: doSlideRight\">&#9654;</div>\n\t<div class='dijitTabListWrapper dijitTabContainerTopNone dijitAlignClient' data-dojo-attach-point='tablistWrapper'>\n\t\t<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'\n\t\t\t\tdata-dojo-attach-point='containerNode' class='nowrapTabStrip dijitTabContainerTop-tabs'></div>\n\t</div>\n</div>","url:dijit/templates/Menu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable\" role=\"menu\" tabIndex=\"${tabIndex}\"\n\t   cellspacing=\"0\">\n\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"containerNode\"></tbody>\n</table>\n","url:dijit/templates/Tooltip.html":"<div class=\"dijitTooltip dijitTooltipLeft\" id=\"dojoTooltip\" data-dojo-attach-event=\"mouseenter:onMouseEnter,mouseleave:onMouseLeave\"\n\t><div class=\"dijitTooltipConnector\" data-dojo-attach-point=\"connectorNode\"></div\n\t><div class=\"dijitTooltipContainer dijitTooltipContents\" data-dojo-attach-point=\"containerNode\" role='alert'></div\n></div>\n","url:curam/widget/templates/IDXComboBox.html":"<div id=\"widget_${id}\" class=\"dijitReset dijitInline idxComposite\" \n  ><div class=\"idxLabel dijitInline dijitHidden\" dojoAttachPoint=\"labelWrap\"\n    ><span class=\"idxRequiredIcon\">*&nbsp</span\n    ><label for=\"widget_${id}\" dojoAttachPoint=\"compLabelNode\"\n    ></label\n  ></div\n  ><div class=\"dijitInline\"\n    ><div class=\"dijit dijitInline dijitReset dijitInlineTable dijitLeft\" role=\"listbox\" dojoAttachPoint=\"stateNode,oneuiBaseNode,_aroundNode,_popupStateNode\"\n      ><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer' dojoAttachPoint=\"_buttonNode\" role=\"presentation\"\n      ><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n      ${_buttonInputDisabled}\n      /></div\n      ><div class=\"dijitReset dijitInputField dijitInputContainer\" role=\"listbox\" dojoAttachPoint=\"inputContainer\" dojoAttachEvent=\"onmouseenter: _onInputContainerEnter, onmouseleave: _onInputContainerLeave\"\n        ><input class='dijitReset dijitInputInner' ${!nameAttrSetting}  type=\"text\" autocomplete=\"off\" dojoAttachPoint=\"textbox,focusNode\" role=\"textbox\" aria-haspopup=\"true\" \n      /></div\n    ></div\n    ><div class=\"idxUnit dijitInline dijitHidden\" dojoAttachPoint=\"compUnitNode\"\n    ></div\n    ><div class='dijitReset dijitValidationContainer dijitInline' dojoAttachPoint=\"iconNode\"\n      ><div class=\"dijitValidationIcon\"\n      ><input class=\"dijitReset dijitInputField  dijitValidationInner\" value=\"&#935;\" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"/\n    ></div></div\n    ><div class=\"dijitHidden idxHintOutside\" dojoAttachPoint=\"compHintNode\"></div\n  ></div\n></div>","url:dijit/layout/templates/ScrollingTabController.html":"<div class=\"dijitTabListContainer-${tabPosition} tabStrip-disabled dijitLayoutContainer\"><!-- CURAM-FIX: removed style=\"visibility:hidden, dd the tabStrip-disabled class by default.\" -->\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerMenuButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_menuBtn\"\n\t\t data-dojo-props=\"containerId: '${containerId}', iconClass: 'dijitTabStripMenuIcon',\n\t\t\t\t\tdropDownPosition: ['below-alt', 'above-alt']\"\n\t\t data-dojo-attach-point=\"_menuBtn\" showLabel=\"false\" title=\"Navigation menu\">&#9660;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_leftBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideLeftIcon', showLabel:false, title:'Navigation left'\"\n\t\t data-dojo-attach-point=\"_leftBtn\" data-dojo-attach-event=\"onClick: doSlideLeft\">&#9664;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_rightBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideRightIcon', showLabel:false, title:'Navigation right'\"\n\t\t data-dojo-attach-point=\"_rightBtn\" data-dojo-attach-event=\"onClick: doSlideRight\">&#9654;</div>\n\t<div class='dijitTabListWrapper dijitTabContainerTopNone dijitAlignClient' data-dojo-attach-point='tablistWrapper'>\n\t\t<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'\n\t\t\t\tdata-dojo-attach-point='containerNode' class='nowrapTabStrip dijitTabContainerTop-tabs'></div>\n\t</div>\n</div>","*now":function(r){
r(["dojo/i18n!*preload*dojo/nls/cdej-ua-ieg*[\"ar\",\"ca\",\"cs\",\"da\",\"de\",\"el\",\"en-gb\",\"en-us\",\"es-es\",\"fi-fi\",\"fr-fr\",\"he-il\",\"hu\",\"it-it\",\"ja-jp\",\"ko-kr\",\"nl-nl\",\"nb\",\"pl\",\"pt-br\",\"pt-pt\",\"ru\",\"sk\",\"sl\",\"sv\",\"th\",\"tr\",\"zh-tw\",\"zh-cn\",\"ROOT\"]"]);
}}});
define("dojo/cdej-ua-ieg",[],1);
