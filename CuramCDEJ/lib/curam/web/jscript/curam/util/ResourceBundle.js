//>>built
define("curam/util/ResourceBundle",["dojo/_base/declare","dojo/i18n","dojo/string"],function(_1,_2,_3){
var _4=_1("curam.util.ResourceBundle",null,{_bundle:undefined,constructor:function(_5,_6){
var _7=_5.split(".");
var _8=_7[_7.length-1];
var _9=_7.length==1?"curam.application":_5.slice(0,_5.length-_8.length-1);
try{
var b=_2.getLocalization(_9,_8,_6);
if(this._isEmpty(b)){
throw new Error("Empty resource bundle.");
}else{
this._bundle=b;
}
}
catch(e){
throw new Error("Unable to access resource bundle: "+_9+"."+_8+": "+e.message);
}
},_isEmpty:function(_a){
for(var _b in _a){
return false;
}
return true;
},getProperty:function(_c,_d){
var _e=this._bundle[_c];
var _f=_e;
if(_d){
_f=_3.substitute(_e,_d);
}
return _f;
}});
return _4;
});
