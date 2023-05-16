//>>built
define("curam/util/ContextPanel",["dijit/registry","dojo/dom-attr","curam/inspection/Layer","curam/debug","curam/util/onLoad","curam/util","curam/tab","curam/define"],function(_1,_2,_3,_4,_5){
curam.define.singleton("curam.util.ContextPanel",{CONTENT_URL_ATTRIB:"data-content-url",setupLoadEventPublisher:function(_6,_7,_8){
curam.util.ContextPanel._doSetup(_6,_7,_8,function(_9){
return _1.byId(_9);
});
},_doSetup:function(_a,_b,_c,_d){
var _e=curam.util.getTopmostWindow().dojo.subscribe(_a,function(){
var _f=_d(_b);
var _10=curam.util.ContextPanel._getIframe(_f);
if(_10){
curam.tab.executeOnTabClose(function(){
var src=_2.get(_10,"src");
_2.set(_10,"src","");
curam.debug.log("curam.util.ContextPanel: Released iframe content for "+src);
},_b);
_4.log(_4.getProperty("curam.util.ContextPanel.loaded"));
curam.util.getTopmostWindow().dojo.publish("/curam/frame/detailsPanelLoaded",[{loaded:true},_b]);
_10._finishedLoading=true;
if(_10._scheduledRefresh){
curam.util.ContextPanel.refresh(_f);
_10._scheduledRefresh=false;
}
}
});
_5.addSubscriber(_c,curam.util.ContextPanel.addTitle);
curam.tab.unsubscribeOnTabClose(_e,_b);
curam.tab.executeOnTabClose(function(){
_5.removeSubscriber(_c,curam.util.ContextPanel.addTitle);
},_b);
},refresh:function(tab){
var _11=curam.util.ContextPanel._getIframe(tab);
if(_11){
curam.debug.log(_4.getProperty("curam.util.ContextPanel.refresh.prep"));
if(_11._finishedLoading){
curam.debug.log(_4.getProperty("curam.util.ContextPanel.refresh"));
_11._finishedLoading=false;
var doc=_11.contentDocument||_11.contentWindow.document;
doc.location.reload(false);
}else{
curam.debug.log(_4.getProperty("curam.util.ContextPanel.refresh.delay"));
_11._scheduledRefresh=true;
}
}
},_getIframe:function(tab){
if(tab){
var _12=dojo.query("iframe.detailsPanelFrame",tab.domNode);
return _12[0];
}
},addTitle:function(_13){
var _14=dojo.query("."+_13)[0];
var _15=_14.contentWindow.document.title;
_14.setAttribute("title",CONTEXT_PANEL_TITLE+" - "+_15);
},load:function(tab){
var _16=curam.util.ContextPanel._getIframe(tab);
if(_16){
var _17=_2.get(_16,curam.util.ContextPanel.CONTENT_URL_ATTRIB);
if(_17&&_17!="undefined"){
_16[curam.util.ContextPanel.CONTENT_URL_ATTRIB]=undefined;
_2.set(_16,"src",_17);
}
}
}});
var _18=curam.util.getTopmostWindow();
if(typeof _18._curamContextPanelTabReadyListenerRegistered!="boolean"){
_18.dojo.subscribe("/curam/application/tab/ready",null,function(_19){
curam.util.ContextPanel.load(_19);
});
_18._curamContextPanelTabReadyListenerRegistered=true;
}
_3.register("curam/util/ContextPanel",this);
return curam.util.ContextPanel;
});
