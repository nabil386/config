//>>built
define("curam/ListMap",["dojo/_base/declare"],function(_1){
var _2=_1("curam.ListMap",null,{constructor:function(){
this.keys=new Array();
this.objects=new Array();
this.count=this.keys.length;
},add:function(_3,_4){
if(this.getIndexByKey(_3)>=0){
this.set(_3,_4);
}else{
this.keys.push(_3);
this.count++;
this.objects[_3]=_4;
}
},set:function(_5,_6){
var _7=this.getIndexByKey(_5);
this.keys[_7]=_5;
this.objects[_5]=_6;
},getObjectByIndex:function(_8){
return this.objects[this.keys[_8]];
},getKeyByIndex:function(_9){
return this.keys[_9];
},getObjectByKey:function(_a){
if(this.getIndexByKey(_a)!=-1){
return this.objects[_a];
}
},getIndexByKey:function(_b){
return this.indexOf(_b);
},removeByKey:function(_c){
var _d=this.getIndexByKey(_c);
if(_d>=0&&_d<this.count){
this.count--;
this.keys.splice(_d,1);
this.objects[_c]=null;
}
},removeAtIndex:function(_e){
if(_e>=0&&_e<this.count){
this.count--;
this.keys.splice(_e,1);
}
},indexOf:function(_f){
for(var i=0;i<this.count;i++){
if(this.keys[i]==_f){
return i;
}
}
}});
return _2;
});
