//>>built
define("curam/widget/orgTree/dtree",["dojo/dom","dojo/dom-style","dojo/_base/declare","curam/util"],function(_1,_2,_3){
function _4(id,_5,_6,_7,_8,_9,_a,_b,_c){
this.id=id;
this.pid=_5;
this.name=_6;
this.url=_7;
this.title=_8;
this.target=_9;
this.icon=_a;
this.iconOpen=_b;
this._io=_c||false;
this._is=false;
this._ls=false;
this._hc=false;
this._ai=0;
this._p;
};
var _d=_3("dTree",null,{config:null,icon:null,constructor:function(_e){
this.config={target:null,folderLinks:true,useSelection:true,useCookies:true,useLines:true,useIcons:true,useStatusText:false,closeSameLevel:false,inOrder:false};
this.icon={root:"../CDEJ/jscript/dTree/img/base.png",folder:"../CDEJ/jscript/dTree/img/folder.png",folderOpen:"../CDEJ/jscript/dTree/img/folder-open.png",node:"../CDEJ/jscript/dTree/img/page.png",empty:"../CDEJ/jscript/dTree/img/empty.gif",line:"../CDEJ/jscript/dTree/img/empty.gif",join:"../CDEJ/jscript/dTree/img/empty.gif",joinBottom:"../CDEJ/jscript/dTree/img/empty.gif",plus:"../CDEJ/jscript/dTree/img/plus.png",plusBottom:"../CDEJ/jscript/dTree/img/plus.png",minus:"../CDEJ/jscript/dTree/img/minus.png",minusBottom:"../CDEJ/jscript/dTree/img/minus.png",nlPlus:"../CDEJ/jscript/dTree/img/plus.png",nlMinus:"../CDEJ/jscript/dTree/img/minus.png"};
this.obj=_e;
this.aNodes=[];
this.aIndent=[];
this.root=new _4(-1);
this.selectedNode=null;
this.selectedFound=false;
this.completed=false;
window.currentTree=this;
},add:function(id,_f,_10,url,_11,_12,_13,_14,_15){
this.aNodes[this.aNodes.length]=new _4(id,_f,_10,url,_11,_12,_13,_14,_15);
},openAll:function(){
this.oAll(true);
},closeAll:function(){
this.oAll(false);
},toString:function(){
var str="<div class=\"dtree-container\">";
str+="<div class=\"dtree\">\n";
if(document.getElementById){
if(this.config.useCookies){
this.selectedNode=this.getSelected();
}
str+=this.addNode(this.root);
}else{
str+="Browser not supported.";
}
str+="</div></div>";
if(!this.selectedFound){
this.selectedNode=null;
}
this.completed=true;
return str;
},addNode:function(_16){
var str="";
var n=0;
if(this.config.inOrder){
n=_16._ai;
}
for(n;n<this.aNodes.length;n++){
if(this.aNodes[n].pid==_16.id){
var cn=this.aNodes[n];
cn._p=_16;
cn._ai=n;
this.setCS(cn);
if(!cn.target&&this.config.target){
cn.target=this.config.target;
}
if(cn._hc&&!cn._io&&this.config.useCookies){
cn._io=this.isOpen(cn.id);
}
if(!this.config.folderLinks&&cn._hc){
cn.url=null;
}
if(this.config.useSelection&&cn.id==this.selectedNode&&!this.selectedFound){
cn._is=true;
this.selectedNode=n;
this.selectedFound=true;
}
str+=this.node(cn,n);
if(cn._ls){
break;
}
}
}
return str;
},node:function(_17,_18){
var str="<div class=\"dTreeNode\">"+this.indent(_17,_18);
if(this.config.useIcons){
if(!_17.icon){
_17.icon=(this.root.id==_17.pid)?this.icon.root:((_17._hc)?this.icon.folder:this.icon.node);
}
if(!_17.iconOpen){
_17.iconOpen=(_17._hc)?this.icon.folderOpen:this.icon.node;
}
str+="<img id=\"i"+this.obj+_18+"\" src=\""+((_17._io)?_17.iconOpen:_17.icon)+"\" alt=\"\" />";
}
if(_17.url){
str+="<a href=\"javascript:void(0);\" id=\"s"+this.obj+_18+"\" class=\""+((this.config.useSelection)?((_17._is?"nodeSel":"node")):"node")+"\"";
if(_17.title){
str+=" title=\""+_17.title+"\"";
}
if(this.config.useStatusText){
str+=" onmouseover=\"window.status='"+_17.name+"';return true;\" onmouseout=\"window.status='';return true;\" ";
}
if(this.config.useSelection&&((_17._hc&&this.config.folderLinks)||!_17._hc)){
str+=" onclick=\""+this.obj+".loadContent('"+_17.url+"',"+_18+");\"";
}
str+=">";
}else{
if((!this.config.folderLinks||!_17.url)&&_17._hc&&_17.pid!=this.root.id){
str+="<a href=\"#\" onclick=\""+this.obj+".o("+_18+");\" class=\"node\">";
}
}
str+=_17.name;
if(_17.url||((!this.config.folderLinks||!_17.url)&&_17._hc)){
str+="</a>";
}
str+="</div>";
if(_17._hc){
str+="<div id=\"d"+this.obj+_18+"\" class=\"clip\" style=\"display:"+((this.root.id==_17.pid||_17._io)?"inline":"none")+";\">";
str+=this.addNode(_17);
str+="</div>";
}
this.aIndent.pop();
return str;
},indent:function(_19,_1a){
var str="";
if(this.root.id!=_19.pid){
for(var n=0;n<this.aIndent.length;n++){
str+="<img src=\""+((this.aIndent[n]==1&&this.config.useLines)?this.icon.line:this.icon.empty)+"\" alt=\"\" />";
}
(_19._ls)?this.aIndent.push(0):this.aIndent.push(1);
if(_19._hc){
str+="<a href=\"#\" onclick=\""+this.obj+".o("+_1a+");\"><img id=\"j"+this.obj+_1a+"\" src=\"";
if(!this.config.useLines){
str+=(_19._io)?this.icon.nlMinus:this.icon.nlPlus;
}else{
str+=((_19._io)?((_19._ls&&this.config.useLines)?this.icon.minusBottom:this.icon.minus):((_19._ls&&this.config.useLines)?this.icon.plusBottom:this.icon.plus));
}
str+="\" alt=\"\" /></a>";
}else{
str+="<img src=\""+((this.config.useLines)?((_19._ls)?this.icon.joinBottom:this.icon.join):this.icon.empty)+"\" alt=\"\" />";
}
}
return str;
},setCS:function(_1b){
var _1c;
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n].pid==_1b.id){
_1b._hc=true;
}
if(this.aNodes[n].pid==_1b.pid){
_1c=this.aNodes[n].id;
}
}
if(_1c==_1b.id){
_1b._ls=true;
}
},getSelected:function(){
var sn=this.getCookie("cs"+this.obj);
return (sn)?sn:null;
},s:function(id){
if(!this.config.useSelection){
return;
}
var cn=this.aNodes[id];
if(cn._hc&&!this.config.folderLinks){
return;
}
if(this.selectedNode!=id){
if(this.selectedNode||this.selectedNode==0){
eOld=document.getElementById("s"+this.obj+this.selectedNode);
eOld.className="node";
}
eNew=document.getElementById("s"+this.obj+id);
eNew.className="nodeSel";
this.selectedNode=id;
if(this.config.useCookies){
this.setCookie("cs"+this.obj,cn.id);
this.setCookie("cunique"+this.obj,cn.name);
}
}
},o:function(id){
var cn=this.aNodes[id];
this.nodeStatus(!cn._io,id,cn._ls);
cn._io=!cn._io;
if(this.config.closeSameLevel){
this.closeLevel(cn);
}
if(this.config.useCookies){
this.updateCookie();
}
},oAll:function(_1d){
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n]._hc&&this.aNodes[n].pid!=this.root.id){
this.nodeStatus(_1d,n,this.aNodes[n]._ls);
this.aNodes[n]._io=_1d;
}
}
if(this.config.useCookies){
this.updateCookie();
}
},openTo:function(nId,_1e,_1f){
if(!_1f){
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n].id==nId){
nId=n;
break;
}
}
}
var cn=this.aNodes[nId];
if(cn==null||!cn._p){
return;
}
cn._io=true;
cn._is=_1e;
if(this.completed&&cn._hc&&cn.pid!=this.root.id){
this.nodeStatus(true,cn._ai,cn._ls);
}
if(this.completed&&_1e){
this.s(cn._ai);
}else{
if(_1e){
this._sn=cn._ai;
}
}
if(cn.pid==this.root.id){
return;
}
this.openTo(cn._p._ai,false,true);
},closeLevel:function(_20){
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n].pid==_20.pid&&this.aNodes[n].id!=_20.id&&this.aNodes[n]._hc){
this.nodeStatus(false,n,this.aNodes[n]._ls);
this.aNodes[n]._io=false;
this.closeAllChildren(this.aNodes[n]);
}
}
},closeAllChildren:function(_21){
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n].pid==_21.id&&this.aNodes[n]._hc){
if(this.aNodes[n]._io){
this.nodeStatus(false,n,this.aNodes[n]._ls);
}
this.aNodes[n]._io=false;
this.closeAllChildren(this.aNodes[n]);
}
}
},nodeStatus:function(_22,id,_23){
eDiv=document.getElementById("d"+this.obj+id);
eJoin=document.getElementById("j"+this.obj+id);
if(this.config.useIcons){
eIcon=document.getElementById("i"+this.obj+id);
if(eIcon){
eIcon.src=(_22)?this.aNodes[id].iconOpen:this.aNodes[id].icon;
}
}
if(eJoin){
eJoin.src=(this.config.useLines)?((_22)?((_23)?this.icon.minusBottom:this.icon.minus):((_23)?this.icon.plusBottom:this.icon.plus)):((_22)?this.icon.nlMinus:this.icon.nlPlus);
}
if(eDiv){
eDiv.style.display=(_22)?"inline":"none";
}
},clearCookies:function(){
var now=new Date();
var _24=new Date(now.getTime()-1000*60*60*24);
this.setCookie("co"+this.obj,"cookieValue",_24);
this.setCookie("cs"+this.obj,"cookieValue",_24);
this.setCookie("cst"+this.obj,"cookieValue",_24);
this.setCookie("csunique"+this.obj,"cookieValue",_24);
},setCookie:function(_25,_26,_27,_28,_29,_2a){
document.cookie=escape(_25)+"="+escape(_26)+(_27?"; expires="+_27.toGMTString():"")+(_28?"; path="+_28:"")+(_29?"; domain="+_29:"")+(_2a?"; secure":"");
},getCookie:function(_2b){
var _2c="";
var _2d=document.cookie.indexOf(escape(_2b)+"=");
if(_2d!=-1){
var _2e=_2d+(escape(_2b)+"=").length;
var _2f=document.cookie.indexOf(";",_2e);
if(_2f!=-1){
_2c=unescape(document.cookie.substring(_2e,_2f));
}else{
_2c=unescape(document.cookie.substring(_2e));
}
}
return (_2c);
},updateCookie:function(){
var str="";
for(var n=0;n<this.aNodes.length;n++){
if(this.aNodes[n]._io&&this.aNodes[n].pid!=this.root.id){
if(str){
str+=".";
}
str+=this.aNodes[n].id;
}
}
this.setCookie("co"+this.obj,str);
},isOpen:function(id){
var _30=this.getCookie("co"+this.obj).split(".");
for(var n=0;n<_30.length;n++){
if(_30[n]==id){
return true;
}
}
return false;
},clearCookie:function(_31){
var now=new Date();
var _32=new Date(now.getTime()-1000*60*60*24);
this.setCookie(_31+this.obj,"cookieValue",_32);
},getLinkForNode:function(id){
var _33=-1;
if(this.aNodes[id]!=null){
return this.aNodes[id].url.replace(/&#38;/g,"&");
}else{
_33=id-1;
if(_33>-1){
return this.aNodes[_33].url.replace(/&#38;/g,"&");
}else{
return "";
}
}
},refreshIt:true,registerStart:function(_34){
if(this.config.useCookies){
var sn=this.getCookie("cst"+this.obj)||"";
if(sn!=""){
var _35=curam.util.getFrameRoot(window,"iegtree");
var _36=_35.contentframe||_35.frames[1];
var _37=(_36&&_36.dojo)?(_36.dojo.byId("rScript")==null):false;
if(sn==_34){
if(this.aNodes[_34]){
var _38=this.getCookie("cunique"+this.obj);
if(this.aNodes[_34].name==_38){
refreshIt=false;
return null;
}
if(!_37){
refreshIt=false;
return null;
}
}else{
this.clearCookie("cst");
return null;
}
}else{
if(!_37){
refreshIt=false;
return null;
}
}
}
this.setCookie("cst"+this.obj,_34);
}
return _34;
},loadContent:function(url,_39){
this.s(_39);
var _3a=curam.util.getFrameRoot(window,"iegtree");
var _3b=_3a.contentframe||_3a.frames[1];
_3b.location.href=url;
return false;
},setSelection:function(_3c,_3d){
window.treeNavigator=this;
var _3e=curam.util.getFrameRoot(window,"iegtree");
var _3f=_3e.contentframe||_3e.frames[1];
if(_3c.registerStart(_3d)!=null){
selected=_3d;
if(_3f.location.search.indexOf("&norefresh")>-1||_3f.location.search.indexOf("&amp;norefresh")>-1){
return;
}
_3f.location.href=_3c.getLinkForNode(selected);
}else{
if(_3c.getSelected()!=null){
selected=_3c.getSelected();
if(_3f.document.getElementById("rScript")){
if((_3f.location.href==_3c.getLinkForNode(selected))||!refreshIt){
refreshIt=true;
return;
}else{
if(_3f.location.search.indexOf("norefresh")==-1){
_3f.location.href=_3c.getLinkForNode(selected);
}
}
return;
}else{
_3f.location.href=_3c.getLinkForNode(selected);
}
}
}
},redrawTree:function(){
var _40=dojo.body().clientWidth;
dojo.style(dojo.body(),{width:(_40-1)+"px"});
}});
return _d;
});
