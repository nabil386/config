﻿/**
 * @license Copyright (c) 2003-2016, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */
CKEDITOR.dialog.add("a11yHelp",function(e){function t(e){for(var t,a,n=[],l=0;l<r.length;l++)a=r[l],t=e/r[l],t>1&&2>=t&&(e-=a,n.push(i[a]));return n.push(i[e]||String.fromCharCode(e)),n.join("+")}function a(){for(var e='<div class="cke_accessibility_legend" role="document" aria-labelledby="'+l+'_arialbl" tabIndex="-1">%1</div><span id="'+l+'_arialbl" class="cke_voice_label">'+n.contents+" </span>",t="<h1>%1</h1><dl>%2</dl>",a="<dt>%1</dt><dd>%2</dd>",i=[],r=n.legend,o=r.length,s=0;o>s;s++){for(var p=r[s],g=[],u=p.items,f=u.length,m=0;f>m;m++){var h=u[m],b=h.legend.replace(c,d);b.match(c)||g.push(a.replace("%1",h.name).replace("%2",b))}i.push(t.replace("%1",p.name).replace("%2",g.join("")))}return e.replace("%1",i.join(""))}var n=e.lang.a11yhelp,l=CKEDITOR.tools.getNextId(),i={8:n.backspace,9:n.tab,13:n.enter,16:n.shift,17:n.ctrl,18:n.alt,19:n.pause,20:n.capslock,27:n.escape,33:n.pageUp,34:n.pageDown,35:n.end,36:n.home,37:n.leftArrow,38:n.upArrow,39:n.rightArrow,40:n.downArrow,45:n.insert,46:n["delete"],91:n.leftWindowKey,92:n.rightWindowKey,93:n.selectKey,96:n.numpad0,97:n.numpad1,98:n.numpad2,99:n.numpad3,100:n.numpad4,101:n.numpad5,102:n.numpad6,103:n.numpad7,104:n.numpad8,105:n.numpad9,106:n.multiply,107:n.add,109:n.subtract,110:n.decimalPoint,111:n.divide,112:n.f1,113:n.f2,114:n.f3,115:n.f4,116:n.f5,117:n.f6,118:n.f7,119:n.f8,120:n.f9,121:n.f10,122:n.f11,123:n.f12,144:n.numLock,145:n.scrollLock,186:n.semiColon,187:n.equalSign,188:n.comma,189:n.dash,190:n.period,191:n.forwardSlash,192:n.graveAccent,219:n.openBracket,220:n.backSlash,221:n.closeBracket,222:n.singleQuote};i[CKEDITOR.ALT]=n.alt,i[CKEDITOR.SHIFT]=n.shift,i[CKEDITOR.CTRL]=n.ctrl;var r=[CKEDITOR.ALT,CKEDITOR.SHIFT,CKEDITOR.CTRL],c=/\$\{(.*?)\}/g,d=function(){var a=e.keystrokeHandler.keystrokes,n={};for(var l in a)n[a[l]]=l;return function(e,a){return n[a]?t(n[a]):e}}();return{title:n.title,minWidth:600,minHeight:400,contents:[{id:"info",label:e.lang.common.generalTab,expand:!0,elements:[{type:"html",id:"legends",style:"white-space:normal;",focus:function(){this.getElement().focus()},html:a()+'<style type="text/css">.cke_accessibility_legend{width:600px;height:400px;padding-right:5px;overflow-y:auto;overflow-x:hidden;}.cke_browser_quirks .cke_accessibility_legend,{height:390px}.cke_accessibility_legend *{white-space:normal;}.cke_accessibility_legend h1{font-size: 20px;border-bottom: 1px solid #AAA;margin: 5px 0px 15px;}.cke_accessibility_legend dl{margin-left: 5px;}.cke_accessibility_legend dt{font-size: 13px;font-weight: bold;}.cke_accessibility_legend dd{margin:10px}</style>'}]}],buttons:[CKEDITOR.dialog.cancelButton]}});