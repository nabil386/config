//>>built
define("curam/util/Dropdown",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.Dropdown",null,{constructor:function(){
},updateDropdownItems:function(_3,_4,_5){
if(_4.constructor!==Array||_5.constructor!==Array){
throw new Error("Both "+_4+" and "+_5+" must be an array");
}
if(_4.length!=_5.length){
throw new Error("Both "+_4+" and "+_5+" must have the same number of items");
}
if(window.spm){
window.spm.requireCarbonAddons().then(function(_6){
_6.ComboBox.unmount(window,_3);
});
var _7=[];
for(var i=0;i<_4.length;i++){
item={};
item.id=_3+"_"+_4[i];
item.text=_5[i];
item.value=_4[i];
_7.push(item);
}
labelConfig=curam.util.getTopmostWindow().__dropdownLabelConfig;
var _8=labelConfig?labelConfig:{};
var _9={inputId:_3,items:_7,attributes:{},comboBoxOptions:{},labels:_8,iframeWindow:window};
window.spm.requireCarbonAddons().then(function(_a){
_a.ComboBox.render(_9);
});
}
},resetDropdownToEmpty:function(_b){
this.updateDropdownItems(_b,[],[]);
},setSelectedOnDropdownByDescription:function(_c,_d){
if(_c){
var _e=_c.lastIndexOf("_wrapper")!=-1?_c.substring(0,_c.lastIndexOf("_wrapper")):_c;
dojo.publish("/curam/comboxbox/setSelectedItem",[_e,undefined,_d]);
}
},setSelectedOnDropdownIDByCodevalue:function(_f,_10){
if(_f){
var _11=_f.lastIndexOf("_wrapper")!=-1?_f.substring(0,_f.lastIndexOf("_wrapper")):_f;
curam.util.getTopmostWindow().dojo.publish("/curam/comboxbox/setSelectedItem".concat(_f),[_11,_10]);
}
},setSelectedOnDropdownByCodevalue:function(_12,_13){
if(_12){
var _14=_12.lastIndexOf("_wrapper")!=-1?_12.substring(0,_12.lastIndexOf("_wrapper")):_12;
curam.util.getTopmostWindow().dojo.publish("/curam/comboxbox/setSelectedItem",[_14,_13]);
}
},executeActionFromInitialValueOnDropdown:function(_15,_16){
this.dropdownInputID=_15;
var _17=dojo.subscribe("/curam/comboxbox/initialValue",this,function(val,_18){
if(this.dropdownInputID===_18){
this.__initialValueFromDropdown=val;
_16();
dojo.unsubscribe(_17);
}
});
}});
return _2;
});
