//>>built
define("curam/util/ui/AppExitConfirmation",["dojo/on","dojo/keys","dojo/query","dojo/dom-attr","dojo/window","dojo/sniff","dojo/topic","dojo/dom-class","curam/debug","curam/util","dojo/NodeList-traverse"],function(on,_1,_2,_3,_4,_5,_6,_7,_8){
var _9=_AEX=dojo.setObject("curam.util.ui.AppExitConfirmation",{_OPEN_BRACKET_KEY_CODE:219,_W_KEY_CODE:87,_MILLISECONDS_TIME:3000,handlers:[],tracker:null,cMessage:_8.getProperty("curam.util.ui.AppExitConfirmation.mesg"),install:function(){
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
var _a=(e||window.event).target.nodeName;
var _b=_AEX._isClickOnAction(e);
_AEX.tracker.hasClickedOnWindow=(hr&&/^mailto\:|directLink/.test(hr))||_b;
}));
var uh=on(window,"unload",function(){
_AEX.uninstall();
uh.remove();
});
},_showAppExitConfirmationDialog:function(_c){
var e=_c||window.event;
e.returnValue=_AEX.cMessage;
e.preventDefault();
if(_5("ie")||_5("trident")||_5("edge")){
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
var _d=e.keyCode||e.which,_e=e.metaKey||e.ctrlKey||e.altKey||e.shiftKey;
if(_d==_1.ENTER){
_AEX.tracker.isPointerOnWindow=true;
}else{
if(_d===_1.BACKSPACE||((_d==_AEX._OPEN_BRACKET_KEY_CODE||_d==_1.LEFT_ARROW||_d==_AEX._W_KEY_CODE)&&_e)){
var el=e.target.nodeName.toLowerCase(),_f=/input|textarea/.test(el);
if(!_f||_3.get(_2(e.target),"readonly")){
_AEX.tracker.isPointerOnWindow=false;
}
}
}
},_isClickOnAction:function(e){
var _10=e.target;
switch(_10.tagName){
case "INPUT":
if((_3.get(_10,"type")=="submit"&&typeof _10.form!="undefined")||_3.get(_10,"type")=="button"){
return true;
}
case "IMG":
case "SPAN":
case "DIV":
_10=_2(_10).closest("A")[0];
if(_10==null){
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
var _11=_10.getAttribute("href");
if(_11==""){
return true;
}
if(!_11){
return false;
}
if(_11.indexOf("javascript")==0){
return true;
}
if(_11&&_11.indexOf("/servlet/FileDownload?")>-1){
return true;
}
if(_7.contains(_10,"external-link")){
return true;
}
return false;
}});
_AEX.tlw=curam.util.getTopmostWindow()||window.top;
return _9;
});
