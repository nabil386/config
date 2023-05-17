//>>built
define("curam/widget/_FormSelectWidget",["dojo/_base/array","dojo/_base/declare","dojo/dom","dojo/dom-class",],function(_1,_2,_3,_4){
var _5=_2("curam.widget._FormSelectWidget",[dijit.form._FormSelectWidget],{_updateSelection:function(){
this.inherited(arguments);
this.focusedChild=null;
this._set("value",this._getValueFromOpts());
var _6=[].concat(this.value);
if(_6&&_6[0]||(this.name=="month"&&_6[0]>=0)){
var _7=this;
_1.forEach(this._getChildren(),function(_8){
var _9=_1.some(_6,function(v){
return _8.option&&(v===_8.option.value);
});
if(_9&&!_7.multiple){
_7.focusedChild=_8;
}
_4.toggle(_8.domNode,this.baseClass.replace(/\s+|$/g,"SelectedOption "),_9);
_8.domNode.setAttribute("aria-selected",_9?"true":"false");
},this);
}
}});
return _5;
});
