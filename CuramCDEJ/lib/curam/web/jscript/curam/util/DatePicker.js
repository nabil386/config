//>>built
define("curam/util/DatePicker",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.DatePicker",null,{constructor:function(){
},setDate:function(_3,_4){
if(_3){
var _5=_3.lastIndexOf("_wrapper")!=-1?_3.substring(0,_3.lastIndexOf("_wrapper")):_3;
curam.util.getTopmostWindow().dojo.publish("/curam/datePicker/setDate",[_5,_4]);
}
}});
return _2;
});
