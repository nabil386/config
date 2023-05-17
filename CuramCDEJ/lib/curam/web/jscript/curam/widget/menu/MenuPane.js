//>>built
define("curam/widget/menu/MenuPane",["dojo/_base/declare","dojo/_base/lang","dojo/on","dojo/dom-class","curam/widget/componentWrappers/ListWraper","curam/widget/form/ToggleButtonGroup","dojo/_base/window","dojo/dom-construct","dijit/TooltipDialog","dijit/popup","dojo/_base/fx","dojo/dom-style","dojox/layout/ExpandoPane","dojo/dom-geometry","dojo/aspect","dojo/keys","dijit/Tooltip","idx/widget/HoverCard","dojo/query","dojo/dom-style","dojo/has","dojo/dom-attr"],function(_1,_2,on,_3,_4,_5,_6,_7,_8,_9,fx,_a,_b,_c,_d,_e,_f,_10,_11,_12,has,_13){
var _14=_1("curam.widget.menu._MenuPaneButtonIndexer",null,{selectedButtonKey:-1,selectedButtonDisplayIndex:-1,expandButtonDisplayIndex:-1,_buttonDisplayOrderArrayOrginale:null,_buttonMap:null,_subPagenMap:null,_buttonPrimaryContainerArray:null,_buttonSecondaryContainerArray:null,constructor:function(_15){
this._buttonMap=[];
this._subPagenMap=[];
this._buttonDisplayOrderArrayOrginale=new Array();
this._buttonPrimaryContainerArray=new Array();
this._buttonSecondaryContainerArray=new Array();
},addNewButton:function(_16,key){
var _17={key:key,id:_16.id,button:_16,contextBox:null,displayOrderIndex:null,displayOrderOrginaleIndex:this._buttonDisplayOrderArrayOrginale.length};
this._buttonMap[key]=_17;
this._buttonDisplayOrderArrayOrginale.push(key);
},addButtonReferenceToPrimaryContainer:function(key,_18){
if(_18){
this._buttonPrimaryContainerArray.push(key);
}else{
this._buttonSecondaryContainerArray.push(key);
}
},getButton:function(key){
var _19=this._buttonMap[key];
return _19;
},setNewSubPage:function(_1a,_1b){
this._subPagenMap[_1a]=_1b;
},getSubPagePrimaryPage:function(_1c){
var _1d=this._subPagenMap[_1c];
return _1d;
},getButtonPrimary:function(_1e){
var key=this._buttonPrimaryContainerArray[_1e];
var _1f=this.getButton(key);
return _1f;
},getButtonSecondary:function(_20){
var key=this._buttonSecondaryContainerArray[_20];
var _21=this.getButton(key);
return _21;
},swapButtonFomPrimaryContainerToSecondaryContainer:function(_22){
if(_22){
var _23=this._buttonPrimaryContainerArray.pop();
this._buttonSecondaryContainerArray.unshift(_23);
}else{
var _23=this._buttonSecondaryContainerArray.shift();
this._buttonPrimaryContainerArray.push(_23);
}
},swapButtonContainerToContainer:function(_24,_25,_26){
if(_24){
var _27=this._buttonPrimaryContainerArray.splice(_25,1);
this._buttonSecondaryContainerArray.splice(_26,0,_27[0]);
}else{
var _27=this._buttonSecondaryContainerArray.splice(_25,1);
this._buttonPrimaryContainerArray.splice(_26,0,_27[0]);
}
},swapButtonContainerItemIndex:function(_28,_29,_2a){
if(_28){
var _2b=this._buttonPrimaryContainerArray.splice(_29,1);
this._buttonPrimaryContainerArray.splice(_2a,0,_2b[0]);
}else{
var _2b=this._buttonSecondaryContainerArray.splice(_29,1);
this._buttonSecondaryContainerArray.splice(_2a,0,_2b[0]);
}
},getWhichContinerFromIndex:function(_2c){
var _2d=0;
if(_2c>=this._buttonPrimaryContainerArray.length){
_2d=1;
}
return _2d;
}});
return _1("curam.widget.menu.MenuPane",[_b],{_listWrapper:null,_expandButton:null,_expandButtonContentBox:null,_toolTipDialogExpand:null,_toolTipDialogExpandContents:null,_fadeIn:null,_fadeOut:null,_menuPaneButtonIndexer:null,duration:300,_buttonSizerDiv:null,_buttonSizerList:null,_resizeResizeHandler:null,_showEndresizeResizeHandler:null,_hideEndResizeHandler:null,resizeDelay:250,_resizeDelayHandler:null,_previouseHeight:-1,_resizeStatusQue:1,_resizeStatusResizeing:0,_resizeStatusNotInUse:-1,_resizeCurentStatus:-1,_classNavMenu:"navMenu",_classNavMenuOverFlow:"navMenuOverFlow",_classCurramSideMenuButton:"curramSideMenuButton",_classCurramSideMenuButtonIcon:"curramSideMenuButtonIcon",_classCurramSideMenuOverFlowButton:"curramSideMenuOverFlowButton",_classCurramSideMenuOverFlowButtonIcon:"curramSideMenuOverFlowButtonIcon",_classCurramSideMenuOverFlowButtonExpand:"curramSideMenuOverFlowButtonExpand",_classCurramSideMenuOverFlowButtonExpandIcon:"curramSideMenuOverFlowButtonExpandIcon",constructor:function(_2e){
this.inherited(arguments);
this._menuPaneButtonIndexer=new _14();
},postCreate:function(){
this.inherited(arguments);
_3.add(this.titleWrapper,"dijitHidden");
this._expandButton=new dijit.form.Button({id:"navOverflowButton",baseClass:this._classCurramSideMenuOverFlowButtonExpand,iconClass:this._classCurramSideMenuOverFlowButtonExpandIcon,orgID:"exapnadButton",showLabel:false});
this._toolTipDialogExpandContentsListWrapper=new curam.widget.componentWrappers.ListWraper({listType:"ol",role:"menu",baseClass:this._classNavMenuOverFlow,_doBeforeItemSet:_2.hitch(this,function(_2f,_30){
if(_2f!=null){
if(this.isLeftToRight()){
_12.set(_2f.focusNode,"textAlign","left");
_12.set(_2f.containerNode,"marginRight","10px");
}else{
_12.set(_2f.focusNode,"textAlign","right");
_12.set(_2f.containerNode,"marginLeft","10px");
}
_12.set(_2f.containerNode,"padding","0px");
_2f.set("baseClass",this._classCurramSideMenuOverFlowButton);
_3.replace(_2f.domNode,this._classCurramSideMenuOverFlowButton,this._classCurramSideMenuButton);
_3.add(_2f.iconNode,this._classCurramSideMenuOverFlowButtonIcon);
}
})});
var _31=null;
if(has("ie")!=null&&has("ie")<9){
_31=_7.create("div");
}else{
_31=_7.create("nav");
}
_13.set(_31,"id","overFlowContainer");
_13.set(_31,"role","navigation");
this.overFlowContainer=_31;
this._toolTipDialogExpandContentsListWrapper.placeAt(_31);
this._listWrapper=new curam.widget.componentWrappers.ListWraper({listType:"ol",role:"menu",baseClass:this._classNavMenu,_doBeforeItemSet:_2.hitch(this,function(_32,_33){
if(_32!=null&&_32.orgID!="exapnadButton"){
_3.remove(_32.iconNode,this._classCurramSideMenuOverFlowButtonIcon);
if(has("ie")){
_3.remove(_32.domNode,"curramSideMenuOverFlowButtonHover");
}
_12.set(_32.focusNode,"textAlign","center");
if(this.isLeftToRight()){
_12.set(_32.containerNode,"marginRight","0px");
}else{
_12.set(_32.containerNode,"marginLeft","0px");
}
_32.set("baseClass",this._classCurramSideMenuButton);
_3.replace(_32.domNode,this._classCurramSideMenuButton,this._classCurramSideMenuOverFlowButton);
}
})});
if(has("ie")!=null&&has("ie")<9){
var _34=_7.create("div",null,this.containerNode);
_13.set(_34,"role","navigation");
this._listWrapper.placeAt(_34);
}else{
var _35=_7.create("nav",null,this.containerNode);
_13.set(_35,"role","navigation");
this._listWrapper.placeAt(_35);
}
this._fadeIn=fx.fadeIn({node:this._listWrapper.domNode,duration:this.duration,onEnd:_2.hitch(this,"_showContainer")});
this._fadeOut=fx.fadeOut({node:this._listWrapper.domNode,duration:this.duration,onEnd:_2.hitch(this,"_onHideEnd")});
this._resizeResizeHandler=_d.after(this,"resize",this._doResize,true);
this._showEndresizeResizeHandler=_d.after(this,"_showEnd",_2.hitch(this,"_onShowComplete"),false);
this._hideEndResizeHandler=_d.after(this,"_hideEnd",_2.hitch(this,"_onHideComplete"),false);
},startup:function(){
this.inherited(arguments);
},fadeIn:function(){
this._fadeIcons(true);
},fadeOut:function(){
this._fadeIcons(false);
},_fadeIcons:function(_36){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
if(_36==true){
if(this._fadeOut.status()=="playing"){
this._fadeOut.stop();
this._fadeIn.play();
}else{
if(this._fadeIn.status()!="playing"){
this._fadeIn.play();
}
}
}else{
if(this._fadeIn.status()=="playing"){
this._fadeIn.stop();
this._fadeOut.play();
}else{
if(this._fadeOut.status()!="playing"){
this._fadeOut.play();
}
}
}
},_showContainer:function(){
if(!this._showing){
this.toggle();
}
},_onShowComplete:function(){
},_onHideEnd:function(){
if(this._showing){
this.toggle();
}
},_onHideComplete:function(){
},addMenuItems:function(_37){
this._cleanDownExistingMenuItems();
dojo.forEach(_37,function(_38,i){
this._addMenuItem(_38,i);
},this);
this._initaleProcessMenuItems();
this._initalePlaceMenuItems();
},_cleanDownExistingMenuItems:function(){
this._removeButtonCacheContent();
this._toolTipDialogExpandContentsListWrapper.deleteAllChildern();
this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length=0;
this._removeExpandButton();
this._listWrapper.deleteAllChildern();
this._menuPaneButtonIndexer._buttonDisplayOrderArrayOrginale.length=0;
this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length=0;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.expandButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
for(var key in this._menuPaneButtonIndexer._subPagenMap){
delete this._menuPaneButtonIndexer._subPagenMap[key];
}
},setSelectedButton:function(_39){
if(_39.exceptionButtonFound==null){
_39.exceptionButtonFound=true;
}
if(this._menuPaneButtonIndexer.getButton(_39.key)==null&&this._menuPaneButtonIndexer.getSubPagePrimaryPage(_39.key)==null){
if(_39.exceptionButtonFound==false){
this._onSelectAfter(_39);
}else{
throw new Error("No button exists with the requested id : "+_39.key);
}
}else{
this._buttonSelected(_39,true);
}
},deselect:function(){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=-1){
var _3a=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
_3a.button.set("checked",false);
}
},_onSelectBefore:function(_3b){
},_onSelectAfter:function(_3c){
},_addMenuItem:function(_3d,_3e){
_3d=this._filterItem(_3d);
this._generateSubPageIndex(_3d.id,_3d.subPageIds);
var cb=_2.hitch(this,function(_3f){
var _40={key:_3f.orgID,param:[]};
this._buttonSelected(_40,false);
});
var but=new curam.widget.form.ToggleButtonGroup({label:_3d.label,orgID:_3d.id,groupName:"menuPaneCuramWidget",onClick:function(e){
cb(this);
},baseClass:this._classCurramSideMenuButton,iconClass:this._classCurramSideMenuButtonIcon});
if(_3d.iconPath!=null&&_2.trim(_3d.iconPath).length>0){
_a.set(but.iconNode,{backgroundImage:"url("+_3d.iconPath+")"});
}
if(_3d.selected!=null&&_3d.selected==true){
this._menuPaneButtonIndexer.selectedButtonKey=_3d.id;
}
this._menuPaneButtonIndexer.addNewButton(but,_3d.id);
},_generateSubPageIndex:function(_41,_42){
if(_42!=null&&_42.length>0){
dojo.forEach(_42,function(_43){
if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(_43)==null){
this._menuPaneButtonIndexer.setNewSubPage(_43,_41);
}else{
throw new Error("There has been a clash, sub page has all ready been registered.  Primary ID : "+_41+" Subpage ID : "+_43);
}
},this);
}
},_filterItem:function(_44){
return _44;
},_initaleProcessMenuItems:function(){
var _45=dojo.contentBox(this.domNode);
if(this._showing==false){
_45.w=this._showSize;
}
this._buttonSizerDiv=_7.create("div",{style:{height:_45.h+"px",width:_45.w+"px"}});
_3.add(this._buttonSizerDiv,"dijitOffScreen");
dojo.place(this._buttonSizerDiv,_6.body());
this._buttonSizerList=new curam.widget.componentWrappers.ListWraper({listType:"ol",baseClass:this._classNavMenu}).placeAt(this._buttonSizerDiv);
for(var key in this._menuPaneButtonIndexer._buttonMap){
var _46=this._menuPaneButtonIndexer.getButton(key);
if(_46.button){
this._buttonSizerList.set("item",_46.button.domNode);
var _47=dojo.contentBox(_46.button.domNode);
this._menuPaneButtonIndexer.getButton(key).contextBox=_47;
}
}
this._buttonSizerList.set("item",this._expandButton.domNode);
this._expandButtonContentBox=dojo.contentBox(this._expandButton.domNode);
_3.add(this._expandButton.domNode,"dijitHidden");
_7.place(this._expandButton.domNode,_6.body());
this._toolTipDialogExpand=new idx.widget.HoverCard({draggable:false,hideDelay:450,showDelay:0,target:this._expandButton.id,content:this.overFlowContainer,forceFocus:true,focus:_2.hitch(this,function(){
var _48=this._menuPaneButtonIndexer.getButtonSecondary(0);
_48.button.focus();
}),defaultPosition:["after-centered","before-centered"],moreActions:[],actions:[]});
_13.remove(this._toolTipDialogExpand.focusNode.parentNode,"role");
_3.add(this._toolTipDialogExpand.domNode,"dijitHidden");
_3.add(_11(".idxOneuiHoverCardFooter",this._toolTipDialogExpand.bodyNode)[0],"dijitHidden");
_3.add(this._toolTipDialogExpand.gripNode,"dijitHidden");
_3.add(this._toolTipDialogExpand.actionIcons,"dijitHidden");
_3.add(this._toolTipDialogExpand.moreActionsNode,"dijitHidden");
},_initalePlaceMenuItems:function(){
var _49=0;
for(var key in this._menuPaneButtonIndexer._buttonMap){
var _4a=this._menuPaneButtonIndexer.getButton(key);
if(_4a.button.get("checked")){
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=_49;
this._menuPaneButtonIndexer.selectedButtonKey=key;
}
_4a.displayOrderOrginaleIndex=_49;
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>(this._expandButtonContentBox.h+_4a.contextBox.h)){
this._listWrapper.set("item",_4a.button);
this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key,true);
}else{
this._addExpandButton(_49);
this._toolTipDialogExpandContentsListWrapper.set("item",_4a.button);
this._menuPaneButtonIndexer.addButtonReferenceToPrimaryContainer(key,false);
if(_49==this._menuPaneButtonIndexer.selectedButtonDisplayIndex){
selectedIndexPositionTemp=this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length-1;
}
}
if(_49==0){
idcar=_4a.button.id;
}
_49++;
}
this._buttonSizerList.destroy();
_7.destroy(this._buttonSizerDiv);
if(this._menuPaneButtonIndexer.selectedButtonKey!=-1){
var _4b=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
_4b.button._onClick();
}
},_addExpandButton:function(_4c){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1){
console.info("add expando");
this._menuPaneButtonIndexer.expandButtonDisplayIndex=_4c;
_3.remove(this._expandButton.domNode,"dijitHidden");
this._listWrapper.set("item",this._expandButton);
}
},_removeExpandButton:function(){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length==0){
this._menuPaneButtonIndexer.expandButtonDisplayIndex=-1;
console.info("Remove expando : "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
_3.add(this._expandButton.domNode,"dijitHidden");
_7.place(this._expandButton.domNode,_6.body());
this._listWrapper.deleteChild(this._listWrapper.get("ItemCount"));
}
},_doResize:function(_4d){
if(_4d!=null&&_4d.h!=null&&_4d.h>10){
if(this._previouseHeight!=_4d.h&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
if(this.resizeDelay>0){
if(this._resizeDelayHandler!=null){
this._resizeDelayHandler.remove();
}
this._previouseHeight=_4d.h;
if(this._toolTipDialogExpand){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
}
var cb=_2.hitch(this,function(){
this._callRepositionButtons();
});
this._resizeDelayHandler=this.defer(cb,this.resizeDelay);
}else{
this._callRepositionButtons();
}
}
}
},_callRepositionButtons:function(){
if(this._resizeCurentStatus==this._resizeStatusNotInUse){
this._positionButtonDuringResize();
}else{
this._resizeCurentStatus==this._resizeStatusQue;
}
},_positionButtonDuringResize:function(){
this._resizeCurentStatus=this._resizeStatusResizeing;
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0&&this.get("ContainerHeight")<this._listWrapper.get("ContainerHeight")){
this._addExpandButton(this._listWrapper.get("ItemCount"));
var _4e=1;
while((this.get("ContainerHeight")<this._listWrapper.get("ContainerHeight"))&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
if(_4e==2&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length==1){
_4e=1;
}
var _4f=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-_4e;
var _50=this._menuPaneButtonIndexer.getButtonPrimary(_4f);
if(_50.button.get("checked")&&this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>1){
_4e=2;
console.info(_4f+" : I am checked *************************  = "+_50.button.get("checked"));
}else{
console.info("selected = "+_50.button.get("checked"));
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,_4f,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_50.button,"first");
this._listWrapper.deleteChild(_4f);
this._menuPaneButtonIndexer.expandButtonDisplayIndex--;
if(_4e==2){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=0){
this._menuPaneButtonIndexer.selectedButtonDisplayIndex--;
}else{
}
}
}
}
console.info("Move from main to popup-----------------");
}else{
if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0&&this.get("ContainerHeight")>this._listWrapper.get("ContainerHeight")){
console.info(" secondary container size = "+this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length);
console.info("Move from popup to main****************");
var _51=true;
while(_51&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0){
var _52=0;
var _53=this._menuPaneButtonIndexer.getButtonSecondary(_52);
if(_53!=null&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>_53.contextBox.h){
var _54=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length;
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1){
this._menuPaneButtonIndexer.expandButtonDisplayIndex++;
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _55=this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
if(_55.button.get("checked")&&_55.displayOrderOrginaleIndex>=_54){
if(_54!=0){
_54--;
}
this._menuPaneButtonIndexer.selectedButtonDisplayIndex++;
}
}
}
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,0,_54);
this._listWrapper.set("item",_53.button,_54);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_52);
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex!=-1&&this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length<=0){
this._removeExpandButton();
}
}else{
_51=false;
}
}
}else{
}
}
if(this._resizeCurentStatus<=this._resizeStatusResizeing){
this._resizeCurentStatus=this._resizeStatusNotInUse;
}else{
this._positionButtonDuringResize.apply(this);
}
},_buttonSelected:function(_56,_57){
this._toolTipDialogExpand.hide(this._expandButton.domNode);
var _58;
if(this._menuPaneButtonIndexer.getButton(_56.key)!=null){
_58=this._menuPaneButtonIndexer.getButton(_56.key);
}else{
if(this._menuPaneButtonIndexer.getSubPagePrimaryPage(_56.key)!=null){
var _59=this._menuPaneButtonIndexer.getSubPagePrimaryPage(_56.key);
_58=this._menuPaneButtonIndexer.getButton(_59);
}else{
throw new Error("state unknow for requested selected button : "+_56.key);
}
}
_58.button.set("checked",true);
this._onSelectBefore(_56);
this._positionSelectedButton(_58);
if(this._menuPaneButtonIndexer._buttonSecondaryContainerArray.length>0){
this._previouseHeight++;
this._callRepositionButtons();
}
this._onSelectAfter(_56);
},_positionSelectedButton:function(_5a){
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=-1){
var _5b=this._menuPaneButtonIndexer.getButton(this._menuPaneButtonIndexer.selectedButtonKey);
var _5c=_5b.displayOrderOrginaleIndex;
if(this._menuPaneButtonIndexer.selectedButtonDisplayIndex!=_5c){
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _5d=_5c-(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
var _5e=this._menuPaneButtonIndexer.getButtonSecondary(0);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,this._menuPaneButtonIndexer.selectedButtonDisplayIndex,_5d);
this._toolTipDialogExpandContentsListWrapper.set("item",_5b.button,_5d);
this._listWrapper.deleteChild(this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,0,this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._listWrapper.set("item",_5e.button,this._menuPaneButtonIndexer.selectedButtonDisplayIndex);
this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
}else{
var _5d=_5b.displayOrderOrginaleIndex;
this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false,0,_5d);
this._toolTipDialogExpandContentsListWrapper.set("item",_5b.button,_5d+1);
this._toolTipDialogExpandContentsListWrapper.deleteChild(0);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
this._menuPaneButtonIndexer.selectedButtonKey=-1;
}
}else{
console.info("no need to repostion old selected button");
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=-1;
}
}
var _5c=_5a.displayOrderOrginaleIndex;
if(this._menuPaneButtonIndexer.getWhichContinerFromIndex(_5c)==1){
if(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length>0){
var _5f=_5c-(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
var _60=this._menuPaneButtonIndexer.getButtonPrimary(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(true,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_60.button,0);
this._listWrapper.deleteChild(this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
this._menuPaneButtonIndexer.swapButtonContainerToContainer(false,_5f+1,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length);
this._listWrapper.set("item",_5a.button,this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_5f+1);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=this._menuPaneButtonIndexer._buttonPrimaryContainerArray.length-1;
this._menuPaneButtonIndexer.selectedButtonKey=_5a.key;
}else{
this._menuPaneButtonIndexer.swapButtonContainerItemIndex(false,_5c,0);
this._toolTipDialogExpandContentsListWrapper.set("item",_5a.button,0);
this._toolTipDialogExpandContentsListWrapper.deleteChild(_5c+1);
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=0;
this._menuPaneButtonIndexer.selectedButtonKey=_5a.key;
}
}else{
this._menuPaneButtonIndexer.selectedButtonDisplayIndex=_5c;
this._menuPaneButtonIndexer.selectedButtonKey=_5a.key;
console.info("no need to repostion New selected button :"+_5c+" key = "+_5a.key);
}
},_placeMenuItems:function(_61,_62){
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1&&(this.get("ContainerHeight")-this._listWrapper.get("ContainerHeight"))>(this._expandButtonContentBox.h+_61.contextBox.h)){
this._listWrapper.set("item",_61.button);
}else{
if(this._menuPaneButtonIndexer.expandButtonDisplayIndex==-1){
this._menuPaneButtonIndexer.expandButtonDisplayIndex=_62;
_3.remove(this._expandButton.domNode,"dijitHidden");
this._listWrapper.set("item",this._expandButton);
}
this._toolTipDialogExpandContentsListWrapper.set("item",_61.button);
}
},_getContainerHeightAttr:function(){
var _63=_c.getContentBox(this.containerNode);
return _63.h;
},_setWidthAttr:function(_64){
if(this._showing){
}else{
this._showAnim.properties.width=_64;
this._showSize=_64;
this._currentSize.w=_64;
}
},_removeButtonCacheContent:function(){
for(var key in this._menuPaneButtonIndexer._buttonMap){
var _65=this._menuPaneButtonIndexer.getButton(key);
if(_65.button){
_65.button.destroy();
}
delete _65.button;
delete _65.contextBox;
delete _65.displayOrderIndex;
delete _65.displayOrderOrginaleIndex;
delete _65.id;
delete _65.key;
delete _65;
delete this._menuPaneButtonIndexer._buttonMap[key];
}
},destroy:function(){
try{
this._resizeCurentStatus=this._resizeStatusNotInUse;
this._resizeDelayHandler!=null?this._resizeDelayHandler.remove():null;
this._resizeResizeHandler.remove();
this._showEndresizeResizeHandler.remove();
this._hideEndResizeHandler.remove();
this._expandButton.destroy();
this._removeButtonCacheContent();
this._toolTipDialogExpandContentsListWrapper.destroy();
this._toolTipDialogExpand.destroy();
this._listWrapper.destroy();
delete this._menuPaneButtonIndexer;
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
