//>>built
define("curam/util/ui/form/renderer/CheckboxEditRendererFormEventsAdapter",["dojo/_base/declare","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter"],function(_1,_2){
var _3=_1("curam.util.ui.form.renderer.CheckboxEditRendererFormEventsAdapter",_2,{setFormElementValue:function(_4){
this.element.checked=_4;
},getFormElementValue:function(){
return this.getFormElement().checked;
},});
return _3;
});
