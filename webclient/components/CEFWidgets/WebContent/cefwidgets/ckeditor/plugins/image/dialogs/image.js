﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
!function(){var t=function(t,e){function i(){var t=arguments,e=this.getContentElement("advanced","txtdlgGenStyle");e&&e.commit.apply(e,t),this.foreach(function(e){e.commit&&"txtdlgGenStyle"!=e.id&&e.commit.apply(e,t)})}function n(t){if(!a){a=1;var e=this.getDialog(),i=e.imageElement;if(i){this.commit(s,i),t=[].concat(t);for(var n,l=t.length,o=0;l>o;o++)n=e.getContentElement.apply(e,t[o].split(":")),n&&n.setup(s,i)}a=0}}var a,l,s=1,o=2,r=4,m=8,g=/^\s*(\d+)((px)|\%)?\s*$/i,h=/(^\s*(\d+)((px)|\%)?\s*$)|^$/i,d=/^\d+px$/,u=function(){var t=this.getValue(),e=this.getDialog(),i=t.match(g);if(i&&("%"==i[2]&&p(e,!1),t=i[1]),e.lockRatio){var n=e.originalElement;"true"==n.getCustomData("isReady")&&("txtHeight"==this.id?(t&&"0"!=t&&(t=Math.round(n.$.width*(t/n.$.height))),isNaN(t)||e.setValueOf("info","txtWidth",t)):(t&&"0"!=t&&(t=Math.round(n.$.height*(t/n.$.width))),isNaN(t)||e.setValueOf("info","txtHeight",t)))}c(e)},c=function(t){return t.originalElement&&t.preview?(t.commitContent(r,t.preview),0):1},p=function(t,e){if(!t.getContentElement("info","ratioLock"))return null;var i=t.originalElement;if(!i)return null;if("check"==e){if(!t.userlockRatio&&"true"==i.getCustomData("isReady")){var n=t.getValueOf("info","txtWidth"),a=t.getValueOf("info","txtHeight"),l=1e3*i.$.width/i.$.height,s=1e3*n/a;t.lockRatio=!1,n||a?isNaN(l)||isNaN(s)||Math.round(l)==Math.round(s)&&(t.lockRatio=!0):t.lockRatio=!0}}else void 0!==e?t.lockRatio=e:(t.userlockRatio=1,t.lockRatio=!t.lockRatio);var o=CKEDITOR.document.getById(C);if(t.lockRatio?o.removeClass("cke_btn_unlocked"):o.addClass("cke_btn_unlocked"),o.setAttribute("aria-checked",t.lockRatio),CKEDITOR.env.hc){var r=o.getChild(0);r.setHtml(t.lockRatio?CKEDITOR.env.ie?"■":"▣":CKEDITOR.env.ie?"□":"▢")}return t.lockRatio},v=function(t,e){var i=t.originalElement,n="true"==i.getCustomData("isReady");if(n){var a,l,s=t.getContentElement("info","txtWidth"),o=t.getContentElement("info","txtHeight");e?(a=0,l=0):(a=i.$.width,l=i.$.height),s&&s.setValue(a),o&&o.setValue(l)}c(t)},f=function(t,e){function i(t,e){var i=t.match(g);return i?("%"==i[2]&&(i[1]+="%",p(n,!1)),i[1]):e}if(t==s){var n=this.getDialog(),a="",l="txtWidth"==this.id?"width":"height",o=e.getAttribute(l);o&&(a=i(o,a)),a=i(e.getStyle(l),a),this.setValue(a)}},b=function(){var e=this.originalElement,i=CKEDITOR.document.getById(k);e.setCustomData("isReady","true"),e.removeListener("load",b),e.removeListener("error",y),e.removeListener("abort",y),i&&i.setStyle("display","none"),this.dontResetSize||v(this,t.config.image_prefillDimensions===!1),this.firstLoad&&CKEDITOR.tools.setTimeout(function(){p(this,"check")},0,this),this.firstLoad=!1,this.dontResetSize=!1,c(this)},y=function(){var t=this.originalElement,e=CKEDITOR.document.getById(k);t.removeListener("load",b),t.removeListener("error",y),t.removeListener("abort",y);var i=CKEDITOR.getUrl(CKEDITOR.plugins.get("image").path+"images/noimage.png");this.preview&&this.preview.setAttribute("src",i),e&&e.setStyle("display","none"),p(this,!1)},E=function(t){return CKEDITOR.tools.getNextId()+"_"+t},C=E("btnLockSizes"),x=E("btnResetSize"),k=E("ImagePreviewLoader"),S=E("previewLink"),w=E("previewImage");return{title:t.lang.image["image"==e?"title":"titleButton"],minWidth:420,minHeight:360,onShow:function(){this.imageElement=!1,this.linkElement=!1,this.imageEditMode=!1,this.linkEditMode=!1,this.lockRatio=!0,this.userlockRatio=0,this.dontResetSize=!1,this.firstLoad=!0,this.addLink=!1;var t=this.getParentEditor(),i=t.getSelection(),n=i&&i.getSelectedElement(),a=n&&t.elementPath(n).contains("a",1),r=CKEDITOR.document.getById(k);if(r&&r.setStyle("display","none"),l=new CKEDITOR.dom.element("img",t.document),this.preview=CKEDITOR.document.getById(w),this.originalElement=t.document.createElement("img"),this.originalElement.setAttribute("alt",""),this.originalElement.setCustomData("isReady","false"),a){this.linkElement=a,this.linkEditMode=!0,this.addLink=!0;var m=a.getChildren();if(1==m.count()){var g=m.getItem(0);g.type==CKEDITOR.NODE_ELEMENT&&(g.is("img")||g.is("input"))&&(this.imageElement=m.getItem(0),this.imageElement.is("img")?this.imageEditMode="img":this.imageElement.is("input")&&(this.imageEditMode="input"))}"image"==e&&this.setupContent(o,a)}this.customImageElement?(this.imageEditMode="img",this.imageElement=this.customImageElement,delete this.customImageElement):(n&&"img"==n.getName()&&!n.data("cke-realelement")||n&&"input"==n.getName()&&"image"==n.getAttribute("type"))&&(this.imageEditMode=n.getName(),this.imageElement=n),this.imageEditMode&&(this.cleanImageElement=this.imageElement,this.imageElement=this.cleanImageElement.clone(!0,!0),this.setupContent(s,this.imageElement)),p(this,!0),CKEDITOR.tools.trim(this.getValueOf("info","txtUrl"))||(this.preview.removeAttribute("src"),this.preview.setStyle("display","none"))},onOk:function(){if(this.imageEditMode){var i=this.imageEditMode;"image"==e&&"input"==i&&confirm(t.lang.image.button2Img)?(i="img",this.imageElement=t.document.createElement("img"),this.imageElement.setAttribute("alt",""),t.insertElement(this.imageElement)):"image"!=e&&"img"==i&&confirm(t.lang.image.img2Button)?(i="input",this.imageElement=t.document.createElement("input"),this.imageElement.setAttributes({type:"image",alt:""}),t.insertElement(this.imageElement)):(this.imageElement=this.cleanImageElement,delete this.cleanImageElement)}else"image"==e?this.imageElement=t.document.createElement("img"):(this.imageElement=t.document.createElement("input"),this.imageElement.setAttribute("type","image")),this.imageElement.setAttribute("alt","");this.linkEditMode||(this.linkElement=t.document.createElement("a")),this.commitContent(s,this.imageElement),this.commitContent(o,this.linkElement),this.imageElement.getAttribute("style")||this.imageElement.removeAttribute("style"),this.imageEditMode?!this.linkEditMode&&this.addLink?(t.insertElement(this.linkElement),this.imageElement.appendTo(this.linkElement)):this.linkEditMode&&!this.addLink&&(t.getSelection().selectElement(this.linkElement),t.insertElement(this.imageElement)):this.addLink?this.linkEditMode?this.linkElement.equals(t.getSelection().getSelectedElement())?(this.linkElement.setHtml(""),this.linkElement.append(this.imageElement,!1)):t.insertElement(this.imageElement):(t.insertElement(this.linkElement),this.linkElement.append(this.imageElement,!1)):t.insertElement(this.imageElement)},onLoad:function(){"image"!=e&&this.hidePage("Link");var t=this._.element.getDocument();this.getContentElement("info","ratioLock")&&(this.addFocusable(t.getById(x),5),this.addFocusable(t.getById(C),5)),this.commitContent=i},onHide:function(){this.preview&&this.commitContent(m,this.preview),this.originalElement&&(this.originalElement.removeListener("load",b),this.originalElement.removeListener("error",y),this.originalElement.removeListener("abort",y),this.originalElement.remove(),this.originalElement=!1),delete this.imageElement},contents:[{id:"info",label:t.lang.image.infoTab,accessKey:"I",elements:[{type:"vbox",padding:0,children:[{type:"hbox",widths:["280px","110px"],align:"right",children:[{id:"txtUrl",type:"text",label:t.lang.common.url,required:!0,onChange:function(){var t=this.getDialog(),e=this.getValue();if(e.length>0){t=this.getDialog();var i=t.originalElement;t.preview&&t.preview.removeStyle("display"),i.setCustomData("isReady","false");var n=CKEDITOR.document.getById(k);n&&n.setStyle("display",""),i.on("load",b,t),i.on("error",y,t),i.on("abort",y,t),i.setAttribute("src",e),t.preview&&(l.setAttribute("src",e),t.preview.setAttribute("src",l.$.src),c(t))}else t.preview&&(t.preview.removeAttribute("src"),t.preview.setStyle("display","none"))},setup:function(t,e){if(t==s){var i=e.data("cke-saved-src")||e.getAttribute("src"),n=this;this.getDialog().dontResetSize=!0,n.setValue(i),n.setInitValue()}},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())?(e.data("cke-saved-src",this.getValue()),e.setAttribute("src",this.getValue())):t==m&&(e.setAttribute("src",""),e.removeAttribute("src"))},validate:CKEDITOR.dialog.validate.notEmpty(t.lang.image.urlMissing)},{type:"button",id:"browse",style:"display:inline-block;margin-top:14px;",align:"center",label:t.lang.common.browseServer,hidden:!0,filebrowser:"info:txtUrl"}]}]},{id:"txtAlt",type:"text",label:t.lang.image.alt,accessKey:"T","default":"",onChange:function(){c(this.getDialog())},setup:function(t,e){t==s&&this.setValue(e.getAttribute("alt"))},commit:function(t,e){t==s?(this.getValue()||this.isChanged())&&e.setAttribute("alt",this.getValue()):t==r?e.setAttribute("alt",this.getValue()):t==m&&e.removeAttribute("alt")}},{type:"hbox",children:[{id:"basic",type:"vbox",children:[{type:"hbox",requiredContent:"img{width,height}",widths:["50%","50%"],children:[{type:"vbox",padding:1,children:[{type:"text",width:"45px",id:"txtWidth",label:t.lang.common.width,onKeyUp:u,onChange:function(){n.call(this,"advanced:txtdlgGenStyle")},validate:function(){var e=this.getValue().match(h),i=!(!e||0===parseInt(e[1],10));return i||alert(t.lang.common.invalidWidth),i},setup:f,commit:function(e,i){var n=this.getValue();if(e==s)n&&t.activeFilter.check("img{width,height}")?i.setStyle("width",CKEDITOR.tools.cssLength(n)):i.removeStyle("width"),i.removeAttribute("width");else if(e==r){var a=n.match(g);if(a)i.setStyle("width",CKEDITOR.tools.cssLength(n));else{var l=this.getDialog().originalElement;"true"==l.getCustomData("isReady")&&i.setStyle("width",l.$.width+"px")}}else e==m&&(i.removeAttribute("width"),i.removeStyle("width"))}},{type:"text",id:"txtHeight",width:"45px",label:t.lang.common.height,onKeyUp:u,onChange:function(){n.call(this,"advanced:txtdlgGenStyle")},validate:function(){var e=this.getValue().match(h),i=!(!e||0===parseInt(e[1],10));return i||alert(t.lang.common.invalidHeight),i},setup:f,commit:function(e,i){var n=this.getValue();if(e==s)n&&t.activeFilter.check("img{width,height}")?i.setStyle("height",CKEDITOR.tools.cssLength(n)):i.removeStyle("height"),i.removeAttribute("height");else if(e==r){var a=n.match(g);if(a)i.setStyle("height",CKEDITOR.tools.cssLength(n));else{var l=this.getDialog().originalElement;"true"==l.getCustomData("isReady")&&i.setStyle("height",l.$.height+"px")}}else e==m&&(i.removeAttribute("height"),i.removeStyle("height"))}}]},{id:"ratioLock",type:"html",style:"margin-top:30px;width:40px;height:40px;",onLoad:function(){var t=CKEDITOR.document.getById(x),e=CKEDITOR.document.getById(C);t&&(t.on("click",function(t){v(this),t.data&&t.data.preventDefault()},this.getDialog()),t.on("mouseover",function(){this.addClass("cke_btn_over")},t),t.on("mouseout",function(){this.removeClass("cke_btn_over")},t)),e&&(e.on("click",function(t){p(this);var e=this.originalElement,i=this.getValueOf("info","txtWidth");if("true"==e.getCustomData("isReady")&&i){var n=e.$.height/e.$.width*i;isNaN(n)||(this.setValueOf("info","txtHeight",Math.round(n)),c(this))}t.data&&t.data.preventDefault()},this.getDialog()),e.on("mouseover",function(){this.addClass("cke_btn_over")},e),e.on("mouseout",function(){this.removeClass("cke_btn_over")},e))},html:'<div><a href="javascript:void(0)" tabindex="-1" title="'+t.lang.image.lockRatio+'" class="cke_btn_locked" id="'+C+'" role="checkbox"><span class="cke_icon"></span><span class="cke_label">'+t.lang.image.lockRatio+'</span></a><a href="javascript:void(0)" tabindex="-1" title="'+t.lang.image.resetSize+'" class="cke_btn_reset" id="'+x+'" role="button"><span class="cke_label">'+t.lang.image.resetSize+"</span></a></div>"}]},{type:"vbox",padding:1,children:[{type:"text",id:"txtBorder",requiredContent:"img{border-width}",width:"60px",label:t.lang.image.border,"default":"",onKeyUp:function(){c(this.getDialog())},onChange:function(){n.call(this,"advanced:txtdlgGenStyle")},validate:CKEDITOR.dialog.validate.integer(t.lang.image.validateBorder),setup:function(t,e){if(t==s){var i,n=e.getStyle("border-width");n=n&&n.match(/^(\d+px)(?: \1 \1 \1)?$/),i=n&&parseInt(n[1],10),isNaN(parseInt(i,10))&&(i=e.getAttribute("border")),this.setValue(i)}},commit:function(t,e){var i=parseInt(this.getValue(),10);t==s||t==r?(isNaN(i)?!i&&this.isChanged()&&e.removeStyle("border"):(e.setStyle("border-width",CKEDITOR.tools.cssLength(i)),e.setStyle("border-style","solid")),t==s&&e.removeAttribute("border")):t==m&&(e.removeAttribute("border"),e.removeStyle("border-width"),e.removeStyle("border-style"),e.removeStyle("border-color"))}},{type:"text",id:"txtHSpace",requiredContent:"img{margin-left,margin-right}",width:"60px",label:t.lang.image.hSpace,"default":"",onKeyUp:function(){c(this.getDialog())},onChange:function(){n.call(this,"advanced:txtdlgGenStyle")},validate:CKEDITOR.dialog.validate.integer(t.lang.image.validateHSpace),setup:function(t,e){if(t==s){var i,n,a,l=e.getStyle("margin-left"),o=e.getStyle("margin-right");l=l&&l.match(d),o=o&&o.match(d),n=parseInt(l,10),a=parseInt(o,10),i=n==a&&n,isNaN(parseInt(i,10))&&(i=e.getAttribute("hspace")),this.setValue(i)}},commit:function(t,e){var i=parseInt(this.getValue(),10);t==s||t==r?(isNaN(i)?!i&&this.isChanged()&&(e.removeStyle("margin-left"),e.removeStyle("margin-right")):(e.setStyle("margin-left",CKEDITOR.tools.cssLength(i)),e.setStyle("margin-right",CKEDITOR.tools.cssLength(i))),t==s&&e.removeAttribute("hspace")):t==m&&(e.removeAttribute("hspace"),e.removeStyle("margin-left"),e.removeStyle("margin-right"))}},{type:"text",id:"txtVSpace",requiredContent:"img{margin-top,margin-bottom}",width:"60px",label:t.lang.image.vSpace,"default":"",onKeyUp:function(){c(this.getDialog())},onChange:function(){n.call(this,"advanced:txtdlgGenStyle")},validate:CKEDITOR.dialog.validate.integer(t.lang.image.validateVSpace),setup:function(t,e){if(t==s){var i,n,a,l=e.getStyle("margin-top"),o=e.getStyle("margin-bottom");l=l&&l.match(d),o=o&&o.match(d),n=parseInt(l,10),a=parseInt(o,10),i=n==a&&n,isNaN(parseInt(i,10))&&(i=e.getAttribute("vspace")),this.setValue(i)}},commit:function(t,e){var i=parseInt(this.getValue(),10);t==s||t==r?(isNaN(i)?!i&&this.isChanged()&&(e.removeStyle("margin-top"),e.removeStyle("margin-bottom")):(e.setStyle("margin-top",CKEDITOR.tools.cssLength(i)),e.setStyle("margin-bottom",CKEDITOR.tools.cssLength(i))),t==s&&e.removeAttribute("vspace")):t==m&&(e.removeAttribute("vspace"),e.removeStyle("margin-top"),e.removeStyle("margin-bottom"))}},{id:"cmbAlign",requiredContent:"img{float}",type:"select",widths:["35%","65%"],style:"width:90px",label:t.lang.common.align,"default":"",items:[[t.lang.common.notSet,""],[t.lang.common.alignLeft,"left"],[t.lang.common.alignRight,"right"]],onChange:function(){c(this.getDialog()),n.call(this,"advanced:txtdlgGenStyle")},setup:function(t,e){if(t==s){var i=e.getStyle("float");switch(i){case"inherit":case"none":i=""}!i&&(i=(e.getAttribute("align")||"").toLowerCase()),this.setValue(i)}},commit:function(t,e){var i=this.getValue();if(t==s||t==r){if(i?e.setStyle("float",i):e.removeStyle("float"),t==s)switch(i=(e.getAttribute("align")||"").toLowerCase()){case"left":case"right":e.removeAttribute("align")}}else t==m&&e.removeStyle("float")}}]}]},{type:"vbox",height:"250px",children:[{type:"html",id:"htmlPreview",style:"width:95%;",html:"<div>"+CKEDITOR.tools.htmlEncode(t.lang.common.preview)+'<br><div id="'+k+'" class="ImagePreviewLoader" style="display:none"><div class="loading">&nbsp;</div></div><div class="ImagePreviewBox"><table><tr><td><a href="javascript:void(0)" target="_blank" onclick="return false;" id="'+S+'"><img id="'+w+'" alt="" /></a>'+(t.config.image_previewText||"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Maecenas feugiat consequat diam. Maecenas metus. Vivamus diam purus, cursus a, commodo non, facilisis vitae, nulla. Aenean dictum lacinia tortor. Nunc iaculis, nibh non iaculis aliquam, orci felis euismod neque, sed ornare massa mauris sed velit. Nulla pretium mi et risus. Fusce mi pede, tempor id, cursus ac, ullamcorper nec, enim. Sed tortor. Curabitur molestie. Duis velit augue, condimentum at, ultrices a, luctus ut, orci. Donec pellentesque egestas eros. Integer cursus, augue in cursus faucibus, eros pede bibendum sem, in tempus tellus justo quis ligula. Etiam eget tortor. Vestibulum rutrum, est ut placerat elementum, lectus nisl aliquam velit, tempor aliquam eros nunc nonummy metus. In eros metus, gravida a, gravida sed, lobortis id, turpis. Ut ultrices, ipsum at venenatis fringilla, sem nulla lacinia tellus, eget aliquet turpis mauris non enim. Nam turpis. Suspendisse lacinia. Curabitur ac tortor ut ipsum egestas elementum. Nunc imperdiet gravida mauris.")+"</td></tr></table></div></div>"}]}]}]},{id:"Link",requiredContent:"a[href]",label:t.lang.image.linkTab,padding:0,elements:[{id:"txtUrl",type:"text",label:t.lang.common.url,style:"width: 100%","default":"",setup:function(t,e){if(t==o){var i=e.data("cke-saved-href");i||(i=e.getAttribute("href")),this.setValue(i)}},commit:function(e,i){if(e==o&&(this.getValue()||this.isChanged())){var n=this.getValue();i.data("cke-saved-href",n),i.setAttribute("href",n),this.getValue()||!t.config.image_removeLinkByEmptyURL?this.getDialog().addLink=!0:this.getDialog().addLink=!1}}},{type:"button",id:"browse",filebrowser:{action:"Browse",target:"Link:txtUrl",url:t.config.filebrowserImageBrowseLinkUrl},style:"float:right",hidden:!0,label:t.lang.common.browseServer},{id:"cmbTarget",type:"select",requiredContent:"a[target]",label:t.lang.common.target,"default":"",items:[[t.lang.common.notSet,""],[t.lang.common.targetNew,"_blank"],[t.lang.common.targetTop,"_top"],[t.lang.common.targetSelf,"_self"],[t.lang.common.targetParent,"_parent"]],setup:function(t,e){t==o&&this.setValue(e.getAttribute("target")||"")},commit:function(t,e){t==o&&(this.getValue()||this.isChanged())&&e.setAttribute("target",this.getValue())}}]},{id:"Upload",hidden:!0,filebrowser:"uploadButton",label:t.lang.image.upload,elements:[{type:"file",id:"upload",label:t.lang.image.btnUpload,style:"height:40px",size:38},{type:"fileButton",id:"uploadButton",filebrowser:"info:txtUrl",label:t.lang.image.btnUpload,"for":["Upload","upload"]}]},{id:"advanced",label:t.lang.common.advancedTab,elements:[{type:"hbox",widths:["50%","25%","25%"],children:[{type:"text",id:"linkId",requiredContent:"img[id]",label:t.lang.common.id,setup:function(t,e){t==s&&this.setValue(e.getAttribute("id"))},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("id",this.getValue())}},{id:"cmbLangDir",type:"select",requiredContent:"img[dir]",style:"width : 100px;",label:t.lang.common.langDir,"default":"",items:[[t.lang.common.notSet,""],[t.lang.common.langDirLtr,"ltr"],[t.lang.common.langDirRtl,"rtl"]],setup:function(t,e){t==s&&this.setValue(e.getAttribute("dir"))},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("dir",this.getValue())}},{type:"text",id:"txtLangCode",requiredContent:"img[lang]",label:t.lang.common.langCode,"default":"",setup:function(t,e){t==s&&this.setValue(e.getAttribute("lang"))},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("lang",this.getValue())}}]},{type:"text",id:"txtGenLongDescr",requiredContent:"img[longdesc]",label:t.lang.common.longDescr,setup:function(t,e){t==s&&this.setValue(e.getAttribute("longDesc"))},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("longDesc",this.getValue())}},{type:"hbox",widths:["50%","50%"],children:[{type:"text",id:"txtGenClass",requiredContent:"img(cke-xyz)",label:t.lang.common.cssClass,"default":"",setup:function(t,e){t==s&&this.setValue(e.getAttribute("class"))},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("class",this.getValue())}},{type:"text",id:"txtGenTitle",requiredContent:"img[title]",label:t.lang.common.advisoryTitle,"default":"",onChange:function(){c(this.getDialog())},setup:function(t,e){t==s&&this.setValue(e.getAttribute("title"))},commit:function(t,e){t==s?(this.getValue()||this.isChanged())&&e.setAttribute("title",this.getValue()):t==r?e.setAttribute("title",this.getValue()):t==m&&e.removeAttribute("title")}}]},{type:"text",id:"txtdlgGenStyle",requiredContent:"img{cke-xyz}",label:t.lang.common.cssStyle,validate:CKEDITOR.dialog.validate.inlineStyle(t.lang.common.invalidInlineStyle),"default":"",setup:function(t,e){if(t==s){var i=e.getAttribute("style");!i&&e.$.style.cssText&&(i=e.$.style.cssText),this.setValue(i);var n=e.$.style.height,a=e.$.style.width,l=(n?n:"").match(g),o=(a?a:"").match(g);this.attributesInStyle={height:!!l,width:!!o}}},onChange:function(){n.call(this,["info:cmbFloat","info:cmbAlign","info:txtVSpace","info:txtHSpace","info:txtBorder","info:txtWidth","info:txtHeight"]),c(this)},commit:function(t,e){t==s&&(this.getValue()||this.isChanged())&&e.setAttribute("style",this.getValue())}}]}]}};CKEDITOR.dialog.add("image",function(e){return t(e,"image")}),CKEDITOR.dialog.add("imagebutton",function(e){return t(e,"imagebutton")})}();