//>>built
define("curam/html",["curam/define"],function(){
curam.define.singleton("curam.html",{splitWithTag:function(_1,_2,_3,_4){
var _5=_1.split(_2||"\n");
if(_5.length<2){
return _4?_4(_1):_1;
}
var t=(_3||"div")+">";
var _6="<"+t,_7="</"+t;
if(_4){
for(var i=0;i<_5.length;i++){
_5[i]=_4(_5[i]);
}
}
return _6+_5.join(_7+_6)+_7;
}});
return curam.html;
});
