/**
 * 24-Feb-2011  BD  [CR00251957]  Moved PodContainer related functionality to the file
 *                                PodContainer.js
 */

/*  TODO: Check if this function is being used any more.
    Check for an enter event. If the search tool bar has a query invoke the
    search feature.
*/

/*    TODO: Check if this function is being used any more.
      this function defines a popup windows
 */
function popup(mylink, windowname, params ) {
  if (! window.focus) return true;
  var href;
  if (typeof(mylink) == 'string')
     href=mylink;
  else {
     href=mylink.href;
  }
  window.open(href,windowname,params);
  return false;
}

function checkForSearchRequest(e,searchQuery,target){ 
  var characterCode            
  if(e && e.which){
    e = e
    characterCode = e.which
  }
  else{
    e = event
    characterCode = e.keyCode
  }            
  if(characterCode == 13){
    if(searchQuery!=''){
      window.location.replace(target+searchQuery)
    }
    return false
  }
  else{
    return true
  }  
}