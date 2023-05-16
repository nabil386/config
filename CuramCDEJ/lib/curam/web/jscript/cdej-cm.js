//>>built
require({cache:{"cm/_base/_behavior":function(){
define(["dojo/behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{behaviors:{},addedBehaviors:{},addBehavior:function(_1){
var b=cm.behaviors[_1];
if(b&&!cm.addedBehaviors[_1]){
dojo.behavior.add(b);
cm.addedBehaviors[_1]=true;
dojo.behavior.apply();
}
},registerBehavior:function(_2,_3){
cm.behaviors[_2]=_3;
}});
return cm;
});
},"cm/_base/_validation":function(){
define([],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{validation:{messages:{},strings:{},validationFns:{},addLocalizedString:function(id,_4){
cm.validation.strings[id]=_4;
},getLocalizedString:function(id){
return cm.validation.strings[id]||"";
},addMessage:function(_5,_6,_7){
_5=cm.validation._getInputName(_5);
var _8=cm.getFormItems().getLabel(_5);
if(!_8){
return;
}
if(_7){
_6=cm.validation.getLocalizedString(_6);
}
cm.validation.messages[_5]=cm.validation.replaceTokens(_6,_8);
dojo.publish(cm.topics.VALIDATION_MSG_ADDED,[{name:_5,msg:_6}]);
},removeMessage:function(_9){
_9=cm.validation._getInputName(_9);
if(!cm.getFormItems().getLabel(_9)){
return;
}
delete cm.validation.messages[_9];
dojo.publish(cm.topics.VALIDATION_MSG_REMOVED,[_9]);
},getMessage:function(_a,_b){
_a=cm.validation._getInputName(_a);
if(_b){
cm.validation._refreshValidation(_a);
}
return cm.validation.messages[_a];
},getAllMessages:function(_c,_d){
var _e=[];
var _f=cm.validation.messages;
if(_d){
for(var _10 in _f){
cm.validation._refreshValidation(_10);
}
}
for(var _10 in _f){
if(_c){
_e.push({input:dojo.query("input[name='"+_10+"'],select[name='"+_10+"']")[0],msg:_f[_10]});
}else{
_e.push(_f[_10]);
}
}
return _e;
},registerValidation:function(_11,fn,_12){
cm.validation.validationFns[_11]={fn:fn,node:_12};
},validateMandatory:function(_13,_14){
var n=_13;
var v=cm.validation;
v._checkMandatory(_13);
switch(_14){
case "text":
case "password":
if(n._mandatory&&(!n.value||dojo.trim(n.value).length==0)){
v.addMessage(n,"MANDATORY_FIELD",true);
}else{
v.removeMessage(n);
}
break;
case "radio":
if(!n._siblings){
var _15=dojo.query("input[name='"+n.getAttribute("name")+"']");
_15.forEach(function(_16){
_16._siblings=_15;
});
}
if(n.checked){
v.removeMessage(n);
}else{
var _17=false;
n._siblings.forEach(function(_18){
if(_18.checked){
_17=true;
}
});
if(!_17){
v.addMessage(n,"MANDATORY_FIELD",true);
}
}
break;
case "checkbox":
if(n._mandatory&&!n.checked){
v.addMessage(n,"MANDATORY_FIELD",true);
}else{
v.removeMessage(n);
}
break;
case "select":
if(n._mandatory&&(!n.value||dojo.trim(n.value).length==0)){
v.addMessage(n,"MANDATORY_FIELD",true);
}else{
v.removeMessage(n);
}
break;
}
},replaceTokens:function(str){
for(var i=0;i<arguments.length;i++){
tok="%"+i+"s";
str=str.split(tok).join("<span class=\"val-msg\">"+arguments[i+1]+"</span>");
}
return str;
},_refreshValidation:function(_19){
var _1a=cm.validation.validationFns[_19];
if(_1a){
_1a.fn(_1a.node);
}
},_checkMandatory:function(_1b){
if(typeof (_1b._mandatory)!="undefined"){
return;
}
_1b._mandatory=cm.getFormItems().isMandatory(_1b.getAttribute("name"));
},_getInputName:function(_1c){
return dojo.isString(_1c)?_1c:_1c.getAttribute("name");
}}});
return cm.validation;
});
},"cm/_base/_pageBehaviors":function(){
define(["cm/_base/_behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
cm.registerBehavior("FORM_SINGLE_SUBMIT",{"form":{"onsubmit":function(evt){
if(cm.wasFormSubmitted(evt.target)){
try{
dojo.stopEvent(evt);
}
catch(e){
}
return false;
}
cm.setFormSubmitted(evt.target,true);
}}});
function _1d(_1e){
return function(evt){
cm.validation.validateMandatory(evt.target?evt.target:evt,_1e);
};
};
function _1f(_20,_21){
var obj={};
var fn=_1d(_20);
dojo.forEach(_21,function(evt){
obj[evt]=fn;
});
obj.found=function(_22){
cm.validation.registerValidation(_22.getAttribute("name"),fn,_22);
fn(_22);
};
return obj;
};
cm.registerBehavior("MANDATORY_FIELD_VALIDATION",{"input[type='text'],input[type='password']":_1f("text",["blur","onkeyup"]),"input[type='checkbox']":_1f("checkbox",["blur","onclick"]),"select":_1f("select",["blur","onchange"]),"input[type='radio']":_1f("radio",["blur","onclick"])});
return cm;
});
},"dojo/behavior":function(){
define(["./_base/kernel","./_base/lang","./_base/array","./_base/connect","./query","./domReady"],function(_23,_24,_25,_26,_27,_28){
_23.deprecated("dojo.behavior","Use dojo/on with event delegation (on.selector())");
var _29=function(){
function _2a(obj,_2b){
if(!obj[_2b]){
obj[_2b]=[];
}
return obj[_2b];
};
var _2c=0;
function _2d(obj,_2e,_2f){
var _30={};
for(var x in obj){
if(typeof _30[x]=="undefined"){
if(!_2f){
_2e(obj[x],x);
}else{
_2f.call(_2e,obj[x],x);
}
}
}
};
this._behaviors={};
this.add=function(_31){
_2d(_31,this,function(_32,_33){
var _34=_2a(this._behaviors,_33);
if(typeof _34["id"]!="number"){
_34.id=_2c++;
}
var _35=[];
_34.push(_35);
if((_24.isString(_32))||(_24.isFunction(_32))){
_32={found:_32};
}
_2d(_32,function(_36,_37){
_2a(_35,_37).push(_36);
});
});
};
var _38=function(_39,_3a,_3b){
if(_24.isString(_3a)){
if(_3b=="found"){
_26.publish(_3a,[_39]);
}else{
_26.connect(_39,_3b,function(){
_26.publish(_3a,arguments);
});
}
}else{
if(_24.isFunction(_3a)){
if(_3b=="found"){
_3a(_39);
}else{
_26.connect(_39,_3b,_3a);
}
}
}
};
this.apply=function(){
_2d(this._behaviors,function(_3c,id){
_27(id).forEach(function(_3d){
var _3e=0;
var bid="_dj_behavior_"+_3c.id;
if(typeof _3d[bid]=="number"){
_3e=_3d[bid];
if(_3e==(_3c.length)){
return;
}
}
for(var x=_3e,_3f;_3f=_3c[x];x++){
_2d(_3f,function(_40,_41){
if(_24.isArray(_40)){
_25.forEach(_40,function(_42){
_38(_3d,_42,_41);
});
}
});
}
_3d[bid]=_3c.length;
});
});
};
};
_23.behavior=new _29();
_28(function(){
_23.behavior.apply();
});
return _23.behavior;
});
},"cm/_base/_dom":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class"],function(dom,_43,_44){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(_45,_46){
return cm._findSibling(_45,_46,true);
},prevSibling:function(_47,_48){
return cm._findSibling(_47,_48,false);
},getInput:function(_49,_4a){
if(!dojo.isString(_49)){
return _49;
}
var _4b=dojo.query("input[name='"+_49+"'],select[name='"+_49+"']");
return _4a?(_4b.length>0?_4b:null):(_4b.length>0?_4b[0]:null);
},getParentByClass:function(_4c,_4d){
_4c=_4c.parentNode;
while(_4c){
if(_44.contains(_4c,_4d)){
return _4c;
}
_4c=_4c.parentNode;
}
return null;
},getParentByType:function(_4e,_4f){
_4e=_4e.parentNode;
_4f=_4f.toLowerCase();
var _50="html";
while(_4e){
if(_4e.tagName.toLowerCase()==_50){
break;
}
if(_4e.tagName.toLowerCase()==_4f){
return _4e;
}
_4e=_4e.parentNode;
}
return null;
},replaceClass:function(_51,_52,_53){
_44.remove(_51,_53);
_44.add(_51,_52);
},setClass:function(_54,_55){
_54=dom.byId(_54);
var cs=new String(_55);
try{
if(typeof _54.className=="string"){
_54.className=cs;
}else{
if(_54.setAttribute){
_54.setAttribute("class",_55);
_54.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
},_findSibling:function(_56,_57,_58){
if(!_56){
return null;
}
if(_57){
_57=_57.toLowerCase();
}
var _59=_58?"nextSibling":"previousSibling";
do{
_56=_56[_59];
}while(_56&&_56.nodeType!=1);
if(_56&&_57&&_57!=_56.tagName.toLowerCase()){
return cm[_58?"nextSibling":"prevSibling"](_56,_57);
}
return _56;
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
},toggleDisplay:function(_5a){
_43.set(_5a,"display",_43.get(_5a,"display")=="none"?"":"none");
},endsWith:function(str,end,_5b){
if(_5b){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_43.set(n,"display","none");
},show:function(n){
_43.set(n,"display","");
}});
return cm;
});
},"cm/_base/_topics":function(){
define([],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{topics:{MANDATORY_FIELD_VALIDATION:"topic_mandatory_field_validation",VALIDATION_MSG_ADDED:"topic_validation_msg_added",VALIDATION_MSG_REMOVED:"topic_validation_msg_removed"}});
return cm.topics;
});
},"cm/_base/_form":function(){
define([],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{checkAll:function(_5c,_5d){
cm.query("input[type='checkbox']",_5d).forEach("item.checked = "+(_5c?"true":"false"));
},setFormSubmitted:function(_5e,_5f){
_5e._alreadySubmitted=_5f;
},wasFormSubmitted:function(_60){
return _60._alreadySubmitted;
},getFormItems:function(){
if(cm._formItems){
return cm._formItems;
}
var _61=dojo.query("input[name='__o3fmeta']");
var _62=_61.length>0?dojo.fromJson(_61[0].value):{};
var _63=[];
for(var x in _62){
_63.push(x);
}
cm._formItems=new function(){
this.length=function(){
return _63.length;
};
this.getNames=function(){
return _63;
};
this.getInputs=function(_64){
var _65=[];
dojo.forEach(_63,function(_66,_67){
if(!_64||this.isMandatory(_67)){
_65.push("[name='"+_66+"']");
}
},this);
return _65.length>0?dojo.query(_65.join(",")):[];
};
function fn(_68){
return function(_69){
var d=_62[dojo.isString(_69)?_69:_63[_69]];
return d?d[_68]:null;
};
};
this.getTargetPath=fn(0);
this.getLabel=fn(1);
this.getDomain=fn(2);
this.isMandatory=fn(3);
};
return cm._formItems;
}});
return cm;
});
}}});
define("dojo/cdej-cm",[],1);
