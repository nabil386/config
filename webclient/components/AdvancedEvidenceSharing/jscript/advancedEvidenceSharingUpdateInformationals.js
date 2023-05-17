/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2020. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
function advancedEvidenceSharingRepositionInformationals(e) {

	var errorMessages = document.getElementById('error-messages-container');
	if(errorMessages){
		errorMessages.remove();
		document.getElementById('mainForm').after(errorMessages);
	}
}
