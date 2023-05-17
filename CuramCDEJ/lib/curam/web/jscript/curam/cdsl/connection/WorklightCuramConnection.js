//>>built
define("curam/cdsl/connection/WorklightCuramConnection",["dojo/_base/declare","dojo/Deferred","../_base/_Connection"],function(_1,_2,_3){
var _4=_1(_3,{invoke:function(_5){
var _6={adapter:"CuramAdapter",procedure:"callFacade",parameters:[_5.toJson()]};
var _7=new _2();
var _8={onSuccess:function(_9){
_7.resolve(_9);
},onFailure:function(_a){
_7.reject(_a);
},invocationContext:{}};
WL.Client.invokeProcedure(_6,_8);
return _7;
}});
return _4;
});
