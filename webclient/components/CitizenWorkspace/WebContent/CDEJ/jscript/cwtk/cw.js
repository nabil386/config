/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
var cw = cw || {};
 
require([
	"dojo/ready",
	"cwtk/util/Commons", 
	"cwtk/util/SlidingHandler", 
	"cwtk/util/EventHandler", 
	"cwtk/util/AjaxHandler", 
	"cwtk/util/FormHandler", 
	"cwtk/util/UIMFragment", 
	"cwtk/util/LoadingHandler", 
	"cwtk/util/PopupManager", 
	"cwtk/eligibilityresults/EligibilityResult",
	"cwtk/widget/HoverHelpTooltip", 
    "cwtk/widget/InlineDropDown", 
    "cwtk/widget/Rotator", 
    "cwtk/widget/MenuItem", 
    "cwtk/widget/TitlePane",
    "cwtk/widget/FragmentPane", 
    "cwtk/widget/DropDown",
    "cwtk/widget/ContrastModeChooser"
], function(
	    ready,
	    Commons, 
	    SlidingHandler, 
	    EventHandler, 
	    AjaxHandler, 
	    FormHandler, 
	    UIMFragment, 
	    LoadingHandler, 
	    PopupManager, 
	    EligibilityResult,
	    HoverHelpTooltip){
		
        cw.commons = new Commons();
        cw.sliding = new SlidingHandler();
        cw.event = new EventHandler();
        cw.ajax = new AjaxHandler();
        cw.form = new FormHandler();
        cw.fragment = new UIMFragment();
        cw.loading = new LoadingHandler();
        cw.popup = new PopupManager();
        cw.eligibilityResult = new EligibilityResult();
        HoverHelpTooltip.defaultPosition = ['below'];
        
});
