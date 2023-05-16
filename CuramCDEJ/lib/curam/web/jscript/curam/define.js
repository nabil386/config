//>>built
define("curam/define",["dojo/_base/lang"],function(_1){
var _2=this;
if(typeof (_2.curam)=="undefined"){
_2.curam={};
}
if(typeof (_2.curam.define)=="undefined"){
_1.mixin(_2.curam,{define:{}});
}
_1.mixin(_2.curam.define,{singleton:function(_3,_4){
var _5=_3.split(".");
var _6=window;
for(var i=0;i<_5.length;i++){
var _7=_5[i];
if(typeof _6[_7]=="undefined"){
_6[_7]={};
}
_6=_6[_7];
}
if(_4){
_1.mixin(_6,_4);
}
}});
return _2.curam.define;
});
