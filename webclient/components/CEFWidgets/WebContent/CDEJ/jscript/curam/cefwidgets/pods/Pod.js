dojo.provide("curam.cefwidgets.pods.Pod");
require(["curam/cefwidgets/pods/TitlePane","dojo/fx","dijit/form/CheckBox","dijit/layout/ContentPane","dijit/layout/BorderContainer"]);
dojo.declare("curam.cefwidgets.pods.Pod",[curam.cefwidgets.pods.TitlePane],{resizeChildren:true,closable:true,_parents:null,_size:null,dragRestriction:false,podCloseButtonAltText:"",podToggleExpandButtonAltText:"",podToggleCollapseButtonAltText:"",podFilterExpandButtonAltText:"",podFilterCollapseButtonAltText:"",buildRendering:function(){
this.inherited(arguments);
},postMixInProperties:function(){
this.inherited("postMixInProperties",arguments);
this.closeIconAltText=this.podCloseButtonAltText;
if(this.open){
this.togglePodAltText=this.podToggleCollapseButtonAltText;
}else{
this.togglePodAltText=this.podToggleExpandButtonAltText;
}
},postCreate:function(){
this.inherited(arguments);
var _1=this;
if(this.resizeChildren){
this.subscribe("/dnd/drop",function(){
_1._updateSize();
});
this.subscribe("/Portlet/sizechange",function(_2){
_1.onSizeChange(_2);
});
this.connect(window,"onresize",function(){
_1._updateSize();
});
var _3=dojo.hitch(this,function(id,_4){
var _5=dijit.byId(id);
if(_5.selectChild){
var s=this.subscribe(id+"-selectChild",function(_6){
var n=_1.domNode.parentNode;
while(n){
if(n==_6.domNode){
_1.unsubscribe(s);
_1._updateSize();
break;
}
n=n.parentNode;
}
});
var _7=dijit.byId(_4);
if(_5&&_7){
_1._parents.push({parent:_5,child:_7});
}
}
});
var _8;
this._parents=[];
for(var p=this.domNode.parentNode;p!=null;p=p.parentNode){
var id=p.getAttribute?p.getAttribute("widgetId"):null;
if(id){
_3(id,_8);
_8=id;
}
}
}
this.connect(this.titleBarNode,"onmousedown",function(_9){
if(dojo.hasClass(_9.target,"dojoxPortletIcon")){
dojo.stopEvent(_9);
return false;
}
return true;
});
this.connect(this._wipeOut,"onEnd",function(){
_1._publish();
});
this.connect(this._wipeIn,"onEnd",function(){
_1._publish();
});
dojo.removeAttr(this.titleBarNode,"tabindex");
dojo.attr(this.domNode,"role", "list");
dojo.attr(this.titleBarNode,"role", "listitem");
dojo.removeAttr(this.titleBarNode,"aria-pressed");
},startup:function(){
if(this._started){
return;
}
var _a=this.getChildren();
this._placeSettingsWidgets();
dojo.forEach(_a,function(_b){
try{
if(!_b.started&&!_b._started){
_b.startup();
}
}
catch(e){
console.log(this.id+":"+this.declaredClass,e);
}
});
this.inherited(arguments);
},_placeSettingsWidgets:function(){
console.log("curam.cefwidgets.pods.Pod ::: _placeSettingsWidgets");
dojo.forEach(this.getChildren(),dojo.hitch(this,function(_c){
if(_c.portletIconClass&&_c.toggle&&!_c.attr("portlet")){
this._createIcon(_c.portletIconClass,_c.portletIconHoverClass,_c.portletIconSelectClass,dojo.hitch(_c,"toggle"));
dojo.place(_c.domNode,this.containerNode,"before");
_c.attr("portlet",this);
this._settingsWidget=_c;
}
}));
},_createIcon:function(_d,_e,_f,fn){
var _10=dojo.create("button",{"class":"dojoxPortletIcon "+_d,"waiRole":"button","tabindex":"0","role":"button","type": "button"});
var _11=dojo.create("span",{"class":"hiddenControlForScreenReader"});
_11.innerHTML=this.podFilterExpandButtonAltText;
dojo.place(_11,_10,"first");
dojo.place(_10,this.closeNode,"before");
this.connect(_10,"onclick",fn);
this.connect(_10,"onkeypress",fn);
this.connect(_10,"onclick",function(){
if(this._settingsWidget.visible){
dojo.addClass(_10,_f);
_11.innerHTML=this.podFilterCollapseButtonAltText;
}else{
dojo.removeClass(_10,_f);
_11.innerHTML=this.podFilterExpandButtonAltText;
}
});
this.connect(_10,"onkeypress",function(){
if(this._settingsWidget.visible){
dojo.addClass(_10,_f);
_11.innerHTML=this.podFilterCollapseButtonAltText;
}else{
dojo.removeClass(_10,_f);
_11.innerHTML=this.podFilterExpandButtonAltText;
}
});
if(_e){
this.connect(_10,"onfocus",function(){
dojo.addClass(_10,_e);
});
this.connect(_10,"onmouseover",function(){
dojo.addClass(_10,_e);
});
this.connect(_10,"onblur",function(){
dojo.removeClass(_10,_e);
});
this.connect(_10,"onmouseout",function(){
dojo.removeClass(_10,_e);
});
this.connect(_10,"onkeydown",function(){
dojo.addClass(_10,_f);
});
this.connect(_10,"onmousedown",function(){
dojo.addClass(_10,_f);
});
}
return _10;
},onClose:function(evt){
if(evt.type==="keyup"||evt.type==="keydown"){
if(CEFUtils.enterKeyPress(evt)!==true){
return false;
}
}
dojo.style(this.domNode,"display","none");
dojo.attr(this.domNode,"closed","true");
dojo.publish("/dojox/mdnd/close",[this]);
},mouseenterClose:function(){
dojo.addClass(this.closeNode,"dojoxCloseNodeHover");
},mouseoutClose:function(){
dojo.removeClass(this.closeNode,"dojoxCloseNodeHover");
},mousedownClose:function(){
dojo.addClass(this.closeNode,"dojoxCloseNodeSelect");
},mouseupClose:function(){
dojo.removeClass(this.closeNode,"dojoxCloseNodeSelect");
},onSizeChange:function(_12){
if(_12==this){
return;
}
this._updateSize();
},_updateSize:function(){
if(!this.open||!this._started||!this.resizeChildren){
return;
}
if(this._timer){
clearTimeout(this._timer);
}
this._timer=setTimeout(dojo.hitch(this,function(){
var _13={w:dojo.style(this.domNode,"width"),h:dojo.style(this.domNode,"height")};
for(var i=0;i<this._parents.length;i++){
var p=this._parents[i];
var sel=p.parent.selectedChildWidget;
if(sel&&sel!=p.child){
return;
}
}
if(this._size){
if(this._size.w==_13.w&&this._size.h==_13.h){
return;
}
}
this._size=_13;
var fns=["resize","layout"];
this._timer=null;
var _14=this.getChildren();
dojo.forEach(_14,function(_15){
for(var i=0;i<fns.length;i++){
if(dojo.isFunction(_15[fns[i]])){
try{
_15[fns[i]]();
}
catch(e){
console.log(e);
}
break;
}
}
});
this.onUpdateSize();
}),100);
},onUpdateSize:function(){
},_publish:function(){
dojo.publish("/Portlet/sizechange",[this]);
},_onTitleClick:function(evt){
if(evt.target==this.arrowNode){
this.inherited(arguments);
}
},addChild:function(_16){
this._size=null;
this.inherited(arguments);
if(this._started){
this._placeSettingsWidgets();
this._updateSize();
}
if(this._started&&!_16.started&&!_16._started){
_16.startup();
}
},destroyDescendants:function(_17){
this.inherited(arguments);
if(this._settingsWidget){
this._settingsWidget.destroyRecursive(_17);
}
},_setCss:function(){
this.inherited(arguments);
dojo.style(this.arrowNode,"display",this.toggleable?"":"none");
if(this.open){
dojo.attr(this.arrowNode,"alt",this.podToggleCollapseButtonAltText);
}else{
dojo.attr(this.arrowNode,"alt",this.podToggleExpandButtonAltText);
}
}});
dojo.declare("curam.cefwidgets.pods.PodSettings",[dijit._Container,dijit.layout.ContentPane],{portletIconClass:"dojoxPortletSettingsIcon",portletIconHoverClass:"dojoxPortletSettingsIconHover",portletIconSelectClass:"dojoxPortletSettingsIconSelect",visible:true,postCreate:function(){
this.inherited(arguments);
dojo.style(this.domNode,"display","none");
dojo.addClass(this.domNode,"dojoxPortletSettingsContainer");
dojo.removeClass(this.domNode,"dijitContentPane");
},_setPortletAttr:function(_18){
this.portlet=_18;
},toggle:function(_19){
if(_19.type==="keypress"){
if(CEFUtils.enterKeyPress(_19)!==true){
return false;
}else{
dojo.stopEvent(_19);
}
}
var n=this.domNode;
if(dojo.style(n,"display")=="none"){
dojo.style(n,{"display":"block","height":"1px","width":"auto"});
dojo.fx.wipeIn({node:n}).play();
this.visible=true;
}else{
dojo.fx.wipeOut({node:n,onEnd:dojo.hitch(this,function(){
dojo.style(n,{"display":"none","height":"","width":""});
})}).play();
this.visible=false;
}
}});
dojo.declare("curam.cefwidgets.pods.PodDialogSettings",curam.cefwidgets.pods.PodSettings,{dimensions:null,constructor:function(_1a,_1b){
this.dimensions=_1a.dimensions||[300,100];
},toggle:function(){
if(!this.dialog){
dojo["require"]("dijit.Dialog");
this.dialog=new dijit.Dialog({title:this.title});
dojo.body().appendChild(this.dialog.domNode);
this.dialog.containerNode.appendChild(this.domNode);
dojo.style(this.dialog.domNode,{"width":this.dimensions[0]+"px","height":this.dimensions[1]+"px"});
dojo.style(this.domNode,"display","");
}
if(this.dialog.open){
this.dialog.hide();
}else{
this.dialog.show(this.domNode);
}
}});

