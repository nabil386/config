//>>built
define("curam/util/ui/refresh/RefreshEvent",["dojo/_base/declare"],function(_1){
var _2=_1("curam.util.ui.refresh.RefreshEvent",null,{TYPE_ONLOAD:"onload",TYPE_ONSUBMIT:"onsubmit",SOURCE_CONTEXT_MAIN:"main-content",SOURCE_CONTEXT_DIALOG:"dialog",SOURCE_CONTEXT_INLINE:"inline",_type:null,_context:null,constructor:function(_3,_4){
if(!_3||!_4){
throw "Required parameters missing.";
}
if(!(_3==this.TYPE_ONLOAD||_3==this.TYPE_ONSUBMIT)){
throw "Unknown type: "+_3;
}
if(!(_4==this.SOURCE_CONTEXT_DIALOG||_4==this.SOURCE_CONTEXT_INLINE||_4==this.SOURCE_CONTEXT_MAIN)){
throw "Unknown context: "+_4;
}
this._type=_3;
this._context=_4;
},equals:function(_5){
if(typeof _5!="object"){
return false;
}
if(_5.declaredClass!=this.declaredClass){
return false;
}
return this._type===_5._type&&this._context===_5._context;
}});
return _2;
});
