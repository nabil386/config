//>>built
define("curam/ui/OpenTabEvent",["dojo/_base/declare","curam/ui/PageRequest"],function(_1,_2){
var _3=_1("curam.ui.OpenTabEvent",null,{constructor:function(_4,_5,_6){
this.tabDescriptor=_4;
this.openInBackground=_6?true:false;
if(_5){
this.uimPageRequest=_5;
}else{
this.uimPageRequest=new _2(_4,_4.isHomePage);
}
}});
return _3;
});
