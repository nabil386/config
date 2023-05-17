//>>built
define("curam/widget/IDXSelect",["dojo/_base/declare","dojo/_base/array","dojo/_base/lang","idx/form/Select"],function(_1,_2,_3){
var _4=_1("curam.widget.IDXSelect",idx.oneui.form.Select,{focusSelectedItem:function(){
var _5=this.value;
if(typeof _5!="undefined"){
if(!_3.isArray(_5)){
_5=[_5];
}
var _6=_2.some(this._getChildren(),function(_7){
var _8=false;
if(typeof _7.option!="undefined"){
_8=_5[0]===_7.option.value;
}
if(_8){
this.dropDown.focusChild(_7);
}
return _8;
},this);
if(!_6){
this.dropDown.focusFirstChild();
}
}else{
console.warn("Value is not defined");
}
}});
return _4;
});
