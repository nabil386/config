//>>built
define("curam/util/ListSort",["dojo/dom","dojo/dom-construct","dojo/dom-style","dojo/dom-attr","curam/util","curam/debug","dojo/query","dojo/on","dojo/sniff"],function(_1,_2,_3,_4,_5,_6,_7,on,_8){
var _9=_LS=dojo.setObject("curam.util.ListSort",{LIVE:"aria-live",READOUT:"aria-label",BUSY:"aria-busy",SCROLLABLE:"_slh",EVT_SORT_PREP:"/curam/list/toBeSorted",EVT_SORTED:"/curam/list/sorted",sortableTable:null,sortTexts:null,tablesWithListeners:[],sorters:[],headingUpdaters:[],currentUpdater:null,updatesByNow:-1,sortedCell:null,cleaner:null,makeSortable:function(_a,_b,_c,_d,_e,_f){
dojo.addOnLoad(function(){
var _10=_1.byId(_a);
if(!_10){
return;
}
var _11=_7("tHead tr th",_10);
if(_11==null){
return;
}
_10.ariaListeners=[];
_10.onces=[];
_LS.sortTexts=_e.split("|");
var _12=1+(+_c),_13=_10.tBodies[0],rw=_13&&_13.rows,_14=rw?rw.length:false,_15=_14?_14-_12:0;
var _16=function(_17,_18){
return function(){
_4.set(_17,_LS.READOUT,_18);
};
},_19=function(_1a,_1b){
return _1a+"."+_1b;
},_1c=function(_1d,_1e,_1f){
var _20=function(_21,_22,sIx,_23){
if(_21==_4.get(_1d,"paginationId")){
if(_LS.updatesByNow>0){
var _24=_1f+" "+_d;
var _25=null;
if(_1e==_22){
_25=_24+"."+_23;
_24=_19(_24,_23);
_LS.sortedCell=_1d;
if(_8("trident")){
_4.set(_1d,_LS.READOUT," ");
_4.set(_7("span",_1d)[0],_LS.READOUT,_24);
}else{
_4.set(_1d,_LS.READOUT,_24);
}
}else{
_24+="."+_LS.sortTexts[0];
_10.onces.push(_16(_1d,_24));
_8("trident")&&_4.set(_7("span",_1d)[0],_LS.READOUT,_24);
}
_4.set(_1d,_LS.BUSY,"false");
}
if(--_LS.updatesByNow==0){
_LS.doFocus(_10.onces,_25);
}
}
};
_20.cleanUp=function(){
_1d=null;
};
return _20;
},_26=function(tbl,_27,_28){
return function(pid,_29){
if(tbl==_29){
_LS.sortedCell=null;
tbl.onces=[];
_LS.resetReadout(_27,_28);
_4.set(_27,_LS.BUSY,"true");
}
};
};
_11.forEach(function(_2a,ix){
if(_2a.id&&_2a.childNodes[0]){
var _2b=(_f?_1.byId(_2a.id+_LS.SCROLLABLE):_2a).childNodes[0],_2c=_2b.childNodes[0];
if(_2c&&_2c.nodeType==3){
var _2d=dojo.trim(_2c.nodeValue);
if((_2d.length>0)&&(_2d!=" ")){
_2b.innerHTML="";
var _2e=_2d+" "+_d,_2f=_2e+" "+_LS.sortTexts[0];
_4.set(_2b,{"tabindex":"0","paginationid":_b,"aria-label":_2f,className:"listSortable"});
if(_8("trident")){
var _2e=_2e+_LS.sortTexts[0];
_4.set(_2b,{"onblur":"_LS.updateAriaLabel(this)"});
}
_LS.setPlaceholders(_2b,_2d,_2e);
_LS.sorters.push(on(_2b,"click, keyup",dojo.partial(_LS.sortTable,_10,_b,ix,_12,_c,_15)));
_LS.sorters.push(dojo.subscribe(_LS.EVT_SORT_PREP,_26(_10,_2b,_2d)));
_10.ariaListeners.push(dojo.subscribe(_LS.EVT_SORTED,_1c(_2b,ix,_2d)));
}
}
}
});
(_10.ariaListeners.length>0)&&_LS.tablesWithListeners.push(_10);
_7(".hidden-table-header a").forEach(function(_30){
_4.set(_30,"tabindex","-1");
});
_10._sortUp=true;
_LS.sortableTable=_10;
_LS.cleaner=on(window,"unload",_LS.cleanUp);
});
},updateAriaLabel:function(_31){
_4.remove(_31,_9.READOUT);
},cleanUp:function(){
_LS.tablesWithListeners.forEach(function(tbl){
tbl.ariaListeners.forEach(function(lsn){
dojo.unsubscribe(lsn);
});
tbl.ariaListeners=null;
});
_LS.tablesWithListeners=null;
_LS.sorters.forEach(function(srt){
srt.cleanUp&&srt.cleanUp();
});
_LS.sorters=null;
_LS.sortedCell=null;
_LS.sortableTable=null;
_LS.cleaner.remove();
},sortTable:function(_32,pid,col,_33,_34,_35,evt){
if(evt&&evt.keyCode&&!(evt.keyCode==13||evt.keyCode==32)){
return;
}
var _36=_32.tBodies[0],_37=_36.rows;
var _38=_36&&_36.rows.length;
if((_38<3&&_34)||(_38<2&&!_34)){
return;
}
var _39=_32._sortUp?function(a,b){
return a-b;
}:function(a,b){
return b-a;
};
var _3a=function(a,b){
var aa=_LS.getOrd(a.cells[col]);
var bb=_LS.getOrd(b.cells[col]);
return _39(aa,bb);
};
dojo.publish("curam/sort/earlyAware",[pid]);
dojo.publish(_LS.EVT_SORT_PREP,[pid,_32]);
count=_36&&_36.rows.length;
if(count&&count<=_33){
return;
}
var _3b=_LS.getOrd(_37[0].cells[col]);
if(_3b>=0){
var _3c=[];
var i=0;
while(i<count){
var fl=_37[i++];
_3c.push(fl);
_34&&(fl._detailsRow=_37[i++]);
}
_3c.sort(_3a);
_32._sortUp=!_32._sortUp;
var _3d=document.createDocumentFragment(),_3e=document.createDocumentFragment();
_3c.forEach(function(_3f,ix){
_4.set(_3f,"data-lix",ix);
_3f.done=false;
var app=(ix<=_35)?_3d:_3e;
app.appendChild(_3f);
_3f._detailsRow&&app.appendChild(_3f._detailsRow);
});
_36.appendChild(_3d);
curam.util.stripeTable(_32,_34,_35);
_36.appendChild(_3e);
var _40=0;
if(_3c.length>1){
var _41=_LS.getOrd(_3c[0].cells[col]),_42=_LS.getOrd(_3c[_3c.length-1].cells[col]);
_40=_42==_41?0:(_42>_41)?1:2;
}
var _43=_LS.sortTexts[_40];
_LS.updatesByNow=_32.ariaListeners.length;
dojo.publish(_LS.EVT_SORTED,[pid,col,_40,_43]);
var _44=dojo.exists;
if(_44("curam.listControls."+pid)||_44("curam.listTogglers."+pid)||(_44("curam.listMenus."+pid)&&curam.listMenus[pid].length>0)){
dojo.publish("curam/update/readings/sort",[pid,_3c]);
}
}
return false;
},doFocus:function(_45,_46){
_4.set(_LS.sortedCell,_LS.LIVE,"polite");
var fn;
while(fn=_45.pop()){
fn();
}
_LS.sortedCell.aux&&_LS.sortedCell.aux.focus();
var to=setTimeout(function(){
clearTimeout(to);
_LS.sortedCell&&_LS.sortedCell.focus();
},500);
if(_46){
var to1=setTimeout(function(){
clearTimeout(to1);
_LS.sortedCell&&_4.set(_LS.sortedCell,_LS.READOUT,_46);
},2500);
}
},getOrd:function(el){
if(!el){
return -1;
}
if(el.ord){
return el.ord;
}
var sps=_7("span[data-curam-sort-order]",el)[0];
if(!sps){
el.ord=-1;
return el.ord;
}
var ord=_4.get(sps,"data-curam-sort-order");
var _47=parseInt(ord),res=isNaN(_47)?-1:_47;
el.ord=res;
return res;
}});
_8("trident")?dojo.mixin(_9,{setPlaceholders:function(_48,_49,_4a){
_2.create("p",{"aria-hidden":"true",innerHTML:_49,className:"sortColText"},_48);
var _4b=_2.create("span",{"tabindex":-1,"aria-label":_4a},_48);
_3.set(_4b,{"opacity":"0","margin":"0","padding":"0"});
_48.aux=_4b;
},resetReadout:function(_4c,_4d){
_4.set(_4c,_9.READOUT,_4d);
}}):dojo.mixin(_9,{setPlaceholders:function(_4e,_4f,_50){
!/Edg/.test(navigator.userAgent)&&_4.set(_4e,"role","button");
_2.create("p",{"aria-hidden":"true",innerHTML:_4f,className:"sortColText"},_4e);
},resetReadout:function(_51,_52){
_4.remove(_51,_9.LIVE);
_4.remove(_51,_9.READOUT);
}});
_8("ios")&&dojo.mixin(_9,{setPlaceholders:function(_53,_54,_55){
_4.set(_53,"role","link");
var _56=_2.create("p",{"aria-hidden":"true",innerHTML:"placeholder",className:"sortColText"},_53);
_3.set(_56,{"opacity":"0","margin":"0","padding":"0","position":"absolute","bottom":"0"});
var lnk=_2.create("a",{"tabindex":0,"aria-label":_54,"href":"#",innerHTML:_54},_53);
}});
return _9;
});
