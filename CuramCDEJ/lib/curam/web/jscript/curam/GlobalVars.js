//>>built
define("curam/GlobalVars",["curam/util"],function(){
var _1={popupMappingRepository:[],popupMappingLoaded:[],popupInputs:[],currentPopupProps:null,currentPopupInstanceName:"",popupWindow:null,popupCTCodeMappings:[],popupPropertiesRepository:[],POPUP_EMPTY_SPAN_MIN_SIZE:15,POPUP_EMPTY_SPAN_CHAR:" ",POPUP_EMPTY_SPAN_VALUE:null,replacedButtons:[]};
var gc=dojo.global.curam;
dojo.mixin(gc,_1);
gc.POPUP_EMPTY_SPAN_VALUE=curam.util.fillString(gc.POPUP_EMPTY_SPAN_CHAR,gc.POPUP_EMPTY_SPAN_MIN_SIZE);
return _1;
});
