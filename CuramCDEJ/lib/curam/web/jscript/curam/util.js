//>>built
define("curam/util",["dojo/dom","dijit/registry","dojo/dom-construct","dojo/ready","dojo/_base/window","dojo/dom-style","dojo/_base/array","dojo/dom-class","dojo/topic","dojo/_base/event","dojo/query","dojo/Deferred","dojo/has","dojo/_base/unload","dojo/dom-geometry","dojo/_base/json","dojo/dom-attr","dojo/_base/lang","dojo/on","dijit/_BidiSupport","curam/define","curam/debug","curam/util/RuntimeContext","curam/util/Constants","dojo/_base/sniff","cm/_base/_dom","curam/util/ResourceBundle","dojo/NodeList-traverse"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,on,_13,_14,_15,_16,_17,_18,_19,_1a){
curam.define.singleton("curam.util",{PREVENT_CACHE_FLAG:"o3pc",INFORMATIONAL_MSGS_STORAGE_ID:"__informationals__",ERROR_MESSAGES_CONTAINER:"error-messages-container",ERROR_MESSAGES_LIST:"error-messages",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",PAGE_ID_PREFIX:"Curam_",msgLocaleSelectorActionPage:"$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",GENERIC_ERROR_MODAL_MAP:{},wrappersMap:[],lastOpenedTabButton:false,tabButtonClicked:false,secureURLsExemptParamName:"suep",secureURLsExemptParamsPrefix:"spm",secureURLsHashTokenParam:"suhtp",setTabButtonClicked:function(_1b){
curam.util.getTopmostWindow().curam.util.tabButtonClicked=_1b;
},getTabButtonClicked:function(){
var _1c=curam.util.getTopmostWindow().curam.util.tabButtonClicked;
curam.util.getTopmostWindow().curam.util.tabButtonClicked=false;
return _1c;
},setLastOpenedTabButton:function(_1d){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=_1d;
},getLastOpenedTabButton:function(){
var _1e=curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=false;
return _1e;
},insertCssText:function(_1f,_20){
var id=_20?_20:"_runtime_stylesheet_";
var _21=_1.byId(id);
var _22;
if(_21){
if(_21.styleSheet){
_1f=_21.styleSheet.cssText+_1f;
_22=_21;
_22.setAttribute("id","_nodeToRm");
}else{
_21.appendChild(document.createTextNode(_1f));
return;
}
}
var pa=document.getElementsByTagName("head")[0];
_21=_3.create("style",{type:"text/css",id:id});
if(_21.styleSheet){
_21.styleSheet.cssText=_1f;
}else{
_21.appendChild(document.createTextNode(_1f));
}
pa.appendChild(_21);
if(_22){
_22.parentNode.removeChild(_22);
}
},fireRefreshTreeEvent:function(){
if(dojo.global.parent&&dojo.global.parent.amIFrame){
var wpl=dojo.global.parent.loader;
}
if(wpl&&wpl.dojo){
wpl.dojo.publish("refreshTree");
}
},firePageSubmittedEvent:function(_23){
require(["curam/tab"],function(){
var _24=curam.tab.getSelectedTab();
if(_24){
var _25=curam.tab.getTabWidgetId(_24);
var _26=curam.util.getTopmostWindow();
var ctx=(_23=="dialog")?curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG:curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
_26.curam.util.Refresh.getController(_25).pageSubmitted(dojo.global.jsPageID,ctx);
_26.dojo.publish("/curam/main-content/page/submitted",[dojo.global.jsPageID,_25]);
}else{
curam.debug.log("/curam/main-content/page/submitted: "+_15.getProperty("curam.util.no.open"));
}
});
},fireTabOpenedEvent:function(_27){
curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",[dojo.global.jsPageID,_27]);
},setupSubmitEventPublisher:function(){
_4(function(){
var _28=_1.byId("mainForm");
if(_28){
curam.util.connect(_28,"onsubmit",function(){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[curam.util.PAGE_ID_PREFIX+dojo.global.jsPageID]);
curam.util.firePageSubmittedEvent("main-content");
});
}
});
},getScrollbar:function(){
var _29=_3.create("div",{},_5.body());
_6.set(_29,{width:"100px",height:"100px",overflow:"scroll",position:"absolute",top:"-300px",left:"0px"});
var _2a=_3.create("div",{},_29);
_6.set(_2a,{width:"400px",height:"400px"});
var _2b=_29.offsetWidth-_29.clientWidth;
_3.destroy(_29);
return {width:_2b};
},isModalWindow:function(){
return (dojo.global.curamModal===undefined)?false:true;
},isExitingIEGScriptInModalWindow:function(_2c){
return (curam.util.getTopmostWindow().exitingIEGScript===undefined)?false:true;
},setExitingIEGScriptInModalWindowVariable:function(){
curam.util.getTopmostWindow().exitingIEGScript=true;
},getTopmostWindow:function(){
if(typeof (dojo.global._curamTopmostWindow)=="undefined"){
var _2d=dojo.global;
if(_2d.__extAppTopWin){
dojo.global._curamTopmostWindow=_2d;
}else{
while(_2d.parent!=_2d){
_2d=_2d.parent;
if(_2d.__extAppTopWin){
break;
}
}
dojo.global._curamTopmostWindow=_2d;
}
}
if(dojo.global._curamTopmostWindow.location.href.indexOf("AppController.do")<0&&typeof (dojo.global._curamTopmostWindow.__extAppTopWin)=="undefined"){
curam.debug.log(_15.getProperty("curam.util.wrong.window")+dojo.global._curamTopmostWindow.location.href);
}
return dojo.global._curamTopmostWindow;
},getUrlParamValue:function(url,_2e){
var _2f=url.indexOf("?");
if(_2f<0){
return null;
}
var _30=url.substring(_2f+1,url.length);
function _31(_32){
var _33=_30.split(_32);
_2e+="=";
for(var i=0;i<_33.length;i++){
if(_33[i].indexOf(_2e)==0){
return _33[i].split("=")[1];
}
}
};
return _31("&")||_31("");
},addUrlParam:function(_34,_35,_36,_37){
var _38=_34.indexOf("?")>-1;
var _39=_37?_37:"undefined";
if(!_38||(_39==false)){
return _34+(_38?"&":"?")+_35+"="+_36;
}else{
var _3a=_34.split("?");
_34=_3a[0]+"?"+_35+"="+_36+(_3a[1]!=""?("&"+_3a[1]):"");
return _34;
}
},replaceUrlParam:function(_3b,_3c,_3d){
_3b=curam.util.removeUrlParam(_3b,_3c);
return curam.util.addUrlParam(_3b,_3c,_3d);
},removeUrlParam:function(url,_3e,_3f){
var _40=url.indexOf("?");
if(_40<0){
return url;
}
if(url.indexOf(_3e+"=")<0){
return url;
}
var _41=url.substring(_40+1,url.length);
var _42=_41.split("&");
var _43;
var _44,_45;
for(var i=0;i<_42.length;i++){
if(_42[i].indexOf(_3e+"=")==0){
_45=false;
if(_3f){
_44=_42[i].split("=");
if(_44.length>1){
if(_44[1]==_3f){
_45=true;
}
}else{
if(_3f==""){
_45=true;
}
}
}else{
_45=true;
}
if(_45){
_42.splice(i,1);
i--;
}
}
}
return url.substring(0,_40+1)+_42.join("&");
},stripHash:function(url){
var idx=url.indexOf("#");
if(idx<0){
return url;
}
return url.substring(0,url);
},isSameUrl:function(_46,_47,rtc){
if(!_47){
_47=rtc.getHref();
}
if(_46.indexOf("#")==0){
return true;
}
var _48=_46.indexOf("#");
if(_48>-1){
if(_48==0){
return true;
}
var _49=_46.split("#");
var _4a=_47.indexOf("#");
if(_4a>-1){
if(_4a==0){
return true;
}
_47=_47.split("#")[0];
}
return _49[0]==_47;
}
var _4b=function(url){
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
var _4c=curam.util.stripHash(rp(_47,curam.util.Constants.RETURN_PAGE_PARAM));
var _4d=curam.util.stripHash(rp(_46,curam.util.Constants.RETURN_PAGE_PARAM));
var _4e=_4d.split("?");
var _4f=_4c.split("?");
_4f[0]=_4b(_4f[0]);
_4e[0]=_4b(_4e[0]);
var _50=(_4f[0]==_4e[0]||_4f[0].match(_4e[0]+"$")==_4e[0]);
if(!_50){
return false;
}
if(_4f.length==1&&_4e.length==1&&_50){
return true;
}else{
var _51;
var _52;
if(typeof _4f[1]!="undefined"&&_4f[1]!=""){
_51=_4f[1].split("&");
}else{
_51=new Array();
}
if(typeof _4e[1]!="undefined"&&_4e[1]!=""){
_52=_4e[1].split("&");
}else{
_52=new Array();
}
curam.debug.log("curam.util.isSameUrl: paramsHere "+_15.getProperty("curam.util.before")+_51.length);
_51=_7.filter(_51,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_15.getProperty("curam.util.after")+_51.length);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_15.getProperty("curam.util.before")+_52.length);
_52=_7.filter(_52,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_15.getProperty("curam.util.after")+_52.length);
if(_51.length!=_52.length){
return false;
}
var _53={};
var _54;
for(var i=0;i<_51.length;i++){
_54=_51[i].split("=");
_54[0]=decodeURIComponent(_54[0]);
_54[1]=decodeURIComponent(_54[1]);
_53[_54[0]]=_54[1];
}
for(var i=0;i<_52.length;i++){
_54=_52[i].split("=");
_54[0]=decodeURIComponent(_54[0]);
_54[1]=decodeURIComponent(_54[1]);
if(_53[_54[0]]!=_54[1]){
curam.debug.log(_15.getProperty("curam.util.no.match",[_54[0],_54[1],_53[_54[0]]]));
return false;
}
}
}
return true;
},isNotCDEJParam:function(_55){
return !((_55.charAt(0)=="o"&&_55.charAt(1)=="3")||(_55.charAt(0)=="_"&&_55.charAt(1)=="_"&&_55.charAt(2)=="o"&&_55.charAt(3)=="3"));
},setAttributes:function(_56,map){
for(var x in map){
_56.setAttribute(x,map[x]);
}
},invalidatePage:function(){
curam.PAGE_INVALIDATED=true;
var _57=dojo.global.dialogArguments?dojo.global.dialogArguments[0]:opener;
if(_57&&_57!=dojo.global){
try{
_57.curam.util.invalidatePage();
}
catch(e){
curam.debug.log(_15.getProperty("curam.util.error"),e);
}
}
},redirectWindow:function(_58,_59,_5a){
var rtc=new curam.util.RuntimeContext(dojo.global);
var _5b=function(_5c,_5d,_5e,_5f,_60){
curam.util.getFrameRoot(_5c,_5d).curam.util.redirectContentPanel(_5e,_5f,_60);
};
curam.util._doRedirectWindow(_58,_59,_5a,dojo.global.jsScreenContext,rtc,curam.util.publishRefreshEvent,_5b);
},_doRedirectWindow:function(_61,_62,_63,_64,rtc,_65,_66){
if(_61&&curam.util.isActionPage(_61)&&!curam.util.LOCALE_REFRESH){
curam.debug.log(_15.getProperty("curam.util.stopping"),_61);
return;
}
var rpl=curam.util.replaceUrlParam;
var _67=_64.hasContextBits("TREE")||_64.hasContextBits("AGENDA")||_64.hasContextBits("ORG_TREE");
if(curam.util.LOCALE_REFRESH){
curam.util.publishRefreshEvent();
curam.util.getTopmostWindow().location.reload();
return;
}else{
if(curam.util.FORCE_REFRESH){
_61=rpl(rtc.getHref(),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(curam.util.isModalWindow()||_67){
_65();
dojo.global.location.href=_61;
}else{
if(_64.hasContextBits("LIST_ROW_INLINE_PAGE")||_64.hasContextBits("NESTED_UIM")){
curam.util._handleInlinePageRefresh(_61);
}else{
_65();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
require(["curam/tab"],function(){
_66(dojo.global,curam.tab.getTabController().ROOT_OBJ,_61,true,true);
});
}
}
}
return;
}
}
var u=curam.util;
var rtc=new curam.util.RuntimeContext(dojo.global);
if(!_67&&!_62&&!curam.PAGE_INVALIDATED&&u.isSameUrl(_61,null,rtc)){
return;
}
if(curam.util.isModalWindow()||_67){
_61=rpl(rpl(_61,"o3frame","modal"),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
var _68=_3.create("form",{action:_61,method:"POST"});
if(!_67){
if(!_1.byId("o3ctx")){
_68.action=curam.util.removeUrlParam(_68.action,"o3ctx");
var _69=_3.create("input",{type:"hidden",id:"o3ctx",name:"o3ctx",value:_64.getValue()},_68);
}
_5.body().appendChild(_68);
_65();
_68.submit();
}
if(!_63){
if(_67){
curam.util.redirectFrame(_61);
}
}
}else{
var _6a=sessionStorage.getItem("launchWordEdit");
if(!_6a&&(_64.hasContextBits("LIST_ROW_INLINE_PAGE")||_64.hasContextBits("NESTED_UIM"))){
curam.util._handleInlinePageRefresh(_61);
}else{
if(_6a){
sessionStorage.removeItem("launchWordEdit");
}
_65();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
if(_64.hasContextBits("EXTAPP")){
var _6b=window.top;
_6b.dijit.byId("curam-app").updateMainContentIframe(_61);
}else{
require(["curam/tab"],function(){
curam.util.getFrameRoot(dojo.global,curam.tab.getTabController().ROOT_OBJ).curam.util.redirectContentPanel(_61,_62);
});
}
}
}
}
},_handleInlinePageRefresh:function(_6c){
curam.debug.log(_15.getProperty("curam.util.closing.modal"),_6c);
var _6d=new curam.ui.PageRequest(_6c);
require(["curam/tab"],function(){
curam.tab.getTabController().checkPage(_6d,function(_6e){
curam.util.publishRefreshEvent();
window.location.reload(false);
});
});
},redirectContentPanel:function(url,_6f,_70){
require(["curam/tab"],function(){
var _71=curam.tab.getContentPanelIframe();
var _72=url;
if(_71!=null){
var rpu=curam.util.Constants.RETURN_PAGE_PARAM;
var _73=null;
if(url.indexOf(rpu+"=")>=0){
curam.debug.log("curam.util.redirectContentPanel: "+_15.getProperty("curam.util.rpu"));
_73=decodeURIComponent(curam.util.getUrlParamValue(url,rpu));
}
if(_73){
_73=curam.util.removeUrlParam(_73,rpu);
_72=curam.util.replaceUrlParam(url,rpu,encodeURIComponent(_73));
}
}
var _74=new curam.ui.PageRequest(_72);
if(_6f){
_74.forceLoad=true;
}
if(_70){
_74.justRefresh=true;
}
curam.tab.getTabController().handlePageRequest(_74);
});
},redirectFrame:function(_75){
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
var _76=curam.util.getFrameRoot(dojo.global,"wizard").targetframe;
_76.curam.util.publishRefreshEvent();
_76.location.href=_75;
}else{
if(dojo.global.jsScreenContext.hasContextBits("ORG_TREE")){
var _76=curam.util.getFrameRoot(dojo.global,"orgTreeRoot");
_76.curam.util.publishRefreshEvent();
_76.dojo.publish("orgTree.refreshContent",[_75]);
}else{
var _77=curam.util.getFrameRoot(dojo.global,"iegtree");
var _78=_77.navframe||_77.frames[0];
var _79=_77.contentframe||_77.frames["contentframe"];
_79.curam.util.publishRefreshEvent();
if(curam.PAGE_INVALIDATED||_78.curam.PAGE_INVALIDATED){
var _7a=curam.util.modifyUrlContext(_75,"ACTION");
_79.location.href=_7a;
}else{
_79.location.href=_75;
}
}
}
return true;
},publishRefreshEvent:function(){
_9.publish("/curam/page/refresh");
},openGenericErrorModalDialog:function(_7b,_7c,_7d,_7e,_7f){
var _80=curam.util.getTopmostWindow();
_80.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_7b,"titleInfo":_7c,"msg":_7d,"msgPlaceholder":_7e,"errorModal":_7f,"hasCancelButton":false};
var url="generic-modal-error.jspx";
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=true;
curam.util.openModalDialog({href:encodeURI(url)},_7b);
},openGenericErrorModalDialogYesNo:function(_81,_82,_83){
var sc=dojo.global.jsScreenContext;
var _84=curam.util.getTopmostWindow();
sc.addContextBits("MODAL");
_84.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_81,"titleInfo":_82,"msg":_83,"msgPlaceholder":"","errorModal":false,"hasCancelButton":true};
var url="generic-modal-error.jspx?"+sc.toRequestString();
curam.util.openModalDialog({href:encodeURI(url)},_81);
},addPlaceholderFocusClassToEventOrAnchorTag:function(_85,_86){
var _87=curam.util.getTopmostWindow();
_87.curam.util.PLACEHOLDER_WINDOW_LIST=!_87.curam.util.PLACEHOLDER_WINDOW_LIST?[]:_87.curam.util.PLACEHOLDER_WINDOW_LIST;
_8.add(_85,"placeholder-for-focus");
_87.curam.util.PLACEHOLDER_WINDOW_LIST.push(_86);
},returnFocusToPopupActionAnchorElement:function(_88){
var _89=_88.curam.util.PLACEHOLDER_WINDOW_LIST;
if(_89&&_89.length>0){
var _8a=_89.pop();
var _8b=_8a&&_8a.document.activeElement;
var _8c=_8b&&dojo.query(".placeholder-for-focus",_8b);
if(_8c&&_8c.length==1){
_8c[0].focus();
_8.remove(_8c[0],"placeholder-for-focus");
}
_88.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0,_88.curam.util.PLACEHOLDER_WINDOW_LIST.length);
_88.curam.util.PLACEHOLDER_WINDOW_LIST=null;
}
},openModalDialog:function(_8d,_8e,_8f,top,_90){
_8d.event&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_8d.event,_8d.event.ownerDocument.defaultView.window);
var _91;
if(!_8d||!_8d.href){
_8d=_a.fix(_8d);
var _92=_8d.target;
while(_92.tagName!="A"&&_92!=_5.body()){
_92=_92.parentNode;
}
_91=_92.href;
_92._isModal=true;
_a.stop(_8d);
}else{
_91=_8d.href;
_8d._isModal=true;
}
require(["curam/dialog"]);
var _93=curam.dialog.parseWindowOptions(_8e);
curam.util.showModalDialog(_91,_8d,_93["width"],_93["height"],_8f,top,false,null,null,_90);
return true;
},showModalDialog:function(url,_94,_95,_96,_97,top,_98,_99,_9a,_9b){
var _9c=curam.util.getTopmostWindow();
if(dojo.global!=_9c){
curam.debug.log("curam.util.showModalDialog: "+_15.getProperty("curam.util.redirecting.modal"));
_9c.curam.util.showModalDialog(url,_94,_95,_96,_97,top,_98,_99,dojo.global,_9b);
return;
}
var rup=curam.util.replaceUrlParam;
url=rup(url,"o3frame","modal");
url=curam.util.modifyUrlContext(url,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
url=rup(url,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
curam.debug.log(_15.getProperty("curam.util.modal.url"),url);
if(_95){
_95=typeof (_95)=="number"?_95:parseInt(_95);
}
if(_96){
_96=typeof (_96)=="number"?_96:parseInt(_96);
}
if(!curam.util._isModalCurrentlyOpening()){
if(url.indexOf("__o3rpu=about%3Ablank")>=0){
alert(curam_util_showModalDialog_pageStillLoading);
return;
}
curam.util._setModalCurrentlyOpening(true);
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
new curam.ModalDialog({href:url,width:_95,height:_96,openNode:(_94&&_94.target)?_94.target:null,parentWindow:_9a,uimToken:_9b});
}else{
require(["curam/modal/CuramCarbonModal"]);
new curam.modal.CuramCarbonModal({href:url,width:_95,height:_96,openNode:(_94&&_94.target)?_94.target:null,parentWindow:_9a,uimToken:_9b});
}
return true;
}
},showModalDialogWithRef:function(_9d,_9e,_9f){
var _a0=curam.util.getTopmostWindow();
if(dojo.global!=_a0){
return _a0.curam.util.showModalDialogWithRef(_9d,dojo.global);
}
var rup=curam.util.replaceUrlParam;
_9d=curam.util.modifyUrlContext(_9d,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
_9d=rup(_9d,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(!curam.util._isModalCurrentlyOpening()){
curam.util._setModalCurrentlyOpening(true);
var dl;
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
if(_9f){
dl=new curam.ModalDialog({href:_9d,width:_9f.width,height:_9f.height,parentWindow:_9e});
}else{
dl=new curam.ModalDialog({href:_9d,parentWindow:_9e});
}
}else{
require(["curam/modal/CuramCarbonModal"]);
if(_9f){
dl=new curam.modal.CuramCarbonModal({href:_9d,width:_9f.width,height:_9f.height,parentWindow:_9e});
}else{
dl=new curam.modal.CuramCarbonModal({href:_9d,parentWindow:_9e});
}
}
return dl;
}
},_isModalCurrentlyOpening:function(){
return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
},_setModalCurrentlyOpening:function(_a1){
curam.util.getTopmostWindow().curam.util._modalOpenInProgress=_a1;
},setupPreferencesLink:function(_a2){
_4(function(){
var _a3=_b(".user-preferences")[0];
if(_a3){
if(typeof (_a3._disconnectToken)=="undefined"){
_a3._disconnectToken=curam.util.connect(_a3,"onclick",curam.util.openPreferences);
}
if(!_a2){
_a2=dojo.global.location.href;
}
}else{
curam.debug.log(_15.getProperty("curam.util.no.setup"));
}
});
},setupLocaleLink:function(_a4){
_4(function(){
var _a5=_b(".user-locale")[0];
if(_a5){
if(typeof (_a5._disconnectToken)=="undefined"){
_a5._disconnectToken=curam.util.connect(_a5,"onclick",curam.util.openLocaleNew);
}
if(!_a4){
_a4=dojo.global.location.href;
}
}else{
curam.debug.log(_15.getProperty("curam.util.no.setup"));
}
});
},openPreferences:function(_a6){
_a.stop(_a6);
if(_a6.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-prefs-editor.jspx",{dialogOptions:"width=605"});
});
},logout:function(_a7){
var _a8=curam.util.getTopmostWindow();
_a8.dojo.publish("curam/redirect/logout");
window.location.href=jsBaseURL+"/logout.jsp";
},openAbout:function(_a9){
_a.stop(_a9);
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("about.jsp",{dialogOptions:"width=583,height=399"});
});
},addMinWidthCalendarCluster:function(id){
var _aa=_1.byId(id);
var i=0;
function _ab(evt){
_7.forEach(_aa.childNodes,function(_ac){
if(_8.contains(_ac,"cluster")){
_6.set(_ac,"width","97%");
if(_ac.clientWidth<700){
_6.set(_ac,"width","700px");
}
}
});
};
if(_d("ie")>6){
_7.forEach(_aa.childNodes,function(_ad){
if(_8.contains(_ad,"cluster")){
_6.set(_ad,"minWidth","700px");
}
});
}else{
on(dojo.global,"resize",_ab);
_4(_ab);
}
},addPopupFieldListener:function(id){
if(!_d("ie")||_d("ie")>6){
return;
}
if(!curam.util._popupFields){
function _ae(evt){
var _af=0;
var j=0;
var x=0;
var arr=curam.util._popupFields;
_7.forEach(curam.util._popupFields,function(id){
var _b0=_1.byId(id);
_b("> .popup-actions",_b0).forEach(function(_b1){
_af=_b1.clientWidth+30;
});
_b("> .desc",_b0).forEach(function(_b2){
_6.set(_b2,"width",Math.max(0,_b0.clientWidth-_af)+"px");
});
});
};
curam.util._popupFields=[id];
on(dojo.global,"resize",_ae);
_4(_ae);
}else{
curam.util._popupFields.push(id);
}
},addContentWidthListener:function(id){
if(_d("ie")>6){
return;
}
var _b3=_6.set;
var _b4=_8.contains;
function _b5(evt){
var i=0;
var _b6=_1.byId("content");
if(_b6){
var _b7=_b6.clientWidth;
if(_d("ie")==6&&_1.byId("footer")){
var _b8=_5.body().clientHeight-100;
_b3(_b6,"height",_b8+"px");
var _b9=_1.byId("sidebar");
if(_b9){
_b3(_b9,"height",_b8+"px");
}
}
try{
_b("> .page-title-bar",_b6).forEach(function(_ba){
var _bb=_f.getMarginSize(_ba).w-_f.getContentBox(_ba).w;
if(!_d("ie")){
_bb+=1;
}
_b7=_b6.clientWidth-_bb;
_6.set(_ba,"width",_b7+"px");
});
}
catch(e){
}
_b("> .page-description",_b6).style("width",_b7+"px");
_b("> .in-page-navigation",_b6).style("width",_b7+"px");
}
};
curam.util.subscribe("/clusterToggle",_b5);
curam.util.connect(dojo.global,"onresize",_b5);
_4(_b5);
},alterScrollableListBottomBorder:function(id,_bc){
var _bd=_bc;
var _be="#"+id+" table";
function _bf(){
var _c0=_b(_be)[0];
if(_c0.offsetHeight>=_bd){
var _c1=_b(".odd-last-row",_c0)[0];
if(typeof _c1!="undefined"){
_8.add(_c1,"no-bottom-border");
}
}else{
if(_c0.offsetHeight<_bd){
var _c1=_b(".even-last-row",_c0)[0];
if(typeof _c1!="undefined"){
_8.add(_c1,"add-bottom-border");
}
}else{
curam.debug.log("curam.util.alterScrollableListBottomBorder: "+_15.getProperty("curam.util.code"));
}
}
};
_4(_bf);
},addFileUploadResizeListener:function(_c2){
function _c3(evt){
if(_b(".widget")){
_b(".widget").forEach(function(_c4){
var _c5=_c4.clientWidth;
if(_b(".fileUpload",_c4)){
_b(".fileUpload",_c4).forEach(function(_c6){
fileUploadWidth=_c5/30;
if(fileUploadWidth<4){
_c6.size=1;
}else{
_c6.size=fileUploadWidth;
}
});
}
});
}
};
on(dojo.global,"resize",_c3);
_4(_c3);
},openCenteredNonModalWindow:function(url,_c7,_c8,_c9){
_c7=Number(_c7);
_c8=Number(_c8);
var _ca=(screen.width-_c7)/2;
var _cb=(screen.height-_c8)/2;
_c8=_cb<0?screen.height:_c8;
_cb=Math.max(0,_cb);
_c7=_ca<0?screen.width:_c7;
_ca=Math.max(0,_ca);
var _cc="left",top="top";
if(_d("ff")){
_cc="screenX",top="screenY";
}
var _cd="location=no, menubar=no, status=no, toolbar=no, "+"scrollbars=yes, resizable=no";
var _ce=dojo.global.open(url,_c9||"name","width="+_c7+", height="+_c8+", "+_cc+"="+_ca+","+top+"="+_cb+","+_cd);
_ce.resizeTo(_c7,_c8);
_ce.moveTo(_ca,_cb);
_ce.focus();
},adjustTargetContext:function(win,_cf){
if(win&&win.dojo.global.jsScreenContext){
var _d0=win.dojo.global.jsScreenContext;
_d0.updateStates(dojo.global.jsScreenContext);
return curam.util.replaceUrlParam(_cf,"o3ctx",_d0.getValue());
}
return _cf;
},modifyUrlContext:function(url,_d1,_d2){
var _d3=url;
var ctx=new curam.util.ScreenContext();
var _d4=curam.util.getUrlParamValue(url,"o3ctx");
if(_d4){
ctx.setContext(_d4);
}else{
ctx.clear();
}
if(_d1){
ctx.addContextBits(_d1);
}
if(_d2){
ctx.clear(_d2);
}
_d3=curam.util.replaceUrlParam(url,"o3ctx",ctx.getValue());
return _d3;
},updateCtx:function(_d5){
var _d6=curam.util.getUrlParamValue(_d5,"o3ctx");
if(!_d6){
return _d5;
}
return curam.util.modifyUrlContext(_d5,null,"MODAL");
},getFrameRoot:function(_d7,_d8){
var _d9=false;
var _da=_d7;
if(_da){
while(_da!=top&&!_da.rootObject){
_da=_da.parent;
}
if(_da.rootObject){
_d9=(_da.rootObject==_d8);
}
}
return _d9?_da:null;
},saveInformationalMsgs:function(_db){
try{
localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID]=_10.toJson({pageID:_5.body().id,total:_1.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,listItems:_1.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML});
_db();
}
catch(e){
curam.debug.log(_15.getProperty("curam.util.exception"),e);
}
},disableInformationalLoad:function(){
curam.util._informationalsDisabled=true;
},redirectDirectUrl:function(){
_4(function(){
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
_4(function(){
if(dojo.global.jsScreenContext.hasContextBits("CONTEXT_PANEL")){
return;
}
if(curam.util._informationalsDisabled){
return;
}
var _dc=localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID];
if(_dc&&_dc!=""){
_dc=_10.fromJson(_dc);
localStorage.removeItem(curam.util.INFORMATIONAL_MSGS_STORAGE_ID);
var div=_1.byId(curam.util.ERROR_MESSAGES_CONTAINER);
var _dd=_1.byId(curam.util.ERROR_MESSAGES_LIST);
if(_dc.pageID!=_5.body().id){
return;
}
if(_dd){
var _de=_3.create("ul",{innerHTML:_dc.listItems});
var _df=[];
for(var i=0;i<_dd.childNodes.length;i++){
if(_dd.childNodes[i].tagName=="LI"){
_df.push(_dd.childNodes[i]);
}
}
var _e0=false;
var _e1=_de.childNodes;
for(var i=0;i<_e1.length;i++){
_e0=false;
for(var j=0;j<_df.length;j++){
if(_e1[i].innerHTML==_df[j].innerHTML){
_e0=true;
break;
}
}
if(!_e0){
_dd.appendChild(_e1[i]);
i--;
}
}
}else{
if(div){
div.innerHTML=_dc.total;
}
}
}
var _e2=_1.byId("container-messages-ul")?_1.byId("container-messages-ul"):_1.byId("error-messages");
if(_e2&&!dojo.global.jsScreenContext.hasContextBits("MODAL")){
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked){
curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
setTimeout(function(){
_e2.innerHTML=_e2.innerHTML+" ";
},500);
}else{
_e2.focus();
}
}
var _e3=_1.byId("error-messages-container-wrapper");
if(_e3){
var _e4=_b("#container-messages-ul",_e3)[0];
if(_e4){
_e4.focus();
}
}
});
},_setFocusCurrentIframe:function(){
var _e5=/Edg/.test(navigator.userAgent);
if(_e5){
var _e6=window.frameElement;
if(_e6){
_e6.setAttribute("tabindex","0");
_e6.focus();
setTimeout(function(){
_e6.removeAttribute("tabindex");
},10);
}
}
},setFocus:function(){
var _e7;
if(window.document.getElementsByClassName("skeleton").length>0){
_e7=setTimeout(function(){
curam.util.setFocus();
},300);
}else{
if(_e7){
clearTimeout(_e7);
}
var _e8=curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
if(_e8){
return;
}
curam.util._setFocusCurrentIframe();
var _e9=curam.util.getUrlParamValue(dojo.global.location.href,"o3frame")=="modal"||(curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==null&&curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==undefined);
if(!_e9){
_4(function(){
var _ea=_1.byId("container-messages-ul")?_1.byId("container-messages-ul"):_1.byId("error-messages");
var _eb=sessionStorage.getItem("curamDefaultActionId");
var _ec=null;
if(!_ea&&_eb){
sessionStorage.removeItem("curamDefaultActionId");
_ec=dojo.query(".curam-default-action")[0].previousSibling;
}else{
_ec=curam.util.doSetFocus();
}
if(_ec){
curam.util.setFocusOnField(_ec,false);
}else{
window.focus();
}
});
}
}
},setFocusOnField:function(_ed,_ee,_ef){
if(_d("IE")||_d("trident")){
var _f0=1000;
var _f1=_ee?200:500;
curam.util._createHiddenInputField(_ed);
var _f2=function(ff){
return function(){
var _f3=ff.ownerDocument.activeElement;
if(_f3.tagName==="INPUT"&&!_f3.classList.contains("hidden-focus-input")||_f3.tagName==="TEXTAREA"||(_f3.tagName==="SPAN"&&_f3.className=="fileUploadButton")||(_f3.tagName==="A"&&_f3.className=="popup-action")||(_f3.tagName==="IFRAME"&&_f3.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
if(_ee){
var _f4=_11.get(_ed,"aria-label");
var _f5="";
var _f6=_11.get(_ed,"objid");
if(_f6&&_f6.indexOf("component")==0||_8.contains(_ed,"dijitReset dijitInputInner")){
_f5=_ed.title;
}else{
_f5=_ef||"Modal Dialog";
}
if(_ed&&_ed.id!=="container-messages-ul"){
_11.set(_ed,"aria-label",_f5);
}
var _f7=function(_f8){
return function(e){
_b("input|select[aria-label="+_f5+"]").forEach(function(_f9){
_f8&&_11.set(_f9,"aria-label",_f8);
!_f8&&_11.remove(_f9,"aria-label");
});
};
};
on(_ed,"blur",_f7(_f4));
}
if(_ed.tagName==="TEXTAREA"){
setTimeout(_f2(_ed),_f0);
}else{
if(_ed.tagName==="SELECT"||(_ed.tagName==="INPUT"&&_11.get(_ed,"type")==="text")){
setTimeout(_f2(_ed),_f1);
}else{
_ed.focus();
}
}
}else{
_ed.focus();
}
},_createHiddenInputField:function(_fa){
var _fb=_fa.ownerDocument.forms["mainForm"];
if(_fb&&(_fa.tagName==="SELECT"||(_fa.tagName==="INPUT"&&_11.get(_fa,"type")==="text"))){
var _fc=_3.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_3.place(_fc,_fb,"before");
_fc.focus();
on(_fc,"blur",function(){
_3.destroy(_fc);
});
}
},doSetFocus:function(){
try{
var _fd=curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
if(_fd!=false&&!curam.util.isModalWindow()){
return _fd;
}
var _fe=_1.byId("container-messages-ul")?_1.byId("container-messages-ul"):_1.byId("error-messages");
if(_fe){
return _fe;
}
var _ff=document.forms[0];
if(!_ff){
return false;
}
var _100=_ff.querySelectorAll("button, output, input:not([type=\"image\"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton");
var _101=false;
var l=_100.length,el;
for(var i=0;i<l;i++){
el=_100[i];
if(!_101&&/selectHook/.test(el.className)){
_101=_b(el).closest("table")[0];
}
if(!_101&&!(el.style.visibility=="hidden")&&(/select-one|select-multiple|checkbox|radio|text/.test(el.type)||el.tagName=="TEXTAREA"||/popup-action|fileUploadButton/.test(el.className))&&!/dijitArrowButtonInner|dijitValidationInner/.test(el.className)){
_101=el;
}
if(el.tabIndex=="1"){
el.tabIndex=0;
_101=el;
break;
}
}
lastOpenedTabButton=curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
if(!_101&&lastOpenedTabButton){
return lastOpenedTabButton;
}
var _102=_101.classList.contains("bx--date-picker__input");
if(_102){
var _103=document.querySelector(".bx--uim-modal");
if(_103){
_101=_103;
}
}
return _101;
}
catch(e){
_15.log(_15.getProperty("curam.util.error.focus"),e.message);
return false;
}
return false;
},openLocaleSelector:function(_104){
_104=_a.fix(_104);
var _105=_104.target;
while(_105&&_105.tagName!="A"){
_105=_105.parentNode;
}
var loc=_105.href;
var rpu=curam.util.getUrlParamValue(loc,"__o3rpu");
rpu=curam.util.removeUrlParam(rpu,"__o3rpu");
var href="user-locale-selector.jspx"+"?__o3rpu="+rpu;
if(!curam.util.isActionPage(dojo.global.location.href)){
openModalDialog({href:href},"width=500,height=300",200,150);
}else{
alert(curam.util.msgLocaleSelectorActionPage);
}
return false;
},openLocaleNew:function(_106){
_a.stop(_106);
if(_106.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-locale-selector.jspx",{dialogOptions:"width=300"});
});
},isActionPage:function(url){
var _107=curam.util.getLastPathSegmentWithQueryString(url);
var _108=_107.split("?")[0];
return _108.indexOf("Action.do")>-1;
},closeLocaleSelector:function(_109){
_109=_a.fix(_109);
_a.stop(_109);
dojo.global.close();
return false;
},getSuffixFromClass:function(node,_10a){
var _10b=_11.get(node,"class").split(" ");
var _10c=_7.filter(_10b,function(_10d){
return _10d.indexOf(_10a)==0;
});
if(_10c.length>0){
return _10c[0].split(_10a)[1];
}else{
return null;
}
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},stripeTable:function(_10e,_10f,_110){
var _111=_10e.tBodies[0];
var _112=(_10f?2:1);
if(_111.rows.length<_112){
return;
}
var rows=_111.rows;
var _113=[],_114=[],_115=false,_116=[],_117="";
for(var i=0,l=rows.length;i<l;i+=_112){
var _118=(i%(2*_112)==0),_119=_118?_114:_113;
_8.remove(rows[i],["odd-last-row","even-last-row"]);
_119.push(rows[i]);
if(i==_110){
_116.push(rows[i]);
_117=_118?"odd":"even";
_115=true;
}
if(_10f&&rows[i+1]){
_8.remove(rows[i+1],["odd-last-row","even-last-row"]);
_119.push(rows[i+1]);
_115&&_116.push(rows[i+1]);
}
_115=false;
}
_114.forEach(function(_11a){
_8.replace(_11a,"odd","even");
});
_113.forEach(function(_11b){
_8.replace(_11b,"even","odd");
});
_116.forEach(function(_11c){
_8.add(_11c,_117+"-last-row");
});
},fillString:function(_11d,_11e){
var _11f="";
while(_11e>0){
_11f+=_11d;
_11e-=1;
}
return _11f;
},updateHeader:function(qId,_120,_121,_122){
var _123=_1.byId("header_"+qId);
_123.firstChild.nextSibling.innerHTML=_120;
answerCell=_1.byId("chosenAnswer_"+qId);
answerCell.innerHTML=_121;
sourceCell=_1.byId("chosenSource_"+qId);
sourceCell.innerHTML=_122;
},search:function(_124,_125){
var _126=_2.byId(_124).get("value");
var _127=_2.byId(_125);
if(_127==null){
_127=_1.byId(_125);
}
var _128=null;
if(_127!=null){
if(_127.tagName==null){
_128=_127?_127.get("value"):null;
}else{
if(_127.tagName=="SELECT"){
var _129=_b(".multiple-search-banner select option");
_7.forEach(_129,function(elem){
if(elem.selected){
_128=elem.value;
}
});
}
}
}
var _12a="";
var _12b;
var _12c;
if(_128){
_12c=_128.split("|");
_12a=_12c[0];
_12b=_12c[1];
}
var _12d=curam.util.defaultSearchPageID;
var _12e="";
if(sessionStorage.getItem("appendSUEP")==="true"){
if(_12a===""){
_12e=_12d+"Page.do?searchText="+encodeURIComponent(_126)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1");
}else{
_12e=_12b+"Page.do?searchText="+encodeURIComponent(_126)+"&searchType="+encodeURIComponent(_12a)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1,"+curam.util.secureURLsExemptParamsPrefix+"_ST2");
}
}else{
if(_12a===""){
_12e=_12d+"Page.do?searchText="+encodeURIComponent(_126);
}else{
_12e=_12b+"Page.do?searchText="+encodeURIComponent(_126)+"&searchType="+encodeURIComponent(_12a);
}
}
var _12f=new curam.ui.PageRequest(_12e);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_12f);
});
},updateDefaultSearchText:function(_130,_131){
var _132=_2.byId(_130);
var _133=_2.byId(_131);
var _134=_132?_132.get("value"):null;
var str=_134.split("|")[2];
_133.set("placeHolder",str);
_9.publish("curam/application-search/combobox-changed",_134);
},updateSearchBtnState:function(_135,_136){
var _137=_2.byId(_135);
var btn=_1.byId(_136);
var _138=_137.get("value");
if(!_138||_12.trim(_138).length<1){
_8.add(btn,"dijitDisabled");
}else{
_8.remove(btn,"dijitDisabled");
}
},furtherOptionsSearch:function(){
var _139=curam.util.furtherOptionsPageID+"Page.do";
var _13a=new curam.ui.PageRequest(_139);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_13a);
});
},searchButtonStatus:function(_13b){
var btn=_1.byId(_13b);
if(!_8.contains(btn,"dijitDisabled")){
return true;
}
},getPageHeight:function(){
var _13c=400;
var _13d=0;
if(_b("frameset").length>0){
curam.debug.log("curam.util.getPageHeight() "+_15.getProperty("curam.util.default.height"),_13c);
_13d=_13c;
}else{
var _13e=function(node){
if(!node){
curam.debug.log(_15.getProperty("curam.util.node"));
return 0;
}
var mb=_f.getMarginSize(node);
var pos=_f.position(node);
return pos.y+mb.h;
};
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
var _13f=_b("div#content")[0];
var _140=_13e(_13f);
curam.debug.log(_15.getProperty("curam.util.page.height"),_140);
_13d=_140;
}else{
var _141=_1.byId("content")||_1.byId("wizard-content");
var _142=_b("> *",_141).filter(function(n){
return n.tagName.indexOf("SCRIPT")<0&&_6.get(n,"visibility")!="hidden"&&_6.get(n,"display")!="none";
});
var _143=_142[0];
for(var i=1;i<_142.length;i++){
if(_13e(_142[i])>=_13e(_143)){
_143=_142[i];
}
}
_13d=_13e(_143);
curam.debug.log("curam.util.getPageHeight() "+_15.getProperty("curam.util.base.height"),_13d);
var _144=_b(".actions-panel",_5.body());
if(_144.length>0){
var _145=_f.getMarginBox(_144[0]).h;
curam.debug.log("curam.util.getPageHeight() "+_15.getProperty("curam.util.panel.height"));
_13d+=_145;
_13d+=10;
}
var _146=_b("body.details");
if(_146.length>0){
curam.debug.log("curam.util.getPageHeight() "+_15.getProperty("curam.util.bar.height"));
_13d+=20;
}
}
}
curam.debug.log("curam.util.getPageHeight() "+_15.getProperty("curam.util.returning"),_13d);
return _13d;
},toCommaSeparatedList:function(_147){
var _148="";
for(var i=0;i<_147.length;i++){
_148+=_147[i];
if(i<_147.length-1){
_148+=",";
}
}
return _148;
},setupGenericKeyHandler:function(){
_4(function(){
var f=function(_149){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")&&_149.keyCode==27){
var ev=_a.fix(_149);
var _14a=_2.byId(ev.target.id);
var _14b=typeof _14a!="undefined"&&_14a.baseClass=="dijitTextBox dijitComboBox";
if(!_14b){
curam.dialog.closeModalDialog();
}
}
if(_149.keyCode==13){
var ev=_a.fix(_149);
var _14c=ev.target.type=="text";
var _14d=ev.target.type=="radio";
var _14e=ev.target.type=="checkbox";
var _14f=ev.target.type=="select-multiple";
var _150=ev.target.type=="password";
var _151=_2.byId(ev.target.id);
if(typeof _151!="undefined"){
var _152=_2.byId(ev.target.id);
if(!_152){
_152=_2.byNode(_1.byId("widget_"+ev.target.id));
}
if(_152&&_152.enterKeyOnOpenDropDown){
_152.enterKeyOnOpenDropDown=false;
return false;
}
}
var _153=ev.target.getAttribute("data-carbon-attach-point");
if(_153&&_153==="carbon-menu"){
return false;
}
var _154=typeof _151!="undefined"&&_151.baseClass=="dijitComboBox";
if((!_14c&&!_14d&&!_14e&&!_14f&&!_150)||_154){
return true;
}
var _155=null;
var _156=_b(".curam-default-action");
if(_156.length>0){
_155=_156[0];
}else{
var _157=_b("input[type='submit']");
if(_157.length>0){
_155=_157[0];
}
}
if(_155!=null){
_a.stop(_a.fix(_149));
curam.util.clickButton(_155);
return false;
}
require(["curam/dateSelectorUtil"],function(_158){
var _159=_1.byId("year");
if(_159){
dojo.stopEvent(dojo.fixEvent(_149));
_158.updateCalendar();
}
});
}
return true;
};
curam.util.connect(_5.body(),"onkeyup",f);
});
},enterKeyPress:function(_15a){
if(_15a.keyCode==13){
return true;
}
},swapState:function(node,_15b,_15c,_15d){
if(_15b){
_8.replace(node,_15c,_15d);
}else{
_8.replace(node,_15d,_15c);
}
},makeQueryString:function(_15e){
if(!_15e||_15e.length==0){
return "";
}
var _15f=[];
for(var _160 in _15e){
_15f.push(_160+"="+encodeURIComponent(_15e[_160]));
}
return "?"+_15f.join("&");
},fileDownloadAnchorHandler:function(url){
var _161=curam.util.getTopmostWindow();
var _162=_161.dojo.subscribe("/curam/dialog/close",function(id,_163){
if(_163==="confirm"){
curam.util.clickHandlerForListActionMenu(url,false,false);
}
_161.dojo.unsubscribe(_162);
});
var _164=new _1a("GenericModalError");
var _165=_164.getProperty("file.download.warning.dialog.width");
var _166=_164.getProperty("file.download.warning.dialog.height");
if(!_165){
_165=500;
}
if(!_166){
_166=225;
}
var _167=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_165+",height="+_166,"file.download.warning.title","file.download.warning."+_167);
return false;
},fileDownloadListActionHandler:function(url,_168,_169,_16a){
var _16b=curam.util.getTopmostWindow();
var _16c=_16b.dojo.subscribe("/curam/dialog/close",function(id,_16d){
if(_16d==="confirm"){
curam.util.clickHandlerForListActionMenu(url,_168,_169,_16a);
}
_16b.dojo.unsubscribe(_16c);
});
var _16e=new _1a("GenericModalError");
var _16f=_16e.getProperty("file.download.warning.dialog.width");
var _170=_16e.getProperty("file.download.warning.dialog.height");
if(!_16f){
_16f=500;
}
if(!_170){
_170=225;
}
var _171=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_16f+",height="+_170,"file.download.warning.title","file.download.warning."+_171);
},_getBrowserName:function(){
var _172=_d("trident");
var _173=dojo.isFF;
var _174=dojo.isChrome;
var _175=dojo.isSafari;
var _176=curam.util.getTopmostWindow();
var _177=curam.util.ExpandableLists._isExternalApp(_176);
if(_172!=undefined){
var _178=_172+4;
if(_178<8){
return "unknown.browser";
}else{
return "ie"+_178;
}
}else{
if(_173!=undefined&&_177){
return "firefox";
}else{
if(_174!=undefined){
return "chrome";
}else{
if(_175!=undefined&&_177){
return "safari";
}
}
}
}
return "unknown.browser";
},clickHandlerForListActionMenu:function(url,_179,_17a,_17b){
if(_179){
var href=curam.util.replaceUrlParam(url,"o3frame","modal");
var ctx=dojo.global.jsScreenContext;
ctx.addContextBits("MODAL");
href=curam.util.replaceUrlParam(href,"o3ctx",ctx.getValue());
curam.util.redirectWindow(href);
return;
}
var _17c={href:url};
require(["curam/ui/UIMPageAdaptor"]);
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_17c)){
if(_17c.href.indexOf("/servlet/FileDownload")){
sessionStorage.setItem("addOnUnloadTriggeredByFileDownload","true");
dojo.global.location=url;
sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
}else{
dojo.global.location=url;
}
return;
}
if(_17c!=null){
if(_17b){
_a.fix(_17b);
_a.stop(_17b);
}
if(!_17c.href||_17c.href.length==0){
return;
}
if(_17a&&!curam.util.isInternal(url)){
dojo.global.open(url);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_17c)){
var _17d=new curam.ui.PageRequest(_17c.href);
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")){
_17d.pageHolder=dojo.global;
}
require(["curam/tab"],function(){
var _17e=curam.tab.getTabController();
if(_17e){
_17e.handlePageRequest(_17d);
}
});
}
}
}
},clickHandlerForMailtoLinks:function(_17f,url){
dojo.stopEvent(_17f);
var _180=dojo.query("#mailto_frame")[0];
if(!_180){
_180=dojo.io.iframe.create("mailto_frame","");
}
_180.src=url;
return false;
},isInternal:function(url){
var path=url.split("?")[0];
var _181=path.match("Page.do");
if(_181!=null){
return true;
}
return false;
},getLastPathSegmentWithQueryString:function(url){
var _182=url.split("?");
var _183=_182[0].split("/");
return _183[_183.length-1]+(_182[1]?"?"+_182[1]:"");
},replaceSubmitButton:function(name,_184,_185,_186,_187){
if(curam.replacedButtons[name]=="true"){
return;
}
var _188="__o3btn."+name;
var _189;
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_189=_b("input[id='"+_188+"']");
}else{
_189=_b("input[name='"+_188+"']");
}
_189.forEach(function(_18a,_18b,_18c){
if(_184){
var _18d=_18c[1];
_18d.setAttribute("value",_184);
}
_18a.tabIndex=-1;
var _18e=_18a.parentNode;
var _18f;
var _190=curam.util.isInternalModal()&&curam.util.isModalFooter(_18a);
var _191="btn-id-"+_18b;
var _192="ac initially-hidden-widget "+_191;
if(_8.contains(_18a,"first-action-control")){
_192+=" first-action-control";
}
if(_190){
var _193=(_185&&!_187)?undefined:_18c[0];
var _194=_185?{"href":"","buttonid":_186}:{"buttonid":_186};
var _195=_18a.getAttribute("data-rawtestid");
if(_195){
_194.dataTestId=_195;
}
var _196=_8.contains(_18a,"curam-default-action")?true:false;
curam.util.addCarbonModalButton(_194,_18a.value,_193,_196);
}else{
curam.util.setupWidgetLoadMask("a."+_191);
var _192="ac initially-hidden-widget "+_191;
if(_8.contains(_18a,"first-action-control")){
_192+=" first-action-control";
}
var _18f=_3.create("a",{"class":_192,href:"#"},_18a,"before");
var _197=dojo.query(".page-level-menu")[0];
if(_197){
_11.set(_18f,"title",_18a.value);
}
_3.create("span",{"class":"filler"},_18f,"before");
var left=_3.create("span",{"class":"left-corner"},_18f);
var _198=_3.create("span",{"class":"right-corner"},left);
var _199=_3.create("span",{"class":"middle"},_198);
_199.appendChild(document.createTextNode(_18a.value));
curam.util.addActionControlClass(_18f);
}
if(_18f){
on(_18f,"click",function(_19a){
curam.util.clickButton(this._submitButton);
_a.stop(_19a);
});
_18f._submitButton=_18c[0];
}
_8.add(_18a,"hidden-button");
_11.set(_18a,"aria-hidden","true");
_11.set(_18a,"id",_18a.id+"_"+_18b);
});
curam.replacedButtons[name]="true";
},isInternalModal:function(){
return !dojo.global.jsScreenContext.hasContextBits("EXTAPP")&&dojo.global.jsScreenContext.hasContextBits("MODAL");
},isModalFooter:function(_19b){
if(_19b){
var _19c=_19b.parentNode.parentNode;
return _19c&&_19c.id=="actions-panel";
}
},addCarbonModalButton:function(_19d,_19e,_19f,_1a0){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton",[_19d,_19e,_19f,_1a0,window]);
},setupWidgetLoadMask:function(_1a1){
curam.util.subscribe("/curam/page/loaded",function(){
var _1a2=_b(_1a1)[0];
if(_1a2){
_6.set(_1a2,"visibility","visible");
}else{
curam.debug.log("setupButtonLoadMask: "+_15.getProperty("curam.util.not.found")+"'"+_1a1+"'"+_15.getProperty("curam.util.ignore.mask"));
}
});
},optReplaceSubmitButton:function(name){
if(curam.util.getFrameRoot(dojo.global,"wizard")==null){
curam.util.replaceSubmitButton(name);
return;
}
var _1a3=curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
if(_1a3.delegatesSubmit[jsPageID]!="assumed"){
curam.util.replaceSubmitButton(name);
}
},clickButton:function(_1a4){
var _1a5=_1.byId("mainForm");
var _1a6;
if(!_1a4){
curam.debug.log("curam.util.clickButton: "+_15.getProperty("curam.util..no.arg"));
return;
}
if(typeof (_1a4)=="string"){
var _1a7=_1a4;
curam.debug.log("curam.util.clickButton: "+_15.getProperty("curam.util.searching")+_15.getProperty("curam.util.id.of")+"'"+_1a7+"'.");
_1a4=_b("input[id='"+_1a7+"']")[0];
if(!_1a4){
_1a4=_b("input[name='"+_1a7+"']")[0];
}
if(!_1a4.form&&!_1a4.id){
curam.debug.log("curam.util.clickButton: "+_15.getProperty("curam.util.searched")+_15.getProperty("curam.util.id.of")+"'"+_1a7+_15.getProperty("curam.util.exiting"));
return;
}
}
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_1a6=_1a4;
}else{
_1a6=_b("input[id='"+_1a4.id+"']",_1a5)[0];
if(!_1a6){
_1a6=_b("input[name='"+_1a4.name+"']",_1a5)[0];
}
}
try{
if(_11.get(_1a5,"action").indexOf(jsPageID)==0){
curam.util.publishRefreshEvent();
}
_1a6.click();
}
catch(e){
curam.debug.log(_15.getProperty("curam.util.exception.clicking"));
}
},printPage:function(_1a8,_1a9){
_a.stop(_1a9);
var _1aa=dojo.window.get(_1a9.currentTarget.ownerDocument);
if(_1a8===false){
curam.util._printMainAreaWindow(_1aa);
return false;
}
var _1ab=_1aa.frameElement;
var _1ac=_1ab;
while(_1ac&&!_8.contains(_1ac,"tab-content-holder")){
_1ac=_1ac.parentNode;
}
var _1ad=_1ac;
var _1ae=dojo.query(".detailsPanelFrame",_1ad)[0];
var _1af=_1ae!=undefined&&_1ae!=null;
if(_1af){
var isIE=_d("trident")||_d("ie");
var _1b0=_d("edge");
var _1b1=_8.contains(_1ae.parentNode,"collapsed");
if(isIE&&_1b1){
_6.set(_1ae.parentNode,"display","block");
}
_1ae.contentWindow.focus();
_1ae.contentWindow.print();
if(isIE&&_1b1){
_6.set(_1ae.parentNode,"display","");
}
if(isIE||_1b0){
setTimeout(function(){
if(_1b0){
function _1b2(){
curam.util._printMainAreaWindow(_1aa);
curam.util.getTopmostWindow().document.body.removeEventListener("mouseover",_1b2,true);
return false;
};
curam.util.getTopmostWindow().document.body.addEventListener("mouseover",_1b2,true);
}else{
if(isIE){
curam.util._printMainAreaWindow(_1aa);
return false;
}
}
},2000);
}else{
curam.util._printMainAreaWindow(_1aa);
return false;
}
}else{
curam.util._printMainAreaWindow(_1aa);
return false;
}
},_printMainAreaWindow:function(_1b3){
var _1b4=_b(".list-details-row-toggle.expanded");
if(_1b4.length>0){
curam.util._prepareContentPrint(_1b3);
_1b3.focus();
_1b3.print();
curam.util._deletePrintVersion();
}else{
_1b3.focus();
_1b3.print();
}
},_prepareContentPrint:function(_1b5){
var _1b6=Array.prototype.slice.call(_1b5.document.querySelectorAll("body iframe"));
_1b6.forEach(function(_1b7){
curam.util._prepareContentPrint(_1b7.contentWindow);
var list=_1b7.contentWindow.document.querySelectorAll(".title-exists");
var _1b8=_1b7.contentWindow.document.querySelectorAll(".title-exists div.context-panel-wrapper");
if(list.length>0&&_1b8.length===0){
var _1b9=document.createElement("div");
_1b9.setAttribute("class","tempContentPanelFrameWrapper");
_1b9.innerHTML=list[0].innerHTML;
var _1ba=_1b7.parentNode;
_1ba.parentNode.appendChild(_1b9);
_1ba.style.display="none";
curam.util.wrappersMap.push({tempDivWithIframeContent:_1b9,iframeParentElement:_1ba});
}
});
},_deletePrintVersion:function(){
if(curam.util.wrappersMap){
curam.util.wrappersMap.forEach(function(_1bb){
_1bb.tempDivWithIframeContent.parentNode.removeChild(_1bb.tempDivWithIframeContent);
_1bb.iframeParentElement.style.display="block";
});
curam.util.wrappersMap=[];
}
},addSelectedClass:function(_1bc){
_8.add(_1bc.target,"selected");
},removeSelectedClass:function(_1bd){
_8.remove(_1bd.target,"selected");
},openHelpPage:function(_1be,_1bf){
_a.stop(_1be);
dojo.global.open(_1bf);
},connect:function(_1c0,_1c1,_1c2){
var h=function(_1c3){
_1c2(_a.fix(_1c3));
};
if(_d("ie")&&_d("ie")<9){
_1c0.attachEvent(_1c1,h);
_e.addOnWindowUnload(function(){
_1c0.detachEvent(_1c1,h);
});
return {object:_1c0,eventName:_1c1,handler:h};
}else{
var _1c4=_1c1;
if(_1c1.indexOf("on")==0){
_1c4=_1c1.slice(2);
}
var dt=on(_1c0,_1c4,h);
_e.addOnWindowUnload(function(){
dt.remove();
});
return dt;
}
},disconnect:function(_1c5){
if(_d("ie")&&_d("ie")<9){
_1c5.object.detachEvent(_1c5.eventName,_1c5.handler);
}else{
_1c5.remove();
}
},subscribe:function(_1c6,_1c7){
var st=_9.subscribe(_1c6,_1c7);
_e.addOnWindowUnload(function(){
st.remove();
});
return st;
},unsubscribe:function(_1c8){
_1c8.remove();
},addActionControlClickListener:function(_1c9){
var _1ca=_1.byId(_1c9);
var _1cb=_b(".ac",_1ca);
if(_1cb.length>0){
for(var i=0;i<_1cb.length;i++){
var _1cc=_1cb[i];
curam.util.addActionControlClass(_1cc);
}
}
this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
},_addAccessibilityMarkupInAddressClustersWhenContextIsMissing:function(){
var _1cd=_b(".bx--accordion__content");
_1cd.forEach(function(_1ce){
var _1cf=_b(".bx--address",_1ce)[0];
if(typeof (_1cf)!="undefined"){
var _1d0=new _1a("util");
var _1d1=_1cf.parentElement.parentElement.parentElement;
var _1d2=_1d1.parentElement.parentElement;
var _1d3=_b("h4, h3",_1d1).length==1?true:false;
var _1d4=_11.get(_1d2,"aria-label")!==null?true:false;
if(!_1d3&&!_1d4){
_11.set(_1d2,"role","group");
_11.set(_1d2,"aria-label",_1d0.getProperty("curam.address.header"));
}
}
});
},addActionControlClass:function(_1d5){
curam.util.connect(_1d5,"onmousedown",function(){
_8.add(_1d5,"selected-button");
curam.util.connect(_1d5,"onmouseout",function(){
_8.remove(_1d5,"selected-button");
});
});
},getClusterActionSet:function(){
var _1d6=_1.byId("content");
var _1d7=_b(".blue-action-set",_1d6);
if(_1d7.length>0){
for(var i=0;i<_1d7.length;i++){
curam.util.addActionControlClickListener(_1d7[i]);
}
}
},adjustActionButtonWidth:function(){
if(_d("ie")==8){
_4(function(){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")){
_b(".action-set > a").forEach(function(node){
if(node.childNodes[0].offsetWidth>node.offsetWidth){
_6.set(node,"width",node.childNodes[0].offsetWidth+"px");
_6.set(node,"display","block");
_6.set(node,"display","inline-block");
}
});
}
});
}
},setRpu:function(url,rtc,_1d8){
if(!url||!rtc||!rtc.getHref()){
throw {name:"Unexpected values",message:"This value not allowed for url or rtc"};
}
var _1d9=curam.util.getLastPathSegmentWithQueryString(rtc.getHref());
_1d9=curam.util.removeUrlParam(_1d9,curam.util.Constants.RETURN_PAGE_PARAM);
if(_1d8){
var i;
for(i=0;i<_1d8.length;i++){
if(!_1d8[i].key||!_1d8[i].value){
throw {name:"undefined value error",message:"The object did not contain a valid key/value pair"};
}
_1d9=curam.util.replaceUrlParam(_1d9,_1d8[i].key,_1d8[i].value);
}
}
var _1da=curam.util.replaceUrlParam(url,curam.util.Constants.RETURN_PAGE_PARAM,encodeURIComponent(_1d9));
curam.debug.log("curam.util.setRpu "+_15.getProperty("curam.util.added.rpu")+_1da);
return _1da;
},retrieveBaseURL:function(){
return dojo.global.location.href.match(".*://[^/]*/[^/]*");
},removeRoleRegion:function(){
var body=dojo.query("body")[0];
_11.remove(body,"role");
},iframeTitleFallBack:function(){
var _1db=curam.tab.getContainerTab(curam.tab.getContentPanelIframe());
var _1dc=_1.byId(curam.tab.getContentPanelIframe());
var _1dd=_1dc.contentWindow.document.title;
var _1de=dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
var _1df=dojo.query("span.tabLabel",_1de)[0];
var _1e0=dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked",_1db.domNode)[0];
var _1e1=dojo.query("span.tabLabel",_1e0)[0];
if(_1dd=="undefined"){
return this.getPageTitleOnContentPanel();
}else{
if(_1dd&&_1dd!=""){
return _1dd;
}else{
if(_1e0){
return _1e1.innerHTML;
}else{
return _1df.innerHTML;
}
}
}
},getPageTitleOnContentPanel:function(){
var _1e2;
var _1e3=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _1e4;
if(_1e3&&_1e3.length==1){
_1e4=_1e3[0].contentWindow.document;
_5.withDoc(_1e4,function(){
var _1e5=dojo.query("div.title h2 span:not(.hidden)");
if(_1e5&&_1e5.length==1&&_1e5[0].textContent){
_1e2=_12.trim(_1e5[0].textContent);
}
},this);
}
if(_1e2){
return _1e2;
}else{
return undefined;
}
},addClassToLastNodeInContentArea:function(){
var _1e6=_b("> div","content");
var _1e7=_1e6.length;
if(_1e7==0){
return "No need to add";
}
var _1e8=_1e6[--_1e7];
while(_8.contains(_1e8,"hidden-action-set")&&_1e8){
_1e8=_1e6[--_1e7];
}
_8.add(_1e8,"last-node");
},highContrastModeType:function(){
var _1e9=dojo.query("body.high-contrast")[0];
return _1e9;
},isRtlMode:function(){
var _1ea=dojo.query("body.rtl")[0];
return _1ea;
},processBidiContextual:function(_1eb){
_1eb.dir=_13.prototype._checkContextual(_1eb.value);
},getCookie:function(name){
var dc=document.cookie;
var _1ec=name+"=";
var _1ed=dc.indexOf("; "+_1ec);
if(_1ed==-1){
_1ed=dc.indexOf(_1ec);
if(_1ed!=0){
return null;
}
}else{
_1ed+=2;
}
var end=document.cookie.indexOf(";",_1ed);
if(end==-1){
end=dc.length;
}
return unescape(dc.substring(_1ed+_1ec.length,end));
},getHeadingTitleForScreenReader:function(_1ee){
var _1ef=curam.util.getTopmostWindow();
var _1f0=_1ef.dojo.global._tabTitle;
if(_1f0){
curam.util.getHeadingTitle(_1f0,_1ee);
}else{
var _1f1=_1ef.dojo.subscribe("/curam/_tabTitle",function(_1f2){
if(_1f2){
curam.util.getHeadingTitle(_1f2,_1ee);
}
_1ef.dojo.unsubscribe(_1f1);
});
}
},getHeadingTitle:function(_1f3,_1f4){
var _1f5=undefined;
if(_1f3&&_1f3.length>0){
_1f5=_1f3;
}else{
_1f5=_1f4;
}
var _1f6=dojo.query(".page-title-bar");
var _1f7=dojo.query("div h2",_1f6[0]);
if(_1f7){
var _1f8=dojo.query("span",_1f7[0]);
var span=undefined;
if(_1f8){
span=_1f8[0];
}
if(!span||(span&&(span.innerHTML.length==0))){
if(span){
_11.set(span,"class","hidden");
_11.set(span,"title",_1f5);
span.innerHTML=_1f5;
}else{
span=_3.create("span",{"class":"hidden","title":_1f5},_1f7[0]);
span.innerHTML=_1f5;
}
}
}
},_setupBrowserTabTitle:function(_1f9,_1fa,_1fb){
_1f9=_1f9.replace("\\n"," ");
curam.util._browserTabTitleData.staticTabTitle=_1f9;
curam.util._browserTabTitleData.separator=_1fa;
curam.util._browserTabTitleData.appNameFirst=_1fb;
},_browserTabTitleData:{},setBrowserTabTitle:function(_1fc){
curam.debug.log("curam.util.setBrowserTabTitle(title = "+_1fc+") called");
if(!_1fc){
_1fc=curam.util._findAppropriateDynamicTitle();
}
var _1fd=curam.util._browserTabTitleData.staticTabTitle;
var _1fe=curam.util._browserTabTitleData.separator;
var _1ff=curam.util._browserTabTitleData.appNameFirst;
if(!_1fd&&!_1fe&&!_1ff&&!_1fc){
var _200=document.querySelectorAll("head title")[0];
if(_200){
document.title=_200.text;
}
}else{
if(!_1fc){
document.title=_1fd;
}else{
if(_1fd){
if(_1ff){
document.title=_1fd+_1fe+_1fc;
}else{
document.title=_1fc+_1fe+_1fd;
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
var _201=dojo.query("iframe.curam-active-modal").length;
if(_201>1){
var _202=dojo.query("iframe.curam-active-modal")[0];
if(_202){
var _203=_202.contentDocument;
if(_203){
var _204=_203.head.getElementsByTagName("title")[0];
if(_204){
if(_204.innerHTML!=""){
one=_202.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
}
}
}
}
}
if(one){
return one;
}
var two;
var _205=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _206;
if(_205&&_205.length==1){
_206=_205[0].contentWindow.document;
_5.withDoc(_206,function(){
var _207=dojo.query("div.title h2 span:not(.hidden)");
var _208=_1.byId("error-messages",_206);
if(_208){
two=_206.head.getElementsByTagName("title")[0].textContent;
}else{
if(_207&&_207.length==1&&_207[0].textContent){
two=_12.trim(_207[0].textContent);
curam.debug.log("(2) Page title for Content Panel = "+two);
}else{
if(_207&&_207.length>1){
two=this._checkForSubTitles(_207);
}else{
curam.debug.log("(2) Could not find page title for content panel: header = "+_207);
}
}
}
},this);
}else{
curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = "+_205);
}
if(two){
return two;
}
var _209;
var _20a=dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
if(_20a&&_20a.length==1&&_20a[0].textContent){
_209=_12.trim(_20a[0].textContent);
curam.debug.log("(3) Selected navigation item = "+_209);
}else{
curam.debug.log("(3) Could not find selected navigation item: navItem = "+_20a);
}
if(_209){
return _209;
}
var four;
var _20b=dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
var _20c;
for(i=0;i<_20b.length;i++){
if(_20b[i].getAttribute("aria-selected")==="true"){
_20c=_20b[i];
}
}
if(_20c&&_20c.textContent){
four=_12.trim(_20c.textContent);
curam.debug.log("(4) Selected navigation bar tab = "+four);
}else{
curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = "+_20c);
}
if(four){
return four;
}
var five;
var _20d=dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
if(_20d&&_20d.length==1&&_20d[0].textContent){
five=_12.trim(_20d[0].textContent);
curam.debug.log("(5) Selected application tab title bar = "+five);
}else{
curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = "+_20d);
}
if(five){
return five;
}
var six;
var _20e=dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
var _20f;
for(i=0;i<_20e.length;i++){
if(_20e[i].getAttribute("aria-selected")==="true"){
_20f=_20e[i];
break;
}
}
if(_20f&&_20f.textContent){
six=_12.trim(_20f.textContent);
curam.debug.log("(6) Selected section title = "+six);
}else{
curam.debug.log("(6) Could not find selected section title: sections = "+_20e);
}
if(six){
return six;
}
var _210;
_205=_1.byId("curamUAIframe");
if(_205&&_205.contentWindow&&_205.contentWindow.document){
_206=_205.contentWindow.document;
_5.withDoc(_206,function(){
var _211=dojo.query("div.page-header > div.page-title-bar > div.title > h2 > span");
if(_211&&_211.length==1&&_211[0].textContent){
_210=_12.trim(_211[0].textContent);
curam.debug.log("(7) UIM page title for external application page = "+_210);
}else{
curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = "+_211);
}
},this);
}
if(_210){
return _210;
}
return undefined;
},_checkForSubTitles:function(_212){
var i;
if(!_212[0].textContent){
return undefined;
}
for(i=1;i<_212.length;i++){
var _213=_212[i].getAttribute("class");
if(_213.indexOf("sub-title")===-1||!_212[i].textContent){
curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
for(i=0;i<_212.length;i++){
curam.debug.log(_212[i]);
}
return undefined;
}
}
var ret=_212[0].textContent;
for(i=1;i<_212.length;i++){
ret+=_212[i].textContent;
}
return ret;
},_addContextToWidgetForScreenReader:function(_214){
var _215=false;
var _216=0;
var _217=dojo.query(".training-details-list");
if(_217.length==1){
var _218=_217[0].parentElement;
var _219=dojo.query("div.bx--cluster",_218);
var _21a=Array.prototype.indexOf.call(_219,_217[0]);
if(_21a>=0){
for(var i=_21a;i>=0;i--){
if(dojo.query("h3",_219[i]).length==1){
_215=true;
_216=i;
break;
}
}
}
if(_215){
var _21b=dojo.query("h3.bx--accordion__title",_219[_216]);
if(_21b.length==1){
var _21c=_21b[0].className+"_id";
_11.set(_21b[0],"id",_21c);
var _21d=dojo.byId(_214).parentElement;
_11.set(_21d,"aria-labelledby",_21c);
_11.set(_21d,"role","region");
}
}
}
},setParentFocusByChild:function(_21e){
var win=curam.util.UimDialog._getDialogFrameWindow(_21e);
if(win){
var _21f=curam.dialog.getParentWindow(win);
if(_21f){
_21f.focus();
}
}
},toClipboard:function(_220){
try{
navigator.clipboard.writeText(_220);
}
catch(err){
console.warn("Failed to copy into the clipboard.");
}
if(dojo.getObject("curam.dialog",false)!=null){
var pw=curam.dialog.getParentWindow(window);
pw&&pw.dojo.publish("/curam/clip/selected",[_220]);
}
return false;
},removeTopScrollForIos:function(){
if(_d("ios")){
window.document.body.scrollTop=0;
}
},insertAriaLiveLabelRecordSearchItem:function(_221){
var span=dojo.query("[data-search-page]")[0];
if(span){
span.setAttribute("aria-live",_d("ios")?"polite":"assertive");
setTimeout(function(){
var _222=span.firstChild.nodeValue;
var _223=_222+_221;
span.innerHTML=_223;
},10);
}
},removeSessionStorageProperty:function(_224){
sessionStorage.removeItem(_224);
},addLayoutStylingOnDateTimeWidgetOnZoom:function(){
var _225=dojo.query("table.input-cluster td.field table.date-time");
console.log("datetimetable from util.js: "+_225);
var _226=_225.length;
if(_225.length>0){
for(var i=0;i<_225.length;i++){
var _227=_225[i];
var _228=_227.parentNode.parentNode;
_228.setAttribute("class","date-time-exists");
}
}
},fileUploadOpenFileBrowser:function(e,_229){
if(e.keyCode==32||e.keyCode==13){
_1.byId(_229).click();
}
},setupControlledLists:function(){
var _22a="curam.listControls",_22b="curam.listTogglers";
var _22c=_22d(_22a),_22e=_22d(_22b),_22f=[];
var _230=_22c&&_b("*[data-control]"),_231=_22e&&_b("a[data-toggler]");
if(_22c||_22e){
for(var _232 in _22c){
_230.filter(function(item){
return _11.get(item,"data-control")==_232;
}).forEach(function(_233,ix){
var c=_1.byId(_233),tr=_b(_233).closest("tr")[0];
!tr.controls&&(tr.controls=new Array());
tr.controls.push(c);
if(!tr.visited){
tr.visited=true;
_22c[_232].push(tr);
}
});
var _234=_22d(_22a+"."+_232);
if(_234&&_234.length&&_234.length>0){
_22f.push(_232);
}else{
_260(_22a+"."+_232,false);
}
}
if(_231&&_231.length>0){
for(var _232 in _22e){
_231.filter(function(item){
return _11.get(item,"data-toggler")==_232;
}).forEach(function(_235){
var tr=_b(_235).closest("tr")[0];
tr.hasToggler=_235;
tr.visited=true;
_22e[_232].push(tr);
});
var _236=_22d(_22b+"."+_232);
if(_236&&_236.length&&_236.length>0){
(_22f.indexOf(_232)==-1)&&_22f.push(_232);
}else{
_260(_22b+"."+_232,false);
}
}
}
_22f.forEach(function(_237){
var _238=_22d(_22a+"."+_237)||_22d(_22b+"."+_237);
cu.updateListControlReadings(_237,_238);
});
}
dojo.subscribe("curam/sort/earlyAware",function(_239){
cu.suppressPaginationUpdate=_239;
});
dojo.subscribe("curam/update/readings/sort",function(_23a,rows){
if(!_d("trident")){
cu.updateListActionReadings(_23a);
cu.updateListControlReadings(_23a,rows);
cu.suppressPaginationUpdate=false;
}else{
var _23b=cu.getPageBreak(_23a),_23c=Math.ceil(rows.length/_23b);
cu.listRangeUpdate(0,_23c,_23a,rows,_23b);
}
});
dojo.subscribe("curam/update/readings/pagination",function(_23d,_23e){
_260("curam.pageBreak."+_23d,_23e);
});
dojo.subscribe("curam/update/pagination/rows",function(_23f,_240){
cu.updateDeferred&&!cu.updateDeferred.isResolved()&&cu.updateDeferred.cancel("Superseeded");
if(cu.suppressPaginationUpdate&&cu.suppressPaginationUpdate==_240){
return;
}
var _241=_26d("curam.listTogglers."+_240),_242=_26d("curam.listControls."+_240),lms=_22d("curam.listMenus."+_240),_243=lms&&(lms.length>0);
var _244=_242||_241;
if(!_244&&!_243){
return;
}
if(_244){
var _245=_23f.filter(function(aRow){
return (!aRow.visited||!aRow.done)&&_11.has(aRow,"data-lix");
});
_241&&_245.forEach(function(aRow){
var tgl=_b("a[data-toggler]",aRow)[0];
aRow.hasToggler=tgl;
aRow.visited=true;
curam.listTogglers[_240].push(aRow);
});
_242&&_245.forEach(function(aRow){
var _246=_b("*[data-control]",aRow),_247=new Array();
_246.forEach(function(cRef){
_247.push(_1.byId(cRef));
});
aRow.controls=_247;
curam.listControls[_240].push(aRow);
aRow.visited=true;
});
var _248=_242?curam.listControls[_240]:curam.listTogglers[_240];
cu.updateListControlReadings(_240,_248);
}
_243&&cu.updateListActionReadings(_240);
});
},listRangeUpdate:function(_249,_24a,_24b,rows,psz){
if(_249==_24a){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
return;
}
var def=cu.updateDeferred=new _c(function(_24c){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
});
def.then(function(pNum){
cu.listRangeUpdate(pNum,_24a,_24b,rows,psz);
},function(err){
});
var _24d=(_249===0)?0:200;
setTimeout(function(){
var _24e=_249+1,_24f=[_249*psz,(_24e*psz)];
cu.updateListActionReadings(_24b,_24f);
cu.updateListControlReadings(_24b,rows,_24f);
def.resolve(_24e);
},_24d);
},updateListControlReadings:function(_250,_251,_252){
var c0,psz=cu.getPageBreak(_250),_253=cu.getStartShift(_250,_251[0]||false),_254=_251;
_252&&(_254=_251.slice(_252[0],_252[1]));
for(var rix in _254){
var aRow=_254[rix],_255=parseInt(_11.get(aRow,_256)),lx=(_255%psz)+_253,_257=aRow.controls;
if(!_257){
var _258=_b("*[data-control]",aRow),_259=new Array();
_258.forEach(function(cRef){
_259.push(_1.byId(cRef));
});
aRow.controls=_259;
_257=aRow.controls;
}
if(_257){
for(var cix in _257){
var crtl=_257[cix],ttl=crtl.textContent||false,_25a=ttl?ttl+",":"";
if(crtl.nodeName=="A"){
var _25b=_b("img",crtl)[0];
if(_25b&&_8.contains(crtl,"ac first-action-control external-link")){
var _25c=_11.get(_25b,"alt");
_11.set(crtl,_25d,_25c+","+[listcontrol.reading.anchors,lx].join(" "));
}else{
_11.set(crtl,_25d,_25a+[listcontrol.reading.anchors,lx].join(" "));
}
}else{
_11.set(crtl,_25d,_25a+[listcontrol.reading.selectors,lx].join(" "));
}
}
}
cu.updateToggler(aRow,lx);
aRow.done=true;
}
},initListActionReadings:function(_25e){
var _25f="curam.listMenus."+_25e;
_260(_25f,[]);
dojo.subscribe("curam/listmenu/started",function(_261,_262){
var tr=_b(_261.containerNode).closest("tr")[0],lix=parseInt(_11.get(tr,_256)),lx=(lix%cu.getPageBreak(_262))+cu.getStartShift(_262,tr);
_261.set({"belongsTo":tr,"aria-labelledBy":"","aria-label":[listcontrol.reading.menus,lx].join(" ")});
_22d(_25f).push(_261);
cu.updateToggler(tr,lx);
});
},updateToggler:function(_263,_264){
_263.hasToggler&&_11.set(_263.hasToggler,_25d,[listcontrol.reading.togglers,_264].join(" "));
},updateListActionReadings:function(_265,_266){
var _267=_22d("curam.listMenus."+_265),psz=cu.getPageBreak(_265),_268=false,_269=_267;
_266&&(_269=_267.slice(_266[0],_266[1]));
for(var ix in _269){
var _26a=_269[ix],tr=_26a.belongsTo,lix=parseInt(_11.get(tr,_256)),_268=_268||cu.getStartShift(_265,tr),_26b=(lix%psz)+_268;
_26a.set(_25d,[listcontrol.reading.menus,_26b].join(" "));
cu.updateToggler(tr,_26b);
tr.done=true;
}
},getPageBreak:function(_26c){
if(!_26d("curam.list.isPaginated."+_26c)){
return 1000;
}
if(_22d("curam.shortlist."+_26c)){
return 1000;
}
var psz=_22d("curam.pageBreak."+_26c)||_22d("curam.pagination.defaultPageSize")||1000;
return psz;
},getStartShift:function(_26e,_26f){
if(!_26f){
return 2;
}
var _270="curam.listHeaderStep."+_26e,_271=_22d(_270);
if(_271){
return _271;
}
_260(_270,2);
var _272=_b(_26f).closest("table");
if(_272.length==0){
return 2;
}
var _273=_272.children("thead")[0];
!_273&&_260(_270,1);
return curam.listHeaderStep[_26e];
},extendXHR:function(){
var _274=XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open=function(){
this.addEventListener("load",function(){
if(typeof (Storage)!=="undefined"){
var _275=this.getResponseHeader("sessionExpiry");
sessionStorage.setItem("sessionExpiry",_275);
}
});
_274.apply(this,arguments);
};
},suppressPaginationUpdate:false,updateDeferred:null});
var cu=curam.util,_22d=dojo.getObject,_260=dojo.setObject,_26d=dojo.exists,_25d="aria-label",_256="data-lix";
return curam.util;
});
