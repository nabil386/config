
// generic map vars
var map = "";

var zoomLevel="";

//  array to hold gmarkers to track them
var gmarkers = {};

// our implementation specific vars
var addressData=new Array();

// initialises a google map
// mapID - reference to the map DIV
// centreLatitude - long
// centreLongitude - long
// zoomLevel - int - max 16
function initialiseMap(mapID, centreLatitude, centreLongitude, zoomLevel) {


    if (zoomLevel.length == 0) {
      zoomLevel = 16;
	}
	  
	var mapOptions = {
      zoom: zoomLevel,
      //zoomControl: true,
      center: new google.maps.LatLng(centreLatitude, centreLongitude),
     // mapTypeControl: true,
     // draggableCursor: 'default',
     // scaleControl: true,
     // scrollwheel: true,
     // streetViewControl: true,
	  mapTypeId: google.maps.MapTypeId.ROADMAP
    };
	
	map = new google.maps.Map(document.getElementById(mapID), mapOptions); 
}

/*
 * Removes the markers that exist on the map from it.
 */
function clearMarkers() {
	for (var key in gmarkers ){
		gmarkers[key].setMap(null);	
	}	
}


/*
* Shows any markers in the gmarkers that have been cleared from the map.
*/
function showMarkers() {
	for (var key in gmarkers ){
		if (!gmarkers[key].getMap()){
			gmarkers[key].setMap(map);	
		}
	}
}

function createAndAddMapMarker(markerID, infoWindowHtml, markerOptions, displayOnLoad, category) {
    // Create the marker

    var marker =  new google.maps.Marker(markerOptions);
    var isThisMarkerClickable = marker.getClickable();

    // hide the marker if the category is not selected
    if(!displayOnLoad) {
    	marker.setVisible(false);
    }
    
    var markerShapeIs = marker.getShape();
    
    
    // When the marker is clicked, open an information window.
	google.maps.event.addListener(marker, 'click', markerClickListener);
	
	function markerClickListener() {
		// check to see if the marker is already open.
		// if it is already open, then there is no need to do anything.
		if (gmarkers[marker.id].isOpen){
			return;
		}
		
		var infowindow = new google.maps.InfoWindow({
		    content: infoWindowHtml
		  });
		  
		  infowindow.marker = marker;
		  
		  	google.maps.event.addListener(infowindow, 'closeclick', function() {
				 gmarkers[infowindow.marker.id].isOpen = false;
			});
			
		
		// this should be the marker.
		gmarkers[marker.id].isOpen = true;
        infowindow.open(map,this);
	};   
    
    // add marker to array
	marker.category = category;
	marker.isOpen = false;
	marker.id = markerID;
  	gmarkers[markerID] = marker;
  
    return marker;
}

