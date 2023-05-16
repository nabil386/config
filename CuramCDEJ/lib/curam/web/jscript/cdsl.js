//>>built
require({cache:{"dojo/request/default":function(){
define(["exports","require","../has"],function(_1,_2,_3){
var _4=_3("config-requestProvider"),_5;
if(1||_3("host-webworker")){
_5="./xhr";
}else{
if(0){
_5="./node";
}
}
if(!_4){
_4=_5;
}
_1.getPlatformDefaultId=function(){
return _5;
};
_1.load=function(id,_6,_7,_8){
_2([id=="platform"?_5:_4],function(_9){
_7(_9);
});
};
});
},"dojo/store/Memory":function(){
define(["../_base/declare","./util/QueryResults","./util/SimpleQueryEngine"],function(_a,_b,_c){
var _d=null;
return _a("dojo.store.Memory",_d,{constructor:function(_e){
for(var i in _e){
this[i]=_e[i];
}
this.setData(this.data||[]);
},data:null,idProperty:"id",index:null,queryEngine:_c,get:function(id){
return this.data[this.index[id]];
},getIdentity:function(_f){
return _f[this.idProperty];
},put:function(_10,_11){
var _12=this.data,_13=this.index,_14=this.idProperty;
var id=_10[_14]=(_11&&"id" in _11)?_11.id:_14 in _10?_10[_14]:Math.random();
if(id in _13){
if(_11&&_11.overwrite===false){
throw new Error("Object already exists");
}
_12[_13[id]]=_10;
}else{
_13[id]=_12.push(_10)-1;
}
return id;
},add:function(_15,_16){
(_16=_16||{}).overwrite=false;
return this.put(_15,_16);
},remove:function(id){
var _17=this.index;
var _18=this.data;
if(id in _17){
_18.splice(_17[id],1);
this.setData(_18);
return true;
}
},query:function(_19,_1a){
return _b(this.queryEngine(_19,_1a)(this.data));
},setData:function(_1b){
if(_1b.items){
this.idProperty=_1b.identifier||this.idProperty;
_1b=this.data=_1b.items;
}else{
this.data=_1b;
}
this.index={};
for(var i=0,l=_1b.length;i<l;i++){
this.index[_1b[i][this.idProperty]]=i;
}
}});
});
},"dojo/i18n":function(){
define(["./_base/kernel","require","./has","./_base/array","./_base/config","./_base/lang","./_base/xhr","./json","module"],function(_1c,_1d,has,_1e,_1f,_20,xhr,_21,_22){
has.add("dojo-preload-i18n-Api",1);
1||has.add("dojo-v1x-i18n-Api",1);
var _23=_1c.i18n={},_24=/(^.*(^|\/)nls)(\/|$)([^\/]*)\/?([^\/]*)/,_25=function(_26,_27,_28,_29){
for(var _2a=[_28+_29],_2b=_27.split("-"),_2c="",i=0;i<_2b.length;i++){
_2c+=(_2c?"-":"")+_2b[i];
if(!_26||_26[_2c]){
_2a.push(_28+_2c+"/"+_29);
_2a.specificity=_2c;
}
}
return _2a;
},_2d={},_2e=function(_2f,_30,_31){
_31=_31?_31.toLowerCase():_1c.locale;
_2f=_2f.replace(/\./g,"/");
_30=_30.replace(/\./g,"/");
return (/root/i.test(_31))?(_2f+"/nls/"+_30):(_2f+"/nls/"+_31+"/"+_30);
},_32=_1c.getL10nName=function(_33,_34,_35){
return _33=_22.id+"!"+_2e(_33,_34,_35);
},_36=function(_37,_38,_39,_3a,_3b,_3c){
_37([_38],function(_3d){
var _3e=_20.clone(_3d.root||_3d.ROOT),_3f=_25(!_3d._v1x&&_3d,_3b,_39,_3a);
_37(_3f,function(){
for(var i=1;i<_3f.length;i++){
_3e=_20.mixin(_20.clone(_3e),arguments[i]);
}
var _40=_38+"/"+_3b;
_2d[_40]=_3e;
_3e.$locale=_3f.specificity;
_3c();
});
});
},_41=function(id,_42){
return /^\./.test(id)?_42(id):id;
},_43=function(_44){
var _45=_1f.extraLocale||[];
_45=_20.isArray(_45)?_45:[_45];
_45.push(_44);
return _45;
},_46=function(id,_47,_48){
if(has("dojo-preload-i18n-Api")){
var _49=id.split("*"),_4a=_49[1]=="preload";
if(_4a){
if(!_2d[id]){
_2d[id]=1;
_4b(_49[2],_21.parse(_49[3]),1,_47);
}
_48(1);
}
if(_4a||_4c(id,_47,_48)){
return;
}
}
var _4d=_24.exec(id),_4e=_4d[1]+"/",_4f=_4d[5]||_4d[4],_50=_4e+_4f,_51=(_4d[5]&&_4d[4]),_52=_51||_1c.locale||"",_53=_50+"/"+_52,_54=_51?[_52]:_43(_52),_55=_54.length,_56=function(){
if(!--_55){
_48(_20.delegate(_2d[_53]));
}
};
_1e.forEach(_54,function(_57){
var _58=_50+"/"+_57;
if(has("dojo-preload-i18n-Api")){
_59(_58);
}
if(!_2d[_58]){
_36(_47,_50,_4e,_4f,_57,_56);
}else{
_56();
}
});
};
if(has("dojo-unit-tests")){
var _5a=_23.unitTests=[];
}
if(has("dojo-preload-i18n-Api")||1){
var _5b=_23.normalizeLocale=function(_5c){
var _5d=_5c?_5c.toLowerCase():_1c.locale;
return _5d=="root"?"ROOT":_5d;
},_5e=function(mid,_5f){
return (1&&1)?_5f.isXdUrl(_1d.toUrl(mid+".js")):true;
},_60=0,_61=[],_4b=_23._preloadLocalizations=function(_62,_63,_64,_65){
_65=_65||_1d;
function _66(mid,_67){
if(_5e(mid,_65)||_64){
_65([mid],_67);
}else{
_86([mid],_67,_65);
}
};
function _68(_69,_6a){
var _6b=_69.split("-");
while(_6b.length){
if(_6a(_6b.join("-"))){
return;
}
_6b.pop();
}
_6a("ROOT");
};
function _6c(){
_60++;
};
function _6d(){
--_60;
while(!_60&&_61.length){
_46.apply(null,_61.shift());
}
};
function _6e(_6f,_70,loc,_71){
return _71.toAbsMid(_6f+_70+"/"+loc);
};
function _72(_73){
_73=_5b(_73);
_68(_73,function(loc){
if(_1e.indexOf(_63,loc)>=0){
var mid=_62.replace(/\./g,"/")+"_"+loc;
_6c();
_66(mid,function(_74){
for(var p in _74){
var _75=_74[p],_76=p.match(/(.+)\/([^\/]+)$/),_77,_78;
if(!_76){
continue;
}
_77=_76[2];
_78=_76[1]+"/";
if(!_75._localized){
continue;
}
var _79;
if(loc==="ROOT"){
var _7a=_79=_75._localized;
delete _75._localized;
_7a.root=_75;
_2d[_1d.toAbsMid(p)]=_7a;
}else{
_79=_75._localized;
_2d[_6e(_78,_77,loc,_1d)]=_75;
}
if(loc!==_73){
function _7b(_7c,_7d,_7e,_7f){
var _80=[],_81=[];
_68(_73,function(loc){
if(_7f[loc]){
_80.push(_1d.toAbsMid(_7c+loc+"/"+_7d));
_81.push(_6e(_7c,_7d,loc,_1d));
}
});
if(_80.length){
_6c();
_65(_80,function(){
for(var i=_80.length-1;i>=0;i--){
_7e=_20.mixin(_20.clone(_7e),arguments[i]);
_2d[_81[i]]=_7e;
}
_2d[_6e(_7c,_7d,_73,_1d)]=_20.clone(_7e);
_6d();
});
}else{
_2d[_6e(_7c,_7d,_73,_1d)]=_7e;
}
};
_7b(_78,_77,_75,_79);
}
}
_6d();
});
return true;
}
return false;
});
};
_72();
_1e.forEach(_1c.config.extraLocale,_72);
},_4c=function(id,_82,_83){
if(_60){
_61.push([id,_82,_83]);
}
return _60;
},_59=function(){
};
}
if(1){
var _84={},_85=new Function("__bundle","__checkForLegacyModules","__mid","__amdValue","var define = function(mid, factory){define.called = 1; __amdValue.result = factory || mid;},"+"\t   require = function(){define.called = 1;};"+"try{"+"define.called = 0;"+"eval(__bundle);"+"if(define.called==1)"+"return __amdValue;"+"if((__checkForLegacyModules = __checkForLegacyModules(__mid)))"+"return __checkForLegacyModules;"+"}catch(e){}"+"try{"+"return eval('('+__bundle+')');"+"}catch(e){"+"return e;"+"}"),_86=function(_87,_88,_89){
var _8a=[];
_1e.forEach(_87,function(mid){
var url=_89.toUrl(mid+".js");
function _46(_8b){
var _8c=_85(_8b,_59,mid,_84);
if(_8c===_84){
_8a.push(_2d[url]=_84.result);
}else{
if(_8c instanceof Error){
console.error("failed to evaluate i18n bundle; url="+url,_8c);
_8c={};
}
_8a.push(_2d[url]=(/nls\/[^\/]+\/[^\/]+$/.test(url)?_8c:{root:_8c,_v1x:1}));
}
};
if(_2d[url]){
_8a.push(_2d[url]);
}else{
var _8d=_89.syncLoadNls(mid);
if(!_8d){
_8d=_59(mid.replace(/nls\/([^\/]*)\/([^\/]*)$/,"nls/$2/$1"));
}
if(_8d){
_8a.push(_8d);
}else{
if(!xhr){
try{
_89.getText(url,true,_46);
}
catch(e){
_8a.push(_2d[url]={});
}
}else{
xhr.get({url:url,sync:true,load:_46,error:function(){
_8a.push(_2d[url]={});
}});
}
}
}
});
_88&&_88.apply(null,_8a);
};
_59=function(_8e){
for(var _8f,_90=_8e.split("/"),_91=_1c.global[_90[0]],i=1;_91&&i<_90.length-1;_91=_91[_90[i++]]){
}
if(_91){
_8f=_91[_90[i]];
if(!_8f){
_8f=_91[_90[i].replace(/-/g,"_")];
}
if(_8f){
_2d[_8e]=_8f;
}
}
return _8f;
};
_23.getLocalization=function(_92,_93,_94){
var _95,_96=_2e(_92,_93,_94);
_46(_96,(!_5e(_96,_1d)?function(_97,_98){
_86(_97,_98,_1d);
}:_1d),function(_99){
_95=_99;
});
return _95;
};
if(has("dojo-unit-tests")){
_5a.push(function(doh){
doh.register("tests.i18n.unit",function(t){
var _9a;
_9a=_85("{prop:1}",_59,"nonsense",_84);
t.is({prop:1},_9a);
t.is(undefined,_9a[1]);
_9a=_85("({prop:1})",_59,"nonsense",_84);
t.is({prop:1},_9a);
t.is(undefined,_9a[1]);
_9a=_85("{'prop-x':1}",_59,"nonsense",_84);
t.is({"prop-x":1},_9a);
t.is(undefined,_9a[1]);
_9a=_85("({'prop-x':1})",_59,"nonsense",_84);
t.is({"prop-x":1},_9a);
t.is(undefined,_9a[1]);
_9a=_85("define({'prop-x':1})",_59,"nonsense",_84);
t.is(_84,_9a);
t.is({"prop-x":1},_84.result);
_9a=_85("define('some/module', {'prop-x':1})",_59,"nonsense",_84);
t.is(_84,_9a);
t.is({"prop-x":1},_84.result);
_9a=_85("this is total nonsense and should throw an error",_59,"nonsense",_84);
t.is(_9a instanceof Error,true);
});
});
}
}
return _20.mixin(_23,{dynamic:true,normalize:_41,load:_46,cache:_2d,getL10nName:_32});
});
},"curam/util/ResourceBundle":function(){
define(["dojo/_base/declare","dojo/i18n","dojo/string"],function(_9b,_9c,_9d){
var _9e=_9b("curam.util.ResourceBundle",null,{_bundle:undefined,constructor:function(_9f,_a0){
var _a1=_9f.split(".");
var _a2=_a1[_a1.length-1];
var _a3=_a1.length==1?"curam.application":_9f.slice(0,_9f.length-_a2.length-1);
try{
var b=_9c.getLocalization(_a3,_a2,_a0);
if(this._isEmpty(b)){
throw new Error("Empty resource bundle.");
}else{
this._bundle=b;
}
}
catch(e){
throw new Error("Unable to access resource bundle: "+_a3+"."+_a2+": "+e.message);
}
},_isEmpty:function(_a4){
for(var _a5 in _a4){
return false;
}
return true;
},getProperty:function(key,_a6){
var msg=this._bundle[key];
var _a7=msg;
if(_a6){
_a7=_9d.substitute(msg,_a6);
}
return _a7;
}});
return _9e;
});
},"dojo/string":function(){
define(["./_base/kernel","./_base/lang"],function(_a8,_a9){
var _aa=/[&<>'"\/]/g;
var _ab={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#x27;","/":"&#x2F;"};
var _ac={};
_a9.setObject("dojo.string",_ac);
_ac.escape=function(str){
if(!str){
return "";
}
return str.replace(_aa,function(c){
return _ab[c];
});
};
_ac.rep=function(str,num){
if(num<=0||!str){
return "";
}
var buf=[];
for(;;){
if(num&1){
buf.push(str);
}
if(!(num>>=1)){
break;
}
str+=str;
}
return buf.join("");
};
_ac.pad=function(_ad,_ae,ch,end){
if(!ch){
ch="0";
}
var out=String(_ad),pad=_ac.rep(ch,Math.ceil((_ae-out.length)/ch.length));
return end?out+pad:pad+out;
};
_ac.substitute=function(_af,map,_b0,_b1){
_b1=_b1||_a8.global;
_b0=_b0?_a9.hitch(_b1,_b0):function(v){
return v;
};
return _af.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_b2,key,_b3){
var _b4=_a9.getObject(key,false,map);
if(_b3){
_b4=_a9.getObject(_b3,false,_b1).call(_b1,_b4,key);
}
return _b0(_b4,key).toString();
});
};
_ac.trim=String.prototype.trim?_a9.trim:function(str){
str=str.replace(/^\s+/,"");
for(var i=str.length-1;i>=0;i--){
if(/\S/.test(str.charAt(i))){
str=str.substring(0,i+1);
break;
}
}
return str;
};
return _ac;
});
},"dojo/store/Observable":function(){
define(["../_base/kernel","../_base/lang","../when","../_base/array"],function(_b5,_b6,_b7,_b8){
var _b9=function(_ba){
var _bb,_bc=[],_bd=0;
_ba=_b6.delegate(_ba);
_ba.notify=function(_be,_bf){
_bd++;
var _c0=_bc.slice();
for(var i=0,l=_c0.length;i<l;i++){
_c0[i](_be,_bf);
}
};
var _c1=_ba.query;
_ba.query=function(_c2,_c3){
_c3=_c3||{};
var _c4=_c1.apply(this,arguments);
if(_c4&&_c4.forEach){
var _c5=_b6.mixin({},_c3);
delete _c5.start;
delete _c5.count;
var _c6=_ba.queryEngine&&_ba.queryEngine(_c2,_c5);
var _c7=_bd;
var _c8=[],_c9;
_c4.observe=function(_ca,_cb){
if(_c8.push(_ca)==1){
_bc.push(_c9=function(_cc,_cd){
_b7(_c4,function(_ce){
var _cf=_ce.length!=_c3.count;
var i,l,_ca;
if(++_c7!=_bd){
throw new Error("Query is out of date, you must observe() the query prior to any data modifications");
}
var _d0,_d1=-1,_d2=-1;
if(_cd!==_bb){
var _d3=[].concat(_ce);
if(_c6&&!_cc){
_d3=_c6(_ce);
}
for(i=0,l=_ce.length;i<l;i++){
var _d4=_ce[i];
if(_ba.getIdentity(_d4)==_cd){
if(_d3.indexOf(_d4)<0){
continue;
}
_d0=_d4;
_d1=i;
if(_c6||!_cc){
_ce.splice(i,1);
}
break;
}
}
}
if(_c6){
if(_cc&&(_c6.matches?_c6.matches(_cc):_c6([_cc]).length)){
var _d5=_d1>-1?_d1:_ce.length;
_ce.splice(_d5,0,_cc);
_d2=_b8.indexOf(_c6(_ce),_cc);
_ce.splice(_d5,1);
if((_c3.start&&_d2==0)||(!_cf&&_d2==_ce.length)){
_d2=-1;
}else{
_ce.splice(_d2,0,_cc);
}
}
}else{
if(_cc){
if(_cd!==_bb){
_d2=_d1;
}else{
if(!_c3.start){
_d2=_ba.defaultIndex||0;
_ce.splice(_d2,0,_cc);
}
}
}
}
if((_d1>-1||_d2>-1)&&(_cb||!_c6||(_d1!=_d2))){
var _d6=_c8.slice();
for(i=0;_ca=_d6[i];i++){
_ca(_cc||_d0,_d1,_d2);
}
}
});
});
}
var _d7={};
_d7.remove=_d7.cancel=function(){
var _d8=_b8.indexOf(_c8,_ca);
if(_d8>-1){
_c8.splice(_d8,1);
if(!_c8.length){
_bc.splice(_b8.indexOf(_bc,_c9),1);
}
}
};
return _d7;
};
}
return _c4;
};
var _d9;
function _da(_db,_dc){
var _dd=_ba[_db];
if(_dd){
_ba[_db]=function(_de){
var _df;
if(_db==="put"){
_df=_ba.getIdentity(_de);
}
if(_d9){
return _dd.apply(this,arguments);
}
_d9=true;
try{
var _e0=_dd.apply(this,arguments);
_b7(_e0,function(_e1){
_dc((typeof _e1=="object"&&_e1)||_de,_df);
});
return _e0;
}
finally{
_d9=false;
}
};
}
};
_da("put",function(_e2,_e3){
_ba.notify(_e2,_e3);
});
_da("add",function(_e4){
_ba.notify(_e4);
});
_da("remove",function(id){
_ba.notify(undefined,id);
});
return _ba;
};
_b6.setObject("dojo.store.Observable",_b9);
return _b9;
});
},"dojo/request/registry":function(){
define(["require","../_base/array","./default!platform","./util"],function(_e5,_e6,_e7,_e8){
var _e9=[];
function _ea(url,_eb){
var _ec=_e9.slice(0),i=0,_ed;
while(_ed=_ec[i++]){
if(_ed(url,_eb)){
return _ed.request.call(null,url,_eb);
}
}
return _e7.apply(null,arguments);
};
function _ee(_ef,_f0){
var _f1;
if(_f0){
if(_ef.test){
_f1=function(url){
return _ef.test(url);
};
}else{
if(_ef.apply&&_ef.call){
_f1=function(){
return _ef.apply(null,arguments);
};
}else{
_f1=function(url){
return url===_ef;
};
}
}
_f1.request=_f0;
}else{
_f1=function(){
return true;
};
_f1.request=_ef;
}
return _f1;
};
_ea.register=function(url,_f2,_f3){
var _f4=_ee(url,_f2);
_e9[(_f3?"unshift":"push")](_f4);
return {remove:function(){
var idx;
if(~(idx=_e6.indexOf(_e9,_f4))){
_e9.splice(idx,1);
}
}};
};
_ea.load=function(id,_f5,_f6,_f7){
if(id){
_e5([id],function(_f8){
_e7=_f8;
_f6(_ea);
});
}else{
_f6(_ea);
}
};
_e8.addCommonMethods(_ea);
return _ea;
});
},"dojo/store/util/SimpleQueryEngine":function(){
define(["../../_base/array"],function(_f9){
return function(_fa,_fb){
switch(typeof _fa){
default:
throw new Error("Can not query with a "+typeof _fa);
case "object":
case "undefined":
var _fc=_fa;
_fa=function(_fd){
for(var key in _fc){
var _fe=_fc[key];
if(_fe&&_fe.test){
if(!_fe.test(_fd[key],_fd)){
return false;
}
}else{
if(_fe!=_fd[key]){
return false;
}
}
}
return true;
};
break;
case "string":
if(!this[_fa]){
throw new Error("No filter function "+_fa+" was found in store");
}
_fa=this[_fa];
case "function":
}
function _ff(_100){
var _101=_f9.filter(_100,_fa);
var _102=_fb&&_fb.sort;
if(_102){
_101.sort(typeof _102=="function"?_102:function(a,b){
for(var sort,i=0;sort=_102[i];i++){
var _103=a[sort.attribute];
var _104=b[sort.attribute];
_103=_103!=null?_103.valueOf():_103;
_104=_104!=null?_104.valueOf():_104;
if(_103!=_104){
return !!sort.descending==(_103==null||_103>_104)?-1:1;
}
}
return 0;
});
}
if(_fb&&(_fb.start||_fb.count)){
var _105=_101.length;
_101=_101.slice(_fb.start||0,(_fb.start||0)+(_fb.count||Infinity));
_101.total=_105;
}
return _101;
};
_ff.matches=_fa;
return _ff;
};
});
},"dojo/date":function(){
define(["./has","./_base/lang"],function(has,lang){
var date={};
date.getDaysInMonth=function(_106){
var _107=_106.getMonth();
var days=[31,28,31,30,31,30,31,31,30,31,30,31];
if(_107==1&&date.isLeapYear(_106)){
return 29;
}
return days[_107];
};
date.isLeapYear=function(_108){
var year=_108.getFullYear();
return !(year%400)||(!(year%4)&&!!(year%100));
};
date.getTimezoneName=function(_109){
var str=_109.toString();
var tz="";
var _10a;
var pos=str.indexOf("(");
if(pos>-1){
tz=str.substring(++pos,str.indexOf(")"));
}else{
var pat=/([A-Z\/]+) \d{4}$/;
if((_10a=str.match(pat))){
tz=_10a[1];
}else{
str=_109.toLocaleString();
pat=/ ([A-Z\/]+)$/;
if((_10a=str.match(pat))){
tz=_10a[1];
}
}
}
return (tz=="AM"||tz=="PM")?"":tz;
};
date.compare=function(_10b,_10c,_10d){
_10b=new Date(+_10b);
_10c=new Date(+(_10c||new Date()));
if(_10d=="date"){
_10b.setHours(0,0,0,0);
_10c.setHours(0,0,0,0);
}else{
if(_10d=="time"){
_10b.setFullYear(0,0,0);
_10c.setFullYear(0,0,0);
}
}
if(_10b>_10c){
return 1;
}
if(_10b<_10c){
return -1;
}
return 0;
};
date.add=function(date,_10e,_10f){
var sum=new Date(+date);
var _110=false;
var _111="Date";
switch(_10e){
case "day":
break;
case "weekday":
var days,_112;
var mod=_10f%5;
if(!mod){
days=(_10f>0)?5:-5;
_112=(_10f>0)?((_10f-5)/5):((_10f+5)/5);
}else{
days=mod;
_112=parseInt(_10f/5);
}
var strt=date.getDay();
var adj=0;
if(strt==6&&_10f>0){
adj=1;
}else{
if(strt==0&&_10f<0){
adj=-1;
}
}
var trgt=strt+days;
if(trgt==0||trgt==6){
adj=(_10f>0)?2:-2;
}
_10f=(7*_112)+days+adj;
break;
case "year":
_111="FullYear";
_110=true;
break;
case "week":
_10f*=7;
break;
case "quarter":
_10f*=3;
case "month":
_110=true;
_111="Month";
break;
default:
_111="UTC"+_10e.charAt(0).toUpperCase()+_10e.substring(1)+"s";
}
if(_111){
sum["set"+_111](sum["get"+_111]()+_10f);
}
if(_110&&(sum.getDate()<date.getDate())){
sum.setDate(0);
}
return sum;
};
date.difference=function(_113,_114,_115){
_114=_114||new Date();
_115=_115||"day";
var _116=_114.getFullYear()-_113.getFullYear();
var _117=1;
switch(_115){
case "quarter":
var m1=_113.getMonth();
var m2=_114.getMonth();
var q1=Math.floor(m1/3)+1;
var q2=Math.floor(m2/3)+1;
q2+=(_116*4);
_117=q2-q1;
break;
case "weekday":
var days=Math.round(date.difference(_113,_114,"day"));
var _118=parseInt(date.difference(_113,_114,"week"));
var mod=days%7;
if(mod==0){
days=_118*5;
}else{
var adj=0;
var aDay=_113.getDay();
var bDay=_114.getDay();
_118=parseInt(days/7);
mod=days%7;
var _119=new Date(_113);
_119.setDate(_119.getDate()+(_118*7));
var _11a=_119.getDay();
if(days>0){
switch(true){
case aDay==6:
adj=-1;
break;
case aDay==0:
adj=0;
break;
case bDay==6:
adj=-1;
break;
case bDay==0:
adj=-2;
break;
case (_11a+mod)>5:
adj=-2;
}
}else{
if(days<0){
switch(true){
case aDay==6:
adj=0;
break;
case aDay==0:
adj=1;
break;
case bDay==6:
adj=2;
break;
case bDay==0:
adj=1;
break;
case (_11a+mod)<0:
adj=2;
}
}
}
days+=adj;
days-=(_118*2);
}
_117=days;
break;
case "year":
_117=_116;
break;
case "month":
_117=(_114.getMonth()-_113.getMonth())+(_116*12);
break;
case "week":
_117=parseInt(date.difference(_113,_114,"day")/7);
break;
case "day":
_117/=24;
case "hour":
_117/=60;
case "minute":
_117/=60;
case "second":
_117/=1000;
case "millisecond":
_117*=_114.getTime()-_113.getTime();
}
return Math.round(_117);
};
1&&lang.mixin(lang.getObject("dojo.date",true),date);
return date;
});
},"curam/cdsl/request/CuramService":function(){
define(["dojo/_base/declare","curam/cdsl/Struct","curam/cdsl/_base/FacadeMethodResponse","dojo/_base/lang","curam/cdsl/_base/_Connection","curam/cdsl/_base/FacadeMethodCall"],function(_11b,_11c,_11d,lang){
var _11e={dataAdapter:null},_11f=function(_120){
var o=lang.clone(_11e);
o=lang.mixin(o,_120);
return o;
},rule="********************************************************",_121=_11b(null,{_connection:null,_dataAdapter:null,constructor:function(_122,opts){
var _123=_11f(opts);
this._connection=_122;
this._dataAdapter=_123.dataAdapter;
},call:function(_124,_125){
var _126=_124[0];
if(!_126.dataAdapter()){
_126.dataAdapter(this._dataAdapter);
}
var _127=this._connection.invoke(_126,_125);
return _127.then(lang.hitch(this,function(_128){
var _129=new _11d(_126,_128,this._connection.metadata());
if(_129.failed()){
var e=_129.getError();
if(_129.devMode()){
console.log(rule);
console.log(e.toString());
console.log(rule);
}
throw e;
}
this._connection.updateMetadata(_129);
return [_129.returnValue()];
}));
}});
return _121;
});
},"curam/cdsl/util/FormatDateTime":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/date/locale","dojo/date"],function(_12a,lang,_12b,_12c){
return {_checkPreferences:function(_12d,_12e,_12f){
if(_12d===undefined){
throw new Error("The dateFormat preference was not available");
}
if(_12e===undefined){
throw new Error("The timeFormat preference was not available");
}
if(_12f===undefined){
throw new Error("The timezone preference was not available");
}
},formatDateTime:function(date,_130){
var _131=_130.getPreference("dateFormat");
var _132=_130.getPreference("timeFormat");
var _133=_130.getPreference("timezone");
this._checkPreferences(_131,_132,_133);
var _134=date.getTimezoneOffset()*60000;
var _135=Date.parse(date)+_134;
var _136=new Date(_135-(_133*60000));
return _12b.format(_136,{"datePattern":_131,"timePattern":_132});
},formatDate:function(date,_137){
var _138=_137.getPreference("dateFormat");
var _139=_137.getPreference("timezone");
this._checkPreferences(_138,"ignore",_139);
var _13a=date.getTimezoneOffset()*60000;
var _13b=Date.parse(date)+_13a;
var _13c=new Date(_13b);
return _12b.format(_13c,{"datePattern":_138,"selector":"date"});
},_formatTime:function(date,_13d,_13e){
var _13f=_13d.getPreference("timeFormat");
var _140=_13d.getPreference("timezone");
this._checkPreferences("ignore",_13f,_140);
var _141=date.getTimezoneOffset()*60000;
var _142=Date.parse(date)+_141;
if(_13e!=undefined&&_13e){
var _143=new Date(_142-(_140*60000));
return _12b.format(_143,{"timePattern":_13f,"selector":"time"});
}else{
return _12b.format(new Date(_142),{"timePattern":_13f,"selector":"time"});
}
}};
});
},"curam/cdsl/types/codetable/CodeTableItem":function(){
define(["dojo/_base/declare"],function(_144){
var _145=_144(null,{_code:null,_desc:null,_isDefault:null,constructor:function(code,desc){
this._code=code;
this._desc=desc;
this._isDefault=false;
},getCode:function(){
return this._code;
},getDescription:function(){
return this._desc;
},isDefault:function(_146){
if(typeof _146==="undefined"){
return this._isDefault||false;
}else{
var _147=this._isDefault;
this._isDefault=_146;
return _147;
}
}});
return _145;
});
},"dijit/_WidgetBase":function(){
define(["require","dojo/_base/array","dojo/aspect","dojo/_base/config","dojo/_base/connect","dojo/_base/declare","dojo/dom","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/dom-geometry","dojo/dom-style","dojo/has","dojo/_base/kernel","dojo/_base/lang","dojo/on","dojo/ready","dojo/Stateful","dojo/topic","dojo/_base/window","./Destroyable","dojo/has!dojo-bidi?./_BidiMixin","./registry"],function(_148,_149,_14a,_14b,_14c,_14d,dom,_14e,_14f,_150,_151,_152,has,_153,lang,on,_154,_155,_156,win,_157,_158,_159){
var _15a=typeof (dojo.global.perf)!="undefined";
1||has.add("dijit-legacy-requires",!_153.isAsync);
has.add("dojo-bidi",false);
if(1){
_154(0,function(){
var _15b=["dijit/_base/manager"];
_148(_15b);
});
}
var _15c={};
function _15d(obj){
var ret={};
for(var attr in obj){
ret[attr.toLowerCase()]=true;
}
return ret;
};
function _15e(attr){
return function(val){
_14e[val?"set":"remove"](this.domNode,attr,val);
this._set(attr,val);
};
};
function _15f(a,b){
return a===b||(a!==a&&b!==b);
};
var _160=_14d("dijit._WidgetBase",[_155,_157],{id:"",_setIdAttr:"domNode",lang:"",_setLangAttr:_15e("lang"),dir:"",_setDirAttr:_15e("dir"),"class":"",_setClassAttr:{node:"domNode",type:"class"},_setTypeAttr:null,style:"",title:"",tooltip:"",baseClass:"",srcNodeRef:null,domNode:null,containerNode:null,ownerDocument:null,_setOwnerDocumentAttr:function(val){
this._set("ownerDocument",val);
},attributeMap:{},_blankGif:_14b.blankGif||_148.toUrl("dojo/resources/blank.gif"),_introspect:function(){
var ctor=this.constructor;
if(!ctor._setterAttrs){
var _161=ctor.prototype,_162=ctor._setterAttrs=[],_163=(ctor._onMap={});
for(var name in _161.attributeMap){
_162.push(name);
}
for(name in _161){
if(/^on/.test(name)){
_163[name.substring(2).toLowerCase()]=name;
}
if(/^_set[A-Z](.*)Attr$/.test(name)){
name=name.charAt(4).toLowerCase()+name.substr(5,name.length-9);
if(!_161.attributeMap||!(name in _161.attributeMap)){
_162.push(name);
}
}
}
}
},postscript:function(_164,_165){
this.create(_164,_165);
},create:function(_166,_167){
if(_15a){
perf.widgetStartedLoadingCallback();
}
this._introspect();
this.srcNodeRef=dom.byId(_167);
this._connects=[];
this._supportingWidgets=[];
if(this.srcNodeRef&&(typeof this.srcNodeRef.id=="string")){
this.id=this.srcNodeRef.id;
}
if(_166){
this.params=_166;
lang.mixin(this,_166);
}
this.postMixInProperties();
if(!this.id){
this.id=_159.getUniqueId(this.declaredClass.replace(/\./g,"_"));
if(this.params){
delete this.params.id;
}
}
this.ownerDocument=this.ownerDocument||(this.srcNodeRef?this.srcNodeRef.ownerDocument:document);
this.ownerDocumentBody=win.body(this.ownerDocument);
_159.add(this);
this.buildRendering();
var _168;
if(this.domNode){
this._applyAttributes();
var _169=this.srcNodeRef;
if(_169&&_169.parentNode&&this.domNode!==_169){
_169.parentNode.replaceChild(this.domNode,_169);
_168=true;
}
this.domNode.setAttribute("widgetId",this.id);
}
this.postCreate();
if(_168){
delete this.srcNodeRef;
}
this._created=true;
if(_15a){
perf.widgetLoadedCallback(this);
}
},_applyAttributes:function(){
var _16a={};
for(var key in this.params||{}){
_16a[key]=this._get(key);
}
_149.forEach(this.constructor._setterAttrs,function(key){
if(!(key in _16a)){
var val=this._get(key);
if(val){
this.set(key,val);
}
}
},this);
for(key in _16a){
this.set(key,_16a[key]);
}
},postMixInProperties:function(){
},buildRendering:function(){
if(!this.domNode){
this.domNode=this.srcNodeRef||this.ownerDocument.createElement("div");
}
if(this.baseClass){
var _16b=this.baseClass.split(" ");
if(!this.isLeftToRight()){
_16b=_16b.concat(_149.map(_16b,function(name){
return name+"Rtl";
}));
}
_14f.add(this.domNode,_16b);
}
},postCreate:function(){
},startup:function(){
if(this._started){
return;
}
this._started=true;
_149.forEach(this.getChildren(),function(obj){
if(!obj._started&&!obj._destroyed&&lang.isFunction(obj.startup)){
obj.startup();
obj._started=true;
}
});
},destroyRecursive:function(_16c){
this._beingDestroyed=true;
this.destroyDescendants(_16c);
this.destroy(_16c);
},destroy:function(_16d){
this._beingDestroyed=true;
this.uninitialize();
function _16e(w){
if(w.destroyRecursive){
w.destroyRecursive(_16d);
}else{
if(w.destroy){
w.destroy(_16d);
}
}
};
_149.forEach(this._connects,lang.hitch(this,"disconnect"));
_149.forEach(this._supportingWidgets,_16e);
if(this.domNode){
_149.forEach(_159.findWidgets(this.domNode,this.containerNode),_16e);
}
this.destroyRendering(_16d);
_159.remove(this.id);
this._destroyed=true;
},destroyRendering:function(_16f){
if(this.bgIframe){
this.bgIframe.destroy(_16f);
delete this.bgIframe;
}
if(this.domNode){
if(_16f){
_14e.remove(this.domNode,"widgetId");
}else{
_150.destroy(this.domNode);
}
delete this.domNode;
}
if(this.srcNodeRef){
if(!_16f){
_150.destroy(this.srcNodeRef);
}
delete this.srcNodeRef;
}
},destroyDescendants:function(_170){
_149.forEach(this.getChildren(),function(_171){
if(_171.destroyRecursive){
_171.destroyRecursive(_170);
}
});
},uninitialize:function(){
return false;
},_setStyleAttr:function(_172){
var _173=this.domNode;
if(lang.isObject(_172)){
_152.set(_173,_172);
}else{
if(_173.style.cssText){
_173.style.cssText+="; "+_172;
}else{
_173.style.cssText=_172;
}
}
this._set("style",_172);
},_attrToDom:function(attr,_174,_175){
_175=arguments.length>=3?_175:this.attributeMap[attr];
_149.forEach(lang.isArray(_175)?_175:[_175],function(_176){
var _177=this[_176.node||_176||"domNode"];
var type=_176.type||"attribute";
switch(type){
case "attribute":
if(lang.isFunction(_174)){
_174=lang.hitch(this,_174);
}
var _178=_176.attribute?_176.attribute:(/^on[A-Z][a-zA-Z]*$/.test(attr)?attr.toLowerCase():attr);
if(_177.tagName){
_14e.set(_177,_178,_174);
}else{
_177.set(_178,_174);
}
break;
case "innerText":
_177.innerHTML="";
_177.appendChild(this.ownerDocument.createTextNode(_174));
break;
case "innerHTML":
_177.innerHTML=_174;
break;
case "class":
_14f.replace(_177,_174,this[attr]);
break;
}
},this);
},get:function(name){
var _179=this._getAttrNames(name);
return this[_179.g]?this[_179.g]():this._get(name);
},set:function(name,_17a){
if(typeof name==="object"){
for(var x in name){
this.set(x,name[x]);
}
return this;
}
var _17b=this._getAttrNames(name),_17c=this[_17b.s];
if(lang.isFunction(_17c)){
var _17d=_17c.apply(this,Array.prototype.slice.call(arguments,1));
}else{
var _17e=this.focusNode&&!lang.isFunction(this.focusNode)?"focusNode":"domNode",tag=this[_17e]&&this[_17e].tagName,_17f=tag&&(_15c[tag]||(_15c[tag]=_15d(this[_17e]))),map=name in this.attributeMap?this.attributeMap[name]:_17b.s in this?this[_17b.s]:((_17f&&_17b.l in _17f&&typeof _17a!="function")||/^aria-|^data-|^role$/.test(name))?_17e:null;
if(map!=null){
this._attrToDom(name,_17a,map);
}
this._set(name,_17a);
}
return _17d||this;
},_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
var uc=name.replace(/^[a-z]|-[a-zA-Z]/g,function(c){
return c.charAt(c.length-1).toUpperCase();
});
return (apn[name]={n:name+"Node",s:"_set"+uc+"Attr",g:"_get"+uc+"Attr",l:uc.toLowerCase()});
},_set:function(name,_180){
var _181=this[name];
this[name]=_180;
if(this._created&&!_15f(_181,_180)){
if(this._watchCallbacks){
this._watchCallbacks(name,_181,_180);
}
this.emit("attrmodified-"+name,{detail:{prevValue:_181,newValue:_180}});
}
},_get:function(name){
return this[name];
},emit:function(type,_182,_183){
_182=_182||{};
if(_182.bubbles===undefined){
_182.bubbles=true;
}
if(_182.cancelable===undefined){
_182.cancelable=true;
}
if(!_182.detail){
_182.detail={};
}
_182.detail.widget=this;
var ret,_184=this["on"+type];
if(_184){
ret=_184.apply(this,_183?_183:[_182]);
}
if(this._started&&!this._beingDestroyed){
on.emit(this.domNode,type.toLowerCase(),_182);
}
return ret;
},on:function(type,func){
var _185=this._onMap(type);
if(_185){
return _14a.after(this,_185,func,true);
}
return this.own(on(this.domNode,type,func))[0];
},_onMap:function(type){
var ctor=this.constructor,map=ctor._onMap;
if(!map){
map=(ctor._onMap={});
for(var attr in ctor.prototype){
if(/^on/.test(attr)){
map[attr.replace(/^on/,"").toLowerCase()]=attr;
}
}
}
return map[typeof type=="string"&&type.toLowerCase()];
},toString:function(){
return "[Widget "+this.declaredClass+", "+(this.id||"NO ID")+"]";
},getChildren:function(){
return this.containerNode?_159.findWidgets(this.containerNode):[];
},getParent:function(){
return _159.getEnclosingWidget(this.domNode.parentNode);
},connect:function(obj,_186,_187){
return this.own(_14c.connect(obj,_186,this,_187))[0];
},disconnect:function(_188){
_188.remove();
},subscribe:function(t,_189){
return this.own(_156.subscribe(t,lang.hitch(this,_189)))[0];
},unsubscribe:function(_18a){
_18a.remove();
},isLeftToRight:function(){
return this.dir?(this.dir.toLowerCase()=="ltr"):_151.isBodyLtr(this.ownerDocument);
},isFocusable:function(){
return this.focus&&(_152.get(this.domNode,"display")!="none");
},placeAt:function(_18b,_18c){
var _18d=!_18b.tagName&&_159.byId(_18b);
if(_18d&&_18d.addChild&&(!_18c||typeof _18c==="number")){
_18d.addChild(this,_18c);
}else{
var ref=_18d&&("domNode" in _18d)?(_18d.containerNode&&!/after|before|replace/.test(_18c||"")?_18d.containerNode:_18d.domNode):dom.byId(_18b,this.ownerDocument);
_150.place(this.domNode,ref,_18c);
if(!this._started&&(this.getParent()||{})._started){
this.startup();
}
}
return this;
},defer:function(fcn,_18e){
var _18f=setTimeout(lang.hitch(this,function(){
if(!_18f){
return;
}
_18f=null;
if(!this._destroyed){
lang.hitch(this,fcn)();
}
}),_18e||0);
return {remove:function(){
if(_18f){
clearTimeout(_18f);
_18f=null;
}
return null;
}};
}});
if(has("dojo-bidi")){
_160.extend(_158);
}
return _160;
});
},"curam/define":function(){
define(["dojo/_base/lang"],function(lang){
var _190=this;
if(typeof (_190.curam)=="undefined"){
_190.curam={};
}
if(typeof (_190.curam.define)=="undefined"){
lang.mixin(_190.curam,{define:{}});
}
lang.mixin(_190.curam.define,{singleton:function(_191,_192){
var _193=_191.split(".");
var _194=window;
for(var i=0;i<_193.length;i++){
var part=_193[i];
if(typeof _194[part]=="undefined"){
_194[part]={};
}
_194=_194[part];
}
if(_192){
lang.mixin(_194,_192);
}
}});
return _190.curam.define;
});
},"dojo/cldr/supplemental":function(){
define(["../_base/lang","../i18n"],function(lang,i18n){
var _195={};
lang.setObject("dojo.cldr.supplemental",_195);
_195.getFirstDayOfWeek=function(_196){
var _197={bd:5,mv:5,ae:6,af:6,bh:6,dj:6,dz:6,eg:6,iq:6,ir:6,jo:6,kw:6,ly:6,ma:6,om:6,qa:6,sa:6,sd:6,sy:6,ye:6,ag:0,ar:0,as:0,au:0,br:0,bs:0,bt:0,bw:0,by:0,bz:0,ca:0,cn:0,co:0,dm:0,"do":0,et:0,gt:0,gu:0,hk:0,hn:0,id:0,ie:0,il:0,"in":0,jm:0,jp:0,ke:0,kh:0,kr:0,la:0,mh:0,mm:0,mo:0,mt:0,mx:0,mz:0,ni:0,np:0,nz:0,pa:0,pe:0,ph:0,pk:0,pr:0,py:0,sg:0,sv:0,th:0,tn:0,tt:0,tw:0,um:0,us:0,ve:0,vi:0,ws:0,za:0,zw:0};
var _198=_195._region(_196);
var dow=_197[_198];
return (dow===undefined)?1:dow;
};
_195._region=function(_199){
_199=i18n.normalizeLocale(_199);
var tags=_199.split("-");
var _19a=tags[1];
if(!_19a){
_19a={aa:"et",ab:"ge",af:"za",ak:"gh",am:"et",ar:"eg",as:"in",av:"ru",ay:"bo",az:"az",ba:"ru",be:"by",bg:"bg",bi:"vu",bm:"ml",bn:"bd",bo:"cn",br:"fr",bs:"ba",ca:"es",ce:"ru",ch:"gu",co:"fr",cr:"ca",cs:"cz",cv:"ru",cy:"gb",da:"dk",de:"de",dv:"mv",dz:"bt",ee:"gh",el:"gr",en:"us",es:"es",et:"ee",eu:"es",fa:"ir",ff:"sn",fi:"fi",fj:"fj",fo:"fo",fr:"fr",fy:"nl",ga:"ie",gd:"gb",gl:"es",gn:"py",gu:"in",gv:"gb",ha:"ng",he:"il",hi:"in",ho:"pg",hr:"hr",ht:"ht",hu:"hu",hy:"am",ia:"fr",id:"id",ig:"ng",ii:"cn",ik:"us","in":"id",is:"is",it:"it",iu:"ca",iw:"il",ja:"jp",ji:"ua",jv:"id",jw:"id",ka:"ge",kg:"cd",ki:"ke",kj:"na",kk:"kz",kl:"gl",km:"kh",kn:"in",ko:"kr",ks:"in",ku:"tr",kv:"ru",kw:"gb",ky:"kg",la:"va",lb:"lu",lg:"ug",li:"nl",ln:"cd",lo:"la",lt:"lt",lu:"cd",lv:"lv",mg:"mg",mh:"mh",mi:"nz",mk:"mk",ml:"in",mn:"mn",mo:"ro",mr:"in",ms:"my",mt:"mt",my:"mm",na:"nr",nb:"no",nd:"zw",ne:"np",ng:"na",nl:"nl",nn:"no",no:"no",nr:"za",nv:"us",ny:"mw",oc:"fr",om:"et",or:"in",os:"ge",pa:"in",pl:"pl",ps:"af",pt:"br",qu:"pe",rm:"ch",rn:"bi",ro:"ro",ru:"ru",rw:"rw",sa:"in",sd:"in",se:"no",sg:"cf",si:"lk",sk:"sk",sl:"si",sm:"ws",sn:"zw",so:"so",sq:"al",sr:"rs",ss:"za",st:"za",su:"id",sv:"se",sw:"tz",ta:"in",te:"in",tg:"tj",th:"th",ti:"et",tk:"tm",tl:"ph",tn:"za",to:"to",tr:"tr",ts:"za",tt:"ru",ty:"pf",ug:"cn",uk:"ua",ur:"pk",uz:"uz",ve:"za",vi:"vn",wa:"be",wo:"sn",xh:"za",yi:"il",yo:"ng",za:"cn",zh:"cn",zu:"za",ace:"id",ady:"ru",agq:"cm",alt:"ru",amo:"ng",asa:"tz",ast:"es",awa:"in",bal:"pk",ban:"id",bas:"cm",bax:"cm",bbc:"id",bem:"zm",bez:"tz",bfq:"in",bft:"pk",bfy:"in",bhb:"in",bho:"in",bik:"ph",bin:"ng",bjj:"in",bku:"ph",bqv:"ci",bra:"in",brx:"in",bss:"cm",btv:"pk",bua:"ru",buc:"yt",bug:"id",bya:"id",byn:"er",cch:"ng",ccp:"in",ceb:"ph",cgg:"ug",chk:"fm",chm:"ru",chp:"ca",chr:"us",cja:"kh",cjm:"vn",ckb:"iq",crk:"ca",csb:"pl",dar:"ru",dav:"ke",den:"ca",dgr:"ca",dje:"ne",doi:"in",dsb:"de",dua:"cm",dyo:"sn",dyu:"bf",ebu:"ke",efi:"ng",ewo:"cm",fan:"gq",fil:"ph",fon:"bj",fur:"it",gaa:"gh",gag:"md",gbm:"in",gcr:"gf",gez:"et",gil:"ki",gon:"in",gor:"id",grt:"in",gsw:"ch",guz:"ke",gwi:"ca",haw:"us",hil:"ph",hne:"in",hnn:"ph",hoc:"in",hoj:"in",ibb:"ng",ilo:"ph",inh:"ru",jgo:"cm",jmc:"tz",kaa:"uz",kab:"dz",kaj:"ng",kam:"ke",kbd:"ru",kcg:"ng",kde:"tz",kdt:"th",kea:"cv",ken:"cm",kfo:"ci",kfr:"in",kha:"in",khb:"cn",khq:"ml",kht:"in",kkj:"cm",kln:"ke",kmb:"ao",koi:"ru",kok:"in",kos:"fm",kpe:"lr",krc:"ru",kri:"sl",krl:"ru",kru:"in",ksb:"tz",ksf:"cm",ksh:"de",kum:"ru",lag:"tz",lah:"pk",lbe:"ru",lcp:"cn",lep:"in",lez:"ru",lif:"np",lis:"cn",lki:"ir",lmn:"in",lol:"cd",lua:"cd",luo:"ke",luy:"ke",lwl:"th",mad:"id",mag:"in",mai:"in",mak:"id",man:"gn",mas:"ke",mdf:"ru",mdh:"ph",mdr:"id",men:"sl",mer:"ke",mfe:"mu",mgh:"mz",mgo:"cm",min:"id",mni:"in",mnk:"gm",mnw:"mm",mos:"bf",mua:"cm",mwr:"in",myv:"ru",nap:"it",naq:"na",nds:"de","new":"np",niu:"nu",nmg:"cm",nnh:"cm",nod:"th",nso:"za",nus:"sd",nym:"tz",nyn:"ug",pag:"ph",pam:"ph",pap:"bq",pau:"pw",pon:"fm",prd:"ir",raj:"in",rcf:"re",rej:"id",rjs:"np",rkt:"in",rof:"tz",rwk:"tz",saf:"gh",sah:"ru",saq:"ke",sas:"id",sat:"in",saz:"in",sbp:"tz",scn:"it",sco:"gb",sdh:"ir",seh:"mz",ses:"ml",shi:"ma",shn:"mm",sid:"et",sma:"se",smj:"se",smn:"fi",sms:"fi",snk:"ml",srn:"sr",srr:"sn",ssy:"er",suk:"tz",sus:"gn",swb:"yt",swc:"cd",syl:"bd",syr:"sy",tbw:"ph",tcy:"in",tdd:"cn",tem:"sl",teo:"ug",tet:"tl",tig:"er",tiv:"ng",tkl:"tk",tmh:"ne",tpi:"pg",trv:"tw",tsg:"ph",tts:"th",tum:"mw",tvl:"tv",twq:"ne",tyv:"ru",tzm:"ma",udm:"ru",uli:"fm",umb:"ao",unr:"in",unx:"in",vai:"lr",vun:"tz",wae:"ch",wal:"et",war:"ph",xog:"ug",xsr:"np",yao:"mz",yap:"fm",yav:"cm",zza:"tr"}[tags[0]];
}else{
if(_19a.length==4){
_19a=tags[2];
}
}
return _19a;
};
_195.getWeekend=function(_19b){
var _19c={"in":0,af:4,dz:4,ir:4,om:4,sa:4,ye:4,ae:5,bh:5,eg:5,il:5,iq:5,jo:5,kw:5,ly:5,ma:5,qa:5,sd:5,sy:5,tn:5},_19d={af:5,dz:5,ir:5,om:5,sa:5,ye:5,ae:6,bh:5,eg:6,il:6,iq:6,jo:6,kw:6,ly:6,ma:6,qa:6,sd:6,sy:6,tn:6},_19e=_195._region(_19b),_19f=_19c[_19e],end=_19d[_19e];
if(_19f===undefined){
_19f=6;
}
if(end===undefined){
end=0;
}
return {start:_19f,end:end};
};
return _195;
});
},"curam/cdsl/_base/MetadataRegistry":function(){
define(["dojo/_base/declare","curam/cdsl/types/codetable/CodeTables","curam/cdsl/types/codetable/CodeTable","dojo/_base/lang"],function(_1a0,_1a1,_1a2,lang){
var _1a3=_1a0(null,{_callEntries:null,_codetables:null,constructor:function(){
this._callEntries={};
this._codetables={};
},setFlags:function(_1a4){
var _1a5=_1a4.intf()+"."+_1a4.method(),_1a6=!this._callEntries[_1a5];
_1a4._sendCodetables(_1a6);
},update:function(_1a7){
var _1a8=_1a7.request(),_1a9=_1a8.intf()+"."+_1a8.method(),_1aa=this._callEntries[_1a9];
if(!_1aa){
_1aa={};
this._callEntries[_1a9]=_1aa;
}
if(_1a7.hasCodetables()){
var data=_1a7.getCodetablesData();
for(var i=0;i<data.length;i++){
this._codetables[data[i].name]=new _1a2(data[i].name,data[i].defaultCode,data[i].codes);
}
}
},codetables:function(){
return this._codetables;
}});
return _1a3;
});
},"dojo/Stateful":function(){
define(["./_base/declare","./_base/lang","./_base/array","./when"],function(_1ab,lang,_1ac,when){
return _1ab("dojo.Stateful",null,{_attrPairNames:{},_getAttrNames:function(name){
var apn=this._attrPairNames;
if(apn[name]){
return apn[name];
}
return (apn[name]={s:"_"+name+"Setter",g:"_"+name+"Getter"});
},postscript:function(_1ad){
if(_1ad){
this.set(_1ad);
}
},_get:function(name,_1ae){
return typeof this[_1ae.g]==="function"?this[_1ae.g]():this[name];
},get:function(name){
return this._get(name,this._getAttrNames(name));
},set:function(name,_1af){
if(typeof name==="object"){
for(var x in name){
if(name.hasOwnProperty(x)&&x!="_watchCallbacks"){
this.set(x,name[x]);
}
}
return this;
}
var _1b0=this._getAttrNames(name),_1b1=this._get(name,_1b0),_1b2=this[_1b0.s],_1b3;
if(typeof _1b2==="function"){
_1b3=_1b2.apply(this,Array.prototype.slice.call(arguments,1));
}else{
this[name]=_1af;
}
if(this._watchCallbacks){
var self=this;
when(_1b3,function(){
self._watchCallbacks(name,_1b1,_1af);
});
}
return this;
},_changeAttrValue:function(name,_1b4){
var _1b5=this.get(name);
this[name]=_1b4;
if(this._watchCallbacks){
this._watchCallbacks(name,_1b5,_1b4);
}
return this;
},watch:function(name,_1b6){
var _1b7=this._watchCallbacks;
if(!_1b7){
var self=this;
_1b7=this._watchCallbacks=function(name,_1b8,_1b9,_1ba){
var _1bb=function(_1bc){
if(_1bc){
_1bc=_1bc.slice();
for(var i=0,l=_1bc.length;i<l;i++){
_1bc[i].call(self,name,_1b8,_1b9);
}
}
};
_1bb(_1b7["_"+name]);
if(!_1ba){
_1bb(_1b7["*"]);
}
};
}
if(!_1b6&&typeof name==="function"){
_1b6=name;
name="*";
}else{
name="_"+name;
}
var _1bd=_1b7[name];
if(typeof _1bd!=="object"){
_1bd=_1b7[name]=[];
}
_1bd.push(_1b6);
var _1be={};
_1be.unwatch=_1be.remove=function(){
var _1bf=_1ac.indexOf(_1bd,_1b6);
if(_1bf>-1){
_1bd.splice(_1bf,1);
}
};
return _1be;
}});
});
},"curam/cdsl/_base/_Connection":function(){
define(["dojo/_base/declare","./MetadataRegistry","./PreferenceMap"],function(_1c0,_1c1,_1c2){
var _1c3=_1c0(null,{_DEFAULT_REQUEST_TIMEOUT:60000,_metadata:null,_preferences:null,constructor:function(){
this._metadata=new _1c1();
this._preferences=new _1c2();
},invoke:function(_1c4,_1c5){
this._metadata.setFlags(_1c4);
},updateMetadata:function(_1c6){
this._metadata.update(_1c6);
},metadata:function(){
return this._metadata;
},preferences:function(){
return this._preferences;
}});
return _1c3;
});
},"curam/debug":function(){
define(["curam/define","curam/util/LocalConfig","dojo/ready","dojo/_base/lang","curam/util/ResourceBundle"],function(_1c7,_1c8,_1c9,lang,_1ca){
var _1cb=new _1ca("curam.application.Debug");
_1c7.singleton("curam.debug",{log:function(){
if(curam.debug.enabled()){
try{
var a=arguments;
if(!dojo.isIE){
console.log.apply(console,a);
}else{
var _1cc=a.length;
var sa=curam.debug._serializeArgument;
switch(_1cc){
case 1:
console.log(arguments[0]);
break;
case 2:
console.log(a[0],sa(a[1]));
break;
case 3:
console.log(a[0],sa(a[1]),sa(a[2]));
break;
case 4:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]));
break;
case 5:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]));
break;
case 6:
console.log(a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
break;
default:
console.log("[Incomplete message - "+(_1cc-5)+" message a truncated] "+a[0],sa(a[1]),sa(a[2]),sa(a[3]),sa(a[4]),sa(a[5]));
}
}
}
catch(e){
console.log(e);
}
}
},getProperty:function(key,_1cd){
return _1cb.getProperty(key,_1cd);
},_serializeArgument:function(arg){
if(typeof arg!="undefined"&&typeof arg.nodeType!="undefined"&&typeof arg.cloneNode!="undefined"){
return ""+arg;
}else{
if(curam.debug._isWindow(arg)){
return arg.location.href;
}else{
if(curam.debug._isArray(arg)&&curam.debug._isWindow(arg[0])){
return "[array of window objects, length "+arg.length+"]";
}else{
return dojo.toJson(arg);
}
}
}
},_isArray:function(arg){
return typeof arg!="undefined"&&(dojo.isArray(arg)||typeof arg.length!="undefined");
},_isWindow:function(arg){
var _1ce=typeof arg!="undefined"&&typeof arg.closed!="undefined"&&arg.closed;
if(_1ce){
return true;
}else{
return typeof arg!="undefined"&&typeof arg.location!="undefined"&&typeof arg.navigator!="undefined"&&typeof arg.document!="undefined"&&typeof arg.closed!="undefined";
}
},enabled:function(){
return _1c8.readOption("jsTraceLog","false")=="true";
},_setup:function(_1cf){
_1c8.seedOption("jsTraceLog",_1cf.trace,"false");
_1c8.seedOption("ajaxDebugMode",_1cf.ajaxDebug,"false");
_1c8.seedOption("asyncProgressMonitor",_1cf.asyncProgressMonitor,"false");
}});
return curam.debug;
});
},"curam/cdsl/store/CuramStore":function(){
define(["dojo/_base/declare","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","curam/cdsl/store/IdentityApi","dojo/store/util/QueryResults","dojo/_base/lang","curam/cdsl/_base/_Connection"],function(_1d0,_1d1,_1d2,_1d3,_1d4,_1d5,lang){
var _1d6={query:"listItems",get:"read",put:"modify",add:"insert",remove:"remove"};
var _1d7={identityApi:new _1d4(),dataAdapter:null};
var _1d8=function(_1d9){
var o=lang.clone(_1d7);
if(_1d9&&_1d9.getIdentity&&_1d9.parseIdentity&&_1d9.getIdentityPropertyNames){
o.identityApi=_1d9;
}else{
o=lang.mixin(o,_1d9);
}
return o;
};
var _1da=null;
var _1db=_1d0(_1da,{_service:null,_baseFacadeName:null,_identityApi:null,constructor:function(_1dc,_1dd,opts){
var _1de=_1d8(opts);
this._service=new _1d1(_1dc,{dataAdapter:_1de.dataAdapter});
this._baseFacadeName=_1dd;
this._identityApi=_1de.identityApi;
},get:function(_1df){
var _1e0=new _1d3(this._identityApi.parseIdentity(_1df));
var _1e1=new _1d2(this._baseFacadeName,_1d6.get,[_1e0]);
return this._service.call([_1e1]).then(function(data){
return data[0];
});
},getIdentity:function(_1e2){
return this._identityApi.getIdentity(_1e2);
},query:function(_1e3,_1e4){
var _1e5=new _1d2(this._baseFacadeName,_1d6.query,[new _1d3(_1e3)]);
if(_1e4){
_1e5._setMetadata({queryOptions:{offset:_1e4.start,count:_1e4.count,sort:_1e4.sort}});
}
var _1e6=this._service.call([_1e5]).then(function(data){
return data[0].dtls;
});
return new _1d5(_1e6);
},put:function(_1e7,_1e8){
if(_1e8&&typeof _1e8.overwrite!=="undefined"&&!_1e8.overwrite){
throw new Error("The overwrite option is set to false, but adding new items "+"via CuramStore.put() is not supported.");
}
return this._addOrPut(_1d6.put,_1e7,_1e8,"putOptions");
},add:function(_1e9,_1ea){
var opts={};
if(_1ea){
opts=lang.mixin(opts,_1ea);
}
opts.overwrite=false;
return this._addOrPut(_1d6.add,_1e9,opts,"addOptions");
},_addOrPut:function(_1eb,_1ec,_1ed,_1ee){
var _1ef=_1ec;
if(!_1ef.isInstanceOf||!_1ef.isInstanceOf(_1d3)){
_1ef=new _1d3(_1ec);
}
var _1f0=new _1d2(this._baseFacadeName,_1eb,[_1ef]);
if(_1ed){
var _1f1={};
_1f1[_1ee]={id:_1ed.id?_1ed.id:null,before:_1ed.before?this.getIdentity(_1ed.before):null,parent:_1ed.parent?this.getIdentity(_1ed.parent):null,overwrite:false};
_1f0._setMetadata(_1f1);
}
return this._service.call([_1f0]).then(lang.hitch(this,function(data){
if(_1ed&&_1ed.id){
return _1ed.id;
}
if(_1ed&&_1ed.overwrite){
return this.getIdentity(_1ef);
}
return this.getIdentity(data[0]);
}));
},remove:function(_1f2){
var _1f3=new _1d3(this._identityApi.parseIdentity(_1f2)),_1f4=new _1d2(this._baseFacadeName,_1d6.remove,[_1f3]);
return this._service.call([_1f4]).then(function(data){
return _1f2;
});
}});
return _1db;
});
},"curam/util/Constants":function(){
define(["curam/define"],function(){
curam.define.singleton("curam.util.Constants",{RETURN_PAGE_PARAM:"__o3rpu"});
return curam.util.Constants;
});
},"curam/cdsl/connection/SimpleAccess":function(){
define(["curam/cdsl/_base/_Connection","curam/cdsl/store/CuramStore","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","dojo/_base/lang","dojo/store/Observable","dojo/store/Cache","dojo/store/Memory"],function(_1f5,_1f6,_1f7,_1f8,_1f9,lang,_1fa,_1fb,_1fc){
var _1fd=null;
return {initConnection:function(_1fe){
if(_1fe==null){
throw new Error("The connection object should be provided.");
}else{
if(!(_1fe instanceof _1f5)){
throw new Error("The wrong type of the connection object is provided.");
}
}
if(_1fd==null){
_1fd=_1fe;
}
return _1fd;
},buildStore:function(_1ff,_200,_201,_202){
if(_1fd==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_1ff==null){
throw new Error("Facade class name is missing.");
}
if(_202==null){
_202=false;
}
if(_201==null){
_201=false;
}
var _203=new _1f6(_1fd,_1ff,_200);
if(_201){
_203=new _1fa(_203);
}
if(_202){
var _204=new _1fc();
_203=new _1fb(_203,_204);
}
return _203;
},makeRequest:function(_205,_206,_207){
if(_1fd==null){
throw new Error("The connection shoud be initialized first with initConnection() before using this API.");
}
if(_205==null){
throw new Error("Facade class name is missing.");
}
if(_206==null){
throw new Error("Facade method name is missing.");
}
var _208=new _1f7(_1fd);
var _209;
if(_207==null){
_209=[];
}else{
_209=[new _1f9(_207)];
}
var _20a=new _1f8(_205,_206,_209);
return _208.call([_20a]);
}};
});
},"curam/util/RuntimeContext":function(){
define(["dojo/_base/declare"],function(_20b){
var _20c=_20b("curam.util.RuntimeContext",null,{_window:null,constructor:function(_20d){
this._window=_20d;
},getHref:function(){
return this._window.location.href;
},getPathName:function(){
return this._window.location.pathName;
},contextObject:function(){
return this._window;
}});
return _20c;
});
},"dojo/NodeList-traverse":function(){
define(["./query","./_base/lang","./_base/array"],function(_20e,lang,_20f){
var _210=_20e.NodeList;
lang.extend(_210,{_buildArrayFromCallback:function(_211){
var ary=[];
for(var i=0;i<this.length;i++){
var _212=_211.call(this[i],this[i],ary);
if(_212){
ary=ary.concat(_212);
}
}
return ary;
},_getUniqueAsNodeList:function(_213){
var ary=[];
for(var i=0,node;node=_213[i];i++){
if(node.nodeType==1&&_20f.indexOf(ary,node)==-1){
ary.push(node);
}
}
return this._wrap(ary,null,this._NodeListCtor);
},_getUniqueNodeListWithParent:function(_214,_215){
var ary=this._getUniqueAsNodeList(_214);
ary=(_215?_20e._filterResult(ary,_215):ary);
return ary._stash(this);
},_getRelatedUniqueNodes:function(_216,_217){
return this._getUniqueNodeListWithParent(this._buildArrayFromCallback(_217),_216);
},children:function(_218){
return this._getRelatedUniqueNodes(_218,function(node,ary){
return lang._toArray(node.childNodes);
});
},closest:function(_219,root){
return this._getRelatedUniqueNodes(null,function(node,ary){
do{
if(_20e._filterResult([node],_219,root).length){
return node;
}
}while(node!=root&&(node=node.parentNode)&&node.nodeType==1);
return null;
});
},parent:function(_21a){
return this._getRelatedUniqueNodes(_21a,function(node,ary){
return node.parentNode;
});
},parents:function(_21b){
return this._getRelatedUniqueNodes(_21b,function(node,ary){
var pary=[];
while(node.parentNode){
node=node.parentNode;
pary.push(node);
}
return pary;
});
},siblings:function(_21c){
return this._getRelatedUniqueNodes(_21c,function(node,ary){
var pary=[];
var _21d=(node.parentNode&&node.parentNode.childNodes);
for(var i=0;i<_21d.length;i++){
if(_21d[i]!=node){
pary.push(_21d[i]);
}
}
return pary;
});
},next:function(_21e){
return this._getRelatedUniqueNodes(_21e,function(node,ary){
var next=node.nextSibling;
while(next&&next.nodeType!=1){
next=next.nextSibling;
}
return next;
});
},nextAll:function(_21f){
return this._getRelatedUniqueNodes(_21f,function(node,ary){
var pary=[];
var next=node;
while((next=next.nextSibling)){
if(next.nodeType==1){
pary.push(next);
}
}
return pary;
});
},prev:function(_220){
return this._getRelatedUniqueNodes(_220,function(node,ary){
var prev=node.previousSibling;
while(prev&&prev.nodeType!=1){
prev=prev.previousSibling;
}
return prev;
});
},prevAll:function(_221){
return this._getRelatedUniqueNodes(_221,function(node,ary){
var pary=[];
var prev=node;
while((prev=prev.previousSibling)){
if(prev.nodeType==1){
pary.push(prev);
}
}
return pary;
});
},andSelf:function(){
return this.concat(this._parent);
},first:function(){
return this._wrap(((this[0]&&[this[0]])||[]),this);
},last:function(){
return this._wrap((this.length?[this[this.length-1]]:[]),this);
},even:function(){
return this.filter(function(item,i){
return i%2!=0;
});
},odd:function(){
return this.filter(function(item,i){
return i%2==0;
});
}});
return _210;
});
},"dijit/registry":function(){
define(["dojo/_base/array","dojo/_base/window","./main"],function(_222,win,_223){
var _224={},hash={};
var _225={length:0,add:function(_226){
if(hash[_226.id]){
throw new Error("Tried to register widget with id=="+_226.id+" but that id is already registered");
}
hash[_226.id]=_226;
this.length++;
},remove:function(id){
if(hash[id]){
delete hash[id];
this.length--;
}
},byId:function(id){
return typeof id=="string"?hash[id]:id;
},byNode:function(node){
return hash[node.getAttribute("widgetId")];
},toArray:function(){
var ar=[];
for(var id in hash){
ar.push(hash[id]);
}
return ar;
},getUniqueId:function(_227){
var id;
do{
id=_227+"_"+(_227 in _224?++_224[_227]:_224[_227]=0);
}while(hash[id]);
return _223._scopeName=="dijit"?id:_223._scopeName+"_"+id;
},findWidgets:function(root,_228){
var _229=[];
function _22a(root){
for(var node=root.firstChild;node;node=node.nextSibling){
if(node.nodeType==1){
var _22b=node.getAttribute("widgetId");
if(_22b){
var _22c=hash[_22b];
if(_22c){
_229.push(_22c);
}
}else{
if(node!==_228){
_22a(node);
}
}
}
}
};
_22a(root);
return _229;
},_destroyAll:function(){
_223._curFocus=null;
_223._prevFocus=null;
_223._activeStack=[];
_222.forEach(_225.findWidgets(win.body()),function(_22d){
if(!_22d._destroyed){
if(_22d.destroyRecursive){
_22d.destroyRecursive();
}else{
if(_22d.destroy){
_22d.destroy();
}
}
}
});
},getEnclosingWidget:function(node){
while(node){
var id=node.nodeType==1&&node.getAttribute("widgetId");
if(id){
return hash[id];
}
node=node.parentNode;
}
return null;
},_hash:hash};
_223.registry=_225;
return _225;
});
},"dijit/_BidiMixin":function(){
define([],function(){
var _22e={LRM:"‎",LRE:"‪",PDF:"‬",RLM:"‏",RLE:"‫"};
return {textDir:"",getTextDir:function(text){
return this.textDir=="auto"?this._checkContextual(text):this.textDir;
},_checkContextual:function(text){
var fdc=/[A-Za-z\u05d0-\u065f\u066a-\u06ef\u06fa-\u07ff\ufb1d-\ufdff\ufe70-\ufefc]/.exec(text);
return fdc?(fdc[0]<="z"?"ltr":"rtl"):this.dir?this.dir:this.isLeftToRight()?"ltr":"rtl";
},applyTextDir:function(_22f,text){
if(this.textDir){
var _230=this.textDir;
if(_230=="auto"){
if(typeof text==="undefined"){
var _231=_22f.tagName.toLowerCase();
text=(_231=="input"||_231=="textarea")?_22f.value:_22f.innerText||_22f.textContent||"";
}
_230=this._checkContextual(text);
}
if(_22f.dir!=_230){
_22f.dir=_230;
}
}
},enforceTextDirWithUcc:function(_232,text){
if(this.textDir){
if(_232){
_232.originalText=text;
}
var dir=this.textDir=="auto"?this._checkContextual(text):this.textDir;
return (dir=="ltr"?_22e.LRE:_22e.RLE)+text+_22e.PDF;
}
return text;
},restoreOriginalText:function(_233){
if(_233.originalText){
_233.text=_233.originalText;
delete _233.originalText;
}
return _233;
},_setTextDirAttr:function(_234){
if(!this._created||this.textDir!=_234){
this._set("textDir",_234);
var node=null;
if(this.displayNode){
node=this.displayNode;
this.displayNode.align=this.dir=="rtl"?"right":"left";
}else{
node=this.textDirNode||this.focusNode||this.textbox;
}
if(node){
this.applyTextDir(node);
}
}
}};
});
},"dijit/_BidiSupport":function(){
define(["dojo/has","./_WidgetBase","./_BidiMixin"],function(has,_235,_236){
_235.extend(_236);
has.add("dojo-bidi",true);
return _235;
});
},"curam/cdsl/_base/PreferenceMap":function(){
define(["dojo/_base/declare","dojo/_base/lang"],function(_237,lang){
var _238=_237(null,{_preferences:null,_preferenceNames:null,constructor:function(){
this._preferences={};
this._preferenceNames=[];
},getPreference:function(name){
return this._preferences[name];
},getPreferenceNames:function(){
return this._preferenceNames;
},addPreference:function(name,_239){
this._preferences[name]=_239;
this._preferenceNames[this._preferenceNames.length]=name;
}});
return _238;
});
},"dojo/store/Cache":function(){
define(["../_base/lang","../when"],function(lang,when){
var _23a=function(_23b,_23c,_23d){
_23d=_23d||{};
return lang.delegate(_23b,{query:function(_23e,_23f){
var _240=_23b.query(_23e,_23f);
_240.forEach(function(_241){
if(!_23d.isLoaded||_23d.isLoaded(_241)){
_23c.put(_241);
}
});
return _240;
},queryEngine:_23b.queryEngine||_23c.queryEngine,get:function(id,_242){
return when(_23c.get(id),function(_243){
return _243||when(_23b.get(id,_242),function(_244){
if(_244){
_23c.put(_244,{id:id});
}
return _244;
});
});
},add:function(_245,_246){
return when(_23b.add(_245,_246),function(_247){
_23c.add(_247&&typeof _247=="object"?_247:_245,_246);
return _247;
});
},put:function(_248,_249){
_23c.remove((_249&&_249.id)||this.getIdentity(_248));
return when(_23b.put(_248,_249),function(_24a){
_23c.put(_24a&&typeof _24a=="object"?_24a:_248,_249);
return _24a;
});
},remove:function(id,_24b){
return when(_23b.remove(id,_24b),function(_24c){
return _23c.remove(id,_24b);
});
},evict:function(id){
return _23c.remove(id);
}});
};
lang.setObject("dojo.store.Cache",_23a);
return _23a;
});
},"curam/cdsl/store/IdentityApi":function(){
define(["dojo/_base/declare","dojo/json"],function(_24d,json){
var _24e=_24d(null,{getIdentity:function(item){
var _24f=this.getIdentityPropertyNames()[0];
if(typeof item[_24f]==="object"){
throw new Error("Complex identity attributes are not supported by this implementation.");
}
return item[_24f];
},parseIdentity:function(_250){
var _251={};
_251[this.getIdentityPropertyNames()[0]]=_250;
return _251;
},getIdentityPropertyNames:function(){
return ["id"];
}});
return _24e;
});
},"curam/cdsl/types/codetable/CodeTables":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/Deferred","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct"],function(_252,lang,_253,_254,_255){
var _256=_252(null,{_connection:null,constructor:function(_257){
if(!_257){
throw new Error("Missing parameter.");
}
if(typeof _257!=="object"){
throw new Error("Wrong parameter type: "+typeof _257);
}
this._connection=_257;
},getCodeTable:function(name){
return this._connection.metadata().codetables()[name];
},loadForFacades:function(_258){
var _259=new _253();
require(["curam/cdsl/request/CuramService"],lang.hitch(this,function(_25a){
var _25b=new _25a(this._connection),_25c=new _254("CuramService","getCodetables",this._getInputStructsForLoadingCodetables(_258));
_25b.call([_25c]).then(lang.hitch(this,function(data){
_259.resolve(this);
}),function(err){
_259.reject(err);
});
}));
return _259;
},_getInputStructsForLoadingCodetables:function(_25d){
var ret=[];
for(var i=0;i<_25d.length;i++){
ret.push(new _255({service:_25d[i].intf(),method:_25d[i].method()}));
}
return ret;
}});
return _256;
});
},"curam/cdsl/types/FrequencyPattern":function(){
define(["dojo/_base/declare","dojo/_base/lang"],function(_25e){
var _25f=_25e(null,{_code:null,_description:null,constructor:function(code,_260){
this._code=code;
this._description=_260;
},getCode:function(){
return this._code;
},getDescription:function(){
return this._description;
}});
return _25f;
});
},"curam/util/LocalConfig":function(){
define([],function(){
var _261=function(name){
return "curam_util_LocalConfig_"+name;
},_262=function(name,_263){
var _264=_261(name);
if(typeof top[_264]==="undefined"){
top[_264]=_263;
}
return top[_264];
},_265=function(name){
return top&&top!=null?top[_261(name)]:undefined;
};
_262("seedValues",{}),_262("overrides",{});
var _266=function(_267,_268){
if(typeof _267!=="undefined"&&typeof _267!=="string"){
throw new Error("Invalid "+_268+" type: "+typeof _267+"; expected string");
}
};
var _269={seedOption:function(name,_26a,_26b){
_266(_26a,"value");
_266(_26b,"defaultValue");
_265("seedValues")[name]=(typeof _26a!=="undefined")?_26a:_26b;
},overrideOption:function(name,_26c){
_266(_26c,"value");
if(typeof (Storage)!=="undefined"){
localStorage[name]=_26c;
}else{
_265("overrides")[name]=_26c;
}
},readOption:function(name,_26d){
var _26e=_265("seedValues");
var _26f=_265("overrides");
_266(_26d,"defaultValue");
var _270=null;
if(typeof (Storage)!=="undefined"&&localStorage&&typeof localStorage[name]!=="undefined"){
_270=localStorage[name];
}else{
if(_26f&&typeof _26f[name]!=="undefined"){
_270=_26f[name];
}else{
if(_26e&&typeof _26e[name]!=="undefined"){
_270=_26e[name];
}else{
_270=_26d;
}
}
}
return _270;
},clearOption:function(name){
if(typeof (Storage)!=="undefined"){
localStorage.removeItem(name);
}
delete _265("overrides")[name];
delete _265("seedValues")[name];
}};
return _269;
});
},"curam/cdsl/_base/FacadeMethodCall":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/json","dojo/_base/array"],function(_271,lang,json,_272){
var _273=_271(null,{_intf:null,_method:null,_structs:null,_metadata:null,_options:null,constructor:function(intf,_274,_275,_276){
if(_275&&!lang.isArray(_275)){
throw new Error("Unexpected type of the 'structs' argument.");
}
this._intf=intf;
this._method=_274;
this._structs=_275?_275:[];
this._options={};
lang.mixin(this._options,{raw:true,formatted:false,sendCodetables:true,dataAdapter:null},_276);
},url:function(base){
return base+"/"+this._intf+"/"+this._method;
},_setMetadata:function(_277){
this._metadata=_277;
},toJson:function(){
var data={service:this._intf,method:this._method,data:_272.map(this._structs,lang.hitch(this,function(item){
item.setDataAdapter(this._options.dataAdapter);
return item.getData();
})),configOptions:{"response-type":this._responseType(),"send-codetables":this._sendCodetables()}};
if(this._metadata&&this._metadata.queryOptions){
data.queryOptions=this._metadata.queryOptions;
}
return json.stringify(data);
},formatted:function(_278){
return this._getOrSet(_278,this._options,"formatted");
},raw:function(_279){
return this._getOrSet(_279,this._options,"raw");
},_responseType:function(){
if(this.raw()&&this.formatted()){
return "both";
}else{
if(this.raw()){
return "raw";
}else{
if(this.formatted()){
return "formatted";
}
}
}
throw new Error("Invalid response type: neither raw nor formatted was requested.");
},_sendCodetables:function(_27a){
return this._getOrSet(_27a,this._options,"sendCodetables");
},_getOrSet:function(_27b,_27c,_27d){
if(typeof _27b==="undefined"){
return _27c[_27d];
}else{
var _27e=_27c[_27d];
_27c[_27d]=_27b;
return _27e;
}
},intf:function(){
return this._intf;
},method:function(){
return this._method;
},dataAdapter:function(_27f){
if(!_27f){
return this._options.dataAdapter;
}
this._options.dataAdapter=_27f;
}});
return _273;
});
},"cm/_base/_dom":function(){
define(["dojo/dom","dojo/dom-style","dojo/dom-class"],function(dom,_280,_281){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{nextSibling:function(node,_282){
return cm._findSibling(node,_282,true);
},prevSibling:function(node,_283){
return cm._findSibling(node,_283,false);
},getInput:function(name,_284){
if(!dojo.isString(name)){
return name;
}
var _285=dojo.query("input[name='"+name+"'],select[name='"+name+"']");
return _284?(_285.length>0?_285:null):(_285.length>0?_285[0]:null);
},getParentByClass:function(node,_286){
node=node.parentNode;
while(node){
if(_281.contains(node,_286)){
return node;
}
node=node.parentNode;
}
return null;
},getParentByType:function(node,type){
node=node.parentNode;
type=type.toLowerCase();
var _287="html";
while(node){
if(node.tagName.toLowerCase()==_287){
break;
}
if(node.tagName.toLowerCase()==type){
return node;
}
node=node.parentNode;
}
return null;
},replaceClass:function(node,_288,_289){
_281.remove(node,_289);
_281.add(node,_288);
},setClass:function(node,_28a){
node=dom.byId(node);
var cs=new String(_28a);
try{
if(typeof node.className=="string"){
node.className=cs;
}else{
if(node.setAttribute){
node.setAttribute("class",_28a);
node.className=cs;
}else{
return false;
}
}
}
catch(e){
dojo.debug("dojo.html.setClass() failed",e);
}
return true;
},_findSibling:function(node,_28b,_28c){
if(!node){
return null;
}
if(_28b){
_28b=_28b.toLowerCase();
}
var _28d=_28c?"nextSibling":"previousSibling";
do{
node=node[_28d];
}while(node&&node.nodeType!=1);
if(node&&_28b&&_28b!=node.tagName.toLowerCase()){
return cm[_28c?"nextSibling":"prevSibling"](node,_28b);
}
return node;
},getViewport:function(){
var d=dojo.doc,dd=d.documentElement,w=window,b=dojo.body();
if(dojo.isMozilla){
return {w:dd.clientWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&w.innerWidth){
return {w:w.innerWidth,h:w.innerHeight};
}else{
if(!dojo.isOpera&&dd&&dd.clientWidth){
return {w:dd.clientWidth,h:dd.clientHeight};
}else{
if(b.clientWidth){
return {w:b.clientWidth,h:b.clientHeight};
}
}
}
}
return null;
},toggleDisplay:function(node){
_280.set(node,"display",_280.get(node,"display")=="none"?"":"none");
},endsWith:function(str,end,_28e){
if(_28e){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
},hide:function(n){
_280.set(n,"display","none");
},show:function(n){
_280.set(n,"display","");
}});
return cm;
});
},"curam/cdsl/types/codetable/CodeTable":function(){
define(["dojo/_base/declare","./CodeTableItem"],function(_28f,_290){
var _291=_28f(null,{_name:null,_defaultCode:null,_codes:null,_items:null,constructor:function(name,_292,_293){
this._name=name;
this._defaultCode=_292;
this._codes=this._parseCodesIntoObject(_293);
},listItems:function(){
this._initItems(this._codes,this._defaultCode);
var ret=[];
for(code in this._items){
ret.push(this._items[code]);
}
return ret;
},_parseCodesIntoObject:function(_294){
var ret={};
if(_294){
for(var i=0;i<_294.length;i++){
var raw=_294[i];
var code=raw.split(":")[0];
var desc=raw.slice(code.length+1);
ret[code]=desc;
}
}
return ret;
},_initItems:function(_295,_296){
if(!this._items){
this._items={};
for(code in _295){
var cti=new _290(code,_295[code]);
cti.isDefault(code===_296);
this._items[code]=cti;
}
}
},getItem:function(code){
this._initItems(this._codes,this._defaultCode);
return this._items[code];
}});
return _291;
});
},"curam/cdsl/connection/CuramConnection":function(){
define(["dojo/_base/declare","dojo/request/registry","curam/cdsl/_base/_Connection","curam/util"],function(_297,_298,_299,util){
var _29a=_297(_299,{_baseUrl:null,constructor:function(_29b){
this._baseUrl=_29b;
},invoke:function(_29c,_29d){
this.inherited(arguments);
var _29e=util.getTopmostWindow();
return _298(_29c.url(this._baseUrl),{data:_29c.toJson(),method:"POST",headers:{"Content-Encoding":"UTF-8","csrfToken":_29e.csrfToken},query:null,preventCache:true,timeout:_29d?_29d:this._DEFAULT_REQUEST_TIMEOUT,handleAs:"text"});
}});
return _29a;
});
},"dojo/store/util/QueryResults":function(){
define(["../../_base/array","../../_base/lang","../../when"],function(_29f,lang,when){
var _2a0=function(_2a1){
if(!_2a1){
return _2a1;
}
var _2a2=!!_2a1.then;
if(_2a2){
_2a1=lang.delegate(_2a1);
}
function _2a3(_2a4){
_2a1[_2a4]=function(){
var args=arguments;
var _2a5=when(_2a1,function(_2a6){
Array.prototype.unshift.call(args,_2a6);
return _2a0(_29f[_2a4].apply(_29f,args));
});
if(_2a4!=="forEach"||_2a2){
return _2a5;
}
};
};
_2a3("forEach");
_2a3("filter");
_2a3("map");
if(_2a1.total==null){
_2a1.total=when(_2a1,function(_2a7){
return _2a7.length;
});
}
return _2a1;
};
lang.setObject("dojo.store.util.QueryResults",_2a0);
return _2a0;
});
},"dijit/main":function(){
define(["dojo/_base/kernel"],function(dojo){
return dojo.dijit;
});
},"curam/util":function(){
define(["dojo/dom","dijit/registry","dojo/dom-construct","dojo/ready","dojo/_base/window","dojo/dom-style","dojo/_base/array","dojo/dom-class","dojo/topic","dojo/_base/event","dojo/query","dojo/Deferred","dojo/has","dojo/_base/unload","dojo/dom-geometry","dojo/_base/json","dojo/dom-attr","dojo/_base/lang","dojo/on","dijit/_BidiSupport","curam/define","curam/debug","curam/util/RuntimeContext","curam/util/Constants","dojo/_base/sniff","cm/_base/_dom","curam/util/ResourceBundle","dojo/NodeList-traverse"],function(dom,_2a8,_2a9,_2aa,_2ab,_2ac,_2ad,_2ae,_2af,_2b0,_2b1,_2b2,has,_2b3,geom,json,attr,lang,on,bidi,_2b4,_2b5,_2b6,_2b7,_2b8,_2b9,_2ba){
curam.define.singleton("curam.util",{PREVENT_CACHE_FLAG:"o3pc",INFORMATIONAL_MSGS_STORAGE_ID:"__informationals__",ERROR_MESSAGES_CONTAINER:"error-messages-container",ERROR_MESSAGES_LIST:"error-messages",CACHE_BUSTER:0,CACHE_BUSTER_PARAM_NAME:"o3nocache",PAGE_ID_PREFIX:"Curam_",msgLocaleSelectorActionPage:"$not-locaized$ Usage of the Language Selector is not permitted from an editable page that has previously been submitted.",GENERIC_ERROR_MODAL_MAP:{},wrappersMap:[],lastOpenedTabButton:false,tabButtonClicked:false,secureURLsExemptParamName:"suep",secureURLsExemptParamsPrefix:"spm",secureURLsHashTokenParam:"suhtp",setTabButtonClicked:function(_2bb){
curam.util.getTopmostWindow().curam.util.tabButtonClicked=_2bb;
},getTabButtonClicked:function(){
var _2bc=curam.util.getTopmostWindow().curam.util.tabButtonClicked;
curam.util.getTopmostWindow().curam.util.tabButtonClicked=false;
return _2bc;
},setLastOpenedTabButton:function(_2bd){
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=_2bd;
},getLastOpenedTabButton:function(){
var _2be=curam.util.getTopmostWindow().curam.util.lastOpenedTabButton;
curam.util.getTopmostWindow().curam.util.lastOpenedTabButton=false;
return _2be;
},insertCssText:function(_2bf,_2c0){
var id=_2c0?_2c0:"_runtime_stylesheet_";
var _2c1=dom.byId(id);
var _2c2;
if(_2c1){
if(_2c1.styleSheet){
_2bf=_2c1.styleSheet.cssText+_2bf;
_2c2=_2c1;
_2c2.setAttribute("id","_nodeToRm");
}else{
_2c1.appendChild(document.createTextNode(_2bf));
return;
}
}
var pa=document.getElementsByTagName("head")[0];
_2c1=_2a9.create("style",{type:"text/css",id:id});
if(_2c1.styleSheet){
_2c1.styleSheet.cssText=_2bf;
}else{
_2c1.appendChild(document.createTextNode(_2bf));
}
pa.appendChild(_2c1);
if(_2c2){
_2c2.parentNode.removeChild(_2c2);
}
},fireRefreshTreeEvent:function(){
if(dojo.global.parent&&dojo.global.parent.amIFrame){
var wpl=dojo.global.parent.loader;
}
if(wpl&&wpl.dojo){
wpl.dojo.publish("refreshTree");
}
},firePageSubmittedEvent:function(_2c3){
require(["curam/tab"],function(){
var _2c4=curam.tab.getSelectedTab();
if(_2c4){
var _2c5=curam.tab.getTabWidgetId(_2c4);
var _2c6=curam.util.getTopmostWindow();
var ctx=(_2c3=="dialog")?curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_DIALOG:curam.util.ui.refresh.RefreshEvent.prototype.SOURCE_CONTEXT_MAIN;
_2c6.curam.util.Refresh.getController(_2c5).pageSubmitted(dojo.global.jsPageID,ctx);
_2c6.dojo.publish("/curam/main-content/page/submitted",[dojo.global.jsPageID,_2c5]);
}else{
curam.debug.log("/curam/main-content/page/submitted: "+_2b5.getProperty("curam.util.no.open"));
}
});
},fireTabOpenedEvent:function(_2c7){
curam.util.getTopmostWindow().dojo.publish("curam.tabOpened",[dojo.global.jsPageID,_2c7]);
},setupSubmitEventPublisher:function(){
_2aa(function(){
var form=dom.byId("mainForm");
if(form){
curam.util.connect(form,"onsubmit",function(){
curam.util.getTopmostWindow().dojo.publish("/curam/progress/display",[curam.util.PAGE_ID_PREFIX+dojo.global.jsPageID]);
curam.util.firePageSubmittedEvent("main-content");
});
}
});
},getScrollbar:function(){
var _2c8=_2a9.create("div",{},_2ab.body());
_2ac.set(_2c8,{width:"100px",height:"100px",overflow:"scroll",position:"absolute",top:"-300px",left:"0px"});
var test=_2a9.create("div",{},_2c8);
_2ac.set(test,{width:"400px",height:"400px"});
var _2c9=_2c8.offsetWidth-_2c8.clientWidth;
_2a9.destroy(_2c8);
return {width:_2c9};
},isModalWindow:function(){
return (dojo.global.curamModal===undefined)?false:true;
},isExitingIEGScriptInModalWindow:function(_2ca){
return (curam.util.getTopmostWindow().exitingIEGScript===undefined)?false:true;
},setExitingIEGScriptInModalWindowVariable:function(){
curam.util.getTopmostWindow().exitingIEGScript=true;
},getTopmostWindow:function(){
if(typeof (dojo.global._curamTopmostWindow)=="undefined"){
var _2cb=dojo.global;
if(_2cb.__extAppTopWin){
dojo.global._curamTopmostWindow=_2cb;
}else{
while(_2cb.parent!=_2cb){
_2cb=_2cb.parent;
if(_2cb.__extAppTopWin){
break;
}
}
dojo.global._curamTopmostWindow=_2cb;
}
}
if(dojo.global._curamTopmostWindow.location.href.indexOf("AppController.do")<0&&typeof (dojo.global._curamTopmostWindow.__extAppTopWin)=="undefined"){
curam.debug.log(_2b5.getProperty("curam.util.wrong.window")+dojo.global._curamTopmostWindow.location.href);
}
return dojo.global._curamTopmostWindow;
},getUrlParamValue:function(url,_2cc){
var qPos=url.indexOf("?");
if(qPos<0){
return null;
}
var _2cd=url.substring(qPos+1,url.length);
function _2ce(_2cf){
var _2d0=_2cd.split(_2cf);
_2cc+="=";
for(var i=0;i<_2d0.length;i++){
if(_2d0[i].indexOf(_2cc)==0){
return _2d0[i].split("=")[1];
}
}
};
return _2ce("&")||_2ce("");
},addUrlParam:function(href,_2d1,_2d2,_2d3){
var hasQ=href.indexOf("?")>-1;
var _2d4=_2d3?_2d3:"undefined";
if(!hasQ||(_2d4==false)){
return href+(hasQ?"&":"?")+_2d1+"="+_2d2;
}else{
var _2d5=href.split("?");
href=_2d5[0]+"?"+_2d1+"="+_2d2+(_2d5[1]!=""?("&"+_2d5[1]):"");
return href;
}
},replaceUrlParam:function(href,_2d6,_2d7){
href=curam.util.removeUrlParam(href,_2d6);
return curam.util.addUrlParam(href,_2d6,_2d7);
},removeUrlParam:function(url,_2d8,_2d9){
var qPos=url.indexOf("?");
if(qPos<0){
return url;
}
if(url.indexOf(_2d8+"=")<0){
return url;
}
var _2da=url.substring(qPos+1,url.length);
var _2db=_2da.split("&");
var _2dc;
var _2dd,_2de;
for(var i=0;i<_2db.length;i++){
if(_2db[i].indexOf(_2d8+"=")==0){
_2de=false;
if(_2d9){
_2dd=_2db[i].split("=");
if(_2dd.length>1){
if(_2dd[1]==_2d9){
_2de=true;
}
}else{
if(_2d9==""){
_2de=true;
}
}
}else{
_2de=true;
}
if(_2de){
_2db.splice(i,1);
i--;
}
}
}
return url.substring(0,qPos+1)+_2db.join("&");
},stripHash:function(url){
var idx=url.indexOf("#");
if(idx<0){
return url;
}
return url.substring(0,url);
},isSameUrl:function(_2df,_2e0,rtc){
if(!_2e0){
_2e0=rtc.getHref();
}
if(_2df.indexOf("#")==0){
return true;
}
var _2e1=_2df.indexOf("#");
if(_2e1>-1){
if(_2e1==0){
return true;
}
var _2e2=_2df.split("#");
var _2e3=_2e0.indexOf("#");
if(_2e3>-1){
if(_2e3==0){
return true;
}
_2e0=_2e0.split("#")[0];
}
return _2e2[0]==_2e0;
}
var _2e4=function(url){
var idx=url.lastIndexOf("Page.do");
var len=7;
if(idx<0){
idx=url.lastIndexOf("Action.do");
len=9;
}
if(idx<0){
idx=url.lastIndexOf("Frame.do");
len=8;
}
if(idx>-1&&idx==url.length-len){
return url.substring(0,idx);
}
return url;
};
var rp=curam.util.removeUrlParam;
var here=curam.util.stripHash(rp(_2e0,curam.util.Constants.RETURN_PAGE_PARAM));
var _2e5=curam.util.stripHash(rp(_2df,curam.util.Constants.RETURN_PAGE_PARAM));
var _2e6=_2e5.split("?");
var _2e7=here.split("?");
_2e7[0]=_2e4(_2e7[0]);
_2e6[0]=_2e4(_2e6[0]);
var _2e8=(_2e7[0]==_2e6[0]||_2e7[0].match(_2e6[0]+"$")==_2e6[0]);
if(!_2e8){
return false;
}
if(_2e7.length==1&&_2e6.length==1&&_2e8){
return true;
}else{
var _2e9;
var _2ea;
if(typeof _2e7[1]!="undefined"&&_2e7[1]!=""){
_2e9=_2e7[1].split("&");
}else{
_2e9=new Array();
}
if(typeof _2e6[1]!="undefined"&&_2e6[1]!=""){
_2ea=_2e6[1].split("&");
}else{
_2ea=new Array();
}
curam.debug.log("curam.util.isSameUrl: paramsHere "+_2b5.getProperty("curam.util.before")+_2e9.length);
_2e9=_2ad.filter(_2e9,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_2b5.getProperty("curam.util.after")+_2e9.length);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_2b5.getProperty("curam.util.before")+_2ea.length);
_2ea=_2ad.filter(_2ea,curam.util.isNotCDEJParam);
curam.debug.log("curam.util.isSameUrl: paramsHere "+_2b5.getProperty("curam.util.after")+_2ea.length);
if(_2e9.length!=_2ea.length){
return false;
}
var _2eb={};
var _2ec;
for(var i=0;i<_2e9.length;i++){
_2ec=_2e9[i].split("=");
_2ec[0]=decodeURIComponent(_2ec[0]);
_2ec[1]=decodeURIComponent(_2ec[1]);
_2eb[_2ec[0]]=_2ec[1];
}
for(var i=0;i<_2ea.length;i++){
_2ec=_2ea[i].split("=");
_2ec[0]=decodeURIComponent(_2ec[0]);
_2ec[1]=decodeURIComponent(_2ec[1]);
if(_2eb[_2ec[0]]!=_2ec[1]){
curam.debug.log(_2b5.getProperty("curam.util.no.match",[_2ec[0],_2ec[1],_2eb[_2ec[0]]]));
return false;
}
}
}
return true;
},isNotCDEJParam:function(_2ed){
return !((_2ed.charAt(0)=="o"&&_2ed.charAt(1)=="3")||(_2ed.charAt(0)=="_"&&_2ed.charAt(1)=="_"&&_2ed.charAt(2)=="o"&&_2ed.charAt(3)=="3"));
},setAttributes:function(node,map){
for(var x in map){
node.setAttribute(x,map[x]);
}
},invalidatePage:function(){
curam.PAGE_INVALIDATED=true;
var _2ee=dojo.global.dialogArguments?dojo.global.dialogArguments[0]:opener;
if(_2ee&&_2ee!=dojo.global){
try{
_2ee.curam.util.invalidatePage();
}
catch(e){
curam.debug.log(_2b5.getProperty("curam.util.error"),e);
}
}
},redirectWindow:function(href,_2ef,_2f0){
var rtc=new curam.util.RuntimeContext(dojo.global);
var _2f1=function(_2f2,_2f3,href,_2f4,_2f5){
curam.util.getFrameRoot(_2f2,_2f3).curam.util.redirectContentPanel(href,_2f4,_2f5);
};
curam.util._doRedirectWindow(href,_2ef,_2f0,dojo.global.jsScreenContext,rtc,curam.util.publishRefreshEvent,_2f1);
},_doRedirectWindow:function(href,_2f6,_2f7,_2f8,rtc,_2f9,_2fa){
if(href&&curam.util.isActionPage(href)&&!curam.util.LOCALE_REFRESH){
curam.debug.log(_2b5.getProperty("curam.util.stopping"),href);
return;
}
var rpl=curam.util.replaceUrlParam;
var _2fb=_2f8.hasContextBits("TREE")||_2f8.hasContextBits("AGENDA")||_2f8.hasContextBits("ORG_TREE");
if(curam.util.LOCALE_REFRESH){
curam.util.publishRefreshEvent();
curam.util.getTopmostWindow().location.reload();
return;
}else{
if(curam.util.FORCE_REFRESH){
href=rpl(rtc.getHref(),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(curam.util.isModalWindow()||_2fb){
_2f9();
dojo.global.location.href=href;
}else{
if(_2f8.hasContextBits("LIST_ROW_INLINE_PAGE")||_2f8.hasContextBits("NESTED_UIM")){
curam.util._handleInlinePageRefresh(href);
}else{
_2f9();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
require(["curam/tab"],function(){
_2fa(dojo.global,curam.tab.getTabController().ROOT_OBJ,href,true,true);
});
}
}
}
return;
}
}
var u=curam.util;
var rtc=new curam.util.RuntimeContext(dojo.global);
if(!_2fb&&!_2f6&&!curam.PAGE_INVALIDATED&&u.isSameUrl(href,null,rtc)){
return;
}
if(curam.util.isModalWindow()||_2fb){
href=rpl(rpl(href,"o3frame","modal"),curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
var form=_2a9.create("form",{action:href,method:"POST"});
if(!_2fb){
if(!dom.byId("o3ctx")){
form.action=curam.util.removeUrlParam(form.action,"o3ctx");
var _2fc=_2a9.create("input",{type:"hidden",id:"o3ctx",name:"o3ctx",value:_2f8.getValue()},form);
}
_2ab.body().appendChild(form);
_2f9();
form.submit();
}
if(!_2f7){
if(_2fb){
curam.util.redirectFrame(href);
}
}
}else{
var _2fd=sessionStorage.getItem("launchWordEdit");
if(!_2fd&&(_2f8.hasContextBits("LIST_ROW_INLINE_PAGE")||_2f8.hasContextBits("NESTED_UIM"))){
curam.util._handleInlinePageRefresh(href);
}else{
if(_2fd){
sessionStorage.removeItem("launchWordEdit");
}
_2f9();
if(dojo.global.location!==curam.util.getTopmostWindow().location){
if(_2f8.hasContextBits("EXTAPP")){
var _2fe=window.top;
_2fe.dijit.byId("curam-app").updateMainContentIframe(href);
}else{
require(["curam/tab"],function(){
curam.util.getFrameRoot(dojo.global,curam.tab.getTabController().ROOT_OBJ).curam.util.redirectContentPanel(href,_2f6);
});
}
}
}
}
},_handleInlinePageRefresh:function(href){
curam.debug.log(_2b5.getProperty("curam.util.closing.modal"),href);
var _2ff=new curam.ui.PageRequest(href);
require(["curam/tab"],function(){
curam.tab.getTabController().checkPage(_2ff,function(_300){
curam.util.publishRefreshEvent();
window.location.reload(false);
});
});
},redirectContentPanel:function(url,_301,_302){
require(["curam/tab"],function(){
var _303=curam.tab.getContentPanelIframe();
var _304=url;
if(_303!=null){
var rpu=curam.util.Constants.RETURN_PAGE_PARAM;
var _305=null;
if(url.indexOf(rpu+"=")>=0){
curam.debug.log("curam.util.redirectContentPanel: "+_2b5.getProperty("curam.util.rpu"));
_305=decodeURIComponent(curam.util.getUrlParamValue(url,rpu));
}
if(_305){
_305=curam.util.removeUrlParam(_305,rpu);
_304=curam.util.replaceUrlParam(url,rpu,encodeURIComponent(_305));
}
}
var _306=new curam.ui.PageRequest(_304);
if(_301){
_306.forceLoad=true;
}
if(_302){
_306.justRefresh=true;
}
curam.tab.getTabController().handlePageRequest(_306);
});
},redirectFrame:function(href){
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
var _307=curam.util.getFrameRoot(dojo.global,"wizard").targetframe;
_307.curam.util.publishRefreshEvent();
_307.location.href=href;
}else{
if(dojo.global.jsScreenContext.hasContextBits("ORG_TREE")){
var _307=curam.util.getFrameRoot(dojo.global,"orgTreeRoot");
_307.curam.util.publishRefreshEvent();
_307.dojo.publish("orgTree.refreshContent",[href]);
}else{
var _308=curam.util.getFrameRoot(dojo.global,"iegtree");
var _309=_308.navframe||_308.frames[0];
var _30a=_308.contentframe||_308.frames["contentframe"];
_30a.curam.util.publishRefreshEvent();
if(curam.PAGE_INVALIDATED||_309.curam.PAGE_INVALIDATED){
var _30b=curam.util.modifyUrlContext(href,"ACTION");
_30a.location.href=_30b;
}else{
_30a.location.href=href;
}
}
}
return true;
},publishRefreshEvent:function(){
_2af.publish("/curam/page/refresh");
},openGenericErrorModalDialog:function(_30c,_30d,_30e,_30f,_310){
var _311=curam.util.getTopmostWindow();
_311.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_30c,"titleInfo":_30d,"msg":_30e,"msgPlaceholder":_30f,"errorModal":_310,"hasCancelButton":false};
var url="generic-modal-error.jspx";
curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton=true;
curam.util.openModalDialog({href:encodeURI(url)},_30c);
},openGenericErrorModalDialogYesNo:function(_312,_313,_314){
var sc=dojo.global.jsScreenContext;
var _315=curam.util.getTopmostWindow();
sc.addContextBits("MODAL");
_315.curam.util.GENERIC_ERROR_MODAL_MAP={"windowsOptions":_312,"titleInfo":_313,"msg":_314,"msgPlaceholder":"","errorModal":false,"hasCancelButton":true};
var url="generic-modal-error.jspx?"+sc.toRequestString();
curam.util.openModalDialog({href:encodeURI(url)},_312);
},addPlaceholderFocusClassToEventOrAnchorTag:function(_316,_317){
var _318=curam.util.getTopmostWindow();
_318.curam.util.PLACEHOLDER_WINDOW_LIST=!_318.curam.util.PLACEHOLDER_WINDOW_LIST?[]:_318.curam.util.PLACEHOLDER_WINDOW_LIST;
_2ae.add(_316,"placeholder-for-focus");
_318.curam.util.PLACEHOLDER_WINDOW_LIST.push(_317);
},returnFocusToPopupActionAnchorElement:function(_319){
var _31a=_319.curam.util.PLACEHOLDER_WINDOW_LIST;
if(_31a&&_31a.length>0){
var _31b=_31a.pop();
var _31c=_31b&&_31b.document.activeElement;
var _31d=_31c&&dojo.query(".placeholder-for-focus",_31c);
if(_31d&&_31d.length==1){
_31d[0].focus();
_2ae.remove(_31d[0],"placeholder-for-focus");
}
_319.curam.util.PLACEHOLDER_WINDOW_LIST.splice(0,_319.curam.util.PLACEHOLDER_WINDOW_LIST.length);
_319.curam.util.PLACEHOLDER_WINDOW_LIST=null;
}
},openModalDialog:function(_31e,_31f,left,top,_320){
_31e.event&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_31e.event,_31e.event.ownerDocument.defaultView.window);
var href;
if(!_31e||!_31e.href){
_31e=_2b0.fix(_31e);
var _321=_31e.target;
while(_321.tagName!="A"&&_321!=_2ab.body()){
_321=_321.parentNode;
}
href=_321.href;
_321._isModal=true;
_2b0.stop(_31e);
}else{
href=_31e.href;
_31e._isModal=true;
}
require(["curam/dialog"]);
var opts=curam.dialog.parseWindowOptions(_31f);
curam.util.showModalDialog(href,_31e,opts["width"],opts["height"],left,top,false,null,null,_320);
return true;
},showModalDialog:function(url,_322,_323,_324,left,top,_325,_326,_327,_328){
var _329=curam.util.getTopmostWindow();
if(dojo.global!=_329){
curam.debug.log("curam.util.showModalDialog: "+_2b5.getProperty("curam.util.redirecting.modal"));
_329.curam.util.showModalDialog(url,_322,_323,_324,left,top,_325,_326,dojo.global,_328);
return;
}
var rup=curam.util.replaceUrlParam;
url=rup(url,"o3frame","modal");
url=curam.util.modifyUrlContext(url,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
url=rup(url,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
curam.debug.log(_2b5.getProperty("curam.util.modal.url"),url);
if(_323){
_323=typeof (_323)=="number"?_323:parseInt(_323);
}
if(_324){
_324=typeof (_324)=="number"?_324:parseInt(_324);
}
if(!curam.util._isModalCurrentlyOpening()){
if(url.indexOf("__o3rpu=about%3Ablank")>=0){
alert(curam_util_showModalDialog_pageStillLoading);
return;
}
curam.util._setModalCurrentlyOpening(true);
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
new curam.ModalDialog({href:url,width:_323,height:_324,openNode:(_322&&_322.target)?_322.target:null,parentWindow:_327,uimToken:_328});
}else{
require(["curam/modal/CuramCarbonModal"]);
new curam.modal.CuramCarbonModal({href:url,width:_323,height:_324,openNode:(_322&&_322.target)?_322.target:null,parentWindow:_327,uimToken:_328});
}
return true;
}
},showModalDialogWithRef:function(_32a,_32b,_32c){
var _32d=curam.util.getTopmostWindow();
if(dojo.global!=_32d){
return _32d.curam.util.showModalDialogWithRef(_32a,dojo.global);
}
var rup=curam.util.replaceUrlParam;
_32a=curam.util.modifyUrlContext(_32a,"MODAL","TAB|LIST_ROW_INLINE_PAGE|LIST_EVEN_ROW|NESTED_UIM");
_32a=rup(_32a,curam.util.PREVENT_CACHE_FLAG,(new Date()).getTime());
if(!curam.util._isModalCurrentlyOpening()){
curam.util._setModalCurrentlyOpening(true);
var dl;
if(jsScreenContext.hasContextBits("EXTAPP")){
require(["curam/ModalDialog"]);
if(_32c){
dl=new curam.ModalDialog({href:_32a,width:_32c.width,height:_32c.height,parentWindow:_32b});
}else{
dl=new curam.ModalDialog({href:_32a,parentWindow:_32b});
}
}else{
require(["curam/modal/CuramCarbonModal"]);
if(_32c){
dl=new curam.modal.CuramCarbonModal({href:_32a,width:_32c.width,height:_32c.height,parentWindow:_32b});
}else{
dl=new curam.modal.CuramCarbonModal({href:_32a,parentWindow:_32b});
}
}
return dl;
}
},_isModalCurrentlyOpening:function(){
return curam.util.getTopmostWindow().curam.util._modalOpenInProgress;
},_setModalCurrentlyOpening:function(_32e){
curam.util.getTopmostWindow().curam.util._modalOpenInProgress=_32e;
},setupPreferencesLink:function(href){
_2aa(function(){
var _32f=_2b1(".user-preferences")[0];
if(_32f){
if(typeof (_32f._disconnectToken)=="undefined"){
_32f._disconnectToken=curam.util.connect(_32f,"onclick",curam.util.openPreferences);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_2b5.getProperty("curam.util.no.setup"));
}
});
},setupLocaleLink:function(href){
_2aa(function(){
var _330=_2b1(".user-locale")[0];
if(_330){
if(typeof (_330._disconnectToken)=="undefined"){
_330._disconnectToken=curam.util.connect(_330,"onclick",curam.util.openLocaleNew);
}
if(!href){
href=dojo.global.location.href;
}
}else{
curam.debug.log(_2b5.getProperty("curam.util.no.setup"));
}
});
},openPreferences:function(_331){
_2b0.stop(_331);
if(_331.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-prefs-editor.jspx",{dialogOptions:"width=605"});
});
},logout:function(_332){
var _333=curam.util.getTopmostWindow();
_333.dojo.publish("curam/redirect/logout");
window.location.href=jsBaseURL+"/logout.jsp";
},openAbout:function(_334){
_2b0.stop(_334);
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("about.jsp",{dialogOptions:"width=583,height=399"});
});
},addMinWidthCalendarCluster:function(id){
var _335=dom.byId(id);
var i=0;
function _336(evt){
_2ad.forEach(_335.childNodes,function(node){
if(_2ae.contains(node,"cluster")){
_2ac.set(node,"width","97%");
if(node.clientWidth<700){
_2ac.set(node,"width","700px");
}
}
});
};
if(has("ie")>6){
_2ad.forEach(_335.childNodes,function(node){
if(_2ae.contains(node,"cluster")){
_2ac.set(node,"minWidth","700px");
}
});
}else{
on(dojo.global,"resize",_336);
_2aa(_336);
}
},addPopupFieldListener:function(id){
if(!has("ie")||has("ie")>6){
return;
}
if(!curam.util._popupFields){
function _337(evt){
var _338=0;
var j=0;
var x=0;
var arr=curam.util._popupFields;
_2ad.forEach(curam.util._popupFields,function(id){
var _339=dom.byId(id);
_2b1("> .popup-actions",_339).forEach(function(node){
_338=node.clientWidth+30;
});
_2b1("> .desc",_339).forEach(function(node){
_2ac.set(node,"width",Math.max(0,_339.clientWidth-_338)+"px");
});
});
};
curam.util._popupFields=[id];
on(dojo.global,"resize",_337);
_2aa(_337);
}else{
curam.util._popupFields.push(id);
}
},addContentWidthListener:function(id){
if(has("ie")>6){
return;
}
var _33a=_2ac.set;
var _33b=_2ae.contains;
function _33c(evt){
var i=0;
var _33d=dom.byId("content");
if(_33d){
var _33e=_33d.clientWidth;
if(has("ie")==6&&dom.byId("footer")){
var _33f=_2ab.body().clientHeight-100;
_33a(_33d,"height",_33f+"px");
var _340=dom.byId("sidebar");
if(_340){
_33a(_340,"height",_33f+"px");
}
}
try{
_2b1("> .page-title-bar",_33d).forEach(function(node){
var _341=geom.getMarginSize(node).w-geom.getContentBox(node).w;
if(!has("ie")){
_341+=1;
}
_33e=_33d.clientWidth-_341;
_2ac.set(node,"width",_33e+"px");
});
}
catch(e){
}
_2b1("> .page-description",_33d).style("width",_33e+"px");
_2b1("> .in-page-navigation",_33d).style("width",_33e+"px");
}
};
curam.util.subscribe("/clusterToggle",_33c);
curam.util.connect(dojo.global,"onresize",_33c);
_2aa(_33c);
},alterScrollableListBottomBorder:function(id,_342){
var _343=_342;
var _344="#"+id+" table";
function _345(){
var _346=_2b1(_344)[0];
if(_346.offsetHeight>=_343){
var _347=_2b1(".odd-last-row",_346)[0];
if(typeof _347!="undefined"){
_2ae.add(_347,"no-bottom-border");
}
}else{
if(_346.offsetHeight<_343){
var _347=_2b1(".even-last-row",_346)[0];
if(typeof _347!="undefined"){
_2ae.add(_347,"add-bottom-border");
}
}else{
curam.debug.log("curam.util.alterScrollableListBottomBorder: "+_2b5.getProperty("curam.util.code"));
}
}
};
_2aa(_345);
},addFileUploadResizeListener:function(code){
function _348(evt){
if(_2b1(".widget")){
_2b1(".widget").forEach(function(_349){
var _34a=_349.clientWidth;
if(_2b1(".fileUpload",_349)){
_2b1(".fileUpload",_349).forEach(function(_34b){
fileUploadWidth=_34a/30;
if(fileUploadWidth<4){
_34b.size=1;
}else{
_34b.size=fileUploadWidth;
}
});
}
});
}
};
on(dojo.global,"resize",_348);
_2aa(_348);
},openCenteredNonModalWindow:function(url,_34c,_34d,name){
_34c=Number(_34c);
_34d=Number(_34d);
var _34e=(screen.width-_34c)/2;
var _34f=(screen.height-_34d)/2;
_34d=_34f<0?screen.height:_34d;
_34f=Math.max(0,_34f);
_34c=_34e<0?screen.width:_34c;
_34e=Math.max(0,_34e);
var left="left",top="top";
if(has("ff")){
left="screenX",top="screenY";
}
var _350="location=no, menubar=no, status=no, toolbar=no, "+"scrollbars=yes, resizable=no";
var _351=dojo.global.open(url,name||"name","width="+_34c+", height="+_34d+", "+left+"="+_34e+","+top+"="+_34f+","+_350);
_351.resizeTo(_34c,_34d);
_351.moveTo(_34e,_34f);
_351.focus();
},adjustTargetContext:function(win,href){
if(win&&win.dojo.global.jsScreenContext){
var _352=win.dojo.global.jsScreenContext;
_352.updateStates(dojo.global.jsScreenContext);
return curam.util.replaceUrlParam(href,"o3ctx",_352.getValue());
}
return href;
},modifyUrlContext:function(url,_353,_354){
var _355=url;
var ctx=new curam.util.ScreenContext();
var _356=curam.util.getUrlParamValue(url,"o3ctx");
if(_356){
ctx.setContext(_356);
}else{
ctx.clear();
}
if(_353){
ctx.addContextBits(_353);
}
if(_354){
ctx.clear(_354);
}
_355=curam.util.replaceUrlParam(url,"o3ctx",ctx.getValue());
return _355;
},updateCtx:function(_357){
var _358=curam.util.getUrlParamValue(_357,"o3ctx");
if(!_358){
return _357;
}
return curam.util.modifyUrlContext(_357,null,"MODAL");
},getFrameRoot:function(_359,_35a){
var _35b=false;
var _35c=_359;
if(_35c){
while(_35c!=top&&!_35c.rootObject){
_35c=_35c.parent;
}
if(_35c.rootObject){
_35b=(_35c.rootObject==_35a);
}
}
return _35b?_35c:null;
},saveInformationalMsgs:function(_35d){
try{
localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID]=json.toJson({pageID:_2ab.body().id,total:dom.byId(curam.util.ERROR_MESSAGES_CONTAINER).innerHTML,listItems:dom.byId(curam.util.ERROR_MESSAGES_LIST).innerHTML});
_35d();
}
catch(e){
curam.debug.log(_2b5.getProperty("curam.util.exception"),e);
}
},disableInformationalLoad:function(){
curam.util._informationalsDisabled=true;
},redirectDirectUrl:function(){
_2aa(function(){
if(dojo.global.parent==dojo.global){
var url=document.location.href;
var idx=url.lastIndexOf("/");
if(idx>-1){
if(idx<=url.length){
url=url.substring(idx+1);
}
}
dojo.global.location=jsBaseURL+"/AppController.do?o3gtu="+encodeURIComponent(url);
}
});
},loadInformationalMsgs:function(){
_2aa(function(){
if(dojo.global.jsScreenContext.hasContextBits("CONTEXT_PANEL")){
return;
}
if(curam.util._informationalsDisabled){
return;
}
var msgs=localStorage[curam.util.INFORMATIONAL_MSGS_STORAGE_ID];
if(msgs&&msgs!=""){
msgs=json.fromJson(msgs);
localStorage.removeItem(curam.util.INFORMATIONAL_MSGS_STORAGE_ID);
var div=dom.byId(curam.util.ERROR_MESSAGES_CONTAINER);
var list=dom.byId(curam.util.ERROR_MESSAGES_LIST);
if(msgs.pageID!=_2ab.body().id){
return;
}
if(list){
var _35e=_2a9.create("ul",{innerHTML:msgs.listItems});
var _35f=[];
for(var i=0;i<list.childNodes.length;i++){
if(list.childNodes[i].tagName=="LI"){
_35f.push(list.childNodes[i]);
}
}
var skip=false;
var _360=_35e.childNodes;
for(var i=0;i<_360.length;i++){
skip=false;
for(var j=0;j<_35f.length;j++){
if(_360[i].innerHTML==_35f[j].innerHTML){
skip=true;
break;
}
}
if(!skip){
list.appendChild(_360[i]);
i--;
}
}
}else{
if(div){
div.innerHTML=msgs.total;
}
}
}
var _361=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_361&&!dojo.global.jsScreenContext.hasContextBits("MODAL")){
if(curam.util.getTopmostWindow().curam.util.tabButtonClicked){
curam.util.getTopmostWindow().curam.util.getTabButtonClicked().focus();
setTimeout(function(){
_361.innerHTML=_361.innerHTML+" ";
},500);
}else{
_361.focus();
}
}
var _362=dom.byId("error-messages-container-wrapper");
if(_362){
var _363=_2b1("#container-messages-ul",_362)[0];
if(_363){
_363.focus();
}
}
});
},_setFocusCurrentIframe:function(){
var _364=/Edg/.test(navigator.userAgent);
if(_364){
var _365=window.frameElement;
if(_365){
_365.setAttribute("tabindex","0");
_365.focus();
setTimeout(function(){
_365.removeAttribute("tabindex");
},10);
}
}
},setFocus:function(){
var _366;
if(window.document.getElementsByClassName("skeleton").length>0){
_366=setTimeout(function(){
curam.util.setFocus();
},300);
}else{
if(_366){
clearTimeout(_366);
}
var _367=curam.util.getTopmostWindow().dojo.global.focusSetOnCancelButton;
if(_367){
return;
}
curam.util._setFocusCurrentIframe();
var _368=curam.util.getUrlParamValue(dojo.global.location.href,"o3frame")=="modal"||(curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==null&&curam.util.getUrlParamValue(dojo.global.location.href,"o3modalprev")!==undefined);
if(!_368){
_2aa(function(){
var _369=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
var _36a=sessionStorage.getItem("curamDefaultActionId");
var _36b=null;
if(!_369&&_36a){
sessionStorage.removeItem("curamDefaultActionId");
_36b=dojo.query(".curam-default-action")[0].previousSibling;
}else{
_36b=curam.util.doSetFocus();
}
if(_36b){
curam.util.setFocusOnField(_36b,false);
}else{
window.focus();
}
});
}
}
},setFocusOnField:function(_36c,_36d,_36e){
if(has("IE")||has("trident")){
var _36f=1000;
var _370=_36d?200:500;
curam.util._createHiddenInputField(_36c);
var _371=function(ff){
return function(){
var _372=ff.ownerDocument.activeElement;
if(_372.tagName==="INPUT"&&!_372.classList.contains("hidden-focus-input")||_372.tagName==="TEXTAREA"||(_372.tagName==="SPAN"&&_372.className=="fileUploadButton")||(_372.tagName==="A"&&_372.className=="popup-action")||(_372.tagName==="IFRAME"&&_372.classList.contains("cke_wysiwyg_frame"))){
return;
}else{
ff.focus();
}
};
};
if(_36d){
var _373=attr.get(_36c,"aria-label");
var _374="";
var _375=attr.get(_36c,"objid");
if(_375&&_375.indexOf("component")==0||_2ae.contains(_36c,"dijitReset dijitInputInner")){
_374=_36c.title;
}else{
_374=_36e||"Modal Dialog";
}
if(_36c&&_36c.id!=="container-messages-ul"){
attr.set(_36c,"aria-label",_374);
}
var _376=function(_377){
return function(e){
_2b1("input|select[aria-label="+_374+"]").forEach(function(_378){
_377&&attr.set(_378,"aria-label",_377);
!_377&&attr.remove(_378,"aria-label");
});
};
};
on(_36c,"blur",_376(_373));
}
if(_36c.tagName==="TEXTAREA"){
setTimeout(_371(_36c),_36f);
}else{
if(_36c.tagName==="SELECT"||(_36c.tagName==="INPUT"&&attr.get(_36c,"type")==="text")){
setTimeout(_371(_36c),_370);
}else{
_36c.focus();
}
}
}else{
_36c.focus();
}
},_createHiddenInputField:function(_379){
var _37a=_379.ownerDocument.forms["mainForm"];
if(_37a&&(_379.tagName==="SELECT"||(_379.tagName==="INPUT"&&attr.get(_379,"type")==="text"))){
var _37b=_2a9.create("input",{"class":"hidden-focus-input","style":"position: absolute; height: 1px; width: 1px; overflow: hidden; clip: rect(1px, 1px, 1px, 1px); white-space: nowrap;","type":"text","aria-hidden":"true","tabindex":"-1"});
_2a9.place(_37b,_37a,"before");
_37b.focus();
on(_37b,"blur",function(){
_2a9.destroy(_37b);
});
}
},doSetFocus:function(){
try{
var _37c=curam.util.getTopmostWindow().curam.util.getTabButtonClicked();
if(_37c!=false&&!curam.util.isModalWindow()){
return _37c;
}
var _37d=dom.byId("container-messages-ul")?dom.byId("container-messages-ul"):dom.byId("error-messages");
if(_37d){
return _37d;
}
var form=document.forms[0];
if(!form){
return false;
}
var _37e=form.querySelectorAll("button, output, input:not([type=\"image\"]), select, object, textarea, fieldset, a.popup-action, span.fileUploadButton");
var _37f=false;
var l=_37e.length,el;
for(var i=0;i<l;i++){
el=_37e[i];
if(!_37f&&/selectHook/.test(el.className)){
_37f=_2b1(el).closest("table")[0];
}
if(!_37f&&!(el.style.visibility=="hidden")&&(/select-one|select-multiple|checkbox|radio|text/.test(el.type)||el.tagName=="TEXTAREA"||/popup-action|fileUploadButton/.test(el.className))&&!/dijitArrowButtonInner|dijitValidationInner/.test(el.className)){
_37f=el;
}
if(el.tabIndex=="1"){
el.tabIndex=0;
_37f=el;
break;
}
}
lastOpenedTabButton=curam.util.getTopmostWindow().curam.util.getLastOpenedTabButton();
if(!_37f&&lastOpenedTabButton){
return lastOpenedTabButton;
}
var _380=_37f.classList.contains("bx--date-picker__input");
if(_380){
var _381=document.querySelector(".bx--uim-modal");
if(_381){
_37f=_381;
}
}
return _37f;
}
catch(e){
_2b5.log(_2b5.getProperty("curam.util.error.focus"),e.message);
return false;
}
return false;
},openLocaleSelector:function(_382){
_382=_2b0.fix(_382);
var _383=_382.target;
while(_383&&_383.tagName!="A"){
_383=_383.parentNode;
}
var loc=_383.href;
var rpu=curam.util.getUrlParamValue(loc,"__o3rpu");
rpu=curam.util.removeUrlParam(rpu,"__o3rpu");
var href="user-locale-selector.jspx"+"?__o3rpu="+rpu;
if(!curam.util.isActionPage(dojo.global.location.href)){
openModalDialog({href:href},"width=500,height=300",200,150);
}else{
alert(curam.util.msgLocaleSelectorActionPage);
}
return false;
},openLocaleNew:function(_384){
_2b0.stop(_384);
if(_384.target._curamDisable){
return;
}
require(["curam/tab"],function(){
curam.tab.getTabController().handleLinkClick("user-locale-selector.jspx",{dialogOptions:"width=300"});
});
},isActionPage:function(url){
var _385=curam.util.getLastPathSegmentWithQueryString(url);
var _386=_385.split("?")[0];
return _386.indexOf("Action.do")>-1;
},closeLocaleSelector:function(_387){
_387=_2b0.fix(_387);
_2b0.stop(_387);
dojo.global.close();
return false;
},getSuffixFromClass:function(node,_388){
var _389=attr.get(node,"class").split(" ");
var _38a=_2ad.filter(_389,function(_38b){
return _38b.indexOf(_388)==0;
});
if(_38a.length>0){
return _38a[0].split(_388)[1];
}else{
return null;
}
},getCacheBusterParameter:function(){
return this.CACHE_BUSTER_PARAM_NAME+"="+new Date().getTime()+"_"+this.CACHE_BUSTER++;
},stripeTable:function(_38c,_38d,_38e){
var _38f=_38c.tBodies[0];
var _390=(_38d?2:1);
if(_38f.rows.length<_390){
return;
}
var rows=_38f.rows;
var _391=[],_392=[],_393=false,_394=[],_395="";
for(var i=0,l=rows.length;i<l;i+=_390){
var _396=(i%(2*_390)==0),_397=_396?_392:_391;
_2ae.remove(rows[i],["odd-last-row","even-last-row"]);
_397.push(rows[i]);
if(i==_38e){
_394.push(rows[i]);
_395=_396?"odd":"even";
_393=true;
}
if(_38d&&rows[i+1]){
_2ae.remove(rows[i+1],["odd-last-row","even-last-row"]);
_397.push(rows[i+1]);
_393&&_394.push(rows[i+1]);
}
_393=false;
}
_392.forEach(function(_398){
_2ae.replace(_398,"odd","even");
});
_391.forEach(function(_399){
_2ae.replace(_399,"even","odd");
});
_394.forEach(function(_39a){
_2ae.add(_39a,_395+"-last-row");
});
},fillString:function(_39b,_39c){
var _39d="";
while(_39c>0){
_39d+=_39b;
_39c-=1;
}
return _39d;
},updateHeader:function(qId,_39e,_39f,_3a0){
var _3a1=dom.byId("header_"+qId);
_3a1.firstChild.nextSibling.innerHTML=_39e;
answerCell=dom.byId("chosenAnswer_"+qId);
answerCell.innerHTML=_39f;
sourceCell=dom.byId("chosenSource_"+qId);
sourceCell.innerHTML=_3a0;
},search:function(_3a2,_3a3){
var _3a4=_2a8.byId(_3a2).get("value");
var _3a5=_2a8.byId(_3a3);
if(_3a5==null){
_3a5=dom.byId(_3a3);
}
var _3a6=null;
if(_3a5!=null){
if(_3a5.tagName==null){
_3a6=_3a5?_3a5.get("value"):null;
}else{
if(_3a5.tagName=="SELECT"){
var _3a7=_2b1(".multiple-search-banner select option");
_2ad.forEach(_3a7,function(elem){
if(elem.selected){
_3a6=elem.value;
}
});
}
}
}
var _3a8="";
var _3a9;
var _3aa;
if(_3a6){
_3aa=_3a6.split("|");
_3a8=_3aa[0];
_3a9=_3aa[1];
}
var _3ab=curam.util.defaultSearchPageID;
var _3ac="";
if(sessionStorage.getItem("appendSUEP")==="true"){
if(_3a8===""){
_3ac=_3ab+"Page.do?searchText="+encodeURIComponent(_3a4)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1");
}else{
_3ac=_3a9+"Page.do?searchText="+encodeURIComponent(_3a4)+"&searchType="+encodeURIComponent(_3a8)+"&"+curam.util.secureURLsExemptParamName+"="+encodeURIComponent(curam.util.secureURLsExemptParamsPrefix+"_ST1,"+curam.util.secureURLsExemptParamsPrefix+"_ST2");
}
}else{
if(_3a8===""){
_3ac=_3ab+"Page.do?searchText="+encodeURIComponent(_3a4);
}else{
_3ac=_3a9+"Page.do?searchText="+encodeURIComponent(_3a4)+"&searchType="+encodeURIComponent(_3a8);
}
}
var _3ad=new curam.ui.PageRequest(_3ac);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_3ad);
});
},updateDefaultSearchText:function(_3ae,_3af){
var _3b0=_2a8.byId(_3ae);
var _3b1=_2a8.byId(_3af);
var _3b2=_3b0?_3b0.get("value"):null;
var str=_3b2.split("|")[2];
_3b1.set("placeHolder",str);
_2af.publish("curam/application-search/combobox-changed",_3b2);
},updateSearchBtnState:function(_3b3,_3b4){
var _3b5=_2a8.byId(_3b3);
var btn=dom.byId(_3b4);
var _3b6=_3b5.get("value");
if(!_3b6||lang.trim(_3b6).length<1){
_2ae.add(btn,"dijitDisabled");
}else{
_2ae.remove(btn,"dijitDisabled");
}
},furtherOptionsSearch:function(){
var _3b7=curam.util.furtherOptionsPageID+"Page.do";
var _3b8=new curam.ui.PageRequest(_3b7);
require(["curam/tab"],function(){
curam.tab.getTabController().handlePageRequest(_3b8);
});
},searchButtonStatus:function(_3b9){
var btn=dom.byId(_3b9);
if(!_2ae.contains(btn,"dijitDisabled")){
return true;
}
},getPageHeight:function(){
var _3ba=400;
var _3bb=0;
if(_2b1("frameset").length>0){
curam.debug.log("curam.util.getPageHeight() "+_2b5.getProperty("curam.util.default.height"),_3ba);
_3bb=_3ba;
}else{
var _3bc=function(node){
if(!node){
curam.debug.log(_2b5.getProperty("curam.util.node"));
return 0;
}
var mb=geom.getMarginSize(node);
var pos=geom.position(node);
return pos.y+mb.h;
};
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
var _3bd=_2b1("div#content")[0];
var _3be=_3bc(_3bd);
curam.debug.log(_2b5.getProperty("curam.util.page.height"),_3be);
_3bb=_3be;
}else{
var _3bf=dom.byId("content")||dom.byId("wizard-content");
var _3c0=_2b1("> *",_3bf).filter(function(n){
return n.tagName.indexOf("SCRIPT")<0&&_2ac.get(n,"visibility")!="hidden"&&_2ac.get(n,"display")!="none";
});
var _3c1=_3c0[0];
for(var i=1;i<_3c0.length;i++){
if(_3bc(_3c0[i])>=_3bc(_3c1)){
_3c1=_3c0[i];
}
}
_3bb=_3bc(_3c1);
curam.debug.log("curam.util.getPageHeight() "+_2b5.getProperty("curam.util.base.height"),_3bb);
var _3c2=_2b1(".actions-panel",_2ab.body());
if(_3c2.length>0){
var _3c3=geom.getMarginBox(_3c2[0]).h;
curam.debug.log("curam.util.getPageHeight() "+_2b5.getProperty("curam.util.panel.height"));
_3bb+=_3c3;
_3bb+=10;
}
var _3c4=_2b1("body.details");
if(_3c4.length>0){
curam.debug.log("curam.util.getPageHeight() "+_2b5.getProperty("curam.util.bar.height"));
_3bb+=20;
}
}
}
curam.debug.log("curam.util.getPageHeight() "+_2b5.getProperty("curam.util.returning"),_3bb);
return _3bb;
},toCommaSeparatedList:function(_3c5){
var _3c6="";
for(var i=0;i<_3c5.length;i++){
_3c6+=_3c5[i];
if(i<_3c5.length-1){
_3c6+=",";
}
}
return _3c6;
},setupGenericKeyHandler:function(){
_2aa(function(){
var f=function(_3c7){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")&&_3c7.keyCode==27){
var ev=_2b0.fix(_3c7);
var _3c8=_2a8.byId(ev.target.id);
var _3c9=typeof _3c8!="undefined"&&_3c8.baseClass=="dijitTextBox dijitComboBox";
if(!_3c9){
curam.dialog.closeModalDialog();
}
}
if(_3c7.keyCode==13){
var ev=_2b0.fix(_3c7);
var _3ca=ev.target.type=="text";
var _3cb=ev.target.type=="radio";
var _3cc=ev.target.type=="checkbox";
var _3cd=ev.target.type=="select-multiple";
var _3ce=ev.target.type=="password";
var _3cf=_2a8.byId(ev.target.id);
if(typeof _3cf!="undefined"){
var _3d0=_2a8.byId(ev.target.id);
if(!_3d0){
_3d0=_2a8.byNode(dom.byId("widget_"+ev.target.id));
}
if(_3d0&&_3d0.enterKeyOnOpenDropDown){
_3d0.enterKeyOnOpenDropDown=false;
return false;
}
}
var _3d1=ev.target.getAttribute("data-carbon-attach-point");
if(_3d1&&_3d1==="carbon-menu"){
return false;
}
var _3d2=typeof _3cf!="undefined"&&_3cf.baseClass=="dijitComboBox";
if((!_3ca&&!_3cb&&!_3cc&&!_3cd&&!_3ce)||_3d2){
return true;
}
var _3d3=null;
var _3d4=_2b1(".curam-default-action");
if(_3d4.length>0){
_3d3=_3d4[0];
}else{
var _3d5=_2b1("input[type='submit']");
if(_3d5.length>0){
_3d3=_3d5[0];
}
}
if(_3d3!=null){
_2b0.stop(_2b0.fix(_3c7));
curam.util.clickButton(_3d3);
return false;
}
require(["curam/dateSelectorUtil"],function(_3d6){
var _3d7=dom.byId("year");
if(_3d7){
dojo.stopEvent(dojo.fixEvent(_3c7));
_3d6.updateCalendar();
}
});
}
return true;
};
curam.util.connect(_2ab.body(),"onkeyup",f);
});
},enterKeyPress:function(_3d8){
if(_3d8.keyCode==13){
return true;
}
},swapState:function(node,_3d9,_3da,_3db){
if(_3d9){
_2ae.replace(node,_3da,_3db);
}else{
_2ae.replace(node,_3db,_3da);
}
},makeQueryString:function(_3dc){
if(!_3dc||_3dc.length==0){
return "";
}
var _3dd=[];
for(var _3de in _3dc){
_3dd.push(_3de+"="+encodeURIComponent(_3dc[_3de]));
}
return "?"+_3dd.join("&");
},fileDownloadAnchorHandler:function(url){
var _3df=curam.util.getTopmostWindow();
var _3e0=_3df.dojo.subscribe("/curam/dialog/close",function(id,_3e1){
if(_3e1==="confirm"){
curam.util.clickHandlerForListActionMenu(url,false,false);
}
_3df.dojo.unsubscribe(_3e0);
});
var _3e2=new _2ba("GenericModalError");
var _3e3=_3e2.getProperty("file.download.warning.dialog.width");
var _3e4=_3e2.getProperty("file.download.warning.dialog.height");
if(!_3e3){
_3e3=500;
}
if(!_3e4){
_3e4=225;
}
var _3e5=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_3e3+",height="+_3e4,"file.download.warning.title","file.download.warning."+_3e5);
return false;
},fileDownloadListActionHandler:function(url,_3e6,_3e7,_3e8){
var _3e9=curam.util.getTopmostWindow();
var _3ea=_3e9.dojo.subscribe("/curam/dialog/close",function(id,_3eb){
if(_3eb==="confirm"){
curam.util.clickHandlerForListActionMenu(url,_3e6,_3e7,_3e8);
}
_3e9.dojo.unsubscribe(_3ea);
});
var _3ec=new _2ba("GenericModalError");
var _3ed=_3ec.getProperty("file.download.warning.dialog.width");
var _3ee=_3ec.getProperty("file.download.warning.dialog.height");
if(!_3ed){
_3ed=500;
}
if(!_3ee){
_3ee=225;
}
var _3ef=curam.util._getBrowserName();
curam.util.openGenericErrorModalDialogYesNo("width="+_3ed+",height="+_3ee,"file.download.warning.title","file.download.warning."+_3ef);
},_getBrowserName:function(){
var _3f0=has("trident");
var _3f1=dojo.isFF;
var _3f2=dojo.isChrome;
var _3f3=dojo.isSafari;
var _3f4=curam.util.getTopmostWindow();
var _3f5=curam.util.ExpandableLists._isExternalApp(_3f4);
if(_3f0!=undefined){
var _3f6=_3f0+4;
if(_3f6<8){
return "unknown.browser";
}else{
return "ie"+_3f6;
}
}else{
if(_3f1!=undefined&&_3f5){
return "firefox";
}else{
if(_3f2!=undefined){
return "chrome";
}else{
if(_3f3!=undefined&&_3f5){
return "safari";
}
}
}
}
return "unknown.browser";
},clickHandlerForListActionMenu:function(url,_3f7,_3f8,_3f9){
if(_3f7){
var href=curam.util.replaceUrlParam(url,"o3frame","modal");
var ctx=dojo.global.jsScreenContext;
ctx.addContextBits("MODAL");
href=curam.util.replaceUrlParam(href,"o3ctx",ctx.getValue());
curam.util.redirectWindow(href);
return;
}
var _3fa={href:url};
require(["curam/ui/UIMPageAdaptor"]);
if(curam.ui.UIMPageAdaptor.allowLinkToContinue(_3fa)){
if(_3fa.href.indexOf("/servlet/FileDownload")){
sessionStorage.setItem("addOnUnloadTriggeredByFileDownload","true");
dojo.global.location=url;
sessionStorage.removeItem("addOnUnloadTriggeredByFileDownload");
}else{
dojo.global.location=url;
}
return;
}
if(_3fa!=null){
if(_3f9){
_2b0.fix(_3f9);
_2b0.stop(_3f9);
}
if(!_3fa.href||_3fa.href.length==0){
return;
}
if(_3f8&&!curam.util.isInternal(url)){
dojo.global.open(url);
}else{
if(curam.ui.UIMPageAdaptor.isLinkValidForTabProcessing(_3fa)){
var _3fb=new curam.ui.PageRequest(_3fa.href);
if(dojo.global.jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")||dojo.global.jsScreenContext.hasContextBits("NESTED_UIM")){
_3fb.pageHolder=dojo.global;
}
require(["curam/tab"],function(){
var _3fc=curam.tab.getTabController();
if(_3fc){
_3fc.handlePageRequest(_3fb);
}
});
}
}
}
},clickHandlerForMailtoLinks:function(_3fd,url){
dojo.stopEvent(_3fd);
var _3fe=dojo.query("#mailto_frame")[0];
if(!_3fe){
_3fe=dojo.io.iframe.create("mailto_frame","");
}
_3fe.src=url;
return false;
},isInternal:function(url){
var path=url.split("?")[0];
var _3ff=path.match("Page.do");
if(_3ff!=null){
return true;
}
return false;
},getLastPathSegmentWithQueryString:function(url){
var _400=url.split("?");
var _401=_400[0].split("/");
return _401[_401.length-1]+(_400[1]?"?"+_400[1]:"");
},replaceSubmitButton:function(name,_402,_403,_404,_405){
if(curam.replacedButtons[name]=="true"){
return;
}
var _406="__o3btn."+name;
var _407;
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_407=_2b1("input[id='"+_406+"']");
}else{
_407=_2b1("input[name='"+_406+"']");
}
_407.forEach(function(_408,_409,_40a){
if(_402){
var _40b=_40a[1];
_40b.setAttribute("value",_402);
}
_408.tabIndex=-1;
var _40c=_408.parentNode;
var _40d;
var _40e=curam.util.isInternalModal()&&curam.util.isModalFooter(_408);
var _40f="btn-id-"+_409;
var _410="ac initially-hidden-widget "+_40f;
if(_2ae.contains(_408,"first-action-control")){
_410+=" first-action-control";
}
if(_40e){
var _411=(_403&&!_405)?undefined:_40a[0];
var _412=_403?{"href":"","buttonid":_404}:{"buttonid":_404};
var _413=_408.getAttribute("data-rawtestid");
if(_413){
_412.dataTestId=_413;
}
var _414=_2ae.contains(_408,"curam-default-action")?true:false;
curam.util.addCarbonModalButton(_412,_408.value,_411,_414);
}else{
curam.util.setupWidgetLoadMask("a."+_40f);
var _410="ac initially-hidden-widget "+_40f;
if(_2ae.contains(_408,"first-action-control")){
_410+=" first-action-control";
}
var _40d=_2a9.create("a",{"class":_410,href:"#"},_408,"before");
var _415=dojo.query(".page-level-menu")[0];
if(_415){
attr.set(_40d,"title",_408.value);
}
_2a9.create("span",{"class":"filler"},_40d,"before");
var left=_2a9.create("span",{"class":"left-corner"},_40d);
var _416=_2a9.create("span",{"class":"right-corner"},left);
var _417=_2a9.create("span",{"class":"middle"},_416);
_417.appendChild(document.createTextNode(_408.value));
curam.util.addActionControlClass(_40d);
}
if(_40d){
on(_40d,"click",function(_418){
curam.util.clickButton(this._submitButton);
_2b0.stop(_418);
});
_40d._submitButton=_40a[0];
}
_2ae.add(_408,"hidden-button");
attr.set(_408,"aria-hidden","true");
attr.set(_408,"id",_408.id+"_"+_409);
});
curam.replacedButtons[name]="true";
},isInternalModal:function(){
return !dojo.global.jsScreenContext.hasContextBits("EXTAPP")&&dojo.global.jsScreenContext.hasContextBits("MODAL");
},isModalFooter:function(_419){
if(_419){
var _41a=_419.parentNode.parentNode;
return _41a&&_41a.id=="actions-panel";
}
},addCarbonModalButton:function(_41b,_41c,_41d,_41e){
curam.util.getTopmostWindow().dojo.publish("/curam/CuramCarbonModal/addModalButton",[_41b,_41c,_41d,_41e,window]);
},setupWidgetLoadMask:function(_41f){
curam.util.subscribe("/curam/page/loaded",function(){
var _420=_2b1(_41f)[0];
if(_420){
_2ac.set(_420,"visibility","visible");
}else{
curam.debug.log("setupButtonLoadMask: "+_2b5.getProperty("curam.util.not.found")+"'"+_41f+"'"+_2b5.getProperty("curam.util.ignore.mask"));
}
});
},optReplaceSubmitButton:function(name){
if(curam.util.getFrameRoot(dojo.global,"wizard")==null){
curam.util.replaceSubmitButton(name);
return;
}
var _421=curam.util.getFrameRoot(dojo.global,"wizard").navframe.wizardNavigator;
if(_421.delegatesSubmit[jsPageID]!="assumed"){
curam.util.replaceSubmitButton(name);
}
},clickButton:function(_422){
var _423=dom.byId("mainForm");
var _424;
if(!_422){
curam.debug.log("curam.util.clickButton: "+_2b5.getProperty("curam.util..no.arg"));
return;
}
if(typeof (_422)=="string"){
var _425=_422;
curam.debug.log("curam.util.clickButton: "+_2b5.getProperty("curam.util.searching")+_2b5.getProperty("curam.util.id.of")+"'"+_425+"'.");
_422=_2b1("input[id='"+_425+"']")[0];
if(!_422){
_422=_2b1("input[name='"+_425+"']")[0];
}
if(!_422.form&&!_422.id){
curam.debug.log("curam.util.clickButton: "+_2b5.getProperty("curam.util.searched")+_2b5.getProperty("curam.util.id.of")+"'"+_425+_2b5.getProperty("curam.util.exiting"));
return;
}
}
if(dojo.global.jsScreenContext.hasContextBits("AGENDA")){
_424=_422;
}else{
_424=_2b1("input[id='"+_422.id+"']",_423)[0];
if(!_424){
_424=_2b1("input[name='"+_422.name+"']",_423)[0];
}
}
try{
if(attr.get(_423,"action").indexOf(jsPageID)==0){
curam.util.publishRefreshEvent();
}
_424.click();
}
catch(e){
curam.debug.log(_2b5.getProperty("curam.util.exception.clicking"));
}
},printPage:function(_426,_427){
_2b0.stop(_427);
var _428=dojo.window.get(_427.currentTarget.ownerDocument);
if(_426===false){
curam.util._printMainAreaWindow(_428);
return false;
}
var _429=_428.frameElement;
var _42a=_429;
while(_42a&&!_2ae.contains(_42a,"tab-content-holder")){
_42a=_42a.parentNode;
}
var _42b=_42a;
var _42c=dojo.query(".detailsPanelFrame",_42b)[0];
var _42d=_42c!=undefined&&_42c!=null;
if(_42d){
var isIE=has("trident")||has("ie");
var _42e=has("edge");
var _42f=_2ae.contains(_42c.parentNode,"collapsed");
if(isIE&&_42f){
_2ac.set(_42c.parentNode,"display","block");
}
_42c.contentWindow.focus();
_42c.contentWindow.print();
if(isIE&&_42f){
_2ac.set(_42c.parentNode,"display","");
}
if(isIE||_42e){
setTimeout(function(){
if(_42e){
function _430(){
curam.util._printMainAreaWindow(_428);
curam.util.getTopmostWindow().document.body.removeEventListener("mouseover",_430,true);
return false;
};
curam.util.getTopmostWindow().document.body.addEventListener("mouseover",_430,true);
}else{
if(isIE){
curam.util._printMainAreaWindow(_428);
return false;
}
}
},2000);
}else{
curam.util._printMainAreaWindow(_428);
return false;
}
}else{
curam.util._printMainAreaWindow(_428);
return false;
}
},_printMainAreaWindow:function(_431){
var _432=_2b1(".list-details-row-toggle.expanded");
if(_432.length>0){
curam.util._prepareContentPrint(_431);
_431.focus();
_431.print();
curam.util._deletePrintVersion();
}else{
_431.focus();
_431.print();
}
},_prepareContentPrint:function(_433){
var _434=Array.prototype.slice.call(_433.document.querySelectorAll("body iframe"));
_434.forEach(function(_435){
curam.util._prepareContentPrint(_435.contentWindow);
var list=_435.contentWindow.document.querySelectorAll(".title-exists");
var _436=_435.contentWindow.document.querySelectorAll(".title-exists div.context-panel-wrapper");
if(list.length>0&&_436.length===0){
var _437=document.createElement("div");
_437.setAttribute("class","tempContentPanelFrameWrapper");
_437.innerHTML=list[0].innerHTML;
var _438=_435.parentNode;
_438.parentNode.appendChild(_437);
_438.style.display="none";
curam.util.wrappersMap.push({tempDivWithIframeContent:_437,iframeParentElement:_438});
}
});
},_deletePrintVersion:function(){
if(curam.util.wrappersMap){
curam.util.wrappersMap.forEach(function(_439){
_439.tempDivWithIframeContent.parentNode.removeChild(_439.tempDivWithIframeContent);
_439.iframeParentElement.style.display="block";
});
curam.util.wrappersMap=[];
}
},addSelectedClass:function(_43a){
_2ae.add(_43a.target,"selected");
},removeSelectedClass:function(_43b){
_2ae.remove(_43b.target,"selected");
},openHelpPage:function(_43c,_43d){
_2b0.stop(_43c);
dojo.global.open(_43d);
},connect:function(_43e,_43f,_440){
var h=function(_441){
_440(_2b0.fix(_441));
};
if(has("ie")&&has("ie")<9){
_43e.attachEvent(_43f,h);
_2b3.addOnWindowUnload(function(){
_43e.detachEvent(_43f,h);
});
return {object:_43e,eventName:_43f,handler:h};
}else{
var _442=_43f;
if(_43f.indexOf("on")==0){
_442=_43f.slice(2);
}
var dt=on(_43e,_442,h);
_2b3.addOnWindowUnload(function(){
dt.remove();
});
return dt;
}
},disconnect:function(_443){
if(has("ie")&&has("ie")<9){
_443.object.detachEvent(_443.eventName,_443.handler);
}else{
_443.remove();
}
},subscribe:function(_444,_445){
var st=_2af.subscribe(_444,_445);
_2b3.addOnWindowUnload(function(){
st.remove();
});
return st;
},unsubscribe:function(_446){
_446.remove();
},addActionControlClickListener:function(_447){
var _448=dom.byId(_447);
var _449=_2b1(".ac",_448);
if(_449.length>0){
for(var i=0;i<_449.length;i++){
var _44a=_449[i];
curam.util.addActionControlClass(_44a);
}
}
this._addAccessibilityMarkupInAddressClustersWhenContextIsMissing();
},_addAccessibilityMarkupInAddressClustersWhenContextIsMissing:function(){
var _44b=_2b1(".bx--accordion__content");
_44b.forEach(function(_44c){
var _44d=_2b1(".bx--address",_44c)[0];
if(typeof (_44d)!="undefined"){
var _44e=new _2ba("util");
var _44f=_44d.parentElement.parentElement.parentElement;
var _450=_44f.parentElement.parentElement;
var _451=_2b1("h4, h3",_44f).length==1?true:false;
var _452=attr.get(_450,"aria-label")!==null?true:false;
if(!_451&&!_452){
attr.set(_450,"role","group");
attr.set(_450,"aria-label",_44e.getProperty("curam.address.header"));
}
}
});
},addActionControlClass:function(_453){
curam.util.connect(_453,"onmousedown",function(){
_2ae.add(_453,"selected-button");
curam.util.connect(_453,"onmouseout",function(){
_2ae.remove(_453,"selected-button");
});
});
},getClusterActionSet:function(){
var _454=dom.byId("content");
var _455=_2b1(".blue-action-set",_454);
if(_455.length>0){
for(var i=0;i<_455.length;i++){
curam.util.addActionControlClickListener(_455[i]);
}
}
},adjustActionButtonWidth:function(){
if(has("ie")==8){
_2aa(function(){
if(dojo.global.jsScreenContext.hasContextBits("MODAL")){
_2b1(".action-set > a").forEach(function(node){
if(node.childNodes[0].offsetWidth>node.offsetWidth){
_2ac.set(node,"width",node.childNodes[0].offsetWidth+"px");
_2ac.set(node,"display","block");
_2ac.set(node,"display","inline-block");
}
});
}
});
}
},setRpu:function(url,rtc,_456){
if(!url||!rtc||!rtc.getHref()){
throw {name:"Unexpected values",message:"This value not allowed for url or rtc"};
}
var _457=curam.util.getLastPathSegmentWithQueryString(rtc.getHref());
_457=curam.util.removeUrlParam(_457,curam.util.Constants.RETURN_PAGE_PARAM);
if(_456){
var i;
for(i=0;i<_456.length;i++){
if(!_456[i].key||!_456[i].value){
throw {name:"undefined value error",message:"The object did not contain a valid key/value pair"};
}
_457=curam.util.replaceUrlParam(_457,_456[i].key,_456[i].value);
}
}
var _458=curam.util.replaceUrlParam(url,curam.util.Constants.RETURN_PAGE_PARAM,encodeURIComponent(_457));
curam.debug.log("curam.util.setRpu "+_2b5.getProperty("curam.util.added.rpu")+_458);
return _458;
},retrieveBaseURL:function(){
return dojo.global.location.href.match(".*://[^/]*/[^/]*");
},removeRoleRegion:function(){
var body=dojo.query("body")[0];
attr.remove(body,"role");
},iframeTitleFallBack:function(){
var _459=curam.tab.getContainerTab(curam.tab.getContentPanelIframe());
var _45a=dom.byId(curam.tab.getContentPanelIframe());
var _45b=_45a.contentWindow.document.title;
var _45c=dojo.query("div.nowrapTabStrip.dijitTabContainerTop-tabs > div.dijitTabChecked.dijitChecked")[0];
var _45d=dojo.query("span.tabLabel",_45c)[0];
var _45e=dojo.query("div.nowrapTabStrip.dijitTabNoLayout > div.dijitTabChecked.dijitChecked",_459.domNode)[0];
var _45f=dojo.query("span.tabLabel",_45e)[0];
if(_45b=="undefined"){
return this.getPageTitleOnContentPanel();
}else{
if(_45b&&_45b!=""){
return _45b;
}else{
if(_45e){
return _45f.innerHTML;
}else{
return _45d.innerHTML;
}
}
}
},getPageTitleOnContentPanel:function(){
var _460;
var _461=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _462;
if(_461&&_461.length==1){
_462=_461[0].contentWindow.document;
_2ab.withDoc(_462,function(){
var _463=dojo.query("div.title h2 span:not(.hidden)");
if(_463&&_463.length==1&&_463[0].textContent){
_460=lang.trim(_463[0].textContent);
}
},this);
}
if(_460){
return _460;
}else{
return undefined;
}
},addClassToLastNodeInContentArea:function(){
var _464=_2b1("> div","content");
var _465=_464.length;
if(_465==0){
return "No need to add";
}
var _466=_464[--_465];
while(_2ae.contains(_466,"hidden-action-set")&&_466){
_466=_464[--_465];
}
_2ae.add(_466,"last-node");
},highContrastModeType:function(){
var _467=dojo.query("body.high-contrast")[0];
return _467;
},isRtlMode:function(){
var _468=dojo.query("body.rtl")[0];
return _468;
},processBidiContextual:function(_469){
_469.dir=bidi.prototype._checkContextual(_469.value);
},getCookie:function(name){
var dc=document.cookie;
var _46a=name+"=";
var _46b=dc.indexOf("; "+_46a);
if(_46b==-1){
_46b=dc.indexOf(_46a);
if(_46b!=0){
return null;
}
}else{
_46b+=2;
}
var end=document.cookie.indexOf(";",_46b);
if(end==-1){
end=dc.length;
}
return unescape(dc.substring(_46b+_46a.length,end));
},getHeadingTitleForScreenReader:function(_46c){
var _46d=curam.util.getTopmostWindow();
var _46e=_46d.dojo.global._tabTitle;
if(_46e){
curam.util.getHeadingTitle(_46e,_46c);
}else{
var _46f=_46d.dojo.subscribe("/curam/_tabTitle",function(_470){
if(_470){
curam.util.getHeadingTitle(_470,_46c);
}
_46d.dojo.unsubscribe(_46f);
});
}
},getHeadingTitle:function(_471,_472){
var _473=undefined;
if(_471&&_471.length>0){
_473=_471;
}else{
_473=_472;
}
var _474=dojo.query(".page-title-bar");
var _475=dojo.query("div h2",_474[0]);
if(_475){
var _476=dojo.query("span",_475[0]);
var span=undefined;
if(_476){
span=_476[0];
}
if(!span||(span&&(span.innerHTML.length==0))){
if(span){
attr.set(span,"class","hidden");
attr.set(span,"title",_473);
span.innerHTML=_473;
}else{
span=_2a9.create("span",{"class":"hidden","title":_473},_475[0]);
span.innerHTML=_473;
}
}
}
},_setupBrowserTabTitle:function(_477,_478,_479){
_477=_477.replace("\\n"," ");
curam.util._browserTabTitleData.staticTabTitle=_477;
curam.util._browserTabTitleData.separator=_478;
curam.util._browserTabTitleData.appNameFirst=_479;
},_browserTabTitleData:{},setBrowserTabTitle:function(_47a){
curam.debug.log("curam.util.setBrowserTabTitle(title = "+_47a+") called");
if(!_47a){
_47a=curam.util._findAppropriateDynamicTitle();
}
var _47b=curam.util._browserTabTitleData.staticTabTitle;
var _47c=curam.util._browserTabTitleData.separator;
var _47d=curam.util._browserTabTitleData.appNameFirst;
if(!_47b&&!_47c&&!_47d&&!_47a){
var _47e=document.querySelectorAll("head title")[0];
if(_47e){
document.title=_47e.text;
}
}else{
if(!_47a){
document.title=_47b;
}else{
if(_47b){
if(_47d){
document.title=_47b+_47c+_47a;
}else{
document.title=_47a+_47c+_47b;
}
}
}
}
},toggleCheckboxedSelectStyle:function(e,div){
if(e.checked){
div.classList.remove("unchecked");
div.classList.add("checked");
}else{
div.classList.remove("checked");
div.classList.add("unchecked");
}
},_findAppropriateDynamicTitle:function(){
var i;
var one;
var _47f=dojo.query("iframe.curam-active-modal").length;
if(_47f>1){
var _480=dojo.query("iframe.curam-active-modal")[0];
if(_480){
var _481=_480.contentDocument;
if(_481){
var _482=_481.head.getElementsByTagName("title")[0];
if(_482){
if(_482.innerHTML!=""){
one=_480.contentDocument.head.getElementsByTagName("title")[0].innerHTML;
}
}
}
}
}
if(one){
return one;
}
var two;
var _483=dojo.query("div.dijitVisible div.dijitVisible iframe.contentPanelFrame");
var _484;
if(_483&&_483.length==1){
_484=_483[0].contentWindow.document;
_2ab.withDoc(_484,function(){
var _485=dojo.query("div.title h2 span:not(.hidden)");
var _486=dom.byId("error-messages",_484);
if(_486){
two=_484.head.getElementsByTagName("title")[0].textContent;
}else{
if(_485&&_485.length==1&&_485[0].textContent){
two=lang.trim(_485[0].textContent);
curam.debug.log("(2) Page title for Content Panel = "+two);
}else{
if(_485&&_485.length>1){
two=this._checkForSubTitles(_485);
}else{
curam.debug.log("(2) Could not find page title for content panel: header = "+_485);
}
}
}
},this);
}else{
curam.debug.log("(2) Could not find iframeDoc for content panel: iframe = "+_483);
}
if(two){
return two;
}
var _487;
var _488=dojo.query("div.dijitVisible div.dijitVisible div.dijitVisible div.child-nav-items li.selected > div.link");
if(_488&&_488.length==1&&_488[0].textContent){
_487=lang.trim(_488[0].textContent);
curam.debug.log("(3) Selected navigation item = "+_487);
}else{
curam.debug.log("(3) Could not find selected navigation item: navItem = "+_488);
}
if(_487){
return _487;
}
var four;
var _489=dojo.query("div.dijitVisible div.dijitVisible div.navigation-bar-tabs span.tabLabel");
var _48a;
for(i=0;i<_489.length;i++){
if(_489[i].getAttribute("aria-selected")==="true"){
_48a=_489[i];
}
}
if(_48a&&_48a.textContent){
four=lang.trim(_48a.textContent);
curam.debug.log("(4) Selected navigation bar tab = "+four);
}else{
curam.debug.log("(4) Could not find selected navigation bar tab: selectedNavTab = "+_48a);
}
if(four){
return four;
}
var five;
var _48b=dojo.query("div.dijitVisible div.dijitVisible h1.detailsTitleText");
if(_48b&&_48b.length==1&&_48b[0].textContent){
five=lang.trim(_48b[0].textContent);
curam.debug.log("(5) Selected application tab title bar = "+five);
}else{
curam.debug.log("(5) Could not find selected application tab title bar: appTabTitleBar = "+_48b);
}
if(five){
return five;
}
var six;
var _48c=dojo.query("div.dijitTabInnerDiv div.dijitTabContent div span.tabLabel");
var _48d;
for(i=0;i<_48c.length;i++){
if(_48c[i].getAttribute("aria-selected")==="true"){
_48d=_48c[i];
break;
}
}
if(_48d&&_48d.textContent){
six=lang.trim(_48d.textContent);
curam.debug.log("(6) Selected section title = "+six);
}else{
curam.debug.log("(6) Could not find selected section title: sections = "+_48c);
}
if(six){
return six;
}
var _48e;
_483=dom.byId("curamUAIframe");
if(_483&&_483.contentWindow&&_483.contentWindow.document){
_484=_483.contentWindow.document;
_2ab.withDoc(_484,function(){
var _48f=dojo.query("div.page-header > div.page-title-bar > div.title > h2 > span");
if(_48f&&_48f.length==1&&_48f[0].textContent){
_48e=lang.trim(_48f[0].textContent);
curam.debug.log("(7) UIM page title for external application page = "+_48e);
}else{
curam.debug.log("(7) Could not find UIM page title for external application page: uimPageTitle = "+_48f);
}
},this);
}
if(_48e){
return _48e;
}
return undefined;
},_checkForSubTitles:function(_490){
var i;
if(!_490[0].textContent){
return undefined;
}
for(i=1;i<_490.length;i++){
var _491=_490[i].getAttribute("class");
if(_491.indexOf("sub-title")===-1||!_490[i].textContent){
curam.debug.log("(1) Failed to construct title from content panel page title. Not all header element spans had 'sub-title' class.");
for(i=0;i<_490.length;i++){
curam.debug.log(_490[i]);
}
return undefined;
}
}
var ret=_490[0].textContent;
for(i=1;i<_490.length;i++){
ret+=_490[i].textContent;
}
return ret;
},_addContextToWidgetForScreenReader:function(_492){
var _493=false;
var _494=0;
var _495=dojo.query(".training-details-list");
if(_495.length==1){
var _496=_495[0].parentElement;
var _497=dojo.query("div.bx--cluster",_496);
var _498=Array.prototype.indexOf.call(_497,_495[0]);
if(_498>=0){
for(var i=_498;i>=0;i--){
if(dojo.query("h3",_497[i]).length==1){
_493=true;
_494=i;
break;
}
}
}
if(_493){
var _499=dojo.query("h3.bx--accordion__title",_497[_494]);
if(_499.length==1){
var _49a=_499[0].className+"_id";
attr.set(_499[0],"id",_49a);
var _49b=dojo.byId(_492).parentElement;
attr.set(_49b,"aria-labelledby",_49a);
attr.set(_49b,"role","region");
}
}
}
},setParentFocusByChild:function(_49c){
var win=curam.util.UimDialog._getDialogFrameWindow(_49c);
if(win){
var _49d=curam.dialog.getParentWindow(win);
if(_49d){
_49d.focus();
}
}
},toClipboard:function(_49e){
try{
navigator.clipboard.writeText(_49e);
}
catch(err){
console.warn("Failed to copy into the clipboard.");
}
if(dojo.getObject("curam.dialog",false)!=null){
var pw=curam.dialog.getParentWindow(window);
pw&&pw.dojo.publish("/curam/clip/selected",[_49e]);
}
return false;
},removeTopScrollForIos:function(){
if(has("ios")){
window.document.body.scrollTop=0;
}
},insertAriaLiveLabelRecordSearchItem:function(_49f){
var span=dojo.query("[data-search-page]")[0];
if(span){
span.setAttribute("aria-live",has("ios")?"polite":"assertive");
setTimeout(function(){
var _4a0=span.firstChild.nodeValue;
var _4a1=_4a0+_49f;
span.innerHTML=_4a1;
},10);
}
},removeSessionStorageProperty:function(_4a2){
sessionStorage.removeItem(_4a2);
},addLayoutStylingOnDateTimeWidgetOnZoom:function(){
var _4a3=dojo.query("table.input-cluster td.field table.date-time");
console.log("datetimetable from util.js: "+_4a3);
var _4a4=_4a3.length;
if(_4a3.length>0){
for(var i=0;i<_4a3.length;i++){
var _4a5=_4a3[i];
var _4a6=_4a5.parentNode.parentNode;
_4a6.setAttribute("class","date-time-exists");
}
}
},fileUploadOpenFileBrowser:function(e,_4a7){
if(e.keyCode==32||e.keyCode==13){
dom.byId(_4a7).click();
}
},setupControlledLists:function(){
var _4a8="curam.listControls",_4a9="curam.listTogglers";
var _4aa=_4ab(_4a8),_4ac=_4ab(_4a9),_4ad=[];
var _4ae=_4aa&&_2b1("*[data-control]"),_4af=_4ac&&_2b1("a[data-toggler]");
if(_4aa||_4ac){
for(var _4b0 in _4aa){
_4ae.filter(function(item){
return attr.get(item,"data-control")==_4b0;
}).forEach(function(_4b1,ix){
var c=dom.byId(_4b1),tr=_2b1(_4b1).closest("tr")[0];
!tr.controls&&(tr.controls=new Array());
tr.controls.push(c);
if(!tr.visited){
tr.visited=true;
_4aa[_4b0].push(tr);
}
});
var _4b2=_4ab(_4a8+"."+_4b0);
if(_4b2&&_4b2.length&&_4b2.length>0){
_4ad.push(_4b0);
}else{
_4de(_4a8+"."+_4b0,false);
}
}
if(_4af&&_4af.length>0){
for(var _4b0 in _4ac){
_4af.filter(function(item){
return attr.get(item,"data-toggler")==_4b0;
}).forEach(function(_4b3){
var tr=_2b1(_4b3).closest("tr")[0];
tr.hasToggler=_4b3;
tr.visited=true;
_4ac[_4b0].push(tr);
});
var _4b4=_4ab(_4a9+"."+_4b0);
if(_4b4&&_4b4.length&&_4b4.length>0){
(_4ad.indexOf(_4b0)==-1)&&_4ad.push(_4b0);
}else{
_4de(_4a9+"."+_4b0,false);
}
}
}
_4ad.forEach(function(_4b5){
var _4b6=_4ab(_4a8+"."+_4b5)||_4ab(_4a9+"."+_4b5);
cu.updateListControlReadings(_4b5,_4b6);
});
}
dojo.subscribe("curam/sort/earlyAware",function(_4b7){
cu.suppressPaginationUpdate=_4b7;
});
dojo.subscribe("curam/update/readings/sort",function(_4b8,rows){
if(!has("trident")){
cu.updateListActionReadings(_4b8);
cu.updateListControlReadings(_4b8,rows);
cu.suppressPaginationUpdate=false;
}else{
var _4b9=cu.getPageBreak(_4b8),_4ba=Math.ceil(rows.length/_4b9);
cu.listRangeUpdate(0,_4ba,_4b8,rows,_4b9);
}
});
dojo.subscribe("curam/update/readings/pagination",function(_4bb,_4bc){
_4de("curam.pageBreak."+_4bb,_4bc);
});
dojo.subscribe("curam/update/pagination/rows",function(_4bd,_4be){
cu.updateDeferred&&!cu.updateDeferred.isResolved()&&cu.updateDeferred.cancel("Superseeded");
if(cu.suppressPaginationUpdate&&cu.suppressPaginationUpdate==_4be){
return;
}
var _4bf=_4eb("curam.listTogglers."+_4be),_4c0=_4eb("curam.listControls."+_4be),lms=_4ab("curam.listMenus."+_4be),_4c1=lms&&(lms.length>0);
var _4c2=_4c0||_4bf;
if(!_4c2&&!_4c1){
return;
}
if(_4c2){
var _4c3=_4bd.filter(function(aRow){
return (!aRow.visited||!aRow.done)&&attr.has(aRow,"data-lix");
});
_4bf&&_4c3.forEach(function(aRow){
var tgl=_2b1("a[data-toggler]",aRow)[0];
aRow.hasToggler=tgl;
aRow.visited=true;
curam.listTogglers[_4be].push(aRow);
});
_4c0&&_4c3.forEach(function(aRow){
var _4c4=_2b1("*[data-control]",aRow),_4c5=new Array();
_4c4.forEach(function(cRef){
_4c5.push(dom.byId(cRef));
});
aRow.controls=_4c5;
curam.listControls[_4be].push(aRow);
aRow.visited=true;
});
var _4c6=_4c0?curam.listControls[_4be]:curam.listTogglers[_4be];
cu.updateListControlReadings(_4be,_4c6);
}
_4c1&&cu.updateListActionReadings(_4be);
});
},listRangeUpdate:function(_4c7,_4c8,_4c9,rows,psz){
if(_4c7==_4c8){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
return;
}
var def=cu.updateDeferred=new _2b2(function(_4ca){
cu.suppressPaginationUpdate=false;
cu.updateDeferred=null;
});
def.then(function(pNum){
cu.listRangeUpdate(pNum,_4c8,_4c9,rows,psz);
},function(err){
});
var _4cb=(_4c7===0)?0:200;
setTimeout(function(){
var _4cc=_4c7+1,_4cd=[_4c7*psz,(_4cc*psz)];
cu.updateListActionReadings(_4c9,_4cd);
cu.updateListControlReadings(_4c9,rows,_4cd);
def.resolve(_4cc);
},_4cb);
},updateListControlReadings:function(_4ce,_4cf,_4d0){
var c0,psz=cu.getPageBreak(_4ce),_4d1=cu.getStartShift(_4ce,_4cf[0]||false),_4d2=_4cf;
_4d0&&(_4d2=_4cf.slice(_4d0[0],_4d0[1]));
for(var rix in _4d2){
var aRow=_4d2[rix],_4d3=parseInt(attr.get(aRow,_4d4)),lx=(_4d3%psz)+_4d1,_4d5=aRow.controls;
if(!_4d5){
var _4d6=_2b1("*[data-control]",aRow),_4d7=new Array();
_4d6.forEach(function(cRef){
_4d7.push(dom.byId(cRef));
});
aRow.controls=_4d7;
_4d5=aRow.controls;
}
if(_4d5){
for(var cix in _4d5){
var crtl=_4d5[cix],ttl=crtl.textContent||false,_4d8=ttl?ttl+",":"";
if(crtl.nodeName=="A"){
var _4d9=_2b1("img",crtl)[0];
if(_4d9&&_2ae.contains(crtl,"ac first-action-control external-link")){
var _4da=attr.get(_4d9,"alt");
attr.set(crtl,_4db,_4da+","+[listcontrol.reading.anchors,lx].join(" "));
}else{
attr.set(crtl,_4db,_4d8+[listcontrol.reading.anchors,lx].join(" "));
}
}else{
attr.set(crtl,_4db,_4d8+[listcontrol.reading.selectors,lx].join(" "));
}
}
}
cu.updateToggler(aRow,lx);
aRow.done=true;
}
},initListActionReadings:function(_4dc){
var _4dd="curam.listMenus."+_4dc;
_4de(_4dd,[]);
dojo.subscribe("curam/listmenu/started",function(_4df,_4e0){
var tr=_2b1(_4df.containerNode).closest("tr")[0],lix=parseInt(attr.get(tr,_4d4)),lx=(lix%cu.getPageBreak(_4e0))+cu.getStartShift(_4e0,tr);
_4df.set({"belongsTo":tr,"aria-labelledBy":"","aria-label":[listcontrol.reading.menus,lx].join(" ")});
_4ab(_4dd).push(_4df);
cu.updateToggler(tr,lx);
});
},updateToggler:function(_4e1,_4e2){
_4e1.hasToggler&&attr.set(_4e1.hasToggler,_4db,[listcontrol.reading.togglers,_4e2].join(" "));
},updateListActionReadings:function(_4e3,_4e4){
var _4e5=_4ab("curam.listMenus."+_4e3),psz=cu.getPageBreak(_4e3),_4e6=false,_4e7=_4e5;
_4e4&&(_4e7=_4e5.slice(_4e4[0],_4e4[1]));
for(var ix in _4e7){
var _4e8=_4e7[ix],tr=_4e8.belongsTo,lix=parseInt(attr.get(tr,_4d4)),_4e6=_4e6||cu.getStartShift(_4e3,tr),_4e9=(lix%psz)+_4e6;
_4e8.set(_4db,[listcontrol.reading.menus,_4e9].join(" "));
cu.updateToggler(tr,_4e9);
tr.done=true;
}
},getPageBreak:function(_4ea){
if(!_4eb("curam.list.isPaginated."+_4ea)){
return 1000;
}
if(_4ab("curam.shortlist."+_4ea)){
return 1000;
}
var psz=_4ab("curam.pageBreak."+_4ea)||_4ab("curam.pagination.defaultPageSize")||1000;
return psz;
},getStartShift:function(_4ec,_4ed){
if(!_4ed){
return 2;
}
var _4ee="curam.listHeaderStep."+_4ec,_4ef=_4ab(_4ee);
if(_4ef){
return _4ef;
}
_4de(_4ee,2);
var _4f0=_2b1(_4ed).closest("table");
if(_4f0.length==0){
return 2;
}
var _4f1=_4f0.children("thead")[0];
!_4f1&&_4de(_4ee,1);
return curam.listHeaderStep[_4ec];
},extendXHR:function(){
var _4f2=XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open=function(){
this.addEventListener("load",function(){
if(typeof (Storage)!=="undefined"){
var _4f3=this.getResponseHeader("sessionExpiry");
sessionStorage.setItem("sessionExpiry",_4f3);
}
});
_4f2.apply(this,arguments);
};
},suppressPaginationUpdate:false,updateDeferred:null});
var cu=curam.util,_4ab=dojo.getObject,_4de=dojo.setObject,_4eb=dojo.exists,_4db="aria-label",_4d4="data-lix";
return curam.util;
});
},"dojo/regexp":function(){
define(["./_base/kernel","./_base/lang"],function(dojo,lang){
var _4f4={};
lang.setObject("dojo.regexp",_4f4);
_4f4.escapeString=function(str,_4f5){
return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+\-^])/g,function(ch){
if(_4f5&&_4f5.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
_4f4.buildGroupRE=function(arr,re,_4f6){
if(!(arr instanceof Array)){
return re(arr);
}
var b=[];
for(var i=0;i<arr.length;i++){
b.push(re(arr[i]));
}
return _4f4.group(b.join("|"),_4f6);
};
_4f4.group=function(_4f7,_4f8){
return "("+(_4f8?"?:":"")+_4f7+")";
};
return _4f4;
});
},"dojo/date/locale":function(){
define(["../_base/lang","../_base/array","../date","../cldr/supplemental","../i18n","../regexp","../string","../i18n!../cldr/nls/gregorian","module"],function(lang,_4f9,date,_4fa,i18n,_4fb,_4fc,_4fd,_4fe){
var _4ff={};
lang.setObject(_4fe.id.replace(/\//g,"."),_4ff);
function _500(_501,_502,_503,_504){
return _504.replace(/([a-z])\1*/ig,function(_505){
var s,pad,c=_505.charAt(0),l=_505.length,_506=["abbr","wide","narrow"];
switch(c){
case "G":
s=_502[(l<4)?"eraAbbr":"eraNames"][_501.getFullYear()<0?0:1];
break;
case "y":
s=_501.getFullYear();
switch(l){
case 1:
break;
case 2:
if(!_503.fullYear){
s=String(s);
s=s.substr(s.length-2);
break;
}
default:
pad=true;
}
break;
case "Q":
case "q":
s=Math.ceil((_501.getMonth()+1)/3);
pad=true;
break;
case "M":
case "L":
var m=_501.getMonth();
if(l<3){
s=m+1;
pad=true;
}else{
var _507=["months",c=="L"?"standAlone":"format",_506[l-3]].join("-");
s=_502[_507][m];
}
break;
case "w":
var _508=0;
s=_4ff._getWeekOfYear(_501,_508);
pad=true;
break;
case "d":
s=_501.getDate();
pad=true;
break;
case "D":
s=_4ff._getDayOfYear(_501);
pad=true;
break;
case "e":
case "c":
var d=_501.getDay();
if(l<2){
s=(d-_4fa.getFirstDayOfWeek(_503.locale)+8)%7;
break;
}
case "E":
d=_501.getDay();
if(l<3){
s=d+1;
pad=true;
}else{
var _509=["days",c=="c"?"standAlone":"format",_506[l-3]].join("-");
s=_502[_509][d];
}
break;
case "a":
var _50a=_501.getHours()<12?"am":"pm";
s=_503[_50a]||_502["dayPeriods-format-wide-"+_50a];
break;
case "h":
case "H":
case "K":
case "k":
var h=_501.getHours();
switch(c){
case "h":
s=(h%12)||12;
break;
case "H":
s=h;
break;
case "K":
s=(h%12);
break;
case "k":
s=h||24;
break;
}
pad=true;
break;
case "m":
s=_501.getMinutes();
pad=true;
break;
case "s":
s=_501.getSeconds();
pad=true;
break;
case "S":
s=Math.round(_501.getMilliseconds()*Math.pow(10,l-3));
pad=true;
break;
case "v":
case "z":
s=_4ff._getZone(_501,true,_503);
if(s){
break;
}
l=4;
case "Z":
var _50b=_4ff._getZone(_501,false,_503);
var tz=[(_50b<=0?"+":"-"),_4fc.pad(Math.floor(Math.abs(_50b)/60),2),_4fc.pad(Math.abs(_50b)%60,2)];
if(l==4){
tz.splice(0,0,"GMT");
tz.splice(3,0,":");
}
s=tz.join("");
break;
default:
throw new Error("dojo.date.locale.format: invalid pattern char: "+_504);
}
if(pad){
s=_4fc.pad(s,l);
}
return s;
});
};
_4ff._getZone=function(_50c,_50d,_50e){
if(_50d){
return date.getTimezoneName(_50c);
}else{
return _50c.getTimezoneOffset();
}
};
_4ff.format=function(_50f,_510){
_510=_510||{};
var _511=i18n.normalizeLocale(_510.locale),_512=_510.formatLength||"short",_513=_4ff._getGregorianBundle(_511),str=[],_514=lang.hitch(this,_500,_50f,_513,_510);
if(_510.selector=="year"){
return _515(_513["dateFormatItem-yyyy"]||"yyyy",_514);
}
var _516;
if(_510.selector!="date"){
_516=_510.timePattern||_513["timeFormat-"+_512];
if(_516){
str.push(_515(_516,_514));
}
}
if(_510.selector!="time"){
_516=_510.datePattern||_513["dateFormat-"+_512];
if(_516){
str.push(_515(_516,_514));
}
}
return str.length==1?str[0]:_513["dateTimeFormat-"+_512].replace(/\'/g,"").replace(/\{(\d+)\}/g,function(_517,key){
return str[key];
});
};
_4ff.regexp=function(_518){
return _4ff._parseInfo(_518).regexp;
};
_4ff._parseInfo=function(_519){
_519=_519||{};
var _51a=i18n.normalizeLocale(_519.locale),_51b=_4ff._getGregorianBundle(_51a),_51c=_519.formatLength||"short",_51d=_519.datePattern||_51b["dateFormat-"+_51c],_51e=_519.timePattern||_51b["timeFormat-"+_51c],_51f;
if(_519.selector=="date"){
_51f=_51d;
}else{
if(_519.selector=="time"){
_51f=_51e;
}else{
_51f=_51b["dateTimeFormat-"+_51c].replace(/\{(\d+)\}/g,function(_520,key){
return [_51e,_51d][key];
});
}
}
var _521=[],re=_515(_51f,lang.hitch(this,_522,_521,_51b,_519));
return {regexp:re,tokens:_521,bundle:_51b};
};
_4ff.parse=function(_523,_524){
var _525=/[\u200E\u200F\u202A\u202E]/g,info=_4ff._parseInfo(_524),_526=info.tokens,_527=info.bundle,re=new RegExp("^"+info.regexp.replace(_525,"")+"$",info.strict?"":"i"),_528=re.exec(_523&&_523.replace(_525,""));
if(!_528){
return null;
}
var _529=["abbr","wide","narrow"],_52a=[1970,0,1,0,0,0,0],amPm="",_52b=_4f9.every(_528,function(v,i){
if(!i){
return true;
}
var _52c=_526[i-1],l=_52c.length,c=_52c.charAt(0);
switch(c){
case "y":
if(l!=2&&_524.strict){
_52a[0]=v;
}else{
if(v<100){
v=Number(v);
var year=""+new Date().getFullYear(),_52d=year.substring(0,2)*100,_52e=Math.min(Number(year.substring(2,4))+20,99);
_52a[0]=(v<_52e)?_52d+v:_52d-100+v;
}else{
if(_524.strict){
return false;
}
_52a[0]=v;
}
}
break;
case "M":
case "L":
if(l>2){
var _52f=_527["months-"+(c=="L"?"standAlone":"format")+"-"+_529[l-3]].concat();
if(!_524.strict){
v=v.replace(".","").toLowerCase();
_52f=_4f9.map(_52f,function(s){
return s.replace(".","").toLowerCase();
});
}
v=_4f9.indexOf(_52f,v);
if(v==-1){
return false;
}
}else{
v--;
}
_52a[1]=v;
break;
case "E":
case "e":
case "c":
var days=_527["days-"+(c=="c"?"standAlone":"format")+"-"+_529[l-3]].concat();
if(!_524.strict){
v=v.toLowerCase();
days=_4f9.map(days,function(d){
return d.toLowerCase();
});
}
v=_4f9.indexOf(days,v);
if(v==-1){
return false;
}
break;
case "D":
_52a[1]=0;
case "d":
_52a[2]=v;
break;
case "a":
var am=_524.am||_527["dayPeriods-format-wide-am"],pm=_524.pm||_527["dayPeriods-format-wide-pm"];
if(!_524.strict){
var _530=/\./g;
v=v.replace(_530,"").toLowerCase();
am=am.replace(_530,"").toLowerCase();
pm=pm.replace(_530,"").toLowerCase();
}
if(_524.strict&&v!=am&&v!=pm){
return false;
}
amPm=(v==pm)?"p":(v==am)?"a":"";
break;
case "K":
if(v==24){
v=0;
}
case "h":
case "H":
case "k":
if(v>23){
return false;
}
_52a[3]=v;
break;
case "m":
_52a[4]=v;
break;
case "s":
_52a[5]=v;
break;
case "S":
_52a[6]=v;
}
return true;
});
var _531=+_52a[3];
if(amPm==="p"&&_531<12){
_52a[3]=_531+12;
}else{
if(amPm==="a"&&_531==12){
_52a[3]=0;
}
}
var _532=new Date(_52a[0],_52a[1],_52a[2],_52a[3],_52a[4],_52a[5],_52a[6]);
if(_524.strict){
_532.setFullYear(_52a[0]);
}
var _533=_526.join(""),_534=_533.indexOf("d")!=-1,_535=_533.indexOf("M")!=-1;
if(!_52b||(_535&&_532.getMonth()>_52a[1])||(_534&&_532.getDate()>_52a[2])){
return null;
}
if((_535&&_532.getMonth()<_52a[1])||(_534&&_532.getDate()<_52a[2])){
_532=date.add(_532,"hour",1);
}
return _532;
};
function _515(_536,_537,_538,_539){
var _53a=function(x){
return x;
};
_537=_537||_53a;
_538=_538||_53a;
_539=_539||_53a;
var _53b=_536.match(/(''|[^'])+/g),_53c=_536.charAt(0)=="'";
_4f9.forEach(_53b,function(_53d,i){
if(!_53d){
_53b[i]="";
}else{
_53b[i]=(_53c?_538:_537)(_53d.replace(/''/g,"'"));
_53c=!_53c;
}
});
return _539(_53b.join(""));
};
function _522(_53e,_53f,_540,_541){
_541=_4fb.escapeString(_541);
if(!_540.strict){
_541=_541.replace(" a"," ?a");
}
return _541.replace(/([a-z])\1*/ig,function(_542){
var s,c=_542.charAt(0),l=_542.length,p2="",p3="";
if(_540.strict){
if(l>1){
p2="0"+"{"+(l-1)+"}";
}
if(l>2){
p3="0"+"{"+(l-2)+"}";
}
}else{
p2="0?";
p3="0{0,2}";
}
switch(c){
case "y":
s="\\d{2,4}";
break;
case "M":
case "L":
s=(l>2)?"\\S+?":"1[0-2]|"+p2+"[1-9]";
break;
case "D":
s="[12][0-9][0-9]|3[0-5][0-9]|36[0-6]|"+p2+"[1-9][0-9]|"+p3+"[1-9]";
break;
case "d":
s="3[01]|[12]\\d|"+p2+"[1-9]";
break;
case "w":
s="[1-4][0-9]|5[0-3]|"+p2+"[1-9]";
break;
case "E":
case "e":
case "c":
s=".+?";
break;
case "h":
s="1[0-2]|"+p2+"[1-9]";
break;
case "k":
s="1[01]|"+p2+"\\d";
break;
case "H":
s="1\\d|2[0-3]|"+p2+"\\d";
break;
case "K":
s="1\\d|2[0-4]|"+p2+"[1-9]";
break;
case "m":
case "s":
s="[0-5]\\d";
break;
case "S":
s="\\d{"+l+"}";
break;
case "a":
var am=_540.am||_53f["dayPeriods-format-wide-am"],pm=_540.pm||_53f["dayPeriods-format-wide-pm"];
s=am+"|"+pm;
if(!_540.strict){
if(am!=am.toLowerCase()){
s+="|"+am.toLowerCase();
}
if(pm!=pm.toLowerCase()){
s+="|"+pm.toLowerCase();
}
if(s.indexOf(".")!=-1){
s+="|"+s.replace(/\./g,"");
}
}
s=s.replace(/\./g,"\\.");
break;
default:
s=".*";
}
if(_53e){
_53e.push(_542);
}
return "("+s+")";
}).replace(/[\xa0 ]/g,"[\\s\\xa0]");
};
var _543=[];
var _544={};
_4ff.addCustomFormats=function(_545,_546){
_543.push({pkg:_545,name:_546});
_544={};
};
_4ff._getGregorianBundle=function(_547){
if(_544[_547]){
return _544[_547];
}
var _548={};
_4f9.forEach(_543,function(desc){
var _549=i18n.getLocalization(desc.pkg,desc.name,_547);
_548=lang.mixin(_548,_549);
},this);
return _544[_547]=_548;
};
_4ff.addCustomFormats(_4fe.id.replace(/\/date\/locale$/,".cldr"),"gregorian");
_4ff.getNames=function(item,type,_54a,_54b){
var _54c,_54d=_4ff._getGregorianBundle(_54b),_54e=[item,_54a,type];
if(_54a=="standAlone"){
var key=_54e.join("-");
_54c=_54d[key];
if(_54c[0]==1){
_54c=undefined;
}
}
_54e[1]="format";
return (_54c||_54d[_54e.join("-")]).concat();
};
_4ff.isWeekend=function(_54f,_550){
var _551=_4fa.getWeekend(_550),day=(_54f||new Date()).getDay();
if(_551.end<_551.start){
_551.end+=7;
if(day<_551.start){
day+=7;
}
}
return day>=_551.start&&day<=_551.end;
};
_4ff._getDayOfYear=function(_552){
return date.difference(new Date(_552.getFullYear(),0,1,_552.getHours()),_552)+1;
};
_4ff._getWeekOfYear=function(_553,_554){
if(arguments.length==1){
_554=0;
}
var _555=new Date(_553.getFullYear(),0,1).getDay(),adj=(_555-_554+7)%7,week=Math.floor((_4ff._getDayOfYear(_553)+adj-1)/7);
if(_555==_554){
week++;
}
return week;
};
return _4ff;
});
},"dijit/Destroyable":function(){
define(["dojo/_base/array","dojo/aspect","dojo/_base/declare"],function(_556,_557,_558){
return _558("dijit.Destroyable",null,{destroy:function(_559){
this._destroyed=true;
},own:function(){
var _55a=["destroyRecursive","destroy","remove"];
_556.forEach(arguments,function(_55b){
var _55c;
var odh=_557.before(this,"destroy",function(_55d){
_55b[_55c](_55d);
});
var hdhs=[];
function _55e(){
odh.remove();
_556.forEach(hdhs,function(hdh){
hdh.remove();
});
};
if(_55b.then){
_55c="cancel";
_55b.then(_55e,_55e);
}else{
_556.forEach(_55a,function(_55f){
if(typeof _55b[_55f]==="function"){
if(!_55c){
_55c=_55f;
}
hdhs.push(_557.after(_55b,_55f,_55e,true));
}
});
}
},this);
return arguments;
}});
});
},"curam/cdsl/Struct":function(){
define(["dojo/_base/declare","dojo/_base/lang","./_base/_StructBase"],function(_560,lang,_561){
var _562=_560(_561,{constructor:function(data){
lang.mixin(this,this._data);
}});
return _562;
});
},"curam/cdsl/_base/FacadeMethodResponse":function(){
define(["dojo/_base/declare","curam/cdsl/Struct","dojo/json","dojo/string"],function(_563,_564,json,_565){
var _566="for(;;);";
var _567=function(_568,_569){
var ret=[],_56a=_569?Array(_569+1).join("  "):"";
dojo.forEach(_568,function(e){
ret.push(_565.substitute("${indent}Type: ${type}\n"+"${indent}Message: ${msg}\n"+"${indent}Stack trace:\n"+"${indent}  ${stackTrace}",{type:e.type,msg:e.message,stackTrace:e.stackTrace,indent:_56a}));
if(e.nestedError){
ret.push("\n-- nested error --");
ret.push(_567([e.nestedError],_569?_569+1:1));
}
});
return ret.join("\n");
};
var _56b=_563(null,{_request:null,_data:null,_metadataRegistry:null,constructor:function(_56c,_56d,_56e){
if(!_56c||!_56d){
throw new Error("Missing parameter.");
}
if(typeof _56d=="string"){
this._data=json.parse(_56d.substr(_566.length,_56d.length));
}else{
if(typeof _56d=="object"){
this._data=_56d;
}else{
throw new Error("Wrong parameter type: "+typeof _56c+", "+typeof _56d);
}
}
this._request=_56c;
this._metadataRegistry=_56e;
},returnValue:function(){
return new _564(this._data.data,{bareInput:true,fixups:this._data.metadata&&this._data.metadata.fixups?this._data.metadata.fixups:null,metadataRegistry:this._metadataRegistry,dataAdapter:this._request.dataAdapter()});
},failed:function(){
return this._data.code!==0;
},getError:function(){
var _56f=this._data.errors;
if(_56f){
var e=new Error("Server returned "+_56f.length+(_56f.length==1?" error":" errors")+".");
e.errors=_56f;
e.toString=function(){
return _567(_56f);
};
return e;
}
return null;
},hasCodetables:function(){
return this._data.metadata&&this._data.metadata.codetables&&this._data.metadata.codetables.length>0;
},getCodetablesData:function(){
return this._data.metadata.codetables;
},devMode:function(){
var dm=false;
if(this._data.metadata&&this._data.metadata.devMode){
dm=(this._data.metadata.devMode===true);
}
return dm;
},request:function(){
return this._request;
}});
return _56b;
});
},"curam/cdsl/_base/_StructBase":function(){
define(["dojo/_base/declare","dojo/_base/lang","dojo/json","curam/cdsl/types/FrequencyPattern"],function(_570,lang,json,_571){
var _572={bareInput:false,fixups:null,metadataRegistry:null,dataAdapter:null};
var _573=function(_574){
var o=lang.clone(_572);
return lang.mixin(o,_574);
};
var _575={onRequest:{onItem:function(path,data){
return data;
},onStruct:function(_576){
},},onResponse:{onItem:function(path,data){
return data;
},onStruct:function(_577){
},}};
var _578=function(frag,_579){
if(frag){
return frag+"."+_579;
}
return _579;
};
var _57a=_570(null,{_data:null,_converter:null,_dataAdapter:null,constructor:function(data,opts){
if(!data){
throw new Error("Missing parameter.");
}
if(typeof data!=="object"){
throw new Error("Wrong parameter type: "+typeof data);
}
var _57b=_573(opts);
if(!_57b.bareInput){
this._data=this._typedToBare(data);
}else{
this.setDataAdapter(_57b.dataAdapter);
this._data=this._bareToTyped(data);
if(_57b.fixups){
this._applyFixUps(_57b.fixups,this._data,_57b.metadataRegistry);
}
}
},_applyFixUps:function(_57c,data,_57d){
dojo.forEach(_57c,function(item,_57e){
var path=_57c[_57e].path;
var type=_57c[_57e].type;
this._processFixUp(data,path,this._getTransformFunction(type,_57d));
},this);
},_processFixUp:function(data,path,_57f){
if(path.length==1){
data[path[0]]=_57f(data[path[0]]);
return;
}else{
if(lang.isArray(data[path[0]])){
dojo.forEach(data[path[0]],function(item,_580){
this._processFixUp(item,path.slice(1,path.length),_57f);
},this);
}else{
this._processFixUp(data[path[0]],path.slice(1,path.length),_57f);
}
}
},_getTransformFunction:function(type,_581){
if(type[0]==="frequencypattern"){
return function(data){
return new _571(data.code,data.description);
};
}else{
if(type[0]==="datetime"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="date"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="time"){
return function(data){
return new Date(data);
};
}else{
if(type[0]==="codetable"){
if(type.length<2){
throw new Error("Missing codetable name, type specified is: "+type);
}
return function(data){
var _582=_581.codetables()[type[1]];
if(_582){
return _582.getItem(data);
}else{
throw new Error("Codetable does not exist: codetable name="+type[1]);
}
};
}else{
throw new Error("Unsupported type: "+type);
}
}
}
}
}
},toJson:function(){
return json.stringify(this.getData());
},getData:function(){
for(var name in this._data){
this._data[name]=this[name];
}
return this._typedToBare(this._data);
},_bareToTyped:function(data,path){
if(lang.isObject(data)){
var _583={};
this._applyResponseStructAdapter(data);
for(var prop in data){
if(lang.isArray(data[prop])){
_583[prop]=[];
for(var i=0;i<data[prop].length;i++){
_583[prop].push(this._bareToTyped(data[prop][i],_578(path,prop+"["+i+"]")));
}
}else{
if(typeof data[prop]==="object"){
_583[prop]=this._bareToTyped(data[prop],_578(path,prop));
}else{
_583[prop]=data[prop];
}
}
var _584=_578(path,prop);
_583[prop]=this._applyResponseDataAdapter(_584,_583[prop]);
}
return _583;
}
return this._applyResponseDataAdapter(path,data);
},_typedToBare:function(data,path){
if(lang.isObject(data)){
var _585={};
for(var prop in data){
if(data.hasOwnProperty(prop)&&"_data"!==prop&&"_dataAdapter"!==prop&&"_inherited"!==prop&&"_converter"!==prop){
if(lang.isArray(data[prop])){
_585[prop]=[];
for(var i=0;i<data[prop].length;i++){
_585[prop].push(this._typedToBare(data[prop][i],_578(path,prop+"["+i+"]")));
}
}else{
if(data[prop].getDescription&&data[prop].getCode){
_585[prop]=data[prop].getCode();
}else{
if(data[prop].getTime){
_585[prop]=data[prop].getTime();
}else{
if(typeof data[prop]==="object"){
_585[prop]=this._typedToBare(data[prop],_578(path,prop));
}else{
_585[prop]=data[prop];
}
}
}
}
var _586=_578(path,prop);
_585[prop]=this._applyRequestDataAdapter(_586,_585[prop]);
}
}
this._applyRequestStructAdapter(_585);
return _585;
}
return this._applyRequestDataAdapter(path,data);
},setDataAdapter:function(_587){
if(_587){
var a=lang.clone(_575);
if(_587.onRequest&&_587.onRequest.onItem){
a.onRequest.onItem=_587.onRequest.onItem;
}
if(_587.onRequest&&_587.onRequest.onStruct){
a.onRequest.onStruct=_587.onRequest.onStruct;
}
if(_587.onResponse&&_587.onResponse.onItem){
a.onResponse.onItem=_587.onResponse.onItem;
}
if(_587.onResponse&&_587.onResponse.onStruct){
a.onResponse.onStruct=_587.onResponse.onStruct;
}
this._dataAdapter=a;
}else{
this._dataAdapter=null;
}
},_applyRequestDataAdapter:function(path,_588){
if(this._dataAdapter){
return this._dataAdapter.onRequest.onItem(path,_588);
}
return _588;
},_applyResponseDataAdapter:function(path,_589){
if(this._dataAdapter){
return this._dataAdapter.onResponse.onItem(path,_589);
}
return _589;
},_applyRequestStructAdapter:function(_58a){
if(this._dataAdapter){
this._dataAdapter.onRequest.onStruct(_58a);
}
},_applyResponseStructAdapter:function(_58b){
if(this._dataAdapter){
this._dataAdapter.onResponse.onStruct(_58b);
}
}});
return _57a;
});
},"curam/cdsl/util/Preferences":function(){
define(["dojo/_base/declare","dojo/_base/lang","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/request/CuramService","curam/cdsl/Struct"],function(_58c,lang,_58d,_58e,_58f){
var _590=_58c(null,{_connection:null,constructor:function(_591){
if(!_591){
throw new Error("Missing parameter.");
}
if(typeof _591!=="object"){
throw new Error("Wrong parameter type: "+typeof _591);
}
this._connection=_591;
},getPreference:function(name){
return this._connection.preferences().getPreference(name);
},getPreferenceNames:function(){
return this._connection.preferences().getPreferenceNames();
},loadPreferences:function(){
var _592=new _58e(this._connection);
var _593=new _58d("CuramService","getPreferences");
return _592.call([_593]).then(lang.hitch(this,function(data){
var _594=data[0].getData();
for(prop in _594){
this._connection.preferences().addPreference(prop,_594[prop]);
}
return this;
}));
}});
return _590;
});
},"*now":function(r){
r(["dojo/i18n!*preload*dojo/nls/cdsl*[\"ar\",\"ca\",\"cs\",\"da\",\"de\",\"el\",\"en-gb\",\"en-us\",\"es-es\",\"fi-fi\",\"fr-fr\",\"he-il\",\"hu\",\"it-it\",\"ja-jp\",\"ko-kr\",\"nl-nl\",\"nb\",\"pl\",\"pt-br\",\"pt-pt\",\"ru\",\"sk\",\"sl\",\"sv\",\"th\",\"tr\",\"zh-tw\",\"zh-cn\",\"ROOT\"]"]);
}}});
define("dojo/cdsl",[],1);
