//>>built
define("curam/inspection/EndpointBase",["dojo/_base/declare","dojo/_base/lang"],function(_1){
var _2=_1("curam.inspection.EndpointBase",null,{reporter:null,adapterName:null,listenersName:null,directAspectsName:null,adapter:null,directAdapter:null,listeners:null,defaultReporterName:"curam/inspection/RawDataDumper",configReporter:null,tWinRef:null,constructor:function(_3){
this.tWinRef=_3;
if(this.adapterName&&this.listenersName){
var me=this;
require([this.adapterName,this.listenersName],function(_4,_5){
me.adapter=_4;
me.adapter.setTopWindow(me.tWinRef);
me.listeners=_5;
});
}
},init:function(_6){
var _7=this.defaultReporterName;
this.configReporter=_6.split(".").join("/");
if(this.configReporter!="default"){
_7=this.configReporter;
}
var me=this;
require([_7],function(_8){
me.reporter=new _8();
});
},getDirectAspects:function(){
return this.directAspectsName;
},doObserve:function(_9,_a){
if(this.adapter){
this.adapter.adapt(_9,_a);
}
},getRef:function(){
return this;
}});
return _2;
});
