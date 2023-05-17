//>>built
require({cache:{"dijit/_base/scroll":function(){
define(["dojo/window","../main"],function(_1,_2){
_2.scrollIntoView=function(_3,_4){
_1.scrollIntoView(_3,_4);
};
});
},"dijit/_TemplatedMixin":function(){
define(["dojo/cache","dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/on","dojo/sniff","dojo/string","./_AttachMixin"],function(_5,_6,_7,_8,on,_9,_a,_b){
var _c=_6("dijit._TemplatedMixin",_b,{templateString:null,templatePath:null,_skipNodeCache:false,searchContainerNode:true,_stringRepl:function(_d){
var _e=this.declaredClass,_f=this;
return _a.substitute(_d,this,function(_10,key){
if(key.charAt(0)=="!"){
_10=_8.getObject(key.substr(1),false,_f);
}
if(typeof _10=="undefined"){
throw new Error(_e+" template:"+key);
}
if(_10==null){
return "";
}
return key.charAt(0)=="!"?_10:this._escapeValue(""+_10);
},this);
},_escapeValue:function(val){
return val.replace(/["'<>&]/g,function(val){
return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;"}[val];
});
},buildRendering:function(){
if(!this._rendered){
if(!this.templateString){
this.templateString=_5(this.templatePath,{sanitize:true});
}
var _11=_c.getCachedTemplate(this.templateString,this._skipNodeCache,this.ownerDocument);
var _12;
if(_8.isString(_11)){
_12=_7.toDom(this._stringRepl(_11),this.ownerDocument);
if(_12.nodeType!=1){
throw new Error("Invalid template: "+_11);
}
}else{
_12=_11.cloneNode(true);
}
this.domNode=_12;
}
this.inherited(arguments);
if(!this._rendered){
this._fillContent(this.srcNodeRef);
}
this._rendered=true;
},_fillContent:function(_13){
var _14=this.containerNode;
if(_13&&_14){
while(_13.hasChildNodes()){
_14.appendChild(_13.firstChild);
}
}
}});
_c._templateCache={};
_c.getCachedTemplate=function(_15,_16,doc){
var _17=_c._templateCache;
var key=_15;
var _18=_17[key];
if(_18){
try{
if(!_18.ownerDocument||_18.ownerDocument==(doc||document)){
return _18;
}
}
catch(e){
}
_7.destroy(_18);
}
_15=_a.trim(_15);
if(_16||_15.match(/\$\{([^\}]+)\}/g)){
return (_17[key]=_15);
}else{
var _19=_7.toDom(_15,doc);
if(_19.nodeType!=1){
throw new Error("Invalid template: "+_15);
}
return (_17[key]=_19);
}
};
if(_9("ie")){
on(window,"unload",function(){
var _1a=_c._templateCache;
for(var key in _1a){
var _1b=_1a[key];
if(typeof _1b=="object"){
_7.destroy(_1b);
}
delete _1a[key];
}
});
}
return _c;
});
},"dijit/_CssStateMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/has","dojo/_base/lang","dojo/on","dojo/domReady","dojo/touch","dojo/_base/window","./a11yclick","./registry"],function(_1c,_1d,dom,_1e,has,_1f,on,_20,_21,win,_22,_23){
var _24=_1d("dijit._CssStateMixin",[],{hovering:false,active:false,_applyAttributes:function(){
this.inherited(arguments);
_1c.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","_opened"],function(_25){
this.watch(_25,_1f.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes||{}){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.domNode,this.baseClass);
this._setStateClass();
},_cssMouseEvent:function(_26){
if(!this.disabled){
switch(_26.type){
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
var _27=this.baseClass.split(" ");
function _28(_29){
_27=_27.concat(_1c.map(_27,function(c){
return c+_29;
}),"dijit"+_29);
};
if(!this.isLeftToRight()){
_28("Rtl");
}
var _2a=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_28(_2a);
}
if(this.state){
_28(this.state);
}
if(this.selected){
_28("Selected");
}
if(this._opened){
_28("Opened");
}
if(this.disabled){
_28("Disabled");
}else{
if(this.readOnly){
_28("ReadOnly");
}else{
if(this.active){
_28("Active");
}else{
if(this.hovering){
_28("Hover");
}
}
}
}
if(this.focused){
_28("Focused");
}
var tn=this.stateNode||this.domNode,_2b={};
_1c.forEach(tn.className.split(" "),function(c){
_2b[c]=true;
});
if("_stateClasses" in this){
_1c.forEach(this._stateClasses,function(c){
delete _2b[c];
});
}
_1c.forEach(_27,function(c){
_2b[c]=true;
});
var _2c=[];
for(var c in _2b){
_2c.push(c);
}
var cls=_2c.join(" ");
if(cls!=tn.className){
tn.className=cls;
}
this._stateClasses=_27;
},_subnodeCssMouseEvent:function(_2d,_2e,evt){
if(this.disabled||this.readOnly){
return;
}
function _2f(_30){
_1e.toggle(_2d,_2e+"Hover",_30);
};
function _31(_32){
_1e.toggle(_2d,_2e+"Active",_32);
};
function _33(_34){
_1e.toggle(_2d,_2e+"Focused",_34);
};
switch(evt.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
_2f(true);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
_2f(false);
_31(false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
_31(true);
break;
case "mouseup":
case "MSPointerUp":
case "pointerup":
case "dojotouchend":
case "keyup":
_31(false);
break;
case "focus":
case "focusin":
_33(true);
break;
case "blur":
case "focusout":
_33(false);
break;
}
},_trackMouseState:function(_35,_36){
_35._cssState=_36;
}});
_20(function(){
function _37(evt,_38,_39){
if(_39&&dom.isDescendant(_39,_38)){
return;
}
for(var _3a=_38;_3a&&_3a!=_39;_3a=_3a.parentNode){
if(_3a._cssState){
var _3b=_23.getEnclosingWidget(_3a);
if(_3b){
if(_3a==_3b.domNode){
_3b._cssMouseEvent(evt);
}else{
_3b._subnodeCssMouseEvent(_3a,_3a._cssState,evt);
}
}
}
}
};
var _3c=win.body(),_3d;
on(_3c,_21.over,function(evt){
_37(evt,evt.target,evt.relatedTarget);
});
on(_3c,_21.out,function(evt){
_37(evt,evt.target,evt.relatedTarget);
});
on(_3c,_22.press,function(evt){
_3d=evt.target;
_37(evt,_3d);
});
on(_3c,_22.release,function(evt){
_37(evt,_3d);
_3d=null;
});
on(_3c,"focusin, focusout",function(evt){
var _3e=evt.target;
if(_3e._cssState&&!_3e.getAttribute("widgetId")){
var _3f=_23.getEnclosingWidget(_3e);
if(_3f){
_3f._subnodeCssMouseEvent(_3e,_3e._cssState,evt);
}
}
});
});
return _24;
});
},"curam/app/ExternalApplication":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/aspect","dojo/dom-attr","dojo/query","dijit/_Widget","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/form/Button","dojo/text!./templates/ExternalApplication.html","curam/util/UIMFragment","dojo/dom","dojo/dom-class","dojo/dom-style","curam/ui/ClientDataAccessor","curam/widget/containers/TransitionContainer","dojo/on","curam/widget/menu/MenuPane","dijit/CheckedMenuItem","dojo/fx","dijit/focus","dojo/dom-geometry","idx/widget/MenuBar","idx/widget/Menu","idx/app/Header","idx/widget/MenuDialog","idx/widget/MenuHeading","idx/widget/HoverHelpTooltip","dijit/PopupMenuBarItem","dijit/MenuItem","dijit/form/ComboButton","curam/widget/menu/BannerMenuItem","curam/widget/menu/MegaMenu","curam/util/SessionTimeout","curam/util/ui/AppExitConfirmation"],function(_40,_41,win,_42,_43,_44,_45,_46,_47,_48,_49,_4a,_4b,_4c,dom,_4d,_4e,_4f,_50,_51,_52,_53,fx,_54,_55){
return _40("curam.app.ExternalApplication",[_45,_46,_47],{started:false,templateString:_4b,widgetsInTemplate:true,baseClass:"curamApp",_appConfig:null,_initializedNavBarID:null,guardAgainstLeaving:null,directLinkData:null,postMixInProperties:function(){
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this._init();
this._setupUserLeavingGuard();
},_isNavBarItem:function(_56){
return (this._appConfig.map[_56]!=null);
},_init:function(){
var da=new _4f();
da.getRaw("/config/tablayout/extapp["+curam.config.appID+"]",_41.hitch(this,function(_57){
console.log("External App config data:"+_57);
this._appConfig=_57;
this._postDataLoadInit();
}),function(_58,_59){
console.log("External App config data load error:"+_58);
},null);
_42.before(dijit.popup,"open",this._evenOutMenuRows,true);
_42.after(dijit.popup,"open",_41.hitch(this,"_animateMenuOpen"),true);
_42.after(dijit.popup,"close",_41.hitch(this,"_animateMenuClose"),true);
this._bannerResizeTimer=null;
_51(window,"resize",dojo.hitch(this,function(){
if(this._bannerResizeTimer){
clearTimeout(this._bannerResizeTimer);
}
this._bannerResizeTimer=setTimeout(this._handleBannerResize,100);
}));
},_setupUserLeavingGuard:function(){
if(this.guardAgainstLeaving){
curam.util.ui.AppExitConfirmation.install();
}
},_loadLandingPage:function(_5a){
if(curam.config.landingPage){
this._displayOnlyBodyContent({pageID:curam.config.landingPage});
}else{
throw "ERROR: Landing page not set correctly: "+curam.config.landingPage;
}
},_loadBanner:function(){
_4c.get({url:"CDEJ/extapp/application-banner-fragment.jspx",targetID:this._appBanner.id,onLoad:_41.hitch(this,this._initializeBannerLandingPageLink)});
},_reloadBanner:function(){
if(dijit.byId("MMMenuID")){
dijit.byId("MMMenuID").destroy();
}
if(dijit.byId("appMegaMenu")){
dijit.byId("appMegaMenu").destroy();
}
if(dijit.byId("appHelpMenu")){
dijit.byId("appHelpMenu").destroy();
}
if(dijit.byId("appBannerPrintMenu")){
dijit.byId("appBannerPrintMenu").destroy();
}
this._loadBanner();
},_initializeBannerLandingPageLink:function(_5b){
var _5c=_44(".idxHeaderPrimaryTitle",_5b.domNode)[0];
if(!_5c){
throw "Landing Page link not initialized, title node cannot be found";
}
var _5d=_41.hitch(this,"_loadLandingPage");
dojo.connect(_5c,"onclick",function(){
_5d();
});
},_animateMenuOpen:function(_5e){
var _5f=dom.byId(_5e.popup._popupWrapper.id);
if(_43.get(_5f,"dijitpopupparent")!=="appMegaMenu"&!_4d.contains(_5f,"oneuiHeaderGlobalActionsMenu_help")){
return;
}
if(!_5f.aniTable){
_5f.aniTable=dojo.query("table",_5f)[0];
}
if(!_5f.inAnimation){
_5f.inAnimation=false;
}
if(!_5f.isShown){
_5f.isShown=false;
}
if(_5f.inAnimation){
if(dijit.byId("appHelpMenu")===_5e.popup){
if(_5e.popup.shouldNotClose){
_5e.popup.shouldNotClose=false;
_5e.popup.cancelClose=true;
return;
}
}
_5f.fx&&_5f.fx.stop();
_5f.inAnimation=false;
_5f.isShown=false;
_4e.set(_5f,"display","none");
}
if(!_5f.isShown&&!_5f.inAnimation){
dojo.style(_5f,"display","none");
dojo.style(_5f.aniTable,"opacity","0");
}
var _60=function(){
_5f.inAnimation=true;
_5f.isShown=false;
if(dijit.byId("appHelpMenu")===_5e.popup){
_5e.popup.shouldNotClose=_5e.popup.shouldNotClose?false:true;
_5e.popup.justClosed=false;
}
};
var _61=function(){
dojo.style(_5f.aniTable,"opacity","1");
_5f.inAnimation=false;
_5f.isShown=true;
var _62=dojo.marginBox(_5f).h;
var _63=dojo.window.getBox().h-65-20;
if(_63<_62){
_4e.set(_5f,"height",_63+"px");
_4e.set(_5f,"border-bottom","1px solid black");
}
};
this._animateMenu(_5f,_5f.aniTable,"open",_60,_61);
},_animateMenuClose:function(_64){
var _65=dom.byId(_64._popupWrapper.id);
if(_43.get(_65,"dijitpopupparent")!=="appMegaMenu"&!_4d.contains(_65,"oneuiHeaderGlobalActionsMenu_help")){
return;
}
if(dijit.byId("appHelpMenu")===_64){
if(_64.cancelClose){
_64.cancelClose=false;
return;
}else{
if(_64.justClosed){
_64.justClosed=false;
return;
}
}
}
if(!_65.aniTable){
_65.aniTable=dojo.query("table",_65)[0];
}
if(_65.inAnimation){
_65.fx&&_65.fx.stop();
_65.inAnimation=false;
_65.isShown=false;
dojo.style(_65.aniTable,"opacity","1");
}
_4e.set(_65,"display","block");
var _66=function(){
_65.inAnimation=true;
_65.isShown=true;
};
var _67=function(){
_65.inAnimation=false;
_65.isShown=false;
_4e.set(_65,"display","none");
_4e.set(_65,"border-bottom","");
if(dijit.byId("appHelpMenu")===_64){
_64.shouldNotClose=false;
_64.justClosed=true;
}
};
this._animateMenu(_65,_65.aniTable,"close",_66,_67);
},_animateMenu:function(_68,_69,_6a,_6b,_6c){
var _6d=300;
var _6e=[];
var _6f=fx.wipeIn({node:_68,duration:_6d});
var _70=dojo.fadeIn({node:_69,duration:_6d});
var _71=fx.wipeOut({node:_68,duration:_6d});
var _72=dojo.fadeOut({node:_69,duration:_6d});
if(_6a==="open"){
_6e.push(_6f);
_6e.push(_70);
}else{
_6e.push(_72);
_6e.push(_71);
}
_68.fx=fx.chain(_6e);
dojo.connect(_68.fx,"onBegin",_6b);
dojo.connect(_68.fx,"onEnd",_6c);
_68.fx.play();
},_evenOutMenuRows:function(_73){
var _74=dom.byId(_73.popup.id);
var _75=_73.parent?_73.parent.id:_73.popup.id;
if(_75!=="appMegaMenu"&_75!=="appHelpMenu"){
return;
}
var _76,_77;
if(_75==="appMegaMenu"){
_76="MMItemContainerRow";
_77="MMItemContainer";
}else{
_76="HMItemContainerRow";
_77="HMItemContainer";
}
var _78=dojo.query("div."+_77,_74);
var _79=_73.popup._popupWrapper?_73.popup._popupWrapper:_74;
_4e.set(_79,"display","block");
var _7a=_78.length;
var _7b=_7a<6?1:Math.ceil(_7a/3);
var _7c=[];
for(var i=0;i<_7b;i++){
_7c[i]=0;
}
for(var _7d=0;_7d<_7b;_7d++){
dojo.forEach(_78,function(_7e,_7f){
_4e.set(_7e,"height","auto");
if(_7d===0&&!_4d.contains(_7e,"iconSetOUI")){
var _80=_7e;
while(!_4d.contains(_80,"menuItemClassOUI")){
_80=_80.parentNode;
}
_80=dojo.query("td.dijitMenuItemIconCell",_80)[0];
if(_7b===1){
_4e.set(_80,"width","50px");
}else{
_4e.set(_80,"width","34px");
}
_4d.add(_7e,"iconSetOUI");
}
var _81=_4e.get(_7e,"height");
if(_4d.contains(_7e,_76+_7d)){
if(_81>_7c[_7d]){
_7c[_7d]=_81;
}
}
});
}
_4e.set(_79,"display","none");
for(var i=0;i<_7b;i++){
dojo.forEach(_78,function(_82,_83){
if(_4d.contains(_82,_76+i)){
_4e.set(_82,"height",_7c[i]+"px");
}
});
}
},_handleBannerResize:function(_84){
CuramExternalApp._oneuiBanner=_84||CuramExternalApp._oneuiBanner;
var _85=CuramExternalApp._oneuiBanner;
var _86=_85._helpNode?true:false;
var _87=_85.userNode?true:false;
var _88=_85.navigation?true:false;
var _89=_85.logoExists;
var _8a=_85._settingsNode?true:false;
if(_8a){
if(_43.get(_85._settingsNode,"title")){
_43.set(_85._settingsNode,"title",CuramExternalApp._appConfig.printMenuLabel);
}
if(_43.get(_85._settingsNode,"alt")){
_43.set(_85._settingsNode,"alt",CuramExternalApp._appConfig.printMenuLabel);
}
}
var _8b=_85.isLeftToRight();
if(!_87&&!_88){
return;
}
var _8c=dojo.query(".idxHeaderPrimaryTitleContainer",_85._leadingGlobalActionsNode)[0];
var _8d=0;
if(_89){
var _8e=dojo.query(".idxHeaderLogoBox",_85._trailingGlobalActionsNode)[0];
_8d=_55.getMarginBox(_8e).w;
}
var _8f=_55.position(win.body()).w;
var _90=_8f-_55.getMarginBox(_8c).w-(_86?_55.getMarginBox(_85._helpNode).w:0)-(_8a?_55.getMarginBox(_85._settingsNode).w:0)-_8d;
var _91=_86?_8b?_55.position(_85._helpNode).x:box(_85._mainContainerNode).w+box(_85._helpNode).w-_55.position(_85._helpNode).x:885;
if(_87){
var _92=_85.userNode;
var _93=_85.userTextNode;
_4e.set(_93,"width","");
_4e.set(_93,"display","");
_4e.set(_93,"bottom","");
var _94=_55.getMarginBox(_92).w;
var _95=_55.getMarginBox(_93).w;
}
if(_88){
var _96=_85.navigation.domNode;
var _97=dojo.query("span[id*=text]",_96)[0];
_4e.set(_97,"width","");
_4e.set(_97,"display","");
_4e.set(_96,"margin-left","");
_4e.set(_96,"vertical-align","");
var _98=_55.getMarginBox(_96).w;
var _99=_55.getMarginBox(_97).w;
}
var _9a=_90-(_88?(_98-_99):0)-(_87?(_94-_95):0);
var _9b=(_88?_99:0)+(_87?_95:0);
if(_9a-_9b<0){
var _9c=_9a/2;
var _9d;
var _9e=[];
if(_88&_87){
if(_95<_9c){
_9d=_9a-_95;
_9e.push(_97);
}else{
if(_99<_9c){
_9d=_9a-_99;
_9e.push(_93);
}else{
_9d=_9c;
_9e.push(_97);
_9e.push(_93);
}
}
}else{
if(_88){
_9d=_9a;
_9e.push(_97);
}else{
_9d=_9a;
_9e.push(_93);
}
}
for(var i=0;i<_9e.length;i++){
_4e.set(_9e[i],"width",_9d+"px");
_4e.set(_9e[i],"display","inline-block");
if(_85.userTextNode===_9e[i]){
_4e.set(_9e[i],"bottom","3px");
}else{
_4e.set(_9e[i],"vertical-align","top");
}
}
}
if(_88){
var _9f=_8f-_55.position(_85._trailingGlobalActionsNode).x;
_4e.set(_85._leadingGlobalActionsNode,"right",_9f+"px");
}
_4e.set(_96,_8b?"right":"left",_9f+"px");
},_postDataLoadInit:function(){
this._appContentBody._doResourceLookUp=_41.hitch(this,this._doResourceLookUpForMainBody);
this._appNav._onSelectAfter=_41.hitch(this,function(_a0){
this._appContentBody.set("displayPanel",_a0);
});
this._makeNavBarAccessible();
this._loadBanner();
if(this.directLinkData){
if(this._isNavBarItem(this.directLinkData.pageID)){
this._initNavBar(this.directLinkData.pageID,_41.hitch(this,function(){
this.directLinkData.isDirectLink=true;
this._displayNavMenuAndBodyContent(this.directLinkData);
}));
}else{
this._displayOnlyBodyContent(this.directLinkData);
}
}else{
this._loadLandingPage();
}
},_initNavBar:function(_a1,_a2){
var _a3=this._appConfig.map[_a1];
if(typeof (_a3)=="undefined"||_a3==this._initializedNavBarID){
_a2();
return;
}
var da=new _4f();
da.getRaw("/config/tablayout/extnav["+curam.config.appID+"]["+_a3+"]",_41.hitch(this,function(_a4){
console.log("External App config data:"+_a4);
this._loadMenuItems(_a4.navItems,_a4.navBarPixelWidth);
_a2();
this._initializedNavBarID=_a3;
}),function(_a5,_a6){
console.log("External App navigation config data load error:"+_a5);
},null);
},_makeNavBarAccessible:function(){
var _a7=dojo.query(".idxOneuiHoverCardCloseIcon")[0];
if(_a7){
_43.set(_a7,"tabindex",-1);
_43.set(_a7,"aria-label",this._appConfig.hoverCardCloseButtonLabel);
}
var _a8=dijit.byId("navOverflowButton");
_a8._setLabelAttr(this._appConfig.navOverflowButtonLabel);
},_loadMenuItems:function(_a9,_aa){
var _ab=[];
this._appNav.set("width",_aa);
for(var i=0;i<_a9.length;i++){
var _ac=_a9[i];
var _ad={id:_ac.pageID,label:_ac.title,selected:false,iconPath:_ac.iconPath,subPageIds:_ac.subPageIds,iconClass:"whoKnow"};
_ab.push(_ad);
}
this._appNav.addMenuItems(_ab);
},megaMenuClick:function(_ae){
if(typeof (_ae.displayNavBar)=="undefined"){
_ae.displayNavBar=false;
}
this.displayContent(_ae);
},displayContent:function(_af){
if(_af!=null){
_af.forceRefresh=true;
if(_af.displayNavBar==false){
this._displayOnlyBodyContent(_af);
return;
}
if(_af.displayNavBar==true){
this._displayNavMenuAndBodyContent(_af);
return;
}
if(_af.pageID==curam.config.landingPage){
this._displayOnlyBodyContent(_af);
return;
}
if(this._isNavBarItem(_af.pageID)){
this._displayNavMenuAndBodyContent(_af);
return;
}else{
if(this._appNav._showing){
this._displayNavMenuAndBodyContent(_af);
return;
}else{
this._displayOnlyBodyContent(_af);
return;
}
}
}
},_displayOnlyBodyContent:function(_b0){
if(this._appNav._showing){
var _b1=this.connect(this._appContentBody,"_panelFadeOutComplete",_41.hitch(this,function(){
_b1.remove();
var _b2=this.connect(this._appNav,"_onHideComplete",_41.hitch(this,function(){
this._appNav.deselect();
_b2.remove();
_b0.key=_b0.pageID;
this._appContentBody.set("displayPanel",_b0);
}));
this._appNav.fadeOut();
}));
this._appContentBody.fadeOutDisplay();
}else{
_b0.key=_b0.pageID;
this._appContentBody.set("displayPanel",_b0);
}
},_displayNavMenuAndBodyContent:function(_b3){
_b3.key=_b3.pageID;
if(_b3.param==null){
_b3.param=[];
}
_b3.exceptionButtonFound=false;
if(this._appNav._showing){
this._appNav.setSelectedButton(_b3);
}else{
if(_b3.isDirectLink){
var _b4=this.connect(this._appNav,"_onShowComplete",_41.hitch(this,function(){
_b4.remove();
this._appNav.setSelectedButton(_b3);
}));
this._appNav.fadeIn();
}else{
var _b5=this.connect(this._appContentBody,"_panelFadeOutComplete",_41.hitch(this,function(){
_b5.remove();
var _b6=this.connect(this._appNav,"_onShowComplete",_41.hitch(this,function(){
_b6.remove();
this._appNav.setSelectedButton(_b3);
}));
this._appNav.fadeIn();
}));
this._appContentBody.fadeOutDisplay();
}
}
},_doResourceLookUpForMainBody:function(_b7,_b8,_b9){
var uri;
if(_b7.key){
if(this._isUIMFragment(_b7.key)){
uri=jsL+"/"+_b7.key+"Page.do?"+this._addCDEJParameters();
}else{
uri=jsL+"/UIMIFrameWrapperPage.do?uimPageID="+_b7.key+"&"+this._addCDEJParameters();
}
}else{
if(_b7.url){
uri=_b7.url;
}
}
return uri;
},_addCDEJParameters:function(){
return jsScreenContext.toRequestString();
},updateMainContentIframe:function(_ba){
var _bb=dojo.query("iframe",this.domNode)[0];
if(_bb){
_bb.contentWindow.location.href=_ba;
}
},_isUIMFragment:function(_bc){
return (this._appConfig&&this._appConfig.uimFragRegistry[_bc]!=null);
},_setupUserMenuLinking:function(_bd,_be){
dojo.connect(_bd,"onclick",_41.partial(function(_bf,evt){
var _c0=dom.byId("curam-extapp_userMenuArrow");
if(evt.target!=_c0){
displayContent(_bf);
}
},_be));
dojo.connect(_bd,"onkeypress",function(evt){
if(evt.charOrCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
displayContent(_be);
}
});
},_makeUserMenuAccessible:function(_c1){
var _c2=_c1.userNode;
var _c3=_c1.userTextNode;
_43.set(_c2,"tabindex","3");
_43.set(_c3,"title",_c3.innerText);
var _c4=dojo.query(".idxHeaderDropDownArrow",_c2)[0];
_43.set(_c4,"tabindex","4");
_43.set(_c4,"role","button");
_43.set(_c4,"title",_c3.innerText);
this._handleUserImageHighContrast(_c2);
},_handleUserImageHighContrast:function(_c5){
var _c6=dojo.query(".idxHeaderUserIcon",_c5)[1];
if(_c6){
var _c7=win.body();
if(_c7&&_4d.contains(_c7,"high-contrast")){
_c6.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
_51(_c6,"mouseover",function(){
_c6.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_roll.png]";
});
_51(_c6,"click",function(){
_c6.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_click.png]";
});
_51(_c6,"mouseout",function(){
_c6.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
});
}
}
},_makeMegaMenuAccessible:function(_c8){
var _c9=dojo.query("span[id*=text]",_c8.domNode)[0];
_43.set(_c9,"title",_c9.innerText);
},_makeHelpMenuAccessible:function(_ca){
_43.set(_ca,"tabindex","6");
_43.set(_ca,"role","button");
dojo.connect(_ca,"onkeydown",function(evt){
if(evt.keyCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
dijit.byId("appHelpMenu")._scheduleOpen(evt.target);
}
});
},_makePrintMenuAccessible:function(_cb){
var _cc=_cb._settingsNode;
_43.set(_cc,"tabindex","5");
_43.set(_cc,"role","button");
dojo.connect(_cc,"onkeydown",function(evt){
if(evt.keyCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
dijit.byId("appBannerPrintMenu")._scheduleOpen(evt.target);
}
});
},_setupUserMenuHoverCard:function(_cd){
var _ce=_42.after(idx.app.Header.prototype,"_renderUser",function(){
_4d.add(this.userNode,"idxHeaderDropDown");
var _cf=dojo.query(".idxHeaderDropDownArrow",this.userNode)[0];
_cf.id="curam-extapp_userMenuArrow";
dojo.connect(_cf,"onclick",idx.widget.HoverHelpTooltip.defaultPosition=["below"]);
_ce.remove();
});
if(dojo.isIE!==7){
_42.before(idx.widget.HoverHelpTooltip,"show",function(){
var _d0=dom.byId("curam-extapp_userMenuArrow");
var _d1=CuramExternalApp._oneuiBanner.isLeftToRight();
_4e.set(_d0,_d1?{"position":"fixed","top":"30px","right":"21px"}:{"position":"fixed","top":"30px","left":"21px"});
});
_42.after(idx.widget.HoverHelpTooltip,"show",function(){
_4e.set(dom.byId("curam-extapp_userMenuArrow"),"position","static");
});
_42.after(_cd,"onShow",_41.partial(function(_d2){
var _d3="idx_widget__MasterHoverHelpTooltip_0";
if(_d2.lastIndexOf("_")!=-1){
_d3="idx_widget__MasterHoverHelpTooltip_"+_d2.slice(_d2.lastIndexOf("_")+1);
}
var _d4=dom.byId("curam-extapp_userMenuArrow");
var _d5=dijit.byId(_d3);
_d5.focus();
var _d6=_d5.domNode;
var _d7=_d5.connectorNode;
var _d8=(_55.position(_d6).x+_55.position(_d6).w)-(_55.position(_d7).x+_55.position(_d7).w/2);
var _d9=_55.position(dojo.body()).w-(_55.position(_d4).x+_55.position(_d4).w/2)-_d8;
_4e.set(_d6,{"left":"auto","right":_d9+"px"});
var _da=CuramExternalApp._oneuiBanner._settingsNode?true:false;
var _db=oneuiBanner.isLeftToRight();
_4e.set(dom.byId(_d3),_db?{"left":"auto","right":rightVal+"px"}:{"right":"auto","left":(rightVal+14)+"px"});
},_cd.id));
}
},_addHelpMenuCustomClass:function(){
var _dc=dijit.byId("appHelpMenu")._popupWrapper;
if(!_4d.contains(_dc,"oneuiHeaderGlobalActionsMenu_help")){
_4d.add(_dc,"oneuiHeaderGlobalActionsMenu_help");
}
},displayMegaMenuItemInModal:function(_dd){
console.log(_dd);
},_preventJAWSCrashClick:function(_de){
var _df=dojo.query("#"+_de.id+"_text",_de)[0];
if(!_df.isModified){
dojo.query(".wtfoneui",_df).forEach(function(_e0){
_e0.oldInnerText=_e0.innerText;
if(_4d.contains(_e0,"MMtitle")){
_e0.innerText=_e0.innerText.substring(0,229).concat("...");
}else{
var _e1=_e0.previousSibling;
while(!_4d.contains(_e1,"MMtitle")){
_e1=_e1.previousSibling;
}
_e1=_e1.innerText.length;
var _e2=Math.min(250-_e1,Math.max(10,_e0.innerText.length-_e1));
_e0.innerText=_e0.innerText.substring(0,_e2).concat("...");
}
});
_df.isModified=true;
_df.innerModdedTimer&&clearTimeout(_df.innerModdedTimer);
_df.innerModdedTimer=setTimeout(dojo.partial(function(_e3){
if(_df.isModified){
dojo.query(".wtfoneui",_df).forEach(function(_e4){
_e4.innerText=_e4.oldInnerText;
});
_df.isModified=false;
_df["innerModdedTimer"]=undefined;
try{
delete _df.innerModdedTimer;
}
catch(e){
}
}
},_de),2);
}
},_preventJAWSCrashFocus:function(_e5){
var _e6=dojo.query("#"+_e5.id+"_text",_e5)[0];
if(!_e6.isModified){
dojo.query(".wtfoneui",_e6).forEach(function(_e7){
_e7.oldInnerText=_e7.innerText;
if(_4d.contains(_e7,"MMtitle")){
_e7.innerText=_e7.innerText.substring(0,229).concat("...");
}else{
var _e8=_e7.previousSibling;
while(!_4d.contains(_e8,"MMtitle")){
_e8=_e8.previousSibling;
}
_e8=_e8.innerText.length;
var _e9=Math.min(250-_e8,Math.max(10,_e7.innerText.length-_e8));
_e7.innerText=_e7.innerText.substring(0,_e9).concat("...");
}
});
_e6.isModified=true;
_e6.innerModdedTimer&&clearTimeout(_e6.innerModdedTimer);
_e6.innerModdedTimer=setTimeout(dojo.partial(function(_ea){
if(_e6.isModified){
dojo.query(".wtfoneui",_e6).forEach(function(_eb){
_eb.innerText=_eb.oldInnerText;
});
_e6.isModified=false;
_e6["innerModdedTimer"]=undefined;
try{
delete _e6.innerModdedTimer;
}
catch(e){
}
}
},_e5),2);
}
},_preventJAWSCrashBlur:function(_ec){
var _ed=dojo.query("#"+_ec.id+"_text",_ec)[0];
_ed.innerModdedTimer&&clearTimeout(_ed.innerModdedTimer);
if(_ed.isModified){
dojo.query(".wtfoneui",_ed).forEach(function(_ee){
_ee.innerText=_ee.oldInnerText;
});
_ed.isModified=false;
}
},_skipLinkFocus:function(_ef){
_ef=_ef||"app-content";
var _f0=dom.byId(_ef);
if(_f0){
_f0.focus();
}
},_showHideSkipLink:function(e){
var _f1=dom.byId("skipLink");
if(_f1){
var _f2=_f1.parentNode;
if(e.type=="focus"&&_4d.contains(_f2,"hidden")){
_4d.remove(_f2,"hidden");
}else{
if(e.type=="blur"&&!_4d.contains(_f2,"hidden")){
_4d.add(_f2,"hidden");
}
}
}
},print:function(){
var _f3=_44("#app-content iframe.curam-iframe")[0];
console.log("PRINTING IFRAME:"+_f3);
if(_f3){
if(dojo.isIE<11){
console.log("# IE: "+dojo.isIE+", calling iframe.contentWindow.document.execCommand");
_f3.contentWindow.document.execCommand("print",false,null);
}else{
console.log("# Calling iframe.contentWindow.print()");
_f3.contentWindow.print();
}
}else{
console.log("# Calling window.print()");
window.print();
}
}});
});
},"dijit/place":function(){
define(["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","./Viewport","./main"],function(_f4,_f5,_f6,_f7,win,_f8,_f9){
function _fa(_fb,_fc,_fd,_fe){
var _ff=_f8.getEffectiveBox(_fb.ownerDocument);
if(!_fb.parentNode||String(_fb.parentNode.tagName).toLowerCase()!="body"){
win.body(_fb.ownerDocument).appendChild(_fb);
}
var best=null;
_f4.some(_fc,function(_100){
var _101=_100.corner;
var pos=_100.pos;
var _102=0;
var _103={w:{"L":_ff.l+_ff.w-pos.x,"R":pos.x-_ff.l,"M":_ff.w}[_101.charAt(1)],h:{"T":_ff.t+_ff.h-pos.y,"B":pos.y-_ff.t,"M":_ff.h}[_101.charAt(0)]};
var s=_fb.style;
s.left=s.right="auto";
if(_fd){
var res=_fd(_fb,_100.aroundCorner,_101,_103,_fe);
_102=typeof res=="undefined"?0:res;
}
var _104=_fb.style;
var _105=_104.display;
var _106=_104.visibility;
if(_104.display=="none"){
_104.visibility="hidden";
_104.display="";
}
var bb=_f5.position(_fb);
_104.display=_105;
_104.visibility=_106;
var _107={"L":pos.x,"R":pos.x-bb.w,"M":Math.max(_ff.l,Math.min(_ff.l+_ff.w,pos.x+(bb.w>>1))-bb.w)}[_101.charAt(1)],_108={"T":pos.y,"B":pos.y-bb.h,"M":Math.max(_ff.t,Math.min(_ff.t+_ff.h,pos.y+(bb.h>>1))-bb.h)}[_101.charAt(0)],_109=Math.max(_ff.l,_107),_10a=Math.max(_ff.t,_108),endX=Math.min(_ff.l+_ff.w,_107+bb.w),endY=Math.min(_ff.t+_ff.h,_108+bb.h),_10b=endX-_109,_10c=endY-_10a;
_102+=(bb.w-_10b)+(bb.h-_10c);
if(best==null||_102<best.overflow){
best={corner:_101,aroundCorner:_100.aroundCorner,x:_109,y:_10a,w:_10b,h:_10c,overflow:_102,spaceAvailable:_103};
}
return !_102;
});
if(best.overflow&&_fd){
_fd(_fb,best.aroundCorner,best.corner,best.spaceAvailable,_fe);
}
var top=best.y,side=best.x,body=win.body(_fb.ownerDocument);
if(/relative|absolute/.test(_f6.get(body,"position"))){
top-=_f6.get(body,"marginTop");
side-=_f6.get(body,"marginLeft");
}
var s=_fb.style;
s.top=top+"px";
s.left=side+"px";
s.right="auto";
return best;
};
var _10d={"TL":"BR","TR":"BL","BL":"TR","BR":"TL"};
var _10e={at:function(node,pos,_10f,_110,_111){
var _112=_f4.map(_10f,function(_113){
var c={corner:_113,aroundCorner:_10d[_113],pos:{x:pos.x,y:pos.y}};
if(_110){
c.pos.x+=_113.charAt(1)=="L"?_110.x:-_110.x;
c.pos.y+=_113.charAt(0)=="T"?_110.y:-_110.y;
}
return c;
});
return _fa(node,_112,_111);
},around:function(node,_114,_115,_116,_117){
var _118;
if(typeof _114=="string"||"offsetWidth" in _114||"ownerSVGElement" in _114){
_118=_f5.position(_114,true);
if(/^(above|below)/.test(_115[0])){
var _119=_f5.getBorderExtents(_114),_11a=_114.firstChild?_f5.getBorderExtents(_114.firstChild):{t:0,l:0,b:0,r:0},_11b=_f5.getBorderExtents(node),_11c=node.firstChild?_f5.getBorderExtents(node.firstChild):{t:0,l:0,b:0,r:0};
_118.y+=Math.min(_119.t+_11a.t,_11b.t+_11c.t);
_118.h-=Math.min(_119.t+_11a.t,_11b.t+_11c.t)+Math.min(_119.b+_11a.b,_11b.b+_11c.b);
}
}else{
_118=_114;
}
if(_114.parentNode){
var _11d=_f6.getComputedStyle(_114).position=="absolute";
var _11e=_114.parentNode;
while(_11e&&_11e.nodeType==1&&_11e.nodeName!="BODY"){
var _11f=_f5.position(_11e,true),pcs=_f6.getComputedStyle(_11e);
if(/relative|absolute/.test(pcs.position)){
_11d=false;
}
if(!_11d&&/hidden|auto|scroll/.test(pcs.overflow)){
var _120=Math.min(_118.y+_118.h,_11f.y+_11f.h);
var _121=Math.min(_118.x+_118.w,_11f.x+_11f.w);
_118.x=Math.max(_118.x,_11f.x);
_118.y=Math.max(_118.y,_11f.y);
_118.h=_120-_118.y;
_118.w=_121-_118.x;
}
if(pcs.position=="absolute"){
_11d=true;
}
_11e=_11e.parentNode;
}
}
var x=_118.x,y=_118.y,_122="w" in _118?_118.w:(_118.w=_118.width),_123="h" in _118?_118.h:(_f7.deprecated("place.around: dijit/place.__Rectangle: { x:"+x+", y:"+y+", height:"+_118.height+", width:"+_122+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_118.height+", w:"+_122+" }","","2.0"),_118.h=_118.height);
var _124=[];
function push(_125,_126){
_124.push({aroundCorner:_125,corner:_126,pos:{x:{"L":x,"R":x+_122,"M":x+(_122>>1)}[_125.charAt(1)],y:{"T":y,"B":y+_123,"M":y+(_123>>1)}[_125.charAt(0)]}});
};
_f4.forEach(_115,function(pos){
var ltr=_116;
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
var _127=_fa(node,_124,_117,{w:_122,h:_123});
_127.aroundNodePos=_118;
return _127;
}};
return _f9.place=_10e;
});
},"dijit/_HasDropDown":function(){
define(["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","./registry","./focus","./popup","./_FocusMixin"],function(_128,_129,dom,_12a,_12b,_12c,_12d,has,keys,lang,on,_12e,_12f,_130,_131,_132){
return _128("dijit._HasDropDown",_132,{_buttonNode:null,_arrowWrapperNode:null,_popupStateNode:null,_aroundNode:null,dropDown:null,autoWidth:true,forceWidth:false,maxHeight:-1,dropDownPosition:["below","above"],_stopClickEvents:true,_onDropDownMouseDown:function(e){
if(this.disabled||this.readOnly){
return;
}
if(e.type!="MSPointerDown"&&e.type!="pointerdown"){
e.preventDefault();
}
this.own(on.once(this.ownerDocument,_12e.release,lang.hitch(this,"_onDropDownMouseUp")));
this.toggleDropDown();
},_onDropDownMouseUp:function(e){
var _133=this.dropDown,_134=false;
if(e&&this._opened){
var c=_12c.position(this._buttonNode,true);
if(!(e.pageX>=c.x&&e.pageX<=c.x+c.w)||!(e.pageY>=c.y&&e.pageY<=c.y+c.h)){
var t=e.target;
while(t&&!_134){
if(_12b.contains(t,"dijitPopup")){
_134=true;
}else{
t=t.parentNode;
}
}
if(_134){
t=e.target;
if(_133.onItemClick){
var _135;
while(t&&!(_135=_12f.byNode(t))){
t=t.parentNode;
}
if(_135&&_135.onClick&&_135.getParent){
_135.getParent().onItemClick(_135,e);
}
}
return;
}
}
}
if(this._opened){
if(_133.focus&&(_133.autoFocus!==false||(e.type=="mouseup"&&!this.hovering))){
this._focusDropDownTimer=this.defer(function(){
_133.focus();
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
var _136={"after":this.isLeftToRight()?"Right":"Left","before":this.isLeftToRight()?"Left":"Right","above":"Up","below":"Down","left":"Left","right":"Right"}[this.dropDownPosition[0]]||this.dropDownPosition[0]||"Down";
_12b.add(this._arrowWrapperNode||this._buttonNode,"dijit"+_136+"ArrowButton");
},postCreate:function(){
this.inherited(arguments);
var _137=this.focusNode||this.domNode;
this.own(on(this._buttonNode,_12e.press,lang.hitch(this,"_onDropDownMouseDown")),on(this._buttonNode,"click",lang.hitch(this,"_onDropDownClick")),on(_137,"keydown",lang.hitch(this,"_onKey")),on(_137,"keyup",lang.hitch(this,"_onKeyUp")));
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
var d=this.dropDown,_138=e.target;
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
if(!this._opened&&(e.keyCode==keys.DOWN_ARROW||((e.keyCode==keys.ENTER||(e.keyCode==keys.SPACE&&(!this._searchTimer||(e.ctrlKey||e.altKey||e.metaKey))))&&((_138.tagName||"").toLowerCase()!=="input"||(_138.type&&_138.type.toLowerCase()!=="text"))))){
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
},loadDropDown:function(_139){
_139();
},loadAndOpenDropDown:function(){
var d=new _129(),_13a=lang.hitch(this,function(){
this.openDropDown();
d.resolve(this.dropDown);
});
if(!this.isLoaded()){
this.loadDropDown(_13a);
}else{
_13a();
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
var _13b=this.dropDown,_13c=_13b.domNode,_13d=this._aroundNode||this.domNode,self=this;
var _13e=_131.open({parent:this,popup:_13b,around:_13d,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
self.closeDropDown(true);
},onCancel:function(){
self.closeDropDown(true);
},onClose:function(){
_12a.set(self._popupStateNode,"popupActive",false);
_12b.remove(self._popupStateNode,"dijitHasDropDownOpen");
self._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_13d.offsetWidth>_13b._popupWrapper.offsetWidth)){
var _13f=_13d.offsetWidth-_13b._popupWrapper.offsetWidth;
var _140={w:_13b.domNode.offsetWidth+_13f};
this._origStyle=_13c.style.cssText;
if(lang.isFunction(_13b.resize)){
_13b.resize(_140);
}else{
_12c.setMarginBox(_13c,_140);
}
if(_13e.corner[1]=="R"){
_13b._popupWrapper.style.left=(_13b._popupWrapper.style.left.replace("px","")-_13f)+"px";
}
}
_12a.set(this._popupStateNode,"popupActive","true");
_12b.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_13b.id);
if(_13c.getAttribute("role")!=="presentation"&&!_13c.getAttribute("aria-labelledby")){
_13c.setAttribute("aria-labelledby",this.id);
}
return _13e;
},closeDropDown:function(_141){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(_141&&this.focus){
this.focus();
}
_131.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
}});
});
},"curam/util/Request":function(){
define(["dojo/_base/xhr","curam/debug","curam/util/ResourceBundle","curam/inspection/Layer","curam/util/LocalConfig"],function(xhr,_142,_143,_144,_145){
var _146=new _143("curam.application.Request");
_isLoginPage=null,isLoginPage=function(_147){
if(_isLoginPage){
return _isLoginPage(_147);
}else{
return _147.responseText.indexOf("action=\"j_security_check\"")>0;
}
},errorDisplayHookpoint=function(err,_148){
if(isLoginPage(_148.xhr)){
_142.log(_146.getProperty("sessionExpired"));
}else{
_142.log(_146.getProperty("ajaxError"));
}
_142.log(err);
_142.log("HTTP status was: "+_148.xhr.status);
},_xhr=function(_149,args){
var _14a=_145.readOption("ajaxDebugMode","false")=="true";
var _14b=args.error;
if(_14a){
args.error=function(err,_14c){
if(args.errorHandlerOverrideDefault!==true){
errorDisplayHookpoint(err,_14c);
}
if(_14b){
_14b(err,_14c);
}
};
}
var _14d=_149(args);
return _14d;
};
var _14e={post:function(args){
return _xhr(xhr.post,args);
},get:function(args){
return _xhr(xhr.get,args);
},setLoginPageDetector:function(_14f){
_isLoginPage=_14f;
},checkLoginPage:function(args){
return isLoginPage(args);
}};
_144.register("curam/util/Request",_14e);
return _14e;
});
},"dijit/_MenuBase":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/lang","dojo/mouse","dojo/on","dojo/window","./a11yclick","./registry","./_Widget","./_CssStateMixin","./_KeyNavContainer","./_TemplatedMixin"],function(_150,_151,dom,_152,_153,lang,_154,on,_155,_156,_157,_158,_159,_15a,_15b){
return _151("dijit._MenuBase",[_158,_15b,_15a,_159],{selected:null,_setSelectedAttr:function(item){
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
_153.toggle(this.domNode,"dijitMenuActive",val);
_153.toggle(this.domNode,"dijitMenuPassive",!val);
this._set("activated",val);
},parentMenu:null,popupDelay:500,passivePopupDelay:Infinity,autoFocus:false,childSelector:function(node){
var _15c=_157.byNode(node);
return node.parentNode==this.containerNode&&_15c&&_15c.focus;
},postCreate:function(){
var self=this,_15d=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.containerNode,on.selector(_15d,_154.enter),function(){
self.onItemHover(_157.byNode(this));
}),on(this.containerNode,on.selector(_15d,_154.leave),function(){
self.onItemUnhover(_157.byNode(this));
}),on(this.containerNode,on.selector(_15d,_156),function(evt){
self.onItemClick(_157.byNode(this),evt);
evt.stopPropagation();
}),on(this.containerNode,on.selector(_15d,"focusin"),function(){
self._onItemFocus(_157.byNode(this));
}));
this.inherited(arguments);
},onKeyboardSearch:function(item,evt,_15e,_15f){
this.inherited(arguments);
if(!!item&&(_15f==-1||(!!item.popup&&_15f==1))){
this.onItemClick(item,evt);
}
},_keyboardSearchCompare:function(item,_160){
if(!!item.shortcutKey){
return _160==item.shortcutKey.toLowerCase()?-1:0;
}
return this.inherited(arguments)?1:0;
},onExecute:function(){
},onCancel:function(){
},_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}else{
var _161=this._getTopMenu();
if(_161&&_161._isMenuBar){
_161.focusNext();
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
var _162=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
this._openItemPopup(item,_162);
}else{
this.onExecute();
item._onClick?item._onClick(evt):item.onClick(evt);
}
},_openItemPopup:function(_163,_164){
if(_163==this.currentPopupItem){
return;
}
if(this.currentPopupItem){
this._stopPendingCloseTimer();
this.currentPopupItem._closePopup();
}
this._stopPopupTimer();
var _165=_163.popup;
_165.parentMenu=this;
this.own(this._mouseoverHandle=on.once(_165.domNode,"mouseover",lang.hitch(this,"_onPopupHover")));
var self=this;
_163._openPopup({parent:this,orient:this._orient||["after","before"],onCancel:function(){
if(_164){
self.focusChild(_163);
}
self._cleanUp();
},onExecute:lang.hitch(this,"_cleanUp",true),onClose:function(){
if(self._mouseoverHandle){
self._mouseoverHandle.remove();
delete self._mouseoverHandle;
}
}},_164);
this.currentPopupItem=_163;
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
_152.set(this.selected.focusNode,"tabIndex",this.tabIndex);
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
},_cleanUp:function(_166){
this._closeChild();
if(typeof this.isShowingNow=="undefined"){
this.set("activated",false);
}
if(_166){
this.set("selected",null);
}
}});
});
},"curam/dialog":function(){
define(["dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/on","dojo/sniff","curam/inspection/Layer","curam/util","curam/debug","curam/util/external","curam/util/Refresh","curam/tab","curam/util/RuntimeContext","curam/util/ScreenContext","curam/define","curam/util/onLoad","dojo/dom-class","dojo/query","dojo/NodeList-traverse"],function(dom,_167,_168,on,has,_169,util,_16a,_16b,_16c,tab,_16d,_16e,_16f,_170,_171,_172){
curam.define.singleton("curam.dialog",{MODAL_PREV_FLAG:"o3modalprev",MODAL_PREV_FLAG_INPUT:"curam_dialog_prev_marker",FORCE_CLOSE:false,ERROR_MESSAGES_HEADER:"error-messages-header",_hierarchy:[],_id:null,_displayedHandlerUnsToken:null,_displayed:false,_size:null,_justClose:false,_modalExitingIEGScript:false,validTargets:{"_top":true,"_self":true},initModal:function(_173,_174,_175){
curam.dialog.pageId=_173;
curam.dialog.messagesExist=_174;
var _176=false;
var p1;
util.extendXHR();
var _177=util.getTopmostWindow();
var _178=false;
var _179=_177.dojo.subscribe("/curam/dialog/SetId",this,function(_17a){
_16a.log("curam.dialog: "+_16a.getProperty("curam.dialog.id"),_17a);
curam.dialog._id=_17a;
_178=true;
_177.dojo.unsubscribe(_179);
});
_177.dojo.publish("/curam/dialog/init");
if(!_178){
_16a.log("curam.dialog: "+_16a.getProperty("curam.dialog.no.id"));
_177.dojo.unsubscribe(_179);
}
if(curam.dialog.closeDialog(false,_175)){
return;
}
curam.dialog._displayedHandlerUnsToken=util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",null,function(_17b,size){
if(_17b==curam.dialog._id){
curam.dialog._displayed=true;
curam.dialog._size=size;
util.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
});
if(_17c==undefined){
var _17c=this.jsScreenContext;
if(!_17c){
_17c=new _16e();
_17c.addContextBits("MODAL");
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_17c.addContextBits("AGENDA");
}
curam.util.external.inExternalApp()&&_17c.addContextBits("EXTAPP");
}
}
if(_17c.hasContextBits("AGENDA")||_17c.hasContextBits("TREE")){
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
var _17d=dom.byId("o3ctx");
var sc=new curam.util.ScreenContext(_17c.getValue());
sc.addContextBits("ACTION|ERROR");
_17d.value=sc.getValue();
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
var _17e=on(window,"unload",function(){
_17e.remove();
util.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}
if(_178){
dojo.publish("/curam/dialog/ready");
}
},setVariableForModalExitingIEGScript:function(){
_modalExitingIEGScript=true;
},closeDialog:function(_17f,_180){
if(_17f){
curam.dialog.forceClose();
}
var _181=curam.dialog.checkClose(curam.dialog.pageId,_180);
if(_181){
util.onLoad.addPublisher(function(_182){
_182.modalClosing=true;
});
if(curam.dialog.messagesExist){
dojo.addOnLoad(function(){
var _183=dom.byId(util.ERROR_MESSAGES_CONTAINER);
var _184=dom.byId(util.ERROR_MESSAGES_LIST);
var _185=dom.byId(curam.dialog.ERROR_MESSAGES_HEADER);
if(_184&&_185){
util.saveInformationalMsgs(_181);
util.disableInformationalLoad();
}else{
_181();
}
});
}else{
_181();
}
return true;
}
return false;
},addFormInput:function(form,type,name,_186){
return _168.create("input",{"type":type,"name":name,"value":_186},form);
},checkClose:function(_187,_188){
if(curam.dialog._justClose){
return function(){
curam.dialog.closeModalDialog();
};
}
var _189=curam.dialog.getParentWindow(window);
if(!_189){
return false;
}
var href;
if(_188){
href=curam.util.retrieveBaseURL()+_188;
}else{
href=window.location.href;
}
var _18a=curam.dialog.MODAL_PREV_FLAG;
var _18b=util.getUrlParamValue(href,_18a);
var _18c=true;
if(_18b){
if(_189){
if(_18b==_187){
_18c=false;
}
}
}else{
_18c=false;
}
var _18d=util.getUrlParamValue(href,"o3ctx");
if(_18d){
var sc=new curam.util.ScreenContext();
sc.setContext(_18d);
if(sc.hasContextBits("TREE|ACTION")){
_18c=false;
}
}
if(_18c||curam.dialog.FORCE_CLOSE){
if(!curam.dialog.FORCE_CLOSE){
if(_18b=="user-prefs-editor"){
return function(){
if(_189&&_189.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_189);
}
curam.dialog.closeModalDialog();
};
}
return function(){
var rp=util.removeUrlParam;
href=rp(rp(rp(href,_18a),"o3frame"),util.PREVENT_CACHE_FLAG);
href=util.adjustTargetContext(_189,href);
if(_189&&_189.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_189,href,true);
}else{
curam.tab.getTabController().handleLinkClick(href);
}
curam.dialog.closeModalDialog();
};
}else{
return function(){
if(_189!==util.getTopmostWindow()){
_189.curam.util.loadInformationalMsgs();
}
curam.dialog.closeModalDialog();
};
}
}
return false;
},getParentWindow:function(_18e){
if(!_18e){
_16a.log(["curam.dialog.getParentWindow():",_16a.getProperty("curam.dialog.no.child"),window.location?" "+window.location.href:"[no location]"].join(" "));
_16a.log("returning as parent = ",window.parent.location.href);
return window.parent;
}
var _18f=curam.dialog._getDialogHierarchy();
if(_18f){
for(var i=0;i<_18f.length;i++){
if(_18f[i]==_18e){
var _190=(i>0)?_18f[i-1]:_18f[0];
_16a.log(["curam.dialog.getParentWindow():",_16a.getProperty("curam.dialog.parent.window"),_190.location?_190.location.href:"[no location]"].join(" "));
return _190;
}
}
var ret=_18f.length>0?_18f[_18f.length-1]:undefined;
_16a.log(["curam.dialog.getParentWindow():",_16a.getProperty("curam.dialog.returning.parent"),ret?ret.location.href:"undefined"].join(" "));
return ret;
}
},_getDialogHierarchy:function(){
var _191=util.getTopmostWindow();
_191.require(["curam/dialog"]);
return _191.curam.dialog._hierarchy;
},pushOntoDialogHierarchy:function(_192){
var _193=curam.dialog._getDialogHierarchy();
if(_193&&dojo.indexOf(_193,_192)<0){
_193.push(_192);
_16a.log(_16a.getProperty("curam.dialog.add.hierarchy"),_192.location.href);
_16a.log(_16a.getProperty("curam.dialog.full.hierarchy")+_193.reduce(function(acc,hwin){
return acc+"["+(hwin.location.href||"-")+"]";
}),"");
}
},removeFromDialogHierarchy:function(_194){
var _195=curam.dialog._getDialogHierarchy();
if(!_194||_195[_195.length-1]==_194){
_195.pop();
}else{
_16a.log("curam.dialog.removeFromDialogHierarchy(): "+_16a.getProperty("curam.dialog.ignore.request"));
try{
_16a.log(_194.location.href);
}
catch(e){
_16a.log(e.message);
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
},_isSameBaseUrl:function(href,rtc,_196){
if(href&&href.indexOf("#")==0){
return true;
}
var _197=href.split("?");
var _198=rtc.getHref().split("?");
if(_197[0].indexOf("/")<0){
var _199=_198[0].split("/");
_198[0]=_199[_199.length-1];
}
if(_198[0].indexOf("/")<0){
var _199=_197[0].split("/");
_197[0]=_199[_199.length-1];
}
if(_196&&_196==true){
_197[0]=curam.dialog.stripPageOrActionFromUrl(_197[0]);
_198[0]=curam.dialog.stripPageOrActionFromUrl(_198[0]);
}
if(_197[0]==_198[0]){
return true;
}
return false;
},modalEventHandler:function(_19a){
curam.dialog._doHandleModalEvent(_19a,new curam.util.RuntimeContext(window),curam.dialog.closeModalDialog,curam.dialog.doRedirect);
},_showSpinnerInDialog:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/spinner");
},_doHandleModalEvent:function(e,rtc,_19b,_19c){
var _19d=e.target;
var u=util;
switch(_19d.tagName){
case "INPUT":
if(_167.get(_19d,"type")=="submit"&&typeof _19d.form!="undefined"){
_19d.form.setAttribute("keepModal",_19d.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
}
return true;
case "IMG":
case "SPAN":
case "DIV":
_19d=_172(_19d).closest("A")[0];
if(_19d==null){
return;
}
case "A":
case "BUTTON":
if(_19d._submitButton){
_19d._submitButton.form.setAttribute("keepModal",_19d._submitButton.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
return;
}
break;
default:
return true;
}
var _19e=dojo.stopEvent;
var href=_19d.getAttribute("href")||_19d.getAttribute("data-href");
if(href==""){
_19b();
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
var _19f=_19d.getAttribute("target");
if(_19f&&!curam.dialog.validTargets[_19f]){
return true;
}
if(href&&href.indexOf("/servlet/FileDownload?")>-1){
var _1a0=_168.create("iframe",{src:href},dojo.body());
_1a0.style.display="none";
_19e(e);
return false;
}
if(_171.contains(_19d,"external-link")){
return true;
}
if(util.isSameUrl(href,null,rtc)){
if(href.indexOf("#")<0){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_19c(window,href);
return false;
}
return true;
}
if(href&&curam.dialog._isSameBaseUrl(href,rtc,true)&&!_19d.getAttribute("keepModal")){
_19d.setAttribute("keepModal","true");
}
var _1a1=curam.dialog.getParentWindow(rtc.contextObject());
if(_19d&&_19d.getAttribute){
_19e(e);
if(_19d.getAttribute("keepModal")=="true"){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_19c(window,href);
}else{
if(_1a1){
href=u.removeUrlParam(href,"o3frame");
href=u.removeUrlParam(href,curam.dialog.MODAL_PREV_FLAG);
if(_1a1.location!==util.getTopmostWindow().location){
var _1a2=new curam.util.RuntimeContext(_1a1);
var _1a3=_1a2.getHref();
_1a3=u.removeUrlParam(_1a3,"o3frame");
if(util.isActionPage(_1a3)){
if(!curam.dialog._isSameBaseUrl(href,_1a2,true)){
href=u.adjustTargetContext(_1a1,href);
_19c(_1a1,href);
}
}else{
if(!util.isSameUrl(href,_1a3)){
href=u.adjustTargetContext(_1a1,href);
curam.dialog.doRedirect(_1a1,href);
}
}
}else{
var _1a4=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_1a4.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_19b();
}
}
return false;
}
if(_1a1&&typeof (_19d)=="undefined"||_19d==null||_19d=="_self"||_19d==""){
_19e(e);
href=href.replace(/[&?]o3frame=modal/g,"").replace("%3Fo3frame%3Dmodal","").replace("?o3frame%3Dmodal","");
href=util.updateCtx(href);
if(_1a1.location!==util.getTopmostWindow().location){
_19c(_1a1,href);
}else{
var _1a4=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_1a4.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_19b();
return false;
}
return true;
},formSubmitHandler:function(e){
if(e.type=="submit"&&e.defaultPrevented){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
return false;
}
var _1a5=curam.dialog.getParentWindow(window);
if(typeof _1a5=="undefined"){
return true;
}
e.target.method="post";
e.target.setAttribute("target",window.name);
var _1a6=e.target.action;
var _1a7=curam.dialog.MODAL_PREV_FLAG;
var _1a8=curam.dialog.MODAL_PREV_FLAG_INPUT;
var u=util;
var _1a9=dom.byId(_1a8);
if(_1a9){
_1a9.parentNode.removeChild(_1a9);
}
if(e.target.getAttribute("keepModal")!="true"&&!jsScreenContext.hasContextBits("AGENDA")){
var _1aa="multipart/form-data";
if(e.target.enctype==_1aa||e.target.encoding==_1aa){
e.target.action=u.removeUrlParam(_1a6,_1a7);
_1a9=curam.dialog.addFormInput(e.target,"hidden",_1a7,curam.dialog.pageId);
_1a9.setAttribute("id",_1a8);
_1a9.id=_1a8;
}else{
e.target.action=u.replaceUrlParam(_1a6,_1a7,curam.dialog.pageId);
}
}else{
e.target.action=u.removeUrlParam(_1a6,_1a7);
}
_1a5.curam.util.invalidatePage();
if(!jsScreenContext.hasContextBits("EXTAPP")){
util.firePageSubmittedEvent("dialog");
}
return true;
},forceClose:function(){
curam.dialog.FORCE_CLOSE=true;
},forceParentRefresh:function(){
var _1ab=curam.dialog.getParentWindow(window);
if(!_1ab){
return;
}
_1ab.curam.util.FORCE_REFRESH=true;
},forceParentLocaleRefresh:function(){
var _1ac=curam.dialog.getParentWindow(window);
if(!_1ac){
return;
}
_1ac.curam.util.LOCALE_REFRESH=true;
},closeModalDialog:function(_1ad){
var _1ae=util.getTopmostWindow();
if(curam.dialog._displayedHandlerUnsToken!=null){
_1ae.dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
var _1af=curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP");
if((typeof (curam.dialog._id)=="undefined"||curam.dialog._id==null||!_1af)&&window.frameElement){
var _1b0=window.frameElement.id;
var _1b1=_1b0.substring(7);
curam.dialog._id=_1b1;
_16a.log("curam.dialog.closeModalDialog() "+_16a.getProperty("curam.dialog.modal.id")+_1b1);
}
util.getTopmostWindow().dojo.publish("/curam/dialog/close/appExitConfirmation",[curam.dialog._id]);
_16a.log("publishing /curam/dialog/close/appExitConfirmation for ",curam.dialog._id);
_16a.log("publishing /curam/dialog/close for ",curam.dialog._id);
util.getTopmostWindow().dojo.publish("/curam/dialog/close",[curam.dialog._id,_1ad]);
},parseWindowOptions:function(_1b2){
var opts={};
if(_1b2){
_16a.log("curam.dialog.parseWindowOptions "+_16a.getProperty("curam.dialog.parsing"),_1b2);
var _1b3=_1b2.split(",");
var _1b4;
for(var i=0;i<_1b3.length;i++){
_1b4=_1b3[i].split("=");
opts[_1b4[0]]=_1b4[1];
}
_16a.log("done:",dojo.toJson(opts));
}else{
_16a.log("curam.dialog.parseWindowOptions "+_16a.getProperty("curam.dialog.no.options"));
}
return opts;
},doRedirect:function(_1b5,href,_1b6,_1b7){
window.curamDialogRedirecting=true;
if(!curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP")){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/redirectingModal");
}
_1b5.curam.util.redirectWindow(href,_1b6,_1b7);
},_screenReaderAnnounceCurrentTabOnWizard:function(){
var _1b8=dom.byId("wizard-progress-bar");
if(_1b8){
var _1b9=dom.byId("hideAriaLiveElement");
if(typeof _1b9!=null){
this._createSpanContainingInformationOnCurrentWizardTab(_1b8,_1b9);
}
}
},_stylingAddedToMandatoryIconHelp:function(){
var _1ba=dom.byId("wizard-progress-bar");
var _1bb=dojo.query(".mandatory-icon-help")[0];
if(_1ba&&_1bb){
_171.add(_1bb,"wizard-progress-bar-exists");
}
},_createSpanContainingInformationOnCurrentWizardTab:function(_1bc,_1bd){
var _1be=null;
var _1bf="";
var _1c0=" ";
var _1c1=_172(".title",_1bc)[0]&&_172(".title",_1bc)[0].innerText;
var desc=_172(".desc",_1bc)[0]&&_172(".desc",_1bc)[0].innerText;
if(_1c1&&_1c1!=""){
_1bf+=_1c1;
}
if(desc&&desc!=""){
_1bf!=""?_1bf+=_1c0:"";
_1bf+=desc;
}
var _1c2=dom.byId("content");
var _1c3=_172(".cluster,.list",_1c2)[0];
if(_1c3){
if(typeof _172(".collapse-title",_1c3)[0]=="undefined"||_172(".collapse-title",_1c3)[0].innerHTML==""){
if(typeof _172(".description",_1c3)[0]!="undefined"){
if(_172(".description",_1c3)[0].innerHTML!==""){
var _1c4=_172(".description",_1c3)[0];
if(_1c4&&_1c4.innerText!==""){
_1bf!=""?_1bf+=_1c0:"";
_1bf+=_172(".description",_1c3)[0].innerText;
}
}
}
}
}else{
var _1c5=_172("tr:first-child > td.field.last-cell",_1c2)[0];
if(_1c5&&_1c5.innerText!==""){
_1bf!=""?_1bf+=_1c0:"";
_1bf+=_1c5.innerText;
}
}
if(_1bf){
_1be=_168.create("span",{innerHTML:_1bf});
setTimeout(function(){
_168.place(_1be,_1bd);
},1000);
}
},closeGracefully:function(){
curam.dialog._justClose=true;
},});
_169.register("curam/dialog",this);
return curam.dialog;
});
},"curam/widget/menu/MenuPane":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/on","dojo/dom-class","curam/widget/componentWrappers/ListWraper","curam/widget/form/ToggleButtonGroup","dojo/_base/window","dojo/dom-construct","dijit/TooltipDialog","dijit/popup","dojo/_base/fx","dojo/dom-style","dojox/layout/ExpandoPane","dojo/dom-geometry","dojo/aspect","dojo/keys","dijit/Tooltip","idx/widget/HoverCard","dojo/query","dojo/dom-style","dojo/has","dojo/dom-attr"],function(_1c6,lang,on,_1c7,_1c8,_1c9,_1ca,_1cb,_1cc,_1cd,fx,_1ce,_1cf,_1d0,_1d1,keys,_1d2,_1d3,_1d4,_1d5,has,_1d6){
var _1d7=_1c6("curam.widget.menu._MenuPaneButtonIndexer",null,{selectedButtonKey:-1,selectedButtonDisplayIndex:-1,expandButtonDisplayIndex:-1,_buttonDisplayOrderArrayOrginale:null,_buttonMap:null,_subPagenMap:null,_buttonPrimaryContainerArray:null,_buttonSecondaryContainerArray:null,constructor:function(args){
this._buttonMap=[];
this._subPagenMap=[];
this._buttonDisplayOrderArrayOrginale=new Array();
this._buttonPrimaryContainerArray=new Array();
this._buttonSecondaryContainerArray=new Array();
},addNewButton:function(_1d8,key){
var _1d9={key:key,id:_1d8.id,button:_1d8,contextBox:null,displayOrderIndex:null,displayOrderOrginaleIndex:this._buttonDisplayOrderArrayOrginale.length};
this._buttonMap[key]=_1d9;
this._buttonDisplayOrderArrayOrginale.push(key);
},addButtonReferenceToPrimaryContainer:function(key,_1da){
if(_1da){
this._buttonPrimaryContainerArray.push(key);
}else{
this._buttonSecondaryContainerArray.push(key);
}
},getButton:function(key){
var _1db=this._buttonMap[key];
return _1db;
},setNewSubPage:function(_1dc,_1dd){
this._subPagenMap[_1dc]=_1dd;
},getSubPagePrimaryPage:function(_1de){
var _1df=this._subPagenMap[_1de];
return _1df;
},getButtonPrimary:function(_1e0){
var key=this._buttonPrimaryContainerArray[_1e0];
var _1e1=this.getButton(key);
return _1e1;
},getButtonSecondary:function(_1e2){
var key=this._buttonSecondaryContainerArray[_1e2];
var _1e3=this.getButton(key);
return _1e3;
},swapButtonFomPrimaryContainerToSecondaryContainer:function(_1e4){
if(_1e4){
var item=this._buttonPrimaryContainerArray.pop();
this._buttonSecondaryContainerArray.unshift(item);
}else{
var item=this._buttonSecondaryContainerArray.shift();
this._buttonPrimaryContainerArray.push(item);
}
},swapButtonContainerToContainer:function(_1e5,_1e6,_1e7){
if(_1e5){
var item=this._buttonPrimaryContainerArray.splice(_1e6,1);
this._buttonSecondaryContainerArray.splice(_1e7,0,item[0]);
}else{
var item=this._buttonSecondaryContainerArray.splice(_1e6,1);
this._buttonPrimaryContainerArray.splice(_1e7,0,item[0]);
}
},swapButtonContainerItemIndex:function(_1e8,_1e9,_1ea){
if(_1e8){
var item=this._buttonPrimaryContainerArray.splice(_1e9,1);
this._buttonPrimaryContainerArray.splice(_1ea,0,item[0]);
}else{
var item=this._buttonSecondaryContainerArray.splice(_1e9,1);
this._buttonSecondaryContainerArray.splice(_1ea,0,item[0]);
}
},getWhichContinerFromIndex:function(_1eb){
var _1ec=0;
if(_1eb>=this._buttonPrimaryContainerArray.length){
_1ec=1;
}
return _1ec;
}});
return _1c6("curam.widget.menu.MenuPane",[_1cf],{_listWrapper:null,_expandButton:null,_expandButtonContentBox:null,_toolTipDialogExpand:null,_toolTipDialogExpandContents:null,_fadeIn:null,_fadeOut:null,_menuPaneButtonIndexer:null,duration:300,_buttonSizerDiv:null,_buttonSizerList:null,_resizeResizeHandler:null,_showEndresizeResizeHandler:null,_hideEndResizeHandler:null,resizeDelay:250,_resizeDelayHandler:null,_previouseHeight:-1,_resizeStatusQue:1,_resizeStatusResizeing:0,_resizeStatusNotInUse:-1,_resizeCurentStatus:-1,_classNavMenu:"navMenu",_classNavMenuOverFlow:"navMenuOverFlow",_classCurramSideMenuButton:"curramSideMenuButton",_classCurramSideMenuButtonIcon:"curramSideMenuButtonIcon",_classCurramSideMenuOverFlowButton:"curramSideMenuOverFlowButton",_classCurramSideMenuOverFlowButtonIcon:"curramSideMenuOverFlowButtonIcon",_classCurramSideMenuOverFlowButtonExpand:"curramSideMenuOverFlowButtonExpand",_classCurramSideMenuOverFlowButtonExpandIcon:"curramSideMenuOverFlowButtonExpandIcon",constructor:function(args){
this.inherited(arguments);
this._menuPaneButtonIndexer=new _1d7();
},postCreate:function(){
this.inherited(arguments);
_1c7.add(this.titleWrapper,"dijitHidden");
this._expandButton=new dijit.form.Button({id:"navOverflowButton",baseClass:this._classCurramSideMenuOverFlowButtonExpand,iconClass:this._classCurramSideMenuOverFlowButtonExpandIcon,orgID:"exapnadButton",showLabel:false});
this._toolTipDialogExpandContentsListWrapper=new curam.widget.componentWrappers.ListWraper({listType:"ol",role:"menu",baseClass:this._classNavMenuOverFlow,_doBeforeItemSet:lang.hitch(this,function(item,_1ed){
if(item!=null){
if(this.isLeftToRight()){
_1d5.set(item.focusNode,"textAlign","left");
_1d5.set(item.containerNode,"marginRight","10px");
}else{
_1d5.set(item.focusNode,"textAlign","right");
_1d5.set(item.containerNode,"marginLeft","10px");
}
_1d5.set(item.containerNode,"padding","0px");
item.set("baseClass",this._classCurramSideMenuOverFlowButton);
_1c7.replace(item.domNode,this._classCurramSideMenuOverFlowButton,this._classCurramSideMenuButton);
_1c7.add(item.iconNode,this._classCurramSideMenuOverFlowButtonIcon);
}
})});
var _1ee=null;
if(has("ie")!=null&&has("ie")<9){
_1ee=_1cb.create("div");
}else{
_1ee=_1cb.create("nav");
}
_1d6.set(_1ee,"id","overFlowContainer");
_1d6.set(_1ee,"role","navigation");
this.overFlowContainer=_1ee;
this._toolTipDialogExpandContentsListWrapper.placeAt(_1ee);
this._listWrapper=new curam.widget.componentWrappers.ListWraper({listType:"ol",role:"menu",baseClass:this._classNavMenu,_doBeforeItemSet:lang.hitch(this,function(item,_1ef){
if(item!=null&&item.orgID!="exapnadButton"){
_1c7.remove(item.iconNode,this._classCurramSideMenuOverFlowButtonIcon);
if(has("ie")){
_1c7.remove(item.domNode,"curramSideMenuOverFlowButtonHover");
}
_1d5.set(item.focusNode,"textAlign","center");
if(this.isLeftToRight()){
_1d5.set(item.containerNode,"marginRight","0px");
}else{
_1d5.set(item.containerNode,"marginLeft","0px");
}
item.set("baseClass",this._classCurramSideMenuButton);
_1c7.replace(item.domNode,this._classCurramSideMenuButton,this._classCurramSideMenuOverFlowButton);
}
})});
if(has("ie")!=null&&has("ie")<9){
var div1=_1cb.create("div",null,this.containerNode);
_1d6.set(div1,"role","navigation");
this._listWrapper.placeAt(div1);
}else{
var _1f0=_1cb.create("nav",null,this.containerNode);
_1d6.set(_1f0,"role","navigation");
this._listWrapper.placeAt(_1f0);
}
this._fadeIn=fx.fadeIn({node:this._listWrapper.domNode,duration:this.duration,onEnd:lang.hitch(this,"_showContainer")});
this._fadeOut=fx.fadeOut({node:this._listWrapper.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHideEnd")});
this._resizeResizeHandler=_1d1.after(this,"resize",this._doResize,true);
this._showEndresizeResizeHandler=_1d1.after(this,"_showEnd",lang.hitch(this,"_onShowComplete"),false);
this._hideEndResizeHandler=_1d1.after(this,"_hideEnd",lang.hitch(this,"_onHideComplete"),false);
},startup:function(){
this.inherited(arguments);
},fadeIn:function(){
this._fadeIcons(true);
},fadeOut:function(){
this._fadeIcons(false);
},_fadeIcons:function(_1f1){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
if(_1f1==true){
if(this._fadeOut.status()=="playing"){
this._fadeOut.stop();
this._fadeIn.play();
}else{
if(this._fadeIn.status()!="playing"){
this._fadeIn.play();
}
}
}else{
if(this._fadeIn.status()=="playing"){
this._fadeIn.stop();
this._fadeOut.play();
}else{
if(this._fadeOut.status()!="playing"){
this._fadeOut.play();
}
}
}
},_showContainer:function(){
if(!this._showing){
this.toggle();
}
},_onShowComplete:function(){
},_onHideEnd:function(){
if(this._showing){
this.toggle();
}
},_onHideComplete:function(){
},addMenuItems:function(_1f2){
this._cleanDownExistingMenuItems();
dojo.forEach(_1f2,function(item,i){
this._addMenuItem(item,i);
},this);
this._initaleProcessMenuItems();
this._initalePlaceMenuItems();
},_cleanDownExistingMenuItems:function(){
this._removeButtonCacheContent();
this._toolTipDialogExpandContentsListWrapper.deleteAllChildern();
this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length=0;
this._removeExpandButton();
this._listWrapper.deleteAllChildern();
this._menuPaneButtonIndexer._buttonDisplayOrderArrayOrginale.length=0;
this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length=0;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.expandButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
for(var key in this._menuPaneButtonIndexer._subPagenMap){
delete this._menuPaneButtonIndexer._subPagenMap[key];
}
},setSelectedButton:function(_1f3){
if(_1f3.exceptionButtonFound==null){
_1f3.exceptionButtonFound=true;
}
if(this._menuPaneButtonIndexer.getButton(_1f3.key)==null&&this._menuPaneButtonIndexer.getSubPagePrimaryPage(_1f3.key)==null){
if(_1f3.exceptionButtonFound==false){
this._onSelectAfter(_1f3);
}else{
throw new Error("No button exists with the requested id : "+_1f3.key);
}
}else{
this._buttonSelected(_1f3,true);
}
},deselect:function(){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=-1){
var _1f4=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
_1f4.button.set("checked",false);
}
},_onSelectBefore:function(_1f5){
},_onSelectAfter:function(_1f6){
},_addMenuItem:function(item,_1f7){
item=this._filterItem(item);
this._generateSubPageIndex(item.id,item.subPageIds);
var cb=lang.hitch(this,function(_1f8){
var pram={key:_1f8.orgID,param:[]};
this._buttonSelected(pram,false);
});
var but=new curam.widget.form.ToggleButtonGroup({label:item.label,orgID:item.id,groupName:"menuPaneCuramWidget",onClick:function(e){
cb(this);
},baseClass:this._classCurramSideMenuButton,iconClass:this._classCurramSideMenuButtonIcon});
if(item.iconPath!=null&&lang.trim(item.iconPath).length>0){
_1ce.set(but.iconNode,{backgroundImage:"url("+item.iconPath+")"});
}
if(item.selected!=null&&item.selected==true){
this._menuPaneButtonIndexer.selectedButtonKey=item.id;
}
this._menuPaneButtonIndexer.addNewButton(but,item.id);
},_generateSubPageIndex:function(_1f9,_1fa){
if(_1fa!=null&&_1fa.length>0){
dojo.forEach(_1fa,function(_1fb){
if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(_1fb)==null){
this._menuPaneButtonIndexer.setNewSubPage(_1fb,_1f9);
}else{
throw new Error("There has been a clash, sub page has all ready been registered.  Primary ID : "+_1f9+" Subpage ID : "+_1fb);
}
},this);
}
},_filterItem:function(item){
return item;
},_initaleProcessMenuItems:function(){
var _1fc=dojo.contentBox(this.domNode);
if(this._showing==false){
_1fc.w=this._showSize;
}
this._buttonSizerDiv=_1cb.create("div",{style:{height:_1fc.h+"px",width:_1fc.w+"px"}});
_1c7.add(this._buttonSizerDiv,"dijitOffScreen");
dojo.place(this._buttonSizerDiv,_1ca.body());
this._buttonSizerList=new curam.widget.componentWrappers.ListWraper({listType:"ol",baseClass:this._classNavMenu}).placeAt(this._buttonSizerDiv);
for(var key in this._menuPaneButtonIndexer._buttonMap){
var _1fd=this._menuPaneButtonIndexer.getButton(key);
if(_1fd.button){
this._buttonSizerList.set("item",_1fd.button.domNode);
var _1fe=dojo.contentBox(_1fd.button.domNode);
this._menuPaneButtonIndexer.getButton(key).contextBox=_1fe;
}
}
this._buttonSizerList.set("item",this._expandButton.domNode);
this._expandButtonContentBox=dojo.contentBox(this._expandButton.domNode);
_1c7.add(this._expandButton.domNode,"dijitHidden");
_1cb.place(this._expandButton.domNode,_1ca.body());
this._toolTipDialogExpand=new idx.widget.HoverCard({draggable:false,hideDelay:450,showDelay:0,target:this._expandButton.id,content:this.overFlowContainer,forceFocus:true,focus:lang.hitch(this,function(){
var _1ff=this._menuPaneButtonIndexer.getButtonSecondary(0);
_1ff.button.focus();
}),defaultPosition:["after-centered","before-centered"],moreActions:[],actions:[]});
_1d6.remove(this._toolTipDialogExpand.focusNode.parentNode,"role");
_1c7.add(this._toolTipDialogExpand.domNode,"dijitHidden");
_1c7.add(_1d4(".idxOneuiHoverCardFooter",this._toolTipDialogExpand.bodyNode)[0],"dijitHidden");
_1c7.add(this._toolTipDialogExpand.gripNode,"dijitHidden");
_1c7.add(this._toolTipDialogExpand.actionIcons,"dijitHidden");
_1c7.add(this._toolTipDialogExpand.moreActionsNode,"dijitHidden");
},_initalePlaceMenuItems:function(){
var _200=0;
for(var key in this._menuPaneButtonIndexer._buttonMap){
var item=this._menuPaneButtonIndexer.getButton(key);
if(item.button.get("checked")){
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=_200;
this._menuPaneButtonIndexer.selectedButtonKey=key;
}
item.displayOrderOrginaleIndex=_200;
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>(this._expandButtonContentBox.h+item.contextBox.h)){
this._listWrapper.set("item",item.button);
this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key,true);
}else{
this._addExpandButton(_200);
this._toolTipDialogExpandContentsListWrapper.set("item",item.button);
this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key,false);
if(_200==this._menuPaneButtonIndexer.selectedButtonDisplayIndex){
selectedIndexPositionTemp=this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length-1;
}
}
if(_200==0){
idcar=item.button.id;
}
_200++;
}
this._buttonSizerList.destroy();
_1cb.destroy(this._buttonSizerDiv);
if(this._menuPaneButtonIndexer.selectedButtonKey!=-1){
var _201=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
_201.button._onClick();
}
},_addExpandButton:function(_202){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1){
console.info("add expando");
this._menuPaneButtonIndexer.expandButtonDisplayIndex=_202;
_1c7.remove(this._expandButton.domNode,"dijitHidden");
this._listWrapper.set("item",this._expandButton);
}
},_removeExpandButton:function(){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length==0){
this._menuPaneButtonIndexer.expandButtonDisplayIndex=-1;
console.info("Remove expando : "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
_1c7.add(this._expandButton.domNode,"dijitHidden");
_1cb.place(this._expandButton.domNode,_1ca.body());
this._listWrapper.deleteChild(this._listWrapper.get("ItemCount"));
}
},_doResize:function(args){
if(args!=null&&args.h!=null&&args.h>10){
if(this._previouseHeight!=args.h&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
if(this.resizeDelay>0){
if(this._resizeDelayHandler!=null){
this._resizeDelayHandler.remove();
}
this._previouseHeight=args.h;
if(this._toolTipDialogExpand){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
}
var cb=lang.hitch(this,function(){
this._callRepositionButtons();
});
this._resizeDelayHandler=this.defer(cb,this.resizeDelay);
}else{
this._callRepositionButtons();
}
}
}
},_callRepositionButtons:function(){
if(this._resizeCurentStatus==this._resizeStatusNotInUse){
this._positionButtonDuringResize();
}else{
this._resizeCurentStatus==this._resizeStatusQue;
}
},_positionButtonDuringResize:function(){
this._resizeCurentStatus=this._resizeStatusResizeing;
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0&&this.get("ContainerHeight")<this._listWrapper.get("ContainerHeight")){
this._addExpandButton(this._listWrapper.get("ItemCount"));
var _203=1;
while((this.get("ContainerHeight")<this._listWrapper.get("ContainerHeight"))&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
if(_203==2&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length==1){
_203=1;
}
var _204=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-_203;
var _205=this._menuPaneButtonIndexer.getButtonPrimary(_204);
if(_205.button.get("checked")&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>1){
_203=2;
console.info(_204+" : I am checked *************************  = "+_205.button.get("checked"));
}else{
console.info("selected = "+_205.button.get("checked"));
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,_204,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_205.button,"first");
this._listWrapper.deleteChild(_204);
this._menuPaneButtonIndexer.expandButtonDisplayIndex--;
if(_203==2){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=0){
this._menuPaneButtonIndexer.selectedButtonDisplayIndex--;
}else{
}
}
}
}
console.info("Move from main to popup-----------------");
}else{
if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0&&this.get("ContainerHeight")>this._listWrapper.get("ContainerHeight")){
console.info(" secondary container size = "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
console.info("Move from popup to main****************");
var _206=true;
while(_206&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0){
var _207=0;
var item=this._menuPaneButtonIndexer.getButtonSecondary(_207);
if(item!=null&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>item.contextBox.h){
var _208=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length;
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1){
this._menuPaneButtonIndexer.expandButtonDisplayIndex++;
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _209=this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
if(_209.button.get("checked")&&_209.displayOrderOrginaleIndex>=_208){
if(_208!=0){
_208--;
}
this._menuPaneButtonIndexer.selectedButtonDisplayIndex++;
}
}
}
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,0,_208);
this._listWrapper.set("item",item.button,_208);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_207);
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length<=0){
this._removeExpandButton();
}
}else{
_206=false;
}
}
}else{
}
}
if(this._resizeCurentStatus<=this._resizeStatusResizeing){
this._resizeCurentStatus=this._resizeStatusNotInUse;
}else{
this._positionButtonDuringResize.apply(this);
}
},_buttonSelected:function(_20a,_20b){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
var _20c;
if(this._menuPaneButtonIndexer.getButton(_20a.key)!=null){
_20c=this._menuPaneButtonIndexer.getButton(_20a.key);
}else{
if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(_20a.key)!=null){
var _20d=this._menuPaneButtonIndexer.getSubPagePrimaryPage(_20a.key);
_20c=this._menuPaneButtonIndexer.getButton(_20d);
}else{
throw new Error("state unknow for requested selected button : "+_20a.key);
}
}
_20c.button.set("checked",true);
this._onSelectBefore(_20a);
this._positionSelectedButton(_20c);
if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0){
this._previouseHeight++;
this._callRepositionButtons();
}
this._onSelectAfter(_20a);
},_positionSelectedButton:function(_20e){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=-1){
var _20f=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
var _210=_20f.displayOrderOrginaleIndex;
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=_210){
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _211=_210-(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
var _212=this._menuPaneButtonIndexer.getButtonSecondary(0);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,this._menuPaneButtonIndexer.selectedButtonDisplayIndex,_211);
this._toolTipDialogExpandContentsListWrapper.set("item",_20f.button,_211);
this._listWrapper.deleteChild(this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,0,this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._listWrapper.set("item",_212.button,this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
}else{
var _211=_20f.displayOrderOrginaleIndex;
this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false,0,_211);
this._toolTipDialogExpandContentsListWrapper.set("item",_20f.button,_211+1);
this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
}
}else{
console.info("no need to repostion old selected button");
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
}
}
var _210=_20e.displayOrderOrginaleIndex;
if(this._menuPaneButtonIndexer.getWhichContinerFromIndex(_210)==1){
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _213=_210-(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
var _214=this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_214.button,0);
this._listWrapper.deleteChild(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,_213+1,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
this._listWrapper.set("item",_20e.button,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_213+1);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1;
this._menuPaneButtonIndexer.selectedButtonKey=_20e.key;
}else{
this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false,_210,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_20e.button,0);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_210+1);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=0;
this._menuPaneButtonIndexer.selectedButtonKey=_20e.key;
}
}else{
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=_210;
this._menuPaneButtonIndexer.selectedButtonKey=_20e.key;
console.info("no need to repostion New selected button :"+_210+" key = "+_20e.key);
}
},_placeMenuItems:function(item,_215){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>(this._expandButtonContentBox.h+item.contextBox.h)){
this._listWrapper.set("item",item.button);
}else{
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1){
this._menuPaneButtonIndexer.expandButtonDisplayIndex=_215;
_1c7.remove(this._expandButton.domNode,"dijitHidden");
this._listWrapper.set("item",this._expandButton);
}
this._toolTipDialogExpandContentsListWrapper.set("item",item.button);
}
},_getContainerHeightAttr:function(){
var _216=_1d0.getContentBox(this.containerNode);
return _216.h;
},_setWidthAttr:function(_217){
if(this._showing){
}else{
this._showAnim.properties.width=_217;
this._showSize=_217;
this._currentSize.w=_217;
}
},_removeButtonCacheContent:function(){
for(var key in this._menuPaneButtonIndexer._buttonMap){
var _218=this._menuPaneButtonIndexer.getButton(key);
if(_218.button){
_218.button.destroy();
}
delete _218.button;
delete _218.contextBox;
delete _218.displayOrderIndex;
delete _218.displayOrderOrginaleIndex;
delete _218.id;
delete _218.key;
delete _218;
delete this._menuPaneButtonIndexer._buttonMap[key];
}
},destroy:function(){
try{
this._resizeCurentStatus=this._resizeStatusNotInUse;
this._resizeDelayHandler!=null?this._resizeDelayHandler.remove():null;
this._resizeResizeHandler.remove();
this._showEndresizeResizeHandler.remove();
this._hideEndResizeHandler.remove();
this._expandButton.destroy();
this._removeButtonCacheContent();
this._toolTipDialogExpandContentsListWrapper.destroy();
this._toolTipDialogExpand.destroy();
this._listWrapper.destroy();
delete this._menuPaneButtonIndexer;
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
},"idx/resources":function(){
define(["dojo/_base/lang","idx/main","dojo/i18n","./string","./util","dojo/i18n!./nls/base"],function(_219,_21a,_21b,_21c,_21d){
var _21e=_219.getObject("resources",true,_21a);
_21e._legacyScopeMap={"":"idx/",".":"idx/","app":"idx/app/","app._Launcher":"idx/app/_Launcher","app.A11yPrologue":"idx/app/A11yPrologue","app.WorkspaceType":"idx/app/WorkspaceType","app.WorkspaceTab":"idx/app/WorkspaceTab","dialogs":"idx/dialogs","form":"idx/form/","form.buttons":"idx/form/buttons","grid":"idx/grid/","grid.prop":"idx/grid/","grid.prop.PropertyGrid":"idx/grid/PropertyGrid","grid.prop.PropertyFormatter":"idx/grid/PropertyGrid","layout":"idx/layout/","layout.BorderContainer":"idx/layout/BorderContainer","widget":"idx/widget/","widget.ModalDialog":"idx/widget/ModalDialog","widget.TypeAhead":"idx/widget/TypeAhead","widget.HoverHelp":"idx/widget/HoverHelp","widget.EditController":"idx/widget/EditController"};
_21e._defaultResources={dateFormatOptions:{formatLength:"medium",fullYear:true,selector:"date"},timeFormatOptions:{formatLength:"medium",selector:"time"},dateTimeFormatOptions:{formatLength:"medium",fullYear:true},decimalFormatOptions:{type:"decimal"},integerFormatOptions:{type:"decimal",fractional:false,round:0},percentFormatOptions:{type:"percent",fractional:true,places:2},currencyFormatOptions:{type:"currency"},labelFieldSeparator:":"};
_21e._localResources=[];
_21e._currentResources=[];
_21e._scopeResources=[];
_21e._normalizeScope=function(_21f){
if((!_21f)||(_21f.length==0)){
return "idx/";
}
if(_21e._legacyScopeMap[_21f]){
return _21e._legacyScopeMap[_21f];
}
return _21f;
};
_21e._getBundle=function(_220,_221,_222){
_222=_21b.normalizeLocale(_222);
var _223=_220+"."+_221;
var _224=_21e._currentResources[_222];
if(!_224){
_224=[];
_21e._currentResources[_222]=_224;
}
var _225=_21e._localResources[_222];
if(!_225){
_225=[];
_21e._localResources[_222]=_225;
}
var _226=_224[_223];
if(!_226){
var _227=_225[_223];
if(!_227){
_227=_21b.getLocalization(_220,_221,_222);
if(!_227){
_227=new Object();
}
_225[_223]=_227;
}
_226=new Object();
_219.mixin(_226,_227);
_224[_223]=_226;
}
return _226;
};
_21e.clearLocalOverrides=function(_228){
_228=_21b.normalizeLocale(_228);
_21e._currentResources[_228]=null;
_21e._scopeResources[_228]=null;
};
_21e.clearOverrides=function(){
_21e._currentResources=[];
_21e._scopeResources=[];
};
_21e.install=function(_229,_22a,_22b){
_22b=_21b.normalizeLocale(_22b);
_22a=_21e._normalizeScope(_22a);
var _22c=_22a.lastIndexOf("/");
var _22d="";
var _22e="";
if(_22c==_22a.length-1){
_22e="base";
_22d=_22a.substr(0,_22a.length-1);
}else{
if(_22c>=0){
_22e=_22a.substr(_22c+1);
_22d=_22a.substr(0,_22c);
}
}
var _22f=_21e._getBundle(_22d,_22e,_22b);
_219.mixin(_22f,_229);
_21e._clearResourcesCache(_22b,_22a);
};
_21e.getResources=function(_230,_231){
_231=_21b.normalizeLocale(_231);
_230=_21e._normalizeScope(_230);
var _232=_21e._scopeResources[_231];
if(!_232){
_232=[];
_21e._scopeResources[_231]=_232;
}
var _233=_232[_230];
if(_233){
return _233;
}
_233=new Object();
var _234=_230.split("/");
var _235=0;
var pkg="";
var _236="";
for(_235=0;_235<_234.length;_235++){
var _237="base";
if(_235<_234.length-1){
pkg=pkg+_236+_234[_235];
_236=".";
}else{
_237=_234[_235];
}
if(_237.length==0){
continue;
}
var _238=_21e._getBundle(pkg,_237,_231);
if(!_238){
continue;
}
for(var _239 in _238){
_233[_239]=_238[_239];
}
}
_232[_230]=_233;
return _233;
};
_21e.getDependencies=function(_23a){
_23a=_21e._normalizeScope(_23a);
var _23b=[];
var _23c=_23a.split("/");
var _23d=0;
var pkg="";
var _23e="";
var _23f="";
var _240="";
for(_23d=0;_23d<_23c.length;_23d++){
_23f="base";
if(_23d<_23c.length-1){
pkg=pkg+_23e+_23c[_23d];
_23e="/";
}else{
_23f=_23c[_23d];
}
if(_23f.length==0){
continue;
}
_240="dojo/i18n!"+pkg+_23e+"nls/"+_23f;
_23b.push(_240);
}
return _23b;
};
_21e.getStrings=function(_241,_242){
return _21e.getResources(_241,_242);
};
_21e._clearResourcesCache=function(_243,_244){
_243=_21b.normalizeLocale(_243);
if(_21e._scopeResources[_243]){
if(!_244){
_21e._scopeResources[_243]=null;
}else{
var _245=_21e._scopeResources[_243];
for(field in _245){
if(_21c.startsWith(field,_244)){
_245[field]=null;
}
}
}
}
};
_21e.get=function(name,_246,_247){
_247=_21b.normalizeLocale(_247);
_246=_21e._normalizeScope(_246);
var _248=_246.split("/");
var _249=0;
for(_249=0;_249<_248.length;_249++){
var _24a=(_249==0)?_248[_248.length-1]:"base";
var _24b="";
var _24c="";
var _24d=(_249==0)?(_248.length-_249-1):(_248.length-_249);
for(var idx2=0;idx2<_24d;idx2++){
_24b=_24b+_24c+_248[idx2];
_24c=".";
}
var _24e=_21e._getBundle(_24b,_24a,_247);
if(!_24e){
continue;
}
if(name in _24e){
return _24e[name];
}
}
if(name in _21e._defaultResources){
return _21e._defaultResources[name];
}
return null;
};
_21e.getLabelFieldSeparator=function(_24f,_250){
return _21e.get("labelFieldSeparator",_24f,_250);
};
_21e.getDateFormatOptions=function(_251,_252){
return _21e.get("dateFormatOptions",_251,_252);
};
_21e.getTimeFormatOptions=function(_253,_254){
return _21e.get("timeFormatOptions",_253,_254);
};
_21e.getDateTimeFormatOptions=function(_255,_256){
return _21e.get("dateTimeFormatOptions",_255,_256);
};
_21e.getDecimalFormatOptions=function(_257,_258){
return _21e.get("decimalFormatOptions",_257,_258);
};
_21e.getIntegerFormatOptions=function(_259,_25a){
return _21e.get("integerFormatOptions",_259,_25a);
};
_21e.getPercentFormatOptions=function(_25b,_25c){
return _21e.get("percentFormatOptions",_25b,_25c);
};
_21e.getCurrencyFormatOptions=function(_25d,_25e){
return _21e.get("currencyFormatOptions",_25d,_25e);
};
return _21e;
});
},"dijit/focus":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/domReady","dojo/sniff","dojo/Stateful","dojo/_base/window","dojo/window","./a11y","./registry","./main"],function(_25f,_260,dom,_261,_262,_263,_264,lang,on,_265,has,_266,win,_267,a11y,_268,_269){
var _26a;
var _26b;
var _26c=_260([_266,_264],{curNode:null,activeStack:[],constructor:function(){
var _26d=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_25f.before(_263,"empty",_26d);
_25f.before(_263,"destroy",_26d);
},registerIframe:function(_26e){
return this.registerWin(_26e.contentWindow,_26e);
},registerWin:function(_26f,_270){
var _271=this,body=_26f.document&&_26f.document.body;
if(body){
var _272=has("pointer-events")?"pointerdown":has("MSPointer")?"MSPointerDown":has("touch-events")?"mousedown, touchstart":"mousedown";
var mdh=on(_26f.document,_272,function(evt){
if(evt&&evt.target&&evt.target.parentNode==null){
return;
}
_271._onTouchNode(_270||evt.target,"mouse");
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
_271._onFocusNode(_270||evt.target);
}else{
_271._onTouchNode(_270||evt.target);
}
});
var foh=on(body,"focusout",function(evt){
_271._onBlurNode(_270||evt.target);
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
if(now<_26a+100){
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
if(now<_26b+100){
return;
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
}),0);
},_onTouchNode:function(node,by){
_26b=(new Date()).getTime();
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_262.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _273=[];
try{
while(node){
var _274=_261.get(node,"dijitPopupParent");
if(_274){
node=_268.byId(_274).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_267.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_275=id&&_268.byId(id);
if(_275&&!(by=="mouse"&&_275.get("disabled"))){
_273.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_273,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
_26a=(new Date()).getTime();
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
},_setStack:function(_276,by){
var _277=this.activeStack,_278=_277.length-1,_279=_276.length-1;
if(_276[_279]==_277[_278]){
return;
}
this.set("activeStack",_276);
var _27a,i;
for(i=_278;i>=0&&_277[i]!=_276[i];i--){
_27a=_268.byId(_277[i]);
if(_27a){
_27a._hasBeenBlurred=true;
_27a.set("focused",false);
if(_27a._focusManager==this){
_27a._onBlur(by);
}
this.emit("widget-blur",_27a,by);
}
}
for(i++;i<=_279;i++){
_27a=_268.byId(_276[i]);
if(_27a){
_27a.set("focused",true);
if(_27a._focusManager==this){
_27a._onFocus(by);
}
this.emit("widget-focus",_27a,by);
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
var _27b=new _26c();
_265(function(){
var _27c=_27b.registerWin(_267.get(document));
if(has("ie")){
on(window,"unload",function(){
if(_27c){
_27c.remove();
_27c=null;
}
});
}
});
_269.focus=function(node){
_27b.focus(node);
};
for(var attr in _27b){
if(!/^_/.test(attr)){
_269.focus[attr]=typeof _27b[attr]=="function"?lang.hitch(_27b,attr):_27b[attr];
}
}
_27b.watch(function(attr,_27d,_27e){
_269.focus[attr]=_27e;
});
return _27b;
});
},"dojo/i18n":function(){
define(["./_base/kernel","require","./has","./_base/array","./_base/config","./_base/lang","./_base/xhr","./json","module"],function(dojo,_27f,has,_280,_281,lang,xhr,json,_282){
has.add("dojo-preload-i18n-Api",1);
1||has.add("dojo-v1x-i18n-Api",1);
var _283=dojo.i18n={},_284=/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,_285=function(root,_286,_287,_288){
for(var _289=[_287+_288],_28a=_286.split("-"),_28b="",i=0;i<_28a.length;i++){
_28b+=(_28b?"-":"")+_28a[i];
if(!root||root[_28b]){
_289.push(_287+_28b+"/"+_288);
_289.specificity=_28b;
}
}
return _289;
},_28c={},_28d=function(_28e,_28f,_290){
_290=_290?_290.toLowerCase():dojo.locale;
_28e=_28e.replace(/\./g,"/");
_28f=_28f.replace(/\./g,"/");
return (/root/i.test(_290))?(_28e+"/nls/"+_28f):(_28e+"/nls/"+_290+"/"+_28f);
},_291=dojo.getL10nName=function(_292,_293,_294){
return _292=_282.id+"!"+_28d(_292,_293,_294);
},_295=function(_296,_297,_298,_299,_29a,load){
_296([_297],function(root){
var _29b=lang.clone(root.root||root.ROOT),_29c=_285(!root._v1x&&root,_29a,_298,_299);
_296(_29c,function(){
for(var i=1;i<_29c.length;i++){
_29b=lang.mixin(lang.clone(_29b),arguments[i]);
}
var _29d=_297+"/"+_29a;
_28c[_29d]=_29b;
_29b.$locale=_29c.specificity;
load();
});
});
},_29e=function(id,_29f){
return /^\./.test(id)?_29f(id):id;
},_2a0=function(_2a1){
var list=_281.extraLocale||[];
list=lang.isArray(list)?list:[list];
list.push(_2a1);
return list;
},load=function(id,_2a2,load){
if(has("dojo-preload-i18n-Api")){
var _2a3=id.split("*"),_2a4=_2a3[1]=="preload";
if(_2a4){
if(!_28c[id]){
_28c[id]=1;
_2a5(_2a3[2],json.parse(_2a3[3]),1,_2a2);
}
load(1);
}
if(_2a4||_2a6(id,_2a2,load)){
return;
}
}
var _2a7=_284.exec(id),_2a8=_2a7[1]+"/",_2a9=_2a7[5]||_2a7[4],_2aa=_2a8+_2a9,_2ab=(_2a7[5]&&_2a7[4]),_2ac=_2ab||dojo.locale||"",_2ad=_2aa+"/"+_2ac,_2ae=_2ab?[_2ac]:_2a0(_2ac),_2af=_2ae.length,_2b0=function(){
if(!--_2af){
load(lang.delegate(_28c[_2ad]));
}
};
_280.forEach(_2ae,function(_2b1){
var _2b2=_2aa+"/"+_2b1;
if(has("dojo-preload-i18n-Api")){
_2b3(_2b2);
}
if(!_28c[_2b2]){
_295(_2a2,_2aa,_2a8,_2a9,_2b1,_2b0);
}else{
_2b0();
}
});
};
if(has("dojo-unit-tests")){
var _2b4=_283.unitTests=[];
}
if(has("dojo-preload-i18n-Api")||1){
var _2b5=_283.normalizeLocale=function(_2b6){
var _2b7=_2b6?_2b6.toLowerCase():dojo.locale;
return _2b7=="root"?"ROOT":_2b7;
},isXd=function(mid,_2b8){
return (1&&1)?_2b8.isXdUrl(_27f.toUrl(mid+".js")):true;
},_2b9=0,_2ba=[],_2a5=_283._preloadLocalizations=function(_2bb,_2bc,_2bd,_2be){
_2be=_2be||_27f;
function _2bf(mid,_2c0){
if(isXd(mid,_2be)||_2bd){
_2be([mid],_2c0);
}else{
_2da([mid],_2c0,_2be);
}
};
function _2c1(_2c2,func){
var _2c3=_2c2.split("-");
while(_2c3.length){
if(func(_2c3.join("-"))){
return;
}
_2c3.pop();
}
func("ROOT");
};
function _2c4(){
_2b9++;
};
function _2c5(){
--_2b9;
while(!_2b9&&_2ba.length){
load.apply(null,_2ba.shift());
}
};
function _2c6(path,name,loc,_2c7){
return _2c7.toAbsMid(path+name+"/"+loc);
};
function _2c8(_2c9){
_2c9=_2b5(_2c9);
_2c1(_2c9,function(loc){
if(_280.indexOf(_2bc,loc)>=0){
var mid=_2bb.replace(/\./g,"/")+"_"+loc;
_2c4();
_2bf(mid,function(_2ca){
for(var p in _2ca){
var _2cb=_2ca[p],_2cc=p.match(/(.+)\/([^\/]+)$/),_2cd,_2ce;
if(!_2cc){
continue;
}
_2cd=_2cc[2];
_2ce=_2cc[1]+"/";
if(!_2cb._localized){
continue;
}
var _2cf;
if(loc==="ROOT"){
var root=_2cf=_2cb._localized;
delete _2cb._localized;
root.root=_2cb;
_28c[_27f.toAbsMid(p)]=root;
}else{
_2cf=_2cb._localized;
_28c[_2c6(_2ce,_2cd,loc,_27f)]=_2cb;
}
if(loc!==_2c9){
function _2d0(_2d1,_2d2,_2d3,_2d4){
var _2d5=[],_2d6=[];
_2c1(_2c9,function(loc){
if(_2d4[loc]){
_2d5.push(_27f.toAbsMid(_2d1+loc+"/"+_2d2));
_2d6.push(_2c6(_2d1,_2d2,loc,_27f));
}
});
if(_2d5.length){
_2c4();
_2be(_2d5,function(){
for(var i=_2d5.length-1;i>=0;i--){
_2d3=lang.mixin(lang.clone(_2d3),arguments[i]);
_28c[_2d6[i]]=_2d3;
}
_28c[_2c6(_2d1,_2d2,_2c9,_27f)]=lang.clone(_2d3);
_2c5();
});
}else{
_28c[_2c6(_2d1,_2d2,_2c9,_27f)]=_2d3;
}
};
_2d0(_2ce,_2cd,_2cb,_2cf);
}
}
_2c5();
});
return true;
}
return false;
});
};
_2c8();
_280.forEach(dojo.config.extraLocale,_2c8);
},_2a6=function(id,_2d7,load){
if(_2b9){
_2ba.push([id,_2d7,load]);
}
return _2b9;
},_2b3=function(){
};
}
if(1){
var _2d8={},_2d9=new Function("__bundle","__checkForLegacyModules","__mid","__amdValue","var define = function(mid, factory){define.called = 1; __amdValue.result = factory || mid;},"+"\t   require = function(){define.called = 1;};"+"try{"+"define.called = 0;"+"eval(__bundle);"+"if(define.called==1)"+"return __amdValue;"+"if((__checkForLegacyModules = __checkForLegacyModules(__mid)))"+"return __checkForLegacyModules;"+"}catch(e){}"+"try{"+"return eval('('+__bundle+')');"+"}catch(e){"+"return e;"+"}"),_2da=function(deps,_2db,_2dc){
var _2dd=[];
_280.forEach(deps,function(mid){
var url=_2dc.toUrl(mid+".js");
function load(text){
var _2de=_2d9(text,_2b3,mid,_2d8);
if(_2de===_2d8){
_2dd.push(_28c[url]=_2d8.result);
}else{
if(_2de instanceof Error){
console.error("failed to evaluate i18n bundle; url="+url,_2de);
_2de={};
}
_2dd.push(_28c[url]=(/nls\/[^\/]+\/[^\/]+$/.test(url)?_2de:{root:_2de,_v1x:1}));
}
};
if(_28c[url]){
_2dd.push(_28c[url]);
}else{
var _2df=_2dc.syncLoadNls(mid);
if(!_2df){
_2df=_2b3(mid.replace(/nls\/([^\/]*)\/([^\/]*)$/,"nls/$2/$1"));
}
if(_2df){
_2dd.push(_2df);
}else{
if(!xhr){
try{
_2dc.getText(url,true,load);
}
catch(e){
_2dd.push(_28c[url]={});
}
}else{
xhr.get({url:url,sync:true,load:load,error:function(){
_2dd.push(_28c[url]={});
}});
}
}
}
});
_2db&&_2db.apply(null,_2dd);
};
_2b3=function(_2e0){
for(var _2e1,_2e2=_2e0.split("/"),_2e3=dojo.global[_2e2[0]],i=1;_2e3&&i<_2e2.length-1;_2e3=_2e3[_2e2[i++]]){
}
if(_2e3){
_2e1=_2e3[_2e2[i]];
if(!_2e1){
_2e1=_2e3[_2e2[i].replace(/-/g,"_")];
}
if(_2e1){
_28c[_2e0]=_2e1;
}
}
return _2e1;
};
_283.getLocalization=function(_2e4,_2e5,_2e6){
var _2e7,_2e8=_28d(_2e4,_2e5,_2e6);
load(_2e8,(!isXd(_2e8,_27f)?function(deps,_2e9){
_2da(deps,_2e9,_27f);
}:_27f),function(_2ea){
_2e7=_2ea;
});
return _2e7;
};
if(has("dojo-unit-tests")){
_2b4.push(function(doh){
doh.register("tests.i18n.unit",function(t){
var _2eb;
_2eb=_2d9("{prop:1}",_2b3,"nonsense",_2d8);
t.is({prop:1},_2eb);
t.is(undefined,_2eb[1]);
_2eb=_2d9("({prop:1})",_2b3,"nonsense",_2d8);
t.is({prop:1},_2eb);
t.is(undefined,_2eb[1]);
_2eb=_2d9("{'prop-x':1}",_2b3,"nonsense",_2d8);
t.is({"prop-x":1},_2eb);
t.is(undefined,_2eb[1]);
_2eb=_2d9("({'prop-x':1})",_2b3,"nonsense",_2d8);
t.is({"prop-x":1},_2eb);
t.is(undefined,_2eb[1]);
_2eb=_2d9("define({'prop-x':1})",_2b3,"nonsense",_2d8);
t.is(_2d8,_2eb);
t.is({"prop-x":1},_2d8.result);
_2eb=_2d9("define('some/module', {'prop-x':1})",_2b3,"nonsense",_2d8);
t.is(_2d8,_2eb);
t.is({"prop-x":1},_2d8.result);
_2eb=_2d9("this is total nonsense and should throw an error",_2b3,"nonsense",_2d8);
t.is(_2eb instanceof Error,true);
});
});
}
}
return lang.mixin(_283,{dynamic:true,normalize:_29e,load:load,cache:_28c,getL10nName:_291});
});
},"curam/widget/menu/BannerMenuItem":function(){
define(["dojo","dijit/dijit","dojo/_base/declare","dijit/MenuItem"],function(dojo,_2ec,_2ed,_2ee){
return _2ed("curam.widget.menu.BannerMenuItem",[_2ee],{iconSrc:"unknown",_setIconSrcAttr:{node:"iconNode",type:"attribute",attribute:"src"},iconStyle:"unknown",_setIconStyleAttr:{node:"iconNode",type:"attribute",attribute:"style"}});
});
},"dijit/hccss":function(){
define(["dojo/dom-class","dojo/hccss","dojo/domReady","dojo/_base/window"],function(_2ef,has,_2f0,win){
_2f0(function(){
if(has("highcontrast")){
_2ef.add(win.body(),"dijit_a11y");
}
});
return has;
});
},"curam/util/LocalConfig":function(){
define([],function(){
var _2f1=function(name){
return "curam_util_LocalConfig_"+name;
},_2f2=function(name,_2f3){
var _2f4=_2f1(name);
if(typeof top[_2f4]==="undefined"){
top[_2f4]=_2f3;
}
return top[_2f4];
},_2f5=function(name){
return top&&top!=null?top[_2f1(name)]:undefined;
};
_2f2("seedValues",{}),_2f2("overrides",{});
var _2f6=function(_2f7,_2f8){
if(typeof _2f7!=="undefined"&&typeof _2f7!=="string"){
throw new Error("Invalid "+_2f8+" type: "+typeof _2f7+"; expected string");
}
};
var _2f9={seedOption:function(name,_2fa,_2fb){
_2f6(_2fa,"value");
_2f6(_2fb,"defaultValue");
_2f5("seedValues")[name]=(typeof _2fa!=="undefined")?_2fa:_2fb;
},overrideOption:function(name,_2fc){
_2f6(_2fc,"value");
if(typeof (Storage)!=="undefined"){
localStorage[name]=_2fc;
}else{
_2f5("overrides")[name]=_2fc;
}
},readOption:function(name,_2fd){
var _2fe=_2f5("seedValues");
var _2ff=_2f5("overrides");
_2f6(_2fd,"defaultValue");
var _300=null;
if(typeof (Storage)!=="undefined"&&localStorage&&typeof localStorage[name]!=="undefined"){
_300=localStorage[name];
}else{
if(_2ff&&typeof _2ff[name]!=="undefined"){
_300=_2ff[name];
}else{
if(_2fe&&typeof _2fe[name]!=="undefined"){
_300=_2fe[name];
}else{
_300=_2fd;
}
}
}
return _300;
},clearOption:function(name){
if(typeof (Storage)!=="undefined"){
localStorage.removeItem(name);
}
delete _2f5("overrides")[name];
delete _2f5("seedValues")[name];
}};
return _2f9;
});
},"dijit/PopupMenuBarItem":function(){
define(["dojo/_base/declare","./PopupMenuItem","./MenuBarItem"],function(_301,_302,_303){
var _304=_303._MenuBarItemMixin;
return _301("dijit.PopupMenuBarItem",[_302,_304],{});
});
},"dojo/parser":function(){
define(["require","./_base/kernel","./_base/lang","./_base/array","./_base/config","./dom","./_base/window","./_base/url","./aspect","./promise/all","./date/stamp","./Deferred","./has","./query","./on","./ready"],function(_305,dojo,_306,_307,_308,dom,_309,_30a,_30b,all,_30c,_30d,has,_30e,don,_30f){
new Date("X");
function _310(text){
return eval("("+text+")");
};
var _311=0;
_30b.after(_306,"extend",function(){
_311++;
},true);
function _312(ctor){
var map=ctor._nameCaseMap,_313=ctor.prototype;
if(!map||map._extendCnt<_311){
map=ctor._nameCaseMap={};
for(var name in _313){
if(name.charAt(0)==="_"){
continue;
}
map[name.toLowerCase()]=name;
}
map._extendCnt=_311;
}
return map;
};
var _314={};
function _315(_316,_317){
var ts=_316.join();
if(!_314[ts]){
var _318=[];
for(var i=0,l=_316.length;i<l;i++){
var t=_316[i];
_318[_318.length]=(_314[t]=_314[t]||(_306.getObject(t)||(~t.indexOf("/")&&(_317?_317(t):_305(t)))));
}
var ctor=_318.shift();
_314[ts]=_318.length?(ctor.createSubclass?ctor.createSubclass(_318):ctor.extend.apply(ctor,_318)):ctor;
}
return _314[ts];
};
var _319={_clearCache:function(){
_311++;
_314={};
},_functionFromScript:function(_31a,_31b){
var _31c="",_31d="",_31e=(_31a.getAttribute(_31b+"args")||_31a.getAttribute("args")),_31f=_31a.getAttribute("with");
var _320=(_31e||"").split(/\s*,\s*/);
if(_31f&&_31f.length){
_307.forEach(_31f.split(/\s*,\s*/),function(part){
_31c+="with("+part+"){";
_31d+="}";
});
}
return new Function(_320,_31c+_31a.innerHTML+_31d);
},instantiate:function(_321,_322,_323){
_322=_322||{};
_323=_323||{};
var _324=(_323.scope||dojo._scopeName)+"Type",_325="data-"+(_323.scope||dojo._scopeName)+"-",_326=_325+"type",_327=_325+"mixins";
var list=[];
_307.forEach(_321,function(node){
var type=_324 in _322?_322[_324]:node.getAttribute(_326)||node.getAttribute(_324);
if(type){
var _328=node.getAttribute(_327),_329=_328?[type].concat(_328.split(/\s*,\s*/)):[type];
list.push({node:node,types:_329});
}
});
return this._instantiate(list,_322,_323);
},_instantiate:function(_32a,_32b,_32c,_32d){
var _32e=_307.map(_32a,function(obj){
var ctor=obj.ctor||_315(obj.types,_32c.contextRequire);
if(!ctor){
throw new Error("Unable to resolve constructor for: '"+obj.types.join()+"'");
}
return this.construct(ctor,obj.node,_32b,_32c,obj.scripts,obj.inherited);
},this);
function _32f(_330){
if(!_32b._started&&!_32c.noStart){
_307.forEach(_330,function(_331){
if(typeof _331.startup==="function"&&!_331._started){
_331.startup();
}
});
}
return _330;
};
if(_32d){
return all(_32e).then(_32f);
}else{
return _32f(_32e);
}
},construct:function(ctor,node,_332,_333,_334,_335){
var _336=ctor&&ctor.prototype;
_333=_333||{};
var _337={};
if(_333.defaults){
_306.mixin(_337,_333.defaults);
}
if(_335){
_306.mixin(_337,_335);
}
var _338;
if(has("dom-attributes-explicit")){
_338=node.attributes;
}else{
if(has("dom-attributes-specified-flag")){
_338=_307.filter(node.attributes,function(a){
return a.specified;
});
}else{
var _339=/^input$|^img$/i.test(node.nodeName)?node:node.cloneNode(false),_33a=_339.outerHTML.replace(/=[^\s"']+|="[^"]*"|='[^']*'/g,"").replace(/^\s*<[a-zA-Z0-9]*\s*/,"").replace(/\s*>.*$/,"");
_338=_307.map(_33a.split(/\s+/),function(name){
var _33b=name.toLowerCase();
return {name:name,value:(node.nodeName=="LI"&&name=="value")||_33b=="enctype"?node.getAttribute(_33b):node.getAttributeNode(_33b).value};
});
}
}
var _33c=_333.scope||dojo._scopeName,_33d="data-"+_33c+"-",hash={};
if(_33c!=="dojo"){
hash[_33d+"props"]="data-dojo-props";
hash[_33d+"type"]="data-dojo-type";
hash[_33d+"mixins"]="data-dojo-mixins";
hash[_33c+"type"]="dojoType";
hash[_33d+"id"]="data-dojo-id";
}
var i=0,item,_33e=[],_33f,_340;
while(item=_338[i++]){
var name=item.name,_341=name.toLowerCase(),_342=item.value;
switch(hash[_341]||_341){
case "data-dojo-type":
case "dojotype":
case "data-dojo-mixins":
break;
case "data-dojo-props":
_340=_342;
break;
case "data-dojo-id":
case "jsid":
_33f=_342;
break;
case "data-dojo-attach-point":
case "dojoattachpoint":
_337.dojoAttachPoint=_342;
break;
case "data-dojo-attach-event":
case "dojoattachevent":
_337.dojoAttachEvent=_342;
break;
case "class":
_337["class"]=node.className;
break;
case "style":
_337["style"]=node.style&&node.style.cssText;
break;
default:
if(!(name in _336)){
var map=_312(ctor);
name=map[_341]||name;
}
if(name in _336){
switch(typeof _336[name]){
case "string":
_337[name]=_342;
break;
case "number":
_337[name]=_342.length?Number(_342):NaN;
break;
case "boolean":
_337[name]=_342.toLowerCase()!="false";
break;
case "function":
if(_342===""||_342.search(/[^\w\.]+/i)!=-1){
_337[name]=new Function(_342);
}else{
_337[name]=_306.getObject(_342,false)||new Function(_342);
}
_33e.push(name);
break;
default:
var pVal=_336[name];
_337[name]=(pVal&&"length" in pVal)?(_342?_342.split(/\s*,\s*/):[]):(pVal instanceof Date)?(_342==""?new Date(""):_342=="now"?new Date():_30c.fromISOString(_342)):(pVal instanceof _30a)?(dojo.baseUrl+_342):_310(_342);
}
}else{
_337[name]=_342;
}
}
}
for(var j=0;j<_33e.length;j++){
var _343=_33e[j].toLowerCase();
node.removeAttribute(_343);
node[_343]=null;
}
if(_340){
try{
_340=_310.call(_333.propsThis,"{"+_340+"}");
_306.mixin(_337,_340);
}
catch(e){
throw new Error(e.toString()+" in data-dojo-props='"+_340+"'");
}
}
_306.mixin(_337,_332);
if(!_334){
_334=(ctor&&(ctor._noScript||_336._noScript)?[]:_30e("> script[type^='dojo/']",node));
}
var _344=[],_345=[],_346=[],ons=[];
if(_334){
for(i=0;i<_334.length;i++){
var _347=_334[i];
node.removeChild(_347);
var _348=(_347.getAttribute(_33d+"event")||_347.getAttribute("event")),prop=_347.getAttribute(_33d+"prop"),_349=_347.getAttribute(_33d+"method"),_34a=_347.getAttribute(_33d+"advice"),_34b=_347.getAttribute("type"),nf=this._functionFromScript(_347,_33d);
if(_348){
if(_34b=="dojo/connect"){
_344.push({method:_348,func:nf});
}else{
if(_34b=="dojo/on"){
ons.push({event:_348,func:nf});
}else{
_337[_348]=nf;
}
}
}else{
if(_34b=="dojo/aspect"){
_344.push({method:_349,advice:_34a,func:nf});
}else{
if(_34b=="dojo/watch"){
_346.push({prop:prop,func:nf});
}else{
_345.push(nf);
}
}
}
}
}
var _34c=ctor.markupFactory||_336.markupFactory;
var _34d=_34c?_34c(_337,node,ctor):new ctor(_337,node);
function _34e(_34f){
if(_33f){
_306.setObject(_33f,_34f);
}
for(i=0;i<_344.length;i++){
_30b[_344[i].advice||"after"](_34f,_344[i].method,_306.hitch(_34f,_344[i].func),true);
}
for(i=0;i<_345.length;i++){
_345[i].call(_34f);
}
for(i=0;i<_346.length;i++){
_34f.watch(_346[i].prop,_346[i].func);
}
for(i=0;i<ons.length;i++){
don(_34f,ons[i].event,ons[i].func);
}
return _34f;
};
if(_34d.then){
return _34d.then(_34e);
}else{
return _34e(_34d);
}
},scan:function(root,_350){
var list=[],mids=[],_351={};
var _352=(_350.scope||dojo._scopeName)+"Type",_353="data-"+(_350.scope||dojo._scopeName)+"-",_354=_353+"type",_355=_353+"textdir",_356=_353+"mixins";
var node=root.firstChild;
var _357=_350.inherited;
if(!_357){
function _358(node,attr){
return (node.getAttribute&&node.getAttribute(attr))||(node.parentNode&&_358(node.parentNode,attr));
};
_357={dir:_358(root,"dir"),lang:_358(root,"lang"),textDir:_358(root,_355)};
for(var key in _357){
if(!_357[key]){
delete _357[key];
}
}
}
var _359={inherited:_357};
var _35a;
var _35b;
function _35c(_35d){
if(!_35d.inherited){
_35d.inherited={};
var node=_35d.node,_35e=_35c(_35d.parent);
var _35f={dir:node.getAttribute("dir")||_35e.dir,lang:node.getAttribute("lang")||_35e.lang,textDir:node.getAttribute(_355)||_35e.textDir};
for(var key in _35f){
if(_35f[key]){
_35d.inherited[key]=_35f[key];
}
}
}
return _35d.inherited;
};
while(true){
if(!node){
if(!_359||!_359.node){
break;
}
node=_359.node.nextSibling;
_35b=false;
_359=_359.parent;
_35a=_359.scripts;
continue;
}
if(node.nodeType!=1){
node=node.nextSibling;
continue;
}
if(_35a&&node.nodeName.toLowerCase()=="script"){
type=node.getAttribute("type");
if(type&&/^dojo\/\w/i.test(type)){
_35a.push(node);
}
node=node.nextSibling;
continue;
}
if(_35b){
node=node.nextSibling;
continue;
}
var type=node.getAttribute(_354)||node.getAttribute(_352);
var _360=node.firstChild;
if(!type&&(!_360||(_360.nodeType==3&&!_360.nextSibling))){
node=node.nextSibling;
continue;
}
var _361;
var ctor=null;
if(type){
var _362=node.getAttribute(_356),_363=_362?[type].concat(_362.split(/\s*,\s*/)):[type];
try{
ctor=_315(_363,_350.contextRequire);
}
catch(e){
}
if(!ctor){
_307.forEach(_363,function(t){
if(~t.indexOf("/")&&!_351[t]){
_351[t]=true;
mids[mids.length]=t;
}
});
}
var _364=ctor&&!ctor.prototype._noScript?[]:null;
_361={types:_363,ctor:ctor,parent:_359,node:node,scripts:_364};
_361.inherited=_35c(_361);
list.push(_361);
}else{
_361={node:node,scripts:_35a,parent:_359};
}
_35a=_364;
_35b=node.stopParser||(ctor&&ctor.prototype.stopParser&&!(_350.template));
_359=_361;
node=_360;
}
var d=new _30d();
if(mids.length){
if(has("dojo-debug-messages")){
console.warn("WARNING: Modules being Auto-Required: "+mids.join(", "));
}
var r=_350.contextRequire||_305;
r(mids,function(){
d.resolve(_307.filter(list,function(_365){
if(!_365.ctor){
try{
_365.ctor=_315(_365.types,_350.contextRequire);
}
catch(e){
}
}
var _366=_365.parent;
while(_366&&!_366.types){
_366=_366.parent;
}
var _367=_365.ctor&&_365.ctor.prototype;
_365.instantiateChildren=!(_367&&_367.stopParser&&!(_350.template));
_365.instantiate=!_366||(_366.instantiate&&_366.instantiateChildren);
return _365.instantiate;
}));
});
}else{
d.resolve(list);
}
return d.promise;
},_require:function(_368,_369){
var hash=_310("{"+_368.innerHTML+"}"),vars=[],mids=[],d=new _30d();
var _36a=(_369&&_369.contextRequire)||_305;
for(var name in hash){
vars.push(name);
mids.push(hash[name]);
}
_36a(mids,function(){
for(var i=0;i<vars.length;i++){
_306.setObject(vars[i],arguments[i]);
}
d.resolve(arguments);
});
return d.promise;
},_scanAmd:function(root,_36b){
var _36c=new _30d(),_36d=_36c.promise;
_36c.resolve(true);
var self=this;
_30e("script[type='dojo/require']",root).forEach(function(node){
_36d=_36d.then(function(){
return self._require(node,_36b);
});
node.parentNode.removeChild(node);
});
return _36d;
},parse:function(_36e,_36f){
if(_36e&&typeof _36e!="string"&&!("nodeType" in _36e)){
_36f=_36e;
_36e=_36f.rootNode;
}
var root=_36e?dom.byId(_36e):_309.body();
_36f=_36f||{};
var _370=_36f.template?{template:true}:{},_371=[],self=this;
var p=this._scanAmd(root,_36f).then(function(){
return self.scan(root,_36f);
}).then(function(_372){
return self._instantiate(_372,_370,_36f,true);
}).then(function(_373){
return _371=_371.concat(_373);
}).otherwise(function(e){
console.error("dojo/parser::parse() error",e);
throw e;
});
_306.mixin(_371,p);
return _371;
}};
if(1){
dojo.parser=_319;
}
if(_308.parseOnLoad){
_30f(100,_319,"parse");
}
return _319;
});
},"dojox/html/_base":function(){
define(["dojo/_base/declare","dojo/Deferred","dojo/dom-construct","dojo/html","dojo/_base/kernel","dojo/_base/lang","dojo/ready","dojo/_base/sniff","dojo/_base/url","dojo/_base/xhr","dojo/when","dojo/_base/window"],function(_374,_375,_376,_377,_378,lang,_379,has,_37a,_37b,when,_37c){
var html=_378.getObject("dojox.html",true);
if(has("ie")){
var _37d=/(AlphaImageLoader\([^)]*?src=(['"]))(?![a-z]+:|\/)([^\r\n;}]+?)(\2[^)]*\)\s*[;}]?)/g;
}
var _37e=/(?:(?:@import\s*(['"])(?![a-z]+:|\/)([^\r\n;{]+?)\1)|url\(\s*(['"]?)(?![a-z]+:|\/)([^\r\n;]+?)\3\s*\))([a-z, \s]*[;}]?)/g;
var _37f=html._adjustCssPaths=function(_380,_381){
if(!_381||!_380){
return;
}
if(_37d){
_381=_381.replace(_37d,function(_382,pre,_383,url,post){
return pre+(new _37a(_380,"./"+url).toString())+post;
});
}
return _381.replace(_37e,function(_384,_385,_386,_387,_388,_389){
if(_386){
return "@import \""+(new _37a(_380,"./"+_386).toString())+"\""+_389;
}else{
return "url("+(new _37a(_380,"./"+_388).toString())+")"+_389;
}
});
};
var _38a=/(<[a-z][a-z0-9]*\s[^>]*)(?:(href|src)=(['"]?)([^>]*?)\3|style=(['"]?)([^>]*?)\5)([^>]*>)/gi;
var _38b=html._adjustHtmlPaths=function(_38c,cont){
var url=_38c||"./";
return cont.replace(_38a,function(tag,_38d,name,_38e,_38f,_390,_391,end){
return _38d+(name?(name+"="+_38e+(new _37a(url,_38f).toString())+_38e):("style="+_390+_37f(url,_391)+_390))+end;
});
};
var _392=html._snarfStyles=function(_393,cont,_394){
_394.attributes=[];
cont=cont.replace(/<[!][-][-](.|\s)*?[-][-]>/g,function(_395){
return _395.replace(/<(\/?)style\b/ig,"&lt;$1Style").replace(/<(\/?)link\b/ig,"&lt;$1Link").replace(/@import "/ig,"@ import \"");
});
return cont.replace(/(?:<style([^>]*)>([\s\S]*?)<\/style>|<link\s+(?=[^>]*rel=['"]?stylesheet)([^>]*?href=(['"])([^>]*?)\4[^>\/]*)\/?>)/gi,function(_396,_397,_398,_399,_39a,href){
var i,attr=(_397||_399||"").replace(/^\s*([\s\S]*?)\s*$/i,"$1");
if(_398){
i=_394.push(_393?_37f(_393,_398):_398);
}else{
i=_394.push("@import \""+href+"\";");
attr=attr.replace(/\s*(?:rel|href)=(['"])?[^\s]*\1\s*/gi,"");
}
if(attr){
attr=attr.split(/\s+/);
var _39b={},tmp;
for(var j=0,e=attr.length;j<e;j++){
tmp=attr[j].split("=");
_39b[tmp[0]]=tmp[1].replace(/^\s*['"]?([\s\S]*?)['"]?\s*$/,"$1");
}
_394.attributes[i-1]=_39b;
}
return "";
});
};
var _39c=html._snarfScripts=function(cont,_39d){
_39d.code="";
cont=cont.replace(/<[!][-][-](.|\s)*?[-][-]>/g,function(_39e){
return _39e.replace(/<(\/?)script\b/ig,"&lt;$1Script");
});
function _39f(src){
if(_39d.downloadRemote){
src=src.replace(/&([a-z0-9#]+);/g,function(m,name){
switch(name){
case "amp":
return "&";
case "gt":
return ">";
case "lt":
return "<";
default:
return name.charAt(0)=="#"?String.fromCharCode(name.substring(1)):"&"+name+";";
}
});
_37b.get({url:src,sync:true,load:function(code){
if(_39d.code!==""){
code="\n"+code;
}
_39d.code+=code+";";
},error:_39d.errBack});
}
};
return cont.replace(/<script\s*(?![^>]*type=['"]?(?:dojo\/|text\/html\b))[^>]*?(?:src=(['"]?)([^>]*?)\1[^>]*)?>([\s\S]*?)<\/script>/gi,function(_3a0,_3a1,src,code){
if(src){
_39f(src);
}else{
if(_39d.code!==""){
code="\n"+code;
}
_39d.code+=code+";";
}
return "";
});
};
var _3a2=html.evalInGlobal=function(code,_3a3){
_3a3=_3a3||_37c.doc.body;
var n=_3a3.ownerDocument.createElement("script");
n.type="text/javascript";
_3a3.appendChild(n);
n.text=code;
};
html._ContentSetter=_374(_377._ContentSetter,{adjustPaths:false,referencePath:".",renderStyles:false,executeScripts:false,scriptHasHooks:false,scriptHookReplacement:null,_renderStyles:function(_3a4){
this._styleNodes=[];
var st,att,_3a5,doc=this.node.ownerDocument;
var head=doc.getElementsByTagName("head")[0];
for(var i=0,e=_3a4.length;i<e;i++){
_3a5=_3a4[i];
att=_3a4.attributes[i];
st=doc.createElement("style");
st.setAttribute("type","text/css");
for(var x in att){
st.setAttribute(x,att[x]);
}
this._styleNodes.push(st);
head.appendChild(st);
if(st.styleSheet){
st.styleSheet.cssText=_3a5;
}else{
st.appendChild(doc.createTextNode(_3a5));
}
}
},empty:function(){
this.inherited("empty",arguments);
this._styles=[];
},onBegin:function(){
this.inherited("onBegin",arguments);
var cont=this.content,node=this.node;
var _3a6=this._styles;
if(lang.isString(cont)){
if(this.adjustPaths&&this.referencePath){
cont=_38b(this.referencePath,cont);
}
if(this.renderStyles||this.cleanContent){
cont=_392(this.referencePath,cont,_3a6);
}
if(this.executeScripts){
var _3a7=this;
var _3a8={downloadRemote:true,errBack:function(e){
_3a7._onError.call(_3a7,"Exec","Error downloading remote script in \""+_3a7.id+"\"",e);
}};
cont=_39c(cont,_3a8);
this._code=_3a8.code;
}
}
this.content=cont;
},onEnd:function(){
var code=this._code,_3a9=this._styles;
if(this._styleNodes&&this._styleNodes.length){
while(this._styleNodes.length){
_376.destroy(this._styleNodes.pop());
}
}
if(this.renderStyles&&_3a9&&_3a9.length){
this._renderStyles(_3a9);
}
var d=new _375();
var _3aa=this.getInherited(arguments),args=arguments,_3ab=lang.hitch(this,function(){
_3aa.apply(this,args);
when(this.parseDeferred,function(){
d.resolve();
});
});
if(this.executeScripts&&code){
if(this.cleanContent){
code=code.replace(/(<!--|(?:\/\/)?-->|<!\[CDATA\[|\]\]>)/g,"");
}
if(this.scriptHasHooks){
code=code.replace(/_container_(?!\s*=[^=])/g,this.scriptHookReplacement);
}
try{
_3a2(code,this.node);
}
catch(e){
this._onError("Exec","Error eval script in "+this.id+", "+e.message,e);
}
_379(_3ab);
}else{
_3ab();
}
return d.promise;
},tearDown:function(){
this.inherited(arguments);
delete this._styles;
if(this._styleNodes&&this._styleNodes.length){
while(this._styleNodes.length){
_376.destroy(this._styleNodes.pop());
}
}
delete this._styleNodes;
lang.mixin(this,html._ContentSetter.prototype);
}});
html.set=function(node,cont,_3ac){
if(!_3ac){
return _377._setNodeContent(node,cont,true);
}else{
var op=new html._ContentSetter(lang.mixin(_3ac,{content:cont,node:node}));
return op.set();
}
};
return html;
});
},"curam/widget/OptimalBrowserMessage":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/dom","dojo/dom-class","curam/util","curam/util/UIMFragment","curam/ui/ClientDataAccessor","dijit/_Widget","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/form/Button","dojo/has","dojo/text!curam/widget/templates/OptimalBrowserMessage.html","dojo/dom-attr"],function(_3ad,lang,dom,_3ae,util,_3af,_3b0,_3b1,_3b2,_3b3,_3b4,_3b5,_3b6,has,_3b7,_3b8){
return _3ad("curam.widget.OptimalBrowserMessage",[_3b1,_3b2,_3b3],{OPTIMAL_BROWSER_MSG:"optimal-browser-msg",isExternalApp:null,optimalBrowserMsgPaddingCSS:"optimal-browser-banner",optimalBrowserNode:null,appSectionsNode:null,appBannerHeaderNode:null,intApp:"internal",extApp:"external",templateString:_3b7,widgetsInTemplate:true,baseClass:"",optimalBrowserNodeID:"_optimalMessage",_appConfig:null,postMixInProperties:function(){
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this._init();
this._loadNodes(this._optimalMessage.id);
},_init:function(){
da=new _3b0();
da.getRaw("/config/tablayout/settings["+curam.config.appID+"]",lang.hitch(this,function(data){
console.log("External App config data:"+data);
this._appConfig=data;
this._getAppConfig();
this._updateOptimalBrowserNode();
}),function(_3b9,args){
console.log("External App config data load error:"+_3b9);
},null);
},_getAppConfig:function(){
var _3ba=this._appConfig.optimalBrowserMessageEnabled;
var _3bb=this._createStorageKey(this.OPTIMAL_BROWSER_MSG);
var _3bc=this;
if(_3ba=="true"|_3ba=="TRUE"){
return _3bc._isOptimalBrowserCheckDue(_3bb,_3bc);
}
return false;
},_updateOptimalBrowserNode:function(){
this.optimalBrowserNode=dom.byId(this._optimalMessage.id);
_3b8.set(optimalBrowserNode,"aria-label",this._appConfig.optimalMessageDivLabel);
},_isOptimalBrowserCheckDue:function(_3bd,_3be){
var _3bf=localStorage[_3bd];
if(_3bf&&_3bf!=""){
if(new Date(_3be._getTargetDate())>new Date(_3bf)){
_3be._executeBrowserVersionCheck();
return true;
}
}else{
_3be._executeBrowserVersionCheck();
return true;
}
return false;
},_executeBrowserVersionCheck:function(){
var _3c0=this._appConfig.ieMinVersion;
var _3c1=this._appConfig.ieMaxVersion;
var _3c2=this._appConfig.ffMinVersion;
var _3c3=this._appConfig.ffMaxVersion;
var _3c4=this._appConfig.chromeMinVersion;
var _3c5=this._appConfig.chromeMaxVersion;
var _3c6=this._appConfig.safariMinVersion;
var _3c7=this._appConfig.safariMaxVersion;
var _3c8=dojo.isIE;
var _3c9=has("trident")||has("edge");
var _3ca=dojo.isFF;
var _3cb=dojo.isChrome;
var _3cc=dojo.isSafari;
if(_3c8!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_3c8,_3c0,_3c1);
}else{
if(_3c9>6&&this.isExternalApp){
var _3cd=_3c0-4;
var _3ce=_3c1-4;
return this._isCurrentBrowserVerSupported(_3c9,_3cd,_3ce);
}else{
if(_3ca!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_3ca,_3c2,_3c3);
}else{
if(_3cb!=undefined){
return this._isCurrentBrowserVerSupported(_3cb,_3c4,_3c5);
}else{
if(_3cc!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_3cc,_3c6,_3c7);
}
}
}
}
}
return false;
},_isCurrentBrowserVerSupported:function(_3cf,_3d0,_3d1){
var _3d2=false;
if(_3d0>0){
if(_3cf<_3d0){
_3d2=true;
this._displayOptimalBrowserMsg();
return true;
}
}
if(_3d1>0&&!_3d2){
if(_3cf>_3d1){
this._displayOptimalBrowserMsg();
return true;
}
}
return false;
},_displayOptimalBrowserMsg:function(){
this._addOrRemoveCssForInternalApp(true,this.optimalBrowserMsgPaddingCSS);
_3af.get({url:"optimal-browser-msg-fragment.jspx",targetID:this._optimalMessage.id});
this._postRenderingTasks();
},_postRenderingTasks:function(){
var _3d3=this._optimalMessage.id;
dojo.addOnLoad(function(){
var _3d4=dom.byId(_3d3);
_3ae.remove(_3d4,_3d4.className);
});
localStorage[this._createStorageKey(this.OPTIMAL_BROWSER_MSG)]=this._getTargetDate(this._appConfig.nextBrowserCheck);
},_loadNodes:function(_3d5){
dojo.addOnLoad(function(){
this.optimalBrowserNode=dom.byId(_3d5);
this.appSectionsNode=dom.byId("app-sections-container-dc");
this.appBannerHeaderNode=dom.byId("app-header-container-dc");
});
},_createStorageKey:function(_3d6){
if(this.isExternalApp){
_3d6=_3d6+"_"+this.extApp;
}else{
_3d6=_3d6+"_"+this.intApp;
}
return _3d6;
},_addOrRemoveCssForInternalApp:function(_3d7,_3d8){
var _3d9=this.isExternalApp;
dojo.addOnLoad(function(){
if(!_3d9){
if(_3d7){
_3ae.add(this.appSectionsNode,_3d8);
if(this.appBannerHeaderNode){
_3ae.add(this.appSectionsNode.children.item(1),_3d8);
_3ae.add(this.appSectionsNode.children.item(2),_3d8);
}
}else{
_3ae.remove(this.appSectionsNode,_3d8);
if(this.appBannerHeaderNode){
_3ae.remove(this.appSectionsNode.children.item(1),_3d8);
_3ae.remove(this.appSectionsNode.children.item(2),_3d8);
}
}
}
});
},_getTargetDate:function(_3da){
var _3db=new Date();
if(_3da==undefined){
_3db.setDate(_3db.getDate());
}else{
_3db.setDate(_3db.getDate()+_3da);
}
return _3db.toUTCString();
},exitOptimalBrowserMessageBox:function(){
var _3dc=dom.byId(this._optimalMessage.id);
if(_3dc){
_3dc.parentNode.removeChild(_3dc);
}
this._addOrRemoveCssForInternalApp(false,this.optimalBrowserMsgPaddingCSS);
}});
});
},"dijit/form/ToggleButton":function(){
define(["dojo/_base/declare","dojo/_base/kernel","./Button","./_ToggleButtonMixin"],function(_3dd,_3de,_3df,_3e0){
return _3dd("dijit.form.ToggleButton",[_3df,_3e0],{baseClass:"dijitToggleButton",setChecked:function(_3e1){
_3de.deprecated("setChecked("+_3e1+") is deprecated. Use set('checked',"+_3e1+") instead.","","2.0");
this.set("checked",_3e1);
}});
});
},"dojo/date/stamp":function(){
define(["../_base/lang","../_base/array"],function(lang,_3e2){
var _3e3={};
lang.setObject("dojo.date.stamp",_3e3);
_3e3.fromISOString=function(_3e4,_3e5){
if(!_3e3._isoRegExp){
_3e3._isoRegExp=/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
}
var _3e6=_3e3._isoRegExp.exec(_3e4),_3e7=null;
if(_3e6){
_3e6.shift();
if(_3e6[1]){
_3e6[1]--;
}
if(_3e6[6]){
_3e6[6]*=1000;
}
if(_3e5){
_3e5=new Date(_3e5);
_3e2.forEach(_3e2.map(["FullYear","Month","Date","Hours","Minutes","Seconds","Milliseconds"],function(prop){
return _3e5["get"+prop]();
}),function(_3e8,_3e9){
_3e6[_3e9]=_3e6[_3e9]||_3e8;
});
}
_3e7=new Date(_3e6[0]||1970,_3e6[1]||0,_3e6[2]||1,_3e6[3]||0,_3e6[4]||0,_3e6[5]||0,_3e6[6]||0);
if(_3e6[0]<100){
_3e7.setFullYear(_3e6[0]||1970);
}
var _3ea=0,_3eb=_3e6[7]&&_3e6[7].charAt(0);
if(_3eb!="Z"){
_3ea=((_3e6[8]||0)*60)+(Number(_3e6[9])||0);
if(_3eb!="-"){
_3ea*=-1;
}
}
if(_3eb){
_3ea-=_3e7.getTimezoneOffset();
}
if(_3ea){
_3e7.setTime(_3e7.getTime()+_3ea*60000);
}
}
return _3e7;
};
_3e3.toISOString=function(_3ec,_3ed){
var _3ee=function(n){
return (n<10)?"0"+n:n;
};
_3ed=_3ed||{};
var _3ef=[],_3f0=_3ed.zulu?"getUTC":"get",date="";
if(_3ed.selector!="time"){
var year=_3ec[_3f0+"FullYear"]();
date=["0000".substr((year+"").length)+year,_3ee(_3ec[_3f0+"Month"]()+1),_3ee(_3ec[_3f0+"Date"]())].join("-");
}
_3ef.push(date);
if(_3ed.selector!="date"){
var time=[_3ee(_3ec[_3f0+"Hours"]()),_3ee(_3ec[_3f0+"Minutes"]()),_3ee(_3ec[_3f0+"Seconds"]())].join(":");
var _3f1=_3ec[_3f0+"Milliseconds"]();
if(_3ed.milliseconds){
time+="."+(_3f1<100?"0":"")+_3ee(_3f1);
}
if(_3ed.zulu){
time+="Z";
}else{
if(_3ed.selector!="time"){
var _3f2=_3ec.getTimezoneOffset();
var _3f3=Math.abs(_3f2);
time+=(_3f2>0?"-":"+")+_3ee(Math.floor(_3f3/60))+":"+_3ee(_3f3%60);
}
}
_3ef.push(time);
}
return _3ef.join("T");
};
return _3e3;
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_3f4,lang,_3f5,when){
return _3f4("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_3f6){
if(_3f6){
this.set(_3f6);
}
},_get:function(name,_3f7){
return typeof this[_3f7.g]==="function"?this[_3f7.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,_3f8){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var _3f9=this._getAttrNames(name),_3fa=this._get(name,_3f9),_3fb=this[_3f9.s],_3fc;
if(typeof _3fb==="function"){
_3fc=_3fb.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=_3f8;
}
if(this._watchCallbacks){
var self=this;
when(_3fc,function(){
self._watchCallbacks(name,_3fa,_3f8);
});
}
return this;
},_changeAttrValue:function(name,_3fd){
var _3fe=this.get(name);
this[name]=_3fd;
if(this._watchCallbacks){
this._watchCallbacks(name,_3fe,_3fd);
}
return this;
},watch:function(name,_3ff){
var _400=this._watchCallbacks;
if(!_400){
var self=this;
_400=this._watchCallbacks=function(name,_401,_402,_403){
var _404=function(_405){
if(_405){
_405=_405.slice();
for(var i=0,l=_405.length;i<l;i++){
_405[i].call(self,name,_401,_402);
}
}
};
_404(_400["_"+name]);
if(!_403){
_404(_400["*"]);
}
};
}
if(!_3ff&&typeof name==="function"){
_3ff=name;
name="*";
}else{
name="_"+name;
}
var _406=_400[name];
if(typeof _406!=="object"){
_406=_400[name]=[];
}
_406.push(_3ff);
var _407={};
_407.unwatch=_407.remove=function(){
var _408=_3f5.indexOf(_406,_3ff);
if(_408>-1){
_406.splice(_408,1);
}
};
return _407;
}});
});
},"dijit/form/ComboButton":function(){
define(["dojo/_base/declare","dojo/keys","../focus","./DropDownButton","dojo/text!./templates/ComboButton.html","../a11yclick"],function(_409,keys,_40a,_40b,_40c){
return _409("dijit.form.ComboButton",_40b,{templateString:_40c,_setIdAttr:"",_setTabIndexAttr:["focusNode","titleNode"],_setTitleAttr:"titleNode",optionsTitle:"",baseClass:"dijitComboButton",cssStateNodes:{"buttonNode":"dijitButtonNode","titleNode":"dijitButtonContents","_popupStateNode":"dijitDownArrowButton"},_focusedNode:null,_onButtonKeyDown:function(evt){
if(evt.keyCode==keys[this.isLeftToRight()?"RIGHT_ARROW":"LEFT_ARROW"]){
_40a.focus(this._popupStateNode);
evt.stopPropagation();
evt.preventDefault();
}
},_onArrowKeyDown:function(evt){
if(evt.keyCode==keys[this.isLeftToRight()?"LEFT_ARROW":"RIGHT_ARROW"]){
_40a.focus(this.titleNode);
evt.stopPropagation();
evt.preventDefault();
}
},focus:function(_40d){
if(!this.disabled){
_40a.focus(_40d=="start"?this.titleNode:this._popupStateNode);
}
}});
});
},"dojo/NodeList-traverse":function(){
define(["./query","./_base/lang","./_base/array"],function(_40e,lang,_40f){
var _410=_40e.NodeList;
lang.extend(_410,{_buildArrayFromCallback:function(_411){
var ary=[];
for(var i=0;i<this.length;i++){
var _412=_411.call(this[i],this[i],ary);
if(_412){
ary=ary.concat(_412);
}
}
return ary;
},_getUniqueAsNodeList:function(_413){
var ary=[];
for(var i=0,node;node=_413[i];i++){
if(node.nodeType==1&&_40f.indexOf(ary,node)==-1){
ary.push(node);
}
}
return this._wrap(ary,null,this._NodeListCtor);
},_getUniqueNodeListWithParent:function(_414,_415){
var ary=this._getUniqueAsNodeList(_414);
ary=(_415?_40e._filterResult(ary,_415):ary);
return ary._stash(this);
},_getRelatedUniqueNodes:function(_416,_417){
return this._getUniqueNodeListWithParent(this._buildArrayFromCallback(_417),_416);
},children:function(_418){
return this._getRelatedUniqueNodes(_418,function(node,ary){
return lang._toArray(node.childNodes);
});
},closest:function(_419,root){
return this._getRelatedUniqueNodes(null,function(node,ary){
do{
if(_40e._filterResult([node],_419,root).length){
return node;
}
}while(node!=root&&(node=node.parentNode)&&node.nodeType==1);
return null;
});
},parent:function(_41a){
return this._getRelatedUniqueNodes(_41a,function(node,ary){
return node.parentNode;
});
},parents:function(_41b){
return this._getRelatedUniqueNodes(_41b,function(node,ary){
var pary=[];
while(node.parentNode){
node=node.parentNode;
pary.push(node);
}
return pary;
});
},siblings:function(_41c){
return this._getRelatedUniqueNodes(_41c,function(node,ary){
var pary=[];
var _41d=(node.parentNode&&node.parentNode.childNodes);
for(var i=0;i<_41d.length;i++){
if(_41d[i]!=node){
pary.push(_41d[i]);
}
}
return pary;
});
},next:function(_41e){
return this._getRelatedUniqueNodes(_41e,function(node,ary){
var next=node.nextSibling;
while(next&&next.nodeType!=1){
next=next.nextSibling;
}
return next;
});
},nextAll:function(_41f){
return this._getRelatedUniqueNodes(_41f,function(node,ary){
var pary=[];
var next=node;
while((next=next.nextSibling)){
if(next.nodeType==1){
pary.push(next);
}
}
return pary;
});
},prev:function(_420){
return this._getRelatedUniqueNodes(_420,function(node,ary){
var prev=node.previousSibling;
while(prev&&prev.nodeType!=1){
prev=prev.previousSibling;
}
return prev;
});
},prevAll:function(_421){
return this._getRelatedUniqueNodes(_421,function(node,ary){
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
return _410;
});
},"dijit/_base/window":function(){
define(["dojo/window","../main"],function(_422,_423){
_423.getDocumentWindow=function(doc){
return _422.get(doc);
};
});
},"dijit/PopupMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-style","dojo/_base/lang","dojo/query","./popup","./registry","./MenuItem","./hccss"],function(_424,_425,lang,_426,pm,_427,_428){
return _424("dijit.PopupMenuItem",_428,{baseClass:"dijitMenuItem dijitPopupMenuItem",_fillContent:function(){
if(this.srcNodeRef){
var _429=_426("*",this.srcNodeRef);
this.inherited(arguments,[_429[0]]);
this.dropDownContainer=this.srcNodeRef;
}
},_openPopup:function(_42a,_42b){
var _42c=this.popup;
pm.open(lang.delegate(_42a,{popup:this.popup,around:this.domNode}));
if(_42b&&_42c.focus){
_42c.focus();
}
},_closePopup:function(){
pm.close(this.popup);
this.popup.parentMenu=null;
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
if(!this.popup){
var node=_426("[widgetId]",this.dropDownContainer)[0];
this.popup=_427.byNode(node);
}
this.ownerDocumentBody.appendChild(this.popup.domNode);
this.popup.domNode.setAttribute("aria-labelledby",this.containerNode.id);
this.popup.startup();
this.popup.domNode.style.display="none";
if(this.arrowWrapper){
_425.set(this.arrowWrapper,"visibility","");
}
this.focusNode.setAttribute("aria-haspopup","true");
},destroyDescendants:function(_42d){
if(this.popup){
if(!this.popup._destroyed){
this.popup.destroyRecursive(_42d);
}
delete this.popup;
}
this.inherited(arguments);
}});
});
},"dojo/hccss":function(){
define(["require","./_base/config","./dom-class","./dom-style","./has","./domReady","./_base/window"],function(_42e,_42f,_430,_431,has,_432,win){
has.add("highcontrast",function(){
var div=win.doc.createElement("div");
div.style.cssText="border: 1px solid; border-color:red green; position: absolute; height: 5px; top: -999px;"+"background-image: url(\""+(_42f.blankGif||_42e.toUrl("./resources/blank.gif"))+"\");";
win.body().appendChild(div);
var cs=_431.getComputedStyle(div),_433=cs.backgroundImage,hc=(cs.borderTopColor==cs.borderRightColor)||(_433&&(_433=="none"||_433=="url(invalid-url:)"));
if(has("ie")<=8){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
return hc;
});
_432(function(){
if(has("highcontrast")){
_430.add(win.body(),"dj_a11y");
}
});
return has;
});
},"dijit/_BidiMixin":function(){
define([],function(){
var _434={LRM:"‎",LRE:"‪",PDF:"‬",RLM:"‏",RLE:"‫"};
return {textDir:"",getTextDir:function(text){
return this.textDir=="auto"?this._checkContextual(text):this.textDir;
},_checkContextual:function(text){
var fdc=/[A-Za-z\u05d0-\u065f\u066a-\u06ef\u06fa-\u07ff\ufb1d-\ufdff\ufe70-\ufefc]/.exec(text);
return fdc?(fdc[0]<="z"?"ltr":"rtl"):this.dir?this.dir:this.isLeftToRight()?"ltr":"rtl";
},applyTextDir:function(_435,text){
if(this.textDir){
var _436=this.textDir;
if(_436=="auto"){
if(typeof text==="undefined"){
var _437=_435.tagName.toLowerCase();
text=(_437=="input"||_437=="textarea")?_435.value:_435.innerText||_435.textContent||"";
}
_436=this._checkContextual(text);
}
if(_435.dir!=_436){
_435.dir=_436;
}
}
},enforceTextDirWithUcc:function(_438,text){
if(this.textDir){
if(_438){
_438.originalText=text;
}
var dir=this.textDir=="auto"?this._checkContextual(text):this.textDir;
return (dir=="ltr"?_434.LRE:_434.RLE)+text+_434.PDF;
}
return text;
},restoreOriginalText:function(_439){
if(_439.originalText){
_439.text=_439.originalText;
delete _439.originalText;
}
return _439;
},_setTextDirAttr:function(_43a){
if(!this._created||this.textDir!=_43a){
this._set("textDir",_43a);
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
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"curam/define":function(){
define(["dojo/_base/lang"],function(lang){
var _43b=this;
if(typeof (_43b.curam)=="undefined"){
_43b.curam={};
}
if(typeof (_43b.curam.define)=="undefined"){
lang.mixin(_43b.curam,{define:{}});
}
lang.mixin(_43b.curam.define,{singleton:function(_43c,_43d){
var _43e=_43c.split(".");
var _43f=window;
for(var i=0;i<_43e.length;i++){
var part=_43e[i];
if(typeof _43f[part]=="undefined"){
_43f[part]={};
}
_43f=_43f[part];
}
if(_43d){
lang.mixin(_43f,_43d);
}
}});
return _43b.curam.define;
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
},"idx/widget/_EventTriggerMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/event","dojo/_base/lang","dojo/dom","dojo/request/iframe","dojo/mouse","dojo/on","dojo/window","dijit/_MenuBase"],function(_440,_441,_442,lang,dom,_443,_444,on,_445,_446){
var dojo={},_447={};
return _441("idx.widget._EventTriggerMixin",null,{_bindings:null,_hoverTimer:null,hoverDuration:_446.prototype.popupDelay,constructor:function(){
this._bindings=[];
},_addEventTrigger:function(_448,_449,_44a,_44b){
_448=dom.byId(_448);
if(!_448){
require.log("ERROR: oneui._EventTriggerMixin._addEventTrigger(): Invalid triggerNode parameter.");
return;
}
var _44c=lang.hitch(this,function(_44d){
var _44e={triggerNode:_448,eventName:_449,event:_44d,additionalData:_44b};
if(!_44a||_44a(_44e)){
this._onTrigger(_44e);
}
});
var _44f=function(_450){
return {type:"hover",pageX:_450.pageX,pageY:_450.pageY,screenX:_450.screenX,screenY:_450.screenY,clientX:_450.clientX,clientY:_450.clientY};
};
var _451={triggerNode:_448,connectHandles:[]};
if(_449=="hover"){
_451.hoverDuration=this.hoverDuration;
_451.hoverTimer=null;
}
_451.bindFunction=function(){
var _452;
if(_448.tagName=="IFRAME"){
try{
var _453=_443.doc(_448);
_452=_453?_453.body:null;
}
catch(e){
require.log("ERROR: oneui._EventTriggerMixin._addEventTrigger(): Error accessing body of document within iframe. "+e);
}
}else{
_452=_448;
}
if(!_452){
require.log("ERROR: oneui._EventTriggerMixin._addEventTrigger(): Unable to determine node to attach event listener(s) to.");
return;
}
if(_449=="hover"){
var _454=null;
_451.connectHandles.push(on(_452,_444.enter,lang.hitch(this,function(_455){
_454=_44f(_455);
if(_451.hoverTimer){
clearTimeout(_451.hoverTimer);
}
_451.hoverTimer=setTimeout(function(){
_44c(_454);
},_451.hoverDuration);
})));
_451.connectHandles.push(on(_452,_444.leave,lang.hitch(this,function(_456){
if(_451.hoverTimer){
clearTimeout(_451.hoverTimer);
_451.hoverTimer=null;
}
_454=undefined;
})));
_451.connectHandles.push(on(_452,"mousemove",function(_457){
_454=_44f(_457);
}));
}else{
_451.connectHandles.push(on(_452,_449,function(_458){
_44c(_458);
}));
}
};
_451.unbindFunction=function(){
_440.forEach(_451.connectHandles,function(conn){
conn.remove();
});
if(_451.hoverTimer){
clearTimeout(_451.hoverTimer);
_451.hoverTimer=null;
}
};
if(_448.tagName==="IFRAME"){
_451.iframeOnLoadHandler=function(_459){
try{
_451.unbindFunction();
}
catch(e){
}
_451.bindFunction();
};
if(_448.addEventListener){
_448.addEventListener("load",_451.iframeOnLoadHandler,false);
}else{
_448.attachEvent("onload",_451.iframeOnLoadHandler);
}
}
this._bindings.push(_451);
_451.bindFunction();
},_onTrigger:function(_45a){
},_removeEventTriggers:function(_45b){
if(_45b){
_45b=dom.byId(_45b);
}
for(var i=this._bindings.length-1;i>=0;i--){
var _45c=this._bindings[i];
if(!_45b||(_45b===_45c.triggerNode)){
_45c.unbindFunction();
if(_45c.iframeOnLoadHandler){
if(_45c.triggerNode.removeEventListener){
_45c.triggerNode.removeEventListener("load",_45c.iframeOnLoadHandler,false);
}else{
_45c.triggerNode.detachEvent("onload",_45c.iframeOnLoadHandler);
}
}
this._bindings.splice(i,1);
}
}
}});
});
},"dijit/_OnDijitClickMixin":function(){
define(["dojo/on","dojo/_base/array","dojo/keys","dojo/_base/declare","dojo/has","./a11yclick"],function(on,_45d,keys,_45e,has,_45f){
var ret=_45e("dijit._OnDijitClickMixin",null,{connect:function(obj,_460,_461){
return this.inherited(arguments,[obj,_460=="ondijitclick"?_45f:_460,_461]);
}});
ret.a11yclick=_45f;
return ret;
});
},"dojo/dnd/autoscroll":function(){
define(["../_base/lang","../sniff","../_base/window","../dom-geometry","../dom-style","../window"],function(lang,has,win,_462,_463,_464){
var _465={};
lang.setObject("dojo.dnd.autoscroll",_465);
_465.getViewport=_464.getBox;
_465.V_TRIGGER_AUTOSCROLL=32;
_465.H_TRIGGER_AUTOSCROLL=32;
_465.V_AUTOSCROLL_VALUE=16;
_465.H_AUTOSCROLL_VALUE=16;
var _466,doc=win.doc,_467=Infinity,_468=Infinity;
_465.autoScrollStart=function(d){
doc=d;
_466=_464.getBox(doc);
var html=win.body(doc).parentNode;
_467=Math.max(html.scrollHeight-_466.h,0);
_468=Math.max(html.scrollWidth-_466.w,0);
};
_465.autoScroll=function(e){
var v=_466||_464.getBox(doc),html=win.body(doc).parentNode,dx=0,dy=0;
if(e.clientX<_465.H_TRIGGER_AUTOSCROLL){
dx=-_465.H_AUTOSCROLL_VALUE;
}else{
if(e.clientX>v.w-_465.H_TRIGGER_AUTOSCROLL){
dx=Math.min(_465.H_AUTOSCROLL_VALUE,_468-html.scrollLeft);
}
}
if(e.clientY<_465.V_TRIGGER_AUTOSCROLL){
dy=-_465.V_AUTOSCROLL_VALUE;
}else{
if(e.clientY>v.h-_465.V_TRIGGER_AUTOSCROLL){
dy=Math.min(_465.V_AUTOSCROLL_VALUE,_467-html.scrollTop);
}
}
window.scrollBy(dx,dy);
};
_465._validNodes={"div":1,"p":1,"td":1};
_465._validOverflow={"auto":1,"scroll":1};
_465.autoScrollNodes=function(e){
var b,t,w,h,rx,ry,dx=0,dy=0,_469,_46a;
for(var n=e.target;n;){
if(n.nodeType==1&&(n.tagName.toLowerCase() in _465._validNodes)){
var s=_463.getComputedStyle(n),_46b=(s.overflow.toLowerCase() in _465._validOverflow),_46c=(s.overflowX.toLowerCase() in _465._validOverflow),_46d=(s.overflowY.toLowerCase() in _465._validOverflow);
if(_46b||_46c||_46d){
b=_462.getContentBox(n,s);
t=_462.position(n,true);
}
if(_46b||_46c){
w=Math.min(_465.H_TRIGGER_AUTOSCROLL,b.w/2);
rx=e.pageX-t.x;
if(has("webkit")||has("opera")){
rx+=win.body().scrollLeft;
}
dx=0;
if(rx>0&&rx<b.w){
if(rx<w){
dx=-w;
}else{
if(rx>b.w-w){
dx=w;
}
}
_469=n.scrollLeft;
n.scrollLeft=n.scrollLeft+dx;
}
}
if(_46b||_46d){
h=Math.min(_465.V_TRIGGER_AUTOSCROLL,b.h/2);
ry=e.pageY-t.y;
if(has("webkit")||has("opera")){
ry+=win.body().scrollTop;
}
dy=0;
if(ry>0&&ry<b.h){
if(ry<h){
dy=-h;
}else{
if(ry>b.h-h){
dy=h;
}
}
_46a=n.scrollTop;
n.scrollTop=n.scrollTop+dy;
}
}
if(dx||dy){
return;
}
}
try{
n=n.parentNode;
}
catch(x){
n=null;
}
}
_465.autoScroll(e);
};
return _465;
});
},"dojo/dnd/TimedMoveable":function(){
define(["../_base/declare","./Moveable"],function(_46e,_46f){
var _470=_46f.prototype.onMove;
return _46e("dojo.dnd.TimedMoveable",_46f,{timeout:40,constructor:function(node,_471){
if(!_471){
_471={};
}
if(_471.timeout&&typeof _471.timeout=="number"&&_471.timeout>=0){
this.timeout=_471.timeout;
}
},onMoveStop:function(_472){
if(_472._timer){
clearTimeout(_472._timer);
_470.call(this,_472,_472._leftTop);
}
_46f.prototype.onMoveStop.apply(this,arguments);
},onMove:function(_473,_474){
_473._leftTop=_474;
if(!_473._timer){
var _475=this;
_473._timer=setTimeout(function(){
_473._timer=null;
_470.call(_475,_473,_473._leftTop);
},this.timeout);
}
}});
});
},"dijit/_BidiSupport":function(){
define(["dojo/has","./_WidgetBase","./_BidiMixin"],function(has,_476,_477){
_476.extend(_477);
has.add("dojo-bidi",true);
return _476;
});
},"idx/widgets":function(){
define(["dojo/_base/lang","idx/main","dojo/dom","dojo/dom-class","dijit/_WidgetBase","dijit/_base/manager","./string","./util"],function(_478,_479,dDom,_47a,_47b,_47c,_47d,_47e){
var _47f=_478.getObject("widgets",true,_479);
_478.extend(_47b,{idxBaseClass:"",idxExtraClasses:"",idxDefaultsClass:"",idxHCDefaults:null,idxChildClass:"",mixinArgs:null,idxBeforePostMixInProperties:function(){
var _480=this.mixinArgs;
while(this.mixinArgs!=null){
var args=this.mixinArgs;
this.mixinArgs=null;
_478.mixin(this,args);
if(!this.params){
this.params={};
}
_478.mixin(this.params,args);
}
this.mixinArgs=_480;
if(this.params){
this.params.mixinArgs=_480;
}else{
this.params={mixinArgs:_480};
}
},idxAfterPostMixInProperties:function(){
},idxBeforeBuildRendering:function(){
},idxAfterBuildRendering:function(){
if((this.domNode)&&(_47d.nullTrim(this.idxBaseClass))){
_47a.add(this.domNode,this.idxBaseClass);
}
if((this.domNode)&&(_47d.nullTrim(this.idxExtraClasses))){
var _481=this.idxExtraClasses.split(",");
for(var _482=0;_482<_481.length;_482++){
var _483=_481[_482];
_47a.add(this.domNode,_483);
}
}
},idxBeforePostCreate:function(){
},idxAfterPostCreate:function(){
},idxBeforeStartup:function(){
},idxAfterStartup:function(){
var _484=this.getChildren();
if((this.domNode)&&(_47d.nullTrim(this.idxChildClass))){
this._idxStyleChildNodes(this.idxChildClass,this.domNode);
}
this._idxStyleChildren();
},_idxWidgetPostMixInProperties:function(){
if(!("_idxWidgetOrigPostMixInProperties" in this)){
return;
}
if(!this._idxWidgetOrigPostMixInProperties){
return;
}
this.postMixInProperties=this._idxWidgetOrigPostMixInProperties;
this.idxBeforePostMixInProperties();
this.postMixInProperties();
this.idxAfterPostMixInProperties();
},_idxWidgetBuildRendering:function(){
if(!("_idxWidgetOrigBuildRendering" in this)){
return;
}
if(!this._idxWidgetOrigBuildRendering){
return;
}
this.buildRendering=this._idxWidgetOrigBuildRendering;
this.idxBeforeBuildRendering();
this.buildRendering();
this.idxAfterBuildRendering();
},_idxWidgetPostCreate:function(){
if(!("_idxWidgetOrigPostCreate" in this)){
return;
}
if(!this._idxWidgetOrigPostCreate){
return;
}
this.postCreate=this._idxWidgetOrigPostCreate;
this.idxBeforePostCreate();
this.postCreate();
this.idxAfterPostCreate();
},_idxWidgetStartup:function(){
if(!("_idxWidgetOrigStartup" in this)){
return;
}
if(!this._idxWidgetOrigStartup){
return;
}
this.startup=this._idxWidgetOrigStartup;
this.idxBeforeStartup();
this.startup();
this.idxAfterStartup();
},_idxStyleChildren:function(){
if(!_47d.nullTrim(this.idxChildClass)){
return;
}
if(!_47d.nullTrim(this.baseClass)){
return;
}
var _485=this._idxPrevStyledChildren;
if((_485)&&(_485.length>0)){
var _486=this._idxPrevChildBase;
var _487=_486+"-idxChild";
var _488=_486+"-idxFirstChild";
var _489=_486+"-idxMiddleChild";
var _48a=_486+"-idxLastChild";
var _48b=_486+"-idxOnlyChild";
for(var _48c=0;_48c<_485.length;_48c++){
var _48d=_485[_48c];
if(!_48d.domNode){
continue;
}
if(_48d._idxUnstyleChildNodes){
_48d._idxUnstyleChildNodes(_48d.domNode,_486);
}else{
dRemoveClass(_48d.domNode,_487);
dRemoveClass(_48d.domNode,_488);
dRemoveClass(_48d.domNode,_489);
dRemoveClass(_48d.domNode,_48a);
dRemoveClass(_48d.domNode,_48b);
}
}
}
this._idxPrevStyledChildren=null;
this._idxPrevChildBase=null;
var _48c=0;
var _48e=this.getChildren();
if((_48e)&&(_48e.length>0)){
var _486=this.baseClass+"-"+this.idxChildClass;
var _487=_486+"-idxChild";
var _488=_486+"-idxFirstChild";
var _489=_486+"-idxMiddleChild";
var _48a=_486+"-idxLastChild";
var _48b=_486+"-idxOnlyChild";
this._idxPrevStyledChildren=[];
this._idxPrevChildBase=_486;
for(_48c=0;_48c<_48e.length;_48c++){
var _48d=_48e[_48c];
if(!_48d.domNode){
continue;
}
this._idxPrevStyledChildren.push(_48d);
if(_48d._idxStyleChildNodes){
_48d._idxStyleChildNodes(_487,_48d.domNode);
}else{
_47a.add(_48d.domNode,_487);
}
if(_48c==0){
if(_48d._idxStyleChildNodes){
_48d._idxStyleChildNodes(_488,_48d.domNode);
}else{
_47a.add(_48d.domNode,_488);
}
}
if((_48c>0)&&(_48c<(_48e.length-1))){
if(_48d._idxStyleChildNodes){
_48d._idxStyleChildNodes(_489,_48d.domNode);
}else{
_47a.add(_48d.domNode,_489);
}
}
if(_48c==(_48e.length-1)){
if(_48d._idxStyleChildNodes){
_48d._idxStyleChildNodes(_48a,_48d.domNode);
}else{
_47a.add(_48d.domNode,_48a);
}
}
if(_48e.length==1){
if(_48d._idxStyleChildNodes){
_48d._idxStyleChildNodes(_48b,_48d.domNode);
}else{
_47a.add(_48d.domNode,_48b);
}
}
}
}
},_idxUnstyleChildNodes:function(_48f,_490){
if(!_48f){
_48f=this.domNode;
}
if(!_48f){
return;
}
var _491=_490+"-idxChild";
var _492=_490+"-idxFirstChild";
var _493=_490+"-idxMiddleChild";
var _494=_490+"-idxLastChild";
var _495=_490+"-idxOnlyChild";
dRemoveClass(_48f,_491);
dRemoveClass(_48f,_492);
dRemoveClass(_48f,_493);
dRemoveClass(_48f,_494);
dRemoveClass(_48f,_495);
var _496=_48f.childNodes;
if(!_496){
return;
}
for(var _497=0;_497<_496.length;_497++){
var _498=_496[_497];
if(_47c.getEnclosingWidget(_498)==this){
this._idxUnstyleChildNodes(_498,_490);
}
}
},_idxStyleChildNodes:function(_499,_49a){
if(!_49a){
_49a=this.domNode;
}
if(!_49a){
return;
}
_47a.add(_49a,_499);
var _49b=_49a.childNodes;
if(!_49b){
return;
}
for(var _49c=0;_49c<_49b.length;_49c++){
var _49d=_49b[_49c];
if(_49d.nodeType!=1){
continue;
}
if(_47c.getEnclosingWidget(_49d)==this){
this._idxStyleChildNodes(_499,_49d);
}
}
}});
var _49e=_47b.prototype;
var _49f=_49e.create;
_49e.create=function(_4a0,_4a1){
var _4a2="";
var _4a3=false;
if(_4a0){
if(_4a0.idxDefaultsClass){
_4a2=_4a0.idxDefaultsClass;
_4a2=_47d.nullTrim(_4a2);
_4a3=false;
}
if((!_4a2)&&_4a0.idxBaseClass){
_4a2=_4a0.idxBaseClass;
_4a2=_47d.nullTrim(_4a2);
_4a3=true;
}
if((!_4a2)&&_4a0.baseClass){
_4a2=_4a0.baseClass;
_4a2=_47d.nullTrim(_4a2);
_4a3=true;
}
}
if(!_4a2){
_4a2=_47d.nullTrim(this.idxDefaultsClass);
_4a3=false;
}
if(!_4a2){
_4a2=_47d.nullTrim(this.idxBaseClass);
_4a3=true;
}
if(!_4a2){
_4a2=_47d.nullTrim(this.baseClass);
_4a3=true;
}
if((_4a2)&&(_4a1)){
if(_4a3){
_4a2=_4a2+"_idxDefaults";
}
var _4a4=_47e.getCSSOptions(_4a2,dDom.byId(_4a1),this,this.idxHCDefaults);
if(_4a4!=null){
for(var _4a5 in _4a4){
if(!(_4a5 in _4a0)){
_4a0[_4a5]=_4a4[_4a5];
}
}
}
}
if(!this._idxWidgetOrigBuildRendering){
this._idxWidgetOrigBuildRendering=this.buildRendering;
}
this.buildRendering=this._idxWidgetBuildRendering;
if(!this._idxWidgetOrigPostMixInProperties){
this._idxWidgetOrigPostMixInProperties=this.postMixInProperties;
}
this.postMixInProperties=this._idxWidgetPostMixInProperties;
if(!this._idxWidgetOrigPostCreate){
this._idxWidgetOrigPostCreate=this.postCreate;
}
this.postCreate=this._idxWidgetPostCreate;
if(!this._idxWidgetOrigStartup){
this._idxWidgetOrigStartup=this.startup;
}
this.startup=this._idxWidgetStartup;
return _49f.call(this,_4a0,_4a1);
};
return _47f;
});
},"dojo/cookie":function(){
define(["./_base/kernel","./regexp"],function(dojo,_4a6){
dojo.cookie=function(name,_4a7,_4a8){
var c=document.cookie,ret;
if(arguments.length==1){
var _4a9=c.match(new RegExp("(?:^|; )"+_4a6.escapeString(name)+"=([^;]*)"));
ret=_4a9?decodeURIComponent(_4a9[1]):undefined;
}else{
_4a8=_4a8||{};
var exp=_4a8.expires;
if(typeof exp=="number"){
var d=new Date();
d.setTime(d.getTime()+exp*24*60*60*1000);
exp=_4a8.expires=d;
}
if(exp&&exp.toUTCString){
_4a8.expires=exp.toUTCString();
}
_4a7=encodeURIComponent(_4a7);
var _4aa=name+"="+_4a7,_4ab;
for(_4ab in _4a8){
_4aa+="; "+_4ab;
var _4ac=_4a8[_4ab];
if(_4ac!==true){
_4aa+="="+_4ac;
}
}
document.cookie=_4aa;
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
},"dojo/cache":function(){
define(["./_base/kernel","./text"],function(dojo){
return dojo.cache;
});
},"curam/util/ui/refresh/TabRefreshController":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/debug","curam/util/ui/refresh/RefreshEvent"],function(_4ad,_4ae,_4af){
var _4b0=_4ad("curam.util.ui.refresh.TabRefreshController",null,{EVENT_REFRESH_MENU:"/curam/refresh/menu",EVENT_REFRESH_NAVIGATION:"/curam/refresh/navigation",EVENT_REFRESH_CONTEXT:"/curam/refresh/context",EVENT_REFRESH_MAIN:"/curam/refresh/main-content",_tabWidgetId:null,_configOnSubmit:null,_configOnLoad:null,_handler:null,_lastSubmitted:null,_currentlyRefreshing:null,_ignoreContextRefresh:true,_initialMenuAndNavRefreshDone:false,_nullController:null,constructor:function(_4b1,_4b2){
this._configOnSubmit={};
this._configOnLoad={};
if(!_4b2){
this._nullController=true;
return;
}
this._tabWidgetId=_4b1;
dojo.forEach(_4b2.config,dojo.hitch(this,function(item){
this._configOnSubmit[item.page]=item.onsubmit;
this._configOnLoad[item.page]=item.onload;
}));
_4ae.register("curam/util/ui/refresh/TabRefreshController",this);
},pageSubmitted:function(_4b3,_4b4){
new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONSUBMIT,_4b4);
_4af.log("curam.util.ui.refresh.TabRefreshController: "+_4af.getProperty("curam.util.ui.refresh.TabRefreshController.submit",[_4b3,_4b4]));
dojo.publish("curam/form/submit",[_4b3]);
if(this._configOnSubmit[_4b3]){
this._lastSubmitted=_4b3;
_4af.log("curam.util.ui.refresh.TabRefreshController: "+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+"submit.notify"));
}
},pageLoaded:function(_4b5,_4b6){
var _4b7=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,_4b6);
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController.load",[_4b5,_4b6]));
if(this._currentlyRefreshing&&this._currentlyRefreshing.equals(_4b7)){
this._currentlyRefreshing=null;
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+"refresh"));
return;
}
var _4b8={};
if(_4b6==_4b7.SOURCE_CONTEXT_MAIN&&this._configOnLoad[_4b5]){
_4b8=this._configOnLoad[_4b5];
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".load.config"));
}
if(this._lastSubmitted){
var cfg=this._configOnSubmit[this._lastSubmitted];
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".submit.config",[this._lastSubmitted]));
_4b8.details=_4b8.details||cfg.details;
_4b8.menubar=_4b8.menubar||cfg.menubar;
_4b8.navigation=_4b8.navigation||cfg.navigation;
_4b8.mainContent=_4b8.mainContent||cfg.mainContent;
this._lastSubmitted=null;
}
if(!this._nullController){
this._fireRefreshEvents(_4b8,this._ignoreContextRefresh,!this._initialMenuAndNavRefreshDone);
}
if(this._ignoreContextRefresh&&_4b6==_4b7.SOURCE_CONTEXT_MAIN){
this._ignoreContextRefresh=false;
}
if(!this._initialMenuAndNavRefreshDone){
this._initialMenuAndNavRefreshDone=true;
}
},_fireRefreshEvents:function(cfg,_4b9,_4ba){
var _4bb=[];
if(cfg.details){
if(_4b9){
curam.debug.log("curam.util.ui.refresh.TabRefreshController: ignoring the first CONTEXT refresh request");
}else{
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.context"));
_4bb.push(this.EVENT_REFRESH_CONTEXT+"/"+this._tabWidgetId);
}
}else{
if(!_4b9){
dojo.publish("curam/tab/contextRefresh");
}
}
if(cfg.menubar||_4ba){
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.menu"));
_4bb.push(this.EVENT_REFRESH_MENU+"/"+this._tabWidgetId);
}
if(cfg.navigation||_4ba){
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.nav"));
_4bb.push(this.EVENT_REFRESH_NAVIGATION+"/"+this._tabWidgetId);
}
if(cfg.mainContent){
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.main"));
this._currentlyRefreshing=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN,null);
_4bb.push(this.EVENT_REFRESH_MAIN+"/"+this._tabWidgetId);
}
if(_4bb.length>0){
_4af.log("curam.util.ui.refresh.TabRefreshController:"+_4af.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.log",[_4bb.length,_4bb]));
this._handler(_4bb);
}
},setRefreshHandler:function(_4bc){
this._handler=_4bc;
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
return _4b0;
});
},"dijit/_base/popup":function(){
define(["dojo/dom-class","dojo/_base/window","../popup","../BackgroundIframe"],function(_4bd,win,_4be){
var _4bf=_4be._createWrapper;
_4be._createWrapper=function(_4c0){
if(!_4c0.declaredClass){
_4c0={_popupWrapper:(_4c0.parentNode&&_4bd.contains(_4c0.parentNode,"dijitPopup"))?_4c0.parentNode:null,domNode:_4c0,destroy:function(){
},ownerDocument:_4c0.ownerDocument,ownerDocumentBody:win.body(_4c0.ownerDocument)};
}
return _4bf.call(this,_4c0);
};
var _4c1=_4be.open;
_4be.open=function(args){
if(args.orient&&typeof args.orient!="string"&&!("length" in args.orient)){
var ary=[];
for(var key in args.orient){
ary.push({aroundCorner:key,corner:args.orient[key]});
}
args.orient=ary;
}
return _4c1.call(this,args);
};
return _4be;
});
},"dojo/promise/all":function(){
define(["../_base/array","../Deferred","../when"],function(_4c2,_4c3,when){
"use strict";
var some=_4c2.some;
return function all(_4c4){
var _4c5,_4c2;
if(_4c4 instanceof Array){
_4c2=_4c4;
}else{
if(_4c4&&typeof _4c4==="object"){
_4c5=_4c4;
}
}
var _4c6;
var _4c7=[];
if(_4c5){
_4c2=[];
for(var key in _4c5){
if(Object.hasOwnProperty.call(_4c5,key)){
_4c7.push(key);
_4c2.push(_4c5[key]);
}
}
_4c6={};
}else{
if(_4c2){
_4c6=[];
}
}
if(!_4c2||!_4c2.length){
return new _4c3().resolve(_4c6);
}
var _4c8=new _4c3();
_4c8.promise.always(function(){
_4c6=_4c7=null;
});
var _4c9=_4c2.length;
some(_4c2,function(_4ca,_4cb){
if(!_4c5){
_4c7.push(_4cb);
}
when(_4ca,function(_4cc){
if(!_4c8.isFulfilled()){
_4c6[_4c7[_4cb]]=_4cc;
if(--_4c9===0){
_4c8.resolve(_4c6);
}
}
},_4c8.reject);
return _4c8.isFulfilled();
});
return _4c8.promise;
};
});
},"dojo/_base/url":function(){
define(["./kernel"],function(dojo){
var ore=new RegExp("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$"),ire=new RegExp("^((([^\\[:]+):)?([^@]+)@)?(\\[([^\\]]+)\\]|([^\\[:]*))(:([0-9]+))?$"),_4cd=function(){
var n=null,_4ce=arguments,uri=[_4ce[0]];
for(var i=1;i<_4ce.length;i++){
if(!_4ce[i]){
continue;
}
var _4cf=new _4cd(_4ce[i]+""),_4d0=new _4cd(uri[0]+"");
if(_4cf.path==""&&!_4cf.scheme&&!_4cf.authority&&!_4cf.query){
if(_4cf.fragment!=n){
_4d0.fragment=_4cf.fragment;
}
_4cf=_4d0;
}else{
if(!_4cf.scheme){
_4cf.scheme=_4d0.scheme;
if(!_4cf.authority){
_4cf.authority=_4d0.authority;
if(_4cf.path.charAt(0)!="/"){
var path=_4d0.path.substring(0,_4d0.path.lastIndexOf("/")+1)+_4cf.path;
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
_4cf.path=segs.join("/");
}
}
}
}
uri=[];
if(_4cf.scheme){
uri.push(_4cf.scheme,":");
}
if(_4cf.authority){
uri.push("//",_4cf.authority);
}
uri.push(_4cf.path);
if(_4cf.query){
uri.push("?",_4cf.query);
}
if(_4cf.fragment){
uri.push("#",_4cf.fragment);
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
_4cd.prototype.toString=function(){
return this.uri;
};
return dojo._Url=_4cd;
});
},"dojo/text":function(){
define(["./_base/kernel","require","./has","./request"],function(dojo,_4d1,has,_4d2){
var _4d3;
if(1){
_4d3=function(url,sync,load){
_4d2(url,{sync:!!sync,headers:{"X-Requested-With":null}}).then(load);
};
}else{
if(_4d1.getText){
_4d3=_4d1.getText;
}else{
console.error("dojo/text plugin failed to load because loader does not support getText");
}
}
var _4d4={},_4d5=function(text){
if(text){
text=text.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _4d6=text.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_4d6){
text=_4d6[1];
}
}else{
text="";
}
return text;
},_4d7={},_4d8={};
dojo.cache=function(_4d9,url,_4da){
var key;
if(typeof _4d9=="string"){
if(/\//.test(_4d9)){
key=_4d9;
_4da=url;
}else{
key=_4d1.toUrl(_4d9.replace(/\./g,"/")+(url?("/"+url):""));
}
}else{
key=_4d9+"";
_4da=url;
}
var val=(_4da!=undefined&&typeof _4da!="string")?_4da.value:_4da,_4db=_4da&&_4da.sanitize;
if(typeof val=="string"){
_4d4[key]=val;
return _4db?_4d5(val):val;
}else{
if(val===null){
delete _4d4[key];
return null;
}else{
if(!(key in _4d4)){
_4d3(key,true,function(text){
_4d4[key]=text;
});
}
return _4db?_4d5(_4d4[key]):_4d4[key];
}
}
};
return {dynamic:true,normalize:function(id,_4dc){
var _4dd=id.split("!"),url=_4dd[0];
return (/^\./.test(url)?_4dc(url):url)+(_4dd[1]?"!"+_4dd[1]:"");
},load:function(id,_4de,load){
var _4df=id.split("!"),_4e0=_4df.length>1,_4e1=_4df[0],url=_4de.toUrl(_4df[0]),_4e2="url:"+url,text=_4d7,_4e3=function(text){
load(_4e0?_4d5(text):text);
};
if(_4e1 in _4d4){
text=_4d4[_4e1];
}else{
if(_4de.cache&&_4e2 in _4de.cache){
text=_4de.cache[_4e2];
}else{
if(url in _4d4){
text=_4d4[url];
}
}
}
if(text===_4d7){
if(_4d8[url]){
_4d8[url].push(_4e3);
}else{
var _4e4=_4d8[url]=[_4e3];
_4d3(url,!_4de.async,function(text){
_4d4[_4e1]=_4d4[url]=text;
for(var i=0;i<_4e4.length;){
_4e4[i++](text);
}
delete _4d8[url];
});
}
}else{
_4e3(text);
}
}};
});
},"dijit/layout/LayoutContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/_base/lang","../_WidgetBase","./_LayoutWidget","./utils"],function(_4e5,_4e6,_4e7,_4e8,lang,_4e9,_4ea,_4eb){
var _4ec=_4e6("dijit.layout.LayoutContainer",_4ea,{design:"headline",baseClass:"dijitLayoutContainer",startup:function(){
if(this._started){
return;
}
_4e5.forEach(this.getChildren(),this._setupChild,this);
this.inherited(arguments);
},_setupChild:function(_4ed){
this.inherited(arguments);
var _4ee=_4ed.region;
if(_4ee){
_4e7.add(_4ed.domNode,this.baseClass+"Pane");
}
},_getOrderedChildren:function(){
var _4ef=_4e5.map(this.getChildren(),function(_4f0,idx){
return {pane:_4f0,weight:[_4f0.region=="center"?Infinity:0,_4f0.layoutPriority,(this.design=="sidebar"?1:-1)*(/top|bottom/.test(_4f0.region)?1:-1),idx]};
},this);
_4ef.sort(function(a,b){
var aw=a.weight,bw=b.weight;
for(var i=0;i<aw.length;i++){
if(aw[i]!=bw[i]){
return aw[i]-bw[i];
}
}
return 0;
});
return _4e5.map(_4ef,function(w){
return w.pane;
});
},layout:function(){
_4eb.layoutChildren(this.domNode,this._contentBox,this._getOrderedChildren());
},addChild:function(_4f1,_4f2){
this.inherited(arguments);
if(this._started){
this.layout();
}
},removeChild:function(_4f3){
this.inherited(arguments);
if(this._started){
this.layout();
}
_4e7.remove(_4f3.domNode,this.baseClass+"Pane");
_4e8.set(_4f3.domNode,{top:"auto",bottom:"auto",left:"auto",right:"auto",position:"static"});
_4e8.set(_4f3.domNode,/top|bottom/.test(_4f3.region)?"width":"height","auto");
}});
_4ec.ChildWidgetProperties={region:"",layoutAlign:"",layoutPriority:0};
lang.extend(_4e9,_4ec.ChildWidgetProperties);
return _4ec;
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./domReady","./sniff","./_base/window"],function(_4f4,lang,_4f5,has,_4f6){
var html=_4f6.doc.documentElement,ie=has("ie"),_4f7=has("trident"),_4f8=has("opera"),maj=Math.floor,ff=has("ff"),_4f9=_4f4.boxModel.replace(/-/,""),_4fa={"dj_quirks":has("quirks"),"dj_opera":_4f8,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_edge":has("edge"),"dj_gecko":has("mozilla"),"dj_ios":has("ios"),"dj_android":has("android")};
if(ie){
_4fa["dj_ie"]=true;
_4fa["dj_ie"+maj(ie)]=true;
_4fa["dj_iequirks"]=has("quirks");
}
if(_4f7){
_4fa["dj_trident"]=true;
_4fa["dj_trident"+maj(_4f7)]=true;
}
if(ff){
_4fa["dj_ff"+maj(ff)]=true;
}
_4fa["dj_"+_4f9]=true;
var _4fb="";
for(var clz in _4fa){
if(_4fa[clz]){
_4fb+=clz+" ";
}
}
html.className=lang.trim(html.className+" "+_4fb);
_4f5(function(){
if(!_4f4.isBodyLtr()){
var _4fc="dj_rtl dijitRtl "+_4fb.replace(/ /g,"-rtl ");
html.className=lang.trim(html.className+" "+_4fc+"dj_rtl dijitRtl "+_4fb.replace(/ /g,"-rtl "));
}
});
return has;
});
},"dijit/Tooltip":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/fx","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","./_base/manager","./place","./_Widget","./_TemplatedMixin","./BackgroundIframe","dojo/text!./templates/Tooltip.html","./main"],function(_4fd,_4fe,fx,dom,_4ff,_500,_501,lang,_502,on,has,_503,_504,_505,_506,_507,_508,_509){
var _50a=_4fe("dijit._MasterTooltip",[_505,_506],{duration:_503.defaultDuration,templateString:_508,postCreate:function(){
this.ownerDocumentBody.appendChild(this.domNode);
this.bgIframe=new _507(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
},show:function(_50b,_50c,_50d,rtl,_50e,_50f,_510){
if(this.aroundNode&&this.aroundNode===_50c&&this.containerNode.innerHTML==_50b){
return;
}
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.containerNode.innerHTML=_50b;
if(_50e){
this.set("textDir",_50e);
}
this.containerNode.align=rtl?"right":"left";
var pos=_504.around(this.domNode,_50c,_50d&&_50d.length?_50d:_511.defaultPosition,!rtl,lang.hitch(this,"orient"));
var _512=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_512.y+((_512.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_512.x+((_512.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}else{
this.connectorNode.style.left="";
this.connectorNode.style.top="";
}
}
_501.set(this.domNode,"opacity",0);
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_50c;
this.onMouseEnter=_50f||noop;
this.onMouseLeave=_510||noop;
},orient:function(node,_513,_514,_515,_516){
this.connectorNode.style.top="";
var _517=_515.h,_518=_515.w;
node.className="dijitTooltip "+{"MR-ML":"dijitTooltipRight","ML-MR":"dijitTooltipLeft","TM-BM":"dijitTooltipAbove","BM-TM":"dijitTooltipBelow","BL-TL":"dijitTooltipBelow dijitTooltipABLeft","TL-BL":"dijitTooltipAbove dijitTooltipABLeft","BR-TR":"dijitTooltipBelow dijitTooltipABRight","TR-BR":"dijitTooltipAbove dijitTooltipABRight","BR-BL":"dijitTooltipRight","BL-BR":"dijitTooltipLeft"}[_513+"-"+_514];
this.domNode.style.width="auto";
var size=_500.position(this.domNode);
if(has("ie")||has("trident")){
size.w+=2;
}
var _519=Math.min((Math.max(_518,1)),size.w);
_500.setMarginBox(this.domNode,{w:_519});
if(_514.charAt(0)=="B"&&_513.charAt(0)=="B"){
var bb=_500.position(node);
var _51a=this.connectorNode.offsetHeight;
if(bb.h>_517){
var _51b=_517-((_516.h+_51a)>>1);
this.connectorNode.style.top=_51b+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(_516.h/2-_51a/2,0),bb.h-_51a)+"px";
this.connectorNode.style.top="";
}
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
return Math.max(0,size.w-_518);
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
},hide:function(_51c){
if(this._onDeck&&this._onDeck[1]==_51c){
this._onDeck=null;
}else{
if(this.aroundNode===_51c){
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
_50a.extend({_setAutoTextDir:function(node){
this.applyTextDir(node);
_4fd.forEach(node.children,function(_51d){
this._setAutoTextDir(_51d);
},this);
},_setTextDirAttr:function(_51e){
this._set("textDir",_51e);
if(_51e=="auto"){
this._setAutoTextDir(this.containerNode);
}else{
this.containerNode.dir=this.textDir;
}
}});
}
_509.showTooltip=function(_51f,_520,_521,rtl,_522,_523,_524){
if(_521){
_521=_4fd.map(_521,function(val){
return {after:"after-centered",before:"before-centered"}[val]||val;
});
}
if(!_511._masterTT){
_509._masterTT=_511._masterTT=new _50a();
}
return _511._masterTT.show(_51f,_520,_521,rtl,_522,_523,_524);
};
_509.hideTooltip=function(_525){
return _511._masterTT&&_511._masterTT.hide(_525);
};
var _526="DORMANT",_527="SHOW TIMER",_528="SHOWING",_529="HIDE TIMER";
function noop(){
};
var _511=_4fe("dijit.Tooltip",_505,{label:"",showDelay:400,hideDelay:400,connectId:[],position:[],selector:"",_setConnectIdAttr:function(_52a){
_4fd.forEach(this._connections||[],function(_52b){
_4fd.forEach(_52b,function(_52c){
_52c.remove();
});
},this);
this._connectIds=_4fd.filter(lang.isArrayLike(_52a)?_52a:(_52a?[_52a]:[]),function(id){
return dom.byId(id,this.ownerDocument);
},this);
this._connections=_4fd.map(this._connectIds,function(id){
var node=dom.byId(id,this.ownerDocument),_52d=this.selector,_52e=_52d?function(_52f){
return on.selector(_52d,_52f);
}:function(_530){
return _530;
},self=this;
return [on(node,_52e(_502.enter),function(){
self._onHover(this);
}),on(node,_52e("focusin"),function(){
self._onHover(this);
}),on(node,_52e(_502.leave),lang.hitch(self,"_onUnHover")),on(node,_52e("focusout"),lang.hitch(self,"set","state",_526))];
},this);
this._set("connectId",_52a);
},addTarget:function(node){
var id=node.id||node;
if(_4fd.indexOf(this._connectIds,id)==-1){
this.set("connectId",this._connectIds.concat(id));
}
},removeTarget:function(node){
var id=node.id||node,idx=_4fd.indexOf(this._connectIds,id);
if(idx>=0){
this._connectIds.splice(idx,1);
this.set("connectId",this._connectIds);
}
},buildRendering:function(){
this.inherited(arguments);
_4ff.add(this.domNode,"dijitTooltipData");
},startup:function(){
this.inherited(arguments);
var ids=this.connectId;
_4fd.forEach(lang.isArrayLike(ids)?ids:[ids],this.addTarget,this);
},getContent:function(node){
return this.label||this.domNode.innerHTML;
},state:_526,_setStateAttr:function(val){
if(this.state==val||(val==_527&&this.state==_528)||(val==_529&&this.state==_526)){
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
case _526:
if(this._connectNode){
_511.hide(this._connectNode);
delete this._connectNode;
this.onHide();
}
break;
case _527:
if(this.state!=_528){
this._showTimer=this.defer(function(){
this.set("state",_528);
},this.showDelay);
}
break;
case _528:
var _531=this.getContent(this._connectNode);
if(!_531){
this.set("state",_526);
return;
}
_511.show(_531,this._connectNode,this.position,!this.isLeftToRight(),this.textDir,lang.hitch(this,"set","state",_528),lang.hitch(this,"set","state",_529));
this.onShow(this._connectNode,this.position);
break;
case _529:
this._hideTimer=this.defer(function(){
this.set("state",_526);
},this.hideDelay);
break;
}
this._set("state",val);
},_onHover:function(_532){
if(this._connectNode&&_532!=this._connectNode){
this.set("state",_526);
}
this._connectNode=_532;
this.set("state",_527);
},_onUnHover:function(_533){
this.set("state",_529);
},open:function(_534){
this.set("state",_526);
this._connectNode=_534;
this.set("state",_528);
},close:function(){
this.set("state",_526);
},onShow:function(){
},onHide:function(){
},destroy:function(){
this.set("state",_526);
_4fd.forEach(this._connections||[],function(_535){
_4fd.forEach(_535,function(_536){
_536.remove();
});
},this);
this.inherited(arguments);
}});
_511._MasterTooltip=_50a;
_511.show=_509.showTooltip;
_511.hide=_509.hideTooltip;
_511.defaultPosition=["after-centered","before-centered"];
return _511;
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(_537,lang){
var _538=/[&<>'"\/]/g;
var _539={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;","/":"&#x2F;"};
var _53a={};
lang.setObject("dojo.string",_53a);
_53a.escape=function(str){
if(!str){
return "";
}
return str.replace(_538,function(c){
return _539[c];
});
};
_53a.rep=function(str,num){
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
_53a.pad=function(text,size,ch,end){
if(!ch){
ch="0";
}
var out=String(text),pad=_53a.rep(ch,Math.ceil((size-out.length)/ch.length));
return end?out+pad:pad+out;
};
_53a.substitute=function(_53b,map,_53c,_53d){
_53d=_53d||_537.global;
_53c=_53c?lang.hitch(_53d,_53c):function(v){
return v;
};
return _53b.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_53e,key,_53f){
var _540=lang.getObject(key,false,map);
if(_53f){
_540=lang.getObject(_53f,false,_53d).call(_53d,_540,key);
}
return _53c(_540,key).toString();
});
};
_53a.trim=String.prototype.trim?lang.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return _53a;
});
},"curam/util/ui/refresh/RefreshEvent":function(){
define(["dojo/_base/declare"],function(_541){
var _542=_541("curam.util.ui.refresh.RefreshEvent",null,{TYPE_ONLOAD:"onload",TYPE_ONSUBMIT:"onsubmit",SOURCE_CONTEXT_MAIN:"main-content",SOURCE_CONTEXT_DIALOG:"dialog",SOURCE_CONTEXT_INLINE:"inline",_type:null,_context:null,constructor:function(type,_543){
if(!type||!_543){
throw "Required parameters missing.";
}
if(!(type==this.TYPE_ONLOAD||type==this.TYPE_ONSUBMIT)){
throw "Unknown type: "+type;
}
if(!(_543==this.SOURCE_CONTEXT_DIALOG||_543==this.SOURCE_CONTEXT_INLINE||_543==this.SOURCE_CONTEXT_MAIN)){
throw "Unknown context: "+_543;
}
this._type=type;
this._context=_543;
},equals:function(_544){
if(typeof _544!="object"){
return false;
}
if(_544.declaredClass!=this.declaredClass){
return false;
}
return this._type===_544._type&&this._context===_544._context;
}});
return _542;
});
},"idx/widget/MenuHeading":function(){
define(["dojo/_base/declare","dojo/_base/lang","dijit/MenuSeparator","dojo/text!./templates/MenuHeading.html"],function(_545,lang,_546,_547){
var _548=lang.getObject("idx.oneui",true);
return _548.MenuHeading=_545("idx.widget.MenuHeading",[_546],{label:"",_setLabelAttr:{node:"containerNode",type:"innerHTML"},templateString:_547,baseClass:"oneuiMenuHeading"});
});
},"dijit/dijit":function(){
define(["./main","./_base","dojo/parser","./_Widget","./_TemplatedMixin","./_Container","./layout/_LayoutWidget","./form/_FormWidget","./form/_FormValueWidget"],function(_549){
return _549;
});
},"dijit/form/DropDownButton":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/query","../registry","../popup","./Button","../_Container","../_HasDropDown","dojo/text!./templates/DropDownButton.html","../a11yclick"],function(_54a,lang,_54b,_54c,_54d,_54e,_54f,_550,_551){
return _54a("dijit.form.DropDownButton",[_54e,_54f,_550],{baseClass:"dijitDropDownButton",templateString:_551,_fillContent:function(){
if(this.srcNodeRef){
var _552=_54b("*",this.srcNodeRef);
this.inherited(arguments,[_552[0]]);
this.dropDownContainer=this.srcNodeRef;
}
},startup:function(){
if(this._started){
return;
}
if(!this.dropDown&&this.dropDownContainer){
var _553=_54b("[widgetId]",this.dropDownContainer)[0];
if(_553){
this.dropDown=_54c.byNode(_553);
}
delete this.dropDownContainer;
}
if(this.dropDown){
_54d.hide(this.dropDown);
}
this.inherited(arguments);
},isLoaded:function(){
var _554=this.dropDown;
return (!!_554&&(!_554.href||_554.isLoaded));
},loadDropDown:function(_555){
var _556=this.dropDown;
var _557=_556.on("load",lang.hitch(this,function(){
_557.remove();
_555();
}));
_556.refresh();
},isFocusable:function(){
return this.inherited(arguments)&&!this._mouseDown;
}});
});
},"dojox/layout/ContentPane":function(){
define(["dojo/_base/lang","dojo/_base/xhr","dijit/layout/ContentPane","dojox/html/_base","dojo/_base/declare"],function(lang,_558,_559,_55a,_55b){
return _55b("dojox.layout.ContentPane",_559,{adjustPaths:false,cleanContent:false,renderStyles:false,executeScripts:true,scriptHasHooks:false,ioMethod:_558.get,ioArgs:{},onExecError:function(e){
},_setContent:function(cont){
var _55c=this._contentSetter;
if(!(_55c&&_55c instanceof _55a._ContentSetter)){
_55c=this._contentSetter=new _55a._ContentSetter({node:this.containerNode,_onError:lang.hitch(this,this._onError),onContentError:lang.hitch(this,function(e){
var _55d=this.onContentError(e);
try{
this.containerNode.innerHTML=_55d;
}
catch(e){
console.error("Fatal "+this.id+" could not change content due to "+e.message,e);
}
})});
}
this._contentSetterParams={adjustPaths:Boolean(this.adjustPaths&&(this.href||this.referencePath)),referencePath:this.href||this.referencePath,renderStyles:this.renderStyles,executeScripts:this.executeScripts,scriptHasHooks:this.scriptHasHooks,scriptHookReplacement:"dijit.byId('"+this.id+"')"};
return this.inherited("_setContent",arguments);
},destroy:function(){
var _55e=this._contentSetter;
if(_55e){
_55e.tearDown();
}
this.inherited(arguments);
}});
});
},"dijit/form/_FormValueMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","./_FormWidgetMixin"],function(_55f,_560,keys,lang,on,_561){
return _55f("dijit.form._FormValueMixin",_561,{readOnly:false,_setReadOnlyAttr:function(_562){
_560.set(this.focusNode,"readOnly",_562);
this._set("readOnly",_562);
},postCreate:function(){
this.inherited(arguments);
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.value;
}
},_setValueAttr:function(_563,_564){
this._handleOnChange(_563,_564);
},_handleOnChange:function(_565,_566){
this._set("value",_565);
this.inherited(arguments);
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
}});
});
},"dijit/form/_FormWidgetMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","dojo/window","../a11y"],function(_567,_568,_569,_56a,lang,_56b,on,has,_56c,a11y){
return _568("dijit.form._FormWidgetMixin",null,{name:"",alt:"",value:"",type:"text","aria-label":"focusNode",tabIndex:"0",_setTabIndexAttr:"focusNode",disabled:false,intermediateChanges:false,scrollOnFocus:true,_setIdAttr:"focusNode",_setDisabledAttr:function(_56d){
this._set("disabled",_56d);
if(/^(button|input|select|textarea|optgroup|option|fieldset)$/i.test(this.focusNode.tagName)){
_569.set(this.focusNode,"disabled",_56d);
}else{
this.focusNode.setAttribute("aria-disabled",_56d?"true":"false");
}
if(this.valueNode){
_569.set(this.valueNode,"disabled",_56d);
}
if(_56d){
this._set("hovering",false);
this._set("active",false);
var _56e="tabIndex" in this.attributeMap?this.attributeMap.tabIndex:("_setTabIndexAttr" in this)?this._setTabIndexAttr:"focusNode";
_567.forEach(lang.isArray(_56e)?_56e:[_56e],function(_56f){
var node=this[_56f];
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
var _570=this.own(on(this.focusNode,"focus",function(){
_571.remove();
_570.remove();
}))[0];
var _572=has("pointer-events")?"pointerup":has("MSPointer")?"MSPointerUp":has("touch-events")?"touchend, mouseup":"mouseup";
var _571=this.own(on(this.ownerDocumentBody,_572,lang.hitch(this,function(evt){
_571.remove();
_570.remove();
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
_56c.scrollIntoView(this.domNode);
});
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&this.focusNode&&(_56a.get(this.domNode,"display")!="none");
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
},_onChangeActive:false,_handleOnChange:function(_573,_574){
if(this._lastValueReported==undefined&&(_574===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_573;
}
this._pendingOnChange=this._pendingOnChange||(typeof _573!=typeof this._lastValueReported)||(this.compare(_573,this._lastValueReported)!=0);
if((this.intermediateChanges||_574||_574===undefined)&&this._pendingOnChange){
this._lastValueReported=_573;
this._pendingOnChange=false;
if(this._onChangeActive){
if(this._onChangeHandle){
this._onChangeHandle.remove();
}
this._onChangeHandle=this.defer(function(){
this._onChangeHandle=null;
this.onChange(_573);
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
define(["dojo/keys","dojo/mouse","dojo/on","dojo/touch"],function(keys,_575,on,_576){
function _577(e){
if((e.keyCode===keys.ENTER||e.keyCode===keys.SPACE)&&!/input|button|textarea/i.test(e.target.nodeName)){
for(var node=e.target;node;node=node.parentNode){
if(node.dojoClick){
return true;
}
}
}
};
var _578;
on(document,"keydown",function(e){
if(_577(e)){
_578=e.target;
e.preventDefault();
}else{
_578=null;
}
});
on(document,"keyup",function(e){
if(_577(e)&&e.target==_578){
_578=null;
on.emit(e.target,"click",{cancelable:true,bubbles:true,ctrlKey:e.ctrlKey,shiftKey:e.shiftKey,metaKey:e.metaKey,altKey:e.altKey,_origType:e.type});
}
});
var _579=function(node,_57a){
node.dojoClick=true;
return on(node,"click",_57a);
};
_579.click=_579;
_579.press=function(node,_57b){
var _57c=on(node,_576.press,function(evt){
if(evt.type=="mousedown"&&!_575.isLeft(evt)){
return;
}
_57b(evt);
}),_57d=on(node,"keydown",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_57b(evt);
}
});
return {remove:function(){
_57c.remove();
_57d.remove();
}};
};
_579.release=function(node,_57e){
var _57f=on(node,_576.release,function(evt){
if(evt.type=="mouseup"&&!_575.isLeft(evt)){
return;
}
_57e(evt);
}),_580=on(node,"keyup",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_57e(evt);
}
});
return {remove:function(){
_57f.remove();
_580.remove();
}};
};
_579.move=_576.move;
return _579;
});
},"idx/widget/MenuBar":function(){
define(["dojo/_base/declare","dojo/_base/lang","dijit/MenuBar","./_MenuOpenOnHoverMixin","idx/widgets"],function(_581,lang,_582,_583){
var _584=lang.getObject("idx.oneui",true);
return _584.MenuBar=_581("idx.widget.MenuBar",[_582,_583],{idxBaseClass:"idxMenuBar",openOnHover:true});
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_585,_586,_587){
return _587("dijit.Destroyable",null,{destroy:function(_588){
this._destroyed=true;
},own:function(){
var _589=["destroyRecursive","destroy","remove"];
_585.forEach(arguments,function(_58a){
var _58b;
var odh=_586.before(this,"destroy",function(_58c){
_58a[_58b](_58c);
});
var hdhs=[];
function _58d(){
odh.remove();
_585.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_58a.then){
_58b="cancel";
_58a.then(_58d,_58d);
}else{
_585.forEach(_589,function(_58e){
if(typeof _58a[_58e]==="function"){
if(!_58b){
_58b=_58e;
}
hdhs.push(_586.after(_58a,_58e,_58d,true));
}
});
}
},this);
return arguments;
}});
});
},"curam/inspection/Layer":function(){
define(["curam/define"],function(def){
curam.define.singleton("curam.inspection.Layer",{register:function(_58f,inst){
require(["curam/util"]);
var tWin=curam.util.getTopmostWindow();
return tWin.inspectionManager?tWin.inspectionManager.observe(_58f,inst):null;
}});
var ref=curam.inspection.Layer;
require(["curam/util"]);
ref.tWin=curam.util.getTopmostWindow();
var _590=ref.tWin.inspectionManager?ref.tWin.inspectionManager.getDirects():[];
if(_590.length>0){
require(_590);
}
return ref;
});
},"idx/widget/_MenuOpenOnHoverMixin":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/_base/lang","dojo/_base/window","dojo/dom","dojo/dom-attr","dojo/on","dojo/keys","dijit/Menu"],function(_591,has,lang,win,dom,_592,on,keys,Menu){
var dojo={},_593={};
return _591("idx.widget._MenuOpenOnHoverMixin",null,{openOnHover:false,_isActuallyActive:false,_setOpenOnHoverAttr:function(_594){
this.openOnHover=_594;
if(_594){
this._forceActive();
}else{
this._disableActive();
}
},_forceActive:function(){
this.set("activated",this._isActuallyActive=true);
},_disableActive:function(){
this.set("activated",this._isActuallyActive=false);
},onItemHover:function(item){
if(has("ie")&&(has("ie")>=11)){
}else{
this.focusChild(item);
}
this.inherited(arguments);
},_cleanUp:function(){
this.inherited(arguments);
this.set("activated",this._isActuallyActive);
},bindDomNode:function(node){
var _595=this.getInherited(arguments);
if(!_595){
console.log("Attempt to call bindDomNode() on a widget that does not "+"appear to be a dijit/Menu");
return;
}
node=dom.byId(node,this.ownerDocument);
var cn;
if(node.tagName.toLowerCase()=="iframe"){
var _596=node,_597=this._iframeContentWindow(_596);
cn=win.body(_597.document);
}else{
cn=(node==win.body(this.ownerDocument)?this.ownerDocument.documentElement:node);
}
var _598={node:node,iframe:_596};
_592.set(node,"_dijitMenu"+this.id,this._bindings.push(_598));
var _599=lang.hitch(this,function(cn){
var _59a=this.selector,_59b=_59a?function(_59c){
return on.selector(_59a,_59c);
}:function(_59d){
return _59d;
},self=this;
result=[on(cn,_59b(this.leftClickToOpen?"click":"contextmenu"),function(evt){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_596,{x:evt.pageX,y:evt.pageY});
}),on(cn,_59b("keydown"),function(evt){
if(evt.shiftKey&&evt.keyCode==keys.F10){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_596);
}
})];
result.push(on(cn,_59b("mouseover"),function(evt){
if(self.openOnHover){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_596,{x:evt.pageX,y:evt.pageY});
}
}));
return result;
});
_598.connects=cn?_599(cn):[];
if(_596){
_598.onloadHandler=lang.hitch(this,function(){
var _59e=this._iframeContentWindow(_596),cn=win.body(_59e.document);
_598.connects=_599(cn);
});
if(_596.addEventListener){
_596.addEventListener("load",_598.onloadHandler,false);
}else{
_596.attachEvent("onload",_598.onloadHandler);
}
}
}});
});
},"dijit/layout/_ContentPaneResizeMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang","dojo/query","../registry","../Viewport","./utils"],function(_59f,_5a0,_5a1,_5a2,_5a3,lang,_5a4,_5a5,_5a6,_5a7){
return _5a0("dijit.layout._ContentPaneResizeMixin",null,{doLayout:true,isLayoutContainer:true,startup:function(){
if(this._started){
return;
}
var _5a8=this.getParent();
this._childOfLayoutWidget=_5a8&&_5a8.isLayoutContainer;
this._needLayout=!this._childOfLayoutWidget;
this.inherited(arguments);
if(this._isShown()){
this._onShow();
}
if(!this._childOfLayoutWidget){
this.own(_5a6.on("resize",lang.hitch(this,"resize")));
}
},_checkIfSingleChild:function(){
if(!this.doLayout){
return;
}
var _5a9=[],_5aa=false;
_5a4("> *",this.containerNode).some(function(node){
var _5ab=_5a5.byNode(node);
if(_5ab&&_5ab.resize){
_5a9.push(_5ab);
}else{
if(!/script|link|style/i.test(node.nodeName)&&node.offsetHeight){
_5aa=true;
}
}
});
this._singleChild=_5a9.length==1&&!_5aa?_5a9[0]:null;
_5a1.toggle(this.containerNode,this.baseClass+"SingleChild",!!this._singleChild);
},resize:function(_5ac,_5ad){
this._resizeCalled=true;
this._scheduleLayout(_5ac,_5ad);
},_scheduleLayout:function(_5ae,_5af){
if(this._isShown()){
this._layout(_5ae,_5af);
}else{
this._needLayout=true;
this._changeSize=_5ae;
this._resultSize=_5af;
}
},_layout:function(_5b0,_5b1){
delete this._needLayout;
if(!this._wasShown&&this.open!==false){
this._onShow();
}
if(_5b0){
_5a2.setMarginBox(this.domNode,_5b0);
}
var cn=this.containerNode;
if(cn===this.domNode){
var mb=_5b1||{};
lang.mixin(mb,_5b0||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_5a2.getMarginBox(cn),mb);
}
this._contentBox=_5a7.marginBox2contentBox(cn,mb);
}else{
this._contentBox=_5a2.getContentBox(cn);
}
this._layoutChildren();
},_layoutChildren:function(){
this._checkIfSingleChild();
if(this._singleChild&&this._singleChild.resize){
var cb=this._contentBox||_5a2.getContentBox(this.containerNode);
this._singleChild.resize({w:cb.w,h:cb.h});
}else{
var _5b2=this.getChildren(),_5b3,i=0;
while(_5b3=_5b2[i++]){
if(_5b3.resize){
_5b3.resize();
}
}
}
},_isShown:function(){
if(this._childOfLayoutWidget){
if(this._resizeCalled&&"open" in this){
return this.open;
}
return this._resizeCalled;
}else{
if("open" in this){
return this.open;
}else{
var node=this.domNode,_5b4=this.domNode.parentNode;
return (node.style.display!="none")&&(node.style.visibility!="hidden")&&!_5a1.contains(node,"dijitHidden")&&_5b4&&_5b4.style&&(_5b4.style.display!="none");
}
}
},_onShow:function(){
this._wasShown=true;
if(this._needLayout){
this._layout(this._changeSize,this._resultSize);
}
this.inherited(arguments);
}});
});
},"dijit/WidgetSet":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","./registry"],function(_5b5,_5b6,_5b7,_5b8){
var _5b9=_5b6("dijit.WidgetSet",null,{constructor:function(){
this._hash={};
this.length=0;
},add:function(_5ba){
if(this._hash[_5ba.id]){
throw new Error("Tried to register widget with id=="+_5ba.id+" but that id is already registered");
}
this._hash[_5ba.id]=_5ba;
this.length++;
},remove:function(id){
if(this._hash[id]){
delete this._hash[id];
this.length--;
}
},forEach:function(func,_5bb){
_5bb=_5bb||_5b7.global;
var i=0,id;
for(id in this._hash){
func.call(_5bb,this._hash[id],i++,this._hash);
}
return this;
},filter:function(_5bc,_5bd){
_5bd=_5bd||_5b7.global;
var res=new _5b9(),i=0,id;
for(id in this._hash){
var w=this._hash[id];
if(_5bc.call(_5bd,w,i++,this._hash)){
res.add(w);
}
}
return res;
},byId:function(id){
return this._hash[id];
},byClass:function(cls){
var res=new _5b9(),id,_5be;
for(id in this._hash){
_5be=this._hash[id];
if(_5be.declaredClass==cls){
res.add(_5be);
}
}
return res;
},toArray:function(){
var ar=[];
for(var id in this._hash){
ar.push(this._hash[id]);
}
return ar;
},map:function(func,_5bf){
return _5b5.map(this.toArray(),func,_5bf);
},every:function(func,_5c0){
_5c0=_5c0||_5b7.global;
var x=0,i;
for(i in this._hash){
if(!func.call(_5c0,this._hash[i],x++,this._hash)){
return false;
}
}
return true;
},some:function(func,_5c1){
_5c1=_5c1||_5b7.global;
var x=0,i;
for(i in this._hash){
if(func.call(_5c1,this._hash[i],x++,this._hash)){
return true;
}
}
return false;
}});
_5b5.forEach(["forEach","filter","byClass","map","every","some"],function(func){
_5b8[func]=_5b9.prototype[func];
});
return _5b9;
});
},"dojo/dnd/Moveable":function(){
define(["../_base/array","../_base/declare","../_base/lang","../dom","../dom-class","../Evented","../on","../topic","../touch","./common","./Mover","../_base/window"],function(_5c2,_5c3,lang,dom,_5c4,_5c5,on,_5c6,_5c7,dnd,_5c8,win){
var _5c9=_5c3("dojo.dnd.Moveable",[_5c5],{handle:"",delay:0,skip:false,constructor:function(node,_5ca){
this.node=dom.byId(node);
if(!_5ca){
_5ca={};
}
this.handle=_5ca.handle?dom.byId(_5ca.handle):null;
if(!this.handle){
this.handle=this.node;
}
this.delay=_5ca.delay>0?_5ca.delay:0;
this.skip=_5ca.skip;
this.mover=_5ca.mover?_5ca.mover:_5c8;
this.events=[on(this.handle,_5c7.press,lang.hitch(this,"onMouseDown")),on(this.handle,"dragstart",lang.hitch(this,"onSelectStart")),on(this.handle,"selectstart",lang.hitch(this,"onSelectStart"))];
},markupFactory:function(_5cb,node,Ctor){
return new Ctor(node,_5cb);
},destroy:function(){
_5c2.forEach(this.events,function(_5cc){
_5cc.remove();
});
this.events=this.node=this.handle=null;
},onMouseDown:function(e){
if(this.skip&&dnd.isFormElement(e)){
return;
}
if(this.delay){
this.events.push(on(this.handle,_5c7.move,lang.hitch(this,"onMouseMove")),on(this.handle,_5c7.release,lang.hitch(this,"onMouseUp")));
this._lastX=e.pageX;
this._lastY=e.pageY;
}else{
this.onDragDetected(e);
}
e.stopPropagation();
e.preventDefault();
},onMouseMove:function(e){
if(Math.abs(e.pageX-this._lastX)>this.delay||Math.abs(e.pageY-this._lastY)>this.delay){
this.onMouseUp(e);
this.onDragDetected(e);
}
e.stopPropagation();
e.preventDefault();
},onMouseUp:function(e){
for(var i=0;i<2;++i){
this.events.pop().remove();
}
e.stopPropagation();
e.preventDefault();
},onSelectStart:function(e){
if(!this.skip||!dnd.isFormElement(e)){
e.stopPropagation();
e.preventDefault();
}
},onDragDetected:function(e){
new this.mover(this.node,e,this);
},onMoveStart:function(_5cd){
_5c6.publish("/dnd/move/start",_5cd);
_5c4.add(win.body(),"dojoMove");
_5c4.add(this.node,"dojoMoveItem");
},onMoveStop:function(_5ce){
_5c6.publish("/dnd/move/stop",_5ce);
_5c4.remove(win.body(),"dojoMove");
_5c4.remove(this.node,"dojoMoveItem");
},onFirstMove:function(){
},onMove:function(_5cf,_5d0){
this.onMoving(_5cf,_5d0);
var s=_5cf.node.style;
s.left=_5d0.l+"px";
s.top=_5d0.t+"px";
this.onMoved(_5cf,_5d0);
},onMoving:function(){
},onMoved:function(){
}});
return _5c9;
});
},"dijit/TooltipDialog":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./focus","./layout/ContentPane","./_DialogMixin","./form/_FormMixin","./_TemplatedMixin","dojo/text!./templates/TooltipDialog.html","./main"],function(_5d1,_5d2,has,keys,lang,on,_5d3,_5d4,_5d5,_5d6,_5d7,_5d8,_5d9){
var _5da=_5d1("dijit.TooltipDialog",[_5d4,_5d7,_5d6,_5d5],{title:"",doLayout:false,autofocus:true,baseClass:"dijitTooltipDialog",_firstFocusItem:null,_lastFocusItem:null,templateString:_5d8,_setTitleAttr:"containerNode",postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"keydown",lang.hitch(this,"_onKey")));
},orient:function(node,_5db,_5dc){
var newC={"MR-ML":"dijitTooltipRight","ML-MR":"dijitTooltipLeft","TM-BM":"dijitTooltipAbove","BM-TM":"dijitTooltipBelow","BL-TL":"dijitTooltipBelow dijitTooltipABLeft","TL-BL":"dijitTooltipAbove dijitTooltipABLeft","BR-TR":"dijitTooltipBelow dijitTooltipABRight","TR-BR":"dijitTooltipAbove dijitTooltipABRight","BR-BL":"dijitTooltipRight","BL-BR":"dijitTooltipLeft","BR-TL":"dijitTooltipBelow dijitTooltipABLeft","BL-TR":"dijitTooltipBelow dijitTooltipABRight","TL-BR":"dijitTooltipAbove dijitTooltipABRight","TR-BL":"dijitTooltipAbove dijitTooltipABLeft"}[_5db+"-"+_5dc];
_5d2.replace(this.domNode,newC,this._currentOrientClass||"");
this._currentOrientClass=newC;
},focus:function(){
this._getFocusItems();
_5d3.focus(this._firstFocusItem);
},onOpen:function(pos){
this.orient(this.domNode,pos.aroundCorner,pos.corner);
var _5dd=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_5dd.y+((_5dd.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_5dd.x+((_5dd.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}
}
this._onShow();
},onClose:function(){
this.onHide();
},_onKey:function(evt){
if(evt.keyCode==keys.ESCAPE){
this.defer("onCancel");
evt.stopPropagation();
evt.preventDefault();
}else{
if(evt.keyCode==keys.TAB){
var node=evt.target;
this._getFocusItems();
if(this._firstFocusItem==this._lastFocusItem){
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._firstFocusItem&&evt.shiftKey){
_5d3.focus(this._lastFocusItem);
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._lastFocusItem&&!evt.shiftKey){
_5d3.focus(this._firstFocusItem);
evt.stopPropagation();
evt.preventDefault();
}else{
evt.stopPropagation();
}
}
}
}
}
}});
if(has("dojo-bidi")){
_5da.extend({_setTitleAttr:function(_5de){
this.containerNode.title=(this.textDir&&this.enforceTextDirWithUcc)?this.enforceTextDirWithUcc(null,_5de):_5de;
this._set("title",_5de);
},_setTextDirAttr:function(_5df){
if(!this._created||this.textDir!=_5df){
this._set("textDir",_5df);
if(this.textDir&&this.title){
this.containerNode.title=this.enforceTextDirWithUcc(null,this.title);
}
}
}});
}
return _5da;
});
},"dojox/collections/Dictionary":function(){
define(["dojo/_base/kernel","dojo/_base/array","./_base"],function(dojo,_5e0,dxc){
dxc.Dictionary=function(_5e1){
var _5e2={};
this.count=0;
var _5e3={};
this.add=function(k,v){
var b=(k in _5e2);
_5e2[k]=new dxc.DictionaryEntry(k,v);
if(!b){
this.count++;
}
};
this.clear=function(){
_5e2={};
this.count=0;
};
this.clone=function(){
return new dxc.Dictionary(this);
};
this.contains=this.containsKey=function(k){
if(_5e3[k]){
return false;
}
return (_5e2[k]!=null);
};
this.containsValue=function(v){
var e=this.getIterator();
while(e.get()){
if(e.element.value==v){
return true;
}
}
return false;
};
this.entry=function(k){
return _5e2[k];
};
this.forEach=function(fn,_5e4){
var a=[];
for(var p in _5e2){
if(!_5e3[p]){
a.push(_5e2[p]);
}
}
dojo.forEach(a,fn,_5e4);
};
this.getKeyList=function(){
return (this.getIterator()).map(function(_5e5){
return _5e5.key;
});
};
this.getValueList=function(){
return (this.getIterator()).map(function(_5e6){
return _5e6.value;
});
};
this.item=function(k){
if(k in _5e2){
return _5e2[k].valueOf();
}
return undefined;
};
this.getIterator=function(){
return new dxc.DictionaryIterator(_5e2);
};
this.remove=function(k){
if(k in _5e2&&!_5e3[k]){
delete _5e2[k];
this.count--;
return true;
}
return false;
};
if(_5e1){
var e=_5e1.getIterator();
while(e.get()){
this.add(e.element.key,e.element.value);
}
}
};
return dxc.Dictionary;
});
},"dijit/typematic":function(){
define(["dojo/_base/array","dojo/_base/connect","dojo/_base/lang","dojo/on","dojo/sniff","./main"],function(_5e7,_5e8,lang,on,has,_5e9){
var _5ea=(_5e9.typematic={_fireEventAndReload:function(){
this._timer=null;
this._callback(++this._count,this._node,this._evt);
this._currentTimeout=Math.max(this._currentTimeout<0?this._initialDelay:(this._subsequentDelay>1?this._subsequentDelay:Math.round(this._currentTimeout*this._subsequentDelay)),this._minDelay);
this._timer=setTimeout(lang.hitch(this,"_fireEventAndReload"),this._currentTimeout);
},trigger:function(evt,_5eb,node,_5ec,obj,_5ed,_5ee,_5ef){
if(obj!=this._obj){
this.stop();
this._initialDelay=_5ee||500;
this._subsequentDelay=_5ed||0.9;
this._minDelay=_5ef||10;
this._obj=obj;
this._node=node;
this._currentTimeout=-1;
this._count=-1;
this._callback=lang.hitch(_5eb,_5ec);
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
},addKeyListener:function(node,_5f0,_5f1,_5f2,_5f3,_5f4,_5f5){
var type="keyCode" in _5f0?"keydown":"charCode" in _5f0?"keypress":_5e8._keypress,attr="keyCode" in _5f0?"keyCode":"charCode" in _5f0?"charCode":"charOrCode";
var _5f6=[on(node,type,lang.hitch(this,function(evt){
if(evt[attr]==_5f0[attr]&&(_5f0.ctrlKey===undefined||_5f0.ctrlKey==evt.ctrlKey)&&(_5f0.altKey===undefined||_5f0.altKey==evt.altKey)&&(_5f0.metaKey===undefined||_5f0.metaKey==(evt.metaKey||false))&&(_5f0.shiftKey===undefined||_5f0.shiftKey==evt.shiftKey)){
evt.stopPropagation();
evt.preventDefault();
_5ea.trigger(evt,_5f1,node,_5f2,_5f0,_5f3,_5f4,_5f5);
}else{
if(_5ea._obj==_5f0){
_5ea.stop();
}
}
})),on(node,"keyup",lang.hitch(this,function(){
if(_5ea._obj==_5f0){
_5ea.stop();
}
}))];
return {remove:function(){
_5e7.forEach(_5f6,function(h){
h.remove();
});
}};
},addMouseListener:function(node,_5f7,_5f8,_5f9,_5fa,_5fb){
var _5fc=[on(node,"mousedown",lang.hitch(this,function(evt){
evt.preventDefault();
_5ea.trigger(evt,_5f7,node,_5f8,node,_5f9,_5fa,_5fb);
})),on(node,"mouseup",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_5ea.stop();
})),on(node,"mouseout",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_5ea.stop();
})),on(node,"dblclick",lang.hitch(this,function(evt){
evt.preventDefault();
if(has("ie")<9){
_5ea.trigger(evt,_5f7,node,_5f8,node,_5f9,_5fa,_5fb);
setTimeout(lang.hitch(this,_5ea.stop),50);
}
}))];
return {remove:function(){
_5e7.forEach(_5fc,function(h){
h.remove();
});
}};
},addListener:function(_5fd,_5fe,_5ff,_600,_601,_602,_603,_604){
var _605=[this.addKeyListener(_5fe,_5ff,_600,_601,_602,_603,_604),this.addMouseListener(_5fd,_600,_601,_602,_603,_604)];
return {remove:function(){
_5e7.forEach(_605,function(h){
h.remove();
});
}};
}});
return _5ea;
});
},"dijit/MenuItem":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/kernel","dojo/sniff","dojo/_base/lang","./_Widget","./_TemplatedMixin","./_Contained","./_CssStateMixin","dojo/text!./templates/MenuItem.html"],function(_606,dom,_607,_608,_609,has,lang,_60a,_60b,_60c,_60d,_60e){
var _60f=_606("dijit.MenuItem"+(has("dojo-bidi")?"_NoBidi":""),[_60a,_60b,_60c,_60d],{templateString:_60e,baseClass:"dijitMenuItem",label:"",_setLabelAttr:function(val){
this._set("label",val);
var _610="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_610=val.charAt(ndx+1);
var _611=val.substr(0,ndx);
var _612=val.substr(ndx+3);
text=_611+_610+_612;
val=_611+"<span class=\"dijitMenuItemShortcutKey\">"+_610+"</span>"+_612;
}else{
text=val;
}
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_610);
},iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},accelKey:"",disabled:false,_fillContent:function(_613){
if(_613&&!("label" in this.params)){
this._set("label",_613.innerHTML);
}
},buildRendering:function(){
this.inherited(arguments);
var _614=this.id+"_text";
_607.set(this.containerNode,"id",_614);
if(this.accelKeyNode){
_607.set(this.accelKeyNode,"id",this.id+"_accel");
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
},_setSelected:function(_615){
_608.toggle(this.domNode,"dijitMenuItemSelected",_615);
},setLabel:function(_616){
_609.deprecated("dijit.MenuItem.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_616);
},setDisabled:function(_617){
_609.deprecated("dijit.Menu.setDisabled() is deprecated.  Use set('disabled', bool) instead.","","2.0");
this.set("disabled",_617);
},_setDisabledAttr:function(_618){
this.focusNode.setAttribute("aria-disabled",_618?"true":"false");
this._set("disabled",_618);
},_setAccelKeyAttr:function(_619){
if(this.accelKeyNode){
this.accelKeyNode.style.display=_619?"":"none";
this.accelKeyNode.innerHTML=_619;
_607.set(this.containerNode,"colSpan",_619?"1":"2");
}
this._set("accelKey",_619);
}});
if(has("dojo-bidi")){
_60f=_606("dijit.MenuItem",_60f,{_setLabelAttr:function(val){
this.inherited(arguments);
if(this.textDir==="auto"){
this.applyTextDir(this.textDirNode);
}
}});
}
return _60f;
});
},"dijit/MenuBarItem":function(){
define(["dojo/_base/declare","./MenuItem","dojo/text!./templates/MenuBarItem.html"],function(_61a,_61b,_61c){
var _61d=_61a("dijit._MenuBarItemMixin",null,{templateString:_61c,_setIconClassAttr:null});
var _61e=_61a("dijit.MenuBarItem",[_61b,_61d],{});
_61e._MenuBarItemMixin=_61d;
return _61e;
});
},"dijit/MenuBar":function(){
define(["dojo/_base/declare","dojo/keys","./_MenuBase","dojo/text!./templates/MenuBar.html"],function(_61f,keys,_620,_621){
return _61f("dijit.MenuBar",_620,{templateString:_621,baseClass:"dijitMenuBar",popupDelay:0,_isMenuBar:true,_orient:["below"],_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}
},focusChild:function(item){
this.inherited(arguments);
if(this.activated&&item.popup&&!item.disabled){
this._openItemPopup(item,true);
}
},_onChildDeselect:function(item){
if(this.currentPopupItem==item){
this.currentPopupItem=null;
item._closePopup();
}
this.inherited(arguments);
},_onLeftArrow:function(){
this.focusPrev();
},_onRightArrow:function(){
this.focusNext();
},_onDownArrow:function(evt){
this._moveToPopup(evt);
},_onUpArrow:function(){
},onItemClick:function(item,evt){
if(item.popup&&item.popup.isShowingNow&&(!/^key/.test(evt.type)||evt.keyCode!==keys.DOWN_ARROW)){
item.focusNode.focus();
this._cleanUp(true);
}else{
this.inherited(arguments);
}
}});
});
},"idx/util":function(){
define(["dojo/_base/lang","idx/main","dojo/_base/kernel","dojo/has","dojo/aspect","dojo/_base/xhr","dojo/_base/window","dojo/_base/url","dojo/date/stamp","dojo/json","dojo/string","dojo/dom-class","dojo/dom-style","dojo/dom-attr","dojo/dom-construct","dojo/dom-geometry","dojo/io-query","dojo/query","dojo/NodeList-dom","dojo/Stateful","dijit/registry","dijit/form/_FormWidget","dijit/_WidgetBase","dojo/_base/sniff","dijit/_base/manager"],function(_622,_623,_624,dHas,_625,dXhr,_626,dURL,_627,_628,_629,_62a,_62b,_62c,_62d,_62e,_62f,_630,_631,_632,_633,_634,_635){
var _636=_622.getObject("util",true,_623);
_636.getVersion=function(full){
var _637={url:_624.moduleUrl("idx","version.txt"),showProgress:false,handleAs:"json",load:function(_638,_639){
var msg=_638.version;
if(full){
msg+="-";
msg+=_638.revision;
}
console.debug(msg);
},error:function(_63a,_63b){
console.debug(_63a);
return;
}};
dXhr.xhrGet(_637);
};
_636.getOffsetPosition=function(node,root){
var body=_626.body();
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
_636.typeOfObject=function(_63c){
if(_622.isString(_63c)){
return "string";
}
if(typeof _63c=="undefined"){
return "undefined";
}
if(typeof _63c=="number"){
return "number";
}
if(typeof _63c=="boolean"){
return "boolean";
}
if(_622.isFunction(_63c)){
return "function";
}
if(_622.isArray(_63c)){
return "array";
}
if(_63c instanceof Date){
return "date";
}
if(_63c instanceof dURL){
return "url";
}
return "object";
};
_636.parseObject=function(_63d,type){
var _63e=0;
switch(type){
case "regex":
_63d=""+_63d;
_63e=_63d.lastIndexOf("/");
if((_63d.length>2)&&(_63d.charAt(0)=="/")&&(_63e>0)){
return new RegExp(_63d.substring(1,_63e),((_63e==_63d.length-1)?undefined:_63d.substring(_63e+1)));
}else{
return new RegExp(_63d);
}
break;
case "null":
return null;
case "undefined":
return undefined;
case "string":
return ""+_63d;
case "number":
if(typeof _63d=="number"){
return _63d;
}
return _63d.length?Number(_63d):NaN;
case "boolean":
return (typeof _63d=="boolean")?_63d:!(_63d.toLowerCase()=="false");
case "function":
if(_622.isFunction(_63d)){
_63d=_63d.toString();
_63d=_622.trim(_63d.substring(_63d.indexOf("{")+1,_63d.length-1));
}
try{
if(_63d===""||_63d.search(/[^\w\.]+/i)!=-1){
return new Function(_63d);
}else{
return _622.getObject(_63d,false)||new Function(_63d);
}
}
catch(e){
return new Function();
}
case "array":
if(_622.isArray(_63d)){
return _63d;
}
return _63d?_63d.split(/\s*,\s*/):[];
case "date":
if(_63d instanceof Date){
return _63d;
}
switch(_63d){
case "":
return new Date("");
case "now":
return new Date();
default:
return _627.fromISOString(_63d);
}
case "url":
if(_63d instanceof dURL){
return _63d;
}
return _624.baseUrl+_63d;
default:
if(_636.typeOfObject(_63d)=="string"){
return _628.parse(_63d);
}else{
return _63d;
}
}
};
_636.getCSSOptions=function(_63f,_640,_641,_642){
var body=_626.body();
if((!_640)||(("canHaveHTML" in _640)&&(!_640.canHaveHTML))){
_640=body;
}
var _643=false;
var trav=_640;
while(trav&&(!_643)){
if(trav===body){
_643=true;
}
trav=trav.parentNode;
}
var root=null;
if(!_643){
trav=_640;
var _644=[];
while(trav){
_644.push(_62c.get(trav,"class"));
trav=trav.parentNode;
}
_644.reverse();
var root=_62d.create("div",{style:"visibility: hidden; display: none;"},body,"last");
var trav=root;
for(var _645=0;_645<_644.length;_645++){
var _646=_644[_645];
if(_646){
_646=_629.trim(_646);
}
if(_646&&_646.length==0){
_646=null;
}
var _647=(_646)?{"class":_646}:null;
trav=_62d.create("div",_647,trav,"last");
}
_640=trav;
}
var _648=_62d.create("div",null,_640);
_62a.add(_648,_63f);
var _649=_62b.getComputedStyle(_648);
var _64a=null;
if(_649){
_64a=""+_649.backgroundImage;
}
_62d.destroy(_648);
if(root){
_62d.destroy(root);
}
var _64b=((!_64a)||(_64a.length<5)||(_64a.toLowerCase().substring(0,4)!="url(")||(_64a.charAt(_64a.length-1)!=")"));
var _64c=null;
if(_64b&&(_64a==null||_64a=="none")&&_642&&(!_622.isString(_642))){
_64c=_642;
}
if(!_64c){
var _64d=null;
if(_64b&&(_64a==null||_64a=="none")&&_642&&_622.isString(_642)){
_64d=_642;
}else{
if(!_64b){
_64a=_64a.substring(4,_64a.length-1);
if(_64a.charAt(0)=="\""){
if(_64a.length<2){
return null;
}
if(_64a.charAt(_64a.length-1)!="\""){
return null;
}
_64a=_64a.substring(1,_64a.length-1);
}
var _64e=_64a.lastIndexOf("?");
var _64f=_64a.lastIndexOf("/");
if(_64e<0){
return null;
}
_64d=_64a.substring(_64e+1,_64a.length);
}
}
if(_64d==null){
return null;
}
if(_64d.length==0){
return null;
}
_64c=_62f.queryToObject(_64d);
}
return (_641)?_636.mixin({},_64c,_641):_64c;
};
_636.mixinCSSDefaults=function(_650,_651,_652){
if(!_650){
return null;
}
var opts=_636.getCSSOptions(_651,_652);
if(!opts){
return null;
}
_636.mixin(_650,opts);
return opts;
};
_636.mixin=function(_653,_654,_655){
if(!_653){
return null;
}
if(!_654){
return _653;
}
if(!_655){
_655=_653;
}
var src={};
for(var _656 in _654){
if(!(_656 in _655)){
continue;
}
var _657=_636.typeOfObject(_655[_656]);
src[_656]=_636.parseObject(_654[_656],_657);
}
_622.mixin(_653,src);
return _653;
};
_636.recursiveMixin=function(_658,_659,_65a){
var _65b=null;
var _65c=null;
var _65d=null;
if(_65a){
_65b=_65a.clone;
_65c=_65a.controlField;
if("controlValue" in _65a){
_65d=_65a.controlValue;
}else{
_65d=true;
}
}
for(field in _659){
if(field in _658){
var _65e=_658[field];
var _65f=_659[field];
var _660=_636.typeOfObject(_65e);
var _661=_636.typeOfObject(_65f);
if((_660==_661)&&(_660=="object")&&((!_65c)||(_65e[_65c]==_65d))){
_636.recursiveMixin(_65e,_65f,_65a);
}else{
_658[field]=(_65b)?_622.clone(_65f):_65f;
}
}else{
_658[field]=(_65b)?_622.clone(_659[field]):_659[field];
}
}
};
_636.nullify=function(_662,_663,_664){
var _665=0;
for(_665=0;_665<_664.length;_665++){
var prop=_664[_665];
if(!(prop in _662)){
continue;
}
if((_663)&&(prop in _663)){
continue;
}
_662[prop]=null;
}
};
_636._getNodeStyle=function(node){
var _666=_62c.get(node,"style");
if(!_666){
return null;
}
var _667=null;
if(_636.typeOfObject(_666)=="string"){
_667={};
var _668=_666.split(";");
for(var _669=0;_669<_668.length;_669++){
var _66a=_668[_669];
var _66b=_66a.indexOf(":");
if(_66b<0){
continue;
}
var _66c=_66a.substring(0,_66b);
var _66d="";
if(_66b<_66a.length-1){
_66d=_66a.substring(_66b+1);
}
_667[_66c]=_66d;
}
}else{
_667=_666;
}
return _667;
};
_636._getNodePosition=function(node){
var _66e=_636._getNodeStyle(node);
if(!_66e){
return "";
}
if(!_66e.position){
return "";
}
return _66e.position;
};
_636.fitToWidth=function(_66f,_670){
var pos=_636._getNodePosition(_670);
_62b.set(_66f,{width:"auto"});
_62b.set(_670,{position:"static"});
var dim=_62e.getMarginBox(_66f);
_62b.set(_66f,{width:dim.w+"px"});
_62b.set(_670,{position:pos});
return dim;
};
_636.fitToHeight=function(_671,_672){
var pos=_636._getNodePosition(_672);
_62b.set(_671,{height:"auto"});
_62b.set(_672,{position:"static"});
var dim=_62e.getMarginBox(_671);
_62b.set(_671,{height:dim.h+"px"});
_62b.set(_672,{position:pos});
return dim;
};
_636.fitToSize=function(_673,_674){
var pos=_636._getNodePosition(_674);
_62b.set(_673,{width:"auto",height:"auto"});
_62b.set(_674,{position:"static"});
var dim=_62e.getMarginBox(_673);
_62b.set(_673,{width:dim.w+"px",height:dim.h+"px"});
_62b.set(_674,{position:pos});
return dim;
};
_636.getStaticSize=function(node){
var _675=_636._getNodeStyle(node);
var pos=(_675&&_675.position)?_675.position:"";
var _676=(_675&&_675.width)?_675.width:"";
var _677=(_675&&_675.height)?_675.height:"";
_62b.set(node,{position:"static",width:"auto",height:"auto"});
var dim=_62e.getMarginBox(node);
_62b.set(node,{position:pos,width:_676,height:_677});
return dim;
};
_636.reposition=function(node,_678){
var _679=_636._getNodePosition(node);
_62b.set(node,{position:_678});
return _679;
};
_636.getParentWidget=function(_67a,_67b){
var _67c=(_67a instanceof _635)?_67a.domNode:_67a;
var _67d=_67c.parentNode;
if(_67d==null){
return null;
}
var _67e=_633.getEnclosingWidget(_67d);
while((_67b)&&(_67e)&&(!(_67e instanceof _67b))){
_67d=_67e.domNode.parentNode;
_67e=null;
if(_67d){
_67e=_633.getEnclosingWidget(_67d);
}
}
return _67e;
};
_636.getSiblingWidget=function(_67f,_680,_681){
var _682=(_67f instanceof _635)?_67f.domNode:_67f;
var _683=_682.parentNode;
if(_683==null){
return null;
}
var _684=_683.childNodes;
if(!_684){
return null;
}
var _685=0;
for(_685=0;_685<_684.length;_685++){
if(_684[_685]==_682){
break;
}
}
if(_685==_684.length){
return null;
}
var step=(_680)?-1:1;
var _686=(_680)?-1:_684.length;
var _687=0;
var _688=null;
for(_687=(_685+step);_687!=_686;(_687+=step)){
var _689=_684[_687];
var _68a=_633.getEnclosingWidget(_689);
if(!_68a){
continue;
}
if(_68a.domNode==_689){
if((!_681)||(_68a instanceof _681)){
_688=_68a;
break;
}
}
}
return _688;
};
_636.getChildWidget=function(_68b,last,_68c){
if(!(_68b instanceof _635)){
var _68d=_633.getEnclosingWidget(_68b);
if(_68d){
_68b=_68d;
}
}
var _68e=null;
if(_68b instanceof _635){
_68e=_68b.getChildren();
}else{
_68e=_68b.childNodes;
}
if(!_68e){
return null;
}
if(_68e.length==0){
return null;
}
var _68f=(last)?(_68e.length-1):0;
var step=(last)?-1:1;
var _690=(last)?-1:_68e.length;
var _691=0;
var _692=null;
for(_691=_68f;_691!=_690;(_691+=step)){
var _68d=_68e[_691];
if(!(_68d instanceof _635)){
var node=_68d;
_68d=_633.getEnclosingWidget(node);
if(!_68d){
continue;
}
if(_68d.domNode!=node){
continue;
}
}
if((!_68c)||(_68d instanceof _68c)){
_692=_68d;
break;
}
}
return _692;
};
_636.getFormWidget=function(_693,_694){
var _695=null;
if(!_694){
_694=_626.body();
}else{
if(_694 instanceof _635){
_695=_694.domNode;
}else{
_695=form;
}
}
var _696=null;
var _697=_630("[name='"+_693+"']",_695);
for(var _698=0;(!_696)&&(_698<_697.length);_698++){
var node=_697[_698];
var _699=_633.getEnclosingWidget(node);
if(!_699){
continue;
}
if(!(_699 instanceof _634)){
continue;
}
var name=_699.get("name");
if(name!=_693){
continue;
}
_696=_699;
}
return _696;
};
_636.isNodeOrElement=function(obj){
return ((obj.parentNode)&&(obj.childNodes)&&(obj.attributes))?true:false;
};
_636.debugObject=function(obj){
return _636._debugObject(obj,"/",[]);
};
_636._debugObject=function(obj,path,seen){
if(obj===null){
return "null";
}
var _69a=_636.typeOfObject(obj);
switch(_69a){
case "object":
for(var _69b=0;_69b<seen.length;_69b++){
if(seen[_69b].obj==obj){
return "CIRCULAR_REFERENCE[ "+seen[_69b].path+" ]";
}
}
seen.push({obj:obj,path:path});
var _69c="{ ";
var _69d="";
for(field in obj){
_69c=(_69c+_69d+"\""+field+"\": "+_636._debugObject(obj[field],path+"/\""+field+"\"",seen));
_69d=", ";
}
_69c=_69c+" }";
return _69c;
case "date":
return "DATE[ "+_627.toISOString(obj)+" ]";
default:
return _628.stringify(obj);
}
};
_636.isBrowser=dHas("host-browser");
_636.isIE=dHas("ie");
_636.isFF=dHas("ff");
_636.isSafari=dHas("safari");
_636.isChrome=dHas("chrome");
_636.isMozilla=dHas("mozilla");
_636.isOpera=dHas("opera");
_636.isKhtml=dHas("khtml");
_636.isAIR=dHas("air");
_636.isQuirks=dHas("quirks");
_636.isWebKit=dHas("webkit");
_636.fromJson=typeof JSON!="undefined"?JSON.parse:(function(){
var _69e="(?:-?\\b(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?\\b)";
var _69f="(?:[^\\0-\\x08\\x0a-\\x1f\"\\\\]"+"|\\\\(?:[\"/\\\\bfnrt]|u[0-9A-Fa-f]{4}))";
var _6a0="(?:\""+_69f+"*\")";
var _6a1=new RegExp("(?:false|true|null|[\\{\\}\\[\\]]"+"|"+_69e+"|"+_6a0+")","g");
var _6a2=new RegExp("\\\\(?:([^u])|u(.{4}))","g");
var _6a3={"\"":"\"","/":"/","\\":"\\","b":"\b","f":"\f","n":"\n","r":"\r","t":"\t"};
function _6a4(_6a5,ch,hex){
return ch?_6a3[ch]:String.fromCharCode(parseInt(hex,16));
};
var _6a6=new String("");
var _6a7="\\";
var _6a8={"{":Object,"[":Array};
var hop=Object.hasOwnProperty;
return function(json,_6a9){
var toks=json.match(_6a1);
var _6aa;
var tok=toks[0];
var _6ab=false;
if("{"===tok){
_6aa={};
}else{
if("["===tok){
_6aa=[];
}else{
_6aa=[];
_6ab=true;
}
}
var key;
var _6ac=[_6aa];
for(var i=1-_6ab,n=toks.length;i<n;++i){
tok=toks[i];
var cont;
switch(tok.charCodeAt(0)){
default:
cont=_6ac[0];
cont[key||cont.length]=+(tok);
key=void 0;
break;
case 34:
tok=tok.substring(1,tok.length-1);
if(tok.indexOf(_6a7)!==-1){
tok=tok.replace(_6a2,_6a4);
}
cont=_6ac[0];
if(!key){
if(cont instanceof Array){
key=cont.length;
}else{
key=tok||_6a6;
break;
}
}
cont[key]=tok;
key=void 0;
break;
case 91:
cont=_6ac[0];
_6ac.unshift(cont[key||cont.length]=[]);
key=void 0;
break;
case 93:
_6ac.shift();
break;
case 102:
cont=_6ac[0];
cont[key||cont.length]=false;
key=void 0;
break;
case 110:
cont=_6ac[0];
cont[key||cont.length]=null;
key=void 0;
break;
case 116:
cont=_6ac[0];
cont[key||cont.length]=true;
key=void 0;
break;
case 123:
cont=_6ac[0];
_6ac.unshift(cont[key||cont.length]={});
key=void 0;
break;
case 125:
_6ac.shift();
break;
}
}
if(_6ab){
if(_6ac.length!==1){
throw new Error();
}
_6aa=_6aa[0];
}else{
if(_6ac.length){
throw new Error();
}
}
if(_6a9){
var walk=function(_6ad,key){
var _6ae=_6ad[key];
if(_6ae&&typeof _6ae==="object"){
var _6af=null;
for(var k in _6ae){
if(hop.call(_6ae,k)&&_6ae!==_6ad){
var v=walk(_6ae,k);
if(v!==void 0){
_6ae[k]=v;
}else{
if(!_6af){
_6af=[];
}
_6af.push(k);
}
}
}
if(_6af){
for(var i=_6af.length;--i>=0;){
delete _6ae[_6af[i]];
}
}
}
return _6a9.call(_6ad,key,_6ae);
};
_6aa=walk({"":_6aa},"");
}
return _6aa;
};
})();
function _6b0(_6b1,_6b2){
var _6b3=_6b1;
if(!_6b3){
_6b3={"1em":-1,"1ex":-1,"100%":-1,"12pt":-1,"16px":-1,"xx-small":-1,"x-small":-1,"small":1,"medium":-1,"large":-1,"x-large":-1,"xx-large":-1};
}
if(_6b2&&(!(_6b2 in _6b3))){
_6b3[_6b2]=-1;
}
var p;
if(dHas("ie")){
_626.doc.documentElement.style.fontSize="100%";
}
var div=_62d.create("div",{style:{position:"absolute",left:"0",top:"-100000px",width:"30px",height:"1000em",borderWidth:"0",margin:"0",padding:"0",outline:"none",lineHeight:"1",overflow:"hidden"}},_626.body());
for(p in _6b3){
if(_6b3[p]>=0){
continue;
}
_62b.set(div,"fontSize",p);
_6b3[p]=Math.round(div.offsetHeight*12/16)*16/12/1000;
}
_626.body().removeChild(div);
return _6b3;
};
function _6b4(_6b5){
var _6b6=_636.typeOfObject(_6b5);
if((_6b6=="object")&&(_636.isNodeOrElement(_6b5))){
var _6b7=_62b.getComputedStyle(_6b5);
_6b5=_6b7["fontSize"];
}else{
if(_6b6!="string"){
_6b5=""+_6b5;
}
}
return _6b5;
};
var _6b8=null;
function _6b9(_6ba,_6bb){
var _6bc=_636.typeOfObject(_6ba);
if(_6bc=="boolean"){
_6bb=_6ba;
_6ba=null;
}else{
_6ba=_6b4(_6ba);
}
if(_6b8&&_6bb){
if(_6ba){
if(_6b8[_6ba]){
delete _6b8[_6ba];
}
}else{
_6b8=null;
}
}
if(_6bb||!_6b8||(_6ba&&!_6b8[_6ba])){
_6b8=_6b0(_6b8,_6ba);
}
return _6b8;
};
_636.normalizedLength=function(len,_6bd){
if(len.length===0){
return 0;
}
if(!_6bd){
_6bd="12pt";
}else{
_6bd=_6b4(_6bd);
}
if(len.length>2){
var _6be=len.slice(-2);
var _6bf=(_6be=="em"||_6be=="ex");
if(!_6bf){
_6bd="12pt";
}
var fm=_6b9(_6bd);
var _6c0=fm["12pt"]/12;
var _6c1=fm[_6bd];
var _6c2=_6c1/2;
var val=parseFloat(len);
switch(len.slice(-2)){
case "px":
return val;
case "em":
return val*_6c1;
case "ex":
return val*_6c2;
case "pt":
return val*_6c0;
case "in":
return val*72*_6c0;
case "pc":
return val*12*_6c0;
case "mm":
return val*g.mm_in_pt*_6c0;
case "cm":
return val*g.cm_in_pt*_6c0;
}
}
return parseFloat(len);
};
_636.isPercentage=function(_6c3){
return /^\d+%$/.test(_6c3);
};
_636.includeValidCSSWidth=function(_6c4){
return /width:\s*\d+(%|px|pt|in|pc|mm|cm)/.test(_6c4);
};
_636.getValidCSSWidth=function(_6c5){
var _6c6=_636.typeOfObject(_6c5);
if(_6c6!="string"){
if("width" in _6c5){
_6c5="width: "+_6c5.width+";";
}else{
_6c5="width: "+_6c5+";";
}
}
return /width:\s*\d+(%|px|pt|in|pc|mm|cm|em|ex)/.test(_6c5)?_6c5.match(/width:\s*(\d+(%|px|pt|in|pc|mm|cm|em|ex))/)[1]:"";
};
_636.widgetEquals=function(w1,w2){
if(w1===w2){
return true;
}
if(!w1&&w2){
return false;
}
if(w1&&!w2){
return false;
}
if((!_622.isString(w1))&&("id" in w1)){
w1=w1.id;
}
if((!_622.isString(w2))&&("id" in w2)){
w2=w2.id;
}
return (w1==w2);
};
_636.watch=function(obj,attr,func){
if(!obj){
return null;
}
if(!attr){
return null;
}
if((!("watch" in obj))||(!_622.isFunction(obj.watch))){
return null;
}
attr=_629.trim(attr);
if(attr.length==0){
return null;
}
var uc=attr.charAt(0).toUpperCase();
if(attr.length>1){
uc=uc+attr.substring(1);
}
var _6c7="_set"+uc+"Attr";
if(_6c7 in obj){
return _625.around(obj,_6c7,function(_6c8){
return function(_6c9){
var _6ca=obj.get(attr);
_6c8.apply(obj,arguments);
var _6cb=obj.get(attr);
if(_6ca!=_6cb){
func.call(undefined,attr,_6ca,_6cb);
}
};
});
}else{
return obj.watch(attr,func);
}
};
return _636;
});
},"dijit/layout/_LayoutWidget":function(){
define(["dojo/_base/lang","../_Widget","../_Container","../_Contained","../Viewport","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style"],function(lang,_6cc,_6cd,_6ce,_6cf,_6d0,_6d1,_6d2,_6d3){
return _6d0("dijit.layout._LayoutWidget",[_6cc,_6cd,_6ce],{baseClass:"dijitLayoutContainer",isLayoutContainer:true,_setTitleAttr:null,buildRendering:function(){
this.inherited(arguments);
_6d1.add(this.domNode,"dijitContainer");
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
var _6d4=this.getParent&&this.getParent();
if(!(_6d4&&_6d4.isLayoutContainer)){
this.resize();
this.own(_6cf.on("resize",lang.hitch(this,"resize")));
}
},resize:function(_6d5,_6d6){
var node=this.domNode;
if(_6d5){
_6d2.setMarginBox(node,_6d5);
}
var mb=_6d6||{};
lang.mixin(mb,_6d5||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_6d2.getMarginBox(node),mb);
}
var cs=_6d3.getComputedStyle(node);
var me=_6d2.getMarginExtents(node,cs);
var be=_6d2.getBorderExtents(node,cs);
var bb=(this._borderBox={w:mb.w-(me.w+be.w),h:mb.h-(me.h+be.h)});
var pe=_6d2.getPadExtents(node,cs);
this._contentBox={l:_6d3.toPixelValue(node,cs.paddingLeft),t:_6d3.toPixelValue(node,cs.paddingTop),w:bb.w-pe.w,h:bb.h-pe.h};
this.layout();
},layout:function(){
},_setupChild:function(_6d7){
var cls=this.baseClass+"-child "+(_6d7.baseClass?this.baseClass+"-"+_6d7.baseClass:"");
_6d1.add(_6d7.domNode,cls);
},addChild:function(_6d8,_6d9){
this.inherited(arguments);
if(this._started){
this._setupChild(_6d8);
}
},removeChild:function(_6da){
var cls=this.baseClass+"-child"+(_6da.baseClass?" "+this.baseClass+"-"+_6da.baseClass:"");
_6d1.remove(_6da.domNode,cls);
this.inherited(arguments);
}});
});
},"curam/util/SessionTimeout":function(){
define(["dojo/dom","dojo/dom-construct","curam/util","curam/dialog","curam/util/external","curam/util/LocalConfig"],function(dom,_6db,util,_6dc,_6dd,_6de){
curam.define.singleton("curam.util.SessionTimeout",{_EXPIRED_MESSAGE_KEY:"expiredMessage",_INTERNAL_LOGOUT_WRAPPER:"internal-logout-wrapper",logoutPageID:"",userMessageNode:null,userMessageNodeID:"userMessage",minutesCompNodeID:"minutesComp",secondsCompNodeID:"secondsComp",sessTimeoutWarningJSPXDialog:"session-timeout-warning-dialog.jspx",interactionAllowance:3000,_idleTick:10000,_expiryTick:1000,_cfg:null,_idleClock:null,idleAllowance:0,tlw:false,_isExternal:false,requiredWarnDurationMs:0,effectiveTimeout:0,__remainingCountdown:0,checkSessionExpired:function(){
sto._invalidateIdleClock(true);
sto._isExternal=sto.tlw.jsScreenContext&&sto.tlw.jsScreenContext.hasContextBits("EXTAPP");
sto._cfg=sto.getTimeoutWarningConfig();
sto._cfg["isExternal"]=sto._isExternal;
sto._cfg["tlw"]=sto.tlw;
if(sto._cfg.bufferingPeriod){
sto.interactionAllowance=sto._cfg.bufferingPeriod*1000;
}
var _6df=sto._cfg&&sto._cfg.timeout;
sto.requiredWarnDurationMs=_6df?_6df*1000:0;
sto._idleClock=setInterval(function(){
sto._doCheckExp();
},sto._idleTick);
},_doCheckExp:function(){
var _6e0="";
if(typeof (Storage)!=="undefined"){
if(sessionStorage.sessionExpiry){
_6e0=sessionStorage.sessionExpiry;
}
}
if(!sto.previousSessionExpiryString||(sto.previousSessionExpiryString!=_6e0)){
sto._validateSessionExpiry(_6e0);
return;
}
if(sto.idleAllowance==0){
return;
}
sto.idleAllowance-=sto._idleTick;
if(sto.idleAllowance<=0){
sto._invalidateIdleClock(true);
sto._openSessionTimeoutWarningModalDialog();
}
},_validateSessionExpiry:function(_6e1){
if(_6e1==null){
sto._invalidateIdleClock();
return;
}
var _6e2=_6e1.split("-",2);
if(_6e2&&_6e2.length==2){
for(var idx in _6e2){
var _6e3=Math.abs(_6e2[idx]);
if(isNaN(_6e3)){
sto._invalidateIdleClock();
return;
}
_6e2[idx]=_6e3;
}
sto.idleAllowance=Math.abs(_6e2[0]-_6e2[1]);
sto._insertWarnDuration();
sto.previousSessionExpiryString=_6e1;
return;
}
sto._invalidateIdleClock();
},_invalidateIdleClock:function(_6e4){
_6e4&&clearInterval(sto._idleClock);
sto.previousSessionExpiryString=false;
sto.idleAllowance=0;
},_insertWarnDuration:function(){
var _6e5=sto.idleAllowance-sto.interactionAllowance-sto._idleTick;
sto.effectiveTimeout=Math.min(sto.requiredWarnDurationMs,Math.max(_6e5,0));
sto._cfg["effectiveTimeout"]=sto.effectiveTimeout;
sto.idleAllowance-=(sto.effectiveTimeout);
},getTimeoutWarningConfig:function(){
var wdef=sto.tlw||sto._cfg.tlw;
if(wdef.TIMEOUT_WARNING_CONFIG){
return wdef.TIMEOUT_WARNING_CONFIG.timeoutWarning;
}else{
return sto.pseudoConfig;
}
},_openSessionTimeoutWarningModalDialog:function(){
var size={width:sto._cfg.width,height:sto._cfg.height};
if(sto._isExternal){
sto.tlw.openModal(sto.sessTimeoutWarningJSPXDialog,size);
}else{
sto.tlw.dialogOpenerRef=util.showModalDialogWithRef(sto.sessTimeoutWarningJSPXDialog,null,size);
sto.tlw.dialogOpenerRef&&sto.tlw.dialogOpenerRef._setClosableAttr(false);
}
},initTimer:function(_6e6,_6e7){
sto._cfg=_6e6;
sto.__remainingCountdown=_6e6.effectiveTimeout;
var _6e8=dom.byId(sto.minutesCompNodeID),_6e9=dom.byId(sto.secondsCompNodeID);
sto.expiryCountdown=setInterval(function(){
sto._countDown(_6e8,_6e9);
},sto._expiryTick);
},_countDown:function(_6ea,_6eb){
if(sto.__remainingCountdown==0){
sto._stopCountdown();
return;
}
sto.__remainingCountdown-=sto._expiryTick;
if(sto.__remainingCountdown<=0){
sto._stopCountdown();
sto.tlw.curam.util.SessionTimeout.autoLogout();
_6dc.closeModalDialog();
return;
}
var _6ec=new Date(sto.__remainingCountdown);
var secs=""+_6ec.getSeconds(),sPad=(secs.length==1)?"0":"";
_6ea.innerHTML=_6ec.getMinutes();
_6eb.innerHTML=sPad+secs;
},_stopCountdown:function(){
sto.__remainingCountdown=0;
clearInterval(sto.expiryCountdown);
},waitForRedirection:function(){
dojo.subscribe("/curam/dialog/close",function(){
sto._redirectToLogoutWrapper();
});
},autoLogout:function(){
dojo.subscribe("/curam/dialog/close",function(){
sto._redirectLoginWithSessionExpiredMessage();
});
},_redirectToLogoutWrapper:function(){
sto._cfg.tlw.dojo.publish("curam/redirect/logout");
var page=sto._cfg.logoutPage||false;
if(!page){
return;
}
if(sto._cfg.isExternal){
sto._cfg.tlw.displayContent({pageID:page,param:[{paramKey:"invalidateSession",paramValue:true}]});
}else{
sto._redirectInternalLogoutWrapper(page);
}
},_redirectInternalLogoutWrapper:function(_6ed){
sto._cfg.tlw.dialogOpenerRef=null;
if(_6ed===sto._INTERNAL_LOGOUT_WRAPPER){
_6ed+=".jspx?invalidateSession=true";
dojo.global.location=jsBaseURL+"/"+_6ed;
}else{
var _6ee=_6dc.getParentWindow();
_6ed+="Page.do?invalidateSession=true";
if(_6ee&&_6ee.location!==sto._cfg.tlw.location){
_6dc.doRedirect(_6ee,_6ed,true);
}else{
curam.tab.getTabController().handleLinkClick(_6ed);
}
}
},_redirectLoginWithSessionExpiredMessage:function(){
var _6ef=sto._cfg.expiredUserMessageTxt||"";
localStorage[sto._EXPIRED_MESSAGE_KEY]=_6ef;
sto._redirectToLogoutWrapper();
},resetAndStay:function(){
var _6f0=sto._cfg.tlw.dojo.subscribe("/curam/dialog/close",function(){
if(sto._cfg.tlw.dialogOpenerRef){
var pRef=_6dc.getParentWindow(window);
pRef&&pRef.focus();
sto._cfg.tlw.dialogOpenerRef=null;
}
sto._cfg.tlw.dojo.unsubscribe(_6f0);
sto._stopCountdown();
sto.checkSessionExpired();
require(["curam/debug"],function(_6f1){
_6f1.log(_6f1.getProperty("continueApp"));
});
});
},createExpiredSessionMessageHTML:function(_6f2){
var _6f3=_6de.readOption(sto._EXPIRED_MESSAGE_KEY);
if(_6f3){
messageContainerDOM=dom.byId(_6f2);
if(messageContainerDOM){
var _6f4="<div id='error-messages-container' class='wrapper-expired-message'>"+"<ul id='error-messages' class='messages'>"+"<li class='level-1'><div><span id='message'>"+_6f3+"</span></div></li></ul></div>";
_6db.place(_6db.toDom(_6f4),messageContainerDOM);
}
_6de.clearOption(sto._EXPIRED_MESSAGE_KEY);
}
},displayUserMsgAsParagraphs:function(msg,_6f5){
var _6f6=_6f5||dom.byId(sto.userMessageNodeID);
var _6f7=msg.replace("\\n","[<p>]").replace("\n","[<p>]").split("[<p>]");
var _6f8=document.createDocumentFragment();
for(line in _6f7){
var _6f9=document.createElement("p");
_6f9.innerHTML=_6f7[line];
_6f8.appendChild(_6f9);
}
_6db.place(_6f8,_6f6);
},});
var sto=curam.util.SessionTimeout;
sto.tlw=curam.util.getTopmostWindow()||window.top;
return sto;
});
},"dijit/popup":function(){
define("dijit/popup",["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./place","./BackgroundIframe","./Viewport","./main","dojo/touch"],function(_6fa,_6fb,_6fc,dom,_6fd,_6fe,_6ff,_700,_701,has,keys,lang,on,_702,_703,_704,_705){
function _706(){
if(this._popupWrapper){
_6ff.destroy(this._popupWrapper);
delete this._popupWrapper;
}
};
var _707=_6fc(null,{_stack:[],_beginZIndex:1000,_idGen:1,_repositionAll:function(){
if(this._firstAroundNode){
var _708=this._firstAroundPosition,_709=_700.position(this._firstAroundNode,true),dx=_709.x-_708.x,dy=_709.y-_708.y;
if(dx||dy){
this._firstAroundPosition=_709;
for(var i=0;i<this._stack.length;i++){
var _70a=this._stack[i].wrapper.style;
_70a.top=(parseFloat(_70a.top)+dy)+"px";
if(_70a.right=="auto"){
_70a.left=(parseFloat(_70a.left)+dx)+"px";
}else{
_70a.right=(parseFloat(_70a.right)-dx)+"px";
}
}
}
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),dx||dy?10:50);
}
},_createWrapper:function(_70b){
var _70c=_70b._popupWrapper,node=_70b.domNode;
if(!_70c){
_70c=_6ff.create("div",{"class":"dijitPopup",style:{display:"none"},role:"region","aria-label":_70b["aria-label"]||_70b.label||_70b.name||_70b.id},_70b.ownerDocumentBody);
_70c.appendChild(node);
var s=node.style;
s.display="";
s.visibility="";
s.position="";
s.top="0px";
_70b._popupWrapper=_70c;
_6fb.after(_70b,"destroy",_706,true);
if("ontouchend" in document){
on(_70c,"touchend",function(evt){
if(!/^(input|button|textarea)$/i.test(evt.target.tagName)){
evt.preventDefault();
}
});
}
_70c.dojoClick=true;
}
return _70c;
},moveOffScreen:function(_70d){
var _70e=this._createWrapper(_70d);
var ltr=_700.isBodyLtr(_70d.ownerDocument),_70f={visibility:"hidden",top:"-9999px",display:""};
_70f[ltr?"left":"right"]="-9999px";
_70f[ltr?"right":"left"]="auto";
_701.set(_70e,_70f);
return _70e;
},hide:function(_710){
var _711=this._createWrapper(_710);
if(dojo.hasClass(_711,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
_6fd.set(_711,"aria-hidden","true");
_701.set(_711,{position:"absolute",overflow:"hidden",clip:"rect(0 0 0 0)",height:"1px",width:"1px",margin:"-1px",padding:"0",border:"0"});
setTimeout(function(){
if(_6fd.get(_711,"aria-hidden","true")){
_701.set(_711,{display:"none",height:"auto",overflowY:"visible",border:"",position:"",overflow:"",clip:"",width:"",margin:"",padding:""});
}
},200,_711);
}else{
_701.set(_711,{display:"none",height:"auto",overflowY:"visible",border:""});
}
var node=_710.domNode;
if("_originalStyle" in node){
node.style.cssText=node._originalStyle;
}
},getTopPopup:function(){
var _712=this._stack;
for(var pi=_712.length-1;pi>0&&_712[pi].parent===_712[pi-1].widget;pi--){
}
return _712[pi];
},open:function(args){
var _713=this._stack,_714=args.popup,node=_714.domNode,_715=args.orient||["below","below-alt","above","above-alt"],ltr=args.parent?args.parent.isLeftToRight():_700.isBodyLtr(_714.ownerDocument),_716=args.around,id=(args.around&&args.around.id)?(args.around.id+"_dropdown"):("popup_"+this._idGen++);
while(_713.length&&(!args.parent||!dom.isDescendant(args.parent.domNode,_713[_713.length-1].widget.domNode))){
this.close(_713[_713.length-1].widget);
}
var _717=this.moveOffScreen(_714);
if(_714.startup&&!_714._started){
_714.startup();
}
var _718,_719=_700.position(node);
if("maxHeight" in args&&args.maxHeight!=-1){
_718=args.maxHeight||Infinity;
}else{
var _71a=_704.getEffectiveBox(this.ownerDocument),_71b=_716?_700.position(_716,false):{y:args.y-(args.padding||0),h:(args.padding||0)*2};
_718=Math.floor(Math.max(_71b.y,_71a.h-(_71b.y+_71b.h)));
}
if(_719.h>_718){
var cs=_701.getComputedStyle(node),_71c=cs.borderLeftWidth+" "+cs.borderLeftStyle+" "+cs.borderLeftColor;
_701.set(_717,{overflowY:"scroll",height:(_718-2)+"px",border:_71c});
node._originalStyle=node.style.cssText;
node.style.border="none";
}
_6fd.set(_717,{id:id,style:{zIndex:this._beginZIndex+_713.length},"class":"dijitPopup "+(_714.baseClass||_714["class"]||"").split(" ")[0]+"Popup",dijitPopupParent:args.parent?args.parent.id:""});
if(dojo.hasClass(_717,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
if(_6fd.get(_717,"aria-hidden")==="true"){
_701.set(_717,{position:"",clip:"",width:"",margin:"",padding:"",border:""});
}
_6fd.set(_717,"aria-hidden","false");
}
if(_713.length==0&&_716){
this._firstAroundNode=_716;
this._firstAroundPosition=_700.position(_716,true);
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),50);
}
if(has("config-bgIframe")&&!_714.bgIframe){
_714.bgIframe=new _703(_717);
}
var _71d=_714.orient?lang.hitch(_714,"orient"):null,best=_716?_702.around(_717,_716,_715,ltr,_71d):_702.at(_717,args,_715=="R"?["TR","BR","TL","BL"]:["TL","BL","TR","BR"],args.padding,_71d);
_717.style.visibility="visible";
node.style.visibility="visible";
var _71e=[];
_71e.push(on(_717,"keydown",lang.hitch(this,function(evt){
if(evt.keyCode==keys.ESCAPE&&args.onCancel){
evt.stopPropagation();
evt.preventDefault();
args.onCancel(evt);
}else{
if(evt.keyCode==keys.TAB){
evt.stopPropagation();
evt.preventDefault();
var _71f=this.getTopPopup();
if(_71f&&_71f.onCancel){
_71f.onCancel(evt);
}
}
}
})));
if(_714.onCancel&&args.onCancel){
_71e.push(_714.on("cancel",args.onCancel));
}
_71e.push(_714.on(_714.onExecute?"execute":"change",lang.hitch(this,function(){
var _720=this.getTopPopup();
if(_720&&_720.onExecute){
_720.onExecute();
}
})));
_713.push({widget:_714,wrapper:_717,parent:args.parent,onExecute:args.onExecute,onCancel:args.onCancel,onClose:args.onClose,handlers:_71e});
if(_714.onOpen){
_714.onOpen(best);
}
return best;
},close:function(_721){
var _722=this._stack;
while((_721&&_6fa.some(_722,function(elem){
return elem.widget==_721;
}))||(!_721&&_722.length)){
var top=_722.pop(),_723=top.widget,_724=top.onClose;
if(_723.bgIframe){
_723.bgIframe.destroy();
delete _723.bgIframe;
}
if(_723.onClose){
_723.onClose();
}
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_723&&_723.domNode){
this.hide(_723);
}
if(_724){
_724();
}
}
if(_722.length==0&&this._aroundMoveListener){
clearTimeout(this._aroundMoveListener);
this._firstAroundNode=this._firstAroundPosition=this._aroundMoveListener=null;
}
}});
return (_705.popup=new _707());
});
},"dijit/_base/manager":function(){
define(["dojo/_base/array","dojo/_base/config","dojo/_base/lang","../registry","../main"],function(_725,_726,lang,_727,_728){
var _729={};
_725.forEach(["byId","getUniqueId","findWidgets","_destroyAll","byNode","getEnclosingWidget"],function(name){
_729[name]=_727[name];
});
lang.mixin(_729,{defaultDuration:_726["defaultDuration"]||200});
lang.mixin(_728,_729);
return _728;
});
},"curam/util/onLoad":function(){
define(["curam/util","curam/define","curam/debug","dojo/dom-attr"],function(util,_72a,_72b,attr){
_72a.singleton("curam.util.onLoad",{EVENT:"/curam/frame/load",publishers:[],subscribers:[],defaultGetIdFunction:function(_72c){
var _72d=attr.get(_72c,"class").split(" ");
return dojo.filter(_72d,function(_72e){
return _72e.indexOf("iframe-")==0;
})[0];
},addPublisher:function(_72f){
curam.util.onLoad.publishers.push(_72f);
},addSubscriber:function(_730,_731,_732){
curam.util.onLoad.subscribers.push({"getId":_732?_732:curam.util.onLoad.defaultGetIdFunction,"callback":_731,"iframeId":_730});
},removeSubscriber:function(_733,_734,_735){
curam.util.onLoad.subscribers=dojo.filter(curam.util.onLoad.subscribers,function(_736){
return !(_736.iframeId==_733&&_736.callback==_734);
});
},execute:function(){
if(window.parent==window){
curam.debug.log("curam.util.onLoad.execute(): "+_72b.getProperty("curam.util.onLoad.exit"));
return;
}
var _737={};
dojo.forEach(curam.util.onLoad.publishers,function(_738){
_738(_737);
});
curam.util.onLoad.publishers=[];
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
window.parent.dojo.publish(curam.util.onLoad.EVENT,[window.frameElement,_737]);
}});
curam.util.subscribe(curam.util.onLoad.EVENT,function(_739,_73a){
dojo.forEach(curam.util.onLoad.subscribers,function(_73b){
var _73c=_73b.getId(_739);
if(_73b.iframeId==_73c){
_73b.callback(_73c,_73a);
}
});
});
return curam.util.onLoad;
});
},"curam/widget/containers/TransitionContainer":function(){
define(["dojo/_base/declare","dojo/parser","dijit/_Widget","dojo/dom-construct","dojo/_base/window","dijit/layout/ContentPane","dojo/dom-class","dojo/_base/fx","curam/util/cache/CacheLRU","dojox/layout/ContentPane","dojo/_base/array","dojo/query"],function(_73d,_73e,_73f,_740,_741,_742,_743,fx,_744,_745,_746,_747){
return _73d("curam.widget.containers.TransitionContainer",[_742],{transitionDuration:200,_panelCache:null,_currentlyDisplayedPanelKey:-1,_panelToLoadKey:-1,_beenProcessed:false,constructor:function(args){
var _748={maxSize:5};
this._panelCache=new _744(_748);
},_buildPramUrl:function(_749){
var _74a="";
if(_749.param!=null){
_746.forEach(_749.param,function(_74b,i){
if(i>0){
_74a+="&";
}
_74a+=encodeURIComponent(_74b.paramKey)+"="+encodeURIComponent(_74b.paramValue);
});
}
return _74a;
},_setDisplayPanelAttr:function(_74c){
_74c=this._doDataTranslation(_74c);
var _74d=this._buildPramUrl(_74c);
var _74e=_74c.key;
if(this._currentlyDisplayedPanelKey!=_74e){
this._panelCache.getItem(this._currentlyDisplayedPanelKey);
var _74f=this._panelCache.getItem(_74e);
if(_74f==null){
var uri=this._doResourceLookUp(_74c,_74d,_74e);
uri=this._applyParamToUri(_74c,_74d,_74e,uri);
var _750=new _745({href:uri,preload:false,preventCache:true,executeScripts:true,scriptHasHooks:true,refreshOnShow:false,open:false,style:{padding:0,border:0,opacity:0}});
_750=this._contentPaneCreated(_74c,_74d,_74e,_750);
var _751={node:_750.domNode,duration:this.transitionDuration,onEnd:dojo.hitch(this,this._panelFadeInComplete)};
var _752=dojo.hitch(this,function(key){
this._panelFadedOut(key);
curam.debug.log("TransitionContainer.js calling curam.util.setBrowserTabTitle()");
curam.util.setBrowserTabTitle();
});
var _753={node:_750.domNode,duration:this.transitionDuration,onEnd:function(){
console.info("Fadding out onEnd Called for : "+_74e);
_752(_74e);
}};
var _754=fx.fadeIn(_751);
var _755=fx.fadeOut(_753);
_74f={panel:_750,fadeIn:_754,fadeOut:_755};
var _756={callback:function(key,item){
try{
item.panel.destroy();
delete item;
}
catch(err){
console.error(err);
}
}};
this._panelCache.addItem(_74e,_74f,_756);
}else{
console.info("Doning nothing as panel all ready exists");
if(_74c.forceRefresh){
var _74f=this._panelCache.getItem(_74e);
if(_74f){
var uri=this._doResourceLookUp(_74c,_74d,_74e);
uri=this._applyParamToUri(_74c,_74d,_74e,uri);
_74f.panel.open=false;
_74f.panel.set("href",uri);
}
}
}
this._doSwapPanel(_74c,_74e);
}else{
if(_74c.forceRefresh){
var _74f=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_74f){
var uri=this._doResourceLookUp(_74c,_74d,this._currentlyDisplayedPanelKey);
uri=this._applyParamToUri(_74c,_74d,_74e,uri);
_74f.panel.set("href",uri);
}
}
}
},_doDataTranslation:function(_757){
return _757;
},_contentPaneCreated:function(_758,_759,_75a,_75b){
return _75b;
},_doResourceLookUp:function(_75c,_75d,_75e){
var uri=_75c.key;
return uri;
},_applyParamToUri:function(_75f,_760,_761,uri){
if(_760.length>0){
if(uri.indexOf("?")!=-1){
uri+="&";
}else{
uri+="?";
}
uri+=_760;
}
return uri;
},_panelFadedOut:function(_762){
var _763=this._panelCache.getItem(_762);
_763.panel.cancel();
if(_763.panel.domNode!=null){
_743.add(_763.panel.domNode,"dijitHidden");
}else{
}
_763.panel.open=false;
_746.forEach(_747("iframe",_763.panel.domNode),function(_764){
_764.src="";
});
this._fadedOutPanelProcess(_763);
_740.place(_763.panel.domNode,_741.body());
this._panelFadeOutComplete();
this._panelFadeIn();
},_fadedOutPanelProcess:function(_765){
},_panelFadeOutComplete:function(){
},_panelFadeIn:function(){
if(this._panelToFadeInKey!=-1){
var _766=this._panelCache.getItem(this._panelToFadeInKey);
this.set("content",_766.panel);
this._currentlyDisplayedPanelKey=this._panelToFadeInKey;
if(_766.panel.domNode!=null){
_743.remove(_766.panel.domNode,"dijitHidden");
}else{
}
_766.panel.onLoad=function(){
_766.fadeIn.play();
};
_766.panel.open=true;
_766.panel.refresh();
_766.panel.resize();
}
},_panelFadeInComplete:function(){
},_doSwapPanel:function(_767,key){
var _768=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_768!=null){
this.fadeOutDisplay(key);
}else{
this._panelToFadeInKey=key;
this._panelFadeIn();
}
},fadeOutDisplay:function(key){
console.info("fadeOutDisplay");
if(key==null){
key=-1;
}
var _769=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_769!=null){
if(_769.fadeIn.status()=="playing"){
console.info("fadeOutDisplay  - currentlyDisplayedPanel.fadeIn.status() == playing");
_769.fadeIn.stop();
_769.fadeOut.play();
}else{
if(_769.fadeOut.status()!="playing"){
_769.fadeOut.play();
}
}
this._panelToFadeInKey=key;
}else{
this._panelToFadeInKey=key;
this._panelFadeIn();
}
},destroy:function(){
try{
this._panelCache.destroy();
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
},"dojo/dnd/Mover":function(){
define(["../_base/array","../_base/declare","../_base/lang","../sniff","../_base/window","../dom","../dom-geometry","../dom-style","../Evented","../on","../touch","./common","./autoscroll"],function(_76a,_76b,lang,has,win,dom,_76c,_76d,_76e,on,_76f,dnd,_770){
return _76b("dojo.dnd.Mover",[_76e],{constructor:function(node,e,host){
this.node=dom.byId(node);
this.marginBox={l:e.pageX,t:e.pageY};
this.mouseButton=e.button;
var h=(this.host=host),d=node.ownerDocument;
function _771(e){
e.preventDefault();
e.stopPropagation();
};
this.events=[on(d,_76f.move,lang.hitch(this,"onFirstMove")),on(d,_76f.move,lang.hitch(this,"onMouseMove")),on(d,_76f.release,lang.hitch(this,"onMouseUp")),on(d,"dragstart",_771),on(d.body,"selectstart",_771)];
_770.autoScrollStart(d);
if(h&&h.onMoveStart){
h.onMoveStart(this);
}
},onMouseMove:function(e){
_770.autoScroll(e);
var m=this.marginBox;
this.host.onMove(this,{l:m.l+e.pageX,t:m.t+e.pageY},e);
e.preventDefault();
e.stopPropagation();
},onMouseUp:function(e){
if(has("webkit")&&has("mac")&&this.mouseButton==2?e.button==0:this.mouseButton==e.button){
this.destroy();
}
e.preventDefault();
e.stopPropagation();
},onFirstMove:function(e){
var s=this.node.style,l,t,h=this.host;
switch(s.position){
case "relative":
case "absolute":
l=Math.round(parseFloat(s.left))||0;
t=Math.round(parseFloat(s.top))||0;
break;
default:
s.position="absolute";
var m=_76c.getMarginBox(this.node);
var b=win.doc.body;
var bs=_76d.getComputedStyle(b);
var bm=_76c.getMarginBox(b,bs);
var bc=_76c.getContentBox(b,bs);
l=m.l-(bc.l-bm.l);
t=m.t-(bc.t-bm.t);
break;
}
this.marginBox.l=l-this.marginBox.l;
this.marginBox.t=t-this.marginBox.t;
if(h&&h.onFirstMove){
h.onFirstMove(this,e);
}
this.events.shift().remove();
},destroy:function(){
_76a.forEach(this.events,function(_772){
_772.remove();
});
var h=this.host;
if(h&&h.onMoveStop){
h.onMoveStop(this);
}
this.events=this.node=this.host=null;
}});
});
},"dojo/request/default":function(){
define(["exports","require","../has"],function(_773,_774,has){
var _775=has("config-requestProvider"),_776;
if(1||has("host-webworker")){
_776="./xhr";
}else{
if(0){
_776="./node";
}
}
if(!_775){
_775=_776;
}
_773.getPlatformDefaultId=function(){
return _776;
};
_773.load=function(id,_777,_778,_779){
_774([id=="platform"?_776:_775],function(_77a){
_778(_77a);
});
};
});
},"curam/ui/ClientDataAccessor":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/util/Request","curam/debug"],function(_77b,_77c,_77d,_77e){
return _77b("curam.ui.ClientDataAccessor",null,{constructor:function(){
_77c.register("curam/ui/ClientDataAccessor",this);
},get:function(path,_77f,_780,_781){
var _782="servlet/PathResolver"+"?p="+path;
if(_780==undefined){
_780=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_781==undefined){
_781=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_77d.post({url:_782,headers:{"Content-Encoding":"UTF-8"},handleAs:"text",preventCache:true,load:_77f,error:_780,handle:_781});
},getList:function(path,_783,_784,_785){
var _786="servlet/PathResolver"+"?r=l&p="+path;
if(_784==undefined){
_784=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_785==undefined){
_785=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_77d.post({url:_786,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_783,error:_784,handle:_785});
},getRaw:function(path,_787,_788,_789){
var _78a="servlet/PathResolver"+"?r=j&p="+path;
if(_788==undefined){
_788=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_789==undefined){
_789=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_77d.post({url:_78a,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_787,error:_788,handle:_789});
},set:function(path,_78b,_78c,_78d,_78e){
var _78f="servlet/PathResolver"+"?r=x&p="+path+"&v="+encodeURIComponent(_78b);
var _790=curam.util.getTopmostWindow();
if(_78d==undefined||_78d==null){
_78d=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_78e==undefined||_78e==null){
_78e=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
if(_78c==undefined||_78c==null){
_78c=dojo.hitch(this,this.handleClientDataAccessorSuccess);
}
_77d.post({url:_78f,headers:{"Content-Encoding":"UTF-8","csrfToken":_790.csrfToken},handleAs:"text",preventCache:true,load:_78c,error:_78d,handle:_78e});
},handleClientDataAccessorError:function(_791,_792){
var _793=_77e.getProperty("curam.ui.ClientDataAccessor.err.1")+"PathResolverServlet : ";
var _794=_77e.getProperty("curam.ui.ClientDataAccessor.err.2");
_77e.log(_793+_791+_794+_792);
},handleClientDataAccessorSuccess:function(_795,_796){
curam.debug.log("curam.ui.ClientDataAccessor.handleClientDataAccessorSuccess : "+_795);
},handleClientDataAccessorCallback:function(_797,_798){
_77e.log("curam.ui.ClientDataAccessor.handleClientDataAccessorCallback :"+" "+_77e.getProperty("curam.ui.ClientDataAccessor.callback"));
}});
});
},"dijit/BackgroundIframe":function(){
define(["require","./main","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/sniff"],function(_799,_79a,_79b,_79c,_79d,lang,on,has){
has.add("config-bgIframe",(has("ie")&&!/IEMobile\/10\.0/.test(navigator.userAgent))||(has("trident")&&/Windows NT 6.[01]/.test(navigator.userAgent)));
var _79e=new function(){
var _79f=[];
this.pop=function(){
var _7a0;
if(_79f.length){
_7a0=_79f.pop();
_7a0.style.display="";
}else{
if(has("ie")<9){
var burl=_79b["dojoBlankHtmlUrl"]||_799.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_7a0=document.createElement(html);
}else{
_7a0=_79c.create("iframe");
_7a0.src="javascript:\"\"";
_7a0.className="dijitBackgroundIframe";
_7a0.setAttribute("role","presentation");
_79d.set(_7a0,"opacity",0.1);
}
_7a0.tabIndex=-1;
}
return _7a0;
};
this.push=function(_7a1){
_7a1.style.display="none";
_79f.push(_7a1);
};
}();
_79a.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("config-bgIframe")){
var _7a2=(this.iframe=_79e.pop());
node.appendChild(_7a2);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,"resize",node));
}else{
_79d.set(_7a2,{width:"100%",height:"100%"});
}
}
};
lang.extend(_79a.BackgroundIframe,{resize:function(node){
if(this.iframe){
_79d.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
this.iframe.parentNode.removeChild(this.iframe);
_79e.push(this.iframe);
delete this.iframe;
}
}});
return _79a.BackgroundIframe;
});
},"curam/util/Constants":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.util.Constants",{RETURN_PAGE_PARAM:"__o3rpu"});
return curam.util.Constants;
});
},"dijit/form/Button":function(){
define(["require","dojo/_base/declare","dojo/dom-class","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/ready","./_FormWidget","./_ButtonMixin","dojo/text!./templates/Button.html","../a11yclick"],function(_7a3,_7a4,_7a5,has,_7a6,lang,_7a7,_7a8,_7a9,_7aa){
if(1){
_7a7(0,function(){
var _7ab=["dijit/form/DropDownButton","dijit/form/ComboButton","dijit/form/ToggleButton"];
_7a3(_7ab);
});
}
var _7ac=_7a4("dijit.form.Button"+(has("dojo-bidi")?"_NoBidi":""),[_7a8,_7a9],{showLabel:true,iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},baseClass:"dijitButton",templateString:_7aa,_setValueAttr:"valueNode",_setNameAttr:function(name){
if(this.valueNode){
this.valueNode.setAttribute("name",name);
}
},_fillContent:function(_7ad){
if(_7ad&&(!this.params||!("label" in this.params))){
var _7ae=lang.trim(_7ad.innerHTML);
if(_7ae){
this.label=_7ae;
}
}
},_setShowLabelAttr:function(val){
if(this.containerNode){
_7a5.toggle(this.containerNode,"dijitDisplayNone",!val);
}
this._set("showLabel",val);
},setLabel:function(_7af){
_7a6.deprecated("dijit.form.Button.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_7af);
},_setLabelAttr:function(_7b0){
this.inherited(arguments);
if(!this.showLabel&&!("title" in this.params)){
this.titleNode.title=lang.trim(this.containerNode.innerText||this.containerNode.textContent||"");
}
}});
if(has("dojo-bidi")){
_7ac=_7a4("dijit.form.Button",_7ac,{_setLabelAttr:function(_7b1){
this.inherited(arguments);
if(this.titleNode.title){
this.applyTextDir(this.titleNode,this.titleNode.title);
}
},_setTextDirAttr:function(_7b2){
if(this._created&&this.textDir!=_7b2){
this._set("textDir",_7b2);
this._setLabelAttr(this.label);
}
}});
}
return _7ac;
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_7b3,_7b4,_7b5,_7b6,_7b7,_7b8,dom,_7b9,_7ba,_7bb,_7bc,_7bd,has,_7be,lang,on,_7bf,_7c0,_7c1,win,_7c2,_7c3,_7c4){
var _7c5=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_7be.isAsync);
has.add("dojo-bidi",false);
if(1){
_7bf(0,function(){
var _7c6=["dijit/_base/manager"];
_7b3(_7c6);
});
}
var _7c7={};
function _7c8(obj){
var ret={};
for(var attr in obj){
ret[attr.toLowerCase()]=true;
}
return ret;
};
function _7c9(attr){
return function(val){
_7b9[val?"set":"remove"](this.domNode,attr,val);
this._set(attr,val);
};
};
function _7ca(a,b){
return a===b||(a!==a&&b!==b);
};
var _7cb=_7b8("dijit._WidgetBase",[_7c0,_7c2],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_7c9("lang"),dir:"",_setDirAttr:_7c9("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_7b6.blankGif||_7b3.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var ctor=this.constructor;
if(!ctor._setterAttrs){
var _7cc=ctor.prototype,_7cd=ctor._setterAttrs=[],_7ce=(ctor._onMap={});
for(var name in _7cc.attributeMap){
_7cd.push(name);
}
for(name in _7cc){
if(/^on/.test(name)){
_7ce[name.substring(2).toLowerCase()]=name;
}
if(/^_set[A-Z](.*)Attr$/.test(name)){
name=name.charAt(4).toLowerCase()+name.substr(5,name.length-9);
if(!_7cc.attributeMap||!(name in _7cc.attributeMap)){
_7cd.push(name);
}
}
}
}
},postscript:function(_7cf,_7d0){
this.create(_7cf,_7d0);
},create:function(_7d1,_7d2){
if(_7c5){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_7d2);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_7d1){
this.params=_7d1;
lang.mixin(this,_7d1);
}
this.postMixInProperties();
if(!this.id){
this.id=_7c4.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_7c4.add(this);
this.buildRendering();
var _7d3;
if(this.domNode){
this._applyAttributes();
var _7d4=this.srcNodeRef;
if(_7d4&&_7d4.parentNode&&this.domNode!==_7d4){
_7d4.parentNode.replaceChild(this.domNode,_7d4);
_7d3=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_7d3){
delete this.srcNodeRef;
}
this._created=true;
if(_7c5){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _7d5={};
for(var key in this.params||{}){
_7d5[key]=this._get(key);
}
_7b4.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _7d5)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _7d5){
this.set(key,_7d5[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _7d6=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_7d6=_7d6.concat(_7b4.map(_7d6,function(name){
return name+"Rtl";
}));
}
_7ba.add(this.domNode,_7d6);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_7b4.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_7d7){
this._beingDestroyed=true;
this.destroyDescendants(_7d7);
this.destroy(_7d7);
},destroy:function(_7d8){
this._beingDestroyed=true;
this.uninitialize();
function _7d9(w){
if(w.destroyRecursive){
w.destroyRecursive(_7d8);
}else{
if(w.destroy){
w.destroy(_7d8);
}
}
};
_7b4.forEach(this._connects,lang.hitch(this,"disconnect"));
_7b4.forEach(this._supportingWidgets,_7d9);
if(this.domNode){
_7b4.forEach(_7c4.findWidgets(this.domNode,this.containerNode),_7d9);
}
this.destroyRendering(_7d8);
_7c4.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_7da){
if(this.bgIframe){
this.bgIframe.destroy(_7da);
delete this.bgIframe;
}
if(this.domNode){
if(_7da){
_7b9.remove(this.domNode,"widgetId");
}else{
_7bb.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_7da){
_7bb.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_7db){
_7b4.forEach(this.getChildren(),function(_7dc){
if(_7dc.destroyRecursive){
_7dc.destroyRecursive(_7db);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_7dd){
var _7de=this.domNode;
if(lang.isObject(_7dd)){
_7bd.set(_7de,_7dd);
}else{
if(_7de.style.cssText){
_7de.style.cssText+="; "+_7dd;
}else{
_7de.style.cssText=_7dd;
}
}
this._set("style",_7dd);
},_attrToDom:function(attr,_7df,_7e0){
_7e0=arguments.length>=3?_7e0:this.attributeMap[attr];
_7b4.forEach(lang.isArray(_7e0)?_7e0:[_7e0],function(_7e1){
var _7e2=this[_7e1.node||_7e1||"domNode"];
var type=_7e1.type||"attribute";
switch(type){
case "attribute":
if(lang.isFunction(_7df)){
_7df=lang.hitch(this,_7df);
}
var _7e3=_7e1.attribute?_7e1.attribute:(/^on[A-Z][a-zA-Z]*$/.test(attr)?attr.toLowerCase():attr);
if(_7e2.tagName){
_7b9.set(_7e2,_7e3,_7df);
}else{
_7e2.set(_7e3,_7df);
}
break;
case "innerText":
_7e2.innerHTML="";
_7e2.appendChild(this.ownerDocument.createTextNode(_7df));
break;
case "innerHTML":
_7e2.innerHTML=_7df;
break;
case "class":
_7ba.replace(_7e2,_7df,this[attr]);
break;
}
},this);
},get:function(name){
var _7e4=this._getAttrNames(name);
return this[_7e4.g]?this[_7e4.g]():this._get(name);
},set:function(name,_7e5){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var _7e6=this._getAttrNames(name),_7e7=this[_7e6.s];
if(lang.isFunction(_7e7)){
var _7e8=_7e7.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _7e9=this.focusNode&&!lang.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_7e9]&&this[_7e9].tagName,_7ea=tag&&(_7c7[tag]||(_7c7[tag]=_7c8(this[_7e9]))),map=name in this.attributeMap?this.attributeMap[name]:_7e6.s in this?this[_7e6.s]:((_7ea&&_7e6.l in _7ea&&typeof _7e5!="function")||/^aria-|^data-|^role$/.test(name))?_7e9:null;
if(map!=null){
this._attrToDom(name,_7e5,map);
}
this._set(name,_7e5);
}
return _7e8||this;
},_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
var uc=name.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[name]={n:name+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(name,_7eb){
var _7ec=this[name];
this[name]=_7eb;
if(this._created&&!_7ca(_7ec,_7eb)){
if(this._watchCallbacks){
this._watchCallbacks(name,_7ec,_7eb);
}
this.emit("attrmodified-"+name,{detail:{prevValue:_7ec,newValue:_7eb}});
}
},_get:function(name){
return this[name];
},emit:function(type,_7ed,_7ee){
_7ed=_7ed||{};
if(_7ed.bubbles===undefined){
_7ed.bubbles=true;
}
if(_7ed.cancelable===undefined){
_7ed.cancelable=true;
}
if(!_7ed.detail){
_7ed.detail={};
}
_7ed.detail.widget=this;
var ret,_7ef=this["on"+type];
if(_7ef){
ret=_7ef.apply(this,_7ee?_7ee:[_7ed]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,type.toLowerCase(),_7ed);
}
return ret;
},on:function(type,func){
var _7f0=this._onMap(type);
if(_7f0){
return _7b5.after(this,_7f0,func,true);
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
return this.containerNode?_7c4.findWidgets(this.containerNode):[];
},getParent:function(){
return _7c4.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_7f1,_7f2){
return this.own(_7b7.connect(obj,_7f1,this,_7f2))[0];
},disconnect:function(_7f3){
_7f3.remove();
},subscribe:function(t,_7f4){
return this.own(_7c1.subscribe(t,lang.hitch(this,_7f4)))[0];
},unsubscribe:function(_7f5){
_7f5.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_7bc.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_7bd.get(this.domNode,"display")!="none");
},placeAt:function(_7f6,_7f7){
var _7f8=!_7f6.tagName&&_7c4.byId(_7f6);
if(_7f8&&_7f8.addChild&&(!_7f7||typeof _7f7==="number")){
_7f8.addChild(this,_7f7);
}else{
var ref=_7f8&&("domNode" in _7f8)?(_7f8.containerNode&&!/after|before|replace/.test(_7f7||"")?_7f8.containerNode:_7f8.domNode):dom.byId(_7f6,this.ownerDocument);
_7bb.place(this.domNode,ref,_7f7);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,_7f9){
var _7fa=setTimeout(lang.hitch(this,function(){
if(!_7fa){
return;
}
_7fa=null;
if(!this._destroyed){
lang.hitch(this,fcn)();
}
}),_7f9||0);
return {remove:function(){
if(_7fa){
clearTimeout(_7fa);
_7fa=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_7cb.extend(_7c3);
}
return _7cb;
});
},"idx/main":function(){
define(["require","dojo/_base/lang","dojo/_base/kernel","dojo/_base/window","dojo/ready","dojo/has","dojo/sniff","dojo/dom-class"],function(_7fb,_7fc,_7fd,_7fe,_7ff,dHas,_800,_801){
var _802=_7fc.getObject("idx",true);
var _803=_7fd.version.major;
var _804=_7fd.version.minor;
var _805=function(){
var _806=_7fe.body();
var _807="idx_dojo_"+_7fd.version.major+"_"+_7fd.version.minor;
_801.add(_806,_807);
var _808=_7fd.locale.toLowerCase();
if((_808.indexOf("he")==0)||(_808.indexOf("_il")>=0)){
_801.add(_806,"idx_i18n_il");
}
if(dHas("ie")){
_801.add(_806,"dj_ie");
_801.add(_806,"dj_ie"+dHas("ie"));
}else{
if(dHas("ff")){
_801.add(_806,"dj_ff");
_801.add(_806,"dj_ff"+dHas("ff"));
}else{
if(dHas("safari")){
_801.add(_806,"dj_safari");
_801.add(_806,"dj_safari"+dHas("safari"));
}else{
if(dHas("chrome")){
_801.add(_806,"dj_chrome");
_801.add(_806,"dj_chrome"+dHas("chrome"));
}else{
if(dHas("webkit")){
_801.add(_806,"dj_webkit");
}
}
}
}
}
};
if((_803<2)&&(_804<7)){
dojo.addOnLoad(_805);
}else{
_7ff(_805);
}
return _802;
});
},"curam/util/Refresh":function(){
define(["dijit/registry","dojo/dom-class","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/ResourceBundle","curam/util","curam/tab","curam/util/ContextPanel","curam/util/ui/refresh/TabRefreshController"],function(_809,_80a,_80b,_80c,_80d,_80e,_80f,_810){
_80e.singleton("curam.util.Refresh",{submitted:false,pageSubmitted:"",refreshConfig:[],menuBarCallback:null,navigationCallback:null,_controllers:{},_pageRefreshButton:undefined,setMenuBarCallbacks:function(_811,_812){
if(!curam.util.Refresh.menuBarCallback){
curam.util.Refresh.menuBarCallback={updateMenuItemStates:_811,getRefreshParams:_812};
}
},setNavigationCallbacks:function(_813,_814){
if(!curam.util.Refresh.navigationCallback){
curam.util.Refresh.navigationCallback={updateNavItemStates:_813,getRefreshParams:_814};
}
},refreshMenuAndNavigation:function(_815,_816,_817){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"tabWidgetId=%s, refreshMenuBar || refreshNavigation: %s || %s",_815,_816,_817);
if(!_816&&!_817){
curam.debug.log(_80f.getProperty("curam.util.Refresh.no.refresh"));
return;
}
var _818={update:function(_819,_81a,_81b){
curam.debug.log(_80f.getProperty("curam.util.Refresh.dynamic.refresh"),_81a);
var ncb=curam.util.Refresh.navigationCallback;
curam.debug.log("refreshNavigation? ",_817);
if(_817&&_81a.navData&&ncb){
ncb.updateNavItemStates(_819,_81a);
}
var mcb=curam.util.Refresh.menuBarCallback;
curam.debug.log("refreshMenuBar? ",_816);
if(_816&&_81a.menuData&&mcb){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"dynamic tab menu item update");
mcb.updateMenuItemStates(_819,_81a);
}else{
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"no dynamic data, updating initially loaded tab action items to show"+"only those that should be inlined");
curam.util.TabActionsMenu.manageInlineTabMenuStates(_819);
}
},error:function(_81c,_81d){
curam.debug.log("========= "+_80f.getProperty("curam.util.Refresh.dynamic.failure")+" ===========");
curam.debug.log(_80f.getProperty("curam.util.Refresh.dynamic.error"),_81c);
curam.debug.log(_80f.getProperty("curam.util.Refresh.dynamic.args"),_81d);
curam.debug.log("==================================================");
}};
var _81e="servlet/JSONServlet?o3c=TAB_DYNAMIC_STATE_QUERY",_81f=false;
var mcb=curam.util.Refresh.menuBarCallback;
if(_816&&mcb){
var _820=mcb.getRefreshParams(_815);
if(_820){
_81e+="&"+_820;
_81f=true;
}
}
var ncb=curam.util.Refresh.navigationCallback;
if(_817&&ncb){
var _821=ncb.getRefreshParams(_815);
if(_821){
_81e+="&"+_821;
_81f=true;
}
}
curam.debug.log(_80f.getProperty("curam.util.Refresh.dynamic.refresh.req"));
if(_81f){
_80d.post({url:_81e,handleAs:"json",preventCache:true,load:dojo.hitch(_818,"update",_815),error:dojo.hitch(_818,"error")});
}else{
curam.util.TabActionsMenu.manageInlineTabMenuStates(_815);
curam.debug.log(_80f.getProperty("curam.util.Refresh.dynamic.refresh.no_dynamic_items"));
}
},addConfig:function(_822){
var _823=false;
dojo.forEach(curam.util.Refresh.refreshConfig,function(_824){
if(_824.tab==_822.tab){
_824.config=_822.config;
_823=true;
}
});
if(!_823){
curam.util.Refresh.refreshConfig.push(_822);
}
},setupRefreshController:function(_825){
curam.debug.log("curam.util.Refresh.setupRefreshController "+_80f.getProperty("curam.util.ExpandableLists.load.for"),_825);
var _826=_809.byId(_825);
var _827=_826.tabDescriptor.tabID;
var _828=dojo.filter(curam.util.Refresh.refreshConfig,function(item){
return item.tab==_827;
});
if(_828.length==1){
var _829=_828[0];
var ctl=new curam.util.ui.refresh.TabRefreshController(_825,_829);
curam.util.Refresh._controllers[_825]=ctl;
ctl.setRefreshHandler(curam.util.Refresh.handleRefreshEvent);
}else{
if(_828.length==0){
curam.debug.log(_80f.getProperty("curam.util.Refresh.no.dynamic.refresh"),_825);
var ctl=new curam.util.ui.refresh.TabRefreshController(_825,null);
curam.util.Refresh._controllers[_825]=ctl;
}else{
throw "curam.util.Refresh: multiple dynamic refresh "+"configurations found for tab "+_825;
}
}
curam.tab.executeOnTabClose(function(){
curam.util.Refresh._controllers[_825].destroy();
curam.util.Refresh._controllers[_825]=undefined;
},_825);
},getController:function(_82a){
var ctl=curam.util.Refresh._controllers[_82a];
if(!ctl){
throw "Refresh controller for tab '"+_82a+"' not found!";
}
return ctl;
},handleOnloadNestedInlinePage:function(_82b,_82c){
curam.debug.log("curam.util.Refresh.handleOnloadNestedInlinePage "+_80f.getProperty("curam.util.Refresh.iframe",[_82b,_82c]));
var _82d=curam.util.getTopmostWindow();
var _82e=undefined;
var _82f=curam.tab.getSelectedTab();
if(_82f){
_82e=curam.tab.getTabWidgetId(_82f);
}
if(_82e){
curam.debug.log(_80f.getProperty("curam.util.Refresh.parent"),_82e);
_82d.curam.util.Refresh.getController(_82e).pageLoaded(_82c.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
_82d.dojo.publish("/curam/main-content/page/loaded",[_82c.pageID,_82e,_82f]);
return true;
}
return false;
},handleRefreshEvent:function(_830){
var _831=function(_832){
curam.util.ContextPanel.refresh(_809.byId(_832));
};
var _833=function(_834){
curam.tab.refreshMainContentPanel(_809.byId(_834));
};
var _835=function(_836,_837,_838){
curam.util.Refresh.refreshMenuAndNavigation(_836,_837,_838);
};
curam.util.Refresh._doRefresh(_830,_831,_833,_835);
},_doRefresh:function(_839,_83a,_83b,_83c){
var _83d=null;
var _83e=false;
var _83f=false;
var _840=false;
var _841=false;
var trc=curam.util.ui.refresh.TabRefreshController.prototype;
dojo.forEach(_839,function(_842){
var _843=_842.lastIndexOf("/");
var _844=_842.slice(0,_843);
if(!_83d){
_83d=_842.slice(_843+1,_842.length);
}
if(_844==trc.EVENT_REFRESH_MENU){
_83e=true;
}
if(_844==trc.EVENT_REFRESH_NAVIGATION){
_83f=true;
}
if(_844==trc.EVENT_REFRESH_CONTEXT){
_840=true;
}
if(_844==trc.EVENT_REFRESH_MAIN){
_841=true;
}
});
if(_840){
_83a(_83d);
}
if(_841){
_83b(_83d);
}
_83c(_83d,_83e,_83f);
},setupRefreshButton:function(_845){
dojo.ready(function(){
var _846=dojo.query("."+_845)[0];
if(!_846){
throw "Refresh button not found: "+_845;
}
curam.util.Refresh._pageRefreshButton=_846;
var href=window.location.href;
if(curam.util.isActionPage(href)){
var _847=new _810("Refresh");
var _848=_847.getProperty("refresh.button.disabled");
_80a.add(_846,"disabled");
_80b.set(_846,"title",_848);
_80b.set(_846,"aria-label",_848);
_80b.set(_846,"role","link");
_80b.remove(_846,"href");
_80b.set(_846,"aria-disabled","true");
curam.util.Refresh._pageRefreshButton._curamDisable=true;
if(_846.firstChild!=null){
_80a.add(_846.firstChild,"refresh-disabled");
_80b.set(_846.firstChild,"alt",_848);
}
}else{
_80a.add(_846,"enabled");
curam.util.Refresh._pageRefreshButton["_curamDisable"]=undefined;
}
curam.util.getTopmostWindow().curam.util.setupPreferencesLink(href);
});
},refreshPage:function(_849){
dojo.stopEvent(_849);
var href=window.location.href;
var _84a=curam.util.Refresh._pageRefreshButton._curamDisable;
if(_84a){
return;
}
curam.util.FORCE_REFRESH=true;
curam.util.redirectWindow(href,true);
}});
_80c.register("curam/util/Refresh",curam.util.Refresh);
return curam.util.Refresh;
});
},"curam/util/ContextPanel":function(){
define(["dijit/registry","dojo/dom-attr","curam/inspection/Layer","curam/debug","curam/util/onLoad","curam/util","curam/tab","curam/define"],function(_84b,_84c,_84d,_84e,_84f){
curam.define.singleton("curam.util.ContextPanel",{CONTENT_URL_ATTRIB:"data-content-url",setupLoadEventPublisher:function(_850,_851,_852){
curam.util.ContextPanel._doSetup(_850,_851,_852,function(_853){
return _84b.byId(_853);
});
},_doSetup:function(_854,_855,_856,_857){
var _858=curam.util.getTopmostWindow().dojo.subscribe(_854,function(){
var tab=_857(_855);
var _859=curam.util.ContextPanel._getIframe(tab);
if(_859){
curam.tab.executeOnTabClose(function(){
var src=_84c.get(_859,"src");
_84c.set(_859,"src","");
curam.debug.log("curam.util.ContextPanel: Released iframe content for "+src);
},_855);
_84e.log(_84e.getProperty("curam.util.ContextPanel.loaded"));
curam.util.getTopmostWindow().dojo.publish("/curam/frame/detailsPanelLoaded",[{loaded:true},_855]);
_859._finishedLoading=true;
if(_859._scheduledRefresh){
curam.util.ContextPanel.refresh(tab);
_859._scheduledRefresh=false;
}
}
});
_84f.addSubscriber(_856,curam.util.ContextPanel.addTitle);
curam.tab.unsubscribeOnTabClose(_858,_855);
curam.tab.executeOnTabClose(function(){
_84f.removeSubscriber(_856,curam.util.ContextPanel.addTitle);
},_855);
},refresh:function(tab){
var _85a=curam.util.ContextPanel._getIframe(tab);
if(_85a){
curam.debug.log(_84e.getProperty("curam.util.ContextPanel.refresh.prep"));
if(_85a._finishedLoading){
curam.debug.log(_84e.getProperty("curam.util.ContextPanel.refresh"));
_85a._finishedLoading=false;
var doc=_85a.contentDocument||_85a.contentWindow.document;
doc.location.reload(false);
}else{
curam.debug.log(_84e.getProperty("curam.util.ContextPanel.refresh.delay"));
_85a._scheduledRefresh=true;
}
}
},_getIframe:function(tab){
if(tab){
var _85b=dojo.query("iframe.detailsPanelFrame",tab.domNode);
return _85b[0];
}
},addTitle:function(_85c){
var _85d=dojo.query("."+_85c)[0];
var _85e=_85d.contentWindow.document.title;
_85d.setAttribute("title",CONTEXT_PANEL_TITLE+" - "+_85e);
},load:function(tab){
var _85f=curam.util.ContextPanel._getIframe(tab);
if(_85f){
var _860=_84c.get(_85f,curam.util.ContextPanel.CONTENT_URL_ATTRIB);
if(_860&&_860!="undefined"){
_85f[curam.util.ContextPanel.CONTENT_URL_ATTRIB]=undefined;
_84c.set(_85f,"src",_860);
}
}
}});
var _861=curam.util.getTopmostWindow();
if(typeof _861._curamContextPanelTabReadyListenerRegistered!="boolean"){
_861.dojo.subscribe("/curam/application/tab/ready",null,function(_862){
curam.util.ContextPanel.load(_862);
});
_861._curamContextPanelTabReadyListenerRegistered=true;
}
_84d.register("curam/util/ContextPanel",this);
return curam.util.ContextPanel;
});
},"curam/util":function(){
define(["dojo/dom","dijit/registry","dojo/dom-construct","dojo/ready","dojo/_base/window","dojo/dom-style","dojo/_base/array","dojo/dom-class","dojo/topic","dojo/_base/event","dojo/query","dojo/Deferred","dojo/has","dojo/_base/unload","dojo/dom-geometry","dojo/_base/json","dojo/dom-attr","dojo/_base/lang","dojo/on","dijit/_BidiSupport","curam/define","curam/debug","curam/util/RuntimeContext","curam/util/Constants","dojo/_base/sniff","cm/_base/_dom","curam/util/ResourceBundle","dojo/NodeList-traverse"],function(dom,_863,_864,_865,_866,_867,_868,_869,_86a,_86b,_86c,_86d,has,_86e,geom,json,attr,lang,on,bidi,_86f,_870,_871,_872,_873,_874,_875){
curam.define.singleton("curam.util",{PREVENT_CACHE_FLAG:"o3pc",INFORMATIONAL_MSGS_STORAGE_ID:"__informationals__",ERROR_MESSAGES_CONTAINER:"error-messages-container",ERROR_MESSAGES_LIST:"error-messages",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",PAGE_ID_PREFIX:"Curam_",msgLocaleSelectorActionPage:"$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",GENERIC_ERROR_MODAL_MAP:{},wrappersMap:[],lastOpenedTabButton:false,tabButtonClicked:false,secureURLsExemptParamName:"suep",secureURLsExemptParamsPrefix:"spm",secureURLsHashTokenParam:"suhtp",setTabButtonClicked:function(_876){
curam.util.getTopmostWindow().curam.util.tabButtonClicked=_876;
},getTabButtonClicked:function(){
var _877=curam.util.getTopmostWindow().curam.util.tabButtonClicked;
curam.util.getTopmostWindow().curam.util.tabButtonClicked=false;
return _877;
},setLastOpenedTabButton:function(_878){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=_878;
},getLastOpenedTabButton:function(){
var _879=curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=false;
return _879;
},insertCssText:function(_87a,_87b){
var id=_87b?_87b:"_runtime_stylesheet_";
var _87c=dom.byId(id);
var _87d;
if(_87c){
if(_87c.styleSheet){
_87a=_87c.styleSheet.cssText+_87a;
_87d=_87c;
_87d.setAttribute("id","_nodeToRm");
}else{
_87c.appendChild(document.createTextNode(_87a));
return;
}
}
var pa=document.getElementsByTagName("head")[0];
_87c=_864.create("style",{type:"text/css",id:id});
if(_87c.styleSheet){
_87c.styleSheet.cssText=_87a;
}else{
_87c.appendChild(document.createTextNode(_87a));
}
pa.appendChild(_87c);
if(_87d){
_87d.parentNode.removeChild(_87d);
}
},fireRefreshTreeEvent:function(){
if(dojo.global.parent&&dojo.global.parent.amIFrame){
var wpl=dojo.global.parent.loader;
}
if(wpl&&wpl.dojo){
wpl.dojo.publish("refreshTree");
}
},firePageSubmittedEvent:function(_87e){
require(["curam/tab"],function(){
var _87f=curam.tab.getSelectedTab();
if(_87f){
var _880=curam.tab.getTabWidgetId(_87f);
var _881=curam.util.getTopmostWindow();
var ctx=(_87e=="dialog")?curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG:curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
_881.curam.util.Refresh.getController(_880).pageSubmitted(dojo.global.jsPageID,ctx);
_881.dojo.publish("/curam/main-content/page/submitted",[dojo.global.jsPageID,_880]);
}else{
curam.debug.log("/curam/main-content/page/submitted: "+_870.getProperty("curam.util.no.open"));
}
});
},fireTabOpenedEvent:function(_882){
curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",[dojo.global.jsPageID,_882]);
},setupSubmitEventPublisher:function(){
_865(function(){
var form=dom.byId("mainForm");
if(form){
curam.util.connect(form,"onsubmit",function(){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[curam.util.PAGE_ID_PREFIX+dojo.global.jsPageID]);
curam.util.firePageSubmittedEvent("main-content");
});
}
});
},getScrollbar:function(){
var _883=_864.create("div",{},_866.body());
_867.set(_883,{width:"100px",height:"100px",overflow:"scroll",position:"absolute",top:"-300px",left:"0px"});
var test=_864.create("div",{},_883);
_867.set(test,{width:"400px",height:"400px"});
var _884=_883.offsetWidth-_883.clientWidth;
_864.destroy(_883);
return {width:_884};
},isModalWindow:function(){
return (dojo.global.curamModal===undefined)?false:true;
},isExitingIEGScriptInModalWindow:function(_885){
return (curam.util.getTopmostWindow().exitingIEGScript===undefined)?false:true;
},setExitingIEGScriptInModalWindowVariable:function(){
curam.util.getTopmostWindow().exitingIEGScript=true;
},getTopmostWindow:function(){
if(typeof (dojo.global._curamTopmostWindow)=="undefined"){
var _886=dojo.global;
if(_886.__extAppTopWin){
dojo.global._curamTopmostWindow=_886;
}else{
while(_886.parent!=_886){
_886=_886.parent;
if(_886.__extAppTopWin){
break;
}
}
dojo.global._curamTopmostWindow=_886;
}
}
if(dojo.global._curamTopmostWindow.location.href.indexOf("AppController.do")<0&&typeof (dojo.global._curamTopmostWindow.__extAppTopWin)=="undefined"){
curam.debug.log(_870.getProperty("curam.util.wrong.window")+dojo.global._curamTopmostWindow.location.href);
}
return dojo.global._curamTopmostWindow;
},getUrlParamValue:function(url,_887){
var qPos=url.indexOf("?");
if(qPos<0){
return null;
}
var _888=url.substring(qPos+1,url.length);
function _889(_88a){
var _88b=_888.split(_88a);
_887+="=";
for(var i=0;i<_88b.length;i++){
if(_88b[i].indexOf(_887)==0){
return _88b[i].split("=")[1];
}
}
};
return _889("&")||_889("");
},addUrlParam:function(href,_88c,_88d,_88e){
var hasQ=href.indexOf("?")>-1;
var _88f=_88e?_88e:"undefined";
if(!hasQ||(_88f==false)){
return href+(hasQ?"&":"?")+_88c+"="+_88d;
}else{
var _890=href.split("?");
href=_890[0]+"?"+_88c+"="+_88d+(_890[1]!=""?("&"+_890[1]):"");
return href;
}
},replaceUrlParam:function(href,_891,_892){
href=curam.util.removeUrlParam(href,_891);
return curam.util.addUrlParam(href,_891,_892);
},removeUrlParam:function(url,_893,_894){
var qPos=url.indexOf("?");
if(qPos<0){
return url;
}
if(url.indexOf(_893+"=")<0){
return url;
}
var _895=url.substring(qPos+1,url.length);
var _896=_895.split("&");
var _897;
var _898,_899;
for(var i=0;i<_896.length;i++){
if(_896[i].indexOf(_893+"=")==0){
_899=false;
if(_894){
_898=_896[i].split("=");
if(_898.length>1){
if(_898[1]==_894){
_899=true;
}
}else{
if(_894==""){
_899=true;
}
}
}else{
_899=true;
}
if(_899){
_896.splice(i,1);
i--;
}
}
}
return url.substring(0,qPos+1)+_896.join("&");
},stripHash:function(url){
var idx=url.indexOf("#");
if(idx<0){
return url;
}
return url.substring(0,url);
},isSameUrl:function(_89a,_89b,rtc){
if(!_89b){
_89b=rtc.getHref();
}
if(_89a.indexOf("#")==0){
return true;
}
var _89c=_89a.indexOf("#");
if(_89c>-1){
if(_89c==0){
return true;
}
var _89d=_89a.split("#");
var _89e=_89b.indexOf("#");
if(_89e>-1){
if(_89e==0){
return true;
}
_89b=_89b.split("#")[0];
}
return _89d[0]==_89b;
}
var _89f=function(url){
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
var here=curam.util.stripHash(rp(_89b,curam.util.Constants.RETURN_PAGE_PARAM));
var _8a0=curam.util.stripHash(rp(_89a,curam.util.Constants.RETURN_PAGE_PARAM));
var _8a1=_8a0.split("?");
var _8a2=here.split("?");
_8a2[0]=_89f(_8a2[0]);
_8a1[0]=_89f(_8a1[0]);
var _8a3=(_8a2[0]==_8a1[0]||_8a2[0].match(_8a1[0]+"$")==_8a1[0]);
if(!_8a3){
return false;
}
if(_8a2.length==1&&_8a1.length==1&&_8a3){
return true;
}else{
var _8a4;
var _8a5;
if(typeof _8a2[1]!="undefined"&&_8a2[1]!=""){
_8a4=_8a2[1].split("&");
}else{
_8a4=new Array();
}
if(typeof _8a1[1]!="undefined"&&_8a1[1]!=""){
_8a5=_8a1[1].split("&");
}else{
_8a5=new Array();
}
curam.debug.log("curam.util.isSameUrl: paramsHere "+_870.getProperty("curam.util.before")+_8a4.length);
_8a4=_868.filter(_8a4,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_870.getProperty("curam.util.after")+_8a4.length);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_870.getProperty("curam.util.before")+_8a5.length);
_8a5=_868.filter(_8a5,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_870.getProperty("curam.util.after")+_8a5.length);
if(_8a4.length!=_8a5.length){
return false;
}
var _8a6={};
var _8a7;
for(var i=0;i<_8a4.length;i++){
_8a7=_8a4[i].split("=");
_8a7[0]=decodeURIComponent(_8a7[0]);
_8a7[1]=decodeURIComponent(_8a7[1]);
_8a6[_8a7[0]]=_8a7[1];
}
for(var i=0;i<_8a5.length;i++){
_8a7=_8a5[i].split("=");
_8a7[0]=decodeURIComponent(_8a7[0]);
_8a7[1]=decodeURIComponent(_8a7[1]);
if(_8a6[_8a7[0]]!=_8a7[1]){
curam.debug.log(_870.getProperty("curam.util.no.match",[_8a7[0],_8a7[1],_8a6[_8a7[0]]]));
return false;
}
}
}
return true;
},isNotCDEJParam:function(_8a8){
return !((_8a8.charAt(0)=="o"&&_8a8.charAt(1)=="3")||(_8a8.charAt(0)=="_"&&_8a8.charAt(1)=="_"&&_8a8.charAt(2)=="o"&&_8a8.charAt(3)=="3"));
},setAttributes:function(node,map){
for(var x in map){
node.setAttribute(x,map[x]);
}
},invalidatePage:function(){
curam.PAGE_INVALIDATED=true;
var _8a9=dojo.global.dialogArguments?dojo.global.dialogArguments[0]:opener;
if(_8a9&&_8a9!=dojo.global){
try{
_8a9.curam.util.invalidatePage();
}
catch(e){
curam.debug.log(_870.getProperty("curam.util.error"),e);
}
}
},redirectWindow:function(href,_8aa,_8ab){
var rtc=new curam.util.RuntimeContext(dojo.global);
var _8ac=function(_8ad,_8ae,href,_8af,_8b0){
curam.util.getFrameRoot(_8ad,_8ae).curam.util.redirectContentPanel(href,_8af,_8b0);
};
curam.util._doRedirectWindow(href,_8aa,_8ab,dojo.global.jsScreenContext,rtc,curam.util.publishRefreshEvent,_8ac);
},_doRedirectWindow:function(href,_8b1,_8b2,_8b3,rtc,_8b4,_8b5){
if(href&&curam.util.isActionPage(href)&&!curam.util.LOCALE_REFRESH){
curam.debug.log(_870.getProperty("curam.util.stopping"),href);
return;
}
var rpl=curam.util.replaceUrlParam;
var _8b6=_8b3.hasContextBits("TREE")||_8b3.hasContextBits("AGENDA")||_8b3.hasContextBits("ORG_TREE");
if(curam.util.LOCALE_REFRESH){
curam.util.publishRefreshEvent();
curam.util.getTopmostWindow().location.reload();
return;
}else{
if(curam.util.FORCE_REFRESH){
href=rpl(rtc.getHref(),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(curam.util.isModalWindow()||_8b6){
_8b4();
dojo.global.location.href=href;
}else{
if(_8b3.hasContextBits("LIST_ROW_INLINE_PAGE")||_8b3.hasContextBits("NESTED_UIM")){
curam.util._handleInlinePageRefresh(href);
}else{
_8b4();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
require(["curam/tab"],function(){
_8b5(dojo.global,curam.tab.getTabController().ROOT_OBJ,href,true,true);
});
}
}
}
return;
}
}
var u=curam.util;
var rtc=new curam.util.RuntimeContext(dojo.global);
if(!_8b6&&!_8b1&&!curam.PAGE_INVALIDATED&&u.isSameUrl(href,null,rtc)){
return;
}
if(curam.util.isModalWindow()||_8b6){
href=rpl(rpl(href,"o3frame","modal"),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
var form=_864.create("form",{action:href,method:"POST"});
if(!_8b6){
if(!dom.byId("o3ctx")){
form.action=curam.util.removeUrlParam(form.action,"o3ctx");
var _8b7=_864.create("input",{type:"hidden",id:"o3ctx",name:"o3ctx",value:_8b3.getValue()},form);
}
_866.body().appendChild(form);
_8b4();
form.submit();
}
if(!_8b2){
if(_8b6){
curam.util.redirectFrame(href);
}
}
}else{
var _8b8=sessionStorage.getItem("launchWordEdit");
if(!_8b8&&(_8b3.hasContextBits("LIST_ROW_INLINE_PAGE")||_8b3.hasContextBits("NESTED_UIM"))){
curam.util._handleInlinePageRefresh(href);
}else{
if(_8b8){
sessionStorage.removeItem("launchWordEdit");
}
_8b4();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
if(_8b3.hasContextBits("EXTAPP")){
var _8b9=window.top;
_8b9.dijit.byId("curam-app").updateMainContentIframe(href);
}else{
require(["curam/tab"],function(){
curam.util.getFrameRoot(dojo.global,curam.tab.getTabController().ROOT_OBJ).curam.util.redirectContentPanel(href,_8b1);
});
}
}
}
}
},_handleInlinePageRefresh:function(href){
curam.debug.log(_870.getProperty("curam.util.closing.modal"),href);
var _8ba=new curam.ui.PageRequest(href);
require(["curam/tab"],function(){
curam.tab.getTabController().checkPage(_8ba,function(_8bb){
curam.util.publishRefreshEvent();
window.location.reload(false);
});
});
},redirectContentPanel:function(url,_8bc,_8bd){
require(["curam/tab"],function(){
var _8be=curam.tab.getContentPanelIframe();
var _8bf=url;
if(_8be!=null){
var rpu=curam.util.Constants.RETURN_PAGE_PARAM;
var _8c0=null;
if(url.indexOf(rpu+"=")>=0){
curam.debug.log("curam.util.redirectContentPanel: "+_870.getProperty("curam.util.rpu"));
_8c0=decodeURIComponent(curam.util.getUrlParamValue(url,rpu));
}
if(_8c0){
_8c0=curam.util.removeUrlParam(_8c0,rpu);
_8bf=curam.util.replaceUrlParam(url,rpu,encodeURIComponent(_8c0));
}
}
var _8c1=new curam.ui.PageRequest(_8bf);
if(_8bc){
_8c1.forceLoad=true;
}
if(_8bd){
_8c1.justRefresh=true;
}
curam.tab.getTabController().handlePageRequest(_8c1);
});
},redirectFrame:function(href){
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
var _8c2=curam.util.getFrameRoot(dojo.global,"wizard").targetframe;
_8c2.curam.util.publishRefreshEvent();
_8c2.location.href=href;
}else{
if(dojo.global.jsScreenContext.hasContextBits("ORG_TREE")){
var _8c2=curam.util.getFrameRoot(dojo.global,"orgTreeRoot");
_8c2.curam.util.publishRefreshEvent();
_8c2.dojo.publish("orgTree.refreshContent",[href]);
}else{
var _8c3=curam.util.getFrameRoot(dojo.global,"iegtree");
var _8c4=_8c3.navframe||_8c3.frames[0];
var _8c5=_8c3.contentframe||_8c3.frames["contentframe"];
_8c5.curam.util.publishRefreshEvent();
if(curam.PAGE_INVALIDATED||_8c4.curam.PAGE_INVALIDATED){
var _8c6=curam.util.modifyUrlContext(href,"ACTION");
_8c5.location.href=_8c6;
}else{
_8c5.location.href=href;
}
}
}
return true;
},publishRefreshEvent:function(){
_86a.publish("/curam/page/refresh");
},openGenericErrorModalDialog:function(_8c7,_8c8,_8c9,_8ca,_8cb){
var _8cc=curam.util.getTopmostWindow();
_8cc.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_8c7,"titleInfo":_8c8,"msg":_8c9,"msgPlaceholder":_8ca,"errorModal":_8cb,"hasCancelButton":false};
var url="generic-modal-error.jspx";
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=true;
curam.util.openModalDialog({href:encodeURI(url)},_8c7);
},openGenericErrorModalDialogYesNo:function(_8cd,_8ce,_8cf){
var sc=dojo.global.jsScreenContext;
var _8d0=curam.util.getTopmostWindow();
sc.addContextBits("MODAL");
_8d0.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_8cd,"titleInfo":_8ce,"msg":_8cf,"msgPlaceholder":"","errorModal":false,"hasCancelButton":true};
var url="generic-modal-error.jspx?"+sc.toRequestString();
curam.util.openModalDialog({href:encodeURI(url)},_8cd);
},addPlaceholderFocusClassToEventOrAnchorTag:function(_8d1,_8d2){
var _8d3=curam.util.getTopmostWindow();
_8d3.curam.util.PLACEHOLDER_WINDOW_LIST=!_8d3.curam.util.PLACEHOLDER_WINDOW_LIST?[]:_8d3.curam.util.PLACEHOLDER_WINDOW_LIST;
_869.add(_8d1,"placeholder-for-focus");
_8d3.curam.util.PLACEHOLDER_WINDOW_LIST.push(_8d2);
},returnFocusToPopupActionAnchorElement:function(_8d4){
var _8d5=_8d4.curam.util.PLACEHOLDER_WINDOW_LIST;
if(_8d5&&_8d5.length>0){
var _8d6=_8d5.pop();
var _8d7=_8d6&&_8d6.document.activeElement;
var _8d8=_8d7&&dojo.query(".placeholder-for-focus",_8d7);
if(_8d8&&_8d8.length==1){
_8d8[0].focus();
_869.remove(_8d8[0],"placeholder-for-focus");
}
_8d4.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0,_8d4.curam.util.PLACEHOLDER_WINDOW_LIST.length);
_8d4.curam.util.PLACEHOLDER_WINDOW_LIST=null;
}
},openModalDialog:function(_8d9,_8da,left,top,_8db){
_8d9.event&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_8d9.event,_8d9.event.ownerDocument.defaultView.window);
var href;
if(!_8d9||!_8d9.href){
_8d9=_86b.fix(_8d9);
var _8dc=_8d9.target;
while(_8dc.tagName!="A"&&_8dc!=_866.body()){
_8dc=_8dc.parentNode;
}
href=_8dc.href;
_8dc._isModal=true;
_86b.stop(_8d9);
}else{
href=_8d9.href;
_8d9._isModal=true;
}
require(["curam/dialog"]);
var opts=curam.dialog.parseWindowOptions(_8da);
curam.util.showModalDialog(href,_8d9,opts["width"],opts["height"],left,top,false,null,null,_8db);
return true;
},showModalDialog:function(url,_8dd,_8de,_8df,left,top,_8e0,_8e1,_8e2,_8e3){
var _8e4=curam.util.getTopmostWindow();
if(dojo.global!=_8e4){
curam.debug.log("curam.util.showModalDialog: "+_870.getProperty("curam.util.redirecting.modal"));
_8e4.curam.util.showModalDialog(url,_8dd,_8de,_8df,left,top,_8e0,_8e1,dojo.global,_8e3);
return;
}
var rup=curam.util.replaceUrlParam;
url=rup(url,"o3frame","modal");
url=curam.util.modifyUrlContext(url,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
url=rup(url,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
curam.debug.log(_870.getProperty("curam.util.modal.url"),url);
if(_8de){
_8de=typeof (_8de)=="number"?_8de:parseInt(_8de);
}
if(_8df){
_8df=typeof (_8df)=="number"?_8df:parseInt(_8df);
}
if(!curam.util._isModalCurrentlyOpening()){
if(url.indexOf("__o3rpu=about%3Ablank")>=0){
alert(curam_util_showModalDialog_pageStillLoading);
return;
}
curam.util._setModalCurrentlyOpening(true);
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
new curam.ModalDialog({href:url,width:_8de,height:_8df,openNode:(_8dd&&_8dd.target)?_8dd.target:null,parentWindow:_8e2,uimToken:_8e3});
}else{
require(["curam/modal/CuramCarbonModal"]);
new curam.modal.CuramCarbonModal({href:url,width:_8de,height:_8df,openNode:(_8dd&&_8dd.target)?_8dd.target:null,parentWindow:_8e2,uimToken:_8e3});
}
return true;
}
},showModalDialogWithRef:function(_8e5,_8e6,_8e7){
var _8e8=curam.util.getTopmostWindow();
if(dojo.global!=_8e8){
return _8e8.curam.util.showModalDialogWithRef(_8e5,dojo.global);
}
var rup=curam.util.replaceUrlParam;
_8e5=curam.util.modifyUrlContext(_8e5,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
_8e5=rup(_8e5,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(!curam.util._isModalCurrentlyOpening()){
curam.util._setModalCurrentlyOpening(true);
var dl;
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
if(_8e7){
dl=new curam.ModalDialog({href:_8e5,width:_8e7.width,height:_8e7.height,parentWindow:_8e6});
}else{
dl=new curam.ModalDialog({href:_8e5,parentWindow:_8e6});
}
}else{
require(["curam/modal/CuramCarbonModal"]);
if(_8e7){
dl=new curam.modal.CuramCarbonModal({href:_8e5,width:_8e7.width,height:_8e7.height,parentWindow:_8e6});
}else{
dl=new curam.modal.CuramCarbonModal({href:_8e5,parentWindow:_8e6});
}
}
return dl;
}
},_isModalCurrentlyOpening:function(){
return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
},_setModalCurrentlyOpening:function(_8e9){
curam.util.getTopmostWindow().curam.util._modalOpenInProgress=_8e9;
},setupPreferencesLink:function(href){
_865(function(){
var _8ea=_86c(".user-preferences")[0];
if(_8ea){
if(typeof (_8ea._disconnectToken)=="undefined"){
_8ea._disconnectToken=curam.util.connect(_8ea,"onclick",curam.util.openPreferences);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_870.getProperty("curam.util.no.setup"));
}
});
},setupLocaleLink:function(href){
_865(function(){
var _8eb=_86c(".user-locale")[0];
if(_8eb){
if(typeof (_8eb._disconnectToken)=="undefined"){
_8eb._disconnectToken=curam.util.connect(_8eb,"onclick",curam.util.openLocaleNew);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_870.getProperty("curam.util.no.setup"));
}
});
},openPreferences:function(_8ec){
_86b.stop(_8ec);
if(_8ec.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-prefs-editor.jspx",{dialogOptions:"width=605"});
});
},logout:function(_8ed){
var _8ee=curam.util.getTopmostWindow();
_8ee.dojo.publish("curam/redirect/logout");
window.location.href=jsBaseURL+"/logout.jsp";
},openAbout:function(_8ef){
_86b.stop(_8ef);
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("about.jsp",{dialogOptions:"width=583,height=399"});
});
},addMinWidthCalendarCluster:function(id){
var _8f0=dom.byId(id);
var i=0;
function _8f1(evt){
_868.forEach(_8f0.childNodes,function(node){
if(_869.contains(node,"cluster")){
_867.set(node,"width","97%");
if(node.clientWidth<700){
_867.set(node,"width","700px");
}
}
});
};
if(has("ie")>6){
_868.forEach(_8f0.childNodes,function(node){
if(_869.contains(node,"cluster")){
_867.set(node,"minWidth","700px");
}
});
}else{
on(dojo.global,"resize",_8f1);
_865(_8f1);
}
},addPopupFieldListener:function(id){
if(!has("ie")||has("ie")>6){
return;
}
if(!curam.util._popupFields){
function _8f2(evt){
var _8f3=0;
var j=0;
var x=0;
var arr=curam.util._popupFields;
_868.forEach(curam.util._popupFields,function(id){
var _8f4=dom.byId(id);
_86c("> .popup-actions",_8f4).forEach(function(node){
_8f3=node.clientWidth+30;
});
_86c("> .desc",_8f4).forEach(function(node){
_867.set(node,"width",Math.max(0,_8f4.clientWidth-_8f3)+"px");
});
});
};
curam.util._popupFields=[id];
on(dojo.global,"resize",_8f2);
_865(_8f2);
}else{
curam.util._popupFields.push(id);
}
},addContentWidthListener:function(id){
if(has("ie")>6){
return;
}
var _8f5=_867.set;
var _8f6=_869.contains;
function _8f7(evt){
var i=0;
var _8f8=dom.byId("content");
if(_8f8){
var _8f9=_8f8.clientWidth;
if(has("ie")==6&&dom.byId("footer")){
var _8fa=_866.body().clientHeight-100;
_8f5(_8f8,"height",_8fa+"px");
var _8fb=dom.byId("sidebar");
if(_8fb){
_8f5(_8fb,"height",_8fa+"px");
}
}
try{
_86c("> .page-title-bar",_8f8).forEach(function(node){
var _8fc=geom.getMarginSize(node).w-geom.getContentBox(node).w;
if(!has("ie")){
_8fc+=1;
}
_8f9=_8f8.clientWidth-_8fc;
_867.set(node,"width",_8f9+"px");
});
}
catch(e){
}
_86c("> .page-description",_8f8).style("width",_8f9+"px");
_86c("> .in-page-navigation",_8f8).style("width",_8f9+"px");
}
};
curam.util.subscribe("/clusterToggle",_8f7);
curam.util.connect(dojo.global,"onresize",_8f7);
_865(_8f7);
},alterScrollableListBottomBorder:function(id,_8fd){
var _8fe=_8fd;
var _8ff="#"+id+" table";
function _900(){
var _901=_86c(_8ff)[0];
if(_901.offsetHeight>=_8fe){
var _902=_86c(".odd-last-row",_901)[0];
if(typeof _902!="undefined"){
_869.add(_902,"no-bottom-border");
}
}else{
if(_901.offsetHeight<_8fe){
var _902=_86c(".even-last-row",_901)[0];
if(typeof _902!="undefined"){
_869.add(_902,"add-bottom-border");
}
}else{
curam.debug.log("curam.util.alterScrollableListBottomBorder: "+_870.getProperty("curam.util.code"));
}
}
};
_865(_900);
},addFileUploadResizeListener:function(code){
function _903(evt){
if(_86c(".widget")){
_86c(".widget").forEach(function(_904){
var _905=_904.clientWidth;
if(_86c(".fileUpload",_904)){
_86c(".fileUpload",_904).forEach(function(_906){
fileUploadWidth=_905/30;
if(fileUploadWidth<4){
_906.size=1;
}else{
_906.size=fileUploadWidth;
}
});
}
});
}
};
on(dojo.global,"resize",_903);
_865(_903);
},openCenteredNonModalWindow:function(url,_907,_908,name){
_907=Number(_907);
_908=Number(_908);
var _909=(screen.width-_907)/2;
var _90a=(screen.height-_908)/2;
_908=_90a<0?screen.height:_908;
_90a=Math.max(0,_90a);
_907=_909<0?screen.width:_907;
_909=Math.max(0,_909);
var left="left",top="top";
if(has("ff")){
left="screenX",top="screenY";
}
var _90b="location=no, menubar=no, status=no, toolbar=no, "+"scrollbars=yes, resizable=no";
var _90c=dojo.global.open(url,name||"name","width="+_907+", height="+_908+", "+left+"="+_909+","+top+"="+_90a+","+_90b);
_90c.resizeTo(_907,_908);
_90c.moveTo(_909,_90a);
_90c.focus();
},adjustTargetContext:function(win,href){
if(win&&win.dojo.global.jsScreenContext){
var _90d=win.dojo.global.jsScreenContext;
_90d.updateStates(dojo.global.jsScreenContext);
return curam.util.replaceUrlParam(href,"o3ctx",_90d.getValue());
}
return href;
},modifyUrlContext:function(url,_90e,_90f){
var _910=url;
var ctx=new curam.util.ScreenContext();
var _911=curam.util.getUrlParamValue(url,"o3ctx");
if(_911){
ctx.setContext(_911);
}else{
ctx.clear();
}
if(_90e){
ctx.addContextBits(_90e);
}
if(_90f){
ctx.clear(_90f);
}
_910=curam.util.replaceUrlParam(url,"o3ctx",ctx.getValue());
return _910;
},updateCtx:function(_912){
var _913=curam.util.getUrlParamValue(_912,"o3ctx");
if(!_913){
return _912;
}
return curam.util.modifyUrlContext(_912,null,"MODAL");
},getFrameRoot:function(_914,_915){
var _916=false;
var _917=_914;
if(_917){
while(_917!=top&&!_917.rootObject){
_917=_917.parent;
}
if(_917.rootObject){
_916=(_917.rootObject==_915);
}
}
return _916?_917:null;
},saveInformationalMsgs:function(_918){
try{
localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID]=json.toJson({pageID:_866.body().id,total:dom.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,listItems:dom.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML});
_918();
}
catch(e){
curam.debug.log(_870.getProperty("curam.util.exception"),e);
}
},disableInformationalLoad:function(){
curam.util._informationalsDisabled=true;
},redirectDirectUrl:function(){
_865(function(){
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
_865(function(){
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
if(msgs.pageID!=_866.body().id){
return;
}
if(list){
var _919=_864.create("ul",{innerHTML:msgs.listItems});
var _91a=[];
for(var i=0;i<list.childNodes.length;i++){
if(list.childNodes[i].tagName=="LI"){
_91a.push(list.childNodes[i]);
}
}
var skip=false;
var _91b=_919.childNodes;
for(var i=0;i<_91b.length;i++){
skip=false;
for(var j=0;j<_91a.length;j++){
if(_91b[i].innerHTML==_91a[j].innerHTML){
skip=true;
break;
}
}
if(!skip){
list.appendChild(_91b[i]);
i--;
}
}
}else{
if(div){
div.innerHTML=msgs.total;
}
}
}
var _91c=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_91c&&!dojo.global.jsScreenContext.hasContextBits("MODAL")){
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked){
curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
setTimeout(function(){
_91c.innerHTML=_91c.innerHTML+" ";
},500);
}else{
_91c.focus();
}
}
var _91d=dom.byId("error-messages-container-wrapper");
if(_91d){
var _91e=_86c("#container-messages-ul",_91d)[0];
if(_91e){
_91e.focus();
}
}
});
},_setFocusCurrentIframe:function(){
var _91f=/Edg/.test(navigator.userAgent);
if(_91f){
var _920=window.frameElement;
if(_920){
_920.setAttribute("tabindex","0");
_920.focus();
setTimeout(function(){
_920.removeAttribute("tabindex");
},10);
}
}
},setFocus:function(){
var _921;
if(window.document.getElementsByClassName("skeleton").length>0){
_921=setTimeout(function(){
curam.util.setFocus();
},300);
}else{
if(_921){
clearTimeout(_921);
}
var _922=curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
if(_922){
return;
}
curam.util._setFocusCurrentIframe();
var _923=curam.util.getUrlParamValue(dojo.global.location.href,"o3frame")=="modal"||(curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==null&&curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==undefined);
if(!_923){
_865(function(){
var _924=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
var _925=sessionStorage.getItem("curamDefaultActionId");
var _926=null;
if(!_924&&_925){
sessionStorage.removeItem("curamDefaultActionId");
_926=dojo.query(".curam-default-action")[0].previousSibling;
}else{
_926=curam.util.doSetFocus();
}
if(_926){
curam.util.setFocusOnField(_926,false);
}else{
window.focus();
}
});
}
}
},setFocusOnField:function(_927,_928,_929){
if(has("IE")||has("trident")){
var _92a=1000;
var _92b=_928?200:500;
curam.util._createHiddenInputField(_927);
var _92c=function(ff){
return function(){
var _92d=ff.ownerDocument.activeElement;
if(_92d.tagName==="INPUT"&&!_92d.classList.contains("hidden-focus-input")||_92d.tagName==="TEXTAREA"||(_92d.tagName==="SPAN"&&_92d.className=="fileUploadButton")||(_92d.tagName==="A"&&_92d.className=="popup-action")||(_92d.tagName==="IFRAME"&&_92d.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
if(_928){
var _92e=attr.get(_927,"aria-label");
var _92f="";
var _930=attr.get(_927,"objid");
if(_930&&_930.indexOf("component")==0||_869.contains(_927,"dijitReset dijitInputInner")){
_92f=_927.title;
}else{
_92f=_929||"Modal Dialog";
}
if(_927&&_927.id!=="container-messages-ul"){
attr.set(_927,"aria-label",_92f);
}
var _931=function(_932){
return function(e){
_86c("input|select[aria-label="+_92f+"]").forEach(function(_933){
_932&&attr.set(_933,"aria-label",_932);
!_932&&attr.remove(_933,"aria-label");
});
};
};
on(_927,"blur",_931(_92e));
}
if(_927.tagName==="TEXTAREA"){
setTimeout(_92c(_927),_92a);
}else{
if(_927.tagName==="SELECT"||(_927.tagName==="INPUT"&&attr.get(_927,"type")==="text")){
setTimeout(_92c(_927),_92b);
}else{
_927.focus();
}
}
}else{
_927.focus();
}
},_createHiddenInputField:function(_934){
var _935=_934.ownerDocument.forms["mainForm"];
if(_935&&(_934.tagName==="SELECT"||(_934.tagName==="INPUT"&&attr.get(_934,"type")==="text"))){
var _936=_864.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_864.place(_936,_935,"before");
_936.focus();
on(_936,"blur",function(){
_864.destroy(_936);
});
}
},doSetFocus:function(){
try{
var _937=curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
if(_937!=false&&!curam.util.isModalWindow()){
return _937;
}
var _938=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_938){
return _938;
}
var form=document.forms[0];
if(!form){
return false;
}
var _939=form.querySelectorAll("button, output, input:not([type=\"image\"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton");
var _93a=false;
var l=_939.length,el;
for(var i=0;i<l;i++){
el=_939[i];
if(!_93a&&/selectHook/.test(el.className)){
_93a=_86c(el).closest("table")[0];
}
if(!_93a&&!(el.style.visibility=="hidden")&&(/select-one|select-multiple|checkbox|radio|text/.test(el.type)||el.tagName=="TEXTAREA"||/popup-action|fileUploadButton/.test(el.className))&&!/dijitArrowButtonInner|dijitValidationInner/.test(el.className)){
_93a=el;
}
if(el.tabIndex=="1"){
el.tabIndex=0;
_93a=el;
break;
}
}
lastOpenedTabButton=curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
if(!_93a&&lastOpenedTabButton){
return lastOpenedTabButton;
}
var _93b=_93a.classList.contains("bx--date-picker__input");
if(_93b){
var _93c=document.querySelector(".bx--uim-modal");
if(_93c){
_93a=_93c;
}
}
return _93a;
}
catch(e){
_870.log(_870.getProperty("curam.util.error.focus"),e.message);
return false;
}
return false;
},openLocaleSelector:function(_93d){
_93d=_86b.fix(_93d);
var _93e=_93d.target;
while(_93e&&_93e.tagName!="A"){
_93e=_93e.parentNode;
}
var loc=_93e.href;
var rpu=curam.util.getUrlParamValue(loc,"__o3rpu");
rpu=curam.util.removeUrlParam(rpu,"__o3rpu");
var href="user-locale-selector.jspx"+"?__o3rpu="+rpu;
if(!curam.util.isActionPage(dojo.global.location.href)){
openModalDialog({href:href},"width=500,height=300",200,150);
}else{
alert(curam.util.msgLocaleSelectorActionPage);
}
return false;
},openLocaleNew:function(_93f){
_86b.stop(_93f);
if(_93f.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-locale-selector.jspx",{dialogOptions:"width=300"});
});
},isActionPage:function(url){
var _940=curam.util.getLastPathSegmentWithQueryString(url);
var _941=_940.split("?")[0];
return _941.indexOf("Action.do")>-1;
},closeLocaleSelector:function(_942){
_942=_86b.fix(_942);
_86b.stop(_942);
dojo.global.close();
return false;
},getSuffixFromClass:function(node,_943){
var _944=attr.get(node,"class").split(" ");
var _945=_868.filter(_944,function(_946){
return _946.indexOf(_943)==0;
});
if(_945.length>0){
return _945[0].split(_943)[1];
}else{
return null;
}
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},stripeTable:function(_947,_948,_949){
var _94a=_947.tBodies[0];
var _94b=(_948?2:1);
if(_94a.rows.length<_94b){
return;
}
var rows=_94a.rows;
var _94c=[],_94d=[],_94e=false,_94f=[],_950="";
for(var i=0,l=rows.length;i<l;i+=_94b){
var _951=(i%(2*_94b)==0),_952=_951?_94d:_94c;
_869.remove(rows[i],["odd-last-row","even-last-row"]);
_952.push(rows[i]);
if(i==_949){
_94f.push(rows[i]);
_950=_951?"odd":"even";
_94e=true;
}
if(_948&&rows[i+1]){
_869.remove(rows[i+1],["odd-last-row","even-last-row"]);
_952.push(rows[i+1]);
_94e&&_94f.push(rows[i+1]);
}
_94e=false;
}
_94d.forEach(function(_953){
_869.replace(_953,"odd","even");
});
_94c.forEach(function(_954){
_869.replace(_954,"even","odd");
});
_94f.forEach(function(_955){
_869.add(_955,_950+"-last-row");
});
},fillString:function(_956,_957){
var _958="";
while(_957>0){
_958+=_956;
_957-=1;
}
return _958;
},updateHeader:function(qId,_959,_95a,_95b){
var _95c=dom.byId("header_"+qId);
_95c.firstChild.nextSibling.innerHTML=_959;
answerCell=dom.byId("chosenAnswer_"+qId);
answerCell.innerHTML=_95a;
sourceCell=dom.byId("chosenSource_"+qId);
sourceCell.innerHTML=_95b;
},search:function(_95d,_95e){
var _95f=_863.byId(_95d).get("value");
var _960=_863.byId(_95e);
if(_960==null){
_960=dom.byId(_95e);
}
var _961=null;
if(_960!=null){
if(_960.tagName==null){
_961=_960?_960.get("value"):null;
}else{
if(_960.tagName=="SELECT"){
var _962=_86c(".multiple-search-banner select option");
_868.forEach(_962,function(elem){
if(elem.selected){
_961=elem.value;
}
});
}
}
}
var _963="";
var _964;
var _965;
if(_961){
_965=_961.split("|");
_963=_965[0];
_964=_965[1];
}
var _966=curam.util.defaultSearchPageID;
var _967="";
if(sessionStorage.getItem("appendSUEP")==="true"){
if(_963===""){
_967=_966+"Page.do?searchText="+encodeURIComponent(_95f)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1");
}else{
_967=_964+"Page.do?searchText="+encodeURIComponent(_95f)+"&searchType="+encodeURIComponent(_963)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1,"+curam.util.secureURLsExemptParamsPrefix+"_ST2");
}
}else{
if(_963===""){
_967=_966+"Page.do?searchText="+encodeURIComponent(_95f);
}else{
_967=_964+"Page.do?searchText="+encodeURIComponent(_95f)+"&searchType="+encodeURIComponent(_963);
}
}
var _968=new curam.ui.PageRequest(_967);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_968);
});
},updateDefaultSearchText:function(_969,_96a){
var _96b=_863.byId(_969);
var _96c=_863.byId(_96a);
var _96d=_96b?_96b.get("value"):null;
var str=_96d.split("|")[2];
_96c.set("placeHolder",str);
_86a.publish("curam/application-search/combobox-changed",_96d);
},updateSearchBtnState:function(_96e,_96f){
var _970=_863.byId(_96e);
var btn=dom.byId(_96f);
var _971=_970.get("value");
if(!_971||lang.trim(_971).length<1){
_869.add(btn,"dijitDisabled");
}else{
_869.remove(btn,"dijitDisabled");
}
},furtherOptionsSearch:function(){
var _972=curam.util.furtherOptionsPageID+"Page.do";
var _973=new curam.ui.PageRequest(_972);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_973);
});
},searchButtonStatus:function(_974){
var btn=dom.byId(_974);
if(!_869.contains(btn,"dijitDisabled")){
return true;
}
},getPageHeight:function(){
var _975=400;
var _976=0;
if(_86c("frameset").length>0){
curam.debug.log("curam.util.getPageHeight() "+_870.getProperty("curam.util.default.height"),_975);
_976=_975;
}else{
var _977=function(node){
if(!node){
curam.debug.log(_870.getProperty("curam.util.node"));
return 0;
}
var mb=geom.getMarginSize(node);
var pos=geom.position(node);
return pos.y+mb.h;
};
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
var _978=_86c("div#content")[0];
var _979=_977(_978);
curam.debug.log(_870.getProperty("curam.util.page.height"),_979);
_976=_979;
}else{
var _97a=dom.byId("content")||dom.byId("wizard-content");
var _97b=_86c("> *",_97a).filter(function(n){
return n.tagName.indexOf("SCRIPT")<0&&_867.get(n,"visibility")!="hidden"&&_867.get(n,"display")!="none";
});
var _97c=_97b[0];
for(var i=1;i<_97b.length;i++){
if(_977(_97b[i])>=_977(_97c)){
_97c=_97b[i];
}
}
_976=_977(_97c);
curam.debug.log("curam.util.getPageHeight() "+_870.getProperty("curam.util.base.height"),_976);
var _97d=_86c(".actions-panel",_866.body());
if(_97d.length>0){
var _97e=geom.getMarginBox(_97d[0]).h;
curam.debug.log("curam.util.getPageHeight() "+_870.getProperty("curam.util.panel.height"));
_976+=_97e;
_976+=10;
}
var _97f=_86c("body.details");
if(_97f.length>0){
curam.debug.log("curam.util.getPageHeight() "+_870.getProperty("curam.util.bar.height"));
_976+=20;
}
}
}
curam.debug.log("curam.util.getPageHeight() "+_870.getProperty("curam.util.returning"),_976);
return _976;
},toCommaSeparatedList:function(_980){
var _981="";
for(var i=0;i<_980.length;i++){
_981+=_980[i];
if(i<_980.length-1){
_981+=",";
}
}
return _981;
},setupGenericKeyHandler:function(){
_865(function(){
var f=function(_982){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")&&_982.keyCode==27){
var ev=_86b.fix(_982);
var _983=_863.byId(ev.target.id);
var _984=typeof _983!="undefined"&&_983.baseClass=="dijitTextBox dijitComboBox";
if(!_984){
curam.dialog.closeModalDialog();
}
}
if(_982.keyCode==13){
var ev=_86b.fix(_982);
var _985=ev.target.type=="text";
var _986=ev.target.type=="radio";
var _987=ev.target.type=="checkbox";
var _988=ev.target.type=="select-multiple";
var _989=ev.target.type=="password";
var _98a=_863.byId(ev.target.id);
if(typeof _98a!="undefined"){
var _98b=_863.byId(ev.target.id);
if(!_98b){
_98b=_863.byNode(dom.byId("widget_"+ev.target.id));
}
if(_98b&&_98b.enterKeyOnOpenDropDown){
_98b.enterKeyOnOpenDropDown=false;
return false;
}
}
var _98c=ev.target.getAttribute("data-carbon-attach-point");
if(_98c&&_98c==="carbon-menu"){
return false;
}
var _98d=typeof _98a!="undefined"&&_98a.baseClass=="dijitComboBox";
if((!_985&&!_986&&!_987&&!_988&&!_989)||_98d){
return true;
}
var _98e=null;
var _98f=_86c(".curam-default-action");
if(_98f.length>0){
_98e=_98f[0];
}else{
var _990=_86c("input[type='submit']");
if(_990.length>0){
_98e=_990[0];
}
}
if(_98e!=null){
_86b.stop(_86b.fix(_982));
curam.util.clickButton(_98e);
return false;
}
require(["curam/dateSelectorUtil"],function(_991){
var _992=dom.byId("year");
if(_992){
dojo.stopEvent(dojo.fixEvent(_982));
_991.updateCalendar();
}
});
}
return true;
};
curam.util.connect(_866.body(),"onkeyup",f);
});
},enterKeyPress:function(_993){
if(_993.keyCode==13){
return true;
}
},swapState:function(node,_994,_995,_996){
if(_994){
_869.replace(node,_995,_996);
}else{
_869.replace(node,_996,_995);
}
},makeQueryString:function(_997){
if(!_997||_997.length==0){
return "";
}
var _998=[];
for(var _999 in _997){
_998.push(_999+"="+encodeURIComponent(_997[_999]));
}
return "?"+_998.join("&");
},fileDownloadAnchorHandler:function(url){
var _99a=curam.util.getTopmostWindow();
var _99b=_99a.dojo.subscribe("/curam/dialog/close",function(id,_99c){
if(_99c==="confirm"){
curam.util.clickHandlerForListActionMenu(url,false,false);
}
_99a.dojo.unsubscribe(_99b);
});
var _99d=new _875("GenericModalError");
var _99e=_99d.getProperty("file.download.warning.dialog.width");
var _99f=_99d.getProperty("file.download.warning.dialog.height");
if(!_99e){
_99e=500;
}
if(!_99f){
_99f=225;
}
var _9a0=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_99e+",height="+_99f,"file.download.warning.title","file.download.warning."+_9a0);
return false;
},fileDownloadListActionHandler:function(url,_9a1,_9a2,_9a3){
var _9a4=curam.util.getTopmostWindow();
var _9a5=_9a4.dojo.subscribe("/curam/dialog/close",function(id,_9a6){
if(_9a6==="confirm"){
curam.util.clickHandlerForListActionMenu(url,_9a1,_9a2,_9a3);
}
_9a4.dojo.unsubscribe(_9a5);
});
var _9a7=new _875("GenericModalError");
var _9a8=_9a7.getProperty("file.download.warning.dialog.width");
var _9a9=_9a7.getProperty("file.download.warning.dialog.height");
if(!_9a8){
_9a8=500;
}
if(!_9a9){
_9a9=225;
}
var _9aa=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_9a8+",height="+_9a9,"file.download.warning.title","file.download.warning."+_9aa);
},_getBrowserName:function(){
var _9ab=has("trident");
var _9ac=dojo.isFF;
var _9ad=dojo.isChrome;
var _9ae=dojo.isSafari;
var _9af=curam.util.getTopmostWindow();
var _9b0=curam.util.ExpandableLists._isExternalApp(_9af);
if(_9ab!=undefined){
var _9b1=_9ab+4;
if(_9b1<8){
return "unknown.browser";
}else{
return "ie"+_9b1;
}
}else{
if(_9ac!=undefined&&_9b0){
return "firefox";
}else{
if(_9ad!=undefined){
return "chrome";
}else{
if(_9ae!=undefined&&_9b0){
return "safari";
}
}
}
}
return "unknown.browser";
},clickHandlerForListActionMenu:function(url,_9b2,_9b3,_9b4){
if(_9b2){
var href=curam.util.replaceUrlParam(url,"o3frame","modal");
var ctx=dojo.global.jsScreenContext;
ctx.addContextBits("MODAL");
href=curam.util.replaceUrlParam(href,"o3ctx",ctx.getValue());
curam.util.redirectWindow(href);
return;
}
var _9b5={href:url};
require(["curam/ui/UIMPageAdaptor"]);
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_9b5)){
if(_9b5.href.indexOf("/servlet/FileDownload")){
sessionStorage.setItem("addOnUnloadTriggeredByFileDownload","true");
dojo.global.location=url;
sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
}else{
dojo.global.location=url;
}
return;
}
if(_9b5!=null){
if(_9b4){
_86b.fix(_9b4);
_86b.stop(_9b4);
}
if(!_9b5.href||_9b5.href.length==0){
return;
}
if(_9b3&&!curam.util.isInternal(url)){
dojo.global.open(url);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_9b5)){
var _9b6=new curam.ui.PageRequest(_9b5.href);
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")){
_9b6.pageHolder=dojo.global;
}
require(["curam/tab"],function(){
var _9b7=curam.tab.getTabController();
if(_9b7){
_9b7.handlePageRequest(_9b6);
}
});
}
}
}
},clickHandlerForMailtoLinks:function(_9b8,url){
dojo.stopEvent(_9b8);
var _9b9=dojo.query("#mailto_frame")[0];
if(!_9b9){
_9b9=dojo.io.iframe.create("mailto_frame","");
}
_9b9.src=url;
return false;
},isInternal:function(url){
var path=url.split("?")[0];
var _9ba=path.match("Page.do");
if(_9ba!=null){
return true;
}
return false;
},getLastPathSegmentWithQueryString:function(url){
var _9bb=url.split("?");
var _9bc=_9bb[0].split("/");
return _9bc[_9bc.length-1]+(_9bb[1]?"?"+_9bb[1]:"");
},replaceSubmitButton:function(name,_9bd,_9be,_9bf,_9c0){
if(curam.replacedButtons[name]=="true"){
return;
}
var _9c1="__o3btn."+name;
var _9c2;
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_9c2=_86c("input[id='"+_9c1+"']");
}else{
_9c2=_86c("input[name='"+_9c1+"']");
}
_9c2.forEach(function(_9c3,_9c4,_9c5){
if(_9bd){
var _9c6=_9c5[1];
_9c6.setAttribute("value",_9bd);
}
_9c3.tabIndex=-1;
var _9c7=_9c3.parentNode;
var _9c8;
var _9c9=curam.util.isInternalModal()&&curam.util.isModalFooter(_9c3);
var _9ca="btn-id-"+_9c4;
var _9cb="ac initially-hidden-widget "+_9ca;
if(_869.contains(_9c3,"first-action-control")){
_9cb+=" first-action-control";
}
if(_9c9){
var _9cc=(_9be&&!_9c0)?undefined:_9c5[0];
var _9cd=_9be?{"href":"","buttonid":_9bf}:{"buttonid":_9bf};
var _9ce=_9c3.getAttribute("data-rawtestid");
if(_9ce){
_9cd.dataTestId=_9ce;
}
var _9cf=_869.contains(_9c3,"curam-default-action")?true:false;
curam.util.addCarbonModalButton(_9cd,_9c3.value,_9cc,_9cf);
}else{
curam.util.setupWidgetLoadMask("a."+_9ca);
var _9cb="ac initially-hidden-widget "+_9ca;
if(_869.contains(_9c3,"first-action-control")){
_9cb+=" first-action-control";
}
var _9c8=_864.create("a",{"class":_9cb,href:"#"},_9c3,"before");
var _9d0=dojo.query(".page-level-menu")[0];
if(_9d0){
attr.set(_9c8,"title",_9c3.value);
}
_864.create("span",{"class":"filler"},_9c8,"before");
var left=_864.create("span",{"class":"left-corner"},_9c8);
var _9d1=_864.create("span",{"class":"right-corner"},left);
var _9d2=_864.create("span",{"class":"middle"},_9d1);
_9d2.appendChild(document.createTextNode(_9c3.value));
curam.util.addActionControlClass(_9c8);
}
if(_9c8){
on(_9c8,"click",function(_9d3){
curam.util.clickButton(this._submitButton);
_86b.stop(_9d3);
});
_9c8._submitButton=_9c5[0];
}
_869.add(_9c3,"hidden-button");
attr.set(_9c3,"aria-hidden","true");
attr.set(_9c3,"id",_9c3.id+"_"+_9c4);
});
curam.replacedButtons[name]="true";
},isInternalModal:function(){
return !dojo.global.jsScreenContext.hasContextBits("EXTAPP")&&dojo.global.jsScreenContext.hasContextBits("MODAL");
},isModalFooter:function(_9d4){
if(_9d4){
var _9d5=_9d4.parentNode.parentNode;
return _9d5&&_9d5.id=="actions-panel";
}
},addCarbonModalButton:function(_9d6,_9d7,_9d8,_9d9){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton",[_9d6,_9d7,_9d8,_9d9,window]);
},setupWidgetLoadMask:function(_9da){
curam.util.subscribe("/curam/page/loaded",function(){
var _9db=_86c(_9da)[0];
if(_9db){
_867.set(_9db,"visibility","visible");
}else{
curam.debug.log("setupButtonLoadMask: "+_870.getProperty("curam.util.not.found")+"'"+_9da+"'"+_870.getProperty("curam.util.ignore.mask"));
}
});
},optReplaceSubmitButton:function(name){
if(curam.util.getFrameRoot(dojo.global,"wizard")==null){
curam.util.replaceSubmitButton(name);
return;
}
var _9dc=curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
if(_9dc.delegatesSubmit[jsPageID]!="assumed"){
curam.util.replaceSubmitButton(name);
}
},clickButton:function(_9dd){
var _9de=dom.byId("mainForm");
var _9df;
if(!_9dd){
curam.debug.log("curam.util.clickButton: "+_870.getProperty("curam.util..no.arg"));
return;
}
if(typeof (_9dd)=="string"){
var _9e0=_9dd;
curam.debug.log("curam.util.clickButton: "+_870.getProperty("curam.util.searching")+_870.getProperty("curam.util.id.of")+"'"+_9e0+"'.");
_9dd=_86c("input[id='"+_9e0+"']")[0];
if(!_9dd){
_9dd=_86c("input[name='"+_9e0+"']")[0];
}
if(!_9dd.form&&!_9dd.id){
curam.debug.log("curam.util.clickButton: "+_870.getProperty("curam.util.searched")+_870.getProperty("curam.util.id.of")+"'"+_9e0+_870.getProperty("curam.util.exiting"));
return;
}
}
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_9df=_9dd;
}else{
_9df=_86c("input[id='"+_9dd.id+"']",_9de)[0];
if(!_9df){
_9df=_86c("input[name='"+_9dd.name+"']",_9de)[0];
}
}
try{
if(attr.get(_9de,"action").indexOf(jsPageID)==0){
curam.util.publishRefreshEvent();
}
_9df.click();
}
catch(e){
curam.debug.log(_870.getProperty("curam.util.exception.clicking"));
}
},printPage:function(_9e1,_9e2){
_86b.stop(_9e2);
var _9e3=dojo.window.get(_9e2.currentTarget.ownerDocument);
if(_9e1===false){
curam.util._printMainAreaWindow(_9e3);
return false;
}
var _9e4=_9e3.frameElement;
var _9e5=_9e4;
while(_9e5&&!_869.contains(_9e5,"tab-content-holder")){
_9e5=_9e5.parentNode;
}
var _9e6=_9e5;
var _9e7=dojo.query(".detailsPanelFrame",_9e6)[0];
var _9e8=_9e7!=undefined&&_9e7!=null;
if(_9e8){
var isIE=has("trident")||has("ie");
var _9e9=has("edge");
var _9ea=_869.contains(_9e7.parentNode,"collapsed");
if(isIE&&_9ea){
_867.set(_9e7.parentNode,"display","block");
}
_9e7.contentWindow.focus();
_9e7.contentWindow.print();
if(isIE&&_9ea){
_867.set(_9e7.parentNode,"display","");
}
if(isIE||_9e9){
setTimeout(function(){
if(_9e9){
function _9eb(){
curam.util._printMainAreaWindow(_9e3);
curam.util.getTopmostWindow().document.body.removeEventListener("mouseover",_9eb,true);
return false;
};
curam.util.getTopmostWindow().document.body.addEventListener("mouseover",_9eb,true);
}else{
if(isIE){
curam.util._printMainAreaWindow(_9e3);
return false;
}
}
},2000);
}else{
curam.util._printMainAreaWindow(_9e3);
return false;
}
}else{
curam.util._printMainAreaWindow(_9e3);
return false;
}
},_printMainAreaWindow:function(_9ec){
var _9ed=_86c(".list-details-row-toggle.expanded");
if(_9ed.length>0){
curam.util._prepareContentPrint(_9ec);
_9ec.focus();
_9ec.print();
curam.util._deletePrintVersion();
}else{
_9ec.focus();
_9ec.print();
}
},_prepareContentPrint:function(_9ee){
var _9ef=Array.prototype.slice.call(_9ee.document.querySelectorAll("body iframe"));
_9ef.forEach(function(_9f0){
curam.util._prepareContentPrint(_9f0.contentWindow);
var list=_9f0.contentWindow.document.querySelectorAll(".title-exists");
var _9f1=_9f0.contentWindow.document.querySelectorAll(".title-exists div.context-panel-wrapper");
if(list.length>0&&_9f1.length===0){
var _9f2=document.createElement("div");
_9f2.setAttribute("class","tempContentPanelFrameWrapper");
_9f2.innerHTML=list[0].innerHTML;
var _9f3=_9f0.parentNode;
_9f3.parentNode.appendChild(_9f2);
_9f3.style.display="none";
curam.util.wrappersMap.push({tempDivWithIframeContent:_9f2,iframeParentElement:_9f3});
}
});
},_deletePrintVersion:function(){
if(curam.util.wrappersMap){
curam.util.wrappersMap.forEach(function(_9f4){
_9f4.tempDivWithIframeContent.parentNode.removeChild(_9f4.tempDivWithIframeContent);
_9f4.iframeParentElement.style.display="block";
});
curam.util.wrappersMap=[];
}
},addSelectedClass:function(_9f5){
_869.add(_9f5.target,"selected");
},removeSelectedClass:function(_9f6){
_869.remove(_9f6.target,"selected");
},openHelpPage:function(_9f7,_9f8){
_86b.stop(_9f7);
dojo.global.open(_9f8);
},connect:function(_9f9,_9fa,_9fb){
var h=function(_9fc){
_9fb(_86b.fix(_9fc));
};
if(has("ie")&&has("ie")<9){
_9f9.attachEvent(_9fa,h);
_86e.addOnWindowUnload(function(){
_9f9.detachEvent(_9fa,h);
});
return {object:_9f9,eventName:_9fa,handler:h};
}else{
var _9fd=_9fa;
if(_9fa.indexOf("on")==0){
_9fd=_9fa.slice(2);
}
var dt=on(_9f9,_9fd,h);
_86e.addOnWindowUnload(function(){
dt.remove();
});
return dt;
}
},disconnect:function(_9fe){
if(has("ie")&&has("ie")<9){
_9fe.object.detachEvent(_9fe.eventName,_9fe.handler);
}else{
_9fe.remove();
}
},subscribe:function(_9ff,_a00){
var st=_86a.subscribe(_9ff,_a00);
_86e.addOnWindowUnload(function(){
st.remove();
});
return st;
},unsubscribe:function(_a01){
_a01.remove();
},addActionControlClickListener:function(_a02){
var _a03=dom.byId(_a02);
var _a04=_86c(".ac",_a03);
if(_a04.length>0){
for(var i=0;i<_a04.length;i++){
var _a05=_a04[i];
curam.util.addActionControlClass(_a05);
}
}
this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
},_addAccessibilityMarkupInAddressClustersWhenContextIsMissing:function(){
var _a06=_86c(".bx--accordion__content");
_a06.forEach(function(_a07){
var _a08=_86c(".bx--address",_a07)[0];
if(typeof (_a08)!="undefined"){
var _a09=new _875("util");
var _a0a=_a08.parentElement.parentElement.parentElement;
var _a0b=_a0a.parentElement.parentElement;
var _a0c=_86c("h4, h3",_a0a).length==1?true:false;
var _a0d=attr.get(_a0b,"aria-label")!==null?true:false;
if(!_a0c&&!_a0d){
attr.set(_a0b,"role","group");
attr.set(_a0b,"aria-label",_a09.getProperty("curam.address.header"));
}
}
});
},addActionControlClass:function(_a0e){
curam.util.connect(_a0e,"onmousedown",function(){
_869.add(_a0e,"selected-button");
curam.util.connect(_a0e,"onmouseout",function(){
_869.remove(_a0e,"selected-button");
});
});
},getClusterActionSet:function(){
var _a0f=dom.byId("content");
var _a10=_86c(".blue-action-set",_a0f);
if(_a10.length>0){
for(var i=0;i<_a10.length;i++){
curam.util.addActionControlClickListener(_a10[i]);
}
}
},adjustActionButtonWidth:function(){
if(has("ie")==8){
_865(function(){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")){
_86c(".action-set > a").forEach(function(node){
if(node.childNodes[0].offsetWidth>node.offsetWidth){
_867.set(node,"width",node.childNodes[0].offsetWidth+"px");
_867.set(node,"display","block");
_867.set(node,"display","inline-block");
}
});
}
});
}
},setRpu:function(url,rtc,_a11){
if(!url||!rtc||!rtc.getHref()){
throw {name:"Unexpected values",message:"This value not allowed for url or rtc"};
}
var _a12=curam.util.getLastPathSegmentWithQueryString(rtc.getHref());
_a12=curam.util.removeUrlParam(_a12,curam.util.Constants.RETURN_PAGE_PARAM);
if(_a11){
var i;
for(i=0;i<_a11.length;i++){
if(!_a11[i].key||!_a11[i].value){
throw {name:"undefined value error",message:"The object did not contain a valid key/value pair"};
}
_a12=curam.util.replaceUrlParam(_a12,_a11[i].key,_a11[i].value);
}
}
var _a13=curam.util.replaceUrlParam(url,curam.util.Constants.RETURN_PAGE_PARAM,encodeURIComponent(_a12));
curam.debug.log("curam.util.setRpu "+_870.getProperty("curam.util.added.rpu")+_a13);
return _a13;
},retrieveBaseURL:function(){
return dojo.global.location.href.match(".*://[^/]*/[^/]*");
},removeRoleRegion:function(){
var body=dojo.query("body")[0];
attr.remove(body,"role");
},iframeTitleFallBack:function(){
var _a14=curam.tab.getContainerTab(curam.tab.getContentPanelIframe());
var _a15=dom.byId(curam.tab.getContentPanelIframe());
var _a16=_a15.contentWindow.document.title;
var _a17=dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
var _a18=dojo.query("span.tabLabel",_a17)[0];
var _a19=dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked",_a14.domNode)[0];
var _a1a=dojo.query("span.tabLabel",_a19)[0];
if(_a16=="undefined"){
return this.getPageTitleOnContentPanel();
}else{
if(_a16&&_a16!=""){
return _a16;
}else{
if(_a19){
return _a1a.innerHTML;
}else{
return _a18.innerHTML;
}
}
}
},getPageTitleOnContentPanel:function(){
var _a1b;
var _a1c=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _a1d;
if(_a1c&&_a1c.length==1){
_a1d=_a1c[0].contentWindow.document;
_866.withDoc(_a1d,function(){
var _a1e=dojo.query("div.title h2 span:not(.hidden)");
if(_a1e&&_a1e.length==1&&_a1e[0].textContent){
_a1b=lang.trim(_a1e[0].textContent);
}
},this);
}
if(_a1b){
return _a1b;
}else{
return undefined;
}
},addClassToLastNodeInContentArea:function(){
var _a1f=_86c("> div","content");
var _a20=_a1f.length;
if(_a20==0){
return "No need to add";
}
var _a21=_a1f[--_a20];
while(_869.contains(_a21,"hidden-action-set")&&_a21){
_a21=_a1f[--_a20];
}
_869.add(_a21,"last-node");
},highContrastModeType:function(){
var _a22=dojo.query("body.high-contrast")[0];
return _a22;
},isRtlMode:function(){
var _a23=dojo.query("body.rtl")[0];
return _a23;
},processBidiContextual:function(_a24){
_a24.dir=bidi.prototype._checkContextual(_a24.value);
},getCookie:function(name){
var dc=document.cookie;
var _a25=name+"=";
var _a26=dc.indexOf("; "+_a25);
if(_a26==-1){
_a26=dc.indexOf(_a25);
if(_a26!=0){
return null;
}
}else{
_a26+=2;
}
var end=document.cookie.indexOf(";",_a26);
if(end==-1){
end=dc.length;
}
return unescape(dc.substring(_a26+_a25.length,end));
},getHeadingTitleForScreenReader:function(_a27){
var _a28=curam.util.getTopmostWindow();
var _a29=_a28.dojo.global._tabTitle;
if(_a29){
curam.util.getHeadingTitle(_a29,_a27);
}else{
var _a2a=_a28.dojo.subscribe("/curam/_tabTitle",function(_a2b){
if(_a2b){
curam.util.getHeadingTitle(_a2b,_a27);
}
_a28.dojo.unsubscribe(_a2a);
});
}
},getHeadingTitle:function(_a2c,_a2d){
var _a2e=undefined;
if(_a2c&&_a2c.length>0){
_a2e=_a2c;
}else{
_a2e=_a2d;
}
var _a2f=dojo.query(".page-title-bar");
var _a30=dojo.query("div h2",_a2f[0]);
if(_a30){
var _a31=dojo.query("span",_a30[0]);
var span=undefined;
if(_a31){
span=_a31[0];
}
if(!span||(span&&(span.innerHTML.length==0))){
if(span){
attr.set(span,"class","hidden");
attr.set(span,"title",_a2e);
span.innerHTML=_a2e;
}else{
span=_864.create("span",{"class":"hidden","title":_a2e},_a30[0]);
span.innerHTML=_a2e;
}
}
}
},_setupBrowserTabTitle:function(_a32,_a33,_a34){
_a32=_a32.replace("\\n"," ");
curam.util._browserTabTitleData.staticTabTitle=_a32;
curam.util._browserTabTitleData.separator=_a33;
curam.util._browserTabTitleData.appNameFirst=_a34;
},_browserTabTitleData:{},setBrowserTabTitle:function(_a35){
curam.debug.log("curam.util.setBrowserTabTitle(title = "+_a35+") called");
if(!_a35){
_a35=curam.util._findAppropriateDynamicTitle();
}
var _a36=curam.util._browserTabTitleData.staticTabTitle;
var _a37=curam.util._browserTabTitleData.separator;
var _a38=curam.util._browserTabTitleData.appNameFirst;
if(!_a36&&!_a37&&!_a38&&!_a35){
var _a39=document.querySelectorAll("head title")[0];
if(_a39){
document.title=_a39.text;
}
}else{
if(!_a35){
document.title=_a36;
}else{
if(_a36){
if(_a38){
document.title=_a36+_a37+_a35;
}else{
document.title=_a35+_a37+_a36;
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
var _a3a=dojo.query("iframe.curam-active-modal").length;
if(_a3a>1){
var _a3b=dojo.query("iframe.curam-active-modal")[0];
if(_a3b){
var _a3c=_a3b.contentDocument;
if(_a3c){
var _a3d=_a3c.head.getElementsByTagName("title")[0];
if(_a3d){
if(_a3d.innerHTML!=""){
one=_a3b.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
}
}
}
}
}
if(one){
return one;
}
var two;
var _a3e=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _a3f;
if(_a3e&&_a3e.length==1){
_a3f=_a3e[0].contentWindow.document;
_866.withDoc(_a3f,function(){
var _a40=dojo.query("div.title h2 span:not(.hidden)");
var _a41=dom.byId("error-messages",_a3f);
if(_a41){
two=_a3f.head.getElementsByTagName("title")[0].textContent;
}else{
if(_a40&&_a40.length==1&&_a40[0].textContent){
two=lang.trim(_a40[0].textContent);
curam.debug.log("(2) Page title for Content Panel = "+two);
}else{
if(_a40&&_a40.length>1){
two=this._checkForSubTitles(_a40);
}else{
curam.debug.log("(2) Could not find page title for content panel: header = "+_a40);
}
}
}
},this);
}else{
curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = "+_a3e);
}
if(two){
return two;
}
var _a42;
var _a43=dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
if(_a43&&_a43.length==1&&_a43[0].textContent){
_a42=lang.trim(_a43[0].textContent);
curam.debug.log("(3) Selected navigation item = "+_a42);
}else{
curam.debug.log("(3) Could not find selected navigation item: navItem = "+_a43);
}
if(_a42){
return _a42;
}
var four;
var _a44=dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
var _a45;
for(i=0;i<_a44.length;i++){
if(_a44[i].getAttribute("aria-selected")==="true"){
_a45=_a44[i];
}
}
if(_a45&&_a45.textContent){
four=lang.trim(_a45.textContent);
curam.debug.log("(4) Selected navigation bar tab = "+four);
}else{
curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = "+_a45);
}
if(four){
return four;
}
var five;
var _a46=dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
if(_a46&&_a46.length==1&&_a46[0].textContent){
five=lang.trim(_a46[0].textContent);
curam.debug.log("(5) Selected application tab title bar = "+five);
}else{
curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = "+_a46);
}
if(five){
return five;
}
var six;
var _a47=dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
var _a48;
for(i=0;i<_a47.length;i++){
if(_a47[i].getAttribute("aria-selected")==="true"){
_a48=_a47[i];
break;
}
}
if(_a48&&_a48.textContent){
six=lang.trim(_a48.textContent);
curam.debug.log("(6) Selected section title = "+six);
}else{
curam.debug.log("(6) Could not find selected section title: sections = "+_a47);
}
if(six){
return six;
}
var _a49;
_a3e=dom.byId("curamUAIframe");
if(_a3e&&_a3e.contentWindow&&_a3e.contentWindow.document){
_a3f=_a3e.contentWindow.document;
_866.withDoc(_a3f,function(){
var _a4a=dojo.query("div.page-header > div.page-title-bar > div.title > h2 > span");
if(_a4a&&_a4a.length==1&&_a4a[0].textContent){
_a49=lang.trim(_a4a[0].textContent);
curam.debug.log("(7) UIM page title for external application page = "+_a49);
}else{
curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = "+_a4a);
}
},this);
}
if(_a49){
return _a49;
}
return undefined;
},_checkForSubTitles:function(_a4b){
var i;
if(!_a4b[0].textContent){
return undefined;
}
for(i=1;i<_a4b.length;i++){
var _a4c=_a4b[i].getAttribute("class");
if(_a4c.indexOf("sub-title")===-1||!_a4b[i].textContent){
curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
for(i=0;i<_a4b.length;i++){
curam.debug.log(_a4b[i]);
}
return undefined;
}
}
var ret=_a4b[0].textContent;
for(i=1;i<_a4b.length;i++){
ret+=_a4b[i].textContent;
}
return ret;
},_addContextToWidgetForScreenReader:function(_a4d){
var _a4e=false;
var _a4f=0;
var _a50=dojo.query(".training-details-list");
if(_a50.length==1){
var _a51=_a50[0].parentElement;
var _a52=dojo.query("div.bx--cluster",_a51);
var _a53=Array.prototype.indexOf.call(_a52,_a50[0]);
if(_a53>=0){
for(var i=_a53;i>=0;i--){
if(dojo.query("h3",_a52[i]).length==1){
_a4e=true;
_a4f=i;
break;
}
}
}
if(_a4e){
var _a54=dojo.query("h3.bx--accordion__title",_a52[_a4f]);
if(_a54.length==1){
var _a55=_a54[0].className+"_id";
attr.set(_a54[0],"id",_a55);
var _a56=dojo.byId(_a4d).parentElement;
attr.set(_a56,"aria-labelledby",_a55);
attr.set(_a56,"role","region");
}
}
}
},setParentFocusByChild:function(_a57){
var win=curam.util.UimDialog._getDialogFrameWindow(_a57);
if(win){
var _a58=curam.dialog.getParentWindow(win);
if(_a58){
_a58.focus();
}
}
},toClipboard:function(_a59){
try{
navigator.clipboard.writeText(_a59);
}
catch(err){
console.warn("Failed to copy into the clipboard.");
}
if(dojo.getObject("curam.dialog",false)!=null){
var pw=curam.dialog.getParentWindow(window);
pw&&pw.dojo.publish("/curam/clip/selected",[_a59]);
}
return false;
},removeTopScrollForIos:function(){
if(has("ios")){
window.document.body.scrollTop=0;
}
},insertAriaLiveLabelRecordSearchItem:function(_a5a){
var span=dojo.query("[data-search-page]")[0];
if(span){
span.setAttribute("aria-live",has("ios")?"polite":"assertive");
setTimeout(function(){
var _a5b=span.firstChild.nodeValue;
var _a5c=_a5b+_a5a;
span.innerHTML=_a5c;
},10);
}
},removeSessionStorageProperty:function(_a5d){
sessionStorage.removeItem(_a5d);
},addLayoutStylingOnDateTimeWidgetOnZoom:function(){
var _a5e=dojo.query("table.input-cluster td.field table.date-time");
console.log("datetimetable from util.js: "+_a5e);
var _a5f=_a5e.length;
if(_a5e.length>0){
for(var i=0;i<_a5e.length;i++){
var _a60=_a5e[i];
var _a61=_a60.parentNode.parentNode;
_a61.setAttribute("class","date-time-exists");
}
}
},fileUploadOpenFileBrowser:function(e,_a62){
if(e.keyCode==32||e.keyCode==13){
dom.byId(_a62).click();
}
},setupControlledLists:function(){
var _a63="curam.listControls",_a64="curam.listTogglers";
var _a65=_a66(_a63),_a67=_a66(_a64),_a68=[];
var _a69=_a65&&_86c("*[data-control]"),_a6a=_a67&&_86c("a[data-toggler]");
if(_a65||_a67){
for(var _a6b in _a65){
_a69.filter(function(item){
return attr.get(item,"data-control")==_a6b;
}).forEach(function(_a6c,ix){
var c=dom.byId(_a6c),tr=_86c(_a6c).closest("tr")[0];
!tr.controls&&(tr.controls=new Array());
tr.controls.push(c);
if(!tr.visited){
tr.visited=true;
_a65[_a6b].push(tr);
}
});
var _a6d=_a66(_a63+"."+_a6b);
if(_a6d&&_a6d.length&&_a6d.length>0){
_a68.push(_a6b);
}else{
_a99(_a63+"."+_a6b,false);
}
}
if(_a6a&&_a6a.length>0){
for(var _a6b in _a67){
_a6a.filter(function(item){
return attr.get(item,"data-toggler")==_a6b;
}).forEach(function(_a6e){
var tr=_86c(_a6e).closest("tr")[0];
tr.hasToggler=_a6e;
tr.visited=true;
_a67[_a6b].push(tr);
});
var _a6f=_a66(_a64+"."+_a6b);
if(_a6f&&_a6f.length&&_a6f.length>0){
(_a68.indexOf(_a6b)==-1)&&_a68.push(_a6b);
}else{
_a99(_a64+"."+_a6b,false);
}
}
}
_a68.forEach(function(_a70){
var _a71=_a66(_a63+"."+_a70)||_a66(_a64+"."+_a70);
cu.updateListControlReadings(_a70,_a71);
});
}
dojo.subscribe("curam/sort/earlyAware",function(_a72){
cu.suppressPaginationUpdate=_a72;
});
dojo.subscribe("curam/update/readings/sort",function(_a73,rows){
if(!has("trident")){
cu.updateListActionReadings(_a73);
cu.updateListControlReadings(_a73,rows);
cu.suppressPaginationUpdate=false;
}else{
var _a74=cu.getPageBreak(_a73),_a75=Math.ceil(rows.length/_a74);
cu.listRangeUpdate(0,_a75,_a73,rows,_a74);
}
});
dojo.subscribe("curam/update/readings/pagination",function(_a76,_a77){
_a99("curam.pageBreak."+_a76,_a77);
});
dojo.subscribe("curam/update/pagination/rows",function(_a78,_a79){
cu.updateDeferred&&!cu.updateDeferred.isResolved()&&cu.updateDeferred.cancel("Superseeded");
if(cu.suppressPaginationUpdate&&cu.suppressPaginationUpdate==_a79){
return;
}
var _a7a=_aa6("curam.listTogglers."+_a79),_a7b=_aa6("curam.listControls."+_a79),lms=_a66("curam.listMenus."+_a79),_a7c=lms&&(lms.length>0);
var _a7d=_a7b||_a7a;
if(!_a7d&&!_a7c){
return;
}
if(_a7d){
var _a7e=_a78.filter(function(aRow){
return (!aRow.visited||!aRow.done)&&attr.has(aRow,"data-lix");
});
_a7a&&_a7e.forEach(function(aRow){
var tgl=_86c("a[data-toggler]",aRow)[0];
aRow.hasToggler=tgl;
aRow.visited=true;
curam.listTogglers[_a79].push(aRow);
});
_a7b&&_a7e.forEach(function(aRow){
var _a7f=_86c("*[data-control]",aRow),_a80=new Array();
_a7f.forEach(function(cRef){
_a80.push(dom.byId(cRef));
});
aRow.controls=_a80;
curam.listControls[_a79].push(aRow);
aRow.visited=true;
});
var _a81=_a7b?curam.listControls[_a79]:curam.listTogglers[_a79];
cu.updateListControlReadings(_a79,_a81);
}
_a7c&&cu.updateListActionReadings(_a79);
});
},listRangeUpdate:function(_a82,_a83,_a84,rows,psz){
if(_a82==_a83){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
return;
}
var def=cu.updateDeferred=new _86d(function(_a85){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
});
def.then(function(pNum){
cu.listRangeUpdate(pNum,_a83,_a84,rows,psz);
},function(err){
});
var _a86=(_a82===0)?0:200;
setTimeout(function(){
var _a87=_a82+1,_a88=[_a82*psz,(_a87*psz)];
cu.updateListActionReadings(_a84,_a88);
cu.updateListControlReadings(_a84,rows,_a88);
def.resolve(_a87);
},_a86);
},updateListControlReadings:function(_a89,_a8a,_a8b){
var c0,psz=cu.getPageBreak(_a89),_a8c=cu.getStartShift(_a89,_a8a[0]||false),_a8d=_a8a;
_a8b&&(_a8d=_a8a.slice(_a8b[0],_a8b[1]));
for(var rix in _a8d){
var aRow=_a8d[rix],_a8e=parseInt(attr.get(aRow,_a8f)),lx=(_a8e%psz)+_a8c,_a90=aRow.controls;
if(!_a90){
var _a91=_86c("*[data-control]",aRow),_a92=new Array();
_a91.forEach(function(cRef){
_a92.push(dom.byId(cRef));
});
aRow.controls=_a92;
_a90=aRow.controls;
}
if(_a90){
for(var cix in _a90){
var crtl=_a90[cix],ttl=crtl.textContent||false,_a93=ttl?ttl+",":"";
if(crtl.nodeName=="A"){
var _a94=_86c("img",crtl)[0];
if(_a94&&_869.contains(crtl,"ac first-action-control external-link")){
var _a95=attr.get(_a94,"alt");
attr.set(crtl,_a96,_a95+","+[listcontrol.reading.anchors,lx].join(" "));
}else{
attr.set(crtl,_a96,_a93+[listcontrol.reading.anchors,lx].join(" "));
}
}else{
attr.set(crtl,_a96,_a93+[listcontrol.reading.selectors,lx].join(" "));
}
}
}
cu.updateToggler(aRow,lx);
aRow.done=true;
}
},initListActionReadings:function(_a97){
var _a98="curam.listMenus."+_a97;
_a99(_a98,[]);
dojo.subscribe("curam/listmenu/started",function(_a9a,_a9b){
var tr=_86c(_a9a.containerNode).closest("tr")[0],lix=parseInt(attr.get(tr,_a8f)),lx=(lix%cu.getPageBreak(_a9b))+cu.getStartShift(_a9b,tr);
_a9a.set({"belongsTo":tr,"aria-labelledBy":"","aria-label":[listcontrol.reading.menus,lx].join(" ")});
_a66(_a98).push(_a9a);
cu.updateToggler(tr,lx);
});
},updateToggler:function(_a9c,_a9d){
_a9c.hasToggler&&attr.set(_a9c.hasToggler,_a96,[listcontrol.reading.togglers,_a9d].join(" "));
},updateListActionReadings:function(_a9e,_a9f){
var _aa0=_a66("curam.listMenus."+_a9e),psz=cu.getPageBreak(_a9e),_aa1=false,_aa2=_aa0;
_a9f&&(_aa2=_aa0.slice(_a9f[0],_a9f[1]));
for(var ix in _aa2){
var _aa3=_aa2[ix],tr=_aa3.belongsTo,lix=parseInt(attr.get(tr,_a8f)),_aa1=_aa1||cu.getStartShift(_a9e,tr),_aa4=(lix%psz)+_aa1;
_aa3.set(_a96,[listcontrol.reading.menus,_aa4].join(" "));
cu.updateToggler(tr,_aa4);
tr.done=true;
}
},getPageBreak:function(_aa5){
if(!_aa6("curam.list.isPaginated."+_aa5)){
return 1000;
}
if(_a66("curam.shortlist."+_aa5)){
return 1000;
}
var psz=_a66("curam.pageBreak."+_aa5)||_a66("curam.pagination.defaultPageSize")||1000;
return psz;
},getStartShift:function(_aa7,_aa8){
if(!_aa8){
return 2;
}
var _aa9="curam.listHeaderStep."+_aa7,_aaa=_a66(_aa9);
if(_aaa){
return _aaa;
}
_a99(_aa9,2);
var _aab=_86c(_aa8).closest("table");
if(_aab.length==0){
return 2;
}
var _aac=_aab.children("thead")[0];
!_aac&&_a99(_aa9,1);
return curam.listHeaderStep[_aa7];
},extendXHR:function(){
var _aad=XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open=function(){
this.addEventListener("load",function(){
if(typeof (Storage)!=="undefined"){
var _aae=this.getResponseHeader("sessionExpiry");
sessionStorage.setItem("sessionExpiry",_aae);
}
});
_aad.apply(this,arguments);
};
},suppressPaginationUpdate:false,updateDeferred:null});
var cu=curam.util,_a66=dojo.getObject,_a99=dojo.setObject,_aa6=dojo.exists,_a96="aria-label",_a8f="data-lix";
return curam.util;
});
},"dijit/_base/sniff":function(){
define(["dojo/uacss"],function(){
});
},"dojo/request/iframe":function(){
define(["module","require","./watch","./util","./handlers","../_base/lang","../io-query","../query","../has","../dom","../dom-construct","../_base/window","../NodeList-dom"],function(_aaf,_ab0,_ab1,util,_ab2,lang,_ab3,_ab4,has,dom,_ab5,win){
var mid=_aaf.id.replace(/[\/\.\-]/g,"_"),_ab6=mid+"_onload";
if(!win.global[_ab6]){
win.global[_ab6]=function(){
var dfd=_ab7._currentDfd;
if(!dfd){
_ab7._fireNextRequest();
return;
}
var _ab8=dfd.response,_ab9=_ab8.options,_aba=dom.byId(_ab9.form)||dfd._tmpForm;
if(_aba){
var _abb=dfd._contentToClean;
for(var i=0;i<_abb.length;i++){
var key=_abb[i];
for(var j=0;j<_aba.childNodes.length;j++){
var _abc=_aba.childNodes[j];
if(_abc.name===key){
_ab5.destroy(_abc);
break;
}
}
}
dfd._originalAction&&_aba.setAttribute("action",dfd._originalAction);
if(dfd._originalMethod){
_aba.setAttribute("method",dfd._originalMethod);
_aba.method=dfd._originalMethod;
}
if(dfd._originalTarget){
_aba.setAttribute("target",dfd._originalTarget);
_aba.target=dfd._originalTarget;
}
}
if(dfd._tmpForm){
_ab5.destroy(dfd._tmpForm);
delete dfd._tmpForm;
}
dfd._finished=true;
};
}
function _abd(name,_abe,uri){
if(win.global[name]){
return win.global[name];
}
if(win.global.frames[name]){
return win.global.frames[name];
}
if(!uri){
if(has("config-useXDomain")&&!has("config-dojoBlankHtmlUrl")){
console.warn("dojo/request/iframe: When using cross-domain Dojo builds,"+" please save dojo/resources/blank.html to your domain and set dojoConfig.dojoBlankHtmlUrl"+" to the path on your domain to blank.html");
}
uri=(has("config-dojoBlankHtmlUrl")||_ab0.toUrl("dojo/resources/blank.html"));
}
var _abf=_ab5.place("<iframe id=\""+name+"\" name=\""+name+"\" src=\""+uri+"\" onload=\""+_abe+"\" style=\"position: absolute; left: 1px; top: 1px; height: 1px; width: 1px; visibility: hidden\">",win.body());
win.global[name]=_abf;
return _abf;
};
function _ac0(_ac1,src,_ac2){
var _ac3=win.global.frames[_ac1.name];
if(_ac3.contentWindow){
_ac3=_ac3.contentWindow;
}
try{
if(!_ac2){
_ac3.location=src;
}else{
_ac3.location.replace(src);
}
}
catch(e){
console.log("dojo/request/iframe.setSrc: ",e);
}
};
function doc(_ac4){
if(_ac4.contentDocument){
return _ac4.contentDocument;
}
var name=_ac4.name;
if(name){
var _ac5=win.doc.getElementsByTagName("iframe");
if(_ac4.document&&_ac5[name].contentWindow&&_ac5[name].contentWindow.document){
return _ac5[name].contentWindow.document;
}else{
if(win.doc.frames[name]&&win.doc.frames[name].document){
return win.doc.frames[name].document;
}
}
}
return null;
};
function _ac6(){
return _ab5.create("form",{name:mid+"_form",style:{position:"absolute",top:"-1000px",left:"-1000px"}},win.body());
};
function _ac7(){
var dfd;
try{
if(_ab7._currentDfd||!_ab7._dfdQueue.length){
return;
}
do{
dfd=_ab7._currentDfd=_ab7._dfdQueue.shift();
}while(dfd&&(dfd.canceled||(dfd.isCanceled&&dfd.isCanceled()))&&_ab7._dfdQueue.length);
if(!dfd||dfd.canceled||(dfd.isCanceled&&dfd.isCanceled())){
_ab7._currentDfd=null;
return;
}
var _ac8=dfd.response,_ac9=_ac8.options,c2c=dfd._contentToClean=[],_aca=dom.byId(_ac9.form),_acb=util.notify,data=_ac9.data||null,_acc;
if(!dfd._legacy&&_ac9.method==="POST"&&!_aca){
_aca=dfd._tmpForm=_ac6();
}else{
if(_ac9.method==="GET"&&_aca&&_ac8.url.indexOf("?")>-1){
_acc=_ac8.url.slice(_ac8.url.indexOf("?")+1);
data=lang.mixin(_ab3.queryToObject(_acc),data);
}
}
if(_aca){
if(!dfd._legacy){
var _acd=_aca;
do{
_acd=_acd.parentNode;
}while(_acd&&_acd!==win.doc.documentElement);
if(!_acd){
_aca.style.position="absolute";
_aca.style.left="-1000px";
_aca.style.top="-1000px";
win.body().appendChild(_aca);
}
if(!_aca.name){
_aca.name=mid+"_form";
}
}
if(data){
var _ace=function(name,_acf){
_ab5.create("input",{type:"hidden",name:name,value:_acf},_aca);
c2c.push(name);
};
for(var x in data){
var val=data[x];
if(lang.isArray(val)&&val.length>1){
for(var i=0;i<val.length;i++){
_ace(x,val[i]);
}
}else{
if(!_aca[x]){
_ace(x,val);
}else{
_aca[x].value=val;
}
}
}
}
var _ad0=_aca.getAttributeNode("action"),_ad1=_aca.getAttributeNode("method"),_ad2=_aca.getAttributeNode("target");
if(_ac8.url){
dfd._originalAction=_ad0?_ad0.value:null;
if(_ad0){
_ad0.value=_ac8.url;
}else{
_aca.setAttribute("action",_ac8.url);
}
}
if(!dfd._legacy){
dfd._originalMethod=_ad1?_ad1.value:null;
if(_ad1){
_ad1.value=_ac9.method;
}else{
_aca.setAttribute("method",_ac9.method);
}
}else{
if(!_ad1||!_ad1.value){
if(_ad1){
_ad1.value=_ac9.method;
}else{
_aca.setAttribute("method",_ac9.method);
}
}
}
dfd._originalTarget=_ad2?_ad2.value:null;
if(_ad2){
_ad2.value=_ab7._iframeName;
}else{
_aca.setAttribute("target",_ab7._iframeName);
}
_aca.target=_ab7._iframeName;
_acb&&_acb.emit("send",_ac8,dfd.promise.cancel);
_ab7._notifyStart(_ac8);
_aca.submit();
}else{
var _ad3="";
if(_ac8.options.data){
_ad3=_ac8.options.data;
if(typeof _ad3!=="string"){
_ad3=_ab3.objectToQuery(_ad3);
}
}
var _ad4=_ac8.url+(_ac8.url.indexOf("?")>-1?"&":"?")+_ad3;
_acb&&_acb.emit("send",_ac8,dfd.promise.cancel);
_ab7._notifyStart(_ac8);
_ab7.setSrc(_ab7._frame,_ad4,true);
}
}
catch(e){
dfd.reject(e);
}
};
function _ad5(_ad6){
return !this.isFulfilled();
};
function _ad7(_ad8){
return !!this._finished;
};
function _ad9(_ada,_adb){
if(!_adb){
try{
var _adc=_ada.options,doc=_ab7.doc(_ab7._frame),_add=_adc.handleAs;
if(_add!=="html"){
if(_add==="xml"){
if(doc.documentElement.tagName.toLowerCase()==="html"){
_ab4("a",doc.documentElement).orphan();
var _ade=doc.documentElement.innerText||doc.documentElement.textContent;
_ade=_ade.replace(/>\s+</g,"><");
_ada.text=lang.trim(_ade);
}else{
_ada.data=doc;
}
}else{
_ada.text=doc.getElementsByTagName("textarea")[0].value;
}
_ab2(_ada);
}else{
_ada.data=doc;
}
}
catch(e){
_adb=e;
}
}
if(_adb){
this.reject(_adb);
}else{
if(this._finished){
this.resolve(_ada);
}else{
this.reject(new Error("Invalid dojo/request/iframe request state"));
}
}
};
function last(_adf){
this._callNext();
};
var _ae0={method:"POST"};
function _ab7(url,_ae1,_ae2){
var _ae3=util.parseArgs(url,util.deepCreate(_ae0,_ae1),true);
url=_ae3.url;
_ae1=_ae3.options;
if(_ae1.method!=="GET"&&_ae1.method!=="POST"){
throw new Error(_ae1.method+" not supported by dojo/request/iframe");
}
if(!_ab7._frame){
_ab7._frame=_ab7.create(_ab7._iframeName,_ab6+"();");
}
var dfd=util.deferred(_ae3,null,_ad5,_ad7,_ad9,last);
dfd._callNext=function(){
if(!this._calledNext){
this._calledNext=true;
_ab7._currentDfd=null;
_ab7._fireNextRequest();
}
};
dfd._legacy=_ae2;
_ab7._dfdQueue.push(dfd);
_ab7._fireNextRequest();
_ab1(dfd);
return _ae2?dfd:dfd.promise;
};
_ab7.create=_abd;
_ab7.doc=doc;
_ab7.setSrc=_ac0;
_ab7._iframeName=mid+"_IoIframe";
_ab7._notifyStart=function(){
};
_ab7._dfdQueue=[];
_ab7._currentDfd=null;
_ab7._fireNextRequest=_ac7;
util.addCommonMethods(_ab7,["GET","POST"]);
return _ab7;
});
},"dojox/collections/_base":function(){
define(["dojo/_base/kernel","dojo/_base/lang","dojo/_base/array"],function(dojo,lang,arr){
var _ae4=lang.getObject("dojox.collections",true);
_ae4.DictionaryEntry=function(k,v){
this.key=k;
this.value=v;
this.valueOf=function(){
return this.value;
};
this.toString=function(){
return String(this.value);
};
};
_ae4.Iterator=function(a){
var _ae5=0;
this.element=a[_ae5]||null;
this.atEnd=function(){
return (_ae5>=a.length);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=a[_ae5++];
return this.element;
};
this.map=function(fn,_ae6){
return arr.map(a,fn,_ae6);
};
this.reset=function(){
_ae5=0;
this.element=a[_ae5];
};
};
_ae4.DictionaryIterator=function(obj){
var a=[];
var _ae7={};
for(var p in obj){
if(!_ae7[p]){
a.push(obj[p]);
}
}
var _ae8=0;
this.element=a[_ae8]||null;
this.atEnd=function(){
return (_ae8>=a.length);
};
this.get=function(){
if(this.atEnd()){
return null;
}
this.element=a[_ae8++];
return this.element;
};
this.map=function(fn,_ae9){
return arr.map(a,fn,_ae9);
};
this.reset=function(){
_ae8=0;
this.element=a[_ae8];
};
};
return _ae4;
});
},"dojo/regexp":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
var _aea={};
lang.setObject("dojo.regexp",_aea);
_aea.escapeString=function(str,_aeb){
return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+\-^])/g,function(ch){
if(_aeb&&_aeb.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
_aea.buildGroupRE=function(arr,re,_aec){
if(!(arr instanceof Array)){
return re(arr);
}
var b=[];
for(var i=0;i<arr.length;i++){
b.push(re(arr[i]));
}
return _aea.group(b.join("|"),_aec);
};
_aea.group=function(_aed,_aee){
return "("+(_aee?"?:":"")+_aed+")";
};
return _aea;
});
},"curam/debug":function(){
define(["curam/define","curam/util/LocalConfig","dojo/ready","dojo/_base/lang","curam/util/ResourceBundle"],function(_aef,_af0,_af1,lang,_af2){
var _af3=new _af2("curam.application.Debug");
_aef.singleton("curam.debug",{log:function(){
if(curam.debug.enabled()){
try{
var a=arguments;
if(!dojo.isIE){
console.log.apply(console,a);
}else{
var _af4=a.length;
var sa=curam.debug._serializeArgument;
switch(_af4){
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
console.log("[Incomplete message - "+(_af4-5)+" message a truncated] "+a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
}
}
}
catch(e){
console.log(e);
}
}
},getProperty:function(key,_af5){
return _af3.getProperty(key,_af5);
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
var _af6=typeof arg!="undefined"&&typeof arg.closed!="undefined"&&arg.closed;
if(_af6){
return true;
}else{
return typeof arg!="undefined"&&typeof arg.location!="undefined"&&typeof arg.navigator!="undefined"&&typeof arg.document!="undefined"&&typeof arg.closed!="undefined";
}
},enabled:function(){
return _af0.readOption("jsTraceLog","false")=="true";
},_setup:function(_af7){
_af0.seedOption("jsTraceLog",_af7.trace,"false");
_af0.seedOption("ajaxDebugMode",_af7.ajaxDebug,"false");
_af0.seedOption("asyncProgressMonitor",_af7.asyncProgressMonitor,"false");
}});
return curam.debug;
});
},"dijit/DropDownMenu":function(){
define(["dojo/_base/declare","dojo/keys","dojo/text!./templates/Menu.html","./_MenuBase"],function(_af8,keys,_af9,_afa){
return _af8("dijit.DropDownMenu",_afa,{templateString:_af9,baseClass:"dijitMenu",_onUpArrow:function(){
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
define(["require","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/mouse","dojo/on","dojo/touch","./_WidgetBase"],function(_afb,_afc,_afd,_afe,lang,_aff,on,_b00,_b01){
var _b02=lang.delegate(_b00,{"mouseenter":_aff.enter,"mouseleave":_aff.leave,"keypress":_afd._keypress});
var _b03;
var _b04=_afe("dijit._AttachMixin",null,{constructor:function(){
this._attachPoints=[];
this._attachEvents=[];
},buildRendering:function(){
this.inherited(arguments);
this._attachTemplateNodes(this.domNode);
this._beforeFillContent();
},_beforeFillContent:function(){
},_attachTemplateNodes:function(_b05){
var node=_b05;
while(true){
if(node.nodeType==1&&(this._processTemplateNode(node,function(n,p){
return n.getAttribute(p);
},this._attach)||this.searchContainerNode)&&node.firstChild){
node=node.firstChild;
}else{
if(node==_b05){
return;
}
while(!node.nextSibling){
node=node.parentNode;
if(node==_b05){
return;
}
}
node=node.nextSibling;
}
}
},_processTemplateNode:function(_b06,_b07,_b08){
var ret=true;
var _b09=this.attachScope||this,_b0a=_b07(_b06,"dojoAttachPoint")||_b07(_b06,"data-dojo-attach-point");
if(_b0a){
var _b0b,_b0c=_b0a.split(/\s*,\s*/);
while((_b0b=_b0c.shift())){
if(lang.isArray(_b09[_b0b])){
_b09[_b0b].push(_b06);
}else{
_b09[_b0b]=_b06;
}
ret=(_b0b!="containerNode");
this._attachPoints.push(_b0b);
}
}
var _b0d=_b07(_b06,"dojoAttachEvent")||_b07(_b06,"data-dojo-attach-event");
if(_b0d){
var _b0e,_b0f=_b0d.split(/\s*,\s*/);
var trim=lang.trim;
while((_b0e=_b0f.shift())){
if(_b0e){
var _b10=null;
if(_b0e.indexOf(":")!=-1){
var _b11=_b0e.split(":");
_b0e=trim(_b11[0]);
_b10=trim(_b11[1]);
}else{
_b0e=trim(_b0e);
}
if(!_b10){
_b10=_b0e;
}
this._attachEvents.push(_b08(_b06,_b0e,lang.hitch(_b09,_b10)));
}
}
}
return ret;
},_attach:function(node,type,func){
type=type.replace(/^on/,"").toLowerCase();
if(type=="dijitclick"){
type=_b03||(_b03=_afb("./a11yclick"));
}else{
type=_b02[type]||type;
}
return on(node,type,func);
},_detachTemplateNodes:function(){
var _b12=this.attachScope||this;
_afc.forEach(this._attachPoints,function(_b13){
delete _b12[_b13];
});
this._attachPoints=[];
_afc.forEach(this._attachEvents,function(_b14){
_b14.remove();
});
this._attachEvents=[];
},destroyRendering:function(){
this._detachTemplateNodes();
this.inherited(arguments);
}});
lang.extend(_b01,{dojoAttachEvent:"",dojoAttachPoint:""});
return _b04;
});
},"dijit/form/_FormMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/window"],function(_b15,_b16,_b17,lang,on,_b18){
return _b16("dijit.form._FormMixin",null,{state:"",_getDescendantFormWidgets:function(_b19){
var res=[];
_b15.forEach(_b19||this.getChildren(),function(_b1a){
if("value" in _b1a){
res.push(_b1a);
}else{
res=res.concat(this._getDescendantFormWidgets(_b1a.getChildren()));
}
},this);
return res;
},reset:function(){
_b15.forEach(this._getDescendantFormWidgets(),function(_b1b){
if(_b1b.reset){
_b1b.reset();
}
});
},validate:function(){
var _b1c=false;
return _b15.every(_b15.map(this._getDescendantFormWidgets(),function(_b1d){
_b1d._hasBeenBlurred=true;
var _b1e=_b1d.disabled||!_b1d.validate||_b1d.validate();
if(!_b1e&&!_b1c){
_b18.scrollIntoView(_b1d.containerNode||_b1d.domNode);
_b1d.focus();
_b1c=true;
}
return _b1e;
}),function(item){
return item;
});
},setValues:function(val){
_b17.deprecated(this.declaredClass+"::setValues() is deprecated. Use set('value', val) instead.","","2.0");
return this.set("value",val);
},_setValueAttr:function(obj){
var map={};
_b15.forEach(this._getDescendantFormWidgets(),function(_b1f){
if(!_b1f.name){
return;
}
var _b20=map[_b1f.name]||(map[_b1f.name]=[]);
_b20.push(_b1f);
});
for(var name in map){
if(!map.hasOwnProperty(name)){
continue;
}
var _b21=map[name],_b22=lang.getObject(name,false,obj);
if(_b22===undefined){
continue;
}
_b22=[].concat(_b22);
if(typeof _b21[0].checked=="boolean"){
_b15.forEach(_b21,function(w){
w.set("value",_b15.indexOf(_b22,w._get("value"))!=-1);
});
}else{
if(_b21[0].multiple){
_b21[0].set("value",_b22);
}else{
_b15.forEach(_b21,function(w,i){
w.set("value",_b22[i]);
});
}
}
}
},getValues:function(){
_b17.deprecated(this.declaredClass+"::getValues() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},_getValueAttr:function(){
var obj={};
_b15.forEach(this._getDescendantFormWidgets(),function(_b23){
var name=_b23.name;
if(!name||_b23.disabled){
return;
}
var _b24=_b23.get("value");
if(typeof _b23.checked=="boolean"){
if(/Radio/.test(_b23.declaredClass)){
if(_b24!==false){
lang.setObject(name,_b24,obj);
}else{
_b24=lang.getObject(name,false,obj);
if(_b24===undefined){
lang.setObject(name,null,obj);
}
}
}else{
var ary=lang.getObject(name,false,obj);
if(!ary){
ary=[];
lang.setObject(name,ary,obj);
}
if(_b24!==false){
ary.push(_b24);
}
}
}else{
var prev=lang.getObject(name,false,obj);
if(typeof prev!="undefined"){
if(lang.isArray(prev)){
prev.push(_b24);
}else{
lang.setObject(name,[prev,_b24],obj);
}
}else{
lang.setObject(name,_b24,obj);
}
}
});
return obj;
},isValid:function(){
return this.state=="";
},onValidStateChange:function(){
},_getState:function(){
var _b25=_b15.map(this._descendants,function(w){
return w.get("state")||"";
});
return _b15.indexOf(_b25,"Error")>=0?"Error":_b15.indexOf(_b25,"Incomplete")>=0?"Incomplete":"";
},disconnectChildren:function(){
},connectChildren:function(_b26){
this._descendants=this._getDescendantFormWidgets();
_b15.forEach(this._descendants,function(_b27){
if(!_b27._started){
_b27.startup();
}
});
if(!_b26){
this._onChildChange();
}
},_onChildChange:function(attr){
if(!attr||attr=="state"||attr=="disabled"){
this._set("state",this._getState());
}
if(!attr||attr=="value"||attr=="disabled"||attr=="checked"){
if(this._onChangeDelayTimer){
this._onChangeDelayTimer.remove();
}
this._onChangeDelayTimer=this.defer(function(){
delete this._onChangeDelayTimer;
this._set("value",this.get("value"));
},10);
}
},startup:function(){
this.inherited(arguments);
this._descendants=this._getDescendantFormWidgets();
this.value=this.get("value");
this.state=this._getState();
var self=this;
this.own(on(this.containerNode,"attrmodified-state, attrmodified-disabled, attrmodified-value, attrmodified-checked",function(evt){
if(evt.target==self.domNode){
return;
}
self._onChildChange(evt.type.replace("attrmodified-",""));
}));
this.watch("state",function(attr,_b28,_b29){
this.onValidStateChange(_b29=="");
});
},destroy:function(){
this.inherited(arguments);
}});
});
},"dijit/Menu":function(){
define(["require","dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/sniff","dojo/_base/window","dojo/window","./popup","./DropDownMenu","dojo/ready"],function(_b2a,_b2b,_b2c,dom,_b2d,_b2e,_b2f,keys,lang,on,has,win,_b30,pm,_b31,_b32){
if(1){
_b32(0,function(){
var _b33=["dijit/MenuItem","dijit/PopupMenuItem","dijit/CheckedMenuItem","dijit/MenuSeparator"];
_b2a(_b33);
});
}
return _b2c("dijit.Menu",_b31,{constructor:function(){
this._bindings=[];
},targetNodeIds:[],selector:"",contextMenuForWindow:false,leftClickToOpen:false,refocus:true,postCreate:function(){
if(this.contextMenuForWindow){
this.bindDomNode(this.ownerDocumentBody);
}else{
_b2b.forEach(this.targetNodeIds,this.bindDomNode,this);
}
this.inherited(arguments);
},_iframeContentWindow:function(_b34){
return _b30.get(this._iframeContentDocument(_b34))||this._iframeContentDocument(_b34)["__parent__"]||(_b34.name&&document.frames[_b34.name])||null;
},_iframeContentDocument:function(_b35){
return _b35.contentDocument||(_b35.contentWindow&&_b35.contentWindow.document)||(_b35.name&&document.frames[_b35.name]&&document.frames[_b35.name].document)||null;
},bindDomNode:function(node){
node=dom.byId(node,this.ownerDocument);
var cn;
if(node.tagName.toLowerCase()=="iframe"){
var _b36=node,_b37=this._iframeContentWindow(_b36);
cn=win.body(_b37.document);
}else{
cn=(node==win.body(this.ownerDocument)?this.ownerDocument.documentElement:node);
}
var _b38={node:node,iframe:_b36};
_b2d.set(node,"_dijitMenu"+this.id,this._bindings.push(_b38));
var _b39=lang.hitch(this,function(cn){
var _b3a=this.selector,_b3b=_b3a?function(_b3c){
return on.selector(_b3a,_b3c);
}:function(_b3d){
return _b3d;
},self=this;
return [on(cn,_b3b(this.leftClickToOpen?"click":"contextmenu"),function(evt){
evt.stopPropagation();
evt.preventDefault();
if((new Date()).getTime()<self._lastKeyDown+500){
return;
}
self._scheduleOpen(this,_b36,{x:evt.pageX,y:evt.pageY},evt.target);
}),on(cn,_b3b("keydown"),function(evt){
if(evt.keyCode==93||(evt.shiftKey&&evt.keyCode==keys.F10)||(self.leftClickToOpen&&evt.keyCode==keys.SPACE)){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_b36,null,evt.target);
self._lastKeyDown=(new Date()).getTime();
}
})];
});
_b38.connects=cn?_b39(cn):[];
if(_b36){
_b38.onloadHandler=lang.hitch(this,function(){
var _b3e=this._iframeContentWindow(_b36),cn=win.body(_b3e.document);
_b38.connects=_b39(cn);
});
if(_b36.addEventListener){
_b36.addEventListener("load",_b38.onloadHandler,false);
}else{
_b36.attachEvent("onload",_b38.onloadHandler);
}
}
},unBindDomNode:function(_b3f){
var node;
try{
node=dom.byId(_b3f,this.ownerDocument);
}
catch(e){
return;
}
var _b40="_dijitMenu"+this.id;
if(node&&_b2d.has(node,_b40)){
var bid=_b2d.get(node,_b40)-1,b=this._bindings[bid],h;
while((h=b.connects.pop())){
h.remove();
}
var _b41=b.iframe;
if(_b41){
if(_b41.removeEventListener){
_b41.removeEventListener("load",b.onloadHandler,false);
}else{
_b41.detachEvent("onload",b.onloadHandler);
}
}
_b2d.remove(node,_b40);
delete this._bindings[bid];
}
},_scheduleOpen:function(_b42,_b43,_b44,_b45){
if(!this._openTimer){
this._openTimer=this.defer(function(){
delete this._openTimer;
this._openMyself({target:_b45,delegatedTarget:_b42,iframe:_b43,coords:_b44});
},1);
}
},_openMyself:function(args){
var _b46=args.target,_b47=args.iframe,_b48=args.coords,_b49=!_b48;
this.currentTarget=args.delegatedTarget;
if(_b48){
if(_b47){
var ifc=_b2e.position(_b47,true),_b4a=this._iframeContentWindow(_b47),_b4b=_b2e.docScroll(_b4a.document);
var cs=_b2f.getComputedStyle(_b47),tp=_b2f.toPixelValue,left=(has("ie")&&has("quirks")?0:tp(_b47,cs.paddingLeft))+(has("ie")&&has("quirks")?tp(_b47,cs.borderLeftWidth):0),top=(has("ie")&&has("quirks")?0:tp(_b47,cs.paddingTop))+(has("ie")&&has("quirks")?tp(_b47,cs.borderTopWidth):0);
_b48.x+=ifc.x+left-_b4b.x;
_b48.y+=ifc.y+top-_b4b.y;
}
}else{
_b48=_b2e.position(_b46,true);
_b48.x+=10;
_b48.y+=10;
}
var self=this;
var _b4c=this._focusManager.get("prevNode");
var _b4d=this._focusManager.get("curNode");
var _b4e=!_b4d||(dom.isDescendant(_b4d,this.domNode))?_b4c:_b4d;
function _b4f(){
if(self.refocus&&_b4e){
_b4e.focus();
}
pm.close(self);
};
pm.open({popup:this,x:_b48.x,y:_b48.y,onExecute:_b4f,onCancel:_b4f,orient:this.isLeftToRight()?"L":"R"});
this.focus();
if(!_b49){
this.defer(function(){
this._cleanUp(true);
});
}
this._onBlur=function(){
this.inherited("_onBlur",arguments);
pm.close(this);
};
},destroy:function(){
_b2b.forEach(this._bindings,function(b){
if(b){
this.unBindDomNode(b.node);
}
},this);
this.inherited(arguments);
}});
});
},"idx/app/Header":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/aspect","dojo/query","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-style","dojo/i18n","dojo/keys","dojo/string","dijit/_base/popup","dijit/place","dijit/registry","dijit/_Widget","dijit/_TemplatedMixin","idx/util","idx/resources","dojo/NodeList-dom","dojo/i18n!../nls/base","dojo/i18n!./nls/base","dojo/i18n!./nls/Header"],function(_b50,_b51,_b52,_b53,_b54,_b55,_b56,_b57,_b58,_b59,i18n,keys,_b5a,_b5b,_b5c,_b5d,_b5e,_b5f,_b60,_b61){
var _b62=_b52.getObject("idx.oneui",true);
var dojo={},_b63={};
var _b64=function(){
log.error("dijit/form/Button has been used without being loaded");
};
var _b65=function(){
log.error("dijit/form/TextBox has been used without being loaded");
};
var _b66=function(){
log.error("idx/layout/MenuTabController has been used without being loaded");
};
return _b62.Header=_b51("idx.app.Header",[_b5e,_b5f],{baseClass:"idxHeader",primaryTitle:"",primaryBannerType:"thin",navigation:undefined,showNavigationDropDownArrows:true,primarySearch:undefined,user:undefined,showUserDropDownArrow:true,settings:undefined,showSettingsDropDownArrow:true,help:undefined,showHelpDropDownArrow:true,secondaryTitle:"",secondaryBannerType:"blue",secondarySubtitle:"",additionalContext:"",actions:undefined,contentContainer:"",contentTabsInline:false,secondarySearch:undefined,layoutType:"variable",templateString:"<div role=\"banner\">"+"<div style=\"visibility: hidden; display: none;\" data-dojo-attach-point=\"containerNode\">"+"</div>"+"<div data-dojo-attach-point=\"_mainContainerNode\">"+"</div>"+"</div>",_getComputedUserName:function(){
return (this.user&&(typeof this.user.displayName=="function"))?this.user.displayName():(this.user.displayName||"");
},_getComputedUserImage:function(){
return (this.user&&(typeof this.user.displayImage=="function"))?this.user.displayImage():this.user.displayImage;
},_getComputedUserMessage:function(){
var _b67=this._getComputedUserName(),_b68=((typeof this.user.messageName=="function")?this.user.messageName():this.user.messageName)||_b67,_b69=_b68;
if(this.user&&this.user.message){
var _b6a=(typeof this.user.message=="function")?this.user.message():this.user.message;
_b69=_b5a.substitute(_b6a,this.user,function(_b6b,key){
switch(key){
case "messageName":
return _b68;
case "displayName":
return _b67;
default:
return _b6b||"";
}
});
}
return _b69;
},_setUserDisplayNameAttr:function(_b6c){
this.user=this.user||{};
this.user.displayName=_b6c;
this._refreshUser();
},_setUserDisplayImageAttr:function(_b6d){
this.user=this.user||{};
this.user.displayImage=_b6d;
this._refreshUser();
},_setUserMessageNameAttr:function(_b6e){
this.user=this.user||{};
this.user.messageName=_b6e;
this._refreshUser();
},_setUserMessageAttr:function(_b6f){
this.user=this.user||{};
this.user.message=_b6f;
this._refreshUser();
},_prepareMenu:function(menu,_b70,_b71,_b72,_b73,_b74){
if(!_b74){
_b74=[];
}
if(menu._popupWrapper){
menu.placeAt(menu._popupWrapper);
}
if(_b70){
if(_b70[0]){
var _b75=null;
var _b76=_b54.after(menu,"onOpen",function(){
if(menu._popupWrapper){
if(!menu._oneuiWrapper){
menu._oneuiWrapper=_b58.create("div",{"class":"idxHeaderContainer "+_b70[0]},_b53.body());
var _b77=null;
_b77=_b54.after(menu,"destroy",function(){
_b58.destroy(menu._oneuiWrapper);
delete menu._oneuiWrapper;
if(_b77){
_b77.remove();
}
});
_b74.push(_b77);
}
menu._oneuiWrapper.appendChild(menu._popupWrapper);
}
});
_b75=_b54.after(menu,"destroy",function(){
if(_b76){
_b76.remove();
}
if(_b75){
_b75.remove();
}
});
_b74.push(_b75);
_b74.push(_b76);
}
var _b78=(_b70.length>1)?_b70.slice(1):_b70,me=this;
_b50.forEach(menu.getChildren(),function(_b79){
if(_b79.popup){
me._prepareMenu(_b79.popup,_b78,undefined,undefined,undefined,_b74);
}
if(_b79.currentPage){
_b57.add(_b79.domNode,"idxHeaderNavCurrentPage");
}
});
}
if(_b72){
var _b7a=_b72;
menu._scheduleOpen=function(_b7b,_b7c,_b7d){
if(!this._openTimer){
var ltr=menu.isLeftToRight(),_b7e=_b5c.around(_b5b._createWrapper(menu),_b7a,_b73?["below-alt","below","above-alt","above"]:["below","below-alt","above","above-alt"],ltr,menu.orient?_b52.hitch(menu,"orient"):null);
if(!ltr){
_b7e.x=_b7e.x+_b7e.w;
}
this._openTimer=setTimeout(_b52.hitch(this,function(){
delete this._openTimer;
this._openMyself({target:_b7b,iframe:_b7c,coords:_b7e});
}),1);
}
};
menu.leftClickToOpen=true;
if(_b71){
menu.bindDomNode(_b71);
}
}
return _b74;
},_refreshUser:function(){
if(this.userNode){
var name=this._getComputedUserName(),_b7f=this._getComputedUserImage(),msg=this._getComputedUserMessage();
_b56.set(this.userNode,"title",name);
_b56.set(this.userImageNode,"src",_b7f||"");
_b59.set(this.userImageNode,"display",_b7f?"block":"none");
this.userTextNode.innerHTML=msg;
_b57.replace(this.userNode,msg?"idxHeaderUserName":"idxHeaderUserNameNoText","idxHeaderUserName idxHeaderUserNameNoText");
}
},_injectTemplate:function(_b80,_b81){
var _b82=_b5f.getCachedTemplate(_b81,true);
var node;
if(_b52.isString(_b82)){
node=_b58.toDom(this._stringRepl(_b82));
}else{
node=_b82.cloneNode(true);
}
this._attachTemplateNodes(node,function(n,p){
return n.getAttribute(p);
});
_b80.appendChild(node);
},postMixInProperties:function(){
this._nls=_b61.getResources("idx/app/Header",this.lang);
},_setup:function(){
if(this.contentContainer&&this.secondaryBannerType&&this.secondaryBannerType.toLowerCase()=="white"){
require.log("*** Warning: Header will not display content tabs when secondaryBannerType is \"white\". Specify a different type to see content tabs.");
}
var _b83=this.primaryTitle,_b84=true,_b85=this.help,_b86=this.sharing,_b87=this.settings,_b88=this.user,_b89=this.navigation,_b8a=this.primarySearch,_b8b=this.secondaryTitle||this.secondarySubtitle,_b8c=this.contextActions,_b8d=this.secondarySearch,_b8e=this.contentContainer&&(!this.secondaryBannerType||(this.secondaryBannerType.toLowerCase()!="white")),_b8f=_b8e&&(this.contentTabsInline||!_b8b),_b90=this.secondaryBannerType&&(this.secondaryBannerType.toLowerCase()=="white"),_b91=_b8e&&!_b8f,_b92=_b83||_b84||_b85||_b87||_b86||_b88||_b89||_b8a,_b93=_b8b||_b8c||_b8d||_b8f,_b94=_b91,_b95;
if(_b92||_b93||_b94){
_b57.add(this.domNode,"idxHeaderContainer");
if(this.primaryBannerType&&(this.primaryBannerType.toLowerCase()=="thick")){
_b57.add(this._mainContainerNode,"idxHeaderPrimaryThick");
}else{
_b57.add(this._mainContainerNode,"idxHeaderPrimaryThin");
}
if(this.secondaryBannerType&&((this.secondaryBannerType.toLowerCase()=="lightgrey")||(this.secondaryBannerType.toLowerCase()=="lightgray"))){
_b57.add(this._mainContainerNode,"idxHeaderSecondaryGray");
_b57.add(this._mainContainerNode,_b94?"idxHeaderSecondaryGrayDoubleRow":"idxHeaderSecondaryGraySingleRow");
_b95=_b92;
}else{
if(this.secondaryBannerType&&(this.secondaryBannerType.toLowerCase()=="white")){
_b57.add(this._mainContainerNode,"idxHeaderSecondaryWhite");
_b57.add(this._mainContainerNode,_b94?"idxHeaderSecondaryWhiteDoubleRow":"idxHeaderSecondaryWhiteSingleRow");
_b95=_b92;
}else{
_b57.add(this._mainContainerNode,"idxHeaderSecondaryBlue");
_b57.add(this._mainContainerNode,(_b94)?"idxHeaderSecondaryBlueDoubleRow":"idxHeaderSecondaryBlueSingleRow");
_b95=_b92&&!_b93&&!_b94;
}
}
_b57.add(this._mainContainerNode,_b94?"idxHeaderSecondaryDoubleRow":"idxHeaderSecondarySingleRow");
if(this.layoutType&&(this.layoutType.toLowerCase()=="fixed")){
_b57.add(this._mainContainerNode,"idxHeaderWidthFixed");
}else{
_b57.add(this._mainContainerNode,"idxHeaderWidthLiquid");
}
}
var _b96=[],_b97=[],me=this;
if(_b8a||_b8d||_b8c){
_b96.push("dijit/form/Button");
_b97.push(function(obj){
_b64=obj;
});
}
if(_b8a||_b8d){
_b96.push("dijit/form/TextBox");
_b97.push(function(obj){
_b65=obj;
});
}
if(_b8e){
_b96.push("idx/layout/MenuTabController");
_b97.push(function(obj){
_b66=obj;
});
}
require(_b96,function(){
for(var i=0;i<_b97.length;i++){
_b97[i](arguments[i]);
}
if(_b92){
me._injectTemplate(me._mainContainerNode,"<div class=\"idxHeaderPrimary\">"+"<div class=\"idxHeaderPrimaryInner\" data-dojo-attach-point=\"primaryBannerNode\">"+"<ul class=\"idxHeaderPrimaryActionsLeading\" data-dojo-attach-point=\"_leadingGlobalActionsNode\">"+"</ul>"+"<ul class=\"idxHeaderPrimaryActionsTrailing\" data-dojo-attach-point=\"_trailingGlobalActionsNode\">"+"</ul>"+"</div>"+"</div>");
}
if(_b84&&me._logoPosition=="leading"){
me._renderLogo(me._leadingGlobalActionsNode);
}
if(_b83){
me._renderPrimaryTitle(me._leadingGlobalActionsNode);
}
if(_b8a){
me._renderPrimarySearch(me._trailingGlobalActionsNode);
}
if(_b88){
me._renderUser(me._trailingGlobalActionsNode);
}
if(_b86){
me._renderSharing(me._trailingGlobalActionsNode,_b88);
}
if(_b87){
me._renderSettings(me._trailingGlobalActionsNode,_b88);
}
if(_b85){
me._renderHelp(me._trailingGlobalActionsNode,_b87||_b86||_b88);
}
if((me.user&&me.user.actions)||me.sharing||me.settings||me.help){
me._trailingGlobalActionsNode.setAttribute("role","menubar");
}
if(_b84&&me._logoPosition!="leading"){
me._renderLogo(me._trailingGlobalActionsNode);
}
if(_b89){
var _b98=_b58.create("li",{"class":"idxHeaderPrimaryNav"},me._leadingGlobalActionsNode,"last");
me._renderNavigation(_b98);
}
if(_b95){
me._injectTemplate(me._mainContainerNode,"<div class=\"idxHeaderBlueLip\">"+"</div>");
}
if(_b93){
me._injectTemplate(me._mainContainerNode,"<div class=\"idxHeaderSecondary\"> "+"<div class=\"idxHeaderSecondaryInner\" data-dojo-attach-point=\"secondaryBannerNode\"> "+"<div class=\"idxHeaderSecondaryLeading\" data-dojo-attach-point=\"leadingSecondaryBannerNode\">"+"</div>"+"<div class=\"idxHeaderSecondaryTrailing\" data-dojo-attach-point=\"trailingSecondaryBannerNode\">"+"</div>"+"</div>"+"</div>");
}
if(_b8b){
me._renderSecondaryTitle(me.leadingSecondaryBannerNode);
}
if(_b8f){
me._renderContent(me.leadingSecondaryBannerNode,false);
}
if(_b8c){
me._renderContextActions(me.trailingSecondaryBannerNode);
}
if(_b8d){
me._renderSecondarySearch(me.trailingSecondaryBannerNode);
}
if(_b90){
me._renderSecondaryInnerBorder(me.secondaryBannerNode);
}
if(_b91){
me._renderContent(me._mainContainerNode,true);
}
if(me._refreshing>0){
me._refreshing--;
}
if(me._refreshing==0){
me._refreshRequired=false;
}else{
me._doRefresh();
}
});
},_reset:function(){
_b57.remove(this.domNode,"idxHeaderContainer");
_b57.remove(this._mainContainerNode,"idxHeaderPrimaryThick");
_b57.remove(this._mainContainerNode,"idxHeaderPrimaryThin");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryGray");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryGrayDoubleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryGraySingleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryWhite");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryWhiteDoubleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryWhiteSingleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryBlue");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryBlueDoubleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryBlueSingleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondaryDoubleRow");
_b57.remove(this._mainContainerNode,"idxHeaderSecondarySingleRow");
_b57.remove(this._mainContainerNode,"idxHeaderWidthFixed");
_b57.remove(this._mainContainerNode,"idxHeaderWidthLiquid");
if(this.help){
this.help=_b5d.byId(this.help);
if(this.help){
this.help.placeAt(this.containerNode);
}
}
this._clearHandles("_helpHandles");
if(this.settings){
this.settings=_b5d.byId(this.settings);
if(this.settings){
this.settings.placeAt(this.containerNode);
}
}
this._clearHandles("_settingsHandles");
if(this.sharing){
this.sharing=_b5d.byId(this.sharing);
if(this.sharing){
this.sharing.placeAt(this.containerNode);
}
}
this._clearHandles("_sharingHandles");
if(this.user&&this.user.actions){
this.user.actions=_b5d.byId(this.user.actions);
if(this.user.actions){
this.user.actions.placeAt(this.containerNode);
}
}
this._clearHandles("_actionsHandles");
if(this.navigation){
this.navigation=_b5d.byId(this.navigation);
if(this.navigation){
this.navigation.placeAt(this.containerNode);
}
}
this._clearHandles("_navHandles");
this._clearHandles("_controllerHandlers");
_b50.forEach(_b5d.findWidgets(this._mainContainerNode),function(_b99){
_b99.destroy();
});
var _b9a=[];
_b50.forEach(this._mainContainerNode.childNodes,function(node){
_b9a.push(node);
});
_b50.forEach(_b9a,function(node){
_b58.destroy(node);
});
this._leadingGlobalActionsNode=null;
this._trailingGlobalActionsNode=null;
this.primaryTitleTextNode=null;
this.primaryBannerNode=null;
this.leadingSecondaryBannerNode=null;
this.trailingSecondaryBannerNode=null;
this._helpNode=null;
this._settingsNode=null;
this._sharingNode=null;
this.userNode=null;
this.userImageNode=null;
this.userTextNode=null;
this.primarySearchTextNode=null;
this.primarySearchButtonNode=null;
this.secondaryTitleTextNode=null;
this._secondaryTitleSeparatorNode=null;
this.secondarySubtitleTextNode=null;
this.additionalContextTextNode=null;
this._contextActionsNode=null;
if(this.contextActionNodes){
delete this.contextActionNodes;
}
this.secondarySearchTextNode=null;
this.secondarySearchButtonNode=null;
this.contentControllerNode=null;
},constructor:function(args,node){
this._refreshing=0;
},destroy:function(){
if(this.navigation){
if(this.navigation.destroyRecursive){
this.navigation.destroyRecursive();
}else{
if(this.navigation.destroy){
this.navigation.destroy();
}
}
this.navigation=null;
}
this._clearHandles("_navHandles");
if(this._controller){
if(this._controller.destroyRecursive){
this._controller.destroyRecursive();
}else{
if(this._controller.destroy){
this._controller.destroy();
}
}
delete this._controller;
}
this._clearHandles("_controllerHandles");
if(this.settings){
if(this.settings.destroyRecursive){
this.settings.destroyRecursive();
}else{
if(this.settings.destroy){
this.settings.destroy();
}
}
this.settings=null;
}
this._clearHandles("_settingsHandles");
if(this.sharing){
if(this.sharing.destroyRecursive){
this.sharing.destroyRecursive();
}else{
if(this.sharing.destroy){
this.sharing.destroy();
}
}
this.sharing=null;
}
this._clearHandles("_sharingHandles");
if(this.help){
if(this.help.destroyRecursive){
this.help.destroyRecursive();
}else{
if(this.help.destroy){
this.help.destroy();
}
}
this.help=null;
}
this._clearHandles("_helpHandles");
if(this.user&&this.user.actions){
if(this.user.actions.destroyRecursive){
this.user.actions.destroyRecursive();
}else{
if(this.user.actions.destroy){
this.user.actions.destroy();
}
}
this.user.actions=null;
this.user=null;
}
this._clearHandles("_actionsHandles");
this.inherited(arguments);
},_clearHandles:function(name){
if(!(name in this)){
return;
}
var _b9b=this[name];
_b50.forEach(_b9b,function(_b9c){
_b9c.remove();
});
delete this[name];
},buildRendering:function(){
this.inherited(arguments);
var _b9d=_b60.getCSSOptions(this.baseClass+"Options",this.domNode,null,{logo:"trailing"});
this._logoPosition=_b9d.logo;
console.log("LOGO POSITION: "+this._logoPosition);
this._setup();
},_setContentTabsInlineAttr:function(_b9e){
var _b9f=this.contentTabsInline;
this.contentTabsInline=_b9e;
if(_b9f!=_b9e){
this._refresh();
}
},_setContentContainerAttr:function(_ba0){
var _ba1=this.contentContainer;
this.contentContainer=_ba0;
if(!_b60.widgetEquals(_ba1,_ba0)){
this._refresh();
}
},_setActionsAttr:function(_ba2){
var _ba3=this.actions;
this.actions=_ba2;
if(_ba3!==_ba2){
this._refresh();
}
},_setAdditionalContextAttr:function(_ba4){
var _ba5=this.additionalContext;
var node=this.additionalContextTextNode;
this.additionalContext=_ba4;
if(node){
node.innerHTML=(_ba4)?_ba4:"";
}
},_setSecondaryBannerTypeAttr:function(_ba6){
var _ba7=this.secondaryBannerType;
this.secondaryBannerType=_ba6;
if(_ba7!=_ba6){
this._refresh();
}
},_setPrimaryTitleAttr:function(_ba8){
var _ba9=this.primaryTitle;
var node=this.primaryTitleTextNode;
this.primaryTitle=_ba8;
if((_ba8&&!_ba9)||(_ba9&&!_ba8)){
this._refresh();
}else{
if(node&&_ba8){
node.innerHTML=_ba8;
}
}
},_setSecondaryTitleAttr:function(_baa){
var _bab=this.secondaryTitle;
var node=this.secondaryTitleTextNode;
this.secondaryTitle=_baa;
if((_baa&&!_bab)||(_bab&&!_baa)){
this._refresh();
}else{
if(node&&_baa){
node.innerHTML=_baa;
}
}
},_setLayoutTypeAttr:function(_bac){
var _bad=this.layoutType;
this.layoutType=_bac;
if(this._mainContainerNode){
if(this.layoutType&&(this.layoutType.toLowerCase()=="fixed")){
_b57.add(this._mainContainerNode,"idxHeaderWidthFixed");
}else{
_b57.add(this._mainContainerNode,"idxHeaderWidthLiquid");
}
}
},_setSecondarySubtitleAttr:function(_bae){
var _baf=this.secondarySubtitle;
var node=this.secondarySubtitleTextNode;
this.secondarySubtitle=_bae;
if((_bae&&!_baf)||(_baf&&!_bae)){
this._refresh();
}else{
if(node&&_bae){
node.innerHTML=_bae;
}
}
},_setShowHelpDropDownArrowAttr:function(_bb0){
var _bb1=this.showHelpDropDownArrow;
this.showHelpDropDownArrow=_bb0;
if(this._helpNode){
_b57.toggle(this._helpNode,"idxHeaderDropDown",this.showHelpDropDownArrow);
}
},_setShowSettingsDropDownArrowAttr:function(_bb2){
var _bb3=this.showSettingsDropDownArrow;
this.showSettingsDropDownArrow=_bb2;
if(this._settingsNode){
_b57.toggle(this._settingsNode,"idxHeaderDropDown",this.showSettingsDropDownArrow);
}
},_setShowSharingDropDownArrowAttr:function(_bb4){
var _bb5=this.showSharingDropDownArrow;
this.showSharingDropDownArrow=_bb4;
if(this._sharingNode){
_b57.toggle(this._sharingNode,"idxHeaderDropDown",this.showSharingDropDownArrow);
}
},_setShowUserDropDownArrowAttr:function(_bb6){
var _bb7=this.showUserDropDownArrow;
this.showUserDropDownArrow=_bb6;
if(this.userNode){
_b57.toggle(this.userNode,"idxHeaderDropDown",this.showUserDropDownArrow);
}
},_setUserAttr:function(_bb8){
var _bb9=this.user;
this.user=_bb8;
if(_bb9===_bb8){
return;
}else{
if((_bb9&&!_bb8)||(_bb8&&!_bb9)){
this._refresh();
}else{
if(_bb9&&_bb9.actions!==_bb8.actions){
this._refresh();
}else{
this._refreshUser();
}
}
}
},_setShowNavigationDropDownArrowsAttr:function(_bba){
var _bbb=this.showNavigationDropDownArrows;
this.showNavigationDropDownArrows=_bba;
if(_bbb!=_bba){
this._refresh();
}
},_setPrimarySearchAttr:function(_bbc){
var _bbd=this.primarySearch;
this.primarySearch=_bbc;
if(_bbd!==_bbc){
this._refresh();
}
},_setSecondarySearchAttr:function(_bbe){
var _bbf=this.secondarySearch;
this.secondarySearch=_bbe;
if(_bbf!==_bbe){
this._refresh();
}
},_setNavigationAttr:function(_bc0){
var _bc1=this.navigation;
this.navigation=_bc0;
if(!_b60.widgetEquals(_bc1,_bc0)){
this._refresh();
}
},_setSettingsAttr:function(_bc2){
var _bc3=this.settings;
this.settings=_bc2;
if(!_b60.widgetEquals(_bc3,_bc2)){
this._refresh();
}
},_setSharingAttr:function(_bc4){
var _bc5=this.sharing;
this.sharing=_bc4;
if(!_b60.widgetEquals(_bc5,_bc4)){
this._refresh();
}
},_setHelpAttr:function(_bc6){
var _bc7=this.help;
this.help=_bc6;
if(!_b60.widgetEquals(_bc7,_bc6)){
this._refresh();
}
},deferRefresh:function(){
this._refreshDeferred=true;
},refresh:function(){
if(this._refreshDeferred){
this._refreshDeferred=false;
}
if(this._refreshRequired){
this._refresh();
}
},_refresh:function(){
this._refreshRequired=true;
if(this._started&&(!this._refreshDeferred)){
this._refreshing++;
if(this._refreshing==1){
this._doRefresh();
}
}else{
console.log("Deferring header refresh.  started=[ "+this._started+" ], deferred=[ "+this._refreshDeferred+" ]");
}
},_doRefresh:function(){
this._reset();
this._setup();
},_setupChild:function(_bc8){
if(!("region" in _bc8)){
return;
}
var _bc9=_bc8.region;
switch(_bc9){
case "navigation":
this.set("navigation",_bc8);
break;
case "settings":
this.set("settings",_bc8);
break;
case "sharing":
this.set("sharing",_bc8);
break;
case "help":
this.set("help",_bc8);
break;
default:
console.log("WARNING: Found child with unrecognized region: "+_bc9);
break;
}
},addChild:function(_bca){
this.inherited(arguments);
this._setupChild(_bca);
},removeChild:function(_bcb){
if(_bcb===this.help){
this.set("help",null);
}
if(_bcb===this.navigation){
this.set("navigation",null);
}
if(_bcb===this.settings){
this.set("settings",null);
}
if(_bcb===this.sharing){
this.set("sharing",null);
}
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this.deferRefresh();
var _bcc=this.getChildren();
for(var _bcd=0;_bcd<_bcc.length;_bcd++){
var _bce=_bcc[_bcd];
this._setupChild(_bce);
}
this.refresh();
},_renderPrimaryTitle:function(_bcf){
this._injectTemplate(_bcf,"<li role=\"presentation\">"+"<span>"+"<div class=\"idxHeaderPrimaryTitle\" id=\"${id}_PrimaryTitle\" data-dojo-attach-point=\"primaryTitleTextNode\">"+"${primaryTitle}"+"</div>"+"</span>"+"</li>");
},_renderLogo:function(_bd0){
console.log("RENDERING LOGO....");
this._injectTemplate(_bd0,"<li role=\"presentation\" class=\"idxHeaderPrimaryAction idxHeaderLogoItem\">"+"<span>"+"<div class=\"idxHeaderLogoBox\">"+"<div class=\"idxHeaderLogo\" alt=\"${_nls.ibmlogo}\">"+"<span class=\"idxTextAlternative\">${_nls.ibmlogo}</span>"+"</div>"+"</div>"+"</span>"+"</li>");
},_renderSeparator:function(_bd1){
this._injectTemplate(_bd1,"<li role=\"presentation\" class=\"idxHeaderPrimaryAction idxHeaderSeparator\"><span></span></li>");
},_renderHelp:function(_bd2,_bd3){
if(_bd3){
this._renderSeparator(_bd2);
}
this._injectTemplate(_bd2,"<li class=\"idxHeaderPrimaryAction idxHeaderHelp\">"+"<a tabindex=\"0\" href=\"javascript://\" data-dojo-attach-point=\"_helpNode\" title=\"${_nls.actionHelp}\" role=\"presentation\">"+"<span class=\"idxHeaderHelpIcon\">"+"<span class=\"idxTextAlternative\">${_nls.actionHelp}</span>"+"</span>"+"<span class=\"idxHeaderDropDownArrow\">"+"<span class=\"idxTextAlternative\">(v)</span>"+"</span>"+"</a>"+"</li>");
if(this.help){
this.help=_b5d.byId(this.help);
this._clearHandles("_helpHandles");
this._helpHandles=this._prepareMenu(this.help,["oneuiHeaderGlobalActionsMenu","oneuiHeaderGlobalActionsSubmenu"],this._helpNode,this._helpNode,true);
_b57.toggle(this._helpNode,"idxHeaderDropDown",this.showHelpDropDownArrow);
this._helpNode.setAttribute("role","menuitem");
this._helpNode.setAttribute("aria-haspopup",true);
}
},_renderSettings:function(_bd4,_bd5){
if(_bd5){
this._renderSeparator(_bd4);
}
this._injectTemplate(_bd4,"<li class=\"idxHeaderPrimaryAction idxHeaderTools\">"+"<a tabindex=\"0\" href=\"javascript://\" data-dojo-attach-point=\"_settingsNode\" title=\"${_nls.actionSettings}\" role=\"presentation\">"+"<span class=\"idxHeaderSettingsIcon\">"+"<span class=\"idxTextAlternative\">${_nls.actionSettings}</span>"+"</span>"+"<span class=\"idxHeaderDropDownArrow\">"+"<span class=\"idxTextAlternative\">(v)</span>"+"</span>"+"</a>"+"</li>");
if(this.settings){
this.settings=_b5d.byId(this.settings);
this._clearHandles("_settingsHandles");
this._settingsHandles=this._prepareMenu(this.settings,["oneuiHeaderGlobalActionsMenu","oneuiHeaderGlobalActionsSubmenu"],this._settingsNode,this._settingsNode,true);
_b57.toggle(this._settingsNode,"idxHeaderDropDown",this.showSettingsDropDownArrow);
this._settingsNode.setAttribute("role","menuitem");
this._settingsNode.setAttribute("aria-haspopup",true);
}
},_renderSharing:function(_bd6,_bd7){
if(_bd7){
this._renderSeparator(_bd6);
}
this._injectTemplate(_bd6,"<li class=\"idxHeaderPrimaryAction idxHeaderTools\">"+"<a tabindex=\"0\" href=\"javascript://\" data-dojo-attach-point=\"_sharingNode\" title=\"${_nls.actionShare}\" role=\"presentation\">"+"<span class=\"idxHeaderShareIcon\">"+"<span class=\"idxTextAlternative\">${_nls.actionShare}</span>"+"</span>"+"<span class=\"idxHeaderDropDownArrow\">"+"<span class=\"idxTextAlternative\">(v)</span>"+"</span>"+"</a>"+"</li>");
if(this.sharing){
this.sharing=_b5d.byId(this.sharing);
this._clearHandles("_sharingHandles");
this._sharingHandles=this._prepareMenu(this.sharing,["oneuiHeaderGlobalActionsMenu","oneuiHeaderGlobalActionsSubmenu"],this._sharingNode,this._sharingNode,true);
_b57.toggle(this._sharingNode,"idxHeaderDropDown",this.showSharingDropDownArrow);
this._sharingNode.setAttribute("role","menuitem");
this._sharingNode.setAttribute("aria-haspopup",true);
}
},_renderUser:function(_bd8){
this._injectTemplate(_bd8,"<li class=\"idxHeaderPrimaryAction\">"+"<a tabindex=\"0\" href=\"javascript://\" data-dojo-attach-point=\"userNode\" class=\"idxHeaderUserNameNoText\" role=\"presentation\">"+"<span class=\"idxHeaderUserIcon\">"+"<img data-dojo-attach-point=\"userImageNode\" class=\"idxHeaderUserIcon\" alt=\"\" />"+"</span>"+"<span class=\"idxHeaderUserText\" data-dojo-attach-point=\"userTextNode\">"+"</span>"+"<span class=\"idxHeaderDropDownArrow\">"+"<span class=\"idxTextAlternative\">(v)</span>"+"</span>"+"</a>"+"</li>");
this._refreshUser();
if(this.user&&this.user.actions){
this.user.actions=_b5d.byId(this.user.actions);
this._clearHandles("_actionsHandles");
this._actionsHandles=this._prepareMenu(this.user.actions,["oneuiHeaderGlobalActionsMenu","oneuiHeaderGlobalActionsSubmenu"],this.userNode,this.userNode,true);
_b57.toggle(this.userNode,"idxHeaderDropDown",this.showUserDropDownArrow);
this.userNode.setAttribute("role","menuitem");
this.userNode.setAttribute("aria-haspopup",true);
}
},_renderNavigation:function(_bd9){
this.navigation=((typeof this.navigation=="object")&&("nodeType" in this.navigation))?_b5d.byNode(this.navigation):_b5d.byId(this.navigation);
if(!this.navigation){
require.log("WARNING: navigation widget not found");
}else{
this.navigation.placeAt(_bd9);
this.navigation.startup();
var _bda=this.navigation.getChildren();
if((_bda.length==1)&&(_bda[0].label=="")){
var _bdb=_bda[0];
_b57.toggle(_bdb.containerNode,"idxHeaderNavigationHome",true);
_b57.toggle(_bdb.containerNode,"idxHeaderNavigationHomeButtonOnly",!_bdb.popup);
_b56.set(_bdb.domNode,"title",this._nls.homeButton);
_b58.place("<span class='idxTextAlternative'>"+this._nls.homeButton+"</span>",_bdb.containerNode,"first");
}else{
if(this.showNavigationDropDownArrows){
for(var i=0;i<_bda.length;i++){
if(_bda[i].popup){
var _bdc=_b55(".idxHeaderDropDownArrow",_bda[i].focusNode);
_b57.toggle(_bda[i].domNode,"idxHeaderDropDown",true);
if(_bdc.length>0){
continue;
}
this._injectTemplate(_bda[i].focusNode,"<span class=\"idxHeaderDropDownArrow\"><span class=\"idxTextAlternative\">(v)</span></span>");
}
}
}
}
var node=this.navigation.domNode.firstChild,del;
while(node){
del=node;
node=node.nextSibling;
if((del.nodeType==3)&&(!del.nodeValue.match(/\S/))){
this.navigation.domNode.removeChild(del);
}
}
this._clearHandles("_navHandles");
this._navHandles=this._prepareMenu(this.navigation,[null,"oneuiHeaderNavigationMenu","oneuiHeaderNavigationSubmenu"]);
}
},_renderPrimarySearch:function(_bdd){
this._injectTemplate(_bdd,"<li role=\"search\" aria-label=\""+this.id+" ${_nls.primarySearchLabelSuffix}\" class=\"idxHeaderSearchContainer\">"+"<input type=\"text\" data-dojo-attach-point=\"primarySearchTextNode\" />"+"<input type=\"image\" data-dojo-attach-point=\"primarySearchButtonNode\" />"+"</li>");
this.primarySearch.onChange=_b52.isFunction(this.primarySearch.onChange)?this.primarySearch.onChange:new Function("value",this.primarySearch.onChange);
this.primarySearch.onSubmit=_b52.isFunction(this.primarySearch.onSubmit)?this.primarySearch.onSubmit:new Function("value",this.primarySearch.onSubmit);
var me=this,_bde=("entryPrompt" in this.primarySearch)?this.primarySearch.entryPrompt:this._nls.searchEntry,_bdf=("submitPrompt" in this.primarySearch)?this.primarySearch.submitPrompt:this._nls.searchSubmit;
var text=new _b65({trim:true,placeHolder:_bde,intermediateChanges:true,title:_bde},this.primarySearchTextNode);
text.own(_b54.after(text,"onChange",function(){
me._onPrimarySearchChange(text.attr("value"));
}));
text.own(text.on("keyup",function(_be0){
if(_be0.keyCode==keys.ENTER){
me._onPrimarySearchSubmit(text.attr("value"));
}
}));
var _be1=new _b64({label:_bdf,showLabel:false,iconClass:"idxHeaderSearchButton"},this.primarySearchButtonNode);
_be1.own(_b54.after(_be1,"onClick",function(){
me._onPrimarySearchSubmit(text.attr("value"));
}));
},_renderSecondaryTitle:function(_be2){
this._injectTemplate(_be2,"<span class=\"idxHeaderSecondaryTitleContainer\">"+"<span class=\"idxHeaderSecondaryTitle\" id=\"${id}_SecondaryTitle\"  data-dojo-attach-point=\"secondaryTitleTextNode\">"+"${secondaryTitle}"+"</span>"+"<span class=\"idxHeaderSecondarySubtitle\" data-dojo-attach-point=\"_secondaryTitleSeparatorNode\">"+"&nbsp;&ndash;&nbsp;"+"</span>"+"<span class=\"idxHeaderSecondarySubtitle\" data-dojo-attach-point=\"secondarySubtitleTextNode\">"+"${secondarySubtitle}"+"</span>"+"&nbsp;&nbsp;"+"<span class=\"idxHeaderSecondaryAdditionalContext\" data-dojo-attach-point=\"additionalContextTextNode\">"+"${additionalContext}"+"</span>"+"</span>");
_b59.set(this._secondaryTitleSeparatorNode,"display",(this.secondaryTitle&&this.secondarySubtitle)?"":"none");
},_renderContextActions:function(_be3){
this._injectTemplate(_be3,"<div class=\"idxHeaderSecondaryActions\" data-dojo-attach-point=\"_contextActionsNode\"></div>");
this.contextActionNodes=[];
for(var i=0;i<this.contextActions.length;i++){
this._injectTemplate(this._contextActionsNode,"<button type=\"button\" data-dojo-attach-point=\"_nextActionNode\"></button>");
new _b64(this.contextActions[i],this._nextActionNode);
this.contextActionNodes.push(this._nextActionNode);
delete this._nextActionNode;
}
},_renderSecondarySearch:function(_be4){
this._injectTemplate(_be4,"<div role=\"search\" aria-label=\""+this.id+" ${_nls.secondarySearchLabelSuffix}\" class=\"idxHeaderSearchContainer\">"+"<input type=\"text\" data-dojo-attach-point=\"secondarySearchTextNode\" />"+"<input type=\"image\" data-dojo-attach-point=\"secondarySearchButtonNode\" />"+"</div>");
this.secondarySearch.onChange=_b52.isFunction(this.secondarySearch.onChange)?this.secondarySearch.onChange:new Function("value",this.secondarySearch.onChange);
this.secondarySearch.onSubmit=_b52.isFunction(this.secondarySearch.onSubmit)?this.secondarySearch.onSubmit:new Function("value",this.secondarySearch.onSubmit);
var me=this,_be5=("entryPrompt" in this.secondarySearch)?this.secondarySearch.entryPrompt:this._nls.searchEntry,_be6=("submitPrompt" in this.secondarySearch)?this.secondarySearch.submitPrompt:this._nls.searchSubmit;
var text=new _b65({trim:true,placeHolder:_be5,intermediateChanges:true,title:_be5},this.secondarySearchTextNode);
text.own(_b54.after(text,"onChange",function(){
me._onSecondarySearchChange(text.attr("value"));
}));
text.own(text.on("keyup",function(_be7){
if(_be7.keyCode==keys.ENTER){
me._onSecondarySearchSubmit(text.attr("value"));
}
}));
var _be8=new _b64({label:_be6,showLabel:false,iconClass:"idxHeaderSearchButton"},this.secondarySearchButtonNode);
_be8.own(_b54.after(_be8,"onClick",function(){
me._onSecondarySearchSubmit(text.attr("value"));
}));
},_renderSecondaryInnerBorder:function(_be9){
this._injectTemplate(_be9,"<div class=\"idxHeaderSecondaryInnerBorder\">"+"</div>");
},_renderContent:function(_bea,_beb){
this._injectTemplate(_bea,"<div class=\"oneuiContentContainer\">"+(_beb?"<div class=\"oneuiContentContainerInner\">":"")+"<div data-dojo-attach-point=\"contentControllerNode\"></div>"+(_beb?"</div>":"")+"</div>");
var _bec=new _b66({containerId:(typeof this.contentContainer==="string")?this.contentContainer:this.contentContainer.id,"class":"dijitTabContainerTop-tabs",useMenu:this._tabMenu,useSlider:this._tabSlider,buttonWidget:_b52.extend(idx.layout._PopupTabButton,{tabDropDownText:"",tabSeparatorText:"|"})},this.contentControllerNode),me=this;
this._clearHandles("_controllerHandles");
this._controllerHandles=this._prepareMenu(_bec._menuBtn,["oneuiHeader2ndLevMenu","oneuiHeader2ndLevSubmenu"]);
this._controllerHandles.push(_b54.after(_bec,"_bindPopup",function(page,_bed,_bee,_bef){
me._prepareMenu(_bef,["oneuiHeader2ndLevMenu","oneuiHeader2ndLevSubmenu"],_bee,_bed,this._controllerHandles);
},true));
_bec.startup();
_b59.set(_bec.containerNode,"width","auto");
this._controller=_bec;
var _bf0=_b5d.byId(this.contentContainer);
if(_bf0&&_bf0._started){
_bec.onStartup({children:_bf0.getChildren(),selected:_bf0.selectedChildWidget});
}
},_onPrimarySearchChange:function(_bf1){
this.primarySearch.onChange(_bf1);
},_onPrimarySearchSubmit:function(_bf2){
this.primarySearch.onSubmit(_bf2);
},_onSecondarySearchChange:function(_bf3){
this.secondarySearch.onChange(_bf3);
},_onSecondarySearchSubmit:function(_bf4){
this.secondarySearch.onSubmit(_bf4);
}});
});
},"idx/widget/HoverHelpTooltip":function(){
define(["dojo/_base/declare","dojo/_base/fx","dojo/keys","dojo/_base/array","dojo/dom","dojo/on","dojo/aspect","dojo/when","dojo/Deferred","dojo/dom-attr","dojo/_base/lang","dojo/_base/sniff","dijit/popup","dijit/focus","dojo/_base/event","dojo/dom-geometry","dojo/dom-construct","dojo/dom-class","dijit/registry","dijit/place","dijit/a11y","dojo/dom-style","dojo/_base/window","dijit/_base/manager","dijit/_Widget","dijit/_TemplatedMixin","dijit/Tooltip","dojo/has!dojo-bidi?../bidi/widget/HoverHelpTooltip","dojo/text!./templates/HoverHelpTooltip.html","dijit/dijit","dojo/i18n!./nls/Dialog","dojo/i18n!./nls/HoverHelpTooltip"],function(_bf5,fx,keys,_bf6,dom,on,_bf7,when,_bf8,_bf9,lang,has,_bfa,_bfb,_bfc,_bfd,_bfe,_bff,_c00,_c01,a11y,_c02,win,_c03,_c04,_c05,_c06,_c07,_c08,_c09,_c0a,_c0b){
var _c0c=lang.getObject("idx.oneui",true);
var _c0d=_bf5("idx.widget.HoverHelpTooltip",_c06,{showDelay:500,hideDelay:800,showLearnMore:false,learnMoreLinkValue:"#updateme",showCloseIcon:true,forceFocus:false,textDir:"auto",_onHover:function(e){
if(!_c0d._showTimer){
var _c0e=e.target;
_c0d._showTimer=setTimeout(lang.hitch(this,function(){
this.open(_c0e);
}),this.showDelay);
}
if(_c0d._hideTimer){
clearTimeout(_c0d._hideTimer);
delete _c0d._hideTimer;
}
},_onUnHover:function(){
if(_c0d._showTimer){
clearTimeout(_c0d._showTimer);
delete _c0d._showTimer;
}
if(!_c0d._hideTimer){
_c0d._hideTimer=setTimeout(lang.hitch(this,function(){
this.close();
}),this.hideDelay);
}
},open:function(_c0f){
if(_c0d._showTimer){
clearTimeout(_c0d._showTimer);
delete _c0d._showTimer;
}
if(!dom.byId(_c0f)){
return;
}
var _c10=_bf9.get(this.domNode,"aria-label");
_c0d.show(this.content||this.label||this.domNode.innerHTML,_c0f,this.position,!this.isLeftToRight(),this.textDir,this.showLearnMore,this.learnMoreLinkValue,this.showCloseIcon,this.forceFocus,_c10);
this._connectNode=_c0f;
this.onShow(_c0f,this.position);
},close:function(_c11){
if(this._connectNode){
var anim=_c0d.hide(this._connectNode,_c11);
delete this._connectNode;
this.onHide();
if(anim){
var d=new _bf8();
var _c12=[];
var _c13=function(){
if(d){
d.resolve();
}
_bf6.forEach(_c12,function(_c14){
if(_c14){
_c14.remove();
}
});
};
_c12.push(_bf7.after(anim,"onEnd",_c13));
_c12.push(_bf7.after(anim,"onStop",_c13));
if((anim.status()=="stopped")&&(!d.isResolved())){
_c13();
}
return d;
}else{
return null;
}
}
if(_c0d._showTimer){
clearTimeout(_c0d._showTimer);
delete _c0d._showTimer;
}
},_setConnectIdAttr:function(_c15){
_bf6.forEach(this._connections||[],function(_c16){
_bf6.forEach(_c16,lang.hitch(this,"disconnect"));
},this);
this._connectIds=_bf6.filter(lang.isArrayLike(_c15)?_c15:(_c15?[_c15]:[]),function(id){
return dom.byId(id);
});
this._connections=_bf6.map(this._connectIds,function(id){
var node=dom.byId(id);
return [this.connect(node,"onmouseenter","_onHover"),this.connect(node,"onmouseleave","_onUnHover"),this.connect(node,"onclick","_onHover"),this.connect(node,"onkeypress","_onConnectIdKey")];
},this);
this._set("connectId",_c15);
},_onConnectIdKey:function(evt){
var node=evt.target;
if(evt.charOrCode==keys.ENTER||evt.charOrCode==keys.SPACE||evt.charOrCode==" "||evt.charOrCode==keys.F1){
_c0d._showTimer=setTimeout(lang.hitch(this,function(){
this.open(node);
}),this.showDelay);
_bfc.stop(evt);
}
}});
var _c17=has("dojo-bidi")?"idx.widget._MasterHoverHelpTooltip_":"idx.widget._MasterHoverHelpTooltip";
var _c18=_bf5(_c17,[_c04,_c05],{duration:_c03.defaultDuration,templateString:_c08,learnMoreLabel:"",draggable:true,_firstFocusItem:null,_lastFocusItem:null,postMixInProperties:function(){
this.learnMoreLabel=_c0b.learnMoreLabel;
this.buttonClose=_c0a.closeButtonLabel;
},postCreate:function(){
win.body().appendChild(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
this.connect(this.domNode,"onkeypress","_onKey");
this.connect(this.domNode,"onmouseenter",lang.hitch(this,function(e){
if(_c0d._hideTimer){
clearTimeout(_c0d._hideTimer);
delete _c0d._hideTimer;
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
_c0d._hideTimer=setTimeout(lang.hitch(this,function(){
this.hide(this.aroundNode);
}),this.hideDelay);
}));
},show:function(_c19,_c1a,_c1b,rtl,_c1c,_c1d,_c1e,_c1f,_c20,_c21){
this._lastFocusNode=_c1a;
if(_c1d){
this.learnMoreNode.style.display="inline";
this.learnMoreNode.href=_c1e;
}else{
this.learnMoreNode.style.display="none";
}
if(_c1f||_c1f==null){
this.closeButtonNode.style.display="inline";
}else{
this.closeButtonNode.style.display="none";
}
this.connectorNode.hidden=false;
if(this.aroundNode&&this.aroundNode===_c1a&&this.containerNode.innerHTML==_c19){
return;
}
this.domNode.width="auto";
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.containerNode.innerHTML=_c19;
if(_c21){
_bf9.set(this.domNode,"aria-label",_c21);
}
this.set("textDir",_c1c);
this.containerNode.align=rtl?"right":"left";
var pos=_c01.around(this.domNode,_c1a,_c1b&&_c1b.length?_c1b:_c0d.defaultPosition,!rtl,lang.hitch(this,"orient"));
var _c22=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_c22.y+((_c22.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_c22.x+((_c22.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}
}
_c02.set(this.domNode,"opacity",0);
_bff.add(this.domNode,"dijitPopup");
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_c1a;
var _c23=_bf9.get(_c1a,"id");
if(typeof _c23=="string"){
_c09.setWaiState(this.containerNode,"labelledby",_c23);
}
if(_c20){
this.focus();
}
return;
},orient:function(node,_c24,_c25,_c26,_c27){
this.connectorNode.style.top="";
var _c28=_c26.w-this.connectorNode.offsetWidth;
node.className="idxOneuiHoverHelpTooltip "+{"MR-ML":"idxOneuiHoverHelpTooltipRight","ML-MR":"idxOneuiHoverHelpTooltipLeft","TM-BM":"idxOneuiHoverHelpTooltipAbove","BM-TM":"idxOneuiHoverHelpTooltipBelow","BL-TL":"idxOneuiHoverHelpTooltipBelow idxOneuiHoverHelpTooltipABLeft","TL-BL":"idxOneuiHoverHelpTooltipAbove idxOneuiHoverHelpTooltipABLeft","BR-TR":"idxOneuiHoverHelpTooltipBelow idxOneuiHoverHelpTooltipABRight","TR-BR":"idxOneuiHoverHelpTooltipAbove idxOneuiHoverHelpTooltipABRight","BR-BL":"idxOneuiHoverHelpTooltipRight","BL-BR":"idxOneuiHoverHelpTooltipLeft","TR-TL":"idxOneuiHoverHelpTooltipRight"}[_c24+"-"+_c25];
this.domNode.style.width="auto";
var size=_bfd.position(this.domNode);
var _c29=Math.min((Math.max(_c28,1)),size.w);
var _c2a=_c29<size.w;
this.domNode.style.width=_c29+"px";
if(_c2a){
this.containerNode.style.overflow="auto";
var _c2b=this.containerNode.scrollWidth;
this.containerNode.style.overflow="visible";
if(_c2b>_c29){
_c2b=_c2b+_c02.get(this.domNode,"paddingLeft")+_c02.get(this.domNode,"paddingRight");
this.domNode.style.width=_c2b+"px";
}
}
if(_c25.charAt(0)=="B"&&_c24.charAt(0)=="B"){
var mb=_bfd.getMarginBox(node);
var _c2c=this.connectorNode.offsetHeight;
if(mb.h>_c26.h){
var _c2d=_c26.h-((_c27.h+_c2c)>>1);
this.connectorNode.style.top=_c2d+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(_c27.h/2-_c2c/2,0),mb.h-_c2c)+"px";
this.connectorNode.style.top="";
}
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
return Math.max(0,size.w-_c28);
},focus:function(){
if(this._focus){
return;
}
this._getFocusItems(this.outerContainerNode);
this._focus=true;
_bfb.focus(this._firstFocusItem);
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
_bf9.set(this.containerNode,"tabindex","0");
_bf9.set(this.learnMoreNode,"tabindex","0");
_bf9.set(this.closeButtonNode,"tabindex","0");
},hide:function(_c2e,_c2f){
if(this._keepShowing){
this._keepShowing=false;
if(!_c2f){
return;
}
}
if(this._onDeck&&this._onDeck[1]==_c2e){
this._onDeck=null;
}else{
if(this.aroundNode===_c2e){
return this._forceHide();
}
}
},hideOnClickClose:function(){
this._forceHide(true);
},_forceHide:function(_c30){
if(_c30&&this._lastFocusNode){
var _c31=_c00.getEnclosingWidget(this._lastFocusNode);
if(_c31&&lang.isFunction(_c31.refocus)){
_c31.refocus();
}else{
_bfb.focus(this._lastFocusNode);
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
var _c32=a11y._getTabNavigable(this.containerNode),_c33=_c02.get(this.learnMoreNode,"display")=="none"?this.closeButtonNode:this.learnMoreNode;
this._firstFocusItem=_c32.lowest||_c32.first||_c33;
this._lastFocusItem=_c32.last||_c32.highest||_c33;
},_onKey:function(evt){
var node=evt.target;
if(evt.charOrCode===keys.TAB){
this._getFocusItems(this.outerContainerNode);
}
var _c34=(this._firstFocusItem==this._lastFocusItem);
if(evt.charOrCode==keys.ESCAPE){
setTimeout(lang.hitch(this,"hideOnClickClose"),0);
_bfc.stop(evt);
}else{
if(node==this._firstFocusItem&&evt.shiftKey&&evt.charOrCode===keys.TAB){
if(!_c34){
_bfb.focus(this._lastFocusItem);
}
_bfc.stop(evt);
}else{
if(node==this._lastFocusItem&&evt.charOrCode===keys.TAB&&!evt.shiftKey){
if(!_c34){
_bfb.focus(this._firstFocusItem);
}
_bfc.stop(evt);
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
_bf9.remove(this.containerNode,"tabindex");
_bf9.remove(this.learnMoreNode,"tabindex");
_bf9.remove(this.closeButtonNode,"tabindex");
_bf9.remove(this.domNode,"aria-label");
if(this._onDeck){
var args=this._onDeck;
this._onDeck=null;
this.show.apply(this,args);
}
},onBlur:function(){
this._forceHide();
}});
_c0d._MasterHoverHelpTooltip=MasterHoverHelpTooltip=has("dojo-bidi")?_bf5("idx.widget._MasterHoverHelpTooltip",[_c18,_c07]):_c18;
_c0d.show=idx.widget.showHoverHelpTooltip=function(_c35,_c36,_c37,rtl,_c38,_c39,_c3a,_c3b,_c3c,_c3d){
if(!_c0d._masterTT){
idx.widget._masterTT=_c0d._masterTT=new MasterHoverHelpTooltip();
}
return _c0d._masterTT.show(_c35,_c36,_c37,rtl,_c38,_c39,_c3a,_c3b,_c3c,_c3d);
};
_c0d.hide=idx.widget.hideHoverHelpTooltip=function(_c3e){
return _c0d._masterTT&&_c0d._masterTT.hide(_c3e);
};
_c0d.defaultPosition=["after-centered","before-centered","below","above"];
_c0c.HoverHelpTooltip=_c0d;
return _c0d;
});
},"dijit/layout/ContentPane":function(){
define(["dojo/_base/kernel","dojo/_base/lang","../_Widget","../_Container","./_ContentPaneResizeMixin","dojo/string","dojo/html","dojo/i18n!../nls/loading","dojo/_base/array","dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/_base/xhr","dojo/i18n","dojo/when"],function(_c3f,lang,_c40,_c41,_c42,_c43,html,_c44,_c45,_c46,_c47,dom,_c48,_c49,xhr,i18n,when){
var _c4a=typeof (dojo.global.perf)!="undefined"&&dojo.global.perf!="undefined";
return _c46("dijit.layout.ContentPane",[_c40,_c41,_c42],{href:"",content:"",extractContent:false,parseOnLoad:true,parserScope:_c3f._scopeName,preventCache:false,preload:false,refreshOnShow:false,loadingMessage:"<span class='dijitContentPaneLoading'><span class='dijitInline dijitIconLoading'></span>${loadingState}</span>",errorMessage:"<span class='dijitContentPaneError'><span class='dijitInline dijitIconError'></span>${errorState}</span>",isLoaded:false,baseClass:"dijitContentPane",ioArgs:{},onLoadDeferred:null,_setTitleAttr:null,stopParser:true,template:false,markupFactory:function(_c4b,node,ctor){
var self=new ctor(_c4b,node);
return !self.href&&self._contentSetter&&self._contentSetter.parseDeferred&&!self._contentSetter.parseDeferred.isFulfilled()?self._contentSetter.parseDeferred.then(function(){
return self;
}):self;
},create:function(_c4c,_c4d){
if((!_c4c||!_c4c.template)&&_c4d&&!("href" in _c4c)&&!("content" in _c4c)){
_c4d=dom.byId(_c4d);
var df=_c4d.ownerDocument.createDocumentFragment();
while(_c4d.firstChild){
df.appendChild(_c4d.firstChild);
}
_c4c=lang.delegate(_c4c,{content:df});
}
this.inherited(arguments,[_c4c,_c4d]);
},postMixInProperties:function(){
this.inherited(arguments);
var _c4e=i18n.getLocalization("dijit","loading",this.lang);
this.loadingMessage=_c43.substitute(this.loadingMessage,_c4e);
this.errorMessage=_c43.substitute(this.errorMessage,_c4e);
},buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
this.domNode.removeAttribute("title");
},startup:function(){
this.inherited(arguments);
if(this._contentSetter){
_c45.forEach(this._contentSetter.parseResults,function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
},this);
}
},_startChildren:function(){
_c45.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
if(this._contentSetter){
_c45.forEach(this._contentSetter.parseResults,function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
},this);
}
},setHref:function(href){
_c3f.deprecated("dijit.layout.ContentPane.setHref() is deprecated. Use set('href', ...) instead.","","2.0");
return this.set("href",href);
},_setHrefAttr:function(href){
this.cancel();
this.onLoadDeferred=new _c47(lang.hitch(this,"cancel"));
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
this._set("href",href);
if(this.preload||(this._created&&this._isShown())){
this._load();
}else{
this._hrefChanged=true;
}
return this.onLoadDeferred;
},setContent:function(data){
_c3f.deprecated("dijit.layout.ContentPane.setContent() is deprecated.  Use set('content', ...) instead.","","2.0");
this.set("content",data);
},_setContentAttr:function(data){
this._set("href","");
this.cancel();
this.onLoadDeferred=new _c47(lang.hitch(this,"cancel"));
if(this._created){
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
}
this._setContent(data||"");
this._isDownloaded=false;
return this.onLoadDeferred;
},_getContentAttr:function(){
return this.containerNode.innerHTML;
},cancel:function(){
if(this._xhrDfd&&(this._xhrDfd.fired==-1)){
this._xhrDfd.cancel();
}
delete this._xhrDfd;
this.onLoadDeferred=null;
},destroy:function(){
this.cancel();
this.inherited(arguments);
},destroyRecursive:function(_c4f){
if(this._beingDestroyed){
return;
}
this.inherited(arguments);
},_onShow:function(){
this.inherited(arguments);
if(this.href){
if(!this._xhrDfd&&(!this.isLoaded||this._hrefChanged||this.refreshOnShow)){
return this.refresh();
}
}
},refresh:function(){
this.cancel();
this.onLoadDeferred=new _c47(lang.hitch(this,"cancel"));
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
this._load();
return this.onLoadDeferred;
},_load:function(){
if(_c4a){
perf.widgetStartedLoadingCallback();
}
this._setContent(this.onDownloadStart(),true);
var self=this;
var _c50={preventCache:(this.preventCache||this.refreshOnShow),url:this.href,handleAs:"text"};
if(lang.isObject(this.ioArgs)){
lang.mixin(_c50,this.ioArgs);
}
var hand=(this._xhrDfd=(this.ioMethod||xhr.get)(_c50)),_c51;
hand.then(function(html){
_c51=html;
try{
self._isDownloaded=true;
return self._setContent(html,false);
}
catch(err){
self._onError("Content",err);
}
},function(err){
if(!hand.canceled){
self._onError("Download",err);
}
delete self._xhrDfd;
return err;
}).then(function(){
self.onDownloadEnd();
if(_c4a){
perf.widgetLoadedCallback(self);
}
delete self._xhrDfd;
return _c51;
});
delete this._hrefChanged;
},_onLoadHandler:function(data){
this._set("isLoaded",true);
try{
this.onLoadDeferred.resolve(data);
}
catch(e){
console.error("Error "+this.widgetId+" running custom onLoad code: "+e.message);
}
},_onUnloadHandler:function(){
this._set("isLoaded",false);
try{
this.onUnload();
}
catch(e){
console.error("Error "+this.widgetId+" running custom onUnload code: "+e.message);
}
},destroyDescendants:function(_c52){
if(this.isLoaded){
this._onUnloadHandler();
}
var _c53=this._contentSetter;
_c45.forEach(this.getChildren(),function(_c54){
if(_c54.destroyRecursive){
_c54.destroyRecursive(_c52);
}else{
if(_c54.destroy){
_c54.destroy(_c52);
}
}
_c54._destroyed=true;
});
if(_c53){
_c45.forEach(_c53.parseResults,function(_c55){
if(!_c55._destroyed){
if(_c55.destroyRecursive){
_c55.destroyRecursive(_c52);
}else{
if(_c55.destroy){
if((_c55["class"]==="rotator"||_c55["class"]==="hcrRotatorNav")&&!_c55.wfe){
_c55.wfe={remove:function(){
}};
}
_c55.destroy(_c52);
}
}
_c55._destroyed=true;
}
});
delete _c53.parseResults;
}
if(!_c52){
_c49.empty(this.containerNode);
}
delete this._singleChild;
},_setContent:function(cont,_c56){
this.destroyDescendants();
var _c57=this._contentSetter;
if(!(_c57&&_c57 instanceof html._ContentSetter)){
_c57=this._contentSetter=new html._ContentSetter({node:this.containerNode,_onError:lang.hitch(this,this._onError),onContentError:lang.hitch(this,function(e){
var _c58=this.onContentError(e);
try{
this.containerNode.innerHTML=_c58;
}
catch(e){
console.error("Fatal "+this.id+" could not change content due to "+e.message,e);
}
})});
}
var _c59=lang.mixin({cleanContent:this.cleanContent,extractContent:this.extractContent,parseContent:!cont.domNode&&this.parseOnLoad,parserScope:this.parserScope,startup:false,dir:this.dir,lang:this.lang,textDir:this.textDir},this._contentSetterParams||{});
var p=_c57.set((lang.isObject(cont)&&cont.domNode)?cont.domNode:cont,_c59);
var self=this;
return when(p&&p.then?p:_c57.parseDeferred,function(){
delete self._contentSetterParams;
if(!_c56){
if(self._started){
self._startChildren();
self._scheduleLayout();
}
self._onLoadHandler(cont);
}
});
},_onError:function(type,err,_c5a){
this.onLoadDeferred.reject(err);
var _c5b=this["on"+type+"Error"].call(this,err);
if(_c5a){
console.error(_c5a,err);
}else{
if(_c5b){
this._setContent(_c5b,true);
}
}
},onLoad:function(){
},onUnload:function(){
},onDownloadStart:function(){
return this.loadingMessage;
},onContentError:function(){
},onDownloadError:function(){
return this.errorMessage;
},onDownloadEnd:function(){
}});
});
},"curam/util/RuntimeContext":function(){
define(["dojo/_base/declare"],function(_c5c){
var _c5d=_c5c("curam.util.RuntimeContext",null,{_window:null,constructor:function(_c5e){
this._window=_c5e;
},getHref:function(){
return this._window.location.href;
},getPathName:function(){
return this._window.location.pathName;
},contextObject:function(){
return this._window;
}});
return _c5d;
});
},"dijit/_KeyNavContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/_base/kernel","dojo/keys","dojo/_base/lang","./registry","./_Container","./_FocusMixin","./_KeyNavMixin"],function(_c5f,_c60,_c61,_c62,keys,lang,_c63,_c64,_c65,_c66){
return _c60("dijit._KeyNavContainer",[_c65,_c66,_c64],{connectKeyNavHandlers:function(_c67,_c68){
var _c69=(this._keyNavCodes={});
var prev=lang.hitch(this,"focusPrev");
var next=lang.hitch(this,"focusNext");
_c5f.forEach(_c67,function(code){
_c69[code]=prev;
});
_c5f.forEach(_c68,function(code){
_c69[code]=next;
});
_c69[keys.HOME]=lang.hitch(this,"focusFirstChild");
_c69[keys.END]=lang.hitch(this,"focusLastChild");
},startupKeyNavChildren:function(){
_c62.deprecated("startupKeyNavChildren() call no longer needed","","2.0");
},startup:function(){
this.inherited(arguments);
_c5f.forEach(this.getChildren(),lang.hitch(this,"_startupChild"));
},addChild:function(_c6a,_c6b){
this.inherited(arguments);
this._startupChild(_c6a);
},_startupChild:function(_c6c){
_c6c.set("tabIndex","-1");
},_getFirst:function(){
var _c6d=this.getChildren();
return _c6d.length?_c6d[0]:null;
},_getLast:function(){
var _c6e=this.getChildren();
return _c6e.length?_c6e[_c6e.length-1]:null;
},focusNext:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,1));
},focusPrev:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,-1),true);
},childSelector:function(node){
var node=_c63.byNode(node);
return node&&node.getParent()==this;
}});
});
},"dijit/layout/utils":function(){
define(["dojo/_base/array","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang"],function(_c6f,_c70,_c71,_c72,lang){
function _c73(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
};
function size(_c74,dim){
var _c75=_c74.resize?_c74.resize(dim):_c71.setMarginBox(_c74.domNode,dim);
if(_c74.fakeWidget){
return;
}
if(_c75){
lang.mixin(_c74,_c75);
}else{
lang.mixin(_c74,_c71.getMarginBoxSimple(_c74.domNode));
}
};
var _c76={marginBox2contentBox:function(node,mb){
var cs=_c72.getComputedStyle(node);
var me=_c71.getMarginExtents(node,cs);
var pb=_c71.getPadBorderExtents(node,cs);
return {l:_c72.toPixelValue(node,cs.paddingLeft),t:_c72.toPixelValue(node,cs.paddingTop),w:mb.w-(me.w+pb.w),h:mb.h-(me.h+pb.h)};
},layoutChildren:function(_c77,dim,_c78,_c79,_c7a){
dim=lang.mixin({},dim);
_c70.add(_c77,"dijitLayoutContainer");
_c78=_c6f.filter(_c78,function(item){
return item.region!="center"&&item.layoutAlign!="client";
}).concat(_c6f.filter(_c78,function(item){
return item.region=="center"||item.layoutAlign=="client";
}));
var _c7b={};
_c6f.forEach(_c78,function(_c7c){
var elm=_c7c.domNode,pos=(_c7c.region||_c7c.layoutAlign);
if(!pos){
throw new Error("No region setting for "+_c7c.id);
}
var _c7d=elm.style;
_c7d.left=dim.l+"px";
_c7d.top=dim.t+"px";
_c7d.position="absolute";
_c70.add(elm,"dijitAlign"+_c73(pos));
var _c7e={};
if(_c79&&_c79==_c7c.id){
_c7e[_c7c.region=="top"||_c7c.region=="bottom"?"h":"w"]=_c7a;
}
if(pos=="leading"){
pos=_c7c.isLeftToRight()?"left":"right";
}
if(pos=="trailing"){
pos=_c7c.isLeftToRight()?"right":"left";
}
if(pos=="top"||pos=="bottom"){
_c7e.w=dim.w;
size(_c7c,_c7e);
dim.h-=_c7c.h;
if(pos=="top"){
dim.t+=_c7c.h;
}else{
_c7d.top=dim.t+dim.h+"px";
}
}else{
if(pos=="left"||pos=="right"){
_c7e.h=dim.h;
size(_c7c,_c7e);
if(_c7c.isSplitter){
_c7d.left=dim.l-_c7c.w+"px";
dim.w+=_c7c.w;
}else{
dim.w-=_c7c.w;
}
if(pos=="left"){
if(!_c7c.isSplitter){
dim.l+=_c7c.w;
}
}else{
if(_c7c.isSplitter){
_c7d.left=dim.l+dim.w-_c7c.w+"px";
dim.l+=_c7c.w;
}else{
_c7d.left=dim.l+dim.w+"px";
}
}
}else{
if(pos=="client"||pos=="center"){
size(_c7c,dim);
}
}
}
_c7b[pos]={w:dim.w,h:dim.h};
});
return _c7b;
}};
lang.setObject("dijit.layout.utils",_c76);
return _c76;
});
},"dijit/_Contained":function(){
define(["dojo/_base/declare","./registry"],function(_c7f,_c80){
return _c7f("dijit._Contained",null,{_getSibling:function(_c81){
var p=this.getParent();
return (p&&p._getSiblingOfChild&&p._getSiblingOfChild(this,_c81=="previous"?-1:1))||null;
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
},"idx/widget/MenuDialog":function(){
define(["dojo/_base/declare","dojo/_base/array","dojo/_base/connect","dojo/_base/event","dojo/_base/lang","dojo/_base/sniff","dojo/_base/window","dojo/aspect","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/request/iframe","dojo/keys","dojo/window","dijit/popup","dijit/TooltipDialog","./_EventTriggerMixin","dojo/text!./templates/MenuDialog.html"],function(_c82,_c83,_c84,_c85,lang,has,win,_c86,dom,_c87,_c88,_c89,_c8a,keys,_c8b,_c8c,_c8d,_c8e,_c8f){
var _c90=lang.getObject("idx.oneui",true);
function _c91(node){
return (node.nodeName==="TEXTAREA")||((node.nodeName==="INPUT")&&(node.type==="text"));
};
return _c90.MenuDialog=_c82("idx.widget.MenuDialog",[_c8d,_c8e],{baseClass:"oneuiMenuDialog",_closeOnBlur:false,hoverToOpen:true,isShowingNow:false,leftClickToOpen:false,parentMenu:null,refocus:true,targetNodeIds:[],templateString:_c8f,useConnector:false,postMixInProperties:function(){
this.targetNodeIds=this.targetNodeIds||[];
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
var l=this.isLeftToRight();
this._nextMenuKey=l?keys.RIGHT_ARROW:keys.LEFT_ARROW;
this._prevMenuKey=l?keys.LEFT_ARROW:keys.RIGHT_ARROW;
this.connect(this.domNode,"onkeypress","_onDomNodeKeypress");
if(this.contextMenuForWindow){
this.bindDomNode(win.body());
}else{
_c83.forEach(this.targetNodeIds,function(_c92){
this.bindDomNode(_c92);
},this);
}
},_onDomNodeKeypress:function(evt){
var _c93=evt.target||evt.srcElement,_c94=false;
if(this.parentMenu&&!evt.ctrlKey&&!evt.altKey&&(!_c93||!_c91(_c93))){
switch(evt.charOrCode){
case this._nextMenuKey:
this.parentMenu._getTopMenu().focusNext();
_c94=true;
break;
case this._prevMenuKey:
if(this.parentMenu._isMenuBar){
this.parentMenu.focusPrev();
}else{
this.onCancel(false);
}
_c94=true;
break;
}
}
if(_c94){
_c85.stop(evt);
}else{
this.inherited(arguments);
}
},_getMenuForDialog:function(){
var _c95=this.getChildren(),_c96;
for(var i=0;!_c96&&(i<_c95.length);i++){
if(_c95[i]&&_c95[i].menuForDialog){
_c96=_c95[i];
}
}
return _c96;
},focus:function(){
if(!this.focused){
this.inherited(arguments);
}
},_getFocusItems:function(){
this.inherited(arguments);
if(this._firstFocusItem==this.domNode){
this._firstFocusItem=this.containerNode;
}
if(this._lastFocusItem==this.domNode){
this._lastFocusItem=this.containerNode;
}
},_onTrigger:function(_c97){
var _c98=null;
if(!_c97.additionalData.leftClickToOpen&&("pageX" in _c97.event)){
_c98={x:_c97.event.pageX,y:_c97.event.pageY};
if(_c97.triggerNode.tagName==="IFRAME"){
var ifc=_c88.position(_c97.triggerNode,true),_c99=win.withGlobal(_c8b.get(_c8a.doc(_c97.triggerNode)),"docScroll",_c88);
var cs=_c89.getComputedStyle(_c97.triggerNode),tp=_c89.toPixelValue,left=(has("ie")&&has("quirks")?0:tp(_c97.triggerNode,cs.paddingLeft))+(has("ie")&&has("quirks")?tp(_c97.triggerNode,cs.borderLeftWidth):0),top=(has("ie")&&has("quirks")?0:tp(_c97.triggerNode,cs.paddingTop))+(has("ie")&&has("quirks")?tp(_c97.triggerNode,cs.borderTopWidth):0);
_c98.x+=ifc.x+left-_c99.x;
_c98.y+=ifc.y+top-_c99.y;
}
}
if(!this._openTimer){
this._openTimer=setTimeout(lang.hitch(this,function(){
delete this._openTimer;
this.open({around:_c97.triggerNode,coords:_c98,position:_c97.additionalData.popupPosition,useConnector:_c97.additionalData.useConnector});
}),1);
}
if(_c97.event.type!="hover"){
_c85.stop(_c97.event);
}
},onBlur:function(){
this.inherited(arguments);
if(this._closeOnBlur){
this.close();
}
},open:function(args){
var _c9a=null;
if(this.refocus){
_c9a=this._focusManager.get("curNode");
if(!_c9a||dom.isDescendant(_c9a,this.domNode)){
_c9a=this._focusManager.get("prevNode");
}
if(dom.isDescendant(_c9a,this.domNode)){
_c9a=null;
}
}
var _c9b=(args&&(args.coords?{x:args.coords.x,y:args.coords.y,w:0,h:0}:args.around))||_c9a||this._focusManager.get("curNode")||{x:0,y:0,w:0,h:0};
if(!_c9b.getBoundingClientRect){
_c9b.getBoundingClientRect=function(){
return _c9b;
};
}
var _c9c=lang.hitch(this,function(){
if(_c9a){
_c9a.focus();
}
this.close();
});
this._useConnectorForPopup=(args&&("useConnector" in args))?args.useConnector:this.useConnector;
_c8c.open({popup:this,around:_c9b,onExecute:_c9c,onCancel:_c9c,orient:(args&&("position" in args))?args.position:this.popupPosition});
delete this._useConnectorForPopup;
this.focus();
this._closeOnBlur=true;
},close:function(){
_c8c.close(this);
},bindDomNode:function(node,_c9d){
var _c9e=lang.delegate(this);
for(var name in _c9d){
_c9e[name]=_c9d[name];
}
this._addEventTrigger(node,"click",function(_c9f){
return _c9e.leftClickToOpen;
},_c9e);
this._addEventTrigger(node,"contextmenu",function(_ca0){
return !_c9e.leftClickToOpen;
},_c9e);
this._addEventTrigger(node,"keydown",function(_ca1){
return !_c9e.leftClickToOpen&&_ca1.event.shiftKey&&(_ca1.event.keyCode==keys.F10);
},_c9e);
this._addEventTrigger(node,"hover",function(_ca2){
return _c9e.hoverToOpen;
},_c9e);
},unBindDomNode:function(_ca3){
this._removeEventTriggers(_ca3);
},_layoutNodes:function(_ca4,_ca5,_ca6,_ca7){
var _ca8=_ca7?"oneuiMenuDialogConnected":"",_ca9="",_caa=_ca6&&(_ca6.length>=1)&&_ca6.charAt(0),_cab=_ca6&&(_ca6.length>=2)&&_ca6.charAt(1),_cac=_c88.getContentBox(this.domNode),_cad,_cae,_caf=function(node){
var _cb0=node.style,_cb1=_cb0.display,_cb2=_cb0.visibility;
if(_cb0.display=="none"){
_cb0.visibility="hidden";
_cb0.display="";
}
var _cb3=_c88.getContentBox(node);
_cb0.display=_cb1;
_cb0.visibility=_cb2;
return _cb3;
};
if((_caa==="M")||((_cab!=="M")&&(_cab!==_ca5.charAt(1)))){
_ca8+=" dijitTooltip"+(_cab==="L"?"Right":"Left");
switch(_caa){
case "M":
_ca8+=" dijitTooltipLRMiddle";
break;
case "T":
_ca8+=" dijitTooltipLRTop";
_ca7&&(_ca9="connectorNearTopEdge");
if(_ca4.h>0){
_cad="top";
if(this.isLeftToRight()){
_cae=Math.max(4,4+Math.min(_caf(this.domNode.parentNode).h-24,_ca4.h/2))+"px";
}else{
_cae=(4+Math.min(_caf(this.domNode.parentNode).h-24,_ca4.h/2))+"px";
}
}
break;
case "B":
_ca8+=" dijitTooltipLRBottom";
_ca7&&(_ca9="connectorNearBottomEdge");
if(_ca4.h>0){
_cad="bottom";
if(this.isLeftToRight()){
_cae=(4+Math.min(_caf(this.domNode.parentNode).h-24,_ca4.h/2))+"px";
}else{
_cae=Math.max(4,4+Math.min(_caf(this.domNode.parentNode).h-24,_ca4.h/2))+"px";
}
}
break;
}
}else{
_ca8+=" dijitTooltip"+(_caa==="T"?"Below":"Above");
switch(_cab){
case "M":
_ca8+=" dijitTooltipABMiddle";
break;
case "L":
_ca8+=" dijitTooltipABLeft";
_ca7&&(_ca9="connectorNearLeftEdge");
if(_ca4.w>0){
_cad="left";
if(this.isLeftToRight()){
_cae=Math.max(4,4+Math.min(_caf(this.domNode.parentNode).w-16,_ca4.w/2))+"px";
}else{
_cae=(4+Math.min(_caf(this.domNode.parentNode).w-24,_ca4.w/2))+"px";
}
}
break;
case "R":
_ca8+=" dijitTooltipABRight";
_ca7&&(_ca9="connectorNearRightEdge");
if(_ca4.h>0){
_cad="right";
if(this.isLeftToRight()){
_cae=(4+Math.min(_caf(this.domNode.parentNode).w-24,_ca4.w/2))+"px";
}else{
_cae=Math.max(4,4+Math.min(_caf(this.domNode.parentNode).w-16,_ca4.w/2))+"px";
}
}
break;
}
}
_c87.replace(this.domNode,_ca8,this._currentOrientClass||"");
this._currentOrientClass=_ca8;
_c87.replace(this.domNode.parentNode,_ca9,this._currentConnectorClass||"");
this._currentConnectorClass=_ca9;
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
this.connectorNode.style.left="";
this.connectorNode.style.right="";
if(_cad){
this.connectorNode.style[_cad]=_cae;
}
},orient:function(node,_cb4,_cb5,_cb6,_cb7){
this._layoutNodes(_cb7,_cb4,_cb5,("_useConnectorForPopup" in this)?this._useConnectorForPopup:this.useConnector);
},onOpen:function(pos){
this.isShowingNow=true;
this._layoutNodes(pos.aroundNodePos,pos.aroundCorner,pos.corner,("_useConnectorForPopup" in this)?this._useConnectorForPopup:this.useConnector);
this.reset();
var menu=this._getMenuForDialog();
if(menu){
if(this._menuparented){
this._menuparented.parentMenu=null;
}
menu.parentMenu=this.parentMenu;
this._menuparented=menu;
if(this._handleexecute){
this._handleexecute.remove();
}
var _cb8=lang.hitch(this,this.onExecute);
this._handleexecute=menu.on("execute",_cb8);
if(this._handleopen){
this._handleopen.remove();
}
this._handleopen=_c86.after(menu,"_openPopup",function(){
var _cb9=_c8c._stack[_c8c._stack.length-1];
if(!_cb9._menuregistered){
_cb9._menuregistered=true;
_cb9.handlers.push(_c86.around(_cb9,"onExecute",function(_cba){
return function(){
_cba.apply(this,arguments);
_cb8();
};
}));
}
},true);
}
this._onShow();
},onClose:function(){
this.isShowingNow=false;
this._closeOnBlur=false;
if(this._handleexecute){
this._handleexecute.remove();
this._handleexecute=null;
}
if(this._handleopen){
this._handleopen.remove();
this._handleopen=null;
}
if(this._menuparented){
this._menuparented.parentMenu=null;
this._menuparented=null;
}
this.onHide();
},onExecute:function(){
}});
});
},"dijit/_Container":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","dojo/_base/kernel"],function(_cbb,_cbc,_cbd,_cbe){
return _cbc("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_cbf,_cc0){
var _cc1=this.containerNode;
if(_cc0>0){
_cc1=_cc1.firstChild;
while(_cc0>0){
if(_cc1.nodeType==1){
_cc0--;
}
_cc1=_cc1.nextSibling;
}
if(_cc1){
_cc0="before";
}else{
_cc1=this.containerNode;
_cc0="last";
}
}
_cbd.place(_cbf.domNode,_cc1,_cc0);
if(this._started&&!_cbf._started){
_cbf.startup();
}
},removeChild:function(_cc2){
if(typeof _cc2=="number"){
_cc2=this.getChildren()[_cc2];
}
if(_cc2){
var node=_cc2.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_cc3,dir){
var _cc4=this.getChildren(),idx=_cbb.indexOf(_cc4,_cc3);
return _cc4[idx+dir];
},getIndexOfChild:function(_cc5){
return _cbb.indexOf(this.getChildren(),_cc5);
}});
});
},"dojo/html":function(){
define(["./_base/kernel","./_base/lang","./_base/array","./_base/declare","./dom","./dom-construct","./parser"],function(_cc6,lang,_cc7,_cc8,dom,_cc9,_cca){
var _ccb=0;
var html={_secureForInnerHtml:function(cont){
return cont.replace(/(?:\s*<!DOCTYPE\s[^>]+>|<title[^>]*>[\s\S]*?<\/title>)/ig,"");
},_emptyNode:_cc9.empty,_setNodeContent:function(node,cont){
_cc9.empty(node);
if(cont){
if(typeof cont=="string"){
cont=_cc9.toDom(cont,node.ownerDocument);
}
if(!cont.nodeType&&lang.isArrayLike(cont)){
for(var _ccc=cont.length,i=0;i<cont.length;i=_ccc==cont.length?i+1:0){
_cc9.place(cont[i],node,"last");
}
}else{
_cc9.place(cont,node,"last");
}
}
return node;
},_ContentSetter:_cc8("dojo.html._ContentSetter",null,{node:"",content:"",id:"",cleanContent:false,extractContent:false,parseContent:false,parserScope:_cc6._scopeName,startup:true,constructor:function(_ccd,node){
lang.mixin(this,_ccd||{});
node=this.node=dom.byId(this.node||node);
if(!this.id){
this.id=["Setter",(node)?node.id||node.tagName:"",_ccb++].join("_");
}
},set:function(cont,_cce){
if(undefined!==cont){
this.content=cont;
}
if(_cce){
this._mixin(_cce);
}
this.onBegin();
this.setContent();
var ret=this.onEnd();
if(ret&&ret.then){
return ret;
}else{
return this.node;
}
},setContent:function(){
var node=this.node;
if(!node){
throw new Error(this.declaredClass+": setContent given no node");
}
try{
node=html._setNodeContent(node,this.content);
}
catch(e){
var _ccf=this.onContentError(e);
try{
node.innerHTML=_ccf;
}
catch(e){
console.error("Fatal "+this.declaredClass+".setContent could not change content due to "+e.message,e);
}
}
this.node=node;
},empty:function(){
if(this.parseDeferred){
if(!this.parseDeferred.isResolved()){
this.parseDeferred.cancel();
}
delete this.parseDeferred;
}
if(this.parseResults&&this.parseResults.length){
_cc7.forEach(this.parseResults,function(w){
if(w.destroy){
w.destroy();
}
});
delete this.parseResults;
}
_cc9.empty(this.node);
},onBegin:function(){
var cont=this.content;
if(lang.isString(cont)){
if(this.cleanContent){
cont=html._secureForInnerHtml(cont);
}
if(this.extractContent){
var _cd0=cont.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_cd0){
cont=_cd0[1];
}
}
}
this.empty();
this.content=cont;
return this.node;
},onEnd:function(){
if(this.parseContent){
this._parse();
}
return this.node;
},tearDown:function(){
delete this.parseResults;
delete this.parseDeferred;
delete this.node;
delete this.content;
},onContentError:function(err){
return "Error occurred setting content: "+err;
},onExecError:function(err){
return "Error occurred executing scripts: "+err;
},_mixin:function(_cd1){
var _cd2={},key;
for(key in _cd1){
if(key in _cd2){
continue;
}
this[key]=_cd1[key];
}
},_parse:function(){
var _cd3=this.node;
try{
var _cd4={};
_cc7.forEach(["dir","lang","textDir"],function(name){
if(this[name]){
_cd4[name]=this[name];
}
},this);
var self=this;
this.parseDeferred=_cca.parse({rootNode:_cd3,noStart:!this.startup,inherited:_cd4,scope:this.parserScope}).then(function(_cd5){
return self.parseResults=_cd5;
},function(e){
self._onError("Content",e,"Error parsing in _ContentSetter#"+this.id);
});
}
catch(e){
this._onError("Content",e,"Error parsing in _ContentSetter#"+this.id);
}
},_onError:function(type,err,_cd6){
var _cd7=this["on"+type+"Error"].call(this,err);
if(_cd6){
console.error(_cd6,err);
}else{
if(_cd7){
html._setNodeContent(this.node,_cd7,true);
}
}
}}),set:function(node,cont,_cd8){
if(undefined==cont){
console.warn("dojo.html.set: no cont argument provided, using empty string");
cont="";
}
if(!_cd8){
return html._setNodeContent(node,cont,true);
}else{
var op=new html._ContentSetter(lang.mixin(_cd8,{content:cont,node:node}));
return op.set();
}
}};
lang.setObject("dojo.html",html);
return html;
});
},"dijit/selection":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/sniff","dojo/_base/window","dijit/focus"],function(_cd9,dom,lang,has,_cda,_cdb){
var _cdc=function(win){
var doc=win.document;
this.getType=function(){
if(doc.getSelection){
var _cdd="text";
var oSel;
try{
oSel=win.getSelection();
}
catch(e){
}
if(oSel&&oSel.rangeCount==1){
var _cde=oSel.getRangeAt(0);
if((_cde.startContainer==_cde.endContainer)&&((_cde.endOffset-_cde.startOffset)==1)&&(_cde.startContainer.nodeType!=3)){
_cdd="control";
}
}
return _cdd;
}else{
return doc.selection.type.toLowerCase();
}
};
this.getSelectedText=function(){
if(doc.getSelection){
var _cdf=win.getSelection();
return _cdf?_cdf.toString():"";
}else{
if(this.getType()=="control"){
return null;
}
return doc.selection.createRange().text;
}
};
this.getSelectedHtml=function(){
if(doc.getSelection){
var _ce0=win.getSelection();
if(_ce0&&_ce0.rangeCount){
var i;
var html="";
for(i=0;i<_ce0.rangeCount;i++){
var frag=_ce0.getRangeAt(i).cloneContents();
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
var _ce1=win.getSelection();
return _ce1.anchorNode.childNodes[_ce1.anchorOffset];
}else{
var _ce2=doc.selection.createRange();
if(_ce2&&_ce2.item){
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
var _ce3=doc.getSelection();
if(_ce3){
var node=_ce3.anchorNode;
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
this.hasAncestorElement=function(_ce4){
return this.getAncestorElement.apply(this,arguments)!=null;
};
this.getAncestorElement=function(_ce5){
var node=this.getSelectedElement()||this.getParentElement();
return this.getParentOfType(node,arguments);
};
this.isTag=function(node,tags){
if(node&&node.tagName){
var _ce6=node.tagName.toLowerCase();
for(var i=0;i<tags.length;i++){
var _ce7=String(tags[i]).toLowerCase();
if(_ce6==_ce7){
return _ce7;
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
this.collapse=function(_ce8){
if(doc.getSelection){
var _ce9=win.getSelection();
if(_ce9.removeAllRanges){
if(_ce8){
_ce9.collapseToStart();
}else{
_ce9.collapseToEnd();
}
}else{
_ce9.collapse(_ce8);
}
}else{
var _cea=doc.selection.createRange();
_cea.collapse(_ce8);
_cea.select();
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
this.selectElementChildren=function(_ceb,_cec){
var _ced;
_ceb=dom.byId(_ceb);
if(doc.getSelection){
var _cee=win.getSelection();
if(has("opera")){
if(_cee.rangeCount){
_ced=_cee.getRangeAt(0);
}else{
_ced=doc.createRange();
}
_ced.setStart(_ceb,0);
_ced.setEnd(_ceb,(_ceb.nodeType==3)?_ceb.length:_ceb.childNodes.length);
_cee.addRange(_ced);
}else{
_cee.selectAllChildren(_ceb);
}
}else{
_ced=_ceb.ownerDocument.body.createTextRange();
_ced.moveToElementText(_ceb);
if(!_cec){
try{
_ced.select();
}
catch(e){
}
}
}
};
this.selectElement=function(_cef,_cf0){
var _cf1;
_cef=dom.byId(_cef);
if(doc.getSelection){
var _cf2=doc.getSelection();
_cf1=doc.createRange();
if(_cf2.removeAllRanges){
if(has("opera")){
if(_cf2.getRangeAt(0)){
_cf1=_cf2.getRangeAt(0);
}
}
_cf1.selectNode(_cef);
_cf2.removeAllRanges();
_cf2.addRange(_cf1);
}
}else{
try{
var tg=_cef.tagName?_cef.tagName.toLowerCase():"";
if(tg==="img"||tg==="table"){
_cf1=_cda.body(doc).createControlRange();
}else{
_cf1=_cda.body(doc).createRange();
}
_cf1.addElement(_cef);
if(!_cf0){
_cf1.select();
}
}
catch(e){
this.selectElementChildren(_cef,_cf0);
}
}
};
this.inSelection=function(node){
if(node){
var _cf3;
var _cf4;
if(doc.getSelection){
var sel=win.getSelection();
if(sel&&sel.rangeCount>0){
_cf4=sel.getRangeAt(0);
}
if(_cf4&&_cf4.compareBoundaryPoints&&doc.createRange){
try{
_cf3=doc.createRange();
_cf3.setStart(node,0);
if(_cf4.compareBoundaryPoints(_cf4.START_TO_END,_cf3)===1){
return true;
}
}
catch(e){
}
}
}else{
_cf4=doc.selection.createRange();
try{
_cf3=node.ownerDocument.body.createTextRange();
_cf3.moveToElementText(node);
}
catch(e2){
}
if(_cf4&&_cf3){
if(_cf4.compareEndPoints("EndToStart",_cf3)===1){
return true;
}
}
}
}
return false;
};
this.getBookmark=function(){
var bm,rg,tg,sel=doc.selection,cf=_cdb.curNode;
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
this.moveToBookmark=function(_cf5){
var mark=_cf5.mark;
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
_cd9.forEach(mark,function(n){
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
var _cf6=new _cdc(window);
_cf6.SelectionManager=_cdc;
return _cf6;
});
},"dijit/_base/typematic":function(){
define(["../typematic"],function(){
});
},"dijit/layout/BorderContainer":function(){
define(["dojo/_base/array","dojo/cookie","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","../_WidgetBase","../_Widget","../_TemplatedMixin","./LayoutContainer","./utils"],function(_cf7,_cf8,_cf9,_cfa,_cfb,_cfc,_cfd,keys,lang,on,_cfe,_cff,_d00,_d01,_d02,_d03){
var _d04=_cf9("dijit.layout._Splitter",[_d00,_d01],{live:true,templateString:"<div class=\"dijitSplitter\" data-dojo-attach-event=\"onkeydown:_onKeyDown,press:_startDrag,onmouseenter:_onMouse,onmouseleave:_onMouse\" tabIndex=\"0\"><div class=\"dijitSplitterThumb\"></div></div>",constructor:function(){
this._handlers=[];
},postMixInProperties:function(){
this.inherited(arguments);
this.horizontal=/top|bottom/.test(this.region);
this._factor=/top|left/.test(this.region)?1:-1;
this._cookieName=this.container.id+"_"+this.region;
},buildRendering:function(){
this.inherited(arguments);
_cfa.add(this.domNode,"dijitSplitter"+(this.horizontal?"H":"V"));
if(this.container.persist){
var _d05=_cf8(this._cookieName);
if(_d05){
this.child.domNode.style[this.horizontal?"height":"width"]=_d05;
}
}
},_computeMaxSize:function(){
var dim=this.horizontal?"h":"w",_d06=_cfc.getMarginBox(this.child.domNode)[dim],_d07=_cf7.filter(this.container.getChildren(),function(_d08){
return _d08.region=="center";
})[0];
var _d09=_cfc.getContentBox(_d07.domNode)[dim]-10;
return Math.min(this.child.maxSize,_d06+_d09);
},_startDrag:function(e){
if(!this.cover){
this.cover=_cfb.place("<div class=dijitSplitterCover></div>",this.child.domNode,"after");
}
_cfa.add(this.cover,"dijitSplitterCoverActive");
if(this.fake){
_cfb.destroy(this.fake);
}
if(!(this._resize=this.live)){
(this.fake=this.domNode.cloneNode(true)).removeAttribute("id");
_cfa.add(this.domNode,"dijitSplitterShadow");
_cfb.place(this.fake,this.domNode,"after");
}
_cfa.add(this.domNode,"dijitSplitterActive dijitSplitter"+(this.horizontal?"H":"V")+"Active");
if(this.fake){
_cfa.remove(this.fake,"dijitSplitterHover dijitSplitter"+(this.horizontal?"H":"V")+"Hover");
}
var _d0a=this._factor,_d0b=this.horizontal,axis=_d0b?"pageY":"pageX",_d0c=e[axis],_d0d=this.domNode.style,dim=_d0b?"h":"w",_d0e=_cfd.getComputedStyle(this.child.domNode),_d0f=_cfc.getMarginBox(this.child.domNode,_d0e)[dim],max=this._computeMaxSize(),min=Math.max(this.child.minSize,_cfc.getPadBorderExtents(this.child.domNode,_d0e)[dim]+10),_d10=this.region,_d11=_d10=="top"||_d10=="bottom"?"top":"left",_d12=parseInt(_d0d[_d11],10),_d13=this._resize,_d14=lang.hitch(this.container,"_layoutChildren",this.child.id),de=this.ownerDocument;
this._handlers=this._handlers.concat([on(de,_cfe.move,this._drag=function(e,_d15){
var _d16=e[axis]-_d0c,_d17=_d0a*_d16+_d0f,_d18=Math.max(Math.min(_d17,max),min);
if(_d13||_d15){
_d14(_d18);
}
_d0d[_d11]=_d16+_d12+_d0a*(_d18-_d17)+"px";
}),on(de,"dragstart",function(e){
e.stopPropagation();
e.preventDefault();
}),on(this.ownerDocumentBody,"selectstart",function(e){
e.stopPropagation();
e.preventDefault();
}),on(de,_cfe.release,lang.hitch(this,"_stopDrag"))]);
e.stopPropagation();
e.preventDefault();
},_onMouse:function(e){
var o=(e.type=="mouseover"||e.type=="mouseenter");
_cfa.toggle(this.domNode,"dijitSplitterHover",o);
_cfa.toggle(this.domNode,"dijitSplitter"+(this.horizontal?"H":"V")+"Hover",o);
},_stopDrag:function(e){
try{
if(this.cover){
_cfa.remove(this.cover,"dijitSplitterCoverActive");
}
if(this.fake){
_cfb.destroy(this.fake);
}
_cfa.remove(this.domNode,"dijitSplitterActive dijitSplitter"+(this.horizontal?"H":"V")+"Active dijitSplitterShadow");
this._drag(e);
this._drag(e,true);
}
finally{
this._cleanupHandlers();
delete this._drag;
}
if(this.container.persist){
_cf8(this._cookieName,this.child.domNode.style[this.horizontal?"height":"width"],{expires:365});
}
},_cleanupHandlers:function(){
var h;
while(h=this._handlers.pop()){
h.remove();
}
},_onKeyDown:function(e){
this._resize=true;
var _d19=this.horizontal;
var tick=1;
switch(e.keyCode){
case _d19?keys.UP_ARROW:keys.LEFT_ARROW:
tick*=-1;
case _d19?keys.DOWN_ARROW:keys.RIGHT_ARROW:
break;
default:
return;
}
var _d1a=_cfc.getMarginSize(this.child.domNode)[_d19?"h":"w"]+this._factor*tick;
this.container._layoutChildren(this.child.id,Math.max(Math.min(_d1a,this._computeMaxSize()),this.child.minSize));
e.stopPropagation();
e.preventDefault();
},destroy:function(){
this._cleanupHandlers();
delete this.child;
delete this.container;
delete this.cover;
delete this.fake;
this.inherited(arguments);
}});
var _d1b=_cf9("dijit.layout._Gutter",[_d00,_d01],{templateString:"<div class=\"dijitGutter\" role=\"presentation\"></div>",postMixInProperties:function(){
this.inherited(arguments);
this.horizontal=/top|bottom/.test(this.region);
},buildRendering:function(){
this.inherited(arguments);
_cfa.add(this.domNode,"dijitGutter"+(this.horizontal?"H":"V"));
}});
var _d1c=_cf9("dijit.layout.BorderContainer",_d02,{gutters:true,liveSplitters:true,persist:false,baseClass:"dijitBorderContainer",_splitterClass:_d04,postMixInProperties:function(){
if(!this.gutters){
this.baseClass+="NoGutter";
}
this.inherited(arguments);
},_setupChild:function(_d1d){
this.inherited(arguments);
var _d1e=_d1d.region,ltr=_d1d.isLeftToRight();
if(_d1e=="leading"){
_d1e=ltr?"left":"right";
}
if(_d1e=="trailing"){
_d1e=ltr?"right":"left";
}
if(_d1e){
if(_d1e!="center"&&(_d1d.splitter||this.gutters)&&!_d1d._splitterWidget){
var _d1f=_d1d.splitter?this._splitterClass:_d1b;
if(lang.isString(_d1f)){
_d1f=lang.getObject(_d1f);
}
var _d20=new _d1f({id:_d1d.id+"_splitter",container:this,child:_d1d,region:_d1e,live:this.liveSplitters});
_d20.isSplitter=true;
_d1d._splitterWidget=_d20;
var _d21=_d1e=="bottom"||_d1e==(this.isLeftToRight()?"right":"left");
_cfb.place(_d20.domNode,_d1d.domNode,_d21?"before":"after");
_d20.startup();
}
}
},layout:function(){
this._layoutChildren();
},removeChild:function(_d22){
var _d23=_d22._splitterWidget;
if(_d23){
_d23.destroy();
delete _d22._splitterWidget;
}
this.inherited(arguments);
},getChildren:function(){
return _cf7.filter(this.inherited(arguments),function(_d24){
return !_d24.isSplitter;
});
},getSplitter:function(_d25){
return _cf7.filter(this.getChildren(),function(_d26){
return _d26.region==_d25;
})[0]._splitterWidget;
},resize:function(_d27,_d28){
if(!this.cs||!this.pe){
var node=this.domNode;
this.cs=_cfd.getComputedStyle(node);
this.pe=_cfc.getPadExtents(node,this.cs);
this.pe.r=_cfd.toPixelValue(node,this.cs.paddingRight);
this.pe.b=_cfd.toPixelValue(node,this.cs.paddingBottom);
_cfd.set(node,"padding","0px");
}
this.inherited(arguments);
},_layoutChildren:function(_d29,_d2a){
if(!this._borderBox||!this._borderBox.h){
return;
}
var _d2b=[];
_cf7.forEach(this._getOrderedChildren(),function(pane){
_d2b.push(pane);
if(pane._splitterWidget){
_d2b.push(pane._splitterWidget);
}
});
var dim={l:this.pe.l,t:this.pe.t,w:this._borderBox.w-this.pe.w,h:this._borderBox.h-this.pe.h};
_d03.layoutChildren(this.domNode,dim,_d2b,_d29,_d2a);
},destroyRecursive:function(){
_cf7.forEach(this.getChildren(),function(_d2c){
var _d2d=_d2c._splitterWidget;
if(_d2d){
_d2d.destroy();
}
delete _d2c._splitterWidget;
});
this.inherited(arguments);
}});
_d1c.ChildWidgetProperties={splitter:false,minSize:0,maxSize:Infinity};
lang.mixin(_d1c.ChildWidgetProperties,_d02.ChildWidgetProperties);
lang.extend(_cff,_d1c.ChildWidgetProperties);
_d1c._Splitter=_d04;
_d1c._Gutter=_d1b;
return _d1c;
});
},"dijit/_base":function(){
define(["./main","./a11y","./WidgetSet","./_base/focus","./_base/manager","./_base/place","./_base/popup","./_base/scroll","./_base/sniff","./_base/typematic","./_base/wai","./_base/window"],function(_d2e){
return _d2e._base;
});
},"dojo/window":function(){
define(["./_base/lang","./sniff","./_base/window","./dom","./dom-geometry","./dom-style","./dom-construct"],function(lang,has,_d2f,dom,geom,_d30,_d31){
has.add("rtl-adjust-position-for-verticalScrollBar",function(win,doc){
var body=_d2f.body(doc),_d32=_d31.create("div",{style:{overflow:"scroll",overflowX:"visible",direction:"rtl",visibility:"hidden",position:"absolute",left:"0",top:"0",width:"64px",height:"64px"}},body,"last"),div=_d31.create("div",{style:{overflow:"hidden",direction:"ltr"}},_d32,"last"),ret=geom.position(div).x!=0;
_d32.removeChild(div);
body.removeChild(_d32);
return ret;
});
has.add("position-fixed-support",function(win,doc){
var body=_d2f.body(doc),_d33=_d31.create("span",{style:{visibility:"hidden",position:"fixed",left:"1px",top:"1px"}},body,"last"),_d34=_d31.create("span",{style:{position:"fixed",left:"0",top:"0"}},_d33,"last"),ret=geom.position(_d34).x!=geom.position(_d33).x;
_d33.removeChild(_d34);
body.removeChild(_d33);
return ret;
});
var _d35={getBox:function(doc){
doc=doc||_d2f.doc;
var _d36=(doc.compatMode=="BackCompat")?_d2f.body(doc):doc.documentElement,_d37=geom.docScroll(doc),w,h;
if(has("touch")){
var _d38=_d35.get(doc);
w=_d38.innerWidth||_d36.clientWidth;
h=_d38.innerHeight||_d36.clientHeight;
}else{
w=_d36.clientWidth;
h=_d36.clientHeight;
}
return {l:_d37.x,t:_d37.y,w:w,h:h};
},get:function(doc){
if(has("ie")&&_d35!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
},scrollIntoView:function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_d2f.doc,body=_d2f.body(doc),html=doc.documentElement||body.parentNode,isIE=has("ie")||has("trident"),isWK=has("webkit");
if(node==body||node==html){
return;
}
if(!(has("mozilla")||isIE||isWK||has("opera")||has("trident")||has("edge"))&&("scrollIntoView" in node)){
node.scrollIntoView(false);
return;
}
var _d39=doc.compatMode=="BackCompat",_d3a=Math.min(body.clientWidth||html.clientWidth,html.clientWidth||body.clientWidth),_d3b=Math.min(body.clientHeight||html.clientHeight,html.clientHeight||body.clientHeight),_d3c=(isWK||_d39)?body:html,_d3d=pos||geom.position(node),el=node.parentNode,_d3e=function(el){
return (isIE<=6||(isIE==7&&_d39))?false:(has("position-fixed-support")&&(_d30.get(el,"position").toLowerCase()=="fixed"));
},self=this,_d3f=function(el,x,y){
if(el.tagName=="BODY"||el.tagName=="HTML"){
self.get(el.ownerDocument).scrollBy(x,y);
}else{
x&&(el.scrollLeft+=x);
y&&(el.scrollTop+=y);
}
};
if(_d3e(node)){
return;
}
while(el){
if(el==body){
el=_d3c;
}
var _d40=geom.position(el),_d41=_d3e(el),rtl=_d30.getComputedStyle(el).direction.toLowerCase()=="rtl";
if(el==_d3c){
_d40.w=_d3a;
_d40.h=_d3b;
if(_d3c==html&&(isIE||has("trident"))&&rtl){
_d40.x+=_d3c.offsetWidth-_d40.w;
}
_d40.x=0;
_d40.y=0;
}else{
var pb=geom.getPadBorderExtents(el);
_d40.w-=pb.w;
_d40.h-=pb.h;
_d40.x+=pb.l;
_d40.y+=pb.t;
var _d42=el.clientWidth,_d43=_d40.w-_d42;
if(_d42>0&&_d43>0){
if(rtl&&has("rtl-adjust-position-for-verticalScrollBar")){
_d40.x+=_d43;
}
_d40.w=_d42;
}
_d42=el.clientHeight;
_d43=_d40.h-_d42;
if(_d42>0&&_d43>0){
_d40.h=_d42;
}
}
if(_d41){
if(_d40.y<0){
_d40.h+=_d40.y;
_d40.y=0;
}
if(_d40.x<0){
_d40.w+=_d40.x;
_d40.x=0;
}
if(_d40.y+_d40.h>_d3b){
_d40.h=_d3b-_d40.y;
}
if(_d40.x+_d40.w>_d3a){
_d40.w=_d3a-_d40.x;
}
}
var l=_d3d.x-_d40.x,t=_d3d.y-_d40.y,r=l+_d3d.w-_d40.w,bot=t+_d3d.h-_d40.h;
var s,old;
if(r*l>0&&(!!el.scrollLeft||el==_d3c||el.scrollWidth>el.offsetHeight)){
s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_d39)||has("trident")>=5)){
s=-s;
}
old=el.scrollLeft;
_d3f(el,s,0);
s=el.scrollLeft-old;
_d3d.x-=s;
}
if(bot*t>0&&(!!el.scrollTop||el==_d3c||el.scrollHeight>el.offsetHeight)){
s=Math.ceil(Math[t<0?"max":"min"](t,bot));
old=el.scrollTop;
_d3f(el,0,s);
s=el.scrollTop-old;
_d3d.y-=s;
}
el=(el!=_d3c)&&!_d41&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
}};
1&&lang.setObject("dojo.window",_d35);
return _d35;
});
},"dijit/_FocusMixin":function(){
define(["./focus","./_WidgetBase","dojo/_base/declare","dojo/_base/lang"],function(_d44,_d45,_d46,lang){
lang.extend(_d45,{focused:false,onFocus:function(){
},onBlur:function(){
},_onFocus:function(){
this.onFocus();
},_onBlur:function(){
this.onBlur();
}});
return _d46("dijit._FocusMixin",null,{_focusManager:_d44});
});
},"dijit/_WidgetsInTemplateMixin":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/_base/lang","dojo/parser"],function(_d47,_d48,_d49,lang,_d4a){
return _d49("dijit._WidgetsInTemplateMixin",null,{_earlyTemplatedStartup:false,widgetsInTemplate:true,contextRequire:null,_beforeFillContent:function(){
if(this.widgetsInTemplate){
var node=this.domNode;
if(this.containerNode&&!this.searchContainerNode){
this.containerNode.stopParser=true;
}
_d4a.parse(node,{noStart:!this._earlyTemplatedStartup,template:true,inherited:{dir:this.dir,lang:this.lang,textDir:this.textDir},propsThis:this,contextRequire:this.contextRequire,scope:"dojo"}).then(lang.hitch(this,function(_d4b){
this._startupWidgets=_d4b;
for(var i=0;i<_d4b.length;i++){
this._processTemplateNode(_d4b[i],function(n,p){
return n[p];
},function(_d4c,type,_d4d){
if(type in _d4c){
return _d4c.connect(_d4c,type,_d4d);
}else{
return _d4c.on(type,_d4d,true);
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
},_processTemplateNode:function(_d4e,_d4f,_d50){
if(_d4f(_d4e,"dojoType")||_d4f(_d4e,"data-dojo-type")){
return true;
}
return this.inherited(arguments);
},startup:function(){
_d47.forEach(this._startupWidgets,function(w){
if(w&&!w._started&&w.startup){
w.startup();
}
});
this._startupWidgets=null;
this.inherited(arguments);
}});
});
},"idx/widget/HoverCard":function(){
define(["dojo/_base/declare","dojo/_base/array","dojo/keys","dijit/focus","dijit/a11y","dojo/_base/event","dojo/_base/fx","dojo/_base/lang","dojo/_base/html","dojo/dom-geometry","dijit/place","dojo/dom","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","dojo/dnd/Moveable","dojo/dnd/TimedMoveable","dojo/_base/window","dojo/_base/connect","dojo/_base/sniff","dojo/topic","dijit/_base/wai","dijit/_base/manager","dijit/popup","dijit/focus","dijit/BackgroundIframe","dijit/TooltipDialog","idx/widget/Menu","dijit/MenuItem","dijit/form/DropDownButton","dijit/form/Button","dojo/text!./templates/HoverCard.html","dijit/registry","idx/form/_FocusManager","idx/util","dojo/i18n!./nls/HoverCard","dojo/i18n!./nls/Dialog"],function(_d51,_d52,keys,_d53,a11y,_d54,fx,lang,html,_d55,_d56,dom,_d57,_d58,_d59,_d5a,_d5b,_d5c,win,_d5d,has,_d5e,wai,_d5f,_d60,_d61,_d62,_d63,Menu,_d64,_d65,_d66,_d67,_d68,_d69,_d6a,_d6b,_d6c){
var _d6d=lang.getObject("idx.oneui",true);
var _d6e=_d51("idx.widget.HoverCard",[_d63],{templateString:_d67,target:"",draggable:true,showDelay:500,hideDelay:800,moreActions:null,actions:null,content:null,forceFocus:true,position:null,duration:_d5f.defaultDuration,postMixInProperties:function(){
this.moreActionsLabel=_d6b.moreActionsLabel;
this.buttonClose=_d6c.closeButtonLabel;
},destroy:function(){
if(this._targetConnections){
for(var _d6f=0;_d6f<this._targetConnections.length;_d6f++){
_d5d.disconnect(this._targetConnections[_d6f]);
}
delete this._targetConnections;
}
this.inherited(arguments);
},_setTargetAttr:function(_d70){
var _d70=dom.byId(_d70);
if(_d70==this.target){
return;
}
if(this._targetConnections){
for(var _d71=0;_d71<this._targetConnections.length;_d71++){
_d5d.disconnect(this._targetConnections[_d71]);
}
delete this._targetConnections;
}
this._targetConnections=[_d5d.connect(_d70,"onmouseenter",this,"_onHover"),_d5d.connect(_d70,"onmouseleave",this,"_onUnHover"),_d5d.connect(_d70,"onkeypress",this,"_onTargetNodeKey")];
this._set("target",_d70);
this._createWrapper();
},_onTargetNodeKey:function(evt){
var node=evt.target;
if(evt.charOrCode==keys.ENTER||evt.charOrCode==keys.SPACE||evt.charOrCode==" "){
this._showTimer=setTimeout(lang.hitch(this,function(){
this.open(node);
}),this.showDelay);
_d54.stop(evt);
}
},_setActionsAttr:function(_d72){
_d52.forEach(_d72,function(_d73){
var _d74=new _d66({iconClass:_d73.iconClass,onClick:_d73.content?lang.hitch(this,function(){
}):_d73.onClick,baseClass:"idxOneuiHoverCardFooterButton"});
html.place(_d74.domNode,this.actionIcons);
},this);
},_setMoreActionsAttr:function(_d75){
var menu=new Menu({});
_d52.forEach(_d75,function(_d76){
menu.addChild(new _d64({label:_d76.label,onClick:_d76.onClick}));
});
menu.startup();
var _d77=new _d65({label:this.moreActionsLabel,dropDown:menu,baseClass:"idxOneuiHoverCardMenu"},this.moreActionsNode);
this.moreActionsMenu=menu;
},_setContentAttr:function(_d78){
var _d79=_d68.byId(_d78);
if(!_d79.declaredClass){
this.inherited(arguments);
}else{
html.place(_d79.domNode,this.containerNode);
_d58.toggle(this.containerNode,"idxOneuiHoverCardWithoutPreviewImg",!_d79.image);
}
},_onHover:function(e){
if(this.isShowingNow){
return;
}
if(!this._showTimer){
var _d7a=e.target;
this._showTimer=setTimeout(lang.hitch(this,function(){
this.open(_d7a);
}),this.showDelay);
}
if(this._hideTimer){
clearTimeout(this._hideTimer);
delete this._hideTimer;
}
},_onUnHover:function(){
if(this._focus){
return;
}
if(this._showTimer){
clearTimeout(this._showTimer);
delete this._showTimer;
}
if(!this._hideTimer){
this._hideTimer=setTimeout(lang.hitch(this,function(){
this.close();
}),this.hideDelay);
}
},onClose:function(){
this.close();
},onBlur:function(){
this.close();
},postCreate:function(){
this.bgIframe=new _d62(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
this.subscribe("new-hoverCard-open","onOtherCardOpen");
this.connect(this.gripNode,"onmouseenter",function(){
_d58.add(this.gripNode,"idxOneuiHoverCardGripHover");
});
this.connect(this.gripNode,"onmouseleave",function(){
_d58.remove(this.gripNode,"idxOneuiHoverCardGripHover");
});
this.connect(this.domNode,"onkeypress","_onKey");
this.connect(this.domNode,"onmouseenter",function(){
this._hovered=true;
if(this._hideTimer){
clearTimeout(this._hideTimer);
delete this._hideTimer;
}
});
this.connect(this.domNode,"onmouseleave",function(){
this._hovered=false;
var _d7b=_d60._stack;
if(_d7b.length&&_d7b[_d7b.length-1].widget!=this){
return;
}
this._onUnHover();
});
},open:function(_d7c){
_d61._onTouchNode(this.target,"mouse");
wai.setWaiRole(this.domNode,"alertdialog");
if(this._showTimer){
clearTimeout(this._showTimer);
delete this._showTimer;
}
if(this.isShowingNow){
if(this.forceFocus){
this.focus();
}
return;
}
_d5e.publish("new-hoverCard-open",this.target);
this.show(this.domNode.innerHTML,_d7c,this.position,!this.isLeftToRight(),this.textDir);
this._connectNode=_d7c;
this.onShow(_d7c,this.position);
},close:function(){
if(this._connectNode&&!this._hovered&&!this._moved&&(!this.moreActionsMenu||!this.moreActionsMenu._hoveredChild)){
this.hide(this._connectNode);
delete this._connectNode;
this.onHide();
this._removeFromPopupStack();
}
if(this._showTimer){
clearTimeout(this._showTimer);
delete this._showTimer;
}
},focus:function(){
this.inherited(arguments);
this._focus=true;
},show:function(_d7d,_d7e,_d7f,rtl,_d80){
_d57.set(this._popupWrapper,"display","");
_d58.remove(this.domNode,"dijitHidden");
if(dojo.isIE<=7){
_d57.set(this.bodyNode,"width",_d57.get(this.containerNode,"width")+5+"px");
}
_d57.set(this.connectorNode,"visibility","visible");
this.domNode.width="auto";
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.set("textDir",_d80);
this.containerNode.align=rtl?"right":"left";
var pos=_d56.around(this.domNode,_d7e,_d7f&&_d7f.length?_d7f:_d6e.defaultPosition,!rtl,lang.hitch(this,"orient"));
this._connectorReposition(pos);
if(this.gripNode&&this.draggable){
this.enableDnd();
}
_d57.set(this.domNode,{"opacity":0,"position":"absolute"});
this._popupWrapper.appendChild(this.domNode);
if(rtl){
_d57.set(this.domNode,{"left":_d55.position(this.domNode).x+"px","right":"auto"});
}
this._addToPopupStack();
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_d7e;
},_addToPopupStack:function(){
var _d81=_d60._stack;
_d57.set(this.domNode,"zIndex",_d60._beginZIndex+_d81.length);
_d60._stack.push({widget:this,handlers:[]});
},_createWrapper:function(){
if(!this._popupWrapper){
this._popupWrapper=_d5a.create("div",{style:{"display":"none"}},this.ownerDocumentBody);
if(this.target&&this.target.id){
_d59.set(this._popupWrapper,"dijitPopupParent",this.target.id);
}else{
this._popupWrapper.dijitPopupParent=this.target;
}
}
},_removeFromPopupStack:function(){
var _d82=_d60._stack,top;
while((top=_d82.pop())){
var _d83=top.onClose,_d84=top.widget;
_d84.onClose&&_d84.onClose();
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_d84&&_d84.domNode){
_d60.hide(_d84);
}
_d83&&_d83();
if(top.widget==this){
break;
}
}
},_connectorReposition:function(pos){
if(pos.corner.charAt(0)=="B"&&pos.aroundCorner.charAt(0)=="B"){
var mb=_d55.getMarginBox(this.domNode);
var _d85=this.connectorNode.offsetHeight;
if(mb.h>pos.spaceAvailable.h){
var _d86=pos.spaceAvailable.h-((pos.aroundNodePos.h+_d85)>>1);
this.connectorNode.style.top=_d86+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(pos.aroundNodePos.h/2-_d85/2,0),mb.h-_d85)+"px";
this.connectorNode.style.top="";
}
}else{
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=pos.aroundNodePos.y+((pos.aroundNodePos.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=pos.aroundNodePos.x+((pos.aroundNodePos.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
}
}
},orient:function(node,_d87,_d88,_d89,_d8a){
this.connectorNode.style.top="";
var _d8b=_d89.w-this.connectorNode.offsetWidth;
node.className="idxOneuiHoverCard "+{"MR-ML":"idxOneuiHoverCardRight","ML-MR":"idxOneuiHoverCardLeft","TM-BM":"idxOneuiHoverCardAbove","BM-TM":"idxOneuiHoverCardBelow","BL-TL":"idxOneuiHoverCardBelow idxOneuiHoverCardABLeft","TL-BL":"idxOneuiHoverCardAbove idxOneuiHoverCardABLeft","BR-TR":"idxOneuiHoverCardBelow idxOneuiHoverCardABRight","TR-BR":"idxOneuiHoverCardAbove idxOneuiHoverCardABRight","BR-BL":"idxOneuiHoverCardRight","BL-BR":"idxOneuiHoverCardLeft","TR-TL":"idxOneuiHoverCardRight"}[_d87+"-"+_d88];
this.domNode.style.width="auto";
var size=_d55.getContentBox(this.domNode);
var _d8c=Math.min((Math.max(_d8b,1)),size.w);
var _d8d=_d8c<size.w;
this.domNode.style.width=_d8c+"px";
if(_d8d){
this.containerNode.style.overflow="auto";
var _d8e=this.containerNode.scrollWidth;
this.containerNode.style.overflow="visible";
if(_d8e>_d8c){
_d8e=_d8e+_d57.get(this.domNode,"paddingLeft")+_d57.get(this.domNode,"paddingRight");
this.domNode.style.width=_d8e+"px";
}
}
return Math.max(0,size.w-_d8b);
},hide:function(_d8f){
if(this._onDeck&&this._onDeck[1]==_d8f){
this._onDeck=null;
}else{
if(this.aroundNode===_d8f||this.isShowingNow){
this.fadeIn.stop();
this.isShowingNow=false;
this.aroundNode=null;
this.fadeOut.play();
this._focus=false;
this._hovered=false;
this._moved=false;
}else{
}
}
},enableDnd:function(){
var node=this.domNode,_d90=this.connectorNode;
this._moveable=new ((has("ie")==6)?_d5c:_d5b)(node,{handle:this.gripNode});
this.connect(this._moveable,"onMoveStart",function(){
_d57.set(_d90,"visibility","hidden");
_d58.add(this.gripNode,"idxOneuiHoverCardGripActive");
this._moved=true;
});
this.connect(this._moveable,"onMoveStop",function(){
_d58.remove(this.gripNode,"idxOneuiHoverCardGripActive");
_d58.add(this.gripNode,"idxOneuiHoverCardGrip");
});
},onOtherCardOpen:function(_d91){
if(!this.isShowingNow){
return;
}
var _d92=_d60._stack;
if(_d92.length&&_d92[_d92.length-1].widget==this){
if(!dom.isDescendant(_d91,this.bodyNode)){
this.close();
}
}
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
if(this.forceFocus){
this.focus();
}
},_onHide:function(){
if(this.domNode){
_d58.add(this.domNode,"dijitHidden");
}
_d57.set(this._popupWrapper,"display","none");
this.target.focus&&this.target.focus();
if(this._onDeck){
this.show.apply(this,this._onDeck);
this._onDeck=null;
}
},_getFocusItems:function(){
var _d93=a11y._getTabNavigable(this.domNode);
this._firstFocusItem=_d93.lowest||_d93.first||this.closeButtonNode||this.domNode;
this._lastFocusItem=_d93.last||_d93.highest||this._firstFocusItem;
},_onKey:function(evt){
var node=evt.target;
if(evt.charOrCode===keys.TAB){
this._getFocusItems(this.domNode);
}
var _d94=(this._firstFocusItem==this._lastFocusItem);
if(evt.charOrCode==keys.ESCAPE){
setTimeout(lang.hitch(this,"hide"),0);
_d54.stop(evt);
}else{
if(node==this._firstFocusItem&&evt.shiftKey&&evt.charOrCode===keys.TAB){
if(!_d94){
_d53.focus(this._lastFocusItem);
}
_d54.stop(evt);
}else{
if(node==this._lastFocusItem&&evt.charOrCode===keys.TAB&&!evt.shiftKey){
if(!_d94){
_d53.focus(this._firstFocusItem);
}
_d54.stop(evt);
}else{
if(evt.charOrCode===keys.TAB){
evt.stopPropagation();
}
}
}
}
}});
_d6e.defaultPosition=["after-centered","before-centered","below","above"];
_d6d.HoverCard=_d6e;
return _d6e;
});
},"dijit/form/_ButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom","dojo/has","../registry"],function(_d95,dom,has,_d96){
var _d97=_d95("dijit.form._ButtonMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{label:"",type:"button",__onClick:function(e){
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
var _d98=e.defaultPrevented;
if(!_d98&&this.type=="submit"&&!(this.valueNode||this.focusNode).form){
for(var node=this.domNode;node.parentNode;node=node.parentNode){
var _d99=_d96.byNode(node);
if(_d99&&typeof _d99._onSubmit=="function"){
_d99._onSubmit(e);
e.preventDefault();
_d98=true;
break;
}
}
}
return !_d98;
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.focusNode,false);
},onClick:function(){
return true;
},_setLabelAttr:function(_d9a){
this._set("label",_d9a);
var _d9b=this.containerNode||this.focusNode;
_d9b.innerHTML=_d9a;
}});
if(has("dojo-bidi")){
_d97=_d95("dijit.form._ButtonMixin",_d97,{_setLabelAttr:function(){
this.inherited(arguments);
var _d9c=this.containerNode||this.focusNode;
this.applyTextDir(_d9c);
}});
}
return _d97;
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_d9d,win,_d9e){
var _d9f={},hash={};
var _da0={length:0,add:function(_da1){
if(hash[_da1.id]){
throw new Error("Tried to register widget with id=="+_da1.id+" but that id is already registered");
}
hash[_da1.id]=_da1;
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
},getUniqueId:function(_da2){
var id;
do{
id=_da2+"_"+(_da2 in _d9f?++_d9f[_da2]:_d9f[_da2]=0);
}while(hash[id]);
return _d9e._scopeName=="dijit"?id:_d9e._scopeName+"_"+id;
},findWidgets:function(root,_da3){
var _da4=[];
function _da5(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _da6=node.getAttribute("widgetId");
if(_da6){
var _da7=hash[_da6];
if(_da7){
_da4.push(_da7);
}
}else{
if(node!==_da3){
_da5(node);
}
}
}
}
};
_da5(root);
return _da4;
},_destroyAll:function(){
_d9e._curFocus=null;
_d9e._prevFocus=null;
_d9e._activeStack=[];
_d9d.forEach(_da0.findWidgets(win.body()),function(_da8){
if(!_da8._destroyed){
if(_da8.destroyRecursive){
_da8.destroyRecursive();
}else{
if(_da8.destroy){
_da8.destroy();
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
_d9e.registry=_da0;
return _da0;
});
},"dijit/_base/wai":function(){
define(["dojo/dom-attr","dojo/_base/lang","../main","../hccss"],function(_da9,lang,_daa){
var _dab={hasWaiRole:function(elem,role){
var _dac=this.getWaiRole(elem);
return role?(_dac.indexOf(role)>-1):(_dac.length>0);
},getWaiRole:function(elem){
return lang.trim((_da9.get(elem,"role")||"").replace("wairole:",""));
},setWaiRole:function(elem,role){
_da9.set(elem,"role",role);
},removeWaiRole:function(elem,role){
var _dad=_da9.get(elem,"role");
if(!_dad){
return;
}
if(role){
var t=lang.trim((" "+_dad+" ").replace(" "+role+" "," "));
_da9.set(elem,"role",t);
}else{
elem.removeAttribute("role");
}
},hasWaiState:function(elem,_dae){
return elem.hasAttribute?elem.hasAttribute("aria-"+_dae):!!elem.getAttribute("aria-"+_dae);
},getWaiState:function(elem,_daf){
return elem.getAttribute("aria-"+_daf)||"";
},setWaiState:function(elem,_db0,_db1){
elem.setAttribute("aria-"+_db0,_db1);
},removeWaiState:function(elem,_db2){
elem.removeAttribute("aria-"+_db2);
}};
lang.mixin(_daa,_dab);
return _daa;
});
},"curam/widget/componentWrappers/ListWraper":function(){
define(["dojo/_base/declare","dojo/on","dijit/_Widget","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/dom-class","dojo/dom-attr"],function(_db3,on,_db4,_db5,_db6,_db7,_db8,_db9){
return _db3("curam.widget.componentWrappers.ListWraper",[_db4],{baseClass:"navMenu",_listTypeUnordered:"ul",_listTypeOrdered:"ol",listType:this._listTypeOrdered,baseClass:"listWrapper",itemClass:null,itemStyle:null,role:null,buildRendering:function(){
if(this.listType==this._listTypeUnordered){
this.domNode=_db5.create("ul");
}else{
this.domNode=_db5.create("ol");
_db9.set(this.domNode,"tabindex","0");
}
if(this.role!=null){
_db9.set(this.domNode,"role",this.role);
}
this.inherited(arguments);
},_setItemAttr:function(item,_dba){
if(_dba==null){
_dba="last";
}
var _dbb=_db5.create("li",null,this.domNode,_dba);
_db9.set(_dbb,"role","menuitem");
_db9.set(_dbb,"tabindex","-1");
this._doBeforeItemSet(item,_dbb);
_db5.place(item.domNode?item.domNode:item,_dbb);
this._doAfterItemSet(item,_dbb);
if(this.itemStyle){
_db7.set(_dbb,this.itemStyle);
}
if(this.itemClass){
_db8.add(_dbb,this.itemClass);
}
},_doBeforeItemSet:function(item,_dbc){
},_doAfterItemSet:function(item,_dbd){
},_getItemCountAttr:function(){
return this.domNode.children.length;
},_getContainerHeightAttr:function(){
var _dbe=_db6.getContentBox(this.domNode);
return _dbe.h;
},getChildElament:function(_dbf){
var _dc0=this.domNode.childNodes[_dbf];
return _dc0;
},placeItemToPostion:function(item,_dc1){
var _dc2=this.domNode.childNodes[_dc1];
_db5.place(_dc2,item);
},deleteChild:function(_dc3){
var _dc4=this.getChildElament(_dc3);
_db5.destroy(_dc4);
},deleteAllChildern:function(){
while(this.domNode.children.length>0){
this.deleteChild(0);
}
}});
});
},"curam/util/ResourceBundle":function(){
define(["dojo/_base/declare","dojo/i18n","dojo/string"],function(_dc5,i18n,_dc6){
var _dc7=_dc5("curam.util.ResourceBundle",null,{_bundle:undefined,constructor:function(_dc8,_dc9){
var _dca=_dc8.split(".");
var _dcb=_dca[_dca.length-1];
var _dcc=_dca.length==1?"curam.application":_dc8.slice(0,_dc8.length-_dcb.length-1);
try{
var b=i18n.getLocalization(_dcc,_dcb,_dc9);
if(this._isEmpty(b)){
throw new Error("Empty resource bundle.");
}else{
this._bundle=b;
}
}
catch(e){
throw new Error("Unable to access resource bundle: "+_dcc+"."+_dcb+": "+e.message);
}
},_isEmpty:function(_dcd){
for(var prop in _dcd){
return false;
}
return true;
},getProperty:function(key,_dce){
var msg=this._bundle[key];
var _dcf=msg;
if(_dce){
_dcf=_dc6.substitute(msg,_dce);
}
return _dcf;
}});
return _dc7;
});
},"dojox/layout/ExpandoPane":function(){
define(["dojo/_base/kernel","dojo/_base/lang","dojo/_base/declare","dojo/_base/array","dojo/_base/connect","dojo/_base/event","dojo/_base/fx","dojo/dom-style","dojo/dom-class","dojo/dom-geometry","dojo/text!./resources/ExpandoPane.html","dijit/layout/ContentPane","dijit/_TemplatedMixin","dijit/_Contained","dijit/_Container"],function(_dd0,lang,_dd1,_dd2,_dd3,_dd4,_dd5,_dd6,_dd7,_dd8,_dd9,_dda,_ddb,_ddc,_ddd){
_dd0.experimental("dojox.layout.ExpandoPane");
return _dd1("dojox.layout.ExpandoPane",[_dda,_ddb,_ddc,_ddd],{attributeMap:lang.delegate(_dda.prototype.attributeMap,{title:{node:"titleNode",type:"innerHTML"}}),templateString:_dd9,easeOut:"dojo._DefaultEasing",easeIn:"dojo._DefaultEasing",duration:420,startExpanded:true,previewOpacity:0.75,previewOnDblClick:false,tabIndex:"0",_setTabIndexAttr:"iconNode",baseClass:"dijitExpandoPane",postCreate:function(){
this.inherited(arguments);
this._animConnects=[];
this._isHorizontal=true;
if(lang.isString(this.easeOut)){
this.easeOut=lang.getObject(this.easeOut);
}
if(lang.isString(this.easeIn)){
this.easeIn=lang.getObject(this.easeIn);
}
var _dde="",rtl=!this.isLeftToRight();
if(this.region){
switch(this.region){
case "trailing":
case "right":
_dde=rtl?"Left":"Right";
this._needsPosition="left";
break;
case "leading":
case "left":
_dde=rtl?"Right":"Left";
break;
case "top":
_dde="Top";
break;
case "bottom":
this._needsPosition="top";
_dde="Bottom";
break;
}
_dd7.add(this.domNode,"dojoxExpando"+_dde);
_dd7.add(this.iconNode,"dojoxExpandoIcon"+_dde);
this._isHorizontal=/top|bottom/.test(this.region);
}
_dd6.set(this.domNode,{overflow:"hidden",padding:0});
this.connect(this.domNode,"ondblclick",this.previewOnDblClick?"preview":"toggle");
this.iconNode.setAttribute("aria-controls",this.id);
this.iconNode.setAttribute("role","button");
this.iconNode.setAttribute("aria-label",this.titleNode.innerHTML);
if(this.previewOnDblClick){
this.connect(this.getParent(),"_layoutChildren",lang.hitch(this,function(){
this._isonlypreview=false;
}));
}
},_startupSizes:function(){
this._container=this.getParent();
this._closedSize=this._titleHeight=_dd8.getMarginBox(this.titleWrapper).h;
if(this.splitter){
var myid=this.id;
_dd2.forEach(dijit.registry.toArray(),function(w){
if(w&&w.child&&w.child.id==myid){
this.connect(w,"_stopDrag","_afterResize");
}
},this);
}
this._currentSize=_dd8.getContentBox(this.domNode);
this._showSize=this._currentSize[(this._isHorizontal?"h":"w")];
this._setupAnims();
if(this.startExpanded){
this._showing=true;
}else{
this._showing=false;
this._hideWrapper();
this._hideAnim.gotoPercent(99,true);
}
this.domNode.setAttribute("aria-expanded",this._showing);
this._hasSizes=true;
},_afterResize:function(e){
var tmp=this._currentSize;
this._currentSize=_dd8.getMarginBox(this.domNode);
var n=this._currentSize[(this._isHorizontal?"h":"w")];
if(n>this._titleHeight){
if(!this._showing){
this._showing=!this._showing;
this._showEnd();
}
this._showSize=n;
this._setupAnims();
}else{
this._showSize=tmp[(this._isHorizontal?"h":"w")];
this._showing=false;
this._hideWrapper();
this._hideAnim.gotoPercent(89,true);
}
},_setupAnims:function(){
_dd2.forEach(this._animConnects,_dd3.disconnect);
var _ddf={node:this.domNode,duration:this.duration},_de0=this._isHorizontal,_de1={},_de2=this._showSize,_de3=this._closedSize,_de4={},_de5=_de0?"height":"width",also=this._needsPosition;
_de1[_de5]={end:_de2};
_de4[_de5]={end:_de3};
if(also){
_de1[also]={end:function(n){
var c=parseInt(n.style[also],10);
return c-_de2+_de3;
}};
_de4[also]={end:function(n){
var c=parseInt(n.style[also],10);
return c+_de2-_de3;
}};
}
this._showAnim=_dd5.animateProperty(lang.mixin(_ddf,{easing:this.easeIn,properties:_de1}));
this._hideAnim=_dd5.animateProperty(lang.mixin(_ddf,{easing:this.easeOut,properties:_de4}));
this._animConnects=[_dd3.connect(this._showAnim,"onEnd",this,"_showEnd"),_dd3.connect(this._hideAnim,"onEnd",this,"_hideEnd")];
},preview:function(){
if(!this._showing){
this._isonlypreview=!this._isonlypreview;
}
this.toggle();
},toggle:function(){
if(this._showing){
this._hideWrapper();
this._showAnim&&this._showAnim.stop();
this._hideAnim.play();
}else{
this._hideAnim&&this._hideAnim.stop();
this._showAnim.play();
}
this._showing=!this._showing;
this.domNode.setAttribute("aria-expanded",this._showing);
},_hideWrapper:function(){
_dd7.add(this.domNode,"dojoxExpandoClosed");
_dd6.set(this.cwrapper,{visibility:"hidden",opacity:"0",overflow:"hidden"});
},_showEnd:function(){
_dd6.set(this.cwrapper,{opacity:0,visibility:"visible"});
_dd5.anim(this.cwrapper,{opacity:this._isonlypreview?this.previewOpacity:1},227);
_dd7.remove(this.domNode,"dojoxExpandoClosed");
if(!this._isonlypreview){
setTimeout(lang.hitch(this._container,"layout"),15);
}else{
this._previewShowing=true;
this.resize();
}
},_hideEnd:function(){
if(!this._isonlypreview){
setTimeout(lang.hitch(this._container,"layout"),25);
}else{
this._previewShowing=false;
}
this._isonlypreview=false;
},resize:function(_de6){
if(!this._hasSizes){
this._startupSizes(_de6);
}
var _de7=_dd8.getMarginBox(this.domNode);
this._contentBox={w:_de6&&"w" in _de6?_de6.w:_de7.w,h:(_de6&&"h" in _de6?_de6.h:_de7.h)-this._titleHeight};
_dd6.set(this.containerNode,"height",this._contentBox.h+"px");
if(_de6){
_dd8.setMarginBox(this.domNode,_de6);
}
this._layoutChildren();
this._setupAnims();
},_trap:function(e){
_dd4.stop(e);
}});
});
},"dijit/_KeyNavMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","dijit/registry","dijit/_FocusMixin"],function(_de8,_de9,_dea,keys,lang,on,_deb,_dec){
return _de9("dijit._KeyNavMixin",_dec,{tabIndex:"0",childSelector:null,postCreate:function(){
this.inherited(arguments);
_dea.set(this.domNode,"tabIndex",this.tabIndex);
if(!this._keyNavCodes){
var _ded=this._keyNavCodes={};
_ded[keys.HOME]=lang.hitch(this,"focusFirstChild");
_ded[keys.END]=lang.hitch(this,"focusLastChild");
_ded[this.isLeftToRight()?keys.LEFT_ARROW:keys.RIGHT_ARROW]=lang.hitch(this,"_onLeftArrow");
_ded[this.isLeftToRight()?keys.RIGHT_ARROW:keys.LEFT_ARROW]=lang.hitch(this,"_onRightArrow");
_ded[keys.UP_ARROW]=lang.hitch(this,"_onUpArrow");
_ded[keys.DOWN_ARROW]=lang.hitch(this,"_onDownArrow");
}
var self=this,_dee=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.domNode,"keypress",lang.hitch(this,"_onContainerKeypress")),on(this.domNode,"keydown",lang.hitch(this,"_onContainerKeydown")),on(this.domNode,"focus",lang.hitch(this,"_onContainerFocus")),on(this.containerNode,on.selector(_dee,"focusin"),function(evt){
self._onChildFocus(_deb.getEnclosingWidget(this),evt);
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
},focusChild:function(_def,last){
if(!_def){
return;
}
if(this.focusedChild&&_def!==this.focusedChild){
this._onChildBlur(this.focusedChild);
}
_def.set("tabIndex",this.tabIndex);
_def.focus(last?"end":"start");
},_onContainerFocus:function(evt){
if(evt.target!==this.domNode||this.focusedChild){
return;
}
this.focus();
},_onFocus:function(){
_dea.set(this.domNode,"tabIndex","-1");
this.inherited(arguments);
},_onBlur:function(evt){
_dea.set(this.domNode,"tabIndex",this.tabIndex);
if(this.focusedChild){
this.focusedChild.set("tabIndex","-1");
this.lastFocusedChild=this.focusedChild;
this._set("focusedChild",null);
}
this.inherited(arguments);
},_onChildFocus:function(_df0){
if(_df0&&_df0!=this.focusedChild){
if(this.focusedChild&&!this.focusedChild._destroyed){
this.focusedChild.set("tabIndex","-1");
}
_df0.set("tabIndex",this.tabIndex);
this.lastFocused=_df0;
this._set("focusedChild",_df0);
}
},_searchString:"",multiCharSearchDuration:1000,onKeyboardSearch:function(item,evt,_df1,_df2){
if(item){
this.focusChild(item);
}
},_keyboardSearchCompare:function(item,_df3){
var _df4=item.domNode,text=item.label||(_df4.focusNode?_df4.focusNode.label:"")||_df4.innerText||_df4.textContent||"",_df5=text.replace(/^\s+/,"").substr(0,_df3.length).toLowerCase();
return (!!_df3.length&&_df5==_df3)?-1:0;
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
},_keyboardSearch:function(evt,_df6){
var _df7=null,_df8,_df9=0,_dfa=lang.hitch(this,function(){
if(this._searchTimer){
this._searchTimer.remove();
}
this._searchString+=_df6;
var _dfb=/^(.)\1*$/.test(this._searchString);
var _dfc=_dfb?1:this._searchString.length;
_df8=this._searchString.substr(0,_dfc);
this._searchTimer=this.defer(function(){
this._searchTimer=null;
this._searchString="";
},this.multiCharSearchDuration);
var _dfd=this.focusedChild||null;
if(_dfc==1||!_dfd){
_dfd=this._getNextFocusableChild(_dfd,1);
if(!_dfd){
return;
}
}
var stop=_dfd;
do{
var rc=this._keyboardSearchCompare(_dfd,_df8);
if(!!rc&&_df9++==0){
_df7=_dfd;
}
if(rc==-1){
_df9=-1;
break;
}
_dfd=this._getNextFocusableChild(_dfd,1);
}while(_dfd&&_dfd!=stop);
});
_dfa();
this.onKeyboardSearch(_df7,evt,_df8,_df9);
},_onChildBlur:function(){
},_getNextFocusableChild:function(_dfe,dir){
var _dff=_dfe;
do{
if(!_dfe){
_dfe=this[dir>0?"_getFirst":"_getLast"]();
if(!_dfe){
break;
}
}else{
_dfe=this._getNext(_dfe,dir);
}
if(_dfe!=null&&_dfe!=_dff&&_dfe.isFocusable()){
return _dfe;
}
}while(_dfe!=_dff);
return null;
},_getFirst:function(){
return null;
},_getLast:function(){
return null;
},_getNext:function(_e00,dir){
if(_e00){
_e00=_e00.domNode;
while(_e00){
_e00=_e00[dir<0?"previousSibling":"nextSibling"];
if(_e00&&"getAttribute" in _e00){
var w=_deb.byNode(_e00);
if(w){
return w;
}
}
}
}
return null;
}});
});
},"curam/util/UIMFragment":function(){
define(["curam/util/Request","curam/define","curam/debug","curam/util/ScreenContext"],function(_e01){
curam.define.singleton("curam.util.UIMFragment",{get:function(args){
var _e02=args&&args.pageID;
var url=args&&args.url;
var _e03=args&&args.params;
var _e04=args&&args.onLoad;
var _e05=args&&args.onDownloadError;
var _e06=args&&args.targetID;
if(_e06===""||typeof _e06==="undefined"){
throw "UIMFragment: targetID must be set.";
}
var _e07=null;
if(url){
_e07=url;
}else{
_e07=curam.util.UIMFragment._constructPath(_e02)+curam.util.UIMFragment._addCDEJParameters()+curam.util.UIMFragment._encodeParameters(_e03);
}
curam.debug.log("UIMFragment: GET to "+_e07);
curam.util.UIMFragment._doService(_e07,_e06,args,_e04,_e05);
},submitForm:function(_e08){
var _e08=dojo.fixEvent(_e08);
var _e09=_e08.target;
dojo.stopEvent(_e08);
var _e0a={url:curam.util.UIMFragment._constructFormActionPath(_e09),form:_e09,load:function(data){
var cp=dijit.getEnclosingWidget(_e09);
cp.set("content",data);
},error:function(_e0b){
alert("form error: error!!");
}};
_e01.post(_e0a);
console.log(_e08+" "+_e09);
},_constructFormActionPath:function(_e0c){
var _e0d="";
if(window===window.top){
_e0d=curam.config.locale+"/";
}
return _e0d+_e0c.getAttribute("action");
},_initForm:function(_e0e){
var _e0f=dojo.query("form",dijit.byId(_e0e).domNode)[0];
if(_e0f){
dojo.connect(_e0f,"onsubmit",curam.util.UIMFragment.submitForm);
}
},_constructPath:function(_e10){
var _e11=window;
var _e12=window.top;
return curam.util.UIMFragment._constructPathValue(_e10,_e11,_e12);
},_constructPathValue:function(_e13,_e14,_e15){
if(_e13===""||typeof _e13==="undefined"){
throw "UIMFragment: pageID must be set.";
}
var _e16="";
if(_e14.location.pathname===_e15.location.pathname){
var _e17=_e15.curam&&_e15.curam.config&&_e15.curam.config.locale;
_e16=(_e17||"en")+"/";
}
return _e16+_e13+"Page.do";
},_encodeParameters:function(_e18){
if(typeof _e18==="undefined"||dojo.toJson(_e18)==="{}"){
curam.debug.log("UIMFragment: No params included in request.");
return "";
}
var _e19=[];
for(var _e1a in _e18){
_e19.push(_e1a+"="+encodeURIComponent(_e18[_e1a]));
}
return "&"+_e19.join("&");
},_addCDEJParameters:function(){
return "?"+jsScreenContext.toRequestString();
},_doService:function(url,_e1b,args,_e1c,_e1d){
var cp=dijit.byId(_e1b);
cp.onLoad=dojo.hitch(cp,curam.util.UIMFragment._handleLoadSuccess,args,_e1c);
cp.preventCache=true;
cp.set("href",url);
},_handleDownloadError:function(_e1e){
curam.debug.log("Error invoking the UIMFragment: "+_e1e);
return "UIMFragment: Generic Error Handler";
},_handleLoadSuccess:function(_e1f,_e20){
curam.util.UIMFragment._initForm(_e1f.targetID);
if(_e20){
_e20(this);
}
curam.debug.log("");
return "UIMFragment: Generic Success Handler";
}});
return curam.util.UIMFragment;
});
},"dijit/form/_FormWidget":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/_base/kernel","dojo/ready","../_Widget","../_CssStateMixin","../_TemplatedMixin","./_FormWidgetMixin"],function(_e21,has,_e22,_e23,_e24,_e25,_e26,_e27){
if(1){
_e23(0,function(){
var _e28=["dijit/form/_FormValueWidget"];
require(_e28);
});
}
return _e21("dijit.form._FormWidget",[_e24,_e26,_e25,_e27],{setDisabled:function(_e29){
_e22.deprecated("setDisabled("+_e29+") is deprecated. Use set('disabled',"+_e29+") instead.","","2.0");
this.set("disabled",_e29);
},setValue:function(_e2a){
_e22.deprecated("dijit.form._FormWidget:setValue("+_e2a+") is deprecated.  Use set('value',"+_e2a+") instead.","","2.0");
this.set("value",_e2a);
},getValue:function(){
_e22.deprecated(this.declaredClass+"::getValue() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},postMixInProperties:function(){
this.nameAttrSetting=(this.name&&!has("msapp"))?("name=\""+this.name.replace(/"/g,"&quot;")+"\""):"";
this.inherited(arguments);
}});
});
},"dojo/dnd/common":function(){
define(["../sniff","../_base/kernel","../_base/lang","../dom"],function(has,_e2b,lang,dom){
var _e2c=lang.getObject("dojo.dnd",true);
_e2c.getCopyKeyState=function(evt){
return evt[has("mac")?"metaKey":"ctrlKey"];
};
_e2c._uniqueId=0;
_e2c.getUniqueId=function(){
var id;
do{
id=_e2b._scopeName+"Unique"+(++_e2c._uniqueId);
}while(dom.byId(id));
return id;
};
_e2c._empty={};
_e2c.isFormElement=function(e){
var t=e.target;
if(t.nodeType==3){
t=t.parentNode;
}
return " a button textarea input select option ".indexOf(" "+t.tagName.toLowerCase()+" ")>=0;
};
return _e2c;
});
},"dijit/CheckedMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-class","./MenuItem","dojo/text!./templates/CheckedMenuItem.html","./hccss"],function(_e2d,_e2e,_e2f,_e30){
return _e2d("dijit.CheckedMenuItem",_e2f,{baseClass:"dijitMenuItem dijitCheckedMenuItem",templateString:_e30,checked:false,_setCheckedAttr:function(_e31){
this.domNode.setAttribute("aria-checked",_e31?"true":"false");
this._set("checked",_e31);
},iconClass:"",role:"menuitemcheckbox",checkedChar:"&#10003;",onChange:function(){
},_onClick:function(evt){
if(!this.disabled){
this.set("checked",!this.checked);
this.onChange(this.checked);
}
this.onClick(evt);
}});
});
},"dijit/Viewport":function(){
define(["dojo/Evented","dojo/on","dojo/domReady","dojo/sniff","dojo/window"],function(_e32,on,_e33,has,_e34){
var _e35=new _e32();
var _e36;
_e33(function(){
var _e37=_e34.getBox();
_e35._rlh=on(window,"resize",function(){
var _e38=_e34.getBox();
if(_e37.h==_e38.h&&_e37.w==_e38.w){
return;
}
_e37=_e38;
_e35.emit("resize");
});
if(has("ie")==8){
var _e39=screen.deviceXDPI;
setInterval(function(){
if(screen.deviceXDPI!=_e39){
_e39=screen.deviceXDPI;
_e35.emit("resize");
}
},500);
}
if(has("ios")){
on(document,"focusin",function(evt){
_e36=evt.target;
});
on(document,"focusout",function(evt){
_e36=null;
});
}
});
_e35.getEffectiveBox=function(doc){
var box=_e34.getBox(doc);
var tag=_e36&&_e36.tagName&&_e36.tagName.toLowerCase();
if(has("ios")&&_e36&&!_e36.readOnly&&(tag=="textarea"||(tag=="input"&&/^(color|email|number|password|search|tel|text|url)$/.test(_e36.type)))){
box.h*=(orientation==0||orientation==180?0.66:0.4);
var rect=_e36.getBoundingClientRect();
box.h=Math.max(box.h,rect.top+rect.height);
}
return box;
};
return _e35;
});
},"curam/widget/form/ToggleButtonGroup":function(){
define(["dojo/_base/declare","dojo/_base/connect","dijit/form/ToggleButton"],function(_e3a,_e3b,_e3c){
return _e3a("curam.widget.form.ToggleButtonGroup",[_e3c],{_connectHandler:null,_unselectChannel:null,groupName:"toggleButtonGroup",postMixInProperties:function(){
this.inherited(arguments);
this._unselectChannel="/toggleButtonGroup%$��!|WE/"+this.groupName;
this._connectHandler=_e3b.subscribe(this._unselectChannel,this,"_unselect");
},_unselect:function(_e3d){
if(_e3d!==this&&this.checked){
this.set("checked",false);
}
},_onClick:function(e){
if(this.disabled){
return false;
}
if(!this.checked){
this._select();
}
return this.onClick(e);
},_select:function(){
dojo.publish(this._unselectChannel,[this]);
this.set("checked",true);
},_setCheckedAttr:function(_e3e,_e3f){
dojo.publish(this._unselectChannel,[this]);
this.inherited(arguments);
},destroy:function(){
try{
_e3b.disconnect(this._connectHandler);
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
},"idx/form/_FocusManager":function(){
define(["dijit/focus","dojo/_base/window","dojo/window","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/declare","dojo/_base/lang","dijit/registry"],function(_e40,win,_e41,dom,_e42,_e43,_e44,lang,_e45){
_e40._onTouchNode=function(node,by){
var _e46=node;
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_e43.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _e47=[];
try{
while(node){
var _e48=_e42.get(node,"dijitPopupParent");
if(typeof node.dijitPopupParent=="object"){
node=node.dijitPopupParent;
}else{
if(_e48){
node=_e45.byId(_e48)?_e45.byId(_e48).domNode:dom.byId(_e48);
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_e41.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_e49=id&&_e45.byId(id);
if(_e49&&!(by=="mouse"&&_e49.get("disabled"))){
if(!_e49._isValidFocusNode||_e49._isValidFocusNode(_e46)){
_e47.unshift(id);
}
}
node=node.parentNode;
}
}
}
}
}
catch(e){
}
this._setStack(_e47,by);
};
return _e40;
});
},"dijit/_base/place":function(){
define(["dojo/_base/array","dojo/_base/lang","dojo/window","../place","../main"],function(_e4a,lang,_e4b,_e4c,_e4d){
var _e4e={};
_e4e.getViewport=function(){
return _e4b.getBox();
};
_e4e.placeOnScreen=_e4c.at;
_e4e.placeOnScreenAroundElement=function(node,_e4f,_e50,_e51){
var _e52;
if(lang.isArray(_e50)){
_e52=_e50;
}else{
_e52=[];
for(var key in _e50){
_e52.push({aroundCorner:key,corner:_e50[key]});
}
}
return _e4c.around(node,_e4f,_e52,true,_e51);
};
_e4e.placeOnScreenAroundNode=_e4e.placeOnScreenAroundElement;
_e4e.placeOnScreenAroundRectangle=_e4e.placeOnScreenAroundElement;
_e4e.getPopupAroundAlignment=function(_e53,_e54){
var _e55={};
_e4a.forEach(_e53,function(pos){
var ltr=_e54;
switch(pos){
case "after":
_e55[_e54?"BR":"BL"]=_e54?"BL":"BR";
break;
case "before":
_e55[_e54?"BL":"BR"]=_e54?"BR":"BL";
break;
case "below-alt":
ltr=!ltr;
case "below":
_e55[ltr?"BL":"BR"]=ltr?"TL":"TR";
_e55[ltr?"BR":"BL"]=ltr?"TR":"TL";
break;
case "above-alt":
ltr=!ltr;
case "above":
default:
_e55[ltr?"TL":"TR"]=ltr?"BL":"BR";
_e55[ltr?"TR":"TL"]=ltr?"BR":"BL";
break;
}
});
return _e55;
};
lang.mixin(_e4d,_e4e);
return _e4d;
});
},"curam/tab":function(){
define(["dijit/registry","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-class","curam/inspection/Layer","curam/define","curam/util","curam/util/ScreenContext"],function(_e56,dom,_e57,_e58,_e59,_e5a){
curam.define.singleton("curam.tab",{SECTION_TAB_CONTAINER_ID:"app-sections-container-dc",SMART_PANEL_IFRAME_ID:"curam_tab_SmartPanelIframe",toBeExecutedOnTabClose:[],_mockSelectedTab:null,getSelectedTab:function(_e5b){
if(curam.tab._mockSelectedTab){
return curam.tab._mockSelectedTab;
}
if(curam.tab.getTabContainer(_e5b)){
return curam.tab.getTabContainer(_e5b).selectedChildWidget;
}
},getTabContainer:function(_e5c){
return curam.tab.getTabContainerFromSectionID(_e5c||curam.tab.getCurrentSectionId());
},getCurrentSectionId:function(_e5d){
var _e5e=curam.util.getTopmostWindow().dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
if(_e5e){
var _e5f=_e5e.selectedChildWidget.domNode.id;
return _e5f.substring(0,_e5f.length-4);
}else{
if(!_e5d){
throw new Error("curam.tab.getCurrentSectionId() - application section"+" tab container not found");
}
}
return null;
},inTabbedUI:function(){
return curam.tab.getCurrentSectionId(true)!=null;
},getTabContainerFromSectionID:function(_e60){
var _e61=_e56.byId(_e60+"-stc");
if(!_e61&&window.parent&&window.parent!=window){
_e61=curam.util.getTopmostWindow().dijit.byId(_e60+"-stc");
}
return _e61;
},getTabWidgetId:function(tab){
return tab.id;
},getSelectedTabWidgetId:function(){
return curam.tab.getTabWidgetId(curam.tab.getSelectedTab());
},getContainerTab:function(node){
var _e62=dijit.getEnclosingWidget(node);
if(_e62&&!_e62.tabDescriptor){
_e62=curam.tab.getContainerTab(_e62.domNode.parentNode);
}
if(!_e62||!_e62.tabDescriptor){
throw "Containing tab widget could not be found for node: "+node;
}
return _e62;
},getContentPanelIframe:function(tab){
var _e63=tab?tab:curam.tab.getSelectedTab(),_e64=null;
if(_e63){
_e64=dojo.query("iframe",_e63.domNode).filter(function(item){
return _e57.get(item,"iscpiframe")=="true";
})[0];
}
return _e64?_e64:null;
},refreshMainContentPanel:function(tab){
var _e65=tab?tab:curam.tab.getSelectedTab();
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_e65.domNode]);
var _e66=curam.tab.getContentPanelIframe(tab);
_e66.contentWindow.curam.util.publishRefreshEvent();
_e66.contentWindow.location.reload(false);
},getSmartPanelIframe:function(tab){
var _e67=tab?tab:curam.tab.getSelectedTab();
var _e68=dojo.query("iframe",_e67.domNode).filter(function(item){
return item.id==curam.tab.SMART_PANEL_IFRAME_ID;
})[0];
return _e68;
},unsubscribeOnTabClose:function(_e69,_e6a){
curam.tab.toBeExecutedOnTabClose.push(function(_e6b){
if(_e6a==_e6b){
dojo.unsubscribe(_e69);
return true;
}
return false;
});
},executeOnTabClose:function(func,_e6c){
curam.tab.toBeExecutedOnTabClose.push(function(_e6d){
if(_e6c==_e6d){
func();
return true;
}
return false;
});
},doExecuteOnTabClose:function(_e6e){
var _e6f=new Array();
for(var i=0;i<curam.tab.toBeExecutedOnTabClose.length;i++){
var func=curam.tab.toBeExecutedOnTabClose[i];
if(!func(_e6e)){
_e6f.push(func);
}
}
curam.tab.toBeExecutedOnTabClose=_e6f;
},getHandlerForTab:function(_e70,_e71){
return function(_e72,_e73){
if(_e73==_e71){
_e70(_e72,_e71);
}else{
}
};
},getTabController:function(){
return curam.util.getTopmostWindow().curam.ui.UIController;
},initTabLinks:function(_e74){
dojo.query("a").forEach(function(link){
if(link.href.indexOf("#")!=0&&link.href.indexOf("javascript:")!=0&&(link.href.indexOf("Page.do")>-1||link.href.indexOf("Frame.do")>-1)){
if(link.href.indexOf("&o3ctx")<0&&link.href.indexOf("?o3ctx")<0){
var _e75=(link.href.indexOf("?")>-1)?"&":"?";
link.href+=_e75+jsScreenContext.toRequestString();
}
}
});
elements=document.forms;
for(var i=0;i<elements.length;++i){
elem=elements[i];
var _e76=dom.byId("o3ctx");
if(!_e76){
var ctx=new curam.util.ScreenContext();
ctx.setContextBits("ACTION");
_e58.create("input",{"type":"hidden","name":"o3ctx","value":ctx.getValue()},elem);
}
_e58.create("input",{"type":"hidden","name":"o3prv","value":jsPageID},elem);
}
if(elements.length>0){
curam.util.getTopmostWindow().dojo.publish("curam.fireNextRequest",[]);
}
},initContent:function(_e77,_e78){
var _e79=dom.byId("content");
_e59.remove(_e79,"hidden-panel");
return;
},setupSectionSelectionListener:function(){
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",curam.tab.onSectionSelected);
},onSectionSelected:function(_e7a){
if(_e7a.curamDefaultPageID){
var _e7b;
if(_e7a.id.substring(_e7a.id.length-4,_e7a.id.length)=="-sbc"){
var _e7c=_e7a.id.substring(0,_e7a.id.length-4);
_e7b=curam.tab.getTabContainer(_e7c);
}else{
_e7b=_e7a;
}
if(_e7b&&_e7b.getChildren().length==0){
curam.tab.getTabController().handleUIMPageID(_e7a.curamDefaultPageID,true);
}
return true;
}
return false;
},setSectionDefaultPage:function(_e7d,_e7e){
var _e7f=_e56.byId(_e7d);
if(_e7f){
_e7f.curamDefaultPageID=_e7e;
}else{
throw "curam.tab.setSectionDefaultPage() - cannot find section dijit ID:"+_e7d;
}
},publishSmartPanelContentReady:function(){
var _e80="smartpanel.content.loaded";
var _e81=window.frameElement;
_e81.setAttribute("_SPContentLoaded","true");
curam.util.getTopmostWindow().dojo.publish(_e80,[_e81]);
}});
_e5a.register("curam/tab",curam.tab);
return curam.tab;
});
},"dijit/MenuSeparator":function(){
define(["dojo/_base/declare","dojo/dom","./_WidgetBase","./_TemplatedMixin","./_Contained","dojo/text!./templates/MenuSeparator.html"],function(_e82,dom,_e83,_e84,_e85,_e86){
return _e82("dijit.MenuSeparator",[_e83,_e84,_e85],{templateString:_e86,buildRendering:function(){
this.inherited(arguments);
dom.setSelectable(this.domNode,false);
},isFocusable:function(){
return false;
}});
});
},"cm/_base/_dom":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class"],function(dom,_e87,_e88){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(node,_e89){
return cm._findSibling(node,_e89,true);
},prevSibling:function(node,_e8a){
return cm._findSibling(node,_e8a,false);
},getInput:function(name,_e8b){
if(!dojo.isString(name)){
return name;
}
var _e8c=dojo.query("input[name='"+name+"'],select[name='"+name+"']");
return _e8b?(_e8c.length>0?_e8c:null):(_e8c.length>0?_e8c[0]:null);
},getParentByClass:function(node,_e8d){
node=node.parentNode;
while(node){
if(_e88.contains(node,_e8d)){
return node;
}
node=node.parentNode;
}
return null;
},getParentByType:function(node,type){
node=node.parentNode;
type=type.toLowerCase();
var _e8e="html";
while(node){
if(node.tagName.toLowerCase()==_e8e){
break;
}
if(node.tagName.toLowerCase()==type){
return node;
}
node=node.parentNode;
}
return null;
},replaceClass:function(node,_e8f,_e90){
_e88.remove(node,_e90);
_e88.add(node,_e8f);
},setClass:function(node,_e91){
node=dom.byId(node);
var cs=new String(_e91);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_e91);
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
},_findSibling:function(node,_e92,_e93){
if(!node){
return null;
}
if(_e92){
_e92=_e92.toLowerCase();
}
var _e94=_e93?"nextSibling":"previousSibling";
do{
node=node[_e94];
}while(node&&node.nodeType!=1);
if(node&&_e92&&_e92!=node.tagName.toLowerCase()){
return cm[_e93?"nextSibling":"prevSibling"](node,_e92);
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
_e87.set(node,"display",_e87.get(node,"display")=="none"?"":"none");
},endsWith:function(str,end,_e95){
if(_e95){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_e87.set(n,"display","none");
},show:function(n){
_e87.set(n,"display","");
}});
return cm;
});
},"dijit/_base/focus":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/topic","dojo/_base/window","../focus","../selection","../main"],function(_e96,dom,lang,_e97,win,_e98,_e99,_e9a){
var _e9b={_curFocus:null,_prevFocus:null,isCollapsed:function(){
return _e9a.getBookmark().isCollapsed;
},getBookmark:function(){
var sel=win.global==window?_e99:new _e99.SelectionManager(win.global);
return sel.getBookmark();
},moveToBookmark:function(_e9c){
var sel=win.global==window?_e99:new _e99.SelectionManager(win.global);
return sel.moveToBookmark(_e9c);
},getFocus:function(menu,_e9d){
var node=!_e98.curNode||(menu&&dom.isDescendant(_e98.curNode,menu.domNode))?_e9a._prevFocus:_e98.curNode;
return {node:node,bookmark:node&&(node==_e98.curNode)&&win.withGlobal(_e9d||win.global,_e9a.getBookmark),openedForWindow:_e9d};
},_activeStack:[],registerIframe:function(_e9e){
return _e98.registerIframe(_e9e);
},unregisterIframe:function(_e9f){
_e9f&&_e9f.remove();
},registerWin:function(_ea0,_ea1){
return _e98.registerWin(_ea0,_ea1);
},unregisterWin:function(_ea2){
_ea2&&_ea2.remove();
}};
_e98.focus=function(_ea3){
if(!_ea3){
return;
}
var node="node" in _ea3?_ea3.node:_ea3,_ea4=_ea3.bookmark,_ea5=_ea3.openedForWindow,_ea6=_ea4?_ea4.isCollapsed:false;
if(node){
var _ea7=(node.tagName.toLowerCase()=="iframe")?node.contentWindow:node;
if(_ea7&&_ea7.focus){
try{
_ea7.focus();
}
catch(e){
}
}
_e98._onFocusNode(node);
}
if(_ea4&&win.withGlobal(_ea5||win.global,_e9a.isCollapsed)&&!_ea6){
if(_ea5){
_ea5.focus();
}
try{
win.withGlobal(_ea5||win.global,_e9a.moveToBookmark,null,[_ea4]);
}
catch(e2){
}
}
};
_e98.watch("curNode",function(name,_ea8,_ea9){
_e9a._curFocus=_ea9;
_e9a._prevFocus=_ea8;
if(_ea9){
_e97.publish("focusNode",_ea9);
}
});
_e98.watch("activeStack",function(name,_eaa,_eab){
_e9a._activeStack=_eab;
});
_e98.on("widget-blur",function(_eac,by){
_e97.publish("widgetBlur",_eac,by);
});
_e98.on("widget-focus",function(_ead,by){
_e97.publish("widgetFocus",_ead,by);
});
lang.mixin(_e9a,_e9b);
return _e9a;
});
},"curam/util/ScreenContext":function(){
define(["dojo/_base/declare"],function(_eae){
var _eaf={DEFAULT_CONTEXT:112,SAMPLE22:2,SAMPLE21:1,SAMPLE13:4,SAMPLE12:2,SAMPLE11:1,EXTAPP:1048576,SMART_PANEL:262144,NESTED_UIM:131072,ORG_TREE:65536,CONTEXT_PANEL:32768,LIST_ROW_INLINE_PAGE:8192,LIST_EVEN_ROW:16384,TAB:4096,TREE:2048,AGENDA:1024,POPUP:512,MODAL:256,HOME:128,HEADER:64,NAVIGATOR:32,FOOTER:16,OVAL:8,RESOLVE:4,ACTION:2,ERROR:1,EMPTY:0};
var _eb0=[["ERROR","ACTION","RESOLVE","OPT_VALIDATION","FOOTER","NAVIGATOR","HEADER","HOME_PAGE","MODAL","POPUP","AGENDA","TREE","TAB","LIST_EVEN_ROW","LIST_ROW_INLINE_PAGE","CONTEXT_PANEL","ORG_TREE","NESTED_UIM","SMART_PANEL","EXTAPP"],["SAMPLE11","SAMPLE12","SAMPLE13"],["SAMPLE21","SAMPLE22"]];
var _eb1=_eae("curam.util.ScreenContext",null,{constructor:function(_eb2){
if(_eb2){
this.setContext(_eb2);
}else{
this.currentContext=[_eaf["DEFAULT_CONTEXT"]|_eaf["DEFAULT_CONTEXT"]];
}
},setContext:function(_eb3){
var tmp=this.setup(_eb3);
this.currentContext=((tmp==null)?([_eaf["DEFAULT_CONTEXT"]|_eaf["DEFAULT_CONTEXT"]]):(tmp));
},addContextBits:function(_eb4,idx){
if(!_eb4){
return;
}
var _eb5=(idx)?idx:0;
var _eb6=this.parseContext(_eb4);
if(_eb6!=null){
this.currentContext[_eb5]|=_eb6;
}
return this.currentContext[_eb5];
},addAll:function(idx){
var _eb7=(idx)?idx:0;
this.currentContext[_eb7]=4294967295;
return this.currentContext[_eb7];
},clear:function(_eb8,idx){
if(!_eb8){
this.clearAll();
return;
}
var _eb9=(idx)?idx:0;
if(_eb8==0){
return this.currentContext[_eb9];
}
var _eba=this.parseContext(_eb8);
if(_eba!=null){
var _ebb=this.currentContext[_eb9]&_eba;
this.currentContext[_eb9]^=_ebb;
}
return this.currentContext[_eb9];
},clearAll:function(idx){
if(idx){
this.currentContext[idx]=0;
}else{
for(var i=0;i<this.currentContext.length;i++){
this.currentContext[i]=0;
}
}
},updateStates:function(_ebc){
this.clear("ERROR|ACTION|RESOLVE");
this.currentContext[0]=this.currentContext[0]|(_ebc&7);
},hasContextBits:function(_ebd,idx){
if(!_ebd){
return false;
}
var _ebe=(idx)?idx:0;
var _ebf=this.parseContext(_ebd);
if(_ebf!=null){
var _ec0=this.currentContext[_ebe]&_ebf;
return (_ec0==_ebf);
}
return false;
},getValue:function(){
var _ec1="";
for(var i=0;i<this.currentContext.length;i++){
_ec1+=this.currentContext[i]+"|";
}
return _ec1.substring(0,_ec1.length-1);
},toRequestString:function(){
return "o3ctx="+this.getValue();
},toBinary:function(){
var _ec2="";
for(var i=0;i<this.currentContext.length;i++){
_ec2+=this.currentContext[i].toString(2)+"|";
}
return _ec2.substring(0,_ec2.length-1);
},toString:function(){
var _ec3="";
for(var i=0;i<this.currentContext.length;i++){
var _ec4="";
var j=0;
while(j<_eb0[i].length){
if(((this.currentContext[i]>>j)&1)!=0){
_ec4+=","+_eb0[i][j];
}
j++;
}
if(_ec4==""){
return "{}";
}
_ec3+="|"+_ec4.replace(",","{")+((_ec4.length==0)?"":"}");
}
return _ec3.substring(1);
},parseContext:function(_ec5){
var _ec6=_ec5.replace(/,/g,"|");
var _ec7=_ec6.split("|");
var tmp=isNaN(_ec7[0])?parseInt(_eaf[_ec7[0]]):_ec7[0];
for(var i=1;i<_ec7.length;i++){
tmp=tmp|(isNaN(_ec7[i])?parseInt(_eaf[_ec7[i]]):_ec7[i]);
}
return (isNaN(tmp)?null:tmp);
},setup:function(_ec8){
if(!_ec8){
return null;
}
var _ec9=(""+_ec8).split("|");
var _eca=new Array(_ec9.length);
for(var i=0;i<_ec9.length;i++){
_eca[i]=this.parseContext(_ec9[_ec9.length-i-1]);
_eca[i]=_eca[i]|_eca[i];
if(!_eca[i]||isNaN(_eca[i])||_eca[i]>4294967295){
return null;
}
}
return _eca;
}});
return _eb1;
});
},"dijit/a11y":function(){
define(["dojo/_base/array","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/sniff","./main"],function(_ecb,dom,_ecc,_ecd,lang,has,_ece){
var _ecf;
var a11y={_isElementShown:function(elem){
var s=_ecd.get(elem);
return (s.visibility!="hidden")&&(s.visibility!="collapsed")&&(s.display!="none")&&(_ecc.get(elem,"type")!="hidden");
},hasDefaultTabStop:function(elem){
switch(elem.nodeName.toLowerCase()){
case "a":
return _ecc.has(elem,"href");
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
var _ed0=elem.contentDocument;
if("designMode" in _ed0&&_ed0.designMode=="on"){
return true;
}
body=_ed0.body;
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
if(_ecc.get(elem,"disabled")){
return _ecf;
}else{
if(_ecc.has(elem,"tabIndex")){
return +_ecc.get(elem,"tabIndex");
}else{
return a11y.hasDefaultTabStop(elem)?0:_ecf;
}
}
},isTabNavigable:function(elem){
return a11y.effectiveTabIndex(elem)>=0;
},isFocusable:function(elem){
return a11y.effectiveTabIndex(elem)>=-1;
},_getTabNavigable:function(root){
var _ed1,last,_ed2,_ed3,_ed4,_ed5,_ed6={};
function _ed7(node){
return node&&node.tagName.toLowerCase()=="input"&&node.type&&node.type.toLowerCase()=="radio"&&node.name&&node.name.toLowerCase();
};
var _ed8=a11y._isElementShown,_ed9=a11y.effectiveTabIndex;
var _eda=function(_edb){
for(var _edc=_edb.firstChild;_edc;_edc=_edc.nextSibling){
if(_edc.nodeType!=1||(has("ie")<=9&&_edc.scopeName!=="HTML")||!_ed8(_edc)){
continue;
}
var _edd=_ed9(_edc);
if(_edd>=0){
if(_edd==0){
if(!_ed1){
_ed1=_edc;
}
last=_edc;
}else{
if(_edd>0){
if(!_ed2||_edd<_ed3){
_ed3=_edd;
_ed2=_edc;
}
if(!_ed4||_edd>=_ed5){
_ed5=_edd;
_ed4=_edc;
}
}
}
var rn=_ed7(_edc);
if(_ecc.get(_edc,"checked")&&rn){
_ed6[rn]=_edc;
}
}
if(_edc.nodeName.toUpperCase()!="SELECT"){
_eda(_edc);
}
}
};
if(_ed8(root)){
_eda(root);
}
function rs(node){
return _ed6[_ed7(node)]||node;
};
return {first:rs(_ed1),last:rs(last),lowest:rs(_ed2),highest:rs(_ed4)};
},getFirstInTabbingOrder:function(root,doc){
var _ede=a11y._getTabNavigable(dom.byId(root,doc));
return _ede.lowest?_ede.lowest:_ede.first;
},getLastInTabbingOrder:function(root,doc){
var _edf=a11y._getTabNavigable(dom.byId(root,doc));
return _edf.last?_edf.last:_edf.highest;
}};
1&&lang.mixin(_ece,a11y);
return a11y;
});
},"idx/widget/Menu":function(){
define(["dojo/_base/lang","dojo/_base/array","dojo/_base/declare","dojo/_base/event","dojo/dom-geometry","dijit/_TemplatedMixin","dijit/_WidgetBase","dijit/Menu","dijit/MenuItem","dijit/registry","./_MenuOpenOnHoverMixin","dojo/text!./templates/Menu.html","dojo/text!./templates/_MenuColumn.html","idx/widgets"],function(lang,_ee0,_ee1,_ee2,_ee3,_ee4,_ee5,Menu,_ee6,_ee7,_ee8,_ee9,_eea){
var _eeb={"error":"oneuiErrorMenuItemIcon","warning":"oneuiWarningMenuItemIcon","confirmation":"oneuiConfirmationMenuItemIcon","information":"oneuiInformationMenuItemIcon","success":"oneuiSuccessMenuItemIcon","critical":"oneuiCriticalMenuItemIcon","attention":"oneuiAttentionMenuItemIcon","compliance":"oneuiComplianceMenuItemIcon"};
var Menu=_ee1("idx.widget.Menu",[Menu,_ee8],{idxBaseClass:"idxMenu",_containerNodes:null,columnNodes:null,menuForDialog:true,templateString:_ee9,constructor:function(){
this._containerNodes=[];
this.columnNodes=[];
},childSelector:function(node){
var _eec=_ee7.byNode(node);
return (_ee0.indexOf(this._containerNodes,node.parentNode)>=0)&&_eec&&_eec.focus;
},_getNextFocusableChild:function(_eed,dir){
var _eee=null;
var _eef=this.getChildren();
var _ef0;
if(_eed!=null){
_ef0=_ee0.indexOf(_eef,_eed);
if(_ef0!=-1){
_ef0+=dir;
if(_ef0<0){
_ef0=_eef.length-1;
}
if(_ef0>=_eef.length){
_ef0=0;
}
}
}else{
if(_eef.length==0){
_ef0=-1;
}else{
_ef0=(dir==1)?0:_eef.length-1;
}
}
if(_ef0!=-1){
var i=_ef0;
do{
if(_eef[i].isFocusable()){
_eee=_eef[i];
break;
}
i+=dir;
if(i<0){
i=_eef.length-1;
}
if(i>=_eef.length){
i=0;
}
}while(i!=_ef0);
}
return _eee;
},_moveToColumn:function(dir){
if(this.focusedChild){
for(var i=0;i<this._containerNodes.length;i++){
if(this.focusedChild.domNode.parentNode==this._containerNodes[i]){
var _ef1=i,yPos=_ee3.getMarginBox(this.focusedChild.domNode).t;
break;
}
}
}
if(_ef1!=undefined){
for(i=_ef1+dir;i>=0&&i<this._containerNodes.length;i+=dir){
var _ef2=_ee7.findWidgets(this._containerNodes[i]);
var _ef3=dojo.filter(_ef2,function(_ef4){
return _ef4.isFocusable();
});
if(_ef3.length>0){
var _ef5=i;
break;
}
}
if(_ef5!=undefined){
for(i=0;i<_ef3.length;i++){
var _ef6=_ef3[i];
var _ef7=_ee3.getMarginBox(_ef6.domNode);
if(yPos>=_ef7.t&&yPos<=_ef7.t+_ef7.h-1){
this.focusChild(_ef6);
return true;
}else{
if(yPos<_ef7.t){
if(i>0){
this.focusChild(_ef3[i-1]);
return true;
}else{
this.focusChild(_ef6);
return true;
}
}else{
if(i==_ef3.length-1){
this.focusChild(_ef6);
return true;
}
}
}
}
}
}
return false;
},_onKeyPress:function(evt){
if(evt.ctrlKey||evt.altKey){
return;
}
switch(evt.charOrCode){
case this._openSubMenuKey:
if(!this._moveToColumn(+1)){
this._moveToPopup(evt);
}
_ee2.stop(evt);
break;
case this._closeSubMenuKey:
if(!this._moveToColumn(-1)){
if(this.parentMenu){
if(this.parentMenu._isMenuBar){
this.parentMenu.focusPrev();
}else{
this.onCancel(false);
}
}
}
_ee2.stop(evt);
break;
}
},refresh:function(){
var _ef8=this.getChildren();
for(var i=0;i<_ef8.length;i++){
this.addChild(_ef8[i]);
}
},buildRendering:function(){
this.inherited(arguments);
this.containerNode=this._columnContainerNode;
},startup:function(){
if(this._started){
return;
}
this._started=true;
this.inherited(arguments);
this.refresh();
},addChild:function(_ef9,_efa){
while(this._containerNodes.length<=(_ef9.column||0)){
var node=_ee4.getCachedTemplate(_eea).cloneNode(true);
this._attachTemplateNodes(node,function(n,p){
return n.getAttribute(p);
});
this._columnContainerNode.appendChild(node);
}
this.containerNode=this._containerNodes[_ef9.column||0];
this.inherited(arguments);
this.containerNode=this._columnContainerNode;
}});
Menu.createMessageMenuItem=function(args){
var _efb="";
if(args){
if(args.timestamp){
_efb+="‏<span class=\"messageMenuTimestamp messagesContrast\">‎"+args.timestamp+"‏</span>‎";
}
if(args.content){
_efb+="‏ <span class=\"messageTitles\">‎"+args.content+"‏</span>‎";
}
if(args.messageId){
_efb+="‏ <span class=\"messagesContrast\">(‎"+args.messageId+"‏)</span>‎";
}
}
return new _ee6({label:_efb,iconClass:args&&args.type&&_eeb[args.type]});
};
var _efc=lang.getObject("idx.oneui",true);
_efc.Menu=Menu;
return Menu;
});
},"dijit/form/_ToggleButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr"],function(_efd,_efe){
return _efd("dijit.form._ToggleButtonMixin",null,{checked:false,_aria_attr:"aria-pressed",_onClick:function(evt){
var _eff=this.checked;
this._set("checked",!_eff);
var ret=this.inherited(arguments);
this.set("checked",ret?this.checked:_eff);
return ret;
},_setCheckedAttr:function(_f00,_f01){
this._set("checked",_f00);
var node=this.focusNode||this.domNode;
if(this._created){
if(_efe.get(node,"checked")!=!!_f00){
_efe.set(node,"checked",!!_f00);
}
}
node.setAttribute(this._aria_attr,String(_f00));
this._handleOnChange(_f00,_f01);
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
define(["dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/ready","./registry","./_WidgetBase","./_OnDijitClickMixin","./_FocusMixin","dojo/uacss","./hccss"],function(_f02,_f03,_f04,_f05,has,_f06,lang,_f07,_f08,_f09,_f0a,_f0b,_f0c){
function _f0d(){
};
function _f0e(_f0f){
return function(obj,_f10,_f11,_f12){
if(obj&&typeof _f10=="string"&&obj[_f10]==_f0d){
return obj.on(_f10.substring(2).toLowerCase(),lang.hitch(_f11,_f12));
}
return _f0f.apply(_f04,arguments);
};
};
_f02.around(_f04,"connect",_f0e);
if(_f06.connect){
_f02.around(_f06,"connect",_f0e);
}
var _f13=_f05("dijit._Widget",[_f0a,_f0b,_f0c],{onClick:_f0d,onDblClick:_f0d,onKeyDown:_f0d,onKeyPress:_f0d,onKeyUp:_f0d,onMouseDown:_f0d,onMouseMove:_f0d,onMouseOut:_f0d,onMouseOver:_f0d,onMouseLeave:_f0d,onMouseEnter:_f0d,onMouseUp:_f0d,constructor:function(_f14){
this._toConnect={};
for(var name in _f14){
if(this[name]===_f0d){
this._toConnect[name.replace(/^on/,"").toLowerCase()]=_f14[name];
delete _f14[name];
}
}
},postCreate:function(){
this.inherited(arguments);
for(var name in this._toConnect){
this.on(name,this._toConnect[name]);
}
delete this._toConnect;
},on:function(type,func){
if(this[this._onMap(type)]===_f0d){
return _f04.connect(this.domNode,type.toLowerCase(),this,func);
}
return this.inherited(arguments);
},_setFocusedAttr:function(val){
this._focused=val;
this._set("focused",val);
},setAttribute:function(attr,_f15){
_f06.deprecated(this.declaredClass+"::setAttribute(attr, value) is deprecated. Use set() instead.","","2.0");
this.set(attr,_f15);
},attr:function(name,_f16){
var args=arguments.length;
if(args>=2||typeof name==="object"){
return this.set.apply(this,arguments);
}else{
return this.get(name);
}
},getDescendants:function(){
_f06.deprecated(this.declaredClass+"::getDescendants() is deprecated. Use getChildren() instead.","","2.0");
return this.containerNode?_f07("[widgetId]",this.containerNode).map(_f09.byNode):[];
},_onShow:function(){
this.onShow();
},onShow:function(){
},onHide:function(){
},onClose:function(){
return true;
}});
if(1){
_f08(0,function(){
var _f17=["dijit/_base"];
require(_f17);
});
}
return _f13;
});
},"dojo/touch":function(){
define(["./_base/kernel","./aspect","./dom","./dom-class","./_base/lang","./on","./has","./mouse","./domReady","./_base/window"],function(dojo,_f18,dom,_f19,lang,on,has,_f1a,_f1b,win){
var ios4=has("ios")<5;
var _f1c=has("pointer-events")||has("MSPointer"),_f1d=(function(){
var _f1e={};
for(var type in {down:1,move:1,up:1,cancel:1,over:1,out:1}){
_f1e[type]=has("MSPointer")?"MSPointer"+type.charAt(0).toUpperCase()+type.slice(1):"pointer"+type;
}
return _f1e;
})();
var _f1f=has("touch-events");
var _f20,_f21,_f22=false,_f23,_f24,_f25,_f26,_f27,_f28;
var _f29;
function _f2a(_f2b,_f2c,_f2d){
if(_f1c&&_f2d){
return function(node,_f2e){
return on(node,_f2d,_f2e);
};
}else{
if(_f1f){
return function(node,_f2f){
var _f30=on(node,_f2c,function(evt){
_f2f.call(this,evt);
_f29=(new Date()).getTime();
}),_f31=on(node,_f2b,function(evt){
if(!_f29||(new Date()).getTime()>_f29+1000){
_f2f.call(this,evt);
}
});
return {remove:function(){
_f30.remove();
_f31.remove();
}};
};
}else{
return function(node,_f32){
return on(node,_f2b,_f32);
};
}
}
};
function _f33(node){
do{
if(node.dojoClick!==undefined){
return node;
}
}while(node=node.parentNode);
};
function _f34(e,_f35,_f36){
if(_f1a.isRight(e)){
return;
}
var _f37=_f33(e.target);
_f21=!e.target.disabled&&_f37&&_f37.dojoClick;
if(_f21){
_f22=(_f21=="useTarget");
_f23=(_f22?_f37:e.target);
if(_f22){
e.preventDefault();
}
_f24=e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX;
_f25=e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY;
_f26=(typeof _f21=="object"?_f21.x:(typeof _f21=="number"?_f21:0))||4;
_f27=(typeof _f21=="object"?_f21.y:(typeof _f21=="number"?_f21:0))||4;
if(!_f20){
_f20=true;
function _f38(e){
if(_f22){
_f21=dom.isDescendant(win.doc.elementFromPoint((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX),(e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)),_f23);
}else{
_f21=_f21&&(e.changedTouches?e.changedTouches[0].target:e.target)==_f23&&Math.abs((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX)-_f24)<=_f26&&Math.abs((e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)-_f25)<=_f27;
}
};
win.doc.addEventListener(_f35,function(e){
if(_f1a.isRight(e)){
return;
}
_f38(e);
if(_f22){
e.preventDefault();
}
},true);
win.doc.addEventListener(_f36,function(e){
if(_f1a.isRight(e)){
return;
}
_f38(e);
if(_f21){
_f28=(new Date()).getTime();
var _f39=(_f22?_f23:e.target);
if(_f39.tagName==="LABEL"){
_f39=dom.byId(_f39.getAttribute("for"))||_f39;
}
var src=(e.changedTouches)?e.changedTouches[0]:e;
function _f3a(type){
var evt=document.createEvent("MouseEvents");
evt._dojo_click=true;
evt.initMouseEvent(type,true,true,e.view,e.detail,src.screenX,src.screenY,src.clientX,src.clientY,e.ctrlKey,e.altKey,e.shiftKey,e.metaKey,0,null);
return evt;
};
var _f3b=_f3a("mousedown");
var _f3c=_f3a("mouseup");
var _f3d=_f3a("click");
setTimeout(function(){
on.emit(_f39,"mousedown",_f3b);
on.emit(_f39,"mouseup",_f3c);
on.emit(_f39,"click",_f3d);
_f28=(new Date()).getTime();
},0);
}
},true);
function _f3e(type){
win.doc.addEventListener(type,function(e){
if(_f21&&!e._dojo_click&&(new Date()).getTime()<=_f28+1000&&!(e.target.tagName=="INPUT"&&_f19.contains(e.target,"dijitOffScreen"))){
e.stopPropagation();
e.stopImmediatePropagation&&e.stopImmediatePropagation();
if(type=="click"&&(e.target.tagName!="INPUT"||e.target.type=="radio"||e.target.type=="checkbox")&&e.target.tagName!="TEXTAREA"&&e.target.tagName!="AUDIO"&&e.target.tagName!="VIDEO"){
e.preventDefault();
}
}
},true);
};
_f3e("click");
_f3e("mousedown");
_f3e("mouseup");
}
}
};
var _f3f;
if(has("touch")){
if(_f1c){
_f1b(function(){
win.doc.addEventListener(_f1d.down,function(evt){
_f34(evt,_f1d.move,_f1d.up);
},true);
});
}else{
_f1b(function(){
_f3f=win.body();
win.doc.addEventListener("touchstart",function(evt){
_f29=(new Date()).getTime();
var _f40=_f3f;
_f3f=evt.target;
on.emit(_f40,"dojotouchout",{relatedTarget:_f3f,bubbles:true});
on.emit(_f3f,"dojotouchover",{relatedTarget:_f40,bubbles:true});
_f34(evt,"touchmove","touchend");
},true);
function _f41(evt){
var _f42=lang.delegate(evt,{bubbles:true});
if(has("ios")>=6){
_f42.touches=evt.touches;
_f42.altKey=evt.altKey;
_f42.changedTouches=evt.changedTouches;
_f42.ctrlKey=evt.ctrlKey;
_f42.metaKey=evt.metaKey;
_f42.shiftKey=evt.shiftKey;
_f42.targetTouches=evt.targetTouches;
}
return _f42;
};
on(win.doc,"touchmove",function(evt){
_f29=(new Date()).getTime();
var _f43=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset));
if(_f43){
if(_f3f!==_f43){
on.emit(_f3f,"dojotouchout",{relatedTarget:_f43,bubbles:true});
on.emit(_f43,"dojotouchover",{relatedTarget:_f3f,bubbles:true});
_f3f=_f43;
}
if(!on.emit(_f43,"dojotouchmove",_f41(evt))){
evt.preventDefault();
}
}
});
on(win.doc,"touchend",function(evt){
_f29=(new Date()).getTime();
var node=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset))||win.body();
on.emit(node,"dojotouchend",_f41(evt));
});
});
}
}
var _f44={press:_f2a("mousedown","touchstart",_f1d.down),move:_f2a("mousemove","dojotouchmove",_f1d.move),release:_f2a("mouseup","dojotouchend",_f1d.up),cancel:_f2a(_f1a.leave,"touchcancel",_f1c?_f1d.cancel:null),over:_f2a("mouseover","dojotouchover",_f1d.over),out:_f2a("mouseout","dojotouchout",_f1d.out),enter:_f1a._eventHandler(_f2a("mouseover","dojotouchover",_f1d.over)),leave:_f1a._eventHandler(_f2a("mouseout","dojotouchout",_f1d.out))};
1&&(dojo.touch=_f44);
return _f44;
});
},"idx/string":function(){
define(["dojo/_base/lang","idx/main","dojo/string"],function(_f45,_f46,_f47){
var _f48=_f45.getObject("string",true,_f46);
var _f49="\\[]()^.+*{}?!=-";
var _f4a={};
var _f4b=0;
var _f4c=50;
_f48.unescapedIndexesOf=function(text,_f4d,_f4e,_f4f){
if(!_f4d||(_f4d.length==0)){
throw "One or more characters must be provided to search for unescaped characters: "+" chars=[ "+_f4d+" ], escaper=[ "+_f4e+" ], text=[ "+text+" ]";
}
if(!_f4e||_f4e.length==0){
_f4e="\\";
}
if(_f4e.length>1){
throw "The escaper must be a single character for unescaped character search: "+" escaper=[ "+_f4e+" ], chars=[ "+_f4d+" ], text=[ "+text+" ]";
}
if(_f4d.indexOf(_f4e)>=0){
throw "The escaping character cannot also be a search character for unescaped character search: "+" escaper=[ "+_f4e+" ], separators=[ "+separators+" ] text=[ "+text+" ]";
}
if((_f4f===null)||(_f4f===undefined)||(_f4f===0)||(_f4f===false)){
_f4f=-1;
}
if(_f4f<0){
_f4f=0;
}
var _f50=[];
var _f51=0;
var _f52=false;
var _f53=null;
for(_f51=0;_f51<text.length;_f51++){
_f53=text.charAt(_f51);
if(_f52){
_f52=false;
continue;
}
if(_f53==_f4e){
_f52=true;
continue;
}
if(_f4d.indexOf(_f53)>=0){
_f50.push(_f51);
if(_f4f&&(_f50.length==_f4f)){
break;
}
}
}
return _f50;
};
_f48.unescapedSplit=function(text,_f54,_f55,_f56){
if(!_f54||(_f54.length==0)){
_f54=",";
}
if(!_f55||_f55.length==0){
_f55="\\";
}
if(_f55.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_f55+" ], separators=[ "+_f54+" ], text=[ "+text+" ]";
}
if(_f54.indexOf(_f55)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_f55+" ], separators=[ "+_f54+" ] text=[ "+text+" ]";
}
if((_f56===null)||(_f56===undefined)||(_f56===0)||(_f56===false)){
_f56=-1;
}
if(_f56<0){
_f56=0;
}
var _f57=[];
var _f58=0;
var _f59=false;
var _f5a=null;
var _f5b=0;
for(_f58=0;_f58<text.length;_f58++){
_f5a=text.charAt(_f58);
if(_f59){
_f59=false;
continue;
}
if(_f5a==_f55){
_f59=true;
continue;
}
if(_f54.indexOf(_f5a)>=0){
if(_f5b==_f58){
_f57.push("");
}else{
_f57.push(text.substring(_f5b,_f58));
}
_f5b=_f58+1;
if(_f56&&(_f57.length==_f56)){
break;
}
}
}
if((!_f56)||(_f56&&_f57.length<_f56)){
if(_f5b==text.length){
_f57.push("");
}else{
_f57.push(text.substring(_f5b));
}
}
return _f57;
};
_f48.parseTokens=function(text,_f5c,_f5d,_f5e){
if(!_f5c||(_f5c.length==0)){
_f5c=",";
}
if(!_f5d||_f5d.length==0){
_f5d="\\";
}
if(_f5d.length>1){
throw "The escaper must be a single character for token parsing: "+" escaper=[ "+_f5d+" ], separators=[ "+_f5c+" ], text=[ "+text+" ]";
}
if(_f5c.indexOf(_f5d)>=0){
throw "The escaping character cannot also be a separator for token parsing: "+" escaper=[ "+_f5d+" ], separators=[ "+_f5c+" ] text=[ "+text+" ]";
}
if(_f5e&&_f5e.length==0){
_f5e=null;
}
var _f5f=0;
var _f60=[];
var _f61=0;
var _f62=false;
var part="";
var end=-1;
var _f63=null;
for(_f5f=0;_f5f<text.length;_f5f++){
_f63=text.charAt(_f5f);
end=-1;
if(!_f62&&_f63==_f5d){
if(_f5e&&((_f5f+1)<text.length)&&(_f5e.indexOf(_f63)>=0)){
_f5f++;
continue;
}
part=part+text.substring(_f61,_f5f);
_f61=_f5f+1;
_f62=true;
continue;
}
if(_f62){
_f62=false;
continue;
}
if(_f5c.indexOf(_f63)>=0){
end=_f5f;
}
if(end>=0){
part=part+text.substring(_f61,end);
_f61=end+1;
_f60.push(part);
part="";
}
}
if((end<0)&&(_f61<text.length)){
part=part+text.substring(_f61);
_f60.push(part);
}else{
if((end>=0)||_f62){
_f60.push(part);
}
}
return _f60;
};
_f48.escapeChars=function(text,_f64,_f65){
if(!_f64||(_f64.length==0)){
_f64="";
}
if(!_f65||_f65.length==0){
_f65="\\";
}
if(_f65.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_f65+" ], chars=[ "+_f64+" ], text=[ "+text+" ]";
}
if(_f64.indexOf(_f65)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_f65+" ], chars=[ "+_f64+" ] text=[ "+text+" ]";
}
var _f64=_f64+_f65;
var _f66=_f4a[_f64];
var _f67="";
if(!_f66){
_f67="([";
for(index=0;index<_f64.length;index++){
if(_f49.indexOf(_f64.charAt(index))>=0){
_f67=_f67+"\\";
}
_f67=_f67+_f64.charAt(index);
}
_f67=_f67+"])";
try{
_f66=new RegExp(_f67,"g");
}
catch(e){
console.log("Pattern: "+_f67);
throw e;
}
if(_f4b<_f4c){
_f4a[_f64]=_f66;
_f4b++;
}
}
return text.replace(_f66,_f65+"$1");
};
_f48.escapedJoin=function(arr,_f68,_f69){
if(!_f68||(_f68.length==0)){
_f68=",";
}
if(_f68.length>1){
throw "Only one separator character can be used in escapedJoin: ";
" separator=[ "+_f68+" ], text=[ "+text+" ]";
}
if(!_f69||_f69.length==0){
_f69="\\";
}
if(_f69.length>1){
throw "The escaper must be a single character for escaped split: "+" escaper=[ "+_f69+" ], separator=[ "+_f68+" ], text=[ "+text+" ]";
}
if(_f68.indexOf(_f69)>=0){
throw "The escaping character cannot also be a separator for escaped split: "+" escaper=[ "+_f69+" ], separator=[ "+_f68+" ] text=[ "+text+" ]";
}
var _f6a=0;
var _f6b="";
var part=null;
var _f6c="";
for(_f6a=0;_f6a<arr.length;_f6a++){
part=arr[_f6a];
part=_f48.escapeChars(part,_f68,_f69);
_f6b=_f6b+_f6c+part;
_f6c=_f68;
}
return _f6b;
};
_f48.startsWith=function(text,_f6d){
return (_f45.isString(text)&&_f45.isString(_f6d)&&text.indexOf(_f6d)===0);
};
_f48.endsWith=function(text,_f6e){
return (_f45.isString(text)&&_f45.isString(_f6e)&&text.lastIndexOf(_f6e)===text.length-_f6e.length);
};
_f48.equalsIgnoreCase=function(s1,s2){
return (_f45.isString(s1)&&_f45.isString(s2)&&s1.toLowerCase()===s2.toLowerCase());
};
_f48.isNumber=function(n){
return (typeof n=="number"&&isFinite(n));
};
_f48.nullTrim=function(str){
if(!str){
return null;
}
var _f6f=_f47.trim(str);
return (_f6f.length==0)?null:_f6f;
};
_f48.propToLabel=function(_f70){
if(!_f70){
return _f70;
}
_f70=_f47.trim(_f70);
var _f71=_f70.toUpperCase();
var _f72=_f70.toLowerCase();
var _f73=0;
var _f74="";
var _f75="";
var _f76=false;
var _f77=false;
for(_f73=0;_f73<_f70.length;_f73++){
var _f78=_f71.charAt(_f73);
var _f79=_f72.charAt(_f73);
var _f7a=_f70.charAt(_f73);
var _f7b=((_f78==_f7a)&&(_f79!=_f7a));
var _f7c=((_f79==_f7a)&&(_f78!=_f7a));
if((_f7a=="_")||(_f7a==" ")){
if(_f75==" "){
continue;
}
_f75=" ";
_f76=_f7b;
_f77=_f7c;
_f74=_f74+_f75;
continue;
}
if(_f7a=="."){
_f75="/";
_f76=_f7b;
_f77=_f7c;
_f74=_f74+" "+_f75+" ";
continue;
}
if((_f73==0)||(_f75==" ")){
_f75=_f78;
_f76=_f7b;
_f77=_f7c;
_f74=_f74+_f75;
continue;
}
if((!_f7b)&&(!_f7c)){
if(_f76||_f77){
_f74=_f74+" ";
}
_f76=_f7b;
_f77=_f7c;
_f75=_f7a;
_f74=_f74+_f75;
continue;
}
if((!_f76)&&(!_f77)){
var _f7d=(_f75=="/");
_f76=_f7b;
_f77=_f7c;
_f75=_f78;
if(_f7d){
_f74=_f74+_f75;
}else{
_f74=_f74+" "+_f75;
}
continue;
}
if(_f7b&&_f77){
_f75=_f78;
_f76=_f7b;
_f77=_f7c;
_f74=_f74+" "+_f75;
continue;
}
_f75=_f79;
_f76=_f7b;
_f77=_f7c;
_f74=_f74+_f75;
}
return _f74;
};
return _f48;
});
},"dojo/fx":function(){
define(["./_base/lang","./Evented","./_base/kernel","./_base/array","./aspect","./_base/fx","./dom","./dom-style","./dom-geometry","./ready","require"],function(lang,_f7e,dojo,_f7f,_f80,_f81,dom,_f82,geom,_f83,_f84){
if(!dojo.isAsync){
_f83(0,function(){
var _f85=["./fx/Toggler"];
_f84(_f85);
});
}
var _f86=dojo.fx={};
var _f87={_fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,args||[]);
}
return this;
}};
var _f88=function(_f89){
this._index=-1;
this._animations=_f89||[];
this._current=this._onAnimateCtx=this._onEndCtx=null;
this.duration=0;
_f7f.forEach(this._animations,function(a){
if(a){
if(typeof a.duration!="undefined"){
this.duration+=a.duration;
}
if(a.delay){
this.duration+=a.delay;
}
}
},this);
};
_f88.prototype=new _f7e();
lang.extend(_f88,{_onAnimate:function(){
this._fire("onAnimate",arguments);
},_onEnd:function(){
this._onAnimateCtx.remove();
this._onEndCtx.remove();
this._onAnimateCtx=this._onEndCtx=null;
if(this._index+1==this._animations.length){
this._fire("onEnd");
}else{
this._current=this._animations[++this._index];
this._onAnimateCtx=_f80.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
this._onEndCtx=_f80.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play(0,true);
}
},play:function(_f8a,_f8b){
if(!this._current){
this._current=this._animations[this._index=0];
}
if(!_f8b&&this._current.status()=="playing"){
return this;
}
var _f8c=_f80.after(this._current,"beforeBegin",lang.hitch(this,function(){
this._fire("beforeBegin");
}),true),_f8d=_f80.after(this._current,"onBegin",lang.hitch(this,function(arg){
this._fire("onBegin",arguments);
}),true),_f8e=_f80.after(this._current,"onPlay",lang.hitch(this,function(arg){
this._fire("onPlay",arguments);
_f8c.remove();
_f8d.remove();
_f8e.remove();
}));
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
this._onAnimateCtx=_f80.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
if(this._onEndCtx){
this._onEndCtx.remove();
}
this._onEndCtx=_f80.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play.apply(this._current,arguments);
return this;
},pause:function(){
if(this._current){
var e=_f80.after(this._current,"onPause",lang.hitch(this,function(arg){
this._fire("onPause",arguments);
e.remove();
}),true);
this._current.pause();
}
return this;
},gotoPercent:function(_f8f,_f90){
this.pause();
var _f91=this.duration*_f8f;
this._current=null;
_f7f.some(this._animations,function(a,_f92){
if(_f91<=a.duration){
this._current=a;
this._index=_f92;
return true;
}
_f91-=a.duration;
return false;
},this);
if(this._current){
this._current.gotoPercent(_f91/this._current.duration);
}
if(_f90){
this.play();
}
return this;
},stop:function(_f93){
if(this._current){
if(_f93){
for(;this._index+1<this._animations.length;++this._index){
this._animations[this._index].stop(true);
}
this._current=this._animations[this._index];
}
var e=_f80.after(this._current,"onStop",lang.hitch(this,function(arg){
this._fire("onStop",arguments);
e.remove();
}),true);
this._current.stop();
}
return this;
},status:function(){
return this._current?this._current.status():"stopped";
},destroy:function(){
this.stop();
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
if(this._onEndCtx){
this._onEndCtx.remove();
}
}});
lang.extend(_f88,_f87);
_f86.chain=function(_f94){
return new _f88(_f94);
};
var _f95=function(_f96){
this._animations=_f96||[];
this._connects=[];
this._finished=0;
this.duration=0;
_f7f.forEach(_f96,function(a){
var _f97=a.duration;
if(a.delay){
_f97+=a.delay;
}
if(this.duration<_f97){
this.duration=_f97;
}
this._connects.push(_f80.after(a,"onEnd",lang.hitch(this,"_onEnd"),true));
},this);
this._pseudoAnimation=new _f81.Animation({curve:[0,1],duration:this.duration});
var self=this;
_f7f.forEach(["beforeBegin","onBegin","onPlay","onAnimate","onPause","onStop","onEnd"],function(evt){
self._connects.push(_f80.after(self._pseudoAnimation,evt,function(){
self._fire(evt,arguments);
},true));
});
};
lang.extend(_f95,{_doAction:function(_f98,args){
_f7f.forEach(this._animations,function(a){
a[_f98].apply(a,args);
});
return this;
},_onEnd:function(){
if(++this._finished>this._animations.length){
this._fire("onEnd");
}
},_call:function(_f99,args){
var t=this._pseudoAnimation;
t[_f99].apply(t,args);
},play:function(_f9a,_f9b){
this._finished=0;
this._doAction("play",arguments);
this._call("play",arguments);
return this;
},pause:function(){
this._doAction("pause",arguments);
this._call("pause",arguments);
return this;
},gotoPercent:function(_f9c,_f9d){
var ms=this.duration*_f9c;
_f7f.forEach(this._animations,function(a){
a.gotoPercent(a.duration<ms?1:(ms/a.duration),_f9d);
});
this._call("gotoPercent",arguments);
return this;
},stop:function(_f9e){
this._doAction("stop",arguments);
this._call("stop",arguments);
return this;
},status:function(){
return this._pseudoAnimation.status();
},destroy:function(){
this.stop();
_f7f.forEach(this._connects,function(_f9f){
_f9f.remove();
});
}});
lang.extend(_f95,_f87);
_f86.combine=function(_fa0){
return new _f95(_fa0);
};
_f86.wipeIn=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_f81.animateProperty(lang.mixin({properties:{height:{start:function(){
o=s.overflow;
s.overflow="hidden";
if(s.visibility=="hidden"||s.display=="none"){
s.height="1px";
s.display="";
s.visibility="";
return 1;
}else{
var _fa1=_f82.get(node,"height");
return Math.max(_fa1,1);
}
},end:function(){
return node.scrollHeight;
}}}},args));
var fini=function(){
s.height="auto";
s.overflow=o;
};
_f80.after(anim,"onStop",fini,true);
_f80.after(anim,"onEnd",fini,true);
return anim;
};
_f86.wipeOut=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_f81.animateProperty(lang.mixin({properties:{height:{end:1}}},args));
_f80.after(anim,"beforeBegin",function(){
o=s.overflow;
s.overflow="hidden";
s.display="";
},true);
var fini=function(){
s.overflow=o;
s.height="auto";
s.display="none";
};
_f80.after(anim,"onStop",fini,true);
_f80.after(anim,"onEnd",fini,true);
return anim;
};
_f86.slideTo=function(args){
var node=args.node=dom.byId(args.node),top=null,left=null;
var init=(function(n){
return function(){
var cs=_f82.getComputedStyle(n);
var pos=cs.position;
top=(pos=="absolute"?n.offsetTop:parseInt(cs.top)||0);
left=(pos=="absolute"?n.offsetLeft:parseInt(cs.left)||0);
if(pos!="absolute"&&pos!="relative"){
var ret=geom.position(n,true);
top=ret.y;
left=ret.x;
n.style.position="absolute";
n.style.top=top+"px";
n.style.left=left+"px";
}
};
})(node);
init();
var anim=_f81.animateProperty(lang.mixin({properties:{top:args.top||0,left:args.left||0}},args));
_f80.after(anim,"beforeBegin",init,true);
return anim;
};
return _f86;
});
},"curam/util/cache/CacheLRU":function(){
define(["dojo/_base/declare","dojox/collections/Dictionary"],function(_fa2,_fa3){
var _fa4=_fa2("curam/util/cache/CacheItem",null,{constPriority:{Low:10,Normal:20,High:30},constructor:function(keys,_fa5,_fa6){
if(keys==null){
throw new Error("Cache key cannot be null ");
}
this.key=keys;
this.value=_fa5;
if(_fa6==null){
_fa6={};
}
if(_fa6.priority==null){
_fa6.priority=this.constPriority.Normal;
}
this.options=_fa6;
this.lastAccessed=new Date().getTime();
},destroy:function(){
try{
this.inherited(arguments);
}
catch(err){
console.error(err);
}
}});
return _fa2("curam/util/cache/CacheLRU",null,{maxSize:20,activePurgeFrequency:null,_dataColection:null,_tippingPoint:null,_purgePoint:null,_purgingNow:false,constPriority:{Low:10,Normal:20,High:30},constructor:function(_fa7){
try{
dojo.mixin(this,_fa7);
this._dataColection=new dojox.collections.Dictionary();
if(this.maxSize==null){
this.maxSize=-1;
}
if(this.activePurgeFrequency==null){
this.activePurgeFrequency=-1;
}
this._tippingPoint=0.75;
this._purgePoint=Math.round(this.maxSize*this._tippingPoint);
this._purgingNow=false;
if(this.activePurgeFrequency>0){
this._timerPurge();
}
}
catch(e){
console.error("There has been an issue with cache LRU\"");
console.error(e);
}
},addItem:function(key,data,_fa8){
if(this.maxSize<1){
return;
}
if(this._dataColection.contains(key)==true){
this._removeItem(key);
}
var _fa9=new _fa4(key,data,_fa8);
this._dataColection.add(_fa9.key,_fa9);
if((this.maxSize>0)&&(this._dataColection.count>this.maxSize)){
this._purge();
}
},getItem:function(key){
var item=this._dataColection.item(key);
if(item!=null){
if(!this._isExpired(item)){
item.lastAccessed=new Date().getTime();
}else{
this._removeItem(key);
item=null;
}
}
var _faa=null;
if(item!=null){
_faa=item.value;
}
return _faa;
},clear:function(){
this._dataColection.forEach(function(data,_fab,_fac){
var tmp=data.value;
this._removeItem(tmp.key);
},this);
},_purge:function(){
console.debug("purging cache");
this._purgingNow=true;
var _fad=new Array();
this._dataColection.forEach(function(data,_fae,_faf){
var tmp=data.value;
if(this._isExpired(tmp)){
this._removeItem(tmp.key);
}else{
_fad.push(tmp);
}
},this);
if(_fad.length>this._purgePoint){
_fad=_fad.sort(function(a,b){
if(a.options.priority!=b.options.priority){
return b.options.priority-a.options.priority;
}else{
return b.lastAccessed-a.lastAccessed;
}
});
while(_fad.length>this._purgePoint){
var temp=_fad.pop();
this._removeItem(temp.key);
}
}
this._purgingNow=false;
},_removeItem:function(key){
var item=this._dataColection.item(key);
this._dataColection.remove(key);
if(item.options.callback!=null){
var _fb0=dojo.hitch(this,function(){
item.options.callback(item.key,item.value);
});
setTimeout(_fb0,0);
}
},_isExpired:function(item){
var now=new Date().getTime();
var _fb1=false;
if((item.options.expirationAbsolute)&&(item.options.expirationAbsolute<now)){
_fb1=true;
}
if((_fb1==false)&&(item.options.expirationSliding)){
var _fb2=item.lastAccessed+(item.options.expirationSliding*1000);
if(_fb2<now){
_fb1=true;
}
}
return _fb1;
},_timerPurge:function(){
if(this._dataColection.count>0){
this._purge();
}
this._timerID=setTimeout(dojo.hitch(this,function(){
this._timerPurgeSecond();
}),this.activePurgeFrequency);
},_timerPurgeSecond:function(){
if(this._dataColection.count>0){
this._purge();
}
this._timerID=setTimeout(dojo.hitch(this,function(){
this._timerPurge();
}),this.activePurgeFrequency);
},generateCacheOptions:function(_fb3,_fb4,_fb5){
var _fb6=new Object();
if(_fb3){
_fb6.expirationSliding=_fb3;
}
if(_fb4){
_fb6.expirationAbsolute=_fb4;
}
if(_fb5){
_fb6.priority=_fb5;
}
return _fb6;
},destroy:function(){
try{
this.clear();
delete this._dataColection;
this.inherited(arguments);
}
catch(err){
console.error(err);
}
}});
});
},"dojo/request":function(){
define(["./request/default!"],function(_fb7){
return _fb7;
});
},"dijit/_DialogMixin":function(){
define(["dojo/_base/declare","./a11y"],function(_fb8,a11y){
return _fb8("dijit._DialogMixin",null,{actionBarTemplate:"",execute:function(){
},onCancel:function(){
},onExecute:function(){
},_onSubmit:function(){
this.onExecute();
this.execute(this.get("value"));
},_getFocusItems:function(){
var _fb9=a11y._getTabNavigable(this.domNode);
this._firstFocusItem=_fb9.lowest||_fb9.first||this.closeButtonNode||this.domNode;
this._lastFocusItem=_fb9.last||_fb9.highest||this._firstFocusItem;
}});
});
},"curam/util/ui/AppExitConfirmation":function(){
define("curam/util/ui/AppExitConfirmation",["dojo/on","dojo/keys","dojo/query","dojo/dom-attr","dojo/window","dojo/sniff","dojo/topic","dojo/dom-class","curam/debug","curam/util","dojo/NodeList-traverse"],function(on,keys,_fba,_fbb,win,has,_fbc,_fbd,_fbe){
var _fbf=_AEX=dojo.setObject("curam.util.ui.AppExitConfirmation",{_OPEN_BRACKET_KEY_CODE:219,_W_KEY_CODE:87,_MILLISECONDS_TIME:3000,handlers:[],tracker:null,cMessage:_fbe.getProperty("curam.util.ui.AppExitConfirmation.mesg"),install:function(){
_AEX.tlw.require(["curam/util/ui/AppExitTracker"],function(tr){
_AEX.tracker=tr;
tr.resetPageTracker();
});
_AEX._addEventsToAppExitConfimation();
},uninstall:function(){
_AEX.handlers&&_AEX.handlers.forEach(function(hh){
hh.remove();
});
_AEX.handlers=[];
},_addEventsToAppExitConfimation:function(){
_AEX.handlers.push(on(window,"beforeunload",_AEX._beforeUnloadHandler),on(window,"keydown",_AEX._onKeyDown),on(window.document.body,"click",function(e){
var hr=(e||window.event).target.href;
var _fc0=(e||window.event).target.nodeName;
var _fc1=_AEX._isClickOnAction(e);
_AEX.tracker.hasClickedOnWindow=(hr&&/^mailto\:|directLink/.test(hr))||_fc1;
}));
var uh=on(window,"unload",function(){
_AEX.uninstall();
uh.remove();
});
},_showAppExitConfirmationDialog:function(_fc2){
var e=_fc2||window.event;
e.returnValue=_AEX.cMessage;
e.preventDefault();
if(has("ie")||has("trident")||has("edge")){
_AEX._deferNextConfirmation();
}
return _AEX.cMessage;
},_beforeUnloadHandler:function(e){
if(_AEX.tracker.shouldDisplayAppExitConfirmationDialog()){
if(!_AEX.tracker.hasClickedOnWindow){
return _AEX._showAppExitConfirmationDialog(e);
}
_AEX.tracker.hasClickedOnWindow=false;
}
},_deferNextConfirmation:function(){
_AEX.tracker.setStopBrowserConfirmationDialog(true);
var tt=setTimeout(function(){
clearTimeout(tt);
_AEX.tracker.setStopBrowserConfirmationDialog(false);
},_AEX._MILLISECONDS_TIME);
},_onKeyDown:function(e){
var e=e||window.event;
var code=e.keyCode||e.which,_fc3=e.metaKey||e.ctrlKey||e.altKey||e.shiftKey;
if(code==keys.ENTER){
_AEX.tracker.isPointerOnWindow=true;
}else{
if(code===keys.BACKSPACE||((code==_AEX._OPEN_BRACKET_KEY_CODE||code==keys.LEFT_ARROW||code==_AEX._W_KEY_CODE)&&_fc3)){
var el=e.target.nodeName.toLowerCase(),_fc4=/input|textarea/.test(el);
if(!_fc4||_fbb.get(_fba(e.target),"readonly")){
_AEX.tracker.isPointerOnWindow=false;
}
}
}
},_isClickOnAction:function(e){
var _fc5=e.target;
switch(_fc5.tagName){
case "INPUT":
if((_fbb.get(_fc5,"type")=="submit"&&typeof _fc5.form!="undefined")||_fbb.get(_fc5,"type")=="button"){
return true;
}
case "IMG":
case "SPAN":
case "DIV":
_fc5=_fba(_fc5).closest("A")[0];
if(_fc5==null){
return false;
}else{
return true;
}
case "A":
case "BUTTON":
return true;
break;
default:
return false;
}
var href=_fc5.getAttribute("href");
if(href==""){
return true;
}
if(!href){
return false;
}
if(href.indexOf("javascript")==0){
return true;
}
if(href&&href.indexOf("/servlet/FileDownload?")>-1){
return true;
}
if(_fbd.contains(_fc5,"external-link")){
return true;
}
return false;
}});
_AEX.tlw=curam.util.getTopmostWindow()||window.top;
return _fbf;
});
},"dijit/form/_FormValueWidget":function(){
define(["dojo/_base/declare","dojo/sniff","./_FormWidget","./_FormValueMixin"],function(_fc6,has,_fc7,_fc8){
return _fc6("dijit.form._FormValueWidget",[_fc7,_fc8],{_layoutHackIE7:function(){
if(has("ie")==7){
var _fc9=this.domNode;
var _fca=_fc9.parentNode;
var _fcb=_fc9.firstChild||_fc9;
var _fcc=_fcb.style.filter;
var _fcd=this;
while(_fca&&_fca.clientHeight==0){
(function ping(){
var _fce=_fcd.connect(_fca,"onscroll",function(){
_fcd.disconnect(_fce);
_fcb.style.filter=(new Date()).getMilliseconds();
_fcd.defer(function(){
_fcb.style.filter=_fcc;
});
});
})();
_fca=_fca.parentNode;
}
}
}});
});
},"curam/widget/menu/MegaMenu":function(){
define(["dojo/_base/declare","idx/widget/Menu"],function(_fcf,_fd0){
return _fcf("curam.widget.menu.MegaMenu",[_fd0],{_onRightArrow:function(){
this.focusNext();
},_onLeftArrow:function(){
this.focusPrev();
},});
});
},"url:dijit/templates/Menu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable\" role=\"menu\" tabIndex=\"${tabIndex}\"\n\t   cellspacing=\"0\">\n\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"containerNode\"></tbody>\n</table>\n","url:dojox/layout/resources/ExpandoPane.html":"<div class=\"dojoxExpandoPane\">\n\t<div dojoAttachPoint=\"titleWrapper\" class=\"dojoxExpandoTitle\">\n\t\t<div class=\"dojoxExpandoIcon\" dojoAttachPoint=\"iconNode\" dojoAttachEvent=\"ondijitclick:toggle\"><span class=\"a11yNode\">X</span></div>\n\t\t<span class=\"dojoxExpandoTitleNode\" dojoAttachPoint=\"titleNode\">${title}</span>\n\t</div>\n\t<div class=\"dojoxExpandoWrapper\" dojoAttachPoint=\"cwrapper\" dojoAttachEvent=\"ondblclick:_trap\">\n\t\t<div class=\"dojoxExpandoContent\" dojoAttachPoint=\"containerNode\"></div>\n\t</div>\n</div>\n","url:dijit/templates/MenuBar.html":"<div class=\"dijitMenuBar dijitMenuPassive\" data-dojo-attach-point=\"containerNode\" role=\"menubar\" tabIndex=\"${tabIndex}\"\n\t ></div>\n","url:idx/widget/templates/HoverHelpTooltip.html":"<div class=\"idxOneuiHoverHelpTooltip idxOneuiHoverHelpTooltipLeft\" role=\"alert\"\r\n\t><div\r\n\t\t><span data-dojo-attach-point=\"closeButtonNode\" class=\"idxOneuiHoverHelpTooltipCloseIcon\" \r\n\t\t\tdata-dojo-attach-event=\"ondijitclick: hideOnClickClose\" role=\"button\" aria-label=\"${buttonClose}\"\r\n\t\t\t><span class=\"idxHoverHelpCloseText\">x</span\r\n\t\t></span\r\n\t></div\r\n\t><div data-dojo-attach-point=\"outerContainerNode\" class=\"idxOneuiHoverHelpTooltipContainer idxOneuiHoverHelpTooltipContents\"\r\n\t\t><div data-dojo-attach-point=\"containerNode\" role=\"presentation\"></div\r\n\t\t><a target=\"_blank\" href=\"#updateme\" role=\"link\" class=\"idxOneuiHoverHelpTooltipLearnLink\" data-dojo-attach-point=\"learnMoreNode\"><span>${learnMoreLabel}</span></a\r\n\t></div\r\n\t><div class=\"idxOneuiHoverHelpTooltipConnector\" data-dojo-attach-point=\"connectorNode\"></div\r\n></div>","url:curam/app/templates/ExternalApplication.html":"<div class=\"app-container\">\n  <div class=\"app-container-bc\" \n    data-dojo-type=\"dijit/layout/BorderContainer\"\n    data-dojo-props=\"gutters:false\"\n    data-dojo-attach-point=\"_borderContainer\">\n    <div class=\"app-banner\" aria-label=\"app-banner\"\n      data-dojo-type=\"dojox/layout/ContentPane\"\n      data-dojo-props=\"region: 'top'\"\n      data-dojo-attach-point=\"_appBanner\" role=\"banner\">\n    </div>\n    <div id=\"app-nav\" aria-label=\"app-nav\"\n      data-dojo-type=\"curam/widget/menu/MenuPane\"\n      data-dojo-props=\"region: 'leading', startExpanded: false\"\n      data-dojo-attach-point=\"_appNav\"\n      class=\"leftNavMenu\">\n    </div>\n\t\t<div id=\"app-content\" aria-label=\"app-content\"\n\t\t\tdata-dojo-type=\"curam/widget/containers/TransitionContainer\"\n\t\t\tdata-dojo-attach-point=\"_appContentBody\" class=\"mainBody\"\n\t\t\tdata-dojo-props='region:\"center\", style : {padding : 0, border : 0}' role=\"main\">\n\t\t</div>\n  </div>\n</div>\n","url:dijit/templates/MenuSeparator.html":"<tr class=\"dijitMenuSeparator\" role=\"separator\">\n\t<td class=\"dijitMenuSeparatorIconCell\">\n\t\t<div class=\"dijitMenuSeparatorTop\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n\t<td colspan=\"3\" class=\"dijitMenuSeparatorLabelCell\">\n\t\t<div class=\"dijitMenuSeparatorTop dijitMenuSeparatorLabel\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n</tr>\n","url:idx/widget/templates/_MenuColumn.html":"<td class=\"dijitReset oneuiMenuColumn\" data-dojo-attach-point=\"columnNodes\">\r\n\t<table class=\"dijitReset\" cellspacing=\"0\" width=\"100%\" role=\"presentation\">\r\n\t\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"_containerNodes\">\r\n<!-- this must be kept in synch with column 0 included in Menu.html -->\r\n\t\t</tbody>\r\n\t</table>\r\n</td>","url:dijit/form/templates/DropDownButton.html":"<span class=\"dijit dijitReset dijitInline\"\n\t><span class='dijitReset dijitInline dijitButtonNode'\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" data-dojo-attach-point=\"_buttonNode\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"focusNode,titleNode,_arrowWrapperNode,_popupStateNode\"\n\t\t\trole=\"button\" aria-haspopup=\"true\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\"\n\t\t\t\tdata-dojo-attach-point=\"iconNode\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonInner\"></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonChar\">&#9660;</span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" tabIndex=\"-1\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\" data-dojo-attach-point=\"valueNode\" aria-hidden=\"true\"\n/></span>\n","url:dijit/templates/CheckedMenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"${role}\" tabIndex=\"-1\" aria-checked=\"${checked}\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span class=\"dijitInline dijitIcon dijitMenuItemIcon dijitCheckedMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t\t<span class=\"dijitMenuItemIconChar dijitCheckedMenuItemIconChar\">${!checkedChar}</span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,labelNode,textDirNode\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">&#160;</td>\n</tr>\n","url:dijit/templates/Tooltip.html":"<div class=\"dijitTooltip dijitTooltipLeft\" id=\"dojoTooltip\" data-dojo-attach-event=\"mouseenter:onMouseEnter,mouseleave:onMouseLeave\"\n\t><div class=\"dijitTooltipConnector\" data-dojo-attach-point=\"connectorNode\"></div\n\t><div class=\"dijitTooltipContainer dijitTooltipContents\" data-dojo-attach-point=\"containerNode\" role='alert'></div\n></div>\n","url:dijit/form/templates/ComboButton.html":"<table class=\"dijit dijitReset dijitInline dijitLeft\"\n\tcellspacing='0' cellpadding='0' role=\"presentation\"\n\t><tbody role=\"presentation\"><tr role=\"presentation\"\n\t\t><td class=\"dijitReset dijitStretch dijitButtonNode\" data-dojo-attach-point=\"buttonNode\" data-dojo-attach-event=\"ondijitclick:__onClick,onkeydown:_onButtonKeyDown\"\n\t\t><div id=\"${id}_button\" class=\"dijitReset dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"titleNode\"\n\t\t\trole=\"button\" aria-labelledby=\"${id}_label\"\n\t\t\t><div class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\" role=\"presentation\"></div\n\t\t\t><div class=\"dijitReset dijitInline dijitButtonText\" id=\"${id}_label\" data-dojo-attach-point=\"containerNode\" role=\"presentation\"></div\n\t\t></div\n\t\t></td\n\t\t><td id=\"${id}_arrow\" class='dijitReset dijitRight dijitButtonNode dijitArrowButton'\n\t\t\tdata-dojo-attach-point=\"_popupStateNode,focusNode,_buttonNode\"\n\t\t\tdata-dojo-attach-event=\"onkeydown:_onArrowKeyDown\"\n\t\t\ttitle=\"${optionsTitle}\"\n\t\t\trole=\"button\" aria-haspopup=\"true\"\n\t\t\t><div class=\"dijitReset dijitArrowButtonInner\" role=\"presentation\"></div\n\t\t\t><div class=\"dijitReset dijitArrowButtonChar\" role=\"presentation\">&#9660;</div\n\t\t></td\n\t\t><td style=\"display:none !important;\"\n\t\t\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" data-dojo-attach-point=\"valueNode\"\n\t\t\t\tclass=\"dijitOffScreen\" aria-hidden=\"true\" data-dojo-attach-event=\"onclick:_onClick\"\n\t\t/></td></tr></tbody\n></table>\n","url:curam/widget/templates/OptimalBrowserMessage.html":"<div>\n  <div class=\"hidden\"\n       data-dojo-type=\"dojox/layout/ContentPane\"\n       data-dojo-attach-point=\"_optimalMessage\">\n  </div>\n</div>\n","url:idx/widget/templates/HoverCard.html":"<div class=\"idxOneuiHoverCard idxOneuiHoverCardLeft dijitHidden\"\r\n\t><div\r\n\t\t><span data-dojo-attach-point=\"closeButtonNode, focusNode\" class=\"idxOneuiHoverCardCloseIcon\" \r\n\t\t\tdata-dojo-attach-event=\"ondijitclick: hide\" role=\"button\" tabIndex=\"0\" aria-label=\"${buttonClose}\"\r\n\t\t\t><span class=\"closeText\">x</span\r\n\t\t></span\r\n\t></div\r\n\t><div data-dojo-attach-point=\"bodyNode\" class=\"idxOneuiHoverCardBody\"\r\n\t\t><div class=\"idxOneuiHoverCardGrip\" data-dojo-attach-point=\"gripNode\"></div\r\n\t\t><div class=\"idxOneuiHoverCardContainer\" data-dojo-attach-point=\"containerNode\"></div\r\n\t\t><div class=\"idxOneuiHoverCardFooter\"\r\n\t\t\t><div class=\"idxOneuiHoverCardActionIcons\" data-dojo-attach-point=\"actionIcons\"></div\r\n\t\t\t><span aria-haspopup=\"true\" data-dojo-attach-point=\"moreActionsNode\"></span\r\n\t\t></div\r\n\t\t><div class=\"idxOneuiHoverCardFooterExpand\"></div\r\n\t></div\r\n\t><div class=\"idxOneuiHoverCardConnector\" data-dojo-attach-point=\"connectorNode\"></div\r\n></div>\r\n\r\n\r\n","url:dijit/templates/MenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,textDirNode\"\n\t\trole=\"presentation\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">\n\t\t<span data-dojo-attach-point=\"arrowWrapper\" style=\"visibility: hidden\">\n\t\t\t<span class=\"dijitInline dijitIcon dijitMenuExpand\"></span>\n\t\t\t<span class=\"dijitMenuExpandA11y\">+</span>\n\t\t</span>\n\t</td>\n</tr>\n","url:dijit/templates/MenuBarItem.html":"<div class=\"dijitReset dijitInline dijitMenuItem dijitMenuItemLabel\" data-dojo-attach-point=\"focusNode\"\n\t \trole=\"menuitem\" tabIndex=\"-1\">\n\t<span data-dojo-attach-point=\"containerNode,textDirNode\"></span>\n</div>\n","url:idx/widget/templates/MenuDialog.html":"<div role=\"presentation\">\r\n\t<div class=\"dijitTooltipContainer\" role=\"presentation\">\r\n\t\t<div class =\"dijitTooltipContents dijitTooltipFocusNode\" data-dojo-attach-point=\"containerNode\" role=\"presentation\" tabIndex=\"-1\"></div>\r\n\t</div>\r\n\t<div class=\"dijitTooltipConnector idxConnector\" role=\"presentation\" data-dojo-attach-point=\"connectorNode\"\r\n\t\t><span role=\"presentation\" class=\"idxConnectorBelow\">▲</span\r\n\t\t><span role=\"presentation\" class=\"idxConnectorAbove\">▼</span\r\n\t\t><span role=\"presentation\" class=\"idxConnectorLeft\">▶</span\r\n\t\t><span role=\"presentation\" class=\"idxConnectorRight\">◀</span\r\n\t></div>\r\n</div>\r\n","url:dijit/form/templates/Button.html":"<span class=\"dijit dijitReset dijitInline\" role=\"presentation\"\n\t><span class=\"dijitReset dijitInline dijitButtonNode\"\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" role=\"presentation\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"titleNode,focusNode\"\n\t\t\trole=\"button\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span\n\t\t\t><span class=\"dijitReset dijitToggleButtonIconChar\">&#x25CF;</span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode\"\n\t\t\t></span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\"\n\t\ttabIndex=\"-1\" aria-hidden=\"true\" data-dojo-attach-point=\"valueNode\"\n/></span>\n","url:idx/widget/templates/MenuHeading.html":"<tr class=\"dijitReset oneuiMenuHeading\" role=\"presentation\" tabindex=\"-1\">\r\n\t<td class=\"dijitReset oneuiMenuHeadingLabel\" colspan=\"4\" data-dojo-attach-point=\"containerNode\"></td>\r\n</tr>","url:idx/widget/templates/Menu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable\" role=\"menu\" tabIndex=\"${tabIndex}\" data-dojo-attach-event=\"onkeypress:_onKeyPress\" cellspacing=\"0\">\r\n\t<tbody class=\"dijitReset\">\r\n\t\t<tr data-dojo-attach-point=\"_columnContainerNode\">\r\n\t\t\t<td class=\"dijitReset oneuiMenuColumn\" data-dojo-attach-point=\"columnNodes\">\r\n\t\t\t\t<table class=\"dijitReset\" cellspacing=\"0\" width=\"100%\" role=\"presentation\">\r\n\t\t\t\t\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"_containerNodes,containerNode\">\r\n<!-- this is column 0, which also starts out as the container node so menu items are initially loaded here.\r\n     containerNode changes to point to _columnContainerNode once the widget has initialised, so the whole set of columns is the container.\r\n\t this must be kept in synch with _MenuColumn.html -->\r\n\t\t\t\t\t</tbody>\r\n\t\t\t\t</table>\r\n\t\t\t</td>\r\n\t\t</tr>\r\n\t</tbody>\r\n</table>\r\n","url:dijit/templates/TooltipDialog.html":"<div role=\"alertdialog\" tabIndex=\"-1\">\n\t<div class=\"dijitTooltipContainer\" role=\"presentation\">\n\t\t<div data-dojo-attach-point=\"contentsNode\" class=\"dijitTooltipContents dijitTooltipFocusNode\">\n\t\t\t<div data-dojo-attach-point=\"containerNode\"></div>\n\t\t\t${!actionBarTemplate}\n\t\t</div>\n\t</div>\n\t<div class=\"dijitTooltipConnector\" role=\"presentation\" data-dojo-attach-point=\"connectorNode\"></div>\n</div>\n","*now":function(r){
r(["dojo/i18n!*preload*dojo/nls/curam-ext-app*[\"ar\",\"ca\",\"cs\",\"da\",\"de\",\"el\",\"en-gb\",\"en-us\",\"es-es\",\"fi-fi\",\"fr-fr\",\"he-il\",\"hu\",\"it-it\",\"ja-jp\",\"ko-kr\",\"nl-nl\",\"nb\",\"pl\",\"pt-br\",\"pt-pt\",\"ru\",\"sk\",\"sl\",\"sv\",\"th\",\"tr\",\"zh-tw\",\"zh-cn\",\"ROOT\"]"]);
}}});
define("dojo/curam-ext-app",[],1);
