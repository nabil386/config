//>>built
define("curam/ui/SectionShortcutsPanel",["curam/inspection/Layer","curam/define","curam/tab","curam/util","curam/ui/UIController"],function(_1){
var _2=curam.define.singleton("curam.ui.SectionShortcutsPanel",{handleClickOnAnchorElement:function(_3,_4){
if(!_4){
curam.tab.getTabController().handleUIMPageID(_3);
}else{
curam.ui.SectionShortcutsPanel.openInModal(_3);
}
},handleClick:function(_5,_6){
var _7=eval(_5+"JsonStore");
var _8=_7.getValue(_6,"pageID");
var _9=_7.getValue(_6,"openInModal");
if(!_9){
curam.tab.getTabController().handleUIMPageID(_8);
}else{
curam.ui.SectionShortcutsPanel.openInModal(_8);
}
},openInModal:function(_a){
var _b=_a+"Page.do";
var _c={};
curam.tab.getTabController().handleLinkClick(_b,_c);
},setupCleanupScript:function(_d){
dojo.ready(function(){
var _e=eval(_d+"JsonStore");
dojo.addOnWindowUnload(function(){
_e.close();
});
});
}});
_1.register("curam/ui/SectionShortcutsPanel",this);
return _2;
});
