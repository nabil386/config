//>>built
define("curam/util/external",["curam/util"],function(_1){
curam.define.singleton("curam.util.external",{inExternalApp:function(){
return jsScreenContext.hasContextBits("EXTAPP");
},getUimParentWindow:function(){
if(_1.getTopmostWindow()===dojo.global){
return null;
}else{
return dojo.global;
}
}});
return curam.util.external;
});
