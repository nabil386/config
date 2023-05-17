//>>built
define("curam/widget/SearchMultipleTextBoxHookPoints",["dojo/topic"],function(_1){
curam.define.singleton("curam.widget.SearchMultipleTextBoxHookPoints",{_preNavigationHookCompleted:function(){
_1.publish("/smartnavigator/prenavigationhook/completed");
}});
return curam.widget.SearchMultipleTextBoxHookPoints;
});
