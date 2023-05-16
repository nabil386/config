//>>built
define("curam/cdsl/_base/_StructBase",["dojo/_base/declare","dojo/_base/lang","dojo/json","curam/cdsl/types/FrequencyPattern"],function(_1,_2,_3,_4){
var _5={bareInput:false,fixups:null,metadataRegistry:null,dataAdapter:null};
var _6=function(_7){
var o=_2.clone(_5);
return _2.mixin(o,_7);
};
var _8={onRequest:{onItem:function(_9,_a){
return _a;
},onStruct:function(_b){
},},onResponse:{onItem:function(_c,_d){
return _d;
},onStruct:function(_e){
},}};
var _f=function(_10,_11){
if(_10){
return _10+"."+_11;
}
return _11;
};
var _12=_1(null,{_data:null,_converter:null,_dataAdapter:null,constructor:function(_13,_14){
if(!_13){
throw new Error("Missing parameter.");
}
if(typeof _13!=="object"){
throw new Error("Wrong parameter type: "+typeof _13);
}
var _15=_6(_14);
if(!_15.bareInput){
this._data=this._typedToBare(_13);
}else{
this.setDataAdapter(_15.dataAdapter);
this._data=this._bareToTyped(_13);
if(_15.fixups){
this._applyFixUps(_15.fixups,this._data,_15.metadataRegistry);
}
}
},_applyFixUps:function(_16,_17,_18){
dojo.forEach(_16,function(_19,_1a){
var _1b=_16[_1a].path;
var _1c=_16[_1a].type;
this._processFixUp(_17,_1b,this._getTransformFunction(_1c,_18));
},this);
},_processFixUp:function(_1d,_1e,_1f){
if(_1e.length==1){
_1d[_1e[0]]=_1f(_1d[_1e[0]]);
return;
}else{
if(_2.isArray(_1d[_1e[0]])){
dojo.forEach(_1d[_1e[0]],function(_20,_21){
this._processFixUp(_20,_1e.slice(1,_1e.length),_1f);
},this);
}else{
this._processFixUp(_1d[_1e[0]],_1e.slice(1,_1e.length),_1f);
}
}
},_getTransformFunction:function(_22,_23){
if(_22[0]==="frequencypattern"){
return function(_24){
return new _4(_24.code,_24.description);
};
}else{
if(_22[0]==="datetime"){
return function(_25){
return new Date(_25);
};
}else{
if(_22[0]==="date"){
return function(_26){
return new Date(_26);
};
}else{
if(_22[0]==="time"){
return function(_27){
return new Date(_27);
};
}else{
if(_22[0]==="codetable"){
if(_22.length<2){
throw new Error("Missing codetable name, type specified is: "+_22);
}
return function(_28){
var _29=_23.codetables()[_22[1]];
if(_29){
return _29.getItem(_28);
}else{
throw new Error("Codetable does not exist: codetable name="+_22[1]);
}
};
}else{
throw new Error("Unsupported type: "+_22);
}
}
}
}
}
},toJson:function(){
return _3.stringify(this.getData());
},getData:function(){
for(var _2a in this._data){
this._data[_2a]=this[_2a];
}
return this._typedToBare(this._data);
},_bareToTyped:function(_2b,_2c){
if(_2.isObject(_2b)){
var _2d={};
this._applyResponseStructAdapter(_2b);
for(var _2e in _2b){
if(_2.isArray(_2b[_2e])){
_2d[_2e]=[];
for(var i=0;i<_2b[_2e].length;i++){
_2d[_2e].push(this._bareToTyped(_2b[_2e][i],_f(_2c,_2e+"["+i+"]")));
}
}else{
if(typeof _2b[_2e]==="object"){
_2d[_2e]=this._bareToTyped(_2b[_2e],_f(_2c,_2e));
}else{
_2d[_2e]=_2b[_2e];
}
}
var _2f=_f(_2c,_2e);
_2d[_2e]=this._applyResponseDataAdapter(_2f,_2d[_2e]);
}
return _2d;
}
return this._applyResponseDataAdapter(_2c,_2b);
},_typedToBare:function(_30,_31){
if(_2.isObject(_30)){
var _32={};
for(var _33 in _30){
if(_30.hasOwnProperty(_33)&&"_data"!==_33&&"_dataAdapter"!==_33&&"_inherited"!==_33&&"_converter"!==_33){
if(_2.isArray(_30[_33])){
_32[_33]=[];
for(var i=0;i<_30[_33].length;i++){
_32[_33].push(this._typedToBare(_30[_33][i],_f(_31,_33+"["+i+"]")));
}
}else{
if(_30[_33].getDescription&&_30[_33].getCode){
_32[_33]=_30[_33].getCode();
}else{
if(_30[_33].getTime){
_32[_33]=_30[_33].getTime();
}else{
if(typeof _30[_33]==="object"){
_32[_33]=this._typedToBare(_30[_33],_f(_31,_33));
}else{
_32[_33]=_30[_33];
}
}
}
}
var _34=_f(_31,_33);
_32[_33]=this._applyRequestDataAdapter(_34,_32[_33]);
}
}
this._applyRequestStructAdapter(_32);
return _32;
}
return this._applyRequestDataAdapter(_31,_30);
},setDataAdapter:function(_35){
if(_35){
var a=_2.clone(_8);
if(_35.onRequest&&_35.onRequest.onItem){
a.onRequest.onItem=_35.onRequest.onItem;
}
if(_35.onRequest&&_35.onRequest.onStruct){
a.onRequest.onStruct=_35.onRequest.onStruct;
}
if(_35.onResponse&&_35.onResponse.onItem){
a.onResponse.onItem=_35.onResponse.onItem;
}
if(_35.onResponse&&_35.onResponse.onStruct){
a.onResponse.onStruct=_35.onResponse.onStruct;
}
this._dataAdapter=a;
}else{
this._dataAdapter=null;
}
},_applyRequestDataAdapter:function(_36,_37){
if(this._dataAdapter){
return this._dataAdapter.onRequest.onItem(_36,_37);
}
return _37;
},_applyResponseDataAdapter:function(_38,_39){
if(this._dataAdapter){
return this._dataAdapter.onResponse.onItem(_38,_39);
}
return _39;
},_applyRequestStructAdapter:function(_3a){
if(this._dataAdapter){
this._dataAdapter.onRequest.onStruct(_3a);
}
},_applyResponseStructAdapter:function(_3b){
if(this._dataAdapter){
this._dataAdapter.onResponse.onStruct(_3b);
}
}});
return _12;
});
