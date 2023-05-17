/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
function convertCheckBoxesToRadios_onFirstLoad(e) {

  var checkboxes = document.querySelectorAll('input[type="checkbox"]');
  for(var i = 0; i < checkboxes.length; i++){
	  checkboxes[i].type = 'radio';
  }
}

function deselectOtherRadios_onClick(checkedRadioButton) {
	var radioButtons = document.querySelectorAll('input[type="radio"]');
	for(var i = 0; i < radioButtons.length; i++){
		var radioButton = radioButtons[i];
		if(radioButton !== checkedRadioButton){
			radioButton.checked = false;
			radioButton.setAttribute('aria-checked', false);
		}
	}
}