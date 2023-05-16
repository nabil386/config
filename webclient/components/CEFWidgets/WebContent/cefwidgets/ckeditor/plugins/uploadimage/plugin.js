﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
"use strict";!function(){CKEDITOR.plugins.add("uploadimage",{requires:"uploadwidget",onLoad:function(){CKEDITOR.addCss(".cke_upload_uploading img{opacity: 0.3}")},init:function(a){if(CKEDITOR.plugins.clipboard.isFileApiSupported){var e=CKEDITOR.fileTools,i=e.getUploadUrl(a.config,"image");if(!i)return void CKEDITOR.error("uploadimage-config");e.addUploadWidget(a,"uploadimage",{supportedTypes:/image\/(jpeg|png|gif|bmp)/,uploadUrl:i,fileToElement:function(){var a=new CKEDITOR.dom.element("img");return a.setAttribute("src",t),a},parts:{img:"img"},onUploading:function(t){this.parts.img.setAttribute("src",t.data)},onUploaded:function(t){this.replaceWith('<img src="'+t.url+'" width="'+this.parts.img.$.naturalWidth+'" height="'+this.parts.img.$.naturalHeight+'">')}}),a.on("paste",function(t){if(t.data.dataValue.match(/<img[\s\S]+data:/i)){var d,o,n,r=t.data,l=document.implementation.createHTMLDocument(""),u=new CKEDITOR.dom.element(l.body);for(u.data("cke-editable",1),u.appendHtml(r.dataValue),d=u.find("img"),n=0;n<d.count();n++){o=d.getItem(n);var g=o.getAttribute("src")&&"data:"==o.getAttribute("src").substring(0,5),p=null===o.data("cke-realelement");if(g&&p&&!o.data("cke-upload-id")&&!o.isReadOnly(1)){var s=a.uploadRepository.create(o.getAttribute("src"));s.upload(i),e.markElement(o,"uploadimage",s.id),e.bindNotifications(a,s)}}r.dataValue=u.getHtml()}})}}});var t="data:image/gif;base64,R0lGODlhDgAOAIAAAAAAAP///yH5BAAAAAAALAAAAAAOAA4AAAIMhI+py+0Po5y02qsKADs="}();