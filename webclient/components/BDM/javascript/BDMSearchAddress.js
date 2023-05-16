/*
 * Task 5155 - CKwong - Address Evidence
 * Implements methods to handle Address Search pop-up screen display,
 * to collect address data and pass it to that screen, and to re-set
 * address fields to values returned from the pop-up screen.
 */

require(['curam/util/Dialog']);
require(["dojo/_base/array"]);
require(["dojo"]);


//NOTE: if the address format is to ever change, this is NOT robust and must be updated
/* json object containing the custom field indices for each layout and whether or not it needs to be hidden */
var layoutConfiguration = {
  CANADA: {
    postalZipCodeIndex : 1,
    provinceStateIndex: 9,
    //START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    unParsedIndIndex: 12,
    //END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    hide: false
  },
  UNITEDSTATES: {
    postalZipCodeIndex : 2,
    provinceStateIndex: 10,
    //START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    unParsedIndIndex: 12,
    //END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    hide: false
  },
  INTERNATIONAL: {
    postalZipCodeIndex : 3,
    provinceStateIndex: 11,
    //START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    unParsedIndIndex: 12,
    //END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    hide: false
  }
}

/* Address format enumeration*/
var addressFormats = {
  CANADA: "CA",
  UNITEDSTATES: "US",
  INTERNATIONAL: "BDMINTL"
}

/* Checks if an error has occurred at any point. If it has, stop any rerendering from happening*/
var hasErrorOccurred = false;


window.onload = disableAddressFields;
/* Global variables */
/* Address header size in lines. */
var addressHeaderSize = 6;

/* Element count in the address format. */
var addressElementCount = 12;

/* Address items count without header information.
   This includes address element count and additional search element. */
var addressItemsCount = addressElementCount + 1;

/* Address fields array. It includes addressItemsCount of elements
   for each address on the screen.  */
var aAddressFields = new Array();

/**
 *  function to set all the adress fields to read-only
 *  looping through titles as id changes every load of the page
 *
 */
function disableAddressFields(){

let inputTag = document.getElementsByTagName('input');
// Address Fields Label Names
let addressFieldsTitles = ['Country','Country Mandatory','Unit/Apt/Suite Number','PO Box/Rural Route/General Delivery','Street Number','Street Name','City, Town or Village','City, Town or Village Mandatory','Province or Territory','Postal Code','State','Zip Code','State/Province/Region', 'Code postal', 'UnParsedInd', 'Numéro d’unité/appartement/bureau', 'Numéro de boîte postale', 'Numéro municipal', 'Nom de rue', 'Ville ou village', 'Province ou territoire', 'Pays', 'Pays Obligatoire', 'Ville ou village Obligatoire','Ind non analysé'];

for(var i = 0 ; i < inputTag.length ; i++){
    if(addressFieldsTitles.includes(inputTag[i].title) && inputTag[i].id!='__o3id0_display' ){
      document.getElementById(inputTag[i].id).setAttribute('readonly','true');
    }
  }
}

/*
 *Sets value to fv for the element identified by id.
 */
function setAddressFieldIDValue(fv, id) {

  var displayIDIndex = id.indexOf("_display");
  if (displayIDIndex !== -1) {
    parsedID = id.substring(0,displayIDIndex);
    setAddressFieldIDValue(fv,parsedID);
  }



  // Determine the correct array of address fields to update.
  var element = document.getElementById(id);

  if (element != null) {
      var e = dijit.byId(id);
      if (!e) {
        e = dojo.byId(id);
      }
      if (e.declaredClass == "curam.widget.FilteringSelect") {
          e.store.fetch({query:{"value": fv},queryOptions: { ignoreCase: true },onComplete:function(items){
              if(items[0] != null || items[0] != "undefined"){
                   e.setValue(items[0].value);
              }
            }
          });
      } else {
          e.value=fv;
      }
  }
}


/*
 * Converts \\' to ' in the string passed in.
 */
function fixSpecialChars(addressData) {
    return addressData.replace("\\'", "'");
}


/*
 * Called from the pop-up Address Search page when a Select action is clicked for
 * a returned address result row.
 */
function setAddressFieldsData(addressData) {
    // addressData is separated by newlines
    var addressFieldValues = addressData.split("\\n");
    this.selectedAddressData = addressFieldValues;
    var count = aAddressFields.length;

    if (this.selectedAddressData == null) {
    } else {

        // We are not interested in the first addressHeaderSize fields,
        // as this is address header info.
        var count = this.selectedAddressData.length;

        for (var i=addressHeaderSize; i < count; i++) {

          var itemValue = this.selectedAddressData[i];

          if (itemValue != null) {

              var addressFieldValue = itemValue.split("=");
              var tagValue;
              var fieldValue;

              if (addressFieldValue.length == 2) {

                tagValue = addressFieldValue[0]
                fieldValue = addressFieldValue[1];
                fieldValue = fixSpecialChars(fieldValue);

              } else if (addressFieldValue.length == 1) {

                tagValue = addressFieldValue[0];
                // No value set
                fieldValue = "";

              } else {

                tagValue = "";
                fieldValue = "";

              }

              // If it is a valid address line.
              if (tagValue.length > 0) {
                // Add 1 to index to skip Search Address field.
                var fieldIdx = i-addressHeaderSize;

                var id = aAddressFields[fieldIdx];
                setAddressFieldIDValue(fieldValue, id);
              }

            }
        }

    }
}


/*
 * Initializes address aAddressFields array.
 */
function initAddressFields(searchID) {
  // Get the list of address fields. These are in table cells with class type of address.
  // Remember the list of address fields for later.
  var searchIDFormatted = "#" + searchID;
  var parentAddressNode = dojo.query(searchIDFormatted).closest('.bx--cluster');

  var addressFieldIndex = 0;

  var childAddressDivs = dojo.query(".bx--text-input", parentAddressNode[0]);

  for (var i = 0; i < childAddressDivs.length; i++) {

    // Store the address field id for later reference.
    var childAddressField = childAddressDivs[i];
    if (!childAddressField.hidden) {
      aAddressFields[addressFieldIndex] = childAddressField.id;
      addressFieldIndex++;
    }

  }

}


/*
 * Initializes address fields and tags arrays.
 */
function initAddressArrays(searchID) {

    initAddressFields(searchID);

}


/*
 * Displays Address Search pop-up screen.
 */
function execPopupForAddress(element, countryID) {

    // Get ID of the clicked element.
    var elementID = element.id;
	// Initialize array of address field identifiers.
    initAddressArrays(elementID);
    // Address search button clicked. Open modal dialog.
    this.selectedAddressData = null;
    var addressDataStr = getAddressFieldsParameters(elementID);

    // Open as modal window only.
    curam.util.Dialog.open('BDMSearchAddressPopupStep1Page.do',{searchID:elementID,addressDetails:addressDataStr,countryID:countryID,wizardStateID:0},{});

}


/*
 * Populates the parent window address fields and then closes the
 * address search pop-up screen.
 */
function populateAddress(thisStr) {

    // Get address data of selected element
    //var searchIDPar = curam.util.getUrlParamValue(thisStr.href.toString(), "searchID");
    var hrefAddress = thisStr.document.getElementsByTagName('a')[4].href;
    var addressDataPar = curam.util.getUrlParamValue(hrefAddress.toString(), "value");
    let countryID = curam.util.getUrlParamValue(hrefAddress.toString(), "countryID");
    let country= curam.util.getUrlParamValue(hrefAddress.toString(), "country");
    var newAddressData = addressDataPar.replace(/%0A/g,"\\n");
    newAddressData = newAddressData.replace(/%3D/g,"=");
    newAddressData = newAddressData.replace(/\+/g," ");
    newAddressData = newAddressData.replace(/%2F/g,"\/");
    newAddressData = newAddressData.replace(/%27/g,"'");
    newAddressData = newAddressData.replace(/%2C/g,",");

    // Get parent window and set address fields
    var parentWin = curam.dialog.getParentWindow(window);
    parentWin.setAddressFieldsData(newAddressData);

    // call rerender method to re-render the address cluster based on country selected
    rerenderAddressFields(parentWin.document.getElementById(countryID).closest('.bx--cluster'), country);
    curam.dialog.closeModalDialog();
    // Return false to prevent search address window from re-submitting form
    // on select of a search result.
    return false;

}

function populateValidAddress(thisStr) {

  // Get address data of selected element
  //var searchIDPar = curam.util.getUrlParamValue(thisStr.href.toString(), "searchID");
  var hrefAddress = thisStr.location.href;
  var addressDataPar = curam.util.getUrlParamValue(hrefAddress.toString(), "addressDetails");
  let countryID = curam.util.getUrlParamValue(hrefAddress.toString(), "countryID");
  let country= curam.util.getUrlParamValue(hrefAddress.toString(), "countryCode");
  var newAddressData = addressDataPar.replace(/%0A/g,"\\n");
  newAddressData = newAddressData.replace(/%3D/g,"=");
  newAddressData = newAddressData.replace(/\+/g," ");
  newAddressData = newAddressData.replace(/%2F/g,"\/");
  newAddressData = newAddressData.replace(/%27/g,"'");
  newAddressData = newAddressData.replace(/%2C/g,",");
  
  newAddressData= decodeURIComponent(newAddressData);
  
  // Get parent window and set address fields
  var parentWin = curam.dialog.getParentWindow(window);
  parentWin.setAddressFieldsData(newAddressData);

  // call rerender method to re-render the address cluster based on country selected
  rerenderAddressFields(parentWin.document.getElementById(countryID).closest('.bx--cluster'), country);
  curam.dialog.closeModalDialog();
  // Return false to prevent search address window from re-submitting form
  // on select of a search result.
  return false;

}

/*
 * Retrieves address details in pipe delimited string.
 * searchID identifies address.
 */
function getAddressFieldsParameters(searchID) {

    // addressData is separated by newlines
    var selectedAddressFieldIDs = new Array();
    var count = aAddressFields.length;
    var paramValues="";

    // Identify the address fields of the source Address Cluster.
    // It is required when there are more than one Address Clusters in the parent page.
    for (var l=0; l<count; l++) {
      if (aAddressFields[l] == searchID) {
        var k =0;
        for (var j=l; j<l+addressItemsCount; j++) {
          selectedAddressFieldIDs[k++] = aAddressFields[j];
        }
        break;
      }
    }

    for (var i=0; i < addressItemsCount; i++) {

      var id = selectedAddressFieldIDs[i];
      var nameValue;

      nameValue = getAddressFieldIDValue(id);

      if( nameValue != null && nameValue != "undefined" ){

        paramValues = paramValues + nameValue;

        if (i < addressItemsCount-1) {
          paramValues = paramValues + "|";
        }

      }
    }

    if(paramValues.length > 0){
      paramValues = paramValues.substr(0,paramValues.length);
    }

    return paramValues;

}


/*
 * Retrieves address field value.
 */
function getAddressFieldIDValue(id) {

  // Determine the correct array of address fields to update.
  var element = document.getElementById(id);

  var paramValue;

  if (element != null) {

    var e = dijit.byId(id);

    if (!e) {
      e = dojo.byId(id);
    }

    if(e != null){
      paramValue= e.value;
    }

    return paramValue;

  }

}
/**
 * Rerenders the address layout according to the given country. Triggered on change of the country field.
 * @param {} currDoc
 */
 function rerenderAddressFields(addressNodeElement, country) {

  //if no errors have occurred during load or setting the active field, hide fields
  if (!hasErrorOccurred) {
    hideFields(addressNodeElement, country);
  }
}

/**
 * Hides address fields inside the address cluster according to which country is passed in
 * @param {Node} addressNode
 * @param {String} country
 */
function hideFields(addressNode, country) {
  //determine which layout fields to hide. If no country is specified, the default is Canada
  if (!country || country === addressFormats.CANADA) {
    layoutConfiguration.CANADA.hide = false;
    layoutConfiguration.UNITEDSTATES.hide = true;
    layoutConfiguration.INTERNATIONAL.hide = true;

  }
  else if (country ===addressFormats. UNITEDSTATES) {
    layoutConfiguration.CANADA.hide = true;
    layoutConfiguration.UNITEDSTATES.hide = false;
    layoutConfiguration.INTERNATIONAL.hide = true;
  }
  else {
    layoutConfiguration.CANADA.hide = true;
    layoutConfiguration.UNITEDSTATES.hide = true;
    layoutConfiguration.INTERNATIONAL.hide = false;
  }

  //get all the columns in the address cluster (omitting the column corresponding to the search widget)
  var addressFields = dojo.query(".bx--col:not(.bdm-address-search)", addressNode);

  var keys = Object.keys(layoutConfiguration);

  for (var i = 0; i < keys.length; i++) {

    var currLayout = layoutConfiguration[keys[i]];

    //START: Task 93505: DEV: Address Format Updates to add BDMUNPARSE
    //Always hide the unparsedInd
    var unParsedIndCol = addressFields[currLayout.unParsedIndIndex];
    hideField(unParsedIndCol);
    //END: Task 93505: DEV: Address Format Updates to add BDMUNPARSE

    //get the columns corresponding the layout's custom fields
    var postalZipCodeCol = addressFields[currLayout.postalZipCodeIndex];
    var provinceStateCol = addressFields[currLayout.provinceStateIndex];

    if (currLayout.hide) {
      hideField(postalZipCodeCol);
      hideField(provinceStateCol);

    }
    else {
      postalZipCodeCol.style.display="";
      provinceStateCol.style.display="";
    }
  }
}

/**
 * Hides the given column and clears its value
 * @param {Node} column
 */
function hideField(column) {
  column.style.display="none";

  //gets all the inputs in the column - there are multiple input elements when it is a dropdown, otherwise there is only 1
  inputs=dojo.query("input",column);

  for (var j = 0; j < inputs.length; j++) {
    inputs[j].value="";
  }
}
