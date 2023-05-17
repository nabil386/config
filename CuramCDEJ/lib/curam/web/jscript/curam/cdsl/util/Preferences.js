//>>built
define("curam/cdsl/util/Preferences",["dojo/_base/declare","dojo/_base/lang","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/request/CuramService","curam/cdsl/Struct"],function(_1,_2,_3,_4,_5){
var _6=_1(null,{_connection:null,constructor:function(_7){
if(!_7){
throw new Error("Missing parameter.");
}
if(typeof _7!=="object"){
throw new Error("Wrong parameter type: "+typeof _7);
}
this._connection=_7;
},getPreference:function(_8){
return this._connection.preferences().getPreference(_8);
},getPreferenceNames:function(){
return this._connection.preferences().getPreferenceNames();
},loadPreferences:function(){
var _9=new _4(this._connection);
var _a=new _3("CuramService","getPreferences");
return _9.call([_a]).then(_2.hitch(this,function(_b){
var _c=_b[0].getData();
for(prop in _c){
this._connection.preferences().addPreference(prop,_c[prop]);
}
return this;
}));
}});
return _6;
});
