//>>built
define("curam/codetable-hierarchy",["curam/util/Request","curam/debug","dojo/data/ItemFileReadStore","curam/widget/FilteringSelect"],function(_1,_2){
var _3={initLists:function(_4,_5,_6){
this.noOptionCode=_4;
this.noOptionDesc=_5;
this.ddInfo=_6;
this.lists=function(){
var _7=null;
for(var i=_6.length-1;i>=0;i--){
_7=new _3.DropDown(dijit.byId(_6[i].id),_6[i].ctName,_4,_5,_7);
}
};
dojo.addOnLoad(this.lists);
},DropDown:function(_8,_9,_a,_b,_c){
this.node=_8.domNode;
this.widgetNode=_8;
this.codeTableName=_9;
this.noOptionCode=_a;
this.noOptionDesc=_b;
this.next=_c;
var _d=this;
this.populate=function(){
if(!_d.widgetNode.get("value")){
_d.resetNext(_d);
}else{
if(_d.next!=null){
_d.resetNext(_d);
if(_d.widgetNode.get("value")==0){
return;
}
_1.post({url:"../servlet/JSONServlet",handleAs:"text",preventCache:true,load:function(_e,_f){
if(_e.length<3){
curam.debug.log(_2.getProperty("curam.codetable-hierarchy.msg.1")+_d.codeTableName+_2.getProperty("curam.codetable-hierarchy.msg.2")+_d.widgetNode.get("value"));
return;
}
var _10=dojo.fromJson(_e);
_10.unshift({"value":_d.noOptionCode,"name":""});
var _11=dijit.byId(_d.next.widgetNode.id);
var _12=new dojo.data.ItemFileReadStore({data:{label:"name",identifier:"value",items:_10}});
_12.fetch({onComplete:function(_13,_14){
_11.set("store",_12);
_11.set("value",_d.noOptionCode);
}});
},error:function(_15){
_2.log(_15);
},content:{"content":dojo.toJson({operation:"getCodeTableSubsetForFilteringSelect",args:[_d.codeTableName,_d.widgetNode.get("value")]})}});
}
}
};
this.resetNext=function(_16){
while(_16.next!=null){
var _17=[];
_17.unshift({"value":_16.noOptionCode,"name":_16.noOptionDesc});
var _18=dijit.byId(_16.next.widgetNode.id);
var _19=new dojo.data.ItemFileReadStore({data:{label:"name",identifier:"value",items:_17}});
_19.fetch({onComplete:function(_1a,_1b){
_18.set("store",_19);
_18.set("displayedValue",_16.noOptionDesc);
}});
_16=_16.next;
}
};
if(_c!=null){
dojo.connect(this.widgetNode,"onChange",this.populate);
}
}};
dojo.global.CodeTableHierarchy=_3;
return _3;
});
