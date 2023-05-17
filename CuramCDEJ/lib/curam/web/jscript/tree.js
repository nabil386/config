var COLLAPSE_TITLE;
var COLLAPSED_STATE;
var COLLAPSE_ICON;
var EXPAND_TITLE;
var EXPANDED_STATE;
var EXPAND_ICON;
function toggleSubtree(_1,_2){
var _3=firstChildElement(_1);
var _4=nextSiblingElement(_3);
var _5=firstChildElement(_3);
var _6=lastChildElement(_3);
if(_2){
_4.style.display="block";
_5.src=COLLAPSE_ICON;
_6.alt=EXPANDED_STATE;
_6.title=EXPANDED_STATE;
_5.alt=COLLAPSE_TITLE;
_5.title=COLLAPSE_TITLE;
}else{
_4.style.display="none";
_5.src=EXPAND_ICON;
_6.alt=COLLAPSED_STATE;
_6.title=COLLAPSED_STATE;
_5.alt=EXPAND_TITLE;
_5.title=EXPAND_TITLE;
}
_5.onclick=function(){
toggleSubtree(_1,!_2);
};
};
function nextSiblingElement(_7){
var _8=_7.nextSibling;
if(_8&&_8.nodeType!=1){
return nextSiblingElement(_8);
}
return _8;
};
function prevSiblingElement(_9){
var _a=_9.previousSibling;
if(_a&&_a.nodeType!=1){
return prevSiblingElement(_a);
}
return _a;
};
function firstChildElement(_b){
var _c=_b.firstChild;
if(_c&&_c.nodeType!=1){
return nextSiblingElement(_c);
}
return _c;
};
function lastChildElement(_d){
var _e=_d.lastChild;
if(_e&&_e.nodeType!=1){
return prevSiblingElement(_e);
}
return _e;
};
function addToggleKeyHandler(_f){
var _10=firstChildElement(_f);
lastChildElement(_10).onkeypress=function(e){
var _11;
var _12;
if(window.event){
_11=window.event.keyCode;
}else{
if(e.which){
_11=e.which;
}
}
_12=String.fromCharCode(_11);
if(_12=="+"){
toggleSubtree(_f,true);
}else{
if(_12=="-"){
toggleSubtree(_f,false);
}
}
};
};
function collapseTree(_13,_14,_15,_16,_17,_18){
COLLAPSE_TITLE=_13;
COLLAPSED_STATE=_14;
COLLAPSE_ICON=_15;
EXPAND_TITLE=_16;
EXPANDED_STATE=_17;
EXPAND_ICON=_18;
var _19=document.getElementsByTagName("li");
for(var i=0;i<_19.length;i++){
if(_19[i].className=="subtree"){
toggleSubtree(_19[i],false);
addToggleKeyHandler(_19[i]);
}
}
};
function expandTreeToShowLeaf(_1a){
if(_1a&&_1a.parentNode){
var _1b=_1a.parentNode.parentNode;
if(_1b&&_1b.className=="subtree"){
toggleSubtree(_1b,true);
expandTreeToShowLeaf(_1b);
}else{
expandTreeToShowLeaf(_1a.parentNode);
}
}
};

