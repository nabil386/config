//>>built
define("curam/util/ui/Application",["curam/util","curam/define"],function(){
curam.define.singleton("curam.util.ui.Application",{setUserLocale:function(_1){
var _2=document.getElementById("available-locales");
var _3=_2.options[_2.selectedIndex].text;
var _4=document.getElementById("returnUrl").value;
var w=curam.util.getTopmostWindow();
w.location=_1+"?r="+_4+"&l="+_3;
}});
return curam.util.ui.Application;
});
