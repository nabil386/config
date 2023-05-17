require(["dojo/_base/connect","curam/cefwidgets/evidence/DashboardTitlePane"]);
dojo.require("dojo.NodeList-traverse");
dojo.require("dijit.form.Select");
dojo.require("curam.util.UimDialog");
dojo.registerModulePath("cef","../..");
var pageID;
var participantID;
var caseID=0;
var issueTooltip="";
var verificationTooltip="";
var inEditTooltip="";
var createTooltip="";
var currentFilter="all";
var currentFilterClass="";
var mandatoryTooltip="";
var filterListLabel="";
var iconVerifications="../Images/items-to-verify__filled-triangle--20-enabled.svg";
var iconInEditEvidence="../Images/evidence-in-edit__filled-square--20-enabled.svg";
var iconIssues="../Images/issues__filled--20-enabled.svg";
var iconCreate="../Images/add--20-enabled.svg";
var highContrastIconVerifications="../Images/highcontrast/dashboard_icon_verifications.png";
var highContrastIconInEditEvidence="../Images/highcontrast/dashboard_icon_inedit.png";
var highContrastIconIssues="../Images/highcontrast/dashboard_icon_issue.png";
var highContrastIconCreate="../Images/highcontrast/dashboard_icon_create.png";
var rowCounter;
var colCounter;
var allColumnPartitions;
var recordedColumnPartitions;
dojo.global.createEvidenceDashboard=function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13,_14,_15){
allColumnPartitions=_11;
recordedColumnPartitions=_12;
if(allColumnPartitions===undefined||allColumnPartitions.length==0){
createMissingEvidenceTypesMessage(_10);
}else{
createLegend(_1,_3,_4,_5,_e,_f,_15);
this.caseID=_2;
this.participantID=_14;
this.pageID=_13;
this.issueTooltip=_3;
this.verificationTooltip=_4;
this.inEditTooltip=_5;
this.createTooltip=_c;
this.mandatoryTooltip=_d;
this.filterListLabel=_e;
var _16=dojo.byId("dashboardData");
var i=0;
dojo.forEach(_16.children,function(_17){
var _18=dojo.attr(_17,"value");
var _19=createCategorySection(_1,i,_18,_6,_7,_8,_9,_a,_b);
filterDashboard("all",_19,_19,i);
i++;
});
dojo.query(".dijitTitlePaneTitleFocus").forEach(function(_1a){
dojo.attr(_1a,"role","button");
dojo.attr(_1a,"class","evidence_dijitTitlePaneTitleFocus");
});
}
};
function createMissingEvidenceTypesMessage(_1b){
var _1c=dojo.byId(curam.util.ERROR_MESSAGES_CONTAINER);
var _1d=dojo.byId(curam.util.ERROR_MESSAGES_LIST);
if(!_1d){
_1d=dojo.place("<ul id='error-messages' class='messages' />",_1c);
}
dojo.place("<li class='level-2'><div><span>Informational Message: </span>"+_1b+"</div></li>",_1d);
};
function createLegend(_1e,_1f,_20,_21,_22,_23,_24){
var _25=dojo.create("table",{role:"presentation"},_1e);
dojo.addClass(_25,"dashboardHeader");
var tr=dojo.create("tr",null,_25);
var _26=dojo.create("td",{id:"filterColumn"},tr);
var _27=dojo.create("td",{id:"iconsColumn"},tr);
var _28=dojo.byId("dashboardFilters");
if(_28){
var _29=dojo.create("div",null,_26);
dojo.addClass(_29,"dashboardFilter codetable");
var _2a=dojo.create("span",{innerHTML:_22,title:_22},_29);
dojo.setStyle(_2a,{"padding":"0 20px 0 10px","vertical-align":"top"});
var _2b=dojo.create("select",{"data-dojo-type":"dijit/form/Select",maxHeight:"200",maxWidth:"250",id:"dashboardFilterSelection",title:_22,tabindex:"0"},_29);
dojo.addClass(_2b,"dijitComboBox");
dojo.setStyle(_2b,{"max-height":"200","max-width":"250"});
dojo.attr(_2b,"onchange","refreshEvidenceDashboard(this.value,'"+_24+"')");
var _2c;
_2c=dojo.create("option",{innerHTML:_23,value:"-"},_2b);
var _2d=dojo.query("li",_28);
dojo.forEach(_2d,function(_2e){
var _2f=dojo.attr(_2e,"name");
var _30=dojo.attr(_2e,"id");
var _31=dojo.attr(_2e,"selected");
if(_31=="true"){
_2c=dojo.create("option",{innerHTML:_2f,value:_30,"selected":"selected",label:_2f},_2b);
}else{
_2c=dojo.create("option",{innerHTML:_2f,value:_30},_2b);
}
});
}
var _32=dojo.create("div");
dojo.place(_32,iconsColumn);
dojo.addClass(_32,"dashboardLegend");
var _33=dojo.create("span",{innerHTML:_1f},_32);
dojo.addClass(_33,"dashboardLegendSpan");
dojo.attr(_33,"aria-hidden","true");
if(curam.util.highContrastModeType()){
var _34=dojo.create("img",{src:highContrastIconIssues,alt:"",style:{verticalAlign:"middle"}},_33);
dojo.place(_34,_33,"before");
}else{
var _34=dojo.create("img",{src:iconIssues,alt:"",style:{verticalAlign:"middle"}},_33);
if(curam.util.isRtlMode()){
dojo.place(_34,_33,"after");
}else{
dojo.place(_34,_33,"before");
}
}
var _35=dojo.create("span",{innerHTML:_20},_32);
dojo.addClass(_35,"dashboardLegendSpan");
dojo.attr(_35,"aria-hidden","true");
if(curam.util.highContrastModeType()){
var _36=dojo.create("img",{src:highContrastIconVerifications,alt:"",style:{verticalAlign:"middle"}},_33);
dojo.place(_36,_35,"before");
}else{
var _36=dojo.create("img",{src:iconVerifications,alt:"",style:{verticalAlign:"middle"}},_35);
if(curam.util.isRtlMode()){
dojo.place(_36,_35,"after");
}else{
dojo.place(_36,_35,"before");
}
}
var _37=dojo.create("span",{innerHTML:_21},_32);
dojo.addClass(_37,"dashboardLegendSpan");
dojo.attr(_37,"aria-hidden","true");
if(curam.util.highContrastModeType()){
var _38=dojo.create("img",{src:highContrastIconInEditEvidence,alt:"",style:{verticalAlign:"middle"}},_33);
dojo.place(_38,_37,"before");
}else{
var _38=dojo.create("img",{src:iconInEditEvidence,alt:"",style:{verticalAlign:"middle"}},_37);
if(curam.util.isRtlMode()){
dojo.place(_38,_37,"after");
}else{
dojo.place(_38,_37,"before");
}
}
};
function createCategorySection(_39,i,_3a,_3b,_3c,_3d,_3e,_3f,_40){
var _41="ContentPanel_"+i;
var _42="Dashboard_"+_41;
var _43="Table_"+_41;
var _44=dojo.create("div",{id:"LabelHolder_"+_41});
dojo.addClass(_44,"contentPanelLabelsStyle");
var _45=dojo.create("div",{id:"LabelHolder_"+_41+"_inner",role:"application","aria-label":_3a},_44);
createCategoryHeader(_45,_43,i,"All_Label_"+_41,_3e,_3d,"all");
createCategoryHeader(_45,_43,i,"Recorded_Label_"+_41,_3e,_3c,"recorded");
var _46=dojo.create("table",{id:_43,role:"grid",width:"100%"});
dojo.addClass(_46,"treeHolderStyle");
var _47=dojo.create("tbody",null,_46);
titlePane=new curam.cefwidgets.evidence.DashboardTitlePane({title:_3a,id:"Title_"+_41,navigationContent:_44,content:_46,selectedButtonAltText:_3e,toggleExpandAltText:_3f,toggleCollapseAltText:_40});
dojo.place(titlePane.domNode,_39);
var _48=(dojo.NodeList(titlePane.domNode)[0]).querySelector("#Title_"+_41+"_pane");
if(_48!="undefined"){
dojo.attr(_46,"aria-label",dojo.attr(_48,"aria-label"));
}
var _49=dojo.create("br");
dojo.place(_49,_39);
dojo.place(_47,_39);
return _43;
};
function createCategoryHeader(_4a,_4b,i,id,_4c,_4d,_4e){
var _4f=dojo.create("div",{id:id,labelType:_4e,customtype:"label",role:"button",tabindex:"0"});
dojo.attr(_4f,"aria-pressed","false");
addEvent(_4f,"mouseover",filterMouseIn);
addEvent(_4f,"mouseout",filterMouseOut);
dojo.place(_4f,_4a);
if(currentFilter==_4e){
dojo.addClass(_4f,"backgroundHighlight");
dojo.attr(_4f,"alt",_4c+" "+_4d);
}else{
dojo.addClass(_4f,"backgroundNormal");
dojo.attr(_4f,"alt","");
}
var _50=dojo.create("div");
dojo.place(_50,_4f);
var _51=dojo.create("span",{innerHTML:_4d},_50);
dojo.addClass(_51,"filterLabelText");
var _52=[_4e,_4b,_4a.id,i,_4f];
dojo.connect(_4f,"onclick",dojo.partial(startFilter,_52));
dojo.connect(_4f,"onkeypress",dojo.partial(startFilterOnKeyPress,_52));
};
function startFilterOnKeyPress(_53,_54){
if(CEFUtils.enterKeyPress(_54)){
startFilter(_53);
}
};
function addEvent(obj,_55,fn){
if(obj.addEventListener){
obj.addEventListener(_55,fn,false);
}else{
if(obj.attachEvent){
obj["e"+_55+fn]=fn;
obj[_55+fn]=function(){
obj["e"+_55+fn](window.event);
};
obj.attachEvent("on"+_55,obj[_55+fn]);
}else{
obj["on"+_55]=obj["e"+_55+fn];
}
}
};
function filterMouseOut(){
this.className=currentFilterClass;
};
function filterMouseIn(){
currentFilterClass=this.className;
if(this.className=="backgroundNormal"){
this.className="backgroundNormalRollover";
}else{
if(this.className=="backgroundHighlight"){
this.className="backgroundHighlightRollover";
}
}
};
function startFilter(_56,_57){
currentFilter=_56[0];
filterDashboard(_56[0],_56[1],_56[2],_56[3]);
var _58="div#"+_56[4].parentNode.id+" > div";
dojo.query(_58).forEach(function(_59){
dojo.attr(_59,"aria-pressed","false");
dojo.attr(_59,"class","backgroundNormal");
});
dojo.attr(_56[4],"aria-pressed","true");
dojo.attr(_56[4],"class","backgroundHighlight");
};
function filterDashboard(_5a,_5b,_5c,_5d){
var _5e;
if(_5a=="recorded"){
_5e=recordedColumnPartitions[_5d];
}else{
_5e=allColumnPartitions[_5d];
}
if(_5e===undefined||_5e.length==0){
return;
}
var _5f=dojo.byId(_5b);
var _60=dojo.byId("dashboardData");
var _61=dojo.create("table",{id:_5b,role:"grid",width:"100%"});
dojo.attr(_61,"aria-label",dojo.attr(_5f,"aria-label"));
dojo.addClass(_61,"treeHolderStyle");
var _62=dojo.create("thead",null,_61);
var _63=dojo.create("tbody",null,_61);
var _64=dojo.query(">",(_60.children[_5d]).children[0]);
rowCounter=0;
colCounter=0;
var _65=dojo.create("tr",null,_62);
var tr=dojo.create("tr",{valign:"top"},_63);
var td=dojo.create("td",{width:"34%"},tr);
dojo.addClass(td,"dashboardColumn");
tdDiv=dojo.create("div",{role:"rowgroup"},td);
dojo.addClass(tdDiv,"dashboardColumnContent");
var x;
for(x=0;x<_64.length;x++){
var _66=true;
dojo.create("th",null,_65);
if(_5a=="recorded"&&dojo.attr(_64[x],"recorded")!="true"){
_66=false;
}
if(_66){
if(rowCounter>(_5e[colCounter]-1)){
colCounter++;
td=dojo.create("td",{width:"33%"},tr);
if(colCounter<2){
dojo.addClass(td,"dashboardColumn");
}
tdDiv=dojo.create("div",{role:"rowgroup"},td);
dojo.addClass(tdDiv,"dashboardColumnContent");
rowCounter=0;
}
var _67="tr_"+colCounter+"_"+rowCounter+"_"+_5b;
createEntityRowContent(_5a,_64[x],_67,tdDiv,caseID,0,_5e);
}
}
if((x==_64.length)&&(colCounter<2)){
for(var _68=++colCounter;_68<=2;_68++){
td=dojo.create("td",{width:"33%"},tr);
if(_68<2){
dojo.addClass(td,"dashboardColumn");
}
tdDiv=dojo.create("div",{role:"rowgroup"},td);
dojo.addClass(tdDiv,"dashboardColumnContent");
}
}
dojo.place(_61,_5f,"replace");
this.currentFilter=_5a;
currentFilterClass="backgroundHighlight";
};
function createEntityRowContent(_69,_6a,_6b,_6c,_6d,_6e,_6f){
var _70=dojo.create("tr",{id:_6b,tabindex:0,role:"row"},_6c);
var _71=dojo.create("td",{id:_6b+"_text",role:"gridcell"},_70);
dojo.addClass(_71,"dashboardTextColumn");
var _72=dojo.create("td",{id:_6b+"_create",role:"gridcell"},_70);
dojo.addClass(_72,"dashboardImageColumn");
createEntry(_71,_72,_6a,_6d,_6e);
var _73=dojo.attr(_6a,"readonly");
dojo.attr(_70,"onmouseover","highlightRow(this,'"+_73+"')");
dojo.attr(_70,"onmouseout","dehighlightRow(this)");
dojo.attr(_70,"onfocus","highlightRow(this,'"+_73+"')");
dojo.attr(_70,"onclick","highlightRow(this,'"+_73+"')");
dojo.attr(_70,"onblur","this.style.backgroundColor='transparent'");
dojo.attr(_70,"onkeypress",dojo.partial(fadeRow,[_70]));
var _74=_6a.getElementsByTagName("ul")[0];
var _75;
if(typeof (_74)!="undefined"){
_75=dojo.query(">",_74);
}
if(typeof (_75)!="undefined"){
if(_75.length>0){
_6e++;
for(var y=0;y<_75.length;y++){
var _76="true";
if((_69=="recorded")&&(dojo.attr(_75[y],"recorded")!="true")){
_76="false";
}
if(_76=="true"){
createEntityRowContent(_69,_75[y],_6b+"_"+y,_6c,_6d,_6e,_6f);
}
}
}
}
rowCounter++;
};
function createEntry(_77,_78,_79,_7a,_7b){
var _7c=_7b*15;
var _7d=dojo.create("div",{height:"22px"},_77);
if(dojo.isBodyLtr()){
dojo.setStyle(_7d,{"padding":"0 0 0 "+_7c+"px","vertical-align":"middle"});
}else{
dojo.setStyle(_7d,{"padding":"0 "+_7c+"px 0 0","vertical-align":"middle"});
}
var _7e="";
if(_7b>0){
_7e="-&nbsp;"+dojo.attr(_79,"name");
}else{
_7e=dojo.attr(_79,"name");
}
var _7f;
if((dojo.attr(_79,"recorded")=="true")){
_7f=dojo.create("span",null,_7d);
var _80=dojo.create("a",{href:"Evidence_workspaceTypeListPage.do?caseID="+this.caseID+"&evidenceType="+dojo.attr(_79,"evidenceType"),tabindex:0,onfocus:"highlightParentRow(this,'"+dojo.attr(_79,"readonly")+"')",title:dojo.attr(_79,"name"),"innerHTML":_7e},_7f);
if(_7b>0){
dojo.setStyle(_80,{"font-style":"italic"});
}
if(dojo.attr(_79,"mandatory")=="true"){
dojo.addClass(_7f,"mandatory-label");
}
if(dojo.attr(_79,"issues")!="0"){
if(curam.util.highContrastModeType()){
var _81=dojo.create("img",{src:highContrastIconIssues,style:{verticalAlign:"top"},alt:this.issueTooltip,title:this.issueTooltip},_7d);
}else{
var _81=dojo.create("img",{src:iconIssues,style:{verticalAlign:"top"},alt:this.issueTooltip,title:this.issueTooltip},_7d);
}
}
if(dojo.attr(_79,"verification")!="0"){
if(curam.util.highContrastModeType()){
var _81=dojo.create("img",{src:highContrastIconVerifications,style:{verticalAlign:"top"},alt:this.verificationTooltip,title:this.verificationTooltip},_7d);
}else{
var _81=dojo.create("img",{src:iconVerifications,style:{verticalAlign:"top"},alt:this.verificationTooltip,title:this.verificationTooltip},_7d);
}
}
if(dojo.attr(_79,"inedit")!="0"){
if(curam.util.highContrastModeType()){
var _81=dojo.create("img",{src:highContrastIconInEditEvidence,style:{verticalAlign:"top"},alt:this.inEditTooltip,title:this.inEditTooltip},_7d);
}else{
var _81=dojo.create("img",{src:iconInEditEvidence,style:{verticalAlign:"top"},alt:this.inEditTooltip,title:this.inEditTooltip},_7d);
}
}
}else{
_7f=dojo.create("span",{"innerHTML":_7e},_7d);
if(_7b>0){
dojo.setStyle(_7f,{"font-style":"italic"});
}
if(dojo.attr(_79,"mandatory")=="true"){
dojo.addClass(_7f,"mandatory-label");
}
}
var _82;
if(curam.util.highContrastModeType()){
_82=highContrastIconCreate;
}else{
_82=iconCreate;
}
var _81=dojo.create("img",{id:_78.id+"_icon",tabindex:0,role:"button",onfocus:"highlightParentRow(this,'"+dojo.attr(_79,"readonly")+"')",onmouseover:"highlightParentRow(this,'"+dojo.attr(_79,"readonly")+"')",onblur:"dehighlightParentRow(this)",src:_82,alt:this.createTooltip,title:this.createTooltip,href:"#",onclick:"openCreateEvidenceModal('"+dojo.attr(_79,"evidenceType")+"')",onkeypress:"if (CEFUtils.enterKeyPress(event)) {openCreateEvidenceModal('"+dojo.attr(_79,"evidenceType")+"')}"},_78);
};
function openCreateEvidenceModal(_83){
var _84=curam.util.UimDialog.open("Evidence_resolveCreateFromMetaDataPage.do",{caseID:this.caseID,evidenceType:_83});
};
function refreshEvidenceDashboard(_85,_86){
if(_85&&(_85!="")){
require(["curam/util/Navigation"],function(nav){
nav.goToUrl(_86+"Page.do?o3ctx=4096&caseID="+this.caseID+"&pageID="+_86+"&participantID="+this.participantID+"&groupFilterID="+_85+"&customFilterPage="+_86);
});
}
};
function highlightRow(_87,_88){
if(_87){
_87.style.backgroundColor="rgba(224,224,224,0.5)";
if(_88=="false"){
var _89=_87.querySelector("#"+_87.id+"_create_icon");
_89.style.visibility="visible";
}
}
};
function dehighlightRow(_8a){
if(_8a){
_8a.style.backgroundColor="transparent";
var _8b=_8a.querySelector("#"+_8a.id+"_create_icon");
_8b.style.visibility="hidden";
}
};
function fadeRow(_8c,_8d){
if(_8c){
if((_8d.keyCode==9)&&_8d.shiftKey){
_8c[0].style.backgroundColor="transparent";
var _8e=_8c[0].querySelector("#"+_8c[0].id+"_create_icon");
_8e.style.visibility="hidden";
}
}
};
function focusParentRow(_8f,_90){
if((_90.keyCode==9)&&_90.shiftKey){
var _91=dojo.NodeList(_8f).parents("tr").first();
if(_91[0]){
_91[0].style.backgroundColor="rgba(224,224,224,0.5)";
_91[0].focus();
}
}
};
function highlightParentRow(_92,_93){
if(_93=="false"){
var _94=dojo.NodeList(_92).parents("tr").first();
if(_94[0]){
_94[0].style.backgroundColor="rgba(224,224,224,0.5)";
if(_93=="false"){
var _95=_94[0].querySelector("#"+_94[0].id+"_create_icon");
_95.style.visibility="visible";
}
}
}
};
function dehighlightParentRow(_96,_97){
var _98=dojo.NodeList(_96).parents("tr").first();
dehighlightRow(_98[0]);
};
