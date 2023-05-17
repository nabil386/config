require(["dojo/query", "dojo/NodeList-dom", "dojo/domReady!"])

function disablecheck() {
    "use strict";
    var clients = dojo.query("input[name='__o3msw.ACTION.key$clientTabList']");
    var dropDownBoxes = dojo.query("input[type=text]").filter(".dijitInputInner");
    if (clients.length != 0 && dropDownBoxes.length != 0)
    {
	var priClient = dropDownBoxes[0].nextSibling;
    for (var i = 0;i<clients.length;i++) 
    {
     var client = clients[i].value;
     if (client === priClient.value)
     {
       clients[i].disabled=true;
       clients[i].checked=true;
     }
	 else
	 {
	   if (clients[i].disabled)
	   {
	    clients[i].checked=false;
	    clients[i].disabled=false;
	   } 
	   
	   
	 }
    }
    }
	disableClientsForSelectedCase();
}

function disableClientsForSelectedCase() {
		"use strict";
		var opCases = dojo.query("input[name='__o3msw.ACTION.key$opCaseTabList']");
		if (opCases.length === 0)
		{
			opCases = dojo.query("input[name='__o3msw.ACTION.details$opCaseTabList']");	
		}
		var clientList = dojo.query("input[name='__o3msw.ACTION.key$opClientTabList']");
		if (clientList.length === 0)
		{
			clientList = dojo.query("input[name='__o3msw.ACTION.details$opClientTabList']");
		}
		var checkedCount = 0;
		var selectedCase;
	   if (opCases.length != 0 && clientList.length != 0)
	{	
		for (var i = 0;i<opCases.length;i++) 
		{
			if (opCases[i].checked === true)
			{
			 checkedCount = checkedCount + 1;
			 selectedCase = opCases[i];
			}
		}		   
		if (checkedCount === 1)
		{
		var opCaseString = selectedCase.value;
		var caseArray = opCaseString.split("|");
		var opClients = caseArray[1].split(", ");
	    for (var j = 0;j<clientList.length;j++)
	    {
		 var noMatch=true;
	     for (var x = 0;x<opClients.length;x++)
		 {
			if (clientList[j].value===opClients[x])
			{
				clientList[j].disabled=true;
				clientList[j].checked=true;
				noMatch=false;
				break;
			}
		  }
		  if (noMatch)
		  {
		    if (clientList[j].disabled)
		    {
		      clientList[j].checked=false;	
		      clientList[j].disabled=false;
		    }		    		    
		  }
	     }
        }
		else if (checkedCount === 0)
	    {
	     for (var y = 0;y<clientList.length;y++)
		  {
			if (clientList[y].disabled)
			{ 
			 clientList[y].disabled=false;
			 clientList[y].checked=false;
			} 
		  }
	    }
	}
}
 
 