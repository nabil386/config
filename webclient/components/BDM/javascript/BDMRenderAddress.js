

/*
 * Task 5155 - CKwong - Address Evidence
 * Implements methods to handle dynamic rendering of the address layout.
 * For a Canadian address format, the elements are APT, POBOXNO, ADD1, ADD2, CITY, PROV, POSTCODE, COUNTRY
 * For an American address format, the elements are APT, POBOXNO, ADD1, ADD2, CITY, STATEPROV, ZIP, COUNTRY
 * For an international address format, the elements are APT, POBOXNO, ADD1, ADD2, CITY, STATEPROVX, ZIPX, COUNTRY
 */


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




/**
 * Initial load of the page to render the page with the appropriate address format
 * @param {string} countryID
 */
function initialLoad(countryID, initialCountry) {

  // get the column corresponding to the country field via its ID (set by the renderer)
  var countryColumnNode = dojo.byId(countryID);

  if (countryColumnNode == null) {
    alert("There is an error loading the address, please contact your administrator.");
    hasErrorOccurred = true;
    return;
  }

  //on the initial load, get the initial country value (either from the default or the previous value on a reload)
  var addressNode = dojo.byId(countryID).closest('.bx--cluster');
  hideFields(addressNode, initialCountry);

}


/**
 * Rerenders the address layout according to the given country. Triggered on change of the country field.
 * @param {} currDoc
 */
 function rerenderAddressFields(currDoc, countryID) {
  var addressNode = dojo.byId(countryID).closest('.bx--cluster');
  //because the on change event is triggered OOTB, we are unable to pass the changed value, and we must use this variable instead
  var country = currDoc.__spmComboboxSelectedCode;
  //if no errors have occurred during load or setting the active field, hide fields
  if (!hasErrorOccurred) {
    hideFields(addressNode, country);
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
      hideField(provinceStateCol)
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
