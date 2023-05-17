/*
 * Copyright 2017,2021 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 *

 * Modification History
 * --------------------
 * 13-Aug-2018  YF  [RTC212868] Removed hard coded strings "Evidence,Status, System Action". These values are now loaded from properties file and can be localized.
*/

// Here we are telling Dojo which AMD modules need to use in out widget
define([ "dojo/_base/declare", "dijit/_WidgetBase", "dijit/_TemplatedMixin",  "curam/util/ResourceBundle", "dojo/text!./templates/AESGraph.html", "curam/AESGraph/vis", "dojo/domReady!" ], function(declare, _WidgetBase, _TemplatedMixin, ResourceBundle, aesGraphTemplate, vis) {
 
  // you can think of this next line as we're creating a class called AESGraph which extends _WidgetBase & _TemplatedMixin
  return declare("AESGraph", [ _WidgetBase, _TemplatedMixin], {

    // Since our widget extends _TemplatedMixin, it expects to find the attribute below which has the template
	templateString: aesGraphTemplate,
	 
    // We'll do all our widget work in here.
    // Dojo widget lifecycle will call this method last
    startup: function(){
   
    this.inherited(arguments);
	
	var parsedJSON = JSON.parse(this.data);
	
	var nodes = parsedJSON.nodes;
	var edges = parsedJSON.edges;
	
	var locale = curam.config ? curam.config.locale : jsL;
	var bundle = new ResourceBundle("AESGraph");
	
	var evidenceTooltip = bundle.getProperty('AESGraph.Tooltip.Evidence');
	var statusTooltip = bundle.getProperty('AESGraph.Tooltip.Status');
	var systemActionTooltip = bundle.getProperty('AESGraph.Tooltip.SystemAction');

	//Append all evidence types to the title of an edge in HTML format to display in a tooltip popup on a hover event
	for(var i = 0; i<edges.length; i++){
		edges[i].title = "<table class=\"aesGraphTooltip\"><tr><th>"+evidenceTooltip 
		   +"</th><th>"+ statusTooltip 
		     +"</th><th>"+systemActionTooltip+"</th></tr>";
		for(var j =0; j<edges[i].evidences.length; j++){
			edges[i].title += "<tr><td>" + edges[i].evidences[j].description + "</td><td>" + edges[i].evidences[j].status + "</td><td>" + edges[i].evidences[j].action + "</td></tr>";
		}
		edges[i].title += "</table>";
	}
	
	//Update the color of the Source Case node(s)
	for(var i=0; i<nodes.length; i++){
		if(nodes[i].caseType === "source"){
			nodes[i].color = { 
					background: '#8CD211',
					highlight: {
						background: '#8CD211'
					}
			}
		}
	}

	var data = {
		nodes: nodes,
		edges: edges
	}
	
	var layoutOptions = {
    	hierarchical: {
    		enabled: true,
    		parentCentralization: true,
    		direction: 'UD',
    		sortMethod: 'directed',
    		nodeSpacing: 200
    	
    	}
   	}; 

	var options = {
	    layout: layoutOptions,
	    nodes: {
	    	shape: 'dot',
	    	size: 15,
	    	selectable: false,
	    	hover: false,
	    	background: '#1f57a4',
	    	widthConstraint: {
	    		maximum : 170
	    	}
	    }, 
	    edges: {
	    	hover: false,
	    	arrows : { 
	    		to: {
	    			enabled: true
	    			}	
	    	},
	    	font: {color: '#000000'}
	    }
	}; 
    
    var container = document.getElementsByClassName('AESGraph')[0];

    // draw network graph
    var network = new vis.Network(container, data, options);
    }
  });
});