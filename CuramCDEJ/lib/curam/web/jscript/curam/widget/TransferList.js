//>>built
define("curam/widget/TransferList",["dijit/_Widget","dojo/_base/declare","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","dojo/dom","dojo/query","curam/debug"],function(_1,_2,_3,_4,_5,_6,_7,_8){
var _9=_2("curam.widget.TransferList",dijit._Widget,{btnNames:["allRight","toRight","toLeft","allLeft"],btnValues:[" "," "," "," "],bntClasses:["allRight","toRight","toLeft","allLeft"],rightEmptyText:"",widgetType:"TransferList",postCreate:function(){
var _a=this.domNode.parentNode;
_3.add(_a,"transferlistparent");
var _b=_7(this.domNode).next()[0];
this.leftList=this.domNode;
var _c=_5.create("table",{"class":"transfer-list",role:"presentation"});
var _d=_5.create("tbody",{},_c);
var _e=_5.create("tr",{},_d);
var _f=_5.create("td");
var _10=_5.create("td",{"class":"controls"});
var _11=this;
function _12(_13){
return function(){
_11.setSelection(_13);
return false;
};
};
function _14(id){
return function(){
_3.add(_6.byId(id),"active");
return false;
};
};
function _15(id){
return function(){
_3.remove(_6.byId(id),"active");
return false;
};
};
for(j=0;j<4;j++){
var _16=_5.create("div",{},_10);
var _17=new Array(LOCALISED_TRANSFER_LIST_RA,LOCALISED_TRANSFER_LIST_R,LOCALISED_TRANSFER_LIST_L,LOCALISED_TRANSFER_LIST_LA);
var btn=_5.create("input",{type:"button",id:this.btnNames[j]+this.domNode.name,value:this.btnValues[j],"class":this.bntClasses[j],"title":_17[j]},_16);
btn.listtwins=this;
dojo.connect(btn,"onclick",_12(btn.id));
dojo.connect(btn,"onmousedown",_14(btn.id));
dojo.connect(btn,"onmouseup",_15(btn.id));
dojo.connect(btn,"onmouseout",_15(btn.id));
}
var _18=document.createElement("td");
var _19=_5.create("select",{id:this.domNode.name,name:this.domNode.name,title:LOCALISED_TRANSFER_LIST_SELECTED_ITEMS,multiple:"multiple","class":"selected",size:5},_18);
_4.set(this.domNode,{name:"__o3ign."+_19.name,id:"__o3ign."+_19.name,"class":"selected",size:5});
this.rightList=_19;
dojo.connect(this.leftList,"ondblclick",_12("toRight"));
dojo.connect(this.rightList,"ondblclick",_12("toLeft"));
function _1a(_1b){
return function(evt){
if(evt.keyCode==evt.KEY_ENTER){
_11.setSelection(_1b);
}
return false;
};
};
dojo.connect(this.leftList,"onkeydown",_1a("toRight"));
dojo.connect(this.rightList,"onkeydown",_1a("toLeft"));
_f.appendChild(this.domNode);
_e.appendChild(_f);
_e.appendChild(_10);
_e.appendChild(_18);
if(_b){
_a.insertBefore(_c,_b);
}else{
_a.appendChild(_c);
}
this.setInitialSelection();
this.adjustEmpties(this.leftList,this.rightList);
var _1c=_7(this.domNode).closest("form")[0];
if(!_1c){
_8.log("curam.widget.TransferList "+_8.getProperty("curam.widget.TransferList.msg"));
return;
}
dojo.connect(_1c,"onsubmit",function(){
var _1d=_11.rightList;
var _1e=new Array();
for(k1=0;k1<_1d.options.length;k1++){
_1e[_1e.length]=_1d.options[k1];
}
_1d.options.length=0;
for(k2=0;k2<_1e.length;k2++){
_1e[k2].selected=true;
_1d.appendChild(_1e[k2]);
}
});
dojo.connect(window,"onresize",this.selectWidthSetting);
dojo.addOnLoad(this.selectWidthSetting);
var _1f=function(_20){
for(var i=0;i<_20.options.length;i++){
if(_20.options[i].text){
_20.options[i].title=_20.options[i].text;
}
}
};
_1f(_11.leftList);
_1f(_11.rightList);
},setSelection:function(id){
var _21=(id.indexOf("all")>-1);
var _22=(id.indexOf("Right")>-1)?this.leftList:this.rightList;
var _23=(id.indexOf("Left")>-1)?this.leftList:this.rightList;
if(_22.options[0]!=null&&_22.options[0].text!=this.rightEmptyText){
if(_23.options[0]!=null&&(_23.options[0].text==this.rightEmptyText||_23.options[0].text=="")){
_23.options[0]=null;
}
this.transferOptions(_22,_23,_21);
this.adjustEmpties(this.leftList,this.rightList);
}
},setInitialSelection:function(){
this.transferOptions(this.leftList,this.rightList,false);
},adjustEmpties:function(_24,_25){
if(_25.options.length==0){
_25.options[0]=new Option(this.rightEmptyText,"",false,false);
}
},transferOptions:function(_26,_27,_28){
if(_26&&_27){
var _29=new Array();
dojo.forEach(_26.options,function(opt){
if(_28||opt.selected){
_29[_29.length]=opt;
}
});
this.appendAll(_27,_29);
}
},appendAll:function(_2a,_2b){
for(var i=0;i<_2b.length;i++){
_2b[i].selected=true;
_2a.appendChild(_2b[i]);
}
},selectWidthSetting:function(){
if(dojo.query(".transfer-list select.selected")){
dojo.query(".transfer-list select.selected").forEach(function(_2c){
var _2d=_2c.parentNode.clientWidth;
_2c.style.width=_2d+"px";
});
}
}});
return _9;
});
