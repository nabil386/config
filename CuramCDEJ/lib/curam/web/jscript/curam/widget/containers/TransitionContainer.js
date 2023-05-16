//>>built
define("curam/widget/containers/TransitionContainer",["dojo/_base/declare","dojo/parser","dijit/_Widget","dojo/dom-construct","dojo/_base/window","dijit/layout/ContentPane","dojo/dom-class","dojo/_base/fx","curam/util/cache/CacheLRU","dojox/layout/ContentPane","dojo/_base/array","dojo/query"],function(_1,_2,_3,_4,_5,_6,_7,fx,_8,_9,_a,_b){
return _1("curam.widget.containers.TransitionContainer",[_6],{transitionDuration:200,_panelCache:null,_currentlyDisplayedPanelKey:-1,_panelToLoadKey:-1,_beenProcessed:false,constructor:function(_c){
var _d={maxSize:5};
this._panelCache=new _8(_d);
},_buildPramUrl:function(_e){
var _f="";
if(_e.param!=null){
_a.forEach(_e.param,function(_10,i){
if(i>0){
_f+="&";
}
_f+=encodeURIComponent(_10.paramKey)+"="+encodeURIComponent(_10.paramValue);
});
}
return _f;
},_setDisplayPanelAttr:function(_11){
_11=this._doDataTranslation(_11);
var _12=this._buildPramUrl(_11);
var _13=_11.key;
if(this._currentlyDisplayedPanelKey!=_13){
this._panelCache.getItem(this._currentlyDisplayedPanelKey);
var _14=this._panelCache.getItem(_13);
if(_14==null){
var uri=this._doResourceLookUp(_11,_12,_13);
uri=this._applyParamToUri(_11,_12,_13,uri);
var _15=new _9({href:uri,preload:false,preventCache:true,executeScripts:true,scriptHasHooks:true,refreshOnShow:false,open:false,style:{padding:0,border:0,opacity:0}});
_15=this._contentPaneCreated(_11,_12,_13,_15);
var _16={node:_15.domNode,duration:this.transitionDuration,onEnd:dojo.hitch(this,this._panelFadeInComplete)};
var _17=dojo.hitch(this,function(key){
this._panelFadedOut(key);
curam.debug.log("TransitionContainer.js calling curam.util.setBrowserTabTitle()");
curam.util.setBrowserTabTitle();
});
var _18={node:_15.domNode,duration:this.transitionDuration,onEnd:function(){
console.info("Fadding out onEnd Called for : "+_13);
_17(_13);
}};
var _19=fx.fadeIn(_16);
var _1a=fx.fadeOut(_18);
_14={panel:_15,fadeIn:_19,fadeOut:_1a};
var _1b={callback:function(key,_1c){
try{
_1c.panel.destroy();
delete _1c;
}
catch(err){
console.error(err);
}
}};
this._panelCache.addItem(_13,_14,_1b);
}else{
console.info("Doning nothing as panel all ready exists");
if(_11.forceRefresh){
var _14=this._panelCache.getItem(_13);
if(_14){
var uri=this._doResourceLookUp(_11,_12,_13);
uri=this._applyParamToUri(_11,_12,_13,uri);
_14.panel.open=false;
_14.panel.set("href",uri);
}
}
}
this._doSwapPanel(_11,_13);
}else{
if(_11.forceRefresh){
var _14=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_14){
var uri=this._doResourceLookUp(_11,_12,this._currentlyDisplayedPanelKey);
uri=this._applyParamToUri(_11,_12,_13,uri);
_14.panel.set("href",uri);
}
}
}
},_doDataTranslation:function(_1d){
return _1d;
},_contentPaneCreated:function(_1e,_1f,_20,_21){
return _21;
},_doResourceLookUp:function(_22,_23,_24){
var uri=_22.key;
return uri;
},_applyParamToUri:function(_25,_26,_27,uri){
if(_26.length>0){
if(uri.indexOf("?")!=-1){
uri+="&";
}else{
uri+="?";
}
uri+=_26;
}
return uri;
},_panelFadedOut:function(_28){
var _29=this._panelCache.getItem(_28);
_29.panel.cancel();
if(_29.panel.domNode!=null){
_7.add(_29.panel.domNode,"dijitHidden");
}else{
}
_29.panel.open=false;
_a.forEach(_b("iframe",_29.panel.domNode),function(_2a){
_2a.src="";
});
this._fadedOutPanelProcess(_29);
_4.place(_29.panel.domNode,_5.body());
this._panelFadeOutComplete();
this._panelFadeIn();
},_fadedOutPanelProcess:function(_2b){
},_panelFadeOutComplete:function(){
},_panelFadeIn:function(){
if(this._panelToFadeInKey!=-1){
var _2c=this._panelCache.getItem(this._panelToFadeInKey);
this.set("content",_2c.panel);
this._currentlyDisplayedPanelKey=this._panelToFadeInKey;
if(_2c.panel.domNode!=null){
_7.remove(_2c.panel.domNode,"dijitHidden");
}else{
}
_2c.panel.onLoad=function(){
_2c.fadeIn.play();
};
_2c.panel.open=true;
_2c.panel.refresh();
_2c.panel.resize();
}
},_panelFadeInComplete:function(){
},_doSwapPanel:function(_2d,key){
var _2e=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_2e!=null){
this.fadeOutDisplay(key);
}else{
this._panelToFadeInKey=key;
this._panelFadeIn();
}
},fadeOutDisplay:function(key){
console.info("fadeOutDisplay");
if(key==null){
key=-1;
}
var _2f=this._panelCache.getItem(this._currentlyDisplayedPanelKey);
if(_2f!=null){
if(_2f.fadeIn.status()=="playing"){
console.info("fadeOutDisplay  - currentlyDisplayedPanel.fadeIn.status() == playing");
_2f.fadeIn.stop();
_2f.fadeOut.play();
}else{
if(_2f.fadeOut.status()!="playing"){
_2f.fadeOut.play();
}
}
this._panelToFadeInKey=key;
}else{
this._panelToFadeInKey=key;
this._panelFadeIn();
}
},destroy:function(){
try{
this._panelCache.destroy();
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
