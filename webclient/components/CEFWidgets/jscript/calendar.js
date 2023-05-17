/*
 * Copyright 2010 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/* Modification History
 * ====================
 * 21 Oct 2010 PDN   [CR00223976]  Completed documentation and formatted code.
 */

/**
 * This file provides javascript functionality that is required by the curam calendar
 * widget.
 * 
 * It provides an initialize method which purpose is to initialize the dijit widgets so 
 * this display the correct initial data. This is needed when the widget is placed 
 * inside a dijit widgit (e.g Pod) which does not initialize its internal dijit widgets used
 * by the calendar. 
 * 
 * The two dijit widgets that are initialized are the stack container which holds the 
 * month html tables; The daily activity StackContainer which holds all the activities.
 *
 * The other function that is provided is the dayClick() which is called when a date
 * has been clicked on to select and displau the associated  acivity content pane.
 */



dojo.addOnLoad(initialize);

/*
 * Find a dojo Dijit by using a known unique class name.
 * The nodes are searched on the class name and return the first found instance
 * of a node with that class. The node id is then used to call dijit.byId.
 * This allows the developer to create a widget with a class instead of an id,
 * and then use the class like an id to return the widget. This prevents 
 * duplicate ids being used.
 *
 */

function findDijitByClass(/*String*/ className){

	var widgetNode = dojo.query('.' + className)[0];
	if(!widgetNode){
	 console.debug("Dijit with the class '" + className + "' could not be found.");
	 return undefined;
	}
	return dijit.byId(widgetNode.id);
}

var dayClick = function(activityID, node){

	// Get the activities pane container
	var activitiesPageContainer = findDijitByClass('activitiesPageContainer');
	activitiesPageContainer.selectChild( findDijitByClass(activityID) );
	
	// Set the selected day class
	var calendarTableNode = node.parentNode.parentNode;
	var existingSelectedDay = dojo.query('.selected-day',calendarTableNode);
	existingSelectedDay.removeClass("selected-day");
    dojo.addClass(node, "selected-day");
}


function initialize(){ 
	// Find the Month stack container
	var monthStackContainerWidget = findDijitByClass('monthPageContainer');
		
	monthStackContainerWidget.startup();

	// Find the current month content panel and show it
	var theMonthContentPanes = monthStackContainerWidget.getChildren();
	var currentMonthPane = dijit.byNode(dojo.query('.currentMonth')[0]);
	
	monthStackContainerWidget._showChild( currentMonthPane );
	
	// Find the activities stack container
  var activityStackContainerWidget = findDijitByClass('activitiesPageContainer');
	activityStackContainerWidget.startup();

	// Select the current date's pane
	var todaysDate = new Date();
	var date = todaysDate.getFullYear() + "-" + todaysDate.getMonth() + "-" + todaysDate.getDate();
	var selectedActivityPaneID;
	
	// Check if there is activity details for today
	if(findDijitByClass(date) == undefined){
		selectedActivityPaneID = findDijitByClass("empty-activity");
	}
	else {
		selectedActivityPaneID = findDijitByClass(date);
	}
	activityStackContainerWidget._showChild( selectedActivityPaneID );
} 
