//>>built
define("curam/dateSelectorUtil",["dojo/on","dojo/dom","dojo/dom-attr","dojo/has","dijit/registry","curam/date","curam/util","curam/dialog","curam/debug","curam/omega3-util","dojo/sniff"],function(On,_1,_2,_3,_4,_5,_6,_7,_8,_9){
var _a=_DSU=dojo.setObject("curam.dateSelectorUtil",{myOwner:null,milliDay:86400000,startWeekDay:undefined,topic:undefined,monthCombo:null,yearInput:null,inDate:null,today:new Date(),thisMonth:null,thisYear:null,thisDay:null,dayTitle:"",daysInMonth:[31,28,31,30,31,30,31,31,30,31,30,31],navigationKeys:[dojo.keys.LEFT_ARROW,dojo.keys.RIGHT_ARROW,dojo.keys.DELETE,dojo.keys.BACKSPACE,dojo.keys.END,dojo.keys.HOME,dojo.keys.TAB,dojo.keys.F5],initCalendar:function(){
_DSU.thisMonth=_DSU.today.getMonth();
_DSU.thisYear=_DSU.today.getFullYear();
_DSU.thisDay=_DSU.today.getDate();
_DSU.myOwner=_7.getParentWindow(window);
_8.log("curam.dateSelectorUtil:initCalendar(), myOwner = ",_DSU.myOwner.location.href);
var _b=_5.getDateFromFormat(_DSU.myOwner.getPopupInput("dateSelectorInitDate"),jsDF);
if(isNaN(_b)||_b==0){
_b=_DSU.today;
}
_DSU.inDate=_b;
var y=_b.getFullYear(),m=_b.getMonth();
_DSU.yearInput.value=y;
_DSU.monthCombo.set("value",m);
_DSU.drawCalendar(m,y);
},updateCalendar:function(_c,_d){
var _e=_DSU.monthCombo.value;
var _f=_DSU.yearInput.value;
var _10=function(_11){
return function(){
_11&&_11.focus();
};
};
if(!this.validMonth(_e)||!this.validYear(_f)){
_e=_DSU.thisMonth;
_f=_DSU.thisYear;
}
_DSU.drawCalendar(_e,_f);
setTimeout(_10(_c),300);
},drawCalendar:function(_12,_13){
var _14=(_13==_DSU.thisYear)&&(_12==_DSU.thisMonth);
var _15=_DSU.daysInMonth;
if(((_13%4==0)&&(_13%100!=0))||(_13%400==0)){
_15[1]=29;
}
var _16=new Date(_13,_12,1),_17=_16.getDay(),_18=_17-_DSU.startWeekDay,_19=_16.setDate(_16.getDate()-_18),_1a=_15[_12]+_17-1,_1b=(_DSU.startWeekDay==0)?6:0;
var _1c=[];
var cs="other-month";
var _1d=new Date(_19);
for(i=0;i<42;i++){
var _1e=new Date(_1d),d=_1e.getDate(),_1f=_5.formatDate(_1e,"MMM dd, yyyy");
if(i>=_18){
cs=(6-(i%7)==_1b)?"sunday":"this-month";
if(_1e-_DSU.inDate==0){
cs="current-date";
_1f+=" "+LOCALISED_SELECTED_DAY;
}
if(_14&&d==_DSU.thisDay){
cs="today";
_1f+=" "+LOCALISED_CURRENT_DAY;
}
if(i>_1a&&d<_15[_12]){
cs="other-month";
}
}
_1c.push(["<span tabindex=\"0\" role=\"button\" class=\""+cs+"\"title=\""+_1f+"\">"+d+"</span>",_1e]);
_1d.setDate(_1d.getDate()+1);
}
var _20=_1.byId("calendarData");
_1c.forEach(function(_21,ix){
var cl=_20.rows[Math.floor(ix/7)+1].cells[ix%7];
cl.innerHTML=_21[0];
_2.remove(cl,"aria-hidden");
On(cl,"keydown",function(e){
if(e.keyCode==13||e.keyCode==32){
dojo.fixEvent(event);
dojo.stopEvent(e);
_DSU.publishDate(_21[1]);
return false;
}
return true;
});
On.once(cl,"click",function(e){
dojo.fixEvent(event);
dojo.stopEvent(e);
_DSU.publishDate(_21[1]);
});
});
},publishDate:function(_22){
_22=_22?_22:new Date();
var _23=curam.date.formatDate(_22,jsDF);
if(_DSU.topic&&strlen(_DSU.topic)>0&&typeof (_DSU.myOwner.curam.msg.publish)!="undefined"){
_DSU.myOwner.curam.msg.publish(_DSU.topic,_23);
}else{
_DSU.myOwner.executeMapping("return_date",_23);
}
if(_DSU.myOwner!=window){
_8.log(_8.getProperty("curam.dateSelectorUtil.msg"));
_7.closeModalDialog();
}
},initFieldsAndListeners:function(){
var mm,yy;
_DSU.monthCombo=mm=_4.byId("month");
_DSU.yearInput=yy=_1.byId("year");
On(yy,"keyup",function(e){
if(_DSU.navigationKeys.indexOf(e.key)==-1){
if(yy.value.length>4){
yy.value=yy.value.substring(0,4);
return false;
}
}
return true;
});
On(yy,"keydown",function(e){
if(e.keyCode==13||e.keyCode==32){
_8.log("date.selector.event.triggered.year");
_DSU.updateCalendar(yy,mm);
return false;
}
return true;
});
On(yy,"change",function(e){
_8.log("date.selector.event.triggered.year");
_DSU.updateCalendar(yy,mm);
});
On(mm,"change",function(e){
_8.log("date.selector.event.triggered.month");
_DSU.updateCalendar(mm,yy);
});
On(_1.byId("calendar-form"),"submit",function(e){
e.preventDefault();
return false;
});
},validYear:function(_24){
return !(_24==null||isNaN(_24)||_24<1||_24>99999);
},validMonth:function(_25){
return !(_25==null||isNaN(_25)||_25<0||_25>11);
},});
return _a;
});
