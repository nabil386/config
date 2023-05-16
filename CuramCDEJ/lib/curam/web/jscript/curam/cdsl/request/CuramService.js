//>>built
define("curam/cdsl/request/CuramService",["dojo/_base/declare","curam/cdsl/Struct","curam/cdsl/_base/FacadeMethodResponse","dojo/_base/lang","curam/cdsl/_base/_Connection","curam/cdsl/_base/FacadeMethodCall"],function(_1,_2,_3,_4){
var _5={dataAdapter:null},_6=function(_7){
var o=_4.clone(_5);
o=_4.mixin(o,_7);
return o;
},_8="********************************************************",_9=_1(null,{_connection:null,_dataAdapter:null,constructor:function(_a,_b){
var _c=_6(_b);
this._connection=_a;
this._dataAdapter=_c.dataAdapter;
},call:function(_d,_e){
var _f=_d[0];
if(!_f.dataAdapter()){
_f.dataAdapter(this._dataAdapter);
}
var _10=this._connection.invoke(_f,_e);
return _10.then(_4.hitch(this,function(_11){
var _12=new _3(_f,_11,this._connection.metadata());
if(_12.failed()){
var e=_12.getError();
if(_12.devMode()){
console.log(_8);
console.log(e.toString());
console.log(_8);
}
throw e;
}
this._connection.updateMetadata(_12);
return [_12.returnValue()];
}));
}});
return _9;
});
