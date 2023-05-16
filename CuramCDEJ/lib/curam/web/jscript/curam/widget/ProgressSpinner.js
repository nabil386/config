//>>built
define("curam/widget/ProgressSpinner",["dojox/widget/Standby","dojo/dom-construct","curam/debug","curam/util","curam/tab"],function(_1,_2,_3){
var _4=dojo.setObject("curam.widget.ProgressSpinner",{PROGRESS_WIDGET_ENABLED:true,PROGRESS_WIDGET_DEFAULT_THRESHOLD:2000,PROGRESS_WIDGET_COLOR:"white",PROGRESS_WIDGET_TITLE:"Please wait...",PROGRESS_WIDGET_TIMEOUT_MAX:90000,LOAD_MASK_TIMEOUT:15000,_spinnerTokens:[],_launcherTokens:[],_forcedTimeouts:[],_enabled:false,_eHandler:false,_tDojo:null,init:function(){
_3.log("PROGRESS SPINNER: isEnabled: "+_4.PROGRESS_WIDGET_ENABLED);
_3.log("PROGRESS SPINNER: threshold: "+_4.PROGRESS_WIDGET_DEFAULT_THRESHOLD);
_3.log("PROGRESS SPINNER: max timeout: "+_4.PROGRESS_WIDGET_TIMEOUT_MAX);
_4._enabled=_4._isEnabled();
_4.tDojo.subscribe("/curam/progress/unload",function(){
_4._enabled&&_4.dismissSpinner();
});
_4.tDojo.subscribe("/curam/progress/display",function(_5,_6){
if(_4._enabled){
var _7=_4.dismissSpinner();
_5&&_4.launch(_5,(_7?0:_6));
}
});
},_isEnabled:function(){
return (_4.PROGRESS_WIDGET_ENABLED&&(_4.PROGRESS_WIDGET_ENABLED===true||_4.PROGRESS_WIDGET_ENABLED.toString().toLowerCase()==="true"));
},launch:function(_8,_9){
var _a=_4._getSpinner(_8);
_a&&_4.show(_a,_9);
},getSpinner:function(_b){
return _4._getSpinner(_b);
},_getSpinner:function(_c){
if(_c!=null&&_4._enabled){
_3.log(_3.getProperty("curam.widget.ProgressSpinner.load",[_c]));
var _d=dojo.isString(_c)?_4.adjustTarget(_c):_c;
var _e=new _1({target:_d,centerIndicator:"text",zIndex:999999999,color:_4.PROGRESS_WIDGET_COLOR,text:"<div class='curam-spinner curam-h1'></div>",title:_4.PROGRESS_WIDGET_TITLE,duration:250,onHide:function(){
curam.util.getTopmostWindow().document.querySelectorAll(".standby").forEach(_2.destroy);
}});
_e.domNode.classList.add("standby");
curam.util.getTopmostWindow().document.body.appendChild(_e.domNode);
_4._spinnerTokens.push(_e);
_e.startup();
return _e;
}
return null;
},adjustTarget:function(_f){
var _10=_f;
var wid=curam.tab.getSelectedTabWidgetId();
if(wid){
var _11=curam.util.getTopmostWindow().dijit.byId(wid);
_11&&(_10=_11.containerNode);
}
return _10;
},show:function(_12,_13){
!!_13&&(_13=_4.PROGRESS_WIDGET_DEFAULT_THRESHOLD);
var _14=setTimeout(function(){
_12.show();
var _15=setTimeout(function(){
_4.tDojo.publish("/curam/progress/unload");
},_4.PROGRESS_WIDGET_TIMEOUT_MAX);
_4._forcedTimeouts.push(_15);
},_13);
_4._launcherTokens.push(_14);
return _14;
},hide:function(_16){
_16.hide();
},dismissSpinner:function(){
return _4._dismissSpinner();
},_dismissSpinner:function(){
var res=_4._spinnerTokens.length;
_4._forcedTimeouts.forEach(function(_17){
clearTimeout(_17);
});
_4._forcedTimeouts.splice(0,_4._forcedTimeouts.length);
_4._launcherTokens.forEach(function(_18){
clearTimeout(_18);
});
_4._launcherTokens.splice(0,_4._launcherTokens.length);
_4._spinnerTokens.forEach(function(_19){
_19.hide();
});
_4._spinnerTokens.splice(0,_4._spinnerTokens.length);
curam.debug.log("PROGRESS SPINNER: "+_3.getProperty("curam.widget.ProgressSpinner.unload"));
return (res>0);
}});
_4.tDojo=curam.util.getTopmostWindow().dojo;
if(!_4.eHandler){
_4.tDojo.subscribe("/curam/progress/unload",function(){
_4._enabled&&_4.dismissSpinner();
});
_4.eHandler=_4.tDojo.subscribe("/curam/progress/display",function(_1a,_1b){
if(_4._enabled){
var _1c=_4.dismissSpinner();
_1a&&_4.launch(_1a,(_1c?0:_1b));
}
});
}
return curam.widget.ProgressSpinner;
});
