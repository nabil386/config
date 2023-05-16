//>>built
function Wizard(){
var _1=false;
var _2=false;
var _3=false;
var _4;
};
Wizard.prototype.setButtonBarReady=function(){
this.buttonBarReady=true;
this.notifyNavigator();
};
Wizard.prototype.setNavigatorReady=function(){
this.navigatorReady=true;
this.notifyNavigator();
};
Wizard.prototype.setContentReady=function(_5){
this.pageId=_5;
this.contentReady=true;
this.notifyNavigator();
};
Wizard.prototype.notifyNavigator=function(){
if(this.buttonBarReady&&this.navigatorReady&&this.contentReady){
var _6=window.navframe;
this.nav=_6.wizardNavigator;
this.nav.newContent(this.pageId);
this.nav.registerForm(this.pageId);
this.nav.optShowNavigator();
_6.frameElement.navRef=this.nav;
_6.frameElement.onresize=this.nav.handleResize;
}
};
Wizard.prototype.changeFramesetStyle=function(){
var _7=document.getElementsByTagName("frameset")[0].style;
_7.margin="6px";
};
dojo.subscribe("curam/agendaplayer/page/loaded",function(_8,_9){
require(["curam/dialog"],function(_a){
var _b=_a.getParentWindow(window);
var _c=_b.curam.util.getTopmostWindow(),_d=_c.document.querySelector(".bx--modal");
var _e=_c.dijit.byId(_d.parentNode.id);
_e.modalContainer.querySelector(".bx--modal-content--overflow-indicator").remove();
_e.modalContainer.classList.add("bx--modal-container--progress");
_e.containerNode.classList.add("bx--modal-content--with-own-footer");
_e.containerNode.classList.remove("bx--modal-scroll-content");
window.frameElement.setAttribute("style","height:100%");
});
});
