//>>built
define("curam/util/ui/form/CuramFormsAPI",["curam/define","curam/debug"],function(_1,_2){
curam.define.singleton("curam.util.ui.form.CuramFormsAPI",{pageID:"",_PATH:"path",_VALUE:"value",_formComponentMap:{},_expectedComponentCount:0,_ready:false,isEnabled:function(){
return curam.util.getTopmostWindow().CURAM_FORMS_API_ENABLED==="true";
},incrementExpectedComponentsCount:function(_3,_4,_5){
this._expectedComponentCount+=1;
curam.debug.log("curam.util.CuramFormsAPI: "+_2.getProperty("curam.util.CuramFormsAPI.expectedComponent"),[this._expectedComponentCount,_4]);
},getComponentCount:function(){
return this._expectedComponentCount;
},isReady:function(){
return this._ready;
},registerComponent:function(_6){
if(!this.isEnabled()){
return;
}
if(!this._formComponentMap[_6.pathID]){
this._formComponentMap[_6.pathID]=_6;
}
if(_6.addChangeListener&&_6.addChangeListener instanceof Function){
_6.addChangeListener(this._handleComponentChanges.bind(this));
}
var _7=Object.keys(this._formComponentMap).length;
curam.debug.log("curam.util.CuramFormsAPI: "+_2.getProperty("curam.util.CuramFormsAPI.registerComponent"),[_7,_6.pathID]);
if(_7===this._expectedComponentCount){
this._ready=true;
curam.util.getTopmostWindow().dojo.publish("curam/util/CuramFormsAPI/ready");
}
},setFormFields:function(_8,_9){
if(!this.isEnabled()){
return;
}
if(this.isReady()){
this._setFormFieldsInternal(_8,_9);
}else{
this._formPageID=_8;
this._formData=_9;
var _a=curam.util.getTopmostWindow().dojo.subscribe("curam/util/CuramFormsAPI/ready",this,function(){
this._setFormFieldsInternal(this._formPageID,this._formData);
curam.util.getTopmostWindow().dojo.unsubscribe(_a);
});
}
},_setFormFieldsInternal:function(_b,_c){
var _d=_c.data;
for(var _e in this._formComponentMap){
var _f=this._formComponentMap[_e];
if(this._formComponentMap.hasOwnProperty(_e)&&_f){
var _10=_e;
var _11=_d.find(function(_12){
return _10===_12._PATH&&_12._VALUE!=="";
});
_11&&this._updateComponentWithStoredData(_11,_f);
}
}
},_updateComponentWithStoredData:function(_13,_14){
_14.setFormElementValue(_13._VALUE);
},_handleComponentChanges:function(_15){
var _16=this.getFormComponentDataAsJSON(_15);
var _17=location.href;
var _18=window.frameElement.id.replace("iframe-","");
curam.util.getTopmostWindow().dojo.publish("curam/util/CuramFormsAPI/formChange",[{data:_16,"href":_17,"frameID":_18}]);
},getFormComponentDataAsJSON:function(){
var _19=[];
for(var key in this._formComponentMap){
var _1a=this._formComponentMap[key];
if(this._formComponentMap.hasOwnProperty(key)&&_1a){
var _1b=key;
_19.push({_PATH:_1b,_VALUE:_1a.getFormElementValue()});
}
}
var _1c=JSON.stringify({data:_19});
return _1c;
},getFormComponentMap:function(){
return this._formComponentMap;
},});
return curam.util.ui.form.CuramFormsAPI;
});
