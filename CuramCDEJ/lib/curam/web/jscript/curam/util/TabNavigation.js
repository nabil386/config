//>>built
dojo.require("curam.util.ResourceBundle");
define("curam/util/TabNavigation",["dijit/registry","dojo/dom","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","curam/inspection/Layer","curam/debug","curam/define","curam/util","curam/tab","curam/util/Refresh"],function(_1,_2,_3,_4,_5,_6,_7,_8){
curam.define.singleton("curam.util.TabNavigation",{CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",disabledItems:{},tabLists:{},init:function(_9,_a){
var _b=_9+"child-nav-selectChild";
var _c=dojo.subscribe(_b,"",function(){
curam.util.TabNavigation.onParentSelect(null,_9);
});
curam.tab.unsubscribeOnTabClose(_c,_a);
},onParentSelect:function(_d,_e){
var _f=_e+"-child-nav";
var _10=_1.byId(_f);
var _11=true;
if(!_d){
var _11=false;
var _12=_e+"-parent-nav";
var _13=_1.byId(_12);
_d=_13.selectedChildWidget;
}
if(_d.curamDoNoReload){
_11=false;
_d.setAttribute("curamDoNoReload",null);
}
var _14=_d.id+"-Stack";
var _15=_1.byId(_14);
var _16=_5.get(_15.get("srcNodeRef"),"page-ref");
if(!_16){
var _17=_15;
if(_17){
var _18=dojo.query("li.selected > div.link",_17.id)[0];
_16=_5.get(_18,"page-ref");
}else{
throw new Error("Could not find a page reference. The menu item '"+_d.id+"' has no page reference and no selected child item was found.");
}
}
if(_11){
var ifr=curam.util.TabNavigation.getIframe(_e);
if(dojo.isIE&&dojo.isIE<9){
ifrBody=ifr.contentWindow.document.body;
}else{
ifrBody=ifr.contentDocument.activeElement;
}
var _19=function(){
_10.selectChild(_15);
_3.set(_10.domNode,"visibility","visible");
_3.set(ifr,"visibility","visible");
_3.set(ifr,"opacity","1");
curam.debug.log("curam.util.TabNavigation.postIframeLoadHandler anon function calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
};
if(dojo.isIE&&dojo.isIE<9){
var lh=function(){
if(ifr.readyState=="complete"){
ifr.detachEvent("onreadystatechange",lh);
_19();
}
};
ifr.attachEvent("onreadystatechange",lh);
}else{
var dt=dojo.connect(ifr,"onload",null,function(){
dojo.disconnect(dt);
_19();
});
}
dojo.query("div.list",ifrBody).forEach(function(_1a){
_4.add(_1a,"hidden");
});
_3.set(ifr,"opacity","0");
_3.set(_10.domNode,"visibility","hidden");
curam.util.TabNavigation.loadIframe(_16,_e);
}
var _1b=curam.util.TabNavigation.childMenuExists(_d);
curam.util.TabNavigation.toggleChildMenu(_1b,_e);
},childMenuExists:function(_1c){
var _1d=_1c.id+"-Stack";
var _1e=dojo.query("#"+_1d+" ul");
if(_1e.length==0){
return false;
}else{
return true;
}
},toggleChildMenu:function(_1f,_20){
var _21=_20+"-navigation-tab";
var _22=_2.byId(_21);
var _23=dojo.query(".content-area-container",_22)[0];
var _24=dojo.query(".child-nav",_22)[0];
if(!_1f){
var _25="0px";
var _26=((_3.getComputedStyle(_23).direction=="ltr")?{left:_25}:{right:_25});
var _27={width:_25};
_3.set(_23,_26);
_3.set(_24,_27);
}else{
var _28=_5.get(_22,"child-menu-width");
var _26=((_3.getComputedStyle(_23).direction=="ltr")?{left:_28}:{right:_28});
var _27={width:_28};
_3.set(_23,_26);
_3.set(_24,_27);
}
},handleChildSelect:function(_29,_2a,_2b){
if(!curam.util.TabNavigation.isSelectable(_29.parentNode.id)){
dojo.stopEvent(dojo.fixEvent(_2b));
return false;
}
var ul=curam.util.TabNavigation.getNext(_29,"UL");
var _2c=ul.childNodes;
for(var i=0;i<_2c.length;i++){
_4.replace(_2c[i],"not-selected","selected");
}
_4.replace(_29.parentNode,"selected","not-selected");
var _2d=_5.get(_29,"page-ref");
curam.util.TabNavigation.loadIframe(_2d,_2a);
return true;
},isSelectable:function(_2e){
return !curam.util.TabNavigation.disabledItems[_2e];
},getNext:function(_2f,_30){
var _31=_2f.parentNode;
if(_31==null){
curam.debug.log(_8.getProperty("curam.util.TabNavigation.error",[_30]));
return null;
}
if(_31.nodeName===_30){
return _31;
}else{
var _31=curam.util.TabNavigation.getNext(_31,_30);
return _31;
}
},loadIframe:function(_32,_33){
var _34=curam.util.TabNavigation.getIframe(_33);
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_1.getEnclosingWidget(_34).domNode]);
_5.set(_34,"src",_32+"&"+this.getCacheBusterParameter());
},getIframe:function(_35){
var _36=_35+"-navigation-tab";
var _37=_2.byId(_36);
var _38=dojo.query("iframe",_37);
return _38[0];
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},setupOnParentSelect:function(_39,_3a,_3b){
var _3c=_2.byId(_39+"-navigation-tab");
var _3d=curam.tab.getContainerTab(_3c);
_3d.subscribe(_39+"-child-nav-startup",function(){
curam.util.TabNavigation.onParentSelect(null,_39);
var _3e=_3b.split(",");
for(tabID in _3e){
var _3f=curam.util.TabNavigation.findNavItem("navItem_"+this.id+"_"+_3e[tabID]);
if(_3f!=null){
_3f.set("curamVisible",false);
}
}
});
_3d.subscribe(_3a,function(_40){
curam.util.TabNavigation.onParentSelect(_40,_39);
});
},setupRefresh:function(_41){
curam.util.Refresh.setNavigationCallbacks(curam.util.TabNavigation.updateNavItemStates,curam.util.TabNavigation.getRefreshParams);
},getRefreshParams:function(_42){
curam.debug.log("curam.util.TabNavigation.getRefreshParams(%s)",_42);
var _43=curam.util.TabNavigation.dynamicNavigationData[_42];
if(!_43){
curam.debug.log(_8.getProperty("curam.util.TabNavigation.no.dynamic"));
return null;
}
var _44="navId="+_43.navigationId;
_44+="&navItemIds="+curam.util.toCommaSeparatedList(_43.dynamicNavItemIds);
_44+="&navLoaders="+curam.util.toCommaSeparatedList(_43.dynamicNavLoaders);
_44+="&navPageParameters="+_43.pageParameters;
return _44;
},updateNavItemStates:function(_45,_46){
var _47=_46.navData;
for(var i=0;i<_47.itemStates.length;i++){
curam.util.TabNavigation.updateNavItemState(_47.itemStates[i],_45);
}
curam.util.TabNavigation._updateAriaMarkupForEnabledAndDisabledNavigationTabs();
},_updateAriaMarkupForEnabledAndDisabledNavigationTabs:function(){
var _48;
var _49;
var _4a="true";
var _4b=[];
var _4c=dojo.query(".dijitTabContainerTop-tabs");
var _4d=new curam.util.ResourceBundle("TabNavigation");
_4c.forEach(function(_4e){
_48=dojo.query(".tabLabel",_4e);
if(_48.length>0&&_4a=="true"){
_49=0;
_4b=[];
_4a="false";
}
_48.forEach(function(tab){
var _4f=tab.id+"_tabButtonContainer";
var _50=dojo.byId(_4f);
if(!_4.contains(_50,"disabled")&&(_4.contains(_50,"visible")||_4.contains(_50,"enabled, dijitChecked"))){
_5.set(tab,"aria-disabled","false");
}else{
_5.set(tab,"aria-disabled","true");
_49++;
var _51=_5.get(tab,"title");
if(typeof (_51)!="undefined"){
_4b.push(" "+_51);
}
}
});
_48.forEach(function(tab,_52){
var _53=_5.get(tab,"aria-disabled");
if(_53=="false"){
var _54;
var _55=_48[_52].innerHTML;
tab.setAttribute("title",_55);
if(_49==1){
tab.removeAttribute("aria-labelledby");
_54=". "+_4d.getProperty("curam.navigation.tab")+" "+(_52+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_48.length+". "+_49+" "+_4d.getProperty("curam.single.disabled.navigation.tab")+": "+_4b+".";
tab.setAttribute("aria-label",_55+_54);
}else{
if(_49>1){
tab.removeAttribute("aria-labelledby");
_54=". "+_4d.getProperty("curam.navigation.tab")+" "+(_52+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_48.length+". "+_49+" "+_4d.getProperty("curam.multiple.disabled.navigation.tab")+": "+_4b+".";
tab.setAttribute("aria-label",_55+_54);
}else{
_54=". "+_4d.getProperty("curam.navigation.tab")+" "+(_52+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_48.length+". ";
tab.setAttribute("aria-label",_55+_54);
}
}
}else{
tab.removeAttribute("role");
tab.removeAttribute("tabindex");
tab.removeAttribute("aria-label");
}
});
_4a="true";
});
},updateNavItemState:function(_56,_57){
var _58=curam.util.TabNavigation.findNavItem("navItem_"+_57+"_"+_56.id);
if(_58!=null){
if(!_58.domNode){
curam.util.TabNavigation.disabledItems[_58.id]=!_56.enabled;
curam.util.swapState(_58,_56.enabled,"enabled","disabled");
curam.util.swapState(_58,_56.visible,"visible","hidden");
var _59=_5.get(_58,"class").indexOf("disabled")>0?true:false;
if(_59){
_5.set(_58.children[0],"aria-disabled","true");
}
}else{
_58.set("curamDisabled",!_56.enabled);
_58.set("curamVisible",_56.visible);
}
}
},findNavItem:function(_5a){
var _5b=dojo.query("."+_5a);
if(_5b.length==1){
var _5c=_5b[0];
var _5d=dijit.byNode(_5c);
if(!_5d){
return _5c;
}else{
return _5d.controlButton;
}
}else{
curam.debug.log(_8.getProperty("curam.util.TabNavigation.item",[_5a]));
return null;
}
},addRollOverClass:function(_5e){
_4.add(_5e.target,"hover");
curam.util.connect(_5e.target,"onmouseout",function(){
_4.remove(_5e.target,"hover");
});
},setupOnLoadListener:function(_5f,_60){
var _61=dojo.fromJson(_60);
var _62=function(_63,_64){
curam.util.TabNavigation.handleContentAreaUpdate(_63,_64,_61);
};
var _65=curam.tab.getHandlerForTab(_62,_5f);
var _66=curam.util.getTopmostWindow();
var _67=_66.dojo.subscribe("/curam/main-content/page/loaded",null,_65);
curam.tab.unsubscribeOnTabClose(_67,_5f);
},setupTabList:function(_68,_69){
if(!curam.util.TabNavigation.tabLists[_68]){
curam.tab.executeOnTabClose(function(){
delete curam.util.TabNavigation.tabLists[_68];
},_68);
}
delete curam.util.TabNavigation.tabLists[_68];
curam.util.TabNavigation.tabLists[_68]=_69;
},handleContentAreaUpdate:function(_6a,_6b,_6c){
var ids=_6c[_6a];
if(ids){
var _6d=ids["dojoTabId"];
var _6e=_6d+"-parent-nav";
var _6f=ids["tabId"];
var _70=ids["childId"];
var _71=_1.byId(_6f);
var _72=_1.byId(_6e);
if(_71){
if(_72.selectedChildWidget!=_71){
_71.setAttribute("curamDoNoReload",true);
_72.selectChild(_71);
}
if(_70){
var _73=_6f+"-Stack";
var _74=_6d+"-child-nav";
var _75=_1.byId(_74);
var _76=_1.byId(_73);
_75.selectChild(_76);
var _77=dojo.query("li",_76.domNode);
for(var i=0;i<_77.length;i++){
var _78=_77[i];
if(_78.id==_70){
var _79=_78;
}
}
if(_79){
if(!_4.contains(_79,"selected")){
var _7a=_79.parentNode.childNodes;
for(var i=0;i<_7a.length;i++){
_4.replace(_7a[i],"not-selected","selected");
}
_4.replace(_79,"selected","not-selected");
}
}
}
}
}
},getInsertIndex:function(_7b,_7c,_7d){
var _7e=curam.util.TabNavigation.tabLists[_7b];
var _7f=dojo.indexOf(_7e,_7d);
var _80=_7f;
for(var i=_7f-1;i>=0;i--){
if(dojo.indexOf(_7c,_7e[i])<0){
_80--;
}
}
return _80;
}});
_7.register("curam/util/TabNavigation",curam.util.TabNavigation);
return curam.util.TabNavigation;
});
