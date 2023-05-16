//>>built
define("curam/util/onLoad",["curam/util","curam/define","curam/debug","dojo/dom-attr"],function(_1,_2,_3,_4){
_2.singleton("curam.util.onLoad",{EVENT:"/curam/frame/load",publishers:[],subscribers:[],defaultGetIdFunction:function(_5){
var _6=_4.get(_5,"class").split(" ");
return dojo.filter(_6,function(_7){
return _7.indexOf("iframe-")==0;
})[0];
},addPublisher:function(_8){
curam.util.onLoad.publishers.push(_8);
},addSubscriber:function(_9,_a,_b){
curam.util.onLoad.subscribers.push({"getId":_b?_b:curam.util.onLoad.defaultGetIdFunction,"callback":_a,"iframeId":_9});
},removeSubscriber:function(_c,_d,_e){
curam.util.onLoad.subscribers=dojo.filter(curam.util.onLoad.subscribers,function(_f){
return !(_f.iframeId==_c&&_f.callback==_d);
});
},execute:function(){
if(window.parent==window){
curam.debug.log("curam.util.onLoad.execute(): "+_3.getProperty("curam.util.onLoad.exit"));
return;
}
var _10={};
dojo.forEach(curam.util.onLoad.publishers,function(_11){
_11(_10);
});
curam.util.onLoad.publishers=[];
curam.util.getTopmostWindow().dojo.publish("/curam/progress/unload");
window.parent.dojo.publish(curam.util.onLoad.EVENT,[window.frameElement,_10]);
}});
curam.util.subscribe(curam.util.onLoad.EVENT,function(_12,_13){
dojo.forEach(curam.util.onLoad.subscribers,function(_14){
var _15=_14.getId(_12);
if(_14.iframeId==_15){
_14.callback(_15,_13);
}
});
});
return curam.util.onLoad;
});
