//>>built
define("curam/pagination/DefaultListModel",["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/query","curam/debug","curam/pagination"],function(_1,_2,_3,_4,_5,_6){
var _7=_1("curam.pagination.DefaultListModel",null,{_rowCount:null,constructor:function(_8){
this.tableNode=_4("table.paginated-list-id-"+_8)[0];
if(!this.tableNode){
throw "Table node for ID "+_8+" not found - failing!";
}
_5.log("curam.pagination.DefaultListModel "+_5.getProperty("curam.pagination.DefaultListModel"),this.tableNode);
this._id=_8;
},getId:function(){
return this._id;
},getRowCount:function(){
if(this._rowCount==null){
this._rowCount=0;
var _9=_4("tbody > script.hidden-list-rows",this.tableNode),_a=document.createDocumentFragment();
for(var i=0;i<_9.length;i++){
var _b=_9[i];
var _c=(i==_9.length-1);
if(!_c){
this._rowCount+=_6.getNumRowsInBlock(_b);
}else{
_6.unpackRows(_b,_a);
_b.innerHTML="";
}
}
this.tableNode.tBodies[0].appendChild(_a);
var _d=_4("tbody > tr",this.tableNode).length;
this._rowCount+=_d;
}
return this._rowCount;
},hideRange:function(_e,_f){
var _10=this._getRowNodes(_e,_f);
for(var i=_e;i<=_f;i++){
_3.set(_10[i-1],{"display":"none"});
_2.remove(_10[i-1],"even-last-row");
_2.remove(_10[i-1],"odd-last-row");
}
},showRange:function(_11,_12){
var _13=this._getRowNodes(_11,_12);
var _14=(_12%2==0)?"even-last-row":"odd-last-row";
_2.add(_13[_12-1],_14);
for(var i=_11;i<=_12;i++){
_3.set(_13[i-1],{"display":""});
}
dojo.publish("curam/update/pagination/rows",[_13,this.getId()]);
},_getRowNodes:function(_15,_16){
var _17=_6.readListContent(this.tableNode);
for(var i=1;i<=_16&&i<=_17.length;i++){
var _18=_17[i-1];
if(_18.tagName=="SCRIPT"){
_6.unpackRows(_18);
_17=_6.readListContent(this.tableNode);
i--;
}
}
return _4("tbody > tr",this.tableNode);
}});
return _7;
});
