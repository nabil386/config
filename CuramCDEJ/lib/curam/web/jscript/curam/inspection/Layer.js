//>>built
define("curam/inspection/Layer",["curam/define"],function(_1){
curam.define.singleton("curam.inspection.Layer",{register:function(_2,_3){
require(["curam/util"]);
var _4=curam.util.getTopmostWindow();
return _4.inspectionManager?_4.inspectionManager.observe(_2,_3):null;
}});
var _5=curam.inspection.Layer;
require(["curam/util"]);
_5.tWin=curam.util.getTopmostWindow();
var _6=_5.tWin.inspectionManager?_5.tWin.inspectionManager.getDirects():[];
if(_6.length>0){
require(_6);
}
return _5;
});
