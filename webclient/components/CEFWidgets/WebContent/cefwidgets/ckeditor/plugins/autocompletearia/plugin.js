﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
"use strict";!function(){function t(t,e){this.ariaElement=null,this.opened=null,this.lang=e.lang.autocompletearia,this.langTemplates={resultsPlural:new CKEDITOR.template(this.lang.resultsPlural),listLength:new CKEDITOR.template(this.lang.listLength)},this.timeoutId=null,this.attach(t)}CKEDITOR.plugins.add("autocompletearia",{lang:"en",beforeInit:function(t){t.on("autoCompleteAttached",function(t){t.data.ariaHelper=new CKEDITOR.plugins.autocompletearia(t.data.view,t.editor)})}}),t.prototype={attach:function(t){var e=new CKEDITOR.dom.element("span",CKEDITOR.document);e.setAttributes({"aria-live":CKEDITOR.env.ie?"assertive":"polite","aria-atomic":!0,role:"status"}),e.setStyles({border:0,clip:"rect(0 0 0 0)",height:"1px",margin:"-1px",overflow:"hidden",padding:"0",position:"absolute",width:"1px"}),this.ariaElement=e,CKEDITOR.document.getBody().append(this.ariaElement),t.on("opened",this.onListOpened,this),t.on("closed",this.onListClosed,this),t.on("updated",this.onListUpdated,this),t.on("selected",this.onItemSelected,this)},onListOpened:function(t){this.opened||(this.opened=!0,this.updateStatus(this.getOpenInfo(t.data.size)))},onListClosed:function(){this.opened=!1},onListUpdated:function(t){this.opened&&this.updateStatus(this.getUpdateInfo(t.data.size))},onItemSelected:function(t){this.opened&&this.updateStatus(this.getSelectedItemInfo(t.data.text,t.data.index,t.data.size))},updateStatus:function(t){this.timeoutId&&clearTimeout(this.timeoutId),this.timeoutId=CKEDITOR.tools.setTimeout(function(){this.ariaElement.setText(t+".")},250,this)},getOpenInfo:function(t){return this.lang.label+", "+this.getResultsLengthInfo(t)+", "+this.lang.navigationInfo},getUpdateInfo:function(t){return this.lang.label+" "+this.lang.updated+", "+this.getResultsLengthInfo(t)},getSelectedItemInfo:function(t,e,n){return t+" "+this.lang.selected+", "+this.langTemplates.listLength.output({selected:e,size:n})},getResultsLengthInfo:function(t){var e="";return e=0===t?this.lang.noResults:1==t?this.lang.resultsSingular:this.langTemplates.resultsPlural.output({size:t})}},CKEDITOR.plugins.autocompletearia=t}();