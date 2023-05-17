//>>built
define("curam/widget/form/DateTextBox",["dojo/_base/declare","idx/form/DateTextBox","dojo/date/locale","dojo/dom","dojo/query"],function(_1,_2,_3,_4,_5){
return _1("curam.widget.form.DateTextBox",_2,{curamFormat:{selector:"date",datePattern:jsDF,locale:dojo.config.locale},value:"",postMixInProperties:function(){
this.inherited(arguments);
this.value=_3.parse(this.value,this.curamFormat);
},postCreate:function(){
this.inherited(arguments);
var _6=_5("input[type='hidden'][name='"+this.id+"']",this.domNode)[0];
if(_6){
_6.value=_3.format(this.value,this.curamFormat);
}
},serialize:function(_7,_8){
return _3.format(_7,this.curamFormat);
}});
});
