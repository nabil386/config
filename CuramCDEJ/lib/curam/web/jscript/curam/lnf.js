//>>built
define("curam/lnf",["dojo/dom","dojo/dom-class","curam/define"],function(_1,_2,_3){
_3.singleton("curam.lnf",{setCTParent:function(id){
var _4=_1.byId(id);
var _5=_4.parentNode;
if(_5.tagName=="TD"){
_2.add(_5,"codetable");
}
}});
return curam.lnf;
});
