//>>built
define("curam/util/ui/form/renderer/GenericRendererFormEventsAdapter",["dojo/_base/declare","curam/define"],function(_1){
var _2=_1("curam.util.ui.form.renderer.GenericRendererFormEventsAdapter",null,{elementID:"",pathID:"",element:"",constructor:function(id,_3){
this.elementID=id;
this.pathID=_3;
this.element=document.getElementById(id);
},addChangeListener:function(_4){
this.element.addEventListener("change",_4,this.getFormElement());
},getElementID:function(){
return this.elementID;
},getFormElement:function(){
return this.element;
},setFormElementValue:function(_5){
this.element.value=_5;
},getFormElementValue:function(){
return this.getFormElement().value;
},});
return _2;
});
