/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
!function(e){if("undefined"==typeof e)throw new Error("jQuery should be loaded before CKEditor jQuery adapter.");if("undefined"==typeof CKEDITOR)throw new Error("CKEditor should be loaded before CKEditor jQuery adapter.");CKEDITOR.config.jqueryOverrideVal="undefined"==typeof CKEDITOR.config.jqueryOverrideVal?!0:CKEDITOR.config.jqueryOverrideVal,e.extend(e.fn,{ckeditorGet:function(){var e=this.eq(0).data("ckeditorInstance");if(!e)throw"CKEditor is not initialized yet, use ckeditor() with a callback.";return e},ckeditor:function(t,n){if(!CKEDITOR.env.isCompatible)throw new Error("The environment is incompatible.");if(!e.isFunction(t)){var r=n;n=t,t=r}var i=[];n=n||{},this.each(function(){var r=e(this),o=r.data("ckeditorInstance"),a=r.data("_ckeditorInstanceLock"),d=this,u=new e.Deferred;i.push(u.promise()),o&&!a?(t&&t.apply(o,[this]),u.resolve()):a?o.once("instanceReady",function(){setTimeout(function(){return o.element?(o.element.$==d&&t&&t.apply(o,[d]),void u.resolve()):void setTimeout(arguments.callee,100)},0)},null,null,9999):((n.autoUpdateElement||"undefined"==typeof n.autoUpdateElement&&CKEDITOR.config.autoUpdateElement)&&(n.autoUpdateElementJquery=!0),n.autoUpdateElement=!1,r.data("_ckeditorInstanceLock",!0),o=e(this).is("textarea")?CKEDITOR.replace(d,n):CKEDITOR.inline(d,n),r.data("ckeditorInstance",o),o.on("instanceReady",function(n){var i=n.editor;setTimeout(function(){if(!i.element)return void setTimeout(arguments.callee,100);if(n.removeListener(),i.on("dataReady",function(){r.trigger("dataReady.ckeditor",[i])}),i.on("setData",function(e){r.trigger("setData.ckeditor",[i,e.data])}),i.on("getData",function(e){r.trigger("getData.ckeditor",[i,e.data])},999),i.on("destroy",function(){r.trigger("destroy.ckeditor",[i])}),i.on("save",function(){return e(d.form).submit(),!1},null,null,20),i.config.autoUpdateElementJquery&&r.is("textarea")&&e(d.form).length){var o=function(){r.ckeditor(function(){i.updateElement()})};e(d.form).submit(o),e(d.form).bind("form-pre-serialize",o),r.bind("destroy.ckeditor",function(){e(d.form).unbind("submit",o),e(d.form).unbind("form-pre-serialize",o)})}i.on("destroy",function(){r.removeData("ckeditorInstance")}),r.removeData("_ckeditorInstanceLock"),r.trigger("instanceReady.ckeditor",[i]),t&&t.apply(i,[d]),u.resolve()},0)},null,null,9999))});var o=new e.Deferred;return this.promise=o.promise(),e.when.apply(this,i).then(function(){o.resolve()}),this.editor=this.eq(0).data("ckeditorInstance"),this}}),CKEDITOR.config.jqueryOverrideVal&&(e.fn.val=CKEDITOR.tools.override(e.fn.val,function(t){return function(n){if(arguments.length){var r=this,i=[],o=this.each(function(){var r=e(this),o=r.data("ckeditorInstance");if(r.is("textarea")&&o){var a=new e.Deferred;return o.setData(n,function(){a.resolve()}),i.push(a.promise()),!0}return t.call(r,n)});if(i.length){var a=new e.Deferred;return e.when.apply(this,i).done(function(){a.resolveWith(r)}),a.promise()}return o}var d=e(this).eq(0),u=d.data("ckeditorInstance");return d.is("textarea")&&u?u.getData():t.call(d)}}))}(window.jQuery);