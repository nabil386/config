//>>built
define("curam/debug",["curam/define","curam/util/LocalConfig","dojo/ready","dojo/_base/lang","curam/util/ResourceBundle"],function(_1,_2,_3,_4,_5){
var _6=new _5("curam.application.Debug");
_1.singleton("curam.debug",{log:function(){
if(curam.debug.enabled()){
try{
var a=arguments;
if(!dojo.isIE){
console.log.apply(console,a);
}else{
var _7=a.length;
var sa=curam.debug._serializeArgument;
switch(_7){
case 1:
console.log(arguments[0]);
break;
case 2:
console.log(a[0],sa(a[1]));
break;
case 3:
console.log(a[0],sa(a[1]),sa(a[2]));
break;
case 4:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]));
break;
case 5:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]));
break;
case 6:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
break;
default:
console.log("[Incomplete message - "+(_7-5)+" message a truncated] "+a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
}
}
}
catch(e){
console.log(e);
}
}
},getProperty:function(_8,_9){
return _6.getProperty(_8,_9);
},_serializeArgument:function(_a){
if(typeof _a!="undefined"&&typeof _a.nodeType!="undefined"&&typeof _a.cloneNode!="undefined"){
return ""+_a;
}else{
if(curam.debug._isWindow(_a)){
return _a.location.href;
}else{
if(curam.debug._isArray(_a)&&curam.debug._isWindow(_a[0])){
return "[array of window objects, length "+_a.length+"]";
}else{
return dojo.toJson(_a);
}
}
}
},_isArray:function(_b){
return typeof _b!="undefined"&&(dojo.isArray(_b)||typeof _b.length!="undefined");
},_isWindow:function(_c){
var _d=typeof _c!="undefined"&&typeof _c.closed!="undefined"&&_c.closed;
if(_d){
return true;
}else{
return typeof _c!="undefined"&&typeof _c.location!="undefined"&&typeof _c.navigator!="undefined"&&typeof _c.document!="undefined"&&typeof _c.closed!="undefined";
}
},enabled:function(){
return _2.readOption("jsTraceLog","false")=="true";
},_setup:function(_e){
_2.seedOption("jsTraceLog",_e.trace,"false");
_2.seedOption("ajaxDebugMode",_e.ajaxDebug,"false");
_2.seedOption("asyncProgressMonitor",_e.asyncProgressMonitor,"false");
}});
return curam.debug;
});
