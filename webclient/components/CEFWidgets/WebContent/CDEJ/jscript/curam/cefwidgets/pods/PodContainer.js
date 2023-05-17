dojo.provide("curam.cefwidgets.pods.PodContainer");
require(["curam/cefwidgets/GridContainer/dojox/layout/GridContainerLayer"]);
var uw={columns:[],podHeights:{},eventSubscriptions:[],eventConnections:[],init:function(){
uw.calendarMonthSelect=dojo.subscribe("monthCalendarStack-selectChild",uw.selected);
uw.eventSubscriptions.push(uw.calendarMonthSelect);
uw.podDrag=dojo.subscribe("/dojox/mdnd/drag/start",function(){
if(dojo.isIE<8){
uw.temporarilyDisableScrollBarsOnContentPanel();
uw.temporarilyDisableBodyElementScrollbars();
uw.temporarilyDisableHtmlElementScrollbars();
uw.temporarilyDisableIframeScrollbars();
}
});
uw.eventSubscriptions.push(uw.podDrag);
uw.podCloseListener=dojo.subscribe("/dojox/mdnd/close",function(){
uw.recordPodPosChange(true);
});
uw.eventSubscriptions.push(uw.podCloseListener);
uw.podDropListener=dojo.subscribe("/dojox/mdnd/drop",function(){
uw.recordPodPosChange(true);
uw.toggleSize();
});
uw.eventSubscriptions.push(uw.podDropListener);
uw.podListSizeChangeListener=dojo.subscribe("/curam/list/changed",function(_1){
uw.recordPodListSizeChange(_1.podId,_1.listId,_1.visibleItems);
});
uw.eventSubscriptions.push(uw.podListSizeChangeListener);
uw.inc=true;
uw.resizeConnection=dojo.connect(window,"onresize",uw,"toggleSize");
uw.eventConnections.push(uw.resizeConnection);
uw.recordPodPosChange(jsDefaultRecord);
uw.finishedLoadingPods=curam.util.getTopmostWindow().dojo.subscribe("pods.fullyloaded",uw,function(_2){
_2.gridContainer.enableDnd();
_2.uw.recordPodPosChange();
dojo.disconnect(uw.finishedLoadingPods);
});
uw.readyForMorePodsHandler=dojo.subscribe("pods.readyformore",uw,function(_3){
dojo.unsubscribe(uw.readyForMorePodsHandler);
uw.addMorePodsHandler=dojo.subscribe("pods.addmore",uw,function(_4){
dojo.unsubscribe(uw.addMorePodsHandler);
uw.showRemainingPods(_4);
});
uw.eventSubscriptions.push(uw.addMorePodsHandler);
setTimeout(function(){
dojo.publish("pods.addmore",[_3]);
},_3.podDropDelay);
dojo.addOnWindowUnload(function(){
dojo.forEach(uw.eventSubscriptions,dojo.unsubscribe);
dojo.forEach(uw.eventConnections,dojo.disconnect);
dojo.disconnect(uw.destoryListeners);
});
});
},toggleSize:function(){
var _5=dojo.byId("podContainer");
if(_5){
var _6=dojo.marginBox(_5).h;
dojo.style(_5,"height",(_6+(uw.inc?1:-1))+"px");
uw.inc=!uw.inc;
}
},temporarilyDisableIframeScrollbars:function(){
uw.iframe=curam.tab.getContentPanelIframe();
var _7=dojo.attr(uw.iframe,"scrolling");
dojo.attr(uw.iframe,"scrolling","no");
uw.dropListenerForIFrameScrollbars=dojo.subscribe("/dojox/mdnd/drop",function(){
dojo.attr(uw.iframe,"scrolling",_7);
dojo.unsubscribe(uw.dropListenerForIFrameScrollbars);
uw.toggleSize();
});
uw.eventSubscriptions.push(uw.dropListenerForIFrameScrollbars);
},temporarilyDisableHtmlElementScrollbars:function(){
var _8=document.getElementsByTagName("html")[0];
var _9=dojo.style(_8,"overflow");
var _a=dojo.style(_8,"overflowX");
var _b=dojo.style(_8,"overflowY");
uw.modifyScrollbar(_8,null,"hidden");
uw.modifyScrollbar(_8,"X","hidden");
uw.modifyScrollbar(_8,"Y","hidden");
uw.dropListenerForContentPanelScrollbars=dojo.subscribe("/dojox/mdnd/drop",function(){
uw.modifyScrollbar(_8,null,_9);
uw.modifyScrollbar(_8,"X",_a);
uw.modifyScrollbar(_8,"Y",_b);
dojo.unsubscribe(uw.dropListenerForContentPanelScrollbars);
});
uw.eventSubscriptions.push(uw.dropListenerForContentPanelScrollbars);
},temporarilyDisableBodyElementScrollbars:function(){
var _c=dojo.body();
var _d=dojo.style(_c,"overflow");
var _e=dojo.style(_c,"overflowX");
var _f=dojo.style(_c,"overflowY");
uw.modifyScrollbar(_c,null,"hidden");
uw.modifyScrollbar(_c,"X","hidden");
uw.modifyScrollbar(_c,"Y","hidden");
uw.dropListenerForBodyElementScrollbars=dojo.subscribe("/dojox/mdnd/drop",function(){
uw.modifyScrollbar(_c,null,_d);
uw.modifyScrollbar(_c,"X",_e);
uw.modifyScrollbar(_c,"Y",_f);
dojo.unsubscribe(uw.dropListenerForBodyElementScrollbars);
});
uw.eventSubscriptions.push(uw.dropListenerForBodyElementScrollbars);
},temporarilyDisableScrollBarsOnContentPanel:function(){
var _10=dojo.byId("content");
var _11=dojo.style(_10,"overflow");
var _12=dojo.style(_10,"overflowX");
var _13=dojo.style(_10,"overflowY");
uw.modifyScrollbar(_10,null,"hidden");
uw.modifyScrollbar(_10,"X","hidden");
uw.modifyScrollbar(_10,"Y","hidden");
uw.dropListenerForContentPanelScrollbars=dojo.subscribe("/dojox/mdnd/drop",function(){
uw.modifyScrollbar(_10,null,_11);
uw.modifyScrollbar(_10,"X",_12);
uw.modifyScrollbar(_10,"Y",_13);
dojo.unsubscribe(uw.dropListenerForContentPanelScrollbars);
});
uw.eventSubscriptions.push(uw.dropListenerForContentPanelScrollbars);
},modifyScrollbar:function(_14,_15,_16){
if(_15==="y"||_15==="Y"){
dojo.style(_14,"overflowY",_16);
}else{
if(_15==="y"||_15==="Y"){
dojo.style(_14,"overflowX",_16);
}else{
if(!_15||_15===null||_15===""){
dojo.style(_14,"overflow",_16);
}
}
}
},showRemainingPods:function(_17){
_17.enableDnd();
var _18=dojo.query(".hidden-pod",_17.domNode);
var _19=[];
dojo.forEach(_18,function(_1a){
var _1b=_1a.childNodes;
dojo.forEach(_1b,function(_1c){
var _1d=dojo.attr(_1c,"dojoType");
if(_1d&&_1d==="curam.cefwidgets.pods.PodSettings"){
dijit.registry.remove(dojo.attr(_1c,"widgetId"));
}
});
var _1e=dojo.attr(_1a,"class");
_1e+=" dragHandle";
var _1f=dojo.attr(_1a,"closable");
var _20=parseInt(dojo.attr(_1a,"column"),10);
var _21=dojo.attr(_1a,"dndType");
var _22=dojo.attr(_1a,"dragRestriction");
var id=dojo.attr(_1a,"id");
var _23=dojo.attr(_1a,"title");
var _24=dojo.attr(_1a,"toggleable");
var _25=dojo.attr(_1a,"podFilterExpandButtonAltText");
var _26=dojo.attr(_1a,"podFilterCollapseButtonAltText");
var _27=dojo.attr(_1a,"podCloseButtonAltText");
var _28=dojo.attr(_1a,"podToggleExpandButtonAltText");
var _29=dojo.attr(_1a,"podToggleCollapseButtonAltText");
var _2a=new curam.cefwidgets.pods.Pod({id:id,title:_23,closeable:_1f,column:_20,dndType:_21,dragRestriction:_22,toggleable:_24,podCloseButtonAltText:_27,podFilterExpandButtonAltText:_25,podFilterCollapseButtonAltText:_26,podToggleExpandButtonAltText:_28,podToggleCollapseButtonAltText:_29,"class":_1e,content:_1b});
_2a._placeSettingsWidgets();
_1a.parentNode.removeChild(_1a);
_19.push(_2a);
});
var row=99;
var _2b=_17.podDropDelay;
if(_19.length===0){
curam.util.getTopmostWindow().dojo.publish("pods.fullyloaded",[{gridContainer:_17,uw:uw}]);
}
var _2c=[];
dojo.forEach(_19,function(_2d){
_2d.row=row;
uwForTimeout=uw;
_2c.push(_2d.id);
setTimeout(function(){
_17.addChild(_2d,(_2d.column)-1,_2d.row);
_2c.pop(_2d.id);
if(_2c.length==0){
curam.util.getTopmostWindow().dojo.publish("pods.fullyloaded",[{gridContainer:_17,uw:uwForTimeout}]);
}
},_2b);
row++;
_2b+=_17.podDropDelay;
});
},recordPodPosChange:function(_2e){
if(jsDefaultRecord){
var xml="<user-page-config loadedFromDefault=\"true\">";
jsDefaultRecord=false;
}else{
var xml="<user-page-config>";
}
var _2f=dijit.registry.byClass("dojox.layout.GridContainer").toArray()[0];
var _30=_2f._grid;
var _31=0;
for(_31=0;_31<_30.length;_31++){
var col=_30[_31];
var _32=uw.columns[_31];
var _33=dojo.byId(_32);
if(!_33){
return;
}
var _34=[],id;
var _35=dojo.query("> div",dojo.byId(col.node.id));
var i=0;
for(i=0;i<_35.length;i++){
var _36=dojo.attr(_35[i],"closed");
id=dojo.attr(_35[i],"id");
if(!_36){
if(id){
_34.push(id);
}
}else{
if(_36){
uw.uncheckCheckboxContainer(id);
}
}
}
_33.value=_34.join(",");
var _37=0;
for(_37=0;_37<_34.length;_37++){
xml+="<pod-config";
xml+=" col=\""+_31+"\"";
xml+=" row=\""+_37+"\"";
xml+=" id =\""+_34[_37]+"\"";
xml+="/>";
}
}
xml+="</user-page-config>";
if(_2e){
var _38="4096";
dojo.xhrPost({url:"SavePodPositionsPage.do",load:function(_39,_3a,evt){
},error:function(_3b,_3c){
console.log("Error during Pod postition update. See PodContainer.js");
},content:{o3ctx:_38,podPositions:xml,pageID:jsPageID},mimetype:"text/json"});
}
},uncheckCheckboxContainer:function(_3d){
var _3e="cbc_"+_3d;
var _3f=dojo.byId(_3e);
var _40=_3f.childNodes[0];
_40.checked=false;
_40.defaultChecked=false;
},recordPodListSizeChange:function(_41,_42,_43){
var _44=_41+","+_42+","+_43;
var _45="4096";
dojo.xhrPost({url:"SavePodListSizePage.do",load:function(_46,_47,evt){
},error:function(_48,_49){
console.log("Error on post size update. See PodContainer.js");
},content:{o3ctx:_45,podListSize:_44,pageID:jsPageID},mimetype:"text/json"});
},registerColumn:function(idx,id){
uw.columns[Number(idx)]=id;
},cancelPodConfig:function(_4a,_4b,_4c){
var _4d="div[name='"+_4c[0]+"']";
var _4e=dojo.query(_4d)[0];
if(_4e._anim){
return;
}
if(_4e.isOpen===undefined){
_4e.isOpen=dojo.style(_4e,"display")!="none";
}
_4e._anim=dojo.fx.wipeOut({node:_4e,duration:500,onEnd:function(){
_4e.isOpen=false;
_4e._anim=null;
dojo.publish("wipeCompleted",[_4e]);
uw.resetSelections(_4e.id);
}});
_4e._anim.play();
},toggleWipe:function(id){
var _4f=dojo.byId(id);
if(_4f._anim){
return;
}
if(_4f.isOpen===undefined){
_4f.isOpen=dojo.style(_4f,"display")!="none";
}
_4f._anim=dojo.fx[_4f.isOpen?"wipeOut":"wipeIn"]({node:_4f,duration:500,onEnd:function(){
_4f.isOpen=_4f.isOpen?false:true;
uw.toggleSize();
_4f._anim=null;
dojo.publish("wipeCompleted",[_4f]);
}});
_4f._anim.play();
},invokePodSave:function(_50,_51,_52){
var _53=dojo.byId(_52[0]);
_53.value="true";
dojo.byId("mainForm").submit();
},processButtonEvent:function(_54,_55,_56,_57){
if(_55.type==="keyup"||_55.type==="keydown"){
if(CEFUtils.enterKeyPress(_55)!==true){
return false;
}
}
if(_55.type==="mouseover"||_55.type==="focus"){
dojo.addClass(_54,"hover");
}else{
if(_55.type==="mouseout"||_55.type==="blur"){
dojo.removeClass(_54,"hover");
dojo.removeClass(_54,"selected");
}else{
if((_55.type==="mousedown")||_55.type==="keydown"){
_54.className=_54.className+" selected";
}else{
if(_55.type==="mouseup"){
dojo.removeClass(_54,"selected");
}else{
if(_55.type==="click"||_55.type==="keyup"){
uw[_56](_54,_55,_57);
}
}
}
}
}
},openCustomizePanel:function(_58,_59,_5a){
if(_58.className.indexOf("opened-console")===-1){
_58.className="customize-button opened-console selected";
}else{
_58.className="customize-button closed-console";
}
uw.toggleWipe("podSelectPane");
},savePage:function(_5b,_5c,_5d){
curam.util.clickButton("__o3btn.CTL1");
},cancelCustomizePanel:function(_5e,_5f,_60){
uw.resetSelections("podSelectPane");
uw.toggleWipe("podSelectPane");
},openHelpPage:function(_61,_62,_63){
dojo.stopEvent(_62);
var _64=curam.config?curam.config.locale:jsL;
var _65;
if(_64.indexOf("en")!=-1){
_65="/help/index.html?"+jsPageID;
}else{
_65="/help_"+_64+"/index.html?"+jsPageID;
}
window.open(_65);
},pageRefresh:function(_66,_67,_68){
curam.util.Refresh.refreshPage(_67);
},pageReset:function(_69,_6a,_6b){
var _6c=dojo.byId(_6b[0]);
_6c.value=true;
dojo.byId("mainForm").submit();
},showTableRow:function(_6d,_6e){
var _6f=_6e.getElementsByTagName("tbody")[0];
for(var i=0;i<_6f.rows.length;i++){
if(dojo.hasClass(_6f.rows[i],"blocked")){
dojo.removeClass(_6f.rows[i],"blocked");
if(i==_6f.rows.length-1){
dojo._setOpacity(_6d,0.3);
dojo._setOpacity(_6d.firstChild,0.3);
dojo.style(_6d,"cursor","default");
}else{
dojo._setOpacity(_6d,1);
dojo._setOpacity(_6d.firstChild,1);
dojo.style(_6d,"cursor","pointer");
}
var _70=_6d.nextSibling;
dojo._setOpacity(_70,1);
dojo._setOpacity(_70.firstChild,1);
dojo.style(_70,"cursor","pointer");
break;
}
}
},hideTableRow:function(_71,_72){
var _73=_72.getElementsByTagName("tbody")[0];
var i;
for(i=_73.rows.length-1;i>-1;i--){
if(i==1){
dojo._setOpacity(_71,0.3);
dojo._setOpacity(_71.firstChild,0.3);
dojo.style(_71,"cursor","default");
}
if(!dojo.hasClass(_73.rows[i],"blocked")){
dojo.addClass(_73.rows[i],"blocked");
var _74=_71.previousSibling;
dojo._setOpacity(_74,1);
dojo._setOpacity(_74.firstChild,1);
dojo.style(_74,"cursor","pointer");
break;
}
if(i==1){
return false;
}
}
if(i>i){
dojo._setOpacity(_71,1);
dojo._setOpacity(_71.firstChild,1);
dojo.style(_71,"cursor","pointer");
}
},resetSelections:function(_75){
uw.toggleWipe(_75);
var _76=dojo.query("input",_75);
for(i=0;i<_76.length;i++){
inputelem=_76[i];
if(inputelem.nodeType==1&&(inputelem.type=="checkbox"||inputelem.type=="radio")){
var att=null;
att=inputelem.defaultChecked;
if(att==null||att==false){
inputelem.checked=false;
}else{
inputelem.checked=true;
}
}
}
},selected:function(_77){
console.debug("page selected "+_77.id);
var _78=dijit.byId("myStackContainer");
dijit.byId("previous").attr("disabled",_77.isFirstChild);
dijit.byId("next").attr("disabled",_77.isLastChild);
},updateListInfo:function(_79){
var _7a=dojo.byId(_79.listId);
var _7b=dojo.query(".visible-items",_7a)[0];
if(_7b){
_7b.innerHTML=_79.visibleItems;
if(_79.removedItems){
var _7c=dojo.query(".total-items",_7a)[0];
var _7d=_7c.innerHTML;
var _7d=_7d-_79.removedItems;
_7c.innerHTML=_7d;
}
}
},updateImage:function(_7e,_7f){
_7e.children[0].src=_7f;
}};

