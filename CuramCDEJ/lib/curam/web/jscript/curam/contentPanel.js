//>>built
define("curam/contentPanel",["curam/util","curam/tab","dojo/dom-attr","dojo/dom","curam/util/onLoad","curam/util/Refresh","curam/util/ui/refresh/RefreshEvent","curam/define","curam/debug","curam/ui/PageRequest","dijit/registry"],function(cu,ct,_1,_2){
curam.define.singleton("curam.contentPanel",{smartPanelLoadedTokens:{},initSmartPanelExpListPageLoadListener:function(){
if(!cu.getTopmostWindow().dojo.body()._spListenerInitialized){
cu.getTopmostWindow().dojo.subscribe("expandedList.pageLoaded",curam.contentPanel.smartPanelExpListPageLoadListener);
cu.getTopmostWindow().dojo.body()._spListenerInitialized="true";
}
},smartPanelExpListPageLoadListener:function(_3){
if(ct.getSmartPanelIframe()){
curam.contentPanel.checkSmartPanelLoaded(_3,"ExpandedList.TabContentArea.Reloaded",curam.tab.getSelectedTabWidgetId());
}
},publishSmartPanelExpListPageLoad:function(_4){
if(ct.getSmartPanelIframe()){
cu.getTopmostWindow().dojo.publish("expandedList.pageLoaded",[_4.contentWindow.location.href]);
}
},setupOnLoad:function(_5,_6){
curam.debug.log("curam.contenPanel: setupOnLoad: "+_5+" "+_6);
curam.contentPanel.initSmartPanelExpListPageLoadListener();
var _7=curam.contentPanel.iframeOnloadHandler;
curam.util.onLoad.addSubscriber(_5,_7);
if(curam.tab.getSmartPanelIframe(dijit.byId(_6))){
curam.debug.log("tab has smart panel, setting up event listener");
curam.contentPanel.targetSmartPanel(_5,_6);
}
ct.executeOnTabClose(function(){
curam.util.onLoad.removeSubscriber(_5,_7);
curam.contentPanel._unregisterSmartPanelListener(_6);
if(curam.util.getTopmostWindow().curam.util.lastOpenedTabButton&&curam.util.getTopmostWindow().curam.util.lastOpenedTabButton.domNode.id.includes(_6,0)){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=null;
}
},_6);
},iframeUpdateTitle:function(_8,_9,_a){
var _b=CONTENT_PANEL_TITLE+" - ";
var _c=_2.byId(_9);
if(_c==null){
_c=dojo.query("iframe."+_9)[0];
}
ct.executeOnTabClose(function(){
var _d=_1.get(_c,"src");
_1.set(_c,"src","");
curam.debug.log("curam.contentPanel: Released iframe content for "+_d);
},_8);
var _e;
if(_a){
var _f=dojo.query("iframe.contentPanelFrame");
var _10;
for(var i=0;i<_f.length;i++){
if(_f[i].title==_b){
_10=_f[i];
break;
}
}
if(_10){
_e=_a;
_10.contentWindow.document.title=_e;
}
}else{
_e=_c.contentWindow.document.title;
}
if(_e==""||_e=="undefined"){
var _11=curam.util.iframeTitleFallBack();
_c.contentWindow.document.title=_11;
}
_1.set(_c,"title",_b+curam.util.iframeTitleFallBack());
_1.set(_c,"data-done-loading",true);
},iframeOnloadHandler:function(_12,_13){
dojo.subscribe("tab.title.name.finished",function(_14){
var _15=ct.getContainerTab(dojo.query("iframe."+_14.frameid)[0]);
var _16=ct.getTabWidgetId(_15);
curam.contentPanel.iframeUpdateTitle(_16,_14.frameid,_14.title);
});
var _17=ct.getContainerTab(dojo.query("iframe."+_12)[0]);
var _18=ct.getTabWidgetId(_17);
curam.contentPanel.iframeUpdateTitle(_18,_12);
cu.Refresh.getController(_18).pageLoaded(_13.pageID,cu.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN);
dojo.publish("/curam/main-content/page/loaded",[_13.pageID,_18,_17]);
},spOnLoadHandler:function(_19,_1a){
var _1b=dojo.query("."+_19)[0];
curam.contentPanel.checkSmartPanelLoaded(_1b.src,"TabContentArea.Reloaded",curam.tab.getSelectedTabWidgetId());
},checkSmartPanelLoaded:function(url,_1c,_1d){
if(!_1d){
throw new Error("Required argument 'tabWidgetId' was not specified.");
}
var _1e=ct.getSmartPanelIframe();
var _1f=_1.get(_1e,"iframeLoaded");
if(_1f=="true"){
curam.contentPanel.smartPanelPublisher(_1e,url,_1c,_1d);
}else{
var _20=dojo.subscribe("smartPanel.loaded",function(_21){
if(_21==_1e){
curam.contentPanel._unregisterSmartPanelListener(_1d);
curam.contentPanel.smartPanelPublisher(_1e,url,_1c,_1d);
}
});
curam.contentPanel._storeSmartPanelToken(_20,_1d);
}
},_storeSmartPanelToken:function(_22,_23){
var _24=curam.contentPanel.smartPanelLoadedTokens[_23];
if(_24){
dojo.unsubscribe(_24);
}
curam.contentPanel.smartPanelLoadedTokens[_23]=_22;
},_unregisterSmartPanelListener:function(_25){
dojo.unsubscribe(curam.contentPanel.smartPanelLoadedTokens[_25]);
curam.contentPanel.smartPanelLoadedTokens[_25]=null;
},smartPanelPublisher:function(_26,url,_27,_28){
var _29=new curam.ui.PageRequest(url);
curam.debug.log("Publishing event to smart panel in tab %s: eventType=%s;"+" pageID=%s; parameters=%s",_28,_27,_29.pageID,dojo.toJson(_29.parameters));
_26.contentWindow.dojo.publish("contentPane.targetSmartPanel",[{"eventType":_27,"pageId":_29.pageID,"parameters":_29.parameters}]);
},targetSmartPanel:function(_2a,_2b){
curam.debug.log("curam.contentPanel:targetSmartPanel(): "+_2a+" "+_2b);
var _2c=ct.getSmartPanelIframe();
var _2d=_2b;
if(_2c){
var _2e=curam.util.onLoad.defaultGetIdFunction(_2c);
var _2f=dojo.subscribe("expandedList.toggle",function(_30,_31,_32){
if(_2d===_32){
curam.contentPanel.checkSmartPanelLoaded(_31.url,_31.eventType,_32);
}
});
var _33=curam.contentPanel.spOnLoadHandler;
curam.util.onLoad.addSubscriber(_2a,_33);
ct.executeOnTabClose(function(){
dojo.unsubscribe(_2f);
curam.util.onLoad.removeSubscriber(_2a,_33);
curam.util.onLoad.removeSubscriber(_2e,curam.smartPanel._handleSmartPanelLoad);
},_2b);
}
}});
return curam.contentPanel;
});
