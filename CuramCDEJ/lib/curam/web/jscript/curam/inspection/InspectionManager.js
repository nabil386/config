//>>built
define("curam/inspection/InspectionManager",["dojo/_base/declare","dojo/_base/lang"],function(_1){
var _2=_1("curam.inspection.InspectionManager",null,{inspectors:[],configs:[],modules:[],arrDirectAspects:[],tWinRef:null,constructor:function(_3){
this.tWinRef=_3;
},addInspector:function(_4){
this.addInspectors([_4]);
},addInspectors:function(_5,_6){
for(var i=0,l=_5.length;i<l;i++){
if(this.modules.indexOf(_5[i])==-1){
this.modules.push({"layer":_5[i],"config":_6[i]});
}
}
},load:function(){
for(modBase in this.modules){
var _7=null;
var _8=this.modules[modBase]["layer"]+"/Endpoint";
var _9=this.modules[modBase]["config"];
var _a=this;
require([_8],function(_b){
_7=new _b(_a.tWinRef);
_7.init(_9);
_a.addDirect(_7.getDirectAspects());
_a.inspectors.push({epName:_7});
});
}
},addDirect:function(_c){
if(_c&&_c!=""){
this.arrDirectAspects.push(_c);
}
},getDirects:function(){
return this.arrDirectAspects;
},observe:function(_d,_e){
for(var _f in this.inspectors){
this.inspectors[_f]["epName"].doObserve(_d,_e);
}
}});
return _2;
});
