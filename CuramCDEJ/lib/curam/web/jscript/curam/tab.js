//>>built
define("curam/tab",["dijit/registry","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-class","curam/inspection/Layer","curam/define","curam/util","curam/util/ScreenContext"],function(_1,_2,_3,_4,_5,_6){
curam.define.singleton("curam.tab",{SECTION_TAB_CONTAINER_ID:"app-sections-container-dc",SMART_PANEL_IFRAME_ID:"curam_tab_SmartPanelIframe",toBeExecutedOnTabClose:[],_mockSelectedTab:null,getSelectedTab:function(_7){
if(curam.tab._mockSelectedTab){
return curam.tab._mockSelectedTab;
}
if(curam.tab.getTabContainer(_7)){
return curam.tab.getTabContainer(_7).selectedChildWidget;
}
},getTabContainer:function(_8){
return curam.tab.getTabContainerFromSectionID(_8||curam.tab.getCurrentSectionId());
},getCurrentSectionId:function(_9){
var _a=curam.util.getTopmostWindow().dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
if(_a){
var _b=_a.selectedChildWidget.domNode.id;
return _b.substring(0,_b.length-4);
}else{
if(!_9){
throw new Error("curam.tab.getCurrentSectionId() - application section"+" tab container not found");
}
}
return null;
},inTabbedUI:function(){
return curam.tab.getCurrentSectionId(true)!=null;
},getTabContainerFromSectionID:function(_c){
var _d=_1.byId(_c+"-stc");
if(!_d&&window.parent&&window.parent!=window){
_d=curam.util.getTopmostWindow().dijit.byId(_c+"-stc");
}
return _d;
},getTabWidgetId:function(_e){
return _e.id;
},getSelectedTabWidgetId:function(){
return curam.tab.getTabWidgetId(curam.tab.getSelectedTab());
},getContainerTab:function(_f){
var _10=dijit.getEnclosingWidget(_f);
if(_10&&!_10.tabDescriptor){
_10=curam.tab.getContainerTab(_10.domNode.parentNode);
}
if(!_10||!_10.tabDescriptor){
throw "Containing tab widget could not be found for node: "+_f;
}
return _10;
},getContentPanelIframe:function(tab){
var _11=tab?tab:curam.tab.getSelectedTab(),_12=null;
if(_11){
_12=dojo.query("iframe",_11.domNode).filter(function(_13){
return _3.get(_13,"iscpiframe")=="true";
})[0];
}
return _12?_12:null;
},refreshMainContentPanel:function(tab){
var _14=tab?tab:curam.tab.getSelectedTab();
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_14.domNode]);
var _15=curam.tab.getContentPanelIframe(tab);
_15.contentWindow.curam.util.publishRefreshEvent();
_15.contentWindow.location.reload(false);
},getSmartPanelIframe:function(tab){
var _16=tab?tab:curam.tab.getSelectedTab();
var _17=dojo.query("iframe",_16.domNode).filter(function(_18){
return _18.id==curam.tab.SMART_PANEL_IFRAME_ID;
})[0];
return _17;
},unsubscribeOnTabClose:function(_19,_1a){
curam.tab.toBeExecutedOnTabClose.push(function(_1b){
if(_1a==_1b){
dojo.unsubscribe(_19);
return true;
}
return false;
});
},executeOnTabClose:function(_1c,_1d){
curam.tab.toBeExecutedOnTabClose.push(function(_1e){
if(_1d==_1e){
_1c();
return true;
}
return false;
});
},doExecuteOnTabClose:function(_1f){
var _20=new Array();
for(var i=0;i<curam.tab.toBeExecutedOnTabClose.length;i++){
var _21=curam.tab.toBeExecutedOnTabClose[i];
if(!_21(_1f)){
_20.push(_21);
}
}
curam.tab.toBeExecutedOnTabClose=_20;
},getHandlerForTab:function(_22,_23){
return function(_24,_25){
if(_25==_23){
_22(_24,_23);
}else{
}
};
},getTabController:function(){
return curam.util.getTopmostWindow().curam.ui.UIController;
},initTabLinks:function(_26){
dojo.query("a").forEach(function(_27){
if(_27.href.indexOf("#")!=0&&_27.href.indexOf("javascript:")!=0&&(_27.href.indexOf("Page.do")>-1||_27.href.indexOf("Frame.do")>-1)){
if(_27.href.indexOf("&o3ctx")<0&&_27.href.indexOf("?o3ctx")<0){
var _28=(_27.href.indexOf("?")>-1)?"&":"?";
_27.href+=_28+jsScreenContext.toRequestString();
}
}
});
elements=document.forms;
for(var i=0;i<elements.length;++i){
elem=elements[i];
var _29=_2.byId("o3ctx");
if(!_29){
var ctx=new curam.util.ScreenContext();
ctx.setContextBits("ACTION");
_4.create("input",{"type":"hidden","name":"o3ctx","value":ctx.getValue()},elem);
}
_4.create("input",{"type":"hidden","name":"o3prv","value":jsPageID},elem);
}
if(elements.length>0){
curam.util.getTopmostWindow().dojo.publish("curam.fireNextRequest",[]);
}
},initContent:function(_2a,_2b){
var _2c=_2.byId("content");
_5.remove(_2c,"hidden-panel");
return;
},setupSectionSelectionListener:function(){
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",curam.tab.onSectionSelected);
},onSectionSelected:function(_2d){
if(_2d.curamDefaultPageID){
var _2e;
if(_2d.id.substring(_2d.id.length-4,_2d.id.length)=="-sbc"){
var _2f=_2d.id.substring(0,_2d.id.length-4);
_2e=curam.tab.getTabContainer(_2f);
}else{
_2e=_2d;
}
if(_2e&&_2e.getChildren().length==0){
curam.tab.getTabController().handleUIMPageID(_2d.curamDefaultPageID,true);
}
return true;
}
return false;
},setSectionDefaultPage:function(_30,_31){
var _32=_1.byId(_30);
if(_32){
_32.curamDefaultPageID=_31;
}else{
throw "curam.tab.setSectionDefaultPage() - cannot find section dijit ID:"+_30;
}
},publishSmartPanelContentReady:function(){
var _33="smartpanel.content.loaded";
var _34=window.frameElement;
_34.setAttribute("_SPContentLoaded","true");
curam.util.getTopmostWindow().dojo.publish(_33,[_34]);
}});
_6.register("curam/tab",curam.tab);
return curam.tab;
});
