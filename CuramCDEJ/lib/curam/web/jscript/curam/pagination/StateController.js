//>>built
define("curam/pagination/StateController",["dojo/_base/declare","curam/pagination","curam/debug"],function(_1){
var _2=_1("curam.pagination.StateController",null,{pageSize:undefined,currentPage:0,_listModel:undefined,_gui:undefined,constructor:function(_3,_4){
this.pageSize=curam.pagination.defaultPageSize;
this._listModel=_3;
this.pageSize=curam.pagination.defaultPageSize;
this._gui=_4;
var _5={};
_5.pageSizeOptions=[15,30,45];
_5.pageSizeOptions.contains=function(_6){
for(var i=0;i<_5.pageSizeOptions.length;i++){
if(_5.pageSizeOptions[i]==_6){
return true;
}
}
return false;
};
if(!_5.pageSizeOptions.contains(curam.pagination.defaultPageSize)){
_5.pageSizeOptions.push(curam.pagination.defaultPageSize);
_5.pageSizeOptions.sort(function(a,b){
return a-b;
});
}
_5.currentPageSize=this.pageSize;
_5.directLinkRangeWidth=3;
_5.lastPage=this._getLastPageNumber();
this._gui.updateState(_5);
var _7={};
_7.first=dojo.hitch(this,this.gotoFirst);
_7.last=dojo.hitch(this,this.gotoLast);
_7.previous=dojo.hitch(this,this.gotoPrevious);
_7.next=dojo.hitch(this,this.gotoNext);
_7.page=dojo.hitch(this,this.gotoPage);
_7.pageSize=dojo.hitch(this,this.changePageSize);
this._gui.setHandlers(_7);
},reset:function(){
this._listModel.hideRange(1,this._listModel.getRowCount());
this.currentPage=0;
this._gui.reset();
this.gotoFirst();
},gotoFirst:function(){
if(this.currentPage!=1){
this.gotoPage(1);
}
},gotoLast:function(){
var _8=this._getLastPageNumber();
if(this.currentPage!=_8){
this.gotoPage(_8);
}
},gotoPrevious:function(){
if(this.currentPage>1){
this.gotoPage(this.currentPage-1);
}
},gotoNext:function(){
curam.debug.log("curam.pagination.StateController.gotoNext");
var _9=this._getLastPageNumber();
if(this.currentPage<_9){
this.gotoPage(this.currentPage+1);
}
},gotoPage:function(_a){
curam.debug.log("curam.pagination.StateController.gotoPage: ",_a);
if(this.currentPage!=0){
this._listModel.hideRange(this._calcRangeStart(this.currentPage),this._calcRangeEnd(this.currentPage));
}
this._listModel.showRange(this._calcRangeStart(_a),this._calcRangeEnd(_a));
this.currentPage=_a;
this._updateGui();
},changePageSize:function(_b){
curam.debug.log("curam.pagination.StateController.changePageSize: ",_b);
this.pageSize=_b;
var _c={};
_c.currentPageSize=_b;
_c.lastPage=this._getLastPageNumber();
this._gui.updateState(_c);
dojo.publish("curam/update/readings/pagination",[this._listModel.getId(),_b]);
this.reset();
},_calcRangeStart:function(_d){
return (_d*this.pageSize)-this.pageSize+1;
},_calcRangeEnd:function(_e){
if(_e!=this._getLastPageNumber()){
return _e*this.pageSize;
}else{
return this._listModel.getRowCount();
}
},_getLastPageNumber:function(){
var _f=this._listModel.getRowCount();
var mod=_f%this.pageSize;
return ((_f-mod)/this.pageSize)+(mod>0?1:0);
},_updateGui:function(){
var _10={};
_10.first=this.currentPage>1;
_10.previous=_10.first;
_10.next=this.currentPage<this._getLastPageNumber();
_10.last=_10.next;
_10.currentPage=this.currentPage;
_10.rowInfo=[this._calcRangeStart(this.currentPage),this._calcRangeEnd(this.currentPage),this._listModel.getRowCount()];
this._gui.updateState(_10);
}});
return _2;
});
