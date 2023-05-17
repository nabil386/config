/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.plugins.add("divarea",{afterInit:function(e){e.addMode("wysiwyg",function(t){var n=CKEDITOR.dom.element.createFromHtml('<div class="cke_wysiwyg_div cke_reset cke_enable_context_menu" hidefocus="true"></div>'),a=e.ui.space("contents");a.append(n),n=e.editable(n),n.detach=CKEDITOR.tools.override(n.detach,function(e){return function(){e.apply(this,arguments),this.remove()}}),e.setData(e.getData(1),t),e.fire("contentDom")})}});