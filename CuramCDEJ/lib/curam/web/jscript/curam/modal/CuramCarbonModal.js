//>>built
require({cache:{"url:curam/layout/resources/CuramCarbonModal.html":"<div class=\"spm-component\">  \n   <div id=\"${id}_modal-root\" data-testid=\"${id}_modal-root\" data-dojo-attach-point=\"carbonModalNode\" class=\"bx--modal\" role=\"presentation\">\n        <div data-dojo-attach-point=\"modalContainer\" aria-modal=\"true\" aria-labelledby=\"${id}_modal-heading\" aria-live=\"assertive\" class=\"bx--modal-container\" data-testid=\"${id}_modal-container\" id=\"${id}_modal-container\" role=\"dialog\" tabindex=\"-1\">\n          <div class=\"bx--modal-header bx--modal-header--with-pair-icons\">\n            <h3 class=\"bx--modal-header__heading bx--type-beta\" data-dojo-attach-point=\"titleNode\" id=\"${id}_modal-heading\" data-testid=\"${id}_modal-heading\">\n              ${_title}\n            </h3>\n            <button data-dojo-attach-point=\"helpIcon\" aria-label=\"${helpTitle}\" class=\"bx--modal-help\" data-dojo-attach-event=\"onclick:displayHelp, onkeypress:displayHelp,onkeydown:escapeOnClose,onkeydown:preventBackwardsTabEscFromModal\" data-testid=\"button_modal.help.button.icon\" id=\"${id}_modal-help\" type=\"button\">\n              <svg focusable=\"false\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 32 32\">\n               <defs><style>.cls-1{fill:none;}</style></defs>\n               <title>${helpTitle}</title>\n               <path d=\"M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,26A12,12,0,1,1,28,16,12,12,0,0,1,16,28Z\"/>\n               <circle cx=\"16\" cy=\"23.5\" r=\"1.5\"/>\n               <path d=\"M17,8H15.5A4.49,4.49,0,0,0,11,12.5V13h2v-.5A2.5,2.5,0,0,1,15.5,10H17a2.5,2.5,0,0,1,0,5H15v4.5h2V17a4.5,4.5,0,0,0,0-9Z\"/>\n               <rect class=\"cls-1\" width=\"32\" height=\"32\"/>\n             </svg>\n            </button> \n            <button data-dojo-attach-point=\"closeButton\" aria-label=\"${closeTitle}\" class=\"bx--modal-close\" data-dojo-attach-event=\"onclick:hideAndClose,onkeydown:escapeOnClose,onkeydown:preventBackwardsTabEscFromModal\" data-testid=\"button_modal.close.button.icon\" id=\"${id}_modal-button-close\" type=\"button\">\n              <svg aria-hidden=\"true\" class=\"bx--modal-close__icon\" focusable=\"false\" preserveAspectRatio=\"xMidYMid meet\" style=\"will-change: transform;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" viewBox=\"0 0 16 16\">\n                <title>${closeTitle}</title>\n                <path d=\"M12 4.7l-.7-.7L8 7.3 4.7 4l-.7.7L7.3 8 4 11.3l.7.7L8 8.7l3.3 3.3.7-.7L8.7 8z\"></path>\n              </svg>\n            </button>\n          </div>\n\n          <div class=\"bx--modal-content bx--modal-scroll-content bx--modal-content--with-iframe\" data-dojo-attach-point=\"containerNode\" data-testid=\"${id}_modal-content\" id=\"${id}_modal-content\">\n          </div>\n          <div class=\"bx--modal-content--overflow-indicator\"></div>\n          <div data-dojo-attach-point=\"modalFooter\" class=\"bx--modal-footer\" data-testid=\"${id}_modal-footer\" id=\"${id}_modal-footer\"></div>\n          <span data-dojo-attach-event=\"onkeydown:escapeOnClose,onfocus:preventForwardsTabEscFromModal\" id=\"modal-end_${id}\" tabindex=\"0\"></span>\n        </div>\n    </div>\n</div>"}});
define("curam/modal/CuramCarbonModal",["dojo/text!curam/layout/resources/CuramCarbonModal.html","dojo/_base/declare","dojo/dom-construct","dijit/_Widget","dijit/_Templated","curam/modal/CuramBaseModal","curam/ModalUIMController","curam/util/onLoad","curam/util","dojo/aspect","curam/debug","curam/ui/PageRequest"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b){
return _2("curam.modal.CuramCarbonModal",[_6,_4,_5],{templateString:_1,modalContainerBaseModifier:"bx--modal-container--",carbonInstantiatedNode:null,modalSPMComponentRoot:null,currentPageID:null,modalSizeClassName:"md",carbonModal:null,isWizardModal:false,isIEGModal:null,cancelButtonID:null,redirectPageID:null,isRedirectModal:false,closingDialogEvent:null,addButttonEvent:null,_setCarbonModalAttr:{node:"carbonModalNode",type:"innerHTML"},carbonModalController:null,resizeController:null,isAlreadyShown:false,hasSingleModalButton:false,primaryButtonOnViewModalID:null,hasMultipleButtons:true,hasMultipleSubmitButtons:false,tlw:null,tabbingBackwards:null,isIEGScript:false,isAgendaPlayer:false,helpIconEnabled:false,wRef:null,closingAlready:false,closeTitle:LOCALISED_MODAL_CLOSE_BUTTON,helpAltText:LOCALISED_MODAL_HELP_ALT,helpTitle:LOCALISED_MODAL_HELP_TITLE,btnRequestCont:0,isOwnAction:false,isContentAction:false,cleanupFooter:false,actionSource:false,preventAction:false,fqWatchers:[],isIPNNav:false,constructor:function(_c){
this.iframeHref=_c.href;
},_getUnits:function(){
return "%";
},_calculateWidth:function(){
return "100";
},_calculateHeight:function(){
return "100";
},_determineSize:function(_d){
this.inherited(arguments);
if(_d.windowOptions){
this.setModalSizeClassName(_d.windowOptions["width"]);
}
},_setupHelpIcon:function(_e){
var _f=typeof _e!="undefined"?_e.helpEnabled:false;
var _10=_f?_e.helpExtension:"";
var _11=_f?_e.pageID:"";
if(!_f){
this.helpIcon.classList.add("is-not-displayed");
}else{
this.helpIconEnabled=true;
this.helpIcon.dataset.pageid=_11;
}
},displayHelp:function(e){
if(!e.keyCode||e.keyCode===13){
var _12=curam.config?curam.config.locale:jsL;
var url="./help.jsp?pageID="+this.helpIcon.dataset.pageid;
window.open(url);
}
},prepareAction:function(){
this.menuWatcher&&this.menuWatcher.remove();
this.formWatcher&&this.formWatcher.remove();
this.contentClickWatcher&&this.contentClickWatcher.remove();
!this.isIEGScript&&this.modalFooter.setAttribute("style","opacity:0.6;");
this.cleanupFooter=true;
this.btnRequestCont=0;
},actionEarly:function(){
this.prepareAction();
this.isContentAction=true;
},actionLate:function(){
this.prepareAction();
},actionWord:function(_13,evt){
this.isOwnAction=true;
this.wRef&&this.wRef.curam.util.clickButton(_13);
},actionSubmit:function(_14,evt){
this.isOwnAction=true;
this.actionEarly();
this.wRef&&this.wRef.curam.util.clickButton(_14);
this.isContentAction=false;
if(!this.preventAction){
dojo.publish("/curam/progress/display",[this.containerNode,100]);
}else{
this.modalFooter.removeAttribute("style");
}
},actionClick:function(evt,_15){
this.preventAction=false;
this.isOwnAction=true;
this.actionEarly();
var _16=this;
var _17;
if(_15){
_15(evt);
_17=setTimeout(function(){
clearTimeout(_17);
_16.modalFooter.removeAttribute("style");
},5000);
}else{
this.wRef&&this.wRef.curam.dialog.modalEventHandler(evt);
}
this.isContentAction=false;
var _18=evt.target.dataset&&evt.target.dataset.href;
if(_18&&_18.indexOf("FileDownload")>-1){
_17=setTimeout(function(){
dojo.publish("/curam/progress/unload");
clearTimeout(_17);
_16.modalFooter.removeAttribute("style");
},3000);
}
dojo.publish("/curam/progress/display",[this.containerNode]);
},actionCleanup:function(evt){
this.wRef&&(this.wRef.spm=null);
this.carbonModalController=null;
},_setClosableAttr:function(_19){
this.closeButton.setAttribute("style","display:"+(!!_19?"block":"none"));
},checkIfWizardModal:function(){
var _1a=this;
var _1b=this.tDojo.subscribe("/curam/CuramCarbonModal/wizardModalIndicator",this,function(){
_1a.isWizardModal=true;
_1a.modalFooter&&_1a.modalFooter.classList&&_1a.modalFooter.classList.add("bx--modal-footer--progress");
_1a.tDojo.unsubscribe(_1b);
});
},checkIfIEGModal:function(){
var _1c=this;
var _1d=this.tDojo.subscribe("/curam/CuramCarbonModal/iegModalIndicator",this,function(){
_1c.isIEGModal=true;
_1c.tDojo.unsubscribe(_1d);
});
},checkIfNonStandardModalButtons:function(){
var _1e=this;
var _1f=this.tDojo.subscribe("/curam/CuramCarbonModal/nonStandardModalFooter",this,function(_20){
if(_20){
if(_20.numButtons){
var _21=_20.numButtons;
if(_21==0){
var _22=this.iframe&&this.iframe.contentWindow.jsPageID;
if(_22){
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=_22;
}
}else{
if(_21==1){
_1e.hasSingleModalButton=true;
}
}
_1e.hasMultipleButtons=(_21<=1);
}else{
if(_20.hasDefaultSubmitButton){
_1e.hasMultipleSubmitButtons=true;
}
}
}
_1e.tDojo.unsubscribe(_1f);
});
},updateWizardCancelStyle:function(){
var _23="bx--btn--secondary";
if(this.hasSingleModalButton){
_23="bx--btn--primary";
}
if(this.cancelButtonID!=null){
var _24=document.getElementById(this.cancelButtonID);
if(this.isWizardModal){
_23="bx--btn--ghost";
}
}
if(_24){
_24.classList.add(_23);
}
},updateRegularCancelStyle:function(){
var _25=false;
if(this.cancelButtonID!=null){
_25=document.getElementById(this.cancelButtonID);
}
if(_25){
if(_25.classList.contains("bx--btn--secondary")||_25.classList.contains("bx--btn--primary")){
return;
}else{
var _26="bx--btn--secondary";
if(this.hasSingleModalButton){
_26="bx--btn--primary";
}
_25.classList.add(_26);
}
}
},checkPrimaryButtonOnViewModal:function(){
if(this.primaryButtonOnViewModalID!=null){
return document.getElementById(this.primaryButtonOnViewModalID);
}
},show:function(_27){
if(!this.isIEGScript&&!this.isAgendaPlayer){
if(this.modalFooter.classList.contains("is-not-displayed")){
this.modalFooter.classList.remove("is-not-displayed");
}
}
this.manageModalFooterButtons();
if(this.isIEGScript||this.isWizardModal){
this.updateWizardCancelStyle();
}else{
this.updateRegularCancelStyle();
}
this.modalFooter.childNodes.forEach(function(btn){
btn.classList.remove("is-not-displayed");
});
var _28;
var _29=800;
var _2a=this.isIEGScript&&!curam.util.getTopmostWindow().exitingIEGScript;
this.isIEGScript=this.tlw.exitingIEGScript;
var _2b=this.modalFooter.children;
var _2c=this.modalContainerBaseModifier+this.modalSizeClassName;
if(this.isIEGScript){
var _2d=this.iframe.contentWindow.document.body.getElementsByTagName("FORM");
_28=_2d?_2d[0]:undefined;
if(_28&&_28.tagName=="FORM"){
!this.modalFooter.classList.contains("is-not-displayed")&&this.modalFooter.classList.add("is-not-displayed");
}
}else{
_28=this.iframe.contentWindow.document.querySelector("#content");
if(_2a){
if(this.modalFooter.classList.contains("is-not-displayed")){
this.modalFooter.classList.remove("is-not-displayed");
}
}
}
if(_28&&_28!=null){
_29=_28.scrollHeight;
}
if(_29){
var _2e=_29<this.modalContainer.scrollHeight?50:110;
var _2f=this.isIEGScript?_29:_29+_2e;
this.iframe.style.height=_2f+"px";
}
if(_28&&_28.style){
_28.style.position="relative";
_28.style.top="0px";
}
if(window.spm&&_28){
this.resizeController=new window.spm.helpers.ModalLayoutHelper(this.containerNode,this.uimController,this.iframe,_28,this.isIEGScript);
this.resizeController.register();
}
this.modalContainer.classList.add(_2c);
if(this.iframe){
this.iframe.classList.add("bx--modal-content__iframe-wrapper");
}
this.carbonModalNode.classList.add("is-visible");
!this.isIEGScript&&this.modalFooter.removeAttribute("style");
var _30=this.iframe&&this.iframe.contentWindow.jsPageID;
var _31=curam.util.getTopmostWindow().dojo.global.passiveModalPageID;
if(_30&&_31&&(_30==_31)){
if(!this.modalFooter.classList.contains("is-not-displayed")){
this.modalFooter.classList.add("is-not-displayed");
}
this.carbonInstantiatedNode=this.carbonModalController.create(this.carbonModalNode);
this.carbonInstantiatedNode.show();
}
this.isAlreadyShown=true;
},setModalSizeClassName:function(_32){
if(_32){
if(_32<=420){
this.modalSizeClassName="xs";
}else{
if(_32>420&&_32<=576){
this.modalSizeClassName="sm";
}else{
if(_32>768&&_32<1200){
this.modalSizeClassName="lg";
}else{
if(_32>=1200){
this.modalSizeClassName="xlg";
}
}
}
}
}
},manageModalFooterButtons:function(){
var _33=this;
this.preventAction=false;
if(!this.ipnWatcher){
this.ipnWatcher=_a.before(this.uimController,"handleIPNTabClick",function(tab){
_33.isIPNNav=true;
});
}
if(!this._isCDEJModal&&this.wRef){
this.menuWatcher=_a.before(this.wRef.curam.util,"clickHandlerForListActionMenu",function(){
_33.actionLate();
});
this.formWatcher=_a.after(this.wRef,"dc",function(_34){
if(_34){
_33.actionEarly();
}
return _34;
});
this.contentClickWatcher=_a.after(this.wRef,"clickMarker",function(_35){
_33.actionLate();
return _35;
});
}else{
if(this.iframeHref.indexOf("frequency-editor.jsp")>-1){
var _36=this.iframe.contentWindow;
var _37=function(_38){
_33.preventAction=!_38;
return _38;
};
this.fqWatchers.push(_a.after(_36.curam.util.FrequencyEditor,"validateDailyPattern",_37),_a.after(_36.curam.util.FrequencyEditor,"validateWeeklyPattern",_37),_a.after(_36.curam.util.FrequencyEditor,"validateBimonthlyData",_37),_a.after(_36.curam.util.FrequencyEditor,"validateMonthlyData",_37),_a.after(_36.curam.util.FrequencyEditor,"validateBimonthlyData",_37),_a.after(_36.curam.util.FrequencyEditor,"validateYearlyData",_37));
}
}
var _39=this.iframe.contentWindow.jsPageID;
this.actionSource=_39;
if(this.hasMultipleButtons){
this.modalFooter.classList.add("bx--btn-set");
}
this.hasSingleModalButton=(this.btnRequestCont==1);
this.isOwnAction=false;
this.isContentAction=false;
this.isIPNNav=false;
},hide:function(_3a){
if(!this.closingAlready){
this.closingAlready=true;
dojo.publish("/curam/progress/unload");
this.hideAndClose(_3a);
}
},escapeOnClose:function(evt){
if(evt.keyCode===27){
var _3b=this;
_3b.carbonModalNode.classList.remove("is-visible");
}
},hideAndClose:function(_3c){
if(curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton){
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=false;
}
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=undefined;
this.fqWatchers&&this.fqWatchers.splice(0,this.fqWatchers.length);
curam.debug.log("curam.modal.CuramBaseModal._modalClosedHandler calling curam.util.setBrowserTabTitle");
if(this.parentWindow){
curam.util.setBrowserTabTitle(this.parentWindow.document.title);
if(this.iframe&&this.iframe.contentWindow){
var _3d=this.iframe.contentWindow.__customEventSubscriptions;
if(_3d){
for(var x=0;x<_3d.length;x++){
this.unsubscribes.push(_3d[x]);
}
}
}
}else{
curam.util.setBrowserTabTitle();
}
this.tDojo.publish("/curam/dialog/BeforeClose",[this.id,this.iframe.id]);
this.tDojo.unsubscribe(this.addButttonEvent);
if(this._scrollConnected){
this._scrollConnected=false;
}
if(this._relativePosition){
delete this._relativePosition;
}
curam.dialog.removeFromDialogHierarchy(this.iframe.contentWindow);
curam.dialog.removeFromDialogHierarchy(this.parentWindow);
dojo.forEach(this.modalconnects,dojo.disconnect);
this.modalconnects&&this.modalconnects.splice(0,this.modalconnects.length);
dojo.forEach(this.unsubscribes,this.tDojo.unsubscribe);
this.unsubscribes&&this.unsubscribes.splice(0,this.unsubscribes.length);
_8.removeSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
if(this._explodeNode&&this._explodeNode.parentNode){
this._explodeNode.parentNode.removeChild(this._explodeNode);
}
this.tDojo.publish("/curam/dialog/close/appExitConfirmation",[this.id]);
this._markAsActiveDialog(false);
if(typeof this.parentWindow!="undefined"&&this.parentWindow!=null){
this.parentWindow.focus();
}
if(_3c&&!_3c instanceof Event){
this.tlw.modalsToDestroy.push(_3c);
}else{
this.tlw.modalsToDestroy.push(this.modalSPMComponentRoot.id);
}
sessionStorage.removeItem("firstPageHitAfterModalOpened");
this.carbonModalNode.classList.remove("is-visible");
this.parentWindow&&curam.util.returnFocusToPopupActionAnchorElement(this.parentWindow);
},_destroyOldModals:function(){
if(this.tlw.modalsToDestroy){
var mId=null;
while((mId=this.tlw.modalsToDestroy.pop())!=null){
var _3e=document.querySelector("#"+mId);
if(_3e&&_3e.parentNode!=null){
_3e.parentNode.removeChild(_3e);
_3e=null;
}
}
this.tlw.modalsToDestroy.splice(0,this.tlw.modalsToDestroy.length);
}
},preventForwardsTabEscFromModal:function(e){
var _3f=this.modalSPMComponentRoot.id;
var _40=this.helpIconEnabled?"_modal-help":"_modal-button-close";
var _41=document.getElementById(_3f+_40);
this.handleTabbingForwards(e,_41);
},preventBackwardsTabEscFromModal:function(e){
var _42=this.modalSPMComponentRoot.id;
var _43="modal-end_"+_42;
var _44=_42+"_modal-help";
this.handleTabbingBackwards(e,_44,_43);
},handleSubmitBtnStyling:function(_45,_46){
var _47=" bx--btn--primary";
if(_46&&!_45){
_47=" bx--btn--secondary";
}
return _47;
},buildButtonHTML:function(_48,_49,_4a){
var _4b=_4a!=undefined?_48+_49+"='"+_4a+"'":_48;
return _4b;
},createHTMLFromButtonJson:function(_4c){
htmlString="";
htmlString=this.buildButtonHTML(htmlString,"data-href",_4c.href);
htmlString=this.buildButtonHTML(htmlString,"keepmodal",_4c.keepmodal);
htmlString=this.buildButtonHTML(htmlString,"accesskey",_4c.accesskey);
htmlString=this.buildButtonHTML(htmlString,"onmouseover",_4c.onmouseover);
htmlString=this.buildButtonHTML(htmlString,"onmouseout",_4c.onmouseout);
htmlString=this.buildButtonHTML(htmlString,"buttonid",_4c.buttonid);
htmlString=this.buildButtonHTML(htmlString,"data-control",_4c.datacontrol);
return htmlString;
},indciatesCancelButton:function(_4d){
var _4e=curam.util.Constants.RETURN_PAGE_PARAM;
var _4f=(_4d!=undefined);
var _50=_4f&&(_4d.length==1)&&(_4d.indexOf("#")!=-1);
if(_4f&&(_4d.length==0||_4d.indexOf("keepModal=")==-1&&_4d.indexOf(_4e+"=")==-1)&&!_50){
return true;
}
return false;
},postMixInProperties:function(){
this.inherited(arguments);
this.tlw=curam.util.getTopmostWindow();
this.tDojo=this.tlw.dojo;
},enableDraggableHookpoint:function(_51){
try{
require(["curam/application/modal/ModalHooks"],function(_52){
_52.enableDraggableModals(_51);
});
}
catch(e){
curam.debug.log("WARNING: The file curam.util.modal.ModalHooks.js is not found");
}
},postCreate:function(){
this.inherited(arguments);
this.agendaGuard=this.tDojo.subscribe("agenda/buttons/loaded",(function(){
this.modalFooter&&this.modalFooter.classList.add("is-not-displayed");
this.isAgendaPlayer=true;
}).bind(this));
this.carbonModalController=spm.Modal;
this.initModal(arguments);
this.tlw.modalsToDestroy=[];
this.modalSPMComponentRoot=this.carbonModalNode.parentNode;
document.body.appendChild(this.modalSPMComponentRoot);
this.rootNode=document.getElementById(this.modalSPMComponentRoot.id);
var _53=this;
this.enableDraggableHookpoint(this.rootNode.firstElementChild);
this.checkIfWizardModal();
this.checkIfNonStandardModalButtons();
this.checkIfIEGModal();
var _54=this.tDojo.subscribe("/curam/CuramCarbonModal/redirectingModal",this,(function(){
this.isRedirectModal=true;
}).bind(this));
var _55=this.carbonModalController.options.eventAfterHidden;
document.addEventListener(_55,(function(evt){
evt.stopPropagation();
this.hideAndClose();
}).bind(this));
this.setModalSizeClassName(this.width);
var _56=0;
this.addButttonEvent=this.tDojo.subscribe("/curam/CuramCarbonModal/addModalButton",this,function(_57,_58,_59,_5a,_5b){
if(this.iframe.contentWindow.jsPageID!==_5b.jsPageID){
return;
}
if(this.isIPNNav){
return;
}
if(_57&&!typeof _57==="object"){
throw new Error(_57+" is not a valid object for this button");
}
if(this.isRedirectModal){
this.hasSingleModalButton=undefined;
}
this.wRef=_5b;
var _5c=false;
if(_57.buttonid&&_57.buttonid=="commitDocChangesBtnIE"){
this.isOwnAction=true;
_5c=true;
}
this.btnRequestCont+=1;
if(!this.isOwnAction){
if(!this._isCDEJModal&&!this.isContentAction&&this.actionSource==_5b.jsPageID){
this.btnRequestCont=0;
return;
}else{
if(this.cleanupFooter){
this.modalFooter.innerHTML="";
this.cleanupFooter=false;
}
}
}else{
if(this.cleanupFooter){
this.modalFooter.innerHTML="";
this.cleanupFooter=false;
}
}
var _5d=_57&&_57.buttonid?_57.buttonid:this.modalSPMComponentRoot.id+"_modal-button_"+_56;
if(this.hasSingleModalButton){
this.primaryButtonOnViewModalID=_5d;
}
var _5e=this.hasSingleModalButton?" bx--btn--primary":" bx--btn--secondary";
var _5f=_57&&_57.href;
if(this.indciatesCancelButton(_5f)){
this.cancelButtonID=_5d;
_5e="";
if(this.isAlreadyShown){
_5e=this.isWizardModal&&!this.hasSingleModalButton?" bx--btn--ghost":_5e;
}
}
var _60=this.handleSubmitBtnStyling(_5a,this.hasMultipleSubmitButtons);
var _61=_59?_60:_5e;
var _62=_57?this.createHTMLFromButtonJson(_57)+">":">";
var _63=_3.toDom("<button "+_62+_58+"</button>");
_63.setAttribute("type","button");
_63.setAttribute("tabIndex","0");
if(_57.dataTestId&&_57.dataTestId!==null){
_63.setAttribute("data-testid",_57.dataTestId);
}
_63.setAttribute("id",_5d);
("bx--btn"+_61).split(" ").forEach(function(cls){
_63.classList.add(cls);
});
this.isRedirectModal&&!_63.classList.contains("is-not-displayed")&&_63.classList.add("is-not-displayed");
if(!_59){
_63.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
if(_53.wRef&&_57&&_57.onclick){
_53.actionClick(e,_53.wRef.Function(_57.onclick));
}else{
_53.actionClick(e);
}
});
_63.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.stopPropagation();
_53.carbonModalNode.classList.remove("is-visible");
}
});
}else{
var _64=_59.id;
if(_5c){
_63.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
_53.actionWord(_64,e);
});
_63.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.preventDefault();
e.stopPropagation();
_53.hideAndClose();
}
});
}else{
_63.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
_53.actionSubmit(_64,e);
});
}
this.wRef.dojo.subscribe("curam/CarbonWrapper/cleanup",function(e){
_53.actionCleanup();
});
_63.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.preventDefault();
e.stopPropagation();
_53.hideAndClose();
}
});
}
_3.place(_63,this.modalFooter);
_56++;
});
this.containerNode.appendChild(this.uimController.domNode);
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/spinner",function(e){
_53._displaySpinner(_53);
}));
},initModal:function(_65){
this._destroyOldModals();
this._verticalModalSpace=100;
this.draggable=false;
var _66=0.9;
this.maximumWidth=(dijit.getViewport().w*_66)-this._horizontalModalSpace;
this.maximumHeight=(dijit.getViewport().h*_66)-this._verticalModalSpace;
this.unsubscribes=[];
this.modalconnects=[];
this._isCDEJModal=(this.iframeHref.indexOf("CDEJ/popups")>-1||this.iframeHref.indexOf("frequency-editor.jsp")>-1||this.iframeHref.indexOf("user-pref")>-1);
this._initParentWindowRef();
curam.dialog.pushOntoDialogHierarchy(this.parentWindow||this.tlw);
this._registerInitListener();
var _67=dojo.subscribe("/curam/dialog/iframeUnloaded",this,function(_68,_69){
if(this.id==_68){
curam.dialog.removeFromDialogHierarchy(_69);
this.initDone=false;
this._registerInitListener();
}
});
this.unsubscribes.push(_67);
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=undefined;
var _6a=(function(_6b,_6c){
_8.removeSubscriber(this._getEventIdentifier(),_6a);
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_6c);
if(!this.isRegisteredForClosing){
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/close",this,function(_6d){
if(this.id==_6d){
this.hide();
this.menuWatcher&&this.menuWatcher.remove();
this.formWatcher&&this.formWatcher.remove();
this.contentClickWatcher&&this.contentClickWatcher.remove();
this.ipnWatcher.remove();
this.resizeController&&this.resizeController.reset();
this.resizeController=null;
this.tDojo.unsubscribe(this.agendaGuard);
this.agendaGuard=null;
if(this.wRef){
this.wRef.dojo.publish("curam/CarbonWrapper/cleanup");
this.wRef=null;
}
wRef=null;
this.iframe.setAttribute("src","");
this.iframe=null;
delete this.parentWindow;
this.uimController.destroyRecursive(false);
delete this.uimController;
this.destroyRecursive(false);
window.webpackJsonpspm=null;
}
}));
this.isRegisteredForClosing=true;
}
this.doShow(_6c);
this._notifyModalDisplayed();
}).bind(this);
_8.addSubscriber(this._getEventIdentifier(),_6a);
if(this._isCDEJModal){
_8.addSubscriber(this._getEventIdentifier(),function(_6e,_6f){
var _70=document.querySelector("#"+_6e);
if(_70){
_70.focus();
_70.contentWindow.focusFirst&&_70.contentWindow.focusFirst();
}
});
}
var _71=true;
this.onLoadSubsequentHandler=(function(_72,_73){
if(_71){
_71=false;
}else{
if(!_73.modalClosing){
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_73);
this._position(true);
this.doShow(_73);
this._notifyModalDisplayed();
}
}
var _74=document.querySelector("#"+_72);
_74&&_74.setAttribute("title",[LOCALISED_MODAL_FRAME_TITLE,_74.contentWindow.document.title].join(" - "));
}).bind(this);
_8.addSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/iframeFailedToLoad",this,function(_75){
if(this.id==_75){
_8.removeSubscriber(this._getEventIdentifier(),_6a);
this._determineSize({height:450,title:"Error!"});
this.doShow();
this._notifyModalDisplayed();
}
}));
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/displayed",this,function(){
curam.util._setModalCurrentlyOpening(false);
}),this.tDojo.subscribe("/curam/dialog/displayed",this,this._setFocusHandler),this.tDojo.subscribe("/curam/dialog/displayed",this,function(_76){
(_76==this.id)&&this.tDojo.publish("/curam/dialog/AfterDisplay",[_76]);
}));
var _77=function(_78){
return _78.indexOf(":")>0;
};
var _79=_77(this.iframeHref)?this.iframeHref:this._getBaseUrl(this.tlw.location.href)+jsL+"/"+this.iframeHref;
this.uimController=new _7({uid:this.id,url:_79,loadFrameOnCreate:false,inDialog:true,iframeId:this._getEventIdentifier(),width:this._calculateWidth(this.width)+"px",height:this.maximumHeight+"px"});
this.iframe=this.uimController.getIFrame();
this.set("content",this.uimController.domNode);
this.iframe.classList.add(this._getEventIdentifier());
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/displayed",this,this._modalDisplayedHandler),this.tDojo.subscribe("/curam/dialog/closed",this,this._modalClosedHandler));
this._registerOnIframeLoad(this._loadErrorHandler.bind(this));
this.uimController.loadPage();
},_initParentWindowRef:function(){
if(!this.parentWindow){
var _7a=curam.tab.getContentPanelIframe();
_7a&&(this.parentWindow=_7a.contentWindow);
}else{
this.inherited(arguments);
}
}});
});
