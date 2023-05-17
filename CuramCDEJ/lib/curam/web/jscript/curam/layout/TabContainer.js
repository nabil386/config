//>>built
require({cache:{"url:curam/layout/resources/TabContainer.html":"<div class=\"dijitTabContainer\">\n\t<div class=\"dijitTabListWrapper\" data-dojo-attach-point=\"tablistNode\"></div>\n\t<div data-dojo-attach-point=\"tablistSpacer\" class=\"dijitTabSpacer ${baseClass}-spacer dijitAlignTop\"></div>\n\t<div class=\"dijitTabPaneWrapper ${baseClass}-container dijitAlignClient\" data-dojo-attach-point=\"containerNode\"></div>\n</div>\n"}});
define("curam/layout/TabContainer",["dojo/_base/declare","dijit/layout/TabContainer","dojo/text!curam/layout/resources/TabContainer.html","curam/inspection/Layer","dojo/_base/connect","curam/layout/ScrollingTabController","dijit/layout/TabController"],function(_1,_2,_3,_4,_5,_6,_7){
var _8=_1("curam.layout.TabContainer",dijit.layout.TabContainer,{templateString:_3,_theSelectedTabIndex:0,_thePage:null,_theChildren:null,postCreate:function(){
this.inherited(arguments);
var tl=this.tablist;
this.connect(tl,"onRemoveChild","_changeTab");
this.connect(tl,"onSelectChild","updateTabTitle");
_4.register("curam/layout/TabContainer",this);
},selectChild:function(_9,_a){
this.inherited(arguments);
dojo.publish("curam/tab/selected",[_9.id]);
},updateTabTitle:function(){
curam.util.setBrowserTabTitle();
},_changeTab:function(){
if(this._beingDestroyed){
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren==null){
return;
}
if(this._theChildren[this._theSelectedTabIndex]!=this._thePage){
this.selectChild(this._theChildren[this._theSelectedTabIndex]);
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren.length<1){
this._thePage=null;
return;
}else{
if(this._theChildren.length==1){
this.selectChild(this._theChildren[this._theChildren.length-1]);
this._thePage=null;
this._theChildren=null;
}else{
if(this._theSelectedTabIndex==(this._theChildren.length-1)){
this.selectChild(this._theChildren[this._theChildren.length-2]);
}else{
if(this._theSelectedTabIndex==0){
this.selectChild(this._theChildren[1]);
}else{
if(this._theChildren.length>2){
this.selectChild(this._theChildren[this._theSelectedTabIndex+1]);
}
}
}
this._thePage=null;
this._theChildren=null;
}
}
},removeChild:function(_b){
if(this._started&&!this._beingDestroyed){
var _c=this.getChildren();
var i=0;
var _d=0;
for(i=0;i<_c.length;i++){
if(_c[i].get("selected")){
_d=i;
break;
}
}
this._theSelectedTabIndex=_d;
this._thePage=_b;
this._theChildren=_c;
var _e=_b.id;
_5.publish("/curam/tab/closing",[_e]);
}
this.inherited(arguments);
},postMixInProperties:function(){
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_6:_7;
}
this.inherited(arguments);
}});
return _8;
});
