//>>built
define("cm/_base/_form",[],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{checkAll:function(_1,_2){
cm.query("input[type='checkbox']",_2).forEach("item.checked = "+(_1?"true":"false"));
},setFormSubmitted:function(_3,_4){
_3._alreadySubmitted=_4;
},wasFormSubmitted:function(_5){
return _5._alreadySubmitted;
},getFormItems:function(){
if(cm._formItems){
return cm._formItems;
}
var _6=dojo.query("input[name='__o3fmeta']");
var _7=_6.length>0?dojo.fromJson(_6[0].value):{};
var _8=[];
for(var x in _7){
_8.push(x);
}
cm._formItems=new function(){
this.length=function(){
return _8.length;
};
this.getNames=function(){
return _8;
};
this.getInputs=function(_9){
var _a=[];
dojo.forEach(_8,function(_b,_c){
if(!_9||this.isMandatory(_c)){
_a.push("[name='"+_b+"']");
}
},this);
return _a.length>0?dojo.query(_a.join(",")):[];
};
function fn(_d){
return function(_e){
var d=_7[dojo.isString(_e)?_e:_8[_e]];
return d?d[_d]:null;
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
