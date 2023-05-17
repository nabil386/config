//>>built
define("curam/dialog",["dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/on","dojo/sniff","curam/inspection/Layer","curam/util","curam/debug","curam/util/external","curam/util/Refresh","curam/tab","curam/util/RuntimeContext","curam/util/ScreenContext","curam/define","curam/util/onLoad","dojo/dom-class","dojo/query","dojo/NodeList-traverse"],function(_1,_2,_3,on,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10){
curam.define.singleton("curam.dialog",{MODAL_PREV_FLAG:"o3modalprev",MODAL_PREV_FLAG_INPUT:"curam_dialog_prev_marker",FORCE_CLOSE:false,ERROR_MESSAGES_HEADER:"error-messages-header",_hierarchy:[],_id:null,_displayedHandlerUnsToken:null,_displayed:false,_size:null,_justClose:false,_modalExitingIEGScript:false,validTargets:{"_top":true,"_self":true},initModal:function(_11,_12,_13){
curam.dialog.pageId=_11;
curam.dialog.messagesExist=_12;
var _14=false;
var p1;
_6.extendXHR();
var _15=_6.getTopmostWindow();
var _16=false;
var _17=_15.dojo.subscribe("/curam/dialog/SetId",this,function(_18){
_7.log("curam.dialog: "+_7.getProperty("curam.dialog.id"),_18);
curam.dialog._id=_18;
_16=true;
_15.dojo.unsubscribe(_17);
});
_15.dojo.publish("/curam/dialog/init");
if(!_16){
_7.log("curam.dialog: "+_7.getProperty("curam.dialog.no.id"));
_15.dojo.unsubscribe(_17);
}
if(curam.dialog.closeDialog(false,_13)){
return;
}
curam.dialog._displayedHandlerUnsToken=_6.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",null,function(_19,_1a){
if(_19==curam.dialog._id){
curam.dialog._displayed=true;
curam.dialog._size=_1a;
_6.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
});
if(_1b==undefined){
var _1b=this.jsScreenContext;
if(!_1b){
_1b=new _c();
_1b.addContextBits("MODAL");
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_1b.addContextBits("AGENDA");
}
curam.util.external.inExternalApp()&&_1b.addContextBits("EXTAPP");
}
}
if(_1b.hasContextBits("AGENDA")||_1b.hasContextBits("TREE")){
dojo.addOnUnload(function(){
_6.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
});
}
dojo.addOnLoad(function(){
_6.connect(dojo.body(),"onclick",curam.dialog.modalEventHandler);
for(var i=0;i<document.forms.length;i++){
var _1c=document.forms[i];
curam.dialog.addFormInput(_1c,"hidden","o3frame","modal");
var _1d=_1.byId("o3ctx");
var sc=new curam.util.ScreenContext(_1b.getValue());
sc.addContextBits("ACTION|ERROR");
_1d.value=sc.getValue();
_6.connect(_1c,"onsubmit",curam.dialog.formSubmitHandler);
}
window.curamModal=true;
});
if(curam.util.isExitingIEGScriptInModalWindow()){
delete curam.util.getTopmostWindow().exitingIEGScript;
dojo.addOnUnload(function(){
_6.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}else{
var _1e=on(window,"unload",function(){
_1e.remove();
_6.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}
if(_16){
dojo.publish("/curam/dialog/ready");
}
},setVariableForModalExitingIEGScript:function(){
_modalExitingIEGScript=true;
},closeDialog:function(_1f,_20){
if(_1f){
curam.dialog.forceClose();
}
var _21=curam.dialog.checkClose(curam.dialog.pageId,_20);
if(_21){
_6.onLoad.addPublisher(function(_22){
_22.modalClosing=true;
});
if(curam.dialog.messagesExist){
dojo.addOnLoad(function(){
var _23=_1.byId(_6.ERROR_MESSAGES_CONTAINER);
var _24=_1.byId(_6.ERROR_MESSAGES_LIST);
var _25=_1.byId(curam.dialog.ERROR_MESSAGES_HEADER);
if(_24&&_25){
_6.saveInformationalMsgs(_21);
_6.disableInformationalLoad();
}else{
_21();
}
});
}else{
_21();
}
return true;
}
return false;
},addFormInput:function(_26,_27,_28,_29){
return _3.create("input",{"type":_27,"name":_28,"value":_29},_26);
},checkClose:function(_2a,_2b){
if(curam.dialog._justClose){
return function(){
curam.dialog.closeModalDialog();
};
}
var _2c=curam.dialog.getParentWindow(window);
if(!_2c){
return false;
}
var _2d;
if(_2b){
_2d=curam.util.retrieveBaseURL()+_2b;
}else{
_2d=window.location.href;
}
var _2e=curam.dialog.MODAL_PREV_FLAG;
var _2f=_6.getUrlParamValue(_2d,_2e);
var _30=true;
if(_2f){
if(_2c){
if(_2f==_2a){
_30=false;
}
}
}else{
_30=false;
}
var _31=_6.getUrlParamValue(_2d,"o3ctx");
if(_31){
var sc=new curam.util.ScreenContext();
sc.setContext(_31);
if(sc.hasContextBits("TREE|ACTION")){
_30=false;
}
}
if(_30||curam.dialog.FORCE_CLOSE){
if(!curam.dialog.FORCE_CLOSE){
if(_2f=="user-prefs-editor"){
return function(){
if(_2c&&_2c.location!==_6.getTopmostWindow().location){
curam.dialog.doRedirect(_2c);
}
curam.dialog.closeModalDialog();
};
}
return function(){
var rp=_6.removeUrlParam;
_2d=rp(rp(rp(_2d,_2e),"o3frame"),_6.PREVENT_CACHE_FLAG);
_2d=_6.adjustTargetContext(_2c,_2d);
if(_2c&&_2c.location!==_6.getTopmostWindow().location){
curam.dialog.doRedirect(_2c,_2d,true);
}else{
curam.tab.getTabController().handleLinkClick(_2d);
}
curam.dialog.closeModalDialog();
};
}else{
return function(){
if(_2c!==_6.getTopmostWindow()){
_2c.curam.util.loadInformationalMsgs();
}
curam.dialog.closeModalDialog();
};
}
}
return false;
},getParentWindow:function(_32){
if(!_32){
_7.log(["curam.dialog.getParentWindow():",_7.getProperty("curam.dialog.no.child"),window.location?" "+window.location.href:"[no location]"].join(" "));
_7.log("returning as parent = ",window.parent.location.href);
return window.parent;
}
var _33=curam.dialog._getDialogHierarchy();
if(_33){
for(var i=0;i<_33.length;i++){
if(_33[i]==_32){
var _34=(i>0)?_33[i-1]:_33[0];
_7.log(["curam.dialog.getParentWindow():",_7.getProperty("curam.dialog.parent.window"),_34.location?_34.location.href:"[no location]"].join(" "));
return _34;
}
}
var ret=_33.length>0?_33[_33.length-1]:undefined;
_7.log(["curam.dialog.getParentWindow():",_7.getProperty("curam.dialog.returning.parent"),ret?ret.location.href:"undefined"].join(" "));
return ret;
}
},_getDialogHierarchy:function(){
var _35=_6.getTopmostWindow();
_35.require(["curam/dialog"]);
return _35.curam.dialog._hierarchy;
},pushOntoDialogHierarchy:function(_36){
var _37=curam.dialog._getDialogHierarchy();
if(_37&&dojo.indexOf(_37,_36)<0){
_37.push(_36);
_7.log(_7.getProperty("curam.dialog.add.hierarchy"),_36.location.href);
_7.log(_7.getProperty("curam.dialog.full.hierarchy")+_37.reduce(function(acc,_38){
return acc+"["+(_38.location.href||"-")+"]";
}),"");
}
},removeFromDialogHierarchy:function(_39){
var _3a=curam.dialog._getDialogHierarchy();
if(!_39||_3a[_3a.length-1]==_39){
_3a.pop();
}else{
_7.log("curam.dialog.removeFromDialogHierarchy(): "+_7.getProperty("curam.dialog.ignore.request"));
try{
_7.log(_39.location.href);
}
catch(e){
_7.log(e.message);
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
},_isSameBaseUrl:function(_3b,rtc,_3c){
if(_3b&&_3b.indexOf("#")==0){
return true;
}
var _3d=_3b.split("?");
var _3e=rtc.getHref().split("?");
if(_3d[0].indexOf("/")<0){
var _3f=_3e[0].split("/");
_3e[0]=_3f[_3f.length-1];
}
if(_3e[0].indexOf("/")<0){
var _3f=_3d[0].split("/");
_3d[0]=_3f[_3f.length-1];
}
if(_3c&&_3c==true){
_3d[0]=curam.dialog.stripPageOrActionFromUrl(_3d[0]);
_3e[0]=curam.dialog.stripPageOrActionFromUrl(_3e[0]);
}
if(_3d[0]==_3e[0]){
return true;
}
return false;
},modalEventHandler:function(_40){
curam.dialog._doHandleModalEvent(_40,new curam.util.RuntimeContext(window),curam.dialog.closeModalDialog,curam.dialog.doRedirect);
},_showSpinnerInDialog:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/spinner");
},_doHandleModalEvent:function(e,rtc,_41,_42){
var _43=e.target;
var u=_6;
switch(_43.tagName){
case "INPUT":
if(_2.get(_43,"type")=="submit"&&typeof _43.form!="undefined"){
_43.form.setAttribute("keepModal",_43.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
}
return true;
case "IMG":
case "SPAN":
case "DIV":
_43=_10(_43).closest("A")[0];
if(_43==null){
return;
}
case "A":
case "BUTTON":
if(_43._submitButton){
_43._submitButton.form.setAttribute("keepModal",_43._submitButton.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
return;
}
break;
default:
return true;
}
var _44=dojo.stopEvent;
var _45=_43.getAttribute("href")||_43.getAttribute("data-href");
if(_45==""){
_41();
return false;
}
if(!_45){
return false;
}
if(_45.indexOf("javascript")==0){
return false;
}
var ctx=jsScreenContext;
ctx.addContextBits("MODAL");
var _46=_43.getAttribute("target");
if(_46&&!curam.dialog.validTargets[_46]){
return true;
}
if(_45&&_45.indexOf("/servlet/FileDownload?")>-1){
var _47=_3.create("iframe",{src:_45},dojo.body());
_47.style.display="none";
_44(e);
return false;
}
if(_f.contains(_43,"external-link")){
return true;
}
if(_6.isSameUrl(_45,null,rtc)){
if(_45.indexOf("#")<0){
_45=u.replaceUrlParam(_45,"o3frame","modal");
_45=u.replaceUrlParam(_45,"o3ctx",ctx.getValue());
_42(window,_45);
return false;
}
return true;
}
if(_45&&curam.dialog._isSameBaseUrl(_45,rtc,true)&&!_43.getAttribute("keepModal")){
_43.setAttribute("keepModal","true");
}
var _48=curam.dialog.getParentWindow(rtc.contextObject());
if(_43&&_43.getAttribute){
_44(e);
if(_43.getAttribute("keepModal")=="true"){
_45=u.replaceUrlParam(_45,"o3frame","modal");
_45=u.replaceUrlParam(_45,"o3ctx",ctx.getValue());
_42(window,_45);
}else{
if(_48){
_45=u.removeUrlParam(_45,"o3frame");
_45=u.removeUrlParam(_45,curam.dialog.MODAL_PREV_FLAG);
if(_48.location!==_6.getTopmostWindow().location){
var _49=new curam.util.RuntimeContext(_48);
var _4a=_49.getHref();
_4a=u.removeUrlParam(_4a,"o3frame");
if(_6.isActionPage(_4a)){
if(!curam.dialog._isSameBaseUrl(_45,_49,true)){
_45=u.adjustTargetContext(_48,_45);
_42(_48,_45);
}
}else{
if(!_6.isSameUrl(_45,_4a)){
_45=u.adjustTargetContext(_48,_45);
curam.dialog.doRedirect(_48,_45);
}
}
}else{
var _4b=new curam.util.ScreenContext("TAB");
_45=u.replaceUrlParam(_45,"o3ctx",_4b.getValue());
curam.tab.getTabController().handleLinkClick(_45);
}
_41();
}
}
return false;
}
if(_48&&typeof (_43)=="undefined"||_43==null||_43=="_self"||_43==""){
_44(e);
_45=_45.replace(/[&?]o3frame=modal/g,"").replace("%3Fo3frame%3Dmodal","").replace("?o3frame%3Dmodal","");
_45=_6.updateCtx(_45);
if(_48.location!==_6.getTopmostWindow().location){
_42(_48,_45);
}else{
var _4b=new curam.util.ScreenContext("TAB");
_45=u.replaceUrlParam(_45,"o3ctx",_4b.getValue());
curam.tab.getTabController().handleLinkClick(_45);
}
_41();
return false;
}
return true;
},formSubmitHandler:function(e){
if(e.type=="submit"&&e.defaultPrevented){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
return false;
}
var _4c=curam.dialog.getParentWindow(window);
if(typeof _4c=="undefined"){
return true;
}
e.target.method="post";
e.target.setAttribute("target",window.name);
var _4d=e.target.action;
var _4e=curam.dialog.MODAL_PREV_FLAG;
var _4f=curam.dialog.MODAL_PREV_FLAG_INPUT;
var u=_6;
var _50=_1.byId(_4f);
if(_50){
_50.parentNode.removeChild(_50);
}
if(e.target.getAttribute("keepModal")!="true"&&!jsScreenContext.hasContextBits("AGENDA")){
var _51="multipart/form-data";
if(e.target.enctype==_51||e.target.encoding==_51){
e.target.action=u.removeUrlParam(_4d,_4e);
_50=curam.dialog.addFormInput(e.target,"hidden",_4e,curam.dialog.pageId);
_50.setAttribute("id",_4f);
_50.id=_4f;
}else{
e.target.action=u.replaceUrlParam(_4d,_4e,curam.dialog.pageId);
}
}else{
e.target.action=u.removeUrlParam(_4d,_4e);
}
_4c.curam.util.invalidatePage();
if(!jsScreenContext.hasContextBits("EXTAPP")){
_6.firePageSubmittedEvent("dialog");
}
return true;
},forceClose:function(){
curam.dialog.FORCE_CLOSE=true;
},forceParentRefresh:function(){
var _52=curam.dialog.getParentWindow(window);
if(!_52){
return;
}
_52.curam.util.FORCE_REFRESH=true;
},forceParentLocaleRefresh:function(){
var _53=curam.dialog.getParentWindow(window);
if(!_53){
return;
}
_53.curam.util.LOCALE_REFRESH=true;
},closeModalDialog:function(_54){
var _55=_6.getTopmostWindow();
if(curam.dialog._displayedHandlerUnsToken!=null){
_55.dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
var _56=curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP");
if((typeof (curam.dialog._id)=="undefined"||curam.dialog._id==null||!_56)&&window.frameElement){
var _57=window.frameElement.id;
var _58=_57.substring(7);
curam.dialog._id=_58;
_7.log("curam.dialog.closeModalDialog() "+_7.getProperty("curam.dialog.modal.id")+_58);
}
_6.getTopmostWindow().dojo.publish("/curam/dialog/close/appExitConfirmation",[curam.dialog._id]);
_7.log("publishing /curam/dialog/close/appExitConfirmation for ",curam.dialog._id);
_7.log("publishing /curam/dialog/close for ",curam.dialog._id);
_6.getTopmostWindow().dojo.publish("/curam/dialog/close",[curam.dialog._id,_54]);
},parseWindowOptions:function(_59){
var _5a={};
if(_59){
_7.log("curam.dialog.parseWindowOptions "+_7.getProperty("curam.dialog.parsing"),_59);
var _5b=_59.split(",");
var _5c;
for(var i=0;i<_5b.length;i++){
_5c=_5b[i].split("=");
_5a[_5c[0]]=_5c[1];
}
_7.log("done:",dojo.toJson(_5a));
}else{
_7.log("curam.dialog.parseWindowOptions "+_7.getProperty("curam.dialog.no.options"));
}
return _5a;
},doRedirect:function(_5d,_5e,_5f,_60){
window.curamDialogRedirecting=true;
if(!curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP")){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/redirectingModal");
}
_5d.curam.util.redirectWindow(_5e,_5f,_60);
},_screenReaderAnnounceCurrentTabOnWizard:function(){
var _61=_1.byId("wizard-progress-bar");
if(_61){
var _62=_1.byId("hideAriaLiveElement");
if(typeof _62!=null){
this._createSpanContainingInformationOnCurrentWizardTab(_61,_62);
}
}
},_stylingAddedToMandatoryIconHelp:function(){
var _63=_1.byId("wizard-progress-bar");
var _64=dojo.query(".mandatory-icon-help")[0];
if(_63&&_64){
_f.add(_64,"wizard-progress-bar-exists");
}
},_createSpanContainingInformationOnCurrentWizardTab:function(_65,_66){
var _67=null;
var _68="";
var _69=" ";
var _6a=_10(".title",_65)[0]&&_10(".title",_65)[0].innerText;
var _6b=_10(".desc",_65)[0]&&_10(".desc",_65)[0].innerText;
if(_6a&&_6a!=""){
_68+=_6a;
}
if(_6b&&_6b!=""){
_68!=""?_68+=_69:"";
_68+=_6b;
}
var _6c=_1.byId("content");
var _6d=_10(".cluster,.list",_6c)[0];
if(_6d){
if(typeof _10(".collapse-title",_6d)[0]=="undefined"||_10(".collapse-title",_6d)[0].innerHTML==""){
if(typeof _10(".description",_6d)[0]!="undefined"){
if(_10(".description",_6d)[0].innerHTML!==""){
var _6e=_10(".description",_6d)[0];
if(_6e&&_6e.innerText!==""){
_68!=""?_68+=_69:"";
_68+=_10(".description",_6d)[0].innerText;
}
}
}
}
}else{
var _6f=_10("tr:first-child > td.field.last-cell",_6c)[0];
if(_6f&&_6f.innerText!==""){
_68!=""?_68+=_69:"";
_68+=_6f.innerText;
}
}
if(_68){
_67=_3.create("span",{innerHTML:_68});
setTimeout(function(){
_3.place(_67,_66);
},1000);
}
},closeGracefully:function(){
curam.dialog._justClose=true;
},});
_5.register("curam/dialog",this);
return curam.dialog;
});
