//>>built
define("curam/layout/CuramTabContainer",["dojo/_base/lang","dojo/_base/declare","dijit/layout/TabContainer","curam/layout/ScrollingTabController","dijit/layout/TabController"],function(_1,_2,_3,_4,_5){
var _6=_2("curam.layout.CuramTabContainer",dijit.layout.TabContainer,{postMixInProperties:function(){
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_4:_5;
}
this.inherited(arguments);
}});
return _6;
});
