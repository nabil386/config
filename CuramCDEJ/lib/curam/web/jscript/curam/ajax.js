//>>built
define("curam/ajax",["curam/util/Request"],function(_1){
var _2=function(_3,_4){
this.target=_3;
this.inputProvider=_4||"null";
};
var _5={doRequest:function(_6,_7,_8,_9){
var _a="../servlet/JSONServlet";
var _b=this;
if(_8){
_a="../"+_a;
}
var _c={caller:this.target.id,operation:_6,inputProvider:this.inputProvider,args:_7};
function _d(_e,_f){
_e=dojo.fromJson(_e);
if(_e instanceof Array){
if(_e.length>1){
if(_f=="getCodeTableSubset"){
_b.fillCTWithBlank(_e);
}else{
_b.fillCT(_e);
}
}else{
if(_f=="getCodeTableSubset"){
_b.fillCTWithBlank(_e);
}else{
_b.fillSingle(_e,true);
}
}
}else{
_b.fillSingle(_e);
}
};
_1.post({url:_a,handleAs:"text",load:function(_10,evt){
_d(_10,_6);
},error:function(){
alert("error");
},content:{"content":dojo.toJson(_c)},preventCache:true,sync:_9});
},fillCT:function(_11){
this.target.options.length=0;
for(var i=0;i<_11.length;i++){
this.target.options[i]=new Option(_11[i]["descr"],_11[i]["code"],_11[i]["default"]);
}
},fillCTWithBlank:function(_12){
this.target.options.length=0;
this.target.options[0]=new Option("");
for(var i=0;i<_12.length;i++){
this.target.options[i+1]=new Option(_12[i]["descr"],_12[i]["code"]);
}
},fillSingle:function(_13,_14){
if(_14){
this.target.value=_13[0]["value"];
}else{
this.target.value=_13["value"];
}
}};
dojo.mixin(_2.prototype,_5);
dojo.global.AJAXCall=_2;
return _2;
});
