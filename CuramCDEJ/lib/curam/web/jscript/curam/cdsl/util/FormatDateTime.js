//>>built
define("curam/cdsl/util/FormatDateTime",["dojo/_base/declare","dojo/_base/lang","dojo/date/locale","dojo/date"],function(_1,_2,_3,_4){
return {_checkPreferences:function(_5,_6,_7){
if(_5===undefined){
throw new Error("The dateFormat preference was not available");
}
if(_6===undefined){
throw new Error("The timeFormat preference was not available");
}
if(_7===undefined){
throw new Error("The timezone preference was not available");
}
},formatDateTime:function(_8,_9){
var _a=_9.getPreference("dateFormat");
var _b=_9.getPreference("timeFormat");
var _c=_9.getPreference("timezone");
this._checkPreferences(_a,_b,_c);
var _d=_8.getTimezoneOffset()*60000;
var _e=Date.parse(_8)+_d;
var _f=new Date(_e-(_c*60000));
return _3.format(_f,{"datePattern":_a,"timePattern":_b});
},formatDate:function(_10,_11){
var _12=_11.getPreference("dateFormat");
var _13=_11.getPreference("timezone");
this._checkPreferences(_12,"ignore",_13);
var _14=_10.getTimezoneOffset()*60000;
var _15=Date.parse(_10)+_14;
var _16=new Date(_15);
return _3.format(_16,{"datePattern":_12,"selector":"date"});
},_formatTime:function(_17,_18,_19){
var _1a=_18.getPreference("timeFormat");
var _1b=_18.getPreference("timezone");
this._checkPreferences("ignore",_1a,_1b);
var _1c=_17.getTimezoneOffset()*60000;
var _1d=Date.parse(_17)+_1c;
if(_19!=undefined&&_19){
var _1e=new Date(_1d-(_1b*60000));
return _3.format(_1e,{"timePattern":_1a,"selector":"time"});
}else{
return _3.format(new Date(_1d),{"timePattern":_1a,"selector":"time"});
}
}};
});
