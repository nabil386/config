//>>built
define("curam/util/AutoRecoveryAPI",["dojo/request/xhr","curam/define","dojo/_base/declare","curam/debug"],function(_1,_2,_3,_4){
var _5=_3("curam.util.AutoRecoveryAPI",null,{_pageID:"",_pageParams:"",_iFrameID:"",_unsubscribes:[],_unsubscribesFormEvent:[],_subscribeCloseModal:null,_throttle:[],constructor:function(){
this._unset();
this._unsubscribes.push(dojo.subscribe("curam/tab/restoreModal",this,function(){
this._getAutoRecoveryService();
}));
if(!this._subscribeCloseModal){
dojo.subscribe("/curam/dialog/BeforeClose",this._beforeCloseHandler.bind(this));
}
this._throttle={enabled:AUTORECOVERY_THROTTLE_INTERVAL>0,gateOpen:true,queuedPost:[]};
this.tabSessionManager=new curam.tab.TabSessionManager();
},initialize:function(_6){
var _7=_6.frameElement.id.replace("iframe-","");
if(!this._iFrameID){
this._iFrameID=_7;
}
var _8=this._iFrameID===_7;
var _9=_6.location.href.includes("Action.do");
var _a=_8&&!_9;
if(_a){
this._uninstall(this._unsubscribesFormEvent);
var _b=new curam.ui.PageRequest(_6.location.href);
this._pageParams=_b.parameters;
this._pageID=_b.pageID;
var _c=this.tabSessionManager._getPrevSelectedTab();
this._updateTabContent(_c);
if(!_c.restoreModalInd){
this._createAutorecoveryHandler(_c);
}else{
this._getAutoRecoveryService(_6);
}
var _d=this._registerFormChangeHandler.bind(this);
this._unsubscribesFormEvent.push(dojo.subscribe("curam/util/CuramFormsAPI/formChange",_d));
}
},_beforeCloseHandler:function(_e){
if(this._iFrameID===_e){
this._deleteAutoRecoveryService();
this._unset();
this._unsetSelectedTD();
}
},_unsetSelectedTD:function(){
var _f=this.tabSessionManager._getPrevSelectedTab();
_f.foregroundTabContent=null;
_f.restoreModalInd=false;
curam.debug.log("curam.modal.CuramCarbonModal: "+_4.getProperty("curam.modal.CuramCarbonModal.foreground.removed"),_f);
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_f.toJson();
},_createAutorecoveryHandler:function(_10){
var _11={pageID:this._pageID,params:this._pageParams};
var _12="";
var _13=false;
this._postAutoRecoveryService(_11,_12,_13,_10);
},_updateTabContent:function(_14){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.foreground.added"),_14);
if(!_14.foregroundTabContent){
this._updateForegroundTabContent(_14);
}else{
if(_14.foregroundTabContent.pageID!==this._pageID){
_14.restoreModalInd=false;
this._updateForegroundTabContent(_14);
}
}
},_updateForegroundTabContent:function(_15){
var _16={"pageID":this._pageID,"pageParams":this._pageParams};
_15.foregroundTabContent=_16;
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_15.toJson();
},_unset:function(){
this._uninstall(this._unsubscribes,this._unsubscribesFormEvent);
this._pageID="";
this._pageParams="";
this._iFrameID="";
this._unsubscribes=[];
this._unsubscribesFormEvent=[];
},_uninstall:function(_17,_18){
_17&&_17.forEach(function(hh){
hh.remove();
});
_18&&_18.forEach(function(hh){
hh.remove();
});
},_registerFormChangeHandler:function(_19){
if(this._iFrameID===_19.frameID){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.register.form.change.handler"));
var _1a=_19.data;
var _1b={pageID:this._pageID,params:this._pageParams};
var _1c=true;
this._postAutoRecoveryService(_1b,_1a,_1c);
}
},_getSelectedTD:function(_1d,_1e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.previous.modal.background"));
var _1f="";
if(_1d){
_1f=this.tabSessionManager._getPrevSelectedModal();
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.previous.modal.background.set"));
_1f=!_1e?this.tabSessionManager._getPrevSelectedTab():_1e;
_1f.restoreModalInd=true;
localStorage[curam.tab.TabSessionManager.PREVIOUSLY_SELECTED_MODAL_KEY]=_1f.toJson();
}
return _1f;
},_postAutoRecoveryService:function(_20,_21,_22){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle"));
if(this._throttle.enabled===false){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.disabled"));
this._postAutoRecoveryServiceUnthrottled(_20,_21,this._getSelectedTD(_22));
return;
}
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.gateStatus")+this._throttle.gateOpen);
var _23=this._throttle.gateOpen;
this._throttle.gateOpen=false;
if(arguments.length>0){
this._throttle.queuedPost={foregroundPage:_20,foregroundPageData:_21,backgroundSelectedTD:this._getSelectedTD(_22)};
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.storeRequest")+", foregroundPage: "+this._throttle.queuedPost.foregroundPage.pageID+", foregroundPageData: "+this._throttle.queuedPost.foregroundPageData+", backgroundSelectedTD: "+this._throttle.queuedPost.backgroundSelectedTD.tabID);
}
if(_23===true){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.post")+", foregroundPage: "+this._throttle.queuedPost.foregroundPage.pageID+", foregroundPageData: "+this._throttle.queuedPost.foregroundPageData+", backgroundSelectedTD: "+this._throttle.queuedPost.backgroundSelectedTD.tabID);
var _20=this._throttle.queuedPost.foregroundPage;
var _21=this._throttle.queuedPost.foregroundPageData;
var _24=this._throttle.queuedPost.backgroundSelectedTD;
this._throttle.queuedPost=[];
this._postAutoRecoveryServiceUnthrottled(_20,_21,_24);
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.setTimer")+AUTORECOVERY_THROTTLE_INTERVAL);
this._throttle.timeoutID=setTimeout(function(){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.timerElapsed"));
this._throttle.gateOpen=true;
if(Object.keys(this._throttle.queuedPost).length!==0){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.throttle.processQueuedRequest"));
this._postAutoRecoveryService();
}
}.bind(this),AUTORECOVERY_THROTTLE_INTERVAL);
}
},_postAutoRecoveryServiceUnthrottled:function(_25,_26,_27){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post"));
var _28="servlet/autorecovery?command=save";
_1.post(_28,{data:{foregroundPage:JSON.stringify(_25),foregroundPageData:_26,backgroundTD:JSON.stringify(_27),requestSentTimeStamp:Date.now()}}).then(function(_29){
try{
if(_29!="OK"){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.error"));
return;
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.success"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.catch.error")+" '"+e+"'.");
}
},function(_2a){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.post.function.error")+" '"+_2a+"'.");
});
},_deleteAutoRecoveryService:function(){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.delete"));
clearTimeout(this._throttle.timeoutID);
var _2b="servlet/autorecovery?command=delete";
_1.post(_2b,{}).then(function(_2c){
try{
if(_2c!="OK"){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.delete.error"));
return;
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.delete.complete"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.delete.catch.error")+" '"+e+"'.");
}
},function(_2d){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.delete.function.error")+" '"+_2d+"'.");
});
},_loadModal:function(_2e,_2f){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI._loadModal"));
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_2f;
var _30=_2e.pageID;
var _31=_2e.params;
curam.util.UimDialog.open(_30+"Page.do",_31);
},_getAutoRecoveryService:function(_32){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get"));
var _33=this;
var _34="servlet/autorecovery?command=load";
_1.post(_34,{}).then(function(_35){
try{
if(_35&&_35.length>0){
var _36=JSON.parse(_35);
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get.success"));
var _37=JSON.parse(_36.foregroundPage);
if(!_32){
var _38=JSON.parse(_36.backgroundTD);
_33._loadModal(_37,JSON.stringify(_38));
}else{
var _39="Curam_"+_37.pageID;
if(_32.document.getElementById(_39)){
if(_36.foregroundPageData){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get.setFormFields"));
var _3a=JSON.parse(_36.foregroundPageData);
_32.curam.util.ui.form.CuramFormsAPI.setFormFields(_37,_3a);
}
}else{
_33._createAutorecoveryHandler();
}
}
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get.no.record"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get.catch.error")+" '"+e+"'.");
}
},function(_3b){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_4.getProperty("curam.util.AutoRecoveryAPI.get.function.error")+" '"+_3b+"'.");
});
}});
return _5;
});
