//>>built
define("curam/omega3-util",["dojo/dom-geometry","dojo/dom","dojo/dom-class","dojo/dom-attr","dojo/dom-style","dojo/dom-construct","dojo/sniff","dijit/registry","curam/util/EditableList","curam/debug","curam/validation","curam/util","curam/html","curam/GlobalVars","cm/_base/_dom","cm/_base/_form","curam/util/RuntimeContext"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c){
var _d={PopupMapping:function(_e,_f){
this.name=_e;
this.targetWidgetID=_f;
},openPopupFromCTCode:function(_10,_11,_12,_13,_14,_15){
var _16;
var _17=_12.parentNode.parentNode.parentNode.childNodes[0];
var _18=dijit.byNode(_17);
if(_18){
_16=_18.getValue();
}else{
var _19=_12.parentNode.parentNode.parentNode.childNodes[1];
try{
_16=_19.options[_19.selectedIndex].value;
}
catch(e){
}
if(_16===undefined){
try{
_16=_17.options[_17.selectedIndex].value;
}
catch(e){
}
}
if(_16===undefined){
try{
var _1a=_12.parentNode.parentNode.parentNode.childNodes[0];
_16=_1a.firstChild.value;
}
catch(e){
}
}
if(_16===undefined){
curam.debug.log("Error: cannot find element containing codetable value in dropdown");
}
}
if(_16!=""){
if(curam.popupCTCodeMappings[_16]){
openPopupFromDomain(_10,_11,curam.popupCTCodeMappings[_16],_13,false,_14,_15);
}
}
},openPopupFromCTCodeNoDomain:function(_1b,_1c,_1d,_1e,_1f,_20){
var _21=_1d.parentNode.parentNode.parentNode.childNodes[2];
var _22=dijit.byNode(_21);
var _23;
var _24;
var _25;
var _26;
var _27;
var _28;
var _29;
var _2a;
if(_22){
var _2b=_22.getValue();
}else{
var _21=_1d.parentNode.parentNode.parentNode.childNodes[1];
var _2b=_21.options[_21.selectedIndex].value;
}
if(_2b!=""){
if(curam.popupCTCodeMappings[_2b]){
_23=getPopupProperties(curam.popupCTCodeMappings[_2b]);
_24=_23.pageID;
_25=_23.createPageID;
_26=_23.height;
_27=_23.width;
_28=_23.scrollBars;
_29=_23.insertMode;
_2a=_23.codeTableCode;
var _2c=_23.uimType;
if(_2c=="DYNAMIC"){
openPopup(_1b,_1c,null,_24,_25,_27,_26,_28,_29,null,null,_1e,false,_1f,_20);
}
}
}
},openPopupFromDomain:function(_2d,_2e,_2f,_30,_31,_32,_33){
var _34=getPopupProperties(_2f);
var _35=_34.pageID;
var _36=_34.createPageID;
var _37=_34.height;
var _38=_34.width;
var _39=_34.scrollBars;
var _3a=_34.insertMode;
var _3b=_34.codeTableCode;
var _3c=dojo.byId(_2d).previousElementSibling;
_3c&&curam.util.addPlaceholderFocusClassToEventOrAnchorTag(_3c,window);
openPopup(_2d,_2e,_2f,_35,_36,_38,_37,_39,_3a,_3b,_30,_31,_32,_33);
},openPopupNoDomain:function(_3d,_3e,_3f,_40,_41,_42,_43,_44,_45,_46,_47,_48){
openPopup(_3d,_3e,null,_3f,_40,_41,_42,_43,_44,null,null,_45,_46,_47,_48);
var _49=dojo.byId(_3d).previousElementSibling;
_3.add(_49,"placeholder-for-focus");
},openPopup:function(_4a,_4b,_4c,_4d,_4e,_4f,_50,_51,_52,_53,_54,_55,_56,_57){
setMappingsLoaded(_4b);
if(curam.popupWindow&&!curam.popupWindow.closed){
curam.popupWindow.close();
}
curam.currentPopupInstanceName=_4b;
curam.currentPopupProps=setPopupProperties(_4d,_4c,_53,_4f,_50,_51,_4e,_52,null);
var ctx=jsScreenContext;
ctx.addContextBits("POPUP");
ctx.clear("TAB|TREE|AGENDA");
var url="";
var _58;
if(_55==true){
url=_4e;
if(_57&&_57.length>0){
_58="&"+curam.util.secureURLsHashTokenParam+"="+_57;
}
}else{
url=_4d;
if(_56&&_56.length>0){
_58="&"+curam.util.secureURLsHashTokenParam+"="+_56;
}
}
if(_54&&_54.length>0){
url=url+"?"+_54;
_58?url+=_58+"&":url+="&";
}else{
url+="?";
}
url+=ctx.toRequestString();
if(window.curam.util.showModalDialog){
curam.util.showModalDialog(url,null,_4f,_50,0,0,false,null,null);
}
},addPopupMapping:function(_59,_5a,_5b){
var _5c=curam.popupMappingRepository;
if(curam.popupMappingLoaded[_59]==true){
return;
}
if(_5c[_59]==null){
_5c[_59]=[];
_5c[_59][_5a]=[];
_5c[_59][_5a][0]=_5b;
}else{
if(_5c[_59][_5a]==null){
_5c[_59][_5a]=[];
_5c[_59][_5a][0]=_5b;
}else{
var _5d=_5c[_59][_5a].length;
_5c[_59][_5a][_5d]=_5b;
}
}
},setMappingsLoaded:function(_5e){
curam.popupMappingLoaded[_5e]=true;
},executeMapping:function(_5f,_60){
dojo.body().setAttribute("tabIndex","-1");
var pmr=curam.popupMappingRepository;
var _61=curam.currentPopupInstanceName;
if(!pmr||!pmr[_61]||pmr[_61][_5f]==null){
return;
}
var _62=null;
for(var i=0;i<pmr[_61][_5f].length;i++){
var _63=null;
_63=_2.byId(pmr[_61][_5f][i]);
_62=_63;
if(_63.tagName=="SPAN"){
_63.innerHTML=curam.html.splitWithTag(_60,null,null,escapeXML);
_63.setAttribute("title",_60);
_63._reposition=_63._reposition||dojo.query("div",_63).length>0;
if(_63._reposition){
var _64=cm.nextSibling(_63,"span");
if(_64){
var _65=_1.getMarginBoxSimple(_63).h;
var _66=_1.getMarginBoxSimple(_64).h;
_5.set(_64,"position","relative");
var _67=_65-_66-((dojo.isIE&&dojo.isIE<9)?2:0);
_5.set(_64,"bottom","-"+(_67)+"px");
}
}
}else{
if(_63.tagName=="TEXTAREA"){
if(curam.currentPopupProps.insertMode=="insert"){
insertAtCursor(_63,escapeXML(_60));
}else{
if(curam.currentPopupProps.insertMode=="append"){
_63.value+=_60;
}else{
_63.value=_60;
}
}
}else{
if(_8.byId(pmr[_61][_5f][i])){
_8.byId(pmr[_61][_5f][i]).set("value",_60);
_63.value=_60;
}else{
_63.value=_60;
var _68=_63.id;
if(_68.indexOf("_value")>0){
var _69=_68.replace("_value","_clear");
var _6a=_2.byId(_69);
_6a.classList.remove("bx--search-close--hidden");
}
}
}
}
}
setTimeout(function(){
if(_62){
_62.setAttribute("value",_60);
_62.setAttribute("aria-label",_60);
_62.focus();
}
},300);
dojo.body().setAttribute("tabIndex","0");
},insertAtCursor:function(_6b,_6c){
if(document.selection){
_6b.focus();
sel=document.selection.createRange();
sel.text=_6c;
}else{
if(_6b.selectionStart||_6b.selectionStart=="0"){
var _6d=_6b.selectionStart;
var _6e=_6b.selectionEnd;
_6b.value=_6b.value.substring(0,_6d)+_6c+_6b.value.substring(_6e,_6b.value.length);
}else{
_6b.value+=_6c;
}
}
},escapeXML:function(_6f){
return _6f.replace(/&/g,"&#38;").replace(/</g,"&#60;").replace(/>/g,"&#62;").replace(/"/g,"&#34;").replace(/'/g,"&#39;");
},executeOpenerMapping:function(_70,_71){
var _72=undefined;
if(curam.util.isModalWindow()){
_72=curam.dialog.getParentWindow(window);
}else{
if(window.dialogArguments){
_72=window.dialogArguments[0];
}
}
if((_72)&&(!_72.closed)){
_72.executeMapping(_70,_71);
}else{
_a.log("curam.omega3-util.executeOpenerMapping:, "+_a.getProperty("curam.omega3-util.parent"));
}
},storePopupInputFromWidget:function(_73,_74){
var _75=null;
_75=_2.byId(_74).value;
if(_75){
curam.popupInputs[_73]=_75;
}else{
curam.popupInputs[_73]="";
}
},getPopupInput:function(_76){
if(curam.popupInputs[_76]!=null){
return curam.popupInputs[_76];
}else{
return "";
}
},PopupProperties:function(_77,_78,_79,_7a,_7b,_7c,_7d){
this.width=_78;
this.height=_79;
this.scrollBars=_7a;
this.pageID=_77;
this.createPageID=_7b;
if(_7c==null){
this.insertMode="overwrite";
}else{
this.insertMode=_7c;
}
if(_7d!=null){
this.uimType=_7d;
}
},setPopupProperties:function(_7e,_7f,_80,_81,_82,_83,_84,_85,_86){
if(_80){
curam.popupCTCodeMappings[_80]=_7f;
}
curam.popupPropertiesRepository[_7f]=new PopupProperties(_7e,_81,_82,_83,_84,_85,_86);
},getPopupAttributes:function(_87,_88,_89){
var _8a="width="+_87+","+"height="+_88+","+"scrollbars="+(_89?"yes":"no")+",";
return _8a;
},getPopupAttributesIEModal:function(_8b){
var _8c="dialogWidth:"+curam.popupPropertiesRepository[_8b].width+"px;"+"dialogHeight:"+curam.popupPropertiesRepository[_8b].height+"px;";
return _8c;
},trimFileExtension:function(_8d){
var _8e=_8d.lastIndexOf("/")+1;
if(_8e==-1){
_8e=_8d.lastIndexOf("\\")+1;
}
if(_8e==-1){
_8e=0;
}
return _8d.substring(_8e,_8d.lastIndexOf("."));
},getPopupProperties:function(_8f){
return curam.popupPropertiesRepository[_8f];
},validateDate:function(_90){
return curam.validation.validateDate(_90).valid;
},addStartDate:function(_91){
var _92=_2.byId("startDate").value;
var _93=curam.validation.validateDate(_92);
if(_93.valid){
var _94=_2.byId("gotoDate");
_94.href=curam.util.replaceUrlParam(_94.href,"startDate",_92);
return true;
}else{
require(["curam/validation/calendar"],function(_95){
alert(_95.invalidGotoDateEntered.replace("%s",_92).replace("%s",jsDFs));
});
dojo.stopEvent(_91);
return false;
}
},checkEnter:function(_96){
if(_96.keyCode==13){
if(addStartDate(_96)){
var _97=_2.byId("gotoDate");
window.location=_97.href;
return true;
}
return false;
}
return true;
},createWindowName:function(_98){
var _99=new String("");
for(var i=0;i<_98.length;i++){
var ch=_98.charAt(i);
if(ch=="$"||ch=="."){
_99+="_";
}else{
_99+=ch;
}
}
return _99;
},clearPopup:function(_9a,_9b){
var _9c=_9a.id.substring(0,_9a.id.indexOf("_clear"));
var _9d=_9c+"_value";
var _9e=_9c+"_desc";
var _9f=_9c+"_deschf";
var _a0=_2.byId(_9a);
_a0.classList.add("bx--search-close--hidden");
var _a1=_2.byId(_9d);
if(_a1){
if(_a1.tagName=="INPUT"){
_a1.value="";
}else{
if(_a1.tagName=="TEXTAREA"){
_a1.value="";
}
}
if(_a1.tagName=="SPAN"){
_a1.innerHTML=curam.POPUP_EMPTY_SPAN_VALUE;
}
}
var _a2=_2.byId(_9e);
if(_a2){
if(_a2.tagName=="INPUT"){
_a2.value="";
}else{
if(_a2.tagName=="TEXTAREA"){
_a2.value="";
}else{
if(_a2.tagName=="SPAN"){
_a2.innerHTML=curam.POPUP_EMPTY_SPAN_VALUE;
_4.set(_a2,"title",_4.get(_a2,"data-field-label"));
}
}
}
}
var _a3=_2.byId(_9f);
if(_a3){
if(_a3.tagName=="INPUT"){
_a3.value="";
}else{
_a3.innerHTML="&nbsp";
}
}
if(_9b){
_9b=dojo.fixEvent(_9b);
dojo.stopEvent(_9b);
}
return false;
},showClearIcon:function(_a4){
var _a5=_2.byId(_a4+"_clear");
var _a6=_2.byId(_a4+"_value");
if(_a6.value==""){
_a5.classList.add("bx--search-close--hidden");
}else{
_a5.classList.remove("bx--search-close--hidden");
}
},swapImage:function(_a7,_a8){
_2.byId(_a7).src=_a8;
},appendTabColumn:function(_a9,_aa){
var _ab;
var _ac=[];
dojo.query("input[name='"+_a9+"']").filter(function(_ad){
return _ad.checked;
}).forEach(function(_ae){
_ac.push(_ae.value);
});
_ab=_ac.join("\t");
_aa.href=_aa.href+(_aa.href.indexOf("?")==-1?"?":"&");
if(_ab!=""){
_aa.href=_aa.href+_a9+"="+encodeURIComponent(_ab);
}else{
_aa.href=_aa.href+_a9+"=";
}
},ToggleAll:function(e,_af){
dojo.query("input[name='"+_af+"']").forEach(function(_b0){
if(_b0.checked===true){
_b0.checked=false;
}else{
_b0.checked=true;
}
});
},ToggleSelectAll:function(e,_b1){
if(e.checked){
CheckAll(_b1);
}else{
ClearAll(_b1);
}
},CheckAll:function(_b2){
dojo.query("input[name^='"+_b2+"'][onclick]").forEach(function(_b3){
_b3.checked=true;
_9._doToggling(_b3);
});
},ClearAll:function(_b4){
dojo.query("input[name^='"+_b4+"'][onclick]").forEach(function(_b5){
_b5.checked=false;
_9._doToggling(_b5);
});
},Check:function(e){
e.checked=true;
},Clear:function(e){
e.checked=false;
},ChooseSelectAll:function(e,_b6,_b7){
var _b8=_2.byId(_b6);
if(_b8){
if(dojo.query("input[name='"+_b7+"']").every("return item.checked")){
Check(_b8);
}else{
Clear(_b8);
}
}
_9._doToggling(e);
},ChooseCheckbox:function(e,_b9){
var _ba=_2.byId(_b9);
if(_ba){
if(e.checked){
Clear(_b9);
}else{
Check(_b9);
}
}
_9._doToggling(e);
},selectAllIfNeeded:function(_bb,_bc){
if(dojo.query("input[name='"+_bc+"']").some("return !item.checked")){
return;
}
var _bd=_2.byId(_bb);
if(_bd){
Check(_bd);
}
},dc:function(_be,_bf,_c0){
if(cm.wasFormSubmitted(_be)){
var evt=dojo.fixEvent(_c0);
dojo.stopEvent(evt);
return false;
}
cm.setFormSubmitted(_be,1);
var _c1=dojo.query(".curam-default-action")[0];
if(_c1!==null&&_c1!==undefined){
sessionStorage.setItem("curamDefaultActionId",_c1.id);
}
return true;
},setFocus:function(){
curam.util.setFocus();
},setParentFocus:function(_c2){
_a.log("curam.omega3-util.setParentFocus: "+_a.getProperty("curam.omega3-util.called"));
var _c3=curam.dialog.getParentWindow(window);
if(!_c3.closed){
var _c4=dojo.query("button..bx--search-close.placeholder-for-focus",_c3.document);
if(_c4.length==1){
_c4[0].focus();
_3.remove(_c4[0],"placeholder-for-focus");
}else{
_c3.focus();
var _c5=dojo.query(".placeholder-for-focus",_c3.document);
if(_c5.length==1){
_3.remove(_c5[0],"placeholder-for-focus");
}
}
}else{
alert("The parent window has been closed");
}
if(_c2||window.event){
dojo.stopEvent(_c2||window.event);
}
curam.dialog.closeModalDialog();
},getParentWin:function(){
return curam.dialog.getParentWindow(window);
},addQuestionsFromPopup:function(evt){
evt=dojo.fixEvent(evt);
dojo.stopEvent(evt);
if(window._questionsAdded){
return;
}
window._questionsAdded=true;
var _c6=getParentWin();
var _c7=dojo.query("INPUT");
var _c8=[];
dojo.query("INPUT[type='checkbox']").forEach(function(_c9){
if(_c9.checked&&_c9.id.indexOf("__o3mswa")<0){
_c8.push(_c9.value);
}
});
var _ca=dojo.toJson(_c8);
_c6.newQuestions=_ca;
_c6.curam.matrix.Constants.container.matrix.addQuestionsFromPopup();
curam.dialog.closeModalDialog();
return false;
},getRequestParams:function(_cb){
var _cc=[];
var uri=new dojo._Url(_cb);
if(uri.query!=null){
var _cd=uri.query.split("&");
for(var i=0;i<_cd.length;i++){
var arr=_cd[i].split("=");
_cc[arr[0]]=arr[1];
}
}
return _cc;
},openModalDialog:function(_ce,_cf,_d0,top){
curam.util.openModalDialog(_ce,_cf,_d0,top);
},initCluster:function(_d1){
var _d2=_d1.parentNode;
var _d3=dojo.query("div.toggle-group",_d2);
if(_d3.length>=1){
return _d3[0];
}
var _d4=cm.nextSibling(_d1,"p")||cm.nextSibling(_d1,"table");
if(!_d4){
return;
}
_d3=_6.create("div",{"class":"toggle-group"},_d4,"before");
var arr=[];
var _d5=dojo.query("p.description",_d1)[0];
if(_d5){
arr.push(_d5);
var _d6=_5.get(_d1,"marginBottom");
_5.set(_d1,"marginBottom",0);
_5.set(_d5,"marginBottom",_d6+"px");
}
var _d7=_d2;
while(_d7&&!(_3.contains(_d7,"cluster")||_3.contains(_d7,"list"))){
_d7=_d7.parentNode;
}
_d3.isClosed=_3.contains(_d7,"uncollapse")?true:false;
if(_d3.isClosed){
_5.set(_d3,"display","none");
}
for(var _d8=0;_d8<_d2.childNodes.length;_d8++){
if(_d2.childNodes[_d8]==_d1||_d2.childNodes[_d8]==_d3){
continue;
}
arr.push(_d2.childNodes[_d8]);
}
for(var _d8=0;_d8<arr.length;_d8++){
_d3.appendChild(arr[_d8]);
}
return _d3;
},initClusterHeight:function(_d9,_da,_db){
if(_d9.correctHeight){
return;
}
var _dc=dojo._getBorderBox(_da).h;
var _dd=0,_de;
for(var _df=0;_df<_d9.childNodes.length;_df++){
_de=_d9.childNodes[_df];
if(_de==_da){
continue;
}
_dd+=dojo._getBorderBox(_de).h;
}
if(_dd==0){
return;
}
if(_db){
_5.set(_da.parentNode,"height","");
}
_d9.correctHeight=_dd;
},getCursorPosition:function(e){
e=e||dojo.global().event;
var _e0={x:0,y:0};
if(e.pageX||e.pageY){
_e0.x=e.pageX;
_e0.y=e.pageY;
}else{
var de=dojo.doc().documentElement;
var db=dojo.body();
_e0.x=e.clientX+((de||db)["scrollLeft"])-((de||db)["clientLeft"]);
_e0.y=e.clientY+((de||db)["scrollTop"])-((de||db)["clientTop"]);
}
return _e0;
},overElement:function(_e1,e){
_e1=_2.byId(_e1);
var _e2=getCursorPosition(e);
var bb=dojo._getBorderBox(_e1);
var _e3=dojo._abs(_e1,true);
var top=_e3.y;
var _e4=top+bb.h;
var _e5=_e3.x;
var _e6=_e5+bb.w;
return (_e2.x>=_e5&&_e2.x<=_e6&&_e2.y>=top&&_e2.y<=_e4);
},_getToggleImages:function(){
var _e7="../themes/curam/images/chevron--down20-enabled.svg";
var _e8="../themes/curam/images/chevron--down20-enabled.svg";
var _e9;
var _ea;
var _eb=_c.isRtlMode();
if(_eb){
_e9="../themes/curam/images/chevron--right20-enabled.svg";
_ea="../themes/curam/images/chevron--right20-enabled.svg";
}else{
_e9="../themes/curam/images/chevron--left20-enabled.svg";
_ea="../themes/curam/images/chevron--left20-enabled.svg";
}
return [_e7,_e9,_e8,_ea];
},setToggleClusterIcon:function(img){
var _ec=this._getToggleImages();
var _ed=_ec[0];
if(img.className=="hoverIcon"){
_ed=_ec[2];
}
var _ee=img;
while(_ee&&!(_3.contains(_ee,"cluster")||_3.contains(_ee,"list"))){
_ee=_ee.parentNode;
}
if(_3.contains(_ee,"is-collapsed")){
if(img.className=="hoverIcon"){
_ed=_ec[3];
}else{
_ed=_ec[1];
}
}
img.onload=null;
img.src=_ed;
img.alt="";
},toggleCluster:function(_ef,_f0){
var _f1=_ef;
var img;
var _f2;
var _f3=dojo.query("img",_ef);
if(_f3&&_f3.length==2){
img=_f3[0];
_f2=_f3[1];
}
var _f4=this._getToggleImages();
var _f5=_f4[0];
var _f6=_f4[1];
var _f7=_f4[2];
var _f8=_f4[3];
while(_ef&&!(_3.contains(_ef,"cluster")||_3.contains(_ef,"list"))){
_ef=_ef.parentNode;
}
var _f9=false;
var _fa=dojo.query(" > :not(.header-wrapper) ",_ef.childNodes[0]);
if(!_3.contains(_fa[0],"toggleDiv")){
var _fb=_6.create("div",{className:"toggleDiv"},_fa[0].parentNode);
var _fc=_6.create("div",{className:"toggleDiv2"},_fa[0].parentNode);
_fa.forEach(function(_fd){
var _fe;
var _ff;
dojo.query(".cke",_fd).forEach(function(_100){
_fe=dojo.query("textarea",_100.parentElement)[0].id;
for(var i in CKEDITOR.instances){
if(CKEDITOR.instances[i].name.includes(_fe)){
_ff=CKEDITOR.instances[i].config.customConfig;
CKEDITOR.instances[i].destroy();
}
}
});
if(_fd.tagName!="DIV"){
_fb.appendChild(_fd);
}else{
_fc.appendChild(_fd);
}
if(_fe){
CKEDITOR.replace(_fe,{customConfig:_ff});
}
});
}else{
var _fb=_fa[0];
var _fc=_fa[1];
}
var desc=dojo.query(" > .header-wrapper p ",_ef.childNodes[0])[0];
if(typeof desc!="undefined"){
_f9=true;
}
if(_3.contains(_ef,"init-collapsed")){
_3.remove(_ef,"init-collapsed");
_5.set(_fb,"display","none");
}
if(!_fb||_fb.inAnimation){
return;
}
require(["dojo/fx"],function(fx){
var _101={node:_fb,duration:600,onBegin:function(){
_fb.inAnimation=true;
_3.remove(_ef,"is-collapsed");
_3.add(_ef,"is-uncollapsed");
_4.set(_f1,"aria-expanded","true");
dojo.stopEvent(_f0);
},onEnd:function(){
_fb.inAnimation=false;
}};
var _102={node:_fb,duration:600,onBegin:function(){
_fb.inAnimation=true;
_3.remove(_ef,"is-uncollapsed");
_3.add(_ef,"is-collapsed");
_4.set(_f1,"aria-expanded","false");
dojo.stopEvent(_f0);
},onEnd:function(){
_fb.inAnimation=false;
}};
if(_fc.hasChildNodes()){
var _103={node:_fc,duration:600};
var _104={node:_fc,duration:600};
}
if(_f9){
var _105={node:desc,duration:100};
var _106={node:desc,duration:100,delay:500};
}
if(_3.contains(_ef,"is-collapsed")){
if(typeof _105!="undefined"){
fx.wipeIn(_105).play();
}
fx.wipeIn(_101).play();
if(typeof _103!="undefined"){
fx.wipeIn(_103).play();
}
if(img){
img.src=_f5;
}
if(_f2){
_f2.src=_f7;
}
}else{
if(_3.contains(_ef,"is-uncollapsed")){
if(typeof _104!="undefined"){
fx.wipeOut(_104).play();
}
fx.wipeOut(_102).play();
if(typeof _106!="undefined"){
fx.wipeOut(_106).play();
}
if(img){
img.src=_f6;
}
if(_f2){
_f2.src=_f8;
}
}else{
_a.log("The cluster does not have a class name indicating"+"its collapsed/uncollapsed state");
}
}
});
},disableClusterToggle:function(node){
dojo.addOnLoad(function(){
node=_2.byId(node);
var body=dojo.body();
while(node&&node!=body){
if(_3.contains(node,"is-collapsed")||_3.contains(node,"is-uncollapsed")){
_3.remove(node,"is-collapsed");
_3.remove(node,"is-uncollapsed");
_4.remove(dojo.query("BUTTON.grouptoggleArrow",node)[0],"onclick");
}
node=node.parentNode;
}
});
},addClassToPositionQuestionaireStatic:function(){
var body=document.getElementsByTagName("body")[0];
body.className=body.className+" static-print-position";
},openUserPrefsEditor:function(_107){
_107=dojo.fixEvent(_107);
var _108=_107.target;
while(_108&&_108.tagName!="A"){
_108=_108.parentNode;
}
var _109={location:{href:_108.href}};
var rtc=new curam.util.RuntimeContext(_109);
var href=curam.util.setRpu("user-locale-selector.jspx",rtc);
openModalDialog({href:href},"width=500,height=300",200,150,false);
return false;
},calendarOpenModalDialog:function(_10a,_10b){
dojo.stopEvent(_10a);
curam.util.openModalDialog(_10b,"");
},clickMarker:function(){
return true;
}};
for(prop in _d){
dojo.global[prop]=_d[prop];
}
return _d;
});
