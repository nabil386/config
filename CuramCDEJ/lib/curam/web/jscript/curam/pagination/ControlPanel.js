//>>built
define("curam/pagination/ControlPanel",["dijit/registry","dojo/_base/declare","dojo/dom-style","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/sniff","dojo/window","curam/pagination","curam/debug","curam/util"],function(_1,_2,_3,_4,_5,_6,_7,_8){
var _9=_2("curam.pagination.ControlPanel",null,{first:"FIRST",last:"LAST",previous:"PREV",next:"NEXT",page:"GOTO_PAGE",pageSize:"PAGE_SIZE",rowInfo:"ROW_INFO",classFirst:"first",classLast:"last",classPrevious:"previous",classNext:"next",classPage:"page",classDisplayInfo:"display_info",_controls:undefined,currentPage:0,lastPage:9999,currentPageSize:0,directLinkRangeWidth:3,parentNode:undefined,handlers:undefined,directLinksDisconnects:undefined,constructor:function(_a){
this._controls={};
this.handlers={};
this.directLinksDisconnects=[];
var _b=this._localize;
var ul=_6.create("ul",null,_a);
_5.add(ul,"pagination-control-list");
this._controls[this.pageSize]=this._createDropdownControl(this.pageSize,_b("pageSize_title"),ul);
this._controls[this.rowInfo]=this._createDisplayControl(this.rowInfo,_b("pagination_info",["$dummy$","$dummy$","$dummy$"]),ul,null,null);
this._controls[this.first]=this._createLinkControl(this.first,_b("firstPage_btn"),ul,null,this.classFirst,_b("firstPage_title"));
this._controls[this.previous]=this._createLinkControl(this.previous,_b("prevPage_btn"),ul,null,this.classPrevious,_b("prevPage_title"));
this._controls[this.page]=[];
this._controls[this.page].push(this._createLinkControl(this.page,"direct-page-links-section",ul,null,this.classPage,_b("page_title")));
this._controls[this.next]=this._createLinkControl(this.next,_b("nextPage_btn"),ul,null,this.classNext,_b("nextPage_title"));
this._controls[this.last]=this._createLinkControl(this.last,_b("lastPage_btn"),ul,null,this.classLast,_b("lastPage_title"));
this.parentNode=_a;
_3.set(_a,{"display":""});
},_localize:function(_c,_d){
var _e=curam.pagination.localizedStrings[_c];
if(!_d){
return _e;
}
for(var i=0;i<_d.length;i++){
_e=_e.replace(/%s/i,_d[i]);
}
return _e;
},_createLinkControl:function(_f,_10,_11,_12,_13,_14){
var cls=_13!=null?_13:"";
var li=_6.create("li",{"id":_f,"class":cls},_11,_12);
_5.add(li,"pagination-control-list-item enabled");
var a=_6.create("a",{"innerHTML":_10,"href":"#","title":_14},li);
_5.add(a,"pagination-link");
if(_f==this.first||_f==this.last||_f==this.previous||_f==this.next){
if(curam.util.highContrastModeType()){
var _15="../CDEJ/themes/v6/images/high-contrast/"+_f+"-contrast"+".png";
_6.create("img",{"src":_15,"alt":_14},a);
}else{
var _15="../CDEJ/themes/curam/images/"+_f+".svg";
var _16="../CDEJ/themes/curam/images/"+_f+"_hover.svg";
a.setAttribute("onfocus","this.children[0].src = '"+_16+"';");
a.setAttribute("onblur","this.children[0].src = '"+_15+"';");
_6.create("img",{"src":_15,"alt":_14,"onmouseover":"this.src = '"+_16+"';","onmouseout":"this.src = '"+_15+"';","onmouseup":"this.src = '"+_15+"';","aria-hidden":"true"},a);
}
}else{
var _10=_6.create("p",{"innerHTML":_10},li);
_5.add(_10,"pagination-text");
}
return li;
},_createDropdownControl:function(_17,_18,_19,_1a){
var li=_6.create("li",{"id":_17},_19,_1a);
_5.add(li,"pagination-control-list-item");
var _1b="page-size-select"+new Date().getTime();
var _1c=_6.create("label",{"innerHTML":_18+": ","for":_1b},li);
_5.add(_1c,"pagination-page-size-dropdown-label");
var _1d=_6.create("select",{"title":_18,"id":_1b},li);
li._type="dropdown";
return li;
},_createDisplayControl:function(_1e,_1f,_20,_21,_22){
var cls=_22!=null?_22:"";
var li=_6.create("li",{"id":_1e,"class":cls},_20,_21);
_5.add(li,"pagination-control-list-item");
var _1f=_6.create("p",{"innerHTML":"["+_1f+"]"},li);
return li;
},updateState:function(_23){
curam.debug.log("curam.pagination.ControlPanel.updateState: ",_23);
if(typeof (_23.first)!="undefined"){
this._setEnabled(this._controls[this.first],_23.first);
}
if(typeof (_23.previous)!="undefined"){
this._setEnabled(this._controls[this.previous],_23.previous);
}
if(typeof (_23.next)!="undefined"){
this._setEnabled(this._controls[this.next],_23.next);
}
if(typeof (_23.last)!="undefined"){
this._setEnabled(this._controls[this.last],_23.last);
}
if(typeof (_23.currentPage)!="undefined"){
this.currentPage=_23.currentPage;
}
if(typeof (_23.lastPage)!="undefined"){
this.lastPage=_23.lastPage;
}
if(typeof (_23.currentPageSize)!="undefined"){
this.currentPageSize=_23.currentPageSize;
}
if(typeof (_23.directLinkRangeWidth)!="undefined"){
this.directLinkRangeWidth=_23.directLinkRangeWidth;
}
if(typeof (_23.rowInfo)!="undefined"){
var _24=this._controls[this.rowInfo].previousSibling;
_6.destroy(this._controls[this.rowInfo]);
var _25=_23.rowInfo[0];
var end=_23.rowInfo[1];
var _26=_23.rowInfo[2];
var _27=this._localize("pagination_info",[_25,end,_26]);
this._controls[this.rowInfo]=this._createDisplayControl(this.rowInfo,_27,_24,"after",this.classDisplayInfo);
}
if(typeof (_23.pageSizeOptions)!="undefined"){
var _28=dojo.query("select",this._controls[this.pageSize])[0];
dojo.forEach(_28.childNodes,function(_29){
_6.destroy(_29);
});
for(var i=0;i<_23.pageSizeOptions.length;i++){
var _2a=_23.pageSizeOptions[i];
var _2b=_6.create("option",{"value":_2a,"innerHTML":_2a},_28);
if(_2a==this.currentPageSize){
_4.set(_2b,"selected","selected");
}
}
}
this._updateDirectLinks();
var _2c=_1.byId("content");
if(_2c){
_2c.resize();
}
},setHandlers:function(_2d){
curam.debug.log("curam.pagination.ControlPanel.setHandlers: ",_2d);
this.handlers=_2d;
if(_2d.first){
this._connectSimpleHandler(this._controls[this.first],_2d.first);
}
if(_2d.previous){
this._connectSimpleHandler(this._controls[this.previous],_2d.previous);
}
if(_2d.next){
this._connectSimpleHandler(this._controls[this.next],_2d.next);
}
if(_2d.last){
this._connectSimpleHandler(this._controls[this.last],_2d.last);
}
if(_2d.page){
this._connectDirectLinkHandlers(_2d.page);
}
if(_2d.pageSize){
var _2e=dojo.query("select",this._controls[this.pageSize])[0];
var _2f=this._setFocusToListDivContainerAfterNewPageSelected;
dojo.connect(_2e,"onchange",dojo.hitch(this,function(_30){
var _31=_30.target.value;
this.currentPageSize=_31;
_2d.pageSize(this.currentPageSize);
var _32=_30.currentTarget;
if(_32&&_5.contains(_32.parentElement,"pagination-control-list-item")){
var _33=_32.parentElement;
_2f(_33);
}
var _34=dojo.query("option",_2e);
_34.forEach(function(_35){
if(_4.get(_35,"value")==_31){
_4.set(_35,"selected","selected");
}else{
_4.remove(_35,"selected");
}
});
}));
}
},_connectSimpleHandler:function(_36,_37){
var h=_37?_37:_36._handler;
this._removeSimpleHandler(_36);
var _38=this._setFocusToListDivContainerAfterNewPageSelected;
var _39=curam.util.connect(_36,"onclick",function(_3a){
dojo.stopEvent(_3a);
h();
var _3b=_3a.currentTarget;
if(_3b&&_5.contains(_3b,"pagination-control-list-item")){
var _3c=_3b.parentElement;
_38(_3c);
}
});
_36._handler=h;
_36._disconnect=_39;
},_removeSimpleHandler:function(_3d){
if(_3d._disconnect){
curam.util.disconnect(_3d._disconnect);
}
},reset:function(){
curam.debug.log("curam.pagination.ControlPanel.reset");
},_getDirectLinkPageNumbers:function(){
var _3e=2*this.directLinkRangeWidth+1;
var p=this.currentPage;
var _3f=[];
var num=p>this.directLinkRangeWidth?p-this.directLinkRangeWidth:1;
for(var i=0;i<_3e;i++){
_3f[i]=num++;
if(num>this.lastPage){
break;
}
}
return _3f;
},_updateDirectLinks:function(){
curam.debug.log("curam.pagination.ControlPanel._updateDirectLinks");
var loc=this._localize;
var _40=this._controls[this.page];
dojo.query("div.pagination-direct-links-dots").forEach(_6.destroy);
var _41=_40[0].previousSibling;
_3.set(this.parentNode,"display","none");
for(var i=0;i<_40.length;i++){
if(_40._dots){
_6.destroy(_40._dots);
}
_6.destroy(_40[i]);
_40[i]=undefined;
}
this._controls[this.page]=[];
_40=this._controls[this.page];
var _42=this._getDirectLinkPageNumbers();
for(var i=0;i<_42.length;i++){
var _43=_42[i];
_40[i]=this._createLinkControl(this.page+"("+_43+")",_43,_41,"after",null,loc("page_title")+" "+_43);
_5.add(_40[i],"pagination-direct-link");
if(_43==this.currentPage){
_5.add(_40[i],"selected");
}
_41=_40[i];
_40[i]._pageNum=_43;
}
var _44=_40[0];
_5.add(_44,"firstDirectLink");
if(_42[0]>1){
_5.add(_44,"has-previous");
var _45=_6.create("div",{innerHTML:"..."},_44,"before");
_5.add(_45,"pagination-direct-links-dots");
}
var _46=_40[_40.length-1];
_5.add(_46,"lastDirectLink");
if(_42[_42.length-1]<this.lastPage){
_5.add(_46,"has-next");
var _45=_6.create("div",{innerHTML:"..."},_46,"after");
_5.add(_45,"pagination-direct-links-dots");
}
if(this.handlers.page){
this._connectDirectLinkHandlers(this.handlers.page);
}
_3.set(this.parentNode,"display","");
},_connectDirectLinkHandlers:function(_47){
dojo.forEach(this.directLinksDisconnects,dojo.disconnect);
this.directLinksDisconnects=[];
var _48=this._controls[this.page];
for(var i=0;i<_48.length;i++){
var _49=_48[i];
var _4a=this._setFocusToListDivContainerAfterNewPageSelected;
var h=function(_4b){
dojo.stopEvent(_4b);
var _4c=_4b.currentTarget;
var _4d=_4c.parentElement;
_47(this._pageNum);
if(_4c&&_5.contains(_4c,"pagination-control-list-item")){
if(!_5.contains(_4c,"selected")){
_4a(_4d);
}else{
var _4e=_4c.id;
var _4f=dojo.query("a.pagination-link",_4d.children[_4e])[0];
if(_4f){
_4f.focus();
}
}
}
};
h._pageNum=_49._pageNum;
this.directLinksDisconnects.push(dojo.connect(_49,"onclick",h));
}
},_setEnabled:function(_50,_51){
if(_51){
this._connectSimpleHandler(_50);
_5.replace(_50,"enabled","disabled");
}else{
this._removeSimpleHandler(_50);
_5.replace(_50,"disabled","enabled");
}
},_setFocusToListDivContainerAfterNewPageSelected:function(_52){
var _53="";
while(_52!==null){
if(_5.contains(_52,"list")){
_53=_52;
break;
}else{
_52=_52.parentElement;
}
}
if(_53!=""){
_53.setAttribute("tabindex","-1");
_53.focus();
_7("trident")?_8.scrollIntoView(_53):_53.scrollIntoView();
}
}});
return _9;
});
