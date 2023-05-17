dojo.provide("curam.googlemap");

/* Modification History 
 * ====================
 * 05-Sep-2013  SD [CR00393446] Migrate to Google Maps V3 javascript API.
 * 20-Jun-2012  DG [CR00329440] Added localization support.
 */

dojo.require("curam.util.ResourceBundle");
dojo.requireLocalization("curam.application", "googlemap");

var markerList = [];
var toHtmlList = [];
var fromHtmlList = [];
var map = "";
// BEGIN,CR00266151,GG
var zoomLevel="";
var gmarkers = [];
var addressData=new Array();
// END,CR00266151

var directionsDisplay;
var directionService = new google.maps.DirectionsService();

// BEGIN, CR00329440, DG
var bundle = new curam.util.ResourceBundle("googlemap");
var googlemapLocalizedText = {
  initGoogleMapAddressText : bundle.getProperty("initGoogleMap.Address.Text"),
 
  createMarkertohereGetDirectionsText : bundle.getProperty("createMarker.tohere.GetDirections.Text"),
  createMarkertohereToHereText : bundle.getProperty("createMarker.tohere.ToHere.Text"),
  createMarkertohereFromHereText : bundle.getProperty("createMarker.tohere.FromHere.Text"),
  createMarkertohereStartAddressText : bundle.getProperty("createMarker.tohere.StartAddress.Text"),
  createMarkertohereGoText : bundle.getProperty("createMarker.tohere.Go.Text"),
  createMarkertohereWalkText : bundle.getProperty("createMarker.tohere.Walk.Text"),

  createMarkerfromhereGetDirectionsText : bundle.getProperty("createMarker.fromhere.GetDirections.Text"),
  createMarkerfromhereToHereText : bundle.getProperty("createMarker.fromhere.ToHere.Text"),
  createMarkerfromhereFromHereText : bundle.getProperty("createMarker.fromhere.FromHere.Text"),
  createMarkerfromhereEndAddressText : bundle.getProperty("createMarker.fromhere.EndAddress.Text"),
  createMarkerfromhereGo2Text : bundle.getProperty("createMarker.fromhere.Go2.Text"),
  createMarkerfromhereWalkText : bundle.getProperty("createMarker.fromhere.Walk.Text"),

  createMarkerinactiveGetDirectionsText : bundle.getProperty("createMarker.inactive.GetDirections.Text"),
  createMarkerinactiveToHereText : bundle.getProperty("createMarker.inactive.ToHere.Text"),
  createMarkerinactiveFromHereText : bundle.getProperty("createMarker.inactive.FromHere.Text"),

  onDirectionsErrorG_GEO_UNKNOWN_DIRECTIONSText : bundle.getProperty("onDirectionsError.G_GEO_UNKNOWN_DIRECTIONS.Text"),
  onDirectionsErrorG_GEO_UNKNOWN_ADDRESSText : bundle.getProperty("onDirectionsError.G_GEO_UNKNOWN_ADDRESS.Text"),
  onDirectionsErrorG_GEO_UNKNOWN_ADDRESSSuggestion1 : bundle.getProperty("onDirectionsError.G_GEO_UNKNOWN_ADDRESS.Suggestion1"),
  onDirectionsErrorG_GEO_UNKNOWN_ADDRESSSuggestion2 : bundle.getProperty("onDirectionsError.G_GEO_UNKNOWN_ADDRESS.Suggestion2"),

  onDirectionsErrorOtherText : bundle.getProperty("onDirectionsError.Other.Text"),
  onDirectionsErrorOtherSuggestion1 : bundle.getProperty("onDirectionsError.Other.Suggestion1"),
  onDirectionsErrorOtherSuggestion2 : bundle.getProperty("onDirectionsError.Other.Suggestion2")
};
// END, CR00329440

function initGoogleMap(mapID) {

  var mapOptions = {
    center: new google.maps.LatLng(addressData[0].latitude, addressData[0].longitude),
    zoom: determineZoomLevel(zoomLevel),
    largeMapControl: true,
    mapTypeControl: true,
    scaleControl: true,
    scrollwheel: true
  };

  map = new google.maps.Map(document.getElementById(mapID), mapOptions);    	  
  clearMapOverlays();
    
    if (showDirections) {
      directionsDisplay = new google.maps.DirectionsRenderer();
      directionsDisplay.setMap(map);
      directionsDisplay.setPanel(document.getElementById("directionsDiv"));
     
      google.maps.event.addListener(directionsDisplay, "error", onDirectionsError);
      google.maps.event.addListener(directionsDisplay, "load", onDirectionsLoad);                        
    }
    
    // for each address, create a marker for it and customise it, if required
    for (var i = 0; i < addressData.length; i++) {
  
      var currentAddress = addressData[i].address;
      var currentIconLocation = addressData[i].iconLocation;
      var point = new google.maps.LatLng(addressData[i].latitude, addressData[i].longitude);    	
      var infoWindowHtml = "";
      
      if (currentAddress.length != 0) {
        infoWindowHtml = googlemapLocalizedText.initGoogleMapAddressText + "<b>" + currentAddress + "</b><br>";	      
      }
     
      for (var j = 0; j < addressData[i].links.length; j++) {
        infoWindowHtml = infoWindowHtml + '<br><a href="'+addressData[i].links[j].url+'">'+addressData[i].links[j].displayText+'</a>';
      }
          
      var addressIconSize = "";
      var addressImageURL = "";
      
      if (currentIconLocation.length != 0) {
        addressImageURL = currentIconLocation;      
      }
      
      if (addressData[i].iconWidth.length != 0 && addressData[i].iconHeight.length != 0) {
        addressIconSize = new google.maps.Size(addressData[i].iconWidth, addressData[i].iconHeight);
      }

      var addressIcon = {
        url: addressImageURL,
        size: addressIconSize
      };
      
      var markerOptions = { 
        icon:addressIcon,
        title:currentAddress 
      };

      createMarker(point, infoWindowHtml, markerOptions);	
    }  
}

function createMarker(point, infoWindowHtml, markerOptions) {

  // Create the marker
  var marker = new google.maps.Marker({
    position: point,
    title: markerOptions.title,
    map: map,
    clickable: true,
    info: new google.maps.InfoWindow({content: infoWindowHtml})
  });
  
  // If icon details are available, add to marker, otherwise, leave to default
  if (typeof(markerOptions.addressIcon) != "undefined" && markerOptions.addressIcon != null) {
    marker.icon = markerOptions.addressIcon; 
  }
 
  // When the marker is clicked, open an information window.
  google.maps.event.addListener(marker, "click", function() {
    marker.info.open(map, marker);
  });
  
  // Set up 'To Here' and 'From Here' links
  if (showDirections) {
    
    var i = markerList.length;

    // The info window version with the "to here" form open
    toHtmlList[i] = infoWindowHtml + '<br><br>' + googlemapLocalizedText.createMarkertohereGetDirectionsText + '<b>' + googlemapLocalizedText.createMarkertohereToHereText + '<\/b> - <a href="javascript:fromHere(' + i + ')">' + googlemapLocalizedText.createMarkertohereFromHereText + '<\/a>' +
      '<br><br>'+ googlemapLocalizedText.createMarkertohereStartAddressText+ '<form action="javascript:getGoogleMapDirections()">' +
      '<input type="text" SIZE=30 name="startAddress" id="startAddress" value="" /> ' +
      '<INPUT value="'+googlemapLocalizedText.createMarkertohereGoText+'" TYPE="SUBMIT"><br>' +
      googlemapLocalizedText.createMarkertohereWalkText + ' <input type="checkbox" name="walk" id="walk" />' +
      '<input type="hidden" id="endAddress" value="'+name+"@"+ point.lat() + ',' + point.lng() + '"/>';

    // The info window version with the "from here" form open
    fromHtmlList[i] = infoWindowHtml + '<br><br>' + googlemapLocalizedText.createMarkertohereGetDirectionsText + '<a href="javascript:toHere(' + i + ')">' + googlemapLocalizedText.createMarkerfromhereFromHereText + '<\/a> - <b>' + googlemapLocalizedText.createMarkertohereFromHereText + '<\/b>' +
      '<br><br>' + googlemapLocalizedText.createMarkerfromhereEndAddressText + '<form action="javascript:getGoogleMapDirections()">' +
      '<input type="text" SIZE=30 name="endAddress" id="endAddress" value="" /> ' +
      '<INPUT value="'+googlemapLocalizedText.createMarkerfromhereGo2Text+'" TYPE="SUBMIT"><br>' +
      googlemapLocalizedText.createMarkerfromhereWalkText + ' <input type="checkbox" name="walk" id="walk" />' +      
      '<input type="hidden" id="startAddress" value="'+name+"@"+ point.lat() + ',' + point.lng() +'"/>';
        
    // The inactive version of the direction info
    infoWindowHtml = infoWindowHtml + '<br><br>' + googlemapLocalizedText.createMarkerinactiveGetDirectionsText + '<a href="javascript:toHere('+i+')">' + googlemapLocalizedText.createMarkerinactiveToHereText + '<\/a> - <a href="javascript:fromHere('+i+')">' + googlemapLocalizedText.createMarkerinactiveFromHereText + '<\/a>';
   
    markerList.push(marker);
  }
  
  return marker;
}

function getGoogleMapDirections() {
 
  // Check for travel options and request the directions from google
  document.getElementById("directionsDiv").innerHTML = '';
  dojo.html.setStyle(document.getElementById("directionsDiv"), "background-color", "white");	

  var travelOptions = {};
     
  if (document.getElementById("walk").checked) {   
    travelOptions.travelMode = google.maps.DirectionsTravelMode.WALKING
  }
  
  var startAddress = document.getElementById("startAddress").value;
  var endAddress = document.getElementById("endAddress").value;
  
  directionsService.route({
    origin: startAddress,
    destination: endAddress,
    travelMode: travelOptions.travelMode
  }, function(result, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(result);
    }
  });
}

function toHere(i) {
  markerList[i].info = new google.maps.InfoWindow({
    content: toHtmlList[i]
  });
  
  markerList[i].info.open(map, markerList[i]);
}

function fromHere(i) {
  markerList[i].info = new google.maps.InfoWindow({
    content: fromHtmlList[i]
  });
  
  markerList[i].info.open(map, markerList[i]);
}

function onDirectionsError() {

  // Output some meaningful error messages
  dojo.html.setStyle(document.getElementById("directionsDiv"), "background", "#FFF1A8");	
  
  var errorCode = directions.getStatus().code;

  if (errorCode == G_GEO_UNKNOWN_DIRECTIONS) {
  
      document.getElementById("directionsDiv").innerHTML = '<br><b>' + googlemapLocalizedText.onDirectionsErrorG_GEO_UNKNOWN_DIRECTIONSText + '</b>';
  	
  } else if (errorCode == G_GEO_UNKNOWN_ADDRESS) {
  
      document.getElementById("directionsDiv").innerHTML = '<br><b>' + googlemapLocalizedText.onDirectionsErrorG_GEO_UNKNOWN_ADDRESSText + '</b><br>' +
      '<ul style="list-style-position:inside; list-style-type:disc"><li>' + googlemapLocalizedText.onDirectionsErrorG_GEO_UNKNOWN_ADDRESSSuggestion1 + '</li><li>' + googlemapLocalizedText.onDirectionsErrorG_GEO_UNKNOWN_ADDRESSSuggestion2 + '</li></ul>';
  
  } else {
  
      document.getElementById("directionsDiv").innerHTML = '<br><b>' + googlemapLocalizedText.onDirectionsErrorOtherText + '</b><br>' +
      '<ul style="list-style-position:inside; list-style-type:disc"><li>' + googlemapLocalizedText.onDirectionsErrorOtherSuggestion1 + '</li><li>' + googlemapLocalizedText.onDirectionsErrorOtherSuggestion2 + '</li></ul>';
  }
  
}  
  
function onDirectionsLoad() {

  clearMapOverlays();
}

// BEGIN,CR00266151,GG
// initialises a google map
// mapID - reference to the map DIV
// centreLatitude - long
// centreLongitude - long
// zoomLevel - int - max 16
function initialiseMap(mapID, centreLatitude, centreLongitude, zoomLevel) {

  var localMapOptions = {
    center: new google.maps.LatLng(centreLatitude, centreLongitude),
    zoom: determineZoomLevel(zoomLevel),
    largeMapControl: true,
    mapTypeControl: true,
    scaleControl: true,
    scrollwheel: true
  };
  
  map = new google.maps.Map(document.getElementById(mapID), localMapOptions);    	  
  clearMapOverlays();
}

// moved this in here so it has access to Gmarkers
// This function picks up the click and opens the corresponding info window
function myclick(i) {
   google.maps.event.trigger(gmarkers[i], "click");
}


function createAndAddMapMarker(point, infoWindowHtml, markerOptions, displayOnLoad) {

  // Create the marker
  var marker = new google.maps.Marker({
    position: point,
    title: markerOptions.title,
    map: map,
    clickable: true,
    info: new google.maps.InfoWindow({content: infoWindowHtml})
  });
  
  // If icon details are available, add to marker, otherwise, leave to default
  if (typeof(markerOptions.addressIcon) != "undefined" && markerOptions.addressIcon != null) {
    marker.icon = markerOptions.addressIcon; 
  }
 
  // When the marker is clicked, open an information window.
  google.maps.event.addListener(marker, "click", function() {
    marker.info.open(map, marker);
  });  
  
  // add marker to array
  gmarkers.push(marker);
    
  // hide the marker if the category is not selected
  if(!displayOnLoad) {
    marker.hide();
  }
    
  return marker;
}
// END,CR00266151

function determineZoomLevel (zoomLevel){
  var zoom = "";
  
  // Set a default zoom level.
  if (zoomLevel.length == 0) {
    zoom = 16;
  } else {
    zoom = zoomLevel
  }
  
  return zoom;
}

function clearMapOverlays() {
  if (gmarkers) {
    for (i in gmarkers) {
      gmarkers[i].setMap(null);
    }
  }
}