/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.env.ie&&CKEDITOR.env.version<9&&CKEDITOR.tools.enableHtml5Elements(document),CKEDITOR.config.height=150,CKEDITOR.config.width="auto";var initSample=function(){function e(){return"%REV%"==CKEDITOR.revision?!0:!!CKEDITOR.plugins.get("wysiwygarea")}var t=e(),n=!!CKEDITOR.plugins.get("bbcode");return function(){var e=CKEDITOR.document.getById("editor");n&&e.setHtml("Hello world!\n\nI'm an instance of [url=http://ckeditor.com]CKEditor[/url]."),t?CKEDITOR.replace("editor"):(e.setAttribute("contenteditable","true"),CKEDITOR.inline("editor"))}}();