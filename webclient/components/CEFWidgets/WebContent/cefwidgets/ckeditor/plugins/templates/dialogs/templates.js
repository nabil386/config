﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
!function(){CKEDITOR.dialog.add("templates",function(e){function t(e,t){e.setHtml("");for(var l=0,s=t.length;s>l;l++)for(var i=CKEDITOR.getTemplates(t[l]),n=i.imagesPath,o=i.templates,p=o.length,c=0;p>c;c++){var r=o[c],d=a(r,n);d.setAttribute("aria-posinset",c+1),d.setAttribute("aria-setsize",p),e.append(d)}}function a(e,t){var a=CKEDITOR.dom.element.createFromHtml('<a href="javascript:void(0)" tabIndex="-1" role="option" ><div class="cke_tpl_item"></div></a>'),s='<table style="width:350px;" class="cke_tpl_preview" role="presentation"><tr>';return e.image&&t&&(s+='<td class="cke_tpl_preview_img"><img src="'+CKEDITOR.getUrl(t+e.image)+'"'+(CKEDITOR.env.ie6Compat?' onload="this.width=this.width"':"")+' alt="" title=""></td>'),s+='<td style="white-space:normal;"><span class="cke_tpl_title">'+e.title+"</span><br/>",e.description&&(s+="<span>"+e.description+"</span>"),s+="</td></tr></table>",a.getFirst().setHtml(s),a.on("click",function(){l(e.html)}),a}function l(t){var a=CKEDITOR.dialog.getCurrent(),l=a.getValueOf("selectTpl","chkInsertOpt");l?(e.fire("saveSnapshot"),e.setData(t,function(){a.hide();var t=e.createRange();t.moveToElementEditStart(e.editable()),t.select(),setTimeout(function(){e.fire("saveSnapshot")},0)})):(e.insertHtml(t),a.hide())}function s(e){var t=e.data.getTarget(),a=n.equals(t);if(a||n.contains(t)){var l,s=e.data.getKeystroke(),i=n.getElementsByTag("a");if(i){if(a)l=i.getItem(0);else switch(s){case 40:l=t.getNext();break;case 38:l=t.getPrevious();break;case 13:case 32:t.fire("click")}l&&(l.focus(),e.data.preventDefault())}}}var i=CKEDITOR.plugins.get("templates");CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(i.path+"dialogs/templates.css"));var n,o="cke_tpl_list_label_"+CKEDITOR.tools.getNextNumber(),p=e.lang.templates,c=e.config;return{title:e.lang.templates.title,minWidth:CKEDITOR.env.ie?440:400,minHeight:340,contents:[{id:"selectTpl",label:p.title,elements:[{type:"vbox",padding:5,children:[{id:"selectTplText",type:"html",html:"<span>"+p.selectPromptMsg+"</span>"},{id:"templatesList",type:"html",focus:!0,html:'<div class="cke_tpl_list" tabIndex="-1" role="listbox" aria-labelledby="'+o+'"><div class="cke_tpl_loading"><span></span></div></div><span class="cke_voice_label" id="'+o+'">'+p.options+"</span>"},{id:"chkInsertOpt",type:"checkbox",label:p.insertOption,"default":c.templates_replaceContent}]}]}],buttons:[CKEDITOR.dialog.cancelButton],onShow:function(){var e=this.getContentElement("selectTpl","templatesList");n=e.getElement(),CKEDITOR.loadTemplates(c.templates_files,function(){var a=(c.templates||"default").split(",");a.length?(t(n,a),e.focus()):n.setHtml('<div class="cke_tpl_empty"><span>'+p.emptyListMsg+"</span></div>")}),this._.element.on("keydown",s)},onHide:function(){this._.element.removeListener("keydown",s)}}})}();