//>>built
define("curam/util/Request",["dojo/_base/xhr","curam/debug","curam/util/ResourceBundle","curam/inspection/Layer","curam/util/LocalConfig"],function(_1,_2,_3,_4,_5){
var _6=new _3("curam.application.Request");
_isLoginPage=null,isLoginPage=function(_7){
if(_isLoginPage){
return _isLoginPage(_7);
}else{
return _7.responseText.indexOf("action=\"j_security_check\"")>0;
}
},errorDisplayHookpoint=function(_8,_9){
if(isLoginPage(_9.xhr)){
_2.log(_6.getProperty("sessionExpired"));
}else{
_2.log(_6.getProperty("ajaxError"));
}
_2.log(_8);
_2.log("HTTP status was: "+_9.xhr.status);
},_xhr=function(_a,_b){
var _c=_5.readOption("ajaxDebugMode","false")=="true";
var _d=_b.error;
if(_c){
_b.error=function(_e,_f){
if(_b.errorHandlerOverrideDefault!==true){
errorDisplayHookpoint(_e,_f);
}
if(_d){
_d(_e,_f);
}
};
}
var _10=_a(_b);
return _10;
};
var _11={post:function(_12){
return _xhr(_1.post,_12);
},get:function(_13){
return _xhr(_1.get,_13);
},setLoginPageDetector:function(_14){
_isLoginPage=_14;
},checkLoginPage:function(_15){
return isLoginPage(_15);
}};
_4.register("curam/util/Request",_11);
return _11;
});
