var directions = "";
var markerList = [];
var toHtmlList = [];
var fromHtmlList = [];
var map = "";

function initGoogleMap(mapID) {

	var bounds = new google.maps.LatLngBounds(new google.maps.LatLng(
			southBound, westBound), new google.maps.LatLng(northBound,
			eastBound));

	// convert the variable, mapZoomLevel, to a number if it already is not a number.
	var zoomLevel;
	if(typeof mapZoomLevel == "string") {
		zoomLevel = parseInt(mapZoomLevel);
	} else {
		zoomLevel = mapZoomLevel;
	}
	
	var mapOptions = {
		zoom : zoomLevel,
		center : bounds.getCenter(),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};

	map = new google.maps.Map(document.getElementById(mapID), mapOptions);

	if (staticMap) {
		var additionalOptions = {
		disableDoubleClickZoom: true,
		draggable: false,
		scaleControl: false,
		panControl: false,
		zoomControl:false,
		scrollwheel: false
		
		}
		map.setOptions(additionalOptions);
	} 
	
	if (!staticMap) {
		document.getElementsByTagName('table')[1].style.display = "none";

		google.maps.event.addListener(map, 'idle', function() {
			setFieldValues();
		});
	}

}
function setFieldValues() {
	
	var mapBounds = map.getBounds();
	document.getElementById('__o3id0').value = mapBounds.getNorthEast()
			.lat();
	document.getElementById('__o3id2').value = mapBounds.getSouthWest()
			.lat();
	document.getElementById('__o3id1').value = mapBounds.getNorthEast()
			.lng();
	document.getElementById('__o3id3').value = mapBounds.getSouthWest()
			.lng();
}