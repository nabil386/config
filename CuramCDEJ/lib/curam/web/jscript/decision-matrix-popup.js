define(["dojo/dom","curam/util","curam/dialog"],function(_1){
var _2=false;
function _3(){
if(_2){
return;
}
_2=true;
var _4=curam.dialog.getParentWindow(window);
var _5=new curam.ListMap();
var _6=_1.byId("messageTable").childNodes;
var _7=null;
for(var i=0;i<_6.length;i++){
if(_6[i].nodeType==1){
_7=dojo.query("> :first-child",_6[i])[0];
_5.add(_7.innerHTML,dojo.query("> :first-child",cm.nextSibling(_7))[0].value);
}
}
_4.curam.matrix.Constants.container.matrix.addMessagesFromPopup(_5,_1.byId("combinationId").value);
curam.dialog.closeModalDialog();
};
function _8(){
var _9=curam.dialog.getParentWindow(window);
var _a=_1.byId("combinationId").value;
var _b;
var _c;
dojo.query("#messageTable input").forEach(function(_d){
_b=_d.getAttribute("id");
_c=_9.curam.matrix.Constants.container.matrix.getContradictionMsg(_a,_b);
if(_c){
_d.value=_c;
}
});
for(var i=0;i<document.forms.length;i++){
dojo.connect(document.forms[i],"onsubmit",_3);
}
};
dojo.addOnLoad(_8);
});

