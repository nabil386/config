//>>built
define("curam/ui/UIMPageAdaptor",["dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/query","curam/tab","curam/define","curam/debug","curam/util/ui/form/renderer/CTListEditRendererBlankedDisplayValue","curam/util","curam/ui/PageRequest"],function(_1,_2,_3,_4,_5,_6,_7,_8){
_6.singleton("curam.ui.UIMPageAdaptor",{initialize:function(){
if(jsScreenContext.hasContextBits("MODAL")){
return;
}
curam.util.connect(dojo.body(),"onclick",curam.ui.UIMPageAdaptor.clickHandler);
var _9=null;
var _a=null;
if(!jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
_9=curam.util.getTopmostWindow().dojo;
_a="/iframe-loaded/"+window.jsPageID;
_7.log(_7.getProperty("curam.ui.UIMPageAdaptor.event")+_a);
_9.publish(_a);
}
},externalInitialize:function(){
if(jsScreenContext.hasContextBits("MODAL")){
return;
}
curam.util.connect(dojo.body(),"onclick",curam.ui.UIMPageAdaptor.clickHandler);
},externalClickHandler:function(_b,_c){
var _d=new curam.ui.PageRequest(_c.href);
var _e=window.top.dijit.byId("curam-app");
if(_e!=null){
var _f=[];
var i=0;
for(param in _d.parameters){
_f[i]={paramKey:param,paramValue:_d.parameters[param]};
i=i+1;
}
var _10={pageID:_d.pageID,param:_f};
if(_e._isNavBarItem(_d.pageID)){
dojo.stopEvent(_b||window.event);
window.top.displayContent(_10);
}else{
if(_e._isUIMFragment(_d.pageID)){
dojo.stopEvent(_b||window.event);
window.top.displayContent(_10);
}
}
}
},clickHandler:function(_11){
var _12=null;
if(_11.target.nodeName=="A"){
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_11.target)){
return;
}
_12=_11.target;
}else{
if((_11.target.nodeName=="IMG"&&!_3.contains(_11.target.parentNode,"file-download"))||(_11.target.nodeName=="SPAN"&&(_11.target.className=="middle"||_11.target.className=="bidi"))){
_12=_4(_11.target).closest("A")[0];
}
}
if(_12!=null){
if(!_12.href||_12.href.length==0){
dojo.stopEvent(_11||window.event);
return;
}
if(jsScreenContext.hasContextBits("EXTAPP")){
curam.ui.UIMPageAdaptor.externalClickHandler(_11,_12);
}else{
dojo.stopEvent(_11||window.event);
if(curam.ui.UIMPageAdaptor.shouldLinkOpenInNewWindow(_12)){
window.open(_12.href);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_12)){
var _13=new curam.ui.PageRequest(_12.href);
var _14=function(){
curam.tab.getTabController().handlePageRequest(_13);
};
var _15=false;
if(jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||jsScreenContext.hasContextBits("NESTED_UIM")){
_13.pageHolder=window;
}
if(window.jsPageID===_13.pageID){
_13.forceLoad=true;
var _16=new _8();
if(_13.pageID){
_15=true;
_16.resetRegisteredDefaultedDropdownsJsonInSession(_13.pageID+"Page.do",_14);
}
}
if(!_15){
_14();
}
}
}
}
}
},allowLinkToContinue:function(_17){
if(_17&&_17._submitButton){
return true;
}
if(_17&&_17.href){
return (_17.href.indexOf("/servlet/FileDownload")!=-1||_17.href.indexOf("#")!=-1||_17.href.substr(0,7)=="mailto:");
}else{
return false;
}
},isLinkValidForTabProcessing:function(_18){
if(!_18||(_3.contains(_18,"popup-action")||_3.contains(_18,"list-details-row-toggle"))||_18.protocol=="javascript:"){
return false;
}
return true;
},shouldLinkOpenInNewWindow:function(_19){
return _2.has(_19,"target")&&!curam.util.isInternal(_19.href);
},setTabTitleAndName:function(){
var _1a=_1.byId("tab-title").innerHTML;
var _1b=_1.byId("tab-name").innerHTML;
window.parent.dojo.publish("tab.title.name.set",[window.frameElement,_1a,_1b]);
}});
return curam.ui.UIMPageAdaptor;
});
