//calculate the time before calling the function in window.onload
beforeload = (new Date()).getTime();
function pageloadingtime()
{
 
        //calculate the current time in afterload 
        afterload = (new Date()).getTime();
        // now use the beforeload and afterload to calculate the seconds
        secondes = (afterload-beforeload)/1000;
        // If necessary update in window.status
        window.status='You Page Load took  ' + secondes + ' second(s).';
        // Place the seconds in the innerHTML to show the results
        document.getElementById("loadingtime").innerHTML = "<font color='red'>(This Page Load took " + secondes + " second(s).)</font>";
        
}
  
window.onload = pageloadingtime;