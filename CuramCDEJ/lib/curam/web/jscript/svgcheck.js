dojo.require("curam.util.ResourceBundle");
dojo.requireLocalization("curam.application","SVGText");
var bundle=new curam.util.ResourceBundle("SVGText");
function getBrowser(){
var _1=navigator.userAgent.toLowerCase();
var _2=parseInt(navigator.appVersion);
var _3=parseFloat(navigator.appVersion);
is_nav=((_1.indexOf("mozilla")!=-1)&&(_1.indexOf("spoofer")==-1)&&(_1.indexOf("compatible")==-1)&&(_1.indexOf("opera")==-1)&&(_1.indexOf("webtv")==-1));
is_nav3=(is_nav&&(_2==3));
is_nav4up=(is_nav&&(_2>=4));
is_nav407up=(is_nav&&(_3>=4.07));
is_nav408up=(is_nav&&(_3>=4.08));
is_ie=(_1.indexOf("msie")!=-1);
is_ie3=(is_ie&&(_2<4));
is_ie4=(is_ie&&(_2==4)&&(_1.indexOf("msie 5.0")==-1));
is_ie4up=(is_ie&&(_2>=4));
is_ie5=(is_ie&&(_2==4)&&(_1.indexOf("msie 5.0")!=-1));
is_ie5up=(is_ie&&!is_ie3&&!is_ie4);
is_win=((_1.indexOf("win")!=-1)||(_1.indexOf("16bit")!=-1));
is_win95=((_1.indexOf("win95")!=-1)||(_1.indexOf("windows 95")!=-1));
is_win98=((_1.indexOf("win98")!=-1)||(_1.indexOf("windows 98")!=-1));
is_winnt=((_1.indexOf("winnt")!=-1)||(_1.indexOf("windows nt")!=-1));
is_win32=(is_win95||is_winnt||is_win98||((_2>=4)&&(navigator.platform=="Win32"))||(_1.indexOf("win32")!=-1)||(_1.indexOf("32bit")!=-1));
is_mac=(_1.indexOf("mac")!=-1);
is_macPPC=(is_mac&&((_1.indexOf("ppc")!=-1)||(_1.indexOf("powerpc")!=-1)));
};
function setCookie(_4,_5,_6,_7,_8,_9){
var _a=_4+"="+escape(_5)+((_6)?"; expires="+_6.toGMTString():"")+((_7)?"; path="+_7:"")+((_8)?"; domain="+_8:"")+((_9)?"; secure":"");
document.cookie=_a;
};
function getCookie(_b){
var dc=document.cookie;
var _c=_b+"=";
var _d=dc.indexOf("; "+_c);
if(_d==-1){
_d=dc.indexOf(_c);
if(_d!=0){
return null;
}
}else{
_d+=2;
}
var _e=document.cookie.indexOf(";",_d);
if(_e==-1){
_e=dc.length;
}
return unescape(dc.substring(_d+_c.length,_e));
};
function deleteCookie(_f,_10,_11){
if(getCookie(_f)){
document.cookie=_f+"="+((_10)?"; path="+_10:"")+((_11)?"; domain="+_11:"")+"; expires=Thu, 01-Jan-70 00:00:01 GMT";
}
};
function fixDate(_12){
var _13=new Date(0);
var _14=_13.getTime();
if(_14>0){
_12.setTime(_12.getTime()-_14);
}
};
var svgInstallBase="http://www.adobe.com/svg/viewer/install/";
var svgInstallPage=svgInstallBase+"auto/";
var svgInfoPage="http://www.adobe.com/svg/";
var svgDownloadPage=svgInstallBase;
var checkIntervalDays=30;
var firstSVG=true;
var nativeSVG;
function getSVGInstallPage(){
return svgInstallPage+"?"+location;
};
function getCheckInterval(){
return checkIntervalDays*24*60*60*1000;
};
function setSVGCookie(){
if(getCheckInterval()>0){
var _15=new Date();
fixDate(_15);
_15.setTime(_15.getTime()+getCheckInterval());
setCookie("SVGCheck","0",_15,"/");
}
};
function isSVGPluginInstalled(){
return (navigator.mimeTypes["image/svg"]&&navigator.mimeTypes["image/svg"].enabledPlugin!=null)||(navigator.mimeTypes["image/svg-xml"]&&navigator.mimeTypes["image/svg-xml"].enabledPlugin!=null);
};
function checkSVGViewer(){
if(isNativeSVG()){
window.svgViewerAvailable=true;
return;
}
window.askForSVGViewer=false;
if(window.svgInstalled){
return;
}
getBrowser();
if(is_win32&&is_ie4up){
window.svgViewerAvailable=true;
window.svgInstalled=isSVGControlInstalled();
if(!window.svgInstalled){
window.askForSVGViewer=true;
}
}else{
if((is_win32&&is_nav4up)||(is_macPPC&&is_nav407up)){
window.svgViewerAvailable=true;
window.svgInstalled=isSVGPluginInstalled();
if(!window.svgInstalled&&is_nav408up&&navigator.javaEnabled()){
window.askForSVGViewer=true;
}
}else{
if(is_macPPC&&is_ie5up){
window.svgViewerAvailable=true;
}
}
}
};
function getSVGViewer(){
if(confirm(bundle.getProperty("VIEWER_INSTALL_MESSAGE"))){
location=getSVGInstallPage();
}
};
function checkAndGetSVGViewer(){
if(dojo.isIE){
checkSVGViewer();
var _16=getCookie("SVGCheck");
if(firstSVG&&!_16){
if(window.askForSVGViewer){
setSVGCookie();
getSVGViewer();
}
firstSVG=false;
}
}
};
function checkViewerVersion(_17){
if(!window||!window.navigator||!window.navigator.appVersion||window.navigator.appVersion.indexOf("3.0")==-1){
if(_17){
alert(bundle.getProperty("WRONG_ASV_VERSION"));
}
return false;
}else{
if(!checkJScriptVersion()){
if(_17){
alert("Incorrect version of JScript Engine.");
}
return false;
}
return true;
}
};
function checkJScriptVersion(){
var _18;
var _19;
if(is_ie){
_18=ScriptEngineMajorVersion();
_19=ScriptEngineMinorVersion();
if(_18<5){
return false;
}else{
if(_19<5){
return false;
}
}
}
return true;
};
function emitSVG(_1a){
if(isNativeSVG()||window.svgInstalled){
document.writeln("<embed "+_1a+" iebrowser=\""+dojo.ieIE+"\">");
}else{
if(window.askForSVGViewer){
if(navigator.appName=="Netscape"){
document.writeln(bundle.getProperty("NETSCAPE_VIEWER_INSTALL_MESSAGE_ONE"));
document.writeln("<a href=\""+getSVGInstallPage()+"\">"+bundle.getProperty("NETSCAPE_VIEWER_INSTALL_MESSAGE_TWO"));
}else{
document.writeln("<embed "+_1a+" pluginspage=\""+getSVGInstallPage()+"\" iebrowser=\"true\">");
}
}else{
if(window.svgViewerAvailable){
document.writeln("<embed "+_1a+" pluginspage=\""+svgDownloadPage+"\" iebrowser=\"true\">");
}else{
document.writeln(bundle.getProperty("NETSCAPE_VIEWER_INSTALL_MESSAGE_ONE")+" "+bundle.getProperty("NO_VIEWER_AVAILABLE"));
document.writeln("<a href=\""+svgInfoPage+"\">"+bundle.getProperty("NETSCAPE_VIEWER_INSTALL_MESSAGE_TWO"));
}
}
}
var _1b=document.getElementById("__o3__SVGWorkflow");
if(_1b!=null){
var _1c=dojo.query("table",_1b.domNode)[0];
_1c.style.tableLayout="auto";
}
};
function isNativeSVG(){
if(!nativeSVG){
try{
nativeSVG=(!!document.createElementNS&&!!document.createElementNS("http://www.w3.org/2000/svg","svg").createSVGRect);
}
catch(e){
nativeSVG=false;
}
}
return nativeSVG;
};

