if(!dojo._hasResource["dojox.layout.GridContainerLite"]){
dojo._hasResource["dojox.layout.GridContainerLite"]=true;
dojo.provide("dojox.layout.GridContainerLite");
require(["curam/cefwidgets/pods/Pod","dojox/mdnd/Moveable","dojox/mdnd/AreaManager","dojox/mdnd/DropIndicator","dojox/mdnd/dropMode/OverDropMode","dojox/mdnd/AutoScroll"]);
dojo.declare("dojox.layout.GridContainerLite",[dijit.layout._LayoutWidget,dijit._Templated],{autoRefresh:true,templateString:dojo.cache("dojox.layout","resources/GridContainer.html","<div id=\"${id}\" class=\"gridContainer\" dojoAttachPoint=\"containerNode\" tabIndex=\"0\" dojoAttachEvent=\"onkeypress:_selectFocus\">\r\n\t<div dojoAttachPoint=\"gridContainerDiv\">\r\n\t\t<table class=\"gridContainerTable\"  role=\"presentation\" dojoAttachPoint=\"gridContainerTable\" cellspacing=\"0\" cellpadding=\"0\">\r\n\t\t\t<tbody>\r\n\t\t\t\t<tr dojoAttachPoint=\"gridNode\" >\r\n\t\t\t\t\t\r\n\t\t\t\t</tr>\r\n\t\t\t</tbody>\r\n\t\t</table>\r\n\t</div>\r\n</div>\r\n"),dragHandleClass:"dojoxDragHandle",nbZones:1,doLayout:true,isAutoOrganized:true,acceptTypes:[],colWidths:"",lazyLoad:false,podDropDelay:500,podFadeInDuration:500,constructor:function(_1,_2){
this.acceptTypes=(_1||{}).acceptTypes||["text"];
this._disabled=true;
},postCreate:function(){
this._grid=[];
this._createCells();
var _3=curam.util.getTopmostWindow();
this.handler=_3.dojo.subscribe("/curam/main-content/page/loaded",this,function(){
_gridContainer=this;
dojo.publish("pods.readyformore",[_gridContainer]);
_3.dojo.unsubscribe(_gridContainer.handler);
});
this._dragManager=dojox.mdnd.areaManager();
this._dragManager.autoRefresh=this.autoRefresh;
this._dragManager.dragHandleClass=this.dragHandleClass;
if(this.doLayout){
this._border={"h":(dojo.isIE)?dojo._getBorderExtents(this.gridContainerTable).h:0,"w":(dojo.isIE==6)?1:0};
}else{
dojo.style(this.domNode,"overflowY","hidden");
dojo.style(this.gridContainerTable,"height","auto");
}
},startup:function(){
if(this.isAutoOrganized){
this._organizeChildren();
}else{
this._organizeChildrenManually();
}
dojo.forEach(this.getChildren(),function(_4){
_4.startup();
});
},dragFunctionality:function(){
if(this._started){
return;
}
if(this.isAutoOrganized){
this._organizeChildren();
}else{
this._organizeChildrenManually();
}
if(!this.lazyLoad&&this._isShown()){
this.enableDnd();
}
},resizeChildAfterDrop:function(_5,_6,_7){
if(this._disabled){
return false;
}
if(dijit.getEnclosingWidget(_6.node)==this){
var _8=dijit.byNode(_5);
if(_8.resize&&dojo.isFunction(_8.resize)){
_8.resize();
}
if(this.doLayout){
var _9=this._contentBox.h,_a=dojo.contentBox(this.gridContainerDiv).h;
if(_a>=_9){
dojo.style(this.gridContainerTable,"height",(_9-this._border.h)+"px");
}
}
return true;
}
return false;
},resizeChildAfterDragStart:function(_b,_c,_d){
if(this._disabled){
return false;
}
if(dijit.getEnclosingWidget(_c.node)==this){
this._draggedNode=_b;
if(this.doLayout){
dojo.marginBox(this.gridContainerTable,{"h":dojo.contentBox(this.gridContainerDiv).h-this._border.h});
}
return true;
}
return false;
},getChildren:function(){
var _e=[];
dojo.forEach(this._grid,function(_f){
var _10=dojo.query("> [class]",_f.node);
_e=_e.concat(dojo.query("> [widgetId]",_f.node).map(dijit.byNode));
});
return _e;
},_isShown:function(){
if("open" in this){
return this.open;
}else{
var _11=this.domNode;
return (_11.style.display!="none")&&(_11.style.visibility!="hidden")&&!dojo.hasClass(_11,"dijitHidden");
}
},layout:function(){
if(this.doLayout){
var _12=this._contentBox;
dojo.marginBox(this.gridContainerTable,{"h":_12.h-this._border.h});
dojo.contentBox(this.domNode,{"w":_12.w-this._border.w});
}
dojo.forEach(this.getChildren(),function(_13){
if(_13.resize&&dojo.isFunction(_13.resize)){
_13.resize();
}
});
},onShow:function(){
if(this._disabled){
this.enableDnd();
}
},onHide:function(){
if(!this._disabled){
this.disableDnd();
}
},_createCells:function(){
if(this.nbZones===0){
this.nbZones=1;
}
var _14=this.acceptTypes.join(","),i=0;
var _15=this.colWidths||[];
var _16=[];
var _17;
var _18=0;
for(i=0;i<this.nbZones;i++){
if(_16.length<_15.length){
_18+=_15[i];
_16.push(_15[i]);
}else{
if(!_17){
_17=(100-_18)/(this.nbZones-i);
}
_16.push(_17);
}
}
i=0;
while(i<this.nbZones){
this._grid.push({"node":dojo.create("td",{"class":"gridContainerZone","accept":_14,"id":this.id+"_dz"+i,"style":{"width":_16[i]+"%"}},this.gridNode)});
i++;
}
},enableDnd:function(){
var m=this._dragManager;
dojo.forEach(this._grid,function(_19){
m.registerByNode(_19.node);
});
m._dropMode.updateAreas(m._areaList);
this._disabled=false;
},disableDnd:function(){
var m=this._dragManager;
dojo.forEach(this._grid,function(_1a){
m.unregister(_1a.node);
});
m._dropMode.updateAreas(m._areaList);
this._disabled=true;
},_organizeChildren:function(){
var _1b=dojox.layout.GridContainerLite.superclass.getChildren.call(this);
var _1c=this.nbZones,_1d=Math.floor(_1b.length/_1c),mod=_1b.length%_1c,i=0;
for(var z=0;z<_1c;z++){
for(var r=0;r<_1d;r++){
this._insertChild(_1b[i],z);
i++;
}
if(mod>0){
try{
this._insertChild(_1b[i],z);
i++;
}
catch(e){
console.error("Unable to insert child in GridContainer",e);
}
mod--;
}else{
if(_1d===0){
break;
}
}
}
},_organizeChildrenManually:function(){
var _1e=dojox.layout.GridContainerLite.superclass.getChildren.call(this),_1f=_1e.length,_20;
for(var i=0;i<_1f;i++){
_20=_1e[i];
try{
this._insertChild(_20,_20.column-1);
}
catch(e){
console.error("Unable to insert child in GridContainer",e);
}
}
},_insertChild:function(_21,_22,p){
if((_21.declaredClass)==="curam.cefwidgets.pods.Pod"){
var _23=this._grid[_22].node,_24=_23.childNodes.length;
if(typeof (p)=="undefined"||p>_24){
p=_24;
}
if(this._disabled){
dojo.place(_21.domNode,_23,p);
dojo.attr(_21.domNode,"tabIndex","0");
}else{
if(!_21.dragRestriction||_21.dragRestriction==="false"){
this._dragManager.addDragItem(_23,_21.domNode,p,true,this.podFadeInDuration);
}else{
dojo.place(_21.domNode,_23,p);
dojo.attr(_21.domNode,"tabIndex","0");
}
}
return _21;
}
},removeChild:function(_25){
if(this._disabled){
this.inherited(arguments);
}else{
this._dragManager.removeDragItem(_25.domNode.parentNode,_25.domNode);
}
},addService:function(_26,_27,p){
dojo.deprecated("addService is deprecated.","Please use  instead.","Future");
this.addChild(_26,_27,p);
},addChild:function(_28,_29,p){
_28.domNode.id=_28.id;
dojox.layout.GridContainerLite.superclass.addChild.call(this,_28,0);
if(_29<0||_29==undefined){
_29=0;
}
if(p<=0){
p=0;
}
try{
return this._insertChild(_28,_29,p);
}
catch(e){
console.error("Unable to insert child in GridContainer",e);
}
return null;
},_setColWidthsAttr:function(_2a){
this.colWidths=dojo.isString(_2a)?_2a.split(","):(dojo.isArray(_2a)?_2a:[_2a]);
if(this._started){
this._updateColumnsWidth();
}
},_updateColumnsWidth:function(_2b){
var _2c=this._grid.length;
var _2d=this.colWidths||[];
var _2e=[];
var _2f;
var _30=0;
var i;
for(i=0;i<_2c;i++){
if(_2e.length<_2d.length){
_30+=_2d[i]*1;
_2e.push(_2d[i]);
}else{
if(!_2f){
_2f=(100-_30)/(this.nbZones-i);
if(_2f<0){
_2f=100/this.nbZones;
}
}
_2e.push(_2f);
_30+=_2f*1;
}
}
if(_30>100){
var _31=100/_30;
for(i=0;i<_2e.length;i++){
_2e[i]*=_31;
}
}
for(i=0;i<_2c;i++){
this._grid[i].node.style.width=_2e[i]+"%";
}
},_selectFocus:function(_32){
if(this._disabled){
return;
}
var key=_32.keyCode,k=dojo.keys,_33=null,_34=dijit.getFocus(),_35=_34.node,m=this._dragManager,_36,i,j,r,_37,_38,_39;
if(_35==null){
return;
}else{
if(_35==this.containerNode){
_38=this.gridNode.childNodes;
switch(key){
case k.DOWN_ARROW:
case k.RIGHT_ARROW:
_36=false;
for(i=0;i<_38.length;i++){
_37=_38[i].childNodes;
for(j=0;j<_37.length;j++){
_33=_37[j];
if(_33!=null&&_33.style.display!="none"){
dijit.focus(_33);
dojo.stopEvent(_32);
_36=true;
break;
}
}
if(_36){
break;
}
}
break;
case k.UP_ARROW:
case k.LEFT_ARROW:
_38=this.gridNode.childNodes;
_36=false;
for(i=_38.length-1;i>=0;i--){
_37=_38[i].childNodes;
for(j=_37.length;j>=0;j--){
_33=_37[j];
if(_33!=null&&_33.style.display!="none"){
dijit.focus(_33);
dojo.stopEvent(_32);
_36=true;
break;
}
}
if(_36){
break;
}
}
break;
}
}else{
if(_35.parentNode.parentNode==this.gridNode){
var _3a=(key==k.UP_ARROW||key==k.LEFT_ARROW)?"lastChild":"firstChild";
var pos=(key==k.UP_ARROW||key==k.LEFT_ARROW)?"previousSibling":"nextSibling";
switch(key){
case k.UP_ARROW:
case k.DOWN_ARROW:
dojo.stopEvent(_32);
_36=false;
var _3b=_35;
while(!_36){
_37=_3b.parentNode.childNodes;
var num=0;
for(i=0;i<_37.length;i++){
if(_37[i].style.display!="none"){
num++;
}
if(num>1){
break;
}
}
if(num==1){
return;
}
if(_3b[pos]==null){
_33=_3b.parentNode[_3a];
}else{
_33=_3b[pos];
}
if(_33.style.display==="none"){
_3b=_33;
}else{
_36=true;
}
}
if(_32.shiftKey){
var _3c=_35.parentNode;
for(i=0;i<this.gridNode.childNodes.length;i++){
if(_3c==this.gridNode.childNodes[i]){
break;
}
}
_37=this.gridNode.childNodes[i].childNodes;
for(j=0;j<_37.length;j++){
if(_33==_37[j]){
break;
}
}
if(dojo.isMoz||dojo.isWebKit){
i--;
}
_39=dijit.byNode(_35);
if(!_39.dragRestriction){
r=m.removeDragItem(_3c,_35);
this.addChild(_39,i,j);
dojo.attr(_35,"tabIndex","0");
dijit.focus(_35);
}else{
dojo.publish("/dojox/layout/gridContainer/moveRestriction",[this]);
}
}else{
dijit.focus(_33);
}
break;
case k.RIGHT_ARROW:
case k.LEFT_ARROW:
dojo.stopEvent(_32);
if(_32.shiftKey){
var z=0;
if(_35.parentNode[pos]==null){
if(dojo.isIE&&key==k.LEFT_ARROW){
z=this.gridNode.childNodes.length-1;
}
}else{
if(_35.parentNode[pos].nodeType==3){
z=this.gridNode.childNodes.length-2;
}else{
for(i=0;i<this.gridNode.childNodes.length;i++){
if(_35.parentNode[pos]==this.gridNode.childNodes[i]){
break;
}
z++;
}
if(dojo.isMoz||dojo.isWebKit){
z--;
}
}
}
_39=dijit.byNode(_35);
var _3d=_35.getAttribute("dndtype");
if(_3d==null){
if(_39&&_39.dndType){
_3d=_39.dndType.split(/\s*,\s*/);
}else{
_3d=["text"];
}
}else{
_3d=_3d.split(/\s*,\s*/);
}
var _3e=false;
for(i=0;i<this.acceptTypes.length;i++){
for(j=0;j<_3d.length;j++){
if(_3d[j]==this.acceptTypes[i]){
_3e=true;
break;
}
}
}
if(_3e&&!_39.dragRestriction){
var _3f=_35.parentNode,_40=0;
if(k.LEFT_ARROW==key){
var t=z;
if(dojo.isMoz||dojo.isWebKit){
t=z+1;
}
_40=this.gridNode.childNodes[t].childNodes.length;
}
r=m.removeDragItem(_3f,_35);
this.addChild(_39,z,_40);
dojo.attr(r,"tabIndex","0");
dijit.focus(r);
}else{
dojo.publish("/dojox/layout/gridContainer/moveRestriction",[this]);
}
}else{
var _41=_35.parentNode;
while(_33===null){
if(_41[pos]!==null&&_41[pos].nodeType!==3){
_41=_41[pos];
}else{
if(pos==="previousSibling"){
_41=_41.parentNode.childNodes[_41.parentNode.childNodes.length-1];
}else{
_41=(dojo.isIE)?_41.parentNode.childNodes[0]:_41.parentNode.childNodes[1];
}
}
_33=_41[_3a];
if(_33&&_33.style.display=="none"){
_37=_33.parentNode.childNodes;
var _42=null;
if(pos=="previousSibling"){
for(i=_37.length-1;i>=0;i--){
if(_37[i].style.display!="none"){
_42=_37[i];
break;
}
}
}else{
for(i=0;i<_37.length;i++){
if(_37[i].style.display!="none"){
_42=_37[i];
break;
}
}
}
if(!_42){
_35=_33;
_41=_35.parentNode;
_33=null;
}else{
_33=_42;
}
}
}
dijit.focus(_33);
}
break;
}
}
}
}
},destroy:function(){
var m=this._dragManager;
dojo.forEach(this._grid,function(_43){
m.unregister(_43.node);
});
this.inherited(arguments);
}});
dojo.extend(dijit._Widget,{column:"1",dragRestriction:false});
}
if(!dojo._hasResource["dojox.layout.GridContainer"]){
dojo._hasResource["dojox.layout.GridContainer"]=true;
dojo.provide("dojox.layout.GridContainer");
dojo.declare("dojox.layout.GridContainer",dojox.layout.GridContainerLite,{hasResizableColumns:true,liveResizeColumns:false,minColWidth:20,minChildWidth:150,mode:"right",isRightFixed:false,isLeftFixed:false,startup:function(){
this.inherited(arguments);
if(this.hasResizableColumns){
for(var i=0;i<this._grid.length-1;i++){
this._createGrip(i);
}
if(!this.getParent()){
dojo.ready(dojo.hitch(this,"_placeGrips"));
}
}
},resizeChildAfterDrop:function(_44,_45,_46){
if(this.inherited(arguments)){
this._placeGrips();
}
},onShow:function(){
this.inherited(arguments);
this._placeGrips();
},resize:function(){
this.inherited(arguments);
if(this._isShown()&&this.hasResizableColumns){
this._placeGrips();
}
},_createGrip:function(_47){
var _48=this._grid[_47],_49=dojo.create("div",{"class":"gridContainerGrip"},this.domNode);
_48.grip=_49;
_48.gripHandler=[this.connect(_49,"onmouseover",function(e){
var _4a=false;
for(var i=0;i<this._grid.length-1;i++){
if(dojo.hasClass(this._grid[i].grip,"gridContainerGripShow")){
_4a=true;
break;
}
}
if(!_4a){
dojo.removeClass(e.target,"gridContainerGrip");
dojo.addClass(e.target,"gridContainerGripShow");
}
})[0],this.connect(_49,"onmouseout",function(e){
if(!this._isResized){
dojo.removeClass(e.target,"gridContainerGripShow");
dojo.addClass(e.target,"gridContainerGrip");
}
})[0],this.connect(_49,"onmousedown","_resizeColumnOn")[0],this.connect(_49,"ondblclick","_onGripDbClick")[0]];
},_placeGrips:function(){
var _4b,_4c,_4d=0,_4e;
var _4f=this.domNode.style.overflowY;
dojo.forEach(this._grid,function(_50){
if(_50.grip){
_4e=_50.grip;
if(!_4b){
_4b=_4e.offsetWidth/2;
}
_4d+=dojo.marginBox(_50.node).w;
dojo.style(_4e,"left",(_4d-_4b)+"px");
if(!_4c){
_4c=dojo.contentBox(this.gridNode).h;
}
if(_4c>0){
dojo.style(_4e,"height",_4c+"px");
}
}
},this);
},_onGripDbClick:function(){
this._updateColumnsWidth(this._dragManager);
this.resize();
},_resizeColumnOn:function(e){
this._activeGrip=e.target;
this._initX=e.pageX;
e.preventDefault();
dojo.body().style.cursor="ew-resize";
this._isResized=true;
var _51=[];
var _52;
var i;
for(i=0;i<this._grid.length;i++){
_51[i]=dojo.contentBox(this._grid[i].node).w;
}
this._oldTabSize=_51;
for(i=0;i<this._grid.length;i++){
_52=this._grid[i];
if(this._activeGrip==_52.grip){
this._currentColumn=_52.node;
this._currentColumnWidth=_51[i];
this._nextColumn=this._grid[i+1].node;
this._nextColumnWidth=_51[i+1];
}
_52.node.style.width=_51[i]+"px";
}
var _53=function(_54,_55){
var _56=0;
var _57=0;
dojo.forEach(_54,function(_58){
if(_58.nodeType==1){
var _59=dojo.getComputedStyle(_58);
var _5a=(dojo.isIE)?_55:parseInt(_59.minWidth);
_57=_5a+parseInt(_59.marginLeft)+parseInt(_59.marginRight);
if(_56<_57){
_56=_57;
}
}
});
return _56;
};
var _5b=_53(this._currentColumn.childNodes,this.minChildWidth);
var _5c=_53(this._nextColumn.childNodes,this.minChildWidth);
var _5d=Math.round((dojo.marginBox(this.gridContainerTable).w*this.minColWidth)/100);
this._currentMinCol=_5b;
this._nextMinCol=_5c;
if(_5d>this._currentMinCol){
this._currentMinCol=_5d;
}
if(_5d>this._nextMinCol){
this._nextMinCol=_5d;
}
this._connectResizeColumnMove=dojo.connect(dojo.doc,"onmousemove",this,"_resizeColumnMove");
this._connectOnGripMouseUp=dojo.connect(dojo.doc,"onmouseup",this,"_onGripMouseUp");
},_onGripMouseUp:function(){
dojo.body().style.cursor="default";
dojo.disconnect(this._connectResizeColumnMove);
dojo.disconnect(this._connectOnGripMouseUp);
this._connectOnGripMouseUp=this._connectResizeColumnMove=null;
if(this._activeGrip){
dojo.removeClass(this._activeGrip,"gridContainerGripShow");
dojo.addClass(this._activeGrip,"gridContainerGrip");
}
this._isResized=false;
},_resizeColumnMove:function(e){
e.preventDefault();
if(!this._connectResizeColumnOff){
dojo.disconnect(this._connectOnGripMouseUp);
this._connectOnGripMouseUp=null;
this._connectResizeColumnOff=dojo.connect(dojo.doc,"onmouseup",this,"_resizeColumnOff");
}
var d=e.pageX-this._initX;
if(d==0){
return;
}
if(!(this._currentColumnWidth+d<this._currentMinCol||this._nextColumnWidth-d<this._nextMinCol)){
this._currentColumnWidth+=d;
this._nextColumnWidth-=d;
this._initX=e.pageX;
this._activeGrip.style.left=parseInt(this._activeGrip.style.left)+d+"px";
if(this.liveResizeColumns){
this._currentColumn.style["width"]=this._currentColumnWidth+"px";
this._nextColumn.style["width"]=this._nextColumnWidth+"px";
this.resize();
}
}
},_resizeColumnOff:function(e){
dojo.body().style.cursor="default";
dojo.disconnect(this._connectResizeColumnMove);
dojo.disconnect(this._connectResizeColumnOff);
this._connectResizeColumnOff=this._connectResizeColumnMove=null;
if(!this.liveResizeColumns){
this._currentColumn.style["width"]=this._currentColumnWidth+"px";
this._nextColumn.style["width"]=this._nextColumnWidth+"px";
}
var _5e=[],_5f=[],_60=this.gridContainerTable.clientWidth,_61,_62=false,i;
for(i=0;i<this._grid.length;i++){
_61=this._grid[i].node;
if(dojo.isIE){
_5e[i]=dojo.marginBox(_61).w;
_5f[i]=dojo.contentBox(_61).w;
}else{
_5e[i]=dojo.contentBox(_61).w;
_5f=_5e;
}
}
for(i=0;i<_5f.length;i++){
if(_5f[i]!=this._oldTabSize[i]){
_62=true;
break;
}
}
if(_62){
var mul=dojo.isIE?100:10000;
for(i=0;i<this._grid.length;i++){
this._grid[i].node.style.width=Math.round((100*mul*_5e[i])/_60)/mul+"%";
}
this.resize();
}
if(this._activeGrip){
dojo.removeClass(this._activeGrip,"gridContainerGripShow");
dojo.addClass(this._activeGrip,"gridContainerGrip");
}
this._isResized=false;
},setColumns:function(_63){
var z,j;
if(_63>0){
var _64=this._grid.length,_65=_64-_63;
if(_65>0){
var _66=[],_67,_68,end,_69;
if(this.mode=="right"){
end=(this.isLeftFixed&&_64>0)?1:0;
_68=(this.isRightFixed)?_64-2:_64-1;
for(z=_68;z>=end;z--){
_69=0;
_67=this._grid[z].node;
for(j=0;j<_67.childNodes.length;j++){
if(_67.childNodes[j].nodeType==1&&!(_67.childNodes[j].id=="")){
_69++;
break;
}
}
if(_69==0){
_66[_66.length]=z;
}
if(_66.length>=_65){
this._deleteColumn(_66);
break;
}
}
if(_66.length<_65){
dojo.publish("/dojox/layout/gridContainer/noEmptyColumn",[this]);
}
}else{
_68=(this.isLeftFixed&&_64>0)?1:0;
end=(this.isRightFixed)?_64-1:_64;
for(z=_68;z<end;z++){
_69=0;
_67=this._grid[z].node;
for(j=0;j<_67.childNodes.length;j++){
if(_67.childNodes[j].nodeType==1&&!(_67.childNodes[j].id=="")){
_69++;
break;
}
}
if(_69==0){
_66[_66.length]=z;
}
if(_66.length>=_65){
this._deleteColumn(_66);
break;
}
}
if(_66.length<_65){
dojo.publish("/dojox/layout/gridContainer/noEmptyColumn",[this]);
}
}
}else{
if(_65<0){
this._addColumn(Math.abs(_65));
}
}
if(this.hasResizableColumns){
this._placeGrips();
}
}
},_addColumn:function(_6a){
var _6b=this._grid,_6c,_6d,_6e,_6f,_70=(this.mode=="right"),_71=this.acceptTypes.join(","),m=this._dragManager;
if(this.hasResizableColumns&&((!this.isRightFixed&&_70)||(this.isLeftFixed&&!_70&&this.nbZones==1))){
this._createGrip(_6b.length-1);
}
for(var i=0;i<_6a;i++){
_6d=dojo.create("td",{"class":"gridContainerZone dojoxDndArea","accept":_71,"id":this.id+"_dz"+this.nbZones});
_6f=_6b.length;
if(_70){
if(this.isRightFixed){
_6e=_6f-1;
_6b.splice(_6e,0,{"node":_6b[_6e].node.parentNode.insertBefore(_6d,_6b[_6e].node)});
}else{
_6e=_6f;
_6b.push({"node":this.gridNode.appendChild(_6d)});
}
}else{
if(this.isLeftFixed){
_6e=(_6f==1)?0:1;
this._grid.splice(1,0,{"node":this._grid[_6e].node.parentNode.appendChild(_6d,this._grid[_6e].node)});
_6e=1;
}else{
_6e=_6f-this.nbZones;
this._grid.splice(_6e,0,{"node":_6b[_6e].node.parentNode.insertBefore(_6d,_6b[_6e].node)});
}
}
if(this.hasResizableColumns){
if((!_70&&this.nbZones!=1)||(!_70&&this.nbZones==1&&!this.isLeftFixed)||(_70&&i<_6a-1)||(_70&&i==_6a-1&&this.isRightFixed)){
this._createGrip(_6e);
}
}
m.registerByNode(_6b[_6e].node);
this.nbZones++;
}
this._updateColumnsWidth(m);
},_deleteColumn:function(_72){
var _73,_74,_75,_76=0,_77=_72.length,m=this._dragManager;
for(var i=0;i<_77;i++){
_75=(this.mode=="right")?_72[i]:_72[i]-_76;
_74=this._grid[_75];
if(this.hasResizableColumns&&_74.grip){
dojo.forEach(_74.gripHandler,function(_78){
dojo.disconnect(_78);
});
dojo.destroy(this.domNode.removeChild(_74.grip));
_74.grip=null;
}
m.unregister(_74.node);
dojo.destroy(this.gridNode.removeChild(_74.node));
this._grid.splice(_75,1);
this.nbZones--;
_76++;
}
var _79=this._grid[this.nbZones-1];
if(_79.grip){
dojo.forEach(_79.gripHandler,dojo.disconnect);
dojo.destroy(this.domNode.removeChild(_79.grip));
_79.grip=null;
}
this._updateColumnsWidth(m);
},_updateColumnsWidth:function(_7a){
this.inherited(arguments);
_7a._dropMode.updateAreas(_7a._areaList);
},destroy:function(){
dojo.unsubscribe(this._dropHandler);
this.inherited(arguments);
}});
}
if(!dojo._hasResource["dojox.widget.Portlet"]){
dojo._hasResource["dojox.widget.Portlet"]=true;
dojo.experimental("dojox.widget.Portlet");
require(["dijit/TitlePane"]);
dojo.provide("dojox.widget.Portlet");
dojo.declare("dojox.widget.Portlet",[dijit.TitlePane,dijit._Container],{resizeChildren:true,closable:true,_parents:null,_size:null,dragRestriction:false,dndType:"Portlet",buildRendering:function(){
this.inherited(arguments);
dojo.style(this.domNode,"visibility","hidden");
dojo.removeClass(this.arrowNode,"dijitArrowNode");
},postCreate:function(){
this.inherited(arguments);
var _7b=this;
if(this.resizeChildren){
this.subscribe("/dnd/drop",function(){
_7b._updateSize();
});
this.subscribe("/Portlet/sizechange",function(_7c){
_7b.onSizeChange(_7c);
});
this.connect(window,"onresize",function(){
_7b._updateSize();
});
var _7d=dojo.hitch(this,function(id,_7e){
var _7f=dijit.byId(id);
if(_7f.selectChild){
var s=this.subscribe(id+"-selectChild",function(_80){
var n=_7b.domNode.parentNode;
while(n){
if(n==_80.domNode){
_7b.unsubscribe(s);
_7b._updateSize();
break;
}
n=n.parentNode;
}
});
var _81=dijit.byId(_7e);
if(_7f&&_81){
_7b._parents.push({parent:_7f,child:_81});
}
}
});
var _82;
this._parents=[];
for(var p=this.domNode.parentNode;p!=null;p=p.parentNode){
var id=p.getAttribute?p.getAttribute("widgetId"):null;
if(id){
_7d(id,_82);
_82=id;
}
}
}
this.connect(this.titleBarNode,"onmousedown",function(evt){
if(dojo.hasClass(evt.target,"dojoxPortletIcon")){
dojo.stopEvent(evt);
return false;
}
return true;
});
this.connect(this._wipeOut,"onEnd",function(){
_7b._publish();
});
this.connect(this._wipeIn,"onEnd",function(){
_7b._publish();
});
if(this.closable){
this.closeIcon=this._createIcon("dojoxCloseNode","dojoxCloseNodeHover",dojo.hitch(this,"onClose"));
dojo.style(this.closeIcon,"display","");
}
},startup:function(){
if(this._started){
return;
}
var _83=this.getChildren();
this._placeSettingsWidgets();
dojo.forEach(_83,function(_84){
try{
if(!_84.started&&!_84._started){
_84.startup();
}
}
catch(e){
console.log(this.id+":"+this.declaredClass,e);
}
});
this.inherited(arguments);
dojo.style(this.domNode,"visibility","visible");
},_placeSettingsWidgets:function(){
dojo.forEach(this.getChildren(),dojo.hitch(this,function(_85){
if(_85.portletIconClass&&_85.toggle&&!_85.attr("portlet")){
this._createIcon(_85.portletIconClass,_85.portletIconHoverClass,dojo.hitch(_85,"toggle"));
dojo.place(_85.domNode,this.containerNode,"before");
_85.attr("portlet",this);
}
}));
},_createIcon:function(_86,_87,fn){
var _88=dojo.create("div",{"class":"dojoxPortletIcon "+_86,"waiRole":"presentation"});
dojo.place(_88,this.arrowNode,"before");
this.connect(_88,"onclick",fn);
if(_87){
this.connect(_88,"onmouseover",function(){
});
this.connect(_88,"onmouseout",function(){
dojo.removeClass(_88,_87);
});
}
return _88;
},onClose:function(evt){
dojo.style(this.domNode,"display","none");
dojo.attr(this.domNode,"closed","true");
dojo.publish("/dojox/mdnd/close",[this]);
},onSizeChange:function(_89){
if(_89==this){
return;
}
this._updateSize();
},_updateSize:function(){
if(!this.open||!this._started||!this.resizeChildren){
return;
}
if(this._timer){
clearTimeout(this._timer);
}
this._timer=setTimeout(dojo.hitch(this,function(){
var _8a={w:dojo.style(this.domNode,"width"),h:dojo.style(this.domNode,"height")};
for(var i=0;i<this._parents.length;i++){
var p=this._parents[i];
var sel=p.parent.selectedChildWidget;
if(sel&&sel!=p.child){
return;
}
}
if(this._size){
if(this._size.w==_8a.w&&this._size.h==_8a.h){
return;
}
}
this._size=_8a;
var fns=["resize","layout"];
this._timer=null;
var _8b=this.getChildren();
dojo.forEach(_8b,function(_8c){
for(var i=0;i<fns.length;i++){
if(dojo.isFunction(_8c[fns[i]])){
try{
_8c[fns[i]]();
}
catch(e){
console.log(e);
}
break;
}
}
});
this.onUpdateSize();
}),100);
},onUpdateSize:function(){
},_publish:function(){
dojo.publish("/Portlet/sizechange",[this]);
},_onTitleClick:function(evt){
if(evt.target==this.arrowNode){
this.inherited(arguments);
}
},addChild:function(_8d){
this._size=null;
this.inherited(arguments);
if(this._started){
this._placeSettingsWidgets();
this._updateSize();
}
if(this._started&&!_8d.started&&!_8d._started){
_8d.startup();
}
},_setCss:function(){
this.inherited(arguments);
dojo.style(this.arrowNode,"display",this.toggleable?"":"none");
}});
dojo.declare("dojox.widget.PortletSettings",[dijit._Container,dijit.layout.ContentPane],{portletIconClass:"dojoxPortletSettingsIcon",portletIconHoverClass:"dojoxPortletSettingsIconHover",buildRendering:function(){
this.inherited(arguments);
dojo.style(this.domNode,"display","none");
dojo.removeClass(this.domNode,"dijitContentPane");
},postCreate:function(){
},_setPortletAttr:function(_8e){
this.portlet=_8e;
},toggle:function(){
var n=this.domNode;
if(dojo.style(n,"display")=="none"){
dojo.style(n,{"display":"block","height":"1px","width":"auto"});
dojo.fx.wipeIn({node:n}).play();
}else{
dojo.fx.wipeOut({node:n,onEnd:dojo.hitch(this,function(){
dojo.style(n,{"display":"none","height":"","width":""});
})}).play();
}
}});
dojo.declare("dojox.widget.PortletDialogSettings",dojox.widget.PortletSettings,{dimensions:null,constructor:function(_8f,_90){
this.dimensions=_8f.dimensions||[300,100];
},toggle:function(){
if(!this.dialog){
require(["dijit/Dialog"]);
this.dialog=new dijit.Dialog({title:this.title});
dojo.body().appendChild(this.dialog.domNode);
this.dialog.containerNode.appendChild(this.domNode);
dojo.style(this.dialog.domNode,{"width":this.dimensions[0]+"px","height":this.dimensions[1]+"px"});
dojo.style(this.domNode,"display","");
}
if(this.dialog.open){
this.dialog.hide();
}else{
this.dialog.show(this.domNode);
}
}});
}

