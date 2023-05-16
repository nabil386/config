//>>built
define("curam/tab/TabSessionManager",["dojo/_base/declare","curam/debug","curam/tab","curam/util/AutoRecoveryAPI"],function(_1,_2,_3,_4){
var _5=_1("curam.tab.TabSessionManager",null,{init:function(_6){
if(_6){
this._directBrowseURL=_6;
}
new curam.ui.ClientDataAccessor().getRaw("/data/tab/get",dojo.hitch(this,this._restoreTabSession),dojo.hitch(this,this._handleGetTabFailure));
},_handleGetTabFailure:function(_7,_8){
var _9=curam.tab.getTabContainer();
var _a=dojo.toJson(_7);
this._log(_2.getProperty("curam.tab.TabSessionManager.error")+_a);
var _b=new dojox.layout.ContentPane({title:"Error",closable:true,content:"An error occurred. Try refreshing the browser or contact your "+"administrator if it persists. Error: "+_7.message});
_9.addChild(_b);
},_restoreTabSession:function(_c,_d){
var _e=[];
var _f=[];
var _10=[];
curam.tab.getTabController().MAX_NUM_TABS=_c.maxTabs;
curam.tab.getTabController().TABS_SEQUENTIAL_ORDER=_c.tabsInSequence;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_ENABLED=_c.progressWidgetEnabled;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_DEFAULT_THRESHOLD=_c.progressWidgetThreshold;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_TIMEOUT_MAX=_c.progressWidgetTimeoutMax;
curam.widget.ProgressSpinner.init();
var _11=this._isNewSession();
var _12=_11?null:this._getPrevSelectedTab();
var _13=this._getHomePageTab();
_12=_12?_12:_13;
this.tabSelected(_12);
_10[_13.sectionID]=true;
if(_c&&_c.tabs&&_c.tabs.length>0){
var _14=_c.tabs;
this._log(_2.getProperty("curam.tab.TabSessionManager.previous")+_14.length+" "+_2.getProperty("curam.tab.TabSessionManager.tabs"));
for(var i=0;i<_14.length;i++){
var _15=curam.tab.TabDescriptor.fromJson(_14[i]);
if(_15.tabSignature==_13.tabSignature){
if(!_11){
if(this._directBrowseURL){
_12=_15;
}else{
_13=_15;
}
}
}else{
if(_15.sectionID==_12.sectionID){
_e.push(_15);
}else{
_f.push(_15);
}
}
_10[_15.sectionID]=true;
}
if(_13.sectionID==_12.sectionID){
_e.unshift(_13);
}else{
_f.unshift(_13);
}
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.no.session"));
_e.push(_13);
}
this._restoreSectionTabs(_e,_12);
this._restoreSectionTabs(_f,null);
this._selectedTD=_12;
if(AUTORECOVERY_ENABLED==="true"){
this._restoreModal(_e,_f);
}
this._connectSelectionListeners(_10);
if(this._directBrowseURL){
var _16=this._createDirectBrowseClosure();
var _17=curam.util.getTopmostWindow();
var _18=_17.dojo.subscribe("/curam/main-content/page/loaded",null,function(_19,_1a){
var _1b=_16.getThis();
var _1c=_1b._directBrowseURL;
var _1d=_1b._selectedTD.tabContent.pageID;
if(_19===_1d){
require(["curam/util/Navigation"],function(nav){
nav.goToUrl(_1c);
});
_1b._selectedTD.tabContent.pageID=_1c.replace(/Page.do\??.*/,"");
_1b.tabSelected(_1b._selectedTD);
dojo.unsubscribe(_18);
}
});
}
},_createDirectBrowseClosure:function(){
var _1e=this;
return {getThis:function(){
return _1e;
}};
},_restoreSectionTabs:function(_1f,_20){
this._log(_2.getProperty("curam.tab.TabSessionManager.saved.tabs"));
for(var i=0;i<_1f.length;i++){
var _21=_1f[i];
this._log(_2.getProperty("curam.tab.TabSessionManager.saved.tab"),_21,i);
dojo.publish(curam.tab.getTabController().TAB_TOPIC,[new curam.ui.OpenTabEvent(_21,null,this._isOpenInBackground(_21,_20,i))]);
}
},_restoreModal:function(_22,_23){
this._log(_2.getProperty("curam.tab.TabSessionManager.restore.modal"));
this._tabLoadedCount=0;
this._tabCountForCurrentSection=_22.length;
this._tabCountForOtherSections=0;
var _24=[];
for(var i=0;i<_23.length;i++){
if(!_24.includes(_23[i].sectionID)){
_24.push(_23[i].sectionID);
this._tabCountForOtherSections++;
}
}
var _25=curam.util.getTopmostWindow().dojo.subscribe("/curam/main-content/page/loaded",this,function(_26,_27,_28){
this._tabLoadedCount++;
var _29=false;
if(_28.tabDescriptor.openInBackground===false){
_29=true;
}else{
if(this._tabCountForCurrentSection===0&&this._tabLoadedCount===this._tabCountForOtherSections){
_29=true;
}else{
if(this._tabCountForCurrentSection+this._tabCountForOtherSections<=1){
_29=true;
}
}
}
if(_29){
dojo.publish("curam/tab/restoreModal");
dojo.unsubscribe(_25);
}
});
},_connectSelectionListeners:function(_2a){
var _2b=false;
for(var _2c in _2a){
if(curam.tab.getTabContainer(_2c)){
dojo.subscribe(curam.tab.getTabContainer(_2c).id+"-selectChild",dojo.hitch(this,this.tabContentPaneSelected));
_2b=true;
}
}
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",dojo.hitch(this,this.tabSectionSelected));
return _2b;
},tabUpdated:function(_2d){
var _2e=sessionStorage.getItem("fileEditWigetDialog");
if(!_2e){
this._log(_2.getProperty("curam.tab.TabSessionManager.saving.tab"),_2d);
new curam.ui.ClientDataAccessor().set("/data/tab/update",_2d.toJson());
}else{
sessionStorage.removeItem("fileEditWigetDialog");
}
},tabClosed:function(_2f){
this._log(_2.getProperty("curam.tab.TabSessionManager.tab.closed"),_2f);
new curam.ui.ClientDataAccessor().set("/data/tab/close",_2f.toJson());
},tabSelected:function(_30){
this._log(_2.getProperty("curam.tab.TabSessionManager.selected.tab"),_30);
if(_30.tabSignature){
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_30.toJson();
this._log(_2.getProperty("curam.tab.TabSessionManager.recorded"),_30);
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.not.recorded"),_30);
}
curam.debug.log("curam.tab.TabSessionManager.tabSelected calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
},tabContentPaneSelected:function(_31){
if(_31.tabDescriptor){
this.tabSelected(_31.tabDescriptor);
dojo.publish("curam/tab/selected",[_31.id]);
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.no.descriptor"));
}
if(_31.isLoaded){
curam.util.TabActionsMenu.setInlinedTabMenuItemsDisplayed(_31.id);
}
},tabSectionSelected:function(_32){
var _33=false;
if(_32){
var id=_32.id;
this._log(_2.getProperty("curam.tab.TabSessionManager.new.section")+" '"+id+"'.");
var _34=id.substring(0,id.length-4);
var _35=curam.tab.getSelectedTab(_34);
if(_35){
this._log(_2.getProperty("curam.tab.TabSessionManager.changing.selection"));
this.tabContentPaneSelected(_35);
_33=true;
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.not.changing.selection"));
}
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.no.container"));
}
curam.debug.log("curam.tab.TabSessionManager.tabSectionSelected calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
return _33;
},_isNewSession:function(){
return false;
},_getPrevSelectedTab:function(){
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.tab"));
var _36;
_36=localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY];
var _37=null;
if(_36){
_37=curam.tab.TabDescriptor.fromJson(_36);
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.tab.found"),_37);
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.tab.not.found"));
}
return _37;
},_getPrevSelectedModal:function(){
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.modal"));
var _38;
_38=localStorage[curam.tab.TabSessionManager.PREVIOUSLY_SELECTED_MODAL_KEY];
var _39=null;
if(_38){
_39=curam.tab.TabDescriptor.fromJson(_38);
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.modal.found"),_39);
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.previous.modal.not.found"));
}
return _39;
},_isOpenInBackground:function(_3a,_3b,pos){
var _3c=true;
if(_3b&&_3b.tabSignature==_3a.tabSignature){
this._log(_2.getProperty("curam.tab.TabSessionManager.foreground"),_3a,pos);
_3c=false;
}else{
this._log(_2.getProperty("curam.tab.TabSessionManager.background"),_3a,pos);
}
return _3c;
},_getHomePageTab:function(){
this._log(_2.getProperty("curam.tab.TabSessionManager.home.page")+" '"+USER_HOME_PAGE_ID+"'.");
if(!USER_HOME_PAGE_TAB_ASSOC.tabIDs||!USER_HOME_PAGE_TAB_ASSOC.sectionID){
throw new Error("The application cannot be launched because the home page, '"+USER_HOME_PAGE_ID+"', has not been associated with a section or "+" tab.");
}
var _3d=USER_HOME_PAGE_TAB_ASSOC.tabIDs[0];
var _3e=USER_HOME_PAGE_TAB_ASSOC.sectionID;
var _3f=new curam.tab.TabDescriptor(_3e,_3d);
var _40=new curam.ui.PageRequest(USER_HOME_PAGE_ID,true);
_3f.isHomePage=true;
_3f.setTabSignature([],_40,true);
_3f.setTabContent(_40);
this._log(_2.getProperty("curam.tab.TabSessionManager.created"),_3f);
return _3f;
},_log:function(msg,_41,pos){
if(curam.debug.enabled()){
var _42="TAB SESSION";
if(typeof pos=="number"){
_42+=" [pos="+pos+"]";
}
curam.debug.log(_42+": "+msg+(_41?" "+_41.toJson():""));
}
}});
dojo.mixin(curam.tab.TabSessionManager,{SELECTED_TAB_KEY:"curam_selected_tab",PREVIOUSLY_SELECTED_MODAL_KEY:"curam_previously_selected_modal",SELECTED_TAB_SESSION_KEY:"curam_selected_tab_session"});
return _5;
});
