//>>built
define("curam/wizard",["curam/define","curam/debug","curam/util/Dialog"],function(_1,_2,_3){
_1.singleton("curam.wizard",{setupTargetFrameForModals:function(_4){
_2.log("curam.wizard.setupTargetFrameForModals "+_2.getProperty("curam.wizard.called"));
curam.util.Dialog.init();
curam.util.Dialog.registerGetTitleFunc(function(){
return _4;
});
dojo.addOnLoad(function(){
curam.util.Dialog.pageLoadFinished();
});
}});
return curam.wizard;
});
