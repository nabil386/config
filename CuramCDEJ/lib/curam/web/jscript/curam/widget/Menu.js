//>>built
define("curam/widget/Menu",["dijit/Menu","dojo/_base/declare","dojo/dom-class","curam/debug","curam/util","dijit/registry","dojo/on","dojo/_base/lang","dojo/has"],function(_1,_2,_3,_4,_5,_6,on,_7,_8){
var _9=_2("curam.widget.Menu",dijit.Menu,{_CSS_CLASS_ACTIVE_MENU:"curam-active-menu",_EVENT_OPENED:"/curam/menu/opened",_EVENT_CLOSED:"/curam/menu/closed",_amIActive:false,autoFocus:true,mobContext:_8("ios"),postCreate:function(){
curam.debug.log(_4.getProperty("curam.widget.Menu.created",[this.id]));
this.own(on(this,"Open",dojo.hitch(this,function(){
curam.debug.log(_4.getProperty("curam.widget.Menu.opened",[this.id]));
curam.util.getTopmostWindow().dojo.publish(this._EVENT_OPENED,[this.id]);
this._markAsActive(true);
})));
var _a=curam.util.getTopmostWindow().dojo.subscribe(this._EVENT_OPENED,this,function(_b){
_4.log(_4.getProperty("curam.widget.Menu.event",[this.id,this._amIActive?"active":"passive",_b]));
if(this.id!=_b&&this._amIActive){
_4.log(_4.getProperty("curam.widget.Menu.deactivate"));
this._markAsActive(false);
var _c=curam.util.getTopmostWindow().dojo.subscribe(this._EVENT_CLOSED,this,function(_d){
if(_d==_b){
_4.log(_4.getProperty("curam.widget.Menu.reactivate",[_b,this.id]));
dojo.unsubscribe(_c);
this._markAsActive(true);
}
});
}
});
this.own(on(this,"Close",dojo.hitch(this,function(){
curam.debug.log(_4.getProperty("curam.widget.Menu.closing",[this.id]));
curam.util.getTopmostWindow().dojo.publish(this._EVENT_CLOSED,[this.id]);
this._markAsActive(false);
dojo.unsubscribe(_a);
})));
this.inherited(arguments);
},_markAsActive:function(_e){
if(_e){
curam.debug.log(_4.getProperty("curam.widget.Menu.add.class"),this.id);
_3.add(this.domNode,this._CSS_CLASS_ACTIVE_MENU);
}else{
curam.debug.log(_4.getProperty("curam.widget.Menu.remove.class"),this.id);
_3.remove(this.domNode,this._CSS_CLASS_ACTIVE_MENU);
}
this._amIActive=_e;
},onItemClick:function(_f,evt){
if(this.passive_hover_timer){
this.passive_hover_timer.remove();
}
this.focusChild(_f);
if(_f.disabled){
return false;
}
if(_f.popup){
this.set("selected",_f);
this.set("activated",true);
var _10=false;
if(this.mobContext){
_10=/click/.test(evt._origType||evt.type);
}else{
_10=/^key/.test(evt._origType||evt.type)||(evt.clientX==0&&evt.clientY==0);
}
this._openItemPopup(_f,_10);
}else{
this.onExecute();
_f._onClick?_f._onClick(evt):_f.onClick(evt);
}
}});
return _9;
});
