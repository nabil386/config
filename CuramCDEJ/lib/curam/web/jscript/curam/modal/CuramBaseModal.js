//>>built
require({cache:{"url:curam/layout/resources/CuramBaseModal.html":"<div class=\"dijitDialog\" role=\"dialog\" aria-labelledby=\"${id}_title\" aria-live=\"assertive\">\n\t<div data-dojo-attach-point=\"titleBar\" class=\"dijitDialogTitleBar\">\n\t\t<span data-dojo-attach-point=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\"\n\t\t\t\trole=\"heading\" level=\"2\"></span>\n\t\t<button data-dojo-attach-point=\"closeButtonNode\" class=\"dijitDialogCloseIcon\"\n          data-dojo-attach-event=\"ondijitclick: onCancel\" title=\"${buttonCancel}\"\n          role=\"button\" tabindex=\"0\" aria-label=\"${closeModalText}\" \n          onKeyDown=\"require(['curam/ModalDialog'], function(md) {md.handleTitlebarIconKeydown(event)});\"\n          style=\"visibility: hidden;\">\n      \t\t<img src=\"${buttonCloseIcon}\" alt=\"${closeModalText}\" class=\"button-close-icon-default\"/>\n      \t\t<img src=\"${buttonCloseIconHover}\" alt=\"${closeModalText}\" class=\"button-close-icon-hover\"/>\n\t\t\t<span data-dojo-attach-point=\"closeText\" class=\"closeText\" title=\"${buttonCancel}\">${closeModalText}</span>\n\t    </button>\n\t</div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitDialogPaneContent\"></div>\n\t${!actionBarTemplate}\n</div>\n"}});
define("curam/modal/CuramBaseModal",["dojo/text!curam/layout/resources/CuramBaseModal.html","dojo/_base/declare","dojo/dom-geometry","dojo/Deferred","dojo/on","dojo/query","dojo/_base/lang","dojo/_base/fx","dojo/window","dojo/sniff","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/dom-class","dojo/dom-construct","dojo/aspect","dijit/Dialog","curam/inspection/Layer","curam/util/external","curam/dialog","curam/tab","curam/debug","curam/ModalUIMController","curam/widget/ProgressSpinner","curam/util/RuntimeContext"],function(_1,_2,_3,_4,on,_5,_6,fx,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,tab,_13,_14,_15){
var _16=_2("curam.modal.CuramBaseModal",null,{templateString:_1,autofocus:false,refocus:false,iframeHref:"",iframe:undefined,width:undefined,height:undefined,defaultWidth:800,closeModalText:LOCALISED_MODAL_CLOSE_BUTTON,buttonCloseIcon:MODAL_CLOSE_BUTTON_ICON,buttonCloseIconHover:MODAL_CLOSE_BUTTON_ICON_HOVER,modalPromptText:". "+LOCALISED_MODAL_SCREEN_READER_PROMPT+" .",maximumWidth:null,maximumHeight:null,_determinedWidth:null,_determinedHeight:null,_horizontalModalSpace:100,_verticalModalSpace:100,duration:5,parentWindow:undefined,isRegisteredForClosing:false,unsubscribes:undefined,modalconnects:undefined,onIframeLoadHandler:undefined,initialized:false,initDone:false,initUnsubToken:null,uimController:null,_helpIcon:null,_title:null,_isMobileUA:false,_isMobileUADialogPositioned:false,uimToken:undefined,postCreate:function(){
this.initModal(arguments);
},initModal:function(_17){
curam.debug.log("curam.modal.CuramBaseModal.postCreate(): w=%s; h=%s",this.width?this.width:"not given",this.height?this.height:"not given");
this._destroyOldModals();
var _18=curam.util.getTopmostWindow();
if(_18.curam.config){
this._isMobileUA=_18.curam.config.mobileUserAgent;
}
if(_18&&_18.jsScreenContext){
this._verticalModalSpace=100;
if(_18.jsScreenContext.hasContextBits("EXTAPP")){
this._verticalModalSpace=50;
curam.debug.log("curam.modal.CuramBaseModal.postCreate(): Detected external app, setting _verticalModalSpace to "+this._verticalModalSpace);
}
}
if(typeof (this._isMobileUA)!="boolean"){
this._isMobileUA=false;
}
this.draggable=!this._isMobileUA;
var _19=0.9;
this.maximumWidth=(dijit.getViewport().w*_19)-this._horizontalModalSpace;
this.maximumHeight=(dijit.getViewport().h*_19)-this._verticalModalSpace;
if(jsScreenContext.hasContextBits("EXTAPP")){
this.maximumHeight-=this._verticalModalSpace;
}
this.inherited(_17);
this.unsubscribes=[];
this.modalconnects=[];
this._isCDEJModal=(this.iframeHref.indexOf("CDEJ/popups")>-1||this.iframeHref.indexOf("frequency-editor.jsp")>-1);
if(jsScreenContext.hasContextBits("EXTAPP")){
_b.set(this.domNode,"top","1px");
_b.set(this.domNode,"display","");
_b.set(this.domNode,"opacity","0");
_c.add(this.domNode,"modalDialog");
}
this._initParentWindowRef();
if(this.parentWindow){
curam.dialog.pushOntoDialogHierarchy(this.parentWindow);
}else{
curam.dialog.pushOntoDialogHierarchy(curam.util.getTopmostWindow());
}
this.unsubscribes.push(this.subscribe("/dnd/move/start",dojo.hitch(this,this._startDrag)));
this.unsubscribes.push(this.subscribe("/dnd/move/stop",function(){
var ovr=dojo.query(".overlay-iframe")[0];
if(ovr){
_d.destroy(ovr);
}
}));
this._registerInitListener();
var _1a=dojo.subscribe("/curam/dialog/iframeUnloaded",this,function(_1b,_1c){
if(this.id==_1b){
curam.debug.log(_13.getProperty("curam.ModalDialog.unload"),_1b);
curam.dialog.removeFromDialogHierarchy(_1c);
this.initDone=false;
this._registerInitListener();
}
});
this.unsubscribes.push(_1a);
var _1d=dojo.hitch(this,function(_1e,_1f){
curam.debug.log(_13.getProperty("curam.ModalDialog.load.init"),_1e);
curam.util.onLoad.removeSubscriber(this._getEventIdentifier(),_1d);
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_1f);
if(!this.isRegisteredForClosing){
var _20=curam.util.getTopmostWindow();
this.unsubscribes.push(_20.dojo.subscribe("/curam/dialog/close",this,function(_21){
if(this.id==_21){
curam.debug.log("/curam/dialog/close "+_13.getProperty("curam.ModalDialog.event.for"),_21);
this.hide();
}
}));
this.isRegisteredForClosing=true;
}
this.doShow(_1f);
this._notifyModalDisplayed();
});
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),_1d);
if(this._isCDEJModal){
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),function(_22,_23){
var _24=_9.byId(_22);
if(_24){
_24.focus();
_24.contentWindow.focusFirst&&_24.contentWindow.focusFirst();
}
});
}
var _25=true;
this.onLoadSubsequentHandler=dojo.hitch(this,function(_26,_27){
if(_25){
_25=false;
}else{
curam.debug.log(_13.getProperty("curam.ModalDialog.load"),_26);
if(!_27.modalClosing){
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_27);
this._position(true);
this.doShow(_27);
this._notifyModalDisplayed();
}else{
curam.debug.log(_13.getProperty("curam.ModalDialog.close"));
}
}
var _28=_9.byId(_26);
var _29=_28.contentWindow.document.title;
_28.setAttribute("title",LOCALISED_MODAL_FRAME_TITLE+" - "+_29);
});
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/iframeFailedToLoad",this,function(_2a){
if(this.id==_2a){
curam.util.onLoad.removeSubscriber(this._getEventIdentifier(),_1d);
this._determineSize({height:450,title:"Error!"});
this.doShow();
this._notifyModalDisplayed();
}
}));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,this._setFocusHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,function(_2b){
if(_2b==this.id){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/AfterDisplay",[_2b]);
}
}));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,function(){
curam.util._setModalCurrentlyOpening(false);
}));
var _2c=function(_2d){
return _2d.indexOf(":")>0;
};
var _2e=_2c(this.iframeHref)?this.iframeHref:this._getBaseUrl(curam.util.getTopmostWindow().location.href)+jsL+"/"+this.iframeHref;
this.uimController=new _14({uid:this.id,url:_2e,loadFrameOnCreate:false,inDialog:true,iframeId:this._getEventIdentifier(),width:this._calculateWidth(this.width)+"px",height:this.maximumHeight+"px"});
_10.register("curam/modal/CuramBaseModal",this);
curam.debug.log("DEBUG: ModalDialog.js:postCreate(): uimController: "+this.uimController);
this.iframe=this.uimController.getIFrame();
curam.debug.log("DEBUG: ModalDialog.js:postCreate(): uimController.domNode: "+this.uimController.domNode);
this.modalconnects.push(dojo.connect(this,"onHide",this,this._onHideHandler));
this.set("content",this.uimController.domNode);
_c.add(this.iframe,this._getEventIdentifier());
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,this._modalDisplayedHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/closed",this,this._modalClosedHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/spinner",this._displaySpinner(this)));
this._registerOnIframeLoad(dojo.hitch(this,this._loadErrorHandler));
this.uimController.loadPage();
},_displaySpinner:function(_2f){
var a1=_e.before(this,"_loadErrorHandler",function(){
curam.util.getTopmostWindow().dojo.publish("curam/progress/unload");
a1.remove();
});
var a2=_e.after(_15,"dismissSpinner",function(){
a1&&a1.remove();
a2&&a2.remove();
});
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_2f.containerNode,100]);
},hide:function(){
if(!this._alreadyInitialized||!this.open){
return;
}
if(this._fadeInDeferred){
this._fadeInDeferred.cancel();
}
var _30=dojo.fadeOut({node:this.domNode,duration:this.duration,onEnd:dojo.hitch(this,function(){
this.domNode.style.display="none";
dijit.Dialog._DialogLevelManager.hide(this);
this._fadeOutDeferred.resolve(true);
delete this._fadeOutDeferred;
})});
this._fadeOutDeferred=new _4(dojo.hitch(this,function(){
_30.stop();
delete this._fadeOutDeferred;
}));
var _31=this._fadeOutDeferred.promise;
dojo.hitch(this,"onHide")();
_30.play();
if(this._scrollConnected){
this._scrollConnected=false;
}
var h;
while(h=this._modalconnects.pop()){
h.remove();
}
if(this._relativePosition){
delete this._relativePosition;
}
this._set("open",false);
return _31;
},_getBaseUrl:function(_32){
var _33=_32.indexOf("?");
_32=(_33>-1)?_32.substring(0,_33):_32;
var _34=_32.lastIndexOf("/");
return _32.substring(0,_34+1);
},_setupHelpIcon:function(_35){
var _36=typeof _35!="undefined"?_35.helpEnabled:false,_37=_36?_35.helpExtension:"",_38=_36?_35.pageID:"",_39=dojo.query(".modalDialog#"+this.id+" .dijitDialogCloseIcon");
for(var i=0;i<_39.length;i++){
if(_36&&!this._helpIcon){
this._helpIcon=this._createHelpIcon("dijitDialogHelpIcon","dijitDialogHelpIcon-hover",_37,_39[i]);
this._helpIcon.setAttribute("role","button");
this._helpIcon.setAttribute("style","visibility: hidden;");
this._setTabIndex(this._helpIcon,"0");
this.connect(this._helpIcon,"onkeydown",function(_3a){
this.handleTabbingBackwards(dojo.fixEvent(_3a));
});
this._helpIcon._enabled=false;
}
this._setTabIndex(_39[i],"0");
}
if(_36&&this._helpIcon){
this._helpIcon._pageID=_38;
}
if((_36&&this._helpIcon&&this._helpIcon._enabled)||(!_36||!this._helpIcon||!this._helpIcon._enabled)){
return;
}
_b.set(this._helpIcon,"display",_36?"":"none");
this._helpIcon._enabled=_36;
},_createHelpIcon:function(_3b,_3c,_3d,_3e){
var _3f=_d.create("span",{"class":_3b,"waiRole":"presentation","title":LOCALISED_MODAL_HELP_TITLE});
_d.place(_3f,_3e,"before");
this.connect(_3f,"onclick",function(){
var _40=curam.config?curam.config.locale:jsL;
var url;
url="./help.jsp?pageID="+this._helpIcon._pageID;
window.open(url);
});
this.connect(_3f,"onkeydown",function(){
if(curam.util.enterKeyPress(event)){
var _41=curam.config?curam.config.locale:jsL;
var url;
url="./help.jsp?pageID="+this._helpIcon._pageID;
window.open(url);
}
});
if(_3c){
this.connect(_3f,"onmouseover",function(){
_c.add(_3f,_3c);
});
this.connect(_3f,"onmouseout",function(){
_c.remove(_3f,_3c);
});
}
var _42=_d.create("img",{"src":MODAL_HELP_BUTTON_ICON,"alt":LOCALISED_MODAL_HELP_ALT,"class":"button-help-icon-default"});
var _43=_d.create("img",{"src":MODAL_HELP_BUTTON_ICON_HOVER,"alt":LOCALISED_MODAL_HELP_ALT,"class":"button-help-icon-hover"});
_d.place(_42,_3f);
_d.place(_43,_3f);
return _3f;
},_registerInitListener:function(){
if(!this.initUnsubToken){
this.initUnsubToken=dojo.subscribe("/curam/dialog/init",this,function(){
dojo.publish("/curam/dialog/SetId",[this.id]);
this.initDone=true;
if(this.uimToken){
dojo.publish("/curam/dialog/uim/opened/"+this.uimToken,[this.id]);
}
dojo.unsubscribe(this.initUnsubToken);
this.initUnsubToken=false;
});
}
},_getEventIdentifier:function(){
return "iframe-"+this.id;
},_registerOnIframeLoad:function(_44){
if(dojo.isIE&&dojo.isIE<9){
this.onIframeLoadHandler=dojo.hitch(this,function(){
if(typeof this.iframe!="undefined"&&typeof this.iframe.readyState!="undefined"&&this.iframe.readyState=="complete"){
_44();
}
});
this.iframe.attachEvent("onreadystatechange",this.onIframeLoadHandler);
}else{
this.modalconnects.push(dojo.connect(this.iframe,"onload",this,_44));
}
},_startDrag:function(_45){
if(!this.iframe){
return;
}
if(_45&&_45.node&&_45.node===this.domNode){
var _46=_d.create("div",{"class":"overlay-iframe"});
_46.innerHTML="";
_d.place(_46,this.iframe,"before");
var _47=_3.getContentBox(this.containerNode,_b.getComputedStyle(this.containerNode));
_b.set(_46,{width:_47.w+"px",height:_47.h+"px"});
var _48=_3.getMarginBoxSimple(dijit._underlay.domNode);
var _49={l:_48.w-_47.w-10,t:_48.h-_47.h-30};
this._moveable.onMove=function(_4a,_4b,e){
_4b.l=Math.max(5,Math.min(_4b.l,_49.l));
_4b.t=Math.max(5,Math.min(_4b.t,_49.t));
dojo.dnd.Moveable.prototype.onMove.apply(this,[_4a,_4b,e]);
};
}
},_loadErrorHandler:function(){
curam.debug.log(_13.getProperty("curam.ModalDialog.onload.notify")+this.iframe.id);
if(!this.initDone){
dojo.unsubscribe(this.initUnsubToken);
curam.debug.log(_13.getProperty("curam.ModalDialog.firing")+" /curam/dialog/iframeFailedToLoad "+_13.getProperty("curam.ModalDialog.for"),this.id);
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/iframeFailedToLoad",[this.id]);
}else{
curam.debug.log("UIM "+_13.getProperty("curam.ModalDialog.onload.success"));
}
if(this.iframe&&this.iframe.contentWindow&&this.iframe.contentWindow.document&&this.iframe.contentWindow.document.title){
curam.debug.log("curam.modal.CuramBaseModal._loadErrorHandler calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle(this.iframe.contentWindow.document.title);
}
},_setFocusHandler:function(_4c){
if(_4c==this.id&&this.initDone){
curam.debug.log("curam.modal.CuramBaseModal_setFocusHandler(): "+_13.getProperty("curam.ModalDialog.execute"),_4c);
var _4d=this.iframe.contentWindow;
var _4e=this;
var _4f;
if(_4d.document.getElementsByClassName("skeleton").length>0){
_4f=setTimeout(function(){
_4e._setFocusHandler(_4c);
},300);
}else{
_4d.dojo.publish("curam/modal/component/ready");
if(_4f){
clearTimeout(_4f);
}
if(typeof _4d.dijit=="object"&&typeof _4d.dijit.focus=="function"){
_4d.dijit.focus(this.iframe);
}else{
this.iframe.focus();
}
var _50;
dojo.withDoc(this.iframe.contentWindow.document,function(){
_50=dojo.byId("error-messages");
var _51=window.curam.util.getTopmostWindow();
if(_50&&_51.curam.__datePickerIds){
var _52=_51.curam.__datePickerIds;
for(var i=0;i<_52.length;i++){
var _53=_52[i];
var _54=dojo.byId(_53);
if(_54){
_54.nextElementSibling.focus();
_54.focus();
}
}
_51.curam.__datePickerIds=undefined;
}
});
var _55=sessionStorage.getItem("curamDefaultActionId");
var _56=null;
if(!_50&&_55){
sessionStorage.removeItem("curamDefaultActionId");
dojo.withDoc(this.iframe.contentWindow.document,function(){
if(dojo.byId(_55)){
_56=dojo.byId(_55).previousSibling;
}else{
_56=_4d.curam.util.doSetFocus();
}
});
}else{
_56=_4d.curam.util.doSetFocus();
if(!_56&&this.checkPrimaryButtonOnViewModal&&typeof this.checkPrimaryButtonOnViewModal==="function"){
_56=this.checkPrimaryButtonOnViewModal();
}
}
if(_56){
if(_8("trident")||window.navigator.userAgent.indexOf("Edg/")>-1||_8("ie")){
var _57=_56.ownerDocument.forms["mainForm"];
var _58=false;
if(sessionStorage.getItem("suppressCuramModalFocusTimeout")&&sessionStorage.getItem("suppressCuramModalFocusTimeout")==="true"){
_58=true;
}
if(_57&&(_56.tagName==="SELECT"||(_56.tagName==="INPUT"&&_a.get(_56,"type")==="text"))){
var _59=_d.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_d.place(_59,_57,"before");
_59.focus();
on(_59,"blur",function(){
_d.destroy(_59);
});
}
var _5a=function(ff){
return function(){
var _5b=ff.ownerDocument.activeElement;
if(_5b.tagName==="INPUT"&&!_5b.classList.contains("hidden-focus-input")||_5b.tagName==="TEXTAREA"||(_5b.tagName==="LABEL"&&_5b.className=="fileUploadButton")||(_5b.tagName==="A"&&_5b.className=="popup-action")||(_5b.tagName==="IFRAME"&&_5b.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
var _5c=_a.get(_56,"aria-label");
var _5d="";
var _5e=_a.get(_56,"objid");
if(_5e&&_5e.indexOf("component")==0||_c.contains(_56,"dijitReset dijitInputInner")){
_5d=_56.title;
}else{
_5d=LOCALISED_MODAL_FRAME_TITLE||"Modal Dialog";
}
if(_56&&_56.id!=="container-messages-ul"){
_a.set(_56,"aria-label",_5d);
}
var _5f=function(_60){
return function(e){
_5("input|select[aria-label="+_5d+"]").forEach(function(_61){
_60&&_a.set(_61,"aria-label",_60);
!_60&&_a.remove(_61,"aria-label");
});
};
};
on(_56,"blur",_5f(_5c));
if(!_58&&_56.tagName==="TEXTAREA"){
setTimeout(_5a(_56),1000);
}else{
if(!_58&&(_56.tagName==="SELECT"||(_56.tagName==="INPUT"&&_a.get(_56,"type")==="text"))){
setTimeout(_5a(_56),200);
}else{
_56.focus();
}
}
}else{
_56.focus();
}
if(sessionStorage.getItem("suppressCuramModalFocusTimeout")){
sessionStorage.removeItem("suppressCuramModalFocusTimeout");
}
}
}
}
},_modalDisplayedHandler:function(_62){
if(_62==this.id){
curam.debug.log(_13.getProperty("curam.ModalDialog.dialog.open.1")+"("+this.id+")"+_13.getProperty("curam.ModalDialog.dialog.open.2"));
this._markAsActiveDialog(true);
}else{
if(!this.deactivatedBy){
curam.debug.log(_13.getProperty("curam.ModalDialog.dialog.deactivating.1")+"("+this.id+"),"+_13.getProperty("curam.ModalDialog.dialog.deactivating.2"),_62);
this._markAsActiveDialog(false);
this.deactivatedBy=_62;
}
}
},_modalClosedHandler:function(_63){
if(this.deactivatedBy==_63){
curam.debug.log(_13.getProperty("curam.ModalDialog.dialog.activating.1")+"("+this.id+"),"+_13.getProperty("curam.ModalDialog.dialog.activating.2"),_63);
this._markAsActiveDialog(true);
delete this.deactivatedBy;
}
curam.debug.log("curam.modal.CuramBaseModal._modalClosedHandler calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
},_destroyOldModals:function(){
if(!curam.dialog.oldModalsToDestroy){
curam.dialog.oldModalsToDestroy=[];
}
dojo.forEach(curam.dialog.oldModalsToDestroy,function(_64){
_64._cleanupIframe();
_64.destroyRecursive();
});
curam.dialog.oldModalsToDestroy=[];
},_initParentWindowRef:function(){
if(!this.parentWindow){
var _65=null;
if(curam.tab.inTabbedUI()){
_65=curam.tab.getContentPanelIframe();
}else{
if(_11.inExternalApp()){
_65=_11.getUimParentWindow();
}
}
if(_65){
this.parentWindow=_65.contentWindow;
}
}else{
if(_c.contains(this.parentWindow.frameElement,"detailsPanelFrame")){
var _66=curam.tab.getContentPanelIframe();
var _67=curam.util.getLastPathSegmentWithQueryString(_66.src);
_67=curam.util.removeUrlParam(_67,"__o3rpu");
curam.debug.log("o3rpu "+_13.getProperty("curam.ModalDialog.property"),encodeURIComponent(_67));
this.iframeHref=curam.util.replaceUrlParam(this.iframeHref,"__o3rpu",encodeURIComponent(_67));
this.parentWindow=_66.contentWindow;
}
}
},_notifyModalDisplayed:function(){
curam.debug.log(_13.getProperty("curam.ModalDialog.publishing")+" /curam/dialog/displayed "+_13.getProperty("curam.ModalDialog.for"),this.id);
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/displayed",[this.id,{width:this._determinedWidth,height:this._determinedHeight}]);
},_markAsActiveDialog:function(_68){
var _69="curam-active-modal";
if(_68){
_c.add(this.iframe,_69);
curam.debug.log(_13.getProperty("curam.ModalDialog.add.class"),[this.id,this.iframeHref]);
}else{
_c.remove(this.iframe,_69);
curam.debug.log(_13.getProperty("curam.ModalDialog.remove.class"),[this.id,this.iframe.src]);
}
},_setHrefAttr:function(_6a){
curam.debug.log("setHrefAttr");
this.iframeHref=_6a;
this.inherited(arguments);
},_setTabIndex:function(_6b,_6c){
_6b.setAttribute("tabIndex",_6c);
},_position:function(_6d){
curam.debug.log(_13.getProperty("curam.ModalDialog.position"));
if(this._isMobileUADialogPositioned==false&&(this.open||_6d)){
this.inherited(arguments);
if(this._isMobileUA==true){
this._isMobileUADialogPositioned=true;
}
}else{
curam.debug.log(_13.getProperty("curam.ModalDialog.ignoring")+" curam.ModalDialog_position");
}
},_getUnits:function(){
return "px";
},_calculateWidth:function(_6e){
if(_6e){
_6e=new Number(_6e);
if(!this._isCDEJModal&&typeof (G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR)!="undefined"){
_6e*=G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR;
}
if(_6e>this.maximumWidth){
curam.debug.log(_13.getProperty("curam.ModalDialog.specified.width.over"),this.maximumWidth);
return this.maximumWidth;
}else{
return Math.floor(_6e);
}
}else{
var _6f=this.defaultWidth;
if(!this._isCDEJModal&&typeof (G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR)!="undefined"){
_6f*=G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR;
}
curam.debug.log(_13.getProperty("curam.ModalDialog.default.width"),_6f);
if(_6f>this.maximumWidth){
curam.debug.log(_13.getProperty("curam.ModalDialog.default.width.over"),this.maximumWidth);
return this.maximumWidth;
}else{
return Math.floor(_6f);
}
}
},_calculateHeight:function(_70,_71){
if(_70){
_70=isNaN(_70)?new Number(_70):_70;
if(_70>this.maximumHeight){
curam.debug.log("specified height exceeds available space, "+"overriding with max available height of ",this.maximumHeight);
return this.maximumHeight;
}else{
if(_70<this.modalMinimumHeight){
curam.debug.log(_13.getProperty("curam.ModalDialog.specified.height.over.1"),this.modalMinimumHeight);
return this.modalMinimumHeight;
}else{
return _70;
}
}
}else{
curam.debug.log(_13.getProperty("curam.ModalDialog.no.height"),_71);
if(_71>this.maximumHeight){
curam.debug.log(_13.getProperty("curam.ModalDialog.calculated.height.over.1"),this.maximumHeight);
return this.maximumHeight;
}else{
if(_71<this.modalMinimumHeight){
curam.debug.log(_13.getProperty("curam.ModalDialog.calculated.height.over.2"),this.modalMinimumHeight);
return this.modalMinimumHeight;
}else{
return _71;
}
}
}
},_determineSize:function(_72){
var _73=_72.height;
var _74=_72.windowOptions;
curam.debug.log(_13.getProperty("curam.ModalDialog.size"));
try{
var w=this._calculateWidth(this.width);
var h=this._calculateHeight(this.height,_73);
if(_74){
if(_74["width"]||_74["height"]){
curam.debug.log(_13.getProperty("curam.ModalDialog.options"));
w=this._calculateWidth(_74["width"]);
h=this._calculateHeight(_74["height"],_73);
}
}
w=w+this._getUnits();
h=h+this._getUnits();
curam.debug.log("curam.ModalDialog:_determineSize() %s x %s",w,h);
if(jsScreenContext.hasContextBits("EXTAPP")){
this.uimController.setDimensionsForModalDialog(w,h,_72);
}
this._determinedWidth=w;
this._determinedHeight=h;
this.setTitle(_72,w);
}
catch(e){
curam.debug.log("curam.ModalDialog:_determineSize() : "+_13.getProperty("curam.ModalDialog.error")+dojo.toJson(e));
}
},setTitle:function(_75,_76){
var _77=_75.title;
if(!_77){
curam.debug.log("curam.ModalDialog.setTitle() - "+_13.getProperty("curam.ModalDialog.no.title"));
_77="";
}
var _78=_75.messageTitleAppend;
curam.debug.log("curam.ModalDialog.setTitle('%s')",_77);
var _79=_77.indexOf(_78);
if(_79!=-1){
var _7a=_d.create("span",{innerHTML:_78,"class":"messagesPresent"});
_77=_77.split(_78).join("<span class=\"messagesPresent\" aria-owns=\"error-messages-container-wrapper\" role=\"alert\"></span>");
}
this.titleNode.innerHTML=_77;
_b.set(this.titleBar,{width:_76+"px",height:21+"px"});
_b.set(this.titleNode,"width",Math.ceil(_76*0.85)+"px");
},doShow:function(_7b){
curam.debug.log("curam.ModalDialog.doShow(): "+_13.getProperty("curam.ModalDialog.show"));
if(!this.initialized){
this.initialized=true;
}
this._setupHelpIcon(_7b);
this.show();
this.dismissModalSpinner();
if(jsScreenContext.hasContextBits("EXTAPP")){
var _7c=dojo.query(".modalDialog#"+this.id+" .dijitDialogCloseIcon");
_b.set(_7c[0],"visibility","visible");
_b.set(_9.byId("end-"+this.id),"visibility","visible");
_b.set(this.iframe,"visibility","visible");
_b.set(this.domNode,"visibility","visible");
if(this._helpIcon){
_b.set(this._helpIcon,"visibility","visible");
}
}
},dismissModalSpinner:function(){
dojo.publish("/curam/progress/unload");
},_onHideHandler:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/BeforeClose",[this.id]);
_b.set(this.domNode,{visibility:"hidden",display:"block"});
curam.dialog.removeFromDialogHierarchy(this.iframe.contentWindow);
curam.dialog.removeFromDialogHierarchy(this.parentWindow);
var _7d=curam.util.getTopmostWindow();
_7d.dojo.publish("/curam/dialog/closed",[this.id]);
dojo.unsubscribe(this.initUnsubToken);
dojo.forEach(this.unsubscribes,_7d.dojo.unsubscribe);
this.unsubscribes=[];
dojo.forEach(this.modalconnects,dojo.disconnect);
this.modalconnects=[];
if(dojo.isIE&&dojo.isIE<9){
this.iframe.detachEvent("onreadystatechange",this.onIframeLoadHandler);
}
curam.util.onLoad.removeSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
if(this._explodeNode&&this._explodeNode.parentNode){
this._explodeNode.parentNode.removeChild(this._explodeNode);
}
curam.debug.log(_13.getProperty("curam.ModalDialog.deactivating",[this.id]));
_7d.dojo.publish("/curam/dialog/close/appExitConfirmation",[this.id]);
this._markAsActiveDialog(false);
if(typeof this.parentWindow!="undefined"&&this.parentWindow!=null){
this.parentWindow.focus();
}
delete this.parentWindow;
_a.set(this.iframe,"src","");
sessionStorage.removeItem("firstPageHitAfterModalOpened");
curam.dialog.oldModalsToDestroy.push(this);
},handleTabbingForwards:function(e,_7e){
if(!tabbingBackwards){
if(_7e){
setTimeout(function(){
_7e.focus();
},1);
}
}
tabbingBackwards=null;
},handleTabbingBackwards:function(e,_7f,_80){
if(e.shiftKey&&e.keyCode==9){
var _81,evt=e?e:window.event;
if(evt.srcElement){
_81=evt.srcElement;
}else{
if(evt.target){
_81=evt.target;
}else{
throw new Error("handleTabbingBackwards(): No target element found.");
}
}
var _82=_7f&&(_81.previousElementSibling.id==_7f)&&this.helpIconEnabled;
if(_81.previousSibling.className=="dijitDialogHelpIcon"||_82){
return false;
}else{
var _83=_80?_80:_81.parentElement.parentElement.id;
var _84=document.getElementById(_83);
if(_84){
tabbingBackwards=true;
_84.focus();
}
}
}
},_cleanupIframe:function(){
delete this.content;
delete this.uimController;
var _85=this.iframe;
_85.src="";
delete this.iframe;
_d.destroy(_85);
}});
return _16;
});
