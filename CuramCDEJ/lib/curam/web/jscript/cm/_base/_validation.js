//>>built
define("cm/_base/_validation",[],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{validation:{messages:{},strings:{},validationFns:{},addLocalizedString:function(id,_1){
cm.validation.strings[id]=_1;
},getLocalizedString:function(id){
return cm.validation.strings[id]||"";
},addMessage:function(_2,_3,_4){
_2=cm.validation._getInputName(_2);
var _5=cm.getFormItems().getLabel(_2);
if(!_5){
return;
}
if(_4){
_3=cm.validation.getLocalizedString(_3);
}
cm.validation.messages[_2]=cm.validation.replaceTokens(_3,_5);
dojo.publish(cm.topics.VALIDATION_MSG_ADDED,[{name:_2,msg:_3}]);
},removeMessage:function(_6){
_6=cm.validation._getInputName(_6);
if(!cm.getFormItems().getLabel(_6)){
return;
}
delete cm.validation.messages[_6];
dojo.publish(cm.topics.VALIDATION_MSG_REMOVED,[_6]);
},getMessage:function(_7,_8){
_7=cm.validation._getInputName(_7);
if(_8){
cm.validation._refreshValidation(_7);
}
return cm.validation.messages[_7];
},getAllMessages:function(_9,_a){
var _b=[];
var _c=cm.validation.messages;
if(_a){
for(var _d in _c){
cm.validation._refreshValidation(_d);
}
}
for(var _d in _c){
if(_9){
_b.push({input:dojo.query("input[name='"+_d+"'],select[name='"+_d+"']")[0],msg:_c[_d]});
}else{
_b.push(_c[_d]);
}
}
return _b;
},registerValidation:function(_e,fn,_f){
cm.validation.validationFns[_e]={fn:fn,node:_f};
},validateMandatory:function(_10,_11){
var n=_10;
var v=cm.validation;
v._checkMandatory(_10);
switch(_11){
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
var _12=dojo.query("input[name='"+n.getAttribute("name")+"']");
_12.forEach(function(_13){
_13._siblings=_12;
});
}
if(n.checked){
v.removeMessage(n);
}else{
var _14=false;
n._siblings.forEach(function(_15){
if(_15.checked){
_14=true;
}
});
if(!_14){
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
},_refreshValidation:function(_16){
var _17=cm.validation.validationFns[_16];
if(_17){
_17.fn(_17.node);
}
},_checkMandatory:function(_18){
if(typeof (_18._mandatory)!="undefined"){
return;
}
_18._mandatory=cm.getFormItems().isMandatory(_18.getAttribute("name"));
},_getInputName:function(_19){
return dojo.isString(_19)?_19:_19.getAttribute("name");
}}});
return cm.validation;
});
