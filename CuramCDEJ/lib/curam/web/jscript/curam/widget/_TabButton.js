//>>built
require({cache:{"url:curam/widget/templates/_TabButton.html":"<div role=\"presentation\" data-dojo-attach-point=\"titleNode,innerDiv,tabContent\" class=\"dijitTab\" id=\"${id}_tabButtonContainer\">\n  <div role=\"presentation\" class='dijitTabInnerDiv'>\n    <div role=\"presentation\" class='dijitTabContent'>\n\t    <span role=\"presentation\" class=\"dijitInline dijitIcon dijitTabButtonIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t    <div role=\"presentation\" aria-labelledby='${id}'>\n\t\t    <span data-dojo-attach-point='containerNode,focusNode' class='tabLabel' id='${id}'></span>\n\t\t    <button class=\"dijitInline dijitTabCloseButton dijitTabCloseIcon\" data-dojo-attach-point='closeNode'\n\t\t\t        tabindex=\"-1\">\n\t\t\t    <span data-dojo-attach-point='closeText' class='dijitTabCloseText'>Close Tab</span>\n        \t</button>\n      </div>\n    </div>\n  </div>\n</div>\n"}});
define("curam/widget/_TabButton",["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-style","dojo/has","dojo/i18n","dojo/_base/lang","dojo/text!curam/widget/templates/_TabButton.html","dijit/registry","dojo/_base/connect","curam/inspection/Layer","dijit/layout/StackController","dijit/Menu","dijit/MenuItem","curam/widget/MenuItem","curam/tab"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c){
_b.subscribe("/curam/tab/labelUpdated",function(){
var _d;
var _e=dojo.query(".dijitTabContainerTop-tabs");
_e.forEach(function(_f){
_d=dojo.query(".tabLabel",_f);
_d.forEach(function(tab,_10){
var _11=_d[_10].innerHTML;
tab.setAttribute("title",_11);
var _12=tab.nextSibling;
while(_12){
if(_4.contains(_12,"dijitTabCloseButton")){
var _13=_12.getAttribute("title");
if(_13&&_13.indexOf(_11)<0){
_12.setAttribute("title",_13+" - "+_11);
}
break;
}
_12=_12.nextSibling;
}
});
});
});
var _14=_1("curam.widget._TabButton",dijit.layout._StackButton,{templateString:_9,_setNameAttr:"focusNode",scrollOnFocus:false,curamDisabled:false,curamVisible:true,baseClass:"dijitTab",postMixInProperties:function(){
if(!this.iconClass){
this.iconClass="dijitTabButtonIcon";
}
},postCreate:function(){
this.inherited(arguments);
_2.setSelectable(this.containerNode,false);
if(this.iconNode.className=="dijitTabButtonIcon"){
_5.set(this.iconNode,"width","1px");
}
_3.set(this.focusNode,"id",this.id);
_c.register("curam/widget/_TabButton",this);
},startup:function(){
dijit.layout._StackButton.prototype.startup.apply(this,arguments);
},_setCloseButtonAttr:function(_15){
this._set("closeButton",_15);
_4.toggle(this.domNode,"dijitClosable",_15);
this.closeNode.style.display=_15?"":"none";
if(_15){
var _16=_7.getLocalization("dijit","common");
if(this.closeNode){
_3.set(this.closeNode,"title",_16.itemClose);
}
}else{
_4.add(this.titleNode,"hasNoCloseButton");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
}
},_setCuramDisabledAttr:function(_17){
this.curamDisabled=_17;
this._swapState(this.domNode,this.curamDisabled,"disabled","enabled");
},_setCuramVisibleAttr:function(_18){
this.curamVisible=_18;
this._swapState(this.domNode,this.curamVisible,"visible","hidden");
},_swapState:function(_19,_1a,_1b,_1c){
if(_1a){
_4.replace(_19,_1b,_1c);
}else{
_4.replace(_19,_1c,_1b);
}
},destroy:function(){
_b.publish("/curam/tab/labelUpdated");
if(this._closeMenu){
this._closeMenu.destroyRecursive();
delete this._closeMenu;
}
this.inherited(arguments);
}});
return _14;
});
