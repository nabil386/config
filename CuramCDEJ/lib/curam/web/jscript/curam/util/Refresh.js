//>>built
define("curam/util/Refresh",["dijit/registry","dojo/dom-class","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/ResourceBundle","curam/util","curam/tab","curam/util/ContextPanel","curam/util/ui/refresh/TabRefreshController"],function(_1,_2,_3,_4,_5,_6,_7,_8){
_6.singleton("curam.util.Refresh",{submitted:false,pageSubmitted:"",refreshConfig:[],menuBarCallback:null,navigationCallback:null,_controllers:{},_pageRefreshButton:undefined,setMenuBarCallbacks:function(_9,_a){
if(!curam.util.Refresh.menuBarCallback){
curam.util.Refresh.menuBarCallback={updateMenuItemStates:_9,getRefreshParams:_a};
}
},setNavigationCallbacks:function(_b,_c){
if(!curam.util.Refresh.navigationCallback){
curam.util.Refresh.navigationCallback={updateNavItemStates:_b,getRefreshParams:_c};
}
},refreshMenuAndNavigation:function(_d,_e,_f){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"tabWidgetId=%s, refreshMenuBar || refreshNavigation: %s || %s",_d,_e,_f);
if(!_e&&!_f){
curam.debug.log(_7.getProperty("curam.util.Refresh.no.refresh"));
return;
}
var _10={update:function(_11,_12,_13){
curam.debug.log(_7.getProperty("curam.util.Refresh.dynamic.refresh"),_12);
var ncb=curam.util.Refresh.navigationCallback;
curam.debug.log("refreshNavigation? ",_f);
if(_f&&_12.navData&&ncb){
ncb.updateNavItemStates(_11,_12);
}
var mcb=curam.util.Refresh.menuBarCallback;
curam.debug.log("refreshMenuBar? ",_e);
if(_e&&_12.menuData&&mcb){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"dynamic tab menu item update");
mcb.updateMenuItemStates(_11,_12);
}else{
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"no dynamic data, updating initially loaded tab action items to show"+"only those that should be inlined");
curam.util.TabActionsMenu.manageInlineTabMenuStates(_11);
}
},error:function(_14,_15){
curam.debug.log("========= "+_7.getProperty("curam.util.Refresh.dynamic.failure")+" ===========");
curam.debug.log(_7.getProperty("curam.util.Refresh.dynamic.error"),_14);
curam.debug.log(_7.getProperty("curam.util.Refresh.dynamic.args"),_15);
curam.debug.log("==================================================");
}};
var _16="servlet/JSONServlet?o3c=TAB_DYNAMIC_STATE_QUERY",_17=false;
var mcb=curam.util.Refresh.menuBarCallback;
if(_e&&mcb){
var _18=mcb.getRefreshParams(_d);
if(_18){
_16+="&"+_18;
_17=true;
}
}
var ncb=curam.util.Refresh.navigationCallback;
if(_f&&ncb){
var _19=ncb.getRefreshParams(_d);
if(_19){
_16+="&"+_19;
_17=true;
}
}
curam.debug.log(_7.getProperty("curam.util.Refresh.dynamic.refresh.req"));
if(_17){
_5.post({url:_16,handleAs:"json",preventCache:true,load:dojo.hitch(_10,"update",_d),error:dojo.hitch(_10,"error")});
}else{
curam.util.TabActionsMenu.manageInlineTabMenuStates(_d);
curam.debug.log(_7.getProperty("curam.util.Refresh.dynamic.refresh.no_dynamic_items"));
}
},addConfig:function(_1a){
var _1b=false;
dojo.forEach(curam.util.Refresh.refreshConfig,function(_1c){
if(_1c.tab==_1a.tab){
_1c.config=_1a.config;
_1b=true;
}
});
if(!_1b){
curam.util.Refresh.refreshConfig.push(_1a);
}
},setupRefreshController:function(_1d){
curam.debug.log("curam.util.Refresh.setupRefreshController "+_7.getProperty("curam.util.ExpandableLists.load.for"),_1d);
var _1e=_1.byId(_1d);
var _1f=_1e.tabDescriptor.tabID;
var _20=dojo.filter(curam.util.Refresh.refreshConfig,function(_21){
return _21.tab==_1f;
});
if(_20.length==1){
var _22=_20[0];
var ctl=new curam.util.ui.refresh.TabRefreshController(_1d,_22);
curam.util.Refresh._controllers[_1d]=ctl;
ctl.setRefreshHandler(curam.util.Refresh.handleRefreshEvent);
}else{
if(_20.length==0){
curam.debug.log(_7.getProperty("curam.util.Refresh.no.dynamic.refresh"),_1d);
var ctl=new curam.util.ui.refresh.TabRefreshController(_1d,null);
curam.util.Refresh._controllers[_1d]=ctl;
}else{
throw "curam.util.Refresh: multiple dynamic refresh "+"configurations found for tab "+_1d;
}
}
curam.tab.executeOnTabClose(function(){
curam.util.Refresh._controllers[_1d].destroy();
curam.util.Refresh._controllers[_1d]=undefined;
},_1d);
},getController:function(_23){
var ctl=curam.util.Refresh._controllers[_23];
if(!ctl){
throw "Refresh controller for tab '"+_23+"' not found!";
}
return ctl;
},handleOnloadNestedInlinePage:function(_24,_25){
curam.debug.log("curam.util.Refresh.handleOnloadNestedInlinePage "+_7.getProperty("curam.util.Refresh.iframe",[_24,_25]));
var _26=curam.util.getTopmostWindow();
var _27=undefined;
var _28=curam.tab.getSelectedTab();
if(_28){
_27=curam.tab.getTabWidgetId(_28);
}
if(_27){
curam.debug.log(_7.getProperty("curam.util.Refresh.parent"),_27);
_26.curam.util.Refresh.getController(_27).pageLoaded(_25.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
_26.dojo.publish("/curam/main-content/page/loaded",[_25.pageID,_27,_28]);
return true;
}
return false;
},handleRefreshEvent:function(_29){
var _2a=function(_2b){
curam.util.ContextPanel.refresh(_1.byId(_2b));
};
var _2c=function(_2d){
curam.tab.refreshMainContentPanel(_1.byId(_2d));
};
var _2e=function(_2f,_30,_31){
curam.util.Refresh.refreshMenuAndNavigation(_2f,_30,_31);
};
curam.util.Refresh._doRefresh(_29,_2a,_2c,_2e);
},_doRefresh:function(_32,_33,_34,_35){
var _36=null;
var _37=false;
var _38=false;
var _39=false;
var _3a=false;
var trc=curam.util.ui.refresh.TabRefreshController.prototype;
dojo.forEach(_32,function(_3b){
var _3c=_3b.lastIndexOf("/");
var _3d=_3b.slice(0,_3c);
if(!_36){
_36=_3b.slice(_3c+1,_3b.length);
}
if(_3d==trc.EVENT_REFRESH_MENU){
_37=true;
}
if(_3d==trc.EVENT_REFRESH_NAVIGATION){
_38=true;
}
if(_3d==trc.EVENT_REFRESH_CONTEXT){
_39=true;
}
if(_3d==trc.EVENT_REFRESH_MAIN){
_3a=true;
}
});
if(_39){
_33(_36);
}
if(_3a){
_34(_36);
}
_35(_36,_37,_38);
},setupRefreshButton:function(_3e){
dojo.ready(function(){
var _3f=dojo.query("."+_3e)[0];
if(!_3f){
throw "Refresh button not found: "+_3e;
}
curam.util.Refresh._pageRefreshButton=_3f;
var _40=window.location.href;
if(curam.util.isActionPage(_40)){
var _41=new _8("Refresh");
var _42=_41.getProperty("refresh.button.disabled");
_2.add(_3f,"disabled");
_3.set(_3f,"title",_42);
_3.set(_3f,"aria-label",_42);
_3.set(_3f,"role","link");
_3.remove(_3f,"href");
_3.set(_3f,"aria-disabled","true");
curam.util.Refresh._pageRefreshButton._curamDisable=true;
if(_3f.firstChild!=null){
_2.add(_3f.firstChild,"refresh-disabled");
_3.set(_3f.firstChild,"alt",_42);
}
}else{
_2.add(_3f,"enabled");
curam.util.Refresh._pageRefreshButton["_curamDisable"]=undefined;
}
curam.util.getTopmostWindow().curam.util.setupPreferencesLink(_40);
});
},refreshPage:function(_43){
dojo.stopEvent(_43);
var _44=window.location.href;
var _45=curam.util.Refresh._pageRefreshButton._curamDisable;
if(_45){
return;
}
curam.util.FORCE_REFRESH=true;
curam.util.redirectWindow(_44,true);
}});
_4.register("curam/util/Refresh",curam.util.Refresh);
return curam.util.Refresh;
});
