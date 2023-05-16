dojo.ready(function(){
    
    var elements = dojo.query("body.CITWSAPP.high-contrast");
    if(elements.length > 0) {
		//CW app is in high contrast mode
		var topBody = window.top.document.body;
		if(!dojo.hasClass(topBody, "high-contrast")) {
			dojo.addClass(topBody, "high-contrast");
		}		    
    } 
    
});