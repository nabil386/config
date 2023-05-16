/*
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
    "dojo", 
    "dojo/_base/declare",
    "dojo/io/script"
], function(dojo, declare, script){

		return declare("cwtk.util.Commons", null, {

		  addJavascript : function(jsname) {
		  
			var path = 'CDEJ/jscript/';
			
			script.get({url : path + jsname});
			
		  }
		});

});