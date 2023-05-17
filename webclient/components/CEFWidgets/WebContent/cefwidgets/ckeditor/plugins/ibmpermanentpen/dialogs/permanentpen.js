﻿/* Copyright IBM Corp. 2009-2016  All Rights Reserved. *//*Copyright IBM Corp. 2010-2014 All Rights Reserved.*/
CKEDITOR.dialog.add("permanentpen",function(t){function e(t,e){if(Object.keys){var n=Object.keys(t),o=Object.keys(e);if(n.length!=o.length)return!1}for(var i in t){if("undefined"==typeof e[i])return!1;if(("textColor"==i||"bgColor"==i)&&(t[i]=CKEDITOR.tools.convertRgbToHex(t[i]),e[i]=CKEDITOR.tools.convertRgbToHex(e[i])),t[i]!==e[i])return!1}return!0}function n(t){var e=t.substring(4,t.length-1).split(",");if(e[0]>=0&&e[0]<=255&&e[1]>=0&&e[1]<=255&&e[2]>=0&&e[2]<=255){var n=parseInt(e[0]).toString(16);"0"==n&&(n="00");var o=parseInt(e[1]).toString(16);"0"==o&&(o="00");var i=parseInt(e[2]).toString(16);return"0"==i&&(i="00"),"#"+n+o+i}return!1}function o(e,n){var o=function(){i(this),n(this,this._.parentDialog)},i=function(t){t.removeListener("show",o)},l=function(t){t.on("show",o)};t._.storedDialogs.colordialog?l(t._.storedDialogs.colordialog):CKEDITOR.on("dialogDefinition",function(t){if(t.data.name==e){var n=t.data.definition;t.removeListener(),n.onLoad=CKEDITOR.tools.override(n.onLoad,function(t){return function(){l(this),n.onLoad=t,"function"==typeof t&&t.call(this)}})}})}for(var i=t.config.fontSize_sizes.split(";"),l=[[t.lang.common.notSet,""]],r=0;r<i.length;r++){var a=i[r];if(a){var s=a.split("/");l.push(s)}}for(var g=t.config.font_names.split(";"),c=[[t.lang.common.notSet,""]],r=0;r<g.length;r++){var a=g[r];if(a){var s=a.split("/");c.push(s)}}var u=function(){var e=t.lang,n=e.colorbutton.colors,o=[[e.common.notSet,""]];div=CKEDITOR.document.createElement("div");for(var i in n)div.setStyle("color","#"+i),o.push([n[i],div.getStyle("color")]);return o.sort(function(t,e){return t[0]>e[0]})}(),h=function(t,e){e=e.toLowerCase();var o;"rgb"==e.substring(0,3)?o=n(e):"#"==e.substring(0,1)&&4==e.length&&(o=convertTo6DigitHex(e));for(var i=null==t?this:t,l=i.getInputElement().$,r=l.length;r--;){var a=l.options[r].value.toLowerCase();if(a===e||a===o||n(a)===e){l.options[r].selected="selected";break}}-1==r&&(i.add(e,e),l.options[l.length-1].selected="selected")},f=CKEDITOR.tools.getNextId()+"_bgcolorswatch",d=CKEDITOR.tools.getNextId()+"_textcolorswatch",m="border: 1px solid #A0A0A0; height: 20px; width: 20px;",p=function(t,e){"rgb"==t.substring(0,3)&&t.length<=18||"#"==t.substring(0,1)&&t.length<=7||!(t.indexOf(" ")>=0)||(t=""),t?CKEDITOR.document.getById(e).setStyle("background-color",t):CKEDITOR.document.getById(e).removeStyle("background-color")};return{title:t.lang.ibmpermanentpen.title,minWidth:220,minHeight:50,onOk:function(){this.commitContent(),t.config.styleDefined=!0;var n={bgColor:this.getValueOf("info","bgColorList"),textColor:this.getValueOf("info","textColorList"),fontName:this.getValueOf("info","font"),fontSize:this.getValueOf("info","size"),boldValue:this.getValueOf("info","bold"),italicValue:this.getValueOf("info","italic"),underlineValue:this.getValueOf("info","underline"),strikethroughValue:this.getValueOf("info","strikethrough")};t.config.ibmPermanentPenStyle&&!e(t.config.ibmPermanentPenStyle,n)&&t.fire("permanentPenStyleUpdated",n,t)},onShow:function(){this.setupContent()},contents:[{id:"info",label:t.lang.ibmpermanentpen.title,elements:[{type:"vbox",widths:["40%","5%","40%"],children:[{type:"hbox",children:[{type:"select",id:"font",style:"width : 100%;",label:t.lang.ibmpermanentpen.font,items:c,requiredContent:"span{font-family}",setup:function(){this.setValue(t.config.fontName)},commit:function(){t.config.fontName=this.getValue()}},{type:"select",id:"size",style:"width : 100%;",label:t.lang.ibmpermanentpen.size,items:l,requiredContent:"span{font-size}",setup:function(){this.setValue(t.config.fontSize)},commit:function(){t.config.fontSize=this.getValue()}}]},{type:"hbox",children:[{type:"checkbox",id:"bold",label:t.lang.ibmpermanentpen.bold,requiredContent:["strong","b"],setup:function(){this.setValue(t.config.boldValue)},commit:function(){t.config.boldValue=this.getValue()}},{type:"checkbox",id:"italic",requiredContent:["em","i"],label:t.lang.ibmpermanentpen.italic,setup:function(){this.setValue(t.config.italicValue)},commit:function(){t.config.italicValue=this.getValue()}},{type:"checkbox",id:"underline",requiredContent:"u",label:t.lang.ibmpermanentpen.underline,setup:function(){this.setValue(t.config.underlineValue)},commit:function(){t.config.underlineValue=this.getValue()}},{type:"checkbox",id:"strikethrough",requiredContent:"s",label:t.lang.ibmpermanentpen.strikethrough,setup:function(){this.setValue(t.config.strikethroughValue)},commit:function(){t.config.strikethroughValue=this.getValue()}}]},{type:"vbox",padding:0,requiredContent:"span{color}",children:[{type:"hbox",widths:["70%","5%","25%"],children:[{type:"select",id:"textColorList",items:u,label:t.lang.ibmpermanentpen.textcolor,style:"width : 100%;",onShow:function(){t.config.textColor&&h.call(this,null,t.config.textColor)},onChange:function(){p(this.getValue(),d)},onKeyUp:function(){p(this.getValue(),d)},commit:function(){t.config.textColor=this.getValue()}},{type:"html",html:'<div id="'+d+'" style="'+m+'"></div>',onLoad:function(){var t=CKEDITOR.document.getById(d);CKEDITOR.env.hc?t.hide():(t.show(),t.getParent().setStyle("vertical-align","bottom"))}},{type:"button",id:"textColorToChoose","class":"colorChooser",label:t.lang.ibmpermanentpen.color,onLoad:function(){this.getElement().getParent().setStyle("vertical-align","bottom"),CKEDITOR.env.hc?this.getElement().hide():(this.getElement().show(),this.origOnLoad&&this.origOnLoad(),this.getElement().getParent().setStyle("padding-bottom","5px"))},onClick:function(){t.getColorFromDialog(function(t){t&&this.getDialog().getContentElement("info","setTextColor").setValue(t),this.focus()},this);var e=this;o("colordialog",function(t){var o=e.getDialog().getContentElement("info","textColorList").getValue();o&&("rgb"==o.substring(0,3)?o=n(o):"#"==o.substring(0,1)&&4==o.length&&(o=convertTo6DigitHex(o)),o&&t.getContentElement("picker","currentColor").setValue(o))})}},{type:"text",id:"setTextColor",style:"display : none;","default":"",onChange:function(){this.origOnChange&&this.origOnChange(element);var t=this.getValue(),e=CKEDITOR.document.createElement("div");e.setStyle("color",t);var n=e.getStyle("color");n||(t=""),h(this.getDialog().getContentElement("info","textColorList"),t),p(t,d)},setup:function(){this.getElement().hide(),p(t.config.textColor,d)}}]},{type:"hbox",widths:["70%","5%","25%"],requiredContent:"span{background-color}",children:[{type:"select",id:"bgColorList",label:t.lang.ibmpermanentpen.background,items:u,style:"width : 100%;",onShow:function(){t.config.bgColor&&h.call(this,null,t.config.bgColor)},onChange:function(t){p(this.getValue(),f)},onKeyUp:function(){p(this.getValue(),f)},commit:function(){t.config.bgColor=this.getValue()}},{type:"html",html:'<div id="'+f+'" style="'+m+'"></div>',onLoad:function(){var t=CKEDITOR.document.getById(f);CKEDITOR.env.hc?t.hide():(t.show(),t.getParent().setStyle("vertical-align","bottom"))}},{type:"button",id:"bgColorChoose","class":"colorChooser",label:t.lang.ibmpermanentpen.color,onLoad:function(){CKEDITOR.env.hc?this.getElement().hide():(this.getElement().show(),this.getElement().getParent().setStyle("vertical-align","bottom"),this.getElement().getParent().setStyle("padding-bottom","5px"))},onClick:function(){t.getColorFromDialog(function(t){t&&this.getDialog().getContentElement("info","bgColor").setValue(t),this.focus()},this);var e=this;o("colordialog",function(t){var o=e.getDialog().getContentElement("info","bgColorList").getValue();o&&("rgb"==o.substring(0,3)?o=n(o):"#"==o.substring(0,1)&&4==o.length&&(o=convertTo6DigitHex(o)),o&&t.getContentElement("picker","currentColor").setValue(o))})}},{type:"text",id:"bgColor",style:"display : none;","default":"",onChange:function(){var t=this.getValue(),e=CKEDITOR.document.createElement("div");e.setStyle("background-color",t);var n=e.getStyle("background-color");n||(t=""),h(this.getDialog().getContentElement("info","bgColorList"),t),p(t,f)},setup:function(){this.getElement().hide(),p(t.config.bgColor,f)}}]}]}]}]}]}});