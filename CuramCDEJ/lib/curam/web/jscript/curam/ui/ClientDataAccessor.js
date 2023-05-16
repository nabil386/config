//>>built
define("curam/ui/ClientDataAccessor",["dojo/_base/declare","curam/inspection/Layer","curam/util/Request","curam/debug"],function(_1,_2,_3,_4){
return _1("curam.ui.ClientDataAccessor",null,{constructor:function(){
_2.register("curam/ui/ClientDataAccessor",this);
},get:function(_5,_6,_7,_8){
var _9=jsBaseURL+"/servlet/PathResolver";
var _a=_9+"?p="+_5;
if(_7==undefined){
_7=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_8==undefined){
_8=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_3.post({url:_a,headers:{"Content-Encoding":"UTF-8"},handleAs:"text",preventCache:true,load:_6,error:_7,handle:_8});
},getList:function(_b,_c,_d,_e){
var _f="servlet/PathResolver"+"?r=l&p="+_b;
if(_d==undefined){
_d=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_e==undefined){
_e=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_3.post({url:_f,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_c,error:_d,handle:_e});
},getRaw:function(_10,_11,_12,_13){
var _14="servlet/PathResolver"+"?r=j&p="+_10;
if(_12==undefined){
_12=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_13==undefined){
_13=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
_3.post({url:_14,headers:{"Content-Encoding":"UTF-8"},handleAs:"json",preventCache:true,load:_11,error:_12,handle:_13});
},set:function(_15,_16,_17,_18,_19){
var _1a=jsBaseURL+"/servlet/PathResolver";
var _1b=_1a+"?r=x&p="+_15+"&v="+encodeURIComponent(_16);
var _1c=curam.util.getTopmostWindow();
if(_18==undefined||_18==null){
_18=dojo.hitch(this,this.handleClientDataAccessorError);
}
if(_19==undefined||_19==null){
_19=dojo.hitch(this,this.handleClientDataAccessorCallback);
}
if(_17==undefined||_17==null){
_17=dojo.hitch(this,this.handleClientDataAccessorSuccess);
}
_3.post({url:_1b,headers:{"Content-Encoding":"UTF-8","csrfToken":_1c.csrfToken},handleAs:"text",preventCache:true,load:_17,error:_18,handle:_19});
},handleClientDataAccessorError:function(_1d,_1e){
var _1f=_4.getProperty("curam.ui.ClientDataAccessor.err.1")+"PathResolverServlet : ";
var _20=_4.getProperty("curam.ui.ClientDataAccessor.err.2");
_4.log(_1f+_1d+_20+_1e);
},handleClientDataAccessorSuccess:function(_21,_22){
curam.debug.log("curam.ui.ClientDataAccessor.handleClientDataAccessorSuccess : "+_21);
},handleClientDataAccessorCallback:function(_23,_24){
_4.log("curam.ui.ClientDataAccessor.handleClientDataAccessorCallback :"+" "+_4.getProperty("curam.ui.ClientDataAccessor.callback"));
}});
});
