//>>built
define("curam/util/UimDialog",["curam/util/RuntimeContext","curam/util/external","curam/util","curam/define","curam/dialog","curam/util/DialogObject"],function(_1,_2){
curam.define.singleton("curam.util.UimDialog",{open:function(_3,_4,_5){
var _6=_3+curam.util.makeQueryString(_4);
return this.openUrl(_6,_5);
},openUrl:function(_7,_8){
var _9=curam.util.getCacheBusterParameter();
var _a=new curam.util.DialogObject(_9);
var _b=null;
if(_8){
_b="width="+_8.width+",height="+_8.height;
}
curam.util.openModalDialog({href:this._addRpu(_7)},_b,null,null,_9);
return _a;
},_addRpu:function(_c){
var _d=_c;
if(curam.tab.inTabbedUI()){
var _e=curam.tab.getContentPanelIframe();
if(_e){
_d=curam.util.setRpu(_c,new _1(_e.contentWindow));
}
}else{
if(_2.inExternalApp()){
var _f=_2.getUimParentWindow();
if(_f){
_d=curam.util.setRpu(_c,new _1(_f));
}
}
}
return _d;
},get:function(){
if(curam.dialog._id==null){
throw "Dialog infrastructure not ready.";
}
return new curam.util.DialogObject(null,curam.dialog._id);
},ready:function(_10){
if(curam.dialog._id==null){
dojo.subscribe("/curam/dialog/ready",_10);
}else{
_10();
}
},_getDialogFrameWindow:function(_11){
var _12=window.top.dijit.byId(_11);
return _12.uimController.getIFrame().contentWindow;
}});
return curam.util.UimDialog;
});
