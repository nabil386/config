//>>built
define("curam/StringBuffer",["dojo/_base/declare"],function(_1){
var _2=_1("curam.StringBuffer",null,{constructor:function(){
this.buffer=[];
},append:function append(_3){
this.buffer.push(_3);
return this;
},toString:function toString(){
return this.buffer.join("");
}});
return _2;
});
