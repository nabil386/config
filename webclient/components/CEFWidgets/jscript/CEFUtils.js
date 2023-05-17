/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2018, 2020.
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */
/*
 * Copyright 2011 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/* Modification History
 * ====================
 * 31-Jan-2020	CMC [RTC255384]   Falling back to CDEJ print function instead of CEFUtils._printNotesPage() when required.
 * 28-Jan-2020  CD  [RTC256887]   Swapping styles to the new main fonts.
 * 25-Nov-2019	CMC [RTC255170]   Ensuring context panel and main area print after expanding Note.
 * 27-Sep-2019	CMC [RTC253403]   Removed changes from 234842 and updated how Notes are printed.
 * 17-Aug-2018	CMC [RTC234842]   Added min-height print only css to Note iFrames.
 * 14-Jan-2016	JAY [CR00474858]  Space bar key press event
 * 07-May-2013  SD  [CR00383002]  Add require statement for curam.util.Dialog
 * 06-Apr-2013  SD  [CR00375669]  Added toggleAriaPressed function.
 * 28-Oct-2011  BD  [CR00296073]  Add function to open a modal dialog. 
 * 01 Jun 2011  SD  [CR00267939]  keyPressExist function added, prevents 
 *                                over-riding of click functionality with key
 *                                presses. 
 */
 var CEFUtils={
    
    /**
     * Returns true is key press event is triggered by the enter or space bar key.
     * Used by context panel toggle icon and list/cluster toggle icons.
     * Used by pod links key press event.
     */  
    enterKeyPress: function(event) { 
      if (event.keyCode === 13 || event.keyCode === 32) {
        return true;
      }
    },
    
    /**
     * Utility method to check if a key press event exists.
     */
    keyPressExist: function(event) {
      if (event.type === "keypress") {
        return true;
      }
    },
    
    /**
     * Open a modal using the URL of the event passed in.
     *
     * @param event the event raised by the users action.
     * @param size
     * 
     * @return the UimDialog object.
     */
    showInModal: function(event, size) {

      // IE doesn't pass event as argument.
      event = event || window.event; 

      // IE doesn't use .target
      var tgt = event.target || event.srcElement; 

      // stop the original event from being processed
      dojo.stopEvent(event); 
      
      // open the dialog.
      dojo.require('curam.util.UimDialog');
      return curam.util.UimDialog.openUrl(tgt.href, size);
    },
    
    /**
     * Toggles the aria-pressed tag attribute within specified DOM node. 
     * The parent identifier is required to set all associated aria-pressed
     * attributes to false to reset the values prior to the true state
     * being applied to the specified DOM node.
     *
     * @param parentId DOM node identifier of parent element.
     * @param id DOM node identifier of attribute to be updated.
     * 
     */    
    toggleAriaPressed: function(parentId, id) {
  
      // look for all immediate child <DIV>'s within DIV with specified ID
      var searchString = 'div#' + parentId + ' > div';
    
      // By setting all 'aria-pressed' attributes to be false, this removes the
      // previous state of any buttons which have been selected to be true
      dojo.query(searchString).forEach(
        function(labelElem) {
          dojo.attr(labelElem, "aria-pressed", "false");
        }
      );
    
      // only update specified button attribute
      dojo.attr(id, "aria-pressed", "true");
    },
    
    /**
     * Overwriting page level print icon to call CEFUtils._printNotesPage()
     * rather than curam.util.printPage().
     */
    _overwritePrintButtonToPrintNotesPage: function() {

      var iframe = window.frameElement;
      var iframeSRCOfNotesPage = iframe.src;
      
      var printButton = dojo.query("a[class=print]", iframe.parentElement.ownerDocument)[0];
      var cloneButtonClone = dojo.clone(printButton);
      dojo.place(cloneButtonClone, printButton, "after");
      dojo.attr(printButton, "style", "display: none;");

      cloneButtonClone.addEventListener("click", function(event) {
        event.stopPropagation();
        var mainAreaWindow = dojo.window.get(event.currentTarget.ownerDocument);
    	CEFUtils._printNotesPage(mainAreaWindow);
      });
      
      // Reset print button when Notes tab (iframe) is closed.
      var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {  
          if (iframeSRCOfNotesPage !== window.frameElement.src) {
            dojo.attr(printButton, "style", "");
            dojo.destroy(cloneButtonClone);
          }
      	});
      });
      
      observer.observe(iframe, { attributes: true }); 
      
    },
    
    /**
     * Handles click event on the page level print button when there are
     * Notes in the main content area.
     * @param mainAreaWindow The main area window which contains all Notes.
     * 
     */
    _printNotesPage: function(mainAreaWindow) {
      // Just print the main content area when printing of the context panel
      // is disabled.
      if (dojo.getObject('disable.context.panel.print') === 'false') {
        var printFrame = CEFUtils._createPrintFriendlyNotesWindow(mainAreaWindow);
        printFrame.contentWindow.focus();
        var ua = navigator.userAgent.toLowerCase();
        // If the browser is IE
        if((/msie/.test(ua)||/trident/.test(ua)) && !/opera/.test(ua)) {
          printFrame.contentWindow.document.execCommand('print', false, null);
        }
        else {
          printFrame.contentWindow.print();
        }
        dojo.destroy(printFrame);
        return false;
      }

      var mainAreaIframeNode = mainAreaWindow.frameElement;

      // Find the "tab-content-holder" HTML element which contains each tab
      var searchNode = mainAreaIframeNode;
      while(searchNode && !dojo.hasClass(searchNode, "tab-content-holder")) {
        searchNode = searchNode.parentNode;
      }
      var tabContentHolderNode = searchNode;

      var contextPanel = dojo.query(".detailsPanelFrame", tabContentHolderNode)[0];
      var hasContextPanel = contextPanel != undefined && contextPanel != null;
      if (hasContextPanel) {
          
        var isIE;
        var isEdge
        require(["dojo/sniff"], function(has){
          isIE = has('trident') || has('ie');
          isEdge = has('edge');
        });
      
        // If a context panel is collapsed in IE11, it will not print due to display: none styling.
        // Instead, the main content area is printed twice. The below code addresses that issue.
        var contextPanelCollapsed = dojo.hasClass(contextPanel.parentNode, "collapsed");
      
        if (isIE && contextPanelCollapsed) {
          dojo.setStyle(contextPanel.parentNode, "display", "block");
        }
        contextPanel.contentWindow.focus();
        contextPanel.contentWindow.print();
      
        if (isIE && contextPanelCollapsed) {
          dojo.setStyle(contextPanel.parentNode, "display", "");
        }
        
        if (isIE || isEdge) {  
          // Give the print dialog time to display
  	      setTimeout(function() {
  	        if (isEdge) {
  	          function printMainAreaNotesWindow() {
  	            CEFUtils._printMainAreaNotesWindow(mainAreaWindow);
  	            curam.util.getTopmostWindow().document.body.removeEventListener("mouseover", printMainAreaNotesWindow, true);
  	            return false;
  	          }
  	          // Edge will not trigger the onmouseover event on the browsers top most window until the
  	          // context panel print dialog is closed.
  	    	  curam.util.getTopmostWindow().document.body.addEventListener("mouseover", printMainAreaNotesWindow, true);
  	        } else if (isIE) {
  	          // Internet Explorer will pause timeout countdown when context panel print dialog is displayed, 
  	          // once this dialog is closed the timer will continue and we can print the main content area.  
  	          CEFUtils._printMainAreaNotesWindow(mainAreaWindow);
  	          return false;
  	        }
  	      }, 2000);
        } else {
          CEFUtils._printMainAreaNotesWindow(mainAreaWindow);    
          return false;
        }
      } else {
        CEFUtils._printMainAreaNotesWindow(mainAreaWindow);
      }

      return false;
    },
    
    /**
     * Prints the main area Notes window.
     */
    _printMainAreaNotesWindow: function(mainAreaWindow) {
      try {
	    var printFrame = CEFUtils._createPrintFriendlyNotesWindow(mainAreaWindow);
	    printFrame.contentWindow.focus();
	    var ua = navigator.userAgent.toLowerCase();
	    // If the browser is IE
	    if((/msie/.test(ua)||/trident/.test(ua)) && !/opera/.test(ua)) {
	      printFrame.contentWindow.document.execCommand('print', false, null);
	    }
	    else {
	      printFrame.contentWindow.print();
	    }
	    dojo.destroy(printFrame);
      } catch (err) {
    	// If not printing from Person Page -> Contact -> Notes tab,
    	// or if that UIM has been modified, fall back to the CDEJ
    	// print function to print the main content area.
    	dojo.destroy(dojo.query('.printFriendlyNotesFrame', curam.util.getTopmostWindow().document.body)[0]);
    	curam.util._prepareContentPrint(mainAreaWindow);
    	mainAreaWindow.focus();
    	mainAreaWindow.print();
    	curam.util._deletePrintVersion();
      }
    },
    
    /**
     * Creates a lightweight iFrame containing Notes which takes less memory to print.
     * @param  mainAreaWindow The main content area window which contains all Notes.
     * @return printFriendlyNotesFrame A lightweight iFrame containing Notes as they appear
     * on the page.
     */
    _createPrintFriendlyNotesWindow: function(mainAreaWindow) {
	  var printFriendlyNotesFrame = CEFUtils._createPrintFriendlyNotesFrame();
      printFriendlyNotesFrameDoc = printFriendlyNotesFrame.contentWindow.document;
	  var populatedNotesContainer = dojo.create("div");
		  
      dojo.withDoc(mainAreaWindow.frameElement.contentWindow.document, function() {
	    var parentTableOfNotes = dojo.query(".list table")[0];
		var rowContainer = dojo.create("div");
		       
        var fallbackFontFamily = "'Helvetica Neue', -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, 'Droid Sans', Helvetica, Arial, sans-serif;"
        var regularFontStyle = "font-weight: normal; font-family: MainRegularFontforIBM, " + fallbackFontFamily;
        var mediumFontStyle = "font-weight: 500; font-family: MainMediumFontforIBM, " + fallbackFontFamily;
        var boldFontStyle = "font-weight: bold; font-family: MainBoldFontforIBM, " + fallbackFontFamily;
    
		// Iterate through each Note list row
		dojo.query(parentTableOfNotes).query('tr[data-lix]').forEach(function(note) {
	 	  // For each Note, populate a table with 2 rows.
	 	  // One row with the main header cells (Subject, Last Update, ...)
	 	  // A second row with the specific header cells for each note.
	 	  // Add div after each table which is populated with the Note's main content if expanded.
	      if (note.style.display != 'none') {	
		    var populatedNoteTable = dojo.create("table");
		    dojo.attr(populatedNoteTable, "class", "populatedNotesTable");
		 	dojo.attr(populatedNoteTable, "style", "break-inside: avoid; table-layout: fixed; width: 100%;");
		 	var populatedNoteTableRow = dojo.create("tr");
		    var populatedNoteTableCells = note.cells;
		    for (var cellNumber = 1; cellNumber < populatedNoteTableCells.length-1; cellNumber++) {
		      var populatedNoteTableCell = dojo.create("td");
		      if (cellNumber == 3) {
		        dojo.attr(populatedNoteTableCell, "style", "color: #1f57a4; font-size: 14px; word-wrap: break-word; " +
		          boldFontStyle);
		      } else {
		        dojo.attr(populatedNoteTableCell, "style", "color: black; font-size: 14px; word-wrap: break-word; " +
				  regularFontStyle);
		      }
		      populatedNoteTableCell.innerHTML = populatedNoteTableCells[cellNumber].innerText;
		      dojo.place(populatedNoteTableCell, populatedNoteTableRow);
		    }
				
		    var mainTableHeading = CEFUtils._createMainTableHeaderRow(parentTableOfNotes);
		    dojo.place(mainTableHeading, populatedNoteTable);
		    dojo.place(populatedNoteTableRow, populatedNoteTable);
		    dojo.place(populatedNoteTable, rowContainer);
		     
		    var noteHistoryDiv = dojo.create("div");
		    dojo.attr(noteHistoryDiv, "style", "word-wrap: break-word; width: 90%; margin-left: 5%; font-size: 14px; " +
		       mediumFontStyle);
		    // If a Note is expanded, get the expanded Note content
		    if (!dojo.hasClass(note.nextSibling, "collapsed")) {
		      var noteContentIFrame = dojo.query(".contentPanelFrameWrapper", note.nextSibling)[0].firstElementChild;
		      noteHistoryDiv.innerHTML = CEFUtils._getNoteExpandedContent(noteContentIFrame.contentWindow.document).innerHTML;
		    }
		        
		    var noteSeperator = dojo.create("hr");
		    dojo.attr(noteSeperator, "style", "border: 1px solid #e0e0e0;");
		        
		    dojo.place(noteHistoryDiv, rowContainer);
		    dojo.place(noteSeperator, rowContainer);
	      }
	    });
		  
		var notesHeading =  dojo.create("h2");
		dojo.attr(notesHeading, "style", "color: black; font-size: 21px; " +
		  regularFontStyle);
		notesHeading.innerText = dojo.byId('bodyTitle').innerText;
		  
		dojo.place(notesHeading, populatedNotesContainer);
	    dojo.place(rowContainer, populatedNotesContainer);
	    dojo.place(populatedNotesContainer, printFriendlyNotesFrameDoc.body);
	  });
	     
	  return printFriendlyNotesFrame;
	},
	  
    /**
     * Creates a lightweight iFrame off screen to populate with content to print.
     * @return printFriendlyNotesFrame An empty iframe to populate with Notes data.
     */
	_createPrintFriendlyNotesFrame: function() {
      var printFriendlyNotesFrame = dojo.create("iframe");
      dojo.attr(printFriendlyNotesFrame, "class", "printFriendlyNotesFrame");
      dojo.attr(printFriendlyNotesFrame, "style", "border: none; position: absolute; left: -5000px; tabindex: -1;");
      dojo.attr(printFriendlyNotesFrame, "aria-hidden", "true;");
      dojo.place(printFriendlyNotesFrame, curam.util.getTopmostWindow().document.body);
		       
	  return printFriendlyNotesFrame;
    },
    
    /**
     * Creates the main table header row made up of table cells as they appear on the page.
     * Example: Subject, Note Text, Entered By, Date, Priority, Status.
     * @param  parentTableOfNotes The HTML table element which contains all Notes.
     * @return populatedMainHeaderRow A populated HTML table row.
     */
	_createMainTableHeaderRow: function(parentTableOfNotes) {
	
    var fallbackFontFamily = "'Helvetica Neue', -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, 'Droid Sans', Helvetica, Arial, sans-serif;"
    var boldFontStyle = "font-weight: bold; font-family: MainBoldFontforIBM, " + fallbackFontFamily;
	
      var populatedMainHeaderRow = dojo.create("tr");
      dojo.attr(populatedMainHeaderRow, "class", "populatedTableHeaderRow");
	  dojo.setAttr(populatedMainHeaderRow, "style", "break-inside: avoid; color: #5A5A5A; " +
		"font-size: 14px; " + boldFontStyle);
			    	    
	  var cells = parentTableOfNotes.tHead.firstElementChild.cells;
	  for (var cellNumber = 1; cellNumber < cells.length-1; cellNumber++) {
	    var populatedMainHeaderCell = dojo.create("td");
		if (cellNumber == 2) {
		  dojo.setAttr(populatedMainHeaderCell, "style", "width: 30%;");
		}
		
		populatedMainHeaderCell.innerHTML = cells[cellNumber].innerText;
	    populatedMainHeaderRow.appendChild(populatedMainHeaderCell);
	  }
			       
	  return populatedMainHeaderRow;
	},
   
    /**
     * Get the Note history which appears when a Note is expanded.
     * @param  iFrameDoc  An iFrame containing the expanded Note content.
     * @return expandableNoteContentContainer A HTML div element containing the expanded Note iFrame).
     */
	_getNoteExpandedContent: function(iFrameDoc) {
	
    var fallbackFontFamily = "'Helvetica Neue', -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, 'Droid Sans', Helvetica, Arial, sans-serif;"
    var regularFontStyle = "font-weight: normal; font-family: MainRegularFontforIBM, " + fallbackFontFamily;
    var mediumFontStyle = "font-weight: 500; font-family: MainMediumFontforIBM, " + fallbackFontFamily;
    var boldFontStyle = "font-weight: bold; font-family: MainBoldFontforIBM, " + fallbackFontFamily;
	
	  var expandableNoteContentContainer = dojo.create("div");
	  dojo.attr(expandableNoteContentContainer, "class", "expandableNoteContentContainer");
	  dojo.attr(expandableNoteContentContainer, "style", "padding-left: 7px; padding-top: 15px;");
	     
	  dojo.withDoc(iFrameDoc, function() {	      		  
	    var personPageNote = dojo.byId("note-history");
	    var noteTitles = dojo.query("tr[class=title]");
	    var noteBodies = dojo.query("tr[class=body]");
	       
	    //Get the Note Sensitivity content (Example: Sensitivity = 1)
	    var noteSensitivity = dojo.create("div");
	    dojo.attr(noteSensitivity, "style", "padding-bottom: 15px;padding-top: 15px; " +
	      mediumFontStyle + " font-size: 14px;");
	    var noteSensitivityText = dojo.create("span");
		dojo.attr(noteSensitivityText, "style", boldFontStyle + " padding-right:12px");
	    noteSensitivityText.innerHTML =  dojo.query("th[class=label]")[0].firstElementChild.innerText;
	    var noteSensitivityValue = dojo.create("span");
	    noteSensitivityValue.innerHTML =  dojo.query("th[class=label]")[0].firstElementChild.parentNode.nextSibling.innerText;
	    dojo.place(noteSensitivityText, noteSensitivity);
	    dojo.place(noteSensitivityValue, noteSensitivity);
	      
	    dojo.place(noteSensitivity, expandableNoteContentContainer);
	      
	    //Get the Note History heading (Example: Note History)
	    var notesHistoryHeading =  dojo.create("h3");
	    dojo.attr(notesHistoryHeading, "style", "color: #1d3649; font-size: 18px; " +
	      regularFontStyle + " margin-top: 6px;");
	    notesHistoryHeading.innerHTML = dojo.query("span[class=collapse-title]")[0].innerText;
	    dojo.place(notesHistoryHeading, expandableNoteContentContainer);
	    var noteSeperator = dojo.create("hr");
	    dojo.attr(noteSeperator, "style", "border: 1px solid #e0e0e0;");
	    dojo.place(noteSeperator, expandableNoteContentContainer);
	       
	    // Iterate through each Note and add its content to the expandableNoteContentContainer
	    for (var noteNumber = 0; noteNumber < noteTitles.length; noteNumber++) {	  
	      var noteAuthorDate = dojo.create("div");
	      var author = dojo.create("span");
	      dojo.attr(author, "style",  "color: #1f57a4; " + boldFontStyle + 
	        " font-size: 14px;  padding-right: 12px;");
	      author.innerHTML = dojo.query(".author", noteTitles[noteNumber])[0].firstElementChild.innerHTML;
	      var on = dojo.create("span");
	      dojo.attr(on, "style", regularFontStyle +
	        " font-size: 14px; padding-right: 12px;");
	      on.innerHTML = noteTitles[noteNumber].getElementsByClassName('date')[0].innerHTML;
	      var date = dojo.create("span");
   	      dojo.attr(date, "style", regularFontStyle + " font-size: 14px;");
	      date.innerHTML = dojo.query(".date", noteTitles[noteNumber])[1].innerHTML;
	      dojo.place(author, noteAuthorDate);
	      dojo.place(on, noteAuthorDate);
	      dojo.place(date, noteAuthorDate);
	      dojo.place(noteAuthorDate, expandableNoteContentContainer);
	    		  
	      var noteMainContent = dojo.create("div");
	      noteMainContent.innerHTML = dojo.query(".rich-text", noteBodies[noteNumber])[0].innerHTML;
	      dojo.place(noteMainContent, expandableNoteContentContainer);
	    }
	  });
	    
	  return expandableNoteContentContainer;
	}
};

