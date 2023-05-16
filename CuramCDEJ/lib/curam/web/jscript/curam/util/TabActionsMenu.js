//>>built
define("curam/util/TabActionsMenu",["dijit/registry","dojo/query","curam/inspection/Layer","curam/tab","curam/debug","curam/define","curam/util","curam/util/Refresh"],function(_1,_2,_3,_4,_5){
curam.define.singleton("curam.util.TabActionsMenu",{_tabMenuStates:{},maxNumToDisplayInline:2,inlineMenuConfig:{contentPanelID:"",numItemsDisplayedInline:"",inlineItemsDisplayed:[],numOverflowItemsDisplayed:null},classNameHidden:"bx--btn-hidden",getRefreshParams:function(_6){
curam.debug.log("curam.util.TabActionsMenu.getRefreshParams(%s)",_6);
if(!curam.util.TabActionsMenu.dynamicMenuBarData[_6]){
curam.debug.log(_5.getProperty("curam.util.TabActionsMenu.no.dynamic"));
return null;
}
var _7="menuId="+curam.util.TabActionsMenu.dynamicMenuBarData[_6].menuBarId;
_7+="&menuItemIds="+curam.util.toCommaSeparatedList(curam.util.TabActionsMenu.dynamicMenuBarData[_6].dynamicMenuItemIds);
_7+="&menuLoaders="+curam.util.toCommaSeparatedList(curam.util.TabActionsMenu.dynamicMenuBarData[_6].dynamicMenuLoaders);
_7+="&menuPageParameters="+curam.util.TabActionsMenu.dynamicMenuBarData[_6].pageParameters;
return _7;
},updateMenuItemStates:function(_8,_9){
var _a=_9.menuData;
for(var i=0;i<_a.itemStates.length;i++){
var _b=_a.itemStates[i];
dojo.publish("/curam/tabactions/menuitemstate",[_8,_b.id,_b.enabled,_b.visible]);
}
curam.util.TabActionsMenu.manageInlineTabMenuStates(_8);
var _c=function(){
for(var i=0;i<_a.itemStates.length;i++){
curam.util.TabActionsMenu.updateMenuItemState(_a.itemStates[i],_8);
}
};
if(curam.util.TabActionsMenu._isMenuCreated(_8)){
_c();
}else{
var _d=curam.util.getTopmostWindow();
var _e=_d.dojo.subscribe("/curam/menu/created",this,function(_f){
_5.log("Received /curam/menu/created "+_5.getProperty("curam.util.ExpandableLists.load.for"),_f);
if(_f==_8){
_5.log(_5.getProperty("curam.util.TabActionsMenu.match"));
curam.util.TabActionsMenu._tabMenuStates[_f]=true;
_c();
_d.dojo.unsubscribe(_e);
}
});
curam.tab.unsubscribeOnTabClose(_e,_8);
}
},_isMenuCreated:function(_10){
return curam.util.TabActionsMenu._tabMenuStates[_10]==true;
},updateMenuItemState:function(_11,_12){
var _13=_1.byId("menuItem_"+_12+"_"+_11.id);
var _14=_13!=null?_13:_1.byId("overflowItem_"+_12+"_"+_11.id);
if(_14!=null){
_14.disabled=!_11.enabled;
curam.util.swapState(_14.domNode,_11.enabled,"enabled","disabled");
curam.util.swapState(_14.domNode,_11.visible,"visible","hidden");
if(_14.disabled){
_14.domNode.setAttribute("aria-disabled","true");
}
}
},setupHandlers:function(_15){
curam.util.Refresh.setMenuBarCallbacks(curam.util.TabActionsMenu.updateMenuItemStates,curam.util.TabActionsMenu.getRefreshParams);
},handleOnClick:function(url,_16){
if(_16){
curam.tab.getTabController().handleDownLoadClick(url);
}else{
curam.tab.getTabController().handleLinkClick(url);
}
},handleOnClickModal:function(url,_17){
var _18={dialogOptions:_17};
curam.tab.getTabController().handleLinkClick(url,_18);
},updateInlineTabMenuState:function(){
dojo.subscribe("/curam/tabactions/menuitemstate",this,function(_19,id,_1a,_1b){
var _1c="inlinedItem_"+_19+"_"+id;
var _1d=document.getElementById("inlinedItem_"+_19+"_"+id);
if(_1d){
if(_1a){
_5.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - ENABLING inlined item: "+_1c);
if(_1d.classList.contains("disabled")){
_1d.classList.remove("disabled");
}
if(_1d.hasAttribute("disabled")){
_1d.removeAttribute("disabled");
}
}else{
_5.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - DISABLING inlined item: "+_1c);
if(!_1d.classList.contains("disabled")){
_1d.classList.add("disabled");
}
_1d.setAttribute("disabled","");
}
if(_1b){
_5.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - DISPLAYING inlined item: "+_1c);
if(_1d.classList.contains(curam.util.TabActionsMenu.classNameHidden)){
_1d.classList.remove(curam.util.TabActionsMenu.classNameHidden);
}
}else{
_5.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - HIDING inlined item: "+_1c);
if(!_1d.classList.contains(curam.util.TabActionsMenu.classNameHidden)){
_1d.classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}
}
});
},_getTabMenuItemsDisplayedInline:function(_1e){
return _2("#"+_1e+" .bx--btn-tab-menu:not(."+curam.util.TabActionsMenu.classNameHidden+")");
},setInlinedTabMenuItemsDisplayed:function(_1f,_20){
var _21;
var _22=curam.util.TabActionsMenu.inlineMenuConfig;
if(_22&&_22.contentPanelID!=_1f){
_21=_20?_20:curam.util.TabActionsMenu._getTabMenuItemsDisplayedInline(_1f);
if(_21&&_21.length>0){
var _23={};
_23.contentPanelID=_1f;
_23.numItemsDisplayedInline=_21.length;
var _24=[];
for(var i=0;i<_21.length;i++){
var id=_21[i].id;
_24.push(id);
}
_23.inlineItemsDisplayed=_24;
var _22=curam.util.TabActionsMenu.inlineMenuConfig;
curam.util.TabActionsMenu.inlineMenuConfig=_23;
}
}
},hideTabMenuOverflowItems:function(_25,_26){
var _27=curam.util.TabActionsMenu.inlineMenuConfig;
if(_27&&_27.contentPanelID==_25){
var _28=_2("tr::not(.dijitMenuSeparator):not(.hidden)",_26);
var _29=_27.numOverflowItemsDisplayed;
var _2a=_29&&_29!=_28.length;
var _2b=!_29||_2a;
if(_28&&_28.length>0&&_2b){
if(_2a){
var _2c=_27.inlineItemsDisplayed;
if(_2c){
for(var x=0;x<_2c.length;x++){
var _2d=_2c[x];
var _2e=_2d.substring(_2d.indexOf(_25),_2d.length);
var _2f=document.getElementById("overflowItem_"+_2e);
if(_2f&&!_2f.classList.contains("hidden")){
_5.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - HIDING oveflow item: "+"overflowItem_"+_2e);
curam.util.swapState(_2f,false,"visible","hidden");
}
}
}
}else{
for(var i=0;i<_27.numItemsDisplayedInline;i++){
var _2f=_28[i];
if(_2f){
_5.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - dynamic update recored");
_5.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - HIDING oveflow item: "+"overflowItem_"+_2e);
curam.util.swapState(_2f,false,"visible","hidden");
}
}
}
_28=_2("tr::not(.dijitMenuSeparator):not(.hidden)",_26);
_5.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - setting number of overflow items to be: "+_28.length);
curam.util.TabActionsMenu.inlineMenuConfig.numOverflowItemsDisplayed=_28.length;
}
}
},manageInlineTabMenuStates:function(_30){
var _31=+curam.util.TabActionsMenu.maxNumToDisplayInline;
var _32=_31;
curam.util.TabActionsMenu.inlineMenuItemIds=[];
var _33=curam.util.TabActionsMenu._getTabMenuItemsDisplayedInline(_30);
var _34=_33&&_33.length>0;
var _35=[];
if(_34){
var _36=_33[0].parentNode;
if(_33.length>_31){
for(var i=_31;i<_33.length;i++){
_5.log("curam.util.TabActionsMenu.manageInlineTabMenuStates() - HIDING inlined tab action item: "+_33[i].id);
_33[i].classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}else{
_32=_33.length;
if(_32>0){
var _37=_2("img",_36);
if(_37&&_37.length>0){
_37[0].classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}
}
_5.log("curam.util.TabActionsMenu.manageInlineTabMenuStates() - REMOVING mask for action items on tab menu bar: "+_36.id);
_36.classList.remove(curam.util.TabActionsMenu.classNameHidden);
for(var i=0;i<_32;i++){
_35.push(_33[i]);
}
curam.util.TabActionsMenu.setInlinedTabMenuItemsDisplayed(_30,_35);
}
}});
_3.register("curam/util/TabActionsMenu",this);
return curam.util.TabActionsMenu;
});
