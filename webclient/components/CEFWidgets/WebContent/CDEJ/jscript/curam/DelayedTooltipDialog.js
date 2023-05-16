dojo.provide("curam.DelayedTooltipDialog");
dojo.require("dijit.TooltipDialog");
dojo.declare("curam.DelayedTooltipDialog",dijit.TooltipDialog,{closeDelay:1000,openDelay:1000,orientation:null,targetNode:"",closeButton:"",closeButtonAltText:"",tooltipDialogAnnouncementText:"",closeButtonDefaultIcon:"",closeButtonHoverIcon:"",templateString:dojo.cache("curam","templates/TooltipDialog.html"),buildRendering:function(){
this.inherited(arguments);
var _1=this.content.firstChild;
var _2=dojo.query("div[class*=\"tooltip-title\"]",_1)[0];
var _3=dojo.query("div[class*=\"tooltip-inner-panel\"]",_1)[0];
if(_2){
var _4=this.domNode.children[0];
var _5=this.id+"_tooltip_title";
if(_4&&_4.id===_5){
_4.innerHTML=_2.innerText;
var ua=navigator.userAgent.toLowerCase();
if((/msie/.test(ua)||/trident/.test(ua))&&!/opera/.test(ua)){
_4.innerHTML=_4.innerHTML+" "+this.tooltipDialogAnnouncementText;
}
}
}
if(_3){
var _6=this.domNode.children[1];
var _7=this.id+"_tooltip_content";
if(_6&&_6.id===_7){
_6.innerHTML=_3.innerText;
}
}
},postCreate:function(){
this.inherited(arguments);
dojo.style(this.domNode,"display","none");
var _8="<div>"+"<img src=\""+this.closeButtonDefaultIcon+"\" alt=\"\" class=\"tooltip-close-default-icon\">"+"<img src=\""+this.closeButtonHoverIcon+"\" alt=\"\" class=\"tooltip-close-hover-icon\">"+"<span class=\"hiddenControlForScreenReader\">"+this.closeButtonAltText+"</span>"+"</div>";
var _9=dojo.query("div[class*=\"tooltip-title\"]",this.domNode)[0];
this.closeButton=dojo.create("button",{innerHTML:_8},_9);
dojo.attr(this.closeButton,"class","tooltip-close-button tooltip-close-button-normal");
dojo.attr(this.closeButton,"tabIndex","0");
var _a=this;
dojo.connect(this.domNode,"onkeypress",function(_b){
if(_b.keyCode==27){
_a.close();
dojo.stopEvent(_b);
}else{
if(document.activeElement===_a.domNode){
var _c="button, [href], input, select, textarea, [tabindex]:not([tabindex=\"-1\"])";
var _d=_a.domNode.querySelectorAll(_c);
var _e=_d[0];
var _f=_d[_d.length-1];
if(_b.keyCode==9&&_b.shiftKey){
_f.focus();
dojo.stopEvent(_b);
}else{
if(_b.keyCode==9&&!(_b.shiftKey)){
_e.focus();
dojo.stopEvent(_b);
}
}
}
}
});
dojo.connect(this.domNode,"onfocus",function(){
if(window.document.documentMode&&_a._popupWrapper){
dojo.attr(_a._popupWrapper,"style",{"outline":"1px dotted #686868"});
}
});
dojo.connect(this.domNode,"onblur",function(){
if(window.document.documentMode&&_a._popupWrapper){
dojo.attr(_a._popupWrapper,"style",{"outline":"0"});
}
});
dojo.connect(this.closeButton,"onmouseover",function(){
dojo.attr(_a.closeButton,"class","tooltip-close-button tooltip-close-button-mouseover");
dojo.attr(_a.closeButton,"style",{cursor:"pointer"});
});
dojo.connect(this.closeButton,"onfocus",function(){
dojo.attr(_a.closeButton,"class","tooltip-close-button tooltip-close-button-mouseover");
});
dojo.connect(this.closeButton,"onmouseout",function(){
dojo.attr(_a.closeButton,"class","tooltip-close-button tooltip-close-button-normal");
});
dojo.connect(this.closeButton,"onblur",function(){
dojo.attr(_a.closeButton,"class","tooltip-close-button tooltip-close-button-normal");
});
dojo.connect(this.closeButton,"onclick",function(_10){
_a.close();
dojo.stopEvent(_10);
});
dojo.connect(this.closeButton,"onkeypress",function(_11){
if(CEFUtils.enterKeyPress(_11)){
_a.close();
dojo.stopEvent(_11);
}else{
_a.closeButton.focus();
}
});
if(this.targetNode){
var _12=dojo.byId(this.targetNode);
dojo.connect(_12,"onclick",_a,function(_13){
_a.openTooltip(_13);
dojo.stopEvent(_13);
});
dojo.connect(_12,"onkeypress",_a,function(_14){
if(CEFUtils.enterKeyPress(_14)){
_a.openTooltip(_14);
dojo.stopEvent(_14);
}
});
dojo.connect(_12,"onmouseover",function(){
dojo.attr(_12,"style",{cursor:"pointer"});
dojo.attr(_12,"class","rollover");
});
dojo.connect(_12,"onfocus",function(){
dojo.attr(_12,"style",{cursor:"pointer"});
dojo.attr(_12,"class","rollover");
});
dojo.connect(_12,"onblur",function(){
dojo.attr(_12,"class","normal");
});
dojo.connect(_12,"onmouseout",function(){
dojo.attr(_12,"class","normal");
});
}
},openTooltip:function(_15){
if(!((_15.type==="click")||CEFUtils.enterKeyPress(_15))){
return;
}
var _16=this;
var _17=dojo.byId(this.id);
dojo.removeClass(_17,"tooltip-startup-position");
var _18=dijit.popup.open({popup:_16,around:_16.targetNode,orient:_16.orientation});
if(this._popupWrapper){
dojo.attr(this._popupWrapper,"style",{height:"auto"});
dojo.removeAttr(this._popupWrapper,"aria-label");
}
this.domNode.focus();
},close:function(){
dijit.popup.close(this);
dojo.attr(this.closeButton,"class","tooltip-close-button tooltip-close-button-normal");
dojo.byId(this.targetNode).focus();
}});

