/*
 * Licensed Materials - Property of IBM
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2020. All rights reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
dojo.ready(function(){
   formatList();
});

/**
 * formats the list to make it more usable for a caseworker to view.
 * @returns
 */
function formatList() {
  var table = document.getElementsByTagName("table")[0];
  var tHead = table.children[1];
  var tBody = table.children[2];
  var scriptTag = tBody.children[0];
  
  // ensure the HTML structure is rendered as expected
  if (scriptTag.tagName == "SCRIPT") {
	  var tableRowsHTMLString = scriptTag.innerHTML;
	  
	  var tempNode = document.createElement("tbody");
	  tempNode.insertAdjacentHTML('afterbegin', tableRowsHTMLString);
	  
	  var tempChildren = tempNode.childNodes;
	  
	  for (var index = 0; index < tempChildren.length; index++) {
	    var tableRow = tempChildren[index];
	    var removeBottomBorder = false;
	    
	    if (index < tempChildren.length - 1) {
	      var participantNameRow = tableRow.childNodes[1];
		  var participantNameNextRow = tempChildren[index+1].childNodes[1];
		  var participantNameSameAsNextRow = compare2RowItemsColumns(participantNameRow, participantNameNextRow);
		  var evidenceTypeCurrentRow = tableRow.childNodes[3];
		  var evidenceTypeNextRow = tempChildren[index+1].childNodes[3];
		  var evidenceTypeSameAsNextRow = compare2RowItemsColumns(evidenceTypeCurrentRow, evidenceTypeNextRow);
		  removeBottomBorder = participantNameSameAsNextRow && evidenceTypeSameAsNextRow;
	    }
	  
	    var tableCellChildren = tableRow.childNodes;
	    
	    // compare Name Column
	    if (removeBottomBorder) {
	    	for (var index1 = 0; index1 < tableCellChildren.length; index1++) {
	        	var column = tableCellChildren[index1];
	        	if (column.tagName == "TD") {
	        	  column.setAttribute("style", "border-bottom:none");
	        	}
	        	
	        	// for first column we expect an image, hide it where applicable
	        	if (index1 == 0) {
	            	var firstColumn = tempChildren[index+1].childNodes[0];
	              var img = firstColumn.children[0];
	              if (img.tagName == "IMG") {
	            	  img.setAttribute("style", "display:none");
	              }              
	        	}
	        }
	    }
	  }
	  scriptTag.innerHTML = tempNode.innerHTML;
  } 
};

/**
 * Compare 2 rows, the current row and the next row to see if they have the same value.
 * @param rowItem1 The first row to check.
 * @param rowItem2 The second row to check.
 * @returns true if the rows are the same, otherwise false.
 */
function compare2RowItemsColumns(rowItem1, rowItem2) {
	var row1Content = rowItem1.textContent;
	var row2Content = rowItem2.textContent;
	if (row1Content && row2Content) {
	  if (row1Content == row2Content) {
		return true;
	  }
	}
	return false;
};