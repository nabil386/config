//>>built
require({cache:{"url:curam/app/templates/ExternalApplication.html":"<div class=\"app-container\">\n  <div class=\"app-container-bc\" \n    data-dojo-type=\"dijit/layout/BorderContainer\"\n    data-dojo-props=\"gutters:false\"\n    data-dojo-attach-point=\"_borderContainer\">\n    <div class=\"app-banner\" aria-label=\"app-banner\"\n      data-dojo-type=\"dojox/layout/ContentPane\"\n      data-dojo-props=\"region: 'top'\"\n      data-dojo-attach-point=\"_appBanner\" role=\"banner\">\n    </div>\n    <div id=\"app-nav\" aria-label=\"app-nav\"\n      data-dojo-type=\"curam/widget/menu/MenuPane\"\n      data-dojo-props=\"region: 'leading', startExpanded: false\"\n      data-dojo-attach-point=\"_appNav\"\n      class=\"leftNavMenu\">\n    </div>\n\t\t<div id=\"app-content\" aria-label=\"app-content\"\n\t\t\tdata-dojo-type=\"curam/widget/containers/TransitionContainer\"\n\t\t\tdata-dojo-attach-point=\"_appContentBody\" class=\"mainBody\"\n\t\t\tdata-dojo-props='region:\"center\", style : {padding : 0, border : 0}' role=\"main\">\n\t\t</div>\n  </div>\n</div>\n"}});
define("curam/app/ExternalApplication",["dojo/_base/declare","dojo/_base/lang","dojo/_base/window","dojo/aspect","dojo/dom-attr","dojo/query","dijit/_Widget","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/form/Button","dojo/text!./templates/ExternalApplication.html","curam/util/UIMFragment","dojo/dom","dojo/dom-class","dojo/dom-style","curam/ui/ClientDataAccessor","curam/widget/containers/TransitionContainer","dojo/on","curam/widget/menu/MenuPane","dijit/CheckedMenuItem","dojo/fx","dijit/focus","dojo/dom-geometry","idx/widget/MenuBar","idx/widget/Menu","idx/app/Header","idx/widget/MenuDialog","idx/widget/MenuHeading","idx/widget/HoverHelpTooltip","dijit/PopupMenuBarItem","dijit/MenuItem","dijit/form/ComboButton","curam/widget/menu/BannerMenuItem","curam/widget/menu/MegaMenu","curam/util/SessionTimeout","curam/util/ui/AppExitConfirmation"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13,_14,_15,_16,fx,_17,_18){
return _1("curam.app.ExternalApplication",[_7,_8,_9],{started:false,templateString:_d,widgetsInTemplate:true,baseClass:"curamApp",_appConfig:null,_initializedNavBarID:null,guardAgainstLeaving:null,directLinkData:null,postMixInProperties:function(){
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this._init();
this._setupUserLeavingGuard();
},_isNavBarItem:function(_19){
return (this._appConfig.map[_19]!=null);
},_init:function(){
var da=new _12();
da.getRaw("/config/tablayout/extapp["+curam.config.appID+"]",_2.hitch(this,function(_1a){
console.log("External App config data:"+_1a);
this._appConfig=_1a;
this._postDataLoadInit();
}),function(_1b,_1c){
console.log("External App config data load error:"+_1b);
},null);
_4.before(dijit.popup,"open",this._evenOutMenuRows,true);
_4.after(dijit.popup,"open",_2.hitch(this,"_animateMenuOpen"),true);
_4.after(dijit.popup,"close",_2.hitch(this,"_animateMenuClose"),true);
this._bannerResizeTimer=null;
_14(window,"resize",dojo.hitch(this,function(){
if(this._bannerResizeTimer){
clearTimeout(this._bannerResizeTimer);
}
this._bannerResizeTimer=setTimeout(this._handleBannerResize,100);
}));
},_setupUserLeavingGuard:function(){
if(this.guardAgainstLeaving){
curam.util.ui.AppExitConfirmation.install();
}
},_loadLandingPage:function(_1d){
if(curam.config.landingPage){
this._displayOnlyBodyContent({pageID:curam.config.landingPage});
}else{
throw "ERROR: Landing page not set correctly: "+curam.config.landingPage;
}
},_loadBanner:function(){
_e.get({url:"CDEJ/extapp/application-banner-fragment.jspx",targetID:this._appBanner.id,onLoad:_2.hitch(this,this._initializeBannerLandingPageLink)});
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
},_initializeBannerLandingPageLink:function(_1e){
var _1f=_6(".idxHeaderPrimaryTitle",_1e.domNode)[0];
if(!_1f){
throw "Landing Page link not initialized, title node cannot be found";
}
var _20=_2.hitch(this,"_loadLandingPage");
dojo.connect(_1f,"onclick",function(){
_20();
});
},_animateMenuOpen:function(_21){
var _22=_f.byId(_21.popup._popupWrapper.id);
if(_5.get(_22,"dijitpopupparent")!=="appMegaMenu"&!_10.contains(_22,"oneuiHeaderGlobalActionsMenu_help")){
return;
}
if(!_22.aniTable){
_22.aniTable=dojo.query("table",_22)[0];
}
if(!_22.inAnimation){
_22.inAnimation=false;
}
if(!_22.isShown){
_22.isShown=false;
}
if(_22.inAnimation){
if(dijit.byId("appHelpMenu")===_21.popup){
if(_21.popup.shouldNotClose){
_21.popup.shouldNotClose=false;
_21.popup.cancelClose=true;
return;
}
}
_22.fx&&_22.fx.stop();
_22.inAnimation=false;
_22.isShown=false;
_11.set(_22,"display","none");
}
if(!_22.isShown&&!_22.inAnimation){
dojo.style(_22,"display","none");
dojo.style(_22.aniTable,"opacity","0");
}
var _23=function(){
_22.inAnimation=true;
_22.isShown=false;
if(dijit.byId("appHelpMenu")===_21.popup){
_21.popup.shouldNotClose=_21.popup.shouldNotClose?false:true;
_21.popup.justClosed=false;
}
};
var _24=function(){
dojo.style(_22.aniTable,"opacity","1");
_22.inAnimation=false;
_22.isShown=true;
var _25=dojo.marginBox(_22).h;
var _26=dojo.window.getBox().h-65-20;
if(_26<_25){
_11.set(_22,"height",_26+"px");
_11.set(_22,"border-bottom","1px solid black");
}
};
this._animateMenu(_22,_22.aniTable,"open",_23,_24);
},_animateMenuClose:function(_27){
var _28=_f.byId(_27._popupWrapper.id);
if(_5.get(_28,"dijitpopupparent")!=="appMegaMenu"&!_10.contains(_28,"oneuiHeaderGlobalActionsMenu_help")){
return;
}
if(dijit.byId("appHelpMenu")===_27){
if(_27.cancelClose){
_27.cancelClose=false;
return;
}else{
if(_27.justClosed){
_27.justClosed=false;
return;
}
}
}
if(!_28.aniTable){
_28.aniTable=dojo.query("table",_28)[0];
}
if(_28.inAnimation){
_28.fx&&_28.fx.stop();
_28.inAnimation=false;
_28.isShown=false;
dojo.style(_28.aniTable,"opacity","1");
}
_11.set(_28,"display","block");
var _29=function(){
_28.inAnimation=true;
_28.isShown=true;
};
var _2a=function(){
_28.inAnimation=false;
_28.isShown=false;
_11.set(_28,"display","none");
_11.set(_28,"border-bottom","");
if(dijit.byId("appHelpMenu")===_27){
_27.shouldNotClose=false;
_27.justClosed=true;
}
};
this._animateMenu(_28,_28.aniTable,"close",_29,_2a);
},_animateMenu:function(_2b,_2c,_2d,_2e,_2f){
var _30=300;
var _31=[];
var _32=fx.wipeIn({node:_2b,duration:_30});
var _33=dojo.fadeIn({node:_2c,duration:_30});
var _34=fx.wipeOut({node:_2b,duration:_30});
var _35=dojo.fadeOut({node:_2c,duration:_30});
if(_2d==="open"){
_31.push(_32);
_31.push(_33);
}else{
_31.push(_35);
_31.push(_34);
}
_2b.fx=fx.chain(_31);
dojo.connect(_2b.fx,"onBegin",_2e);
dojo.connect(_2b.fx,"onEnd",_2f);
_2b.fx.play();
},_evenOutMenuRows:function(_36){
var _37=_f.byId(_36.popup.id);
var _38=_36.parent?_36.parent.id:_36.popup.id;
if(_38!=="appMegaMenu"&_38!=="appHelpMenu"){
return;
}
var _39,_3a;
if(_38==="appMegaMenu"){
_39="MMItemContainerRow";
_3a="MMItemContainer";
}else{
_39="HMItemContainerRow";
_3a="HMItemContainer";
}
var _3b=dojo.query("div."+_3a,_37);
var _3c=_36.popup._popupWrapper?_36.popup._popupWrapper:_37;
_11.set(_3c,"display","block");
var _3d=_3b.length;
var _3e=_3d<6?1:Math.ceil(_3d/3);
var _3f=[];
for(var i=0;i<_3e;i++){
_3f[i]=0;
}
for(var _40=0;_40<_3e;_40++){
dojo.forEach(_3b,function(_41,_42){
_11.set(_41,"height","auto");
if(_40===0&&!_10.contains(_41,"iconSetOUI")){
var _43=_41;
while(!_10.contains(_43,"menuItemClassOUI")){
_43=_43.parentNode;
}
_43=dojo.query("td.dijitMenuItemIconCell",_43)[0];
if(_3e===1){
_11.set(_43,"width","50px");
}else{
_11.set(_43,"width","34px");
}
_10.add(_41,"iconSetOUI");
}
var _44=_11.get(_41,"height");
if(_10.contains(_41,_39+_40)){
if(_44>_3f[_40]){
_3f[_40]=_44;
}
}
});
}
_11.set(_3c,"display","none");
for(var i=0;i<_3e;i++){
dojo.forEach(_3b,function(_45,_46){
if(_10.contains(_45,_39+i)){
_11.set(_45,"height",_3f[i]+"px");
}
});
}
},_handleBannerResize:function(_47){
CuramExternalApp._oneuiBanner=_47||CuramExternalApp._oneuiBanner;
var _48=CuramExternalApp._oneuiBanner;
var _49=_48._helpNode?true:false;
var _4a=_48.userNode?true:false;
var _4b=_48.navigation?true:false;
var _4c=_48.logoExists;
var _4d=_48._settingsNode?true:false;
if(_4d){
if(_5.get(_48._settingsNode,"title")){
_5.set(_48._settingsNode,"title",CuramExternalApp._appConfig.printMenuLabel);
}
if(_5.get(_48._settingsNode,"alt")){
_5.set(_48._settingsNode,"alt",CuramExternalApp._appConfig.printMenuLabel);
}
}
var _4e=_48.isLeftToRight();
if(!_4a&&!_4b){
return;
}
var _4f=dojo.query(".idxHeaderPrimaryTitleContainer",_48._leadingGlobalActionsNode)[0];
var _50=0;
if(_4c){
var _51=dojo.query(".idxHeaderLogoBox",_48._trailingGlobalActionsNode)[0];
_50=_18.getMarginBox(_51).w;
}
var _52=_18.position(_3.body()).w;
var _53=_52-_18.getMarginBox(_4f).w-(_49?_18.getMarginBox(_48._helpNode).w:0)-(_4d?_18.getMarginBox(_48._settingsNode).w:0)-_50;
var _54=_49?_4e?_18.position(_48._helpNode).x:box(_48._mainContainerNode).w+box(_48._helpNode).w-_18.position(_48._helpNode).x:885;
if(_4a){
var _55=_48.userNode;
var _56=_48.userTextNode;
_11.set(_56,"width","");
_11.set(_56,"display","");
_11.set(_56,"bottom","");
var _57=_18.getMarginBox(_55).w;
var _58=_18.getMarginBox(_56).w;
}
if(_4b){
var _59=_48.navigation.domNode;
var _5a=dojo.query("span[id*=text]",_59)[0];
_11.set(_5a,"width","");
_11.set(_5a,"display","");
_11.set(_59,"margin-left","");
_11.set(_59,"vertical-align","");
var _5b=_18.getMarginBox(_59).w;
var _5c=_18.getMarginBox(_5a).w;
}
var _5d=_53-(_4b?(_5b-_5c):0)-(_4a?(_57-_58):0);
var _5e=(_4b?_5c:0)+(_4a?_58:0);
if(_5d-_5e<0){
var _5f=_5d/2;
var _60;
var _61=[];
if(_4b&_4a){
if(_58<_5f){
_60=_5d-_58;
_61.push(_5a);
}else{
if(_5c<_5f){
_60=_5d-_5c;
_61.push(_56);
}else{
_60=_5f;
_61.push(_5a);
_61.push(_56);
}
}
}else{
if(_4b){
_60=_5d;
_61.push(_5a);
}else{
_60=_5d;
_61.push(_56);
}
}
for(var i=0;i<_61.length;i++){
_11.set(_61[i],"width",_60+"px");
_11.set(_61[i],"display","inline-block");
if(_48.userTextNode===_61[i]){
_11.set(_61[i],"bottom","3px");
}else{
_11.set(_61[i],"vertical-align","top");
}
}
}
if(_4b){
var _62=_52-_18.position(_48._trailingGlobalActionsNode).x;
_11.set(_48._leadingGlobalActionsNode,"right",_62+"px");
}
_11.set(_59,_4e?"right":"left",_62+"px");
},_postDataLoadInit:function(){
this._appContentBody._doResourceLookUp=_2.hitch(this,this._doResourceLookUpForMainBody);
this._appNav._onSelectAfter=_2.hitch(this,function(_63){
this._appContentBody.set("displayPanel",_63);
});
this._makeNavBarAccessible();
this._loadBanner();
if(this.directLinkData){
if(this._isNavBarItem(this.directLinkData.pageID)){
this._initNavBar(this.directLinkData.pageID,_2.hitch(this,function(){
this.directLinkData.isDirectLink=true;
this._displayNavMenuAndBodyContent(this.directLinkData);
}));
}else{
this._displayOnlyBodyContent(this.directLinkData);
}
}else{
this._loadLandingPage();
}
},_initNavBar:function(_64,_65){
var _66=this._appConfig.map[_64];
if(typeof (_66)=="undefined"||_66==this._initializedNavBarID){
_65();
return;
}
var da=new _12();
da.getRaw("/config/tablayout/extnav["+curam.config.appID+"]["+_66+"]",_2.hitch(this,function(_67){
console.log("External App config data:"+_67);
this._loadMenuItems(_67.navItems,_67.navBarPixelWidth);
_65();
this._initializedNavBarID=_66;
}),function(_68,_69){
console.log("External App navigation config data load error:"+_68);
},null);
},_makeNavBarAccessible:function(){
var _6a=dojo.query(".idxOneuiHoverCardCloseIcon")[0];
if(_6a){
_5.set(_6a,"tabindex",-1);
_5.set(_6a,"aria-label",this._appConfig.hoverCardCloseButtonLabel);
}
var _6b=dijit.byId("navOverflowButton");
_6b._setLabelAttr(this._appConfig.navOverflowButtonLabel);
},_loadMenuItems:function(_6c,_6d){
var _6e=[];
this._appNav.set("width",_6d);
for(var i=0;i<_6c.length;i++){
var _6f=_6c[i];
var _70={id:_6f.pageID,label:_6f.title,selected:false,iconPath:_6f.iconPath,subPageIds:_6f.subPageIds,iconClass:"whoKnow"};
_6e.push(_70);
}
this._appNav.addMenuItems(_6e);
},megaMenuClick:function(_71){
if(typeof (_71.displayNavBar)=="undefined"){
_71.displayNavBar=false;
}
this.displayContent(_71);
},displayContent:function(_72){
if(_72!=null){
_72.forceRefresh=true;
if(_72.displayNavBar==false){
this._displayOnlyBodyContent(_72);
return;
}
if(_72.displayNavBar==true){
this._displayNavMenuAndBodyContent(_72);
return;
}
if(_72.pageID==curam.config.landingPage){
this._displayOnlyBodyContent(_72);
return;
}
if(this._isNavBarItem(_72.pageID)){
this._displayNavMenuAndBodyContent(_72);
return;
}else{
if(this._appNav._showing){
this._displayNavMenuAndBodyContent(_72);
return;
}else{
this._displayOnlyBodyContent(_72);
return;
}
}
}
},_displayOnlyBodyContent:function(_73){
if(this._appNav._showing){
var _74=this.connect(this._appContentBody,"_panelFadeOutComplete",_2.hitch(this,function(){
_74.remove();
var _75=this.connect(this._appNav,"_onHideComplete",_2.hitch(this,function(){
this._appNav.deselect();
_75.remove();
_73.key=_73.pageID;
this._appContentBody.set("displayPanel",_73);
}));
this._appNav.fadeOut();
}));
this._appContentBody.fadeOutDisplay();
}else{
_73.key=_73.pageID;
this._appContentBody.set("displayPanel",_73);
}
},_displayNavMenuAndBodyContent:function(_76){
_76.key=_76.pageID;
if(_76.param==null){
_76.param=[];
}
_76.exceptionButtonFound=false;
if(this._appNav._showing){
this._appNav.setSelectedButton(_76);
}else{
if(_76.isDirectLink){
var _77=this.connect(this._appNav,"_onShowComplete",_2.hitch(this,function(){
_77.remove();
this._appNav.setSelectedButton(_76);
}));
this._appNav.fadeIn();
}else{
var _78=this.connect(this._appContentBody,"_panelFadeOutComplete",_2.hitch(this,function(){
_78.remove();
var _79=this.connect(this._appNav,"_onShowComplete",_2.hitch(this,function(){
_79.remove();
this._appNav.setSelectedButton(_76);
}));
this._appNav.fadeIn();
}));
this._appContentBody.fadeOutDisplay();
}
}
},_doResourceLookUpForMainBody:function(_7a,_7b,_7c){
var uri;
if(_7a.key){
if(this._isUIMFragment(_7a.key)){
uri=jsL+"/"+_7a.key+"Page.do?"+this._addCDEJParameters();
}else{
uri=jsL+"/UIMIFrameWrapperPage.do?uimPageID="+_7a.key+"&"+this._addCDEJParameters();
}
}else{
if(_7a.url){
uri=_7a.url;
}
}
return uri;
},_addCDEJParameters:function(){
return jsScreenContext.toRequestString();
},updateMainContentIframe:function(_7d){
var _7e=dojo.query("iframe",this.domNode)[0];
if(_7e){
_7e.contentWindow.location.href=_7d;
}
},_isUIMFragment:function(_7f){
return (this._appConfig&&this._appConfig.uimFragRegistry[_7f]!=null);
},_setupUserMenuLinking:function(_80,_81){
dojo.connect(_80,"onclick",_2.partial(function(_82,evt){
var _83=_f.byId("curam-extapp_userMenuArrow");
if(evt.target!=_83){
displayContent(_82);
}
},_81));
dojo.connect(_80,"onkeypress",function(evt){
if(evt.charOrCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
displayContent(_81);
}
});
},_makeUserMenuAccessible:function(_84){
var _85=_84.userNode;
var _86=_84.userTextNode;
_5.set(_85,"tabindex","3");
_5.set(_86,"title",_86.innerText);
var _87=dojo.query(".idxHeaderDropDownArrow",_85)[0];
_5.set(_87,"tabindex","4");
_5.set(_87,"role","button");
_5.set(_87,"title",_86.innerText);
this._handleUserImageHighContrast(_85);
},_handleUserImageHighContrast:function(_88){
var _89=dojo.query(".idxHeaderUserIcon",_88)[1];
if(_89){
var _8a=_3.body();
if(_8a&&_10.contains(_8a,"high-contrast")){
_89.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
_14(_89,"mouseover",function(){
_89.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_roll.png]";
});
_14(_89,"click",function(){
_89.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_home_click.png]";
});
_14(_89,"mouseout",function(){
_89.src="servlet/PathResolver?r=i&p=/config/tablayout/image[banner_hom_normal.png]";
});
}
}
},_makeMegaMenuAccessible:function(_8b){
var _8c=dojo.query("span[id*=text]",_8b.domNode)[0];
_5.set(_8c,"title",_8c.innerText);
},_makeHelpMenuAccessible:function(_8d){
_5.set(_8d,"tabindex","6");
_5.set(_8d,"role","button");
dojo.connect(_8d,"onkeydown",function(evt){
if(evt.keyCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
dijit.byId("appHelpMenu")._scheduleOpen(evt.target);
}
});
},_makePrintMenuAccessible:function(_8e){
var _8f=_8e._settingsNode;
_5.set(_8f,"tabindex","5");
_5.set(_8f,"role","button");
dojo.connect(_8f,"onkeydown",function(evt){
if(evt.keyCode===dojo.keys.ENTER){
dojo.stopEvent(evt);
dijit.byId("appBannerPrintMenu")._scheduleOpen(evt.target);
}
});
},_setupUserMenuHoverCard:function(_90){
var _91=_4.after(idx.app.Header.prototype,"_renderUser",function(){
_10.add(this.userNode,"idxHeaderDropDown");
var _92=dojo.query(".idxHeaderDropDownArrow",this.userNode)[0];
_92.id="curam-extapp_userMenuArrow";
dojo.connect(_92,"onclick",idx.widget.HoverHelpTooltip.defaultPosition=["below"]);
_91.remove();
});
if(dojo.isIE!==7){
_4.before(idx.widget.HoverHelpTooltip,"show",function(){
var _93=_f.byId("curam-extapp_userMenuArrow");
var _94=CuramExternalApp._oneuiBanner.isLeftToRight();
_11.set(_93,_94?{"position":"fixed","top":"30px","right":"21px"}:{"position":"fixed","top":"30px","left":"21px"});
});
_4.after(idx.widget.HoverHelpTooltip,"show",function(){
_11.set(_f.byId("curam-extapp_userMenuArrow"),"position","static");
});
_4.after(_90,"onShow",_2.partial(function(_95){
var _96="idx_widget__MasterHoverHelpTooltip_0";
if(_95.lastIndexOf("_")!=-1){
_96="idx_widget__MasterHoverHelpTooltip_"+_95.slice(_95.lastIndexOf("_")+1);
}
var _97=_f.byId("curam-extapp_userMenuArrow");
var _98=dijit.byId(_96);
_98.focus();
var _99=_98.domNode;
var _9a=_98.connectorNode;
var _9b=(_18.position(_99).x+_18.position(_99).w)-(_18.position(_9a).x+_18.position(_9a).w/2);
var _9c=_18.position(dojo.body()).w-(_18.position(_97).x+_18.position(_97).w/2)-_9b;
_11.set(_99,{"left":"auto","right":_9c+"px"});
var _9d=CuramExternalApp._oneuiBanner._settingsNode?true:false;
var _9e=oneuiBanner.isLeftToRight();
_11.set(_f.byId(_96),_9e?{"left":"auto","right":rightVal+"px"}:{"right":"auto","left":(rightVal+14)+"px"});
},_90.id));
}
},_addHelpMenuCustomClass:function(){
var _9f=dijit.byId("appHelpMenu")._popupWrapper;
if(!_10.contains(_9f,"oneuiHeaderGlobalActionsMenu_help")){
_10.add(_9f,"oneuiHeaderGlobalActionsMenu_help");
}
},displayMegaMenuItemInModal:function(_a0){
console.log(_a0);
},_preventJAWSCrashClick:function(_a1){
var _a2=dojo.query("#"+_a1.id+"_text",_a1)[0];
if(!_a2.isModified){
dojo.query(".wtfoneui",_a2).forEach(function(_a3){
_a3.oldInnerText=_a3.innerText;
if(_10.contains(_a3,"MMtitle")){
_a3.innerText=_a3.innerText.substring(0,229).concat("...");
}else{
var _a4=_a3.previousSibling;
while(!_10.contains(_a4,"MMtitle")){
_a4=_a4.previousSibling;
}
_a4=_a4.innerText.length;
var _a5=Math.min(250-_a4,Math.max(10,_a3.innerText.length-_a4));
_a3.innerText=_a3.innerText.substring(0,_a5).concat("...");
}
});
_a2.isModified=true;
_a2.innerModdedTimer&&clearTimeout(_a2.innerModdedTimer);
_a2.innerModdedTimer=setTimeout(dojo.partial(function(_a6){
if(_a2.isModified){
dojo.query(".wtfoneui",_a2).forEach(function(_a7){
_a7.innerText=_a7.oldInnerText;
});
_a2.isModified=false;
_a2["innerModdedTimer"]=undefined;
try{
delete _a2.innerModdedTimer;
}
catch(e){
}
}
},_a1),2);
}
},_preventJAWSCrashFocus:function(_a8){
var _a9=dojo.query("#"+_a8.id+"_text",_a8)[0];
if(!_a9.isModified){
dojo.query(".wtfoneui",_a9).forEach(function(_aa){
_aa.oldInnerText=_aa.innerText;
if(_10.contains(_aa,"MMtitle")){
_aa.innerText=_aa.innerText.substring(0,229).concat("...");
}else{
var _ab=_aa.previousSibling;
while(!_10.contains(_ab,"MMtitle")){
_ab=_ab.previousSibling;
}
_ab=_ab.innerText.length;
var _ac=Math.min(250-_ab,Math.max(10,_aa.innerText.length-_ab));
_aa.innerText=_aa.innerText.substring(0,_ac).concat("...");
}
});
_a9.isModified=true;
_a9.innerModdedTimer&&clearTimeout(_a9.innerModdedTimer);
_a9.innerModdedTimer=setTimeout(dojo.partial(function(_ad){
if(_a9.isModified){
dojo.query(".wtfoneui",_a9).forEach(function(_ae){
_ae.innerText=_ae.oldInnerText;
});
_a9.isModified=false;
_a9["innerModdedTimer"]=undefined;
try{
delete _a9.innerModdedTimer;
}
catch(e){
}
}
},_a8),2);
}
},_preventJAWSCrashBlur:function(_af){
var _b0=dojo.query("#"+_af.id+"_text",_af)[0];
_b0.innerModdedTimer&&clearTimeout(_b0.innerModdedTimer);
if(_b0.isModified){
dojo.query(".wtfoneui",_b0).forEach(function(_b1){
_b1.innerText=_b1.oldInnerText;
});
_b0.isModified=false;
}
},_skipLinkFocus:function(_b2){
_b2=_b2||"app-content";
var _b3=_f.byId(_b2);
if(_b3){
_b3.focus();
}
},_showHideSkipLink:function(e){
var _b4=_f.byId("skipLink");
if(_b4){
var _b5=_b4.parentNode;
if(e.type=="focus"&&_10.contains(_b5,"hidden")){
_10.remove(_b5,"hidden");
}else{
if(e.type=="blur"&&!_10.contains(_b5,"hidden")){
_10.add(_b5,"hidden");
}
}
}
},print:function(){
var _b6=_6("#app-content iframe.curam-iframe")[0];
console.log("PRINTING IFRAME:"+_b6);
if(_b6){
if(dojo.isIE<11){
console.log("# IE: "+dojo.isIE+", calling iframe.contentWindow.document.execCommand");
_b6.contentWindow.document.execCommand("print",false,null);
}else{
console.log("# Calling iframe.contentWindow.print()");
_b6.contentWindow.print();
}
}else{
console.log("# Calling window.print()");
window.print();
}
}});
});
