//>>built
define("cm/_base/_pageBehaviors",["cm/_base/_behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
cm.registerBehavior("FORM_SINGLE_SUBMIT",{"form":{"onsubmit":function(_1){
if(cm.wasFormSubmitted(_1.target)){
try{
dojo.stopEvent(_1);
}
catch(e){
}
return false;
}
cm.setFormSubmitted(_1.target,true);
}}});
function _2(_3){
return function(_4){
cm.validation.validateMandatory(_4.target?_4.target:_4,_3);
};
};
function _5(_6,_7){
var _8={};
var fn=_2(_6);
dojo.forEach(_7,function(_9){
_8[_9]=fn;
});
_8.found=function(_a){
cm.validation.registerValidation(_a.getAttribute("name"),fn,_a);
fn(_a);
};
return _8;
};
cm.registerBehavior("MANDATORY_FIELD_VALIDATION",{"input[type='text'],input[type='password']":_5("text",["blur","onkeyup"]),"input[type='checkbox']":_5("checkbox",["blur","onclick"]),"select":_5("select",["blur","onchange"]),"input[type='radio']":_5("radio",["blur","onclick"])});
return cm;
});
