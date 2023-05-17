//>>built
define("curam/FastUIMController",["dojo/_base/declare","dojo/parser","dijit/registry","curam/inspection/Layer","curam/UIMController","curam/debug","dojo/dom-class","dojo/dom-style","dojo/dom-attr","curam/util/onLoad"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
var _a=_1("curam.FastUIMController",[curam.UIMController],{buildRendering:function(){
this.domNode=this.srcNodeRef;
this._attachTemplateNodes(this.domNode,function(_b,_c){
return _b.getAttribute(_c);
});
},postCreate:function(){
_4.register("curam/FastUIMController",this);
},startup:function(){
this.tabController=_3.byId(this.tabControllerId);
_9.set(this.frame,"iscpiframe",this.iscpiframe);
_9.set(this.frame,"title",this.title);
_7.add(this.frame,this.iframeClassList);
_7.add(this.domNode,this.classList);
this.frameLoadEvent=this.EVENT.TOPIC_PREFIX+this.frame.id;
this.setURL(this.url);
if(this._iframeLoaded()){
_6.log("curam.FastUIMController "+_6.getProperty("curam.FastUIMControlle.msg"));
}else{
var _d=dojo.hitch(this,"processFrameLoadEvent");
curam.util.onLoad.addSubscriber(this.frame.id,_d);
dojo.connect(this,"destroy",function(){
curam.util.onLoad.removeSubscriber(this.iframeId,_d);
_d=null;
});
}
if(this.inDialog){
_8.set(this.frame,{width:this.width,height:this.height});
}
},_iframeLoaded:function(){
return _9.get(this.frame,"data-done-loading")=="true";
}});
return _a;
});
