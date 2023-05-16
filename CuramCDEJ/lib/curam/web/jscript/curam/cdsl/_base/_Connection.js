//>>built
define("curam/cdsl/_base/_Connection",["dojo/_base/declare","./MetadataRegistry","./PreferenceMap"],function(_1,_2,_3){
var _4=_1(null,{_DEFAULT_REQUEST_TIMEOUT:60000,_metadata:null,_preferences:null,constructor:function(){
this._metadata=new _2();
this._preferences=new _3();
},invoke:function(_5,_6){
this._metadata.setFlags(_5);
},updateMetadata:function(_7){
this._metadata.update(_7);
},metadata:function(){
return this._metadata;
},preferences:function(){
return this._preferences;
}});
return _4;
});
