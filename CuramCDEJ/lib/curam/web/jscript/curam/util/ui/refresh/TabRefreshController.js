//>>built
define("curam/util/ui/refresh/TabRefreshController",["dojo/_base/declare","curam/inspection/Layer","curam/debug","curam/util/ui/refresh/RefreshEvent"],function(_1,_2,_3){
var _4=_1("curam.util.ui.refresh.TabRefreshController",null,{EVENT_REFRESH_MENU:"/curam/refresh/menu",EVENT_REFRESH_NAVIGATION:"/curam/refresh/navigation",EVENT_REFRESH_CONTEXT:"/curam/refresh/context",EVENT_REFRESH_MAIN:"/curam/refresh/main-content",_tabWidgetId:null,_configOnSubmit:null,_configOnLoad:null,_handler:null,_lastSubmitted:null,_currentlyRefreshing:null,_ignoreContextRefresh:true,_initialMenuAndNavRefreshDone:false,_nullController:null,constructor:function(_5,_6){
this._configOnSubmit={};
this._configOnLoad={};
if(!_6){
this._nullController=true;
return;
}
this._tabWidgetId=_5;
dojo.forEach(_6.config,dojo.hitch(this,function(_7){
this._configOnSubmit[_7.page]=_7.onsubmit;
this._configOnLoad[_7.page]=_7.onload;
}));
_2.register("curam/util/ui/refresh/TabRefreshController",this);
},pageSubmitted:function(_8,_9){
new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONSUBMIT,_9);
_3.log("curam.util.ui.refresh.TabRefreshController: "+_3.getProperty("curam.util.ui.refresh.TabRefreshController.submit",[_8,_9]));
dojo.publish("curam/form/submit",[_8]);
if(this._configOnSubmit[_8]){
this._lastSubmitted=_8;
_3.log("curam.util.ui.refresh.TabRefreshController: "+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+"submit.notify"));
}
},pageLoaded:function(_a,_b){
var _c=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,_b);
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController.load",[_a,_b]));
if(this._currentlyRefreshing&&this._currentlyRefreshing.equals(_c)){
this._currentlyRefreshing=null;
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+"refresh"));
return;
}
var _d={};
if(_b==_c.SOURCE_CONTEXT_MAIN&&this._configOnLoad[_a]){
_d=this._configOnLoad[_a];
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".load.config"));
}
if(this._lastSubmitted){
var _e=this._configOnSubmit[this._lastSubmitted];
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".submit.config",[this._lastSubmitted]));
_d.details=_d.details||_e.details;
_d.menubar=_d.menubar||_e.menubar;
_d.navigation=_d.navigation||_e.navigation;
_d.mainContent=_d.mainContent||_e.mainContent;
this._lastSubmitted=null;
}
if(!this._nullController){
this._fireRefreshEvents(_d,this._ignoreContextRefresh,!this._initialMenuAndNavRefreshDone);
}
if(this._ignoreContextRefresh&&_b==_c.SOURCE_CONTEXT_MAIN){
this._ignoreContextRefresh=false;
}
if(!this._initialMenuAndNavRefreshDone){
this._initialMenuAndNavRefreshDone=true;
}
},_fireRefreshEvents:function(_f,_10,_11){
var _12=[];
if(_f.details){
if(_10){
curam.debug.log("curam.util.ui.refresh.TabRefreshController: ignoring the first CONTEXT refresh request");
}else{
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.context"));
_12.push(this.EVENT_REFRESH_CONTEXT+"/"+this._tabWidgetId);
}
}else{
if(!_10){
dojo.publish("curam/tab/contextRefresh");
}
}
if(_f.menubar||_11){
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.menu"));
_12.push(this.EVENT_REFRESH_MENU+"/"+this._tabWidgetId);
}
if(_f.navigation||_11){
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.nav"));
_12.push(this.EVENT_REFRESH_NAVIGATION+"/"+this._tabWidgetId);
}
if(_f.mainContent){
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.main"));
this._currentlyRefreshing=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN,null);
_12.push(this.EVENT_REFRESH_MAIN+"/"+this._tabWidgetId);
}
if(_12.length>0){
_3.log("curam.util.ui.refresh.TabRefreshController:"+_3.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.log",[_12.length,_12]));
this._handler(_12);
}
},setRefreshHandler:function(_13){
this._handler=_13;
},destroy:function(){
for(prop in this._configOnSubmit){
if(this._configOnSubmit.hasOwnProperty(prop)){
delete this._configOnSubmit[prop];
}
}
for(prop in this._configOnLoad){
if(this._configOnLoad.hasOwnProperty(prop)){
delete this._configOnLoad[prop];
}
}
this._configOnSubmit={};
this._configOnLoad={};
this._handler=null;
this._lastSubmitted=null;
this._currentlyRefreshing=null;
}});
return _4;
});
