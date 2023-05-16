//>>built
define("curam/util/UIMFragment",["curam/util/Request","curam/define","curam/debug","curam/util/ScreenContext"],function(_1){
curam.define.singleton("curam.util.UIMFragment",{get:function(_2){
var _3=_2&&_2.pageID;
var _4=_2&&_2.url;
var _5=_2&&_2.params;
var _6=_2&&_2.onLoad;
var _7=_2&&_2.onDownloadError;
var _8=_2&&_2.targetID;
if(_8===""||typeof _8==="undefined"){
throw "UIMFragment: targetID must be set.";
}
var _9=null;
if(_4){
_9=_4;
}else{
_9=curam.util.UIMFragment._constructPath(_3)+curam.util.UIMFragment._addCDEJParameters()+curam.util.UIMFragment._encodeParameters(_5);
}
curam.debug.log("UIMFragment: GET to "+_9);
curam.util.UIMFragment._doService(_9,_8,_2,_6,_7);
},submitForm:function(_a){
var _a=dojo.fixEvent(_a);
var _b=_a.target;
dojo.stopEvent(_a);
var _c={url:curam.util.UIMFragment._constructFormActionPath(_b),form:_b,load:function(_d){
var cp=dijit.getEnclosingWidget(_b);
cp.set("content",_d);
},error:function(_e){
alert("form error: error!!");
}};
_1.post(_c);
console.log(_a+" "+_b);
},_constructFormActionPath:function(_f){
var _10="";
if(window===window.top){
_10=curam.config.locale+"/";
}
return _10+_f.getAttribute("action");
},_initForm:function(_11){
var _12=dojo.query("form",dijit.byId(_11).domNode)[0];
if(_12){
dojo.connect(_12,"onsubmit",curam.util.UIMFragment.submitForm);
}
},_constructPath:function(_13){
var _14=window;
var _15=window.top;
return curam.util.UIMFragment._constructPathValue(_13,_14,_15);
},_constructPathValue:function(_16,_17,_18){
if(_16===""||typeof _16==="undefined"){
throw "UIMFragment: pageID must be set.";
}
var _19="";
if(_17.location.pathname===_18.location.pathname){
var _1a=_18.curam&&_18.curam.config&&_18.curam.config.locale;
_19=(_1a||"en")+"/";
}
return _19+_16+"Page.do";
},_encodeParameters:function(_1b){
if(typeof _1b==="undefined"||dojo.toJson(_1b)==="{}"){
curam.debug.log("UIMFragment: No params included in request.");
return "";
}
var _1c=[];
for(var _1d in _1b){
_1c.push(_1d+"="+encodeURIComponent(_1b[_1d]));
}
return "&"+_1c.join("&");
},_addCDEJParameters:function(){
return "?"+jsScreenContext.toRequestString();
},_doService:function(url,_1e,_1f,_20,_21){
var cp=dijit.byId(_1e);
cp.onLoad=dojo.hitch(cp,curam.util.UIMFragment._handleLoadSuccess,_1f,_20);
cp.preventCache=true;
cp.set("href",url);
},_handleDownloadError:function(_22){
curam.debug.log("Error invoking the UIMFragment: "+_22);
return "UIMFragment: Generic Error Handler";
},_handleLoadSuccess:function(_23,_24){
curam.util.UIMFragment._initForm(_23.targetID);
if(_24){
_24(this);
}
curam.debug.log("");
return "UIMFragment: Generic Success Handler";
}});
return curam.util.UIMFragment;
});
