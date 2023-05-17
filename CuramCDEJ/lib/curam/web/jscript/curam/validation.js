//>>built
define("curam/validation",["dojo/dom","curam/define","curam/date"],function(_1){
curam.define.singleton("curam.validation",{FILE_UPLOAD_FLGS:[],fileUploadChecker:null,invalidPathMsg:null,preventKeyPress:function(_2){
if(dojo.isIE){
_2.cancelBubble=true;
_2.returnValue=false;
return false;
}
return true;
},activateFileUploadChecker:function(_3){
if(!curam.validation.fileUploadChecker){
curam.validation.fileUploadChecker=function(){
var _4=_1.byId("mainForm");
var _5=function(_6){
var _7=curam.validation.FILE_UPLOAD_FLGS;
for(var i=0;i<_7.length;i++){
var _8=_7[i];
var _9=cm.nextSibling(_1.byId(_8),"input");
if(!curam.validation.isValidFilePath(_9.value)){
dojo.stopEvent(_6);
alert(curam.validation.invalidPathMsg+" '"+_9.value+"'");
cm.setFormSubmitted(_4,0);
return false;
}
}
return true;
};
dojo.connect(_4,"onsubmit",_5);
};
dojo.addOnLoad(curam.validation.fileUploadChecker);
}
},isValidFilePath:function(_a){
return true;
},validateDate:function(_b){
var _c={valid:curam.date.isDate(_b,jsDF),validFormat:jsDF.toLowerCase()};
return _c;
}});
return curam.validation;
});
