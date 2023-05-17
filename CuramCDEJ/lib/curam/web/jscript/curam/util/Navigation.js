//>>built
define("curam/util/Navigation",["curam/util","curam/tab","curam/define"],function(){
curam.define.singleton("curam.util.Navigation",{goToPage:function(_1,_2){
var _3=_1+"Page.do"+curam.util.makeQueryString(_2);
curam.util.Navigation.goToUrl(_3);
},goToUrl:function(_4){
curam.tab.getTabController().processURL(_4);
}});
return curam.util.Navigation;
});
