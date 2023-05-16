function TextWrap(x,y,_1,_2,_3){
this._ownerDoc=_2;
this._node=_1;
this._string=_1.text;
this._x=x;
this._y=y;
this._id=_1.id;
this._width=_3;
this._svg=null;
this._lines=null;
this._align="left";
this._height=0;
this._construct();
};
TextWrap.prototype._construct=function(){
this._build();
this._splitString();
this._layout();
};
TextWrap.prototype._build=function(){
this._svg=this._ownerDoc.createElementNS(SVG_NS,"text");
this._svg.setAttribute("x",this._x);
this._svg.setAttribute("y",this._y);
this._svg.setAttribute("font-family",FONT_FAMILY);
this._svg.appendChild(this._ownerDoc.createTextNode(""));
};
TextWrap.prototype._splitString=function(){
this._hide();
this._clear();
var _4=null;
var _5=null;
var _6=new Array();
var _7=0;
var _8=0;
_5=this._node.lines;
if(_5==null){
_5=new Array();
_4=this._string.split(" ");
while(_4.length){
var _9=_4[0];
this._svg.firstChild.data=_6.join(" ")+" "+_9;
try{
_7=this._svg.getComputedTextLength();
}
catch(e){
if(e.name!="RangeError"){
if(isNative()){
_7=fallbackEval(this._ownerDoc,this._svg);
}else{
alert("SVG Error: "+e.name);
}
}
}
if(_7>this._width){
if(!_4.length){
_6.push(_4[0]);
}else{
if(_6.length==0){
_6.push(_4.shift());
}
}
_5.push(new Line(_8,_6));
_6=new Array();
}else{
_6.push(_4.shift());
}
_8=_7;
if(_4.length==0){
_5.push(new Line(0,_6));
}
}
this._node.lines=_5;
this._node.text=null;
}
this._lines=_5;
};
TextWrap.prototype._layout=function(){
this._clear();
var _a=(new Array(0)).concat(this._lines);
var _b="start";
if(this._align=="center"){
_b="middle";
}else{
if(this._align=="right"){
_b="end";
}
}
for(var i=0;i<_a.length;i++){
var x=0;
line=_a[i];
this._svg.appendChild(this._ownerDoc.createTextNode(" "));
var _c=this._ownerDoc.createElementNS(SVG_NS,"tspan");
_c.appendChild(this._ownerDoc.createTextNode(line._words.join(" ")));
if(this._align=="justify"){
var _d=(this._width-line._width)/(line._words.length-1);
_d=(i!=_a.length-1)?_d:0;
_c.style.setProperty("word-spacing",_d+"px");
}else{
if(this._align=="center"){
_b="middle";
x=this._width/2;
}else{
if(this._align=="right"){
_b="end";
x=this._width;
}
}
}
_c.setAttribute("x",this._x);
if(i==0){
_c.setAttribute("dy",0);
}else{
_c.setAttribute("dy",Y_HEIGHT);
}
this._svg.appendChild(_c);
this._height+=Y_HEIGHT;
}
this._svg.style.setProperty("text-anchor",_b);
this._show();
};
TextWrap.prototype._clear=function(){
while(this._svg.hasChildNodes()){
this._svg.removeChild(this._svg.firstChild);
}
this._svg.appendChild(this._ownerDoc.createTextNode(""));
};
TextWrap.prototype._hide=function(){
this._svg.style.setProperty("opacity","0");
};
TextWrap.prototype._show=function(){
this._svg.style.setProperty("opacity","1");
};
TextWrap.prototype.getTextHeight=function(){
return this._height;
};
TextWrap.prototype.getTextNode=function(){
return this._svg;
};
function Line(_e,_f){
this._width=_e;
this._words=_f;
};

