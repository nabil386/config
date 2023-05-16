//>>built
define("curam/util/ScreenContext",["dojo/_base/declare"],function(_1){
var _2={DEFAULT_CONTEXT:112,SAMPLE22:2,SAMPLE21:1,SAMPLE13:4,SAMPLE12:2,SAMPLE11:1,EXTAPP:1048576,SMART_PANEL:262144,NESTED_UIM:131072,ORG_TREE:65536,CONTEXT_PANEL:32768,LIST_ROW_INLINE_PAGE:8192,LIST_EVEN_ROW:16384,TAB:4096,TREE:2048,AGENDA:1024,POPUP:512,MODAL:256,HOME:128,HEADER:64,NAVIGATOR:32,FOOTER:16,OVAL:8,RESOLVE:4,ACTION:2,ERROR:1,EMPTY:0};
var _3=[["ERROR","ACTION","RESOLVE","OPT_VALIDATION","FOOTER","NAVIGATOR","HEADER","HOME_PAGE","MODAL","POPUP","AGENDA","TREE","TAB","LIST_EVEN_ROW","LIST_ROW_INLINE_PAGE","CONTEXT_PANEL","ORG_TREE","NESTED_UIM","SMART_PANEL","EXTAPP"],["SAMPLE11","SAMPLE12","SAMPLE13"],["SAMPLE21","SAMPLE22"]];
var _4=_1("curam.util.ScreenContext",null,{constructor:function(_5){
if(_5){
this.setContext(_5);
}else{
this.currentContext=[_2["DEFAULT_CONTEXT"]|_2["DEFAULT_CONTEXT"]];
}
},setContext:function(_6){
var _7=this.setup(_6);
this.currentContext=((_7==null)?([_2["DEFAULT_CONTEXT"]|_2["DEFAULT_CONTEXT"]]):(_7));
},addContextBits:function(_8,_9){
if(!_8){
return;
}
var _a=(_9)?_9:0;
var _b=this.parseContext(_8);
if(_b!=null){
this.currentContext[_a]|=_b;
}
return this.currentContext[_a];
},addAll:function(_c){
var _d=(_c)?_c:0;
this.currentContext[_d]=4294967295;
return this.currentContext[_d];
},clear:function(_e,_f){
if(!_e){
this.clearAll();
return;
}
var _10=(_f)?_f:0;
if(_e==0){
return this.currentContext[_10];
}
var _11=this.parseContext(_e);
if(_11!=null){
var _12=this.currentContext[_10]&_11;
this.currentContext[_10]^=_12;
}
return this.currentContext[_10];
},clearAll:function(idx){
if(idx){
this.currentContext[idx]=0;
}else{
for(var i=0;i<this.currentContext.length;i++){
this.currentContext[i]=0;
}
}
},updateStates:function(_13){
this.clear("ERROR|ACTION|RESOLVE");
this.currentContext[0]=this.currentContext[0]|(_13&7);
},hasContextBits:function(_14,idx){
if(!_14){
return false;
}
var _15=(idx)?idx:0;
var _16=this.parseContext(_14);
if(_16!=null){
var _17=this.currentContext[_15]&_16;
return (_17==_16);
}
return false;
},getValue:function(){
var _18="";
for(var i=0;i<this.currentContext.length;i++){
_18+=this.currentContext[i]+"|";
}
return _18.substring(0,_18.length-1);
},toRequestString:function(){
return "o3ctx="+this.getValue();
},toBinary:function(){
var _19="";
for(var i=0;i<this.currentContext.length;i++){
_19+=this.currentContext[i].toString(2)+"|";
}
return _19.substring(0,_19.length-1);
},toString:function(){
var _1a="";
for(var i=0;i<this.currentContext.length;i++){
var _1b="";
var j=0;
while(j<_3[i].length){
if(((this.currentContext[i]>>j)&1)!=0){
_1b+=","+_3[i][j];
}
j++;
}
if(_1b==""){
return "{}";
}
_1a+="|"+_1b.replace(",","{")+((_1b.length==0)?"":"}");
}
return _1a.substring(1);
},parseContext:function(_1c){
var _1d=_1c.replace(/,/g,"|");
var _1e=_1d.split("|");
var tmp=isNaN(_1e[0])?parseInt(_2[_1e[0]]):_1e[0];
for(var i=1;i<_1e.length;i++){
tmp=tmp|(isNaN(_1e[i])?parseInt(_2[_1e[i]]):_1e[i]);
}
return (isNaN(tmp)?null:tmp);
},setup:function(_1f){
if(!_1f){
return null;
}
var _20=(""+_1f).split("|");
var _21=new Array(_20.length);
for(var i=0;i<_20.length;i++){
_21[i]=this.parseContext(_20[_20.length-i-1]);
_21[i]=_21[i]|_21[i];
if(!_21[i]||isNaN(_21[i])||_21[i]>4294967295){
return null;
}
}
return _21;
}});
return _4;
});
