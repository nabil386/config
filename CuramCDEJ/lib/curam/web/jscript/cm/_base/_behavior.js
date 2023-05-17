//>>built
define("cm/_base/_behavior",["dojo/behavior"],function(){
var cm=dojo.global.cm||{};
dojo.global.cm=cm;
dojo.mixin(cm,{behaviors:{},addedBehaviors:{},addBehavior:function(_1){
var b=cm.behaviors[_1];
if(b&&!cm.addedBehaviors[_1]){
dojo.behavior.add(b);
cm.addedBehaviors[_1]=true;
dojo.behavior.apply();
}
},registerBehavior:function(_2,_3){
cm.behaviors[_2]=_3;
}});
return cm;
});
