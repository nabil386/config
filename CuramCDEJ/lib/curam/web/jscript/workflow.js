var wf_model=null;
var svgDocument=null;
var nodes=new Array();
var max_X=0;
var max_Y=0;
var ellipsisLength=0;
var workflowLinkInfo=null;
var showInstanceData=false;
var splitsAndJoins=null;
var isIE=true;
var nodesToBeMirrored=new Array();
var textNodes=new Array();
var ACTIVITY_WIDTH=90;
var ACTIVITY_HEIGHT=60;
var ACT_WIDTH_HALF=ACTIVITY_WIDTH/2;
var ACT_HEIGHT_HALF=ACTIVITY_HEIGHT/2;
var TEXT_DEPTH=54;
var TEXT_BREADTH=6;
var ACTIVITY_TEXT_WIDTH=ACTIVITY_WIDTH-(TEXT_BREADTH+2);
var ACT_TYPE_DEPTH=12;
var ACT_TYPE_BREADTH=16;
var XOR_DEPTH=30;
var ARROW_WIDTH=8;
var GRID_WIDTH=130+ARROW_WIDTH;
var GRID_HEIGHT=62;
function loadWorkFlow(_1){
var _2=null;
var _3=new Array;
var _4=null;
var _5;
var _6=new Array;
var _7=new Array;
var _8;
svgDocument=document;
var _9=svgDocument.getElementById("rootSVG");
_9.onload=null;
var _a=parent.document.getElementById("__o3__SVGWorkflow").attributes;
for(var i=0;i<_a.length;i++){
if(_a[i].nodeName=="iebrowser"){
isIE=_a[i].nodeValue;
break;
}
}
init();
_2=getModel().firstChild.childNodes;
for(var j=0;j<_2.length;j++){
_4=_2.item(j);
if(_4.nodeName=="EDGE"){
_5=_4.getAttribute("SEGMENT");
if(_5!==null&&_5!=""){
_8="id_"+_4.getAttribute("RELATED-ID");
_7=_6[_8];
if(_7==null){
_7=[_5];
}else{
_7.push(_5);
}
_6[_8]=_7;
}else{
_3.push(_4);
}
}else{
if(_4.nodeName=="NODE"){
_3.push(_4);
}
}
}
for(var i=0;i<_3.length;i++){
_4=_3[i];
if(_4.nodeName=="NODE"){
drawNode(_4);
}else{
if(_4.nodeName=="EDGE"){
drawEdge(_4,i,_6);
}
}
}
svgDocument.rootElement.appendChild(splitsAndJoins);
setWidthAndHeight();
if(!parent.dojo.isBodyLtr()){
parent.direction="rtl";
svgWidth=parent.document.getElementById("__o3__SVGWorkflow").width;
for(var i=0;i<nodesToBeMirrored.length;i++){
_4=nodesToBeMirrored[i];
_4.setAttribute("transform","translate("+(svgWidth-4)+",0),scale(-1,1)");
}
for(var i=0;i<textNodes.length;i++){
_4=textNodes[i];
_4.setAttribute("x",svgWidth-_4.getAttribute("x")-ACT_WIDTH_HALF);
_4.setAttribute("text-anchor","middle");
}
}
};
function init(){
var _b=null;
_b=svgDocument.createElementNS(SVG_NS,"text");
_b.setAttribute("class","box-text");
_b.appendChild(svgDocument.createTextNode(ELLIPSIS));
ellipsisLength=getTextLength(_b);
workflowLinkInfo="processID="+getModel().firstChild.getAttribute("ID");
workflowLinkInfo+="&processVersionNo="+getModel().firstChild.getAttribute("PROCESS-VERSION");
if(window.parent.id1.length>0){
showInstanceData=true;
}
splitsAndJoins=svgDocument.createElementNS(SVG_NS,"g");
};
function getModel(){
if(wf_model==null){
var _c=window.parent.getWorkflowData();
if(window.DOMParser){
parser=new DOMParser();
wf_model=parser.parseFromString(_c,"text/xml");
}else{
wf_model=parseXML(_c,null);
}
}
return wf_model;
};
function drawNode(_d){
var _e=null;
var _f=null;
var _10=null;
var _11=null;
var _12=null;
var _13=null;
var key=null;
var _14=null;
var _15=null;
var _16=false;
var _17=false;
var x=null;
var y=null;
var _18=false;
var _19=null;
key=_d.getAttribute("ID");
nodes[key]=_d;
x=parseFloat(_d.getAttribute("X"));
y=parseFloat(_d.getAttribute("Y"));
if(x>max_X){
max_X=x;
}
if(y>max_Y){
max_Y=y;
}
_17=_d.getAttribute("HIGHLIGHT")=="true";
if(_d.getAttribute("HIDDEN")=="false"){
x=x*GRID_WIDTH;
y=y*GRID_HEIGHT;
_14=_d.getAttribute("ACTIVITY-TYPE-CODE");
_16=_d.getAttribute("IS-EXECUTED")=="true";
if(_14==parent.startType||_14==parent.endType){
_18=true;
}
if(!_18&&window.parent.readonlyView=="false"){
_19=getLink(key,_14);
_11=svgDocument.createElementNS(SVG_NS,"a");
_11.setAttributeNS(XLINK_NS,"xlink:href",_19);
_11.addEventListener("click",getClickListener(_19),false);
_11.setAttribute("target","_blank");
}else{
_11=svgDocument.createElementNS(SVG_NS,"g");
}
_12=_d.getAttribute("TEXT");
if(!_18){
_e=svgDocument.createElementNS(SVG_NS,"use");
_e.setAttribute("x",x);
_e.setAttribute("y",y);
nodesToBeMirrored.push(_e);
if(_17){
_e.setAttributeNS(XLINK_NS,"xlink:href","#current-activity");
}else{
if(_16){
_e.setAttributeNS(XLINK_NS,"xlink:href","#executed-activity");
}else{
_e.setAttributeNS(XLINK_NS,"xlink:href","#activity");
}
}
_11.appendChild(_e);
}
_f=svgDocument.createElementNS(SVG_NS,"text");
_f.setAttributeNS(CURAM_NS,"curam:popup-text-content",_12);
_f.setAttribute("x",x+TEXT_BREADTH);
_f.setAttribute("y",y+TEXT_DEPTH);
textNodes.push(_f);
_f.setAttribute("font-family",FONT_FAMILY);
_f.setAttribute("class","box-text");
if(_14.length>0){
_15=_d.getAttribute("ACTIVITY-TYPE-TEXT");
if(_15==null){
_15="";
}
if(svgDocument.rootElement.getElementById("_i"+_14)){
if(_14==parent.startType){
_13=createUse(x+55,y+17,"#_i"+_14);
}else{
if(_14==parent.endType){
_13=createUse(x+7,y+17,"#_i"+_14);
}else{
_13=createUse(x+29,y+8,"#_i"+_14);
}
}
nodesToBeMirrored.push(_13);
_13.setAttributeNS(CURAM_NS,"curam:popup-text-content",_15);
_13.addEventListener("mouseover",displayPopUp,false);
_13.addEventListener("mouseout",removePopUp,false);
_11.appendChild(_13);
}else{
_10=_f.cloneNode(true);
_10.setAttribute("x",x+ACT_TYPE_BREADTH);
_10.setAttribute("y",y+ACT_TYPE_DEPTH);
nodesToBeMirrored.push(_10);
_10.appendChild(svgDocument.createTextNode(_15));
truncateText(_10,_15,ACTIVITY_TEXT_WIDTH);
_11.appendChild(_10);
}
}
if(!_18){
_f.appendChild(svgDocument.createTextNode(_12));
truncateText(_f,_12,ACTIVITY_TEXT_WIDTH);
_f.setAttribute("id",key);
_f.addEventListener("mouseover",displayPopUp,false);
_f.addEventListener("mouseout",removePopUp,false);
_11.appendChild(_f);
}
if(showInstanceData&&_16){
_11.setAttributeNS(CURAM_NS,"curam:id",key);
_11.addEventListener("mouseover",displayInstanceData,false);
if(_17){
displayInitialInstanceData(_11);
}
}
svgDocument.rootElement.appendChild(_11);
if(_d.getAttribute("SPLIT-TYPE")=="XOR"){
addXOR(x,y,true);
}else{
if(_d.getAttribute("SPLIT-TYPE")=="AND"){
addAND(x,y,true);
}
}
if(_d.getAttribute("JOIN-TYPE")=="XOR"){
addXOR(x,y,false);
}else{
if(_d.getAttribute("JOIN-TYPE")=="AND"){
addAND(x,y,false);
}
}
if(_d.getAttribute("HAS-NOTIFICATION")=="true"){
drawNotification(x,y,key,_14);
}
}
};
function drawNotification(x,y,key,_1a){
var _1b=null;
var _1c=null;
var _1d=null;
if(window.parent.readonlyView=="false"){
_1b=svgDocument.createElementNS(SVG_NS,"a");
_1c=getCurrentPageRoot()+window.parent.notificationPage+"&"+window.workflowLinkInfo+"&activityID="+key+"&activityType="+_1a;
_1b.setAttributeNS(XLINK_NS,"xlink:href",_1c);
_1b.addEventListener("click",getClickListener(_1c),false);
_1b.setAttribute("target","_blank");
}else{
_1b=svgDocument.createElementNS(SVG_NS,"g");
}
_1d=createUse(x+64,y+6,"#notify-icon");
_1b.appendChild(_1d);
svgDocument.rootElement.appendChild(_1b);
};
function displayInitialInstanceData(_1e){
var id=_1e.getAttributeNS(CURAM_NS,"id");
replaceText("o3wf_id_1",parent.id1[id]);
replaceText("o3wf_id_2",parent.id2[id]);
replaceText("o3wf_id_3",parent.id3[id]);
replaceText("o3wf_id_4",parent.id4[id]);
replaceText("o3wf_id_5",parent.id5[id]);
replaceText("o3wf_id_6",parent.id6[id]);
replaceText("o3wf_id_7",parent.id7[id]);
replaceText("o3wf_id_8",parent.id8[id]);
replaceText("o3wf_id_9",parent.id9[id]);
};
function displayInstanceData(evt){
var id=evt.currentTarget.getAttributeNS(CURAM_NS,"id");
replaceText("o3wf_id_1",parent.id1[id]);
replaceText("o3wf_id_2",parent.id2[id]);
replaceText("o3wf_id_3",parent.id3[id]);
replaceText("o3wf_id_4",parent.id4[id]);
replaceText("o3wf_id_5",parent.id5[id]);
replaceText("o3wf_id_6",parent.id6[id]);
replaceText("o3wf_id_7",parent.id7[id]);
replaceText("o3wf_id_8",parent.id8[id]);
replaceText("o3wf_id_9",parent.id9[id]);
};
function replaceText(_1f,_20){
var _21=parent.document.getElementById(_1f);
while(_21.hasChildNodes()){
_21.removeChild(_21.lastChild);
}
_21.appendChild(parent.document.createTextNode(_20));
};
function getLink(key,_22){
var _23=null;
_23=parent.linkLocation.substring(0,parent.linkLocation.indexOf("?")+1);
_23+=workflowLinkInfo;
if(_22==null){
if(key==0){
return null;
}
_23+="&componentType=Transition";
_23+="&componentID="+key;
}else{
_23+="&componentType=Activity";
_23+="&componentID="+key;
_23+="&activityType="+_22;
}
return _23;
};
function addXOR(x,y,_24){
var XOR=null;
XOR=svgDocument.createElementNS(SVG_NS,"use");
XOR.setAttributeNS(XLINK_NS,"xlink:href","#xor");
if(_24){
XOR.setAttribute("x",x+ACTIVITY_WIDTH-6);
}else{
XOR.setAttribute("x",x+6);
}
nodesToBeMirrored.push(XOR);
XOR.setAttribute("y",y+XOR_DEPTH);
splitsAndJoins.appendChild(XOR);
};
function addAND(x,y,_25){
var AND=null;
AND=svgDocument.createElementNS(SVG_NS,"use");
AND.setAttributeNS(XLINK_NS,"xlink:href","#and");
if(_25){
AND.setAttribute("x",x+ACTIVITY_WIDTH-6);
}else{
AND.setAttribute("x",x+6);
}
nodesToBeMirrored.push(AND);
AND.setAttribute("y",y+XOR_DEPTH);
splitsAndJoins.appendChild(AND);
};
function drawEdge(_26,_27,_28){
var _29=null;
var _2a=null;
var _2b=null;
var _2c=null;
var _2d=false;
var _2e=false;
var _2f=null;
var _30=null;
var _31=null;
var _32=null;
var _33=null;
var _34=null;
var _35=null;
var x1=null;
var y1=null;
var x2=null;
var y2=null;
var xi=null;
var yi=null;
var _36="";
var id="_e";
var _37=null;
if(_26.getAttribute("HIDDEN")=="false"){
_2e=_26.getAttribute("IS-EXECUTED")=="true";
_2f=_26.getAttribute("TRANSITION-ID");
_2a=nodes[_26.getAttribute("FROM")];
_2b=nodes[_26.getAttribute("TO")];
if(_26.getAttribute("ENABLED")!=="false"&&window.parent.readonlyView=="false"){
_37=getLink(_2f);
_29=svgDocument.createElementNS(SVG_NS,"a");
_29.setAttributeNS(XLINK_NS,"xlink:href",_37);
_29.addEventListener("click",getClickListener(_37),false);
_29.setAttribute("target","_blank");
_30=_29;
}else{
_30=svgDocument.createElementNS(SVG_NS,"g");
}
_2c=svgDocument.createElementNS(SVG_NS,"path");
id+=_27;
_2c.setAttribute("id",id);
id="#"+id;
svgDocument.rootElement.getElementsByTagName("defs").item(0).appendChild(_2c);
_2d=_26.getAttribute("REVERSE-ARROW")=="true";
x1=parseFloat(_2a.getAttribute("X"))*GRID_WIDTH+ACTIVITY_WIDTH;
y1=parseFloat(_2a.getAttribute("Y"))*GRID_HEIGHT+ACT_HEIGHT_HALF;
x2=parseFloat(_2b.getAttribute("X"))*GRID_WIDTH;
y2=parseFloat(_2b.getAttribute("Y"))*GRID_HEIGHT+ACT_HEIGHT_HALF;
if(_2d){
y1+=10;
y2+=10;
}
if(showInstanceData){
if(_2e){
_2c.setAttribute("class","transition");
}else{
_2c.setAttribute("class","executed-transition");
}
}else{
_2c.setAttribute("class","transition");
}
_36+="M "+x1+","+y1;
_31=_28["id_"+_2f];
if(_31!=null){
for(var i=0;i<_31.length;i++){
_32=nodes[_31[i]];
xi=parseFloat(_32.getAttribute("X"))*GRID_WIDTH+40;
yi=parseFloat(_32.getAttribute("Y"))*GRID_HEIGHT+45;
_33=getAdjustedMidPoint(x1,y1,xi,yi);
_36+=" T "+(_33.x-2)+","+_33.y+" T "+xi+","+yi;
x1=xi;
y1=yi;
if(i+1==_31.length){
var _33=getAdjustedMidPoint(x1,y1,x2,y2);
_36+=" T "+(_33.x-2)+","+_33.y+" T "+x2+","+y2;
}
}
}else{
var _33=getAdjustedMidPoint(x1,y1,x2,y2);
_36+=" T "+(_33.x-10)+","+_33.y+" T "+x2+","+y2;
}
_2c.setAttribute("d",_36);
if(!_2d){
_30.appendChild(drawTextArrow(id,_2c.getTotalLength(),0));
}else{
if(_2d&&_2a.getAttribute("HIDDEN")=="false"){
_30.appendChild(drawTextArrow(id,0,1));
}
}
_34=_26.getAttribute("TRANSITION-CONDITION");
if(_34!==""&&_34!=null){
var _38=drawTextArrow(id,_2c.getTotalLength()/2,2);
_38.setAttributeNS(CURAM_NS,"curam:popup-text-content",_34);
_38.addEventListener("mouseover",displayPopUp,false);
_38.addEventListener("mouseout",removePopUp,false);
_30.appendChild(_38);
}
_35=_26.getAttribute("ORDER");
if(_35!==""&&_35!=null){
var _39=drawTextArrow(id,_2c.getTotalLength()-20,3,_35);
_39.setAttributeNS(CURAM_NS,"curam:popup-text-content",window.parent.orderText+" "+_35);
_39.addEventListener("mouseover",displayPopUp,false);
_39.addEventListener("mouseout",removePopUp,false);
_30.appendChild(_39);
}
_30.appendChild(createUse(null,null,id));
svgDocument.rootElement.appendChild(_30);
nodesToBeMirrored.push(_30);
}
};
function drawTextArrow(id,_3a,ch,_3b){
var _3c;
var _3d;
_3c=svgDocument.createElementNS(SVG_NS,"textPath");
_3c.setAttributeNS(XLINK_NS,"xlink:href",id);
if(ch==0){
_3d=createText(null,null,null,"arrow-text");
_3d.setAttribute("dy",6.5);
_3d.setAttribute("dx",_3a-8);
_3c.appendChild(svgDocument.createTextNode(">"));
}else{
if(ch==1){
_3d=createText(null,null,null,"arrow-text");
_3d.setAttribute("dy",6);
_3c.appendChild(svgDocument.createTextNode("<"));
}else{
if(ch==2){
_3d=createText(null,null,null,"transition-text");
_3d.setAttribute("dy",7);
_3d.setAttribute("dx",_3a-4);
_3c.appendChild(svgDocument.createTextNode("*"));
}else{
if(ch==3){
_3d=createText(null,null,null,"box-text");
_3d.setAttribute("dy",-2);
_3d.setAttribute("dx",_3a-4);
_3c.appendChild(svgDocument.createTextNode(_3b));
}
}
}
}
_3d.appendChild(_3c);
return _3d;
};
function setWidthAndHeight(){
var _3e=null;
_3e=(max_Y+2)*GRID_HEIGHT;
parent.document.getElementById("__o3__SVGWorkflow").height=_3e;
parent.document.getElementById("__o3__SVGWorkflow").width=(max_X+1.5)*GRID_WIDTH;
};
function drawGrid(){
var _3f=null;
var _40=null;
var _41=null;
var _42=(max_X+1)*GRID_WIDTH;
var _43=(max_Y+1)*GRID_HEIGHT;
for(var y=0;y<=max_Y+1;y++){
_3f=svgDocument.createElementNS(SVG_NS,"line");
_3f.setAttribute("style","fill:none;stroke:gray;stroke-dasharray: 2,2;");
_40=y*GRID_HEIGHT;
_3f.setAttribute("y1",_40);
_3f.setAttribute("x1",0);
_3f.setAttribute("y2",_40);
_3f.setAttribute("x2",_42);
svgDocument.rootElement.appendChild(_3f);
}
for(var x=0;x<=max_X+1;x++){
_3f=svgDocument.createElementNS(SVG_NS,"line");
_3f.setAttribute("style","fill:none;stroke:gray;stroke-dasharray: 2,2;");
_41=x*GRID_WIDTH;
_3f.setAttribute("y1",0);
_3f.setAttribute("x1",_41);
_3f.setAttribute("y2",_43);
_3f.setAttribute("x2",_41);
svgDocument.rootElement.appendChild(_3f);
}
};
function getClickListener(_44){
return function(evt){
try{
if(!evt){
evt=parent.dojo.fixEvent(evt);
}
evt.preventDefault();
}
catch(e){
}
parent.dojo.stopEvent(evt);
var _45=window.parent.curam.util.getTopmostWindow();
var _46=new _45.curam.ui.PageRequest(_44);
_45.curam.ui.UIController.handlePageRequest(_46);
return false;
};
};

