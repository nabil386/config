//>>built
define("curam/ui/UIController",["dojo/_base/declare","dojo/_base/lang","dojo/json","dojo/dom-style","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/RuntimeContext","curam/tab/TabDescriptor","curam/util/ui/ApplicationTabbedUiController"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
curam.define.singleton("curam.ui.UIController",{TAB_TOPIC:"/app/tab",ROOT_OBJ:"curam.ui.UIController",PAGE_ASSOCIATIONS:{},RESOLVE_PAGES:{},PAGE_ID_PARAM_NAME:"o3pid",COMMAND_PARAM_NAME:"o3c",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",DUPLICATE_TAB_MAPPING_ERROR:"dupTabError",UNASSOCIATED_SHORTCUT_ERROR:"looseShortcutError",LOAD_MASK_TIMEOUT:15000,TABS_INFO_MODAL_TITLE_PROP_NAME:"title.info",TABS_ERROR_MODAL_TITLE_PROP_NAME:"title.error",TABS_INFO_MODAL_MSG_PROP_NAME:"message.max.tabs.info",TABS_ERROR_MODAL_MSG_PROP_NAME:"message.max.tabs.error",TABS_MSG_PLACEHOLDER_MAX_TABS:15,TABS_SEQUENTIAL_ORDER:false,MAX_NUM_TABS:15,MAX_TABS_MODAL_SIZE:"width=470,height=80",TAB_INSTANTIATOR:null,initialize:function(_a){
curam.ui.UIController._log("curam.ui.UIController.initialize()");
curam.ui.UIController._log("dojo.isQuirks: "+dojo.isQuirks);
window.rootObject=curam.ui.UIController.ROOT_OBJ;
curam.util.subscribe(curam.ui.UIController.TAB_TOPIC,curam.ui.UIController.tabTopicHandler);
curam.util.subscribe("tab.title.name.set",curam.ui.UIController.setTabTitleAndName);
if(_a){
new curam.tab.TabSessionManager().init(_a);
}else{
new curam.tab.TabSessionManager().init();
}
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.event"));
},ajaxPostFailure:function(_b){
curam.ui.UIController._log("========= "+_9.getProperty("curam.ui.UIController.test")+" JSON "+_9.getProperty("curam.ui.UIController.servlet.failure")+" =========");
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.error")+" "+_b);
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.args")+" "+ioargs);
curam.ui.UIController._log("============================================");
},instantiateTab:function(_c,_d,_e){
var _f=_e?2:1;
dojo.publish("curam/tab/quantityExpectedFrames/load",[_d,_f]);
var _10;
if(curam.ui.UIController.TAB_INSTANTIATOR!=null){
_10=curam.ui.UIController.TAB_INSTANTIATOR;
}else{
_10=dijit.byId(_d);
}
if(_10){
curam.util.getTopmostWindow().dojo.publish("/curam/application/tab/requested",[_d]);
var td=_10.tabDescriptor;
var _11="'"+td.tabID+"/"+_d+"'";
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.instantiating")+" "+_11+" "+_9.getProperty("curam.ui.UIController.with.signature"));
td.setTabSignature(_c,_10.uimPageRequest);
var _12=function(){
var _13=dojo.query("#"+_d+" .tab-wrapper .tab-load-mask")[0];
if(_13&&_4.get(_13,"display")!="none"){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.revealing")+" "+_11+" "+_9.getProperty("curam.ui.UIController.now"));
_4.set(_13,"display","none");
curam.util.getTopmostWindow().dojo.publish("/curam/application/tab/revealed",[_d]);
curam.debug.log("curam.ui.UIController.revealTabNow function calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
}
};
if(!_e){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.no.details"));
_12();
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.has.details")+_11+_9.getProperty("curam.ui.UIController.listeners"));
dojo.global.tabLoadMaskTimeout=setTimeout(_12,curam.ui.UIController.LOAD_MASK_TIMEOUT);
var _14=false;
var _15=function(){
if(_14){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.panels.loaded"));
_12();
clearTimeout(dojo.global.tabLoadMaskTimeout);
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.panels.not.loaded")+" "+_11+" "+_9.getProperty("curam.ui.UIController.later"));
_14=true;
}
};
var _16=dojo.connect(_10,"onDownloadEnd",function(){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.content.pane.loaded")+" "+_9.getProperty("curam.ui.UIController.reveal")+" "+_11+" "+_9.getProperty("curam.ui.UIController.now"));
_15();
dojo.disconnect(_16);
});
var _17=curam.util.getTopmostWindow().dojo.subscribe("/curam/frame/detailsPanelLoaded",function(_18,_19){
if(_d==_19){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.details.panel.loaded")+" "+_11+" "+ +_9.getProperty("curam.ui.UIController.now"));
_15();
dojo.unsubscribe(_17);
}
});
}
var _1a=curam.tab.getHandlerForTab(function(_1b,_1c){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.content.pane.changed")+" "+_11+" "+_9.getProperty("curam.ui.UIController.now"));
curam.ui.UIController._contentPanelUpdated(_10);
},_d);
var _1d=curam.util.getTopmostWindow().dojo.subscribe("/curam/main-content/page/loaded",null,_1a);
curam.tab.unsubscribeOnTabClose(_1d,_d);
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.tab.not.found")+" '"+_d+"'.");
}
},_contentPanelUpdated:function(tab){
var _1e=curam.tab.getContentPanelIframe(tab);
tab.tabDescriptor.setTabContent(new curam.ui.PageRequest(_1e.src),null);
},getCacheBusterParameter:function(){
return curam.ui.UIController.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+curam.ui.UIController.CACHE_BUSTER++;
},_getTabbedUiApi:function(_1f){
var _20=curam.ui.UIController._selectSection(_1f);
return new curam.util.ui.ApplicationTabbedUiController(_20);
},_selectSection:function(_21){
var _22=_21?!_21.openInBackground:true;
var _23=dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
var _24=_21?_21.tabDescriptor.sectionID:curam.tab.getCurrentSectionId();
var _25=dijit.byId(_24+"-sbc");
var _26=curam.tab.getTabContainer(_24);
if(_22){
if(_25){
_23.selectChild(_25);
}else{
_23.selectChild(_26);
}
}
return _26;
},tabTopicHandler:function(_27){
var api=curam.ui.UIController._getTabbedUiApi(_27);
curam.ui.UIController._doHandleTabEvent(_27,api);
},_doHandleTabEvent:function(_28,_29){
var _2a=_28.tabDescriptor;
var _2b=_2a.sectionID;
var _2c=curam.tab.getTabContainer(_2b);
_28.tabDescriptor.openInBackground=_28.openInBackground;
var _2d=curam.util.getTopmostWindow().dojo;
var _2e=false;
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.fired")+" "+_2b+" : "+_2a.tabID+" : "+_28.uimPageRequest.pageID);
var tab=_29.findOpenTab(_28);
if(tab===null&&_2.exists("selectedChildWidget.tabDescriptor.isHomePage",_2c)&&_2c.selectedChildWidget.tabDescriptor.isHomePage===true&&_2c.selectedChildWidget.tabDescriptor.tabID===_28.tabDescriptor.tabID){
tab=_2c.selectedChildWidget;
}
if(!tab){
if(_2c==undefined){
return false;
}
var _2f=_2c.getChildren().length+1;
var _30=this.MAX_NUM_TABS;
var _31=this._checkMaxNumOpenTabsExceeded(_30,_2f);
if(_31){
return true;
}
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.creating"));
tab=_29.createTab(_28);
dojo.publish("/curam/progress/display",[tab.domNode]);
tab.connect(tab,"onLoad",function(){
var _32=curam.tab.getContentPanelIframe(tab);
_5.set(_32,"src",_5.get(_32,"data-content-url"));
_2d.publish("/curam/application/tab/ready",[tab]);
});
_2e=true;
}
if(_2e){
var _33=_29.insertTabIntoApp(tab,_28.uimPageRequest.isHomePage,this.TABS_SEQUENTIAL_ORDER);
if(!_28.openInBackground){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.new.fore.tab"),tab.tabDescriptor);
_29.selectTab(tab);
if(tab.controlButton!="undefined"){
curam.util.setLastOpenedTabButton(dijit.byId(tab.controlButton.id));
}
if(_33!=null){
_29.selectTab(_33);
if(_33.controlButton!="undefined"){
curam.util.setLastOpenedTabButton(dijit.byId(_33.controlButton.id));
}
}
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.new.back.tab"),tab.tabDescriptor);
}
this._checkMaxNumOpenTabsReached(_30,_2f);
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.show.page"),tab.tabDescriptor);
_29.selectTab(tab);
if(_28.uimPageRequest.justRefresh){
_29.refreshExistingPageInTab(tab);
}else{
if(_28.uimPageRequest.forceLoad){
_29.openPageInCurrentTab(_28.uimPageRequest);
}else{
var _34=tab.tabDescriptor;
var _35=_34.tabID==_28.tabDescriptor.tabID&&_34.matchesPageRequest(_28.uimPageRequest);
var _36=_34.tabContent.pageID==_28.uimPageRequest.pageID;
if(_35&&!_36){
_29.openPageInCurrentTab(_28.uimPageRequest);
}
_36&&_35&&curam.util.getTopmostWindow().dojo.publish("curam/tab/contextRefresh",[true]);
}
}
}
return true;
},_checkMaxNumOpenTabsReached:function(_37,_38){
if(_38==_37){
this.TABS_MSG_PLACEHOLDER_MAX_TABS=_37;
curam.util.openGenericErrorModalDialog(this.MAX_TABS_MODAL_SIZE,this.TABS_INFO_MODAL_TITLE_PROP_NAME,this.TABS_INFO_MODAL_MSG_PROP_NAME,this.TABS_MSG_PLACEHOLDER_MAX_TABS,false);
return true;
}
},_checkMaxNumOpenTabsExceeded:function(_39,_3a){
if(_3a>_39){
this.TABS_MSG_PLACEHOLDER_MAX_TABS=_39;
curam.util.openGenericErrorModalDialog(this.MAX_TABS_MODAL_SIZE,this.TABS_ERROR_MODAL_TITLE_PROP_NAME,this.TABS_ERROR_MODAL_MSG_PROP_NAME,this.TABS_MSG_PLACEHOLDER_MAX_TABS,true);
return true;
}
},checkPage:function(_3b,_3c){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.checking.page")+" '"+_3b.pageID+"'.");
var _3d=null;
var _3e=null;
var _3f=null;
var _40=null;
var _41=_3c;
if(_3c&&typeof _3c!="function"){
_41=_3c.unmappedPageLoader;
_3d=_3c.preferredTabs;
_3e=_3c.moreThanOneTabMappedCallback;
_3f=_3c.shouldLoadPage;
_40=_3c.successCallback;
}
if(typeof _3f==="undefined"||_3f===null){
_3f=true;
}
if(_3b.pageID==""){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.ignoring")+" "+_3b.getURL());
return;
}
var _42=curam.ui.UIController._ensurePageAssociationInitialized(_3b,function(){
if(curam.ui.UIController.isPageAssociationInitialized(_3b.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS)){
curam.ui.UIController.checkPage(_3b,_3c);
}else{
var msg=_9.getProperty("curam.ui.UIController.failed");
curam.ui.UIController._log(msg);
throw new Error(msg);
}
});
if(_42){
try{
var _43=curam.ui.UIController.getTabDescriptorForPage(_3b.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS,_3d);
if(_43!=null){
if(_3f){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.page.opened")+" '"+_3b.pageID+"'. "+_9.getProperty("curam.ui.UIController.sec.id")+" '"+_43.sectionID+"'. "+_9.getProperty("curam.ui.UIController.tab.id")+" '"+_43.tabID+"'.");
if(_3b.isHomePage){
_43.isHomePage=true;
}
_43.setTabContent(_3b);
dojo.publish(curam.ui.UIController.TAB_TOPIC,[new curam.ui.OpenTabEvent(_43,_3b)]);
}
if(_40){
_40();
}
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.page.id")+" '"+_3b.pageID+"'.");
if(!_41||typeof _41!="function"){
if(typeof curam.tab.getSelectedTab()=="undefined"){
throw {name:curam.ui.UIController.UNASSOCIATED_SHORTCUT_ERROR,message:"ERROR:The requested page "+_3b.pageID+" is not associated with any tab and there is no "+"tab to open it!"};
}
if(_3f){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.load"));
curam.ui.UIController._getTabbedUiApi().openPageInCurrentTab(_3b);
}
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.unmapped"));
_41(_3b);
}
}
}
catch(e){
if(e.name==curam.ui.UIController.DUPLICATE_TAB_MAPPING_ERROR){
if(!_3e){
alert(e.message);
curam.ui.UIController._getTabbedUiApi().openPageInCurrentTab(_3b);
}else{
_3e(_3b);
}
return null;
}else{
if(e.name==curam.ui.UIController.UNASSOCIATED_SHORTCUT_ERROR){
alert(e.message);
console.error(e.message);
return null;
}else{
throw e;
}
}
}
}
},isPageAssociationInitialized:function(_44,_45){
var _46=_45[_44];
return !(typeof _46=="undefined");
},_ensurePageAssociationInitialized:function(_47,_48){
if(!curam.ui.UIController.isPageAssociationInitialized(_47.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS)){
var _49="/config/tablayout/associated["+_47.pageID+"]["+USER_APPLICATION_ID+"]";
new curam.ui.ClientDataAccessor().getRaw(_49,function(_4a){
curam.ui.UIController.initializePageAssociations(_47,_4a);
_48();
},function(_4b,_4c){
var msg=curam_ui_UIController_data_error+" "+_4b;
curam.ui.UIController._log(msg);
if(!_7.checkLoginPage(_4c.xhr)){
alert(msg);
}
var _4d=curam.util.getTopmostWindow();
_4d.dojo.publish("/curam/progress/unload");
_4d.location.reload(false);
},null);
return false;
}
return true;
},initializePageAssociations:function(_4e,_4f){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.got.assoc")+" '"+_4e.pageID+"'.");
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.assoc"),_4f);
if(_4f){
if(_4f.tabIDs&&_4f.tabIDs.length>0){
curam.ui.UIController.PAGE_ASSOCIATIONS[_4e.pageID]=_4f;
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.no.mappings")+" '"+_4e.pageID+"'.");
curam.ui.UIController.PAGE_ASSOCIATIONS[_4e.pageID]=null;
}
}else{
throw "initializePageAssociations did not recieve a valid response.";
}
},getTabDescriptorForPage:function(_50,_51,_52){
var _53=_51[_50];
if(!curam.ui.UIController.isPageAssociationInitialized(_50,_51)){
throw "Page associations have not been initialized for: "+_50;
}
if(_53!=null){
var _54=curam.ui.UIController.getTabFromMappings(_53.tabIDs,curam.tab.getSelectedTab(),_52);
return new curam.tab.TabDescriptor(_53.sectionID,_54);
}else{
return null;
}
},getTabFromMappings:function(_55,_56,_57){
if(!_56){
if(_55.length==1){
return _55[0];
}else{
if(_55.length>1){
if(_57&&_57.length>0){
for(var i=0;i<_57.length;i++){
if(_55.indexOf(_57[i])>=0){
return _57[i];
}
}
}
throw "Home page mapped to multiple tabs";
}
}
}
if(_55.length>1&&_57&&_57.length>0){
for(var i=0;i<_57.length;i++){
if(_55.indexOf(_57[i])>=0){
return _57[i];
}
}
}
var _58=_56.tabDescriptor.tabID;
for(var i=0;i<_55.length;i++){
if(_58==_55[i]){
return _58;
}
}
if(_55.length==1){
return _55[0];
}else{
if(_55.length>1){
throw {name:curam.ui.UIController.DUPLICATE_TAB_MAPPING_ERROR,message:"ERROR: The page that you are trying to link to is associated with "+"multiple tabs: ["+_55.toString()+"]. Therefore the "+"tab to open cannot be determined and the page will open in the "+"current tab. Please report this error.",tabID:_58};
}else{
}
}
},handleUIMPageID:function(_59,_5a){
var _5b=_5a?true:false;
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.handling.uim")+" '"+_59+"'. Page is "+(_5b?"":"not ")+_9.getProperty("curam.ui.UIController.default.sec"));
curam.ui.UIController.handlePageRequest(new curam.ui.PageRequest(_59+"Page.do",_5b));
},processURL:function(url){
var _5c=new curam.ui.PageRequest(url);
curam.ui.UIController.handlePageRequest(_5c);
},handlePageRequest:function(_5d,_5e){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.handling.page")+" '"+_5d.pageID+"'. "+_9.getProperty("curam.ui.UIController.panel.will")+(_5d.forceRefresh?"":_9.getProperty("curam.ui.UIController.not"))+_9.getProperty("curam.ui.UIController.reload"));
var _5f=curam.ui.UIController.checkResolvePage(_5d,_5d.forceRefresh,null,_5e);
if(_5f==true){
curam.ui.UIController.checkPage(_5d,{preferredTabs:_5e});
}
},checkResolvePage:function(_60,_61,_62,_63,_64,_65,_66){
var _67={unmappedPageLoader:_62,moreThanOneTabMappedCallback:_64,shouldLoadPage:_65,successCallback:_66,preferredTabs:_63};
if(_61){
return true;
}
var _68=curam.ui.UIController.RESOLVE_PAGES[_60.pageID];
if(_68==false){
return true;
}else{
var _69;
if(_60.getURL().indexOf("?")==-1){
_69="?";
}else{
_69="&";
}
var loc=curam.config?curam.config.locale+"/":"";
_7.post({url:loc+_60.getURL()+_69+"o3resolve=true",handleAs:"text",preventCache:true,load:dojo.hitch(curam.ui.UIController,"resolvePageCheckSuccess",_60,_67),error:dojo.hitch(curam.ui.UIController,"resolvePageCheckFailure",_60,_67)});
return false;
}
},resolvePageCheckSuccess:function(_6a,_6b,_6c,_6d){
var _6e=false;
var _6f;
var _70;
var _71;
if(_6c.substring(2,0)=="{\""&&_6c.charAt(_6c.length-1)=="}"){
_6e=true;
_6c=_3.parse(_6c,true);
_6f=_6c.pageID;
_70=_6c.pageURL;
}else{
_6e=false;
}
if(_6e&&_6a.pageID!=_6f){
curam.ui.UIController.RESOLVE_PAGES[_6a.pageID]=true;
_70=_70.replace("&amp;o3resolve=true","");
_70=_70.replace("&o3resolve=true","");
_70=_70.replace("o3resolve=true","");
for(paramName in _6a.cdejParameters){
if(paramName.length>0&&paramName.indexOf("__o3")!=-1){
if(_70.indexOf("?")==-1){
_70+="?"+paramName+"="+encodeURIComponent(_6a.cdejParameters[paramName]);
}else{
_70+="&"+paramName+"="+encodeURIComponent(_6a.cdejParameters[paramName]);
}
}
}
_71=new curam.ui.PageRequest(_70);
if(_6a.forceLoad){
_71.forceLoad=_6a.forceLoad;
}
}else{
curam.ui.UIController.RESOLVE_PAGES[_6a.pageID]=false;
_71=_6a;
}
curam.ui.UIController.checkPage(_71,_6b);
},resolvePageCheckFailure:function(_72,_73,_74,_75){
curam.ui.UIController.RESOLVE_PAGES[_72.pageID]=false;
curam.ui.UIController.checkPage(_72,_73);
},setTabTitleAndName:function(_76,_77,_78){
var tab=curam.tab.getContainerTab(_76);
if(tab){
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.changing.tab")+" '"+_77+"', '"+_78+"'. "+_9.getProperty("curam.ui.UIController.descriptor.before"),tab.tabDescriptor);
dojo.query("h1.detailsTitleText",tab.domNode)[0].innerHTML=_77;
var _79=dojo.query("h1.detailsTitleText",tab.domNode)[0];
_79.setAttribute("title",_77);
tab.set("title",_78);
var _7a;
for(var i=0;i<_76.classList.length;i++){
if(_76.classList[i].indexOf("iframe-")==0){
_7a=_76.classList[i];
break;
}
}
dojo.publish("tab.title.name.finished",[{"title":_78,"frameid":_7a}]);
}else{
curam.ui.UIController._log(_9.getProperty("curam.ui.UIController.cannot.change")+" '"+_77+"', '"+_78+"'. "+_9.getProperty("curam.ui.UIController.iframe")+" '"+_76.id+"'.");
}
},handleLinkClick:function(_7b,_7c){
curam.ui.UIController._doHandleLinkClick(_7b,_7c,curam.tab.getContentPanelIframe(),curam.ui.UIController.handlePageRequest,curam.util.openModalDialog);
},_doHandleLinkClick:function(_7d,_7e,_7f,_80,_81){
var _82=_7d;
if(_7f){
var rtc=new curam.util.RuntimeContext(_7f.contentWindow);
var _83=null;
if(_7e){
_83=[{key:"o3frame",value:"modal"}];
}
_82=curam.util.setRpu(_7d,rtc,_83);
}
if(_7e&&curam.config&&curam.config.modalsEnabled!="false"){
var _84=_7e.openDialogFunction||_81;
var _85=_7e.args||[{href:_82},_7e.dialogOptions];
_84.apply(this,_85);
}else{
var _86=new curam.ui.PageRequest(_82);
_80(_86);
}
},handleDownLoadClickLegacy:function(_87){
require(["dojo/io/iframe"],function(_88){
var _89=_88.create("o3lrm_frame","");
_89.src=location.href.substring(0,location.href.lastIndexOf("/"))+decodeURIComponent(_87.replace(/\+/g," "));
return;
});
},handleDownLoadClick:function(_8a){
var _8b=curam.tab.getContentPanelIframe();
_8b.src=location.href.substring(0,location.href.lastIndexOf("/"))+decodeURIComponent(_8a.replace(/\+/g," "))+"&"+jsScreenContext.toRequestString();
return;
},_log:function(msg,_8c){
if(curam.debug.enabled()){
curam.debug.log("UI CONTROLLER: "+msg+(_8c?" "+dojo.toJson(_8c):""));
}
},_prepareWordLanding:function(){
var as1,as2;
require(["dojo/aspect","curam/tab"],function(asp){
as1=asp.before(curam.ui.UIController,"_doHandleTabEvent",function(ote,_8d){
var _8e=curam.tab.getTabContainer(ote.tabDescriptor.sectionID);
as2=asp.after(_8d,"findOpenTab",function(_8f){
if(!_8f){
ote.uimPageRequest.forceLoad=true;
tabs=_8e.getChildren();
for(var i=0;i<tabs.length;i++){
var _90=tabs[i];
var _91=_90.tabDescriptor;
if(_91&&_91.tabID==ote.tabDescriptor.tabID){
_8f=_90;
}
}
}
as2.remove();
return _8f;
});
as1.remove();
});
});
dojo.subscribe("curam/fileedit/aspect/release",function(){
if(as2!=null){
as2.remove();
}
as1.remove();
});
return as1;
}});
_6.register("curam/ui/UIController",curam.ui.UIController);
return curam.ui.UIController;
});
