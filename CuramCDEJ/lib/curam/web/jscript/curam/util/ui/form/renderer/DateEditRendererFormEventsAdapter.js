//>>built
define("curam/util/ui/form/renderer/DateEditRendererFormEventsAdapter",["dojo/_base/declare","dojo/_base/unload","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter","curam/util/DatePicker"],function(_1,_2,_3,_4){
var _5=_1("curam.util.ui.form.renderer.DateEditRendererFormEventsAdapter",_3,{_unsubscribes:[],constructor:function(id,_6){
this.elementID=id;
this.pathID=_6;
var _7=dojo.subscribe("curam/modal/component/ready",this,function(){
this.element=document.getElementById(this.elementID);
_7.remove();
});
_2.addOnUnload(function(){
this._unsubscribes&&this._unsubscribes.forEach(function(hh){
hh.remove();
});
});
},addChangeListener:function(_8){
var _9="curam/util/CuramFormsAPI/formChange/datePicker".concat(this.getElementID());
this._unsubscribes.push(window.dojo.subscribe(_9,this,function(_a){
this.getFormElement().value=_a.value;
_8();
}));
},setFormElementValue:function(_b){
var _c=this;
this._unsubscribes.push(dojo.subscribe("curam/modal/component/ready",this,function(){
var _d=new _4();
_d.setDate(_c.getElementID(),_b);
this.getFormElement().value=_b;
}));
}});
return _5;
});
