//This method validates at least one radio button selection 
function validateRadioButtonSelection(message)
{
   for(i=0; i <document.forms[0].elements.length; i++) {
     type = document.forms[0].elements[i].type;
     if ((type == "radio") && (document.forms[0].elements[i].checked)) {
          return true;
     }
   }
   
   alert(message);
   return false;
}

//This method validates at least one checkbox button selection 
function validateCheckBoxSelection(message)
{
   for(i=0; i <document.forms[0].elements.length; i++) {
     type = document.forms[0].elements[i].type;
     if ((type == "checkbox") && (document.forms[0].elements[i].checked)) {
          return true;
     }
   }
   
   alert(message);
   return false;
}
