//>>built
define("curam/util/DataServiceAPI",["curam/util/Request","curam/define","curam/debug"],function(_1,_2,_3){
_2.singleton("curam.util.DataServiceAPI",{getDataService:function(_4,_5,_6,_7,_8,_9){
var _a=curam.util.DataServiceAPI._constructPath(_4)+curam.util.DataServiceAPI._encodeParameters(_5);
curam.debug.log(_3.getProperty("curam.util.DataServiceAPI.get"));
curam.util.DataServiceAPI._doDataService("GET",_a,undefined,_6,_7,_8,_9);
},postDataService:function(_b,_c,_d,_e,_f,_10){
var _11=curam.util.DataServiceAPI._constructPath(_b);
curam.debug.log(_3.getProperty("curam.util.DataServiceAPI.post"));
curam.util.DataServiceAPI._doDataService("POST",_11,_c,_d,_e,_f,_10);
},_constructPath:function(_12){
var _13=window;
var _14=curam.util.getTopmostWindow();
return curam.util.DataServiceAPI._constructPathValue(_12,_13,_14);
},_constructPathValue:function(_15,_16,_17){
if(_15===""||typeof _15==="undefined"){
throw "Data Service: pageId must be set.";
}
var _18="";
if(_16.location.pathname===_17.location.pathname){
var _19=_17.curam&&_17.curam.config&&_17.curam.config.locale;
_18=(_19||"en")+"/";
}
return _18+_15+"Page.do";
},_encodeParameters:function(_1a){
if(typeof _1a==="undefined"||dojo.toJson(_1a)==="{}"){
curam.debug.log(_3.getProperty("curam.util.DataServiceAPI.no.params"));
return "";
}
var _1b=[];
for(var _1c in _1a){
_1b.push(_1c+"="+encodeURIComponent(_1a[_1c]));
}
return "?"+_1b.join("&");
},_doDataService:function(_1d,_1e,_1f,_20,_21,_22,_23){
if(typeof _21==="undefined"){
_21=dojo.hitch(this,this._handleDataServiceError);
}
if(typeof _22==="undefined"){
_22=dojo.hitch(this,this._handleDataServiceCallback);
}
if(typeof _20==="undefined"||_20==null){
_20=dojo.hitch(this,this._handleDataServiceSuccess);
}
if(_1d==="GET"){
_1.get({url:_1e,headers:{"Content-Encoding":"UTF-8"},handleAs:(_23||"json"),load:_20,error:_21,handle:_22});
}else{
_1.post({url:_1e,headers:{"Content-Encoding":"UTF-8"},handleAs:(_23||"json"),preventCache:true,load:_20,error:_21,handle:_22,content:(_1f||"")});
}
},_handleDataServiceError:function(_24,_25){
var _26=_3.getProperty("curam.util.DataServiceAPI.error.1");
var _27=_3.getProperty("curam.util.DataServiceAPI.error.2");
curam.debug.log(_26+_24+_27+_25);
return "Data Service: Generic Error Handler";
},_handleDataServiceSuccess:function(_28,_29){
curam.debug.log("curam.util.DataServiceAPI._handleDataServiceSuccess : "+_28);
return "Data Service: Generic Success Handler";
},_handleDataServiceCallback:function(_2a,_2b){
curam.debug.log("curam.util.DataServiceAPI._handleDataServiceCallback : "+_2a);
return "Data Service: Generic Handler";
}});
return curam.util.DataServiceAPI;
});
