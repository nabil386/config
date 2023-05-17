/*
 * Licensed Materials - Property of IBM
 *
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
var isIE;
var childCombo;
var parentCombo;
var val;

function AJAXInteraction(url) {
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
          postProcess(req.responseXML);
        }
      }
    }

    this.send = function() {
   
        req.open("POST", url, true);
        req.send(null);
    }
}


function init(attrb) {
    
	var fieldName = "__o3fwp.ACTION." + attrb;
    this.val = event.srcElement;
    parentCombo = document.getElementById(val.id);
    childCombo = document.getElementById(fieldName);
}

function postProcess(responseXML) {
   
    clearCombo();
  	var codetables = responseXML.getElementsByTagName("codetables")[0];
    populateCombo("", " ");
    if(codetables != null){
	    for (loop = 0; loop < codetables.childNodes.length; loop++) {
		    var codetable = codetables.childNodes[loop];
		var name = codetable.getElementsByTagName("name")[0];
		var id = codetable.getElementsByTagName("id")[0];
		populateCombo(id.childNodes[0].nodeValue,name.childNodes[0].nodeValue);
	    }
    }
}

function clearCombo() {
    if (childCombo) {
      childCombo.style.visible = false;
      for (loop = childCombo.childNodes.length -1; loop >= 0 ; loop--) {
        childCombo.remove(loop);
      }
    }
}

function populateCombo(id,name) {
	var option = new Option(name,id);
	try{
		childCombo.add(option,null);
	}
	catch(e){
		childCombo.add(option,-1);	
	}
}

function doCompletion(arg0, arg1) {

    init(arg0);
    if (parentCombo.value == "") {
        clearCombo();
    } else {
        var url = arg1 + "Page.do?Param=" + escape(parentCombo.value);
        var ajax = new AJAXInteraction(url);
        ajax.send();
    }
}
