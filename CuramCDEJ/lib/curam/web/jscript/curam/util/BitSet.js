//>>built
define("curam/util/BitSet",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.BitSet",null,{_idCounter:0,constructor:function(){
this.value=[];
this.max=-1;
this.log2=Math.log(2);
this.id=++curam.util.BitSet.prototype._idCounter;
},set:function(_3){
this.max=Math.max(this.max,_3);
var _4=this._getPos(_3,true);
var _5=this.value[_4];
this.value[_4]=this.value[_4]|this._pow(_3);
return _5!=this.value[_4];
},unSet:function(_6){
this.max=Math.max(this.max,_6);
var _7=this._getPos(_6,false);
if(_7<0){
return;
}
var _8=this.value[_7];
this.value[_7]=this.value[_7]&(~this._pow(_6));
if(this.value[_7]==0&&_7==this.value.length-1){
this.value.splice(_7,1);
return true;
}
return _8!=this.value[_7];
},isSet:function(_9){
var _a=this._getPos(_9,false);
return _a>-1&&((this._pow(_9)&this.value[_a])>0);
},isClear:function(){
for(var _b=0;_b<this.value.length;_b++){
if(this.value[_b]>0){
return false;
}
}
return true;
},isSingleSet:function(){
var _c;
var _d=false;
for(var _e=0;_e<this.value.length;_e++){
if(this.value[_e]==0){
continue;
}
_c=Math.log(this.value[_e])/this.log2;
if(_c==Math.floor(_c)&&!_d){
_d=true;
}else{
return false;
}
}
return _d;
},equals:function(_f){
if(!_f||this.value.length!=_f.value.length){
return false;
}
var _10=Math.max(this.value.length,_f.value.length);
for(var _11=0;_11<_10;_11++){
if(_f.value[_11]!=this.value[_11]){
return false;
}
}
return true;
},_getPos:function(_12,_13){
var pos=Math.floor(Number(_12)/31);
while(_13&&this.value.length<=pos){
this.value[this.value.length]=0;
}
return (this.value.length<=pos?-1:pos);
},_pow:function(_14){
return Math.pow(2,Number(_14)%31);
}});
return _2;
});
