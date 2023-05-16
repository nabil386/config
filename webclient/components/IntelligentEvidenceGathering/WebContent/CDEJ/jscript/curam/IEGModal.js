dojo.provide("curam.IEGModal");
require(["dijit/Dialog"]);

dojo.declare("curam.IEGModal", dijit.Dialog, { templateString: "<div class=\"dijitDialog\" tabindex=\"-1\" waiRole=\"alertdialog\" waiState=\"labelledby-${id}_title\">\n<div dojoAttachPoint=\"titleBar\"\nclass=\"dijitDialogTitleBar\">\n<span dojoAttachPoint=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\">${title}</span>\n</div>\n<div dojoAttachPoint=\"containerNode\" class=\"dijitDialogPaneContent\">\n</div>\n</div>"});