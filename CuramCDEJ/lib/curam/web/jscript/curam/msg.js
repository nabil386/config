//>>built
define("curam/msg",["dojo/_base/declare","dojo/dom","curam/define","curam/util","curam/debug","curam/date"],function(_1,_2,_3,_4,_5){
_3.singleton("curam.msg",{_cache:{},_msgSetters:[],_msgPublishers:[],_msgHandlerMap:{},_msgSubscribers:{},_currentPublishedTopics:{},publish:function(_6,_7,_8){
var _9=curam.msg._currentPublishedTopics;
if(_9[_6]){
return;
}
_9[_6]=_8;
curam.msg._cache[_8]=_7;
try{
var _a=curam.msg._msgSubscribers[_6];
if(_a){
for(var i=0;i<_a.length;i++){
if(_a[i].srcId==_6){
continue;
}
if(_a[i].setters!=null){
try{
var _b=false;
for(var j=0;j<_a[i].setters.length;j++){
if(_a[i].setters[j].setValue(_a[i].destId,_7)){
_b=true;
break;
}
}
if(_b){
continue;
}
}
catch(e){
_5.log("curam.msg.publish "+_5.getProperty("curam.msg.publish.exception")+e);
}
}
var _c=curam.msg._msgSetters;
var _b=false;
for(var j=0;j<_c.length;j++){
try{
if(_c[j].setValue(_a[i].destId,_7)){
if(!_a[i].setters){
_a[i].setters=[];
}
_a[i].setters.push(_c[j]);
_b=true;
break;
}
}
catch(e){
_5.log("curam.msg.publish "+_5.getProperty("curam.msg.publish.exception")+e);
}
}
if(!_b){
_5.log("curam.msg.publish"+_5.getPropery("curam.msg.publish.unsuscribe.1")+"'"+_7+"'"+_5.getPropery("curam.msg.publish.unsuscribe.2")+"'"+_8+"' "+_5.getPropery("curam.msg.publish.unsuscribe.3")+_6);
_a.splice(i,1);
}
}
}
}
catch(e){
_9[_6]=null;
throw e;
}
_9[_6]=null;
},processingTopicSource:function(id){
},getCached:function(_d){
return curam.msg._cache[_d];
},registerMessageSetter:function(_e){
curam.msg._msgSetters.splice(0,0,_e);
},registerMessagePublisher:function(_f){
curam.msg._msgPublishers.splice(0,0,_f);
},registerMsgSubscribers:function(_10){
var _11=curam.msg._msgSubscribers;
for(var i=0;i<_10.length;i++){
if(!_11[_10[i].destId]){
_11[_10[i].destId]=[];
}
_11[_10[i].destId].push(_10[i]);
if(_10[i].extraSubscribers){
curam.msg.registerMsgSubscribers(_10[i].extraSubscribers);
}
}
}});
_1("curam.msg.Subscriber",null,{constructor:function(){
if(dojo.isArray(destIds)){
this.extraSubscribers=[];
for(var i=1;i<destIds.length;i++){
this.extraSubscribers.push(new curam.msg.Subscriber(srcId,destIds[i]));
}
destIds=destIds[0];
}
this.srcId=srcId;
this.destId=destIds;
var _12=curam.msg._msgPublishers;
for(var i=0;i<_12.length;i++){
if(_12[i].setUp(srcId,destIds)){
break;
}
}
},setters:null});
curam.msg.registerMessagePublisher({setUp:function(_13,_14){
var _15=_2.byId(_13);
if(_15&&input.tagName=="INPUT"&&_15.type&&_15.type.toLowerCase()=="text"){
var _16=function(){
curam.msg.publish(_14,_15.value,_13);
};
curam.util.connect(_15,"onblur",_16);
curam.util.connect(_15,"onkeyup",function(evt){
if(evt.keyCode==evt.KEY_ENTER){
_16();
}
});
if(_15.value&&_15.value.length>0){
dojo.addOnLoad(_16);
}
return true;
}
return false;
}});
curam.msg.registerMessagePublisher({setUp:function(_17,_18){
var _19=dijit.byId(_17);
if(_19&&_19.ns=="curam"){
if(_19.widgetType=="DatePicker"||_19.widgetType=="DatePickerDojo"){
curam.util.connect(_19,"setDate",function(){
if(curam.msg._currentPublishedTopics[_18]){
return;
}
curam.msg.publish(_18,(_19.date?_19.date:_19.value),_17);
});
return true;
}else{
if(_19.widgetType=="DropdownDatePicker"||_19.widgetType=="DropdownDatePickerDojo"){
var _1a=function(){
curam.debug.log("curam.msg.registerMessagePublisher() addWidgetListener");
curam.util.connect(_19.subwidget,"setDate",function(){
if(curam.msg._currentPublishedTopics[_18]){
return;
}
curam.msg.publish(_18,(_19.subwidget.date?_19.subwidget.date:_19.subwidget.value),_17);
});
};
if(_19.subwidget!=null){
_1a();
}else{
var dt=dojo.connect(_19,"init",function(){
_1a();
dojo.disconnect(dt);
});
}
return true;
}
}
}
return false;
}});
curam.msg.registerMessageSetter({id:"InputTextSetter",setValue:function(_1b,_1c){
_5.log("curam.msg.registerMessageSetter: "+_5.getProperty("curam.msg.publish.text.input"));
var _1b=_2.byId(_1b);
if(!_1b||_1b.tagName!="INPUT"||!_1b.type||_1b.type.toLowerCase()!="text"){
_5.log("curam.msg.registerMessageSetter: "+_5.getProperty("curam.msg.publish.text.input.issue")+_1b);
return false;
}
if(_1c instanceof Date){
var df=_1b.getAttribute("dateFormat");
if(!df){
df="d/M/yyyy";
}
_1c=curam.date.formatDate(_1c,df);
}
_1b.value=_1c;
return true;
}});
curam.msg.registerMessageSetter({id:"CuramDatePickerSetter",setValue:function(_1d,_1e){
_5.log("curam.msg.registerMessageSette: "+_5.getProperty("curam.msg.publish.date.input"));
var _1f=dijit.byId(_1d);
if(!_1f||_1f.ns!="curam"||(_1f.widgetType!="DatePicker"&&_1f.widgetType!="DropdownDatePicker")){
_5.log("curam.msg.registerMessageSetter: "+_5.getProperty("curam.msg.publish.date.input.issue")+_1d);
return false;
}
var df=_1f.dateFormat;
_5.log("curam.msg.registerMessageSetter: "+_5.getProperty("curam.msg.publish.date.input.set")+_1d);
if(!(_1e instanceof Date)){
_1e=curam.date.getDateFromFormat(_1e,df);
}
_1f.setDate(_1e);
return true;
}});
curam.msg.registerMessageSetter({id:"CuramDatePickerDojoSetter",setValue:function(_20,_21){
curam.debug.log("curam.msg.registerMessageSetter: curam:DatePickerDojo");
var _22=dijit.byId(_20);
if(!_22||_22.ns!="curam"||(_22.widgetType!="DatePickerDojo"&&_22.widgetType!="DropdownDatePickerDojo")){
_5.log("curam.msg.registerMessageSetter: "+"curam:DatePickerDojo "+_5.getProperty("curam.msg.publish.picker.unable")+_20);
return false;
}
var df=curam.widget.DatePicker.prototype.dateFormat;
_5.log("curam.msg.registerMessageSetter: curam:DatePickerDojo "+_5.getProperty("curam.msg.publish.picker.able")+_20);
if(!(_21 instanceof Date)){
_21=curam.date.getDateFromFormat(_21,df);
_5.log("curam.msg.registerMessageSetter:"+"curam:DatePickerDojo "+_5.getProperty("curam.msg.publish.picker.translated")+_21);
}
_22.setDate(_21);
return true;
}});
return curam.msg;
});
