//>>built
require({cache:{"dojo/request/default":function(){
define(["exports","require","../has"],function(_1,_2,_3){
var _4=_3("config-requestProvider"),_5;
if(1||_3("host-webworker")){
_5="./xhr";
}else{
if(0){
_5="./node";
}
}
if(!_4){
_4=_5;
}
_1.getPlatformDefaultId=function(){
return _5;
};
_1.load=function(id,_6,_7,_8){
_2([id=="platform"?_5:_4],function(_9){
_7(_9);
});
};
});
},"dijit/popup":function(){
define("dijit/popup",["dojo/_base/array","dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/keys","dojo/_base/lang","dojo/on","./place","./BackgroundIframe","./Viewport","./main","dojo/touch"],function(_a,_b,_c,_d,_e,_f,_10,_11,_12,has,_13,_14,on,_15,_16,_17,_18){
function _19(){
if(this._popupWrapper){
_10.destroy(this._popupWrapper);
delete this._popupWrapper;
}
};
var _1a=_c(null,{_stack:[],_beginZIndex:1000,_idGen:1,_repositionAll:function(){
if(this._firstAroundNode){
var _1b=this._firstAroundPosition,_1c=_11.position(this._firstAroundNode,true),dx=_1c.x-_1b.x,dy=_1c.y-_1b.y;
if(dx||dy){
this._firstAroundPosition=_1c;
for(var i=0;i<this._stack.length;i++){
var _1d=this._stack[i].wrapper.style;
_1d.top=(parseFloat(_1d.top)+dy)+"px";
if(_1d.right=="auto"){
_1d.left=(parseFloat(_1d.left)+dx)+"px";
}else{
_1d.right=(parseFloat(_1d.right)-dx)+"px";
}
}
}
this._aroundMoveListener=setTimeout(_14.hitch(this,"_repositionAll"),dx||dy?10:50);
}
},_createWrapper:function(_1e){
var _1f=_1e._popupWrapper,_20=_1e.domNode;
if(!_1f){
_1f=_10.create("div",{"class":"dijitPopup",style:{display:"none"},role:"region","aria-label":_1e["aria-label"]||_1e.label||_1e.name||_1e.id},_1e.ownerDocumentBody);
_1f.appendChild(_20);
var s=_20.style;
s.display="";
s.visibility="";
s.position="";
s.top="0px";
_1e._popupWrapper=_1f;
_b.after(_1e,"destroy",_19,true);
if("ontouchend" in document){
on(_1f,"touchend",function(evt){
if(!/^(input|button|textarea)$/i.test(evt.target.tagName)){
evt.preventDefault();
}
});
}
_1f.dojoClick=true;
}
return _1f;
},moveOffScreen:function(_21){
var _22=this._createWrapper(_21);
var ltr=_11.isBodyLtr(_21.ownerDocument),_23={visibility:"hidden",top:"-9999px",display:""};
_23[ltr?"left":"right"]="-9999px";
_23[ltr?"right":"left"]="auto";
_12.set(_22,_23);
return _22;
},hide:function(_24){
var _25=this._createWrapper(_24);
if(dojo.hasClass(_25,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
_e.set(_25,"aria-hidden","true");
_12.set(_25,{position:"absolute",overflow:"hidden",clip:"rect(0 0 0 0)",height:"1px",width:"1px",margin:"-1px",padding:"0",border:"0"});
setTimeout(function(){
if(_e.get(_25,"aria-hidden","true")){
_12.set(_25,{display:"none",height:"auto",overflowY:"visible",border:"",position:"",overflow:"",clip:"",width:"",margin:"",padding:""});
}
},200,_25);
}else{
_12.set(_25,{display:"none",height:"auto",overflowY:"visible",border:""});
}
var _26=_24.domNode;
if("_originalStyle" in _26){
_26.style.cssText=_26._originalStyle;
}
},getTopPopup:function(){
var _27=this._stack;
for(var pi=_27.length-1;pi>0&&_27[pi].parent===_27[pi-1].widget;pi--){
}
return _27[pi];
},open:function(_28){
var _29=this._stack,_2a=_28.popup,_2b=_2a.domNode,_2c=_28.orient||["below","below-alt","above","above-alt"],ltr=_28.parent?_28.parent.isLeftToRight():_11.isBodyLtr(_2a.ownerDocument),_2d=_28.around,id=(_28.around&&_28.around.id)?(_28.around.id+"_dropdown"):("popup_"+this._idGen++);
while(_29.length&&(!_28.parent||!_d.isDescendant(_28.parent.domNode,_29[_29.length-1].widget.domNode))){
this.close(_29[_29.length-1].widget);
}
var _2e=this.moveOffScreen(_2a);
if(_2a.startup&&!_2a._started){
_2a.startup();
}
var _2f,_30=_11.position(_2b);
if("maxHeight" in _28&&_28.maxHeight!=-1){
_2f=_28.maxHeight||Infinity;
}else{
var _31=_17.getEffectiveBox(this.ownerDocument),_32=_2d?_11.position(_2d,false):{y:_28.y-(_28.padding||0),h:(_28.padding||0)*2};
_2f=Math.floor(Math.max(_32.y,_31.h-(_32.y+_32.h)));
}
if(_30.h>_2f){
var cs=_12.getComputedStyle(_2b),_33=cs.borderLeftWidth+" "+cs.borderLeftStyle+" "+cs.borderLeftColor;
_12.set(_2e,{overflowY:"scroll",height:(_2f-2)+"px",border:_33});
_2b._originalStyle=_2b.style.cssText;
_2b.style.border="none";
}
_e.set(_2e,{id:id,style:{zIndex:this._beginZIndex+_29.length},"class":"dijitPopup "+(_2a.baseClass||_2a["class"]||"").split(" ")[0]+"Popup",dijitPopupParent:_28.parent?_28.parent.id:""});
if(dojo.hasClass(_2e,"dijitMenuPopup")&&(has("trident")||has("edge")||has("ie"))){
if(_e.get(_2e,"aria-hidden")==="true"){
_12.set(_2e,{position:"",clip:"",width:"",margin:"",padding:"",border:""});
}
_e.set(_2e,"aria-hidden","false");
}
if(_29.length==0&&_2d){
this._firstAroundNode=_2d;
this._firstAroundPosition=_11.position(_2d,true);
this._aroundMoveListener=setTimeout(_14.hitch(this,"_repositionAll"),50);
}
if(has("config-bgIframe")&&!_2a.bgIframe){
_2a.bgIframe=new _16(_2e);
}
var _34=_2a.orient?_14.hitch(_2a,"orient"):null,_35=_2d?_15.around(_2e,_2d,_2c,ltr,_34):_15.at(_2e,_28,_2c=="R"?["TR","BR","TL","BL"]:["TL","BL","TR","BR"],_28.padding,_34);
_2e.style.visibility="visible";
_2b.style.visibility="visible";
var _36=[];
_36.push(on(_2e,"keydown",_14.hitch(this,function(evt){
if(evt.keyCode==_13.ESCAPE&&_28.onCancel){
evt.stopPropagation();
evt.preventDefault();
_28.onCancel(evt);
}else{
if(evt.keyCode==_13.TAB){
evt.stopPropagation();
evt.preventDefault();
var _37=this.getTopPopup();
if(_37&&_37.onCancel){
_37.onCancel(evt);
}
}
}
})));
if(_2a.onCancel&&_28.onCancel){
_36.push(_2a.on("cancel",_28.onCancel));
}
_36.push(_2a.on(_2a.onExecute?"execute":"change",_14.hitch(this,function(){
var _38=this.getTopPopup();
if(_38&&_38.onExecute){
_38.onExecute();
}
})));
_29.push({widget:_2a,wrapper:_2e,parent:_28.parent,onExecute:_28.onExecute,onCancel:_28.onCancel,onClose:_28.onClose,handlers:_36});
if(_2a.onOpen){
_2a.onOpen(_35);
}
return _35;
},close:function(_39){
var _3a=this._stack;
while((_39&&_a.some(_3a,function(_3b){
return _3b.widget==_39;
}))||(!_39&&_3a.length)){
var top=_3a.pop(),_3c=top.widget,_3d=top.onClose;
if(_3c.bgIframe){
_3c.bgIframe.destroy();
delete _3c.bgIframe;
}
if(_3c.onClose){
_3c.onClose();
}
var h;
while(h=top.handlers.pop()){
h.remove();
}
if(_3c&&_3c.domNode){
this.hide(_3c);
}
if(_3d){
_3d();
}
}
if(_3a.length==0&&this._aroundMoveListener){
clearTimeout(this._aroundMoveListener);
this._firstAroundNode=this._firstAroundPosition=this._aroundMoveListener=null;
}
}});
return (_18.popup=new _1a());
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(_3e,_3f){
var _40=/[&<>'"\/]/g;
var _41={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;","/":"&#x2F;"};
var _42={};
_3f.setObject("dojo.string",_42);
_42.escape=function(str){
if(!str){
return "";
}
return str.replace(_40,function(c){
return _41[c];
});
};
_42.rep=function(str,num){
if(num<=0||!str){
return "";
}
var buf=[];
for(;;){
if(num&1){
buf.push(str);
}
if(!(num>>=1)){
break;
}
str+=str;
}
return buf.join("");
};
_42.pad=function(_43,_44,ch,end){
if(!ch){
ch="0";
}
var out=String(_43),pad=_42.rep(ch,Math.ceil((_44-out.length)/ch.length));
return end?out+pad:pad+out;
};
_42.substitute=function(_45,map,_46,_47){
_47=_47||_3e.global;
_46=_46?_3f.hitch(_47,_46):function(v){
return v;
};
return _45.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_48,key,_49){
var _4a=_3f.getObject(key,false,map);
if(_49){
_4a=_3f.getObject(_49,false,_47).call(_47,_4a,key);
}
return _46(_4a,key).toString();
});
};
_42.trim=String.prototype.trim?_3f.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return _42;
});
},"dijit/a11y":function(){
define(["dojo/_base/array","dojo/dom","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/sniff","./main"],function(_4b,dom,_4c,_4d,_4e,has,_4f){
var _50;
var _51={_isElementShown:function(_52){
var s=_4d.get(_52);
return (s.visibility!="hidden")&&(s.visibility!="collapsed")&&(s.display!="none")&&(_4c.get(_52,"type")!="hidden");
},hasDefaultTabStop:function(_53){
switch(_53.nodeName.toLowerCase()){
case "a":
return _4c.has(_53,"href");
case "area":
case "button":
case "input":
case "object":
case "select":
case "textarea":
return true;
case "iframe":
var _54;
try{
var _55=_53.contentDocument;
if("designMode" in _55&&_55.designMode=="on"){
return true;
}
_54=_55.body;
}
catch(e1){
try{
_54=_53.contentWindow.document.body;
}
catch(e2){
return false;
}
}
return _54&&(_54.contentEditable=="true"||(_54.firstChild&&_54.firstChild.contentEditable=="true"));
default:
return _53.contentEditable=="true";
}
},effectiveTabIndex:function(_56){
if(_4c.get(_56,"disabled")){
return _50;
}else{
if(_4c.has(_56,"tabIndex")){
return +_4c.get(_56,"tabIndex");
}else{
return _51.hasDefaultTabStop(_56)?0:_50;
}
}
},isTabNavigable:function(_57){
return _51.effectiveTabIndex(_57)>=0;
},isFocusable:function(_58){
return _51.effectiveTabIndex(_58)>=-1;
},_getTabNavigable:function(_59){
var _5a,_5b,_5c,_5d,_5e,_5f,_60={};
function _61(_62){
return _62&&_62.tagName.toLowerCase()=="input"&&_62.type&&_62.type.toLowerCase()=="radio"&&_62.name&&_62.name.toLowerCase();
};
var _63=_51._isElementShown,_64=_51.effectiveTabIndex;
var _65=function(_66){
for(var _67=_66.firstChild;_67;_67=_67.nextSibling){
if(_67.nodeType!=1||(has("ie")<=9&&_67.scopeName!=="HTML")||!_63(_67)){
continue;
}
var _68=_64(_67);
if(_68>=0){
if(_68==0){
if(!_5a){
_5a=_67;
}
_5b=_67;
}else{
if(_68>0){
if(!_5c||_68<_5d){
_5d=_68;
_5c=_67;
}
if(!_5e||_68>=_5f){
_5f=_68;
_5e=_67;
}
}
}
var rn=_61(_67);
if(_4c.get(_67,"checked")&&rn){
_60[rn]=_67;
}
}
if(_67.nodeName.toUpperCase()!="SELECT"){
_65(_67);
}
}
};
if(_63(_59)){
_65(_59);
}
function rs(_69){
return _60[_61(_69)]||_69;
};
return {first:rs(_5a),last:rs(_5b),lowest:rs(_5c),highest:rs(_5e)};
},getFirstInTabbingOrder:function(_6a,doc){
var _6b=_51._getTabNavigable(dom.byId(_6a,doc));
return _6b.lowest?_6b.lowest:_6b.first;
},getLastInTabbingOrder:function(_6c,doc){
var _6d=_51._getTabNavigable(dom.byId(_6c,doc));
return _6d.last?_6d.last:_6d.highest;
}};
1&&_4e.mixin(_4f,_51);
return _51;
});
},"dijit/WidgetSet":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/_base/kernel","./registry"],function(_6e,_6f,_70,_71){
var _72=_6f("dijit.WidgetSet",null,{constructor:function(){
this._hash={};
this.length=0;
},add:function(_73){
if(this._hash[_73.id]){
throw new Error("Tried to register widget with id=="+_73.id+" but that id is already registered");
}
this._hash[_73.id]=_73;
this.length++;
},remove:function(id){
if(this._hash[id]){
delete this._hash[id];
this.length--;
}
},forEach:function(_74,_75){
_75=_75||_70.global;
var i=0,id;
for(id in this._hash){
_74.call(_75,this._hash[id],i++,this._hash);
}
return this;
},filter:function(_76,_77){
_77=_77||_70.global;
var res=new _72(),i=0,id;
for(id in this._hash){
var w=this._hash[id];
if(_76.call(_77,w,i++,this._hash)){
res.add(w);
}
}
return res;
},byId:function(id){
return this._hash[id];
},byClass:function(cls){
var res=new _72(),id,_78;
for(id in this._hash){
_78=this._hash[id];
if(_78.declaredClass==cls){
res.add(_78);
}
}
return res;
},toArray:function(){
var ar=[];
for(var id in this._hash){
ar.push(this._hash[id]);
}
return ar;
},map:function(_79,_7a){
return _6e.map(this.toArray(),_79,_7a);
},every:function(_7b,_7c){
_7c=_7c||_70.global;
var x=0,i;
for(i in this._hash){
if(!_7b.call(_7c,this._hash[i],x++,this._hash)){
return false;
}
}
return true;
},some:function(_7d,_7e){
_7e=_7e||_70.global;
var x=0,i;
for(i in this._hash){
if(_7d.call(_7e,this._hash[i],x++,this._hash)){
return true;
}
}
return false;
}});
_6e.forEach(["forEach","filter","byClass","map","every","some"],function(_7f){
_71[_7f]=_72.prototype[_7f];
});
return _72;
});
},"dijit/_base/wai":function(){
define(["dojo/dom-attr","dojo/_base/lang","../main","../hccss"],function(_80,_81,_82){
var _83={hasWaiRole:function(_84,_85){
var _86=this.getWaiRole(_84);
return _85?(_86.indexOf(_85)>-1):(_86.length>0);
},getWaiRole:function(_87){
return _81.trim((_80.get(_87,"role")||"").replace("wairole:",""));
},setWaiRole:function(_88,_89){
_80.set(_88,"role",_89);
},removeWaiRole:function(_8a,_8b){
var _8c=_80.get(_8a,"role");
if(!_8c){
return;
}
if(_8b){
var t=_81.trim((" "+_8c+" ").replace(" "+_8b+" "," "));
_80.set(_8a,"role",t);
}else{
_8a.removeAttribute("role");
}
},hasWaiState:function(_8d,_8e){
return _8d.hasAttribute?_8d.hasAttribute("aria-"+_8e):!!_8d.getAttribute("aria-"+_8e);
},getWaiState:function(_8f,_90){
return _8f.getAttribute("aria-"+_90)||"";
},setWaiState:function(_91,_92,_93){
_91.setAttribute("aria-"+_92,_93);
},removeWaiState:function(_94,_95){
_94.removeAttribute("aria-"+_95);
}};
_81.mixin(_82,_83);
return _82;
});
},"dijit/Viewport":function(){
define(["dojo/Evented","dojo/on","dojo/domReady","dojo/sniff","dojo/window"],function(_96,on,_97,has,_98){
var _99=new _96();
var _9a;
_97(function(){
var _9b=_98.getBox();
_99._rlh=on(window,"resize",function(){
var _9c=_98.getBox();
if(_9b.h==_9c.h&&_9b.w==_9c.w){
return;
}
_9b=_9c;
_99.emit("resize");
});
if(has("ie")==8){
var _9d=screen.deviceXDPI;
setInterval(function(){
if(screen.deviceXDPI!=_9d){
_9d=screen.deviceXDPI;
_99.emit("resize");
}
},500);
}
if(has("ios")){
on(document,"focusin",function(evt){
_9a=evt.target;
});
on(document,"focusout",function(evt){
_9a=null;
});
}
});
_99.getEffectiveBox=function(doc){
var box=_98.getBox(doc);
var tag=_9a&&_9a.tagName&&_9a.tagName.toLowerCase();
if(has("ios")&&_9a&&!_9a.readOnly&&(tag=="textarea"||(tag=="input"&&/^(color|email|number|password|search|tel|text|url)$/.test(_9a.type)))){
box.h*=(orientation==0||orientation==180?0.66:0.4);
var _9e=_9a.getBoundingClientRect();
box.h=Math.max(box.h,_9e.top+_9e.height);
}
return box;
};
return _99;
});
},"dojo/hccss":function(){
define(["require","./_base/config","./dom-class","./dom-style","./has","./domReady","./_base/window"],function(_9f,_a0,_a1,_a2,has,_a3,win){
has.add("highcontrast",function(){
var div=win.doc.createElement("div");
div.style.cssText="border: 1px solid; border-color:red green; position: absolute; height: 5px; top: -999px;"+"background-image: url(\""+(_a0.blankGif||_9f.toUrl("./resources/blank.gif"))+"\");";
win.body().appendChild(div);
var cs=_a2.getComputedStyle(div),_a4=cs.backgroundImage,hc=(cs.borderTopColor==cs.borderRightColor)||(_a4&&(_a4=="none"||_a4=="url(invalid-url:)"));
if(has("ie")<=8){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
return hc;
});
_a3(function(){
if(has("highcontrast")){
_a1.add(win.body(),"dj_a11y");
}
});
return has;
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_a5,_a6,_a7,_a8,_a9,_aa,dom,_ab,_ac,_ad,_ae,_af,has,_b0,_b1,on,_b2,_b3,_b4,win,_b5,_b6,_b7){
var _b8=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_b0.isAsync);
has.add("dojo-bidi",false);
if(1){
_b2(0,function(){
var _b9=["dijit/_base/manager"];
_a5(_b9);
});
}
var _ba={};
function _bb(obj){
var ret={};
for(var _bc in obj){
ret[_bc.toLowerCase()]=true;
}
return ret;
};
function _bd(_be){
return function(val){
_ab[val?"set":"remove"](this.domNode,_be,val);
this._set(_be,val);
};
};
function _bf(a,b){
return a===b||(a!==a&&b!==b);
};
var _c0=_aa("dijit._WidgetBase",[_b3,_b5],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_bd("lang"),dir:"",_setDirAttr:_bd("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_a8.blankGif||_a5.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var _c1=this.constructor;
if(!_c1._setterAttrs){
var _c2=_c1.prototype,_c3=_c1._setterAttrs=[],_c4=(_c1._onMap={});
for(var _c5 in _c2.attributeMap){
_c3.push(_c5);
}
for(_c5 in _c2){
if(/^on/.test(_c5)){
_c4[_c5.substring(2).toLowerCase()]=_c5;
}
if(/^_set[A-Z](.*)Attr$/.test(_c5)){
_c5=_c5.charAt(4).toLowerCase()+_c5.substr(5,_c5.length-9);
if(!_c2.attributeMap||!(_c5 in _c2.attributeMap)){
_c3.push(_c5);
}
}
}
}
},postscript:function(_c6,_c7){
this.create(_c6,_c7);
},create:function(_c8,_c9){
if(_b8){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_c9);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_c8){
this.params=_c8;
_b1.mixin(this,_c8);
}
this.postMixInProperties();
if(!this.id){
this.id=_b7.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_b7.add(this);
this.buildRendering();
var _ca;
if(this.domNode){
this._applyAttributes();
var _cb=this.srcNodeRef;
if(_cb&&_cb.parentNode&&this.domNode!==_cb){
_cb.parentNode.replaceChild(this.domNode,_cb);
_ca=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_ca){
delete this.srcNodeRef;
}
this._created=true;
if(_b8){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _cc={};
for(var key in this.params||{}){
_cc[key]=this._get(key);
}
_a6.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _cc)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _cc){
this.set(key,_cc[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _cd=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_cd=_cd.concat(_a6.map(_cd,function(_ce){
return _ce+"Rtl";
}));
}
_ac.add(this.domNode,_cd);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_a6.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&_b1.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_cf){
this._beingDestroyed=true;
this.destroyDescendants(_cf);
this.destroy(_cf);
},destroy:function(_d0){
this._beingDestroyed=true;
this.uninitialize();
function _d1(w){
if(w.destroyRecursive){
w.destroyRecursive(_d0);
}else{
if(w.destroy){
w.destroy(_d0);
}
}
};
_a6.forEach(this._connects,_b1.hitch(this,"disconnect"));
_a6.forEach(this._supportingWidgets,_d1);
if(this.domNode){
_a6.forEach(_b7.findWidgets(this.domNode,this.containerNode),_d1);
}
this.destroyRendering(_d0);
_b7.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_d2){
if(this.bgIframe){
this.bgIframe.destroy(_d2);
delete this.bgIframe;
}
if(this.domNode){
if(_d2){
_ab.remove(this.domNode,"widgetId");
}else{
_ad.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_d2){
_ad.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_d3){
_a6.forEach(this.getChildren(),function(_d4){
if(_d4.destroyRecursive){
_d4.destroyRecursive(_d3);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_d5){
var _d6=this.domNode;
if(_b1.isObject(_d5)){
_af.set(_d6,_d5);
}else{
if(_d6.style.cssText){
_d6.style.cssText+="; "+_d5;
}else{
_d6.style.cssText=_d5;
}
}
this._set("style",_d5);
},_attrToDom:function(_d7,_d8,_d9){
_d9=arguments.length>=3?_d9:this.attributeMap[_d7];
_a6.forEach(_b1.isArray(_d9)?_d9:[_d9],function(_da){
var _db=this[_da.node||_da||"domNode"];
var _dc=_da.type||"attribute";
switch(_dc){
case "attribute":
if(_b1.isFunction(_d8)){
_d8=_b1.hitch(this,_d8);
}
var _dd=_da.attribute?_da.attribute:(/^on[A-Z][a-zA-Z]*$/.test(_d7)?_d7.toLowerCase():_d7);
if(_db.tagName){
_ab.set(_db,_dd,_d8);
}else{
_db.set(_dd,_d8);
}
break;
case "innerText":
_db.innerHTML="";
_db.appendChild(this.ownerDocument.createTextNode(_d8));
break;
case "innerHTML":
_db.innerHTML=_d8;
break;
case "class":
_ac.replace(_db,_d8,this[_d7]);
break;
}
},this);
},get:function(_de){
var _df=this._getAttrNames(_de);
return this[_df.g]?this[_df.g]():this._get(_de);
},set:function(_e0,_e1){
if(typeof _e0==="object"){
for(var x in _e0){
this.set(x,_e0[x]);
}
return this;
}
var _e2=this._getAttrNames(_e0),_e3=this[_e2.s];
if(_b1.isFunction(_e3)){
var _e4=_e3.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _e5=this.focusNode&&!_b1.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_e5]&&this[_e5].tagName,_e6=tag&&(_ba[tag]||(_ba[tag]=_bb(this[_e5]))),map=_e0 in this.attributeMap?this.attributeMap[_e0]:_e2.s in this?this[_e2.s]:((_e6&&_e2.l in _e6&&typeof _e1!="function")||/^aria-|^data-|^role$/.test(_e0))?_e5:null;
if(map!=null){
this._attrToDom(_e0,_e1,map);
}
this._set(_e0,_e1);
}
return _e4||this;
},_attrPairNames:{},_getAttrNames:function(_e7){
var apn=this._attrPairNames;
if(apn[_e7]){
return apn[_e7];
}
var uc=_e7.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[_e7]={n:_e7+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(_e8,_e9){
var _ea=this[_e8];
this[_e8]=_e9;
if(this._created&&!_bf(_ea,_e9)){
if(this._watchCallbacks){
this._watchCallbacks(_e8,_ea,_e9);
}
this.emit("attrmodified-"+_e8,{detail:{prevValue:_ea,newValue:_e9}});
}
},_get:function(_eb){
return this[_eb];
},emit:function(_ec,_ed,_ee){
_ed=_ed||{};
if(_ed.bubbles===undefined){
_ed.bubbles=true;
}
if(_ed.cancelable===undefined){
_ed.cancelable=true;
}
if(!_ed.detail){
_ed.detail={};
}
_ed.detail.widget=this;
var ret,_ef=this["on"+_ec];
if(_ef){
ret=_ef.apply(this,_ee?_ee:[_ed]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,_ec.toLowerCase(),_ed);
}
return ret;
},on:function(_f0,_f1){
var _f2=this._onMap(_f0);
if(_f2){
return _a7.after(this,_f2,_f1,true);
}
return this.own(on(this.domNode,_f0,_f1))[0];
},_onMap:function(_f3){
var _f4=this.constructor,map=_f4._onMap;
if(!map){
map=(_f4._onMap={});
for(var _f5 in _f4.prototype){
if(/^on/.test(_f5)){
map[_f5.replace(/^on/,"").toLowerCase()]=_f5;
}
}
}
return map[typeof _f3=="string"&&_f3.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_b7.findWidgets(this.containerNode):[];
},getParent:function(){
return _b7.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_f6,_f7){
return this.own(_a9.connect(obj,_f6,this,_f7))[0];
},disconnect:function(_f8){
_f8.remove();
},subscribe:function(t,_f9){
return this.own(_b4.subscribe(t,_b1.hitch(this,_f9)))[0];
},unsubscribe:function(_fa){
_fa.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_ae.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_af.get(this.domNode,"display")!="none");
},placeAt:function(_fb,_fc){
var _fd=!_fb.tagName&&_b7.byId(_fb);
if(_fd&&_fd.addChild&&(!_fc||typeof _fc==="number")){
_fd.addChild(this,_fc);
}else{
var ref=_fd&&("domNode" in _fd)?(_fd.containerNode&&!/after|before|replace/.test(_fc||"")?_fd.containerNode:_fd.domNode):dom.byId(_fb,this.ownerDocument);
_ad.place(this.domNode,ref,_fc);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,_fe){
var _ff=setTimeout(_b1.hitch(this,function(){
if(!_ff){
return;
}
_ff=null;
if(!this._destroyed){
_b1.hitch(this,fcn)();
}
}),_fe||0);
return {remove:function(){
if(_ff){
clearTimeout(_ff);
_ff=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_c0.extend(_b6);
}
return _c0;
});
},"dijit/_base":function(){
define(["./main","./a11y","./WidgetSet","./_base/focus","./_base/manager","./_base/place","./_base/popup","./_base/scroll","./_base/sniff","./_base/typematic","./_base/wai","./_base/window"],function(_100){
return _100._base;
});
},"dojo/touch":function(){
define(["./_base/kernel","./aspect","./dom","./dom-class","./_base/lang","./on","./has","./mouse","./domReady","./_base/window"],function(dojo,_101,dom,_102,lang,on,has,_103,_104,win){
var ios4=has("ios")<5;
var _105=has("pointer-events")||has("MSPointer"),_106=(function(){
var _107={};
for(var type in {down:1,move:1,up:1,cancel:1,over:1,out:1}){
_107[type]=has("MSPointer")?"MSPointer"+type.charAt(0).toUpperCase()+type.slice(1):"pointer"+type;
}
return _107;
})();
var _108=has("touch-events");
var _109,_10a,_10b=false,_10c,_10d,_10e,_10f,_110,_111;
var _112;
function _113(_114,_115,_116){
if(_105&&_116){
return function(node,_117){
return on(node,_116,_117);
};
}else{
if(_108){
return function(node,_118){
var _119=on(node,_115,function(evt){
_118.call(this,evt);
_112=(new Date()).getTime();
}),_11a=on(node,_114,function(evt){
if(!_112||(new Date()).getTime()>_112+1000){
_118.call(this,evt);
}
});
return {remove:function(){
_119.remove();
_11a.remove();
}};
};
}else{
return function(node,_11b){
return on(node,_114,_11b);
};
}
}
};
function _11c(node){
do{
if(node.dojoClick!==undefined){
return node;
}
}while(node=node.parentNode);
};
function _11d(e,_11e,_11f){
if(_103.isRight(e)){
return;
}
var _120=_11c(e.target);
_10a=!e.target.disabled&&_120&&_120.dojoClick;
if(_10a){
_10b=(_10a=="useTarget");
_10c=(_10b?_120:e.target);
if(_10b){
e.preventDefault();
}
_10d=e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX;
_10e=e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY;
_10f=(typeof _10a=="object"?_10a.x:(typeof _10a=="number"?_10a:0))||4;
_110=(typeof _10a=="object"?_10a.y:(typeof _10a=="number"?_10a:0))||4;
if(!_109){
_109=true;
function _121(e){
if(_10b){
_10a=dom.isDescendant(win.doc.elementFromPoint((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX),(e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)),_10c);
}else{
_10a=_10a&&(e.changedTouches?e.changedTouches[0].target:e.target)==_10c&&Math.abs((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX)-_10d)<=_10f&&Math.abs((e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)-_10e)<=_110;
}
};
win.doc.addEventListener(_11e,function(e){
if(_103.isRight(e)){
return;
}
_121(e);
if(_10b){
e.preventDefault();
}
},true);
win.doc.addEventListener(_11f,function(e){
if(_103.isRight(e)){
return;
}
_121(e);
if(_10a){
_111=(new Date()).getTime();
var _122=(_10b?_10c:e.target);
if(_122.tagName==="LABEL"){
_122=dom.byId(_122.getAttribute("for"))||_122;
}
var src=(e.changedTouches)?e.changedTouches[0]:e;
function _123(type){
var evt=document.createEvent("MouseEvents");
evt._dojo_click=true;
evt.initMouseEvent(type,true,true,e.view,e.detail,src.screenX,src.screenY,src.clientX,src.clientY,e.ctrlKey,e.altKey,e.shiftKey,e.metaKey,0,null);
return evt;
};
var _124=_123("mousedown");
var _125=_123("mouseup");
var _126=_123("click");
setTimeout(function(){
on.emit(_122,"mousedown",_124);
on.emit(_122,"mouseup",_125);
on.emit(_122,"click",_126);
_111=(new Date()).getTime();
},0);
}
},true);
function _127(type){
win.doc.addEventListener(type,function(e){
if(_10a&&!e._dojo_click&&(new Date()).getTime()<=_111+1000&&!(e.target.tagName=="INPUT"&&_102.contains(e.target,"dijitOffScreen"))){
e.stopPropagation();
e.stopImmediatePropagation&&e.stopImmediatePropagation();
if(type=="click"&&(e.target.tagName!="INPUT"||e.target.type=="radio"||e.target.type=="checkbox")&&e.target.tagName!="TEXTAREA"&&e.target.tagName!="AUDIO"&&e.target.tagName!="VIDEO"){
e.preventDefault();
}
}
},true);
};
_127("click");
_127("mousedown");
_127("mouseup");
}
}
};
var _128;
if(has("touch")){
if(_105){
_104(function(){
win.doc.addEventListener(_106.down,function(evt){
_11d(evt,_106.move,_106.up);
},true);
});
}else{
_104(function(){
_128=win.body();
win.doc.addEventListener("touchstart",function(evt){
_112=(new Date()).getTime();
var _129=_128;
_128=evt.target;
on.emit(_129,"dojotouchout",{relatedTarget:_128,bubbles:true});
on.emit(_128,"dojotouchover",{relatedTarget:_129,bubbles:true});
_11d(evt,"touchmove","touchend");
},true);
function _12a(evt){
var _12b=lang.delegate(evt,{bubbles:true});
if(has("ios")>=6){
_12b.touches=evt.touches;
_12b.altKey=evt.altKey;
_12b.changedTouches=evt.changedTouches;
_12b.ctrlKey=evt.ctrlKey;
_12b.metaKey=evt.metaKey;
_12b.shiftKey=evt.shiftKey;
_12b.targetTouches=evt.targetTouches;
}
return _12b;
};
on(win.doc,"touchmove",function(evt){
_112=(new Date()).getTime();
var _12c=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset));
if(_12c){
if(_128!==_12c){
on.emit(_128,"dojotouchout",{relatedTarget:_12c,bubbles:true});
on.emit(_12c,"dojotouchover",{relatedTarget:_128,bubbles:true});
_128=_12c;
}
if(!on.emit(_12c,"dojotouchmove",_12a(evt))){
evt.preventDefault();
}
}
});
on(win.doc,"touchend",function(evt){
_112=(new Date()).getTime();
var node=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset))||win.body();
on.emit(node,"dojotouchend",_12a(evt));
});
});
}
}
var _12d={press:_113("mousedown","touchstart",_106.down),move:_113("mousemove","dojotouchmove",_106.move),release:_113("mouseup","dojotouchend",_106.up),cancel:_113(_103.leave,"touchcancel",_105?_106.cancel:null),over:_113("mouseover","dojotouchover",_106.over),out:_113("mouseout","dojotouchout",_106.out),enter:_103._eventHandler(_113("mouseover","dojotouchover",_106.over)),leave:_103._eventHandler(_113("mouseout","dojotouchout",_106.out))};
1&&(dojo.touch=_12d);
return _12d;
});
},"dijit/form/_FormValueMixin":function(){
define(["dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","./_FormWidgetMixin"],function(_12e,_12f,keys,lang,on,_130){
return _12e("dijit.form._FormValueMixin",_130,{readOnly:false,_setReadOnlyAttr:function(_131){
_12f.set(this.focusNode,"readOnly",_131);
this._set("readOnly",_131);
},postCreate:function(){
this.inherited(arguments);
if(this._resetValue===undefined){
this._lastValueReported=this._resetValue=this.value;
}
},_setValueAttr:function(_132,_133){
this._handleOnChange(_132,_133);
},_handleOnChange:function(_134,_135){
this._set("value",_134);
this.inherited(arguments);
},undo:function(){
this._setValueAttr(this._lastValueReported,false);
},reset:function(){
this._hasBeenBlurred=false;
this._setValueAttr(this._resetValue,true);
}});
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_136,lang,_137,when){
return _136("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_138){
if(_138){
this.set(_138);
}
},_get:function(name,_139){
return typeof this[_139.g]==="function"?this[_139.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,_13a){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var _13b=this._getAttrNames(name),_13c=this._get(name,_13b),_13d=this[_13b.s],_13e;
if(typeof _13d==="function"){
_13e=_13d.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=_13a;
}
if(this._watchCallbacks){
var self=this;
when(_13e,function(){
self._watchCallbacks(name,_13c,_13a);
});
}
return this;
},_changeAttrValue:function(name,_13f){
var _140=this.get(name);
this[name]=_13f;
if(this._watchCallbacks){
this._watchCallbacks(name,_140,_13f);
}
return this;
},watch:function(name,_141){
var _142=this._watchCallbacks;
if(!_142){
var self=this;
_142=this._watchCallbacks=function(name,_143,_144,_145){
var _146=function(_147){
if(_147){
_147=_147.slice();
for(var i=0,l=_147.length;i<l;i++){
_147[i].call(self,name,_143,_144);
}
}
};
_146(_142["_"+name]);
if(!_145){
_146(_142["*"]);
}
};
}
if(!_141&&typeof name==="function"){
_141=name;
name="*";
}else{
name="_"+name;
}
var _148=_142[name];
if(typeof _148!=="object"){
_148=_142[name]=[];
}
_148.push(_141);
var _149={};
_149.unwatch=_149.remove=function(){
var _14a=_137.indexOf(_148,_141);
if(_14a>-1){
_148.splice(_14a,1);
}
};
return _149;
}});
});
},"dijit/_CssStateMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/has","dojo/_base/lang","dojo/on","dojo/domReady","dojo/touch","dojo/_base/window","./a11yclick","./registry"],function(_14b,_14c,dom,_14d,has,lang,on,_14e,_14f,win,_150,_151){
var _152=_14c("dijit._CssStateMixin",[],{hovering:false,active:false,_applyAttributes:function(){
this.inherited(arguments);
_14b.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","_opened"],function(attr){
this.watch(attr,lang.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes||{}){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.domNode,this.baseClass);
this._setStateClass();
},_cssMouseEvent:function(_153){
if(!this.disabled){
switch(_153.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
this._set("hovering",true);
this._set("active",this._mouseDown);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
this._set("hovering",false);
this._set("active",false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
this._set("active",true);
break;
case "mouseup":
case "dojotouchend":
case "MSPointerUp":
case "pointerup":
case "keyup":
this._set("active",false);
break;
}
}
},_setStateClass:function(){
var _154=this.baseClass.split(" ");
function _155(_156){
_154=_154.concat(_14b.map(_154,function(c){
return c+_156;
}),"dijit"+_156);
};
if(!this.isLeftToRight()){
_155("Rtl");
}
var _157=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_155(_157);
}
if(this.state){
_155(this.state);
}
if(this.selected){
_155("Selected");
}
if(this._opened){
_155("Opened");
}
if(this.disabled){
_155("Disabled");
}else{
if(this.readOnly){
_155("ReadOnly");
}else{
if(this.active){
_155("Active");
}else{
if(this.hovering){
_155("Hover");
}
}
}
}
if(this.focused){
_155("Focused");
}
var tn=this.stateNode||this.domNode,_158={};
_14b.forEach(tn.className.split(" "),function(c){
_158[c]=true;
});
if("_stateClasses" in this){
_14b.forEach(this._stateClasses,function(c){
delete _158[c];
});
}
_14b.forEach(_154,function(c){
_158[c]=true;
});
var _159=[];
for(var c in _158){
_159.push(c);
}
var cls=_159.join(" ");
if(cls!=tn.className){
tn.className=cls;
}
this._stateClasses=_154;
},_subnodeCssMouseEvent:function(node,_15a,evt){
if(this.disabled||this.readOnly){
return;
}
function _15b(_15c){
_14d.toggle(node,_15a+"Hover",_15c);
};
function _15d(_15e){
_14d.toggle(node,_15a+"Active",_15e);
};
function _15f(_160){
_14d.toggle(node,_15a+"Focused",_160);
};
switch(evt.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
_15b(true);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
_15b(false);
_15d(false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
_15d(true);
break;
case "mouseup":
case "MSPointerUp":
case "pointerup":
case "dojotouchend":
case "keyup":
_15d(false);
break;
case "focus":
case "focusin":
_15f(true);
break;
case "blur":
case "focusout":
_15f(false);
break;
}
},_trackMouseState:function(node,_161){
node._cssState=_161;
}});
_14e(function(){
function _162(evt,_163,_164){
if(_164&&dom.isDescendant(_164,_163)){
return;
}
for(var node=_163;node&&node!=_164;node=node.parentNode){
if(node._cssState){
var _165=_151.getEnclosingWidget(node);
if(_165){
if(node==_165.domNode){
_165._cssMouseEvent(evt);
}else{
_165._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
}
}
};
var body=win.body(),_166;
on(body,_14f.over,function(evt){
_162(evt,evt.target,evt.relatedTarget);
});
on(body,_14f.out,function(evt){
_162(evt,evt.target,evt.relatedTarget);
});
on(body,_150.press,function(evt){
_166=evt.target;
_162(evt,_166);
});
on(body,_150.release,function(evt){
_162(evt,_166);
_166=null;
});
on(body,"focusin, focusout",function(evt){
var node=evt.target;
if(node._cssState&&!node.getAttribute("widgetId")){
var _167=_151.getEnclosingWidget(node);
if(_167){
_167._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
});
});
return _152;
});
},"dijit/_base/manager":function(){
define(["dojo/_base/array","dojo/_base/config","dojo/_base/lang","../registry","../main"],function(_168,_169,lang,_16a,_16b){
var _16c={};
_168.forEach(["byId","getUniqueId","findWidgets","_destroyAll","byNode","getEnclosingWidget"],function(name){
_16c[name]=_16a[name];
});
lang.mixin(_16c,{defaultDuration:_169["defaultDuration"]||200});
lang.mixin(_16b,_16c);
return _16b;
});
},"dojo/fx/Toggler":function(){
define(["../_base/lang","../_base/declare","../_base/fx","../aspect"],function(lang,_16d,_16e,_16f){
return _16d("dojo.fx.Toggler",null,{node:null,showFunc:_16e.fadeIn,hideFunc:_16e.fadeOut,showDuration:200,hideDuration:200,constructor:function(args){
var _170=this;
lang.mixin(_170,args);
_170.node=args.node;
_170._showArgs=lang.mixin({},args);
_170._showArgs.node=_170.node;
_170._showArgs.duration=_170.showDuration;
_170.showAnim=_170.showFunc(_170._showArgs);
_170._hideArgs=lang.mixin({},args);
_170._hideArgs.node=_170.node;
_170._hideArgs.duration=_170.hideDuration;
_170.hideAnim=_170.hideFunc(_170._hideArgs);
_16f.after(_170.showAnim,"beforeBegin",lang.hitch(_170.hideAnim,"stop",true),true);
_16f.after(_170.hideAnim,"beforeBegin",lang.hitch(_170.showAnim,"stop",true),true);
},show:function(_171){
return this.showAnim.play(_171||0);
},hide:function(_172){
return this.hideAnim.play(_172||0);
}});
});
},"dijit/_base/sniff":function(){
define(["dojo/uacss"],function(){
});
},"dijit/BackgroundIframe":function(){
define(["require","./main","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/sniff"],function(_173,_174,_175,_176,_177,lang,on,has){
has.add("config-bgIframe",(has("ie")&&!/IEMobile\/10\.0/.test(navigator.userAgent))||(has("trident")&&/Windows NT 6.[01]/.test(navigator.userAgent)));
var _178=new function(){
var _179=[];
this.pop=function(){
var _17a;
if(_179.length){
_17a=_179.pop();
_17a.style.display="";
}else{
if(has("ie")<9){
var burl=_175["dojoBlankHtmlUrl"]||_173.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_17a=document.createElement(html);
}else{
_17a=_176.create("iframe");
_17a.src="javascript:\"\"";
_17a.className="dijitBackgroundIframe";
_17a.setAttribute("role","presentation");
_177.set(_17a,"opacity",0.1);
}
_17a.tabIndex=-1;
}
return _17a;
};
this.push=function(_17b){
_17b.style.display="none";
_179.push(_17b);
};
}();
_174.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("config-bgIframe")){
var _17c=(this.iframe=_178.pop());
node.appendChild(_17c);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,"resize",node));
}else{
_177.set(_17c,{width:"100%",height:"100%"});
}
}
};
lang.extend(_174.BackgroundIframe,{resize:function(node){
if(this.iframe){
_177.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
this.iframe.parentNode.removeChild(this.iframe);
_178.push(this.iframe);
delete this.iframe;
}
}});
return _174.BackgroundIframe;
});
},"dijit/typematic":function(){
define(["dojo/_base/array","dojo/_base/connect","dojo/_base/lang","dojo/on","dojo/sniff","./main"],function(_17d,_17e,lang,on,has,_17f){
var _180=(_17f.typematic={_fireEventAndReload:function(){
this._timer=null;
this._callback(++this._count,this._node,this._evt);
this._currentTimeout=Math.max(this._currentTimeout<0?this._initialDelay:(this._subsequentDelay>1?this._subsequentDelay:Math.round(this._currentTimeout*this._subsequentDelay)),this._minDelay);
this._timer=setTimeout(lang.hitch(this,"_fireEventAndReload"),this._currentTimeout);
},trigger:function(evt,_181,node,_182,obj,_183,_184,_185){
if(obj!=this._obj){
this.stop();
this._initialDelay=_184||500;
this._subsequentDelay=_183||0.9;
this._minDelay=_185||10;
this._obj=obj;
this._node=node;
this._currentTimeout=-1;
this._count=-1;
this._callback=lang.hitch(_181,_182);
this._evt={faux:true};
for(var attr in evt){
if(attr!="layerX"&&attr!="layerY"){
var v=evt[attr];
if(typeof v!="function"&&typeof v!="undefined"){
this._evt[attr]=v;
}
}
}
this._fireEventAndReload();
}
},stop:function(){
if(this._timer){
clearTimeout(this._timer);
this._timer=null;
}
if(this._obj){
this._callback(-1,this._node,this._evt);
this._obj=null;
}
},addKeyListener:function(node,_186,_187,_188,_189,_18a,_18b){
var type="keyCode" in _186?"keydown":"charCode" in _186?"keypress":_17e._keypress,attr="keyCode" in _186?"keyCode":"charCode" in _186?"charCode":"charOrCode";
var _18c=[on(node,type,lang.hitch(this,function(evt){
if(evt[attr]==_186[attr]&&(_186.ctrlKey===undefined||_186.ctrlKey==evt.ctrlKey)&&(_186.altKey===undefined||_186.altKey==evt.altKey)&&(_186.metaKey===undefined||_186.metaKey==(evt.metaKey||false))&&(_186.shiftKey===undefined||_186.shiftKey==evt.shiftKey)){
evt.stopPropagation();
evt.preventDefault();
_180.trigger(evt,_187,node,_188,_186,_189,_18a,_18b);
}else{
if(_180._obj==_186){
_180.stop();
}
}
})),on(node,"keyup",lang.hitch(this,function(){
if(_180._obj==_186){
_180.stop();
}
}))];
return {remove:function(){
_17d.forEach(_18c,function(h){
h.remove();
});
}};
},addMouseListener:function(node,_18d,_18e,_18f,_190,_191){
var _192=[on(node,"mousedown",lang.hitch(this,function(evt){
evt.preventDefault();
_180.trigger(evt,_18d,node,_18e,node,_18f,_190,_191);
})),on(node,"mouseup",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_180.stop();
})),on(node,"mouseout",lang.hitch(this,function(evt){
if(this._obj){
evt.preventDefault();
}
_180.stop();
})),on(node,"dblclick",lang.hitch(this,function(evt){
evt.preventDefault();
if(has("ie")<9){
_180.trigger(evt,_18d,node,_18e,node,_18f,_190,_191);
setTimeout(lang.hitch(this,_180.stop),50);
}
}))];
return {remove:function(){
_17d.forEach(_192,function(h){
h.remove();
});
}};
},addListener:function(_193,_194,_195,_196,_197,_198,_199,_19a){
var _19b=[this.addKeyListener(_194,_195,_196,_197,_198,_199,_19a),this.addMouseListener(_193,_196,_197,_198,_199,_19a)];
return {remove:function(){
_17d.forEach(_19b,function(h){
h.remove();
});
}};
}});
return _180;
});
},"dojo/_base/url":function(){
define(["./kernel"],function(dojo){
var ore=new RegExp("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$"),ire=new RegExp("^((([^\\[:]+):)?([^@]+)@)?(\\[([^\\]]+)\\]|([^\\[:]*))(:([0-9]+))?$"),_19c=function(){
var n=null,_19d=arguments,uri=[_19d[0]];
for(var i=1;i<_19d.length;i++){
if(!_19d[i]){
continue;
}
var _19e=new _19c(_19d[i]+""),_19f=new _19c(uri[0]+"");
if(_19e.path==""&&!_19e.scheme&&!_19e.authority&&!_19e.query){
if(_19e.fragment!=n){
_19f.fragment=_19e.fragment;
}
_19e=_19f;
}else{
if(!_19e.scheme){
_19e.scheme=_19f.scheme;
if(!_19e.authority){
_19e.authority=_19f.authority;
if(_19e.path.charAt(0)!="/"){
var path=_19f.path.substring(0,_19f.path.lastIndexOf("/")+1)+_19e.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==(segs.length-1)){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_19e.path=segs.join("/");
}
}
}
}
uri=[];
if(_19e.scheme){
uri.push(_19e.scheme,":");
}
if(_19e.authority){
uri.push("//",_19e.authority);
}
uri.push(_19e.path);
if(_19e.query){
uri.push("?",_19e.query);
}
if(_19e.fragment){
uri.push("#",_19e.fragment);
}
}
this.uri=uri.join("");
var r=this.uri.match(ore);
this.scheme=r[2]||(r[1]?"":n);
this.authority=r[4]||(r[3]?"":n);
this.path=r[5];
this.query=r[7]||(r[6]?"":n);
this.fragment=r[9]||(r[8]?"":n);
if(this.authority!=n){
r=this.authority.match(ire);
this.user=r[3]||n;
this.password=r[4]||n;
this.host=r[6]||r[7];
this.port=r[9]||n;
}
};
_19c.prototype.toString=function(){
return this.uri;
};
return dojo._Url=_19c;
});
},"dojo/date/stamp":function(){
define(["../_base/lang","../_base/array"],function(lang,_1a0){
var _1a1={};
lang.setObject("dojo.date.stamp",_1a1);
_1a1.fromISOString=function(_1a2,_1a3){
if(!_1a1._isoRegExp){
_1a1._isoRegExp=/^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
}
var _1a4=_1a1._isoRegExp.exec(_1a2),_1a5=null;
if(_1a4){
_1a4.shift();
if(_1a4[1]){
_1a4[1]--;
}
if(_1a4[6]){
_1a4[6]*=1000;
}
if(_1a3){
_1a3=new Date(_1a3);
_1a0.forEach(_1a0.map(["FullYear","Month","Date","Hours","Minutes","Seconds","Milliseconds"],function(prop){
return _1a3["get"+prop]();
}),function(_1a6,_1a7){
_1a4[_1a7]=_1a4[_1a7]||_1a6;
});
}
_1a5=new Date(_1a4[0]||1970,_1a4[1]||0,_1a4[2]||1,_1a4[3]||0,_1a4[4]||0,_1a4[5]||0,_1a4[6]||0);
if(_1a4[0]<100){
_1a5.setFullYear(_1a4[0]||1970);
}
var _1a8=0,_1a9=_1a4[7]&&_1a4[7].charAt(0);
if(_1a9!="Z"){
_1a8=((_1a4[8]||0)*60)+(Number(_1a4[9])||0);
if(_1a9!="-"){
_1a8*=-1;
}
}
if(_1a9){
_1a8-=_1a5.getTimezoneOffset();
}
if(_1a8){
_1a5.setTime(_1a5.getTime()+_1a8*60000);
}
}
return _1a5;
};
_1a1.toISOString=function(_1aa,_1ab){
var _1ac=function(n){
return (n<10)?"0"+n:n;
};
_1ab=_1ab||{};
var _1ad=[],_1ae=_1ab.zulu?"getUTC":"get",date="";
if(_1ab.selector!="time"){
var year=_1aa[_1ae+"FullYear"]();
date=["0000".substr((year+"").length)+year,_1ac(_1aa[_1ae+"Month"]()+1),_1ac(_1aa[_1ae+"Date"]())].join("-");
}
_1ad.push(date);
if(_1ab.selector!="date"){
var time=[_1ac(_1aa[_1ae+"Hours"]()),_1ac(_1aa[_1ae+"Minutes"]()),_1ac(_1aa[_1ae+"Seconds"]())].join(":");
var _1af=_1aa[_1ae+"Milliseconds"]();
if(_1ab.milliseconds){
time+="."+(_1af<100?"0":"")+_1ac(_1af);
}
if(_1ab.zulu){
time+="Z";
}else{
if(_1ab.selector!="time"){
var _1b0=_1aa.getTimezoneOffset();
var _1b1=Math.abs(_1b0);
time+=(_1b0>0?"-":"+")+_1ac(Math.floor(_1b1/60))+":"+_1ac(_1b1%60);
}
}
_1ad.push(time);
}
return _1ad.join("T");
};
return _1a1;
});
},"dojo/text":function(){
define(["./_base/kernel","require","./has","./request"],function(dojo,_1b2,has,_1b3){
var _1b4;
if(1){
_1b4=function(url,sync,load){
_1b3(url,{sync:!!sync,headers:{"X-Requested-With":null}}).then(load);
};
}else{
if(_1b2.getText){
_1b4=_1b2.getText;
}else{
console.error("dojo/text plugin failed to load because loader does not support getText");
}
}
var _1b5={},_1b6=function(text){
if(text){
text=text.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _1b7=text.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_1b7){
text=_1b7[1];
}
}else{
text="";
}
return text;
},_1b8={},_1b9={};
dojo.cache=function(_1ba,url,_1bb){
var key;
if(typeof _1ba=="string"){
if(/\//.test(_1ba)){
key=_1ba;
_1bb=url;
}else{
key=_1b2.toUrl(_1ba.replace(/\./g,"/")+(url?("/"+url):""));
}
}else{
key=_1ba+"";
_1bb=url;
}
var val=(_1bb!=undefined&&typeof _1bb!="string")?_1bb.value:_1bb,_1bc=_1bb&&_1bb.sanitize;
if(typeof val=="string"){
_1b5[key]=val;
return _1bc?_1b6(val):val;
}else{
if(val===null){
delete _1b5[key];
return null;
}else{
if(!(key in _1b5)){
_1b4(key,true,function(text){
_1b5[key]=text;
});
}
return _1bc?_1b6(_1b5[key]):_1b5[key];
}
}
};
return {dynamic:true,normalize:function(id,_1bd){
var _1be=id.split("!"),url=_1be[0];
return (/^\./.test(url)?_1bd(url):url)+(_1be[1]?"!"+_1be[1]:"");
},load:function(id,_1bf,load){
var _1c0=id.split("!"),_1c1=_1c0.length>1,_1c2=_1c0[0],url=_1bf.toUrl(_1c0[0]),_1c3="url:"+url,text=_1b8,_1c4=function(text){
load(_1c1?_1b6(text):text);
};
if(_1c2 in _1b5){
text=_1b5[_1c2];
}else{
if(_1bf.cache&&_1c3 in _1bf.cache){
text=_1bf.cache[_1c3];
}else{
if(url in _1b5){
text=_1b5[url];
}
}
}
if(text===_1b8){
if(_1b9[url]){
_1b9[url].push(_1c4);
}else{
var _1c5=_1b9[url]=[_1c4];
_1b4(url,!_1bf.async,function(text){
_1b5[_1c2]=_1b5[url]=text;
for(var i=0;i<_1c5.length;){
_1c5[i++](text);
}
delete _1b9[url];
});
}
}else{
_1c4(text);
}
}};
});
},"dijit/_base/place":function(){
define(["dojo/_base/array","dojo/_base/lang","dojo/window","../place","../main"],function(_1c6,lang,_1c7,_1c8,_1c9){
var _1ca={};
_1ca.getViewport=function(){
return _1c7.getBox();
};
_1ca.placeOnScreen=_1c8.at;
_1ca.placeOnScreenAroundElement=function(node,_1cb,_1cc,_1cd){
var _1ce;
if(lang.isArray(_1cc)){
_1ce=_1cc;
}else{
_1ce=[];
for(var key in _1cc){
_1ce.push({aroundCorner:key,corner:_1cc[key]});
}
}
return _1c8.around(node,_1cb,_1ce,true,_1cd);
};
_1ca.placeOnScreenAroundNode=_1ca.placeOnScreenAroundElement;
_1ca.placeOnScreenAroundRectangle=_1ca.placeOnScreenAroundElement;
_1ca.getPopupAroundAlignment=function(_1cf,_1d0){
var _1d1={};
_1c6.forEach(_1cf,function(pos){
var ltr=_1d0;
switch(pos){
case "after":
_1d1[_1d0?"BR":"BL"]=_1d0?"BL":"BR";
break;
case "before":
_1d1[_1d0?"BL":"BR"]=_1d0?"BR":"BL";
break;
case "below-alt":
ltr=!ltr;
case "below":
_1d1[ltr?"BL":"BR"]=ltr?"TL":"TR";
_1d1[ltr?"BR":"BL"]=ltr?"TR":"TL";
break;
case "above-alt":
ltr=!ltr;
case "above":
default:
_1d1[ltr?"TL":"TR"]=ltr?"BL":"BR";
_1d1[ltr?"TR":"TL"]=ltr?"BR":"BL";
break;
}
});
return _1d1;
};
lang.mixin(_1c9,_1ca);
return _1c9;
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_1d2,win,_1d3){
var _1d4={},hash={};
var _1d5={length:0,add:function(_1d6){
if(hash[_1d6.id]){
throw new Error("Tried to register widget with id=="+_1d6.id+" but that id is already registered");
}
hash[_1d6.id]=_1d6;
this.length++;
},remove:function(id){
if(hash[id]){
delete hash[id];
this.length--;
}
},byId:function(id){
return typeof id=="string"?hash[id]:id;
},byNode:function(node){
return hash[node.getAttribute("widgetId")];
},toArray:function(){
var ar=[];
for(var id in hash){
ar.push(hash[id]);
}
return ar;
},getUniqueId:function(_1d7){
var id;
do{
id=_1d7+"_"+(_1d7 in _1d4?++_1d4[_1d7]:_1d4[_1d7]=0);
}while(hash[id]);
return _1d3._scopeName=="dijit"?id:_1d3._scopeName+"_"+id;
},findWidgets:function(root,_1d8){
var _1d9=[];
function _1da(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _1db=node.getAttribute("widgetId");
if(_1db){
var _1dc=hash[_1db];
if(_1dc){
_1d9.push(_1dc);
}
}else{
if(node!==_1d8){
_1da(node);
}
}
}
}
};
_1da(root);
return _1d9;
},_destroyAll:function(){
_1d3._curFocus=null;
_1d3._prevFocus=null;
_1d3._activeStack=[];
_1d2.forEach(_1d5.findWidgets(win.body()),function(_1dd){
if(!_1dd._destroyed){
if(_1dd.destroyRecursive){
_1dd.destroyRecursive();
}else{
if(_1dd.destroy){
_1dd.destroy();
}
}
}
});
},getEnclosingWidget:function(node){
while(node){
var id=node.nodeType==1&&node.getAttribute("widgetId");
if(id){
return hash[id];
}
node=node.parentNode;
}
return null;
},_hash:hash};
_1d3.registry=_1d5;
return _1d5;
});
},"dijit/form/_FormWidgetMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/_base/lang","dojo/mouse","dojo/on","dojo/sniff","dojo/window","../a11y"],function(_1de,_1df,_1e0,_1e1,lang,_1e2,on,has,_1e3,a11y){
return _1df("dijit.form._FormWidgetMixin",null,{name:"",alt:"",value:"",type:"text","aria-label":"focusNode",tabIndex:"0",_setTabIndexAttr:"focusNode",disabled:false,intermediateChanges:false,scrollOnFocus:true,_setIdAttr:"focusNode",_setDisabledAttr:function(_1e4){
this._set("disabled",_1e4);
if(/^(button|input|select|textarea|optgroup|option|fieldset)$/i.test(this.focusNode.tagName)){
_1e0.set(this.focusNode,"disabled",_1e4);
}else{
this.focusNode.setAttribute("aria-disabled",_1e4?"true":"false");
}
if(this.valueNode){
_1e0.set(this.valueNode,"disabled",_1e4);
}
if(_1e4){
this._set("hovering",false);
this._set("active",false);
var _1e5="tabIndex" in this.attributeMap?this.attributeMap.tabIndex:("_setTabIndexAttr" in this)?this._setTabIndexAttr:"focusNode";
_1de.forEach(lang.isArray(_1e5)?_1e5:[_1e5],function(_1e6){
var node=this[_1e6];
if(has("webkit")||a11y.hasDefaultTabStop(node)){
node.setAttribute("tabIndex","-1");
}else{
node.removeAttribute("tabIndex");
}
},this);
}else{
if(this.tabIndex!=""){
this.set("tabIndex",this.tabIndex);
}
}
},_onFocus:function(by){
if(by=="mouse"&&this.isFocusable()){
var _1e7=this.own(on(this.focusNode,"focus",function(){
_1e8.remove();
_1e7.remove();
}))[0];
var _1e9=has("pointer-events")?"pointerup":has("MSPointer")?"MSPointerUp":has("touch-events")?"touchend, mouseup":"mouseup";
var _1e8=this.own(on(this.ownerDocumentBody,_1e9,lang.hitch(this,function(evt){
_1e8.remove();
_1e7.remove();
if(this.focused){
if(evt.type=="touchend"){
this.defer("focus");
}else{
this.focus();
}
}
})))[0];
}
if(this.scrollOnFocus){
this.defer(function(){
_1e3.scrollIntoView(this.domNode);
});
}
this.inherited(arguments);
},isFocusable:function(){
return !this.disabled&&this.focusNode&&(_1e1.get(this.domNode,"display")!="none");
},focus:function(){
if(!this.disabled&&this.focusNode.focus){
try{
this.focusNode.focus();
}
catch(e){
}
}
},compare:function(val1,val2){
if(typeof val1=="number"&&typeof val2=="number"){
return (isNaN(val1)&&isNaN(val2))?0:val1-val2;
}else{
if(val1>val2){
return 1;
}else{
if(val1<val2){
return -1;
}else{
return 0;
}
}
}
},onChange:function(){
},_onChangeActive:false,_handleOnChange:function(_1ea,_1eb){
if(this._lastValueReported==undefined&&(_1eb===null||!this._onChangeActive)){
this._resetValue=this._lastValueReported=_1ea;
}
this._pendingOnChange=this._pendingOnChange||(typeof _1ea!=typeof this._lastValueReported)||(this.compare(_1ea,this._lastValueReported)!=0);
if((this.intermediateChanges||_1eb||_1eb===undefined)&&this._pendingOnChange){
this._lastValueReported=_1ea;
this._pendingOnChange=false;
if(this._onChangeActive){
if(this._onChangeHandle){
this._onChangeHandle.remove();
}
this._onChangeHandle=this.defer(function(){
this._onChangeHandle=null;
this.onChange(_1ea);
});
}
}
},create:function(){
this.inherited(arguments);
this._onChangeActive=true;
},destroy:function(){
if(this._onChangeHandle){
this._onChangeHandle.remove();
this.onChange(this._lastValueReported);
}
this.inherited(arguments);
}});
});
},"dijit/_AttachMixin":function(){
define(["require","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/mouse","dojo/on","dojo/touch","./_WidgetBase"],function(_1ec,_1ed,_1ee,_1ef,lang,_1f0,on,_1f1,_1f2){
var _1f3=lang.delegate(_1f1,{"mouseenter":_1f0.enter,"mouseleave":_1f0.leave,"keypress":_1ee._keypress});
var _1f4;
var _1f5=_1ef("dijit._AttachMixin",null,{constructor:function(){
this._attachPoints=[];
this._attachEvents=[];
},buildRendering:function(){
this.inherited(arguments);
this._attachTemplateNodes(this.domNode);
this._beforeFillContent();
},_beforeFillContent:function(){
},_attachTemplateNodes:function(_1f6){
var node=_1f6;
while(true){
if(node.nodeType==1&&(this._processTemplateNode(node,function(n,p){
return n.getAttribute(p);
},this._attach)||this.searchContainerNode)&&node.firstChild){
node=node.firstChild;
}else{
if(node==_1f6){
return;
}
while(!node.nextSibling){
node=node.parentNode;
if(node==_1f6){
return;
}
}
node=node.nextSibling;
}
}
},_processTemplateNode:function(_1f7,_1f8,_1f9){
var ret=true;
var _1fa=this.attachScope||this,_1fb=_1f8(_1f7,"dojoAttachPoint")||_1f8(_1f7,"data-dojo-attach-point");
if(_1fb){
var _1fc,_1fd=_1fb.split(/\s*,\s*/);
while((_1fc=_1fd.shift())){
if(lang.isArray(_1fa[_1fc])){
_1fa[_1fc].push(_1f7);
}else{
_1fa[_1fc]=_1f7;
}
ret=(_1fc!="containerNode");
this._attachPoints.push(_1fc);
}
}
var _1fe=_1f8(_1f7,"dojoAttachEvent")||_1f8(_1f7,"data-dojo-attach-event");
if(_1fe){
var _1ff,_200=_1fe.split(/\s*,\s*/);
var trim=lang.trim;
while((_1ff=_200.shift())){
if(_1ff){
var _201=null;
if(_1ff.indexOf(":")!=-1){
var _202=_1ff.split(":");
_1ff=trim(_202[0]);
_201=trim(_202[1]);
}else{
_1ff=trim(_1ff);
}
if(!_201){
_201=_1ff;
}
this._attachEvents.push(_1f9(_1f7,_1ff,lang.hitch(_1fa,_201)));
}
}
}
return ret;
},_attach:function(node,type,func){
type=type.replace(/^on/,"").toLowerCase();
if(type=="dijitclick"){
type=_1f4||(_1f4=_1ec("./a11yclick"));
}else{
type=_1f3[type]||type;
}
return on(node,type,func);
},_detachTemplateNodes:function(){
var _203=this.attachScope||this;
_1ed.forEach(this._attachPoints,function(_204){
delete _203[_204];
});
this._attachPoints=[];
_1ed.forEach(this._attachEvents,function(_205){
_205.remove();
});
this._attachEvents=[];
},destroyRendering:function(){
this._detachTemplateNodes();
this.inherited(arguments);
}});
lang.extend(_1f2,{dojoAttachEvent:"",dojoAttachPoint:""});
return _1f5;
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./domReady","./sniff","./_base/window"],function(_206,lang,_207,has,_208){
var html=_208.doc.documentElement,ie=has("ie"),_209=has("trident"),_20a=has("opera"),maj=Math.floor,ff=has("ff"),_20b=_206.boxModel.replace(/-/,""),_20c={"dj_quirks":has("quirks"),"dj_opera":_20a,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_edge":has("edge"),"dj_gecko":has("mozilla"),"dj_ios":has("ios"),"dj_android":has("android")};
if(ie){
_20c["dj_ie"]=true;
_20c["dj_ie"+maj(ie)]=true;
_20c["dj_iequirks"]=has("quirks");
}
if(_209){
_20c["dj_trident"]=true;
_20c["dj_trident"+maj(_209)]=true;
}
if(ff){
_20c["dj_ff"+maj(ff)]=true;
}
_20c["dj_"+_20b]=true;
var _20d="";
for(var clz in _20c){
if(_20c[clz]){
_20d+=clz+" ";
}
}
html.className=lang.trim(html.className+" "+_20d);
_207(function(){
if(!_206.isBodyLtr()){
var _20e="dj_rtl dijitRtl "+_20d.replace(/ /g,"-rtl ");
html.className=lang.trim(html.className+" "+_20e+"dj_rtl dijitRtl "+_20d.replace(/ /g,"-rtl "));
}
});
return has;
});
},"dojo/gears":function(){
define(["./_base/lang","./sniff"],function(lang,has){
var _20f={};
lang.setObject("dojo.gears",_20f);
_20f._gearsObject=function(){
var _210;
var _211=lang.getObject("google.gears");
if(_211){
return _211;
}
if(typeof GearsFactory!="undefined"){
_210=new GearsFactory();
}else{
if(has("ie")){
try{
_210=new ActiveXObject("Gears.Factory");
}
catch(e){
}
}else{
if(navigator.mimeTypes["application/x-googlegears"]){
_210=document.createElement("object");
_210.setAttribute("type","application/x-googlegears");
_210.setAttribute("width",0);
_210.setAttribute("height",0);
_210.style.display="none";
document.documentElement.appendChild(_210);
}
}
}
if(!_210){
return null;
}
lang.setObject("google.gears.factory",_210);
return lang.getObject("google.gears");
};
_20f.available=(!!_20f._gearsObject())||0;
return _20f;
});
},"dijit/place":function(){
define(["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","./Viewport","./main"],function(_212,_213,_214,_215,win,_216,_217){
function _218(node,_219,_21a,_21b){
var view=_216.getEffectiveBox(node.ownerDocument);
if(!node.parentNode||String(node.parentNode.tagName).toLowerCase()!="body"){
win.body(node.ownerDocument).appendChild(node);
}
var best=null;
_212.some(_219,function(_21c){
var _21d=_21c.corner;
var pos=_21c.pos;
var _21e=0;
var _21f={w:{"L":view.l+view.w-pos.x,"R":pos.x-view.l,"M":view.w}[_21d.charAt(1)],h:{"T":view.t+view.h-pos.y,"B":pos.y-view.t,"M":view.h}[_21d.charAt(0)]};
var s=node.style;
s.left=s.right="auto";
if(_21a){
var res=_21a(node,_21c.aroundCorner,_21d,_21f,_21b);
_21e=typeof res=="undefined"?0:res;
}
var _220=node.style;
var _221=_220.display;
var _222=_220.visibility;
if(_220.display=="none"){
_220.visibility="hidden";
_220.display="";
}
var bb=_213.position(node);
_220.display=_221;
_220.visibility=_222;
var _223={"L":pos.x,"R":pos.x-bb.w,"M":Math.max(view.l,Math.min(view.l+view.w,pos.x+(bb.w>>1))-bb.w)}[_21d.charAt(1)],_224={"T":pos.y,"B":pos.y-bb.h,"M":Math.max(view.t,Math.min(view.t+view.h,pos.y+(bb.h>>1))-bb.h)}[_21d.charAt(0)],_225=Math.max(view.l,_223),_226=Math.max(view.t,_224),endX=Math.min(view.l+view.w,_223+bb.w),endY=Math.min(view.t+view.h,_224+bb.h),_227=endX-_225,_228=endY-_226;
_21e+=(bb.w-_227)+(bb.h-_228);
if(best==null||_21e<best.overflow){
best={corner:_21d,aroundCorner:_21c.aroundCorner,x:_225,y:_226,w:_227,h:_228,overflow:_21e,spaceAvailable:_21f};
}
return !_21e;
});
if(best.overflow&&_21a){
_21a(node,best.aroundCorner,best.corner,best.spaceAvailable,_21b);
}
var top=best.y,side=best.x,body=win.body(node.ownerDocument);
if(/relative|absolute/.test(_214.get(body,"position"))){
top-=_214.get(body,"marginTop");
side-=_214.get(body,"marginLeft");
}
var s=node.style;
s.top=top+"px";
s.left=side+"px";
s.right="auto";
return best;
};
var _229={"TL":"BR","TR":"BL","BL":"TR","BR":"TL"};
var _22a={at:function(node,pos,_22b,_22c,_22d){
var _22e=_212.map(_22b,function(_22f){
var c={corner:_22f,aroundCorner:_229[_22f],pos:{x:pos.x,y:pos.y}};
if(_22c){
c.pos.x+=_22f.charAt(1)=="L"?_22c.x:-_22c.x;
c.pos.y+=_22f.charAt(0)=="T"?_22c.y:-_22c.y;
}
return c;
});
return _218(node,_22e,_22d);
},around:function(node,_230,_231,_232,_233){
var _234;
if(typeof _230=="string"||"offsetWidth" in _230||"ownerSVGElement" in _230){
_234=_213.position(_230,true);
if(/^(above|below)/.test(_231[0])){
var _235=_213.getBorderExtents(_230),_236=_230.firstChild?_213.getBorderExtents(_230.firstChild):{t:0,l:0,b:0,r:0},_237=_213.getBorderExtents(node),_238=node.firstChild?_213.getBorderExtents(node.firstChild):{t:0,l:0,b:0,r:0};
_234.y+=Math.min(_235.t+_236.t,_237.t+_238.t);
_234.h-=Math.min(_235.t+_236.t,_237.t+_238.t)+Math.min(_235.b+_236.b,_237.b+_238.b);
}
}else{
_234=_230;
}
if(_230.parentNode){
var _239=_214.getComputedStyle(_230).position=="absolute";
var _23a=_230.parentNode;
while(_23a&&_23a.nodeType==1&&_23a.nodeName!="BODY"){
var _23b=_213.position(_23a,true),pcs=_214.getComputedStyle(_23a);
if(/relative|absolute/.test(pcs.position)){
_239=false;
}
if(!_239&&/hidden|auto|scroll/.test(pcs.overflow)){
var _23c=Math.min(_234.y+_234.h,_23b.y+_23b.h);
var _23d=Math.min(_234.x+_234.w,_23b.x+_23b.w);
_234.x=Math.max(_234.x,_23b.x);
_234.y=Math.max(_234.y,_23b.y);
_234.h=_23c-_234.y;
_234.w=_23d-_234.x;
}
if(pcs.position=="absolute"){
_239=true;
}
_23a=_23a.parentNode;
}
}
var x=_234.x,y=_234.y,_23e="w" in _234?_234.w:(_234.w=_234.width),_23f="h" in _234?_234.h:(_215.deprecated("place.around: dijit/place.__Rectangle: { x:"+x+", y:"+y+", height:"+_234.height+", width:"+_23e+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_234.height+", w:"+_23e+" }","","2.0"),_234.h=_234.height);
var _240=[];
function push(_241,_242){
_240.push({aroundCorner:_241,corner:_242,pos:{x:{"L":x,"R":x+_23e,"M":x+(_23e>>1)}[_241.charAt(1)],y:{"T":y,"B":y+_23f,"M":y+(_23f>>1)}[_241.charAt(0)]}});
};
_212.forEach(_231,function(pos){
var ltr=_232;
switch(pos){
case "above-centered":
push("TM","BM");
break;
case "below-centered":
push("BM","TM");
break;
case "after-centered":
ltr=!ltr;
case "before-centered":
push(ltr?"ML":"MR",ltr?"MR":"ML");
break;
case "after":
ltr=!ltr;
case "before":
push(ltr?"TL":"TR",ltr?"TR":"TL");
push(ltr?"BL":"BR",ltr?"BR":"BL");
break;
case "below-alt":
ltr=!ltr;
case "below":
push(ltr?"BL":"BR",ltr?"TL":"TR");
push(ltr?"BR":"BL",ltr?"TR":"TL");
break;
case "above-alt":
ltr=!ltr;
case "above":
push(ltr?"TL":"TR",ltr?"BL":"BR");
push(ltr?"TR":"TL",ltr?"BR":"BL");
break;
default:
push(pos.aroundCorner,pos.corner);
}
});
var _243=_218(node,_240,_233,{w:_23e,h:_23f});
_243.aroundNodePos=_234;
return _243;
}};
return _217.place=_22a;
});
},"dojo/promise/all":function(){
define(["../_base/array","../Deferred","../when"],function(_244,_245,when){
"use strict";
var some=_244.some;
return function all(_246){
var _247,_244;
if(_246 instanceof Array){
_244=_246;
}else{
if(_246&&typeof _246==="object"){
_247=_246;
}
}
var _248;
var _249=[];
if(_247){
_244=[];
for(var key in _247){
if(Object.hasOwnProperty.call(_247,key)){
_249.push(key);
_244.push(_247[key]);
}
}
_248={};
}else{
if(_244){
_248=[];
}
}
if(!_244||!_244.length){
return new _245().resolve(_248);
}
var _24a=new _245();
_24a.promise.always(function(){
_248=_249=null;
});
var _24b=_244.length;
some(_244,function(_24c,_24d){
if(!_247){
_249.push(_24d);
}
when(_24c,function(_24e){
if(!_24a.isFulfilled()){
_248[_249[_24d]]=_24e;
if(--_24b===0){
_24a.resolve(_248);
}
}
},_24a.reject);
return _24a.isFulfilled();
});
return _24a.promise;
};
});
},"dojo/window":function(){
define(["./_base/lang","./sniff","./_base/window","./dom","./dom-geometry","./dom-style","./dom-construct"],function(lang,has,_24f,dom,geom,_250,_251){
has.add("rtl-adjust-position-for-verticalScrollBar",function(win,doc){
var body=_24f.body(doc),_252=_251.create("div",{style:{overflow:"scroll",overflowX:"visible",direction:"rtl",visibility:"hidden",position:"absolute",left:"0",top:"0",width:"64px",height:"64px"}},body,"last"),div=_251.create("div",{style:{overflow:"hidden",direction:"ltr"}},_252,"last"),ret=geom.position(div).x!=0;
_252.removeChild(div);
body.removeChild(_252);
return ret;
});
has.add("position-fixed-support",function(win,doc){
var body=_24f.body(doc),_253=_251.create("span",{style:{visibility:"hidden",position:"fixed",left:"1px",top:"1px"}},body,"last"),_254=_251.create("span",{style:{position:"fixed",left:"0",top:"0"}},_253,"last"),ret=geom.position(_254).x!=geom.position(_253).x;
_253.removeChild(_254);
body.removeChild(_253);
return ret;
});
var _255={getBox:function(doc){
doc=doc||_24f.doc;
var _256=(doc.compatMode=="BackCompat")?_24f.body(doc):doc.documentElement,_257=geom.docScroll(doc),w,h;
if(has("touch")){
var _258=_255.get(doc);
w=_258.innerWidth||_256.clientWidth;
h=_258.innerHeight||_256.clientHeight;
}else{
w=_256.clientWidth;
h=_256.clientHeight;
}
return {l:_257.x,t:_257.y,w:w,h:h};
},get:function(doc){
if(has("ie")&&_255!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
},scrollIntoView:function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_24f.doc,body=_24f.body(doc),html=doc.documentElement||body.parentNode,isIE=has("ie")||has("trident"),isWK=has("webkit");
if(node==body||node==html){
return;
}
if(!(has("mozilla")||isIE||isWK||has("opera")||has("trident")||has("edge"))&&("scrollIntoView" in node)){
node.scrollIntoView(false);
return;
}
var _259=doc.compatMode=="BackCompat",_25a=Math.min(body.clientWidth||html.clientWidth,html.clientWidth||body.clientWidth),_25b=Math.min(body.clientHeight||html.clientHeight,html.clientHeight||body.clientHeight),_25c=(isWK||_259)?body:html,_25d=pos||geom.position(node),el=node.parentNode,_25e=function(el){
return (isIE<=6||(isIE==7&&_259))?false:(has("position-fixed-support")&&(_250.get(el,"position").toLowerCase()=="fixed"));
},self=this,_25f=function(el,x,y){
if(el.tagName=="BODY"||el.tagName=="HTML"){
self.get(el.ownerDocument).scrollBy(x,y);
}else{
x&&(el.scrollLeft+=x);
y&&(el.scrollTop+=y);
}
};
if(_25e(node)){
return;
}
while(el){
if(el==body){
el=_25c;
}
var _260=geom.position(el),_261=_25e(el),rtl=_250.getComputedStyle(el).direction.toLowerCase()=="rtl";
if(el==_25c){
_260.w=_25a;
_260.h=_25b;
if(_25c==html&&(isIE||has("trident"))&&rtl){
_260.x+=_25c.offsetWidth-_260.w;
}
_260.x=0;
_260.y=0;
}else{
var pb=geom.getPadBorderExtents(el);
_260.w-=pb.w;
_260.h-=pb.h;
_260.x+=pb.l;
_260.y+=pb.t;
var _262=el.clientWidth,_263=_260.w-_262;
if(_262>0&&_263>0){
if(rtl&&has("rtl-adjust-position-for-verticalScrollBar")){
_260.x+=_263;
}
_260.w=_262;
}
_262=el.clientHeight;
_263=_260.h-_262;
if(_262>0&&_263>0){
_260.h=_262;
}
}
if(_261){
if(_260.y<0){
_260.h+=_260.y;
_260.y=0;
}
if(_260.x<0){
_260.w+=_260.x;
_260.x=0;
}
if(_260.y+_260.h>_25b){
_260.h=_25b-_260.y;
}
if(_260.x+_260.w>_25a){
_260.w=_25a-_260.x;
}
}
var l=_25d.x-_260.x,t=_25d.y-_260.y,r=l+_25d.w-_260.w,bot=t+_25d.h-_260.h;
var s,old;
if(r*l>0&&(!!el.scrollLeft||el==_25c||el.scrollWidth>el.offsetHeight)){
s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_259)||has("trident")>=5)){
s=-s;
}
old=el.scrollLeft;
_25f(el,s,0);
s=el.scrollLeft-old;
_25d.x-=s;
}
if(bot*t>0&&(!!el.scrollTop||el==_25c||el.scrollHeight>el.offsetHeight)){
s=Math.ceil(Math[t<0?"max":"min"](t,bot));
old=el.scrollTop;
_25f(el,0,s);
s=el.scrollTop-old;
_25d.y-=s;
}
el=(el!=_25c)&&!_261&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
}};
1&&lang.setObject("dojo.window",_255);
return _255;
});
},"dijit/form/_FormValueWidget":function(){
define(["dojo/_base/declare","dojo/sniff","./_FormWidget","./_FormValueMixin"],function(_264,has,_265,_266){
return _264("dijit.form._FormValueWidget",[_265,_266],{_layoutHackIE7:function(){
if(has("ie")==7){
var _267=this.domNode;
var _268=_267.parentNode;
var _269=_267.firstChild||_267;
var _26a=_269.style.filter;
var _26b=this;
while(_268&&_268.clientHeight==0){
(function ping(){
var _26c=_26b.connect(_268,"onscroll",function(){
_26b.disconnect(_26c);
_269.style.filter=(new Date()).getMilliseconds();
_26b.defer(function(){
_269.style.filter=_26a;
});
});
})();
_268=_268.parentNode;
}
}
}});
});
},"dijit/_OnDijitClickMixin":function(){
define(["dojo/on","dojo/_base/array","dojo/keys","dojo/_base/declare","dojo/has","./a11yclick"],function(on,_26d,keys,_26e,has,_26f){
var ret=_26e("dijit._OnDijitClickMixin",null,{connect:function(obj,_270,_271){
return this.inherited(arguments,[obj,_270=="ondijitclick"?_26f:_270,_271]);
}});
ret.a11yclick=_26f;
return ret;
});
},"dijit/a11yclick":function(){
define(["dojo/keys","dojo/mouse","dojo/on","dojo/touch"],function(keys,_272,on,_273){
function _274(e){
if((e.keyCode===keys.ENTER||e.keyCode===keys.SPACE)&&!/input|button|textarea/i.test(e.target.nodeName)){
for(var node=e.target;node;node=node.parentNode){
if(node.dojoClick){
return true;
}
}
}
};
var _275;
on(document,"keydown",function(e){
if(_274(e)){
_275=e.target;
e.preventDefault();
}else{
_275=null;
}
});
on(document,"keyup",function(e){
if(_274(e)&&e.target==_275){
_275=null;
on.emit(e.target,"click",{cancelable:true,bubbles:true,ctrlKey:e.ctrlKey,shiftKey:e.shiftKey,metaKey:e.metaKey,altKey:e.altKey,_origType:e.type});
}
});
var _276=function(node,_277){
node.dojoClick=true;
return on(node,"click",_277);
};
_276.click=_276;
_276.press=function(node,_278){
var _279=on(node,_273.press,function(evt){
if(evt.type=="mousedown"&&!_272.isLeft(evt)){
return;
}
_278(evt);
}),_27a=on(node,"keydown",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_278(evt);
}
});
return {remove:function(){
_279.remove();
_27a.remove();
}};
};
_276.release=function(node,_27b){
var _27c=on(node,_273.release,function(evt){
if(evt.type=="mouseup"&&!_272.isLeft(evt)){
return;
}
_27b(evt);
}),_27d=on(node,"keyup",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_27b(evt);
}
});
return {remove:function(){
_27c.remove();
_27d.remove();
}};
};
_276.move=_273.move;
return _276;
});
},"dojo/request":function(){
define(["./request/default!"],function(_27e){
return _27e;
});
},"dijit/hccss":function(){
define(["dojo/dom-class","dojo/hccss","dojo/domReady","dojo/_base/window"],function(_27f,has,_280,win){
_280(function(){
if(has("highcontrast")){
_27f.add(win.body(),"dijit_a11y");
}
});
return has;
});
},"dijit/dijit":function(){
define(["./main","./_base","dojo/parser","./_Widget","./_TemplatedMixin","./_Container","./layout/_LayoutWidget","./form/_FormWidget","./form/_FormValueWidget"],function(_281){
return _281;
});
},"dijit/_TemplatedMixin":function(){
define(["dojo/cache","dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/on","dojo/sniff","dojo/string","./_AttachMixin"],function(_282,_283,_284,lang,on,has,_285,_286){
var _287=_283("dijit._TemplatedMixin",_286,{templateString:null,templatePath:null,_skipNodeCache:false,searchContainerNode:true,_stringRepl:function(tmpl){
var _288=this.declaredClass,_289=this;
return _285.substitute(tmpl,this,function(_28a,key){
if(key.charAt(0)=="!"){
_28a=lang.getObject(key.substr(1),false,_289);
}
if(typeof _28a=="undefined"){
throw new Error(_288+" template:"+key);
}
if(_28a==null){
return "";
}
return key.charAt(0)=="!"?_28a:this._escapeValue(""+_28a);
},this);
},_escapeValue:function(val){
return val.replace(/["'<>&]/g,function(val){
return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;"}[val];
});
},buildRendering:function(){
if(!this._rendered){
if(!this.templateString){
this.templateString=_282(this.templatePath,{sanitize:true});
}
var _28b=_287.getCachedTemplate(this.templateString,this._skipNodeCache,this.ownerDocument);
var node;
if(lang.isString(_28b)){
node=_284.toDom(this._stringRepl(_28b),this.ownerDocument);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_28b);
}
}else{
node=_28b.cloneNode(true);
}
this.domNode=node;
}
this.inherited(arguments);
if(!this._rendered){
this._fillContent(this.srcNodeRef);
}
this._rendered=true;
},_fillContent:function(_28c){
var dest=this.containerNode;
if(_28c&&dest){
while(_28c.hasChildNodes()){
dest.appendChild(_28c.firstChild);
}
}
}});
_287._templateCache={};
_287.getCachedTemplate=function(_28d,_28e,doc){
var _28f=_287._templateCache;
var key=_28d;
var _290=_28f[key];
if(_290){
try{
if(!_290.ownerDocument||_290.ownerDocument==(doc||document)){
return _290;
}
}
catch(e){
}
_284.destroy(_290);
}
_28d=_285.trim(_28d);
if(_28e||_28d.match(/\$\{([^\}]+)\}/g)){
return (_28f[key]=_28d);
}else{
var node=_284.toDom(_28d,doc);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_28d);
}
return (_28f[key]=node);
}
};
if(has("ie")){
on(window,"unload",function(){
var _291=_287._templateCache;
for(var key in _291){
var _292=_291[key];
if(typeof _292=="object"){
_284.destroy(_292);
}
delete _291[key];
}
});
}
return _287;
});
},"dijit/selection":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/sniff","dojo/_base/window","dijit/focus"],function(_293,dom,lang,has,_294,_295){
var _296=function(win){
var doc=win.document;
this.getType=function(){
if(doc.getSelection){
var _297="text";
var oSel;
try{
oSel=win.getSelection();
}
catch(e){
}
if(oSel&&oSel.rangeCount==1){
var _298=oSel.getRangeAt(0);
if((_298.startContainer==_298.endContainer)&&((_298.endOffset-_298.startOffset)==1)&&(_298.startContainer.nodeType!=3)){
_297="control";
}
}
return _297;
}else{
return doc.selection.type.toLowerCase();
}
};
this.getSelectedText=function(){
if(doc.getSelection){
var _299=win.getSelection();
return _299?_299.toString():"";
}else{
if(this.getType()=="control"){
return null;
}
return doc.selection.createRange().text;
}
};
this.getSelectedHtml=function(){
if(doc.getSelection){
var _29a=win.getSelection();
if(_29a&&_29a.rangeCount){
var i;
var html="";
for(i=0;i<_29a.rangeCount;i++){
var frag=_29a.getRangeAt(i).cloneContents();
var div=doc.createElement("div");
div.appendChild(frag);
html+=div.innerHTML;
}
return html;
}
return null;
}else{
if(this.getType()=="control"){
return null;
}
return doc.selection.createRange().htmlText;
}
};
this.getSelectedElement=function(){
if(this.getType()=="control"){
if(doc.getSelection){
var _29b=win.getSelection();
return _29b.anchorNode.childNodes[_29b.anchorOffset];
}else{
var _29c=doc.selection.createRange();
if(_29c&&_29c.item){
return doc.selection.createRange().item(0);
}
}
}
return null;
};
this.getParentElement=function(){
if(this.getType()=="control"){
var p=this.getSelectedElement();
if(p){
return p.parentNode;
}
}else{
if(doc.getSelection){
var _29d=doc.getSelection();
if(_29d){
var node=_29d.anchorNode;
while(node&&(node.nodeType!=1)){
node=node.parentNode;
}
return node;
}
}else{
var r=doc.selection.createRange();
r.collapse(true);
return r.parentElement();
}
}
return null;
};
this.hasAncestorElement=function(_29e){
return this.getAncestorElement.apply(this,arguments)!=null;
};
this.getAncestorElement=function(_29f){
var node=this.getSelectedElement()||this.getParentElement();
return this.getParentOfType(node,arguments);
};
this.isTag=function(node,tags){
if(node&&node.tagName){
var _2a0=node.tagName.toLowerCase();
for(var i=0;i<tags.length;i++){
var _2a1=String(tags[i]).toLowerCase();
if(_2a0==_2a1){
return _2a1;
}
}
}
return "";
};
this.getParentOfType=function(node,tags){
while(node){
if(this.isTag(node,tags).length){
return node;
}
node=node.parentNode;
}
return null;
};
this.collapse=function(_2a2){
if(doc.getSelection){
var _2a3=win.getSelection();
if(_2a3.removeAllRanges){
if(_2a2){
_2a3.collapseToStart();
}else{
_2a3.collapseToEnd();
}
}else{
_2a3.collapse(_2a2);
}
}else{
var _2a4=doc.selection.createRange();
_2a4.collapse(_2a2);
_2a4.select();
}
};
this.remove=function(){
var sel=doc.selection;
if(doc.getSelection){
sel=win.getSelection();
sel.deleteFromDocument();
return sel;
}else{
if(sel.type.toLowerCase()!="none"){
sel.clear();
}
return sel;
}
};
this.selectElementChildren=function(_2a5,_2a6){
var _2a7;
_2a5=dom.byId(_2a5);
if(doc.getSelection){
var _2a8=win.getSelection();
if(has("opera")){
if(_2a8.rangeCount){
_2a7=_2a8.getRangeAt(0);
}else{
_2a7=doc.createRange();
}
_2a7.setStart(_2a5,0);
_2a7.setEnd(_2a5,(_2a5.nodeType==3)?_2a5.length:_2a5.childNodes.length);
_2a8.addRange(_2a7);
}else{
_2a8.selectAllChildren(_2a5);
}
}else{
_2a7=_2a5.ownerDocument.body.createTextRange();
_2a7.moveToElementText(_2a5);
if(!_2a6){
try{
_2a7.select();
}
catch(e){
}
}
}
};
this.selectElement=function(_2a9,_2aa){
var _2ab;
_2a9=dom.byId(_2a9);
if(doc.getSelection){
var _2ac=doc.getSelection();
_2ab=doc.createRange();
if(_2ac.removeAllRanges){
if(has("opera")){
if(_2ac.getRangeAt(0)){
_2ab=_2ac.getRangeAt(0);
}
}
_2ab.selectNode(_2a9);
_2ac.removeAllRanges();
_2ac.addRange(_2ab);
}
}else{
try{
var tg=_2a9.tagName?_2a9.tagName.toLowerCase():"";
if(tg==="img"||tg==="table"){
_2ab=_294.body(doc).createControlRange();
}else{
_2ab=_294.body(doc).createRange();
}
_2ab.addElement(_2a9);
if(!_2aa){
_2ab.select();
}
}
catch(e){
this.selectElementChildren(_2a9,_2aa);
}
}
};
this.inSelection=function(node){
if(node){
var _2ad;
var _2ae;
if(doc.getSelection){
var sel=win.getSelection();
if(sel&&sel.rangeCount>0){
_2ae=sel.getRangeAt(0);
}
if(_2ae&&_2ae.compareBoundaryPoints&&doc.createRange){
try{
_2ad=doc.createRange();
_2ad.setStart(node,0);
if(_2ae.compareBoundaryPoints(_2ae.START_TO_END,_2ad)===1){
return true;
}
}
catch(e){
}
}
}else{
_2ae=doc.selection.createRange();
try{
_2ad=node.ownerDocument.body.createTextRange();
_2ad.moveToElementText(node);
}
catch(e2){
}
if(_2ae&&_2ad){
if(_2ae.compareEndPoints("EndToStart",_2ad)===1){
return true;
}
}
}
}
return false;
};
this.getBookmark=function(){
var bm,rg,tg,sel=doc.selection,cf=_295.curNode;
if(doc.getSelection){
sel=win.getSelection();
if(sel){
if(sel.isCollapsed){
tg=cf?cf.tagName:"";
if(tg){
tg=tg.toLowerCase();
if(tg=="textarea"||(tg=="input"&&(!cf.type||cf.type.toLowerCase()=="text"))){
sel={start:cf.selectionStart,end:cf.selectionEnd,node:cf,pRange:true};
return {isCollapsed:(sel.end<=sel.start),mark:sel};
}
}
bm={isCollapsed:true};
if(sel.rangeCount){
bm.mark=sel.getRangeAt(0).cloneRange();
}
}else{
rg=sel.getRangeAt(0);
bm={isCollapsed:false,mark:rg.cloneRange()};
}
}
}else{
if(sel){
tg=cf?cf.tagName:"";
tg=tg.toLowerCase();
if(cf&&tg&&(tg=="button"||tg=="textarea"||tg=="input")){
if(sel.type&&sel.type.toLowerCase()=="none"){
return {isCollapsed:true,mark:null};
}else{
rg=sel.createRange();
return {isCollapsed:rg.text&&rg.text.length?false:true,mark:{range:rg,pRange:true}};
}
}
bm={};
try{
rg=sel.createRange();
bm.isCollapsed=!(sel.type=="Text"?rg.htmlText.length:rg.length);
}
catch(e){
bm.isCollapsed=true;
return bm;
}
if(sel.type.toUpperCase()=="CONTROL"){
if(rg.length){
bm.mark=[];
var i=0,len=rg.length;
while(i<len){
bm.mark.push(rg.item(i++));
}
}else{
bm.isCollapsed=true;
bm.mark=null;
}
}else{
bm.mark=rg.getBookmark();
}
}else{
console.warn("No idea how to store the current selection for this browser!");
}
}
return bm;
};
this.moveToBookmark=function(_2af){
var mark=_2af.mark;
if(mark){
if(doc.getSelection){
var sel=win.getSelection();
if(sel&&sel.removeAllRanges){
if(mark.pRange){
var n=mark.node;
n.selectionStart=mark.start;
n.selectionEnd=mark.end;
}else{
sel.removeAllRanges();
sel.addRange(mark);
}
}else{
console.warn("No idea how to restore selection for this browser!");
}
}else{
if(doc.selection&&mark){
var rg;
if(mark.pRange){
rg=mark.range;
}else{
if(lang.isArray(mark)){
rg=doc.body.createControlRange();
_293.forEach(mark,function(n){
rg.addElement(n);
});
}else{
rg=doc.body.createTextRange();
rg.moveToBookmark(mark);
}
}
rg.select();
}
}
}
};
this.isCollapsed=function(){
return this.getBookmark().isCollapsed;
};
};
var _2b0=new _296(window);
_2b0.SelectionManager=_296;
return _2b0;
});
},"dijit/form/_FormWidget":function(){
define(["dojo/_base/declare","dojo/sniff","dojo/_base/kernel","dojo/ready","../_Widget","../_CssStateMixin","../_TemplatedMixin","./_FormWidgetMixin"],function(_2b1,has,_2b2,_2b3,_2b4,_2b5,_2b6,_2b7){
if(1){
_2b3(0,function(){
var _2b8=["dijit/form/_FormValueWidget"];
require(_2b8);
});
}
return _2b1("dijit.form._FormWidget",[_2b4,_2b6,_2b5,_2b7],{setDisabled:function(_2b9){
_2b2.deprecated("setDisabled("+_2b9+") is deprecated. Use set('disabled',"+_2b9+") instead.","","2.0");
this.set("disabled",_2b9);
},setValue:function(_2ba){
_2b2.deprecated("dijit.form._FormWidget:setValue("+_2ba+") is deprecated.  Use set('value',"+_2ba+") instead.","","2.0");
this.set("value",_2ba);
},getValue:function(){
_2b2.deprecated(this.declaredClass+"::getValue() is deprecated. Use get('value') instead.","","2.0");
return this.get("value");
},postMixInProperties:function(){
this.nameAttrSetting=(this.name&&!has("msapp"))?("name=\""+this.name.replace(/"/g,"&quot;")+"\""):"";
this.inherited(arguments);
}});
});
},"dijit/_base/focus":function(){
define(["dojo/_base/array","dojo/dom","dojo/_base/lang","dojo/topic","dojo/_base/window","../focus","../selection","../main"],function(_2bb,dom,lang,_2bc,win,_2bd,_2be,_2bf){
var _2c0={_curFocus:null,_prevFocus:null,isCollapsed:function(){
return _2bf.getBookmark().isCollapsed;
},getBookmark:function(){
var sel=win.global==window?_2be:new _2be.SelectionManager(win.global);
return sel.getBookmark();
},moveToBookmark:function(_2c1){
var sel=win.global==window?_2be:new _2be.SelectionManager(win.global);
return sel.moveToBookmark(_2c1);
},getFocus:function(menu,_2c2){
var node=!_2bd.curNode||(menu&&dom.isDescendant(_2bd.curNode,menu.domNode))?_2bf._prevFocus:_2bd.curNode;
return {node:node,bookmark:node&&(node==_2bd.curNode)&&win.withGlobal(_2c2||win.global,_2bf.getBookmark),openedForWindow:_2c2};
},_activeStack:[],registerIframe:function(_2c3){
return _2bd.registerIframe(_2c3);
},unregisterIframe:function(_2c4){
_2c4&&_2c4.remove();
},registerWin:function(_2c5,_2c6){
return _2bd.registerWin(_2c5,_2c6);
},unregisterWin:function(_2c7){
_2c7&&_2c7.remove();
}};
_2bd.focus=function(_2c8){
if(!_2c8){
return;
}
var node="node" in _2c8?_2c8.node:_2c8,_2c9=_2c8.bookmark,_2ca=_2c8.openedForWindow,_2cb=_2c9?_2c9.isCollapsed:false;
if(node){
var _2cc=(node.tagName.toLowerCase()=="iframe")?node.contentWindow:node;
if(_2cc&&_2cc.focus){
try{
_2cc.focus();
}
catch(e){
}
}
_2bd._onFocusNode(node);
}
if(_2c9&&win.withGlobal(_2ca||win.global,_2bf.isCollapsed)&&!_2cb){
if(_2ca){
_2ca.focus();
}
try{
win.withGlobal(_2ca||win.global,_2bf.moveToBookmark,null,[_2c9]);
}
catch(e2){
}
}
};
_2bd.watch("curNode",function(name,_2cd,_2ce){
_2bf._curFocus=_2ce;
_2bf._prevFocus=_2cd;
if(_2ce){
_2bc.publish("focusNode",_2ce);
}
});
_2bd.watch("activeStack",function(name,_2cf,_2d0){
_2bf._activeStack=_2d0;
});
_2bd.on("widget-blur",function(_2d1,by){
_2bc.publish("widgetBlur",_2d1,by);
});
_2bd.on("widget-focus",function(_2d2,by){
_2bc.publish("widgetFocus",_2d2,by);
});
lang.mixin(_2bf,_2c0);
return _2bf;
});
},"dojo/parser":function(){
define(["require","./_base/kernel","./_base/lang","./_base/array","./_base/config","./dom","./_base/window","./_base/url","./aspect","./promise/all","./date/stamp","./Deferred","./has","./query","./on","./ready"],function(_2d3,dojo,_2d4,_2d5,_2d6,dom,_2d7,_2d8,_2d9,all,_2da,_2db,has,_2dc,don,_2dd){
new Date("X");
function _2de(text){
return eval("("+text+")");
};
var _2df=0;
_2d9.after(_2d4,"extend",function(){
_2df++;
},true);
function _2e0(ctor){
var map=ctor._nameCaseMap,_2e1=ctor.prototype;
if(!map||map._extendCnt<_2df){
map=ctor._nameCaseMap={};
for(var name in _2e1){
if(name.charAt(0)==="_"){
continue;
}
map[name.toLowerCase()]=name;
}
map._extendCnt=_2df;
}
return map;
};
var _2e2={};
function _2e3(_2e4,_2e5){
var ts=_2e4.join();
if(!_2e2[ts]){
var _2e6=[];
for(var i=0,l=_2e4.length;i<l;i++){
var t=_2e4[i];
_2e6[_2e6.length]=(_2e2[t]=_2e2[t]||(_2d4.getObject(t)||(~t.indexOf("/")&&(_2e5?_2e5(t):_2d3(t)))));
}
var ctor=_2e6.shift();
_2e2[ts]=_2e6.length?(ctor.createSubclass?ctor.createSubclass(_2e6):ctor.extend.apply(ctor,_2e6)):ctor;
}
return _2e2[ts];
};
var _2e7={_clearCache:function(){
_2df++;
_2e2={};
},_functionFromScript:function(_2e8,_2e9){
var _2ea="",_2eb="",_2ec=(_2e8.getAttribute(_2e9+"args")||_2e8.getAttribute("args")),_2ed=_2e8.getAttribute("with");
var _2ee=(_2ec||"").split(/\s*,\s*/);
if(_2ed&&_2ed.length){
_2d5.forEach(_2ed.split(/\s*,\s*/),function(part){
_2ea+="with("+part+"){";
_2eb+="}";
});
}
return new Function(_2ee,_2ea+_2e8.innerHTML+_2eb);
},instantiate:function(_2ef,_2f0,_2f1){
_2f0=_2f0||{};
_2f1=_2f1||{};
var _2f2=(_2f1.scope||dojo._scopeName)+"Type",_2f3="data-"+(_2f1.scope||dojo._scopeName)+"-",_2f4=_2f3+"type",_2f5=_2f3+"mixins";
var list=[];
_2d5.forEach(_2ef,function(node){
var type=_2f2 in _2f0?_2f0[_2f2]:node.getAttribute(_2f4)||node.getAttribute(_2f2);
if(type){
var _2f6=node.getAttribute(_2f5),_2f7=_2f6?[type].concat(_2f6.split(/\s*,\s*/)):[type];
list.push({node:node,types:_2f7});
}
});
return this._instantiate(list,_2f0,_2f1);
},_instantiate:function(_2f8,_2f9,_2fa,_2fb){
var _2fc=_2d5.map(_2f8,function(obj){
var ctor=obj.ctor||_2e3(obj.types,_2fa.contextRequire);
if(!ctor){
throw new Error("Unable to resolve constructor for: '"+obj.types.join()+"'");
}
return this.construct(ctor,obj.node,_2f9,_2fa,obj.scripts,obj.inherited);
},this);
function _2fd(_2fe){
if(!_2f9._started&&!_2fa.noStart){
_2d5.forEach(_2fe,function(_2ff){
if(typeof _2ff.startup==="function"&&!_2ff._started){
_2ff.startup();
}
});
}
return _2fe;
};
if(_2fb){
return all(_2fc).then(_2fd);
}else{
return _2fd(_2fc);
}
},construct:function(ctor,node,_300,_301,_302,_303){
var _304=ctor&&ctor.prototype;
_301=_301||{};
var _305={};
if(_301.defaults){
_2d4.mixin(_305,_301.defaults);
}
if(_303){
_2d4.mixin(_305,_303);
}
var _306;
if(has("dom-attributes-explicit")){
_306=node.attributes;
}else{
if(has("dom-attributes-specified-flag")){
_306=_2d5.filter(node.attributes,function(a){
return a.specified;
});
}else{
var _307=/^input$|^img$/i.test(node.nodeName)?node:node.cloneNode(false),_308=_307.outerHTML.replace(/=[^\s"']+|="[^"]*"|='[^']*'/g,"").replace(/^\s*<[a-zA-Z0-9]*\s*/,"").replace(/\s*>.*$/,"");
_306=_2d5.map(_308.split(/\s+/),function(name){
var _309=name.toLowerCase();
return {name:name,value:(node.nodeName=="LI"&&name=="value")||_309=="enctype"?node.getAttribute(_309):node.getAttributeNode(_309).value};
});
}
}
var _30a=_301.scope||dojo._scopeName,_30b="data-"+_30a+"-",hash={};
if(_30a!=="dojo"){
hash[_30b+"props"]="data-dojo-props";
hash[_30b+"type"]="data-dojo-type";
hash[_30b+"mixins"]="data-dojo-mixins";
hash[_30a+"type"]="dojoType";
hash[_30b+"id"]="data-dojo-id";
}
var i=0,item,_30c=[],_30d,_30e;
while(item=_306[i++]){
var name=item.name,_30f=name.toLowerCase(),_310=item.value;
switch(hash[_30f]||_30f){
case "data-dojo-type":
case "dojotype":
case "data-dojo-mixins":
break;
case "data-dojo-props":
_30e=_310;
break;
case "data-dojo-id":
case "jsid":
_30d=_310;
break;
case "data-dojo-attach-point":
case "dojoattachpoint":
_305.dojoAttachPoint=_310;
break;
case "data-dojo-attach-event":
case "dojoattachevent":
_305.dojoAttachEvent=_310;
break;
case "class":
_305["class"]=node.className;
break;
case "style":
_305["style"]=node.style&&node.style.cssText;
break;
default:
if(!(name in _304)){
var map=_2e0(ctor);
name=map[_30f]||name;
}
if(name in _304){
switch(typeof _304[name]){
case "string":
_305[name]=_310;
break;
case "number":
_305[name]=_310.length?Number(_310):NaN;
break;
case "boolean":
_305[name]=_310.toLowerCase()!="false";
break;
case "function":
if(_310===""||_310.search(/[^\w\.]+/i)!=-1){
_305[name]=new Function(_310);
}else{
_305[name]=_2d4.getObject(_310,false)||new Function(_310);
}
_30c.push(name);
break;
default:
var pVal=_304[name];
_305[name]=(pVal&&"length" in pVal)?(_310?_310.split(/\s*,\s*/):[]):(pVal instanceof Date)?(_310==""?new Date(""):_310=="now"?new Date():_2da.fromISOString(_310)):(pVal instanceof _2d8)?(dojo.baseUrl+_310):_2de(_310);
}
}else{
_305[name]=_310;
}
}
}
for(var j=0;j<_30c.length;j++){
var _311=_30c[j].toLowerCase();
node.removeAttribute(_311);
node[_311]=null;
}
if(_30e){
try{
_30e=_2de.call(_301.propsThis,"{"+_30e+"}");
_2d4.mixin(_305,_30e);
}
catch(e){
throw new Error(e.toString()+" in data-dojo-props='"+_30e+"'");
}
}
_2d4.mixin(_305,_300);
if(!_302){
_302=(ctor&&(ctor._noScript||_304._noScript)?[]:_2dc("> script[type^='dojo/']",node));
}
var _312=[],_313=[],_314=[],ons=[];
if(_302){
for(i=0;i<_302.length;i++){
var _315=_302[i];
node.removeChild(_315);
var _316=(_315.getAttribute(_30b+"event")||_315.getAttribute("event")),prop=_315.getAttribute(_30b+"prop"),_317=_315.getAttribute(_30b+"method"),_318=_315.getAttribute(_30b+"advice"),_319=_315.getAttribute("type"),nf=this._functionFromScript(_315,_30b);
if(_316){
if(_319=="dojo/connect"){
_312.push({method:_316,func:nf});
}else{
if(_319=="dojo/on"){
ons.push({event:_316,func:nf});
}else{
_305[_316]=nf;
}
}
}else{
if(_319=="dojo/aspect"){
_312.push({method:_317,advice:_318,func:nf});
}else{
if(_319=="dojo/watch"){
_314.push({prop:prop,func:nf});
}else{
_313.push(nf);
}
}
}
}
}
var _31a=ctor.markupFactory||_304.markupFactory;
var _31b=_31a?_31a(_305,node,ctor):new ctor(_305,node);
function _31c(_31d){
if(_30d){
_2d4.setObject(_30d,_31d);
}
for(i=0;i<_312.length;i++){
_2d9[_312[i].advice||"after"](_31d,_312[i].method,_2d4.hitch(_31d,_312[i].func),true);
}
for(i=0;i<_313.length;i++){
_313[i].call(_31d);
}
for(i=0;i<_314.length;i++){
_31d.watch(_314[i].prop,_314[i].func);
}
for(i=0;i<ons.length;i++){
don(_31d,ons[i].event,ons[i].func);
}
return _31d;
};
if(_31b.then){
return _31b.then(_31c);
}else{
return _31c(_31b);
}
},scan:function(root,_31e){
var list=[],mids=[],_31f={};
var _320=(_31e.scope||dojo._scopeName)+"Type",_321="data-"+(_31e.scope||dojo._scopeName)+"-",_322=_321+"type",_323=_321+"textdir",_324=_321+"mixins";
var node=root.firstChild;
var _325=_31e.inherited;
if(!_325){
function _326(node,attr){
return (node.getAttribute&&node.getAttribute(attr))||(node.parentNode&&_326(node.parentNode,attr));
};
_325={dir:_326(root,"dir"),lang:_326(root,"lang"),textDir:_326(root,_323)};
for(var key in _325){
if(!_325[key]){
delete _325[key];
}
}
}
var _327={inherited:_325};
var _328;
var _329;
function _32a(_32b){
if(!_32b.inherited){
_32b.inherited={};
var node=_32b.node,_32c=_32a(_32b.parent);
var _32d={dir:node.getAttribute("dir")||_32c.dir,lang:node.getAttribute("lang")||_32c.lang,textDir:node.getAttribute(_323)||_32c.textDir};
for(var key in _32d){
if(_32d[key]){
_32b.inherited[key]=_32d[key];
}
}
}
return _32b.inherited;
};
while(true){
if(!node){
if(!_327||!_327.node){
break;
}
node=_327.node.nextSibling;
_329=false;
_327=_327.parent;
_328=_327.scripts;
continue;
}
if(node.nodeType!=1){
node=node.nextSibling;
continue;
}
if(_328&&node.nodeName.toLowerCase()=="script"){
type=node.getAttribute("type");
if(type&&/^dojo\/\w/i.test(type)){
_328.push(node);
}
node=node.nextSibling;
continue;
}
if(_329){
node=node.nextSibling;
continue;
}
var type=node.getAttribute(_322)||node.getAttribute(_320);
var _32e=node.firstChild;
if(!type&&(!_32e||(_32e.nodeType==3&&!_32e.nextSibling))){
node=node.nextSibling;
continue;
}
var _32f;
var ctor=null;
if(type){
var _330=node.getAttribute(_324),_331=_330?[type].concat(_330.split(/\s*,\s*/)):[type];
try{
ctor=_2e3(_331,_31e.contextRequire);
}
catch(e){
}
if(!ctor){
_2d5.forEach(_331,function(t){
if(~t.indexOf("/")&&!_31f[t]){
_31f[t]=true;
mids[mids.length]=t;
}
});
}
var _332=ctor&&!ctor.prototype._noScript?[]:null;
_32f={types:_331,ctor:ctor,parent:_327,node:node,scripts:_332};
_32f.inherited=_32a(_32f);
list.push(_32f);
}else{
_32f={node:node,scripts:_328,parent:_327};
}
_328=_332;
_329=node.stopParser||(ctor&&ctor.prototype.stopParser&&!(_31e.template));
_327=_32f;
node=_32e;
}
var d=new _2db();
if(mids.length){
if(has("dojo-debug-messages")){
console.warn("WARNING: Modules being Auto-Required: "+mids.join(", "));
}
var r=_31e.contextRequire||_2d3;
r(mids,function(){
d.resolve(_2d5.filter(list,function(_333){
if(!_333.ctor){
try{
_333.ctor=_2e3(_333.types,_31e.contextRequire);
}
catch(e){
}
}
var _334=_333.parent;
while(_334&&!_334.types){
_334=_334.parent;
}
var _335=_333.ctor&&_333.ctor.prototype;
_333.instantiateChildren=!(_335&&_335.stopParser&&!(_31e.template));
_333.instantiate=!_334||(_334.instantiate&&_334.instantiateChildren);
return _333.instantiate;
}));
});
}else{
d.resolve(list);
}
return d.promise;
},_require:function(_336,_337){
var hash=_2de("{"+_336.innerHTML+"}"),vars=[],mids=[],d=new _2db();
var _338=(_337&&_337.contextRequire)||_2d3;
for(var name in hash){
vars.push(name);
mids.push(hash[name]);
}
_338(mids,function(){
for(var i=0;i<vars.length;i++){
_2d4.setObject(vars[i],arguments[i]);
}
d.resolve(arguments);
});
return d.promise;
},_scanAmd:function(root,_339){
var _33a=new _2db(),_33b=_33a.promise;
_33a.resolve(true);
var self=this;
_2dc("script[type='dojo/require']",root).forEach(function(node){
_33b=_33b.then(function(){
return self._require(node,_339);
});
node.parentNode.removeChild(node);
});
return _33b;
},parse:function(_33c,_33d){
if(_33c&&typeof _33c!="string"&&!("nodeType" in _33c)){
_33d=_33c;
_33c=_33d.rootNode;
}
var root=_33c?dom.byId(_33c):_2d7.body();
_33d=_33d||{};
var _33e=_33d.template?{template:true}:{},_33f=[],self=this;
var p=this._scanAmd(root,_33d).then(function(){
return self.scan(root,_33d);
}).then(function(_340){
return self._instantiate(_340,_33e,_33d,true);
}).then(function(_341){
return _33f=_33f.concat(_341);
}).otherwise(function(e){
console.error("dojo/parser::parse() error",e);
throw e;
});
_2d4.mixin(_33f,p);
return _33f;
}};
if(1){
dojo.parser=_2e7;
}
if(_2d6.parseOnLoad){
_2dd(100,_2e7,"parse");
}
return _2e7;
});
},"dijit/layout/_LayoutWidget":function(){
define(["dojo/_base/lang","../_Widget","../_Container","../_Contained","../Viewport","dojo/_base/declare","dojo/dom-class","dojo/dom-geometry","dojo/dom-style"],function(lang,_342,_343,_344,_345,_346,_347,_348,_349){
return _346("dijit.layout._LayoutWidget",[_342,_343,_344],{baseClass:"dijitLayoutContainer",isLayoutContainer:true,_setTitleAttr:null,buildRendering:function(){
this.inherited(arguments);
_347.add(this.domNode,"dijitContainer");
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
var _34a=this.getParent&&this.getParent();
if(!(_34a&&_34a.isLayoutContainer)){
this.resize();
this.own(_345.on("resize",lang.hitch(this,"resize")));
}
},resize:function(_34b,_34c){
var node=this.domNode;
if(_34b){
_348.setMarginBox(node,_34b);
}
var mb=_34c||{};
lang.mixin(mb,_34b||{});
if(!("h" in mb)||!("w" in mb)){
mb=lang.mixin(_348.getMarginBox(node),mb);
}
var cs=_349.getComputedStyle(node);
var me=_348.getMarginExtents(node,cs);
var be=_348.getBorderExtents(node,cs);
var bb=(this._borderBox={w:mb.w-(me.w+be.w),h:mb.h-(me.h+be.h)});
var pe=_348.getPadExtents(node,cs);
this._contentBox={l:_349.toPixelValue(node,cs.paddingLeft),t:_349.toPixelValue(node,cs.paddingTop),w:bb.w-pe.w,h:bb.h-pe.h};
this.layout();
},layout:function(){
},_setupChild:function(_34d){
var cls=this.baseClass+"-child "+(_34d.baseClass?this.baseClass+"-"+_34d.baseClass:"");
_347.add(_34d.domNode,cls);
},addChild:function(_34e,_34f){
this.inherited(arguments);
if(this._started){
this._setupChild(_34e);
}
},removeChild:function(_350){
var cls=this.baseClass+"-child"+(_350.baseClass?" "+this.baseClass+"-"+_350.baseClass:"");
_347.remove(_350.domNode,cls);
this.inherited(arguments);
}});
});
},"dijit/_Widget":function(){
define(["dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/ready","./registry","./_WidgetBase","./_OnDijitClickMixin","./_FocusMixin","dojo/uacss","./hccss"],function(_351,_352,_353,_354,has,_355,lang,_356,_357,_358,_359,_35a,_35b){
function _35c(){
};
function _35d(_35e){
return function(obj,_35f,_360,_361){
if(obj&&typeof _35f=="string"&&obj[_35f]==_35c){
return obj.on(_35f.substring(2).toLowerCase(),lang.hitch(_360,_361));
}
return _35e.apply(_353,arguments);
};
};
_351.around(_353,"connect",_35d);
if(_355.connect){
_351.around(_355,"connect",_35d);
}
var _362=_354("dijit._Widget",[_359,_35a,_35b],{onClick:_35c,onDblClick:_35c,onKeyDown:_35c,onKeyPress:_35c,onKeyUp:_35c,onMouseDown:_35c,onMouseMove:_35c,onMouseOut:_35c,onMouseOver:_35c,onMouseLeave:_35c,onMouseEnter:_35c,onMouseUp:_35c,constructor:function(_363){
this._toConnect={};
for(var name in _363){
if(this[name]===_35c){
this._toConnect[name.replace(/^on/,"").toLowerCase()]=_363[name];
delete _363[name];
}
}
},postCreate:function(){
this.inherited(arguments);
for(var name in this._toConnect){
this.on(name,this._toConnect[name]);
}
delete this._toConnect;
},on:function(type,func){
if(this[this._onMap(type)]===_35c){
return _353.connect(this.domNode,type.toLowerCase(),this,func);
}
return this.inherited(arguments);
},_setFocusedAttr:function(val){
this._focused=val;
this._set("focused",val);
},setAttribute:function(attr,_364){
_355.deprecated(this.declaredClass+"::setAttribute(attr, value) is deprecated. Use set() instead.","","2.0");
this.set(attr,_364);
},attr:function(name,_365){
var args=arguments.length;
if(args>=2||typeof name==="object"){
return this.set.apply(this,arguments);
}else{
return this.get(name);
}
},getDescendants:function(){
_355.deprecated(this.declaredClass+"::getDescendants() is deprecated. Use getChildren() instead.","","2.0");
return this.containerNode?_356("[widgetId]",this.containerNode).map(_358.byNode):[];
},_onShow:function(){
this.onShow();
},onShow:function(){
},onHide:function(){
},onClose:function(){
return true;
}});
if(1){
_357(0,function(){
var _366=["dijit/_base"];
require(_366);
});
}
return _362;
});
},"dijit/_FocusMixin":function(){
define(["./focus","./_WidgetBase","dojo/_base/declare","dojo/_base/lang"],function(_367,_368,_369,lang){
lang.extend(_368,{focused:false,onFocus:function(){
},onBlur:function(){
},_onFocus:function(){
this.onFocus();
},_onBlur:function(){
this.onBlur();
}});
return _369("dijit._FocusMixin",null,{_focusManager:_367});
});
},"dijit/focus":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/domReady","dojo/sniff","dojo/Stateful","dojo/_base/window","dojo/window","./a11y","./registry","./main"],function(_36a,_36b,dom,_36c,_36d,_36e,_36f,lang,on,_370,has,_371,win,_372,a11y,_373,_374){
var _375;
var _376;
var _377=_36b([_371,_36f],{curNode:null,activeStack:[],constructor:function(){
var _378=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_36a.before(_36e,"empty",_378);
_36a.before(_36e,"destroy",_378);
},registerIframe:function(_379){
return this.registerWin(_379.contentWindow,_379);
},registerWin:function(_37a,_37b){
var _37c=this,body=_37a.document&&_37a.document.body;
if(body){
var _37d=has("pointer-events")?"pointerdown":has("MSPointer")?"MSPointerDown":has("touch-events")?"mousedown, touchstart":"mousedown";
var mdh=on(_37a.document,_37d,function(evt){
if(evt&&evt.target&&evt.target.parentNode==null){
return;
}
_37c._onTouchNode(_37b||evt.target,"mouse");
});
var fih=on(body,"focusin",function(evt){
if(!evt.target.tagName){
return;
}
var tag=evt.target.tagName.toLowerCase();
if(tag=="#document"||tag=="body"){
return;
}
if(a11y.isFocusable(evt.target)){
_37c._onFocusNode(_37b||evt.target);
}else{
_37c._onTouchNode(_37b||evt.target);
}
});
var foh=on(body,"focusout",function(evt){
_37c._onBlurNode(_37b||evt.target);
});
return {remove:function(){
mdh.remove();
fih.remove();
foh.remove();
mdh=fih=foh=null;
body=null;
}};
}
},_onBlurNode:function(node){
var now=(new Date()).getTime();
if(now<_375+100){
return;
}
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
}
this._clearFocusTimer=setTimeout(lang.hitch(this,function(){
this.set("prevNode",this.curNode);
this.set("curNode",null);
}),0);
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
}
if(now<_376+100){
return;
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
}),0);
},_onTouchNode:function(node,by){
_376=(new Date()).getTime();
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_36d.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _37e=[];
try{
while(node){
var _37f=_36c.get(node,"dijitPopupParent");
if(_37f){
node=_373.byId(_37f).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_372.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_380=id&&_373.byId(id);
if(_380&&!(by=="mouse"&&_380.get("disabled"))){
_37e.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_37e,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
_375=(new Date()).getTime();
if(this._clearFocusTimer){
clearTimeout(this._clearFocusTimer);
delete this._clearFocusTimer;
}
this._onTouchNode(node);
if(node==this.curNode){
return;
}
this.set("prevNode",this.curNode);
this.set("curNode",node);
},_setStack:function(_381,by){
var _382=this.activeStack,_383=_382.length-1,_384=_381.length-1;
if(_381[_384]==_382[_383]){
return;
}
this.set("activeStack",_381);
var _385,i;
for(i=_383;i>=0&&_382[i]!=_381[i];i--){
_385=_373.byId(_382[i]);
if(_385){
_385._hasBeenBlurred=true;
_385.set("focused",false);
if(_385._focusManager==this){
_385._onBlur(by);
}
this.emit("widget-blur",_385,by);
}
}
for(i++;i<=_384;i++){
_385=_373.byId(_381[i]);
if(_385){
_385.set("focused",true);
if(_385._focusManager==this){
_385._onFocus(by);
}
this.emit("widget-focus",_385,by);
}
}
},focus:function(node){
if(node){
try{
node.focus();
}
catch(e){
}
}
}});
var _386=new _377();
_370(function(){
var _387=_386.registerWin(_372.get(document));
if(has("ie")){
on(window,"unload",function(){
if(_387){
_387.remove();
_387=null;
}
});
}
});
_374.focus=function(node){
_386.focus(node);
};
for(var attr in _386){
if(!/^_/.test(attr)){
_374.focus[attr]=typeof _386[attr]=="function"?lang.hitch(_386,attr):_386[attr];
}
}
_386.watch(function(attr,_388,_389){
_374.focus[attr]=_389;
});
return _386;
});
},"dojo/behavior":function(){
define(["./_base/kernel","./_base/lang","./_base/array","./_base/connect","./query","./domReady"],function(dojo,lang,_38a,_38b,_38c,_38d){
dojo.deprecated("dojo.behavior","Use dojo/on with event delegation (on.selector())");
var _38e=function(){
function _38f(obj,name){
if(!obj[name]){
obj[name]=[];
}
return obj[name];
};
var _390=0;
function _391(obj,_392,func){
var _393={};
for(var x in obj){
if(typeof _393[x]=="undefined"){
if(!func){
_392(obj[x],x);
}else{
func.call(_392,obj[x],x);
}
}
}
};
this._behaviors={};
this.add=function(_394){
_391(_394,this,function(_395,name){
var _396=_38f(this._behaviors,name);
if(typeof _396["id"]!="number"){
_396.id=_390++;
}
var _397=[];
_396.push(_397);
if((lang.isString(_395))||(lang.isFunction(_395))){
_395={found:_395};
}
_391(_395,function(rule,_398){
_38f(_397,_398).push(rule);
});
});
};
var _399=function(node,_39a,_39b){
if(lang.isString(_39a)){
if(_39b=="found"){
_38b.publish(_39a,[node]);
}else{
_38b.connect(node,_39b,function(){
_38b.publish(_39a,arguments);
});
}
}else{
if(lang.isFunction(_39a)){
if(_39b=="found"){
_39a(node);
}else{
_38b.connect(node,_39b,_39a);
}
}
}
};
this.apply=function(){
_391(this._behaviors,function(_39c,id){
_38c(id).forEach(function(elem){
var _39d=0;
var bid="_dj_behavior_"+_39c.id;
if(typeof elem[bid]=="number"){
_39d=elem[bid];
if(_39d==(_39c.length)){
return;
}
}
for(var x=_39d,tver;tver=_39c[x];x++){
_391(tver,function(_39e,_39f){
if(lang.isArray(_39e)){
_38a.forEach(_39e,function(_3a0){
_399(elem,_3a0,_39f);
});
}
});
}
elem[bid]=_39c.length;
});
});
};
};
dojo.behavior=new _38e();
_38d(function(){
dojo.behavior.apply();
});
return dojo.behavior;
});
},"dijit/_Contained":function(){
define(["dojo/_base/declare","./registry"],function(_3a1,_3a2){
return _3a1("dijit._Contained",null,{_getSibling:function(_3a3){
var p=this.getParent();
return (p&&p._getSiblingOfChild&&p._getSiblingOfChild(this,_3a3=="previous"?-1:1))||null;
},getPreviousSibling:function(){
return this._getSibling("previous");
},getNextSibling:function(){
return this._getSibling("next");
},getIndexInParent:function(){
var p=this.getParent();
if(!p||!p.getIndexOfChild){
return -1;
}
return p.getIndexOfChild(this);
}});
});
},"dijit/_base/scroll":function(){
define(["dojo/window","../main"],function(_3a4,_3a5){
_3a5.scrollIntoView=function(node,pos){
_3a4.scrollIntoView(node,pos);
};
});
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_3a6,_3a7,_3a8){
return _3a8("dijit.Destroyable",null,{destroy:function(_3a9){
this._destroyed=true;
},own:function(){
var _3aa=["destroyRecursive","destroy","remove"];
_3a6.forEach(arguments,function(_3ab){
var _3ac;
var odh=_3a7.before(this,"destroy",function(_3ad){
_3ab[_3ac](_3ad);
});
var hdhs=[];
function _3ae(){
odh.remove();
_3a6.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_3ab.then){
_3ac="cancel";
_3ab.then(_3ae,_3ae);
}else{
_3a6.forEach(_3aa,function(_3af){
if(typeof _3ab[_3af]==="function"){
if(!_3ac){
_3ac=_3af;
}
hdhs.push(_3a7.after(_3ab,_3af,_3ae,true));
}
});
}
},this);
return arguments;
}});
});
},"dojo/cache":function(){
define(["./_base/kernel","./text"],function(dojo){
return dojo.cache;
});
},"dijit/_base/window":function(){
define(["dojo/window","../main"],function(_3b0,_3b1){
_3b1.getDocumentWindow=function(doc){
return _3b0.get(doc);
};
});
},"dojo/fx":function(){
define(["./_base/lang","./Evented","./_base/kernel","./_base/array","./aspect","./_base/fx","./dom","./dom-style","./dom-geometry","./ready","require"],function(lang,_3b2,dojo,_3b3,_3b4,_3b5,dom,_3b6,geom,_3b7,_3b8){
if(!dojo.isAsync){
_3b7(0,function(){
var _3b9=["./fx/Toggler"];
_3b8(_3b9);
});
}
var _3ba=dojo.fx={};
var _3bb={_fire:function(evt,args){
if(this[evt]){
this[evt].apply(this,args||[]);
}
return this;
}};
var _3bc=function(_3bd){
this._index=-1;
this._animations=_3bd||[];
this._current=this._onAnimateCtx=this._onEndCtx=null;
this.duration=0;
_3b3.forEach(this._animations,function(a){
if(a){
if(typeof a.duration!="undefined"){
this.duration+=a.duration;
}
if(a.delay){
this.duration+=a.delay;
}
}
},this);
};
_3bc.prototype=new _3b2();
lang.extend(_3bc,{_onAnimate:function(){
this._fire("onAnimate",arguments);
},_onEnd:function(){
this._onAnimateCtx.remove();
this._onEndCtx.remove();
this._onAnimateCtx=this._onEndCtx=null;
if(this._index+1==this._animations.length){
this._fire("onEnd");
}else{
this._current=this._animations[++this._index];
this._onAnimateCtx=_3b4.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
this._onEndCtx=_3b4.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play(0,true);
}
},play:function(_3be,_3bf){
if(!this._current){
this._current=this._animations[this._index=0];
}
if(!_3bf&&this._current.status()=="playing"){
return this;
}
var _3c0=_3b4.after(this._current,"beforeBegin",lang.hitch(this,function(){
this._fire("beforeBegin");
}),true),_3c1=_3b4.after(this._current,"onBegin",lang.hitch(this,function(arg){
this._fire("onBegin",arguments);
}),true),_3c2=_3b4.after(this._current,"onPlay",lang.hitch(this,function(arg){
this._fire("onPlay",arguments);
_3c0.remove();
_3c1.remove();
_3c2.remove();
}));
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
this._onAnimateCtx=_3b4.after(this._current,"onAnimate",lang.hitch(this,"_onAnimate"),true);
if(this._onEndCtx){
this._onEndCtx.remove();
}
this._onEndCtx=_3b4.after(this._current,"onEnd",lang.hitch(this,"_onEnd"),true);
this._current.play.apply(this._current,arguments);
return this;
},pause:function(){
if(this._current){
var e=_3b4.after(this._current,"onPause",lang.hitch(this,function(arg){
this._fire("onPause",arguments);
e.remove();
}),true);
this._current.pause();
}
return this;
},gotoPercent:function(_3c3,_3c4){
this.pause();
var _3c5=this.duration*_3c3;
this._current=null;
_3b3.some(this._animations,function(a,_3c6){
if(_3c5<=a.duration){
this._current=a;
this._index=_3c6;
return true;
}
_3c5-=a.duration;
return false;
},this);
if(this._current){
this._current.gotoPercent(_3c5/this._current.duration);
}
if(_3c4){
this.play();
}
return this;
},stop:function(_3c7){
if(this._current){
if(_3c7){
for(;this._index+1<this._animations.length;++this._index){
this._animations[this._index].stop(true);
}
this._current=this._animations[this._index];
}
var e=_3b4.after(this._current,"onStop",lang.hitch(this,function(arg){
this._fire("onStop",arguments);
e.remove();
}),true);
this._current.stop();
}
return this;
},status:function(){
return this._current?this._current.status():"stopped";
},destroy:function(){
this.stop();
if(this._onAnimateCtx){
this._onAnimateCtx.remove();
}
if(this._onEndCtx){
this._onEndCtx.remove();
}
}});
lang.extend(_3bc,_3bb);
_3ba.chain=function(_3c8){
return new _3bc(_3c8);
};
var _3c9=function(_3ca){
this._animations=_3ca||[];
this._connects=[];
this._finished=0;
this.duration=0;
_3b3.forEach(_3ca,function(a){
var _3cb=a.duration;
if(a.delay){
_3cb+=a.delay;
}
if(this.duration<_3cb){
this.duration=_3cb;
}
this._connects.push(_3b4.after(a,"onEnd",lang.hitch(this,"_onEnd"),true));
},this);
this._pseudoAnimation=new _3b5.Animation({curve:[0,1],duration:this.duration});
var self=this;
_3b3.forEach(["beforeBegin","onBegin","onPlay","onAnimate","onPause","onStop","onEnd"],function(evt){
self._connects.push(_3b4.after(self._pseudoAnimation,evt,function(){
self._fire(evt,arguments);
},true));
});
};
lang.extend(_3c9,{_doAction:function(_3cc,args){
_3b3.forEach(this._animations,function(a){
a[_3cc].apply(a,args);
});
return this;
},_onEnd:function(){
if(++this._finished>this._animations.length){
this._fire("onEnd");
}
},_call:function(_3cd,args){
var t=this._pseudoAnimation;
t[_3cd].apply(t,args);
},play:function(_3ce,_3cf){
this._finished=0;
this._doAction("play",arguments);
this._call("play",arguments);
return this;
},pause:function(){
this._doAction("pause",arguments);
this._call("pause",arguments);
return this;
},gotoPercent:function(_3d0,_3d1){
var ms=this.duration*_3d0;
_3b3.forEach(this._animations,function(a){
a.gotoPercent(a.duration<ms?1:(ms/a.duration),_3d1);
});
this._call("gotoPercent",arguments);
return this;
},stop:function(_3d2){
this._doAction("stop",arguments);
this._call("stop",arguments);
return this;
},status:function(){
return this._pseudoAnimation.status();
},destroy:function(){
this.stop();
_3b3.forEach(this._connects,function(_3d3){
_3d3.remove();
});
}});
lang.extend(_3c9,_3bb);
_3ba.combine=function(_3d4){
return new _3c9(_3d4);
};
_3ba.wipeIn=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_3b5.animateProperty(lang.mixin({properties:{height:{start:function(){
o=s.overflow;
s.overflow="hidden";
if(s.visibility=="hidden"||s.display=="none"){
s.height="1px";
s.display="";
s.visibility="";
return 1;
}else{
var _3d5=_3b6.get(node,"height");
return Math.max(_3d5,1);
}
},end:function(){
return node.scrollHeight;
}}}},args));
var fini=function(){
s.height="auto";
s.overflow=o;
};
_3b4.after(anim,"onStop",fini,true);
_3b4.after(anim,"onEnd",fini,true);
return anim;
};
_3ba.wipeOut=function(args){
var node=args.node=dom.byId(args.node),s=node.style,o;
var anim=_3b5.animateProperty(lang.mixin({properties:{height:{end:1}}},args));
_3b4.after(anim,"beforeBegin",function(){
o=s.overflow;
s.overflow="hidden";
s.display="";
},true);
var fini=function(){
s.overflow=o;
s.height="auto";
s.display="none";
};
_3b4.after(anim,"onStop",fini,true);
_3b4.after(anim,"onEnd",fini,true);
return anim;
};
_3ba.slideTo=function(args){
var node=args.node=dom.byId(args.node),top=null,left=null;
var init=(function(n){
return function(){
var cs=_3b6.getComputedStyle(n);
var pos=cs.position;
top=(pos=="absolute"?n.offsetTop:parseInt(cs.top)||0);
left=(pos=="absolute"?n.offsetLeft:parseInt(cs.left)||0);
if(pos!="absolute"&&pos!="relative"){
var ret=geom.position(n,true);
top=ret.y;
left=ret.x;
n.style.position="absolute";
n.style.top=top+"px";
n.style.left=left+"px";
}
};
})(node);
init();
var anim=_3b5.animateProperty(lang.mixin({properties:{top:args.top||0,left:args.left||0}},args));
_3b4.after(anim,"beforeBegin",init,true);
return anim;
};
return _3ba;
});
},"dijit/_base/typematic":function(){
define(["../typematic"],function(){
});
},"dijit/_base/popup":function(){
define(["dojo/dom-class","dojo/_base/window","../popup","../BackgroundIframe"],function(_3d6,win,_3d7){
var _3d8=_3d7._createWrapper;
_3d7._createWrapper=function(_3d9){
if(!_3d9.declaredClass){
_3d9={_popupWrapper:(_3d9.parentNode&&_3d6.contains(_3d9.parentNode,"dijitPopup"))?_3d9.parentNode:null,domNode:_3d9,destroy:function(){
},ownerDocument:_3d9.ownerDocument,ownerDocumentBody:win.body(_3d9.ownerDocument)};
}
return _3d8.call(this,_3d9);
};
var _3da=_3d7.open;
_3d7.open=function(args){
if(args.orient&&typeof args.orient!="string"&&!("length" in args.orient)){
var ary=[];
for(var key in args.orient){
ary.push({aroundCorner:key,corner:args.orient[key]});
}
args.orient=ary;
}
return _3da.call(this,args);
};
return _3d7;
});
},"dijit/_Container":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","dojo/_base/kernel"],function(_3db,_3dc,_3dd,_3de){
return _3dc("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_3df,_3e0){
var _3e1=this.containerNode;
if(_3e0>0){
_3e1=_3e1.firstChild;
while(_3e0>0){
if(_3e1.nodeType==1){
_3e0--;
}
_3e1=_3e1.nextSibling;
}
if(_3e1){
_3e0="before";
}else{
_3e1=this.containerNode;
_3e0="last";
}
}
_3dd.place(_3df.domNode,_3e1,_3e0);
if(this._started&&!_3df._started){
_3df.startup();
}
},removeChild:function(_3e2){
if(typeof _3e2=="number"){
_3e2=this.getChildren()[_3e2];
}
if(_3e2){
var node=_3e2.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_3e3,dir){
var _3e4=this.getChildren(),idx=_3db.indexOf(_3e4,_3e3);
return _3e4[idx+dir];
},getIndexOfChild:function(_3e5){
return _3db.indexOf(this.getChildren(),_3e5);
}});
});
}}});
define("dojo/dojo.layer",[],1);
