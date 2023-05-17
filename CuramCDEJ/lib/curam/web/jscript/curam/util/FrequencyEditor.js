//>>built
define("curam/util/FrequencyEditor",["dojo/dom","dojo/dom-style","dojo/dom-class","dojo/dom-attr","dojo/dom-construct","dojo/query","cm/_base/_dom","curam/util","curam/define","curam/debug","dojo/has","dojo/sniff"],function(_1,_2,_3,_4,_5,_6,cm,_7,_8,_9,_a){
_8.singleton("curam.util.FrequencyEditor",{CORRECTOR:1,DAILY_FREQUENCY:0,WEEKLY_FREQUENCY:1,MONTHLY_FREQUENCY:2,YEARLY_FREQUENCY:3,BIMONTHLY_FREQUENCY:4,EVERY_DAY_MASK:201,EVERY_WEEKDAY_MASK:202,EVERY_WEEKENDDAY_MASK:203,MON_MASK:1,TUE_MASK:2,WED_MASK:4,THU_MASK:8,FRI_MASK:16,SAT_MASK:32,SUN_MASK:64,daysInMonth:[31,28,31,30,31,30,31,31,30,31,30,31],EVERY_DAY:0,EVERY_WEEKDAY:1,MON:0,TUE:1,WED:2,THU:3,FRI:4,SAT:5,SUN:6,START_DATE:0,MONTH_DAY_NUM:1,MONTH_SEL_DAY:2,DAY_NUM:0,SEL_DAY:1,SEL_MONTH_DAY_NUM:0,SEL_MONTH_SEL_DAY:1,domSelector:(_a("ios")||_a("android")?dojo:dijit),allowableCharsForNumeric:["1","2","3","4","5","6","7","8","9","0",dojo.keys.LEFT_ARROW,dojo.keys.RIGHT_ARROW,dojo.keys.DELETE,dojo.keys.ENTER,dojo.keys.BACKSPACE,dojo.keys.END,dojo.keys.HOME,dojo.keys.TAB,dojo.keys.F5],allowableDayString:["32","33","34","35","36"],allowableDayOfWeekMask:["201","202","203","1","2","4","8","16","32","64"],allowableFirstDayStringForBimonthly:["32","33","34","35"],allowableSecondDayStringForBimonthly:["33","34","35","36"],allowableWeekdayStringForBimonthly:["1","2","4","8","16","32","64"],allowableMonthString:["1","2","3","4","5","6","7","8","9","10","11","12"],initPage:function(){
var _b=curam.dialog.getParentWindow(window);
if(formActivated==true){
executeOpenerMapping("freq_text",translatedPatternString);
executeOpenerMapping("freq_data",patternString);
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
curam.dialog.closeModalDialog();
return false;
}
var _c=_b.getPopupInput("initFreq");
curam.debug.log(_9.getProperty("curam.util.FrequencyEditor.input"),_c);
if(!_c||_c==null||_c.length==0){
document.theForm.freqType[0].checked=true;
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
return true;
}
var _d=parseInt(_c.charAt(0),10);
if(_d==curam.util.FrequencyEditor.DAILY_FREQUENCY){
curam.util.FrequencyEditor.setupDailyFrequency(_c);
}else{
if(_d==curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
curam.util.FrequencyEditor.setupWeeklyFrequency(_c);
}else{
if(_d==curam.util.FrequencyEditor.MONTHLY_FREQUENCY){
curam.util.FrequencyEditor.setupMonthlyFrequency(_c);
}else{
if(_d==curam.util.FrequencyEditor.YEARLY_FREQUENCY){
curam.util.FrequencyEditor.setupYearlyFrequency(_c);
}else{
if(_d==curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY){
curam.util.FrequencyEditor.setupBimonthlyFrequency(_c);
}else{
alert(errorMsgs.freqPattern);
}
}
}
}
}
return true;
},setupDailyFrequency:function(_e){
var _f=_e.substr(4,3);
document.theForm.freqType[curam.util.FrequencyEditor.DAILY_FREQUENCY].checked=true;
if(parseInt(_f,10)==curam.util.FrequencyEditor.EVERY_WEEKDAY_MASK){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_WEEKDAY].checked=true;
}else{
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
var _10=parseInt(_e.substr(1,3),10);
document.theForm.daily_num.value=""+_10;
}
},setupWeeklyFrequency:function(_11){
var _12=parseInt(_11.substr(4,3),10);
document.theForm.freqType[curam.util.FrequencyEditor.WEEKLY_FREQUENCY].checked=true;
if(_12&curam.util.FrequencyEditor.MON_MASK){
document.theForm.weekly_select_mon.checked=true;
}
if(_12&curam.util.FrequencyEditor.TUE_MASK){
document.theForm.weekly_select_tue.checked=true;
}
if(_12&curam.util.FrequencyEditor.WED_MASK){
document.theForm.weekly_select_wed.checked=true;
}
if(_12&curam.util.FrequencyEditor.THU_MASK){
document.theForm.weekly_select_thur.checked=true;
}
if(_12&curam.util.FrequencyEditor.FRI_MASK){
document.theForm.weekly_select_fri.checked=true;
}
if(_12&curam.util.FrequencyEditor.SAT_MASK){
document.theForm.weekly_select_sat.checked=true;
}
if(_12&curam.util.FrequencyEditor.SUN_MASK){
document.theForm.weekly_select_sun.checked=true;
}
var _13=parseInt(_11.substr(1,3),10);
document.theForm.weekly_num.value=""+_13;
},setupMonthlyFrequency:function(_14){
var _15=parseInt(_14.substr(1,3),10);
var _16=parseInt(_14.substr(4,3),10);
var _17=parseInt(_14.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.MONTHLY_FREQUENCY].checked=true;
if(_17==0){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.START_DATE].checked=true;
document.theForm.monthly0_month_interval.value=_15;
}else{
if(_17<=31){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_DAY_NUM].checked=true;
document.theForm.monthly1_day_num.value=_17;
document.theForm.monthly1_month_interval.value=_15;
}else{
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_SEL_DAY].checked=true;
var _18;
if(_a("ios")||_a("android")){
_18=dojo.byId("monthly2_select_day_num");
_18.value=_17;
_18=dojo.byId("monthly2_select_day");
_18.value=_16;
}else{
_18=dijit.byId("monthly2_select_day_num");
_18.set("value",_17);
_18=dijit.byId("monthly_select_day");
_18.set("value",_16);
}
document.theForm.monthly2_month_interval.value=_15;
}
}
},setupBimonthlyFrequency:function(_19){
var _1a=parseInt(_19.substr(1,2),10);
var _1b=parseInt(_19.substr(4,3),10);
var _1c=parseInt(_19.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY-curam.util.FrequencyEditor.CORRECTOR].checked=true;
if(_1c<=31){
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
document.theForm.bimonthly1_day1_num.value=_1c;
document.theForm.bimonthly1_day2_num.value=_1a;
}else{
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=true;
var _1d;
if(_a("ios")||_a("android")){
_1d=dojo.byId("bimonthly2_select_day1_num");
_1d.value=_1c;
_1d=dojo.byId("bimonthly2_select_day2_num");
_1d.value=_1a;
_1d=dojo.byId("bimonthly2_select_weekday");
_1d.value=_1b;
}else{
_1d=dijit.byId("bimonthly2_select_day1_num");
_1d.set("value",_1c);
_1d=dijit.byId("bimonthly2_select_day2_num");
_1d.set("value",_1a);
_1d=dijit.byId("bimonthly2_select_weekday");
_1d.set("value",_1b);
}
}
},setupYearlyFrequency:function(_1e){
var _1f=parseInt(_1e.substr(1,3),10);
var _20=parseInt(_1e.substr(4,3),10);
var _21=parseInt(_1e.substr(7,2),10);
document.theForm.freqType[curam.util.FrequencyEditor.YEARLY_FREQUENCY+curam.util.FrequencyEditor.CORRECTOR].checked=true;
if(_21<=31){
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=true;
var _22;
if(_a("ios")||_a("android")){
_22=dojo.byId("yearly1_select_month");
_22.value=_1f;
}else{
_22=dijit.byId("yearly1_select_month");
_22.set("value",_1f);
}
document.theForm.yearly1_day_num.value=_21;
}else{
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_SEL_DAY].checked=true;
var _22;
if(_a("ios")||_a("android")){
_22=dojo.byId("yearly2_select_day_num");
_22.value=_21;
_22=dojo.byId("yearly2_select_day");
_22.value=_20;
_22=dojo.byId("yearly2_select_month");
_22.value=_1f;
}else{
_22=dijit.byId("yearly2_select_day_num");
_22.set("value",_21);
_22=dijit.byId("yearly2_select_day");
_22.set("value",_20);
_22=dijit.byId("yearly2_select_month");
_22.set("value",_1f);
}
}
},createPatternString:function(){
var _23=null;
var _24=false;
if(document.theForm.freqType[0].checked==true){
_24=curam.util.FrequencyEditor.createDailyPatternString();
}else{
if(document.theForm.freqType[1].checked==true){
_24=curam.util.FrequencyEditor.createWeeklyPatternString();
}else{
if(document.theForm.freqType[2].checked==true){
_24=curam.util.FrequencyEditor.createMonthlyPatternString();
}else{
if(document.theForm.freqType[3].checked==true){
_24=curam.util.FrequencyEditor.createBimonthlyPatternString();
}else{
_24=curam.util.FrequencyEditor.createYearlyPatternString();
}
}
}
}
if(_24){
curam.util.FrequencyEditor.disableRowBorder();
return true;
}else{
return false;
}
},createDailyPatternString:function(){
var _25="0";
if(document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked==true){
var _26=parseInt(document.theForm.daily_num.value,10);
if(curam.util.FrequencyEditor.validateDailyPattern(_26)){
_25+=curam.util.FrequencyEditor.doZeroPadding(_26,3);
_25+="000";
}else{
return false;
}
}else{
_25+="001";
_25+=curam.util.FrequencyEditor.EVERY_WEEKDAY_MASK;
}
_25+="00";
document.theForm.patternString.value=_25;
return true;
},validateDailyPattern:function(_27){
if(isNaN(_27)||_27<1){
alert(errorMsgs.everyDay);
return false;
}
return true;
},createWeeklyPatternString:function(){
var _28="1";
var _29=0;
var _2a=parseInt(document.theForm.weekly_num.value,10);
if(curam.util.FrequencyEditor.validateWeeklyPattern(_2a)){
_28+=curam.util.FrequencyEditor.doZeroPadding(_2a,3);
var _2b=false;
var _2c=document.theForm.weekly_select_mon;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_tue;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_wed;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_thur;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_fri;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_sat;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
_2c=document.theForm.weekly_select_sun;
if(_2c.checked==true){
_2b=true;
_29+=_2c.value-0;
}
if(!_2b){
alert(errorMsgs.noDaySelected);
return false;
}
if(_29>0){
_28+=curam.util.FrequencyEditor.doZeroPadding(_29,3);
}else{
_28+="000";
}
_28+="00";
document.theForm.patternString.value=_28;
return true;
}
return false;
},validateWeeklyPattern:function(_2d){
if(isNaN(_2d)||_2d<1){
alert(errorMsgs.everyWeek);
return false;
}
return true;
},createMonthlyPatternString:function(){
var _2e="2";
if(document.theForm.monthlyFreqType[curam.util.FrequencyEditor.START_DATE].checked==true){
var _2f=parseInt(document.theForm.monthly0_month_interval.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_2f)){
return false;
}
var _30=0;
_2e+=curam.util.FrequencyEditor.doZeroPadding(_2f,3);
_2e+="000";
_2e+=curam.util.FrequencyEditor.doZeroPadding(_30,2);
}else{
if(document.theForm.monthlyFreqType[curam.util.FrequencyEditor.MONTH_DAY_NUM].checked==true){
var _2f=parseInt(document.theForm.monthly1_month_interval.value,10);
var _30=parseInt(document.theForm.monthly1_day_num.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_2f,_30)){
return false;
}
_2e+=curam.util.FrequencyEditor.doZeroPadding(_2f,3);
_2e+="000";
_2e+=curam.util.FrequencyEditor.doZeroPadding(_30,2);
}else{
var _2f=parseInt(document.theForm.monthly2_month_interval.value,10);
if(!curam.util.FrequencyEditor.validateMonthlyData(_2f)){
return false;
}
var day=curam.util.FrequencyEditor.domSelector.byId("monthly2_select_day_num").value;
var _31=curam.util.FrequencyEditor.domSelector.byId("monthly2_select_day").value;
if(!curam.util.FrequencyEditor.validateDayWeekString(day,_31,_2e)){
return false;
}
_2e+=curam.util.FrequencyEditor.doZeroPadding(_2f,3);
_2e+=curam.util.FrequencyEditor.doZeroPadding(_31,3);
_2e+=curam.util.FrequencyEditor.doZeroPadding(day,2);
}
}
document.theForm.patternString.value=_2e;
return true;
},validateMonthlyData:function(_32,_33){
if(isNaN(_32)||_32<1||_32>100){
alert(errorMsgs.monthNum);
return false;
}
if(_33==null){
return true;
}
if(isNaN(_33)||_33<1||_33>28){
alert(errorMsgs.dayNum);
return false;
}
return true;
},validateDayWeekString:function(day,_34,_35){
var _36=curam.util.FrequencyEditor.allowableDayString;
var _37=curam.util.FrequencyEditor.allowableDayOfWeekMask;
var _38=false;
var _39=false;
for(var i=0;i<_36.length;i++){
if(day==_36[i]){
_38=true;
break;
}
}
for(var i=0;i<_37.length;i++){
if(_34==_37[i]){
_39=true;
break;
}
}
if(_38&&_39){
return true;
}else{
if(!_38){
if(_35=="2"){
alert(errorMsgs.dayStringForMonthly);
}else{
if(_35=="3"){
alert(errorMsgs.dayStringForYearly);
}else{
alert(errorMsgs.dayString);
}
}
return false;
}else{
if(!_39){
if(_35=="2"){
alert(errorMsgs.dayOfWeekMaskForMonthly);
}else{
if(_35=="3"){
alert(errorMsgs.dayOfWeekMaskForYearly);
}else{
alert(errorMsgs.dayOfWeekMask);
}
}
return false;
}
}
}
},createBimonthlyPatternString:function(){
var _3a="4";
var _3b;
if(document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked==true){
var _3c=parseInt(document.theForm.bimonthly1_day1_num.value,10);
var _3d=parseInt(document.theForm.bimonthly1_day2_num.value,10);
if(!curam.util.FrequencyEditor.validateBimonthlyData(_3c,_3d,null)){
return false;
}
if(_3c>_3d){
_3b=_3c;
_3c=_3d;
_3d=_3b;
}
_3a+=curam.util.FrequencyEditor.doZeroPadding(_3d,2);
_3a+="0000";
_3a+=curam.util.FrequencyEditor.doZeroPadding(_3c,2);
}else{
var _3e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_day1_num");
var _3f=_3e.value;
_3e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_day2_num");
var _40=_3e.value;
_3e=curam.util.FrequencyEditor.domSelector.byId("bimonthly2_select_weekday");
var _41=_3e.value;
if(!curam.util.FrequencyEditor.validateBimonthlyDataString(_3f,_40,_41)){
return false;
}
if(_3f>_40){
_3b=_3f;
_3f=_40;
_40=_3b;
}
if(!curam.util.FrequencyEditor.validateBimonthlyData(_3f,_40,_41)){
return false;
}
_3a+=curam.util.FrequencyEditor.doZeroPadding(_40,2);
_3a+="0";
_3a+=curam.util.FrequencyEditor.doZeroPadding(_41,3);
_3a+=curam.util.FrequencyEditor.doZeroPadding(_3f,2);
}
document.theForm.patternString.value=_3a;
return true;
},validateBimonthlyData:function(_42,_43,_44){
if(_44!=null){
if(isNaN(_44)||_44<1||_44>64){
alert(errorMsgs.weekend);
return false;
}
}else{
if(isNaN(_42)||_42<1||_42>28||isNaN(_43)||_43<1||_43>28){
alert(errorMsgs.dayNum);
return false;
}
}
if(_42==_43){
alert(errorMsgs.dayDiff);
return false;
}
return true;
},validateBimonthlyDataString:function(_45,_46,_47){
var _48=curam.util.FrequencyEditor.allowableFirstDayStringForBimonthly;
var _49=curam.util.FrequencyEditor.allowableSecondDayStringForBimonthly;
var _4a=curam.util.FrequencyEditor.allowableWeekdayStringForBimonthly;
var _4b=false;
var _4c=false;
var _4d=false;
for(var i=0;i<_48.length;i++){
if(_45==_48[i]){
_4b=true;
break;
}
}
for(var i=0;i<_49.length;i++){
if(_46==_49[i]){
_4c=true;
break;
}
}
for(var i=0;i<_4a.length;i++){
if(_47==_4a[i]){
_4d=true;
break;
}
}
if(_4b&&_4c&&_4d){
return true;
}else{
if(!_4b){
alert(errorMsgs.firstDayString);
return false;
}else{
if(!_4c){
alert(errorMsgs.secondDayString);
return false;
}else{
if(!_4d){
alert(errorMsgs.weekend);
return false;
}
}
}
}
},createYearlyPatternString:function(){
var _4e="3";
var _4f=null;
if(document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked==true){
_4f=curam.util.FrequencyEditor.domSelector.byId("yearly1_select_month");
var _50=_4f.value;
_4e+=curam.util.FrequencyEditor.doZeroPadding(_50,3);
_4e+="000";
if(!curam.util.FrequencyEditor.validateMonthString(_50)){
return false;
}
var _51=parseInt(document.theForm.yearly1_day_num.value,10);
if(!curam.util.FrequencyEditor.validateYearlyData(_51,_50)){
return false;
}
_4e+=curam.util.FrequencyEditor.doZeroPadding(_51,2);
}else{
var day=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_day_num").value;
var _52=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_day").value;
var _53=curam.util.FrequencyEditor.domSelector.byId("yearly2_select_month").value;
if(!curam.util.FrequencyEditor.validateDayWeekString(day,_52,_4e)){
return false;
}
if(!curam.util.FrequencyEditor.validateMonthString(_53)){
return false;
}
_4e+=curam.util.FrequencyEditor.doZeroPadding(_53,3);
_4e+=curam.util.FrequencyEditor.doZeroPadding(_52,3);
_4e+=curam.util.FrequencyEditor.doZeroPadding(day,2);
}
document.theForm.patternString.value=_4e;
return true;
},validateYearlyData:function(_54,_55){
if(isNaN(_54)||_54<1||_54>curam.util.FrequencyEditor.daysInMonth[_55-1]){
alert(errorMsgs.dayNumAnd+"  "+curam.util.FrequencyEditor.daysInMonth[_55-1]);
return false;
}
return true;
},validateMonthString:function(_56){
var _57=curam.util.FrequencyEditor.allowableMonthString;
for(var i=0;i<_57.length;i++){
if(_56==_57[i]){
return true;
}
}
alert(errorMsgs.monthString);
return false;
},doZeroPadding:function(_58,_59){
var _5a=""+_58;
var _5b=_59-_5a.length;
for(var i=0;i<_5b;i++){
_5a="0"+_5a;
}
return _5a;
},_setFirstLevelRadioButton:function(_5c){
var _5d=_6("input[name='freqType']",_1.byId("mainForm"))[_5c];
if(_5d==null){
throw new Error("The radio button for the selected"+" frequency type could not be found!");
}
if(!_5d.checked){
_6("input[type='radio']:checked",_1.byId("mainForm")).forEach(function(_5e){
_5e.checked=false;
});
if(_5c!=curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
_6("input[type='checkbox']:checked",_1.byId("mainForm")).forEach(function(_5f){
_5f.checked=false;
});
}
_5d.checked=true;
}
},_setSecondLevelRadioButton:function(_60){
if(_60==undefined){
return "undefined";
}
var _61;
if(_60.domNode){
_61=_60.domNode;
}else{
_61=_60;
}
if(_61.tagName.toLowerCase()=="input"&&_4.get(_61,"type")=="radio"){
_61.checked=true;
return "radio node clicked";
}
var _62=cm.getParentByType(_61,"TD");
if(_62==null){
throw new Error("Exception: The row contains the node should be found");
}
var _63=_6("input[type = 'radio']",_62)[0];
if(_63==null){
throw new Error("Exception: The radio node should exist");
}else{
_63.checked=true;
return "text input or codetable clicked";
}
},setSelectedFreqType:function(_64,_65){
_9.log("curam.util.FrequencyEditor: "+_9.getProperty("curam.util.FrequencyEditor.radio"));
curam.util.FrequencyEditor._setFirstLevelRadioButton(_64);
curam.util.FrequencyEditor._setSecondLevelRadioButton(_65);
},setDefaultOption:function(_66){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=false;
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_WEEKDAY].checked=false;
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=false;
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=false;
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=false;
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.SEL_DAY].checked=false;
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=false;
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_SEL_DAY].checked=false;
if(_66!=curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
document.theForm.weekly_select_mon.checked=false;
document.theForm.weekly_select_tue.checked=false;
document.theForm.weekly_select_wed.checked=false;
document.theForm.weekly_select_thur.checked=false;
document.theForm.weekly_select_fri.checked=false;
document.theForm.weekly_select_sat.checked=false;
document.theForm.weekly_select_sun.checked=false;
}
if(_66==curam.util.FrequencyEditor.DAILY_FREQUENCY){
document.theForm.daily_select_type[curam.util.FrequencyEditor.EVERY_DAY].checked=true;
}else{
if(_66==curam.util.FrequencyEditor.WEEKLY_FREQUENCY){
document.theForm.weekly_select_mon.checked=true;
}else{
if(_66==curam.util.FrequencyEditor.MONTHLY_FREQUENCY){
document.theForm.monthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
}else{
if(_66==curam.util.FrequencyEditor.BIMONTHLY_FREQUENCY){
document.theForm.bimonthlyFreqType[curam.util.FrequencyEditor.DAY_NUM].checked=true;
}else{
if(_66==curam.util.FrequencyEditor.YEARLY_FREQUENCY){
document.theForm.yearlyFreqType[curam.util.FrequencyEditor.SEL_MONTH_DAY_NUM].checked=true;
}
}
}
}
}
},_doPosNumbericInputChecker:function(_67){
if(_67==""){
return false;
}
var _68=curam.util.FrequencyEditor.allowableCharsForNumeric;
for(var i=0;i<_68.length;i++){
if(_67==_68[i]){
return true;
}
}
return false;
},posNumericInputChecker:function(_69){
_69=dojo.fixEvent(_69);
var _6a=_69.keyChar;
var _6b=curam.util.FrequencyEditor._doPosNumbericInputChecker(_6a);
if(!_6b){
dojo.stopEvent(_69);
}
},prePopulateTextFields:function(_6c){
return function(e){
for(var i=0;i<_6c.length;i++){
if(!_6c[i].value||_6c[i].value==""){
_6c[i].value=1;
}
}
};
},disableRowBorder:function(){
_6("form[name='theForm'] table tr").forEach(function(_6d){
_3.add(_6d,"row-no-border");
});
},addInputListener:function(){
dojo.ready(function(){
var _6e=[];
_6("input[type='text']:not(input.dijitReset)").forEach(function(_6f){
_6e.push(_6f);
curam.util.connect(_6f,"onkeypress",curam.util.FrequencyEditor.posNumericInputChecker);
});
curam.util.connect(_1.byId("mainForm"),"onsubmit",function(_70){
curam.util.FrequencyEditor.prePopulateTextFields(_6e);
});
});
},replacePlaceholderWithDomNode:function(){
_6("body#Curam_frequency-editor table tr td.frequency").forEach(function(_71){
curam.util.FrequencyEditor._parse(_71);
});
},_parse:function(_72){
var _73=_6("> .node-needs-replacement",_72);
var _74=_6("> span",_72)[0];
if(_74==null||_74==undefined){
throw new Error("Exception: Some text string is missing for some certain "+"frequency type, please check the 'frequency-editor.jsp' file.");
}
var _75=_74.innerHTML;
var _76=/%[^%]*%/g;
var _77=_75.match(_76);
if(_73.length==0&&_77==null){
return "No need to parse";
}else{
if(_73.length==0&&_77!=null){
throw new Error("The text string '"+_75+"' from the 'FrequencyPatternSelector.properties'"+" should not have any placeholder.");
}else{
if(_73.length!=0&&_77==null){
throw new Error("The text string '"+_75+"' from the 'FrequencyPatternSelector.properties'"+" should have some placeholders.");
}
}
}
if(_3.contains(_72,"weekly-frequency")){
if(_77.length!=2){
throw new Error("The text string '"+_75+"' from the 'FrequencyPatternSelector.properties' "+"has the incorrect number of placeholders.");
}
var _78=dojo.clone(_73[0]);
_73.forEach(function(_79){
_5.destroy(_79);
});
_3.remove(_78,"node-needs-replacement");
var _7a=_78.className.match(_76);
var _7b;
for(var i=0;i<_77.length;i++){
if(_77[i]!=_7a){
_7b=_77[i];
break;
}
}
var _7c=_75.split(_7b);
var _7d=_7c[0];
var _7e=_7c[1];
var _7f;
if(_7d.indexOf(_7a)!=-1){
_7f=true;
_7d=_7d.replace(_7a,"<span class='"+_7a+"'>placeholder</span>");
}else{
_7f=false;
_7e=_7e.replace(_7a,"<span class='"+_7a+"'>placeholder</span>");
}
if(_7e==""){
_74.innerHTML=_7d;
_5.place(_78,_6("span."+_7a,_74)[0],"replace");
}else{
_74.innerHTML=_7d;
var _80=_72.parentNode.nextSibling.nextSibling;
var _81=_5.create("tr",{"class":"blue"});
var _82=_5.create("td",{"class":"bottom"},_81);
_82.colSpan="4";
_2.set(_82,"paddingLeft","20px");
var _83=_5.create("span",{innerHTML:_7e},_82);
_5.place(_81,_80,"after");
if(_7f){
_5.place(_78,_6("span."+_7a,_74)[0],"replace");
}else{
_5.place(_78,_6("span[class='"+_7a+"'",_83)[0],"replace");
}
_6("td.day",_80).forEach(function(_84){
_3.remove(_84,"bottom");
});
if(_7d==""){
_3.remove(_72,"top");
}
_6("th.type",_72.parentNode)[0].rowSpan="4";
}
return "Parsed Successfully";
}
if(_73.length!=_77.length){
throw new Error("The text string '"+_75+"' from the 'FrequencyPatternSelector.properties' "+"has the incorrect number of placeholders.");
}
var _85=dojo.clone(_73);
_73.forEach(_5.destroy);
for(i=0;i<_77.length;i++){
var _86=_77[i];
_75=_75.replace(_86,"<span class='"+_86+"'>placeholder</span>");
}
_74.innerHTML=_75;
_85.forEach(function(_87,i){
_3.remove(_87,"node-needs-replacement");
var _88=_87.className.match(_76);
_5.place(_87,_6("span."+_88,_72)[0],"replace");
});
return "Parsed Successfully";
}});
return curam.util.FrequencyEditor;
});
