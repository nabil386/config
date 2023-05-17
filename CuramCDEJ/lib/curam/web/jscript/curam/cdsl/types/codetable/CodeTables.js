//>>built
define("curam/cdsl/types/codetable/CodeTables",["dojo/_base/declare","dojo/_base/lang","dojo/Deferred","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct"],function(_1,_2,_3,_4,_5){
var _6=_1(null,{_connection:null,constructor:function(_7){
if(!_7){
throw new Error("Missing parameter.");
}
if(typeof _7!=="object"){
throw new Error("Wrong parameter type: "+typeof _7);
}
this._connection=_7;
},getCodeTable:function(_8){
return this._connection.metadata().codetables()[_8];
},loadForFacades:function(_9){
var _a=new _3();
require(["curam/cdsl/request/CuramService"],_2.hitch(this,function(_b){
var _c=new _b(this._connection),_d=new _4("CuramService","getCodetables",this._getInputStructsForLoadingCodetables(_9));
_c.call([_d]).then(_2.hitch(this,function(_e){
_a.resolve(this);
}),function(_f){
_a.reject(_f);
});
}));
return _a;
},_getInputStructsForLoadingCodetables:function(_10){
var ret=[];
for(var i=0;i<_10.length;i++){
ret.push(new _5({service:_10[i].intf(),method:_10[i].method()}));
}
return ret;
}});
return _6;
});
