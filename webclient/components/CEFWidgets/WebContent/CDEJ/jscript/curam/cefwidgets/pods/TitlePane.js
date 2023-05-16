dojo.provide("curam.cefwidgets.pods.TitlePane");
require(["dijit/TitlePane","curam/util/ResourceBundle","dojo/text","dojo/cache"]);
dojo.requireLocalization("curam.application","TitlePane");
dojo.declare("curam.cefwidgets.pods.TitlePane",[dijit.TitlePane,dijit._WidgetsInTemplateMixin],{closeIconAltText:"",togglePodAltText:"",templateString:dojo.cache("curam","cefwidgets/pods/templates/TitlePane.html"),collapsible:true,constructor:function(){
var _1="close.icon.alt.text";
var _2="toggle.pod.alt.text";
var _3=new curam.util.ResourceBundle("TitlePane");
this.closeIconAltText=_3.getProperty(_1);
this.togglePodAltText=_3.getProperty(_2);
},postCreate:function(){
this.inherited(arguments);
if(!this.collapsible){
dojo.style(this.arrowNode,"display","none");
}else{
dojo.addClass(this.domNode,"dijitTitlePaneTitle-collapsible");
}
dojo.forEach(this.getChildren(),function(_4){
if(!_4.started&&!_4._started){
_4.startup();
}
});
dojo.addClass(this.wipeNode,"dijitTitlePaneContentWipeNode");
dojo.connect(this._wipeOut,"onEnd",this,"_publish");
dojo.connect(this._wipeIn,"onEnd",this,"_publish");
dojo.connect(this,"onKeyPress",this,function(_5){
if(_5.type==="keyup"||_5.type==="keydown"){
if(CEFUtils.enterKeyPress(_5)!==true){
return false;
}
}
if((_5.type==="keypress")&&(CEFUtils.enterKeyPress(_5)===true)){
this.toggle();
}
});
},addChild:function(_6){
this.inherited(arguments);
},_publish:function(){
dojo.publish("/TitlePane/sizechange",[this]);
},toggle:function(){
if(!this.collapsible){
return;
}
this.inherited(arguments);
}});

