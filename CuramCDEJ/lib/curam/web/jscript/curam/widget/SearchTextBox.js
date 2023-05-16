//>>built
require({cache:{"url:curam/widget/templates/SearchTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner searchTextBox input-placeholder-closed\" data-dojo-attach-point='textbox,focusNode' data-dojo-attach-event=\"onclick:__onClick,onFocus:__onClick,onblur:__onBlur,onkeyup:__onKeyUp,onpaste:__onPaste\" autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}'\n\t/></div\n></div>\n"}});
define("curam/widget/SearchTextBox",["dojo/query","dojo/dom-style","dojo/dom-construct","dojo/on","dojo/dom-geometry","dojo/dom-class","curam/util","dojo/dom-attr","dojo/_base/fx","dojo/_base/lang","dijit/form/TextBox","dojo/_base/declare","dojo/text!curam/widget/templates/SearchTextBox.html"],function(_1,_2,_3,on,_4,_5,_6,_7,fx,_8,_9,_a,_b){
var _c=_a("curam.widget.SearchTextBox",_9,{templateString:_b,backgroundColor:null,searchTextDiv:null,searchIconDiv:null,searchIcon:null,searchInputField:null,searchInputImg:null,placeholderText:null,applicationSearchDiv:null,searchControlsDiv:null,placeholderText:null,closedHeight:null,searchOptionsDiv:null,searchOptionsDivOpenedColor:null,originalInputColor:null,menuOpenedHeight:60,__populateValues:function(){
if(this.searchControlsDiv==null){
this.searchControlsDiv=_1(".search-input-controls");
}
if(this.searchIconDiv==null){
this.searchIconDiv=_1(".application-search-anchor-div");
}
if(this.searchIcon==null){
this.searchIcon=_1(".application-search-anchor");
}
if(this.searchInputField==null){
this.searchInputField=_1(".search-input-controls div.dijitInputField input")[0];
}
if(this.applicationSearchDiv==null){
this.applicationSearchDiv=_1(".application-search");
}
if(this.searchOptionsDiv==null){
this.searchOptionsDiv=_1(".search-options.no-dropdown");
}
if(this.searchOptionsDiv.length>0&&this.searchOptionsDivOpenedColor==null){
this.searchOptionsDivOpenedColor=_2.get(this.searchOptionsDiv[0],"color");
}
if(this.searchInputImg==null){
this.searchInputImg=_1(".application-search-anchor img");
}
},postCreate:function(){
this.__populateValues();
},__hideElements:function(){
if(this.__isSearchInputFieldPopulated()){
_2.set(this.searchInputField,"color",_2.get(this.applicationSearchDiv[0],"color"));
}else{
_2.set(this.searchInputField,this.originalInputColor);
}
_5.remove(this.searchInputField,"input-placeholder-opened");
_5.add(this.searchInputField,"input-placeholder-closed");
if(this.searchTextDiv!=null&&_2.get(this.searchTextDiv[0],"background-color")!=this.backgroundColor){
fx.animateProperty({node:this.searchTextDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
fx.animateProperty({node:this.searchControlsDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
fx.animateProperty({node:this.searchIconDiv[0],properties:{backgroundColor:{start:this.searchOptionsDivOpenedColor,end:this.backgroundColor}}}).play();
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-on-dark.svg";
if(this.searchOptionsDiv.length>0){
_5.remove(this.applicationSearchDiv[0],"application-search-upfront-popup");
_2.set(this.searchOptionsDiv[0],"display","none");
fx.animateProperty({node:this.applicationSearchDiv[0],properties:{height:{start:this.menuOpenedHeight,end:this.closedHeight}}}).play();
this.applicationSearchDiv.style({left:"0px"});
}
}
},__recursive:function(_d){
if(_d.parentElement!=null){
if(_5.contains(_d,"application-search")){
return true;
}else{
return this.__recursive(_d.parentElement);
}
}
return false;
},__checkBlur:function(_e){
var _f=this;
setTimeout(function(){
var _10=document.activeElement;
if(!(_5.contains(_10,"searchTextBox")||_5.contains(_10,"application-search-anchor")||_5.contains(_10,"curam")||(_10.parentElement!=null&&_5.contains(_10.parentElement,"search-options"))||_5.contains(_10,"search-options"))){
_f.__hideElements();
}
},1);
},_setPlaceHolderAttr:function(v){
this.__populateValues();
var _11=this;
on(_1("body.curam"),"click",function(evt){
var _12=_11.__recursive(evt.target);
if(!_12){
_11.__hideElements();
}
});
on(_1("body.curam"),"touchstart",function(evt){
var _13=_11.__recursive(evt.target);
if(!_13){
_11.__hideElements();
}
});
on(this.searchIcon,"blur",function(evt){
_11.__checkBlur(evt);
});
if(this.searchOptionsDiv.length>0){
on(this.searchOptionsDiv[0].firstChild,"blur",function(evt){
_11.__checkBlur(evt);
});
}
placeholderText=v;
this.searchInputField=this.domNode.firstChild.firstChild;
_7.set(this.searchInputField,"placeholder",placeholderText);
},__isSearchInputFieldPopulated:function(){
if(this.searchInputField.value.length>0){
return true;
}
return false;
},__onKeyUp:function(evt){
var _14=this.__isSearchInputFieldPopulated();
this.__enableOrDisableSearchLink(evt,_14);
},__onPaste:function(evt){
var _15=evt.clipboardData||window.clipboardData;
var _16=_15.getData("Text");
var _17=(_16&&_16.length>0)||this.__isSearchInputFieldPopulated();
this.__enableOrDisableSearchLink(evt,_17);
},__enableOrDisableSearchLink:function(evt,_18){
if(_18&&_18===true){
_5.remove(this.searchIcon[0],"dijitDisabled");
if(evt.keyCode==13){
curam.util.search("__o3.appsearch.searchText","__o3.appsearch.searchType");
}
}else{
_5.add(this.searchIcon[0],"dijitDisabled");
}
},__onClick:function(evt){
this.__populateValues();
if(this.searchTextDiv==null){
this.searchTextDiv=_1(".search-input-controls div.dijitInputField");
}
if(this.originalInputColor==null){
this.originalInputColor=_2.get(this.searchInputField,"color");
}
_2.set(this.searchInputField,"color",this.originalInputColor);
_5.remove(this.searchInputField,"input-placeholder-closed");
_5.add(this.searchInputField,"input-placeholder-opened");
if(this.searchOptionsDiv.length>0){
var _19=_4.position(this.searchInputField).x-11;
var _1a=_6.isRtlMode();
if(_1a!=null){
_19=_4.position(this.applicationSearchDiv[0]).x-20;
}
if(this.closedHeight==null){
this.closedHeight=_2.get(this.applicationSearchDiv[0],"height");
}
this.applicationSearchDiv.style({left:_19+"px"});
_5.add(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(_2.get(this.applicationSearchDiv[0],"height")!=60){
_2.set(this.searchOptionsDiv[0],"display","block");
_2.set(this.searchOptionsDiv[0],"opacity","0");
var _1b={node:this.searchOptionsDiv[0]};
fx.animateProperty({node:this.applicationSearchDiv[0],properties:{height:{start:this.closedHeight,end:this.menuOpenedHeight}}}).play();
fx.fadeIn(_1b).play();
}
}
if(this.backgroundColor==null){
this.backgroundColor=_2.get(this.searchTextDiv[0],"background-color");
}
if(this.searchTextDiv!=null&&_2.get(this.searchTextDiv[0],"background-color")!=this.searchOptionsDivOpenedColor){
fx.animateProperty({node:this.searchTextDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
fx.animateProperty({node:this.searchControlsDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
fx.animateProperty({node:this.searchIconDiv[0],properties:{backgroundColor:{start:this.backgroundColor,end:this.searchOptionsDivOpenedColor}}}).play();
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-enabled.svg";
}
},__onBlur:function(evt){
this.__populateValues();
if(!this.__isSearchInputFieldPopulated()){
this.searchInputField.value="";
_7.set(this.searchInputField,"placeholder",placeholderText);
}
this.__checkBlur(evt);
}});
return _c;
});
