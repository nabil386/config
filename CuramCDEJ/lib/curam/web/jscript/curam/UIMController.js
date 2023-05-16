//>>built
require({cache:{"url:curam/layout/resources/UIMController.html":"<div id=\"uimcontroller_${uid}\" class=\"uimcontroller_${uid} uimController ${classList}\" data-dojo-attach-point=\"uimController\">\n  <div style=\"display:none;\" \n       id=\"uimcontroller_tc_${uid}\" \n       class=\"ipnTabController in-page-nav-tabContainer\"\n       data-dojo-attach-point=\"tabController\" \n       data-dojo-type=\"curam/layout/TabContainer\">\n  </div>\n  <div class=\"contentPanelFrameWrapper\"  \n        data-dojo-attach-point=\"frameWrapper\">\n    <iframe frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\"\n             allowTransparency=\"true\" \n             id=\"${iframeId}\" \n             data-dojo-attach-point=\"frame\"                 \n             class=\"${iframeId} ${iframeClassList}\"\n             iscpiframe=\"${iscpiframe}\"\n             title=\"${title}\" >\n    </iframe>\n  </div> \n</div>"}});
define("curam/UIMController",["dojo/text!curam/layout/resources/UIMController.html","dojo/_base/declare","dijit/_Widget","dijit/_Templated","dijit/layout/ContentPane","curam/inspection/Layer","curam/tab","curam/debug","dojo/dom-style","dojo/dom-attr","curam/util","curam/util/onLoad"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a){
var _b=_2("curam.UIMController",[dijit._Widget,dijit._Templated],{TAB_HEIGHT:20,EVENT:{TOPIC_PREFIX:"UIMController.InPageNav_"},TOPIC_LOADED:"/curam/uim/controller/loaded",frameLoadEvent:"",uid:"",url:"",tabControllerId:"",oldTabsTitlesList:[],newTabsTitlesList:[],widgetsInTemplate:true,finishedLoadingTabs:false,classList:"",iframeId:"",height:"",width:"",iframeClassList:"",iscpiframe:"false",ipnTabClickEvent:null,title:"",handleIPNTabClickListener:null,inPageNavItems:null,loadFrameOnCreate:true,resizeFrameOnLoad:false,templateString:_1,inDialog:false,constructor:function(_c){
if(!_c.uid){
throw "'uid' attribute not provided to constructor for"+" curam.UIMController(url,uid)";
}
this.uid="uimcontroller_"+_c.uid;
this.tabControllerId="uimcontroller_tc_"+_c.uid;
this.newTabsTitlesList=[];
this.ipnTabClickEvent=this.tabControllerId+"-selectChild";
if(this.height==""){
this.height="99%";
}
if(this.width==""){
this.width="99%";
}
curam.debug.log(_8.getProperty("curam.UIMController.new")+" curam.UIMController()...");
curam.debug.log("curam.UIMController "+_8.getProperty("curam.UIMController.identifier")+" "+this.uid);
curam.debug.log("curam.UIMController "+_8.getProperty("curam.UIMController.url")+" "+this.url);
curam.debug.log("curam.UIMController "+_8.getProperty("curam.UIMController.identifier")+" "+this.tabControllerId);
curam.debug.log("curam.UIMController: newTabsTitlesList "+" "+this.newTabsTitlesList);
_6.register("curam/UIMController",this);
return this.uimController;
},postCreate:function(){
if(jsScreenContext.hasContextBits("EXTAPP")){
_a.set(this.domNode,"style",{"height":this.height,"width":this.width});
}
curam.debug.log("UIMController Height: "+_9.get(this.domNode,"height"));
curam.debug.log("UIMController Width: "+_9.get(this.domNode,"width"));
this.frameLoadEvent=this.EVENT.TOPIC_PREFIX+this.frame.id;
this.setURL(this.url);
var _d=dojo.hitch(this,"processFrameLoadEvent");
curam.util.onLoad.addSubscriber(this.frame.id,_d);
dojo.connect(this,"destroy",function(){
curam.util.onLoad.removeSubscriber(this.iframeId,_d);
this.fLoadFunct=null;
});
if(jsScreenContext.hasContextBits("EXTAPP")){
if(this.inDialog){
_9.set(this.frame,{width:this.width,height:this.height});
}
}
curam.debug.log("curam.UIMController: loadFrameOnCreate="+this.loadFrameOnCreate);
curam.debug.log("curam.UIMController "+_8.getProperty("curam.UIMController.url")+this.url);
if(this.loadFrameOnCreate==true&&typeof (this.url)!="undefined"){
curam.debug.log("curam.UIMController: "+_8.getProperty("uram.UIMController.loading"));
this.loadPage();
}
},setURL:function(_e){
if(_e.indexOf("Page.do")==-1){
this.absoluteURL=true;
this.url=_e;
}else{
this.absoluteURL=false;
this.url=this._trimURL(_e);
}
},processFrameLoadEvent:function(_f,_10){
curam.debug.log("curam.UIMController: processFrameLoadEvent "+_8.getProperty("curam.UIMController.processing.IPN")+_10);
this.inPageNavItems=_10.inPageNavItems;
curam.debug.log("curam.UIMController: processFrameLoadEvent: "+_8.getProperty("curam.UIMController.processing"));
curam.debug.log("curam.UIMController.processFrameLoadEvent: this.tabController: "+this.tabController);
if(this.resizeFrameOnLoad==true){
var _11=_10.height;
curam.debug.log(_8.getProperty("curam.UIMController.resizing")+_11);
if(_11){
_9.set(this.getIFrame(),{height:_11+"px"});
}
}
curam.debug.log(_8.getProperty("curam.UIMController.IPN.items")+JSON.stringify(this.inPageNavItems));
if(!this.hasInPageNavigation()){
curam.debug.log(_8.getProperty("curam.UIMController.no.IPN"));
this.clearIPNTabs();
if(!this._tabControllerHidden()){
curam.debug.log(_8.getProperty("curam.UIMController.hiding"));
this.showTabContainer(false);
}
dojo.publish(this.TOPIC_LOADED);
if(this.inDialog===false){
curam.debug.log("curam.UIMController.processFrameLoadEvent calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
}
return;
}
curam.debug.log(_8.getProperty("curam.UIMController.extract"));
var _12=-1;
for(var i=0;i<this.inPageNavItems.length;i++){
this.newTabsTitlesList.push(this.inPageNavItems[i].title);
if(this.inPageNavItems[i].selected==true){
_12=i;
}
curam.debug.log(_8.getProperty("curam.UIMController.IPN.")+"["+this.inPageNavItems[i].title+", "+this.inPageNavItems[i].href+", "+this.inPageNavItems[i].selected+"]");
}
var _13=!(this.compareLists(this.oldTabsTitlesList,this.newTabsTitlesList));
if(_13){
this.clearIPNTabs(this);
this.createIPNTabs(this.inPageNavItems);
if(this._tabControllerHidden()){
this.showTabContainer(true);
}
}else{
curam.debug.log(_8.getProperty("curam.UIMController.no.change"));
if(_12>-1){
var _14=this.tabController.getIndexOfChild(this.tabController.selectedChildWidget);
if(_14!=_12){
curam.debug.log(_8.getProperty("curam.UIMController.change")+_14+_8.getProperty("curam.UIMController.to")+_12);
this.toggleIPNTabClickEventListener("off");
this.tabController.selectChild(this.tabController.getChildren()[_12]);
this.toggleIPNTabClickEventListener("on");
}
}
}
this.newTabsTitlesList=[];
curam.debug.log(_8.getProperty("curam.UIMController.clear")+this.newTabsTitlesList);
this.finishedLoadingTabs=true;
dojo.publish(this.TOPIC_LOADED);
dojo.publish("/curam/tab/labelUpdated");
},_tabControllerHidden:function(){
return _9.get(this.tabController.domNode,"display")=="none";
},toggleIPNTabClickEventListener:function(_15){
if(_15=="off"){
if(this.handleIPNTabClickListener!=null){
curam.debug.log(_8.getProperty("curam.UIMController.off.listener"));
dojo.unsubscribe(this.handleIPNTabClickListener);
}
}else{
curam.debug.log(_8.getProperty("curam.UIMController.on.listener"));
this.handleIPNTabClickListener=this.subscribe(this.ipnTabClickEvent,dojo.hitch(this,this.handleIPNTabClick));
}
},handleIPNTabClick:function(tab){
if(this.finishedLoadingTabs){
curam.debug.log(_8.getProperty("curam.UIMController.finishing"));
this.finishedLoadingTabs=false;
this.setURL(this._getURLByTitle(tab.title));
this.loadPage();
}
},createIPNTabs:function(_16){
this.toggleIPNTabClickEventListener("off");
if(!this.tabController){
console.error("curam.UIMController.createIPNTabs: "+_8.getProperty("uram.UIMController.no.widget")+" '"+this.tabControllerId+"'");
}else{
curam.debug.log("curam.UIMController.createIPNTabs: "+_8.getProperty("curam.UIMController.creating.tabs")+_16);
var _17=null;
for(var i=0;i<_16.length;i++){
var cp=new dijit.layout.ContentPane({title:_16[i].title});
this.tabController.addChild(cp);
if(_16[i].selected==true||_17==null){
_17=cp;
}
this.oldTabsTitlesList.push(_16[i].title);
curam.debug.log("curam.UIMController.createIPNTabs: "+_8.getProperty("curam.UIMController.adding.tabs")+_16[i].title);
}
this.tabController.startup();
this.tabController.selectChild(_17);
}
this.toggleIPNTabClickEventListener("on");
this.newTabsTitlesList=[];
},clearIPNTabs:function(){
var _18=false;
curam.debug.log("curam.UIMController.clearIPNTabs: "+_8.getProperty("curam.UIMController.clearing.tabs")+this.oldTabsTitlesList);
this.toggleIPNTabClickEventListener("off");
try{
this.tabController.destroyDescendants();
}
catch(e){
curam.debug.log("curam.UIMController.clearIPNTabs - this.tabController.destroyDescendants(): "+_8.getProperty("curam.UIMController.clear.warning"));
_18=true;
}
this.tabController.selectedChildWidget=null;
this.oldTabsTitlesList=[];
this.toggleIPNTabClickEventListener("on");
if(!_18){
curam.debug.log("curam.UIMController.createIPNTabs: "+_8.getProperty("curam.UIMController.clearing.notify")+this.oldTabsTitlesList);
}
},compareLists:function(_19,_1a){
curam.debug.log("curam.UIMController.compareLists: "+_8.getProperty("curam.UIMController.comparing.tabs"));
curam.debug.log(_8.getProperty("curam.UIMController.tab.list1")+_19);
curam.debug.log(_8.getProperty("curam.UIMController.tab.list1")+_1a);
var _1b=true;
if(_19.length!=_1a.length){
_1b=false;
}
for(var i=0;i<_19.length;i++){
if(_19[i]!=_1a[i]){
_1b=false;
}
}
curam.debug.log(_8.getProperty("curam.UIMController.result")+_1b);
return _1b;
},_getURLByTitle:function(_1c){
var url=null;
dojo.forEach(this.inPageNavItems,function(_1d){
if(_1d.title==_1c){
url=_1d.href;
}
});
curam.debug.log(url);
return url;
},_trimURL:function(_1e){
var idx=_1e.lastIndexOf("/");
if(idx>-1&&idx<=_1e.length){
return _1e.substring(idx+1);
}else{
return _1e;
}
},hasInPageNavigation:function(){
return this.inPageNavItems!=null;
},getIFrame:function(){
return this.frame;
},loadPage:function(_1f){
if(typeof (this.url)=="undefined"||this.url==null){
var e=new Error("curam.UIMController: Cannot load page as URL has not been set");
if(_1f){
_1f.errback(e);
}
throw e;
}
if(_1f){
var st=curam.util.subscribe(this.TOPIC_LOADED,function(){
curam.util.unsubscribe(st);
_1f.callback();
});
}
var _20=this._getFullURL();
curam.debug.log("curam.UIMController.loadPage(): "+_8.getProperty("curam.UIMController.set.source")+this.frame.id+" to url: "+_20);
_a.set(this.frame,"src",_20);
},_getFullURL:function(){
if(typeof (this.absoluteURL)!="undefined"&&this.absoluteURL==true){
return this.url;
}
var _21;
if(this.url.indexOf("?")==-1){
_21="?";
}else{
_21="&";
}
var _22=curam.config?curam.config.locale:jsL;
var _23="";
if(window==curam.util.getTopmostWindow()){
_23=_22+"/";
}
if(this.url.indexOf("o3nocache=")==-1){
return _23+this.url+_21+curam.util.getCacheBusterParameter();
}else{
return _23+this.url;
}
},showTabContainer:function(_24){
if(_24&&!this.hasInPageNavigation()){
curam.debug.log(_8.getProperty("curam.UIMController.ignore.reuest"));
return;
}
_9.set(this.frameWrapper,"top",(_24?this.TAB_HEIGHT+7:"0")+"px");
_9.set(this.tabController.domNode,"display",_24?"block":"none");
if(this.hasInPageNavigation()){
_9.set(this.tabController.domNode.parentNode,"overflow","visible");
}
if(_24){
this.tabController.resize();
}
},setDimensionsForModalDialog:function(w,h,_25){
curam.debug.log("curam.UIMController:setDimensionsForModalDialog() - "+"w="+w+", h="+h);
_9.set(this.frame,{width:w,height:h});
_9.set(this.tabController.domNode,{width:w});
if(typeof (_25.inPageNavItems)!="undefined"){
h+=this.TAB_HEIGHT+5;
curam.debug.log("cura.UIMController:setDimensionsForModalDialog() - "+_8.getProperty("curam.UIMController.height"));
}
_9.set(this.domNode,{width:w,height:h});
},destroy:function(){
this.iframe=null;
this.inPageNavItems=null;
dojo.unsubscribe(this.handleIPNTabClickListener);
this.tabController.destroy();
this.inherited(arguments);
}});
return _b;
});
