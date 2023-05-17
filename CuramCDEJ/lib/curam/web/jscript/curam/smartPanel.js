//>>built
define("curam/smartPanel",["dojo/dom-attr","curam/tab","curam/util/onLoad","curam/define"],function(_1){
curam.define.singleton("curam.smartPanel",{setupOnLoad:function(_2){
curam.util.onLoad.addSubscriber(_2,curam.smartPanel._handleSmartPanelLoad);
dojo.ready(function(){
var _3=dojo.query("."+_2)[0];
if(!_1.get(_3,"src")){
var _4=dojo.query(".outer-navigation-tab")[0];
var _5=_4?dijit.byNode(_4):null;
if(_5){
var _6=null;
if(dojo.isBodyLtr()){
_6=_5.getSplitter("right");
}else{
_6=_5.getSplitter("left");
}
_6.connect(_6,"onMouseUp",curam.smartPanel.loadSmartPanelIframe);
}
}
});
},_handleSmartPanelLoad:function(_7,_8){
var _9=dojo.query("."+_7)[0];
var _a=_1.get(_9,"src");
curam.smartPanel.addTitle(_7);
var _b="smartpanel.content.loaded";
if(_a){
_9.setAttribute("iframeLoaded","true");
if(_1.get(_9,"_SPContentLoaded")==="true"){
dojo.publish("smartPanel.loaded",[_9]);
}else{
dojo.subscribe(_b,function(_c){
if(_c!=_9){
return;
}
dojo.publish("smartPanel.loaded",[_9]);
});
}
}
},loadSmartPanelIframe:function(){
var _d=curam.tab.getSmartPanelIframe();
if(_d){
var _e=_1.get(_d,"src");
if(_e==""){
var _e=_1.get(_d,"_srcContents");
_1.set(_d,"src",_e);
}
}
},addTitle:function(_f){
var _10=dojo.query("."+_f)[0];
var _11=SMART_PANEL_TITLE+" - "+_10.contentWindow.document.title;
_10.setAttribute("title",_11);
},_addDefaultImage:function(_12,_13){
var _14=document.createElement("img");
_14.src="";
_14.setAttribute("alt",_12.iconNode.getAttribute("aria-label"));
_14.setAttribute("class","default-smart-panel-icon");
_12.iconNodeLink.appendChild(_14);
this._updateSmartPanelImage(_12,_13);
},_updateSmartPanelImage:function(_15,_16){
if(_16){
if(_15.orient=="Right"){
_15.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--left20-enabled.svg");
}else{
_15.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--right20-enabled.svg");
}
}else{
if(!_16&&_15.orient=="Right"){
_15.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--right20-enabled.svg");
}else{
_15.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--left20-enabled.svg");
}
}
}});
return curam.smartPanel;
});
