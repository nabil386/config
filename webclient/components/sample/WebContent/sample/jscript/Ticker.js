/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/** Modifications
 * ============
 * 04-Oct-2012 BD [CR00345902] Upgrade code for Dojo 1.7
 */
require(["dojo.string.common"]);
	    function makeTicker(node, msec, delay, tagName) {

	      node = dojo.byId(node);

	      var start = 0; // The first character to place in the ticker
	      var end = 0;   // The last character to place in the ticker
	      var interval;  // Holds the pointer to the interval timer
	      var stringIndex = 0; // The string to show
	      var text;      // An array of the text strings to print
	      var links;     // An array of the hyperlinks to use

	      var marginBox = dojo.html.getContentBox(node);

	      var innerNode;
	      var ss = dojo.html.setStyle; // Store a shortcut to the setStyle method


	      var outerWidth = marginBox.width;

	      // Get a list of all the "div" elements in the node
	      // Each one represents another message to print
	      var childNodes = node.getElementsByTagName(tagName || "a");

	      // Build up the arrays of strings to display and links
	      // to put on the anchor tag
	      if (childNodes == null || childNodes.length == 0) {
	        text = [dojo.string.trim(node.innerHTML, 0)];
	        links = [""];
	      } else {
	        text = [];
	        links = [];
	        for (var i = 0; i < childNodes.length; i++) {
	          text.push(dojo.string.trim(childNodes[i].innerHTML, 0));
	          links.push(childNodes[i].getAttribute("href"));
	          if (i > 0) {
	            ss(childNodes[i], "display", "none");
	          }
	          ss(childNodes[i], "white-space", "nowrap");
	        }
	      }


	      // The outer node could collapse, so get the height of a single line
	      // of text, and hard code it in the outer nodes style
	      //innerNode.innerHTML = text[0].substring(0, 1);
	      ss(node, "height", dojo.html.getMarginBox(childNodes[0]).height + "px");
	      ss(node, "visibility", "visible");


	      function startTicker() {
	        if (innerNode) {
	          ss(innerNode, "display", "none");
	        }

	        innerNode = childNodes[stringIndex];
	        innerNode.innerHTML = "";
	        ss(innerNode, "display", "");


	        interval = setInterval(ticker, msec || 200);
	      }

	      function ticker(){
	        // If a complete string has been displayed, show
	        // the next one
	        if (end >= text[stringIndex].length) {
	          end = 0;
	          start = 0;
	          stringIndex ++;

	          // If the last string has been shown, loop back to the first one
	          if (stringIndex > text.length - 1) {
	            stringIndex = 0;
	          }

	          clearInterval(interval);

	          // Show the next string after a short delay
	          setTimeout(startTicker, delay || 500);
	          return;
	        }

	        innerNode.innerHTML = text[stringIndex].substring(start, ++end);
	        var width = dojo.html.getMarginBox(innerNode).width;

	        // If the string is almost too wide to fit,
	        // cut off one letter from the start.
	        if (width >= marginBox.width - (Math.ceil(width / (end - start)) + 20)) {
	          start++;
	        }
	      }

	      startTicker();

	    }