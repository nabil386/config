var map;

// Starts off a listener for key presses
function wireKeyPressEvent() {
  var inputNode;
  inputNode = dojo.query(".text")[0];
  dojo.connect(inputNode, "onkeypress", function(event){
// Key is set for all browsers
  	var key = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;  
    if(key ==13) {  
// if key is 13 (Enter) Default response will bring to home page.
// 'preventDefault' stops this
// and an invisible button on page is called
    	 document.getElementById('GetDirections').click(); 
    	 event.preventDefault(); 
		}
		return true;
	});  
}

var startFromInputField = document.getElementById("__o3id0");
startFromInputField.onclick = function() { wireKeyPressEvent(); };

function printDirections(mapDivID){
window.print();
};

function printMapDirections(destination, language, width, height){
		var fromAddress = "";
		
    var inputArray = dojo.query('input[type=\"text\"]');        
        if(inputArray.length != 0) {
            fromAddress = inputArray[0].value;
        }
        var left = parseInt((screen.availWidth/2) - (width/2));
    	var top = parseInt((screen.availHeight/2) - (height/2));
    	var windowFeatures = "width=" + width + ",height=" + height + ",status=1,resizable=1,scrollbars=1,left=" + left + ",top=" + top + "screenX=" + left + ",screenY=" + top;
        var url = "http://maps.google.com/maps?f=d&saddr=" + fromAddress + "&daddr=" + destination + "&hl=" + language + "&pw=2";

        printWindow = window.open(url,"",windowFeatures);
};

var getDirections = {



	init: function(mapDivID, latitude, longitude) {
		
            var lat = latitude;	        
	        var lng = longitude;

	    	var mapOptions = {
	    	  zoom: 16,
	    	  // zoomControl: true,
	    	  center: new google.maps.LatLng(lat, lng),
	    	  // mapTypeControl: true,
	    	  // draggableCursor: 'default',
	    	  // scaleControl: true,
	    	  // scrollwheel: true,
	    	  // streetViewControl: true,
	    	  mapTypeId: google.maps.MapTypeId.ROADMAP
	    	};
	    			
            map = new google.maps.Map(document.getElementById(mapDivID), mapOptions); 


	},
	
	setDirections: function(mapDivID, directionsDivID, latitude, longitude, toAddress, locale, calculate_distance_in) {

        // direction variables
		var _directionsService = new google.maps.DirectionsService();
		var _directionsDisplay = new google.maps.DirectionsRenderer();
		var _directionsRequestDetails;
		
        var geocoder = null;
        var addressMarker;
        var userLocale = locale;
        var fromAddress = "";
        var mode = "";

        _directionsDisplay.setMap(map);
        
        var inputArray = dojo.query('input[type=\"text\"]');
       
        
        if(inputArray.length == 1) {
          fromAddress = inputArray[0].value;
        }
        
        var dropdownArray = dojo.query("select");
       
        mode = dropdownArray[0].value;
      

        // MOT10001 is walking
        if(mode == "MOT10001") {
        	mode = google.maps.TravelMode.WALKING;
        } else if(mode == "MOT10000") {
        	// MOT10000 is driving
        	mode = google.maps.TravelMode.DRIVING;
        } else {
        	// the default from Google.
        	mode = google.maps.TravelMode.DRIVING;
        }
        
        dojo.byId(directionsDivID).innerHTML = "";
        

           var directionsPanel = dojo.byId(directionsDivID);
            
 

			geocoder = new google.maps.Geocoder();
			var _geocoderRequest = {
				address:fromAddress
			};
		
			
			if (geocoder) {
				var geocoderResultWithinBounds;
				geocoder.geocode(
					_geocoderRequest,
					function(geocoderResultArray, status) {
						var plottedAnAddress = new Boolean(false);
						
						if (status == google.maps.GeocoderStatus.OK) {
						
							var numPotentialAddress = 0;
							require(["dojo/_base/array"], function(arrayUtil) {
								arrayUtil.forEach(geocoderResultArray, function(geocoderResult, index){
									
									if(latLngBounds.contains(geocoderResult.geometry.location)) {
										geocoderResultWithinBounds = geocoderResult.geometry.location;
										plottedAnAddress = true;
										numPotentialAddress++;
									}	
								});								
							});
							

							
							if(plottedAnAddress == false) {
								alert(ADDRESS_NOT_IN_RANGE.replace('%1s', "\"" + fromAddress + "\""));
							} else if (numPotentialAddress > 1){
								alert(MULTIPLE_ADDRESSES_FOUND.replace('%1s', "\"" + fromAddress + "\""));
							} else {
					            _directionsRequestDetails = {
					            		  origin: geocoderResultWithinBounds,
					            		  destination: toAddress,
					            		  travelMode: mode,
					            		  unitSystem: getDirections._getUnitOfDistance(calculate_distance_in)
					            }
					            

					             _directionsService.route(_directionsRequestDetails, function(response, status) {
					                if (status == google.maps.DirectionsStatus.OK) {
					                  _directionsDisplay.setDirections(response);
					                  _directionsDisplay.setPanel(document.getElementById(directionsDivID));

					                } else if (status == google.maps.DirectionsStatus.ZERO_RESULTS) {
					                  alert(UNKNOWN_ADDRESS.replace('%1s', "\"" + google.maps.DirectionsStatus.ZERO_RESULTS + "\""));
					                } else if (status == google.maps.DirectionsStatus.NOT_FOUND) {
					                  alert(UNKNOWN_ADDRESS.replace('%1s', "\"" + google.maps.DirectionsStatus.NOT_FOUND + "\""));
					                } else if (status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT) {
					                  alert(SERVER_ERROR.replace('%1s', "\"" + google.maps.DirectionsStatus.OVER_QUERY_LIMIT + "\""));
					                } else if (status == google.maps.DirectionsStatus.REQUEST_DENIED) {
					                  alert(MISSING_QUERY.replace('%1s', "\"" + google.maps.DirectionsStatus.REQUEST_DENIED + "\""));
					                } else if (status == google.maps.DirectionsStatus.MAX_WAYPOINTS_EXCEEDED) {
					                  alert(SERVER_ERROR.replace('%1s', "\"" + google.maps.DirectionsStatus.MAX_WAYPOINTS_EXCEEDED + "\""));
					                } else if (status == google.maps.DirectionsStatus.INVALID_REQUEST) {
					              	  alert(BAD_REQUEST.replace('%1s', "\"" + google.maps.DirectionsStatus.INVALID_REQUEST + "\""));
					              	} else {
					                  alert(google.maps.DirectionsStatus.UNKNOWN_ERROR);
					                }
					              });					            
							}							
						}
						
						if (status == google.maps.GeocoderStatus.ZERO_RESULTS) {
							alert(ADDRESS_NOT_FOUND.replace('%1s', "\"" + fromAddress + "\""));
						} 
					} 
				);
			}            
            	

	},
	
	/**
	 * Retrieves the value representing the unit system (metric/Imperial) the
	 * calculated distance is to be displayed in.
	 * 
	 * @param calculate_distance_in
	 *            entry from the codetable, UnitOfDistance to be converted into
	 *            the google maps representation
	 * @return the unit system to be used when displaying the distance and
	 *         directions to the user
	 */
	_getUnitOfDistance: function(calculate_distance_in) {
		var unitOfDistance;
		// Kilometers UOD2001
		// Miles UOD2002
		if(calculate_distance_in == 'UOD2001') {
			unitOfDistance = google.maps.UnitSystem.METRIC;
		} else {
			unitOfDistance = google.maps.UnitSystem.IMPERIAL;
		}
		
		return unitOfDistance;
	},
	
	onGDirectionsLoad: function() {
		


	}

	
    		
}