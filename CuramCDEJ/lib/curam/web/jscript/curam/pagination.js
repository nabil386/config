//>>built
define("curam/pagination",["dojo/parser","dojo/dom-class","dojo/dom-construct","dojo/dom-attr","curam/debug","curam/define","curam/pagination/ControlPanel","curam/pagination/StateController"],function(_1,_2,_3,_4,_5){
curam.define.singleton("curam.pagination",{defaultPageSize:15,threshold:15,listModels:{},ROW_COUNT_CLASS_NAME:"numRows-",ESC_SCRIPT_START:"<!--@pg@",ESC_SCRIPT_END:"@pg@-->",localizedStrings:{firstPage_btn:"|<",firstPage_title:"$not-localized$ First page",prevPage_btn:"<",prevPage_title:"$not-localized$ Previous page",nextPage_btn:">",nextPage_title:"$not-localized$ Next page",lastPage_btn:">|",lastPage_title:"$not-localized$ Last page",pageSize_title:"$not-localized$ Page size",pagination_info:"$not-localized$ Displaying rows %s to %s out of %s",page_title:"Go to page"},addPagination:function(_6,_7){
var _8=_6.getRowCount();
var _9=_6.getId();
if(_8<=curam.pagination.threshold){
dojo.setObject("curam.shortlist."+_9,true);
_6.showRange(1,_8);
return;
}
_5.log("curam.pagination.addPagination: listId: ",_9);
if(curam.pagination.listModels[_9]){
throw "Pagination on this list has already been initialized: "+_9;
}
curam.pagination.listModels[_9]=_6;
_5.log("curam.pagination.listModels : ",curam.pagination.listModels);
var _a=new curam.pagination.ControlPanel(_7);
var _b=new curam.pagination.StateController(_6,_a);
_6._controller=_b;
dojo.subscribe("/curam/list/toBeSorted",this,function(_c){
_5.log(_5.getProperty("curam.omega3-util.received")+" /curam/list/toBeSorted "+_5.getProperty("curam.omega3-util.for")+":",_c);
var _d=curam.pagination.listModels[_c];
_d&&curam.pagination.unpackAll(_d);
});
dojo.subscribe("/curam/list/sorted",this,function(_e){
_5.log(_5.getProperty("curam.omega3-util.received")+" /curam/list/sorted "+_5.getProperty("curam.omega3-util.for")+":",_e);
var _f=curam.pagination.listModels[_e];
_f&&curam.pagination.paginatedListSorted(_f);
});
_b.gotoFirst();
},paginatedListSorted:function(_10){
_10._controller.reset();
},unpackRows:function(_11,_12){
var _13=_11.innerHTML;
var _14=_2.contains(_11,"has-row-actions");
if(_14){
_13=_13.replace(new RegExp(curam.pagination.ESC_SCRIPT_START,"g"),"<script type=\"text/javascript\">");
_13=_13.replace(new RegExp(curam.pagination.ESC_SCRIPT_END,"g"),"</script>");
}
var _15=_3.toDom(_13);
if(_14){
dojo.query("script",_15).forEach(function(s){
eval(s.innerHTML);
});
_1.parse(_15);
}
if(_12){
_12.appendChild(_15);
}else{
_3.place(_15,_11,"replace");
}
},unpackAll:function(_16){
_16._controller.gotoLast();
},readListContent:function(_17){
return dojo.query("tbody > *",_17).filter(function(n){
return typeof (n.tagName)!="undefined"&&(n.tagName=="TR"||(n.tagName=="SCRIPT"&&_4.get(n,"type")=="list-row-container"));
});
},getNumRowsInBlock:function(_18){
var _19=dojo.filter(_18.className.split(" "),function(cn){
return cn.indexOf(curam.pagination.ROW_COUNT_CLASS_NAME)==0;
});
return parseInt(_19[0].split(curam.pagination.ROW_COUNT_CLASS_NAME)[1]);
}});
return curam.pagination;
});
