//>>built
define("curam/widget/componentWrappers/ListWraper",["dojo/_base/declare","dojo/on","dijit/_Widget","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/dom-class","dojo/dom-attr"],function(_1,on,_2,_3,_4,_5,_6,_7){
return _1("curam.widget.componentWrappers.ListWraper",[_2],{baseClass:"navMenu",_listTypeUnordered:"ul",_listTypeOrdered:"ol",listType:this._listTypeOrdered,baseClass:"listWrapper",itemClass:null,itemStyle:null,role:null,buildRendering:function(){
if(this.listType==this._listTypeUnordered){
this.domNode=_3.create("ul");
}else{
this.domNode=_3.create("ol");
_7.set(this.domNode,"tabindex","0");
}
if(this.role!=null){
_7.set(this.domNode,"role",this.role);
}
this.inherited(arguments);
},_setItemAttr:function(_8,_9){
if(_9==null){
_9="last";
}
var _a=_3.create("li",null,this.domNode,_9);
_7.set(_a,"role","menuitem");
_7.set(_a,"tabindex","-1");
this._doBeforeItemSet(_8,_a);
_3.place(_8.domNode?_8.domNode:_8,_a);
this._doAfterItemSet(_8,_a);
if(this.itemStyle){
_5.set(_a,this.itemStyle);
}
if(this.itemClass){
_6.add(_a,this.itemClass);
}
},_doBeforeItemSet:function(_b,_c){
},_doAfterItemSet:function(_d,_e){
},_getItemCountAttr:function(){
return this.domNode.children.length;
},_getContainerHeightAttr:function(){
var _f=_4.getContentBox(this.domNode);
return _f.h;
},getChildElament:function(_10){
var _11=this.domNode.childNodes[_10];
return _11;
},placeItemToPostion:function(_12,_13){
var _14=this.domNode.childNodes[_13];
_3.place(_14,_12);
},deleteChild:function(_15){
var _16=this.getChildElament(_15);
_3.destroy(_16);
},deleteAllChildern:function(){
while(this.domNode.children.length>0){
this.deleteChild(0);
}
}});
});
