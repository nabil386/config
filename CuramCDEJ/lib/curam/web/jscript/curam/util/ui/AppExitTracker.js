//>>built
define("curam/util/ui/AppExitTracker",["dojo/mouse","dojo/on","dojo/aspect","dijit/registry","dojo/sniff"],function(_1,on,_2,_3,_4){
var _5=_AET=dojo.setObject("curam.util.ui.AppExitTracker",{isPointerOnWindow:undefined,stopBrowserConfirmationDialog:false,hasClickedOnWindow:false,uHandlers:[],_unsubscribes:[],_isRedirectingToLogoutPage:false,_tabWidgetIdsWithQtyOfIframes:{},_isSubmittingThePage:false,_expectedQtyOfIframesInCurrentTab:-1,_currentTabWidgetID:null,_checkMouseOutOfScreen:function(_6,_7){
var e=_7||window.event;
if(_AET.isPointerOnWindow===false){
return;
}
var _8=e.relatedTarget||e.toElement;
var _9=document.body.getBoundingClientRect();
var _a=e.pageX<=_9.left||e.pageX>=_9.right||e.pageY<=_9.top||e.pageY>=_9.bottom;
if(_6&&e.toElement==null&&e.relatedTarget==null){
_AET.isPointerOnWindow=false;
}else{
if(!_8||_8.nodeName=="HTML"){
if(_a){
_AET.isPointerOnWindow=false;
}
}else{
if(_a){
_AET.isPointerOnWindow=false;
}
}
}
},setStopBrowserConfirmationDialog:function(_b){
if(!_AET._isRedirectingToLogoutPage){
_AET.stopBrowserConfirmationDialog=_b;
}
},resetPageTracker:function(){
_AET.stopBrowserConfirmationDialog=false;
_AET.isPointerOnWindow=true;
_AET.hasClickedOnWindow=false;
},shouldDisplayAppExitConfirmationDialog:function(){
return !(_AET.stopBrowserConfirmationDialog||_AET.isPointerOnWindow);
},_setCurrentTabWidgetID:function(_c){
_AET._currentTabWidgetID=_c;
},_putTabWidgetIdWithQtyOfIframes:function(_d,_e){
if(_AET._tabWidgetIdsWithQtyOfIframes[_d]){
_AET._tabWidgetIdsWithQtyOfIframes[_d].qtyOfIframes=_e;
}else{
_AET._tabWidgetIdsWithQtyOfIframes[_d]={qtyOfIframes:_e};
}
},_getQtyOfIframesByTabWidgetId:function(_f){
if(_AET._tabWidgetIdsWithQtyOfIframes[_f]){
return _AET._tabWidgetIdsWithQtyOfIframes[_f].qtyOfIframes;
}
return 0;
},_removeTabWidgetId:function(_10){
return _AET._tabWidgetIdsWithQtyOfIframes[_10]&&delete _AET._tabWidgetIdsWithQtyOfIframes[_10];
},_decreaseQtyOfExpectedIframebyTab:function(_11){
if(_AET._expectedQtyOfIframesInCurrentTab>0&&_AET._isSubmittingThePage){
_11?_AET._expectedQtyOfIframesInCurrentTab=0:_AET._expectedQtyOfIframesInCurrentTab--;
if(_AET._expectedQtyOfIframesInCurrentTab==0){
_AET.setStopBrowserConfirmationDialog(false);
_AET._isSubmittingThePage=false;
_AET._expectedQtyOfIframesInCurrentTab=_AET._getQtyOfIframesByTabWidgetId(_AET._currentTabWidgetID);
}else{
_AET.setStopBrowserConfirmationDialog(true);
}
}
},_unload:function(){
dojo.forEach(_AET._unsubscribes,window.dojo.unsubscribe);
_AET._unsubscribes=[];
_AET.uHandlers&&_AET.uHandlers.forEach(function(_12){
_12.remove();
});
_AET.uHandlers=[];
_AET._tabWidgetIdsWithQtyOfIframes={};
},});
_AET._unsubscribes=[dojo.subscribe("curam/form/submit",function(){
_AET.setStopBrowserConfirmationDialog(true);
_AET._isSubmittingThePage=true;
}),dojo.subscribe("curam/redirect/logout",function(){
_AET.setStopBrowserConfirmationDialog(true);
_AET._isRedirectingToLogoutPage=true;
}),dojo.subscribe("curam/tab/selected",function(_13){
_AET._setCurrentTabWidgetID(_13);
_AET._expectedQtyOfIframesInCurrentTab=_AET._getQtyOfIframesByTabWidgetId(_13);
}),dojo.subscribe("curam/tab/quantityExpectedFrames/load",function(_14,_15){
_AET._putTabWidgetIdWithQtyOfIframes(_14,_15);
if(_AET._currentTabWidgetID&&_14==_AET._currentTabWidgetID){
_AET._expectedQtyOfIframesInCurrentTab=_15;
}
}),dojo.subscribe("curam/modal/close/ignoreRefresh",function(){
_AET.setStopBrowserConfirmationDialog(false);
}),dojo.subscribe("/curam/SessionTimeout/continueUsingApp",function(){
_AET.setStopBrowserConfirmationDialog(false);
}),dojo.subscribe("curam/tab/contextRefresh",function(_16){
_AET._decreaseQtyOfExpectedIframebyTab(_16);
}),dojo.subscribe("/curam/tab/closing",function(_17){
_AET._removeTabWidgetId(_17);
}),dojo.subscribe("/curam/frame/detailsPanelLoaded",function(){
_AET._decreaseQtyOfExpectedIframebyTab();
}),dojo.subscribe("/curam/main-content/page/loaded",function(){
_AET._decreaseQtyOfExpectedIframebyTab();
})];
_AET.uHandlers=[on(document,"mouseover",function(evt){
var e=evt||window.event;
if(_AET.isPointerOnWindow===false){
_AET.isPointerOnWindow=true;
}
}),on(window,"unload",_AET._unload),on(document,"mouseout",dojo.partial(_AET._checkMouseOutOfScreen,_4("ie")||_4("trident")||_4("edge")))];
return _5;
});
