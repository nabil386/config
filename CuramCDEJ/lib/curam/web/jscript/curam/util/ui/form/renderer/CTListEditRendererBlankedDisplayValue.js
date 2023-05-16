//>>built
define("curam/util/ui/form/renderer/CTListEditRendererBlankedDisplayValue",["dojo/_base/declare","curam/util","curam/ui/ClientDataAccessor"],function(_1,_2){
var _3=_1("curam.util.ui.form.renderer.CTListEditRendererBlankedDisplayValue",null,{topicName:"curam/util/CTListEditRendererBlankedDisplayValue/userBlanked",sessionScopedPath:"/data/scope/session/registeredDropDownWithDefaults",clientDataAccessor:new curam.ui.ClientDataAccessor(),pageID:null,inputID:null,constructor:function(_4,_5){
this.pageID=_4;
this.inputID=_5;
},publishUserHasBlankedValue:function(){
dojo.publish(this.topicName);
},registerUserHasSelectedBlank:function(_6){
var _7=document.getElementById(_6);
var _8=_7.getAttribute("data-defaultused");
if(_8==""){
this.setRegisteredDefaultedDropdownsJsonInSession();
}
},registerUserHasBlankedDefaultValue:function(){
dojo.subscribe(this.topicName,this,function(){
this.setRegisteredDefaultedDropdownsJsonInSession();
});
},resetRegisteredDefaultedDropdownsJsonInSession:function(_9,_a){
var _b=this;
var _c=function(_d){
if(_9){
var _e=JSON.parse(_d);
var _f=_e&&_e.pageID;
if(_9.indexOf(_f+"Page.do")!=-1||_9.indexOf(_f+"Action.do")!=-1||_9.indexOf(_f)!=-1){
_b.clientDataAccessor.set(_b.sessionScopedPath,JSON.stringify({}));
}
}else{
_b.clientDataAccessor.set(_b.sessionScopedPath,JSON.stringify({}));
}
if(_a){
_a();
}
};
this.clientDataAccessor.get(this.sessionScopedPath,_c);
},setRegisteredDefaultedDropdownsJsonInSession:function(){
var _10=this;
var _11={};
_11.inputID=this.inputID;
var _12=function(_13){
var _14=JSON.parse(_13);
if(_14&&_14.pageID==_10.pageID){
var _15=false;
for(var i=0;i<_14.inputIDs.length;i++){
if(_14.inputIDs[i].inputID==_10.inputID){
_15=true;
break;
}
}
if(!_15){
_14.inputIDs.push(_11);
_10.clientDataAccessor.set(_10.sessionScopedPath,JSON.stringify(_14));
}
}else{
var _16=[];
_16.push(_11);
var obj={pageID:_10.pageID,inputIDs:_16};
var _17=JSON.stringify(obj);
_10.clientDataAccessor.set(_10.sessionScopedPath,_17);
}
};
var _18=document.getElementById("mainForm");
if(_18&&_18.getAttribute("onSubmit").indexOf("return dc(this,true")!=-1){
this.clientDataAccessor.get(this.sessionScopedPath,_12);
}
}});
return _3;
});
