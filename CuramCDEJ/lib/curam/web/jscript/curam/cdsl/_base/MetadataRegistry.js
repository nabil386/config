//>>built
define("curam/cdsl/_base/MetadataRegistry",["dojo/_base/declare","curam/cdsl/types/codetable/CodeTables","curam/cdsl/types/codetable/CodeTable","dojo/_base/lang"],function(_1,_2,_3,_4){
var _5=_1(null,{_callEntries:null,_codetables:null,constructor:function(){
this._callEntries={};
this._codetables={};
},setFlags:function(_6){
var _7=_6.intf()+"."+_6.method(),_8=!this._callEntries[_7];
_6._sendCodetables(_8);
},update:function(_9){
var _a=_9.request(),_b=_a.intf()+"."+_a.method(),_c=this._callEntries[_b];
if(!_c){
_c={};
this._callEntries[_b]=_c;
}
if(_9.hasCodetables()){
var _d=_9.getCodetablesData();
for(var i=0;i<_d.length;i++){
this._codetables[_d[i].name]=new _3(_d[i].name,_d[i].defaultCode,_d[i].codes);
}
}
},codetables:function(){
return this._codetables;
}});
return _5;
});
