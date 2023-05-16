//>>built
require({cache:{"url:curam/layout/resources/CuramAccordionButton.html":"<div data-dojo-attach-event='ondijitclick:_onTitleClick' class='dijitAccordionTitle dijitTestingTest' role=\"presentation\">\n  <div data-dojo-attach-point='titleNode,focusNode' data-dojo-attach-event='onkeydown:_onTitleKeyDown'\n      class='dijitAccordionTitleFocus' role=\"tab\" aria-expanded=\"false\"\n    ><span class='dijitInline dijitAccordionArrow' role=\"presentation\"></span\n    ><span class='arrowTextUp' role=\"presentation\">+</span\n    ><span class='arrowTextDown' role=\"presentation\">-</span\n    ><span role=\"presentation\" class=\"dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span>\n    <span role=\"presentation\" data-dojo-attach-point='titleTextNode, textDirNode' class='dijitAccordionText'></span\n    >\n    <div class=\"accordionButtonWrapper\"><img src=\"${buttonImageSrc}\" data-dojo-attach-point='accordionButtonIcon' class=\"accordionButtonIcon\">\n    </div>\n  </div> \n</div>\n"}});
define("curam/layout/AccordionContainer",["dijit/layout/AccordionContainer","dijit/_CssStateMixin","dijit/focus","dijit/_TemplatedMixin","dijit/_Widget","dojo/_base/array","dojo/_base/declare","dojo/_base/fx","dojo/_base/sniff","dojo/dom","dojo/dom-attr","dojo/dom-geometry","dojo/text!curam/layout/resources/CuramAccordionButton.html","curam/util/ResourceBundle","curam/util"],function(ac,_1,_2,_3,_4,_5,_6,fx,_7,_8,_9,_a,_b,_c){
var _d=_6("dijit.layout._AccordionButton",[_4,_3,_1],{buttonImageSrc:"",templateString:_b,label:"",_setLabelAttr:{node:"titleTextNode",type:"innerHTML"},title:"",_setTitleAttr:{node:"titleTextNode",type:"attribute",attribute:"title"},iconClassAttr:"",_setIconClassAttr:{node:"iconNode",type:"class"},baseClass:"dijitAccordionTitle",getParent:function(){
return this.parent;
},buildRendering:function(){
this.inherited(arguments);
var _e=this.id.replace(" ","_");
_9.set(this.titleTextNode,"id",_e+"_title");
this.focusNode.setAttribute("aria-labelledby",_9.get(this.titleTextNode,"id"));
_8.setSelectable(this.domNode,false);
this._updateShortcutsImage(false);
},getTitleHeight:function(){
return _a.getMarginSize(this.domNode).h;
},_onTitleClick:function(){
var _f=this.getParent();
_f.selectChild(this.contentWidget,true);
_2.focus(this.focusNode);
},_onTitleKeyDown:function(evt){
return this.getParent()._onKeyDown(evt,this.contentWidget);
},_setSelectedAttr:function(_10){
this._set("selected",_10);
this.focusNode.setAttribute("aria-expanded",_10?"true":"false");
this.focusNode.setAttribute("aria-selected",_10?"true":"false");
this.focusNode.setAttribute("tabIndex",_10?"0":"-1");
this._updateShortcutsImage(_10);
},_updateShortcutsImage:function(_11){
var _12=new _c("AccordionContainer");
var _13=this.label;
if(_11){
_9.set(this.accordionButtonIcon,"src",require.toUrl("./themes/curam/images/chevron--up10-on-dark.svg"));
_9.set(this.accordionButtonIcon,"alt",_12.getProperty("singleChevronIcon.expanded.altText",[_13]));
}else{
_9.set(this.accordionButtonIcon,"src",require.toUrl("./themes/curam/images/chevron--down10-on-dark.svg"));
_9.set(this.accordionButtonIcon,"alt",_12.getProperty("singleChevronIcon.contracted.altText",[_13]));
}
}});
var _14=_6("curam.layout.AccordionContainer",dijit.layout.AccordionContainer,{templateString:_b,buttonWidget:_d,layout:function(){
var _15=this.selectedChildWidget;
if(!_15){
return;
}
var _16=_15._wrapperWidget.domNode,_17=_a.getMarginExtents(_16),_18=_a.getPadBorderExtents(_16),_19=_15._wrapperWidget.containerNode,_1a=_a.getMarginExtents(_19),_1b=_a.getPadBorderExtents(_19),_1c=this._contentBox;
var _1d=0;
_5.forEach(this.getChildren(),function(_1e){
_1d+=_1e._buttonWidget.getTitleHeight();
});
var _1f=_15.containerNode.childNodes[0].offsetHeight+20;
if(_1d+_1f<this._contentBox.h){
_1f=this._contentBox.h-_1d;
}
this._containerContentBox={h:_1f,w:this._contentBox.w-_17.w-_18.w-_1a.w-_1b.w};
if(_15){
_15.resize(this._containerContentBox);
}
},_transition:function(_20,_21,_22){
if(_7("ie")<8){
_22=false;
}
if(this._animation){
this._animation.stop(true);
delete this._animation;
}
var _23=this;
if(_20){
_20.resize({h:0,w:this._containerContentBox.w});
var d=this._showChild(_20);
if(this.doLayout&&_20.resize){
var _24=_20.containerNode.childNodes[0].offsetHeight+20;
var _25=0;
dojo.forEach(this.getChildren(),function(_26){
_25+=_26._buttonWidget.getTitleHeight();
});
this._containerContentBox.h=this._contentBox.h-_25;
if(this._containerContentBox.h<_24){
this._containerContentBox.h=_24;
}
this._verticalSpace=_24;
_20.resize(this._containerContentBox);
}
if(!_22){
_20._wrapperWidget.set("selected",true);
}
}
if(_21){
_21._wrapperWidget.set("selected",false);
if(!_22){
this._hideChild(_21);
}
}
if(_22){
var _27=_20._wrapperWidget.containerNode,_28=_21._wrapperWidget.containerNode;
var _29=_20._wrapperWidget.containerNode,_2a=_a.getMarginExtents(_29),_2b=_a.getPadBorderExtents(_29),_2c=_2a.h+_2b.h;
_28.style.height=(_23._verticalSpace-_2c)+"px";
this._animation=new fx.Animation({node:_27,duration:this.duration,curve:[1,this._verticalSpace-_2c-1],onAnimate:function(_2d){
_2d=Math.floor(_2d);
_27.style.height=_2d+"px";
_28.style.height=(_23._verticalSpace-_2c-_2d-1)+"px";
},onEnd:function(){
delete _23._animation;
_27.style.height="auto";
_21._wrapperWidget.containerNode.style.display="none";
_28.style.height="auto";
_23._hideChild(_21);
_20._wrapperWidget.set("selected",true);
}});
this._animation.onStop=this._animation.onEnd;
this._animation.play();
}
return d;
}});
_14._Button=_d;
return _14;
});
