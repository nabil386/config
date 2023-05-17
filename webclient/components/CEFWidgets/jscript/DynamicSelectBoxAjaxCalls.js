var isIE;
var childCompIndex = 0;
var componentSelectorID;
var subComponentSelectorID;
var componentBaseType;
var parentCombo;
var childCombo;
var parentComboInitialCode;
var childComboInitialCode;
var componentPage;
var subComponentPage;

// Default script which will manage the sub components contents on load.
selectBoxes=document.getElementsByTagName('select');
for (i=0;i<selectBoxes.length;i++) {
	if(selectBoxes[i].getAttribute("objID")=="component") {
    	componentSelectorID = selectBoxes[i].id;
    	componentBaseType = selectBoxes[i].getAttribute("componentBaseType");
    	parentCombo = selectBoxes[i]; 
    	parentComboInitialCode = parentCombo.getAttribute("initialCode"); 
    	componentPage = parentCombo.getAttribute("componentPage");
    	
    	
    }
    
   	if(selectBoxes[i].getAttribute("objID")=="subComponent") {
   		subComponentSelectorID = selectBoxes[i].id;
   		childCombo = selectBoxes[i];
   		childComboInitialCode = childCombo.getAttribute("initialCode");
   		subComponentPage = parentCombo.getAttribute("subComponentPage");
   		 
   		
   }
        
    
}

initiateComponentList();

function initiateComponentList() {
	 
	var url = escape(componentPage) + "?componentBaseType=" + escape(componentBaseType);
	var ajax = new AJAXInteraction(url,parentCombo);
    ajax.send();

}


function AJAXInteraction(url,comboType) {
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
          postProcess(req.responseXML, comboType);
          if(comboType.getAttribute("objID")=="component") {
          	getSubComponentItems();
          }
        }
      }
    }

    this.send = function() {
   
        req.open("POST", url, true);
        req.send(null);
    }
}

function postProcess(responseXML, comboType) {
   
	
	clearCombo(comboType);
	var xmlText;
	if(comboType.getAttribute("objID")=="component") {
		xmlText = responseXML.getElementsByTagName("Components")[0];
	} else {
		xmlText = responseXML.getElementsByTagName("Sub-Components")[0];
	}
	if(xmlText != null){
		populateCombo("","",comboType);
	    for (loop = 0; loop < xmlText.childNodes.length; loop++) {
		    var option = xmlText.childNodes[loop];
			var name = option.getElementsByTagName("Name")[0].childNodes[0].nodeValue;
			var id = option.getElementsByTagName("Value")[0].childNodes[0].nodeValue;
			populateCombo(id,name,comboType);
	    }
	    	
    }
}

function populateCombo(id,name,comboType) {


	option = new Option(name,id);
	if(comboType.getAttribute("objID")=="component") {
		if(id == parentComboInitialCode) {
			option.selected = true;
		}
	} else {
	
		if(id == childComboInitialCode) {
			option.selected = true;
		}
	
	}
	try{
			comboType.add(option,null);
			childCompIndex = childCompIndex+1;
	}
	catch(e){
		comboType.add(option,-1);	
	}
	
	
	
}

function clearCombo(comboType) {
    if (comboType) {
      comboType.style.visible = false;
      for (loop = comboType.childNodes.length -1; loop >= 0 ; loop--) {
        comboType.remove(loop);
      }
    }
}


function getSubComponentItems() {

	
	var code = parentCombo.options[parentCombo.selectedIndex].value;
	if(code != "") {
		var url = escape(subComponentPage) + "?componentCode=" + escape(code);
		var ajax = new AJAXInteraction(url,childCombo);
    	ajax.send();
    	}
    } 


