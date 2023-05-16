dojo.require("curam.util.ResourceBundle");
dojo.requireLocalization("curam.application","SVGText");
var bundle=new curam.util.ResourceBundle("SVGText");
var rootNodesHash=new Array();
var Y_HEIGHT=20;
var INDENT=20;
var PRE_TEXT_ADJUST=32;
var TEXT_WIDTH=300;
var TEXT_WIDTH_SUMMARY=500;
var LINE_COLOUR="gray";
var EMPTY_TREE=bundle.getProperty("EMPTY_TREE");
var hyperLinkText=false;
var popupGroup=null;
var isIE11=/rv:11./i.test(navigator.userAgent)&&(navigator.msMaxTouchPoints!==void 0);
dojo.connect(window,"onresize",resizeTreeContainers);
RootTreeNode.prototype=new TreeNode();
RootTreeNode.prototype.constructor=RootTreeNode;
RootTreeNode.superclass=TreeNode.prototype;
function TreeNode(_1,x,y,id,_2,_3,_4,_5,_6){
if(arguments.length>0){
this.construct(_1,x,y,id,_2,_3,_4,_5,_6);
}
};
TreeNode.prototype.construct=function(_7,x,y,id,_8,_9,_a,_b,_c){
this.text=_7;
this.x=x;
this.y=y;
this.id=id;
this.parent=_8;
this.image=_9;
this.preText=_a;
this.link=_b;
this.helpLink=_c;
this.open=false;
this.children=new Array();
this.height=Y_HEIGHT;
this.showing=false;
this.groupNode=null;
this.index=0;
this.lines=null;
this.whenDrawOpen=false;
this.objectiveText=null;
this.preTextAdjust=false;
this.root=null;
this.setPlusMinus=isIE11?this.setPlusMinus11:this.setPlusMinus;
};
TreeNode.prototype.drawNode=function(_d){
var _e=0;
this.showing=true;
this.appendToDocument(_d);
if(this.whenDrawOpen){
this.whenDrawOpen=false;
this.open=true;
this.openPlusMinus();
_e=this.showChildNodes();
this.moveNodes(_e);
}
};
TreeNode.prototype.openNode=function(){
this.whenDrawOpen=true;
};
TreeNode.prototype.addObjectiveText=function(_f){
this.objectiveText=_f;
};
TreeNode.prototype.writeObjectiveText=function(x){
var _10=this.root.svgDocument.createElementNS(SVG_NS,"text");
_10.setAttribute("x",x);
_10.setAttribute("y",this.y+2+this.height);
_10.setAttribute("class","objectiveText");
_10.setAttribute("font-family",FONT_FAMILY);
_10.appendChild(this.root.svgDocument.createTextNode(this.objectiveText));
this.groupNode.appendChild(_10);
};
TreeNode.prototype.remove=function(){
if(this.showing){
this.showing=false;
this.groupNode.parentNode.removeChild(this.groupNode);
}
};
TreeNode.prototype.removeAllChildren=function(){
for(var i=0;i<this.children.length;i++){
if(this.children[i].showing){
this.children[i].removeAllChildren();
this.children[i].open=false;
this.children[i].remove();
}
}
};
TreeNode.prototype.a=function(_11,_12,_13,_14,_15,_16,_17){
var _18;
var _19;
if(this.root.suffix!==null){
var _1a=_11+"--"+this.root.suffix;
}else{
var _1a=_11;
}
_19=editPages[_16];
if(_19==""){
_18=null;
}else{
_18=_19+"Page.do?id="+_11+"&rulesetId="+this.root.rulesetId+"&"+this.root.o3rpu+"&"+this.root.decisionIdParam;
}
if(_17&&_17.length==0){
_18=null;
}
var _1b=null;
var _1c=null;
_1b=this.root.hash[_12];
if(_1b==null){
_1b=this.root;
}
_1c=new TreeNode(_13,_1b.x,_1b.y,_1a,_1b,_14+_16,_15,_18,_17);
_1c.root=this.root;
this.root.hash[_1a]=_1c;
_1b.children.push(_1c);
_1c.index=_1b.children.length;
};
function xmlUnescape(_1d){
var re=/&#38;/g;
_1d=_1d.replace(re,"&");
re=/&#60;/g;
_1d=_1d.replace(re,"<");
re=/&#62;/g;
_1d=_1d.replace(re,">");
re=/&#34;/g;
_1d=_1d.replace(re,"\"");
re=/&#39;/g;
_1d=_1d.replace(re,"'");
re=/&amp;/g;
_1d=_1d.replace(re,"&");
re=/&lt;/g;
_1d=_1d.replace(re,"<");
re=/&gt;/g;
_1d=_1d.replace(re,">");
re=/&quot;/g;
_1d=_1d.replace(re,"\"");
re=/&apos;/g;
_1d=_1d.replace(re,"'");
return _1d;
};
TreeNode.prototype.toggleNode=function(evt){
var id=evt.currentTarget.getAttribute("id");
var _1e=id.substr(0,id.indexOf("__$$__"));
var _1f=id.substr(id.indexOf("__$$__")+6,id.length);
var _20=rootNodesHash[_1e];
var _21=_20.hash[_1f];
_21.open=!_21.open;
_21.setPlusMinus(evt.currentTarget,true);
var _22=0;
if(_21.open){
_22=_21.showChildNodes();
_21.moveNodes(_22);
}else{
_22=_21.measureChildNodes();
_21.root.lowestNode=_21;
_21.removeAllChildren();
_21.moveNodes(-_22);
}
_21.root.adjustWindowWidth();
_21.root.adjustWindowHeight();
};
TreeNode.prototype.displayPopUp=function(evt){
var _23=null;
var _24=null;
var _25=null;
var _26=0;
var _27=null;
if(!isNative()&&popupGroup==null){
_27=evt.currentTarget.getAttribute("popup-text-content");
if(_27!=null&&_27.length>0){
_27=returnPopupText(_27);
popupGroup=evt.currentTarget.ownerDocument.createElementNS(SVG_NS,"use");
popupGroup.setAttributeNS(XLINK_NS,"xlink:href","#popup");
popupGroup.setAttribute("x",evt.clientX+5);
popupGroup.setAttribute("y",parseInt(evt.currentTarget.getAttribute("y"))+24);
_24=evt.currentTarget.ownerDocument.getElementById("popup-text");
_24.removeChild(_24.firstChild);
_24.setAttribute("font-family",FONT_FAMILY);
_24.appendChild(evt.currentTarget.ownerDocument.createTextNode(_27));
try{
_26=_24.getComputedTextLength();
}
catch(e){
if(e.name!="RangeError"){
if(isNative()){
_26=fallbackEval(evt.currentTarget.ownerDocument,_24);
}else{
alert("SVG Error: "+e.name);
}
}
}
if(_26>0){
_25=evt.currentTarget.ownerDocument.getElementById("popup-rect");
_25.setAttribute("width",_26+12);
_23=evt.currentTarget.ownerDocument.getElementById("popup-rect-anim");
_23.beginElement();
_23=evt.currentTarget.ownerDocument.getElementById("popup-text-anim");
_23.beginElement();
evt.currentTarget.ownerDocument.documentElement.appendChild(popupGroup);
}
}
}
};
TreeNode.prototype.removePopUp=function(evt){
if(!isNative()&&popupGroup!=null){
evt.currentTarget.ownerDocument.documentElement.removeChild(popupGroup);
popupGroup=null;
}
};
TreeNode.prototype.showChildNodes=function(){
var _28=0;
for(var i=0;i<this.children.length;i++){
this.children[i].showing=true;
this.children[i].y=this.y+_28;
this.children[i].drawNode(true);
_28+=this.children[i].height;
}
return _28;
};
TreeNode.prototype.measureChildNodes=function(){
var _29=0;
for(var i=0;i<this.children.length;i++){
if(this.children[i].showing){
_29+=this.children[i].measureChildNodes();
_29+=this.children[i].height;
}
}
return _29;
};
TreeNode.prototype.getDepth=function(){
var _2a=0;
var _2b=0;
for(var i=0;i<this.children.length;i++){
if(this.children[i].showing){
_2b=1;
_2b+=this.children[i].getDepth();
}
if(_2b>_2a){
_2a=_2b;
}
}
return _2a;
};
TreeNode.prototype.changeNode=function(_2c){
this.y+=_2c;
this.remove();
this.drawNode(false);
};
TreeNode.prototype.moveNodes=function(_2d){
var _2e=this.root.hash;
var _2f=null;
var _30=false;
for(var i in _2e){
if(_2e[i].showing&&_2e[i].y>this.y){
_2f=_2e[i];
while(_2f!=null){
if(_2f==this){
_30=true;
_2f=null;
break;
}
_2f=_2f.parent;
}
if(!_30){
_2e[i].changeNode(_2d);
}
_30=false;
}
}
};
TreeNode.prototype.appendToDocument=function(_31){
var _32=null;
var _33=null;
var _34=null;
var _35=null;
var _36=null;
var _37=false;
var bar=null;
var _38=0;
var _39=null;
var _3a=null;
var _3b=0;
this.groupNode=this.root.svgDocument.createElementNS(SVG_NS,"g");
if(_31){
this.calculateY();
}
this.calculateX();
_38=this.addPreText();
if(this.helpLink){
_3b=this.addHelpLink(_38);
}
_3a=new TextWrap(this.x+_38+43+_3b,this.y+9,this,this.root.svgDocument,this.root.textNodeWidth);
this.height=_3a.getTextHeight();
_32=_3a.getTextNode();
if(this.objectiveText){
this.writeObjectiveText(this.x+_38+43);
this.height+=10;
}
if(this.children.length>0){
_34=this.root.svgDocument.createElementNS(SVG_NS,"use");
this.setPlusMinus(_34,false);
_34.setAttributeNS(null,"x",this.x);
_34.setAttributeNS(null,"y",this.y);
_34.setAttributeNS(null,"id",this.root.id+"__$$__"+this.id);
_34.addEventListener("click",this.toggleNode,false);
}else{
_37=true;
}
_35=this.root.svgDocument.createElementNS(SVG_NS,"use");
_35.setAttributeNS(XLINK_NS,"xlink:href","#"+this.image);
_35.setAttribute("x",this.x+_38+17);
_35.setAttribute("y",this.y-4);
_35.addEventListener("mouseover",this.displayPopUp,false);
_35.addEventListener("mouseout",this.removePopUp,false);
_35.setAttribute("popup-text-content",this.image);
_36=this.root.svgDocument.createElementNS(SVG_NS,"use");
if(this.parent!=this.root){
if(_37&&this.index!=this.parent.children.length){
bar=this.root.svgDocument.createElementNS(SVG_NS,"use");
bar.setAttributeNS(XLINK_NS,"xlink:href","#bar");
bar.setAttributeNS(null,"x",this.x);
bar.setAttributeNS(null,"y",this.y);
this.groupNode.appendChild(bar);
}
_36.setAttributeNS(XLINK_NS,"xlink:href","#el");
}else{
if(this.children.length>0){
_36.setAttributeNS(XLINK_NS,"xlink:href","#dash");
}
}
_36.setAttributeNS(null,"x",this.x+4);
_36.setAttributeNS(null,"y",this.y);
if(this.link){
_39=this.root.svgDocument.createElementNS(SVG_NS,"a");
_39.setAttribute("onclick","parent.openLinkInNewWindow('"+this.link+"')");
_39.setAttribute("target","_blank");
if(hyperLinkText){
_39.appendChild(_32);
_39.appendChild(_35);
_32=_39;
}else{
_39.appendChild(_35);
this.groupNode.appendChild(_39);
}
}else{
this.groupNode.appendChild(_35);
}
this.completeTree();
this.groupNode.appendChild(_36);
this.groupNode.appendChild(_32);
if(_34!=null){
this.groupNode.appendChild(_34);
}
this.root.svgDocument.documentElement.appendChild(this.groupNode);
this.checkLowestNode();
};
TreeNode.prototype.checkLowestNode=function(){
if(this.root.lowestNode==null){
this.root.lowestNode=this;
}else{
if(this.root.lowestNode.y<this.y){
this.root.lowestNode=this;
}
}
};
TreeNode.prototype.adjustWindowHeight=function(){
var _3c=null;
_3c=this.root.lowestNode.y+this.root.lowestNode.height+40;
dojo.byId(this.root.id).height=_3c;
};
TreeNode.prototype.adjustWindowWidth=function(){
var _3d;
_3d=(INDENT+PRE_TEXT_ADJUST)*this.root.getDepth()+this.root.textNodeWidth;
dojo.byId(this.root.id).width=_3d;
};
TreeNode.prototype.calculateX=function(){
var _3e=this.parent;
this.x=_3e.x+INDENT;
if(_3e.preTextAdjust){
this.x+=PRE_TEXT_ADJUST;
}
};
TreeNode.prototype.calculateY=function(){
var _3f=null;
var _40=0;
if(this.index>1){
_3f=this.parent.children[this.index-2];
_40=_3f.y+_3f.height;
if(_3f.open){
_40+=_3f.measureChildNodes();
}
}else{
_40=this.parent.y+this.parent.height;
}
this.y=_40;
};
TreeNode.prototype.setPlusMinus=function(_41,_42){
var _43=this.open?"#minus":"#plus";
var _44=_41.parentNode;
if(isNative()&&_44){
_41.parentNode.insertBefore(_41,_41);
}
_41.setAttributeNS(XLINK_NS,"xlink:href",_43);
};
TreeNode.prototype.setPlusMinus11=function(_45,_46){
var _47=_45.parentNode;
var _48=this.open?"#minus":"#plus";
if(_46&&_47){
_47.appendChild(this.nextPlusMinus(_45,_48),_45);
}else{
_45.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xlink",XLINK_NS);
_45.setAttributeNS(XLINK_NS,"xlink:href",_48);
}
};
TreeNode.prototype.nextPlusMinus=function(_49,_4a){
var _4b=_49.getAttribute("x");
var ord=_49.getAttribute("y");
var id1=_49.getAttribute("id");
var _4c=this.root.svgDocument.createElementNS(SVG_NS,"use");
_4c.setAttributeNS(null,"id",id1);
_4c.setAttributeNS(null,"x",_4b);
_4c.setAttributeNS(null,"y",ord);
_49.removeEventListener("click",this.toggleNode);
_49.showing=false;
_4c.addEventListener("click",this.toggleNode,false);
_4c.setAttributeNS(XLINK_NS,"xlink:href",_4a);
return _4c;
};
TreeNode.prototype.openPlusMinus=function(){
var _4d=null;
_4d=this.groupNode.lastChild;
if(_4d.getAttribute("id")==this.root.id+"__$$__"+this.id){
_4d.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:xlink",XLINK_NS);
_4d.setAttributeNS(XLINK_NS,"xlink:href","#minus");
}else{
}
};
RootTreeNode.prototype.initialOpenNode=function(_4e){
openNode=this.hash[_4e];
if(openNode!=null){
openNode.openToNode();
}
};
TreeNode.prototype.openToNode=function(){
this.whenDrawOpen=true;
if(this.id!=this.root.id){
parentNode=this.root.hash[this.parent.id];
parentNode.openToNode();
}
};
TreeNode.prototype.showChildNodes=function(){
var _4f=0;
for(var i=0;i<this.children.length;i++){
this.children[i].showing=true;
this.children[i].y=this.y+_4f;
this.children[i].drawNode(true);
_4f+=this.children[i].height;
}
return _4f;
};
TreeNode.prototype.completeTree=function(){
var _50=this.parent;
var _51=null;
var _52=0;
if(_50!=this.root){
if(this.index==1){
this.drawVerticalLine(this.x+4,this.y-7,this.y-this.parent.height+Y_HEIGHT-7);
}else{
_51=_50.children[this.index-2];
this.drawVerticalLine(this.x+4,this.y-7,_51.y+8);
}
}
};
TreeNode.prototype.drawVerticalLine=function(x,y1,y2){
var _53=this.root.svgDocument.createElementNS(SVG_NS,"line");
_53.setAttributeNS(null,"x1",x);
_53.setAttributeNS(null,"y1",y1);
_53.setAttributeNS(null,"x2",x);
_53.setAttributeNS(null,"y2",y2);
_53.setAttributeNS(null,"stroke",LINE_COLOUR);
_53.setAttributeNS(null,"class","crisp");
this.groupNode.appendChild(_53);
};
TreeNode.prototype.drawHorizontalLine=function(x1,x2,y){
var _54=this.root.svgDocument.createElementNS(SVG_NS,"line");
_54.setAttributeNS(null,"x1",x1);
_54.setAttributeNS(null,"y1",y);
_54.setAttributeNS(null,"x2",x2);
_54.setAttributeNS(null,"y2",y);
_54.setAttributeNS(null,"stroke",LINE_COLOUR);
_54.setAttributeNS(null,"class","crisp");
this.groupNode.appendChild(_54);
};
TreeNode.prototype.addPreText=function(){
var _55=0;
var _56=0;
var _57=null;
var bar=null;
var _58=null;
if(this.preText&&this.preText!=""){
if(this.parent==this.root){
return 0;
}
if(this.index>1){
_57=this.root.svgDocument.createElementNS(SVG_NS,"text");
_57.setAttributeNS(null,"x",this.x+18);
_57.setAttributeNS(null,"y",this.y+9);
_57.setAttributeNS(null,"class","preText");
_57.setAttribute("font-family",FONT_FAMILY);
_58=this.root.svgDocument.createTextNode(this.preText);
_57.appendChild(_58);
try{
_56=_57.getComputedTextLength();
if(_56==0){
_56=this.preText.length;
}
}
catch(e){
if(e.name!="RangeError"){
if(isNative()){
_56=fallbackEval(this.root.svgDocument,_57);
}else{
alert("SVG Error: "+e.name);
}
}
}
if(_56>PRE_TEXT_ADJUST){
return 0;
}
this.groupNode.appendChild(_57);
this.drawHorizontalLine(this.x+_56+41,this.x+PRE_TEXT_ADJUST+14,this.y+4);
this.preTextAdjust=true;
_55=PRE_TEXT_ADJUST;
}else{
if(this.parent.children.length>1){
if(this.preText.length>3){
return 0;
}
_55=PRE_TEXT_ADJUST;
this.preTextAdjust=true;
this.drawHorizontalLine(this.x+14,this.x+46,this.y+4);
}
}
}
return _55;
};
TreeNode.prototype.addHelpLink=function(_59){
var _5a=null;
var _5b=null;
_5a=this.root.svgDocument.createElementNS(SVG_NS,"use");
_5a.setAttributeNS(XLINK_NS,"xlink:href","#help-icon");
_5a.setAttribute("x",this.x+_59+36);
_5a.setAttribute("y",this.y-4);
_5a.addEventListener("mouseover",this.displayPopUp,false);
_5a.addEventListener("mouseout",this.removePopUp,false);
_5a.setAttribute("popup-text-content","l");
_5b=this.root.svgDocument.createElementNS(SVG_NS,"a");
_5b.setAttribute("onclick","parent.openLinkInNewWindow('"+this.helpLink+"')");
_5b.setAttribute("target","_blank");
_5b.appendChild(_5a);
this.groupNode.appendChild(_5b);
return 5;
};
TreeNode.prototype.getWindowWidth=function(){
return this.root.windowWidth;
};
function RootTreeNode(id,_5c,_5d,_5e,_5f,_60,_61,_62,_63){
if(arguments.length>0){
this.construct(id,_5c,_5d,_5e,_5f,_60,_61,_62,_63);
}
};
RootTreeNode.prototype.construct=function(id,_64,_65,_66,_67,_68,_69,_6a,_6b){
this.windowWidth=_64;
this.svgDocument=_65;
this.hash=_66;
this.summaryInd=_67;
this.o3rpu=_68;
this.rulesetId=_69;
this.decisionIdParam=_6a;
this.suffix=_6b;
if(_67){
this.textNodeWidth=TEXT_WIDTH_SUMMARY;
}else{
this.textNodeWidth=TEXT_WIDTH;
}
var _6c=dojo.byId(id).parentNode;
var _6d=_6c.offsetWidth;
_6c.width=_6d;
var _6e;
_6e=(INDENT+PRE_TEXT_ADJUST)+this.textNodeWidth;
dojo.byId(id).width=_6e;
RootTreeNode.superclass.construct.call(this,"",-10,0,id,null,null,null,null,null);
this.root=this;
var _6f=null;
this.hash[id]=this;
};
function setDocumentRoot(_70){
return true;
};
function resizeTreeContainers(){
var _71;
var _72=getElementsByClass("rulestree-container",document,"div");
for(var i=0;i<_72.length;i++){
_72[i].width="100%";
_71=_72[i].offsetWidth;
_72[i].width=_71+"px";
}
};
function getElementsByClass(_73,_74,tag){
var _75=new Array();
if(_74==null){
_74=document;
}
if(tag==null){
tag="*";
}
var els=_74.getElementsByTagName(tag);
var _76=els.length;
var _77=new RegExp("(^|\\s)"+_73+"(\\s|$)");
for(var i=0,j=0;i<_76;i++){
if(_77.test(els[i].className)){
_75[j]=els[i];
j++;
}
}
return _75;
};
function openLinkInNewWindow(url){
dojo.require("curam.util.UimDialog");
curam.util.UimDialog.openUrl(url,{width:720,height:400});
};

