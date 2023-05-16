//>>built
define("curam/util/ExpandableLists",["dojo/dom","dojo/dom-construct","dojo/_base/window","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/query","dojo/sniff","dijit/registry","curam/inspection/Layer","curam/util","curam/debug","curam/UIMController","curam/util/ui/refresh/RefreshEvent","curam/define","curam/contentPanel"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c){
curam.define.singleton("curam.util.ExpandableLists",{_minimumExpandedHeight:[],stateData:[],_LIST_ID_PREFIX:"list-id-",_ROW_ID_PREFIX:"row-id-",_EVENT_TOGGLE:"/curam/list/row/toggle",_EVENT_TYPE_EXPANDED:"Expanded",_EVENT_TYPE_COLLAPSED:"Collapsed",setupToggleHandler:function(){
dojo.ready(function(){
var _d=curam.util.ExpandableLists;
var _e=function(_f,_10,_11){
if(_f){
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.event1",[_11,_f,_10]));
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.event2",[_11]));
}
if(_11==_d._EVENT_TYPE_EXPANDED){
var _12=_d._getListData(_f);
var _13=dojo.filter(_12.expandedRows,function(_14){
return _14==_10;
});
if(_13.length==0){
_12.expandedRows.push(_10);
}
}else{
var _12=_d._getListData(_f);
_12.expandedRows=dojo.filter(_12.expandedRows,function(_15){
return _15!=_10;
});
if(_12.expandedRows.length==0){
_d._removeListData(_f);
}
}
curam.debug.log("curam.util.ExpandableLists.setupToggleHandler stateData: ",_d.stateData);
};
dojo.subscribe(_d._EVENT_TOGGLE,this,_e);
dojo.subscribe("/curam/page/refresh",this,_d._saveStateData);
});
},_saveStateData:function(){
var _16=curam.util.ExpandableLists;
curam.debug.log("/curam/page/refresh"+_c.getProperty("curam.util.ExpandableLists.refresh"),_16.stateData);
try{
dojo.forEach(_16.stateData,function(_17){
var _18=dojo.toJson(_17);
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.exception"),_18);
localStorage[_16._sanitizeKey(_17.listId)]=_18;
});
}
catch(e){
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.exception"),e);
}
},_sanitizeKey:function(key){
return key.replace("-","_");
},loadStateData:function(_19){
if(typeof (window.curamDialogRedirecting)!="undefined"){
curam.debug.log("curam.util.ExpandableLists.loadStateData "+_c.getProperty("curam.util.ExpandableLists.load.exit"));
return;
}
var _1a=curam.util.ExpandableLists;
var _1b=function(){
curam.debug.log("curam.util.ExpandableLists.loadStateData "+ +_c.getProperty("curam.util.ExpandableLists.load.for"),_19);
var _1c=localStorage[_1a._sanitizeKey(_19)];
if(_1c&&_1c!=""){
var _1d=dojo.fromJson(_1c);
var _1e=_7("table."+_1a._LIST_ID_PREFIX+_19);
dojo.forEach(_1d.expandedRows,function(_1f){
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.load.row"),_1f);
var _20=_7("tr."+_1a._ROW_ID_PREFIX+_1f,_1e[0]);
if(_20.length>0){
var _21=_20[0].prev("tr").children();
var _22=_21.children("a.list-details-row-toggle")[0];
if(_22){
_1a._toggleDetailsRow(_22);
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.load.button"+".disabled"));
}
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.load.row.disabled"));
}
});
localStorage.removeItem(_1a._sanitizeKey(_19));
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.load.no.data"));
}
};
dojo.ready(function(){
_1b();
});
},_getListData:function(_23){
var _24=curam.util.ExpandableLists.stateData;
var _25=dojo.filter(_24,function(_26){
return _26.listId==_23;
});
if(_25.length==0){
_25.push({listId:_23,expandedRows:[]});
_24.push(_25[0]);
}
return _25[0];
},_removeListData:function(_27){
var _28=curam.util.ExpandableLists;
_28.stateData=dojo.filter(_28.stateData,function(_29){
return _29.listId!=_27;
});
},toggleListDetailsRow:function(_2a){
if(_2a){
_2a=dojo.fixEvent(_2a);
dojo.stopEvent(_2a);
var _2b=_2a.currentTarget;
curam.util.ExpandableLists._toggleDetailsRow(_2b);
}
},_generateUimController:function(_2c){
var _2d=_7("td",_2c)[0];
var _2e=_7("div",_2c)[0];
var _2f=new curam.UIMController({uid:_6.get(_2e,"uid"),url:_6.get(_2e,"url"),iframeId:_6.get(_2e,"iframeId"),iframeClassList:_6.get(_2e,"iframeClassList"),loadFrameOnCreate:_6.get(_2e,"loadFrameOnCreate")});
_2d.appendChild(_2f.domNode);
if(_2e&&_2d){
_2d.removeChild(_2e);
}
return _2f;
},_toggleDetailsRow:function(_30){
curam.debug.log("curam.util.ExpandableLists._toggleDetailsRow "+_c.getProperty("curam.util.ExpandableLists.load.for"),_30);
var _31=curam.util.ExpandableLists;
var _32=_7(_30).closest("tr")[0];
var _33=_7(_32).next("tr")[0];
var _34=!_31.isDetailsRowExpanded(_33);
_31._handleStripingAndRoundedCorners(_32,_33,_34);
var _35=_7("div.uimController",_33);
var _36=null;
var _37=null;
if(_35==null||_35.length==0){
_37=_31._generateUimController(_33);
}else{
_36=_35[0];
_37=_9.byNode(_36);
}
if(typeof (_37)=="undefined"||_37==null){
throw "UIMController Dijit not found for node: "+_36;
}
var _38=_6.get(_37.frame,"src");
var _39=false;
_31.setDetailsRowExpandedState(_32,_33,_34,_30);
var def=new dojo.Deferred();
if(!_38||_38==null||_38==""){
_37.loadPage(def);
}else{
_39=true;
def.callback();
}
def.addCallback(function(){
var _3a=_37.hasInPageNavigation();
_39=_39||_3a;
if(_3a){
_37.showTabContainer(_34);
}
if(_39){
curam.util.ExpandableLists.resizeExpandableListAncestors(window);
}
var _3b=_34?_31._EVENT_TYPE_EXPANDED:_31._EVENT_TYPE_COLLAPSED;
var _3c=_31._findListId(_33);
var _3d=curam.util.getSuffixFromClass(_33,_31._ROW_ID_PREFIX);
dojo.publish(_31._EVENT_TOGGLE,[_3c,_3d,_3b]);
if(!curam.util.ExpandableLists._isExternalApp(window)){
var _3b=_34?"ListDetailsRow.Expand":"ListDetailsRow.Collapse";
var _3e={url:_6.get(_37.frame,"src"),eventType:_3b};
var _3f=curam.tab.getSelectedTab();
if(_3f){
var _40=curam.tab.getTabWidgetId(_3f);
curam.util.getTopmostWindow().dojo.publish("expandedList.toggle",[window.frameElement,_3e,_40]);
}
}
});
},_handleStripingAndRoundedCorners:function(_41,_42,_43){
var odd="odd";
var _44="even";
var _45="row-no-border";
var _46="odd-last-row";
var _47="even-last-row";
if(!curam.util.ExpandableLists._isLastRow(_41,_42)){
if(_5.contains(_41,odd)){
_5.add(_42,odd);
}else{
if(_5.contains(_41,_44)){
_5.add(_42,_44);
}
}
}else{
if(_43){
if(_5.contains(_41,_46)){
_5.remove(_41,_46);
_5.add(_41,odd);
_5.add(_42,odd);
_5.add(_42,_46);
}else{
if(_5.contains(_41,_47)){
_5.remove(_41,_47);
_5.add(_41,_44);
_5.add(_42,_44);
_5.add(_42,_47);
}
}
}else{
if(_5.contains(_41,odd)){
_5.remove(_41,odd);
_5.add(_41,_46);
_5.remove(_42,_46);
_5.remove(_42,odd);
}else{
if(_5.contains(_41,_44)){
_5.remove(_41,_44);
_5.add(_41,_47);
_5.remove(_42,_44);
_5.remove(_42,_47);
}
}
}
}
if(_43){
_5.add(_41,_45);
}else{
_5.remove(_41,_45);
}
if(_5.contains(_41,_45)){
_5.remove(_42,"collapsed");
}else{
_5.add(_42,"collapsed");
}
},setDetailsRowExpandedState:function(_48,_49,_4a,_4b){
var _4c=curam.util.ExpandableLists.isDetailsRowExpanded(_49);
_5.remove(_49,"collapsed");
if(!_4c){
_5.add(_49,"collapsed");
}
if(_48.style.display=="none"){
_49.setAttribute("style","display:none");
}else{
_49.removeAttribute("style");
}
if(_4b){
var _4d=_7("img",_4b)[0];
if(_4a){
_5.add(_4b,"expanded");
_6.set(_4b,"aria-expanded","true");
if(_4d){
_6.set(_4d,"src",require.toUrl("../themes/curam/images/chevron--down20-enabled.svg"));
}
}else{
_5.remove(_4b,"expanded");
_6.set(_4b,"aria-expanded","false");
if(_4d&&curam.util.isRtlMode()){
_6.set(_4d,"src",require.toUrl("../themes/curam/images/chevron--left20-enabled.svg"));
}else{
_6.set(_4d,"src",require.toUrl("../themes/curam/images/chevron--right20-enabled.svg"));
}
}
}
},_isLastRow:function(_4e,_4f){
return _5.contains(_4e,"even-last-row")||_5.contains(_4e,"odd-last-row")||_5.contains(_4f,"even-last-row")||_5.contains(_4f,"odd-last-row");
},isDetailsRowExpanded:function(_50){
return !_5.contains(_50,"collapsed");
},listRowFrameLoaded:function(_51,_52){
curam.debug.log("========= "+_c.getProperty("curam.util.ExpandableLists.page.load")+" =======");
curam.debug.log(_51);
curam.debug.log(dojo.toJson(_52));
var _53=_1.byId(_51);
if(!_53){
throw "List Row Expanded: No iframe found";
}
if(!_53._spExpListPageLoadListener){
_53._spExpListPageLoadListener="true";
dojo.addOnUnload(function(){
if(sessionStorage.getItem("addOnUnloadTriggeredByFileDownload")==null){
_6.set(_53,"src","");
}
});
}else{
if(!curam.util.ExpandableLists._isExternalApp(window)){
curam.contentPanel.publishSmartPanelExpListPageLoad(_53);
}
}
var _54=curam.util.ExpandableLists._findListId(_53);
var _55=curam.util.ExpandableLists.getMinimumExpandedHeight(_54);
var _56=_52.height;
if(_56<_55){
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.min.height",[_55]));
_56=_55;
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.height",[_56]));
}
curam.util.ExpandableLists._resizeIframe(_53,_56);
curam.util.ExpandableLists.resizeExpandableListAncestors(window);
curam.util.ExpandableLists._setFrameTitle(_53,_52);
if(!curam.util.ExpandableLists._isExternalApp(window)){
var _57=curam.tab.getSelectedTab();
if(_57){
var _58=curam.tab.getTabWidgetId(_57);
var _59=curam.util.getTopmostWindow();
_59.curam.util.Refresh.getController(_58).pageLoaded(_52.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
}
}
curam.debug.log("================================================");
},_resizeIframe:function(_5a,_5b){
_4.set(_5a,{height:_5b+"px"});
curam.util.ExpandableLists._addMediaPrintToIframe(_5a,_5b);
},_addMediaPrintToIframe:function(_5c,_5d){
var _5e=_5c.id;
var _5f=_5c.contentWindow.frames.parent;
var _60=0;
var _61=false;
var _62=false;
var _63=_5c.contentWindow.document;
_3.withDoc(_63,function(){
var _64=dojo.query(".person-container-panel")[0];
if(_64){
_61=true;
if(_8("ie")||_8("trident")){
var _65=_63.body;
_65.setAttribute("style","overflow: hidden !important");
}
}
if(dojo.query(".context-panel-wrapper")[0]){
_61=true;
}
if(_61){
var _66=dojo.query(".pane");
var _67=dojo.query(".intake-user")[0]||dojo.query(".pd-case-owner")[0]||dojo.query(".ic-case-owner")[0];
if(_66.length>1){
_60=_66[0].scrollHeight*(_66.length-1);
}else{
if(_67){
if(_67.scrollHeight>20){
_60=_67.scrollHeight;
}
}
}
if(dojo.query(".Intakecontainer-panel")[0]){
_62=true;
}
}
},this);
if(_61){
var css="";
if(_62){
css="@media print { ."+_5e+" {"+"min-height: calc( "+_60+"px + "+_5d+"px) !important;  "+"min-width: 1300px !important;  "+"-ms-transform: scale(0.85);"+"-ms-transform-origin: 0 0;"+"-moz-transform: scale(0.85);"+"-moz-transform-origin: 0 0;"+"-webkit-transform: scale(0.70);"+"-webkit-transform-origin: 0 0;} }";
}else{
css="@media print { ."+_5e+" {"+"min-height: calc( "+_60+"px + "+_5d+"px) !important;  "+"min-width: 1200px !important;  "+"-ms-transform: scale(0.9);"+"-ms-transform-origin: 0 0;"+"-moz-transform: scale(0.9);"+"-moz-transform-origin: 0 0;"+"-webkit-transform: scale(0.85);"+"-webkit-transform-origin: 0 0;} }";
}
}else{
var css="@media print { ."+_5e+" { min-height: calc( 300px + "+_5d+"px) !important;  } }";
}
var _68=_5f.window.document;
_3.withDoc(_68,function(){
if(_7("#"+_5c.id+"_print").length>0){
_1.byId(_5c.id+"_print").innerHTML=css;
}else{
_2.create("style",{id:_5c.id+"_print",innerHTML:css},_3.body());
}
});
},_setFrameTitle:function(_69,_6a){
_69.title=_69.title+" "+_6a.title;
},_findListId:function(_6b){
return curam.util.getSuffixFromClass(_7(_6b).closest("table")[0],curam.util.ExpandableLists._LIST_ID_PREFIX);
},resizeExpandableListAncestors:function(_6c){
curam.debug.log("curam.util.ExpandableLists.resizeExpandableListAncestors: ",_6c.location.href);
if(_6c&&_6c!==window.top&&typeof (_6c.frameElement)!="undefined"&&(_5.contains(_6c.frameElement,"expanded_row_iframe")||curam.util.ExpandableLists.isNestedUIM(_6c))){
var _6d=_6c.curam.util.getPageHeight();
curam.debug.log("curam.util.ExpandableLists"+".resizeExpandableListAncestors: "+_c.getProperty("curam.util.ExpandableLists.resize.height"),_6d);
curam.util.ExpandableLists._resizeIframe(_6c.frameElement,_6d);
curam.util.ExpandableLists.resizeExpandableListAncestors(_6c.parent);
}else{
curam.debug.log("curam.util.ExpandableLists"+".resizeExpandableListAncestors: "+_c.getProperty("curam.util.ExpandableLists.resize.end"));
return;
}
},isNestedUIM:function(_6e){
if(_6e&&_6e.jsScreenContext){
return _6e.jsScreenContext.hasContextBits("NESTED_UIM");
}else{
return false;
}
},_isExternalApp:function(_6f){
if(_6f&&_6f.jsScreenContext){
return _6f.jsScreenContext.hasContextBits("EXTAPP");
}else{
return false;
}
},setMinimumExpandedHeight:function(_70,_71){
curam.util.ExpandableLists._minimumExpandedHeight.push({listId:_70,minExpHeight:_71});
},getMinimumExpandedHeight:function(_72){
var _73=dojo.filter(curam.util.ExpandableLists._minimumExpandedHeight,function(_74){
return _74.listId==_72;
});
if(_73.length==1){
return _73[0].minExpHeight;
}else{
curam.debug.log(_c.getProperty("curam.util.ExpandableLists.default.height"),_72);
return 30;
}
}});
_a.register("curam/util/ExpandableLists",this);
return curam.util.ExpandableLists;
});
