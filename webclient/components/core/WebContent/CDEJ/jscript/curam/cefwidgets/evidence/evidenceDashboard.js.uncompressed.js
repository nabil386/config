/* 
 * Licensed Materials - Property of IBM
 * 
 * PID 5725-H26
 * 
 * Copyright IBM Corporation 2012,2021. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/**
 * Modifications
 * -------------
 * 24-Feb-2020 FN  [RTC242665] Amended createLegend() to prevent screen reader from reading decorative icons legend.
 * 15-Aug-2019 SH  [233930] Handle evidence dashboard for cases with no evidence types.
 * 11-Feb-2015 DM  [CR00457538] Evidence Dashboard - child/parent relationships enhancement
 * 23-Oct-2013 DM  [CR00447889] Accessibility - tabbing oder for columns and toggle button.
 * 06-Apr-2013 SD  [CR00375669] Added aria-pressed logic to evidence labels.
 * 04-Jan-2013 SD  [CR00354220] RPT fix for evidence dashboard.
 * 03-Oct-2012 BD  [CR00345902] Dojo 1.7 Upgrade. Migrate code.
 */
require(["dojo/_base/connect","curam/cefwidgets/evidence/DashboardTitlePane"]);

dojo.require("dojo.NodeList-traverse"); 
dojo.require("dijit.form.Select");
dojo.require('curam.util.UimDialog');
// BEGIN, CR00399746, MV
dojo.registerModulePath("cef", "../..");
// END, CR00399746
var pageID;
var participantID;
var caseID=0;
var issueTooltip = '';
var verificationTooltip = '';
var inEditTooltip = '';
var createTooltip = '';
var currentFilter='all';
var currentFilterClass= '';
var mandatoryTooltip='';
var filterListLabel='';

var iconVerifications="../Images/items-to-verify__filled-triangle--20-enabled.svg";
var iconInEditEvidence="../Images/evidence-in-edit__filled-square--20-enabled.svg";
var iconIssues="../Images/issues__filled--20-enabled.svg";
var iconCreate="../Images/add--20-enabled.svg";

// BEGIN, CR00447163, JAF
var highContrastIconVerifications="../Images/highcontrast/dashboard_icon_verifications.png";
var highContrastIconInEditEvidence="../Images/highcontrast/dashboard_icon_inedit.png";
var highContrastIconIssues="../Images/highcontrast/dashboard_icon_issue.png";
var highContrastIconCreate="../Images/highcontrast/dashboard_icon_create.png";
// END, CR00447163, JAF

var rowCounter;
var colCounter;
var allColumnPartitions;
var recordedColumnPartitions;
    
dojo.global.createEvidenceDashboard = function(
  domLocation, caseID, issueText, verificationText, ineditText, notRecordedText,
  recordedText, allText, selectedText, expandText, collapseText, createText, mandatoryText, 
  filterListText, filterNoneText, evidenceTypesText, columnPartitionsAll, columnPartitionsRecorded, pageID, participantID, customFilterPage) {
  
  	allColumnPartitions = columnPartitionsAll;
  	recordedColumnPartitions = columnPartitionsRecorded;
    
    if (allColumnPartitions === undefined || allColumnPartitions.length == 0) {
      // with no evidence types defined, the dashboardView node is in the wrong location; move it up a level
      // var dashboardData = dojo.byId("dashboardData");
      // dashboardData.parentNode.appendChild(dojo.byId("dashboardView"));

      createMissingEvidenceTypesMessage(evidenceTypesText);
    }
    else {
      createLegend(domLocation, issueText, verificationText, ineditText, filterListText, filterNoneText, customFilterPage);

      this.caseID = caseID;
      this.participantID=participantID;
      this.pageID=pageID;
      this.issueTooltip = issueText;
      this.verificationTooltip = verificationText;
      this.inEditTooltip = ineditText;
      this.createTooltip = createText;
      this.mandatoryTooltip = mandatoryText;
      this.filterListLabel = filterListText;
    
      var dashboardData = dojo.byId("dashboardData");
    
      var i = 0; 
      
      // For each row
      dojo.forEach(dashboardData.children, function(entry) {
      
        var catName = dojo.attr(entry, "value");        
        var tableId = createCategorySection(domLocation, i, catName, notRecordedText,
          recordedText, allText, selectedText, expandText, collapseText);
          
        filterDashboard('all',tableId,tableId,i);        
        i++;
      });
          
      // BEGIN, CR00443380, YF
      dojo.query(".dijitTitlePaneTitleFocus").forEach(function(node){
        dojo.attr(node,"role", "button");
        // BEGIN, CR00447163, JAF
        dojo.attr(node, "class", "evidence_dijitTitlePaneTitleFocus");
        // END, CR00447163, JAF
      });
      // END, CR00443380
    }
}

function createMissingEvidenceTypesMessage(evidenceTypesText) {
  var errorMessagesDiv = dojo.byId(curam.util.ERROR_MESSAGES_CONTAINER);
  var list = dojo.byId(curam.util.ERROR_MESSAGES_LIST);
  
  if (!list) {
    // create list for error messages
    list = dojo.place("<ul id='error-messages' class='messages' />", errorMessagesDiv);
  }
  // append informational
  dojo.place("<li class='level-2'><div><span>Informational Message: </span>" + evidenceTypesText + "</div></li>", list);
}

function createLegend(domLocation, issueText, verificationText, ineditText, filterListText, filterNoneText, customFilterPage){
    //
    // Setup the legend
    //
    var table=dojo.create('table',{role: 'presentation'}, domLocation);
    dojo.addClass(table, 'dashboardHeader');
    var tr=dojo.create('tr', null, table);
    var tdFilter=dojo.create('td', {id: 'filterColumn'}, tr);
    var tdIcons=dojo.create('td', {id: 'iconsColumn'}, tr);

    //
    // First column of the Legend, holding the  "Dashboard Filter Groups"
    //    
    var dashboardFilters = dojo.byId("dashboardFilters");
  	if (dashboardFilters) {
  	   var listDiv = dojo.create("div",null,tdFilter);
       dojo.addClass(listDiv, 'dashboardFilter codetable');
    
       var filterLabel = dojo.create("span", {innerHTML:filterListText, title:filterListText},listDiv); 
       dojo.setStyle(filterLabel,{'padding':'0 20px 0 10px', 'vertical-align':'top'});
         	
       var filterSelectList = dojo.create("select", {'data-dojo-type':'dijit/form/Select', maxHeight:'200',maxWidth:'250', id:'dashboardFilterSelection',title:filterListText, tabindex:'0'}, listDiv);
       dojo.addClass(filterSelectList, 'dijitComboBox');
       dojo.setStyle(filterSelectList,{'max-height':'200', 'max-width':'250'});
       dojo.attr(filterSelectList, 'onchange', 'refreshEvidenceDashboard(this.value,\''+customFilterPage+'\')');
   
       var filterOption;
         
       filterOption = dojo.create("option", {innerHTML:filterNoneText, value:"-"}, filterSelectList);
   
       //get the immediate evidence items children for the matching evidence category
       var filtersList = dojo.query('li',dashboardFilters);
  	
       dojo.forEach(filtersList, function(entry){
    	 var filterName = dojo.attr(entry, "name");
         var filterId = dojo.attr(entry, "id");
         var filterSelected = dojo.attr(entry, "selected"); 
         
         if (filterSelected=='true'){
           filterOption = dojo.create("option", {innerHTML:filterName, value:filterId, 'selected':'selected', label:filterName}, filterSelectList);   
         } else{
            filterOption = dojo.create("option", {innerHTML:filterName, value:filterId}, filterSelectList);        
         }
        //dojo.addClass(span_issue, 'dashboardLegendSpan');
        });
     }
     
     var legendDiv = dojo.create("div");
     dojo.place(legendDiv, iconsColumn);
     dojo.addClass(legendDiv, 'dashboardLegend');
    
    // Issues
    var span_issue = dojo.create("span", {innerHTML:issueText},legendDiv);   
    dojo.addClass(span_issue, 'dashboardLegendSpan');
    dojo.attr(span_issue, "aria-hidden", "true");
    // BEGIN, CR00447163, JAF
    if(curam.util.highContrastModeType()){
      var image_issue = dojo.create("img" ,
        {src:highContrastIconIssues, alt:"", style: {verticalAlign:"middle"}},span_issue);
      dojo.place(image_issue,span_issue,'before');
    } else {
      var image_issue = dojo.create("img" ,
        {src:iconIssues, alt:"", style: {verticalAlign:"middle"}},span_issue);
      if(curam.util.isRtlMode()){
        dojo.place(image_issue,span_issue,'after');
      } else {
        dojo.place(image_issue,span_issue,'before');
      }
    }
    // END, CR00447163, JAF
    
    // Verifications
    var span_verification = dojo.create("span", {innerHTML:verificationText},legendDiv);
    dojo.addClass(span_verification, 'dashboardLegendSpan'); 
    dojo.attr(span_verification, "aria-hidden", "true");
    // BEGIN, CR00447163, JAF       
    if(curam.util.highContrastModeType()){
      var image_verify = dojo.create("img" ,
        {src:highContrastIconVerifications, alt:"", style: {verticalAlign:"middle"}},span_issue);
      dojo.place(image_verify,span_verification,'before');
    } else {
      var image_verify = dojo.create("img" ,
        {src:iconVerifications, alt:"", style: {verticalAlign:"middle"}},span_verification);
      if(curam.util.isRtlMode()){
        dojo.place(image_verify,span_verification,'after');
      } else {
        dojo.place(image_verify,span_verification,'before');
      }
    }
    // END, CR00447163, JAF
    // InEdit Evidence
    var span_inEdit = dojo.create("span", {innerHTML:ineditText},legendDiv);
    dojo.addClass(span_inEdit, 'dashboardLegendSpan');
    dojo.attr(span_inEdit, "aria-hidden", "true");
    // BEGIN, CR00447163, JAF
    if(curam.util.highContrastModeType()){
      var image_inEdit = dojo.create("img" ,
        {src:highContrastIconInEditEvidence, alt:"", style: {verticalAlign:"middle"}},span_issue);
      dojo.place(image_inEdit,span_inEdit,'before');
    } else {
      var image_inEdit = dojo.create("img" ,
        {src:iconInEditEvidence, alt:"", style: {verticalAlign:"middle"}},span_inEdit);
      if(curam.util.isRtlMode()){
        dojo.place(image_inEdit,span_inEdit,'after');
      } else {
        dojo.place(image_inEdit,span_inEdit,'before');
      }
    }
    // END, CR00447163, JAF
}

function createCategorySection(domLocation, i, catName, notRecordedText,
  			recordedText, allText, selectedText, expandText, collapseText){
    //
    // Create content panel
    //
    var contentPanelId = 'ContentPanel_' + i;
    var dashboardID = "Dashboard_" + contentPanelId;
    var tableId = 'Table_' + contentPanelId;
        
    //
    // Create labels.
    //
    var contentPanelLabels = dojo.create("div", {
        id: "LabelHolder_" + contentPanelId
    });
    dojo.addClass(contentPanelLabels, 'contentPanelLabelsStyle');
    
    var contentPanelLabelsHolder = dojo.create("div", {id: "LabelHolder_" + contentPanelId+"_inner", role: "application", "aria-label": catName 
    }, contentPanelLabels);
        
    // Create labels for navigator.
        
    //BEGIN, CR00447889, DM
    
	//
    // All
    //
    createCategoryHeader(contentPanelLabelsHolder, tableId, i, 'All_Label_' + contentPanelId, selectedText, allText, 'all');

    //
    // Recorded
    //
    createCategoryHeader(contentPanelLabelsHolder, tableId, i, 'Recorded_Label_' + contentPanelId, selectedText, recordedText, 'recorded');
   

    //
    // Create a table to hold the dashboard
    //
    var table = dojo.create('table', {
      id: tableId,
      role: 'grid',
      width: '100%'
    });
    dojo.addClass(table, 'treeHolderStyle');
    var tbody = dojo.create("tbody", null, table);

    //
    // Create the  pane for the evidence category
    //
    titlePane = new curam.cefwidgets.evidence.DashboardTitlePane({
    	title: catName,
    	id:  'Title_'+contentPanelId,
    	navigationContent: contentPanelLabels,
    	content: table,
    	selectedButtonAltText: selectedText,
    	toggleExpandAltText: expandText,
    	toggleCollapseAltText: collapseText});
    	
    dojo.place(titlePane.domNode, domLocation);
    
    var ariaLabelHolder=(dojo.NodeList(titlePane.domNode)[0]).querySelector('#Title_'+contentPanelId+'_pane');
    
	if (ariaLabelHolder != "undefined") {
		dojo.attr(table, 'aria-label',dojo.attr(ariaLabelHolder,'aria-label'));
	}
	
    // Add a line break
    var lineBreak = dojo.create("br");
    dojo.place(lineBreak, domLocation);
        
    dojo.place(tbody, domLocation);
        
    return tableId;
}

function createCategoryHeader(contentPanelLabels, tableId, i, id, selectedText, text, label){

    var background = dojo.create("div" ,{id: id,
            labelType: label,
            customtype: 'label', 
            role: 'button', 
            tabindex: '0'});
    dojo.attr(background, "aria-pressed", "false");
                                                    
    addEvent(background,'mouseover',filterMouseIn);
    addEvent(background,'mouseout',filterMouseOut);
    dojo.place(background, contentPanelLabels);
        
    if(currentFilter==label) {
       dojo.addClass(background, 'backgroundHighlight');
       dojo.attr(background, "alt", selectedText+" "+text);
    } else {
       dojo.addClass(background, 'backgroundNormal');
       dojo.attr(background, "alt", "");
    }

    var backgroundSelected = dojo.create("div");
    dojo.place(backgroundSelected, background);
        
    var filterSpan = dojo.create("span", {
         innerHTML: text
    }, backgroundSelected);
    dojo.addClass(filterSpan, 'filterLabelText');
        
    var args = [label, tableId, contentPanelLabels.id, i, background];
    dojo.connect(background, "onclick", dojo.partial(startFilter,args));
    dojo.connect(background, "onkeypress", dojo.partial(startFilterOnKeyPress,args));
}

function startFilterOnKeyPress(args, event){
	if (CEFUtils.enterKeyPress(event)) { 
	    startFilter(args); 
	}
}

function addEvent( obj, type, fn ) {
  if (obj.addEventListener) {
    obj.addEventListener( type, fn, false );
  }
  else if (obj.attachEvent) {
    obj["e"+type+fn] = fn;
    obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
    obj.attachEvent( "on"+type, obj[type+fn] );
  }
  else {
    obj["on"+type] = obj["e"+type+fn];
  }
}

function filterMouseOut() {
  this.className=currentFilterClass;
}

function filterMouseIn() {
  currentFilterClass=this.className;
  if(this.className == 'backgroundNormal') {
    this.className='backgroundNormalRollover';
  }
  else if(this.className == 'backgroundHighlight') {
    this.className='backgroundHighlightRollover';
  }
}

function startFilter(args, event) {
  currentFilter=args[0];
  filterDashboard(args[0], args[1], args[2], args[3]);
  
  // look for all immediate child <DIV>'s within DIV with specified ID
  var searchString = 'div#' + args[4].parentNode.id + ' > div';
    
  // By setting all 'aria-pressed' attributes to be false, this removes the
  // previous state of any buttons which have been selected to be true
  dojo.query(searchString).forEach(
  function(labelElem) {
     dojo.attr(labelElem, "aria-pressed", "false");
     dojo.attr(labelElem, "class", "backgroundNormal");
  }
  );
    
  // only update specified button attribute
  dojo.attr(args[4], "aria-pressed", "true");
  dojo.attr(args[4], "class", "backgroundHighlight");
  
}

/**
 *Filter the evidence dashboard to show 'All' or 'Recorded' evidence types
 */
function filterDashboard(option, tableId, labelHolderId, panelNumber) {
    //1. Use tableID to find the table node
    //2. Use the panel number (0, 1,2 etc) to identify the category
    //3. Only add evidence where option matches (recorded,NotRecorded,All)
    var rowMax;
    
    //get the array holding the number of rows for each of the three columns
    //this depends on whether we are in the "All" or "Recorded" view
    if (option=='recorded'){
    	rowMax=recordedColumnPartitions[panelNumber];
    } else {
    	rowMax=allColumnPartitions[panelNumber];
    }

    if (rowMax === undefined || rowMax.length == 0) {
      return;
    }

    // Get the table node
    var existingTable = dojo.byId(tableId);
    var dashboardData = dojo.byId("dashboardData");

    // Create a new table
    var newTable = dojo.create('table', {
        id: tableId,
        role:'grid',
        width: '100%'
    });
    
    dojo.attr(newTable,'aria-label', dojo.attr(existingTable,'aria-label'));
    
    dojo.addClass(newTable, 'treeHolderStyle');
    var thead = dojo.create("thead", null, newTable);    
    var tbody = dojo.create("tbody", null, newTable);

    //get the immediate evidence items children for the matching evidence category
    var evidenceNodes = dojo.query('>',(dashboardData.children[panelNumber]).children[0]);
    
    //resetting row counter and column counter for category
    rowCounter = 0;
    colCounter = 0;    
    
    var tableHeaderRow = dojo.create('tr',null,thead);

    var tr = dojo.create('tr',{
      valign:"top"
    },tbody);
    
    //create a new column
    var td = dojo.create('td',{width:'34%'},tr);
    dojo.addClass(td,'dashboardColumn');
    
    tdDiv = dojo.create('div',{role: 'rowgroup'}, td);
    dojo.addClass(tdDiv,'dashboardColumnContent');
    
    //parse all the evidence nodes 
    var x;
    for (x = 0; x < evidenceNodes.length; x++){ 
    
      var showEvidence=true;
      
      // Add a <th> element for each <td>, satisfies RPT checks
      dojo.create('th', null, tableHeaderRow);
      
      if (option == 'recorded' &&  dojo.attr(evidenceNodes[x], "recorded")!="true") {
        showEvidence=false;
      } 
      
      if (showEvidence) {
        
        // if we have reached the limit for the row, then create a new column
        if(rowCounter > (rowMax[colCounter]-1)) {
        	colCounter++;
        	
          	td = dojo.create('td',{width:'33%'},tr);
          	
          	if (colCounter<2) {
    			dojo.addClass(td,'dashboardColumn');
    		}
          	
          	tdDiv = dojo.create('div',{role: 'rowgroup'}, td);
          	dojo.addClass(tdDiv,'dashboardColumnContent');
    		
          	rowCounter=0;
        } 		
        
        var trchildId = "tr_" +colCounter+ "_" +rowCounter+ "_" + tableId;
        
        createEntityRowContent(option,evidenceNodes[x], trchildId, tdDiv, caseID, 0, rowMax);
               
      }
    } 
    
    //if we have added all the evidence items, but that didn't create 3 columns
    // then we need to create the additional number of null columns
    if ((x == evidenceNodes.length) && (colCounter<2)){
    	for (var iterator=++colCounter;iterator<=2;iterator++){
    		td = dojo.create('td',{width:'33%'},tr);
    		
    		if (iterator<2) {
    			dojo.addClass(td,'dashboardColumn');
    		}
          	
        	tdDiv = dojo.create('div',{role: 'rowgroup'}, td);
        	dojo.addClass(tdDiv,'dashboardColumnContent');
    		
    	};
    }
    
    // Replace old table
    dojo.place(newTable,existingTable,"replace");
   
    this.currentFilter=option;
    currentFilterClass='backgroundHighlight';
}


function createEntityRowContent(option,evidenceNode, parentId, parent, caseID, space, rowMax){

		var trchild = dojo.create('tr',{
        	id:parentId,
        	tabindex:0,
        	role:'row'
        },parent);

		//column to hold the evidence text and icons
        var tdchild = dojo.create('td', {
                  id: parentId+'_text',
                  role:'gridcell'
              }, trchild);
        dojo.addClass(tdchild, 'dashboardTextColumn');
              
        //column to hold the "create new evidence" icon
        var tdPlus = dojo.create('td', {
        		  id: parentId+"_create",
        		  role:'gridcell'
              }, trchild);
        dojo.addClass(tdPlus, 'dashboardImageColumn');
        
        createEntry(tdchild, tdPlus, evidenceNode, caseID, space);
        
        var readOnlyEvidence=dojo.attr(evidenceNode, "readonly");
        
        dojo.attr(trchild,'onmouseover','highlightRow(this,\''+readOnlyEvidence+'\')');
        dojo.attr(trchild,'onmouseout','dehighlightRow(this)');
        dojo.attr(trchild,'onfocus','highlightRow(this,\''+readOnlyEvidence+'\')');
        dojo.attr(trchild,'onclick','highlightRow(this,\''+readOnlyEvidence+'\')');
        dojo.attr(trchild,'onblur','this.style.backgroundColor=\'transparent\'');
        dojo.attr(trchild,'onkeypress',dojo.partial(fadeRow,[trchild]));
        
        //getting the evidence types who are immediate children of the current evidence type
        var ulElem=evidenceNode.getElementsByTagName('ul')[0];
        var childEvidenceList;
        if (typeof(ulElem) != "undefined") {
        	childEvidenceList = dojo.query('>',ulElem);
        }
        
        if (typeof(childEvidenceList) != "undefined"){
        	if (childEvidenceList.length>0){
        
            	//each child needs to be idented more relative to its previous parent, so we are increasing the space
            	space++;
        	
        		//for each child, create a new row, by calling this same method recursively
        		for (var y = 0; y < childEvidenceList.length; y++){ 
        			var displayChild="true";
        			//in "recorded" filter view, only create a new row for the child if the chils is a recorded evidence
        			if ((option=="recorded")&&(dojo.attr(childEvidenceList[y],"recorded")!="true")){
        				displayChild="false";
        			}
        			if (displayChild=="true"){
        				createEntityRowContent(option,childEvidenceList[y], parentId+"_"+y, parent, caseID, space, rowMax );
        			}
        		}
        
        	}
        }
        
        rowCounter++;
}


/**
  *Create an Evidence entry on the dashboard. 
  *An entry displays the evidence type name which links to the appropriate 
  *workpace view. If there are any inedit records, issues or verifications for
  *that evidence type then an appropriate image will be displayed beside the
  *link.
  */
function createEntry(evidenceEntry, plusEntry, evidenceDetails, caseID, space) {

   //seeting identation for children and grandchildren elements
   var paddingValue=space*15;
   
   
   //creating the  <div> to hold the evidence text
   var divText = dojo.create('div', {
           height:'22px'
   }, evidenceEntry);
   
   if(dojo.isBodyLtr()){
   	  dojo.setStyle(divText,{'padding':'0 0 0 '+paddingValue+'px', 'vertical-align':'middle'});
   } else {
      dojo.setStyle(divText,{'padding':'0 '+paddingValue+'px 0 0', 'vertical-align':'middle'});
   };
   
   var evidenceText="";
   if (space>0){
   	   evidenceText="-&nbsp;"+dojo.attr(evidenceDetails, "name");
   } else {
	   evidenceText=dojo.attr(evidenceDetails, "name");
   }
   
   
   var textSpan;
   
	//if the evidence is recorded then we display it as a hyperlink, otherwise as normal text
	//if the evidence is a child, it must be idented relative to its parent and italicized
    if ((dojo.attr(evidenceDetails, "recorded")=="true")){
       textSpan=dojo.create('span', null, divText);
       
   	   var hrefSpan= dojo.create('a', {
   	   href: 'Evidence_workspaceTypeListPage.do?caseID='+this.caseID+'&evidenceType='+ dojo.attr(evidenceDetails,'evidenceType'),
   	   tabindex:0,
   	   onfocus: 'highlightParentRow(this,\''+dojo.attr(evidenceDetails, "readonly")+'\')',
   	   title: dojo.attr(evidenceDetails, "name"),
   	   'innerHTML':evidenceText
   	   }, textSpan);
   	   
   	   if (space>0){
   	        dojo.setStyle(hrefSpan, {'font-style': 'italic'});
	   }
	   
	   if (dojo.attr(evidenceDetails, "mandatory")=="true"){
           dojo.addClass(textSpan, "mandatory-label");
       }
	   
	   //creating the "evidence issues" image
       if(dojo.attr(evidenceDetails, "issues")!="0") {
       // BEGIN, CR00447163, JAF
       if(curam.util.highContrastModeType()){
         var image = dojo.create("img" ,
          {src:highContrastIconIssues, style: {verticalAlign:"top"}, alt:this.issueTooltip, title:this.issueTooltip},divText);
       } else {
         var image = dojo.create("img" ,
          {src:iconIssues, style: {verticalAlign:"top"}, alt:this.issueTooltip, title:this.issueTooltip},divText);
       }
       // END, CR00447163, JAF
       }
  
       //creating the "evidence verification" image
       if(dojo.attr(evidenceDetails, "verification")!="0") {
       // BEGIN, CR00447163, JAF
       if(curam.util.highContrastModeType()){
       var image = dojo.create("img" ,
         {src:highContrastIconVerifications, style: {verticalAlign:"top"}, alt:this.verificationTooltip, title:this.verificationTooltip},divText);
       } else {
       var image = dojo.create("img" ,
        {src:iconVerifications, style: {verticalAlign:"top"}, alt:this.verificationTooltip, title:this.verificationTooltip},divText);
       }
       // END, CR00447163, JAF
       } 

      //creating the "evidence in edit" image
      if(dojo.attr(evidenceDetails, "inedit")!="0") {
      // BEGIN, CR00447163, JAF
      if(curam.util.highContrastModeType()){
        var image = dojo.create("img" ,
         {src:highContrastIconInEditEvidence, style: {verticalAlign:"top"}, alt:this.inEditTooltip, title:this.inEditTooltip},divText);
      } else {
        var image = dojo.create("img" ,
        {src:iconInEditEvidence, style: {verticalAlign:"top"}, alt:this.inEditTooltip, title:this.inEditTooltip},divText);
      }
      // END, CR00447163, JAF
      } 
	   
   } else {
        textSpan=dojo.create('span', {'innerHTML': evidenceText}, divText);
   		if (space>0){
   		    dojo.setStyle(textSpan, {'font-style': 'italic'});
   	   	} 
   	   	if (dojo.attr(evidenceDetails, "mandatory")=="true"){
           dojo.addClass(textSpan, "mandatory-label");
        }
   }
   

   
  //adding the "create evidence" image on mouseover
  var createImagePath;
  if(curam.util.highContrastModeType()){ 
  	createImagePath=highContrastIconCreate;
  } else {
  	createImagePath=iconCreate;
  }
  
  var image = dojo.create("img" ,
    	{
    	id:plusEntry.id+'_icon',
    	tabindex:0,
    	role:'button',
   	    onfocus: 'highlightParentRow(this,\''+dojo.attr(evidenceDetails,"readonly")+'\')',
   	    onmouseover: 'highlightParentRow(this,\''+dojo.attr(evidenceDetails,"readonly")+'\')',
   	    onblur: 'dehighlightParentRow(this)',
   	    //onkeydown: 'dojo.partial(focusParentRow,[this])',
    	src: createImagePath,
    	alt:this.createTooltip, 
    	title:this.createTooltip,
    	href:'#',
    	onclick: 'openCreateEvidenceModal(\''+dojo.attr(evidenceDetails,'evidenceType')+'\')',
    	onkeypress: 'if (CEFUtils.enterKeyPress(event)) {openCreateEvidenceModal(\''+dojo.attr(evidenceDetails,'evidenceType')+'\')}'
    	},plusEntry);
}

/**
  * Open the modal for creating a piece of evidence of a certain type for the current caseID.
  * When the modal is closed, refresh the evidence dashboard and display the "All" view by default.
  */
function openCreateEvidenceModal(evidenceType){

	var dialogObject = curam.util.UimDialog.open(
  		'Evidence_resolveCreateFromMetaDataPage.do', { caseID:this.caseID,evidenceType:evidenceType } );
  
}

function refreshEvidenceDashboard(value, customFilterPage){
    if (value && (value!='')){
        require(["curam/util/Navigation"],function(nav){
            nav.goToUrl(customFilterPage+"Page.do?o3ctx=4096&caseID="+this.caseID+"&pageID="+customFilterPage+"&participantID="+this.participantID+"&groupFilterID="+value+"&customFilterPage="+customFilterPage);
        });
     }
}

function highlightRow(parent,readOnlyEvidence){
	if (parent)
	{
		parent.style.backgroundColor="rgba(224,224,224,0.5)";
		if (readOnlyEvidence=="false"){
   		  var imgElem=parent.querySelector("#"+parent.id+"_create_icon");
		  imgElem.style.visibility = "visible";
		} 
	}
}

function dehighlightRow(parent){
	if (parent)
	{
		parent.style.backgroundColor="transparent";
		var imgElem=parent.querySelector("#"+parent.id+"_create_icon");
		imgElem.style.visibility = "hidden";
	}
}
function fadeRow(args, event){
	if (args)
	{
		if ((event.keyCode == 9) && event.shiftKey){
		   args[0].style.backgroundColor="transparent";
		   var imgElem=args[0].querySelector("#"+args[0].id+"_create_icon");
		   imgElem.style.visibility = "hidden";
	    }
	}
}
function focusParentRow(image, event){
  if ((event.keyCode == 9) && event.shiftKey){
    var parentRow=dojo.NodeList(image).parents("tr").first();
	if (parentRow[0])
	  {
		parentRow[0].style.backgroundColor="rgba(224,224,224,0.5)";
		parentRow[0].focus();
      }
	}
}
function highlightParentRow(link,readOnlyEvidence){
	if (readOnlyEvidence=="false"){
	  var parentRow=dojo.NodeList(link).parents("tr").first();
	  if (parentRow[0])
	  {
		parentRow[0].style.backgroundColor="rgba(224,224,224,0.5)";
		if (readOnlyEvidence=="false"){
   		  var imgElem=parentRow[0].querySelector("#"+parentRow[0].id+"_create_icon");
		  imgElem.style.visibility = "visible";
		} 
	  }
	}
}

function dehighlightParentRow(link,readOnlyEvidence){
	var parentRow=dojo.NodeList(link).parents("tr").first();
	dehighlightRow(parentRow[0]);
}