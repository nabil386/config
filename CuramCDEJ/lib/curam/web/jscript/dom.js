define(["dojo/dom-construct"],function(_1){
function _2(_3,_4,_5,_6){
var e=_1.create(_3,_4);
if(_5){
for(key in _5){
e.style[key]=_5[key];
}
}
if(_6){
e.appendChild(document.createTextNode(_6));
}
return e;
};
});

