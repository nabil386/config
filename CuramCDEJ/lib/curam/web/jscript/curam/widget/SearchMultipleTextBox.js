//>>built
require({cache:{"url:curam/widget/templates/SearchMultipleTextBox.html":"<div class=\"dijit dijitReset dijitInline dijitLeft\" id=\"widget_${id}\" role=\"presentation\"\n\t><div class=\"dijitReset dijitInputField dijitInputContainer\"\n\t\t><input class=\"dijitReset dijitInputInner searchTextBox input-placeholder-closed\" data-dojo-attach-point='textbox,focusNode'\n\t\t\tdata-dojo-attach-event=\"onFocus:_givenFocus,onblur:_onBlur, onpaste:_onPaste\" autocomplete=\"off\"\n\t\t\t${!nameAttrSetting} type='${type}' \n\t/></div\n></div>\n"}});
define("curam/widget/SearchMultipleTextBox",["dojo/query","dojo/dom-style","dojo/dom-construct","dojo/on","dojo/dom-geometry","dojo/dom-class","dojo/mouse","dojo/dom-attr","dojo/_base/fx","dojo/_base/lang","dojo/_base/declare","dojo/_base/array","dojo/topic","dojo/debounce","dojo/Deferred","dojo/keys","curam/date/locale","dojo/has","dojo/sniff","dojo/text!curam/widget/templates/SearchMultipleTextBox.html","dijit/form/TextBox","curam/util","curam/util/Request","curam/util/ResourceBundle","curam/ui/UIController","curam/tab","curam/debug","curam/cdsl/connection/SimpleAccess","curam/cdsl/connection/CuramConnection","curam/tab"],function(_1,_2,_3,on,_4,_5,_6,_7,fx,_8,_9,_a,_b,_c,_d,_e,_f,has,_10,_11,_12,_13,_14,_15,_16,tab,_17,_18,_19,tab){
var _1a=_9("curam.widget.SearchMultipleTextBox",_12,{templateString:_11,quickSearch:null,hasIos:false,hasQuickSearch:null,quickSearchXHRPromises:[],searchResults:{},lastQueryTerm:"",spinnerTimeouts:[],_simpleAccess:_18,personQuantityToEagerLoad:6,paginationStart:0,paginationPageSize:6,pagesMappedToTab:[],pagesNotMappedToTab:[],pagesMappedToMultipleTabs:[],availableKeywordsList:null,menuOpenedComboboxHeight:72,menuOpenedFurtherOptionsHeight:18,menuOpenedFurtherOptionsHeightQuickSearch:12,menuOpenedFurtherOptionsHeightNoCombobox:18,quickSearchMenuOpenedHeight:29,currentOpenedHeight:29,maxItemsHeight:416,maxFlatListHeight:416,closedHeight:null,destroyContextMenu:false,personInContextMenu:null,constructor:function(){
this.inherited(arguments);
this.bundle=new _15("SmartNavigator");
},postCreate:function(){
this._simpleAccess.initConnection(new _19(curam.util.retrieveBaseURL()+"/dataservice"));
this.hasIos=has("ios");
this._queryDOMNodes();
_5.add(this.searchControlsDiv[0],"multiple-search-banner");
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
_5.add(this.searchOptionsDiv[0],"multiple-search-options");
}
this.searchInputField.maxLength=100;
on(this.applicationSearchDiv[0],"mousedown",_8.hitch(this,function(evt){
this.clickedInsideApplicationSearchWidget=this._isElementPartOfApplicationSearch(evt.target);
}));
on(this.applicationSearchDiv[0],"mouseup",_8.hitch(this,function(evt){
this.clickedInsideApplicationSearchWidget=false;
}));
if(this.searchOptionsDiv.length>0){
on(this.searchOptionsDiv[0].firstChild,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
}
var _1b=this.searchIcon[0].onclick;
this.searchIcon[0].onclick=_8.hitch(this,function(e){
if(!_5.contains(this.searchIcon[0],"dijitDisabled")){
if(this.quickSearch){
this._search();
}else{
_1b(e);
}
}
});
on(this.searchIcon,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(this.searchIcon,"click",_8.hitch(this,function(evt){
if(!this.quickSearch){
this._hideSearchBox();
}
}));
if(this.hasQuickSearch){
this._initializeSmartNavigatorWidget();
}else{
on(this.searchInputField,"keydown",_8.hitch(this,this._onKeyUp));
}
},_initializeSmartNavigatorWidget:function(){
_b.subscribe("curam/application-search/combobox-changed",_8.hitch(this,function(_1c){
if(this.quickSearch!=null){
if(_1c.indexOf("smart-navigator|smart-navigator")>=0){
this._enableQuickSearch();
}else{
this._disableQuickSearch();
}
this._fixQuickSearchHeight();
}
}));
var _1d=this._simpleAccess.makeRequest("SmartNavigatorFacade","getSearchConfiguration");
this.quickSearchXHRPromises.push(_1d);
_1d.then(_8.hitch(this,function(_1e){
var _1f=false;
if(_1e[0].debounceTimeout){
var _20=parseInt(_1e[0].debounceTimeout);
if(!isNaN(_20)&&_20>=0){
on(this.searchInputField,"keydown",_c(_8.hitch(this,this._onKeyUpAutoComplete),_20));
_1f=true;
}
}
if(!_1f){
on(this.searchInputField,"keydown",_8.hitch(this,this._onKeyUp));
}
}),_8.hitch(this,this._ajaxErrorCallback));
on(this.searchInputField,"keyup",_8.hitch(this,function(_21){
this._onDownArrowKey(_21);
}));
on(this.searchInputField,"keydown",_8.hitch(this,function(_22){
if(_22.keyCode==8){
var _23=(window.getSelection().toString()===this.searchInputField.value)?true:false;
if(_23){
this._removeAriaOwnsAttributeFromHiddenInfoSpanElement();
}
}
}));
on(this.searchIcon,"keyup",_8.hitch(this,function(_24){
this._onDownArrowKey(_24);
}));
on(window,"resize",_c(_8.hitch(this,function(){
var _25=_1(".application-search-upfront-popup")[0];
if(_25&&!has("android")&&!has("ios")){
this._hideSearchBox();
}
}),50));
on.emit(window,"resize",{bubbles:true,cancelable:true});
this.searchIcon[0].title=this.bundle.getProperty("SmartNavigator.icon.title");
on(_1("body.curam"),["click","touchstart","keyup"],_8.hitch(this,function(evt){
if(evt.type==="keyup"&&evt.keyCode!==_e.ESCAPE){
return;
}
var _26=evt.type==="keyup";
var _27=this._destroyContextMenuIfNecessary(evt.target);
var _28=this._hideKeywordsListIfNecessary(evt.target,_26);
var _29=_5.contains(evt.target,"appSearchItem");
if(evt.type==="keyup"){
if(_29){
var _2a=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_2a){
_2a.focus();
}
}else{
if(!_27&&!_28){
this._hideSearchBox();
if(this._isElementPartOfApplicationSearch(evt.target)){
this.searchIcon[0].focus();
}
}
}
}else{
var _2b=this._isElementPartOfApplicationSearch(evt.target);
if(!_2b){
this._hideSearchBox();
}
}
}));
},_queryDOMNodes:function(){
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
this.searchInputField=this.focusNode;
}
if(this.inputNode==null){
this.inputNode=_1(".search-input-controls .text")[0];
}
if(this.applicationSearchDiv==null){
this.applicationSearchDiv=_1(".application-search");
}
if(this.searchOptionsDiv==null){
this.searchOptionsDiv=_1(".search-options");
}
if(this.searchOptionsDiv.length>0&&this.searchOptionsDivOpenedColor==null){
this.searchOptionsDivOpenedColor=_2.get(this.searchOptionsDiv[0],"color");
}
if(this.searchInputImg==null){
this.searchInputImg=_1(".application-search-anchor img");
}
if(this.quickSearch==null){
this.quickSearch=_1(".application-search.quick-search").length>0;
}
if(this.hasQuickSearch==null){
this.hasQuickSearch=_1(".application-search.has-quick-search").length>0;
}
},_hideSearchBox:function(){
if(this._isSearchInputFieldPopulated()){
_2.set(this.searchInputField,"color",_2.get(this.applicationSearchDiv[0],"color"));
}else{
_2.set(this.searchInputField,this.originalInputColor);
}
_5.remove(this.searchInputField,"input-placeholder-opened");
_5.add(this.searchInputField,"input-placeholder-closed");
_7.remove(this.searchInputField,"aria-describedby");
if(this.searchTextDiv!=null&&_2.get(this.searchTextDiv[0],"background-color")!=this.backgroundColor){
_2.set(this.searchTextDiv[0],"background-color",this.backgroundColor);
_2.set(this.searchControlsDiv[0],"background-color",this.backgroundColor);
_2.set(this.searchIconDiv[0],"background-color",this.backgroundColor);
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-on-dark.svg";
_5.remove(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(this.searchOptionsDiv.length>0){
_2.set(this.searchOptionsDiv[0],"display","none");
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_2.set(this.appBannerComboBoxDiv[0],"display","none");
}
_2.set(this.applicationSearchDiv[0],"height",this.closedHeight+"px");
this.applicationSearchDiv.style({left:"0px"});
this._hideResultList();
}
},_isElementPartOfApplicationSearch:function(_2c){
if(_2c.parentElement!=null){
if(_5.contains(_2c,"application-search")||_5.contains(_2c,"dijitComboBoxMenuPopup")){
return true;
}else{
return this._isElementPartOfApplicationSearch(_2c.parentElement);
}
}
return false;
},_hideSearchBoxIfNotFocused:function(evt){
var _2d=this;
setTimeout(function(){
var _2e=document.activeElement;
if(!_2d._isElementPartOfContainer(_2e,"application-search")&&!_2d.clickedInsideApplicationSearchWidget){
_2d._hideSearchBox();
}
},1);
},_setPlaceHolderAttr:function(v){
this.searchInputField=this.domNode.firstChild.firstChild;
_7.set(this.searchInputField,"placeholder",v);
},_isSearchInputFieldPopulated:function(){
return this.searchInputField.value.length>0;
},_onKeyUp:function(evt){
if(evt.keyCode===_e.DOWN_ARROW){
return false;
}
var _2f=this._isSearchInputFieldPopulated();
this._enableOrDisableSearchLink(evt,_2f);
if((evt.keyCode===_e.BACKSPACE||evt.keyCode===_e.DELETE)&&this.quickSearch&&!_2f&&!this._isShowingHistory()){
this.lastQueryTerm="";
this._hideDropDown();
this._showSearchHistory();
}else{
return false;
}
},_onKeyUpAutoComplete:function(evt){
if(evt.keyCode===_e.DOWN_ARROW){
return false;
}
this._onKeyUp(evt);
if(evt.target.value.trim()!==this.lastQueryTerm.trim()){
this._doQuickSearch(evt.target.value);
}
},_onDownArrowKey:function(_30){
if(_30.keyCode===_e.DOWN_ARROW){
var _31=_1(".appSearchDropDownButton",this.applicationSearchDiv[0]);
if(_31.length>0){
_31[0].focus();
}else{
var _32=_1(".appSearchKeywordTag",this.applicationSearchDiv[0]);
if(_32.length>0){
_32[0].focus();
}else{
var _33=_1(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(_33.length>0){
_33[0].focus();
}
}
}
}
},_onKeywordsDownArrowKey:function(_34){
if(_34.keyCode===_e.DOWN_ARROW){
if(_5.contains(_34.target,"appSearchKeywordsListButton")){
if(this._showingAvailableKeywordsList){
_1(".availableKeyword .keywordTerm",this.applicationSearchDiv[0])[0].focus();
}else{
var _35=_1(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(_35.length>0){
_35[0].focus();
}
}
}else{
var _36=this._findSiblingElement(_34.target,true,"keywordTerm");
if(_36){
_36.focus();
}else{
var _37=this._findSiblingElement(_34.target.parentElement.parentElement,true,"availableKeyword");
if(_37){
_36=_1(".keywordTerm:first-child",_37)[0];
if(_36){
_36.focus();
}
}
}
}
}
},_onKeywordsUpArrowKey:function(_38){
if(_38.keyCode===_e.UP_ARROW){
var _39=this._findSiblingElement(_38.target,false,"keywordTerm");
if(_39){
_39.focus();
}else{
var _3a=this._findSiblingElement(_38.target.parentElement.parentElement,false,"availableKeyword");
if(_3a){
_39=_1(".keywordTerm:last-child",_3a)[0];
if(_39){
_39.focus();
}
}
}
}
},_search:function(){
if(this.quickSearch){
this._doQuickSearch(this.searchInputField.value);
if(!_5.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
}
}else{
curam.util.search("__o3.appsearch.searchText","__o3.appsearch.searchType");
this._hideSearchBox();
}
},_doQuickSearch:function(_3b){
if(this.quickSearch){
if(_3b){
this.lastQueryTerm=_3b;
this._resetSearchResults();
var _3c={term:_3b,dateFormat:window.jsDF};
if(this.quickSearchXHRPromises.length>0){
this.quickSearchXHRPromises.forEach(function(_3d){
_3d.cancel();
});
this.quickSearchXHRPromises=[];
}
var _3e=this._simpleAccess.makeRequest("SmartNavigatorFacade","search",_3c);
this.quickSearchXHRPromises.push(_3e);
_3e.then(_8.hitch(this,this._searchCallback,_3b),_8.hitch(this,this._ajaxErrorCallback));
this._showSpinnerDiv();
}else{
this.lastQueryTerm="";
this._hideDropDown();
this._showSearchHistory();
}
}
},_showSearchHistory:function(){
this._resetSearchResults();
this._cancelPreviousAjaxCalls();
var _3f=this._simpleAccess.makeRequest("SmartNavigatorFacade","searchUserHistory");
this.quickSearchXHRPromises.push(_3f);
_3f.then(_8.hitch(this,this._showSearchHistorySuccessCallback),_8.hitch(this,this._ajaxErrorCallback));
},_showSearchHistorySuccessCallback:function(_40){
var _41=_40[0]._data;
this.quickSearchXHRPromises=[];
if(_41.dtls&&_41.dtls.length>0){
var _42=_41.dtls.map(function(_43){
var _44={url:_43.url,icon:_43.icon,preferredTabs:_43.preferredTabs,isModal:_43.isModal};
if(_43.targetValue){
_44.action={description:_43.targetDescription,type:_43.targetType};
}
if(_43.searchConcernRoleName){
_44.person={concernRoleName:_43.searchConcernRoleName,dateOfBirth:_43.searchConcernRoleDateOfBirth,concernRoleId:_43.searchConcernRoleId,formattedAddress:_43.searchConcernRoleAddress,restrictedIndOpt:_43.searchConcernRoleRestrictedIndOpt,personPhotoURL:_43.personPhotoURL,items:[]};
}
return _44;
});
this._searchCallback("",[{_data:{data:_42}}],this.bundle.getProperty("SmartNavigator.recent.searches"),true);
}
},_ajaxErrorCallback:function(err){
if(this.quickSearch&&err.name!=="CancelError"){
_17.log("Application Search - Smart Navigator Error: ",err);
this.quickSearchXHRPromises=[];
this._hideSpinnerDiv();
if(!_5.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
}
if(err.errors&&err.errors[0]&&err.errors[0].type&&err.errors[0].type==="error"&&err.errors[0].message){
this._createMessage(err.errors[0].message);
}else{
this._createMessage(this.bundle.getProperty("SmartNavigator.error"));
}
this._fixQuickSearchHeight();
}
return [];
},_searchCallback:function(_45,_46,_47,_48){
var _49=_46[0]._data;
var _4a=this;
_4a.quickSearchXHRPromises=[];
_4a.paginationTotalItems=0;
_4a.searchResults=_49.data.reduce(function(_4b,_4c,_4d){
_4c.rendered=false;
if(_4c.person&&_4c.person.concernRoleId!=="0"){
_4b.people.push(_4c);
_4b.personQueryTerms.push(_4c.person.terms);
_4b.hasItems=true;
_4a.paginationTotalItems++;
if(_4c.person.restrictedIndOpt){
_4c.person.formattedDateOfBirth="******";
}else{
if(_4c.person.dateOfBirth&&_4c.person.dateOfBirth.getTime()!==-62135769600000){
_4c.person.formattedDateOfBirth=_f.format(_f.parseDate(_4c.person.dateOfBirth),{selector:"date",datePattern:window.jsDF})+", "+_4a.bundle.getProperty("SmartNavigator.age")+" "+_4a._getAge(_4c.person.dateOfBirth);
}else{
_4c.person.formattedDateOfBirth=_4a.bundle.getProperty("SmartNavigator.age.not.recorded");
}
}
if(_4c.action&&_4c.action.description){
_4c.items=[_4c.action];
}else{
_4c.action=null;
}
}else{
if(_4c.action){
_4c.person=null;
_4b.actionsOnly.push(_4c);
_4b.hasItems=true;
_4a.paginationTotalItems++;
}
}
return _4b;
},_4a.searchResults);
_4a.searchResults.messages.info=_49.infoMessage;
_4a.searchResults.header=_47;
_4a.searchResults.isHistory=_48;
_4a.searchResults.keywords=_49.keywords||[];
_4a.searchResults.queryTerm=_45;
if(_4a.searchResults.people.length>0&&_4a.searchResults.keywords.length>0){
var _4e=_4a.searchResults.people.slice(0,this.personQuantityToEagerLoad);
_4a._loadPeopleItems(_4e).then(function(){
_4a._hideSpinnerDiv();
_4a._renderResults();
});
}else{
_4a._hideSpinnerDiv();
_4a._renderResults();
}
},_loadPeopleItems:function(_4f){
var _50=new _d();
var _51="";
var _52={};
_4f.forEach(function(_53){
var _54=_8.clone(_53.person);
_54.personTabDetailsURL=null;
_54.personPhotoURL=null;
_54.personTabDetailsURL=null;
_54.dateOfBirth=null;
_54.formattedDateOfBirth=null;
_51+=JSON.stringify(_54)+"@@@";
_52[_54.concernRoleId]=[];
});
_51=_51.substring(0,_51.lastIndexOf("@@@"));
this._cancelPreviousAjaxCalls();
var _55=this._simpleAccess.makeRequest("SmartNavigatorFacade","searchForTargetsOfPeople",{searchQuery:this.searchResults.queryTerm,peopleDetails:_51});
this.quickSearchXHRPromises.push(_55);
_55.then(_8.hitch(this,function(_56,_57,_58){
var _56=_58[0].data.reduce(function(map,_59,_5a){
map[_59.concernRole].push(_59);
return map;
},_56);
_57.forEach(function(_5b){
_5b.person.itemsLazyLoad=false;
_5b.person.items=_56[_5b.person.concernRoleId];
});
_50.resolve();
},_52,_4f),_8.hitch(this,this._ajaxErrorCallback));
return _50;
},_renderResults:function(_5c){
var _5d=this;
var _5e=_1(".searchResultsScreenReadersInfo.hidden",this.applicationSearchDiv[0])[0];
if(_5d.searchResults.hasItems){
_5d.searchResults.moreRecords=_5d.paginationTotalItems>_5d.paginationStart+_5d.paginationPageSize;
_5d._createResultsList(_5d.searchResults.header,_5d.searchResults.isHistory,_5d.searchResults.keywords);
if(this.searchResults.keywords&&this.paginationStart===0){
_5d._createKeywordTags(this.searchResults.keywords,this.searchResults.personQueryTerms);
}
_5d._createMessages();
if(_5d.paginationTotalItems<=_5d.personQuantityToEagerLoad&&_5d._getItemsHeight(true)<_5d.maxFlatListHeight){
_5d._showHiddenResultRows();
}else{
if(_5d.searchResults.keywords.length>0){
_5d._createPersonItemsContextMenuButton();
}
}
}else{
if(_5d.searchResults.queryTerm.length>2){
_5d._createMessage(_5d.bundle.getProperty("SmartNavigator.empty.search"),true,true);
}else{
if(_5d.searchResults.queryTerm.length<=2){
_7.remove(_5e,"aria-owns");
}
}
}
_5.add(_5d.applicationSearchDiv[0],"shadow");
_5d._fixQuickSearchHeight();
if(!_5.contains(this.applicationSearchDiv[0],"application-search-upfront-popup")){
this._givenFocus();
this.searchInputField.focus();
}
if(_5c){
var _5f=_1(".appSearchNewItem",_5d.applicationSearchDiv[0]);
if(_5f.length>0){
_5f[0].focus();
}
}
var _60=_1(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0])[0];
if(_60){
_7.set(this.searchInputField,"aria-describedby","resultRowHeaderId");
}else{
_7.remove(this.searchInputField,"aria-describedby");
}
if(_5e){
var _61=_1(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
if(_61&&_61.childElementCount>0){
_7.set(_5e,"aria-owns","applicationSearchResultListId");
}else{
_7.remove(_5e,"aria-owns");
}
}
_5d._updateSearchResultsScreenReadersInfo();
_5d._autoScrollToNewItems();
},_removeAriaOwnsAttributeFromHiddenInfoSpanElement:function(){
var _62=_1(".searchResultsScreenReadersInfo.hidden",this.applicationSearchDiv[0])[0];
_7.remove(_62,"aria-owns");
},_createResultsList:function(){
var _63=_1(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _64=null;
if(this.paginationStart===0){
this._destroyResultList();
if(this.searchResults.moreRecords){
this._createMoreRecordsRow(_63);
}
if(this.searchResults.hasItems){
outerContainer=_3.place("<div class=\"appSearchItemsOuterContainer\" role=\"presentation\" id=\"applicationSearchResultListId\" aria-live=\"assertive\"></div>",_63,"after");
_64=_3.place("<ul class=\"appSearchItemsContainer\" aria-live=\"assertive\"></ul>",outerContainer,"last");
on(outerContainer,"scroll",_8.hitch(this,function(_65){
if(!this._isElementPartOfContainer(document.activeElement,"appSearchItemContext")){
_1(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(_66){
_5.remove(_66,"clicked");
});
}
}));
}
}else{
if(!this.searchResults.moreRecords){
this.searchInputField.focus();
_1(".moreItems",this.applicationSearchDiv[0]).forEach(_3.destroy);
}
_64=_1(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
}
var _67=0;
if(this.searchResults.actionsOnly&&this.searchResults.actionsOnly.length>0){
this.searchResults.actionsOnly.forEach(function(_68){
if(_67<this.paginationPageSize){
if(!_68.rendered){
_67++;
}
this._createResultRow(_68,_64,"Action",this.searchResults.isHistory);
}
},this);
}
this.searchResults.people.forEach(function(_69){
if(_67<this.paginationPageSize){
if(!_69.rendered){
_67++;
}
this._createResultRow(_69,_64,"Person",this.searchResults.isHistory);
}
},this);
if(this.searchResults.header&&this.paginationStart===0){
this._createResultRowHeader(this.searchResults.header);
}
if(this.searchResults.hasItems&&this.paginationStart===0){
_5.remove(this.applicationSearchDiv[0],"application-search-items-list");
_5.add(this.applicationSearchDiv[0],"application-search-items-list");
}
var _6a=_1(".appSearchItemsOuterContainer",this.applicationSearchDiv[0])[0];
if(_6a&&_6a.scrollHeight>_6a.clientHeight){
_5.add(_63.parentNode,"hasScrollBars");
var _6b=_1(".moreItems",this.applicationSearchDiv[0])[0];
if(_6b){
_2.set(_6b,"width",_64.offsetWidth+"px");
}
}else{
_5.remove(_63.parentNode,"hasScrollBars");
}
},_createResultRow:function(_6c,_6d,_6e,_6f,_70){
if(!_6c.rendered){
if(_6f&&_6c.person&&_6c.action){
_6e="Action";
_6c.action.type="REQUIRES_PERSON_HISTORY";
}
var _71=null;
if(_6e==="Person"){
_71=this._createPersonRow(_6c,_6d,_6e,_6f,_70);
}else{
if(_6e==="Action"){
_71=this._createActionOnlyRow(_6c,_6d,_6f);
}else{
if(_6e==="Action_Person"){
_71=this._createPersonActionRow(_6c,_6d,_6f,_70);
}
}
}
if(_71){
on(_71,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
}
}
_6c.rendered=true;
},_createPersonRow:function(_72,_73,_74,_75){
var _76="active";
if(!_72.url||_72.url.length===0||_72.person.restrictedIndOpt){
_76="inactive";
}
var _77=_75?" appSearchItemHistory ":"";
var _78=this.paginationStart>0?" appSearchNewItem ":"";
var _79=_72.person.restrictedIndOpt?" restrictedPerson ":"";
var _7a=_79?this.bundle.getProperty("SmartNavigator.sensitity.error"):_72.person.concernRoleName;
var _7b=_79?"":_72.person.formattedDateOfBirth;
var _7c=_79?"":this.bundle.getProperty("SmartNavigator.address")+" "+_72.person.formattedAddress;
var _7d="<li class=\"appSearchItem-ListItem person\">";
_7d+="<span tabindex=\"0\" role=\"button\" class=\"appSearchItem person dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox "+_76+_78+" "+_77+_79+"\""+" data-concernRole=\""+_72.person.concernRoleId+"\" >";
_7d+="<span class=\"itemIcon personPhoto\" aria-hidden=\"true\">"+"<img draggable=\"false\" alt=\"Person image\" src=\""+_72.person.personPhotoURL+"\" /></span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine person\" aria-label=\""+_7a+"\" >"+_72.person.concernRoleName+"</span>"+"<span class=\"itemDescSecondLine address\" aria-label=\""+_7b+"\">"+_72.person.formattedDateOfBirth+"</span>"+"<span class=\"itemDescSecondLine address\" aria-label=\""+_7c+"\">"+_72.person.formattedAddress+"</span></span>";
_7d+="</span>";
_7d+="</li>";
var _7e=_3.toDom(_7d);
if(_76=="active"){
on(_7e,["click","keyup"],_8.hitch(this,function(_7f){
this._onItemClick(_7f,_72,_75);
}));
}
on(_7e,["keyup"],_8.hitch(this,function(_80){
this._onItemKeyNavigation(_7e,_80);
}));
on(_7e,["focus"],_8.hitch(this,function(_81){
this._destroyPersonItemsContextMenu(_81);
}));
_3.place(_7e,_73,"last");
if(_72.person.items&&!_72.person.restrictedIndOpt){
_72.person.items.forEach(function(_82,_83){
_82.person=_8.clone(_72.person);
_82.person.items=[];
_82.first=(_83===0);
_82.last=(_83===_72.person.items.length-1);
this._createResultRow(_82,_73,"Action_Person",_75);
},this);
}
return _7e;
},_createPersonActionRow:function(_84,_85,_86,_87){
var _88="active";
if(!_84.url&&_84.url.length===0){
_88="inactive";
}
var _89=_84.first?"first":"";
var _8a=_84.last?"last":"";
var _8b=_86?"appSearchItemHistory":"";
var _8c=_87?"contextMenuItem":"hide";
var _8d="<li tabindex=\"0\" role=\"button\" aria-live=\"assertive\" class=\"appSearchItem appSearchItem-ListItem personAction dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox  "+_8c+" "+_89+" "+_8a+" "+_88+" "+_8b+"\" "+"\" >";
_8d+="<div class=\"personActionItem\">";
_8d+="<span class=\"itemIcon actionPhoto\">";
if(_84.icon){
_8d+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+_84.icon+"\" alt=\"Action Icon\" />";
}else{
_8d+="<span class=\"actionImage actionEnabled\" /></span>";
}
_8d+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+_84.action.description+"</span>"+"</span>";
_8d+="</div>";
_8d+="</li>";
var _8e=_3.toDom(_8d);
if(_88=="active"){
on(_8e,["click","keyup"],_8.hitch(this,function(_8f){
this._onItemClick(_8f,_84,_86);
}));
}
on(_8e,["keyup"],_8.hitch(this,function(_90){
this._onItemKeyNavigation(_8e,_90);
}));
_3.place(_8e,_85,"last");
return _8e;
},_createActionOnlyRow:function(_91,_92,_93){
var _94="active";
if(!_91.url&&_91.url.length===0){
_94="inactive";
}
var _95=_93?"appSearchItemHistory":"";
var _96="title=\"";
if(_91.action.type==="REQUIRES_PERSON_HISTORY"){
_96+=_91.action.description+" - "+_91.person.concernRoleName+" - "+_91.person.formattedDateOfBirth+"\"";
}else{
if(_91.action.type&&_91.action.type==="REQUIRES_PERSON"){
_96+=_91.action.description+"\"";
}else{
_96+=_91.action.description+"\"";
}
}
var _97="<li tabindex=\"0\" role=\"button\" class=\"appSearchItem appSearchItem-ListItem actionOnly dijit dijitReset dijitInline dijitLeft"+"appBannerComboBox dijitTextBox dijitComboBox dijitValidationTextBox "+_94+" "+_95+"\" "+">";
if(_91.action.type==="REQUIRES_PERSON_HISTORY"){
_97+="<span class=\"itemIcon actionPhoto\">";
if(_91.icon){
_97+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+_91.icon+"\" alt=\"Action Icon\" />";
}else{
_97+="<span class=\"actionImage actionEnabled\" /></span>";
}
_97+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+_91.action.description+"</span>"+"<span class=\"itemDescSecondLine\">"+_91.person.concernRoleName+"</span>"+"<span class=\"itemDescSecondLine\">"+_91.person.formattedDateOfBirth+"</span>"+"</span>";
}else{
if(_91.action.type&&_91.action.type==="REQUIRES_PERSON"){
_97+="<span class=\"itemIcon actionPhoto\">";
if(_91.icon){
_97+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+_91.icon+"\" alt=\"Action Icon\" />";
}else{
_97+="<span class=\"actionImage actionDisabled\" /></span>";
}
_97+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescFirstLine action\">"+_91.action.description+"</span>"+"<span class=\"itemDescSecondLine infoRequired\"> ["+this.bundle.getProperty("SmartNavigator.person.required")+"] </span>"+"</span>";
}else{
_97+="<span class=\"itemIcon actionPhoto\">";
if(_91.icon){
_97+="<img draggable=\"false\" src=\""+jsBaseURL+"/"+_91.icon+"\" alt=\""+this.bundle.getProperty("SmartNavigator.action.icon")+"\" />";
}else{
_97+="<span class=\"actionImage actionEnabled\" /></span>";
}
_97+="</span>"+"<span class=\"itemDescription\">"+"<span class=\"itemDescSingleLine action\">"+_91.action.description+"</span>"+"</span>";
}
}
_97+="</li>";
var _98=_3.toDom(_97);
if(_94=="active"){
on(_98,["click","keyup"],_8.hitch(this,function(_99){
this._onItemClick(_99,_91,_93);
}));
}
on(_98,["keyup"],_8.hitch(this,function(_9a){
this._onItemKeyNavigation(_98,_9a);
}));
on(_98,["focus"],_8.hitch(this,function(_9b){
this._destroyPersonItemsContextMenu(_9b);
}));
_3.place(_98,_92,"last");
return _98;
},_createPersonItemsContextMenuButton:function(){
var _9c=this;
_1(".appSearchItem.person:not(.restrictedPerson):not(.hide)",_9c.applicationSearchDiv[0]).forEach(function(_9d){
if(_1(".personItems",_9d.parentElement).length===0){
var _9e=true;
_9c.searchResults.keywords.forEach(function(_9f){
if(_9f.type!=="NO_REQUIREMENT"){
_9e=false;
}
});
if(_9e){
return false;
}
var _a0=_9c.hasIos?"SmartNavigator.button.to.toggle.items.ios":"SmartNavigator.button.to.toggle.items";
var _a1=_3.toDom("<span tabindex=\"0\" aria-label=\""+_9c.bundle.getProperty(_a0)+"\" role=\"button\" aria-expanded=\"false\" class=\"personItems\" "+"data-concernrole=\""+_9d.dataset.concernrole+"\">"+"</span>");
_3.place(_a1,_9d,"after");
_5.add(_9d,"hasPersonItemBtn");
on(_a1,["click"],_8.hitch(_9c,_9c._createPersonItemsContextMenu,_9d,_a1));
if(!_9c.hasIos){
on(_a1,[_6.enter,"keyup"],_8.hitch(_9c,_9c._createPersonItemsContextMenu,_9d,_a1));
}
on(_a1,[_6.leave],_8.hitch(_9c,_9c._destroyPersonItemsContextMenu));
on(_a1,["blur"],function(e){
_9c._destroyPersonItemsContextMenu(e);
_9c._hideSearchBoxIfNotFocused(e);
});
}
});
},_createPersonItemsContextMenu:function(_a2,_a3,_a4){
if(_a4.type==="keyup"&&(_a4.keyCode!==_e.ENTER&&_a4.keyCode!==_e.SPACE)){
return;
}
var _a5=this;
var _a6=false;
if(_a4.type==="click"||_a4.type==="keyup"){
_a6=true;
_5.add(_a3,"clicked");
_a4.preventDefault();
_a4.stopPropagation();
}
if(_a5._showingAvailableKeywordsList){
_a5._hideAvailableKeywordsList();
}
_a5.personInContextMenu=_a5.searchResults.people.filter(function(_a7){
return _a7.person.concernRoleId===_a2.dataset.concernrole;
})[0];
var _a8=_1(".appSearchItemContext",_a5.applicationSearchDiv[0])[0];
if(!_a8||!_5.contains(_a8,"clicked")||_a6){
_3.destroy(_a8);
if(_a6){
_1(".personItems.clicked",_a5.applicationSearchDiv[0]).forEach(function(_a9){
if(_a9.dataset.concernrole!==_a5.personInContextMenu.person.concernRoleId){
_5.remove(_a9,"clicked");
}
});
}
var _aa=_1(".search-input-controls .text",_a5.applicationSearchDiv[0])[0];
var _ab=_1(".appSearchItemsOuterContainer",_a5.applicationSearchDiv[0])[0];
var _ac=_ab.scrollHeight>_ab.clientHeight?" scrollbars ":"";
var _ad=_a6?" clicked ":"";
var _ae=_3.toDom("<ul role=\"status\" class=\"appSearchItemContext"+_ad+_ac+"\" "+"style=\"opacity: 0;\" data-concernrole=\""+_a5.personInContextMenu.person.concernRoleId+"\"></ul>");
_3.place(_ae,_a3,"after");
var _af=_a3.getBoundingClientRect();
_2.set(_ae,"top",_af.top+"px");
var _b0=_13.isRtlMode();
if(_b0!=null){
_2.set(_ae,"left",(_af.left-236)+"px");
}else{
_2.set(_ae,"left",(_af.left+40)+"px");
}
var _b1=Math.max(document.documentElement.clientHeight,window.innerHeight||0);
var _b2=_ae.offsetTop;
var _b3=_b1-50-_b2;
_2.set(_ae,"max-height",(_b3)+"px");
fx.fadeIn({node:_ae}).play();
_8.hitch(_a5,_a5._createPersonItemsContextMenuRows,_a5.personInContextMenu,_ae)();
var _b4=_2.get(_ae,"height");
if(_b4>=_b3){
_5.add(_ae,"scrollbars");
}
on(_ae,[_6.enter,"focus"],function(_b5){
_a5.destroyContextMenu=false;
});
on(_ae,[_6.leave,"blur","keyup"],_8.hitch(_a5,_a5._destroyPersonItemsContextMenu));
}
},_destroyPersonItemsContextMenu:function(_b6){
if(_b6.type==="keyup"){
if(_b6.keyCode!==_e.ESCAPE&&_b6.keyCode!==_e.LEFT_ARROW&&_b6.keyCode!==_e.RIGHT_ARROW){
return;
}
var _b7=_13.isRtlMode();
if(_b7&&_b6.keyCode===_e.LEFT_ARROW){
return;
}else{
if(!_b7&&_b6.keyCode===_e.RIGHT_ARROW){
return;
}
}
_b6.preventDefault();
_b6.stopPropagation();
}
var _b8=this;
var _b9=_1(".appSearchItemContext",_b8.applicationSearchDiv[0]);
if(_b9.length>0&&(!_5.contains(_b9[0],"clicked"))||_b6.type==="keyup"||_b6.type==="focus"){
if(_b6.type==="keyup"){
var _ba=null;
_1(".appSearchItem").forEach(function(_bb){
if(_bb.dataset.concernrole===_b9[0].dataset.concernrole){
_ba=_bb;
}
});
if(_ba){
_ba.focus();
}
}
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(_bc){
_5.remove(_bc,"clicked");
});
_b8.destroyContextMenu=true;
setTimeout(function(){
if(_b8.destroyContextMenu){
_b9.forEach(function(_bd){
fx.fadeOut({node:_bd,onEnd:function(){
_3.destroy(_bd);
}}).play();
});
}
},25);
}
},_createPersonItemsContextMenuRows:function(_be,_bf){
if(_be.person.items&&_be.person.items.length>0){
this._destroyPersonContextSpinningLoader(_bf);
_be.person.items.forEach(function(_c0,_c1){
_c0.rendered=false;
_c0.person=_8.clone(_be.person);
_c0.person.items=[];
_c0.first=(_c1===0);
_c0.last=(_c1===_be.person.items.length-1);
this._createResultRow(_c0,_bf,"Action_Person",false,true);
},this);
if(this.hasIos){
_bf.focus();
}
if(this.personItemsContextMenuCreationCallback){
this.personItemsContextMenuCreationCallback(_bf);
}
}else{
if(_be.person.itemsLazyLoad){
this._createPersonContextSpinningLoader(_bf);
this._loadPeopleItems([_be]).then(_8.hitch(this,function(){
this._createPersonItemsContextMenuRows(_be,_bf);
}));
}else{
this._destroyPersonContextSpinningLoader(_bf);
this._createPersonContextNoResultsMessage(_bf);
if(this.personItemsContextMenuCreationCallback){
this.personItemsContextMenuCreationCallback(_bf);
}
}
}
},_createResultRowHeader:function(_c2){
if(_c2&&_c2.length>0){
var _c3=_1(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _c4="<span class=\"appSearchItemSeparatorLabel\" id=\"resultRowHeaderId\" aria-owns=\"applicationSearchResultListId\" aria-live=\"assertive\">";
_c4+=_c2;
_c4+="</span>";
var _c5=_3.toDom(_c4);
_3.place(_c5,_c3,"first");
}
},_showHiddenResultRows:function(){
_1(".appSearchItem.hide",this.applicationSearchDiv[0]).forEach(function(_c6){
_5.remove(_c6,"hide");
});
},_hideResultList:function(){
_1(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]).forEach(function(_c7){
_2.set(_c7,"display","none");
});
_1(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(_c8){
_2.set(_c8,"display","none");
});
if(this._showingAvailableKeywordsList){
this._hideAvailableKeywordsList();
}
_1(".availableKeywordsListContainer",this.applicationSearchDiv[0]).forEach(function(_c9){
_5.add(_c9,"hide");
});
_1(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(_ca){
_2.set(_ca,"display","none");
});
_1(".moreItems",this.applicationSearchDiv[0]).forEach(function(_cb){
_2.set(_cb,"display","none");
});
_1(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(function(_cc){
_2.set(_cc,"display","none");
});
_1(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(_cd){
_5.remove(_cd,"clicked");
});
},_showResultList:function(){
var _ce=_1(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]);
_ce.forEach(function(_cf){
_2.set(_cf,"display","block");
});
if(_ce.length>0){
_5.remove(this.applicationSearchDiv[0],"application-search-items-list");
_5.add(this.applicationSearchDiv[0],"application-search-items-list");
}
_1(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(function(_d0){
_2.set(_d0,"display","inline-block");
});
_1(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(_d1){
_2.set(_d1,"display","block");
});
_1(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(_d2){
_2.set(_d2,"display","block");
});
_1(".moreItems",this.applicationSearchDiv[0]).forEach(function(_d3){
_2.set(_d3,"display","block");
});
this._updateSearchResultsScreenReadersInfo();
},_createMoreRecordsRow:function(_d4){
_1(".paginationContainer",this.applicationSearchDiv[0]).forEach(_3.destroy);
var _d5="<div role=\"button\" tabindex=\"0\" class=\"moreItems\" aria-label=\""+this.bundle.getProperty("SmartNavigator.more.results")+"\">"+this.bundle.getProperty("SmartNavigator.more.results")+"</div>";
var _d6=_3.toDom("<div class=\"paginationContainer\" role=\"application\"></div>");
var _d7=_3.toDom(_d5);
on(_d7,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(_d7,["focus"],_8.hitch(this,function(_d8){
this._destroyPersonItemsContextMenu(_d8);
}));
on(_d7,["click","keyup"],_8.hitch(this,function(_d9){
var _da=_d9.type==="keyup";
if(_da&&(_d9.keyCode!==_e.ENTER&&_d9.keyCode!==_e.SPACE)){
this._onItemKeyNavigation(_d7,_d9);
return;
}
this.paginationStart+=6;
this._renderResults(_da);
if(!_da){
this.searchInputField.focus();
}
_d9.preventDefault();
_d9.stopPropagation();
}));
_3.place(_d6,_d4,"after");
return _3.place(_d7,_d6,"first");
},_createKeywordTagsContainer:function(){
var _db=_3.toDom("<div class=\"appSearchKeywords\" role=\"application\" aria-live=\"assertive\"></div>");
var _dc=this.appBannerComboBoxDiv[0];
if(_dc){
_3.place(_db,_dc,"before");
}else{
_3.place(_db,this.searchIconDiv[0],"after");
}
var _dd=this._showingAvailableKeywordsList?"itemsDisplayed":"";
var _de=_3.toDom("<span role=\"button\" title=\""+this.bundle.getProperty("SmartNavigator.keywords.info")+"\" aria-label=\""+this.bundle.getProperty("SmartNavigator.keywords.info")+"\" tabindex=\"0\" class=\"appSearchKeywordsListButton "+_dd+"\">"+this.bundle.getProperty("SmartNavigator.keywords")+"</span>");
_3.place(_de,_db,"first");
on(_de,["click","keyup"],_8.hitch(this,this._onAvailableKeywordsListButtonClick,_db));
on(_de,"keydown",_8.hitch(this,this._onKeywordsDownArrowKey));
on(_de,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
if(!this.searchResultsScreenReadersInfo){
this.searchResultsScreenReadersInfo=_3.toDom("<span class=\"searchResultsScreenReadersInfo hidden\" id=\"searchResultsScreenReadersInfoId\"></span>");
_3.place(this.searchResultsScreenReadersInfo,_db,"last");
}
this._fixQuickSearchHeight();
},_updateSearchResultsScreenReadersInfo:function(){
if(!this.searchResultsScreenReadersInfo){
this.searchResultsScreenReadersInfo=_1(".searchResultsScreenReadersInfo",this.applicationSearchDiv[0])[0];
}
var _df=_1(".appSearchItem:not(.hide)").length;
if(this._isShowingHistory()){
this.searchResultsScreenReadersInfo.innerHTML=this.bundle.getProperty("SmartNavigator.rows.displayed.recent.search",[_df]);
}else{
this.searchResultsScreenReadersInfo.innerHTML=this.bundle.getProperty("SmartNavigator.rows.displayed",[_df]);
}
},_destroyKeywordTagsContainer:function(){
_1(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(_3.destroy);
this._hideAvailableKeywordsList();
},_createKeywordTags:function(_e0,_e1){
var _e2=[];
var _e3=false;
var _e4=_1(".appSearchKeywords",this.applicationSearchDiv[0])[0];
var _e5=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
var _e6=12;
if(_5.contains(_e5,"itemsDisplayed")){
_e6=0;
}
var _e7=240-(_e5.offsetWidth+_e6);
if(_e0.length>0){
var _e8=_3.toDom("<span class=\"appSearchKeywordsContainer\"></span>");
_3.place(_e8,_e4,"last");
_e0.forEach(_8.hitch(this,function(_e9,_ea,_eb){
var _ec=_1(".appSearchKeywordTag",this.applicationSearchDiv[0]).reduce(function(_ed,tag){
_ed+=parseInt(_2.getComputedStyle(tag).width)+22;
return _ed;
},0);
var _ee=(_ea===_eb.length-1);
var _ef="<span tabindex=\"0\" role=\"button\" aria-label=\""+this.bundle.getProperty("SmartNavigator.keyword.identified",[_e9.description])+"\" aria-owns=\"applicationSearchResultListId\" aria-live=\"assertive\" class=\"appSearchKeywordTag hidden "+(_ee?"last":"")+"\" ";
_ef+="data-terms=\""+_e9.terms+"\" ";
_ef+=">";
_ef+=_e9.description;
_ef+="</span>";
var tag=_3.toDom(_ef);
_3.place(tag,_e8,"last");
var _f0=parseInt(_2.getComputedStyle(tag).width);
if(_ec+_f0>_e7){
_e3=true;
}
_2.set(_e8,"maxWidth",_e7+"px");
_e2.push(tag);
on(tag,"click",_8.hitch(this,function(_f1){
this._destroyKeywordTag(_e1,_f1,true);
this.searchInputField.focus();
}));
on(tag,"keyup",_8.hitch(this,function(_f2){
if(_f2.keyCode===_e.ENTER||_f2.keyCode===_e.SPACE){
this._destroyKeywordTag(_e1,_f2,true);
this.searchInputField.focus();
}
this._onItemKeyNavigation(tag,_f2);
}));
}));
_e2.forEach(function(tag){
_5.remove(tag,"hidden");
});
}
},_destroyKeywordTag:function(_f3,_f4,_f5,_f6){
if(_f4.type==="keyup"&&(_f4.keyCode!==_e.ENTER&&_f4.keyCode!==_e.SPACE)){
return;
}
if(_f5){
_f4.preventDefault();
_f4.stopPropagation();
}
var _f7=_f6||_f4.target.dataset.terms;
var _f8=_f7.replace(/[.*+?^${}()|[\]\\]/g,"\\$&");
var _f9="(\\b)*("+_f8+")+(\\b)*(\\s)*";
var _fa=this.searchInputField.value;
if(_f3.length>0){
var _fb=_fa.split(_f3[0]);
var _fc=_fb[0].replace(new RegExp(_f9),"");
var end="";
if(_fb.length>0){
end=_fb.slice(1).join("").replace(new RegExp(_f9),"");
}
this.searchInputField.value=(_fc+_f3[0]+end).trim();
}else{
this.searchInputField.value=(_fa.replace(new RegExp(_f9),"")).trim();
}
if(this.searchInputField.value){
this._doQuickSearch(this.searchInputField.value);
}
},_onAvailableKeywordsListButtonClick:function(_fd,_fe,_ff){
if(_fe.type==="keyup"&&(_fe.keyCode!==_e.ENTER&&_fe.keyCode!==_e.SPACE)){
return;
}
_fe.preventDefault();
_fe.stopPropagation();
this._destroyVisibleContextMenu();
if(this._showingAvailableKeywordsList&&!_ff){
this._hideAvailableKeywordsList();
}else{
if(!this.availableKeywordsList){
var _100=this._simpleAccess.makeRequest("SmartNavigatorFacade","listSearchTargets");
this.quickSearchXHRPromises.push(_100);
_100.then(_8.hitch(this,function(data){
this.availableKeywordsList=data[0].dtls;
this._showingAvailableKeywordsList=true;
this._createAvailableKeywordsList(_fd,_fe.target);
_5.add(_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0],"itemsDisplayed");
this._voiceOverFocusOnFirstKeyword();
}),_8.hitch(this,this._ajaxErrorCallback));
}else{
this._showAvailableKeywordsList();
this._voiceOverFocusOnFirstKeyword();
}
}
},_voiceOverFocusOnFirstKeyword:function(){
if(this.hasIos){
keywordTerm=_1(".keywordTerm",this.applicationSearchDiv[0])[0];
setTimeout(function(){
keywordTerm.focus();
},400);
}
},_createAvailableKeywordsList:function(_101,_102){
if(!_101){
_101=_1(".appSearchKeywords",this.applicationSearchDiv[0])[0];
}
var _103=_3.toDom("<div class=\"availableKeywordsListContainer\"></div>");
_3.place(_103,_101,"last");
var _104=this.hasIos?"aria-hidden=true":" aria-live=\"assertive\" ";
var _105=_3.toDom("<div class=\"hidden availableKeywordsListDescription\" "+_104+" >"+this.bundle.getProperty("SmartNavigator.keywords.list.description")+"</div>");
_3.place(_105,_103,"last");
this._setKeywordsListPosition(_102,_103);
var _106=_3.toDom("<div class=\"availableKeywordsListInnerContainer\"></div>");
_3.place(_106,_103,"last");
this.availableKeywordsList.forEach(function(_107){
var _108=_3.toDom("<div class=\"availableKeyword\"></div>");
var _109=_3.toDom("<span class=\"keywordTarget\" >"+_107.description+"</span>");
var _10a=_3.toDom("<span class=\"keywordTerms\"></span>");
_107.terms.split(",").forEach(function(term,_10b,_10c){
var _10d=_3.toDom("<pre>"+term+"</pre>").innerHTML;
var _10e=(_10b===_10c.length-1)?"":", <br />";
var _10f=_3.toDom("<li class=\"keywordTerm\" role=\"button\" aria-label=\""+this.bundle.getProperty("SmartNavigator.available.keyword",[_10d,_107.description])+"\" tabindex=\"0\">"+_10d+_10e+"</li>");
on(_10f,["click","keyup"],_8.hitch(this,function(evt){
if(evt.type==="keyup"&&(evt.keyCode!==_e.ENTER&&evt.keyCode!==_e.SPACE)){
return;
}
this.searchInputField.value=this.searchInputField.value.trim()+" "+_10d;
this.searchInputField.focus();
this._hideAvailableKeywordsList();
this._doQuickSearch(this.searchInputField.value);
}));
on(_10f,"keydown",_8.hitch(this,function(_110){
this._onKeywordsDownArrowKey(_110);
this._onKeywordsUpArrowKey(_110);
}));
on(_10f,"blur",_8.hitch(this,this._hideKeywordsOnBlur));
_3.place(_10f,_10a,"last");
},this);
_3.place(_109,_108,"first");
_3.place(_10a,_108,"last");
_3.place(_108,_106,"last");
},this);
on(_103,"blur",_8.hitch(this,this._hideKeywordsOnBlur));
return _103;
},_setKeywordsListPosition:function(_111,_112){
var _113=_111.getBoundingClientRect();
_2.set(_112,"top",(_113.top+10)+"px");
var _114=_13.isRtlMode();
if(_114!=null){
_2.set(_112,"left",65+"px");
}else{
_2.set(_112,"left",(_113.left+65)+"px");
}
},_hideKeywordsOnBlur:function(_115){
if(_115.relatedTarget&&!this._isElementPartOfContainer(_115.relatedTarget,"availableKeywordsListContainer")){
var _116=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_116){
_116.focus();
}
this._hideAvailableKeywordsList();
}
},_hideAvailableKeywordsList:function(){
var _117=_1(".availableKeywordsListContainer",this.applicationSearchDiv[0])[0];
if(_117){
_5.add(_117,"hide");
}
var _118=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_118){
_5.remove(_118,"itemsDisplayed");
}
this._showingAvailableKeywordsList=false;
this._fixQuickSearchHeight();
},_showAvailableKeywordsList:function(){
var _119=_1(".availableKeywordsListContainer",this.applicationSearchDiv[0])[0];
var _11a=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(!_119){
_119=this._createAvailableKeywordsList(null,_11a);
}
this._setKeywordsListPosition(_11a,_119);
_5.remove(_119,"hide");
_5.add(_11a,"itemsDisplayed");
var _11b=_1(".availableKeywordsListDescription",this.applicationSearchDiv[0])[0];
if(_11b){
_11b.innerHTML=_11b.innerHTML;
}
this._showingAvailableKeywordsList=true;
this._fixQuickSearchHeight();
},_createMessages:function(){
var _11c="";
var _11d=null;
if(this.searchResults.messages.info){
_11c=this.searchResults.messages.info;
}else{
if(this.searchResults.messages.unmapped.pages.length>0){
_11c=this.bundle.getProperty("SmartNavigator.no.tabs.configured",[this.searchResults.messages.unmapped.pages.join()]);
}else{
if(this.searchResults.messages.moreThanOneTab.pages.length>0){
_11c=this.bundle.getProperty("SmartNavigator.more.than.one.tab",[this.searchResults.messages.moreThanOneTab.pages.join()]);
}
}
}
if(_11c.length>0){
_11d=this._createMessage(_11c,true);
}
return _11d;
},_createMessage:function(_11e,_11f,_120){
_1(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(_3.destroy);
var _121=false;
if(this.searchResults&&this.searchResults.keywords&&this.searchResults.keywords.length>0){
_121=true;
}
var _122=_11f?" info ":" error ";
var _123=_3.toDom("<div role='alert' class='appSearchMessage topMessage "+_122+"'><span>"+_11e+"</span></div>");
var _124=_1(".appSearchKeywords",this.applicationSearchDiv[0])[0];
_3.place(_123,_124,"after");
on(_123,"blur",_8.hitch(this,function(evt){
this._hideSearchBoxIfNotFocused(evt);
}));
on(_123,["keyup"],_8.hitch(this,function(_125){
this._onItemKeyNavigation(_123,_125);
}));
return _123;
},_isPageMappedToATab:function(url,page,_126){
var _127=new _d();
var _128={isMapped:true};
var _129=new curam.ui.PageRequest(url);
var _12a=_8.hitch(this,this._pageNotMappedToTabErrorCallback,_129.pageID,_128,_127,page);
var _12b=_8.hitch(this,this._pageMappedToMoreThanOneTabErrorCallback,_129.pageID,_128,_127,page);
var _12c=_8.hitch(this,this._pageMappedToTabCallback,_129.pageID,_128,_127);
if(_a.indexOf(this.pagesMappedToTab,_129.pageID)>=0){
_12c();
}else{
if(_a.indexOf(this.pagesNotMappedToTab,_129.pageID)>=0){
_12a();
}else{
if(_a.indexOf(this.pagesMappedToMultipleTabs,_129.pageID)>=0){
_12b();
}else{
var _12d=null;
if(_126){
_12d=_126.split(",").map(function(_12e){
return _12e.trim();
});
}
var _12f=!curam.ui.UIController.checkResolvePage(_129,_129.forceRefresh,_12a,_12d,_12b,false,_12c);
if(!_12f){
var _130={unmappedPageLoader:_12a,preferredTabs:_12d,moreThanOneTabMappedCallback:_12b,shouldLoadPage:false,successCallback:_12c};
curam.ui.UIController.checkPage(_129,_130);
}
}
}
}
return _127;
},_pageNotMappedToTabErrorCallback:function(_131,_132,_133,page){
_132.isMapped=false;
if(_a.indexOf(this.pagesNotMappedToTab,_131)<0){
this.pagesNotMappedToTab.push(_131);
}
if(_a.indexOf(this.searchResults.messages.unmapped.pages,page)<0){
this.searchResults.messages.unmapped.pages.push(page);
}
_133.resolve(_132);
},_pageMappedToMoreThanOneTabErrorCallback:function(_134,_135,_136,page){
_135.isMapped=false;
if(_a.indexOf(this.pagesMappedToMultipleTabs,_134)<0){
this.pagesMappedToMultipleTabs.push(_134);
}
if(_a.indexOf(this.searchResults.messages.moreThanOneTab.pages,page)<0){
this.searchResults.messages.moreThanOneTab.pages.push(page);
}
_136.resolve(_135);
},_pageMappedToTabCallback:function(_137,_138,_139){
_138.isMapped=true;
if(_a.indexOf(this.pagesMappedToTab,_137)<0){
this.pagesMappedToTab.push(_137);
}
_139.resolve(_138);
},_createPersonContextNoResultsMessage:function(_13a){
var _13b=_3.toDom("<div class='appSearchMessage info' tabIndex='0'><span>"+this.bundle.getProperty("SmartNavigator.empty.search")+"</span></div>");
_3.place(_13b,_13a,"last");
},_createPersonContextSpinningLoader:function(_13c){
var _13d=_3.toDom("<div class=\"contextMenuLoader\">"+"<div class=\"curam-spinner curam-h1 quick-search contextMenu\"></div></div>");
_3.place(_13d,_13c,"last");
},_destroyPersonContextSpinningLoader:function(_13e){
_1(".contextMenuLoader",_13e).forEach(_3.destroy);
},_destroyContextMenuIfNecessary:function(_13f){
var _140=false;
var _141=_1(".appSearchItemContext",this.applicationSearchDiv[0]);
if(_141.length>0){
if(!this._isElementPartOfContainer(_13f,"appSearchItemContext")){
if(this._isElementPartOfContainer(_13f,"application-search")){
if(_13f.tabIndex===0){
_13f.focus();
}else{
this.searchInputField.focus();
}
}
this._destroyVisibleContextMenu(_141);
_140=true;
}
}
return _140;
},_destroyVisibleContextMenu:function(_142){
if(!_142){
_142=_1(".appSearchItemContext",this.applicationSearchDiv[0]);
}
if(_142.length>0){
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(_143){
if(_143.dataset.concernrole===_142[0].dataset.concernrole){
_5.remove(_143,"clicked");
}
});
_3.destroy(_142[0]);
}
},_hideKeywordsListIfNecessary:function(_144,_145){
var _146=false;
var _147=_1(".availableKeywordsListContainer:not(.hide)",this.applicationSearchDiv[0]);
if(_147.length>0){
if(_145||!this._isElementPartOfContainer(_144,"availableKeywordsListContainer")){
this._hideAvailableKeywordsList();
_146=true;
}
if(_145){
var _148=_1(".appSearchKeywordsListButton",this.applicationSearchDiv[0])[0];
if(_148){
_148.focus();
}
}
}
return _146;
},_isElementPartOfContainer:function(_149,_14a){
if(_149.parentElement!=null){
if(_5.contains(_149,_14a)){
return true;
}else{
return this._isElementPartOfContainer(_149.parentElement,_14a);
}
}
return false;
},_onItemClick:function(_14b,item,_14c){
if(_14b.type==="keyup"&&(_14b.keyCode!==_e.ENTER&&_14b.keyCode!==_e.SPACE)){
return;
}
_14b.preventDefault();
_14b.stopPropagation();
var _14d=null;
if(item.action){
_14d=item.action.description;
}else{
if(item.person){
_14d=item.person.concernRoleName;
}
}
this._resetSearchMessages();
item.url=item.url.replace(/&#38;/g,"&");
if(item.isModal){
this._openLinkAndSaveHistory(_14b,item,_14c,true);
}else{
this._isPageMappedToATab(item.url,_14d,item.preferredTabs).then(_8.hitch(this,function(_14e){
this._openLinkAndSaveHistory(_14b,item,_14c,false,_14e);
}));
}
},_openLinkAndSaveHistory:function(_14f,item,_150,_151,_152){
_1(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_3.destroy);
var _153=this.searchResults.messages.moreThanOneTab.pages.length>0;
if(_151||(_152&&_152.isMapped)||(_153&&item.preferredTabs)){
this._hideSearchBox();
require(["curam/widget/SearchMultipleTextBoxHookPoints"],_8.hitch(this,function(_154){
if(_154.preNavigationHook){
var _155=_b.subscribe("/smartnavigator/prenavigationhook/completed",_8.hitch(this,function(){
this._openLinkAndSaveHistoryPostHook(_14f,item,_150,_151,_152);
_155.remove();
}));
var data={url:item.url};
if(item.person){
data.concernRoleId=item.person.concernRoleId;
}
_154.preNavigationHook(data);
}else{
this._openLinkAndSaveHistoryPostHook(_14f,item,_150,_151,_152);
}
}));
}else{
var _156=this._createMessages();
if(_156){
_156.focus();
}
this._fixQuickSearchHeight();
}
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_5.remove(item,"clicked");
});
},_openLinkAndSaveHistoryPostHook:function(_157,item,_158,_159,_15a){
if(_159){
tab.getTabController().handleLinkClick(item.url,{});
}else{
var _15b=null;
if(item.preferredTabs){
_15b=item.preferredTabs.split(",").map(function(_15c){
return _15c.trim();
});
}
var _15d=new curam.ui.PageRequest(item.url);
curam.ui.UIController.handlePageRequest(_15d,_15b);
}
if(!_158){
var _15e=_8.clone(item);
delete _15e.rendered;
delete _15e.first;
delete _15e.last;
if(!_15e.action){
delete _15e.action;
}
if(!_15e.person){
delete _15e.person;
}else{
delete _15e.person.items;
delete _15e.person.formattedDateOfBirth;
}
var _15f=this._simpleAccess.makeRequest("SmartNavigatorFacade","saveUserHistoryItem",_15e);
this.quickSearchXHRPromises.push(_15f);
_15f.then(function(){
},function(err){
_17.log("Error Saving Search History",err);
});
}
},_onItemKeyNavigation:function(row,_160){
if(_160.keyCode===_e.DOWN_ARROW){
this._onItemDownArrowKey(row,_160);
}else{
if(_160.keyCode===_e.UP_ARROW){
this._onItemUpArrowKey(row,_160);
}else{
if(_160.keyCode===_e.RIGHT_ARROW){
if(_13.isRtlMode()){
this._onItemLeftArrowKey(row,_160);
}else{
this._onItemRightArrowKey(row,_160);
}
}else{
if(_160.keyCode===_e.LEFT_ARROW){
if(_13.isRtlMode()){
this._onItemRightArrowKey(row,_160);
}else{
this._onItemLeftArrowKey(row,_160);
}
}
}
}
}
},_onItemDownArrowKey:function(row,_161){
_161.preventDefault();
_161.stopPropagation();
if(_5.contains(_161.target,"topMessage")){
this._setFocusOnFirstResultRow();
}else{
if(_5.contains(_161.target,"appSearchKeywordTag")||_5.contains(_161.target,"appSearchDropDownButton")){
this._setFocusOnFirstResultRow();
}else{
var _162=this._findSiblingElement(row,true,"appSearchItem-ListItem","hide");
if(_162){
this._focusAppSearchRowItem(_162);
}else{
if(!_5.contains(_161.target,"contextMenuItem")&&!_5.contains(_161.target,"appSearchMessage")){
var _163=_1(".moreItems",this.applicationSearchDiv[0]);
if(_163.length>0){
_163[0].focus();
}
}
}
}
}
},_onItemUpArrowKey:function(row,_164){
_164.preventDefault();
_164.stopPropagation();
if(_5.contains(_164.target,"appSearchKeywordTag")||_5.contains(_164.target,"appSearchDropDownButton")){
this.searchInputField.focus();
return;
}
var _165=this._findSiblingElement(row,false,"appSearchItem-ListItem","hide");
if(_165){
this._focusAppSearchRowItem(_165);
}else{
if(_5.contains(_164.target,"moreItems")){
var _166=_1(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
this._focusAppSearchRowItem(_166[_166.length-1]);
}else{
if(!_5.contains(_164.target,"contextMenuItem")){
var _167=_1(".appSearchDropDownButton",this.applicationSearchDiv[0]);
if(_167.length>0){
_167[0].focus();
}else{
var tags=_1(".appSearchKeywordTag",this.applicationSearchDiv[0]);
if(tags.length>0){
tags[0].focus();
}else{
this.searchInputField.focus();
}
}
}
}
}
},_onItemRightArrowKey:function(row,_168){
_168.preventDefault();
_168.stopPropagation();
if(_5.contains(_168.target,"appSearchKeywordTag")){
var _169=this._findSiblingElement(_168.target,true,"appSearchKeywordTag");
if(_169){
_169.focus();
}
}
var _16a=_1(".personItems",row);
if(_16a.length>0){
this.personItemsContextMenuCreationCallback=function(_16b){
var _16c=_1(".contextMenuItem",_16b);
if(_16c.length>0){
_16c[0].focus();
}else{
_1(".appSearchMessage",_16b)[0].focus();
}
this.personItemsContextMenuCreationCallback=null;
};
_16a[0].click();
}
},_onItemLeftArrowKey:function(row,_16d){
if(_5.contains(_16d.target,"appSearchKeywordTag")){
var _16e=this._findSiblingElement(_16d.target,false,"appSearchKeywordTag");
if(_16e){
_16e.focus();
}
}
},_focusAppSearchRowItem:function(row){
if(_5.contains(row,"appSearchItem")){
row.focus();
}else{
var _16f=_1(".appSearchItem",row)[0];
if(_16f){
_16f.focus();
}
}
},_findSiblingElement:function(row,_170,_171,_172){
var _173=_170?row.nextElementSibling:row.previousElementSibling;
while(_173){
if(!_5.contains(_173,_171)){
return null;
}
if(!_172||!_5.contains(_173,_172)){
return _173;
}
_173=_170?_173.nextElementSibling:_173.previousElementSibling;
}
},_setFocusOnFirstResultRow:function(){
var _174=_1(".appSearchItem:not(.hide)",this.applicationSearchDiv[0]);
if(_174.length>0){
_174[0].focus();
}
},_onPaste:function(evt){
var _175=evt.clipboardData||window.clipboardData;
var _176=_175.getData("Text");
var _177=(_176&&_176.length>0)||this._isSearchInputFieldPopulated();
this._enableOrDisableSearchLink(evt,_177);
},_givenFocus:function(){
var _178=this;
var ua=navigator.userAgent.toLowerCase();
var _179=/trident/.test(ua)?true:false;
if(_1(".appSearchItemContext",this.applicationSearchDiv[0]).length>0){
return;
}
if(!this.spinnerDiv){
this._createSpinnerDiv();
}
if(this.searchTextDiv==null){
this.searchTextDiv=_1(".search-input-controls div.dijitInputField",this.applicationSearchDiv[0]);
}
if(this.originalInputColor==null){
this.originalInputColor="#152935";
}
if(this.appBannerComboBoxDiv==null||this.appBannerComboBoxDiv.length==0){
this.appBannerComboBoxDiv=_1(".appBannerComboBox",this.applicationSearchDiv[0]);
if(this.appBannerComboBoxDiv==null||this.appBannerComboBoxDiv.length==0){
this.appBannerComboBoxDiv=_1(".multiple-search-banner select",this.applicationSearchDiv[0]);
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_5.add(this.appBannerComboBoxDiv[0],"appBannerSelect");
}
}
}
_2.set(this.searchInputField,"color",this.originalInputColor);
_5.remove(this.searchInputField,"input-placeholder-closed");
_5.add(this.searchInputField,"input-placeholder-opened");
var _17a=0;
var _17b=_13.isRtlMode();
if(_17b!=null){
_17a=_4.position(this.searchInputField).x-46;
}else{
_17a=_4.position(this.searchInputField).x-10;
}
if(this.closedHeight==null){
this.closedHeight=_2.get(this.applicationSearchDiv[0],"height");
}
this.applicationSearchDiv.style({left:_17a+"px"});
_5.add(this.applicationSearchDiv[0],"application-search-upfront-popup");
if(this.quickSearch){
if(_1(".appSearchKeywords",this.applicationSearchDiv[0]).length===0){
this._createKeywordTagsContainer();
}
}else{
this._destroyKeywordTagsContainer();
}
var _17c=this.quickSearch?40:100;
if(_2.get(this.applicationSearchDiv[0],"height")!=_17c){
if(this.searchOptionsDiv.length>0){
_2.set(this.searchOptionsDiv[0],"display","block");
_2.set(this.searchOptionsDiv[0],"opacity","0");
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_2.set(this.appBannerComboBoxDiv[0],"display","block");
}
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
var _17d={node:this.searchOptionsDiv[0]};
fx.fadeIn(_17d).play();
}
}
if(this.backgroundColor==null){
this.backgroundColor=_2.get(this.searchTextDiv[0],"background-color");
}
if(this.searchTextDiv!=null&&_2.get(this.searchTextDiv[0],"background-color")!=this.searchOptionsDivOpenedColor){
_2.set(this.searchTextDiv[0],"background-color","#ffffff");
_2.set(this.searchControlsDiv[0],"background-color","#ffffff");
_2.set(this.searchIconDiv[0],"background-color","#ffffff");
this.searchInputImg[0].src=jsBaseURL+"/themes/curam/images/search--20-enabled.svg";
}
if(this.quickSearch){
if(!this.focusNode.value&&!this._isShowingHistory()){
this._destroyResultList();
this._hideDropDown();
this._showSearchHistory();
}
var _17e=_1(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0])[0];
if(_17e){
if(_179){
_7.set(this.searchInputField,"aria-describedby","applicationSearchResultListId");
}else{
_7.set(this.searchInputField,"aria-describedby","resultRowHeaderId");
}
}
this._showResultList();
}
this._fixQuickSearchHeight();
this.searchInputField.focus();
setTimeout(function(){
_178.searchInputField.select();
},5);
var _17f=_1(".appSearchItemsContainer",this.applicationSearchDiv[0])[0];
if(typeof (this.searchInputField)!="undefined"&&this.searchInputField.value.length>0&&typeof (_17f)!="undefined"&&_17f.children.length>0){
if(_179){
_7.set(this.searchInputField,"aria-describedby","applicationSearchResultListId");
}else{
_7.set(this.searchInputField,"aria-describedby","searchResultsScreenReadersInfoId");
}
}
},_onBlur:function(evt){
if(!this._isSearchInputFieldPopulated()){
this.searchInputField.value="";
}
this._hideSearchBoxIfNotFocused(evt);
},_destroyResultList:function(){
_1(".appSearchItemsOuterContainer",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchItemsContainer",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchItem",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchItemSeparatorLabel",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchKeywordTag",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".appSearchItemContext",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".moreItems",this.applicationSearchDiv[0]).forEach(_3.destroy);
_1(".personItems.clicked",this.applicationSearchDiv[0]).forEach(function(item){
_5.remove(item,"clicked");
});
_5.remove(this.applicationSearchDiv[0],"application-search-items-list");
this._fixQuickSearchHeight();
_5.remove(this.applicationSearchDiv[0],"shadow");
},_autoScrollToNewItems:function(){
if(this.searchResults.hasItems&&this.paginationStart>0){
var _180=_1(".appSearchItemsOuterContainer",this.applicationSearchDiv[0])[0];
_180.scrollTop=_180.scrollHeight;
_1(".appSearchNewItem",this.applicationSearchDiv[0]).forEach(function(item){
fx.animateProperty({node:item,duration:650,properties:{opacity:{start:".4",end:"1"}}}).play();
_5.remove(item,"appSearchNewItem");
});
}
},_getAge:function(dob){
if(dob){
var _181=_f.parseDate(dob);
return parseInt(((Date.now()-_181)/(31557600000)));
}else{
return "";
}
},_fixQuickSearchHeight:function(){
this.currentOpenedHeight=this._getOpenedMenuHeight();
_2.set(this.applicationSearchDiv[0],"height",this.currentOpenedHeight+"px");
},_hideDropDown:function(){
this._fixQuickSearchHeight();
_5.remove(this.applicationSearchDiv[0],"shadow");
},_isShowingHistory:function(){
return _1(".appSearchItemHistory",this.applicationSearchDiv[0]).length>0;
},_resetSearchResults:function(){
this.paginationStart=0;
this._destroyResultList();
this.searchResults={people:[],actionsOnly:[],personQueryTerms:[],messages:{info:null,unmapped:{pages:[]},moreThanOneTab:{pages:[]}},hasItems:false};
},_resetSearchMessages:function(){
this.searchResults.messages={info:null,unmapped:{pages:[]},moreThanOneTab:{pages:[]}};
},_enableOrDisableSearchLink:function(evt,_182){
if(_182&&_182===true){
_5.remove(this.searchIcon[0],"dijitDisabled");
if(evt.keyCode==_e.ENTER){
this._search();
}
}else{
_5.add(this.searchIcon[0],"dijitDisabled");
}
},_isQuickSearchOnly:function(){
var _183=false;
var _184=false;
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
_183=true;
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_184=true;
}
return !_183&&!_184;
},_getItemsHeight:function(_185){
var _186=this._isQuickSearchOnly();
var _187=".appSearchItem:not(.hide):not(.contextMenuItem)";
if(_185){
_187=".appSearchItem";
}
var _188=_1(_187,this.applicationSearchDiv[0]).reduce(function(_189,node){
_189+=_2.get(node,"height");
_189+=_2.get(node,"padding-top");
_189+=_2.get(node,"padding-bottom");
_189+=_2.get(node,"margin-top");
_189+=_2.get(node,"margin-bottom");
return _189;
},0);
if(_186&&_188>0){
_188+=3;
}
if(_188>this.maxItemsHeight){
_188=this.maxItemsHeight;
}
return _188;
},_getOpenedMenuHeight:function(){
var _18a=0;
if(this.searchOptionsDiv!=null&&this.searchOptionsDiv.length>0){
if(this.quickSearch){
_18a+=this.appBannerComboBoxDiv[0]?this.menuOpenedFurtherOptionsHeightQuickSearch:this.menuOpenedFurtherOptionsHeightNoCombobox;
}else{
_18a+=this.menuOpenedFurtherOptionsHeight;
}
}
if(this.appBannerComboBoxDiv!=null&&this.appBannerComboBoxDiv.length>0){
_18a+=this.menuOpenedComboboxHeight;
}else{
if(this.quickSearch){
_18a+=this.quickSearchMenuOpenedHeight;
}
}
var _18b=0;
_1(".appSearchKeywords",this.applicationSearchDiv[0]).forEach(function(_18c){
_18b+=this._calculateElementHeight(_18c);
},this);
_18b+=this._getTopMessagesHeight();
_18b+=_1(".moreItems",this.applicationSearchDiv[0]).reduce(function(_18d){
return _18d+=40;
},0);
if(_1(".appSearchItemsOuterContainer.margin",this.applicationSearchDiv[0]).length>0){
_18a+=10;
}
if(_1(".appSearchMessage.topMessage.margin",this.applicationSearchDiv[0]).length>0){
_18a+=10;
}
var _18e=this._getItemsHeight(false);
_18a+=_18e+_18b;
var _18f=27;
var _190=_18a===_18f+this.quickSearchMenuOpenedHeight;
if(_190){
_18a-=5;
}
_2.set(this.applicationSearchDiv[0],"height",_18a+"px");
this.currentOpenedHeight=_18a;
return _18a;
},_getTopMessagesHeight:function(){
var _191=0;
_1(".appSearchMessage.topMessage",this.applicationSearchDiv[0]).forEach(function(_192){
_191+=_192.clientHeight+5;
});
return _191;
},_calculateElementHeight:function(_193){
var _194=window.getComputedStyle(_193);
var _195=parseFloat(_194["marginTop"])+parseFloat(_194["marginBottom"]);
return Math.ceil(_193.offsetHeight+_195);
},_enableQuickSearch:function(){
_5.add(this.applicationSearchDiv[0],"quick-search");
this.quickSearch=true;
if(_1(".appSearchKeywords",this.applicationSearchDiv[0]).length===0){
this._createKeywordTagsContainer();
}
},_disableQuickSearch:function(){
this._destroyResultList();
_5.remove(this.applicationSearchDiv[0],"quick-search");
this.quickSearch=false;
this._destroyKeywordTagsContainer();
},_createSpinnerDiv:function(){
this.spinnerDiv=_3.toDom("<div class=\"curam-spinner curam-h1 quick-search hide\"></div>");
_3.place(this.spinnerDiv,this.searchIconDiv[0],"last");
},_showSpinnerDiv:function(){
this.spinnerTimeouts.push(setTimeout(_8.hitch(this,function(){
_2.set(this.searchIcon[0],"display","none");
_5.remove(this.spinnerDiv,"hide");
}),500));
},_hideSpinnerDiv:function(){
this.spinnerTimeouts.forEach(function(_196){
clearTimeout(_196);
});
this.spinnerTimeouts=[];
_2.set(this.searchIcon[0],"display","block");
if(this.spinnerDiv){
_5.add(this.spinnerDiv,"hide");
}
},_cancelPreviousAjaxCalls:function(){
if(this.quickSearchXHRPromises.length>0){
this.quickSearchXHRPromises.forEach(function(_197){
_197.cancel();
});
this.quickSearchXHRPromises=[];
}
}});
return _1a;
});
