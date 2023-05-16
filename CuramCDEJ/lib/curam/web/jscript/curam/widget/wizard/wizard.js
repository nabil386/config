//>>built
define("wizard",["dojo/dom","dojo/dom-style","dojo/dom-attr","dojo/dom-class","dojo/dom-construct","dojo/ready","curam/define","curam/util","curam/dialog"],function(_1,_2,_3,_4,_5,_6){
var _7=curam.util.getFrameRoot;
var _8=_7(window,"wizard");
function _9(){
};
_9.prototype.init=function(){
var _a=document.getElementById("wizard_summary");
var _b=_8.wizard.nav;
var _c=document.getElementById("visited");
var _d=document.getElementById("unvisited");
var _e;
_5.empty(_c);
_5.empty(_d);
for(i=0;i<_b.getLengthWithoutSummary();i++){
_e=_5.create("li",{});
_e.appendChild(document.createTextNode(_b.pages[i].title));
if(_b.isVisited(i)){
_c.appendChild(_e);
}else{
_d.appendChild(_e);
}
}
};
function _f(){
var _10;
var _11;
};
_f.prototype.initButtonBar=function(_12,_13,_14,_15){
this.buttonHandlers=new Array();
this.buttonHandlers["wizard_back"]=this.wizard_back;
this.buttonHandlers["wizard_forward"]=this.wizard_forward;
this.buttonHandlers["wizard_finish"]=this.wizard_finish;
this.buttonHandlers["wizard_quit"]=this.wizard_quit;
this.outOfWizard=false;
_3.set(dojo.byId("wizard_back"),{"title":_12,"aria-label":_12});
_3.set(dojo.byId("wizard_forward"),{"title":_13,"aria-label":_13});
_3.set(dojo.byId("wizard_finish"),{"title":_15,"aria-label":_15});
_3.set(dojo.byId("wizard_quit"),{"title":_14,"aria-label":_14});
_7(window,"wizard").wizard.setButtonBarReady();
};
_f.prototype.getNavigator=function(){
if(!this.wizNavigator){
this.wizNavigator=_7(window,"wizard").wizard.nav;
}
return this.wizNavigator;
};
_f.prototype.wizard_back=function(){
this.getNavigator().previous();
};
_f.prototype.wizard_forward=function(){
this.getNavigator().next();
};
_f.prototype.wizard_finish=function(){
this.getNavigator().finish();
};
_f.prototype.wizard_quit=function(){
this.getNavigator().quit();
};
_f.prototype.optionalEnable=function(_16,_17){
if(_17){
this.enable(_16);
}
};
_f.prototype.optionalDisable=function(_18,_19){
if(_19){
this.disable(_18);
}
};
_f.prototype.enable=function(_1a){
var _1b=_1a.parentNode;
var _1c=_5.create("a",{href:"#",title:_1a.getAttribute("title"),id:_1a.getAttribute("id")});
_1c.className="ac disabled";
var _1d=_5.create("span",{"class":"left-corner"},_1c);
var _1e=_5.create("span",{"class":"right-corner"},_1d);
var _1f=_5.create("span",{"class":"middle"},_1e);
_1f.appendChild(document.createTextNode(_1a.getAttribute("title")));
_1b.replaceChild(_1c,_1a);
_1c._conn=dojo.connect(_1c,"onclick",this,this.buttonHandlers[_1a.getAttribute("id")]);
};
_f.prototype.disable=function(_20){
dojo.disconnect(_20._conn);
var _21=_20.parentNode;
var _22=_5.create("span",{title:_20.getAttribute("title"),id:_20.getAttribute("id")});
_22.className="disabled-button";
var _23=_5.create("span",{"class":"left-corner"},_22);
var _24=_5.create("span",{"class":"right-corner"},_23);
var _25=_5.create("span",{"class":"middle chupa"},_24);
_25.appendChild(document.createTextNode(_20.getAttribute("title")));
_21.replaceChild(_22,_20);
};
_f.prototype.enableButtons=function(_26,_27){
var nav=this.getNavigator();
if(!nav.isLastPage()){
this.enableSummary();
if(!_27){
this.enable(document.getElementById("wizard_forward"));
}else{
this.disable(document.getElementById("wizard_forward"));
this.disableFwdLinks();
}
}else{
this.disableSummary();
this.disable(document.getElementById("wizard_forward"));
}
this.enable(document.getElementById("wizard_quit"));
if(!nav.isFirstPage()){
if(!_26){
this.enable(document.getElementById("wizard_back"));
if(nav.currentPage<nav.getLengthWithoutSummary()){
this.enableBackLinks();
}
}else{
this.disable(document.getElementById("wizard_back"));
this.disableLinks(true);
}
}else{
this.enable(document.getElementById("wizard_back"));
document.getElementById("wizard_back").style.display="none";
}
};
_f.prototype.disableButtons=function(){
this.disable(document.getElementById("wizard_back"));
this.disable(document.getElementById("wizard_forward"));
this.disableSummary();
this.disable(document.getElementById("wizard_quit"));
this.disableLinks();
};
_f.prototype.disableLinks=function(_28){
var nav=this.getNavigator();
var _29=_28?nav.currentPage:nav.getLengthWithoutSummary();
if(nav.modeInd==1){
for(k1=0;k1<_29;k1++){
nav.dropLink(k1);
}
}
nav.backLinksDisabled=_28;
};
_f.prototype.disableFwdLinks=function(){
var nav=this.getNavigator();
if(nav.modeInd==1){
for(k2=nav.currentPage+1;k2<nav.getLengthWithoutSummary();k2++){
nav.dropLink(k2);
}
}
};
_f.prototype.reenableLinks=function(){
var nav=this.getNavigator();
if(this.outOfWizard){
if(nav.modeInd>0){
nav.restoreLinks(nav.currentPage);
}
nav.highlight(nav.currentPage);
this.outOfWizard=false;
}
};
_f.prototype.enableBackLinks=function(){
var nav=this.getNavigator();
if(nav.backLinksDisabled){
if(!this.outOfWizard){
if(nav.modeInd>1){
nav.restoreLinks(nav.currentPage);
}
}
nav.backLinksDisabled=false;
}
};
_f.prototype.enableSummary=function(){
var nav=this.getNavigator();
if(nav.summary.summaryNum>-1){
nav.summary.summaryLink.onclick=nav.finish;
nav.summary.sumObject.className="summaryDiv";
if(nav.claimantScheme==true){
var _2a=_1.byId("wizard_finish");
_3.set(_2a,"tabIndex","3");
this.enable(_2a);
}
}
};
_f.prototype.disableSummary=function(){
var nav=this.getNavigator();
if(nav.summary.summaryNum>-1){
nav.summary.summaryLink.onclick=null;
if(nav.summary.sumObject.className.indexOf("highlightSummary")>-1){
nav.summary.sumObject.className="summaryDisabled highlightSummary";
}else{
nav.summary.sumObject.className="summaryDisabled";
}
if(nav.claimantScheme==true){
var _2b=_1.byId("wizard_finish");
_3.set(_2b,"tabIndex","-1");
this.disable(_2b);
}
}
};
_f.prototype.showFinishButton=function(){
var _2c=_1.byId("wizard_finish");
_3.set(_2c,"tabIndex","3");
_2.set(_2c,"display","");
_2c.className="restored";
this.enable(_1.byId("wizard_finish"));
};
function _2d(_2e,_2f){
this.name=_2e;
this.title=_2f;
this.liRef=null;
this.imgRef=null;
this.linkRef=null;
this.visited=false;
this.sectionInd=-1;
};
function _30(idx){
this.sId=idx;
this.firstPending=false;
this.itself=null;
this.textWrapper=null;
this.children=new Array();
this.isHidden=false;
this.expander=null;
};
function _31(_32,_33){
this.summaryNum=-1;
this.sumObject=null;
this.summaryLink=null;
this.clicked=false;
this.name=null;
this.title=null;
this.closeOnSubmit=false;
};
function _34(_35,_36,_37){
this.exitPage=encodeURI(_35);
this.currentPage=_36;
this.returnPage=_36;
this.listRef=null;
this.summaryRef=null;
this.ulRef=null;
this.length=0;
this.modeInd=((_37=="full")?2:(_37=="basic")?0:1);
this.buttonPane=_7(window,"wizard").buttonframe;
this.contentPane=_7(window,"wizard").targetframe;
this.parentPage=parent;
this.params=new Array();
this.sections=new Array();
this.sectionTitles=new Array();
this.pages=new Array();
this.summary=new _31();
this.disableBackList=new Array();
this.disableForwardList=new Array();
this.delegatesSubmit=new Array();
this.backLinksDisabled=false;
this.visible=false;
this.quitConfirm=false;
this.quitQuestion="Quitting?";
this.claimantScheme=false;
this.wrappedTo=-1;
this.wasWrappedTo=-1;
window.wizardNavigator=this;
};
_34.prototype.addDisableBack=function(_38,_39){
this.disableBackList[_38]=_39;
};
_34.prototype.setVisible=function(){
this.visible=true;
var _3a=_7(window,"wizard");
_3a.dojo.publish("curam/agendaplayer/navigator/loaded",["Navigation frame sample title"]);
};
_34.prototype.setQuitConfirm=function(_3b){
this.quitConfirm=true;
this.quitQuestion=_3b;
};
_34.prototype.setClaimantScheme=function(){
_7(window,"wizard").wizard.changeFramesetStyle();
_3.set(dojo.body(),"tabindex","-1");
this.claimantScheme=true;
};
_34.prototype.addDisableForward=function(_3c,_3d){
this.disableForwardList[_3c]=_3d;
};
_34.prototype.addFormRef=function(_3e){
this.delegatesSubmit[_3e]="assumed";
};
_34.prototype.setPageList=function(_3f){
for(i0=0;i0<_3f.length;i0++){
this.pages[i0]=new _2d(_3f[i0],"");
}
this.length=_3f.length;
};
_34.prototype.setPageTitleList=function(_40){
for(i1=0;i1<_40.length;i1++){
this.pages[i1].title=(i1+1)+"."+_40[i1];
}
};
_34.prototype.setSectionTitleList=function(_41){
this.sectionTitles=_41;
};
_34.prototype.setSummary=function(_42,_43,num,_44){
this.summary.name=_42;
this.summary.title=_43;
this.summary.summaryNum=num;
this.summary.closeOnSubmit=_44||false;
};
_34.prototype.initHoldersAndLinks=function(){
var s1=-1;
var _45=document.getElementsByTagName("li");
var i2=0;
for(;i2<_45.length;i2++){
if(_45[i2].id.indexOf("s_")==0){
var idx=_45[i2].id.split("_",2)[1];
_45[i2].getElementsByTagName("div")[0].appendChild(document.createTextNode(this.sectionTitles[idx]));
s1++;
this.sections[s1]=new _30(s1);
this.sections[s1].firstPending=true;
this.sections[s1].itself=_45[i2];
this.sections[s1].textWrapper=_45[i2].getElementsByTagName("div")[0];
this.sections[s1].expander=document.getElementById("si_"+s1);
this.sections[s1].expander.imgNav=this;
this.sections[s1].expander.mySectionNum=s1;
this.sections[s1].expander.onclick=this.toggleSection;
}else{
if(_45[i2].id.indexOf("nav_")==0){
var idx=_45[i2].id.split("_",2)[1];
_45[i2].appendChild(document.createTextNode(this.pages[idx].title));
this.pages[idx].liRef=_45[i2];
this.pages[idx].imgRef=this.pages[idx].liRef.getElementsByTagName("img")[0]||"no_img";
if(s1>-1){
if(this.sections[s1].firstPending==true){
this.pages[idx].liRef.isFirst=true;
this.pages[idx].liRef.className="first";
this.sections[s1].firstPending=false;
}
this.pages[idx].sectionInd=s1;
this.sections[s1].children[this.sections[s1].children.length]=this.pages[idx].liRef;
}
}
}
}
if(this.summary.summaryNum>-1){
var _46=this.summary.summaryNum;
this.summary.sumObject=document.getElementById("sum_0");
this.summary.summaryLink=this.summary.sumObject.getElementsByTagName("a")[0];
this.summary.summaryLink.innerHTML=this.summary.title;
this.summary.summaryLink.onclick=this.finish;
this.summary.summaryLink.navigator=this;
this.pages[_46]=new _2d(this.summary.name,this.summary.title);
this.pages[_46].liRef=this.summary.sumObject;
this.pages[_46].imgRef=this.pages[_46].liRef.getElementsByTagName("img")[0]||"no_img";
this.length=_46+1;
this.summaryRef=document.getElementById("swrap");
}
this.listRef=document.getElementById("wizardItems");
this.ulRef=document.getElementById("fullList");
};
_34.prototype.initContent=function(){
_4.add(dojo.body(),"wizard-navigation");
this.initHoldersAndLinks();
dojo.body().style.width="100%";
this.initProgressBar();
if(this.currentPage>0){
for(var ii=0;ii<this.currentPage;ii++){
this.pages[ii].visited=true;
}
}
if(this.modeInd>0){
this.restoreLinks(this.currentPage,this.currentPage);
if(this.modeInd==1){
this.foldSections(true);
this.unfoldSections(this.currentPage);
this.adjustSectionStyle(0);
}
}
if(this.summary.summaryNum==-1){
this.listRef.style.height="100%";
}else{
var _47=_7(window,"wizard").navframe.frameElement.clientHeight;
this.listRef.style.height=_47-_2.get(this.summaryRef,"height");
}
if(this.getItemPos()>this.listRef.clientHeight){
this.listRef.className="itemsList";
this.adjustScrollBar();
this.adjustFirstWrapping(this.wasWrappedTo-1);
}
this.wrapSections();
this.setContent(this.currentPage);
_7(window,"wizard").wizard.setNavigatorReady();
};
_34.prototype.wrapSections=function(){
for(i9=0;i9<this.sections.length;i9++){
var _48=this.sections[i9].itself.getElementsByTagName("div")[0];
if(_48.scrollHeight>44){
_48.className="alterH";
this.sections[i9].itself.style.height=28;
}
}
};
_34.prototype.foldSections=function(_49){
for(s2=((_49)?1:0);s2<this.sections.length;s2++){
this.foldSection(null,s2,true);
}
};
_34.prototype.foldSection=function(_4a,_4b,_4c,_4d){
var sId=((_4a==null)?_4b:this.pages[_4a].sectionInd);
if(sId==-1){
return;
}
if(this.sections[sId].isHidden){
return;
}
for(child in this.sections[sId].children){
this.sections[sId].children[child].style.display="none";
this.sections[sId].isHidden=true;
this.sections[sId].expander.src="../CDEJ/themes/classic/images/wizard/expand_16x16.gif";
if(_4c&&_4c==true){
this.sections[sId].expander.style.display="none";
}
}
if(this.ulRef.offsetHeight<=this.listRef.clientHeight){
this.listRef.className="noscroll";
this.unwrapFirsts();
this.wasWrappedTo=this.wrappedTo;
this.wrappedTo=-1;
}
this.restoreCurrentView();
};
_34.prototype.restoreCurrentView=function(){
for(var s2=0;s2<this.sections.length;s2++){
var _4e=this.sections[s2];
if(!_4e.isHidden){
for(child in _4e.children){
_4e.children[child].style.display="none";
_4e.children[child].style.display="";
}
}
}
};
_34.prototype.unfoldSections=function(_4f){
var _50=this.pages[_4f].sectionInd;
for(var i7=0;i7<=_50;i7++){
this.unfoldSection(null,null,i7);
}
this.wasWrappedTo=_50+1;
};
_34.prototype.unfoldSection=function(_51,_52,_53,_54){
var sId=((_51!=null)?this.pages[_51].sectionInd:((_52==null)?_53:_52));
if(sId==null||sId==-1){
return;
}
if(!this.sections[sId].isHidden){
return;
}
for(var _55 in this.sections[sId].children){
this.sections[sId].children[_55].style.display="";
this.sections[sId].isHidden=false;
this.sections[sId].expander.src="../CDEJ/themes/classic/images/wizard/contract_16x16.gif";
if(this.sections[sId].expander.style.display=="none"){
this.sections[sId].expander.style.display="";
}
}
this.adjustSectionStyle(sId);
if(this.ulRef.offsetHeight>this.listRef.clientHeight){
this.listRef.className="itemsList";
this.adjustScrollBar();
this.adjustFirstWrapping(sId);
}
};
_34.prototype.adjustFirstWrapping=function(_56){
var dir=((this.wasWrappedTo>_56)?"up":"down");
var _57=((dir=="down")?_56:0);
var _58=(dir=="down")?function(){
return _57>this.wrappedTo;
}:function(){
return _57<this.wasWrappedTo;
};
while(_58.call(this)&&this.sections[_57]){
if(this.sections[_57].children[0].scrollHeight>this.sections[_57].children[0].clientHeight){
this.sections[_57].children[0].className="first itemWrapped"+((this.sections[_57].children[0].className.indexOf("highlight")>-1)?" highlight":"");
}
((dir=="down")?_57--:_57++);
}
this.wrappedTo=_56;
this.wasWrappedTo=-1;
};
_34.prototype.unwrapFirsts=function(){
for(i8=0;i8<this.sections.length;i8++){
this.sections[i8].children[0].className="first"+((this.sections[i8].children[0].className.indexOf("highlight")>-1)?" highlight":"");
}
};
_34.prototype.adjustSectionStyle=function(_59){
if(!this.sections[_59]){
return;
}
this.sections[_59].itself.className=this.sections[_59].itself.className.replace("Up","Normal");
if(this.sections[_59+1]){
this.sections[_59+1].itself.className="sectionNormal";
}
};
_34.prototype.toggleSection=function(){
var _5a=this.mySectionNum;
if(this.imgNav.sections[_5a].isHidden){
this.imgNav.unfoldSection(null,_5a,null,true);
}else{
this.imgNav.foldSection(null,_5a,null,true);
}
};
_34.prototype.setContent=function(_5b){
this.contentPane.location.href=this.getPath(_5b);
this.contentPane.document.title=this.pages[_5b].title;
this.highlight(_5b);
};
_34.prototype.registerForm=function(_5c){
if(this.getButtonBar().outOfWizard){
if(this.contentPane.document.forms.length>0){
var _5d=this.contentPane.dom.byId("ctxParam");
var _5e=new curam.util.ScreenContext();
_5e.setContext(_5d["value"]);
_5e.clear("OVAL");
_5d.value=_5e.getValue();
return;
}
}
if(this.delegatesSubmit[_5c]=="assumed"){
if(this.contentPane.document.forms.length>0){
this.hideSubmitButtons(this.contentPane.document.forms[0],this.contentPane.document);
this.nextSubmits=true;
}
}else{
if(this.summary.summaryNum>-1&&_5c==this.summary.name){
if(this.summary.closeOnSubmit&&this.contentPane.document.forms[0]){
var _5f=function(e){
e.target.actualButton.click();
};
var _60=dojo.query("input[type=submit],input[type=image]",this.contentPane.document);
if(_60.length>1){
_60[1].actualButton=_60[0];
dojo.connect(_60[1],"onclick",_5f);
}
this.alterSubmit(this.contentPane.document.forms[0]);
}
}
}
};
_34.prototype.newContent=function(_61){
var len=this.length;
var _62=false;
var _63=false;
var _64=false;
if(this.claimantScheme==true&&this.summary.summaryNum>-1){
this.getButtonBar().showFinishButton();
}
for(var i=0;i<len;++i){
if(_61==this.pages[i].name){
this.setCurrent(i);
_62=true;
break;
}
}
if(this.disableBackList[_61]){
if(this.disableBackList[_61]!="dynamic"){
_63=this.disableBackList[_61];
}else{
_63=this.contentPane.DISABLE_BACK!=null&&this.contentPane.DISABLE_BACK==true;
}
}
if(this.disableForwardList[_61]){
if(this.disableForwardList[_61]!="dynamic"){
_64=this.disableForwardList[_61];
}else{
_64=this.contentPane.DISABLE_FORWARD!=null&&this.contentPane.DISABLE_FORWARD==true;
}
}
if(_62){
this.getButtonBar().enableButtons(_63,_64);
this.getButtonBar().reenableLinks();
this.progressBar.markCurrent(this.currentPage);
var _65=_7(window,"wizard");
_65.dojo.publish("curam/agendaplayer/content/loaded",[this.pages[i].title,this.progressBar.progressMsg]);
}else{
this.getButtonBar().outOfWizard=true;
this.getButtonBar().disableButtons();
this.highlight(-1);
this.progressBar.markCompleted(this.currentPage);
}
this.updateParams();
};
_34.prototype.next=function(){
if(this.modeInd==1){
this.getButtonBar().disableButtons();
}
if(this.nextSubmits){
this.btnRef.click();
return false;
}
if(!this.isLastPage()){
this.returnPage=this.currentPage;
this.progressBar.accum=(this.progressBar.accum>=this.currentPage+1)?this.progressBar.accum:this.currentPage+1;
this.progressBar.markCompleted(this.currentPage);
++this.currentPage;
this.makeThisCurrent();
}
if(this.getItemPos()>this.listRef.clientHeight){
this.listRef.className="itemsList";
this.adjustScrollBar(20);
}
this.setContent(this.currentPage);
};
_34.prototype.proceedNext=function(_66){
this.nextSubmits=false;
if(_66!=null&&_66!="undefined"){
this.updateParams(_66);
}
this.next();
};
_34.prototype.previous=function(){
if(this.modeInd==1){
this.getButtonBar().disableButtons();
}
if(this.isLastPage()){
this.currentPage=this.returnPage;
}else{
if(this.currentPage>0){
this.renderLink(this.currentPage);
--this.currentPage;
this.returnPage=this.currentPage;
}
}
this.makeThisCurrent();
this.setContent(this.currentPage);
};
_34.prototype.finish=function(){
var nav=this.navigator||this;
if(!nav.summary.clicked){
nav.renderLink(nav.currentPage);
nav.progressBar.markCompleted(nav.currentPage);
nav.progressBar.updateProgress();
_8.dojo.publish("curam/agendaplayer/content/loaded",[nav.summary.title,nav.progressBar.progressMsg]);
nav.returnPage=nav.currentPage;
nav.setContent(nav.length-1);
nav.summary.clicked=true;
}
};
_34.prototype.quit=function(){
if(jsScreenContext.hasContextBits("MODAL")){
var _67=true;
if(this.quitConfirm){
_67=window.confirm(this.quitQuestion);
}
if(_67==true){
var _68=curam.util.getFrameRoot(window,"wizard");
var _69=curam.util.adjustTargetContext(curam.dialog.getParentWindow(_68),this.exitPage);
_68.curam.util.Dialog.close(true,_69);
}
}else{
alert("Unsupported context for Agenda Player");
}
};
_34.prototype.quitFromSummary=function(_6a,_6b){
if(jsScreenContext.hasContextBits("MODAL")){
var _6c=_6a+_6b;
var _6d=curam.util.getFrameRoot(window,"wizard");
var _6e=curam.util.adjustTargetContext(curam.dialog.getParentWindow(_6d),_6c);
_6d.curam.util.Dialog.close(true,_6e);
}else{
alert("Unsupported context for Agenda Player");
}
};
_34.prototype.setCurrent=function(_6f){
this.currentPage=_6f;
this.highlight(this.currentPage);
};
_34.prototype.getPath=function(_70){
var _71=this.pages[_70].name+"Page.do?";
_71+=_7(window,"wizard").jsScreenContext.toRequestString();
for(var _72 in this.params){
if(this.params[_72]!=null&&this.params[_72]!=""){
_71=_71+"&"+_72+"="+encodeURIComponent(this.params[_72]);
}
}
return _71;
};
_34.prototype.addParam=function(_73,_74){
this.params[_73]=_74;
};
_34.prototype.updateParams=function(_75){
var _76=_75||getRequestParams(this.contentPane.location);
for(var _77 in this.params){
if(_76[_77]&&_76[_77]!=""){
this.addParam(_77,decodeURIComponent(_76[_77]));
for(i6=0;i6<this.length;i6++){
this.pages[i6].href=null;
}
}
}
};
_34.prototype.renderLink=function(_78){
if(this.modeInd>0){
var _79=this.pages[_78].liRef;
aLink=_5.create("a",{target:"targetframe",innerHTML:this.pages[_78].title});
aLink.pgNum=_78;
aLink.navigator=this;
dojo.connect(aLink,"onclick",this.makeThisCurrent);
dojo.attr(aLink,"href",this.getPath(_78));
this.pages[_78].linkRef=aLink;
_5.empty(_79);
if(this.pages[_78].imgRef!="no_img"){
_79.appendChild(this.pages[_78].imgRef);
}
_79.appendChild(aLink);
}
if(this.modeInd<2){
if(this.progressBar.accum<=_78+1){
this.progressBar.accum=_78+1;
}
this.progressBar.markCompleted(_78);
}
};
_34.prototype.makeThisCurrent=function(){
var nav=this.navigator||this;
if(nav.modeInd>0){
var _7a=(this.pgNum!=null)?this.pgNum:nav.currentPage;
nav.dropLink(_7a);
nav.highlight(_7a);
nav.unfoldSection(_7a);
nav.restoreLinks(_7a);
if(!nav.delegatesSubmit[nav.pages[_7a].name]){
nav.nextSubmits=false;
}
}
};
_34.prototype.highlight=function(_7b){
if(_7b>-1){
for(i4=0;i4<this.length;++i4){
if(this.pages[i4].liRef.className.indexOf("first")>-1){
this.pages[i4].liRef.className=((this.pages[i4].liRef.className.indexOf("itemWrapped")>-1)?"first itemWrapped":"first");
}else{
this.pages[i4].liRef.className="item";
}
if(i4==this.summary.summaryNum){
this.pages[i4].liRef.className="summaryDiv";
this.summary.clicked=false;
}
}
this.pages[_7b].liRef.className+=" highlight";
if(_7b==this.summary.summaryNum){
this.pages[_7b].liRef.className+="Summary";
this.summary.clicked=true;
}
this.pages[_7b].visited=true;
this.progressBar.markCurrent(_7b);
}else{
this.pages[this.currentPage].liRef.className="item";
}
};
_34.prototype.dropLink=function(_7c){
var nav=this.navigator||this;
var _7d=nav.pages[_7c].linkRef;
if(_7d!=null){
_5.empty(nav.pages[_7c].liRef);
if(nav.pages[_7c].imgRef!="no_img"){
nav.pages[_7c].liRef.appendChild(nav.pages[_7c].imgRef);
}
nav.pages[_7c].liRef.appendChild(document.createTextNode(nav.pages[_7c].title));
}
};
_34.prototype.restoreLinks=function(_7e,_7f){
var _80=(this.modeInd==2)?this.isTrue:((_7f==null)?this.isVisited:this.upTo);
for(k=0;k<this.getLengthWithoutSummary();k++){
if(k!=_7e){
if(_80.call(this,k)){
this.renderLink(k);
}
}
}
};
_34.prototype.hideSubmitButtons=function(_81,_82){
var _83=dojo.query("input[type=submit],input[type=image]",_81)[0];
if(_83&&_83.name.indexOf("__o3btn.")>-1){
_83.name+="NEXT";
this.btnRef=_83;
}
var _84=dojo.query(".actions-panel",_82)[0];
if(_84){
_5.empty(_84);
_84.style.display="none";
}
};
_34.prototype.alterSubmit=function(_85){
var _86=_85.getElementsByTagName("input");
for(i6=0;i6<_86.length;i6++){
if(_86[i6].type=="submit"||(_86[i6].type=="image"&&(_86[i6].name.indexOf("__o3btn.")>-1))){
_86[i6].name+="SUM";
}
}
};
_34.prototype.isFirstPage=function(){
return (this.currentPage===0);
};
_34.prototype.isLastPage=function(){
return (this.currentPage==this.length-1);
};
_34.prototype.isVisited=function(_87){
return this.pages[_87].visited;
};
_34.prototype.getLengthWithoutSummary=function(){
return this.length-1*(this.summary.summaryNum>-1);
};
_34.prototype.getButtonBar=function(){
return this.buttonPane.buttonBar;
};
_34.prototype.isTrue=function(_88){
return true;
};
_34.prototype.upTo=function(arg){
return arg<this.currentPage;
};
_34.prototype.initProgressBar=function(){
this.progressBar=new _89(this,this.currentPage,this.modeInd);
};
_34.prototype.adjustScrollBar=function(_8a){
this.listRef.scrollTop=_8a?(this.listRef.scrollTop+_8a):this.ulRef.offsetHeight;
};
_34.prototype.getItemPos=function(){
return this.pages[this.currentPage].liRef.offsetTop+this.pages[this.currentPage].liRef.offsetHeight;
};
_34.prototype.optShowNavigator=function(){
var _8b=_7(window,"wizard").document.getElementById("navset");
if(this.visible){
_8b.cols=_8b.className=="rtl"?"*,210":"210,*";
}else{
_8b.cols="0,*";
}
};
_34.prototype.handleResize=function(){
if(this.navRef.summary.summaryNum>-1){
if(this.oldHeight==null||this.oldHeight!=this.clientHeight){
this.oldHeight=this.clientHeight;
this.navRef.listRef.style.height=this.oldHeight-_2.get(this.navRef.summaryRef,"height");
}
}
};
function _89(_8c,_8d,_8e){
this.navigator=_8c;
this.accum=_8d;
this.isFull=(_8e==2);
this.panel=_7(window,"wizard").headerframe.document;
this.countSteps=_8c.getLengthWithoutSummary();
this.wrapper=this.panel.getElementById("progressBar");
this.progressText=this.panel.getElementById("progressText");
this.progressTemplate=this.panel.getElementById("progressText").innerHTML;
this.progressReport="00";
var _8f=this.wrapper.innerHTML;
if(_8f.indexOf("%counter")==-1){
_8f="<span id='counter' style='display:none;'>0</span>"+_8f;
}
if(_8f.indexOf("%total")==-1){
_8f+="<span id='total' style='display:none;'>n</span>";
}
_8f=_8f.replace("%counter","<span id='counter'>0</span>");
_8f=_8f.replace("%total","<span id='total'>n</span>");
this.wrapper.innerHTML=_8f;
this.passedPg=this.panel.getElementById("counter");
this.totalPg=this.panel.getElementById("total");
if(!this.isFull){
this.totalPg.innerHTML=this.totalPg.text=this.countSteps;
if(this.progressTemplate){
this.progressTemplate=this.progressTemplate.replace("%total",this.countSteps);
}
var _90=this.progressText;
_90.style.margin="2px 0 0 0";
this.steps=new Array();
this.fillStepsArray();
this.completeTo(_8d);
}
};
_89.prototype.fillStepsArray=function(){
for(var s1=0;s1<this.countSteps;s1++){
var _91=this.panel.createElement("div");
_91.className="empty-step";
this.steps[s1]=_91;
this.wrapper.appendChild(this.steps[s1]);
}
if(this.navigator.modeInd<2){
this.wrapper.style.display="";
}
};
_89.prototype.markCurrent=function(idx){
if(!this.isFull&&this.steps[idx]){
this.steps[idx].className="current-step";
}
};
_89.prototype.completeTo=function(idx){
for(var s2=0;s2<idx;s2++){
this.markCompleted(s2);
}
this.markCurrent(idx);
};
_89.prototype.updateProgress=function(){
this.progressMsg=this.progressTemplate.replace("%counter",this.passedPg.innerHTML);
};
_89.prototype.markCompleted=function(idx){
if(!this.isFull){
this.steps[idx].className="completed-step";
this.passedPg.innerHTML=this.passedPg.text=this.accum;
this.updateProgress();
}
};
this.ButtonBar=_f;
this.ProgressBar=_89;
this.WizardNavigator=_34;
this.SummaryData=_31;
this.WizardSection=_30;
this.WizardPage=_2d;
this.WizardSummary=_9;
});
