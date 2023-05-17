//>>built
define("curam/util/ui/ApplicationTabbedUiController",["dojo/_base/declare","curam/inspection/Layer","curam/debug","dojox/layout/ContentPane","curam/tab"],function(_1,_2,_3){
var _4=_1("curam.util.ui.ApplicationTabbedUiController",null,{_tabContainer:null,constructor:function(_5){
curam.util.extendXHR();
this._tabContainer=_5;
_2.register("curam/util/ui/ApplicationTabbedUiController",this);
},findOpenTab:function(_6){
var _7=_6.tabDescriptor;
var _8=curam.tab.getTabContainer(_7.sectionID);
var _9=null;
var _a=undefined;
var _b=undefined;
if(_8!=undefined){
_a=_8.getChildren();
_b=_8.selectedChildWidget;
}
if(_b){
var _c=_b.tabDescriptor;
this._log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.testing"));
if(_6.uimPageRequest.openInCurrentTab||(_c.tabID==_7.tabID&&_c.matchesPageRequest(_6.uimPageRequest))){
_9=_b;
}
}
if(!_9&&_a){
var _d=true;
this._log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.searching")+" "+_a.length+" "+_3.getProperty("curam.util.ui.ApplicationTabbedUiController.tabs"));
for(var i=0;i<_a.length;i++){
var _e=_a[i];
var _f=_e.tabDescriptor;
if(_f&&_f.tabID==_7.tabID){
if((_d&&_f.tabSignature==_f.tabID)||_f.matchesPageRequest(_6.uimPageRequest)){
_9=_e;
break;
}
_d=false;
}
}
}
this._log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.searched")+" '"+_7.tabID+"'. "+_3.getProperty("curam.util.ui.ApplicationTabbedUiController.found")+" "+(_9?_3.getProperty("curam.util.ui.ApplicationTabbedUiController.a"):_3.getProperty("curam.util.ui.ApplicationTabbedUiController.no"))+" "+_3.getProperty("curam.util.ui.ApplicationTabbedUiController.match"));
return _9;
},openPageInCurrentTab:function(_10){
var _11=curam.tab.getSelectedTab();
var _12=undefined;
if(_11){
_12=dojo.query(".nav-panel",_11.domNode)[0];
}
if(_12){
var _13=dojo.query(".content-area-container",_12)[0];
if(!_13){
_13=_11.domNode;
}
dojo.publish("/curam/progress/display",[_13]);
var _14;
if(_10.getURL().indexOf("?")==-1){
_14="?";
}else{
_14="&";
}
var loc=curam.config?curam.config.locale:jsL;
var _15=dojo.global.jsBaseURL+"/"+loc+"/"+_10.getURL()+_14+curam.tab.getTabController().getCacheBusterParameter();
if(_10.pageHolder){
_10.pageHolder.location.href=_15;
}else{
var _16=dojo.query(".contentPanelFrame",_12)[0];
_16.src=_15;
}
}
},_openInCurrentTab:function(_17){
var _18=curam.tab.getSelectedTab();
var _19=undefined;
if(_18){
_19=dojo.query(".nav-panel",_18.domNode)[0];
}
if(_19){
var _1a=dojo.query(".contentPanelFrame",_19)[0];
_17.cdejParameters["o3ctx"]="4096";
var loc=curam.config?curam.config.locale:jsL;
var url=loc+"/"+_17.getURL();
if(url.indexOf("?")==-1){
url+="?";
}else{
url+="&";
}
_1a.src=url+curam.tab.getTabController().getCacheBusterParameter();
}
},refreshExistingPageInTab:function(tab){
var _1b=curam.tab.getContentPanelIframe(tab);
_1b.contentWindow.location.reload(false);
},selectTab:function(tab){
this._tabContainer.selectChild(tab);
},createTab:function(_1c){
this._log("createTab(): "+_3.getProperty("curam.util.ui.ApplicationTabbedUiController.start"));
var _1d=_1c.tabDescriptor;
var _1e="";
if(_1d.tabContent&&_1d.tabContent.tabName){
_1e=_1d.tabContent.tabName;
}
var cp=new dojox.layout.ContentPane({tabDescriptor:_1d,uimPageRequest:_1c.uimPageRequest,title:_1e,closable:!_1d.isHomePage,preventCache:true,"class":"tab-content-holder dijitContentPane "+"dijitTabContainerTop-child "+"dijitTabContainerTop-dijitContentPane dijitTabPane",onDownloadStart:function(){
return "&nbsp;";
}});
var _1f=[];
_1c.uimPageRequest.cdejParameters["o3ctx"]="4096";
var _20=dojo.connect(cp,"onDownloadEnd",null,function(){
curam.util.fireTabOpenedEvent(cp.id);
});
_1f.push(_20);
_20=dojo.subscribe("/curam/tab/closing",null,function(_21){
if(_21==cp.id){
curam.tab.doExecuteOnTabClose(cp.id);
}
});
_1f.push(_20);
_1f.push(dojo.connect(cp,"destroy",function(){
dojo.forEach(_1f,dojo.disconnect);
}));
_20=dojo.connect(cp,"set",function(_22,_23){
if(_22=="title"&&arguments.length==2){
curam.debug.log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.title"));
var _24=curam.util.getTopmostWindow().dojo;
_24.global._tabTitle=_23;
try{
_24.publish("/curam/_tabTitle",[_23]);
}
catch(err){
}
cp.tabDescriptor.setTabContent(_1c.uimPageRequest,_23);
var _25=curam.tab.getSelectedTab();
if(_25){
var _26=_25.domNode.parentNode;
if(_26){
_26.focus();
}
}
}
});
_1f.push(_20);
_20=dojo.connect(cp,"onClose",function(){
new curam.tab.TabSessionManager().tabClosed(cp.tabDescriptor);
});
_1f.push(_20);
var qs=_1c.uimPageRequest.getQueryString();
var _27="TabContent.do"+"?"+curam.tab.getTabController().COMMAND_PARAM_NAME+"=PAGE&"+curam.tab.getTabController().PAGE_ID_PARAM_NAME+"="+_1c.uimPageRequest.pageID+(qs.length>0?"&"+qs:"")+"&o3tabid="+_1d.tabID+"&o3tabWidgetId="+cp.id;
this._log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.href")+" "+_27);
cp.set("href",_27);
this._log(_3.getProperty("curam.util.ui.ApplicationTabbedUiController.finished")+" ",cp.tabDescriptor);
return cp;
},insertTabIntoApp:function(_28,_29,_2a){
var _2b=null;
if(_29){
if(this._tabContainer.hasChildren()){
_2b=this._tabContainer.selectedChildWidget;
}
this._tabContainer.addChild(_28,0);
}else{
if(_2a){
var _2c=-1;
if(this._tabContainer.hasChildren()){
var _2d=this._tabContainer.selectedChildWidget;
var _2e=this._tabContainer.tablist.getChildren();
for(var i=0;i<_2e.length;i++){
var _2f=_2e[i];
if(_2f&&_2f._curamPageId==_2d.id){
_2c=i;
break;
}
}
}
if(_2c!=-1){
this._tabContainer.addChild(_28,_2c+1);
}else{
this._tabContainer.addChild(_28);
}
}else{
this._tabContainer.addChild(_28);
}
}
return _2b;
},_log:function(msg,_30){
if(curam.debug.enabled()){
curam.debug.log("curam.util.ui.ApplicationTabbedUiController: "+msg+(_30?" "+dojo.toJson(_30):""));
}
}});
return _4;
});
