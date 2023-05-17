var caseID = "";
var applicationID = "";

function onLoadHandler() {
    
   //to prevent the page reloading while in modal window.   
   if (typeof(jsScreenContext) != "undefined"
       && jsScreenContext.hasContextBits('MODAL')) {
     return;
   }
    	
        
   // obtain caseid
   caseID=getUrlVars()["caseID"];
   if (caseID==undefined) {
    caseID = 0;
   }   

   // obtain applicationid
   applicationID=getUrlVars()["applicationID"];  
   if (applicationID==undefined) {
     applicationID = 0;
   }
        
   
   var servletURL =
     '/Curam/servlet/PathResolver' + 
     "?r=j&p=" + "/smartpanel/data/creoleprogramrecommendation/ping[" + 
     caseID + "]["+ applicationID +"]";
			
   dojo.xhrPost({
     url: servletURL,
     headers: { "Content-Type": "text/json" },
     handleAs: 'json',
     load: function(response, args) {
	   
     if (response.progRecInProgressInd == "true") {
       ping(); 
     }
    },
    error: function(error, args) {
     //TODO Need to handle the error efficiently
    }
  });

}

/*
* Obtain page parameters.
*/
function getUrlVars() {

  var vars = [], hash;
  var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
  for(var i = 0; i < hashes.length; i++) {
    hash = hashes[i].split('='); 
    vars.push(hash[0]); 
    vars[hash[0]] = hash[1];
  }
  return vars; 
}

function ping() {
              
	 var servletURL =
	      '/Curam/servlet/PathResolver' + 
		    "?r=j&p=" + "/smartpanel/data/creoleprogramrecommendation/ping[" + 
		    caseID + "]["+ applicationID +"]";
		
	  dojo.xhrPost({
		  url: servletURL,
			headers: { "Content-Type": "text/json" },
			handleAs: 'json',
			load: function(response, args) {
	   
  	    if (response.progRecInProgressInd == "true") {
	  	    var t=setTimeout("ping()",5000);

		    } else {
		      location.reload(true); 
		    }
		  },
		    error: function(error, args) {
	      //TODO Need to handle the error efficiently
		  }
		});
}
