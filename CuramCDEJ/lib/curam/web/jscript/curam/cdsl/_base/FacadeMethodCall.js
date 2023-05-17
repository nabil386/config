//>>built
define("curam/cdsl/_base/FacadeMethodCall",["dojo/_base/declare","dojo/_base/lang","dojo/json","dojo/_base/array"],function(_1,_2,_3,_4){
var _5=_1(null,{_intf:null,_method:null,_structs:null,_metadata:null,_options:null,constructor:function(_6,_7,_8,_9){
if(_8&&!_2.isArray(_8)){
throw new Error("Unexpected type of the 'structs' argument.");
}
this._intf=_6;
this._method=_7;
this._structs=_8?_8:[];
this._options={};
_2.mixin(this._options,{raw:true,formatted:false,sendCodetables:true,dataAdapter:null},_9);
},url:function(_a){
return _a+"/"+this._intf+"/"+this._method;
},_setMetadata:function(_b){
this._metadata=_b;
},toJson:function(){
var _c={service:this._intf,method:this._method,data:_4.map(this._structs,_2.hitch(this,function(_d){
_d.setDataAdapter(this._options.dataAdapter);
return _d.getData();
})),configOptions:{"response-type":this._responseType(),"send-codetables":this._sendCodetables()}};
if(this._metadata&&this._metadata.queryOptions){
_c.queryOptions=this._metadata.queryOptions;
}
return _3.stringify(_c);
},formatted:function(_e){
return this._getOrSet(_e,this._options,"formatted");
},raw:function(_f){
return this._getOrSet(_f,this._options,"raw");
},_responseType:function(){
if(this.raw()&&this.formatted()){
return "both";
}else{
if(this.raw()){
return "raw";
}else{
if(this.formatted()){
return "formatted";
}
}
}
throw new Error("Invalid response type: neither raw nor formatted was requested.");
},_sendCodetables:function(_10){
return this._getOrSet(_10,this._options,"sendCodetables");
},_getOrSet:function(_11,_12,_13){
if(typeof _11==="undefined"){
return _12[_13];
}else{
var _14=_12[_13];
_12[_13]=_11;
return _14;
}
},intf:function(){
return this._intf;
},method:function(){
return this._method;
},dataAdapter:function(_15){
if(!_15){
return this._options.dataAdapter;
}
this._options.dataAdapter=_15;
}});
return _5;
});
