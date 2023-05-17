//>>built
define("curam/util/ui/AppExitConditionHandler",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.ui.AppExitConditionHandler",null,{_handler:null,constructor:function(_3){
if(typeof _3!="function"){
throw new Error("Illegal argument: "+_3);
}
this._handler=_3;
},isConfirmationAllowed:function(){
return this._handler()?true:false;
}});
return _2;
});
