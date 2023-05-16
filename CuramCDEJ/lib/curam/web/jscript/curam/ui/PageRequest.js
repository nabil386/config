//>>built
define("curam/ui/PageRequest",["dojo/_base/declare","curam/debug"],function(_1,_2){
var _3=_1("curam.ui.PageRequest",null,{forceLoad:false,justRefresh:false,constructor:function(_4,_5,_6){
this.parameters={};
this.cdejParameters={};
this.cdejParameters["o3ctx"]="4096";
if(_5){
this.isHomePage=true;
}else{
this.isHomePage=false;
}
if(_6){
this.openInCurrentTab=true;
}else{
this.openInCurrentTab=false;
}
this.pageHolder=null;
var _7;
if(dojo.isString(_4)){
_7=_4;
curam.debug.log("PAGE REQUEST: "+_2.getProperty("curam.ui.PageRequest.url")+" "+_7);
}else{
curam.debug.log("PAGE REQUEST: "+_2.getProperty("curam.ui.PageRequest.descriptor")+" "+_4.toJson());
var tc=_4.tabContent;
_7=tc.pageID+"Page.do";
var _8=true;
for(param in tc.parameters){
if(_8){
_7+="?";
_8=false;
}else{
_7+="&";
}
_7+=param+"="+encodeURIComponent(tc.parameters[param]);
}
curam.debug.log("PAGE REQUEST: "+_2.getProperty("curam.ui.PageRequest.derived")+" "+_7);
}
var _9=_7.split("?");
this.parseUIMPageID(_9[0]);
if(_9.length==2){
this.parseParameters(_9[1]);
}
},parseUIMPageID:function(_a){
var _b=_a.split("/");
var _c=_b[_b.length-1];
this.pageID=_c.replace("Page.do","");
},parseParameterName:function(_d){
if(_d.charAt(0)=="a"&&_d.charAt(1)=="m"&&_d.charAt(2)=="p"&&_d.charAt(3)==";"){
return _d.substring(4,_d.length);
}else{
return _d;
}
},parseParameters:function(_e){
var _f=_e.split("&");
for(var i=0;i<_f.length;i++){
var _10=_f[i].split("=");
var _11=this.parseParameterName(_10[0]);
if(_11.length>0){
if(!this.isCDEJParam(_11)){
this.parameters[_11]=decodeURIComponent(_10[1].replace(/\+/g," "));
}else{
if(_11!="o3nocache"){
this.cdejParameters[_11]=decodeURIComponent(_10[1].replace(/\+/g," "));
}
}
}
}
},isCDEJParam:function(_12){
return (_12.charAt(0)=="o"&&_12.charAt(1)=="3")||(_12.charAt(0)=="_"&&_12.charAt(1)=="_"&&_12.charAt(2)=="o"&&_12.charAt(3)=="3");
},getQueryString:function(_13){
var _14="";
var _15;
for(_15 in this.parameters){
_14+=_15+"="+encodeURIComponent(this.parameters[_15])+"&";
}
if(!_13==true||_13==false){
for(_15 in this.cdejParameters){
_14+=_15+"="+encodeURIComponent(this.cdejParameters[_15])+"&";
}
}
_14=_14.substring(0,_14.length-1);
this.queryString=_14;
return this.queryString;
},getURL:function(_16){
var _17=this.pageID+"Page.do";
var qs=this.getQueryString(_16);
if(qs!=""){
_17+="?"+qs;
}
this.url=_17;
return this.url;
}});
return _3;
});
