//>>built
define("curam/widget/orgTree/treecontent",["dojo/dom-style","curam/util"],function(_1){
var _2=dojo.setObject("treecontent",{refreshTree:function(){
var _3=curam.util.getFrameRoot(window,"iegtree");
if(_3!=null){
if(window.location.search.indexOf("&isResolve")>-1||window.location.search.indexOf("&amp;isResolve")>-1){
return;
}
var _4=_3.navframe||_3.frames[0];
if(!_4.refreshIt){
_4.refreshIt=true;
return false;
}
_4.location.reload();
_3.curam.PAGE_INVALIDATED=false;
_4.curam.PAGE_INVALIDATED=false;
_4.refreshIt=false;
}
return true;
},refreshTreeFrame:function(){
var _5=window.location.href;
if(_5.indexOf("Action.do")>-1){
_5=_5.replace("Action.do","Page.do");
}
window.location.href=_5;
},redrawTreeContents:function(){
var _6=dojo.body().clientWidth;
_1.set(dojo.body(),{width:(_6-1)+"px"});
}});
return _2;
});
