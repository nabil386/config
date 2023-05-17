dojo.require("dijit.Dialog");

var geocoder = null;

var screeningID = "";

var applicationID = "";

var screeningSelected = "";

var homeMarker;

var addressEnteredMarker;

var mapDivID;

var tabIndex = 0;

var windowListenerAdded = false;
// //////////////////////////////////////////////////////////
// CODE TO HIJACK HITTING ENTER ON THE ADDRESS INPUT FIELD //
// //////////////////////////////////////////////////////////
var addressField = dojo.byId("addressField");
dojo.connect(addressField, "onkeydown", function(event) {	
	if (event.keyCode == dojo.keys.ENTER) {
		centreOnAddress(addressField.value);
		dojo.stopEvent(event);
	}
});


function addressObject() {
	this.id = '';
	this.address = '';
	this.latitude = '';
	this.longitude = '';
	this.iconLocation = '';
	this.iconWidth = '';
	this.iconHeight = '';
	this.links = '';
	this.serviceID = '';
	this.hasMultipleLocations = '';
	this.category = '';
	this.providerName = '';
	this.phoneNumber = '';
	this.webAddress = '';
}

// this function is called when a checkbox is clicked. it determines
// whenter the box was checked or unchecked and calls the relevant method
function boxclick(box, category) {
	if (box.checked) {
		show(category);
	} else {
		hide(category);
	}
}

// shows all markers of a particular category, and ensures the checkbox is
// checked
function show(category) {
	getMapData();
	for (var key in gmarkers){
		if (gmarkers[key].category === category){
			gmarkers[key].setVisible(true);
		}
	}
	
	// check the checkbox
	document.getElementById(category + "box").checked = true;
}

// hides all markers of a particular category, and ensures the checkbox is
// cleared
function hide(category) {
	for (var key in gmarkers) {
		if (gmarkers[key].category === category){
			// the visibility incase the min-zoom is exceeded and re-entered.
			gmarkers[key].setVisible(false);
			// close the info window, in case it's open on a marker that we just hid.
			gmarkers[key].setMap(null);
			
			delete gmarkers[key];
		}
	}
	// clear the checkbox
	document.getElementById(category + "box").checked = false;
}

function getMapData() {
	
	if (checkMinZoomExceeded() === true) {
		return;
	}
	if (homeMarker) {
		var markerOptions = {
				map: map	
		};
		homeMarker.setOptions(markerOptions);
	}
	
	var zoomValue = map.getZoom();
	// this needs to be moved to a listener
	var n = map.getBounds().getNorthEast().lat();
	var s = map.getBounds().getSouthWest().lat();
	var e = map.getBounds().getNorthEast().lng();
	var w = map.getBounds().getSouthWest().lng();

	var addressData = new Array();

	var xhrArgs = {

		url : "../servlet/MapProviders?" + "n=" + n + "&s=" + s + "&e=" + e
				+ "&w=" + w,
		handleAs : "json",

		timeout : 50000,

		load : function(response, ioArgs) {

			var referenceOrder = new Array();
			var inputs = dojo.doc.getElementsByTagName("input");

			for ( var i = 0; i < inputs.length; i++) {
				if (dojo.attr(inputs[i], "listitemnumber")) {
					referenceOrder[dojo.attr(inputs[i], "reference")] = dojo
							.attr(inputs[i], "listitemnumber");
				}
			}

			for ( var i = 0; i < response.items.length; i++) {

				if (referenceOrder[response.items[i].category] > 0) {

					var addressDetails = new addressObject();
					
					addressDetails.id = response.items[i].id;
					addressDetails.providerName = response.items[i].name;
					addressDetails.address = response.items[i].address;
					addressDetails.iconLocation = "../Images/service_pins/pins_no_"
							+ referenceOrder[response.items[i].category]
							+ ".png";
					// addressDetails.iconHeight = response.items[i].iconHeight;
					// addressDetails.iconWidth = response.items[i].iconWidth;
					addressDetails.iconHeight = "23";
					addressDetails.iconWidth = "19";
					addressDetails.latitude = response.items[i].lat;
					addressDetails.longitude = response.items[i].lng;
					addressDetails.serviceID = response.items[i].serviceID;
					addressDetails.hasMultipleLocations = response.items[i].hasMultipleLocations;
					addressDetails.category = response.items[i].category;
					addressDetails.phoneNumber = response.items[i].phone;
					addressDetails.webAddress = response.items[i].web;

					addressData.push(addressDetails);
				}
			}

			
			plotPoints(addressData);	
			
			return response;
		},

		error : function(response, ioArgs) {
			console.error(HTTP_STATUS_CODE.replace('%1s', "\""
					+ ioArgs.xhr.status + "\""));
			return response;
		}
	};

	var deferred = dojo.xhrGet(xhrArgs);
	return deferred;

}

function plotPoints(addressData) {
	
	var checkedCategories = new Array();
	var inputs = dojo.doc.getElementsByTagName("input");

	for ( var i = 0; i < inputs.length; i++) {

		if (inputs[i].checked == true) {
			checkedCategories.push(dojo.attr(inputs[i], "reference"));
		}
	}

	// for each address, create a marker for it and customise it, if required
	for ( var j = 0; j < addressData.length; j++) {
		// check to see if the marker is already in the hash map of markers,
		if (!gmarkers[[addressData[j].id]]){	
			// the marker is not in the hashmap so we need to create it and add it to the map.
			var currentAddress = addressData[j].address;
			var currentIconLocation = addressData[j].iconLocation;
			var point = new google.maps.LatLng(addressData[j].latitude,
				addressData[j].longitude);
			var providerName = addressData[j].providerName;
			var serviceID = addressData[j].serviceID;
			var hasMultipleLocations = addressData[j].hasMultipleLocations;
			var category = addressData[j].category;
			var phoneNumber = addressData[j].phoneNumber;
			var webAddress = addressData[j].webAddress;
			var markerID = addressData[j].id;

			var addressIcon;
			var addressWidth = convertStringToNumber(addressData[j].iconWidth);
			var addressHeight = convertStringToNumber(addressData[j].iconHeight);
		
			if (currentIconLocation.length != 0) {
				addressIcon = {
					url : currentIconLocation,
					size : new google.maps.Size(addressWidth,
							addressHeight),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(7, 23)
				};
			}

			var shadowIcon = {
				url : '../Images/pin_number_shadow.png',
				size : new google.maps.Size(34, 24),
				origin : new google.maps.Point(0, 0),
				anchor : new google.maps.Point(7, 23)
			};

			var markerOptions;
		
			if (addressIcon) {
				var shapeDetails = {
					coord : [0, 0,
				    	     addressWidth,addressHeight],
					type : 'rect'
				};
				markerOptions = {
					position : point,
					icon : addressIcon,
					shadow: shadowIcon,
					shape : shapeDetails,
					title : currentAddress,
					map : map
				};

		} else {
			markerOptions = {
				position : point,
				title : currentAddress,
				map : map
			};
		}

		var displayOnLoad = (dojo.indexOf(checkedCategories, category) > -1);

		createAndAddMapMarker(markerID, getInfoWindowHTML(providerName,phoneNumber,currentAddress,webAddress,serviceID,hasMultipleLocations), markerOptions, displayOnLoad, category);
		} 
		else {
			// the marker is already in the hashmap so we just have to show it on the map.
			showMarkers();
		}
	}
}


/*
* Puts together the HTML for the infowindow.
*/
function getInfoWindowHTML(providerName,phoneNumber,currentAddress,webAddress,serviceID,hasMultipleLocations){
	var infoWindowHtml = "";
	
	if (providerName.length != 0) {
		infoWindowHtml = "<b>" + providerName + "</b><br>";
	}

	if (currentAddress.length != 0) {

	// parse the address to clean it up - put first line on an
	// individual line
	var addressParsed = currentAddress.replace(',', '<br>');
	infoWindowHtml = infoWindowHtml + addressParsed;
	if (phoneNumber) {
		infoWindowHtml = infoWindowHtml + '<br>' + phoneNumber;
	}
	if (webAddress) {
		infoWindowHtml = infoWindowHtml
			+ '<br><a class="google-bubble-link" href="javascript: void(0)" onclick="window.open(\''
			+ webAddress + '\');">' + webAddress + "</a>"
	}
	infoWindowHtml = infoWindowHtml
			+ '<br><br/><a class="google-bubble-link" href="javascript: void(0)" onclick="dojo.stopEvent(arguments[0]); openCenteredWindow(\'../cw/PlayerPage.do?id=' + pagePlayerID +'&page=ProviderServiceModal&serviceID='
			+ serviceID + '&hasMultipleLocations='
			+ hasMultipleLocations + '\', \'520\', \'975\');">'
			+ MORE_INFO_LINK_TEXT + '</a>'
	}
			
	return infoWindowHtml;
}

function convertStringToNumber(objectToConvert) {
	
	if(typeof objectToConvert == "string") {
		return parseInt(objectToConvert);
	}
	
	return objectToConvert
}

function getServiceRefOrder() {

	var inputs = dojo.doc.getElementsByTagName("input");

	for ( var i = 0; i < inputs.length; i++) {
		if (dojo.attr(inputs[i], "listitemnumber")) {
			referenceOrder[dojo.attr(inputs[i], "reference")] = dojo.attr(
					inputs[i], "listitemnumber");
		}
	}

}

function initGoogleMap(mapID) {

	mapDivID = mapID;
	// Check whether the government services are being displayed.
	// If not, make the map area larger
	var govServicesDiv = dojo.byId('gov_services_div_expanded');

	if (!govServicesDiv) {
		dojo.query('.recommenations-wrapper').forEach(
				function(node, index, arr) {
					node.style.paddingRight = '1px';
				});
		dojo.byId('curam_map_div_id').style.width = '100%';
	}

	if (geocodeRequired) {
		geocoder = new google.maps.Geocoder();

		var plottedAnAddress = false;

		var geocoderRequestObject = {
			address : userAddress
		};

		geocoder.geocode(geocoderRequestObject,
				function(geoResults, geoStatus) {

					if (status == google.maps.GeocoderStatus.OK) {

						var numPotentialAddress = 0;
						var point;

						for ( var i = 0; i < result.Placemark.length; i++) {

							point = results[0].geometry.location;

							if (latLngBounds.containsLatLng(point)) {

								plottedAnAddress = true;
								numPotentialAddress++;
							}
						}
						if (plottedAnAddress == true
								&& numPotentialAddress == 1) {
							centerLatitude = point.lat();
							centerLongitude = point.lng();
						}
					}
				});
	}

	initialiseMap(mapID, centerLatitude, centerLongitude, zoomLevel);

	// add the Go button to the tabindex
	var index = dojo.byId("addressField").tabIndex;
	if (index === tabIndex){
		tabIndex = index;
		dojo.byId('goButton').tabIndex = index += 1;
	}

	// add event listeners
	google.maps.event.addListener(map, 'idle', function() {
		getMapData();
		checkMinZoomExceeded();
	});
	
	// Once the windowlistener has been set, there is no need to ever set it again.
	// Multiple 'resize' listeners will result in poor preformance.
	if (windowListenerAdded === false){
		// if the zoom level is changed, lets get a fresh map.
		// This will ensure that the images for our markers are not duplicated on the map.
		google.maps.event.addDomListener(window, "resize", function() {
			clearMarkers();
			// note the current zoom level of the map, it should be used for the new map.
			zoomLevel = map.getZoom();
			
			// take note of where we are on the map.
			var center = map.getCenter();
			
			delete map;		
		
			// build a new map that will work at this magnification level.
			initGoogleMap(mapDivID);
 			google.maps.event.trigger(map, "resize");
 			map.setCenter(center); 
		});
	}
	// Adding multiple 'resize' listeners to the window object will slow down the browser, 
	// so set this flag so that no more listeners will get added.
	windowListenerAdded = true;
	
	// add a home icon if we have an address for it
	if (userAddress.length > 0 && plottedAnAddress == true) {

		var addressIcon = {
			url : '../Images/pin_location.png',
			size : new google.maps.Size(23, 19)
		}

		var opts = {
			position : new google.maps.LatLng(centerLatitude, centerLongitude),
			icon : addressIcon,
			map : map
		};

		homeMarker = new google.maps.Marker(opts);

		google.maps.event.addListener(homeMarker, 'click', function() {
			var infowindow = new google.maps.InfoWindow({
				content : userAddress
			});
			infowindow.open(map, homeMarker);
		});

	}

}

function checkMinZoomExceeded() {
	if (map.getZoom() < pinsMinZoomLevel) {
		dojo.byId(zoomWarningDivID).style.display = "";
		clearMarkers();
		return true;
	} else {
		dojo.byId(zoomWarningDivID).style.display = "none";
	}
	
	return false;
}

function centreOnAddress(addressEntered) {

	
	geocoder = new google.maps.Geocoder();
	var _geocoderRequest = {
		address:addressEntered
	};
	
	var geocoderResultWithinBounds;
	var addressTitle;
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
											addressTitle = geocoderResult.formatted_address;
											plottedAnAddress = true;
											numPotentialAddress++;
										}	
									});								
								});
								if (plottedAnAddress == false) {

									var myDialog = dijit
											.byId('message-container-div');

									var messageText = ADDRESS_NOT_IN_RANGE
											.replace('%1s', "\""
													+ addressEntered + "\"");

									dojo.byId('message-div').innerHTML = messageText
											.toString();

									myDialog.show();
								} else if (numPotentialAddress > 1) {

									var myDialog = dijit
											.byId('message-container-div');

									var messageText = MULTIPLE_ADDRESSES_FOUND
											.replace('%1s', "\""
													+ addressEntered + "\"");

									dojo.byId('message-div').innerHTML = messageText
											.toString();

									myDialog.show();
								} else {
									map.setCenter(geocoderResultWithinBounds, map.getZoom());

									// need to add the icon to show a user that
									// we've found the address entered
									var addressIcon = {
										url : "../Images/pin_location.png",
										size : new google.maps.Size(19, 23),
									    origin : new google.maps.Point(0, 0),
									    anchor : new google.maps.Point(7, 23)									
									};
									
									var shadowIcon = {
											url : '../Images/pin_number_shadow.png',
											size : new google.maps.Size(34, 24),
											origin : new google.maps.Point(0, 0),
											anchor : new google.maps.Point(7, 23)
								    };		
									
									var shapeDetails = {
											coord : [0, 0,
											         19,23],
											type : 'rect'
								    };									

									var opts = {
										position : geocoderResultWithinBounds,
										icon : addressIcon,
										shape: shapeDetails,
										shadow: shadowIcon,
										title: addressTitle,
										map : map
									};
									
									if (addressEnteredMarker) {
										addressEnteredMarker.setMap(null);
										addressEnteredMarker.setOptions(opts);
         								
 
									} else {
										addressEnteredMarker = new google.maps.Marker(opts);
									}
									
    								google.maps.event.addListener(
											addressEnteredMarker,
											'click',
											function() {
												var infowindow = new google.maps.InfoWindow(
														{
															content : addressTitle
														});
												infowindow.open(map, addressEnteredMarker);
											});
									
									

								}
							} else {
								var myDialog = dijit
										.byId('message-container-div');

								var messageText = ADDRESS_NOT_FOUND.replace(
										'%1s', "\"" + addressEntered + "\"");

								dojo.byId('message-div').innerHTML = messageText;

								myDialog.show();
							}
						});
	
}

function startApplication() {

	dojo.forEach(dojo.query('input[name$=\"applicationID\"]'), function(input) {
		if (input.checked) {
			applicationID = input.value;
			screeningSelected = false;
			populateHiddenFields();
		}
	});

	dojo.forEach(dojo.query('input[name$=\"screeningSelected\"]'), function(
			input) {
		input.value = false;
	});

	if (applicationID != null && applicationID != "") {

		var url = document.location.href;
		if (url.indexOf("PlayerPage.do?") != -1) {
			dojo.byId('mainForm').submit();
		} else {
			this.location = 'CitizenAccount_resolveApplyFromRecommendationsPage.do?applicationID='
					+ applicationID + '&id=' + sessionID;
		}
	}
}

function startScreening() {

	dojo.forEach(dojo.query('input[name$=\"screeningID\"]'), function(input) {
		if (input.checked) {
			screeningID = input.value;
			screeningSelected = true;
			populateHiddenFields();
		}
	});

	dojo.forEach(dojo.query('input[name$=\"screeningSelected\"]'), function(
			input) {
		input.value = true;
	});

	if (screeningID != null && screeningID != "") {

		var url = document.location.href;
		if (url.indexOf("PlayerPage.do?") != -1) {
			dojo.byId('mainForm').submit();
		} else {
			this.location = 'CitizenAccount_resolveScreenFromRecommendationsPage.do?screeningID='
					+ screeningID
					+ '&id='
					+ sessionID
					+ '&lifeEventID='
					+ lifeEventID;
		}
	}
}

function populateHiddenFields() {
	// ensure that this element is available
	if (document.getElementsByName("__o3fmeta")[0]) {

		document.getElementsByName("__o3fmeta")[0].value = '{"screeningID":["/data/si/ACTION/key$pageData/ans/screeningID","","",false],"applicationID":["/data/si/ACTION/key$pageData/ans/applicationID","","",false],"screeningSelected":["/data/si/ACTION/key$pageData/ans/screeningSelected","","",false]}';
	}
}

function openCenteredWindow(url, inputHeight, inputWidth) {
	var width = inputWidth;
	var height = inputHeight;
	var left = parseInt((screen.availWidth / 2) - (width / 2));
	var top = parseInt((screen.availHeight / 2) - (height / 2));
	var windowFeatures = "width=" + width + ",height=" + height
			+ ",status,resizable,left=" + left + ",top=" + top + "screenX="
			+ left + ",screenY=" + top;
	myWindow = window.open(url, "serviceByType", windowFeatures);
}
