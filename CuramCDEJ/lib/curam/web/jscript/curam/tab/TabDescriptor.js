//>>built
define("curam/tab/TabDescriptor",["dojo/_base/declare","curam/tab/TabSessionManager","curam/debug"],function(_1,_2,_3){
var _4=_1("curam.tab.TabDescriptor",null,{constructor:function(_5,_6){
this.sectionID=_5?_5:null;
this.tabID=_6?_6:null;
this.tabSignature=null;
this.tabContent=null;
this.tabParamNames=null;
this.isHomePage=false;
this.foregroundTabContent=null;
this.restoreModalInd=false;
},toJson:function(){
var _7={"sectionID":this.sectionID,"tabID":this.tabID,"tabSignature":this.tabSignature,"tabParamNames":this.tabParamNames,"isHomePage":this.isHomePage};
_7.tabContent=this.tabContent?this.tabContent:null;
_7.foregroundTabContent=this.foregroundTabContent?this.foregroundTabContent:null;
_7.restoreModalInd=this.restoreModalInd?this.restoreModalInd:false;
return dojo.toJson(_7);
},setTabContent:function(_8,_9){
if(this.tabContent){
this._log(_3.getProperty("curam.tab.TabDescriptor.content.changed"));
}else{
this._log(_3.getProperty("curam.tab.TabDescriptor.content.set"));
}
var _a=dojo.clone(_8.parameters);
dojo.mixin(_a,_8.cdejParameters);
if(!this.tabContent){
this.tabContent={};
}
this.tabContent.parameters=_a;
this.tabContent.pageID=_8.pageID;
if(_9){
this.tabContent.tabName=_9;
}else{
if(!this.tabContent.tabName){
this.tabContent.tabName="";
}
}
this._save();
dojo.publish("/curam/tab/labelUpdated");
},setTabSignature:function(_b,_c,_d){
if(!this.tabSignature){
this.tabParamNames=_b.slice(0);
this.tabParamNames.sort();
this.tabSignature=this._generateSignature(this.tabID,this.tabParamNames,_c);
this._log(_3.getProperty("curam.tab.TabDescriptor.signature.set"));
this._save();
if(!_d){
this._select();
}
}else{
this._log(_3.getProperty("curam.tab.TabDescriptor.signature.not.set"));
}
},matchesPageRequest:function(_e){
return this.tabSignature&&this.tabSignature==this._generateSignature(this.tabID,this.tabParamNames,_e);
},_generateSignature:function(_f,_10,_11){
var _12=_f;
if(_10){
for(var i=0;i<_10.length;i++){
var _13=_10[i];
if(_11.parameters[_13]){
_12+="|"+_13+"="+_11.parameters[_13];
}
}
}
return _12;
},_save:function(){
if(this.tabContent&&this.tabSignature){
this._log(_3.getProperty("curam.tab.TabDescriptor.saving"));
new _2().tabUpdated(this);
}
},_select:function(){
if(this.tabSignature){
this._log(_3.getProperty("curam.tab.TabDescriptor.selecting"));
new _2().tabSelected(this);
}
},_log:function(msg){
if(curam.debug.enabled()){
curam.debug.log("TAB DESCRIPTOR: "+msg+" ["+this.toJson()+"]");
}
}});
dojo.mixin(curam.tab.TabDescriptor,{fromJson:function(_14){
var _15=null;
if(_14){
var _16=dojo.fromJson(_14);
var _15=new curam.tab.TabDescriptor(_16.sectionID,_16.tabID);
if(_16.tabSignature){
_15.tabSignature=_16.tabSignature;
}
if(_16.tabContent){
_15.tabContent=_16.tabContent;
}
if(_16.tabParamNames){
_15.tabParamNames=_16.tabParamNames;
}
if(_16.isHomePage){
_15.isHomePage=_16.isHomePage;
}
if(_16.foregroundTabContent){
_15.foregroundTabContent=_16.foregroundTabContent;
}
if(_16.restoreModalInd){
_15.restoreModalInd=_16.restoreModalInd;
}
}
return _15;
}});
return _4;
});
