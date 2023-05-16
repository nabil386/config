/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2012,2017. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
var isIE;

var childComboRef;
var parentCombo;
var pageParameter;
var val;

function AJAXInteractionRedirection(url) {
    this.url = url;
    var req = init();

    req.onreadystatechange = processRequest;

    function init() {
      if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
      } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
      }
    }

    function processRequest () {

      if (req.readyState == 4) {

        if (req.status == 200) {
          postProcessRedirect(req.responseXML);
        }
      }
    }

    this.send = function() {

        req.open("POST", url, true);
        req.send(null);
    }
}


function initRedirection(arg0,arg1) {
   	var fieldName = "__o3fwp.ACTION." + arg1;
    this.val = event.srcElement;
 	parentCombo =document.getElementById(fieldName);
	pageParameter=document.getElementById(arg0);
}

function postProcessRedirect(responseXML) {
	var page = responseXML.getElementsByTagName("redirectPage")[0];
  	
    var name = page.getElementsByTagName("pageName")[0];
    
    var pageContextDesc = page.getElementsByTagName('pageContextDesc')[0];
  
    if(name.childNodes[0]!=null){
    	redirectMe(name.childNodes[0].nodeValue,pageContextDesc.childNodes[0].nodeValue);
    }
}

function redirectMe(pageName,pageContextDesc) {
	// BEGIN, CR00236827, SS
 	var pageDetails=pageName+ "Page.do?" + pageParameter.id  + "=" + pageParameter.value + "&configLegalActionCategoryID=" + parentCombo.value + "&configLegalActionID=" + childComboRef.value+"&"+pageContextDesc;
    window.location=pageDetails;
    // END, CR00236827
}

function doRedirecr(arg0, arg1, arg2,arg3) {

	// BEGIN, CR00236827, SS
 	parentCombo = dijit.byId('__o3id0'); 
 	
 	// BEGIN, CR00248724, SS
 	var submitType = document.getElementById("__o3btn.CTL1");
   	var hiddenType = document.createElement('input');
	hiddenType.type="hidden";
    submitType.parentNode.replaceChild(hiddenType,submitType);
 	var saveOriginal = document.getElementById("__o3btn.CTL1");
 	
 	if (typeof parentCombo === 'undefined') {
 		parentCombo = document.getElementById('__o3id0');
 		
 		if (parentCombo.selectedIndex == 0) {
 			hiddenType.parentNode.replaceChild(saveOriginal,hiddenType);
 	    	return;
 		}
 	} else {
 		parentComboStore = parentCombo.store;
 	   // BEGIN, CR00348478, SSK
 	    if(parentCombo.item.id == ''){
 	    // END, CR00348478
 	    	hiddenType.parentNode.replaceChild(saveOriginal,hiddenType);
 	    	return;
 	    }
 	    parentComboIndex = parentCombo.item.index;
 	}
    
    pageParameter = document.getElementById(arg0);
   	childComboRef = document.getElementById('__o3id1');
   	
    if(childComboRef.value==''){
       	hiddenType.parentNode.replaceChild(saveOriginal,hiddenType);
    	return;
    }
    // END, CR00248724
 
 
    var url = arg2 + "Page.do?Param=" + escape(childComboRef.value)+"&"+arg3.value;
	//END, CR00236827
	 
    var ajax = new AJAXInteractionRedirection(url);
    ajax.send();
}
