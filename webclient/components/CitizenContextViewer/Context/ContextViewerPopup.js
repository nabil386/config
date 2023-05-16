/*
 * Licensed Materials - Property of IBM
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2020. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
function openContextViewer(link, theEvent) {
	// IE doesn't pass event as argument.
	localEvent = theEvent || window.event;

	var newWindow = window
			.open(
					link.href,
					"_blank",
					"resizable=yes,status=no,scrollbars=no,location=no,height=500,width=500,toolbar=no,menubar=no,titlebar=no");

	if ("focus" in window) {
		newWindow.focus();
	}
	
	if ("returnValue" in localEvent) {
		localEvent.returnValue = false;
	} 
	
	// for Chrome and Firefox
	if ("preventDefault" in localEvent) {
		localEvent.preventDefault();
		localEvent.stopPropagation();
	}
	
	// for IE
	if ("cancelBubble" in localEvent) {
		localEvent.cancelBubble = true;
	}
	
	return false;
	
}

function setupPersonSearchEvent() { 
	var personObj = document.getElementById("PersonField");
	dojo.event.connect(personObj, "onchange", this, "onLoadContextViewer");

}

/**
 * Displays the Citizen Context Viewer in a given container.
 *
 * @param {*} citizenContextID 	 DOM component where CCV is rendered. 
 * @param {*} concernRoleID the concern role id
 * @param {*} contextType the context type
 * @param {*} locale the locale
 * @param {*} configuration the color configuration
 */
function showContextViewer(citizenContextID, concernRoleID, contextType,
		locale, configuration) {
	spm.renderers.CitizenContext.CitizenContextViewerRenderer(citizenContextID, concernRoleID, contextType);
}
