﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.plugins.add("iframedialog",{requires:"dialog",onLoad:function(){CKEDITOR.dialog.addIframe=function(e,t,n,o,i,a,d){var r={type:"iframe",src:n,width:"100%",height:"100%"};"function"==typeof a?r.onContentLoad=a:r.onContentLoad=function(){var e=this.getElement(),t=e.$.contentWindow;if(t.onDialogEvent){var n=this.getDialog(),o=function(e){return t.onDialogEvent(e)};n.on("ok",o),n.on("cancel",o),n.on("resize",o),n.on("hide",function(e){n.removeListener("ok",o),n.removeListener("cancel",o),n.removeListener("resize",o),e.removeListener()}),t.onDialogEvent({name:"load",sender:this,editor:n._.editor})}};var l={title:t,minWidth:o,minHeight:i,contents:[{id:"iframe",label:t,expand:!0,elements:[r],style:"width:"+r.width+";height:"+r.height}]};for(var s in d)l[s]=d[s];this.add(e,function(){return l})},function(){var e=function(e,t,n){if(!(arguments.length<3)){var o=this._||(this._={}),i=t.onContentLoad&&CKEDITOR.tools.bind(t.onContentLoad,this),a=CKEDITOR.tools.cssLength(t.width),d=CKEDITOR.tools.cssLength(t.height);o.frameId=CKEDITOR.tools.getNextId()+"_iframe",e.on("load",function(){var e=CKEDITOR.document.getById(o.frameId),t=e.getParent();t.setStyles({width:a,height:d})});var r={src:"%2",id:o.frameId,frameborder:0,allowtransparency:!0},l=[];"function"==typeof t.onContentLoad&&(r.onload="CKEDITOR.tools.callFunction(%1);"),CKEDITOR.ui.dialog.uiElement.call(this,e,t,l,"iframe",{width:a,height:d},r,""),n.push('<div style="width:'+a+";height:"+d+';" id="'+this.domId+'"></div>'),l=l.join(""),e.on("show",function(){var e=CKEDITOR.document.getById(o.frameId),n=e.getParent(),a=CKEDITOR.tools.addFunction(i),d=l.replace("%1",a).replace("%2",CKEDITOR.tools.htmlEncode(t.src));n.setHtml(d)})}};e.prototype=new CKEDITOR.ui.dialog.uiElement,CKEDITOR.dialog.addUIElement("iframe",{build:function(t,n,o){return new e(t,n,o)}})}()}});