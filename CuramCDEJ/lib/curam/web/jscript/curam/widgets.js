//>>built
define("curam/widgets",["dojo/dom","dojo/dom-style","dojo/dom-construct"],function(_1,_2,_3){
var _4=function(_5){
this.accordion=new _6(_5,this);
this.accordion.switchboard=this;
};
var _7={updateButtons:function(){
var _8=this.accordion;
this.collapser.disabled=_8.staysStill(false);
this.expander.disabled=_8.staysStill(true);
},switchMode:function(){
if(this.checked){
this.switchboard.accordion.accordMode=false;
this.switchboard.expander.onclick=this.switchboard.accordion.expandRest;
this.switchboard.collapser.style.display="";
this.switchboard.updateButtons();
}else{
this.switchboard.accordion.accordMode=true;
this.switchboard.collapser.style.display="none";
this.switchboard.accordion.collapseAll();
this.switchboard.expander.value="Expand All";
this.switchboard.expander.onclick=this.switchboard.accordion.expandAll;
this.switchboard.expander.disabled=false;
}
}};
dojo.mixin(_4.prototype,_7);
var _6=function(_9,_a){
var _b;
this.panelHeight="250px";
this.accordMode=true;
this.switchboard=_a;
this.topElement=_1.byId(_9);
this.tabs=[];
var _c=dojo.query("div",this.topElement);
for(var i=0;i<_c.length;i++){
if(_c[i].className=="accordionTab"){
while(_c[++i].className!="tabHeader"){
}
_b=_c[i];
while(_c[++i].className!="tabContent"){
}
this.tabs[this.tabs.length]=new _d(this,_b,_c[i]);
}
}
this.lastTab=this.tabs[0];
for(var i=1;i<this.tabs.length;i++){
this.tabs[i].collapse(false);
}
};
var _e={expandAll:function(){
var _f=this.switchboard.accordion;
for(var i=0;i<_f.tabs.length;i++){
_f.tabs[i].stateExpanded();
}
this.src="../themes/classic/images/evidence-review/CollapseAllButton.png";
this.onclick=_f.collapseAll;
},collapseAll:function(){
var _10=this.switchboard.accordion;
for(var i=0;i<_10.tabs.length;i++){
_10.tabs[i].collapse(false);
}
_10.lastTab.expand(false);
this.src="../themes/classic/images/evidence-review/ExpandAllButton.png";
this.onclick=_10.expandAll;
},expandRest:function(){
if(!this.switchboard.accordion.staysStill(true)){
this.switchboard.accordion.expandAll();
}
this.switchboard.updateButtons();
},collapseRest:function(){
if(!this.switchboard.accordion.staysStill(false)){
this.switchboard.accordion.collapseAll();
}
this.switchboard.updateButtons();
},staysStill:function(_11){
var _12=0;
var _13=this.tabs.length;
for(var i=0;i<_13;i++){
if(this.tabs[i].expanded==true){
_12++;
}
}
return (_11==true)?(_13-_12==0):(_12==1);
}};
dojo.mixin(_6.prototype,_e);
var _d=function(_14,_15,_16){
this.accordion=_14;
this.switchboard=_14.switchboard;
this.header=_15;
this.header.tab=this;
this.content=_16;
_2.set(this.content,{height:_14.panelHeight,overflow:"auto"});
this.content.tab=this;
this.expanded=true;
dojo.connect(this.header,"onclick",this.toggleState);
dojo.connect(this.header,"onmouseover",this.hoverStyle);
dojo.connect(this.header,"onmouseout",this.stillStyle);
};
var _17={hoverStyle:function(e){
if(!this.tab.expanded){
this.className+=" tabHeaderHover";
}
},stillStyle:function(e){
this.className="tabHeader";
},collapse:function(_18){
if(this.accordion.lastTab==this){
return;
}
if(this.accordion.staysStill(false)){
return;
}
if(_18&&this.accordion.accordMode==false){
new _19(this.content,"down");
}else{
_2.set(this.content,{height:"1px",display:"none"});
}
this.expanded=false;
this.content.style.overflow="hidden";
if(this.accordion.accordMode==false){
this.switchboard.updateButtons();
}
},expand:function(_1a){
if(this.accordion.lastTab==this){
return;
}
if(this.accordion.staysStill(true)){
return;
}
var _1b=this.accordion.lastTab;
this.stateExpanded(_1a);
this.accordion.lastTab=this;
if(this.accordion.accordMode==true){
_1b.collapse(true);
}else{
this.switchboard.updateButtons();
}
},stateExpanded:function(_1c){
if(_1c){
this.content.style.display="";
if(this.accordion.accordMode==true){
new _1d(this.content,this.accordion.lastTab.content);
}else{
new _19(this.content,"up");
}
}else{
_2.set(this.content,{height:this.accordion.panelHeight,display:"",overflow:"auto"});
this.expanded=true;
}
},toggleState:function(){
if(this.tab.expanded==true){
this.tab.collapse(true);
}else{
this.tab.expand(true);
}
}};
dojo.mixin(_d.prototype,_17);
var _19=function(_1e,_1f){
this.contentRef=_1e;
this.direction=_1f;
this.duration=100;
this.steps=6;
this.step();
};
var _20={step:function(){
var _21;
if(this.steps<=0){
if(this.direction=="down"){
_2.set(this.contentRef,{height:"1px",display:"none"});
this.contentRef.tab.expanded=false;
}else{
this.contentRef.style.height=this.contentRef.tab.accordion.panelHeight;
this.contentRef.tab.expanded=true;
}
this.contentRef.tab.switchboard.updateButtons();
return;
}
if(this.timer){
clearTimeout(this.timer);
}
var _22=Math.round(this.duration/this.steps);
if(this.direction=="down"){
_21=this.steps>0?(parseInt(this.contentRef.offsetHeight)-1)/this.steps:0;
}else{
_21=this.steps>0?(parseInt(this.contentRef.tab.accordion.panelHeight)-parseInt(this.contentRef.offsetHeight))/this.steps:0;
}
this.resizeBy(_21);
this.duration-=_22;
this.steps--;
this.timer=setTimeout(dojo.hitch(this,this.step),_22);
},resizeBy:function(_23){
var _24=this.contentRef.offsetHeight;
var _25=parseInt(_23);
if(_23!=0){
if(this.direction=="down"){
this.contentRef.style.height=(_24-_25)+"px";
}else{
this.contentRef.style.height=(_24+_25)+"px";
}
}
}};
dojo.mixin(_19.prototype,_20);
var _1d=function(_26,_27){
this.collapsingContent=_27;
this.collapsingContent.style.overflow="hidden";
this.expandingContent=_26;
this.limit=250;
this.duration=100;
this.steps=10;
this.expandingContent.style.display="";
this.step();
};
var _28={step:function(){
if(this.steps<=0){
_2.set(this.collapsingContent,{height:"1px",display:"none"});
_2.set(this.collapsingContent,{height:this.limit,overflow:"auto"});
this.collapsingContent.tab.expanded=false;
this.expandingContent.tab.expanded=true;
return;
}
if(this.timer){
clearTimeout(this.timer);
}
var _29=Math.round(this.duration/this.steps);
var _2a=this.steps>0?(parseInt(this.collapsingContent.style.height)-1)/this.steps:0;
this.resizeBoth(_2a);
this.duration-=_29;
this.steps--;
this.timer=setTimeout(dojo.hitch(this,this.step),_29);
},resizeBoth:function(_2b){
var h1=parseInt(this.collapsingContent.style.height);
var h2=parseInt(this.expandingContent.style.height);
var _2c=parseInt(_2b);
if(_2b!=0){
if(h2+_2c<this.limit){
this.collapsingContent.style.height=(h1-_2c)+"px";
this.expandingContent.style.height=(h2+_2c)+"px";
}
}
}};
dojo.mixin(_1d.prototype,_28);
var _2d={version:"1",AccordionControl:_4,AccordionWidget:_6,AccordionTab:_d,SingleSlowMotion:_19,SynchroSlowMotion:_1d,registerAccordion:function(id){
_4.constructor(id);
}};
var _2e=function(_2f){
this.steps=_2f;
this.regions=new Array();
this.RGB=new Array(256);
var k=0;
var hex=new Array("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
for(var i=0;i<16;i++){
for(j=0;j<16;j++){
this.RGB[k]=hex[i]+hex[j];
k++;
}
}
};
var _30={addRegion:function(_31){
this.regions[this.regions.length]=_31;
},drawMap:function(){
var _32;
if(this.steps%2==0){
_32=this.steps/2;
}else{
_32=(this.steps-1)/2;
}
var _33=parseInt(255/_32);
var red,_34,_35;
for(var i=0;i<this.steps;++i){
var _36;
if(i==0){
_36="#ff0000";
}else{
if(i==(this.steps-1)){
_36="#0000ff";
}else{
if(i==_32){
_36="#ffffff";
}else{
if(i>_32){
var _34=255;
var red=255;
_34-=(i-_32)*_33;
red-=(i-_32)*_33;
_36=this.rgbToHex(red,_34,255);
}else{
if(i<_32){
_34=0;
_35=0;
_34+=_33*i;
_35+=_33*i;
_36=this.rgbToHex(255,_34,_35);
}
}
}
}
}
var _37=_1.byId("heatmapTable");
if(_37){
var _38=_37.getElementsByTagName("td");
for(var j=0;j<_38.length;j++){
if(_38[j].className.indexOf("region"+this.regions[i])>-1){
_38[j].style.background=_36;
if(i>_32){
_2.set(dojo.query("a",_38[j])[0],"color","white");
}
}
}
}
_2.set(_1.byId("legendImage"+this.regions[i]),{color:_36,background:_36});
}
},rgbToHex:function(r,g,b){
var rr=this.RGB[r];
var gg=this.RGB[g];
var bb=this.RGB[b];
return "#"+rr+gg+bb;
}};
dojo.mixin(_2e.prototype,_30);
dojo.global.getDataIn=function(_39){
return eval(_39);
};
dojo.global.Widgets=_2d;
dojo.global.HeatMap=_2e;
return _2d;
});
