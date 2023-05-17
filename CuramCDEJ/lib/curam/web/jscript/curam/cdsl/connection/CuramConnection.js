//>>built
define("curam/cdsl/connection/CuramConnection",["dojo/_base/declare","dojo/request/registry","curam/cdsl/_base/_Connection","curam/util"],function(_1,_2,_3,_4){
var _5=_1(_3,{_baseUrl:null,constructor:function(_6){
this._baseUrl=_6;
},invoke:function(_7,_8){
this.inherited(arguments);
var _9=_4.getTopmostWindow();
return _2(_7.url(this._baseUrl),{data:_7.toJson(),method:"POST",headers:{"Content-Encoding":"UTF-8","csrfToken":_9.csrfToken},query:null,preventCache:true,timeout:_8?_8:this._DEFAULT_REQUEST_TIMEOUT,handleAs:"text"});
}});
return _5;
});
