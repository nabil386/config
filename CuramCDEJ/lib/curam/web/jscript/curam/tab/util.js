//>>built
define("curam/tab/util",["dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-geometry","curam/define","curam/debug"],function(_1,_2,_3,_4,_5,_6){
_5.singleton("curam.tab.util",{toggleDetailsPanel:function(_7){
_7=dojo.fixEvent(_7);
dojo.stopEvent(_7);
var _8=_7.target;
var _9=_8;
while(_9&&!_2.contains(_9,"detailsTitleArrowButton")){
_9=_9.parentNode;
}
var _8=_9;
if(_8._animating){
return;
}
_8._animating=true;
var _9=_8.parentNode;
while(_9&&!_2.contains(_9,"detailsPanel-bc")){
_9=_9.parentNode;
}
var _a=_9;
while(_9&&!_2.contains(_9,"summaryPane")){
_9=_9.parentNode;
}
var _b=_9;
while(_9){
if(_2.contains(_9,"dijitBorderContainer")&&!_2.contains(_9,"detailsPanel-bc")){
break;
}
if(_2.contains(_9,"tab-wrapper")){
break;
}
_9=_9.parentNode;
}
var _c=_9;
headerPanelNode=dojo.query(".detailsPanelTitleBar",_a)[0];
detailsPanelNode=dojo.query(".detailsContentPane",_a)[0];
var _d=_c.children;
var _e=dojo.filter(_d,function(_f){
if(_2.contains(_f,"splitter-pane")||_2.contains(_f,"dijitSplitterH")){
return _f;
}
})[0];
var _10=dojo.filter(_d,function(_11){
if(_2.contains(_11,"nav-panel")){
return _11;
}
})[0];
var _12=_4.getMarginBoxSimple(headerPanelNode).h;
var _13=_4.getMarginBoxSimple(_b).h;
var _14=_e.offsetHeight;
var _15=_4.getMarginBoxSimple(_10).h;
var _16=dojo.query(".detailsContentPane",_a)[0];
if(_12!=_b.clientHeight){
_2.add(_8,"collapsed");
_3.set(_8,"aria-expanded","false");
this._updateToggleArrowNode(_8,false);
_2.add(_16,"collapsed");
curam.debug.log(_6.getProperty("curam.tab.util.collapsing"));
_a._previousHeight=_13;
_10._previousHeight=_15;
dojo.animateProperty({node:_b,duration:500,properties:{height:{end:_12}}}).play();
if(_2.contains(_e,"splitter-pane")){
dojo.animateProperty({node:detailsPanelNode,duration:500,properties:{height:{end:0}}}).play();
}
dojo.animateProperty({node:_e,duration:500,properties:{top:{end:(_12+_14)}}}).play();
dojo.animateProperty({node:_10,duration:500,properties:{top:{end:(_12+_14)}},onEnd:function(){
_8._animating=false;
if(_2.contains(_e,"dijitSplitterH")){
_1.set(_10,"height",(_10._previousHeight+_a._previousHeight-_12)+"px");
}
}}).play();
}else{
_2.remove(_8,"collapsed");
_2.remove(_16,"collapsed");
_3.set(_8,"aria-expanded","true");
this._updateToggleArrowNode(_8,true);
_6.log(_6.getProperty("curam.tab.util.expanding"));
_1.set(_b,"height",_a._previousHeight+"px");
if(_2.contains(_e,"splitter-pane")){
dojo.animateProperty({node:detailsPanelNode,duration:500,properties:{height:{end:_a._previousHeight-_12}}}).play();
}
dojo.animateProperty({node:_e,duration:500,properties:{top:{end:(_a._previousHeight+_14)}}}).play();
dojo.animateProperty({node:_10,duration:500,properties:{top:{end:(_a._previousHeight+_14)}},onEnd:function(){
_8._animating=false;
if(_2.contains(_e,"dijitSplitterH")){
_1.set(_10,"height",_10._previousHeight+"px");
}
}}).play();
}
},_updateToggleArrowNode:function(_17,_18){
var _19=this._getToggleImages();
if(_18){
_17.children[0].src=_19[0];
_17.children[1].src=_19[2];
}else{
_17.children[0].src=_19[1];
_17.children[1].src=_19[3];
}
},_getToggleImages:function(){
var _1a;
var _1b;
var _1c;
var _1d;
var _1e=curam.util.isRtlMode();
_1a="./themes/curam/images/chevron--down20-enabled.svg";
_1c="./themes/curam/images/chevron--down20-enabled.svg";
if(_1e){
_1b="./themes/curam/images/Toggle_Right_Blue80_20px.png";
_1d="./themes/curam/images/Toggle_Fill_Right_Blue50_20px.png";
}else{
_1b="./themes/curam/images/chevron--left20-enabled.svg";
_1d="./themes/curam/images/chevron--left20-enabled.svg";
}
return [_1a,_1b,_1c,_1d];
},});
return curam.tab.util;
});
