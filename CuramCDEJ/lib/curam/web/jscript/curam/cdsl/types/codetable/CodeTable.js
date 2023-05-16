//>>built
define("curam/cdsl/types/codetable/CodeTable",["dojo/_base/declare","./CodeTableItem"],function(_1,_2){
var _3=_1(null,{_name:null,_defaultCode:null,_codes:null,_items:null,constructor:function(_4,_5,_6){
this._name=_4;
this._defaultCode=_5;
this._codes=this._parseCodesIntoObject(_6);
},listItems:function(){
this._initItems(this._codes,this._defaultCode);
var _7=[];
for(code in this._items){
_7.push(this._items[code]);
}
return _7;
},_parseCodesIntoObject:function(_8){
var _9={};
if(_8){
for(var i=0;i<_8.length;i++){
var _a=_8[i];
var _b=_a.split(":")[0];
var _c=_a.slice(_b.length+1);
_9[_b]=_c;
}
}
return _9;
},_initItems:function(_d,_e){
if(!this._items){
this._items={};
for(code in _d){
var _f=new _2(code,_d[code]);
_f.isDefault(code===_e);
this._items[code]=_f;
}
}
},getItem:function(_10){
this._initItems(this._codes,this._defaultCode);
return this._items[_10];
}});
return _3;
});
