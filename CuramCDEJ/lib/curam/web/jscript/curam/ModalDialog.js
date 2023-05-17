//>>built
require({cache:{"url:curam/layout/resources/CuramBaseModal.html":"<div class=\"dijitDialog\" role=\"dialog\" aria-labelledby=\"${id}_title\" aria-live=\"assertive\">\n\t<div data-dojo-attach-point=\"titleBar\" class=\"dijitDialogTitleBar\">\n\t\t<span data-dojo-attach-point=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\"\n\t\t\t\trole=\"heading\" level=\"2\"></span>\n\t\t<button data-dojo-attach-point=\"closeButtonNode\" class=\"dijitDialogCloseIcon\"\n          data-dojo-attach-event=\"ondijitclick: onCancel\" title=\"${buttonCancel}\"\n          role=\"button\" tabindex=\"0\" aria-label=\"${closeModalText}\" \n          onKeyDown=\"require(['curam/ModalDialog'], function(md) {md.handleTitlebarIconKeydown(event)});\"\n          style=\"visibility: hidden;\">\n      \t\t<img src=\"${buttonCloseIcon}\" alt=\"${closeModalText}\" class=\"button-close-icon-default\"/>\n      \t\t<img src=\"${buttonCloseIconHover}\" alt=\"${closeModalText}\" class=\"button-close-icon-hover\"/>\n\t\t\t<span data-dojo-attach-point=\"closeText\" class=\"closeText\" title=\"${buttonCancel}\">${closeModalText}</span>\n\t    </button>\n\t</div>\n\t<div data-dojo-attach-point=\"containerNode\" class=\"dijitDialogPaneContent\"></div>\n\t${!actionBarTemplate}\n</div>\n"}});
define("curam/ModalDialog",["dojo/text!curam/layout/resources/CuramBaseModal.html","dojo/_base/declare","dojo/dom","curam/modal/CuramBaseModal","dijit/Dialog"],function(_1,_2,_3,_4){
tabbingBackwards=null,handleTitlebarIconKeydown=function(e){
};
var _5=_2("curam.ModalDialog",[dijit.Dialog,_4],{constructor:function(){
this.inherited(arguments);
},postCreate:function(){
this.inherited(arguments);
},handleFocusAtEnd:function(_6,_7){
this.handleTabbingForwards(_6,_7);
}});
_5.handleFocusAtEnd=function(_8){
var _9=dojo.query("#"+this.id+" .dijitDialogHelpIcon")[0];
curam.ModalDialog.prototype.handleFocusAtEnd(_8,_9);
};
return _5;
});
