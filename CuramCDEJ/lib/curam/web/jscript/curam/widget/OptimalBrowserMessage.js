//>>built
require({cache:{"url:curam/widget/templates/OptimalBrowserMessage.html":"<div>\n  <div class=\"hidden\"\n       data-dojo-type=\"dojox/layout/ContentPane\"\n       data-dojo-attach-point=\"_optimalMessage\">\n  </div>\n</div>\n"}});
define("curam/widget/OptimalBrowserMessage",["dojo/_base/declare","dojo/_base/lang","dojo/dom","dojo/dom-class","curam/util","curam/util/UIMFragment","curam/ui/ClientDataAccessor","dijit/_Widget","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/form/Button","dojo/has","dojo/text!curam/widget/templates/OptimalBrowserMessage.html","dojo/dom-attr"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10){
return _1("curam.widget.OptimalBrowserMessage",[_8,_9,_a],{OPTIMAL_BROWSER_MSG:"optimal-browser-msg",isExternalApp:null,optimalBrowserMsgPaddingCSS:"optimal-browser-banner",optimalBrowserNode:null,appSectionsNode:null,appBannerHeaderNode:null,intApp:"internal",extApp:"external",templateString:_f,widgetsInTemplate:true,baseClass:"",optimalBrowserNodeID:"_optimalMessage",_appConfig:null,postMixInProperties:function(){
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this._init();
this._loadNodes(this._optimalMessage.id);
},_init:function(){
da=new _7();
da.getRaw("/config/tablayout/settings["+curam.config.appID+"]",_2.hitch(this,function(_11){
console.log("External App config data:"+_11);
this._appConfig=_11;
this._getAppConfig();
this._updateOptimalBrowserNode();
}),function(_12,_13){
console.log("External App config data load error:"+_12);
},null);
},_getAppConfig:function(){
var _14=this._appConfig.optimalBrowserMessageEnabled;
var _15=this._createStorageKey(this.OPTIMAL_BROWSER_MSG);
var _16=this;
if(_14=="true"|_14=="TRUE"){
return _16._isOptimalBrowserCheckDue(_15,_16);
}
return false;
},_updateOptimalBrowserNode:function(){
this.optimalBrowserNode=_3.byId(this._optimalMessage.id);
_10.set(optimalBrowserNode,"aria-label",this._appConfig.optimalMessageDivLabel);
},_isOptimalBrowserCheckDue:function(_17,_18){
var _19=localStorage[_17];
if(_19&&_19!=""){
if(new Date(_18._getTargetDate())>new Date(_19)){
_18._executeBrowserVersionCheck();
return true;
}
}else{
_18._executeBrowserVersionCheck();
return true;
}
return false;
},_executeBrowserVersionCheck:function(){
var _1a=this._appConfig.ieMinVersion;
var _1b=this._appConfig.ieMaxVersion;
var _1c=this._appConfig.ffMinVersion;
var _1d=this._appConfig.ffMaxVersion;
var _1e=this._appConfig.chromeMinVersion;
var _1f=this._appConfig.chromeMaxVersion;
var _20=this._appConfig.safariMinVersion;
var _21=this._appConfig.safariMaxVersion;
var _22=dojo.isIE;
var _23=_e("trident")||_e("edge");
var _24=dojo.isFF;
var _25=dojo.isChrome;
var _26=dojo.isSafari;
if(_22!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_22,_1a,_1b);
}else{
if(_23>6&&this.isExternalApp){
var _27=_1a-4;
var _28=_1b-4;
return this._isCurrentBrowserVerSupported(_23,_27,_28);
}else{
if(_24!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_24,_1c,_1d);
}else{
if(_25!=undefined){
return this._isCurrentBrowserVerSupported(_25,_1e,_1f);
}else{
if(_26!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_26,_20,_21);
}
}
}
}
}
return false;
},_isCurrentBrowserVerSupported:function(_29,_2a,_2b){
var _2c=false;
if(_2a>0){
if(_29<_2a){
_2c=true;
this._displayOptimalBrowserMsg();
return true;
}
}
if(_2b>0&&!_2c){
if(_29>_2b){
this._displayOptimalBrowserMsg();
return true;
}
}
return false;
},_displayOptimalBrowserMsg:function(){
this._addOrRemoveCssForInternalApp(true,this.optimalBrowserMsgPaddingCSS);
_6.get({url:"optimal-browser-msg-fragment.jspx",targetID:this._optimalMessage.id});
this._postRenderingTasks();
},_postRenderingTasks:function(){
var _2d=this._optimalMessage.id;
dojo.addOnLoad(function(){
var _2e=_3.byId(_2d);
_4.remove(_2e,_2e.className);
});
localStorage[this._createStorageKey(this.OPTIMAL_BROWSER_MSG)]=this._getTargetDate(this._appConfig.nextBrowserCheck);
},_loadNodes:function(_2f){
dojo.addOnLoad(function(){
this.optimalBrowserNode=_3.byId(_2f);
this.appSectionsNode=_3.byId("app-sections-container-dc");
this.appBannerHeaderNode=_3.byId("app-header-container-dc");
});
},_createStorageKey:function(_30){
if(this.isExternalApp){
_30=_30+"_"+this.extApp;
}else{
_30=_30+"_"+this.intApp;
}
return _30;
},_addOrRemoveCssForInternalApp:function(_31,_32){
var _33=this.isExternalApp;
dojo.addOnLoad(function(){
if(!_33){
if(_31){
_4.add(this.appSectionsNode,_32);
if(this.appBannerHeaderNode){
_4.add(this.appSectionsNode.children.item(1),_32);
_4.add(this.appSectionsNode.children.item(2),_32);
}
}else{
_4.remove(this.appSectionsNode,_32);
if(this.appBannerHeaderNode){
_4.remove(this.appSectionsNode.children.item(1),_32);
_4.remove(this.appSectionsNode.children.item(2),_32);
}
}
}
});
},_getTargetDate:function(_34){
var _35=new Date();
if(_34==undefined){
_35.setDate(_35.getDate());
}else{
_35.setDate(_35.getDate()+_34);
}
return _35.toUTCString();
},exitOptimalBrowserMessageBox:function(){
var _36=_3.byId(this._optimalMessage.id);
if(_36){
_36.parentNode.removeChild(_36);
}
this._addOrRemoveCssForInternalApp(false,this.optimalBrowserMsgPaddingCSS);
}});
});
