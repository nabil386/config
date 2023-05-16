//>>built
require({cache:{"url:curam/layout/resources/ScrollingTabController.html":"<div class=\"dijitTabListContainer-${tabPosition} tabStrip-disabled dijitLayoutContainer\"><!-- CURAM-FIX: removed style=\"visibility:hidden, dd the tabStrip-disabled class by default.\" -->\n\t<div data-dojo-type=\"curam.layout._ScrollingTabControllerMenuButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_menuBtn\"\n\t\t data-dojo-props=\"containerId: '${containerId}', iconClass: 'dijitTabStripMenuIcon',\n\t\t\t\t\tdropDownPosition: ['below-alt', 'above-alt']\"\n\t\t data-dojo-attach-point=\"_menuBtn\" showLabel=\"false\" title=\"Navigation menu\">&#9660;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_leftBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideLeftIcon', showLabel:false, title:'Navigation left'\"\n\t\t data-dojo-attach-point=\"_leftBtn\" data-dojo-attach-event=\"onClick: doSlideLeft\">&#9664;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_rightBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideRightIcon', showLabel:false, title:'Navigation right'\"\n\t\t data-dojo-attach-point=\"_rightBtn\" data-dojo-attach-event=\"onClick: doSlideRight\">&#9654;</div>\n\t<div class='dijitTabListWrapper dijitTabContainerTopNone dijitAlignClient' data-dojo-attach-point='tablistWrapper'>\n\t\t<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'\n\t\t\t\tdata-dojo-attach-point='containerNode' class='nowrapTabStrip dijitTabContainerTop-tabs'></div>\n\t</div>\n</div>"}});
define("curam/layout/ScrollingTabController",["dojo/_base/declare","dojo/dom-class","dijit/layout/ScrollingTabController","curam/inspection/Layer","curam/debug","curam/widget/_HasDropDown","dojo/text!curam/layout/resources/ScrollingTabController.html"],function(_1,_2,_3,_4,_5,_6,_7){
var _8=_1("curam.layout.ScrollingTabController",_3,{templateString:_7,onStartup:function(){
this.inherited(arguments);
_4.register("curam/layout/ScrollingTabController",this);
},updateTabStyle:function(){
var _9=this.getChildren();
curam.debug.log("curam.layout.ScrollingTabController.updateTabStyle kids = ",this.domNode);
dojo.forEach(_9,function(_a,_b,_c){
_2.remove(_a.domNode,["first-class","last-class"]);
if(_b==0){
_2.add(_a.domNode,"first");
}else{
if(_b==_c.length-1){
_2.add(_a.domNode,"last");
}
}
});
var _d=dojo.query(".nowrapTabStrip",this.domNode)[0];
_2.replace(_d,"nowrapSecTabStrip","nowrapTabStrip");
var _e=document.createElement("div");
_2.add(_e,"block-slope");
_2.add(_e,"dijitTab");
_e.innerHTML="&#x200B;";
_d.appendChild(_e);
}});
_1("curam.layout._ScrollingTabControllerMenuButton",[dijit.layout._ScrollingTabControllerMenuButton,_6],{closeDropDown:function(_f){
this.inherited(arguments);
if(this.dropDown){
this._popupStateNode.removeAttribute("aria-owns");
this.dropDown.destroyRecursive();
delete this.dropDown;
}
}});
return _8;
});
