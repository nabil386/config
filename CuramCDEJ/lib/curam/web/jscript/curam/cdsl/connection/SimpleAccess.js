//>>built
define("curam/cdsl/connection/SimpleAccess",["curam/cdsl/_base/_Connection","curam/cdsl/store/CuramStore","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","dojo/_base/lang","dojo/store/Observable","dojo/store/Cache","dojo/store/Memory"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
var _a=null;
return {initConnection:function(_b){
if(_b==null){
throw new Error("The connection object should be provided.");
}else{
if(!(_b instanceof _1)){
throw new Error("The wrong type of the connection object is provided.");
}
}
if(_a==null){
_a=_b;
}
return _a;
},buildStore:function(_c,_d,_e,_f){
if(_a==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_c==null){
throw new Error("Facade class name is missing.");
}
if(_f==null){
_f=false;
}
if(_e==null){
_e=false;
}
var _10=new _2(_a,_c,_d);
if(_e){
_10=new _7(_10);
}
if(_f){
var _11=new _9();
_10=new _8(_10,_11);
}
return _10;
},makeRequest:function(_12,_13,_14){
if(_a==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_12==null){
throw new Error("Facade class name is missing.");
}
if(_13==null){
throw new Error("Facade method name is missing.");
}
var _15=new _3(_a);
var _16;
if(_14==null){
_16=[];
}else{
_16=[new _5(_14)];
}
var _17=new _4(_12,_13,_16);
return _15.call([_17]);
}};
});
