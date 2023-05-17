dojo.provide("AnnouncementsPlayer");
require(["dijit/_Widget","dijit/_Templated","curam/util/Dialog"]);
dojo.declare("AnnouncementsPlayer",[dijit._Widget,dijit._Templated],{announcementsId:"",currentMessage:"",currentState:"",fullTextURL:"",fullTextURLTitleText:"",timeIn:1000,timeOut:1000,timeDisplayed:3000,altTextLast:"",altTextPlay:"",altTextPause:"",altTextNext:"",imgPlayPause:"",imgLast:"",imgNext:"",bidiDir:"",announcementsTarget:"",templateString:"<div dojoAttachPoint='announcementsWidget' id=${announcementsId} class='announcements' role='application' aria-labelledby='announcements' timeIn='${timeIn}' timeDisplayed='${timeDisplayed}' timeOut='${timeOut}' fullTextURL='${fullTextURL}'>"+"<span class='text-container' dojoAttachEvent='onmouseout:release'>"+"<span aria-hidden='true' class='announcement-text' dojoAttachPoint='textDisplay' id='announcements'>"+"<b dojoAttachPoint='dateTime'></b>"+"<a href='' title='${fullTextURLTitleText}' tabindex='0' dojoAttachPoint='text' dojoAttachEvent='onmouseover:hold, onfocus:hold, onkeypress:showFullMessageText, onclick:showFullMessageText'></a>"+"</span>"+"</span>"+"<span class='controls'>"+"<span role='button' tabindex='0' title='${altTextLast}' alt='${altTextLast}' id='last' class='control last' dojoAttachEvent='onmouseover:_mouseEventLast, onmouseout:_mouseEventLast,onmousedown:_mouseEventLast,onmouseup:_mouseEventLast,onkeyup:_mouseEventLast, onkeydown:_mouseEventLast, onblur:_mouseEventLast, onfocus:_mouseEventLast'><img src='${imgLast}' alt=''></span>"+"<span role='button' tabindex='0' title='${altTextPause}' alt='${altTextPause}' id='pausePlay' class='control pause' dojoAttachPoint='pausePlay' dojoAttachEvent='onmouseover:_mouseEventPausePlay, onmouseout:_mouseEventPausePlay,onmousedown:_mouseEventPausePlay,onmouseup:_mouseEventPausePlay, onkeyup:_mouseEventPausePlay, onkeydown:_mouseEventPausePlay, onblur:_mouseEventPausePlay, onfocus:_mouseEventPausePlay, onfocusout:_removeAriaLive'><img src='${imgPlayPause}' alt=''></span>"+"<span role='button' tabindex='0' title='${altTextNext}' alt='${altTextNext}' id='next' class='control next' dojoAttachEvent='onmouseover:_mouseEventNext, onmouseout:_mouseEventNext,onmousedown:_mouseEventNext,onmouseup:_mouseEventNext,onkeyup:_mouseEventNext, onkeydown:_mouseEventNext, onblur:_mouseEventNext, onfocus:_mouseEventNext'><img src='${imgNext}' alt=''></span>"+"</span>"+"</div>",constructor:function(_1){
this.bidiDir=_1.dir;
this.announcementsId=_1.announcementsId;
this.altTextPlay=_1.altTextPlay;
this.buttonPlay=this._getImages("play")[0];
this.buttonPause=this._getImages("pause")[0];
this.buttonNext=this._getImages("next")[0];
this.buttonLast=this._getImages("last")[0];
if(this.buttonPause&&this.buttonNext&&this.buttonLast){
this.imgPlayPause=this.buttonPause.img[0];
this.imgNext=this.buttonNext.img[0];
this.imgLast=this.buttonLast.img[0];
}
this.fullTextURL=_1.fullTextURL;
if(_1.timeIn){
this.timeIn=_1.timeIn;
}
if(_1.timeOut){
this.timeOut=_1.timeOut;
}
if(_1.timeDisplayed){
this.timeDisplayed=_1.timeDisplayed;
}
this.currentMessageIdx=0;
},postCreate:function(){
if(this.bidiDir=="rtl"){
var _2=document.querySelectorAll(".controls")[0];
var _3=_2.children;
var _4=document.createDocumentFragment();
_4.appendChild(_3[2]);
_4.appendChild(_3[1]);
_4.appendChild(_3[0]);
_2.innerHTML=null;
_2.appendChild(_4);
}
if(!this.announcementsId){
console.error("no announcement id");
return;
}
},startup:function(){
announcementsTarget=dojo.byId("announcements");
if(announcements.announcements.length==0){
dojo.addClass(this.announcementsWidget,"hide-announcements");
return;
}
dojo.subscribe("text/start",this,function(){
this.currentMessageIdx=announcements.announcements.length;
this.play();
});
setTimeout("dojo.publish(\"text/start\");",3000);
},getMessage:function(){
return announcements.announcements[this.currentMessageIdx];
},getNextMessage:function(){
this._incrementMessageIdx();
var _5=announcements.announcements[this.currentMessageIdx];
return _5;
},getLastMessage:function(){
this._decrementMessageIdx();
var _6=announcements.announcements[this.currentMessageIdx];
return _6;
},_incrementMessageIdx:function(){
this.currentMessageIdx++;
if(this.currentMessageIdx>=announcements.announcements.length){
this.currentMessageIdx=0;
}
},_decrementMessageIdx:function(){
this.currentMessageIdx--;
if(this.currentMessageIdx<0){
this.currentMessageIdx=announcements.announcements.length-1;
}
},play:function(){
this.currentState="playing";
dojo.removeClass(this.pausePlay,"play");
dojo.attr(this.pausePlay,"alt",this.altTextPause);
dojo.attr(this.pausePlay,"title",this.altTextPause);
var _7=this.getNextMessage();
this.displayMessage(_7);
dojo.style(this.textDisplay,"opacity","0");
this.player=dojo.fx.chain([dojo.fadeIn({node:this.textDisplay,duration:this.timeIn}),dojo.fadeOut({node:this.textDisplay,delay:this.timeDisplayed,duration:this.timeOut})]);
var _8=dojo.connect(this.player,"onEnd",this,function(){
dojo.disconnect(_8);
this.play();
});
this.player.play();
return;
},_pauseOrPlay:function(){
if(this.currentState=="playing"){
this.pause();
}else{
this.play();
}
},hold:function(){
if(this.currentState!="paused"){
dojo.addClass(this.pausePlay,"play");
var _9=dojo.byId("pausePlay");
_9.children[0].src=this.buttonPlay.img[0];
this.show();
this.player.pause();
}
},release:function(){
if(this.currentState=="playing"){
dojo.removeClass(this.pausePlay,"play");
var _a=dojo.byId("pausePlay");
_a.children[0].src=this.buttonPause.img[0];
this.player.play();
}
},show:function(){
if(this.player){
this.player.stop();
}
var _b=this.getMessage();
this.displayMessage(_b);
dojo.attr(announcementsTarget,"aria-live","polite");
},pause:function(){
dojo.addClass(this.pausePlay,"play");
dojo.attr(this.pausePlay,"alt",this.altTextPlay);
dojo.attr(this.pausePlay,"title",this.altTextPlay);
var _c=dojo.byId("pausePlay");
_c.children[0].src=this.buttonPlay.img[0];
this.show();
if(this.player){
this.player.pause();
}
this.currentState="paused";
},last:function(){
this.currentMessage=this.getLastMessage();
this.pause();
},next:function(){
this.currentMessage=this.getNextMessage();
this.pause();
},displayMessage:function(_d){
dojo.style(this.textDisplay,"opacity","1");
if(_d.bidiDir){
dojo.style(this.textDisplay,"direction",_d.bidiDir);
dojo.style(this.textDisplay,"display","inline-block");
dojo.style(this.textDisplay,"textAlign","start");
}
this.dateTime.innerHTML=_d.date+" "+_d.time+" ";
this.text.innerHTML=_d.text;
var _e=this.fullTextURL+"Page.do?o3ctx=4096&announcementID="+_d.id;
dojo.attr(this.text,"href",_e);
},showFullMessageText:function(_f){
var _10=CEFUtils.keyPressExist(_f);
var _11=CEFUtils.enterKeyPress(_f);
if(_10==true&&_11!==true){
return;
}
if(this.fullTextURL.length==0){
return;
}
this.currentState="showingModal";
var _12=CEFUtils.showInModal(_f);
var _13=this.player;
_12.registerOnDisplayHandler(function(){
_13.pause();
});
_12.registerBeforeCloseHandler(function(){
_13.play();
});
},_validKeyPress:function(_14){
if(_14.type==="keyup"||_14.type==="keydown"){
if(CEFUtils.enterKeyPress(_14)!==true){
return false;
}
}
return true;
},_actionEvent:function(_15){
return (_15.type=="mouseup"||_15.type=="keyup");
},_mouseEventPausePlay:function(_16){
if(!this._validKeyPress(_16)){
return;
}
this._pausePlayStyling(_16);
if(this._actionEvent(_16)){
this._pauseOrPlay();
}
},_mouseEventLast:function(_17){
if(!this._validKeyPress(_17)){
return;
}
this._lastNextStyling("last",_17);
if(this._actionEvent(_17)){
this.last();
}
},_mouseEventNext:function(_18){
if(!this._validKeyPress(_18)){
return;
}
this._lastNextStyling("next",_18);
if(this._actionEvent(_18)){
this.next();
}
},_pausePlayStyling:function(_19){
var _1a=dojo.byId("pausePlay");
if(_19.type=="mouseover"||_19.type=="focus"){
if(dojo.hasClass(_1a,"play")){
dojo.removeClass(_1a,"play");
dojo.addClass(_1a,"play-roll-over");
_1a.children[0].src=this.buttonPlay.imgFocus[0];
}else{
dojo.addClass(_1a,"pause-roll-over");
_1a.children[0].src=this.buttonPause.imgFocus[0];
}
}else{
if(_19.type=="mouseout"||_19.type=="blur"){
if(dojo.hasClass(_1a,"play-roll-over")){
dojo.removeClass(_1a,"play-roll-over");
dojo.addClass(_1a,"play");
_1a.children[0].src=this.buttonPlay.img[0];
}else{
dojo.removeClass(_1a,"pause-roll-over");
_1a.children[0].src=this.buttonPause.img[0];
}
}else{
if(_19.type=="mousedown"||_19.type=="keydown"){
if(dojo.hasClass(_1a,"pause-roll-over")){
dojo.removeClass(_1a,"pause-roll-over");
dojo.addClass(_1a,"pause-select");
_1a.children[0].src=this.buttonPause.imgFocus[0];
}else{
dojo.removeClass(_1a,"play-roll-over");
dojo.addClass(_1a,"play-select");
_1a.children[0].src=this.buttonPlay.imgFocus[0];
}
}else{
if(_19.type=="mouseup"||_19.type=="keyup"){
if(dojo.hasClass(_1a,"play-select")){
dojo.removeClass(_1a,"play-select");
dojo.addClass(_1a,"pause-roll-over");
_1a.children[0].src=this.buttonPause.imgFocus[0];
}else{
dojo.removeClass(_1a,"pause-select");
dojo.addClass(_1a,"play-roll-over");
_1a.children[0].src=this.buttonPlay.imgFocus[0];
}
}
}
}
}
},_lastNextStyling:function(_1b,_1c){
var _1d=_1b+"-roll-over";
var _1e=_1b+"-select";
var _1f=_1b;
var _20=dojo.byId(_1b);
if(_1c.type=="mouseover"||_1c.type=="focus"){
dojo.addClass(_20,_1d);
_20.children[0].src=this._getLastNextImage(_1b,true);
}else{
if(_1c.type=="mousedown"||_1c.type=="keydown"){
dojo.removeClass(_20,_1d);
dojo.addClass(_20,_1e);
_20.children[0].src=this._getLastNextImage(_1b,true);
}else{
if(_1c.type=="mouseup"||_1c.type=="keyup"){
dojo.removeClass(_20,_1e);
dojo.addClass(_20,_1d);
_20.children[0].src=this._getLastNextImage(_1b,true);
}else{
if(_1c.type=="mouseout"||_1c.type=="blur"){
dojo.removeClass(_20,_1d);
_20.children[0].src=this._getLastNextImage(_1b,false);
}
}
}
}
},_getImages:function(_21){
return announcements.images.filter(function(_22){
return _22.type==_21;
});
},_getLastNextImage:function(_23,_24){
if(_24){
if(_23=="next"){
return this.buttonNext.imgFocus[0];
}else{
return this.buttonLast.imgFocus[0];
}
}else{
if(_23=="next"){
return this.buttonNext.img[0];
}else{
return this.buttonLast.img[0];
}
}
},_removeAriaLive:function(){
dojo.removeAttr(announcementsTarget,"aria-live");
}});

