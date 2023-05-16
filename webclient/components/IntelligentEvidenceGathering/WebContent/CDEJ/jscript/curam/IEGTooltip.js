dojo.provide("curam.IEGTooltip");
require(["dijit/TooltipDialog", "dijit/_Templated"]);

dojo.declare("curam.IEGTooltip", [dijit.TooltipDialog, dijit._Templated], { templateString: "<div class=\"dijitTooltipDialog\" tabindex=\"-1\" waiRole=\"alertdialog\" waiState=\"labelledby-${id}_title\">\n<div dojoAttachPoint=\"titleBar\"\nclass=\"dijitTooltipDialogTitleBar\">\n<span dojoAttachPoint=\"titleNode\" class=\"dijitTooltipDialogTitle\" id=\"${id}_title\">${title}</span>\n</div>\n<div dojoAttachPoint=\"containerNode\" class=\"dijitTooltipDialogPaneContent\">\n</div>\n</div>"});