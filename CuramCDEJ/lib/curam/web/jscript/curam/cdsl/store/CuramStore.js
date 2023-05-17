//>>built
define("curam/cdsl/store/CuramStore",["dojo/_base/declare","curam/cdsl/request/CuramService","curam/cdsl/_base/FacadeMethodCall","curam/cdsl/Struct","curam/cdsl/store/IdentityApi","dojo/store/util/QueryResults","dojo/_base/lang","curam/cdsl/_base/_Connection"],function(_1,_2,_3,_4,_5,_6,_7){
var _8={query:"listItems",get:"read",put:"modify",add:"insert",remove:"remove"};
var _9={identityApi:new _5(),dataAdapter:null};
var _a=function(_b){
var o=_7.clone(_9);
if(_b&&_b.getIdentity&&_b.parseIdentity&&_b.getIdentityPropertyNames){
o.identityApi=_b;
}else{
o=_7.mixin(o,_b);
}
return o;
};
var _c=null;
var _d=_1(_c,{_service:null,_baseFacadeName:null,_identityApi:null,constructor:function(_e,_f,_10){
var _11=_a(_10);
this._service=new _2(_e,{dataAdapter:_11.dataAdapter});
this._baseFacadeName=_f;
this._identityApi=_11.identityApi;
},get:function(_12){
var _13=new _4(this._identityApi.parseIdentity(_12));
var _14=new _3(this._baseFacadeName,_8.get,[_13]);
return this._service.call([_14]).then(function(_15){
return _15[0];
});
},getIdentity:function(_16){
return this._identityApi.getIdentity(_16);
},query:function(_17,_18){
var _19=new _3(this._baseFacadeName,_8.query,[new _4(_17)]);
if(_18){
_19._setMetadata({queryOptions:{offset:_18.start,count:_18.count,sort:_18.sort}});
}
var _1a=this._service.call([_19]).then(function(_1b){
return _1b[0].dtls;
});
return new _6(_1a);
},put:function(_1c,_1d){
if(_1d&&typeof _1d.overwrite!=="undefined"&&!_1d.overwrite){
throw new Error("The overwrite option is set to false, but adding new items "+"via CuramStore.put() is not supported.");
}
return this._addOrPut(_8.put,_1c,_1d,"putOptions");
},add:function(_1e,_1f){
var _20={};
if(_1f){
_20=_7.mixin(_20,_1f);
}
_20.overwrite=false;
return this._addOrPut(_8.add,_1e,_20,"addOptions");
},_addOrPut:function(_21,_22,_23,_24){
var _25=_22;
if(!_25.isInstanceOf||!_25.isInstanceOf(_4)){
_25=new _4(_22);
}
var _26=new _3(this._baseFacadeName,_21,[_25]);
if(_23){
var _27={};
_27[_24]={id:_23.id?_23.id:null,before:_23.before?this.getIdentity(_23.before):null,parent:_23.parent?this.getIdentity(_23.parent):null,overwrite:false};
_26._setMetadata(_27);
}
return this._service.call([_26]).then(_7.hitch(this,function(_28){
if(_23&&_23.id){
return _23.id;
}
if(_23&&_23.overwrite){
return this.getIdentity(_25);
}
return this.getIdentity(_28[0]);
}));
},remove:function(_29){
var _2a=new _4(this._identityApi.parseIdentity(_29)),_2b=new _3(this._baseFacadeName,_8.remove,[_2a]);
return this._service.call([_2b]).then(function(_2c){
return _29;
});
}});
return _d;
});
