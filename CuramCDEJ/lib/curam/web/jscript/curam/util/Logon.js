//>>built
define("curam/util/Logon",["dojo/query","dojo/ready","curam/define","curam/util"],function(_1,_2){
curam.define.singleton("curam.util.Logon",{ensureFullPageLogon:function(){
var w=curam.util.Logon.getCurrentWindow();
while(w.parent&&w.parent!=w){
w=w.parent;
}
if(w&&w!=curam.util.Logon.getCurrentWindow()){
curam.util.Logon.getCurrentWindow().location.reload(false);
}
_2(function(){
var _3=_1(".contentPanelFrameWrapper",curam.util.getTopmostWindow().document.body);
var _4=_1("form[action=\"j_security_check\"]",curam.util.Logon.getCurrentWindow().document.body);
if(_3.length>0&&_4.length>0){
curam.util.getTopmostWindow().location.reload(false);
}
});
},getCurrentWindow:function(){
return window;
}});
return curam.util.Logon;
});
