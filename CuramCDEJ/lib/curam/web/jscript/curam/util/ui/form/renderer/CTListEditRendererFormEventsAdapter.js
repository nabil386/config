//>>built
define("curam/util/ui/form/renderer/CTListEditRendererFormEventsAdapter",["dojo/_base/declare","dojo/_base/unload","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter","curam/util/Dropdown"],function(_1,_2,_3,_4){
var _5=_1("curam.util.ui.form.renderer.CTListEditRendererFormEventsAdapter",_3,{_unsubscribes:[],constructor:function(id,_6){
this.elementID=id;
this.pathID=_6;
var _7=dojo.subscribe("/curam/comboxbox/initialValue",this,function(_8,_9){
if(this.getFormElement().id===_9){
this.getFormElement().value=_8;
}
_7.remove();
});
_2.addOnUnload(function(){
this._unsubscribes&&this._unsubscribes.forEach(function(hh){
hh.remove();
});
});
},addChangeListener:function(_a){
var _b="curam/util/CuramFormsAPI/formChange/combobox".concat(this.getElementID());
this._unsubscribes.push(dojo.subscribe(_b,this,function(_c){
this.getFormElement().value=_c.value;
_a();
}));
},setFormElementValue:function(_d){
var _e=this;
var _f=dojo.subscribe("curam/modal/component/ready",this,function(){
var _10=new _4();
_10.setSelectedOnDropdownIDByCodevalue(_e.getElementID(),_d);
this.getFormElement().value=_d;
_f.remove();
});
}});
return _5;
});
