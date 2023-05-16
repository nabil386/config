//>>built
require({cache:{"url:curam/layout/resources/CuramStackButton.html":"<span class=\"dijit dijitReset dijitInline\" role=\"presentation\">\n  <span class=\"dijitReset dijitInline dijitButtonNode\"\n  data-dojo-attach-event=\"ondijitclick:__onClick\" role=\"presentation\">\n    <span class=\"dijitReset dijitStretch dijitButtonContents\" data-dojo-attach-point=\"titleNode,focusNode\"\n    role=\"button\" aria-labelledby=\"${id}_label\">\n      <span class=\"dijitReset dijitInline dijitIcon\" data-dojo-attach-point=\"iconNode\"></span>\n      <span class=\"dijitReset dijitInline curamButtonText\" id=\"${id}_label\" data-dojo-attach-point=\"containerNode\"></span>\n      <img alt=\"${imageAltText}\" src=\"${imageDefaultSrc}\" class=\"defaultIcon\">\n      <img alt=\"${imageAltText}\" src=\"${imageHoverSrc}\" class=\"hoverIcon\">\n    </span>\n  </span>\n  <input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" data-dojo-attach-event=\"onclick:_onClick\"\n  tabIndex=\"-1\" aria-hidden=\"true\" data-dojo-attach-point=\"valueNode\"/>\n</span>"}});
define("curam/layout/CuramStackController",["dojo/_base/declare","dojo/_base/lang","dojo/_base/array","dojo/dom-attr","dijit/form/ToggleButton","dojo/text!curam/layout/resources/CuramStackButton.html","dijit/layout/StackController"],function(_1,_2,_3,_4,_5,_6,_7){
var _8=_1("curam.layout._StackButton",_5,{tabIndex:"0",templateString:_6,imageDefaultSrc:"",imageHoverSrc:"",imageAltText:"",closeButton:false,_aria_attr:"aria-selected",buildRendering:function(_9){
this.inherited(arguments);
(this.focusNode||this.domNode).setAttribute("role","tab");
}});
var _a=_1("curam.layout.CuramStackController",_7,{baseClass:"curamStackController",buttonWidget:_8,buttonWidgetCloseClass:"dijitStackCloseButton",onAddChild:function(_b,_c){
var _d="../Images/avatar--20-enabled.svg";
var _e="../Images/avatar--20-enabled.svg";
var _f="../Images/list--20-enabled.svg";
var _10="../Images/list--20-enabled.svg";
var _11=_b.get("class").indexOf("stack-container-photo")!=-1?true:false;
var _12=_11?_d:_f;
var _13=_11?_e:_10;
var Cls=_2.isString(this.buttonWidget)?_2.getObject(this.buttonWidget):this.buttonWidget;
var _14=new Cls({id:this.id+"_"+_b.id,name:this.id+"_"+_b.id,disabled:_b.disabled,ownerDocument:this.ownerDocument,dir:_b.dir,lang:_b.lang,textDir:_b.textDir||this.textDir,showLabel:_b.showTitle,imageDefaultSrc:_12,imageHoverSrc:_13,imageAltText:_b.title,iconClass:_b.iconClass,closeButton:_b.closable,title:_b.tooltip,page:_b});
this.addChild(_14,_c);
_b.controlButton=_14;
if(!this._currentChild){
this.onSelectChild(_b);
}
var _15=_4.get(_14.domNode,"widgetid");
if(typeof (_15)!="undefined"){
_15.split("_");
var _16=_15.indexOf("2")!=-1?true:false;
if(_16){
var _17=dojo.query("table.list-body >tbody>tr>td.field.body-first-cell>a.ac",this.ownerDocument);
if(_17){
_3.forEach(_17,function(_18){
_4.set(_18,"class",_18.className.concat("list-view"));
});
}
}
}
var _19=_b._wrapper.getAttribute("aria-labelledby")?_b._wrapper.getAttribute("aria-labelledby")+" "+_14.id:_14.id;
_b._wrapper.removeAttribute("aria-label");
_b._wrapper.setAttribute("aria-labelledby",_19);
}});
_a.StackButton=_8;
return _a;
});
