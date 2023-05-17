//>>built
define("curam/charting",["dojo/dom-class","dojo/dom","dojo/ready","cm/_base/_dom","curam/define"],function(_1,_2,_3,_4,_5){
_5.singleton("curam.charting",{alignChartWrapper:function(_6){
_3(function(){
_6=_4.getParentByClass(_2.byId(_6),"cluster");
if(_6){
_1.add(_6,"chart-panel");
}
});
}});
return curam.charting;
});
