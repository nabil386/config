
function initGoogleMap(mapID) {

	initialiseMap(mapID, addressData[0].latitude, addressData[0].longitude,
			zoomLevel);

	// for each address, create a marker for it and customise it, if required
	for ( var i = 0; i < addressData.length; i++) {

		var currentAddress = addressData[i].address;
		var currentIconLocation = addressData[i].iconLocation;
		var point = new google.maps.LatLng(addressData[i].latitude,
				addressData[i].longitude);
		var providerName = addressData[i].providerName;
		var markerID = addressData[i].id;
		var category = addressData[i].category;

		var infoWindowHtml = "";

		if (providerName.length !== 0) {
			infoWindowHtml = "<b>" + providerName + "</b><br>";
		}

		if (currentAddress.length != 0) {
			infoWindowHtml = infoWindowHtml + currentAddress;
		}

		var addressIcon = {
				url : currentIconLocation,
				size : new google.maps.Size(20, 20),
				origin : new google.maps.Point(0, 0)
		};

		var markerOptions = {
		    position : point,
			icon : addressIcon,
			title : currentAddress,
			map : map
		};

		createAndAddMapMarker(markerID, infoWindowHtml, markerOptions, true, category);
	}
}

function openCenteredWindow(url, inputHeight, inputWidth) {
	var width = inputWidth;
	var height = inputWidth;
	var left = parseInt((screen.availWidth / 2) - (width / 2));
	var top = parseInt((screen.availHeight / 2) - (height / 2));
	var windowFeatures = "width=" + width + ",height=" + height
			+ ",status,resizable,left=" + left + ",top=" + top + "screenX="
			+ left + ",screenY=" + top;
	myWindow = window.open(url, "providerModal", windowFeatures);
}