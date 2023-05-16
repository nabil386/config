//>>built
define("curam/util/EditableList",["dojo/dom-class","dojo/dom-attr","curam/debug","curam/define","dojo/query","dojo/NodeList-traverse"],function(_1,_2,_3){
curam.define.singleton("curam.util.EditableList",{onload:function(_4){
var _5=dojo.query("div.list table tbody td input[type = 'checkbox']");
if(_5[0]==null){
return "Outside List";
}
_5.forEach(function(_6){
curam.debug.log("curam.util.EditableList onload()");
curam.util.EditableList._doToggling(_6,_4);
});
return "In List";
},toggle:function(_7,_8){
curam.debug.log("curam.util.EditableList: "+_3.getProperty("curam.util.EditableList.toggle"));
_7=dojo.fixEvent(_7);
if(!_7.target){
return "Improper Event";
}
var _9=_7.target;
curam.util.EditableList._doToggling(_9,_8);
return "Event Processed";
},_doToggling:function(_a,_b){
var _c=_a;
while(_c&&!_1.contains(_c,"list")){
_c=_c.parentNode;
}
if(_c==null){
return "Outside List";
}
var _d=dojo.query(_a).closest("TR")[0];
if(_b){
curam.util.EditableList._updateCheckboxAccessibility(_d,_b);
}
if(_a.checked==true){
isChecked=true;
curam.debug.log(_3.getProperty("curam.util.EditableList.ticking"));
}else{
isChecked=false;
curam.debug.log(_3.getProperty("curam.util.EditableList.unticking"));
}
if(_d==null){
throw new Error("Exception: The TR node is not found");
}
dojo.query("td > *",_d).forEach(function(_e){
if(_1.contains(_e,"text")){
if(isChecked){
_2.remove(_e,"disabled");
_1.remove(_e,"disabled");
curam.debug.log(_3.getProperty("curam.util.EditableList.enable.field"));
}else{
_2.set(_e,"disabled","disable");
_1.add(_e,"disabled");
curam.debug.log(_3.getProperty("curam.util.EditableList.disable.field"));
}
}else{
if(_1.contains(_e,"codetable")){
var _f=_2.get(_e,"widgetid");
var _10=dijit.byId(_f);
if(isChecked){
_10.set("disabled",false);
curam.debug.log(_3.getProperty("curam.util.EditableList.enable.ct"));
}else{
_10.set("disabled",true);
curam.debug.log(_3.getProperty("curam.util.EditableList.disable.ct"));
}
}
}
});
return "Toggled";
},_updateCheckboxAccessibility:function(tr,_11){
var _12=dojo.query("td",tr);
var _13=_12.parents("table");
var _14=_12[0];
var _15=_12[1];
var _16="";
var _17="";
if(_14&&_14.firstElementChild){
if(_14.firstElementChild.nodeName=="INPUT"&&_14.firstElementChild.getAttribute("type")=="checkbox"){
if(_15.firstChild){
if(_15.firstChild.nodeType==3){
_16=_15.firstChild.textContent;
}else{
if(_14.firstElementChild.nodeName=="INPUT"){
_16=_15.firstElementChild.getAttribute("value");
}
}
}
if(_16&&_16.length>0){
_17=_11+" - "+_16;
}else{
_17=_11;
}
if(_13[0]&&_13[0].getAttribute("summary")){
_17=_13[0].getAttribute("summary")+" - "+_17;
}
_14.firstElementChild.setAttribute("aria-label",_17);
_14.firstElementChild.title=_17;
}
}
return _14;
}});
return curam.util.EditableList;
});
