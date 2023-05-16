var SVG_NS="http://www.w3.org/2000/svg";
var XLINK_NS="http://www.w3.org/1999/xlink";
var CURAM_NS="http://www.curamsoftware.com";
var ELLIPSIS="...";
var FONT_FAMILY="Lato, WT Sans Duo SC,Arial Unicode MS,Batang,SimSum,PMingLiU,MS Mincho,sans-serif";
var debugRoot=null;
var popupGroup=null;
var currentPageRoot=null;
var ellipsisLength=0;
var nativeSVG=null;
function getTextLength(_1){
try{
var _2=_1.getComputedTextLength();
if(_2==0){
return fallbackEval(svgDocument,_1);
}
return _2;
}
catch(e){
if(e.name!="RangeError"){
if(isNative()){
return fallbackEval(svgDocument,_1);
}else{
alert("SVG Error: "+e.name);
}
}
}
};
function getCurrentPageRoot(){
var _3=null;
if(currentPageRoot==null){
_3=svgDocument.URL;
currentPageRoot=_3.substring(0,_3.lastIndexOf("/CDEJ/svg"));
currentPageRoot+="/"+window.parent.jsL+"/";
}
return currentPageRoot;
};
function createLine(x1,x2,y1,y2,_4){
var _5=null;
_5=svgDocument.createElementNS(SVG_NS,"line");
if(x1!==null){
_5.setAttribute("x1",x1);
}
if(x2!==null){
_5.setAttribute("x2",x2);
}
if(y1!==null){
_5.setAttribute("y1",y1);
}
if(y2!==null){
_5.setAttribute("y2",y2);
}
_5.setAttribute("class",_4);
return _5;
};
function createRect(x,y,_6,_7,_8){
var _9=null;
_9=svgDocument.createElementNS(SVG_NS,"rect");
if(x!==null){
_9.setAttribute("x",x);
}
if(y!==null){
_9.setAttribute("y",y);
}
_9.setAttribute("width",_6);
_9.setAttribute("height",_7);
if(_8!==null){
_9.setAttribute("class",_8);
}
return _9;
};
function createUse(x,y,_a){
var _b=null;
_b=svgDocument.createElementNS(SVG_NS,"use");
if(x!==null){
_b.setAttribute("x",x);
}
if(y!==null){
_b.setAttribute("y",y);
}
if(_a!==null){
_b.setAttributeNS(XLINK_NS,"xlink:href",_a);
}
return _b;
};
function createText(x,y,_c,_d,_e){
var _f=null;
_f=svgDocument.createElementNS(SVG_NS,"text");
if(x!==null){
_f.setAttribute("x",x);
}
if(y!==null){
_f.setAttribute("y",y);
}
if(_c!==null){
_f.appendChild(svgDocument.createTextNode(_c));
}
if(_d!==null){
_f.setAttribute("class",_d);
}
if(_e!==null){
_f.setAttribute("text-anchor",_e);
}
_f.setAttribute("font-family",FONT_FAMILY);
return _f;
};
function getAdjustedMidPoint(x1,y1,x2,y2){
var _10=new Object();
_10.x=(x1+x2)/2;
_10.y=(y1+y2)/2;
return _10;
};
function truncateText(_11,_12,_13){
var _14=null;
var _15=_13;
var _16=true;
_14=getTextLength(_11);
while(_14>_15){
if(_16){
_15-=ellipsisLength;
}
_12=_12.substring(0,_12.length-1);
_11.removeChild(_11.firstChild);
_11.appendChild(svgDocument.createTextNode(_12));
_16=false;
_14=getTextLength(_11);
}
if(!_16){
_12+=ELLIPSIS;
_11.removeChild(_11.firstChild);
_11.appendChild(svgDocument.createTextNode(_12));
}
};
function displayPopUp(evt){
var _17=null;
var _18=null;
var _19=null;
var _1a=0;
var _1b=null;
var ct=svgDocument.rootElement.currentTranslate;
var x;
var y;
if(popupGroup==null){
_1b=evt.currentTarget.getAttributeNS(CURAM_NS,"popup-text-content");
if(_1b!=null&&_1b.length>0){
popupGroup=svgDocument.createElementNS(SVG_NS,"use");
popupGroup.setAttributeNS(XLINK_NS,"xlink:href","#popup");
_18=svgDocument.getElementById("popup-text");
_18.removeChild(_18.firstChild);
_18.appendChild(svgDocument.createTextNode(_1b));
_18.setAttribute("font-family",FONT_FAMILY);
_1a=getTextLength(_18);
x=evt.clientX-ct.x+5;
if(x+_1a>innerWidth){
x-=_1a;
}
popupGroup.setAttribute("x",x);
y=evt.clientY-ct.y+10;
if(y+24>innerHeight){
y-=36;
}
popupGroup.setAttribute("y",y);
if(_1a>0){
_19=svgDocument.getElementById("popup-rect");
_19.setAttribute("width",_1a+12);
_17=svgDocument.getElementById("popup-rect-anim");
_17.beginElement();
_17=svgDocument.getElementById("popup-text-anim");
_17.beginElement();
svgDocument.documentElement.appendChild(popupGroup);
}
}
}
};
function removePopUp(evt){
if(popupGroup!=null){
evt.target.ownerDocument.documentElement.removeChild(popupGroup);
popupGroup=null;
}
};
function getDebug(){
if(debugRoot==null){
debugRoot=svgDocument.createElementNS(SVG_NS,"g");
}
if(debugRoot.childNodes.length>100){
alert("Error: Remove Debug Statetments.");
}
return debugRoot;
};
function addDebug(_1c){
var _1d=svgDocument.createElementNS(SVG_NS,"text");
_1d.appendChild(svgDocument.createTextNode(_1c));
getDebug().appendChild(_1d);
};
function printDebug(){
alert(printNode(getDebug()));
};
function clearDebug(){
debugRoot=null;
};
function fallbackEval(_1e,_1f){
_1e.rootElement.appendChild(_1f);
var _20=_1f.getComputedTextLength();
_1e.rootElement.removeChild(_1f);
return _20;
};
function isNative(){
if(!nativeSVG){
try{
nativeSVG=!!document.createElementNS&&!!document.createElementNS("http://www.w3.org/2000/svg","svg").createSVGRect;
}
catch(e){
nativeSVG=false;
}
}
return nativeSVG;
};

