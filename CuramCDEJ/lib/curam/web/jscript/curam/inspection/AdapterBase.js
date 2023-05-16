//>>built
define("curam/inspection/AdapterBase",["dojo/_base/declare","dojo/_base/lang","dojo/aspect"],function(_1,_2,_3){
var _4=_1("curam.inspection.AdapterBase",null,{marker:null,seed:0,before:null,after:null,around:null,tWin:null,setTopWindow:function(_5){
this.tWin=_5;
},constructor:function(){
this.before=_3.before;
this.after=_3.after;
this.around=_3.around;
},signal:function(_6,_7){
this.tWin.dojo.publish(_6,_7);
},adapt:function(_8,_9){
if(this.aspects[_8]&&!_9[this.marker]){
this.aspects[_8](_9);
_9[this.marker]=true;
}
},hook:function(_a,_b,_c,_d,_e){
this[_a](_b,_c,_d,_e,this.marker,this.seed++);
}});
return _4;
});
