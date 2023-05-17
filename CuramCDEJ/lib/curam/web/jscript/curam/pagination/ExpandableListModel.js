//>>built
define("curam/pagination/ExpandableListModel",["dojo/_base/declare","dojo/dom-class","dojo/dom-style","dojo/query","curam/debug","curam/util/ExpandableLists","curam/pagination"],function(_1,_2,_3,_4,_5,_6,_7){
var _8=_1("curam.pagination.ExpandableListModel",null,{_rowCount:null,constructor:function(_9){
this.tableNode=_4("table.paginated-list-id-"+_9)[0];
if(!this.tableNode){
throw "Table node for ID "+_9+" not found - failing!";
}
_5.log("curam.pagination.ExpandableListModel "+_5.getProperty("curam.pagination.ExpandableListModel"),this.tableNode);
this._id=_9;
},getId:function(){
return this._id;
},getRowCount:function(){
if(this._rowCount==null){
this._rowCount=0;
var _a=dojo.query("tbody > script.hidden-list-rows",this.tableNode),_b=document.createDocumentFragment();
for(var i=0;i<_a.length;i++){
var _c=_a[i];
var _d=(i==_a.length-1);
if(!_d){
this._rowCount+=(_7.getNumRowsInBlock(_c)*2);
}else{
_7.unpackRows(_c,_b);
_c.innerHTML="";
}
}
this.tableNode.tBodies[0].appendChild(_b);
var _e=dojo.query("tbody > tr",this.tableNode).length;
this._rowCount+=_e;
}
if(this._rowCount<=1){
return 1;
}else{
return this._rowCount/2;
}
},hideRange:function(_f,_10){
var _11=this._getRowNodes(_f,_10);
for(var i=_f;i<=_10;i++){
var _12=(2*i)-2;
var _13=(2*i)-1;
_3.set(_11[_12],"display","none");
_2.remove(_11[_12],"even-last-row");
_2.remove(_11[_12],"odd-last-row");
if(_11.length>_13){
var _14=_11[_13];
if(_14){
_14._curam_pagination_expanded=curam.util.ExpandableLists.isDetailsRowExpanded(_14);
curam.util.ExpandableLists.setDetailsRowExpandedState(_11[_12],_14,false);
}
}
}
},showRange:function(_15,_16){
var _17=this._getRowNodes(_15,_16);
var _18=(_16%2==0)?"even-last-row":"odd-last-row";
_2.add(_17[(_16*2)-2],_18);
for(var i=_15;i<=_16;i++){
var _19=(2*i)-2;
var _1a=(2*i)-1;
_3.set(_17[_19],"display","");
if(_17.length>_1a){
var _1b=_17[_1a];
if(_1b){
curam.util.ExpandableLists.setDetailsRowExpandedState(_17[_19],_1b,_1b._curam_pagination_expanded);
}
}
}
dojo.publish("curam/update/pagination/rows",[_17,this.getId()]);
},_getRowNodes:function(_1c,_1d){
var _1e=curam.pagination.readListContent(this.tableNode);
for(var i=1;i<=(_1d*2)&&i<=_1e.length;i++){
var _1f=_1e[i-1];
if(_1f.tagName=="SCRIPT"){
curam.pagination.unpackRows(_1f);
_1e=curam.pagination.readListContent(this.tableNode);
i--;
}
}
return dojo.query("tbody > tr",this.tableNode);
}});
return _8;
});
