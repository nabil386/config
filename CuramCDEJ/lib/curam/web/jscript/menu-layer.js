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
},"dijit/Viewport":function(){
define(["dojo/Evented","dojo/on","dojo/domReady","dojo/sniff","dojo/window"],function(_6e,on,_6f,has,_70){
var _71=new _6e();
var _72;
_6f(function(){
var _73=_70.getBox();
_71._rlh=on(window,"resize",function(){
var _74=_70.getBox();
if(_73.h==_74.h&&_73.w==_74.w){
return;
}
_73=_74;
_71.emit("resize");
});
if(has("ie")==8){
var _75=screen.deviceXDPI;
setInterval(function(){
if(screen.deviceXDPI!=_75){
_75=screen.deviceXDPI;
_71.emit("resize");
}
},500);
}
if(has("ios")){
on(document,"focusin",function(evt){
_72=evt.target;
});
on(document,"focusout",function(evt){
_72=null;
});
}
});
_71.getEffectiveBox=function(doc){
var box=_70.getBox(doc);
var tag=_72&&_72.tagName&&_72.tagName.toLowerCase();
if(has("ios")&&_72&&!_72.readOnly&&(tag=="textarea"||(tag=="input"&&/^(color|email|number|password|search|tel|text|url)$/.test(_72.type)))){
box.h*=(orientation==0||orientation==180?0.66:0.4);
var _76=_72.getBoundingClientRect();
box.h=Math.max(box.h,_76.top+_76.height);
}
return box;
};
return _71;
});
},"dijit/CheckedMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-class","./MenuItem","dojo/text!./templates/CheckedMenuItem.html","./hccss"],function(_77,_78,_79,_7a){
return _77("dijit.CheckedMenuItem",_79,{baseClass:"dijitMenuItem dijitCheckedMenuItem",templateString:_7a,checked:false,_setCheckedAttr:function(_7b){
this.domNode.setAttribute("aria-checked",_7b?"true":"false");
this._set("checked",_7b);
},iconClass:"",role:"menuitemcheckbox",checkedChar:"&#10003;",onChange:function(){
},_onClick:function(evt){
if(!this.disabled){
this.set("checked",!this.checked);
this.onChange(this.checked);
}
this.onClick(evt);
}});
});
},"dijit/Menu":function(){
define(["require","dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-geometry","dojo/dom-style","dojo/keys","dojo/_base/lang","dojo/on","dojo/sniff","dojo/_base/window","dojo/window","./popup","./DropDownMenu","dojo/ready"],function(_7c,_7d,_7e,dom,_7f,_80,_81,_82,_83,on,has,win,_84,pm,_85,_86){
if(1){
_86(0,function(){
var _87=["dijit/MenuItem","dijit/PopupMenuItem","dijit/CheckedMenuItem","dijit/MenuSeparator"];
_7c(_87);
});
}
return _7e("dijit.Menu",_85,{constructor:function(){
this._bindings=[];
},targetNodeIds:[],selector:"",contextMenuForWindow:false,leftClickToOpen:false,refocus:true,postCreate:function(){
if(this.contextMenuForWindow){
this.bindDomNode(this.ownerDocumentBody);
}else{
_7d.forEach(this.targetNodeIds,this.bindDomNode,this);
}
this.inherited(arguments);
},_iframeContentWindow:function(_88){
return _84.get(this._iframeContentDocument(_88))||this._iframeContentDocument(_88)["__parent__"]||(_88.name&&document.frames[_88.name])||null;
},_iframeContentDocument:function(_89){
return _89.contentDocument||(_89.contentWindow&&_89.contentWindow.document)||(_89.name&&document.frames[_89.name]&&document.frames[_89.name].document)||null;
},bindDomNode:function(_8a){
_8a=dom.byId(_8a,this.ownerDocument);
var cn;
if(_8a.tagName.toLowerCase()=="iframe"){
var _8b=_8a,_8c=this._iframeContentWindow(_8b);
cn=win.body(_8c.document);
}else{
cn=(_8a==win.body(this.ownerDocument)?this.ownerDocument.documentElement:_8a);
}
var _8d={node:_8a,iframe:_8b};
_7f.set(_8a,"_dijitMenu"+this.id,this._bindings.push(_8d));
var _8e=_83.hitch(this,function(cn){
var _8f=this.selector,_90=_8f?function(_91){
return on.selector(_8f,_91);
}:function(_92){
return _92;
},_93=this;
return [on(cn,_90(this.leftClickToOpen?"click":"contextmenu"),function(evt){
evt.stopPropagation();
evt.preventDefault();
if((new Date()).getTime()<_93._lastKeyDown+500){
return;
}
_93._scheduleOpen(this,_8b,{x:evt.pageX,y:evt.pageY},evt.target);
}),on(cn,_90("keydown"),function(evt){
if(evt.keyCode==93||(evt.shiftKey&&evt.keyCode==_82.F10)||(_93.leftClickToOpen&&evt.keyCode==_82.SPACE)){
evt.stopPropagation();
evt.preventDefault();
_93._scheduleOpen(this,_8b,null,evt.target);
_93._lastKeyDown=(new Date()).getTime();
}
})];
});
_8d.connects=cn?_8e(cn):[];
if(_8b){
_8d.onloadHandler=_83.hitch(this,function(){
var _94=this._iframeContentWindow(_8b),cn=win.body(_94.document);
_8d.connects=_8e(cn);
});
if(_8b.addEventListener){
_8b.addEventListener("load",_8d.onloadHandler,false);
}else{
_8b.attachEvent("onload",_8d.onloadHandler);
}
}
},unBindDomNode:function(_95){
var _96;
try{
_96=dom.byId(_95,this.ownerDocument);
}
catch(e){
return;
}
var _97="_dijitMenu"+this.id;
if(_96&&_7f.has(_96,_97)){
var bid=_7f.get(_96,_97)-1,b=this._bindings[bid],h;
while((h=b.connects.pop())){
h.remove();
}
var _98=b.iframe;
if(_98){
if(_98.removeEventListener){
_98.removeEventListener("load",b.onloadHandler,false);
}else{
_98.detachEvent("onload",b.onloadHandler);
}
}
_7f.remove(_96,_97);
delete this._bindings[bid];
}
},_scheduleOpen:function(_99,_9a,_9b,_9c){
if(!this._openTimer){
this._openTimer=this.defer(function(){
delete this._openTimer;
this._openMyself({target:_9c,delegatedTarget:_99,iframe:_9a,coords:_9b});
},1);
}
},_openMyself:function(_9d){
var _9e=_9d.target,_9f=_9d.iframe,_a0=_9d.coords,_a1=!_a0;
this.currentTarget=_9d.delegatedTarget;
if(_a0){
if(_9f){
var ifc=_80.position(_9f,true),_a2=this._iframeContentWindow(_9f),_a3=_80.docScroll(_a2.document);
var cs=_81.getComputedStyle(_9f),tp=_81.toPixelValue,_a4=(has("ie")&&has("quirks")?0:tp(_9f,cs.paddingLeft))+(has("ie")&&has("quirks")?tp(_9f,cs.borderLeftWidth):0),top=(has("ie")&&has("quirks")?0:tp(_9f,cs.paddingTop))+(has("ie")&&has("quirks")?tp(_9f,cs.borderTopWidth):0);
_a0.x+=ifc.x+_a4-_a3.x;
_a0.y+=ifc.y+top-_a3.y;
}
}else{
_a0=_80.position(_9e,true);
_a0.x+=10;
_a0.y+=10;
}
var _a5=this;
var _a6=this._focusManager.get("prevNode");
var _a7=this._focusManager.get("curNode");
var _a8=!_a7||(dom.isDescendant(_a7,this.domNode))?_a6:_a7;
function _a9(){
if(_a5.refocus&&_a8){
_a8.focus();
}
pm.close(_a5);
};
pm.open({popup:this,x:_a0.x,y:_a0.y,onExecute:_a9,onCancel:_a9,orient:this.isLeftToRight()?"L":"R"});
this.focus();
if(!_a1){
this.defer(function(){
this._cleanUp(true);
});
}
this._onBlur=function(){
this.inherited("_onBlur",arguments);
pm.close(this);
};
},destroy:function(){
_7d.forEach(this._bindings,function(b){
if(b){
this.unBindDomNode(b.node);
}
},this);
this.inherited(arguments);
}});
});
},"dojo/hccss":function(){
define(["require","./_base/config","./dom-class","./dom-style","./has","./domReady","./_base/window"],function(_aa,_ab,_ac,_ad,has,_ae,win){
has.add("highcontrast",function(){
var div=win.doc.createElement("div");
div.style.cssText="border: 1px solid; border-color:red green; position: absolute; height: 5px; top: -999px;"+"background-image: url(\""+(_ab.blankGif||_aa.toUrl("./resources/blank.gif"))+"\");";
win.body().appendChild(div);
var cs=_ad.getComputedStyle(div),_af=cs.backgroundImage,hc=(cs.borderTopColor==cs.borderRightColor)||(_af&&(_af=="none"||_af=="url(invalid-url:)"));
if(has("ie")<=8){
div.outerHTML="";
}else{
win.body().removeChild(div);
}
return hc;
});
_ae(function(){
if(has("highcontrast")){
_ac.add(win.body(),"dj_a11y");
}
});
return has;
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_b0,_b1,_b2,_b3,_b4,_b5,dom,_b6,_b7,_b8,_b9,_ba,has,_bb,_bc,on,_bd,_be,_bf,win,_c0,_c1,_c2){
var _c3=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_bb.isAsync);
has.add("dojo-bidi",false);
if(1){
_bd(0,function(){
var _c4=["dijit/_base/manager"];
_b0(_c4);
});
}
var _c5={};
function _c6(obj){
var ret={};
for(var _c7 in obj){
ret[_c7.toLowerCase()]=true;
}
return ret;
};
function _c8(_c9){
return function(val){
_b6[val?"set":"remove"](this.domNode,_c9,val);
this._set(_c9,val);
};
};
function _ca(a,b){
return a===b||(a!==a&&b!==b);
};
var _cb=_b5("dijit._WidgetBase",[_be,_c0],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_c8("lang"),dir:"",_setDirAttr:_c8("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_b3.blankGif||_b0.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var _cc=this.constructor;
if(!_cc._setterAttrs){
var _cd=_cc.prototype,_ce=_cc._setterAttrs=[],_cf=(_cc._onMap={});
for(var _d0 in _cd.attributeMap){
_ce.push(_d0);
}
for(_d0 in _cd){
if(/^on/.test(_d0)){
_cf[_d0.substring(2).toLowerCase()]=_d0;
}
if(/^_set[A-Z](.*)Attr$/.test(_d0)){
_d0=_d0.charAt(4).toLowerCase()+_d0.substr(5,_d0.length-9);
if(!_cd.attributeMap||!(_d0 in _cd.attributeMap)){
_ce.push(_d0);
}
}
}
}
},postscript:function(_d1,_d2){
this.create(_d1,_d2);
},create:function(_d3,_d4){
if(_c3){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_d4);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_d3){
this.params=_d3;
_bc.mixin(this,_d3);
}
this.postMixInProperties();
if(!this.id){
this.id=_c2.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_c2.add(this);
this.buildRendering();
var _d5;
if(this.domNode){
this._applyAttributes();
var _d6=this.srcNodeRef;
if(_d6&&_d6.parentNode&&this.domNode!==_d6){
_d6.parentNode.replaceChild(this.domNode,_d6);
_d5=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_d5){
delete this.srcNodeRef;
}
this._created=true;
if(_c3){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _d7={};
for(var key in this.params||{}){
_d7[key]=this._get(key);
}
_b1.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _d7)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _d7){
this.set(key,_d7[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _d8=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_d8=_d8.concat(_b1.map(_d8,function(_d9){
return _d9+"Rtl";
}));
}
_b7.add(this.domNode,_d8);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_b1.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&_bc.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_da){
this._beingDestroyed=true;
this.destroyDescendants(_da);
this.destroy(_da);
},destroy:function(_db){
this._beingDestroyed=true;
this.uninitialize();
function _dc(w){
if(w.destroyRecursive){
w.destroyRecursive(_db);
}else{
if(w.destroy){
w.destroy(_db);
}
}
};
_b1.forEach(this._connects,_bc.hitch(this,"disconnect"));
_b1.forEach(this._supportingWidgets,_dc);
if(this.domNode){
_b1.forEach(_c2.findWidgets(this.domNode,this.containerNode),_dc);
}
this.destroyRendering(_db);
_c2.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_dd){
if(this.bgIframe){
this.bgIframe.destroy(_dd);
delete this.bgIframe;
}
if(this.domNode){
if(_dd){
_b6.remove(this.domNode,"widgetId");
}else{
_b8.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_dd){
_b8.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_de){
_b1.forEach(this.getChildren(),function(_df){
if(_df.destroyRecursive){
_df.destroyRecursive(_de);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_e0){
var _e1=this.domNode;
if(_bc.isObject(_e0)){
_ba.set(_e1,_e0);
}else{
if(_e1.style.cssText){
_e1.style.cssText+="; "+_e0;
}else{
_e1.style.cssText=_e0;
}
}
this._set("style",_e0);
},_attrToDom:function(_e2,_e3,_e4){
_e4=arguments.length>=3?_e4:this.attributeMap[_e2];
_b1.forEach(_bc.isArray(_e4)?_e4:[_e4],function(_e5){
var _e6=this[_e5.node||_e5||"domNode"];
var _e7=_e5.type||"attribute";
switch(_e7){
case "attribute":
if(_bc.isFunction(_e3)){
_e3=_bc.hitch(this,_e3);
}
var _e8=_e5.attribute?_e5.attribute:(/^on[A-Z][a-zA-Z]*$/.test(_e2)?_e2.toLowerCase():_e2);
if(_e6.tagName){
_b6.set(_e6,_e8,_e3);
}else{
_e6.set(_e8,_e3);
}
break;
case "innerText":
_e6.innerHTML="";
_e6.appendChild(this.ownerDocument.createTextNode(_e3));
break;
case "innerHTML":
_e6.innerHTML=_e3;
break;
case "class":
_b7.replace(_e6,_e3,this[_e2]);
break;
}
},this);
},get:function(_e9){
var _ea=this._getAttrNames(_e9);
return this[_ea.g]?this[_ea.g]():this._get(_e9);
},set:function(_eb,_ec){
if(typeof _eb==="object"){
for(var x in _eb){
this.set(x,_eb[x]);
}
return this;
}
var _ed=this._getAttrNames(_eb),_ee=this[_ed.s];
if(_bc.isFunction(_ee)){
var _ef=_ee.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _f0=this.focusNode&&!_bc.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_f0]&&this[_f0].tagName,_f1=tag&&(_c5[tag]||(_c5[tag]=_c6(this[_f0]))),map=_eb in this.attributeMap?this.attributeMap[_eb]:_ed.s in this?this[_ed.s]:((_f1&&_ed.l in _f1&&typeof _ec!="function")||/^aria-|^data-|^role$/.test(_eb))?_f0:null;
if(map!=null){
this._attrToDom(_eb,_ec,map);
}
this._set(_eb,_ec);
}
return _ef||this;
},_attrPairNames:{},_getAttrNames:function(_f2){
var apn=this._attrPairNames;
if(apn[_f2]){
return apn[_f2];
}
var uc=_f2.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[_f2]={n:_f2+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(_f3,_f4){
var _f5=this[_f3];
this[_f3]=_f4;
if(this._created&&!_ca(_f5,_f4)){
if(this._watchCallbacks){
this._watchCallbacks(_f3,_f5,_f4);
}
this.emit("attrmodified-"+_f3,{detail:{prevValue:_f5,newValue:_f4}});
}
},_get:function(_f6){
return this[_f6];
},emit:function(_f7,_f8,_f9){
_f8=_f8||{};
if(_f8.bubbles===undefined){
_f8.bubbles=true;
}
if(_f8.cancelable===undefined){
_f8.cancelable=true;
}
if(!_f8.detail){
_f8.detail={};
}
_f8.detail.widget=this;
var ret,_fa=this["on"+_f7];
if(_fa){
ret=_fa.apply(this,_f9?_f9:[_f8]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,_f7.toLowerCase(),_f8);
}
return ret;
},on:function(_fb,_fc){
var _fd=this._onMap(_fb);
if(_fd){
return _b2.after(this,_fd,_fc,true);
}
return this.own(on(this.domNode,_fb,_fc))[0];
},_onMap:function(_fe){
var _ff=this.constructor,map=_ff._onMap;
if(!map){
map=(_ff._onMap={});
for(var attr in _ff.prototype){
if(/^on/.test(attr)){
map[attr.replace(/^on/,"").toLowerCase()]=attr;
}
}
}
return map[typeof _fe=="string"&&_fe.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_c2.findWidgets(this.containerNode):[];
},getParent:function(){
return _c2.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_100,_101){
return this.own(_b4.connect(obj,_100,this,_101))[0];
},disconnect:function(_102){
_102.remove();
},subscribe:function(t,_103){
return this.own(_bf.subscribe(t,_bc.hitch(this,_103)))[0];
},unsubscribe:function(_104){
_104.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_b9.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_ba.get(this.domNode,"display")!="none");
},placeAt:function(_105,_106){
var _107=!_105.tagName&&_c2.byId(_105);
if(_107&&_107.addChild&&(!_106||typeof _106==="number")){
_107.addChild(this,_106);
}else{
var ref=_107&&("domNode" in _107)?(_107.containerNode&&!/after|before|replace/.test(_106||"")?_107.containerNode:_107.domNode):dom.byId(_105,this.ownerDocument);
_b8.place(this.domNode,ref,_106);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,_108){
var _109=setTimeout(_bc.hitch(this,function(){
if(!_109){
return;
}
_109=null;
if(!this._destroyed){
_bc.hitch(this,fcn)();
}
}),_108||0);
return {remove:function(){
if(_109){
clearTimeout(_109);
_109=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_cb.extend(_c1);
}
return _cb;
});
},"dijit/_MenuBase":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/lang","dojo/mouse","dojo/on","dojo/window","./a11yclick","./registry","./_Widget","./_CssStateMixin","./_KeyNavContainer","./_TemplatedMixin"],function(_10a,_10b,dom,_10c,_10d,lang,_10e,on,_10f,_110,_111,_112,_113,_114,_115){
return _10b("dijit._MenuBase",[_112,_115,_114,_113],{selected:null,_setSelectedAttr:function(item){
if(this.selected!=item){
if(this.selected){
this.selected._setSelected(false);
this._onChildDeselect(this.selected);
}
if(item){
item._setSelected(true);
}
this._set("selected",item);
}
},activated:false,_setActivatedAttr:function(val){
_10d.toggle(this.domNode,"dijitMenuActive",val);
_10d.toggle(this.domNode,"dijitMenuPassive",!val);
this._set("activated",val);
},parentMenu:null,popupDelay:500,passivePopupDelay:Infinity,autoFocus:false,childSelector:function(node){
var _116=_111.byNode(node);
return node.parentNode==this.containerNode&&_116&&_116.focus;
},postCreate:function(){
var self=this,_117=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.containerNode,on.selector(_117,_10e.enter),function(){
self.onItemHover(_111.byNode(this));
}),on(this.containerNode,on.selector(_117,_10e.leave),function(){
self.onItemUnhover(_111.byNode(this));
}),on(this.containerNode,on.selector(_117,_110),function(evt){
self.onItemClick(_111.byNode(this),evt);
evt.stopPropagation();
}),on(this.containerNode,on.selector(_117,"focusin"),function(){
self._onItemFocus(_111.byNode(this));
}));
this.inherited(arguments);
},onKeyboardSearch:function(item,evt,_118,_119){
this.inherited(arguments);
if(!!item&&(_119==-1||(!!item.popup&&_119==1))){
this.onItemClick(item,evt);
}
},_keyboardSearchCompare:function(item,_11a){
if(!!item.shortcutKey){
return _11a==item.shortcutKey.toLowerCase()?-1:0;
}
return this.inherited(arguments)?1:0;
},onExecute:function(){
},onCancel:function(){
},_moveToPopup:function(evt){
if(this.focusedChild&&this.focusedChild.popup&&!this.focusedChild.disabled){
this.onItemClick(this.focusedChild,evt);
}else{
var _11b=this._getTopMenu();
if(_11b&&_11b._isMenuBar){
_11b.focusNext();
}
}
},_onPopupHover:function(){
this.set("selected",this.currentPopupItem);
this._stopPendingCloseTimer();
},onItemHover:function(item){
if(this.activated){
this.set("selected",item);
if(item.popup&&!item.disabled&&!this.hover_timer){
this.hover_timer=this.defer(function(){
this._openItemPopup(item);
},this.popupDelay);
}
}else{
if(this.passivePopupDelay<Infinity){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.passive_hover_timer=this.defer(function(){
this.onItemClick(item,{type:"click"});
},this.passivePopupDelay);
}
}
this._hoveredChild=item;
item._set("hovering",true);
},_onChildDeselect:function(item){
this._stopPopupTimer();
if(this.currentPopupItem==item){
this._stopPendingCloseTimer();
this._pendingClose_timer=this.defer(function(){
this._pendingClose_timer=null;
this.currentPopupItem=null;
item._closePopup();
},this.popupDelay);
}
},onItemUnhover:function(item){
if(this._hoveredChild==item){
this._hoveredChild=null;
}
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
this.passive_hover_timer=null;
}
item._set("hovering",false);
},_stopPopupTimer:function(){
if(this.hover_timer){
this.hover_timer=this.hover_timer.remove();
}
},_stopPendingCloseTimer:function(){
if(this._pendingClose_timer){
this._pendingClose_timer=this._pendingClose_timer.remove();
}
},_getTopMenu:function(){
for(var top=this;top.parentMenu;top=top.parentMenu){
}
return top;
},onItemClick:function(item,evt){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.focusChild(item);
if(item.disabled){
return false;
}
if(item.popup){
this.set("selected",item);
this.set("activated",true);
var _11c=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
this._openItemPopup(item,_11c);
}else{
this.onExecute();
item._onClick?item._onClick(evt):item.onClick(evt);
}
},_openItemPopup:function(_11d,_11e){
if(_11d==this.currentPopupItem){
return;
}
if(this.currentPopupItem){
this._stopPendingCloseTimer();
this.currentPopupItem._closePopup();
}
this._stopPopupTimer();
var _11f=_11d.popup;
_11f.parentMenu=this;
this.own(this._mouseoverHandle=on.once(_11f.domNode,"mouseover",lang.hitch(this,"_onPopupHover")));
var self=this;
_11d._openPopup({parent:this,orient:this._orient||["after","before"],onCancel:function(){
if(_11e){
self.focusChild(_11d);
}
self._cleanUp();
},onExecute:lang.hitch(this,"_cleanUp",true),onClose:function(){
if(self._mouseoverHandle){
self._mouseoverHandle.remove();
delete self._mouseoverHandle;
}
}},_11e);
this.currentPopupItem=_11d;
},onOpen:function(){
this.isShowingNow=true;
this.set("activated",true);
},onClose:function(){
this.set("activated",false);
this.set("selected",null);
this.isShowingNow=false;
this.parentMenu=null;
},_closeChild:function(){
this._stopPopupTimer();
if(this.currentPopupItem){
if(this.focused){
_10c.set(this.selected.focusNode,"tabIndex",this.tabIndex);
this.selected.focusNode.focus();
}
this.currentPopupItem._closePopup();
this.currentPopupItem=null;
}
},_onItemFocus:function(item){
if(this._hoveredChild&&this._hoveredChild!=item){
this.onItemUnhover(this._hoveredChild);
}
this.set("selected",item);
},_onBlur:function(){
this._cleanUp(true);
this.inherited(arguments);
},_cleanUp:function(_120){
this._closeChild();
if(typeof this.isShowingNow=="undefined"){
this.set("activated",false);
}
if(_120){
this.set("selected",null);
}
}});
});
},"dojo/touch":function(){
define(["./_base/kernel","./aspect","./dom","./dom-class","./_base/lang","./on","./has","./mouse","./domReady","./_base/window"],function(dojo,_121,dom,_122,lang,on,has,_123,_124,win){
var ios4=has("ios")<5;
var _125=has("pointer-events")||has("MSPointer"),_126=(function(){
var _127={};
for(var type in {down:1,move:1,up:1,cancel:1,over:1,out:1}){
_127[type]=has("MSPointer")?"MSPointer"+type.charAt(0).toUpperCase()+type.slice(1):"pointer"+type;
}
return _127;
})();
var _128=has("touch-events");
var _129,_12a,_12b=false,_12c,_12d,_12e,_12f,_130,_131;
var _132;
function _133(_134,_135,_136){
if(_125&&_136){
return function(node,_137){
return on(node,_136,_137);
};
}else{
if(_128){
return function(node,_138){
var _139=on(node,_135,function(evt){
_138.call(this,evt);
_132=(new Date()).getTime();
}),_13a=on(node,_134,function(evt){
if(!_132||(new Date()).getTime()>_132+1000){
_138.call(this,evt);
}
});
return {remove:function(){
_139.remove();
_13a.remove();
}};
};
}else{
return function(node,_13b){
return on(node,_134,_13b);
};
}
}
};
function _13c(node){
do{
if(node.dojoClick!==undefined){
return node;
}
}while(node=node.parentNode);
};
function _13d(e,_13e,_13f){
if(_123.isRight(e)){
return;
}
var _140=_13c(e.target);
_12a=!e.target.disabled&&_140&&_140.dojoClick;
if(_12a){
_12b=(_12a=="useTarget");
_12c=(_12b?_140:e.target);
if(_12b){
e.preventDefault();
}
_12d=e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX;
_12e=e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY;
_12f=(typeof _12a=="object"?_12a.x:(typeof _12a=="number"?_12a:0))||4;
_130=(typeof _12a=="object"?_12a.y:(typeof _12a=="number"?_12a:0))||4;
if(!_129){
_129=true;
function _141(e){
if(_12b){
_12a=dom.isDescendant(win.doc.elementFromPoint((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX),(e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)),_12c);
}else{
_12a=_12a&&(e.changedTouches?e.changedTouches[0].target:e.target)==_12c&&Math.abs((e.changedTouches?e.changedTouches[0].pageX-win.global.pageXOffset:e.clientX)-_12d)<=_12f&&Math.abs((e.changedTouches?e.changedTouches[0].pageY-win.global.pageYOffset:e.clientY)-_12e)<=_130;
}
};
win.doc.addEventListener(_13e,function(e){
if(_123.isRight(e)){
return;
}
_141(e);
if(_12b){
e.preventDefault();
}
},true);
win.doc.addEventListener(_13f,function(e){
if(_123.isRight(e)){
return;
}
_141(e);
if(_12a){
_131=(new Date()).getTime();
var _142=(_12b?_12c:e.target);
if(_142.tagName==="LABEL"){
_142=dom.byId(_142.getAttribute("for"))||_142;
}
var src=(e.changedTouches)?e.changedTouches[0]:e;
function _143(type){
var evt=document.createEvent("MouseEvents");
evt._dojo_click=true;
evt.initMouseEvent(type,true,true,e.view,e.detail,src.screenX,src.screenY,src.clientX,src.clientY,e.ctrlKey,e.altKey,e.shiftKey,e.metaKey,0,null);
return evt;
};
var _144=_143("mousedown");
var _145=_143("mouseup");
var _146=_143("click");
setTimeout(function(){
on.emit(_142,"mousedown",_144);
on.emit(_142,"mouseup",_145);
on.emit(_142,"click",_146);
_131=(new Date()).getTime();
},0);
}
},true);
function _147(type){
win.doc.addEventListener(type,function(e){
if(_12a&&!e._dojo_click&&(new Date()).getTime()<=_131+1000&&!(e.target.tagName=="INPUT"&&_122.contains(e.target,"dijitOffScreen"))){
e.stopPropagation();
e.stopImmediatePropagation&&e.stopImmediatePropagation();
if(type=="click"&&(e.target.tagName!="INPUT"||e.target.type=="radio"||e.target.type=="checkbox")&&e.target.tagName!="TEXTAREA"&&e.target.tagName!="AUDIO"&&e.target.tagName!="VIDEO"){
e.preventDefault();
}
}
},true);
};
_147("click");
_147("mousedown");
_147("mouseup");
}
}
};
var _148;
if(has("touch")){
if(_125){
_124(function(){
win.doc.addEventListener(_126.down,function(evt){
_13d(evt,_126.move,_126.up);
},true);
});
}else{
_124(function(){
_148=win.body();
win.doc.addEventListener("touchstart",function(evt){
_132=(new Date()).getTime();
var _149=_148;
_148=evt.target;
on.emit(_149,"dojotouchout",{relatedTarget:_148,bubbles:true});
on.emit(_148,"dojotouchover",{relatedTarget:_149,bubbles:true});
_13d(evt,"touchmove","touchend");
},true);
function _14a(evt){
var _14b=lang.delegate(evt,{bubbles:true});
if(has("ios")>=6){
_14b.touches=evt.touches;
_14b.altKey=evt.altKey;
_14b.changedTouches=evt.changedTouches;
_14b.ctrlKey=evt.ctrlKey;
_14b.metaKey=evt.metaKey;
_14b.shiftKey=evt.shiftKey;
_14b.targetTouches=evt.targetTouches;
}
return _14b;
};
on(win.doc,"touchmove",function(evt){
_132=(new Date()).getTime();
var _14c=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset));
if(_14c){
if(_148!==_14c){
on.emit(_148,"dojotouchout",{relatedTarget:_14c,bubbles:true});
on.emit(_14c,"dojotouchover",{relatedTarget:_148,bubbles:true});
_148=_14c;
}
if(!on.emit(_14c,"dojotouchmove",_14a(evt))){
evt.preventDefault();
}
}
});
on(win.doc,"touchend",function(evt){
_132=(new Date()).getTime();
var node=win.doc.elementFromPoint(evt.pageX-(ios4?0:win.global.pageXOffset),evt.pageY-(ios4?0:win.global.pageYOffset))||win.body();
on.emit(node,"dojotouchend",_14a(evt));
});
});
}
}
var _14d={press:_133("mousedown","touchstart",_126.down),move:_133("mousemove","dojotouchmove",_126.move),release:_133("mouseup","dojotouchend",_126.up),cancel:_133(_123.leave,"touchcancel",_125?_126.cancel:null),over:_133("mouseover","dojotouchover",_126.over),out:_133("mouseout","dojotouchout",_126.out),enter:_123._eventHandler(_133("mouseover","dojotouchover",_126.over)),leave:_123._eventHandler(_133("mouseout","dojotouchout",_126.out))};
1&&(dojo.touch=_14d);
return _14d;
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_14e,lang,_14f,when){
return _14e("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_150){
if(_150){
this.set(_150);
}
},_get:function(name,_151){
return typeof this[_151.g]==="function"?this[_151.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,_152){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var _153=this._getAttrNames(name),_154=this._get(name,_153),_155=this[_153.s],_156;
if(typeof _155==="function"){
_156=_155.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=_152;
}
if(this._watchCallbacks){
var self=this;
when(_156,function(){
self._watchCallbacks(name,_154,_152);
});
}
return this;
},_changeAttrValue:function(name,_157){
var _158=this.get(name);
this[name]=_157;
if(this._watchCallbacks){
this._watchCallbacks(name,_158,_157);
}
return this;
},watch:function(name,_159){
var _15a=this._watchCallbacks;
if(!_15a){
var self=this;
_15a=this._watchCallbacks=function(name,_15b,_15c,_15d){
var _15e=function(_15f){
if(_15f){
_15f=_15f.slice();
for(var i=0,l=_15f.length;i<l;i++){
_15f[i].call(self,name,_15b,_15c);
}
}
};
_15e(_15a["_"+name]);
if(!_15d){
_15e(_15a["*"]);
}
};
}
if(!_159&&typeof name==="function"){
_159=name;
name="*";
}else{
name="_"+name;
}
var _160=_15a[name];
if(typeof _160!=="object"){
_160=_15a[name]=[];
}
_160.push(_159);
var _161={};
_161.unwatch=_161.remove=function(){
var _162=_14f.indexOf(_160,_159);
if(_162>-1){
_160.splice(_162,1);
}
};
return _161;
}});
});
},"dijit/_CssStateMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class","dojo/has","dojo/_base/lang","dojo/on","dojo/domReady","dojo/touch","dojo/_base/window","./a11yclick","./registry"],function(_163,_164,dom,_165,has,lang,on,_166,_167,win,_168,_169){
var _16a=_164("dijit._CssStateMixin",[],{hovering:false,active:false,_applyAttributes:function(){
this.inherited(arguments);
_163.forEach(["disabled","readOnly","checked","selected","focused","state","hovering","active","_opened"],function(attr){
this.watch(attr,lang.hitch(this,"_setStateClass"));
},this);
for(var ap in this.cssStateNodes||{}){
this._trackMouseState(this[ap],this.cssStateNodes[ap]);
}
this._trackMouseState(this.domNode,this.baseClass);
this._setStateClass();
},_cssMouseEvent:function(_16b){
if(!this.disabled){
switch(_16b.type){
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
var _16c=this.baseClass.split(" ");
function _16d(_16e){
_16c=_16c.concat(_163.map(_16c,function(c){
return c+_16e;
}),"dijit"+_16e);
};
if(!this.isLeftToRight()){
_16d("Rtl");
}
var _16f=this.checked=="mixed"?"Mixed":(this.checked?"Checked":"");
if(this.checked){
_16d(_16f);
}
if(this.state){
_16d(this.state);
}
if(this.selected){
_16d("Selected");
}
if(this._opened){
_16d("Opened");
}
if(this.disabled){
_16d("Disabled");
}else{
if(this.readOnly){
_16d("ReadOnly");
}else{
if(this.active){
_16d("Active");
}else{
if(this.hovering){
_16d("Hover");
}
}
}
}
if(this.focused){
_16d("Focused");
}
var tn=this.stateNode||this.domNode,_170={};
_163.forEach(tn.className.split(" "),function(c){
_170[c]=true;
});
if("_stateClasses" in this){
_163.forEach(this._stateClasses,function(c){
delete _170[c];
});
}
_163.forEach(_16c,function(c){
_170[c]=true;
});
var _171=[];
for(var c in _170){
_171.push(c);
}
var cls=_171.join(" ");
if(cls!=tn.className){
tn.className=cls;
}
this._stateClasses=_16c;
},_subnodeCssMouseEvent:function(node,_172,evt){
if(this.disabled||this.readOnly){
return;
}
function _173(_174){
_165.toggle(node,_172+"Hover",_174);
};
function _175(_176){
_165.toggle(node,_172+"Active",_176);
};
function _177(_178){
_165.toggle(node,_172+"Focused",_178);
};
switch(evt.type){
case "mouseover":
case "MSPointerOver":
case "pointerover":
_173(true);
break;
case "mouseout":
case "MSPointerOut":
case "pointerout":
_173(false);
_175(false);
break;
case "mousedown":
case "touchstart":
case "MSPointerDown":
case "pointerdown":
case "keydown":
_175(true);
break;
case "mouseup":
case "MSPointerUp":
case "pointerup":
case "dojotouchend":
case "keyup":
_175(false);
break;
case "focus":
case "focusin":
_177(true);
break;
case "blur":
case "focusout":
_177(false);
break;
}
},_trackMouseState:function(node,_179){
node._cssState=_179;
}});
_166(function(){
function _17a(evt,_17b,_17c){
if(_17c&&dom.isDescendant(_17c,_17b)){
return;
}
for(var node=_17b;node&&node!=_17c;node=node.parentNode){
if(node._cssState){
var _17d=_169.getEnclosingWidget(node);
if(_17d){
if(node==_17d.domNode){
_17d._cssMouseEvent(evt);
}else{
_17d._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
}
}
};
var body=win.body(),_17e;
on(body,_167.over,function(evt){
_17a(evt,evt.target,evt.relatedTarget);
});
on(body,_167.out,function(evt){
_17a(evt,evt.target,evt.relatedTarget);
});
on(body,_168.press,function(evt){
_17e=evt.target;
_17a(evt,_17e);
});
on(body,_168.release,function(evt){
_17a(evt,_17e);
_17e=null;
});
on(body,"focusin, focusout",function(evt){
var node=evt.target;
if(node._cssState&&!node.getAttribute("widgetId")){
var _17f=_169.getEnclosingWidget(node);
if(_17f){
_17f._subnodeCssMouseEvent(node,node._cssState,evt);
}
}
});
});
return _16a;
});
},"dijit/PopupMenuItem":function(){
define(["dojo/_base/declare","dojo/dom-style","dojo/_base/lang","dojo/query","./popup","./registry","./MenuItem","./hccss"],function(_180,_181,lang,_182,pm,_183,_184){
return _180("dijit.PopupMenuItem",_184,{baseClass:"dijitMenuItem dijitPopupMenuItem",_fillContent:function(){
if(this.srcNodeRef){
var _185=_182("*",this.srcNodeRef);
this.inherited(arguments,[_185[0]]);
this.dropDownContainer=this.srcNodeRef;
}
},_openPopup:function(_186,_187){
var _188=this.popup;
pm.open(lang.delegate(_186,{popup:this.popup,around:this.domNode}));
if(_187&&_188.focus){
_188.focus();
}
},_closePopup:function(){
pm.close(this.popup);
this.popup.parentMenu=null;
},startup:function(){
if(this._started){
return;
}
this.inherited(arguments);
if(!this.popup){
var node=_182("[widgetId]",this.dropDownContainer)[0];
this.popup=_183.byNode(node);
}
this.ownerDocumentBody.appendChild(this.popup.domNode);
this.popup.domNode.setAttribute("aria-labelledby",this.containerNode.id);
this.popup.startup();
this.popup.domNode.style.display="none";
if(this.arrowWrapper){
_181.set(this.arrowWrapper,"visibility","");
}
this.focusNode.setAttribute("aria-haspopup","true");
},destroyDescendants:function(_189){
if(this.popup){
if(!this.popup._destroyed){
this.popup.destroyRecursive(_189);
}
delete this.popup;
}
this.inherited(arguments);
}});
});
},"dijit/_KeyNavMixin":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/keys","dojo/_base/lang","dojo/on","dijit/registry","dijit/_FocusMixin"],function(_18a,_18b,_18c,keys,lang,on,_18d,_18e){
return _18b("dijit._KeyNavMixin",_18e,{tabIndex:"0",childSelector:null,postCreate:function(){
this.inherited(arguments);
_18c.set(this.domNode,"tabIndex",this.tabIndex);
if(!this._keyNavCodes){
var _18f=this._keyNavCodes={};
_18f[keys.HOME]=lang.hitch(this,"focusFirstChild");
_18f[keys.END]=lang.hitch(this,"focusLastChild");
_18f[this.isLeftToRight()?keys.LEFT_ARROW:keys.RIGHT_ARROW]=lang.hitch(this,"_onLeftArrow");
_18f[this.isLeftToRight()?keys.RIGHT_ARROW:keys.LEFT_ARROW]=lang.hitch(this,"_onRightArrow");
_18f[keys.UP_ARROW]=lang.hitch(this,"_onUpArrow");
_18f[keys.DOWN_ARROW]=lang.hitch(this,"_onDownArrow");
}
var self=this,_190=typeof this.childSelector=="string"?this.childSelector:lang.hitch(this,"childSelector");
this.own(on(this.domNode,"keypress",lang.hitch(this,"_onContainerKeypress")),on(this.domNode,"keydown",lang.hitch(this,"_onContainerKeydown")),on(this.domNode,"focus",lang.hitch(this,"_onContainerFocus")),on(this.containerNode,on.selector(_190,"focusin"),function(evt){
self._onChildFocus(_18d.getEnclosingWidget(this),evt);
}));
},_onLeftArrow:function(){
},_onRightArrow:function(){
},_onUpArrow:function(){
},_onDownArrow:function(){
},focus:function(){
this.focusFirstChild();
},_getFirstFocusableChild:function(){
return this._getNextFocusableChild(null,1);
},_getLastFocusableChild:function(){
return this._getNextFocusableChild(null,-1);
},focusFirstChild:function(){
this.focusChild(this._getFirstFocusableChild());
},focusLastChild:function(){
this.focusChild(this._getLastFocusableChild());
},focusChild:function(_191,last){
if(!_191){
return;
}
if(this.focusedChild&&_191!==this.focusedChild){
this._onChildBlur(this.focusedChild);
}
_191.set("tabIndex",this.tabIndex);
_191.focus(last?"end":"start");
},_onContainerFocus:function(evt){
if(evt.target!==this.domNode||this.focusedChild){
return;
}
this.focus();
},_onFocus:function(){
_18c.set(this.domNode,"tabIndex","-1");
this.inherited(arguments);
},_onBlur:function(evt){
_18c.set(this.domNode,"tabIndex",this.tabIndex);
if(this.focusedChild){
this.focusedChild.set("tabIndex","-1");
this.lastFocusedChild=this.focusedChild;
this._set("focusedChild",null);
}
this.inherited(arguments);
},_onChildFocus:function(_192){
if(_192&&_192!=this.focusedChild){
if(this.focusedChild&&!this.focusedChild._destroyed){
this.focusedChild.set("tabIndex","-1");
}
_192.set("tabIndex",this.tabIndex);
this.lastFocused=_192;
this._set("focusedChild",_192);
}
},_searchString:"",multiCharSearchDuration:1000,onKeyboardSearch:function(item,evt,_193,_194){
if(item){
this.focusChild(item);
}
},_keyboardSearchCompare:function(item,_195){
var _196=item.domNode,text=item.label||(_196.focusNode?_196.focusNode.label:"")||_196.innerText||_196.textContent||"",_197=text.replace(/^\s+/,"").substr(0,_195.length).toLowerCase();
return (!!_195.length&&_197==_195)?-1:0;
},_onContainerKeydown:function(evt){
var func=this._keyNavCodes[evt.keyCode];
if(func){
func(evt,this.focusedChild);
evt.stopPropagation();
evt.preventDefault();
this._searchString="";
}else{
if(evt.keyCode==keys.SPACE&&this._searchTimer&&!(evt.ctrlKey||evt.altKey||evt.metaKey)){
evt.stopImmediatePropagation();
evt.preventDefault();
this._keyboardSearch(evt," ");
}
}
},_onContainerKeypress:function(evt){
if(evt.charCode<=keys.SPACE||evt.ctrlKey||evt.altKey||evt.metaKey){
return;
}
evt.preventDefault();
evt.stopPropagation();
this._keyboardSearch(evt,String.fromCharCode(evt.charCode).toLowerCase());
},_keyboardSearch:function(evt,_198){
var _199=null,_19a,_19b=0,_19c=lang.hitch(this,function(){
if(this._searchTimer){
this._searchTimer.remove();
}
this._searchString+=_198;
var _19d=/^(.)\1*$/.test(this._searchString);
var _19e=_19d?1:this._searchString.length;
_19a=this._searchString.substr(0,_19e);
this._searchTimer=this.defer(function(){
this._searchTimer=null;
this._searchString="";
},this.multiCharSearchDuration);
var _19f=this.focusedChild||null;
if(_19e==1||!_19f){
_19f=this._getNextFocusableChild(_19f,1);
if(!_19f){
return;
}
}
var stop=_19f;
do{
var rc=this._keyboardSearchCompare(_19f,_19a);
if(!!rc&&_19b++==0){
_199=_19f;
}
if(rc==-1){
_19b=-1;
break;
}
_19f=this._getNextFocusableChild(_19f,1);
}while(_19f&&_19f!=stop);
});
_19c();
this.onKeyboardSearch(_199,evt,_19a,_19b);
},_onChildBlur:function(){
},_getNextFocusableChild:function(_1a0,dir){
var _1a1=_1a0;
do{
if(!_1a0){
_1a0=this[dir>0?"_getFirst":"_getLast"]();
if(!_1a0){
break;
}
}else{
_1a0=this._getNext(_1a0,dir);
}
if(_1a0!=null&&_1a0!=_1a1&&_1a0.isFocusable()){
return _1a0;
}
}while(_1a0!=_1a1);
return null;
},_getFirst:function(){
return null;
},_getLast:function(){
return null;
},_getNext:function(_1a2,dir){
if(_1a2){
_1a2=_1a2.domNode;
while(_1a2){
_1a2=_1a2[dir<0?"previousSibling":"nextSibling"];
if(_1a2&&"getAttribute" in _1a2){
var w=_18d.byNode(_1a2);
if(w){
return w;
}
}
}
}
return null;
}});
});
},"dijit/BackgroundIframe":function(){
define(["require","./main","dojo/_base/config","dojo/dom-construct","dojo/dom-style","dojo/_base/lang","dojo/on","dojo/sniff"],function(_1a3,_1a4,_1a5,_1a6,_1a7,lang,on,has){
has.add("config-bgIframe",(has("ie")&&!/IEMobile\/10\.0/.test(navigator.userAgent))||(has("trident")&&/Windows NT 6.[01]/.test(navigator.userAgent)));
var _1a8=new function(){
var _1a9=[];
this.pop=function(){
var _1aa;
if(_1a9.length){
_1aa=_1a9.pop();
_1aa.style.display="";
}else{
if(has("ie")<9){
var burl=_1a5["dojoBlankHtmlUrl"]||_1a3.toUrl("dojo/resources/blank.html")||"javascript:\"\"";
var html="<iframe src='"+burl+"' role='presentation'"+" style='position: absolute; left: 0px; top: 0px;"+"z-index: -1; filter:Alpha(Opacity=\"0\");'>";
_1aa=document.createElement(html);
}else{
_1aa=_1a6.create("iframe");
_1aa.src="javascript:\"\"";
_1aa.className="dijitBackgroundIframe";
_1aa.setAttribute("role","presentation");
_1a7.set(_1aa,"opacity",0.1);
}
_1aa.tabIndex=-1;
}
return _1aa;
};
this.push=function(_1ab){
_1ab.style.display="none";
_1a9.push(_1ab);
};
}();
_1a4.BackgroundIframe=function(node){
if(!node.id){
throw new Error("no id");
}
if(has("config-bgIframe")){
var _1ac=(this.iframe=_1a8.pop());
node.appendChild(_1ac);
if(has("ie")<7||has("quirks")){
this.resize(node);
this._conn=on(node,"resize",lang.hitch(this,"resize",node));
}else{
_1a7.set(_1ac,{width:"100%",height:"100%"});
}
}
};
lang.extend(_1a4.BackgroundIframe,{resize:function(node){
if(this.iframe){
_1a7.set(this.iframe,{width:node.offsetWidth+"px",height:node.offsetHeight+"px"});
}
},destroy:function(){
if(this._conn){
this._conn.remove();
this._conn=null;
}
if(this.iframe){
this.iframe.parentNode.removeChild(this.iframe);
_1a8.push(this.iframe);
delete this.iframe;
}
}});
return _1a4.BackgroundIframe;
});
},"dojo/text":function(){
define(["./_base/kernel","require","./has","./request"],function(dojo,_1ad,has,_1ae){
var _1af;
if(1){
_1af=function(url,sync,load){
_1ae(url,{sync:!!sync,headers:{"X-Requested-With":null}}).then(load);
};
}else{
if(_1ad.getText){
_1af=_1ad.getText;
}else{
console.error("dojo/text plugin failed to load because loader does not support getText");
}
}
var _1b0={},_1b1=function(text){
if(text){
text=text.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,"");
var _1b2=text.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
if(_1b2){
text=_1b2[1];
}
}else{
text="";
}
return text;
},_1b3={},_1b4={};
dojo.cache=function(_1b5,url,_1b6){
var key;
if(typeof _1b5=="string"){
if(/\//.test(_1b5)){
key=_1b5;
_1b6=url;
}else{
key=_1ad.toUrl(_1b5.replace(/\./g,"/")+(url?("/"+url):""));
}
}else{
key=_1b5+"";
_1b6=url;
}
var val=(_1b6!=undefined&&typeof _1b6!="string")?_1b6.value:_1b6,_1b7=_1b6&&_1b6.sanitize;
if(typeof val=="string"){
_1b0[key]=val;
return _1b7?_1b1(val):val;
}else{
if(val===null){
delete _1b0[key];
return null;
}else{
if(!(key in _1b0)){
_1af(key,true,function(text){
_1b0[key]=text;
});
}
return _1b7?_1b1(_1b0[key]):_1b0[key];
}
}
};
return {dynamic:true,normalize:function(id,_1b8){
var _1b9=id.split("!"),url=_1b9[0];
return (/^\./.test(url)?_1b8(url):url)+(_1b9[1]?"!"+_1b9[1]:"");
},load:function(id,_1ba,load){
var _1bb=id.split("!"),_1bc=_1bb.length>1,_1bd=_1bb[0],url=_1ba.toUrl(_1bb[0]),_1be="url:"+url,text=_1b3,_1bf=function(text){
load(_1bc?_1b1(text):text);
};
if(_1bd in _1b0){
text=_1b0[_1bd];
}else{
if(_1ba.cache&&_1be in _1ba.cache){
text=_1ba.cache[_1be];
}else{
if(url in _1b0){
text=_1b0[url];
}
}
}
if(text===_1b3){
if(_1b4[url]){
_1b4[url].push(_1bf);
}else{
var _1c0=_1b4[url]=[_1bf];
_1af(url,!_1ba.async,function(text){
_1b0[_1bd]=_1b0[url]=text;
for(var i=0;i<_1c0.length;){
_1c0[i++](text);
}
delete _1b4[url];
});
}
}else{
_1bf(text);
}
}};
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_1c1,win,_1c2){
var _1c3={},hash={};
var _1c4={length:0,add:function(_1c5){
if(hash[_1c5.id]){
throw new Error("Tried to register widget with id=="+_1c5.id+" but that id is already registered");
}
hash[_1c5.id]=_1c5;
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
},getUniqueId:function(_1c6){
var id;
do{
id=_1c6+"_"+(_1c6 in _1c3?++_1c3[_1c6]:_1c3[_1c6]=0);
}while(hash[id]);
return _1c2._scopeName=="dijit"?id:_1c2._scopeName+"_"+id;
},findWidgets:function(root,_1c7){
var _1c8=[];
function _1c9(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _1ca=node.getAttribute("widgetId");
if(_1ca){
var _1cb=hash[_1ca];
if(_1cb){
_1c8.push(_1cb);
}
}else{
if(node!==_1c7){
_1c9(node);
}
}
}
}
};
_1c9(root);
return _1c8;
},_destroyAll:function(){
_1c2._curFocus=null;
_1c2._prevFocus=null;
_1c2._activeStack=[];
_1c1.forEach(_1c4.findWidgets(win.body()),function(_1cc){
if(!_1cc._destroyed){
if(_1cc.destroyRecursive){
_1cc.destroyRecursive();
}else{
if(_1cc.destroy){
_1cc.destroy();
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
_1c2.registry=_1c4;
return _1c4;
});
},"dijit/_AttachMixin":function(){
define(["require","dojo/_base/array","dojo/_base/connect","dojo/_base/declare","dojo/_base/lang","dojo/mouse","dojo/on","dojo/touch","./_WidgetBase"],function(_1cd,_1ce,_1cf,_1d0,lang,_1d1,on,_1d2,_1d3){
var _1d4=lang.delegate(_1d2,{"mouseenter":_1d1.enter,"mouseleave":_1d1.leave,"keypress":_1cf._keypress});
var _1d5;
var _1d6=_1d0("dijit._AttachMixin",null,{constructor:function(){
this._attachPoints=[];
this._attachEvents=[];
},buildRendering:function(){
this.inherited(arguments);
this._attachTemplateNodes(this.domNode);
this._beforeFillContent();
},_beforeFillContent:function(){
},_attachTemplateNodes:function(_1d7){
var node=_1d7;
while(true){
if(node.nodeType==1&&(this._processTemplateNode(node,function(n,p){
return n.getAttribute(p);
},this._attach)||this.searchContainerNode)&&node.firstChild){
node=node.firstChild;
}else{
if(node==_1d7){
return;
}
while(!node.nextSibling){
node=node.parentNode;
if(node==_1d7){
return;
}
}
node=node.nextSibling;
}
}
},_processTemplateNode:function(_1d8,_1d9,_1da){
var ret=true;
var _1db=this.attachScope||this,_1dc=_1d9(_1d8,"dojoAttachPoint")||_1d9(_1d8,"data-dojo-attach-point");
if(_1dc){
var _1dd,_1de=_1dc.split(/\s*,\s*/);
while((_1dd=_1de.shift())){
if(lang.isArray(_1db[_1dd])){
_1db[_1dd].push(_1d8);
}else{
_1db[_1dd]=_1d8;
}
ret=(_1dd!="containerNode");
this._attachPoints.push(_1dd);
}
}
var _1df=_1d9(_1d8,"dojoAttachEvent")||_1d9(_1d8,"data-dojo-attach-event");
if(_1df){
var _1e0,_1e1=_1df.split(/\s*,\s*/);
var trim=lang.trim;
while((_1e0=_1e1.shift())){
if(_1e0){
var _1e2=null;
if(_1e0.indexOf(":")!=-1){
var _1e3=_1e0.split(":");
_1e0=trim(_1e3[0]);
_1e2=trim(_1e3[1]);
}else{
_1e0=trim(_1e0);
}
if(!_1e2){
_1e2=_1e0;
}
this._attachEvents.push(_1da(_1d8,_1e0,lang.hitch(_1db,_1e2)));
}
}
}
return ret;
},_attach:function(node,type,func){
type=type.replace(/^on/,"").toLowerCase();
if(type=="dijitclick"){
type=_1d5||(_1d5=_1cd("./a11yclick"));
}else{
type=_1d4[type]||type;
}
return on(node,type,func);
},_detachTemplateNodes:function(){
var _1e4=this.attachScope||this;
_1ce.forEach(this._attachPoints,function(_1e5){
delete _1e4[_1e5];
});
this._attachPoints=[];
_1ce.forEach(this._attachEvents,function(_1e6){
_1e6.remove();
});
this._attachEvents=[];
},destroyRendering:function(){
this._detachTemplateNodes();
this.inherited(arguments);
}});
lang.extend(_1d3,{dojoAttachEvent:"",dojoAttachPoint:""});
return _1d6;
});
},"dojo/uacss":function(){
define(["./dom-geometry","./_base/lang","./domReady","./sniff","./_base/window"],function(_1e7,lang,_1e8,has,_1e9){
var html=_1e9.doc.documentElement,ie=has("ie"),_1ea=has("trident"),_1eb=has("opera"),maj=Math.floor,ff=has("ff"),_1ec=_1e7.boxModel.replace(/-/,""),_1ed={"dj_quirks":has("quirks"),"dj_opera":_1eb,"dj_khtml":has("khtml"),"dj_webkit":has("webkit"),"dj_safari":has("safari"),"dj_chrome":has("chrome"),"dj_edge":has("edge"),"dj_gecko":has("mozilla"),"dj_ios":has("ios"),"dj_android":has("android")};
if(ie){
_1ed["dj_ie"]=true;
_1ed["dj_ie"+maj(ie)]=true;
_1ed["dj_iequirks"]=has("quirks");
}
if(_1ea){
_1ed["dj_trident"]=true;
_1ed["dj_trident"+maj(_1ea)]=true;
}
if(ff){
_1ed["dj_ff"+maj(ff)]=true;
}
_1ed["dj_"+_1ec]=true;
var _1ee="";
for(var clz in _1ed){
if(_1ed[clz]){
_1ee+=clz+" ";
}
}
html.className=lang.trim(html.className+" "+_1ee);
_1e8(function(){
if(!_1e7.isBodyLtr()){
var _1ef="dj_rtl dijitRtl "+_1ee.replace(/ /g,"-rtl ");
html.className=lang.trim(html.className+" "+_1ef+"dj_rtl dijitRtl "+_1ee.replace(/ /g,"-rtl "));
}
});
return has;
});
},"dijit/_KeyNavContainer":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-attr","dojo/_base/kernel","dojo/keys","dojo/_base/lang","./registry","./_Container","./_FocusMixin","./_KeyNavMixin"],function(_1f0,_1f1,_1f2,_1f3,keys,lang,_1f4,_1f5,_1f6,_1f7){
return _1f1("dijit._KeyNavContainer",[_1f6,_1f7,_1f5],{connectKeyNavHandlers:function(_1f8,_1f9){
var _1fa=(this._keyNavCodes={});
var prev=lang.hitch(this,"focusPrev");
var next=lang.hitch(this,"focusNext");
_1f0.forEach(_1f8,function(code){
_1fa[code]=prev;
});
_1f0.forEach(_1f9,function(code){
_1fa[code]=next;
});
_1fa[keys.HOME]=lang.hitch(this,"focusFirstChild");
_1fa[keys.END]=lang.hitch(this,"focusLastChild");
},startupKeyNavChildren:function(){
_1f3.deprecated("startupKeyNavChildren() call no longer needed","","2.0");
},startup:function(){
this.inherited(arguments);
_1f0.forEach(this.getChildren(),lang.hitch(this,"_startupChild"));
},addChild:function(_1fb,_1fc){
this.inherited(arguments);
this._startupChild(_1fb);
},_startupChild:function(_1fd){
_1fd.set("tabIndex","-1");
},_getFirst:function(){
var _1fe=this.getChildren();
return _1fe.length?_1fe[0]:null;
},_getLast:function(){
var _1ff=this.getChildren();
return _1ff.length?_1ff[_1ff.length-1]:null;
},focusNext:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,1));
},focusPrev:function(){
this.focusChild(this._getNextFocusableChild(this.focusedChild,-1),true);
},childSelector:function(node){
var node=_1f4.byNode(node);
return node&&node.getParent()==this;
}});
});
},"dijit/place":function(){
define(["dojo/_base/array","dojo/dom-geometry","dojo/dom-style","dojo/_base/kernel","dojo/_base/window","./Viewport","./main"],function(_200,_201,_202,_203,win,_204,_205){
function _206(node,_207,_208,_209){
var view=_204.getEffectiveBox(node.ownerDocument);
if(!node.parentNode||String(node.parentNode.tagName).toLowerCase()!="body"){
win.body(node.ownerDocument).appendChild(node);
}
var best=null;
_200.some(_207,function(_20a){
var _20b=_20a.corner;
var pos=_20a.pos;
var _20c=0;
var _20d={w:{"L":view.l+view.w-pos.x,"R":pos.x-view.l,"M":view.w}[_20b.charAt(1)],h:{"T":view.t+view.h-pos.y,"B":pos.y-view.t,"M":view.h}[_20b.charAt(0)]};
var s=node.style;
s.left=s.right="auto";
if(_208){
var res=_208(node,_20a.aroundCorner,_20b,_20d,_209);
_20c=typeof res=="undefined"?0:res;
}
var _20e=node.style;
var _20f=_20e.display;
var _210=_20e.visibility;
if(_20e.display=="none"){
_20e.visibility="hidden";
_20e.display="";
}
var bb=_201.position(node);
_20e.display=_20f;
_20e.visibility=_210;
var _211={"L":pos.x,"R":pos.x-bb.w,"M":Math.max(view.l,Math.min(view.l+view.w,pos.x+(bb.w>>1))-bb.w)}[_20b.charAt(1)],_212={"T":pos.y,"B":pos.y-bb.h,"M":Math.max(view.t,Math.min(view.t+view.h,pos.y+(bb.h>>1))-bb.h)}[_20b.charAt(0)],_213=Math.max(view.l,_211),_214=Math.max(view.t,_212),endX=Math.min(view.l+view.w,_211+bb.w),endY=Math.min(view.t+view.h,_212+bb.h),_215=endX-_213,_216=endY-_214;
_20c+=(bb.w-_215)+(bb.h-_216);
if(best==null||_20c<best.overflow){
best={corner:_20b,aroundCorner:_20a.aroundCorner,x:_213,y:_214,w:_215,h:_216,overflow:_20c,spaceAvailable:_20d};
}
return !_20c;
});
if(best.overflow&&_208){
_208(node,best.aroundCorner,best.corner,best.spaceAvailable,_209);
}
var top=best.y,side=best.x,body=win.body(node.ownerDocument);
if(/relative|absolute/.test(_202.get(body,"position"))){
top-=_202.get(body,"marginTop");
side-=_202.get(body,"marginLeft");
}
var s=node.style;
s.top=top+"px";
s.left=side+"px";
s.right="auto";
return best;
};
var _217={"TL":"BR","TR":"BL","BL":"TR","BR":"TL"};
var _218={at:function(node,pos,_219,_21a,_21b){
var _21c=_200.map(_219,function(_21d){
var c={corner:_21d,aroundCorner:_217[_21d],pos:{x:pos.x,y:pos.y}};
if(_21a){
c.pos.x+=_21d.charAt(1)=="L"?_21a.x:-_21a.x;
c.pos.y+=_21d.charAt(0)=="T"?_21a.y:-_21a.y;
}
return c;
});
return _206(node,_21c,_21b);
},around:function(node,_21e,_21f,_220,_221){
var _222;
if(typeof _21e=="string"||"offsetWidth" in _21e||"ownerSVGElement" in _21e){
_222=_201.position(_21e,true);
if(/^(above|below)/.test(_21f[0])){
var _223=_201.getBorderExtents(_21e),_224=_21e.firstChild?_201.getBorderExtents(_21e.firstChild):{t:0,l:0,b:0,r:0},_225=_201.getBorderExtents(node),_226=node.firstChild?_201.getBorderExtents(node.firstChild):{t:0,l:0,b:0,r:0};
_222.y+=Math.min(_223.t+_224.t,_225.t+_226.t);
_222.h-=Math.min(_223.t+_224.t,_225.t+_226.t)+Math.min(_223.b+_224.b,_225.b+_226.b);
}
}else{
_222=_21e;
}
if(_21e.parentNode){
var _227=_202.getComputedStyle(_21e).position=="absolute";
var _228=_21e.parentNode;
while(_228&&_228.nodeType==1&&_228.nodeName!="BODY"){
var _229=_201.position(_228,true),pcs=_202.getComputedStyle(_228);
if(/relative|absolute/.test(pcs.position)){
_227=false;
}
if(!_227&&/hidden|auto|scroll/.test(pcs.overflow)){
var _22a=Math.min(_222.y+_222.h,_229.y+_229.h);
var _22b=Math.min(_222.x+_222.w,_229.x+_229.w);
_222.x=Math.max(_222.x,_229.x);
_222.y=Math.max(_222.y,_229.y);
_222.h=_22a-_222.y;
_222.w=_22b-_222.x;
}
if(pcs.position=="absolute"){
_227=true;
}
_228=_228.parentNode;
}
}
var x=_222.x,y=_222.y,_22c="w" in _222?_222.w:(_222.w=_222.width),_22d="h" in _222?_222.h:(_203.deprecated("place.around: dijit/place.__Rectangle: { x:"+x+", y:"+y+", height:"+_222.height+", width:"+_22c+" } has been deprecated.  Please use { x:"+x+", y:"+y+", h:"+_222.height+", w:"+_22c+" }","","2.0"),_222.h=_222.height);
var _22e=[];
function push(_22f,_230){
_22e.push({aroundCorner:_22f,corner:_230,pos:{x:{"L":x,"R":x+_22c,"M":x+(_22c>>1)}[_22f.charAt(1)],y:{"T":y,"B":y+_22d,"M":y+(_22d>>1)}[_22f.charAt(0)]}});
};
_200.forEach(_21f,function(pos){
var ltr=_220;
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
var _231=_206(node,_22e,_221,{w:_22c,h:_22d});
_231.aroundNodePos=_222;
return _231;
}};
return _205.place=_218;
});
},"dojo/window":function(){
define(["./_base/lang","./sniff","./_base/window","./dom","./dom-geometry","./dom-style","./dom-construct"],function(lang,has,_232,dom,geom,_233,_234){
has.add("rtl-adjust-position-for-verticalScrollBar",function(win,doc){
var body=_232.body(doc),_235=_234.create("div",{style:{overflow:"scroll",overflowX:"visible",direction:"rtl",visibility:"hidden",position:"absolute",left:"0",top:"0",width:"64px",height:"64px"}},body,"last"),div=_234.create("div",{style:{overflow:"hidden",direction:"ltr"}},_235,"last"),ret=geom.position(div).x!=0;
_235.removeChild(div);
body.removeChild(_235);
return ret;
});
has.add("position-fixed-support",function(win,doc){
var body=_232.body(doc),_236=_234.create("span",{style:{visibility:"hidden",position:"fixed",left:"1px",top:"1px"}},body,"last"),_237=_234.create("span",{style:{position:"fixed",left:"0",top:"0"}},_236,"last"),ret=geom.position(_237).x!=geom.position(_236).x;
_236.removeChild(_237);
body.removeChild(_236);
return ret;
});
var _238={getBox:function(doc){
doc=doc||_232.doc;
var _239=(doc.compatMode=="BackCompat")?_232.body(doc):doc.documentElement,_23a=geom.docScroll(doc),w,h;
if(has("touch")){
var _23b=_238.get(doc);
w=_23b.innerWidth||_239.clientWidth;
h=_23b.innerHeight||_239.clientHeight;
}else{
w=_239.clientWidth;
h=_239.clientHeight;
}
return {l:_23a.x,t:_23a.y,w:w,h:h};
},get:function(doc){
if(has("ie")&&_238!==document.parentWindow){
doc.parentWindow.execScript("document._parentWindow = window;","Javascript");
var win=doc._parentWindow;
doc._parentWindow=null;
return win;
}
return doc.parentWindow||doc.defaultView;
},scrollIntoView:function(node,pos){
try{
node=dom.byId(node);
var doc=node.ownerDocument||_232.doc,body=_232.body(doc),html=doc.documentElement||body.parentNode,isIE=has("ie")||has("trident"),isWK=has("webkit");
if(node==body||node==html){
return;
}
if(!(has("mozilla")||isIE||isWK||has("opera")||has("trident")||has("edge"))&&("scrollIntoView" in node)){
node.scrollIntoView(false);
return;
}
var _23c=doc.compatMode=="BackCompat",_23d=Math.min(body.clientWidth||html.clientWidth,html.clientWidth||body.clientWidth),_23e=Math.min(body.clientHeight||html.clientHeight,html.clientHeight||body.clientHeight),_23f=(isWK||_23c)?body:html,_240=pos||geom.position(node),el=node.parentNode,_241=function(el){
return (isIE<=6||(isIE==7&&_23c))?false:(has("position-fixed-support")&&(_233.get(el,"position").toLowerCase()=="fixed"));
},self=this,_242=function(el,x,y){
if(el.tagName=="BODY"||el.tagName=="HTML"){
self.get(el.ownerDocument).scrollBy(x,y);
}else{
x&&(el.scrollLeft+=x);
y&&(el.scrollTop+=y);
}
};
if(_241(node)){
return;
}
while(el){
if(el==body){
el=_23f;
}
var _243=geom.position(el),_244=_241(el),rtl=_233.getComputedStyle(el).direction.toLowerCase()=="rtl";
if(el==_23f){
_243.w=_23d;
_243.h=_23e;
if(_23f==html&&(isIE||has("trident"))&&rtl){
_243.x+=_23f.offsetWidth-_243.w;
}
_243.x=0;
_243.y=0;
}else{
var pb=geom.getPadBorderExtents(el);
_243.w-=pb.w;
_243.h-=pb.h;
_243.x+=pb.l;
_243.y+=pb.t;
var _245=el.clientWidth,_246=_243.w-_245;
if(_245>0&&_246>0){
if(rtl&&has("rtl-adjust-position-for-verticalScrollBar")){
_243.x+=_246;
}
_243.w=_245;
}
_245=el.clientHeight;
_246=_243.h-_245;
if(_245>0&&_246>0){
_243.h=_245;
}
}
if(_244){
if(_243.y<0){
_243.h+=_243.y;
_243.y=0;
}
if(_243.x<0){
_243.w+=_243.x;
_243.x=0;
}
if(_243.y+_243.h>_23e){
_243.h=_23e-_243.y;
}
if(_243.x+_243.w>_23d){
_243.w=_23d-_243.x;
}
}
var l=_240.x-_243.x,t=_240.y-_243.y,r=l+_240.w-_243.w,bot=t+_240.h-_243.h;
var s,old;
if(r*l>0&&(!!el.scrollLeft||el==_23f||el.scrollWidth>el.offsetHeight)){
s=Math[l<0?"max":"min"](l,r);
if(rtl&&((isIE==8&&!_23c)||has("trident")>=5)){
s=-s;
}
old=el.scrollLeft;
_242(el,s,0);
s=el.scrollLeft-old;
_240.x-=s;
}
if(bot*t>0&&(!!el.scrollTop||el==_23f||el.scrollHeight>el.offsetHeight)){
s=Math.ceil(Math[t<0?"max":"min"](t,bot));
old=el.scrollTop;
_242(el,0,s);
s=el.scrollTop-old;
_240.y-=s;
}
el=(el!=_23f)&&!_244&&el.parentNode;
}
}
catch(error){
console.error("scrollIntoView: "+error);
node.scrollIntoView(false);
}
}};
1&&lang.setObject("dojo.window",_238);
return _238;
});
},"dijit/MenuSeparator":function(){
define(["dojo/_base/declare","dojo/dom","./_WidgetBase","./_TemplatedMixin","./_Contained","dojo/text!./templates/MenuSeparator.html"],function(_247,dom,_248,_249,_24a,_24b){
return _247("dijit.MenuSeparator",[_248,_249,_24a],{templateString:_24b,buildRendering:function(){
this.inherited(arguments);
dom.setSelectable(this.domNode,false);
},isFocusable:function(){
return false;
}});
});
},"dijit/_OnDijitClickMixin":function(){
define(["dojo/on","dojo/_base/array","dojo/keys","dojo/_base/declare","dojo/has","./a11yclick"],function(on,_24c,keys,_24d,has,_24e){
var ret=_24d("dijit._OnDijitClickMixin",null,{connect:function(obj,_24f,_250){
return this.inherited(arguments,[obj,_24f=="ondijitclick"?_24e:_24f,_250]);
}});
ret.a11yclick=_24e;
return ret;
});
},"dijit/a11yclick":function(){
define(["dojo/keys","dojo/mouse","dojo/on","dojo/touch"],function(keys,_251,on,_252){
function _253(e){
if((e.keyCode===keys.ENTER||e.keyCode===keys.SPACE)&&!/input|button|textarea/i.test(e.target.nodeName)){
for(var node=e.target;node;node=node.parentNode){
if(node.dojoClick){
return true;
}
}
}
};
var _254;
on(document,"keydown",function(e){
if(_253(e)){
_254=e.target;
e.preventDefault();
}else{
_254=null;
}
});
on(document,"keyup",function(e){
if(_253(e)&&e.target==_254){
_254=null;
on.emit(e.target,"click",{cancelable:true,bubbles:true,ctrlKey:e.ctrlKey,shiftKey:e.shiftKey,metaKey:e.metaKey,altKey:e.altKey,_origType:e.type});
}
});
var _255=function(node,_256){
node.dojoClick=true;
return on(node,"click",_256);
};
_255.click=_255;
_255.press=function(node,_257){
var _258=on(node,_252.press,function(evt){
if(evt.type=="mousedown"&&!_251.isLeft(evt)){
return;
}
_257(evt);
}),_259=on(node,"keydown",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_257(evt);
}
});
return {remove:function(){
_258.remove();
_259.remove();
}};
};
_255.release=function(node,_25a){
var _25b=on(node,_252.release,function(evt){
if(evt.type=="mouseup"&&!_251.isLeft(evt)){
return;
}
_25a(evt);
}),_25c=on(node,"keyup",function(evt){
if(evt.keyCode===keys.ENTER||evt.keyCode===keys.SPACE){
_25a(evt);
}
});
return {remove:function(){
_25b.remove();
_25c.remove();
}};
};
_255.move=_252.move;
return _255;
});
},"dojo/request":function(){
define(["./request/default!"],function(_25d){
return _25d;
});
},"dijit/hccss":function(){
define(["dojo/dom-class","dojo/hccss","dojo/domReady","dojo/_base/window"],function(_25e,has,_25f,win){
_25f(function(){
if(has("highcontrast")){
_25e.add(win.body(),"dijit_a11y");
}
});
return has;
});
},"dijit/_TemplatedMixin":function(){
define(["dojo/cache","dojo/_base/declare","dojo/dom-construct","dojo/_base/lang","dojo/on","dojo/sniff","dojo/string","./_AttachMixin"],function(_260,_261,_262,lang,on,has,_263,_264){
var _265=_261("dijit._TemplatedMixin",_264,{templateString:null,templatePath:null,_skipNodeCache:false,searchContainerNode:true,_stringRepl:function(tmpl){
var _266=this.declaredClass,_267=this;
return _263.substitute(tmpl,this,function(_268,key){
if(key.charAt(0)=="!"){
_268=lang.getObject(key.substr(1),false,_267);
}
if(typeof _268=="undefined"){
throw new Error(_266+" template:"+key);
}
if(_268==null){
return "";
}
return key.charAt(0)=="!"?_268:this._escapeValue(""+_268);
},this);
},_escapeValue:function(val){
return val.replace(/["'<>&]/g,function(val){
return {"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;"}[val];
});
},buildRendering:function(){
if(!this._rendered){
if(!this.templateString){
this.templateString=_260(this.templatePath,{sanitize:true});
}
var _269=_265.getCachedTemplate(this.templateString,this._skipNodeCache,this.ownerDocument);
var node;
if(lang.isString(_269)){
node=_262.toDom(this._stringRepl(_269),this.ownerDocument);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_269);
}
}else{
node=_269.cloneNode(true);
}
this.domNode=node;
}
this.inherited(arguments);
if(!this._rendered){
this._fillContent(this.srcNodeRef);
}
this._rendered=true;
},_fillContent:function(_26a){
var dest=this.containerNode;
if(_26a&&dest){
while(_26a.hasChildNodes()){
dest.appendChild(_26a.firstChild);
}
}
}});
_265._templateCache={};
_265.getCachedTemplate=function(_26b,_26c,doc){
var _26d=_265._templateCache;
var key=_26b;
var _26e=_26d[key];
if(_26e){
try{
if(!_26e.ownerDocument||_26e.ownerDocument==(doc||document)){
return _26e;
}
}
catch(e){
}
_262.destroy(_26e);
}
_26b=_263.trim(_26b);
if(_26c||_26b.match(/\$\{([^\}]+)\}/g)){
return (_26d[key]=_26b);
}else{
var node=_262.toDom(_26b,doc);
if(node.nodeType!=1){
throw new Error("Invalid template: "+_26b);
}
return (_26d[key]=node);
}
};
if(has("ie")){
on(window,"unload",function(){
var _26f=_265._templateCache;
for(var key in _26f){
var _270=_26f[key];
if(typeof _270=="object"){
_262.destroy(_270);
}
delete _26f[key];
}
});
}
return _265;
});
},"dijit/_Widget":function(){
define(["dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/query","dojo/ready","./registry","./_WidgetBase","./_OnDijitClickMixin","./_FocusMixin","dojo/uacss","./hccss"],function(_271,_272,_273,_274,has,_275,lang,_276,_277,_278,_279,_27a,_27b){
function _27c(){
};
function _27d(_27e){
return function(obj,_27f,_280,_281){
if(obj&&typeof _27f=="string"&&obj[_27f]==_27c){
return obj.on(_27f.substring(2).toLowerCase(),lang.hitch(_280,_281));
}
return _27e.apply(_273,arguments);
};
};
_271.around(_273,"connect",_27d);
if(_275.connect){
_271.around(_275,"connect",_27d);
}
var _282=_274("dijit._Widget",[_279,_27a,_27b],{onClick:_27c,onDblClick:_27c,onKeyDown:_27c,onKeyPress:_27c,onKeyUp:_27c,onMouseDown:_27c,onMouseMove:_27c,onMouseOut:_27c,onMouseOver:_27c,onMouseLeave:_27c,onMouseEnter:_27c,onMouseUp:_27c,constructor:function(_283){
this._toConnect={};
for(var name in _283){
if(this[name]===_27c){
this._toConnect[name.replace(/^on/,"").toLowerCase()]=_283[name];
delete _283[name];
}
}
},postCreate:function(){
this.inherited(arguments);
for(var name in this._toConnect){
this.on(name,this._toConnect[name]);
}
delete this._toConnect;
},on:function(type,func){
if(this[this._onMap(type)]===_27c){
return _273.connect(this.domNode,type.toLowerCase(),this,func);
}
return this.inherited(arguments);
},_setFocusedAttr:function(val){
this._focused=val;
this._set("focused",val);
},setAttribute:function(attr,_284){
_275.deprecated(this.declaredClass+"::setAttribute(attr, value) is deprecated. Use set() instead.","","2.0");
this.set(attr,_284);
},attr:function(name,_285){
var args=arguments.length;
if(args>=2||typeof name==="object"){
return this.set.apply(this,arguments);
}else{
return this.get(name);
}
},getDescendants:function(){
_275.deprecated(this.declaredClass+"::getDescendants() is deprecated. Use getChildren() instead.","","2.0");
return this.containerNode?_276("[widgetId]",this.containerNode).map(_278.byNode):[];
},_onShow:function(){
this.onShow();
},onShow:function(){
},onHide:function(){
},onClose:function(){
return true;
}});
if(1){
_277(0,function(){
var _286=["dijit/_base"];
require(_286);
});
}
return _282;
});
},"dijit/_FocusMixin":function(){
define(["./focus","./_WidgetBase","dojo/_base/declare","dojo/_base/lang"],function(_287,_288,_289,lang){
lang.extend(_288,{focused:false,onFocus:function(){
},onBlur:function(){
},_onFocus:function(){
this.onFocus();
},_onBlur:function(){
this.onBlur();
}});
return _289("dijit._FocusMixin",null,{_focusManager:_287});
});
},"dijit/focus":function(){
define(["dojo/aspect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/Evented","dojo/_base/lang","dojo/on","dojo/domReady","dojo/sniff","dojo/Stateful","dojo/_base/window","dojo/window","./a11y","./registry","./main"],function(_28a,_28b,dom,_28c,_28d,_28e,_28f,lang,on,_290,has,_291,win,_292,a11y,_293,_294){
var _295;
var _296;
var _297=_28b([_291,_28f],{curNode:null,activeStack:[],constructor:function(){
var _298=lang.hitch(this,function(node){
if(dom.isDescendant(this.curNode,node)){
this.set("curNode",null);
}
if(dom.isDescendant(this.prevNode,node)){
this.set("prevNode",null);
}
});
_28a.before(_28e,"empty",_298);
_28a.before(_28e,"destroy",_298);
},registerIframe:function(_299){
return this.registerWin(_299.contentWindow,_299);
},registerWin:function(_29a,_29b){
var _29c=this,body=_29a.document&&_29a.document.body;
if(body){
var _29d=has("pointer-events")?"pointerdown":has("MSPointer")?"MSPointerDown":has("touch-events")?"mousedown, touchstart":"mousedown";
var mdh=on(_29a.document,_29d,function(evt){
if(evt&&evt.target&&evt.target.parentNode==null){
return;
}
_29c._onTouchNode(_29b||evt.target,"mouse");
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
_29c._onFocusNode(_29b||evt.target);
}else{
_29c._onTouchNode(_29b||evt.target);
}
});
var foh=on(body,"focusout",function(evt){
_29c._onBlurNode(_29b||evt.target);
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
if(now<_295+100){
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
if(now<_296+100){
return;
}
this._clearActiveWidgetsTimer=setTimeout(lang.hitch(this,function(){
delete this._clearActiveWidgetsTimer;
this._setStack([]);
}),0);
},_onTouchNode:function(node,by){
_296=(new Date()).getTime();
if(this._clearActiveWidgetsTimer){
clearTimeout(this._clearActiveWidgetsTimer);
delete this._clearActiveWidgetsTimer;
}
if(_28d.contains(node,"dijitPopup")){
node=node.firstChild;
}
var _29e=[];
try{
while(node){
var _29f=_28c.get(node,"dijitPopupParent");
if(_29f){
node=_293.byId(_29f).domNode;
}else{
if(node.tagName&&node.tagName.toLowerCase()=="body"){
if(node===win.body()){
break;
}
node=_292.get(node.ownerDocument).frameElement;
}else{
var id=node.getAttribute&&node.getAttribute("widgetId"),_2a0=id&&_293.byId(id);
if(_2a0&&!(by=="mouse"&&_2a0.get("disabled"))){
_29e.unshift(id);
}
node=node.parentNode;
}
}
}
}
catch(e){
}
this._setStack(_29e,by);
},_onFocusNode:function(node){
if(!node){
return;
}
if(node.nodeType==9){
return;
}
_295=(new Date()).getTime();
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
},_setStack:function(_2a1,by){
var _2a2=this.activeStack,_2a3=_2a2.length-1,_2a4=_2a1.length-1;
if(_2a1[_2a4]==_2a2[_2a3]){
return;
}
this.set("activeStack",_2a1);
var _2a5,i;
for(i=_2a3;i>=0&&_2a2[i]!=_2a1[i];i--){
_2a5=_293.byId(_2a2[i]);
if(_2a5){
_2a5._hasBeenBlurred=true;
_2a5.set("focused",false);
if(_2a5._focusManager==this){
_2a5._onBlur(by);
}
this.emit("widget-blur",_2a5,by);
}
}
for(i++;i<=_2a4;i++){
_2a5=_293.byId(_2a1[i]);
if(_2a5){
_2a5.set("focused",true);
if(_2a5._focusManager==this){
_2a5._onFocus(by);
}
this.emit("widget-focus",_2a5,by);
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
var _2a6=new _297();
_290(function(){
var _2a7=_2a6.registerWin(_292.get(document));
if(has("ie")){
on(window,"unload",function(){
if(_2a7){
_2a7.remove();
_2a7=null;
}
});
}
});
_294.focus=function(node){
_2a6.focus(node);
};
for(var attr in _2a6){
if(!/^_/.test(attr)){
_294.focus[attr]=typeof _2a6[attr]=="function"?lang.hitch(_2a6,attr):_2a6[attr];
}
}
_2a6.watch(function(attr,_2a8,_2a9){
_294.focus[attr]=_2a9;
});
return _2a6;
});
},"dijit/_Contained":function(){
define(["dojo/_base/declare","./registry"],function(_2aa,_2ab){
return _2aa("dijit._Contained",null,{_getSibling:function(_2ac){
var p=this.getParent();
return (p&&p._getSiblingOfChild&&p._getSiblingOfChild(this,_2ac=="previous"?-1:1))||null;
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
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_2ad,_2ae,_2af){
return _2af("dijit.Destroyable",null,{destroy:function(_2b0){
this._destroyed=true;
},own:function(){
var _2b1=["destroyRecursive","destroy","remove"];
_2ad.forEach(arguments,function(_2b2){
var _2b3;
var odh=_2ae.before(this,"destroy",function(_2b4){
_2b2[_2b3](_2b4);
});
var hdhs=[];
function _2b5(){
odh.remove();
_2ad.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_2b2.then){
_2b3="cancel";
_2b2.then(_2b5,_2b5);
}else{
_2ad.forEach(_2b1,function(_2b6){
if(typeof _2b2[_2b6]==="function"){
if(!_2b3){
_2b3=_2b6;
}
hdhs.push(_2ae.after(_2b2,_2b6,_2b5,true));
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
},"dijit/DropDownMenu":function(){
define(["dojo/_base/declare","dojo/keys","dojo/text!./templates/Menu.html","./_MenuBase"],function(_2b7,keys,_2b8,_2b9){
return _2b7("dijit.DropDownMenu",_2b9,{templateString:_2b8,baseClass:"dijitMenu",_onUpArrow:function(){
this.focusPrev();
},_onDownArrow:function(){
this.focusNext();
},_onRightArrow:function(evt){
this._moveToPopup(evt);
evt.stopPropagation();
evt.preventDefault();
},_onLeftArrow:function(evt){
if(this.parentMenu){
if(this.parentMenu._isMenuBar){
this.parentMenu.focusPrev();
}else{
this.onCancel(false);
}
}else{
evt.stopPropagation();
evt.preventDefault();
}
}});
});
},"dijit/MenuItem":function(){
define(["dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/_base/kernel","dojo/sniff","dojo/_base/lang","./_Widget","./_TemplatedMixin","./_Contained","./_CssStateMixin","dojo/text!./templates/MenuItem.html"],function(_2ba,dom,_2bb,_2bc,_2bd,has,lang,_2be,_2bf,_2c0,_2c1,_2c2){
var _2c3=_2ba("dijit.MenuItem"+(has("dojo-bidi")?"_NoBidi":""),[_2be,_2bf,_2c0,_2c1],{templateString:_2c2,baseClass:"dijitMenuItem",label:"",_setLabelAttr:function(val){
this._set("label",val);
var _2c4="";
var text;
var ndx=val.search(/{\S}/);
if(ndx>=0){
_2c4=val.charAt(ndx+1);
var _2c5=val.substr(0,ndx);
var _2c6=val.substr(ndx+3);
text=_2c5+_2c4+_2c6;
val=_2c5+"<span class=\"dijitMenuItemShortcutKey\">"+_2c4+"</span>"+_2c6;
}else{
text=val;
}
this.domNode.setAttribute("aria-label",text+" "+this.accelKey);
this.containerNode.innerHTML=val;
this._set("shortcutKey",_2c4);
},iconClass:"dijitNoIcon",_setIconClassAttr:{node:"iconNode",type:"class"},accelKey:"",disabled:false,_fillContent:function(_2c7){
if(_2c7&&!("label" in this.params)){
this._set("label",_2c7.innerHTML);
}
},buildRendering:function(){
this.inherited(arguments);
var _2c8=this.id+"_text";
_2bb.set(this.containerNode,"id",_2c8);
if(this.accelKeyNode){
_2bb.set(this.accelKeyNode,"id",this.id+"_accel");
}
dom.setSelectable(this.domNode,false);
},onClick:function(){
},focus:function(){
try{
if(has("ie")==8){
this.containerNode.focus();
}
this.focusNode.focus();
}
catch(e){
}
},_setSelected:function(_2c9){
_2bc.toggle(this.domNode,"dijitMenuItemSelected",_2c9);
},setLabel:function(_2ca){
_2bd.deprecated("dijit.MenuItem.setLabel() is deprecated.  Use set('label', ...) instead.","","2.0");
this.set("label",_2ca);
},setDisabled:function(_2cb){
_2bd.deprecated("dijit.Menu.setDisabled() is deprecated.  Use set('disabled', bool) instead.","","2.0");
this.set("disabled",_2cb);
},_setDisabledAttr:function(_2cc){
this.focusNode.setAttribute("aria-disabled",_2cc?"true":"false");
this._set("disabled",_2cc);
},_setAccelKeyAttr:function(_2cd){
if(this.accelKeyNode){
this.accelKeyNode.style.display=_2cd?"":"none";
this.accelKeyNode.innerHTML=_2cd;
_2bb.set(this.containerNode,"colSpan",_2cd?"1":"2");
}
this._set("accelKey",_2cd);
}});
if(has("dojo-bidi")){
_2c3=_2ba("dijit.MenuItem",_2c3,{_setLabelAttr:function(val){
this.inherited(arguments);
if(this.textDir==="auto"){
this.applyTextDir(this.textDirNode);
}
}});
}
return _2c3;
});
},"dijit/_Container":function(){
define(["dojo/_base/array","dojo/_base/declare","dojo/dom-construct","dojo/_base/kernel"],function(_2ce,_2cf,_2d0,_2d1){
return _2cf("dijit._Container",null,{buildRendering:function(){
this.inherited(arguments);
if(!this.containerNode){
this.containerNode=this.domNode;
}
},addChild:function(_2d2,_2d3){
var _2d4=this.containerNode;
if(_2d3>0){
_2d4=_2d4.firstChild;
while(_2d3>0){
if(_2d4.nodeType==1){
_2d3--;
}
_2d4=_2d4.nextSibling;
}
if(_2d4){
_2d3="before";
}else{
_2d4=this.containerNode;
_2d3="last";
}
}
_2d0.place(_2d2.domNode,_2d4,_2d3);
if(this._started&&!_2d2._started){
_2d2.startup();
}
},removeChild:function(_2d5){
if(typeof _2d5=="number"){
_2d5=this.getChildren()[_2d5];
}
if(_2d5){
var node=_2d5.domNode;
if(node&&node.parentNode){
node.parentNode.removeChild(node);
}
}
},hasChildren:function(){
return this.getChildren().length>0;
},_getSiblingOfChild:function(_2d6,dir){
var _2d7=this.getChildren(),idx=_2ce.indexOf(_2d7,_2d6);
return _2d7[idx+dir];
},getIndexOfChild:function(_2d8){
return _2ce.indexOf(this.getChildren(),_2d8);
}});
});
},"url:dijit/templates/MenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,textDirNode\"\n\t\trole=\"presentation\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">\n\t\t<span data-dojo-attach-point=\"arrowWrapper\" style=\"visibility: hidden\">\n\t\t\t<span class=\"dijitInline dijitIcon dijitMenuExpand\"></span>\n\t\t\t<span class=\"dijitMenuExpandA11y\">+</span>\n\t\t</span>\n\t</td>\n</tr>\n","url:dijit/templates/Menu.html":"<table class=\"dijit dijitMenu dijitMenuPassive dijitReset dijitMenuTable\" role=\"menu\" tabIndex=\"${tabIndex}\"\n\t   cellspacing=\"0\">\n\t<tbody class=\"dijitReset\" data-dojo-attach-point=\"containerNode\"></tbody>\n</table>\n","url:dijit/templates/MenuSeparator.html":"<tr class=\"dijitMenuSeparator\" role=\"separator\">\n\t<td class=\"dijitMenuSeparatorIconCell\">\n\t\t<div class=\"dijitMenuSeparatorTop\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n\t<td colspan=\"3\" class=\"dijitMenuSeparatorLabelCell\">\n\t\t<div class=\"dijitMenuSeparatorTop dijitMenuSeparatorLabel\"></div>\n\t\t<div class=\"dijitMenuSeparatorBottom\"></div>\n\t</td>\n</tr>\n","url:dijit/templates/CheckedMenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"${role}\" tabIndex=\"-1\" aria-checked=\"${checked}\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span class=\"dijitInline dijitIcon dijitMenuItemIcon dijitCheckedMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t\t<span class=\"dijitMenuItemIconChar dijitCheckedMenuItemIconChar\">${!checkedChar}</span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,labelNode,textDirNode\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">&#160;</td>\n</tr>\n"}});
define("dojo/menu-layer",[],1);
