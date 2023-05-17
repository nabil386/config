//>>built
define("curam/util/WordFileEdit",["dojo/has","dojo/dom","dojo/dom-style","dojo/dom-construct","curam/define","curam/debug","dijit/DialogUnderlay"],function(_1,_2,_3,_4,_5,_6){
isIE8OrGreater=function(){
return _1("ie")>=8||_1("trident");
};
_5.singleton("curam.util.WordFileEdit",{_clickedFinish:false,_buttonIdPart:"__o3btn.",searchWindow:null,cantLoadControlMsg:"$unlocalized$ cannot load Word integration control",cantSubmitMsg:"$unlocalized$ cannot submit data",searchWindowTitlePrefix:"SEARCH",useApplet:(function(){
return isIE8OrGreater();
})(),controlAttributes:{},controlParameters:{},submitSaveWordFileEdit:function(_7,_8){
try{
var _9=curam.util.WordFileEdit.getParentWindow();
var _a=curam.util.WordFileEdit._findTextArea(_9,_7);
_a.value=_8;
_9.document.forms[0].submit();
}
catch(e){
alert("Error saving: "+dojo.toJson(e));
}
return;
},openWordFileEditWindow:function(_b,_c,_d){
if(curam.util.WordFileEdit.getSearchPage().length>0){
curam.util.WordFileEdit.displaySearchWindow(_b,_c,_d);
}else{
curam.util.WordFileEdit.doOpenWordFileEditWindow(_b,_c,_d);
}
},doOpenWordFileEditWindow:function(_e,_f,_10){
var _11=100;
var _12=100;
var _13=Math.floor((screen.width-_11)/2);
var _14=Math.floor((screen.height-_12)/2);
window.open("../word-file-edit.jsp?id="+_e+"&document-field="+_f+"&details-field="+_10,new Date().valueOf(),"toolbar=no,menubar=no,location=no,scrollbars=no,"+"resizable=no,top="+_14+",left="+_13+",width="+_11+",height="+_12);
},displaySearchWindow:function(_15,_16,_17,_18){
if(!_18){
_18=0;
}
if(_18>3){
return;
}
if(_18==0){
curam.util.WordFileEdit.searchWindow=window.open("about:blank","searchWindow","left=40000,top=40000,scrollbars=yes");
}
var _19=false;
try{
var _1a=curam.util.WordFileEdit.searchWindow.document.title;
if(_1a.indexOf(searchWindowTitlePrefix+":")==-1){
curam.util.WordFileEdit.searchWindow.document.title=searchWindowTitlePrefix+":"+_15;
}else{
_19=true;
}
_1a=curam.util.WordFileEdit.searchWindow.document.title;
if(!_19&&_1a.indexOf(searchWindowTitlePrefix+":")!=-1){
_19=true;
}
}
catch(e){
}
if(!_19){
_18++;
window.setTimeout("displaySearchWindow('"+_15+"','"+_16+"','"+_17+"',"+_18+")",500);
}else{
curam.util.WordFileEdit.doOpenWordFileEditWindow(_15,_16,_17);
}
},redisplaySearchWindow:function(_1b,_1c){
if(!_1c){
_1c=0;
}
if(_1c>3){
return;
}
if(_1c==0){
curam.util.WordFileEdit.searchWindow=window.open("about:blank","searchWindow","left=40000,top=40000");
}
var _1d=false;
try{
var _1e=curam.util.WordFileEdit.searchWindow.document.title;
if(_1e.indexOf(searchWindowTitlePrefix+":")==-1){
curam.util.WordFileEdit.searchWindow.document.title=searchWindowTitlePrefix+":"+_1b;
}else{
_1d=true;
}
_1e=curam.util.WordFileEdit.searchWindow.document.title;
if(!_1d&&_1e.indexOf(searchWindowTitlePrefix+":")!=-1){
_1d=true;
}
}
catch(e){
}
if(!_1d){
_1c++;
window.setTimeout("redisplaySearchWindow('"+_1b+"',"+_1c+")",500);
}
},getSearchPage:function(_1f){
var _20="";
try{
if(!_1f){
_20=document.getElementById("searchPage").value;
}else{
var _21=curam.util.WordFileEdit.getParentWindow();
_20=_21.document.getElementById("searchPage").value;
}
}
catch(e){
}
return _20;
},initialize:function(_22){
var _23=curam.util.WordFileEdit.getParentWindow();
try{
var _24=_2.byId(_22);
if(typeof _24!="undefined"){
curam.util.WordFileEdit._setOverlay(true);
if(curam.util.WordFileEdit.useApplet){
if(!isIE8OrGreater()){
var _25=_23.frameElement;
curam.util.connect(_25,"onload",function(evt){
dojo.fixEvent(evt,_25);
var url=_25.contentWindow.location.href;
try{
_24.mainApplicationPageLoaded(url);
}
catch(e){
alert("Error calling mainApplicationPageLoaded on applet: "+e.message);
}
});
_23.top.dojo.addOnUnload(function(){
_24.mainApplicationPageUnloaded();
});
}
}else{
_24.openDocument();
}
}else{
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
}
catch(e){
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
_23.curam.util.WordFileEdit.cannotLoadControl(e);
}
},_setOverlay:function(_26){
_6.log(_6.getProperty("curam.util.WordFileEdit.version"),"6.0");
},cannotLoadControl:function(e){
var msg=isIE8OrGreater()&&!curam.util.WordFileEdit.useApplet?curam.util.WordFileEdit.cantLoadControlMsgIE:curam.util.WordFileEdit.cantLoadControlMsg;
alert(msg+"\rERROR: "+e.message);
history.go(-1);
},setStatusTextWordFileEditWindow:function(_27){
try{
document.getElementById("statustext").innerHTML=_27;
}
catch(e){
}
},getWordFileEditParentTextareaValue:function(_28){
var _29="";
try{
var _2a=curam.util.WordFileEdit.getParentWindow();
var _2b=curam.util.WordFileEdit._findTextArea(_2a,_28);
_29=_2b.value;
}
catch(e){
alert("getWordFileEditParentTextareaValue('"+_28+"'): \r"+e.message);
}
return _29;
},_findTextArea:function(_2c,_2d,_2e){
var _2f=null;
if(!_2e){
_2f=_2c.dojo.query("input[name='"+_2d+"']",_2c.dojo.body())[0];
}else{
_2f=_2c.dojo.query("input[name$='"+_2d+"']",_2c.dojo.body())[0];
}
return _2f;
},finishedWordFileEditWindow:function(_30,_31,_32){
if(!curam.util.WordFileEdit._clickedFinish){
curam.util.WordFileEdit.doFinishWordFileEditWindow(_30,_31,_32);
curam.util.WordFileEdit._clickedFinish=true;
}
},doFinishWordFileEditWindow:function(_33,_34,_35){
var _36=false;
var _37=false;
try{
var _38=curam.util.WordFileEdit.getParentWindow();
if(_34&&_35){
_37=true;
var _39=curam.util.WordFileEdit._findTextArea(_38,_34);
_39.value=_35;
}
var _3a=_38.dojo.query("form input");
for(var i=0;i<_3a.length&&!_36;i++){
if(_3a[i].id.substring(0,curam.util.WordFileEdit._buttonIdPart.length).toLowerCase()==curam.util.WordFileEdit._buttonIdPart.toLowerCase()){
_36=true;
if(!_37){
var _39=curam.util.WordFileEdit._findTextArea(_38,_34);
_39.value="";
var _3b=false;
var _3c;
var _3d=_3a[i];
try{
while(_3d.tagName.toUpperCase()!="BODY"&&!_3b){
if(_3d.tagName.toUpperCase()=="FORM"){
_3b=true;
_3c=_3d;
}else{
_3d=_3d.parentElement;
}
}
}
catch(e){
alert("doFinishWordFileEditWindow: "+e.message);
}
if(_3b){
var _3e="<input type=\"hidden\" name=\"__o3NoSave\" value=\"true\"/>";
_3c.innerHTML+=_3e;
}
}
_38.curam.util.clickButton(_3a[i].id);
if(_33.length>0){
_38.document.body.innerHTML=_33;
}
curam.util.WordFileEdit._setOverlay(false);
return;
}
}
if(!_36){
alert(curam.util.WordFileEdit.cantSubmitMsg);
try{
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
catch(e){
}
}
}
catch(e){
alert("doFinishWordFileEditWindow: "+e.message);
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
},screenAlertWordFileEditWindow:function(_3f){
try{
curam.util.WordFileEdit.getParentWindow().alert(_3f);
}
catch(e){
}
},hideSubmitButtons:function(){
dojo.query("a.ac").forEach(function(_40){
_40.style.display="none";
});
},getParentWindow:function(){
return window.opener;
},getUrls:function(){
try{
var _41=curam.util.WordFileEdit.getParentWindow();
var doc=_41.document;
var _42=doc.URL;
var _43=_41.dojo.query("form",doc)[0];
var _44=_43.action;
var _45=_42.substr(0,_42.lastIndexOf("/")+1);
window.curam.util.WordFileEdit.urlPath_return_value=_45;
var _46=isIE8OrGreater()?_44:_45+_44;
window.curam.util.WordFileEdit.allowedUrl_return_value=_46;
return [_45,_46];
}
catch(e){
alert("getUrls: "+dojo.toJson(e));
}
},getTitle:function(){
var _47=curam.util.WordFileEdit.getParentWindow().top.document.title;
curam.util.WordFileEdit.title_return_value=_47;
window.curam_wordIntegration_title_return_value=_47;
return _47;
},setTitle:function(_48){
curam.util.WordFileEdit.getParentWindow().top.document.title=_48;
},hasNamedInput:function(_49){
var _4a=curam.util.WordFileEdit.getParentWindow();
var _4b=_49.slice(1);
var _4c=curam.util.WordFileEdit._findTextArea(_4a,_4b,true);
return _4c?true:false;
},closeAppletWindow:function(){
self.close();
},runApplet:function(id){
if(typeof deployJava!="undefined"){
var _4d=deployJava.getPlugin();
if(_4d){
_6.log(_6.getProperty("curam.util.WordFileEdit.version"),_4d.version);
}else{
_6.log(_6.getProperty("curam.util.WordFileEdit.no.plugin"));
}
}else{
_6.log(_6.getProperty("curam.util.WordFileEdit.no.java"));
}
if(typeof deployJava=="undefined"||(!dojo.isChrome&&!deployJava.isPlugin2())){
alert(curam.util.WordFileEdit.noJavaInstalled);
}else{
dojo.mixin(curam.util.WordFileEdit.controlAttributes,{id:id});
var _4e=_4.create("div",{style:"display:none"});
var _4f=_4.create("applet",curam.util.WordFileEdit.controlAttributes,_4e);
var _50=curam.util.WordFileEdit.controlParameters;
for(property in _50){
_4.create("param",{name:property,value:_50[property]},_4f);
}
var _51=_4e.innerHTML;
_4.destroy(_4e);
document.write(_51);
}
}});
return curam.util.WordFileEdit;
});
