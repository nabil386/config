//>>built
define("curam/cdsl/_base/PreferenceMap",["dojo/_base/declare","dojo/_base/lang"],function(_1,_2){
var _3=_1(null,{_preferences:null,_preferenceNames:null,constructor:function(){
this._preferences={};
this._preferenceNames=[];
},getPreference:function(_4){
return this._preferences[_4];
},getPreferenceNames:function(){
return this._preferenceNames;
},addPreference:function(_5,_6){
this._preferences[_5]=_6;
this._preferenceNames[this._preferenceNames.length]=_5;
}});
return _3;
});
