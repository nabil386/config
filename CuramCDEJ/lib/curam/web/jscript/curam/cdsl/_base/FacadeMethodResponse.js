//>>built
define("curam/cdsl/_base/FacadeMethodResponse",["dojo/_base/declare","curam/cdsl/Struct","dojo/json","dojo/string"],function(_1,_2,_3,_4){
var _5="for(;;);";
var _6=function(_7,_8){
var _9=[],_a=_8?Array(_8+1).join("  "):"";
dojo.forEach(_7,function(e){
_9.push(_4.substitute("${indent}Type: ${type}\n"+"${indent}Message: ${msg}\n"+"${indent}Stack trace:\n"+"${indent}  ${stackTrace}",{type:e.type,msg:e.message,stackTrace:e.stackTrace,indent:_a}));
if(e.nestedError){
_9.push("\n-- nested error --");
_9.push(_6([e.nestedError],_8?_8+1:1));
}
});
return _9.join("\n");
};
var _b=_1(null,{_request:null,_data:null,_metadataRegistry:null,constructor:function(_c,_d,_e){
if(!_c||!_d){
throw new Error("Missing parameter.");
}
if(typeof _d=="string"){
this._data=_3.parse(_d.substr(_5.length,_d.length));
}else{
if(typeof _d=="object"){
this._data=_d;
}else{
throw new Error("Wrong parameter type: "+typeof _c+", "+typeof _d);
}
}
this._request=_c;
this._metadataRegistry=_e;
},returnValue:function(){
return new _2(this._data.data,{bareInput:true,fixups:this._data.metadata&&this._data.metadata.fixups?this._data.metadata.fixups:null,metadataRegistry:this._metadataRegistry,dataAdapter:this._request.dataAdapter()});
},failed:function(){
return this._data.code!==0;
},getError:function(){
var _f=this._data.errors;
if(_f){
var e=new Error("Server returned "+_f.length+(_f.length==1?" error":" errors")+".");
e.errors=_f;
e.toString=function(){
return _6(_f);
};
return e;
}
return null;
},hasCodetables:function(){
return this._data.metadata&&this._data.metadata.codetables&&this._data.metadata.codetables.length>0;
},getCodetablesData:function(){
return this._data.metadata.codetables;
},devMode:function(){
var dm=false;
if(this._data.metadata&&this._data.metadata.devMode){
dm=(this._data.metadata.devMode===true);
}
return dm;
},request:function(){
return this._request;
}});
return _b;
});
