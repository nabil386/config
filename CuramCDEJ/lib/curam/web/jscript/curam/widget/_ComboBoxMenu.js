//>>built
define("curam/widget/_ComboBoxMenu",["dojo/_base/declare","dojo/dom-attr","curam/util/ResourceBundle","dijit/form/_ComboBoxMenu"],function(_1,_2,_3){
var _4=new _3("FilteringSelect");
var _5=_1("curam.widget._ComboBoxMenu",[dijit.form._ComboBoxMenu],{_createOption:function(_6,_7){
var _8=this._createMenuItem();
var _9=_7(_6);
if(_9.html){
_8.innerHTML=_9.label;
}else{
_8.appendChild(_8.ownerDocument.createTextNode(_9.label));
}
if(_8.innerHTML==""){
_2.set(_8,"aria-label",_4.getProperty("curam.select.option.blank"));
_2.set(_8,"title",_4.getProperty("curam.select.option.blank"));
}else{
var _a=/<[a-zA-Z\/][^>]*>/g;
var _b=_8.innerHTML;
_8.title=_b.replace(_a,"");
}
return _8;
}});
return _5;
});
