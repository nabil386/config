//>>built
define("curam/dcl",["dojo/_base/connect","curam/define","curam/debug","dojo/query","dojo/dom-class","dijit/registry"],function(_1,_2,_3,_4,_5,_6){
_2.singleton("curam.dcl",{CLUSTER_SHOW:true,CLUSTER_HIDE:false,DCL_PAGE_CLUSTER_INITVALS_KEY:"curam.dcl.page_cluster_intvals_key",initialValues:{},rawItems:{},controls:{},clusters:{},states:{},getters:{},clusterRecords:{},fieldRecords:{},togglesArray:[],fnRoot:{"refs":[]},pageLoaded:false,init:function(){
for(var _7 in curam.dcl.clusterRecords){
var cl=_4("."+_7)[0];
if(cl){
curam.dcl.clusters[_7]=cl;
}else{
console.warn("Dynamic cluster",_7," declared on the page but not associated!");
}
}
for(var _8 in curam.dcl.rawItems){
curam.dcl.fieldRecords[_8]=curam.dcl.initRef(curam.dcl.rawItems[_8]);
}
for(var _9 in curam.dcl.fieldRecords){
curam.dcl.controls[_9]=curam.dcl.fieldRecords[_9];
}
dojo.subscribe("curam/dcl/execute",curam.dcl.evaluateExpression);
curam.dcl.evaluateExpression(false);
for(var _a in curam.dcl.clusterRecords){
curam.dcl.transferValuesFromDomToStorage(_a);
if(curam.dcl.states[_a]==curam.dcl.CLUSTER_HIDE){
curam.dcl.blankFields(_a);
}
}
},addControlVar:function(_b,_c){
curam.dcl.fieldRecords[_b]=curam.dcl.initVar(_c);
},addControlRef:function(_d,_e){
curam.dcl.fieldRecords[_d]=curam.dcl.initRef(_e);
},bindCluster:function(_f,_10,_11){
var _12=dojo.getObject(_11);
var _13={"fnRef":_12,"clId":_f};
if(_10!=""){
var _14=dojo.getObject(_10);
if(!_14.refs){
_14.refs=[];
}
_14.refs.push(_13);
}else{
curam.dcl.fnRoot.refs.push(_13);
}
curam.dcl.clusterRecords[_f]=_12;
curam.dcl.states[_f]=curam.dcl.CLUSTER_HIDE;
},setGetter:function(_15,_16){
curam.dcl.getters[_15]=_16;
},getField:function(_17){
if(!_17){
throw Error("You must specify a field name");
}
var _18=undefined;
try{
_18=curam.dcl.controls[_17].apply();
}
catch(e){
_3.log(_3.getProperty("curam.dcl.field.error")+_17);
_3.log(_3.getProperty("curam.dcl.field.valid",[_17]));
}
if(_18){
curam.debug.log("curam.dcl.getField("+_17+") - "+_18);
}
return _18;
},evaluateRefs:function(_19){
for(var i=0;i<_19.length;i++){
var _1a=_19[i];
var _1b=_1a.fnRef.apply();
if(_1b!=curam.dcl.states[_1a.clId]){
curam.dcl.togglesArray.push(_1a.clId);
}
if(_1a.fnRef.refs!=null){
curam.dcl.evaluateRefs(_1a.fnRef.refs);
}
}
},evaluateExpression:function(_1c){
curam.dcl.pageLoaded=false;
if(!_1c){
curam.dcl.pageLoaded=true;
}
curam.dcl.evaluateRefs(curam.dcl.fnRoot.refs);
var _1d=0;
var _1e=1000;
while(curam.dcl.togglesArray.length>0&&_1d<_1e){
_1d++;
for(var i=0;i<curam.dcl.togglesArray.length;i++){
var _1f=_4("."+curam.dcl.togglesArray[i])[0];
curam.dcl.toggleCluster(curam.dcl.pageLoaded,_1f,curam.dcl.togglesArray[i]);
}
curam.dcl.togglesArray=[];
curam.dcl.evaluateRefs(curam.dcl.fnRoot.refs);
}
if(_1d>=_1e){
_3.log(_3.getProperty("curam.dcl.field.inifinte.loop.info",[_1e]));
}
},toggleCluster:function(_20,_21,_22){
curam.dcl.states[_22]=!curam.dcl.states[_22];
if(curam.dcl.states[_22]==curam.dcl.CLUSTER_SHOW){
_5.remove(_21,"hide-dynamic-cluster");
curam.dcl.transferValuesFromStorageToDom(_22);
}else{
_5.add(_21,"hide-dynamic-cluster");
curam.dcl.blankFields(_22);
}
curam.dcl.animateCluster(_20,_21,_22);
},animateCluster:function(_23,_24,_25){
if(!_24||_24.inAnimation){
return;
}
require(["dojo/fx"],function(fx){
var _26={node:_24,duration:200,onBegin:function(){
_24.inAnimation=true;
},onEnd:function(){
_24.inAnimation=false;
}};
var _27={node:_24,duration:200,onBegin:function(){
_24.inAnimation=true;
},onEnd:function(){
_24.inAnimation=false;
}};
if(_23||curam.dcl.states[_25]==curam.dcl.CLUSTER_SHOW){
fx.wipeIn(_26).play();
}else{
if(curam.dcl.states[_25]==curam.dcl.CLUSTER_HIDE){
fx.wipeOut(_27).play();
}else{
}
}
});
},transferValuesFromStorageToDom:function(_28){
var _29=_4("."+_28)[0];
var _2a=curam.dcl.initialValues[_28];
var _2b=_4("table input[value]",_29);
for(var i=0;i<_2b.length;i++){
textNode=_2b[i];
if(_2a){
var _2c=_2a[i].value;
textNode.value=_2c;
if(textNode.id&&_2a[i]._lastValueReported){
var _2d=this.getDropdownWidget(textNode.id);
_2d._lastValueReported=_2a[i]._lastValueReported;
}
}else{
}
}
},transferValuesFromDomToStorage:function(_2e){
var _2f=_4("."+_2e)[0];
var _30=_4("table input[value]",_2f);
var _31=[];
for(var i=0;i<_30.length;i++){
var _32=_30[i];
var _33={id:_32.id,value:_32.value};
var _34=this.getDropdownWidget(_32.id);
if(_34){
_33._lastValueReported=_34._lastValueReported;
}
_31.push(_33);
}
curam.dcl.initialValues[_2e]=_31;
},blankFields:function(_35){
var _36=_4("."+_35)[0];
var _37=_4("table input[value]",_36);
for(var i=0;i<_37.length;i++){
textNode=_37[i];
if(textNode.type==="hidden"&&textNode.getAttribute("dcl-blankable")==="false"){
_3.log(_3.getProperty("curam.dcl.skip.blank"),textNode);
}else{
textNode.value="";
}
if(textNode.type==="checkbox"||textNode.type==="radio"){
textNode.checked=false;
}
var _38=this.getDropdownWidget(textNode.id);
if(_38){
_38._lastValueReported="";
}
}
},getDropdownWidget:function(id){
if(id){
var _39=_6.byId(id);
return _39;
}
},initRef:function(_3a){
var ref=document.getElementsByName(_3a);
var _3b=curam.dcl.getters[_3a];
var _3c=function(){
return ref[0].selectedValue||ref[0].value;
};
if(ref.length>1){
_3c=function(){
for(var i=0;i<ref.length;i++){
if(ref[i].checked){
return ref[i].value;
}
}
return "";
};
}else{
if(ref[0].type=="checkbox"){
_3c=function(){
return document.getElementsByName(_3a)[0].checked;
};
}
}
return function(){
return _3b?_3b.apply(ref):_3c.apply();
};
},initVar:function(_3d){
return function(){
return _3d;
};
}});
return curam.dcl;
});
