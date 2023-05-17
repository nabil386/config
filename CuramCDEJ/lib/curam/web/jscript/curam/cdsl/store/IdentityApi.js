//>>built
define("curam/cdsl/store/IdentityApi",["dojo/_base/declare","dojo/json"],function(_1,_2){
var _3=_1(null,{getIdentity:function(_4){
var _5=this.getIdentityPropertyNames()[0];
if(typeof _4[_5]==="object"){
throw new Error("Complex identity attributes are not supported by this implementation.");
}
return _4[_5];
},parseIdentity:function(_6){
var _7={};
_7[this.getIdentityPropertyNames()[0]]=_6;
return _7;
},getIdentityPropertyNames:function(){
return ["id"];
}});
return _3;
});
