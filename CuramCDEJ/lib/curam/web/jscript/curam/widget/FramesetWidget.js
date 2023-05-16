//>>built
require({cache:{"url:curam/widget/resources/FramesetWidget.html":"<div data-dojo-attach-point=\"frameset\" class=\"orgFrameset\">\n  <div data-dojo-attach-point=\"layouter\" class=\"orgBorder\"\n        data-dojo-type=\"dijit.layout.BorderContainer\"\n        liveSplitters=\"true\">\n    <div data-dojo-attach-point=\"navFrame\"\n        class=\"orgNavFrame\" data-dojo-type=\"dijit.layout.ContentPane\"\n        splitter=\"true\" region=\"leading\"></div>\n    <div data-dojo-attach-point=\"contentFrame\" class=\"orgContentPanel\"\n          region=\"center\" data-dojo-type=\"dijit.layout.ContentPane\">\n      <div data-dojo-attach-point=\"controller\" data-dojo-type=\"curam.util.UIMLazyController\"\n            uid=\"${uid}\" url=\"../loading.jspx\" iframeId=\"iframe-${uid}\"\n            classList=\"lazyTree\" iframeClassList=\"orgContentFrame\"\n            iscpiframe=\"true\">\n        <span style=\"display:none\"></span>\n      </div>\n    </div>\n  </div>\n</div>\n","url:curam/widget/resources/UIMLazyController.html":"<div id=\"uimcontroller_${uid}\" class=\"uimcontroller_${uid} uimController ${classList}\"\n      data-dojo-attach-point=\"uimController\">\n  <div style=\"display:none;\" id=\"uimcontroller_tc_${uid}\"\n        class=\"ipnTabController in-page-nav-tabContainer\" \n        data-dojo-attach-point=\"tabController\" \n        data-dojo-type=\"dijit.layout.TabContainer\">\n  </div>\n  <div class=\"contentPanelFrameWrapper\" data-dojo-attach-point=\"frameWrapper\">\n    <iframe frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" \n            width=\"100%\" allowTransparency=\"true\" \n            id=\"${iframeId}\" data-dojo-attach-point=\"frame\" \n            class=\"${iframeId} ${iframeClassList}\"></iframe>\n  </div>\n</div>\n"}});
define("curam/widget/FramesetWidget",["dijit/_Widget","dijit/_Templated","dijit/tree/ForestStoreModel","dijit/Tree","dojo/dom-style","dojox/data/JsonRestStore","curam/UIMController","dojo/dom","dojo/_base/declare","dojo/text!curam/widget/resources/FramesetWidget.html","dojo/text!curam/widget/resources/UIMLazyController.html","curam/util/Request","curam/debug","dojo/Deferred","dojo/_base/lang","dijit/layout/BorderContainer","dijit/layout/ContentPane","dijit/layout/TabContainer"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f){
var _10=_9("curam.widget.FramesetWidget",[_1,_2],{widgetsInTemplate:true,configId:null,rootLabel:"OrgTree",previousSelected:null,nextSelected:null,mode:"",rootItem:null,relativeStart:null,initialParams:null,currItem:null,parentItem:null,rpuLink:"",uid:"",templateString:_a,postCreate:function(){
this.inherited(arguments);
var _11=this;
var _12={url:"../servlet/JSONServlet?o3c=ORG_TREE_QUERY",handleAs:"json",preventCache:true,sync:true,content:{"configId":this.configId},load:function(_13){
_11.configData=_13;
},error:function(err){
console.error("Error when getting config!",err);
}};
_c.get(_12);
var _14=new dojox.rpc.Rest("../servlet/",true,null,function(id,_15){
var _16={url:"../servlet/"+(id==null?"":id),handleAs:"json",contentType:"application/json",sync:true,preventCache:true,headers:{Accept:"application/json,application/javascript"}};
if(_15&&(_15.start>=0||_15.count>=0)){
_16.headers.Range="items="+(_15.start||"0")+"-"+((_15.count&&_15.count!=Infinity&&(_15.count+(_15.start||0)-1))||"");
}
return _16;
});
this.lazyStore=new curam.util.orgTreeStore({target:"../servlet/",service:_14,labelAttribute:"name",configId:this.configId,initialParams:this.initialParams,configData:this.configData});
this.lazyForestModel=new curam.util.LazyForestStoreModel({store:this.lazyStore,rootId:"orgTreeRoot",rootLabel:this.rootLabel,deferItemLoadingUntilExpand:true});
this.treeWidget=new curam.util.OrgTree({model:this.lazyForestModel,configData:this.configData,controllerRef:this.controller});
this.navFrame.domNode.appendChild(this.treeWidget.domNode);
},addClass:function(css){
var _17=document.getElementsByTagName("style")[0];
var _18="";
if(!_17){
_17=document.createElement("style");
_17.type="text/css";
document.getElementsByTagName("head")[0].appendChild(_17);
}else{
_18=_17.innerHTML;
}
_17.innerHTML=_18+css;
},startup:function(){
this.inherited(arguments);
this.treeWidget.startup();
this.treeWidget.set("textDir",this.get("textDir"));
this.addClass(".soria .dijitTreeRowSelected .dijitTreeLabel "+"[dir=rtl]{background-color: transparent;}");
this.addClass(".soria .orgNavFrame .dijitTreeNodeHover,"+".soria .orgNavFrame .dijitTreeRow,"+".soria .orgNavFrame .dijitTreeRowSelected .dijitTreeLabel,"+".soria .orgNavFrame .dijitTreeLabelFocused,"+".soria .orgNavFrame .dijitTreeExpandoLeaf  "+"[dir=rtl] {background-color: transparent;}");
jsScreenContext.addContextBits("ORG_TREE");
window.rootObject="orgTreeRoot";
this.contentFrame=_8.byId("iframe-"+this.uid);
if(this.contentFrame){
dojo.subscribe("orgTree.refreshContent",this,function(_19){
this.controller.setURL(_19);
this.controller.loadPage();
var _1a=this.treeWidget.currentSelection.node||this.treeWidget.selectedNode;
this.currItem=this.treeWidget.currentSelection.item;
this.previousSelected=this.currItem;
var _1b=_1a.getParent();
this.parentItem=_1b.item;
var _1c=this;
var _1d=function(_1e){
if(_1e&&(_1e.id||_1e.$ref)){
_1c.updateItems(_1e);
}else{
_1c.lazyStore.deleteItem(_1c.currItem);
_1c.nextSelected=_1b;
}
};
if(this.lazyForestModel.mayHaveChildren(this.currItem)){
if(this.currItem.children=="true"){
if(this.parentItem.loader){
this.mode="updateChildren";
}else{
this.mode="updateMyself";
this.parentItem=this.rootItem;
}
this.reloading(this.parentItem);
}else{
if(this.currItem.loader){
this.mode="updateMyself";
this.reloading(this.currItem,_1d);
}else{
if(this.parentItem.loader){
this.mode="deepUpdate";
}else{
this.mode="updateMyself";
this.parentItem=this.rootItem;
}
this.reloading(this.parentItem);
}
}
}else{
if(this.parentItem.loader){
this.mode="updateChildren";
this.reloading(this.parentItem);
}
}
this.selectNextNode();
});
this.treeWidget.contentFrameRef=this.contentFrame;
var _1f=function(_20){
this.lazyStore.treeId=_20[0].id;
this.rootItem=_20[0];
this.nextSelected=_20[0].selectedNode;
this.relativeStart=_20[0];
this.selectNextNode();
};
this.lazyStore.fetch({start:0,count:1,onComplete:dojo.hitch(this,_1f)});
}
},updateItems:function(_21){
var _22=_21.children;
if(_21.selectedNode){
this.nextSelected=_21.selectedNode;
this.relativeStart=_21;
}
var _23=(this.mode=="updateMyself")?this.currItem:this.parentItem;
var _24=this.lazyStore.getValue(_23,"children");
this.updateAction(_21,_22,_24,_23);
},updateAction:function(_25,_26,_27,_28){
var _29=(_26.length-_27.length);
if(_29!=0){
var _2a=this.diffItems(_27,_26,_29);
if(_2a){
if(_29>0){
if(this.mode=="deepUpdate"){
console.error("Unexpected addition of sibling, ignoring!");
}else{
var _2b=this.lazyStore.newItem(_2a,{parent:_28,attribute:"children"});
if(!this.nextSelected){
this.nextSelected=this.treeWidget.getNodesByItem(_2b)[0];
}
}
}else{
if(this.mode=="updateMyself"){
_d.log(_d.getProperty("curam.widget.FramesetWidget.warning"));
}else{
this.lazyStore.deleteItem(_2a);
if(!this.nextSelected){
this.nextSelected=this.parentItem;
}
}
}
}else{
curam.debug.log(_d.getProperty("curam.widget.FramesetWidget.reloading"));
this.mode="updateMyself";
this.reloading(this.rootItem);
}
}else{
this.editHandler(_25,_27,_26);
}
},editHandler:function(_2c,_2d,_2e){
var _2f=false;
var _30=null;
if(this.mode=="updateMyself"){
if(this.currItem.name!=_2c.name){
this.lazyStore.setValue(this.currItem,"name",_2c.name);
this.nextSelected=null;
_2f=true;
}
}
if(!_2f){
for(var _31 in _2d){
if(!isNaN(_31)){
var _32=(this.mode=="children")?this.currItem:_2d[_31];
var _33=_32.id||_32.$ref;
for(var _34 in _2e){
if((_2e[_34].id||_2e[_34].$ref)==_33){
_30=_2e[_34];
}
}
if(!_30){
throw new Error("The identifiers of the items have changed!"+"Out of synch!");
}
if(_30.name!=_32.name){
this.lazyStore.setValue(_32,"name",_30.name);
this.nextSelected=_32;
_2f=true;
break;
}
}
}
}
if(!_2f&&(this.mode!="updateChildren")){
var _35=(this.mode!="updateMyself")?_2d:this.lazyStore.getValue(this.currItem,"children");
var _36=_2c.selectedNode;
if(_36&&_36.indexOf("/")>-1){
this.relativeStart=this.currItem;
this.nextSelected=_36;
this.selectNextNode();
}else{
for(var _37 in _35){
if(!isNaN(_37)){
var _38=_35[_37];
if(!this.nextSelected){
this.parentItem=this.lazyStore.getParent(_38);
this.currItem=_38;
this.mode="updateMyself";
if(_38.loader){
this.reloading(_38);
}else{
if(this.lazyForestModel.mayHaveChildren(_38)){
this.updateItems(_38);
}
}
}
}
}
}
}
},reloading:function(_39,_3a){
if(_39.loader){
var _3b={url:"../servlet/JSONServlet?o3c=ORG_TREE_QUERY",handleAs:"json",preventCache:true,sync:true,content:{"loader":_39.loader,"parentId":_39.id,"treeId":this.lazyStore.treeId}};
var _3c=function(err){
console.error("Error when reloading children!",err);
};
var _3d=_c.get(_3b);
_3d.addCallback(dojo.hitch(this,_3a||this.updateItems));
_3d.addErrback(_3c);
}else{
throw new Error("Reload impossible, item "+(_39.id||_39.$ref)+" did not have a loader!");
}
},diffItems:function(_3e,_3f,_40){
lessItems=(_40>0)?_3e:_3f;
moreItems=(_40>0)?_3f:_3e;
for(var _41 in moreItems){
if(!isNaN(_41)){
var _42=moreItems[_41];
for(var _43 in lessItems){
if(!isNaN(_43)){
if((_42.$ref||_42.id)==(lessItems[_43].$ref||lessItems[_43].id)){
_42=null;
break;
}
}
}
if(_42!=null){
return _42;
}
}
}
return null;
},selectNextNode:function(){
var _44=null;
if(!this.nextSelected){
return;
}
if(this.nextSelected instanceof dijit._TreeNode){
_44=this.nextSelected;
}else{
var _45=this.nextSelected;
if((typeof _45==="string")&&_45.indexOf("/")>-1){
var _46=_45.split("/");
for(idx in _46){
var _47=this.lazyStore.getItem(_46[idx]);
if(!_47){
if(idx<_46.length-1){
_45=this.relativeStart;
break;
}else{
_45=this.relativeStart;
if(_46[parseInt(idx)-1]){
var _48=this.lazyStore.getItem(_46[parseInt(idx)-1]);
var _49=this.lazyStore.getValue(_48,"children");
this.reloading(_48,dojo.hitch(this,function(_4a){
var _4b=this.diffItems(_49,_4a.children,1);
if(_4b&&(_4b.id||_4b.$ref)==_46[idx]){
var _4c=this.lazyStore.newItem(_4b,{parent:_48,attribute:"children"});
_45=_4c;
}
}));
}
}
}else{
_45=_46[idx];
var _4d=this.treeWidget.getNodesByItem(_47)[0];
if(_47.children&&_47.children.length==0){
var _4e=this.treeWidget;
this.lazyStore.makeLoadable(_47);
_4d._loadDeferred=new _e();
model.getChildren(_47,function(_4f){
node.unmarkProcessing();
node.setChildItems(_4f).then(function(){
node._loadDeferred.resolve(_4f);
});
});
}else{
if(!_4d){
var _48=this.lazyStore.getParent(_47).__parent;
var _50=this.treeWidget.getNodesByItem(_48)[0];
if(_50&&_50.isExpandable&&!_50.isExpanded){
this.treeWidget._expandNode(_50);
_4d=this.treeWidget.getNodesByItem(_47)[0];
}else{
console.error("no valid parent node found, skip selection!");
}
}
if(_4d){
if(idx<_46.length-1){
if(_4d.isExpandable){
if(!_4d.isExpanded){
this.treeWidget._expandNode(_4d);
}
}else{
_45=this.relativeStart;
}
}
}else{
console.error("no node found, skip selection!");
}
}
}
}
}
}
if(!_44){
_44=this.treeWidget.getNodesByItem(_45)[0];
}
if(_44){
this.treeWidget.onClick(_44.item,_44);
}
this.nextSelected=null;
this.previousSelected=_44;
this.mode="";
this.relativeStart=null;
}});
_9("curam.util.LazyForestStoreModel",_3,{unloadedState:false,mayHaveChildren:function(_51){
return _51===this.root||this.store.hasAttribute(_51,"children");
},getChildren:function(_52,_53,_54){
var _55=this;
var _56=function(_57){
_55.root.children=_57;
if(_55.unloadedState==true){
for(var it in _57){
_55.store.makeLoadable(_57[it]);
}
}
_53(_57,_55.unloadedState);
_55.unloadedState=false;
};
if(_52===this.root){
if(this.root.children){
_53(this.root.children);
}else{
this.store.fetch({query:this.query,onComplete:_56,onError:_54});
}
}else{
var _58=this.store;
if(!_58.isItemLoaded(_52)){
var _59=dojo.hitch(this,arguments.callee);
_58.loadItem({item:_52,onItem:function(_5a){
_59(_5a,_56,_54);
},onError:function(){
console.error("Error when loading children!");
}});
return;
}
_56(_58.getValues(_52,"children"));
}
},onDeleteItem:function(_5b){
this.onDelete(_5b);
},onNewItem:function(_5c,_5d){
if(!_5d){
return;
}
this.getChildren(_5d.item,dojo.hitch(this,function(_5e){
this.onChildrenChange(_5d.item,_5e);
}));
}});
_9("curam.util.OrgTree",_4,{contentFrameRef:null,controllerRef:null,autoExpand:false,persist:false,configData:null,showRoot:false,currentSelection:null,onClick:function(_5f,_60){
this.onOpen(_5f,_60);
this.currentSelection={"item":_5f,"node":_60};
if(this.contentFrameRef){
var _61=this.model.store.getValue(_5f,"type");
var sc=new curam.util.ScreenContext("ORG_TREE");
var _62="../"+jsL+"/"+this.configData.nodeTypes[_61].page+"Page.do?"+sc.toRequestString();
var _63=this.model.store.getValue(_5f,"params");
var _64="";
var _65=this.configData.nodeTypes[_61].params;
for(var _66 in _63){
if(!_66.indexOf("__")==0){
var _67=_65[_66];
_64+="&"+_67+"="+_63[_66];
}
}
var _68=this.model.store.getParent(_5f);
if(_68){
var _69=this.model.store.getValue(_68,"type");
if(_69){
this.rpuRef=this.configData.nodeTypes[_69].page+"Page.do";
}
}
var _6a=_62+_64;
if(this.rpuRef&&typeof (this.rpuRef)!="undefined"){
_6a+="&__o3rpu="+this.rpuRef;
}
this.controllerRef.setURL(_6a);
this.controllerRef.loadPage();
}else{
throw new Error("ERROR: nowhere to load page!");
}
},onOpen:function(_6b,_6c){
_6c.setSelected(true);
_6c.focus();
},onClose:function(_6d,_6e){
_6e.setSelected(false);
},_onExpandoClick:function(_6f){
var _70=_6f.node;
if(_70.isExpanded){
var _71=this._collapseNode(_70);
_71.callback();
}else{
this._expandNode(_70);
}
},_collapseNode:function(_72){
var _73=new dojo.Deferred();
_73.then(function(_74){
var _75=new Array();
if(_72.item.loader){
_75.push(_72.item);
this.model.store.deleteRecursively(_72.item);
}else{
_75=this.model.store.scrollStatic(_72.item);
}
for(var i1 in _75){
var _76=this.getNodesByItem(_75[i1])[0];
this.model.store.resetToLoadable(_75[i1]);
_76.makeExpandable();
_76.state="UNLOADED";
}
});
this.onClick(_72.item,_72);
this.inherited(arguments);
if(!this.isLeftToRight()){
var _77=this;
dojo.connect(_72._wipeOut,"onEnd",function(){
_77.resetRtlIndent(_77.tree);
});
}
return _73;
},_expandNode:function(_78){
if(_78._expandNodeDeferred){
return _78._expandNodeDeferred;
}
var _79=this.model,_7a=_78.item,_7b=this;
if(!_78._loadDeferred){
if(_78.state=="UNLOADED"){
_79.unloadedState=true;
}
_78.markProcessing();
var _7c=_78.indent;
_78._loadDeferred=new _e();
_79.getChildren(_7a,function(_7d,_7e){
_78.unmarkProcessing();
_78.setChildItems(_7d).then(function(){
if(_7e){
var _7f=_78.getChildren();
for(var ch in _7f){
if(_7f[ch].isExpandable){
_7f[ch].state="UNLOADED";
}
}
}
_78._loadDeferred.resolve(_7d);
});
},function(err){
console.error(_7b,": error loading "+_78.label+" children: ",err);
_78._loadDeferred.reject(err);
});
}
var def=_78._loadDeferred.then(_f.hitch(this,function(){
var _80=_78.expand();
this.onOpen(_78.item,_78);
_78.indent=_7c;
return _80;
}));
this._startPaint(def);
return def;
},destroyRendering:function(_81){
nd=this.domNode;
if(dojo.isIE){
nd.outerHTML="";
}else{
try{
if(!_destroyContainer||_destroyContainer.ownerDocument!=nd.ownerDocument){
_destroyContainer=nd.ownerDocument.createElement("div");
}
_destroyContainer.appendChild(nd.parentNode?nd.parentNode.removeChild(nd):nd);
_destroyContainer.innerHTML="";
}
catch(e){
}
}
delete nd;
},resize:function(_82){
this.inherited(arguments);
if(!this.isLeftToRight()){
this.resetRtlIndent(this.tree);
}
},resetRtlIndent:function(_83){
var _84=_83.rootNode;
var _85=_83._nodePixelIndent-4;
var _86=_84.domNode.clientWidth;
var _87=18;
var _88=_86-_87;
while(_84){
var _89=_88-Math.max(_84.indent,0)*_85;
_5.set(_84.domNode,"backgroundPosition",_89+"px 0px");
_84=_83._getNextNode(_84);
}
}});
_9("curam.util.orgTreeStore",_6,{servlet:"JSONServlet?o3c=ORG_TREE_QUERY",treeId:null,fetch:function(_8a){
var _8b=_8a.query;
_8a.query=this.servlet+"&loader="+this.configData.rootLoader+"&root=true&initialParams="+this.initialParams;
if(_8b){
_8a.query+=_8b;
}
this.inherited(arguments);
},isItemLoaded:function(_8c){
return (_8c&&!_8c._loadObject);
},loadItem:function(_8d){
var _8e;
var _8f=_8d.item;
var _90=_8f.loader;
_8d.item.__id=this.target+this.servlet+"&loader="+_90+"&parentId="+(_8f.$ref||_8f.id)+"&treeId="+this.treeId;
if(_8d.item._loadObject){
_8d.item._loadObject(function(_91){
_8e=_91;
delete _8e._loadObject;
var _92=_91 instanceof Error?_8d.onError:_8d.onItem;
if(_92){
_92.call(_8d.scope,_91);
}
});
}else{
if(_8d.onItem){
_8d.onItem.call(_8d.scope,_8d.item);
}
}
return _8e;
},newItem:function(_93,_94){
_93=new this._constructor(_93);
if(_93.loader&&!_93.children||_93.children=="true"){
this.makeLoadable(_93);
}else{
for(var _95 in _93.children){
this.makeLoadable(_93.children[_95]);
}
}
if(_94){
var _96=this.getValue(_94.parent,_94.attribute,[]);
_96=_96.concat([_93]);
_93.__parent=_96;
this.setValue(_94.parent,_94.attribute,_96);
}
return _93;
},getItem:function(_97){
if(typeof _97==="string"){
return this._index[this.target+_97];
}
return this._index[this.target+(_97.id||_97.$ref)];
},getParent:function(_98){
var _99=this.getItem(_98);
if(_99&&_99.__parent){
return _99.__parent;
}
return null;
},scrollStatic:function(_9a){
var _9b=this.getValue(_9a,"children");
var _9c=new Array();
if(!isNaN(c)){
for(var c in _9b){
if(!_9b[c].loader){
this.scrollStatic(_9b[c]);
}else{
_9c.push(_9b[c]);
deleteRecursively(_9b[c]);
}
}
}
return _9c;
},deleteRecursively:function(_9d){
var _9e=this.getValue(_9d,"children");
for(var c in _9e){
if(!isNaN(c)){
if(_9e[c].children&&_9e[c].children!="true"){
this.deleteRecursively(_9e[c]);
}
this.deleteItem(_9e[c]);
}
}
},makeLoadable:function(_9f){
if(_9f.loader){
_9f._loadObject=dojox.rpc.JsonRest._loader;
}
},changing:function(_a0,_a1){
},resetToLoadable:function(_a2){
this.setValue(_a2,"$ref",_a2.id);
this.makeLoadable(_a2);
delete _a2.__isDirty;
},deleteItem:function(_a3){
var _a4=[];
var _a5=dojox.data._getStoreForItem(_a3)||this;
if(this.referenceIntegrity){
dojox.rpc.JsonRest._saveNotNeeded=true;
var _a6=dojox.rpc.Rest._index;
var _a7=function(_a8){
var _a9;
_a4.push(_a8);
_a8.__checked=1;
for(var i in _a8){
if(i.substring(0,2)!="__"){
var _aa=_a8[i];
if(_aa==_a3){
if(_a8!=_a6){
if(_a8 instanceof Array){
(_a9=_a9||[]).push(i);
}else{
(dojox.data._getStoreForItem(_a8)||_a5).unsetAttribute(_a8,i);
}
}
}else{
if((typeof _aa=="object")&&_aa){
if(!_aa.__checked){
_a7(_aa);
}
if(typeof _aa.__checked=="object"&&_a8!=_a6){
(dojox.data._getStoreForItem(_a8)||_a5).setValue(_a8,i,_aa.__checked);
}
}
}
}
}
if(_a9){
i=_a9.length;
_a8=_a8.__checked=_a8.concat();
while(i--){
_a8.splice(_a9[i],1);
}
return _a8;
}
return null;
};
_a7(_a6);
dojox.rpc.JsonRest._saveNotNeeded=false;
var i=0;
while(_a4[i]){
delete _a4[i++].__checked;
}
}
_a5.onDelete(_a3);
}});
_9("curam.util.UIMLazyController",_7,{templateString:_b,postCreate:function(){
this.frameLoadEvent=this.EVENT.TOPIC_PREFIX+this.iframeId;
this.setURL(this.url);
curam.util.onLoad.addSubscriber(this.iframeId,dojo.hitch(this,"processFrameLoadEvent"));
_d.log("curam.util.UIMLazyController: "+_d.getProperty("curam.widget.FramesetWidget.url")+this.url);
if(this.loadFrameOnCreate==true&&typeof (this.url)!="undefined"){
this.loadPage();
}
}});
return _10;
});
