define(["dojo/dom","curam/date"],function(_1){
var _2=jsDF;
var _3=jsTF;
var _4=jsTS;
var _5=jsDTFs;
var _6=jsDFs;
function _7(_8,_9){
this.value=_8;
this.msg=_9;
};
function _a(_b){
return new _7(null,null);
};
function _c(_d){
return null;
};
function _e(_f){
return _f;
};
function _10(_11){
return new _7(null,null);
};
function _12(_13){
return null;
};
function _14(_15){
return _15;
};
function _16(_17){
return new _7(null,null);
};
function _18(_19){
return null;
};
function _1a(_1b){
return _1b;
};
function _1c(_1d){
var _1e=new _7(null,null);
if(_1d&&_1d!=""){
if(_1d.length>this.size){
_1e.msg=_1f(SVR_STRING_MSG,new Array(_1d,""+_1d.length,""+this.size));
}else{
_1e.value=_1d;
}
}
return _1e;
};
function _20(_21){
return _21.value;
};
function _22(_23){
return _23;
};
function _24(_25){
var _26=new _7(null,null);
if(_25&&_25!=""){
if(_25.length!=1){
_26.msg=_1f(SVR_CHAR_MSG,new Array(_25));
}else{
_26.value=_25;
}
}
return _26;
};
function _27(_28){
return _28.value;
};
function _29(_2a){
return _2a;
};
function _2b(_2c){
var _2d=new _7(null,null);
if(value&&value!=""){
value=value.toLowerCase();
if(value!="true"&&value!="false"){
_2d.msg=_1f(SVR_BOOLEAN_MSG,new Array(value));
}else{
var _2e=value=="true"?true:false;
_2d.value=_2e;
}
}
return _2d;
};
function _2f(_30){
return _30.value;
};
function _31(_32){
return _32;
};
function _33(_34){
return new _7(null,null);
};
function _35(_36){
return _36.value;
};
function _37(_38){
return _38;
};
function _39(_3a){
var _3b=2147483647;
var _3c=-2147483648;
var _3d=new _7(null,null);
if(_3a&&_3a!=""){
var _3e=_3a-0;
if(isNaN(_3e)||_3e<_3c||_3e>_3b){
_3d.msg=_1f(SVR_INT32_MSG,new Array(_3a));
}else{
_3d.value=_3e;
}
}
return _3d;
};
function _3f(_40){
return _40.value;
};
function _41(_42){
return _42;
};
function _43(_44){
var _45=32767;
var _46=-32768;
var _47=new _7(null,null);
if(_44&&_44!=""){
var _48=_44-0;
if(isNaN(_48)||_48<_46||_48>_45){
_47.msg=_1f(SVR_INT16_MSG,new Array(_44));
}else{
_47.value=_48;
}
}
return _47;
};
function _49(_4a){
return _4a.value;
};
function _4b(_4c){
return _4c;
};
function _4d(_4e){
var _4f=127;
var _50=-128;
var _51=new _7(null,null);
if(_4e&&_4e!=""){
var _52=_4e-0;
if(isNaN(_52)||_52<_50||_52>_4f){
_51.msg=_1f(SVR_INT8_MSG,new Array(_4e));
}else{
_51.value=_52;
}
}
return _51;
};
function _53(_54){
return _54.value;
};
function _55(_56){
return _56;
};
function _57(_58){
var _59=new _7(null,null);
if(_58&&_58!=""){
var _5a=_58-0;
if(isNaN(_5a)){
_59.msg=_1f(SVR_FLOAT_MSG,new Array(_58));
}else{
_59.value=_5a;
}
}
return _59;
};
function _5b(_5c){
return _5c.value;
};
function _5d(_5e){
return _5e;
};
function _5f(_60){
var _61=new _7(null,null);
if(_60&&_60!=""){
var _62=_60-0;
if(isNaN(_62)){
_61.msg=_1f(SVR_DOUBLE_MSG,new Array(_60));
}else{
_61.value=_62;
}
}
return _61;
};
function _63(_64){
return _64.value;
};
function _65(_66){
return _66;
};
function _67(_68){
var _69=new _7(null,null);
if(_68&&_68!=""){
var _6a=curam.date.getDateFromFormat(_68,_2);
if(isNaN(_6a)||_6a==0){
_69.msg=_1f(SVR_DATE_MSG,new Array(_68,_6));
}else{
_69.value=_6a;
}
}
return _69;
};
function _6b(_6c){
return _6c.value;
};
function _6d(_6e){
return curam.date.formatDate(new Date(_6e),_2);
};
function _6f(_70){
var _71=new _7(null,null);
if(_70&&_70!=""){
var _72=curam.date.getDateFromFormat(_70,_3);
if(isNaN(_72)||_72==0){
_71.msg=_1f(SVR_DATETIME_MSG,new Array(_70,_5));
}else{
_71.value=_72;
}
}
return _71;
};
function _73(_74){
var _75=_74.name;
_75=_74.name.substring(0,_75.lastIndexOf("."));
var _76=_74.value;
var _77=_1.byId(_75+".HOUR");
var _78=_1.byId(_75+".MIN");
var _79=_77.options[_77.selectedIndex].text;
var min=_78.options[_78.selectedIndex].text;
if(_79!=""&&min!=""){
return value=_76+" "+_79+_4+min;
}else{
return "";
}
};
function _7a(_7b){
return curam.date.formatDate(new Date(_7b),_3);
};
function v(_7c,_7d){
if(_7c==null||_7c==""){
return "";
}
var _7e=_7f[_7c];
var msg=_7e.doPreConversionValidation(_7e.getValueFromInput(_7d));
if(msg!=null){
return msg;
}
var _80=_7e.doConversion(_7e.getValueFromInput(_7d));
if(_80.msg!=null){
return _80.msg;
}
msg=_7e.doPostConversionValidation(_80.value);
if(msg!=null){
return msg;
}
if(_7e.domainValidation&&_7e.domainValidation.custom){
if(window[_7e.domainValidation.custom]==null){
alert("The custom validation function '"+_7e.domainValidation.custom+"' associated with the domain '"+_7e.typeName+" could not be found");
}else{
msg=window[_7e.domainValidation.custom](_7d);
}
}
if(msg!=null){
return _1f(CUSTOM_VALIDATION_MSG,new Array(_7d.value,msg));
}
return "";
};
function pv(_81,_82,_83){
var _84=_7f[_81];
var _85=_84.doPreValidationFormatting(_82.value);
if(_85!=null){
_82.value=_85;
}
};
function _86(_87){
_87=this.doPreValidationFormatting(_87);
if(this.domainValidation&&this.domainValidation.minchar){
if(_87.length<this.domainValidation.minchar){
return _1f(MIN_LENGTH_MSG,new Array(this.domainValidation.minchar,_87.length));
}
}
return null;
};
function _88(_89){
if(this.domainValidation&&this.domainValidation.minimum){
if(_89<this.domainValidation.minimum){
return _1f(MIN_VALUE_MSG,new Array(this.doFormatting(_89),this.doFormatting(this.domainValidation.minimum)));
}
}
if(this.domainValidation&&this.domainValidation.maximum){
if(_89>this.domainValidation.maximum){
return _1f(MAX_VALUE_MSG,new Array(this.doFormatting(_89),this.doFormatting(this.domainValidation.maximum)));
}
}
return null;
};
function _8a(_8b){
if(this.domainValidation!=null){
return this.domainValidation.doPreValidationFormatting(_8b);
}else{
return null;
}
};
function _8c(_8d,_8e,_8f,_90,_91,_92,_93,_94){
this.doPreValidationFormatting=_8a;
this.doPreConversionValidation=_86;
this.doPostConversionValidation=_88;
this.toString=_95;
this.typeName=_8d;
this.definedAs=_8e;
this.codeTableName=_91;
var _96=null;
if(this.definedAs&&this.definedAs!=""){
_96=_7f[this.definedAs];
this.doConversion=_96.doConversion;
this.getValueFromInput=_96.getValueFromInput;
this.doFormatting=_96.doFormatting;
}else{
this.doConversion=_92;
this.getValueFromInput=_93;
this.doFormatting=_94;
}
var _97=false;
this.domainValidation=null;
if(_90&&_90!=""){
_97=true;
}
if(_96!=null){
if(_8f==0){
this.size=_96.size;
}else{
this.size=_8f;
}
this.domainValidation=_96.domainValidation;
if(this.domainValidation!=null&&_97){
this.domainValidation=this.domainValidation.clone();
this.domainValidation.parseAttributes(_90,this);
}
}
if(this.domainValidation==null&&_97){
this.domainValidation=new _98(_90,this);
}
if(this.domainValidation&&this.domainValidation!=null){
}
};
function _95(){
var str="Type Name: "+this.typeName+" Defined As: "+this.definedAs+" Size: "+this.size;
alert(this.typeName+this.domainValidation.upper);
if(this.domainValidation){
str+=" Domain Attributes : "+this.domainValidation.toString();
}
if(this.codeTableName){
str+=" Code Table Name : "+this.codeTableName;
}
return str;
};
function _99(_9a){
_7f[_9a.typeName]=_9a;
};
function _9b(){
return "Upper"+this.upper+"Leading"+this.leading+"Trailing"+this.trailing+"Compress"+this.compress+"MinChar"+this.minchar+"Minimum"+this.minimum+"Maximum"+this.maximum+"Pattern"+this.pattern+"Custom"+this.custom;
};
function _9c(_9d){
if(_9d&&_9d!=""){
if(this.compress){
_9d=_9e(_9d);
}else{
if(this.leading){
_9d=_9f(_9d);
}
if(this.trailing){
_9d=_a0(_9d);
}
}
if(this.upper){
_9d=_9d.toUpperCase();
}
}
return new String(_9d);
};
function _a1(){
var _a2=new _98();
_a2.upper=this.upper;
_a2.leading=this.leading;
_a2.trailing=this.trailing;
_a2.compress=this.compress;
_a2.minchar=this.minchar;
_a2.minimum=this.minimum;
_a2.maximum=this.maximum;
_a2.pattern=this.pattern;
_a2.custom=this.custom;
return _a2;
};
function _a3(_a4,_a5){
if(!_a4||_a4==""){
return;
}
var _a6=_a4.split(/\s+/g);
for(i=0;i<_a6.length;i+=2){
var _a7=_a6[i];
var _a8=_a6[i+1];
_a7=_a7.toLowerCase();
_a8=_a8.substring(1,_a8.length-1);
if(_a7=="upper"){
this.upper=_a9(_a8);
}else{
if(_a7=="leading"){
this.leading=_a9(_a8);
}else{
if(_a7=="trailing"){
this.trailing=_a9(_a8);
}else{
if(_a7=="compress"){
this.compress=_a9(_a8);
}else{
if(_a7=="minchar"){
this.minchar=_a8-0;
}else{
if(_a7=="minimum"){
var _aa=_2;
var _ab=_3;
_2="ISO8601";
_3="ISO8601";
var _ac=_a5.doConversion(_a8);
_2=_aa;
_3=_ab;
if(_ac.msg==null){
this.minimum=_ac.value;
}else{
alert(_ac.msg);
}
}else{
if(_a7=="maximum"){
var _aa=_2;
var _ab=_3;
_2="ISO8601";
_3="ISO8601";
var _ac=_a5.doConversion(_a8);
_2=_aa;
_3=_ab;
if(_ac.msg==null){
this.maximum=_ac.value;
}else{
alert(_ac.msg);
}
}else{
if(_a7=="pattern"){
this.pattern=_a8;
}else{
if(_a7=="custom"){
this.custom=_a8;
}
}
}
}
}
}
}
}
}
}
};
function _98(_ad,_ae){
this.parseAttributes=_a3;
this.clone=_a1;
this.print=_9b;
this.doPreValidationFormatting=_9c;
this.upper=false;
this.leading=false;
this.trailing=false;
this.compress=false;
this.minchar=0;
this.minimum="";
this.maximum="";
this.pattern=null;
this.custom="";
if(_ad&&_ad!=""){
this.parseAttributes(_ad,_ae);
}
};
function _af(_b0){
if(_b0){
var _b1="";
for(var i=0;i<_b0.length;i++){
if(_b0.charAt(i)!=" "){
_b1+=_b0.charAt(i);
}
}
return _b1;
}
};
function _a9(_b2){
var _b3=_b2.toLowerCase();
if(_b3=="true"||_b3=="yes"){
return true;
}else{
return false;
}
};
function _9f(_b4){
while(_b4.substring(0,1)==" "){
_b4=_b4.substring(1,_b4.length);
}
return _b4;
};
function _a0(_b5){
while(_b5.substring(_b5.length-1,_b5.length)==" "){
_b5=_b5.substring(0,_b5.length-1);
}
return _b5;
};
function _9e(_b6){
_b6=_9f(_b6);
_b6=_a0(_b6);
var _b7="";
var _b8=true;
var _b9=0;
for(var i=0;i<_b6.length;i++){
var _ba=(_b6.charAt(i)==" ")||(_b6.charAt(i)=="\n")||(_b6.charAt(i)=="\t");
if(!_ba||!_b8){
_b7+=_b6.charAt(i);
}
_b8=_ba;
}
return _b7;
};
function _bb(_bc){
var _bd=_bc+"";
var _be=_bd.indexOf("'");
var end=_bd.lastIndexOf("'");
if(_be==-1){
_be=_bd.indexOf("\"");
}
if(end==-1){
end=_bd.lastIndexOf("\"");
}
return _bd.substring(_be+1,end);
};
function _bf(_c0){
var _c1=jsPageID+"_formPreValidation";
if(window[_c1]!=null){
var _c2=window[_c1](_c0);
if(_c2==false){
return false;
}
}
return true;
};
function _c3(_c4){
var _c5=jsPageID+"_formPostValidation";
if(window[_c5]!=null){
var _c6=window[_c5](_c4);
if(_c6==false){
return false;
}
}
return true;
};
function vf(_c7,_c8,_c9){
var evt=dojo.fixEvent(_c9);
var _ca=dojo.stopEvent;
if(cm.wasFormSubmitted(_c7)){
_ca(evt);
return false;
}
var _cb=_bf(_c7);
if(_cb==false){
_ca(evt);
return false;
}
var _cc="";
var _cd=null;
for(var i=0;i<_c7.elements.length;i++){
var _ce=_c7.elements[i];
if(_ce.type=="text"||_ce.tagName=="TEXTAREA"){
var _cf=_ce.onblur;
var _d0=_bb(_ce.onblur);
var msg=v(_d0,_ce);
if(msg.length>0){
_cc+=_d1(msg,_ce)+"\n";
if(_cd==null){
_cd=_ce;
}
}
}
}
if(_cc.length>0){
alert(_cc);
_cd.focus();
_ca(evt);
return false;
}
var _cb=_c3(_c7);
if(_cb==false){
_ca(evt);
return false;
}
cm.setFormSubmitted(_c7,1);
return true;
};
function _d1(msg,_d2){
var _d3=_d4(_d2);
if(_d3!=null){
return _1f(FIELD_MSG,new Array(_d3,msg));
}else{
return msg;
}
};
function _d5(_d6,_d7){
var _d8=_d9(_d6,_d7);
if(_d8){
return _d4(_d8);
}
return null;
};
function _d4(_da){
var _db=_da.name;
var _dc=_db.split(".");
_db="__o3fwl."+_dc[1]+"."+_dc[2];
var _dd=document.getElementsByName(_db)[0];
var _de=null;
if(_dd){
_de=_dd.value;
}
if(_de&&_de!=""){
return _de;
}else{
return null;
}
};
function _d9(_df,_e0){
var _e0="__o3fwp."+_df+"."+_e0;
var _e1=document.getElementsByName(_e0)[0];
if(_e1){
return _e1;
}else{
return null;
}
};
function _e2(_e3){
var _e4=_7f[_e3];
while(true){
if(!_e4){
alert(_1f(UNKNOWN_DOMAIN,new Array(_e3)));
return null;
}
var _e5=_e4.definedAs;
if(!_e5){
return _e4.typeName;
}
_e4=_7f[_e5];
}
};
var _e6=new RegExp("(%\\d)","g");
function _e7(msg,_e8){
var _e9=msg;
var tmp=msg.split(invRe);
var _ea=new Array();
var _eb=0;
for(var i=0;i<tmp.length;i++){
if(tmp[i]){
_ea[_eb++]=tmp[i];
}
}
alert(_ea.length);
for(var j=0;j<_ea.length;j++){
_e9=_e9.replace(_ea[j],_e8[_ea[j].slice(1)-1]);
}
return _e9;
};
function _1f(msg,_ec){
var _ed=new String(msg);
var _ee=new Array();
_ee=msg.match(_e6);
if(_ee){
for(var i=0;i<_ee.length;i++){
_ed=_ed.replace(_ee[i],_ec[_ee[i].slice(1)-1]);
}
}
return _ed;
};
var _7f=new Array();
_99(new _8c("SVR_BOOLEAN","",0,"","",_2b,_2f,_31));
_99(new _8c("SVR_UNBOUNDED_STRING","",0,"","",_a,_c,_e));
_99(new _8c("SVR_STRING","",0,"","",_1c,_20,_22));
_99(new _8c("SVR_DATE","",0,"","",_67,_6b,_6d));
_99(new _8c("SVR_DATETIME","",0,"","",_6f,_73,_7a));
_99(new _8c("SVR_INT64","",0,"","",_33,_35,_37));
_99(new _8c("SVR_INT32","",0,"","",_39,_3f,_41));
_99(new _8c("SVR_INT16","",0,"","",_43,_49,_4b));
_99(new _8c("SVR_INT8","",0,"","",_4d,_53,_55));
_99(new _8c("SVR_MONEY","",0,"","",_16,_18,_1a));
_99(new _8c("SVR_FLOAT","",0,"","",_57,_5b,_5d));
_99(new _8c("SVR_DOUBLE","",0,"","",_5f,_63,_65));
_99(new _8c("SVR_BLOB","",0,"","",_10,_12,_14));
_99(new _8c("SVR_CHAR","",0,"","",_24,_27,_29));
});

