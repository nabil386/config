//>>built
define("curam/util/ui/form/renderer/TextEditRendererFormEventsAdapter",["dojo/_base/declare","curam/util/ui/form/renderer/GenericRendererFormEventsAdapter"],function(_1,_2){
var _3=_1("curam.util.ui.form.renderer.TextEditRendererFormEventsAdapter",_2,{setFormElementValue:function(_4){
this.element.value=_4;
},getFormElementValue:function(){
return this.getFormElement().value;
},});
return _3;
});
