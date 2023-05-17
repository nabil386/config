//>>built
define("curam/widget/form/ToggleButtonGroup",["dojo/_base/declare","dojo/_base/connect","dijit/form/ToggleButton"],function(_1,_2,_3){
return _1("curam.widget.form.ToggleButtonGroup",[_3],{_connectHandler:null,_unselectChannel:null,groupName:"toggleButtonGroup",postMixInProperties:function(){
this.inherited(arguments);
this._unselectChannel="/toggleButtonGroup%$��!|WE/"+this.groupName;
this._connectHandler=_2.subscribe(this._unselectChannel,this,"_unselect");
},_unselect:function(_4){
if(_4!==this&&this.checked){
this.set("checked",false);
}
},_onClick:function(e){
if(this.disabled){
return false;
}
if(!this.checked){
this._select();
}
return this.onClick(e);
},_select:function(){
dojo.publish(this._unselectChannel,[this]);
this.set("checked",true);
},_setCheckedAttr:function(_5,_6){
dojo.publish(this._unselectChannel,[this]);
this.inherited(arguments);
},destroy:function(){
try{
_2.disconnect(this._connectHandler);
}
catch(err){
console.error(err);
}
this.inherited(arguments);
}});
});
