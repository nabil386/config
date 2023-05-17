/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2016, 2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 * Modification History
 * --------------------
 * 19-June-2017 JD [RTC199030] Refactored all dojo calls to use AMD modules.
 * 23-October-2014 ROR [] Refactored to AMD module.
 */

/**
 * Collections of functions related to field level help.
 */
define(["dojo/dom",
"dijit/registry",
"dojo/dom-style",
"dijit/popup",
"dijit/focus",
"dojo/_base/lang",
"dojo/dom-attr"
],
    function(dom, registry, domStyle, popup, focusUtil, lang, domAttr) {
    // PUBLIC METHODS...
    return {
      /**
       * Show Field Level Help.
       *
       * Show the field Level Help dialog on hover.
       *
       * @param dialogId  The dialogs Id
       * @param parentId  The parent Id
       */
      showHelpDialog: function(dialogId, parentId) {
        var dialog = registry.byId(dialogId);
        var currentStyle = domStyle.get(dialogId);

        if (currentStyle.display == "none") {
          domStyle.set(dialogId,'width', '350px');
          domStyle.set(dialogId,'overflow-x', 'visible');
          domStyle.set(dialogId, 'display', 'block');

          popup.open({
            popup: dialog,
            around: dom.byId(parentId),
            onExecute: function(){
              popup.close(dialog);
              domStyle.set(dialogId, 'display', 'none');
              focusUtil.focus(dom.byId(parentId));
            },
            onCancel: function(){
              popup.close(dialog);
              domStyle.set(dialogId, 'display', 'none');
              focusUtil.focus(dom.byId(parentId));
            },
            onClose: function() {
              focusUtil.focus(dom.byId(parentId));
            }
          })
          setTimeout(lang.hitch(dialog, "focus"), 0);
        } else {

          popup.close(dialog);
          domStyle.set(dialogId, 'display', 'none');
          focusUtil.focus(dom.byId(parentId));
        }
      },

      /**
       * Close a field level help dialog.
       *
       * @param dialogId  The id of the dialog to close.
       */
      closeHelpDialog: function(dialogId) {

        var dialog = registry.byId(dialogId);
        popup.close(dialog);
        domAttr.remove(dialogId, 'style');
      }
    };
});
