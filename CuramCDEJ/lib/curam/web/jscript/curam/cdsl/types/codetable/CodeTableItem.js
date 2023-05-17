//>>built
define("curam/cdsl/types/codetable/CodeTableItem",["dojo/_base/declare"],function(_1){
var _2=_1(null,{_code:null,_desc:null,_isDefault:null,constructor:function(_3,_4){
this._code=_3;
this._desc=_4;
this._isDefault=false;
},getCode:function(){
return this._code;
},getDescription:function(){
return this._desc;
},isDefault:function(_5){
if(typeof _5==="undefined"){
return this._isDefault||false;
}else{
var _6=this._isDefault;
this._isDefault=_5;
return _6;
}
}});
return _2;
});
