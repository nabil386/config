//>>built
define("curam/date/locale",["curam/define","dojo/_base/lang","dojo/date/locale"],function(_1,_2,_3){
var _4=_3._getGregorianBundle;
function _5(_6){
var _7=_4(_6);
if(LOCALIZED_MONTH_NAMES){
_7["months-format-abbr"]=LOCALIZED_SHORT_MONTH_NAMES;
_7["months-format-wide"]=LOCALIZED_MONTH_NAMES;
}
return _7;
};
_1.singleton("curam.date.locale",{});
_2.mixin(curam.date.locale,_3);
curam.date.locale.format=function(_8,_9){
_3._getGregorianBundle=_5;
var _a=_3.format(_8,_9);
_3._getGregorianBundle=_4;
return _a;
};
curam.date.locale.parseDate=function(_b){
var _c=new Date(_b);
return new Date(_c.getTime()+_c.getTimezoneOffset()*60000);
};
curam.date.locale.parse=function(_d,_e){
_3._getGregorianBundle=_5;
var _f=_3.parse(_d,_e);
_3._getGregorianBundle=_4;
return _f;
};
return curam.date.locale;
});
