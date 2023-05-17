//>>built
define("curam/util/RuntimeContext",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.RuntimeContext",null,{_window:null,constructor:function(_3){
this._window=_3;
},getHref:function(){
return this._window.location.href;
},getPathName:function(){
return this._window.location.pathName;
},contextObject:function(){
return this._window;
}});
return _2;
});
