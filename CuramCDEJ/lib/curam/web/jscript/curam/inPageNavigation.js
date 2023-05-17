//>>built
define("curam/inPageNavigation",["dijit/registry","curam/inspection/Layer","curam/tab","curam/ui/PageRequest","curam/debug","dojo/_base/declare","dojo/dom-attr","dojo/dom-style","dojo/dom-class","dojo/dom-geometry"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a){
var _b=_6("curam.inPageNavigation",null,{title:"",href:"",selected:false,constructor:function(_c){
this.title=_c.title;
this.href=_c.href;
this.selected=_c.selected;
_5.log("curam.inPageNavigation "+_5.getProperty("curam.inPageNavigation.msg")+this);
_2.register("curam/inPageNavigation",this);
},getLinks:function(){
var _d=dojo.query(".in-page-navigation-tabs")[0];
var _e=dojo.query("li",_d);
var _f=new Array();
dojo.forEach(_e,function(_10){
var _11=dojo.query("a",_10)[0];
if(!_11){
return;
}
var _12=_11.innerText||_11.textContent;
var _13=false;
dojo.filter(_7.get(_11,"class").split(" "),function(_14){
if(_14=="in-page-current-link"){
_13=true;
return;
}
});
var _15=_7.get(_11,"href");
var _16=new curam.inPageNavigation({"title":_12,"selected":_13,"href":_15});
_f.push(_16);
});
return _f;
},processMainContentAreaLinks:function(){
dojo.addOnLoad(function(){
var _17=dojo.query(".ipn-page")[0];
if(_17){
var _18=_1.byId(_7.get(_17,"id"));
var _19=_18.getChildren()[0];
_18.removeChild(_19);
if(_18.getChildren().length==0){
return;
}
var _1a=dojo.query(".in-page-nav-contentWrapper")[0];
var _1b=dojo.query("> *",_1a);
var _1c=_1b[_1b.length-1];
var pos=_a.position(_1c);
var _1d=pos.y;
var _1e="height: "+_1d+"px;";
_7.set(_1a,"style",_1e);
dojo.connect(_18,"_transition",function(_1f,_20){
var _21=dojo.query(".in-page-link",_1f.id)[0];
var _22=new curam.ui.PageRequest(_21.href);
if(jsScreenContext.hasContextBits("LIST_ROW_INLINE_PAGE")){
_22.pageHolder=window;
}
curam.tab.getTabController().handlePageRequest(_22);
});
_8.set(_17,"visibility","visible");
}
});
}});
return _b;
});
