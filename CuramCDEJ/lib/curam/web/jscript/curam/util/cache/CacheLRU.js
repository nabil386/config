//>>built
define("curam/util/cache/CacheLRU",["dojo/_base/declare","dojox/collections/Dictionary"],function(_1,_2){
var _3=_1("curam/util/cache/CacheItem",null,{constPriority:{Low:10,Normal:20,High:30},constructor:function(_4,_5,_6){
if(_4==null){
throw new Error("Cache key cannot be null ");
}
this.key=_4;
this.value=_5;
if(_6==null){
_6={};
}
if(_6.priority==null){
_6.priority=this.constPriority.Normal;
}
this.options=_6;
this.lastAccessed=new Date().getTime();
},destroy:function(){
try{
this.inherited(arguments);
}
catch(err){
console.error(err);
}
}});
return _1("curam/util/cache/CacheLRU",null,{maxSize:20,activePurgeFrequency:null,_dataColection:null,_tippingPoint:null,_purgePoint:null,_purgingNow:false,constPriority:{Low:10,Normal:20,High:30},constructor:function(_7){
try{
dojo.mixin(this,_7);
this._dataColection=new dojox.collections.Dictionary();
if(this.maxSize==null){
this.maxSize=-1;
}
if(this.activePurgeFrequency==null){
this.activePurgeFrequency=-1;
}
this._tippingPoint=0.75;
this._purgePoint=Math.round(this.maxSize*this._tippingPoint);
this._purgingNow=false;
if(this.activePurgeFrequency>0){
this._timerPurge();
}
}
catch(e){
console.error("There has been an issue with cache LRU\"");
console.error(e);
}
},addItem:function(_8,_9,_a){
if(this.maxSize<1){
return;
}
if(this._dataColection.contains(_8)==true){
this._removeItem(_8);
}
var _b=new _3(_8,_9,_a);
this._dataColection.add(_b.key,_b);
if((this.maxSize>0)&&(this._dataColection.count>this.maxSize)){
this._purge();
}
},getItem:function(_c){
var _d=this._dataColection.item(_c);
if(_d!=null){
if(!this._isExpired(_d)){
_d.lastAccessed=new Date().getTime();
}else{
this._removeItem(_c);
_d=null;
}
}
var _e=null;
if(_d!=null){
_e=_d.value;
}
return _e;
},clear:function(){
this._dataColection.forEach(function(_f,_10,_11){
var tmp=_f.value;
this._removeItem(tmp.key);
},this);
},_purge:function(){
console.debug("purging cache");
this._purgingNow=true;
var _12=new Array();
this._dataColection.forEach(function(_13,_14,_15){
var tmp=_13.value;
if(this._isExpired(tmp)){
this._removeItem(tmp.key);
}else{
_12.push(tmp);
}
},this);
if(_12.length>this._purgePoint){
_12=_12.sort(function(a,b){
if(a.options.priority!=b.options.priority){
return b.options.priority-a.options.priority;
}else{
return b.lastAccessed-a.lastAccessed;
}
});
while(_12.length>this._purgePoint){
var _16=_12.pop();
this._removeItem(_16.key);
}
}
this._purgingNow=false;
},_removeItem:function(key){
var _17=this._dataColection.item(key);
this._dataColection.remove(key);
if(_17.options.callback!=null){
var _18=dojo.hitch(this,function(){
_17.options.callback(_17.key,_17.value);
});
setTimeout(_18,0);
}
},_isExpired:function(_19){
var now=new Date().getTime();
var _1a=false;
if((_19.options.expirationAbsolute)&&(_19.options.expirationAbsolute<now)){
_1a=true;
}
if((_1a==false)&&(_19.options.expirationSliding)){
var _1b=_19.lastAccessed+(_19.options.expirationSliding*1000);
if(_1b<now){
_1a=true;
}
}
return _1a;
},_timerPurge:function(){
if(this._dataColection.count>0){
this._purge();
}
this._timerID=setTimeout(dojo.hitch(this,function(){
this._timerPurgeSecond();
}),this.activePurgeFrequency);
},_timerPurgeSecond:function(){
if(this._dataColection.count>0){
this._purge();
}
this._timerID=setTimeout(dojo.hitch(this,function(){
this._timerPurge();
}),this.activePurgeFrequency);
},generateCacheOptions:function(_1c,_1d,_1e){
var _1f=new Object();
if(_1c){
_1f.expirationSliding=_1c;
}
if(_1d){
_1f.expirationAbsolute=_1d;
}
if(_1e){
_1f.priority=_1e;
}
return _1f;
},destroy:function(){
try{
this.clear();
delete this._dataColection;
this.inherited(arguments);
}
catch(err){
console.error(err);
}
}});
});
