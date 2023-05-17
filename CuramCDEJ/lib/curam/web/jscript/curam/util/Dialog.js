//>>built
define("curam/util/Dialog",["curam/util","curam/define","curam/dialog","dojo/on","curam/util/onLoad","curam/debug"],function(_1,_2,_3,on,_4,_5){
_2.singleton("curam.util.Dialog",{_id:null,_unsubscribes:[],_modalWithIEGScript:false,open:function(_6,_7,_8){
var _9=_6+curam.util.makeQueryString(_7);
var _a={href:_9};
var _b=null;
if(_8){
_b="width="+_8.width+",height="+_8.height;
}
window.jsModals=true;
curam.util.openModalDialog(_a,_b);
},init:function(){
var _c=curam.util.getTopmostWindow();
var _d=_c.dojo.subscribe("/curam/dialog/SetId",null,function(_e){
curam.util.Dialog._id=_e;
curam.debug.log(_5.getProperty("curam.util.Dialog.id.success"),curam.util.Dialog._id);
_c.dojo.unsubscribe(_d);
});
curam.util.Dialog._unsubscribes.push(_d);
_c.dojo.publish("/curam/dialog/init");
if(!curam.util.Dialog._id){
curam.debug.log(_5.getProperty("curam.util.Dialog.id.fail"));
}
if(curam.util.Dialog._modalWithIEGScript){
dojo.addOnUnload(function(){
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}else{
var _f=on(window,"unload",function(){
_f.remove();
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}
},initModalWithIEGScript:function(){
curam.util.Dialog._modalWithIEGScript=true;
curam.util.Dialog.init();
},registerGetTitleFunc:function(_10){
curam.util.onLoad.addPublisher(function(_11){
_11.title=_10();
});
},registerGetSizeFunc:function(_12){
curam.util.onLoad.addPublisher(function(_13){
_13.windowOptions=_12();
});
},registerAfterDisplayHandler:function(_14){
var _15=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_15.dojo.subscribe("/curam/dialog/AfterDisplay",null,function(_16){
if(_16==curam.util.Dialog._id){
_14();
}
}));
},registerBeforeCloseHandler:function(_17){
var _18=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_18.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_19){
if(_19===curam.util.Dialog._id){
_17();
}
}));
},pageLoadFinished:function(){
var _1a=curam.util.getTopmostWindow();
curam.util.Dialog._unsTokenReleaseHandlers=_1a.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_1b){
if(_1b==curam.util.Dialog._id){
curam.util.Dialog._releaseHandlers(_1a);
}
});
curam.util.onLoad.execute();
},_releaseHandlers:function(_1c){
var _1d;
if(_1c){
_1d=_1c;
}else{
_1d=curam.util.getTopmostWindow();
}
dojo.forEach(curam.util.Dialog._unsubscribes,_1d.dojo.unsubscribe);
curam.util.Dialog._unsubscribes=[];
_1d.dojo.unsubscribe(curam.util.Dialog._unsTokenReleaseHandlers);
curam.util.Dialog._unsTokenReleaseHandlers=null;
},close:function(_1e,_1f,_20){
var _21=curam.dialog.getParentWindow(window);
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _22=window.frameElement.id;
var _23=_22.substring(7);
curam.util.Dialog._id=_23;
_5.log("curam.util.Dialog.closeAndSubmitParent() "+_5.getProperty("curam.dialog.modal.id")+_23);
}
if(_1e&&!_1f){
curam.dialog.forceParentRefresh();
_21.curam.util.redirectWindow(null);
}else{
if(_1f){
var _24=_1f;
if(_1f.indexOf("Page.do")==-1&&_1f.indexOf("Action.do")==-1){
_24=_1f+"Page.do"+curam.util.makeQueryString(_20);
}
_21.curam.util.redirectWindow(_24);
}
}
var _25=curam.util.getTopmostWindow();
_25.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},closeAndSubmitParent:function(_26){
var _27=curam.dialog.getParentWindow(window);
var _28=_27.document.forms["mainForm"];
var _29=curam.util.getTopmostWindow();
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _2a=window.frameElement.id;
var _2b=_2a.substring(7);
curam.util.Dialog._id=_2b;
_5.log("curam.util.Dialog.closeAndSubmitParent() "+_5.getProperty("curam.dialog.modal.id")+_2b);
}
if(_28==null||_28==undefined){
_29.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
return;
}
var _2c=function(_2d){
for(var _2e in _2d){
if(_2d.hasOwnProperty(_2e)){
return false;
}
}
return true;
};
if(_26&&!_2c(_26)){
var _2f=dojo.query("#"+_28.id+" > "+"input[type=text]");
var _30=dojo.filter(_2f,function(_31){
return _31.readOnly==false;
});
dojo.forEach(_30,function(_32){
_32.value="";
});
for(var _33 in _26){
var _34=_30[parseInt(_33)];
if(_34){
var _35=dojo.query("input[name="+_34.id+"]",_28)[0];
if(_35){
_35.value=_26[_33];
}else{
_34.value=_26[_33];
}
}
}
}else{
}
_27.dojo.publish("/curam/page/refresh");
_28.submit();
_29.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},fileDownloadAnchorHandler:function(url){
return curam.util.fileDownloadAnchorHandler(url);
}});
});
