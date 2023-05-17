//>>built
define("curam/date",["curam/define","dojo/date","curam/date/locale","dojo/date/stamp"],function(_1,_2,_3,_4){
curam.define.singleton("curam.date",{testLocale:null,isDate:function(_5,_6){
return (curam.date.getDateFromFormat(_5,_6)!=0);
},compareDates:function(d1,_7,d2,_8){
var d1=curam.date.getDateFromFormat(d1,_7);
if(d1==0){
return -1;
}
var d2=curam.date.getDateFromFormat(d2,_8);
if(d2==0){
return -1;
}
return _2.compare(d1,d2,"date");
},formatDate:function(d,_9){
var _a=_3.format(d,{selector:"date",datePattern:_9,locale:curam.date.getLocale()});
return _a;
},getDateFromFormat:function(_b,_c){
var _d=_3.parse(_b,{selector:"date",datePattern:_c,locale:curam.date.getLocale()});
return (_d==null)?"0":_d;
},ISO8601StringToDate:function(_e){
return _4.fromISOString(_e);
},getLocale:function(){
var _f=(typeof jsL!="undefined"&&jsL)?jsL:(curam.config?curam.config.locale:null);
return _f||curam.date.testLocale||"en";
}});
return curam.date;
});
