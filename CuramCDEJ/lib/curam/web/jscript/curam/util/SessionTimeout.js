//>>built
define("curam/util/SessionTimeout",["dojo/dom","dojo/dom-construct","curam/util","curam/dialog","curam/util/external","curam/util/LocalConfig"],function(_1,_2,_3,_4,_5,_6){
curam.define.singleton("curam.util.SessionTimeout",{_EXPIRED_MESSAGE_KEY:"expiredMessage",_INTERNAL_LOGOUT_WRAPPER:"internal-logout-wrapper",logoutPageID:"",userMessageNode:null,userMessageNodeID:"userMessage",minutesCompNodeID:"minutesComp",secondsCompNodeID:"secondsComp",sessTimeoutWarningJSPXDialog:"session-timeout-warning-dialog.jspx",interactionAllowance:3000,_idleTick:10000,_expiryTick:1000,_cfg:null,_idleClock:null,idleAllowance:0,tlw:false,_isExternal:false,requiredWarnDurationMs:0,effectiveTimeout:0,__remainingCountdown:0,checkSessionExpired:function(){
_7._invalidateIdleClock(true);
_7._isExternal=_7.tlw.jsScreenContext&&_7.tlw.jsScreenContext.hasContextBits("EXTAPP");
_7._cfg=_7.getTimeoutWarningConfig();
_7._cfg["isExternal"]=_7._isExternal;
_7._cfg["tlw"]=_7.tlw;
if(_7._cfg.bufferingPeriod){
_7.interactionAllowance=_7._cfg.bufferingPeriod*1000;
}
var _8=_7._cfg&&_7._cfg.timeout;
_7.requiredWarnDurationMs=_8?_8*1000:0;
_7._idleClock=setInterval(function(){
_7._doCheckExp();
},_7._idleTick);
},_doCheckExp:function(){
var _9="";
if(typeof (Storage)!=="undefined"){
if(sessionStorage.sessionExpiry){
_9=sessionStorage.sessionExpiry;
}
}
if(!_7.previousSessionExpiryString||(_7.previousSessionExpiryString!=_9)){
_7._validateSessionExpiry(_9);
return;
}
if(_7.idleAllowance==0){
return;
}
_7.idleAllowance-=_7._idleTick;
if(_7.idleAllowance<=0){
_7._invalidateIdleClock(true);
_7._openSessionTimeoutWarningModalDialog();
}
},_validateSessionExpiry:function(_a){
if(_a==null){
_7._invalidateIdleClock();
return;
}
var _b=_a.split("-",2);
if(_b&&_b.length==2){
for(var _c in _b){
var _d=Math.abs(_b[_c]);
if(isNaN(_d)){
_7._invalidateIdleClock();
return;
}
_b[_c]=_d;
}
_7.idleAllowance=Math.abs(_b[0]-_b[1]);
_7._insertWarnDuration();
_7.previousSessionExpiryString=_a;
return;
}
_7._invalidateIdleClock();
},_invalidateIdleClock:function(_e){
_e&&clearInterval(_7._idleClock);
_7.previousSessionExpiryString=false;
_7.idleAllowance=0;
},_insertWarnDuration:function(){
var _f=_7.idleAllowance-_7.interactionAllowance-_7._idleTick;
_7.effectiveTimeout=Math.min(_7.requiredWarnDurationMs,Math.max(_f,0));
_7._cfg["effectiveTimeout"]=_7.effectiveTimeout;
_7.idleAllowance-=(_7.effectiveTimeout);
},getTimeoutWarningConfig:function(){
var _10=_7.tlw||_7._cfg.tlw;
if(_10.TIMEOUT_WARNING_CONFIG){
return _10.TIMEOUT_WARNING_CONFIG.timeoutWarning;
}else{
return _7.pseudoConfig;
}
},_openSessionTimeoutWarningModalDialog:function(){
var _11={width:_7._cfg.width,height:_7._cfg.height};
if(_7._isExternal){
_7.tlw.openModal(_7.sessTimeoutWarningJSPXDialog,_11);
}else{
_7.tlw.dialogOpenerRef=_3.showModalDialogWithRef(_7.sessTimeoutWarningJSPXDialog,null,_11);
_7.tlw.dialogOpenerRef&&_7.tlw.dialogOpenerRef._setClosableAttr(false);
}
},initTimer:function(_12,_13){
_7._cfg=_12;
_7.__remainingCountdown=_12.effectiveTimeout;
var _14=_1.byId(_7.minutesCompNodeID),_15=_1.byId(_7.secondsCompNodeID);
_7.expiryCountdown=setInterval(function(){
_7._countDown(_14,_15);
},_7._expiryTick);
},_countDown:function(_16,_17){
if(_7.__remainingCountdown==0){
_7._stopCountdown();
return;
}
_7.__remainingCountdown-=_7._expiryTick;
if(_7.__remainingCountdown<=0){
_7._stopCountdown();
_7.tlw.curam.util.SessionTimeout.autoLogout();
_4.closeModalDialog();
return;
}
var _18=new Date(_7.__remainingCountdown);
var _19=""+_18.getSeconds(),_1a=(_19.length==1)?"0":"";
_16.innerHTML=_18.getMinutes();
_17.innerHTML=_1a+_19;
},_stopCountdown:function(){
_7.__remainingCountdown=0;
clearInterval(_7.expiryCountdown);
},waitForRedirection:function(){
dojo.subscribe("/curam/dialog/close",function(){
_7._redirectToLogoutWrapper();
});
},autoLogout:function(){
dojo.subscribe("/curam/dialog/close",function(){
_7._redirectLoginWithSessionExpiredMessage();
});
},_redirectToLogoutWrapper:function(){
_7._cfg.tlw.dojo.publish("curam/redirect/logout");
var _1b=_7._cfg.logoutPage||false;
if(!_1b){
return;
}
if(_7._cfg.isExternal){
_7._cfg.tlw.displayContent({pageID:_1b,param:[{paramKey:"invalidateSession",paramValue:true}]});
}else{
_7._redirectInternalLogoutWrapper(_1b);
}
},_redirectInternalLogoutWrapper:function(_1c){
_7._cfg.tlw.dialogOpenerRef=null;
if(_1c===_7._INTERNAL_LOGOUT_WRAPPER){
_1c+=".jspx?invalidateSession=true";
dojo.global.location=jsBaseURL+"/"+_1c;
}else{
var _1d=_4.getParentWindow();
_1c+="Page.do?invalidateSession=true";
if(_1d&&_1d.location!==_7._cfg.tlw.location){
_4.doRedirect(_1d,_1c,true);
}else{
curam.tab.getTabController().handleLinkClick(_1c);
}
}
},_redirectLoginWithSessionExpiredMessage:function(){
var _1e=_7._cfg.expiredUserMessageTxt||"";
localStorage[_7._EXPIRED_MESSAGE_KEY]=_1e;
_7._redirectToLogoutWrapper();
},resetAndStay:function(){
var _1f=_7._cfg.tlw.dojo.subscribe("/curam/dialog/close",function(){
if(_7._cfg.tlw.dialogOpenerRef){
var _20=_4.getParentWindow(window);
_20&&_20.focus();
_7._cfg.tlw.dialogOpenerRef=null;
}
_7._cfg.tlw.dojo.unsubscribe(_1f);
_7._stopCountdown();
_7.checkSessionExpired();
require(["curam/debug"],function(_21){
_21.log(_21.getProperty("continueApp"));
});
});
},createExpiredSessionMessageHTML:function(_22){
var _23=_6.readOption(_7._EXPIRED_MESSAGE_KEY);
if(_23){
messageContainerDOM=_1.byId(_22);
if(messageContainerDOM){
var _24="<div id='error-messages-container' class='wrapper-expired-message'>"+"<ul id='error-messages' class='messages'>"+"<li class='level-1'><div><span id='message'>"+_23+"</span></div></li></ul></div>";
_2.place(_2.toDom(_24),messageContainerDOM);
}
_6.clearOption(_7._EXPIRED_MESSAGE_KEY);
}
},displayUserMsgAsParagraphs:function(msg,_25){
var _26=_25||_1.byId(_7.userMessageNodeID);
var _27=msg.replace("\\n","[<p>]").replace("\n","[<p>]").split("[<p>]");
var _28=document.createDocumentFragment();
for(line in _27){
var _29=document.createElement("p");
_29.innerHTML=_27[line];
_28.appendChild(_29);
}
_2.place(_28,_26);
},});
var _7=curam.util.SessionTimeout;
_7.tlw=curam.util.getTopmostWindow()||window.top;
return _7;
});
