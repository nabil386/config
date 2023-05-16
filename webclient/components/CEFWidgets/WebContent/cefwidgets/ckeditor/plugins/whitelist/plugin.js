/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2015
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */

dojo.require('curam.util.UimDialog');
dojo.require('curam.util.Request');

function whitelistFromProperties(properties) {
	var config = {};
	var lines = properties.split(/\r?\n/);

	for (var i = 0; i < lines.length; i++) {
    	var key = lines[i].split("=")[0];
    	var value = lines[i].split("=")[1];
    	if (value !== undefined && value !== "") {
    		var attr = {};
    		var attributes = {};
    		var attributesArray = value.split(",");
    		for (var j = 0; j < attributesArray.length; j++) {
    			attributes[attributesArray[j]] = "";
    		}
    		attr['attributes'] = attributes;
    		value = attr;
    	}
    	config[key] = value;
	}
  return config;
}

CKEDITOR.plugins.add( 'whitelist',
{
	onLoad: function() {

		curam.util.Request.get({
	        url: "../servlet/resource?r=whitelist.properties",
	        preventCache: true,
	        sync:true,
	        handleAs: "text",
	        handle: function(response) {
	            CKEDITOR.config.whitelist_elements = whitelistFromProperties(response);
	        },
	        error: function(error) {
	        	console.log("Could not read whitelist file. " + error);
	        }
	 	});
	},

	init: function(editor) {
	},

	afterInit: function(editor)	{
	}
} );

CKEDITOR.config.whitelist_globalAttributes = {
	id: "",
	'class': "",
	style: "",
	lang: ""
};

CKEDITOR.on('instanceReady', function(evt) {
	var editor = evt.editor;
	editor.on('paste', function(e){
		var dataProcessor = editor.dataProcessor,
		dataFilter = dataProcessor && dataProcessor.dataFilter,
		htmlFilter = dataProcessor && dataProcessor.htmlFilter;

		// Object with the rules that will be applied on each operation
		var sanitizerRules =
		{
			comment : function( contents )
			{
				// Strip out all comments (as well as protected source like scripts)
				return null;
			},
			elements:
			{
				$ : function( element )
				{
					var whitelisted = false;
					var config=editor.config;
					console.log("element name", element.name, "config", config);
					if (!(element.name in config.whitelist_elements))
					{
						//console.log("remove element: " + element.name);
						whitelisted = true;
						delete element.name;
					}
					else {
						var whitelistAttributes = config.whitelist_elements[ element.name ].attributes || {};
						for( var att in element.attributes )
						{
							var attName=att;
							// Some attributes like href or src are handled by CKEditor in a different way to avoid problems with the browsers
							if ( att.substr(0, 14) == "data-cke-saved" )
								att = att.substr( 15 );

							if (!( att in config.whitelist_globalAttributes) && !(att in whitelistAttributes))
							{
								//console.log("remove attribute: " + att);
								whitelisted = true;
								delete element.attributes[ attName ];
							}
						}
					}

					if (whitelisted){
						//console.log("show Whitelist_errorPage");
						curam.util.UimDialog.open('Whitelist_errorPage.do', {width:350,height:100});
					}
				}
			}
		};

		// dataFilter : conversion from html input to internal data
		dataFilter.addRules( sanitizerRules, 20);
	});
});
