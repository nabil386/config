dojo.provide("birtPod");
var birtPod={eventSubscriptions:[],lazyLoad:function(_1,_2){
var _3=curam.util.getTopmostWindow().dojo;
birtPod.podsFullyLoadSubscription=_3.subscribe("pods.fullyloaded",function(){
setTimeout(function(){
var _4=decodeURIComponent(_2);
dojo.attr(dojo.byId(_1),"src",_4);
curam.util.getTopmostWindow().dojo.unsubscribe(birtPod.podLoadSubscription);
},200);
});
birtPod.eventSubscriptions.push(birtPod.podsFullyLoadSubscription);
dojo.addOnWindowUnload(function(){
dojo.forEach(birtPod.eventSubscriptions,_3.unsubscribe);
});
}};

