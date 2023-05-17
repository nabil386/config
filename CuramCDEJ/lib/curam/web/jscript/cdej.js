//>>built
require({cache:{"dojo/request/default":function(){
define(["exports","require","../has"],function(_1,_2,_3){
var _4=_3("config-requestProvider"),_5;
if(1||_3("host-webworker")){
_5="./xhr";
}else{
if(0){
_5="./node";
}
}
if(!_4){
_4=_5;
}
_1.getPlatformDefaultId=function(){
return _5;
};
_1.load=function(id,_6,_7,_8){
_2([id=="platform"?_5:_4],function(_9){
_7(_9);
});
};
});
},"cm/_base/_behavior":function(){
define(["dojo/behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{behaviors:{},addedBehaviors:{},addBehavior:function(_a){
var b=cm.behaviors[_a];
if(b&&!cm.addedBehaviors[_a]){
dojo.behavior.add(b);
cm.addedBehaviors[_a]=true;
dojo.behavior.apply();
}
},registerBehavior:function(_b,_c){
cm.behaviors[_b]=_c;
}});
return cm;
});
},"curam/date":function(){
define(["curam/define","dojo/date","curam/date/locale","dojo/date/stamp"],function(_d,_e,_f,_10){
curam.define.singleton("curam.date",{testLocale:null,isDate:function(str,fmt){
return (curam.date.getDateFromFormat(str,fmt)!=0);
},compareDates:function(d1,df1,d2,df2){
var d1=curam.date.getDateFromFormat(d1,df1);
if(d1==0){
return -1;
}
var d2=curam.date.getDateFromFormat(d2,df2);
if(d2==0){
return -1;
}
return _e.compare(d1,d2,"date");
},formatDate:function(d,fmt){
var _11=_f.format(d,{selector:"date",datePattern:fmt,locale:curam.date.getLocale()});
return _11;
},getDateFromFormat:function(str,fmt){
var res=_f.parse(str,{selector:"date",datePattern:fmt,locale:curam.date.getLocale()});
return (res==null)?"0":res;
},ISO8601StringToDate:function(val){
return _10.fromISOString(val);
},getLocale:function(){
var _12=(typeof jsL!="undefined"&&jsL)?jsL:(curam.config?curam.config.locale:null);
return _12||curam.date.testLocale||"en";
}});
return curam.date;
});
},"dojo/date":function(){
define(["./has","./_base/lang"],function(has,_13){
var _14={};
_14.getDaysInMonth=function(_15){
var _16=_15.getMonth();
var _17=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_16==1&&_14.isLeapYear(_15)){
return 29;
}
return _17[_16];
};
_14.isLeapYear=function(_18){
var _19=_18.getFullYear();
return !(_19%400)||(!(_19%4)&&!!(_19%100));
};
_14.getTimezoneName=function(_1a){
var str=_1a.toString();
var tz="";
var _1b;
var pos=str.indexOf("(");
if(pos>-1){
tz=str.substring(++pos,str.indexOf(")"));
}else{
var pat=/([A-Z\/]+) \d{4}$/;
if((_1b=str.match(pat))){
tz=_1b[1];
}else{
str=_1a.toLocaleString();
pat=/ ([A-Z\/]+)$/;
if((_1b=str.match(pat))){
tz=_1b[1];
}
}
}
return (tz=="AM"||tz=="PM")?"":tz;
};
_14.compare=function(_1c,_1d,_1e){
_1c=new Date(+_1c);
_1d=new Date(+(_1d||new Date()));
if(_1e=="date"){
_1c.setHours(0,0,0,0);
_1d.setHours(0,0,0,0);
}else{
if(_1e=="time"){
_1c.setFullYear(0,0,0);
_1d.setFullYear(0,0,0);
}
}
if(_1c>_1d){
return 1;
}
if(_1c<_1d){
return -1;
}
return 0;
};
_14.add=function(_1f,_20,_21){
var sum=new Date(+_1f);
var _22=false;
var _23="Date";
switch(_20){
case "day":
break;
case "weekday":
var _24,_25;
var mod=_21%5;
if(!mod){
_24=(_21>0)?5:-5;
_25=(_21>0)?((_21-5)/5):((_21+5)/5);
}else{
_24=mod;
_25=parseInt(_21/5);
}
var _26=_1f.getDay();
var adj=0;
if(_26==6&&_21>0){
adj=1;
}else{
if(_26==0&&_21<0){
adj=-1;
}
}
var _27=_26+_24;
if(_27==0||_27==6){
adj=(_21>0)?2:-2;
}
_21=(7*_25)+_24+adj;
break;
case "year":
_23="FullYear";
_22=true;
break;
case "week":
_21*=7;
break;
case "quarter":
_21*=3;
case "month":
_22=true;
_23="Month";
break;
default:
_23="UTC"+_20.charAt(0).toUpperCase()+_20.substring(1)+"s";
}
if(_23){
sum["set"+_23](sum["get"+_23]()+_21);
}
if(_22&&(sum.getDate()<_1f.getDate())){
sum.setDate(0);
}
return sum;
};
_14.difference=function(_28,_29,_2a){
_29=_29||new Date();
_2a=_2a||"day";
var _2b=_29.getFullYear()-_28.getFullYear();
var _2c=1;
switch(_2a){
case "quarter":
var m1=_28.getMonth();
var m2=_29.getMonth();
var q1=Math.floor(m1/3)+1;
var q2=Math.floor(m2/3)+1;
q2+=(_2b*4);
_2c=q2-q1;
break;
case "weekday":
var _2d=Math.round(_14.difference(_28,_29,"day"));
var _2e=parseInt(_14.difference(_28,_29,"week"));
var mod=_2d%7;
if(mod==0){
_2d=_2e*5;
}else{
var adj=0;
var _2f=_28.getDay();
var _30=_29.getDay();
_2e=parseInt(_2d/7);
mod=_2d%7;
var _31=new Date(_28);
_31.setDate(_31.getDate()+(_2e*7));
var _32=_31.getDay();
if(_2d>0){
switch(true){
case _2f==6:
adj=-1;
break;
case _2f==0:
adj=0;
break;
case _30==6:
adj=-1;
break;
case _30==0:
adj=-2;
break;
case (_32+mod)>5:
adj=-2;
}
}else{
if(_2d<0){
switch(true){
case _2f==6:
adj=0;
break;
case _2f==0:
adj=1;
break;
case _30==6:
adj=2;
break;
case _30==0:
adj=1;
break;
case (_32+mod)<0:
adj=2;
}
}
}
_2d+=adj;
_2d-=(_2e*2);
}
_2c=_2d;
break;
case "year":
_2c=_2b;
break;
case "month":
_2c=(_29.getMonth()-_28.getMonth())+(_2b*12);
break;
case "week":
_2c=parseInt(_14.difference(_28,_29,"day")/7);
break;
case "day":
_2c/=24;
case "hour":
_2c/=60;
case "minute":
_2c/=60;
case "second":
_2c/=1000;
case "millisecond":
_2c*=_29.getTime()-_28.getTime();
}
return Math.round(_2c);
};
1&&_13.mixin(_13.getObject("dojo.date",true),_14);
return _14;
});
},"curam/modal/CuramCarbonModal":function(){
define(["dojo/text!curam/layout/resources/CuramCarbonModal.html","dojo/_base/declare","dojo/dom-construct","dijit/_Widget","dijit/_Templated","curam/modal/CuramBaseModal","curam/ModalUIMController","curam/util/onLoad","curam/util","dojo/aspect","curam/debug","curam/ui/PageRequest"],function(_33,_34,_35,_36,_37,_38,_39,_3a,_3b,asp,_3c){
return _34("curam.modal.CuramCarbonModal",[_38,_36,_37],{templateString:_33,modalContainerBaseModifier:"bx--modal-container--",carbonInstantiatedNode:null,modalSPMComponentRoot:null,currentPageID:null,modalSizeClassName:"md",carbonModal:null,isWizardModal:false,isIEGModal:null,cancelButtonID:null,redirectPageID:null,isRedirectModal:false,closingDialogEvent:null,addButttonEvent:null,_setCarbonModalAttr:{node:"carbonModalNode",type:"innerHTML"},carbonModalController:null,resizeController:null,isAlreadyShown:false,hasSingleModalButton:false,primaryButtonOnViewModalID:null,hasMultipleButtons:true,hasMultipleSubmitButtons:false,tlw:null,tabbingBackwards:null,isIEGScript:false,isAgendaPlayer:false,helpIconEnabled:false,wRef:null,closingAlready:false,closeTitle:LOCALISED_MODAL_CLOSE_BUTTON,helpAltText:LOCALISED_MODAL_HELP_ALT,helpTitle:LOCALISED_MODAL_HELP_TITLE,btnRequestCont:0,isOwnAction:false,isContentAction:false,cleanupFooter:false,actionSource:false,preventAction:false,fqWatchers:[],isIPNNav:false,constructor:function(_3d){
this.iframeHref=_3d.href;
},_getUnits:function(){
return "%";
},_calculateWidth:function(){
return "100";
},_calculateHeight:function(){
return "100";
},_determineSize:function(_3e){
this.inherited(arguments);
if(_3e.windowOptions){
this.setModalSizeClassName(_3e.windowOptions["width"]);
}
},_setupHelpIcon:function(_3f){
var _40=typeof _3f!="undefined"?_3f.helpEnabled:false;
var _41=_40?_3f.helpExtension:"";
var _42=_40?_3f.pageID:"";
if(!_40){
this.helpIcon.classList.add("is-not-displayed");
}else{
this.helpIconEnabled=true;
this.helpIcon.dataset.pageid=_42;
}
},displayHelp:function(e){
if(!e.keyCode||e.keyCode===13){
var _43=curam.config?curam.config.locale:jsL;
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
},actionWord:function(_44,evt){
this.isOwnAction=true;
this.wRef&&this.wRef.curam.util.clickButton(_44);
},actionSubmit:function(_45,evt){
this.isOwnAction=true;
this.actionEarly();
this.wRef&&this.wRef.curam.util.clickButton(_45);
this.isContentAction=false;
if(!this.preventAction){
dojo.publish("/curam/progress/display",[this.containerNode,100]);
}else{
this.modalFooter.removeAttribute("style");
}
},actionClick:function(evt,_46){
this.preventAction=false;
this.isOwnAction=true;
this.actionEarly();
var _47=this;
var _48;
if(_46){
_46(evt);
_48=setTimeout(function(){
clearTimeout(_48);
_47.modalFooter.removeAttribute("style");
},5000);
}else{
this.wRef&&this.wRef.curam.dialog.modalEventHandler(evt);
}
this.isContentAction=false;
var _49=evt.target.dataset&&evt.target.dataset.href;
if(_49&&_49.indexOf("FileDownload")>-1){
_48=setTimeout(function(){
dojo.publish("/curam/progress/unload");
clearTimeout(_48);
_47.modalFooter.removeAttribute("style");
},3000);
}
dojo.publish("/curam/progress/display",[this.containerNode]);
},actionCleanup:function(evt){
this.wRef&&(this.wRef.spm=null);
this.carbonModalController=null;
},_setClosableAttr:function(_4a){
this.closeButton.setAttribute("style","display:"+(!!_4a?"block":"none"));
},checkIfWizardModal:function(){
var _4b=this;
var _4c=this.tDojo.subscribe("/curam/CuramCarbonModal/wizardModalIndicator",this,function(){
_4b.isWizardModal=true;
_4b.modalFooter&&_4b.modalFooter.classList&&_4b.modalFooter.classList.add("bx--modal-footer--progress");
_4b.tDojo.unsubscribe(_4c);
});
},checkIfIEGModal:function(){
var _4d=this;
var _4e=this.tDojo.subscribe("/curam/CuramCarbonModal/iegModalIndicator",this,function(){
_4d.isIEGModal=true;
_4d.tDojo.unsubscribe(_4e);
});
},checkIfNonStandardModalButtons:function(){
var _4f=this;
var _50=this.tDojo.subscribe("/curam/CuramCarbonModal/nonStandardModalFooter",this,function(_51){
if(_51){
if(_51.numButtons){
var _52=_51.numButtons;
if(_52==0){
var _53=this.iframe&&this.iframe.contentWindow.jsPageID;
if(_53){
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=_53;
}
}else{
if(_52==1){
_4f.hasSingleModalButton=true;
}
}
_4f.hasMultipleButtons=(_52<=1);
}else{
if(_51.hasDefaultSubmitButton){
_4f.hasMultipleSubmitButtons=true;
}
}
}
_4f.tDojo.unsubscribe(_50);
});
},updateWizardCancelStyle:function(){
var _54="bx--btn--secondary";
if(this.hasSingleModalButton){
_54="bx--btn--primary";
}
if(this.cancelButtonID!=null){
var _55=document.getElementById(this.cancelButtonID);
if(this.isWizardModal){
_54="bx--btn--ghost";
}
}
if(_55){
_55.classList.add(_54);
}
},updateRegularCancelStyle:function(){
var _56=false;
if(this.cancelButtonID!=null){
_56=document.getElementById(this.cancelButtonID);
}
if(_56){
if(_56.classList.contains("bx--btn--secondary")||_56.classList.contains("bx--btn--primary")){
return;
}else{
var _57="bx--btn--secondary";
if(this.hasSingleModalButton){
_57="bx--btn--primary";
}
_56.classList.add(_57);
}
}
},checkPrimaryButtonOnViewModal:function(){
if(this.primaryButtonOnViewModalID!=null){
return document.getElementById(this.primaryButtonOnViewModalID);
}
},show:function(_58){
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
var _59;
var _5a=800;
var _5b=this.isIEGScript&&!curam.util.getTopmostWindow().exitingIEGScript;
this.isIEGScript=this.tlw.exitingIEGScript;
var _5c=this.modalFooter.children;
var _5d=this.modalContainerBaseModifier+this.modalSizeClassName;
if(this.isIEGScript){
var _5e=this.iframe.contentWindow.document.body.getElementsByTagName("FORM");
_59=_5e?_5e[0]:undefined;
if(_59&&_59.tagName=="FORM"){
!this.modalFooter.classList.contains("is-not-displayed")&&this.modalFooter.classList.add("is-not-displayed");
}
}else{
_59=this.iframe.contentWindow.document.querySelector("#content");
if(_5b){
if(this.modalFooter.classList.contains("is-not-displayed")){
this.modalFooter.classList.remove("is-not-displayed");
}
}
}
if(_59&&_59!=null){
_5a=_59.scrollHeight;
}
if(_5a){
var _5f=_5a<this.modalContainer.scrollHeight?50:110;
var _60=this.isIEGScript?_5a:_5a+_5f;
this.iframe.style.height=_60+"px";
}
if(_59&&_59.style){
_59.style.position="relative";
_59.style.top="0px";
}
if(window.spm&&_59){
this.resizeController=new window.spm.helpers.ModalLayoutHelper(this.containerNode,this.uimController,this.iframe,_59,this.isIEGScript);
this.resizeController.register();
}
this.modalContainer.classList.add(_5d);
if(this.iframe){
this.iframe.classList.add("bx--modal-content__iframe-wrapper");
}
this.carbonModalNode.classList.add("is-visible");
!this.isIEGScript&&this.modalFooter.removeAttribute("style");
var _61=this.iframe&&this.iframe.contentWindow.jsPageID;
var _62=curam.util.getTopmostWindow().dojo.global.passiveModalPageID;
if(_61&&_62&&(_61==_62)){
if(!this.modalFooter.classList.contains("is-not-displayed")){
this.modalFooter.classList.add("is-not-displayed");
}
this.carbonInstantiatedNode=this.carbonModalController.create(this.carbonModalNode);
this.carbonInstantiatedNode.show();
}
this.isAlreadyShown=true;
},setModalSizeClassName:function(_63){
if(_63){
if(_63<=420){
this.modalSizeClassName="xs";
}else{
if(_63>420&&_63<=576){
this.modalSizeClassName="sm";
}else{
if(_63>768&&_63<1200){
this.modalSizeClassName="lg";
}else{
if(_63>=1200){
this.modalSizeClassName="xlg";
}
}
}
}
}
},manageModalFooterButtons:function(){
var _64=this;
this.preventAction=false;
if(!this.ipnWatcher){
this.ipnWatcher=asp.before(this.uimController,"handleIPNTabClick",function(tab){
_64.isIPNNav=true;
});
}
if(!this._isCDEJModal&&this.wRef){
this.menuWatcher=asp.before(this.wRef.curam.util,"clickHandlerForListActionMenu",function(){
_64.actionLate();
});
this.formWatcher=asp.after(this.wRef,"dc",function(_65){
if(_65){
_64.actionEarly();
}
return _65;
});
this.contentClickWatcher=asp.after(this.wRef,"clickMarker",function(_66){
_64.actionLate();
return _66;
});
}else{
if(this.iframeHref.indexOf("frequency-editor.jsp")>-1){
var _67=this.iframe.contentWindow;
var _68=function(_69){
_64.preventAction=!_69;
return _69;
};
this.fqWatchers.push(asp.after(_67.curam.util.FrequencyEditor,"validateDailyPattern",_68),asp.after(_67.curam.util.FrequencyEditor,"validateWeeklyPattern",_68),asp.after(_67.curam.util.FrequencyEditor,"validateBimonthlyData",_68),asp.after(_67.curam.util.FrequencyEditor,"validateMonthlyData",_68),asp.after(_67.curam.util.FrequencyEditor,"validateBimonthlyData",_68),asp.after(_67.curam.util.FrequencyEditor,"validateYearlyData",_68));
}
}
var _6a=this.iframe.contentWindow.jsPageID;
this.actionSource=_6a;
if(this.hasMultipleButtons){
this.modalFooter.classList.add("bx--btn-set");
}
this.hasSingleModalButton=(this.btnRequestCont==1);
this.isOwnAction=false;
this.isContentAction=false;
this.isIPNNav=false;
},hide:function(_6b){
if(!this.closingAlready){
this.closingAlready=true;
dojo.publish("/curam/progress/unload");
this.hideAndClose(_6b);
}
},escapeOnClose:function(evt){
if(evt.keyCode===27){
var _6c=this;
_6c.carbonModalNode.classList.remove("is-visible");
}
},hideAndClose:function(_6d){
if(curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton){
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=false;
}
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=undefined;
this.fqWatchers&&this.fqWatchers.splice(0,this.fqWatchers.length);
curam.debug.log("curam.modal.CuramBaseModal._modalClosedHandler calling curam.util.setBrowserTabTitle");
if(this.parentWindow){
curam.util.setBrowserTabTitle(this.parentWindow.document.title);
if(this.iframe&&this.iframe.contentWindow){
var _6e=this.iframe.contentWindow.__customEventSubscriptions;
if(_6e){
for(var x=0;x<_6e.length;x++){
this.unsubscribes.push(_6e[x]);
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
_3a.removeSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
if(this._explodeNode&&this._explodeNode.parentNode){
this._explodeNode.parentNode.removeChild(this._explodeNode);
}
this.tDojo.publish("/curam/dialog/close/appExitConfirmation",[this.id]);
this._markAsActiveDialog(false);
if(typeof this.parentWindow!="undefined"&&this.parentWindow!=null){
this.parentWindow.focus();
}
if(_6d&&!_6d instanceof Event){
this.tlw.modalsToDestroy.push(_6d);
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
var _6f=document.querySelector("#"+mId);
if(_6f&&_6f.parentNode!=null){
_6f.parentNode.removeChild(_6f);
_6f=null;
}
}
this.tlw.modalsToDestroy.splice(0,this.tlw.modalsToDestroy.length);
}
},preventForwardsTabEscFromModal:function(e){
var _70=this.modalSPMComponentRoot.id;
var _71=this.helpIconEnabled?"_modal-help":"_modal-button-close";
var _72=document.getElementById(_70+_71);
this.handleTabbingForwards(e,_72);
},preventBackwardsTabEscFromModal:function(e){
var _73=this.modalSPMComponentRoot.id;
var _74="modal-end_"+_73;
var _75=_73+"_modal-help";
this.handleTabbingBackwards(e,_75,_74);
},handleSubmitBtnStyling:function(_76,_77){
var _78=" bx--btn--primary";
if(_77&&!_76){
_78=" bx--btn--secondary";
}
return _78;
},buildButtonHTML:function(_79,_7a,_7b){
var _7c=_7b!=undefined?_79+_7a+"='"+_7b+"'":_79;
return _7c;
},createHTMLFromButtonJson:function(_7d){
htmlString="";
htmlString=this.buildButtonHTML(htmlString,"data-href",_7d.href);
htmlString=this.buildButtonHTML(htmlString,"keepmodal",_7d.keepmodal);
htmlString=this.buildButtonHTML(htmlString,"accesskey",_7d.accesskey);
htmlString=this.buildButtonHTML(htmlString,"onmouseover",_7d.onmouseover);
htmlString=this.buildButtonHTML(htmlString,"onmouseout",_7d.onmouseout);
htmlString=this.buildButtonHTML(htmlString,"buttonid",_7d.buttonid);
htmlString=this.buildButtonHTML(htmlString,"data-control",_7d.datacontrol);
return htmlString;
},indciatesCancelButton:function(_7e){
var _7f=curam.util.Constants.RETURN_PAGE_PARAM;
var _80=(_7e!=undefined);
var _81=_80&&(_7e.length==1)&&(_7e.indexOf("#")!=-1);
if(_80&&(_7e.length==0||_7e.indexOf("keepModal=")==-1&&_7e.indexOf(_7f+"=")==-1)&&!_81){
return true;
}
return false;
},postMixInProperties:function(){
this.inherited(arguments);
this.tlw=curam.util.getTopmostWindow();
this.tDojo=this.tlw.dojo;
},enableDraggableHookpoint:function(_82){
try{
require(["curam/application/modal/ModalHooks"],function(_83){
_83.enableDraggableModals(_82);
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
var _84=this;
this.enableDraggableHookpoint(this.rootNode.firstElementChild);
this.checkIfWizardModal();
this.checkIfNonStandardModalButtons();
this.checkIfIEGModal();
var _85=this.tDojo.subscribe("/curam/CuramCarbonModal/redirectingModal",this,(function(){
this.isRedirectModal=true;
}).bind(this));
var _86=this.carbonModalController.options.eventAfterHidden;
document.addEventListener(_86,(function(evt){
evt.stopPropagation();
this.hideAndClose();
}).bind(this));
this.setModalSizeClassName(this.width);
var _87=0;
this.addButttonEvent=this.tDojo.subscribe("/curam/CuramCarbonModal/addModalButton",this,function(_88,_89,_8a,_8b,_8c){
if(this.iframe.contentWindow.jsPageID!==_8c.jsPageID){
return;
}
if(this.isIPNNav){
return;
}
if(_88&&!typeof _88==="object"){
throw new Error(_88+" is not a valid object for this button");
}
if(this.isRedirectModal){
this.hasSingleModalButton=undefined;
}
this.wRef=_8c;
var _8d=false;
if(_88.buttonid&&_88.buttonid=="commitDocChangesBtnIE"){
this.isOwnAction=true;
_8d=true;
}
this.btnRequestCont+=1;
if(!this.isOwnAction){
if(!this._isCDEJModal&&!this.isContentAction&&this.actionSource==_8c.jsPageID){
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
var _8e=_88&&_88.buttonid?_88.buttonid:this.modalSPMComponentRoot.id+"_modal-button_"+_87;
if(this.hasSingleModalButton){
this.primaryButtonOnViewModalID=_8e;
}
var _8f=this.hasSingleModalButton?" bx--btn--primary":" bx--btn--secondary";
var _90=_88&&_88.href;
if(this.indciatesCancelButton(_90)){
this.cancelButtonID=_8e;
_8f="";
if(this.isAlreadyShown){
_8f=this.isWizardModal&&!this.hasSingleModalButton?" bx--btn--ghost":_8f;
}
}
var _91=this.handleSubmitBtnStyling(_8b,this.hasMultipleSubmitButtons);
var _92=_8a?_91:_8f;
var _93=_88?this.createHTMLFromButtonJson(_88)+">":">";
var _94=_35.toDom("<button "+_93+_89+"</button>");
_94.setAttribute("type","button");
_94.setAttribute("tabIndex","0");
if(_88.dataTestId&&_88.dataTestId!==null){
_94.setAttribute("data-testid",_88.dataTestId);
}
_94.setAttribute("id",_8e);
("bx--btn"+_92).split(" ").forEach(function(cls){
_94.classList.add(cls);
});
this.isRedirectModal&&!_94.classList.contains("is-not-displayed")&&_94.classList.add("is-not-displayed");
if(!_8a){
_94.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
if(_84.wRef&&_88&&_88.onclick){
_84.actionClick(e,_84.wRef.Function(_88.onclick));
}else{
_84.actionClick(e);
}
});
_94.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.stopPropagation();
_84.carbonModalNode.classList.remove("is-visible");
}
});
}else{
var _95=_8a.id;
if(_8d){
_94.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
_84.actionWord(_95,e);
});
_94.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.preventDefault();
e.stopPropagation();
_84.hideAndClose();
}
});
}else{
_94.addEventListener("click",function(e){
e.preventDefault();
e.stopPropagation();
_84.actionSubmit(_95,e);
});
}
this.wRef.dojo.subscribe("curam/CarbonWrapper/cleanup",function(e){
_84.actionCleanup();
});
_94.addEventListener("keydown",function(e){
if(e.keyCode===27){
e.preventDefault();
e.stopPropagation();
_84.hideAndClose();
}
});
}
_35.place(_94,this.modalFooter);
_87++;
});
this.containerNode.appendChild(this.uimController.domNode);
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/spinner",function(e){
_84._displaySpinner(_84);
}));
},initModal:function(_96){
this._destroyOldModals();
this._verticalModalSpace=100;
this.draggable=false;
var _97=0.9;
this.maximumWidth=(dijit.getViewport().w*_97)-this._horizontalModalSpace;
this.maximumHeight=(dijit.getViewport().h*_97)-this._verticalModalSpace;
this.unsubscribes=[];
this.modalconnects=[];
this._isCDEJModal=(this.iframeHref.indexOf("CDEJ/popups")>-1||this.iframeHref.indexOf("frequency-editor.jsp")>-1||this.iframeHref.indexOf("user-pref")>-1);
this._initParentWindowRef();
curam.dialog.pushOntoDialogHierarchy(this.parentWindow||this.tlw);
this._registerInitListener();
var _98=dojo.subscribe("/curam/dialog/iframeUnloaded",this,function(_99,_9a){
if(this.id==_99){
curam.dialog.removeFromDialogHierarchy(_9a);
this.initDone=false;
this._registerInitListener();
}
});
this.unsubscribes.push(_98);
curam.util.getTopmostWindow().dojo.global.passiveModalPageID=undefined;
var _9b=(function(_9c,_9d){
_3a.removeSubscriber(this._getEventIdentifier(),_9b);
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_9d);
if(!this.isRegisteredForClosing){
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/close",this,function(_9e){
if(this.id==_9e){
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
this.doShow(_9d);
this._notifyModalDisplayed();
}).bind(this);
_3a.addSubscriber(this._getEventIdentifier(),_9b);
if(this._isCDEJModal){
_3a.addSubscriber(this._getEventIdentifier(),function(_9f,_a0){
var _a1=document.querySelector("#"+_9f);
if(_a1){
_a1.focus();
_a1.contentWindow.focusFirst&&_a1.contentWindow.focusFirst();
}
});
}
var _a2=true;
this.onLoadSubsequentHandler=(function(_a3,_a4){
if(_a2){
_a2=false;
}else{
if(!_a4.modalClosing){
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_a4);
this._position(true);
this.doShow(_a4);
this._notifyModalDisplayed();
}
}
var _a5=document.querySelector("#"+_a3);
_a5&&_a5.setAttribute("title",[LOCALISED_MODAL_FRAME_TITLE,_a5.contentWindow.document.title].join(" - "));
}).bind(this);
_3a.addSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/iframeFailedToLoad",this,function(_a6){
if(this.id==_a6){
_3a.removeSubscriber(this._getEventIdentifier(),_9b);
this._determineSize({height:450,title:"Error!"});
this.doShow();
this._notifyModalDisplayed();
}
}));
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/displayed",this,function(){
curam.util._setModalCurrentlyOpening(false);
}),this.tDojo.subscribe("/curam/dialog/displayed",this,this._setFocusHandler),this.tDojo.subscribe("/curam/dialog/displayed",this,function(_a7){
(_a7==this.id)&&this.tDojo.publish("/curam/dialog/AfterDisplay",[_a7]);
}));
var _a8=function(_a9){
return _a9.indexOf(":")>0;
};
var _aa=_a8(this.iframeHref)?this.iframeHref:this._getBaseUrl(this.tlw.location.href)+jsL+"/"+this.iframeHref;
this.uimController=new _39({uid:this.id,url:_aa,loadFrameOnCreate:false,inDialog:true,iframeId:this._getEventIdentifier(),width:this._calculateWidth(this.width)+"px",height:this.maximumHeight+"px"});
this.iframe=this.uimController.getIFrame();
this.set("content",this.uimController.domNode);
this.iframe.classList.add(this._getEventIdentifier());
this.unsubscribes.push(this.tDojo.subscribe("/curam/dialog/displayed",this,this._modalDisplayedHandler),this.tDojo.subscribe("/curam/dialog/closed",this,this._modalClosedHandler));
this._registerOnIframeLoad(this._loadErrorHandler.bind(this));
this.uimController.loadPage();
},_initParentWindowRef:function(){
if(!this.parentWindow){
var _ab=curam.tab.getContentPanelIframe();
_ab&&(this.parentWindow=_ab.contentWindow);
}else{
this.inherited(arguments);
}
}});
});
},"curam/UIMController":function(){
define(["dojo/text!curam/layout/resources/UIMController.html","dojo/_base/declare","dijit/_Widget","dijit/_Templated","dijit/layout/ContentPane","curam/inspection/Layer","curam/tab","curam/debug","dojo/dom-style","dojo/dom-attr","curam/util","curam/util/onLoad"],function(_ac,_ad,_ae,_af,_b0,_b1,tab,_b2,_b3,_b4){
var _b5=_ad("curam.UIMController",[dijit._Widget,dijit._Templated],{TAB_HEIGHT:20,EVENT:{TOPIC_PREFIX:"UIMController.InPageNav_"},TOPIC_LOADED:"/curam/uim/controller/loaded",frameLoadEvent:"",uid:"",url:"",tabControllerId:"",oldTabsTitlesList:[],newTabsTitlesList:[],widgetsInTemplate:true,finishedLoadingTabs:false,classList:"",iframeId:"",height:"",width:"",iframeClassList:"",iscpiframe:"false",ipnTabClickEvent:null,title:"",handleIPNTabClickListener:null,inPageNavItems:null,loadFrameOnCreate:true,resizeFrameOnLoad:false,templateString:_ac,inDialog:false,constructor:function(_b6){
if(!_b6.uid){
throw "'uid' attribute not provided to constructor for"+" curam.UIMController(url,uid)";
}
this.uid="uimcontroller_"+_b6.uid;
this.tabControllerId="uimcontroller_tc_"+_b6.uid;
this.newTabsTitlesList=[];
this.ipnTabClickEvent=this.tabControllerId+"-selectChild";
if(this.height==""){
this.height="99%";
}
if(this.width==""){
this.width="99%";
}
curam.debug.log(_b2.getProperty("curam.UIMController.new")+" curam.UIMController()...");
curam.debug.log("curam.UIMController "+_b2.getProperty("curam.UIMController.identifier")+" "+this.uid);
curam.debug.log("curam.UIMController "+_b2.getProperty("curam.UIMController.url")+" "+this.url);
curam.debug.log("curam.UIMController "+_b2.getProperty("curam.UIMController.identifier")+" "+this.tabControllerId);
curam.debug.log("curam.UIMController: newTabsTitlesList "+" "+this.newTabsTitlesList);
_b1.register("curam/UIMController",this);
return this.uimController;
},postCreate:function(){
if(jsScreenContext.hasContextBits("EXTAPP")){
_b4.set(this.domNode,"style",{"height":this.height,"width":this.width});
}
curam.debug.log("UIMController Height: "+_b3.get(this.domNode,"height"));
curam.debug.log("UIMController Width: "+_b3.get(this.domNode,"width"));
this.frameLoadEvent=this.EVENT.TOPIC_PREFIX+this.frame.id;
this.setURL(this.url);
var _b7=dojo.hitch(this,"processFrameLoadEvent");
curam.util.onLoad.addSubscriber(this.frame.id,_b7);
dojo.connect(this,"destroy",function(){
curam.util.onLoad.removeSubscriber(this.iframeId,_b7);
this.fLoadFunct=null;
});
if(jsScreenContext.hasContextBits("EXTAPP")){
if(this.inDialog){
_b3.set(this.frame,{width:this.width,height:this.height});
}
}
curam.debug.log("curam.UIMController: loadFrameOnCreate="+this.loadFrameOnCreate);
curam.debug.log("curam.UIMController "+_b2.getProperty("curam.UIMController.url")+this.url);
if(this.loadFrameOnCreate==true&&typeof (this.url)!="undefined"){
curam.debug.log("curam.UIMController: "+_b2.getProperty("uram.UIMController.loading"));
this.loadPage();
}
},setURL:function(url){
if(url.indexOf("Page.do")==-1){
this.absoluteURL=true;
this.url=url;
}else{
this.absoluteURL=false;
this.url=this._trimURL(url);
}
},processFrameLoadEvent:function(_b8,_b9){
curam.debug.log("curam.UIMController: processFrameLoadEvent "+_b2.getProperty("curam.UIMController.processing.IPN")+_b9);
this.inPageNavItems=_b9.inPageNavItems;
curam.debug.log("curam.UIMController: processFrameLoadEvent: "+_b2.getProperty("curam.UIMController.processing"));
curam.debug.log("curam.UIMController.processFrameLoadEvent: this.tabController: "+this.tabController);
if(this.resizeFrameOnLoad==true){
var _ba=_b9.height;
curam.debug.log(_b2.getProperty("curam.UIMController.resizing")+_ba);
if(_ba){
_b3.set(this.getIFrame(),{height:_ba+"px"});
}
}
curam.debug.log(_b2.getProperty("curam.UIMController.IPN.items")+JSON.stringify(this.inPageNavItems));
if(!this.hasInPageNavigation()){
curam.debug.log(_b2.getProperty("curam.UIMController.no.IPN"));
this.clearIPNTabs();
if(!this._tabControllerHidden()){
curam.debug.log(_b2.getProperty("curam.UIMController.hiding"));
this.showTabContainer(false);
}
dojo.publish(this.TOPIC_LOADED);
if(this.inDialog===false){
curam.debug.log("curam.UIMController.processFrameLoadEvent calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
}
return;
}
curam.debug.log(_b2.getProperty("curam.UIMController.extract"));
var _bb=-1;
for(var i=0;i<this.inPageNavItems.length;i++){
this.newTabsTitlesList.push(this.inPageNavItems[i].title);
if(this.inPageNavItems[i].selected==true){
_bb=i;
}
curam.debug.log(_b2.getProperty("curam.UIMController.IPN.")+"["+this.inPageNavItems[i].title+", "+this.inPageNavItems[i].href+", "+this.inPageNavItems[i].selected+"]");
}
var _bc=!(this.compareLists(this.oldTabsTitlesList,this.newTabsTitlesList));
if(_bc){
this.clearIPNTabs(this);
this.createIPNTabs(this.inPageNavItems);
if(this._tabControllerHidden()){
this.showTabContainer(true);
}
}else{
curam.debug.log(_b2.getProperty("curam.UIMController.no.change"));
if(_bb>-1){
var _bd=this.tabController.getIndexOfChild(this.tabController.selectedChildWidget);
if(_bd!=_bb){
curam.debug.log(_b2.getProperty("curam.UIMController.change")+_bd+_b2.getProperty("curam.UIMController.to")+_bb);
this.toggleIPNTabClickEventListener("off");
this.tabController.selectChild(this.tabController.getChildren()[_bb]);
this.toggleIPNTabClickEventListener("on");
}
}
}
this.newTabsTitlesList=[];
curam.debug.log(_b2.getProperty("curam.UIMController.clear")+this.newTabsTitlesList);
this.finishedLoadingTabs=true;
dojo.publish(this.TOPIC_LOADED);
dojo.publish("/curam/tab/labelUpdated");
},_tabControllerHidden:function(){
return _b3.get(this.tabController.domNode,"display")=="none";
},toggleIPNTabClickEventListener:function(_be){
if(_be=="off"){
if(this.handleIPNTabClickListener!=null){
curam.debug.log(_b2.getProperty("curam.UIMController.off.listener"));
dojo.unsubscribe(this.handleIPNTabClickListener);
}
}else{
curam.debug.log(_b2.getProperty("curam.UIMController.on.listener"));
this.handleIPNTabClickListener=this.subscribe(this.ipnTabClickEvent,dojo.hitch(this,this.handleIPNTabClick));
}
},handleIPNTabClick:function(tab){
if(this.finishedLoadingTabs){
curam.debug.log(_b2.getProperty("curam.UIMController.finishing"));
this.finishedLoadingTabs=false;
this.setURL(this._getURLByTitle(tab.title));
this.loadPage();
}
},createIPNTabs:function(_bf){
this.toggleIPNTabClickEventListener("off");
if(!this.tabController){
console.error("curam.UIMController.createIPNTabs: "+_b2.getProperty("uram.UIMController.no.widget")+" '"+this.tabControllerId+"'");
}else{
curam.debug.log("curam.UIMController.createIPNTabs: "+_b2.getProperty("curam.UIMController.creating.tabs")+_bf);
var _c0=null;
for(var i=0;i<_bf.length;i++){
var cp=new dijit.layout.ContentPane({title:_bf[i].title});
this.tabController.addChild(cp);
if(_bf[i].selected==true||_c0==null){
_c0=cp;
}
this.oldTabsTitlesList.push(_bf[i].title);
curam.debug.log("curam.UIMController.createIPNTabs: "+_b2.getProperty("curam.UIMController.adding.tabs")+_bf[i].title);
}
this.tabController.startup();
this.tabController.selectChild(_c0);
}
this.toggleIPNTabClickEventListener("on");
this.newTabsTitlesList=[];
},clearIPNTabs:function(){
var _c1=false;
curam.debug.log("curam.UIMController.clearIPNTabs: "+_b2.getProperty("curam.UIMController.clearing.tabs")+this.oldTabsTitlesList);
this.toggleIPNTabClickEventListener("off");
try{
this.tabController.destroyDescendants();
}
catch(e){
curam.debug.log("curam.UIMController.clearIPNTabs - this.tabController.destroyDescendants(): "+_b2.getProperty("curam.UIMController.clear.warning"));
_c1=true;
}
this.tabController.selectedChildWidget=null;
this.oldTabsTitlesList=[];
this.toggleIPNTabClickEventListener("on");
if(!_c1){
curam.debug.log("curam.UIMController.createIPNTabs: "+_b2.getProperty("curam.UIMController.clearing.notify")+this.oldTabsTitlesList);
}
},compareLists:function(_c2,_c3){
curam.debug.log("curam.UIMController.compareLists: "+_b2.getProperty("curam.UIMController.comparing.tabs"));
curam.debug.log(_b2.getProperty("curam.UIMController.tab.list1")+_c2);
curam.debug.log(_b2.getProperty("curam.UIMController.tab.list1")+_c3);
var _c4=true;
if(_c2.length!=_c3.length){
_c4=false;
}
for(var i=0;i<_c2.length;i++){
if(_c2[i]!=_c3[i]){
_c4=false;
}
}
curam.debug.log(_b2.getProperty("curam.UIMController.result")+_c4);
return _c4;
},_getURLByTitle:function(_c5){
var url=null;
dojo.forEach(this.inPageNavItems,function(_c6){
if(_c6.title==_c5){
url=_c6.href;
}
});
curam.debug.log(url);
return url;
},_trimURL:function(_c7){
var idx=_c7.lastIndexOf("/");
if(idx>-1&&idx<=_c7.length){
return _c7.substring(idx+1);
}else{
return _c7;
}
},hasInPageNavigation:function(){
return this.inPageNavItems!=null;
},getIFrame:function(){
return this.frame;
},loadPage:function(_c8){
if(typeof (this.url)=="undefined"||this.url==null){
var e=new Error("curam.UIMController: Cannot load page as URL has not been set");
if(_c8){
_c8.errback(e);
}
throw e;
}
if(_c8){
var st=curam.util.subscribe(this.TOPIC_LOADED,function(){
curam.util.unsubscribe(st);
_c8.callback();
});
}
var _c9=this._getFullURL();
curam.debug.log("curam.UIMController.loadPage(): "+_b2.getProperty("curam.UIMController.set.source")+this.frame.id+" to url: "+_c9);
_b4.set(this.frame,"src",_c9);
},_getFullURL:function(){
if(typeof (this.absoluteURL)!="undefined"&&this.absoluteURL==true){
return this.url;
}
var _ca;
if(this.url.indexOf("?")==-1){
_ca="?";
}else{
_ca="&";
}
var _cb=curam.config?curam.config.locale:jsL;
var _cc="";
if(window==curam.util.getTopmostWindow()){
_cc=_cb+"/";
}
if(this.url.indexOf("o3nocache=")==-1){
return _cc+this.url+_ca+curam.util.getCacheBusterParameter();
}else{
return _cc+this.url;
}
},showTabContainer:function(_cd){
if(_cd&&!this.hasInPageNavigation()){
curam.debug.log(_b2.getProperty("curam.UIMController.ignore.reuest"));
return;
}
_b3.set(this.frameWrapper,"top",(_cd?this.TAB_HEIGHT+7:"0")+"px");
_b3.set(this.tabController.domNode,"display",_cd?"block":"none");
if(this.hasInPageNavigation()){
_b3.set(this.tabController.domNode.parentNode,"overflow","visible");
}
if(_cd){
this.tabController.resize();
}
},setDimensionsForModalDialog:function(w,h,_ce){
curam.debug.log("curam.UIMController:setDimensionsForModalDialog() - "+"w="+w+", h="+h);
_b3.set(this.frame,{width:w,height:h});
_b3.set(this.tabController.domNode,{width:w});
if(typeof (_ce.inPageNavItems)!="undefined"){
h+=this.TAB_HEIGHT+5;
curam.debug.log("cura.UIMController:setDimensionsForModalDialog() - "+_b2.getProperty("curam.UIMController.height"));
}
_b3.set(this.domNode,{width:w,height:h});
},destroy:function(){
this.iframe=null;
this.inPageNavItems=null;
dojo.unsubscribe(this.handleIPNTabClickListener);
this.tabController.destroy();
this.inherited(arguments);
}});
return _b5;
});
},"curam/util/ui/form/renderer/TextEditRendererFormEventsAdapter":function(){
define(["dojo/_base/declare","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter"],function(_cf,_d0){
var _d1=_cf("curam.util.ui.form.renderer.TextEditRendererFormEventsAdapter",_d0,{setFormElementValue:function(_d2){
this.element.value=_d2;
},getFormElementValue:function(){
return this.getFormElement().value;
},});
return _d1;
});
},"curam/ajax":function(){
define(["curam/util/Request"],function(_d3){
var _d4=function(_d5,_d6){
this.target=_d5;
this.inputProvider=_d6||"null";
};
var _d7={doRequest:function(_d8,_d9,_da,_db){
var _dc="../servlet/JSONServlet";
var _dd=this;
if(_da){
_dc="../"+_dc;
}
var _de={caller:this.target.id,operation:_d8,inputProvider:this.inputProvider,args:_d9};
function _df(_e0,_e1){
_e0=dojo.fromJson(_e0);
if(_e0 instanceof Array){
if(_e0.length>1){
if(_e1=="getCodeTableSubset"){
_dd.fillCTWithBlank(_e0);
}else{
_dd.fillCT(_e0);
}
}else{
if(_e1=="getCodeTableSubset"){
_dd.fillCTWithBlank(_e0);
}else{
_dd.fillSingle(_e0,true);
}
}
}else{
_dd.fillSingle(_e0);
}
};
_d3.post({url:_dc,handleAs:"text",load:function(_e2,evt){
_df(_e2,_d8);
},error:function(){
alert("error");
},content:{"content":dojo.toJson(_de)},preventCache:true,sync:_db});
},fillCT:function(_e3){
this.target.options.length=0;
for(var i=0;i<_e3.length;i++){
this.target.options[i]=new Option(_e3[i]["descr"],_e3[i]["code"],_e3[i]["default"]);
}
},fillCTWithBlank:function(_e4){
this.target.options.length=0;
this.target.options[0]=new Option("");
for(var i=0;i<_e4.length;i++){
this.target.options[i+1]=new Option(_e4[i]["descr"],_e4[i]["code"]);
}
},fillSingle:function(_e5,_e6){
if(_e6){
this.target.value=_e5[0]["value"];
}else{
this.target.value=_e5["value"];
}
}};
dojo.mixin(_d4.prototype,_d7);
dojo.global.AJAXCall=_d4;
return _d4;
});
},"curam/util/Request":function(){
define(["dojo/_base/xhr","curam/debug","curam/util/ResourceBundle","curam/inspection/Layer","curam/util/LocalConfig"],function(xhr,_e7,_e8,_e9,_ea){
var _eb=new _e8("curam.application.Request");
_isLoginPage=null,isLoginPage=function(_ec){
if(_isLoginPage){
return _isLoginPage(_ec);
}else{
return _ec.responseText.indexOf("action=\"j_security_check\"")>0;
}
},errorDisplayHookpoint=function(err,_ed){
if(isLoginPage(_ed.xhr)){
_e7.log(_eb.getProperty("sessionExpired"));
}else{
_e7.log(_eb.getProperty("ajaxError"));
}
_e7.log(err);
_e7.log("HTTP status was: "+_ed.xhr.status);
},_xhr=function(_ee,_ef){
var _f0=_ea.readOption("ajaxDebugMode","false")=="true";
var _f1=_ef.error;
if(_f0){
_ef.error=function(err,_f2){
if(_ef.errorHandlerOverrideDefault!==true){
errorDisplayHookpoint(err,_f2);
}
if(_f1){
_f1(err,_f2);
}
};
}
var _f3=_ee(_ef);
return _f3;
};
var _f4={post:function(_f5){
return _xhr(xhr.post,_f5);
},get:function(_f6){
return _xhr(xhr.get,_f6);
},setLoginPageDetector:function(_f7){
_isLoginPage=_f7;
},checkLoginPage:function(_f8){
return isLoginPage(_f8);
}};
_e9.register("curam/util/Request",_f4);
return _f4;
});
},"curam/widget/DropDownButton":function(){
define(["dojo/_base/declare","dojo/text!curam/widget/templates/DropDownButton.html","dijit/form/DropDownButton","curam/widget/_HasDropDown"],function(_f9,_fa){
var _fb=_f9("curam.widget.DropDownButton",[dijit.form.DropDownButton,curam.widget._HasDropDown],{templateString:_fa});
return _fb;
});
},"cm/_base/_form":function(){
define([],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{checkAll:function(_fc,_fd){
cm.query("input[type='checkbox']",_fd).forEach("item.checked = "+(_fc?"true":"false"));
},setFormSubmitted:function(_fe,_ff){
_fe._alreadySubmitted=_ff;
},wasFormSubmitted:function(form){
return form._alreadySubmitted;
},getFormItems:function(){
if(cm._formItems){
return cm._formItems;
}
var _100=dojo.query("input[name='__o3fmeta']");
var data=_100.length>0?dojo.fromJson(_100[0].value):{};
var _101=[];
for(var x in data){
_101.push(x);
}
cm._formItems=new function(){
this.length=function(){
return _101.length;
};
this.getNames=function(){
return _101;
};
this.getInputs=function(_102){
var _103=[];
dojo.forEach(_101,function(name,_104){
if(!_102||this.isMandatory(_104)){
_103.push("[name='"+name+"']");
}
},this);
return _103.length>0?dojo.query(_103.join(",")):[];
};
function fn(_105){
return function(_106){
var d=data[dojo.isString(_106)?_106:_101[_106]];
return d?d[_105]:null;
};
};
this.getTargetPath=fn(0);
this.getLabel=fn(1);
this.getDomain=fn(2);
this.isMandatory=fn(3);
};
return cm._formItems;
}});
return cm;
});
},"dijit/MenuBar":function(){
define(["dojo/_base/declare","dojo/keys","./_MenuBase","dojo/text!./templates/MenuBar.html"],function(_107,keys,_108,_109){
return _107("dijit.MenuBar",_108,{templateString:_109,baseClass:"dijitMenuBar",popupDelay:0,_isMenuBar:true,_orient:["below"],_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}
},focusChild:function(item){
this.inherited(arguments);
if(this.activated&&item.popup&&!item.disabled){
this._openItemPopup(item,true);
}
},_onChildDeselect:function(item){
if(this.currentPopupItem==item){
this.currentPopupItem=null;
item._closePopup();
}
this.inherited(arguments);
},_onLeftArrow:function(){
this.focusPrev();
},_onRightArrow:function(){
this.focusNext();
},_onDownArrow:function(evt){
this._moveToPopup(evt);
},_onUpArrow:function(){
},onItemClick:function(item,evt){
if(item.popup&&item.popup.isShowingNow&&(!/^key/.test(evt.type)||evt.keyCode!==keys.DOWN_ARROW)){
item.focusNode.focus();
this._cleanUp(true);
}else{
this.inherited(arguments);
}
}});
});
},"dijit/Viewport":function(){
define(["dojo/Evented","dojo/on","dojo/domReady","dojo/sniff","dojo/window"],function(_10a,on,_10b,has,_10c){
var _10d=new _10a();
var _10e;
_10b(function(){
var _10f=_10c.getBox();
_10d._rlh=on(window,"resize",function(){
var _110=_10c.getBox();
if(_10f.h==_110.h&&_10f.w==_110.w){
return;
}
_10f=_110;
_10d.emit("resize");
});
if(has("ie")==8){
var _111=screen.deviceXDPI;
setInterval(function(){
if(screen.deviceXDPI!=_111){
_111=screen.deviceXDPI;
_10d.emit("resize");
}
},500);
}
if(has("ios")){
on(document,"focusin",function(evt){
_10e=evt.target;
});
on(document,"focusout",function(evt){
_10e=null;
});
}
});
_10d.getEffectiveBox=function(doc){
var box=_10c.getBox(doc);
var tag=_10e&&_10e.tagName&&_10e.tagName.toLowerCase();
if(has("ios")&&_10e&&!_10e.readOnly&&(tag=="textarea"||(tag=="input"&&/^(color|email|number|password|search|tel|text|url)$/.test(_10e.type)))){
box.h*=(orientation==0||orientation==180?0.66:0.4);
var rect=_10e.getBoundingClientRect();
box.h=Math.max(box.h,rect.top+rect.height);
}
return box;
};
return _10d;
});
},"dijit/Dialog":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/Deferred","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/fx","dojo/i18n","dojo/keys","dojo/_base/lang","dojo/on","dojo/ready","dojo/sniff","dojo/window","dojo/dnd/Moveable","dojo/dnd/TimedMoveable","./focus","./_base/manager","./_Widget","./_TemplatedMixin","./_CssStateMixin","./form/_FormMixin","./_DialogMixin","./DialogUnderlay","./layout/ContentPane","./layout/utils","dojo/text!./templates/Dialog.html","./a11yclick","dojo/i18n!./nls/common"],function(_112,_113,_114,_115,_116,dom,_117,_118,_119,fx,i18n,keys,lang,on,_11a,has,_11b,_11c,_11d,_11e,_11f,_120,_121,_122,_123,_124,_125,_126,_127,_128){
var _129=new _116();
_129.resolve(true);
var _12a=_115("dijit._DialogBase"+(has("dojo-bidi")?"_NoBidi":""),[_121,_123,_124,_122],{templateString:_128,baseClass:"dijitDialog",cssStateNodes:{closeButtonNode:"dijitDialogCloseIcon"},_setTitleAttr:{node:"titleNode",type:"innerHTML"},open:false,duration:_11f.defaultDuration,refocus:true,autofocus:true,_firstFocusItem:null,_lastFocusItem:null,draggable:true,_setDraggableAttr:function(val){
this._set("draggable",val);
},maxRatio:0.9,closable:true,_setClosableAttr:function(val){
this.closeButtonNode.style.display=val?"":"none";
this._set("closable",val);
},postMixInProperties:function(){
var _12b=i18n.getLocalization("dijit","common");
lang.mixin(this,_12b);
this.inherited(arguments);
},postCreate:function(){
_119.set(this.domNode,{display:"none",position:"absolute"});
this.ownerDocumentBody.appendChild(this.domNode);
this.inherited(arguments);
_114.after(this,"onExecute",lang.hitch(this,"hide"),true);
_114.after(this,"onCancel",lang.hitch(this,"hide"),true);
this._modalconnects=[];
},onLoad:function(){
this.resize();
this._position();
if(this.autofocus&&_12c.isTop(this)){
this._getFocusItems();
_11e.focus(this._firstFocusItem);
}
this.inherited(arguments);
},focus:function(){
this._getFocusItems();
_11e.focus(this._firstFocusItem);
},_endDrag:function(){
var _12d=_118.position(this.domNode),_12e=_11b.getBox(this.ownerDocument);
_12d.y=Math.min(Math.max(_12d.y,0),(_12e.h-_12d.h));
_12d.x=Math.min(Math.max(_12d.x,0),(_12e.w-_12d.w));
this._relativePosition=_12d;
this._position();
},_setup:function(){
var node=this.domNode;
if(this.titleBar&&this.draggable){
this._moveable=new ((has("ie")==6)?_11d:_11c)(node,{handle:this.titleBar});
_114.after(this._moveable,"onMoveStop",lang.hitch(this,"_endDrag"),true);
}else{
_117.add(node,"dijitDialogFixed");
}
this.underlayAttrs={dialogId:this.id,"class":_113.map(this["class"].split(/\s/),function(s){
return s+"_underlay";
}).join(" "),_onKeyDown:lang.hitch(this,"_onKey"),ownerDocument:this.ownerDocument};
},_size:function(){
this.resize();
},_position:function(){
if(!_117.contains(this.ownerDocumentBody,"dojoMove")){
var node=this.domNode,_12f=_11b.getBox(this.ownerDocument),p=this._relativePosition,bb=p?null:_118.position(node),l=Math.floor(_12f.l+(p?p.x:(_12f.w-bb.w)/2)),t=Math.floor(_12f.t+(p?p.y:(_12f.h-bb.h)/2));
_119.set(node,{left:l+"px",top:t+"px"});
}
},_onKey:function(evt){
if(evt.keyCode==keys.TAB){
this._getFocusItems();
var node=evt.target;
if(this._firstFocusItem==this._lastFocusItem){
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._firstFocusItem&&evt.shiftKey){
_11e.focus(this._lastFocusItem);
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._lastFocusItem&&!evt.shiftKey){
_11e.focus(this._firstFocusItem);
evt.stopPropagation();
evt.preventDefault();
}
}
}
}else{
if(this.closable&&evt.keyCode==keys.ESCAPE){
this.onCancel();
evt.stopPropagation();
evt.preventDefault();
}
}
},show:function(){
if(this.open){
return _129.promise;
}
if(!this._started){
this.startup();
}
if(!this._alreadyInitialized){
this._setup();
this._alreadyInitialized=true;
}
if(this._fadeOutDeferred){
this._fadeOutDeferred.cancel();
_12c.hide(this);
}
var win=_11b.get(this.ownerDocument);
this._modalconnects.push(on(win,"scroll",lang.hitch(this,"resize",null)));
this._modalconnects.push(on(this.domNode,"keydown",lang.hitch(this,"_onKey")));
_119.set(this.domNode,{opacity:0,display:""});
this._set("open",true);
this._onShow();
this.resize();
this._position();
var _130;
this._fadeInDeferred=new _116(lang.hitch(this,function(){
_130.stop();
delete this._fadeInDeferred;
}));
var _131=this._fadeInDeferred.promise;
_130=fx.fadeIn({node:this.domNode,duration:this.duration,beforeBegin:lang.hitch(this,function(){
_12c.show(this,this.underlayAttrs);
}),onEnd:lang.hitch(this,function(){
if(this.autofocus&&_12c.isTop(this)){
this._getFocusItems();
_11e.focus(this._firstFocusItem);
}
this._fadeInDeferred.resolve(true);
delete this._fadeInDeferred;
})}).play();
return _131;
},hide:function(){
if(!this._alreadyInitialized||!this.open){
return _129.promise;
}
if(this._fadeInDeferred){
this._fadeInDeferred.cancel();
}
var _132;
this._fadeOutDeferred=new _116(lang.hitch(this,function(){
_132.stop();
delete this._fadeOutDeferred;
}));
this._fadeOutDeferred.then(lang.hitch(this,"onHide"));
var _133=this._fadeOutDeferred.promise;
_132=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,function(){
this.domNode.style.display="none";
_12c.hide(this);
this._fadeOutDeferred.resolve(true);
delete this._fadeOutDeferred;
})}).play();
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
return _133;
},resize:function(dim){
if(this.domNode.style.display!="none"){
this._checkIfSingleChild();
if(!dim){
if(this._shrunk){
if(this._singleChild){
if(typeof this._singleChildOriginalStyle!="undefined"){
this._singleChild.domNode.style.cssText=this._singleChildOriginalStyle;
delete this._singleChildOriginalStyle;
}
}
_113.forEach([this.domNode,this.containerNode,this.titleBar,this.actionBarNode],function(node){
if(node){
_119.set(node,{position:"static",width:"auto",height:"auto"});
}
});
this.domNode.style.position="absolute";
}
var _134=_11b.getBox(this.ownerDocument);
_134.w*=this.maxRatio;
_134.h*=this.maxRatio;
var bb=_118.position(this.domNode);
if(bb.w>=_134.w||bb.h>=_134.h){
dim={w:Math.min(bb.w,_134.w),h:Math.min(bb.h,_134.h)};
this._shrunk=true;
}else{
this._shrunk=false;
}
}
if(dim){
_118.setMarginBox(this.domNode,dim);
var _135=[];
if(this.titleBar){
_135.push({domNode:this.titleBar,region:"top"});
}
if(this.actionBarNode){
_135.push({domNode:this.actionBarNode,region:"bottom"});
}
var _136={domNode:this.containerNode,region:"center"};
_135.push(_136);
var _137=_127.marginBox2contentBox(this.domNode,dim);
_127.layoutChildren(this.domNode,_137,_135);
if(this._singleChild){
var cb=_127.marginBox2contentBox(this.containerNode,_136);
this._singleChild.resize({w:cb.w,h:cb.h});
}else{
this.containerNode.style.overflow="auto";
this._layoutChildren();
}
}else{
this._layoutChildren();
}
if(!has("touch")&&!dim){
this._position();
}
}
},_layoutChildren:function(){
_113.forEach(this.getChildren(),function(_138){
if(_138.resize){
_138.resize();
}
});
},destroy:function(){
if(this._fadeInDeferred){
this._fadeInDeferred.cancel();
}
if(this._fadeOutDeferred){
this._fadeOutDeferred.cancel();
}
if(this._moveable){
this._moveable.destroy();
}
var h;
while(h=this._modalconnects.pop()){
h.remove();
}
_12c.hide(this);
this.inherited(arguments);
}});
if(has("dojo-bidi")){
_12a=_115("dijit._DialogBase",_12a,{_setTitleAttr:function(_139){
this._set("title",_139);
this.titleNode.innerHTML=_139;
this.applyTextDir(this.titleNode);
},_setTextDirAttr:function(_13a){
if(this._created&&this.textDir!=_13a){
this._set("textDir",_13a);
this.set("title",this.title);
}
}});
}
var _13b=_115("dijit.Dialog",[_126,_12a],{});
_13b._DialogBase=_12a;
var _12c=_13b._DialogLevelManager={_beginZIndex:950,show:function(_13c,_13d){
ds[ds.length-1].focus=_11e.curNode;
var _13e=ds[ds.length-1].dialog?ds[ds.length-1].zIndex+2:_13b._DialogLevelManager._beginZIndex;
_119.set(_13c.domNode,"zIndex",_13e);
_125.show(_13d,_13e-1);
ds.push({dialog:_13c,underlayAttrs:_13d,zIndex:_13e});
},hide:function(_13f){
if(ds[ds.length-1].dialog==_13f){
ds.pop();
var pd=ds[ds.length-1];
if(ds.length==1){
_125.hide();
}else{
_125.show(pd.underlayAttrs,pd.zIndex-1);
}
if(_13f.refocus){
var _140=pd.focus;
if(pd.dialog&&(!_140||!dom.isDescendant(_140,pd.dialog.domNode))){
pd.dialog._getFocusItems();
_140=pd.dialog._firstFocusItem;
}
if(_140){
try{
_140.focus();
}
catch(e){
}
}
}
}else{
var idx=_113.indexOf(_113.map(ds,function(elem){
return elem.dialog;
}),_13f);
if(idx!=-1){
ds.splice(idx,1);
}
}
},isTop:function(_141){
return ds[ds.length-1].dialog==_141;
}};
var ds=_13b._dialogStack=[{dialog:null,focus:null,underlayAttrs:null}];
_11e.watch("curNode",function(attr,_142,node){
var _143=ds[ds.length-1].dialog;
if(node&&_143&&!_143._fadeOutDeferred&&node.ownerDocument==_143.ownerDocument){
do{
if(node==_143.domNode||_117.contains(node,"dijitPopup")){
return;
}
}while(node=node.parentNode);
_143.focus();
}
});
if(1){
_11a(0,function(){
var _144=["dijit/TooltipDialog"];
_112(_144);
});
}
return _13b;
});
},"dojo/date/locale":function(){
define(["../_base/lang","../_base/array","../date","../cldr/supplemental","../i18n","../regexp","../string","../i18n!../cldr/nls/gregorian","module"],function(lang,_145,date,_146,i18n,_147,_148,_149,_14a){
var _14b={};
lang.setObject(_14a.id.replace(/\//g,"."),_14b);
function _14c(_14d,_14e,_14f,_150){
return _150.replace(/([a-z])\1*/ig,function(_151){
var s,pad,c=_151.charAt(0),l=_151.length,_152=["abbr","wide","narrow"];
switch(c){
case "G":
s=_14e[(l<4)?"eraAbbr":"eraNames"][_14d.getFullYear()<0?0:1];
break;
case "y":
s=_14d.getFullYear();
switch(l){
case 1:
break;
case 2:
if(!_14f.fullYear){
s=String(s);
s=s.substr(s.length-2);
break;
}
default:
pad=true;
}
break;
case "Q":
case "q":
s=Math.ceil((_14d.getMonth()+1)/3);
pad=true;
break;
case "M":
case "L":
var m=_14d.getMonth();
if(l<3){
s=m+1;
pad=true;
}else{
var _153=["months",c=="L"?"standAlone":"format",_152[l-3]].join("-");
s=_14e[_153][m];
}
break;
case "w":
var _154=0;
s=_14b._getWeekOfYear(_14d,_154);
pad=true;
break;
case "d":
s=_14d.getDate();
pad=true;
break;
case "D":
s=_14b._getDayOfYear(_14d);
pad=true;
break;
case "e":
case "c":
var d=_14d.getDay();
if(l<2){
s=(d-_146.getFirstDayOfWeek(_14f.locale)+8)%7;
break;
}
case "E":
d=_14d.getDay();
if(l<3){
s=d+1;
pad=true;
}else{
var _155=["days",c=="c"?"standAlone":"format",_152[l-3]].join("-");
s=_14e[_155][d];
}
break;
case "a":
var _156=_14d.getHours()<12?"am":"pm";
s=_14f[_156]||_14e["dayPeriods-format-wide-"+_156];
break;
case "h":
case "H":
case "K":
case "k":
var h=_14d.getHours();
switch(c){
case "h":
s=(h%12)||12;
break;
case "H":
s=h;
break;
case "K":
s=(h%12);
break;
case "k":
s=h||24;
break;
}
pad=true;
break;
case "m":
s=_14d.getMinutes();
pad=true;
break;
case "s":
s=_14d.getSeconds();
pad=true;
break;
case "S":
s=Math.round(_14d.getMilliseconds()*Math.pow(10,l-3));
pad=true;
break;
case "v":
case "z":
s=_14b._getZone(_14d,true,_14f);
if(s){
break;
}
l=4;
case "Z":
var _157=_14b._getZone(_14d,false,_14f);
var tz=[(_157<=0?"+":"-"),_148.pad(Math.floor(Math.abs(_157)/60),2),_148.pad(Math.abs(_157)%60,2)];
if(l==4){
tz.splice(0,0,"GMT");
tz.splice(3,0,":");
}
s=tz.join("");
break;
default:
throw new Error("dojo.date.locale.format: invalid pattern char: "+_150);
}
if(pad){
s=_148.pad(s,l);
}
return s;
});
};
_14b._getZone=function(_158,_159,_15a){
if(_159){
return date.getTimezoneName(_158);
}else{
return _158.getTimezoneOffset();
}
};
_14b.format=function(_15b,_15c){
_15c=_15c||{};
var _15d=i18n.normalizeLocale(_15c.locale),_15e=_15c.formatLength||"short",_15f=_14b._getGregorianBundle(_15d),str=[],_160=lang.hitch(this,_14c,_15b,_15f,_15c);
if(_15c.selector=="year"){
return _161(_15f["dateFormatItem-yyyy"]||"yyyy",_160);
}
var _162;
if(_15c.selector!="date"){
_162=_15c.timePattern||_15f["timeFormat-"+_15e];
if(_162){
str.push(_161(_162,_160));
}
}
if(_15c.selector!="time"){
_162=_15c.datePattern||_15f["dateFormat-"+_15e];
if(_162){
str.push(_161(_162,_160));
}
}
return str.length==1?str[0]:_15f["dateTimeFormat-"+_15e].replace(/\'/g,"").replace(/\{(\d+)\}/g,function(_163,key){
return str[key];
});
};
_14b.regexp=function(_164){
return _14b._parseInfo(_164).regexp;
};
_14b._parseInfo=function(_165){
_165=_165||{};
var _166=i18n.normalizeLocale(_165.locale),_167=_14b._getGregorianBundle(_166),_168=_165.formatLength||"short",_169=_165.datePattern||_167["dateFormat-"+_168],_16a=_165.timePattern||_167["timeFormat-"+_168],_16b;
if(_165.selector=="date"){
_16b=_169;
}else{
if(_165.selector=="time"){
_16b=_16a;
}else{
_16b=_167["dateTimeFormat-"+_168].replace(/\{(\d+)\}/g,function(_16c,key){
return [_16a,_169][key];
});
}
}
var _16d=[],re=_161(_16b,lang.hitch(this,_16e,_16d,_167,_165));
return {regexp:re,tokens:_16d,bundle:_167};
};
_14b.parse=function(_16f,_170){
var _171=/[\u200E\u200F\u202A\u202E]/g,info=_14b._parseInfo(_170),_172=info.tokens,_173=info.bundle,re=new RegExp("^"+info.regexp.replace(_171,"")+"$",info.strict?"":"i"),_174=re.exec(_16f&&_16f.replace(_171,""));
if(!_174){
return null;
}
var _175=["abbr","wide","narrow"],_176=[1970,0,1,0,0,0,0],amPm="",_177=_145.every(_174,function(v,i){
if(!i){
return true;
}
var _178=_172[i-1],l=_178.length,c=_178.charAt(0);
switch(c){
case "y":
if(l!=2&&_170.strict){
_176[0]=v;
}else{
if(v<100){
v=Number(v);
var year=""+new Date().getFullYear(),_179=year.substring(0,2)*100,_17a=Math.min(Number(year.substring(2,4))+20,99);
_176[0]=(v<_17a)?_179+v:_179-100+v;
}else{
if(_170.strict){
return false;
}
_176[0]=v;
}
}
break;
case "M":
case "L":
if(l>2){
var _17b=_173["months-"+(c=="L"?"standAlone":"format")+"-"+_175[l-3]].concat();
if(!_170.strict){
v=v.replace(".","").toLowerCase();
_17b=_145.map(_17b,function(s){
return s.replace(".","").toLowerCase();
});
}
v=_145.indexOf(_17b,v);
if(v==-1){
return false;
}
}else{
v--;
}
_176[1]=v;
break;
case "E":
case "e":
case "c":
var days=_173["days-"+(c=="c"?"standAlone":"format")+"-"+_175[l-3]].concat();
if(!_170.strict){
v=v.toLowerCase();
days=_145.map(days,function(d){
return d.toLowerCase();
});
}
v=_145.indexOf(days,v);
if(v==-1){
return false;
}
break;
case "D":
_176[1]=0;
case "d":
_176[2]=v;
break;
case "a":
var am=_170.am||_173["dayPeriods-format-wide-am"],pm=_170.pm||_173["dayPeriods-format-wide-pm"];
if(!_170.strict){
var _17c=/\./g;
v=v.replace(_17c,"").toLowerCase();
am=am.replace(_17c,"").toLowerCase();
pm=pm.replace(_17c,"").toLowerCase();
}
if(_170.strict&&v!=am&&v!=pm){
return false;
}
amPm=(v==pm)?"p":(v==am)?"a":"";
break;
case "K":
if(v==24){
v=0;
}
case "h":
case "H":
case "k":
if(v>23){
return false;
}
_176[3]=v;
break;
case "m":
_176[4]=v;
break;
case "s":
_176[5]=v;
break;
case "S":
_176[6]=v;
}
return true;
});
var _17d=+_176[3];
if(amPm==="p"&&_17d<12){
_176[3]=_17d+12;
}else{
if(amPm==="a"&&_17d==12){
_176[3]=0;
}
}
var _17e=new Date(_176[0],_176[1],_176[2],_176[3],_176[4],_176[5],_176[6]);
if(_170.strict){
_17e.setFullYear(_176[0]);
}
var _17f=_172.join(""),_180=_17f.indexOf("d")!=-1,_181=_17f.indexOf("M")!=-1;
if(!_177||(_181&&_17e.getMonth()>_176[1])||(_180&&_17e.getDate()>_176[2])){
return null;
}
if((_181&&_17e.getMonth()<_176[1])||(_180&&_17e.getDate()<_176[2])){
_17e=date.add(_17e,"hour",1);
}
return _17e;
};
function _161(_182,_183,_184,_185){
var _186=function(x){
return x;
};
_183=_183||_186;
_184=_184||_186;
_185=_185||_186;
var _187=_182.match(/(''|[^'])+/g),_188=_182.charAt(0)=="'";
_145.forEach(_187,function(_189,i){
if(!_189){
_187[i]="";
}else{
_187[i]=(_188?_184:_183)(_189.replace(/''/g,"'"));
_188=!_188;
}
});
return _185(_187.join(""));
};
function _16e(_18a,_18b,_18c,_18d){
_18d=_147.escapeString(_18d);
if(!_18c.strict){
_18d=_18d.replace(" a"," ?a");
}
return _18d.replace(/([a-z])\1*/ig,function(_18e){
var s,c=_18e.charAt(0),l=_18e.length,p2="",p3="";
if(_18c.strict){
if(l>1){
p2="0"+"{"+(l-1)+"}";
}
if(l>2){
p3="0"+"{"+(l-2)+"}";
}
}else{
p2="0?";
p3="0{0,2}";
}
switch(c){
case "y":
s="\\d{2,4}";
break;
case "M":
case "L":
s=(l>2)?"\\S+?":"1[0-2]|"+p2+"[1-9]";
break;
case "D":
s="[12][0-9][0-9]|3[0-5][0-9]|36[0-6]|"+p2+"[1-9][0-9]|"+p3+"[1-9]";
break;
case "d":
s="3[01]|[12]\\d|"+p2+"[1-9]";
break;
case "w":
s="[1-4][0-9]|5[0-3]|"+p2+"[1-9]";
break;
case "E":
case "e":
case "c":
s=".+?";
break;
case "h":
s="1[0-2]|"+p2+"[1-9]";
break;
case "k":
s="1[01]|"+p2+"\\d";
break;
case "H":
s="1\\d|2[0-3]|"+p2+"\\d";
break;
case "K":
s="1\\d|2[0-4]|"+p2+"[1-9]";
break;
case "m":
case "s":
s="[0-5]\\d";
break;
case "S":
s="\\d{"+l+"}";
break;
case "a":
var am=_18c.am||_18b["dayPeriods-format-wide-am"],pm=_18c.pm||_18b["dayPeriods-format-wide-pm"];
s=am+"|"+pm;
if(!_18c.strict){
if(am!=am.toLowerCase()){
s+="|"+am.toLowerCase();
}
if(pm!=pm.toLowerCase()){
s+="|"+pm.toLowerCase();
}
if(s.indexOf(".")!=-1){
s+="|"+s.replace(/\./g,"");
}
}
s=s.replace(/\./g,"\\.");
break;
default:
s=".*";
}
if(_18a){
_18a.push(_18e);
}
return "("+s+")";
}).replace(/[\xa0 ]/g,"[\\s\\xa0]");
};
var _18f=[];
var _190={};
_14b.addCustomFormats=function(_191,_192){
_18f.push({pkg:_191,name:_192});
_190={};
};
_14b._getGregorianBundle=function(_193){
if(_190[_193]){
return _190[_193];
}
var _194={};
_145.forEach(_18f,function(desc){
var _195=i18n.getLocalization(desc.pkg,desc.name,_193);
_194=lang.mixin(_194,_195);
},this);
return _190[_193]=_194;
};
_14b.addCustomFormats(_14a.id.replace(/\/date\/locale$/,".cldr"),"gregorian");
_14b.getNames=function(item,type,_196,_197){
var _198,_199=_14b._getGregorianBundle(_197),_19a=[item,_196,type];
if(_196=="standAlone"){
var key=_19a.join("-");
_198=_199[key];
if(_198[0]==1){
_198=undefined;
}
}
_19a[1]="format";
return (_198||_199[_19a.join("-")]).concat();
};
_14b.isWeekend=function(_19b,_19c){
var _19d=_146.getWeekend(_19c),day=(_19b||new Date()).getDay();
if(_19d.end<_19d.start){
_19d.end+=7;
if(day<_19d.start){
day+=7;
}
}
return day>=_19d.start&&day<=_19d.end;
};
_14b._getDayOfYear=function(_19e){
return date.difference(new Date(_19e.getFullYear(),0,1,_19e.getHours()),_19e)+1;
};
_14b._getWeekOfYear=function(_19f,_1a0){
if(arguments.length==1){
_1a0=0;
}
var _1a1=new Date(_19f.getFullYear(),0,1).getDay(),adj=(_1a1-_1a0+7)%7,week=Math.floor((_14b._getDayOfYear(_19f)+adj-1)/7);
if(_1a1==_1a0){
week++;
}
return week;
};
return _14b;
});
},"curam/widget/AppBannerComboBoxMixin":function(){
define(["dijit/form/ComboBoxMixin","dojo/_base/declare","dojo/_base/lang","dojo/text!curam/widget/templates/AppBannerDropDownBox.html"],function(_1a2,_1a3,lang,_1a4){
var _1a5=_1a3("curam.widget.AppBannerComboBoxMixin",_1a2,{templateString:_1a4});
return _1a5;
});
},"dijit/_Templated":function(){
define(["./_WidgetBase","./_TemplatedMixin","./_WidgetsInTemplateMixin","dojo/_base/array","dojo/_base/declare","dojo/_base/lang","dojo/_base/kernel"],function(_1a6,_1a7,_1a8,_1a9,_1aa,lang,_1ab){
lang.extend(_1a6,{waiRole:"",waiState:""});
return _1aa("dijit._Templated",[_1a7,_1a8],{widgetsInTemplate:false,constructor:function(){
_1ab.deprecated(this.declaredClass+": dijit._Templated deprecated, use dijit._TemplatedMixin and if necessary dijit._WidgetsInTemplateMixin","","2.0");
},_processNode:function(_1ac,_1ad){
var ret=this.inherited(arguments);
var role=_1ad(_1ac,"waiRole");
if(role){
_1ac.setAttribute("role",role);
}
var _1ae=_1ad(_1ac,"waiState");
if(_1ae){
_1a9.forEach(_1ae.split(/\s*,\s*/),function(_1af){
if(_1af.indexOf("-")!=-1){
var pair=_1af.split("-");
_1ac.setAttribute("aria-"+pair[0],pair[1]);
}
});
}
return ret;
}});
});
},"dojo/require":function(){
define(["./_base/loader"],function(_1b0){
return {dynamic:0,normalize:function(id){
return id;
},load:_1b0.require};
});
},"curam/cdsl/store/IdentityApi":function(){
define(["dojo/_base/declare","dojo/json"],function(_1b1,json){
var _1b2=_1b1(null,{getIdentity:function(item){
var _1b3=this.getIdentityPropertyNames()[0];
if(typeof item[_1b3]==="object"){
throw new Error("Complex identity attributes are not supported by this implementation.");
}
return item[_1b3];
},parseIdentity:function(_1b4){
var _1b5={};
_1b5[this.getIdentityPropertyNames()[0]]=_1b4;
return _1b5;
},getIdentityPropertyNames:function(){
return ["id"];
}});
return _1b2;
});
},"dijit/MenuSeparator":function(){
define(["dojo/_base/declare","dojo/dom","./_WidgetBase","./_TemplatedMixin","./_Contained","dojo/text!./templates/MenuSeparator.html"],function(_1b6,dom,_1b7,_1b8,_1b9,_1ba){
return _1b6("dijit.MenuSeparator",[_1b7,_1b8,_1b9],{templateString:_1ba,buildRendering:function(){
this.inherited(arguments);
dom.setSelectable(this.domNode,false);
},isFocusable:function(){
return false;
}});
});
},"dijit/form/ToggleButton":function(){
define(["dojo/_base/declare","dojo/_base/kernel","./Button","./_ToggleButtonMixin"],function(_1bb,_1bc,_1bd,_1be){
return _1bb("dijit.form.ToggleButton",[_1bd,_1be],{baseClass:"dijitToggleButton",setChecked:function(_1bf){
_1bc.deprecated("setChecked("+_1bf+") is deprecated. Use set('checked',"+_1bf+") instead.","","2.0");
this.set("checked",_1bf);
}});
});
},"curam/util/Dialog":function(){
define(["curam/util","curam/define","curam/dialog","dojo/on","curam/util/onLoad","curam/debug"],function(util,_1c0,_1c1,on,_1c2,_1c3){
_1c0.singleton("curam.util.Dialog",{_id:null,_unsubscribes:[],_modalWithIEGScript:false,open:function(path,_1c4,_1c5){
var url=path+curam.util.makeQueryString(_1c4);
var _1c6={href:url};
var _1c7=null;
if(_1c5){
_1c7="width="+_1c5.width+",height="+_1c5.height;
}
window.jsModals=true;
curam.util.openModalDialog(_1c6,_1c7);
},init:function(){
var _1c8=curam.util.getTopmostWindow();
var _1c9=_1c8.dojo.subscribe("/curam/dialog/SetId",null,function(_1ca){
curam.util.Dialog._id=_1ca;
curam.debug.log(_1c3.getProperty("curam.util.Dialog.id.success"),curam.util.Dialog._id);
_1c8.dojo.unsubscribe(_1c9);
});
curam.util.Dialog._unsubscribes.push(_1c9);
_1c8.dojo.publish("/curam/dialog/init");
if(!curam.util.Dialog._id){
curam.debug.log(_1c3.getProperty("curam.util.Dialog.id.fail"));
}
if(curam.util.Dialog._modalWithIEGScript){
dojo.addOnUnload(function(){
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}else{
var _1cb=on(window,"unload",function(){
_1cb.remove();
curam.util.Dialog._releaseHandlers();
window.parent.dojo.publish("/curam/dialog/iframeUnloaded",[curam.util.Dialog._id,window]);
});
}
},initModalWithIEGScript:function(){
curam.util.Dialog._modalWithIEGScript=true;
curam.util.Dialog.init();
},registerGetTitleFunc:function(_1cc){
curam.util.onLoad.addPublisher(function(_1cd){
_1cd.title=_1cc();
});
},registerGetSizeFunc:function(_1ce){
curam.util.onLoad.addPublisher(function(_1cf){
_1cf.windowOptions=_1ce();
});
},registerAfterDisplayHandler:function(_1d0){
var _1d1=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_1d1.dojo.subscribe("/curam/dialog/AfterDisplay",null,function(_1d2){
if(_1d2==curam.util.Dialog._id){
_1d0();
}
}));
},registerBeforeCloseHandler:function(_1d3){
var _1d4=curam.util.getTopmostWindow();
curam.util.Dialog._unsubscribes.push(_1d4.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_1d5){
if(_1d5===curam.util.Dialog._id){
_1d3();
}
}));
},pageLoadFinished:function(){
var _1d6=curam.util.getTopmostWindow();
curam.util.Dialog._unsTokenReleaseHandlers=_1d6.dojo.subscribe("/curam/dialog/BeforeClose",null,function(_1d7){
if(_1d7==curam.util.Dialog._id){
curam.util.Dialog._releaseHandlers(_1d6);
}
});
curam.util.onLoad.execute();
},_releaseHandlers:function(_1d8){
var _1d9;
if(_1d8){
_1d9=_1d8;
}else{
_1d9=curam.util.getTopmostWindow();
}
dojo.forEach(curam.util.Dialog._unsubscribes,_1d9.dojo.unsubscribe);
curam.util.Dialog._unsubscribes=[];
_1d9.dojo.unsubscribe(curam.util.Dialog._unsTokenReleaseHandlers);
curam.util.Dialog._unsTokenReleaseHandlers=null;
},close:function(_1da,_1db,_1dc){
var _1dd=curam.dialog.getParentWindow(window);
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _1de=window.frameElement.id;
var _1df=_1de.substring(7);
curam.util.Dialog._id=_1df;
_1c3.log("curam.util.Dialog.closeAndSubmitParent() "+_1c3.getProperty("curam.dialog.modal.id")+_1df);
}
if(_1da&&!_1db){
curam.dialog.forceParentRefresh();
_1dd.curam.util.redirectWindow(null);
}else{
if(_1db){
var _1e0=_1db;
if(_1db.indexOf("Page.do")==-1&&_1db.indexOf("Action.do")==-1){
_1e0=_1db+"Page.do"+curam.util.makeQueryString(_1dc);
}
_1dd.curam.util.redirectWindow(_1e0);
}
}
var _1e1=curam.util.getTopmostWindow();
_1e1.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},closeAndSubmitParent:function(_1e2){
var _1e3=curam.dialog.getParentWindow(window);
var _1e4=_1e3.document.forms["mainForm"];
var _1e5=curam.util.getTopmostWindow();
if(typeof (curam.util.Dialog._id)==="undefined"||curam.util.Dialog._id==null){
var _1e6=window.frameElement.id;
var _1e7=_1e6.substring(7);
curam.util.Dialog._id=_1e7;
_1c3.log("curam.util.Dialog.closeAndSubmitParent() "+_1c3.getProperty("curam.dialog.modal.id")+_1e7);
}
if(_1e4==null||_1e4==undefined){
_1e5.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
return;
}
var _1e8=function(_1e9){
for(var _1ea in _1e9){
if(_1e9.hasOwnProperty(_1ea)){
return false;
}
}
return true;
};
if(_1e2&&!_1e8(_1e2)){
var _1eb=dojo.query("#"+_1e4.id+" > "+"input[type=text]");
var _1ec=dojo.filter(_1eb,function(node){
return node.readOnly==false;
});
dojo.forEach(_1ec,function(node){
node.value="";
});
for(var _1ed in _1e2){
var _1ee=_1ec[parseInt(_1ed)];
if(_1ee){
var _1ef=dojo.query("input[name="+_1ee.id+"]",_1e4)[0];
if(_1ef){
_1ef.value=_1e2[_1ed];
}else{
_1ee.value=_1e2[_1ed];
}
}
}
}else{
}
_1e3.dojo.publish("/curam/page/refresh");
_1e4.submit();
_1e5.dojo.publish("/curam/dialog/close",[curam.util.Dialog._id]);
},fileDownloadAnchorHandler:function(url){
return curam.util.fileDownloadAnchorHandler(url);
}});
});
},"dijit/CheckedMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-class","./MenuItem","dojo/text!./templates/CheckedMenuItem.html","./hccss"],function(_1f0,_1f1,_1f2,_1f3){
return _1f0("dijit.CheckedMenuItem",_1f2,{baseClass:"dijitMenuItem dijitCheckedMenuItem",templateString:_1f3,checked:false,_setCheckedAttr:function(_1f4){
this.domNode.setAttribute("aria-checked",_1f4?"true":"false");
this._set("checked",_1f4);
},iconClass:"",role:"menuitemcheckbox",checkedChar:"&#10003;",onChange:function(){
},_onClick:function(evt){
if(!this.disabled){
this.set("checked",!this.checked);
this.onChange(this.checked);
}
this.onClick(evt);
}});
});
},"dojox/html/_base":function(){
define(["dojo/_base/declare","dojo/Deferred","dojo/dom-construct","dojo/html","dojo/_base/kernel","dojo/_base/lang","dojo/ready","dojo/_base/sniff","dojo/_base/url","dojo/_base/xhr","dojo/when","dojo/_base/window"],function(_1f5,_1f6,_1f7,_1f8,_1f9,lang,_1fa,has,_1fb,_1fc,when,_1fd){
var html=_1f9.getObject("dojox.html",true);
if(has("ie")){
var _1fe=/(AlphaImageLoader\([^)]*?src=(['"]))(?![a-z]+:|\/)([^\r\n;}]+?)(\2[^)]*\)\s*[;}]?)/g;
}
var _1ff=/(?:(?:@import\s*(['"])(?![a-z]+:|\/)([^\r\n;{]+?)\1)|url\(\s*(['"]?)(?![a-z]+:|\/)([^\r\n;]+?)\3\s*\))([a-z, \s]*[;}]?)/g;
var _200=html._adjustCssPaths=function(_201,_202){
if(!_202||!_201){
return;
}
if(_1fe){
_202=_202.replace(_1fe,function(_203,pre,_204,url,post){
return pre+(new _1fb(_201,"./"+url).toString())+post;
});
}
return _202.replace(_1ff,function(_205,_206,_207,_208,_209,_20a){
if(_207){
return "@import \""+(new _1fb(_201,"./"+_207).toString())+"\""+_20a;
}else{
return "url("+(new _1fb(_201,"./"+_209).toString())+")"+_20a;
}
});
};
var _20b=/(<[a-z][a-z0-9]*\s[^>]*)(?:(href|src)=(['"]?)([^>]*?)\3|style=(['"]?)([^>]*?)\5)([^>]*>)/gi;
var _20c=html._adjustHtmlPaths=function(_20d,cont){
var url=_20d||"./";
return cont.replace(_20b,function(tag,_20e,name,_20f,_210,_211,_212,end){
return _20e+(name?(name+"="+_20f+(new _1fb(url,_210).toString())+_20f):("style="+_211+_200(url,_212)+_211))+end;
});
};
var _213=html._snarfStyles=function(_214,cont,_215){
_215.attributes=[];
cont=cont.replace(/<[!][-][-](.|\s)*?[-][-]>/g,function(_216){
return _216.replace(/<(\/?)style\b/ig,"&lt;$1Style").replace(/<(\/?)link\b/ig,"&lt;$1Link").replace(/@import "/ig,"@ import \"");
});
return cont.replace(/(?:<style([^>]*)>([\s\S]*?)<\/style>|<link\s+(?=[^>]*rel=['"]?stylesheet)([^>]*?href=(['"])([^>]*?)\4[^>\/]*)\/?>)/gi,function(_217,_218,_219,_21a,_21b,href){
var i,attr=(_218||_21a||"").replace(/^\s*([\s\S]*?)\s*$/i,"$1");
if(_219){
i=_215.push(_214?_200(_214,_219):_219);
}else{
i=_215.push("@import \""+href+"\";");
attr=attr.replace(/\s*(?:rel|href)=(['"])?[^\s]*\1\s*/gi,"");
}
if(attr){
attr=attr.split(/\s+/);
var _21c={},tmp;
for(var j=0,e=attr.length;j<e;j++){
tmp=attr[j].split("=");
_21c[tmp[0]]=tmp[1].replace(/^\s*['"]?([\s\S]*?)['"]?\s*$/,"$1");
}
_215.attributes[i-1]=_21c;
}
return "";
});
};
var _21d=html._snarfScripts=function(cont,_21e){
_21e.code="";
cont=cont.replace(/<[!][-][-](.|\s)*?[-][-]>/g,function(_21f){
return _21f.replace(/<(\/?)script\b/ig,"&lt;$1Script");
});
function _220(src){
if(_21e.downloadRemote){
src=src.replace(/&([a-z0-9#]+);/g,function(m,name){
switch(name){
case "amp":
return "&";
case "gt":
return ">";
case "lt":
return "<";
default:
return name.charAt(0)=="#"?String.fromCharCode(name.substring(1)):"&"+name+";";
}
});
_1fc.get({url:src,sync:true,load:function(code){
if(_21e.code!==""){
code="\n"+code;
}
_21e.code+=code+";";
},error:_21e.errBack});
}
};
return cont.replace(/<script\s*(?![^>]*type=['"]?(?:dojo\/|text\/html\b))[^>]*?(?:src=(['"]?)([^>]*?)\1[^>]*)?>([\s\S]*?)<\/script>/gi,function(_221,_222,src,code){
if(src){
_220(src);
}else{
if(_21e.code!==""){
code="\n"+code;
}
_21e.code+=code+";";
}
return "";
});
};
var _223=html.evalInGlobal=function(code,_224){
_224=_224||_1fd.doc.body;
var n=_224.ownerDocument.createElement("script");
n.type="text/javascript";
_224.appendChild(n);
n.text=code;
};
html._ContentSetter=_1f5(_1f8._ContentSetter,{adjustPaths:false,referencePath:".",renderStyles:false,executeScripts:false,scriptHasHooks:false,scriptHookReplacement:null,_renderStyles:function(_225){
this._styleNodes=[];
var st,att,_226,doc=this.node.ownerDocument;
var head=doc.getElementsByTagName("head")[0];
for(var i=0,e=_225.length;i<e;i++){
_226=_225[i];
att=_225.attributes[i];
st=doc.createElement("style");
st.setAttribute("type","text/css");
for(var x in att){
st.setAttribute(x,att[x]);
}
this._styleNodes.push(st);
head.appendChild(st);
if(st.styleSheet){
st.styleSheet.cssText=_226;
}else{
st.appendChild(doc.createTextNode(_226));
}
}
},empty:function(){
this.inherited("empty",arguments);
this._styles=[];
},onBegin:function(){
this.inherited("onBegin",arguments);
var cont=this.content,node=this.node;
var _227=this._styles;
if(lang.isString(cont)){
if(this.adjustPaths&&this.referencePath){
cont=_20c(this.referencePath,cont);
}
if(this.renderStyles||this.cleanContent){
cont=_213(this.referencePath,cont,_227);
}
if(this.executeScripts){
var _228=this;
var _229={downloadRemote:true,errBack:function(e){
_228._onError.call(_228,"Exec","Error downloading remote script in \""+_228.id+"\"",e);
}};
cont=_21d(cont,_229);
this._code=_229.code;
}
}
this.content=cont;
},onEnd:function(){
var code=this._code,_22a=this._styles;
if(this._styleNodes&&this._styleNodes.length){
while(this._styleNodes.length){
_1f7.destroy(this._styleNodes.pop());
}
}
if(this.renderStyles&&_22a&&_22a.length){
this._renderStyles(_22a);
}
var d=new _1f6();
var _22b=this.getInherited(arguments),args=arguments,_22c=lang.hitch(this,function(){
_22b.apply(this,args);
when(this.parseDeferred,function(){
d.resolve();
});
});
if(this.executeScripts&&code){
if(this.cleanContent){
code=code.replace(/(<!--|(?:\/\/)?-->|<!\[CDATA\[|\]\]>)/g,"");
}
if(this.scriptHasHooks){
code=code.replace(/_container_(?!\s*=[^=])/g,this.scriptHookReplacement);
}
try{
_223(code,this.node);
}
catch(e){
this._onError("Exec","Error eval script in "+this.id+", "+e.message,e);
}
_1fa(_22c);
}else{
_22c();
}
return d.promise;
},tearDown:function(){
this.inherited(arguments);
delete this._styles;
if(this._styleNodes&&this._styleNodes.length){
while(this._styleNodes.length){
_1f7.destroy(this._styleNodes.pop());
}
}
delete this._styleNodes;
lang.mixin(this,html._ContentSetter.prototype);
}});
html.set=function(node,cont,_22d){
if(!_22d){
return _1f8._setNodeContent(node,cont,true);
}else{
var op=new html._ContentSetter(lang.mixin(_22d,{content:cont,node:node}));
return op.set();
}
};
return html;
});
},"curam/widget/_HasDropDown":function(){
define(["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","dijit/registry","dijit/focus","dijit/popup","dijit/_FocusMixin","dijit/a11y","dijit/_HasDropDown","curam/util"],function(_22e,_22f,dom,_230,_231,_232,_233,has,keys,lang,on,_234,_235,_236,_237,_238,a11y){
return _22e("curam.widget._HasDropDown",[dijit._HasDropDown],{openDropDown:function(){
var _239=this.dropDown,_23a=_239.domNode,_23b=this._aroundNode||this.domNode,self=this;
var _23c=_237.open({parent:this,popup:_239,around:_23b,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
self.closeDropDown(true);
},onCancel:function(evt){
self.closeDropDown(evt,true);
},onClose:function(){
_230.set(self._popupStateNode,"popupActive",false);
_231.remove(self._popupStateNode,"dijitHasDropDownOpen");
self._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_23b.offsetWidth>_239._popupWrapper.offsetWidth)){
var _23d=_23b.offsetWidth-_239._popupWrapper.offsetWidth;
var _23e={w:_239.domNode.offsetWidth+_23d};
this._origStyle=_23a.style.cssText;
if(lang.isFunction(_239.resize)){
_239.resize(_23e);
}else{
_232.setMarginBox(_23a,_23e);
}
if(_23c.corner[1]=="R"){
_239._popupWrapper.style.left=(_239._popupWrapper.style.left.replace("px","")-_23d)+"px";
}
}
_230.set(this._popupStateNode,"popupActive","true");
_231.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_239.id);
if(_23a.getAttribute("role")!=="presentation"&&!_23a.getAttribute("aria-labelledby")){
_23a.setAttribute("aria-labelledby",this.id);
}
return _23c;
},closeDropDown:function(evt,_23f){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(_23f&&this.focus){
var _240=this._getNextFocusableNode(evt,this.ownerDocument,this.focusNode);
if(_240.focus){
_240.focus();
}else{
this.focus();
}
}
_237.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
},_getNextFocusableNode:function(evt,_241,_242){
var _243=_242;
var _244=curam.util.getTopmostWindow();
if(evt&&evt.keyCode==keys.TAB){
var _245=this._findFocusableElementsInDocument(_241);
var _246=_245.length;
var _247=_245.indexOf(_242);
if(_247>-1){
if(!evt.shiftKey){
_247++;
var _248=null;
for(var i=_247;i<_245.length;i++){
if(_245[i].offsetParent!==null&&!_231.contains(_245[i],"dijitMenuItem")){
_248=_245[i];
break;
}
}
if(_248&&_248.nodeName.toLowerCase()!=="iframe"){
_243=_248;
}else{
if(_248&&_248.nodeName.toLowerCase()==="iframe"){
_248=this._findFocusableElementInIframe(_248,evt.shiftKey);
if(_248){
_243=_248;
}
}else{
if(this.ownerDocument!==_244.document){
var _249=this.ownerDocument;
while(_249!==_244.document&&!_248){
var _24a=window.parent;
var _24b=_24a.document||_24a.contentDocument||_24a.contentWindow.document;
_248=this._findFocusableElementInParentDocument(_24b,evt.shiftKey);
_249=_24b;
}
if(_248){
_243=_248;
}
}
}
}
}else{
_247--;
var _24c=null;
for(var i=_247;i>-1;i--){
if(_245[i].offsetParent!==null){
_24c=_245[i];
break;
}
}
if(_24c&&_24c.nodeName.toLowerCase()!=="iframe"){
_243=_24c;
}else{
if(_24c&&_24c.nodeName.toLowerCase()==="iframe"){
_24c=this._findFocusableElementInIframe(_24c,evt.shiftKey);
if(_24c){
_243=_24c;
}
}else{
if(this.ownerDocument!==_244.document){
var _249=this.ownerDocument;
while(_249!==_244.document&&!_24c){
var _24a=window.parent;
var _24b=_24a.document||_24a.contentDocument||_24a.contentWindow.document;
_24c=this._findFocusableElementInParentDocument(_24b,evt.shiftKey);
_249=_24b;
}
if(_24c){
_243=_24c;
}
}
}
}
}
}
}
return _243;
},_findFocusableElementsInDocument:function(_24d){
var _24e=_24d.querySelectorAll("button:not(.dijitTabCloseButton), [href], input, select, object, iframe, area, textarea, [tabindex]:not([tabindex=\"-1\"])");
var _24f=[];
for(var i=0;i<_24e.length;i++){
if(a11y._isElementShown(_24e[i])){
if(a11y.isTabNavigable(_24e[i])){
_24f.push(_24e[i]);
}else{
if(_24e[i].nodeName.toLowerCase()==="iframe"){
_24f.push(_24e[i]);
}
}
}
}
return _24f;
},_findFocusableElementInParentDocument:function(_250,_251){
var _252=this._findFocusableElementsInDocument(_250);
var _253=false;
var _254=0;
var _255=null;
if(!_251){
for(var i=0;i<_252.length;i++){
if(_252[i].nodeName.toLowerCase()==="iframe"){
if(_252[i].contentDocument===this.ownerDocument){
_253=true;
_254=i;
break;
}
}
}
}else{
for(var i=_252.length-1;i>-1;i--){
if(_252[i].nodeName.toLowerCase()==="iframe"){
if(_252[i].contentDocument===this.ownerDocument){
_253=true;
_254=i;
break;
}
}
}
}
if(_253){
if(!_251){
_254++;
for(var i=_254;i<_252.length;i++){
if(_252[i].offsetParent!==null){
_255=_252[i];
break;
}
}
}else{
_254--;
for(var i=_254;i>-1;i--){
if(_252[i].offsetParent!==null){
_255=_252[i];
break;
}
}
}
if(_255&&_255.nodeName.toLowerCase()==="iframe"){
_255=this._findFocusableElementInIframe(_255,_251);
}
}
return _255;
},_findFocusableElementInIframe:function(node,_256){
var _257=node;
while(_257&&_257.nodeName.toLowerCase()==="iframe"){
var _258=_257.contentDocument||_257.contentWindow.document;
var _259=this._findFocusableElementsInDocument(_258);
if(_259.length>0){
if(!_256){
indexOfNode=0;
for(var i=indexOfNode;i<_259.length;i++){
if(_259[i].offsetParent!==null){
_257=_259[i];
break;
}
}
}else{
indexOfNode=_259.length-1;
for(var i=indexOfNode;i>-1;i--){
if(_259[i].offsetParent!==null){
_257=_259[i];
break;
}
}
}
}else{
_257=null;
}
}
return _257;
}});
});
},"dijit/_DialogMixin":function(){
define(["dojo/_base/declare","./a11y"],function(_25a,a11y){
return _25a("dijit._DialogMixin",null,{actionBarTemplate:"",execute:function(){
},onCancel:function(){
},onExecute:function(){
},_onSubmit:function(){
this.onExecute();
this.execute(this.get("value"));
},_getFocusItems:function(){
var _25b=a11y._getTabNavigable(this.domNode);
this._firstFocusItem=_25b.lowest||_25b.first||this.closeButtonNode||this.domNode;
this._lastFocusItem=_25b.last||_25b.highest||this._firstFocusItem;
}});
});
},"curam/widget/FilteringSelect":function(){
define(["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/dom-construct","dojo/keys","dojo/sniff","dijit/form/FilteringSelect","curam/widget/ComboBoxMixin"],function(_25c,_25d,on,dom,_25e,keys,has){
var _25f=_25d("curam.widget.FilteringSelect",[dijit.form.FilteringSelect,curam.widget.ComboBoxMixin],{enterKeyOnOpenDropDown:false,required:false,postMixInProperties:function(){
if(!this.store){
if(dojo.query("> option",this.srcNodeRef)[0]==undefined){
_25e.create("option",{innerHTML:"<!--__o3_BLANK-->"},this.srcNodeRef);
}
}
if(!this.get("store")&&this.srcNodeRef.value==""){
var _260=this.srcNodeRef,_261=dojo.query("> option[value='']",_260);
if(_261.length&&_261[0].innerHTML!="<!--__o3_BLANK-->"){
this.displayedValue=dojo.trim(_261[0].innerHTML);
}
}
this.inherited(arguments);
},postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _262=_25c.byNode(dom.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_262._opened){
_262.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
},startup:function(){
if(has("trident")){
this.domNode.setAttribute("role","listbox");
}
this.inherited(arguments);
},_callbackSetLabel:function(_263,_264,_265,_266){
if((_264&&_264[this.searchAttr]!==this._lastQuery)||(!_264&&_263.length&&this.get("store").getIdentity(_263[0])!=this._lastQuery)){
return;
}
if(!_263.length){
this.set("value","__o3_INVALID",_266||(_266===undefined&&!this.focused),this.textbox.value,null);
}else{
this.set("item",_263[0],_266);
}
},_onKey:function(evt){
if(evt.charCode>=32){
return;
}
var key=evt.charCode||evt.keyCode;
if(key==keys.ALT||key==keys.CTRL||key==keys.META||key==keys.SHIFT){
return;
}
var pw=this.dropDown;
var _267=null;
this._abortQuery();
this.inherited(arguments);
if(evt.altKey||evt.ctrlKey||evt.metaKey){
return;
}
if(this._opened){
_267=pw.getHighlightedOption();
}
switch(key){
case keys.PAGE_DOWN:
case keys.DOWN_ARROW:
case keys.PAGE_UP:
case keys.UP_ARROW:
if(this._opened){
this._announceOption(_267);
}
evt.stopPropagation();
evt.preventDefault();
break;
case keys.ENTER:
if(_267){
if(_267==pw.nextButton){
this._nextSearch(1);
evt.stopPropagation();
evt.preventDefault();
break;
}else{
if(_267==pw.previousButton){
this._nextSearch(-1);
evt.stopPropagation();
evt.preventDefault();
break;
}
}
evt.stopPropagation();
evt.preventDefault();
}else{
this._setBlurValue();
this._setCaretPos(this.focusNode,0);
var _268=this._resetValue;
var _269=this.displayedValue;
if(_268!=_269){
evt.stopPropagation();
evt.preventDefault();
}
}
case keys.TAB:
var _269=this.get("displayedValue");
if(pw&&(_269==pw._messages["previousMessage"]||_269==pw._messages["nextMessage"])){
break;
}
if(_267){
this._selectOption(_267);
}
case keys.ESCAPE:
if(this._opened){
this._lastQuery=null;
this.closeDropDown();
}
break;
}
},_selectOption:function(_26a){
this.closeDropDown();
if(_26a){
this._announceOption(_26a);
}
this._setCaretPos(this.focusNode,0);
this._handleOnChange(this.value,true);
this.focusNode.removeAttribute("aria-activedescendant");
}});
return _25f;
});
},"dijit/_Widget":function(){
define(["dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/ready","./registry","./_WidgetBase","./_OnDijitClickMixin","./_FocusMixin","dojo/uacss","./hccss"],function(_26b,_26c,_26d,_26e,has,_26f,lang,_270,_271,_272,_273,_274,_275){
function _276(){
};
function _277(_278){
return function(obj,_279,_27a,_27b){
if(obj&&typeof _279=="string"&&obj[_279]==_276){
return obj.on(_279.substring(2).toLowerCase(),lang.hitch(_27a,_27b));
}
return _278.apply(_26d,arguments);
};
};
_26b.around(_26d,"connect",_277);
if(_26f.connect){
_26b.around(_26f,"connect",_277);
}
var _27c=_26e("dijit._Widget",[_273,_274,_275],{onClick:_276,onDblClick:_276,onKeyDown:_276,onKeyPress:_276,onKeyUp:_276,onMouseDown:_276,onMouseMove:_276,onMouseOut:_276,onMouseOver:_276,onMouseLeave:_276,onMouseEnter:_276,onMouseUp:_276,constructor:function(_27d){
this._toConnect={};
for(var name in _27d){
if(this[name]===_276){
this._toConnect[name.replace(/^on/,"").toLowerCase()]=_27d[name];
delete _27d[name];
}
}
},postCreate:function(){
this.inherited(arguments);
for(var name in this._toConnect){
this.on(name,this._toConnect[name]);
}
delete this._toConnect;
},on:function(type,func){
if(this[this._onMap(type)]===_276){
return _26d.connect(this.domNode,type.toLowerCase(),this,func);
}
return this.inherited(arguments);
},_setFocusedAttr:function(val){
this._focused=val;
this._set("focused",val);
},setAttribute:function(attr,_27e){
_26f.deprecated(this.declaredClass+"::setAttribute(attr, value) is deprecated. Use set() instead.","","2.0");
this.set(attr,_27e);
},attr:function(name,_27f){
var args=arguments.length;
if(args>=2||typeof name==="object"){
return this.set.apply(this,arguments);
}else{
return this.get(name);
}
},getDescendants:function(){
_26f.deprecated(this.declaredClass+"::getDescendants() is deprecated. Use getChildren() instead.","","2.0");
return this.containerNode?_270("[widgetId]",this.containerNode).map(_272.byNode):[];
},_onShow:function(){
this.onShow();
},onShow:function(){
},onHide:function(){
},onClose:function(){
return true;
}});
if(1){
_271(0,function(){
var _280=["dijit/_base"];
require(_280);
});
}
return _27c;
});
},"dijit/form/_SearchMixin":function(){
define(["dojo/_base/declare","dojo/keys","dojo/_base/lang","dojo/query","dojo/string","dojo/when","../registry"],function(_281,keys,lang,_282,_283,when,_284){
return _281("dijit.form._SearchMixin",null,{pageSize:Infinity,store:null,fetchProperties:{},query:{},list:"",_setListAttr:function(list){
this._set("list",list);
},searchDelay:200,searchAttr:"name",queryExpr:"${0}*",ignoreCase:true,_patternToRegExp:function(_285){
return new RegExp("^"+_285.replace(/(\\.)|(\*)|(\?)|\W/g,function(str,_286,star,_287){
return star?".*":_287?".":_286?_286:"\\"+str;
})+"$",this.ignoreCase?"mi":"m");
},_abortQuery:function(){
if(this.searchTimer){
this.searchTimer=this.searchTimer.remove();
}
if(this._queryDeferHandle){
this._queryDeferHandle=this._queryDeferHandle.remove();
}
if(this._fetchHandle){
if(this._fetchHandle.abort){
this._cancelingQuery=true;
this._fetchHandle.abort();
this._cancelingQuery=false;
}
if(this._fetchHandle.cancel){
this._cancelingQuery=true;
this._fetchHandle.cancel();
this._cancelingQuery=false;
}
this._fetchHandle=null;
}
},_processInput:function(evt){
if(this.disabled||this.readOnly){
return;
}
var key=evt.charOrCode;
var _288=false;
this._prev_key_backspace=false;
switch(key){
case keys.DELETE:
case keys.BACKSPACE:
this._prev_key_backspace=true;
this._maskValidSubsetError=true;
_288=true;
break;
default:
_288=typeof key=="string"||key==229;
}
if(_288){
if(!this.store){
this.onSearch();
}else{
this.searchTimer=this.defer("_startSearchFromInput",1);
}
}
},onSearch:function(){
},_startSearchFromInput:function(){
this._startSearch(this.focusNode.value);
},_startSearch:function(text){
this._abortQuery();
var _289=this,_282=lang.clone(this.query),_28a={start:0,count:this.pageSize,queryOptions:{ignoreCase:this.ignoreCase,deep:true}},qs=_283.substitute(this.queryExpr,[text.replace(/([\\\*\?])/g,"\\$1")]),q,_28b=function(){
var _28c=_289._fetchHandle=_289.store.query(_282,_28a);
if(_289.disabled||_289.readOnly||(q!==_289._lastQuery)){
return;
}
when(_28c,function(res){
_289._fetchHandle=null;
if(!_289.disabled&&!_289.readOnly&&(q===_289._lastQuery)){
when(_28c.total,function(_28d){
res.total=_28d;
var _28e=_289.pageSize;
if(isNaN(_28e)||_28e>res.total){
_28e=res.total;
}
res.nextPage=function(_28f){
_28a.direction=_28f=_28f!==false;
_28a.count=_28e;
if(_28f){
_28a.start+=res.length;
if(_28a.start>=res.total){
_28a.count=0;
}
}else{
_28a.start-=_28e;
if(_28a.start<0){
_28a.count=Math.max(_28e+_28a.start,0);
_28a.start=0;
}
}
if(_28a.count<=0){
res.length=0;
_289.onSearch(res,_282,_28a);
}else{
_28b();
}
};
_289.onSearch(res,_282,_28a);
});
}
},function(err){
_289._fetchHandle=null;
if(!_289._cancelingQuery){
console.error(_289.declaredClass+" "+err.toString());
}
});
};
lang.mixin(_28a,this.fetchProperties);
if(this.store._oldAPI){
q=qs;
}else{
q=this._patternToRegExp(qs);
q.toString=function(){
return qs;
};
}
this._lastQuery=_282[this.searchAttr]=q;
this._queryDeferHandle=this.defer(_28b,this.searchDelay);
},constructor:function(){
this.query={};
this.fetchProperties={};
},postMixInProperties:function(){
if(!this.store){
var list=this.list;
if(list){
this.store=_284.byId(list);
}
}
this.inherited(arguments);
}});
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_290,_291,_292){
return _292("dijit.Destroyable",null,{destroy:function(_293){
this._destroyed=true;
},own:function(){
var _294=["destroyRecursive","destroy","remove"];
_290.forEach(arguments,function(_295){
var _296;
var odh=_291.before(this,"destroy",function(_297){
_295[_296](_297);
});
var hdhs=[];
function _298(){
odh.remove();
_290.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_295.then){
_296="cancel";
_295.then(_298,_298);
}else{
_290.forEach(_294,function(_299){
if(typeof _295[_299]==="function"){
if(!_296){
_296=_299;
}
hdhs.push(_291.after(_295,_299,_298,true));
}
});
}
},this);
return arguments;
}});
});
},"dojo/request":function(){
define(["./request/default!"],function(_29a){
return _29a;
});
},"curam/util/Dropdown":function(){
define(["dojo/_base/declare"],function(_29b){
var _29c=_29b("curam.util.Dropdown",null,{constructor:function(){
},updateDropdownItems:function(_29d,_29e,_29f){
if(_29e.constructor!==Array||_29f.constructor!==Array){
throw new Error("Both "+_29e+" and "+_29f+" must be an array");
}
if(_29e.length!=_29f.length){
throw new Error("Both "+_29e+" and "+_29f+" must have the same number of items");
}
if(window.spm){
window.spm.requireCarbonAddons().then(function(_2a0){
_2a0.ComboBox.unmount(window,_29d);
});
var _2a1=[];
for(var i=0;i<_29e.length;i++){
item={};
item.id=_29d+"_"+_29e[i];
item.text=_29f[i];
item.value=_29e[i];
_2a1.push(item);
}
labelConfig=curam.util.getTopmostWindow().__dropdownLabelConfig;
var _2a2=labelConfig?labelConfig:{};
var _2a3={inputId:_29d,items:_2a1,attributes:{},comboBoxOptions:{},labels:_2a2,iframeWindow:window};
window.spm.requireCarbonAddons().then(function(_2a4){
_2a4.ComboBox.render(_2a3);
});
}
},resetDropdownToEmpty:function(_2a5){
this.updateDropdownItems(_2a5,[],[]);
},setSelectedOnDropdownByDescription:function(_2a6,_2a7){
if(_2a6){
var _2a8=_2a6.lastIndexOf("_wrapper")!=-1?_2a6.substring(0,_2a6.lastIndexOf("_wrapper")):_2a6;
dojo.publish("/curam/comboxbox/setSelectedItem",[_2a8,undefined,_2a7]);
}
},setSelectedOnDropdownIDByCodevalue:function(_2a9,_2aa){
if(_2a9){
var _2ab=_2a9.lastIndexOf("_wrapper")!=-1?_2a9.substring(0,_2a9.lastIndexOf("_wrapper")):_2a9;
curam.util.getTopmostWindow().dojo.publish("/curam/comboxbox/setSelectedItem".concat(_2a9),[_2ab,_2aa]);
}
},setSelectedOnDropdownByCodevalue:function(_2ac,_2ad){
if(_2ac){
var _2ae=_2ac.lastIndexOf("_wrapper")!=-1?_2ac.substring(0,_2ac.lastIndexOf("_wrapper")):_2ac;
curam.util.getTopmostWindow().dojo.publish("/curam/comboxbox/setSelectedItem",[_2ae,_2ad]);
}
},executeActionFromInitialValueOnDropdown:function(_2af,_2b0){
this.dropdownInputID=_2af;
var _2b1=dojo.subscribe("/curam/comboxbox/initialValue",this,function(val,_2b2){
if(this.dropdownInputID===_2b2){
this.__initialValueFromDropdown=val;
_2b0();
dojo.unsubscribe(_2b1);
}
});
}});
return _29c;
});
},"dojo/NodeList-traverse":function(){
define(["./query","./_base/lang","./_base/array"],function(_2b3,lang,_2b4){
var _2b5=_2b3.NodeList;
lang.extend(_2b5,{_buildArrayFromCallback:function(_2b6){
var ary=[];
for(var i=0;i<this.length;i++){
var _2b7=_2b6.call(this[i],this[i],ary);
if(_2b7){
ary=ary.concat(_2b7);
}
}
return ary;
},_getUniqueAsNodeList:function(_2b8){
var ary=[];
for(var i=0,node;node=_2b8[i];i++){
if(node.nodeType==1&&_2b4.indexOf(ary,node)==-1){
ary.push(node);
}
}
return this._wrap(ary,null,this._NodeListCtor);
},_getUniqueNodeListWithParent:function(_2b9,_2ba){
var ary=this._getUniqueAsNodeList(_2b9);
ary=(_2ba?_2b3._filterResult(ary,_2ba):ary);
return ary._stash(this);
},_getRelatedUniqueNodes:function(_2bb,_2bc){
return this._getUniqueNodeListWithParent(this._buildArrayFromCallback(_2bc),_2bb);
},children:function(_2bd){
return this._getRelatedUniqueNodes(_2bd,function(node,ary){
return lang._toArray(node.childNodes);
});
},closest:function(_2be,root){
return this._getRelatedUniqueNodes(null,function(node,ary){
do{
if(_2b3._filterResult([node],_2be,root).length){
return node;
}
}while(node!=root&&(node=node.parentNode)&&node.nodeType==1);
return null;
});
},parent:function(_2bf){
return this._getRelatedUniqueNodes(_2bf,function(node,ary){
return node.parentNode;
});
},parents:function(_2c0){
return this._getRelatedUniqueNodes(_2c0,function(node,ary){
var pary=[];
while(node.parentNode){
node=node.parentNode;
pary.push(node);
}
return pary;
});
},siblings:function(_2c1){
return this._getRelatedUniqueNodes(_2c1,function(node,ary){
var pary=[];
var _2c2=(node.parentNode&&node.parentNode.childNodes);
for(var i=0;i<_2c2.length;i++){
if(_2c2[i]!=node){
pary.push(_2c2[i]);
}
}
return pary;
});
},next:function(_2c3){
return this._getRelatedUniqueNodes(_2c3,function(node,ary){
var next=node.nextSibling;
while(next&&next.nodeType!=1){
next=next.nextSibling;
}
return next;
});
},nextAll:function(_2c4){
return this._getRelatedUniqueNodes(_2c4,function(node,ary){
var pary=[];
var next=node;
while((next=next.nextSibling)){
if(next.nodeType==1){
pary.push(next);
}
}
return pary;
});
},prev:function(_2c5){
return this._getRelatedUniqueNodes(_2c5,function(node,ary){
var prev=node.previousSibling;
while(prev&&prev.nodeType!=1){
prev=prev.previousSibling;
}
return prev;
});
},prevAll:function(_2c6){
return this._getRelatedUniqueNodes(_2c6,function(node,ary){
var pary=[];
var prev=node;
while((prev=prev.previousSibling)){
if(prev.nodeType==1){
pary.push(prev);
}
}
return pary;
});
},andSelf:function(){
return this.concat(this._parent);
},first:function(){
return this._wrap(((this[0]&&[this[0]])||[]),this);
},last:function(){
return this._wrap((this.length?[this[this.length-1]]:[]),this);
},even:function(){
return this.filter(function(item,i){
return i%2!=0;
});
},odd:function(){
return this.filter(function(item,i){
return i%2==0;
});
}});
return _2b5;
});
},"curam/lnf":function(){
define(["dojo/dom","dojo/dom-class","curam/define"],function(dom,_2c7,_2c8){
_2c8.singleton("curam.lnf",{setCTParent:function(id){
var _2c9=dom.byId(id);
var _2ca=_2c9.parentNode;
if(_2ca.tagName=="TD"){
_2c7.add(_2ca,"codetable");
}
}});
return curam.lnf;
});
},"dojo/data/util/filter":function(){
define(["../../_base/lang"],function(lang){
var _2cb={};
lang.setObject("dojo.data.util.filter",_2cb);
_2cb.patternToRegExp=function(_2cc,_2cd){
var rxp="^";
var c=null;
for(var i=0;i<_2cc.length;i++){
c=_2cc.charAt(i);
switch(c){
case "\\":
rxp+=c;
i++;
rxp+=_2cc.charAt(i);
break;
case "*":
rxp+=".*";
break;
case "?":
rxp+=".";
break;
case "$":
case "^":
case "/":
case "+":
case ".":
case "|":
case "(":
case ")":
case "{":
case "}":
case "[":
case "]":
rxp+="\\";
default:
rxp+=c;
}
}
rxp+="$";
if(_2cd){
return new RegExp(rxp,"mi");
}else{
return new RegExp(rxp,"m");
}
};
return _2cb;
});
},"dojo/dnd/common":function(){
define(["../sniff","../_base/kernel","../_base/lang","../dom"],function(has,_2ce,lang,dom){
var _2cf=lang.getObject("dojo.dnd",true);
_2cf.getCopyKeyState=function(evt){
return evt[has("mac")?"metaKey":"ctrlKey"];
};
_2cf._uniqueId=0;
_2cf.getUniqueId=function(){
var id;
do{
id=_2ce._scopeName+"Unique"+(++_2cf._uniqueId);
}while(dom.byId(id));
return id;
};
_2cf._empty={};
_2cf.isFormElement=function(e){
var t=e.target;
if(t.nodeType==3){
t=t.parentNode;
}
return " a button textarea input select option ".indexOf(" "+t.tagName.toLowerCase()+" ")>=0;
};
return _2cf;
});
},"dijit/tree/ForestStoreModel":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","./TreeStoreModel"],function(_2d0,_2d1,_2d2,lang,_2d3){
return _2d1("dijit.tree.ForestStoreModel",_2d3,{rootId:"$root$",rootLabel:"ROOT",query:null,constructor:function(_2d4){
this.root={store:this,root:true,id:_2d4.rootId,label:_2d4.rootLabel,children:_2d4.rootChildren};
},mayHaveChildren:function(item){
return item===this.root||this.inherited(arguments);
},getChildren:function(_2d5,_2d6,_2d7){
if(_2d5===this.root){
if(this.root.children){
_2d6(this.root.children);
}else{
this.store.fetch({query:this.query,onComplete:lang.hitch(this,function(_2d8){
this.root.children=_2d8;
_2d6(_2d8);
}),onError:_2d7});
}
}else{
this.inherited(arguments);
}
},isItem:function(_2d9){
return (_2d9===this.root)?true:this.inherited(arguments);
},fetchItemByIdentity:function(_2da){
if(_2da.identity==this.root.id){
var _2db=_2da.scope||_2d2.global;
if(_2da.onItem){
_2da.onItem.call(_2db,this.root);
}
}else{
this.inherited(arguments);
}
},getIdentity:function(item){
return (item===this.root)?this.root.id:this.inherited(arguments);
},getLabel:function(item){
return (item===this.root)?this.root.label:this.inherited(arguments);
},newItem:function(args,_2dc,_2dd){
if(_2dc===this.root){
this.onNewRootItem(args);
return this.store.newItem(args);
}else{
return this.inherited(arguments);
}
},onNewRootItem:function(){
},pasteItem:function(_2de,_2df,_2e0,_2e1,_2e2){
if(_2df===this.root){
if(!_2e1){
this.onLeaveRoot(_2de);
}
}
this.inherited(arguments,[_2de,_2df===this.root?null:_2df,_2e0===this.root?null:_2e0,_2e1,_2e2]);
if(_2e0===this.root){
this.onAddToRoot(_2de);
}
},onAddToRoot:function(item){
console.log(this,": item ",item," added to root");
},onLeaveRoot:function(item){
console.log(this,": item ",item," removed from root");
},_requeryTop:function(){
var _2e3=this.root.children||[];
this.store.fetch({query:this.query,onComplete:lang.hitch(this,function(_2e4){
this.root.children=_2e4;
if(_2e3.length!=_2e4.length||_2d0.some(_2e3,function(item,idx){
return _2e4[idx]!=item;
})){
this.onChildrenChange(this.root,_2e4);
}
})});
},onNewItem:function(item,_2e5){
this._requeryTop();
this.inherited(arguments);
},onDeleteItem:function(item){
if(_2d0.indexOf(this.root.children,item)!=-1){
this._requeryTop();
}
this.inherited(arguments);
},onSetItem:function(item,_2e6,_2e7,_2e8){
this._requeryTop();
this.inherited(arguments);
}});
});
},"curam/util/ResourceBundle":function(){
define(["dojo/_base/declare","dojo/i18n","dojo/string"],function(_2e9,i18n,_2ea){
var _2eb=_2e9("curam.util.ResourceBundle",null,{_bundle:undefined,constructor:function(_2ec,_2ed){
var _2ee=_2ec.split(".");
var _2ef=_2ee[_2ee.length-1];
var _2f0=_2ee.length==1?"curam.application":_2ec.slice(0,_2ec.length-_2ef.length-1);
try{
var b=i18n.getLocalization(_2f0,_2ef,_2ed);
if(this._isEmpty(b)){
throw new Error("Empty resource bundle.");
}else{
this._bundle=b;
}
}
catch(e){
throw new Error("Unable to access resource bundle: "+_2f0+"."+_2ef+": "+e.message);
}
},_isEmpty:function(_2f1){
for(var prop in _2f1){
return false;
}
return true;
},getProperty:function(key,_2f2){
var msg=this._bundle[key];
var _2f3=msg;
if(_2f2){
_2f3=_2ea.substitute(msg,_2f2);
}
return _2f3;
}});
return _2eb;
});
},"dijit/Menu":function(){
define(["require","dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/sniff","dojo/_base/window","dojo/window","./popup","./DropDownMenu","dojo/ready"],function(_2f4,_2f5,_2f6,dom,_2f7,_2f8,_2f9,keys,lang,on,has,win,_2fa,pm,_2fb,_2fc){
if(1){
_2fc(0,function(){
var _2fd=["dijit/MenuItem","dijit/PopupMenuItem","dijit/CheckedMenuItem","dijit/MenuSeparator"];
_2f4(_2fd);
});
}
return _2f6("dijit.Menu",_2fb,{constructor:function(){
this._bindings=[];
},targetNodeIds:[],selector:"",contextMenuForWindow:false,leftClickToOpen:false,refocus:true,postCreate:function(){
if(this.contextMenuForWindow){
this.bindDomNode(this.ownerDocumentBody);
}else{
_2f5.forEach(this.targetNodeIds,this.bindDomNode,this);
}
this.inherited(arguments);
},_iframeContentWindow:function(_2fe){
return _2fa.get(this._iframeContentDocument(_2fe))||this._iframeContentDocument(_2fe)["__parent__"]||(_2fe.name&&document.frames[_2fe.name])||null;
},_iframeContentDocument:function(_2ff){
return _2ff.contentDocument||(_2ff.contentWindow&&_2ff.contentWindow.document)||(_2ff.name&&document.frames[_2ff.name]&&document.frames[_2ff.name].document)||null;
},bindDomNode:function(node){
node=dom.byId(node,this.ownerDocument);
var cn;
if(node.tagName.toLowerCase()=="iframe"){
var _300=node,_301=this._iframeContentWindow(_300);
cn=win.body(_301.document);
}else{
cn=(node==win.body(this.ownerDocument)?this.ownerDocument.documentElement:node);
}
var _302={node:node,iframe:_300};
_2f7.set(node,"_dijitMenu"+this.id,this._bindings.push(_302));
var _303=lang.hitch(this,function(cn){
var _304=this.selector,_305=_304?function(_306){
return on.selector(_304,_306);
}:function(_307){
return _307;
},self=this;
return [on(cn,_305(this.leftClickToOpen?"click":"contextmenu"),function(evt){
evt.stopPropagation();
evt.preventDefault();
if((new Date()).getTime()<self._lastKeyDown+500){
return;
}
self._scheduleOpen(this,_300,{x:evt.pageX,y:evt.pageY},evt.target);
}),on(cn,_305("keydown"),function(evt){
if(evt.keyCode==93||(evt.shiftKey&&evt.keyCode==keys.F10)||(self.leftClickToOpen&&evt.keyCode==keys.SPACE)){
evt.stopPropagation();
evt.preventDefault();
self._scheduleOpen(this,_300,null,evt.target);
self._lastKeyDown=(new Date()).getTime();
}
})];
});
_302.connects=cn?_303(cn):[];
if(_300){
_302.onloadHandler=lang.hitch(this,function(){
var _308=this._iframeContentWindow(_300),cn=win.body(_308.document);
_302.connects=_303(cn);
});
if(_300.addEventListener){
_300.addEventListener("load",_302.onloadHandler,false);
}else{
_300.attachEvent("onload",_302.onloadHandler);
}
}
},unBindDomNode:function(_309){
var node;
try{
node=dom.byId(_309,this.ownerDocument);
}
catch(e){
return;
}
var _30a="_dijitMenu"+this.id;
if(node&&_2f7.has(node,_30a)){
var bid=_2f7.get(node,_30a)-1,b=this._bindings[bid],h;
while((h=b.connects.pop())){
h.remove();
}
var _30b=b.iframe;
if(_30b){
if(_30b.removeEventListener){
_30b.removeEventListener("load",b.onloadHandler,false);
}else{
_30b.detachEvent("onload",b.onloadHandler);
}
}
_2f7.remove(node,_30a);
delete this._bindings[bid];
}
},_scheduleOpen:function(_30c,_30d,_30e,_30f){
if(!this._openTimer){
this._openTimer=this.defer(function(){
delete this._openTimer;
this._openMyself({target:_30f,delegatedTarget:_30c,iframe:_30d,coords:_30e});
},1);
}
},_openMyself:function(args){
var _310=args.target,_311=args.iframe,_312=args.coords,_313=!_312;
this.currentTarget=args.delegatedTarget;
if(_312){
if(_311){
var ifc=_2f8.position(_311,true),_314=this._iframeContentWindow(_311),_315=_2f8.docScroll(_314.document);
var cs=_2f9.getComputedStyle(_311),tp=_2f9.toPixelValue,left=(has("ie")&&has("quirks")?0:tp(_311,cs.paddingLeft))+(has("ie")&&has("quirks")?tp(_311,cs.borderLeftWidth):0),top=(has("ie")&&has("quirks")?0:tp(_311,cs.paddingTop))+(has("ie")&&has("quirks")?tp(_311,cs.borderTopWidth):0);
_312.x+=ifc.x+left-_315.x;
_312.y+=ifc.y+top-_315.y;
}
}else{
_312=_2f8.position(_310,true);
_312.x+=10;
_312.y+=10;
}
var self=this;
var _316=this._focusManager.get("prevNode");
var _317=this._focusManager.get("curNode");
var _318=!_317||(dom.isDescendant(_317,this.domNode))?_316:_317;
function _319(){
if(self.refocus&&_318){
_318.focus();
}
pm.close(self);
};
pm.open({popup:this,x:_312.x,y:_312.y,onExecute:_319,onCancel:_319,orient:this.isLeftToRight()?"L":"R"});
this.focus();
if(!_313){
this.defer(function(){
this._cleanUp(true);
});
}
this._onBlur=function(){
this.inherited("_onBlur",arguments);
pm.close(this);
};
},destroy:function(){
_2f5.forEach(this._bindings,function(b){
if(b){
this.unBindDomNode(b.node);
}
},this);
this.inherited(arguments);
}});
});
},"curam/util/TabNavigation":function(){
dojo.require("curam.util.ResourceBundle");
define(["dijit/registry","dojo/dom","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","curam/inspection/Layer","curam/debug","curam/define","curam/util","curam/tab","curam/util/Refresh"],function(_31a,dom,_31b,_31c,_31d,_31e,_31f,_320){
curam.define.singleton("curam.util.TabNavigation",{CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",disabledItems:{},tabLists:{},init:function(_321,_322){
var _323=_321+"child-nav-selectChild";
var _324=dojo.subscribe(_323,"",function(){
curam.util.TabNavigation.onParentSelect(null,_321);
});
curam.tab.unsubscribeOnTabClose(_324,_322);
},onParentSelect:function(_325,_326){
var _327=_326+"-child-nav";
var _328=_31a.byId(_327);
var _329=true;
if(!_325){
var _329=false;
var _32a=_326+"-parent-nav";
var _32b=_31a.byId(_32a);
_325=_32b.selectedChildWidget;
}
if(_325.curamDoNoReload){
_329=false;
_325.setAttribute("curamDoNoReload",null);
}
var _32c=_325.id+"-Stack";
var _32d=_31a.byId(_32c);
var href=_31d.get(_32d.get("srcNodeRef"),"page-ref");
if(!href){
var _32e=_32d;
if(_32e){
var link=dojo.query("li.selected > div.link",_32e.id)[0];
href=_31d.get(link,"page-ref");
}else{
throw new Error("Could not find a page reference. The menu item '"+_325.id+"' has no page reference and no selected child item was found.");
}
}
if(_329){
var ifr=curam.util.TabNavigation.getIframe(_326);
if(dojo.isIE&&dojo.isIE<9){
ifrBody=ifr.contentWindow.document.body;
}else{
ifrBody=ifr.contentDocument.activeElement;
}
var _32f=function(){
_328.selectChild(_32d);
_31b.set(_328.domNode,"visibility","visible");
_31b.set(ifr,"visibility","visible");
_31b.set(ifr,"opacity","1");
curam.debug.log("curam.util.TabNavigation.postIframeLoadHandler anon function calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
};
if(dojo.isIE&&dojo.isIE<9){
var lh=function(){
if(ifr.readyState=="complete"){
ifr.detachEvent("onreadystatechange",lh);
_32f();
}
};
ifr.attachEvent("onreadystatechange",lh);
}else{
var dt=dojo.connect(ifr,"onload",null,function(){
dojo.disconnect(dt);
_32f();
});
}
dojo.query("div.list",ifrBody).forEach(function(node){
_31c.add(node,"hidden");
});
_31b.set(ifr,"opacity","0");
_31b.set(_328.domNode,"visibility","hidden");
curam.util.TabNavigation.loadIframe(href,_326);
}
var open=curam.util.TabNavigation.childMenuExists(_325);
curam.util.TabNavigation.toggleChildMenu(open,_326);
},childMenuExists:function(_330){
var _331=_330.id+"-Stack";
var _332=dojo.query("#"+_331+" ul");
if(_332.length==0){
return false;
}else{
return true;
}
},toggleChildMenu:function(open,_333){
var _334=_333+"-navigation-tab";
var _335=dom.byId(_334);
var _336=dojo.query(".content-area-container",_335)[0];
var _337=dojo.query(".child-nav",_335)[0];
if(!open){
var _338="0px";
var _339=((_31b.getComputedStyle(_336).direction=="ltr")?{left:_338}:{right:_338});
var _33a={width:_338};
_31b.set(_336,_339);
_31b.set(_337,_33a);
}else{
var _33b=_31d.get(_335,"child-menu-width");
var _339=((_31b.getComputedStyle(_336).direction=="ltr")?{left:_33b}:{right:_33b});
var _33a={width:_33b};
_31b.set(_336,_339);
_31b.set(_337,_33a);
}
},handleChildSelect:function(_33c,_33d,_33e){
if(!curam.util.TabNavigation.isSelectable(_33c.parentNode.id)){
dojo.stopEvent(dojo.fixEvent(_33e));
return false;
}
var ul=curam.util.TabNavigation.getNext(_33c,"UL");
var _33f=ul.childNodes;
for(var i=0;i<_33f.length;i++){
_31c.replace(_33f[i],"not-selected","selected");
}
_31c.replace(_33c.parentNode,"selected","not-selected");
var href=_31d.get(_33c,"page-ref");
curam.util.TabNavigation.loadIframe(href,_33d);
return true;
},isSelectable:function(_340){
return !curam.util.TabNavigation.disabledItems[_340];
},getNext:function(_341,_342){
var _343=_341.parentNode;
if(_343==null){
curam.debug.log(_320.getProperty("curam.util.TabNavigation.error",[_342]));
return null;
}
if(_343.nodeName===_342){
return _343;
}else{
var _343=curam.util.TabNavigation.getNext(_343,_342);
return _343;
}
},loadIframe:function(href,_344){
var _345=curam.util.TabNavigation.getIframe(_344);
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_31a.getEnclosingWidget(_345).domNode]);
_31d.set(_345,"src",href+"&"+this.getCacheBusterParameter());
},getIframe:function(_346){
var _347=_346+"-navigation-tab";
var _348=dom.byId(_347);
var _349=dojo.query("iframe",_348);
return _349[0];
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},setupOnParentSelect:function(_34a,_34b,_34c){
var _34d=dom.byId(_34a+"-navigation-tab");
var _34e=curam.tab.getContainerTab(_34d);
_34e.subscribe(_34a+"-child-nav-startup",function(){
curam.util.TabNavigation.onParentSelect(null,_34a);
var tabs=_34c.split(",");
for(tabID in tabs){
var _34f=curam.util.TabNavigation.findNavItem("navItem_"+this.id+"_"+tabs[tabID]);
if(_34f!=null){
_34f.set("curamVisible",false);
}
}
});
_34e.subscribe(_34b,function(_350){
curam.util.TabNavigation.onParentSelect(_350,_34a);
});
},setupRefresh:function(_351){
curam.util.Refresh.setNavigationCallbacks(curam.util.TabNavigation.updateNavItemStates,curam.util.TabNavigation.getRefreshParams);
},getRefreshParams:function(_352){
curam.debug.log("curam.util.TabNavigation.getRefreshParams(%s)",_352);
var _353=curam.util.TabNavigation.dynamicNavigationData[_352];
if(!_353){
curam.debug.log(_320.getProperty("curam.util.TabNavigation.no.dynamic"));
return null;
}
var _354="navId="+_353.navigationId;
_354+="&navItemIds="+curam.util.toCommaSeparatedList(_353.dynamicNavItemIds);
_354+="&navLoaders="+curam.util.toCommaSeparatedList(_353.dynamicNavLoaders);
_354+="&navPageParameters="+_353.pageParameters;
return _354;
},updateNavItemStates:function(_355,data){
var _356=data.navData;
for(var i=0;i<_356.itemStates.length;i++){
curam.util.TabNavigation.updateNavItemState(_356.itemStates[i],_355);
}
curam.util.TabNavigation._updateAriaMarkupForEnabledAndDisabledNavigationTabs();
},_updateAriaMarkupForEnabledAndDisabledNavigationTabs:function(){
var _357;
var _358;
var _359="true";
var _35a=[];
var _35b=dojo.query(".dijitTabContainerTop-tabs");
var _35c=new curam.util.ResourceBundle("TabNavigation");
_35b.forEach(function(_35d){
_357=dojo.query(".tabLabel",_35d);
if(_357.length>0&&_359=="true"){
_358=0;
_35a=[];
_359="false";
}
_357.forEach(function(tab){
var _35e=tab.id+"_tabButtonContainer";
var _35f=dojo.byId(_35e);
if(!_31c.contains(_35f,"disabled")&&(_31c.contains(_35f,"visible")||_31c.contains(_35f,"enabled, dijitChecked"))){
_31d.set(tab,"aria-disabled","false");
}else{
_31d.set(tab,"aria-disabled","true");
_358++;
var _360=_31d.get(tab,"title");
if(typeof (_360)!="undefined"){
_35a.push(" "+_360);
}
}
});
_357.forEach(function(tab,_361){
var _362=_31d.get(tab,"aria-disabled");
if(_362=="false"){
var _363;
var _364=_357[_361].innerHTML;
tab.setAttribute("title",_364);
if(_358==1){
tab.removeAttribute("aria-labelledby");
_363=". "+_35c.getProperty("curam.navigation.tab")+" "+(_361+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_357.length+". "+_358+" "+_35c.getProperty("curam.single.disabled.navigation.tab")+": "+_35a+".";
tab.setAttribute("aria-label",_364+_363);
}else{
if(_358>1){
tab.removeAttribute("aria-labelledby");
_363=". "+_35c.getProperty("curam.navigation.tab")+" "+(_361+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_357.length+". "+_358+" "+_35c.getProperty("curam.multiple.disabled.navigation.tab")+": "+_35a+".";
tab.setAttribute("aria-label",_364+_363);
}else{
_363=". "+_35c.getProperty("curam.navigation.tab")+" "+(_361+1)+" "+LOCALISED_TABCONTAINER_CONTEXT_OF+" "+_357.length+". ";
tab.setAttribute("aria-label",_364+_363);
}
}
}else{
tab.removeAttribute("role");
tab.removeAttribute("tabindex");
tab.removeAttribute("aria-label");
}
});
_359="true";
});
},updateNavItemState:function(_365,_366){
var _367=curam.util.TabNavigation.findNavItem("navItem_"+_366+"_"+_365.id);
if(_367!=null){
if(!_367.domNode){
curam.util.TabNavigation.disabledItems[_367.id]=!_365.enabled;
curam.util.swapState(_367,_365.enabled,"enabled","disabled");
curam.util.swapState(_367,_365.visible,"visible","hidden");
var _368=_31d.get(_367,"class").indexOf("disabled")>0?true:false;
if(_368){
_31d.set(_367.children[0],"aria-disabled","true");
}
}else{
_367.set("curamDisabled",!_365.enabled);
_367.set("curamVisible",_365.visible);
}
}
},findNavItem:function(_369){
var _36a=dojo.query("."+_369);
if(_36a.length==1){
var node=_36a[0];
var _36b=dijit.byNode(node);
if(!_36b){
return node;
}else{
return _36b.controlButton;
}
}else{
curam.debug.log(_320.getProperty("curam.util.TabNavigation.item",[_369]));
return null;
}
},addRollOverClass:function(_36c){
_31c.add(_36c.target,"hover");
curam.util.connect(_36c.target,"onmouseout",function(){
_31c.remove(_36c.target,"hover");
});
},setupOnLoadListener:function(_36d,_36e){
var _36f=dojo.fromJson(_36e);
var _370=function(_371,_372){
curam.util.TabNavigation.handleContentAreaUpdate(_371,_372,_36f);
};
var _373=curam.tab.getHandlerForTab(_370,_36d);
var _374=curam.util.getTopmostWindow();
var _375=_374.dojo.subscribe("/curam/main-content/page/loaded",null,_373);
curam.tab.unsubscribeOnTabClose(_375,_36d);
},setupTabList:function(_376,_377){
if(!curam.util.TabNavigation.tabLists[_376]){
curam.tab.executeOnTabClose(function(){
delete curam.util.TabNavigation.tabLists[_376];
},_376);
}
delete curam.util.TabNavigation.tabLists[_376];
curam.util.TabNavigation.tabLists[_376]=_377;
},handleContentAreaUpdate:function(_378,_379,_37a){
var ids=_37a[_378];
if(ids){
var _37b=ids["dojoTabId"];
var _37c=_37b+"-parent-nav";
var _37d=ids["tabId"];
var _37e=ids["childId"];
var _37f=_31a.byId(_37d);
var _380=_31a.byId(_37c);
if(_37f){
if(_380.selectedChildWidget!=_37f){
_37f.setAttribute("curamDoNoReload",true);
_380.selectChild(_37f);
}
if(_37e){
var _381=_37d+"-Stack";
var _382=_37b+"-child-nav";
var _383=_31a.byId(_382);
var _384=_31a.byId(_381);
_383.selectChild(_384);
var _385=dojo.query("li",_384.domNode);
for(var i=0;i<_385.length;i++){
var _386=_385[i];
if(_386.id==_37e){
var _387=_386;
}
}
if(_387){
if(!_31c.contains(_387,"selected")){
var _388=_387.parentNode.childNodes;
for(var i=0;i<_388.length;i++){
_31c.replace(_388[i],"not-selected","selected");
}
_31c.replace(_387,"selected","not-selected");
}
}
}
}
}
},getInsertIndex:function(_389,_38a,_38b){
var _38c=curam.util.TabNavigation.tabLists[_389];
var _38d=dojo.indexOf(_38c,_38b);
var _38e=_38d;
for(var i=_38d-1;i>=0;i--){
if(dojo.indexOf(_38a,_38c[i])<0){
_38e--;
}
}
return _38e;
}});
_31f.register("curam/util/TabNavigation",curam.util.TabNavigation);
return curam.util.TabNavigation;
});
},"dojo/store/Observable":function(){
define(["../_base/kernel","../_base/lang","../when","../_base/array"],function(_38f,lang,when,_390){
var _391=function(_392){
var _393,_394=[],_395=0;
_392=lang.delegate(_392);
_392.notify=function(_396,_397){
_395++;
var _398=_394.slice();
for(var i=0,l=_398.length;i<l;i++){
_398[i](_396,_397);
}
};
var _399=_392.query;
_392.query=function(_39a,_39b){
_39b=_39b||{};
var _39c=_399.apply(this,arguments);
if(_39c&&_39c.forEach){
var _39d=lang.mixin({},_39b);
delete _39d.start;
delete _39d.count;
var _39e=_392.queryEngine&&_392.queryEngine(_39a,_39d);
var _39f=_395;
var _3a0=[],_3a1;
_39c.observe=function(_3a2,_3a3){
if(_3a0.push(_3a2)==1){
_394.push(_3a1=function(_3a4,_3a5){
when(_39c,function(_3a6){
var _3a7=_3a6.length!=_39b.count;
var i,l,_3a2;
if(++_39f!=_395){
throw new Error("Query is out of date, you must observe() the query prior to any data modifications");
}
var _3a8,_3a9=-1,_3aa=-1;
if(_3a5!==_393){
var _3ab=[].concat(_3a6);
if(_39e&&!_3a4){
_3ab=_39e(_3a6);
}
for(i=0,l=_3a6.length;i<l;i++){
var _3ac=_3a6[i];
if(_392.getIdentity(_3ac)==_3a5){
if(_3ab.indexOf(_3ac)<0){
continue;
}
_3a8=_3ac;
_3a9=i;
if(_39e||!_3a4){
_3a6.splice(i,1);
}
break;
}
}
}
if(_39e){
if(_3a4&&(_39e.matches?_39e.matches(_3a4):_39e([_3a4]).length)){
var _3ad=_3a9>-1?_3a9:_3a6.length;
_3a6.splice(_3ad,0,_3a4);
_3aa=_390.indexOf(_39e(_3a6),_3a4);
_3a6.splice(_3ad,1);
if((_39b.start&&_3aa==0)||(!_3a7&&_3aa==_3a6.length)){
_3aa=-1;
}else{
_3a6.splice(_3aa,0,_3a4);
}
}
}else{
if(_3a4){
if(_3a5!==_393){
_3aa=_3a9;
}else{
if(!_39b.start){
_3aa=_392.defaultIndex||0;
_3a6.splice(_3aa,0,_3a4);
}
}
}
}
if((_3a9>-1||_3aa>-1)&&(_3a3||!_39e||(_3a9!=_3aa))){
var _3ae=_3a0.slice();
for(i=0;_3a2=_3ae[i];i++){
_3a2(_3a4||_3a8,_3a9,_3aa);
}
}
});
});
}
var _3af={};
_3af.remove=_3af.cancel=function(){
var _3b0=_390.indexOf(_3a0,_3a2);
if(_3b0>-1){
_3a0.splice(_3b0,1);
if(!_3a0.length){
_394.splice(_390.indexOf(_394,_3a1),1);
}
}
};
return _3af;
};
}
return _39c;
};
var _3b1;
function _3b2(_3b3,_3b4){
var _3b5=_392[_3b3];
if(_3b5){
_392[_3b3]=function(_3b6){
var _3b7;
if(_3b3==="put"){
_3b7=_392.getIdentity(_3b6);
}
if(_3b1){
return _3b5.apply(this,arguments);
}
_3b1=true;
try{
var _3b8=_3b5.apply(this,arguments);
when(_3b8,function(_3b9){
_3b4((typeof _3b9=="object"&&_3b9)||_3b6,_3b7);
});
return _3b8;
}
finally{
_3b1=false;
}
};
}
};
_3b2("put",function(_3ba,_3bb){
_392.notify(_3ba,_3bb);
});
_3b2("add",function(_3bc){
_392.notify(_3bc);
});
_3b2("remove",function(id){
_392.notify(undefined,id);
});
return _392;
};
lang.setObject("dojo.store.Observable",_391);
return _391;
});
},"curam/pagination/ControlPanel":function(){
define(["dijit/registry","dojo/_base/declare","dojo/dom-style","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/sniff","dojo/window","curam/pagination","curam/debug","curam/util"],function(_3bd,_3be,_3bf,_3c0,_3c1,_3c2,has,_3c3){
var _3c4=_3be("curam.pagination.ControlPanel",null,{first:"FIRST",last:"LAST",previous:"PREV",next:"NEXT",page:"GOTO_PAGE",pageSize:"PAGE_SIZE",rowInfo:"ROW_INFO",classFirst:"first",classLast:"last",classPrevious:"previous",classNext:"next",classPage:"page",classDisplayInfo:"display_info",_controls:undefined,currentPage:0,lastPage:9999,currentPageSize:0,directLinkRangeWidth:3,parentNode:undefined,handlers:undefined,directLinksDisconnects:undefined,constructor:function(_3c5){
this._controls={};
this.handlers={};
this.directLinksDisconnects=[];
var loc=this._localize;
var ul=_3c2.create("ul",null,_3c5);
_3c1.add(ul,"pagination-control-list");
this._controls[this.pageSize]=this._createDropdownControl(this.pageSize,loc("pageSize_title"),ul);
this._controls[this.rowInfo]=this._createDisplayControl(this.rowInfo,loc("pagination_info",["$dummy$","$dummy$","$dummy$"]),ul,null,null);
this._controls[this.first]=this._createLinkControl(this.first,loc("firstPage_btn"),ul,null,this.classFirst,loc("firstPage_title"));
this._controls[this.previous]=this._createLinkControl(this.previous,loc("prevPage_btn"),ul,null,this.classPrevious,loc("prevPage_title"));
this._controls[this.page]=[];
this._controls[this.page].push(this._createLinkControl(this.page,"direct-page-links-section",ul,null,this.classPage,loc("page_title")));
this._controls[this.next]=this._createLinkControl(this.next,loc("nextPage_btn"),ul,null,this.classNext,loc("nextPage_title"));
this._controls[this.last]=this._createLinkControl(this.last,loc("lastPage_btn"),ul,null,this.classLast,loc("lastPage_title"));
this.parentNode=_3c5;
_3bf.set(_3c5,{"display":""});
},_localize:function(_3c6,_3c7){
var _3c8=curam.pagination.localizedStrings[_3c6];
if(!_3c7){
return _3c8;
}
for(var i=0;i<_3c7.length;i++){
_3c8=_3c8.replace(/%s/i,_3c7[i]);
}
return _3c8;
},_createLinkControl:function(_3c9,text,_3ca,_3cb,_3cc,_3cd){
var cls=_3cc!=null?_3cc:"";
var li=_3c2.create("li",{"id":_3c9,"class":cls},_3ca,_3cb);
_3c1.add(li,"pagination-control-list-item enabled");
var a=_3c2.create("a",{"innerHTML":text,"href":"#","title":_3cd},li);
_3c1.add(a,"pagination-link");
if(_3c9==this.first||_3c9==this.last||_3c9==this.previous||_3c9==this.next){
if(curam.util.highContrastModeType()){
var _3ce="../CDEJ/themes/v6/images/high-contrast/"+_3c9+"-contrast"+".png";
_3c2.create("img",{"src":_3ce,"alt":_3cd},a);
}else{
var _3ce="../CDEJ/themes/curam/images/"+_3c9+".svg";
var _3cf="../CDEJ/themes/curam/images/"+_3c9+"_hover.svg";
a.setAttribute("onfocus","this.children[0].src = '"+_3cf+"';");
a.setAttribute("onblur","this.children[0].src = '"+_3ce+"';");
_3c2.create("img",{"src":_3ce,"alt":_3cd,"onmouseover":"this.src = '"+_3cf+"';","onmouseout":"this.src = '"+_3ce+"';","onmouseup":"this.src = '"+_3ce+"';","aria-hidden":"true"},a);
}
}else{
var text=_3c2.create("p",{"innerHTML":text},li);
_3c1.add(text,"pagination-text");
}
return li;
},_createDropdownControl:function(_3d0,text,_3d1,_3d2){
var li=_3c2.create("li",{"id":_3d0},_3d1,_3d2);
_3c1.add(li,"pagination-control-list-item");
var _3d3="page-size-select"+new Date().getTime();
var _3d4=_3c2.create("label",{"innerHTML":text+": ","for":_3d3},li);
_3c1.add(_3d4,"pagination-page-size-dropdown-label");
var _3d5=_3c2.create("select",{"title":text,"id":_3d3},li);
li._type="dropdown";
return li;
},_createDisplayControl:function(_3d6,text,_3d7,_3d8,_3d9){
var cls=_3d9!=null?_3d9:"";
var li=_3c2.create("li",{"id":_3d6,"class":cls},_3d7,_3d8);
_3c1.add(li,"pagination-control-list-item");
var text=_3c2.create("p",{"innerHTML":"["+text+"]"},li);
return li;
},updateState:function(_3da){
curam.debug.log("curam.pagination.ControlPanel.updateState: ",_3da);
if(typeof (_3da.first)!="undefined"){
this._setEnabled(this._controls[this.first],_3da.first);
}
if(typeof (_3da.previous)!="undefined"){
this._setEnabled(this._controls[this.previous],_3da.previous);
}
if(typeof (_3da.next)!="undefined"){
this._setEnabled(this._controls[this.next],_3da.next);
}
if(typeof (_3da.last)!="undefined"){
this._setEnabled(this._controls[this.last],_3da.last);
}
if(typeof (_3da.currentPage)!="undefined"){
this.currentPage=_3da.currentPage;
}
if(typeof (_3da.lastPage)!="undefined"){
this.lastPage=_3da.lastPage;
}
if(typeof (_3da.currentPageSize)!="undefined"){
this.currentPageSize=_3da.currentPageSize;
}
if(typeof (_3da.directLinkRangeWidth)!="undefined"){
this.directLinkRangeWidth=_3da.directLinkRangeWidth;
}
if(typeof (_3da.rowInfo)!="undefined"){
var _3db=this._controls[this.rowInfo].previousSibling;
_3c2.destroy(this._controls[this.rowInfo]);
var _3dc=_3da.rowInfo[0];
var end=_3da.rowInfo[1];
var _3dd=_3da.rowInfo[2];
var _3de=this._localize("pagination_info",[_3dc,end,_3dd]);
this._controls[this.rowInfo]=this._createDisplayControl(this.rowInfo,_3de,_3db,"after",this.classDisplayInfo);
}
if(typeof (_3da.pageSizeOptions)!="undefined"){
var _3df=dojo.query("select",this._controls[this.pageSize])[0];
dojo.forEach(_3df.childNodes,function(item){
_3c2.destroy(item);
});
for(var i=0;i<_3da.pageSizeOptions.length;i++){
var _3e0=_3da.pageSizeOptions[i];
var _3e1=_3c2.create("option",{"value":_3e0,"innerHTML":_3e0},_3df);
if(_3e0==this.currentPageSize){
_3c0.set(_3e1,"selected","selected");
}
}
}
this._updateDirectLinks();
var _3e2=_3bd.byId("content");
if(_3e2){
_3e2.resize();
}
},setHandlers:function(_3e3){
curam.debug.log("curam.pagination.ControlPanel.setHandlers: ",_3e3);
this.handlers=_3e3;
if(_3e3.first){
this._connectSimpleHandler(this._controls[this.first],_3e3.first);
}
if(_3e3.previous){
this._connectSimpleHandler(this._controls[this.previous],_3e3.previous);
}
if(_3e3.next){
this._connectSimpleHandler(this._controls[this.next],_3e3.next);
}
if(_3e3.last){
this._connectSimpleHandler(this._controls[this.last],_3e3.last);
}
if(_3e3.page){
this._connectDirectLinkHandlers(_3e3.page);
}
if(_3e3.pageSize){
var _3e4=dojo.query("select",this._controls[this.pageSize])[0];
var _3e5=this._setFocusToListDivContainerAfterNewPageSelected;
dojo.connect(_3e4,"onchange",dojo.hitch(this,function(_3e6){
var _3e7=_3e6.target.value;
this.currentPageSize=_3e7;
_3e3.pageSize(this.currentPageSize);
var _3e8=_3e6.currentTarget;
if(_3e8&&_3c1.contains(_3e8.parentElement,"pagination-control-list-item")){
var _3e9=_3e8.parentElement;
_3e5(_3e9);
}
var _3ea=dojo.query("option",_3e4);
_3ea.forEach(function(_3eb){
if(_3c0.get(_3eb,"value")==_3e7){
_3c0.set(_3eb,"selected","selected");
}else{
_3c0.remove(_3eb,"selected");
}
});
}));
}
},_connectSimpleHandler:function(_3ec,_3ed){
var h=_3ed?_3ed:_3ec._handler;
this._removeSimpleHandler(_3ec);
var _3ee=this._setFocusToListDivContainerAfterNewPageSelected;
var _3ef=curam.util.connect(_3ec,"onclick",function(_3f0){
dojo.stopEvent(_3f0);
h();
var _3f1=_3f0.currentTarget;
if(_3f1&&_3c1.contains(_3f1,"pagination-control-list-item")){
var _3f2=_3f1.parentElement;
_3ee(_3f2);
}
});
_3ec._handler=h;
_3ec._disconnect=_3ef;
},_removeSimpleHandler:function(_3f3){
if(_3f3._disconnect){
curam.util.disconnect(_3f3._disconnect);
}
},reset:function(){
curam.debug.log("curam.pagination.ControlPanel.reset");
},_getDirectLinkPageNumbers:function(){
var _3f4=2*this.directLinkRangeWidth+1;
var p=this.currentPage;
var _3f5=[];
var num=p>this.directLinkRangeWidth?p-this.directLinkRangeWidth:1;
for(var i=0;i<_3f4;i++){
_3f5[i]=num++;
if(num>this.lastPage){
break;
}
}
return _3f5;
},_updateDirectLinks:function(){
curam.debug.log("curam.pagination.ControlPanel._updateDirectLinks");
var loc=this._localize;
var _3f6=this._controls[this.page];
dojo.query("div.pagination-direct-links-dots").forEach(_3c2.destroy);
var _3f7=_3f6[0].previousSibling;
_3bf.set(this.parentNode,"display","none");
for(var i=0;i<_3f6.length;i++){
if(_3f6._dots){
_3c2.destroy(_3f6._dots);
}
_3c2.destroy(_3f6[i]);
_3f6[i]=undefined;
}
this._controls[this.page]=[];
_3f6=this._controls[this.page];
var _3f8=this._getDirectLinkPageNumbers();
for(var i=0;i<_3f8.length;i++){
var _3f9=_3f8[i];
_3f6[i]=this._createLinkControl(this.page+"("+_3f9+")",_3f9,_3f7,"after",null,loc("page_title")+" "+_3f9);
_3c1.add(_3f6[i],"pagination-direct-link");
if(_3f9==this.currentPage){
_3c1.add(_3f6[i],"selected");
}
_3f7=_3f6[i];
_3f6[i]._pageNum=_3f9;
}
var _3fa=_3f6[0];
_3c1.add(_3fa,"firstDirectLink");
if(_3f8[0]>1){
_3c1.add(_3fa,"has-previous");
var dots=_3c2.create("div",{innerHTML:"..."},_3fa,"before");
_3c1.add(dots,"pagination-direct-links-dots");
}
var _3fb=_3f6[_3f6.length-1];
_3c1.add(_3fb,"lastDirectLink");
if(_3f8[_3f8.length-1]<this.lastPage){
_3c1.add(_3fb,"has-next");
var dots=_3c2.create("div",{innerHTML:"..."},_3fb,"after");
_3c1.add(dots,"pagination-direct-links-dots");
}
if(this.handlers.page){
this._connectDirectLinkHandlers(this.handlers.page);
}
_3bf.set(this.parentNode,"display","");
},_connectDirectLinkHandlers:function(_3fc){
dojo.forEach(this.directLinksDisconnects,dojo.disconnect);
this.directLinksDisconnects=[];
var _3fd=this._controls[this.page];
for(var i=0;i<_3fd.length;i++){
var _3fe=_3fd[i];
var _3ff=this._setFocusToListDivContainerAfterNewPageSelected;
var h=function(_400){
dojo.stopEvent(_400);
var _401=_400.currentTarget;
var _402=_401.parentElement;
_3fc(this._pageNum);
if(_401&&_3c1.contains(_401,"pagination-control-list-item")){
if(!_3c1.contains(_401,"selected")){
_3ff(_402);
}else{
var _403=_401.id;
var _404=dojo.query("a.pagination-link",_402.children[_403])[0];
if(_404){
_404.focus();
}
}
}
};
h._pageNum=_3fe._pageNum;
this.directLinksDisconnects.push(dojo.connect(_3fe,"onclick",h));
}
},_setEnabled:function(_405,_406){
if(_406){
this._connectSimpleHandler(_405);
_3c1.replace(_405,"enabled","disabled");
}else{
this._removeSimpleHandler(_405);
_3c1.replace(_405,"disabled","enabled");
}
},_setFocusToListDivContainerAfterNewPageSelected:function(_407){
var _408="";
while(_407!==null){
if(_3c1.contains(_407,"list")){
_408=_407;
break;
}else{
_407=_407.parentElement;
}
}
if(_408!=""){
_408.setAttribute("tabindex","-1");
_408.focus();
has("trident")?_3c3.scrollIntoView(_408):_408.scrollIntoView();
}
}});
return _3c4;
});
},"dijit/form/MappedTextBox":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/dom-construct","./ValidationTextBox"],function(_409,has,_40a,_40b){
return _409("dijit.form.MappedTextBox",_40b,{postMixInProperties:function(){
this.inherited(arguments);
this.nameAttrSetting="";
},_setNameAttr:"valueNode",serialize:function(val){
return val.toString?val.toString():"";
},toString:function(){
var val=this.filter(this.get("value"));
return val!=null?(typeof val=="string"?val:this.serialize(val,this.constraints)):"";
},validate:function(){
this.valueNode.value=this.toString();
return this.inherited(arguments);
},buildRendering:function(){
this.inherited(arguments);
this.valueNode=_40a.place("<input type='hidden'"+((this.name&&!has("msapp"))?" name=\""+this.name.replace(/"/g,"&quot;")+"\"":"")+"/>",this.textbox,"after");
},reset:function(){
this.valueNode.value="";
this.inherited(arguments);
}});
});
},"dojox/layout/ContentPane":function(){
define(["dojo/_base/lang","dojo/_base/xhr","dijit/layout/ContentPane","dojox/html/_base","dojo/_base/declare"],function(lang,_40c,_40d,_40e,_40f){
return _40f("dojox.layout.ContentPane",_40d,{adjustPaths:false,cleanContent:false,renderStyles:false,executeScripts:true,scriptHasHooks:false,ioMethod:_40c.get,ioArgs:{},onExecError:function(e){
},_setContent:function(cont){
var _410=this._contentSetter;
if(!(_410&&_410 instanceof _40e._ContentSetter)){
_410=this._contentSetter=new _40e._ContentSetter({node:this.containerNode,_onError:lang.hitch(this,this._onError),onContentError:lang.hitch(this,function(e){
var _411=this.onContentError(e);
try{
this.containerNode.innerHTML=_411;
}
catch(e){
console.error("Fatal "+this.id+" could not change content due to "+e.message,e);
}
})});
}
this._contentSetterParams={adjustPaths:Boolean(this.adjustPaths&&(this.href||this.referencePath)),referencePath:this.href||this.referencePath,renderStyles:this.renderStyles,executeScripts:this.executeScripts,scriptHasHooks:this.scriptHasHooks,scriptHookReplacement:"dijit.byId('"+this.id+"')"};
return this.inherited("_setContent",arguments);
},destroy:function(){
var _412=this._contentSetter;
if(_412){
_412.tearDown();
}
this.inherited(arguments);
}});
});
},"curam/date/locale":function(){
define(["curam/define","dojo/_base/lang","dojo/date/locale"],function(_413,lang,_414){
var _415=_414._getGregorianBundle;
function _416(_417){
var _418=_415(_417);
if(LOCALIZED_MONTH_NAMES){
_418["months-format-abbr"]=LOCALIZED_SHORT_MONTH_NAMES;
_418["months-format-wide"]=LOCALIZED_MONTH_NAMES;
}
return _418;
};
_413.singleton("curam.date.locale",{});
lang.mixin(curam.date.locale,_414);
curam.date.locale.format=function(_419,_41a){
_414._getGregorianBundle=_416;
var _41b=_414.format(_419,_41a);
_414._getGregorianBundle=_415;
return _41b;
};
curam.date.locale.parseDate=function(_41c){
var _41d=new Date(_41c);
return new Date(_41d.getTime()+_41d.getTimezoneOffset()*60000);
};
curam.date.locale.parse=function(_41e,_41f){
_414._getGregorianBundle=_416;
var _420=_414.parse(_41e,_41f);
_414._getGregorianBundle=_415;
return _420;
};
return curam.date.locale;
});
},"curam/util/WordFileEdit":function(){
define(["dojo/has","dojo/dom","dojo/dom-style","dojo/dom-construct","curam/define","curam/debug","dijit/DialogUnderlay"],function(has,dom,_421,_422,_423,_424){
isIE8OrGreater=function(){
return has("ie")>=8||has("trident");
};
_423.singleton("curam.util.WordFileEdit",{_clickedFinish:false,_buttonIdPart:"__o3btn.",searchWindow:null,cantLoadControlMsg:"$unlocalized$ cannot load Word integration control",cantSubmitMsg:"$unlocalized$ cannot submit data",searchWindowTitlePrefix:"SEARCH",useApplet:(function(){
return isIE8OrGreater();
})(),controlAttributes:{},controlParameters:{},submitSaveWordFileEdit:function(_425,_426){
try{
var _427=curam.util.WordFileEdit.getParentWindow();
var _428=curam.util.WordFileEdit._findTextArea(_427,_425);
_428.value=_426;
_427.document.forms[0].submit();
}
catch(e){
alert("Error saving: "+dojo.toJson(e));
}
return;
},openWordFileEditWindow:function(_429,_42a,_42b){
if(curam.util.WordFileEdit.getSearchPage().length>0){
curam.util.WordFileEdit.displaySearchWindow(_429,_42a,_42b);
}else{
curam.util.WordFileEdit.doOpenWordFileEditWindow(_429,_42a,_42b);
}
},doOpenWordFileEditWindow:function(_42c,_42d,_42e){
var _42f=100;
var _430=100;
var _431=Math.floor((screen.width-_42f)/2);
var _432=Math.floor((screen.height-_430)/2);
window.open("../word-file-edit.jsp?id="+_42c+"&document-field="+_42d+"&details-field="+_42e,new Date().valueOf(),"toolbar=no,menubar=no,location=no,scrollbars=no,"+"resizable=no,top="+_432+",left="+_431+",width="+_42f+",height="+_430);
},displaySearchWindow:function(_433,_434,_435,_436){
if(!_436){
_436=0;
}
if(_436>3){
return;
}
if(_436==0){
curam.util.WordFileEdit.searchWindow=window.open("about:blank","searchWindow","left=40000,top=40000,scrollbars=yes");
}
var _437=false;
try{
var _438=curam.util.WordFileEdit.searchWindow.document.title;
if(_438.indexOf(searchWindowTitlePrefix+":")==-1){
curam.util.WordFileEdit.searchWindow.document.title=searchWindowTitlePrefix+":"+_433;
}else{
_437=true;
}
_438=curam.util.WordFileEdit.searchWindow.document.title;
if(!_437&&_438.indexOf(searchWindowTitlePrefix+":")!=-1){
_437=true;
}
}
catch(e){
}
if(!_437){
_436++;
window.setTimeout("displaySearchWindow('"+_433+"','"+_434+"','"+_435+"',"+_436+")",500);
}else{
curam.util.WordFileEdit.doOpenWordFileEditWindow(_433,_434,_435);
}
},redisplaySearchWindow:function(_439,_43a){
if(!_43a){
_43a=0;
}
if(_43a>3){
return;
}
if(_43a==0){
curam.util.WordFileEdit.searchWindow=window.open("about:blank","searchWindow","left=40000,top=40000");
}
var _43b=false;
try{
var _43c=curam.util.WordFileEdit.searchWindow.document.title;
if(_43c.indexOf(searchWindowTitlePrefix+":")==-1){
curam.util.WordFileEdit.searchWindow.document.title=searchWindowTitlePrefix+":"+_439;
}else{
_43b=true;
}
_43c=curam.util.WordFileEdit.searchWindow.document.title;
if(!_43b&&_43c.indexOf(searchWindowTitlePrefix+":")!=-1){
_43b=true;
}
}
catch(e){
}
if(!_43b){
_43a++;
window.setTimeout("redisplaySearchWindow('"+_439+"',"+_43a+")",500);
}
},getSearchPage:function(_43d){
var _43e="";
try{
if(!_43d){
_43e=document.getElementById("searchPage").value;
}else{
var _43f=curam.util.WordFileEdit.getParentWindow();
_43e=_43f.document.getElementById("searchPage").value;
}
}
catch(e){
}
return _43e;
},initialize:function(_440){
var _441=curam.util.WordFileEdit.getParentWindow();
try{
var _442=dom.byId(_440);
if(typeof _442!="undefined"){
curam.util.WordFileEdit._setOverlay(true);
if(curam.util.WordFileEdit.useApplet){
if(!isIE8OrGreater()){
var _443=_441.frameElement;
curam.util.connect(_443,"onload",function(evt){
dojo.fixEvent(evt,_443);
var url=_443.contentWindow.location.href;
try{
_442.mainApplicationPageLoaded(url);
}
catch(e){
alert("Error calling mainApplicationPageLoaded on applet: "+e.message);
}
});
_441.top.dojo.addOnUnload(function(){
_442.mainApplicationPageUnloaded();
});
}
}else{
_442.openDocument();
}
}else{
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
}
catch(e){
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
_441.curam.util.WordFileEdit.cannotLoadControl(e);
}
},_setOverlay:function(_444){
_424.log(_424.getProperty("curam.util.WordFileEdit.version"),"6.0");
},cannotLoadControl:function(e){
var msg=isIE8OrGreater()&&!curam.util.WordFileEdit.useApplet?curam.util.WordFileEdit.cantLoadControlMsgIE:curam.util.WordFileEdit.cantLoadControlMsg;
alert(msg+"\rERROR: "+e.message);
history.go(-1);
},setStatusTextWordFileEditWindow:function(text){
try{
document.getElementById("statustext").innerHTML=text;
}
catch(e){
}
},getWordFileEditParentTextareaValue:function(_445){
var _446="";
try{
var _447=curam.util.WordFileEdit.getParentWindow();
var _448=curam.util.WordFileEdit._findTextArea(_447,_445);
_446=_448.value;
}
catch(e){
alert("getWordFileEditParentTextareaValue('"+_445+"'): \r"+e.message);
}
return _446;
},_findTextArea:function(_449,_44a,_44b){
var _44c=null;
if(!_44b){
_44c=_449.dojo.query("input[name='"+_44a+"']",_449.dojo.body())[0];
}else{
_44c=_449.dojo.query("input[name$='"+_44a+"']",_449.dojo.body())[0];
}
return _44c;
},finishedWordFileEditWindow:function(_44d,_44e,_44f){
if(!curam.util.WordFileEdit._clickedFinish){
curam.util.WordFileEdit.doFinishWordFileEditWindow(_44d,_44e,_44f);
curam.util.WordFileEdit._clickedFinish=true;
}
},doFinishWordFileEditWindow:function(_450,_451,_452){
var _453=false;
var _454=false;
try{
var _455=curam.util.WordFileEdit.getParentWindow();
if(_451&&_452){
_454=true;
var _456=curam.util.WordFileEdit._findTextArea(_455,_451);
_456.value=_452;
}
var _457=_455.dojo.query("form input");
for(var i=0;i<_457.length&&!_453;i++){
if(_457[i].id.substring(0,curam.util.WordFileEdit._buttonIdPart.length).toLowerCase()==curam.util.WordFileEdit._buttonIdPart.toLowerCase()){
_453=true;
if(!_454){
var _456=curam.util.WordFileEdit._findTextArea(_455,_451);
_456.value="";
var _458=false;
var _459;
var _45a=_457[i];
try{
while(_45a.tagName.toUpperCase()!="BODY"&&!_458){
if(_45a.tagName.toUpperCase()=="FORM"){
_458=true;
_459=_45a;
}else{
_45a=_45a.parentElement;
}
}
}
catch(e){
alert("doFinishWordFileEditWindow: "+e.message);
}
if(_458){
var _45b="<input type=\"hidden\" name=\"__o3NoSave\" value=\"true\"/>";
_459.innerHTML+=_45b;
}
}
_455.curam.util.clickButton(_457[i].id);
if(_450.length>0){
_455.document.body.innerHTML=_450;
}
curam.util.WordFileEdit._setOverlay(false);
return;
}
}
if(!_453){
alert(curam.util.WordFileEdit.cantSubmitMsg);
try{
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
catch(e){
}
}
}
catch(e){
alert("doFinishWordFileEditWindow: "+e.message);
curam.util.WordFileEdit._setOverlay(false);
curam.util.WordFileEdit.closeAppletWindow();
}
},screenAlertWordFileEditWindow:function(_45c){
try{
curam.util.WordFileEdit.getParentWindow().alert(_45c);
}
catch(e){
}
},hideSubmitButtons:function(){
dojo.query("a.ac").forEach(function(item){
item.style.display="none";
});
},getParentWindow:function(){
return window.opener;
},getUrls:function(){
try{
var _45d=curam.util.WordFileEdit.getParentWindow();
var doc=_45d.document;
var _45e=doc.URL;
var _45f=_45d.dojo.query("form",doc)[0];
var _460=_45f.action;
var _461=_45e.substr(0,_45e.lastIndexOf("/")+1);
window.curam.util.WordFileEdit.urlPath_return_value=_461;
var _462=isIE8OrGreater()?_460:_461+_460;
window.curam.util.WordFileEdit.allowedUrl_return_value=_462;
return [_461,_462];
}
catch(e){
alert("getUrls: "+dojo.toJson(e));
}
},getTitle:function(){
var _463=curam.util.WordFileEdit.getParentWindow().top.document.title;
curam.util.WordFileEdit.title_return_value=_463;
window.curam_wordIntegration_title_return_value=_463;
return _463;
},setTitle:function(_464){
curam.util.WordFileEdit.getParentWindow().top.document.title=_464;
},hasNamedInput:function(_465){
var _466=curam.util.WordFileEdit.getParentWindow();
var _467=_465.slice(1);
var _468=curam.util.WordFileEdit._findTextArea(_466,_467,true);
return _468?true:false;
},closeAppletWindow:function(){
self.close();
},runApplet:function(id){
if(typeof deployJava!="undefined"){
var _469=deployJava.getPlugin();
if(_469){
_424.log(_424.getProperty("curam.util.WordFileEdit.version"),_469.version);
}else{
_424.log(_424.getProperty("curam.util.WordFileEdit.no.plugin"));
}
}else{
_424.log(_424.getProperty("curam.util.WordFileEdit.no.java"));
}
if(typeof deployJava=="undefined"||(!dojo.isChrome&&!deployJava.isPlugin2())){
alert(curam.util.WordFileEdit.noJavaInstalled);
}else{
dojo.mixin(curam.util.WordFileEdit.controlAttributes,{id:id});
var _46a=_422.create("div",{style:"display:none"});
var _46b=_422.create("applet",curam.util.WordFileEdit.controlAttributes,_46a);
var _46c=curam.util.WordFileEdit.controlParameters;
for(property in _46c){
_422.create("param",{name:property,value:_46c[property]},_46b);
}
var _46d=_46a.innerHTML;
_422.destroy(_46a);
document.write(_46d);
}
}});
return curam.util.WordFileEdit;
});
},"dojo/dnd/Moveable":function(){
define(["../_base/array","../_base/declare","../_base/lang","../dom","../dom-class","../Evented","../on","../topic","../touch","./common","./Mover","../_base/window"],function(_46e,_46f,lang,dom,_470,_471,on,_472,_473,dnd,_474,win){
var _475=_46f("dojo.dnd.Moveable",[_471],{handle:"",delay:0,skip:false,constructor:function(node,_476){
this.node=dom.byId(node);
if(!_476){
_476={};
}
this.handle=_476.handle?dom.byId(_476.handle):null;
if(!this.handle){
this.handle=this.node;
}
this.delay=_476.delay>0?_476.delay:0;
this.skip=_476.skip;
this.mover=_476.mover?_476.mover:_474;
this.events=[on(this.handle,_473.press,lang.hitch(this,"onMouseDown")),on(this.handle,"dragstart",lang.hitch(this,"onSelectStart")),on(this.handle,"selectstart",lang.hitch(this,"onSelectStart"))];
},markupFactory:function(_477,node,Ctor){
return new Ctor(node,_477);
},destroy:function(){
_46e.forEach(this.events,function(_478){
_478.remove();
});
this.events=this.node=this.handle=null;
},onMouseDown:function(e){
if(this.skip&&dnd.isFormElement(e)){
return;
}
if(this.delay){
this.events.push(on(this.handle,_473.move,lang.hitch(this,"onMouseMove")),on(this.handle,_473.release,lang.hitch(this,"onMouseUp")));
this._lastX=e.pageX;
this._lastY=e.pageY;
}else{
this.onDragDetected(e);
}
e.stopPropagation();
e.preventDefault();
},onMouseMove:function(e){
if(Math.abs(e.pageX-this._lastX)>this.delay||Math.abs(e.pageY-this._lastY)>this.delay){
this.onMouseUp(e);
this.onDragDetected(e);
}
e.stopPropagation();
e.preventDefault();
},onMouseUp:function(e){
for(var i=0;i<2;++i){
this.events.pop().remove();
}
e.stopPropagation();
e.preventDefault();
},onSelectStart:function(e){
if(!this.skip||!dnd.isFormElement(e)){
e.stopPropagation();
e.preventDefault();
}
},onDragDetected:function(e){
new this.mover(this.node,e,this);
},onMoveStart:function(_479){
_472.publish("/dnd/move/start",_479);
_470.add(win.body(),"dojoMove");
_470.add(this.node,"dojoMoveItem");
},onMoveStop:function(_47a){
_472.publish("/dnd/move/stop",_47a);
_470.remove(win.body(),"dojoMove");
_470.remove(this.node,"dojoMoveItem");
},onFirstMove:function(){
},onMove:function(_47b,_47c){
this.onMoving(_47b,_47c);
var s=_47b.node.style;
s.left=_47c.l+"px";
s.top=_47c.t+"px";
this.onMoved(_47b,_47c);
},onMoving:function(){
},onMoved:function(){
}});
return _475;
});
},"dojo/store/util/QueryResults":function(){
define(["../../_base/array","../../_base/lang","../../when"],function(_47d,lang,when){
var _47e=function(_47f){
if(!_47f){
return _47f;
}
var _480=!!_47f.then;
if(_480){
_47f=lang.delegate(_47f);
}
function _481(_482){
_47f[_482]=function(){
var args=arguments;
var _483=when(_47f,function(_484){
Array.prototype.unshift.call(args,_484);
return _47e(_47d[_482].apply(_47d,args));
});
if(_482!=="forEach"||_480){
return _483;
}
};
};
_481("forEach");
_481("filter");
_481("map");
if(_47f.total==null){
_47f.total=when(_47f,function(_485){
return _485.length;
});
}
return _47f;
};
lang.setObject("dojo.store.util.QueryResults",_47e);
return _47e;
});
},"curam/util/ui/form/renderer/CTListEditRendererFormEventsAdapter":function(){
define(["dojo/_base/declare","dojo/_base/unload","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter","curam/util/Dropdown"],function(_486,_487,_488,_489){
var _48a=_486("curam.util.ui.form.renderer.CTListEditRendererFormEventsAdapter",_488,{_unsubscribes:[],constructor:function(id,_48b){
this.elementID=id;
this.pathID=_48b;
var _48c=dojo.subscribe("/curam/comboxbox/initialValue",this,function(_48d,_48e){
if(this.getFormElement().id===_48e){
this.getFormElement().value=_48d;
}
_48c.remove();
});
_487.addOnUnload(function(){
this._unsubscribes&&this._unsubscribes.forEach(function(hh){
hh.remove();
});
});
},addChangeListener:function(_48f){
var _490="curam/util/CuramFormsAPI/formChange/combobox".concat(this.getElementID());
this._unsubscribes.push(dojo.subscribe(_490,this,function(_491){
this.getFormElement().value=_491.value;
_48f();
}));
},setFormElementValue:function(_492){
var self=this;
var _493=dojo.subscribe("curam/modal/component/ready",this,function(){
var _494=new _489();
_494.setSelectedOnDropdownIDByCodevalue(self.getElementID(),_492);
this.getFormElement().value=_492;
_493.remove();
});
}});
return _48a;
});
},"curam/pagination/DefaultListModel":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/query","curam/debug","curam/pagination"],function(_495,_496,_497,_498,_499,_49a){
var _49b=_495("curam.pagination.DefaultListModel",null,{_rowCount:null,constructor:function(_49c){
this.tableNode=_498("table.paginated-list-id-"+_49c)[0];
if(!this.tableNode){
throw "Table node for ID "+_49c+" not found - failing!";
}
_499.log("curam.pagination.DefaultListModel "+_499.getProperty("curam.pagination.DefaultListModel"),this.tableNode);
this._id=_49c;
},getId:function(){
return this._id;
},getRowCount:function(){
if(this._rowCount==null){
this._rowCount=0;
var _49d=_498("tbody > script.hidden-list-rows",this.tableNode),frag=document.createDocumentFragment();
for(var i=0;i<_49d.length;i++){
var _49e=_49d[i];
var _49f=(i==_49d.length-1);
if(!_49f){
this._rowCount+=_49a.getNumRowsInBlock(_49e);
}else{
_49a.unpackRows(_49e,frag);
_49e.innerHTML="";
}
}
this.tableNode.tBodies[0].appendChild(frag);
var _4a0=_498("tbody > tr",this.tableNode).length;
this._rowCount+=_4a0;
}
return this._rowCount;
},hideRange:function(_4a1,_4a2){
var rows=this._getRowNodes(_4a1,_4a2);
for(var i=_4a1;i<=_4a2;i++){
_497.set(rows[i-1],{"display":"none"});
_496.remove(rows[i-1],"even-last-row");
_496.remove(rows[i-1],"odd-last-row");
}
},showRange:function(_4a3,_4a4){
var rows=this._getRowNodes(_4a3,_4a4);
var _4a5=(_4a4%2==0)?"even-last-row":"odd-last-row";
_496.add(rows[_4a4-1],_4a5);
for(var i=_4a3;i<=_4a4;i++){
_497.set(rows[i-1],{"display":""});
}
dojo.publish("curam/update/pagination/rows",[rows,this.getId()]);
},_getRowNodes:function(_4a6,_4a7){
var _4a8=_49a.readListContent(this.tableNode);
for(var i=1;i<=_4a7&&i<=_4a8.length;i++){
var node=_4a8[i-1];
if(node.tagName=="SCRIPT"){
_49a.unpackRows(node);
_4a8=_49a.readListContent(this.tableNode);
i--;
}
}
return _498("tbody > tr",this.tableNode);
}});
return _49b;
});
},"dijit/form/DropDownButton":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/query","../registry","../popup","./Button","../_Container","../_HasDropDown","dojo/text!./templates/DropDownButton.html","../a11yclick"],function(_4a9,lang,_4aa,_4ab,_4ac,_4ad,_4ae,_4af,_4b0){
return _4a9("dijit.form.DropDownButton",[_4ad,_4ae,_4af],{baseClass:"dijitDropDownButton",templateString:_4b0,_fillContent:function(){
if(this.srcNodeRef){
var _4b1=_4aa("*",this.srcNodeRef);
this.inherited(arguments,[_4b1[0]]);
this.dropDownContainer=this.srcNodeRef;
}
},startup:function(){
if(this._started){
return;
}
if(!this.dropDown&&this.dropDownContainer){
var _4b2=_4aa("[widgetId]",this.dropDownContainer)[0];
if(_4b2){
this.dropDown=_4ab.byNode(_4b2);
}
delete this.dropDownContainer;
}
if(this.dropDown){
_4ac.hide(this.dropDown);
}
this.inherited(arguments);
},isLoaded:function(){
var _4b3=this.dropDown;
return (!!_4b3&&(!_4b3.href||_4b3.isLoaded));
},loadDropDown:function(_4b4){
var _4b5=this.dropDown;
var _4b6=_4b5.on("load",lang.hitch(this,function(){
_4b6.remove();
_4b4();
}));
_4b5.refresh();
},isFocusable:function(){
return this.inherited(arguments)&&!this._mouseDown;
}});
});
},"dojo/regexp":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
var _4b7={};
lang.setObject("dojo.regexp",_4b7);
_4b7.escapeString=function(str,_4b8){
return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+\-^])/g,function(ch){
if(_4b8&&_4b8.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
_4b7.buildGroupRE=function(arr,re,_4b9){
if(!(arr instanceof Array)){
return re(arr);
}
var b=[];
for(var i=0;i<arr.length;i++){
b.push(re(arr[i]));
}
return _4b7.group(b.join("|"),_4b9);
};
_4b7.group=function(_4ba,_4bb){
return "("+(_4bb?"?:":"")+_4ba+")";
};
return _4b7;
});
},"curam/layout/TabContainer":function(){
define(["dojo/_base/declare","dijit/layout/TabContainer","dojo/text!curam/layout/resources/TabContainer.html","curam/inspection/Layer","dojo/_base/connect","curam/layout/ScrollingTabController","dijit/layout/TabController"],function(_4bc,_4bd,_4be,_4bf,_4c0,_4c1,_4c2){
var _4c3=_4bc("curam.layout.TabContainer",dijit.layout.TabContainer,{templateString:_4be,_theSelectedTabIndex:0,_thePage:null,_theChildren:null,postCreate:function(){
this.inherited(arguments);
var tl=this.tablist;
this.connect(tl,"onRemoveChild","_changeTab");
this.connect(tl,"onSelectChild","updateTabTitle");
_4bf.register("curam/layout/TabContainer",this);
},selectChild:function(page,_4c4){
this.inherited(arguments);
dojo.publish("curam/tab/selected",[page.id]);
},updateTabTitle:function(){
curam.util.setBrowserTabTitle();
},_changeTab:function(){
if(this._beingDestroyed){
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren==null){
return;
}
if(this._theChildren[this._theSelectedTabIndex]!=this._thePage){
this.selectChild(this._theChildren[this._theSelectedTabIndex]);
this._thePage=null;
this._theChildren=null;
return;
}
if(this._theChildren.length<1){
this._thePage=null;
return;
}else{
if(this._theChildren.length==1){
this.selectChild(this._theChildren[this._theChildren.length-1]);
this._thePage=null;
this._theChildren=null;
}else{
if(this._theSelectedTabIndex==(this._theChildren.length-1)){
this.selectChild(this._theChildren[this._theChildren.length-2]);
}else{
if(this._theSelectedTabIndex==0){
this.selectChild(this._theChildren[1]);
}else{
if(this._theChildren.length>2){
this.selectChild(this._theChildren[this._theSelectedTabIndex+1]);
}
}
}
this._thePage=null;
this._theChildren=null;
}
}
},removeChild:function(page){
if(this._started&&!this._beingDestroyed){
var _4c5=this.getChildren();
var i=0;
var _4c6=0;
for(i=0;i<_4c5.length;i++){
if(_4c5[i].get("selected")){
_4c6=i;
break;
}
}
this._theSelectedTabIndex=_4c6;
this._thePage=page;
this._theChildren=_4c5;
var _4c7=page.id;
_4c0.publish("/curam/tab/closing",[_4c7]);
}
this.inherited(arguments);
},postMixInProperties:function(){
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_4c1:_4c2;
}
this.inherited(arguments);
}});
return _4c3;
});
},"curam/ui/SectionShortcutsPanel":function(){
define(["curam/inspection/Layer","curam/define","curam/tab","curam/util","curam/ui/UIController"],function(_4c8){
var _4c9=curam.define.singleton("curam.ui.SectionShortcutsPanel",{handleClickOnAnchorElement:function(_4ca,_4cb){
if(!_4cb){
curam.tab.getTabController().handleUIMPageID(_4ca);
}else{
curam.ui.SectionShortcutsPanel.openInModal(_4ca);
}
},handleClick:function(_4cc,item){
var _4cd=eval(_4cc+"JsonStore");
var _4ce=_4cd.getValue(item,"pageID");
var _4cf=_4cd.getValue(item,"openInModal");
if(!_4cf){
curam.tab.getTabController().handleUIMPageID(_4ce);
}else{
curam.ui.SectionShortcutsPanel.openInModal(_4ce);
}
},openInModal:function(_4d0){
var _4d1=_4d0+"Page.do";
var _4d2={};
curam.tab.getTabController().handleLinkClick(_4d1,_4d2);
},setupCleanupScript:function(_4d3){
dojo.ready(function(){
var _4d4=eval(_4d3+"JsonStore");
dojo.addOnWindowUnload(function(){
_4d4.close();
});
});
}});
_4c8.register("curam/ui/SectionShortcutsPanel",this);
return _4c9;
});
},"curam/cdsl/_base/_StructBase":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/json","curam/cdsl/types/FrequencyPattern"],function(_4d5,lang,json,_4d6){
var _4d7={bareInput:false,fixups:null,metadataRegistry:null,dataAdapter:null};
var _4d8=function(_4d9){
var o=lang.clone(_4d7);
return lang.mixin(o,_4d9);
};
var _4da={onRequest:{onItem:function(path,data){
return data;
},onStruct:function(_4db){
},},onResponse:{onItem:function(path,data){
return data;
},onStruct:function(_4dc){
},}};
var _4dd=function(frag,_4de){
if(frag){
return frag+"."+_4de;
}
return _4de;
};
var _4df=_4d5(null,{_data:null,_converter:null,_dataAdapter:null,constructor:function(data,opts){
if(!data){
throw new Error("Missing parameter.");
}
if(typeof data!=="object"){
throw new Error("Wrong parameter type: "+typeof data);
}
var _4e0=_4d8(opts);
if(!_4e0.bareInput){
this._data=this._typedToBare(data);
}else{
this.setDataAdapter(_4e0.dataAdapter);
this._data=this._bareToTyped(data);
if(_4e0.fixups){
this._applyFixUps(_4e0.fixups,this._data,_4e0.metadataRegistry);
}
}
},_applyFixUps:function(_4e1,data,_4e2){
dojo.forEach(_4e1,function(item,_4e3){
var path=_4e1[_4e3].path;
var type=_4e1[_4e3].type;
this._processFixUp(data,path,this._getTransformFunction(type,_4e2));
},this);
},_processFixUp:function(data,path,_4e4){
if(path.length==1){
data[path[0]]=_4e4(data[path[0]]);
return;
}else{
if(lang.isArray(data[path[0]])){
dojo.forEach(data[path[0]],function(item,_4e5){
this._processFixUp(item,path.slice(1,path.length),_4e4);
},this);
}else{
this._processFixUp(data[path[0]],path.slice(1,path.length),_4e4);
}
}
},_getTransformFunction:function(type,_4e6){
if(type[0]==="frequencypattern"){
return function(data){
return new _4d6(data.code,data.description);
};
}else{
if(type[0]==="datetime"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="date"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="time"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="codetable"){
if(type.length<2){
throw new Error("Missing codetable name, type specified is: "+type);
}
return function(data){
var _4e7=_4e6.codetables()[type[1]];
if(_4e7){
return _4e7.getItem(data);
}else{
throw new Error("Codetable does not exist: codetable name="+type[1]);
}
};
}else{
throw new Error("Unsupported type: "+type);
}
}
}
}
}
},toJson:function(){
return json.stringify(this.getData());
},getData:function(){
for(var name in this._data){
this._data[name]=this[name];
}
return this._typedToBare(this._data);
},_bareToTyped:function(data,path){
if(lang.isObject(data)){
var _4e8={};
this._applyResponseStructAdapter(data);
for(var prop in data){
if(lang.isArray(data[prop])){
_4e8[prop]=[];
for(var i=0;i<data[prop].length;i++){
_4e8[prop].push(this._bareToTyped(data[prop][i],_4dd(path,prop+"["+i+"]")));
}
}else{
if(typeof data[prop]==="object"){
_4e8[prop]=this._bareToTyped(data[prop],_4dd(path,prop));
}else{
_4e8[prop]=data[prop];
}
}
var _4e9=_4dd(path,prop);
_4e8[prop]=this._applyResponseDataAdapter(_4e9,_4e8[prop]);
}
return _4e8;
}
return this._applyResponseDataAdapter(path,data);
},_typedToBare:function(data,path){
if(lang.isObject(data)){
var _4ea={};
for(var prop in data){
if(data.hasOwnProperty(prop)&&"_data"!==prop&&"_dataAdapter"!==prop&&"_inherited"!==prop&&"_converter"!==prop){
if(lang.isArray(data[prop])){
_4ea[prop]=[];
for(var i=0;i<data[prop].length;i++){
_4ea[prop].push(this._typedToBare(data[prop][i],_4dd(path,prop+"["+i+"]")));
}
}else{
if(data[prop].getDescription&&data[prop].getCode){
_4ea[prop]=data[prop].getCode();
}else{
if(data[prop].getTime){
_4ea[prop]=data[prop].getTime();
}else{
if(typeof data[prop]==="object"){
_4ea[prop]=this._typedToBare(data[prop],_4dd(path,prop));
}else{
_4ea[prop]=data[prop];
}
}
}
}
var _4eb=_4dd(path,prop);
_4ea[prop]=this._applyRequestDataAdapter(_4eb,_4ea[prop]);
}
}
this._applyRequestStructAdapter(_4ea);
return _4ea;
}
return this._applyRequestDataAdapter(path,data);
},setDataAdapter:function(_4ec){
if(_4ec){
var a=lang.clone(_4da);
if(_4ec.onRequest&&_4ec.onRequest.onItem){
a.onRequest.onItem=_4ec.onRequest.onItem;
}
if(_4ec.onRequest&&_4ec.onRequest.onStruct){
a.onRequest.onStruct=_4ec.onRequest.onStruct;
}
if(_4ec.onResponse&&_4ec.onResponse.onItem){
a.onResponse.onItem=_4ec.onResponse.onItem;
}
if(_4ec.onResponse&&_4ec.onResponse.onStruct){
a.onResponse.onStruct=_4ec.onResponse.onStruct;
}
this._dataAdapter=a;
}else{
this._dataAdapter=null;
}
},_applyRequestDataAdapter:function(path,_4ed){
if(this._dataAdapter){
return this._dataAdapter.onRequest.onItem(path,_4ed);
}
return _4ed;
},_applyResponseDataAdapter:function(path,_4ee){
if(this._dataAdapter){
return this._dataAdapter.onResponse.onItem(path,_4ee);
}
return _4ee;
},_applyRequestStructAdapter:function(_4ef){
if(this._dataAdapter){
this._dataAdapter.onRequest.onStruct(_4ef);
}
},_applyResponseStructAdapter:function(_4f0){
if(this._dataAdapter){
this._dataAdapter.onResponse.onStruct(_4f0);
}
}});
return _4df;
});
},"dojo/debounce":function(){
define([],function(){
return function(cb,wait){
var _4f1;
return function(){
if(_4f1){
clearTimeout(_4f1);
}
var self=this;
var a=arguments;
_4f1=setTimeout(function(){
cb.apply(self,a);
},wait);
};
};
});
},"dojox/widget/Standby":function(){
define(["dojo/_base/kernel","dojo/_base/declare","dojo/_base/array","dojo/_base/event","dojo/_base/sniff","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/window","dojo/_base/window","dojo/_base/fx","dojo/fx","dijit/_Widget","dijit/_TemplatedMixin","dijit/registry"],function(_4f2,_4f3,_4f4,_4f5,has,dom,attr,_4f6,_4f7,_4f8,_4f9,_4fa,_4fb,fx,_4fc,_4fd,_4fe){
_4f2.experimental("dojox.widget.Standby");
return _4f3("dojox.widget.Standby",[_4fc,_4fd],{image:require.toUrl("dojox/widget/Standby/images/loading.gif").toString(),imageText:"Please Wait...",text:"Please wait...",centerIndicator:"image",target:"",color:"#C0C0C0",duration:500,zIndex:"auto",opacity:0.75,templateString:"<div>"+"<div style=\"display: none; opacity: 0; z-index: 9999; "+"position: absolute; cursor:wait;\" dojoAttachPoint=\"_underlayNode\"></div>"+"<img src=\"${image}\" style=\"opacity: 0; display: none; z-index: -10000; "+"position: absolute; top: 0px; left: 0px; cursor:wait;\" "+"dojoAttachPoint=\"_imageNode\">"+"<div style=\"opacity: 0; display: none; z-index: -10000; position: absolute; "+"top: 0px;\" dojoAttachPoint=\"_textNode\"></div>"+"</div>",_underlayNode:null,_imageNode:null,_textNode:null,_centerNode:null,_displayed:false,_resizeCheck:null,_started:false,_parent:null,startup:function(args){
if(!this._started){
if(typeof this.target==="string"){
var w=_4fe.byId(this.target);
this.target=w?w.domNode:dom.byId(this.target);
}
if(this.text){
this._textNode.innerHTML=this.text;
}
if(this.centerIndicator==="image"){
this._centerNode=this._imageNode;
attr.set(this._imageNode,"src",this.image);
attr.set(this._imageNode,"alt",this.imageText);
}else{
this._centerNode=this._textNode;
}
_4f8.set(this._underlayNode,{display:"none",backgroundColor:this.color});
_4f8.set(this._centerNode,"display","none");
this.connect(this._underlayNode,"onclick","_ignore");
if(this.domNode.parentNode&&this.domNode.parentNode!=_4fa.body()){
_4fa.body().appendChild(this.domNode);
}
if(has("ie")==7){
this._ieFixNode=_4f6.create("div");
_4f8.set(this._ieFixNode,{opacity:"0",zIndex:"-1000",position:"absolute",top:"-1000px"});
_4fa.body().appendChild(this._ieFixNode);
}
this.inherited(arguments);
}
},show:function(){
if(!this._displayed){
if(this._anim){
this._anim.stop();
delete this._anim;
}
this._displayed=true;
this._size();
this._disableOverflow();
this._fadeIn();
}
},hide:function(){
if(this._displayed){
try{
if(this._anim){
this._anim.stop();
delete this._anim;
}
this._size();
}
catch(e){
console.error(e);
}
finally{
this._fadeOut();
this._displayed=false;
if(this._resizeCheck!==null){
clearInterval(this._resizeCheck);
this._resizeCheck=null;
}
}
}
},isVisible:function(){
return this._displayed;
},onShow:function(){
},onHide:function(){
},uninitialize:function(){
this._displayed=false;
if(this._resizeCheck){
clearInterval(this._resizeCheck);
}
_4f8.set(this._centerNode,"display","none");
_4f8.set(this._underlayNode,"display","none");
if(has("ie")==7&&this._ieFixNode){
_4fa.body().removeChild(this._ieFixNode);
delete this._ieFixNode;
}
if(this._anim){
this._anim.stop();
delete this._anim;
}
this.target=null;
this._imageNode=null;
this._textNode=null;
this._centerNode=null;
this.inherited(arguments);
},_size:function(){
if(this._displayed){
var dir=attr.get(_4fa.body(),"dir");
if(dir){
dir=dir.toLowerCase();
}
var _4ff;
var _500=this._scrollerWidths();
var _501=this.target;
var _502=_4f8.get(this._centerNode,"display");
_4f8.set(this._centerNode,"display","block");
var box=_4f7.position(_501,true);
if(_501===_4fa.body()||_501===_4fa.doc){
box=_4f9.getBox();
box.x=box.l;
box.y=box.t;
}
var _503=_4f7.getMarginBox(this._centerNode);
_4f8.set(this._centerNode,"display",_502);
if(this._ieFixNode){
_4ff=-this._ieFixNode.offsetTop/1000;
box.x=Math.floor((box.x+0.9)/_4ff);
box.y=Math.floor((box.y+0.9)/_4ff);
box.w=Math.floor((box.w+0.9)/_4ff);
box.h=Math.floor((box.h+0.9)/_4ff);
}
var zi=_4f8.get(_501,"zIndex");
var ziUl=zi;
var ziIn=zi;
if(this.zIndex==="auto"){
if(zi!="auto"){
ziUl=parseInt(ziUl,10)+1;
ziIn=parseInt(ziIn,10)+2;
}else{
var _504=_501;
if(_504&&_504!==_4fa.body()&&_504!==_4fa.doc){
_504=_501.parentNode;
var _505=-100000;
while(_504&&_504!==_4fa.body()){
zi=_4f8.get(_504,"zIndex");
if(!zi||zi==="auto"){
_504=_504.parentNode;
}else{
var _506=parseInt(zi,10);
if(_505<_506){
_505=_506;
ziUl=_506+1;
ziIn=_506+2;
}
_504=_504.parentNode;
}
}
}
}
}else{
ziUl=parseInt(this.zIndex,10)+1;
ziIn=parseInt(this.zIndex,10)+2;
}
_4f8.set(this._centerNode,"zIndex",ziIn);
_4f8.set(this._underlayNode,"zIndex",ziUl);
var pn=_501.parentNode;
if(pn&&pn!==_4fa.body()&&_501!==_4fa.body()&&_501!==_4fa.doc){
var obh=box.h;
var obw=box.w;
var _507=_4f7.position(pn,true);
if(this._ieFixNode){
_4ff=-this._ieFixNode.offsetTop/1000;
_507.x=Math.floor((_507.x+0.9)/_4ff);
_507.y=Math.floor((_507.y+0.9)/_4ff);
_507.w=Math.floor((_507.w+0.9)/_4ff);
_507.h=Math.floor((_507.h+0.9)/_4ff);
}
_507.w-=pn.scrollHeight>pn.clientHeight&&pn.clientHeight>0?_500.v:0;
_507.h-=pn.scrollWidth>pn.clientWidth&&pn.clientWidth>0?_500.h:0;
if(dir==="rtl"){
if(has("opera")){
box.x+=pn.scrollHeight>pn.clientHeight&&pn.clientHeight>0?_500.v:0;
_507.x+=pn.scrollHeight>pn.clientHeight&&pn.clientHeight>0?_500.v:0;
}else{
if(has("ie")){
_507.x+=pn.scrollHeight>pn.clientHeight&&pn.clientHeight>0?_500.v:0;
}else{
if(has("webkit")){
}
}
}
}
if(_507.w<box.w){
box.w=box.w-_507.w;
}
if(_507.h<box.h){
box.h=box.h-_507.h;
}
var _508=_507.y;
var _509=_507.y+_507.h;
var bTop=box.y;
var _50a=box.y+obh;
var _50b=_507.x;
var _50c=_507.x+_507.w;
var _50d=box.x;
var _50e=box.x+obw;
var _50f;
if(_50a>_508&&bTop<_508){
box.y=_507.y;
_50f=_508-bTop;
var _510=obh-_50f;
if(_510<_507.h){
box.h=_510;
}else{
box.h-=2*(pn.scrollWidth>pn.clientWidth&&pn.clientWidth>0?_500.h:0);
}
}else{
if(bTop<_509&&_50a>_509){
box.h=_509-bTop;
}else{
if(_50a<=_508||bTop>=_509){
box.h=0;
}
}
}
if(_50e>_50b&&_50d<_50b){
box.x=_507.x;
_50f=_50b-_50d;
var _511=obw-_50f;
if(_511<_507.w){
box.w=_511;
}else{
box.w-=2*(pn.scrollHeight>pn.clientHeight&&pn.clientHeight>0?_500.w:0);
}
}else{
if(_50d<_50c&&_50e>_50c){
box.w=_50c-_50d;
}else{
if(_50e<=_50b||_50d>=_50c){
box.w=0;
}
}
}
}
if(box.h>0&&box.w>0){
_4f8.set(this._underlayNode,{display:"block",width:box.w+"px",height:box.h+"px",top:box.y+"px",left:box.x+"px"});
var _512=["borderRadius","borderTopLeftRadius","borderTopRightRadius","borderBottomLeftRadius","borderBottomRightRadius"];
this._cloneStyles(_512);
if(!has("ie")){
_512=["MozBorderRadius","MozBorderRadiusTopleft","MozBorderRadiusTopright","MozBorderRadiusBottomleft","MozBorderRadiusBottomright","WebkitBorderRadius","WebkitBorderTopLeftRadius","WebkitBorderTopRightRadius","WebkitBorderBottomLeftRadius","WebkitBorderBottomRightRadius"];
this._cloneStyles(_512,this);
}
var _513=(box.h/2)-(_503.h/2);
var _514=(box.w/2)-(_503.w/2);
if(box.h>=_503.h&&box.w>=_503.w){
_4f8.set(this._centerNode,{top:(_513+box.y)+"px",left:(_514+box.x)+"px",display:"block"});
}else{
_4f8.set(this._centerNode,"display","none");
}
}else{
_4f8.set(this._underlayNode,"display","none");
_4f8.set(this._centerNode,"display","none");
}
if(this._resizeCheck===null){
var self=this;
this._resizeCheck=setInterval(function(){
self._size();
},100);
}
}
},_cloneStyles:function(list){
_4f4.forEach(list,function(s){
_4f8.set(this._underlayNode,s,_4f8.get(this.target,s));
},this);
},_fadeIn:function(){
var self=this;
var _515=_4fb.animateProperty({duration:self.duration,node:self._underlayNode,properties:{opacity:{start:0,end:self.opacity}}});
var _516=_4fb.animateProperty({duration:self.duration,node:self._centerNode,properties:{opacity:{start:0,end:1}},onEnd:function(){
self.onShow();
delete self._anim;
}});
this._anim=fx.combine([_515,_516]);
this._anim.play();
},_fadeOut:function(){
var self=this;
var _517=_4fb.animateProperty({duration:self.duration,node:self._underlayNode,properties:{opacity:{start:self.opacity,end:0}},onEnd:function(){
_4f8.set(this.node,{"display":"none","zIndex":"-1000"});
}});
var _518=_4fb.animateProperty({duration:self.duration,node:self._centerNode,properties:{opacity:{start:1,end:0}},onEnd:function(){
_4f8.set(this.node,{"display":"none","zIndex":"-1000"});
self.onHide();
self._enableOverflow();
delete self._anim;
}});
this._anim=fx.combine([_517,_518]);
this._anim.play();
},_ignore:function(e){
if(e){
_4f5.stop(e);
}
},_scrollerWidths:function(){
var div=_4f6.create("div");
_4f8.set(div,{position:"absolute",opacity:0,overflow:"hidden",width:"50px",height:"50px",zIndex:"-100",top:"-200px",padding:"0px",margin:"0px"});
var iDiv=_4f6.create("div");
_4f8.set(iDiv,{width:"200px",height:"10px"});
div.appendChild(iDiv);
_4fa.body().appendChild(div);
var b=_4f7.getContentBox(div);
_4f8.set(div,"overflow","scroll");
var a=_4f7.getContentBox(div);
_4fa.body().removeChild(div);
return {v:b.w-a.w,h:b.h-a.h};
},_setTextAttr:function(text){
this._textNode.innerHTML=text;
this.text=text;
},_setColorAttr:function(c){
_4f8.set(this._underlayNode,"backgroundColor",c);
this.color=c;
},_setImageTextAttr:function(text){
attr.set(this._imageNode,"alt",text);
this.imageText=text;
},_setImageAttr:function(url){
attr.set(this._imageNode,"src",url);
this.image=url;
},_setCenterIndicatorAttr:function(_519){
this.centerIndicator=_519;
if(_519==="image"){
this._centerNode=this._imageNode;
_4f8.set(this._textNode,"display","none");
}else{
this._centerNode=this._textNode;
_4f8.set(this._imageNode,"display","none");
}
},_disableOverflow:function(){
if(this.target===_4fa.body()||this.target===_4fa.doc){
this._overflowDisabled=true;
var body=_4fa.body();
if(body.style&&body.style.overflow){
this._oldOverflow=_4f8.get(body,"overflow");
}else{
this._oldOverflow="";
}
if(has("ie")&&!has("quirks")){
if(body.parentNode&&body.parentNode.style&&body.parentNode.style.overflow){
this._oldBodyParentOverflow=body.parentNode.style.overflow;
}else{
try{
this._oldBodyParentOverflow=_4f8.get(body.parentNode,"overflow");
}
catch(e){
this._oldBodyParentOverflow="scroll";
}
}
_4f8.set(body.parentNode,"overflow","hidden");
}
_4f8.set(body,"overflow","hidden");
}
},_enableOverflow:function(){
if(this._overflowDisabled){
delete this._overflowDisabled;
var body=_4fa.body();
if(has("ie")&&!has("quirks")){
body.parentNode.style.overflow=this._oldBodyParentOverflow;
delete this._oldBodyParentOverflow;
}
_4f8.set(body,"overflow",this._oldOverflow);
if(has("webkit")){
var div=_4f6.create("div",{style:{height:"2px"}});
body.appendChild(div);
setTimeout(function(){
body.removeChild(div);
},0);
}
delete this._oldOverflow;
}
}});
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(_51a,lang){
var _51b=/[&<>'"\/]/g;
var _51c={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;","/":"&#x2F;"};
var _51d={};
lang.setObject("dojo.string",_51d);
_51d.escape=function(str){
if(!str){
return "";
}
return str.replace(_51b,function(c){
return _51c[c];
});
};
_51d.rep=function(str,num){
if(num<=0||!str){
return "";
}
var buf=[];
for(;;){
if(num&1){
buf.push(str);
}
if(!(num>>=1)){
break;
}
str+=str;
}
return buf.join("");
};
_51d.pad=function(text,size,ch,end){
if(!ch){
ch="0";
}
var out=String(text),pad=_51d.rep(ch,Math.ceil((size-out.length)/ch.length));
return end?out+pad:pad+out;
};
_51d.substitute=function(_51e,map,_51f,_520){
_520=_520||_51a.global;
_51f=_51f?lang.hitch(_520,_51f):function(v){
return v;
};
return _51e.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_521,key,_522){
var _523=lang.getObject(key,false,map);
if(_522){
_523=lang.getObject(_522,false,_520).call(_520,_523,key);
}
return _51f(_523,key).toString();
});
};
_51d.trim=String.prototype.trim?lang.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return _51d;
});
},"dijit/_Contained":function(){
define(["dojo/_base/declare","./registry"],function(_524,_525){
return _524("dijit._Contained",null,{_getSibling:function(_526){
var p=this.getParent();
return (p&&p._getSiblingOfChild&&p._getSiblingOfChild(this,_526=="previous"?-1:1))||null;
},getPreviousSibling:function(){
return this._getSibling("previous");
},getNextSibling:function(){
return this._getSibling("next");
},getIndexInParent:function(){
var p=this.getParent();
if(!p||!p.getIndexOfChild){
return -1;
}
return p.getIndexOfChild(this);
}});
});
},"curam/util/UIMFragment":function(){
define(["curam/util/Request","curam/define","curam/debug","curam/util/ScreenContext"],function(_527){
curam.define.singleton("curam.util.UIMFragment",{get:function(args){
var _528=args&&args.pageID;
var url=args&&args.url;
var _529=args&&args.params;
var _52a=args&&args.onLoad;
var _52b=args&&args.onDownloadError;
var _52c=args&&args.targetID;
if(_52c===""||typeof _52c==="undefined"){
throw "UIMFragment: targetID must be set.";
}
var _52d=null;
if(url){
_52d=url;
}else{
_52d=curam.util.UIMFragment._constructPath(_528)+curam.util.UIMFragment._addCDEJParameters()+curam.util.UIMFragment._encodeParameters(_529);
}
curam.debug.log("UIMFragment: GET to "+_52d);
curam.util.UIMFragment._doService(_52d,_52c,args,_52a,_52b);
},submitForm:function(_52e){
var _52e=dojo.fixEvent(_52e);
var _52f=_52e.target;
dojo.stopEvent(_52e);
var _530={url:curam.util.UIMFragment._constructFormActionPath(_52f),form:_52f,load:function(data){
var cp=dijit.getEnclosingWidget(_52f);
cp.set("content",data);
},error:function(_531){
alert("form error: error!!");
}};
_527.post(_530);
console.log(_52e+" "+_52f);
},_constructFormActionPath:function(_532){
var _533="";
if(window===window.top){
_533=curam.config.locale+"/";
}
return _533+_532.getAttribute("action");
},_initForm:function(_534){
var _535=dojo.query("form",dijit.byId(_534).domNode)[0];
if(_535){
dojo.connect(_535,"onsubmit",curam.util.UIMFragment.submitForm);
}
},_constructPath:function(_536){
var _537=window;
var _538=window.top;
return curam.util.UIMFragment._constructPathValue(_536,_537,_538);
},_constructPathValue:function(_539,_53a,_53b){
if(_539===""||typeof _539==="undefined"){
throw "UIMFragment: pageID must be set.";
}
var _53c="";
if(_53a.location.pathname===_53b.location.pathname){
var _53d=_53b.curam&&_53b.curam.config&&_53b.curam.config.locale;
_53c=(_53d||"en")+"/";
}
return _53c+_539+"Page.do";
},_encodeParameters:function(_53e){
if(typeof _53e==="undefined"||dojo.toJson(_53e)==="{}"){
curam.debug.log("UIMFragment: No params included in request.");
return "";
}
var _53f=[];
for(var _540 in _53e){
_53f.push(_540+"="+encodeURIComponent(_53e[_540]));
}
return "&"+_53f.join("&");
},_addCDEJParameters:function(){
return "?"+jsScreenContext.toRequestString();
},_doService:function(url,_541,args,_542,_543){
var cp=dijit.byId(_541);
cp.onLoad=dojo.hitch(cp,curam.util.UIMFragment._handleLoadSuccess,args,_542);
cp.preventCache=true;
cp.set("href",url);
},_handleDownloadError:function(_544){
curam.debug.log("Error invoking the UIMFragment: "+_544);
return "UIMFragment: Generic Error Handler";
},_handleLoadSuccess:function(_545,_546){
curam.util.UIMFragment._initForm(_545.targetID);
if(_546){
_546(this);
}
curam.debug.log("");
return "UIMFragment: Generic Success Handler";
}});
return curam.util.UIMFragment;
});
},"curam/tab/TabDescriptor":function(){
define(["dojo/_base/declare","curam/tab/TabSessionManager","curam/debug"],function(_547,_548,_549){
var _54a=_547("curam.tab.TabDescriptor",null,{constructor:function(_54b,_54c){
this.sectionID=_54b?_54b:null;
this.tabID=_54c?_54c:null;
this.tabSignature=null;
this.tabContent=null;
this.tabParamNames=null;
this.isHomePage=false;
this.foregroundTabContent=null;
this.restoreModalInd=false;
},toJson:function(){
var _54d={"sectionID":this.sectionID,"tabID":this.tabID,"tabSignature":this.tabSignature,"tabParamNames":this.tabParamNames,"isHomePage":this.isHomePage};
_54d.tabContent=this.tabContent?this.tabContent:null;
_54d.foregroundTabContent=this.foregroundTabContent?this.foregroundTabContent:null;
_54d.restoreModalInd=this.restoreModalInd?this.restoreModalInd:false;
return dojo.toJson(_54d);
},setTabContent:function(_54e,_54f){
if(this.tabContent){
this._log(_549.getProperty("curam.tab.TabDescriptor.content.changed"));
}else{
this._log(_549.getProperty("curam.tab.TabDescriptor.content.set"));
}
var _550=dojo.clone(_54e.parameters);
dojo.mixin(_550,_54e.cdejParameters);
if(!this.tabContent){
this.tabContent={};
}
this.tabContent.parameters=_550;
this.tabContent.pageID=_54e.pageID;
if(_54f){
this.tabContent.tabName=_54f;
}else{
if(!this.tabContent.tabName){
this.tabContent.tabName="";
}
}
this._save();
dojo.publish("/curam/tab/labelUpdated");
},setTabSignature:function(_551,_552,_553){
if(!this.tabSignature){
this.tabParamNames=_551.slice(0);
this.tabParamNames.sort();
this.tabSignature=this._generateSignature(this.tabID,this.tabParamNames,_552);
this._log(_549.getProperty("curam.tab.TabDescriptor.signature.set"));
this._save();
if(!_553){
this._select();
}
}else{
this._log(_549.getProperty("curam.tab.TabDescriptor.signature.not.set"));
}
},matchesPageRequest:function(_554){
return this.tabSignature&&this.tabSignature==this._generateSignature(this.tabID,this.tabParamNames,_554);
},_generateSignature:function(_555,_556,_557){
var _558=_555;
if(_556){
for(var i=0;i<_556.length;i++){
var name=_556[i];
if(_557.parameters[name]){
_558+="|"+name+"="+_557.parameters[name];
}
}
}
return _558;
},_save:function(){
if(this.tabContent&&this.tabSignature){
this._log(_549.getProperty("curam.tab.TabDescriptor.saving"));
new _548().tabUpdated(this);
}
},_select:function(){
if(this.tabSignature){
this._log(_549.getProperty("curam.tab.TabDescriptor.selecting"));
new _548().tabSelected(this);
}
},_log:function(msg){
if(curam.debug.enabled()){
curam.debug.log("TAB DESCRIPTOR: "+msg+" ["+this.toJson()+"]");
}
}});
dojo.mixin(curam.tab.TabDescriptor,{fromJson:function(_559){
var _55a=null;
if(_559){
var _55b=dojo.fromJson(_559);
var _55a=new curam.tab.TabDescriptor(_55b.sectionID,_55b.tabID);
if(_55b.tabSignature){
_55a.tabSignature=_55b.tabSignature;
}
if(_55b.tabContent){
_55a.tabContent=_55b.tabContent;
}
if(_55b.tabParamNames){
_55a.tabParamNames=_55b.tabParamNames;
}
if(_55b.isHomePage){
_55a.isHomePage=_55b.isHomePage;
}
if(_55b.foregroundTabContent){
_55a.foregroundTabContent=_55b.foregroundTabContent;
}
if(_55b.restoreModalInd){
_55a.restoreModalInd=_55b.restoreModalInd;
}
}
return _55a;
}});
return _54a;
});
},"dijit/_Container":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","dojo/_base/kernel"],function(_55c,_55d,_55e,_55f){
return _55d("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_560,_561){
var _562=this.containerNode;
if(_561>0){
_562=_562.firstChild;
while(_561>0){
if(_562.nodeType==1){
_561--;
}
_562=_562.nextSibling;
}
if(_562){
_561="before";
}else{
_562=this.containerNode;
_561="last";
}
}
_55e.place(_560.domNode,_562,_561);
if(this._started&&!_560._started){
_560.startup();
}
},removeChild:function(_563){
if(typeof _563=="number"){
_563=this.getChildren()[_563];
}
if(_563){
var node=_563.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_564,dir){
var _565=this.getChildren(),idx=_55c.indexOf(_565,_564);
return _565[idx+dir];
},getIndexOfChild:function(_566){
return _55c.indexOf(this.getChildren(),_566);
}});
});
},"curam/util/ui/form/renderer/CheckboxEditRendererFormEventsAdapter":function(){
define(["dojo/_base/declare","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter"],function(_567,_568){
var _569=_567("curam.util.ui.form.renderer.CheckboxEditRendererFormEventsAdapter",_568,{setFormElementValue:function(_56a){
this.element.checked=_56a;
},getFormElementValue:function(){
return this.getFormElement().checked;
},});
return _569;
});
},"dijit/layout/BorderContainer":function(){
define(["dojo/_base/array","dojo/cookie","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","../_WidgetBase","../_Widget","../_TemplatedMixin","./LayoutContainer","./utils"],function(_56b,_56c,_56d,_56e,_56f,_570,_571,keys,lang,on,_572,_573,_574,_575,_576,_577){
var _578=_56d("dijit.layout._Splitter",[_574,_575],{live:true,templateString:"<div class=\"dijitSplitter\" data-dojo-attach-event=\"onkeydown:_onKeyDown,press:_startDrag,onmouseenter:_onMouse,onmouseleave:_onMouse\" tabIndex=\"0\"><div class=\"dijitSplitterThumb\"></div></div>",constructor:function(){
this._handlers=[];
},postMixInProperties:function(){
this.inherited(arguments);
this.horizontal=/top|bottom/.test(this.region);
this._factor=/top|left/.test(this.region)?1:-1;
this._cookieName=this.container.id+"_"+this.region;
},buildRendering:function(){
this.inherited(arguments);
_56e.add(this.domNode,"dijitSplitter"+(this.horizontal?"H":"V"));
if(this.container.persist){
var _579=_56c(this._cookieName);
if(_579){
this.child.domNode.style[this.horizontal?"height":"width"]=_579;
}
}
},_computeMaxSize:function(){
var dim=this.horizontal?"h":"w",_57a=_570.getMarginBox(this.child.domNode)[dim],_57b=_56b.filter(this.container.getChildren(),function(_57c){
return _57c.region=="center";
})[0];
var _57d=_570.getContentBox(_57b.domNode)[dim]-10;
return Math.min(this.child.maxSize,_57a+_57d);
},_startDrag:function(e){
if(!this.cover){
this.cover=_56f.place("<div class=dijitSplitterCover></div>",this.child.domNode,"after");
}
_56e.add(this.cover,"dijitSplitterCoverActive");
if(this.fake){
_56f.destroy(this.fake);
}
if(!(this._resize=this.live)){
(this.fake=this.domNode.cloneNode(true)).removeAttribute("id");
_56e.add(this.domNode,"dijitSplitterShadow");
_56f.place(this.fake,this.domNode,"after");
}
_56e.add(this.domNode,"dijitSplitterActive dijitSplitter"+(this.horizontal?"H":"V")+"Active");
if(this.fake){
_56e.remove(this.fake,"dijitSplitterHover dijitSplitter"+(this.horizontal?"H":"V")+"Hover");
}
var _57e=this._factor,_57f=this.horizontal,axis=_57f?"pageY":"pageX",_580=e[axis],_581=this.domNode.style,dim=_57f?"h":"w",_582=_571.getComputedStyle(this.child.domNode),_583=_570.getMarginBox(this.child.domNode,_582)[dim],max=this._computeMaxSize(),min=Math.max(this.child.minSize,_570.getPadBorderExtents(this.child.domNode,_582)[dim]+10),_584=this.region,_585=_584=="top"||_584=="bottom"?"top":"left",_586=parseInt(_581[_585],10),_587=this._resize,_588=lang.hitch(this.container,"_layoutChildren",this.child.id),de=this.ownerDocument;
this._handlers=this._handlers.concat([on(de,_572.move,this._drag=function(e,_589){
var _58a=e[axis]-_580,_58b=_57e*_58a+_583,_58c=Math.max(Math.min(_58b,max),min);
if(_587||_589){
_588(_58c);
}
_581[_585]=_58a+_586+_57e*(_58c-_58b)+"px";
}),on(de,"dragstart",function(e){
e.stopPropagation();
e.preventDefault();
}),on(this.ownerDocumentBody,"selectstart",function(e){
e.stopPropagation();
e.preventDefault();
}),on(de,_572.release,lang.hitch(this,"_stopDrag"))]);
e.stopPropagation();
e.preventDefault();
},_onMouse:function(e){
var o=(e.type=="mouseover"||e.type=="mouseenter");
_56e.toggle(this.domNode,"dijitSplitterHover",o);
_56e.toggle(this.domNode,"dijitSplitter"+(this.horizontal?"H":"V")+"Hover",o);
},_stopDrag:function(e){
try{
if(this.cover){
_56e.remove(this.cover,"dijitSplitterCoverActive");
}
if(this.fake){
_56f.destroy(this.fake);
}
_56e.remove(this.domNode,"dijitSplitterActive dijitSplitter"+(this.horizontal?"H":"V")+"Active dijitSplitterShadow");
this._drag(e);
this._drag(e,true);
}
finally{
this._cleanupHandlers();
delete this._drag;
}
if(this.container.persist){
_56c(this._cookieName,this.child.domNode.style[this.horizontal?"height":"width"],{expires:365});
}
},_cleanupHandlers:function(){
var h;
while(h=this._handlers.pop()){
h.remove();
}
},_onKeyDown:function(e){
this._resize=true;
var _58d=this.horizontal;
var tick=1;
switch(e.keyCode){
case _58d?keys.UP_ARROW:keys.LEFT_ARROW:
tick*=-1;
case _58d?keys.DOWN_ARROW:keys.RIGHT_ARROW:
break;
default:
return;
}
var _58e=_570.getMarginSize(this.child.domNode)[_58d?"h":"w"]+this._factor*tick;
this.container._layoutChildren(this.child.id,Math.max(Math.min(_58e,this._computeMaxSize()),this.child.minSize));
e.stopPropagation();
e.preventDefault();
},destroy:function(){
this._cleanupHandlers();
delete this.child;
delete this.container;
delete this.cover;
delete this.fake;
this.inherited(arguments);
}});
var _58f=_56d("dijit.layout._Gutter",[_574,_575],{templateString:"<div class=\"dijitGutter\" role=\"presentation\"></div>",postMixInProperties:function(){
this.inherited(arguments);
this.horizontal=/top|bottom/.test(this.region);
},buildRendering:function(){
this.inherited(arguments);
_56e.add(this.domNode,"dijitGutter"+(this.horizontal?"H":"V"));
}});
var _590=_56d("dijit.layout.BorderContainer",_576,{gutters:true,liveSplitters:true,persist:false,baseClass:"dijitBorderContainer",_splitterClass:_578,postMixInProperties:function(){
if(!this.gutters){
this.baseClass+="NoGutter";
}
this.inherited(arguments);
},_setupChild:function(_591){
this.inherited(arguments);
var _592=_591.region,ltr=_591.isLeftToRight();
if(_592=="leading"){
_592=ltr?"left":"right";
}
if(_592=="trailing"){
_592=ltr?"right":"left";
}
if(_592){
if(_592!="center"&&(_591.splitter||this.gutters)&&!_591._splitterWidget){
var _593=_591.splitter?this._splitterClass:_58f;
if(lang.isString(_593)){
_593=lang.getObject(_593);
}
var _594=new _593({id:_591.id+"_splitter",container:this,child:_591,region:_592,live:this.liveSplitters});
_594.isSplitter=true;
_591._splitterWidget=_594;
var _595=_592=="bottom"||_592==(this.isLeftToRight()?"right":"left");
_56f.place(_594.domNode,_591.domNode,_595?"before":"after");
_594.startup();
}
}
},layout:function(){
this._layoutChildren();
},removeChild:function(_596){
var _597=_596._splitterWidget;
if(_597){
_597.destroy();
delete _596._splitterWidget;
}
this.inherited(arguments);
},getChildren:function(){
return _56b.filter(this.inherited(arguments),function(_598){
return !_598.isSplitter;
});
},getSplitter:function(_599){
return _56b.filter(this.getChildren(),function(_59a){
return _59a.region==_599;
})[0]._splitterWidget;
},resize:function(_59b,_59c){
if(!this.cs||!this.pe){
var node=this.domNode;
this.cs=_571.getComputedStyle(node);
this.pe=_570.getPadExtents(node,this.cs);
this.pe.r=_571.toPixelValue(node,this.cs.paddingRight);
this.pe.b=_571.toPixelValue(node,this.cs.paddingBottom);
_571.set(node,"padding","0px");
}
this.inherited(arguments);
},_layoutChildren:function(_59d,_59e){
if(!this._borderBox||!this._borderBox.h){
return;
}
var _59f=[];
_56b.forEach(this._getOrderedChildren(),function(pane){
_59f.push(pane);
if(pane._splitterWidget){
_59f.push(pane._splitterWidget);
}
});
var dim={l:this.pe.l,t:this.pe.t,w:this._borderBox.w-this.pe.w,h:this._borderBox.h-this.pe.h};
_577.layoutChildren(this.domNode,dim,_59f,_59d,_59e);
},destroyRecursive:function(){
_56b.forEach(this.getChildren(),function(_5a0){
var _5a1=_5a0._splitterWidget;
if(_5a1){
_5a1.destroy();
}
delete _5a0._splitterWidget;
});
this.inherited(arguments);
}});
_590.ChildWidgetProperties={splitter:false,minSize:0,maxSize:Infinity};
lang.mixin(_590.ChildWidgetProperties,_576.ChildWidgetProperties);
lang.extend(_573,_590.ChildWidgetProperties);
_590._Splitter=_578;
_590._Gutter=_58f;
return _590;
});
},"dojo/dnd/Mover":function(){
define(["../_base/array","../_base/declare","../_base/lang","../sniff","../_base/window","../dom","../dom-geometry","../dom-style","../Evented","../on","../touch","./common","./autoscroll"],function(_5a2,_5a3,lang,has,win,dom,_5a4,_5a5,_5a6,on,_5a7,dnd,_5a8){
return _5a3("dojo.dnd.Mover",[_5a6],{constructor:function(node,e,host){
this.node=dom.byId(node);
this.marginBox={l:e.pageX,t:e.pageY};
this.mouseButton=e.button;
var h=(this.host=host),d=node.ownerDocument;
function _5a9(e){
e.preventDefault();
e.stopPropagation();
};
this.events=[on(d,_5a7.move,lang.hitch(this,"onFirstMove")),on(d,_5a7.move,lang.hitch(this,"onMouseMove")),on(d,_5a7.release,lang.hitch(this,"onMouseUp")),on(d,"dragstart",_5a9),on(d.body,"selectstart",_5a9)];
_5a8.autoScrollStart(d);
if(h&&h.onMoveStart){
h.onMoveStart(this);
}
},onMouseMove:function(e){
_5a8.autoScroll(e);
var m=this.marginBox;
this.host.onMove(this,{l:m.l+e.pageX,t:m.t+e.pageY},e);
e.preventDefault();
e.stopPropagation();
},onMouseUp:function(e){
if(has("webkit")&&has("mac")&&this.mouseButton==2?e.button==0:this.mouseButton==e.button){
this.destroy();
}
e.preventDefault();
e.stopPropagation();
},onFirstMove:function(e){
var s=this.node.style,l,t,h=this.host;
switch(s.position){
case "relative":
case "absolute":
l=Math.round(parseFloat(s.left))||0;
t=Math.round(parseFloat(s.top))||0;
break;
default:
s.position="absolute";
var m=_5a4.getMarginBox(this.node);
var b=win.doc.body;
var bs=_5a5.getComputedStyle(b);
var bm=_5a4.getMarginBox(b,bs);
var bc=_5a4.getContentBox(b,bs);
l=m.l-(bc.l-bm.l);
t=m.t-(bc.t-bm.t);
break;
}
this.marginBox.l=l-this.marginBox.l;
this.marginBox.t=t-this.marginBox.t;
if(h&&h.onFirstMove){
h.onFirstMove(this,e);
}
this.events.shift().remove();
},destroy:function(){
_5a2.forEach(this.events,function(_5aa){
_5aa.remove();
});
var h=this.host;
if(h&&h.onMoveStop){
h.onMoveStop(this);
}
this.events=this.node=this.host=null;
}});
});
},"dijit/form/ComboBoxMixin":function(){
define(["dojo/_base/declare","dojo/Deferred","dojo/_base/kernel","dojo/_base/lang","dojo/store/util/QueryResults","./_AutoCompleterMixin","./_ComboBoxMenu","../_HasDropDown","dojo/text!./templates/DropDownBox.html"],function(_5ab,_5ac,_5ad,lang,_5ae,_5af,_5b0,_5b1,_5b2){
return _5ab("dijit.form.ComboBoxMixin",[_5b1,_5af],{dropDownClass:_5b0,hasDownArrow:true,templateString:_5b2,baseClass:"dijitTextBox dijitComboBox",cssStateNodes:{"_buttonNode":"dijitDownArrowButton"},_setHasDownArrowAttr:function(val){
this._set("hasDownArrow",val);
this._buttonNode.style.display=val?"":"none";
},_showResultList:function(){
this.displayMessage("");
this.inherited(arguments);
},_setStoreAttr:function(_5b3){
if(!_5b3.get){
lang.mixin(_5b3,{_oldAPI:true,get:function(id){
var _5b4=new _5ac();
this.fetchItemByIdentity({identity:id,onItem:function(_5b5){
_5b4.resolve(_5b5);
},onError:function(_5b6){
_5b4.reject(_5b6);
}});
return _5b4.promise;
},query:function(_5b7,_5b8){
var _5b9=new _5ac(function(){
_5ba.abort&&_5ba.abort();
});
_5b9.total=new _5ac();
var _5ba=this.fetch(lang.mixin({query:_5b7,onBegin:function(_5bb){
_5b9.total.resolve(_5bb);
},onComplete:function(_5bc){
_5b9.resolve(_5bc);
},onError:function(_5bd){
_5b9.reject(_5bd);
}},_5b8));
return _5ae(_5b9);
}});
}
this._set("store",_5b3);
},postMixInProperties:function(){
var _5be=this.params.store||this.store;
if(_5be){
this._setStoreAttr(_5be);
}
this.inherited(arguments);
if(!this.params.store&&this.store&&!this.store._oldAPI){
var _5bf=this.declaredClass;
lang.mixin(this.store,{getValue:function(item,attr){
_5ad.deprecated(_5bf+".store.getValue(item, attr) is deprecated for builtin store.  Use item.attr directly","","2.0");
return item[attr];
},getLabel:function(item){
_5ad.deprecated(_5bf+".store.getLabel(item) is deprecated for builtin store.  Use item.label directly","","2.0");
return item.name;
},fetch:function(args){
_5ad.deprecated(_5bf+".store.fetch() is deprecated for builtin store.","Use store.query()","2.0");
var shim=["dojo/data/ObjectStore"];
require(shim,lang.hitch(this,function(_5c0){
new _5c0({objectStore:this}).fetch(args);
}));
}});
}
},buildRendering:function(){
this.inherited(arguments);
this.focusNode.setAttribute("aria-autocomplete",this.autoComplete?"both":"list");
}});
});
},"dijit/form/Select":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/i18n","dojo/keys","dojo/_base/lang","dojo/on","dojo/sniff","./_FormSelectWidget","../_HasDropDown","../DropDownMenu","../MenuItem","../MenuSeparator","../Tooltip","../_KeyNavMixin","../registry","dojo/text!./templates/Select.html","dojo/i18n!./nls/validate"],function(_5c1,_5c2,_5c3,_5c4,_5c5,i18n,keys,lang,on,has,_5c6,_5c7,_5c8,_5c9,_5ca,_5cb,_5cc,_5cd,_5ce){
var _5cf=_5c2("dijit.form._SelectMenu",_5c8,{autoFocus:true,buildRendering:function(){
this.inherited(arguments);
this.domNode.setAttribute("role","listbox");
},postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"selectstart",function(evt){
evt.preventDefault();
evt.stopPropagation();
}));
},focus:function(){
var _5d0=false,val=this.parentWidget.value;
if(lang.isArray(val)){
val=val[val.length-1];
}
if(val){
_5c1.forEach(this.parentWidget._getChildren(),function(_5d1){
if(_5d1.option&&(val===_5d1.option.value)){
_5d0=true;
this.focusChild(_5d1,false);
}
},this);
}
if(!_5d0){
this.inherited(arguments);
}
}});
var _5d2=_5c2("dijit.form.Select"+(has("dojo-bidi")?"_NoBidi":""),[_5c6,_5c7,_5cc],{baseClass:"dijitSelect dijitValidationTextBox",templateString:_5ce,_buttonInputDisabled:has("ie")?"disabled":"",required:false,state:"",message:"",tooltipPosition:[],emptyLabel:"&#160;",_isLoaded:false,_childrenLoaded:false,labelType:"html",_fillContent:function(){
this.inherited(arguments);
if(this.options.length&&!this.value&&this.srcNodeRef){
var si=this.srcNodeRef.selectedIndex||0;
this._set("value",this.options[si>=0?si:0].value);
}
this.dropDown=new _5cf({id:this.id+"_menu",parentWidget:this});
_5c4.add(this.dropDown.domNode,this.baseClass.replace(/\s+|$/g,"Menu "));
},_getMenuItemForOption:function(_5d3){
if(!_5d3.value&&!_5d3.label){
return new _5ca({ownerDocument:this.ownerDocument});
}else{
var _5d4=lang.hitch(this,"_setValueAttr",_5d3);
var item=new _5c9({option:_5d3,label:(this.labelType==="text"?(_5d3.label||"").toString().replace(/&/g,"&amp;").replace(/</g,"&lt;"):_5d3.label)||this.emptyLabel,onClick:_5d4,ownerDocument:this.ownerDocument,dir:this.dir,textDir:this.textDir,disabled:_5d3.disabled||false});
item.focusNode.setAttribute("role","option");
return item;
}
},_addOptionItem:function(_5d5){
if(this.dropDown){
this.dropDown.addChild(this._getMenuItemForOption(_5d5));
}
},_getChildren:function(){
if(!this.dropDown){
return [];
}
return this.dropDown.getChildren();
},focus:function(){
if(!this.disabled&&this.focusNode.focus){
try{
this.focusNode.focus();
}
catch(e){
}
}
},focusChild:function(_5d6){
if(_5d6){
this.set("value",_5d6.option);
}
},_getFirst:function(){
var _5d7=this._getChildren();
return _5d7.length?_5d7[0]:null;
},_getLast:function(){
var _5d8=this._getChildren();
return _5d8.length?_5d8[_5d8.length-1]:null;
},childSelector:function(node){
var node=_5cd.byNode(node);
return node&&node.getParent()==this.dropDown;
},onKeyboardSearch:function(item,evt,_5d9,_5da){
if(item){
this.focusChild(item);
}
},_loadChildren:function(_5db){
if(_5db===true){
if(this.dropDown){
delete this.dropDown.focusedChild;
this.focusedChild=null;
}
if(this.options.length){
this.inherited(arguments);
}else{
_5c1.forEach(this._getChildren(),function(_5dc){
_5dc.destroyRecursive();
});
var item=new _5c9({ownerDocument:this.ownerDocument,label:this.emptyLabel});
this.dropDown.addChild(item);
}
}else{
this._updateSelection();
}
this._isLoaded=false;
this._childrenLoaded=true;
if(!this._loadingStore){
this._setValueAttr(this.value,false);
}
},_refreshState:function(){
if(this._started){
this.validate(this.focused);
}
},startup:function(){
this.inherited(arguments);
this._refreshState();
},_setValueAttr:function(_5dd){
this.inherited(arguments);
_5c3.set(this.valueNode,"value",this.get("value"));
this._refreshState();
},_setNameAttr:"valueNode",_setDisabledAttr:function(_5de){
this.inherited(arguments);
this._refreshState();
},_setRequiredAttr:function(_5df){
this._set("required",_5df);
this.focusNode.setAttribute("aria-required",_5df);
this._refreshState();
},_setOptionsAttr:function(_5e0){
this._isLoaded=false;
this._set("options",_5e0);
},_setDisplay:function(_5e1){
var lbl=(this.labelType==="text"?(_5e1||"").replace(/&/g,"&amp;").replace(/</g,"&lt;"):_5e1)||this.emptyLabel;
this.containerNode.innerHTML="<span role=\"option\" aria-selected=\"true\" class=\"dijitReset dijitInline "+this.baseClass.replace(/\s+|$/g,"Label ")+"\">"+lbl+"</span>";
},validate:function(_5e2){
var _5e3=this.disabled||this.isValid(_5e2);
this._set("state",_5e3?"":(this._hasBeenBlurred?"Error":"Incomplete"));
this.focusNode.setAttribute("aria-invalid",_5e3?"false":"true");
var _5e4=_5e3?"":this._missingMsg;
if(_5e4&&this.focused&&this._hasBeenBlurred){
_5cb.show(_5e4,this.domNode,this.tooltipPosition,!this.isLeftToRight());
}else{
_5cb.hide(this.domNode);
}
this._set("message",_5e4);
return _5e3;
},isValid:function(){
return (!this.required||this.value===0||!(/^\s*$/.test(this.value||"")));
},reset:function(){
this.inherited(arguments);
_5cb.hide(this.domNode);
this._refreshState();
},postMixInProperties:function(){
this.inherited(arguments);
this._missingMsg=i18n.getLocalization("dijit.form","validate",this.lang).missingMessage;
},postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"selectstart",function(evt){
evt.preventDefault();
evt.stopPropagation();
}));
this.domNode.setAttribute("aria-expanded","false");
var _5e5=this._keyNavCodes;
delete _5e5[keys.LEFT_ARROW];
delete _5e5[keys.RIGHT_ARROW];
},_setStyleAttr:function(_5e6){
this.inherited(arguments);
_5c4.toggle(this.domNode,this.baseClass.replace(/\s+|$/g,"FixedWidth "),!!this.domNode.style.width);
},isLoaded:function(){
return this._isLoaded;
},loadDropDown:function(_5e7){
this._loadChildren(true);
this._isLoaded=true;
_5e7();
},destroy:function(_5e8){
if(this.dropDown&&!this.dropDown._destroyed){
this.dropDown.destroyRecursive(_5e8);
delete this.dropDown;
}
_5cb.hide(this.domNode);
this.inherited(arguments);
},_onFocus:function(){
this.validate(true);
},_onBlur:function(){
_5cb.hide(this.domNode);
this.inherited(arguments);
this.validate(false);
}});
if(has("dojo-bidi")){
_5d2=_5c2("dijit.form.Select",_5d2,{_setDisplay:function(_5e9){
this.inherited(arguments);
this.applyTextDir(this.containerNode);
}});
}
_5d2._Menu=_5cf;
function _5ea(_5eb){
return function(evt){
if(!this._isLoaded){
this.loadDropDown(lang.hitch(this,_5eb,evt));
}else{
this.inherited(_5eb,arguments);
}
};
};
_5d2.prototype._onContainerKeydown=_5ea("_onContainerKeydown");
_5d2.prototype._onContainerKeypress=_5ea("_onContainerKeypress");
return _5d2;
});
},"dijit/_base/manager":function(){
define(["dojo/_base/array","dojo/_base/config","dojo/_base/lang","../registry","../main"],function(_5ec,_5ed,lang,_5ee,_5ef){
var _5f0={};
_5ec.forEach(["byId","getUniqueId","findWidgets","_destroyAll","byNode","getEnclosingWidget"],function(name){
_5f0[name]=_5ee[name];
});
lang.mixin(_5f0,{defaultDuration:_5ed["defaultDuration"]||200});
lang.mixin(_5ef,_5f0);
return _5ef;
});
},"curam/util/ui/form/CuramFormsAPI":function(){
define(["curam/define","curam/debug"],function(_5f1,_5f2){
curam.define.singleton("curam.util.ui.form.CuramFormsAPI",{pageID:"",_PATH:"path",_VALUE:"value",_formComponentMap:{},_expectedComponentCount:0,_ready:false,isEnabled:function(){
return curam.util.getTopmostWindow().CURAM_FORMS_API_ENABLED==="true";
},incrementExpectedComponentsCount:function(_5f3,_5f4,_5f5){
this._expectedComponentCount+=1;
curam.debug.log("curam.util.CuramFormsAPI: "+_5f2.getProperty("curam.util.CuramFormsAPI.expectedComponent"),[this._expectedComponentCount,_5f4]);
},getComponentCount:function(){
return this._expectedComponentCount;
},isReady:function(){
return this._ready;
},registerComponent:function(_5f6){
if(!this.isEnabled()){
return;
}
if(!this._formComponentMap[_5f6.pathID]){
this._formComponentMap[_5f6.pathID]=_5f6;
}
if(_5f6.addChangeListener&&_5f6.addChangeListener instanceof Function){
_5f6.addChangeListener(this._handleComponentChanges.bind(this));
}
var _5f7=Object.keys(this._formComponentMap).length;
curam.debug.log("curam.util.CuramFormsAPI: "+_5f2.getProperty("curam.util.CuramFormsAPI.registerComponent"),[_5f7,_5f6.pathID]);
if(_5f7===this._expectedComponentCount){
this._ready=true;
curam.util.getTopmostWindow().dojo.publish("curam/util/CuramFormsAPI/ready");
}
},setFormFields:function(_5f8,_5f9){
if(!this.isEnabled()){
return;
}
if(this.isReady()){
this._setFormFieldsInternal(_5f8,_5f9);
}else{
this._formPageID=_5f8;
this._formData=_5f9;
var _5fa=curam.util.getTopmostWindow().dojo.subscribe("curam/util/CuramFormsAPI/ready",this,function(){
this._setFormFieldsInternal(this._formPageID,this._formData);
curam.util.getTopmostWindow().dojo.unsubscribe(_5fa);
});
}
},_setFormFieldsInternal:function(_5fb,_5fc){
var _5fd=_5fc.data;
for(var key in this._formComponentMap){
var _5fe=this._formComponentMap[key];
if(this._formComponentMap.hasOwnProperty(key)&&_5fe){
var path=key;
var _5ff=_5fd.find(function(_600){
return path===_600._PATH&&_600._VALUE!=="";
});
_5ff&&this._updateComponentWithStoredData(_5ff,_5fe);
}
}
},_updateComponentWithStoredData:function(_601,_602){
_602.setFormElementValue(_601._VALUE);
},_handleComponentChanges:function(_603){
var _604=this.getFormComponentDataAsJSON(_603);
var href=location.href;
var _605=window.frameElement.id.replace("iframe-","");
curam.util.getTopmostWindow().dojo.publish("curam/util/CuramFormsAPI/formChange",[{data:_604,"href":href,"frameID":_605}]);
},getFormComponentDataAsJSON:function(){
var _606=[];
for(var key in this._formComponentMap){
var _607=this._formComponentMap[key];
if(this._formComponentMap.hasOwnProperty(key)&&_607){
var _608=key;
_606.push({_PATH:_608,_VALUE:_607.getFormElementValue()});
}
}
var _609=JSON.stringify({data:_606});
return _609;
},getFormComponentMap:function(){
return this._formComponentMap;
},});
return curam.util.ui.form.CuramFormsAPI;
});
},"dojo/data/ItemFileReadStore":function(){
define(["../_base/kernel","../_base/lang","../_base/declare","../_base/array","../_base/xhr","../Evented","./util/filter","./util/simpleFetch","../date/stamp"],function(_60a,lang,_60b,_60c,xhr,_60d,_60e,_60f,_610){
var _611=_60b("dojo.data.ItemFileReadStore",[_60d],{constructor:function(_612){
this._arrayOfAllItems=[];
this._arrayOfTopLevelItems=[];
this._loadFinished=false;
this._jsonFileUrl=_612.url;
this._ccUrl=_612.url;
this.url=_612.url;
this._jsonData=_612.data;
this.data=null;
this._datatypeMap=_612.typeMap||{};
if(!this._datatypeMap["Date"]){
this._datatypeMap["Date"]={type:Date,deserialize:function(_613){
return _610.fromISOString(_613);
}};
}
this._features={"dojo.data.api.Read":true,"dojo.data.api.Identity":true};
this._itemsByIdentity=null;
this._storeRefPropName="_S";
this._itemNumPropName="_0";
this._rootItemPropName="_RI";
this._reverseRefMap="_RRM";
this._loadInProgress=false;
this._queuedFetches=[];
if(_612.urlPreventCache!==undefined){
this.urlPreventCache=_612.urlPreventCache?true:false;
}
if(_612.hierarchical!==undefined){
this.hierarchical=_612.hierarchical?true:false;
}
if(_612.clearOnClose){
this.clearOnClose=true;
}
if("failOk" in _612){
this.failOk=_612.failOk?true:false;
}
},url:"",_ccUrl:"",data:null,typeMap:null,clearOnClose:false,urlPreventCache:false,failOk:false,hierarchical:true,_assertIsItem:function(item){
if(!this.isItem(item)){
throw new Error(this.declaredClass+": Invalid item argument.");
}
},_assertIsAttribute:function(_614){
if(typeof _614!=="string"){
throw new Error(this.declaredClass+": Invalid attribute argument.");
}
},getValue:function(item,_615,_616){
var _617=this.getValues(item,_615);
return (_617.length>0)?_617[0]:_616;
},getValues:function(item,_618){
this._assertIsItem(item);
this._assertIsAttribute(_618);
return (item[_618]||[]).slice(0);
},getAttributes:function(item){
this._assertIsItem(item);
var _619=[];
for(var key in item){
if((key!==this._storeRefPropName)&&(key!==this._itemNumPropName)&&(key!==this._rootItemPropName)&&(key!==this._reverseRefMap)){
_619.push(key);
}
}
return _619;
},hasAttribute:function(item,_61a){
this._assertIsItem(item);
this._assertIsAttribute(_61a);
return (_61a in item);
},containsValue:function(item,_61b,_61c){
var _61d=undefined;
if(typeof _61c==="string"){
_61d=_60e.patternToRegExp(_61c,false);
}
return this._containsValue(item,_61b,_61c,_61d);
},_containsValue:function(item,_61e,_61f,_620){
return _60c.some(this.getValues(item,_61e),function(_621){
if(_621!==null&&!lang.isObject(_621)&&_620){
if(_621.toString().match(_620)){
return true;
}
}else{
if(_61f===_621){
return true;
}
}
});
},isItem:function(_622){
if(_622&&_622[this._storeRefPropName]===this){
if(this._arrayOfAllItems[_622[this._itemNumPropName]]===_622){
return true;
}
}
return false;
},isItemLoaded:function(_623){
return this.isItem(_623);
},loadItem:function(_624){
this._assertIsItem(_624.item);
},getFeatures:function(){
return this._features;
},getLabel:function(item){
if(this._labelAttr&&this.isItem(item)){
return this.getValue(item,this._labelAttr);
}
return undefined;
},getLabelAttributes:function(item){
if(this._labelAttr){
return [this._labelAttr];
}
return null;
},filter:function(_625,_626,_627){
var _628=[],i,key;
if(_625.query){
var _629,_62a=_625.queryOptions?_625.queryOptions.ignoreCase:false;
var _62b={};
for(key in _625.query){
_629=_625.query[key];
if(typeof _629==="string"){
_62b[key]=_60e.patternToRegExp(_629,_62a);
}else{
if(_629 instanceof RegExp){
_62b[key]=_629;
}
}
}
for(i=0;i<_626.length;++i){
var _62c=true;
var _62d=_626[i];
if(_62d===null){
_62c=false;
}else{
for(key in _625.query){
_629=_625.query[key];
if(!this._containsValue(_62d,key,_629,_62b[key])){
_62c=false;
}
}
}
if(_62c){
_628.push(_62d);
}
}
_627(_628,_625);
}else{
for(i=0;i<_626.length;++i){
var item=_626[i];
if(item!==null){
_628.push(item);
}
}
_627(_628,_625);
}
},_fetchItems:function(_62e,_62f,_630){
var self=this;
if(this._loadFinished){
this.filter(_62e,this._getItemsArray(_62e.queryOptions),_62f);
}else{
if(this._jsonFileUrl!==this._ccUrl){
_60a.deprecated(this.declaredClass+": ","To change the url, set the url property of the store,"+" not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0");
this._ccUrl=this._jsonFileUrl;
this.url=this._jsonFileUrl;
}else{
if(this.url!==this._ccUrl){
this._jsonFileUrl=this.url;
this._ccUrl=this.url;
}
}
if(this.data!=null){
this._jsonData=this.data;
this.data=null;
}
if(this._jsonFileUrl){
if(this._loadInProgress){
this._queuedFetches.push({args:_62e,filter:lang.hitch(self,"filter"),findCallback:lang.hitch(self,_62f)});
}else{
this._loadInProgress=true;
var _631={url:self._jsonFileUrl,handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk};
var _632=xhr.get(_631);
_632.addCallback(function(data){
try{
self._getItemsFromLoadedData(data);
self._loadFinished=true;
self._loadInProgress=false;
self.filter(_62e,self._getItemsArray(_62e.queryOptions),_62f);
self._handleQueuedFetches();
}
catch(e){
self._loadFinished=true;
self._loadInProgress=false;
_630(e,_62e);
}
});
_632.addErrback(function(_633){
self._loadInProgress=false;
_630(_633,_62e);
});
var _634=null;
if(_62e.abort){
_634=_62e.abort;
}
_62e.abort=function(){
var df=_632;
if(df&&df.fired===-1){
df.cancel();
df=null;
}
if(_634){
_634.call(_62e);
}
};
}
}else{
if(this._jsonData){
try{
this._loadFinished=true;
this._getItemsFromLoadedData(this._jsonData);
this._jsonData=null;
self.filter(_62e,this._getItemsArray(_62e.queryOptions),_62f);
}
catch(e){
_630(e,_62e);
}
}else{
_630(new Error(this.declaredClass+": No JSON source data was provided as either URL or a nested Javascript object."),_62e);
}
}
}
},_handleQueuedFetches:function(){
if(this._queuedFetches.length>0){
for(var i=0;i<this._queuedFetches.length;i++){
var _635=this._queuedFetches[i],_636=_635.args,_637=_635.filter,_638=_635.findCallback;
if(_637){
_637(_636,this._getItemsArray(_636.queryOptions),_638);
}else{
this.fetchItemByIdentity(_636);
}
}
this._queuedFetches=[];
}
},_getItemsArray:function(_639){
if(_639&&_639.deep){
return this._arrayOfAllItems;
}
return this._arrayOfTopLevelItems;
},close:function(_63a){
if(this.clearOnClose&&this._loadFinished&&!this._loadInProgress){
if(((this._jsonFileUrl==""||this._jsonFileUrl==null)&&(this.url==""||this.url==null))&&this.data==null){
console.debug(this.declaredClass+": WARNING!  Data reload "+" information has not been provided."+"  Please set 'url' or 'data' to the appropriate value before"+" the next fetch");
}
this._arrayOfAllItems=[];
this._arrayOfTopLevelItems=[];
this._loadFinished=false;
this._itemsByIdentity=null;
this._loadInProgress=false;
this._queuedFetches=[];
}
},_getItemsFromLoadedData:function(_63b){
var _63c=false,self=this;
function _63d(_63e){
return (_63e!==null)&&(typeof _63e==="object")&&(!lang.isArray(_63e)||_63c)&&(!lang.isFunction(_63e))&&(_63e.constructor==Object||lang.isArray(_63e))&&(typeof _63e._reference==="undefined")&&(typeof _63e._type==="undefined")&&(typeof _63e._value==="undefined")&&self.hierarchical;
};
function _63f(_640){
self._arrayOfAllItems.push(_640);
for(var _641 in _640){
var _642=_640[_641];
if(_642){
if(lang.isArray(_642)){
var _643=_642;
for(var k=0;k<_643.length;++k){
var _644=_643[k];
if(_63d(_644)){
_63f(_644);
}
}
}else{
if(_63d(_642)){
_63f(_642);
}
}
}
}
};
this._labelAttr=_63b.label;
var i,item;
this._arrayOfAllItems=[];
this._arrayOfTopLevelItems=_63b.items;
for(i=0;i<this._arrayOfTopLevelItems.length;++i){
item=this._arrayOfTopLevelItems[i];
if(lang.isArray(item)){
_63c=true;
}
_63f(item);
item[this._rootItemPropName]=true;
}
var _645={},key;
for(i=0;i<this._arrayOfAllItems.length;++i){
item=this._arrayOfAllItems[i];
for(key in item){
if(key!==this._rootItemPropName){
var _646=item[key];
if(_646!==null){
if(!lang.isArray(_646)){
item[key]=[_646];
}
}else{
item[key]=[null];
}
}
_645[key]=key;
}
}
while(_645[this._storeRefPropName]){
this._storeRefPropName+="_";
}
while(_645[this._itemNumPropName]){
this._itemNumPropName+="_";
}
while(_645[this._reverseRefMap]){
this._reverseRefMap+="_";
}
var _647;
var _648=_63b.identifier;
if(_648){
this._itemsByIdentity={};
this._features["dojo.data.api.Identity"]=_648;
for(i=0;i<this._arrayOfAllItems.length;++i){
item=this._arrayOfAllItems[i];
_647=item[_648];
var _649=_647[0];
if(!Object.hasOwnProperty.call(this._itemsByIdentity,_649)){
this._itemsByIdentity[_649]=item;
}else{
if(this._jsonFileUrl){
throw new Error(this.declaredClass+":  The json data as specified by: ["+this._jsonFileUrl+"] is malformed.  Items within the list have identifier: ["+_648+"].  Value collided: ["+_649+"]");
}else{
if(this._jsonData){
throw new Error(this.declaredClass+":  The json data provided by the creation arguments is malformed.  Items within the list have identifier: ["+_648+"].  Value collided: ["+_649+"]");
}
}
}
}
}else{
this._features["dojo.data.api.Identity"]=Number;
}
for(i=0;i<this._arrayOfAllItems.length;++i){
item=this._arrayOfAllItems[i];
item[this._storeRefPropName]=this;
item[this._itemNumPropName]=i;
}
for(i=0;i<this._arrayOfAllItems.length;++i){
item=this._arrayOfAllItems[i];
for(key in item){
_647=item[key];
for(var j=0;j<_647.length;++j){
_646=_647[j];
if(_646!==null&&typeof _646=="object"){
if(("_type" in _646)&&("_value" in _646)){
var type=_646._type;
var _64a=this._datatypeMap[type];
if(!_64a){
throw new Error("dojo.data.ItemFileReadStore: in the typeMap constructor arg, no object class was specified for the datatype '"+type+"'");
}else{
if(lang.isFunction(_64a)){
_647[j]=new _64a(_646._value);
}else{
if(lang.isFunction(_64a.deserialize)){
_647[j]=_64a.deserialize(_646._value);
}else{
throw new Error("dojo.data.ItemFileReadStore: Value provided in typeMap was neither a constructor, nor a an object with a deserialize function");
}
}
}
}
if(_646._reference){
var _64b=_646._reference;
if(!lang.isObject(_64b)){
_647[j]=this._getItemByIdentity(_64b);
}else{
for(var k=0;k<this._arrayOfAllItems.length;++k){
var _64c=this._arrayOfAllItems[k],_64d=true;
for(var _64e in _64b){
if(_64c[_64e]!=_64b[_64e]){
_64d=false;
}
}
if(_64d){
_647[j]=_64c;
}
}
}
if(this.referenceIntegrity){
var _64f=_647[j];
if(this.isItem(_64f)){
this._addReferenceToMap(_64f,item,key);
}
}
}else{
if(this.isItem(_646)){
if(this.referenceIntegrity){
this._addReferenceToMap(_646,item,key);
}
}
}
}
}
}
}
},_addReferenceToMap:function(_650,_651,_652){
},getIdentity:function(item){
var _653=this._features["dojo.data.api.Identity"];
if(_653===Number){
return item[this._itemNumPropName];
}else{
var _654=item[_653];
if(_654){
return _654[0];
}
}
return null;
},fetchItemByIdentity:function(_655){
var item,_656;
if(!this._loadFinished){
var self=this;
if(this._jsonFileUrl!==this._ccUrl){
_60a.deprecated(this.declaredClass+": ","To change the url, set the url property of the store,"+" not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0");
this._ccUrl=this._jsonFileUrl;
this.url=this._jsonFileUrl;
}else{
if(this.url!==this._ccUrl){
this._jsonFileUrl=this.url;
this._ccUrl=this.url;
}
}
if(this.data!=null&&this._jsonData==null){
this._jsonData=this.data;
this.data=null;
}
if(this._jsonFileUrl){
if(this._loadInProgress){
this._queuedFetches.push({args:_655});
}else{
this._loadInProgress=true;
var _657={url:self._jsonFileUrl,handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk};
var _658=xhr.get(_657);
_658.addCallback(function(data){
var _659=_655.scope?_655.scope:_60a.global;
try{
self._getItemsFromLoadedData(data);
self._loadFinished=true;
self._loadInProgress=false;
item=self._getItemByIdentity(_655.identity);
if(_655.onItem){
_655.onItem.call(_659,item);
}
self._handleQueuedFetches();
}
catch(error){
self._loadInProgress=false;
if(_655.onError){
_655.onError.call(_659,error);
}
}
});
_658.addErrback(function(_65a){
self._loadInProgress=false;
if(_655.onError){
var _65b=_655.scope?_655.scope:_60a.global;
_655.onError.call(_65b,_65a);
}
});
}
}else{
if(this._jsonData){
self._getItemsFromLoadedData(self._jsonData);
self._jsonData=null;
self._loadFinished=true;
item=self._getItemByIdentity(_655.identity);
if(_655.onItem){
_656=_655.scope?_655.scope:_60a.global;
_655.onItem.call(_656,item);
}
}
}
}else{
item=this._getItemByIdentity(_655.identity);
if(_655.onItem){
_656=_655.scope?_655.scope:_60a.global;
_655.onItem.call(_656,item);
}
}
},_getItemByIdentity:function(_65c){
var item=null;
if(this._itemsByIdentity){
if(Object.hasOwnProperty.call(this._itemsByIdentity,_65c)){
item=this._itemsByIdentity[_65c];
}
}else{
if(Object.hasOwnProperty.call(this._arrayOfAllItems,_65c)){
item=this._arrayOfAllItems[_65c];
}
}
if(item===undefined){
item=null;
}
return item;
},getIdentityAttributes:function(item){
var _65d=this._features["dojo.data.api.Identity"];
if(_65d===Number){
return null;
}else{
return [_65d];
}
},_forceLoad:function(){
var self=this;
if(this._jsonFileUrl!==this._ccUrl){
_60a.deprecated(this.declaredClass+": ","To change the url, set the url property of the store,"+" not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0");
this._ccUrl=this._jsonFileUrl;
this.url=this._jsonFileUrl;
}else{
if(this.url!==this._ccUrl){
this._jsonFileUrl=this.url;
this._ccUrl=this.url;
}
}
if(this.data!=null){
this._jsonData=this.data;
this.data=null;
}
if(this._jsonFileUrl){
var _65e={url:this._jsonFileUrl,handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk,sync:true};
var _65f=xhr.get(_65e);
_65f.addCallback(function(data){
try{
if(self._loadInProgress!==true&&!self._loadFinished){
self._getItemsFromLoadedData(data);
self._loadFinished=true;
}else{
if(self._loadInProgress){
throw new Error(this.declaredClass+":  Unable to perform a synchronous load, an async load is in progress.");
}
}
}
catch(e){
console.log(e);
throw e;
}
});
_65f.addErrback(function(_660){
throw _660;
});
}else{
if(this._jsonData){
self._getItemsFromLoadedData(self._jsonData);
self._jsonData=null;
self._loadFinished=true;
}
}
}});
lang.extend(_611,_60f);
return _611;
});
},"curam/validation/calendar":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.validation.calendar",{invalidGotoDateEntered:null});
return curam.validation.calendar;
});
},"curam/widget/_TabButton":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-style","dojo/has","dojo/i18n","dojo/_base/lang","dojo/text!curam/widget/templates/_TabButton.html","dijit/registry","dojo/_base/connect","curam/inspection/Layer","dijit/layout/StackController","dijit/Menu","dijit/MenuItem","curam/widget/MenuItem","curam/tab"],function(_661,dom,_662,_663,_664,has,i18n,lang,_665,_666,_667,_668){
_667.subscribe("/curam/tab/labelUpdated",function(){
var tabs;
var _669=dojo.query(".dijitTabContainerTop-tabs");
_669.forEach(function(_66a){
tabs=dojo.query(".tabLabel",_66a);
tabs.forEach(function(tab,_66b){
var _66c=tabs[_66b].innerHTML;
tab.setAttribute("title",_66c);
var _66d=tab.nextSibling;
while(_66d){
if(_663.contains(_66d,"dijitTabCloseButton")){
var _66e=_66d.getAttribute("title");
if(_66e&&_66e.indexOf(_66c)<0){
_66d.setAttribute("title",_66e+" - "+_66c);
}
break;
}
_66d=_66d.nextSibling;
}
});
});
});
var _66f=_661("curam.widget._TabButton",dijit.layout._StackButton,{templateString:_665,_setNameAttr:"focusNode",scrollOnFocus:false,curamDisabled:false,curamVisible:true,baseClass:"dijitTab",postMixInProperties:function(){
if(!this.iconClass){
this.iconClass="dijitTabButtonIcon";
}
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.containerNode,false);
if(this.iconNode.className=="dijitTabButtonIcon"){
_664.set(this.iconNode,"width","1px");
}
_662.set(this.focusNode,"id",this.id);
_668.register("curam/widget/_TabButton",this);
},startup:function(){
dijit.layout._StackButton.prototype.startup.apply(this,arguments);
},_setCloseButtonAttr:function(disp){
this._set("closeButton",disp);
_663.toggle(this.domNode,"dijitClosable",disp);
this.closeNode.style.display=disp?"":"none";
if(disp){
var _670=i18n.getLocalization("dijit","common");
if(this.closeNode){
_662.set(this.closeNode,"title",_670.itemClose);
}
}else{
_663.add(this.titleNode,"hasNoCloseButton");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
}
},_setCuramDisabledAttr:function(_671){
this.curamDisabled=_671;
this._swapState(this.domNode,this.curamDisabled,"disabled","enabled");
},_setCuramVisibleAttr:function(_672){
this.curamVisible=_672;
this._swapState(this.domNode,this.curamVisible,"visible","hidden");
},_swapState:function(node,_673,_674,_675){
if(_673){
_663.replace(node,_674,_675);
}else{
_663.replace(node,_675,_674);
}
},destroy:function(){
_667.publish("/curam/tab/labelUpdated");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
this.inherited(arguments);
}});
return _66f;
});
},"dijit/form/ComboButton":function(){
define(["dojo/_base/declare","dojo/keys","../focus","./DropDownButton","dojo/text!./templates/ComboButton.html","../a11yclick"],function(_676,keys,_677,_678,_679){
return _676("dijit.form.ComboButton",_678,{templateString:_679,_setIdAttr:"",_setTabIndexAttr:["focusNode","titleNode"],_setTitleAttr:"titleNode",optionsTitle:"",baseClass:"dijitComboButton",cssStateNodes:{"buttonNode":"dijitButtonNode","titleNode":"dijitButtonContents","_popupStateNode":"dijitDownArrowButton"},_focusedNode:null,_onButtonKeyDown:function(evt){
if(evt.keyCode==keys[this.isLeftToRight()?"RIGHT_ARROW":"LEFT_ARROW"]){
_677.focus(this._popupStateNode);
evt.stopPropagation();
evt.preventDefault();
}
},_onArrowKeyDown:function(evt){
if(evt.keyCode==keys[this.isLeftToRight()?"LEFT_ARROW":"RIGHT_ARROW"]){
_677.focus(this.titleNode);
evt.stopPropagation();
evt.preventDefault();
}
},focus:function(_67a){
if(!this.disabled){
_677.focus(_67a=="start"?this.titleNode:this._popupStateNode);
}
}});
});
},"curam/pagination/ExpandableListModel":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/query","curam/debug","curam/util/ExpandableLists","curam/pagination"],function(_67b,_67c,_67d,_67e,_67f,_680,_681){
var _682=_67b("curam.pagination.ExpandableListModel",null,{_rowCount:null,constructor:function(_683){
this.tableNode=_67e("table.paginated-list-id-"+_683)[0];
if(!this.tableNode){
throw "Table node for ID "+_683+" not found - failing!";
}
_67f.log("curam.pagination.ExpandableListModel "+_67f.getProperty("curam.pagination.ExpandableListModel"),this.tableNode);
this._id=_683;
},getId:function(){
return this._id;
},getRowCount:function(){
if(this._rowCount==null){
this._rowCount=0;
var _684=dojo.query("tbody > script.hidden-list-rows",this.tableNode),frag=document.createDocumentFragment();
for(var i=0;i<_684.length;i++){
var _685=_684[i];
var _686=(i==_684.length-1);
if(!_686){
this._rowCount+=(_681.getNumRowsInBlock(_685)*2);
}else{
_681.unpackRows(_685,frag);
_685.innerHTML="";
}
}
this.tableNode.tBodies[0].appendChild(frag);
var _687=dojo.query("tbody > tr",this.tableNode).length;
this._rowCount+=_687;
}
if(this._rowCount<=1){
return 1;
}else{
return this._rowCount/2;
}
},hideRange:function(_688,_689){
var rows=this._getRowNodes(_688,_689);
for(var i=_688;i<=_689;i++){
var _68a=(2*i)-2;
var _68b=(2*i)-1;
_67d.set(rows[_68a],"display","none");
_67c.remove(rows[_68a],"even-last-row");
_67c.remove(rows[_68a],"odd-last-row");
if(rows.length>_68b){
var _68c=rows[_68b];
if(_68c){
_68c._curam_pagination_expanded=curam.util.ExpandableLists.isDetailsRowExpanded(_68c);
curam.util.ExpandableLists.setDetailsRowExpandedState(rows[_68a],_68c,false);
}
}
}
},showRange:function(_68d,_68e){
var rows=this._getRowNodes(_68d,_68e);
var _68f=(_68e%2==0)?"even-last-row":"odd-last-row";
_67c.add(rows[(_68e*2)-2],_68f);
for(var i=_68d;i<=_68e;i++){
var _690=(2*i)-2;
var _691=(2*i)-1;
_67d.set(rows[_690],"display","");
if(rows.length>_691){
var _692=rows[_691];
if(_692){
curam.util.ExpandableLists.setDetailsRowExpandedState(rows[_690],_692,_692._curam_pagination_expanded);
}
}
}
dojo.publish("curam/update/pagination/rows",[rows,this.getId()]);
},_getRowNodes:function(_693,_694){
var _695=curam.pagination.readListContent(this.tableNode);
for(var i=1;i<=(_694*2)&&i<=_695.length;i++){
var node=_695[i-1];
if(node.tagName=="SCRIPT"){
curam.pagination.unpackRows(node);
_695=curam.pagination.readListContent(this.tableNode);
i--;
}
}
return dojo.query("tbody > tr",this.tableNode);
}});
return _682;
});
},"curam/widget/SearchTextBox":function(){
define(["dojo/query","dojo/dom-style","dojo/dom-construct","dojo/on","dojo/dom-geometry","dojo/dom-class","curam/util","dojo/dom-attr","dojo/_base/fx","dojo/_base/lang","dijit/form/TextBox","dojo/_base/declare","dojo/text!curam/widget/templates/SearchTextBox.html"],function(_696,_697,_698,on,_699,_69a,util,_69b,fx,lang,_69c,_69d,_69e){
var _69f=_69d("curam.widget.SearchTextBox",_69c,{templateString:_69e,backgroundColor:null,searchTextDiv:null,searchIconDiv:null,searchIcon:null,searchInputField:null,searchInputImg:null,placeholderText:null,applicationSearchDiv:null,searchControlsDiv:null,placeholderText:null,closedHeight:null,searchOptionsDiv:null,searchOptionsDivOpenedColor:null,originalInputColor:null,menuOpenedHeight:60,__populateValues:function(){
if(this.searchControlsDiv==null){
this.searchControlsDiv=_696(".search-input-controls");
}
if(this.searchIconDiv==null){
this.searchIconDiv=_696(".application-search-anchor-div");
}
if(this.searchIcon==null){
this.searchIcon=_696(".application-search-anchor");
}
if(this.searchInputField==null){
this.searchInputField=_696(".search-input-controls div.dijitInputField input")[0];
}
if(this.applicationSearchDiv==null){
this.applicationSearchDiv=_696(".application-search");
}
if(this.searchOptionsDiv==null){
this.searchOptionsDiv=_696(".search-options.no-dropdown");
}
if(this.searchOptionsDiv.length>0&&this.searchOptionsDivOpenedColor==null){
this.searchOptionsDivOpenedColor=_697.get(this.searchOptionsDiv[0],"color");
}
if(this.searchInputImg==null){
this.searchInputImg=_696(".application-search-anchor img");
}
},postCreate:function(){
this.__populateValues();
},__hideElements:function(){
if(this.__isSearchInputFieldPopulated()){
_697.set(this.searchInputField,"color",_697.get(this.applicationSearchDiv[0],"color"));
}else{
_697.set(this.searchInputField,this.originalInputColor);
}
_69a.remove(this.searchInputField,"input-placeholder-opened");
_69a.add(this.searchInputField,"input-placeholder-closed");
if(this.searchTextDiv!=null&&_697.get(this.searchTextDiv[0],"background-color")!=this.backgroundColor){
fx.animateProperty({node:this.searchTextDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
fx.animateProperty({node:this.searchControlsDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
fx.animateProperty({node:this.searchIconDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-on-dark.svg";
if(this.searchOptionsDiv.length>0){
_69a.remove(this.applicationSearchDiv[0],"application-search-upfront-popup");
_697.set(this.searchOptionsDiv[0],"display","none");
fx.animateProperty({node:this.applicationSearchDiv[0],properties:{height:{start:this.menuOpenedHeight,end:this.closedHeight}}}).play();
this.applicationSearchDiv.style({left:"0px"});
}
}
},__recursive:function(_6a0){
if(_6a0.parentElement!=null){
if(_69a.contains(_6a0,"application-search")){
return true;
}else{
return this.__recursive(_6a0.parentElement);
}
}
return false;
},__checkBlur:function(evt){
var _6a1=this;
setTimeout(function(){
var _6a2=document.activeElement;
if(!(_69a.contains(_6a2,"searchTextBox")||_69a.contains(_6a2,"application-search-anchor")||_69a.contains(_6a2,"curam")||(_6a2.parentElement!=null&&_69a.contains(_6a2.parentElement,"search-options"))||_69a.contains(_6a2,"search-options"))){
_6a1.__hideElements();
}
},1);
},_setPlaceHolderAttr:function(v){
this.__populateValues();
var _6a3=this;
on(_696("body.curam"),"click",function(evt){
var _6a4=_6a3.__recursive(evt.target);
if(!_6a4){
_6a3.__hideElements();
}
});
on(_696("body.curam"),"touchstart",function(evt){
var _6a5=_6a3.__recursive(evt.target);
if(!_6a5){
_6a3.__hideElements();
}
});
on(this.searchIcon,"blur",function(evt){
_6a3.__checkBlur(evt);
});
if(this.searchOptionsDiv.length>0){
on(this.searchOptionsDiv[0].firstChild,"blur",function(evt){
_6a3.__checkBlur(evt);
});
}
placeholderText=v;
this.searchInputField=this.domNode.firstChild.firstChild;
_69b.set(this.searchInputField,"placeholder",placeholderText);
},__isSearchInputFieldPopulated:function(){
if(this.searchInputField.value.length>0){
return true;
}
return false;
},__onKeyUp:function(evt){
var _6a6=this.__isSearchInputFieldPopulated();
this.__enableOrDisableSearchLink(evt,_6a6);
},__onPaste:function(evt){
var _6a7=evt.clipboardData||window.clipboardData;
var _6a8=_6a7.getData("Text");
var _6a9=(_6a8&&_6a8.length>0)||this.__isSearchInputFieldPopulated();
this.__enableOrDisableSearchLink(evt,_6a9);
},__enableOrDisableSearchLink:function(evt,_6aa){
if(_6aa&&_6aa===true){
_69a.remove(this.searchIcon[0],"dijitDisabled");
if(evt.keyCode==13){
curam.util.search("__o3.appsearch.searchText","__o3.appsearch.searchType");
}
}else{
_69a.add(this.searchIcon[0],"dijitDisabled");
}
},__onClick:function(evt){
this.__populateValues();
if(this.searchTextDiv==null){
this.searchTextDiv=_696(".search-input-controls div.dijitInputField");
}
if(this.originalInputColor==null){
this.originalInputColor=_697.get(this.searchInputField,"color");
}
_697.set(this.searchInputField,"color",this.originalInputColor);
_69a.remove(this.searchInputField,"input-placeholder-closed");
_69a.add(this.searchInputField,"input-placeholder-opened");
if(this.searchOptionsDiv.length>0){
var _6ab=_699.position(this.searchInputField).x-11;
var _6ac=util.isRtlMode();
if(_6ac!=null){
_6ab=_699.position(this.applicationSearchDiv[0]).x-20;
}
if(this.closedHeight==null){
this.closedHeight=_697.get(this.applicationSearchDiv[0],"height");
}
this.applicationSearchDiv.style({left:_6ab+"px"});
_69a.add(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(_697.get(this.applicationSearchDiv[0],"height")!=60){
_697.set(this.searchOptionsDiv[0],"display","block");
_697.set(this.searchOptionsDiv[0],"opacity","0");
var _6ad={node:this.searchOptionsDiv[0]};
fx.animateProperty({node:this.applicationSearchDiv[0],properties:{height:{start:this.closedHeight,end:this.menuOpenedHeight}}}).play();
fx.fadeIn(_6ad).play();
}
}
if(this.backgroundColor==null){
this.backgroundColor=_697.get(this.searchTextDiv[0],"background-color");
}
if(this.searchTextDiv!=null&&_697.get(this.searchTextDiv[0],"background-color")!=this.searchOptionsDivOpenedColor){
fx.animateProperty({node:this.searchTextDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
fx.animateProperty({node:this.searchControlsDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
fx.animateProperty({node:this.searchIconDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-enabled.svg";
}
},__onBlur:function(evt){
this.__populateValues();
if(!this.__isSearchInputFieldPopulated()){
this.searchInputField.value="";
_69b.set(this.searchInputField,"placeholder",placeholderText);
}
this.__checkBlur(evt);
}});
return _69f;
});
},"dojo/touch":function(){
define(["./_base/kernel","./aspect","./dom","./dom-class","./_base/lang","./on","./has","./mouse","./domReady","./_base/window"],function(dojo,_6ae,dom,_6af,lang,on,has,_6b0,_6b1,win){
var ios4=has("ios")<5;
var _6b2=has("pointer-events")||has("MSPointer"),_6b3=(function(){
var _6b4={};
for(var type in {down:1,move:1,up:1,cancel:1,over:1,out:1}){
_6b4[type]=has("MSPointer")?"MSPointer"+type.charAt(0).toUpperCase()+type.slice(1):"pointer"+type;
}
return _6b4;
})();
var _6b5=has("touch-events");
var _6b6,_6b7,_6b8=false,_6b9,_6ba,_6bb,_6bc,_6bd,_6be;
var _6bf;
function _6c0(_6c1,_6c2,_6c3){
if(_6b2&&_6c3){
return function(node,_6c4){
return on(node,_6c3,_6c4);
};
}else{
if(_6b5){
return function(node,_6c5){
var _6c6=on(node,_6c2,function(evt){
_6c5.call(this,evt);
_6bf=(new Date()).getTime();
}),_6c7=on(node,_6c1,function(evt){
if(!_6bf||(new Date()).getTime()>_6bf+1000){
_6c5.call(this,evt);
}
});
return {remove:function(){
_6c6.remove();
_6c7.remove();
}};
};
}else{
return function(node,_6c8){
return on(node,_6c1,_6c8);
};
}
}
};
function _6c9(node){
do{
if(node.dojoClick!==undefined){
return node;
}
}while(node=node.parentNode);
};
function _6ca(e,_6cb,_6cc){
if(_6b0.isRight(e)){
return;
}
var _6cd=_6c9(e.target);
_6b7=!e.target.disabled&&_6cd&&_6cd.dojoClick;
if(_6b7){
_6b8=(_6b7=="useTarget");
_6b9=(_6b8?_6cd:e.target);
if(_6b8){
e.preventDefault();
}
_6ba=e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX;
_6bb=e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY;
_6bc=(typeof _6b7=="object"?_6b7.x:(typeof _6b7=="number"?_6b7:0))||4;
_6bd=(typeof _6b7=="object"?_6b7.y:(typeof _6b7=="number"?_6b7:0))||4;
if(!_6b6){
_6b6=true;
function _6ce(e){
if(_6b8){
_6b7=dom.isDescendant(win.doc.elementFromPoint((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX),(e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)),_6b9);
}else{
_6b7=_6b7&&(e.changedTouches?e.changedTouches[0].target:e.target)==_6b9&&Math.abs((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX)-_6ba)<=_6bc&&Math.abs((e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)-_6bb)<=_6bd;
}
};
win.doc.addEventListener(_6cb,function(e){
if(_6b0.isRight(e)){
return;
}
_6ce(e);
if(_6b8){
e.preventDefault();
}
},true);
win.doc.addEventListener(_6cc,function(e){
if(_6b0.isRight(e)){
return;
}
_6ce(e);
if(_6b7){
_6be=(new Date()).getTime();
var _6cf=(_6b8?_6b9:e.target);
if(_6cf.tagName==="LABEL"){
_6cf=dom.byId(_6cf.getAttribute("for"))||_6cf;
}
var src=(e.changedTouches)?e.changedTouches[0]:e;
function _6d0(type){
var evt=document.createEvent("MouseEvents");
evt._dojo_click=true;
evt.initMouseEvent(type,true,true,e.view,e.detail,src.screenX,src.screenY,src.clientX,src.clientY,e.ctrlKey,e.altKey,e.shiftKey,e.metaKey,0,null);
return evt;
};
var _6d1=_6d0("mousedown");
var _6d2=_6d0("mouseup");
var _6d3=_6d0("click");
setTimeout(function(){
on.emit(_6cf,"mousedown",_6d1);
on.emit(_6cf,"mouseup",_6d2);
on.emit(_6cf,"click",_6d3);
_6be=(new Date()).getTime();
},0);
}
},true);
function _6d4(type){
win.doc.addEventListener(type,function(e){
if(_6b7&&!e._dojo_click&&(new Date()).getTime()<=_6be+1000&&!(e.target.tagName=="INPUT"&&_6af.contains(e.target,"dijitOffScreen"))){
e.stopPropagation();
e.stopImmediatePropagation&&e.stopImmediatePropagation();
if(type=="click"&&(e.target.tagName!="INPUT"||e.target.type=="radio"||e.target.type=="checkbox")&&e.target.tagName!="TEXTAREA"&&e.target.tagName!="AUDIO"&&e.target.tagName!="VIDEO"){
e.preventDefault();
}
}
},true);
};
_6d4("click");
_6d4("mousedown");
_6d4("mouseup");
}
}
};
var _6d5;
if(has("touch")){
if(_6b2){
_6b1(function(){
win.doc.addEventListener(_6b3.down,function(evt){
_6ca(evt,_6b3.move,_6b3.up);
},true);
});
}else{
_6b1(function(){
_6d5=win.body();
win.doc.addEventListener("touchstart",function(evt){
_6bf=(new Date()).getTime();
var _6d6=_6d5;
_6d5=evt.target;
on.emit(_6d6,"dojotouchout",{relatedTarget:_6d5,bubbles:true});
on.emit(_6d5,"dojotouchover",{relatedTarget:_6d6,bubbles:true});
_6ca(evt,"touchmove","touchend");
},true);
function _6d7(evt){
var _6d8=lang.delegate(evt,{bubbles:true});
if(has("ios")>=6){
_6d8.touches=evt.touches;
_6d8.altKey=evt.altKey;
_6d8.changedTouches=evt.changedTouches;
_6d8.ctrlKey=evt.ctrlKey;
_6d8.metaKey=evt.metaKey;
_6d8.shiftKey=evt.shiftKey;
_6d8.targetTouches=evt.targetTouches;
}
return _6d8;
};
on(win.doc,"touchmove",function(evt){
_6bf=(new Date()).getTime();
var _6d9=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset));
if(_6d9){
if(_6d5!==_6d9){
on.emit(_6d5,"dojotouchout",{relatedTarget:_6d9,bubbles:true});
on.emit(_6d9,"dojotouchover",{relatedTarget:_6d5,bubbles:true});
_6d5=_6d9;
}
if(!on.emit(_6d9,"dojotouchmove",_6d7(evt))){
evt.preventDefault();
}
}
});
on(win.doc,"touchend",function(evt){
_6bf=(new Date()).getTime();
var node=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset))||win.body();
on.emit(node,"dojotouchend",_6d7(evt));
});
});
}
}
var _6da={press:_6c0("mousedown","touchstart",_6b3.down),move:_6c0("mousemove","dojotouchmove",_6b3.move),release:_6c0("mouseup","dojotouchend",_6b3.up),cancel:_6c0(_6b0.leave,"touchcancel",_6b2?_6b3.cancel:null),over:_6c0("mouseover","dojotouchover",_6b3.over),out:_6c0("mouseout","dojotouchout",_6b3.out),enter:_6b0._eventHandler(_6c0("mouseover","dojotouchover",_6b3.over)),leave:_6b0._eventHandler(_6c0("mouseout","dojotouchout",_6b3.out))};
1&&(dojo.touch=_6da);
return _6da;
});
},"dojo/cache":function(){
define(["./_base/kernel","./text"],function(dojo){
return dojo.cache;
});
},"dijit/DialogUnderlay":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/aspect","dojo/dom-attr","dojo/dom-style","dojo/on","dojo/window","./_Widget","./_TemplatedMixin","./BackgroundIframe","./Viewport","./main"],function(_6db,lang,_6dc,_6dd,_6de,on,_6df,_6e0,_6e1,_6e2,_6e3,_6e4){
var _6e5=_6db("dijit.DialogUnderlay",[_6e0,_6e1],{templateString:"<div class='dijitDialogUnderlayWrapper'><div class='dijitDialogUnderlay' tabIndex='-1' data-dojo-attach-point='node'></div></div>",dialogId:"","class":"",_modalConnects:[],_setDialogIdAttr:function(id){
_6dd.set(this.node,"id",id+"_underlay");
this._set("dialogId",id);
},_setClassAttr:function(_6e6){
this.node.className="dijitDialogUnderlay "+_6e6;
this._set("class",_6e6);
},postCreate:function(){
this.ownerDocumentBody.appendChild(this.domNode);
this.own(on(this.domNode,"keydown",lang.hitch(this,"_onKeyDown")));
this.inherited(arguments);
},layout:function(){
var is=this.node.style,os=this.domNode.style;
os.display="none";
var _6e7=_6df.getBox(this.ownerDocument);
os.top=_6e7.t+"px";
os.left=_6e7.l+"px";
is.width=_6e7.w+"px";
is.height=_6e7.h+"px";
os.display="block";
},show:function(){
this.domNode.style.display="block";
this.open=true;
this.layout();
this.bgIframe=new _6e2(this.domNode);
var win=_6df.get(this.ownerDocument);
this._modalConnects=[_6e3.on("resize",lang.hitch(this,"layout")),on(win,"scroll",lang.hitch(this,"layout"))];
},hide:function(){
this.bgIframe.destroy();
delete this.bgIframe;
this.domNode.style.display="none";
while(this._modalConnects.length){
(this._modalConnects.pop()).remove();
}
this.open=false;
},destroy:function(){
while(this._modalConnects.length){
(this._modalConnects.pop()).remove();
}
this.inherited(arguments);
},_onKeyDown:function(){
}});
_6e5.show=function(_6e8,_6e9){
var _6ea=_6e5._singleton;
if(!_6ea||_6ea._destroyed){
_6ea=_6e4._underlay=_6e5._singleton=new _6e5(_6e8);
}else{
if(_6e8){
_6ea.set(_6e8);
}
}
_6de.set(_6ea.domNode,"zIndex",_6e9);
if(!_6ea.open){
_6ea.show();
}
};
_6e5.hide=function(){
var _6eb=_6e5._singleton;
if(_6eb&&!_6eb._destroyed){
_6eb.hide();
}
};
return _6e5;
});
},"dijit/form/_ToggleButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr"],function(_6ec,_6ed){
return _6ec("dijit.form._ToggleButtonMixin",null,{checked:false,_aria_attr:"aria-pressed",_onClick:function(evt){
var _6ee=this.checked;
this._set("checked",!_6ee);
var ret=this.inherited(arguments);
this.set("checked",ret?this.checked:_6ee);
return ret;
},_setCheckedAttr:function(_6ef,_6f0){
this._set("checked",_6ef);
var node=this.focusNode||this.domNode;
if(this._created){
if(_6ed.get(node,"checked")!=!!_6ef){
_6ed.set(node,"checked",!!_6ef);
}
}
node.setAttribute(this._aria_attr,String(_6ef));
this._handleOnChange(_6ef,_6f0);
},postCreate:function(){
this.inherited(arguments);
var node=this.focusNode||this.domNode;
if(this.checked){
node.setAttribute("checked","checked");
}
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.checked;
}
},reset:function(){
this._hasBeenBlurred=false;
this.set("checked",this.params.checked||false);
}});
});
},"curam/layout/CuramStackController":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/_base/array","dojo/dom-attr","dijit/form/ToggleButton","dojo/text!curam/layout/resources/CuramStackButton.html","dijit/layout/StackController"],function(_6f1,lang,_6f2,attr,_6f3,_6f4,_6f5){
var _6f6=_6f1("curam.layout._StackButton",_6f3,{tabIndex:"0",templateString:_6f4,imageDefaultSrc:"",imageHoverSrc:"",imageAltText:"",closeButton:false,_aria_attr:"aria-selected",buildRendering:function(evt){
this.inherited(arguments);
(this.focusNode||this.domNode).setAttribute("role","tab");
}});
var _6f7=_6f1("curam.layout.CuramStackController",_6f5,{baseClass:"curamStackController",buttonWidget:_6f6,buttonWidgetCloseClass:"dijitStackCloseButton",onAddChild:function(page,_6f8){
var _6f9="../Images/avatar--20-enabled.svg";
var _6fa="../Images/avatar--20-enabled.svg";
var _6fb="../Images/list--20-enabled.svg";
var _6fc="../Images/list--20-enabled.svg";
var _6fd=page.get("class").indexOf("stack-container-photo")!=-1?true:false;
var _6fe=_6fd?_6f9:_6fb;
var _6ff=_6fd?_6fa:_6fc;
var Cls=lang.isString(this.buttonWidget)?lang.getObject(this.buttonWidget):this.buttonWidget;
var _700=new Cls({id:this.id+"_"+page.id,name:this.id+"_"+page.id,disabled:page.disabled,ownerDocument:this.ownerDocument,dir:page.dir,lang:page.lang,textDir:page.textDir||this.textDir,showLabel:page.showTitle,imageDefaultSrc:_6fe,imageHoverSrc:_6ff,imageAltText:page.title,iconClass:page.iconClass,closeButton:page.closable,title:page.tooltip,page:page});
this.addChild(_700,_6f8);
page.controlButton=_700;
if(!this._currentChild){
this.onSelectChild(page);
}
var _701=attr.get(_700.domNode,"widgetid");
if(typeof (_701)!="undefined"){
_701.split("_");
var _702=_701.indexOf("2")!=-1?true:false;
if(_702){
var _703=dojo.query("table.list-body >tbody>tr>td.field.body-first-cell>a.ac",this.ownerDocument);
if(_703){
_6f2.forEach(_703,function(_704){
attr.set(_704,"class",_704.className.concat("list-view"));
});
}
}
}
var _705=page._wrapper.getAttribute("aria-labelledby")?page._wrapper.getAttribute("aria-labelledby")+" "+_700.id:_700.id;
page._wrapper.removeAttribute("aria-label");
page._wrapper.setAttribute("aria-labelledby",_705);
}});
_6f7.StackButton=_6f6;
return _6f7;
});
},"curam/cdsl/types/FrequencyPattern":function(){
define(["dojo/_base/declare","dojo/_base/lang"],function(_706){
var _707=_706(null,{_code:null,_description:null,constructor:function(code,_708){
this._code=code;
this._description=_708;
},getCode:function(){
return this._code;
},getDescription:function(){
return this._description;
}});
return _707;
});
},"dojo/store/util/SimpleQueryEngine":function(){
define(["../../_base/array"],function(_709){
return function(_70a,_70b){
switch(typeof _70a){
default:
throw new Error("Can not query with a "+typeof _70a);
case "object":
case "undefined":
var _70c=_70a;
_70a=function(_70d){
for(var key in _70c){
var _70e=_70c[key];
if(_70e&&_70e.test){
if(!_70e.test(_70d[key],_70d)){
return false;
}
}else{
if(_70e!=_70d[key]){
return false;
}
}
}
return true;
};
break;
case "string":
if(!this[_70a]){
throw new Error("No filter function "+_70a+" was found in store");
}
_70a=this[_70a];
case "function":
}
function _70f(_710){
var _711=_709.filter(_710,_70a);
var _712=_70b&&_70b.sort;
if(_712){
_711.sort(typeof _712=="function"?_712:function(a,b){
for(var sort,i=0;sort=_712[i];i++){
var _713=a[sort.attribute];
var _714=b[sort.attribute];
_713=_713!=null?_713.valueOf():_713;
_714=_714!=null?_714.valueOf():_714;
if(_713!=_714){
return !!sort.descending==(_713==null||_713>_714)?-1:1;
}
}
return 0;
});
}
if(_70b&&(_70b.start||_70b.count)){
var _715=_711.length;
_711=_711.slice(_70b.start||0,(_70b.start||0)+(_70b.count||Infinity));
_711.total=_715;
}
return _711;
};
_70f.matches=_70a;
return _70f;
};
});
},"dijit/_TemplatedMixin":function(){
define(["dojo/cache","dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/on","dojo/sniff","dojo/string","./_AttachMixin"],function(_716,_717,_718,lang,on,has,_719,_71a){
var _71b=_717("dijit._TemplatedMixin",_71a,{templateString:null,templatePath:null,_skipNodeCache:false,searchContainerNode:true,_stringRepl:function(tmpl){
var _71c=this.declaredClass,_71d=this;
return _719.substitute(tmpl,this,function(_71e,key){
if(key.charAt(0)=="!"){
_71e=lang.getObject(key.substr(1),false,_71d);
}
if(typeof _71e=="undefined"){
throw new Error(_71c+" template:"+key);
}
if(_71e==null){
return "";
}
return key.charAt(0)=="!"?_71e:this._escapeValue(""+_71e);
},this);
},_escapeValue:function(val){
return val.replace(/["'<>&]/g,function(val){
return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;"}[val];
});
},buildRendering:function(){
if(!this._rendered){
if(!this.templateString){
this.templateString=_716(this.templatePath,{sanitize:true});
}
var _71f=_71b.getCachedTemplate(this.templateString,this._skipNodeCache,this.ownerDocument);
var node;
if(lang.isString(_71f)){
node=_718.toDom(this._stringRepl(_71f),this.ownerDocument);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_71f);
}
}else{
node=_71f.cloneNode(true);
}
this.domNode=node;
}
this.inherited(arguments);
if(!this._rendered){
this._fillContent(this.srcNodeRef);
}
this._rendered=true;
},_fillContent:function(_720){
var dest=this.containerNode;
if(_720&&dest){
while(_720.hasChildNodes()){
dest.appendChild(_720.firstChild);
}
}
}});
_71b._templateCache={};
_71b.getCachedTemplate=function(_721,_722,doc){
var _723=_71b._templateCache;
var key=_721;
var _724=_723[key];
if(_724){
try{
if(!_724.ownerDocument||_724.ownerDocument==(doc||document)){
return _724;
}
}
catch(e){
}
_718.destroy(_724);
}
_721=_719.trim(_721);
if(_722||_721.match(/\$\{([^\}]+)\}/g)){
return (_723[key]=_721);
}else{
var node=_718.toDom(_721,doc);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_721);
}
return (_723[key]=node);
}
};
if(has("ie")){
on(window,"unload",function(){
var _725=_71b._templateCache;
for(var key in _725){
var _726=_725[key];
if(typeof _726=="object"){
_718.destroy(_726);
}
delete _725[key];
}
});
}
return _71b;
});
},"curam/util/ui/refresh/RefreshEvent":function(){
define(["dojo/_base/declare"],function(_727){
var _728=_727("curam.util.ui.refresh.RefreshEvent",null,{TYPE_ONLOAD:"onload",TYPE_ONSUBMIT:"onsubmit",SOURCE_CONTEXT_MAIN:"main-content",SOURCE_CONTEXT_DIALOG:"dialog",SOURCE_CONTEXT_INLINE:"inline",_type:null,_context:null,constructor:function(type,_729){
if(!type||!_729){
throw "Required parameters missing.";
}
if(!(type==this.TYPE_ONLOAD||type==this.TYPE_ONSUBMIT)){
throw "Unknown type: "+type;
}
if(!(_729==this.SOURCE_CONTEXT_DIALOG||_729==this.SOURCE_CONTEXT_INLINE||_729==this.SOURCE_CONTEXT_MAIN)){
throw "Unknown context: "+_729;
}
this._type=type;
this._context=_729;
},equals:function(_72a){
if(typeof _72a!="object"){
return false;
}
if(_72a.declaredClass!=this.declaredClass){
return false;
}
return this._type===_72a._type&&this._context===_72a._context;
}});
return _728;
});
},"curam/ui/UIMPageAdaptor":function(){
define(["dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/query","curam/tab","curam/define","curam/debug","curam/util","curam/ui/PageRequest"],function(dom,_72b,_72c,_72d,tab,_72e,_72f){
_72e.singleton("curam.ui.UIMPageAdaptor",{initialize:function(){
if(jsScreenContext.hasContextBits("MODAL")){
return;
}
curam.util.connect(dojo.body(),"onclick",curam.ui.UIMPageAdaptor.clickHandler);
var _730=null;
var _731=null;
if(!jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
_730=curam.util.getTopmostWindow().dojo;
_731="/iframe-loaded/"+window.jsPageID;
_72f.log(_72f.getProperty("curam.ui.UIMPageAdaptor.event")+_731);
_730.publish(_731);
}
},externalInitialize:function(){
if(jsScreenContext.hasContextBits("MODAL")){
return;
}
curam.util.connect(dojo.body(),"onclick",curam.ui.UIMPageAdaptor.clickHandler);
},externalClickHandler:function(_732,_733){
var _734=new curam.ui.PageRequest(_733.href);
var _735=window.top.dijit.byId("curam-app");
if(_735!=null){
var _736=[];
var i=0;
for(param in _734.parameters){
_736[i]={paramKey:param,paramValue:_734.parameters[param]};
i=i+1;
}
var args={pageID:_734.pageID,param:_736};
if(_735._isNavBarItem(_734.pageID)){
dojo.stopEvent(_732||window.event);
window.top.displayContent(args);
}else{
if(_735._isUIMFragment(_734.pageID)){
dojo.stopEvent(_732||window.event);
window.top.displayContent(args);
}
}
}
},clickHandler:function(_737){
var _738=null;
if(_737.target.nodeName=="A"){
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_737.target)){
return;
}
_738=_737.target;
}else{
if((_737.target.nodeName=="IMG"&&!_72c.contains(_737.target.parentNode,"file-download"))||(_737.target.nodeName=="SPAN"&&(_737.target.className=="middle"||_737.target.className=="bidi"))){
_738=_72d(_737.target).closest("A")[0];
}
}
if(_738!=null){
if(!_738.href||_738.href.length==0){
dojo.stopEvent(_737||window.event);
return;
}
if(jsScreenContext.hasContextBits("EXTAPP")){
curam.ui.UIMPageAdaptor.externalClickHandler(_737,_738);
}else{
dojo.stopEvent(_737||window.event);
if(curam.ui.UIMPageAdaptor.shouldLinkOpenInNewWindow(_738)){
window.open(_738.href);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_738)){
var _739=new curam.ui.PageRequest(_738.href);
if(jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||jsScreenContext.hasContextBits("NESTED_UIM")){
_739.pageHolder=window;
}
if(window.jsPageID===_739.pageID){
_739.forceLoad=true;
}
curam.tab.getTabController().handlePageRequest(_739);
}
}
}
}
},allowLinkToContinue:function(_73a){
if(_73a&&_73a._submitButton){
return true;
}
if(_73a&&_73a.href){
return (_73a.href.indexOf("/servlet/FileDownload")!=-1||_73a.href.indexOf("#")!=-1||_73a.href.substr(0,7)=="mailto:");
}else{
return false;
}
},isLinkValidForTabProcessing:function(_73b){
if(!_73b||(_72c.contains(_73b,"popup-action")||_72c.contains(_73b,"list-details-row-toggle"))||_73b.protocol=="javascript:"){
return false;
}
return true;
},shouldLinkOpenInNewWindow:function(_73c){
return _72b.has(_73c,"target")&&!curam.util.isInternal(_73c.href);
},setTabTitleAndName:function(){
var _73d=dom.byId("tab-title").innerHTML;
var _73e=dom.byId("tab-name").innerHTML;
window.parent.dojo.publish("tab.title.name.set",[window.frameElement,_73d,_73e]);
}});
return curam.ui.UIMPageAdaptor;
});
},"curam/ModalDialog":function(){
define(["dojo/text!curam/layout/resources/CuramBaseModal.html","dojo/_base/declare","dojo/dom","curam/modal/CuramBaseModal","dijit/Dialog"],function(_73f,_740,dom,_741){
tabbingBackwards=null,handleTitlebarIconKeydown=function(e){
};
var _742=_740("curam.ModalDialog",[dijit.Dialog,_741],{constructor:function(){
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
},handleFocusAtEnd:function(_743,_744){
this.handleTabbingForwards(_743,_744);
}});
_742.handleFocusAtEnd=function(_745){
var _746=dojo.query("#"+this.id+" .dijitDialogHelpIcon")[0];
curam.ModalDialog.prototype.handleFocusAtEnd(_745,_746);
};
return _742;
});
},"curam/widget/OptimalBrowserMessage":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/dom","dojo/dom-class","curam/util","curam/util/UIMFragment","curam/ui/ClientDataAccessor","dijit/_Widget","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/form/Button","dojo/has","dojo/text!curam/widget/templates/OptimalBrowserMessage.html","dojo/dom-attr"],function(_747,lang,dom,_748,util,_749,_74a,_74b,_74c,_74d,_74e,_74f,_750,has,_751,_752){
return _747("curam.widget.OptimalBrowserMessage",[_74b,_74c,_74d],{OPTIMAL_BROWSER_MSG:"optimal-browser-msg",isExternalApp:null,optimalBrowserMsgPaddingCSS:"optimal-browser-banner",optimalBrowserNode:null,appSectionsNode:null,appBannerHeaderNode:null,intApp:"internal",extApp:"external",templateString:_751,widgetsInTemplate:true,baseClass:"",optimalBrowserNodeID:"_optimalMessage",_appConfig:null,postMixInProperties:function(){
this.inherited(arguments);
},startup:function(){
this.inherited(arguments);
this._init();
this._loadNodes(this._optimalMessage.id);
},_init:function(){
da=new _74a();
da.getRaw("/config/tablayout/settings["+curam.config.appID+"]",lang.hitch(this,function(data){
console.log("External App config data:"+data);
this._appConfig=data;
this._getAppConfig();
this._updateOptimalBrowserNode();
}),function(_753,args){
console.log("External App config data load error:"+_753);
},null);
},_getAppConfig:function(){
var _754=this._appConfig.optimalBrowserMessageEnabled;
var _755=this._createStorageKey(this.OPTIMAL_BROWSER_MSG);
var _756=this;
if(_754=="true"|_754=="TRUE"){
return _756._isOptimalBrowserCheckDue(_755,_756);
}
return false;
},_updateOptimalBrowserNode:function(){
this.optimalBrowserNode=dom.byId(this._optimalMessage.id);
_752.set(optimalBrowserNode,"aria-label",this._appConfig.optimalMessageDivLabel);
},_isOptimalBrowserCheckDue:function(_757,_758){
var _759=localStorage[_757];
if(_759&&_759!=""){
if(new Date(_758._getTargetDate())>new Date(_759)){
_758._executeBrowserVersionCheck();
return true;
}
}else{
_758._executeBrowserVersionCheck();
return true;
}
return false;
},_executeBrowserVersionCheck:function(){
var _75a=this._appConfig.ieMinVersion;
var _75b=this._appConfig.ieMaxVersion;
var _75c=this._appConfig.ffMinVersion;
var _75d=this._appConfig.ffMaxVersion;
var _75e=this._appConfig.chromeMinVersion;
var _75f=this._appConfig.chromeMaxVersion;
var _760=this._appConfig.safariMinVersion;
var _761=this._appConfig.safariMaxVersion;
var _762=dojo.isIE;
var _763=has("trident")||has("edge");
var _764=dojo.isFF;
var _765=dojo.isChrome;
var _766=dojo.isSafari;
if(_762!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_762,_75a,_75b);
}else{
if(_763>6&&this.isExternalApp){
var _767=_75a-4;
var _768=_75b-4;
return this._isCurrentBrowserVerSupported(_763,_767,_768);
}else{
if(_764!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_764,_75c,_75d);
}else{
if(_765!=undefined){
return this._isCurrentBrowserVerSupported(_765,_75e,_75f);
}else{
if(_766!=undefined&&this.isExternalApp){
return this._isCurrentBrowserVerSupported(_766,_760,_761);
}
}
}
}
}
return false;
},_isCurrentBrowserVerSupported:function(_769,_76a,_76b){
var _76c=false;
if(_76a>0){
if(_769<_76a){
_76c=true;
this._displayOptimalBrowserMsg();
return true;
}
}
if(_76b>0&&!_76c){
if(_769>_76b){
this._displayOptimalBrowserMsg();
return true;
}
}
return false;
},_displayOptimalBrowserMsg:function(){
this._addOrRemoveCssForInternalApp(true,this.optimalBrowserMsgPaddingCSS);
_749.get({url:"optimal-browser-msg-fragment.jspx",targetID:this._optimalMessage.id});
this._postRenderingTasks();
},_postRenderingTasks:function(){
var _76d=this._optimalMessage.id;
dojo.addOnLoad(function(){
var _76e=dom.byId(_76d);
_748.remove(_76e,_76e.className);
});
localStorage[this._createStorageKey(this.OPTIMAL_BROWSER_MSG)]=this._getTargetDate(this._appConfig.nextBrowserCheck);
},_loadNodes:function(_76f){
dojo.addOnLoad(function(){
this.optimalBrowserNode=dom.byId(_76f);
this.appSectionsNode=dom.byId("app-sections-container-dc");
this.appBannerHeaderNode=dom.byId("app-header-container-dc");
});
},_createStorageKey:function(_770){
if(this.isExternalApp){
_770=_770+"_"+this.extApp;
}else{
_770=_770+"_"+this.intApp;
}
return _770;
},_addOrRemoveCssForInternalApp:function(_771,_772){
var _773=this.isExternalApp;
dojo.addOnLoad(function(){
if(!_773){
if(_771){
_748.add(this.appSectionsNode,_772);
if(this.appBannerHeaderNode){
_748.add(this.appSectionsNode.children.item(1),_772);
_748.add(this.appSectionsNode.children.item(2),_772);
}
}else{
_748.remove(this.appSectionsNode,_772);
if(this.appBannerHeaderNode){
_748.remove(this.appSectionsNode.children.item(1),_772);
_748.remove(this.appSectionsNode.children.item(2),_772);
}
}
}
});
},_getTargetDate:function(_774){
var _775=new Date();
if(_774==undefined){
_775.setDate(_775.getDate());
}else{
_775.setDate(_775.getDate()+_774);
}
return _775.toUTCString();
},exitOptimalBrowserMessageBox:function(){
var _776=dom.byId(this._optimalMessage.id);
if(_776){
_776.parentNode.removeChild(_776);
}
this._addOrRemoveCssForInternalApp(false,this.optimalBrowserMsgPaddingCSS);
}});
});
},"dijit/_KeyNavMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","dijit/registry","dijit/_FocusMixin"],function(_777,_778,_779,keys,lang,on,_77a,_77b){
return _778("dijit._KeyNavMixin",_77b,{tabIndex:"0",childSelector:null,postCreate:function(){
this.inherited(arguments);
_779.set(this.domNode,"tabIndex",this.tabIndex);
if(!this._keyNavCodes){
var _77c=this._keyNavCodes={};
_77c[keys.HOME]=lang.hitch(this,"focusFirstChild");
_77c[keys.END]=lang.hitch(this,"focusLastChild");
_77c[this.isLeftToRight()?keys.LEFT_ARROW:keys.RIGHT_ARROW]=lang.hitch(this,"_onLeftArrow");
_77c[this.isLeftToRight()?keys.RIGHT_ARROW:keys.LEFT_ARROW]=lang.hitch(this,"_onRightArrow");
_77c[keys.UP_ARROW]=lang.hitch(this,"_onUpArrow");
_77c[keys.DOWN_ARROW]=lang.hitch(this,"_onDownArrow");
}
var self=this,_77d=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.domNode,"keypress",lang.hitch(this,"_onContainerKeypress")),on(this.domNode,"keydown",lang.hitch(this,"_onContainerKeydown")),on(this.domNode,"focus",lang.hitch(this,"_onContainerFocus")),on(this.containerNode,on.selector(_77d,"focusin"),function(evt){
self._onChildFocus(_77a.getEnclosingWidget(this),evt);
}));
},_onLeftArrow:function(){
},_onRightArrow:function(){
},_onUpArrow:function(){
},_onDownArrow:function(){
},focus:function(){
this.focusFirstChild();
},_getFirstFocusableChild:function(){
return this._getNextFocusableChild(null,1);
},_getLastFocusableChild:function(){
return this._getNextFocusableChild(null,-1);
},focusFirstChild:function(){
this.focusChild(this._getFirstFocusableChild());
},focusLastChild:function(){
this.focusChild(this._getLastFocusableChild());
},focusChild:function(_77e,last){
if(!_77e){
return;
}
if(this.focusedChild&&_77e!==this.focusedChild){
this._onChildBlur(this.focusedChild);
}
_77e.set("tabIndex",this.tabIndex);
_77e.focus(last?"end":"start");
},_onContainerFocus:function(evt){
if(evt.target!==this.domNode||this.focusedChild){
return;
}
this.focus();
},_onFocus:function(){
_779.set(this.domNode,"tabIndex","-1");
this.inherited(arguments);
},_onBlur:function(evt){
_779.set(this.domNode,"tabIndex",this.tabIndex);
if(this.focusedChild){
this.focusedChild.set("tabIndex","-1");
this.lastFocusedChild=this.focusedChild;
this._set("focusedChild",null);
}
this.inherited(arguments);
},_onChildFocus:function(_77f){
if(_77f&&_77f!=this.focusedChild){
if(this.focusedChild&&!this.focusedChild._destroyed){
this.focusedChild.set("tabIndex","-1");
}
_77f.set("tabIndex",this.tabIndex);
this.lastFocused=_77f;
this._set("focusedChild",_77f);
}
},_searchString:"",multiCharSearchDuration:1000,onKeyboardSearch:function(item,evt,_780,_781){
if(item){
this.focusChild(item);
}
},_keyboardSearchCompare:function(item,_782){
var _783=item.domNode,text=item.label||(_783.focusNode?_783.focusNode.label:"")||_783.innerText||_783.textContent||"",_784=text.replace(/^\s+/,"").substr(0,_782.length).toLowerCase();
return (!!_782.length&&_784==_782)?-1:0;
},_onContainerKeydown:function(evt){
var func=this._keyNavCodes[evt.keyCode];
if(func){
func(evt,this.focusedChild);
evt.stopPropagation();
evt.preventDefault();
this._searchString="";
}else{
if(evt.keyCode==keys.SPACE&&this._searchTimer&&!(evt.ctrlKey||evt.altKey||evt.metaKey)){
evt.stopImmediatePropagation();
evt.preventDefault();
this._keyboardSearch(evt," ");
}
}
},_onContainerKeypress:function(evt){
if(evt.charCode<=keys.SPACE||evt.ctrlKey||evt.altKey||evt.metaKey){
return;
}
evt.preventDefault();
evt.stopPropagation();
this._keyboardSearch(evt,String.fromCharCode(evt.charCode).toLowerCase());
},_keyboardSearch:function(evt,_785){
var _786=null,_787,_788=0,_789=lang.hitch(this,function(){
if(this._searchTimer){
this._searchTimer.remove();
}
this._searchString+=_785;
var _78a=/^(.)\1*$/.test(this._searchString);
var _78b=_78a?1:this._searchString.length;
_787=this._searchString.substr(0,_78b);
this._searchTimer=this.defer(function(){
this._searchTimer=null;
this._searchString="";
},this.multiCharSearchDuration);
var _78c=this.focusedChild||null;
if(_78b==1||!_78c){
_78c=this._getNextFocusableChild(_78c,1);
if(!_78c){
return;
}
}
var stop=_78c;
do{
var rc=this._keyboardSearchCompare(_78c,_787);
if(!!rc&&_788++==0){
_786=_78c;
}
if(rc==-1){
_788=-1;
break;
}
_78c=this._getNextFocusableChild(_78c,1);
}while(_78c&&_78c!=stop);
});
_789();
this.onKeyboardSearch(_786,evt,_787,_788);
},_onChildBlur:function(){
},_getNextFocusableChild:function(_78d,dir){
var _78e=_78d;
do{
if(!_78d){
_78d=this[dir>0?"_getFirst":"_getLast"]();
if(!_78d){
break;
}
}else{
_78d=this._getNext(_78d,dir);
}
if(_78d!=null&&_78d!=_78e&&_78d.isFocusable()){
return _78d;
}
}while(_78d!=_78e);
return null;
},_getFirst:function(){
return null;
},_getLast:function(){
return null;
},_getNext:function(_78f,dir){
if(_78f){
_78f=_78f.domNode;
while(_78f){
_78f=_78f[dir<0?"previousSibling":"nextSibling"];
if(_78f&&"getAttribute" in _78f){
var w=_77a.byNode(_78f);
if(w){
return w;
}
}
}
}
return null;
}});
});
},"curam/util/EditableList":function(){
define(["dojo/dom-class","dojo/dom-attr","curam/debug","curam/define","dojo/query","dojo/NodeList-traverse"],function(_790,_791,_792){
curam.define.singleton("curam.util.EditableList",{onload:function(_793){
var _794=dojo.query("div.list table tbody td input[type = 'checkbox']");
if(_794[0]==null){
return "Outside List";
}
_794.forEach(function(node){
curam.debug.log("curam.util.EditableList onload()");
curam.util.EditableList._doToggling(node,_793);
});
return "In List";
},toggle:function(_795,_796){
curam.debug.log("curam.util.EditableList: "+_792.getProperty("curam.util.EditableList.toggle"));
_795=dojo.fixEvent(_795);
if(!_795.target){
return "Improper Event";
}
var _797=_795.target;
curam.util.EditableList._doToggling(_797,_796);
return "Event Processed";
},_doToggling:function(_798,_799){
var _79a=_798;
while(_79a&&!_790.contains(_79a,"list")){
_79a=_79a.parentNode;
}
if(_79a==null){
return "Outside List";
}
var _79b=dojo.query(_798).closest("TR")[0];
if(_799){
curam.util.EditableList._updateCheckboxAccessibility(_79b,_799);
}
if(_798.checked==true){
isChecked=true;
curam.debug.log(_792.getProperty("curam.util.EditableList.ticking"));
}else{
isChecked=false;
curam.debug.log(_792.getProperty("curam.util.EditableList.unticking"));
}
if(_79b==null){
throw new Error("Exception: The TR node is not found");
}
dojo.query("td > *",_79b).forEach(function(node){
if(_790.contains(node,"text")){
if(isChecked){
_791.remove(node,"disabled");
_790.remove(node,"disabled");
curam.debug.log(_792.getProperty("curam.util.EditableList.enable.field"));
}else{
_791.set(node,"disabled","disable");
_790.add(node,"disabled");
curam.debug.log(_792.getProperty("curam.util.EditableList.disable.field"));
}
}else{
if(_790.contains(node,"codetable")){
var _79c=_791.get(node,"widgetid");
var _79d=dijit.byId(_79c);
if(isChecked){
_79d.set("disabled",false);
curam.debug.log(_792.getProperty("curam.util.EditableList.enable.ct"));
}else{
_79d.set("disabled",true);
curam.debug.log(_792.getProperty("curam.util.EditableList.disable.ct"));
}
}
}
});
return "Toggled";
},_updateCheckboxAccessibility:function(tr,_79e){
var _79f=dojo.query("td",tr);
var _7a0=_79f.parents("table");
var _7a1=_79f[0];
var _7a2=_79f[1];
var _7a3="";
var _7a4="";
if(_7a1&&_7a1.firstElementChild){
if(_7a1.firstElementChild.nodeName=="INPUT"&&_7a1.firstElementChild.getAttribute("type")=="checkbox"){
if(_7a2.firstChild){
if(_7a2.firstChild.nodeType==3){
_7a3=_7a2.firstChild.textContent;
}else{
if(_7a1.firstElementChild.nodeName=="INPUT"){
_7a3=_7a2.firstElementChild.getAttribute("value");
}
}
}
if(_7a3&&_7a3.length>0){
_7a4=_79e+" - "+_7a3;
}else{
_7a4=_79e;
}
if(_7a0[0]&&_7a0[0].getAttribute("summary")){
_7a4=_7a0[0].getAttribute("summary")+" - "+_7a4;
}
_7a1.firstElementChild.setAttribute("aria-label",_7a4);
_7a1.firstElementChild.title=_7a4;
}
}
return _7a1;
}});
return curam.util.EditableList;
});
},"dijit/form/_TextBoxMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/sniff","dojo/keys","dojo/_base/lang","dojo/on","../main"],function(_7a5,_7a6,dom,has,keys,lang,on,_7a7){
var _7a8=_7a6("dijit.form._TextBoxMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{trim:false,uppercase:false,lowercase:false,propercase:false,maxLength:"",selectOnClick:false,placeHolder:"",_getValueAttr:function(){
return this.parse(this.get("displayedValue"),this.constraints);
},_setValueAttr:function(_7a9,_7aa,_7ab){
var _7ac;
if(_7a9!==undefined){
_7ac=this.filter(_7a9);
if(typeof _7ab!="string"){
if(_7ac!==null&&((typeof _7ac!="number")||!isNaN(_7ac))){
_7ab=this.filter(this.format(_7ac,this.constraints));
}else{
_7ab="";
}
if(this.compare(_7ac,this.filter(this.parse(_7ab,this.constraints)))!=0){
_7ab=null;
}
}
}
if(_7ab!=null&&((typeof _7ab)!="number"||!isNaN(_7ab))&&this.textbox.value!=_7ab){
this.textbox.value=_7ab;
this._set("displayedValue",this.get("displayedValue"));
}
this.inherited(arguments,[_7ac,_7aa]);
},displayedValue:"",_getDisplayedValueAttr:function(){
return this.filter(this.textbox.value);
},_setDisplayedValueAttr:function(_7ad){
if(_7ad==null){
_7ad="";
}else{
if(typeof _7ad!="string"){
_7ad=String(_7ad);
}
}
this.textbox.value=_7ad;
this._setValueAttr(this.get("value"),undefined);
this._set("displayedValue",this.get("displayedValue"));
},format:function(_7ae){
return _7ae==null?"":(_7ae.toString?_7ae.toString():_7ae);
},parse:function(_7af){
return _7af;
},_refreshState:function(){
},onInput:function(){
},_onInput:function(evt){
this._lastInputEventValue=this.textbox.value;
this._processInput(this._lastInputProducingEvent||evt);
delete this._lastInputProducingEvent;
if(this.intermediateChanges){
this._handleOnChange(this.get("value"),false);
}
},_processInput:function(){
this._refreshState();
this._set("displayedValue",this.get("displayedValue"));
},postCreate:function(){
this.textbox.setAttribute("value",this.textbox.value);
this.inherited(arguments);
function _7b0(e){
var _7b1;
if(e.type=="keydown"&&e.keyCode!=229){
_7b1=e.keyCode;
switch(_7b1){
case keys.SHIFT:
case keys.ALT:
case keys.CTRL:
case keys.META:
case keys.CAPS_LOCK:
case keys.NUM_LOCK:
case keys.SCROLL_LOCK:
return;
}
if(!e.ctrlKey&&!e.metaKey&&!e.altKey){
switch(_7b1){
case keys.NUMPAD_0:
case keys.NUMPAD_1:
case keys.NUMPAD_2:
case keys.NUMPAD_3:
case keys.NUMPAD_4:
case keys.NUMPAD_5:
case keys.NUMPAD_6:
case keys.NUMPAD_7:
case keys.NUMPAD_8:
case keys.NUMPAD_9:
case keys.NUMPAD_MULTIPLY:
case keys.NUMPAD_PLUS:
case keys.NUMPAD_ENTER:
case keys.NUMPAD_MINUS:
case keys.NUMPAD_PERIOD:
case keys.NUMPAD_DIVIDE:
return;
}
if((_7b1>=65&&_7b1<=90)||(_7b1>=48&&_7b1<=57)||_7b1==keys.SPACE){
return;
}
var _7b2=false;
for(var i in keys){
if(keys[i]===e.keyCode){
_7b2=true;
break;
}
}
if(!_7b2){
return;
}
}
}
_7b1=e.charCode>=32?String.fromCharCode(e.charCode):e.charCode;
if(!_7b1){
_7b1=(e.keyCode>=65&&e.keyCode<=90)||(e.keyCode>=48&&e.keyCode<=57)||e.keyCode==keys.SPACE?String.fromCharCode(e.keyCode):e.keyCode;
}
if(!_7b1){
_7b1=229;
}
if(e.type=="keypress"){
if(typeof _7b1!="string"){
return;
}
if((_7b1>="a"&&_7b1<="z")||(_7b1>="A"&&_7b1<="Z")||(_7b1>="0"&&_7b1<="9")||(_7b1===" ")){
if(e.ctrlKey||e.metaKey||e.altKey){
return;
}
}
}
var faux={faux:true},attr;
for(attr in e){
if(!/^(layer[XY]|returnValue|keyLocation)$/.test(attr)){
var v=e[attr];
if(typeof v!="function"&&typeof v!="undefined"){
faux[attr]=v;
}
}
}
lang.mixin(faux,{charOrCode:_7b1,_wasConsumed:false,preventDefault:function(){
faux._wasConsumed=true;
e.preventDefault();
},stopPropagation:function(){
e.stopPropagation();
}});
this._lastInputProducingEvent=faux;
if(this.onInput(faux)===false){
faux.preventDefault();
faux.stopPropagation();
}
if(faux._wasConsumed){
return;
}
if(has("ie")<=9){
switch(e.keyCode){
case keys.TAB:
case keys.ESCAPE:
case keys.DOWN_ARROW:
case keys.UP_ARROW:
case keys.LEFT_ARROW:
case keys.RIGHT_ARROW:
break;
default:
if(e.keyCode==keys.ENTER&&this.textbox.tagName.toLowerCase()!="textarea"){
break;
}
this.defer(function(){
if(this.textbox.value!==this._lastInputEventValue){
on.emit(this.textbox,"input",{bubbles:true});
}
});
}
}
};
this.own(on(this.textbox,"keydown, keypress, paste, cut, compositionend",lang.hitch(this,_7b0)),on(this.textbox,"input",lang.hitch(this,"_onInput")),on(this.domNode,"keypress",function(e){
e.stopPropagation();
}));
},_blankValue:"",filter:function(val){
if(val===null){
return this._blankValue;
}
if(typeof val!="string"){
return val;
}
if(this.trim){
val=lang.trim(val);
}
if(this.uppercase){
val=val.toUpperCase();
}
if(this.lowercase){
val=val.toLowerCase();
}
if(this.propercase){
val=val.replace(/[^\s]+/g,function(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
});
}
return val;
},_setBlurValue:function(){
this._setValueAttr(this.get("value"),true);
},_onBlur:function(e){
if(this.disabled){
return;
}
this._setBlurValue();
this.inherited(arguments);
},_isTextSelected:function(){
return this.textbox.selectionStart!=this.textbox.selectionEnd;
},_onFocus:function(by){
if(this.disabled||this.readOnly){
return;
}
if(this.selectOnClick&&by=="mouse"){
this._selectOnClickHandle=on.once(this.domNode,"mouseup, touchend",lang.hitch(this,function(evt){
if(!this._isTextSelected()){
_7a8.selectInputText(this.textbox);
}
}));
this.own(this._selectOnClickHandle);
this.defer(function(){
if(this._selectOnClickHandle){
this._selectOnClickHandle.remove();
this._selectOnClickHandle=null;
}
},500);
}
this.inherited(arguments);
this._refreshState();
},reset:function(){
this.textbox.value="";
this.inherited(arguments);
}});
if(has("dojo-bidi")){
_7a8=_7a6("dijit.form._TextBoxMixin",_7a8,{_setValueAttr:function(){
this.inherited(arguments);
this.applyTextDir(this.focusNode);
},_setDisplayedValueAttr:function(){
this.inherited(arguments);
this.applyTextDir(this.focusNode);
},_onInput:function(){
this.applyTextDir(this.focusNode);
this.inherited(arguments);
}});
}
_7a8._setSelectionRange=_7a7._setSelectionRange=function(_7b3,_7b4,stop){
if(_7b3.setSelectionRange){
_7b3.setSelectionRange(_7b4,stop);
}
};
_7a8.selectInputText=_7a7.selectInputText=function(_7b5,_7b6,stop){
_7b5=dom.byId(_7b5);
if(isNaN(_7b6)){
_7b6=0;
}
if(isNaN(stop)){
stop=_7b5.value?_7b5.value.length:0;
}
try{
_7b5.focus();
_7a8._setSelectionRange(_7b5,_7b6,stop);
}
catch(e){
}
};
return _7a8;
});
},"dijit/a11y":function(){
define(["dojo/_base/array","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/sniff","./main"],function(_7b7,dom,_7b8,_7b9,lang,has,_7ba){
var _7bb;
var a11y={_isElementShown:function(elem){
var s=_7b9.get(elem);
return (s.visibility!="hidden")&&(s.visibility!="collapsed")&&(s.display!="none")&&(_7b8.get(elem,"type")!="hidden");
},hasDefaultTabStop:function(elem){
switch(elem.nodeName.toLowerCase()){
case "a":
return _7b8.has(elem,"href");
case "area":
case "button":
case "input":
case "object":
case "select":
case "textarea":
return true;
case "iframe":
var body;
try{
var _7bc=elem.contentDocument;
if("designMode" in _7bc&&_7bc.designMode=="on"){
return true;
}
body=_7bc.body;
}
catch(e1){
try{
body=elem.contentWindow.document.body;
}
catch(e2){
return false;
}
}
return body&&(body.contentEditable=="true"||(body.firstChild&&body.firstChild.contentEditable=="true"));
default:
return elem.contentEditable=="true";
}
},effectiveTabIndex:function(elem){
if(_7b8.get(elem,"disabled")){
return _7bb;
}else{
if(_7b8.has(elem,"tabIndex")){
return +_7b8.get(elem,"tabIndex");
}else{
return a11y.hasDefaultTabStop(elem)?0:_7bb;
}
}
},isTabNavigable:function(elem){
return a11y.effectiveTabIndex(elem)>=0;
},isFocusable:function(elem){
return a11y.effectiveTabIndex(elem)>=-1;
},_getTabNavigable:function(root){
var _7bd,last,_7be,_7bf,_7c0,_7c1,_7c2={};
function _7c3(node){
return node&&node.tagName.toLowerCase()=="input"&&node.type&&node.type.toLowerCase()=="radio"&&node.name&&node.name.toLowerCase();
};
var _7c4=a11y._isElementShown,_7c5=a11y.effectiveTabIndex;
var _7c6=function(_7c7){
for(var _7c8=_7c7.firstChild;_7c8;_7c8=_7c8.nextSibling){
if(_7c8.nodeType!=1||(has("ie")<=9&&_7c8.scopeName!=="HTML")||!_7c4(_7c8)){
continue;
}
var _7c9=_7c5(_7c8);
if(_7c9>=0){
if(_7c9==0){
if(!_7bd){
_7bd=_7c8;
}
last=_7c8;
}else{
if(_7c9>0){
if(!_7be||_7c9<_7bf){
_7bf=_7c9;
_7be=_7c8;
}
if(!_7c0||_7c9>=_7c1){
_7c1=_7c9;
_7c0=_7c8;
}
}
}
var rn=_7c3(_7c8);
if(_7b8.get(_7c8,"checked")&&rn){
_7c2[rn]=_7c8;
}
}
if(_7c8.nodeName.toUpperCase()!="SELECT"){
_7c6(_7c8);
}
}
};
if(_7c4(root)){
_7c6(root);
}
function rs(node){
return _7c2[_7c3(node)]||node;
};
return {first:rs(_7bd),last:rs(last),lowest:rs(_7be),highest:rs(_7c0)};
},getFirstInTabbingOrder:function(root,doc){
var _7ca=a11y._getTabNavigable(dom.byId(root,doc));
return _7ca.lowest?_7ca.lowest:_7ca.first;
},getLastInTabbingOrder:function(root,doc){
var _7cb=a11y._getTabNavigable(dom.byId(root,doc));
return _7cb.last?_7cb.last:_7cb.highest;
}};
1&&lang.mixin(_7ba,a11y);
return a11y;
});
},"dijit/form/TextBox":function(){
define(["dojo/_base/declare","dojo/dom-construct","dojo/dom-style","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/sniff","./_FormValueWidget","./_TextBoxMixin","dojo/text!./templates/TextBox.html","../main"],function(_7cc,_7cd,_7ce,_7cf,lang,on,has,_7d0,_7d1,_7d2,_7d3){
var _7d4=_7cc("dijit.form.TextBox"+(has("dojo-bidi")?"_NoBidi":""),[_7d0,_7d1],{templateString:_7d2,_singleNodeTemplate:"<input class=\"dijit dijitReset dijitLeft dijitInputField\" data-dojo-attach-point=\"textbox,focusNode\" autocomplete=\"off\" type=\"${type}\" ${!nameAttrSetting} />",_buttonInputDisabled:has("ie")?"disabled":"",baseClass:"dijitTextBox",postMixInProperties:function(){
var type=this.type.toLowerCase();
if(this.templateString&&this.templateString.toLowerCase()=="input"||((type=="hidden"||type=="file")&&this.templateString==this.constructor.prototype.templateString)){
this.templateString=this._singleNodeTemplate;
}
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
if(has("ie")<9){
this.defer(function(){
try{
var s=_7ce.getComputedStyle(this.domNode);
if(s){
var ff=s.fontFamily;
if(ff){
var _7d5=this.domNode.getElementsByTagName("INPUT");
if(_7d5){
for(var i=0;i<_7d5.length;i++){
_7d5[i].style.fontFamily=ff;
}
}
}
}
}
catch(e){
}
});
}
},_setPlaceHolderAttr:function(v){
this._set("placeHolder",v);
if(!this._phspan){
this._attachPoints.push("_phspan");
this._phspan=_7cd.create("span",{className:"dijitPlaceHolder dijitInputField"},this.textbox,"after");
this.own(on(this._phspan,"mousedown",function(evt){
evt.preventDefault();
}),on(this._phspan,"touchend, pointerup, MSPointerUp",lang.hitch(this,function(){
this.focus();
})));
}
this._phspan.innerHTML="";
this._phspan.appendChild(this._phspan.ownerDocument.createTextNode(v));
this._updatePlaceHolder();
},_onInput:function(evt){
this.inherited(arguments);
this._updatePlaceHolder();
},_updatePlaceHolder:function(){
if(this._phspan){
this._phspan.style.display=(this.placeHolder&&!this.textbox.value)?"":"none";
}
},_setValueAttr:function(_7d6,_7d7,_7d8){
this.inherited(arguments);
this._updatePlaceHolder();
},getDisplayedValue:function(){
_7cf.deprecated(this.declaredClass+"::getDisplayedValue() is deprecated. Use get('displayedValue') instead.","","2.0");
return this.get("displayedValue");
},setDisplayedValue:function(_7d9){
_7cf.deprecated(this.declaredClass+"::setDisplayedValue() is deprecated. Use set('displayedValue', ...) instead.","","2.0");
this.set("displayedValue",_7d9);
},_onBlur:function(e){
if(this.disabled){
return;
}
this.inherited(arguments);
this._updatePlaceHolder();
if(has("mozilla")){
if(this.selectOnClick){
this.textbox.selectionStart=this.textbox.selectionEnd=undefined;
}
}
},_onFocus:function(by){
if(this.disabled||this.readOnly){
return;
}
this.inherited(arguments);
this._updatePlaceHolder();
}});
if(has("ie")<9){
_7d4.prototype._isTextSelected=function(){
var _7da=this.ownerDocument.selection.createRange();
var _7db=_7da.parentElement();
return _7db==this.textbox&&_7da.text.length>0;
};
_7d3._setSelectionRange=_7d1._setSelectionRange=function(_7dc,_7dd,stop){
if(_7dc.createTextRange){
var r=_7dc.createTextRange();
r.collapse(true);
r.moveStart("character",-99999);
r.moveStart("character",_7dd);
r.moveEnd("character",stop-_7dd);
r.select();
}
};
}
if(has("dojo-bidi")){
_7d4=_7cc("dijit.form.TextBox",_7d4,{_setPlaceHolderAttr:function(v){
this.inherited(arguments);
this.applyTextDir(this._phspan);
}});
}
return _7d4;
});
},"dijit/layout/StackContainer":function(){
define(["dojo/_base/array","dojo/cookie","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/has","dojo/_base/lang","dojo/on","dojo/ready","dojo/topic","dojo/when","../registry","../_WidgetBase","./_LayoutWidget"],function(_7de,_7df,_7e0,_7e1,_7e2,has,lang,on,_7e3,_7e4,when,_7e5,_7e6,_7e7){
if(1){
_7e3(0,function(){
var _7e8=["dijit/layout/StackController"];
require(_7e8);
});
}
var _7e9=_7e0("dijit.layout.StackContainer",_7e7,{doLayout:true,persist:false,baseClass:"dijitStackContainer",buildRendering:function(){
this.inherited(arguments);
_7e1.add(this.domNode,"dijitLayoutContainer");
},postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"keydown",lang.hitch(this,"_onKeyDown")));
},startup:function(){
if(this._started){
return;
}
var _7ea=this.getChildren();
_7de.forEach(_7ea,this._setupChild,this);
if(this.persist){
this.selectedChildWidget=_7e5.byId(_7df(this.id+"_selectedChild"));
}else{
_7de.some(_7ea,function(_7eb){
if(_7eb.selected){
this.selectedChildWidget=_7eb;
}
return _7eb.selected;
},this);
}
var _7ec=this.selectedChildWidget;
if(!_7ec&&_7ea[0]){
_7ec=this.selectedChildWidget=_7ea[0];
_7ec.selected=true;
}
_7e4.publish(this.id+"-startup",{children:_7ea,selected:_7ec,textDir:this.textDir});
this.inherited(arguments);
},resize:function(){
if(!this._hasBeenShown){
this._hasBeenShown=true;
var _7ed=this.selectedChildWidget;
if(_7ed){
this._showChild(_7ed);
}
}
this.inherited(arguments);
},_setupChild:function(_7ee){
var _7ef=_7ee.domNode,_7f0=_7e2.place("<div role='tabpanel' class='"+this.baseClass+"ChildWrapper dijitHidden'>",_7ee.domNode,"replace"),_7f1=_7ee["aria-label"]||_7ee.title||_7ee.label;
var _7f2=_7f0.parentElement;
if(_7f2&&_7f2.parentElement){
var _7f3=_7f2.parentElement.id+"_tablist";
var _7f4=dojo.byId(_7f3);
if(_7f4&&_7f4.style.height.trim()=="0px"){
_7f0.removeAttribute("role");
}
}
if(_7f1){
_7f0.setAttribute("aria-label",_7f1);
}
_7e2.place(_7ef,_7f0);
_7ee._wrapper=_7f0;
this.inherited(arguments);
if(_7ef.style.display=="none"){
_7ef.style.display="block";
}
_7ee.domNode.removeAttribute("title");
},addChild:function(_7f5,_7f6){
this.inherited(arguments);
if(this._started){
_7e4.publish(this.id+"-addChild",_7f5,_7f6);
this.layout();
if(!this.selectedChildWidget){
this.selectChild(_7f5);
}
}
},removeChild:function(page){
var idx=_7de.indexOf(this.getChildren(),page);
this.inherited(arguments);
_7e2.destroy(page._wrapper);
delete page._wrapper;
if(this._started){
_7e4.publish(this.id+"-removeChild",page);
}
if(this._descendantsBeingDestroyed){
return;
}
if(this.selectedChildWidget===page){
this.selectedChildWidget=undefined;
if(this._started){
var _7f7=this.getChildren();
if(_7f7.length){
this.selectChild(_7f7[Math.max(idx-1,0)]);
}
}
}
if(this._started){
this.layout();
}
},selectChild:function(page,_7f8){
var d;
page=_7e5.byId(page);
if(this.selectedChildWidget!=page){
d=this._transition(page,this.selectedChildWidget,_7f8);
this._set("selectedChildWidget",page);
_7e4.publish(this.id+"-selectChild",page,this._focused);
if(this.persist){
_7df(this.id+"_selectedChild",this.selectedChildWidget.id);
}
}else{
if(this._focused&&true==page.closable){
_7e4.publish(this.id+"-selectChild",page,this._focused);
}
}
return when(d||true);
},_transition:function(_7f9,_7fa){
if(_7fa){
this._hideChild(_7fa);
}
var d=this._showChild(_7f9);
if(_7f9.resize){
if(this.doLayout){
_7f9.resize(this._containerContentBox||this._contentBox);
}else{
_7f9.resize();
}
}
return d;
},_adjacent:function(_7fb){
var _7fc=this.getChildren();
var _7fd=_7de.indexOf(_7fc,this.selectedChildWidget);
_7fd+=_7fb?1:_7fc.length-1;
return _7fc[_7fd%_7fc.length];
},forward:function(){
return this.selectChild(this._adjacent(true),true);
},back:function(){
return this.selectChild(this._adjacent(false),true);
},_onKeyDown:function(e){
_7e4.publish(this.id+"-containerKeyDown",{e:e,page:this});
},layout:function(){
var _7fe=this.selectedChildWidget;
if(_7fe&&_7fe.resize){
if(this.doLayout){
_7fe.resize(this._containerContentBox||this._contentBox);
}else{
_7fe.resize();
}
}
},_showChild:function(page){
var _7ff=this.getChildren();
page.isFirstChild=(page==_7ff[0]);
page.isLastChild=(page==_7ff[_7ff.length-1]);
page._set("selected",true);
if(page._wrapper){
_7e1.replace(page._wrapper,"dijitVisible","dijitHidden");
}
return (page._onShow&&page._onShow())||true;
},_hideChild:function(page){
page._set("selected",false);
if(page._wrapper){
_7e1.replace(page._wrapper,"dijitHidden","dijitVisible");
}
page.onHide&&page.onHide();
},closeChild:function(page){
var _800=page.onClose&&page.onClose(this,page);
if(_800){
this.removeChild(page);
page.destroyRecursive();
}
},destroyDescendants:function(_801){
this._descendantsBeingDestroyed=true;
this.selectedChildWidget=undefined;
_7de.forEach(this.getChildren(),function(_802){
if(!_801){
this.removeChild(_802);
}
_802.destroyRecursive(_801);
},this);
this._descendantsBeingDestroyed=false;
}});
_7e9.ChildWidgetProperties={selected:false,disabled:false,closable:false,iconClass:"dijitNoIcon",showTitle:true};
lang.extend(_7e6,_7e9.ChildWidgetProperties);
return _7e9;
});
},"curam/util/external":function(){
define(["curam/util"],function(util){
curam.define.singleton("curam.util.external",{inExternalApp:function(){
return jsScreenContext.hasContextBits("EXTAPP");
},getUimParentWindow:function(){
if(util.getTopmostWindow()===dojo.global){
return null;
}else{
return dojo.global;
}
}});
return curam.util.external;
});
},"curam/modal/CuramBaseModal":function(){
define(["dojo/text!curam/layout/resources/CuramBaseModal.html","dojo/_base/declare","dojo/dom-geometry","dojo/Deferred","dojo/on","dojo/query","dojo/_base/lang","dojo/_base/fx","dojo/window","dojo/sniff","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/dom-class","dojo/dom-construct","dojo/aspect","dijit/Dialog","curam/inspection/Layer","curam/util/external","curam/dialog","curam/tab","curam/debug","curam/ModalUIMController","curam/widget/ProgressSpinner","curam/util/RuntimeContext"],function(_803,_804,_805,_806,on,_807,lang,fx,_808,has,dom,_809,_80a,_80b,_80c,asp,_80d,_80e,_80f,_810,tab,_811,_812,_813){
var _814=_804("curam.modal.CuramBaseModal",null,{templateString:_803,autofocus:false,refocus:false,iframeHref:"",iframe:undefined,width:undefined,height:undefined,defaultWidth:800,closeModalText:LOCALISED_MODAL_CLOSE_BUTTON,buttonCloseIcon:MODAL_CLOSE_BUTTON_ICON,buttonCloseIconHover:MODAL_CLOSE_BUTTON_ICON_HOVER,modalPromptText:". "+LOCALISED_MODAL_SCREEN_READER_PROMPT+" .",maximumWidth:null,maximumHeight:null,_determinedWidth:null,_determinedHeight:null,_horizontalModalSpace:100,_verticalModalSpace:100,duration:5,parentWindow:undefined,isRegisteredForClosing:false,unsubscribes:undefined,modalconnects:undefined,onIframeLoadHandler:undefined,initialized:false,initDone:false,initUnsubToken:null,uimController:null,_helpIcon:null,_title:null,_isMobileUA:false,_isMobileUADialogPositioned:false,uimToken:undefined,postCreate:function(){
this.initModal(arguments);
},initModal:function(_815){
curam.debug.log("curam.modal.CuramBaseModal.postCreate(): w=%s; h=%s",this.width?this.width:"not given",this.height?this.height:"not given");
this._destroyOldModals();
var _816=curam.util.getTopmostWindow();
if(_816.curam.config){
this._isMobileUA=_816.curam.config.mobileUserAgent;
}
if(_816&&_816.jsScreenContext){
this._verticalModalSpace=100;
if(_816.jsScreenContext.hasContextBits("EXTAPP")){
this._verticalModalSpace=50;
curam.debug.log("curam.modal.CuramBaseModal.postCreate(): Detected external app, setting _verticalModalSpace to "+this._verticalModalSpace);
}
}
if(typeof (this._isMobileUA)!="boolean"){
this._isMobileUA=false;
}
this.draggable=!this._isMobileUA;
var _817=0.9;
this.maximumWidth=(dijit.getViewport().w*_817)-this._horizontalModalSpace;
this.maximumHeight=(dijit.getViewport().h*_817)-this._verticalModalSpace;
if(jsScreenContext.hasContextBits("EXTAPP")){
this.maximumHeight-=this._verticalModalSpace;
}
this.inherited(_815);
this.unsubscribes=[];
this.modalconnects=[];
this._isCDEJModal=(this.iframeHref.indexOf("CDEJ/popups")>-1||this.iframeHref.indexOf("frequency-editor.jsp")>-1);
if(jsScreenContext.hasContextBits("EXTAPP")){
_80a.set(this.domNode,"top","1px");
_80a.set(this.domNode,"display","");
_80a.set(this.domNode,"opacity","0");
_80b.add(this.domNode,"modalDialog");
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
_80c.destroy(ovr);
}
}));
this._registerInitListener();
var _818=dojo.subscribe("/curam/dialog/iframeUnloaded",this,function(_819,_81a){
if(this.id==_819){
curam.debug.log(_811.getProperty("curam.ModalDialog.unload"),_819);
curam.dialog.removeFromDialogHierarchy(_81a);
this.initDone=false;
this._registerInitListener();
}
});
this.unsubscribes.push(_818);
var _81b=dojo.hitch(this,function(_81c,_81d){
curam.debug.log(_811.getProperty("curam.ModalDialog.load.init"),_81c);
curam.util.onLoad.removeSubscriber(this._getEventIdentifier(),_81b);
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_81d);
if(!this.isRegisteredForClosing){
var _81e=curam.util.getTopmostWindow();
this.unsubscribes.push(_81e.dojo.subscribe("/curam/dialog/close",this,function(_81f){
if(this.id==_81f){
curam.debug.log("/curam/dialog/close "+_811.getProperty("curam.ModalDialog.event.for"),_81f);
this.hide();
}
}));
this.isRegisteredForClosing=true;
}
this.doShow(_81d);
this._notifyModalDisplayed();
});
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),_81b);
if(this._isCDEJModal){
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),function(_820,_821){
var _822=dom.byId(_820);
if(_822){
_822.focus();
_822.contentWindow.focusFirst&&_822.contentWindow.focusFirst();
}
});
}
var _823=true;
this.onLoadSubsequentHandler=dojo.hitch(this,function(_824,_825){
if(_823){
_823=false;
}else{
curam.debug.log(_811.getProperty("curam.ModalDialog.load"),_824);
if(!_825.modalClosing){
curam.dialog.pushOntoDialogHierarchy(this.iframe.contentWindow);
this._determineSize(_825);
this._position(true);
this.doShow(_825);
this._notifyModalDisplayed();
}else{
curam.debug.log(_811.getProperty("curam.ModalDialog.close"));
}
}
var _826=dom.byId(_824);
var _827=_826.contentWindow.document.title;
_826.setAttribute("title",LOCALISED_MODAL_FRAME_TITLE+" - "+_827);
});
curam.util.onLoad.addSubscriber(this._getEventIdentifier(),this.onLoadSubsequentHandler);
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/iframeFailedToLoad",this,function(_828){
if(this.id==_828){
curam.util.onLoad.removeSubscriber(this._getEventIdentifier(),_81b);
this._determineSize({height:450,title:"Error!"});
this.doShow();
this._notifyModalDisplayed();
}
}));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,this._setFocusHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,function(_829){
if(_829==this.id){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/AfterDisplay",[_829]);
}
}));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,function(){
curam.util._setModalCurrentlyOpening(false);
}));
var _82a=function(_82b){
return _82b.indexOf(":")>0;
};
var _82c=_82a(this.iframeHref)?this.iframeHref:this._getBaseUrl(curam.util.getTopmostWindow().location.href)+jsL+"/"+this.iframeHref;
this.uimController=new _812({uid:this.id,url:_82c,loadFrameOnCreate:false,inDialog:true,iframeId:this._getEventIdentifier(),width:this._calculateWidth(this.width)+"px",height:this.maximumHeight+"px"});
_80e.register("curam/modal/CuramBaseModal",this);
curam.debug.log("DEBUG: ModalDialog.js:postCreate(): uimController: "+this.uimController);
this.iframe=this.uimController.getIFrame();
curam.debug.log("DEBUG: ModalDialog.js:postCreate(): uimController.domNode: "+this.uimController.domNode);
this.modalconnects.push(dojo.connect(this,"onHide",this,this._onHideHandler));
this.set("content",this.uimController.domNode);
_80b.add(this.iframe,this._getEventIdentifier());
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",this,this._modalDisplayedHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/closed",this,this._modalClosedHandler));
this.unsubscribes.push(curam.util.getTopmostWindow().dojo.subscribe("/curam/dialog/spinner",this._displaySpinner(this)));
this._registerOnIframeLoad(dojo.hitch(this,this._loadErrorHandler));
this.uimController.loadPage();
},_displaySpinner:function(_82d){
var a1=asp.before(this,"_loadErrorHandler",function(){
curam.util.getTopmostWindow().dojo.publish("curam/progress/unload");
a1.remove();
});
var a2=asp.after(_813,"dismissSpinner",function(){
a1&&a1.remove();
a2&&a2.remove();
});
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_82d.containerNode,100]);
},hide:function(){
if(!this._alreadyInitialized||!this.open){
return;
}
if(this._fadeInDeferred){
this._fadeInDeferred.cancel();
}
var _82e=dojo.fadeOut({node:this.domNode,duration:this.duration,onEnd:dojo.hitch(this,function(){
this.domNode.style.display="none";
dijit.Dialog._DialogLevelManager.hide(this);
this._fadeOutDeferred.resolve(true);
delete this._fadeOutDeferred;
})});
this._fadeOutDeferred=new _806(dojo.hitch(this,function(){
_82e.stop();
delete this._fadeOutDeferred;
}));
var _82f=this._fadeOutDeferred.promise;
dojo.hitch(this,"onHide")();
_82e.play();
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
return _82f;
},_getBaseUrl:function(_830){
var _831=_830.indexOf("?");
_830=(_831>-1)?_830.substring(0,_831):_830;
var _832=_830.lastIndexOf("/");
return _830.substring(0,_832+1);
},_setupHelpIcon:function(_833){
var _834=typeof _833!="undefined"?_833.helpEnabled:false,_835=_834?_833.helpExtension:"",_836=_834?_833.pageID:"",_837=dojo.query(".modalDialog#"+this.id+" .dijitDialogCloseIcon");
for(var i=0;i<_837.length;i++){
if(_834&&!this._helpIcon){
this._helpIcon=this._createHelpIcon("dijitDialogHelpIcon","dijitDialogHelpIcon-hover",_835,_837[i]);
this._helpIcon.setAttribute("role","button");
this._helpIcon.setAttribute("style","visibility: hidden;");
this._setTabIndex(this._helpIcon,"0");
this.connect(this._helpIcon,"onkeydown",function(_838){
this.handleTabbingBackwards(dojo.fixEvent(_838));
});
this._helpIcon._enabled=false;
}
this._setTabIndex(_837[i],"0");
}
if(_834&&this._helpIcon){
this._helpIcon._pageID=_836;
}
if((_834&&this._helpIcon&&this._helpIcon._enabled)||(!_834||!this._helpIcon||!this._helpIcon._enabled)){
return;
}
_80a.set(this._helpIcon,"display",_834?"":"none");
this._helpIcon._enabled=_834;
},_createHelpIcon:function(_839,_83a,_83b,_83c){
var icon=_80c.create("span",{"class":_839,"waiRole":"presentation","title":LOCALISED_MODAL_HELP_TITLE});
_80c.place(icon,_83c,"before");
this.connect(icon,"onclick",function(){
var _83d=curam.config?curam.config.locale:jsL;
var url;
url="./help.jsp?pageID="+this._helpIcon._pageID;
window.open(url);
});
this.connect(icon,"onkeydown",function(){
if(curam.util.enterKeyPress(event)){
var _83e=curam.config?curam.config.locale:jsL;
var url;
url="./help.jsp?pageID="+this._helpIcon._pageID;
window.open(url);
}
});
if(_83a){
this.connect(icon,"onmouseover",function(){
_80b.add(icon,_83a);
});
this.connect(icon,"onmouseout",function(){
_80b.remove(icon,_83a);
});
}
var _83f=_80c.create("img",{"src":MODAL_HELP_BUTTON_ICON,"alt":LOCALISED_MODAL_HELP_ALT,"class":"button-help-icon-default"});
var _840=_80c.create("img",{"src":MODAL_HELP_BUTTON_ICON_HOVER,"alt":LOCALISED_MODAL_HELP_ALT,"class":"button-help-icon-hover"});
_80c.place(_83f,icon);
_80c.place(_840,icon);
return icon;
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
},_registerOnIframeLoad:function(_841){
if(dojo.isIE&&dojo.isIE<9){
this.onIframeLoadHandler=dojo.hitch(this,function(){
if(typeof this.iframe!="undefined"&&typeof this.iframe.readyState!="undefined"&&this.iframe.readyState=="complete"){
_841();
}
});
this.iframe.attachEvent("onreadystatechange",this.onIframeLoadHandler);
}else{
this.modalconnects.push(dojo.connect(this.iframe,"onload",this,_841));
}
},_startDrag:function(_842){
if(!this.iframe){
return;
}
if(_842&&_842.node&&_842.node===this.domNode){
var _843=_80c.create("div",{"class":"overlay-iframe"});
_843.innerHTML="";
_80c.place(_843,this.iframe,"before");
var size=_805.getContentBox(this.containerNode,_80a.getComputedStyle(this.containerNode));
_80a.set(_843,{width:size.w+"px",height:size.h+"px"});
var _844=_805.getMarginBoxSimple(dijit._underlay.domNode);
var _845={l:_844.w-size.w-10,t:_844.h-size.h-30};
this._moveable.onMove=function(_846,_847,e){
_847.l=Math.max(5,Math.min(_847.l,_845.l));
_847.t=Math.max(5,Math.min(_847.t,_845.t));
dojo.dnd.Moveable.prototype.onMove.apply(this,[_846,_847,e]);
};
}
},_loadErrorHandler:function(){
curam.debug.log(_811.getProperty("curam.ModalDialog.onload.notify")+this.iframe.id);
if(!this.initDone){
dojo.unsubscribe(this.initUnsubToken);
curam.debug.log(_811.getProperty("curam.ModalDialog.firing")+" /curam/dialog/iframeFailedToLoad "+_811.getProperty("curam.ModalDialog.for"),this.id);
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/iframeFailedToLoad",[this.id]);
}else{
curam.debug.log("UIM "+_811.getProperty("curam.ModalDialog.onload.success"));
}
if(this.iframe&&this.iframe.contentWindow&&this.iframe.contentWindow.document&&this.iframe.contentWindow.document.title){
curam.debug.log("curam.modal.CuramBaseModal._loadErrorHandler calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle(this.iframe.contentWindow.document.title);
}
},_setFocusHandler:function(_848){
if(_848==this.id&&this.initDone){
curam.debug.log("curam.modal.CuramBaseModal_setFocusHandler(): "+_811.getProperty("curam.ModalDialog.execute"),_848);
var _849=this.iframe.contentWindow;
var that=this;
var _84a;
if(_849.document.getElementsByClassName("skeleton").length>0){
_84a=setTimeout(function(){
that._setFocusHandler(_848);
},300);
}else{
_849.dojo.publish("curam/modal/component/ready");
if(_84a){
clearTimeout(_84a);
}
if(typeof _849.dijit=="object"&&typeof _849.dijit.focus=="function"){
_849.dijit.focus(this.iframe);
}else{
this.iframe.focus();
}
var _84b;
dojo.withDoc(this.iframe.contentWindow.document,function(){
_84b=dojo.byId("error-messages");
var _84c=window.curam.util.getTopmostWindow();
if(_84b&&_84c.curam.__datePickerIds){
var _84d=_84c.curam.__datePickerIds;
for(var i=0;i<_84d.length;i++){
var _84e=_84d[i];
var _84f=dojo.byId(_84e);
if(_84f){
_84f.nextElementSibling.focus();
_84f.focus();
}
}
_84c.curam.__datePickerIds=undefined;
}
});
var _850=sessionStorage.getItem("curamDefaultActionId");
var _851=null;
if(!_84b&&_850){
sessionStorage.removeItem("curamDefaultActionId");
dojo.withDoc(this.iframe.contentWindow.document,function(){
if(dojo.byId(_850)){
_851=dojo.byId(_850).previousSibling;
}else{
_851=_849.curam.util.doSetFocus();
}
});
}else{
_851=_849.curam.util.doSetFocus();
if(!_851&&this.checkPrimaryButtonOnViewModal&&typeof this.checkPrimaryButtonOnViewModal==="function"){
_851=this.checkPrimaryButtonOnViewModal();
}
}
if(_851){
if(has("trident")||window.navigator.userAgent.indexOf("Edg/")>-1||has("ie")){
var _852=_851.ownerDocument.forms["mainForm"];
var _853=false;
if(sessionStorage.getItem("suppressCuramModalFocusTimeout")&&sessionStorage.getItem("suppressCuramModalFocusTimeout")==="true"){
_853=true;
}
if(_852&&(_851.tagName==="SELECT"||(_851.tagName==="INPUT"&&_809.get(_851,"type")==="text"))){
var _854=_80c.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_80c.place(_854,_852,"before");
_854.focus();
on(_854,"blur",function(){
_80c.destroy(_854);
});
}
var _855=function(ff){
return function(){
var _856=ff.ownerDocument.activeElement;
if(_856.tagName==="INPUT"&&!_856.classList.contains("hidden-focus-input")||_856.tagName==="TEXTAREA"||(_856.tagName==="LABEL"&&_856.className=="fileUploadButton")||(_856.tagName==="A"&&_856.className=="popup-action")||(_856.tagName==="IFRAME"&&_856.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
var _857=_809.get(_851,"aria-label");
var _858="";
var _859=_809.get(_851,"objid");
if(_859&&_859.indexOf("component")==0||_80b.contains(_851,"dijitReset dijitInputInner")){
_858=_851.title;
}else{
_858=LOCALISED_MODAL_FRAME_TITLE||"Modal Dialog";
}
if(_851&&_851.id!=="container-messages-ul"){
_809.set(_851,"aria-label",_858);
}
var _85a=function(_85b){
return function(e){
_807("input|select[aria-label="+_858+"]").forEach(function(_85c){
_85b&&_809.set(_85c,"aria-label",_85b);
!_85b&&_809.remove(_85c,"aria-label");
});
};
};
on(_851,"blur",_85a(_857));
if(!_853&&_851.tagName==="TEXTAREA"){
setTimeout(_855(_851),1000);
}else{
if(!_853&&(_851.tagName==="SELECT"||(_851.tagName==="INPUT"&&_809.get(_851,"type")==="text"))){
setTimeout(_855(_851),200);
}else{
_851.focus();
}
}
}else{
_851.focus();
}
if(sessionStorage.getItem("suppressCuramModalFocusTimeout")){
sessionStorage.removeItem("suppressCuramModalFocusTimeout");
}
}
}
}
},_modalDisplayedHandler:function(_85d){
if(_85d==this.id){
curam.debug.log(_811.getProperty("curam.ModalDialog.dialog.open.1")+"("+this.id+")"+_811.getProperty("curam.ModalDialog.dialog.open.2"));
this._markAsActiveDialog(true);
}else{
if(!this.deactivatedBy){
curam.debug.log(_811.getProperty("curam.ModalDialog.dialog.deactivating.1")+"("+this.id+"),"+_811.getProperty("curam.ModalDialog.dialog.deactivating.2"),_85d);
this._markAsActiveDialog(false);
this.deactivatedBy=_85d;
}
}
},_modalClosedHandler:function(_85e){
if(this.deactivatedBy==_85e){
curam.debug.log(_811.getProperty("curam.ModalDialog.dialog.activating.1")+"("+this.id+"),"+_811.getProperty("curam.ModalDialog.dialog.activating.2"),_85e);
this._markAsActiveDialog(true);
delete this.deactivatedBy;
}
curam.debug.log("curam.modal.CuramBaseModal._modalClosedHandler calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
},_destroyOldModals:function(){
if(!curam.dialog.oldModalsToDestroy){
curam.dialog.oldModalsToDestroy=[];
}
dojo.forEach(curam.dialog.oldModalsToDestroy,function(_85f){
_85f._cleanupIframe();
_85f.destroyRecursive();
});
curam.dialog.oldModalsToDestroy=[];
},_initParentWindowRef:function(){
if(!this.parentWindow){
var _860=null;
if(curam.tab.inTabbedUI()){
_860=curam.tab.getContentPanelIframe();
}else{
if(_80f.inExternalApp()){
_860=_80f.getUimParentWindow();
}
}
if(_860){
this.parentWindow=_860.contentWindow;
}
}else{
if(_80b.contains(this.parentWindow.frameElement,"detailsPanelFrame")){
var _861=curam.tab.getContentPanelIframe();
var _862=curam.util.getLastPathSegmentWithQueryString(_861.src);
_862=curam.util.removeUrlParam(_862,"__o3rpu");
curam.debug.log("o3rpu "+_811.getProperty("curam.ModalDialog.property"),encodeURIComponent(_862));
this.iframeHref=curam.util.replaceUrlParam(this.iframeHref,"__o3rpu",encodeURIComponent(_862));
this.parentWindow=_861.contentWindow;
}
}
},_notifyModalDisplayed:function(){
curam.debug.log(_811.getProperty("curam.ModalDialog.publishing")+" /curam/dialog/displayed "+_811.getProperty("curam.ModalDialog.for"),this.id);
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/displayed",[this.id,{width:this._determinedWidth,height:this._determinedHeight}]);
},_markAsActiveDialog:function(_863){
var _864="curam-active-modal";
if(_863){
_80b.add(this.iframe,_864);
curam.debug.log(_811.getProperty("curam.ModalDialog.add.class"),[this.id,this.iframeHref]);
}else{
_80b.remove(this.iframe,_864);
curam.debug.log(_811.getProperty("curam.ModalDialog.remove.class"),[this.id,this.iframe.src]);
}
},_setHrefAttr:function(href){
curam.debug.log("setHrefAttr");
this.iframeHref=href;
this.inherited(arguments);
},_setTabIndex:function(_865,_866){
_865.setAttribute("tabIndex",_866);
},_position:function(_867){
curam.debug.log(_811.getProperty("curam.ModalDialog.position"));
if(this._isMobileUADialogPositioned==false&&(this.open||_867)){
this.inherited(arguments);
if(this._isMobileUA==true){
this._isMobileUADialogPositioned=true;
}
}else{
curam.debug.log(_811.getProperty("curam.ModalDialog.ignoring")+" curam.ModalDialog_position");
}
},_getUnits:function(){
return "px";
},_calculateWidth:function(_868){
if(_868){
_868=new Number(_868);
if(!this._isCDEJModal&&typeof (G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR)!="undefined"){
_868*=G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR;
}
if(_868>this.maximumWidth){
curam.debug.log(_811.getProperty("curam.ModalDialog.specified.width.over"),this.maximumWidth);
return this.maximumWidth;
}else{
return Math.floor(_868);
}
}else{
var _869=this.defaultWidth;
if(!this._isCDEJModal&&typeof (G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR)!="undefined"){
_869*=G11N_MODAL_DIALOG_ADJUSTMENT_FACTOR;
}
curam.debug.log(_811.getProperty("curam.ModalDialog.default.width"),_869);
if(_869>this.maximumWidth){
curam.debug.log(_811.getProperty("curam.ModalDialog.default.width.over"),this.maximumWidth);
return this.maximumWidth;
}else{
return Math.floor(_869);
}
}
},_calculateHeight:function(_86a,_86b){
if(_86a){
_86a=isNaN(_86a)?new Number(_86a):_86a;
if(_86a>this.maximumHeight){
curam.debug.log("specified height exceeds available space, "+"overriding with max available height of ",this.maximumHeight);
return this.maximumHeight;
}else{
if(_86a<this.modalMinimumHeight){
curam.debug.log(_811.getProperty("curam.ModalDialog.specified.height.over.1"),this.modalMinimumHeight);
return this.modalMinimumHeight;
}else{
return _86a;
}
}
}else{
curam.debug.log(_811.getProperty("curam.ModalDialog.no.height"),_86b);
if(_86b>this.maximumHeight){
curam.debug.log(_811.getProperty("curam.ModalDialog.calculated.height.over.1"),this.maximumHeight);
return this.maximumHeight;
}else{
if(_86b<this.modalMinimumHeight){
curam.debug.log(_811.getProperty("curam.ModalDialog.calculated.height.over.2"),this.modalMinimumHeight);
return this.modalMinimumHeight;
}else{
return _86b;
}
}
}
},_determineSize:function(_86c){
var _86d=_86c.height;
var _86e=_86c.windowOptions;
curam.debug.log(_811.getProperty("curam.ModalDialog.size"));
try{
var w=this._calculateWidth(this.width);
var h=this._calculateHeight(this.height,_86d);
if(_86e){
if(_86e["width"]||_86e["height"]){
curam.debug.log(_811.getProperty("curam.ModalDialog.options"));
w=this._calculateWidth(_86e["width"]);
h=this._calculateHeight(_86e["height"],_86d);
}
}
w=w+this._getUnits();
h=h+this._getUnits();
curam.debug.log("curam.ModalDialog:_determineSize() %s x %s",w,h);
if(jsScreenContext.hasContextBits("EXTAPP")){
this.uimController.setDimensionsForModalDialog(w,h,_86c);
}
this._determinedWidth=w;
this._determinedHeight=h;
this.setTitle(_86c,w);
}
catch(e){
curam.debug.log("curam.ModalDialog:_determineSize() : "+_811.getProperty("curam.ModalDialog.error")+dojo.toJson(e));
}
},setTitle:function(_86f,_870){
var _871=_86f.title;
if(!_871){
curam.debug.log("curam.ModalDialog.setTitle() - "+_811.getProperty("curam.ModalDialog.no.title"));
_871="";
}
var _872=_86f.messageTitleAppend;
curam.debug.log("curam.ModalDialog.setTitle('%s')",_871);
var _873=_871.indexOf(_872);
if(_873!=-1){
var _874=_80c.create("span",{innerHTML:_872,"class":"messagesPresent"});
_871=_871.split(_872).join("<span class=\"messagesPresent\" aria-owns=\"error-messages-container-wrapper\" role=\"alert\"></span>");
}
this.titleNode.innerHTML=_871;
_80a.set(this.titleBar,{width:_870+"px",height:21+"px"});
_80a.set(this.titleNode,"width",Math.ceil(_870*0.85)+"px");
},doShow:function(_875){
curam.debug.log("curam.ModalDialog.doShow(): "+_811.getProperty("curam.ModalDialog.show"));
if(!this.initialized){
this.initialized=true;
}
this._setupHelpIcon(_875);
this.show();
this.dismissModalSpinner();
if(jsScreenContext.hasContextBits("EXTAPP")){
var _876=dojo.query(".modalDialog#"+this.id+" .dijitDialogCloseIcon");
_80a.set(_876[0],"visibility","visible");
_80a.set(dom.byId("end-"+this.id),"visibility","visible");
_80a.set(this.iframe,"visibility","visible");
_80a.set(this.domNode,"visibility","visible");
if(this._helpIcon){
_80a.set(this._helpIcon,"visibility","visible");
}
}
},dismissModalSpinner:function(){
dojo.publish("/curam/progress/unload");
},_onHideHandler:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/BeforeClose",[this.id]);
_80a.set(this.domNode,{visibility:"hidden",display:"block"});
curam.dialog.removeFromDialogHierarchy(this.iframe.contentWindow);
curam.dialog.removeFromDialogHierarchy(this.parentWindow);
var _877=curam.util.getTopmostWindow();
_877.dojo.publish("/curam/dialog/closed",[this.id]);
dojo.unsubscribe(this.initUnsubToken);
dojo.forEach(this.unsubscribes,_877.dojo.unsubscribe);
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
curam.debug.log(_811.getProperty("curam.ModalDialog.deactivating",[this.id]));
_877.dojo.publish("/curam/dialog/close/appExitConfirmation",[this.id]);
this._markAsActiveDialog(false);
if(typeof this.parentWindow!="undefined"&&this.parentWindow!=null){
this.parentWindow.focus();
}
delete this.parentWindow;
_809.set(this.iframe,"src","");
sessionStorage.removeItem("firstPageHitAfterModalOpened");
curam.dialog.oldModalsToDestroy.push(this);
},handleTabbingForwards:function(e,_878){
if(!tabbingBackwards){
if(_878){
setTimeout(function(){
_878.focus();
},1);
}
}
tabbingBackwards=null;
},handleTabbingBackwards:function(e,_879,_87a){
if(e.shiftKey&&e.keyCode==9){
var elem,evt=e?e:window.event;
if(evt.srcElement){
elem=evt.srcElement;
}else{
if(evt.target){
elem=evt.target;
}else{
throw new Error("handleTabbingBackwards(): No target element found.");
}
}
var _87b=_879&&(elem.previousElementSibling.id==_879)&&this.helpIconEnabled;
if(elem.previousSibling.className=="dijitDialogHelpIcon"||_87b){
return false;
}else{
var _87c=_87a?_87a:elem.parentElement.parentElement.id;
var _87d=document.getElementById(_87c);
if(_87d){
tabbingBackwards=true;
_87d.focus();
}
}
}
},_cleanupIframe:function(){
delete this.content;
delete this.uimController;
var ifrm=this.iframe;
ifrm.src="";
delete this.iframe;
_80c.destroy(ifrm);
}});
return _814;
});
},"curam/util/Navigation":function(){
define(["curam/util","curam/tab","curam/define"],function(){
curam.define.singleton("curam.util.Navigation",{goToPage:function(_87e,_87f){
var url=_87e+"Page.do"+curam.util.makeQueryString(_87f);
curam.util.Navigation.goToUrl(url);
},goToUrl:function(_880){
curam.tab.getTabController().processURL(_880);
}});
return curam.util.Navigation;
});
},"curam/util/RuntimeContext":function(){
define(["dojo/_base/declare"],function(_881){
var _882=_881("curam.util.RuntimeContext",null,{_window:null,constructor:function(_883){
this._window=_883;
},getHref:function(){
return this._window.location.href;
},getPathName:function(){
return this._window.location.pathName;
},contextObject:function(){
return this._window;
}});
return _882;
});
},"curam/util/ui/form/renderer/GenericRendererFormEventsAdapter":function(){
define(["dojo/_base/declare","curam/define"],function(_884){
var _885=_884("curam.util.ui.form.renderer.GenericRendererFormEventsAdapter",null,{elementID:"",pathID:"",element:"",constructor:function(id,_886){
this.elementID=id;
this.pathID=_886;
this.element=document.getElementById(id);
},addChangeListener:function(_887){
this.element.addEventListener("change",_887,this.getFormElement());
},getElementID:function(){
return this.elementID;
},getFormElement:function(){
return this.element;
},setFormElementValue:function(_888){
this.element.value=_888;
},getFormElementValue:function(){
return this.getFormElement().value;
},});
return _885;
});
},"dijit/form/_FormWidgetMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","dojo/window","../a11y"],function(_889,_88a,_88b,_88c,lang,_88d,on,has,_88e,a11y){
return _88a("dijit.form._FormWidgetMixin",null,{name:"",alt:"",value:"",type:"text","aria-label":"focusNode",tabIndex:"0",_setTabIndexAttr:"focusNode",disabled:false,intermediateChanges:false,scrollOnFocus:true,_setIdAttr:"focusNode",_setDisabledAttr:function(_88f){
this._set("disabled",_88f);
if(/^(button|input|select|textarea|optgroup|option|fieldset)$/i.test(this.focusNode.tagName)){
_88b.set(this.focusNode,"disabled",_88f);
}else{
this.focusNode.setAttribute("aria-disabled",_88f?"true":"false");
}
if(this.valueNode){
_88b.set(this.valueNode,"disabled",_88f);
}
if(_88f){
this._set("hovering",false);
this._set("active",false);
var _890="tabIndex" in this.attributeMap?this.attributeMap.tabIndex:("_setTabIndexAttr" in this)?this._setTabIndexAttr:"focusNode";
_889.forEach(lang.isArray(_890)?_890:[_890],function(_891){
var node=this[_891];
if(has("webkit")||a11y.hasDefaultTabStop(node)){
node.setAttribute("tabIndex","-1");
}else{
node.removeAttribute("tabIndex");
}
},this);
}else{
if(this.tabIndex!=""){
this.set("tabIndex",this.tabIndex);
}
}
},_onFocus:function(by){
if(by=="mouse"&&this.isFocusable()){
var _892=this.own(on(this.focusNode,"focus",function(){
_893.remove();
_892.remove();
}))[0];
var _894=has("pointer-events")?"pointerup":has("MSPointer")?"MSPointerUp":has("touch-events")?"touchend, mouseup":"mouseup";
var _893=this.own(on(this.ownerDocumentBody,_894,lang.hitch(this,function(evt){
_893.remove();
_892.remove();
if(this.focused){
if(evt.type=="touchend"){
this.defer("focus");
}else{
this.focus();
}
}
})))[0];
}
if(this.scrollOnFocus){
this.defer(function(){
_88e.scrollIntoView(this.domNode);
});
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&this.focusNode&&(_88c.get(this.domNode,"display")!="none");
},focus:function(){
if(!this.disabled&&this.focusNode.focus){
try{
this.focusNode.focus();
}
catch(e){
}
}
},compare:function(val1,val2){
if(typeof val1=="number"&&typeof val2=="number"){
return (isNaN(val1)&&isNaN(val2))?0:val1-val2;
}else{
if(val1>val2){
return 1;
}else{
if(val1<val2){
return -1;
}else{
return 0;
}
}
}
},onChange:function(){
},_onChangeActive:false,_handleOnChange:function(_895,_896){
if(this._lastValueReported==undefined&&(_896===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_895;
}
this._pendingOnChange=this._pendingOnChange||(typeof _895!=typeof this._lastValueReported)||(this.compare(_895,this._lastValueReported)!=0);
if((this.intermediateChanges||_896||_896===undefined)&&this._pendingOnChange){
this._lastValueReported=_895;
this._pendingOnChange=false;
if(this._onChangeActive){
if(this._onChangeHandle){
this._onChangeHandle.remove();
}
this._onChangeHandle=this.defer(function(){
this._onChangeHandle=null;
this.onChange(_895);
});
}
}
},create:function(){
this.inherited(arguments);
this._onChangeActive=true;
},destroy:function(){
if(this._onChangeHandle){
this._onChangeHandle.remove();
this.onChange(this._lastValueReported);
}
this.inherited(arguments);
}});
});
},"curam/util/ContextPanel":function(){
define(["dijit/registry","dojo/dom-attr","curam/inspection/Layer","curam/debug","curam/util/onLoad","curam/util","curam/tab","curam/define"],function(_897,_898,_899,_89a,_89b){
curam.define.singleton("curam.util.ContextPanel",{CONTENT_URL_ATTRIB:"data-content-url",setupLoadEventPublisher:function(_89c,_89d,_89e){
curam.util.ContextPanel._doSetup(_89c,_89d,_89e,function(_89f){
return _897.byId(_89f);
});
},_doSetup:function(_8a0,_8a1,_8a2,_8a3){
var _8a4=curam.util.getTopmostWindow().dojo.subscribe(_8a0,function(){
var tab=_8a3(_8a1);
var _8a5=curam.util.ContextPanel._getIframe(tab);
if(_8a5){
curam.tab.executeOnTabClose(function(){
var src=_898.get(_8a5,"src");
_898.set(_8a5,"src","");
curam.debug.log("curam.util.ContextPanel: Released iframe content for "+src);
},_8a1);
_89a.log(_89a.getProperty("curam.util.ContextPanel.loaded"));
curam.util.getTopmostWindow().dojo.publish("/curam/frame/detailsPanelLoaded",[{loaded:true},_8a1]);
_8a5._finishedLoading=true;
if(_8a5._scheduledRefresh){
curam.util.ContextPanel.refresh(tab);
_8a5._scheduledRefresh=false;
}
}
});
_89b.addSubscriber(_8a2,curam.util.ContextPanel.addTitle);
curam.tab.unsubscribeOnTabClose(_8a4,_8a1);
curam.tab.executeOnTabClose(function(){
_89b.removeSubscriber(_8a2,curam.util.ContextPanel.addTitle);
},_8a1);
},refresh:function(tab){
var _8a6=curam.util.ContextPanel._getIframe(tab);
if(_8a6){
curam.debug.log(_89a.getProperty("curam.util.ContextPanel.refresh.prep"));
if(_8a6._finishedLoading){
curam.debug.log(_89a.getProperty("curam.util.ContextPanel.refresh"));
_8a6._finishedLoading=false;
var doc=_8a6.contentDocument||_8a6.contentWindow.document;
doc.location.reload(false);
}else{
curam.debug.log(_89a.getProperty("curam.util.ContextPanel.refresh.delay"));
_8a6._scheduledRefresh=true;
}
}
},_getIframe:function(tab){
if(tab){
var _8a7=dojo.query("iframe.detailsPanelFrame",tab.domNode);
return _8a7[0];
}
},addTitle:function(_8a8){
var _8a9=dojo.query("."+_8a8)[0];
var _8aa=_8a9.contentWindow.document.title;
_8a9.setAttribute("title",CONTEXT_PANEL_TITLE+" - "+_8aa);
},load:function(tab){
var _8ab=curam.util.ContextPanel._getIframe(tab);
if(_8ab){
var _8ac=_898.get(_8ab,curam.util.ContextPanel.CONTENT_URL_ATTRIB);
if(_8ac&&_8ac!="undefined"){
_8ab[curam.util.ContextPanel.CONTENT_URL_ATTRIB]=undefined;
_898.set(_8ab,"src",_8ac);
}
}
}});
var _8ad=curam.util.getTopmostWindow();
if(typeof _8ad._curamContextPanelTabReadyListenerRegistered!="boolean"){
_8ad.dojo.subscribe("/curam/application/tab/ready",null,function(_8ae){
curam.util.ContextPanel.load(_8ae);
});
_8ad._curamContextPanelTabReadyListenerRegistered=true;
}
_899.register("curam/util/ContextPanel",this);
return curam.util.ContextPanel;
});
},"curam/widget/Menu":function(){
define(["dijit/Menu","dojo/_base/declare","dojo/dom-class","curam/debug","curam/util","dijit/registry","dojo/on","dojo/_base/lang","dojo/has"],function(menu,_8af,_8b0,_8b1,util,_8b2,on,lang,has){
var Menu=_8af("curam.widget.Menu",dijit.Menu,{_CSS_CLASS_ACTIVE_MENU:"curam-active-menu",_EVENT_OPENED:"/curam/menu/opened",_EVENT_CLOSED:"/curam/menu/closed",_amIActive:false,autoFocus:true,mobContext:has("ios"),postCreate:function(){
curam.debug.log(_8b1.getProperty("curam.widget.Menu.created",[this.id]));
this.own(on(this,"Open",dojo.hitch(this,function(){
curam.debug.log(_8b1.getProperty("curam.widget.Menu.opened",[this.id]));
curam.util.getTopmostWindow().dojo.publish(this._EVENT_OPENED,[this.id]);
this._markAsActive(true);
})));
var _8b3=curam.util.getTopmostWindow().dojo.subscribe(this._EVENT_OPENED,this,function(_8b4){
_8b1.log(_8b1.getProperty("curam.widget.Menu.event",[this.id,this._amIActive?"active":"passive",_8b4]));
if(this.id!=_8b4&&this._amIActive){
_8b1.log(_8b1.getProperty("curam.widget.Menu.deactivate"));
this._markAsActive(false);
var _8b5=curam.util.getTopmostWindow().dojo.subscribe(this._EVENT_CLOSED,this,function(_8b6){
if(_8b6==_8b4){
_8b1.log(_8b1.getProperty("curam.widget.Menu.reactivate",[_8b4,this.id]));
dojo.unsubscribe(_8b5);
this._markAsActive(true);
}
});
}
});
this.own(on(this,"Close",dojo.hitch(this,function(){
curam.debug.log(_8b1.getProperty("curam.widget.Menu.closing",[this.id]));
curam.util.getTopmostWindow().dojo.publish(this._EVENT_CLOSED,[this.id]);
this._markAsActive(false);
dojo.unsubscribe(_8b3);
})));
this.inherited(arguments);
},_markAsActive:function(_8b7){
if(_8b7){
curam.debug.log(_8b1.getProperty("curam.widget.Menu.add.class"),this.id);
_8b0.add(this.domNode,this._CSS_CLASS_ACTIVE_MENU);
}else{
curam.debug.log(_8b1.getProperty("curam.widget.Menu.remove.class"),this.id);
_8b0.remove(this.domNode,this._CSS_CLASS_ACTIVE_MENU);
}
this._amIActive=_8b7;
},onItemClick:function(item,evt){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.focusChild(item);
if(item.disabled){
return false;
}
if(item.popup){
this.set("selected",item);
this.set("activated",true);
var _8b8=false;
if(this.mobContext){
_8b8=/click/.test(evt._origType||evt.type);
}else{
_8b8=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
}
this._openItemPopup(item,_8b8);
}else{
this.onExecute();
item._onClick?item._onClick(evt):item.onClick(evt);
}
}});
return Menu;
});
},"dijit/_BidiMixin":function(){
define([],function(){
var _8b9={LRM:"‎",LRE:"‪",PDF:"‬",RLM:"‏",RLE:"‫"};
return {textDir:"",getTextDir:function(text){
return this.textDir=="auto"?this._checkContextual(text):this.textDir;
},_checkContextual:function(text){
var fdc=/[A-Za-z\u05d0-\u065f\u066a-\u06ef\u06fa-\u07ff\ufb1d-\ufdff\ufe70-\ufefc]/.exec(text);
return fdc?(fdc[0]<="z"?"ltr":"rtl"):this.dir?this.dir:this.isLeftToRight()?"ltr":"rtl";
},applyTextDir:function(_8ba,text){
if(this.textDir){
var _8bb=this.textDir;
if(_8bb=="auto"){
if(typeof text==="undefined"){
var _8bc=_8ba.tagName.toLowerCase();
text=(_8bc=="input"||_8bc=="textarea")?_8ba.value:_8ba.innerText||_8ba.textContent||"";
}
_8bb=this._checkContextual(text);
}
if(_8ba.dir!=_8bb){
_8ba.dir=_8bb;
}
}
},enforceTextDirWithUcc:function(_8bd,text){
if(this.textDir){
if(_8bd){
_8bd.originalText=text;
}
var dir=this.textDir=="auto"?this._checkContextual(text):this.textDir;
return (dir=="ltr"?_8b9.LRE:_8b9.RLE)+text+_8b9.PDF;
}
return text;
},restoreOriginalText:function(_8be){
if(_8be.originalText){
_8be.text=_8be.originalText;
delete _8be.originalText;
}
return _8be;
},_setTextDirAttr:function(_8bf){
if(!this._created||this.textDir!=_8bf){
this._set("textDir",_8bf);
var node=null;
if(this.displayNode){
node=this.displayNode;
this.displayNode.align=this.dir=="rtl"?"right":"left";
}else{
node=this.textDirNode||this.focusNode||this.textbox;
}
if(node){
this.applyTextDir(node);
}
}
}};
});
},"curam/util/ListSort":function(){
define(["dojo/dom","dojo/dom-construct","dojo/dom-style","dojo/dom-attr","curam/util","curam/debug","dojo/query","dojo/on","dojo/sniff"],function(dom,_8c0,_8c1,_8c2,util,_8c3,_8c4,on,has){
var _8c5=_LS=dojo.setObject("curam.util.ListSort",{LIVE:"aria-live",READOUT:"aria-label",BUSY:"aria-busy",SCROLLABLE:"_slh",EVT_SORT_PREP:"/curam/list/toBeSorted",EVT_SORTED:"/curam/list/sorted",sortableTable:null,sortTexts:null,tablesWithListeners:[],sorters:[],headingUpdaters:[],currentUpdater:null,updatesByNow:-1,sortedCell:null,cleaner:null,makeSortable:function(_8c6,_8c7,_8c8,_8c9,_8ca,_8cb){
dojo.addOnLoad(function(){
var _8cc=dom.byId(_8c6);
if(!_8cc){
return;
}
var _8cd=_8c4("tHead tr th",_8cc);
if(_8cd==null){
return;
}
_8cc.ariaListeners=[];
_8cc.onces=[];
_LS.sortTexts=_8ca.split("|");
var _8ce=1+(+_8c8),body=_8cc.tBodies[0],rw=body&&body.rows,_8cf=rw?rw.length:false,_8d0=_8cf?_8cf-_8ce:0;
var _8d1=function(cell,_8d2){
return function(){
_8c2.set(cell,_LS.READOUT,_8d2);
};
},_8d3=function(_8d4,_8d5){
return _8d4+"."+_8d5;
},_8d6=function(_8d7,cNum,_8d8){
var _8d9=function(pgId,_8da,sIx,_8db){
if(pgId==_8c2.get(_8d7,"paginationId")){
if(_LS.updatesByNow>0){
var _8dc=_8d8+" "+_8c9;
var _8dd=null;
if(cNum==_8da){
_8dd=_8dc+"."+_8db;
_8dc=_8d3(_8dc,_8db);
_LS.sortedCell=_8d7;
if(has("trident")){
_8c2.set(_8d7,_LS.READOUT," ");
_8c2.set(_8c4("span",_8d7)[0],_LS.READOUT,_8dc);
}else{
_8c2.set(_8d7,_LS.READOUT,_8dc);
}
}else{
_8dc+="."+_LS.sortTexts[0];
_8cc.onces.push(_8d1(_8d7,_8dc));
has("trident")&&_8c2.set(_8c4("span",_8d7)[0],_LS.READOUT,_8dc);
}
_8c2.set(_8d7,_LS.BUSY,"false");
}
if(--_LS.updatesByNow==0){
_LS.doFocus(_8cc.onces,_8dd);
}
}
};
_8d9.cleanUp=function(){
_8d7=null;
};
return _8d9;
},_8de=function(tbl,_8df,_8e0){
return function(pid,_8e1){
if(tbl==_8e1){
_LS.sortedCell=null;
tbl.onces=[];
_LS.resetReadout(_8df,_8e0);
_8c2.set(_8df,_LS.BUSY,"true");
}
};
};
_8cd.forEach(function(_8e2,ix){
if(_8e2.id&&_8e2.childNodes[0]){
var _8e3=(_8cb?dom.byId(_8e2.id+_LS.SCROLLABLE):_8e2).childNodes[0],_8e4=_8e3.childNodes[0];
if(_8e4&&_8e4.nodeType==3){
var _8e5=dojo.trim(_8e4.nodeValue);
if((_8e5.length>0)&&(_8e5!=" ")){
_8e3.innerHTML="";
var _8e6=_8e5+" "+_8c9,_8e7=_8e6+" "+_LS.sortTexts[0];
_8c2.set(_8e3,{"tabindex":"0","paginationid":_8c7,"aria-label":_8e7,className:"listSortable"});
if(has("trident")){
var _8e6=_8e6+_LS.sortTexts[0];
_8c2.set(_8e3,{"onblur":"_LS.updateAriaLabel(this)"});
}
_LS.setPlaceholders(_8e3,_8e5,_8e6);
_LS.sorters.push(on(_8e3,"click, keyup",dojo.partial(_LS.sortTable,_8cc,_8c7,ix,_8ce,_8c8,_8d0)));
_LS.sorters.push(dojo.subscribe(_LS.EVT_SORT_PREP,_8de(_8cc,_8e3,_8e5)));
_8cc.ariaListeners.push(dojo.subscribe(_LS.EVT_SORTED,_8d6(_8e3,ix,_8e5)));
}
}
}
});
(_8cc.ariaListeners.length>0)&&_LS.tablesWithListeners.push(_8cc);
_8c4(".hidden-table-header a").forEach(function(_8e8){
_8c2.set(_8e8,"tabindex","-1");
});
_8cc._sortUp=true;
_LS.sortableTable=_8cc;
_LS.cleaner=on(window,"unload",_LS.cleanUp);
});
},updateAriaLabel:function(_8e9){
_8c2.remove(_8e9,_8c5.READOUT);
},cleanUp:function(){
_LS.tablesWithListeners.forEach(function(tbl){
tbl.ariaListeners.forEach(function(lsn){
dojo.unsubscribe(lsn);
});
tbl.ariaListeners=null;
});
_LS.tablesWithListeners=null;
_LS.sorters.forEach(function(srt){
srt.cleanUp&&srt.cleanUp();
});
_LS.sorters=null;
_LS.sortedCell=null;
_LS.sortableTable=null;
_LS.cleaner.remove();
},sortTable:function(_8ea,pid,col,step,_8eb,_8ec,evt){
if(evt&&evt.keyCode&&!(evt.keyCode==13||evt.keyCode==32)){
return;
}
var _8ed=_8ea.tBodies[0],_8ee=_8ed.rows;
var _8ef=_8ed&&_8ed.rows.length;
if((_8ef<3&&_8eb)||(_8ef<2&&!_8eb)){
return;
}
var _8f0=_8ea._sortUp?function(a,b){
return a-b;
}:function(a,b){
return b-a;
};
var _8f1=function(a,b){
var aa=_LS.getOrd(a.cells[col]);
var bb=_LS.getOrd(b.cells[col]);
return _8f0(aa,bb);
};
dojo.publish("curam/sort/earlyAware",[pid]);
dojo.publish(_LS.EVT_SORT_PREP,[pid,_8ea]);
count=_8ed&&_8ed.rows.length;
if(count&&count<=step){
return;
}
var _8f2=_LS.getOrd(_8ee[0].cells[col]);
if(_8f2>=0){
var _8f3=[];
var i=0;
while(i<count){
var fl=_8ee[i++];
_8f3.push(fl);
_8eb&&(fl._detailsRow=_8ee[i++]);
}
_8f3.sort(_8f1);
_8ea._sortUp=!_8ea._sortUp;
var _8f4=document.createDocumentFragment(),_8f5=document.createDocumentFragment();
_8f3.forEach(function(_8f6,ix){
_8c2.set(_8f6,"data-lix",ix);
_8f6.done=false;
var app=(ix<=_8ec)?_8f4:_8f5;
app.appendChild(_8f6);
_8f6._detailsRow&&app.appendChild(_8f6._detailsRow);
});
_8ed.appendChild(_8f4);
curam.util.stripeTable(_8ea,_8eb,_8ec);
_8ed.appendChild(_8f5);
var _8f7=0;
if(_8f3.length>1){
var _8f8=_LS.getOrd(_8f3[0].cells[col]),_8f9=_LS.getOrd(_8f3[_8f3.length-1].cells[col]);
_8f7=_8f9==_8f8?0:(_8f9>_8f8)?1:2;
}
var _8fa=_LS.sortTexts[_8f7];
_LS.updatesByNow=_8ea.ariaListeners.length;
dojo.publish(_LS.EVT_SORTED,[pid,col,_8f7,_8fa]);
var _8fb=dojo.exists;
if(_8fb("curam.listControls."+pid)||_8fb("curam.listTogglers."+pid)||(_8fb("curam.listMenus."+pid)&&curam.listMenus[pid].length>0)){
dojo.publish("curam/update/readings/sort",[pid,_8f3]);
}
}
return false;
},doFocus:function(_8fc,_8fd){
_8c2.set(_LS.sortedCell,_LS.LIVE,"polite");
var fn;
while(fn=_8fc.pop()){
fn();
}
_LS.sortedCell.aux&&_LS.sortedCell.aux.focus();
var to=setTimeout(function(){
clearTimeout(to);
_LS.sortedCell&&_LS.sortedCell.focus();
},500);
if(_8fd){
var to1=setTimeout(function(){
clearTimeout(to1);
_LS.sortedCell&&_8c2.set(_LS.sortedCell,_LS.READOUT,_8fd);
},2500);
}
},getOrd:function(el){
if(!el){
return -1;
}
if(el.ord){
return el.ord;
}
var sps=_8c4("span[data-curam-sort-order]",el)[0];
if(!sps){
el.ord=-1;
return el.ord;
}
var ord=_8c2.get(sps,"data-curam-sort-order");
var _8fe=parseInt(ord),res=isNaN(_8fe)?-1:_8fe;
el.ord=res;
return res;
}});
has("trident")?dojo.mixin(_8c5,{setPlaceholders:function(_8ff,_900,_901){
_8c0.create("p",{"aria-hidden":"true",innerHTML:_900,className:"sortColText"},_8ff);
var _902=_8c0.create("span",{"tabindex":-1,"aria-label":_901},_8ff);
_8c1.set(_902,{"opacity":"0","margin":"0","padding":"0"});
_8ff.aux=_902;
},resetReadout:function(_903,_904){
_8c2.set(_903,_8c5.READOUT,_904);
}}):dojo.mixin(_8c5,{setPlaceholders:function(_905,_906,_907){
!/Edg/.test(navigator.userAgent)&&_8c2.set(_905,"role","button");
_8c0.create("p",{"aria-hidden":"true",innerHTML:_906,className:"sortColText"},_905);
},resetReadout:function(_908,_909){
_8c2.remove(_908,_8c5.LIVE);
_8c2.remove(_908,_8c5.READOUT);
}});
has("ios")&&dojo.mixin(_8c5,{setPlaceholders:function(_90a,_90b,_90c){
_8c2.set(_90a,"role","link");
var cTxt=_8c0.create("p",{"aria-hidden":"true",innerHTML:"placeholder",className:"sortColText"},_90a);
_8c1.set(cTxt,{"opacity":"0","margin":"0","padding":"0","position":"absolute","bottom":"0"});
var lnk=_8c0.create("a",{"tabindex":0,"aria-label":_90b,"href":"#",innerHTML:_90b},_90a);
}});
return _8c5;
});
},"dojo/fx":function(){
define(["./_base/lang","./Evented","./_base/kernel","./_base/array","./aspect","./_base/fx","./dom","./dom-style","./dom-geometry","./ready","require"],function(lang,_90d,dojo,_90e,_90f,_910,dom,_911,geom,_912,_913){
if(!dojo.isAsync){
_912(0,function(){
var _914=["./fx/Toggler"];
_913(_914);
});
}
var _915=dojo.fx={};
var _916={_fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,args||[]);
}
return this;
}};
var _917=function(_918){
this._index=-1;
this._animations=_918||[];
this._current=this._onAnimateCtx=this._onEndCtx=null;
this.duration=0;
_90e.forEach(this._animations,function(a){
if(a){
if(typeof a.duration!="undefined"){
this.duration+=a.duration;
}
if(a.delay){
this.duration+=a.delay;
}
}
},this);
};
_917.prototype=new _90d();
lang.extend(_917,{_onAnimate:function(){
this._fire("onAnimate",arguments);
},_onEnd:function(){
this._onAnimateCtx.remove();
this._onEndCtx.remove();
this._onAnimateCtx=this._onEndCtx=null;
if(this._index+1==this._animations.length){
this._fire("onEnd");
}else{
this._current=this._animations[++this._index];
this._onAnimateCtx=_90f.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
this._onEndCtx=_90f.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play(0,true);
}
},play:function(_919,_91a){
if(!this._current){
this._current=this._animations[this._index=0];
}
if(!_91a&&this._current.status()=="playing"){
return this;
}
var _91b=_90f.after(this._current,"beforeBegin",lang.hitch(this,function(){
this._fire("beforeBegin");
}),true),_91c=_90f.after(this._current,"onBegin",lang.hitch(this,function(arg){
this._fire("onBegin",arguments);
}),true),_91d=_90f.after(this._current,"onPlay",lang.hitch(this,function(arg){
this._fire("onPlay",arguments);
_91b.remove();
_91c.remove();
_91d.remove();
}));
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
this._onAnimateCtx=_90f.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
if(this._onEndCtx){
this._onEndCtx.remove();
}
this._onEndCtx=_90f.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play.apply(this._current,arguments);
return this;
},pause:function(){
if(this._current){
var e=_90f.after(this._current,"onPause",lang.hitch(this,function(arg){
this._fire("onPause",arguments);
e.remove();
}),true);
this._current.pause();
}
return this;
},gotoPercent:function(_91e,_91f){
this.pause();
var _920=this.duration*_91e;
this._current=null;
_90e.some(this._animations,function(a,_921){
if(_920<=a.duration){
this._current=a;
this._index=_921;
return true;
}
_920-=a.duration;
return false;
},this);
if(this._current){
this._current.gotoPercent(_920/this._current.duration);
}
if(_91f){
this.play();
}
return this;
},stop:function(_922){
if(this._current){
if(_922){
for(;this._index+1<this._animations.length;++this._index){
this._animations[this._index].stop(true);
}
this._current=this._animations[this._index];
}
var e=_90f.after(this._current,"onStop",lang.hitch(this,function(arg){
this._fire("onStop",arguments);
e.remove();
}),true);
this._current.stop();
}
return this;
},status:function(){
return this._current?this._current.status():"stopped";
},destroy:function(){
this.stop();
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
if(this._onEndCtx){
this._onEndCtx.remove();
}
}});
lang.extend(_917,_916);
_915.chain=function(_923){
return new _917(_923);
};
var _924=function(_925){
this._animations=_925||[];
this._connects=[];
this._finished=0;
this.duration=0;
_90e.forEach(_925,function(a){
var _926=a.duration;
if(a.delay){
_926+=a.delay;
}
if(this.duration<_926){
this.duration=_926;
}
this._connects.push(_90f.after(a,"onEnd",lang.hitch(this,"_onEnd"),true));
},this);
this._pseudoAnimation=new _910.Animation({curve:[0,1],duration:this.duration});
var self=this;
_90e.forEach(["beforeBegin","onBegin","onPlay","onAnimate","onPause","onStop","onEnd"],function(evt){
self._connects.push(_90f.after(self._pseudoAnimation,evt,function(){
self._fire(evt,arguments);
},true));
});
};
lang.extend(_924,{_doAction:function(_927,args){
_90e.forEach(this._animations,function(a){
a[_927].apply(a,args);
});
return this;
},_onEnd:function(){
if(++this._finished>this._animations.length){
this._fire("onEnd");
}
},_call:function(_928,args){
var t=this._pseudoAnimation;
t[_928].apply(t,args);
},play:function(_929,_92a){
this._finished=0;
this._doAction("play",arguments);
this._call("play",arguments);
return this;
},pause:function(){
this._doAction("pause",arguments);
this._call("pause",arguments);
return this;
},gotoPercent:function(_92b,_92c){
var ms=this.duration*_92b;
_90e.forEach(this._animations,function(a){
a.gotoPercent(a.duration<ms?1:(ms/a.duration),_92c);
});
this._call("gotoPercent",arguments);
return this;
},stop:function(_92d){
this._doAction("stop",arguments);
this._call("stop",arguments);
return this;
},status:function(){
return this._pseudoAnimation.status();
},destroy:function(){
this.stop();
_90e.forEach(this._connects,function(_92e){
_92e.remove();
});
}});
lang.extend(_924,_916);
_915.combine=function(_92f){
return new _924(_92f);
};
_915.wipeIn=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_910.animateProperty(lang.mixin({properties:{height:{start:function(){
o=s.overflow;
s.overflow="hidden";
if(s.visibility=="hidden"||s.display=="none"){
s.height="1px";
s.display="";
s.visibility="";
return 1;
}else{
var _930=_911.get(node,"height");
return Math.max(_930,1);
}
},end:function(){
return node.scrollHeight;
}}}},args));
var fini=function(){
s.height="auto";
s.overflow=o;
};
_90f.after(anim,"onStop",fini,true);
_90f.after(anim,"onEnd",fini,true);
return anim;
};
_915.wipeOut=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_910.animateProperty(lang.mixin({properties:{height:{end:1}}},args));
_90f.after(anim,"beforeBegin",function(){
o=s.overflow;
s.overflow="hidden";
s.display="";
},true);
var fini=function(){
s.overflow=o;
s.height="auto";
s.display="none";
};
_90f.after(anim,"onStop",fini,true);
_90f.after(anim,"onEnd",fini,true);
return anim;
};
_915.slideTo=function(args){
var node=args.node=dom.byId(args.node),top=null,left=null;
var init=(function(n){
return function(){
var cs=_911.getComputedStyle(n);
var pos=cs.position;
top=(pos=="absolute"?n.offsetTop:parseInt(cs.top)||0);
left=(pos=="absolute"?n.offsetLeft:parseInt(cs.left)||0);
if(pos!="absolute"&&pos!="relative"){
var ret=geom.position(n,true);
top=ret.y;
left=ret.x;
n.style.position="absolute";
n.style.top=top+"px";
n.style.left=left+"px";
}
};
})(node);
init();
var anim=_910.animateProperty(lang.mixin({properties:{top:args.top||0,left:args.left||0}},args));
_90f.after(anim,"beforeBegin",init,true);
return anim;
};
return _915;
});
},"curam/inPageNavigation":function(){
define(["dijit/registry","curam/inspection/Layer","curam/tab","curam/ui/PageRequest","curam/debug","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/dom-class","dojo/dom-geometry"],function(_931,_932,tab,_933,_934,_935,_936,_937,_938,_939){
var _93a=_935("curam.inPageNavigation",null,{title:"",href:"",selected:false,constructor:function(args){
this.title=args.title;
this.href=args.href;
this.selected=args.selected;
_934.log("curam.inPageNavigation "+_934.getProperty("curam.inPageNavigation.msg")+this);
_932.register("curam/inPageNavigation",this);
},getLinks:function(){
var _93b=dojo.query(".in-page-navigation-tabs")[0];
var _93c=dojo.query("li",_93b);
var _93d=new Array();
dojo.forEach(_93c,function(link){
var _93e=dojo.query("a",link)[0];
if(!_93e){
return;
}
var _93f=_93e.innerText||_93e.textContent;
var _940=false;
dojo.filter(_936.get(_93e,"class").split(" "),function(_941){
if(_941=="in-page-current-link"){
_940=true;
return;
}
});
var href=_936.get(_93e,"href");
var _942=new curam.inPageNavigation({"title":_93f,"selected":_940,"href":href});
_93d.push(_942);
});
return _93d;
},processMainContentAreaLinks:function(){
dojo.addOnLoad(function(){
var _943=dojo.query(".ipn-page")[0];
if(_943){
var _944=_931.byId(_936.get(_943,"id"));
var _945=_944.getChildren()[0];
_944.removeChild(_945);
if(_944.getChildren().length==0){
return;
}
var _946=dojo.query(".in-page-nav-contentWrapper")[0];
var _947=dojo.query("> *",_946);
var _948=_947[_947.length-1];
var pos=_939.position(_948);
var _949=pos.y;
var _94a="height: "+_949+"px;";
_936.set(_946,"style",_94a);
dojo.connect(_944,"_transition",function(_94b,_94c){
var link=dojo.query(".in-page-link",_94b.id)[0];
var _94d=new curam.ui.PageRequest(link.href);
if(jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
_94d.pageHolder=window;
}
curam.tab.getTabController().handlePageRequest(_94d);
});
_937.set(_943,"visibility","visible");
}
});
}});
return _93a;
});
},"curam/util/LocalConfig":function(){
define([],function(){
var _94e=function(name){
return "curam_util_LocalConfig_"+name;
},_94f=function(name,_950){
var _951=_94e(name);
if(typeof top[_951]==="undefined"){
top[_951]=_950;
}
return top[_951];
},_952=function(name){
return top&&top!=null?top[_94e(name)]:undefined;
};
_94f("seedValues",{}),_94f("overrides",{});
var _953=function(_954,_955){
if(typeof _954!=="undefined"&&typeof _954!=="string"){
throw new Error("Invalid "+_955+" type: "+typeof _954+"; expected string");
}
};
var _956={seedOption:function(name,_957,_958){
_953(_957,"value");
_953(_958,"defaultValue");
_952("seedValues")[name]=(typeof _957!=="undefined")?_957:_958;
},overrideOption:function(name,_959){
_953(_959,"value");
if(typeof (Storage)!=="undefined"){
localStorage[name]=_959;
}else{
_952("overrides")[name]=_959;
}
},readOption:function(name,_95a){
var _95b=_952("seedValues");
var _95c=_952("overrides");
_953(_95a,"defaultValue");
var _95d=null;
if(typeof (Storage)!=="undefined"&&localStorage&&typeof localStorage[name]!=="undefined"){
_95d=localStorage[name];
}else{
if(_95c&&typeof _95c[name]!=="undefined"){
_95d=_95c[name];
}else{
if(_95b&&typeof _95b[name]!=="undefined"){
_95d=_95b[name];
}else{
_95d=_95a;
}
}
}
return _95d;
},clearOption:function(name){
if(typeof (Storage)!=="undefined"){
localStorage.removeItem(name);
}
delete _952("overrides")[name];
delete _952("seedValues")[name];
}};
return _956;
});
},"dojo/data/util/sorter":function(){
define(["../../_base/lang"],function(lang){
var _95e={};
lang.setObject("dojo.data.util.sorter",_95e);
_95e.basicComparator=function(a,b){
var r=-1;
if(a===null){
a=undefined;
}
if(b===null){
b=undefined;
}
if(a==b){
r=0;
}else{
if(a>b||a==null){
r=1;
}
}
return r;
};
_95e.createSortFunction=function(_95f,_960){
var _961=[];
function _962(attr,dir,comp,s){
return function(_963,_964){
var a=s.getValue(_963,attr);
var b=s.getValue(_964,attr);
return dir*comp(a,b);
};
};
var _965;
var map=_960.comparatorMap;
var bc=_95e.basicComparator;
for(var i=0;i<_95f.length;i++){
_965=_95f[i];
var attr=_965.attribute;
if(attr){
var dir=(_965.descending)?-1:1;
var comp=bc;
if(map){
if(typeof attr!=="string"&&("toString" in attr)){
attr=attr.toString();
}
comp=map[attr]||bc;
}
_961.push(_962(attr,dir,comp,_960));
}
}
return function(rowA,rowB){
var i=0;
while(i<_961.length){
var ret=_961[i++](rowA,rowB);
if(ret!==0){
return ret;
}
}
return 0;
};
};
return _95e;
});
},"curam/ui/PageRequest":function(){
define(["dojo/_base/declare","curam/debug"],function(_966,_967){
var _968=_966("curam.ui.PageRequest",null,{forceLoad:false,justRefresh:false,constructor:function(_969,_96a,_96b){
this.parameters={};
this.cdejParameters={};
this.cdejParameters["o3ctx"]="4096";
if(_96a){
this.isHomePage=true;
}else{
this.isHomePage=false;
}
if(_96b){
this.openInCurrentTab=true;
}else{
this.openInCurrentTab=false;
}
this.pageHolder=null;
var url;
if(dojo.isString(_969)){
url=_969;
curam.debug.log("PAGE REQUEST: "+_967.getProperty("curam.ui.PageRequest.url")+" "+url);
}else{
curam.debug.log("PAGE REQUEST: "+_967.getProperty("curam.ui.PageRequest.descriptor")+" "+_969.toJson());
var tc=_969.tabContent;
url=tc.pageID+"Page.do";
var _96c=true;
for(param in tc.parameters){
if(_96c){
url+="?";
_96c=false;
}else{
url+="&";
}
url+=param+"="+encodeURIComponent(tc.parameters[param]);
}
curam.debug.log("PAGE REQUEST: "+_967.getProperty("curam.ui.PageRequest.derived")+" "+url);
}
var _96d=url.split("?");
this.parseUIMPageID(_96d[0]);
if(_96d.length==2){
this.parseParameters(_96d[1]);
}
},parseUIMPageID:function(url){
var _96e=url.split("/");
var _96f=_96e[_96e.length-1];
this.pageID=_96f.replace("Page.do","");
},parseParameterName:function(name){
if(name.charAt(0)=="a"&&name.charAt(1)=="m"&&name.charAt(2)=="p"&&name.charAt(3)==";"){
return name.substring(4,name.length);
}else{
return name;
}
},parseParameters:function(_970){
var _971=_970.split("&");
for(var i=0;i<_971.length;i++){
var _972=_971[i].split("=");
var _973=this.parseParameterName(_972[0]);
if(_973.length>0){
if(!this.isCDEJParam(_973)){
this.parameters[_973]=decodeURIComponent(_972[1].replace(/\+/g," "));
}else{
if(_973!="o3nocache"){
this.cdejParameters[_973]=decodeURIComponent(_972[1].replace(/\+/g," "));
}
}
}
}
},isCDEJParam:function(_974){
return (_974.charAt(0)=="o"&&_974.charAt(1)=="3")||(_974.charAt(0)=="_"&&_974.charAt(1)=="_"&&_974.charAt(2)=="o"&&_974.charAt(3)=="3");
},getQueryString:function(_975){
var _976="";
var _977;
for(_977 in this.parameters){
_976+=_977+"="+encodeURIComponent(this.parameters[_977])+"&";
}
if(!_975==true||_975==false){
for(_977 in this.cdejParameters){
_976+=_977+"="+encodeURIComponent(this.cdejParameters[_977])+"&";
}
}
_976=_976.substring(0,_976.length-1);
this.queryString=_976;
return this.queryString;
},getURL:function(_978){
var _979=this.pageID+"Page.do";
var qs=this.getQueryString(_978);
if(qs!=""){
_979+="?"+qs;
}
this.url=_979;
return this.url;
}});
return _968;
});
},"dojo/window":function(){
define(["./_base/lang","./sniff","./_base/window","./dom","./dom-geometry","./dom-style","./dom-construct"],function(lang,has,_97a,dom,geom,_97b,_97c){
has.add("rtl-adjust-position-for-verticalScrollBar",function(win,doc){
var body=_97a.body(doc),_97d=_97c.create("div",{style:{overflow:"scroll",overflowX:"visible",direction:"rtl",visibility:"hidden",position:"absolute",left:"0",top:"0",width:"64px",height:"64px"}},body,"last"),div=_97c.create("div",{style:{overflow:"hidden",direction:"ltr"}},_97d,"last"),ret=geom.position(div).x!=0;
_97d.removeChild(div);
body.removeChild(_97d);
return ret;
});
has.add("position-fixed-support",function(win,doc){
var body=_97a.body(doc),_97e=_97c.create("span",{style:{visibility:"hidden",position:"fixed",left:"1px",top:"1px"}},body,"last"),_97f=_97c.create("span",{style:{position:"fixed",left:"0",top:"0"}},_97e,"last"),ret=geom.position(_97f).x!=geom.position(_97e).x;
_97e.removeChild(_97f);
body.removeChild(_97e);
return ret;
});
var _980={getBox:function(doc){
doc=doc||_97a.doc;
var _981=(doc.compatMode=="BackCompat")?_97a.body(doc):doc.documentElement,_982=geom.docScroll(doc),w,h;
if(has("touch")){
var _983=_980.get(doc);
w=_983.innerWidth||_981.clientWidth;
h=_983.innerHeight||_981.clientHeight;
}else{
w=_981.clientWidth;
h=_981.clientHeight;
}
return {l:_982.x,t:_982.y,w:w,h:h};
},get:function(doc){
if(has("ie")&&_980!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
},scrollIntoView:function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_97a.doc,body=_97a.body(doc),html=doc.documentElement||body.parentNode,isIE=has("ie")||has("trident"),isWK=has("webkit");
if(node==body||node==html){
return;
}
if(!(has("mozilla")||isIE||isWK||has("opera")||has("trident")||has("edge"))&&("scrollIntoView" in node)){
node.scrollIntoView(false);
return;
}
var _984=doc.compatMode=="BackCompat",_985=Math.min(body.clientWidth||html.clientWidth,html.clientWidth||body.clientWidth),_986=Math.min(body.clientHeight||html.clientHeight,html.clientHeight||body.clientHeight),_987=(isWK||_984)?body:html,_988=pos||geom.position(node),el=node.parentNode,_989=function(el){
return (isIE<=6||(isIE==7&&_984))?false:(has("position-fixed-support")&&(_97b.get(el,"position").toLowerCase()=="fixed"));
},self=this,_98a=function(el,x,y){
if(el.tagName=="BODY"||el.tagName=="HTML"){
self.get(el.ownerDocument).scrollBy(x,y);
}else{
x&&(el.scrollLeft+=x);
y&&(el.scrollTop+=y);
}
};
if(_989(node)){
return;
}
while(el){
if(el==body){
el=_987;
}
var _98b=geom.position(el),_98c=_989(el),rtl=_97b.getComputedStyle(el).direction.toLowerCase()=="rtl";
if(el==_987){
_98b.w=_985;
_98b.h=_986;
if(_987==html&&(isIE||has("trident"))&&rtl){
_98b.x+=_987.offsetWidth-_98b.w;
}
_98b.x=0;
_98b.y=0;
}else{
var pb=geom.getPadBorderExtents(el);
_98b.w-=pb.w;
_98b.h-=pb.h;
_98b.x+=pb.l;
_98b.y+=pb.t;
var _98d=el.clientWidth,_98e=_98b.w-_98d;
if(_98d>0&&_98e>0){
if(rtl&&has("rtl-adjust-position-for-verticalScrollBar")){
_98b.x+=_98e;
}
_98b.w=_98d;
}
_98d=el.clientHeight;
_98e=_98b.h-_98d;
if(_98d>0&&_98e>0){
_98b.h=_98d;
}
}
if(_98c){
if(_98b.y<0){
_98b.h+=_98b.y;
_98b.y=0;
}
if(_98b.x<0){
_98b.w+=_98b.x;
_98b.x=0;
}
if(_98b.y+_98b.h>_986){
_98b.h=_986-_98b.y;
}
if(_98b.x+_98b.w>_985){
_98b.w=_985-_98b.x;
}
}
var l=_988.x-_98b.x,t=_988.y-_98b.y,r=l+_988.w-_98b.w,bot=t+_988.h-_98b.h;
var s,old;
if(r*l>0&&(!!el.scrollLeft||el==_987||el.scrollWidth>el.offsetHeight)){
s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_984)||has("trident")>=5)){
s=-s;
}
old=el.scrollLeft;
_98a(el,s,0);
s=el.scrollLeft-old;
_988.x-=s;
}
if(bot*t>0&&(!!el.scrollTop||el==_987||el.scrollHeight>el.offsetHeight)){
s=Math.ceil(Math[t<0?"max":"min"](t,bot));
old=el.scrollTop;
_98a(el,0,s);
s=el.scrollTop-old;
_988.y-=s;
}
el=(el!=_987)&&!_98c&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
}};
1&&lang.setObject("dojo.window",_980);
return _980;
});
},"dojo/DeferredList":function(){
define(["./_base/kernel","./_base/Deferred","./_base/array"],function(dojo,_98f,_990){
dojo.DeferredList=function(list,_991,_992,_993,_994){
var _995=[];
_98f.call(this);
var self=this;
if(list.length===0&&!_991){
this.resolve([0,[]]);
}
var _996=0;
_990.forEach(list,function(item,i){
item.then(function(_997){
if(_991){
self.resolve([i,_997]);
}else{
_998(true,_997);
}
},function(_999){
if(_992){
self.reject(_999);
}else{
_998(false,_999);
}
if(_993){
return null;
}
throw _999;
});
function _998(_99a,_99b){
_995[i]=[_99a,_99b];
_996++;
if(_996===list.length){
self.resolve(_995);
}
};
});
};
dojo.DeferredList.prototype=new _98f();
dojo.DeferredList.prototype.gatherResults=function(_99c){
var d=new dojo.DeferredList(_99c,false,true,false);
d.addCallback(function(_99d){
var ret=[];
_990.forEach(_99d,function(_99e){
ret.push(_99e[1]);
});
return ret;
});
return d;
};
return dojo.DeferredList;
});
},"curam/widgets":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-construct"],function(dom,_99f,_9a0){
var _9a1=function(_9a2){
this.accordion=new _9a3(_9a2,this);
this.accordion.switchboard=this;
};
var _9a4={updateButtons:function(){
var _9a5=this.accordion;
this.collapser.disabled=_9a5.staysStill(false);
this.expander.disabled=_9a5.staysStill(true);
},switchMode:function(){
if(this.checked){
this.switchboard.accordion.accordMode=false;
this.switchboard.expander.onclick=this.switchboard.accordion.expandRest;
this.switchboard.collapser.style.display="";
this.switchboard.updateButtons();
}else{
this.switchboard.accordion.accordMode=true;
this.switchboard.collapser.style.display="none";
this.switchboard.accordion.collapseAll();
this.switchboard.expander.value="Expand All";
this.switchboard.expander.onclick=this.switchboard.accordion.expandAll;
this.switchboard.expander.disabled=false;
}
}};
dojo.mixin(_9a1.prototype,_9a4);
var _9a3=function(_9a6,_9a7){
var _9a8;
this.panelHeight="250px";
this.accordMode=true;
this.switchboard=_9a7;
this.topElement=dom.byId(_9a6);
this.tabs=[];
var _9a9=dojo.query("div",this.topElement);
for(var i=0;i<_9a9.length;i++){
if(_9a9[i].className=="accordionTab"){
while(_9a9[++i].className!="tabHeader"){
}
_9a8=_9a9[i];
while(_9a9[++i].className!="tabContent"){
}
this.tabs[this.tabs.length]=new _9aa(this,_9a8,_9a9[i]);
}
}
this.lastTab=this.tabs[0];
for(var i=1;i<this.tabs.length;i++){
this.tabs[i].collapse(false);
}
};
var _9ab={expandAll:function(){
var _9ac=this.switchboard.accordion;
for(var i=0;i<_9ac.tabs.length;i++){
_9ac.tabs[i].stateExpanded();
}
this.src="../themes/classic/images/evidence-review/CollapseAllButton.png";
this.onclick=_9ac.collapseAll;
},collapseAll:function(){
var _9ad=this.switchboard.accordion;
for(var i=0;i<_9ad.tabs.length;i++){
_9ad.tabs[i].collapse(false);
}
_9ad.lastTab.expand(false);
this.src="../themes/classic/images/evidence-review/ExpandAllButton.png";
this.onclick=_9ad.expandAll;
},expandRest:function(){
if(!this.switchboard.accordion.staysStill(true)){
this.switchboard.accordion.expandAll();
}
this.switchboard.updateButtons();
},collapseRest:function(){
if(!this.switchboard.accordion.staysStill(false)){
this.switchboard.accordion.collapseAll();
}
this.switchboard.updateButtons();
},staysStill:function(_9ae){
var _9af=0;
var _9b0=this.tabs.length;
for(var i=0;i<_9b0;i++){
if(this.tabs[i].expanded==true){
_9af++;
}
}
return (_9ae==true)?(_9b0-_9af==0):(_9af==1);
}};
dojo.mixin(_9a3.prototype,_9ab);
var _9aa=function(_9b1,_9b2,_9b3){
this.accordion=_9b1;
this.switchboard=_9b1.switchboard;
this.header=_9b2;
this.header.tab=this;
this.content=_9b3;
_99f.set(this.content,{height:_9b1.panelHeight,overflow:"auto"});
this.content.tab=this;
this.expanded=true;
dojo.connect(this.header,"onclick",this.toggleState);
dojo.connect(this.header,"onmouseover",this.hoverStyle);
dojo.connect(this.header,"onmouseout",this.stillStyle);
};
var _9b4={hoverStyle:function(e){
if(!this.tab.expanded){
this.className+=" tabHeaderHover";
}
},stillStyle:function(e){
this.className="tabHeader";
},collapse:function(_9b5){
if(this.accordion.lastTab==this){
return;
}
if(this.accordion.staysStill(false)){
return;
}
if(_9b5&&this.accordion.accordMode==false){
new _9b6(this.content,"down");
}else{
_99f.set(this.content,{height:"1px",display:"none"});
}
this.expanded=false;
this.content.style.overflow="hidden";
if(this.accordion.accordMode==false){
this.switchboard.updateButtons();
}
},expand:function(_9b7){
if(this.accordion.lastTab==this){
return;
}
if(this.accordion.staysStill(true)){
return;
}
var _9b8=this.accordion.lastTab;
this.stateExpanded(_9b7);
this.accordion.lastTab=this;
if(this.accordion.accordMode==true){
_9b8.collapse(true);
}else{
this.switchboard.updateButtons();
}
},stateExpanded:function(_9b9){
if(_9b9){
this.content.style.display="";
if(this.accordion.accordMode==true){
new _9ba(this.content,this.accordion.lastTab.content);
}else{
new _9b6(this.content,"up");
}
}else{
_99f.set(this.content,{height:this.accordion.panelHeight,display:"",overflow:"auto"});
this.expanded=true;
}
},toggleState:function(){
if(this.tab.expanded==true){
this.tab.collapse(true);
}else{
this.tab.expand(true);
}
}};
dojo.mixin(_9aa.prototype,_9b4);
var _9b6=function(_9bb,_9bc){
this.contentRef=_9bb;
this.direction=_9bc;
this.duration=100;
this.steps=6;
this.step();
};
var _9bd={step:function(){
var _9be;
if(this.steps<=0){
if(this.direction=="down"){
_99f.set(this.contentRef,{height:"1px",display:"none"});
this.contentRef.tab.expanded=false;
}else{
this.contentRef.style.height=this.contentRef.tab.accordion.panelHeight;
this.contentRef.tab.expanded=true;
}
this.contentRef.tab.switchboard.updateButtons();
return;
}
if(this.timer){
clearTimeout(this.timer);
}
var _9bf=Math.round(this.duration/this.steps);
if(this.direction=="down"){
_9be=this.steps>0?(parseInt(this.contentRef.offsetHeight)-1)/this.steps:0;
}else{
_9be=this.steps>0?(parseInt(this.contentRef.tab.accordion.panelHeight)-parseInt(this.contentRef.offsetHeight))/this.steps:0;
}
this.resizeBy(_9be);
this.duration-=_9bf;
this.steps--;
this.timer=setTimeout(dojo.hitch(this,this.step),_9bf);
},resizeBy:function(_9c0){
var _9c1=this.contentRef.offsetHeight;
var _9c2=parseInt(_9c0);
if(_9c0!=0){
if(this.direction=="down"){
this.contentRef.style.height=(_9c1-_9c2)+"px";
}else{
this.contentRef.style.height=(_9c1+_9c2)+"px";
}
}
}};
dojo.mixin(_9b6.prototype,_9bd);
var _9ba=function(_9c3,_9c4){
this.collapsingContent=_9c4;
this.collapsingContent.style.overflow="hidden";
this.expandingContent=_9c3;
this.limit=250;
this.duration=100;
this.steps=10;
this.expandingContent.style.display="";
this.step();
};
var _9c5={step:function(){
if(this.steps<=0){
_99f.set(this.collapsingContent,{height:"1px",display:"none"});
_99f.set(this.collapsingContent,{height:this.limit,overflow:"auto"});
this.collapsingContent.tab.expanded=false;
this.expandingContent.tab.expanded=true;
return;
}
if(this.timer){
clearTimeout(this.timer);
}
var _9c6=Math.round(this.duration/this.steps);
var _9c7=this.steps>0?(parseInt(this.collapsingContent.style.height)-1)/this.steps:0;
this.resizeBoth(_9c7);
this.duration-=_9c6;
this.steps--;
this.timer=setTimeout(dojo.hitch(this,this.step),_9c6);
},resizeBoth:function(_9c8){
var h1=parseInt(this.collapsingContent.style.height);
var h2=parseInt(this.expandingContent.style.height);
var _9c9=parseInt(_9c8);
if(_9c8!=0){
if(h2+_9c9<this.limit){
this.collapsingContent.style.height=(h1-_9c9)+"px";
this.expandingContent.style.height=(h2+_9c9)+"px";
}
}
}};
dojo.mixin(_9ba.prototype,_9c5);
var _9ca={version:"1",AccordionControl:_9a1,AccordionWidget:_9a3,AccordionTab:_9aa,SingleSlowMotion:_9b6,SynchroSlowMotion:_9ba,registerAccordion:function(id){
_9a1.constructor(id);
}};
var _9cb=function(_9cc){
this.steps=_9cc;
this.regions=new Array();
this.RGB=new Array(256);
var k=0;
var hex=new Array("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
for(var i=0;i<16;i++){
for(j=0;j<16;j++){
this.RGB[k]=hex[i]+hex[j];
k++;
}
}
};
var _9cd={addRegion:function(_9ce){
this.regions[this.regions.length]=_9ce;
},drawMap:function(){
var _9cf;
if(this.steps%2==0){
_9cf=this.steps/2;
}else{
_9cf=(this.steps-1)/2;
}
var step=parseInt(255/_9cf);
var red,_9d0,blue;
for(var i=0;i<this.steps;++i){
var _9d1;
if(i==0){
_9d1="#ff0000";
}else{
if(i==(this.steps-1)){
_9d1="#0000ff";
}else{
if(i==_9cf){
_9d1="#ffffff";
}else{
if(i>_9cf){
var _9d0=255;
var red=255;
_9d0-=(i-_9cf)*step;
red-=(i-_9cf)*step;
_9d1=this.rgbToHex(red,_9d0,255);
}else{
if(i<_9cf){
_9d0=0;
blue=0;
_9d0+=step*i;
blue+=step*i;
_9d1=this.rgbToHex(255,_9d0,blue);
}
}
}
}
}
var _9d2=dom.byId("heatmapTable");
if(_9d2){
var _9d3=_9d2.getElementsByTagName("td");
for(var j=0;j<_9d3.length;j++){
if(_9d3[j].className.indexOf("region"+this.regions[i])>-1){
_9d3[j].style.background=_9d1;
if(i>_9cf){
_99f.set(dojo.query("a",_9d3[j])[0],"color","white");
}
}
}
}
_99f.set(dom.byId("legendImage"+this.regions[i]),{color:_9d1,background:_9d1});
}
},rgbToHex:function(r,g,b){
var rr=this.RGB[r];
var gg=this.RGB[g];
var bb=this.RGB[b];
return "#"+rr+gg+bb;
}};
dojo.mixin(_9cb.prototype,_9cd);
dojo.global.getDataIn=function(_9d4){
return eval(_9d4);
};
dojo.global.Widgets=_9ca;
dojo.global.HeatMap=_9cb;
return _9ca;
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_9d5,win,_9d6){
var _9d7={},hash={};
var _9d8={length:0,add:function(_9d9){
if(hash[_9d9.id]){
throw new Error("Tried to register widget with id=="+_9d9.id+" but that id is already registered");
}
hash[_9d9.id]=_9d9;
this.length++;
},remove:function(id){
if(hash[id]){
delete hash[id];
this.length--;
}
},byId:function(id){
return typeof id=="string"?hash[id]:id;
},byNode:function(node){
return hash[node.getAttribute("widgetId")];
},toArray:function(){
var ar=[];
for(var id in hash){
ar.push(hash[id]);
}
return ar;
},getUniqueId:function(_9da){
var id;
do{
id=_9da+"_"+(_9da in _9d7?++_9d7[_9da]:_9d7[_9da]=0);
}while(hash[id]);
return _9d6._scopeName=="dijit"?id:_9d6._scopeName+"_"+id;
},findWidgets:function(root,_9db){
var _9dc=[];
function _9dd(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _9de=node.getAttribute("widgetId");
if(_9de){
var _9df=hash[_9de];
if(_9df){
_9dc.push(_9df);
}
}else{
if(node!==_9db){
_9dd(node);
}
}
}
}
};
_9dd(root);
return _9dc;
},_destroyAll:function(){
_9d6._curFocus=null;
_9d6._prevFocus=null;
_9d6._activeStack=[];
_9d5.forEach(_9d8.findWidgets(win.body()),function(_9e0){
if(!_9e0._destroyed){
if(_9e0.destroyRecursive){
_9e0.destroyRecursive();
}else{
if(_9e0.destroy){
_9e0.destroy();
}
}
}
});
},getEnclosingWidget:function(node){
while(node){
var id=node.nodeType==1&&node.getAttribute("widgetId");
if(id){
return hash[id];
}
node=node.parentNode;
}
return null;
},_hash:hash};
_9d6.registry=_9d8;
return _9d8;
});
},"curam/validation":function(){
define(["dojo/dom","curam/define","curam/date"],function(dom){
curam.define.singleton("curam.validation",{FILE_UPLOAD_FLGS:[],fileUploadChecker:null,invalidPathMsg:null,preventKeyPress:function(_9e1){
if(dojo.isIE){
_9e1.cancelBubble=true;
_9e1.returnValue=false;
return false;
}
return true;
},activateFileUploadChecker:function(code){
if(!curam.validation.fileUploadChecker){
curam.validation.fileUploadChecker=function(){
var form=dom.byId("mainForm");
var _9e2=function(evt){
var _9e3=curam.validation.FILE_UPLOAD_FLGS;
for(var i=0;i<_9e3.length;i++){
var _9e4=_9e3[i];
var _9e5=cm.nextSibling(dom.byId(_9e4),"input");
if(!curam.validation.isValidFilePath(_9e5.value)){
dojo.stopEvent(evt);
alert(curam.validation.invalidPathMsg+" '"+_9e5.value+"'");
cm.setFormSubmitted(form,0);
return false;
}
}
return true;
};
dojo.connect(form,"onsubmit",_9e2);
};
dojo.addOnLoad(curam.validation.fileUploadChecker);
}
},isValidFilePath:function(path){
return true;
},validateDate:function(_9e6){
var _9e7={valid:curam.date.isDate(_9e6,jsDF),validFormat:jsDF.toLowerCase()};
return _9e7;
}});
return curam.validation;
});
},"dijit/PopupMenuBarItem":function(){
define(["dojo/_base/declare","./PopupMenuItem","./MenuBarItem"],function(_9e8,_9e9,_9ea){
var _9eb=_9ea._MenuBarItemMixin;
return _9e8("dijit.PopupMenuBarItem",[_9e9,_9eb],{});
});
},"dijit/form/_FormMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/window"],function(_9ec,_9ed,_9ee,lang,on,_9ef){
return _9ed("dijit.form._FormMixin",null,{state:"",_getDescendantFormWidgets:function(_9f0){
var res=[];
_9ec.forEach(_9f0||this.getChildren(),function(_9f1){
if("value" in _9f1){
res.push(_9f1);
}else{
res=res.concat(this._getDescendantFormWidgets(_9f1.getChildren()));
}
},this);
return res;
},reset:function(){
_9ec.forEach(this._getDescendantFormWidgets(),function(_9f2){
if(_9f2.reset){
_9f2.reset();
}
});
},validate:function(){
var _9f3=false;
return _9ec.every(_9ec.map(this._getDescendantFormWidgets(),function(_9f4){
_9f4._hasBeenBlurred=true;
var _9f5=_9f4.disabled||!_9f4.validate||_9f4.validate();
if(!_9f5&&!_9f3){
_9ef.scrollIntoView(_9f4.containerNode||_9f4.domNode);
_9f4.focus();
_9f3=true;
}
return _9f5;
}),function(item){
return item;
});
},setValues:function(val){
_9ee.deprecated(this.declaredClass+"::setValues() is deprecated. Use set('value', val) instead.","","2.0");
return this.set("value",val);
},_setValueAttr:function(obj){
var map={};
_9ec.forEach(this._getDescendantFormWidgets(),function(_9f6){
if(!_9f6.name){
return;
}
var _9f7=map[_9f6.name]||(map[_9f6.name]=[]);
_9f7.push(_9f6);
});
for(var name in map){
if(!map.hasOwnProperty(name)){
continue;
}
var _9f8=map[name],_9f9=lang.getObject(name,false,obj);
if(_9f9===undefined){
continue;
}
_9f9=[].concat(_9f9);
if(typeof _9f8[0].checked=="boolean"){
_9ec.forEach(_9f8,function(w){
w.set("value",_9ec.indexOf(_9f9,w._get("value"))!=-1);
});
}else{
if(_9f8[0].multiple){
_9f8[0].set("value",_9f9);
}else{
_9ec.forEach(_9f8,function(w,i){
w.set("value",_9f9[i]);
});
}
}
}
},getValues:function(){
_9ee.deprecated(this.declaredClass+"::getValues() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},_getValueAttr:function(){
var obj={};
_9ec.forEach(this._getDescendantFormWidgets(),function(_9fa){
var name=_9fa.name;
if(!name||_9fa.disabled){
return;
}
var _9fb=_9fa.get("value");
if(typeof _9fa.checked=="boolean"){
if(/Radio/.test(_9fa.declaredClass)){
if(_9fb!==false){
lang.setObject(name,_9fb,obj);
}else{
_9fb=lang.getObject(name,false,obj);
if(_9fb===undefined){
lang.setObject(name,null,obj);
}
}
}else{
var ary=lang.getObject(name,false,obj);
if(!ary){
ary=[];
lang.setObject(name,ary,obj);
}
if(_9fb!==false){
ary.push(_9fb);
}
}
}else{
var prev=lang.getObject(name,false,obj);
if(typeof prev!="undefined"){
if(lang.isArray(prev)){
prev.push(_9fb);
}else{
lang.setObject(name,[prev,_9fb],obj);
}
}else{
lang.setObject(name,_9fb,obj);
}
}
});
return obj;
},isValid:function(){
return this.state=="";
},onValidStateChange:function(){
},_getState:function(){
var _9fc=_9ec.map(this._descendants,function(w){
return w.get("state")||"";
});
return _9ec.indexOf(_9fc,"Error")>=0?"Error":_9ec.indexOf(_9fc,"Incomplete")>=0?"Incomplete":"";
},disconnectChildren:function(){
},connectChildren:function(_9fd){
this._descendants=this._getDescendantFormWidgets();
_9ec.forEach(this._descendants,function(_9fe){
if(!_9fe._started){
_9fe.startup();
}
});
if(!_9fd){
this._onChildChange();
}
},_onChildChange:function(attr){
if(!attr||attr=="state"||attr=="disabled"){
this._set("state",this._getState());
}
if(!attr||attr=="value"||attr=="disabled"||attr=="checked"){
if(this._onChangeDelayTimer){
this._onChangeDelayTimer.remove();
}
this._onChangeDelayTimer=this.defer(function(){
delete this._onChangeDelayTimer;
this._set("value",this.get("value"));
},10);
}
},startup:function(){
this.inherited(arguments);
this._descendants=this._getDescendantFormWidgets();
this.value=this.get("value");
this.state=this._getState();
var self=this;
this.own(on(this.containerNode,"attrmodified-state, attrmodified-disabled, attrmodified-value, attrmodified-checked",function(evt){
if(evt.target==self.domNode){
return;
}
self._onChildChange(evt.type.replace("attrmodified-",""));
}));
this.watch("state",function(attr,_9ff,_a00){
this.onValidStateChange(_a00=="");
});
},destroy:function(){
this.inherited(arguments);
}});
});
},"dijit/BackgroundIframe":function(){
define(["require","./main","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/sniff"],function(_a01,_a02,_a03,_a04,_a05,lang,on,has){
has.add("config-bgIframe",(has("ie")&&!/IEMobile\/10\.0/.test(navigator.userAgent))||(has("trident")&&/Windows NT 6.[01]/.test(navigator.userAgent)));
var _a06=new function(){
var _a07=[];
this.pop=function(){
var _a08;
if(_a07.length){
_a08=_a07.pop();
_a08.style.display="";
}else{
if(has("ie")<9){
var burl=_a03["dojoBlankHtmlUrl"]||_a01.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_a08=document.createElement(html);
}else{
_a08=_a04.create("iframe");
_a08.src="javascript:\"\"";
_a08.className="dijitBackgroundIframe";
_a08.setAttribute("role","presentation");
_a05.set(_a08,"opacity",0.1);
}
_a08.tabIndex=-1;
}
return _a08;
};
this.push=function(_a09){
_a09.style.display="none";
_a07.push(_a09);
};
}();
_a02.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("config-bgIframe")){
var _a0a=(this.iframe=_a06.pop());
node.appendChild(_a0a);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,"resize",node));
}else{
_a05.set(_a0a,{width:"100%",height:"100%"});
}
}
};
lang.extend(_a02.BackgroundIframe,{resize:function(node){
if(this.iframe){
_a05.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
this.iframe.parentNode.removeChild(this.iframe);
_a06.push(this.iframe);
delete this.iframe;
}
}});
return _a02.BackgroundIframe;
});
},"dijit/layout/TabController":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/has","dojo/i18n","dojo/_base/lang","./StackController","../registry","../Menu","../MenuItem","curam/inspection/Layer","dojo/text!./templates/_TabButton.html","curam/widget/_TabButton","curam/widget/MenuItem","dojo/i18n!../nls/common"],function(_a0b,dom,_a0c,_a0d,has,i18n,lang,_a0e,_a0f,Menu,_a10,lm,_a11,_a12,_a13){
var _a14=_a12;
if(has("dojo-bidi")){
_a14=_a0b("dijit.layout._TabButton",_a14,{_setLabelAttr:function(_a15){
this.inherited(arguments);
this.applyTextDir(this.iconNode,this.iconNode.alt);
}});
}
var _a16=_a0b("dijit.layout.TabController",_a0e,{baseClass:"dijitTabController",templateString:"<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'></div>",tabPosition:"top",buttonWidget:_a14,startup:function(){
this.inherited(arguments);
this.connect(this,"onAddChild",function(page,_a17){
var _a18=this;
page.controlButton._curamPageId=page.id;
page.controlButton.connect(page.controlButton,"_setCuramVisibleAttr",function(){
if(page.controlButton.curamVisible){
var _a19=dojo.map(_a18.getChildren(),function(btn){
return btn._curamPageId;
});
var _a1a=curam.tab.getTabWidgetId(curam.tab.getContainerTab(page.domNode));
var _a1b=curam.util.TabNavigation.getInsertIndex(_a1a,_a19,page.id);
var _a1c=false;
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked&&(document.activeElement===curam.util.getTopmostWindow().curam.util.tabButtonClicked)){
_a1c=true;
}
_a18.addChild(page.controlButton,_a1b);
if(_a1c&&(document.activeElement!==curam.util.getTopmostWindow().curam.util.tabButtonClicked)){
curam.util.getTopmostWindow().curam.util.tabButtonClicked.focus();
}
}else{
var _a1d=page.controlButton;
if(dojo.indexOf(_a18.getChildren(),_a1d)!=-1){
_a18.removeChild(_a1d);
}
}
});
});
},buttonWidgetCloseClass:"dijitTabCloseButton",postCreate:function(){
this.inherited(arguments);
lm.register("dijit/layout/TabController",this);
var _a1e=new Menu({id:this.id+"_Menu",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,targetNodeIds:[this.domNode],selector:function(node){
return _a0d.contains(node,"dijitClosable")&&!_a0d.contains(node,"dijitTabDisabled");
}});
this.own(_a1e);
var _a1f=i18n.getLocalization("dijit","common"),_a20=this;
_a1e.addChild(new _a10({label:_a1f.itemClose,ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,onClick:function(evt){
var _a21=_a0f.byNode(this.getParent().currentTarget);
_a20.onCloseButtonClick(_a21.page);
}}));
var _a22=i18n.getLocalization("curam.application","TabMenu"),_a23=new _a13({onClickValue:"_onClickAll",label:_a22["close.all.tabs.text"],dir:this.dir,lang:this.lang,textDir:this.textDir,onClick:function(evt){
this._onClickAll();
}});
_a1e.addChild(_a23);
},onButtonClick:function(page){
if(!page.controlButton.get("curamDisabled")){
var _a24=dijit.byId(this.containerId);
_a24.selectChild(page);
}
}});
_a16.TabButton=_a14;
return _a16;
});
},"dijit/_MenuBase":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/lang","dojo/mouse","dojo/on","dojo/window","./a11yclick","./registry","./_Widget","./_CssStateMixin","./_KeyNavContainer","./_TemplatedMixin"],function(_a25,_a26,dom,_a27,_a28,lang,_a29,on,_a2a,_a2b,_a2c,_a2d,_a2e,_a2f,_a30){
return _a26("dijit._MenuBase",[_a2d,_a30,_a2f,_a2e],{selected:null,_setSelectedAttr:function(item){
if(this.selected!=item){
if(this.selected){
this.selected._setSelected(false);
this._onChildDeselect(this.selected);
}
if(item){
item._setSelected(true);
}
this._set("selected",item);
}
},activated:false,_setActivatedAttr:function(val){
_a28.toggle(this.domNode,"dijitMenuActive",val);
_a28.toggle(this.domNode,"dijitMenuPassive",!val);
this._set("activated",val);
},parentMenu:null,popupDelay:500,passivePopupDelay:Infinity,autoFocus:false,childSelector:function(node){
var _a31=_a2c.byNode(node);
return node.parentNode==this.containerNode&&_a31&&_a31.focus;
},postCreate:function(){
var self=this,_a32=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.containerNode,on.selector(_a32,_a29.enter),function(){
self.onItemHover(_a2c.byNode(this));
}),on(this.containerNode,on.selector(_a32,_a29.leave),function(){
self.onItemUnhover(_a2c.byNode(this));
}),on(this.containerNode,on.selector(_a32,_a2b),function(evt){
self.onItemClick(_a2c.byNode(this),evt);
evt.stopPropagation();
}),on(this.containerNode,on.selector(_a32,"focusin"),function(){
self._onItemFocus(_a2c.byNode(this));
}));
this.inherited(arguments);
},onKeyboardSearch:function(item,evt,_a33,_a34){
this.inherited(arguments);
if(!!item&&(_a34==-1||(!!item.popup&&_a34==1))){
this.onItemClick(item,evt);
}
},_keyboardSearchCompare:function(item,_a35){
if(!!item.shortcutKey){
return _a35==item.shortcutKey.toLowerCase()?-1:0;
}
return this.inherited(arguments)?1:0;
},onExecute:function(){
},onCancel:function(){
},_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}else{
var _a36=this._getTopMenu();
if(_a36&&_a36._isMenuBar){
_a36.focusNext();
}
}
},_onPopupHover:function(){
this.set("selected",this.currentPopupItem);
this._stopPendingCloseTimer();
},onItemHover:function(item){
if(this.activated){
this.set("selected",item);
if(item.popup&&!item.disabled&&!this.hover_timer){
this.hover_timer=this.defer(function(){
this._openItemPopup(item);
},this.popupDelay);
}
}else{
if(this.passivePopupDelay<Infinity){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.passive_hover_timer=this.defer(function(){
this.onItemClick(item,{type:"click"});
},this.passivePopupDelay);
}
}
this._hoveredChild=item;
item._set("hovering",true);
},_onChildDeselect:function(item){
this._stopPopupTimer();
if(this.currentPopupItem==item){
this._stopPendingCloseTimer();
this._pendingClose_timer=this.defer(function(){
this._pendingClose_timer=null;
this.currentPopupItem=null;
item._closePopup();
},this.popupDelay);
}
},onItemUnhover:function(item){
if(this._hoveredChild==item){
this._hoveredChild=null;
}
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
this.passive_hover_timer=null;
}
item._set("hovering",false);
},_stopPopupTimer:function(){
if(this.hover_timer){
this.hover_timer=this.hover_timer.remove();
}
},_stopPendingCloseTimer:function(){
if(this._pendingClose_timer){
this._pendingClose_timer=this._pendingClose_timer.remove();
}
},_getTopMenu:function(){
for(var top=this;top.parentMenu;top=top.parentMenu){
}
return top;
},onItemClick:function(item,evt){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.focusChild(item);
if(item.disabled){
return false;
}
if(item.popup){
this.set("selected",item);
this.set("activated",true);
var _a37=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
this._openItemPopup(item,_a37);
}else{
this.onExecute();
item._onClick?item._onClick(evt):item.onClick(evt);
}
},_openItemPopup:function(_a38,_a39){
if(_a38==this.currentPopupItem){
return;
}
if(this.currentPopupItem){
this._stopPendingCloseTimer();
this.currentPopupItem._closePopup();
}
this._stopPopupTimer();
var _a3a=_a38.popup;
_a3a.parentMenu=this;
this.own(this._mouseoverHandle=on.once(_a3a.domNode,"mouseover",lang.hitch(this,"_onPopupHover")));
var self=this;
_a38._openPopup({parent:this,orient:this._orient||["after","before"],onCancel:function(){
if(_a39){
self.focusChild(_a38);
}
self._cleanUp();
},onExecute:lang.hitch(this,"_cleanUp",true),onClose:function(){
if(self._mouseoverHandle){
self._mouseoverHandle.remove();
delete self._mouseoverHandle;
}
}},_a39);
this.currentPopupItem=_a38;
},onOpen:function(){
this.isShowingNow=true;
this.set("activated",true);
},onClose:function(){
this.set("activated",false);
this.set("selected",null);
this.isShowingNow=false;
this.parentMenu=null;
},_closeChild:function(){
this._stopPopupTimer();
if(this.currentPopupItem){
if(this.focused){
_a27.set(this.selected.focusNode,"tabIndex",this.tabIndex);
this.selected.focusNode.focus();
}
this.currentPopupItem._closePopup();
this.currentPopupItem=null;
}
},_onItemFocus:function(item){
if(this._hoveredChild&&this._hoveredChild!=item){
this.onItemUnhover(this._hoveredChild);
}
this.set("selected",item);
},_onBlur:function(){
this._cleanUp(true);
this.inherited(arguments);
},_cleanUp:function(_a3b){
this._closeChild();
if(typeof this.isShowingNow=="undefined"){
this.set("activated",false);
}
if(_a3b){
this.set("selected",null);
}
}});
});
},"curam/ModalUIMController":function(){
define(["dojo/text!curam/layout/resources/ModalUIMController.html","dojo/_base/declare","dojo/parser","curam/inspection/Layer","curam/UIMController","curam/debug","curam/util/onLoad"],function(_a3c,_a3d,_a3e,_a3f,_a40){
var _a41=_a3d("curam.ModalUIMController",[_a40],{startModalUIMController:LOCALISED_ACCESSIBILITY_MODAL_START,endModalUIMController:LOCALISED_ACCESSIBILITY_MODAL_END,templateString:_a3c,constructor:function(args){
_a3f.register("curam/ModalUIMController",this);
}});
return _a41;
});
},"dijit/layout/ScrollingTabController":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/fx","dojo/_base/lang","dojo/on","dojo/query","dojo/dom-attr","curam/debug","dojo/sniff","../registry","dojo/text!./templates/ScrollingTabController.html","dojo/text!./templates/_ScrollingTabControllerButton.html","./TabController","./utils","../_WidgetsInTemplateMixin","../Menu","../MenuItem","../form/Button","../_HasDropDown","dojo/NodeList-dom","../a11yclick"],function(_a42,_a43,_a44,_a45,_a46,fx,lang,on,_a47,_a48,_a49,has,_a4a,_a4b,_a4c,_a4d,_a4e,_a4f,Menu,_a50,_a51,_a52){
var _a53=_a43("dijit.layout.ScrollingTabController",[_a4d,_a4f],{baseClass:"dijitTabController dijitScrollingTabController",templateString:_a4b,useMenu:true,useSlider:true,tabStripClass:"",_minScroll:5,_setClassAttr:{node:"containerNode",type:"class"},_tabsWidth:-1,_tablistMenuItemIdSuffix:"_stcMi",buildRendering:function(){
this.inherited(arguments);
var n=this.domNode;
this.scrollNode=this.tablistWrapper;
this._initButtons();
if(!this.tabStripClass){
this.tabStripClass="dijitTabContainer"+this.tabPosition.charAt(0).toUpperCase()+this.tabPosition.substr(1).replace(/-.*/,"")+"None";
_a44.add(n,"tabStrip-disabled");
}
_a44.add(this.tablistWrapper,this.tabStripClass);
},onStartup:function(){
this.inherited(arguments);
this._postStartup=true;
this.own(on(this.containerNode,"attrmodified-label, attrmodified-iconclass",lang.hitch(this,function(evt){
if(this._dim){
this.resize(this._dim);
}
this.bustSizeCache=true;
this._tabsWidth=-1;
evt.detail.widget.domNode._width=0;
})));
},onAddChild:function(page,_a54){
this.inherited(arguments);
var _a55=page.id;
this.bustSizeCache=true;
this._tabsWidth=-1;
var _a56=function(pid,_a57){
var _a58=null;
if(_a57._menuBtn.dropDown){
var _a59=dojo.query(pid+_a57._tablistMenuItemIdSuffix,_a57._menuBtn.dropDown.domNode)[0];
if(_a59){
_a58=dijit.byNode(_a59);
}
}
return _a58;
};
this.pane2button(_a55).connect(this.pane2button(_a55),"_setCuramVisibleAttr",lang.hitch(this,function(){
var _a5a=_a56(_a55,this);
if(_a5a){
this._setCuramVisibility(_a5a,_a55);
}
}));
this.pane2button(_a55).connect(this.pane2button(_a55),"_setCuramDisabledAttr",lang.hitch(this,function(){
var _a5b=_a56(_a55,this);
if(_a5b){
this._setCuramAvailability(_a5b,_a55);
}
}));
_a46.set(this.containerNode,"width",(_a46.get(this.containerNode,"width")+200)+"px");
this.containerNode._width=0;
},_setCuramVisibility:function(_a5c,_a5d){
var _a5e=this.pane2button(_a5d).curamVisible;
if(_a5e){
dojo.replaceClass(_a5c.domNode,"visible","hidden");
}else{
dojo.replaceClass(_a5c.domNode,"hidden","visible");
}
},_setCuramAvailability:function(_a5f,_a60){
var _a61=!this.pane2button(_a60).curamDisabled;
_a5f.disabled=!_a61;
if(_a61){
dojo.replaceClass(_a5f.domNode,"enabled","disabled");
}else{
dojo.replaceClass(_a5f.domNode,"disabled","enabled");
}
},_getNodeWidth:function(node){
if(!node._width){
node._width=_a46.get(node,"width");
}
return node._width;
},destroyRendering:function(_a62){
_a42.forEach(this._attachPoints,function(_a63){
delete this[_a63];
},this);
this._attachPoints=[];
_a42.forEach(this._attachEvents,this.disconnect,this);
this.attachEvents=[];
},destroy:function(){
if(this._menuBtn){
this._menuBtn._curamOwnerController=null;
}
this.inherited(arguments);
},onRemoveChild:function(page,_a64){
var _a65=this.pane2button(page.id);
if(this._selectedTab===_a65.domNode){
this._selectedTab=null;
}
this.inherited(arguments);
this.bustSizeCache=true;
this._tabsWidth=-1;
},_initButtons:function(){
this.subscribe("tab.title.name.finished",this._measureBtns);
this._btnWidth=0;
this._buttons=_a47("> .tabStripButton",this.domNode).filter(function(btn){
if((this.useMenu&&btn==this._menuBtn.domNode)||(this.useSlider&&(btn==this._rightBtn.domNode||btn==this._leftBtn.domNode))){
this._btnWidth+=_a45.getMarginBoxSimple(btn).w;
_a48.set(this._menuBtn,"title",_a49.getProperty("dijit.layout.ScrollingTabController.navMenu.title"));
_a48.set(this._menuBtn,"role","presentation");
_a48.set(this._menuBtn,"tabindex",0);
return true;
}else{
_a46.set(btn,"display","none");
return false;
}
},this);
this._menuBtn._curamOwnerController=this;
},_getTabsWidth:function(){
if(this._tabsWidth>-1){
return this._tabsWidth;
}
var _a66=this.getChildren();
if(_a66.length){
var _a67=_a66[this.isLeftToRight()?_a66.length-1:0].domNode;
var _a68=this._getNodeWidth(_a67);
if(this.isLeftToRight()){
this._tabsWidth=_a67.offsetLeft+_a68;
}else{
var _a69=_a66[_a66.length-1].domNode;
this._tabsWidth=_a67.offsetLeft+_a68-_a69.offsetLeft;
}
return this._tabsWidth;
}else{
return 0;
}
},_enableBtn:function(_a6a){
var _a6b=this._getTabsWidth();
_a6a=_a6a||_a46.get(this.scrollNode,"width");
return _a6b>0&&_a6a<_a6b;
},_measureBtns:function(){
if(this._enableBtn()&&this._rightBtn.domNode.style.display=="none"){
this.resize(this._dim);
if(this.isLeftToRight()){
this._rightBtn.set("disabled",true);
}else{
this._leftBtn.set("disabled",true);
}
}
},resize:function(dim){
if(dojo.query("> *",this.containerNode).length<1){
if(this.domNode.style.height!="1px"){
_a46.set(this.domNode,"height","1px");
}
return;
}
if(!this.bustSizeCache&&this._dim&&dim&&this._dim.w==dim.w){
return;
}
this.bustSizeCache=false;
this.scrollNodeHeight=this.scrollNodeHeight||this.scrollNode.offsetHeight;
this._dim=dim;
this.scrollNode.style.height="auto";
var cb=this._contentBox=_a4e.marginBox2contentBox(this.domNode,{h:0,w:dim.w});
cb.h=this.scrollNodeHeight;
_a45.setContentSize(this.domNode,cb);
var _a6c=this._enableBtn(this._contentBox.w);
this._buttons.style("display",_a6c?"":"none");
this._leftBtn.region="left";
this._rightBtn.region="right";
this._menuBtn.region=this.isLeftToRight()?"right":"left";
_a48.set(this._leftBtn,"title",_a49.getProperty("dijit.layout.ScrollingTabController.navLeft.title"));
_a48.set(this._rightBtn,"title",_a49.getProperty("dijit.layout.ScrollingTabController.navRight.title"));
_a48.set(this._rightBtn,"role","presentation");
_a48.set(this._leftBtn,"role","presentation");
var _a6d;
if(_a6c){
_a6d=dijit.layout.utils.layoutChildren(this.domNode,this._contentBox,[this._menuBtn,this._leftBtn,this._rightBtn,{domNode:this.scrollNode,layoutAlign:"client",fakeWidget:true}]);
}else{
_a6d=dijit.layout.utils.layoutChildren(this.domNode,this._contentBox,[{domNode:this.scrollNode,layoutAlign:"client",fakeWidget:true}]);
}
this.scrollNode._width=_a6d.client.w;
if(this._selectedTab){
if(this._anim&&this._anim.status()=="playing"){
this._anim.stop();
}
this.scrollNode.scrollLeft=this._convertToScrollLeft(this._getScrollForSelectedTab());
}
this._setButtonClass(this._getScroll());
this._postResize=true;
return {h:this._contentBox.h,w:dim.w};
},_getScroll:function(){
return (this.isLeftToRight()||has("ie")<8||(has("trident")&&has("quirks"))||has("webkit"))?this.scrollNode.scrollLeft:_a46.get(this.containerNode,"width")-_a46.get(this.scrollNode,"width")+(has("trident")||has("edge")?-1:1)*this.scrollNode.scrollLeft;
},_convertToScrollLeft:function(val){
if(this.isLeftToRight()||has("ie")<8||(has("trident")&&has("quirks"))||has("webkit")){
return val;
}else{
var _a6e=_a46.get(this.containerNode,"width")-_a46.get(this.scrollNode,"width");
return (has("trident")||has("edge")?-1:1)*(val-_a6e);
}
},onSelectChild:function(page,_a6f){
var tab=this.pane2button(page.id);
if(!tab){
return;
}
var node=tab.domNode;
if(node!=this._selectedTab){
this._selectedTab=node;
if(this._postResize){
var _a70=this._getNodeWidth(this.scrollNode);
if(this._getTabsWidth()<_a70){
tab.onClick(null);
tab.focus();
}else{
var sl=this._getScroll();
if(sl>node.offsetLeft||sl+_a70<node.offsetLeft+this._getNodeWidth(node)){
var anim=this.createSmoothScroll();
if(_a6f){
anim.onEnd=function(){
tab.focus();
};
}
anim.play();
}else{
if(_a6f){
tab.focus();
}
}
}
}
}
this.inherited(arguments);
var _a71=document.activeElement;
if(typeof _a71!=="undefined"&&_a71!=null){
if(_a71.className=="tabLabel"&&(_a47(_a71).closest(".nav-panel")).length>0){
curam.util.setTabButtonClicked(_a71);
}
}
},_getScrollBounds:function(){
var _a72=this.getChildren(),_a73=this._getNodeWidth(this.scrollNode),_a74=this._getNodeWidth(this.containerNode),_a75=_a74-_a73,_a76=this._getTabsWidth();
if(_a72.length&&_a76>_a73){
return {min:this.isLeftToRight()?0:_a72[_a72.length-1].domNode.offsetLeft,max:this.isLeftToRight()?_a76-_a73:_a75};
}else{
var _a77=this.isLeftToRight()?0:_a75;
return {min:_a77,max:_a77};
}
},_getScrollForSelectedTab:function(){
var w=this.scrollNode,n=this._selectedTab,_a78=_a46.get(this.scrollNode,"width"),_a79=this._getScrollBounds();
var pos=(n.offsetLeft+_a46.get(n,"width")/2)-_a78/2;
pos=Math.min(Math.max(pos,_a79.min),_a79.max);
return pos;
},createSmoothScroll:function(x){
if(arguments.length>0){
var _a7a=this._getScrollBounds();
x=Math.min(Math.max(x,_a7a.min),_a7a.max);
}else{
x=this._getScrollForSelectedTab();
}
if(this._anim&&this._anim.status()=="playing"){
this._anim.stop();
}
var self=this,w=this.scrollNode,anim=new fx.Animation({beforeBegin:function(){
if(this.curve){
delete this.curve;
}
var oldS=w.scrollLeft,newS=self._convertToScrollLeft(x);
anim.curve=new fx._Line(oldS,newS);
},onAnimate:function(val){
w.scrollLeft=val;
}});
this._anim=anim;
this._setButtonClass(x);
return anim;
},_getBtnNode:function(e){
var n=e.target;
while(n&&!_a44.contains(n,"tabStripButton")){
n=n.parentNode;
}
return n;
},doSlideRight:function(e){
this.doSlide(1,this._getBtnNode(e));
},doSlideLeft:function(e){
this.doSlide(-1,this._getBtnNode(e));
},doSlide:function(_a7b,node){
if(node&&_a44.contains(node,"dijitTabDisabled")){
return;
}
var _a7c=_a46.get(this.scrollNode,"width");
var d=(_a7c*0.75)*_a7b;
var to=this._getScroll()+d;
this._setButtonClass(to);
this.createSmoothScroll(to).play();
},_setButtonClass:function(_a7d){
var _a7e=this._getScrollBounds();
this._leftBtn.set("disabled",_a7d<=_a7e.min);
this._rightBtn.set("disabled",_a7d>=_a7e.max);
}});
var _a7f=_a43("dijit.layout._ScrollingTabControllerButtonMixin",null,{baseClass:"dijitTab tabStripButton",templateString:_a4c,tabIndex:"",isFocusable:function(){
return false;
}});
_a43("dijit.layout._ScrollingTabControllerButton",[_a51,_a7f]);
_a43("dijit.layout._ScrollingTabControllerMenuButton",[_a51,_a52,_a7f],{containerId:"",tabIndex:"-1",isLoaded:function(){
return false;
},loadDropDown:function(_a80){
this.dropDown=new Menu({id:this.containerId+"_menu",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir});
var _a81=_a4a.byId(this.containerId);
_a42.forEach(_a81.getChildren(),function(page){
var _a82=new _a50({id:page.id+"_stcMi",label:page.title,iconClass:page.iconClass,disabled:page.disabled,ownerDocument:this.ownerDocument,dir:page.dir,lang:page.lang,textDir:page.textDir||_a81.textDir,onClick:function(){
_a81.selectChild(page);
}});
this.dropDown.addChild(_a82);
},this);
dojo.forEach(this.dropDown.getChildren(),lang.hitch(this,function(_a83){
var _a84=_a83.id.split(this._curamOwnerController._tablistMenuItemIdSuffix)[0];
this._curamOwnerController._setCuramAvailability(_a83,_a84);
this._curamOwnerController._setCuramVisibility(_a83,_a84);
dojo.connect(_a83,"destroy",function(){
setDynState=null;
});
}));
_a80();
},closeDropDown:function(_a85){
this.inherited(arguments);
if(this.dropDown){
this._popupStateNode.removeAttribute("aria-owns");
this.dropDown.destroyRecursive();
delete this.dropDown;
}
}});
return _a53;
});
},"curam/core-uim":function(){
define(["cm/_base/_dom","cm/_base/_form","cm/_base/_pageBehaviors","curam/util","curam/date","curam/validation","curam/util/ScreenContext","curam/util/onLoad","curam/ui/UIMPageAdaptor","curam/util/ExpandableLists","curam/util/Refresh","curam/omega3-util","dijit/layout/ContentPane","curam/layout/TabContainer","curam/inPageNavigation"],function(){
});
},"dijit/form/_ListMouseMixin":function(){
define(["dojo/_base/declare","dojo/on","dojo/touch","./_ListBase"],function(_a86,on,_a87,_a88){
return _a86("dijit.form._ListMouseMixin",_a88,{postCreate:function(){
this.inherited(arguments);
this.domNode.dojoClick=true;
this._listConnect("click","_onClick");
this._listConnect("mousedown","_onMouseDown");
this._listConnect("mouseup","_onMouseUp");
this._listConnect("mouseover","_onMouseOver");
this._listConnect("mouseout","_onMouseOut");
},_onClick:function(evt,_a89){
this._setSelectedAttr(_a89,false);
if(this._deferredClick){
this._deferredClick.remove();
}
this._deferredClick=this.defer(function(){
this._deferredClick=null;
this.onClick(_a89);
});
},_onMouseDown:function(evt,_a8a){
if(this._hoveredNode){
this.onUnhover(this._hoveredNode);
this._hoveredNode=null;
}
this._isDragging=true;
this._setSelectedAttr(_a8a,false);
},_onMouseUp:function(evt,_a8b){
this._isDragging=false;
var _a8c=this.selected;
var _a8d=this._hoveredNode;
if(_a8c&&_a8b==_a8c){
this.defer(function(){
this._onClick(evt,_a8c);
});
}else{
if(_a8d){
this.defer(function(){
this._onClick(evt,_a8d);
});
}
}
},_onMouseOut:function(evt,_a8e){
if(this._hoveredNode){
this.onUnhover(this._hoveredNode);
this._hoveredNode=null;
}
if(this._isDragging){
this._cancelDrag=(new Date()).getTime()+1000;
}
},_onMouseOver:function(evt,_a8f){
if(this._cancelDrag){
var time=(new Date()).getTime();
if(time>this._cancelDrag){
this._isDragging=false;
}
this._cancelDrag=null;
}
this._hoveredNode=_a8f;
this.onHover(_a8f);
if(this._isDragging){
this._setSelectedAttr(_a8f,false);
}
}});
});
},"curam/util/Constants":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.util.Constants",{RETURN_PAGE_PARAM:"__o3rpu"});
return curam.util.Constants;
});
},"curam/tab/TabSessionManager":function(){
define(["dojo/_base/declare","curam/debug","curam/tab","curam/util/AutoRecoveryAPI"],function(_a90,_a91,tab,_a92){
var _a93=_a90("curam.tab.TabSessionManager",null,{init:function(_a94){
if(_a94){
this._directBrowseURL=_a94;
}
new curam.ui.ClientDataAccessor().getRaw("/data/tab/get",dojo.hitch(this,this._restoreTabSession),dojo.hitch(this,this._handleGetTabFailure));
},_handleGetTabFailure:function(_a95,_a96){
var _a97=curam.tab.getTabContainer();
var _a98=dojo.toJson(_a95);
this._log(_a91.getProperty("curam.tab.TabSessionManager.error")+_a98);
var tab=new dojox.layout.ContentPane({title:"Error",closable:true,content:"An error occurred. Try refreshing the browser or contact your "+"administrator if it persists. Error: "+_a95.message});
_a97.addChild(tab);
},_restoreTabSession:function(_a99,_a9a){
var _a9b=[];
var _a9c=[];
var _a9d=[];
curam.tab.getTabController().MAX_NUM_TABS=_a99.maxTabs;
curam.tab.getTabController().TABS_SEQUENTIAL_ORDER=_a99.tabsInSequence;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_ENABLED=_a99.progressWidgetEnabled;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_DEFAULT_THRESHOLD=_a99.progressWidgetThreshold;
curam.widget.ProgressSpinner.PROGRESS_WIDGET_TIMEOUT_MAX=_a99.progressWidgetTimeoutMax;
curam.widget.ProgressSpinner.init();
var _a9e=this._isNewSession();
var _a9f=_a9e?null:this._getPrevSelectedTab();
var _aa0=this._getHomePageTab();
_a9f=_a9f?_a9f:_aa0;
this.tabSelected(_a9f);
_a9d[_aa0.sectionID]=true;
if(_a99&&_a99.tabs&&_a99.tabs.length>0){
var tabs=_a99.tabs;
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous")+tabs.length+" "+_a91.getProperty("curam.tab.TabSessionManager.tabs"));
for(var i=0;i<tabs.length;i++){
var _aa1=curam.tab.TabDescriptor.fromJson(tabs[i]);
if(_aa1.tabSignature==_aa0.tabSignature){
if(!_a9e){
if(this._directBrowseURL){
_a9f=_aa1;
}else{
_aa0=_aa1;
}
}
}else{
if(_aa1.sectionID==_a9f.sectionID){
_a9b.push(_aa1);
}else{
_a9c.push(_aa1);
}
}
_a9d[_aa1.sectionID]=true;
}
if(_aa0.sectionID==_a9f.sectionID){
_a9b.unshift(_aa0);
}else{
_a9c.unshift(_aa0);
}
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.no.session"));
_a9b.push(_aa0);
}
this._restoreSectionTabs(_a9b,_a9f);
this._restoreSectionTabs(_a9c,null);
this._selectedTD=_a9f;
if(AUTORECOVERY_ENABLED==="true"){
this._restoreModal(_a9b,_a9c);
}
this._connectSelectionListeners(_a9d);
if(this._directBrowseURL){
var _aa2=this._createDirectBrowseClosure();
var _aa3=curam.util.getTopmostWindow();
var _aa4=_aa3.dojo.subscribe("/curam/main-content/page/loaded",null,function(_aa5,_aa6){
var that=_aa2.getThis();
var _aa7=that._directBrowseURL;
var _aa8=that._selectedTD.tabContent.pageID;
if(_aa5===_aa8){
require(["curam/util/Navigation"],function(nav){
nav.goToUrl(_aa7);
});
that._selectedTD.tabContent.pageID=_aa7.replace(/Page.do\??.*/,"");
that.tabSelected(that._selectedTD);
dojo.unsubscribe(_aa4);
}
});
}
},_createDirectBrowseClosure:function(){
var that=this;
return {getThis:function(){
return that;
}};
},_restoreSectionTabs:function(_aa9,_aaa){
this._log(_a91.getProperty("curam.tab.TabSessionManager.saved.tabs"));
for(var i=0;i<_aa9.length;i++){
var _aab=_aa9[i];
this._log(_a91.getProperty("curam.tab.TabSessionManager.saved.tab"),_aab,i);
dojo.publish(curam.tab.getTabController().TAB_TOPIC,[new curam.ui.OpenTabEvent(_aab,null,this._isOpenInBackground(_aab,_aaa,i))]);
}
},_restoreModal:function(_aac,_aad){
this._log(_a91.getProperty("curam.tab.TabSessionManager.restore.modal"));
this._tabLoadedCount=0;
this._tabCountForCurrentSection=_aac.length;
this._tabCountForOtherSections=0;
var _aae=[];
for(var i=0;i<_aad.length;i++){
if(!_aae.includes(_aad[i].sectionID)){
_aae.push(_aad[i].sectionID);
this._tabCountForOtherSections++;
}
}
var _aaf=curam.util.getTopmostWindow().dojo.subscribe("/curam/main-content/page/loaded",this,function(_ab0,_ab1,_ab2){
this._tabLoadedCount++;
var _ab3=false;
if(_ab2.tabDescriptor.openInBackground===false){
_ab3=true;
}else{
if(this._tabCountForCurrentSection===0&&this._tabLoadedCount===this._tabCountForOtherSections){
_ab3=true;
}else{
if(this._tabCountForCurrentSection+this._tabCountForOtherSections<=1){
_ab3=true;
}
}
}
if(_ab3){
dojo.publish("curam/tab/restoreModal");
dojo.unsubscribe(_aaf);
}
});
},_connectSelectionListeners:function(_ab4){
var _ab5=false;
for(var _ab6 in _ab4){
if(curam.tab.getTabContainer(_ab6)){
dojo.subscribe(curam.tab.getTabContainer(_ab6).id+"-selectChild",dojo.hitch(this,this.tabContentPaneSelected));
_ab5=true;
}
}
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",dojo.hitch(this,this.tabSectionSelected));
return _ab5;
},tabUpdated:function(_ab7){
var _ab8=sessionStorage.getItem("fileEditWigetDialog");
if(!_ab8){
this._log(_a91.getProperty("curam.tab.TabSessionManager.saving.tab"),_ab7);
new curam.ui.ClientDataAccessor().set("/data/tab/update",_ab7.toJson());
}else{
sessionStorage.removeItem("fileEditWigetDialog");
}
},tabClosed:function(_ab9){
this._log(_a91.getProperty("curam.tab.TabSessionManager.tab.closed"),_ab9);
new curam.ui.ClientDataAccessor().set("/data/tab/close",_ab9.toJson());
},tabSelected:function(_aba){
this._log(_a91.getProperty("curam.tab.TabSessionManager.selected.tab"),_aba);
if(_aba.tabSignature){
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_aba.toJson();
this._log(_a91.getProperty("curam.tab.TabSessionManager.recorded"),_aba);
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.not.recorded"),_aba);
}
curam.debug.log("curam.tab.TabSessionManager.tabSelected calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
},tabContentPaneSelected:function(_abb){
if(_abb.tabDescriptor){
this.tabSelected(_abb.tabDescriptor);
dojo.publish("curam/tab/selected",[_abb.id]);
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.no.descriptor"));
}
if(_abb.isLoaded){
curam.util.TabActionsMenu.setInlinedTabMenuItemsDisplayed(_abb.id);
}
},tabSectionSelected:function(_abc){
var _abd=false;
if(_abc){
var id=_abc.id;
this._log(_a91.getProperty("curam.tab.TabSessionManager.new.section")+" '"+id+"'.");
var _abe=id.substring(0,id.length-4);
var _abf=curam.tab.getSelectedTab(_abe);
if(_abf){
this._log(_a91.getProperty("curam.tab.TabSessionManager.changing.selection"));
this.tabContentPaneSelected(_abf);
_abd=true;
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.not.changing.selection"));
}
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.no.container"));
}
curam.debug.log("curam.tab.TabSessionManager.tabSectionSelected calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
return _abd;
},_isNewSession:function(){
return false;
},_getPrevSelectedTab:function(){
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.tab"));
var _ac0;
_ac0=localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY];
var _ac1=null;
if(_ac0){
_ac1=curam.tab.TabDescriptor.fromJson(_ac0);
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.tab.found"),_ac1);
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.tab.not.found"));
}
return _ac1;
},_getPrevSelectedModal:function(){
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.modal"));
var _ac2;
_ac2=localStorage[curam.tab.TabSessionManager.PREVIOUSLY_SELECTED_MODAL_KEY];
var _ac3=null;
if(_ac2){
_ac3=curam.tab.TabDescriptor.fromJson(_ac2);
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.modal.found"),_ac3);
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.previous.modal.not.found"));
}
return _ac3;
},_isOpenInBackground:function(_ac4,_ac5,pos){
var _ac6=true;
if(_ac5&&_ac5.tabSignature==_ac4.tabSignature){
this._log(_a91.getProperty("curam.tab.TabSessionManager.foreground"),_ac4,pos);
_ac6=false;
}else{
this._log(_a91.getProperty("curam.tab.TabSessionManager.background"),_ac4,pos);
}
return _ac6;
},_getHomePageTab:function(){
this._log(_a91.getProperty("curam.tab.TabSessionManager.home.page")+" '"+USER_HOME_PAGE_ID+"'.");
if(!USER_HOME_PAGE_TAB_ASSOC.tabIDs||!USER_HOME_PAGE_TAB_ASSOC.sectionID){
throw new Error("The application cannot be launched because the home page, '"+USER_HOME_PAGE_ID+"', has not been associated with a section or "+" tab.");
}
var _ac7=USER_HOME_PAGE_TAB_ASSOC.tabIDs[0];
var _ac8=USER_HOME_PAGE_TAB_ASSOC.sectionID;
var _ac9=new curam.tab.TabDescriptor(_ac8,_ac7);
var _aca=new curam.ui.PageRequest(USER_HOME_PAGE_ID,true);
_ac9.isHomePage=true;
_ac9.setTabSignature([],_aca,true);
_ac9.setTabContent(_aca);
this._log(_a91.getProperty("curam.tab.TabSessionManager.created"),_ac9);
return _ac9;
},_log:function(msg,_acb,pos){
if(curam.debug.enabled()){
var _acc="TAB SESSION";
if(typeof pos=="number"){
_acc+=" [pos="+pos+"]";
}
curam.debug.log(_acc+": "+msg+(_acb?" "+_acb.toJson():""));
}
}});
dojo.mixin(curam.tab.TabSessionManager,{SELECTED_TAB_KEY:"curam_selected_tab",PREVIOUSLY_SELECTED_MODAL_KEY:"curam_previously_selected_modal",SELECTED_TAB_SESSION_KEY:"curam_selected_tab_session"});
return _a93;
});
},"dijit/tree/_dndSelector":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/dnd/common","dojo/dom","dojo/mouse","dojo/on","dojo/touch","../a11yclick","./_dndContainer"],function(_acd,_ace,_acf,lang,_ad0,dom,_ad1,on,_ad2,_ad3,_ad4){
return _ace("dijit.tree._dndSelector",_ad4,{constructor:function(){
this.selection={};
this.anchor=null;
this.events.push(on(this.tree.domNode,_ad2.press,lang.hitch(this,"onMouseDown")),on(this.tree.domNode,_ad2.release,lang.hitch(this,"onMouseUp")),on(this.tree.domNode,_ad2.move,lang.hitch(this,"onMouseMove")),on(this.tree.domNode,_ad3.press,lang.hitch(this,"onClickPress")),on(this.tree.domNode,_ad3.release,lang.hitch(this,"onClickRelease")));
},singular:false,getSelectedTreeNodes:function(){
var _ad5=[],sel=this.selection;
for(var i in sel){
_ad5.push(sel[i]);
}
return _ad5;
},selectNone:function(){
this.setSelection([]);
return this;
},destroy:function(){
this.inherited(arguments);
this.selection=this.anchor=null;
},addTreeNode:function(node,_ad6){
this.setSelection(this.getSelectedTreeNodes().concat([node]));
if(_ad6){
this.anchor=node;
}
return node;
},removeTreeNode:function(node){
var _ad7=_acd.filter(this.getSelectedTreeNodes(),function(_ad8){
return !dom.isDescendant(_ad8.domNode,node.domNode);
});
this.setSelection(_ad7);
return node;
},isTreeNodeSelected:function(node){
return node.id&&!!this.selection[node.id];
},setSelection:function(_ad9){
var _ada=this.getSelectedTreeNodes();
_acd.forEach(this._setDifference(_ada,_ad9),lang.hitch(this,function(node){
node.setSelected(false);
if(this.anchor==node){
delete this.anchor;
}
delete this.selection[node.id];
}));
_acd.forEach(this._setDifference(_ad9,_ada),lang.hitch(this,function(node){
node.setSelected(true);
this.selection[node.id]=node;
}));
this._updateSelectionProperties();
},_setDifference:function(xs,ys){
_acd.forEach(ys,function(y){
y.__exclude__=true;
});
var ret=_acd.filter(xs,function(x){
return !x.__exclude__;
});
_acd.forEach(ys,function(y){
delete y["__exclude__"];
});
return ret;
},_updateSelectionProperties:function(){
var _adb=this.getSelectedTreeNodes();
var _adc=[],_add=[];
_acd.forEach(_adb,function(node){
var ary=node.getTreePath();
_add.push(node);
_adc.push(ary);
},this);
var _ade=_acd.map(_add,function(node){
return node.item;
});
this.tree._set("paths",_adc);
this.tree._set("path",_adc[0]||[]);
this.tree._set("selectedNodes",_add);
this.tree._set("selectedNode",_add[0]||null);
this.tree._set("selectedItems",_ade);
this.tree._set("selectedItem",_ade[0]||null);
},onClickPress:function(e){
if(this.current&&this.current.isExpandable&&this.tree.isExpandoNode(e.target,this.current)){
return;
}
if(e.type=="mousedown"&&_ad1.isLeft(e)){
e.preventDefault();
}
var _adf=e.type=="keydown"?this.tree.focusedChild:this.current;
if(!_adf){
return;
}
var copy=_ad0.getCopyKeyState(e),id=_adf.id;
if(!this.singular&&!e.shiftKey&&this.selection[id]){
this._doDeselect=true;
return;
}else{
this._doDeselect=false;
}
this.userSelect(_adf,copy,e.shiftKey);
},onClickRelease:function(e){
if(!this._doDeselect){
return;
}
this._doDeselect=false;
this.userSelect(e.type=="keyup"?this.tree.focusedChild:this.current,_ad0.getCopyKeyState(e),e.shiftKey);
},onMouseMove:function(){
this._doDeselect=false;
},onMouseDown:function(){
},onMouseUp:function(){
},_compareNodes:function(n1,n2){
if(n1===n2){
return 0;
}
if("sourceIndex" in document.documentElement){
return n1.sourceIndex-n2.sourceIndex;
}else{
if("compareDocumentPosition" in document.documentElement){
return n1.compareDocumentPosition(n2)&2?1:-1;
}else{
if(document.createRange){
var r1=doc.createRange();
r1.setStartBefore(n1);
var r2=doc.createRange();
r2.setStartBefore(n2);
return r1.compareBoundaryPoints(r1.END_TO_END,r2);
}else{
throw Error("dijit.tree._compareNodes don't know how to compare two different nodes in this browser");
}
}
}
},userSelect:function(node,_ae0,_ae1){
if(this.singular){
if(this.anchor==node&&_ae0){
this.selectNone();
}else{
this.setSelection([node]);
this.anchor=node;
}
}else{
if(_ae1&&this.anchor){
var cr=this._compareNodes(this.anchor.rowNode,node.rowNode),_ae2,end,_ae3=this.anchor;
if(cr<0){
_ae2=_ae3;
end=node;
}else{
_ae2=node;
end=_ae3;
}
var _ae4=[];
while(_ae2!=end){
_ae4.push(_ae2);
_ae2=this.tree._getNext(_ae2);
}
_ae4.push(end);
this.setSelection(_ae4);
}else{
if(this.selection[node.id]&&_ae0){
this.removeTreeNode(node);
}else{
if(_ae0){
this.addTreeNode(node,true);
}else{
this.setSelection([node]);
this.anchor=node;
}
}
}
}
},getItem:function(key){
var _ae5=this.selection[key];
return {data:_ae5,type:["treeNode"]};
},forInSelectedItems:function(f,o){
o=o||_acf.global;
for(var id in this.selection){
f.call(o,this.getItem(id),id,this);
}
}});
});
},"dijit/_OnDijitClickMixin":function(){
define(["dojo/on","dojo/_base/array","dojo/keys","dojo/_base/declare","dojo/has","./a11yclick"],function(on,_ae6,keys,_ae7,has,_ae8){
var ret=_ae7("dijit._OnDijitClickMixin",null,{connect:function(obj,_ae9,_aea){
return this.inherited(arguments,[obj,_ae9=="ondijitclick"?_ae8:_ae9,_aea]);
}});
ret.a11yclick=_ae8;
return ret;
});
},"curam/pagination":function(){
define(["dojo/parser","dojo/dom-class","dojo/dom-construct","dojo/dom-attr","curam/debug","curam/define","curam/pagination/ControlPanel","curam/pagination/StateController"],function(_aeb,_aec,_aed,_aee,_aef){
curam.define.singleton("curam.pagination",{defaultPageSize:15,threshold:15,listModels:{},ROW_COUNT_CLASS_NAME:"numRows-",ESC_SCRIPT_START:"<!--@pg@",ESC_SCRIPT_END:"@pg@-->",localizedStrings:{firstPage_btn:"|<",firstPage_title:"$not-localized$ First page",prevPage_btn:"<",prevPage_title:"$not-localized$ Previous page",nextPage_btn:">",nextPage_title:"$not-localized$ Next page",lastPage_btn:">|",lastPage_title:"$not-localized$ Last page",pageSize_title:"$not-localized$ Page size",pagination_info:"$not-localized$ Displaying rows %s to %s out of %s",page_title:"Go to page"},addPagination:function(_af0,_af1){
var _af2=_af0.getRowCount();
var _af3=_af0.getId();
if(_af2<=curam.pagination.threshold){
dojo.setObject("curam.shortlist."+_af3,true);
_af0.showRange(1,_af2);
return;
}
_aef.log("curam.pagination.addPagination: listId: ",_af3);
if(curam.pagination.listModels[_af3]){
throw "Pagination on this list has already been initialized: "+_af3;
}
curam.pagination.listModels[_af3]=_af0;
_aef.log("curam.pagination.listModels : ",curam.pagination.listModels);
var gui=new curam.pagination.ControlPanel(_af1);
var _af4=new curam.pagination.StateController(_af0,gui);
_af0._controller=_af4;
dojo.subscribe("/curam/list/toBeSorted",this,function(_af5){
_aef.log(_aef.getProperty("curam.omega3-util.received")+" /curam/list/toBeSorted "+_aef.getProperty("curam.omega3-util.for")+":",_af5);
var _af6=curam.pagination.listModels[_af5];
_af6&&curam.pagination.unpackAll(_af6);
});
dojo.subscribe("/curam/list/sorted",this,function(_af7){
_aef.log(_aef.getProperty("curam.omega3-util.received")+" /curam/list/sorted "+_aef.getProperty("curam.omega3-util.for")+":",_af7);
var _af8=curam.pagination.listModels[_af7];
_af8&&curam.pagination.paginatedListSorted(_af8);
});
_af4.gotoFirst();
},paginatedListSorted:function(_af9){
_af9._controller.reset();
},unpackRows:function(_afa,_afb){
var _afc=_afa.innerHTML;
var _afd=_aec.contains(_afa,"has-row-actions");
if(_afd){
_afc=_afc.replace(new RegExp(curam.pagination.ESC_SCRIPT_START,"g"),"<script type=\"text/javascript\">");
_afc=_afc.replace(new RegExp(curam.pagination.ESC_SCRIPT_END,"g"),"</script>");
}
var _afe=_aed.toDom(_afc);
if(_afd){
dojo.query("script",_afe).forEach(function(s){
eval(s.innerHTML);
});
_aeb.parse(_afe);
}
if(_afb){
_afb.appendChild(_afe);
}else{
_aed.place(_afe,_afa,"replace");
}
},unpackAll:function(_aff){
_aff._controller.gotoLast();
},readListContent:function(_b00){
return dojo.query("tbody > *",_b00).filter(function(n){
return typeof (n.tagName)!="undefined"&&(n.tagName=="TR"||(n.tagName=="SCRIPT"&&_aee.get(n,"type")=="list-row-container"));
});
},getNumRowsInBlock:function(_b01){
var _b02=dojo.filter(_b01.className.split(" "),function(cn){
return cn.indexOf(curam.pagination.ROW_COUNT_CLASS_NAME)==0;
});
return parseInt(_b02[0].split(curam.pagination.ROW_COUNT_CLASS_NAME)[1]);
}});
return curam.pagination;
});
},"dijit/layout/StackController":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-construct","dojo/keys","dojo/_base/lang","dojo/on","dojo/topic","../focus","../registry","../_Widget","../_TemplatedMixin","../_Container","../form/ToggleButton","dojo/touch"],function(_b03,_b04,_b05,_b06,keys,lang,on,_b07,_b08,_b09,_b0a,_b0b,_b0c,_b0d){
var _b0e=_b04("dijit.layout._StackButton",_b0d,{tabIndex:"-1",closeButton:false,_aria_attr:"aria-selected",buildRendering:function(evt){
this.inherited(arguments);
(this.focusNode||this.domNode).setAttribute("role","tab");
}});
var _b0f=_b04("dijit.layout.StackController",[_b0a,_b0b,_b0c],{baseClass:"dijitStackController",templateString:"<span role='tablist' data-dojo-attach-event='onkeydown'></span>",containerId:"",buttonWidget:_b0e,buttonWidgetCloseClass:"dijitStackCloseButton",pane2button:function(id){
return _b09.byId(this.id+"_"+id);
},postCreate:function(){
this.inherited(arguments);
this.own(_b07.subscribe(this.containerId+"-startup",lang.hitch(this,"onStartup")),_b07.subscribe(this.containerId+"-addChild",lang.hitch(this,"onAddChild")),_b07.subscribe(this.containerId+"-removeChild",lang.hitch(this,"onRemoveChild")),_b07.subscribe(this.containerId+"-selectChild",lang.hitch(this,"onSelectChild")),_b07.subscribe(this.containerId+"-containerKeyDown",lang.hitch(this,"onContainerKeyDown")));
this.containerNode.dojoClick=true;
this.own(on(this.containerNode,"click",lang.hitch(this,function(evt){
var _b10=_b09.getEnclosingWidget(evt.target);
if(_b10!=this.containerNode&&!_b10.disabled&&_b10.page){
for(var _b11=evt.target;_b11!==this.containerNode;_b11=_b11.parentNode){
if(_b05.contains(_b11,this.buttonWidgetCloseClass)){
this.onCloseButtonClick(_b10.page);
break;
}else{
if(_b11==_b10.domNode){
this.onButtonClick(_b10.page);
break;
}
}
}
}
})));
},onStartup:function(info){
this.textDir=info.textDir;
_b03.forEach(info.children,this.onAddChild,this);
if(info.selected){
this.onSelectChild(info.selected);
}
var _b12=_b09.byId(this.containerId).containerNode,_b13=lang.hitch(this,"pane2button"),_b14={"title":"label","showtitle":"showLabel","iconclass":"iconClass","closable":"closeButton","tooltip":"title","disabled":"disabled","textdir":"textdir"},_b15=function(attr,_b16){
return on(_b12,"attrmodified-"+attr,function(evt){
var _b17=_b13(evt.detail&&evt.detail.widget&&evt.detail.widget.id);
if(_b17){
_b17.set(_b16,evt.detail.newValue);
}
});
};
for(var attr in _b14){
this.own(_b15(attr,_b14[attr]));
}
},destroy:function(_b18){
this.destroyDescendants(_b18);
this.inherited(arguments);
},onAddChild:function(page,_b19){
var Cls=lang.isString(this.buttonWidget)?lang.getObject(this.buttonWidget):this.buttonWidget;
var _b1a=new Cls({id:this.id+"_"+page.id,name:this.id+"_"+page.id,label:page.title,disabled:page.disabled,ownerDocument:this.ownerDocument,dir:page.dir,lang:page.lang,textDir:page.textDir||this.textDir,showLabel:page.showTitle,iconClass:page.iconClass,closeButton:page.closable,title:page.tooltip,page:page});
this.addChild(_b1a,_b19);
page.controlButton=_b1a;
if(!this._currentChild){
this.onSelectChild(page);
}
var _b1b=page._wrapper.getAttribute("aria-labelledby")?page._wrapper.getAttribute("aria-labelledby")+" "+_b1a.id:_b1a.id;
page._wrapper.removeAttribute("aria-label");
page._wrapper.setAttribute("aria-labelledby",_b1b);
},onRemoveChild:function(page){
if(this._currentChild===page){
this._currentChild=null;
}
var _b1c=this.pane2button(page.id);
if(_b1c){
this.removeChild(_b1c);
_b1c.destroy();
}
delete page.controlButton;
},onSelectChild:function(page){
if(!page){
return;
}
if(this._currentChild){
var _b1d=this.pane2button(this._currentChild.id);
_b1d.set("checked",false);
_b1d.focusNode.setAttribute("tabIndex","-1");
}
var _b1e=this.pane2button(page.id);
_b1e.set("checked",true);
this._currentChild=page;
_b1e.focusNode.setAttribute("tabIndex","0");
var _b1f=_b09.byId(this.containerId);
},onButtonClick:function(page){
var _b20=this.pane2button(page.id);
_b08.focus(_b20.focusNode);
if(this._currentChild&&this._currentChild.id===page.id){
_b20.set("checked",true);
}
var _b21=_b09.byId(this.containerId);
_b21.selectChild(page);
},onCloseButtonClick:function(page){
var _b22=_b09.byId(this.containerId);
_b22.closeChild(page);
if(this._currentChild){
var b=this.pane2button(this._currentChild.id);
if(b){
_b08.focus(b.focusNode||b.domNode);
}
}
},adjacent:function(_b23){
if(!this.isLeftToRight()&&(!this.tabPosition||/top|bottom/.test(this.tabPosition))){
_b23=!_b23;
}
var _b24=this.getChildren();
var idx=_b03.indexOf(_b24,this.pane2button(this._currentChild.id)),_b25=_b24[idx];
var _b26;
do{
idx=(idx+(_b23?1:_b24.length-1))%_b24.length;
_b26=_b24[idx];
}while(_b26.disabled&&_b26!=_b25);
return _b26;
},onkeydown:function(e,_b27){
if(this.disabled||e.altKey){
return;
}
var _b28=null;
if(e.ctrlKey||!e._djpage){
switch(e.keyCode){
case keys.LEFT_ARROW:
case keys.UP_ARROW:
if(!e._djpage){
_b28=false;
}
break;
case keys.PAGE_UP:
if(e.ctrlKey){
_b28=false;
}
break;
case keys.RIGHT_ARROW:
case keys.DOWN_ARROW:
if(!e._djpage){
_b28=true;
}
break;
case keys.PAGE_DOWN:
if(e.ctrlKey){
_b28=true;
}
break;
case keys.HOME:
var _b29=this.getChildren();
for(var idx=0;idx<_b29.length;idx++){
var _b2a=_b29[idx];
if(!_b2a.disabled){
this.onButtonClick(_b2a.page);
break;
}
}
e.stopPropagation();
e.preventDefault();
break;
case keys.END:
var _b29=this.getChildren();
for(var idx=_b29.length-1;idx>=0;idx--){
var _b2a=_b29[idx];
if(!_b2a.disabled){
this.onButtonClick(_b2a.page);
break;
}
}
e.stopPropagation();
e.preventDefault();
break;
case keys.DELETE:
case "W".charCodeAt(0):
if(this._currentChild.closable&&(e.keyCode==keys.DELETE||e.ctrlKey)){
this.onCloseButtonClick(this._currentChild);
e.stopPropagation();
e.preventDefault();
}
break;
case keys.TAB:
if(e.ctrlKey){
this.onButtonClick(this.adjacent(!e.shiftKey).page);
e.stopPropagation();
e.preventDefault();
}
break;
}
if(_b28!==null){
this.onButtonClick(this.adjacent(_b28).page);
e.stopPropagation();
e.preventDefault();
}
}
},onContainerKeyDown:function(info){
info.e._djpage=info.page;
this.onkeydown(info.e);
}});
_b0f.StackButton=_b0e;
return _b0f;
});
},"curam/cdsl/connection/CuramConnection":function(){
define(["dojo/_base/declare","dojo/request/registry","curam/cdsl/_base/_Connection","curam/util"],function(_b2b,_b2c,_b2d,util){
var _b2e=_b2b(_b2d,{_baseUrl:null,constructor:function(_b2f){
this._baseUrl=_b2f;
},invoke:function(_b30,_b31){
this.inherited(arguments);
var _b32=util.getTopmostWindow();
return _b2c(_b30.url(this._baseUrl),{data:_b30.toJson(),method:"POST",headers:{"Content-Encoding":"UTF-8","csrfToken":_b32.csrfToken},query:null,preventCache:true,timeout:_b31?_b31:this._DEFAULT_REQUEST_TIMEOUT,handleAs:"text"});
}});
return _b2e;
});
},"dijit/form/_FormValueMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","./_FormWidgetMixin"],function(_b33,_b34,keys,lang,on,_b35){
return _b33("dijit.form._FormValueMixin",_b35,{readOnly:false,_setReadOnlyAttr:function(_b36){
_b34.set(this.focusNode,"readOnly",_b36);
this._set("readOnly",_b36);
},postCreate:function(){
this.inherited(arguments);
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.value;
}
},_setValueAttr:function(_b37,_b38){
this._handleOnChange(_b37,_b38);
},_handleOnChange:function(_b39,_b3a){
this._set("value",_b39);
this.inherited(arguments);
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
}});
});
},"dojo/date/stamp":function(){
define(["../_base/lang","../_base/array"],function(lang,_b3b){
var _b3c={};
lang.setObject("dojo.date.stamp",_b3c);
_b3c.fromISOString=function(_b3d,_b3e){
if(!_b3c._isoRegExp){
_b3c._isoRegExp=/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
}
var _b3f=_b3c._isoRegExp.exec(_b3d),_b40=null;
if(_b3f){
_b3f.shift();
if(_b3f[1]){
_b3f[1]--;
}
if(_b3f[6]){
_b3f[6]*=1000;
}
if(_b3e){
_b3e=new Date(_b3e);
_b3b.forEach(_b3b.map(["FullYear","Month","Date","Hours","Minutes","Seconds","Milliseconds"],function(prop){
return _b3e["get"+prop]();
}),function(_b41,_b42){
_b3f[_b42]=_b3f[_b42]||_b41;
});
}
_b40=new Date(_b3f[0]||1970,_b3f[1]||0,_b3f[2]||1,_b3f[3]||0,_b3f[4]||0,_b3f[5]||0,_b3f[6]||0);
if(_b3f[0]<100){
_b40.setFullYear(_b3f[0]||1970);
}
var _b43=0,_b44=_b3f[7]&&_b3f[7].charAt(0);
if(_b44!="Z"){
_b43=((_b3f[8]||0)*60)+(Number(_b3f[9])||0);
if(_b44!="-"){
_b43*=-1;
}
}
if(_b44){
_b43-=_b40.getTimezoneOffset();
}
if(_b43){
_b40.setTime(_b40.getTime()+_b43*60000);
}
}
return _b40;
};
_b3c.toISOString=function(_b45,_b46){
var _b47=function(n){
return (n<10)?"0"+n:n;
};
_b46=_b46||{};
var _b48=[],_b49=_b46.zulu?"getUTC":"get",date="";
if(_b46.selector!="time"){
var year=_b45[_b49+"FullYear"]();
date=["0000".substr((year+"").length)+year,_b47(_b45[_b49+"Month"]()+1),_b47(_b45[_b49+"Date"]())].join("-");
}
_b48.push(date);
if(_b46.selector!="date"){
var time=[_b47(_b45[_b49+"Hours"]()),_b47(_b45[_b49+"Minutes"]()),_b47(_b45[_b49+"Seconds"]())].join(":");
var _b4a=_b45[_b49+"Milliseconds"]();
if(_b46.milliseconds){
time+="."+(_b4a<100?"0":"")+_b47(_b4a);
}
if(_b46.zulu){
time+="Z";
}else{
if(_b46.selector!="time"){
var _b4b=_b45.getTimezoneOffset();
var _b4c=Math.abs(_b4b);
time+=(_b4b>0?"-":"+")+_b47(Math.floor(_b4c/60))+":"+_b47(_b4c%60);
}
}
_b48.push(time);
}
return _b48.join("T");
};
return _b3c;
});
},"dijit/layout/AccordionPane":function(){
define(["dojo/_base/declare","dojo/_base/kernel","./ContentPane"],function(_b4d,_b4e,_b4f){
return _b4d("dijit.layout.AccordionPane",_b4f,{constructor:function(){
_b4e.deprecated("dijit.layout.AccordionPane deprecated, use ContentPane instead","","2.0");
},onSelected:function(){
}});
});
},"curam/omega3-util":function(){
define(["dojo/dom-geometry","dojo/dom","dojo/dom-class","dojo/dom-attr","dojo/dom-style","dojo/dom-construct","dojo/sniff","dijit/registry","curam/util/EditableList","curam/debug","curam/validation","curam/util","curam/html","curam/GlobalVars","cm/_base/_dom","cm/_base/_form","curam/util/RuntimeContext"],function(_b50,dom,_b51,_b52,_b53,_b54,has,_b55,_b56,_b57,_b58,util){
var _b59={PopupMapping:function(name,_b5a){
this.name=name;
this.targetWidgetID=_b5a;
},openPopupFromCTCode:function(_b5b,_b5c,_b5d,_b5e,_b5f,_b60){
var _b61;
var list=_b5d.parentNode.parentNode.parentNode.childNodes[0];
var _b62=dijit.byNode(list);
if(_b62){
_b61=_b62.getValue();
}else{
var _b63=_b5d.parentNode.parentNode.parentNode.childNodes[1];
try{
_b61=_b63.options[_b63.selectedIndex].value;
}
catch(e){
}
if(_b61===undefined){
try{
_b61=list.options[list.selectedIndex].value;
}
catch(e){
}
}
if(_b61===undefined){
try{
var _b64=_b5d.parentNode.parentNode.parentNode.childNodes[0];
_b61=_b64.firstChild.value;
}
catch(e){
}
}
if(_b61===undefined){
curam.debug.log("Error: cannot find element containing codetable value in dropdown");
}
}
if(_b61!=""){
if(curam.popupCTCodeMappings[_b61]){
openPopupFromDomain(_b5b,_b5c,curam.popupCTCodeMappings[_b61],_b5e,false,_b5f,_b60);
}
}
},openPopupFromCTCodeNoDomain:function(_b65,_b66,_b67,_b68,_b69,_b6a){
var list=_b67.parentNode.parentNode.parentNode.childNodes[2];
var _b6b=dijit.byNode(list);
var _b6c;
var _b6d;
var _b6e;
var _b6f;
var _b70;
var _b71;
var _b72;
var _b73;
if(_b6b){
var _b74=_b6b.getValue();
}else{
var list=_b67.parentNode.parentNode.parentNode.childNodes[1];
var _b74=list.options[list.selectedIndex].value;
}
if(_b74!=""){
if(curam.popupCTCodeMappings[_b74]){
_b6c=getPopupProperties(curam.popupCTCodeMappings[_b74]);
_b6d=_b6c.pageID;
_b6e=_b6c.createPageID;
_b6f=_b6c.height;
_b70=_b6c.width;
_b71=_b6c.scrollBars;
_b72=_b6c.insertMode;
_b73=_b6c.codeTableCode;
var _b75=_b6c.uimType;
if(_b75=="DYNAMIC"){
openPopup(_b65,_b66,null,_b6d,_b6e,_b70,_b6f,_b71,_b72,null,null,_b68,false,_b69,_b6a);
}
}
}
},openPopupFromDomain:function(_b76,_b77,_b78,_b79,_b7a,_b7b,_b7c){
var _b7d=getPopupProperties(_b78);
var _b7e=_b7d.pageID;
var _b7f=_b7d.createPageID;
var _b80=_b7d.height;
var _b81=_b7d.width;
var _b82=_b7d.scrollBars;
var _b83=_b7d.insertMode;
var _b84=_b7d.codeTableCode;
var _b85=dojo.byId(_b76).previousElementSibling;
_b85&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_b85,window);
openPopup(_b76,_b77,_b78,_b7e,_b7f,_b81,_b80,_b82,_b83,_b84,_b79,_b7a,_b7b,_b7c);
},openPopupNoDomain:function(_b86,_b87,_b88,_b89,_b8a,_b8b,_b8c,_b8d,_b8e,_b8f,_b90,_b91){
openPopup(_b86,_b87,null,_b88,_b89,_b8a,_b8b,_b8c,_b8d,null,null,_b8e,_b8f,_b90,_b91);
var _b92=dojo.byId(_b86).previousElementSibling;
_b51.add(_b92,"placeholder-for-focus");
},openPopup:function(_b93,_b94,_b95,_b96,_b97,_b98,_b99,_b9a,_b9b,_b9c,_b9d,_b9e,_b9f,_ba0){
setMappingsLoaded(_b94);
if(curam.popupWindow&&!curam.popupWindow.closed){
curam.popupWindow.close();
}
curam.currentPopupInstanceName=_b94;
curam.currentPopupProps=setPopupProperties(_b96,_b95,_b9c,_b98,_b99,_b9a,_b97,_b9b,null);
var ctx=jsScreenContext;
ctx.addContextBits("POPUP");
ctx.clear("TAB|TREE|AGENDA");
var url="";
var _ba1;
if(_b9e==true){
url=_b97;
if(_ba0&&_ba0.length>0){
_ba1="&"+curam.util.secureURLsHashTokenParam+"="+_ba0;
}
}else{
url=_b96;
if(_b9f&&_b9f.length>0){
_ba1="&"+curam.util.secureURLsHashTokenParam+"="+_b9f;
}
}
if(_b9d&&_b9d.length>0){
url=url+"?"+_b9d;
_ba1?url+=_ba1+"&":url+="&";
}else{
url+="?";
}
url+=ctx.toRequestString();
if(window.curam.util.showModalDialog){
curam.util.showModalDialog(url,null,_b98,_b99,0,0,false,null,null);
}
},addPopupMapping:function(_ba2,_ba3,_ba4){
var _ba5=curam.popupMappingRepository;
if(curam.popupMappingLoaded[_ba2]==true){
return;
}
if(_ba5[_ba2]==null){
_ba5[_ba2]=[];
_ba5[_ba2][_ba3]=[];
_ba5[_ba2][_ba3][0]=_ba4;
}else{
if(_ba5[_ba2][_ba3]==null){
_ba5[_ba2][_ba3]=[];
_ba5[_ba2][_ba3][0]=_ba4;
}else{
var _ba6=_ba5[_ba2][_ba3].length;
_ba5[_ba2][_ba3][_ba6]=_ba4;
}
}
},setMappingsLoaded:function(_ba7){
curam.popupMappingLoaded[_ba7]=true;
},executeMapping:function(_ba8,_ba9){
dojo.body().setAttribute("tabIndex","-1");
var pmr=curam.popupMappingRepository;
var cpin=curam.currentPopupInstanceName;
if(!pmr||!pmr[cpin]||pmr[cpin][_ba8]==null){
return;
}
var _baa=null;
for(var i=0;i<pmr[cpin][_ba8].length;i++){
var _bab=null;
_bab=dom.byId(pmr[cpin][_ba8][i]);
_baa=_bab;
if(_bab.tagName=="SPAN"){
_bab.innerHTML=curam.html.splitWithTag(_ba9,null,null,escapeXML);
_bab.setAttribute("title",_ba9);
_bab._reposition=_bab._reposition||dojo.query("div",_bab).length>0;
if(_bab._reposition){
var _bac=cm.nextSibling(_bab,"span");
if(_bac){
var _bad=_b50.getMarginBoxSimple(_bab).h;
var _bae=_b50.getMarginBoxSimple(_bac).h;
_b53.set(_bac,"position","relative");
var diff=_bad-_bae-((dojo.isIE&&dojo.isIE<9)?2:0);
_b53.set(_bac,"bottom","-"+(diff)+"px");
}
}
}else{
if(_bab.tagName=="TEXTAREA"){
if(curam.currentPopupProps.insertMode=="insert"){
insertAtCursor(_bab,escapeXML(_ba9));
}else{
if(curam.currentPopupProps.insertMode=="append"){
_bab.value+=_ba9;
}else{
_bab.value=_ba9;
}
}
}else{
if(_b55.byId(pmr[cpin][_ba8][i])){
_b55.byId(pmr[cpin][_ba8][i]).set("value",_ba9);
_bab.value=_ba9;
}else{
_bab.value=_ba9;
var _baf=_bab.id;
if(_baf.indexOf("_value")>0){
var _bb0=_baf.replace("_value","_clear");
var _bb1=dom.byId(_bb0);
_bb1.classList.remove("bx--search-close--hidden");
}
}
}
}
}
setTimeout(function(){
if(_baa){
_baa.setAttribute("value",_ba9);
_baa.setAttribute("aria-label",_ba9);
_baa.focus();
}
},300);
dojo.body().setAttribute("tabIndex","0");
},insertAtCursor:function(_bb2,_bb3){
if(document.selection){
_bb2.focus();
sel=document.selection.createRange();
sel.text=_bb3;
}else{
if(_bb2.selectionStart||_bb2.selectionStart=="0"){
var _bb4=_bb2.selectionStart;
var _bb5=_bb2.selectionEnd;
_bb2.value=_bb2.value.substring(0,_bb4)+_bb3+_bb2.value.substring(_bb5,_bb2.value.length);
}else{
_bb2.value+=_bb3;
}
}
},escapeXML:function(_bb6){
return _bb6.replace(/&/g,"&#38;").replace(/</g,"&#60;").replace(/>/g,"&#62;").replace(/"/g,"&#34;").replace(/'/g,"&#39;");
},executeOpenerMapping:function(_bb7,_bb8){
var _bb9=undefined;
if(curam.util.isModalWindow()){
_bb9=curam.dialog.getParentWindow(window);
}else{
if(window.dialogArguments){
_bb9=window.dialogArguments[0];
}
}
if((_bb9)&&(!_bb9.closed)){
_bb9.executeMapping(_bb7,_bb8);
}else{
_b57.log("curam.omega3-util.executeOpenerMapping:, "+_b57.getProperty("curam.omega3-util.parent"));
}
},storePopupInputFromWidget:function(name,_bba){
var _bbb=null;
_bbb=dom.byId(_bba).value;
if(_bbb){
curam.popupInputs[name]=_bbb;
}else{
curam.popupInputs[name]="";
}
},getPopupInput:function(name){
if(curam.popupInputs[name]!=null){
return curam.popupInputs[name];
}else{
return "";
}
},PopupProperties:function(_bbc,_bbd,_bbe,_bbf,_bc0,_bc1,_bc2){
this.width=_bbd;
this.height=_bbe;
this.scrollBars=_bbf;
this.pageID=_bbc;
this.createPageID=_bc0;
if(_bc1==null){
this.insertMode="overwrite";
}else{
this.insertMode=_bc1;
}
if(_bc2!=null){
this.uimType=_bc2;
}
},setPopupProperties:function(_bc3,_bc4,_bc5,_bc6,_bc7,_bc8,_bc9,_bca,_bcb){
if(_bc5){
curam.popupCTCodeMappings[_bc5]=_bc4;
}
curam.popupPropertiesRepository[_bc4]=new PopupProperties(_bc3,_bc6,_bc7,_bc8,_bc9,_bca,_bcb);
},getPopupAttributes:function(_bcc,_bcd,_bce){
var _bcf="width="+_bcc+","+"height="+_bcd+","+"scrollbars="+(_bce?"yes":"no")+",";
return _bcf;
},getPopupAttributesIEModal:function(_bd0){
var _bd1="dialogWidth:"+curam.popupPropertiesRepository[_bd0].width+"px;"+"dialogHeight:"+curam.popupPropertiesRepository[_bd0].height+"px;";
return _bd1;
},trimFileExtension:function(_bd2){
var _bd3=_bd2.lastIndexOf("/")+1;
if(_bd3==-1){
_bd3=_bd2.lastIndexOf("\\")+1;
}
if(_bd3==-1){
_bd3=0;
}
return _bd2.substring(_bd3,_bd2.lastIndexOf("."));
},getPopupProperties:function(_bd4){
return curam.popupPropertiesRepository[_bd4];
},validateDate:function(_bd5){
return curam.validation.validateDate(_bd5).valid;
},addStartDate:function(_bd6){
var _bd7=dom.byId("startDate").value;
var _bd8=curam.validation.validateDate(_bd7);
if(_bd8.valid){
var _bd9=dom.byId("gotoDate");
_bd9.href=curam.util.replaceUrlParam(_bd9.href,"startDate",_bd7);
return true;
}else{
require(["curam/validation/calendar"],function(_bda){
alert(_bda.invalidGotoDateEntered.replace("%s",_bd7).replace("%s",jsDFs));
});
dojo.stopEvent(_bd6);
return false;
}
},checkEnter:function(_bdb){
if(_bdb.keyCode==13){
if(addStartDate(_bdb)){
var _bdc=dom.byId("gotoDate");
window.location=_bdc.href;
return true;
}
return false;
}
return true;
},createWindowName:function(_bdd){
var _bde=new String("");
for(var i=0;i<_bdd.length;i++){
var ch=_bdd.charAt(i);
if(ch=="$"||ch=="."){
_bde+="_";
}else{
_bde+=ch;
}
}
return _bde;
},clearPopup:function(_bdf,_be0){
var _be1=_bdf.id.substring(0,_bdf.id.indexOf("_clear"));
var _be2=_be1+"_value";
var _be3=_be1+"_desc";
var _be4=_be1+"_deschf";
var _be5=dom.byId(_bdf);
_be5.classList.add("bx--search-close--hidden");
var _be6=dom.byId(_be2);
if(_be6){
if(_be6.tagName=="INPUT"){
_be6.value="";
}else{
if(_be6.tagName=="TEXTAREA"){
_be6.value="";
}
}
if(_be6.tagName=="SPAN"){
_be6.innerHTML=curam.POPUP_EMPTY_SPAN_VALUE;
}
}
var _be7=dom.byId(_be3);
if(_be7){
if(_be7.tagName=="INPUT"){
_be7.value="";
}else{
if(_be7.tagName=="TEXTAREA"){
_be7.value="";
}else{
if(_be7.tagName=="SPAN"){
_be7.innerHTML=curam.POPUP_EMPTY_SPAN_VALUE;
_b52.set(_be7,"title",_b52.get(_be7,"data-field-label"));
}
}
}
}
var _be8=dom.byId(_be4);
if(_be8){
if(_be8.tagName=="INPUT"){
_be8.value="";
}else{
_be8.innerHTML="&nbsp";
}
}
if(_be0){
_be0=dojo.fixEvent(_be0);
dojo.stopEvent(_be0);
}
return false;
},showClearIcon:function(_be9){
var _bea=dom.byId(_be9+"_clear");
var _beb=dom.byId(_be9+"_value");
if(_beb.value==""){
_bea.classList.add("bx--search-close--hidden");
}else{
_bea.classList.remove("bx--search-close--hidden");
}
},swapImage:function(_bec,_bed){
dom.byId(_bec).src=_bed;
},appendTabColumn:function(_bee,_bef){
var _bf0;
var _bf1=[];
dojo.query("input[name='"+_bee+"']").filter(function(_bf2){
return _bf2.checked;
}).forEach(function(_bf3){
_bf1.push(_bf3.value);
});
_bf0=_bf1.join("\t");
_bef.href=_bef.href+(_bef.href.indexOf("?")==-1?"?":"&");
if(_bf0!=""){
_bef.href=_bef.href+_bee+"="+encodeURIComponent(_bf0);
}else{
_bef.href=_bef.href+_bee+"=";
}
},ToggleAll:function(e,_bf4){
dojo.query("input[name='"+_bf4+"']").forEach(function(_bf5){
if(_bf5.checked===true){
_bf5.checked=false;
}else{
_bf5.checked=true;
}
});
},ToggleSelectAll:function(e,_bf6){
if(e.checked){
CheckAll(_bf6);
}else{
ClearAll(_bf6);
}
},CheckAll:function(_bf7){
dojo.query("input[name^='"+_bf7+"'][onclick]").forEach(function(_bf8){
_bf8.checked=true;
_b56._doToggling(_bf8);
});
},ClearAll:function(_bf9){
dojo.query("input[name^='"+_bf9+"'][onclick]").forEach(function(_bfa){
_bfa.checked=false;
_b56._doToggling(_bfa);
});
},Check:function(e){
e.checked=true;
},Clear:function(e){
e.checked=false;
},ChooseSelectAll:function(e,_bfb,_bfc){
var sAll=dom.byId(_bfb);
if(sAll){
if(dojo.query("input[name='"+_bfc+"']").every("return item.checked")){
Check(sAll);
}else{
Clear(sAll);
}
}
_b56._doToggling(e);
},ChooseCheckbox:function(e,_bfd){
var _bfe=dom.byId(_bfd);
if(_bfe){
if(e.checked){
Clear(_bfd);
}else{
Check(_bfd);
}
}
_b56._doToggling(e);
},selectAllIfNeeded:function(_bff,_c00){
if(dojo.query("input[name='"+_c00+"']").some("return !item.checked")){
return;
}
var sAll=dom.byId(_bff);
if(sAll){
Check(sAll);
}
},dc:function(_c01,_c02,_c03){
if(cm.wasFormSubmitted(_c01)){
var evt=dojo.fixEvent(_c03);
dojo.stopEvent(evt);
return false;
}
cm.setFormSubmitted(_c01,1);
var _c04=dojo.query(".curam-default-action")[0];
if(_c04!==null&&_c04!==undefined){
sessionStorage.setItem("curamDefaultActionId",_c04.id);
}
return true;
},setFocus:function(){
curam.util.setFocus();
},setParentFocus:function(_c05){
_b57.log("curam.omega3-util.setParentFocus: "+_b57.getProperty("curam.omega3-util.called"));
var _c06=curam.dialog.getParentWindow(window);
if(!_c06.closed){
var _c07=dojo.query("button..bx--search-close.placeholder-for-focus",_c06.document);
if(_c07.length==1){
_c07[0].focus();
_b51.remove(_c07[0],"placeholder-for-focus");
}else{
_c06.focus();
var _c08=dojo.query(".placeholder-for-focus",_c06.document);
if(_c08.length==1){
_b51.remove(_c08[0],"placeholder-for-focus");
}
}
}else{
alert("The parent window has been closed");
}
if(_c05||window.event){
dojo.stopEvent(_c05||window.event);
}
curam.dialog.closeModalDialog();
},getParentWin:function(){
return curam.dialog.getParentWindow(window);
},addQuestionsFromPopup:function(evt){
evt=dojo.fixEvent(evt);
dojo.stopEvent(evt);
if(window._questionsAdded){
return;
}
window._questionsAdded=true;
var _c09=getParentWin();
var _c0a=dojo.query("INPUT");
var _c0b=[];
dojo.query("INPUT[type='checkbox']").forEach(function(item){
if(item.checked&&item.id.indexOf("__o3mswa")<0){
_c0b.push(item.value);
}
});
var _c0c=dojo.toJson(_c0b);
_c09.newQuestions=_c0c;
_c09.curam.matrix.Constants.container.matrix.addQuestionsFromPopup();
curam.dialog.closeModalDialog();
return false;
},getRequestParams:function(_c0d){
var _c0e=[];
var uri=new dojo._Url(_c0d);
if(uri.query!=null){
var _c0f=uri.query.split("&");
for(var i=0;i<_c0f.length;i++){
var arr=_c0f[i].split("=");
_c0e[arr[0]]=arr[1];
}
}
return _c0e;
},openModalDialog:function(_c10,_c11,left,top){
curam.util.openModalDialog(_c10,_c11,left,top);
},initCluster:function(_c12){
var _c13=_c12.parentNode;
var _c14=dojo.query("div.toggle-group",_c13);
if(_c14.length>=1){
return _c14[0];
}
var next=cm.nextSibling(_c12,"p")||cm.nextSibling(_c12,"table");
if(!next){
return;
}
_c14=_b54.create("div",{"class":"toggle-group"},next,"before");
var arr=[];
var _c15=dojo.query("p.description",_c12)[0];
if(_c15){
arr.push(_c15);
var _c16=_b53.get(_c12,"marginBottom");
_b53.set(_c12,"marginBottom",0);
_b53.set(_c15,"marginBottom",_c16+"px");
}
var _c17=_c13;
while(_c17&&!(_b51.contains(_c17,"cluster")||_b51.contains(_c17,"list"))){
_c17=_c17.parentNode;
}
_c14.isClosed=_b51.contains(_c17,"uncollapse")?true:false;
if(_c14.isClosed){
_b53.set(_c14,"display","none");
}
for(var _c18=0;_c18<_c13.childNodes.length;_c18++){
if(_c13.childNodes[_c18]==_c12||_c13.childNodes[_c18]==_c14){
continue;
}
arr.push(_c13.childNodes[_c18]);
}
for(var _c18=0;_c18<arr.length;_c18++){
_c14.appendChild(arr[_c18]);
}
return _c14;
},initClusterHeight:function(_c19,_c1a,_c1b){
if(_c19.correctHeight){
return;
}
var _c1c=dojo._getBorderBox(_c1a).h;
var _c1d=0,_c1e;
for(var _c1f=0;_c1f<_c19.childNodes.length;_c1f++){
_c1e=_c19.childNodes[_c1f];
if(_c1e==_c1a){
continue;
}
_c1d+=dojo._getBorderBox(_c1e).h;
}
if(_c1d==0){
return;
}
if(_c1b){
_b53.set(_c1a.parentNode,"height","");
}
_c19.correctHeight=_c1d;
},getCursorPosition:function(e){
e=e||dojo.global().event;
var _c20={x:0,y:0};
if(e.pageX||e.pageY){
_c20.x=e.pageX;
_c20.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_c20.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_c20.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _c20;
},overElement:function(_c21,e){
_c21=dom.byId(_c21);
var _c22=getCursorPosition(e);
var bb=dojo._getBorderBox(_c21);
var _c23=dojo._abs(_c21,true);
var top=_c23.y;
var _c24=top+bb.h;
var left=_c23.x;
var _c25=left+bb.w;
return (_c22.x>=left&&_c22.x<=_c25&&_c22.y>=top&&_c22.y<=_c24);
},_getToggleImages:function(){
var _c26="../themes/curam/images/chevron--down20-enabled.svg";
var _c27="../themes/curam/images/chevron--down20-enabled.svg";
var _c28;
var _c29;
var _c2a=util.isRtlMode();
if(_c2a){
_c28="../themes/curam/images/chevron--right20-enabled.svg";
_c29="../themes/curam/images/chevron--right20-enabled.svg";
}else{
_c28="../themes/curam/images/chevron--left20-enabled.svg";
_c29="../themes/curam/images/chevron--left20-enabled.svg";
}
return [_c26,_c28,_c27,_c29];
},setToggleClusterIcon:function(img){
var _c2b=this._getToggleImages();
var _c2c=_c2b[0];
if(img.className=="hoverIcon"){
_c2c=_c2b[2];
}
var _c2d=img;
while(_c2d&&!(_b51.contains(_c2d,"cluster")||_b51.contains(_c2d,"list"))){
_c2d=_c2d.parentNode;
}
if(_b51.contains(_c2d,"is-collapsed")){
if(img.className=="hoverIcon"){
_c2c=_c2b[3];
}else{
_c2c=_c2b[1];
}
}
img.onload=null;
img.src=_c2c;
img.alt="";
},toggleCluster:function(_c2e,_c2f){
var _c30=_c2e;
var img;
var _c31;
var _c32=dojo.query("img",_c2e);
if(_c32&&_c32.length==2){
img=_c32[0];
_c31=_c32[1];
}
var _c33=this._getToggleImages();
var _c34=_c33[0];
var _c35=_c33[1];
var _c36=_c33[2];
var _c37=_c33[3];
while(_c2e&&!(_b51.contains(_c2e,"cluster")||_b51.contains(_c2e,"list"))){
_c2e=_c2e.parentNode;
}
var _c38=false;
var _c39=dojo.query(" > :not(.header-wrapper) ",_c2e.childNodes[0]);
if(!_b51.contains(_c39[0],"toggleDiv")){
var _c3a=_b54.create("div",{className:"toggleDiv"},_c39[0].parentNode);
var _c3b=_b54.create("div",{className:"toggleDiv2"},_c39[0].parentNode);
_c39.forEach(function(node){
var _c3c;
var _c3d;
dojo.query(".cke",node).forEach(function(_c3e){
_c3c=dojo.query("textarea",_c3e.parentElement)[0].id;
for(var i in CKEDITOR.instances){
if(CKEDITOR.instances[i].name.includes(_c3c)){
_c3d=CKEDITOR.instances[i].config.customConfig;
CKEDITOR.instances[i].destroy();
}
}
});
if(node.tagName!="DIV"){
_c3a.appendChild(node);
}else{
_c3b.appendChild(node);
}
if(_c3c){
CKEDITOR.replace(_c3c,{customConfig:_c3d});
}
});
}else{
var _c3a=_c39[0];
var _c3b=_c39[1];
}
var desc=dojo.query(" > .header-wrapper p ",_c2e.childNodes[0])[0];
if(typeof desc!="undefined"){
_c38=true;
}
if(_b51.contains(_c2e,"init-collapsed")){
_b51.remove(_c2e,"init-collapsed");
_b53.set(_c3a,"display","none");
}
if(!_c3a||_c3a.inAnimation){
return;
}
require(["dojo/fx"],function(fx){
var _c3f={node:_c3a,duration:600,onBegin:function(){
_c3a.inAnimation=true;
_b51.remove(_c2e,"is-collapsed");
_b51.add(_c2e,"is-uncollapsed");
_b52.set(_c30,"aria-expanded","true");
dojo.stopEvent(_c2f);
},onEnd:function(){
_c3a.inAnimation=false;
}};
var _c40={node:_c3a,duration:600,onBegin:function(){
_c3a.inAnimation=true;
_b51.remove(_c2e,"is-uncollapsed");
_b51.add(_c2e,"is-collapsed");
_b52.set(_c30,"aria-expanded","false");
dojo.stopEvent(_c2f);
},onEnd:function(){
_c3a.inAnimation=false;
}};
if(_c3b.hasChildNodes()){
var _c41={node:_c3b,duration:600};
var _c42={node:_c3b,duration:600};
}
if(_c38){
var _c43={node:desc,duration:100};
var _c44={node:desc,duration:100,delay:500};
}
if(_b51.contains(_c2e,"is-collapsed")){
if(typeof _c43!="undefined"){
fx.wipeIn(_c43).play();
}
fx.wipeIn(_c3f).play();
if(typeof _c41!="undefined"){
fx.wipeIn(_c41).play();
}
if(img){
img.src=_c34;
}
if(_c31){
_c31.src=_c36;
}
}else{
if(_b51.contains(_c2e,"is-uncollapsed")){
if(typeof _c42!="undefined"){
fx.wipeOut(_c42).play();
}
fx.wipeOut(_c40).play();
if(typeof _c44!="undefined"){
fx.wipeOut(_c44).play();
}
if(img){
img.src=_c35;
}
if(_c31){
_c31.src=_c37;
}
}else{
_b57.log("The cluster does not have a class name indicating"+"its collapsed/uncollapsed state");
}
}
});
},disableClusterToggle:function(node){
dojo.addOnLoad(function(){
node=dom.byId(node);
var body=dojo.body();
while(node&&node!=body){
if(_b51.contains(node,"is-collapsed")||_b51.contains(node,"is-uncollapsed")){
_b51.remove(node,"is-collapsed");
_b51.remove(node,"is-uncollapsed");
_b52.remove(dojo.query("BUTTON.grouptoggleArrow",node)[0],"onclick");
}
node=node.parentNode;
}
});
},addClassToPositionQuestionaireStatic:function(){
var body=document.getElementsByTagName("body")[0];
body.className=body.className+" static-print-position";
},openUserPrefsEditor:function(_c45){
_c45=dojo.fixEvent(_c45);
var _c46=_c45.target;
while(_c46&&_c46.tagName!="A"){
_c46=_c46.parentNode;
}
var _c47={location:{href:_c46.href}};
var rtc=new curam.util.RuntimeContext(_c47);
var href=curam.util.setRpu("user-locale-selector.jspx",rtc);
openModalDialog({href:href},"width=500,height=300",200,150,false);
return false;
},calendarOpenModalDialog:function(_c48,_c49){
dojo.stopEvent(_c48);
curam.util.openModalDialog(_c49,"");
},clickMarker:function(){
return true;
}};
for(prop in _b59){
dojo.global[prop]=_b59[prop];
}
return _b59;
});
},"curam/widget/_ComboBoxMenu":function(){
define(["dojo/_base/declare","dojo/dom-attr","curam/util/ResourceBundle","dijit/form/_ComboBoxMenu"],function(_c4a,_c4b,_c4c){
var _c4d=new _c4c("FilteringSelect");
var _c4e=_c4a("curam.widget._ComboBoxMenu",[dijit.form._ComboBoxMenu],{_createOption:function(item,_c4f){
var _c50=this._createMenuItem();
var _c51=_c4f(item);
if(_c51.html){
_c50.innerHTML=_c51.label;
}else{
_c50.appendChild(_c50.ownerDocument.createTextNode(_c51.label));
}
if(_c50.innerHTML==""){
_c4b.set(_c50,"aria-label",_c4d.getProperty("curam.select.option.blank"));
_c4b.set(_c50,"title",_c4d.getProperty("curam.select.option.blank"));
}else{
var _c52=/<[a-zA-Z\/][^>]*>/g;
var _c53=_c50.innerHTML;
_c50.title=_c53.replace(_c52,"");
}
return _c50;
}});
return _c4e;
});
},"dijit/form/_AutoCompleterMixin":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/query","dojo/regexp","dojo/sniff","./DataList","./_TextBoxMixin","./_SearchMixin"],function(_c54,_c55,_c56,keys,lang,_c57,_c58,has,_c59,_c5a,_c5b){
var _c5c=_c55("dijit.form._AutoCompleterMixin",_c5b,{item:null,autoComplete:true,highlightMatch:"first",labelAttr:"",labelType:"text",maxHeight:-1,_stopClickEvents:false,_getCaretPos:function(_c5d){
var pos=0;
if(typeof (_c5d.selectionStart)=="number"){
pos=_c5d.selectionStart;
}else{
if(has("ie")){
var tr=_c5d.ownerDocument.selection.createRange().duplicate();
var ntr=_c5d.createTextRange();
tr.move("character",0);
ntr.move("character",0);
try{
ntr.setEndPoint("EndToEnd",tr);
pos=String(ntr.text).replace(/\r/g,"").length;
}
catch(e){
}
}
}
return pos;
},_setCaretPos:function(_c5e,_c5f){
_c5f=parseInt(_c5f);
_c5a.selectInputText(_c5e,_c5f,_c5f);
},_setDisabledAttr:function(_c60){
this.inherited(arguments);
this.domNode.setAttribute("aria-disabled",_c60?"true":"false");
},_onKey:function(evt){
if(evt.charCode>=32){
return;
}
var key=evt.charCode||evt.keyCode;
if(key==keys.ALT||key==keys.CTRL||key==keys.META||key==keys.SHIFT){
return;
}
var pw=this.dropDown;
var _c61=null;
this._abortQuery();
this.inherited(arguments);
if(evt.altKey||evt.ctrlKey||evt.metaKey){
return;
}
if(this._opened){
_c61=pw.getHighlightedOption();
}
switch(key){
case keys.PAGE_DOWN:
case keys.DOWN_ARROW:
case keys.PAGE_UP:
case keys.UP_ARROW:
if(this._opened){
this._announceOption(_c61);
}
evt.stopPropagation();
evt.preventDefault();
break;
case keys.ENTER:
if(_c61){
if(_c61==pw.nextButton){
this._nextSearch(1);
evt.stopPropagation();
evt.preventDefault();
break;
}else{
if(_c61==pw.previousButton){
this._nextSearch(-1);
evt.stopPropagation();
evt.preventDefault();
break;
}
}
evt.stopPropagation();
evt.preventDefault();
}else{
this._setBlurValue();
this._setCaretPos(this.focusNode,this.focusNode.value.length);
}
case keys.TAB:
var _c62=this.get("displayedValue");
if(pw&&(_c62==pw._messages["previousMessage"]||_c62==pw._messages["nextMessage"])){
break;
}
if(_c61){
this._selectOption(_c61);
}
case keys.ESCAPE:
if(this._opened){
this._lastQuery=null;
this.closeDropDown();
}
break;
}
},_autoCompleteText:function(text){
var fn=this.focusNode;
_c5a.selectInputText(fn,fn.value.length);
var _c63=this.ignoreCase?"toLowerCase":"substr";
if(text[_c63](0).indexOf(this.focusNode.value[_c63](0))==0){
var cpos=this.autoComplete?this._getCaretPos(fn):fn.value.length;
if((cpos+1)>fn.value.length){
fn.value=text;
_c5a.selectInputText(fn,cpos);
}
}else{
fn.value=text;
_c5a.selectInputText(fn);
}
},_openResultList:function(_c64,_c65,_c66){
var _c67=this.dropDown.getHighlightedOption();
this.dropDown.clearResultList();
if(!_c64.length&&_c66.start==0){
this.closeDropDown();
return;
}
this._nextSearch=this.dropDown.onPage=lang.hitch(this,function(_c68){
_c64.nextPage(_c68!==-1);
this.focus();
});
this.dropDown.createOptions(_c64,_c66,lang.hitch(this,"_getMenuLabelFromItem"));
this._showResultList();
if("direction" in _c66){
if(_c66.direction){
this.dropDown.highlightFirstOption();
}else{
if(!_c66.direction){
this.dropDown.highlightLastOption();
}
}
if(_c67){
this._announceOption(this.dropDown.getHighlightedOption());
}
}else{
if(this.autoComplete&&!this._prev_key_backspace&&!/^[*]+$/.test(_c65[this.searchAttr].toString())){
this._announceOption(this.dropDown.containerNode.firstChild.nextSibling);
}
}
},_showResultList:function(){
this.closeDropDown(true);
this.openDropDown();
this.domNode.setAttribute("aria-expanded","true");
},loadDropDown:function(){
this._startSearchAll();
},isLoaded:function(){
return false;
},closeDropDown:function(){
this._abortQuery();
if(this._opened){
this.inherited(arguments);
this.domNode.setAttribute("aria-expanded","false");
}
},_setBlurValue:function(){
var _c69=this.get("displayedValue");
var pw=this.dropDown;
if(pw&&(_c69==pw._messages["previousMessage"]||_c69==pw._messages["nextMessage"])){
this._setValueAttr(this._lastValueReported,true);
}else{
if(typeof this.item=="undefined"){
this.item=null;
this.set("displayedValue",_c69);
}else{
if(this.value!=this._lastValueReported){
this._handleOnChange(this.value,true);
}
this._refreshState();
}
}
this.focusNode.removeAttribute("aria-activedescendant");
},_setItemAttr:function(item,_c6a,_c6b){
var _c6c="";
if(item){
if(!_c6b){
_c6b=this.store._oldAPI?this.store.getValue(item,this.searchAttr):item[this.searchAttr];
}
_c6c=this._getValueField()!=this.searchAttr?this.store.getIdentity(item):_c6b;
}
this.set("value",_c6c,_c6a,_c6b,item);
},_announceOption:function(node){
if(!node){
return;
}
var _c6d;
if(node==this.dropDown.nextButton||node==this.dropDown.previousButton){
_c6d=node.innerHTML;
this.item=undefined;
this.value="";
}else{
var item=this.dropDown.items[node.getAttribute("item")];
_c6d=(this.store._oldAPI?this.store.getValue(item,this.searchAttr):item[this.searchAttr]).toString();
this.set("item",item,false,_c6d);
}
this.focusNode.value=this.focusNode.value.substring(0,this._lastInput.length);
this.focusNode.setAttribute("aria-activedescendant",_c56.get(node,"id"));
this._autoCompleteText(_c6d);
},_selectOption:function(_c6e){
this.closeDropDown();
if(_c6e){
this._announceOption(_c6e);
}
this._setCaretPos(this.focusNode,this.focusNode.value.length);
this._handleOnChange(this.value,true);
this.focusNode.removeAttribute("aria-activedescendant");
},_startSearchAll:function(){
this._startSearch("");
},_startSearchFromInput:function(){
this.item=undefined;
this.inherited(arguments);
},_startSearch:function(key){
if(!this.dropDown){
var _c6f=this.id+"_popup",_c70=lang.isString(this.dropDownClass)?lang.getObject(this.dropDownClass,false):this.dropDownClass;
this.dropDown=new _c70({onChange:lang.hitch(this,this._selectOption),id:_c6f,dir:this.dir,textDir:this.textDir});
}
this._lastInput=key;
this.inherited(arguments);
},_getValueField:function(){
return this.searchAttr;
},postMixInProperties:function(){
this.inherited(arguments);
if(!this.store&&this.srcNodeRef){
var _c71=this.srcNodeRef;
this.store=new _c59({},_c71);
if(!("value" in this.params)){
var item=(this.item=this.store.fetchSelectedItem());
if(item){
var _c72=this._getValueField();
this.value=this.store._oldAPI?this.store.getValue(item,_c72):item[_c72];
}
}
}
},postCreate:function(){
var _c73=_c57("label[for=\""+this.id+"\"]");
if(_c73.length){
if(!_c73[0].id){
_c73[0].id=this.id+"_label";
}
this.domNode.setAttribute("aria-labelledby",_c73[0].id);
}
this.inherited(arguments);
_c54.after(this,"onSearch",lang.hitch(this,"_openResultList"),true);
},_getMenuLabelFromItem:function(item){
var _c74=this.labelFunc(item,this.store),_c75=this.labelType;
if(this.highlightMatch!="none"&&this.labelType=="text"&&this._lastInput){
_c74=this.doHighlight(_c74,this._lastInput);
_c75="html";
}
return {html:_c75=="html",label:_c74};
},doHighlight:function(_c76,find){
var _c77=(this.ignoreCase?"i":"")+(this.highlightMatch=="all"?"g":""),i=this.queryExpr.indexOf("${0}");
find=_c58.escapeString(find);
return this._escapeHtml(_c76.replace(new RegExp((i==0?"^":"")+"("+find+")"+(i==(this.queryExpr.length-4)?"$":""),_c77),"\uffff$1\uffff")).replace(/\uFFFF([^\uFFFF]+)\uFFFF/g,"<span class=\"dijitComboBoxHighlightMatch\">$1</span>");
},_escapeHtml:function(str){
str=String(str).replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
return str;
},reset:function(){
this.item=null;
this.inherited(arguments);
},labelFunc:function(item,_c78){
return (_c78._oldAPI?_c78.getValue(item,this.labelAttr||this.searchAttr):item[this.labelAttr||this.searchAttr]).toString();
},_setValueAttr:function(_c79,_c7a,_c7b,item){
this._set("item",item||null);
if(_c79==null){
_c79="";
}
this.inherited(arguments);
}});
if(has("dojo-bidi")){
_c5c.extend({_setTextDirAttr:function(_c7c){
this.inherited(arguments);
if(this.dropDown){
this.dropDown._set("textDir",_c7c);
}
}});
}
return _c5c;
});
},"dijit/TitlePane":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/fx","dojo/has","dojo/_base/kernel","dojo/keys","./_CssStateMixin","./_TemplatedMixin","./layout/ContentPane","dojo/text!./templates/TitlePane.html","./_base/manager","./a11yclick"],function(_c7d,_c7e,dom,_c7f,_c80,_c81,_c82,has,_c83,keys,_c84,_c85,_c86,_c87,_c88){
var _c89=_c7e("dijit.TitlePane",[_c86,_c85,_c84],{title:"",_setTitleAttr:{node:"titleNode",type:"innerHTML"},open:true,toggleable:true,tabIndex:"0",duration:_c88.defaultDuration,baseClass:"dijitTitlePane",templateString:_c87,doLayout:false,_setTooltipAttr:{node:"focusNode",type:"attribute",attribute:"title"},buildRendering:function(){
this.inherited(arguments);
dom.setSelectable(this.titleNode,false);
},postCreate:function(){
this.inherited(arguments);
if(this.toggleable){
this._trackMouseState(this.titleBarNode,this.baseClass+"Title");
}
var _c8a=this.hideNode,_c8b=this.wipeNode;
this._wipeIn=_c82.wipeIn({node:_c8b,duration:this.duration,beforeBegin:function(){
_c8a.style.display="";
}});
this._wipeOut=_c82.wipeOut({node:_c8b,duration:this.duration,onEnd:function(){
_c8a.style.display="none";
}});
},_setOpenAttr:function(open,_c8c){
_c7d.forEach([this._wipeIn,this._wipeOut],function(_c8d){
if(_c8d&&_c8d.status()=="playing"){
_c8d.stop();
}
});
if(_c8c){
var anim=this[open?"_wipeIn":"_wipeOut"];
anim.play();
}else{
this.hideNode.style.display=this.wipeNode.style.display=open?"":"none";
}
if(this._started){
if(open){
this._onShow();
}else{
this.onHide();
}
}
this.containerNode.setAttribute("aria-hidden",open?"false":"true");
this.focusNode.setAttribute("aria-pressed",open?"true":"false");
this._set("open",open);
this._setCss();
},_setToggleableAttr:function(_c8e){
this.focusNode.setAttribute("role",_c8e?"button":"heading");
if(_c8e){
this.focusNode.setAttribute("aria-controls",this.id+"_pane");
this.focusNode.setAttribute("tabIndex",this.tabIndex);
this.focusNode.setAttribute("aria-pressed",this.open);
}else{
_c7f.remove(this.focusNode,"aria-controls");
_c7f.remove(this.focusNode,"tabIndex");
_c7f.remove(this.focusNode,"aria-pressed");
}
this._set("toggleable",_c8e);
this._setCss();
},_setContentAttr:function(_c8f){
if(!this.open||!this._wipeOut||this._wipeOut.status()=="playing"){
this.inherited(arguments);
}else{
if(this._wipeIn&&this._wipeIn.status()=="playing"){
this._wipeIn.stop();
}
_c81.setMarginBox(this.wipeNode,{h:_c81.getMarginBox(this.wipeNode).h});
this.inherited(arguments);
if(this._wipeIn){
this._wipeIn.play();
}else{
this.hideNode.style.display="";
}
}
},toggle:function(){
this._setOpenAttr(!this.open,true);
},_setCss:function(){
var node=this.titleBarNode||this.focusNode;
var _c90=this._titleBarClass;
this._titleBarClass=this.baseClass+"Title"+(this.toggleable?"":"Fixed")+(this.open?"Open":"Closed");
_c80.replace(node,this._titleBarClass,_c90||"");
_c80.replace(node,this._titleBarClass.replace("TitlePaneTitle",""),(_c90||"").replace("TitlePaneTitle",""));
this.arrowNodeInner.innerHTML=this.open?"-":"+";
},_onTitleKey:function(e){
if(e.keyCode==keys.DOWN_ARROW&&this.open){
this.containerNode.focus();
e.preventDefault();
}
},_onTitleClick:function(){
if(this.toggleable){
this.toggle();
}
},setTitle:function(_c91){
_c83.deprecated("dijit.TitlePane.setTitle() is deprecated.  Use set('title', ...) instead.","","2.0");
this.set("title",_c91);
}});
if(has("dojo-bidi")){
_c89.extend({_setTitleAttr:function(_c92){
this._set("title",_c92);
this.titleNode.innerHTML=_c92;
this.applyTextDir(this.titleNode);
},_setTooltipAttr:function(_c93){
this._set("tooltip",_c93);
if(this.textDir){
_c93=this.enforceTextDirWithUcc(null,_c93);
}
_c7f.set(this.focusNode,"title",_c93);
},_setTextDirAttr:function(_c94){
if(this._created&&this.textDir!=_c94){
this._set("textDir",_c94);
this.set("title",this.title);
this.set("tooltip",this.tooltip);
}
}});
}
return _c89;
});
},"curam/layout/EmptyContentPane":function(){
define(["dojo/_base/declare","dijit/layout/ContentPane"],function(_c95){
var _c96=_c95("curam.layout.EmptyContentPane",dijit.layout.ContentPane,{baseClass:"",_layoutChildren:function(){
},resize:function(){
}});
return _c96;
});
},"dijit/place":function(){
define(["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","./Viewport","./main"],function(_c97,_c98,_c99,_c9a,win,_c9b,_c9c){
function _c9d(node,_c9e,_c9f,_ca0){
var view=_c9b.getEffectiveBox(node.ownerDocument);
if(!node.parentNode||String(node.parentNode.tagName).toLowerCase()!="body"){
win.body(node.ownerDocument).appendChild(node);
}
var best=null;
_c97.some(_c9e,function(_ca1){
var _ca2=_ca1.corner;
var pos=_ca1.pos;
var _ca3=0;
var _ca4={w:{"L":view.l+view.w-pos.x,"R":pos.x-view.l,"M":view.w}[_ca2.charAt(1)],h:{"T":view.t+view.h-pos.y,"B":pos.y-view.t,"M":view.h}[_ca2.charAt(0)]};
var s=node.style;
s.left=s.right="auto";
if(_c9f){
var res=_c9f(node,_ca1.aroundCorner,_ca2,_ca4,_ca0);
_ca3=typeof res=="undefined"?0:res;
}
var _ca5=node.style;
var _ca6=_ca5.display;
var _ca7=_ca5.visibility;
if(_ca5.display=="none"){
_ca5.visibility="hidden";
_ca5.display="";
}
var bb=_c98.position(node);
_ca5.display=_ca6;
_ca5.visibility=_ca7;
var _ca8={"L":pos.x,"R":pos.x-bb.w,"M":Math.max(view.l,Math.min(view.l+view.w,pos.x+(bb.w>>1))-bb.w)}[_ca2.charAt(1)],_ca9={"T":pos.y,"B":pos.y-bb.h,"M":Math.max(view.t,Math.min(view.t+view.h,pos.y+(bb.h>>1))-bb.h)}[_ca2.charAt(0)],_caa=Math.max(view.l,_ca8),_cab=Math.max(view.t,_ca9),endX=Math.min(view.l+view.w,_ca8+bb.w),endY=Math.min(view.t+view.h,_ca9+bb.h),_cac=endX-_caa,_cad=endY-_cab;
_ca3+=(bb.w-_cac)+(bb.h-_cad);
if(best==null||_ca3<best.overflow){
best={corner:_ca2,aroundCorner:_ca1.aroundCorner,x:_caa,y:_cab,w:_cac,h:_cad,overflow:_ca3,spaceAvailable:_ca4};
}
return !_ca3;
});
if(best.overflow&&_c9f){
_c9f(node,best.aroundCorner,best.corner,best.spaceAvailable,_ca0);
}
var top=best.y,side=best.x,body=win.body(node.ownerDocument);
if(/relative|absolute/.test(_c99.get(body,"position"))){
top-=_c99.get(body,"marginTop");
side-=_c99.get(body,"marginLeft");
}
var s=node.style;
s.top=top+"px";
s.left=side+"px";
s.right="auto";
return best;
};
var _cae={"TL":"BR","TR":"BL","BL":"TR","BR":"TL"};
var _caf={at:function(node,pos,_cb0,_cb1,_cb2){
var _cb3=_c97.map(_cb0,function(_cb4){
var c={corner:_cb4,aroundCorner:_cae[_cb4],pos:{x:pos.x,y:pos.y}};
if(_cb1){
c.pos.x+=_cb4.charAt(1)=="L"?_cb1.x:-_cb1.x;
c.pos.y+=_cb4.charAt(0)=="T"?_cb1.y:-_cb1.y;
}
return c;
});
return _c9d(node,_cb3,_cb2);
},around:function(node,_cb5,_cb6,_cb7,_cb8){
var _cb9;
if(typeof _cb5=="string"||"offsetWidth" in _cb5||"ownerSVGElement" in _cb5){
_cb9=_c98.position(_cb5,true);
if(/^(above|below)/.test(_cb6[0])){
var _cba=_c98.getBorderExtents(_cb5),_cbb=_cb5.firstChild?_c98.getBorderExtents(_cb5.firstChild):{t:0,l:0,b:0,r:0},_cbc=_c98.getBorderExtents(node),_cbd=node.firstChild?_c98.getBorderExtents(node.firstChild):{t:0,l:0,b:0,r:0};
_cb9.y+=Math.min(_cba.t+_cbb.t,_cbc.t+_cbd.t);
_cb9.h-=Math.min(_cba.t+_cbb.t,_cbc.t+_cbd.t)+Math.min(_cba.b+_cbb.b,_cbc.b+_cbd.b);
}
}else{
_cb9=_cb5;
}
if(_cb5.parentNode){
var _cbe=_c99.getComputedStyle(_cb5).position=="absolute";
var _cbf=_cb5.parentNode;
while(_cbf&&_cbf.nodeType==1&&_cbf.nodeName!="BODY"){
var _cc0=_c98.position(_cbf,true),pcs=_c99.getComputedStyle(_cbf);
if(/relative|absolute/.test(pcs.position)){
_cbe=false;
}
if(!_cbe&&/hidden|auto|scroll/.test(pcs.overflow)){
var _cc1=Math.min(_cb9.y+_cb9.h,_cc0.y+_cc0.h);
var _cc2=Math.min(_cb9.x+_cb9.w,_cc0.x+_cc0.w);
_cb9.x=Math.max(_cb9.x,_cc0.x);
_cb9.y=Math.max(_cb9.y,_cc0.y);
_cb9.h=_cc1-_cb9.y;
_cb9.w=_cc2-_cb9.x;
}
if(pcs.position=="absolute"){
_cbe=true;
}
_cbf=_cbf.parentNode;
}
}
var x=_cb9.x,y=_cb9.y,_cc3="w" in _cb9?_cb9.w:(_cb9.w=_cb9.width),_cc4="h" in _cb9?_cb9.h:(_c9a.deprecated("place.around: dijit/place.__Rectangle: { x:"+x+", y:"+y+", height:"+_cb9.height+", width:"+_cc3+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_cb9.height+", w:"+_cc3+" }","","2.0"),_cb9.h=_cb9.height);
var _cc5=[];
function push(_cc6,_cc7){
_cc5.push({aroundCorner:_cc6,corner:_cc7,pos:{x:{"L":x,"R":x+_cc3,"M":x+(_cc3>>1)}[_cc6.charAt(1)],y:{"T":y,"B":y+_cc4,"M":y+(_cc4>>1)}[_cc6.charAt(0)]}});
};
_c97.forEach(_cb6,function(pos){
var ltr=_cb7;
switch(pos){
case "above-centered":
push("TM","BM");
break;
case "below-centered":
push("BM","TM");
break;
case "after-centered":
ltr=!ltr;
case "before-centered":
push(ltr?"ML":"MR",ltr?"MR":"ML");
break;
case "after":
ltr=!ltr;
case "before":
push(ltr?"TL":"TR",ltr?"TR":"TL");
push(ltr?"BL":"BR",ltr?"BR":"BL");
break;
case "below-alt":
ltr=!ltr;
case "below":
push(ltr?"BL":"BR",ltr?"TL":"TR");
push(ltr?"BR":"BL",ltr?"TR":"TL");
break;
case "above-alt":
ltr=!ltr;
case "above":
push(ltr?"TL":"TR",ltr?"BL":"BR");
push(ltr?"TR":"TL",ltr?"BR":"BL");
break;
default:
push(pos.aroundCorner,pos.corner);
}
});
var _cc8=_c9d(node,_cc5,_cb8,{w:_cc3,h:_cc4});
_cc8.aroundNodePos=_cb9;
return _cc8;
}};
return _c9c.place=_caf;
});
},"dijit/form/ComboBox":function(){
define(["dojo/_base/declare","./ValidationTextBox","./ComboBoxMixin"],function(_cc9,_cca,_ccb){
return _cc9("dijit.form.ComboBox",[_cca,_ccb],{});
});
},"dijit/layout/_LayoutWidget":function(){
define(["dojo/_base/lang","../_Widget","../_Container","../_Contained","../Viewport","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style"],function(lang,_ccc,_ccd,_cce,_ccf,_cd0,_cd1,_cd2,_cd3){
return _cd0("dijit.layout._LayoutWidget",[_ccc,_ccd,_cce],{baseClass:"dijitLayoutContainer",isLayoutContainer:true,_setTitleAttr:null,buildRendering:function(){
this.inherited(arguments);
_cd1.add(this.domNode,"dijitContainer");
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
var _cd4=this.getParent&&this.getParent();
if(!(_cd4&&_cd4.isLayoutContainer)){
this.resize();
this.own(_ccf.on("resize",lang.hitch(this,"resize")));
}
},resize:function(_cd5,_cd6){
var node=this.domNode;
if(_cd5){
_cd2.setMarginBox(node,_cd5);
}
var mb=_cd6||{};
lang.mixin(mb,_cd5||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_cd2.getMarginBox(node),mb);
}
var cs=_cd3.getComputedStyle(node);
var me=_cd2.getMarginExtents(node,cs);
var be=_cd2.getBorderExtents(node,cs);
var bb=(this._borderBox={w:mb.w-(me.w+be.w),h:mb.h-(me.h+be.h)});
var pe=_cd2.getPadExtents(node,cs);
this._contentBox={l:_cd3.toPixelValue(node,cs.paddingLeft),t:_cd3.toPixelValue(node,cs.paddingTop),w:bb.w-pe.w,h:bb.h-pe.h};
this.layout();
},layout:function(){
},_setupChild:function(_cd7){
var cls=this.baseClass+"-child "+(_cd7.baseClass?this.baseClass+"-"+_cd7.baseClass:"");
_cd1.add(_cd7.domNode,cls);
},addChild:function(_cd8,_cd9){
this.inherited(arguments);
if(this._started){
this._setupChild(_cd8);
}
},removeChild:function(_cda){
var cls=this.baseClass+"-child"+(_cda.baseClass?" "+this.baseClass+"-"+_cda.baseClass:"");
_cd1.remove(_cda.domNode,cls);
this.inherited(arguments);
}});
});
},"dojo/cldr/supplemental":function(){
define(["../_base/lang","../i18n"],function(lang,i18n){
var _cdb={};
lang.setObject("dojo.cldr.supplemental",_cdb);
_cdb.getFirstDayOfWeek=function(_cdc){
var _cdd={bd:5,mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,iq:6,ir:6,jo:6,kw:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,sy:6,ye:6,ag:0,ar:0,as:0,au:0,br:0,bs:0,bt:0,bw:0,by:0,bz:0,ca:0,cn:0,co:0,dm:0,"do":0,et:0,gt:0,gu:0,hk:0,hn:0,id:0,ie:0,il:0,"in":0,jm:0,jp:0,ke:0,kh:0,kr:0,la:0,mh:0,mm:0,mo:0,mt:0,mx:0,mz:0,ni:0,np:0,nz:0,pa:0,pe:0,ph:0,pk:0,pr:0,py:0,sg:0,sv:0,th:0,tn:0,tt:0,tw:0,um:0,us:0,ve:0,vi:0,ws:0,za:0,zw:0};
var _cde=_cdb._region(_cdc);
var dow=_cdd[_cde];
return (dow===undefined)?1:dow;
};
_cdb._region=function(_cdf){
_cdf=i18n.normalizeLocale(_cdf);
var tags=_cdf.split("-");
var _ce0=tags[1];
if(!_ce0){
_ce0={aa:"et",ab:"ge",af:"za",ak:"gh",am:"et",ar:"eg",as:"in",av:"ru",ay:"bo",az:"az",ba:"ru",be:"by",bg:"bg",bi:"vu",bm:"ml",bn:"bd",bo:"cn",br:"fr",bs:"ba",ca:"es",ce:"ru",ch:"gu",co:"fr",cr:"ca",cs:"cz",cv:"ru",cy:"gb",da:"dk",de:"de",dv:"mv",dz:"bt",ee:"gh",el:"gr",en:"us",es:"es",et:"ee",eu:"es",fa:"ir",ff:"sn",fi:"fi",fj:"fj",fo:"fo",fr:"fr",fy:"nl",ga:"ie",gd:"gb",gl:"es",gn:"py",gu:"in",gv:"gb",ha:"ng",he:"il",hi:"in",ho:"pg",hr:"hr",ht:"ht",hu:"hu",hy:"am",ia:"fr",id:"id",ig:"ng",ii:"cn",ik:"us","in":"id",is:"is",it:"it",iu:"ca",iw:"il",ja:"jp",ji:"ua",jv:"id",jw:"id",ka:"ge",kg:"cd",ki:"ke",kj:"na",kk:"kz",kl:"gl",km:"kh",kn:"in",ko:"kr",ks:"in",ku:"tr",kv:"ru",kw:"gb",ky:"kg",la:"va",lb:"lu",lg:"ug",li:"nl",ln:"cd",lo:"la",lt:"lt",lu:"cd",lv:"lv",mg:"mg",mh:"mh",mi:"nz",mk:"mk",ml:"in",mn:"mn",mo:"ro",mr:"in",ms:"my",mt:"mt",my:"mm",na:"nr",nb:"no",nd:"zw",ne:"np",ng:"na",nl:"nl",nn:"no",no:"no",nr:"za",nv:"us",ny:"mw",oc:"fr",om:"et",or:"in",os:"ge",pa:"in",pl:"pl",ps:"af",pt:"br",qu:"pe",rm:"ch",rn:"bi",ro:"ro",ru:"ru",rw:"rw",sa:"in",sd:"in",se:"no",sg:"cf",si:"lk",sk:"sk",sl:"si",sm:"ws",sn:"zw",so:"so",sq:"al",sr:"rs",ss:"za",st:"za",su:"id",sv:"se",sw:"tz",ta:"in",te:"in",tg:"tj",th:"th",ti:"et",tk:"tm",tl:"ph",tn:"za",to:"to",tr:"tr",ts:"za",tt:"ru",ty:"pf",ug:"cn",uk:"ua",ur:"pk",uz:"uz",ve:"za",vi:"vn",wa:"be",wo:"sn",xh:"za",yi:"il",yo:"ng",za:"cn",zh:"cn",zu:"za",ace:"id",ady:"ru",agq:"cm",alt:"ru",amo:"ng",asa:"tz",ast:"es",awa:"in",bal:"pk",ban:"id",bas:"cm",bax:"cm",bbc:"id",bem:"zm",bez:"tz",bfq:"in",bft:"pk",bfy:"in",bhb:"in",bho:"in",bik:"ph",bin:"ng",bjj:"in",bku:"ph",bqv:"ci",bra:"in",brx:"in",bss:"cm",btv:"pk",bua:"ru",buc:"yt",bug:"id",bya:"id",byn:"er",cch:"ng",ccp:"in",ceb:"ph",cgg:"ug",chk:"fm",chm:"ru",chp:"ca",chr:"us",cja:"kh",cjm:"vn",ckb:"iq",crk:"ca",csb:"pl",dar:"ru",dav:"ke",den:"ca",dgr:"ca",dje:"ne",doi:"in",dsb:"de",dua:"cm",dyo:"sn",dyu:"bf",ebu:"ke",efi:"ng",ewo:"cm",fan:"gq",fil:"ph",fon:"bj",fur:"it",gaa:"gh",gag:"md",gbm:"in",gcr:"gf",gez:"et",gil:"ki",gon:"in",gor:"id",grt:"in",gsw:"ch",guz:"ke",gwi:"ca",haw:"us",hil:"ph",hne:"in",hnn:"ph",hoc:"in",hoj:"in",ibb:"ng",ilo:"ph",inh:"ru",jgo:"cm",jmc:"tz",kaa:"uz",kab:"dz",kaj:"ng",kam:"ke",kbd:"ru",kcg:"ng",kde:"tz",kdt:"th",kea:"cv",ken:"cm",kfo:"ci",kfr:"in",kha:"in",khb:"cn",khq:"ml",kht:"in",kkj:"cm",kln:"ke",kmb:"ao",koi:"ru",kok:"in",kos:"fm",kpe:"lr",krc:"ru",kri:"sl",krl:"ru",kru:"in",ksb:"tz",ksf:"cm",ksh:"de",kum:"ru",lag:"tz",lah:"pk",lbe:"ru",lcp:"cn",lep:"in",lez:"ru",lif:"np",lis:"cn",lki:"ir",lmn:"in",lol:"cd",lua:"cd",luo:"ke",luy:"ke",lwl:"th",mad:"id",mag:"in",mai:"in",mak:"id",man:"gn",mas:"ke",mdf:"ru",mdh:"ph",mdr:"id",men:"sl",mer:"ke",mfe:"mu",mgh:"mz",mgo:"cm",min:"id",mni:"in",mnk:"gm",mnw:"mm",mos:"bf",mua:"cm",mwr:"in",myv:"ru",nap:"it",naq:"na",nds:"de","new":"np",niu:"nu",nmg:"cm",nnh:"cm",nod:"th",nso:"za",nus:"sd",nym:"tz",nyn:"ug",pag:"ph",pam:"ph",pap:"bq",pau:"pw",pon:"fm",prd:"ir",raj:"in",rcf:"re",rej:"id",rjs:"np",rkt:"in",rof:"tz",rwk:"tz",saf:"gh",sah:"ru",saq:"ke",sas:"id",sat:"in",saz:"in",sbp:"tz",scn:"it",sco:"gb",sdh:"ir",seh:"mz",ses:"ml",shi:"ma",shn:"mm",sid:"et",sma:"se",smj:"se",smn:"fi",sms:"fi",snk:"ml",srn:"sr",srr:"sn",ssy:"er",suk:"tz",sus:"gn",swb:"yt",swc:"cd",syl:"bd",syr:"sy",tbw:"ph",tcy:"in",tdd:"cn",tem:"sl",teo:"ug",tet:"tl",tig:"er",tiv:"ng",tkl:"tk",tmh:"ne",tpi:"pg",trv:"tw",tsg:"ph",tts:"th",tum:"mw",tvl:"tv",twq:"ne",tyv:"ru",tzm:"ma",udm:"ru",uli:"fm",umb:"ao",unr:"in",unx:"in",vai:"lr",vun:"tz",wae:"ch",wal:"et",war:"ph",xog:"ug",xsr:"np",yao:"mz",yap:"fm",yav:"cm",zza:"tr"}[tags[0]];
}else{
if(_ce0.length==4){
_ce0=tags[2];
}
}
return _ce0;
};
_cdb.getWeekend=function(_ce1){
var _ce2={"in":0,af:4,dz:4,ir:4,om:4,sa:4,ye:4,ae:5,bh:5,eg:5,il:5,iq:5,jo:5,kw:5,ly:5,ma:5,qa:5,sd:5,sy:5,tn:5},_ce3={af:5,dz:5,ir:5,om:5,sa:5,ye:5,ae:6,bh:5,eg:6,il:6,iq:6,jo:6,kw:6,ly:6,ma:6,qa:6,sd:6,sy:6,tn:6},_ce4=_cdb._region(_ce1),_ce5=_ce2[_ce4],end=_ce3[_ce4];
if(_ce5===undefined){
_ce5=6;
}
if(end===undefined){
end=0;
}
return {start:_ce5,end:end};
};
return _cdb;
});
},"dijit/layout/_ContentPaneResizeMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang","dojo/query","../registry","../Viewport","./utils"],function(_ce6,_ce7,_ce8,_ce9,_cea,lang,_ceb,_cec,_ced,_cee){
return _ce7("dijit.layout._ContentPaneResizeMixin",null,{doLayout:true,isLayoutContainer:true,startup:function(){
if(this._started){
return;
}
var _cef=this.getParent();
this._childOfLayoutWidget=_cef&&_cef.isLayoutContainer;
this._needLayout=!this._childOfLayoutWidget;
this.inherited(arguments);
if(this._isShown()){
this._onShow();
}
if(!this._childOfLayoutWidget){
this.own(_ced.on("resize",lang.hitch(this,"resize")));
}
},_checkIfSingleChild:function(){
if(!this.doLayout){
return;
}
var _cf0=[],_cf1=false;
_ceb("> *",this.containerNode).some(function(node){
var _cf2=_cec.byNode(node);
if(_cf2&&_cf2.resize){
_cf0.push(_cf2);
}else{
if(!/script|link|style/i.test(node.nodeName)&&node.offsetHeight){
_cf1=true;
}
}
});
this._singleChild=_cf0.length==1&&!_cf1?_cf0[0]:null;
_ce8.toggle(this.containerNode,this.baseClass+"SingleChild",!!this._singleChild);
},resize:function(_cf3,_cf4){
this._resizeCalled=true;
this._scheduleLayout(_cf3,_cf4);
},_scheduleLayout:function(_cf5,_cf6){
if(this._isShown()){
this._layout(_cf5,_cf6);
}else{
this._needLayout=true;
this._changeSize=_cf5;
this._resultSize=_cf6;
}
},_layout:function(_cf7,_cf8){
delete this._needLayout;
if(!this._wasShown&&this.open!==false){
this._onShow();
}
if(_cf7){
_ce9.setMarginBox(this.domNode,_cf7);
}
var cn=this.containerNode;
if(cn===this.domNode){
var mb=_cf8||{};
lang.mixin(mb,_cf7||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_ce9.getMarginBox(cn),mb);
}
this._contentBox=_cee.marginBox2contentBox(cn,mb);
}else{
this._contentBox=_ce9.getContentBox(cn);
}
this._layoutChildren();
},_layoutChildren:function(){
this._checkIfSingleChild();
if(this._singleChild&&this._singleChild.resize){
var cb=this._contentBox||_ce9.getContentBox(this.containerNode);
this._singleChild.resize({w:cb.w,h:cb.h});
}else{
var _cf9=this.getChildren(),_cfa,i=0;
while(_cfa=_cf9[i++]){
if(_cfa.resize){
_cfa.resize();
}
}
}
},_isShown:function(){
if(this._childOfLayoutWidget){
if(this._resizeCalled&&"open" in this){
return this.open;
}
return this._resizeCalled;
}else{
if("open" in this){
return this.open;
}else{
var node=this.domNode,_cfb=this.domNode.parentNode;
return (node.style.display!="none")&&(node.style.visibility!="hidden")&&!_ce8.contains(node,"dijitHidden")&&_cfb&&_cfb.style&&(_cfb.style.display!="none");
}
}
},_onShow:function(){
this._wasShown=true;
if(this._needLayout){
this._layout(this._changeSize,this._resultSize);
}
this.inherited(arguments);
}});
});
},"curam/util/DatePicker":function(){
define(["dojo/_base/declare"],function(_cfc){
var _cfd=_cfc("curam.util.DatePicker",null,{constructor:function(){
},setDate:function(_cfe,_cff){
if(_cfe){
var _d00=_cfe.lastIndexOf("_wrapper")!=-1?_cfe.substring(0,_cfe.lastIndexOf("_wrapper")):_cfe;
curam.util.getTopmostWindow().dojo.publish("/curam/datePicker/setDate",[_d00,_cff]);
}
}});
return _cfd;
});
},"dijit/form/_ButtonMixin":function(){
define(["dojo/_base/declare","dojo/dom","dojo/has","../registry"],function(_d01,dom,has,_d02){
var _d03=_d01("dijit.form._ButtonMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{label:"",type:"button",__onClick:function(e){
e.stopPropagation();
e.preventDefault();
if(!this.disabled){
this.valueNode.click(e);
}
return false;
},_onClick:function(e){
if(this.disabled){
e.stopPropagation();
e.preventDefault();
return false;
}
if(this.onClick(e)===false){
e.preventDefault();
}
var _d04=e.defaultPrevented;
if(!_d04&&this.type=="submit"&&!(this.valueNode||this.focusNode).form){
for(var node=this.domNode;node.parentNode;node=node.parentNode){
var _d05=_d02.byNode(node);
if(_d05&&typeof _d05._onSubmit=="function"){
_d05._onSubmit(e);
e.preventDefault();
_d04=true;
break;
}
}
}
return !_d04;
},postCreate:function(){
this.inherited(arguments);
dom.setSelectable(this.focusNode,false);
},onClick:function(){
return true;
},_setLabelAttr:function(_d06){
this._set("label",_d06);
var _d07=this.containerNode||this.focusNode;
_d07.innerHTML=_d06;
}});
if(has("dojo-bidi")){
_d03=_d01("dijit.form._ButtonMixin",_d03,{_setLabelAttr:function(){
this.inherited(arguments);
var _d08=this.containerNode||this.focusNode;
this.applyTextDir(_d08);
}});
}
return _d03;
});
},"curam/pagination/StateController":function(){
define(["dojo/_base/declare","curam/pagination","curam/debug"],function(_d09){
var _d0a=_d09("curam.pagination.StateController",null,{pageSize:undefined,currentPage:0,_listModel:undefined,_gui:undefined,constructor:function(_d0b,gui){
this.pageSize=curam.pagination.defaultPageSize;
this._listModel=_d0b;
this.pageSize=curam.pagination.defaultPageSize;
this._gui=gui;
var _d0c={};
_d0c.pageSizeOptions=[15,30,45];
_d0c.pageSizeOptions.contains=function(val){
for(var i=0;i<_d0c.pageSizeOptions.length;i++){
if(_d0c.pageSizeOptions[i]==val){
return true;
}
}
return false;
};
if(!_d0c.pageSizeOptions.contains(curam.pagination.defaultPageSize)){
_d0c.pageSizeOptions.push(curam.pagination.defaultPageSize);
_d0c.pageSizeOptions.sort(function(a,b){
return a-b;
});
}
_d0c.currentPageSize=this.pageSize;
_d0c.directLinkRangeWidth=3;
_d0c.lastPage=this._getLastPageNumber();
this._gui.updateState(_d0c);
var _d0d={};
_d0d.first=dojo.hitch(this,this.gotoFirst);
_d0d.last=dojo.hitch(this,this.gotoLast);
_d0d.previous=dojo.hitch(this,this.gotoPrevious);
_d0d.next=dojo.hitch(this,this.gotoNext);
_d0d.page=dojo.hitch(this,this.gotoPage);
_d0d.pageSize=dojo.hitch(this,this.changePageSize);
this._gui.setHandlers(_d0d);
},reset:function(){
this._listModel.hideRange(1,this._listModel.getRowCount());
this.currentPage=0;
this._gui.reset();
this.gotoFirst();
},gotoFirst:function(){
if(this.currentPage!=1){
this.gotoPage(1);
}
},gotoLast:function(){
var _d0e=this._getLastPageNumber();
if(this.currentPage!=_d0e){
this.gotoPage(_d0e);
}
},gotoPrevious:function(){
if(this.currentPage>1){
this.gotoPage(this.currentPage-1);
}
},gotoNext:function(){
curam.debug.log("curam.pagination.StateController.gotoNext");
var _d0f=this._getLastPageNumber();
if(this.currentPage<_d0f){
this.gotoPage(this.currentPage+1);
}
},gotoPage:function(_d10){
curam.debug.log("curam.pagination.StateController.gotoPage: ",_d10);
if(this.currentPage!=0){
this._listModel.hideRange(this._calcRangeStart(this.currentPage),this._calcRangeEnd(this.currentPage));
}
this._listModel.showRange(this._calcRangeStart(_d10),this._calcRangeEnd(_d10));
this.currentPage=_d10;
this._updateGui();
},changePageSize:function(_d11){
curam.debug.log("curam.pagination.StateController.changePageSize: ",_d11);
this.pageSize=_d11;
var _d12={};
_d12.currentPageSize=_d11;
_d12.lastPage=this._getLastPageNumber();
this._gui.updateState(_d12);
dojo.publish("curam/update/readings/pagination",[this._listModel.getId(),_d11]);
this.reset();
},_calcRangeStart:function(_d13){
return (_d13*this.pageSize)-this.pageSize+1;
},_calcRangeEnd:function(_d14){
if(_d14!=this._getLastPageNumber()){
return _d14*this.pageSize;
}else{
return this._listModel.getRowCount();
}
},_getLastPageNumber:function(){
var _d15=this._listModel.getRowCount();
var mod=_d15%this.pageSize;
return ((_d15-mod)/this.pageSize)+(mod>0?1:0);
},_updateGui:function(){
var _d16={};
_d16.first=this.currentPage>1;
_d16.previous=_d16.first;
_d16.next=this.currentPage<this._getLastPageNumber();
_d16.last=_d16.next;
_d16.currentPage=this.currentPage;
_d16.rowInfo=[this._calcRangeStart(this.currentPage),this._calcRangeEnd(this.currentPage),this._listModel.getRowCount()];
this._gui.updateState(_d16);
}});
return _d0a;
});
},"curam/util/TabActionsMenu":function(){
define(["dijit/registry","dojo/query","curam/inspection/Layer","curam/tab","curam/debug","curam/define","curam/util","curam/util/Refresh"],function(_d17,_d18,_d19,tab,_d1a){
curam.define.singleton("curam.util.TabActionsMenu",{_tabMenuStates:{},maxNumToDisplayInline:2,inlineMenuConfig:{contentPanelID:"",numItemsDisplayedInline:"",inlineItemsDisplayed:[],numOverflowItemsDisplayed:null},classNameHidden:"bx--btn-hidden",getRefreshParams:function(_d1b){
curam.debug.log("curam.util.TabActionsMenu.getRefreshParams(%s)",_d1b);
if(!curam.util.TabActionsMenu.dynamicMenuBarData[_d1b]){
curam.debug.log(_d1a.getProperty("curam.util.TabActionsMenu.no.dynamic"));
return null;
}
var _d1c="menuId="+curam.util.TabActionsMenu.dynamicMenuBarData[_d1b].menuBarId;
_d1c+="&menuItemIds="+curam.util.toCommaSeparatedList(curam.util.TabActionsMenu.dynamicMenuBarData[_d1b].dynamicMenuItemIds);
_d1c+="&menuLoaders="+curam.util.toCommaSeparatedList(curam.util.TabActionsMenu.dynamicMenuBarData[_d1b].dynamicMenuLoaders);
_d1c+="&menuPageParameters="+curam.util.TabActionsMenu.dynamicMenuBarData[_d1b].pageParameters;
return _d1c;
},updateMenuItemStates:function(_d1d,data){
var _d1e=data.menuData;
for(var i=0;i<_d1e.itemStates.length;i++){
var _d1f=_d1e.itemStates[i];
dojo.publish("/curam/tabactions/menuitemstate",[_d1d,_d1f.id,_d1f.enabled,_d1f.visible]);
}
curam.util.TabActionsMenu.manageInlineTabMenuStates(_d1d);
var _d20=function(){
for(var i=0;i<_d1e.itemStates.length;i++){
curam.util.TabActionsMenu.updateMenuItemState(_d1e.itemStates[i],_d1d);
}
};
if(curam.util.TabActionsMenu._isMenuCreated(_d1d)){
_d20();
}else{
var _d21=curam.util.getTopmostWindow();
var _d22=_d21.dojo.subscribe("/curam/menu/created",this,function(_d23){
_d1a.log("Received /curam/menu/created "+_d1a.getProperty("curam.util.ExpandableLists.load.for"),_d23);
if(_d23==_d1d){
_d1a.log(_d1a.getProperty("curam.util.TabActionsMenu.match"));
curam.util.TabActionsMenu._tabMenuStates[_d23]=true;
_d20();
_d21.dojo.unsubscribe(_d22);
}
});
curam.tab.unsubscribeOnTabClose(_d22,_d1d);
}
},_isMenuCreated:function(_d24){
return curam.util.TabActionsMenu._tabMenuStates[_d24]==true;
},updateMenuItemState:function(_d25,_d26){
var _d27=_d17.byId("menuItem_"+_d26+"_"+_d25.id);
var _d28=_d27!=null?_d27:_d17.byId("overflowItem_"+_d26+"_"+_d25.id);
if(_d28!=null){
_d28.disabled=!_d25.enabled;
curam.util.swapState(_d28.domNode,_d25.enabled,"enabled","disabled");
curam.util.swapState(_d28.domNode,_d25.visible,"visible","hidden");
if(_d28.disabled){
_d28.domNode.setAttribute("aria-disabled","true");
}
}
},setupHandlers:function(_d29){
curam.util.Refresh.setMenuBarCallbacks(curam.util.TabActionsMenu.updateMenuItemStates,curam.util.TabActionsMenu.getRefreshParams);
},handleOnClick:function(url,_d2a){
if(_d2a){
curam.tab.getTabController().handleDownLoadClick(url);
}else{
curam.tab.getTabController().handleLinkClick(url);
}
},handleOnClickModal:function(url,_d2b){
var _d2c={dialogOptions:_d2b};
curam.tab.getTabController().handleLinkClick(url,_d2c);
},updateInlineTabMenuState:function(){
dojo.subscribe("/curam/tabactions/menuitemstate",this,function(_d2d,id,_d2e,_d2f){
var _d30="inlinedItem_"+_d2d+"_"+id;
var _d31=document.getElementById("inlinedItem_"+_d2d+"_"+id);
if(_d31){
if(_d2e){
_d1a.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - ENABLING inlined item: "+_d30);
if(_d31.classList.contains("disabled")){
_d31.classList.remove("disabled");
}
if(_d31.hasAttribute("disabled")){
_d31.removeAttribute("disabled");
}
}else{
_d1a.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - DISABLING inlined item: "+_d30);
if(!_d31.classList.contains("disabled")){
_d31.classList.add("disabled");
}
_d31.setAttribute("disabled","");
}
if(_d2f){
_d1a.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - DISPLAYING inlined item: "+_d30);
if(_d31.classList.contains(curam.util.TabActionsMenu.classNameHidden)){
_d31.classList.remove(curam.util.TabActionsMenu.classNameHidden);
}
}else{
_d1a.log("curam.util.TabActionsMenu.updateInlineTabMenuState() - HIDING inlined item: "+_d30);
if(!_d31.classList.contains(curam.util.TabActionsMenu.classNameHidden)){
_d31.classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}
}
});
},_getTabMenuItemsDisplayedInline:function(_d32){
return _d18("#"+_d32+" .bx--btn-tab-menu:not(."+curam.util.TabActionsMenu.classNameHidden+")");
},setInlinedTabMenuItemsDisplayed:function(_d33,_d34){
var _d35;
var _d36=curam.util.TabActionsMenu.inlineMenuConfig;
if(_d36&&_d36.contentPanelID!=_d33){
_d35=_d34?_d34:curam.util.TabActionsMenu._getTabMenuItemsDisplayedInline(_d33);
if(_d35&&_d35.length>0){
var _d37={};
_d37.contentPanelID=_d33;
_d37.numItemsDisplayedInline=_d35.length;
var _d38=[];
for(var i=0;i<_d35.length;i++){
var id=_d35[i].id;
_d38.push(id);
}
_d37.inlineItemsDisplayed=_d38;
var _d36=curam.util.TabActionsMenu.inlineMenuConfig;
curam.util.TabActionsMenu.inlineMenuConfig=_d37;
}
}
},hideTabMenuOverflowItems:function(_d39,_d3a){
var _d3b=curam.util.TabActionsMenu.inlineMenuConfig;
if(_d3b&&_d3b.contentPanelID==_d39){
var _d3c=_d18("tr::not(.dijitMenuSeparator):not(.hidden)",_d3a);
var _d3d=_d3b.numOverflowItemsDisplayed;
var _d3e=_d3d&&_d3d!=_d3c.length;
var _d3f=!_d3d||_d3e;
if(_d3c&&_d3c.length>0&&_d3f){
if(_d3e){
var _d40=_d3b.inlineItemsDisplayed;
if(_d40){
for(var x=0;x<_d40.length;x++){
var _d41=_d40[x];
var _d42=_d41.substring(_d41.indexOf(_d39),_d41.length);
var _d43=document.getElementById("overflowItem_"+_d42);
if(_d43&&!_d43.classList.contains("hidden")){
_d1a.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - HIDING oveflow item: "+"overflowItem_"+_d42);
curam.util.swapState(_d43,false,"visible","hidden");
}
}
}
}else{
for(var i=0;i<_d3b.numItemsDisplayedInline;i++){
var _d43=_d3c[i];
if(_d43){
_d1a.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - dynamic update recored");
_d1a.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - HIDING oveflow item: "+"overflowItem_"+_d42);
curam.util.swapState(_d43,false,"visible","hidden");
}
}
}
_d3c=_d18("tr::not(.dijitMenuSeparator):not(.hidden)",_d3a);
_d1a.log("curam.util.TabActionsMenu.hideTabMenuOverflowItems() - setting number of overflow items to be: "+_d3c.length);
curam.util.TabActionsMenu.inlineMenuConfig.numOverflowItemsDisplayed=_d3c.length;
}
}
},manageInlineTabMenuStates:function(_d44){
var _d45=+curam.util.TabActionsMenu.maxNumToDisplayInline;
var _d46=_d45;
curam.util.TabActionsMenu.inlineMenuItemIds=[];
var _d47=curam.util.TabActionsMenu._getTabMenuItemsDisplayedInline(_d44);
var _d48=_d47&&_d47.length>0;
var _d49=[];
if(_d48){
var _d4a=_d47[0].parentNode;
if(_d47.length>_d45){
for(var i=_d45;i<_d47.length;i++){
_d1a.log("curam.util.TabActionsMenu.manageInlineTabMenuStates() - HIDING inlined tab action item: "+_d47[i].id);
_d47[i].classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}else{
_d46=_d47.length;
if(_d46>0){
var _d4b=_d18("img",_d4a);
if(_d4b&&_d4b.length>0){
_d4b[0].classList.add(curam.util.TabActionsMenu.classNameHidden);
}
}
}
_d1a.log("curam.util.TabActionsMenu.manageInlineTabMenuStates() - REMOVING mask for action items on tab menu bar: "+_d4a.id);
_d4a.classList.remove(curam.util.TabActionsMenu.classNameHidden);
for(var i=0;i<_d46;i++){
_d49.push(_d47[i]);
}
curam.util.TabActionsMenu.setInlinedTabMenuItemsDisplayed(_d44,_d49);
}
}});
_d19.register("curam/util/TabActionsMenu",this);
return curam.util.TabActionsMenu;
});
},"curam/util/AutoRecoveryAPI":function(){
define(["dojo/request/xhr","curam/define","dojo/_base/declare","curam/debug"],function(xhr,_d4c,_d4d,_d4e){
var _d4f=_d4d("curam.util.AutoRecoveryAPI",null,{_pageID:"",_pageParams:"",_iFrameID:"",_unsubscribes:[],_unsubscribesFormEvent:[],_subscribeCloseModal:null,_throttle:[],constructor:function(){
this._unset();
this._unsubscribes.push(dojo.subscribe("curam/tab/restoreModal",this,function(){
this._getAutoRecoveryService();
}));
if(!this._subscribeCloseModal){
dojo.subscribe("/curam/dialog/BeforeClose",this._beforeCloseHandler.bind(this));
}
this._throttle={enabled:AUTORECOVERY_THROTTLE_INTERVAL>0,gateOpen:true,queuedPost:[]};
this.tabSessionManager=new curam.tab.TabSessionManager();
},initialize:function(wRef){
var _d50=wRef.frameElement.id.replace("iframe-","");
if(!this._iFrameID){
this._iFrameID=_d50;
}
var _d51=this._iFrameID===_d50;
var _d52=wRef.location.href.includes("Action.do");
var _d53=_d51&&!_d52;
if(_d53){
this._uninstall(this._unsubscribesFormEvent);
var _d54=new curam.ui.PageRequest(wRef.location.href);
this._pageParams=_d54.parameters;
this._pageID=_d54.pageID;
var _d55=this.tabSessionManager._getPrevSelectedTab();
this._updateTabContent(_d55);
if(!_d55.restoreModalInd){
this._createAutorecoveryHandler(_d55);
}else{
this._getAutoRecoveryService(wRef);
}
var _d56=this._registerFormChangeHandler.bind(this);
this._unsubscribesFormEvent.push(dojo.subscribe("curam/util/CuramFormsAPI/formChange",_d56));
}
},_beforeCloseHandler:function(_d57){
if(this._iFrameID===_d57){
this._deleteAutoRecoveryService();
this._unset();
this._unsetSelectedTD();
}
},_unsetSelectedTD:function(){
var _d58=this.tabSessionManager._getPrevSelectedTab();
_d58.foregroundTabContent=null;
_d58.restoreModalInd=false;
curam.debug.log("curam.modal.CuramCarbonModal: "+_d4e.getProperty("curam.modal.CuramCarbonModal.foreground.removed"),_d58);
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_d58.toJson();
},_createAutorecoveryHandler:function(_d59){
var _d5a={pageID:this._pageID,params:this._pageParams};
var _d5b="";
var _d5c=false;
this._postAutoRecoveryService(_d5a,_d5b,_d5c,_d59);
},_updateTabContent:function(_d5d){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.foreground.added"),_d5d);
if(!_d5d.foregroundTabContent){
this._updateForegroundTabContent(_d5d);
}else{
if(_d5d.foregroundTabContent.pageID!==this._pageID){
_d5d.restoreModalInd=false;
this._updateForegroundTabContent(_d5d);
}
}
},_updateForegroundTabContent:function(_d5e){
var _d5f={"pageID":this._pageID,"pageParams":this._pageParams};
_d5e.foregroundTabContent=_d5f;
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_d5e.toJson();
},_unset:function(){
this._uninstall(this._unsubscribes,this._unsubscribesFormEvent);
this._pageID="";
this._pageParams="";
this._iFrameID="";
this._unsubscribes=[];
this._unsubscribesFormEvent=[];
},_uninstall:function(_d60,_d61){
_d60&&_d60.forEach(function(hh){
hh.remove();
});
_d61&&_d61.forEach(function(hh){
hh.remove();
});
},_registerFormChangeHandler:function(_d62){
if(this._iFrameID===_d62.frameID){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.register.form.change.handler"));
var _d63=_d62.data;
var _d64={pageID:this._pageID,params:this._pageParams};
var _d65=true;
this._postAutoRecoveryService(_d64,_d63,_d65);
}
},_getSelectedTD:function(_d66,_d67){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.previous.modal.background"));
var _d68="";
if(_d66){
_d68=this.tabSessionManager._getPrevSelectedModal();
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.previous.modal.background.set"));
_d68=!_d67?this.tabSessionManager._getPrevSelectedTab():_d67;
_d68.restoreModalInd=true;
localStorage[curam.tab.TabSessionManager.PREVIOUSLY_SELECTED_MODAL_KEY]=_d68.toJson();
}
return _d68;
},_postAutoRecoveryService:function(_d69,_d6a,_d6b){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle"));
if(this._throttle.enabled===false){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.disabled"));
this._postAutoRecoveryServiceUnthrottled(_d69,_d6a,this._getSelectedTD(_d6b));
return;
}
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.gateStatus")+this._throttle.gateOpen);
var _d6c=this._throttle.gateOpen;
this._throttle.gateOpen=false;
if(arguments.length>0){
this._throttle.queuedPost={foregroundPage:_d69,foregroundPageData:_d6a,backgroundSelectedTD:this._getSelectedTD(_d6b)};
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.storeRequest")+", foregroundPage: "+this._throttle.queuedPost.foregroundPage.pageID+", foregroundPageData: "+this._throttle.queuedPost.foregroundPageData+", backgroundSelectedTD: "+this._throttle.queuedPost.backgroundSelectedTD.tabID);
}
if(_d6c===true){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.post")+", foregroundPage: "+this._throttle.queuedPost.foregroundPage.pageID+", foregroundPageData: "+this._throttle.queuedPost.foregroundPageData+", backgroundSelectedTD: "+this._throttle.queuedPost.backgroundSelectedTD.tabID);
var _d69=this._throttle.queuedPost.foregroundPage;
var _d6a=this._throttle.queuedPost.foregroundPageData;
var _d6d=this._throttle.queuedPost.backgroundSelectedTD;
this._throttle.queuedPost=[];
this._postAutoRecoveryServiceUnthrottled(_d69,_d6a,_d6d);
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.setTimer")+AUTORECOVERY_THROTTLE_INTERVAL);
this._throttle.timeoutID=setTimeout(function(){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.timerElapsed"));
this._throttle.gateOpen=true;
if(Object.keys(this._throttle.queuedPost).length!==0){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.throttle.processQueuedRequest"));
this._postAutoRecoveryService();
}
}.bind(this),AUTORECOVERY_THROTTLE_INTERVAL);
}
},_postAutoRecoveryServiceUnthrottled:function(_d6e,_d6f,_d70){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post"));
var _d71="servlet/autorecovery?command=save";
xhr.post(_d71,{data:{foregroundPage:JSON.stringify(_d6e),foregroundPageData:_d6f,backgroundTD:JSON.stringify(_d70),requestSentTimeStamp:Date.now()}}).then(function(resp){
try{
if(resp!="OK"){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.error"));
return;
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.success"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.catch.error")+" '"+e+"'.");
}
},function(_d72){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.post.function.error")+" '"+_d72+"'.");
});
},_deleteAutoRecoveryService:function(){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.delete"));
clearTimeout(this._throttle.timeoutID);
var _d73="servlet/autorecovery?command=delete";
xhr.post(_d73,{}).then(function(resp){
try{
if(resp!="OK"){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.delete.error"));
return;
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.delete.complete"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.delete.catch.error")+" '"+e+"'.");
}
},function(_d74){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.delete.function.error")+" '"+_d74+"'.");
});
},_loadModal:function(_d75,_d76){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI._loadModal"));
localStorage[curam.tab.TabSessionManager.SELECTED_TAB_KEY]=_d76;
var _d77=_d75.pageID;
var _d78=_d75.params;
curam.util.UimDialog.open(_d77+"Page.do",_d78);
},_getAutoRecoveryService:function(_d79){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get"));
var self=this;
var _d7a="servlet/autorecovery?command=load";
xhr.post(_d7a,{}).then(function(resp){
try{
if(resp&&resp.length>0){
var _d7b=JSON.parse(resp);
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get.success"));
var _d7c=JSON.parse(_d7b.foregroundPage);
if(!_d79){
var _d7d=JSON.parse(_d7b.backgroundTD);
self._loadModal(_d7c,JSON.stringify(_d7d));
}else{
var page="Curam_"+_d7c.pageID;
if(_d79.document.getElementById(page)){
if(_d7b.foregroundPageData){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get.setFormFields"));
var _d7e=JSON.parse(_d7b.foregroundPageData);
_d79.curam.util.ui.form.CuramFormsAPI.setFormFields(_d7c,_d7e);
}
}else{
self._createAutorecoveryHandler();
}
}
}else{
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get.no.record"));
}
}
catch(e){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get.catch.error")+" '"+e+"'.");
}
},function(_d7f){
curam.debug.log("curam.util.AutoRecoveryAPI: "+_d4e.getProperty("curam.util.AutoRecoveryAPI.get.function.error")+" '"+_d7f+"'.");
});
}});
return _d4f;
});
},"dijit/tree/TreeStoreModel":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/_base/lang"],function(_d80,_d81,_d82,lang){
return _d82("dijit.tree.TreeStoreModel",null,{store:null,childrenAttrs:["children"],newItemIdAttr:"id",labelAttr:"",root:null,query:null,deferItemLoadingUntilExpand:false,constructor:function(args){
lang.mixin(this,args);
this.connects=[];
var _d83=this.store;
if(!_d83.getFeatures()["dojo.data.api.Identity"]){
throw new Error("dijit.tree.TreeStoreModel: store must support dojo.data.Identity");
}
if(_d83.getFeatures()["dojo.data.api.Notification"]){
this.connects=this.connects.concat([_d81.after(_d83,"onNew",lang.hitch(this,"onNewItem"),true),_d81.after(_d83,"onDelete",lang.hitch(this,"onDeleteItem"),true),_d81.after(_d83,"onSet",lang.hitch(this,"onSetItem"),true)]);
}
},destroy:function(){
var h;
while(h=this.connects.pop()){
h.remove();
}
},getRoot:function(_d84,_d85){
if(this.root){
_d84(this.root);
}else{
this.store.fetch({query:this.query,onComplete:lang.hitch(this,function(_d86){
if(_d86.length!=1){
throw new Error("dijit.tree.TreeStoreModel: root query returned "+_d86.length+" items, but must return exactly one");
}
this.root=_d86[0];
_d84(this.root);
}),onError:_d85});
}
},mayHaveChildren:function(item){
return _d80.some(this.childrenAttrs,function(attr){
return this.store.hasAttribute(item,attr);
},this);
},getChildren:function(_d87,_d88,_d89){
var _d8a=this.store;
if(!_d8a.isItemLoaded(_d87)){
var _d8b=lang.hitch(this,arguments.callee);
_d8a.loadItem({item:_d87,onItem:function(_d8c){
_d8b(_d8c,_d88,_d89);
},onError:_d89});
return;
}
var _d8d=[];
for(var i=0;i<this.childrenAttrs.length;i++){
var vals=_d8a.getValues(_d87,this.childrenAttrs[i]);
_d8d=_d8d.concat(vals);
}
var _d8e=0;
if(!this.deferItemLoadingUntilExpand){
_d80.forEach(_d8d,function(item){
if(!_d8a.isItemLoaded(item)){
_d8e++;
}
});
}
if(_d8e==0){
_d88(_d8d);
}else{
_d80.forEach(_d8d,function(item,idx){
if(!_d8a.isItemLoaded(item)){
_d8a.loadItem({item:item,onItem:function(item){
_d8d[idx]=item;
if(--_d8e==0){
_d88(_d8d);
}
},onError:_d89});
}
});
}
},isItem:function(_d8f){
return this.store.isItem(_d8f);
},fetchItemByIdentity:function(_d90){
this.store.fetchItemByIdentity(_d90);
},getIdentity:function(item){
return this.store.getIdentity(item);
},getLabel:function(item){
if(this.labelAttr){
return this.store.getValue(item,this.labelAttr);
}else{
return this.store.getLabel(item);
}
},newItem:function(args,_d91,_d92){
var _d93={parent:_d91,attribute:this.childrenAttrs[0]},_d94;
if(this.newItemIdAttr&&args[this.newItemIdAttr]){
this.fetchItemByIdentity({identity:args[this.newItemIdAttr],scope:this,onItem:function(item){
if(item){
this.pasteItem(item,null,_d91,true,_d92);
}else{
_d94=this.store.newItem(args,_d93);
if(_d94&&(_d92!=undefined)){
this.pasteItem(_d94,_d91,_d91,false,_d92);
}
}
}});
}else{
_d94=this.store.newItem(args,_d93);
if(_d94&&(_d92!=undefined)){
this.pasteItem(_d94,_d91,_d91,false,_d92);
}
}
},pasteItem:function(_d95,_d96,_d97,_d98,_d99){
var _d9a=this.store,_d9b=this.childrenAttrs[0];
if(_d96){
_d80.forEach(this.childrenAttrs,function(attr){
if(_d9a.containsValue(_d96,attr,_d95)){
if(!_d98){
var _d9c=_d80.filter(_d9a.getValues(_d96,attr),function(x){
return x!=_d95;
});
_d9a.setValues(_d96,attr,_d9c);
}
_d9b=attr;
}
});
}
if(_d97){
if(typeof _d99=="number"){
var _d9d=_d9a.getValues(_d97,_d9b).slice();
_d9d.splice(_d99,0,_d95);
_d9a.setValues(_d97,_d9b,_d9d);
}else{
_d9a.setValues(_d97,_d9b,_d9a.getValues(_d97,_d9b).concat(_d95));
}
}
},onChange:function(){
},onChildrenChange:function(){
},onDelete:function(){
},onNewItem:function(item,_d9e){
if(!_d9e){
return;
}
this.getChildren(_d9e.item,lang.hitch(this,function(_d9f){
this.onChildrenChange(_d9e.item,_d9f);
}));
},onDeleteItem:function(item){
this.onDelete(item);
},onSetItem:function(item,_da0){
if(_d80.indexOf(this.childrenAttrs,_da0)!=-1){
this.getChildren(item,lang.hitch(this,function(_da1){
this.onChildrenChange(item,_da1);
}));
}else{
this.onChange(item);
}
}});
});
},"curam/layout/ScrollingTabController":function(){
define(["dojo/_base/declare","dojo/dom-class","dijit/layout/ScrollingTabController","curam/inspection/Layer","curam/debug","curam/widget/_HasDropDown","dojo/text!curam/layout/resources/ScrollingTabController.html"],function(_da2,_da3,_da4,_da5,_da6,_da7,_da8){
var _da9=_da2("curam.layout.ScrollingTabController",_da4,{templateString:_da8,onStartup:function(){
this.inherited(arguments);
_da5.register("curam/layout/ScrollingTabController",this);
},updateTabStyle:function(){
var kids=this.getChildren();
curam.debug.log("curam.layout.ScrollingTabController.updateTabStyle kids = ",this.domNode);
dojo.forEach(kids,function(_daa,_dab,_dac){
_da3.remove(_daa.domNode,["first-class","last-class"]);
if(_dab==0){
_da3.add(_daa.domNode,"first");
}else{
if(_dab==_dac.length-1){
_da3.add(_daa.domNode,"last");
}
}
});
var _dad=dojo.query(".nowrapTabStrip",this.domNode)[0];
_da3.replace(_dad,"nowrapSecTabStrip","nowrapTabStrip");
var _dae=document.createElement("div");
_da3.add(_dae,"block-slope");
_da3.add(_dae,"dijitTab");
_dae.innerHTML="&#x200B;";
_dad.appendChild(_dae);
}});
_da2("curam.layout._ScrollingTabControllerMenuButton",[dijit.layout._ScrollingTabControllerMenuButton,_da7],{closeDropDown:function(_daf){
this.inherited(arguments);
if(this.dropDown){
this._popupStateNode.removeAttribute("aria-owns");
this.dropDown.destroyRecursive();
delete this.dropDown;
}
}});
return _da9;
});
},"dojo/dnd/TimedMoveable":function(){
define(["../_base/declare","./Moveable"],function(_db0,_db1){
var _db2=_db1.prototype.onMove;
return _db0("dojo.dnd.TimedMoveable",_db1,{timeout:40,constructor:function(node,_db3){
if(!_db3){
_db3={};
}
if(_db3.timeout&&typeof _db3.timeout=="number"&&_db3.timeout>=0){
this.timeout=_db3.timeout;
}
},onMoveStop:function(_db4){
if(_db4._timer){
clearTimeout(_db4._timer);
_db2.call(this,_db4,_db4._leftTop);
}
_db1.prototype.onMoveStop.apply(this,arguments);
},onMove:function(_db5,_db6){
_db5._leftTop=_db6;
if(!_db5._timer){
var _db7=this;
_db5._timer=setTimeout(function(){
_db5._timer=null;
_db2.call(_db7,_db5,_db5._leftTop);
},this.timeout);
}
}});
});
},"dijit/a11yclick":function(){
define(["dojo/keys","dojo/mouse","dojo/on","dojo/touch"],function(keys,_db8,on,_db9){
function _dba(e){
if((e.keyCode===keys.ENTER||e.keyCode===keys.SPACE)&&!/input|button|textarea/i.test(e.target.nodeName)){
for(var node=e.target;node;node=node.parentNode){
if(node.dojoClick){
return true;
}
}
}
};
var _dbb;
on(document,"keydown",function(e){
if(_dba(e)){
_dbb=e.target;
e.preventDefault();
}else{
_dbb=null;
}
});
on(document,"keyup",function(e){
if(_dba(e)&&e.target==_dbb){
_dbb=null;
on.emit(e.target,"click",{cancelable:true,bubbles:true,ctrlKey:e.ctrlKey,shiftKey:e.shiftKey,metaKey:e.metaKey,altKey:e.altKey,_origType:e.type});
}
});
var _dbc=function(node,_dbd){
node.dojoClick=true;
return on(node,"click",_dbd);
};
_dbc.click=_dbc;
_dbc.press=function(node,_dbe){
var _dbf=on(node,_db9.press,function(evt){
if(evt.type=="mousedown"&&!_db8.isLeft(evt)){
return;
}
_dbe(evt);
}),_dc0=on(node,"keydown",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_dbe(evt);
}
});
return {remove:function(){
_dbf.remove();
_dc0.remove();
}};
};
_dbc.release=function(node,_dc1){
var _dc2=on(node,_db9.release,function(evt){
if(evt.type=="mouseup"&&!_db8.isLeft(evt)){
return;
}
_dc1(evt);
}),_dc3=on(node,"keyup",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_dc1(evt);
}
});
return {remove:function(){
_dc2.remove();
_dc3.remove();
}};
};
_dbc.move=_db9.move;
return _dbc;
});
},"curam/cdsl/_base/FacadeMethodResponse":function(){
define(["dojo/_base/declare","curam/cdsl/Struct","dojo/json","dojo/string"],function(_dc4,_dc5,json,_dc6){
var _dc7="for(;;);";
var _dc8=function(_dc9,_dca){
var ret=[],_dcb=_dca?Array(_dca+1).join("  "):"";
dojo.forEach(_dc9,function(e){
ret.push(_dc6.substitute("${indent}Type: ${type}\n"+"${indent}Message: ${msg}\n"+"${indent}Stack trace:\n"+"${indent}  ${stackTrace}",{type:e.type,msg:e.message,stackTrace:e.stackTrace,indent:_dcb}));
if(e.nestedError){
ret.push("\n-- nested error --");
ret.push(_dc8([e.nestedError],_dca?_dca+1:1));
}
});
return ret.join("\n");
};
var _dcc=_dc4(null,{_request:null,_data:null,_metadataRegistry:null,constructor:function(_dcd,_dce,_dcf){
if(!_dcd||!_dce){
throw new Error("Missing parameter.");
}
if(typeof _dce=="string"){
this._data=json.parse(_dce.substr(_dc7.length,_dce.length));
}else{
if(typeof _dce=="object"){
this._data=_dce;
}else{
throw new Error("Wrong parameter type: "+typeof _dcd+", "+typeof _dce);
}
}
this._request=_dcd;
this._metadataRegistry=_dcf;
},returnValue:function(){
return new _dc5(this._data.data,{bareInput:true,fixups:this._data.metadata&&this._data.metadata.fixups?this._data.metadata.fixups:null,metadataRegistry:this._metadataRegistry,dataAdapter:this._request.dataAdapter()});
},failed:function(){
return this._data.code!==0;
},getError:function(){
var _dd0=this._data.errors;
if(_dd0){
var e=new Error("Server returned "+_dd0.length+(_dd0.length==1?" error":" errors")+".");
e.errors=_dd0;
e.toString=function(){
return _dc8(_dd0);
};
return e;
}
return null;
},hasCodetables:function(){
return this._data.metadata&&this._data.metadata.codetables&&this._data.metadata.codetables.length>0;
},getCodetablesData:function(){
return this._data.metadata.codetables;
},devMode:function(){
var dm=false;
if(this._data.metadata&&this._data.metadata.devMode){
dm=(this._data.metadata.devMode===true);
}
return dm;
},request:function(){
return this._request;
}});
return _dcc;
});
},"dojo/request/registry":function(){
define(["require","../_base/array","./default!platform","./util"],function(_dd1,_dd2,_dd3,util){
var _dd4=[];
function _dd5(url,_dd6){
var _dd7=_dd4.slice(0),i=0,_dd8;
while(_dd8=_dd7[i++]){
if(_dd8(url,_dd6)){
return _dd8.request.call(null,url,_dd6);
}
}
return _dd3.apply(null,arguments);
};
function _dd9(_dda,_ddb){
var _ddc;
if(_ddb){
if(_dda.test){
_ddc=function(url){
return _dda.test(url);
};
}else{
if(_dda.apply&&_dda.call){
_ddc=function(){
return _dda.apply(null,arguments);
};
}else{
_ddc=function(url){
return url===_dda;
};
}
}
_ddc.request=_ddb;
}else{
_ddc=function(){
return true;
};
_ddc.request=_dda;
}
return _ddc;
};
_dd5.register=function(url,_ddd,_dde){
var _ddf=_dd9(url,_ddd);
_dd4[(_dde?"unshift":"push")](_ddf);
return {remove:function(){
var idx;
if(~(idx=_dd2.indexOf(_dd4,_ddf))){
_dd4.splice(idx,1);
}
}};
};
_dd5.load=function(id,_de0,_de1,_de2){
if(id){
_dd1([id],function(_de3){
_dd3=_de3;
_de1(_dd5);
});
}else{
_de1(_dd5);
}
};
util.addCommonMethods(_dd5);
return _dd5;
});
},"curam/cdsl/_base/PreferenceMap":function(){
define(["dojo/_base/declare","dojo/_base/lang"],function(_de4,lang){
var _de5=_de4(null,{_preferences:null,_preferenceNames:null,constructor:function(){
this._preferences={};
this._preferenceNames=[];
},getPreference:function(name){
return this._preferences[name];
},getPreferenceNames:function(){
return this._preferenceNames;
},addPreference:function(name,_de6){
this._preferences[name]=_de6;
this._preferenceNames[this._preferenceNames.length]=name;
}});
return _de5;
});
},"dijit/hccss":function(){
define(["dojo/dom-class","dojo/hccss","dojo/domReady","dojo/_base/window"],function(_de7,has,_de8,win){
_de8(function(){
if(has("highcontrast")){
_de7.add(win.body(),"dijit_a11y");
}
});
return has;
});
},"dojo/behavior":function(){
define(["./_base/kernel","./_base/lang","./_base/array","./_base/connect","./query","./domReady"],function(dojo,lang,_de9,_dea,_deb,_dec){
dojo.deprecated("dojo.behavior","Use dojo/on with event delegation (on.selector())");
var _ded=function(){
function _dee(obj,name){
if(!obj[name]){
obj[name]=[];
}
return obj[name];
};
var _def=0;
function _df0(obj,_df1,func){
var _df2={};
for(var x in obj){
if(typeof _df2[x]=="undefined"){
if(!func){
_df1(obj[x],x);
}else{
func.call(_df1,obj[x],x);
}
}
}
};
this._behaviors={};
this.add=function(_df3){
_df0(_df3,this,function(_df4,name){
var _df5=_dee(this._behaviors,name);
if(typeof _df5["id"]!="number"){
_df5.id=_def++;
}
var _df6=[];
_df5.push(_df6);
if((lang.isString(_df4))||(lang.isFunction(_df4))){
_df4={found:_df4};
}
_df0(_df4,function(rule,_df7){
_dee(_df6,_df7).push(rule);
});
});
};
var _df8=function(node,_df9,_dfa){
if(lang.isString(_df9)){
if(_dfa=="found"){
_dea.publish(_df9,[node]);
}else{
_dea.connect(node,_dfa,function(){
_dea.publish(_df9,arguments);
});
}
}else{
if(lang.isFunction(_df9)){
if(_dfa=="found"){
_df9(node);
}else{
_dea.connect(node,_dfa,_df9);
}
}
}
};
this.apply=function(){
_df0(this._behaviors,function(_dfb,id){
_deb(id).forEach(function(elem){
var _dfc=0;
var bid="_dj_behavior_"+_dfb.id;
if(typeof elem[bid]=="number"){
_dfc=elem[bid];
if(_dfc==(_dfb.length)){
return;
}
}
for(var x=_dfc,tver;tver=_dfb[x];x++){
_df0(tver,function(_dfd,_dfe){
if(lang.isArray(_dfd)){
_de9.forEach(_dfd,function(_dff){
_df8(elem,_dff,_dfe);
});
}
});
}
elem[bid]=_dfb.length;
});
});
};
};
dojo.behavior=new _ded();
_dec(function(){
dojo.behavior.apply();
});
return dojo.behavior;
});
},"dijit/popup":function(){
define("dijit/popup",["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./place","./BackgroundIframe","./Viewport","./main","dojo/touch"],function(_e00,_e01,_e02,dom,_e03,_e04,_e05,_e06,_e07,has,keys,lang,on,_e08,_e09,_e0a,_e0b){
function _e0c(){
if(this._popupWrapper){
_e05.destroy(this._popupWrapper);
delete this._popupWrapper;
}
};
var _e0d=_e02(null,{_stack:[],_beginZIndex:1000,_idGen:1,_repositionAll:function(){
if(this._firstAroundNode){
var _e0e=this._firstAroundPosition,_e0f=_e06.position(this._firstAroundNode,true),dx=_e0f.x-_e0e.x,dy=_e0f.y-_e0e.y;
if(dx||dy){
this._firstAroundPosition=_e0f;
for(var i=0;i<this._stack.length;i++){
var _e10=this._stack[i].wrapper.style;
_e10.top=(parseFloat(_e10.top)+dy)+"px";
if(_e10.right=="auto"){
_e10.left=(parseFloat(_e10.left)+dx)+"px";
}else{
_e10.right=(parseFloat(_e10.right)-dx)+"px";
}
}
}
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),dx||dy?10:50);
}
},_createWrapper:function(_e11){
var _e12=_e11._popupWrapper,node=_e11.domNode;
if(!_e12){
_e12=_e05.create("div",{"class":"dijitPopup",style:{display:"none"},role:"region","aria-label":_e11["aria-label"]||_e11.label||_e11.name||_e11.id},_e11.ownerDocumentBody);
_e12.appendChild(node);
var s=node.style;
s.display="";
s.visibility="";
s.position="";
s.top="0px";
_e11._popupWrapper=_e12;
_e01.after(_e11,"destroy",_e0c,true);
if("ontouchend" in document){
on(_e12,"touchend",function(evt){
if(!/^(input|button|textarea)$/i.test(evt.target.tagName)){
evt.preventDefault();
}
});
}
_e12.dojoClick=true;
}
return _e12;
},moveOffScreen:function(_e13){
var _e14=this._createWrapper(_e13);
var ltr=_e06.isBodyLtr(_e13.ownerDocument),_e15={visibility:"hidden",top:"-9999px",display:""};
_e15[ltr?"left":"right"]="-9999px";
_e15[ltr?"right":"left"]="auto";
_e07.set(_e14,_e15);
return _e14;
},hide:function(_e16){
var _e17=this._createWrapper(_e16);
if(dojo.hasClass(_e17,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
_e03.set(_e17,"aria-hidden","true");
_e07.set(_e17,{position:"absolute",overflow:"hidden",clip:"rect(0 0 0 0)",height:"1px",width:"1px",margin:"-1px",padding:"0",border:"0"});
setTimeout(function(){
if(_e03.get(_e17,"aria-hidden","true")){
_e07.set(_e17,{display:"none",height:"auto",overflowY:"visible",border:"",position:"",overflow:"",clip:"",width:"",margin:"",padding:""});
}
},200,_e17);
}else{
_e07.set(_e17,{display:"none",height:"auto",overflowY:"visible",border:""});
}
var node=_e16.domNode;
if("_originalStyle" in node){
node.style.cssText=node._originalStyle;
}
},getTopPopup:function(){
var _e18=this._stack;
for(var pi=_e18.length-1;pi>0&&_e18[pi].parent===_e18[pi-1].widget;pi--){
}
return _e18[pi];
},open:function(args){
var _e19=this._stack,_e1a=args.popup,node=_e1a.domNode,_e1b=args.orient||["below","below-alt","above","above-alt"],ltr=args.parent?args.parent.isLeftToRight():_e06.isBodyLtr(_e1a.ownerDocument),_e1c=args.around,id=(args.around&&args.around.id)?(args.around.id+"_dropdown"):("popup_"+this._idGen++);
while(_e19.length&&(!args.parent||!dom.isDescendant(args.parent.domNode,_e19[_e19.length-1].widget.domNode))){
this.close(_e19[_e19.length-1].widget);
}
var _e1d=this.moveOffScreen(_e1a);
if(_e1a.startup&&!_e1a._started){
_e1a.startup();
}
var _e1e,_e1f=_e06.position(node);
if("maxHeight" in args&&args.maxHeight!=-1){
_e1e=args.maxHeight||Infinity;
}else{
var _e20=_e0a.getEffectiveBox(this.ownerDocument),_e21=_e1c?_e06.position(_e1c,false):{y:args.y-(args.padding||0),h:(args.padding||0)*2};
_e1e=Math.floor(Math.max(_e21.y,_e20.h-(_e21.y+_e21.h)));
}
if(_e1f.h>_e1e){
var cs=_e07.getComputedStyle(node),_e22=cs.borderLeftWidth+" "+cs.borderLeftStyle+" "+cs.borderLeftColor;
_e07.set(_e1d,{overflowY:"scroll",height:(_e1e-2)+"px",border:_e22});
node._originalStyle=node.style.cssText;
node.style.border="none";
}
_e03.set(_e1d,{id:id,style:{zIndex:this._beginZIndex+_e19.length},"class":"dijitPopup "+(_e1a.baseClass||_e1a["class"]||"").split(" ")[0]+"Popup",dijitPopupParent:args.parent?args.parent.id:""});
if(dojo.hasClass(_e1d,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
if(_e03.get(_e1d,"aria-hidden")==="true"){
_e07.set(_e1d,{position:"",clip:"",width:"",margin:"",padding:"",border:""});
}
_e03.set(_e1d,"aria-hidden","false");
}
if(_e19.length==0&&_e1c){
this._firstAroundNode=_e1c;
this._firstAroundPosition=_e06.position(_e1c,true);
this._aroundMoveListener=setTimeout(lang.hitch(this,"_repositionAll"),50);
}
if(has("config-bgIframe")&&!_e1a.bgIframe){
_e1a.bgIframe=new _e09(_e1d);
}
var _e23=_e1a.orient?lang.hitch(_e1a,"orient"):null,best=_e1c?_e08.around(_e1d,_e1c,_e1b,ltr,_e23):_e08.at(_e1d,args,_e1b=="R"?["TR","BR","TL","BL"]:["TL","BL","TR","BR"],args.padding,_e23);
_e1d.style.visibility="visible";
node.style.visibility="visible";
var _e24=[];
_e24.push(on(_e1d,"keydown",lang.hitch(this,function(evt){
if(evt.keyCode==keys.ESCAPE&&args.onCancel){
evt.stopPropagation();
evt.preventDefault();
args.onCancel(evt);
}else{
if(evt.keyCode==keys.TAB){
evt.stopPropagation();
evt.preventDefault();
var _e25=this.getTopPopup();
if(_e25&&_e25.onCancel){
_e25.onCancel(evt);
}
}
}
})));
if(_e1a.onCancel&&args.onCancel){
_e24.push(_e1a.on("cancel",args.onCancel));
}
_e24.push(_e1a.on(_e1a.onExecute?"execute":"change",lang.hitch(this,function(){
var _e26=this.getTopPopup();
if(_e26&&_e26.onExecute){
_e26.onExecute();
}
})));
_e19.push({widget:_e1a,wrapper:_e1d,parent:args.parent,onExecute:args.onExecute,onCancel:args.onCancel,onClose:args.onClose,handlers:_e24});
if(_e1a.onOpen){
_e1a.onOpen(best);
}
return best;
},close:function(_e27){
var _e28=this._stack;
while((_e27&&_e00.some(_e28,function(elem){
return elem.widget==_e27;
}))||(!_e27&&_e28.length)){
var top=_e28.pop(),_e29=top.widget,_e2a=top.onClose;
if(_e29.bgIframe){
_e29.bgIframe.destroy();
delete _e29.bgIframe;
}
if(_e29.onClose){
_e29.onClose();
}
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_e29&&_e29.domNode){
this.hide(_e29);
}
if(_e2a){
_e2a();
}
}
if(_e28.length==0&&this._aroundMoveListener){
clearTimeout(this._aroundMoveListener);
this._firstAroundNode=this._firstAroundPosition=this._aroundMoveListener=null;
}
}});
return (_e0b.popup=new _e0d());
});
},"dijit/layout/TabContainer":function(){
define(["dojo/_base/lang","dojo/_base/declare","./_TabContainerBase","./TabController","./ScrollingTabController"],function(lang,_e2b,_e2c,_e2d,_e2e){
return _e2b("dijit.layout.TabContainer",_e2c,{useMenu:true,useSlider:true,controllerWidget:"",_makeController:function(_e2f){
var cls=this.baseClass+"-tabs"+(this.doLayout?"":" dijitTabNoLayout"),_e2d=typeof this.controllerWidget=="string"?lang.getObject(this.controllerWidget):this.controllerWidget;
return new _e2d({id:this.id+"_tablist",ownerDocument:this.ownerDocument,dir:this.dir,lang:this.lang,textDir:this.textDir,tabPosition:this.tabPosition,doLayout:this.doLayout,containerId:this.id,"class":cls,nested:this.nested,useMenu:this.useMenu,useSlider:this.useSlider,tabStripClass:this.tabStrip?this.baseClass+(this.tabStrip?"":"No")+"Strip":null},_e2f);
},postMixInProperties:function(){
this.inherited(arguments);
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_e2e:_e2d;
}
}});
});
},"curam/cdsl/request/CuramService":function(){
define(["dojo/_base/declare","curam/cdsl/Struct","curam/cdsl/_base/FacadeMethodResponse","dojo/_base/lang","curam/cdsl/_base/_Connection","curam/cdsl/_base/FacadeMethodCall"],function(_e30,_e31,_e32,lang){
var _e33={dataAdapter:null},_e34=function(_e35){
var o=lang.clone(_e33);
o=lang.mixin(o,_e35);
return o;
},rule="********************************************************",_e36=_e30(null,{_connection:null,_dataAdapter:null,constructor:function(_e37,opts){
var _e38=_e34(opts);
this._connection=_e37;
this._dataAdapter=_e38.dataAdapter;
},call:function(_e39,_e3a){
var _e3b=_e39[0];
if(!_e3b.dataAdapter()){
_e3b.dataAdapter(this._dataAdapter);
}
var _e3c=this._connection.invoke(_e3b,_e3a);
return _e3c.then(lang.hitch(this,function(_e3d){
var _e3e=new _e32(_e3b,_e3d,this._connection.metadata());
if(_e3e.failed()){
var e=_e3e.getError();
if(_e3e.devMode()){
console.log(rule);
console.log(e.toString());
console.log(rule);
}
throw e;
}
this._connection.updateMetadata(_e3e);
return [_e3e.returnValue()];
}));
}});
return _e36;
});
},"dijit/form/_FormValueWidget":function(){
define(["dojo/_base/declare","dojo/sniff","./_FormWidget","./_FormValueMixin"],function(_e3f,has,_e40,_e41){
return _e3f("dijit.form._FormValueWidget",[_e40,_e41],{_layoutHackIE7:function(){
if(has("ie")==7){
var _e42=this.domNode;
var _e43=_e42.parentNode;
var _e44=_e42.firstChild||_e42;
var _e45=_e44.style.filter;
var _e46=this;
while(_e43&&_e43.clientHeight==0){
(function ping(){
var _e47=_e46.connect(_e43,"onscroll",function(){
_e46.disconnect(_e47);
_e44.style.filter=(new Date()).getMilliseconds();
_e46.defer(function(){
_e44.style.filter=_e45;
});
});
})();
_e43=_e43.parentNode;
}
}
}});
});
},"dijit/_BidiSupport":function(){
define(["dojo/has","./_WidgetBase","./_BidiMixin"],function(has,_e48,_e49){
_e48.extend(_e49);
has.add("dojo-bidi",true);
return _e48;
});
},"curam/util/UimDialog":function(){
define(["curam/util/RuntimeContext","curam/util/external","curam/util","curam/define","curam/dialog","curam/util/DialogObject"],function(_e4a,_e4b){
curam.define.singleton("curam.util.UimDialog",{open:function(path,_e4c,_e4d){
var url=path+curam.util.makeQueryString(_e4c);
return this.openUrl(url,_e4d);
},openUrl:function(url,_e4e){
var _e4f=curam.util.getCacheBusterParameter();
var _e50=new curam.util.DialogObject(_e4f);
var _e51=null;
if(_e4e){
_e51="width="+_e4e.width+",height="+_e4e.height;
}
curam.util.openModalDialog({href:this._addRpu(url)},_e51,null,null,_e4f);
return _e50;
},_addRpu:function(url){
var _e52=url;
if(curam.tab.inTabbedUI()){
var _e53=curam.tab.getContentPanelIframe();
if(_e53){
_e52=curam.util.setRpu(url,new _e4a(_e53.contentWindow));
}
}else{
if(_e4b.inExternalApp()){
var _e54=_e4b.getUimParentWindow();
if(_e54){
_e52=curam.util.setRpu(url,new _e4a(_e54));
}
}
}
return _e52;
},get:function(){
if(curam.dialog._id==null){
throw "Dialog infrastructure not ready.";
}
return new curam.util.DialogObject(null,curam.dialog._id);
},ready:function(_e55){
if(curam.dialog._id==null){
dojo.subscribe("/curam/dialog/ready",_e55);
}else{
_e55();
}
},_getDialogFrameWindow:function(_e56){
var _e57=window.top.dijit.byId(_e56);
return _e57.uimController.getIFrame().contentWindow;
}});
return curam.util.UimDialog;
});
},"dijit/tree/_dndContainer":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom-class","dojo/_base/lang","dojo/on","dojo/touch"],function(_e58,_e59,_e5a,lang,on,_e5b){
return _e59("dijit.tree._dndContainer",null,{constructor:function(tree,_e5c){
this.tree=tree;
this.node=tree.domNode;
lang.mixin(this,_e5c);
this.containerState="";
_e5a.add(this.node,"dojoDndContainer");
this.events=[on(this.node,_e5b.enter,lang.hitch(this,"onOverEvent")),on(this.node,_e5b.leave,lang.hitch(this,"onOutEvent")),_e58.after(this.tree,"_onNodeMouseEnter",lang.hitch(this,"onMouseOver"),true),_e58.after(this.tree,"_onNodeMouseLeave",lang.hitch(this,"onMouseOut"),true),on(this.node,"dragstart, selectstart",function(evt){
evt.preventDefault();
})];
},destroy:function(){
var h;
while(h=this.events.pop()){
h.remove();
}
this.node=this.parent=null;
},onMouseOver:function(_e5d){
this.current=_e5d;
},onMouseOut:function(){
this.current=null;
},_changeState:function(type,_e5e){
var _e5f="dojoDnd"+type;
var _e60=type.toLowerCase()+"State";
_e5a.replace(this.node,_e5f+_e5e,_e5f+this[_e60]);
this[_e60]=_e5e;
},_addItemClass:function(node,type){
_e5a.add(node,"dojoDndItem"+type);
},_removeItemClass:function(node,type){
_e5a.remove(node,"dojoDndItem"+type);
},onOverEvent:function(){
this._changeState("Container","Over");
},onOutEvent:function(){
this._changeState("Container","");
}});
});
},"dijit/focus":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/domReady","dojo/sniff","dojo/Stateful","dojo/_base/window","dojo/window","./a11y","./registry","./main"],function(_e61,_e62,dom,_e63,_e64,_e65,_e66,lang,on,_e67,has,_e68,win,_e69,a11y,_e6a,_e6b){
var _e6c;
var _e6d;
var _e6e=_e62([_e68,_e66],{curNode:null,activeStack:[],constructor:function(){
var _e6f=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_e61.before(_e65,"empty",_e6f);
_e61.before(_e65,"destroy",_e6f);
},registerIframe:function(_e70){
return this.registerWin(_e70.contentWindow,_e70);
},registerWin:function(_e71,_e72){
var _e73=this,body=_e71.document&&_e71.document.body;
if(body){
var _e74=has("pointer-events")?"pointerdown":has("MSPointer")?"MSPointerDown":has("touch-events")?"mousedown, touchstart":"mousedown";
var mdh=on(_e71.document,_e74,function(evt){
if(evt&&evt.target&&evt.target.parentNode==null){
return;
}
_e73._onTouchNode(_e72||evt.target,"mouse");
});
var fih=on(body,"focusin",function(evt){
if(!evt.target.tagName){
return;
}
var tag=evt.target.tagName.toLowerCase();
if(tag=="#document"||tag=="body"){
return;
}
if(a11y.isFocusable(evt.target)){
_e73._onFocusNode(_e72||evt.target);
}else{
_e73._onTouchNode(_e72||evt.target);
}
});
var foh=on(body,"focusout",function(evt){
_e73._onBlurNode(_e72||evt.target);
});
return {remove:function(){
mdh.remove();
fih.remove();
foh.remove();
mdh=fih=foh=null;
body=null;
}};
}
},_onBlurNode:function(node){
var now=(new Date()).getTime();
if(now<_e6c+100){
return;
}
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
}
this._clearFocusTimer=setTimeout(lang.hitch(this,function(){
this.set("prevNode",this.curNode);
this.set("curNode",null);
}),0);
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
}
if(now<_e6d+100){
return;
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
}),0);
},_onTouchNode:function(node,by){
_e6d=(new Date()).getTime();
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_e64.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _e75=[];
try{
while(node){
var _e76=_e63.get(node,"dijitPopupParent");
if(_e76){
node=_e6a.byId(_e76).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_e69.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_e77=id&&_e6a.byId(id);
if(_e77&&!(by=="mouse"&&_e77.get("disabled"))){
_e75.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_e75,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
_e6c=(new Date()).getTime();
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
delete this._clearFocusTimer;
}
this._onTouchNode(node);
if(node==this.curNode){
return;
}
this.set("prevNode",this.curNode);
this.set("curNode",node);
},_setStack:function(_e78,by){
var _e79=this.activeStack,_e7a=_e79.length-1,_e7b=_e78.length-1;
if(_e78[_e7b]==_e79[_e7a]){
return;
}
this.set("activeStack",_e78);
var _e7c,i;
for(i=_e7a;i>=0&&_e79[i]!=_e78[i];i--){
_e7c=_e6a.byId(_e79[i]);
if(_e7c){
_e7c._hasBeenBlurred=true;
_e7c.set("focused",false);
if(_e7c._focusManager==this){
_e7c._onBlur(by);
}
this.emit("widget-blur",_e7c,by);
}
}
for(i++;i<=_e7b;i++){
_e7c=_e6a.byId(_e78[i]);
if(_e7c){
_e7c.set("focused",true);
if(_e7c._focusManager==this){
_e7c._onFocus(by);
}
this.emit("widget-focus",_e7c,by);
}
}
},focus:function(node){
if(node){
try{
node.focus();
}
catch(e){
}
}
}});
var _e7d=new _e6e();
_e67(function(){
var _e7e=_e7d.registerWin(_e69.get(document));
if(has("ie")){
on(window,"unload",function(){
if(_e7e){
_e7e.remove();
_e7e=null;
}
});
}
});
_e6b.focus=function(node){
_e7d.focus(node);
};
for(var attr in _e7d){
if(!/^_/.test(attr)){
_e6b.focus[attr]=typeof _e7d[attr]=="function"?lang.hitch(_e7d,attr):_e7d[attr];
}
}
_e7d.watch(function(attr,_e7f,_e80){
_e6b.focus[attr]=_e80;
});
return _e7d;
});
},"curam/util/ExpandableLists":function(){
define(["dojo/dom","dojo/dom-construct","dojo/_base/window","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/query","dojo/sniff","dijit/registry","curam/inspection/Layer","curam/util","curam/debug","curam/UIMController","curam/util/ui/refresh/RefreshEvent","curam/define","curam/contentPanel"],function(dom,_e81,win,_e82,_e83,_e84,_e85,has,_e86,_e87,util,_e88){
curam.define.singleton("curam.util.ExpandableLists",{_minimumExpandedHeight:[],stateData:[],_LIST_ID_PREFIX:"list-id-",_ROW_ID_PREFIX:"row-id-",_EVENT_TOGGLE:"/curam/list/row/toggle",_EVENT_TYPE_EXPANDED:"Expanded",_EVENT_TYPE_COLLAPSED:"Collapsed",setupToggleHandler:function(){
dojo.ready(function(){
var _e89=curam.util.ExpandableLists;
var _e8a=function(_e8b,_e8c,_e8d){
if(_e8b){
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.event1",[_e8d,_e8b,_e8c]));
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.event2",[_e8d]));
}
if(_e8d==_e89._EVENT_TYPE_EXPANDED){
var _e8e=_e89._getListData(_e8b);
var _e8f=dojo.filter(_e8e.expandedRows,function(item){
return item==_e8c;
});
if(_e8f.length==0){
_e8e.expandedRows.push(_e8c);
}
}else{
var _e8e=_e89._getListData(_e8b);
_e8e.expandedRows=dojo.filter(_e8e.expandedRows,function(item){
return item!=_e8c;
});
if(_e8e.expandedRows.length==0){
_e89._removeListData(_e8b);
}
}
curam.debug.log("curam.util.ExpandableLists.setupToggleHandler stateData: ",_e89.stateData);
};
dojo.subscribe(_e89._EVENT_TOGGLE,this,_e8a);
dojo.subscribe("/curam/page/refresh",this,_e89._saveStateData);
});
},_saveStateData:function(){
var _e90=curam.util.ExpandableLists;
curam.debug.log("/curam/page/refresh"+_e88.getProperty("curam.util.ExpandableLists.refresh"),_e90.stateData);
try{
dojo.forEach(_e90.stateData,function(item){
var data=dojo.toJson(item);
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.exception"),data);
localStorage[_e90._sanitizeKey(item.listId)]=data;
});
}
catch(e){
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.exception"),e);
}
},_sanitizeKey:function(key){
return key.replace("-","_");
},loadStateData:function(_e91){
if(typeof (window.curamDialogRedirecting)!="undefined"){
curam.debug.log("curam.util.ExpandableLists.loadStateData "+_e88.getProperty("curam.util.ExpandableLists.load.exit"));
return;
}
var _e92=curam.util.ExpandableLists;
var _e93=function(){
curam.debug.log("curam.util.ExpandableLists.loadStateData "+ +_e88.getProperty("curam.util.ExpandableLists.load.for"),_e91);
var _e94=localStorage[_e92._sanitizeKey(_e91)];
if(_e94&&_e94!=""){
var _e95=dojo.fromJson(_e94);
var _e96=_e85("table."+_e92._LIST_ID_PREFIX+_e91);
dojo.forEach(_e95.expandedRows,function(item){
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.load.row"),item);
var _e97=_e85("tr."+_e92._ROW_ID_PREFIX+item,_e96[0]);
if(_e97.length>0){
var _e98=_e97[0].prev("tr").children();
var _e99=_e98.children("a.list-details-row-toggle")[0];
if(_e99){
_e92._toggleDetailsRow(_e99);
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.load.button"+".disabled"));
}
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.load.row.disabled"));
}
});
localStorage.removeItem(_e92._sanitizeKey(_e91));
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.load.no.data"));
}
};
dojo.ready(function(){
_e93();
});
},_getListData:function(_e9a){
var _e9b=curam.util.ExpandableLists.stateData;
var _e9c=dojo.filter(_e9b,function(item){
return item.listId==_e9a;
});
if(_e9c.length==0){
_e9c.push({listId:_e9a,expandedRows:[]});
_e9b.push(_e9c[0]);
}
return _e9c[0];
},_removeListData:function(_e9d){
var _e9e=curam.util.ExpandableLists;
_e9e.stateData=dojo.filter(_e9e.stateData,function(item){
return item.listId!=_e9d;
});
},toggleListDetailsRow:function(_e9f){
if(_e9f){
_e9f=dojo.fixEvent(_e9f);
dojo.stopEvent(_e9f);
var _ea0=_e9f.currentTarget;
curam.util.ExpandableLists._toggleDetailsRow(_ea0);
}
},_generateUimController:function(_ea1){
var _ea2=_e85("td",_ea1)[0];
var _ea3=_e85("div",_ea1)[0];
var _ea4=new curam.UIMController({uid:_e84.get(_ea3,"uid"),url:_e84.get(_ea3,"url"),iframeId:_e84.get(_ea3,"iframeId"),iframeClassList:_e84.get(_ea3,"iframeClassList"),loadFrameOnCreate:_e84.get(_ea3,"loadFrameOnCreate")});
_ea2.appendChild(_ea4.domNode);
if(_ea3&&_ea2){
_ea2.removeChild(_ea3);
}
return _ea4;
},_toggleDetailsRow:function(_ea5){
curam.debug.log("curam.util.ExpandableLists._toggleDetailsRow "+_e88.getProperty("curam.util.ExpandableLists.load.for"),_ea5);
var _ea6=curam.util.ExpandableLists;
var _ea7=_e85(_ea5).closest("tr")[0];
var _ea8=_e85(_ea7).next("tr")[0];
var _ea9=!_ea6.isDetailsRowExpanded(_ea8);
_ea6._handleStripingAndRoundedCorners(_ea7,_ea8,_ea9);
var _eaa=_e85("div.uimController",_ea8);
var _eab=null;
var _eac=null;
if(_eaa==null||_eaa.length==0){
_eac=_ea6._generateUimController(_ea8);
}else{
_eab=_eaa[0];
_eac=_e86.byNode(_eab);
}
if(typeof (_eac)=="undefined"||_eac==null){
throw "UIMController Dijit not found for node: "+_eab;
}
var _ead=_e84.get(_eac.frame,"src");
var _eae=false;
_ea6.setDetailsRowExpandedState(_ea7,_ea8,_ea9,_ea5);
var def=new dojo.Deferred();
if(!_ead||_ead==null||_ead==""){
_eac.loadPage(def);
}else{
_eae=true;
def.callback();
}
def.addCallback(function(){
var _eaf=_eac.hasInPageNavigation();
_eae=_eae||_eaf;
if(_eaf){
_eac.showTabContainer(_ea9);
}
if(_eae){
curam.util.ExpandableLists.resizeExpandableListAncestors(window);
}
var _eb0=_ea9?_ea6._EVENT_TYPE_EXPANDED:_ea6._EVENT_TYPE_COLLAPSED;
var _eb1=_ea6._findListId(_ea8);
var _eb2=curam.util.getSuffixFromClass(_ea8,_ea6._ROW_ID_PREFIX);
dojo.publish(_ea6._EVENT_TOGGLE,[_eb1,_eb2,_eb0]);
if(!curam.util.ExpandableLists._isExternalApp(window)){
var _eb0=_ea9?"ListDetailsRow.Expand":"ListDetailsRow.Collapse";
var _eb3={url:_e84.get(_eac.frame,"src"),eventType:_eb0};
var _eb4=curam.tab.getSelectedTab();
if(_eb4){
var _eb5=curam.tab.getTabWidgetId(_eb4);
curam.util.getTopmostWindow().dojo.publish("expandedList.toggle",[window.frameElement,_eb3,_eb5]);
}
}
});
},_handleStripingAndRoundedCorners:function(_eb6,_eb7,_eb8){
var odd="odd";
var even="even";
var _eb9="row-no-border";
var _eba="odd-last-row";
var _ebb="even-last-row";
if(!curam.util.ExpandableLists._isLastRow(_eb6,_eb7)){
if(_e83.contains(_eb6,odd)){
_e83.add(_eb7,odd);
}else{
if(_e83.contains(_eb6,even)){
_e83.add(_eb7,even);
}
}
}else{
if(_eb8){
if(_e83.contains(_eb6,_eba)){
_e83.remove(_eb6,_eba);
_e83.add(_eb6,odd);
_e83.add(_eb7,odd);
_e83.add(_eb7,_eba);
}else{
if(_e83.contains(_eb6,_ebb)){
_e83.remove(_eb6,_ebb);
_e83.add(_eb6,even);
_e83.add(_eb7,even);
_e83.add(_eb7,_ebb);
}
}
}else{
if(_e83.contains(_eb6,odd)){
_e83.remove(_eb6,odd);
_e83.add(_eb6,_eba);
_e83.remove(_eb7,_eba);
_e83.remove(_eb7,odd);
}else{
if(_e83.contains(_eb6,even)){
_e83.remove(_eb6,even);
_e83.add(_eb6,_ebb);
_e83.remove(_eb7,even);
_e83.remove(_eb7,_ebb);
}
}
}
}
if(_eb8){
_e83.add(_eb6,_eb9);
}else{
_e83.remove(_eb6,_eb9);
}
if(_e83.contains(_eb6,_eb9)){
_e83.remove(_eb7,"collapsed");
}else{
_e83.add(_eb7,"collapsed");
}
},setDetailsRowExpandedState:function(_ebc,_ebd,_ebe,_ebf){
var _ec0=curam.util.ExpandableLists.isDetailsRowExpanded(_ebd);
_e83.remove(_ebd,"collapsed");
if(!_ec0){
_e83.add(_ebd,"collapsed");
}
if(_ebc.style.display=="none"){
_ebd.setAttribute("style","display:none");
}else{
_ebd.removeAttribute("style");
}
if(_ebf){
var _ec1=_e85("img",_ebf)[0];
if(_ebe){
_e83.add(_ebf,"expanded");
_e84.set(_ebf,"aria-expanded","true");
if(_ec1){
_e84.set(_ec1,"src",require.toUrl("../themes/curam/images/chevron--down20-enabled.svg"));
}
}else{
_e83.remove(_ebf,"expanded");
_e84.set(_ebf,"aria-expanded","false");
if(_ec1&&curam.util.isRtlMode()){
_e84.set(_ec1,"src",require.toUrl("../themes/curam/images/chevron--left20-enabled.svg"));
}else{
_e84.set(_ec1,"src",require.toUrl("../themes/curam/images/chevron--right20-enabled.svg"));
}
}
}
},_isLastRow:function(_ec2,_ec3){
return _e83.contains(_ec2,"even-last-row")||_e83.contains(_ec2,"odd-last-row")||_e83.contains(_ec3,"even-last-row")||_e83.contains(_ec3,"odd-last-row");
},isDetailsRowExpanded:function(_ec4){
return !_e83.contains(_ec4,"collapsed");
},listRowFrameLoaded:function(_ec5,_ec6){
curam.debug.log("========= "+_e88.getProperty("curam.util.ExpandableLists.page.load")+" =======");
curam.debug.log(_ec5);
curam.debug.log(dojo.toJson(_ec6));
var _ec7=dom.byId(_ec5);
if(!_ec7){
throw "List Row Expanded: No iframe found";
}
if(!_ec7._spExpListPageLoadListener){
_ec7._spExpListPageLoadListener="true";
dojo.addOnUnload(function(){
if(sessionStorage.getItem("addOnUnloadTriggeredByFileDownload")==null){
_e84.set(_ec7,"src","");
}
});
}else{
if(!curam.util.ExpandableLists._isExternalApp(window)){
curam.contentPanel.publishSmartPanelExpListPageLoad(_ec7);
}
}
var _ec8=curam.util.ExpandableLists._findListId(_ec7);
var _ec9=curam.util.ExpandableLists.getMinimumExpandedHeight(_ec8);
var _eca=_ec6.height;
if(_eca<_ec9){
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.min.height",[_ec9]));
_eca=_ec9;
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.height",[_eca]));
}
curam.util.ExpandableLists._resizeIframe(_ec7,_eca);
curam.util.ExpandableLists.resizeExpandableListAncestors(window);
curam.util.ExpandableLists._setFrameTitle(_ec7,_ec6);
if(!curam.util.ExpandableLists._isExternalApp(window)){
var _ecb=curam.tab.getSelectedTab();
if(_ecb){
var _ecc=curam.tab.getTabWidgetId(_ecb);
var _ecd=curam.util.getTopmostWindow();
_ecd.curam.util.Refresh.getController(_ecc).pageLoaded(_ec6.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
}
}
curam.debug.log("================================================");
},_resizeIframe:function(_ece,_ecf){
_e82.set(_ece,{height:_ecf+"px"});
curam.util.ExpandableLists._addMediaPrintToIframe(_ece,_ecf);
},_addMediaPrintToIframe:function(_ed0,_ed1){
var _ed2=_ed0.id;
var _ed3=_ed0.contentWindow.frames.parent;
var _ed4=0;
var _ed5=false;
var _ed6=false;
var _ed7=_ed0.contentWindow.document;
win.withDoc(_ed7,function(){
var _ed8=dojo.query(".person-container-panel")[0];
if(_ed8){
_ed5=true;
if(has("ie")||has("trident")){
var body=_ed7.body;
body.setAttribute("style","overflow: hidden !important");
}
}
if(dojo.query(".context-panel-wrapper")[0]){
_ed5=true;
}
if(_ed5){
var _ed9=dojo.query(".pane");
var _eda=dojo.query(".intake-user")[0]||dojo.query(".pd-case-owner")[0]||dojo.query(".ic-case-owner")[0];
if(_ed9.length>1){
_ed4=_ed9[0].scrollHeight*(_ed9.length-1);
}else{
if(_eda){
if(_eda.scrollHeight>20){
_ed4=_eda.scrollHeight;
}
}
}
if(dojo.query(".Intakecontainer-panel")[0]){
_ed6=true;
}
}
},this);
if(_ed5){
var css="";
if(_ed6){
css="@media print { ."+_ed2+" {"+"min-height: calc( "+_ed4+"px + "+_ed1+"px) !important;  "+"min-width: 1300px !important;  "+"-ms-transform: scale(0.85);"+"-ms-transform-origin: 0 0;"+"-moz-transform: scale(0.85);"+"-moz-transform-origin: 0 0;"+"-webkit-transform: scale(0.70);"+"-webkit-transform-origin: 0 0;} }";
}else{
css="@media print { ."+_ed2+" {"+"min-height: calc( "+_ed4+"px + "+_ed1+"px) !important;  "+"min-width: 1200px !important;  "+"-ms-transform: scale(0.9);"+"-ms-transform-origin: 0 0;"+"-moz-transform: scale(0.9);"+"-moz-transform-origin: 0 0;"+"-webkit-transform: scale(0.85);"+"-webkit-transform-origin: 0 0;} }";
}
}else{
var css="@media print { ."+_ed2+" { min-height: calc( 300px + "+_ed1+"px) !important;  } }";
}
var _edb=_ed3.window.document;
win.withDoc(_edb,function(){
if(_e85("#"+_ed0.id+"_print").length>0){
dom.byId(_ed0.id+"_print").innerHTML=css;
}else{
_e81.create("style",{id:_ed0.id+"_print",innerHTML:css},win.body());
}
});
},_setFrameTitle:function(_edc,_edd){
_edc.title=_edc.title+" "+_edd.title;
},_findListId:function(_ede){
return curam.util.getSuffixFromClass(_e85(_ede).closest("table")[0],curam.util.ExpandableLists._LIST_ID_PREFIX);
},resizeExpandableListAncestors:function(_edf){
curam.debug.log("curam.util.ExpandableLists.resizeExpandableListAncestors: ",_edf.location.href);
if(_edf&&_edf!==window.top&&typeof (_edf.frameElement)!="undefined"&&(_e83.contains(_edf.frameElement,"expanded_row_iframe")||curam.util.ExpandableLists.isNestedUIM(_edf))){
var _ee0=_edf.curam.util.getPageHeight();
curam.debug.log("curam.util.ExpandableLists"+".resizeExpandableListAncestors: "+_e88.getProperty("curam.util.ExpandableLists.resize.height"),_ee0);
curam.util.ExpandableLists._resizeIframe(_edf.frameElement,_ee0);
curam.util.ExpandableLists.resizeExpandableListAncestors(_edf.parent);
}else{
curam.debug.log("curam.util.ExpandableLists"+".resizeExpandableListAncestors: "+_e88.getProperty("curam.util.ExpandableLists.resize.end"));
return;
}
},isNestedUIM:function(_ee1){
if(_ee1&&_ee1.jsScreenContext){
return _ee1.jsScreenContext.hasContextBits("NESTED_UIM");
}else{
return false;
}
},_isExternalApp:function(_ee2){
if(_ee2&&_ee2.jsScreenContext){
return _ee2.jsScreenContext.hasContextBits("EXTAPP");
}else{
return false;
}
},setMinimumExpandedHeight:function(_ee3,_ee4){
curam.util.ExpandableLists._minimumExpandedHeight.push({listId:_ee3,minExpHeight:_ee4});
},getMinimumExpandedHeight:function(_ee5){
var data=dojo.filter(curam.util.ExpandableLists._minimumExpandedHeight,function(item){
return item.listId==_ee5;
});
if(data.length==1){
return data[0].minExpHeight;
}else{
curam.debug.log(_e88.getProperty("curam.util.ExpandableLists.default.height"),_ee5);
return 30;
}
}});
_e87.register("curam/util/ExpandableLists",this);
return curam.util.ExpandableLists;
});
},"curam/cdsl/_base/_Connection":function(){
define(["dojo/_base/declare","./MetadataRegistry","./PreferenceMap"],function(_ee6,_ee7,_ee8){
var _ee9=_ee6(null,{_DEFAULT_REQUEST_TIMEOUT:60000,_metadata:null,_preferences:null,constructor:function(){
this._metadata=new _ee7();
this._preferences=new _ee8();
},invoke:function(_eea,_eeb){
this._metadata.setFlags(_eea);
},updateMetadata:function(_eec){
this._metadata.update(_eec);
},metadata:function(){
return this._metadata;
},preferences:function(){
return this._preferences;
}});
return _ee9;
});
},"curam/tab":function(){
define(["dijit/registry","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/dom-class","curam/inspection/Layer","curam/define","curam/util","curam/util/ScreenContext"],function(_eed,dom,_eee,_eef,_ef0,_ef1){
curam.define.singleton("curam.tab",{SECTION_TAB_CONTAINER_ID:"app-sections-container-dc",SMART_PANEL_IFRAME_ID:"curam_tab_SmartPanelIframe",toBeExecutedOnTabClose:[],_mockSelectedTab:null,getSelectedTab:function(_ef2){
if(curam.tab._mockSelectedTab){
return curam.tab._mockSelectedTab;
}
if(curam.tab.getTabContainer(_ef2)){
return curam.tab.getTabContainer(_ef2).selectedChildWidget;
}
},getTabContainer:function(_ef3){
return curam.tab.getTabContainerFromSectionID(_ef3||curam.tab.getCurrentSectionId());
},getCurrentSectionId:function(_ef4){
var _ef5=curam.util.getTopmostWindow().dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
if(_ef5){
var _ef6=_ef5.selectedChildWidget.domNode.id;
return _ef6.substring(0,_ef6.length-4);
}else{
if(!_ef4){
throw new Error("curam.tab.getCurrentSectionId() - application section"+" tab container not found");
}
}
return null;
},inTabbedUI:function(){
return curam.tab.getCurrentSectionId(true)!=null;
},getTabContainerFromSectionID:function(_ef7){
var _ef8=_eed.byId(_ef7+"-stc");
if(!_ef8&&window.parent&&window.parent!=window){
_ef8=curam.util.getTopmostWindow().dijit.byId(_ef7+"-stc");
}
return _ef8;
},getTabWidgetId:function(tab){
return tab.id;
},getSelectedTabWidgetId:function(){
return curam.tab.getTabWidgetId(curam.tab.getSelectedTab());
},getContainerTab:function(node){
var _ef9=dijit.getEnclosingWidget(node);
if(_ef9&&!_ef9.tabDescriptor){
_ef9=curam.tab.getContainerTab(_ef9.domNode.parentNode);
}
if(!_ef9||!_ef9.tabDescriptor){
throw "Containing tab widget could not be found for node: "+node;
}
return _ef9;
},getContentPanelIframe:function(tab){
var _efa=tab?tab:curam.tab.getSelectedTab(),_efb=null;
if(_efa){
_efb=dojo.query("iframe",_efa.domNode).filter(function(item){
return _eee.get(item,"iscpiframe")=="true";
})[0];
}
return _efb?_efb:null;
},refreshMainContentPanel:function(tab){
var _efc=tab?tab:curam.tab.getSelectedTab();
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[_efc.domNode]);
var _efd=curam.tab.getContentPanelIframe(tab);
_efd.contentWindow.curam.util.publishRefreshEvent();
_efd.contentWindow.location.reload(false);
},getSmartPanelIframe:function(tab){
var _efe=tab?tab:curam.tab.getSelectedTab();
var _eff=dojo.query("iframe",_efe.domNode).filter(function(item){
return item.id==curam.tab.SMART_PANEL_IFRAME_ID;
})[0];
return _eff;
},unsubscribeOnTabClose:function(_f00,_f01){
curam.tab.toBeExecutedOnTabClose.push(function(_f02){
if(_f01==_f02){
dojo.unsubscribe(_f00);
return true;
}
return false;
});
},executeOnTabClose:function(func,_f03){
curam.tab.toBeExecutedOnTabClose.push(function(_f04){
if(_f03==_f04){
func();
return true;
}
return false;
});
},doExecuteOnTabClose:function(_f05){
var _f06=new Array();
for(var i=0;i<curam.tab.toBeExecutedOnTabClose.length;i++){
var func=curam.tab.toBeExecutedOnTabClose[i];
if(!func(_f05)){
_f06.push(func);
}
}
curam.tab.toBeExecutedOnTabClose=_f06;
},getHandlerForTab:function(_f07,_f08){
return function(_f09,_f0a){
if(_f0a==_f08){
_f07(_f09,_f08);
}else{
}
};
},getTabController:function(){
return curam.util.getTopmostWindow().curam.ui.UIController;
},initTabLinks:function(_f0b){
dojo.query("a").forEach(function(link){
if(link.href.indexOf("#")!=0&&link.href.indexOf("javascript:")!=0&&(link.href.indexOf("Page.do")>-1||link.href.indexOf("Frame.do")>-1)){
if(link.href.indexOf("&o3ctx")<0&&link.href.indexOf("?o3ctx")<0){
var _f0c=(link.href.indexOf("?")>-1)?"&":"?";
link.href+=_f0c+jsScreenContext.toRequestString();
}
}
});
elements=document.forms;
for(var i=0;i<elements.length;++i){
elem=elements[i];
var _f0d=dom.byId("o3ctx");
if(!_f0d){
var ctx=new curam.util.ScreenContext();
ctx.setContextBits("ACTION");
_eef.create("input",{"type":"hidden","name":"o3ctx","value":ctx.getValue()},elem);
}
_eef.create("input",{"type":"hidden","name":"o3prv","value":jsPageID},elem);
}
if(elements.length>0){
curam.util.getTopmostWindow().dojo.publish("curam.fireNextRequest",[]);
}
},initContent:function(_f0e,_f0f){
var _f10=dom.byId("content");
_ef0.remove(_f10,"hidden-panel");
return;
},setupSectionSelectionListener:function(){
dojo.subscribe(curam.tab.SECTION_TAB_CONTAINER_ID+"-selectChild",curam.tab.onSectionSelected);
},onSectionSelected:function(_f11){
if(_f11.curamDefaultPageID){
var _f12;
if(_f11.id.substring(_f11.id.length-4,_f11.id.length)=="-sbc"){
var _f13=_f11.id.substring(0,_f11.id.length-4);
_f12=curam.tab.getTabContainer(_f13);
}else{
_f12=_f11;
}
if(_f12&&_f12.getChildren().length==0){
curam.tab.getTabController().handleUIMPageID(_f11.curamDefaultPageID,true);
}
return true;
}
return false;
},setSectionDefaultPage:function(_f14,_f15){
var _f16=_eed.byId(_f14);
if(_f16){
_f16.curamDefaultPageID=_f15;
}else{
throw "curam.tab.setSectionDefaultPage() - cannot find section dijit ID:"+_f14;
}
},publishSmartPanelContentReady:function(){
var _f17="smartpanel.content.loaded";
var _f18=window.frameElement;
_f18.setAttribute("_SPContentLoaded","true");
curam.util.getTopmostWindow().dojo.publish(_f17,[_f18]);
}});
_ef1.register("curam/tab",curam.tab);
return curam.tab;
});
},"dojo/text":function(){
define(["./_base/kernel","require","./has","./request"],function(dojo,_f19,has,_f1a){
var _f1b;
if(1){
_f1b=function(url,sync,load){
_f1a(url,{sync:!!sync,headers:{"X-Requested-With":null}}).then(load);
};
}else{
if(_f19.getText){
_f1b=_f19.getText;
}else{
console.error("dojo/text plugin failed to load because loader does not support getText");
}
}
var _f1c={},_f1d=function(text){
if(text){
text=text.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _f1e=text.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_f1e){
text=_f1e[1];
}
}else{
text="";
}
return text;
},_f1f={},_f20={};
dojo.cache=function(_f21,url,_f22){
var key;
if(typeof _f21=="string"){
if(/\//.test(_f21)){
key=_f21;
_f22=url;
}else{
key=_f19.toUrl(_f21.replace(/\./g,"/")+(url?("/"+url):""));
}
}else{
key=_f21+"";
_f22=url;
}
var val=(_f22!=undefined&&typeof _f22!="string")?_f22.value:_f22,_f23=_f22&&_f22.sanitize;
if(typeof val=="string"){
_f1c[key]=val;
return _f23?_f1d(val):val;
}else{
if(val===null){
delete _f1c[key];
return null;
}else{
if(!(key in _f1c)){
_f1b(key,true,function(text){
_f1c[key]=text;
});
}
return _f23?_f1d(_f1c[key]):_f1c[key];
}
}
};
return {dynamic:true,normalize:function(id,_f24){
var _f25=id.split("!"),url=_f25[0];
return (/^\./.test(url)?_f24(url):url)+(_f25[1]?"!"+_f25[1]:"");
},load:function(id,_f26,load){
var _f27=id.split("!"),_f28=_f27.length>1,_f29=_f27[0],url=_f26.toUrl(_f27[0]),_f2a="url:"+url,text=_f1f,_f2b=function(text){
load(_f28?_f1d(text):text);
};
if(_f29 in _f1c){
text=_f1c[_f29];
}else{
if(_f26.cache&&_f2a in _f26.cache){
text=_f26.cache[_f2a];
}else{
if(url in _f1c){
text=_f1c[url];
}
}
}
if(text===_f1f){
if(_f20[url]){
_f20[url].push(_f2b);
}else{
var _f2c=_f20[url]=[_f2b];
_f1b(url,!_f26.async,function(text){
_f1c[_f29]=_f1c[url]=text;
for(var i=0;i<_f2c.length;){
_f2c[i++](text);
}
delete _f20[url];
});
}
}else{
_f2b(text);
}
}};
});
},"curam/i18n":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.i18n",{values:{},set:function(key,_f2d){
curam.i18n.values[key]=_f2d;
},get:function(key){
return curam.i18n.values[key];
}});
return curam.i18n;
});
},"dojo/i18n":function(){
define(["./_base/kernel","require","./has","./_base/array","./_base/config","./_base/lang","./_base/xhr","./json","module"],function(dojo,_f2e,has,_f2f,_f30,lang,xhr,json,_f31){
has.add("dojo-preload-i18n-Api",1);
1||has.add("dojo-v1x-i18n-Api",1);
var _f32=dojo.i18n={},_f33=/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,_f34=function(root,_f35,_f36,_f37){
for(var _f38=[_f36+_f37],_f39=_f35.split("-"),_f3a="",i=0;i<_f39.length;i++){
_f3a+=(_f3a?"-":"")+_f39[i];
if(!root||root[_f3a]){
_f38.push(_f36+_f3a+"/"+_f37);
_f38.specificity=_f3a;
}
}
return _f38;
},_f3b={},_f3c=function(_f3d,_f3e,_f3f){
_f3f=_f3f?_f3f.toLowerCase():dojo.locale;
_f3d=_f3d.replace(/\./g,"/");
_f3e=_f3e.replace(/\./g,"/");
return (/root/i.test(_f3f))?(_f3d+"/nls/"+_f3e):(_f3d+"/nls/"+_f3f+"/"+_f3e);
},_f40=dojo.getL10nName=function(_f41,_f42,_f43){
return _f41=_f31.id+"!"+_f3c(_f41,_f42,_f43);
},_f44=function(_f45,_f46,_f47,_f48,_f49,load){
_f45([_f46],function(root){
var _f4a=lang.clone(root.root||root.ROOT),_f4b=_f34(!root._v1x&&root,_f49,_f47,_f48);
_f45(_f4b,function(){
for(var i=1;i<_f4b.length;i++){
_f4a=lang.mixin(lang.clone(_f4a),arguments[i]);
}
var _f4c=_f46+"/"+_f49;
_f3b[_f4c]=_f4a;
_f4a.$locale=_f4b.specificity;
load();
});
});
},_f4d=function(id,_f4e){
return /^\./.test(id)?_f4e(id):id;
},_f4f=function(_f50){
var list=_f30.extraLocale||[];
list=lang.isArray(list)?list:[list];
list.push(_f50);
return list;
},load=function(id,_f51,load){
if(has("dojo-preload-i18n-Api")){
var _f52=id.split("*"),_f53=_f52[1]=="preload";
if(_f53){
if(!_f3b[id]){
_f3b[id]=1;
_f54(_f52[2],json.parse(_f52[3]),1,_f51);
}
load(1);
}
if(_f53||_f55(id,_f51,load)){
return;
}
}
var _f56=_f33.exec(id),_f57=_f56[1]+"/",_f58=_f56[5]||_f56[4],_f59=_f57+_f58,_f5a=(_f56[5]&&_f56[4]),_f5b=_f5a||dojo.locale||"",_f5c=_f59+"/"+_f5b,_f5d=_f5a?[_f5b]:_f4f(_f5b),_f5e=_f5d.length,_f5f=function(){
if(!--_f5e){
load(lang.delegate(_f3b[_f5c]));
}
};
_f2f.forEach(_f5d,function(_f60){
var _f61=_f59+"/"+_f60;
if(has("dojo-preload-i18n-Api")){
_f62(_f61);
}
if(!_f3b[_f61]){
_f44(_f51,_f59,_f57,_f58,_f60,_f5f);
}else{
_f5f();
}
});
};
if(has("dojo-unit-tests")){
var _f63=_f32.unitTests=[];
}
if(has("dojo-preload-i18n-Api")||1){
var _f64=_f32.normalizeLocale=function(_f65){
var _f66=_f65?_f65.toLowerCase():dojo.locale;
return _f66=="root"?"ROOT":_f66;
},isXd=function(mid,_f67){
return (1&&1)?_f67.isXdUrl(_f2e.toUrl(mid+".js")):true;
},_f68=0,_f69=[],_f54=_f32._preloadLocalizations=function(_f6a,_f6b,_f6c,_f6d){
_f6d=_f6d||_f2e;
function _f6e(mid,_f6f){
if(isXd(mid,_f6d)||_f6c){
_f6d([mid],_f6f);
}else{
_f89([mid],_f6f,_f6d);
}
};
function _f70(_f71,func){
var _f72=_f71.split("-");
while(_f72.length){
if(func(_f72.join("-"))){
return;
}
_f72.pop();
}
func("ROOT");
};
function _f73(){
_f68++;
};
function _f74(){
--_f68;
while(!_f68&&_f69.length){
load.apply(null,_f69.shift());
}
};
function _f75(path,name,loc,_f76){
return _f76.toAbsMid(path+name+"/"+loc);
};
function _f77(_f78){
_f78=_f64(_f78);
_f70(_f78,function(loc){
if(_f2f.indexOf(_f6b,loc)>=0){
var mid=_f6a.replace(/\./g,"/")+"_"+loc;
_f73();
_f6e(mid,function(_f79){
for(var p in _f79){
var _f7a=_f79[p],_f7b=p.match(/(.+)\/([^\/]+)$/),_f7c,_f7d;
if(!_f7b){
continue;
}
_f7c=_f7b[2];
_f7d=_f7b[1]+"/";
if(!_f7a._localized){
continue;
}
var _f7e;
if(loc==="ROOT"){
var root=_f7e=_f7a._localized;
delete _f7a._localized;
root.root=_f7a;
_f3b[_f2e.toAbsMid(p)]=root;
}else{
_f7e=_f7a._localized;
_f3b[_f75(_f7d,_f7c,loc,_f2e)]=_f7a;
}
if(loc!==_f78){
function _f7f(_f80,_f81,_f82,_f83){
var _f84=[],_f85=[];
_f70(_f78,function(loc){
if(_f83[loc]){
_f84.push(_f2e.toAbsMid(_f80+loc+"/"+_f81));
_f85.push(_f75(_f80,_f81,loc,_f2e));
}
});
if(_f84.length){
_f73();
_f6d(_f84,function(){
for(var i=_f84.length-1;i>=0;i--){
_f82=lang.mixin(lang.clone(_f82),arguments[i]);
_f3b[_f85[i]]=_f82;
}
_f3b[_f75(_f80,_f81,_f78,_f2e)]=lang.clone(_f82);
_f74();
});
}else{
_f3b[_f75(_f80,_f81,_f78,_f2e)]=_f82;
}
};
_f7f(_f7d,_f7c,_f7a,_f7e);
}
}
_f74();
});
return true;
}
return false;
});
};
_f77();
_f2f.forEach(dojo.config.extraLocale,_f77);
},_f55=function(id,_f86,load){
if(_f68){
_f69.push([id,_f86,load]);
}
return _f68;
},_f62=function(){
};
}
if(1){
var _f87={},_f88=new Function("__bundle","__checkForLegacyModules","__mid","__amdValue","var define = function(mid, factory){define.called = 1; __amdValue.result = factory || mid;},"+"\t   require = function(){define.called = 1;};"+"try{"+"define.called = 0;"+"eval(__bundle);"+"if(define.called==1)"+"return __amdValue;"+"if((__checkForLegacyModules = __checkForLegacyModules(__mid)))"+"return __checkForLegacyModules;"+"}catch(e){}"+"try{"+"return eval('('+__bundle+')');"+"}catch(e){"+"return e;"+"}"),_f89=function(deps,_f8a,_f8b){
var _f8c=[];
_f2f.forEach(deps,function(mid){
var url=_f8b.toUrl(mid+".js");
function load(text){
var _f8d=_f88(text,_f62,mid,_f87);
if(_f8d===_f87){
_f8c.push(_f3b[url]=_f87.result);
}else{
if(_f8d instanceof Error){
console.error("failed to evaluate i18n bundle; url="+url,_f8d);
_f8d={};
}
_f8c.push(_f3b[url]=(/nls\/[^\/]+\/[^\/]+$/.test(url)?_f8d:{root:_f8d,_v1x:1}));
}
};
if(_f3b[url]){
_f8c.push(_f3b[url]);
}else{
var _f8e=_f8b.syncLoadNls(mid);
if(!_f8e){
_f8e=_f62(mid.replace(/nls\/([^\/]*)\/([^\/]*)$/,"nls/$2/$1"));
}
if(_f8e){
_f8c.push(_f8e);
}else{
if(!xhr){
try{
_f8b.getText(url,true,load);
}
catch(e){
_f8c.push(_f3b[url]={});
}
}else{
xhr.get({url:url,sync:true,load:load,error:function(){
_f8c.push(_f3b[url]={});
}});
}
}
}
});
_f8a&&_f8a.apply(null,_f8c);
};
_f62=function(_f8f){
for(var _f90,_f91=_f8f.split("/"),_f92=dojo.global[_f91[0]],i=1;_f92&&i<_f91.length-1;_f92=_f92[_f91[i++]]){
}
if(_f92){
_f90=_f92[_f91[i]];
if(!_f90){
_f90=_f92[_f91[i].replace(/-/g,"_")];
}
if(_f90){
_f3b[_f8f]=_f90;
}
}
return _f90;
};
_f32.getLocalization=function(_f93,_f94,_f95){
var _f96,_f97=_f3c(_f93,_f94,_f95);
load(_f97,(!isXd(_f97,_f2e)?function(deps,_f98){
_f89(deps,_f98,_f2e);
}:_f2e),function(_f99){
_f96=_f99;
});
return _f96;
};
if(has("dojo-unit-tests")){
_f63.push(function(doh){
doh.register("tests.i18n.unit",function(t){
var _f9a;
_f9a=_f88("{prop:1}",_f62,"nonsense",_f87);
t.is({prop:1},_f9a);
t.is(undefined,_f9a[1]);
_f9a=_f88("({prop:1})",_f62,"nonsense",_f87);
t.is({prop:1},_f9a);
t.is(undefined,_f9a[1]);
_f9a=_f88("{'prop-x':1}",_f62,"nonsense",_f87);
t.is({"prop-x":1},_f9a);
t.is(undefined,_f9a[1]);
_f9a=_f88("({'prop-x':1})",_f62,"nonsense",_f87);
t.is({"prop-x":1},_f9a);
t.is(undefined,_f9a[1]);
_f9a=_f88("define({'prop-x':1})",_f62,"nonsense",_f87);
t.is(_f87,_f9a);
t.is({"prop-x":1},_f87.result);
_f9a=_f88("define('some/module', {'prop-x':1})",_f62,"nonsense",_f87);
t.is(_f87,_f9a);
t.is({"prop-x":1},_f87.result);
_f9a=_f88("this is total nonsense and should throw an error",_f62,"nonsense",_f87);
t.is(_f9a instanceof Error,true);
});
});
}
}
return lang.mixin(_f32,{dynamic:true,normalize:_f4d,load:load,cache:_f3b,getL10nName:_f40});
});
},"curam/widget/ProgressSpinner":function(){
define(["dojox/widget/Standby","dojo/dom-construct","curam/debug","curam/util","curam/tab"],function(_f9b,_f9c,_f9d){
var _f9e=dojo.setObject("curam.widget.ProgressSpinner",{PROGRESS_WIDGET_ENABLED:true,PROGRESS_WIDGET_DEFAULT_THRESHOLD:2000,PROGRESS_WIDGET_COLOR:"white",PROGRESS_WIDGET_TITLE:"Please wait...",PROGRESS_WIDGET_TIMEOUT_MAX:90000,LOAD_MASK_TIMEOUT:15000,_spinnerTokens:[],_launcherTokens:[],_forcedTimeouts:[],_enabled:false,_eHandler:false,_tDojo:null,init:function(){
_f9d.log("PROGRESS SPINNER: isEnabled: "+_f9e.PROGRESS_WIDGET_ENABLED);
_f9d.log("PROGRESS SPINNER: threshold: "+_f9e.PROGRESS_WIDGET_DEFAULT_THRESHOLD);
_f9d.log("PROGRESS SPINNER: max timeout: "+_f9e.PROGRESS_WIDGET_TIMEOUT_MAX);
_f9e._enabled=_f9e._isEnabled();
_f9e.tDojo.subscribe("/curam/progress/unload",function(){
_f9e._enabled&&_f9e.dismissSpinner();
});
_f9e.tDojo.subscribe("/curam/progress/display",function(_f9f,_fa0){
if(_f9e._enabled){
var _fa1=_f9e.dismissSpinner();
_f9f&&_f9e.launch(_f9f,(_fa1?0:_fa0));
}
});
},_isEnabled:function(){
return (_f9e.PROGRESS_WIDGET_ENABLED&&(_f9e.PROGRESS_WIDGET_ENABLED===true||_f9e.PROGRESS_WIDGET_ENABLED.toString().toLowerCase()==="true"));
},launch:function(_fa2,_fa3){
var _fa4=_f9e._getSpinner(_fa2);
_fa4&&_f9e.show(_fa4,_fa3);
},getSpinner:function(_fa5){
return _f9e._getSpinner(_fa5);
},_getSpinner:function(_fa6){
if(_fa6!=null&&_f9e._enabled){
_f9d.log(_f9d.getProperty("curam.widget.ProgressSpinner.load",[_fa6]));
var _fa7=dojo.isString(_fa6)?_f9e.adjustTarget(_fa6):_fa6;
var _fa8=new _f9b({target:_fa7,centerIndicator:"text",zIndex:999999999,color:_f9e.PROGRESS_WIDGET_COLOR,text:"<div class='curam-spinner curam-h1'></div>",title:_f9e.PROGRESS_WIDGET_TITLE,duration:250,onHide:function(){
curam.util.getTopmostWindow().document.querySelectorAll(".standby").forEach(_f9c.destroy);
}});
_fa8.domNode.classList.add("standby");
curam.util.getTopmostWindow().document.body.appendChild(_fa8.domNode);
_f9e._spinnerTokens.push(_fa8);
_fa8.startup();
return _fa8;
}
return null;
},adjustTarget:function(_fa9){
var _faa=_fa9;
var wid=curam.tab.getSelectedTabWidgetId();
if(wid){
var _fab=curam.util.getTopmostWindow().dijit.byId(wid);
_fab&&(_faa=_fab.containerNode);
}
return _faa;
},show:function(_fac,_fad){
!!_fad&&(_fad=_f9e.PROGRESS_WIDGET_DEFAULT_THRESHOLD);
var tOut=setTimeout(function(){
_fac.show();
var _fae=setTimeout(function(){
_f9e.tDojo.publish("/curam/progress/unload");
},_f9e.PROGRESS_WIDGET_TIMEOUT_MAX);
_f9e._forcedTimeouts.push(_fae);
},_fad);
_f9e._launcherTokens.push(tOut);
return tOut;
},hide:function(_faf){
_faf.hide();
},dismissSpinner:function(){
return _f9e._dismissSpinner();
},_dismissSpinner:function(){
var res=_f9e._spinnerTokens.length;
_f9e._forcedTimeouts.forEach(function(_fb0){
clearTimeout(_fb0);
});
_f9e._forcedTimeouts.splice(0,_f9e._forcedTimeouts.length);
_f9e._launcherTokens.forEach(function(_fb1){
clearTimeout(_fb1);
});
_f9e._launcherTokens.splice(0,_f9e._launcherTokens.length);
_f9e._spinnerTokens.forEach(function(_fb2){
_fb2.hide();
});
_f9e._spinnerTokens.splice(0,_f9e._spinnerTokens.length);
curam.debug.log("PROGRESS SPINNER: "+_f9d.getProperty("curam.widget.ProgressSpinner.unload"));
return (res>0);
}});
_f9e.tDojo=curam.util.getTopmostWindow().dojo;
if(!_f9e.eHandler){
_f9e.tDojo.subscribe("/curam/progress/unload",function(){
_f9e._enabled&&_f9e.dismissSpinner();
});
_f9e.eHandler=_f9e.tDojo.subscribe("/curam/progress/display",function(_fb3,_fb4){
if(_f9e._enabled){
var _fb5=_f9e.dismissSpinner();
_fb3&&_f9e.launch(_fb3,(_fb5?0:_fb4));
}
});
}
return curam.widget.ProgressSpinner;
});
},"dojo/store/Cache":function(){
define(["../_base/lang","../when"],function(lang,when){
var _fb6=function(_fb7,_fb8,_fb9){
_fb9=_fb9||{};
return lang.delegate(_fb7,{query:function(_fba,_fbb){
var _fbc=_fb7.query(_fba,_fbb);
_fbc.forEach(function(_fbd){
if(!_fb9.isLoaded||_fb9.isLoaded(_fbd)){
_fb8.put(_fbd);
}
});
return _fbc;
},queryEngine:_fb7.queryEngine||_fb8.queryEngine,get:function(id,_fbe){
return when(_fb8.get(id),function(_fbf){
return _fbf||when(_fb7.get(id,_fbe),function(_fc0){
if(_fc0){
_fb8.put(_fc0,{id:id});
}
return _fc0;
});
});
},add:function(_fc1,_fc2){
return when(_fb7.add(_fc1,_fc2),function(_fc3){
_fb8.add(_fc3&&typeof _fc3=="object"?_fc3:_fc1,_fc2);
return _fc3;
});
},put:function(_fc4,_fc5){
_fb8.remove((_fc5&&_fc5.id)||this.getIdentity(_fc4));
return when(_fb7.put(_fc4,_fc5),function(_fc6){
_fb8.put(_fc6&&typeof _fc6=="object"?_fc6:_fc4,_fc5);
return _fc6;
});
},remove:function(id,_fc7){
return when(_fb7.remove(id,_fc7),function(_fc8){
return _fb8.remove(id,_fc7);
});
},evict:function(id){
return _fb8.remove(id);
}});
};
lang.setObject("dojo.store.Cache",_fb6);
return _fb6;
});
},"dojo/data/util/simpleFetch":function(){
define(["../../_base/lang","../../_base/kernel","./sorter"],function(lang,_fc9,_fca){
var _fcb={};
lang.setObject("dojo.data.util.simpleFetch",_fcb);
_fcb.errorHandler=function(_fcc,_fcd){
if(_fcd.onError){
var _fce=_fcd.scope||_fc9.global;
_fcd.onError.call(_fce,_fcc,_fcd);
}
};
_fcb.fetchHandler=function(_fcf,_fd0){
var _fd1=_fd0.abort||null,_fd2=false,_fd3=_fd0.start?_fd0.start:0,_fd4=(_fd0.count&&(_fd0.count!==Infinity))?(_fd3+_fd0.count):_fcf.length;
_fd0.abort=function(){
_fd2=true;
if(_fd1){
_fd1.call(_fd0);
}
};
var _fd5=_fd0.scope||_fc9.global;
if(!_fd0.store){
_fd0.store=this;
}
if(_fd0.onBegin){
_fd0.onBegin.call(_fd5,_fcf.length,_fd0);
}
if(_fd0.sort){
_fcf.sort(_fca.createSortFunction(_fd0.sort,this));
}
if(_fd0.onItem){
for(var i=_fd3;(i<_fcf.length)&&(i<_fd4);++i){
var item=_fcf[i];
if(!_fd2){
_fd0.onItem.call(_fd5,item,_fd0);
}
}
}
if(_fd0.onComplete&&!_fd2){
var _fd6=null;
if(!_fd0.onItem){
_fd6=_fcf.slice(_fd3,_fd4);
}
_fd0.onComplete.call(_fd5,_fd6,_fd0);
}
};
_fcb.fetch=function(_fd7){
_fd7=_fd7||{};
if(!_fd7.store){
_fd7.store=this;
}
this._fetchItems(_fd7,lang.hitch(this,"fetchHandler"),lang.hitch(this,"errorHandler"));
return _fd7;
};
return _fcb;
});
},"curam/util":function(){
define(["dojo/dom","dijit/registry","dojo/dom-construct","dojo/ready","dojo/_base/window","dojo/dom-style","dojo/_base/array","dojo/dom-class","dojo/topic","dojo/_base/event","dojo/query","dojo/Deferred","dojo/has","dojo/_base/unload","dojo/dom-geometry","dojo/_base/json","dojo/dom-attr","dojo/_base/lang","dojo/on","dijit/_BidiSupport","curam/define","curam/debug","curam/util/RuntimeContext","curam/util/Constants","dojo/_base/sniff","cm/_base/_dom","curam/util/ResourceBundle","dojo/NodeList-traverse"],function(dom,_fd8,_fd9,_fda,_fdb,_fdc,_fdd,_fde,_fdf,_fe0,_fe1,_fe2,has,_fe3,geom,json,attr,lang,on,bidi,_fe4,_fe5,_fe6,_fe7,_fe8,_fe9,_fea){
curam.define.singleton("curam.util",{PREVENT_CACHE_FLAG:"o3pc",INFORMATIONAL_MSGS_STORAGE_ID:"__informationals__",ERROR_MESSAGES_CONTAINER:"error-messages-container",ERROR_MESSAGES_LIST:"error-messages",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",PAGE_ID_PREFIX:"Curam_",msgLocaleSelectorActionPage:"$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",GENERIC_ERROR_MODAL_MAP:{},wrappersMap:[],lastOpenedTabButton:false,tabButtonClicked:false,secureURLsExemptParamName:"suep",secureURLsExemptParamsPrefix:"spm",secureURLsHashTokenParam:"suhtp",setTabButtonClicked:function(_feb){
curam.util.getTopmostWindow().curam.util.tabButtonClicked=_feb;
},getTabButtonClicked:function(){
var _fec=curam.util.getTopmostWindow().curam.util.tabButtonClicked;
curam.util.getTopmostWindow().curam.util.tabButtonClicked=false;
return _fec;
},setLastOpenedTabButton:function(_fed){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=_fed;
},getLastOpenedTabButton:function(){
var _fee=curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=false;
return _fee;
},insertCssText:function(_fef,_ff0){
var id=_ff0?_ff0:"_runtime_stylesheet_";
var _ff1=dom.byId(id);
var _ff2;
if(_ff1){
if(_ff1.styleSheet){
_fef=_ff1.styleSheet.cssText+_fef;
_ff2=_ff1;
_ff2.setAttribute("id","_nodeToRm");
}else{
_ff1.appendChild(document.createTextNode(_fef));
return;
}
}
var pa=document.getElementsByTagName("head")[0];
_ff1=_fd9.create("style",{type:"text/css",id:id});
if(_ff1.styleSheet){
_ff1.styleSheet.cssText=_fef;
}else{
_ff1.appendChild(document.createTextNode(_fef));
}
pa.appendChild(_ff1);
if(_ff2){
_ff2.parentNode.removeChild(_ff2);
}
},fireRefreshTreeEvent:function(){
if(dojo.global.parent&&dojo.global.parent.amIFrame){
var wpl=dojo.global.parent.loader;
}
if(wpl&&wpl.dojo){
wpl.dojo.publish("refreshTree");
}
},firePageSubmittedEvent:function(_ff3){
require(["curam/tab"],function(){
var _ff4=curam.tab.getSelectedTab();
if(_ff4){
var _ff5=curam.tab.getTabWidgetId(_ff4);
var _ff6=curam.util.getTopmostWindow();
var ctx=(_ff3=="dialog")?curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG:curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
_ff6.curam.util.Refresh.getController(_ff5).pageSubmitted(dojo.global.jsPageID,ctx);
_ff6.dojo.publish("/curam/main-content/page/submitted",[dojo.global.jsPageID,_ff5]);
}else{
curam.debug.log("/curam/main-content/page/submitted: "+_fe5.getProperty("curam.util.no.open"));
}
});
},fireTabOpenedEvent:function(_ff7){
curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",[dojo.global.jsPageID,_ff7]);
},setupSubmitEventPublisher:function(){
_fda(function(){
var form=dom.byId("mainForm");
if(form){
curam.util.connect(form,"onsubmit",function(){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[curam.util.PAGE_ID_PREFIX+dojo.global.jsPageID]);
curam.util.firePageSubmittedEvent("main-content");
});
}
});
},getScrollbar:function(){
var _ff8=_fd9.create("div",{},_fdb.body());
_fdc.set(_ff8,{width:"100px",height:"100px",overflow:"scroll",position:"absolute",top:"-300px",left:"0px"});
var test=_fd9.create("div",{},_ff8);
_fdc.set(test,{width:"400px",height:"400px"});
var _ff9=_ff8.offsetWidth-_ff8.clientWidth;
_fd9.destroy(_ff8);
return {width:_ff9};
},isModalWindow:function(){
return (dojo.global.curamModal===undefined)?false:true;
},isExitingIEGScriptInModalWindow:function(_ffa){
return (curam.util.getTopmostWindow().exitingIEGScript===undefined)?false:true;
},setExitingIEGScriptInModalWindowVariable:function(){
curam.util.getTopmostWindow().exitingIEGScript=true;
},getTopmostWindow:function(){
if(typeof (dojo.global._curamTopmostWindow)=="undefined"){
var _ffb=dojo.global;
if(_ffb.__extAppTopWin){
dojo.global._curamTopmostWindow=_ffb;
}else{
while(_ffb.parent!=_ffb){
_ffb=_ffb.parent;
if(_ffb.__extAppTopWin){
break;
}
}
dojo.global._curamTopmostWindow=_ffb;
}
}
if(dojo.global._curamTopmostWindow.location.href.indexOf("AppController.do")<0&&typeof (dojo.global._curamTopmostWindow.__extAppTopWin)=="undefined"){
curam.debug.log(_fe5.getProperty("curam.util.wrong.window")+dojo.global._curamTopmostWindow.location.href);
}
return dojo.global._curamTopmostWindow;
},getUrlParamValue:function(url,_ffc){
var qPos=url.indexOf("?");
if(qPos<0){
return null;
}
var _ffd=url.substring(qPos+1,url.length);
function _ffe(_fff){
var _1000=_ffd.split(_fff);
_ffc+="=";
for(var i=0;i<_1000.length;i++){
if(_1000[i].indexOf(_ffc)==0){
return _1000[i].split("=")[1];
}
}
};
return _ffe("&")||_ffe("");
},addUrlParam:function(href,_1001,_1002,_1003){
var hasQ=href.indexOf("?")>-1;
var _1004=_1003?_1003:"undefined";
if(!hasQ||(_1004==false)){
return href+(hasQ?"&":"?")+_1001+"="+_1002;
}else{
var parts=href.split("?");
href=parts[0]+"?"+_1001+"="+_1002+(parts[1]!=""?("&"+parts[1]):"");
return href;
}
},replaceUrlParam:function(href,_1005,_1006){
href=curam.util.removeUrlParam(href,_1005);
return curam.util.addUrlParam(href,_1005,_1006);
},removeUrlParam:function(url,_1007,_1008){
var qPos=url.indexOf("?");
if(qPos<0){
return url;
}
if(url.indexOf(_1007+"=")<0){
return url;
}
var _1009=url.substring(qPos+1,url.length);
var _100a=_1009.split("&");
var value;
var _100b,_100c;
for(var i=0;i<_100a.length;i++){
if(_100a[i].indexOf(_1007+"=")==0){
_100c=false;
if(_1008){
_100b=_100a[i].split("=");
if(_100b.length>1){
if(_100b[1]==_1008){
_100c=true;
}
}else{
if(_1008==""){
_100c=true;
}
}
}else{
_100c=true;
}
if(_100c){
_100a.splice(i,1);
i--;
}
}
}
return url.substring(0,qPos+1)+_100a.join("&");
},stripHash:function(url){
var idx=url.indexOf("#");
if(idx<0){
return url;
}
return url.substring(0,url);
},isSameUrl:function(href1,href2,rtc){
if(!href2){
href2=rtc.getHref();
}
if(href1.indexOf("#")==0){
return true;
}
var _100d=href1.indexOf("#");
if(_100d>-1){
if(_100d==0){
return true;
}
var _100e=href1.split("#");
var _100f=href2.indexOf("#");
if(_100f>-1){
if(_100f==0){
return true;
}
href2=href2.split("#")[0];
}
return _100e[0]==href2;
}
var _1010=function(url){
var idx=url.lastIndexOf("Page.do");
var len=7;
if(idx<0){
idx=url.lastIndexOf("Action.do");
len=9;
}
if(idx<0){
idx=url.lastIndexOf("Frame.do");
len=8;
}
if(idx>-1&&idx==url.length-len){
return url.substring(0,idx);
}
return url;
};
var rp=curam.util.removeUrlParam;
var here=curam.util.stripHash(rp(href2,curam.util.Constants.RETURN_PAGE_PARAM));
var there=curam.util.stripHash(rp(href1,curam.util.Constants.RETURN_PAGE_PARAM));
var _1011=there.split("?");
var _1012=here.split("?");
_1012[0]=_1010(_1012[0]);
_1011[0]=_1010(_1011[0]);
var _1013=(_1012[0]==_1011[0]||_1012[0].match(_1011[0]+"$")==_1011[0]);
if(!_1013){
return false;
}
if(_1012.length==1&&_1011.length==1&&_1013){
return true;
}else{
var _1014;
var _1015;
if(typeof _1012[1]!="undefined"&&_1012[1]!=""){
_1014=_1012[1].split("&");
}else{
_1014=new Array();
}
if(typeof _1011[1]!="undefined"&&_1011[1]!=""){
_1015=_1011[1].split("&");
}else{
_1015=new Array();
}
curam.debug.log("curam.util.isSameUrl: paramsHere "+_fe5.getProperty("curam.util.before")+_1014.length);
_1014=_fdd.filter(_1014,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_fe5.getProperty("curam.util.after")+_1014.length);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_fe5.getProperty("curam.util.before")+_1015.length);
_1015=_fdd.filter(_1015,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_fe5.getProperty("curam.util.after")+_1015.length);
if(_1014.length!=_1015.length){
return false;
}
var _1016={};
var param;
for(var i=0;i<_1014.length;i++){
param=_1014[i].split("=");
param[0]=decodeURIComponent(param[0]);
param[1]=decodeURIComponent(param[1]);
_1016[param[0]]=param[1];
}
for(var i=0;i<_1015.length;i++){
param=_1015[i].split("=");
param[0]=decodeURIComponent(param[0]);
param[1]=decodeURIComponent(param[1]);
if(_1016[param[0]]!=param[1]){
curam.debug.log(_fe5.getProperty("curam.util.no.match",[param[0],param[1],_1016[param[0]]]));
return false;
}
}
}
return true;
},isNotCDEJParam:function(_1017){
return !((_1017.charAt(0)=="o"&&_1017.charAt(1)=="3")||(_1017.charAt(0)=="_"&&_1017.charAt(1)=="_"&&_1017.charAt(2)=="o"&&_1017.charAt(3)=="3"));
},setAttributes:function(node,map){
for(var x in map){
node.setAttribute(x,map[x]);
}
},invalidatePage:function(){
curam.PAGE_INVALIDATED=true;
var _1018=dojo.global.dialogArguments?dojo.global.dialogArguments[0]:opener;
if(_1018&&_1018!=dojo.global){
try{
_1018.curam.util.invalidatePage();
}
catch(e){
curam.debug.log(_fe5.getProperty("curam.util.error"),e);
}
}
},redirectWindow:function(href,force,_1019){
var rtc=new curam.util.RuntimeContext(dojo.global);
var _101a=function(_101b,_101c,href,_101d,_101e){
curam.util.getFrameRoot(_101b,_101c).curam.util.redirectContentPanel(href,_101d,_101e);
};
curam.util._doRedirectWindow(href,force,_1019,dojo.global.jsScreenContext,rtc,curam.util.publishRefreshEvent,_101a);
},_doRedirectWindow:function(href,force,_101f,_1020,rtc,_1021,_1022){
if(href&&curam.util.isActionPage(href)&&!curam.util.LOCALE_REFRESH){
curam.debug.log(_fe5.getProperty("curam.util.stopping"),href);
return;
}
var rpl=curam.util.replaceUrlParam;
var _1023=_1020.hasContextBits("TREE")||_1020.hasContextBits("AGENDA")||_1020.hasContextBits("ORG_TREE");
if(curam.util.LOCALE_REFRESH){
curam.util.publishRefreshEvent();
curam.util.getTopmostWindow().location.reload();
return;
}else{
if(curam.util.FORCE_REFRESH){
href=rpl(rtc.getHref(),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(curam.util.isModalWindow()||_1023){
_1021();
dojo.global.location.href=href;
}else{
if(_1020.hasContextBits("LIST_ROW_INLINE_PAGE")||_1020.hasContextBits("NESTED_UIM")){
curam.util._handleInlinePageRefresh(href);
}else{
_1021();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
require(["curam/tab"],function(){
_1022(dojo.global,curam.tab.getTabController().ROOT_OBJ,href,true,true);
});
}
}
}
return;
}
}
var u=curam.util;
var rtc=new curam.util.RuntimeContext(dojo.global);
if(!_1023&&!force&&!curam.PAGE_INVALIDATED&&u.isSameUrl(href,null,rtc)){
return;
}
if(curam.util.isModalWindow()||_1023){
href=rpl(rpl(href,"o3frame","modal"),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
var form=_fd9.create("form",{action:href,method:"POST"});
if(!_1023){
if(!dom.byId("o3ctx")){
form.action=curam.util.removeUrlParam(form.action,"o3ctx");
var _1024=_fd9.create("input",{type:"hidden",id:"o3ctx",name:"o3ctx",value:_1020.getValue()},form);
}
_fdb.body().appendChild(form);
_1021();
form.submit();
}
if(!_101f){
if(_1023){
curam.util.redirectFrame(href);
}
}
}else{
var _1025=sessionStorage.getItem("launchWordEdit");
if(!_1025&&(_1020.hasContextBits("LIST_ROW_INLINE_PAGE")||_1020.hasContextBits("NESTED_UIM"))){
curam.util._handleInlinePageRefresh(href);
}else{
if(_1025){
sessionStorage.removeItem("launchWordEdit");
}
_1021();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
if(_1020.hasContextBits("EXTAPP")){
var _1026=window.top;
_1026.dijit.byId("curam-app").updateMainContentIframe(href);
}else{
require(["curam/tab"],function(){
curam.util.getFrameRoot(dojo.global,curam.tab.getTabController().ROOT_OBJ).curam.util.redirectContentPanel(href,force);
});
}
}
}
}
},_handleInlinePageRefresh:function(href){
curam.debug.log(_fe5.getProperty("curam.util.closing.modal"),href);
var _1027=new curam.ui.PageRequest(href);
require(["curam/tab"],function(){
curam.tab.getTabController().checkPage(_1027,function(_1028){
curam.util.publishRefreshEvent();
window.location.reload(false);
});
});
},redirectContentPanel:function(url,_1029,_102a){
require(["curam/tab"],function(){
var _102b=curam.tab.getContentPanelIframe();
var _102c=url;
if(_102b!=null){
var rpu=curam.util.Constants.RETURN_PAGE_PARAM;
var _102d=null;
if(url.indexOf(rpu+"=")>=0){
curam.debug.log("curam.util.redirectContentPanel: "+_fe5.getProperty("curam.util.rpu"));
_102d=decodeURIComponent(curam.util.getUrlParamValue(url,rpu));
}
if(_102d){
_102d=curam.util.removeUrlParam(_102d,rpu);
_102c=curam.util.replaceUrlParam(url,rpu,encodeURIComponent(_102d));
}
}
var _102e=new curam.ui.PageRequest(_102c);
if(_1029){
_102e.forceLoad=true;
}
if(_102a){
_102e.justRefresh=true;
}
curam.tab.getTabController().handlePageRequest(_102e);
});
},redirectFrame:function(href){
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
var _102f=curam.util.getFrameRoot(dojo.global,"wizard").targetframe;
_102f.curam.util.publishRefreshEvent();
_102f.location.href=href;
}else{
if(dojo.global.jsScreenContext.hasContextBits("ORG_TREE")){
var _102f=curam.util.getFrameRoot(dojo.global,"orgTreeRoot");
_102f.curam.util.publishRefreshEvent();
_102f.dojo.publish("orgTree.refreshContent",[href]);
}else{
var _1030=curam.util.getFrameRoot(dojo.global,"iegtree");
var _1031=_1030.navframe||_1030.frames[0];
var _1032=_1030.contentframe||_1030.frames["contentframe"];
_1032.curam.util.publishRefreshEvent();
if(curam.PAGE_INVALIDATED||_1031.curam.PAGE_INVALIDATED){
var _1033=curam.util.modifyUrlContext(href,"ACTION");
_1032.location.href=_1033;
}else{
_1032.location.href=href;
}
}
}
return true;
},publishRefreshEvent:function(){
_fdf.publish("/curam/page/refresh");
},openGenericErrorModalDialog:function(_1034,_1035,_1036,_1037,_1038){
var _1039=curam.util.getTopmostWindow();
_1039.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_1034,"titleInfo":_1035,"msg":_1036,"msgPlaceholder":_1037,"errorModal":_1038,"hasCancelButton":false};
var url="generic-modal-error.jspx";
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=true;
curam.util.openModalDialog({href:encodeURI(url)},_1034);
},openGenericErrorModalDialogYesNo:function(_103a,_103b,_103c){
var sc=dojo.global.jsScreenContext;
var _103d=curam.util.getTopmostWindow();
sc.addContextBits("MODAL");
_103d.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_103a,"titleInfo":_103b,"msg":_103c,"msgPlaceholder":"","errorModal":false,"hasCancelButton":true};
var url="generic-modal-error.jspx?"+sc.toRequestString();
curam.util.openModalDialog({href:encodeURI(url)},_103a);
},addPlaceholderFocusClassToEventOrAnchorTag:function(_103e,_103f){
var _1040=curam.util.getTopmostWindow();
_1040.curam.util.PLACEHOLDER_WINDOW_LIST=!_1040.curam.util.PLACEHOLDER_WINDOW_LIST?[]:_1040.curam.util.PLACEHOLDER_WINDOW_LIST;
_fde.add(_103e,"placeholder-for-focus");
_1040.curam.util.PLACEHOLDER_WINDOW_LIST.push(_103f);
},returnFocusToPopupActionAnchorElement:function(_1041){
var _1042=_1041.curam.util.PLACEHOLDER_WINDOW_LIST;
if(_1042&&_1042.length>0){
var _1043=_1042.pop();
var _1044=_1043&&_1043.document.activeElement;
var _1045=_1044&&dojo.query(".placeholder-for-focus",_1044);
if(_1045&&_1045.length==1){
_1045[0].focus();
_fde.remove(_1045[0],"placeholder-for-focus");
}
_1041.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0,_1041.curam.util.PLACEHOLDER_WINDOW_LIST.length);
_1041.curam.util.PLACEHOLDER_WINDOW_LIST=null;
}
},openModalDialog:function(_1046,_1047,left,top,_1048){
_1046.event&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_1046.event,_1046.event.ownerDocument.defaultView.window);
var href;
if(!_1046||!_1046.href){
_1046=_fe0.fix(_1046);
var _1049=_1046.target;
while(_1049.tagName!="A"&&_1049!=_fdb.body()){
_1049=_1049.parentNode;
}
href=_1049.href;
_1049._isModal=true;
_fe0.stop(_1046);
}else{
href=_1046.href;
_1046._isModal=true;
}
require(["curam/dialog"]);
var opts=curam.dialog.parseWindowOptions(_1047);
curam.util.showModalDialog(href,_1046,opts["width"],opts["height"],left,top,false,null,null,_1048);
return true;
},showModalDialog:function(url,_104a,width,_104b,left,top,_104c,_104d,_104e,_104f){
var _1050=curam.util.getTopmostWindow();
if(dojo.global!=_1050){
curam.debug.log("curam.util.showModalDialog: "+_fe5.getProperty("curam.util.redirecting.modal"));
_1050.curam.util.showModalDialog(url,_104a,width,_104b,left,top,_104c,_104d,dojo.global,_104f);
return;
}
var rup=curam.util.replaceUrlParam;
url=rup(url,"o3frame","modal");
url=curam.util.modifyUrlContext(url,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
url=rup(url,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
curam.debug.log(_fe5.getProperty("curam.util.modal.url"),url);
if(width){
width=typeof (width)=="number"?width:parseInt(width);
}
if(_104b){
_104b=typeof (_104b)=="number"?_104b:parseInt(_104b);
}
if(!curam.util._isModalCurrentlyOpening()){
if(url.indexOf("__o3rpu=about%3Ablank")>=0){
alert(curam_util_showModalDialog_pageStillLoading);
return;
}
curam.util._setModalCurrentlyOpening(true);
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
new curam.ModalDialog({href:url,width:width,height:_104b,openNode:(_104a&&_104a.target)?_104a.target:null,parentWindow:_104e,uimToken:_104f});
}else{
require(["curam/modal/CuramCarbonModal"]);
new curam.modal.CuramCarbonModal({href:url,width:width,height:_104b,openNode:(_104a&&_104a.target)?_104a.target:null,parentWindow:_104e,uimToken:_104f});
}
return true;
}
},showModalDialogWithRef:function(_1051,_1052,_1053){
var _1054=curam.util.getTopmostWindow();
if(dojo.global!=_1054){
return _1054.curam.util.showModalDialogWithRef(_1051,dojo.global);
}
var rup=curam.util.replaceUrlParam;
_1051=curam.util.modifyUrlContext(_1051,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
_1051=rup(_1051,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(!curam.util._isModalCurrentlyOpening()){
curam.util._setModalCurrentlyOpening(true);
var dl;
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
if(_1053){
dl=new curam.ModalDialog({href:_1051,width:_1053.width,height:_1053.height,parentWindow:_1052});
}else{
dl=new curam.ModalDialog({href:_1051,parentWindow:_1052});
}
}else{
require(["curam/modal/CuramCarbonModal"]);
if(_1053){
dl=new curam.modal.CuramCarbonModal({href:_1051,width:_1053.width,height:_1053.height,parentWindow:_1052});
}else{
dl=new curam.modal.CuramCarbonModal({href:_1051,parentWindow:_1052});
}
}
return dl;
}
},_isModalCurrentlyOpening:function(){
return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
},_setModalCurrentlyOpening:function(_1055){
curam.util.getTopmostWindow().curam.util._modalOpenInProgress=_1055;
},setupPreferencesLink:function(href){
_fda(function(){
var _1056=_fe1(".user-preferences")[0];
if(_1056){
if(typeof (_1056._disconnectToken)=="undefined"){
_1056._disconnectToken=curam.util.connect(_1056,"onclick",curam.util.openPreferences);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_fe5.getProperty("curam.util.no.setup"));
}
});
},setupLocaleLink:function(href){
_fda(function(){
var _1057=_fe1(".user-locale")[0];
if(_1057){
if(typeof (_1057._disconnectToken)=="undefined"){
_1057._disconnectToken=curam.util.connect(_1057,"onclick",curam.util.openLocaleNew);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_fe5.getProperty("curam.util.no.setup"));
}
});
},openPreferences:function(event){
_fe0.stop(event);
if(event.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-prefs-editor.jspx",{dialogOptions:"width=605"});
});
},logout:function(event){
var _1058=curam.util.getTopmostWindow();
_1058.dojo.publish("curam/redirect/logout");
window.location.href=jsBaseURL+"/logout.jsp";
},openAbout:function(event){
_fe0.stop(event);
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("about.jsp",{dialogOptions:"width=583,height=399"});
});
},addMinWidthCalendarCluster:function(id){
var _1059=dom.byId(id);
var i=0;
function _105a(evt){
_fdd.forEach(_1059.childNodes,function(node){
if(_fde.contains(node,"cluster")){
_fdc.set(node,"width","97%");
if(node.clientWidth<700){
_fdc.set(node,"width","700px");
}
}
});
};
if(has("ie")>6){
_fdd.forEach(_1059.childNodes,function(node){
if(_fde.contains(node,"cluster")){
_fdc.set(node,"minWidth","700px");
}
});
}else{
on(dojo.global,"resize",_105a);
_fda(_105a);
}
},addPopupFieldListener:function(id){
if(!has("ie")||has("ie")>6){
return;
}
if(!curam.util._popupFields){
function _105b(evt){
var _105c=0;
var j=0;
var x=0;
var arr=curam.util._popupFields;
_fdd.forEach(curam.util._popupFields,function(id){
var _105d=dom.byId(id);
_fe1("> .popup-actions",_105d).forEach(function(node){
_105c=node.clientWidth+30;
});
_fe1("> .desc",_105d).forEach(function(node){
_fdc.set(node,"width",Math.max(0,_105d.clientWidth-_105c)+"px");
});
});
};
curam.util._popupFields=[id];
on(dojo.global,"resize",_105b);
_fda(_105b);
}else{
curam.util._popupFields.push(id);
}
},addContentWidthListener:function(id){
if(has("ie")>6){
return;
}
var _105e=_fdc.set;
var _105f=_fde.contains;
function _1060(evt){
var i=0;
var _1061=dom.byId("content");
if(_1061){
var width=_1061.clientWidth;
if(has("ie")==6&&dom.byId("footer")){
var _1062=_fdb.body().clientHeight-100;
_105e(_1061,"height",_1062+"px");
var _1063=dom.byId("sidebar");
if(_1063){
_105e(_1063,"height",_1062+"px");
}
}
try{
_fe1("> .page-title-bar",_1061).forEach(function(node){
var _1064=geom.getMarginSize(node).w-geom.getContentBox(node).w;
if(!has("ie")){
_1064+=1;
}
width=_1061.clientWidth-_1064;
_fdc.set(node,"width",width+"px");
});
}
catch(e){
}
_fe1("> .page-description",_1061).style("width",width+"px");
_fe1("> .in-page-navigation",_1061).style("width",width+"px");
}
};
curam.util.subscribe("/clusterToggle",_1060);
curam.util.connect(dojo.global,"onresize",_1060);
_fda(_1060);
},alterScrollableListBottomBorder:function(id,_1065){
var _1066=_1065;
var _1067="#"+id+" table";
function _1068(){
var _1069=_fe1(_1067)[0];
if(_1069.offsetHeight>=_1066){
var _106a=_fe1(".odd-last-row",_1069)[0];
if(typeof _106a!="undefined"){
_fde.add(_106a,"no-bottom-border");
}
}else{
if(_1069.offsetHeight<_1066){
var _106a=_fe1(".even-last-row",_1069)[0];
if(typeof _106a!="undefined"){
_fde.add(_106a,"add-bottom-border");
}
}else{
curam.debug.log("curam.util.alterScrollableListBottomBorder: "+_fe5.getProperty("curam.util.code"));
}
}
};
_fda(_1068);
},addFileUploadResizeListener:function(code){
function _106b(evt){
if(_fe1(".widget")){
_fe1(".widget").forEach(function(_106c){
var width=_106c.clientWidth;
if(_fe1(".fileUpload",_106c)){
_fe1(".fileUpload",_106c).forEach(function(_106d){
fileUploadWidth=width/30;
if(fileUploadWidth<4){
_106d.size=1;
}else{
_106d.size=fileUploadWidth;
}
});
}
});
}
};
on(dojo.global,"resize",_106b);
_fda(_106b);
},openCenteredNonModalWindow:function(url,width,_106e,name){
width=Number(width);
_106e=Number(_106e);
var _106f=(screen.width-width)/2;
var _1070=(screen.height-_106e)/2;
_106e=_1070<0?screen.height:_106e;
_1070=Math.max(0,_1070);
width=_106f<0?screen.width:width;
_106f=Math.max(0,_106f);
var left="left",top="top";
if(has("ff")){
left="screenX",top="screenY";
}
var _1071="location=no, menubar=no, status=no, toolbar=no, "+"scrollbars=yes, resizable=no";
var _1072=dojo.global.open(url,name||"name","width="+width+", height="+_106e+", "+left+"="+_106f+","+top+"="+_1070+","+_1071);
_1072.resizeTo(width,_106e);
_1072.moveTo(_106f,_1070);
_1072.focus();
},adjustTargetContext:function(win,href){
if(win&&win.dojo.global.jsScreenContext){
var _1073=win.dojo.global.jsScreenContext;
_1073.updateStates(dojo.global.jsScreenContext);
return curam.util.replaceUrlParam(href,"o3ctx",_1073.getValue());
}
return href;
},modifyUrlContext:function(url,_1074,_1075){
var _1076=url;
var ctx=new curam.util.ScreenContext();
var _1077=curam.util.getUrlParamValue(url,"o3ctx");
if(_1077){
ctx.setContext(_1077);
}else{
ctx.clear();
}
if(_1074){
ctx.addContextBits(_1074);
}
if(_1075){
ctx.clear(_1075);
}
_1076=curam.util.replaceUrlParam(url,"o3ctx",ctx.getValue());
return _1076;
},updateCtx:function(_1078){
var _1079=curam.util.getUrlParamValue(_1078,"o3ctx");
if(!_1079){
return _1078;
}
return curam.util.modifyUrlContext(_1078,null,"MODAL");
},getFrameRoot:function(_107a,_107b){
var found=false;
var _107c=_107a;
if(_107c){
while(_107c!=top&&!_107c.rootObject){
_107c=_107c.parent;
}
if(_107c.rootObject){
found=(_107c.rootObject==_107b);
}
}
return found?_107c:null;
},saveInformationalMsgs:function(_107d){
try{
localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID]=json.toJson({pageID:_fdb.body().id,total:dom.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,listItems:dom.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML});
_107d();
}
catch(e){
curam.debug.log(_fe5.getProperty("curam.util.exception"),e);
}
},disableInformationalLoad:function(){
curam.util._informationalsDisabled=true;
},redirectDirectUrl:function(){
_fda(function(){
if(dojo.global.parent==dojo.global){
var url=document.location.href;
var idx=url.lastIndexOf("/");
if(idx>-1){
if(idx<=url.length){
url=url.substring(idx+1);
}
}
dojo.global.location=jsBaseURL+"/AppController.do?o3gtu="+encodeURIComponent(url);
}
});
},loadInformationalMsgs:function(){
_fda(function(){
if(dojo.global.jsScreenContext.hasContextBits("CONTEXT_PANEL")){
return;
}
if(curam.util._informationalsDisabled){
return;
}
var msgs=localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID];
if(msgs&&msgs!=""){
msgs=json.fromJson(msgs);
localStorage.removeItem(curam.util.INFORMATIONAL_MSGS_STORAGE_ID);
var div=dom.byId(curam.util.ERROR_MESSAGES_CONTAINER);
var list=dom.byId(curam.util.ERROR_MESSAGES_LIST);
if(msgs.pageID!=_fdb.body().id){
return;
}
if(list){
var _107e=_fd9.create("ul",{innerHTML:msgs.listItems});
var _107f=[];
for(var i=0;i<list.childNodes.length;i++){
if(list.childNodes[i].tagName=="LI"){
_107f.push(list.childNodes[i]);
}
}
var skip=false;
var nodes=_107e.childNodes;
for(var i=0;i<nodes.length;i++){
skip=false;
for(var j=0;j<_107f.length;j++){
if(nodes[i].innerHTML==_107f[j].innerHTML){
skip=true;
break;
}
}
if(!skip){
list.appendChild(nodes[i]);
i--;
}
}
}else{
if(div){
div.innerHTML=msgs.total;
}
}
}
var _1080=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_1080&&!dojo.global.jsScreenContext.hasContextBits("MODAL")){
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked){
curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
setTimeout(function(){
_1080.innerHTML=_1080.innerHTML+" ";
},500);
}else{
_1080.focus();
}
}
var _1081=dom.byId("error-messages-container-wrapper");
if(_1081){
var _1082=_fe1("#container-messages-ul",_1081)[0];
if(_1082){
_1082.focus();
}
}
});
},_setFocusCurrentIframe:function(){
var _1083=/Edg/.test(navigator.userAgent);
if(_1083){
var _1084=window.frameElement;
if(_1084){
_1084.setAttribute("tabindex","0");
_1084.focus();
setTimeout(function(){
_1084.removeAttribute("tabindex");
},10);
}
}
},setFocus:function(){
var _1085;
if(window.document.getElementsByClassName("skeleton").length>0){
_1085=setTimeout(function(){
curam.util.setFocus();
},300);
}else{
if(_1085){
clearTimeout(_1085);
}
var _1086=curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
if(_1086){
return;
}
curam.util._setFocusCurrentIframe();
var _1087=curam.util.getUrlParamValue(dojo.global.location.href,"o3frame")=="modal"||(curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==null&&curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==undefined);
if(!_1087){
_fda(function(){
var _1088=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
var _1089=sessionStorage.getItem("curamDefaultActionId");
var _108a=null;
if(!_1088&&_1089){
sessionStorage.removeItem("curamDefaultActionId");
_108a=dojo.query(".curam-default-action")[0].previousSibling;
}else{
_108a=curam.util.doSetFocus();
}
if(_108a){
curam.util.setFocusOnField(_108a,false);
}else{
window.focus();
}
});
}
}
},setFocusOnField:function(_108b,_108c,_108d){
if(has("IE")||has("trident")){
var _108e=1000;
var _108f=_108c?200:500;
curam.util._createHiddenInputField(_108b);
var _1090=function(ff){
return function(){
var _1091=ff.ownerDocument.activeElement;
if(_1091.tagName==="INPUT"&&!_1091.classList.contains("hidden-focus-input")||_1091.tagName==="TEXTAREA"||(_1091.tagName==="SPAN"&&_1091.className=="fileUploadButton")||(_1091.tagName==="A"&&_1091.className=="popup-action")||(_1091.tagName==="IFRAME"&&_1091.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
if(_108c){
var _1092=attr.get(_108b,"aria-label");
var _1093="";
var _1094=attr.get(_108b,"objid");
if(_1094&&_1094.indexOf("component")==0||_fde.contains(_108b,"dijitReset dijitInputInner")){
_1093=_108b.title;
}else{
_1093=_108d||"Modal Dialog";
}
if(_108b&&_108b.id!=="container-messages-ul"){
attr.set(_108b,"aria-label",_1093);
}
var _1095=function(_1096){
return function(e){
_fe1("input|select[aria-label="+_1093+"]").forEach(function(entry){
_1096&&attr.set(entry,"aria-label",_1096);
!_1096&&attr.remove(entry,"aria-label");
});
};
};
on(_108b,"blur",_1095(_1092));
}
if(_108b.tagName==="TEXTAREA"){
setTimeout(_1090(_108b),_108e);
}else{
if(_108b.tagName==="SELECT"||(_108b.tagName==="INPUT"&&attr.get(_108b,"type")==="text")){
setTimeout(_1090(_108b),_108f);
}else{
_108b.focus();
}
}
}else{
_108b.focus();
}
},_createHiddenInputField:function(_1097){
var _1098=_1097.ownerDocument.forms["mainForm"];
if(_1098&&(_1097.tagName==="SELECT"||(_1097.tagName==="INPUT"&&attr.get(_1097,"type")==="text"))){
var _1099=_fd9.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_fd9.place(_1099,_1098,"before");
_1099.focus();
on(_1099,"blur",function(){
_fd9.destroy(_1099);
});
}
},doSetFocus:function(){
try{
var _109a=curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
if(_109a!=false&&!curam.util.isModalWindow()){
return _109a;
}
var _109b=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_109b){
return _109b;
}
var form=document.forms[0];
if(!form){
return false;
}
var _109c=form.querySelectorAll("button, output, input:not([type=\"image\"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton");
var _109d=false;
var l=_109c.length,el;
for(var i=0;i<l;i++){
el=_109c[i];
if(!_109d&&/selectHook/.test(el.className)){
_109d=_fe1(el).closest("table")[0];
}
if(!_109d&&!(el.style.visibility=="hidden")&&(/select-one|select-multiple|checkbox|radio|text/.test(el.type)||el.tagName=="TEXTAREA"||/popup-action|fileUploadButton/.test(el.className))&&!/dijitArrowButtonInner|dijitValidationInner/.test(el.className)){
_109d=el;
}
if(el.tabIndex=="1"){
el.tabIndex=0;
_109d=el;
break;
}
}
lastOpenedTabButton=curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
if(!_109d&&lastOpenedTabButton){
return lastOpenedTabButton;
}
var _109e=_109d.classList.contains("bx--date-picker__input");
if(_109e){
var _109f=document.querySelector(".bx--uim-modal");
if(_109f){
_109d=_109f;
}
}
return _109d;
}
catch(e){
_fe5.log(_fe5.getProperty("curam.util.error.focus"),e.message);
return false;
}
return false;
},openLocaleSelector:function(event){
event=_fe0.fix(event);
var _10a0=event.target;
while(_10a0&&_10a0.tagName!="A"){
_10a0=_10a0.parentNode;
}
var loc=_10a0.href;
var rpu=curam.util.getUrlParamValue(loc,"__o3rpu");
rpu=curam.util.removeUrlParam(rpu,"__o3rpu");
var href="user-locale-selector.jspx"+"?__o3rpu="+rpu;
if(!curam.util.isActionPage(dojo.global.location.href)){
openModalDialog({href:href},"width=500,height=300",200,150);
}else{
alert(curam.util.msgLocaleSelectorActionPage);
}
return false;
},openLocaleNew:function(event){
_fe0.stop(event);
if(event.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-locale-selector.jspx",{dialogOptions:"width=300"});
});
},isActionPage:function(url){
var _10a1=curam.util.getLastPathSegmentWithQueryString(url);
var _10a2=_10a1.split("?")[0];
return _10a2.indexOf("Action.do")>-1;
},closeLocaleSelector:function(event){
event=_fe0.fix(event);
_fe0.stop(event);
dojo.global.close();
return false;
},getSuffixFromClass:function(node,_10a3){
var _10a4=attr.get(node,"class").split(" ");
var _10a5=_fdd.filter(_10a4,function(_10a6){
return _10a6.indexOf(_10a3)==0;
});
if(_10a5.length>0){
return _10a5[0].split(_10a3)[1];
}else{
return null;
}
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},stripeTable:function(table,_10a7,_10a8){
var tbody=table.tBodies[0];
var _10a9=(_10a7?2:1);
if(tbody.rows.length<_10a9){
return;
}
var rows=tbody.rows;
var _10aa=[],_10ab=[],_10ac=false,_10ad=[],_10ae="";
for(var i=0,l=rows.length;i<l;i+=_10a9){
var _10af=(i%(2*_10a9)==0),_10b0=_10af?_10ab:_10aa;
_fde.remove(rows[i],["odd-last-row","even-last-row"]);
_10b0.push(rows[i]);
if(i==_10a8){
_10ad.push(rows[i]);
_10ae=_10af?"odd":"even";
_10ac=true;
}
if(_10a7&&rows[i+1]){
_fde.remove(rows[i+1],["odd-last-row","even-last-row"]);
_10b0.push(rows[i+1]);
_10ac&&_10ad.push(rows[i+1]);
}
_10ac=false;
}
_10ab.forEach(function(_10b1){
_fde.replace(_10b1,"odd","even");
});
_10aa.forEach(function(_10b2){
_fde.replace(_10b2,"even","odd");
});
_10ad.forEach(function(_10b3){
_fde.add(_10b3,_10ae+"-last-row");
});
},fillString:function(_10b4,count){
var _10b5="";
while(count>0){
_10b5+=_10b4;
count-=1;
}
return _10b5;
},updateHeader:function(qId,_10b6,_10b7,_10b8){
var _10b9=dom.byId("header_"+qId);
_10b9.firstChild.nextSibling.innerHTML=_10b6;
answerCell=dom.byId("chosenAnswer_"+qId);
answerCell.innerHTML=_10b7;
sourceCell=dom.byId("chosenSource_"+qId);
sourceCell.innerHTML=_10b8;
},search:function(_10ba,_10bb){
var _10bc=_fd8.byId(_10ba).get("value");
var _10bd=_fd8.byId(_10bb);
if(_10bd==null){
_10bd=dom.byId(_10bb);
}
var _10be=null;
if(_10bd!=null){
if(_10bd.tagName==null){
_10be=_10bd?_10bd.get("value"):null;
}else{
if(_10bd.tagName=="SELECT"){
var _10bf=_fe1(".multiple-search-banner select option");
_fdd.forEach(_10bf,function(elem){
if(elem.selected){
_10be=elem.value;
}
});
}
}
}
var _10c0="";
var _10c1;
var _10c2;
if(_10be){
_10c2=_10be.split("|");
_10c0=_10c2[0];
_10c1=_10c2[1];
}
var _10c3=curam.util.defaultSearchPageID;
var _10c4="";
if(sessionStorage.getItem("appendSUEP")==="true"){
if(_10c0===""){
_10c4=_10c3+"Page.do?searchText="+encodeURIComponent(_10bc)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1");
}else{
_10c4=_10c1+"Page.do?searchText="+encodeURIComponent(_10bc)+"&searchType="+encodeURIComponent(_10c0)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1,"+curam.util.secureURLsExemptParamsPrefix+"_ST2");
}
}else{
if(_10c0===""){
_10c4=_10c3+"Page.do?searchText="+encodeURIComponent(_10bc);
}else{
_10c4=_10c1+"Page.do?searchText="+encodeURIComponent(_10bc)+"&searchType="+encodeURIComponent(_10c0);
}
}
var _10c5=new curam.ui.PageRequest(_10c4);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_10c5);
});
},updateDefaultSearchText:function(_10c6,_10c7){
var _10c8=_fd8.byId(_10c6);
var _10c9=_fd8.byId(_10c7);
var _10ca=_10c8?_10c8.get("value"):null;
var str=_10ca.split("|")[2];
_10c9.set("placeHolder",str);
_fdf.publish("curam/application-search/combobox-changed",_10ca);
},updateSearchBtnState:function(_10cb,btnID){
var _10cc=_fd8.byId(_10cb);
var btn=dom.byId(btnID);
var value=_10cc.get("value");
if(!value||lang.trim(value).length<1){
_fde.add(btn,"dijitDisabled");
}else{
_fde.remove(btn,"dijitDisabled");
}
},furtherOptionsSearch:function(){
var _10cd=curam.util.furtherOptionsPageID+"Page.do";
var _10ce=new curam.ui.PageRequest(_10cd);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_10ce);
});
},searchButtonStatus:function(btnID){
var btn=dom.byId(btnID);
if(!_fde.contains(btn,"dijitDisabled")){
return true;
}
},getPageHeight:function(){
var _10cf=400;
var _10d0=0;
if(_fe1("frameset").length>0){
curam.debug.log("curam.util.getPageHeight() "+_fe5.getProperty("curam.util.default.height"),_10cf);
_10d0=_10cf;
}else{
var _10d1=function(node){
if(!node){
curam.debug.log(_fe5.getProperty("curam.util.node"));
return 0;
}
var mb=geom.getMarginSize(node);
var pos=geom.position(node);
return pos.y+mb.h;
};
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
var _10d2=_fe1("div#content")[0];
var _10d3=_10d1(_10d2);
curam.debug.log(_fe5.getProperty("curam.util.page.height"),_10d3);
_10d0=_10d3;
}else{
var _10d4=dom.byId("content")||dom.byId("wizard-content");
var nodes=_fe1("> *",_10d4).filter(function(n){
return n.tagName.indexOf("SCRIPT")<0&&_fdc.get(n,"visibility")!="hidden"&&_fdc.get(n,"display")!="none";
});
var _10d5=nodes[0];
for(var i=1;i<nodes.length;i++){
if(_10d1(nodes[i])>=_10d1(_10d5)){
_10d5=nodes[i];
}
}
_10d0=_10d1(_10d5);
curam.debug.log("curam.util.getPageHeight() "+_fe5.getProperty("curam.util.base.height"),_10d0);
var _10d6=_fe1(".actions-panel",_fdb.body());
if(_10d6.length>0){
var _10d7=geom.getMarginBox(_10d6[0]).h;
curam.debug.log("curam.util.getPageHeight() "+_fe5.getProperty("curam.util.panel.height"));
_10d0+=_10d7;
_10d0+=10;
}
var _10d8=_fe1("body.details");
if(_10d8.length>0){
curam.debug.log("curam.util.getPageHeight() "+_fe5.getProperty("curam.util.bar.height"));
_10d0+=20;
}
}
}
curam.debug.log("curam.util.getPageHeight() "+_fe5.getProperty("curam.util.returning"),_10d0);
return _10d0;
},toCommaSeparatedList:function(_10d9){
var _10da="";
for(var i=0;i<_10d9.length;i++){
_10da+=_10d9[i];
if(i<_10d9.length-1){
_10da+=",";
}
}
return _10da;
},setupGenericKeyHandler:function(){
_fda(function(){
var f=function(event){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")&&event.keyCode==27){
var ev=_fe0.fix(event);
var _10db=_fd8.byId(ev.target.id);
var _10dc=typeof _10db!="undefined"&&_10db.baseClass=="dijitTextBox dijitComboBox";
if(!_10dc){
curam.dialog.closeModalDialog();
}
}
if(event.keyCode==13){
var ev=_fe0.fix(event);
var _10dd=ev.target.type=="text";
var _10de=ev.target.type=="radio";
var _10df=ev.target.type=="checkbox";
var _10e0=ev.target.type=="select-multiple";
var _10e1=ev.target.type=="password";
var combo=_fd8.byId(ev.target.id);
if(typeof combo!="undefined"){
var _10e2=_fd8.byId(ev.target.id);
if(!_10e2){
_10e2=_fd8.byNode(dom.byId("widget_"+ev.target.id));
}
if(_10e2&&_10e2.enterKeyOnOpenDropDown){
_10e2.enterKeyOnOpenDropDown=false;
return false;
}
}
var _10e3=ev.target.getAttribute("data-carbon-attach-point");
if(_10e3&&_10e3==="carbon-menu"){
return false;
}
var _10e4=typeof combo!="undefined"&&combo.baseClass=="dijitComboBox";
if((!_10dd&&!_10de&&!_10df&&!_10e0&&!_10e1)||_10e4){
return true;
}
var _10e5=null;
var _10e6=_fe1(".curam-default-action");
if(_10e6.length>0){
_10e5=_10e6[0];
}else{
var _10e7=_fe1("input[type='submit']");
if(_10e7.length>0){
_10e5=_10e7[0];
}
}
if(_10e5!=null){
_fe0.stop(_fe0.fix(event));
curam.util.clickButton(_10e5);
return false;
}
require(["curam/dateSelectorUtil"],function(_10e8){
var _10e9=dom.byId("year");
if(_10e9){
dojo.stopEvent(dojo.fixEvent(event));
_10e8.updateCalendar();
}
});
}
return true;
};
curam.util.connect(_fdb.body(),"onkeyup",f);
});
},enterKeyPress:function(event){
if(event.keyCode==13){
return true;
}
},swapState:function(node,state,_10ea,_10eb){
if(state){
_fde.replace(node,_10ea,_10eb);
}else{
_fde.replace(node,_10eb,_10ea);
}
},makeQueryString:function(_10ec){
if(!_10ec||_10ec.length==0){
return "";
}
var _10ed=[];
for(var _10ee in _10ec){
_10ed.push(_10ee+"="+encodeURIComponent(_10ec[_10ee]));
}
return "?"+_10ed.join("&");
},fileDownloadAnchorHandler:function(url){
var _10ef=curam.util.getTopmostWindow();
var _10f0=_10ef.dojo.subscribe("/curam/dialog/close",function(id,_10f1){
if(_10f1==="confirm"){
curam.util.clickHandlerForListActionMenu(url,false,false);
}
_10ef.dojo.unsubscribe(_10f0);
});
var _10f2=new _fea("GenericModalError");
var width=_10f2.getProperty("file.download.warning.dialog.width");
var _10f3=_10f2.getProperty("file.download.warning.dialog.height");
if(!width){
width=500;
}
if(!_10f3){
_10f3=225;
}
var _10f4=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+width+",height="+_10f3,"file.download.warning.title","file.download.warning."+_10f4);
return false;
},fileDownloadListActionHandler:function(url,_10f5,_10f6,event){
var _10f7=curam.util.getTopmostWindow();
var _10f8=_10f7.dojo.subscribe("/curam/dialog/close",function(id,_10f9){
if(_10f9==="confirm"){
curam.util.clickHandlerForListActionMenu(url,_10f5,_10f6,event);
}
_10f7.dojo.unsubscribe(_10f8);
});
var _10fa=new _fea("GenericModalError");
var width=_10fa.getProperty("file.download.warning.dialog.width");
var _10fb=_10fa.getProperty("file.download.warning.dialog.height");
if(!width){
width=500;
}
if(!_10fb){
_10fb=225;
}
var _10fc=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+width+",height="+_10fb,"file.download.warning.title","file.download.warning."+_10fc);
},_getBrowserName:function(){
var _10fd=has("trident");
var _10fe=dojo.isFF;
var _10ff=dojo.isChrome;
var _1100=dojo.isSafari;
var _1101=curam.util.getTopmostWindow();
var _1102=curam.util.ExpandableLists._isExternalApp(_1101);
if(_10fd!=undefined){
var _1103=_10fd+4;
if(_1103<8){
return "unknown.browser";
}else{
return "ie"+_1103;
}
}else{
if(_10fe!=undefined&&_1102){
return "firefox";
}else{
if(_10ff!=undefined){
return "chrome";
}else{
if(_1100!=undefined&&_1102){
return "safari";
}
}
}
}
return "unknown.browser";
},clickHandlerForListActionMenu:function(url,_1104,_1105,event){
if(_1104){
var href=curam.util.replaceUrlParam(url,"o3frame","modal");
var ctx=dojo.global.jsScreenContext;
ctx.addContextBits("MODAL");
href=curam.util.replaceUrlParam(href,"o3ctx",ctx.getValue());
curam.util.redirectWindow(href);
return;
}
var _1106={href:url};
require(["curam/ui/UIMPageAdaptor"]);
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_1106)){
if(_1106.href.indexOf("/servlet/FileDownload")){
sessionStorage.setItem("addOnUnloadTriggeredByFileDownload","true");
dojo.global.location=url;
sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
}else{
dojo.global.location=url;
}
return;
}
if(_1106!=null){
if(event){
_fe0.fix(event);
_fe0.stop(event);
}
if(!_1106.href||_1106.href.length==0){
return;
}
if(_1105&&!curam.util.isInternal(url)){
dojo.global.open(url);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_1106)){
var _1107=new curam.ui.PageRequest(_1106.href);
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")){
_1107.pageHolder=dojo.global;
}
require(["curam/tab"],function(){
var _1108=curam.tab.getTabController();
if(_1108){
_1108.handlePageRequest(_1107);
}
});
}
}
}
},clickHandlerForMailtoLinks:function(event,url){
dojo.stopEvent(event);
var _1109=dojo.query("#mailto_frame")[0];
if(!_1109){
_1109=dojo.io.iframe.create("mailto_frame","");
}
_1109.src=url;
return false;
},isInternal:function(url){
var path=url.split("?")[0];
var _110a=path.match("Page.do");
if(_110a!=null){
return true;
}
return false;
},getLastPathSegmentWithQueryString:function(url){
var _110b=url.split("?");
var _110c=_110b[0].split("/");
return _110c[_110c.length-1]+(_110b[1]?"?"+_110b[1]:"");
},replaceSubmitButton:function(name,_110d,_110e,_110f,_1110){
if(curam.replacedButtons[name]=="true"){
return;
}
var _1111="__o3btn."+name;
var _1112;
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_1112=_fe1("input[id='"+_1111+"']");
}else{
_1112=_fe1("input[name='"+_1111+"']");
}
_1112.forEach(function(_1113,index,_1114){
if(_110d){
var _1115=_1114[1];
_1115.setAttribute("value",_110d);
}
_1113.tabIndex=-1;
var _1116=_1113.parentNode;
var _1117;
var _1118=curam.util.isInternalModal()&&curam.util.isModalFooter(_1113);
var _1119="btn-id-"+index;
var _111a="ac initially-hidden-widget "+_1119;
if(_fde.contains(_1113,"first-action-control")){
_111a+=" first-action-control";
}
if(_1118){
var _111b=(_110e&&!_1110)?undefined:_1114[0];
var _111c=_110e?{"href":"","buttonid":_110f}:{"buttonid":_110f};
var _111d=_1113.getAttribute("data-rawtestid");
if(_111d){
_111c.dataTestId=_111d;
}
var _111e=_fde.contains(_1113,"curam-default-action")?true:false;
curam.util.addCarbonModalButton(_111c,_1113.value,_111b,_111e);
}else{
curam.util.setupWidgetLoadMask("a."+_1119);
var _111a="ac initially-hidden-widget "+_1119;
if(_fde.contains(_1113,"first-action-control")){
_111a+=" first-action-control";
}
var _1117=_fd9.create("a",{"class":_111a,href:"#"},_1113,"before");
var _111f=dojo.query(".page-level-menu")[0];
if(_111f){
attr.set(_1117,"title",_1113.value);
}
_fd9.create("span",{"class":"filler"},_1117,"before");
var left=_fd9.create("span",{"class":"left-corner"},_1117);
var right=_fd9.create("span",{"class":"right-corner"},left);
var _1120=_fd9.create("span",{"class":"middle"},right);
_1120.appendChild(document.createTextNode(_1113.value));
curam.util.addActionControlClass(_1117);
}
if(_1117){
on(_1117,"click",function(event){
curam.util.clickButton(this._submitButton);
_fe0.stop(event);
});
_1117._submitButton=_1114[0];
}
_fde.add(_1113,"hidden-button");
attr.set(_1113,"aria-hidden","true");
attr.set(_1113,"id",_1113.id+"_"+index);
});
curam.replacedButtons[name]="true";
},isInternalModal:function(){
return !dojo.global.jsScreenContext.hasContextBits("EXTAPP")&&dojo.global.jsScreenContext.hasContextBits("MODAL");
},isModalFooter:function(_1121){
if(_1121){
var _1122=_1121.parentNode.parentNode;
return _1122&&_1122.id=="actions-panel";
}
},addCarbonModalButton:function(_1123,_1124,_1125,_1126){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton",[_1123,_1124,_1125,_1126,window]);
},setupWidgetLoadMask:function(_1127){
curam.util.subscribe("/curam/page/loaded",function(){
var _1128=_fe1(_1127)[0];
if(_1128){
_fdc.set(_1128,"visibility","visible");
}else{
curam.debug.log("setupButtonLoadMask: "+_fe5.getProperty("curam.util.not.found")+"'"+_1127+"'"+_fe5.getProperty("curam.util.ignore.mask"));
}
});
},optReplaceSubmitButton:function(name){
if(curam.util.getFrameRoot(dojo.global,"wizard")==null){
curam.util.replaceSubmitButton(name);
return;
}
var _1129=curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
if(_1129.delegatesSubmit[jsPageID]!="assumed"){
curam.util.replaceSubmitButton(name);
}
},clickButton:function(_112a){
var _112b=dom.byId("mainForm");
var _112c;
if(!_112a){
curam.debug.log("curam.util.clickButton: "+_fe5.getProperty("curam.util..no.arg"));
return;
}
if(typeof (_112a)=="string"){
var _112d=_112a;
curam.debug.log("curam.util.clickButton: "+_fe5.getProperty("curam.util.searching")+_fe5.getProperty("curam.util.id.of")+"'"+_112d+"'.");
_112a=_fe1("input[id='"+_112d+"']")[0];
if(!_112a){
_112a=_fe1("input[name='"+_112d+"']")[0];
}
if(!_112a.form&&!_112a.id){
curam.debug.log("curam.util.clickButton: "+_fe5.getProperty("curam.util.searched")+_fe5.getProperty("curam.util.id.of")+"'"+_112d+_fe5.getProperty("curam.util.exiting"));
return;
}
}
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_112c=_112a;
}else{
_112c=_fe1("input[id='"+_112a.id+"']",_112b)[0];
if(!_112c){
_112c=_fe1("input[name='"+_112a.name+"']",_112b)[0];
}
}
try{
if(attr.get(_112b,"action").indexOf(jsPageID)==0){
curam.util.publishRefreshEvent();
}
_112c.click();
}
catch(e){
curam.debug.log(_fe5.getProperty("curam.util.exception.clicking"));
}
},printPage:function(_112e,event){
_fe0.stop(event);
var _112f=dojo.window.get(event.currentTarget.ownerDocument);
if(_112e===false){
curam.util._printMainAreaWindow(_112f);
return false;
}
var _1130=_112f.frameElement;
var _1131=_1130;
while(_1131&&!_fde.contains(_1131,"tab-content-holder")){
_1131=_1131.parentNode;
}
var _1132=_1131;
var _1133=dojo.query(".detailsPanelFrame",_1132)[0];
var _1134=_1133!=undefined&&_1133!=null;
if(_1134){
var isIE=has("trident")||has("ie");
var _1135=has("edge");
var _1136=_fde.contains(_1133.parentNode,"collapsed");
if(isIE&&_1136){
_fdc.set(_1133.parentNode,"display","block");
}
_1133.contentWindow.focus();
_1133.contentWindow.print();
if(isIE&&_1136){
_fdc.set(_1133.parentNode,"display","");
}
if(isIE||_1135){
setTimeout(function(){
if(_1135){
function _1137(){
curam.util._printMainAreaWindow(_112f);
curam.util.getTopmostWindow().document.body.removeEventListener("mouseover",_1137,true);
return false;
};
curam.util.getTopmostWindow().document.body.addEventListener("mouseover",_1137,true);
}else{
if(isIE){
curam.util._printMainAreaWindow(_112f);
return false;
}
}
},2000);
}else{
curam.util._printMainAreaWindow(_112f);
return false;
}
}else{
curam.util._printMainAreaWindow(_112f);
return false;
}
},_printMainAreaWindow:function(_1138){
var _1139=_fe1(".list-details-row-toggle.expanded");
if(_1139.length>0){
curam.util._prepareContentPrint(_1138);
_1138.focus();
_1138.print();
curam.util._deletePrintVersion();
}else{
_1138.focus();
_1138.print();
}
},_prepareContentPrint:function(_113a){
var _113b=Array.prototype.slice.call(_113a.document.querySelectorAll("body iframe"));
_113b.forEach(function(_113c){
curam.util._prepareContentPrint(_113c.contentWindow);
var list=_113c.contentWindow.document.querySelectorAll(".title-exists");
var _113d=_113c.contentWindow.document.querySelectorAll(".title-exists div.context-panel-wrapper");
if(list.length>0&&_113d.length===0){
var _113e=document.createElement("div");
_113e.setAttribute("class","tempContentPanelFrameWrapper");
_113e.innerHTML=list[0].innerHTML;
var _113f=_113c.parentNode;
_113f.parentNode.appendChild(_113e);
_113f.style.display="none";
curam.util.wrappersMap.push({tempDivWithIframeContent:_113e,iframeParentElement:_113f});
}
});
},_deletePrintVersion:function(){
if(curam.util.wrappersMap){
curam.util.wrappersMap.forEach(function(_1140){
_1140.tempDivWithIframeContent.parentNode.removeChild(_1140.tempDivWithIframeContent);
_1140.iframeParentElement.style.display="block";
});
curam.util.wrappersMap=[];
}
},addSelectedClass:function(event){
_fde.add(event.target,"selected");
},removeSelectedClass:function(event){
_fde.remove(event.target,"selected");
},openHelpPage:function(event,_1141){
_fe0.stop(event);
dojo.global.open(_1141);
},connect:function(_1142,_1143,_1144){
var h=function(event){
_1144(_fe0.fix(event));
};
if(has("ie")&&has("ie")<9){
_1142.attachEvent(_1143,h);
_fe3.addOnWindowUnload(function(){
_1142.detachEvent(_1143,h);
});
return {object:_1142,eventName:_1143,handler:h};
}else{
var _1145=_1143;
if(_1143.indexOf("on")==0){
_1145=_1143.slice(2);
}
var dt=on(_1142,_1145,h);
_fe3.addOnWindowUnload(function(){
dt.remove();
});
return dt;
}
},disconnect:function(token){
if(has("ie")&&has("ie")<9){
token.object.detachEvent(token.eventName,token.handler);
}else{
token.remove();
}
},subscribe:function(_1146,_1147){
var st=_fdf.subscribe(_1146,_1147);
_fe3.addOnWindowUnload(function(){
st.remove();
});
return st;
},unsubscribe:function(token){
token.remove();
},addActionControlClickListener:function(_1148){
var _1149=dom.byId(_1148);
var _114a=_fe1(".ac",_1149);
if(_114a.length>0){
for(var i=0;i<_114a.length;i++){
var _114b=_114a[i];
curam.util.addActionControlClass(_114b);
}
}
this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
},_addAccessibilityMarkupInAddressClustersWhenContextIsMissing:function(){
var _114c=_fe1(".bx--accordion__content");
_114c.forEach(function(_114d){
var _114e=_fe1(".bx--address",_114d)[0];
if(typeof (_114e)!="undefined"){
var _114f=new _fea("util");
var _1150=_114e.parentElement.parentElement.parentElement;
var _1151=_1150.parentElement.parentElement;
var _1152=_fe1("h4, h3",_1150).length==1?true:false;
var _1153=attr.get(_1151,"aria-label")!==null?true:false;
if(!_1152&&!_1153){
attr.set(_1151,"role","group");
attr.set(_1151,"aria-label",_114f.getProperty("curam.address.header"));
}
}
});
},addActionControlClass:function(_1154){
curam.util.connect(_1154,"onmousedown",function(){
_fde.add(_1154,"selected-button");
curam.util.connect(_1154,"onmouseout",function(){
_fde.remove(_1154,"selected-button");
});
});
},getClusterActionSet:function(){
var _1155=dom.byId("content");
var _1156=_fe1(".blue-action-set",_1155);
if(_1156.length>0){
for(var i=0;i<_1156.length;i++){
curam.util.addActionControlClickListener(_1156[i]);
}
}
},adjustActionButtonWidth:function(){
if(has("ie")==8){
_fda(function(){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")){
_fe1(".action-set > a").forEach(function(node){
if(node.childNodes[0].offsetWidth>node.offsetWidth){
_fdc.set(node,"width",node.childNodes[0].offsetWidth+"px");
_fdc.set(node,"display","block");
_fdc.set(node,"display","inline-block");
}
});
}
});
}
},setRpu:function(url,rtc,_1157){
if(!url||!rtc||!rtc.getHref()){
throw {name:"Unexpected values",message:"This value not allowed for url or rtc"};
}
var _1158=curam.util.getLastPathSegmentWithQueryString(rtc.getHref());
_1158=curam.util.removeUrlParam(_1158,curam.util.Constants.RETURN_PAGE_PARAM);
if(_1157){
var i;
for(i=0;i<_1157.length;i++){
if(!_1157[i].key||!_1157[i].value){
throw {name:"undefined value error",message:"The object did not contain a valid key/value pair"};
}
_1158=curam.util.replaceUrlParam(_1158,_1157[i].key,_1157[i].value);
}
}
var _1159=curam.util.replaceUrlParam(url,curam.util.Constants.RETURN_PAGE_PARAM,encodeURIComponent(_1158));
curam.debug.log("curam.util.setRpu "+_fe5.getProperty("curam.util.added.rpu")+_1159);
return _1159;
},retrieveBaseURL:function(){
return dojo.global.location.href.match(".*://[^/]*/[^/]*");
},removeRoleRegion:function(){
var body=dojo.query("body")[0];
attr.remove(body,"role");
},iframeTitleFallBack:function(){
var _115a=curam.tab.getContainerTab(curam.tab.getContentPanelIframe());
var _115b=dom.byId(curam.tab.getContentPanelIframe());
var _115c=_115b.contentWindow.document.title;
var _115d=dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
var _115e=dojo.query("span.tabLabel",_115d)[0];
var _115f=dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked",_115a.domNode)[0];
var _1160=dojo.query("span.tabLabel",_115f)[0];
if(_115c=="undefined"){
return this.getPageTitleOnContentPanel();
}else{
if(_115c&&_115c!=""){
return _115c;
}else{
if(_115f){
return _1160.innerHTML;
}else{
return _115e.innerHTML;
}
}
}
},getPageTitleOnContentPanel:function(){
var _1161;
var _1162=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _1163;
if(_1162&&_1162.length==1){
_1163=_1162[0].contentWindow.document;
_fdb.withDoc(_1163,function(){
var _1164=dojo.query("div.title h2 span:not(.hidden)");
if(_1164&&_1164.length==1&&_1164[0].textContent){
_1161=lang.trim(_1164[0].textContent);
}
},this);
}
if(_1161){
return _1161;
}else{
return undefined;
}
},addClassToLastNodeInContentArea:function(){
var _1165=_fe1("> div","content");
var _1166=_1165.length;
if(_1166==0){
return "No need to add";
}
var _1167=_1165[--_1166];
while(_fde.contains(_1167,"hidden-action-set")&&_1167){
_1167=_1165[--_1166];
}
_fde.add(_1167,"last-node");
},highContrastModeType:function(){
var _1168=dojo.query("body.high-contrast")[0];
return _1168;
},isRtlMode:function(){
var _1169=dojo.query("body.rtl")[0];
return _1169;
},processBidiContextual:function(_116a){
_116a.dir=bidi.prototype._checkContextual(_116a.value);
},getCookie:function(name){
var dc=document.cookie;
var _116b=name+"=";
var begin=dc.indexOf("; "+_116b);
if(begin==-1){
begin=dc.indexOf(_116b);
if(begin!=0){
return null;
}
}else{
begin+=2;
}
var end=document.cookie.indexOf(";",begin);
if(end==-1){
end=dc.length;
}
return unescape(dc.substring(begin+_116b.length,end));
},getHeadingTitleForScreenReader:function(_116c){
var _116d=curam.util.getTopmostWindow();
var _116e=_116d.dojo.global._tabTitle;
if(_116e){
curam.util.getHeadingTitle(_116e,_116c);
}else{
var _116f=_116d.dojo.subscribe("/curam/_tabTitle",function(_1170){
if(_1170){
curam.util.getHeadingTitle(_1170,_116c);
}
_116d.dojo.unsubscribe(_116f);
});
}
},getHeadingTitle:function(_1171,_1172){
var _1173=undefined;
if(_1171&&_1171.length>0){
_1173=_1171;
}else{
_1173=_1172;
}
var _1174=dojo.query(".page-title-bar");
var _1175=dojo.query("div h2",_1174[0]);
if(_1175){
var _1176=dojo.query("span",_1175[0]);
var span=undefined;
if(_1176){
span=_1176[0];
}
if(!span||(span&&(span.innerHTML.length==0))){
if(span){
attr.set(span,"class","hidden");
attr.set(span,"title",_1173);
span.innerHTML=_1173;
}else{
span=_fd9.create("span",{"class":"hidden","title":_1173},_1175[0]);
span.innerHTML=_1173;
}
}
}
},_setupBrowserTabTitle:function(_1177,_1178,_1179){
_1177=_1177.replace("\\n"," ");
curam.util._browserTabTitleData.staticTabTitle=_1177;
curam.util._browserTabTitleData.separator=_1178;
curam.util._browserTabTitleData.appNameFirst=_1179;
},_browserTabTitleData:{},setBrowserTabTitle:function(title){
curam.debug.log("curam.util.setBrowserTabTitle(title = "+title+") called");
if(!title){
title=curam.util._findAppropriateDynamicTitle();
}
var _117a=curam.util._browserTabTitleData.staticTabTitle;
var _117b=curam.util._browserTabTitleData.separator;
var _117c=curam.util._browserTabTitleData.appNameFirst;
if(!_117a&&!_117b&&!_117c&&!title){
var _117d=document.querySelectorAll("head title")[0];
if(_117d){
document.title=_117d.text;
}
}else{
if(!title){
document.title=_117a;
}else{
if(_117a){
if(_117c){
document.title=_117a+_117b+title;
}else{
document.title=title+_117b+_117a;
}
}
}
}
},toggleCheckboxedSelectStyle:function(e,div){
if(e.checked){
div.classList.remove("unchecked");
div.classList.add("checked");
}else{
div.classList.remove("checked");
div.classList.add("unchecked");
}
},_findAppropriateDynamicTitle:function(){
var i;
var one;
var _117e=dojo.query("iframe.curam-active-modal").length;
if(_117e>1){
var _117f=dojo.query("iframe.curam-active-modal")[0];
if(_117f){
var _1180=_117f.contentDocument;
if(_1180){
var _1181=_1180.head.getElementsByTagName("title")[0];
if(_1181){
if(_1181.innerHTML!=""){
one=_117f.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
}
}
}
}
}
if(one){
return one;
}
var two;
var _1182=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _1183;
if(_1182&&_1182.length==1){
_1183=_1182[0].contentWindow.document;
_fdb.withDoc(_1183,function(){
var _1184=dojo.query("div.title h2 span:not(.hidden)");
var _1185=dom.byId("error-messages",_1183);
if(_1185){
two=_1183.head.getElementsByTagName("title")[0].textContent;
}else{
if(_1184&&_1184.length==1&&_1184[0].textContent){
two=lang.trim(_1184[0].textContent);
curam.debug.log("(2) Page title for Content Panel = "+two);
}else{
if(_1184&&_1184.length>1){
two=this._checkForSubTitles(_1184);
}else{
curam.debug.log("(2) Could not find page title for content panel: header = "+_1184);
}
}
}
},this);
}else{
curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = "+_1182);
}
if(two){
return two;
}
var three;
var _1186=dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
if(_1186&&_1186.length==1&&_1186[0].textContent){
three=lang.trim(_1186[0].textContent);
curam.debug.log("(3) Selected navigation item = "+three);
}else{
curam.debug.log("(3) Could not find selected navigation item: navItem = "+_1186);
}
if(three){
return three;
}
var four;
var _1187=dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
var _1188;
for(i=0;i<_1187.length;i++){
if(_1187[i].getAttribute("aria-selected")==="true"){
_1188=_1187[i];
}
}
if(_1188&&_1188.textContent){
four=lang.trim(_1188.textContent);
curam.debug.log("(4) Selected navigation bar tab = "+four);
}else{
curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = "+_1188);
}
if(four){
return four;
}
var five;
var _1189=dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
if(_1189&&_1189.length==1&&_1189[0].textContent){
five=lang.trim(_1189[0].textContent);
curam.debug.log("(5) Selected application tab title bar = "+five);
}else{
curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = "+_1189);
}
if(five){
return five;
}
var six;
var _118a=dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
var _118b;
for(i=0;i<_118a.length;i++){
if(_118a[i].getAttribute("aria-selected")==="true"){
_118b=_118a[i];
break;
}
}
if(_118b&&_118b.textContent){
six=lang.trim(_118b.textContent);
curam.debug.log("(6) Selected section title = "+six);
}else{
curam.debug.log("(6) Could not find selected section title: sections = "+_118a);
}
if(six){
return six;
}
var seven;
_1182=dom.byId("curamUAIframe");
if(_1182&&_1182.contentWindow&&_1182.contentWindow.document){
_1183=_1182.contentWindow.document;
_fdb.withDoc(_1183,function(){
var _118c=dojo.query("div.page-header > div.page-title-bar > div.title > h2 > span");
if(_118c&&_118c.length==1&&_118c[0].textContent){
seven=lang.trim(_118c[0].textContent);
curam.debug.log("(7) UIM page title for external application page = "+seven);
}else{
curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = "+_118c);
}
},this);
}
if(seven){
return seven;
}
return undefined;
},_checkForSubTitles:function(_118d){
var i;
if(!_118d[0].textContent){
return undefined;
}
for(i=1;i<_118d.length;i++){
var clazz=_118d[i].getAttribute("class");
if(clazz.indexOf("sub-title")===-1||!_118d[i].textContent){
curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
for(i=0;i<_118d.length;i++){
curam.debug.log(_118d[i]);
}
return undefined;
}
}
var ret=_118d[0].textContent;
for(i=1;i<_118d.length;i++){
ret+=_118d[i].textContent;
}
return ret;
},_addContextToWidgetForScreenReader:function(_118e){
var found=false;
var index=0;
var _118f=dojo.query(".training-details-list");
if(_118f.length==1){
var _1190=_118f[0].parentElement;
var _1191=dojo.query("div.bx--cluster",_1190);
var _1192=Array.prototype.indexOf.call(_1191,_118f[0]);
if(_1192>=0){
for(var i=_1192;i>=0;i--){
if(dojo.query("h3",_1191[i]).length==1){
found=true;
index=i;
break;
}
}
}
if(found){
var _1193=dojo.query("h3.bx--accordion__title",_1191[index]);
if(_1193.length==1){
var _1194=_1193[0].className+"_id";
attr.set(_1193[0],"id",_1194);
var _1195=dojo.byId(_118e).parentElement;
attr.set(_1195,"aria-labelledby",_1194);
attr.set(_1195,"role","region");
}
}
}
},setParentFocusByChild:function(_1196){
var win=curam.util.UimDialog._getDialogFrameWindow(_1196);
if(win){
var _1197=curam.dialog.getParentWindow(win);
if(_1197){
_1197.focus();
}
}
},toClipboard:function(_1198){
try{
navigator.clipboard.writeText(_1198);
}
catch(err){
console.warn("Failed to copy into the clipboard.");
}
if(dojo.getObject("curam.dialog",false)!=null){
var pw=curam.dialog.getParentWindow(window);
pw&&pw.dojo.publish("/curam/clip/selected",[_1198]);
}
return false;
},removeTopScrollForIos:function(){
if(has("ios")){
window.document.body.scrollTop=0;
}
},insertAriaLiveLabelRecordSearchItem:function(_1199){
var span=dojo.query("[data-search-page]")[0];
if(span){
span.setAttribute("aria-live",has("ios")?"polite":"assertive");
setTimeout(function(){
var _119a=span.firstChild.nodeValue;
var _119b=_119a+_1199;
span.innerHTML=_119b;
},10);
}
},removeSessionStorageProperty:function(_119c){
sessionStorage.removeItem(_119c);
},addLayoutStylingOnDateTimeWidgetOnZoom:function(){
var _119d=dojo.query("table.input-cluster td.field table.date-time");
console.log("datetimetable from util.js: "+_119d);
var _119e=_119d.length;
if(_119d.length>0){
for(var i=0;i<_119d.length;i++){
var _119f=_119d[i];
var _11a0=_119f.parentNode.parentNode;
_11a0.setAttribute("class","date-time-exists");
}
}
},fileUploadOpenFileBrowser:function(e,_11a1){
if(e.keyCode==32||e.keyCode==13){
dom.byId(_11a1).click();
}
},setupControlledLists:function(){
var _11a2="curam.listControls",_11a3="curam.listTogglers";
var _11a4=_11a5(_11a2),_11a6=_11a5(_11a3),lists=[];
var _11a7=_11a4&&_fe1("*[data-control]"),_11a8=_11a6&&_fe1("a[data-toggler]");
if(_11a4||_11a6){
for(var _11a9 in _11a4){
_11a7.filter(function(item){
return attr.get(item,"data-control")==_11a9;
}).forEach(function(_11aa,ix){
var c=dom.byId(_11aa),tr=_fe1(_11aa).closest("tr")[0];
!tr.controls&&(tr.controls=new Array());
tr.controls.push(c);
if(!tr.visited){
tr.visited=true;
_11a4[_11a9].push(tr);
}
});
var _11ab=_11a5(_11a2+"."+_11a9);
if(_11ab&&_11ab.length&&_11ab.length>0){
lists.push(_11a9);
}else{
putTo(_11a2+"."+_11a9,false);
}
}
if(_11a8&&_11a8.length>0){
for(var _11a9 in _11a6){
_11a8.filter(function(item){
return attr.get(item,"data-toggler")==_11a9;
}).forEach(function(_11ac){
var tr=_fe1(_11ac).closest("tr")[0];
tr.hasToggler=_11ac;
tr.visited=true;
_11a6[_11a9].push(tr);
});
var _11ad=_11a5(_11a3+"."+_11a9);
if(_11ad&&_11ad.length&&_11ad.length>0){
(lists.indexOf(_11a9)==-1)&&lists.push(_11a9);
}else{
putTo(_11a3+"."+_11a9,false);
}
}
}
lists.forEach(function(_11ae){
var _11af=_11a5(_11a2+"."+_11ae)||_11a5(_11a3+"."+_11ae);
cu.updateListControlReadings(_11ae,_11af);
});
}
dojo.subscribe("curam/sort/earlyAware",function(_11b0){
cu.suppressPaginationUpdate=_11b0;
});
dojo.subscribe("curam/update/readings/sort",function(_11b1,rows){
if(!has("trident")){
cu.updateListActionReadings(_11b1);
cu.updateListControlReadings(_11b1,rows);
cu.suppressPaginationUpdate=false;
}else{
var _11b2=cu.getPageBreak(_11b1),limit=Math.ceil(rows.length/_11b2);
cu.listRangeUpdate(0,limit,_11b1,rows,_11b2);
}
});
dojo.subscribe("curam/update/readings/pagination",function(_11b3,_11b4){
putTo("curam.pageBreak."+_11b3,_11b4);
});
dojo.subscribe("curam/update/pagination/rows",function(_11b5,_11b6){
cu.updateDeferred&&!cu.updateDeferred.isResolved()&&cu.updateDeferred.cancel("Superseeded");
if(cu.suppressPaginationUpdate&&cu.suppressPaginationUpdate==_11b6){
return;
}
var _11b7=_11db("curam.listTogglers."+_11b6),_11b8=_11db("curam.listControls."+_11b6),lms=_11a5("curam.listMenus."+_11b6),_11b9=lms&&(lms.length>0);
var _11ba=_11b8||_11b7;
if(!_11ba&&!_11b9){
return;
}
if(_11ba){
var _11bb=_11b5.filter(function(aRow){
return (!aRow.visited||!aRow.done)&&attr.has(aRow,"data-lix");
});
_11b7&&_11bb.forEach(function(aRow){
var tgl=_fe1("a[data-toggler]",aRow)[0];
aRow.hasToggler=tgl;
aRow.visited=true;
curam.listTogglers[_11b6].push(aRow);
});
_11b8&&_11bb.forEach(function(aRow){
var _11bc=_fe1("*[data-control]",aRow),_11bd=new Array();
_11bc.forEach(function(cRef){
_11bd.push(dom.byId(cRef));
});
aRow.controls=_11bd;
curam.listControls[_11b6].push(aRow);
aRow.visited=true;
});
var _11be=_11b8?curam.listControls[_11b6]:curam.listTogglers[_11b6];
cu.updateListControlReadings(_11b6,_11be);
}
_11b9&&cu.updateListActionReadings(_11b6);
});
},listRangeUpdate:function(_11bf,limit,_11c0,rows,psz){
if(_11bf==limit){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
return;
}
var def=cu.updateDeferred=new _fe2(function(_11c1){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
});
def.then(function(pNum){
cu.listRangeUpdate(pNum,limit,_11c0,rows,psz);
},function(err){
});
var delay=(_11bf===0)?0:200;
setTimeout(function(){
var _11c2=_11bf+1,range=[_11bf*psz,(_11c2*psz)];
cu.updateListActionReadings(_11c0,range);
cu.updateListControlReadings(_11c0,rows,range);
def.resolve(_11c2);
},delay);
},updateListControlReadings:function(_11c3,_11c4,range){
var c0,psz=cu.getPageBreak(_11c3),_11c5=cu.getStartShift(_11c3,_11c4[0]||false),_11c6=_11c4;
range&&(_11c6=_11c4.slice(range[0],range[1]));
for(var rix in _11c6){
var aRow=_11c6[rix],_11c7=parseInt(attr.get(aRow,_11c8)),lx=(_11c7%psz)+_11c5,crtls=aRow.controls;
if(!crtls){
var _11c9=_fe1("*[data-control]",aRow),_11ca=new Array();
_11c9.forEach(function(cRef){
_11ca.push(dom.byId(cRef));
});
aRow.controls=_11ca;
crtls=aRow.controls;
}
if(crtls){
for(var cix in crtls){
var crtl=crtls[cix],ttl=crtl.textContent||false,_11cb=ttl?ttl+",":"";
if(crtl.nodeName=="A"){
var _11cc=_fe1("img",crtl)[0];
if(_11cc&&_fde.contains(crtl,"ac first-action-control external-link")){
var _11cd=attr.get(_11cc,"alt");
attr.set(crtl,_11ce,_11cd+","+[listcontrol.reading.anchors,lx].join(" "));
}else{
attr.set(crtl,_11ce,_11cb+[listcontrol.reading.anchors,lx].join(" "));
}
}else{
attr.set(crtl,_11ce,_11cb+[listcontrol.reading.selectors,lx].join(" "));
}
}
}
cu.updateToggler(aRow,lx);
aRow.done=true;
}
},initListActionReadings:function(_11cf){
var _11d0="curam.listMenus."+_11cf;
putTo(_11d0,[]);
dojo.subscribe("curam/listmenu/started",function(_11d1,_11d2){
var tr=_fe1(_11d1.containerNode).closest("tr")[0],lix=parseInt(attr.get(tr,_11c8)),lx=(lix%cu.getPageBreak(_11d2))+cu.getStartShift(_11d2,tr);
_11d1.set({"belongsTo":tr,"aria-labelledBy":"","aria-label":[listcontrol.reading.menus,lx].join(" ")});
_11a5(_11d0).push(_11d1);
cu.updateToggler(tr,lx);
});
},updateToggler:function(_11d3,_11d4){
_11d3.hasToggler&&attr.set(_11d3.hasToggler,_11ce,[listcontrol.reading.togglers,_11d4].join(" "));
},updateListActionReadings:function(_11d5,range){
var menus=_11a5("curam.listMenus."+_11d5),psz=cu.getPageBreak(_11d5),_11d6=false,_11d7=menus;
range&&(_11d7=menus.slice(range[0],range[1]));
for(var ix in _11d7){
var _11d8=_11d7[ix],tr=_11d8.belongsTo,lix=parseInt(attr.get(tr,_11c8)),_11d6=_11d6||cu.getStartShift(_11d5,tr),_11d9=(lix%psz)+_11d6;
_11d8.set(_11ce,[listcontrol.reading.menus,_11d9].join(" "));
cu.updateToggler(tr,_11d9);
tr.done=true;
}
},getPageBreak:function(_11da){
if(!_11db("curam.list.isPaginated."+_11da)){
return 1000;
}
if(_11a5("curam.shortlist."+_11da)){
return 1000;
}
var psz=_11a5("curam.pageBreak."+_11da)||_11a5("curam.pagination.defaultPageSize")||1000;
return psz;
},getStartShift:function(_11dc,_11dd){
if(!_11dd){
return 2;
}
var hPath="curam.listHeaderStep."+_11dc,hStep=_11a5(hPath);
if(hStep){
return hStep;
}
putTo(hPath,2);
var _11de=_fe1(_11dd).closest("table");
if(_11de.length==0){
return 2;
}
var _11df=_11de.children("thead")[0];
!_11df&&putTo(hPath,1);
return curam.listHeaderStep[_11dc];
},extendXHR:function(){
var _11e0=XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open=function(){
this.addEventListener("load",function(){
if(typeof (Storage)!=="undefined"){
var _11e1=this.getResponseHeader("sessionExpiry");
sessionStorage.setItem("sessionExpiry",_11e1);
}
});
_11e0.apply(this,arguments);
};
},suppressPaginationUpdate:false,updateDeferred:null});
var cu=curam.util,_11a5=dojo.getObject,putTo=dojo.setObject,_11db=dojo.exists,_11ce="aria-label",_11c8="data-lix";
return curam.util;
});
},"curam/dialog":function(){
define(["dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/on","dojo/sniff","curam/inspection/Layer","curam/util","curam/debug","curam/util/external","curam/util/Refresh","curam/tab","curam/util/RuntimeContext","curam/util/ScreenContext","curam/define","curam/util/onLoad","dojo/dom-class","dojo/query","dojo/NodeList-traverse"],function(dom,_11e2,_11e3,on,has,layer,util,trace,_11e4,_11e5,tab,_11e6,_11e7,_11e8,_11e9,_11ea,query){
curam.define.singleton("curam.dialog",{MODAL_PREV_FLAG:"o3modalprev",MODAL_PREV_FLAG_INPUT:"curam_dialog_prev_marker",FORCE_CLOSE:false,ERROR_MESSAGES_HEADER:"error-messages-header",_hierarchy:[],_id:null,_displayedHandlerUnsToken:null,_displayed:false,_size:null,_justClose:false,_modalExitingIEGScript:false,validTargets:{"_top":true,"_self":true},initModal:function(_11eb,_11ec,_11ed){
curam.dialog.pageId=_11eb;
curam.dialog.messagesExist=_11ec;
var _11ee=false;
var p1;
util.extendXHR();
var _11ef=util.getTopmostWindow();
var _11f0=false;
var _11f1=_11ef.dojo.subscribe("/curam/dialog/SetId",this,function(_11f2){
trace.log("curam.dialog: "+trace.getProperty("curam.dialog.id"),_11f2);
curam.dialog._id=_11f2;
_11f0=true;
_11ef.dojo.unsubscribe(_11f1);
});
_11ef.dojo.publish("/curam/dialog/init");
if(!_11f0){
trace.log("curam.dialog: "+trace.getProperty("curam.dialog.no.id"));
_11ef.dojo.unsubscribe(_11f1);
}
if(curam.dialog.closeDialog(false,_11ed)){
return;
}
curam.dialog._displayedHandlerUnsToken=util.getTopmostWindow().dojo.subscribe("/curam/dialog/displayed",null,function(_11f3,size){
if(_11f3==curam.dialog._id){
curam.dialog._displayed=true;
curam.dialog._size=size;
util.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
});
if(_11f4==undefined){
var _11f4=this.jsScreenContext;
if(!_11f4){
_11f4=new _11e7();
_11f4.addContextBits("MODAL");
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_11f4.addContextBits("AGENDA");
}
curam.util.external.inExternalApp()&&_11f4.addContextBits("EXTAPP");
}
}
if(_11f4.hasContextBits("AGENDA")||_11f4.hasContextBits("TREE")){
dojo.addOnUnload(function(){
util.getTopmostWindow().dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
});
}
dojo.addOnLoad(function(){
util.connect(dojo.body(),"onclick",curam.dialog.modalEventHandler);
for(var i=0;i<document.forms.length;i++){
var form=document.forms[i];
curam.dialog.addFormInput(form,"hidden","o3frame","modal");
var _11f5=dom.byId("o3ctx");
var sc=new curam.util.ScreenContext(_11f4.getValue());
sc.addContextBits("ACTION|ERROR");
_11f5.value=sc.getValue();
util.connect(form,"onsubmit",curam.dialog.formSubmitHandler);
}
window.curamModal=true;
});
if(curam.util.isExitingIEGScriptInModalWindow()){
delete curam.util.getTopmostWindow().exitingIEGScript;
dojo.addOnUnload(function(){
util.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}else{
var _11f6=on(window,"unload",function(){
_11f6.remove();
util.getTopmostWindow().dojo.publish("/curam/dialog/iframeUnloaded",[curam.dialog._id,window]);
});
}
if(_11f0){
dojo.publish("/curam/dialog/ready");
}
},setVariableForModalExitingIEGScript:function(){
_modalExitingIEGScript=true;
},closeDialog:function(force,_11f7){
if(force){
curam.dialog.forceClose();
}
var _11f8=curam.dialog.checkClose(curam.dialog.pageId,_11f7);
if(_11f8){
util.onLoad.addPublisher(function(_11f9){
_11f9.modalClosing=true;
});
if(curam.dialog.messagesExist){
dojo.addOnLoad(function(){
var _11fa=dom.byId(util.ERROR_MESSAGES_CONTAINER);
var _11fb=dom.byId(util.ERROR_MESSAGES_LIST);
var _11fc=dom.byId(curam.dialog.ERROR_MESSAGES_HEADER);
if(_11fb&&_11fc){
util.saveInformationalMsgs(_11f8);
util.disableInformationalLoad();
}else{
_11f8();
}
});
}else{
_11f8();
}
return true;
}
return false;
},addFormInput:function(form,type,name,value){
return _11e3.create("input",{"type":type,"name":name,"value":value},form);
},checkClose:function(_11fd,_11fe){
if(curam.dialog._justClose){
return function(){
curam.dialog.closeModalDialog();
};
}
var _11ff=curam.dialog.getParentWindow(window);
if(!_11ff){
return false;
}
var href;
if(_11fe){
href=curam.util.retrieveBaseURL()+_11fe;
}else{
href=window.location.href;
}
var _1200=curam.dialog.MODAL_PREV_FLAG;
var _1201=util.getUrlParamValue(href,_1200);
var _1202=true;
if(_1201){
if(_11ff){
if(_1201==_11fd){
_1202=false;
}
}
}else{
_1202=false;
}
var scReq=util.getUrlParamValue(href,"o3ctx");
if(scReq){
var sc=new curam.util.ScreenContext();
sc.setContext(scReq);
if(sc.hasContextBits("TREE|ACTION")){
_1202=false;
}
}
if(_1202||curam.dialog.FORCE_CLOSE){
if(!curam.dialog.FORCE_CLOSE){
if(_1201=="user-prefs-editor"){
return function(){
if(_11ff&&_11ff.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_11ff);
}
curam.dialog.closeModalDialog();
};
}
return function(){
var rp=util.removeUrlParam;
href=rp(rp(rp(href,_1200),"o3frame"),util.PREVENT_CACHE_FLAG);
href=util.adjustTargetContext(_11ff,href);
if(_11ff&&_11ff.location!==util.getTopmostWindow().location){
curam.dialog.doRedirect(_11ff,href,true);
}else{
curam.tab.getTabController().handleLinkClick(href);
}
curam.dialog.closeModalDialog();
};
}else{
return function(){
if(_11ff!==util.getTopmostWindow()){
_11ff.curam.util.loadInformationalMsgs();
}
curam.dialog.closeModalDialog();
};
}
}
return false;
},getParentWindow:function(child){
if(!child){
trace.log(["curam.dialog.getParentWindow():",trace.getProperty("curam.dialog.no.child"),window.location?" "+window.location.href:"[no location]"].join(" "));
trace.log("returning as parent = ",window.parent.location.href);
return window.parent;
}
var _1203=curam.dialog._getDialogHierarchy();
if(_1203){
for(var i=0;i<_1203.length;i++){
if(_1203[i]==child){
var _1204=(i>0)?_1203[i-1]:_1203[0];
trace.log(["curam.dialog.getParentWindow():",trace.getProperty("curam.dialog.parent.window"),_1204.location?_1204.location.href:"[no location]"].join(" "));
return _1204;
}
}
var ret=_1203.length>0?_1203[_1203.length-1]:undefined;
trace.log(["curam.dialog.getParentWindow():",trace.getProperty("curam.dialog.returning.parent"),ret?ret.location.href:"undefined"].join(" "));
return ret;
}
},_getDialogHierarchy:function(){
var _1205=util.getTopmostWindow();
_1205.require(["curam/dialog"]);
return _1205.curam.dialog._hierarchy;
},pushOntoDialogHierarchy:function(_1206){
var _1207=curam.dialog._getDialogHierarchy();
if(_1207&&dojo.indexOf(_1207,_1206)<0){
_1207.push(_1206);
trace.log(trace.getProperty("curam.dialog.add.hierarchy"),_1206.location.href);
trace.log(trace.getProperty("curam.dialog.full.hierarchy")+_1207.reduce(function(acc,hwin){
return acc+"["+(hwin.location.href||"-")+"]";
}),"");
}
},removeFromDialogHierarchy:function(child){
var _1208=curam.dialog._getDialogHierarchy();
if(!child||_1208[_1208.length-1]==child){
_1208.pop();
}else{
trace.log("curam.dialog.removeFromDialogHierarchy(): "+trace.getProperty("curam.dialog.ignore.request"));
try{
trace.log(child.location.href);
}
catch(e){
trace.log(e.message);
}
}
},stripPageOrActionFromUrl:function(url){
var idx=url.lastIndexOf("Page.do");
var len=7;
if(idx<0){
idx=url.lastIndexOf("Action.do");
len=9;
}
if(idx<0){
idx=url.lastIndexOf("Frame.do");
len=8;
}
if(idx>-1&&idx==url.length-len){
return url.substring(0,idx);
}
return url;
},_isSameBaseUrl:function(href,rtc,_1209){
if(href&&href.indexOf("#")==0){
return true;
}
var _120a=href.split("?");
var _120b=rtc.getHref().split("?");
if(_120a[0].indexOf("/")<0){
var parts=_120b[0].split("/");
_120b[0]=parts[parts.length-1];
}
if(_120b[0].indexOf("/")<0){
var parts=_120a[0].split("/");
_120a[0]=parts[parts.length-1];
}
if(_1209&&_1209==true){
_120a[0]=curam.dialog.stripPageOrActionFromUrl(_120a[0]);
_120b[0]=curam.dialog.stripPageOrActionFromUrl(_120b[0]);
}
if(_120a[0]==_120b[0]){
return true;
}
return false;
},modalEventHandler:function(event){
curam.dialog._doHandleModalEvent(event,new curam.util.RuntimeContext(window),curam.dialog.closeModalDialog,curam.dialog.doRedirect);
},_showSpinnerInDialog:function(){
curam.util.getTopmostWindow().dojo.publish("/curam/dialog/spinner");
},_doHandleModalEvent:function(e,rtc,_120c,_120d){
var _120e=e.target;
var u=util;
switch(_120e.tagName){
case "INPUT":
if(_11e2.get(_120e,"type")=="submit"&&typeof _120e.form!="undefined"){
_120e.form.setAttribute("keepModal",_120e.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
}
return true;
case "IMG":
case "SPAN":
case "DIV":
_120e=query(_120e).closest("A")[0];
if(_120e==null){
return;
}
case "A":
case "BUTTON":
if(_120e._submitButton){
_120e._submitButton.form.setAttribute("keepModal",_120e._submitButton.getAttribute("keepModal"));
curam.dialog._showSpinnerInDialog();
return;
}
break;
default:
return true;
}
var _120f=dojo.stopEvent;
var href=_120e.getAttribute("href")||_120e.getAttribute("data-href");
if(href==""){
_120c();
return false;
}
if(!href){
return false;
}
if(href.indexOf("javascript")==0){
return false;
}
var ctx=jsScreenContext;
ctx.addContextBits("MODAL");
var _1210=_120e.getAttribute("target");
if(_1210&&!curam.dialog.validTargets[_1210]){
return true;
}
if(href&&href.indexOf("/servlet/FileDownload?")>-1){
var _1211=_11e3.create("iframe",{src:href},dojo.body());
_1211.style.display="none";
_120f(e);
return false;
}
if(_11ea.contains(_120e,"external-link")){
return true;
}
if(util.isSameUrl(href,null,rtc)){
if(href.indexOf("#")<0){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_120d(window,href);
return false;
}
return true;
}
if(href&&curam.dialog._isSameBaseUrl(href,rtc,true)&&!_120e.getAttribute("keepModal")){
_120e.setAttribute("keepModal","true");
}
var _1212=curam.dialog.getParentWindow(rtc.contextObject());
if(_120e&&_120e.getAttribute){
_120f(e);
if(_120e.getAttribute("keepModal")=="true"){
href=u.replaceUrlParam(href,"o3frame","modal");
href=u.replaceUrlParam(href,"o3ctx",ctx.getValue());
_120d(window,href);
}else{
if(_1212){
href=u.removeUrlParam(href,"o3frame");
href=u.removeUrlParam(href,curam.dialog.MODAL_PREV_FLAG);
if(_1212.location!==util.getTopmostWindow().location){
var _1213=new curam.util.RuntimeContext(_1212);
var phref=_1213.getHref();
phref=u.removeUrlParam(phref,"o3frame");
if(util.isActionPage(phref)){
if(!curam.dialog._isSameBaseUrl(href,_1213,true)){
href=u.adjustTargetContext(_1212,href);
_120d(_1212,href);
}
}else{
if(!util.isSameUrl(href,phref)){
href=u.adjustTargetContext(_1212,href);
curam.dialog.doRedirect(_1212,href);
}
}
}else{
var _1214=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_1214.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_120c();
}
}
return false;
}
if(_1212&&typeof (_120e)=="undefined"||_120e==null||_120e=="_self"||_120e==""){
_120f(e);
href=href.replace(/[&?]o3frame=modal/g,"").replace("%3Fo3frame%3Dmodal","").replace("?o3frame%3Dmodal","");
href=util.updateCtx(href);
if(_1212.location!==util.getTopmostWindow().location){
_120d(_1212,href);
}else{
var _1214=new curam.util.ScreenContext("TAB");
href=u.replaceUrlParam(href,"o3ctx",_1214.getValue());
curam.tab.getTabController().handleLinkClick(href);
}
_120c();
return false;
}
return true;
},formSubmitHandler:function(e){
if(e.type=="submit"&&e.defaultPrevented){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
return false;
}
var _1215=curam.dialog.getParentWindow(window);
if(typeof _1215=="undefined"){
return true;
}
e.target.method="post";
e.target.setAttribute("target",window.name);
var _1216=e.target.action;
var _1217=curam.dialog.MODAL_PREV_FLAG;
var _1218=curam.dialog.MODAL_PREV_FLAG_INPUT;
var u=util;
var input=dom.byId(_1218);
if(input){
input.parentNode.removeChild(input);
}
if(e.target.getAttribute("keepModal")!="true"&&!jsScreenContext.hasContextBits("AGENDA")){
var _1219="multipart/form-data";
if(e.target.enctype==_1219||e.target.encoding==_1219){
e.target.action=u.removeUrlParam(_1216,_1217);
input=curam.dialog.addFormInput(e.target,"hidden",_1217,curam.dialog.pageId);
input.setAttribute("id",_1218);
input.id=_1218;
}else{
e.target.action=u.replaceUrlParam(_1216,_1217,curam.dialog.pageId);
}
}else{
e.target.action=u.removeUrlParam(_1216,_1217);
}
_1215.curam.util.invalidatePage();
if(!jsScreenContext.hasContextBits("EXTAPP")){
util.firePageSubmittedEvent("dialog");
}
return true;
},forceClose:function(){
curam.dialog.FORCE_CLOSE=true;
},forceParentRefresh:function(){
var _121a=curam.dialog.getParentWindow(window);
if(!_121a){
return;
}
_121a.curam.util.FORCE_REFRESH=true;
},forceParentLocaleRefresh:function(){
var _121b=curam.dialog.getParentWindow(window);
if(!_121b){
return;
}
_121b.curam.util.LOCALE_REFRESH=true;
},closeModalDialog:function(_121c){
var _121d=util.getTopmostWindow();
if(curam.dialog._displayedHandlerUnsToken!=null){
_121d.dojo.unsubscribe(curam.dialog._displayedHandlerUnsToken);
curam.dialog._displayedHandlerUnsToken=null;
}
var _121e=curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP");
if((typeof (curam.dialog._id)=="undefined"||curam.dialog._id==null||!_121e)&&window.frameElement){
var _121f=window.frameElement.id;
var _1220=_121f.substring(7);
curam.dialog._id=_1220;
trace.log("curam.dialog.closeModalDialog() "+trace.getProperty("curam.dialog.modal.id")+_1220);
}
util.getTopmostWindow().dojo.publish("/curam/dialog/close/appExitConfirmation",[curam.dialog._id]);
trace.log("publishing /curam/dialog/close/appExitConfirmation for ",curam.dialog._id);
trace.log("publishing /curam/dialog/close for ",curam.dialog._id);
util.getTopmostWindow().dojo.publish("/curam/dialog/close",[curam.dialog._id,_121c]);
},parseWindowOptions:function(_1221){
var opts={};
if(_1221){
trace.log("curam.dialog.parseWindowOptions "+trace.getProperty("curam.dialog.parsing"),_1221);
var _1222=_1221.split(",");
var _1223;
for(var i=0;i<_1222.length;i++){
_1223=_1222[i].split("=");
opts[_1223[0]]=_1223[1];
}
trace.log("done:",dojo.toJson(opts));
}else{
trace.log("curam.dialog.parseWindowOptions "+trace.getProperty("curam.dialog.no.options"));
}
return opts;
},doRedirect:function(_1224,href,force,_1225){
window.curamDialogRedirecting=true;
if(!curam.util.getTopmostWindow().dojo.global.jsScreenContext.hasContextBits("EXTAPP")){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/redirectingModal");
}
_1224.curam.util.redirectWindow(href,force,_1225);
},_screenReaderAnnounceCurrentTabOnWizard:function(){
var _1226=dom.byId("wizard-progress-bar");
if(_1226){
var _1227=dom.byId("hideAriaLiveElement");
if(typeof _1227!=null){
this._createSpanContainingInformationOnCurrentWizardTab(_1226,_1227);
}
}
},_stylingAddedToMandatoryIconHelp:function(){
var _1228=dom.byId("wizard-progress-bar");
var _1229=dojo.query(".mandatory-icon-help")[0];
if(_1228&&_1229){
_11ea.add(_1229,"wizard-progress-bar-exists");
}
},_createSpanContainingInformationOnCurrentWizardTab:function(_122a,_122b){
var _122c=null;
var _122d="";
var _122e=" ";
var title=query(".title",_122a)[0]&&query(".title",_122a)[0].innerText;
var desc=query(".desc",_122a)[0]&&query(".desc",_122a)[0].innerText;
if(title&&title!=""){
_122d+=title;
}
if(desc&&desc!=""){
_122d!=""?_122d+=_122e:"";
_122d+=desc;
}
var _122f=dom.byId("content");
var _1230=query(".cluster,.list",_122f)[0];
if(_1230){
if(typeof query(".collapse-title",_1230)[0]=="undefined"||query(".collapse-title",_1230)[0].innerHTML==""){
if(typeof query(".description",_1230)[0]!="undefined"){
if(query(".description",_1230)[0].innerHTML!==""){
var _1231=query(".description",_1230)[0];
if(_1231&&_1231.innerText!==""){
_122d!=""?_122d+=_122e:"";
_122d+=query(".description",_1230)[0].innerText;
}
}
}
}
}else{
var _1232=query("tr:first-child > td.field.last-cell",_122f)[0];
if(_1232&&_1232.innerText!==""){
_122d!=""?_122d+=_122e:"";
_122d+=_1232.innerText;
}
}
if(_122d){
_122c=_11e3.create("span",{innerHTML:_122d});
setTimeout(function(){
_11e3.place(_122c,_122b);
},1000);
}
},closeGracefully:function(){
curam.dialog._justClose=true;
},});
layer.register("curam/dialog",this);
return curam.dialog;
});
},"dijit/_CssStateMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/has","dojo/_base/lang","dojo/on","dojo/domReady","dojo/touch","dojo/_base/window","./a11yclick","./registry"],function(array,_1233,dom,_1234,has,lang,on,_1235,touch,win,_1236,_1237){
var _1238=_1233("dijit._CssStateMixin",[],{hovering:false,active:false,_applyAttributes:function(){
this.inherited(arguments);
array.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","_opened"],function(attr){
this.watch(attr,lang.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes||{}){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.domNode,this.baseClass);
this._setStateClass();
},_cssMouseEvent:function(event){
if(!this.disabled){
switch(event.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
this._set("hovering",true);
this._set("active",this._mouseDown);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
this._set("hovering",false);
this._set("active",false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
this._set("active",true);
break;
case "mouseup":
case "dojotouchend":
case "MSPointerUp":
case "pointerup":
case "keyup":
this._set("active",false);
break;
}
}
},_setStateClass:function(){
var _1239=this.baseClass.split(" ");
function _123a(_123b){
_1239=_1239.concat(array.map(_1239,function(c){
return c+_123b;
}),"dijit"+_123b);
};
if(!this.isLeftToRight()){
_123a("Rtl");
}
var _123c=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_123a(_123c);
}
if(this.state){
_123a(this.state);
}
if(this.selected){
_123a("Selected");
}
if(this._opened){
_123a("Opened");
}
if(this.disabled){
_123a("Disabled");
}else{
if(this.readOnly){
_123a("ReadOnly");
}else{
if(this.active){
_123a("Active");
}else{
if(this.hovering){
_123a("Hover");
}
}
}
}
if(this.focused){
_123a("Focused");
}
var tn=this.stateNode||this.domNode,_123d={};
array.forEach(tn.className.split(" "),function(c){
_123d[c]=true;
});
if("_stateClasses" in this){
array.forEach(this._stateClasses,function(c){
delete _123d[c];
});
}
array.forEach(_1239,function(c){
_123d[c]=true;
});
var _123e=[];
for(var c in _123d){
_123e.push(c);
}
var cls=_123e.join(" ");
if(cls!=tn.className){
tn.className=cls;
}
this._stateClasses=_1239;
},_subnodeCssMouseEvent:function(node,clazz,evt){
if(this.disabled||this.readOnly){
return;
}
function hover(_123f){
_1234.toggle(node,clazz+"Hover",_123f);
};
function _1240(_1241){
_1234.toggle(node,clazz+"Active",_1241);
};
function _1242(_1243){
_1234.toggle(node,clazz+"Focused",_1243);
};
switch(evt.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
hover(true);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
hover(false);
_1240(false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
_1240(true);
break;
case "mouseup":
case "MSPointerUp":
case "pointerup":
case "dojotouchend":
case "keyup":
_1240(false);
break;
case "focus":
case "focusin":
_1242(true);
break;
case "blur":
case "focusout":
_1242(false);
break;
}
},_trackMouseState:function(node,clazz){
node._cssState=clazz;
}});
_1235(function(){
function _1244(evt,_1245,_1246){
if(_1246&&dom.isDescendant(_1246,_1245)){
return;
}
for(var node=_1245;node&&node!=_1246;node=node.parentNode){
if(node._cssState){
var _1247=_1237.getEnclosingWidget(node);
if(_1247){
if(node==_1247.domNode){
_1247._cssMouseEvent(evt);
}else{
_1247._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
}
}
};
var body=win.body(),_1248;
on(body,touch.over,function(evt){
_1244(evt,evt.target,evt.relatedTarget);
});
on(body,touch.out,function(evt){
_1244(evt,evt.target,evt.relatedTarget);
});
on(body,_1236.press,function(evt){
_1248=evt.target;
_1244(evt,_1248);
});
on(body,_1236.release,function(evt){
_1244(evt,_1248);
_1248=null;
});
on(body,"focusin, focusout",function(evt){
var node=evt.target;
if(node._cssState&&!node.getAttribute("widgetId")){
var _1249=_1237.getEnclosingWidget(node);
if(_1249){
_1249._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
});
});
return _1238;
});
},"curam/tab/util":function(){
define(["dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-geometry","curam/define","curam/debug"],function(_124a,_124b,_124c,_124d,_124e,debug){
_124e.singleton("curam.tab.util",{toggleDetailsPanel:function(event){
event=dojo.fixEvent(event);
dojo.stopEvent(event);
var _124f=event.target;
var _1250=_124f;
while(_1250&&!_124b.contains(_1250,"detailsTitleArrowButton")){
_1250=_1250.parentNode;
}
var _124f=_1250;
if(_124f._animating){
return;
}
_124f._animating=true;
var _1250=_124f.parentNode;
while(_1250&&!_124b.contains(_1250,"detailsPanel-bc")){
_1250=_1250.parentNode;
}
var _1251=_1250;
while(_1250&&!_124b.contains(_1250,"summaryPane")){
_1250=_1250.parentNode;
}
var _1252=_1250;
while(_1250){
if(_124b.contains(_1250,"dijitBorderContainer")&&!_124b.contains(_1250,"detailsPanel-bc")){
break;
}
if(_124b.contains(_1250,"tab-wrapper")){
break;
}
_1250=_1250.parentNode;
}
var _1253=_1250;
headerPanelNode=dojo.query(".detailsPanelTitleBar",_1251)[0];
detailsPanelNode=dojo.query(".detailsContentPane",_1251)[0];
var kids=_1253.children;
var _1254=dojo.filter(kids,function(child){
if(_124b.contains(child,"splitter-pane")||_124b.contains(child,"dijitSplitterH")){
return child;
}
})[0];
var _1255=dojo.filter(kids,function(child){
if(_124b.contains(child,"nav-panel")){
return child;
}
})[0];
var _1256=_124d.getMarginBoxSimple(headerPanelNode).h;
var _1257=_124d.getMarginBoxSimple(_1252).h;
var _1258=_1254.offsetHeight;
var _1259=_124d.getMarginBoxSimple(_1255).h;
var _125a=dojo.query(".detailsContentPane",_1251)[0];
if(_1256!=_1252.clientHeight){
_124b.add(_124f,"collapsed");
_124c.set(_124f,"aria-expanded","false");
this._updateToggleArrowNode(_124f,false);
_124b.add(_125a,"collapsed");
curam.debug.log(debug.getProperty("curam.tab.util.collapsing"));
_1251._previousHeight=_1257;
_1255._previousHeight=_1259;
dojo.animateProperty({node:_1252,duration:500,properties:{height:{end:_1256}}}).play();
if(_124b.contains(_1254,"splitter-pane")){
dojo.animateProperty({node:detailsPanelNode,duration:500,properties:{height:{end:0}}}).play();
}
dojo.animateProperty({node:_1254,duration:500,properties:{top:{end:(_1256+_1258)}}}).play();
dojo.animateProperty({node:_1255,duration:500,properties:{top:{end:(_1256+_1258)}},onEnd:function(){
_124f._animating=false;
if(_124b.contains(_1254,"dijitSplitterH")){
_124a.set(_1255,"height",(_1255._previousHeight+_1251._previousHeight-_1256)+"px");
}
}}).play();
}else{
_124b.remove(_124f,"collapsed");
_124b.remove(_125a,"collapsed");
_124c.set(_124f,"aria-expanded","true");
this._updateToggleArrowNode(_124f,true);
debug.log(debug.getProperty("curam.tab.util.expanding"));
_124a.set(_1252,"height",_1251._previousHeight+"px");
if(_124b.contains(_1254,"splitter-pane")){
dojo.animateProperty({node:detailsPanelNode,duration:500,properties:{height:{end:_1251._previousHeight-_1256}}}).play();
}
dojo.animateProperty({node:_1254,duration:500,properties:{top:{end:(_1251._previousHeight+_1258)}}}).play();
dojo.animateProperty({node:_1255,duration:500,properties:{top:{end:(_1251._previousHeight+_1258)}},onEnd:function(){
_124f._animating=false;
if(_124b.contains(_1254,"dijitSplitterH")){
_124a.set(_1255,"height",_1255._previousHeight+"px");
}
}}).play();
}
},_updateToggleArrowNode:function(_125b,_125c){
var _125d=this._getToggleImages();
if(_125c){
_125b.children[0].src=_125d[0];
_125b.children[1].src=_125d[2];
}else{
_125b.children[0].src=_125d[1];
_125b.children[1].src=_125d[3];
}
},_getToggleImages:function(){
var _125e;
var _125f;
var _1260;
var _1261;
var _1262=curam.util.isRtlMode();
_125e="./themes/curam/images/chevron--down20-enabled.svg";
_1260="./themes/curam/images/chevron--down20-enabled.svg";
if(_1262){
_125f="./themes/curam/images/Toggle_Right_Blue80_20px.png";
_1261="./themes/curam/images/Toggle_Fill_Right_Blue50_20px.png";
}else{
_125f="./themes/curam/images/chevron--left20-enabled.svg";
_1261="./themes/curam/images/chevron--left20-enabled.svg";
}
return [_125e,_125f,_1260,_1261];
},});
return curam.tab.util;
});
},"curam/widget/DeferredDropDownButton":function(){
define(["dijit/registry","dijit/form/DropDownButton","dojo/_base/declare","dojo/text!curam/widget/templates/DropDownButton.html","dojo/dom-attr","dojo/dom-construct","dojo/query","dijit/form/Button","dijit/MenuItem","curam/util/TabActionsMenu","curam/debug","curam/util","dijit/MenuSeparator","dijit/Menu","dijit/MenuItem","curam/widget/_HasDropDown"],function(_1263,_1264,_1265,_1266,_1267,_1268,query,_1269,_126a,_126b,debug){
var _126c=_1265("curam.widget.DeferredDropDownButton",[dijit.form.DropDownButton,curam.widget._HasDropDown],{templateString:_1266,o3tabId:null,startup:function(){
if(this._started){
return;
}
var _126d=_1267.get(this.domNode,"class").split(" ");
dojo.forEach(_126d,dojo.hitch(this,function(_126e){
if(_126e.indexOf("tab-widget-id-")!=-1){
this.o3tabId=_126e.slice(14,_126e.length);
}
}));
this.widgetTemplate=curam.widgetTemplates?curam.widgetTemplates[this.id]:null;
dijit.form.Button.prototype.startup.apply(this);
var _126f=this.get("data-notify");
_126f&&dojo.publish("curam/listmenu/started",[this,_126f]);
},toggleDropDown:function(){
if(!this.dropDown&&this.widgetTemplate){
if(this.widgetTemplate.indexOf("__03qu_")!=-1){
this.widgetTemplate=this.widgetTemplate.split("__03qu_").join("\"");
}
this.widgetTemplate=this.widgetTemplate.split("&lt;").join("<").split("&gt;").join(">").split("&amp;").join("&").split("&quot;").join("'");
var _1270=_1268.create("div",{innerHTML:this.widgetTemplate,style:{display:"none"}},dojo.body());
this.dropDown=dojo.parser.parse(_1270)[0];
var menu=_1263.byNode(_1270.firstChild);
if(menu.getChildren().length==0){
var mi=new dijit.MenuItem({disabled:true,label:LOCALISED_EMPTY_MENU_MARKER});
menu.addChild(mi);
}
this.widgetTemplate=null;
debug.log(debug.getProperty("curam.widget.DeferredDropDownButton.publish")+" /curam/menu/created "+debug.getProperty("curam.widget.DeferredDropDownButton.for"),this.o3tabId);
var _1271=curam.util.getTopmostWindow();
_1271.dojo.publish("/curam/menu/created",[this.o3tabId]);
if(this.o3tabId){
_126b.hideTabMenuOverflowItems(this.o3tabId,this.dropDown.domNode);
}
}
this.inherited(arguments);
}});
return _126c;
});
},"dijit/form/FilteringSelect":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/when","./MappedTextBox","./ComboBoxMixin"],function(_1272,lang,when,_1273,_1274){
return _1272("dijit.form.FilteringSelect",[_1273,_1274],{required:true,_lastDisplayedValue:"",_isValidSubset:function(){
return this._opened;
},isValid:function(){
return !!this.item||(!this.required&&this.get("displayedValue")=="");
},_refreshState:function(){
if(!this.searchTimer){
this.inherited(arguments);
}
},_callbackSetLabel:function(_1275,query,_1276,_1277){
if((query&&query[this.searchAttr]!==this._lastQuery)||(!query&&_1275.length&&this.store.getIdentity(_1275[0])!=this._lastQuery)){
return;
}
if(!_1275.length){
this.set("value","",_1277||(_1277===undefined&&!this.focused),this.textbox.value,null);
}else{
this.set("item",_1275[0],_1277);
}
},_openResultList:function(_1278,query,_1279){
if(query[this.searchAttr]!==this._lastQuery){
return;
}
this.inherited(arguments);
if(this.item===undefined){
this.validate(true);
}
},_getValueAttr:function(){
return this.valueNode.value;
},_getValueField:function(){
return "value";
},_setValueAttr:function(value,_127a,_127b,item){
if(!this._onChangeActive){
_127a=null;
}
if(item===undefined){
if(value===null||value===""){
value="";
if(!lang.isString(_127b)){
this._setDisplayedValueAttr(_127b||"",_127a);
return;
}
}
var self=this;
this._lastQuery=value;
when(this.store.get(value),function(item){
self._callbackSetLabel(item?[item]:[],undefined,undefined,_127a);
});
}else{
this.valueNode.value=value;
this.inherited(arguments,[value,_127a,_127b,item]);
}
},_setItemAttr:function(item,_127c,_127d){
this.inherited(arguments);
this._lastDisplayedValue=this.textbox.value;
},_getDisplayQueryString:function(text){
return text.replace(/([\\\*\?])/g,"\\$1");
},_setDisplayedValueAttr:function(label,_127e){
if(label==null){
label="";
}
if(!this._created){
if(!("displayedValue" in this.params)){
return;
}
_127e=false;
}
if(this.store){
this.closeDropDown();
var query=lang.clone(this.query);
var qs=this._getDisplayQueryString(label),q;
if(this.store._oldAPI){
q=qs;
}else{
q=this._patternToRegExp(qs);
q.toString=function(){
return qs;
};
}
this._lastQuery=query[this.searchAttr]=q;
this.textbox.value=label;
this._lastDisplayedValue=label;
this._set("displayedValue",label);
var _127f=this;
var _1280={queryOptions:{ignoreCase:this.ignoreCase,deep:true}};
lang.mixin(_1280,this.fetchProperties);
this._fetchHandle=this.store.query(query,_1280);
when(this._fetchHandle,function(_1281){
_127f._fetchHandle=null;
_127f._callbackSetLabel(_1281||[],query,_1280,_127e);
},function(err){
_127f._fetchHandle=null;
if(!_127f._cancelingQuery){
console.error("dijit.form.FilteringSelect: "+err.toString());
}
});
}
},undo:function(){
this.set("displayedValue",this._lastDisplayedValue);
}});
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_1282,array,_1283,_1284,_1285,_1286,dom,_1287,_1288,_1289,_128a,_128b,has,_128c,lang,on,ready,_128d,topic,win,_128e,_128f,_1290){
var _1291=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_128c.isAsync);
has.add("dojo-bidi",false);
if(1){
ready(0,function(){
var _1292=["dijit/_base/manager"];
_1282(_1292);
});
}
var _1293={};
function _1294(obj){
var ret={};
for(var attr in obj){
ret[attr.toLowerCase()]=true;
}
return ret;
};
function _1295(attr){
return function(val){
_1287[val?"set":"remove"](this.domNode,attr,val);
this._set(attr,val);
};
};
function _1296(a,b){
return a===b||(a!==a&&b!==b);
};
var _1297=_1286("dijit._WidgetBase",[_128d,_128e],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_1295("lang"),dir:"",_setDirAttr:_1295("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_1284.blankGif||_1282.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var ctor=this.constructor;
if(!ctor._setterAttrs){
var proto=ctor.prototype,attrs=ctor._setterAttrs=[],onMap=(ctor._onMap={});
for(var name in proto.attributeMap){
attrs.push(name);
}
for(name in proto){
if(/^on/.test(name)){
onMap[name.substring(2).toLowerCase()]=name;
}
if(/^_set[A-Z](.*)Attr$/.test(name)){
name=name.charAt(4).toLowerCase()+name.substr(5,name.length-9);
if(!proto.attributeMap||!(name in proto.attributeMap)){
attrs.push(name);
}
}
}
}
},postscript:function(_1298,_1299){
this.create(_1298,_1299);
},create:function(_129a,_129b){
if(_1291){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_129b);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_129a){
this.params=_129a;
lang.mixin(this,_129a);
}
this.postMixInProperties();
if(!this.id){
this.id=_1290.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_1290.add(this);
this.buildRendering();
var _129c;
if(this.domNode){
this._applyAttributes();
var _129d=this.srcNodeRef;
if(_129d&&_129d.parentNode&&this.domNode!==_129d){
_129d.parentNode.replaceChild(this.domNode,_129d);
_129c=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_129c){
delete this.srcNodeRef;
}
this._created=true;
if(_1291){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _129e={};
for(var key in this.params||{}){
_129e[key]=this._get(key);
}
array.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _129e)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _129e){
this.set(key,_129e[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _129f=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_129f=_129f.concat(array.map(_129f,function(name){
return name+"Rtl";
}));
}
_1288.add(this.domNode,_129f);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
array.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_12a0){
this._beingDestroyed=true;
this.destroyDescendants(_12a0);
this.destroy(_12a0);
},destroy:function(_12a1){
this._beingDestroyed=true;
this.uninitialize();
function _12a2(w){
if(w.destroyRecursive){
w.destroyRecursive(_12a1);
}else{
if(w.destroy){
w.destroy(_12a1);
}
}
};
array.forEach(this._connects,lang.hitch(this,"disconnect"));
array.forEach(this._supportingWidgets,_12a2);
if(this.domNode){
array.forEach(_1290.findWidgets(this.domNode,this.containerNode),_12a2);
}
this.destroyRendering(_12a1);
_1290.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_12a3){
if(this.bgIframe){
this.bgIframe.destroy(_12a3);
delete this.bgIframe;
}
if(this.domNode){
if(_12a3){
_1287.remove(this.domNode,"widgetId");
}else{
_1289.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_12a3){
_1289.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_12a4){
array.forEach(this.getChildren(),function(_12a5){
if(_12a5.destroyRecursive){
_12a5.destroyRecursive(_12a4);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(value){
var _12a6=this.domNode;
if(lang.isObject(value)){
_128b.set(_12a6,value);
}else{
if(_12a6.style.cssText){
_12a6.style.cssText+="; "+value;
}else{
_12a6.style.cssText=value;
}
}
this._set("style",value);
},_attrToDom:function(attr,value,_12a7){
_12a7=arguments.length>=3?_12a7:this.attributeMap[attr];
array.forEach(lang.isArray(_12a7)?_12a7:[_12a7],function(_12a8){
var _12a9=this[_12a8.node||_12a8||"domNode"];
var type=_12a8.type||"attribute";
switch(type){
case "attribute":
if(lang.isFunction(value)){
value=lang.hitch(this,value);
}
var _12aa=_12a8.attribute?_12a8.attribute:(/^on[A-Z][a-zA-Z]*$/.test(attr)?attr.toLowerCase():attr);
if(_12a9.tagName){
_1287.set(_12a9,_12aa,value);
}else{
_12a9.set(_12aa,value);
}
break;
case "innerText":
_12a9.innerHTML="";
_12a9.appendChild(this.ownerDocument.createTextNode(value));
break;
case "innerHTML":
_12a9.innerHTML=value;
break;
case "class":
_1288.replace(_12a9,value,this[attr]);
break;
}
},this);
},get:function(name){
var names=this._getAttrNames(name);
return this[names.g]?this[names.g]():this._get(name);
},set:function(name,value){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var names=this._getAttrNames(name),_12ab=this[names.s];
if(lang.isFunction(_12ab)){
var _12ac=_12ab.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _12ad=this.focusNode&&!lang.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_12ad]&&this[_12ad].tagName,_12ae=tag&&(_1293[tag]||(_1293[tag]=_1294(this[_12ad]))),map=name in this.attributeMap?this.attributeMap[name]:names.s in this?this[names.s]:((_12ae&&names.l in _12ae&&typeof value!="function")||/^aria-|^data-|^role$/.test(name))?_12ad:null;
if(map!=null){
this._attrToDom(name,value,map);
}
this._set(name,value);
}
return _12ac||this;
},_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
var uc=name.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[name]={n:name+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(name,value){
var _12af=this[name];
this[name]=value;
if(this._created&&!_1296(_12af,value)){
if(this._watchCallbacks){
this._watchCallbacks(name,_12af,value);
}
this.emit("attrmodified-"+name,{detail:{prevValue:_12af,newValue:value}});
}
},_get:function(name){
return this[name];
},emit:function(type,_12b0,_12b1){
_12b0=_12b0||{};
if(_12b0.bubbles===undefined){
_12b0.bubbles=true;
}
if(_12b0.cancelable===undefined){
_12b0.cancelable=true;
}
if(!_12b0.detail){
_12b0.detail={};
}
_12b0.detail.widget=this;
var ret,_12b2=this["on"+type];
if(_12b2){
ret=_12b2.apply(this,_12b1?_12b1:[_12b0]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,type.toLowerCase(),_12b0);
}
return ret;
},on:function(type,func){
var _12b3=this._onMap(type);
if(_12b3){
return _1283.after(this,_12b3,func,true);
}
return this.own(on(this.domNode,type,func))[0];
},_onMap:function(type){
var ctor=this.constructor,map=ctor._onMap;
if(!map){
map=(ctor._onMap={});
for(var attr in ctor.prototype){
if(/^on/.test(attr)){
map[attr.replace(/^on/,"").toLowerCase()]=attr;
}
}
}
return map[typeof type=="string"&&type.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_1290.findWidgets(this.containerNode):[];
},getParent:function(){
return _1290.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,event,_12b4){
return this.own(_1285.connect(obj,event,this,_12b4))[0];
},disconnect:function(_12b5){
_12b5.remove();
},subscribe:function(t,_12b6){
return this.own(topic.subscribe(t,lang.hitch(this,_12b6)))[0];
},unsubscribe:function(_12b7){
_12b7.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_128a.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_128b.get(this.domNode,"display")!="none");
},placeAt:function(_12b8,_12b9){
var _12ba=!_12b8.tagName&&_1290.byId(_12b8);
if(_12ba&&_12ba.addChild&&(!_12b9||typeof _12b9==="number")){
_12ba.addChild(this,_12b9);
}else{
var ref=_12ba&&("domNode" in _12ba)?(_12ba.containerNode&&!/after|before|replace/.test(_12b9||"")?_12ba.containerNode:_12ba.domNode):dom.byId(_12b8,this.ownerDocument);
_1289.place(this.domNode,ref,_12b9);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,delay){
var timer=setTimeout(lang.hitch(this,function(){
if(!timer){
return;
}
timer=null;
if(!this._destroyed){
lang.hitch(this,fcn)();
}
}),delay||0);
return {remove:function(){
if(timer){
clearTimeout(timer);
timer=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_1297.extend(_128f);
}
return _1297;
});
},"dojo/cookie":function(){
define(["./_base/kernel","./regexp"],function(dojo,_12bb){
dojo.cookie=function(name,value,props){
var c=document.cookie,ret;
if(arguments.length==1){
var _12bc=c.match(new RegExp("(?:^|; )"+_12bb.escapeString(name)+"=([^;]*)"));
ret=_12bc?decodeURIComponent(_12bc[1]):undefined;
}else{
props=props||{};
var exp=props.expires;
if(typeof exp=="number"){
var d=new Date();
d.setTime(d.getTime()+exp*24*60*60*1000);
exp=props.expires=d;
}
if(exp&&exp.toUTCString){
props.expires=exp.toUTCString();
}
value=encodeURIComponent(value);
var _12bd=name+"="+value,_12be;
for(_12be in props){
_12bd+="; "+_12be;
var _12bf=props[_12be];
if(_12bf!==true){
_12bd+="="+_12bf;
}
}
document.cookie=_12bd;
}
return ret;
};
dojo.cookie.isSupported=function(){
if(!("cookieEnabled" in navigator)){
this("__djCookieTest__","CookiesAllowed");
navigator.cookieEnabled=this("__djCookieTest__")=="CookiesAllowed";
if(navigator.cookieEnabled){
this("__djCookieTest__","",{expires:-1});
}
}
return navigator.cookieEnabled;
};
return dojo.cookie;
});
},"curam/cdsl/store/CuramStore":function(){
define(["dojo/_base/declare","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","curam/cdsl/store/IdentityApi","dojo/store/util/QueryResults","dojo/_base/lang","curam/cdsl/_base/_Connection"],function(_12c0,_12c1,_12c2,_12c3,_12c4,_12c5,lang){
var _12c6={query:"listItems",get:"read",put:"modify",add:"insert",remove:"remove"};
var _12c7={identityApi:new _12c4(),dataAdapter:null};
var _12c8=function(_12c9){
var o=lang.clone(_12c7);
if(_12c9&&_12c9.getIdentity&&_12c9.parseIdentity&&_12c9.getIdentityPropertyNames){
o.identityApi=_12c9;
}else{
o=lang.mixin(o,_12c9);
}
return o;
};
var Store=null;
var _12ca=_12c0(Store,{_service:null,_baseFacadeName:null,_identityApi:null,constructor:function(_12cb,_12cc,opts){
var _12cd=_12c8(opts);
this._service=new _12c1(_12cb,{dataAdapter:_12cd.dataAdapter});
this._baseFacadeName=_12cc;
this._identityApi=_12cd.identityApi;
},get:function(_12ce){
var _12cf=new _12c3(this._identityApi.parseIdentity(_12ce));
var _12d0=new _12c2(this._baseFacadeName,_12c6.get,[_12cf]);
return this._service.call([_12d0]).then(function(data){
return data[0];
});
},getIdentity:function(_12d1){
return this._identityApi.getIdentity(_12d1);
},query:function(query,_12d2){
var _12d3=new _12c2(this._baseFacadeName,_12c6.query,[new _12c3(query)]);
if(_12d2){
_12d3._setMetadata({queryOptions:{offset:_12d2.start,count:_12d2.count,sort:_12d2.sort}});
}
var _12d4=this._service.call([_12d3]).then(function(data){
return data[0].dtls;
});
return new _12c5(_12d4);
},put:function(_12d5,_12d6){
if(_12d6&&typeof _12d6.overwrite!=="undefined"&&!_12d6.overwrite){
throw new Error("The overwrite option is set to false, but adding new items "+"via CuramStore.put() is not supported.");
}
return this._addOrPut(_12c6.put,_12d5,_12d6,"putOptions");
},add:function(_12d7,_12d8){
var opts={};
if(_12d8){
opts=lang.mixin(opts,_12d8);
}
opts.overwrite=false;
return this._addOrPut(_12c6.add,_12d7,opts,"addOptions");
},_addOrPut:function(_12d9,_12da,_12db,_12dc){
var _12dd=_12da;
if(!_12dd.isInstanceOf||!_12dd.isInstanceOf(_12c3)){
_12dd=new _12c3(_12da);
}
var _12de=new _12c2(this._baseFacadeName,_12d9,[_12dd]);
if(_12db){
var _12df={};
_12df[_12dc]={id:_12db.id?_12db.id:null,before:_12db.before?this.getIdentity(_12db.before):null,parent:_12db.parent?this.getIdentity(_12db.parent):null,overwrite:false};
_12de._setMetadata(_12df);
}
return this._service.call([_12de]).then(lang.hitch(this,function(data){
if(_12db&&_12db.id){
return _12db.id;
}
if(_12db&&_12db.overwrite){
return this.getIdentity(_12dd);
}
return this.getIdentity(data[0]);
}));
},remove:function(_12e0){
var _12e1=new _12c3(this._identityApi.parseIdentity(_12e0)),_12e2=new _12c2(this._baseFacadeName,_12c6.remove,[_12e1]);
return this._service.call([_12e2]).then(function(data){
return _12e0;
});
}});
return _12ca;
});
},"curam/dcl":function(){
define(["dojo/_base/connect","curam/define","curam/debug","dojo/query","dojo/dom-class","dijit/registry"],function(_12e3,def,debug,query,_12e4,_12e5){
def.singleton("curam.dcl",{CLUSTER_SHOW:true,CLUSTER_HIDE:false,DCL_PAGE_CLUSTER_INITVALS_KEY:"curam.dcl.page_cluster_intvals_key",initialValues:{},rawItems:{},controls:{},clusters:{},states:{},getters:{},clusterRecords:{},fieldRecords:{},togglesArray:[],fnRoot:{"refs":[]},pageLoaded:false,init:function(){
for(var _12e6 in curam.dcl.clusterRecords){
var cl=query("."+_12e6)[0];
if(cl){
curam.dcl.clusters[_12e6]=cl;
}else{
console.warn("Dynamic cluster",_12e6," declared on the page but not associated!");
}
}
for(var _12e7 in curam.dcl.rawItems){
curam.dcl.fieldRecords[_12e7]=curam.dcl.initRef(curam.dcl.rawItems[_12e7]);
}
for(var _12e8 in curam.dcl.fieldRecords){
curam.dcl.controls[_12e8]=curam.dcl.fieldRecords[_12e8];
}
dojo.subscribe("curam/dcl/execute",curam.dcl.evaluateExpression);
curam.dcl.evaluateExpression(false);
for(var _12e9 in curam.dcl.clusterRecords){
curam.dcl.transferValuesFromDomToStorage(_12e9);
if(curam.dcl.states[_12e9]==curam.dcl.CLUSTER_HIDE){
curam.dcl.blankFields(_12e9);
}
}
},addControlVar:function(_12ea,val){
curam.dcl.fieldRecords[_12ea]=curam.dcl.initVar(val);
},addControlRef:function(_12eb,_12ec){
curam.dcl.fieldRecords[_12eb]=curam.dcl.initRef(_12ec);
},bindCluster:function(_12ed,_12ee,_12ef){
var _12f0=dojo.getObject(_12ef);
var _12f1={"fnRef":_12f0,"clId":_12ed};
if(_12ee!=""){
var _12f2=dojo.getObject(_12ee);
if(!_12f2.refs){
_12f2.refs=[];
}
_12f2.refs.push(_12f1);
}else{
curam.dcl.fnRoot.refs.push(_12f1);
}
curam.dcl.clusterRecords[_12ed]=_12f0;
curam.dcl.states[_12ed]=curam.dcl.CLUSTER_HIDE;
},setGetter:function(_12f3,_12f4){
curam.dcl.getters[_12f3]=_12f4;
},getField:function(_12f5){
if(!_12f5){
throw Error("You must specify a field name");
}
var value=undefined;
try{
value=curam.dcl.controls[_12f5].apply();
}
catch(e){
debug.log(debug.getProperty("curam.dcl.field.error")+_12f5);
debug.log(debug.getProperty("curam.dcl.field.valid",[_12f5]));
}
if(value){
curam.debug.log("curam.dcl.getField("+_12f5+") - "+value);
}
return value;
},evaluateRefs:function(_12f6){
for(var i=0;i<_12f6.length;i++){
var aRef=_12f6[i];
var _12f7=aRef.fnRef.apply();
if(_12f7!=curam.dcl.states[aRef.clId]){
curam.dcl.togglesArray.push(aRef.clId);
}
if(aRef.fnRef.refs!=null){
curam.dcl.evaluateRefs(aRef.fnRef.refs);
}
}
},evaluateExpression:function(_12f8){
curam.dcl.pageLoaded=false;
if(!_12f8){
curam.dcl.pageLoaded=true;
}
curam.dcl.evaluateRefs(curam.dcl.fnRoot.refs);
var count=0;
var limit=1000;
while(curam.dcl.togglesArray.length>0&&count<limit){
count++;
for(var i=0;i<curam.dcl.togglesArray.length;i++){
var _12f9=query("."+curam.dcl.togglesArray[i])[0];
curam.dcl.toggleCluster(curam.dcl.pageLoaded,_12f9,curam.dcl.togglesArray[i]);
}
curam.dcl.togglesArray=[];
curam.dcl.evaluateRefs(curam.dcl.fnRoot.refs);
}
if(count>=limit){
debug.log(debug.getProperty("curam.dcl.field.inifinte.loop.info",[limit]));
}
},toggleCluster:function(_12fa,_12fb,_12fc){
curam.dcl.states[_12fc]=!curam.dcl.states[_12fc];
if(curam.dcl.states[_12fc]==curam.dcl.CLUSTER_SHOW){
_12e4.remove(_12fb,"hide-dynamic-cluster");
curam.dcl.transferValuesFromStorageToDom(_12fc);
}else{
_12e4.add(_12fb,"hide-dynamic-cluster");
curam.dcl.blankFields(_12fc);
}
curam.dcl.animateCluster(_12fa,_12fb,_12fc);
},animateCluster:function(_12fd,_12fe,_12ff){
if(!_12fe||_12fe.inAnimation){
return;
}
require(["dojo/fx"],function(fx){
var _1300={node:_12fe,duration:200,onBegin:function(){
_12fe.inAnimation=true;
},onEnd:function(){
_12fe.inAnimation=false;
}};
var _1301={node:_12fe,duration:200,onBegin:function(){
_12fe.inAnimation=true;
},onEnd:function(){
_12fe.inAnimation=false;
}};
if(_12fd||curam.dcl.states[_12ff]==curam.dcl.CLUSTER_SHOW){
fx.wipeIn(_1300).play();
}else{
if(curam.dcl.states[_12ff]==curam.dcl.CLUSTER_HIDE){
fx.wipeOut(_1301).play();
}else{
}
}
});
},transferValuesFromStorageToDom:function(_1302){
var _1303=query("."+_1302)[0];
var _1304=curam.dcl.initialValues[_1302];
var _1305=query("table input[value]",_1303);
for(var i=0;i<_1305.length;i++){
textNode=_1305[i];
if(_1304){
var _1306=_1304[i].value;
textNode.value=_1306;
if(textNode.id&&_1304[i]._lastValueReported){
var _1307=this.getDropdownWidget(textNode.id);
_1307._lastValueReported=_1304[i]._lastValueReported;
}
}else{
}
}
},transferValuesFromDomToStorage:function(_1308){
var _1309=query("."+_1308)[0];
var _130a=query("table input[value]",_1309);
var _130b=[];
for(var i=0;i<_130a.length;i++){
var _130c=_130a[i];
var _130d={id:_130c.id,value:_130c.value};
var _130e=this.getDropdownWidget(_130c.id);
if(_130e){
_130d._lastValueReported=_130e._lastValueReported;
}
_130b.push(_130d);
}
curam.dcl.initialValues[_1308]=_130b;
},blankFields:function(_130f){
var _1310=query("."+_130f)[0];
var _1311=query("table input[value]",_1310);
for(var i=0;i<_1311.length;i++){
textNode=_1311[i];
if(textNode.type==="hidden"&&textNode.getAttribute("dcl-blankable")==="false"){
debug.log(debug.getProperty("curam.dcl.skip.blank"),textNode);
}else{
textNode.value="";
}
if(textNode.type==="checkbox"||textNode.type==="radio"){
textNode.checked=false;
}
var _1312=this.getDropdownWidget(textNode.id);
if(_1312){
_1312._lastValueReported="";
}
}
},getDropdownWidget:function(id){
if(id){
var _1313=_12e5.byId(id);
return _1313;
}
},initRef:function(fName){
var ref=document.getElementsByName(fName);
var _1314=curam.dcl.getters[fName];
var _1315=function(){
return ref[0].selectedValue||ref[0].value;
};
if(ref.length>1){
_1315=function(){
for(var i=0;i<ref.length;i++){
if(ref[i].checked){
return ref[i].value;
}
}
return "";
};
}else{
if(ref[0].type=="checkbox"){
_1315=function(){
return document.getElementsByName(fName)[0].checked;
};
}
}
return function(){
return _1314?_1314.apply(ref):_1315.apply();
};
},initVar:function(value){
return function(){
return value;
};
}});
return curam.dcl;
});
},"dijit/MenuBarItem":function(){
define(["dojo/_base/declare","./MenuItem","dojo/text!./templates/MenuBarItem.html"],function(_1316,_1317,_1318){
var _1319=_1316("dijit._MenuBarItemMixin",null,{templateString:_1318,_setIconClassAttr:null});
var _131a=_1316("dijit.MenuBarItem",[_1317,_1319],{});
_131a._MenuBarItemMixin=_1319;
return _131a;
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./domReady","./sniff","./_base/window"],function(_131b,lang,_131c,has,_131d){
var html=_131d.doc.documentElement,ie=has("ie"),_131e=has("trident"),opera=has("opera"),maj=Math.floor,ff=has("ff"),_131f=_131b.boxModel.replace(/-/,""),_1320={"dj_quirks":has("quirks"),"dj_opera":opera,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_edge":has("edge"),"dj_gecko":has("mozilla"),"dj_ios":has("ios"),"dj_android":has("android")};
if(ie){
_1320["dj_ie"]=true;
_1320["dj_ie"+maj(ie)]=true;
_1320["dj_iequirks"]=has("quirks");
}
if(_131e){
_1320["dj_trident"]=true;
_1320["dj_trident"+maj(_131e)]=true;
}
if(ff){
_1320["dj_ff"+maj(ff)]=true;
}
_1320["dj_"+_131f]=true;
var _1321="";
for(var clz in _1320){
if(_1320[clz]){
_1321+=clz+" ";
}
}
html.className=lang.trim(html.className+" "+_1321);
_131c(function(){
if(!_131b.isBodyLtr()){
var _1322="dj_rtl dijitRtl "+_1321.replace(/ /g,"-rtl ");
html.className=lang.trim(html.className+" "+_1322+"dj_rtl dijitRtl "+_1321.replace(/ /g,"-rtl "));
}
});
return has;
});
},"curam/util/Refresh":function(){
define(["dijit/registry","dojo/dom-class","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/ResourceBundle","curam/util","curam/tab","curam/util/ContextPanel","curam/util/ui/refresh/TabRefreshController"],function(_1323,_1324,_1325,layer,_1326,_1327,debug,_1328){
_1327.singleton("curam.util.Refresh",{submitted:false,pageSubmitted:"",refreshConfig:[],menuBarCallback:null,navigationCallback:null,_controllers:{},_pageRefreshButton:undefined,setMenuBarCallbacks:function(_1329,_132a){
if(!curam.util.Refresh.menuBarCallback){
curam.util.Refresh.menuBarCallback={updateMenuItemStates:_1329,getRefreshParams:_132a};
}
},setNavigationCallbacks:function(_132b,_132c){
if(!curam.util.Refresh.navigationCallback){
curam.util.Refresh.navigationCallback={updateNavItemStates:_132b,getRefreshParams:_132c};
}
},refreshMenuAndNavigation:function(_132d,_132e,_132f){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"tabWidgetId=%s, refreshMenuBar || refreshNavigation: %s || %s",_132d,_132e,_132f);
if(!_132e&&!_132f){
curam.debug.log(debug.getProperty("curam.util.Refresh.no.refresh"));
return;
}
var _1330={update:function(_1331,_1332,_1333){
curam.debug.log(debug.getProperty("curam.util.Refresh.dynamic.refresh"),_1332);
var ncb=curam.util.Refresh.navigationCallback;
curam.debug.log("refreshNavigation? ",_132f);
if(_132f&&_1332.navData&&ncb){
ncb.updateNavItemStates(_1331,_1332);
}
var mcb=curam.util.Refresh.menuBarCallback;
curam.debug.log("refreshMenuBar? ",_132e);
if(_132e&&_1332.menuData&&mcb){
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"dynamic tab menu item update");
mcb.updateMenuItemStates(_1331,_1332);
}else{
curam.debug.log("curam.util.Refresh.refreshMenuAndNavigation: "+"no dynamic data, updating initially loaded tab action items to show"+"only those that should be inlined");
curam.util.TabActionsMenu.manageInlineTabMenuStates(_1331);
}
},error:function(error,_1334){
curam.debug.log("========= "+debug.getProperty("curam.util.Refresh.dynamic.failure")+" ===========");
curam.debug.log(debug.getProperty("curam.util.Refresh.dynamic.error"),error);
curam.debug.log(debug.getProperty("curam.util.Refresh.dynamic.args"),_1334);
curam.debug.log("==================================================");
}};
var _1335="servlet/JSONServlet?o3c=TAB_DYNAMIC_STATE_QUERY",_1336=false;
var mcb=curam.util.Refresh.menuBarCallback;
if(_132e&&mcb){
var _1337=mcb.getRefreshParams(_132d);
if(_1337){
_1335+="&"+_1337;
_1336=true;
}
}
var ncb=curam.util.Refresh.navigationCallback;
if(_132f&&ncb){
var _1338=ncb.getRefreshParams(_132d);
if(_1338){
_1335+="&"+_1338;
_1336=true;
}
}
curam.debug.log(debug.getProperty("curam.util.Refresh.dynamic.refresh.req"));
if(_1336){
_1326.post({url:_1335,handleAs:"json",preventCache:true,load:dojo.hitch(_1330,"update",_132d),error:dojo.hitch(_1330,"error")});
}else{
curam.util.TabActionsMenu.manageInlineTabMenuStates(_132d);
curam.debug.log(debug.getProperty("curam.util.Refresh.dynamic.refresh.no_dynamic_items"));
}
},addConfig:function(_1339){
var _133a=false;
dojo.forEach(curam.util.Refresh.refreshConfig,function(_133b){
if(_133b.tab==_1339.tab){
_133b.config=_1339.config;
_133a=true;
}
});
if(!_133a){
curam.util.Refresh.refreshConfig.push(_1339);
}
},setupRefreshController:function(_133c){
curam.debug.log("curam.util.Refresh.setupRefreshController "+debug.getProperty("curam.util.ExpandableLists.load.for"),_133c);
var _133d=_1323.byId(_133c);
var tabId=_133d.tabDescriptor.tabID;
var _133e=dojo.filter(curam.util.Refresh.refreshConfig,function(item){
return item.tab==tabId;
});
if(_133e.length==1){
var _133f=_133e[0];
var ctl=new curam.util.ui.refresh.TabRefreshController(_133c,_133f);
curam.util.Refresh._controllers[_133c]=ctl;
ctl.setRefreshHandler(curam.util.Refresh.handleRefreshEvent);
}else{
if(_133e.length==0){
curam.debug.log(debug.getProperty("curam.util.Refresh.no.dynamic.refresh"),_133c);
var ctl=new curam.util.ui.refresh.TabRefreshController(_133c,null);
curam.util.Refresh._controllers[_133c]=ctl;
}else{
throw "curam.util.Refresh: multiple dynamic refresh "+"configurations found for tab "+_133c;
}
}
curam.tab.executeOnTabClose(function(){
curam.util.Refresh._controllers[_133c].destroy();
curam.util.Refresh._controllers[_133c]=undefined;
},_133c);
},getController:function(_1340){
var ctl=curam.util.Refresh._controllers[_1340];
if(!ctl){
throw "Refresh controller for tab '"+_1340+"' not found!";
}
return ctl;
},handleOnloadNestedInlinePage:function(_1341,_1342){
curam.debug.log("curam.util.Refresh.handleOnloadNestedInlinePage "+debug.getProperty("curam.util.Refresh.iframe",[_1341,_1342]));
var _1343=curam.util.getTopmostWindow();
var _1344=undefined;
var _1345=curam.tab.getSelectedTab();
if(_1345){
_1344=curam.tab.getTabWidgetId(_1345);
}
if(_1344){
curam.debug.log(debug.getProperty("curam.util.Refresh.parent"),_1344);
_1343.curam.util.Refresh.getController(_1344).pageLoaded(_1342.pageID,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_INLINE);
_1343.dojo.publish("/curam/main-content/page/loaded",[_1342.pageID,_1344,_1345]);
return true;
}
return false;
},handleRefreshEvent:function(_1346){
var _1347=function(_1348){
curam.util.ContextPanel.refresh(_1323.byId(_1348));
};
var _1349=function(_134a){
curam.tab.refreshMainContentPanel(_1323.byId(_134a));
};
var _134b=function(_134c,_134d,_134e){
curam.util.Refresh.refreshMenuAndNavigation(_134c,_134d,_134e);
};
curam.util.Refresh._doRefresh(_1346,_1347,_1349,_134b);
},_doRefresh:function(_134f,_1350,_1351,_1352){
var _1353=null;
var _1354=false;
var _1355=false;
var _1356=false;
var _1357=false;
var trc=curam.util.ui.refresh.TabRefreshController.prototype;
dojo.forEach(_134f,function(_1358){
var _1359=_1358.lastIndexOf("/");
var _135a=_1358.slice(0,_1359);
if(!_1353){
_1353=_1358.slice(_1359+1,_1358.length);
}
if(_135a==trc.EVENT_REFRESH_MENU){
_1354=true;
}
if(_135a==trc.EVENT_REFRESH_NAVIGATION){
_1355=true;
}
if(_135a==trc.EVENT_REFRESH_CONTEXT){
_1356=true;
}
if(_135a==trc.EVENT_REFRESH_MAIN){
_1357=true;
}
});
if(_1356){
_1350(_1353);
}
if(_1357){
_1351(_1353);
}
_1352(_1353,_1354,_1355);
},setupRefreshButton:function(_135b){
dojo.ready(function(){
var _135c=dojo.query("."+_135b)[0];
if(!_135c){
throw "Refresh button not found: "+_135b;
}
curam.util.Refresh._pageRefreshButton=_135c;
var href=window.location.href;
if(curam.util.isActionPage(href)){
var _135d=new _1328("Refresh");
var _135e=_135d.getProperty("refresh.button.disabled");
_1324.add(_135c,"disabled");
_1325.set(_135c,"title",_135e);
_1325.set(_135c,"aria-label",_135e);
_1325.set(_135c,"role","link");
_1325.remove(_135c,"href");
_1325.set(_135c,"aria-disabled","true");
curam.util.Refresh._pageRefreshButton._curamDisable=true;
if(_135c.firstChild!=null){
_1324.add(_135c.firstChild,"refresh-disabled");
_1325.set(_135c.firstChild,"alt",_135e);
}
}else{
_1324.add(_135c,"enabled");
curam.util.Refresh._pageRefreshButton["_curamDisable"]=undefined;
}
curam.util.getTopmostWindow().curam.util.setupPreferencesLink(href);
});
},refreshPage:function(event){
dojo.stopEvent(event);
var href=window.location.href;
var _135f=curam.util.Refresh._pageRefreshButton._curamDisable;
if(_135f){
return;
}
curam.util.FORCE_REFRESH=true;
curam.util.redirectWindow(href,true);
}});
layer.register("curam/util/Refresh",curam.util.Refresh);
return curam.util.Refresh;
});
},"dijit/form/_FormWidget":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/_base/kernel","dojo/ready","../_Widget","../_CssStateMixin","../_TemplatedMixin","./_FormWidgetMixin"],function(_1360,has,_1361,ready,_1362,_1363,_1364,_1365){
if(1){
ready(0,function(){
var _1366=["dijit/form/_FormValueWidget"];
require(_1366);
});
}
return _1360("dijit.form._FormWidget",[_1362,_1364,_1363,_1365],{setDisabled:function(_1367){
_1361.deprecated("setDisabled("+_1367+") is deprecated. Use set('disabled',"+_1367+") instead.","","2.0");
this.set("disabled",_1367);
},setValue:function(value){
_1361.deprecated("dijit.form._FormWidget:setValue("+value+") is deprecated.  Use set('value',"+value+") instead.","","2.0");
this.set("value",value);
},getValue:function(){
_1361.deprecated(this.declaredClass+"::getValue() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},postMixInProperties:function(){
this.nameAttrSetting=(this.name&&!has("msapp"))?("name=\""+this.name.replace(/"/g,"&quot;")+"\""):"";
this.inherited(arguments);
}});
});
},"curam/contentPanel":function(){
define(["curam/util","curam/tab","dojo/dom-attr","dojo/dom","curam/util/onLoad","curam/util/Refresh","curam/util/ui/refresh/RefreshEvent","curam/define","curam/debug","curam/ui/PageRequest","dijit/registry"],function(cu,ct,_1368,dom){
curam.define.singleton("curam.contentPanel",{smartPanelLoadedTokens:{},initSmartPanelExpListPageLoadListener:function(){
if(!cu.getTopmostWindow().dojo.body()._spListenerInitialized){
cu.getTopmostWindow().dojo.subscribe("expandedList.pageLoaded",curam.contentPanel.smartPanelExpListPageLoadListener);
cu.getTopmostWindow().dojo.body()._spListenerInitialized="true";
}
},smartPanelExpListPageLoadListener:function(data){
if(ct.getSmartPanelIframe()){
curam.contentPanel.checkSmartPanelLoaded(data,"ExpandedList.TabContentArea.Reloaded",curam.tab.getSelectedTabWidgetId());
}
},publishSmartPanelExpListPageLoad:function(_1369){
if(ct.getSmartPanelIframe()){
cu.getTopmostWindow().dojo.publish("expandedList.pageLoaded",[_1369.contentWindow.location.href]);
}
},setupOnLoad:function(_136a,_136b){
curam.debug.log("curam.contenPanel: setupOnLoad: "+_136a+" "+_136b);
curam.contentPanel.initSmartPanelExpListPageLoadListener();
var _136c=curam.contentPanel.iframeOnloadHandler;
curam.util.onLoad.addSubscriber(_136a,_136c);
if(curam.tab.getSmartPanelIframe(dijit.byId(_136b))){
curam.debug.log("tab has smart panel, setting up event listener");
curam.contentPanel.targetSmartPanel(_136a,_136b);
}
ct.executeOnTabClose(function(){
curam.util.onLoad.removeSubscriber(_136a,_136c);
curam.contentPanel._unregisterSmartPanelListener(_136b);
if(curam.util.getTopmostWindow().curam.util.lastOpenedTabButton&&curam.util.getTopmostWindow().curam.util.lastOpenedTabButton.domNode.id.includes(_136b,0)){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=null;
}
},_136b);
},iframeUpdateTitle:function(_136d,_136e,title){
var _136f=CONTENT_PANEL_TITLE+" - ";
var _1370=dom.byId(_136e);
if(_1370==null){
_1370=dojo.query("iframe."+_136e)[0];
}
ct.executeOnTabClose(function(){
var src=_1368.get(_1370,"src");
_1368.set(_1370,"src","");
curam.debug.log("curam.contentPanel: Released iframe content for "+src);
},_136d);
var _1371;
if(title){
var _1372=dojo.query("iframe.contentPanelFrame");
var _1373;
for(var i=0;i<_1372.length;i++){
if(_1372[i].title==_136f){
_1373=_1372[i];
break;
}
}
if(_1373){
_1371=title;
_1373.contentWindow.document.title=_1371;
}
}else{
_1371=_1370.contentWindow.document.title;
}
if(_1371==""||_1371=="undefined"){
var _1374=curam.util.iframeTitleFallBack();
_1370.contentWindow.document.title=_1374;
}
_1368.set(_1370,"title",_136f+curam.util.iframeTitleFallBack());
_1368.set(_1370,"data-done-loading",true);
},iframeOnloadHandler:function(_1375,_1376){
dojo.subscribe("tab.title.name.finished",function(data){
var _1377=ct.getContainerTab(dojo.query("iframe."+data.frameid)[0]);
var _1378=ct.getTabWidgetId(_1377);
curam.contentPanel.iframeUpdateTitle(_1378,data.frameid,data.title);
});
var _1379=ct.getContainerTab(dojo.query("iframe."+_1375)[0]);
var _137a=ct.getTabWidgetId(_1379);
curam.contentPanel.iframeUpdateTitle(_137a,_1375);
cu.Refresh.getController(_137a).pageLoaded(_1376.pageID,cu.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN);
dojo.publish("/curam/main-content/page/loaded",[_1376.pageID,_137a,_1379]);
},spOnLoadHandler:function(_137b,_137c){
var _137d=dojo.query("."+_137b)[0];
curam.contentPanel.checkSmartPanelLoaded(_137d.src,"TabContentArea.Reloaded",curam.tab.getSelectedTabWidgetId());
},checkSmartPanelLoaded:function(url,_137e,_137f){
if(!_137f){
throw new Error("Required argument 'tabWidgetId' was not specified.");
}
var _1380=ct.getSmartPanelIframe();
var _1381=_1368.get(_1380,"iframeLoaded");
if(_1381=="true"){
curam.contentPanel.smartPanelPublisher(_1380,url,_137e,_137f);
}else{
var token=dojo.subscribe("smartPanel.loaded",function(_1382){
if(_1382==_1380){
curam.contentPanel._unregisterSmartPanelListener(_137f);
curam.contentPanel.smartPanelPublisher(_1380,url,_137e,_137f);
}
});
curam.contentPanel._storeSmartPanelToken(token,_137f);
}
},_storeSmartPanelToken:function(token,_1383){
var _1384=curam.contentPanel.smartPanelLoadedTokens[_1383];
if(_1384){
dojo.unsubscribe(_1384);
}
curam.contentPanel.smartPanelLoadedTokens[_1383]=token;
},_unregisterSmartPanelListener:function(_1385){
dojo.unsubscribe(curam.contentPanel.smartPanelLoadedTokens[_1385]);
curam.contentPanel.smartPanelLoadedTokens[_1385]=null;
},smartPanelPublisher:function(_1386,url,_1387,_1388){
var _1389=new curam.ui.PageRequest(url);
curam.debug.log("Publishing event to smart panel in tab %s: eventType=%s;"+" pageID=%s; parameters=%s",_1388,_1387,_1389.pageID,dojo.toJson(_1389.parameters));
_1386.contentWindow.dojo.publish("contentPane.targetSmartPanel",[{"eventType":_1387,"pageId":_1389.pageID,"parameters":_1389.parameters}]);
},targetSmartPanel:function(_138a,_138b){
curam.debug.log("curam.contentPanel:targetSmartPanel(): "+_138a+" "+_138b);
var _138c=ct.getSmartPanelIframe();
var _138d=_138b;
if(_138c){
var spId=curam.util.onLoad.defaultGetIdFunction(_138c);
var _138e=dojo.subscribe("expandedList.toggle",function(_138f,_1390,_1391){
if(_138d===_1391){
curam.contentPanel.checkSmartPanelLoaded(_1390.url,_1390.eventType,_1391);
}
});
var _1392=curam.contentPanel.spOnLoadHandler;
curam.util.onLoad.addSubscriber(_138a,_1392);
ct.executeOnTabClose(function(){
dojo.unsubscribe(_138e);
curam.util.onLoad.removeSubscriber(_138a,_1392);
curam.util.onLoad.removeSubscriber(spId,curam.smartPanel._handleSmartPanelLoad);
},_138b);
}
}});
return curam.contentPanel;
});
},"dijit/MenuItem":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/kernel","dojo/sniff","dojo/_base/lang","./_Widget","./_TemplatedMixin","./_Contained","./_CssStateMixin","dojo/text!./templates/MenuItem.html"],function(_1393,dom,_1394,_1395,_1396,has,lang,_1397,_1398,_1399,_139a,_139b){
var _139c=_1393("dijit.MenuItem"+(has("dojo-bidi")?"_NoBidi":""),[_1397,_1398,_1399,_139a],{templateString:_139b,baseClass:"dijitMenuItem",label:"",_setLabelAttr:function(val){
this._set("label",val);
var _139d="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_139d=val.charAt(ndx+1);
var _139e=val.substr(0,ndx);
var _139f=val.substr(ndx+3);
text=_139e+_139d+_139f;
val=_139e+"<span class=\"dijitMenuItemShortcutKey\">"+_139d+"</span>"+_139f;
}else{
text=val;
}
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_139d);
},iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},accelKey:"",disabled:false,_fillContent:function(_13a0){
if(_13a0&&!("label" in this.params)){
this._set("label",_13a0.innerHTML);
}
},buildRendering:function(){
this.inherited(arguments);
var label=this.id+"_text";
_1394.set(this.containerNode,"id",label);
if(this.accelKeyNode){
_1394.set(this.accelKeyNode,"id",this.id+"_accel");
}
dom.setSelectable(this.domNode,false);
},onClick:function(){
},focus:function(){
try{
if(has("ie")==8){
this.containerNode.focus();
}
this.focusNode.focus();
}
catch(e){
}
},_setSelected:function(_13a1){
_1395.toggle(this.domNode,"dijitMenuItemSelected",_13a1);
},setLabel:function(_13a2){
_1396.deprecated("dijit.MenuItem.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_13a2);
},setDisabled:function(_13a3){
_1396.deprecated("dijit.Menu.setDisabled() is deprecated.  Use set('disabled', bool) instead.","","2.0");
this.set("disabled",_13a3);
},_setDisabledAttr:function(value){
this.focusNode.setAttribute("aria-disabled",value?"true":"false");
this._set("disabled",value);
},_setAccelKeyAttr:function(value){
if(this.accelKeyNode){
this.accelKeyNode.style.display=value?"":"none";
this.accelKeyNode.innerHTML=value;
_1394.set(this.containerNode,"colSpan",value?"1":"2");
}
this._set("accelKey",value);
}});
if(has("dojo-bidi")){
_139c=_1393("dijit.MenuItem",_139c,{_setLabelAttr:function(val){
this.inherited(arguments);
if(this.textDir==="auto"){
this.applyTextDir(this.textDirNode);
}
}});
}
return _139c;
});
},"dijit/_AttachMixin":function(){
define(["require","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/mouse","dojo/on","dojo/touch","./_WidgetBase"],function(_13a4,array,_13a5,_13a6,lang,mouse,on,touch,_13a7){
var _13a8=lang.delegate(touch,{"mouseenter":mouse.enter,"mouseleave":mouse.leave,"keypress":_13a5._keypress});
var _13a9;
var _13aa=_13a6("dijit._AttachMixin",null,{constructor:function(){
this._attachPoints=[];
this._attachEvents=[];
},buildRendering:function(){
this.inherited(arguments);
this._attachTemplateNodes(this.domNode);
this._beforeFillContent();
},_beforeFillContent:function(){
},_attachTemplateNodes:function(_13ab){
var node=_13ab;
while(true){
if(node.nodeType==1&&(this._processTemplateNode(node,function(n,p){
return n.getAttribute(p);
},this._attach)||this.searchContainerNode)&&node.firstChild){
node=node.firstChild;
}else{
if(node==_13ab){
return;
}
while(!node.nextSibling){
node=node.parentNode;
if(node==_13ab){
return;
}
}
node=node.nextSibling;
}
}
},_processTemplateNode:function(_13ac,_13ad,_13ae){
var ret=true;
var _13af=this.attachScope||this,_13b0=_13ad(_13ac,"dojoAttachPoint")||_13ad(_13ac,"data-dojo-attach-point");
if(_13b0){
var point,_13b1=_13b0.split(/\s*,\s*/);
while((point=_13b1.shift())){
if(lang.isArray(_13af[point])){
_13af[point].push(_13ac);
}else{
_13af[point]=_13ac;
}
ret=(point!="containerNode");
this._attachPoints.push(point);
}
}
var _13b2=_13ad(_13ac,"dojoAttachEvent")||_13ad(_13ac,"data-dojo-attach-event");
if(_13b2){
var event,_13b3=_13b2.split(/\s*,\s*/);
var trim=lang.trim;
while((event=_13b3.shift())){
if(event){
var _13b4=null;
if(event.indexOf(":")!=-1){
var _13b5=event.split(":");
event=trim(_13b5[0]);
_13b4=trim(_13b5[1]);
}else{
event=trim(event);
}
if(!_13b4){
_13b4=event;
}
this._attachEvents.push(_13ae(_13ac,event,lang.hitch(_13af,_13b4)));
}
}
}
return ret;
},_attach:function(node,type,func){
type=type.replace(/^on/,"").toLowerCase();
if(type=="dijitclick"){
type=_13a9||(_13a9=_13a4("./a11yclick"));
}else{
type=_13a8[type]||type;
}
return on(node,type,func);
},_detachTemplateNodes:function(){
var _13b6=this.attachScope||this;
array.forEach(this._attachPoints,function(point){
delete _13b6[point];
});
this._attachPoints=[];
array.forEach(this._attachEvents,function(_13b7){
_13b7.remove();
});
this._attachEvents=[];
},destroyRendering:function(){
this._detachTemplateNodes();
this.inherited(arguments);
}});
lang.extend(_13a7,{dojoAttachEvent:"",dojoAttachPoint:""});
return _13aa;
});
},"curam/ui/ClientDataAccessor":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/util/Request","curam/debug"],function(_13b8,layer,_13b9,debug){
return _13b8("curam.ui.ClientDataAccessor",null,{constructor:function(){
layer.register("curam/ui/ClientDataAccessor",this);
},get:function(path,_13ba,_13bb,_13bc){
var _13bd="servlet/PathResolver"+"?p="+path;
if(_13bb==undefined){
_13bb=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_13bc==undefined){
_13bc=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_13b9.post({url:_13bd,headers:{"Content-Encoding":"UTF-8"},handleAs:"text",preventCache:true,load:_13ba,error:_13bb,handle:_13bc});
},getList:function(path,_13be,_13bf,_13c0){
var _13c1="servlet/PathResolver"+"?r=l&p="+path;
if(_13bf==undefined){
_13bf=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_13c0==undefined){
_13c0=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_13b9.post({url:_13c1,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_13be,error:_13bf,handle:_13c0});
},getRaw:function(path,_13c2,_13c3,_13c4){
var _13c5="servlet/PathResolver"+"?r=j&p="+path;
if(_13c3==undefined){
_13c3=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_13c4==undefined){
_13c4=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_13b9.post({url:_13c5,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_13c2,error:_13c3,handle:_13c4});
},set:function(path,value,_13c6,_13c7,_13c8){
var _13c9="servlet/PathResolver"+"?r=x&p="+path+"&v="+encodeURIComponent(value);
var _13ca=curam.util.getTopmostWindow();
if(_13c7==undefined||_13c7==null){
_13c7=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_13c8==undefined||_13c8==null){
_13c8=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
if(_13c6==undefined||_13c6==null){
_13c6=dojo.hitch(this,this.handleClientDataAccessorSuccess);
}
_13b9.post({url:_13c9,headers:{"Content-Encoding":"UTF-8","csrfToken":_13ca.csrfToken},handleAs:"text",preventCache:true,load:_13c6,error:_13c7,handle:_13c8});
},handleClientDataAccessorError:function(error,_13cb){
var _13cc=debug.getProperty("curam.ui.ClientDataAccessor.err.1")+"PathResolverServlet : ";
var _13cd=debug.getProperty("curam.ui.ClientDataAccessor.err.2");
debug.log(_13cc+error+_13cd+_13cb);
},handleClientDataAccessorSuccess:function(_13ce,_13cf){
curam.debug.log("curam.ui.ClientDataAccessor.handleClientDataAccessorSuccess : "+_13ce);
},handleClientDataAccessorCallback:function(_13d0,_13d1){
debug.log("curam.ui.ClientDataAccessor.handleClientDataAccessorCallback :"+" "+debug.getProperty("curam.ui.ClientDataAccessor.callback"));
}});
});
},"curam/util/onLoad":function(){
define(["curam/util","curam/define","curam/debug","dojo/dom-attr"],function(util,_13d2,debug,attr){
_13d2.singleton("curam.util.onLoad",{EVENT:"/curam/frame/load",publishers:[],subscribers:[],defaultGetIdFunction:function(_13d3){
var _13d4=attr.get(_13d3,"class").split(" ");
return dojo.filter(_13d4,function(_13d5){
return _13d5.indexOf("iframe-")==0;
})[0];
},addPublisher:function(_13d6){
curam.util.onLoad.publishers.push(_13d6);
},addSubscriber:function(_13d7,_13d8,getId){
curam.util.onLoad.subscribers.push({"getId":getId?getId:curam.util.onLoad.defaultGetIdFunction,"callback":_13d8,"iframeId":_13d7});
},removeSubscriber:function(_13d9,_13da,getId){
curam.util.onLoad.subscribers=dojo.filter(curam.util.onLoad.subscribers,function(_13db){
return !(_13db.iframeId==_13d9&&_13db.callback==_13da);
});
},execute:function(){
if(window.parent==window){
curam.debug.log("curam.util.onLoad.execute(): "+debug.getProperty("curam.util.onLoad.exit"));
return;
}
var _13dc={};
dojo.forEach(curam.util.onLoad.publishers,function(_13dd){
_13dd(_13dc);
});
curam.util.onLoad.publishers=[];
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
window.parent.dojo.publish(curam.util.onLoad.EVENT,[window.frameElement,_13dc]);
}});
curam.util.subscribe(curam.util.onLoad.EVENT,function(_13de,_13df){
dojo.forEach(curam.util.onLoad.subscribers,function(_13e0){
var _13e1=_13e0.getId(_13de);
if(_13e0.iframeId==_13e1){
_13e0.callback(_13e1,_13df);
}
});
});
return curam.util.onLoad;
});
},"curam/widget/MenuItem":function(){
define(["dijit/MenuItem","dojo/_base/declare","dojo/text!dijit/templates/MenuItem.html"],function(_13e2,_13e3,_13e4){
var _13e5=_13e3("curam.widget.MenuItem",_13e2,{templateString:_13e4,onClickValue:"",_onClickAll:function(evt){
var _13e6=curam.tab.getTabContainer();
var _13e7=_13e6.getChildren();
for(var i=0;i<_13e7.length;i++){
if(_13e7[i].closable){
_13e6.closeChild(_13e7[i]);
}
}
},_setLabelAttr:function(val){
var _13e8=/<[a-zA-Z\/][^>]*>/g;
var _13e9=val.replace(_13e8,"");
this._set("label",_13e9);
var _13ea="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_13ea=val.charAt(ndx+1);
var _13eb=val.substr(0,ndx);
var _13ec=val.substr(ndx+3);
text=_13eb+_13ea+_13ec;
val=_13eb+"<span class=\"dijitMenuItemShortcutKey\">"+_13ea+"</span>"+_13ec;
}else{
text=val;
}
text=text.replace(_13e8,"");
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_13ea);
}});
return _13e5;
});
},"curam/cdsl/Struct":function(){
define(["dojo/_base/declare","dojo/_base/lang","./_base/_StructBase"],function(_13ed,lang,_13ee){
var _13ef=_13ed(_13ee,{constructor:function(data){
lang.mixin(this,this._data);
}});
return _13ef;
});
},"dijit/DropDownMenu":function(){
define(["dojo/_base/declare","dojo/keys","dojo/text!./templates/Menu.html","./_MenuBase"],function(_13f0,keys,_13f1,_13f2){
return _13f0("dijit.DropDownMenu",_13f2,{templateString:_13f1,baseClass:"dijitMenu",_onUpArrow:function(){
this.focusPrev();
},_onDownArrow:function(){
this.focusNext();
},_onRightArrow:function(evt){
this._moveToPopup(evt);
evt.stopPropagation();
evt.preventDefault();
},_onLeftArrow:function(evt){
if(this.parentMenu){
if(this.parentMenu._isMenuBar){
this.parentMenu.focusPrev();
}else{
this.onCancel(false);
}
}else{
evt.stopPropagation();
evt.preventDefault();
}
}});
});
},"cm/_base/_dom":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class"],function(dom,_13f3,_13f4){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(node,_13f5){
return cm._findSibling(node,_13f5,true);
},prevSibling:function(node,_13f6){
return cm._findSibling(node,_13f6,false);
},getInput:function(name,_13f7){
if(!dojo.isString(name)){
return name;
}
var _13f8=dojo.query("input[name='"+name+"'],select[name='"+name+"']");
return _13f7?(_13f8.length>0?_13f8:null):(_13f8.length>0?_13f8[0]:null);
},getParentByClass:function(node,_13f9){
node=node.parentNode;
while(node){
if(_13f4.contains(node,_13f9)){
return node;
}
node=node.parentNode;
}
return null;
},getParentByType:function(node,type){
node=node.parentNode;
type=type.toLowerCase();
var _13fa="html";
while(node){
if(node.tagName.toLowerCase()==_13fa){
break;
}
if(node.tagName.toLowerCase()==type){
return node;
}
node=node.parentNode;
}
return null;
},replaceClass:function(node,_13fb,_13fc){
_13f4.remove(node,_13fc);
_13f4.add(node,_13fb);
},setClass:function(node,_13fd){
node=dom.byId(node);
var cs=new String(_13fd);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_13fd);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
},_findSibling:function(node,_13fe,_13ff){
if(!node){
return null;
}
if(_13fe){
_13fe=_13fe.toLowerCase();
}
var param=_13ff?"nextSibling":"previousSibling";
do{
node=node[param];
}while(node&&node.nodeType!=1);
if(node&&_13fe&&_13fe!=node.tagName.toLowerCase()){
return cm[_13ff?"nextSibling":"prevSibling"](node,_13fe);
}
return node;
},getViewport:function(){
var d=dojo.doc,dd=d.documentElement,w=window,b=dojo.body();
if(dojo.isMozilla){
return {w:dd.clientWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&w.innerWidth){
return {w:w.innerWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&dd&&dd.clientWidth){
return {w:dd.clientWidth,h:dd.clientHeight};
}else{
if(b.clientWidth){
return {w:b.clientWidth,h:b.clientHeight};
}
}
}
}
return null;
},toggleDisplay:function(node){
_13f3.set(node,"display",_13f3.get(node,"display")=="none"?"":"none");
},endsWith:function(str,end,_1400){
if(_1400){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_13f3.set(n,"display","none");
},show:function(n){
_13f3.set(n,"display","");
}});
return cm;
});
},"dojo/store/Memory":function(){
define(["../_base/declare","./util/QueryResults","./util/SimpleQueryEngine"],function(_1401,_1402,_1403){
var base=null;
return _1401("dojo.store.Memory",base,{constructor:function(_1404){
for(var i in _1404){
this[i]=_1404[i];
}
this.setData(this.data||[]);
},data:null,idProperty:"id",index:null,queryEngine:_1403,get:function(id){
return this.data[this.index[id]];
},getIdentity:function(_1405){
return _1405[this.idProperty];
},put:function(_1406,_1407){
var data=this.data,index=this.index,_1408=this.idProperty;
var id=_1406[_1408]=(_1407&&"id" in _1407)?_1407.id:_1408 in _1406?_1406[_1408]:Math.random();
if(id in index){
if(_1407&&_1407.overwrite===false){
throw new Error("Object already exists");
}
data[index[id]]=_1406;
}else{
index[id]=data.push(_1406)-1;
}
return id;
},add:function(_1409,_140a){
(_140a=_140a||{}).overwrite=false;
return this.put(_1409,_140a);
},remove:function(id){
var index=this.index;
var data=this.data;
if(id in index){
data.splice(index[id],1);
this.setData(data);
return true;
}
},query:function(query,_140b){
return _1402(this.queryEngine(query,_140b)(this.data));
},setData:function(data){
if(data.items){
this.idProperty=data.identifier||this.idProperty;
data=this.data=data.items;
}else{
this.data=data;
}
this.index={};
for(var i=0,l=data.length;i<l;i++){
this.index[data[i][this.idProperty]]=i;
}
}});
});
},"dojo/hccss":function(){
define(["require","./_base/config","./dom-class","./dom-style","./has","./domReady","./_base/window"],function(_140c,_140d,_140e,_140f,has,_1410,win){
has.add("highcontrast",function(){
var div=win.doc.createElement("div");
div.style.cssText="border: 1px solid; border-color:red green; position: absolute; height: 5px; top: -999px;"+"background-image: url(\""+(_140d.blankGif||_140c.toUrl("./resources/blank.gif"))+"\");";
win.body().appendChild(div);
var cs=_140f.getComputedStyle(div),bkImg=cs.backgroundImage,hc=(cs.borderTopColor==cs.borderRightColor)||(bkImg&&(bkImg=="none"||bkImg=="url(invalid-url:)"));
if(has("ie")<=8){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
return hc;
});
_1410(function(){
if(has("highcontrast")){
_140e.add(win.body(),"dj_a11y");
}
});
return has;
});
},"curam/inspection/Layer":function(){
define(["curam/define"],function(def){
curam.define.singleton("curam.inspection.Layer",{register:function(_1411,inst){
require(["curam/util"]);
var tWin=curam.util.getTopmostWindow();
return tWin.inspectionManager?tWin.inspectionManager.observe(_1411,inst):null;
}});
var ref=curam.inspection.Layer;
require(["curam/util"]);
ref.tWin=curam.util.getTopmostWindow();
var _1412=ref.tWin.inspectionManager?ref.tWin.inspectionManager.getDirects():[];
if(_1412.length>0){
require(_1412);
}
return ref;
});
},"dijit/form/_ListBase":function(){
define(["dojo/_base/declare","dojo/on","dojo/window"],function(_1413,on,_1414){
return _1413("dijit.form._ListBase",null,{selected:null,_listConnect:function(_1415,_1416){
var self=this;
return self.own(on(self.containerNode,on.selector(function(_1417,_1418,_1419){
return _1417.parentNode==_1419;
},_1415),function(evt){
self[_1416](evt,this);
}));
},selectFirstNode:function(){
var first=this.containerNode.firstChild;
while(first&&first.style.display=="none"){
first=first.nextSibling;
}
this._setSelectedAttr(first,true);
},selectLastNode:function(){
var last=this.containerNode.lastChild;
while(last&&last.style.display=="none"){
last=last.previousSibling;
}
this._setSelectedAttr(last,true);
},selectNextNode:function(){
var _141a=this.selected;
if(!_141a){
this.selectFirstNode();
}else{
var next=_141a.nextSibling;
while(next&&next.style.display=="none"){
next=next.nextSibling;
}
if(!next){
this.selectFirstNode();
}else{
this._setSelectedAttr(next,true);
}
}
},selectPreviousNode:function(){
var _141b=this.selected;
if(!_141b){
this.selectLastNode();
}else{
var prev=_141b.previousSibling;
while(prev&&prev.style.display=="none"){
prev=prev.previousSibling;
}
if(!prev){
this.selectLastNode();
}else{
this._setSelectedAttr(prev,true);
}
}
},_setSelectedAttr:function(node,_141c){
if(this.selected!=node){
var _141d=this.selected;
if(_141d){
this.onDeselect(_141d);
}
if(node){
if(_141c){
_1414.scrollIntoView(node);
}
this.onSelect(node);
}
this._set("selected",node);
}else{
if(node){
this.onSelect(node);
}
}
}});
});
},"curam/util/DialogObject":function(){
define(["dojo/_base/declare","curam/dialog","curam/util"],function(_141e){
var _141f=_141e("curam.util.DialogObject",null,{_id:null,constructor:function(_1420,id){
if(!id){
var _1421=window.top.dojo.subscribe("/curam/dialog/uim/opened/"+_1420,this,function(_1422){
this._id=_1422;
window.top.dojo.unsubscribe(_1421);
});
}else{
this._id=id;
}
},registerBeforeCloseHandler:function(_1423){
var _1424=window.top.dojo.subscribe("/curam/dialog/BeforeClose",this,function(_1425){
if(_1425==this._id){
window.top.dojo.unsubscribe(_1424);
_1423();
}
});
},registerOnDisplayHandler:function(_1426){
if(curam.dialog._displayed==true){
_1426(curam.dialog._size);
}else{
var ut=window.top.dojo.subscribe("/curam/dialog/displayed",this,function(_1427,size){
if(_1427==this._id){
window.top.dojo.unsubscribe(ut);
_1426(size);
}
});
}
},close:function(_1428,_1429,_142a){
var win=curam.util.UimDialog._getDialogFrameWindow(this._id);
var _142b=win.curam.dialog.getParentWindow(win);
if(_1428&&!_1429){
win.curam.dialog.forceParentRefresh();
curam.dialog.doRedirect(_142b,null);
}else{
if(_1429){
var _142c=_1429;
if(_1429.indexOf("Page.do")==-1){
_142c=_1429+"Page.do"+curam.util.makeQueryString(_142a);
}
curam.dialog.doRedirect(_142b,_142c);
}
}
curam.dialog.closeModalDialog();
}});
return _141f;
});
},"curam/define":function(){
define(["dojo/_base/lang"],function(lang){
var _142d=this;
if(typeof (_142d.curam)=="undefined"){
_142d.curam={};
}
if(typeof (_142d.curam.define)=="undefined"){
lang.mixin(_142d.curam,{define:{}});
}
lang.mixin(_142d.curam.define,{singleton:function(_142e,_142f){
var parts=_142e.split(".");
var _1430=window;
for(var i=0;i<parts.length;i++){
var part=parts[i];
if(typeof _1430[part]=="undefined"){
_1430[part]={};
}
_1430=_1430[part];
}
if(_142f){
lang.mixin(_1430,_142f);
}
}});
return _142d.curam.define;
});
},"curam/util/ui/ApplicationTabbedUiController":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/debug","dojox/layout/ContentPane","curam/tab"],function(_1431,layer,debug){
var _1432=_1431("curam.util.ui.ApplicationTabbedUiController",null,{_tabContainer:null,constructor:function(_1433){
curam.util.extendXHR();
this._tabContainer=_1433;
layer.register("curam/util/ui/ApplicationTabbedUiController",this);
},findOpenTab:function(_1434){
var _1435=_1434.tabDescriptor;
var _1436=curam.tab.getTabContainer(_1435.sectionID);
var _1437=null;
var tabs=undefined;
var _1438=undefined;
if(_1436!=undefined){
tabs=_1436.getChildren();
_1438=_1436.selectedChildWidget;
}
if(_1438){
var selTD=_1438.tabDescriptor;
this._log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.testing"));
if(_1434.uimPageRequest.openInCurrentTab||(selTD.tabID==_1435.tabID&&selTD.matchesPageRequest(_1434.uimPageRequest))){
_1437=_1438;
}
}
if(!_1437&&tabs){
var _1439=true;
this._log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.searching")+" "+tabs.length+" "+debug.getProperty("curam.util.ui.ApplicationTabbedUiController.tabs"));
for(var i=0;i<tabs.length;i++){
var _143a=tabs[i];
var curTD=_143a.tabDescriptor;
if(curTD&&curTD.tabID==_1435.tabID){
if((_1439&&curTD.tabSignature==curTD.tabID)||curTD.matchesPageRequest(_1434.uimPageRequest)){
_1437=_143a;
break;
}
_1439=false;
}
}
}
this._log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.searched")+" '"+_1435.tabID+"'. "+debug.getProperty("curam.util.ui.ApplicationTabbedUiController.found")+" "+(_1437?debug.getProperty("curam.util.ui.ApplicationTabbedUiController.a"):debug.getProperty("curam.util.ui.ApplicationTabbedUiController.no"))+" "+debug.getProperty("curam.util.ui.ApplicationTabbedUiController.match"));
return _1437;
},openPageInCurrentTab:function(_143b){
var _143c=curam.tab.getSelectedTab();
var _143d=undefined;
if(_143c){
_143d=dojo.query(".nav-panel",_143c.domNode)[0];
}
if(_143d){
var _143e=dojo.query(".content-area-container",_143d)[0];
if(!_143e){
_143e=_143c.domNode;
}
dojo.publish("/curam/progress/display",[_143e]);
var _143f;
if(_143b.getURL().indexOf("?")==-1){
_143f="?";
}else{
_143f="&";
}
var loc=curam.config?curam.config.locale:jsL;
var _1440=dojo.global.jsBaseURL+"/"+loc+"/"+_143b.getURL()+_143f+curam.tab.getTabController().getCacheBusterParameter();
if(_143b.pageHolder){
_143b.pageHolder.location.href=_1440;
}else{
var _1441=dojo.query(".contentPanelFrame",_143d)[0];
_1441.src=_1440;
}
}
},_openInCurrentTab:function(_1442){
var _1443=curam.tab.getSelectedTab();
var _1444=undefined;
if(_1443){
_1444=dojo.query(".nav-panel",_1443.domNode)[0];
}
if(_1444){
var _1445=dojo.query(".contentPanelFrame",_1444)[0];
_1442.cdejParameters["o3ctx"]="4096";
var loc=curam.config?curam.config.locale:jsL;
var url=loc+"/"+_1442.getURL();
if(url.indexOf("?")==-1){
url+="?";
}else{
url+="&";
}
_1445.src=url+curam.tab.getTabController().getCacheBusterParameter();
}
},refreshExistingPageInTab:function(tab){
var _1446=curam.tab.getContentPanelIframe(tab);
_1446.contentWindow.location.reload(false);
},selectTab:function(tab){
this._tabContainer.selectChild(tab);
},createTab:function(_1447){
this._log("createTab(): "+debug.getProperty("curam.util.ui.ApplicationTabbedUiController.start"));
var _1448=_1447.tabDescriptor;
var _1449="";
if(_1448.tabContent&&_1448.tabContent.tabName){
_1449=_1448.tabContent.tabName;
}
var cp=new dojox.layout.ContentPane({tabDescriptor:_1448,uimPageRequest:_1447.uimPageRequest,title:_1449,closable:!_1448.isHomePage,preventCache:true,"class":"tab-content-holder dijitContentPane "+"dijitTabContainerTop-child "+"dijitTabContainerTop-dijitContentPane dijitTabPane",onDownloadStart:function(){
return "&nbsp;";
}});
var _144a=[];
_1447.uimPageRequest.cdejParameters["o3ctx"]="4096";
var _144b=dojo.connect(cp,"onDownloadEnd",null,function(){
curam.util.fireTabOpenedEvent(cp.id);
});
_144a.push(_144b);
_144b=dojo.subscribe("/curam/tab/closing",null,function(twid){
if(twid==cp.id){
curam.tab.doExecuteOnTabClose(cp.id);
}
});
_144a.push(_144b);
_144a.push(dojo.connect(cp,"destroy",function(){
dojo.forEach(_144a,dojo.disconnect);
}));
_144b=dojo.connect(cp,"set",function(name,value){
if(name=="title"&&arguments.length==2){
curam.debug.log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.title"));
var _144c=curam.util.getTopmostWindow().dojo;
_144c.global._tabTitle=value;
try{
_144c.publish("/curam/_tabTitle",[value]);
}
catch(err){
}
cp.tabDescriptor.setTabContent(_1447.uimPageRequest,value);
var _144d=curam.tab.getSelectedTab();
if(_144d){
var _144e=_144d.domNode.parentNode;
if(_144e){
_144e.focus();
}
}
}
});
_144a.push(_144b);
_144b=dojo.connect(cp,"onClose",function(){
new curam.tab.TabSessionManager().tabClosed(cp.tabDescriptor);
});
_144a.push(_144b);
var qs=_1447.uimPageRequest.getQueryString();
var href="TabContent.do"+"?"+curam.tab.getTabController().COMMAND_PARAM_NAME+"=PAGE&"+curam.tab.getTabController().PAGE_ID_PARAM_NAME+"="+_1447.uimPageRequest.pageID+(qs.length>0?"&"+qs:"")+"&o3tabid="+_1448.tabID+"&o3tabWidgetId="+cp.id;
this._log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.href")+" "+href);
cp.set("href",href);
this._log(debug.getProperty("curam.util.ui.ApplicationTabbedUiController.finished")+" ",cp.tabDescriptor);
return cp;
},insertTabIntoApp:function(_144f,_1450,_1451){
var _1452=null;
if(_1450){
if(this._tabContainer.hasChildren()){
_1452=this._tabContainer.selectedChildWidget;
}
this._tabContainer.addChild(_144f,0);
}else{
if(_1451){
var _1453=-1;
if(this._tabContainer.hasChildren()){
var _1454=this._tabContainer.selectedChildWidget;
var tabs=this._tabContainer.tablist.getChildren();
for(var i=0;i<tabs.length;i++){
var _1455=tabs[i];
if(_1455&&_1455._curamPageId==_1454.id){
_1453=i;
break;
}
}
}
if(_1453!=-1){
this._tabContainer.addChild(_144f,_1453+1);
}else{
this._tabContainer.addChild(_144f);
}
}else{
this._tabContainer.addChild(_144f);
}
}
return _1452;
},_log:function(msg,_1456){
if(curam.debug.enabled()){
curam.debug.log("curam.util.ui.ApplicationTabbedUiController: "+msg+(_1456?" "+dojo.toJson(_1456):""));
}
}});
return _1432;
});
},"dojo/dnd/move":function(){
define(["../_base/declare","../dom-geometry","../dom-style","./common","./Mover","./Moveable"],function(_1457,_1458,_1459,dnd,Mover,_145a){
var _145b=_1457("dojo.dnd.move.constrainedMoveable",_145a,{constraints:function(){
},within:false,constructor:function(node,_145c){
if(!_145c){
_145c={};
}
this.constraints=_145c.constraints;
this.within=_145c.within;
},onFirstMove:function(mover){
var c=this.constraintBox=this.constraints.call(this,mover);
c.r=c.l+c.w;
c.b=c.t+c.h;
if(this.within){
var mb=_1458.getMarginSize(mover.node);
c.r-=mb.w;
c.b-=mb.h;
}
},onMove:function(mover,_145d){
var c=this.constraintBox,s=mover.node.style;
this.onMoving(mover,_145d);
_145d.l=_145d.l<c.l?c.l:c.r<_145d.l?c.r:_145d.l;
_145d.t=_145d.t<c.t?c.t:c.b<_145d.t?c.b:_145d.t;
s.left=_145d.l+"px";
s.top=_145d.t+"px";
this.onMoved(mover,_145d);
}});
var _145e=_1457("dojo.dnd.move.boxConstrainedMoveable",_145b,{box:{},constructor:function(node,_145f){
var box=_145f&&_145f.box;
this.constraints=function(){
return box;
};
}});
var _1460=_1457("dojo.dnd.move.parentConstrainedMoveable",_145b,{area:"content",constructor:function(node,_1461){
var area=_1461&&_1461.area;
this.constraints=function(){
var n=this.node.parentNode,s=_1459.getComputedStyle(n),mb=_1458.getMarginBox(n,s);
if(area=="margin"){
return mb;
}
var t=_1458.getMarginExtents(n,s);
mb.l+=t.l,mb.t+=t.t,mb.w-=t.w,mb.h-=t.h;
if(area=="border"){
return mb;
}
t=_1458.getBorderExtents(n,s);
mb.l+=t.l,mb.t+=t.t,mb.w-=t.w,mb.h-=t.h;
if(area=="padding"){
return mb;
}
t=_1458.getPadExtents(n,s);
mb.l+=t.l,mb.t+=t.t,mb.w-=t.w,mb.h-=t.h;
return mb;
};
}});
return {constrainedMoveable:_145b,boxConstrainedMoveable:_145e,parentConstrainedMoveable:_1460};
});
},"curam/ui/UIController":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/json","dojo/dom-style","dojo/dom-attr","curam/inspection/Layer","curam/util/Request","curam/define","curam/debug","curam/util/RuntimeContext","curam/tab/TabDescriptor","curam/util/ui/ApplicationTabbedUiController"],function(_1462,lang,_1463,_1464,_1465,layer,_1466,_1467,debug){
curam.define.singleton("curam.ui.UIController",{TAB_TOPIC:"/app/tab",ROOT_OBJ:"curam.ui.UIController",PAGE_ASSOCIATIONS:{},RESOLVE_PAGES:{},PAGE_ID_PARAM_NAME:"o3pid",COMMAND_PARAM_NAME:"o3c",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",DUPLICATE_TAB_MAPPING_ERROR:"dupTabError",UNASSOCIATED_SHORTCUT_ERROR:"looseShortcutError",LOAD_MASK_TIMEOUT:15000,TABS_INFO_MODAL_TITLE_PROP_NAME:"title.info",TABS_ERROR_MODAL_TITLE_PROP_NAME:"title.error",TABS_INFO_MODAL_MSG_PROP_NAME:"message.max.tabs.info",TABS_ERROR_MODAL_MSG_PROP_NAME:"message.max.tabs.error",TABS_MSG_PLACEHOLDER_MAX_TABS:15,TABS_SEQUENTIAL_ORDER:false,MAX_NUM_TABS:15,MAX_TABS_MODAL_SIZE:"width=470,height=80",TAB_INSTANTIATOR:null,initialize:function(_1468){
curam.ui.UIController._log("curam.ui.UIController.initialize()");
curam.ui.UIController._log("dojo.isQuirks: "+dojo.isQuirks);
window.rootObject=curam.ui.UIController.ROOT_OBJ;
curam.util.subscribe(curam.ui.UIController.TAB_TOPIC,curam.ui.UIController.tabTopicHandler);
curam.util.subscribe("tab.title.name.set",curam.ui.UIController.setTabTitleAndName);
if(_1468){
new curam.tab.TabSessionManager().init(_1468);
}else{
new curam.tab.TabSessionManager().init();
}
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.event"));
},ajaxPostFailure:function(err){
curam.ui.UIController._log("========= "+debug.getProperty("curam.ui.UIController.test")+" JSON "+debug.getProperty("curam.ui.UIController.servlet.failure")+" =========");
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.error")+" "+err);
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.args")+" "+ioargs);
curam.ui.UIController._log("============================================");
},instantiateTab:function(_1469,_146a,_146b){
var _146c=_146b?2:1;
dojo.publish("curam/tab/quantityExpectedFrames/load",[_146a,_146c]);
var _146d;
if(curam.ui.UIController.TAB_INSTANTIATOR!=null){
_146d=curam.ui.UIController.TAB_INSTANTIATOR;
}else{
_146d=dijit.byId(_146a);
}
if(_146d){
curam.util.getTopmostWindow().dojo.publish("/curam/application/tab/requested",[_146a]);
var td=_146d.tabDescriptor;
var _146e="'"+td.tabID+"/"+_146a+"'";
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.instantiating")+" "+_146e+" "+debug.getProperty("curam.ui.UIController.with.signature"));
td.setTabSignature(_1469,_146d.uimPageRequest);
var _146f=function(){
var _1470=dojo.query("#"+_146a+" .tab-wrapper .tab-load-mask")[0];
if(_1470&&_1464.get(_1470,"display")!="none"){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.revealing")+" "+_146e+" "+debug.getProperty("curam.ui.UIController.now"));
_1464.set(_1470,"display","none");
curam.util.getTopmostWindow().dojo.publish("/curam/application/tab/revealed",[_146a]);
curam.debug.log("curam.ui.UIController.revealTabNow function calling curam.util.setBrowserTabTitle");
curam.util.setBrowserTabTitle();
}
};
if(!_146b){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.no.details"));
_146f();
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.has.details")+_146e+debug.getProperty("curam.ui.UIController.listeners"));
dojo.global.tabLoadMaskTimeout=setTimeout(_146f,curam.ui.UIController.LOAD_MASK_TIMEOUT);
var _1471=false;
var _1472=function(){
if(_1471){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.panels.loaded"));
_146f();
clearTimeout(dojo.global.tabLoadMaskTimeout);
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.panels.not.loaded")+" "+_146e+" "+debug.getProperty("curam.ui.UIController.later"));
_1471=true;
}
};
var _1473=dojo.connect(_146d,"onDownloadEnd",function(){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.content.pane.loaded")+" "+debug.getProperty("curam.ui.UIController.reveal")+" "+_146e+" "+debug.getProperty("curam.ui.UIController.now"));
_1472();
dojo.disconnect(_1473);
});
var _1474=curam.util.getTopmostWindow().dojo.subscribe("/curam/frame/detailsPanelLoaded",function(_1475,_1476){
if(_146a==_1476){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.details.panel.loaded")+" "+_146e+" "+ +debug.getProperty("curam.ui.UIController.now"));
_1472();
dojo.unsubscribe(_1474);
}
});
}
var _1477=curam.tab.getHandlerForTab(function(_1478,_1479){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.content.pane.changed")+" "+_146e+" "+debug.getProperty("curam.ui.UIController.now"));
curam.ui.UIController._contentPanelUpdated(_146d);
},_146a);
var _147a=curam.util.getTopmostWindow().dojo.subscribe("/curam/main-content/page/loaded",null,_1477);
curam.tab.unsubscribeOnTabClose(_147a,_146a);
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.tab.not.found")+" '"+_146a+"'.");
}
},_contentPanelUpdated:function(tab){
var _147b=curam.tab.getContentPanelIframe(tab);
tab.tabDescriptor.setTabContent(new curam.ui.PageRequest(_147b.src),null);
},getCacheBusterParameter:function(){
return curam.ui.UIController.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+curam.ui.UIController.CACHE_BUSTER++;
},_getTabbedUiApi:function(_147c){
var _147d=curam.ui.UIController._selectSection(_147c);
return new curam.util.ui.ApplicationTabbedUiController(_147d);
},_selectSection:function(_147e){
var _147f=_147e?!_147e.openInBackground:true;
var _1480=dijit.byId(curam.tab.SECTION_TAB_CONTAINER_ID);
var _1481=_147e?_147e.tabDescriptor.sectionID:curam.tab.getCurrentSectionId();
var _1482=dijit.byId(_1481+"-sbc");
var _1483=curam.tab.getTabContainer(_1481);
if(_147f){
if(_1482){
_1480.selectChild(_1482);
}else{
_1480.selectChild(_1483);
}
}
return _1483;
},tabTopicHandler:function(_1484){
var api=curam.ui.UIController._getTabbedUiApi(_1484);
curam.ui.UIController._doHandleTabEvent(_1484,api);
},_doHandleTabEvent:function(_1485,_1486){
var _1487=_1485.tabDescriptor;
var _1488=_1487.sectionID;
var _1489=curam.tab.getTabContainer(_1488);
_1485.tabDescriptor.openInBackground=_1485.openInBackground;
var _148a=curam.util.getTopmostWindow().dojo;
var isNew=false;
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.fired")+" "+_1488+" : "+_1487.tabID+" : "+_1485.uimPageRequest.pageID);
var tab=_1486.findOpenTab(_1485);
if(tab===null&&lang.exists("selectedChildWidget.tabDescriptor.isHomePage",_1489)&&_1489.selectedChildWidget.tabDescriptor.isHomePage===true&&_1489.selectedChildWidget.tabDescriptor.tabID===_1485.tabDescriptor.tabID){
tab=_1489.selectedChildWidget;
}
if(!tab){
if(_1489==undefined){
return false;
}
var _148b=_1489.getChildren().length+1;
var _148c=this.MAX_NUM_TABS;
var _148d=this._checkMaxNumOpenTabsExceeded(_148c,_148b);
if(_148d){
return true;
}
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.creating"));
tab=_1486.createTab(_1485);
dojo.publish("/curam/progress/display",[tab.domNode]);
tab.connect(tab,"onLoad",function(){
var _148e=curam.tab.getContentPanelIframe(tab);
_1465.set(_148e,"src",_1465.get(_148e,"data-content-url"));
_148a.publish("/curam/application/tab/ready",[tab]);
});
isNew=true;
}
if(isNew){
var _148f=_1486.insertTabIntoApp(tab,_1485.uimPageRequest.isHomePage,this.TABS_SEQUENTIAL_ORDER);
if(!_1485.openInBackground){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.new.fore.tab"),tab.tabDescriptor);
_1486.selectTab(tab);
if(tab.controlButton!="undefined"){
curam.util.setLastOpenedTabButton(dijit.byId(tab.controlButton.id));
}
if(_148f!=null){
_1486.selectTab(_148f);
if(_148f.controlButton!="undefined"){
curam.util.setLastOpenedTabButton(dijit.byId(_148f.controlButton.id));
}
}
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.new.back.tab"),tab.tabDescriptor);
}
this._checkMaxNumOpenTabsReached(_148c,_148b);
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.show.page"),tab.tabDescriptor);
_1486.selectTab(tab);
if(_1485.uimPageRequest.justRefresh){
_1486.refreshExistingPageInTab(tab);
}else{
if(_1485.uimPageRequest.forceLoad){
_1486.openPageInCurrentTab(_1485.uimPageRequest);
}else{
var _1490=tab.tabDescriptor;
var _1491=_1490.tabID==_1485.tabDescriptor.tabID&&_1490.matchesPageRequest(_1485.uimPageRequest);
var _1492=_1490.tabContent.pageID==_1485.uimPageRequest.pageID;
if(_1491&&!_1492){
_1486.openPageInCurrentTab(_1485.uimPageRequest);
}
_1492&&_1491&&curam.util.getTopmostWindow().dojo.publish("curam/tab/contextRefresh",[true]);
}
}
}
return true;
},_checkMaxNumOpenTabsReached:function(_1493,_1494){
if(_1494==_1493){
this.TABS_MSG_PLACEHOLDER_MAX_TABS=_1493;
curam.util.openGenericErrorModalDialog(this.MAX_TABS_MODAL_SIZE,this.TABS_INFO_MODAL_TITLE_PROP_NAME,this.TABS_INFO_MODAL_MSG_PROP_NAME,this.TABS_MSG_PLACEHOLDER_MAX_TABS,false);
return true;
}
},_checkMaxNumOpenTabsExceeded:function(_1495,_1496){
if(_1496>_1495){
this.TABS_MSG_PLACEHOLDER_MAX_TABS=_1495;
curam.util.openGenericErrorModalDialog(this.MAX_TABS_MODAL_SIZE,this.TABS_ERROR_MODAL_TITLE_PROP_NAME,this.TABS_ERROR_MODAL_MSG_PROP_NAME,this.TABS_MSG_PLACEHOLDER_MAX_TABS,true);
return true;
}
},checkPage:function(_1497,_1498){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.checking.page")+" '"+_1497.pageID+"'.");
var _1499=null;
var _149a=null;
var _149b=null;
var _149c=null;
var _149d=_1498;
if(_1498&&typeof _1498!="function"){
_149d=_1498.unmappedPageLoader;
_1499=_1498.preferredTabs;
_149a=_1498.moreThanOneTabMappedCallback;
_149b=_1498.shouldLoadPage;
_149c=_1498.successCallback;
}
if(typeof _149b==="undefined"||_149b===null){
_149b=true;
}
if(_1497.pageID==""){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.ignoring")+" "+_1497.getURL());
return;
}
var _149e=curam.ui.UIController._ensurePageAssociationInitialized(_1497,function(){
if(curam.ui.UIController.isPageAssociationInitialized(_1497.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS)){
curam.ui.UIController.checkPage(_1497,_1498);
}else{
var msg=debug.getProperty("curam.ui.UIController.failed");
curam.ui.UIController._log(msg);
throw new Error(msg);
}
});
if(_149e){
try{
var _149f=curam.ui.UIController.getTabDescriptorForPage(_1497.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS,_1499);
if(_149f!=null){
if(_149b){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.page.opened")+" '"+_1497.pageID+"'. "+debug.getProperty("curam.ui.UIController.sec.id")+" '"+_149f.sectionID+"'. "+debug.getProperty("curam.ui.UIController.tab.id")+" '"+_149f.tabID+"'.");
if(_1497.isHomePage){
_149f.isHomePage=true;
}
_149f.setTabContent(_1497);
dojo.publish(curam.ui.UIController.TAB_TOPIC,[new curam.ui.OpenTabEvent(_149f,_1497)]);
}
if(_149c){
_149c();
}
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.page.id")+" '"+_1497.pageID+"'.");
if(!_149d||typeof _149d!="function"){
if(typeof curam.tab.getSelectedTab()=="undefined"){
throw {name:curam.ui.UIController.UNASSOCIATED_SHORTCUT_ERROR,message:"ERROR:The requested page "+_1497.pageID+" is not associated with any tab and there is no "+"tab to open it!"};
}
if(_149b){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.load"));
curam.ui.UIController._getTabbedUiApi().openPageInCurrentTab(_1497);
}
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.unmapped"));
_149d(_1497);
}
}
}
catch(e){
if(e.name==curam.ui.UIController.DUPLICATE_TAB_MAPPING_ERROR){
if(!_149a){
alert(e.message);
curam.ui.UIController._getTabbedUiApi().openPageInCurrentTab(_1497);
}else{
_149a(_1497);
}
return null;
}else{
if(e.name==curam.ui.UIController.UNASSOCIATED_SHORTCUT_ERROR){
alert(e.message);
console.error(e.message);
return null;
}else{
throw e;
}
}
}
}
},isPageAssociationInitialized:function(_14a0,_14a1){
var _14a2=_14a1[_14a0];
return !(typeof _14a2=="undefined");
},_ensurePageAssociationInitialized:function(_14a3,_14a4){
if(!curam.ui.UIController.isPageAssociationInitialized(_14a3.pageID,curam.ui.UIController.PAGE_ASSOCIATIONS)){
var path="/config/tablayout/associated["+_14a3.pageID+"]["+USER_APPLICATION_ID+"]";
new curam.ui.ClientDataAccessor().getRaw(path,function(data){
curam.ui.UIController.initializePageAssociations(_14a3,data);
_14a4();
},function(error,args){
var msg=curam_ui_UIController_data_error+" "+error;
curam.ui.UIController._log(msg);
if(!_1466.checkLoginPage(args.xhr)){
alert(msg);
}
var _14a5=curam.util.getTopmostWindow();
_14a5.dojo.publish("/curam/progress/unload");
_14a5.location.reload(false);
},null);
return false;
}
return true;
},initializePageAssociations:function(_14a6,_14a7){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.got.assoc")+" '"+_14a6.pageID+"'.");
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.assoc"),_14a7);
if(_14a7){
if(_14a7.tabIDs&&_14a7.tabIDs.length>0){
curam.ui.UIController.PAGE_ASSOCIATIONS[_14a6.pageID]=_14a7;
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.no.mappings")+" '"+_14a6.pageID+"'.");
curam.ui.UIController.PAGE_ASSOCIATIONS[_14a6.pageID]=null;
}
}else{
throw "initializePageAssociations did not recieve a valid response.";
}
},getTabDescriptorForPage:function(_14a8,_14a9,_14aa){
var _14ab=_14a9[_14a8];
if(!curam.ui.UIController.isPageAssociationInitialized(_14a8,_14a9)){
throw "Page associations have not been initialized for: "+_14a8;
}
if(_14ab!=null){
var tabID=curam.ui.UIController.getTabFromMappings(_14ab.tabIDs,curam.tab.getSelectedTab(),_14aa);
return new curam.tab.TabDescriptor(_14ab.sectionID,tabID);
}else{
return null;
}
},getTabFromMappings:function(_14ac,_14ad,_14ae){
if(!_14ad){
if(_14ac.length==1){
return _14ac[0];
}else{
if(_14ac.length>1){
if(_14ae&&_14ae.length>0){
for(var i=0;i<_14ae.length;i++){
if(_14ac.indexOf(_14ae[i])>=0){
return _14ae[i];
}
}
}
throw "Home page mapped to multiple tabs";
}
}
}
if(_14ac.length>1&&_14ae&&_14ae.length>0){
for(var i=0;i<_14ae.length;i++){
if(_14ac.indexOf(_14ae[i])>=0){
return _14ae[i];
}
}
}
var _14af=_14ad.tabDescriptor.tabID;
for(var i=0;i<_14ac.length;i++){
if(_14af==_14ac[i]){
return _14af;
}
}
if(_14ac.length==1){
return _14ac[0];
}else{
if(_14ac.length>1){
throw {name:curam.ui.UIController.DUPLICATE_TAB_MAPPING_ERROR,message:"ERROR: The page that you are trying to link to is associated with "+"multiple tabs: ["+_14ac.toString()+"]. Therefore the "+"tab to open cannot be determined and the page will open in the "+"current tab. Please report this error.",tabID:_14af};
}else{
}
}
},handleUIMPageID:function(_14b0,_14b1){
var _14b2=_14b1?true:false;
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.handling.uim")+" '"+_14b0+"'. Page is "+(_14b2?"":"not ")+debug.getProperty("curam.ui.UIController.default.sec"));
curam.ui.UIController.handlePageRequest(new curam.ui.PageRequest(_14b0+"Page.do",_14b2));
},processURL:function(url){
var _14b3=new curam.ui.PageRequest(url);
curam.ui.UIController.handlePageRequest(_14b3);
},handlePageRequest:function(_14b4,_14b5){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.handling.page")+" '"+_14b4.pageID+"'. "+debug.getProperty("curam.ui.UIController.panel.will")+(_14b4.forceRefresh?"":debug.getProperty("curam.ui.UIController.not"))+debug.getProperty("curam.ui.UIController.reload"));
var _14b6=curam.ui.UIController.checkResolvePage(_14b4,_14b4.forceRefresh,null,_14b5);
if(_14b6==true){
curam.ui.UIController.checkPage(_14b4,{preferredTabs:_14b5});
}
},checkResolvePage:function(_14b7,_14b8,_14b9,_14ba,_14bb,_14bc,_14bd){
var _14be={unmappedPageLoader:_14b9,moreThanOneTabMappedCallback:_14bb,shouldLoadPage:_14bc,successCallback:_14bd,preferredTabs:_14ba};
if(_14b8){
return true;
}
var _14bf=curam.ui.UIController.RESOLVE_PAGES[_14b7.pageID];
if(_14bf==false){
return true;
}else{
var _14c0;
if(_14b7.getURL().indexOf("?")==-1){
_14c0="?";
}else{
_14c0="&";
}
var loc=curam.config?curam.config.locale+"/":"";
_1466.post({url:loc+_14b7.getURL()+_14c0+"o3resolve=true",handleAs:"text",preventCache:true,load:dojo.hitch(curam.ui.UIController,"resolvePageCheckSuccess",_14b7,_14be),error:dojo.hitch(curam.ui.UIController,"resolvePageCheckFailure",_14b7,_14be)});
return false;
}
},resolvePageCheckSuccess:function(_14c1,_14c2,_14c3,_14c4){
var _14c5=false;
var _14c6;
var _14c7;
var _14c8;
if(_14c3.substring(2,0)=="{\""&&_14c3.charAt(_14c3.length-1)=="}"){
_14c5=true;
_14c3=_1463.parse(_14c3,true);
_14c6=_14c3.pageID;
_14c7=_14c3.pageURL;
}else{
_14c5=false;
}
if(_14c5&&_14c1.pageID!=_14c6){
curam.ui.UIController.RESOLVE_PAGES[_14c1.pageID]=true;
_14c7=_14c7.replace("&amp;o3resolve=true","");
_14c7=_14c7.replace("&o3resolve=true","");
_14c7=_14c7.replace("o3resolve=true","");
for(paramName in _14c1.cdejParameters){
if(paramName.length>0&&paramName.indexOf("__o3")!=-1){
if(_14c7.indexOf("?")==-1){
_14c7+="?"+paramName+"="+encodeURIComponent(_14c1.cdejParameters[paramName]);
}else{
_14c7+="&"+paramName+"="+encodeURIComponent(_14c1.cdejParameters[paramName]);
}
}
}
_14c8=new curam.ui.PageRequest(_14c7);
if(_14c1.forceLoad){
_14c8.forceLoad=_14c1.forceLoad;
}
}else{
curam.ui.UIController.RESOLVE_PAGES[_14c1.pageID]=false;
_14c8=_14c1;
}
curam.ui.UIController.checkPage(_14c8,_14c2);
},resolvePageCheckFailure:function(_14c9,_14ca,error,_14cb){
curam.ui.UIController.RESOLVE_PAGES[_14c9.pageID]=false;
curam.ui.UIController.checkPage(_14c9,_14ca);
},setTabTitleAndName:function(_14cc,_14cd,_14ce){
var tab=curam.tab.getContainerTab(_14cc);
if(tab){
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.changing.tab")+" '"+_14cd+"', '"+_14ce+"'. "+debug.getProperty("curam.ui.UIController.descriptor.before"),tab.tabDescriptor);
dojo.query("h1.detailsTitleText",tab.domNode)[0].innerHTML=_14cd;
var _14cf=dojo.query("h1.detailsTitleText",tab.domNode)[0];
_14cf.setAttribute("title",_14cd);
tab.set("title",_14ce);
var _14d0;
for(var i=0;i<_14cc.classList.length;i++){
if(_14cc.classList[i].indexOf("iframe-")==0){
_14d0=_14cc.classList[i];
break;
}
}
dojo.publish("tab.title.name.finished",[{"title":_14ce,"frameid":_14d0}]);
}else{
curam.ui.UIController._log(debug.getProperty("curam.ui.UIController.cannot.change")+" '"+_14cd+"', '"+_14ce+"'. "+debug.getProperty("curam.ui.UIController.iframe")+" '"+_14cc.id+"'.");
}
},handleLinkClick:function(_14d1,_14d2){
curam.ui.UIController._doHandleLinkClick(_14d1,_14d2,curam.tab.getContentPanelIframe(),curam.ui.UIController.handlePageRequest,curam.util.openModalDialog);
},_doHandleLinkClick:function(_14d3,_14d4,_14d5,_14d6,_14d7){
var _14d8=_14d3;
if(_14d5){
var rtc=new curam.util.RuntimeContext(_14d5.contentWindow);
var _14d9=null;
if(_14d4){
_14d9=[{key:"o3frame",value:"modal"}];
}
_14d8=curam.util.setRpu(_14d3,rtc,_14d9);
}
if(_14d4&&curam.config&&curam.config.modalsEnabled!="false"){
var _14da=_14d4.openDialogFunction||_14d7;
var fArgs=_14d4.args||[{href:_14d8},_14d4.dialogOptions];
_14da.apply(this,fArgs);
}else{
var _14db=new curam.ui.PageRequest(_14d8);
_14d6(_14db);
}
},handleDownLoadClickLegacy:function(_14dc){
require(["dojo/io/iframe"],function(_14dd){
var _14de=_14dd.create("o3lrm_frame","");
_14de.src=location.href.substring(0,location.href.lastIndexOf("/"))+decodeURIComponent(_14dc.replace(/\+/g," "));
return;
});
},handleDownLoadClick:function(_14df){
var _14e0=curam.tab.getContentPanelIframe();
_14e0.src=location.href.substring(0,location.href.lastIndexOf("/"))+decodeURIComponent(_14df.replace(/\+/g," "))+"&"+jsScreenContext.toRequestString();
return;
},_log:function(msg,_14e1){
if(curam.debug.enabled()){
curam.debug.log("UI CONTROLLER: "+msg+(_14e1?" "+dojo.toJson(_14e1):""));
}
},_prepareWordLanding:function(){
var as1,as2;
require(["dojo/aspect","curam/tab"],function(asp){
as1=asp.before(curam.ui.UIController,"_doHandleTabEvent",function(ote,_14e2){
var _14e3=curam.tab.getTabContainer(ote.tabDescriptor.sectionID);
as2=asp.after(_14e2,"findOpenTab",function(_14e4){
if(!_14e4){
ote.uimPageRequest.forceLoad=true;
tabs=_14e3.getChildren();
for(var i=0;i<tabs.length;i++){
var _14e5=tabs[i];
var curTD=_14e5.tabDescriptor;
if(curTD&&curTD.tabID==ote.tabDescriptor.tabID){
_14e4=_14e5;
}
}
}
as2.remove();
return _14e4;
});
as1.remove();
});
});
dojo.subscribe("curam/fileedit/aspect/release",function(){
if(as2!=null){
as2.remove();
}
as1.remove();
});
return as1;
}});
layer.register("curam/ui/UIController",curam.ui.UIController);
return curam.ui.UIController;
});
},"curam/FastUIMController":function(){
define(["dojo/_base/declare","dojo/parser","dijit/registry","curam/inspection/Layer","curam/UIMController","curam/debug","dojo/dom-class","dojo/dom-style","dojo/dom-attr","curam/util/onLoad"],function(_14e6,_14e7,_14e8,layer,_14e9,debug,_14ea,_14eb,_14ec){
var _14ed=_14e6("curam.FastUIMController",[curam.UIMController],{buildRendering:function(){
this.domNode=this.srcNodeRef;
this._attachTemplateNodes(this.domNode,function(node,prop){
return node.getAttribute(prop);
});
},postCreate:function(){
layer.register("curam/FastUIMController",this);
},startup:function(){
this.tabController=_14e8.byId(this.tabControllerId);
_14ec.set(this.frame,"iscpiframe",this.iscpiframe);
_14ec.set(this.frame,"title",this.title);
_14ea.add(this.frame,this.iframeClassList);
_14ea.add(this.domNode,this.classList);
this.frameLoadEvent=this.EVENT.TOPIC_PREFIX+this.frame.id;
this.setURL(this.url);
if(this._iframeLoaded()){
debug.log("curam.FastUIMController "+debug.getProperty("curam.FastUIMControlle.msg"));
}else{
var _14ee=dojo.hitch(this,"processFrameLoadEvent");
curam.util.onLoad.addSubscriber(this.frame.id,_14ee);
dojo.connect(this,"destroy",function(){
curam.util.onLoad.removeSubscriber(this.iframeId,_14ee);
_14ee=null;
});
}
if(this.inDialog){
_14eb.set(this.frame,{width:this.width,height:this.height});
}
},_iframeLoaded:function(){
return _14ec.get(this.frame,"data-done-loading")=="true";
}});
return _14ed;
});
},"curam/cdsl/connection/SimpleAccess":function(){
define(["curam/cdsl/_base/_Connection","curam/cdsl/store/CuramStore","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","dojo/_base/lang","dojo/store/Observable","dojo/store/Cache","dojo/store/Memory"],function(_14ef,_14f0,_14f1,_14f2,_14f3,lang,_14f4,Cache,_14f5){
var _14f6=null;
return {initConnection:function(_14f7){
if(_14f7==null){
throw new Error("The connection object should be provided.");
}else{
if(!(_14f7 instanceof _14ef)){
throw new Error("The wrong type of the connection object is provided.");
}
}
if(_14f6==null){
_14f6=_14f7;
}
return _14f6;
},buildStore:function(_14f8,_14f9,_14fa,cache){
if(_14f6==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_14f8==null){
throw new Error("Facade class name is missing.");
}
if(cache==null){
cache=false;
}
if(_14fa==null){
_14fa=false;
}
var store=new _14f0(_14f6,_14f8,_14f9);
if(_14fa){
store=new _14f4(store);
}
if(cache){
var _14fb=new _14f5();
store=new Cache(store,_14fb);
}
return store;
},makeRequest:function(_14fc,_14fd,_14fe){
if(_14f6==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_14fc==null){
throw new Error("Facade class name is missing.");
}
if(_14fd==null){
throw new Error("Facade method name is missing.");
}
var _14ff=new _14f1(_14f6);
var _1500;
if(_14fe==null){
_1500=[];
}else{
_1500=[new _14f3(_14fe)];
}
var _1501=new _14f2(_14fc,_14fd,_1500);
return _14ff.call([_1501]);
}};
});
},"cm/_base/_pageBehaviors":function(){
define(["cm/_base/_behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
cm.registerBehavior("FORM_SINGLE_SUBMIT",{"form":{"onsubmit":function(evt){
if(cm.wasFormSubmitted(evt.target)){
try{
dojo.stopEvent(evt);
}
catch(e){
}
return false;
}
cm.setFormSubmitted(evt.target,true);
}}});
function _1502(type){
return function(evt){
cm.validation.validateMandatory(evt.target?evt.target:evt,type);
};
};
function _1503(type,_1504){
var obj={};
var fn=_1502(type);
dojo.forEach(_1504,function(evt){
obj[evt]=fn;
});
obj.found=function(node){
cm.validation.registerValidation(node.getAttribute("name"),fn,node);
fn(node);
};
return obj;
};
cm.registerBehavior("MANDATORY_FIELD_VALIDATION",{"input[type='text'],input[type='password']":_1503("text",["blur","onkeyup"]),"input[type='checkbox']":_1503("checkbox",["blur","onclick"]),"select":_1503("select",["blur","onchange"]),"input[type='radio']":_1503("radio",["blur","onclick"])});
return cm;
});
},"curam/layout/CuramTabContainer":function(){
define(["dojo/_base/lang","dojo/_base/declare","dijit/layout/TabContainer","curam/layout/ScrollingTabController","dijit/layout/TabController"],function(lang,_1505,_1506,_1507,_1508){
var _1509=_1505("curam.layout.CuramTabContainer",dijit.layout.TabContainer,{postMixInProperties:function(){
if(!this.controllerWidget){
this.controllerWidget=(this.tabPosition=="top"||this.tabPosition=="bottom")&&!this.nested?_1507:_1508;
}
this.inherited(arguments);
}});
return _1509;
});
},"dijit/form/_FormSelectWidget":function(){
define(["dojo/_base/array","dojo/_base/Deferred","dojo/aspect","dojo/data/util/sorter","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/when","dojo/store/util/QueryResults","./_FormValueWidget"],function(array,_150a,_150b,_150c,_150d,dom,_150e,_150f,lang,query,when,_1510,_1511){
var _1512=_150d("dijit.form._FormSelectWidget",_1511,{multiple:false,options:null,store:null,_setStoreAttr:function(val){
if(this._created){
this._deprecatedSetStore(val);
}
},query:null,_setQueryAttr:function(query){
if(this._created){
this._deprecatedSetStore(this.store,this.selectedValue,{query:query});
}
},queryOptions:null,_setQueryOptionsAttr:function(_1513){
if(this._created){
this._deprecatedSetStore(this.store,this.selectedValue,{queryOptions:_1513});
}
},labelAttr:"",onFetch:null,sortByLabel:true,loadChildrenOnOpen:false,onLoadDeferred:null,getOptions:function(_1514){
var opts=this.options||[];
if(_1514==null){
return opts;
}
if(lang.isArrayLike(_1514)){
return array.map(_1514,"return this.getOptions(item);",this);
}
if(lang.isString(_1514)){
_1514={value:_1514};
}
if(lang.isObject(_1514)){
if(!array.some(opts,function(_1515,idx){
for(var a in _1514){
if(!(a in _1515)||_1515[a]!=_1514[a]){
return false;
}
}
_1514=idx;
return true;
})){
_1514=-1;
}
}
if(_1514>=0&&_1514<opts.length){
return opts[_1514];
}
return null;
},addOption:function(_1516){
array.forEach(lang.isArrayLike(_1516)?_1516:[_1516],function(i){
if(i&&lang.isObject(i)){
this.options.push(i);
}
},this);
this._loadChildren();
},removeOption:function(_1517){
var _1518=this.getOptions(lang.isArrayLike(_1517)?_1517:[_1517]);
array.forEach(_1518,function(_1519){
if(_1519){
this.options=array.filter(this.options,function(node){
return (node.value!==_1519.value||node.label!==_1519.label);
});
this._removeOptionItem(_1519);
}
},this);
this._loadChildren();
},updateOption:function(_151a){
array.forEach(lang.isArrayLike(_151a)?_151a:[_151a],function(i){
var _151b=this.getOptions({value:i.value}),k;
if(_151b){
for(k in i){
_151b[k]=i[k];
}
}
},this);
this._loadChildren();
},setStore:function(store,_151c,_151d){
_150f.deprecated(this.declaredClass+"::setStore(store, selectedValue, fetchArgs) is deprecated. Use set('query', fetchArgs.query), set('queryOptions', fetchArgs.queryOptions), set('store', store), or set('value', selectedValue) instead.","","2.0");
this._deprecatedSetStore(store,_151c,_151d);
},_deprecatedSetStore:function(store,_151e,_151f){
var _1520=this.store;
_151f=_151f||{};
if(_1520!==store){
var h;
while((h=this._notifyConnections.pop())){
h.remove();
}
if(!store.get){
lang.mixin(store,{_oldAPI:true,get:function(id){
var _1521=new _150a();
this.fetchItemByIdentity({identity:id,onItem:function(_1522){
_1521.resolve(_1522);
},onError:function(error){
_1521.reject(error);
}});
return _1521.promise;
},query:function(query,_1523){
var _1524=new _150a(function(){
if(_1525.abort){
_1525.abort();
}
});
_1524.total=new _150a();
var _1525=this.fetch(lang.mixin({query:query,onBegin:function(count){
_1524.total.resolve(count);
},onComplete:function(_1526){
_1524.resolve(_1526);
},onError:function(error){
_1524.reject(error);
}},_1523));
return new _1510(_1524);
}});
if(store.getFeatures()["dojo.data.api.Notification"]){
this._notifyConnections=[_150b.after(store,"onNew",lang.hitch(this,"_onNewItem"),true),_150b.after(store,"onDelete",lang.hitch(this,"_onDeleteItem"),true),_150b.after(store,"onSet",lang.hitch(this,"_onSetItem"),true)];
}
}
this._set("store",store);
}
if(this.options&&this.options.length){
this.removeOption(this.options);
}
if(this._queryRes&&this._queryRes.close){
this._queryRes.close();
}
if(this._observeHandle&&this._observeHandle.remove){
this._observeHandle.remove();
this._observeHandle=null;
}
if(_151f.query){
this._set("query",_151f.query);
}
if(_151f.queryOptions){
this._set("queryOptions",_151f.queryOptions);
}
if(store&&store.query){
this._loadingStore=true;
this.onLoadDeferred=new _150a();
this._queryRes=store.query(this.query,this.queryOptions);
when(this._queryRes,lang.hitch(this,function(items){
if(this.sortByLabel&&!_151f.sort&&items.length){
if(store.getValue){
items.sort(_150c.createSortFunction([{attribute:store.getLabelAttributes(items[0])[0]}],store));
}else{
var _1527=this.labelAttr;
items.sort(function(a,b){
return a[_1527]>b[_1527]?1:b[_1527]>a[_1527]?-1:0;
});
}
}
if(_151f.onFetch){
items=_151f.onFetch.call(this,items,_151f);
}
array.forEach(items,function(i){
this._addOptionForItem(i);
},this);
if(this._queryRes.observe){
this._observeHandle=this._queryRes.observe(lang.hitch(this,function(_1528,_1529,_152a){
if(_1529==_152a){
this._onSetItem(_1528);
}else{
if(_1529!=-1){
this._onDeleteItem(_1528);
}
if(_152a!=-1){
this._onNewItem(_1528);
}
}
}),true);
}
this._loadingStore=false;
this.set("value","_pendingValue" in this?this._pendingValue:_151e);
delete this._pendingValue;
if(!this.loadChildrenOnOpen){
this._loadChildren();
}else{
this._pseudoLoadChildren(items);
}
this.onLoadDeferred.resolve(true);
this.onSetStore();
}),function(err){
console.error("dijit.form.Select: "+err.toString());
this.onLoadDeferred.reject(err);
});
}
return _1520;
},_setValueAttr:function(_152b,_152c){
if(!this._onChangeActive){
_152c=null;
}
if(this._loadingStore){
this._pendingValue=_152b;
return;
}
if(_152b==null){
return;
}
if(lang.isArrayLike(_152b)){
_152b=array.map(_152b,function(value){
return lang.isObject(value)?value:{value:value};
});
}else{
if(lang.isObject(_152b)){
_152b=[_152b];
}else{
_152b=[{value:_152b}];
}
}
_152b=array.filter(this.getOptions(_152b),function(i){
return i&&i.value;
});
var opts=this.getOptions()||[];
if(!this.multiple&&(!_152b[0]||!_152b[0].value)&&!!opts.length){
_152b[0]=opts[0];
}
array.forEach(opts,function(opt){
opt.selected=array.some(_152b,function(v){
return v.value===opt.value;
});
});
var val=array.map(_152b,function(opt){
return opt.value;
});
if(typeof val=="undefined"||typeof val[0]=="undefined"){
return;
}
var disp=array.map(_152b,function(opt){
return opt.label;
});
this._setDisplay(this.multiple?disp:disp[0]);
this.inherited(arguments,[this.multiple?val:val[0],_152c]);
this._updateSelection();
},_getDisplayedValueAttr:function(){
var ret=array.map([].concat(this.get("selectedOptions")),function(v){
if(v&&"label" in v){
return v.label;
}else{
if(v){
return v.value;
}
}
return null;
},this);
return this.multiple?ret:ret[0];
},_setDisplayedValueAttr:function(label){
this.set("value",this.getOptions(typeof label=="string"?{label:label}:label));
},_loadChildren:function(){
if(this._loadingStore){
return;
}
array.forEach(this._getChildren(),function(child){
child.destroyRecursive();
});
array.forEach(this.options,this._addOptionItem,this);
this._updateSelection();
},_updateSelection:function(){
this.focusedChild=null;
this._set("value",this._getValueFromOpts());
var val=[].concat(this.value);
if(val&&val[0]){
var self=this;
array.forEach(this._getChildren(),function(child){
var _152d=array.some(val,function(v){
return child.option&&(v===child.option.value);
});
if(_152d&&!self.multiple){
self.focusedChild=child;
}
_150e.toggle(child.domNode,this.baseClass.replace(/\s+|$/g,"SelectedOption "),_152d);
child.domNode.setAttribute("aria-selected",_152d?"true":"false");
},this);
}
},_getValueFromOpts:function(){
var opts=this.getOptions()||[];
if(!this.multiple&&opts.length){
var opt=array.filter(opts,function(i){
return i.selected;
})[0];
if(opt&&opt.value){
return opt.value;
}else{
opts[0].selected=true;
return opts[0].value;
}
}else{
if(this.multiple){
return array.map(array.filter(opts,function(i){
return i.selected;
}),function(i){
return i.value;
})||[];
}
}
return "";
},_onNewItem:function(item,_152e){
if(!_152e||!_152e.parent){
this._addOptionForItem(item);
}
},_onDeleteItem:function(item){
var store=this.store;
this.removeOption({value:store.getIdentity(item)});
},_onSetItem:function(item){
this.updateOption(this._getOptionObjForItem(item));
},_getOptionObjForItem:function(item){
var store=this.store,label=(this.labelAttr&&this.labelAttr in item)?item[this.labelAttr]:store.getLabel(item),value=(label?store.getIdentity(item):null);
return {value:value,label:label,item:item};
},_addOptionForItem:function(item){
var store=this.store;
if(store.isItemLoaded&&!store.isItemLoaded(item)){
store.loadItem({item:item,onItem:function(i){
this._addOptionForItem(i);
},scope:this});
return;
}
var _152f=this._getOptionObjForItem(item);
this.addOption(_152f);
},constructor:function(_1530){
this._oValue=(_1530||{}).value||null;
this._notifyConnections=[];
},buildRendering:function(){
this.inherited(arguments);
dom.setSelectable(this.focusNode,false);
},_fillContent:function(){
if(!this.options){
this.options=this.srcNodeRef?query("> *",this.srcNodeRef).map(function(node){
if(node.getAttribute("type")==="separator"){
return {value:"",label:"",selected:false,disabled:false};
}
return {value:(node.getAttribute("data-"+_150f._scopeName+"-value")||node.getAttribute("value")),label:String(node.innerHTML),selected:node.getAttribute("selected")||false,disabled:node.getAttribute("disabled")||false};
},this):[];
}
if(!this.value){
this._set("value",this._getValueFromOpts());
}else{
if(this.multiple&&typeof this.value=="string"){
this._set("value",this.value.split(","));
}
}
},postCreate:function(){
this.inherited(arguments);
_150b.after(this,"onChange",lang.hitch(this,"_updateSelection"));
var store=this.store;
if(store&&(store.getIdentity||store.getFeatures()["dojo.data.api.Identity"])){
this.store=null;
this._deprecatedSetStore(store,this._oValue,{query:this.query,queryOptions:this.queryOptions});
}
this._storeInitialized=true;
},startup:function(){
this._loadChildren();
this.inherited(arguments);
},destroy:function(){
var h;
while((h=this._notifyConnections.pop())){
h.remove();
}
if(this._queryRes&&this._queryRes.close){
this._queryRes.close();
}
if(this._observeHandle&&this._observeHandle.remove){
this._observeHandle.remove();
this._observeHandle=null;
}
this.inherited(arguments);
},_addOptionItem:function(){
},_removeOptionItem:function(){
},_setDisplay:function(){
},_getChildren:function(){
return [];
},_getSelectedOptionsAttr:function(){
return this.getOptions({selected:true});
},_pseudoLoadChildren:function(){
},onSetStore:function(){
}});
return _1512;
});
},"dijit/form/_ComboBoxMenu":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/keys","../_WidgetBase","../_TemplatedMixin","./_ComboBoxMenuMixin","./_ListMouseMixin"],function(_1531,_1532,_1533,keys,_1534,_1535,_1536,_1537){
return _1531("dijit.form._ComboBoxMenu",[_1534,_1535,_1537,_1536],{templateString:"<div class='dijitReset dijitMenu' data-dojo-attach-point='containerNode' style='overflow: auto; overflow-x: hidden;' role='listbox'>"+"<div class='dijitMenuItem dijitMenuPreviousButton' data-dojo-attach-point='previousButton' role='option'></div>"+"<div class='dijitMenuItem dijitMenuNextButton' data-dojo-attach-point='nextButton' role='option'></div>"+"</div>",baseClass:"dijitComboBoxMenu",postCreate:function(){
this.inherited(arguments);
if(!this.isLeftToRight()){
_1532.add(this.previousButton,"dijitMenuItemRtl");
_1532.add(this.nextButton,"dijitMenuItemRtl");
}
this.containerNode.setAttribute("role","listbox");
},_createMenuItem:function(){
var item=this.ownerDocument.createElement("div");
item.className="dijitReset dijitMenuItem"+(this.isLeftToRight()?"":" dijitMenuItemRtl");
item.setAttribute("role","option");
return item;
},onHover:function(node){
_1532.add(node,"dijitMenuItemHover");
},onUnhover:function(node){
_1532.remove(node,"dijitMenuItemHover");
},onSelect:function(node){
_1532.add(node,"dijitMenuItemSelected");
},onDeselect:function(node){
_1532.remove(node,"dijitMenuItemSelected");
},_page:function(up){
var _1538=0;
var _1539=this.domNode.scrollTop;
var _153a=_1533.get(this.domNode,"height");
if(!this.getHighlightedOption()){
this.selectNextNode();
}
while(_1538<_153a){
var _153b=this.getHighlightedOption();
if(up){
if(!_153b.previousSibling||_153b.previousSibling.style.display=="none"){
break;
}
this.selectPreviousNode();
}else{
if(!_153b.nextSibling||_153b.nextSibling.style.display=="none"){
break;
}
this.selectNextNode();
}
var _153c=this.domNode.scrollTop;
_1538+=(_153c-_1539)*(up?-1:1);
_1539=_153c;
}
},handleKey:function(evt){
switch(evt.keyCode){
case keys.DOWN_ARROW:
this.selectNextNode();
return false;
case keys.PAGE_DOWN:
this._page(false);
return false;
case keys.UP_ARROW:
this.selectPreviousNode();
return false;
case keys.PAGE_UP:
this._page(true);
return false;
default:
return true;
}
}});
});
},"dijit/layout/_TabContainerBase":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","./StackContainer","./utils","../_TemplatedMixin","dojo/text!./templates/TabContainer.html"],function(_153d,_153e,_153f,_1540,_1541,_1542,_1543,_1544){
return _153d("dijit.layout._TabContainerBase",[_1541,_1543],{tabPosition:"top",baseClass:"dijitTabContainer",tabStrip:false,nested:false,templateString:_1544,postMixInProperties:function(){
this.baseClass+=this.tabPosition.charAt(0).toUpperCase()+this.tabPosition.substr(1).replace(/-.*/,"");
this.srcNodeRef&&_1540.set(this.srcNodeRef,"visibility","hidden");
this.inherited(arguments);
},buildRendering:function(){
this.inherited(arguments);
this.tablist=this._makeController(this.tablistNode);
if(!this.doLayout){
_153e.add(this.domNode,"dijitTabContainerNoLayout");
}
if(this.nested){
_153e.add(this.domNode,"dijitTabContainerNested");
_153e.add(this.tablist.containerNode,"dijitTabContainerTabListNested");
_153e.add(this.tablistSpacer,"dijitTabContainerSpacerNested");
_153e.add(this.containerNode,"dijitTabPaneWrapperNested");
}else{
_153e.add(this.domNode,"tabStrip-"+(this.tabStrip?"enabled":"disabled"));
}
},_setupChild:function(tab){
_153e.add(tab.domNode,"dijitTabPane");
this.inherited(arguments);
},startup:function(){
if(this._started){
return;
}
this.tablist.startup();
this.inherited(arguments);
},layout:function(){
if(!this._contentBox||typeof (this._contentBox.l)=="undefined"){
return;
}
var sc=this.selectedChildWidget;
if(this.doLayout){
var _1545=this.tabPosition.replace(/-h/,"");
this.tablist.region=_1545;
var _1546=[this.tablist,{domNode:this.tablistSpacer,region:_1545},{domNode:this.containerNode,region:"center"}];
_1542.layoutChildren(this.domNode,this._contentBox,_1546);
this._containerContentBox=_1542.marginBox2contentBox(this.containerNode,_1546[2]);
if(sc&&sc.resize){
sc.resize(this._containerContentBox);
}
}else{
if(this.tablist.resize){
var s=this.tablist.domNode.style;
s.width="0";
var width=_153f.getContentBox(this.domNode).w;
s.width="";
this.tablist.resize({w:width});
}
if(sc&&sc.resize){
sc.resize();
}
}
},destroy:function(_1547){
if(this.tablist){
this.tablist.destroy(_1547);
}
this.inherited(arguments);
}});
});
},"dijit/_KeyNavContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/_base/kernel","dojo/keys","dojo/_base/lang","./registry","./_Container","./_FocusMixin","./_KeyNavMixin"],function(array,_1548,_1549,_154a,keys,lang,_154b,_154c,_154d,_154e){
return _1548("dijit._KeyNavContainer",[_154d,_154e,_154c],{connectKeyNavHandlers:function(_154f,_1550){
var _1551=(this._keyNavCodes={});
var prev=lang.hitch(this,"focusPrev");
var next=lang.hitch(this,"focusNext");
array.forEach(_154f,function(code){
_1551[code]=prev;
});
array.forEach(_1550,function(code){
_1551[code]=next;
});
_1551[keys.HOME]=lang.hitch(this,"focusFirstChild");
_1551[keys.END]=lang.hitch(this,"focusLastChild");
},startupKeyNavChildren:function(){
_154a.deprecated("startupKeyNavChildren() call no longer needed","","2.0");
},startup:function(){
this.inherited(arguments);
array.forEach(this.getChildren(),lang.hitch(this,"_startupChild"));
},addChild:function(_1552,_1553){
this.inherited(arguments);
this._startupChild(_1552);
},_startupChild:function(_1554){
_1554.set("tabIndex","-1");
},_getFirst:function(){
var _1555=this.getChildren();
return _1555.length?_1555[0]:null;
},_getLast:function(){
var _1556=this.getChildren();
return _1556.length?_1556[_1556.length-1]:null;
},focusNext:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,1));
},focusPrev:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,-1),true);
},childSelector:function(node){
var node=_154b.byNode(node);
return node&&node.getParent()==this;
}});
});
},"dijit/form/DataList":function(){
define(["dojo/_base/declare","dojo/dom","dojo/_base/lang","dojo/query","dojo/store/Memory","../registry"],function(_1557,dom,lang,query,_1558,_1559){
function _155a(_155b){
return {id:_155b.value,value:_155b.value,name:lang.trim(_155b.innerText||_155b.textContent||"")};
};
return _1557("dijit.form.DataList",_1558,{constructor:function(_155c,_155d){
this.domNode=dom.byId(_155d);
lang.mixin(this,_155c);
if(this.id){
_1559.add(this);
}
this.domNode.style.display="none";
this.inherited(arguments,[{data:query("option",this.domNode).map(_155a)}]);
},destroy:function(){
_1559.remove(this.id);
},fetchSelectedItem:function(){
var _155e=query("> option[selected]",this.domNode)[0]||query("> option",this.domNode)[0];
return _155e&&_155a(_155e);
}});
});
},"dijit/Tooltip":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/fx","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","./_base/manager","./place","./_Widget","./_TemplatedMixin","./BackgroundIframe","dojo/text!./templates/Tooltip.html","./main"],function(array,_155f,fx,dom,_1560,_1561,_1562,lang,mouse,on,has,_1563,place,_1564,_1565,_1566,_1567,dijit){
var _1568=_155f("dijit._MasterTooltip",[_1564,_1565],{duration:_1563.defaultDuration,templateString:_1567,postCreate:function(){
this.ownerDocumentBody.appendChild(this.domNode);
this.bgIframe=new _1566(this.domNode);
this.fadeIn=fx.fadeIn({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onShow")});
this.fadeOut=fx.fadeOut({node:this.domNode,duration:this.duration,onEnd:lang.hitch(this,"_onHide")});
},show:function(_1569,_156a,_156b,rtl,_156c,_156d,_156e){
if(this.aroundNode&&this.aroundNode===_156a&&this.containerNode.innerHTML==_1569){
return;
}
if(this.fadeOut.status()=="playing"){
this._onDeck=arguments;
return;
}
this.containerNode.innerHTML=_1569;
if(_156c){
this.set("textDir",_156c);
}
this.containerNode.align=rtl?"right":"left";
var pos=place.around(this.domNode,_156a,_156b&&_156b.length?_156b:_156f.defaultPosition,!rtl,lang.hitch(this,"orient"));
var _1570=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_1570.y+((_1570.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_1570.x+((_1570.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}else{
this.connectorNode.style.left="";
this.connectorNode.style.top="";
}
}
_1562.set(this.domNode,"opacity",0);
this.fadeIn.play();
this.isShowingNow=true;
this.aroundNode=_156a;
this.onMouseEnter=_156d||noop;
this.onMouseLeave=_156e||noop;
},orient:function(node,_1571,_1572,_1573,_1574){
this.connectorNode.style.top="";
var _1575=_1573.h,_1576=_1573.w;
node.className="dijitTooltip "+{"MR-ML":"dijitTooltipRight","ML-MR":"dijitTooltipLeft","TM-BM":"dijitTooltipAbove","BM-TM":"dijitTooltipBelow","BL-TL":"dijitTooltipBelow dijitTooltipABLeft","TL-BL":"dijitTooltipAbove dijitTooltipABLeft","BR-TR":"dijitTooltipBelow dijitTooltipABRight","TR-BR":"dijitTooltipAbove dijitTooltipABRight","BR-BL":"dijitTooltipRight","BL-BR":"dijitTooltipLeft"}[_1571+"-"+_1572];
this.domNode.style.width="auto";
var size=_1561.position(this.domNode);
if(has("ie")||has("trident")){
size.w+=2;
}
var width=Math.min((Math.max(_1576,1)),size.w);
_1561.setMarginBox(this.domNode,{w:width});
if(_1572.charAt(0)=="B"&&_1571.charAt(0)=="B"){
var bb=_1561.position(node);
var _1577=this.connectorNode.offsetHeight;
if(bb.h>_1575){
var _1578=_1575-((_1574.h+_1577)>>1);
this.connectorNode.style.top=_1578+"px";
this.connectorNode.style.bottom="";
}else{
this.connectorNode.style.bottom=Math.min(Math.max(_1574.h/2-_1577/2,0),bb.h-_1577)+"px";
this.connectorNode.style.top="";
}
}else{
this.connectorNode.style.top="";
this.connectorNode.style.bottom="";
}
return Math.max(0,size.w-_1576);
},_onShow:function(){
if(has("ie")){
this.domNode.style.filter="";
}
},hide:function(_1579){
if(this._onDeck&&this._onDeck[1]==_1579){
this._onDeck=null;
}else{
if(this.aroundNode===_1579){
this.fadeIn.stop();
this.isShowingNow=false;
this.aroundNode=null;
this.fadeOut.play();
}else{
}
}
this.onMouseEnter=this.onMouseLeave=noop;
},_onHide:function(){
this.domNode.style.cssText="";
this.containerNode.innerHTML="";
if(this._onDeck){
this.show.apply(this,this._onDeck);
this._onDeck=null;
}
}});
if(has("dojo-bidi")){
_1568.extend({_setAutoTextDir:function(node){
this.applyTextDir(node);
array.forEach(node.children,function(child){
this._setAutoTextDir(child);
},this);
},_setTextDirAttr:function(_157a){
this._set("textDir",_157a);
if(_157a=="auto"){
this._setAutoTextDir(this.containerNode);
}else{
this.containerNode.dir=this.textDir;
}
}});
}
dijit.showTooltip=function(_157b,_157c,_157d,rtl,_157e,_157f,_1580){
if(_157d){
_157d=array.map(_157d,function(val){
return {after:"after-centered",before:"before-centered"}[val]||val;
});
}
if(!_156f._masterTT){
dijit._masterTT=_156f._masterTT=new _1568();
}
return _156f._masterTT.show(_157b,_157c,_157d,rtl,_157e,_157f,_1580);
};
dijit.hideTooltip=function(_1581){
return _156f._masterTT&&_156f._masterTT.hide(_1581);
};
var _1582="DORMANT",_1583="SHOW TIMER",_1584="SHOWING",_1585="HIDE TIMER";
function noop(){
};
var _156f=_155f("dijit.Tooltip",_1564,{label:"",showDelay:400,hideDelay:400,connectId:[],position:[],selector:"",_setConnectIdAttr:function(newId){
array.forEach(this._connections||[],function(_1586){
array.forEach(_1586,function(_1587){
_1587.remove();
});
},this);
this._connectIds=array.filter(lang.isArrayLike(newId)?newId:(newId?[newId]:[]),function(id){
return dom.byId(id,this.ownerDocument);
},this);
this._connections=array.map(this._connectIds,function(id){
var node=dom.byId(id,this.ownerDocument),_1588=this.selector,_1589=_1588?function(_158a){
return on.selector(_1588,_158a);
}:function(_158b){
return _158b;
},self=this;
return [on(node,_1589(mouse.enter),function(){
self._onHover(this);
}),on(node,_1589("focusin"),function(){
self._onHover(this);
}),on(node,_1589(mouse.leave),lang.hitch(self,"_onUnHover")),on(node,_1589("focusout"),lang.hitch(self,"set","state",_1582))];
},this);
this._set("connectId",newId);
},addTarget:function(node){
var id=node.id||node;
if(array.indexOf(this._connectIds,id)==-1){
this.set("connectId",this._connectIds.concat(id));
}
},removeTarget:function(node){
var id=node.id||node,idx=array.indexOf(this._connectIds,id);
if(idx>=0){
this._connectIds.splice(idx,1);
this.set("connectId",this._connectIds);
}
},buildRendering:function(){
this.inherited(arguments);
_1560.add(this.domNode,"dijitTooltipData");
},startup:function(){
this.inherited(arguments);
var ids=this.connectId;
array.forEach(lang.isArrayLike(ids)?ids:[ids],this.addTarget,this);
},getContent:function(node){
return this.label||this.domNode.innerHTML;
},state:_1582,_setStateAttr:function(val){
if(this.state==val||(val==_1583&&this.state==_1584)||(val==_1585&&this.state==_1582)){
return;
}
if(this._hideTimer){
this._hideTimer.remove();
delete this._hideTimer;
}
if(this._showTimer){
this._showTimer.remove();
delete this._showTimer;
}
switch(val){
case _1582:
if(this._connectNode){
_156f.hide(this._connectNode);
delete this._connectNode;
this.onHide();
}
break;
case _1583:
if(this.state!=_1584){
this._showTimer=this.defer(function(){
this.set("state",_1584);
},this.showDelay);
}
break;
case _1584:
var _158c=this.getContent(this._connectNode);
if(!_158c){
this.set("state",_1582);
return;
}
_156f.show(_158c,this._connectNode,this.position,!this.isLeftToRight(),this.textDir,lang.hitch(this,"set","state",_1584),lang.hitch(this,"set","state",_1585));
this.onShow(this._connectNode,this.position);
break;
case _1585:
this._hideTimer=this.defer(function(){
this.set("state",_1582);
},this.hideDelay);
break;
}
this._set("state",val);
},_onHover:function(_158d){
if(this._connectNode&&_158d!=this._connectNode){
this.set("state",_1582);
}
this._connectNode=_158d;
this.set("state",_1583);
},_onUnHover:function(_158e){
this.set("state",_1585);
},open:function(_158f){
this.set("state",_1582);
this._connectNode=_158f;
this.set("state",_1584);
},close:function(){
this.set("state",_1582);
},onShow:function(){
},onHide:function(){
},destroy:function(){
this.set("state",_1582);
array.forEach(this._connections||[],function(_1590){
array.forEach(_1590,function(_1591){
_1591.remove();
});
},this);
this.inherited(arguments);
}});
_156f._MasterTooltip=_1568;
_156f.show=dijit.showTooltip;
_156f.hide=dijit.hideTooltip;
_156f.defaultPosition=["after-centered","before-centered"];
return _156f;
});
},"dijit/PopupMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-style","dojo/_base/lang","dojo/query","./popup","./registry","./MenuItem","./hccss"],function(_1592,_1593,lang,query,pm,_1594,_1595){
return _1592("dijit.PopupMenuItem",_1595,{baseClass:"dijitMenuItem dijitPopupMenuItem",_fillContent:function(){
if(this.srcNodeRef){
var nodes=query("*",this.srcNodeRef);
this.inherited(arguments,[nodes[0]]);
this.dropDownContainer=this.srcNodeRef;
}
},_openPopup:function(_1596,focus){
var popup=this.popup;
pm.open(lang.delegate(_1596,{popup:this.popup,around:this.domNode}));
if(focus&&popup.focus){
popup.focus();
}
},_closePopup:function(){
pm.close(this.popup);
this.popup.parentMenu=null;
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
if(!this.popup){
var node=query("[widgetId]",this.dropDownContainer)[0];
this.popup=_1594.byNode(node);
}
this.ownerDocumentBody.appendChild(this.popup.domNode);
this.popup.domNode.setAttribute("aria-labelledby",this.containerNode.id);
this.popup.startup();
this.popup.domNode.style.display="none";
if(this.arrowWrapper){
_1593.set(this.arrowWrapper,"visibility","");
}
this.focusNode.setAttribute("aria-haspopup","true");
},destroyDescendants:function(_1597){
if(this.popup){
if(!this.popup._destroyed){
this.popup.destroyRecursive(_1597);
}
delete this.popup;
}
this.inherited(arguments);
}});
});
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"dijit/layout/ContentPane":function(){
define(["dojo/_base/kernel","dojo/_base/lang","../_Widget","../_Container","./_ContentPaneResizeMixin","dojo/string","dojo/html","dojo/i18n!../nls/loading","dojo/_base/array","dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-construct","dojo/_base/xhr","dojo/i18n","dojo/when"],function(_1598,lang,_1599,_159a,_159b,_159c,html,_159d,array,_159e,_159f,dom,_15a0,_15a1,xhr,i18n,when){
var _15a2=typeof (dojo.global.perf)!="undefined"&&dojo.global.perf!="undefined";
return _159e("dijit.layout.ContentPane",[_1599,_159a,_159b],{href:"",content:"",extractContent:false,parseOnLoad:true,parserScope:_1598._scopeName,preventCache:false,preload:false,refreshOnShow:false,loadingMessage:"<span class='dijitContentPaneLoading'><span class='dijitInline dijitIconLoading'></span>${loadingState}</span>",errorMessage:"<span class='dijitContentPaneError'><span class='dijitInline dijitIconError'></span>${errorState}</span>",isLoaded:false,baseClass:"dijitContentPane",ioArgs:{},onLoadDeferred:null,_setTitleAttr:null,stopParser:true,template:false,markupFactory:function(_15a3,node,ctor){
var self=new ctor(_15a3,node);
return !self.href&&self._contentSetter&&self._contentSetter.parseDeferred&&!self._contentSetter.parseDeferred.isFulfilled()?self._contentSetter.parseDeferred.then(function(){
return self;
}):self;
},create:function(_15a4,_15a5){
if((!_15a4||!_15a4.template)&&_15a5&&!("href" in _15a4)&&!("content" in _15a4)){
_15a5=dom.byId(_15a5);
var df=_15a5.ownerDocument.createDocumentFragment();
while(_15a5.firstChild){
df.appendChild(_15a5.firstChild);
}
_15a4=lang.delegate(_15a4,{content:df});
}
this.inherited(arguments,[_15a4,_15a5]);
},postMixInProperties:function(){
this.inherited(arguments);
var _15a6=i18n.getLocalization("dijit","loading",this.lang);
this.loadingMessage=_159c.substitute(this.loadingMessage,_15a6);
this.errorMessage=_159c.substitute(this.errorMessage,_15a6);
},buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
this.domNode.removeAttribute("title");
},startup:function(){
this.inherited(arguments);
if(this._contentSetter){
array.forEach(this._contentSetter.parseResults,function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
},this);
}
},_startChildren:function(){
array.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
if(this._contentSetter){
array.forEach(this._contentSetter.parseResults,function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
},this);
}
},setHref:function(href){
_1598.deprecated("dijit.layout.ContentPane.setHref() is deprecated. Use set('href', ...) instead.","","2.0");
return this.set("href",href);
},_setHrefAttr:function(href){
this.cancel();
this.onLoadDeferred=new _159f(lang.hitch(this,"cancel"));
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
this._set("href",href);
if(this.preload||(this._created&&this._isShown())){
this._load();
}else{
this._hrefChanged=true;
}
return this.onLoadDeferred;
},setContent:function(data){
_1598.deprecated("dijit.layout.ContentPane.setContent() is deprecated.  Use set('content', ...) instead.","","2.0");
this.set("content",data);
},_setContentAttr:function(data){
this._set("href","");
this.cancel();
this.onLoadDeferred=new _159f(lang.hitch(this,"cancel"));
if(this._created){
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
}
this._setContent(data||"");
this._isDownloaded=false;
return this.onLoadDeferred;
},_getContentAttr:function(){
return this.containerNode.innerHTML;
},cancel:function(){
if(this._xhrDfd&&(this._xhrDfd.fired==-1)){
this._xhrDfd.cancel();
}
delete this._xhrDfd;
this.onLoadDeferred=null;
},destroy:function(){
this.cancel();
this.inherited(arguments);
},destroyRecursive:function(_15a7){
if(this._beingDestroyed){
return;
}
this.inherited(arguments);
},_onShow:function(){
this.inherited(arguments);
if(this.href){
if(!this._xhrDfd&&(!this.isLoaded||this._hrefChanged||this.refreshOnShow)){
return this.refresh();
}
}
},refresh:function(){
this.cancel();
this.onLoadDeferred=new _159f(lang.hitch(this,"cancel"));
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
this._load();
return this.onLoadDeferred;
},_load:function(){
if(_15a2){
perf.widgetStartedLoadingCallback();
}
this._setContent(this.onDownloadStart(),true);
var self=this;
var _15a8={preventCache:(this.preventCache||this.refreshOnShow),url:this.href,handleAs:"text"};
if(lang.isObject(this.ioArgs)){
lang.mixin(_15a8,this.ioArgs);
}
var hand=(this._xhrDfd=(this.ioMethod||xhr.get)(_15a8)),_15a9;
hand.then(function(html){
_15a9=html;
try{
self._isDownloaded=true;
return self._setContent(html,false);
}
catch(err){
self._onError("Content",err);
}
},function(err){
if(!hand.canceled){
self._onError("Download",err);
}
delete self._xhrDfd;
return err;
}).then(function(){
self.onDownloadEnd();
if(_15a2){
perf.widgetLoadedCallback(self);
}
delete self._xhrDfd;
return _15a9;
});
delete this._hrefChanged;
},_onLoadHandler:function(data){
this._set("isLoaded",true);
try{
this.onLoadDeferred.resolve(data);
}
catch(e){
console.error("Error "+this.widgetId+" running custom onLoad code: "+e.message);
}
},_onUnloadHandler:function(){
this._set("isLoaded",false);
try{
this.onUnload();
}
catch(e){
console.error("Error "+this.widgetId+" running custom onUnload code: "+e.message);
}
},destroyDescendants:function(_15aa){
if(this.isLoaded){
this._onUnloadHandler();
}
var _15ab=this._contentSetter;
array.forEach(this.getChildren(),function(_15ac){
if(_15ac.destroyRecursive){
_15ac.destroyRecursive(_15aa);
}else{
if(_15ac.destroy){
_15ac.destroy(_15aa);
}
}
_15ac._destroyed=true;
});
if(_15ab){
array.forEach(_15ab.parseResults,function(_15ad){
if(!_15ad._destroyed){
if(_15ad.destroyRecursive){
_15ad.destroyRecursive(_15aa);
}else{
if(_15ad.destroy){
if((_15ad["class"]==="rotator"||_15ad["class"]==="hcrRotatorNav")&&!_15ad.wfe){
_15ad.wfe={remove:function(){
}};
}
_15ad.destroy(_15aa);
}
}
_15ad._destroyed=true;
}
});
delete _15ab.parseResults;
}
if(!_15aa){
_15a1.empty(this.containerNode);
}
delete this._singleChild;
},_setContent:function(cont,_15ae){
this.destroyDescendants();
var _15af=this._contentSetter;
if(!(_15af&&_15af instanceof html._ContentSetter)){
_15af=this._contentSetter=new html._ContentSetter({node:this.containerNode,_onError:lang.hitch(this,this._onError),onContentError:lang.hitch(this,function(e){
var _15b0=this.onContentError(e);
try{
this.containerNode.innerHTML=_15b0;
}
catch(e){
console.error("Fatal "+this.id+" could not change content due to "+e.message,e);
}
})});
}
var _15b1=lang.mixin({cleanContent:this.cleanContent,extractContent:this.extractContent,parseContent:!cont.domNode&&this.parseOnLoad,parserScope:this.parserScope,startup:false,dir:this.dir,lang:this.lang,textDir:this.textDir},this._contentSetterParams||{});
var p=_15af.set((lang.isObject(cont)&&cont.domNode)?cont.domNode:cont,_15b1);
var self=this;
return when(p&&p.then?p:_15af.parseDeferred,function(){
delete self._contentSetterParams;
if(!_15ae){
if(self._started){
self._startChildren();
self._scheduleLayout();
}
self._onLoadHandler(cont);
}
});
},_onError:function(type,err,_15b2){
this.onLoadDeferred.reject(err);
var _15b3=this["on"+type+"Error"].call(this,err);
if(_15b2){
console.error(_15b2,err);
}else{
if(_15b3){
this._setContent(_15b3,true);
}
}
},onLoad:function(){
},onUnload:function(){
},onDownloadStart:function(){
return this.loadingMessage;
},onContentError:function(){
},onDownloadError:function(){
return this.errorMessage;
},onDownloadEnd:function(){
}});
});
},"dijit/_WidgetsInTemplateMixin":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/_base/lang","dojo/parser"],function(array,_15b4,_15b5,lang,_15b6){
return _15b5("dijit._WidgetsInTemplateMixin",null,{_earlyTemplatedStartup:false,widgetsInTemplate:true,contextRequire:null,_beforeFillContent:function(){
if(this.widgetsInTemplate){
var node=this.domNode;
if(this.containerNode&&!this.searchContainerNode){
this.containerNode.stopParser=true;
}
_15b6.parse(node,{noStart:!this._earlyTemplatedStartup,template:true,inherited:{dir:this.dir,lang:this.lang,textDir:this.textDir},propsThis:this,contextRequire:this.contextRequire,scope:"dojo"}).then(lang.hitch(this,function(_15b7){
this._startupWidgets=_15b7;
for(var i=0;i<_15b7.length;i++){
this._processTemplateNode(_15b7[i],function(n,p){
return n[p];
},function(_15b8,type,_15b9){
if(type in _15b8){
return _15b8.connect(_15b8,type,_15b9);
}else{
return _15b8.on(type,_15b9,true);
}
});
}
if(this.containerNode&&this.containerNode.stopParser){
delete this.containerNode.stopParser;
}
}));
if(!this._startupWidgets){
throw new Error(this.declaredClass+": parser returned unfilled promise (probably waiting for module auto-load), "+"unsupported by _WidgetsInTemplateMixin.   Must pre-load all supporting widgets before instantiation.");
}
}
},_processTemplateNode:function(_15ba,_15bb,_15bc){
if(_15bb(_15ba,"dojoType")||_15bb(_15ba,"data-dojo-type")){
return true;
}
return this.inherited(arguments);
},startup:function(){
array.forEach(this._startupWidgets,function(w){
if(w&&!w._started&&w.startup){
w.startup();
}
});
this._startupWidgets=null;
this.inherited(arguments);
}});
});
},"dijit/_HasDropDown":function(){
define(["dojo/_base/declare","dojo/_base/Deferred","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","dojo/touch","./registry","./focus","./popup","./_FocusMixin"],function(_15bd,_15be,dom,_15bf,_15c0,_15c1,_15c2,has,keys,lang,on,touch,_15c3,focus,popup,_15c4){
return _15bd("dijit._HasDropDown",_15c4,{_buttonNode:null,_arrowWrapperNode:null,_popupStateNode:null,_aroundNode:null,dropDown:null,autoWidth:true,forceWidth:false,maxHeight:-1,dropDownPosition:["below","above"],_stopClickEvents:true,_onDropDownMouseDown:function(e){
if(this.disabled||this.readOnly){
return;
}
if(e.type!="MSPointerDown"&&e.type!="pointerdown"){
e.preventDefault();
}
this.own(on.once(this.ownerDocument,touch.release,lang.hitch(this,"_onDropDownMouseUp")));
this.toggleDropDown();
},_onDropDownMouseUp:function(e){
var _15c5=this.dropDown,_15c6=false;
if(e&&this._opened){
var c=_15c1.position(this._buttonNode,true);
if(!(e.pageX>=c.x&&e.pageX<=c.x+c.w)||!(e.pageY>=c.y&&e.pageY<=c.y+c.h)){
var t=e.target;
while(t&&!_15c6){
if(_15c0.contains(t,"dijitPopup")){
_15c6=true;
}else{
t=t.parentNode;
}
}
if(_15c6){
t=e.target;
if(_15c5.onItemClick){
var _15c7;
while(t&&!(_15c7=_15c3.byNode(t))){
t=t.parentNode;
}
if(_15c7&&_15c7.onClick&&_15c7.getParent){
_15c7.getParent().onItemClick(_15c7,e);
}
}
return;
}
}
}
if(this._opened){
if(_15c5.focus&&(_15c5.autoFocus!==false||(e.type=="mouseup"&&!this.hovering))){
this._focusDropDownTimer=this.defer(function(){
_15c5.focus();
delete this._focusDropDownTimer;
});
}
}else{
if(this.focus){
this.defer("focus");
}
}
},_onDropDownClick:function(e){
if(this._stopClickEvents){
e.stopPropagation();
e.preventDefault();
}
},buildRendering:function(){
this.inherited(arguments);
this._buttonNode=this._buttonNode||this.focusNode||this.domNode;
this._popupStateNode=this._popupStateNode||this.focusNode||this._buttonNode;
var _15c8={"after":this.isLeftToRight()?"Right":"Left","before":this.isLeftToRight()?"Left":"Right","above":"Up","below":"Down","left":"Left","right":"Right"}[this.dropDownPosition[0]]||this.dropDownPosition[0]||"Down";
_15c0.add(this._arrowWrapperNode||this._buttonNode,"dijit"+_15c8+"ArrowButton");
},postCreate:function(){
this.inherited(arguments);
var _15c9=this.focusNode||this.domNode;
this.own(on(this._buttonNode,touch.press,lang.hitch(this,"_onDropDownMouseDown")),on(this._buttonNode,"click",lang.hitch(this,"_onDropDownClick")),on(_15c9,"keydown",lang.hitch(this,"_onKey")),on(_15c9,"keyup",lang.hitch(this,"_onKeyUp")));
},destroy:function(){
if(this._opened){
this.closeDropDown(true);
}
if(this.dropDown){
if(!this.dropDown._destroyed){
this.dropDown.destroyRecursive();
}
delete this.dropDown;
}
this.inherited(arguments);
},_onKey:function(e){
if(this.disabled||this.readOnly){
return;
}
var d=this.dropDown,_15ca=e.target;
if(d&&this._opened&&d.handleKey){
if(d.handleKey(e)===false){
e.stopPropagation();
e.preventDefault();
return;
}
}
if(d&&this._opened&&e.keyCode==keys.ESCAPE){
this.closeDropDown();
e.stopPropagation();
e.preventDefault();
}else{
if(!this._opened&&(e.keyCode==keys.DOWN_ARROW||((e.keyCode==keys.ENTER||(e.keyCode==keys.SPACE&&(!this._searchTimer||(e.ctrlKey||e.altKey||e.metaKey))))&&((_15ca.tagName||"").toLowerCase()!=="input"||(_15ca.type&&_15ca.type.toLowerCase()!=="text"))))){
this._toggleOnKeyUp=true;
e.stopPropagation();
e.preventDefault();
}
}
},_onKeyUp:function(){
if(this._toggleOnKeyUp){
delete this._toggleOnKeyUp;
this.toggleDropDown();
var d=this.dropDown;
if(d&&d.focus){
this.defer(lang.hitch(d,"focus"),1);
}
}
},_onBlur:function(){
this.closeDropDown(false);
this.inherited(arguments);
},isLoaded:function(){
return true;
},loadDropDown:function(_15cb){
_15cb();
},loadAndOpenDropDown:function(){
var d=new _15be(),_15cc=lang.hitch(this,function(){
this.openDropDown();
d.resolve(this.dropDown);
});
if(!this.isLoaded()){
this.loadDropDown(_15cc);
}else{
_15cc();
}
return d;
},toggleDropDown:function(){
if(this.disabled||this.readOnly){
return;
}
if(!this._opened){
this.loadAndOpenDropDown();
}else{
this.closeDropDown(true);
}
},openDropDown:function(){
var _15cd=this.dropDown,_15ce=_15cd.domNode,_15cf=this._aroundNode||this.domNode,self=this;
var _15d0=popup.open({parent:this,popup:_15cd,around:_15cf,orient:this.dropDownPosition,maxHeight:this.maxHeight,onExecute:function(){
self.closeDropDown(true);
},onCancel:function(){
self.closeDropDown(true);
},onClose:function(){
_15bf.set(self._popupStateNode,"popupActive",false);
_15c0.remove(self._popupStateNode,"dijitHasDropDownOpen");
self._set("_opened",false);
}});
if(this.forceWidth||(this.autoWidth&&_15cf.offsetWidth>_15cd._popupWrapper.offsetWidth)){
var _15d1=_15cf.offsetWidth-_15cd._popupWrapper.offsetWidth;
var _15d2={w:_15cd.domNode.offsetWidth+_15d1};
this._origStyle=_15ce.style.cssText;
if(lang.isFunction(_15cd.resize)){
_15cd.resize(_15d2);
}else{
_15c1.setMarginBox(_15ce,_15d2);
}
if(_15d0.corner[1]=="R"){
_15cd._popupWrapper.style.left=(_15cd._popupWrapper.style.left.replace("px","")-_15d1)+"px";
}
}
_15bf.set(this._popupStateNode,"popupActive","true");
_15c0.add(this._popupStateNode,"dijitHasDropDownOpen");
this._set("_opened",true);
this._popupStateNode.setAttribute("aria-expanded","true");
this._popupStateNode.setAttribute("aria-owns",_15cd.id);
if(_15ce.getAttribute("role")!=="presentation"&&!_15ce.getAttribute("aria-labelledby")){
_15ce.setAttribute("aria-labelledby",this.id);
}
return _15d0;
},closeDropDown:function(focus){
if(this._focusDropDownTimer){
this._focusDropDownTimer.remove();
delete this._focusDropDownTimer;
}
if(this._opened){
this._popupStateNode.setAttribute("aria-expanded","false");
if(focus&&this.focus){
this.focus();
}
popup.close(this.dropDown);
this._opened=false;
}
if(this._origStyle){
this.dropDown.domNode.style.cssText=this._origStyle;
delete this._origStyle;
}
}});
});
},"curam/cdsl/types/codetable/CodeTables":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/Deferred","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct"],function(_15d3,lang,_15d4,_15d5,_15d6){
var _15d7=_15d3(null,{_connection:null,constructor:function(_15d8){
if(!_15d8){
throw new Error("Missing parameter.");
}
if(typeof _15d8!=="object"){
throw new Error("Wrong parameter type: "+typeof _15d8);
}
this._connection=_15d8;
},getCodeTable:function(name){
return this._connection.metadata().codetables()[name];
},loadForFacades:function(_15d9){
var _15da=new _15d4();
require(["curam/cdsl/request/CuramService"],lang.hitch(this,function(_15db){
var _15dc=new _15db(this._connection),_15dd=new _15d5("CuramService","getCodetables",this._getInputStructsForLoadingCodetables(_15d9));
_15dc.call([_15dd]).then(lang.hitch(this,function(data){
_15da.resolve(this);
}),function(err){
_15da.reject(err);
});
}));
return _15da;
},_getInputStructsForLoadingCodetables:function(_15de){
var ret=[];
for(var i=0;i<_15de.length;i++){
ret.push(new _15d6({service:_15de[i].intf(),method:_15de[i].method()}));
}
return ret;
}});
return _15d7;
});
},"curam/cdsl/types/codetable/CodeTableItem":function(){
define(["dojo/_base/declare"],function(_15df){
var _15e0=_15df(null,{_code:null,_desc:null,_isDefault:null,constructor:function(code,desc){
this._code=code;
this._desc=desc;
this._isDefault=false;
},getCode:function(){
return this._code;
},getDescription:function(){
return this._desc;
},isDefault:function(_15e1){
if(typeof _15e1==="undefined"){
return this._isDefault||false;
}else{
var _15e2=this._isDefault;
this._isDefault=_15e1;
return _15e2;
}
}});
return _15e0;
});
},"curam/widget/AppBannerFilteringSelect":function(){
define(["dojo/_base/declare","dojo/_base/lang","dijit/form/FilteringSelect","curam/widget/AppBannerComboBoxMixin"],function(_15e3,lang,_15e4,_15e5){
var _15e6=_15e3("curam.widget.AppBannerFilteringSelect",[_15e4,_15e5],{});
return _15e6;
});
},"curam/GlobalVars":function(){
define(["curam/util"],function(){
var _15e7={popupMappingRepository:[],popupMappingLoaded:[],popupInputs:[],currentPopupProps:null,currentPopupInstanceName:"",popupWindow:null,popupCTCodeMappings:[],popupPropertiesRepository:[],POPUP_EMPTY_SPAN_MIN_SIZE:15,POPUP_EMPTY_SPAN_CHAR:" ",POPUP_EMPTY_SPAN_VALUE:null,replacedButtons:[]};
var gc=dojo.global.curam;
dojo.mixin(gc,_15e7);
gc.POPUP_EMPTY_SPAN_VALUE=curam.util.fillString(gc.POPUP_EMPTY_SPAN_CHAR,gc.POPUP_EMPTY_SPAN_MIN_SIZE);
return _15e7;
});
},"curam/html":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.html",{splitWithTag:function(value,delim,_15e8,_15e9){
var _15ea=value.split(delim||"\n");
if(_15ea.length<2){
return _15e9?_15e9(value):value;
}
var t=(_15e8||"div")+">";
var _15eb="<"+t,_15ec="</"+t;
if(_15e9){
for(var i=0;i<_15ea.length;i++){
_15ea[i]=_15e9(_15ea[i]);
}
}
return _15eb+_15ea.join(_15ec+_15eb)+_15ec;
}});
return curam.html;
});
},"dojo/html":function(){
define(["./_base/kernel","./_base/lang","./_base/array","./_base/declare","./dom","./dom-construct","./parser"],function(_15ed,lang,_15ee,_15ef,dom,_15f0,_15f1){
var _15f2=0;
var html={_secureForInnerHtml:function(cont){
return cont.replace(/(?:\s*<!DOCTYPE\s[^>]+>|<title[^>]*>[\s\S]*?<\/title>)/ig,"");
},_emptyNode:_15f0.empty,_setNodeContent:function(node,cont){
_15f0.empty(node);
if(cont){
if(typeof cont=="string"){
cont=_15f0.toDom(cont,node.ownerDocument);
}
if(!cont.nodeType&&lang.isArrayLike(cont)){
for(var _15f3=cont.length,i=0;i<cont.length;i=_15f3==cont.length?i+1:0){
_15f0.place(cont[i],node,"last");
}
}else{
_15f0.place(cont,node,"last");
}
}
return node;
},_ContentSetter:_15ef("dojo.html._ContentSetter",null,{node:"",content:"",id:"",cleanContent:false,extractContent:false,parseContent:false,parserScope:_15ed._scopeName,startup:true,constructor:function(_15f4,node){
lang.mixin(this,_15f4||{});
node=this.node=dom.byId(this.node||node);
if(!this.id){
this.id=["Setter",(node)?node.id||node.tagName:"",_15f2++].join("_");
}
},set:function(cont,_15f5){
if(undefined!==cont){
this.content=cont;
}
if(_15f5){
this._mixin(_15f5);
}
this.onBegin();
this.setContent();
var ret=this.onEnd();
if(ret&&ret.then){
return ret;
}else{
return this.node;
}
},setContent:function(){
var node=this.node;
if(!node){
throw new Error(this.declaredClass+": setContent given no node");
}
try{
node=html._setNodeContent(node,this.content);
}
catch(e){
var _15f6=this.onContentError(e);
try{
node.innerHTML=_15f6;
}
catch(e){
console.error("Fatal "+this.declaredClass+".setContent could not change content due to "+e.message,e);
}
}
this.node=node;
},empty:function(){
if(this.parseDeferred){
if(!this.parseDeferred.isResolved()){
this.parseDeferred.cancel();
}
delete this.parseDeferred;
}
if(this.parseResults&&this.parseResults.length){
_15ee.forEach(this.parseResults,function(w){
if(w.destroy){
w.destroy();
}
});
delete this.parseResults;
}
_15f0.empty(this.node);
},onBegin:function(){
var cont=this.content;
if(lang.isString(cont)){
if(this.cleanContent){
cont=html._secureForInnerHtml(cont);
}
if(this.extractContent){
var match=cont.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(match){
cont=match[1];
}
}
}
this.empty();
this.content=cont;
return this.node;
},onEnd:function(){
if(this.parseContent){
this._parse();
}
return this.node;
},tearDown:function(){
delete this.parseResults;
delete this.parseDeferred;
delete this.node;
delete this.content;
},onContentError:function(err){
return "Error occurred setting content: "+err;
},onExecError:function(err){
return "Error occurred executing scripts: "+err;
},_mixin:function(_15f7){
var empty={},key;
for(key in _15f7){
if(key in empty){
continue;
}
this[key]=_15f7[key];
}
},_parse:function(){
var _15f8=this.node;
try{
var _15f9={};
_15ee.forEach(["dir","lang","textDir"],function(name){
if(this[name]){
_15f9[name]=this[name];
}
},this);
var self=this;
this.parseDeferred=_15f1.parse({rootNode:_15f8,noStart:!this.startup,inherited:_15f9,scope:this.parserScope}).then(function(_15fa){
return self.parseResults=_15fa;
},function(e){
self._onError("Content",e,"Error parsing in _ContentSetter#"+this.id);
});
}
catch(e){
this._onError("Content",e,"Error parsing in _ContentSetter#"+this.id);
}
},_onError:function(type,err,_15fb){
var _15fc=this["on"+type+"Error"].call(this,err);
if(_15fb){
console.error(_15fb,err);
}else{
if(_15fc){
html._setNodeContent(this.node,_15fc,true);
}
}
}}),set:function(node,cont,_15fd){
if(undefined==cont){
console.warn("dojo.html.set: no cont argument provided, using empty string");
cont="";
}
if(!_15fd){
return html._setNodeContent(node,cont,true);
}else{
var op=new html._ContentSetter(lang.mixin(_15fd,{content:cont,node:node}));
return op.set();
}
}};
lang.setObject("dojo.html",html);
return html;
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_15fe,lang,array,when){
return _15fe("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_15ff){
if(_15ff){
this.set(_15ff);
}
},_get:function(name,names){
return typeof this[names.g]==="function"?this[names.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,value){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var names=this._getAttrNames(name),_1600=this._get(name,names),_1601=this[names.s],_1602;
if(typeof _1601==="function"){
_1602=_1601.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=value;
}
if(this._watchCallbacks){
var self=this;
when(_1602,function(){
self._watchCallbacks(name,_1600,value);
});
}
return this;
},_changeAttrValue:function(name,value){
var _1603=this.get(name);
this[name]=value;
if(this._watchCallbacks){
this._watchCallbacks(name,_1603,value);
}
return this;
},watch:function(name,_1604){
var _1605=this._watchCallbacks;
if(!_1605){
var self=this;
_1605=this._watchCallbacks=function(name,_1606,value,_1607){
var _1608=function(_1609){
if(_1609){
_1609=_1609.slice();
for(var i=0,l=_1609.length;i<l;i++){
_1609[i].call(self,name,_1606,value);
}
}
};
_1608(_1605["_"+name]);
if(!_1607){
_1608(_1605["*"]);
}
};
}
if(!_1604&&typeof name==="function"){
_1604=name;
name="*";
}else{
name="_"+name;
}
var _160a=_1605[name];
if(typeof _160a!=="object"){
_160a=_1605[name]=[];
}
_160a.push(_1604);
var _160b={};
_160b.unwatch=_160b.remove=function(){
var index=array.indexOf(_160a,_1604);
if(index>-1){
_160a.splice(index,1);
}
};
return _160b;
}});
});
},"curam/widget/ComboBox":function(){
define(["dijit/registry","dojo/_base/declare","dojo/on","dojo/dom","dojo/text!curam/widget/templates/ComboBox.html","dijit/form/ComboBox","curam/widget/ComboBoxMixin"],function(_160c,_160d,on,dom,_160e){
var _160f=_160d("curam.widget.ComboBox",[dijit.form.ComboBox,curam.widget.ComboBoxMixin],{templateString:_160e,enterKeyOnOpenDropDown:false,postCreate:function(){
on(this.focusNode,"keydown",function(e){
var _1610=_160c.byNode(dom.byId("widget_"+e.target.id));
if(e.keyCode==dojo.keys.ENTER&&_1610._opened){
_1610.enterKeyOnOpenDropDown=true;
}
});
this.inherited(arguments);
}});
return _160f;
});
},"dijit/layout/LayoutContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/_base/lang","../_WidgetBase","./_LayoutWidget","./utils"],function(array,_1611,_1612,_1613,lang,_1614,_1615,_1616){
var _1617=_1611("dijit.layout.LayoutContainer",_1615,{design:"headline",baseClass:"dijitLayoutContainer",startup:function(){
if(this._started){
return;
}
array.forEach(this.getChildren(),this._setupChild,this);
this.inherited(arguments);
},_setupChild:function(child){
this.inherited(arguments);
var _1618=child.region;
if(_1618){
_1612.add(child.domNode,this.baseClass+"Pane");
}
},_getOrderedChildren:function(){
var _1619=array.map(this.getChildren(),function(child,idx){
return {pane:child,weight:[child.region=="center"?Infinity:0,child.layoutPriority,(this.design=="sidebar"?1:-1)*(/top|bottom/.test(child.region)?1:-1),idx]};
},this);
_1619.sort(function(a,b){
var aw=a.weight,bw=b.weight;
for(var i=0;i<aw.length;i++){
if(aw[i]!=bw[i]){
return aw[i]-bw[i];
}
}
return 0;
});
return array.map(_1619,function(w){
return w.pane;
});
},layout:function(){
_1616.layoutChildren(this.domNode,this._contentBox,this._getOrderedChildren());
},addChild:function(child,_161a){
this.inherited(arguments);
if(this._started){
this.layout();
}
},removeChild:function(child){
this.inherited(arguments);
if(this._started){
this.layout();
}
_1612.remove(child.domNode,this.baseClass+"Pane");
_1613.set(child.domNode,{top:"auto",bottom:"auto",left:"auto",right:"auto",position:"static"});
_1613.set(child.domNode,/top|bottom/.test(child.region)?"width":"height","auto");
}});
_1617.ChildWidgetProperties={region:"",layoutAlign:"",layoutPriority:0};
lang.extend(_1614,_1617.ChildWidgetProperties);
return _1617;
});
},"dojo/dnd/autoscroll":function(){
define(["../_base/lang","../sniff","../_base/window","../dom-geometry","../dom-style","../window"],function(lang,has,win,_161b,_161c,_161d){
var _161e={};
lang.setObject("dojo.dnd.autoscroll",_161e);
_161e.getViewport=_161d.getBox;
_161e.V_TRIGGER_AUTOSCROLL=32;
_161e.H_TRIGGER_AUTOSCROLL=32;
_161e.V_AUTOSCROLL_VALUE=16;
_161e.H_AUTOSCROLL_VALUE=16;
var _161f,doc=win.doc,_1620=Infinity,_1621=Infinity;
_161e.autoScrollStart=function(d){
doc=d;
_161f=_161d.getBox(doc);
var html=win.body(doc).parentNode;
_1620=Math.max(html.scrollHeight-_161f.h,0);
_1621=Math.max(html.scrollWidth-_161f.w,0);
};
_161e.autoScroll=function(e){
var v=_161f||_161d.getBox(doc),html=win.body(doc).parentNode,dx=0,dy=0;
if(e.clientX<_161e.H_TRIGGER_AUTOSCROLL){
dx=-_161e.H_AUTOSCROLL_VALUE;
}else{
if(e.clientX>v.w-_161e.H_TRIGGER_AUTOSCROLL){
dx=Math.min(_161e.H_AUTOSCROLL_VALUE,_1621-html.scrollLeft);
}
}
if(e.clientY<_161e.V_TRIGGER_AUTOSCROLL){
dy=-_161e.V_AUTOSCROLL_VALUE;
}else{
if(e.clientY>v.h-_161e.V_TRIGGER_AUTOSCROLL){
dy=Math.min(_161e.V_AUTOSCROLL_VALUE,_1620-html.scrollTop);
}
}
window.scrollBy(dx,dy);
};
_161e._validNodes={"div":1,"p":1,"td":1};
_161e._validOverflow={"auto":1,"scroll":1};
_161e.autoScrollNodes=function(e){
var b,t,w,h,rx,ry,dx=0,dy=0,_1622,_1623;
for(var n=e.target;n;){
if(n.nodeType==1&&(n.tagName.toLowerCase() in _161e._validNodes)){
var s=_161c.getComputedStyle(n),_1624=(s.overflow.toLowerCase() in _161e._validOverflow),_1625=(s.overflowX.toLowerCase() in _161e._validOverflow),_1626=(s.overflowY.toLowerCase() in _161e._validOverflow);
if(_1624||_1625||_1626){
b=_161b.getContentBox(n,s);
t=_161b.position(n,true);
}
if(_1624||_1625){
w=Math.min(_161e.H_TRIGGER_AUTOSCROLL,b.w/2);
rx=e.pageX-t.x;
if(has("webkit")||has("opera")){
rx+=win.body().scrollLeft;
}
dx=0;
if(rx>0&&rx<b.w){
if(rx<w){
dx=-w;
}else{
if(rx>b.w-w){
dx=w;
}
}
_1622=n.scrollLeft;
n.scrollLeft=n.scrollLeft+dx;
}
}
if(_1624||_1626){
h=Math.min(_161e.V_TRIGGER_AUTOSCROLL,b.h/2);
ry=e.pageY-t.y;
if(has("webkit")||has("opera")){
ry+=win.body().scrollTop;
}
dy=0;
if(ry>0&&ry<b.h){
if(ry<h){
dy=-h;
}else{
if(ry>b.h-h){
dy=h;
}
}
_1623=n.scrollTop;
n.scrollTop=n.scrollTop+dy;
}
}
if(dx||dy){
return;
}
}
try{
n=n.parentNode;
}
catch(x){
n=null;
}
}
_161e.autoScroll(e);
};
return _161e;
});
},"curam/cdsl/_base/FacadeMethodCall":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/json","dojo/_base/array"],function(_1627,lang,json,array){
var _1628=_1627(null,{_intf:null,_method:null,_structs:null,_metadata:null,_options:null,constructor:function(intf,_1629,_162a,_162b){
if(_162a&&!lang.isArray(_162a)){
throw new Error("Unexpected type of the 'structs' argument.");
}
this._intf=intf;
this._method=_1629;
this._structs=_162a?_162a:[];
this._options={};
lang.mixin(this._options,{raw:true,formatted:false,sendCodetables:true,dataAdapter:null},_162b);
},url:function(base){
return base+"/"+this._intf+"/"+this._method;
},_setMetadata:function(_162c){
this._metadata=_162c;
},toJson:function(){
var data={service:this._intf,method:this._method,data:array.map(this._structs,lang.hitch(this,function(item){
item.setDataAdapter(this._options.dataAdapter);
return item.getData();
})),configOptions:{"response-type":this._responseType(),"send-codetables":this._sendCodetables()}};
if(this._metadata&&this._metadata.queryOptions){
data.queryOptions=this._metadata.queryOptions;
}
return json.stringify(data);
},formatted:function(_162d){
return this._getOrSet(_162d,this._options,"formatted");
},raw:function(_162e){
return this._getOrSet(_162e,this._options,"raw");
},_responseType:function(){
if(this.raw()&&this.formatted()){
return "both";
}else{
if(this.raw()){
return "raw";
}else{
if(this.formatted()){
return "formatted";
}
}
}
throw new Error("Invalid response type: neither raw nor formatted was requested.");
},_sendCodetables:function(_162f){
return this._getOrSet(_162f,this._options,"sendCodetables");
},_getOrSet:function(value,_1630,_1631){
if(typeof value==="undefined"){
return _1630[_1631];
}else{
var _1632=_1630[_1631];
_1630[_1631]=value;
return _1632;
}
},intf:function(){
return this._intf;
},method:function(){
return this._method;
},dataAdapter:function(_1633){
if(!_1633){
return this._options.dataAdapter;
}
this._options.dataAdapter=_1633;
}});
return _1628;
});
},"curam/util/ScreenContext":function(){
define(["dojo/_base/declare"],function(_1634){
var _1635={DEFAULT_CONTEXT:112,SAMPLE22:2,SAMPLE21:1,SAMPLE13:4,SAMPLE12:2,SAMPLE11:1,EXTAPP:1048576,SMART_PANEL:262144,NESTED_UIM:131072,ORG_TREE:65536,CONTEXT_PANEL:32768,LIST_ROW_INLINE_PAGE:8192,LIST_EVEN_ROW:16384,TAB:4096,TREE:2048,AGENDA:1024,POPUP:512,MODAL:256,HOME:128,HEADER:64,NAVIGATOR:32,FOOTER:16,OVAL:8,RESOLVE:4,ACTION:2,ERROR:1,EMPTY:0};
var _1636=[["ERROR","ACTION","RESOLVE","OPT_VALIDATION","FOOTER","NAVIGATOR","HEADER","HOME_PAGE","MODAL","POPUP","AGENDA","TREE","TAB","LIST_EVEN_ROW","LIST_ROW_INLINE_PAGE","CONTEXT_PANEL","ORG_TREE","NESTED_UIM","SMART_PANEL","EXTAPP"],["SAMPLE11","SAMPLE12","SAMPLE13"],["SAMPLE21","SAMPLE22"]];
var _1637=_1634("curam.util.ScreenContext",null,{constructor:function(_1638){
if(_1638){
this.setContext(_1638);
}else{
this.currentContext=[_1635["DEFAULT_CONTEXT"]|_1635["DEFAULT_CONTEXT"]];
}
},setContext:function(_1639){
var tmp=this.setup(_1639);
this.currentContext=((tmp==null)?([_1635["DEFAULT_CONTEXT"]|_1635["DEFAULT_CONTEXT"]]):(tmp));
},addContextBits:function(_163a,idx){
if(!_163a){
return;
}
var navig=(idx)?idx:0;
var _163b=this.parseContext(_163a);
if(_163b!=null){
this.currentContext[navig]|=_163b;
}
return this.currentContext[navig];
},addAll:function(idx){
var navig=(idx)?idx:0;
this.currentContext[navig]=4294967295;
return this.currentContext[navig];
},clear:function(_163c,idx){
if(!_163c){
this.clearAll();
return;
}
var navig=(idx)?idx:0;
if(_163c==0){
return this.currentContext[navig];
}
var _163d=this.parseContext(_163c);
if(_163d!=null){
var _163e=this.currentContext[navig]&_163d;
this.currentContext[navig]^=_163e;
}
return this.currentContext[navig];
},clearAll:function(idx){
if(idx){
this.currentContext[idx]=0;
}else{
for(var i=0;i<this.currentContext.length;i++){
this.currentContext[i]=0;
}
}
},updateStates:function(_163f){
this.clear("ERROR|ACTION|RESOLVE");
this.currentContext[0]=this.currentContext[0]|(_163f&7);
},hasContextBits:function(_1640,idx){
if(!_1640){
return false;
}
var navig=(idx)?idx:0;
var _1641=this.parseContext(_1640);
if(_1641!=null){
var merge=this.currentContext[navig]&_1641;
return (merge==_1641);
}
return false;
},getValue:function(){
var _1642="";
for(var i=0;i<this.currentContext.length;i++){
_1642+=this.currentContext[i]+"|";
}
return _1642.substring(0,_1642.length-1);
},toRequestString:function(){
return "o3ctx="+this.getValue();
},toBinary:function(){
var _1643="";
for(var i=0;i<this.currentContext.length;i++){
_1643+=this.currentContext[i].toString(2)+"|";
}
return _1643.substring(0,_1643.length-1);
},toString:function(){
var _1644="";
for(var i=0;i<this.currentContext.length;i++){
var _1645="";
var j=0;
while(j<_1636[i].length){
if(((this.currentContext[i]>>j)&1)!=0){
_1645+=","+_1636[i][j];
}
j++;
}
if(_1645==""){
return "{}";
}
_1644+="|"+_1645.replace(",","{")+((_1645.length==0)?"":"}");
}
return _1644.substring(1);
},parseContext:function(_1646){
var _1647=_1646.replace(/,/g,"|");
var parts=_1647.split("|");
var tmp=isNaN(parts[0])?parseInt(_1635[parts[0]]):parts[0];
for(var i=1;i<parts.length;i++){
tmp=tmp|(isNaN(parts[i])?parseInt(_1635[parts[i]]):parts[i]);
}
return (isNaN(tmp)?null:tmp);
},setup:function(_1648){
if(!_1648){
return null;
}
var _1649=(""+_1648).split("|");
var _164a=new Array(_1649.length);
for(var i=0;i<_1649.length;i++){
_164a[i]=this.parseContext(_1649[_1649.length-i-1]);
_164a[i]=_164a[i]|_164a[i];
if(!_164a[i]||isNaN(_164a[i])||_164a[i]>4294967295){
return null;
}
}
return _164a;
}});
return _1637;
});
},"dijit/layout/utils":function(){
define(["dojo/_base/array","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/_base/lang"],function(array,_164b,_164c,_164d,lang){
function _164e(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
};
function size(_164f,dim){
var _1650=_164f.resize?_164f.resize(dim):_164c.setMarginBox(_164f.domNode,dim);
if(_164f.fakeWidget){
return;
}
if(_1650){
lang.mixin(_164f,_1650);
}else{
lang.mixin(_164f,_164c.getMarginBoxSimple(_164f.domNode));
}
};
var utils={marginBox2contentBox:function(node,mb){
var cs=_164d.getComputedStyle(node);
var me=_164c.getMarginExtents(node,cs);
var pb=_164c.getPadBorderExtents(node,cs);
return {l:_164d.toPixelValue(node,cs.paddingLeft),t:_164d.toPixelValue(node,cs.paddingTop),w:mb.w-(me.w+pb.w),h:mb.h-(me.h+pb.h)};
},layoutChildren:function(_1651,dim,_1652,_1653,_1654){
dim=lang.mixin({},dim);
_164b.add(_1651,"dijitLayoutContainer");
_1652=array.filter(_1652,function(item){
return item.region!="center"&&item.layoutAlign!="client";
}).concat(array.filter(_1652,function(item){
return item.region=="center"||item.layoutAlign=="client";
}));
var _1655={};
array.forEach(_1652,function(child){
var elm=child.domNode,pos=(child.region||child.layoutAlign);
if(!pos){
throw new Error("No region setting for "+child.id);
}
var _1656=elm.style;
_1656.left=dim.l+"px";
_1656.top=dim.t+"px";
_1656.position="absolute";
_164b.add(elm,"dijitAlign"+_164e(pos));
var _1657={};
if(_1653&&_1653==child.id){
_1657[child.region=="top"||child.region=="bottom"?"h":"w"]=_1654;
}
if(pos=="leading"){
pos=child.isLeftToRight()?"left":"right";
}
if(pos=="trailing"){
pos=child.isLeftToRight()?"right":"left";
}
if(pos=="top"||pos=="bottom"){
_1657.w=dim.w;
size(child,_1657);
dim.h-=child.h;
if(pos=="top"){
dim.t+=child.h;
}else{
_1656.top=dim.t+dim.h+"px";
}
}else{
if(pos=="left"||pos=="right"){
_1657.h=dim.h;
size(child,_1657);
if(child.isSplitter){
_1656.left=dim.l-child.w+"px";
dim.w+=child.w;
}else{
dim.w-=child.w;
}
if(pos=="left"){
if(!child.isSplitter){
dim.l+=child.w;
}
}else{
if(child.isSplitter){
_1656.left=dim.l+dim.w-child.w+"px";
dim.l+=child.w;
}else{
_1656.left=dim.l+dim.w+"px";
}
}
}else{
if(pos=="client"||pos=="center"){
size(child,dim);
}
}
}
_1655[pos]={w:dim.w,h:dim.h};
});
return _1655;
}};
lang.setObject("dijit.layout.utils",utils);
return utils;
});
},"curam/util/FrequencyEditor":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","dojo/query","cm/_base/_dom","curam/util","curam/define","curam/debug","dojo/has","dojo/sniff"],function(dom,_1658,_1659,_165a,_165b,query,cm,util,_165c,debug,has){
_165c.singleton("curam.util.FrequencyEditor",{CORRECTOR:1,DAILY_FREQUENCY:0,WEEKLY_FREQUENCY:1,MONTHLY_FREQUENCY:2,YEARLY_FREQUENCY:3,BIMONTHLY_FREQUENCY:4,EVERY_DAY_MASK:201,EVERY_WEEKDAY_MASK:202,EVERY_WEEKENDDAY_MASK:203,MON_MASK:1,TUE_MASK:2,WED_MASK:4,THU_MASK:8,FRI_MASK:16,SAT_MASK:32,SUN_MASK:64,daysInMonth:[31,28,31,30,31,30,31,31,30,31,30,31],EVERY_DAY:0,EVERY_WEEKDAY:1,MON:0,TUE:1,WED:2,THU:3,FRI:4,SAT:5,SUN:6,START_DATE:0,MONTH_DAY_NUM:1,MONTH_SEL_DAY:2,DAY_NUM:0,SEL_DAY:1,SEL_MONTH_DAY_NUM:0,SEL_MONTH_SEL_DAY:1,domSelector:(has("ios")||has("android")?dojo:dijit),allowableCharsForNumeric:["1","2","3","4","5","6","7","8","9","0",dojo.keys.LEFT_ARROW,dojo.keys.RIGHT_ARROW,dojo.keys.DELETE,dojo.keys.ENTER,dojo.keys.BACKSPACE,dojo.keys.END,dojo.keys.HOME,dojo.keys.TAB,dojo.keys.F5],allowableDayString:["32","33","34","35","36"],allowableDayOfWeekMask:["201","202","203","1","2","4","8","16","32","64"],allowableFirstDayStringForBimonthly:["32","33","34","35"],allowableSecondDayStringForBimonthly:["33","34","35","36"],allowableWeekdayStringForBimonthly:["1","2","4","8","16","32","64"],allowableMonthString:["1","2","3","4","5","6","7","8","9","10","11","12"],initPage:function(){
var _165d=curam.dialog.getParentWindow(window);
if(formActivated==true){
executeOpenerMapping("freq_text",translatedPatternString);
executeOpenerMapping("freq_data",patternString);
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
curam.dialog.closeModalDialog();
return false;
}
var freq=_165d.getPopupInput("initFreq");
curam.debug.log(debug.getProperty("curam.util.FrequencyEditor.input"),freq);
if(!freq||freq==null||freq.length==0){
document.theForm.freqType[0].checked=true;
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
return true;
}
var _165e=parseInt(freq.charAt(0),10);
if(_165e==curam.util.FrequencyEditor.DAILY_FREQUENCY){
curam.util.FrequencyEditor.setupDailyFrequency(freq);
}else{
if(_165e==curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
curam.util.FrequencyEditor.setupWeeklyFrequency(freq);
}else{
if(_165e==curam.util.FrequencyEditor.MONTHLY_FREQUENCY){
curam.util.FrequencyEditor.setupMonthlyFrequency(freq);
}else{
if(_165e==curam.util.FrequencyEditor.YEARLY_FREQUENCY){
curam.util.FrequencyEditor.setupYearlyFrequency(freq);
}else{
if(_165e==curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY){
curam.util.FrequencyEditor.setupBimonthlyFrequency(freq);
}else{
alert(errorMsgs.freqPattern);
}
}
}
}
}
return true;
},setupDailyFrequency:function(_165f){
var _1660=_165f.substr(4,3);
document.theForm.freqType[curam.util.FrequencyEditor.DAILY_FREQUENCY].checked=true;
if(parseInt(_1660,10)==curam.util.FrequencyEditor.EVERY_WEEKDAY_MASK){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_WEEKDAY].checked=true;
}else{
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
var _1661=parseInt(_165f.substr(1,3),10);
document.theForm.daily_num.value=""+_1661;
}
},setupWeeklyFrequency:function(_1662){
var _1663=parseInt(_1662.substr(4,3),10);
document.theForm.freqType[curam.util.FrequencyEditor.WEEKLY_FREQUENCY].checked=true;
if(_1663&curam.util.FrequencyEditor.MON_MASK){
document.theForm.weekly_select_mon.checked=true;
}
if(_1663&curam.util.FrequencyEditor.TUE_MASK){
document.theForm.weekly_select_tue.checked=true;
}
if(_1663&curam.util.FrequencyEditor.WED_MASK){
document.theForm.weekly_select_wed.checked=true;
}
if(_1663&curam.util.FrequencyEditor.THU_MASK){
document.theForm.weekly_select_thur.checked=true;
}
if(_1663&curam.util.FrequencyEditor.FRI_MASK){
document.theForm.weekly_select_fri.checked=true;
}
if(_1663&curam.util.FrequencyEditor.SAT_MASK){
document.theForm.weekly_select_sat.checked=true;
}
if(_1663&curam.util.FrequencyEditor.SUN_MASK){
document.theForm.weekly_select_sun.checked=true;
}
var _1664=parseInt(_1662.substr(1,3),10);
document.theForm.weekly_num.value=""+_1664;
},setupMonthlyFrequency:function(_1665){
var _1666=parseInt(_1665.substr(1,3),10);
var _1667=parseInt(_1665.substr(4,3),10);
var _1668=parseInt(_1665.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.MONTHLY_FREQUENCY].checked=true;
if(_1668==0){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.START_DATE].checked=true;
document.theForm.monthly0_month_interval.value=_1666;
}else{
if(_1668<=31){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_DAY_NUM].checked=true;
document.theForm.monthly1_day_num.value=_1668;
document.theForm.monthly1_month_interval.value=_1666;
}else{
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_SEL_DAY].checked=true;
var _1669;
if(has("ios")||has("android")){
_1669=dojo.byId("monthly2_select_day_num");
_1669.value=_1668;
_1669=dojo.byId("monthly2_select_day");
_1669.value=_1667;
}else{
_1669=dijit.byId("monthly2_select_day_num");
_1669.set("value",_1668);
_1669=dijit.byId("monthly_select_day");
_1669.set("value",_1667);
}
document.theForm.monthly2_month_interval.value=_1666;
}
}
},setupBimonthlyFrequency:function(_166a){
var _166b=parseInt(_166a.substr(1,2),10);
var _166c=parseInt(_166a.substr(4,3),10);
var _166d=parseInt(_166a.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY-curam.util.FrequencyEditor.CORRECTOR].checked=true;
if(_166d<=31){
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
document.theForm.bimonthly1_day1_num.value=_166d;
document.theForm.bimonthly1_day2_num.value=_166b;
}else{
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=true;
var _166e;
if(has("ios")||has("android")){
_166e=dojo.byId("bimonthly2_select_day1_num");
_166e.value=_166d;
_166e=dojo.byId("bimonthly2_select_day2_num");
_166e.value=_166b;
_166e=dojo.byId("bimonthly2_select_weekday");
_166e.value=_166c;
}else{
_166e=dijit.byId("bimonthly2_select_day1_num");
_166e.set("value",_166d);
_166e=dijit.byId("bimonthly2_select_day2_num");
_166e.set("value",_166b);
_166e=dijit.byId("bimonthly2_select_weekday");
_166e.set("value",_166c);
}
}
},setupYearlyFrequency:function(_166f){
var _1670=parseInt(_166f.substr(1,3),10);
var _1671=parseInt(_166f.substr(4,3),10);
var _1672=parseInt(_166f.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.YEARLY_FREQUENCY+curam.util.FrequencyEditor.CORRECTOR].checked=true;
if(_1672<=31){
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=true;
var _1673;
if(has("ios")||has("android")){
_1673=dojo.byId("yearly1_select_month");
_1673.value=_1670;
}else{
_1673=dijit.byId("yearly1_select_month");
_1673.set("value",_1670);
}
document.theForm.yearly1_day_num.value=_1672;
}else{
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_SEL_DAY].checked=true;
var _1673;
if(has("ios")||has("android")){
_1673=dojo.byId("yearly2_select_day_num");
_1673.value=_1672;
_1673=dojo.byId("yearly2_select_day");
_1673.value=_1671;
_1673=dojo.byId("yearly2_select_month");
_1673.value=_1670;
}else{
_1673=dijit.byId("yearly2_select_day_num");
_1673.set("value",_1672);
_1673=dijit.byId("yearly2_select_day");
_1673.set("value",_1671);
_1673=dijit.byId("yearly2_select_month");
_1673.set("value",_1670);
}
}
},createPatternString:function(){
var _1674=null;
var _1675=false;
if(document.theForm.freqType[0].checked==true){
_1675=curam.util.FrequencyEditor.createDailyPatternString();
}else{
if(document.theForm.freqType[1].checked==true){
_1675=curam.util.FrequencyEditor.createWeeklyPatternString();
}else{
if(document.theForm.freqType[2].checked==true){
_1675=curam.util.FrequencyEditor.createMonthlyPatternString();
}else{
if(document.theForm.freqType[3].checked==true){
_1675=curam.util.FrequencyEditor.createBimonthlyPatternString();
}else{
_1675=curam.util.FrequencyEditor.createYearlyPatternString();
}
}
}
}
if(_1675){
curam.util.FrequencyEditor.disableRowBorder();
return true;
}else{
return false;
}
},createDailyPatternString:function(){
var _1676="0";
if(document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked==true){
var _1677=parseInt(document.theForm.daily_num.value,10);
if(curam.util.FrequencyEditor.validateDailyPattern(_1677)){
_1676+=curam.util.FrequencyEditor.doZeroPadding(_1677,3);
_1676+="000";
}else{
return false;
}
}else{
_1676+="001";
_1676+=curam.util.FrequencyEditor.EVERY_WEEKDAY_MASK;
}
_1676+="00";
document.theForm.patternString.value=_1676;
return true;
},validateDailyPattern:function(_1678){
if(isNaN(_1678)||_1678<1){
alert(errorMsgs.everyDay);
return false;
}
return true;
},createWeeklyPatternString:function(){
var _1679="1";
var _167a=0;
var _167b=parseInt(document.theForm.weekly_num.value,10);
if(curam.util.FrequencyEditor.validateWeeklyPattern(_167b)){
_1679+=curam.util.FrequencyEditor.doZeroPadding(_167b,3);
var _167c=false;
var _167d=document.theForm.weekly_select_mon;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_tue;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_wed;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_thur;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_fri;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_sat;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
_167d=document.theForm.weekly_select_sun;
if(_167d.checked==true){
_167c=true;
_167a+=_167d.value-0;
}
if(!_167c){
alert(errorMsgs.noDaySelected);
return false;
}
if(_167a>0){
_1679+=curam.util.FrequencyEditor.doZeroPadding(_167a,3);
}else{
_1679+="000";
}
_1679+="00";
document.theForm.patternString.value=_1679;
return true;
}
return false;
},validateWeeklyPattern:function(_167e){
if(isNaN(_167e)||_167e<1){
alert(errorMsgs.everyWeek);
return false;
}
return true;
},createMonthlyPatternString:function(){
var _167f="2";
if(document.theForm.monthlyFreqType[curam.util.FrequencyEditor.START_DATE].checked==true){
var _1680=parseInt(document.theForm.monthly0_month_interval.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_1680)){
return false;
}
var _1681=0;
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1680,3);
_167f+="000";
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1681,2);
}else{
if(document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_DAY_NUM].checked==true){
var _1680=parseInt(document.theForm.monthly1_month_interval.value,10);
var _1681=parseInt(document.theForm.monthly1_day_num.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_1680,_1681)){
return false;
}
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1680,3);
_167f+="000";
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1681,2);
}else{
var _1680=parseInt(document.theForm.monthly2_month_interval.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_1680)){
return false;
}
var day=curam.util.FrequencyEditor.domSelector.byId("monthly2_select_day_num").value;
var _1682=curam.util.FrequencyEditor.domSelector.byId("monthly2_select_day").value;
if(!curam.util.FrequencyEditor.validateDayWeekString(day,_1682,_167f)){
return false;
}
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1680,3);
_167f+=curam.util.FrequencyEditor.doZeroPadding(_1682,3);
_167f+=curam.util.FrequencyEditor.doZeroPadding(day,2);
}
}
document.theForm.patternString.value=_167f;
return true;
},validateMonthlyData:function(_1683,_1684){
if(isNaN(_1683)||_1683<1||_1683>100){
alert(errorMsgs.monthNum);
return false;
}
if(_1684==null){
return true;
}
if(isNaN(_1684)||_1684<1||_1684>28){
alert(errorMsgs.dayNum);
return false;
}
return true;
},validateDayWeekString:function(day,_1685,_1686){
var days=curam.util.FrequencyEditor.allowableDayString;
var _1687=curam.util.FrequencyEditor.allowableDayOfWeekMask;
var _1688=false;
var _1689=false;
for(var i=0;i<days.length;i++){
if(day==days[i]){
_1688=true;
break;
}
}
for(var i=0;i<_1687.length;i++){
if(_1685==_1687[i]){
_1689=true;
break;
}
}
if(_1688&&_1689){
return true;
}else{
if(!_1688){
if(_1686=="2"){
alert(errorMsgs.dayStringForMonthly);
}else{
if(_1686=="3"){
alert(errorMsgs.dayStringForYearly);
}else{
alert(errorMsgs.dayString);
}
}
return false;
}else{
if(!_1689){
if(_1686=="2"){
alert(errorMsgs.dayOfWeekMaskForMonthly);
}else{
if(_1686=="3"){
alert(errorMsgs.dayOfWeekMaskForYearly);
}else{
alert(errorMsgs.dayOfWeekMask);
}
}
return false;
}
}
}
},createBimonthlyPatternString:function(){
var _168a="4";
var _168b;
if(document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked==true){
var _168c=parseInt(document.theForm.bimonthly1_day1_num.value,10);
var _168d=parseInt(document.theForm.bimonthly1_day2_num.value,10);
if(!curam.util.FrequencyEditor.validateBimonthlyData(_168c,_168d,null)){
return false;
}
if(_168c>_168d){
_168b=_168c;
_168c=_168d;
_168d=_168b;
}
_168a+=curam.util.FrequencyEditor.doZeroPadding(_168d,2);
_168a+="0000";
_168a+=curam.util.FrequencyEditor.doZeroPadding(_168c,2);
}else{
var _168e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_day1_num");
var _168f=_168e.value;
_168e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_day2_num");
var _1690=_168e.value;
_168e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_weekday");
var _1691=_168e.value;
if(!curam.util.FrequencyEditor.validateBimonthlyDataString(_168f,_1690,_1691)){
return false;
}
if(_168f>_1690){
_168b=_168f;
_168f=_1690;
_1690=_168b;
}
if(!curam.util.FrequencyEditor.validateBimonthlyData(_168f,_1690,_1691)){
return false;
}
_168a+=curam.util.FrequencyEditor.doZeroPadding(_1690,2);
_168a+="0";
_168a+=curam.util.FrequencyEditor.doZeroPadding(_1691,3);
_168a+=curam.util.FrequencyEditor.doZeroPadding(_168f,2);
}
document.theForm.patternString.value=_168a;
return true;
},validateBimonthlyData:function(first,_1692,_1693){
if(_1693!=null){
if(isNaN(_1693)||_1693<1||_1693>64){
alert(errorMsgs.weekend);
return false;
}
}else{
if(isNaN(first)||first<1||first>28||isNaN(_1692)||_1692<1||_1692>28){
alert(errorMsgs.dayNum);
return false;
}
}
if(first==_1692){
alert(errorMsgs.dayDiff);
return false;
}
return true;
},validateBimonthlyDataString:function(_1694,_1695,_1696){
var _1697=curam.util.FrequencyEditor.allowableFirstDayStringForBimonthly;
var _1698=curam.util.FrequencyEditor.allowableSecondDayStringForBimonthly;
var _1699=curam.util.FrequencyEditor.allowableWeekdayStringForBimonthly;
var _169a=false;
var _169b=false;
var _169c=false;
for(var i=0;i<_1697.length;i++){
if(_1694==_1697[i]){
_169a=true;
break;
}
}
for(var i=0;i<_1698.length;i++){
if(_1695==_1698[i]){
_169b=true;
break;
}
}
for(var i=0;i<_1699.length;i++){
if(_1696==_1699[i]){
_169c=true;
break;
}
}
if(_169a&&_169b&&_169c){
return true;
}else{
if(!_169a){
alert(errorMsgs.firstDayString);
return false;
}else{
if(!_169b){
alert(errorMsgs.secondDayString);
return false;
}else{
if(!_169c){
alert(errorMsgs.weekend);
return false;
}
}
}
}
},createYearlyPatternString:function(){
var _169d="3";
var _169e=null;
if(document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked==true){
_169e=curam.util.FrequencyEditor.domSelector.byId("yearly1_select_month");
var _169f=_169e.value;
_169d+=curam.util.FrequencyEditor.doZeroPadding(_169f,3);
_169d+="000";
if(!curam.util.FrequencyEditor.validateMonthString(_169f)){
return false;
}
var _16a0=parseInt(document.theForm.yearly1_day_num.value,10);
if(!curam.util.FrequencyEditor.validateYearlyData(_16a0,_169f)){
return false;
}
_169d+=curam.util.FrequencyEditor.doZeroPadding(_16a0,2);
}else{
var day=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_day_num").value;
var _16a1=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_day").value;
var month=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_month").value;
if(!curam.util.FrequencyEditor.validateDayWeekString(day,_16a1,_169d)){
return false;
}
if(!curam.util.FrequencyEditor.validateMonthString(month)){
return false;
}
_169d+=curam.util.FrequencyEditor.doZeroPadding(month,3);
_169d+=curam.util.FrequencyEditor.doZeroPadding(_16a1,3);
_169d+=curam.util.FrequencyEditor.doZeroPadding(day,2);
}
document.theForm.patternString.value=_169d;
return true;
},validateYearlyData:function(_16a2,_16a3){
if(isNaN(_16a2)||_16a2<1||_16a2>curam.util.FrequencyEditor.daysInMonth[_16a3-1]){
alert(errorMsgs.dayNumAnd+"  "+curam.util.FrequencyEditor.daysInMonth[_16a3-1]);
return false;
}
return true;
},validateMonthString:function(month){
var _16a4=curam.util.FrequencyEditor.allowableMonthString;
for(var i=0;i<_16a4.length;i++){
if(month==_16a4[i]){
return true;
}
}
alert(errorMsgs.monthString);
return false;
},doZeroPadding:function(_16a5,_16a6){
var _16a7=""+_16a5;
var _16a8=_16a6-_16a7.length;
for(var i=0;i<_16a8;i++){
_16a7="0"+_16a7;
}
return _16a7;
},_setFirstLevelRadioButton:function(_16a9){
var _16aa=query("input[name='freqType']",dom.byId("mainForm"))[_16a9];
if(_16aa==null){
throw new Error("The radio button for the selected"+" frequency type could not be found!");
}
if(!_16aa.checked){
query("input[type='radio']:checked",dom.byId("mainForm")).forEach(function(_16ab){
_16ab.checked=false;
});
if(_16a9!=curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
query("input[type='checkbox']:checked",dom.byId("mainForm")).forEach(function(_16ac){
_16ac.checked=false;
});
}
_16aa.checked=true;
}
},_setSecondLevelRadioButton:function(_16ad){
if(_16ad==undefined){
return "undefined";
}
var _16ae;
if(_16ad.domNode){
_16ae=_16ad.domNode;
}else{
_16ae=_16ad;
}
if(_16ae.tagName.toLowerCase()=="input"&&_165a.get(_16ae,"type")=="radio"){
_16ae.checked=true;
return "radio node clicked";
}
var _16af=cm.getParentByType(_16ae,"TD");
if(_16af==null){
throw new Error("Exception: The row contains the node should be found");
}
var _16b0=query("input[type = 'radio']",_16af)[0];
if(_16b0==null){
throw new Error("Exception: The radio node should exist");
}else{
_16b0.checked=true;
return "text input or codetable clicked";
}
},setSelectedFreqType:function(_16b1,_16b2){
debug.log("curam.util.FrequencyEditor: "+debug.getProperty("curam.util.FrequencyEditor.radio"));
curam.util.FrequencyEditor._setFirstLevelRadioButton(_16b1);
curam.util.FrequencyEditor._setSecondLevelRadioButton(_16b2);
},setDefaultOption:function(_16b3){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=false;
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_WEEKDAY].checked=false;
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=false;
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=false;
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=false;
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=false;
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=false;
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_SEL_DAY].checked=false;
if(_16b3!=curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
document.theForm.weekly_select_mon.checked=false;
document.theForm.weekly_select_tue.checked=false;
document.theForm.weekly_select_wed.checked=false;
document.theForm.weekly_select_thur.checked=false;
document.theForm.weekly_select_fri.checked=false;
document.theForm.weekly_select_sat.checked=false;
document.theForm.weekly_select_sun.checked=false;
}
if(_16b3==curam.util.FrequencyEditor.DAILY_FREQUENCY){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
}else{
if(_16b3==curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
document.theForm.weekly_select_mon.checked=true;
}else{
if(_16b3==curam.util.FrequencyEditor.MONTHLY_FREQUENCY){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
}else{
if(_16b3==curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY){
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
}else{
if(_16b3==curam.util.FrequencyEditor.YEARLY_FREQUENCY){
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=true;
}
}
}
}
}
},_doPosNumbericInputChecker:function(_16b4){
if(_16b4==""){
return false;
}
var chars=curam.util.FrequencyEditor.allowableCharsForNumeric;
for(var i=0;i<chars.length;i++){
if(_16b4==chars[i]){
return true;
}
}
return false;
},posNumericInputChecker:function(event){
event=dojo.fixEvent(event);
var _16b5=event.keyChar;
var _16b6=curam.util.FrequencyEditor._doPosNumbericInputChecker(_16b5);
if(!_16b6){
dojo.stopEvent(event);
}
},prePopulateTextFields:function(_16b7){
return function(e){
for(var i=0;i<_16b7.length;i++){
if(!_16b7[i].value||_16b7[i].value==""){
_16b7[i].value=1;
}
}
};
},disableRowBorder:function(){
query("form[name='theForm'] table tr").forEach(function(node){
_1659.add(node,"row-no-border");
});
},addInputListener:function(){
dojo.ready(function(){
var _16b8=[];
query("input[type='text']:not(input.dijitReset)").forEach(function(input){
_16b8.push(input);
curam.util.connect(input,"onkeypress",curam.util.FrequencyEditor.posNumericInputChecker);
});
curam.util.connect(dom.byId("mainForm"),"onsubmit",function(event){
curam.util.FrequencyEditor.prePopulateTextFields(_16b8);
});
});
},replacePlaceholderWithDomNode:function(){
query("body#Curam_frequency-editor table tr td.frequency").forEach(function(_16b9){
curam.util.FrequencyEditor._parse(_16b9);
});
},_parse:function(node){
var _16ba=query("> .node-needs-replacement",node);
var _16bb=query("> span",node)[0];
if(_16bb==null||_16bb==undefined){
throw new Error("Exception: Some text string is missing for some certain "+"frequency type, please check the 'frequency-editor.jsp' file.");
}
var _16bc=_16bb.innerHTML;
var _16bd=/%[^%]*%/g;
var _16be=_16bc.match(_16bd);
if(_16ba.length==0&&_16be==null){
return "No need to parse";
}else{
if(_16ba.length==0&&_16be!=null){
throw new Error("The text string '"+_16bc+"' from the 'FrequencyPatternSelector.properties'"+" should not have any placeholder.");
}else{
if(_16ba.length!=0&&_16be==null){
throw new Error("The text string '"+_16bc+"' from the 'FrequencyPatternSelector.properties'"+" should have some placeholders.");
}
}
}
if(_1659.contains(node,"weekly-frequency")){
if(_16be.length!=2){
throw new Error("The text string '"+_16bc+"' from the 'FrequencyPatternSelector.properties' "+"has the incorrect number of placeholders.");
}
var _16bf=dojo.clone(_16ba[0]);
_16ba.forEach(function(_16c0){
_165b.destroy(_16c0);
});
_1659.remove(_16bf,"node-needs-replacement");
var _16c1=_16bf.className.match(_16bd);
var _16c2;
for(var i=0;i<_16be.length;i++){
if(_16be[i]!=_16c1){
_16c2=_16be[i];
break;
}
}
var _16c3=_16bc.split(_16c2);
var _16c4=_16c3[0];
var _16c5=_16c3[1];
var _16c6;
if(_16c4.indexOf(_16c1)!=-1){
_16c6=true;
_16c4=_16c4.replace(_16c1,"<span class='"+_16c1+"'>placeholder</span>");
}else{
_16c6=false;
_16c5=_16c5.replace(_16c1,"<span class='"+_16c1+"'>placeholder</span>");
}
if(_16c5==""){
_16bb.innerHTML=_16c4;
_165b.place(_16bf,query("span."+_16c1,_16bb)[0],"replace");
}else{
_16bb.innerHTML=_16c4;
var _16c7=node.parentNode.nextSibling.nextSibling;
var _16c8=_165b.create("tr",{"class":"blue"});
var _16c9=_165b.create("td",{"class":"bottom"},_16c8);
_16c9.colSpan="4";
_1658.set(_16c9,"paddingLeft","20px");
var _16ca=_165b.create("span",{innerHTML:_16c5},_16c9);
_165b.place(_16c8,_16c7,"after");
if(_16c6){
_165b.place(_16bf,query("span."+_16c1,_16bb)[0],"replace");
}else{
_165b.place(_16bf,query("span[class='"+_16c1+"'",_16ca)[0],"replace");
}
query("td.day",_16c7).forEach(function(_16cb){
_1659.remove(_16cb,"bottom");
});
if(_16c4==""){
_1659.remove(node,"top");
}
query("th.type",node.parentNode)[0].rowSpan="4";
}
return "Parsed Successfully";
}
if(_16ba.length!=_16be.length){
throw new Error("The text string '"+_16bc+"' from the 'FrequencyPatternSelector.properties' "+"has the incorrect number of placeholders.");
}
var _16cc=dojo.clone(_16ba);
_16ba.forEach(_165b.destroy);
for(i=0;i<_16be.length;i++){
var _16cd=_16be[i];
_16bc=_16bc.replace(_16cd,"<span class='"+_16cd+"'>placeholder</span>");
}
_16bb.innerHTML=_16bc;
_16cc.forEach(function(_16ce,i){
_1659.remove(_16ce,"node-needs-replacement");
var _16cf=_16ce.className.match(_16bd);
_165b.place(_16ce,query("span."+_16cf,node)[0],"replace");
});
return "Parsed Successfully";
}});
return curam.util.FrequencyEditor;
});
},"curam/charting":function(){
define(["dojo/dom-class","dojo/dom","dojo/ready","cm/_base/_dom","curam/define"],function(_16d0,dom,ready,cmDom,_16d1){
_16d1.singleton("curam.charting",{alignChartWrapper:function(node){
ready(function(){
node=cmDom.getParentByClass(dom.byId(node),"cluster");
if(node){
_16d0.add(node,"chart-panel");
}
});
}});
return curam.charting;
});
},"curam/cdsl/_base/MetadataRegistry":function(){
define(["dojo/_base/declare","curam/cdsl/types/codetable/CodeTables","curam/cdsl/types/codetable/CodeTable","dojo/_base/lang"],function(_16d2,_16d3,_16d4,lang){
var _16d5=_16d2(null,{_callEntries:null,_codetables:null,constructor:function(){
this._callEntries={};
this._codetables={};
},setFlags:function(_16d6){
var _16d7=_16d6.intf()+"."+_16d6.method(),_16d8=!this._callEntries[_16d7];
_16d6._sendCodetables(_16d8);
},update:function(_16d9){
var _16da=_16d9.request(),_16db=_16da.intf()+"."+_16da.method(),_16dc=this._callEntries[_16db];
if(!_16dc){
_16dc={};
this._callEntries[_16db]=_16dc;
}
if(_16d9.hasCodetables()){
var data=_16d9.getCodetablesData();
for(var i=0;i<data.length;i++){
this._codetables[data[i].name]=new _16d4(data[i].name,data[i].defaultCode,data[i].codes);
}
}
},codetables:function(){
return this._codetables;
}});
return _16d5;
});
},"dijit/_FocusMixin":function(){
define(["./focus","./_WidgetBase","dojo/_base/declare","dojo/_base/lang"],function(focus,_16dd,_16de,lang){
lang.extend(_16dd,{focused:false,onFocus:function(){
},onBlur:function(){
},_onFocus:function(){
this.onFocus();
},_onBlur:function(){
this.onBlur();
}});
return _16de("dijit._FocusMixin",null,{_focusManager:focus});
});
},"curam/util/ui/form/renderer/DateEditRendererFormEventsAdapter":function(){
define(["dojo/_base/declare","dojo/_base/unload","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter","curam/util/DatePicker"],function(_16df,_16e0,_16e1,_16e2){
var _16e3=_16df("curam.util.ui.form.renderer.DateEditRendererFormEventsAdapter",_16e1,{_unsubscribes:[],constructor:function(id,_16e4){
this.elementID=id;
this.pathID=_16e4;
var _16e5=dojo.subscribe("curam/modal/component/ready",this,function(){
this.element=document.getElementById(this.elementID);
_16e5.remove();
});
_16e0.addOnUnload(function(){
this._unsubscribes&&this._unsubscribes.forEach(function(hh){
hh.remove();
});
});
},addChangeListener:function(_16e6){
var topic="curam/util/CuramFormsAPI/formChange/datePicker".concat(this.getElementID());
this._unsubscribes.push(window.dojo.subscribe(topic,this,function(_16e7){
this.getFormElement().value=_16e7.value;
_16e6();
}));
},setFormElementValue:function(value){
var self=this;
this._unsubscribes.push(dojo.subscribe("curam/modal/component/ready",this,function(){
var _16e8=new _16e2();
_16e8.setDate(self.getElementID(),value);
this.getFormElement().value=value;
}));
}});
return _16e3;
});
},"dijit/form/ValidationTextBox":function(){
define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/i18n","./TextBox","../Tooltip","dojo/text!./templates/ValidationTextBox.html","dojo/i18n!./nls/validate"],function(_16e9,_16ea,lang,i18n,_16eb,_16ec,_16ed){
var _16ee=_16e9("dijit.form.ValidationTextBox",_16eb,{templateString:_16ed,required:false,promptMessage:"",invalidMessage:"$_unset_$",missingMessage:"$_unset_$",message:"",constraints:{},pattern:".*",regExp:"",regExpGen:function(){
},state:"",tooltipPosition:[],_deprecateRegExp:function(attr,value){
if(value!=_16ee.prototype[attr]){
_16ea.deprecated("ValidationTextBox id="+this.id+", set('"+attr+"', ...) is deprecated.  Use set('pattern', ...) instead.","","2.0");
this.set("pattern",value);
}
},_setRegExpGenAttr:function(_16ef){
this._deprecateRegExp("regExpGen",_16ef);
this._set("regExpGen",this._computeRegexp);
},_setRegExpAttr:function(value){
this._deprecateRegExp("regExp",value);
},_setValueAttr:function(){
this.inherited(arguments);
this._refreshState();
},validator:function(value,_16f0){
return (new RegExp("^(?:"+this._computeRegexp(_16f0)+")"+(this.required?"":"?")+"$")).test(value)&&(!this.required||!this._isEmpty(value))&&(this._isEmpty(value)||this.parse(value,_16f0)!==undefined);
},_isValidSubset:function(){
return this.textbox.value.search(this._partialre)==0;
},isValid:function(){
return this.validator(this.textbox.value,this.get("constraints"));
},_isEmpty:function(value){
return (this.trim?/^\s*$/:/^$/).test(value);
},getErrorMessage:function(){
var _16f1=this.invalidMessage=="$_unset_$"?this.messages.invalidMessage:!this.invalidMessage?this.promptMessage:this.invalidMessage;
var _16f2=this.missingMessage=="$_unset_$"?this.messages.missingMessage:!this.missingMessage?_16f1:this.missingMessage;
return (this.required&&this._isEmpty(this.textbox.value))?_16f2:_16f1;
},getPromptMessage:function(){
return this.promptMessage;
},_maskValidSubsetError:true,validate:function(_16f3){
var _16f4="";
var _16f5=this.disabled||this.isValid(_16f3);
if(_16f5){
this._maskValidSubsetError=true;
}
var _16f6=this._isEmpty(this.textbox.value);
var _16f7=!_16f5&&_16f3&&this._isValidSubset();
this._set("state",_16f5?"":(((((!this._hasBeenBlurred||_16f3)&&_16f6)||_16f7)&&(this._maskValidSubsetError||(_16f7&&!this._hasBeenBlurred&&_16f3)))?"Incomplete":"Error"));
this.focusNode.setAttribute("aria-invalid",this.state=="Error"?"true":"false");
if(this.state=="Error"){
this._maskValidSubsetError=_16f3&&_16f7;
_16f4=this.getErrorMessage(_16f3);
}else{
if(this.state=="Incomplete"){
_16f4=this.getPromptMessage(_16f3);
this._maskValidSubsetError=!this._hasBeenBlurred||_16f3;
}else{
if(_16f6){
_16f4=this.getPromptMessage(_16f3);
}
}
}
this.set("message",_16f4);
return _16f5;
},displayMessage:function(_16f8){
if(_16f8&&this.focused){
_16ec.show(_16f8,this.domNode,this.tooltipPosition,!this.isLeftToRight());
}else{
_16ec.hide(this.domNode);
}
},_refreshState:function(){
if(this._created){
this.validate(this.focused);
}
this.inherited(arguments);
},constructor:function(_16f9){
this.constraints=lang.clone(this.constraints);
this.baseClass+=" dijitValidationTextBox";
},startup:function(){
this.inherited(arguments);
this._refreshState();
},_setConstraintsAttr:function(_16fa){
if(!_16fa.locale&&this.lang){
_16fa.locale=this.lang;
}
this._set("constraints",_16fa);
this._refreshState();
},_setPatternAttr:function(_16fb){
this._set("pattern",_16fb);
this._refreshState();
},_computeRegexp:function(_16fc){
var p=this.pattern;
if(typeof p=="function"){
p=p.call(this,_16fc);
}
if(p!=this._lastRegExp){
var _16fd="";
this._lastRegExp=p;
if(p!=".*"){
p.replace(/\\.|\[\]|\[.*?[^\\]{1}\]|\{.*?\}|\(\?[=:!]|./g,function(re){
switch(re.charAt(0)){
case "{":
case "+":
case "?":
case "*":
case "^":
case "$":
case "|":
case "(":
_16fd+=re;
break;
case ")":
_16fd+="|$)";
break;
default:
_16fd+="(?:"+re+"|$)";
break;
}
});
}
try{
"".search(_16fd);
}
catch(e){
_16fd=this.pattern;
console.warn("RegExp error in "+this.declaredClass+": "+this.pattern);
}
this._partialre="^(?:"+_16fd+")$";
}
return p;
},postMixInProperties:function(){
this.inherited(arguments);
this.messages=i18n.getLocalization("dijit.form","validate",this.lang);
this._setConstraintsAttr(this.constraints);
},_setDisabledAttr:function(value){
this.inherited(arguments);
this._refreshState();
},_setRequiredAttr:function(value){
this._set("required",value);
this.focusNode.setAttribute("aria-required",value);
this._refreshState();
},_setMessageAttr:function(_16fe){
this._set("message",_16fe);
this.displayMessage(_16fe);
},reset:function(){
this._maskValidSubsetError=true;
this.inherited(arguments);
},_onBlur:function(){
this.displayMessage("");
this.inherited(arguments);
},destroy:function(){
_16ec.hide(this.domNode);
this.inherited(arguments);
}});
return _16ee;
});
},"curam/util/SessionTimeout":function(){
define(["dojo/dom","dojo/dom-construct","curam/util","curam/dialog","curam/util/external","curam/util/LocalConfig"],function(dom,_16ff,util,_1700,_1701,_1702){
curam.define.singleton("curam.util.SessionTimeout",{_EXPIRED_MESSAGE_KEY:"expiredMessage",_INTERNAL_LOGOUT_WRAPPER:"internal-logout-wrapper",logoutPageID:"",userMessageNode:null,userMessageNodeID:"userMessage",minutesCompNodeID:"minutesComp",secondsCompNodeID:"secondsComp",sessTimeoutWarningJSPXDialog:"session-timeout-warning-dialog.jspx",interactionAllowance:3000,_idleTick:10000,_expiryTick:1000,_cfg:null,_idleClock:null,idleAllowance:0,tlw:false,_isExternal:false,requiredWarnDurationMs:0,effectiveTimeout:0,__remainingCountdown:0,checkSessionExpired:function(){
sto._invalidateIdleClock(true);
sto._isExternal=sto.tlw.jsScreenContext&&sto.tlw.jsScreenContext.hasContextBits("EXTAPP");
sto._cfg=sto.getTimeoutWarningConfig();
sto._cfg["isExternal"]=sto._isExternal;
sto._cfg["tlw"]=sto.tlw;
if(sto._cfg.bufferingPeriod){
sto.interactionAllowance=sto._cfg.bufferingPeriod*1000;
}
var _1703=sto._cfg&&sto._cfg.timeout;
sto.requiredWarnDurationMs=_1703?_1703*1000:0;
sto._idleClock=setInterval(function(){
sto._doCheckExp();
},sto._idleTick);
},_doCheckExp:function(){
var _1704="";
if(typeof (Storage)!=="undefined"){
if(sessionStorage.sessionExpiry){
_1704=sessionStorage.sessionExpiry;
}
}
if(!sto.previousSessionExpiryString||(sto.previousSessionExpiryString!=_1704)){
sto._validateSessionExpiry(_1704);
return;
}
if(sto.idleAllowance==0){
return;
}
sto.idleAllowance-=sto._idleTick;
if(sto.idleAllowance<=0){
sto._invalidateIdleClock(true);
sto._openSessionTimeoutWarningModalDialog();
}
},_validateSessionExpiry:function(_1705){
if(_1705==null){
sto._invalidateIdleClock();
return;
}
var _1706=_1705.split("-",2);
if(_1706&&_1706.length==2){
for(var idx in _1706){
var _1707=Math.abs(_1706[idx]);
if(isNaN(_1707)){
sto._invalidateIdleClock();
return;
}
_1706[idx]=_1707;
}
sto.idleAllowance=Math.abs(_1706[0]-_1706[1]);
sto._insertWarnDuration();
sto.previousSessionExpiryString=_1705;
return;
}
sto._invalidateIdleClock();
},_invalidateIdleClock:function(_1708){
_1708&&clearInterval(sto._idleClock);
sto.previousSessionExpiryString=false;
sto.idleAllowance=0;
},_insertWarnDuration:function(){
var _1709=sto.idleAllowance-sto.interactionAllowance-sto._idleTick;
sto.effectiveTimeout=Math.min(sto.requiredWarnDurationMs,Math.max(_1709,0));
sto._cfg["effectiveTimeout"]=sto.effectiveTimeout;
sto.idleAllowance-=(sto.effectiveTimeout);
},getTimeoutWarningConfig:function(){
var wdef=sto.tlw||sto._cfg.tlw;
if(wdef.TIMEOUT_WARNING_CONFIG){
return wdef.TIMEOUT_WARNING_CONFIG.timeoutWarning;
}else{
return sto.pseudoConfig;
}
},_openSessionTimeoutWarningModalDialog:function(){
var size={width:sto._cfg.width,height:sto._cfg.height};
if(sto._isExternal){
sto.tlw.openModal(sto.sessTimeoutWarningJSPXDialog,size);
}else{
sto.tlw.dialogOpenerRef=util.showModalDialogWithRef(sto.sessTimeoutWarningJSPXDialog,null,size);
sto.tlw.dialogOpenerRef&&sto.tlw.dialogOpenerRef._setClosableAttr(false);
}
},initTimer:function(_170a,_170b){
sto._cfg=_170a;
sto.__remainingCountdown=_170a.effectiveTimeout;
var _170c=dom.byId(sto.minutesCompNodeID),_170d=dom.byId(sto.secondsCompNodeID);
sto.expiryCountdown=setInterval(function(){
sto._countDown(_170c,_170d);
},sto._expiryTick);
},_countDown:function(_170e,_170f){
if(sto.__remainingCountdown==0){
sto._stopCountdown();
return;
}
sto.__remainingCountdown-=sto._expiryTick;
if(sto.__remainingCountdown<=0){
sto._stopCountdown();
sto.tlw.curam.util.SessionTimeout.autoLogout();
_1700.closeModalDialog();
return;
}
var _1710=new Date(sto.__remainingCountdown);
var secs=""+_1710.getSeconds(),sPad=(secs.length==1)?"0":"";
_170e.innerHTML=_1710.getMinutes();
_170f.innerHTML=sPad+secs;
},_stopCountdown:function(){
sto.__remainingCountdown=0;
clearInterval(sto.expiryCountdown);
},waitForRedirection:function(){
dojo.subscribe("/curam/dialog/close",function(){
sto._redirectToLogoutWrapper();
});
},autoLogout:function(){
dojo.subscribe("/curam/dialog/close",function(){
sto._redirectLoginWithSessionExpiredMessage();
});
},_redirectToLogoutWrapper:function(){
sto._cfg.tlw.dojo.publish("curam/redirect/logout");
var page=sto._cfg.logoutPage||false;
if(!page){
return;
}
if(sto._cfg.isExternal){
sto._cfg.tlw.displayContent({pageID:page,param:[{paramKey:"invalidateSession",paramValue:true}]});
}else{
sto._redirectInternalLogoutWrapper(page);
}
},_redirectInternalLogoutWrapper:function(_1711){
sto._cfg.tlw.dialogOpenerRef=null;
if(_1711===sto._INTERNAL_LOGOUT_WRAPPER){
_1711+=".jspx?invalidateSession=true";
dojo.global.location=jsBaseURL+"/"+_1711;
}else{
var _1712=_1700.getParentWindow();
_1711+="Page.do?invalidateSession=true";
if(_1712&&_1712.location!==sto._cfg.tlw.location){
_1700.doRedirect(_1712,_1711,true);
}else{
curam.tab.getTabController().handleLinkClick(_1711);
}
}
},_redirectLoginWithSessionExpiredMessage:function(){
var _1713=sto._cfg.expiredUserMessageTxt||"";
localStorage[sto._EXPIRED_MESSAGE_KEY]=_1713;
sto._redirectToLogoutWrapper();
},resetAndStay:function(){
var _1714=sto._cfg.tlw.dojo.subscribe("/curam/dialog/close",function(){
if(sto._cfg.tlw.dialogOpenerRef){
var pRef=_1700.getParentWindow(window);
pRef&&pRef.focus();
sto._cfg.tlw.dialogOpenerRef=null;
}
sto._cfg.tlw.dojo.unsubscribe(_1714);
sto._stopCountdown();
sto.checkSessionExpired();
require(["curam/debug"],function(debug){
debug.log(debug.getProperty("continueApp"));
});
});
},createExpiredSessionMessageHTML:function(_1715){
var _1716=_1702.readOption(sto._EXPIRED_MESSAGE_KEY);
if(_1716){
messageContainerDOM=dom.byId(_1715);
if(messageContainerDOM){
var _1717="<div id='error-messages-container' class='wrapper-expired-message'>"+"<ul id='error-messages' class='messages'>"+"<li class='level-1'><div><span id='message'>"+_1716+"</span></div></li></ul></div>";
_16ff.place(_16ff.toDom(_1717),messageContainerDOM);
}
_1702.clearOption(sto._EXPIRED_MESSAGE_KEY);
}
},displayUserMsgAsParagraphs:function(msg,_1718){
var _1719=_1718||dom.byId(sto.userMessageNodeID);
var _171a=msg.replace("\\n","[<p>]").replace("\n","[<p>]").split("[<p>]");
var _171b=document.createDocumentFragment();
for(line in _171a){
var _171c=document.createElement("p");
_171c.innerHTML=_171a[line];
_171b.appendChild(_171c);
}
_16ff.place(_171b,_1719);
},});
var sto=curam.util.SessionTimeout;
sto.tlw=curam.util.getTopmostWindow()||window.top;
return sto;
});
},"curam/codetable-hierarchy":function(){
define(["curam/util/Request","curam/debug","dojo/data/ItemFileReadStore","curam/widget/FilteringSelect"],function(_171d,debug){
var _171e={initLists:function(_171f,_1720,_1721){
this.noOptionCode=_171f;
this.noOptionDesc=_1720;
this.ddInfo=_1721;
this.lists=function(){
var next=null;
for(var i=_1721.length-1;i>=0;i--){
next=new _171e.DropDown(dijit.byId(_1721[i].id),_1721[i].ctName,_171f,_1720,next);
}
};
dojo.addOnLoad(this.lists);
},DropDown:function(_1722,_1723,_1724,_1725,next){
this.node=_1722.domNode;
this.widgetNode=_1722;
this.codeTableName=_1723;
this.noOptionCode=_1724;
this.noOptionDesc=_1725;
this.next=next;
var _1726=this;
this.populate=function(){
if(!_1726.widgetNode.get("value")){
_1726.resetNext(_1726);
}else{
if(_1726.next!=null){
_1726.resetNext(_1726);
if(_1726.widgetNode.get("value")==0){
return;
}
_171d.post({url:"../servlet/JSONServlet",handleAs:"text",preventCache:true,load:function(_1727,evt){
if(_1727.length<3){
curam.debug.log(debug.getProperty("curam.codetable-hierarchy.msg.1")+_1726.codeTableName+debug.getProperty("curam.codetable-hierarchy.msg.2")+_1726.widgetNode.get("value"));
return;
}
var _1728=dojo.fromJson(_1727);
_1728.unshift({"value":_1726.noOptionCode,"name":""});
var _1729=dijit.byId(_1726.next.widgetNode.id);
var _172a=new dojo.data.ItemFileReadStore({data:{label:"name",identifier:"value",items:_1728}});
_172a.fetch({onComplete:function(item,_172b){
_1729.set("store",_172a);
_1729.set("value",_1726.noOptionCode);
}});
},error:function(error){
debug.log(error);
},content:{"content":dojo.toJson({operation:"getCodeTableSubsetForFilteringSelect",args:[_1726.codeTableName,_1726.widgetNode.get("value")]})}});
}
}
};
this.resetNext=function(_172c){
while(_172c.next!=null){
var _172d=[];
_172d.unshift({"value":_172c.noOptionCode,"name":_172c.noOptionDesc});
var _172e=dijit.byId(_172c.next.widgetNode.id);
var _172f=new dojo.data.ItemFileReadStore({data:{label:"name",identifier:"value",items:_172d}});
_172f.fetch({onComplete:function(item,_1730){
_172e.set("store",_172f);
_172e.set("displayedValue",_172c.noOptionDesc);
}});
_172c=_172c.next;
}
};
if(next!=null){
dojo.connect(this.widgetNode,"onChange",this.populate);
}
}};
dojo.global.CodeTableHierarchy=_171e;
return _171e;
});
},"curam/util/ui/refresh/TabRefreshController":function(){
define(["dojo/_base/declare","curam/inspection/Layer","curam/debug","curam/util/ui/refresh/RefreshEvent"],function(_1731,layer,debug){
var _1732=_1731("curam.util.ui.refresh.TabRefreshController",null,{EVENT_REFRESH_MENU:"/curam/refresh/menu",EVENT_REFRESH_NAVIGATION:"/curam/refresh/navigation",EVENT_REFRESH_CONTEXT:"/curam/refresh/context",EVENT_REFRESH_MAIN:"/curam/refresh/main-content",_tabWidgetId:null,_configOnSubmit:null,_configOnLoad:null,_handler:null,_lastSubmitted:null,_currentlyRefreshing:null,_ignoreContextRefresh:true,_initialMenuAndNavRefreshDone:false,_nullController:null,constructor:function(_1733,_1734){
this._configOnSubmit={};
this._configOnLoad={};
if(!_1734){
this._nullController=true;
return;
}
this._tabWidgetId=_1733;
dojo.forEach(_1734.config,dojo.hitch(this,function(item){
this._configOnSubmit[item.page]=item.onsubmit;
this._configOnLoad[item.page]=item.onload;
}));
layer.register("curam/util/ui/refresh/TabRefreshController",this);
},pageSubmitted:function(_1735,_1736){
new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONSUBMIT,_1736);
debug.log("curam.util.ui.refresh.TabRefreshController: "+debug.getProperty("curam.util.ui.refresh.TabRefreshController.submit",[_1735,_1736]));
dojo.publish("curam/form/submit",[_1735]);
if(this._configOnSubmit[_1735]){
this._lastSubmitted=_1735;
debug.log("curam.util.ui.refresh.TabRefreshController: "+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+"submit.notify"));
}
},pageLoaded:function(_1737,_1738){
var event=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,_1738);
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController.load",[_1737,_1738]));
if(this._currentlyRefreshing&&this._currentlyRefreshing.equals(event)){
this._currentlyRefreshing=null;
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+"refresh"));
return;
}
var _1739={};
if(_1738==event.SOURCE_CONTEXT_MAIN&&this._configOnLoad[_1737]){
_1739=this._configOnLoad[_1737];
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".load.config"));
}
if(this._lastSubmitted){
var cfg=this._configOnSubmit[this._lastSubmitted];
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".submit.config",[this._lastSubmitted]));
_1739.details=_1739.details||cfg.details;
_1739.menubar=_1739.menubar||cfg.menubar;
_1739.navigation=_1739.navigation||cfg.navigation;
_1739.mainContent=_1739.mainContent||cfg.mainContent;
this._lastSubmitted=null;
}
if(!this._nullController){
this._fireRefreshEvents(_1739,this._ignoreContextRefresh,!this._initialMenuAndNavRefreshDone);
}
if(this._ignoreContextRefresh&&_1738==event.SOURCE_CONTEXT_MAIN){
this._ignoreContextRefresh=false;
}
if(!this._initialMenuAndNavRefreshDone){
this._initialMenuAndNavRefreshDone=true;
}
},_fireRefreshEvents:function(cfg,_173a,_173b){
var _173c=[];
if(cfg.details){
if(_173a){
curam.debug.log("curam.util.ui.refresh.TabRefreshController: ignoring the first CONTEXT refresh request");
}else{
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.context"));
_173c.push(this.EVENT_REFRESH_CONTEXT+"/"+this._tabWidgetId);
}
}else{
if(!_173a){
dojo.publish("curam/tab/contextRefresh");
}
}
if(cfg.menubar||_173b){
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.menu"));
_173c.push(this.EVENT_REFRESH_MENU+"/"+this._tabWidgetId);
}
if(cfg.navigation||_173b){
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.nav"));
_173c.push(this.EVENT_REFRESH_NAVIGATION+"/"+this._tabWidgetId);
}
if(cfg.mainContent){
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.main"));
this._currentlyRefreshing=new curam.util.ui.refresh.RefreshEvent(curam.util.ui.refresh.RefreshEvent.prototype.TYPE_ONLOAD,curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN,null);
_173c.push(this.EVENT_REFRESH_MAIN+"/"+this._tabWidgetId);
}
if(_173c.length>0){
debug.log("curam.util.ui.refresh.TabRefreshController:"+debug.getProperty("curam.util.ui.refresh.TabRefreshController"+".refresh.log",[_173c.length,_173c]));
this._handler(_173c);
}
},setRefreshHandler:function(_173d){
this._handler=_173d;
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
return _1732;
});
},"dojo/_base/url":function(){
define(["./kernel"],function(dojo){
var ore=new RegExp("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$"),ire=new RegExp("^((([^\\[:]+):)?([^@]+)@)?(\\[([^\\]]+)\\]|([^\\[:]*))(:([0-9]+))?$"),_173e=function(){
var n=null,_173f=arguments,uri=[_173f[0]];
for(var i=1;i<_173f.length;i++){
if(!_173f[i]){
continue;
}
var _1740=new _173e(_173f[i]+""),_1741=new _173e(uri[0]+"");
if(_1740.path==""&&!_1740.scheme&&!_1740.authority&&!_1740.query){
if(_1740.fragment!=n){
_1741.fragment=_1740.fragment;
}
_1740=_1741;
}else{
if(!_1740.scheme){
_1740.scheme=_1741.scheme;
if(!_1740.authority){
_1740.authority=_1741.authority;
if(_1740.path.charAt(0)!="/"){
var path=_1741.path.substring(0,_1741.path.lastIndexOf("/")+1)+_1740.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==(segs.length-1)){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_1740.path=segs.join("/");
}
}
}
}
uri=[];
if(_1740.scheme){
uri.push(_1740.scheme,":");
}
if(_1740.authority){
uri.push("//",_1740.authority);
}
uri.push(_1740.path);
if(_1740.query){
uri.push("?",_1740.query);
}
if(_1740.fragment){
uri.push("#",_1740.fragment);
}
}
this.uri=uri.join("");
var r=this.uri.match(ore);
this.scheme=r[2]||(r[1]?"":n);
this.authority=r[4]||(r[3]?"":n);
this.path=r[5];
this.query=r[7]||(r[6]?"":n);
this.fragment=r[9]||(r[8]?"":n);
if(this.authority!=n){
r=this.authority.match(ire);
this.user=r[3]||n;
this.password=r[4]||n;
this.host=r[6]||r[7];
this.port=r[9]||n;
}
};
_173e.prototype.toString=function(){
return this.uri;
};
return dojo._Url=_173e;
});
},"curam/widget/SearchMultipleTextBox":function(){
define(["dojo/query","dojo/dom-style","dojo/dom-construct","dojo/on","dojo/dom-geometry","dojo/dom-class","dojo/mouse","dojo/dom-attr","dojo/_base/fx","dojo/_base/lang","dojo/_base/declare","dojo/_base/array","dojo/topic","dojo/debounce","dojo/Deferred","dojo/keys","curam/date/locale","dojo/has","dojo/sniff","dojo/text!curam/widget/templates/SearchMultipleTextBox.html","dijit/form/TextBox","curam/util","curam/util/Request","curam/util/ResourceBundle","curam/ui/UIController","curam/tab","curam/debug","curam/cdsl/connection/SimpleAccess","curam/cdsl/connection/CuramConnection","curam/tab"],function(query,_1742,_1743,on,_1744,_1745,mouse,_1746,fx,lang,_1747,_1748,topic,_1749,_174a,keys,_174b,has,sniff,_174c,_174d,util,_174e,_174f,_1750,tab,debug,_1751,_1752,tab){
var _1753=_1747("curam.widget.SearchMultipleTextBox",_174d,{templateString:_174c,quickSearch:null,hasIos:false,hasQuickSearch:null,quickSearchXHRPromises:[],searchResults:{},lastQueryTerm:"",spinnerTimeouts:[],_simpleAccess:_1751,personQuantityToEagerLoad:6,paginationStart:0,paginationPageSize:6,pagesMappedToTab:[],pagesNotMappedToTab:[],pagesMappedToMultipleTabs:[],availableKeywordsList:null,menuOpenedComboboxHeight:72,menuOpenedFurtherOptionsHeight:18,menuOpenedFurtherOptionsHeightQuickSearch:12,menuOpenedFurtherOptionsHeightNoCombobox:18,quickSearchMenuOpenedHeight:29,currentOpenedHeight:29,maxItemsHeight:416,maxFlatListHeight:416,closedHeight:null,destroyContextMenu:false,personInContextMenu:null,constructor:function(){
this.inherited(arguments);
this.bundle=new _174f("SmartNavigator");
},postCreate:function(){
this._simpleAccess.initConnection(new _1752(curam.util.retrieveBaseURL()+"/dataservice"));
this.hasIos=has("ios");
this._queryDOMNodes();
_1745.add(this.searchControlsDiv[0],"multiple-search-banner");
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
_1745.add(this.searchOptionsDiv[0],"multiple-search-options");
}
this.searchInputField.maxLength=100;
on(this.applicationSearchDiv[0],"mousedown",lang.hitch(this,function(evt){
this.clickedInsideApplicationSearchWidget=this._isElementPartOfApplicationSearch(evt.target);
}));
on(this.applicationSearchDiv[0],"mouseup",lang.hitch(this,function(evt){
this.clickedInsideApplicationSearchWidget=false;
}));
if(this.searchOptionsDiv.length>0){
on(this.searchOptionsDiv[0].firstChild,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
}
var _1754=this.searchIcon[0].onclick;
this.searchIcon[0].onclick=lang.hitch(this,function(e){
if(!_1745.contains(this.searchIcon[0],"dijitDisabled")){
if(this.quickSearch){
this._search();
}else{
_1754(e);
}
}
});
on(this.searchIcon,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(this.searchIcon,"click",lang.hitch(this,function(evt){
if(!this.quickSearch){
this._hideSearchBox();
}
}));
if(this.hasQuickSearch){
this._initializeSmartNavigatorWidget();
}else{
on(this.searchInputField,"keydown",lang.hitch(this,this._onKeyUp));
}
},_initializeSmartNavigatorWidget:function(){
topic.subscribe("curam/application-search/combobox-changed",lang.hitch(this,function(_1755){
if(this.quickSearch!=null){
if(_1755.indexOf("smart-navigator|smart-navigator")>=0){
this._enableQuickSearch();
}else{
this._disableQuickSearch();
}
this._fixQuickSearchHeight();
}
}));
var _1756=this._simpleAccess.makeRequest("SmartNavigatorFacade","getSearchConfiguration");
this.quickSearchXHRPromises.push(_1756);
_1756.then(lang.hitch(this,function(data){
var _1757=false;
if(data[0].debounceTimeout){
var _1758=parseInt(data[0].debounceTimeout);
if(!isNaN(_1758)&&_1758>=0){
on(this.searchInputField,"keydown",_1749(lang.hitch(this,this._onKeyUpAutoComplete),_1758));
_1757=true;
}
}
if(!_1757){
on(this.searchInputField,"keydown",lang.hitch(this,this._onKeyUp));
}
}),lang.hitch(this,this._ajaxErrorCallback));
on(this.searchInputField,"keyup",lang.hitch(this,function(event){
this._onDownArrowKey(event);
}));
on(this.searchInputField,"keydown",lang.hitch(this,function(event){
if(event.keyCode==8){
var _1759=(window.getSelection().toString()===this.searchInputField.value)?true:false;
if(_1759){
this._removeAriaOwnsAttributeFromHiddenInfoSpanElement();
}
}
}));
on(this.searchIcon,"keyup",lang.hitch(this,function(event){
this._onDownArrowKey(event);
}));
on(window,"resize",_1749(lang.hitch(this,function(){
var _175a=query(".application-search-upfront-popup")[0];
if(_175a&&!has("android")&&!has("ios")){
this._hideSearchBox();
}
}),50));
on.emit(window,"resize",{bubbles:true,cancelable:true});
this.searchIcon[0].title=this.bundle.getProperty("SmartNavigator.icon.title");
on(query("body.curam"),["click","touchstart","keyup"],lang.hitch(this,function(evt){
if(evt.type==="keyup"&&evt.keyCode!==keys.ESCAPE){
return;
}
var _175b=evt.type==="keyup";
var _175c=this._destroyContextMenuIfNecessary(evt.target);
var _175d=this._hideKeywordsListIfNecessary(evt.target,_175b);
var _175e=_1745.contains(evt.target,"appSearchItem");
if(evt.type==="keyup"){
if(_175e){
var _175f=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_175f){
_175f.focus();
}
}else{
if(!_175c&&!_175d){
this._hideSearchBox();
if(this._isElementPartOfApplicationSearch(evt.target)){
this.searchIcon[0].focus();
}
}
}
}else{
var _1760=this._isElementPartOfApplicationSearch(evt.target);
if(!_1760){
this._hideSearchBox();
}
}
}));
},_queryDOMNodes:function(){
if(this.searchControlsDiv==null){
this.searchControlsDiv=query(".search-input-controls");
}
if(this.searchIconDiv==null){
this.searchIconDiv=query(".application-search-anchor-div");
}
if(this.searchIcon==null){
this.searchIcon=query(".application-search-anchor");
}
if(this.searchInputField==null){
this.searchInputField=this.focusNode;
}
if(this.inputNode==null){
this.inputNode=query(".search-input-controls .text")[0];
}
if(this.applicationSearchDiv==null){
this.applicationSearchDiv=query(".application-search");
}
if(this.searchOptionsDiv==null){
this.searchOptionsDiv=query(".search-options");
}
if(this.searchOptionsDiv.length>0&&this.searchOptionsDivOpenedColor==null){
this.searchOptionsDivOpenedColor=_1742.get(this.searchOptionsDiv[0],"color");
}
if(this.searchInputImg==null){
this.searchInputImg=query(".application-search-anchor img");
}
if(this.quickSearch==null){
this.quickSearch=query(".application-search.quick-search").length>0;
}
if(this.hasQuickSearch==null){
this.hasQuickSearch=query(".application-search.has-quick-search").length>0;
}
},_hideSearchBox:function(){
if(this._isSearchInputFieldPopulated()){
_1742.set(this.searchInputField,"color",_1742.get(this.applicationSearchDiv[0],"color"));
}else{
_1742.set(this.searchInputField,this.originalInputColor);
}
_1745.remove(this.searchInputField,"input-placeholder-opened");
_1745.add(this.searchInputField,"input-placeholder-closed");
_1746.remove(this.searchInputField,"aria-describedby");
if(this.searchTextDiv!=null&&_1742.get(this.searchTextDiv[0],"background-color")!=this.backgroundColor){
_1742.set(this.searchTextDiv[0],"background-color",this.backgroundColor);
_1742.set(this.searchControlsDiv[0],"background-color",this.backgroundColor);
_1742.set(this.searchIconDiv[0],"background-color",this.backgroundColor);
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-on-dark.svg";
_1745.remove(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(this.searchOptionsDiv.length>0){
_1742.set(this.searchOptionsDiv[0],"display","none");
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_1742.set(this.appBannerComboBoxDiv[0],"display","none");
}
_1742.set(this.applicationSearchDiv[0],"height",this.closedHeight+"px");
this.applicationSearchDiv.style({left:"0px"});
this._hideResultList();
}
},_isElementPartOfApplicationSearch:function(_1761){
if(_1761.parentElement!=null){
if(_1745.contains(_1761,"application-search")||_1745.contains(_1761,"dijitComboBoxMenuPopup")){
return true;
}else{
return this._isElementPartOfApplicationSearch(_1761.parentElement);
}
}
return false;
},_hideSearchBoxIfNotFocused:function(evt){
var _1762=this;
setTimeout(function(){
var _1763=document.activeElement;
if(!_1762._isElementPartOfContainer(_1763,"application-search")&&!_1762.clickedInsideApplicationSearchWidget){
_1762._hideSearchBox();
}
},1);
},_setPlaceHolderAttr:function(v){
this.searchInputField=this.domNode.firstChild.firstChild;
_1746.set(this.searchInputField,"placeholder",v);
},_isSearchInputFieldPopulated:function(){
return this.searchInputField.value.length>0;
},_onKeyUp:function(evt){
if(evt.keyCode===keys.DOWN_ARROW){
return false;
}
var _1764=this._isSearchInputFieldPopulated();
this._enableOrDisableSearchLink(evt,_1764);
if((evt.keyCode===keys.BACKSPACE||evt.keyCode===keys.DELETE)&&this.quickSearch&&!_1764&&!this._isShowingHistory()){
this.lastQueryTerm="";
this._hideDropDown();
this._showSearchHistory();
}else{
return false;
}
},_onKeyUpAutoComplete:function(evt){
if(evt.keyCode===keys.DOWN_ARROW){
return false;
}
this._onKeyUp(evt);
if(evt.target.value.trim()!==this.lastQueryTerm.trim()){
this._doQuickSearch(evt.target.value);
}
},_onDownArrowKey:function(event){
if(event.keyCode===keys.DOWN_ARROW){
var _1765=query(".appSearchDropDownButton",this.applicationSearchDiv[0]);
if(_1765.length>0){
_1765[0].focus();
}else{
var tags=query(".appSearchKeywordTag",this.applicationSearchDiv[0]);
if(tags.length>0){
tags[0].focus();
}else{
var items=query(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(items.length>0){
items[0].focus();
}
}
}
}
},_onKeywordsDownArrowKey:function(event){
if(event.keyCode===keys.DOWN_ARROW){
if(_1745.contains(event.target,"appSearchKeywordsListButton")){
if(this._showingAvailableKeywordsList){
query(".availableKeyword .keywordTerm",this.applicationSearchDiv[0])[0].focus();
}else{
var items=query(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(items.length>0){
items[0].focus();
}
}
}else{
var _1766=this._findSiblingElement(event.target,true,"keywordTerm");
if(_1766){
_1766.focus();
}else{
var _1767=this._findSiblingElement(event.target.parentElement.parentElement,true,"availableKeyword");
if(_1767){
_1766=query(".keywordTerm:first-child",_1767)[0];
if(_1766){
_1766.focus();
}
}
}
}
}
},_onKeywordsUpArrowKey:function(event){
if(event.keyCode===keys.UP_ARROW){
var _1768=this._findSiblingElement(event.target,false,"keywordTerm");
if(_1768){
_1768.focus();
}else{
var _1769=this._findSiblingElement(event.target.parentElement.parentElement,false,"availableKeyword");
if(_1769){
_1768=query(".keywordTerm:last-child",_1769)[0];
if(_1768){
_1768.focus();
}
}
}
}
},_search:function(){
if(this.quickSearch){
this._doQuickSearch(this.searchInputField.value);
if(!_1745.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
}
}else{
curam.util.search("__o3.appsearch.searchText","__o3.appsearch.searchType");
this._hideSearchBox();
}
},_doQuickSearch:function(_176a){
if(this.quickSearch){
if(_176a){
this.lastQueryTerm=_176a;
this._resetSearchResults();
var query={term:_176a,dateFormat:window.jsDF};
if(this.quickSearchXHRPromises.length>0){
this.quickSearchXHRPromises.forEach(function(_176b){
_176b.cancel();
});
this.quickSearchXHRPromises=[];
}
var _176c=this._simpleAccess.makeRequest("SmartNavigatorFacade","search",query);
this.quickSearchXHRPromises.push(_176c);
_176c.then(lang.hitch(this,this._searchCallback,_176a),lang.hitch(this,this._ajaxErrorCallback));
this._showSpinnerDiv();
}else{
this.lastQueryTerm="";
this._hideDropDown();
this._showSearchHistory();
}
}
},_showSearchHistory:function(){
this._resetSearchResults();
this._cancelPreviousAjaxCalls();
var _176d=this._simpleAccess.makeRequest("SmartNavigatorFacade","searchUserHistory");
this.quickSearchXHRPromises.push(_176d);
_176d.then(lang.hitch(this,this._showSearchHistorySuccessCallback),lang.hitch(this,this._ajaxErrorCallback));
},_showSearchHistorySuccessCallback:function(data){
var _176e=data[0]._data;
this.quickSearchXHRPromises=[];
if(_176e.dtls&&_176e.dtls.length>0){
var _176f=_176e.dtls.map(function(_1770){
var _1771={url:_1770.url,icon:_1770.icon,preferredTabs:_1770.preferredTabs,isModal:_1770.isModal};
if(_1770.targetValue){
_1771.action={description:_1770.targetDescription,type:_1770.targetType};
}
if(_1770.searchConcernRoleName){
_1771.person={concernRoleName:_1770.searchConcernRoleName,dateOfBirth:_1770.searchConcernRoleDateOfBirth,concernRoleId:_1770.searchConcernRoleId,formattedAddress:_1770.searchConcernRoleAddress,restrictedIndOpt:_1770.searchConcernRoleRestrictedIndOpt,personPhotoURL:_1770.personPhotoURL,items:[]};
}
return _1771;
});
this._searchCallback("",[{_data:{data:_176f}}],this.bundle.getProperty("SmartNavigator.recent.searches"),true);
}
},_ajaxErrorCallback:function(err){
if(this.quickSearch&&err.name!=="CancelError"){
debug.log("Application Search - Smart Navigator Error: ",err);
this.quickSearchXHRPromises=[];
this._hideSpinnerDiv();
if(!_1745.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
}
if(err.errors&&err.errors[0]&&err.errors[0].type&&err.errors[0].type==="error"&&err.errors[0].message){
this._createMessage(err.errors[0].message);
}else{
this._createMessage(this.bundle.getProperty("SmartNavigator.error"));
}
this._fixQuickSearchHeight();
}
return [];
},_searchCallback:function(_1772,data,_1773,_1774){
var _1775=data[0]._data;
var _1776=this;
_1776.quickSearchXHRPromises=[];
_1776.paginationTotalItems=0;
_1776.searchResults=_1775.data.reduce(function(_1777,item,index){
item.rendered=false;
if(item.person&&item.person.concernRoleId!=="0"){
_1777.people.push(item);
_1777.personQueryTerms.push(item.person.terms);
_1777.hasItems=true;
_1776.paginationTotalItems++;
if(item.person.restrictedIndOpt){
item.person.formattedDateOfBirth="******";
}else{
if(item.person.dateOfBirth&&item.person.dateOfBirth.getTime()!==-62135769600000){
item.person.formattedDateOfBirth=_174b.format(_174b.parseDate(item.person.dateOfBirth),{selector:"date",datePattern:window.jsDF})+", "+_1776.bundle.getProperty("SmartNavigator.age")+" "+_1776._getAge(item.person.dateOfBirth);
}else{
item.person.formattedDateOfBirth=_1776.bundle.getProperty("SmartNavigator.age.not.recorded");
}
}
if(item.action&&item.action.description){
item.items=[item.action];
}else{
item.action=null;
}
}else{
if(item.action){
item.person=null;
_1777.actionsOnly.push(item);
_1777.hasItems=true;
_1776.paginationTotalItems++;
}
}
return _1777;
},_1776.searchResults);
_1776.searchResults.messages.info=_1775.infoMessage;
_1776.searchResults.header=_1773;
_1776.searchResults.isHistory=_1774;
_1776.searchResults.keywords=_1775.keywords||[];
_1776.searchResults.queryTerm=_1772;
if(_1776.searchResults.people.length>0&&_1776.searchResults.keywords.length>0){
var _1778=_1776.searchResults.people.slice(0,this.personQuantityToEagerLoad);
_1776._loadPeopleItems(_1778).then(function(){
_1776._hideSpinnerDiv();
_1776._renderResults();
});
}else{
_1776._hideSpinnerDiv();
_1776._renderResults();
}
},_loadPeopleItems:function(_1779){
var _177a=new _174a();
var _177b="";
var _177c={};
_1779.forEach(function(item){
var _177d=lang.clone(item.person);
_177d.personTabDetailsURL=null;
_177d.personPhotoURL=null;
_177d.personTabDetailsURL=null;
_177d.dateOfBirth=null;
_177d.formattedDateOfBirth=null;
_177b+=JSON.stringify(_177d)+"@@@";
_177c[_177d.concernRoleId]=[];
});
_177b=_177b.substring(0,_177b.lastIndexOf("@@@"));
this._cancelPreviousAjaxCalls();
var _177e=this._simpleAccess.makeRequest("SmartNavigatorFacade","searchForTargetsOfPeople",{searchQuery:this.searchResults.queryTerm,peopleDetails:_177b});
this.quickSearchXHRPromises.push(_177e);
_177e.then(lang.hitch(this,function(_177f,_1780,data){
var _177f=data[0].data.reduce(function(map,item,index){
map[item.concernRole].push(item);
return map;
},_177f);
_1780.forEach(function(item){
item.person.itemsLazyLoad=false;
item.person.items=_177f[item.person.concernRoleId];
});
_177a.resolve();
},_177c,_1779),lang.hitch(this,this._ajaxErrorCallback));
return _177a;
},_renderResults:function(_1781){
var _1782=this;
var _1783=query(".searchResultsScreenReadersInfo.hidden",this.applicationSearchDiv[0])[0];
if(_1782.searchResults.hasItems){
_1782.searchResults.moreRecords=_1782.paginationTotalItems>_1782.paginationStart+_1782.paginationPageSize;
_1782._createResultsList(_1782.searchResults.header,_1782.searchResults.isHistory,_1782.searchResults.keywords);
if(this.searchResults.keywords&&this.paginationStart===0){
_1782._createKeywordTags(this.searchResults.keywords,this.searchResults.personQueryTerms);
}
_1782._createMessages();
if(_1782.paginationTotalItems<=_1782.personQuantityToEagerLoad&&_1782._getItemsHeight(true)<_1782.maxFlatListHeight){
_1782._showHiddenResultRows();
}else{
if(_1782.searchResults.keywords.length>0){
_1782._createPersonItemsContextMenuButton();
}
}
}else{
if(_1782.searchResults.queryTerm.length>2){
_1782._createMessage(_1782.bundle.getProperty("SmartNavigator.empty.search"),true,true);
}else{
if(_1782.searchResults.queryTerm.length<=2){
_1746.remove(_1783,"aria-owns");
}
}
}
_1745.add(_1782.applicationSearchDiv[0],"shadow");
_1782._fixQuickSearchHeight();
if(!_1745.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
this.searchInputField.focus();
}
if(_1781){
var _1784=query(".appSearchNewItem",_1782.applicationSearchDiv[0]);
if(_1784.length>0){
_1784[0].focus();
}
}
var _1785=query(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0])[0];
if(_1785){
_1746.set(this.searchInputField,"aria-describedby","resultRowHeaderId");
}else{
_1746.remove(this.searchInputField,"aria-describedby");
}
if(_1783){
var _1786=query(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
if(_1786&&_1786.childElementCount>0){
_1746.set(_1783,"aria-owns","applicationSearchResultListId");
}else{
_1746.remove(_1783,"aria-owns");
}
}
_1782._updateSearchResultsScreenReadersInfo();
_1782._autoScrollToNewItems();
},_removeAriaOwnsAttributeFromHiddenInfoSpanElement:function(){
var _1787=query(".searchResultsScreenReadersInfo.hidden",this.applicationSearchDiv[0])[0];
_1746.remove(_1787,"aria-owns");
},_createResultsList:function(){
var _1788=query(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _1789=null;
if(this.paginationStart===0){
this._destroyResultList();
if(this.searchResults.moreRecords){
this._createMoreRecordsRow(_1788);
}
if(this.searchResults.hasItems){
outerContainer=_1743.place("<div class=\"appSearchItemsOuterContainer\" role=\"presentation\" id=\"applicationSearchResultListId\" aria-live=\"assertive\"></div>",_1788,"after");
_1789=_1743.place("<ul class=\"appSearchItemsContainer\" aria-live=\"assertive\"></ul>",outerContainer,"last");
on(outerContainer,"scroll",lang.hitch(this,function(event){
if(!this._isElementPartOfContainer(document.activeElement,"appSearchItemContext")){
query(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"clicked");
});
}
}));
}
}else{
if(!this.searchResults.moreRecords){
this.searchInputField.focus();
query(".moreItems",this.applicationSearchDiv[0]).forEach(_1743.destroy);
}
_1789=query(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
}
var count=0;
if(this.searchResults.actionsOnly&&this.searchResults.actionsOnly.length>0){
this.searchResults.actionsOnly.forEach(function(_178a){
if(count<this.paginationPageSize){
if(!_178a.rendered){
count++;
}
this._createResultRow(_178a,_1789,"Action",this.searchResults.isHistory);
}
},this);
}
this.searchResults.people.forEach(function(_178b){
if(count<this.paginationPageSize){
if(!_178b.rendered){
count++;
}
this._createResultRow(_178b,_1789,"Person",this.searchResults.isHistory);
}
},this);
if(this.searchResults.header&&this.paginationStart===0){
this._createResultRowHeader(this.searchResults.header);
}
if(this.searchResults.hasItems&&this.paginationStart===0){
_1745.remove(this.applicationSearchDiv[0],"application-search-items-list");
_1745.add(this.applicationSearchDiv[0],"application-search-items-list");
}
var _178c=query(".appSearchItemsOuterContainer",this.applicationSearchDiv[0])[0];
if(_178c&&_178c.scrollHeight>_178c.clientHeight){
_1745.add(_1788.parentNode,"hasScrollBars");
var _178d=query(".moreItems",this.applicationSearchDiv[0])[0];
if(_178d){
_1742.set(_178d,"width",_1789.offsetWidth+"px");
}
}else{
_1745.remove(_1788.parentNode,"hasScrollBars");
}
},_createResultRow:function(item,_178e,type,_178f,_1790){
if(!item.rendered){
if(_178f&&item.person&&item.action){
type="Action";
item.action.type="REQUIRES_PERSON_HISTORY";
}
var _1791=null;
if(type==="Person"){
_1791=this._createPersonRow(item,_178e,type,_178f,_1790);
}else{
if(type==="Action"){
_1791=this._createActionOnlyRow(item,_178e,_178f);
}else{
if(type==="Action_Person"){
_1791=this._createPersonActionRow(item,_178e,_178f,_1790);
}
}
}
if(_1791){
on(_1791,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
}
}
item.rendered=true;
},_createPersonRow:function(_1792,_1793,type,_1794){
var _1795="active";
if(!_1792.url||_1792.url.length===0||_1792.person.restrictedIndOpt){
_1795="inactive";
}
var _1796=_1794?" appSearchItemHistory ":"";
var _1797=this.paginationStart>0?" appSearchNewItem ":"";
var _1798=_1792.person.restrictedIndOpt?" restrictedPerson ":"";
var _1799=_1798?this.bundle.getProperty("SmartNavigator.sensitity.error"):_1792.person.concernRoleName;
var _179a=_1798?"":_1792.person.formattedDateOfBirth;
var _179b=_1798?"":this.bundle.getProperty("SmartNavigator.address")+" "+_1792.person.formattedAddress;
var _179c="<li class=\"appSearchItem-ListItem person\">";
_179c+="<span tabindex=\"0\" role=\"button\" class=\"appSearchItem person dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox "+_1795+_1797+" "+_1796+_1798+"\""+" data-concernRole=\""+_1792.person.concernRoleId+"\" >";
_179c+="<span class=\"itemIcon personPhoto\" aria-hidden=\"true\">"+"<img draggable=\"false\" alt=\"Person image\" src=\""+_1792.person.personPhotoURL+"\" /></span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine person\" aria-label=\""+_1799+"\" >"+_1792.person.concernRoleName+"</span>"+"<span class=\"itemDescSecondLine address\" aria-label=\""+_179a+"\">"+_1792.person.formattedDateOfBirth+"</span>"+"<span class=\"itemDescSecondLine address\" aria-label=\""+_179b+"\">"+_1792.person.formattedAddress+"</span></span>";
_179c+="</span>";
_179c+="</li>";
var _179d=_1743.toDom(_179c);
if(_1795=="active"){
on(_179d,["click","keyup"],lang.hitch(this,function(event){
this._onItemClick(event,_1792,_1794);
}));
}
on(_179d,["keyup"],lang.hitch(this,function(event){
this._onItemKeyNavigation(_179d,event);
}));
on(_179d,["focus"],lang.hitch(this,function(event){
this._destroyPersonItemsContextMenu(event);
}));
_1743.place(_179d,_1793,"last");
if(_1792.person.items&&!_1792.person.restrictedIndOpt){
_1792.person.items.forEach(function(item,index){
item.person=lang.clone(_1792.person);
item.person.items=[];
item.first=(index===0);
item.last=(index===_1792.person.items.length-1);
this._createResultRow(item,_1793,"Action_Person",_1794);
},this);
}
return _179d;
},_createPersonActionRow:function(item,_179e,_179f,_17a0){
var _17a1="active";
if(!item.url&&item.url.length===0){
_17a1="inactive";
}
var first=item.first?"first":"";
var last=item.last?"last":"";
var _17a2=_179f?"appSearchItemHistory":"";
var hide=_17a0?"contextMenuItem":"hide";
var _17a3="<li tabindex=\"0\" role=\"button\" aria-live=\"assertive\" class=\"appSearchItem appSearchItem-ListItem personAction dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox  "+hide+" "+first+" "+last+" "+_17a1+" "+_17a2+"\" "+"\" >";
_17a3+="<div class=\"personActionItem\">";
_17a3+="<span class=\"itemIcon actionPhoto\">";
if(item.icon){
_17a3+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+item.icon+"\" alt=\"Action Icon\" />";
}else{
_17a3+="<span class=\"actionImage actionEnabled\" /></span>";
}
_17a3+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+item.action.description+"</span>"+"</span>";
_17a3+="</div>";
_17a3+="</li>";
var _17a4=_1743.toDom(_17a3);
if(_17a1=="active"){
on(_17a4,["click","keyup"],lang.hitch(this,function(event){
this._onItemClick(event,item,_179f);
}));
}
on(_17a4,["keyup"],lang.hitch(this,function(event){
this._onItemKeyNavigation(_17a4,event);
}));
_1743.place(_17a4,_179e,"last");
return _17a4;
},_createActionOnlyRow:function(item,_17a5,_17a6){
var _17a7="active";
if(!item.url&&item.url.length===0){
_17a7="inactive";
}
var _17a8=_17a6?"appSearchItemHistory":"";
var title="title=\"";
if(item.action.type==="REQUIRES_PERSON_HISTORY"){
title+=item.action.description+" - "+item.person.concernRoleName+" - "+item.person.formattedDateOfBirth+"\"";
}else{
if(item.action.type&&item.action.type==="REQUIRES_PERSON"){
title+=item.action.description+"\"";
}else{
title+=item.action.description+"\"";
}
}
var _17a9="<li tabindex=\"0\" role=\"button\" class=\"appSearchItem appSearchItem-ListItem actionOnly dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox "+_17a7+" "+_17a8+"\" "+">";
if(item.action.type==="REQUIRES_PERSON_HISTORY"){
_17a9+="<span class=\"itemIcon actionPhoto\">";
if(item.icon){
_17a9+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+item.icon+"\" alt=\"Action Icon\" />";
}else{
_17a9+="<span class=\"actionImage actionEnabled\" /></span>";
}
_17a9+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+item.action.description+"</span>"+"<span class=\"itemDescSecondLine\">"+item.person.concernRoleName+"</span>"+"<span class=\"itemDescSecondLine\">"+item.person.formattedDateOfBirth+"</span>"+"</span>";
}else{
if(item.action.type&&item.action.type==="REQUIRES_PERSON"){
_17a9+="<span class=\"itemIcon actionPhoto\">";
if(item.icon){
_17a9+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+item.icon+"\" alt=\"Action Icon\" />";
}else{
_17a9+="<span class=\"actionImage actionDisabled\" /></span>";
}
_17a9+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+item.action.description+"</span>"+"<span class=\"itemDescSecondLine infoRequired\"> ["+this.bundle.getProperty("SmartNavigator.person.required")+"] </span>"+"</span>";
}else{
_17a9+="<span class=\"itemIcon actionPhoto\">";
if(item.icon){
_17a9+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+item.icon+"\" alt=\""+this.bundle.getProperty("SmartNavigator.action.icon")+"\" />";
}else{
_17a9+="<span class=\"actionImage actionEnabled\" /></span>";
}
_17a9+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescSingleLine action\">"+item.action.description+"</span>"+"</span>";
}
}
_17a9+="</li>";
var _17aa=_1743.toDom(_17a9);
if(_17a7=="active"){
on(_17aa,["click","keyup"],lang.hitch(this,function(event){
this._onItemClick(event,item,_17a6);
}));
}
on(_17aa,["keyup"],lang.hitch(this,function(event){
this._onItemKeyNavigation(_17aa,event);
}));
on(_17aa,["focus"],lang.hitch(this,function(event){
this._destroyPersonItemsContextMenu(event);
}));
_1743.place(_17aa,_17a5,"last");
return _17aa;
},_createPersonItemsContextMenuButton:function(){
var _17ab=this;
query(".appSearchItem.person:not(.restrictedPerson):not(.hide)",_17ab.applicationSearchDiv[0]).forEach(function(item){
if(query(".personItems",item.parentElement).length===0){
var _17ac=true;
_17ab.searchResults.keywords.forEach(function(_17ad){
if(_17ad.type!=="NO_REQUIREMENT"){
_17ac=false;
}
});
if(_17ac){
return false;
}
var _17ae=_17ab.hasIos?"SmartNavigator.button.to.toggle.items.ios":"SmartNavigator.button.to.toggle.items";
var _17af=_1743.toDom("<span tabindex=\"0\" aria-label=\""+_17ab.bundle.getProperty(_17ae)+"\" role=\"button\" aria-expanded=\"false\" class=\"personItems\" "+"data-concernrole=\""+item.dataset.concernrole+"\">"+"</span>");
_1743.place(_17af,item,"after");
_1745.add(item,"hasPersonItemBtn");
on(_17af,["click"],lang.hitch(_17ab,_17ab._createPersonItemsContextMenu,item,_17af));
if(!_17ab.hasIos){
on(_17af,[mouse.enter,"keyup"],lang.hitch(_17ab,_17ab._createPersonItemsContextMenu,item,_17af));
}
on(_17af,[mouse.leave],lang.hitch(_17ab,_17ab._destroyPersonItemsContextMenu));
on(_17af,["blur"],function(e){
_17ab._destroyPersonItemsContextMenu(e);
_17ab._hideSearchBoxIfNotFocused(e);
});
}
});
},_createPersonItemsContextMenu:function(item,_17b0,event){
if(event.type==="keyup"&&(event.keyCode!==keys.ENTER&&event.keyCode!==keys.SPACE)){
return;
}
var _17b1=this;
var _17b2=false;
if(event.type==="click"||event.type==="keyup"){
_17b2=true;
_1745.add(_17b0,"clicked");
event.preventDefault();
event.stopPropagation();
}
if(_17b1._showingAvailableKeywordsList){
_17b1._hideAvailableKeywordsList();
}
_17b1.personInContextMenu=_17b1.searchResults.people.filter(function(_17b3){
return _17b3.person.concernRoleId===item.dataset.concernrole;
})[0];
var _17b4=query(".appSearchItemContext",_17b1.applicationSearchDiv[0])[0];
if(!_17b4||!_1745.contains(_17b4,"clicked")||_17b2){
_1743.destroy(_17b4);
if(_17b2){
query(".personItems.clicked",_17b1.applicationSearchDiv[0]).forEach(function(_17b5){
if(_17b5.dataset.concernrole!==_17b1.personInContextMenu.person.concernRoleId){
_1745.remove(_17b5,"clicked");
}
});
}
var _17b6=query(".search-input-controls .text",_17b1.applicationSearchDiv[0])[0];
var _17b7=query(".appSearchItemsOuterContainer",_17b1.applicationSearchDiv[0])[0];
var _17b8=_17b7.scrollHeight>_17b7.clientHeight?" scrollbars ":"";
var _17b9=_17b2?" clicked ":"";
var _17ba=_1743.toDom("<ul role=\"status\" class=\"appSearchItemContext"+_17b9+_17b8+"\" "+"style=\"opacity: 0;\" data-concernrole=\""+_17b1.personInContextMenu.person.concernRoleId+"\"></ul>");
_1743.place(_17ba,_17b0,"after");
var _17bb=_17b0.getBoundingClientRect();
_1742.set(_17ba,"top",_17bb.top+"px");
var _17bc=util.isRtlMode();
if(_17bc!=null){
_1742.set(_17ba,"left",(_17bb.left-236)+"px");
}else{
_1742.set(_17ba,"left",(_17bb.left+40)+"px");
}
var _17bd=Math.max(document.documentElement.clientHeight,window.innerHeight||0);
var _17be=_17ba.offsetTop;
var _17bf=_17bd-50-_17be;
_1742.set(_17ba,"max-height",(_17bf)+"px");
fx.fadeIn({node:_17ba}).play();
lang.hitch(_17b1,_17b1._createPersonItemsContextMenuRows,_17b1.personInContextMenu,_17ba)();
var _17c0=_1742.get(_17ba,"height");
if(_17c0>=_17bf){
_1745.add(_17ba,"scrollbars");
}
on(_17ba,[mouse.enter,"focus"],function(event){
_17b1.destroyContextMenu=false;
});
on(_17ba,[mouse.leave,"blur","keyup"],lang.hitch(_17b1,_17b1._destroyPersonItemsContextMenu));
}
},_destroyPersonItemsContextMenu:function(event){
if(event.type==="keyup"){
if(event.keyCode!==keys.ESCAPE&&event.keyCode!==keys.LEFT_ARROW&&event.keyCode!==keys.RIGHT_ARROW){
return;
}
var isRTL=util.isRtlMode();
if(isRTL&&event.keyCode===keys.LEFT_ARROW){
return;
}else{
if(!isRTL&&event.keyCode===keys.RIGHT_ARROW){
return;
}
}
event.preventDefault();
event.stopPropagation();
}
var _17c1=this;
var _17c2=query(".appSearchItemContext",_17c1.applicationSearchDiv[0]);
if(_17c2.length>0&&(!_1745.contains(_17c2[0],"clicked"))||event.type==="keyup"||event.type==="focus"){
if(event.type==="keyup"){
var _17c3=null;
query(".appSearchItem").forEach(function(item){
if(item.dataset.concernrole===_17c2[0].dataset.concernrole){
_17c3=item;
}
});
if(_17c3){
_17c3.focus();
}
}
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"clicked");
});
_17c1.destroyContextMenu=true;
setTimeout(function(){
if(_17c1.destroyContextMenu){
_17c2.forEach(function(_17c4){
fx.fadeOut({node:_17c4,onEnd:function(){
_1743.destroy(_17c4);
}}).play();
});
}
},25);
}
},_createPersonItemsContextMenuRows:function(_17c5,_17c6){
if(_17c5.person.items&&_17c5.person.items.length>0){
this._destroyPersonContextSpinningLoader(_17c6);
_17c5.person.items.forEach(function(item,index){
item.rendered=false;
item.person=lang.clone(_17c5.person);
item.person.items=[];
item.first=(index===0);
item.last=(index===_17c5.person.items.length-1);
this._createResultRow(item,_17c6,"Action_Person",false,true);
},this);
if(this.hasIos){
_17c6.focus();
}
if(this.personItemsContextMenuCreationCallback){
this.personItemsContextMenuCreationCallback(_17c6);
}
}else{
if(_17c5.person.itemsLazyLoad){
this._createPersonContextSpinningLoader(_17c6);
this._loadPeopleItems([_17c5]).then(lang.hitch(this,function(){
this._createPersonItemsContextMenuRows(_17c5,_17c6);
}));
}else{
this._destroyPersonContextSpinningLoader(_17c6);
this._createPersonContextNoResultsMessage(_17c6);
if(this.personItemsContextMenuCreationCallback){
this.personItemsContextMenuCreationCallback(_17c6);
}
}
}
},_createResultRowHeader:function(label){
if(label&&label.length>0){
var _17c7=query(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _17c8="<span class=\"appSearchItemSeparatorLabel\" id=\"resultRowHeaderId\" aria-owns=\"applicationSearchResultListId\" aria-live=\"assertive\">";
_17c8+=label;
_17c8+="</span>";
var _17c9=_1743.toDom(_17c8);
_1743.place(_17c9,_17c7,"first");
}
},_showHiddenResultRows:function(){
query(".appSearchItem.hide",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"hide");
});
},_hideResultList:function(){
query(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","none");
});
query(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","none");
});
if(this._showingAvailableKeywordsList){
this._hideAvailableKeywordsList();
}
query(".availableKeywordsListContainer",this.applicationSearchDiv[0]).forEach(function(item){
_1745.add(item,"hide");
});
query(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","none");
});
query(".moreItems",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","none");
});
query(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","none");
});
query(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"clicked");
});
},_showResultList:function(){
var items=query(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]);
items.forEach(function(item){
_1742.set(item,"display","block");
});
if(items.length>0){
_1745.remove(this.applicationSearchDiv[0],"application-search-items-list");
_1745.add(this.applicationSearchDiv[0],"application-search-items-list");
}
query(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","inline-block");
});
query(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","block");
});
query(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","block");
});
query(".moreItems",this.applicationSearchDiv[0]).forEach(function(item){
_1742.set(item,"display","block");
});
this._updateSearchResultsScreenReadersInfo();
},_createMoreRecordsRow:function(_17ca){
query(".paginationContainer",this.applicationSearchDiv[0]).forEach(_1743.destroy);
var _17cb="<div role=\"button\" tabindex=\"0\" class=\"moreItems\" aria-label=\""+this.bundle.getProperty("SmartNavigator.more.results")+"\">"+this.bundle.getProperty("SmartNavigator.more.results")+"</div>";
var _17cc=_1743.toDom("<div class=\"paginationContainer\" role=\"application\"></div>");
var _17cd=_1743.toDom(_17cb);
on(_17cd,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(_17cd,["focus"],lang.hitch(this,function(event){
this._destroyPersonItemsContextMenu(event);
}));
on(_17cd,["click","keyup"],lang.hitch(this,function(event){
var _17ce=event.type==="keyup";
if(_17ce&&(event.keyCode!==keys.ENTER&&event.keyCode!==keys.SPACE)){
this._onItemKeyNavigation(_17cd,event);
return;
}
this.paginationStart+=6;
this._renderResults(_17ce);
if(!_17ce){
this.searchInputField.focus();
}
event.preventDefault();
event.stopPropagation();
}));
_1743.place(_17cc,_17ca,"after");
return _1743.place(_17cd,_17cc,"first");
},_createKeywordTagsContainer:function(){
var _17cf=_1743.toDom("<div class=\"appSearchKeywords\" role=\"application\" aria-live=\"assertive\"></div>");
var _17d0=this.appBannerComboBoxDiv[0];
if(_17d0){
_1743.place(_17cf,_17d0,"before");
}else{
_1743.place(_17cf,this.searchIconDiv[0],"after");
}
var _17d1=this._showingAvailableKeywordsList?"itemsDisplayed":"";
var _17d2=_1743.toDom("<span role=\"button\" title=\""+this.bundle.getProperty("SmartNavigator.keywords.info")+"\" aria-label=\""+this.bundle.getProperty("SmartNavigator.keywords.info")+"\" tabindex=\"0\" class=\"appSearchKeywordsListButton "+_17d1+"\">"+this.bundle.getProperty("SmartNavigator.keywords")+"</span>");
_1743.place(_17d2,_17cf,"first");
on(_17d2,["click","keyup"],lang.hitch(this,this._onAvailableKeywordsListButtonClick,_17cf));
on(_17d2,"keydown",lang.hitch(this,this._onKeywordsDownArrowKey));
on(_17d2,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
if(!this.searchResultsScreenReadersInfo){
this.searchResultsScreenReadersInfo=_1743.toDom("<span class=\"searchResultsScreenReadersInfo hidden\" id=\"searchResultsScreenReadersInfoId\"></span>");
_1743.place(this.searchResultsScreenReadersInfo,_17cf,"last");
}
this._fixQuickSearchHeight();
},_updateSearchResultsScreenReadersInfo:function(){
if(!this.searchResultsScreenReadersInfo){
this.searchResultsScreenReadersInfo=query(".searchResultsScreenReadersInfo",this.applicationSearchDiv[0])[0];
}
var _17d3=query(".appSearchItem:not(.hide)").length;
if(this._isShowingHistory()){
this.searchResultsScreenReadersInfo.innerHTML=this.bundle.getProperty("SmartNavigator.rows.displayed.recent.search",[_17d3]);
}else{
this.searchResultsScreenReadersInfo.innerHTML=this.bundle.getProperty("SmartNavigator.rows.displayed",[_17d3]);
}
},_destroyKeywordTagsContainer:function(){
query(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(_1743.destroy);
this._hideAvailableKeywordsList();
},_createKeywordTags:function(_17d4,_17d5){
var _17d6=[];
var _17d7=false;
var _17d8=query(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _17d9=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
var _17da=12;
if(_1745.contains(_17d9,"itemsDisplayed")){
_17da=0;
}
var _17db=240-(_17d9.offsetWidth+_17da);
if(_17d4.length>0){
var _17dc=_1743.toDom("<span class=\"appSearchKeywordsContainer\"></span>");
_1743.place(_17dc,_17d8,"last");
_17d4.forEach(lang.hitch(this,function(_17dd,index,array){
var _17de=query(".appSearchKeywordTag",this.applicationSearchDiv[0]).reduce(function(width,tag){
width+=parseInt(_1742.getComputedStyle(tag).width)+22;
return width;
},0);
var _17df=(index===array.length-1);
var _17e0="<span tabindex=\"0\" role=\"button\" aria-label=\""+this.bundle.getProperty("SmartNavigator.keyword.identified",[_17dd.description])+"\" aria-owns=\"applicationSearchResultListId\" aria-live=\"assertive\" class=\"appSearchKeywordTag hidden "+(_17df?"last":"")+"\" ";
_17e0+="data-terms=\""+_17dd.terms+"\" ";
_17e0+=">";
_17e0+=_17dd.description;
_17e0+="</span>";
var tag=_1743.toDom(_17e0);
_1743.place(tag,_17dc,"last");
var _17e1=parseInt(_1742.getComputedStyle(tag).width);
if(_17de+_17e1>_17db){
_17d7=true;
}
_1742.set(_17dc,"maxWidth",_17db+"px");
_17d6.push(tag);
on(tag,"click",lang.hitch(this,function(event){
this._destroyKeywordTag(_17d5,event,true);
this.searchInputField.focus();
}));
on(tag,"keyup",lang.hitch(this,function(event){
if(event.keyCode===keys.ENTER||event.keyCode===keys.SPACE){
this._destroyKeywordTag(_17d5,event,true);
this.searchInputField.focus();
}
this._onItemKeyNavigation(tag,event);
}));
}));
_17d6.forEach(function(tag){
_1745.remove(tag,"hidden");
});
}
},_destroyKeywordTag:function(_17e2,event,_17e3,_17e4){
if(event.type==="keyup"&&(event.keyCode!==keys.ENTER&&event.keyCode!==keys.SPACE)){
return;
}
if(_17e3){
event.preventDefault();
event.stopPropagation();
}
var terms=_17e4||event.target.dataset.terms;
var _17e5=terms.replace(/[.*+?^${}()|[\]\\]/g,"\\$&");
var _17e6="(\\b)*("+_17e5+")+(\\b)*(\\s)*";
var _17e7=this.searchInputField.value;
if(_17e2.length>0){
var _17e8=_17e7.split(_17e2[0]);
var init=_17e8[0].replace(new RegExp(_17e6),"");
var end="";
if(_17e8.length>0){
end=_17e8.slice(1).join("").replace(new RegExp(_17e6),"");
}
this.searchInputField.value=(init+_17e2[0]+end).trim();
}else{
this.searchInputField.value=(_17e7.replace(new RegExp(_17e6),"")).trim();
}
if(this.searchInputField.value){
this._doQuickSearch(this.searchInputField.value);
}
},_onAvailableKeywordsListButtonClick:function(_17e9,event,_17ea){
if(event.type==="keyup"&&(event.keyCode!==keys.ENTER&&event.keyCode!==keys.SPACE)){
return;
}
event.preventDefault();
event.stopPropagation();
this._destroyVisibleContextMenu();
if(this._showingAvailableKeywordsList&&!_17ea){
this._hideAvailableKeywordsList();
}else{
if(!this.availableKeywordsList){
var _17eb=this._simpleAccess.makeRequest("SmartNavigatorFacade","listSearchTargets");
this.quickSearchXHRPromises.push(_17eb);
_17eb.then(lang.hitch(this,function(data){
this.availableKeywordsList=data[0].dtls;
this._showingAvailableKeywordsList=true;
this._createAvailableKeywordsList(_17e9,event.target);
_1745.add(query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0],"itemsDisplayed");
this._voiceOverFocusOnFirstKeyword();
}),lang.hitch(this,this._ajaxErrorCallback));
}else{
this._showAvailableKeywordsList();
this._voiceOverFocusOnFirstKeyword();
}
}
},_voiceOverFocusOnFirstKeyword:function(){
if(this.hasIos){
keywordTerm=query(".keywordTerm",this.applicationSearchDiv[0])[0];
setTimeout(function(){
keywordTerm.focus();
},400);
}
},_createAvailableKeywordsList:function(_17ec,_17ed){
if(!_17ec){
_17ec=query(".appSearchKeywords",this.applicationSearchDiv[0])[0];
}
var _17ee=_1743.toDom("<div class=\"availableKeywordsListContainer\"></div>");
_1743.place(_17ee,_17ec,"last");
var _17ef=this.hasIos?"aria-hidden=true":" aria-live=\"assertive\" ";
var _17f0=_1743.toDom("<div class=\"hidden availableKeywordsListDescription\" "+_17ef+" >"+this.bundle.getProperty("SmartNavigator.keywords.list.description")+"</div>");
_1743.place(_17f0,_17ee,"last");
this._setKeywordsListPosition(_17ed,_17ee);
var _17f1=_1743.toDom("<div class=\"availableKeywordsListInnerContainer\"></div>");
_1743.place(_17f1,_17ee,"last");
this.availableKeywordsList.forEach(function(_17f2){
var _17f3=_1743.toDom("<div class=\"availableKeyword\"></div>");
var _17f4=_1743.toDom("<span class=\"keywordTarget\" >"+_17f2.description+"</span>");
var _17f5=_1743.toDom("<span class=\"keywordTerms\"></span>");
_17f2.terms.split(",").forEach(function(term,index,array){
var _17f6=_1743.toDom("<pre>"+term+"</pre>").innerHTML;
var _17f7=(index===array.length-1)?"":", <br />";
var _17f8=_1743.toDom("<li class=\"keywordTerm\" role=\"button\" aria-label=\""+this.bundle.getProperty("SmartNavigator.available.keyword",[_17f6,_17f2.description])+"\" tabindex=\"0\">"+_17f6+_17f7+"</li>");
on(_17f8,["click","keyup"],lang.hitch(this,function(evt){
if(evt.type==="keyup"&&(evt.keyCode!==keys.ENTER&&evt.keyCode!==keys.SPACE)){
return;
}
this.searchInputField.value=this.searchInputField.value.trim()+" "+_17f6;
this.searchInputField.focus();
this._hideAvailableKeywordsList();
this._doQuickSearch(this.searchInputField.value);
}));
on(_17f8,"keydown",lang.hitch(this,function(event){
this._onKeywordsDownArrowKey(event);
this._onKeywordsUpArrowKey(event);
}));
on(_17f8,"blur",lang.hitch(this,this._hideKeywordsOnBlur));
_1743.place(_17f8,_17f5,"last");
},this);
_1743.place(_17f4,_17f3,"first");
_1743.place(_17f5,_17f3,"last");
_1743.place(_17f3,_17f1,"last");
},this);
on(_17ee,"blur",lang.hitch(this,this._hideKeywordsOnBlur));
return _17ee;
},_setKeywordsListPosition:function(_17f9,_17fa){
var _17fb=_17f9.getBoundingClientRect();
_1742.set(_17fa,"top",(_17fb.top+10)+"px");
var _17fc=util.isRtlMode();
if(_17fc!=null){
_1742.set(_17fa,"left",65+"px");
}else{
_1742.set(_17fa,"left",(_17fb.left+65)+"px");
}
},_hideKeywordsOnBlur:function(event){
if(event.relatedTarget&&!this._isElementPartOfContainer(event.relatedTarget,"availableKeywordsListContainer")){
var _17fd=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_17fd){
_17fd.focus();
}
this._hideAvailableKeywordsList();
}
},_hideAvailableKeywordsList:function(){
var _17fe=query(".availableKeywordsListContainer",this.applicationSearchDiv[0])[0];
if(_17fe){
_1745.add(_17fe,"hide");
}
var _17ff=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_17ff){
_1745.remove(_17ff,"itemsDisplayed");
}
this._showingAvailableKeywordsList=false;
this._fixQuickSearchHeight();
},_showAvailableKeywordsList:function(){
var _1800=query(".availableKeywordsListContainer",this.applicationSearchDiv[0])[0];
var _1801=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(!_1800){
_1800=this._createAvailableKeywordsList(null,_1801);
}
this._setKeywordsListPosition(_1801,_1800);
_1745.remove(_1800,"hide");
_1745.add(_1801,"itemsDisplayed");
var _1802=query(".availableKeywordsListDescription",this.applicationSearchDiv[0])[0];
if(_1802){
_1802.innerHTML=_1802.innerHTML;
}
this._showingAvailableKeywordsList=true;
this._fixQuickSearchHeight();
},_createMessages:function(){
var _1803="";
var _1804=null;
if(this.searchResults.messages.info){
_1803=this.searchResults.messages.info;
}else{
if(this.searchResults.messages.unmapped.pages.length>0){
_1803=this.bundle.getProperty("SmartNavigator.no.tabs.configured",[this.searchResults.messages.unmapped.pages.join()]);
}else{
if(this.searchResults.messages.moreThanOneTab.pages.length>0){
_1803=this.bundle.getProperty("SmartNavigator.more.than.one.tab",[this.searchResults.messages.moreThanOneTab.pages.join()]);
}
}
}
if(_1803.length>0){
_1804=this._createMessage(_1803,true);
}
return _1804;
},_createMessage:function(_1805,_1806,_1807){
query(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(_1743.destroy);
var _1808=false;
if(this.searchResults&&this.searchResults.keywords&&this.searchResults.keywords.length>0){
_1808=true;
}
var _1809=_1806?" info ":" error ";
var _180a=_1743.toDom("<div role='alert' class='appSearchMessage topMessage "+_1809+"'><span>"+_1805+"</span></div>");
var _180b=query(".appSearchKeywords",this.applicationSearchDiv[0])[0];
_1743.place(_180a,_180b,"after");
on(_180a,"blur",lang.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(_180a,["keyup"],lang.hitch(this,function(event){
this._onItemKeyNavigation(_180a,event);
}));
return _180a;
},_isPageMappedToATab:function(url,page,_180c){
var _180d=new _174a();
var _180e={isMapped:true};
var _180f=new curam.ui.PageRequest(url);
var _1810=lang.hitch(this,this._pageNotMappedToTabErrorCallback,_180f.pageID,_180e,_180d,page);
var _1811=lang.hitch(this,this._pageMappedToMoreThanOneTabErrorCallback,_180f.pageID,_180e,_180d,page);
var _1812=lang.hitch(this,this._pageMappedToTabCallback,_180f.pageID,_180e,_180d);
if(_1748.indexOf(this.pagesMappedToTab,_180f.pageID)>=0){
_1812();
}else{
if(_1748.indexOf(this.pagesNotMappedToTab,_180f.pageID)>=0){
_1810();
}else{
if(_1748.indexOf(this.pagesMappedToMultipleTabs,_180f.pageID)>=0){
_1811();
}else{
var _1813=null;
if(_180c){
_1813=_180c.split(",").map(function(_1814){
return _1814.trim();
});
}
var _1815=!curam.ui.UIController.checkResolvePage(_180f,_180f.forceRefresh,_1810,_1813,_1811,false,_1812);
if(!_1815){
var _1816={unmappedPageLoader:_1810,preferredTabs:_1813,moreThanOneTabMappedCallback:_1811,shouldLoadPage:false,successCallback:_1812};
curam.ui.UIController.checkPage(_180f,_1816);
}
}
}
}
return _180d;
},_pageNotMappedToTabErrorCallback:function(_1817,_1818,_1819,page){
_1818.isMapped=false;
if(_1748.indexOf(this.pagesNotMappedToTab,_1817)<0){
this.pagesNotMappedToTab.push(_1817);
}
if(_1748.indexOf(this.searchResults.messages.unmapped.pages,page)<0){
this.searchResults.messages.unmapped.pages.push(page);
}
_1819.resolve(_1818);
},_pageMappedToMoreThanOneTabErrorCallback:function(_181a,_181b,_181c,page){
_181b.isMapped=false;
if(_1748.indexOf(this.pagesMappedToMultipleTabs,_181a)<0){
this.pagesMappedToMultipleTabs.push(_181a);
}
if(_1748.indexOf(this.searchResults.messages.moreThanOneTab.pages,page)<0){
this.searchResults.messages.moreThanOneTab.pages.push(page);
}
_181c.resolve(_181b);
},_pageMappedToTabCallback:function(_181d,_181e,_181f){
_181e.isMapped=true;
if(_1748.indexOf(this.pagesMappedToTab,_181d)<0){
this.pagesMappedToTab.push(_181d);
}
_181f.resolve(_181e);
},_createPersonContextNoResultsMessage:function(_1820){
var _1821=_1743.toDom("<div class='appSearchMessage info' tabIndex='0'><span>"+this.bundle.getProperty("SmartNavigator.empty.search")+"</span></div>");
_1743.place(_1821,_1820,"last");
},_createPersonContextSpinningLoader:function(_1822){
var _1823=_1743.toDom("<div class=\"contextMenuLoader\">"+"<div class=\"curam-spinner curam-h1 quick-search contextMenu\"></div></div>");
_1743.place(_1823,_1822,"last");
},_destroyPersonContextSpinningLoader:function(_1824){
query(".contextMenuLoader",_1824).forEach(_1743.destroy);
},_destroyContextMenuIfNecessary:function(_1825){
var _1826=false;
var _1827=query(".appSearchItemContext",this.applicationSearchDiv[0]);
if(_1827.length>0){
if(!this._isElementPartOfContainer(_1825,"appSearchItemContext")){
if(this._isElementPartOfContainer(_1825,"application-search")){
if(_1825.tabIndex===0){
_1825.focus();
}else{
this.searchInputField.focus();
}
}
this._destroyVisibleContextMenu(_1827);
_1826=true;
}
}
return _1826;
},_destroyVisibleContextMenu:function(_1828){
if(!_1828){
_1828=query(".appSearchItemContext",this.applicationSearchDiv[0]);
}
if(_1828.length>0){
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(_1829){
if(_1829.dataset.concernrole===_1828[0].dataset.concernrole){
_1745.remove(_1829,"clicked");
}
});
_1743.destroy(_1828[0]);
}
},_hideKeywordsListIfNecessary:function(_182a,_182b){
var _182c=false;
var _182d=query(".availableKeywordsListContainer:not(.hide)",this.applicationSearchDiv[0]);
if(_182d.length>0){
if(_182b||!this._isElementPartOfContainer(_182a,"availableKeywordsListContainer")){
this._hideAvailableKeywordsList();
_182c=true;
}
if(_182b){
var _182e=query(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_182e){
_182e.focus();
}
}
}
return _182c;
},_isElementPartOfContainer:function(_182f,_1830){
if(_182f.parentElement!=null){
if(_1745.contains(_182f,_1830)){
return true;
}else{
return this._isElementPartOfContainer(_182f.parentElement,_1830);
}
}
return false;
},_onItemClick:function(event,item,_1831){
if(event.type==="keyup"&&(event.keyCode!==keys.ENTER&&event.keyCode!==keys.SPACE)){
return;
}
event.preventDefault();
event.stopPropagation();
var _1832=null;
if(item.action){
_1832=item.action.description;
}else{
if(item.person){
_1832=item.person.concernRoleName;
}
}
this._resetSearchMessages();
item.url=item.url.replace(/&#38;/g,"&");
if(item.isModal){
this._openLinkAndSaveHistory(event,item,_1831,true);
}else{
this._isPageMappedToATab(item.url,_1832,item.preferredTabs).then(lang.hitch(this,function(_1833){
this._openLinkAndSaveHistory(event,item,_1831,false,_1833);
}));
}
},_openLinkAndSaveHistory:function(event,item,_1834,_1835,_1836){
query(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_1743.destroy);
var _1837=this.searchResults.messages.moreThanOneTab.pages.length>0;
if(_1835||(_1836&&_1836.isMapped)||(_1837&&item.preferredTabs)){
this._hideSearchBox();
require(["curam/widget/SearchMultipleTextBoxHookPoints"],lang.hitch(this,function(_1838){
if(_1838.preNavigationHook){
var _1839=topic.subscribe("/smartnavigator/prenavigationhook/completed",lang.hitch(this,function(){
this._openLinkAndSaveHistoryPostHook(event,item,_1834,_1835,_1836);
_1839.remove();
}));
var data={url:item.url};
if(item.person){
data.concernRoleId=item.person.concernRoleId;
}
_1838.preNavigationHook(data);
}else{
this._openLinkAndSaveHistoryPostHook(event,item,_1834,_1835,_1836);
}
}));
}else{
var _183a=this._createMessages();
if(_183a){
_183a.focus();
}
this._fixQuickSearchHeight();
}
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"clicked");
});
},_openLinkAndSaveHistoryPostHook:function(event,item,_183b,_183c,_183d){
if(_183c){
tab.getTabController().handleLinkClick(item.url,{});
}else{
var _183e=null;
if(item.preferredTabs){
_183e=item.preferredTabs.split(",").map(function(_183f){
return _183f.trim();
});
}
var _1840=new curam.ui.PageRequest(item.url);
curam.ui.UIController.handlePageRequest(_1840,_183e);
}
if(!_183b){
var _1841=lang.clone(item);
delete _1841.rendered;
delete _1841.first;
delete _1841.last;
if(!_1841.action){
delete _1841.action;
}
if(!_1841.person){
delete _1841.person;
}else{
delete _1841.person.items;
delete _1841.person.formattedDateOfBirth;
}
var _1842=this._simpleAccess.makeRequest("SmartNavigatorFacade","saveUserHistoryItem",_1841);
this.quickSearchXHRPromises.push(_1842);
_1842.then(function(){
},function(err){
debug.log("Error Saving Search History",err);
});
}
},_onItemKeyNavigation:function(row,event){
if(event.keyCode===keys.DOWN_ARROW){
this._onItemDownArrowKey(row,event);
}else{
if(event.keyCode===keys.UP_ARROW){
this._onItemUpArrowKey(row,event);
}else{
if(event.keyCode===keys.RIGHT_ARROW){
if(util.isRtlMode()){
this._onItemLeftArrowKey(row,event);
}else{
this._onItemRightArrowKey(row,event);
}
}else{
if(event.keyCode===keys.LEFT_ARROW){
if(util.isRtlMode()){
this._onItemRightArrowKey(row,event);
}else{
this._onItemLeftArrowKey(row,event);
}
}
}
}
}
},_onItemDownArrowKey:function(row,event){
event.preventDefault();
event.stopPropagation();
if(_1745.contains(event.target,"topMessage")){
this._setFocusOnFirstResultRow();
}else{
if(_1745.contains(event.target,"appSearchKeywordTag")||_1745.contains(event.target,"appSearchDropDownButton")){
this._setFocusOnFirstResultRow();
}else{
var _1843=this._findSiblingElement(row,true,"appSearchItem-ListItem","hide");
if(_1843){
this._focusAppSearchRowItem(_1843);
}else{
if(!_1745.contains(event.target,"contextMenuItem")&&!_1745.contains(event.target,"appSearchMessage")){
var _1844=query(".moreItems",this.applicationSearchDiv[0]);
if(_1844.length>0){
_1844[0].focus();
}
}
}
}
}
},_onItemUpArrowKey:function(row,event){
event.preventDefault();
event.stopPropagation();
if(_1745.contains(event.target,"appSearchKeywordTag")||_1745.contains(event.target,"appSearchDropDownButton")){
this.searchInputField.focus();
return;
}
var _1845=this._findSiblingElement(row,false,"appSearchItem-ListItem","hide");
if(_1845){
this._focusAppSearchRowItem(_1845);
}else{
if(_1745.contains(event.target,"moreItems")){
var items=query(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
this._focusAppSearchRowItem(items[items.length-1]);
}else{
if(!_1745.contains(event.target,"contextMenuItem")){
var _1846=query(".appSearchDropDownButton",this.applicationSearchDiv[0]);
if(_1846.length>0){
_1846[0].focus();
}else{
var tags=query(".appSearchKeywordTag",this.applicationSearchDiv[0]);
if(tags.length>0){
tags[0].focus();
}else{
this.searchInputField.focus();
}
}
}
}
}
},_onItemRightArrowKey:function(row,event){
event.preventDefault();
event.stopPropagation();
if(_1745.contains(event.target,"appSearchKeywordTag")){
var _1847=this._findSiblingElement(event.target,true,"appSearchKeywordTag");
if(_1847){
_1847.focus();
}
}
var _1848=query(".personItems",row);
if(_1848.length>0){
this.personItemsContextMenuCreationCallback=function(_1849){
var items=query(".contextMenuItem",_1849);
if(items.length>0){
items[0].focus();
}else{
query(".appSearchMessage",_1849)[0].focus();
}
this.personItemsContextMenuCreationCallback=null;
};
_1848[0].click();
}
},_onItemLeftArrowKey:function(row,event){
if(_1745.contains(event.target,"appSearchKeywordTag")){
var _184a=this._findSiblingElement(event.target,false,"appSearchKeywordTag");
if(_184a){
_184a.focus();
}
}
},_focusAppSearchRowItem:function(row){
if(_1745.contains(row,"appSearchItem")){
row.focus();
}else{
var _184b=query(".appSearchItem",row)[0];
if(_184b){
_184b.focus();
}
}
},_findSiblingElement:function(row,_184c,_184d,_184e){
var _184f=_184c?row.nextElementSibling:row.previousElementSibling;
while(_184f){
if(!_1745.contains(_184f,_184d)){
return null;
}
if(!_184e||!_1745.contains(_184f,_184e)){
return _184f;
}
_184f=_184c?_184f.nextElementSibling:_184f.previousElementSibling;
}
},_setFocusOnFirstResultRow:function(){
var items=query(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(items.length>0){
items[0].focus();
}
},_onPaste:function(evt){
var _1850=evt.clipboardData||window.clipboardData;
var _1851=_1850.getData("Text");
var _1852=(_1851&&_1851.length>0)||this._isSearchInputFieldPopulated();
this._enableOrDisableSearchLink(evt,_1852);
},_givenFocus:function(){
var _1853=this;
var ua=navigator.userAgent.toLowerCase();
var _1854=/trident/.test(ua)?true:false;
if(query(".appSearchItemContext",this.applicationSearchDiv[0]).length>0){
return;
}
if(!this.spinnerDiv){
this._createSpinnerDiv();
}
if(this.searchTextDiv==null){
this.searchTextDiv=query(".search-input-controls div.dijitInputField",this.applicationSearchDiv[0]);
}
if(this.originalInputColor==null){
this.originalInputColor="#152935";
}
if(this.appBannerComboBoxDiv==null||this.appBannerComboBoxDiv.length==0){
this.appBannerComboBoxDiv=query(".appBannerComboBox",this.applicationSearchDiv[0]);
if(this.appBannerComboBoxDiv==null||this.appBannerComboBoxDiv.length==0){
this.appBannerComboBoxDiv=query(".multiple-search-banner select",this.applicationSearchDiv[0]);
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_1745.add(this.appBannerComboBoxDiv[0],"appBannerSelect");
}
}
}
_1742.set(this.searchInputField,"color",this.originalInputColor);
_1745.remove(this.searchInputField,"input-placeholder-closed");
_1745.add(this.searchInputField,"input-placeholder-opened");
var _1855=0;
var _1856=util.isRtlMode();
if(_1856!=null){
_1855=_1744.position(this.searchInputField).x-46;
}else{
_1855=_1744.position(this.searchInputField).x-10;
}
if(this.closedHeight==null){
this.closedHeight=_1742.get(this.applicationSearchDiv[0],"height");
}
this.applicationSearchDiv.style({left:_1855+"px"});
_1745.add(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(this.quickSearch){
if(query(".appSearchKeywords",this.applicationSearchDiv[0]).length===0){
this._createKeywordTagsContainer();
}
}else{
this._destroyKeywordTagsContainer();
}
var _1857=this.quickSearch?40:100;
if(_1742.get(this.applicationSearchDiv[0],"height")!=_1857){
if(this.searchOptionsDiv.length>0){
_1742.set(this.searchOptionsDiv[0],"display","block");
_1742.set(this.searchOptionsDiv[0],"opacity","0");
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_1742.set(this.appBannerComboBoxDiv[0],"display","block");
}
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
var _1858={node:this.searchOptionsDiv[0]};
fx.fadeIn(_1858).play();
}
}
if(this.backgroundColor==null){
this.backgroundColor=_1742.get(this.searchTextDiv[0],"background-color");
}
if(this.searchTextDiv!=null&&_1742.get(this.searchTextDiv[0],"background-color")!=this.searchOptionsDivOpenedColor){
_1742.set(this.searchTextDiv[0],"background-color","#ffffff");
_1742.set(this.searchControlsDiv[0],"background-color","#ffffff");
_1742.set(this.searchIconDiv[0],"background-color","#ffffff");
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-enabled.svg";
}
if(this.quickSearch){
if(!this.focusNode.value&&!this._isShowingHistory()){
this._destroyResultList();
this._hideDropDown();
this._showSearchHistory();
}
var _1859=query(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0])[0];
if(_1859){
if(_1854){
_1746.set(this.searchInputField,"aria-describedby","applicationSearchResultListId");
}else{
_1746.set(this.searchInputField,"aria-describedby","resultRowHeaderId");
}
}
this._showResultList();
}
this._fixQuickSearchHeight();
this.searchInputField.focus();
setTimeout(function(){
_1853.searchInputField.select();
},5);
var _185a=query(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
if(typeof (this.searchInputField)!="undefined"&&this.searchInputField.value.length>0&&typeof (_185a)!="undefined"&&_185a.children.length>0){
if(_1854){
_1746.set(this.searchInputField,"aria-describedby","applicationSearchResultListId");
}else{
_1746.set(this.searchInputField,"aria-describedby","searchResultsScreenReadersInfoId");
}
}
},_onBlur:function(evt){
if(!this._isSearchInputFieldPopulated()){
this.searchInputField.value="";
}
this._hideSearchBoxIfNotFocused(evt);
},_destroyResultList:function(){
query(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchItemsContainer",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchItem",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchKeywordTag",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".moreItems",this.applicationSearchDiv[0]).forEach(_1743.destroy);
query(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_1745.remove(item,"clicked");
});
_1745.remove(this.applicationSearchDiv[0],"application-search-items-list");
this._fixQuickSearchHeight();
_1745.remove(this.applicationSearchDiv[0],"shadow");
},_autoScrollToNewItems:function(){
if(this.searchResults.hasItems&&this.paginationStart>0){
var _185b=query(".appSearchItemsOuterContainer",this.applicationSearchDiv[0])[0];
_185b.scrollTop=_185b.scrollHeight;
query(".appSearchNewItem",this.applicationSearchDiv[0]).forEach(function(item){
fx.animateProperty({node:item,duration:650,properties:{opacity:{start:".4",end:"1"}}}).play();
_1745.remove(item,"appSearchNewItem");
});
}
},_getAge:function(dob){
if(dob){
var _185c=_174b.parseDate(dob);
return parseInt(((Date.now()-_185c)/(31557600000)));
}else{
return "";
}
},_fixQuickSearchHeight:function(){
this.currentOpenedHeight=this._getOpenedMenuHeight();
_1742.set(this.applicationSearchDiv[0],"height",this.currentOpenedHeight+"px");
},_hideDropDown:function(){
this._fixQuickSearchHeight();
_1745.remove(this.applicationSearchDiv[0],"shadow");
},_isShowingHistory:function(){
return query(".appSearchItemHistory",this.applicationSearchDiv[0]).length>0;
},_resetSearchResults:function(){
this.paginationStart=0;
this._destroyResultList();
this.searchResults={people:[],actionsOnly:[],personQueryTerms:[],messages:{info:null,unmapped:{pages:[]},moreThanOneTab:{pages:[]}},hasItems:false};
},_resetSearchMessages:function(){
this.searchResults.messages={info:null,unmapped:{pages:[]},moreThanOneTab:{pages:[]}};
},_enableOrDisableSearchLink:function(evt,_185d){
if(_185d&&_185d===true){
_1745.remove(this.searchIcon[0],"dijitDisabled");
if(evt.keyCode==keys.ENTER){
this._search();
}
}else{
_1745.add(this.searchIcon[0],"dijitDisabled");
}
},_isQuickSearchOnly:function(){
var _185e=false;
var _185f=false;
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
_185e=true;
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_185f=true;
}
return !_185e&&!_185f;
},_getItemsHeight:function(_1860){
var _1861=this._isQuickSearchOnly();
var _1862=".appSearchItem:not(.hide):not(.contextMenuItem)";
if(_1860){
_1862=".appSearchItem";
}
var _1863=query(_1862,this.applicationSearchDiv[0]).reduce(function(_1864,node){
_1864+=_1742.get(node,"height");
_1864+=_1742.get(node,"padding-top");
_1864+=_1742.get(node,"padding-bottom");
_1864+=_1742.get(node,"margin-top");
_1864+=_1742.get(node,"margin-bottom");
return _1864;
},0);
if(_1861&&_1863>0){
_1863+=3;
}
if(_1863>this.maxItemsHeight){
_1863=this.maxItemsHeight;
}
return _1863;
},_getOpenedMenuHeight:function(){
var _1865=0;
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
if(this.quickSearch){
_1865+=this.appBannerComboBoxDiv[0]?this.menuOpenedFurtherOptionsHeightQuickSearch:this.menuOpenedFurtherOptionsHeightNoCombobox;
}else{
_1865+=this.menuOpenedFurtherOptionsHeight;
}
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_1865+=this.menuOpenedComboboxHeight;
}else{
if(this.quickSearch){
_1865+=this.quickSearchMenuOpenedHeight;
}
}
var _1866=0;
query(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(_1867){
_1866+=this._calculateElementHeight(_1867);
},this);
_1866+=this._getTopMessagesHeight();
_1866+=query(".moreItems",this.applicationSearchDiv[0]).reduce(function(count){
return count+=40;
},0);
if(query(".appSearchItemsOuterContainer.margin",this.applicationSearchDiv[0]).length>0){
_1865+=10;
}
if(query(".appSearchMessage.topMessage.margin",this.applicationSearchDiv[0]).length>0){
_1865+=10;
}
var _1868=this._getItemsHeight(false);
_1865+=_1868+_1866;
var _1869=27;
var _186a=_1865===_1869+this.quickSearchMenuOpenedHeight;
if(_186a){
_1865-=5;
}
_1742.set(this.applicationSearchDiv[0],"height",_1865+"px");
this.currentOpenedHeight=_1865;
return _1865;
},_getTopMessagesHeight:function(){
var _186b=0;
query(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(_186c){
_186b+=_186c.clientHeight+5;
});
return _186b;
},_calculateElementHeight:function(_186d){
var _186e=window.getComputedStyle(_186d);
var _186f=parseFloat(_186e["marginTop"])+parseFloat(_186e["marginBottom"]);
return Math.ceil(_186d.offsetHeight+_186f);
},_enableQuickSearch:function(){
_1745.add(this.applicationSearchDiv[0],"quick-search");
this.quickSearch=true;
if(query(".appSearchKeywords",this.applicationSearchDiv[0]).length===0){
this._createKeywordTagsContainer();
}
},_disableQuickSearch:function(){
this._destroyResultList();
_1745.remove(this.applicationSearchDiv[0],"quick-search");
this.quickSearch=false;
this._destroyKeywordTagsContainer();
},_createSpinnerDiv:function(){
this.spinnerDiv=_1743.toDom("<div class=\"curam-spinner curam-h1 quick-search hide\"></div>");
_1743.place(this.spinnerDiv,this.searchIconDiv[0],"last");
},_showSpinnerDiv:function(){
this.spinnerTimeouts.push(setTimeout(lang.hitch(this,function(){
_1742.set(this.searchIcon[0],"display","none");
_1745.remove(this.spinnerDiv,"hide");
}),500));
},_hideSpinnerDiv:function(){
this.spinnerTimeouts.forEach(function(_1870){
clearTimeout(_1870);
});
this.spinnerTimeouts=[];
_1742.set(this.searchIcon[0],"display","block");
if(this.spinnerDiv){
_1745.add(this.spinnerDiv,"hide");
}
},_cancelPreviousAjaxCalls:function(){
if(this.quickSearchXHRPromises.length>0){
this.quickSearchXHRPromises.forEach(function(_1871){
_1871.cancel();
});
this.quickSearchXHRPromises=[];
}
}});
return _1753;
});
},"dijit/form/_ComboBoxMenuMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/has","dojo/i18n","dojo/i18n!./nls/ComboBox"],function(array,_1872,_1873,has,i18n){
var _1874=_1872("dijit.form._ComboBoxMenuMixin"+(has("dojo-bidi")?"_NoBidi":""),null,{_messages:null,postMixInProperties:function(){
this.inherited(arguments);
this._messages=i18n.getLocalization("dijit.form","ComboBox",this.lang);
},buildRendering:function(){
this.inherited(arguments);
this.previousButton.innerHTML=this._messages["previousMessage"];
this.nextButton.innerHTML=this._messages["nextMessage"];
},_setValueAttr:function(value){
this._set("value",value);
this.onChange(value);
},onClick:function(node){
if(node==this.previousButton){
this._setSelectedAttr(null);
this.onPage(-1);
}else{
if(node==this.nextButton){
this._setSelectedAttr(null);
this.onPage(1);
}else{
this.onChange(node);
}
}
},onChange:function(){
},onPage:function(){
},onClose:function(){
this._setSelectedAttr(null);
},_createOption:function(item,_1875){
var _1876=this._createMenuItem();
var _1877=_1875(item);
if(_1877.html){
_1876.innerHTML=_1877.label;
}else{
_1876.appendChild(_1876.ownerDocument.createTextNode(_1877.label));
}
if(_1876.innerHTML==""){
_1876.innerHTML="&#160;";
}
return _1876;
},createOptions:function(_1878,_1879,_187a){
this.items=_1878;
this.previousButton.style.display=(_1879.start==0)?"none":"";
_1873.set(this.previousButton,"id",this.id+"_prev");
array.forEach(_1878,function(item,i){
var _187b=this._createOption(item,_187a);
_187b.setAttribute("item",i);
_1873.set(_187b,"id",this.id+i);
this.nextButton.parentNode.insertBefore(_187b,this.nextButton);
},this);
var _187c=false;
if(_1878.total&&!_1878.total.then&&_1878.total!=-1){
if((_1879.start+_1879.count)<_1878.total){
_187c=true;
}else{
if((_1879.start+_1879.count)>_1878.total&&_1879.count==_1878.length){
_187c=true;
}
}
}else{
if(_1879.count==_1878.length){
_187c=true;
}
}
this.nextButton.style.display=_187c?"":"none";
_1873.set(this.nextButton,"id",this.id+"_next");
},clearResultList:function(){
var _187d=this.containerNode;
while(_187d.childNodes.length>2){
_187d.removeChild(_187d.childNodes[_187d.childNodes.length-2]);
}
this._setSelectedAttr(null);
},highlightFirstOption:function(){
this.selectFirstNode();
},highlightLastOption:function(){
this.selectLastNode();
},selectFirstNode:function(){
this.inherited(arguments);
if(this.getHighlightedOption()==this.previousButton){
this.selectNextNode();
}
},selectLastNode:function(){
this.inherited(arguments);
if(this.getHighlightedOption()==this.nextButton){
this.selectPreviousNode();
}
},getHighlightedOption:function(){
return this.selected;
}});
if(has("dojo-bidi")){
_1874=_1872("dijit.form._ComboBoxMenuMixin",_1874,{_createOption:function(){
var _187e=this.inherited(arguments);
this.applyTextDir(_187e);
return _187e;
}});
}
return _1874;
});
},"dijit/Tree":function(){
define(["dojo/_base/array","dojo/aspect","dojo/cookie","dojo/_base/declare","dojo/Deferred","dojo/promise/all","dojo/dom","dojo/dom-class","dojo/dom-geometry","dojo/dom-style","dojo/errors/create","dojo/fx","dojo/has","dojo/_base/kernel","dojo/keys","dojo/_base/lang","dojo/on","dojo/topic","dojo/touch","dojo/when","./a11yclick","./focus","./registry","./_base/manager","./_Widget","./_TemplatedMixin","./_Container","./_Contained","./_CssStateMixin","./_KeyNavMixin","dojo/text!./templates/TreeNode.html","dojo/text!./templates/Tree.html","./tree/TreeStoreModel","./tree/ForestStoreModel","./tree/_dndSelector","dojo/query!css2"],function(array,_187f,_1880,_1881,_1882,all,dom,_1883,_1884,_1885,_1886,_1887,has,_1888,keys,lang,on,topic,touch,when,_1889,focus,_188a,_188b,_188c,_188d,_188e,_188f,_1890,_1891,_1892,_1893,_1894,_1895,_1896){
function _1897(d){
return lang.delegate(d.promise||d,{addCallback:function(_1898){
this.then(_1898);
},addErrback:function(_1899){
this.otherwise(_1899);
}});
};
var _189a=_1881("dijit._TreeNode",[_188c,_188d,_188e,_188f,_1890],{item:null,isTreeNode:true,label:"",_setLabelAttr:function(val){
this.labelNode[this.labelType=="html"?"innerHTML":"innerText" in this.labelNode?"innerText":"textContent"]=val;
this._set("label",val);
if(has("dojo-bidi")){
this.applyTextDir(this.labelNode);
}
},labelType:"text",isExpandable:null,isExpanded:false,state:"NotLoaded",templateString:_1892,baseClass:"dijitTreeNode",cssStateNodes:{rowNode:"dijitTreeRow"},_setTooltipAttr:{node:"rowNode",type:"attribute",attribute:"title"},buildRendering:function(){
this.inherited(arguments);
this._setExpando();
this._updateItemClasses(this.item);
if(this.isExpandable){
this.labelNode.setAttribute("aria-expanded",this.isExpanded);
}
this.setSelected(false);
},_setIndentAttr:function(_189b){
var _189c=(Math.max(_189b,0)*this.tree._nodePixelIndent)+"px";
_1885.set(this.domNode,"backgroundPosition",_189c+" 0px");
_1885.set(this.rowNode,this.isLeftToRight()?"paddingLeft":"paddingRight",_189c);
array.forEach(this.getChildren(),function(child){
child.set("indent",_189b+1);
});
this._set("indent",_189b);
},markProcessing:function(){
this.state="Loading";
this._setExpando(true);
},unmarkProcessing:function(){
this._setExpando(false);
},_updateItemClasses:function(item){
var tree=this.tree,model=tree.model;
if(tree._v10Compat&&item===model.root){
item=null;
}
this._applyClassAndStyle(item,"icon","Icon");
this._applyClassAndStyle(item,"label","Label");
this._applyClassAndStyle(item,"row","Row");
this.tree._startPaint(true);
},_applyClassAndStyle:function(item,lower,upper){
var _189d="_"+lower+"Class";
var _189e=lower+"Node";
var _189f=this[_189d];
this[_189d]=this.tree["get"+upper+"Class"](item,this.isExpanded);
_1883.replace(this[_189e],this[_189d]||"",_189f||"");
_1885.set(this[_189e],this.tree["get"+upper+"Style"](item,this.isExpanded)||{});
},_updateLayout:function(){
var _18a0=this.getParent(),_18a1=!_18a0||!_18a0.rowNode||_18a0.rowNode.style.display=="none";
_1883.toggle(this.domNode,"dijitTreeIsRoot",_18a1);
_1883.toggle(this.domNode,"dijitTreeIsLast",!_18a1&&!this.getNextSibling());
},_setExpando:function(_18a2){
var _18a3=["dijitTreeExpandoLoading","dijitTreeExpandoOpened","dijitTreeExpandoClosed","dijitTreeExpandoLeaf"],_18a4=["*","-","+","*"],idx=_18a2?0:(this.isExpandable?(this.isExpanded?1:2):3);
_1883.replace(this.expandoNode,_18a3[idx],_18a3);
this.expandoNodeText.innerHTML=_18a4[idx];
},expand:function(){
if(this._expandDeferred){
return _1897(this._expandDeferred);
}
if(this._collapseDeferred){
this._collapseDeferred.cancel();
delete this._collapseDeferred;
}
this.isExpanded=true;
this.labelNode.setAttribute("aria-expanded","true");
if(this.tree.showRoot||this!==this.tree.rootNode){
this.containerNode.setAttribute("role","group");
}
_1883.add(this.contentNode,"dijitTreeContentExpanded");
this._setExpando();
this._updateItemClasses(this.item);
if(this==this.tree.rootNode&&this.tree.showRoot){
this.tree.domNode.setAttribute("aria-expanded","true");
}
var _18a5=_1887.wipeIn({node:this.containerNode,duration:_188b.defaultDuration});
var def=(this._expandDeferred=new _1882(function(){
_18a5.stop();
}));
_187f.after(_18a5,"onEnd",function(){
def.resolve(true);
},true);
_18a5.play();
return _1897(def);
},collapse:function(){
if(this._collapseDeferred){
return _1897(this._collapseDeferred);
}
if(this._expandDeferred){
this._expandDeferred.cancel();
delete this._expandDeferred;
}
this.isExpanded=false;
this.labelNode.setAttribute("aria-expanded","false");
if(this==this.tree.rootNode&&this.tree.showRoot){
this.tree.domNode.setAttribute("aria-expanded","false");
}
_1883.remove(this.contentNode,"dijitTreeContentExpanded");
this._setExpando();
this._updateItemClasses(this.item);
var _18a6=_1887.wipeOut({node:this.containerNode,duration:_188b.defaultDuration});
var def=(this._collapseDeferred=new _1882(function(){
_18a6.stop();
}));
_187f.after(_18a6,"onEnd",function(){
def.resolve(true);
},true);
_18a6.play();
return _1897(def);
},indent:0,setChildItems:function(items){
var tree=this.tree,model=tree.model,defs=[];
var _18a7=tree.focusedChild;
var _18a8=this.getChildren();
array.forEach(_18a8,function(child){
_188e.prototype.removeChild.call(this,child);
},this);
this.defer(function(){
array.forEach(_18a8,function(node){
if(!node._destroyed&&!node.getParent()){
tree.dndController.removeTreeNode(node);
function _18a9(node){
var id=model.getIdentity(node.item),ary=tree._itemNodesMap[id];
if(ary.length==1){
delete tree._itemNodesMap[id];
}else{
var index=array.indexOf(ary,node);
if(index!=-1){
ary.splice(index,1);
}
}
array.forEach(node.getChildren(),_18a9);
};
_18a9(node);
if(tree.persist){
var _18aa=array.map(node.getTreePath(),function(item){
return tree.model.getIdentity(item);
}).join("/");
for(var path in tree._openedNodes){
if(path.substr(0,_18aa.length)==_18aa){
delete tree._openedNodes[path];
}
}
tree._saveExpandedNodes();
}
if(tree.lastFocusedChild&&!dom.isDescendant(tree.lastFocusedChild,tree.domNode)){
delete tree.lastFocusedChild;
}
if(_18a7&&!dom.isDescendant(_18a7,tree.domNode)){
tree.focus();
}
node.destroyRecursive();
}
});
});
this.state="Loaded";
if(items&&items.length>0){
this.isExpandable=true;
array.forEach(items,function(item){
var id=model.getIdentity(item),_18ab=tree._itemNodesMap[id],node;
if(_18ab){
for(var i=0;i<_18ab.length;i++){
if(_18ab[i]&&!_18ab[i].getParent()){
node=_18ab[i];
node.set("indent",this.indent+1);
break;
}
}
}
if(!node){
node=this.tree._createTreeNode({item:item,tree:tree,isExpandable:model.mayHaveChildren(item),label:tree.getLabel(item),labelType:(tree.model&&tree.model.labelType)||"text",tooltip:tree.getTooltip(item),ownerDocument:tree.ownerDocument,dir:tree.dir,lang:tree.lang,textDir:tree.textDir,indent:this.indent+1});
if(_18ab){
_18ab.push(node);
}else{
tree._itemNodesMap[id]=[node];
}
}
this.addChild(node);
if(this.tree.autoExpand||this.tree._state(node)){
defs.push(tree._expandNode(node));
}
},this);
array.forEach(this.getChildren(),function(child){
child._updateLayout();
});
}else{
this.isExpandable=false;
}
if(this._setExpando){
this._setExpando(false);
}
this._updateItemClasses(this.item);
var def=all(defs);
this.tree._startPaint(def);
return _1897(def);
},getTreePath:function(){
var node=this;
var path=[];
while(node&&node!==this.tree.rootNode){
path.unshift(node.item);
node=node.getParent();
}
path.unshift(this.tree.rootNode.item);
return path;
},getIdentity:function(){
return this.tree.model.getIdentity(this.item);
},removeChild:function(node){
this.inherited(arguments);
var _18ac=this.getChildren();
if(_18ac.length==0){
this.isExpandable=false;
this.collapse();
}
array.forEach(_18ac,function(child){
child._updateLayout();
});
},makeExpandable:function(){
this.isExpandable=true;
this._setExpando(false);
},setSelected:function(_18ad){
this.labelNode.setAttribute("aria-selected",_18ad?"true":"false");
_1883.toggle(this.rowNode,"dijitTreeRowSelected",_18ad);
},focus:function(){
focus.focus(this.focusNode);
}});
if(has("dojo-bidi")){
_189a.extend({_setTextDirAttr:function(_18ae){
if(_18ae&&((this.textDir!=_18ae)||!this._created)){
this._set("textDir",_18ae);
this.applyTextDir(this.labelNode);
array.forEach(this.getChildren(),function(_18af){
_18af.set("textDir",_18ae);
},this);
}
}});
}
var Tree=_1881("dijit.Tree",[_188c,_1891,_188d,_1890],{baseClass:"dijitTree",store:null,model:null,query:null,label:"",showRoot:true,childrenAttr:["children"],paths:[],path:[],selectedItems:null,selectedItem:null,openOnClick:false,openOnDblClick:false,templateString:_1893,persist:false,autoExpand:false,dndController:_1896,dndParams:["onDndDrop","itemCreator","onDndCancel","checkAcceptance","checkItemAcceptance","dragThreshold","betweenThreshold"],onDndDrop:null,itemCreator:null,onDndCancel:null,checkAcceptance:null,checkItemAcceptance:null,dragThreshold:5,betweenThreshold:0,_nodePixelIndent:19,_publish:function(_18b0,_18b1){
topic.publish(this.id,lang.mixin({tree:this,event:_18b0},_18b1||{}));
},postMixInProperties:function(){
this.tree=this;
if(this.autoExpand){
this.persist=false;
}
this._itemNodesMap={};
if(!this.cookieName&&this.id){
this.cookieName=this.id+"SaveStateCookie";
}
this.expandChildrenDeferred=new _1882();
this.pendingCommandsPromise=this.expandChildrenDeferred.promise;
this.inherited(arguments);
},postCreate:function(){
this._initState();
var self=this;
this.own(on(this.containerNode,on.selector(".dijitTreeNode",touch.enter),function(evt){
self._onNodeMouseEnter(_188a.byNode(this),evt);
}),on(this.containerNode,on.selector(".dijitTreeNode",touch.leave),function(evt){
self._onNodeMouseLeave(_188a.byNode(this),evt);
}),on(this.containerNode,on.selector(".dijitTreeRow",_1889.press),function(evt){
self._onNodePress(_188a.getEnclosingWidget(this),evt);
}),on(this.containerNode,on.selector(".dijitTreeRow",_1889),function(evt){
self._onClick(_188a.getEnclosingWidget(this),evt);
}),on(this.containerNode,on.selector(".dijitTreeRow","dblclick"),function(evt){
self._onDblClick(_188a.getEnclosingWidget(this),evt);
}));
if(!this.model){
this._store2model();
}
this.own(_187f.after(this.model,"onChange",lang.hitch(this,"_onItemChange"),true),_187f.after(this.model,"onChildrenChange",lang.hitch(this,"_onItemChildrenChange"),true),_187f.after(this.model,"onDelete",lang.hitch(this,"_onItemDelete"),true));
this.inherited(arguments);
if(this.dndController){
if(lang.isString(this.dndController)){
this.dndController=lang.getObject(this.dndController);
}
var _18b2={};
for(var i=0;i<this.dndParams.length;i++){
if(this[this.dndParams[i]]){
_18b2[this.dndParams[i]]=this[this.dndParams[i]];
}
}
this.dndController=new this.dndController(this,_18b2);
}
this._load();
this.onLoadDeferred=_1897(this.pendingCommandsPromise);
this.onLoadDeferred.then(lang.hitch(this,"onLoad"));
},_store2model:function(){
this._v10Compat=true;
_1888.deprecated("Tree: from version 2.0, should specify a model object rather than a store/query");
var _18b3={id:this.id+"_ForestStoreModel",store:this.store,query:this.query,childrenAttrs:this.childrenAttr};
if(this.params.mayHaveChildren){
_18b3.mayHaveChildren=lang.hitch(this,"mayHaveChildren");
}
if(this.params.getItemChildren){
_18b3.getChildren=lang.hitch(this,function(item,_18b4,_18b5){
this.getItemChildren((this._v10Compat&&item===this.model.root)?null:item,_18b4,_18b5);
});
}
this.model=new _1895(_18b3);
this.showRoot=Boolean(this.label);
},onLoad:function(){
},_load:function(){
this.model.getRoot(lang.hitch(this,function(item){
var rn=(this.rootNode=this.tree._createTreeNode({item:item,tree:this,isExpandable:true,label:this.label||this.getLabel(item),labelType:this.model.labelType||"text",textDir:this.textDir,indent:this.showRoot?0:-1}));
if(!this.showRoot){
rn.rowNode.style.display="none";
this.domNode.setAttribute("role","presentation");
this.domNode.removeAttribute("aria-expanded");
this.domNode.removeAttribute("aria-multiselectable");
if(this["aria-label"]){
rn.containerNode.setAttribute("aria-label",this["aria-label"]);
this.domNode.removeAttribute("aria-label");
}else{
if(this["aria-labelledby"]){
rn.containerNode.setAttribute("aria-labelledby",this["aria-labelledby"]);
this.domNode.removeAttribute("aria-labelledby");
}
}
rn.labelNode.setAttribute("role","presentation");
rn.labelNode.removeAttribute("aria-selected");
rn.containerNode.setAttribute("role","tree");
rn.containerNode.setAttribute("aria-expanded","true");
rn.containerNode.setAttribute("aria-multiselectable",!this.dndController.singular);
}else{
this.domNode.setAttribute("aria-multiselectable",!this.dndController.singular);
this.rootLoadingIndicator.style.display="none";
}
this.containerNode.appendChild(rn.domNode);
var _18b6=this.model.getIdentity(item);
if(this._itemNodesMap[_18b6]){
this._itemNodesMap[_18b6].push(rn);
}else{
this._itemNodesMap[_18b6]=[rn];
}
rn._updateLayout();
this._expandNode(rn).then(lang.hitch(this,function(){
if(!this._destroyed){
this.rootLoadingIndicator.style.display="none";
this.expandChildrenDeferred.resolve(true);
}
}));
}),lang.hitch(this,function(err){
console.error(this,": error loading root: ",err);
}));
},getNodesByItem:function(item){
if(!item){
return [];
}
var _18b7=lang.isString(item)?item:this.model.getIdentity(item);
return [].concat(this._itemNodesMap[_18b7]);
},_setSelectedItemAttr:function(item){
this.set("selectedItems",[item]);
},_setSelectedItemsAttr:function(items){
var tree=this;
return this.pendingCommandsPromise=this.pendingCommandsPromise.always(lang.hitch(this,function(){
var _18b8=array.map(items,function(item){
return (!item||lang.isString(item))?item:tree.model.getIdentity(item);
});
var nodes=[];
array.forEach(_18b8,function(id){
nodes=nodes.concat(tree._itemNodesMap[id]||[]);
});
this.set("selectedNodes",nodes);
}));
},_setPathAttr:function(path){
if(path.length){
return _1897(this.set("paths",[path]).then(function(paths){
return paths[0];
}));
}else{
return _1897(this.set("paths",[]).then(function(paths){
return paths[0];
}));
}
},_setPathsAttr:function(paths){
var tree=this;
function _18b9(path,nodes){
var _18ba=path.shift();
var _18bb=array.filter(nodes,function(node){
return node.getIdentity()==_18ba;
})[0];
if(!!_18bb){
if(path.length){
return tree._expandNode(_18bb).then(function(){
return _18b9(path,_18bb.getChildren());
});
}else{
return _18bb;
}
}else{
throw new Tree.PathError("Could not expand path at "+_18ba);
}
};
return _1897(this.pendingCommandsPromise=this.pendingCommandsPromise.always(function(){
return all(array.map(paths,function(path){
path=array.map(path,function(item){
return lang.isString(item)?item:tree.model.getIdentity(item);
});
if(path.length){
return _18b9(path,[tree.rootNode]);
}else{
throw new Tree.PathError("Empty path");
}
}));
}).then(function setNodes(_18bc){
tree.set("selectedNodes",_18bc);
return tree.paths;
}));
},_setSelectedNodeAttr:function(node){
this.set("selectedNodes",[node]);
},_setSelectedNodesAttr:function(nodes){
this.dndController.setSelection(nodes);
},expandAll:function(){
var _18bd=this;
function _18be(node){
return _18bd._expandNode(node).then(function(){
var _18bf=array.filter(node.getChildren()||[],function(node){
return node.isExpandable;
});
return all(array.map(_18bf,_18be));
});
};
return _1897(_18be(this.rootNode));
},collapseAll:function(){
var _18c0=this;
function _18c1(node){
var _18c2=array.filter(node.getChildren()||[],function(node){
return node.isExpandable;
}),defs=all(array.map(_18c2,_18c1));
if(!node.isExpanded||(node==_18c0.rootNode&&!_18c0.showRoot)){
return defs;
}else{
return defs.then(function(){
return _18c0._collapseNode(node);
});
}
};
return _1897(_18c1(this.rootNode));
},mayHaveChildren:function(){
},getItemChildren:function(){
},getLabel:function(item){
return this.model.getLabel(item);
},getIconClass:function(item,_18c3){
return (!item||this.model.mayHaveChildren(item))?(_18c3?"dijitFolderOpened":"dijitFolderClosed"):"dijitLeaf";
},getLabelClass:function(){
},getRowClass:function(){
},getIconStyle:function(){
},getLabelStyle:function(){
},getRowStyle:function(){
},getTooltip:function(){
return "";
},_onDownArrow:function(evt,node){
var _18c4=this._getNext(node);
if(_18c4&&_18c4.isTreeNode){
this.focusNode(_18c4);
}
},_onUpArrow:function(evt,node){
var _18c5=node.getPreviousSibling();
if(_18c5){
node=_18c5;
while(node.isExpandable&&node.isExpanded&&node.hasChildren()){
var _18c6=node.getChildren();
node=_18c6[_18c6.length-1];
}
}else{
var _18c7=node.getParent();
if(!(!this.showRoot&&_18c7===this.rootNode)){
node=_18c7;
}
}
if(node&&node.isTreeNode){
this.focusNode(node);
}
},_onRightArrow:function(evt,node){
if(node.isExpandable&&!node.isExpanded){
this._expandNode(node);
}else{
if(node.hasChildren()){
node=node.getChildren()[0];
if(node&&node.isTreeNode){
this.focusNode(node);
}
}
}
},_onLeftArrow:function(evt,node){
if(node.isExpandable&&node.isExpanded){
this._collapseNode(node);
}else{
var _18c8=node.getParent();
if(_18c8&&_18c8.isTreeNode&&!(!this.showRoot&&_18c8===this.rootNode)){
this.focusNode(_18c8);
}
}
},focusLastChild:function(){
var node=this._getLast();
if(node&&node.isTreeNode){
this.focusNode(node);
}
},_getFirst:function(){
return this.showRoot?this.rootNode:this.rootNode.getChildren()[0];
},_getLast:function(){
var node=this.rootNode;
while(node.isExpanded){
var c=node.getChildren();
if(!c.length){
break;
}
node=c[c.length-1];
}
return node;
},_getNext:function(node){
if(node.isExpandable&&node.isExpanded&&node.hasChildren()){
return node.getChildren()[0];
}else{
while(node&&node.isTreeNode){
var _18c9=node.getNextSibling();
if(_18c9){
return _18c9;
}
node=node.getParent();
}
return null;
}
},childSelector:".dijitTreeRow",isExpandoNode:function(node,_18ca){
return dom.isDescendant(node,_18ca.expandoNode)||dom.isDescendant(node,_18ca.expandoNodeText);
},_onNodePress:function(_18cb,e){
this.focusNode(_18cb);
},__click:function(_18cc,e,_18cd,func){
var _18ce=e.target,_18cf=this.isExpandoNode(_18ce,_18cc);
if(_18cc.isExpandable&&(_18cd||_18cf)){
this._onExpandoClick({node:_18cc});
}else{
this._publish("execute",{item:_18cc.item,node:_18cc,evt:e});
this[func](_18cc.item,_18cc,e);
this.focusNode(_18cc);
}
e.stopPropagation();
e.preventDefault();
},_onClick:function(_18d0,e){
this.__click(_18d0,e,this.openOnClick,"onClick");
},_onDblClick:function(_18d1,e){
this.__click(_18d1,e,this.openOnDblClick,"onDblClick");
},_onExpandoClick:function(_18d2){
var node=_18d2.node;
this.focusNode(node);
if(node.isExpanded){
this._collapseNode(node);
}else{
this._expandNode(node);
}
},onClick:function(){
},onDblClick:function(){
},onOpen:function(){
},onClose:function(){
},_getNextNode:function(node){
_1888.deprecated(this.declaredClass+"::_getNextNode(node) is deprecated. Use _getNext(node) instead.","","2.0");
return this._getNext(node);
},_getRootOrFirstNode:function(){
_1888.deprecated(this.declaredClass+"::_getRootOrFirstNode() is deprecated. Use _getFirst() instead.","","2.0");
return this._getFirst();
},_collapseNode:function(node){
if(node._expandNodeDeferred){
delete node._expandNodeDeferred;
}
if(node.state=="Loading"){
return;
}
if(node.isExpanded){
var ret=node.collapse();
this.onClose(node.item,node);
this._state(node,false);
this._startPaint(ret);
return ret;
}
},_expandNode:function(node){
if(node._expandNodeDeferred){
return node._expandNodeDeferred;
}
var model=this.model,item=node.item,_18d3=this;
if(!node._loadDeferred){
node.markProcessing();
node._loadDeferred=new _1882();
model.getChildren(item,function(items){
node.unmarkProcessing();
node.setChildItems(items).then(function(){
node._loadDeferred.resolve(items);
});
},function(err){
console.error(_18d3,": error loading "+node.label+" children: ",err);
node._loadDeferred.reject(err);
});
}
var def=node._loadDeferred.then(lang.hitch(this,function(){
var def2=node.expand();
this.onOpen(node.item,node);
this._state(node,true);
return def2;
}));
this._startPaint(def);
return def;
},focusNode:function(node){
var _18d4=this.domNode.scrollLeft;
this.focusChild(node);
this.domNode.scrollLeft=_18d4;
},_onNodeMouseEnter:function(){
},_onNodeMouseLeave:function(){
},_onItemChange:function(item){
var model=this.model,_18d5=model.getIdentity(item),nodes=this._itemNodesMap[_18d5];
if(nodes){
var label=this.getLabel(item),_18d6=this.getTooltip(item);
array.forEach(nodes,function(node){
node.set({item:item,label:label,tooltip:_18d6});
node._updateItemClasses(item);
});
}
},_onItemChildrenChange:function(_18d7,_18d8){
var model=this.model,_18d9=model.getIdentity(_18d7),_18da=this._itemNodesMap[_18d9];
if(_18da){
array.forEach(_18da,function(_18db){
_18db.setChildItems(_18d8);
});
}
},_onItemDelete:function(item){
var model=this.model,_18dc=model.getIdentity(item),nodes=this._itemNodesMap[_18dc];
if(nodes){
array.forEach(nodes,function(node){
this.dndController.removeTreeNode(node);
var _18dd=node.getParent();
if(_18dd){
_18dd.removeChild(node);
}
if(this.lastFocusedChild&&!dom.isDescendant(this.lastFocusedChild,this.domNode)){
delete this.lastFocusedChild;
}
if(this.focusedChild&&!dom.isDescendant(this.focusedChild,this.domNode)){
this.focus();
}
node.destroyRecursive();
},this);
delete this._itemNodesMap[_18dc];
}
},_initState:function(){
this._openedNodes={};
if(this.persist&&this.cookieName){
var oreo=_1880(this.cookieName);
if(oreo){
array.forEach(oreo.split(","),function(item){
this._openedNodes[item]=true;
},this);
}
}
},_state:function(node,_18de){
if(!this.persist){
return false;
}
var path=array.map(node.getTreePath(),function(item){
return this.model.getIdentity(item);
},this).join("/");
if(arguments.length===1){
return this._openedNodes[path];
}else{
if(_18de){
this._openedNodes[path]=true;
}else{
delete this._openedNodes[path];
}
this._saveExpandedNodes();
}
},_saveExpandedNodes:function(){
if(this.persist&&this.cookieName){
var ary=[];
for(var id in this._openedNodes){
ary.push(id);
}
_1880(this.cookieName,ary.join(","),{expires:365});
}
},destroy:function(){
if(this._curSearch){
this._curSearch.timer.remove();
delete this._curSearch;
}
if(this.rootNode){
this.rootNode.destroyRecursive();
}
if(this.dndController&&!lang.isString(this.dndController)){
this.dndController.destroy();
}
this.rootNode=null;
this.inherited(arguments);
},destroyRecursive:function(){
this.destroy();
},resize:function(_18df){
if(_18df){
_1884.setMarginBox(this.domNode,_18df);
}
this._nodePixelIndent=_1884.position(this.tree.indentDetector).w||this._nodePixelIndent;
this.expandChildrenDeferred.then(lang.hitch(this,function(){
this.rootNode.set("indent",this.showRoot?0:-1);
this._adjustWidths();
}));
},_outstandingPaintOperations:0,_startPaint:function(p){
this._outstandingPaintOperations++;
if(this._adjustWidthsTimer){
this._adjustWidthsTimer.remove();
delete this._adjustWidthsTimer;
}
var oc=lang.hitch(this,function(){
this._outstandingPaintOperations--;
if(this._outstandingPaintOperations<=0&&!this._adjustWidthsTimer&&this._started){
this._adjustWidthsTimer=this.defer("_adjustWidths");
}
});
when(p,oc,oc);
},_adjustWidths:function(){
if(this._adjustWidthsTimer){
this._adjustWidthsTimer.remove();
delete this._adjustWidthsTimer;
}
this.containerNode.style.width="auto";
this.containerNode.style.width=this.domNode.scrollWidth>this.domNode.offsetWidth?"auto":"100%";
},_createTreeNode:function(args){
return new _189a(args);
},focus:function(){
if(this.lastFocusedChild){
this.focusNode(this.lastFocusedChild);
}else{
this.focusFirstChild();
}
}});
if(has("dojo-bidi")){
Tree.extend({_setTextDirAttr:function(_18e0){
if(_18e0&&this.textDir!=_18e0){
this._set("textDir",_18e0);
this.rootNode.set("textDir",_18e0);
}
}});
}
Tree.PathError=_1886("TreePathError");
Tree._TreeNode=_189a;
return Tree;
});
},"dijit/form/Button":function(){
define(["require","dojo/_base/declare","dojo/dom-class","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/ready","./_FormWidget","./_ButtonMixin","dojo/text!./templates/Button.html","../a11yclick"],function(_18e1,_18e2,_18e3,has,_18e4,lang,ready,_18e5,_18e6,_18e7){
if(1){
ready(0,function(){
var _18e8=["dijit/form/DropDownButton","dijit/form/ComboButton","dijit/form/ToggleButton"];
_18e1(_18e8);
});
}
var _18e9=_18e2("dijit.form.Button"+(has("dojo-bidi")?"_NoBidi":""),[_18e5,_18e6],{showLabel:true,iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},baseClass:"dijitButton",templateString:_18e7,_setValueAttr:"valueNode",_setNameAttr:function(name){
if(this.valueNode){
this.valueNode.setAttribute("name",name);
}
},_fillContent:function(_18ea){
if(_18ea&&(!this.params||!("label" in this.params))){
var _18eb=lang.trim(_18ea.innerHTML);
if(_18eb){
this.label=_18eb;
}
}
},_setShowLabelAttr:function(val){
if(this.containerNode){
_18e3.toggle(this.containerNode,"dijitDisplayNone",!val);
}
this._set("showLabel",val);
},setLabel:function(_18ec){
_18e4.deprecated("dijit.form.Button.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_18ec);
},_setLabelAttr:function(_18ed){
this.inherited(arguments);
if(!this.showLabel&&!("title" in this.params)){
this.titleNode.title=lang.trim(this.containerNode.innerText||this.containerNode.textContent||"");
}
}});
if(has("dojo-bidi")){
_18e9=_18e2("dijit.form.Button",_18e9,{_setLabelAttr:function(_18ee){
this.inherited(arguments);
if(this.titleNode.title){
this.applyTextDir(this.titleNode,this.titleNode.title);
}
},_setTextDirAttr:function(_18ef){
if(this._created&&this.textDir!=_18ef){
this._set("textDir",_18ef);
this._setLabelAttr(this.label);
}
}});
}
return _18e9;
});
},"curam/ui/OpenTabEvent":function(){
define(["dojo/_base/declare","curam/ui/PageRequest"],function(_18f0,_18f1){
var _18f2=_18f0("curam.ui.OpenTabEvent",null,{constructor:function(_18f3,_18f4,_18f5){
this.tabDescriptor=_18f3;
this.openInBackground=_18f5?true:false;
if(_18f4){
this.uimPageRequest=_18f4;
}else{
this.uimPageRequest=new _18f1(_18f3,_18f3.isHomePage);
}
}});
return _18f2;
});
},"dojo/parser":function(){
define(["require","./_base/kernel","./_base/lang","./_base/array","./_base/config","./dom","./_base/window","./_base/url","./aspect","./promise/all","./date/stamp","./Deferred","./has","./query","./on","./ready"],function(_18f6,dojo,dlang,_18f7,_18f8,dom,_18f9,_18fa,_18fb,all,dates,_18fc,has,query,don,ready){
new Date("X");
function _18fd(text){
return eval("("+text+")");
};
var _18fe=0;
_18fb.after(dlang,"extend",function(){
_18fe++;
},true);
function _18ff(ctor){
var map=ctor._nameCaseMap,proto=ctor.prototype;
if(!map||map._extendCnt<_18fe){
map=ctor._nameCaseMap={};
for(var name in proto){
if(name.charAt(0)==="_"){
continue;
}
map[name.toLowerCase()]=name;
}
map._extendCnt=_18fe;
}
return map;
};
var _1900={};
function _1901(types,_1902){
var ts=types.join();
if(!_1900[ts]){
var _1903=[];
for(var i=0,l=types.length;i<l;i++){
var t=types[i];
_1903[_1903.length]=(_1900[t]=_1900[t]||(dlang.getObject(t)||(~t.indexOf("/")&&(_1902?_1902(t):_18f6(t)))));
}
var ctor=_1903.shift();
_1900[ts]=_1903.length?(ctor.createSubclass?ctor.createSubclass(_1903):ctor.extend.apply(ctor,_1903)):ctor;
}
return _1900[ts];
};
var _1904={_clearCache:function(){
_18fe++;
_1900={};
},_functionFromScript:function(_1905,_1906){
var _1907="",_1908="",_1909=(_1905.getAttribute(_1906+"args")||_1905.getAttribute("args")),_190a=_1905.getAttribute("with");
var _190b=(_1909||"").split(/\s*,\s*/);
if(_190a&&_190a.length){
_18f7.forEach(_190a.split(/\s*,\s*/),function(part){
_1907+="with("+part+"){";
_1908+="}";
});
}
return new Function(_190b,_1907+_1905.innerHTML+_1908);
},instantiate:function(nodes,mixin,_190c){
mixin=mixin||{};
_190c=_190c||{};
var _190d=(_190c.scope||dojo._scopeName)+"Type",_190e="data-"+(_190c.scope||dojo._scopeName)+"-",_190f=_190e+"type",_1910=_190e+"mixins";
var list=[];
_18f7.forEach(nodes,function(node){
var type=_190d in mixin?mixin[_190d]:node.getAttribute(_190f)||node.getAttribute(_190d);
if(type){
var _1911=node.getAttribute(_1910),types=_1911?[type].concat(_1911.split(/\s*,\s*/)):[type];
list.push({node:node,types:types});
}
});
return this._instantiate(list,mixin,_190c);
},_instantiate:function(nodes,mixin,_1912,_1913){
var _1914=_18f7.map(nodes,function(obj){
var ctor=obj.ctor||_1901(obj.types,_1912.contextRequire);
if(!ctor){
throw new Error("Unable to resolve constructor for: '"+obj.types.join()+"'");
}
return this.construct(ctor,obj.node,mixin,_1912,obj.scripts,obj.inherited);
},this);
function _1915(_1916){
if(!mixin._started&&!_1912.noStart){
_18f7.forEach(_1916,function(_1917){
if(typeof _1917.startup==="function"&&!_1917._started){
_1917.startup();
}
});
}
return _1916;
};
if(_1913){
return all(_1914).then(_1915);
}else{
return _1915(_1914);
}
},construct:function(ctor,node,mixin,_1918,_1919,_191a){
var proto=ctor&&ctor.prototype;
_1918=_1918||{};
var _191b={};
if(_1918.defaults){
dlang.mixin(_191b,_1918.defaults);
}
if(_191a){
dlang.mixin(_191b,_191a);
}
var _191c;
if(has("dom-attributes-explicit")){
_191c=node.attributes;
}else{
if(has("dom-attributes-specified-flag")){
_191c=_18f7.filter(node.attributes,function(a){
return a.specified;
});
}else{
var clone=/^input$|^img$/i.test(node.nodeName)?node:node.cloneNode(false),attrs=clone.outerHTML.replace(/=[^\s"']+|="[^"]*"|='[^']*'/g,"").replace(/^\s*<[a-zA-Z0-9]*\s*/,"").replace(/\s*>.*$/,"");
_191c=_18f7.map(attrs.split(/\s+/),function(name){
var _191d=name.toLowerCase();
return {name:name,value:(node.nodeName=="LI"&&name=="value")||_191d=="enctype"?node.getAttribute(_191d):node.getAttributeNode(_191d).value};
});
}
}
var scope=_1918.scope||dojo._scopeName,_191e="data-"+scope+"-",hash={};
if(scope!=="dojo"){
hash[_191e+"props"]="data-dojo-props";
hash[_191e+"type"]="data-dojo-type";
hash[_191e+"mixins"]="data-dojo-mixins";
hash[scope+"type"]="dojoType";
hash[_191e+"id"]="data-dojo-id";
}
var i=0,item,_191f=[],_1920,extra;
while(item=_191c[i++]){
var name=item.name,_1921=name.toLowerCase(),value=item.value;
switch(hash[_1921]||_1921){
case "data-dojo-type":
case "dojotype":
case "data-dojo-mixins":
break;
case "data-dojo-props":
extra=value;
break;
case "data-dojo-id":
case "jsid":
_1920=value;
break;
case "data-dojo-attach-point":
case "dojoattachpoint":
_191b.dojoAttachPoint=value;
break;
case "data-dojo-attach-event":
case "dojoattachevent":
_191b.dojoAttachEvent=value;
break;
case "class":
_191b["class"]=node.className;
break;
case "style":
_191b["style"]=node.style&&node.style.cssText;
break;
default:
if(!(name in proto)){
var map=_18ff(ctor);
name=map[_1921]||name;
}
if(name in proto){
switch(typeof proto[name]){
case "string":
_191b[name]=value;
break;
case "number":
_191b[name]=value.length?Number(value):NaN;
break;
case "boolean":
_191b[name]=value.toLowerCase()!="false";
break;
case "function":
if(value===""||value.search(/[^\w\.]+/i)!=-1){
_191b[name]=new Function(value);
}else{
_191b[name]=dlang.getObject(value,false)||new Function(value);
}
_191f.push(name);
break;
default:
var pVal=proto[name];
_191b[name]=(pVal&&"length" in pVal)?(value?value.split(/\s*,\s*/):[]):(pVal instanceof Date)?(value==""?new Date(""):value=="now"?new Date():dates.fromISOString(value)):(pVal instanceof _18fa)?(dojo.baseUrl+value):_18fd(value);
}
}else{
_191b[name]=value;
}
}
}
for(var j=0;j<_191f.length;j++){
var _1922=_191f[j].toLowerCase();
node.removeAttribute(_1922);
node[_1922]=null;
}
if(extra){
try{
extra=_18fd.call(_1918.propsThis,"{"+extra+"}");
dlang.mixin(_191b,extra);
}
catch(e){
throw new Error(e.toString()+" in data-dojo-props='"+extra+"'");
}
}
dlang.mixin(_191b,mixin);
if(!_1919){
_1919=(ctor&&(ctor._noScript||proto._noScript)?[]:query("> script[type^='dojo/']",node));
}
var _1923=[],calls=[],_1924=[],ons=[];
if(_1919){
for(i=0;i<_1919.length;i++){
var _1925=_1919[i];
node.removeChild(_1925);
var event=(_1925.getAttribute(_191e+"event")||_1925.getAttribute("event")),prop=_1925.getAttribute(_191e+"prop"),_1926=_1925.getAttribute(_191e+"method"),_1927=_1925.getAttribute(_191e+"advice"),_1928=_1925.getAttribute("type"),nf=this._functionFromScript(_1925,_191e);
if(event){
if(_1928=="dojo/connect"){
_1923.push({method:event,func:nf});
}else{
if(_1928=="dojo/on"){
ons.push({event:event,func:nf});
}else{
_191b[event]=nf;
}
}
}else{
if(_1928=="dojo/aspect"){
_1923.push({method:_1926,advice:_1927,func:nf});
}else{
if(_1928=="dojo/watch"){
_1924.push({prop:prop,func:nf});
}else{
calls.push(nf);
}
}
}
}
}
var _1929=ctor.markupFactory||proto.markupFactory;
var _192a=_1929?_1929(_191b,node,ctor):new ctor(_191b,node);
function _192b(_192c){
if(_1920){
dlang.setObject(_1920,_192c);
}
for(i=0;i<_1923.length;i++){
_18fb[_1923[i].advice||"after"](_192c,_1923[i].method,dlang.hitch(_192c,_1923[i].func),true);
}
for(i=0;i<calls.length;i++){
calls[i].call(_192c);
}
for(i=0;i<_1924.length;i++){
_192c.watch(_1924[i].prop,_1924[i].func);
}
for(i=0;i<ons.length;i++){
don(_192c,ons[i].event,ons[i].func);
}
return _192c;
};
if(_192a.then){
return _192a.then(_192b);
}else{
return _192b(_192a);
}
},scan:function(root,_192d){
var list=[],mids=[],_192e={};
var _192f=(_192d.scope||dojo._scopeName)+"Type",_1930="data-"+(_192d.scope||dojo._scopeName)+"-",_1931=_1930+"type",_1932=_1930+"textdir",_1933=_1930+"mixins";
var node=root.firstChild;
var _1934=_192d.inherited;
if(!_1934){
function _1935(node,attr){
return (node.getAttribute&&node.getAttribute(attr))||(node.parentNode&&_1935(node.parentNode,attr));
};
_1934={dir:_1935(root,"dir"),lang:_1935(root,"lang"),textDir:_1935(root,_1932)};
for(var key in _1934){
if(!_1934[key]){
delete _1934[key];
}
}
}
var _1936={inherited:_1934};
var _1937;
var _1938;
function _1939(_193a){
if(!_193a.inherited){
_193a.inherited={};
var node=_193a.node,_193b=_1939(_193a.parent);
var _193c={dir:node.getAttribute("dir")||_193b.dir,lang:node.getAttribute("lang")||_193b.lang,textDir:node.getAttribute(_1932)||_193b.textDir};
for(var key in _193c){
if(_193c[key]){
_193a.inherited[key]=_193c[key];
}
}
}
return _193a.inherited;
};
while(true){
if(!node){
if(!_1936||!_1936.node){
break;
}
node=_1936.node.nextSibling;
_1938=false;
_1936=_1936.parent;
_1937=_1936.scripts;
continue;
}
if(node.nodeType!=1){
node=node.nextSibling;
continue;
}
if(_1937&&node.nodeName.toLowerCase()=="script"){
type=node.getAttribute("type");
if(type&&/^dojo\/\w/i.test(type)){
_1937.push(node);
}
node=node.nextSibling;
continue;
}
if(_1938){
node=node.nextSibling;
continue;
}
var type=node.getAttribute(_1931)||node.getAttribute(_192f);
var _193d=node.firstChild;
if(!type&&(!_193d||(_193d.nodeType==3&&!_193d.nextSibling))){
node=node.nextSibling;
continue;
}
var _193e;
var ctor=null;
if(type){
var _193f=node.getAttribute(_1933),types=_193f?[type].concat(_193f.split(/\s*,\s*/)):[type];
try{
ctor=_1901(types,_192d.contextRequire);
}
catch(e){
}
if(!ctor){
_18f7.forEach(types,function(t){
if(~t.indexOf("/")&&!_192e[t]){
_192e[t]=true;
mids[mids.length]=t;
}
});
}
var _1940=ctor&&!ctor.prototype._noScript?[]:null;
_193e={types:types,ctor:ctor,parent:_1936,node:node,scripts:_1940};
_193e.inherited=_1939(_193e);
list.push(_193e);
}else{
_193e={node:node,scripts:_1937,parent:_1936};
}
_1937=_1940;
_1938=node.stopParser||(ctor&&ctor.prototype.stopParser&&!(_192d.template));
_1936=_193e;
node=_193d;
}
var d=new _18fc();
if(mids.length){
if(has("dojo-debug-messages")){
console.warn("WARNING: Modules being Auto-Required: "+mids.join(", "));
}
var r=_192d.contextRequire||_18f6;
r(mids,function(){
d.resolve(_18f7.filter(list,function(_1941){
if(!_1941.ctor){
try{
_1941.ctor=_1901(_1941.types,_192d.contextRequire);
}
catch(e){
}
}
var _1942=_1941.parent;
while(_1942&&!_1942.types){
_1942=_1942.parent;
}
var proto=_1941.ctor&&_1941.ctor.prototype;
_1941.instantiateChildren=!(proto&&proto.stopParser&&!(_192d.template));
_1941.instantiate=!_1942||(_1942.instantiate&&_1942.instantiateChildren);
return _1941.instantiate;
}));
});
}else{
d.resolve(list);
}
return d.promise;
},_require:function(_1943,_1944){
var hash=_18fd("{"+_1943.innerHTML+"}"),vars=[],mids=[],d=new _18fc();
var _1945=(_1944&&_1944.contextRequire)||_18f6;
for(var name in hash){
vars.push(name);
mids.push(hash[name]);
}
_1945(mids,function(){
for(var i=0;i<vars.length;i++){
dlang.setObject(vars[i],arguments[i]);
}
d.resolve(arguments);
});
return d.promise;
},_scanAmd:function(root,_1946){
var _1947=new _18fc(),_1948=_1947.promise;
_1947.resolve(true);
var self=this;
query("script[type='dojo/require']",root).forEach(function(node){
_1948=_1948.then(function(){
return self._require(node,_1946);
});
node.parentNode.removeChild(node);
});
return _1948;
},parse:function(_1949,_194a){
if(_1949&&typeof _1949!="string"&&!("nodeType" in _1949)){
_194a=_1949;
_1949=_194a.rootNode;
}
var root=_1949?dom.byId(_1949):_18f9.body();
_194a=_194a||{};
var mixin=_194a.template?{template:true}:{},_194b=[],self=this;
var p=this._scanAmd(root,_194a).then(function(){
return self.scan(root,_194a);
}).then(function(_194c){
return self._instantiate(_194c,mixin,_194a,true);
}).then(function(_194d){
return _194b=_194b.concat(_194d);
}).otherwise(function(e){
console.error("dojo/parser::parse() error",e);
throw e;
});
dlang.mixin(_194b,p);
return _194b;
}};
if(1){
dojo.parser=_1904;
}
if(_18f8.parseOnLoad){
ready(100,_1904,"parse");
}
return _1904;
});
},"curam/widget/TransferList":function(){
define(["dijit/_Widget","dojo/_base/declare","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","dojo/dom","dojo/query","curam/debug"],function(_194e,_194f,_1950,_1951,_1952,dom,query,debug){
var _1953=_194f("curam.widget.TransferList",dijit._Widget,{btnNames:["allRight","toRight","toLeft","allLeft"],btnValues:[" "," "," "," "],bntClasses:["allRight","toRight","toLeft","allLeft"],rightEmptyText:"",widgetType:"TransferList",postCreate:function(){
var _1954=this.domNode.parentNode;
_1950.add(_1954,"transferlistparent");
var _1955=query(this.domNode).next()[0];
this.leftList=this.domNode;
var _1956=_1952.create("table",{"class":"transfer-list",role:"presentation"});
var _1957=_1952.create("tbody",{},_1956);
var _1958=_1952.create("tr",{},_1957);
var _1959=_1952.create("td");
var _195a=_1952.create("td",{"class":"controls"});
var self=this;
function _195b(name){
return function(){
self.setSelection(name);
return false;
};
};
function _195c(id){
return function(){
_1950.add(dom.byId(id),"active");
return false;
};
};
function _195d(id){
return function(){
_1950.remove(dom.byId(id),"active");
return false;
};
};
for(j=0;j<4;j++){
var _195e=_1952.create("div",{},_195a);
var _195f=new Array(LOCALISED_TRANSFER_LIST_RA,LOCALISED_TRANSFER_LIST_R,LOCALISED_TRANSFER_LIST_L,LOCALISED_TRANSFER_LIST_LA);
var btn=_1952.create("input",{type:"button",id:this.btnNames[j]+this.domNode.name,value:this.btnValues[j],"class":this.bntClasses[j],"title":_195f[j]},_195e);
btn.listtwins=this;
dojo.connect(btn,"onclick",_195b(btn.id));
dojo.connect(btn,"onmousedown",_195c(btn.id));
dojo.connect(btn,"onmouseup",_195d(btn.id));
dojo.connect(btn,"onmouseout",_195d(btn.id));
}
var _1960=document.createElement("td");
var rList=_1952.create("select",{id:this.domNode.name,name:this.domNode.name,title:LOCALISED_TRANSFER_LIST_SELECTED_ITEMS,multiple:"multiple","class":"selected",size:5},_1960);
_1951.set(this.domNode,{name:"__o3ign."+rList.name,id:"__o3ign."+rList.name,"class":"selected",size:5});
this.rightList=rList;
dojo.connect(this.leftList,"ondblclick",_195b("toRight"));
dojo.connect(this.rightList,"ondblclick",_195b("toLeft"));
function _1961(name){
return function(evt){
if(evt.keyCode==evt.KEY_ENTER){
self.setSelection(name);
}
return false;
};
};
dojo.connect(this.leftList,"onkeydown",_1961("toRight"));
dojo.connect(this.rightList,"onkeydown",_1961("toLeft"));
_1959.appendChild(this.domNode);
_1958.appendChild(_1959);
_1958.appendChild(_195a);
_1958.appendChild(_1960);
if(_1955){
_1954.insertBefore(_1956,_1955);
}else{
_1954.appendChild(_1956);
}
this.setInitialSelection();
this.adjustEmpties(this.leftList,this.rightList);
var form=query(this.domNode).closest("form")[0];
if(!form){
debug.log("curam.widget.TransferList "+debug.getProperty("curam.widget.TransferList.msg"));
return;
}
dojo.connect(form,"onsubmit",function(){
var _1962=self.rightList;
var _1963=new Array();
for(k1=0;k1<_1962.options.length;k1++){
_1963[_1963.length]=_1962.options[k1];
}
_1962.options.length=0;
for(k2=0;k2<_1963.length;k2++){
_1963[k2].selected=true;
_1962.appendChild(_1963[k2]);
}
});
dojo.connect(window,"onresize",this.selectWidthSetting);
dojo.addOnLoad(this.selectWidthSetting);
var _1964=function(list){
for(var i=0;i<list.options.length;i++){
if(list.options[i].text){
list.options[i].title=list.options[i].text;
}
}
};
_1964(self.leftList);
_1964(self.rightList);
},setSelection:function(id){
var _1965=(id.indexOf("all")>-1);
var _1966=(id.indexOf("Right")>-1)?this.leftList:this.rightList;
var _1967=(id.indexOf("Left")>-1)?this.leftList:this.rightList;
if(_1966.options[0]!=null&&_1966.options[0].text!=this.rightEmptyText){
if(_1967.options[0]!=null&&(_1967.options[0].text==this.rightEmptyText||_1967.options[0].text=="")){
_1967.options[0]=null;
}
this.transferOptions(_1966,_1967,_1965);
this.adjustEmpties(this.leftList,this.rightList);
}
},setInitialSelection:function(){
this.transferOptions(this.leftList,this.rightList,false);
},adjustEmpties:function(_1968,_1969){
if(_1969.options.length==0){
_1969.options[0]=new Option(this.rightEmptyText,"",false,false);
}
},transferOptions:function(_196a,_196b,_196c){
if(_196a&&_196b){
var _196d=new Array();
dojo.forEach(_196a.options,function(opt){
if(_196c||opt.selected){
_196d[_196d.length]=opt;
}
});
this.appendAll(_196b,_196d);
}
},appendAll:function(aList,_196e){
for(var i=0;i<_196e.length;i++){
_196e[i].selected=true;
aList.appendChild(_196e[i]);
}
},selectWidthSetting:function(){
if(dojo.query(".transfer-list select.selected")){
dojo.query(".transfer-list select.selected").forEach(function(_196f){
var width=_196f.parentNode.clientWidth;
_196f.style.width=width+"px";
});
}
}});
return _1953;
});
},"dojo/promise/all":function(){
define(["../_base/array","../Deferred","../when"],function(array,_1970,when){
"use strict";
var some=array.some;
return function all(_1971){
var _1972,array;
if(_1971 instanceof Array){
array=_1971;
}else{
if(_1971&&typeof _1971==="object"){
_1972=_1971;
}
}
var _1973;
var _1974=[];
if(_1972){
array=[];
for(var key in _1972){
if(Object.hasOwnProperty.call(_1972,key)){
_1974.push(key);
array.push(_1972[key]);
}
}
_1973={};
}else{
if(array){
_1973=[];
}
}
if(!array||!array.length){
return new _1970().resolve(_1973);
}
var _1975=new _1970();
_1975.promise.always(function(){
_1973=_1974=null;
});
var _1976=array.length;
some(array,function(_1977,index){
if(!_1972){
_1974.push(index);
}
when(_1977,function(value){
if(!_1975.isFulfilled()){
_1973[_1974[index]]=value;
if(--_1976===0){
_1975.resolve(_1973);
}
}
},_1975.reject);
return _1975.isFulfilled();
});
return _1975.promise;
};
});
},"curam/widget/ComboBoxMixin":function(){
define(["dojo/_base/declare","curam/widget/_ComboBoxMenu","dijit/form/ComboBoxMixin"],function(_1978,_1979){
var _197a=_1978("curam.widget.ComboBoxMixin",dijit.form.ComboBoxMixin,{dropDownClass:_1979});
return _197a;
});
},"curam/cdsl/types/codetable/CodeTable":function(){
define(["dojo/_base/declare","./CodeTableItem"],function(_197b,_197c){
var _197d=_197b(null,{_name:null,_defaultCode:null,_codes:null,_items:null,constructor:function(name,_197e,codes){
this._name=name;
this._defaultCode=_197e;
this._codes=this._parseCodesIntoObject(codes);
},listItems:function(){
this._initItems(this._codes,this._defaultCode);
var ret=[];
for(code in this._items){
ret.push(this._items[code]);
}
return ret;
},_parseCodesIntoObject:function(_197f){
var ret={};
if(_197f){
for(var i=0;i<_197f.length;i++){
var raw=_197f[i];
var code=raw.split(":")[0];
var desc=raw.slice(code.length+1);
ret[code]=desc;
}
}
return ret;
},_initItems:function(codes,_1980){
if(!this._items){
this._items={};
for(code in codes){
var cti=new _197c(code,codes[code]);
cti.isDefault(code===_1980);
this._items[code]=cti;
}
}
},getItem:function(code){
this._initItems(this._codes,this._defaultCode);
return this._items[code];
}});
return _197d;
});
},"curam/debug":function(){
define(["curam/define","curam/util/LocalConfig","dojo/ready","dojo/_base/lang","curam/util/ResourceBundle"],function(_1981,_1982,ready,lang,_1983){
var _1984=new _1983("curam.application.Debug");
_1981.singleton("curam.debug",{log:function(){
if(curam.debug.enabled()){
try{
var a=arguments;
if(!dojo.isIE){
console.log.apply(console,a);
}else{
var _1985=a.length;
var sa=curam.debug._serializeArgument;
switch(_1985){
case 1:
console.log(arguments[0]);
break;
case 2:
console.log(a[0],sa(a[1]));
break;
case 3:
console.log(a[0],sa(a[1]),sa(a[2]));
break;
case 4:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]));
break;
case 5:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]));
break;
case 6:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
break;
default:
console.log("[Incomplete message - "+(_1985-5)+" message a truncated] "+a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
}
}
}
catch(e){
console.log(e);
}
}
},getProperty:function(key,_1986){
return _1984.getProperty(key,_1986);
},_serializeArgument:function(arg){
if(typeof arg!="undefined"&&typeof arg.nodeType!="undefined"&&typeof arg.cloneNode!="undefined"){
return ""+arg;
}else{
if(curam.debug._isWindow(arg)){
return arg.location.href;
}else{
if(curam.debug._isArray(arg)&&curam.debug._isWindow(arg[0])){
return "[array of window objects, length "+arg.length+"]";
}else{
return dojo.toJson(arg);
}
}
}
},_isArray:function(arg){
return typeof arg!="undefined"&&(dojo.isArray(arg)||typeof arg.length!="undefined");
},_isWindow:function(arg){
var _1987=typeof arg!="undefined"&&typeof arg.closed!="undefined"&&arg.closed;
if(_1987){
return true;
}else{
return typeof arg!="undefined"&&typeof arg.location!="undefined"&&typeof arg.navigator!="undefined"&&typeof arg.document!="undefined"&&typeof arg.closed!="undefined";
}
},enabled:function(){
return _1982.readOption("jsTraceLog","false")=="true";
},_setup:function(_1988){
_1982.seedOption("jsTraceLog",_1988.trace,"false");
_1982.seedOption("ajaxDebugMode",_1988.ajaxDebug,"false");
_1982.seedOption("asyncProgressMonitor",_1988.asyncProgressMonitor,"false");
}});
return curam.debug;
});
},"dijit/TooltipDialog":function(){
define(["dojo/_base/declare","dojo/dom-class","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./focus","./layout/ContentPane","./_DialogMixin","./form/_FormMixin","./_TemplatedMixin","dojo/text!./templates/TooltipDialog.html","./main"],function(_1989,_198a,has,keys,lang,on,focus,_198b,_198c,_198d,_198e,_198f,dijit){
var _1990=_1989("dijit.TooltipDialog",[_198b,_198e,_198d,_198c],{title:"",doLayout:false,autofocus:true,baseClass:"dijitTooltipDialog",_firstFocusItem:null,_lastFocusItem:null,templateString:_198f,_setTitleAttr:"containerNode",postCreate:function(){
this.inherited(arguments);
this.own(on(this.domNode,"keydown",lang.hitch(this,"_onKey")));
},orient:function(node,_1991,_1992){
var newC={"MR-ML":"dijitTooltipRight","ML-MR":"dijitTooltipLeft","TM-BM":"dijitTooltipAbove","BM-TM":"dijitTooltipBelow","BL-TL":"dijitTooltipBelow dijitTooltipABLeft","TL-BL":"dijitTooltipAbove dijitTooltipABLeft","BR-TR":"dijitTooltipBelow dijitTooltipABRight","TR-BR":"dijitTooltipAbove dijitTooltipABRight","BR-BL":"dijitTooltipRight","BL-BR":"dijitTooltipLeft","BR-TL":"dijitTooltipBelow dijitTooltipABLeft","BL-TR":"dijitTooltipBelow dijitTooltipABRight","TL-BR":"dijitTooltipAbove dijitTooltipABRight","TR-BL":"dijitTooltipAbove dijitTooltipABLeft"}[_1991+"-"+_1992];
_198a.replace(this.domNode,newC,this._currentOrientClass||"");
this._currentOrientClass=newC;
},focus:function(){
this._getFocusItems();
focus.focus(this._firstFocusItem);
},onOpen:function(pos){
this.orient(this.domNode,pos.aroundCorner,pos.corner);
var _1993=pos.aroundNodePos;
if(pos.corner.charAt(0)=="M"&&pos.aroundCorner.charAt(0)=="M"){
this.connectorNode.style.top=_1993.y+((_1993.h-this.connectorNode.offsetHeight)>>1)-pos.y+"px";
this.connectorNode.style.left="";
}else{
if(pos.corner.charAt(1)=="M"&&pos.aroundCorner.charAt(1)=="M"){
this.connectorNode.style.left=_1993.x+((_1993.w-this.connectorNode.offsetWidth)>>1)-pos.x+"px";
}
}
this._onShow();
},onClose:function(){
this.onHide();
},_onKey:function(evt){
if(evt.keyCode==keys.ESCAPE){
this.defer("onCancel");
evt.stopPropagation();
evt.preventDefault();
}else{
if(evt.keyCode==keys.TAB){
var node=evt.target;
this._getFocusItems();
if(this._firstFocusItem==this._lastFocusItem){
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._firstFocusItem&&evt.shiftKey){
focus.focus(this._lastFocusItem);
evt.stopPropagation();
evt.preventDefault();
}else{
if(node==this._lastFocusItem&&!evt.shiftKey){
focus.focus(this._firstFocusItem);
evt.stopPropagation();
evt.preventDefault();
}else{
evt.stopPropagation();
}
}
}
}
}
}});
if(has("dojo-bidi")){
_1990.extend({_setTitleAttr:function(title){
this.containerNode.title=(this.textDir&&this.enforceTextDirWithUcc)?this.enforceTextDirWithUcc(null,title):title;
this._set("title",title);
},_setTextDirAttr:function(_1994){
if(!this._created||this.textDir!=_1994){
this._set("textDir",_1994);
if(this.textDir&&this.title){
this.containerNode.title=this.enforceTextDirWithUcc(null,this.title);
}
}
}});
}
return _1990;
});
},"url:curam/widget/templates/AppBannerDropDownBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft appBannerComboBox\"\n\tid=\"widget_${id}\"\n\trole=\"combobox\"\n\taria-haspopup=\"true\"\n\tdata-dojo-attach-point=\"_popupStateNode\"\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class='dijitReset dijitInputInner appBannerComboBoxInput' ${!nameAttrSetting} type=\"text\" autocomplete=\"off\"\n\t\t\tdata-dojo-attach-point=\"textbox,focusNode\" role=\"textbox\"\n\t/></div\n\t><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer'\n\t\tdata-dojo-attach-point=\"_buttonNode\" role=\"presentation\"\n\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner appBannerComboBoxArrow\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"button presentation\" aria-hidden=\"true\"\n\t\t\t${_buttonInputDisabled}\n\t/></div\n></div>\n","url:curam/layout/resources/UIMController.html":"<div id=\"uimcontroller_${uid}\" class=\"uimcontroller_${uid} uimController ${classList}\" data-dojo-attach-point=\"uimController\">\n  <div style=\"display:none;\" \n       id=\"uimcontroller_tc_${uid}\" \n       class=\"ipnTabController in-page-nav-tabContainer\"\n       data-dojo-attach-point=\"tabController\" \n       data-dojo-type=\"curam/layout/TabContainer\">\n  </div>\n  <div class=\"contentPanelFrameWrapper\"  \n        data-dojo-attach-point=\"frameWrapper\">\n    <iframe frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\"\n             allowTransparency=\"true\" \n             id=\"${iframeId}\" \n             data-dojo-attach-point=\"frame\"                 \n             class=\"${iframeId} ${iframeClassList}\"\n             iscpiframe=\"${iscpiframe}\"\n             title=\"${title}\" >\n    </iframe>\n  </div> \n</div>","url:curam/widget/templates/OptimalBrowserMessage.html":"<div>\n  <div class=\"hidden\"\n       data-dojo-type=\"dojox/layout/ContentPane\"\n       data-dojo-attach-point=\"_optimalMessage\">\n  </div>\n</div>\n","url:curam/widget/templates/DropDownButton.html":"<span class=\"dijit dijitReset dijitInline\"\n\t><span class='dijitReset dijitInline dijitButtonNode'\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" data-dojo-attach-point=\"_buttonNode\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"focusNode,titleNode,_arrowWrapperNode\"\n\t\t\trole=\"button\" aria-haspopup=\"true\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\"\n\t\t\t\tdata-dojo-attach-point=\"iconNode\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode,_popupStateNode\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonInner\"></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonChar\">&#9660;</span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" tabIndex=\"-1\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\"\n\t\tdata-dojo-attach-point=\"valueNode\" role=\"presentation\" aria-hidden=\"true\"\n/></span>\n","url:dijit/form/templates/Select.html":"<table class=\"dijit dijitReset dijitInline dijitLeft\"\n\tdata-dojo-attach-point=\"_buttonNode,tableNode,focusNode,_popupStateNode\" cellspacing='0' cellpadding='0'\n\trole=\"listbox\" aria-haspopup=\"true\"\n\t><tbody role=\"presentation\"><tr role=\"presentation\"\n\t\t><td class=\"dijitReset dijitStretch dijitButtonContents\" role=\"presentation\"\n\t\t\t><div class=\"dijitReset dijitInputField dijitButtonText\"  data-dojo-attach-point=\"containerNode,textDirNode\" role=\"presentation\"></div\n\t\t\t><div class=\"dijitReset dijitValidationContainer\"\n\t\t\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t\t\t/></div\n\t\t\t><input type=\"hidden\" ${!nameAttrSetting} data-dojo-attach-point=\"valueNode\" value=\"${value}\" aria-hidden=\"true\"\n\t\t/></td\n\t\t><td class=\"dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer\"\n\t\t\tdata-dojo-attach-point=\"titleNode\" role=\"presentation\"\n\t\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t\t\t\t${_buttonInputDisabled}\n\t\t/></td\n\t></tr></tbody\n></table>\n","url:dijit/form/templates/TextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner\" data-dojo-attach-point='textbox,focusNode' autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n","url:dijit/templates/MenuSeparator.html":"<tr class=\"dijitMenuSeparator\" role=\"separator\">\n\t<td class=\"dijitMenuSeparatorIconCell\">\n\t\t<div class=\"dijitMenuSeparatorTop\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n\t<td colspan=\"3\" class=\"dijitMenuSeparatorLabelCell\">\n\t\t<div class=\"dijitMenuSeparatorTop dijitMenuSeparatorLabel\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n</tr>\n","url:curam/widget/templates/SearchMultipleTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner searchTextBox input-placeholder-closed\" data-dojo-attach-point='textbox,focusNode'\n\t\t\tdata-dojo-attach-event=\"onFocus:_givenFocus,onblur:_onBlur, onpaste:_onPaste\" autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}' \n\t/></div\n></div>\n","url:dijit/templates/TitlePane.html":"<div>\n\t<div data-dojo-attach-event=\"ondijitclick:_onTitleClick, onkeydown:_onTitleKey\"\n\t\t\tclass=\"dijitTitlePaneTitle\" data-dojo-attach-point=\"titleBarNode\" id=\"${id}_titleBarNode\">\n\t\t<div class=\"dijitTitlePaneTitleFocus\" data-dojo-attach-point=\"focusNode\">\n\t\t\t<span data-dojo-attach-point=\"arrowNode\" class=\"dijitInline dijitArrowNode\" role=\"presentation\"></span\n\t\t\t><span data-dojo-attach-point=\"arrowNodeInner\" class=\"dijitArrowNodeInner\"></span\n\t\t\t><span data-dojo-attach-point=\"titleNode\" class=\"dijitTitlePaneTextNode\"></span>\n\t\t</div>\n\t</div>\n\t<div class=\"dijitTitlePaneContentOuter\" data-dojo-attach-point=\"hideNode\" role=\"presentation\">\n\t\t<div class=\"dijitReset\" data-dojo-attach-point=\"wipeNode\" role=\"presentation\">\n\t\t\t<div class=\"dijitTitlePaneContentInner\" data-dojo-attach-point=\"containerNode\" role=\"region\" id=\"${id}_pane\" aria-labelledby=\"${id}_titleBarNode\">\n\t\t\t\t<!-- nested divs because wipeIn()/wipeOut() doesn't work right on node w/padding etc.  Put padding on inner div. -->\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</div>\n","url:dijit/templates/Tooltip.html":"<div class=\"dijitTooltip dijitTooltipLeft\" id=\"dojoTooltip\" data-dojo-attach-event=\"mouseenter:onMouseEnter,mouseleave:onMouseLeave\"\n\t><div class=\"dijitTooltipConnector\" data-dojo-attach-point=\"connectorNode\"></div\n\t><div class=\"dijitTooltipContainer dijitTooltipContents\" data-dojo-attach-point=\"containerNode\" role='alert'></div\n></div>\n","url:curam/layout/resources/CuramBaseModal.html":"<div class=\"dijitDialog\" role=\"dialog\" aria-labelledby=\"${id}_title\" aria-live=\"assertive\">\n\t<div data-dojo-attach-point=\"titleBar\" class=\"dijitDialogTitleBar\">\n\t\t<span data-dojo-attach-point=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\"\n\t\t\t\trole=\"heading\" level=\"2\"></span>\n\t\t<button data-dojo-attach-point=\"closeButtonNode\" class=\"dijitDialogCloseIcon\"\n          data-dojo-attach-event=\"ondijitclick: onCancel\" title=\"${buttonCancel}\"\n          role=\"button\" tabindex=\"0\" aria-label=\"${closeModalText}\" \n          onKeyDown=\"require(['curam/ModalDialog'], function(md) {md.handleTitlebarIconKeydown(event)});\"\n          style=\"visibility: hidden;\">\n      \t\t<img src=\"${buttonCloseIcon}\" alt=\"${closeModalText}\" class=\"button-close-icon-default\"/>\n      \t\t<img src=\"${buttonCloseIconHover}\" alt=\"${closeModalText}\" class=\"button-close-icon-hover\"/>\n\t\t\t<span data-dojo-attach-point=\"closeText\" class=\"closeText\" title=\"${buttonCancel}\">${closeModalText}</span>\n\t    </button>\n\t</div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitDialogPaneContent\"></div>\n\t${!actionBarTemplate}\n</div>\n","url:dijit/form/templates/ComboButton.html":"<table class=\"dijit dijitReset dijitInline dijitLeft\"\n\tcellspacing='0' cellpadding='0' role=\"presentation\"\n\t><tbody role=\"presentation\"><tr role=\"presentation\"\n\t\t><td class=\"dijitReset dijitStretch dijitButtonNode\" data-dojo-attach-point=\"buttonNode\" data-dojo-attach-event=\"ondijitclick:__onClick,onkeydown:_onButtonKeyDown\"\n\t\t><div id=\"${id}_button\" class=\"dijitReset dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"titleNode\"\n\t\t\trole=\"button\" aria-labelledby=\"${id}_label\"\n\t\t\t><div class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\" role=\"presentation\"></div\n\t\t\t><div class=\"dijitReset dijitInline dijitButtonText\" id=\"${id}_label\" data-dojo-attach-point=\"containerNode\" role=\"presentation\"></div\n\t\t></div\n\t\t></td\n\t\t><td id=\"${id}_arrow\" class='dijitReset dijitRight dijitButtonNode dijitArrowButton'\n\t\t\tdata-dojo-attach-point=\"_popupStateNode,focusNode,_buttonNode\"\n\t\t\tdata-dojo-attach-event=\"onkeydown:_onArrowKeyDown\"\n\t\t\ttitle=\"${optionsTitle}\"\n\t\t\trole=\"button\" aria-haspopup=\"true\"\n\t\t\t><div class=\"dijitReset dijitArrowButtonInner\" role=\"presentation\"></div\n\t\t\t><div class=\"dijitReset dijitArrowButtonChar\" role=\"presentation\">&#9660;</div\n\t\t></td\n\t\t><td style=\"display:none !important;\"\n\t\t\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" data-dojo-attach-point=\"valueNode\"\n\t\t\t\tclass=\"dijitOffScreen\" aria-hidden=\"true\" data-dojo-attach-event=\"onclick:_onClick\"\n\t\t/></td></tr></tbody\n></table>\n","url:curam/widget/templates/_TabButton.html":"<div role=\"presentation\" data-dojo-attach-point=\"titleNode,innerDiv,tabContent\" class=\"dijitTab\" id=\"${id}_tabButtonContainer\">\n  <div role=\"presentation\" class='dijitTabInnerDiv'>\n    <div role=\"presentation\" class='dijitTabContent'>\n\t    <span role=\"presentation\" class=\"dijitInline dijitIcon dijitTabButtonIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t    <div role=\"presentation\" aria-labelledby='${id}'>\n\t\t    <span data-dojo-attach-point='containerNode,focusNode' class='tabLabel' id='${id}'></span>\n\t\t    <button class=\"dijitInline dijitTabCloseButton dijitTabCloseIcon\" data-dojo-attach-point='closeNode'\n\t\t\t        tabindex=\"-1\">\n\t\t\t    <span data-dojo-attach-point='closeText' class='dijitTabCloseText'>Close Tab</span>\n        \t</button>\n      </div>\n    </div>\n  </div>\n</div>\n","url:dijit/templates/MenuBarItem.html":"<div class=\"dijitReset dijitInline dijitMenuItem dijitMenuItemLabel\" data-dojo-attach-point=\"focusNode\"\n\t \trole=\"menuitem\" tabIndex=\"-1\">\n\t<span data-dojo-attach-point=\"containerNode,textDirNode\"></span>\n</div>\n","url:curam/layout/resources/CuramCarbonModal.html":"<div class=\"spm-component\">  \n   <div id=\"${id}_modal-root\" data-testid=\"${id}_modal-root\" data-dojo-attach-point=\"carbonModalNode\" class=\"bx--modal\" role=\"presentation\">\n        <div data-dojo-attach-point=\"modalContainer\" aria-modal=\"true\" aria-labelledby=\"${id}_modal-heading\" aria-live=\"assertive\" class=\"bx--modal-container\" data-testid=\"${id}_modal-container\" id=\"${id}_modal-container\" role=\"dialog\" tabindex=\"-1\">\n          <div class=\"bx--modal-header bx--modal-header--with-pair-icons\">\n            <h3 class=\"bx--modal-header__heading bx--type-beta\" data-dojo-attach-point=\"titleNode\" id=\"${id}_modal-heading\" data-testid=\"${id}_modal-heading\">\n              ${_title}\n            </h3>\n            <button data-dojo-attach-point=\"helpIcon\" aria-label=\"${helpTitle}\" class=\"bx--modal-help\" data-dojo-attach-event=\"onclick:displayHelp, onkeypress:displayHelp,onkeydown:escapeOnClose,onkeydown:preventBackwardsTabEscFromModal\" data-testid=\"button_modal.help.button.icon\" id=\"${id}_modal-help\" type=\"button\">\n              <svg focusable=\"false\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 32 32\">\n               <defs><style>.cls-1{fill:none;}</style></defs>\n               <title>${helpTitle}</title>\n               <path d=\"M16,2A14,14,0,1,0,30,16,14,14,0,0,0,16,2Zm0,26A12,12,0,1,1,28,16,12,12,0,0,1,16,28Z\"/>\n               <circle cx=\"16\" cy=\"23.5\" r=\"1.5\"/>\n               <path d=\"M17,8H15.5A4.49,4.49,0,0,0,11,12.5V13h2v-.5A2.5,2.5,0,0,1,15.5,10H17a2.5,2.5,0,0,1,0,5H15v4.5h2V17a4.5,4.5,0,0,0,0-9Z\"/>\n               <rect class=\"cls-1\" width=\"32\" height=\"32\"/>\n             </svg>\n            </button> \n            <button data-dojo-attach-point=\"closeButton\" aria-label=\"${closeTitle}\" class=\"bx--modal-close\" data-dojo-attach-event=\"onclick:hideAndClose,onkeydown:escapeOnClose,onkeydown:preventBackwardsTabEscFromModal\" data-testid=\"button_modal.close.button.icon\" id=\"${id}_modal-button-close\" type=\"button\">\n              <svg aria-hidden=\"true\" class=\"bx--modal-close__icon\" focusable=\"false\" preserveAspectRatio=\"xMidYMid meet\" style=\"will-change: transform;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" viewBox=\"0 0 16 16\">\n                <title>${closeTitle}</title>\n                <path d=\"M12 4.7l-.7-.7L8 7.3 4.7 4l-.7.7L7.3 8 4 11.3l.7.7L8 8.7l3.3 3.3.7-.7L8.7 8z\"></path>\n              </svg>\n            </button>\n          </div>\n\n          <div class=\"bx--modal-content bx--modal-scroll-content bx--modal-content--with-iframe\" data-dojo-attach-point=\"containerNode\" data-testid=\"${id}_modal-content\" id=\"${id}_modal-content\">\n          </div>\n          <div class=\"bx--modal-content--overflow-indicator\"></div>\n          <div data-dojo-attach-point=\"modalFooter\" class=\"bx--modal-footer\" data-testid=\"${id}_modal-footer\" id=\"${id}_modal-footer\"></div>\n          <span data-dojo-attach-event=\"onkeydown:escapeOnClose,onfocus:preventForwardsTabEscFromModal\" id=\"modal-end_${id}\" tabindex=\"0\"></span>\n        </div>\n    </div>\n</div>","url:curam/layout/resources/ModalUIMController.html":"<div id=\"uimcontroller_${uid}\" \n  class=\"uimcontroller_${uid} uimController ${classList}\" \n  data-dojo-attach-point=\"uimController\">\n  <div style=\"display:none;\" \n       id=\"uimcontroller_tc_${uid}\" \n       class=\"ipnTabController in-page-nav-tabContainer\"\n       data-dojo-attach-point=\"tabController\" \n       data-dojo-type=\"curam.layout.TabContainer\">\n  </div>\n  <div class=\"contentPanelFrameWrapper\"\n       data-dojo-attach-point=\"frameWrapper\">\n    <iframe frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\"\n            allowTransparency=\"true\" \n            id=\"${iframeId}\" \n            data-dojo-attach-point=\"frame\"                 \n            class=\"${iframeId} ${iframeClassList}\"\n            iscpiframe=\"${iscpiframe}\"\n            title=\"${title}\" >\n    </iframe>\n    <span onFocus=\"require(['curam/ModalDialog'], function(md) {md.handleFocusAtEnd(event)});\"\n          tabIndex=\"0\" class=\"hidden\" id=\"end-${uid}\" style=\"visibility: hidden;\">${endModalUIMController}</span>\n  </div>\n</div>","url:dijit/templates/Menu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable\" role=\"menu\" tabIndex=\"${tabIndex}\"\n\t   cellspacing=\"0\">\n\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"containerNode\"></tbody>\n</table>\n","url:curam/layout/resources/TabContainer.html":"<div class=\"dijitTabContainer\">\n\t<div class=\"dijitTabListWrapper\" data-dojo-attach-point=\"tablistNode\"></div>\n\t<div data-dojo-attach-point=\"tablistSpacer\" class=\"dijitTabSpacer ${baseClass}-spacer dijitAlignTop\"></div>\n\t<div class=\"dijitTabPaneWrapper ${baseClass}-container dijitAlignClient\" data-dojo-attach-point=\"containerNode\"></div>\n</div>\n","url:dijit/form/templates/DropDownBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\"\n\trole=\"combobox\"\n\taria-haspopup=\"true\"\n\tdata-dojo-attach-point=\"_popupStateNode\"\n\t><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer'\n\t\tdata-dojo-attach-point=\"_buttonNode\" role=\"presentation\"\n\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"button presentation\" aria-hidden=\"true\"\n\t\t\t${_buttonInputDisabled}\n\t/></div\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class='dijitReset dijitInputInner' ${!nameAttrSetting} type=\"text\" autocomplete=\"off\"\n\t\t\tdata-dojo-attach-point=\"textbox,focusNode\" role=\"textbox\"\n\t/></div\n></div>\n","url:dijit/form/templates/ValidationTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\" role=\"presentation\"\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner\" data-dojo-attach-point='textbox,focusNode' autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n","url:dijit/templates/TreeNode.html":"<div class=\"dijitTreeNode\" role=\"presentation\"\n\t><div data-dojo-attach-point=\"rowNode\" class=\"dijitTreeRow\" role=\"presentation\"\n\t\t><span data-dojo-attach-point=\"expandoNode\" class=\"dijitInline dijitTreeExpando\" role=\"presentation\"></span\n\t\t><span data-dojo-attach-point=\"expandoNodeText\" class=\"dijitExpandoText\" role=\"presentation\"></span\n\t\t><span data-dojo-attach-point=\"contentNode\"\n\t\t\tclass=\"dijitTreeContent\" role=\"presentation\">\n\t\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitTreeIcon\" data-dojo-attach-point=\"iconNode\"></span\n\t\t\t><span data-dojo-attach-point=\"labelNode,focusNode\" class=\"dijitTreeLabel\" role=\"treeitem\"\n\t\t\t\t   tabindex=\"-1\" aria-selected=\"false\" id=\"${id}_label\"></span>\n\t\t</span\n\t></div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitTreeNodeContainer\" role=\"presentation\"\n\t\t style=\"display: none;\" aria-labelledby=\"${id}_label\"></div>\n</div>\n","url:dijit/templates/MenuBar.html":"<div class=\"dijitMenuBar dijitMenuPassive\" data-dojo-attach-point=\"containerNode\" role=\"menubar\" tabIndex=\"${tabIndex}\"\n\t ></div>\n","url:curam/widget/templates/SearchTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner searchTextBox input-placeholder-closed\" data-dojo-attach-point='textbox,focusNode' data-dojo-attach-event=\"onclick:__onClick,onFocus:__onClick,onblur:__onBlur,onkeyup:__onKeyUp,onpaste:__onPaste\" autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n","url:curam/layout/resources/CuramStackButton.html":"<span class=\"dijit dijitReset dijitInline\" role=\"presentation\">\n  <span class=\"dijitReset dijitInline dijitButtonNode\"\n  data-dojo-attach-event=\"ondijitclick:__onClick\" role=\"presentation\">\n    <span class=\"dijitReset dijitStretch dijitButtonContents\" data-dojo-attach-point=\"titleNode,focusNode\"\n    role=\"button\" aria-labelledby=\"${id}_label\">\n      <span class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span>\n      <span class=\"dijitReset dijitInline curamButtonText\" id=\"${id}_label\" data-dojo-attach-point=\"containerNode\"></span>\n      <img alt=\"${imageAltText}\" src=\"${imageDefaultSrc}\" class=\"defaultIcon\">\n      <img alt=\"${imageAltText}\" src=\"${imageHoverSrc}\" class=\"hoverIcon\">\n    </span>\n  </span>\n  <input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" data-dojo-attach-event=\"onclick:_onClick\"\n  tabIndex=\"-1\" aria-hidden=\"true\" data-dojo-attach-point=\"valueNode\"/>\n</span>","url:dijit/templates/TooltipDialog.html":"<div role=\"alertdialog\" tabIndex=\"-1\">\n\t<div class=\"dijitTooltipContainer\" role=\"presentation\">\n\t\t<div data-dojo-attach-point=\"contentsNode\" class=\"dijitTooltipContents dijitTooltipFocusNode\">\n\t\t\t<div data-dojo-attach-point=\"containerNode\"></div>\n\t\t\t${!actionBarTemplate}\n\t\t</div>\n\t</div>\n\t<div class=\"dijitTooltipConnector\" role=\"presentation\" data-dojo-attach-point=\"connectorNode\"></div>\n</div>\n","url:dijit/layout/templates/_TabButton.html":"<div role=\"presentation\" data-dojo-attach-point=\"titleNode,innerDiv,tabContent\" class=\"dijitTabInner dijitTabContent\">\n\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitTabButtonIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t<span data-dojo-attach-point='containerNode,focusNode' class='tabLabel'></span>\n\t<span class=\"dijitInline dijitTabCloseButton dijitTabCloseIcon\" data-dojo-attach-point='closeNode'\n\t\t  role=\"presentation\">\n\t\t<span data-dojo-attach-point='closeText' class='dijitTabCloseText'>[x]</span\n\t\t\t\t></span>\n</div>\n","url:dijit/form/templates/Button.html":"<span class=\"dijit dijitReset dijitInline\" role=\"presentation\"\n\t><span class=\"dijitReset dijitInline dijitButtonNode\"\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" role=\"presentation\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"titleNode,focusNode\"\n\t\t\trole=\"button\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span\n\t\t\t><span class=\"dijitReset dijitToggleButtonIconChar\">&#x25CF;</span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode\"\n\t\t\t></span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\"\n\t\ttabIndex=\"-1\" aria-hidden=\"true\" data-dojo-attach-point=\"valueNode\"\n/></span>\n","url:curam/layout/resources/ScrollingTabController.html":"<div class=\"dijitTabListContainer-${tabPosition} tabStrip-disabled dijitLayoutContainer\"><!-- CURAM-FIX: removed style=\"visibility:hidden, dd the tabStrip-disabled class by default.\" -->\n\t<div data-dojo-type=\"curam.layout._ScrollingTabControllerMenuButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_menuBtn\"\n\t\t data-dojo-props=\"containerId: '${containerId}', iconClass: 'dijitTabStripMenuIcon',\n\t\t\t\t\tdropDownPosition: ['below-alt', 'above-alt']\"\n\t\t data-dojo-attach-point=\"_menuBtn\" showLabel=\"false\" title=\"Navigation menu\">&#9660;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_leftBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideLeftIcon', showLabel:false, title:'Navigation left'\"\n\t\t data-dojo-attach-point=\"_leftBtn\" data-dojo-attach-event=\"onClick: doSlideLeft\">&#9664;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_rightBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideRightIcon', showLabel:false, title:'Navigation right'\"\n\t\t data-dojo-attach-point=\"_rightBtn\" data-dojo-attach-event=\"onClick: doSlideRight\">&#9654;</div>\n\t<div class='dijitTabListWrapper dijitTabContainerTopNone dijitAlignClient' data-dojo-attach-point='tablistWrapper'>\n\t\t<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'\n\t\t\t\tdata-dojo-attach-point='containerNode' class='nowrapTabStrip dijitTabContainerTop-tabs'></div>\n\t</div>\n</div>","url:dijit/layout/templates/ScrollingTabController.html":"<div class=\"dijitTabListContainer-${tabPosition} tabStrip-disabled dijitLayoutContainer\"><!-- CURAM-FIX: removed style=\"visibility:hidden, dd the tabStrip-disabled class by default.\" -->\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerMenuButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_menuBtn\"\n\t\t data-dojo-props=\"containerId: '${containerId}', iconClass: 'dijitTabStripMenuIcon',\n\t\t\t\t\tdropDownPosition: ['below-alt', 'above-alt']\"\n\t\t data-dojo-attach-point=\"_menuBtn\" showLabel=\"false\" title=\"Navigation menu\">&#9660;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_leftBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideLeftIcon', showLabel:false, title:'Navigation left'\"\n\t\t data-dojo-attach-point=\"_leftBtn\" data-dojo-attach-event=\"onClick: doSlideLeft\">&#9664;</div>\n\t<div data-dojo-type=\"dijit.layout._ScrollingTabControllerButton\"\n\t\t class=\"tabStripButton-${tabPosition}\"\n\t\t id=\"${id}_rightBtn\"\n\t\t data-dojo-props=\"iconClass:'dijitTabStripSlideRightIcon', showLabel:false, title:'Navigation right'\"\n\t\t data-dojo-attach-point=\"_rightBtn\" data-dojo-attach-event=\"onClick: doSlideRight\">&#9654;</div>\n\t<div class='dijitTabListWrapper dijitTabContainerTopNone dijitAlignClient' data-dojo-attach-point='tablistWrapper'>\n\t\t<div role='tablist' data-dojo-attach-event='onkeydown:onkeydown'\n\t\t\t\tdata-dojo-attach-point='containerNode' class='nowrapTabStrip dijitTabContainerTop-tabs'></div>\n\t</div>\n</div>","url:dijit/templates/Tree.html":"<div role=\"tree\">\n\t<div class=\"dijitInline dijitTreeIndent\" style=\"position: absolute; top: -9999px\" data-dojo-attach-point=\"indentDetector\"></div>\n\t<div class=\"dijitTreeExpando dijitTreeExpandoLoading\" data-dojo-attach-point=\"rootLoadingIndicator\"></div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitTreeContainer\" role=\"presentation\">\n\t</div>\n</div>\n","url:dijit/templates/Dialog.html":"<div class=\"dijitDialog\" role=\"dialog\" aria-labelledby=\"${id}_title\">\n\t<div data-dojo-attach-point=\"titleBar\" class=\"dijitDialogTitleBar\">\n\t\t<span data-dojo-attach-point=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\"\n\t\t\t\trole=\"heading\" level=\"1\"></span>\n\t\t<span data-dojo-attach-point=\"closeButtonNode\" class=\"dijitDialogCloseIcon\" data-dojo-attach-event=\"ondijitclick: onCancel\" title=\"${buttonCancel}\" role=\"button\" tabindex=\"-1\">\n\t\t\t<span data-dojo-attach-point=\"closeText\" class=\"closeText\" title=\"${buttonCancel}\">x</span>\n\t\t</span>\n\t</div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitDialogPaneContent\"></div>\n\t${!actionBarTemplate}\n</div>\n\n","url:dijit/templates/MenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,textDirNode\"\n\t\trole=\"presentation\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">\n\t\t<span data-dojo-attach-point=\"arrowWrapper\" style=\"visibility: hidden\">\n\t\t\t<span class=\"dijitInline dijitIcon dijitMenuExpand\"></span>\n\t\t\t<span class=\"dijitMenuExpandA11y\">+</span>\n\t\t</span>\n\t</td>\n</tr>\n","url:curam/widget/templates/ComboBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\"\n\tid=\"widget_${id}\"\n    role=\"listbox\"\n\taria-haspopup=\"true\"\n\tdata-dojo-attach-point=\"_popupStateNode\"\n\t><div class='dijitReset dijitRight dijitButtonNode dijitArrowButton dijitDownArrowButton dijitArrowButtonContainer'\n\t\tdata-dojo-attach-point=\"_buttonNode\" role=\"presentation\"\n\t\t><input class=\"dijitReset dijitInputField dijitArrowButtonInner\" value=\"&#9660; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"button presentation\" aria-hidden=\"true\"\n\t\t\t${_buttonInputDisabled}\n\t/></div\n\t><div class='dijitReset dijitValidationContainer'\n\t\t><input class=\"dijitReset dijitInputField dijitValidationIcon dijitValidationInner\" value=\"&#935; \" type=\"text\" tabIndex=\"-1\" readonly=\"readonly\" role=\"presentation\"\n\t/></div\n    ><div class=\"dijitReset dijitInputField dijitInputContainer\" role=\"listbox\"\n\t\t><input class='dijitReset dijitInputInner' ${!nameAttrSetting} type=\"text\" autocomplete=\"off\"\n\t\t\tdata-dojo-attach-point=\"textbox,focusNode\" role=\"textbox\"\n\t/></div\n></div>\n","url:dijit/layout/templates/_ScrollingTabControllerButton.html":"<div data-dojo-attach-event=\"ondijitclick:_onClick\" data-dojo-attach-point=\"focusNode\" role=\"button\">\n  <div role=\"presentation\" class=\"dijitTabInnerDiv\">\n    <div role=\"presentation\" class=\"dijitTabContent dijitButtonContents\">\n\t<span role=\"presentation\" class=\"dijitInline dijitTabStripIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t<span data-dojo-attach-point=\"containerNode,titleNode\" class=\"dijitButtonText\"></span>\n</div>  </div>\n</div>","url:dijit/layout/templates/TabContainer.html":"<div class=\"dijitTabContainer\">\n\t<div class=\"dijitTabListWrapper\" data-dojo-attach-point=\"tablistNode\"></div>\n\t<div data-dojo-attach-point=\"tablistSpacer\" class=\"dijitTabSpacer ${baseClass}-spacer\"></div>\n\t<div class=\"dijitTabPaneWrapper ${baseClass}-container\" data-dojo-attach-point=\"containerNode\"></div>\n</div>\n","url:dijit/templates/CheckedMenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"${role}\" tabIndex=\"-1\" aria-checked=\"${checked}\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span class=\"dijitInline dijitIcon dijitMenuItemIcon dijitCheckedMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t\t<span class=\"dijitMenuItemIconChar dijitCheckedMenuItemIconChar\">${!checkedChar}</span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,labelNode,textDirNode\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">&#160;</td>\n</tr>\n","url:dijit/form/templates/DropDownButton.html":"<span class=\"dijit dijitReset dijitInline\"\n\t><span class='dijitReset dijitInline dijitButtonNode'\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" data-dojo-attach-point=\"_buttonNode\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"focusNode,titleNode,_arrowWrapperNode,_popupStateNode\"\n\t\t\trole=\"button\" aria-haspopup=\"true\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\"\n\t\t\t\tdata-dojo-attach-point=\"iconNode\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonInner\"></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonChar\">&#9660;</span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" tabIndex=\"-1\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\" data-dojo-attach-point=\"valueNode\" aria-hidden=\"true\"\n/></span>\n","*now":function(r){
r(["dojo/i18n!*preload*dojo/nls/cdej*[\"ar\",\"ca\",\"cs\",\"da\",\"de\",\"el\",\"en-gb\",\"en-us\",\"es-es\",\"fi-fi\",\"fr-fr\",\"he-il\",\"hu\",\"it-it\",\"ja-jp\",\"ko-kr\",\"nl-nl\",\"nb\",\"pl\",\"pt-br\",\"pt-pt\",\"ru\",\"sk\",\"sl\",\"sv\",\"th\",\"tr\",\"zh-tw\",\"zh-cn\",\"ROOT\"]"]);
}}});
define("dojo/cdej",[],1);
