//>>built
define("curam/util/DialogObject",["dojo/_base/declare","curam/dialog","curam/util"],function(_1){
var _2=_1("curam.util.DialogObject",null,{_id:null,constructor:function(_3,id){
if(!id){
var _4=window.top.dojo.subscribe("/curam/dialog/uim/opened/"+_3,this,function(_5){
this._id=_5;
window.top.dojo.unsubscribe(_4);
});
}else{
this._id=id;
}
},registerBeforeCloseHandler:function(_6){
var _7=window.top.dojo.subscribe("/curam/dialog/BeforeClose",this,function(_8){
if(_8==this._id){
window.top.dojo.unsubscribe(_7);
_6();
}
});
},registerOnDisplayHandler:function(_9){
if(curam.dialog._displayed==true){
_9(curam.dialog._size);
}else{
var ut=window.top.dojo.subscribe("/curam/dialog/displayed",this,function(_a,_b){
if(_a==this._id){
window.top.dojo.unsubscribe(ut);
_9(_b);
}
});
}
},close:function(_c,_d,_e){
var _f=curam.util.UimDialog._getDialogFrameWindow(this._id);
var _10=_f.curam.dialog.getParentWindow(_f);
if(_c&&!_d){
_f.curam.dialog.forceParentRefresh();
curam.dialog.doRedirect(_10,null);
}else{
if(_d){
var _11=_d;
if(_d.indexOf("Page.do")==-1){
_11=_d+"Page.do"+curam.util.makeQueryString(_e);
}
curam.dialog.doRedirect(_10,_11);
}
}
curam.dialog.closeModalDialog();
}});
return _2;
});
