/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
function finishProgramActionAndReturn(contextPath) {
    var backgroundDiv = window.parent.dijit.byId('background-panel');
    var redirectURL = contextPath + '/CitizenWorkspace_eligibilityResultsUpdateAndReturnPage.do';
    backgroundDiv.setHref(redirectURL); 
}
