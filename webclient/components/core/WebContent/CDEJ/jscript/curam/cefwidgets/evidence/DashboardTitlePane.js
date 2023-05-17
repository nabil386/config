dojo.provide("curam.cefwidgets.evidence.DashboardTitlePane");
require(["dijit/TitlePane"]);
dojo.declare("curam.cefwidgets.evidence.DashboardTitlePane",dijit.TitlePane,{_currentOpenCloseClass:"",newId:"",navigationContent:"",selectedButtonAltText:"",toggleExpandAltText:"",toggleCollapseAltText:"",postCreate:function(){
this.inherited(arguments);
dojo.attr(this.domNode,"role","presentation");
var _1=this.titleBarNode;
dojo.attr(_1,"class","evidenceTitlePane");
this.newId="div_"+this.id;
arrowImageSpan=dojo.create("span",{id:this.newId});
dojo.addClass(arrowImageSpan,"arrowImageSpanClass");
dojo.place(arrowImageSpan,this.titleNode,"after");
dojo.place(this.navigationContent,this.titleNode,"before");
arrowImg=dojo.create("img",{id:"img_"+this.id,tabindex:"0",role:"button"},arrowImageSpan);
var _2=[this];
dojo.connect(arrowImg,"onclick",dojo.partial(this.startToggle,_2));
dojo.connect(arrowImg,"onkeypress",dojo.partial(this.startToggleOnKeyPress,_2));
dojo.connect(arrowImg,"onkeyup",dojo.partial(this.startToggleOnKeyPress,_2));
dojo.connect(arrowImg,"onkeydown",dojo.partial(this.startToggleOnKeyPress,_2));
if(this._currentOpenCloseClass=="EvidenceDashboardOpen"){
if(curam.util.highContrastModeType()){
dojo.attr(arrowImg,"src","../Images/highcontrast/arrow_toggle_expand.png");
dojo.attr(arrowImg,"alt",this.toggleCollapseAltText);
}else{
dojo.attr(arrowImg,"src","../Images/chevron--down20-enabled.svg");
dojo.attr(arrowImg,"alt",this.toggleCollapseAltText);
dojo.attr(arrowImg,"onmouseover","this.src = \"../Images/chevron--down20-enabled.svg\"");
dojo.attr(arrowImg,"onmouseout","this.src = \"../Images/chevron--down20-enabled.svg\"");
}
}else{
if(curam.util.highContrastModeType()){
dojo.attr(arrowImg,"src","../Images/highcontrast/arrow_toggle_mini.png");
dojo.attr(arrowImg,"alt",this.toggleExpandAltText);
}else{
if(curam.util.isRtlMode()){
dojo.attr(arrowImg,"src","../Images/chevron--right20-enabled.svg");
dojo.attr(arrowImg,"alt",this.toggleExpandAltText);
dojo.attr(arrowImg,"onmouseover","this.src = \"../Images/chevron--right20-enabled.svg\"");
dojo.attr(arrowImg,"onmouseout","this.src = \"../Images/chevron--right20-enabled.svg\"");
}else{
dojo.attr(arrowImg,"src","../Images/chevron--left20-enabled.svg");
dojo.attr(arrowImg,"alt",this.toggleExpandAltText);
dojo.attr(arrowImg,"onmouseover","this.src = \"../Images/chevron--left20-enabled.svg\"");
dojo.attr(arrowImg,"onmouseout","this.src = \"../Images/chevron--left20-enabled.svg\"");
}
}
}
var _3=this.titleNode;
dojo.attr(_3,"class","evidenceTitlePaneTextNode");
var _4=this.hideNode;
dojo.attr(_4,"class","noBorderTitlePaneContentOuter");
var _5=this.titleNode.innerHTML;
var _6=dojo.query(".dijitTitlePaneContentInner",_4)[0];
dojo.attr(_6,"aria-label",_5);
dojo.removeAttr(this.focusNode,"tabindex");
dojo.removeAttr(this.focusNode,"role");
var h2=dojo.create("h2",{title:"evidenceTitlePaneHeader","class":"evidenceTitlePaneHeader",innerHTML:this.titleNode.outerHTML},this.focusNode);
dojo.place(h2,this.titleNode,"replace");
},startToggleOnKeyPress:function(_7,_8){
if(CEFUtils.enterKeyPress(_8)){
_7[0].startToggle(_7);
}
},_updateArrowCSS:function(){
if(dojo.byId("img_"+this.id)!=null){
if(this._currentOpenCloseClass=="EvidenceDashboardOpen"){
if(curam.util.highContrastModeType()){
dojo.attr(dojo.byId("img_"+this.id),"src","../Images/highcontrast/arrow_toggle_expand.png");
dojo.attr(dojo.byId("img_"+this.id),"alt",this.toggleCollapseAltText);
}else{
dojo.attr(dojo.byId("img_"+this.id),"src","../Images/chevron--down20-enabled.svg");
dojo.attr(dojo.byId("img_"+this.id),"alt",this.toggleCollapseAltText);
dojo.attr(dojo.byId("img_"+this.id),"onmouseover","this.src = \"../Images/chevron--down20-enabled.svg\"");
dojo.attr(dojo.byId("img_"+this.id),"onmouseout","this.src = \"../Images/chevron--down20-enabled.svg\"");
}
}else{
if(curam.util.highContrastModeType()){
dojo.attr(dojo.byId("img_"+this.id),"src","../Images/highcontrast/arrow_toggle_mini.png");
dojo.attr(dojo.byId("img_"+this.id),"alt",this.toggleExpandAltText);
}else{
if(curam.util.isRtlMode()){
dojo.attr(dojo.byId("img_"+this.id),"src","../Images/chevron--right20-enabled.svg");
dojo.attr(dojo.byId("img_"+this.id),"alt",this.toggleExpandAltText);
dojo.attr(dojo.byId("img_"+this.id),"onmouseover","this.src = \"../Images/chevron--right20-enabled.svg\"");
dojo.attr(dojo.byId("img_"+this.id),"onmouseout","this.src = \"../Images/chevron--right20-enabled.svg\"");
}else{
dojo.attr(dojo.byId("img_"+this.id),"src","../Images/chevron--left20-enabled.svg");
dojo.attr(dojo.byId("img_"+this.id),"alt",this.toggleExpandAltText);
dojo.attr(dojo.byId("img_"+this.id),"onmouseover","this.src = \"../Images/chevron--left20-enabled.svg\"");
dojo.attr(dojo.byId("img_"+this.id),"onmouseout","this.src = \"../Images/chevron--left20-enabled.svg\"");
}
}
}
}
},_setCss:function(){
var _9=this.titleBarNode||this.focusNode;
if(this._titleBarClass){
dojo.removeClass(_9,this._titleBarClass);
}
this._titleBarClass="EvidenceDashboard"+(this.toggleable?"":"Fixed")+(this.open?"Open":"Closed");
this._currentOpenCloseClass=this._titleBarClass;
this._updateArrowCSS();
this.arrowNodeInner.innerHTML=this.open?"-":"+";
},_onTitleEnter:function(e){
},_onTitleClick:function(){
},_onTitleKey:function(e){
},startToggle:function(_a,_b){
if(_a[0].toggleable){
_a[0].toggle();
}
},destroyDescendants:function(_c){
},destroy:function(){
}});

