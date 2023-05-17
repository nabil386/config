//>>built
require({cache:{"url:dijit/templates/MenuItem.html":"<tr class=\"dijitReset\" data-dojo-attach-point=\"focusNode\" role=\"menuitem\" tabIndex=\"-1\">\n\t<td class=\"dijitReset dijitMenuItemIconCell\" role=\"presentation\">\n\t\t<span role=\"presentation\" class=\"dijitInline dijitIcon dijitMenuItemIcon\" data-dojo-attach-point=\"iconNode\"></span>\n\t</td>\n\t<td class=\"dijitReset dijitMenuItemLabel\" colspan=\"2\" data-dojo-attach-point=\"containerNode,textDirNode\"\n\t\trole=\"presentation\"></td>\n\t<td class=\"dijitReset dijitMenuItemAccelKey\" style=\"display: none\" data-dojo-attach-point=\"accelKeyNode\"></td>\n\t<td class=\"dijitReset dijitMenuArrowCell\" role=\"presentation\">\n\t\t<span data-dojo-attach-point=\"arrowWrapper\" style=\"visibility: hidden\">\n\t\t\t<span class=\"dijitInline dijitIcon dijitMenuExpand\"></span>\n\t\t\t<span class=\"dijitMenuExpandA11y\">+</span>\n\t\t</span>\n\t</td>\n</tr>\n"}});
define("curam/widget/MenuItem",["dijit/MenuItem","dojo/_base/declare","dojo/text!dijit/templates/MenuItem.html"],function(_1,_2,_3){
var _4=_2("curam.widget.MenuItem",_1,{templateString:_3,onClickValue:"",_onClickAll:function(_5){
var _6=curam.tab.getTabContainer();
var _7=_6.getChildren();
for(var i=0;i<_7.length;i++){
if(_7[i].closable){
_6.closeChild(_7[i]);
}
}
},_setLabelAttr:function(_8){
var _9=/<[a-zA-Z\/][^>]*>/g;
var _a=_8.replace(_9,"");
this._set("label",_a);
var _b="";
var _c;
var _d=_8.search(/{\S}/);
if(_d>=0){
_b=_8.charAt(_d+1);
var _e=_8.substr(0,_d);
var _f=_8.substr(_d+3);
_c=_e+_b+_f;
_8=_e+"<span class=\"dijitMenuItemShortcutKey\">"+_b+"</span>"+_f;
}else{
_c=_8;
}
_c=_c.replace(_9,"");
this.domNode.setAttribute("aria-label",_c+" "+this.accelKey);
this.containerNode.innerHTML=_8;
this._set("shortcutKey",_b);
}});
return _4;
});
