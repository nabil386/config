//>>built
require({cache:{"url:curam/layout/resources/ExpandoPane.html":"<div class=\"dojoxExpandoPane dojoxExpando${orient} ${startupCls}\">\n\t<div dojoAttachPoint=\"titleWrapper\" class=\"dojoxExpandoTitle\">\n\t\t<div class=\"dojoxExpandoIcon dojoxExpandoIcon${orient}\" role=\"application\" aria-label=\"${expandIconAlt}\" dojoAttachPoint=\"iconNode\" dojoAttachEvent=\"ondijitclick:toggle,onkeypress: enterCheck\" tabindex=\"0\">\n\t\t\t<a role=\"button\" tabIndex=\"-1\" dojoAttachPoint=\"iconNodeLink\"></a>\n\t\t</div>\n\t\t<span class=\"dojoxExpandoTitleNode\" dojoAttachPoint=\"titleNode\" title=\"${title}\">${title}</span>\n\t</div>\n\t<div class=\"dojoxExpandoWrapper\" dojoAttachPoint=\"cwrapper\" dojoAttachEvent=\"ondblclick:_trap\">\n\t\t<div class=\"dojoxExpandoContent\" dojoAttachPoint=\"containerNode\"></div>\n\t</div>\n</div>\n"}});
define("curam/layout/ExpandoPane",["dojo/_base/declare","curam/inspection/Layer","curam/smartPanel","curam/debug","curam/util","dojo/_base/lang","dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/fx","dojo/dom-class","dojo/text!curam/layout/resources/ExpandoPane.html","dojo/dom-construct","dijit/focus","dojox/layout/ExpandoPane"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e){
var _f=_1("curam.layout.ExpandoPane",dojox.layout.ExpandoPane,{templateString:_c,startupCls:"",expandIconAlt:"",splitterAriaLabel:"",orient:"",postMixInProperties:function(){
var _10="",rtl=!this.isLeftToRight();
if(this.region){
switch(this.region){
case "trailing":
case "right":
_10=rtl?"Left":"Right";
this._needsPosition="left";
break;
case "leading":
case "left":
_10=rtl?"Right":"Left";
break;
case "top":
_10="Top";
break;
case "bottom":
this._needsPosition="top";
_10="Bottom";
break;
}
this.orient=_10;
}
if(!this.startExpanded){
this.startupCls="dojoxExpandoClosed";
}
this._openWidth=null;
if(!this.startExpanded){
if(this.srcNodeRef&&this.srcNodeRef.style.width){
this._openWidth=_9.get(this.srcNodeRef,"width");
var _11=this.style;
if(_11&&_11.toLowerCase().indexOf("width")>-1){
var _12=_11.split(";");
var _13;
for(var i=0;i<_12.length;i++){
if(dojo.trim(_12[i]).length==0){
_12.splice(i,1);
i--;
}else{
_13=_12[i].split(":");
if(dojo.trim(_13[0]).toLowerCase()=="width"){
_12.splice(i,1);
i--;
}
}
}
this.style=_12.length>0?_12.join(";")+";":"";
}
}
}
_2.register("curam/layout/ExpandoPane",this);
},postCreate:function(){
this.inherited(arguments);
this.connect(this.domNode,"ondblclick",this.previewOnDblClick?"preview":"toggle");
this.iconNode.setAttribute("aria-controls",this.id);
if(_b.contains(this.domNode,"smart-panel")){
_3._addDefaultImage(this,!this.startExpanded);
}else{
var img=document.createElement("img");
img.src="";
img.setAttribute("alt",this.iconNode.getAttribute("aria-label"));
img.setAttribute("class","shortcutsPanelIcon");
this.iconNodeLink.appendChild(img);
this._updateImage(!this.startExpanded,false,this);
}
if(this.previewOnDblClick){
this.connect(this.getParent(),"_layoutChildren",dojo.hitch(this,function(){
this._isonlypreview=false;
}));
}
},_startupSizes:function(){
this._container=this.getParent();
this._closedSize=this._titleHeight=_8.getMarginBoxSimple(this.titleWrapper).h;
curam.debug.log(_4.getProperty("curam.layout.ExpandoPane.size")+" "+this._closedSize);
if(this.splitter){
var _14=this.id;
_7.forEach(dijit.registry.toArray(),function(w){
if(w&&w.child&&w.child.id==_14){
this.connect(w,"_stopDrag","_afterResize");
w.setAttribute("aria-label",this.splitterAriaLabel);
}
},this);
}
this._currentSize=_8.getContentBox(this.domNode);
if(this._openWidth){
_4.log(_4.getProperty("curam.layout.ExpandoPane.changing.size.changing")+"currentSize.w "+_4.getProperty("curam.layout.ExpandoPane.changing.size.from")+this._currentSize.w+" "+_4.getProperty("curam.layout.ExpandoPane.changing.size.to")+this._openWidth);
this._currentSize.w=this._openWidth;
}
curam.debug.log("this._currentSize = ",this._currentSize);
this._showSize=this._currentSize[(this._isHorizontal?"h":"w")];
this._setupAnims();
if(this.startExpanded){
this._showing=true;
}else{
this._showing=false;
this._hideWrapper();
}
this.domNode.setAttribute("aria-expanded",this._showing);
this.iconNode.firstElementChild.setAttribute("aria-expanded",this._showing);
this.iconNode.setAttribute("aria-expanded",this._showing);
this._hasSizes=true;
},resize:function(_15){
if(!this._hasSizes){
this._startupSizes(_15);
}
var _16=_8.getMarginBoxSimple(this.domNode);
this._contentBox={w:_15&&"w" in _15?_15.w:_16.w,h:(_15&&"h" in _15?_15.h:_16.h)-this._titleHeight};
_9.set(this.containerNode,"height",this._contentBox.h+"px");
if(_15){
_8.setMarginBox(this.domNode,_15);
}
this._layoutChildren();
},_afterResize:function(e){
var _17=dojox.layout.ExpandoPane.prototype._afterResize;
_17._useMarginBoxSimple=true;
_17.apply(this,arguments);
delete _17._useMarginBoxSimple;
},enterCheck:function(evt){
if(evt.keyCode==13){
this.toggle();
dojo.stopEvent(evt);
}
},toggle:function(){
if(!this._showing){
if(_b.contains(this.domNode,"smart-panel")){
_3.loadSmartPanelIframe(this.domNode);
}
}
this.inherited(arguments);
var _18=dojo.query(".dijitExpandoPane .dijitAccordionTitle").last();
if(_18.length>0){
_b.add(_18,"dijitAccordionTitle-last");
}
this.iconNode.firstElementChild.setAttribute("aria-expanded",this._showing);
this.iconNode.setAttribute("aria-expanded",this._showing);
},_hideWrapper:function(){
_b.add(this.domNode,"dojoxExpandoClosed");
this._updateImage(true,_b.contains(this.domNode,"smart-panel"),this);
_9.set(this.cwrapper,{opacity:"0"});
},_updateImage:function(_19,_1a,_1b){
if(_1a){
_3._updateSmartPanelImage(_1b,_19);
}else{
this._updateShortcutsPanelImage(_19);
}
},_updateShortcutsPanelImage:function(_1c){
if(_1c){
if(this.orient=="Right"){
this.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--left20-on-dark.svg");
}else{
this.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--right20-on-dark.svg");
}
}else{
if(!_1c&&this.orient=="Right"){
this.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--right20-on-dark.svg");
}else{
this.iconNodeLink.children[0].src=require.toUrl("./themes/curam/images/chevron--left20-on-dark.svg");
}
}
},_showEnd:function(){
this._updateImage(false,_b.contains(this.domNode,"smart-panel"),this);
_9.set(this.cwrapper,{opacity:0});
_a.anim(this.cwrapper,{opacity:this._isonlypreview?this.previewOpacity:1},227);
_b.remove(this.domNode,"dojoxExpandoClosed");
if(!this._isonlypreview){
setTimeout(_6.hitch(this._container,"layout"),15);
}else{
this._previewShowing=true;
this.resize();
}
}});
return _f;
});
