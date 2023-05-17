//>>built
define("cm/_base/_dom",["dojo/dom","dojo/dom-style","dojo/dom-class"],function(_1,_2,_3){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(_4,_5){
return cm._findSibling(_4,_5,true);
},prevSibling:function(_6,_7){
return cm._findSibling(_6,_7,false);
},getInput:function(_8,_9){
if(!dojo.isString(_8)){
return _8;
}
var _a=dojo.query("input[name='"+_8+"'],select[name='"+_8+"']");
return _9?(_a.length>0?_a:null):(_a.length>0?_a[0]:null);
},getParentByClass:function(_b,_c){
_b=_b.parentNode;
while(_b){
if(_3.contains(_b,_c)){
return _b;
}
_b=_b.parentNode;
}
return null;
},getParentByType:function(_d,_e){
_d=_d.parentNode;
_e=_e.toLowerCase();
var _f="html";
while(_d){
if(_d.tagName.toLowerCase()==_f){
break;
}
if(_d.tagName.toLowerCase()==_e){
return _d;
}
_d=_d.parentNode;
}
return null;
},replaceClass:function(_10,_11,_12){
_3.remove(_10,_12);
_3.add(_10,_11);
},setClass:function(_13,_14){
_13=_1.byId(_13);
var cs=new String(_14);
try{
if(typeof _13.className=="string"){
_13.className=cs;
}else{
if(_13.setAttribute){
_13.setAttribute("class",_14);
_13.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
},_findSibling:function(_15,_16,_17){
if(!_15){
return null;
}
if(_16){
_16=_16.toLowerCase();
}
var _18=_17?"nextSibling":"previousSibling";
do{
_15=_15[_18];
}while(_15&&_15.nodeType!=1);
if(_15&&_16&&_16!=_15.tagName.toLowerCase()){
return cm[_17?"nextSibling":"prevSibling"](_15,_16);
}
return _15;
},getViewport:function(){
var d=dojo.doc,dd=d.documentElement,w=window,b=dojo.body();
if(dojo.isMozilla){
return {w:dd.clientWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&w.innerWidth){
return {w:w.innerWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&dd&&dd.clientWidth){
return {w:dd.clientWidth,h:dd.clientHeight};
}else{
if(b.clientWidth){
return {w:b.clientWidth,h:b.clientHeight};
}
}
}
}
return null;
},toggleDisplay:function(_19){
_2.set(_19,"display",_2.get(_19,"display")=="none"?"":"none");
},endsWith:function(str,end,_1a){
if(_1a){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_2.set(n,"display","none");
},show:function(n){
_2.set(n,"display","");
}});
return cm;
});
