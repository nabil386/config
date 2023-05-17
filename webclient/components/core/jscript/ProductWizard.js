/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
function disableProductType(typeCodeCombo){
var selectedVal =document.getElementById('__o3id2').value;
var otherType="Other";

//Disable the Other Textbox,If Other is not selected.
if(selectedVal != otherType){
 document.getElementById('__o3id4').value="";
 document.getElementById('__o3id4').disabled=true
}
else{
 document.getElementById('__o3id4').disabled=false;
}

}