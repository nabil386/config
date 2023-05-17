//>>built
require({cache:{"url:curam/widget/templates/DropDownButton.html":"<span class=\"dijit dijitReset dijitInline\"\n\t><span class='dijitReset dijitInline dijitButtonNode'\n\t\tdata-dojo-attach-event=\"ondijitclick:__onClick\" data-dojo-attach-point=\"_buttonNode\"\n\t\t><span class=\"dijitReset dijitStretch dijitButtonContents\"\n\t\t\tdata-dojo-attach-point=\"focusNode,titleNode,_arrowWrapperNode\"\n\t\t\trole=\"button\" aria-haspopup=\"true\" aria-labelledby=\"${id}_label\"\n\t\t\t><span class=\"dijitReset dijitInline dijitIcon\"\n\t\t\t\tdata-dojo-attach-point=\"iconNode\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitButtonText\"\n\t\t\t\tdata-dojo-attach-point=\"containerNode,_popupStateNode\"\n\t\t\t\tid=\"${id}_label\"\n\t\t\t></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonInner\"></span\n\t\t\t><span class=\"dijitReset dijitInline dijitArrowButtonChar\">&#9660;</span\n\t\t></span\n\t></span\n\t><input ${!nameAttrSetting} type=\"${type}\" value=\"${value}\" class=\"dijitOffScreen\" tabIndex=\"-1\"\n\t\tdata-dojo-attach-event=\"onclick:_onClick\"\n\t\tdata-dojo-attach-point=\"valueNode\" role=\"presentation\" aria-hidden=\"true\"\n/></span>\n"}});
define("curam/widget/DeferredDropDownButton",["dijit/registry","dijit/form/DropDownButton","dojo/_base/declare","dojo/text!curam/widget/templates/DropDownButton.html","dojo/dom-attr","dojo/dom-construct","dojo/query","dijit/form/Button","dijit/MenuItem","curam/util/TabActionsMenu","curam/debug","curam/util","dijit/MenuSeparator","dijit/Menu","dijit/MenuItem","curam/widget/_HasDropDown"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b){
var _c=_3("curam.widget.DeferredDropDownButton",[dijit.form.DropDownButton,curam.widget._HasDropDown],{templateString:_4,o3tabId:null,startup:function(){
if(this._started){
return;
}
var _d=_5.get(this.domNode,"class").split(" ");
dojo.forEach(_d,dojo.hitch(this,function(_e){
if(_e.indexOf("tab-widget-id-")!=-1){
this.o3tabId=_e.slice(14,_e.length);
}
}));
this.widgetTemplate=curam.widgetTemplates?curam.widgetTemplates[this.id]:null;
dijit.form.Button.prototype.startup.apply(this);
var _f=this.get("data-notify");
_f&&dojo.publish("curam/listmenu/started",[this,_f]);
},toggleDropDown:function(){
if(!this.dropDown&&this.widgetTemplate){
if(this.widgetTemplate.indexOf("__03qu_")!=-1){
this.widgetTemplate=this.widgetTemplate.split("__03qu_").join("\"");
}
this.widgetTemplate=this.widgetTemplate.split("&lt;").join("<").split("&gt;").join(">").split("&amp;").join("&").split("&quot;").join("'");
var _10=_6.create("div",{innerHTML:this.widgetTemplate,style:{display:"none"}},dojo.body());
this.dropDown=dojo.parser.parse(_10)[0];
var _11=_1.byNode(_10.firstChild);
if(_11.getChildren().length==0){
var mi=new dijit.MenuItem({disabled:true,label:LOCALISED_EMPTY_MENU_MARKER});
_11.addChild(mi);
}
this.widgetTemplate=null;
_b.log(_b.getProperty("curam.widget.DeferredDropDownButton.publish")+" /curam/menu/created "+_b.getProperty("curam.widget.DeferredDropDownButton.for"),this.o3tabId);
var _12=curam.util.getTopmostWindow();
_12.dojo.publish("/curam/menu/created",[this.o3tabId]);
if(this.o3tabId){
_a.hideTabMenuOverflowItems(this.o3tabId,this.dropDown.domNode);
}
}
this.inherited(arguments);
}});
return _c;
});
