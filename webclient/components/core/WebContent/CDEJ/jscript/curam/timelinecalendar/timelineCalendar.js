/*
 * Licensed Materials - Property of IBM
 * 
 * PID 5725-H26
 * 
 * Copyright IBM Corporation 2015, 2016. All rights reserved.
 * 
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([ "dojo/_base/declare", "dijit/_Widget", "dijit/_TemplatedMixin", "dijit/_WidgetsInTemplateMixin", "dojox/gfx", "dojo/dom-geometry", "dojo/request/xhr", "dojo/text!./templates/timelineCalendar.html", "dojo/dom-style", "dojo/date", "dojo/dom-class", "dojo/date/locale", "dijit/TooltipDialog", "dijit/popup", "dojo/on", "dojo/text!./templates/timelineCalendarTooltipDialog.html", "dojo/text!./templates/keyEventsTooltipDialog.html", "dojo/dom-attr", "curam/util/ResourceBundle", "dojo/_base/kernel", "dojo/_base/window", "dojo/domReady!" ], function(declare, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, gfx, domGeom, xhr, timelineCalendarTemplate, domStyle, date, domClass, locale, TooltipDialog, popup, on, timelineCalendarTooltipDialogTemplate, keyEventsTooltipDialogTemplate, domAttr, ResourceBundle, kernel, win) {
  return declare("timelineCalendar", [ _Widget, _TemplatedMixin, _WidgetsInTemplateMixin ], {

    servletPath : "../servlet/TimelineCalendarServlet",

    resourceBundleName : "timelineCalendar",

    // Right-To-Left
    RTL : false,

    calWidget : null,

    // Widget template
    templateString : timelineCalendarTemplate,

    // Surface used for drawing the graph
    graphSurface : null,

    month : 0,

    year : 0,

    currentYear : 0,

    firstDayOfYear : null,

    lastDayOfYear : null,

    data : null,

    config : null,

    error : null,

    bundle : null,

    keyPressEvent : null,

    monthAndYearTextList : [],
    monthAndYearTextPositionList : [],
    intervalTextShapeList : [],
    intervalTextShapePositionList : [],
    rowAndCalendarTitleShapeList : [],
    rowAndCalendarTitleShapePositionList : [],
    rowAndCalendarTitleTextList : [],
    intervalTextList : [],
    intervalBoxShapeList : [],
    highlightShapeList : [],
    keyEventsShapeList : [],
    keyEventsShapePositionList : [],

    // Object to hold the dimensions of the graph to be calculated & used
    // later
    graphDimensions : {
      height : 0,
      width : 0
    },

    graphViewPosition : {
      height : 0,
      width : 0
    },

    calendarDefaults : {
      borderStep : 1,
      barGap : 10,
      barHeight : 25,
      fontFamily : "MainMediumFontforIBM, Helvetica, Arial, sans-serif",
      fontSize : "14px",
      greyBorderWidth : 1,
      noMonthsColumns : 12,
      nameColumnWidth : 3,
      arrowPadding : 10,
      pixelsPerCharacter : 6.7,
      titlePadding : 10,
      arrowSize : 21,
      keyEventsSize: 5,
      yearFontSize : "14px",
      monthFontSize : "11px",
      titleFontSize : "14px"
    },

    colors : {
      // bar colors
      defaultIntervalColor : "#5596E6",
      
      highlightInner : "#EDF4FA",
      highlightOuter : "#DCECF4",

      // util colors
      monthColor : "#F9F9F9",
      borderColor : "#E3E3E3",
      arrowColor : "#203548",     
      leftPanelColor : "#F4F4F4",
      textColor: "#595959",
      rowTextColor: "white",
      keyEventsColor: "black",
      keyEventsHoverColor: "#25467a"
    },

    tooltipDialog : new TooltipDialog({
      templateString : timelineCalendarTooltipDialogTemplate,
      onClose : function() {
        popup.close(calWidget.tooltipDialog);
      }
    }),
    
    keyEventsDialog : new TooltipDialog({
      templateString : keyEventsTooltipDialogTemplate,
      onClose : function() {
        popup.close(calWidget.keyEventsDialog);
      }
    }),

    barCalculation : {
      height : 0,
      totalMonthsWidth : 0
    },

    coveragePeriodWidgetList : [],

    /* Last method in widget life cycle. All work should be done here */
    startup : function() {
      this.inherited(arguments);
      calWidget = this;
      calWidget.init();
    },

    /* Called by startup() */
    init : function() {

      // Work around to add focus() function to SVGElement
      if (!SVGElement.prototype.focus) {
        SVGElement.prototype.focus = HTMLElement.prototype.focus;
      }

      calWidget.checkRTL();
      calWidget.setDates();
      calWidget.loadBundle();
      calWidget.loadData();
      calWidget.setResize();
    },

    /* Checks if the widget is in Right-To-Left mode */
    checkRTL : function() {
      var body = win.body();
      if (domClass.contains(body, "rtl")) {
        calWidget.RTL = true;
      }
    },

    /* Sets the year, currentYear, month & the first & last days of the year */
    setDates : function() {
      var date = new Date();
      calWidget.month = date.getMonth();
      calWidget.year = calWidget.currentYear = date.getFullYear();
      calWidget.setFirstAndLastDayOfYear();
    },

    /* sets the first & last day of this year */
    setFirstAndLastDayOfYear : function() {
      calWidget.firstDayOfYear = new Date(calWidget.year, 0, 1);

      var monthIndex = calWidget.calendarDefaults.noMonthsColumns - 1;
      calWidget.lastDayOfYear = new Date(calWidget.year, monthIndex, 31);
    },

    /* Loads the ResourceBundle with translatable text */
    loadBundle : function() {
      calWidget.locale = kernel.locale;
      dojo.requireLocalization("curam.application", calWidget.resourceBundleName);
      calWidget.bundle = new ResourceBundle(calWidget.resourceBundleName, kernel.locale);
    },

    /*
     * XHR request to servletPath, passing in year, caseID & concernRoleID. The
     * returned data is handled as JSON. If the xhr is successful, the returned
     * JSON is check for an error object. If no error object is found the data &
     * config objects are set. The graph dimensions are calculated & then the
     * graph is rendered
     */
    loadData : function() {
      xhr(calWidget.servletPath, {
        handleAs : "json",
        query : {
          year : calWidget.year,
          caseID : calWidget.caseID,
          concernRoleID : calWidget.concernRoleID,
        }
      }).then(function(data) {

        if (data.error) {
          calWidget.error = data.error;
          calWidget.handleError();
        }
        else {
          calWidget.data = data.data;
          calWidget.config = data.config;
          if(data.keyEvents){
            calWidget.keyEvents = data.keyEvents.KeyEvents;
          }
          
          
          calWidget.setDimensionConfigurations();
          calWidget.calculateGraphDimensions();
          calWidget.render();
        }

      }, function(err) {
        console.error("An error occurred: " + err);
      });
    },

    /*
     * The error is placed into the error-messages-container & displayed as a
     * normal error would
     */
    handleError : function() {

      var errorStart = '<h3 class="message-list" id="error-messages-header">Message List</h3><ul id="error-messages" class="messages" tabindex="0" aria-live="assertive"><li class="level-1"><div><span>Error: </span>';
      var errorEnd = '</div></li></ul>'
      var errorContainer = document.getElementById("error-messages-container");
      errorContainer.innerHTML = errorStart + calWidget.error + errorEnd;
    },

    /*
     * The showRowTitle configuration is set
     */
    setDimensionConfigurations : function() {
      if (calWidget.config) {
        var config = calWidget.config;

        if (config.showRowTitle === false) {
          calWidget.calendarDefaults.nameColumnWidth = 0;
        }

        // SET REST OF CONFIGURATIONS HERE
      }
    },

    /*
     * The graph dimensions are calculated & set. The height of the div is set
     * before the width is calculated, so that scroll bars can be taken into
     * account.
     */
    calculateGraphDimensions : function() {

      calWidget.barCalculation.height = calWidget.calendarDefaults.barHeight + calWidget.calendarDefaults.barGap;

      calWidget.calculateGraphHeight();

      calWidget._graphView.set("style", "left: 0px; top: 0px; width: 100%; height: " + calWidget.graphDimensions.height + "px;");
      calWidget._curamBorderContainer.set("style", "left: 0px; top: 0px; width: 100%; height: " + calWidget.graphDimensions.height + "px");

      calWidget.calculateGraphWidth();

      calWidget.setWidthVariables();
    },

    /*
     * The width of each column is calculated
     */
    setWidthVariables : function() {

      // Number of columns to divide the calendar into. Default 12 months + 3
      // column wide for person name
      calWidget.calendarDefaults.noColumns = calWidget.calendarDefaults.noMonthsColumns + calWidget.calendarDefaults.nameColumnWidth;

      // A block width is the width of the graph/number of columns
      calWidget.calendarDefaults.blockWidth = calWidget.graphDimensions.width / calWidget.calendarDefaults.noColumns;
      calWidget.barCalculation.totalMonthsWidth = calWidget.calendarDefaults.blockWidth * calWidget.calendarDefaults.noMonthsColumns;

      // name field width is a block width * default 3
      calWidget.calendarDefaults.nameFieldWidth = calWidget.calendarDefaults.blockWidth * calWidget.calendarDefaults.nameColumnWidth;

    },

    /*
     * Graph height is calculated from the data. If a row, contains no
     * timelines, the height of their row is taken into account
     */
    calculateGraphHeight : function() {

      calWidget.rowArray = calWidget.createRowArray(calWidget.data);

      var numTimelineRows = 0;

      if (calWidget.rowArray) {
        for (var i = 0; i < calWidget.rowArray.length; i++) {
          calWidget.removeIneligibleIntervalsFromTimelines(calWidget.rowArray[i].timelines);
          var index = i;
          var numTimelines = calWidget.rowArray[i].timelines.length;
          numTimelineRows += numTimelines;
          if (numTimelines === 0) {
            numTimelineRows += 1;
          }
        }
      }

      var graphHeight = (numTimelineRows + 2) * (calWidget.barCalculation.height);
      calWidget.graphDimensions.height = graphHeight;
    },

    /*
     * Timelines that contains no eligible intervals are removed from the list
     */
    removeIneligibleIntervalsFromTimelines : function(timelines) {

      var i = timelines.length;
      while (i--) {

        calWidget.removeIneligibleIntervals(timelines[i].timeline);
        if (timelines[i].timeline.boundedIntervals.length == 0) {

          timelines.splice(i, 1);
        }
      }
    },

    /*
     * Intervals that are not eligible this year or contain a null value are
     * removed from the timeline
     */
    removeIneligibleIntervals : function(timeline) {

      var i = timeline.boundedIntervals.length;
      while (i--) {

        var dateFrom = undefined;
        var dateTo = undefined;

        var interval = timeline.boundedIntervals[i];

        if (interval.startDate) {
          dateFrom = new Date(interval.startDate.value);
        }

        if (interval.endDate) {
          dateTo = new Date(interval.endDate.value);
        }

        if (!calWidget.isIntervalEligibleThisYear(dateFrom, dateTo) || !interval.value) {
          timeline.boundedIntervals.splice(i, 1);
        }
      }
    },

    /*
     * The width of the graph is set
     */
    calculateGraphWidth : function() {
      calWidget.graphDimensions.width = domGeom.position(calWidget._graphView.domNode).w;
      calWidget.originalRenderedWidth = calWidget.graphDimensions.width;
      calWidget.graphViewPosition.width = calWidget.graphDimensions.width;
    },

    /*
     * creates a row array from the data recieved in the XHR request
     */
    createRowArray : function() {
      var rowArray = calWidget.data.rows;
      return rowArray;
    },

    /*
     * Creates a surface to draw the graph onto & begins drawing. This method
     * sets the width & height to 100% at the end to remove all scroll bars
     */
    render : function() {

      calWidget.createSurface();
      calWidget.renderGraph();
    },

    /*
     * Creates a surface using Dojo's GFX. SVG is assumed, so the viewBox &
     * perserveAspectRatio are set so that scaling with size changes happen
     */
    createSurface : function() {
      // Create the graph surface
      calWidget.graphSurface = gfx.createSurface(calWidget._graphView.domNode, calWidget.graphDimensions.width, calWidget.graphDimensions.height);

      if (calWidget.RTL) {
        // calWidget.graphSurface.setTextDir("rtl");
      }

      domAttr.set(calWidget.graphSurface.rawNode, "viewBox", "0 0 " + calWidget.graphDimensions.width + " " + calWidget.graphDimensions.height);
      domAttr.set(calWidget.graphSurface.rawNode, "preserveAspectRatio", "none");
      calWidget.graphSurface.rawNode.setAttribute("role", "application");

      // Add a title element which will be read out by screen readers
      var title = document.createElementNS("http://www.w3.org/2000/svg", "title");
      var titleText = calWidget.bundle.getProperty("Calendar.title.description", [ calWidget.year ]);
      title.textContent = titleText;
      title.id = "timelineCalendarTitle";
      calWidget.graphSurface.rawNode.appendChild(title);
      calWidget.graphSurface.rawNode.setAttribute("aria-labelledby", "timelineCalendarTitle");
    },

    /*
     * Renders everything to the surface
     */
    renderGraph : function() {

      if (calWidget.config.showRowTitle !== false) {
        calWidget.drawLeftSide();
        calWidget.drawYearHeader();
        calWidget.drawArrows();
        calWidget.drawMonthColorBars();
        calWidget.drawHeaderText();
        calWidget.drawBorder();
        calWidget.drawMonthLabels();
        calWidget.drawKeyEvents();
      }
      else {
        calWidget.drawYearHeader();
        calWidget.drawArrows();
        calWidget.drawMonthColorBars();
        calWidget.drawBorder();
        calWidget.drawMonthLabels();
        calWidget.drawKeyEvents();
      }

      calWidget.drawRows();
    },

    /*
     * Draws the left & right arrows and touch areas onto the surface
     */
    drawArrows : function() {
        
     if(calWidget.RTL){
        var leftArrowX = calWidget.calendarDefaults.arrowPadding;
        var rightArrowX = calWidget.barCalculation.totalMonthsWidth - (calWidget.calendarDefaults.arrowPadding + calWidget.calendarDefaults.arrowSize);
        var leftTouchAreaX = 0;
        var rightTouchAreaX = calWidget.barCalculation.totalMonthsWidth - 42;
      } else {
        var leftArrowX = calWidget.calendarDefaults.arrowPadding + calWidget.calendarDefaults.nameFieldWidth;
        var rightArrowX = calWidget.calendarDefaults.nameFieldWidth + calWidget.barCalculation.totalMonthsWidth - (calWidget.calendarDefaults.arrowPadding + calWidget.calendarDefaults.arrowSize);
        var leftTouchAreaX = calWidget.calendarDefaults.nameFieldWidth;
        var rightTouchAreaX = calWidget.calendarDefaults.nameFieldWidth + calWidget.barCalculation.totalMonthsWidth - 42;
      }

      // Draw the images
      var arrowY = (calWidget.calendarDefaults.barHeight + calWidget.calendarDefaults.barGap - calWidget.calendarDefaults.arrowSize) / 2;

      var leftImage = "../Images/timelinecalendar/arrow_scroll_left.svg";
      var rightImage = "../Images/timelinecalendar/arrow_scroll_right.svg";
      var leftHoverImage = "../Images/timelinecalendar/arrow_scroll_left_hover.svg";
      var rightHoverImage = "../Images/timelinecalendar/arrow_scroll_right_hover.svg";

      var leftArrow = calWidget.graphSurface.createImage({x: leftArrowX, y: arrowY, width: calWidget.calendarDefaults.arrowSize, height: calWidget.calendarDefaults.arrowSize, src: leftImage});
      var rightArrow = calWidget.graphSurface.createImage({x: rightArrowX, y: arrowY, width: calWidget.calendarDefaults.arrowSize, height: calWidget.calendarDefaults.arrowSize, src: rightImage});

      // Draw the touch areas
      var touchAreaY = 2;
      var leftTouchArea = calWidget.graphSurface.createRect({x: leftTouchAreaX, y: touchAreaY, width: 40, height: 30}).setStroke("transparent").setFill("transparent");
      var rightTouchArea = calWidget.graphSurface.createRect({x: rightTouchAreaX, y: touchAreaY, width: 40, height: 30}).setStroke("transparent").setFill("transparent");
      
      // Configure the arrows
      var leftArrowFunction = calWidget.RTL ? calWidget.incrementYear : calWidget.decrementYear;
      var rightArrowFunction = calWidget.RTL ? calWidget.decrementYear : calWidget.incrementYear;
      calWidget.configureArrow(leftArrow, leftTouchArea, leftImage, leftHoverImage, leftArrowFunction, calWidget.bundle.getProperty("Calendar.button.backyear"));
      calWidget.configureArrow(rightArrow, rightTouchArea, rightImage, rightHoverImage, rightArrowFunction, calWidget.bundle.getProperty("Calendar.button.forwardyear"));
      calWidget.leftTouchArea = leftTouchArea;
      calWidget.rightTouchArea = rightTouchArea;
      calWidget.leftArrow = leftArrow;
      calWidget.rightArrow = rightArrow;
    },

    /*
     * Sets an on click listener to change year, adds hover states, and adds required accessiblity classes to an arrow
     */
    configureArrow: function(arrow, touchArea, normalImage, hoverImage, clickFunction, description) {
      // Add onclick/keypress functions to touch area and arrow
      function addOnClick(theArea) {
        on(theArea, "click, keydown", function(e) {
          if (e.type === "keydown" && (e.keyCode === 32 || e.keyCode === 13)) {
            clickFunction();
          }
          else if (e.type === "click") {
            clickFunction();
          }
        });
      };
      [arrow, touchArea].forEach(addOnClick);

      // Add hover/focus state - the arrow image changes when either touch area or arrow are hovered/focused
      function addHoverState(theArea) {
        on(theArea, "mouseover, focus", function(e) {
          arrow.rawNode.href.baseVal = hoverImage;
        });
        on(theArea, "mouseout, blur", function(e) {
          arrow.rawNode.href.baseVal = normalImage;
        });
      };
      [arrow, touchArea].forEach(addHoverState);

      // Add accessibility attributes to touch area
      touchArea.rawNode.setAttribute("class", "timelineCalendarClickable");
      touchArea.rawNode.tabIndex = "0";
      touchArea.rawNode.setAttribute("aria-label", description);
      touchArea.rawNode.setAttribute("role", "button");
      touchArea.rawNode.setAttribute("focusable", "true");
      touchArea.rawNode.setAttribute("title", description);

      // Make arrow not tabbable
      arrow.rawNode.tabIndex = "-1";
      arrow.rawNode.setAttribute("focusable", "false");
      arrow.rawNode.setAttribute("title", description);
      
      //hover title
      var title = document.createElementNS("http://www.w3.org/2000/svg", "title");
      title.textContent = description;
      title.setAttribute("role", "presentation");
      title.setAttribute("aria-hidden", "true");
      touchArea.rawNode.appendChild(title);
    },

    /*
     * Draw the year at the top of the graph
     */
    drawYearHeader : function() {
      var year = calWidget.year;

      if (calWidget.RTL) {
        var offsetX = 0;
      }
      else {
        var offsetX = calWidget.calendarDefaults.nameFieldWidth;
      }

      var offsetY = calWidget.calendarDefaults.barGap / 2;
      var width = calWidget.barCalculation.totalMonthsWidth;

      var text = calWidget.graphSurface.createText({
        x : offsetX + (width / 2),
        y : offsetY + (calWidget.calendarDefaults.barHeight / 1.3),
        text : year,
        align : "middle"
      }).setFont({
        family : calWidget.calendarDefaults.fontFamily,
        size : calWidget.calendarDefaults.yearFontSize,
        weight: "normal"
      }).setFill(calWidget.colors.textColor);

      text.rawNode.setAttribute("tabindex", "0");
      text.rawNode.setAttribute("focusable", "true");
      var description = calWidget.bundle.getProperty("Calendar.title.description", [ year ]);
      text.rawNode.setAttribute("aria-label", description);

      // Add shape & its position to the lists
      calWidget.monthAndYearTextList.push(text);
      calWidget.monthAndYearTextPositionList.push(offsetX + (width / 2));

      // Draw grey border around the whole thing
      var rect = calWidget.graphSurface.createRect({
        x : offsetX - calWidget.calendarDefaults.greyBorderWidth,
        y : 0,
        height : calWidget.barCalculation.height,
        width : width + calWidget.calendarDefaults.greyBorderWidth
      }).setStroke({
        color : calWidget.colors.borderColor,
        width : calWidget.calendarDefaults.greyBorderWidth
      });

      rect.rawNode.setAttribute("class", "line");
    },

    /*
     * Colors in the title side of the graph
     */
    drawLeftSide : function() {

      if (calWidget.RTL) {
        var offsetX = calWidget.barCalculation.totalMonthsWidth;
      }
      else {
        var offsetX = 0;
      }

      var rect = calWidget.graphSurface.createRect({
        x : offsetX,
        y : 0,
        width : calWidget.calendarDefaults.nameFieldWidth - 1,
        height : calWidget.graphDimensions.height
      }).setFill(calWidget.colors.leftPanelColor);
      rect.rawNode.setAttribute("class", "line");
    },

    /*
     * Draws a border around the entire graph
     */
    drawBorder : function() {
      var rect = calWidget.graphSurface.createRect({
        x : 0,
        y : 0,
        width : calWidget.graphDimensions.width,
        height : calWidget.graphDimensions.height
      }).setStroke({
        color : calWidget.colors.borderColor,
        width : calWidget.calendarDefaults.greyBorderWidth
      });

      rect.rawNode.setAttribute("class", "line");
    },

    /*
     * Draws the calendar title
     */
    drawHeaderText : function() {
      var text = calWidget.bundle.getProperty("Calendar.title");

      if (calWidget.RTL) {
        var offsetX = calWidget.barCalculation.totalMonthsWidth + calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.titlePadding;
      }
      else {
        var offsetX = calWidget.calendarDefaults.titlePadding;
      }

      var offsetY = calWidget.calendarDefaults.barHeight + calWidget.calendarDefaults.barGap;
      var textShape = calWidget.graphSurface.createText({
        x : offsetX,
        y : offsetY,
        text : text,
        align : "left"
      }).setFont({
        family : calWidget.calendarDefaults.fontFamily,
        size : calWidget.calendarDefaults.titleFontSize,
        weight: "normal"
      }).setFill(calWidget.colors.textColor);

      calWidget.adjustTextLength(text, "...", textShape, calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.titlePadding);

      textShape.rawNode.setAttribute("role", "presentation");
      textShape.rawNode.setAttribute("aria-hidden", "true");

      
      // Add hover title to name
      // title created using SVG namespace
      var title = document.createElementNS("http://www.w3.org/2000/svg", "title");
      title.textContent = text;
      title.setAttribute("role", "presentation");
      title.setAttribute("aria-hidden", "true");
      textShape.rawNode.appendChild(title);

      calWidget.rowAndCalendarTitleShapeList.push(textShape);
      calWidget.rowAndCalendarTitleShapePositionList.push(offsetX);
      calWidget.rowAndCalendarTitleTextList.push(text);
    },

    /*
     * Colors in every 2nd month
     */
    drawMonthColorBars : function() {
      var offsetY = calWidget.barCalculation.height + calWidget.calendarDefaults.greyBorderWidth / 2;

      if (calWidget.RTL) {
        var offsetNameFieldwidth = 0;
        var drawingCondition = 0;
      }
      else {
        var offsetNameFieldwidth = calWidget.calendarDefaults.nameFieldWidth;
        var drawingCondition = 1;
      }

      for (var i = 0; i < calWidget.calendarDefaults.noMonthsColumns; i++) {
        var offsetX = offsetNameFieldwidth + (i * calWidget.calendarDefaults.blockWidth);

        // Draw light grey backgrounds for every second month
        if (i % 2 == drawingCondition) {
          var rect = calWidget.graphSurface.createRect({
            x : offsetX,
            y : offsetY,
            width : calWidget.calendarDefaults.blockWidth,
            height : calWidget.graphDimensions.height - (calWidget.barCalculation.height + calWidget.calendarDefaults.greyBorderWidth)
          }).setFill(calWidget.colors.monthColor);

          rect.rawNode.setAttribute("class", "month");
        }
      }
    },

    /*
     * Draws the month labels near the top of the widget.
     */
    drawMonthLabels : function() {
      var months = LOCALIZED_SHORT_MONTH_NAMES;

      if (calWidget.RTL) {
        var offsetXNameFieldOnLeft = 0;
      }
      else {
        var offsetXNameFieldOnLeft = calWidget.calendarDefaults.nameFieldWidth;
      }

      var offsetX = offsetXNameFieldOnLeft;

      var offsetY = calWidget.barCalculation.height;

      // Draw grey border around the whole thing
      var rect = calWidget.graphSurface.createRect({
        x : offsetX - calWidget.calendarDefaults.greyBorderWidth,
        y : offsetY,
        height : calWidget.barCalculation.height,
        width : calWidget.barCalculation.totalMonthsWidth + calWidget.calendarDefaults.greyBorderWidth
      }).setStroke({
        color : calWidget.colors.borderColor,
        width : calWidget.calendarDefaults.greyBorderWidth
      });

      rect.rawNode.setAttribute("class", "line");

      if (calWidget.year === calWidget.currentYear) {
        calWidget.drawHighlightCurrentMonth();
      }

      for (var i = 0; i < calWidget.calendarDefaults.noMonthsColumns; i++) {
        var offsetX = offsetXNameFieldOnLeft + (i * calWidget.calendarDefaults.blockWidth);

        if (calWidget.RTL) {
          var monthIndex = calWidget.calendarDefaults.noMonthsColumns - 1 - i;
        }
        else {
          var monthIndex = i;
        }

        // Draw month text label
        var text = calWidget.graphSurface.createText({
          x : offsetX + (calWidget.calendarDefaults.blockWidth / 2),
          y : offsetY + (calWidget.calendarDefaults.barHeight / 1.3),
          text : months[monthIndex],
          align : "middle"
        }).setFont({
          family : calWidget.calendarDefaults.fontFamily,
          size : calWidget.calendarDefaults.monthFontSize,
          weight: "normal"
        }).setFill(calWidget.colors.textColor);
        calWidget.monthAndYearTextList.push(text);
        calWidget.monthAndYearTextPositionList.push(offsetX + (calWidget.calendarDefaults.blockWidth / 2));

        text.rawNode.setAttribute("aria-hidden", "true");
      }
    },
    
    drawKeyEvents : function(){
      
      if (calWidget.RTL) {
        var offsetXNameFieldOnLeft = 0;
      }
      else {
        var offsetXNameFieldOnLeft = calWidget.calendarDefaults.nameFieldWidth;
      }
      
      var offsetX = offsetXNameFieldOnLeft;
      var offsetY = calWidget.barCalculation.height;
      
      for (var i = 0; i < calWidget.calendarDefaults.noMonthsColumns; i++) {
        
        if(calWidget.keyEvents && calWidget.keyEvents[i]){
          var offsetX = offsetXNameFieldOnLeft + (i * calWidget.calendarDefaults.blockWidth) + calWidget.calendarDefaults.blockWidth/2;
  
          if (calWidget.RTL) {
            var monthIndex = calWidget.calendarDefaults.noMonthsColumns - 1 - i;
          }
          else {
            var monthIndex = i;
          }
          
          var linkX = offsetX;
          var linkY = offsetY + 10;
          
          // Draw key events circle
          var circle = calWidget.graphSurface.createCircle({
              cx : linkX,
              cy : offsetY + (calWidget.calendarDefaults.barHeight / 0.9),
              r : calWidget.calendarDefaults.keyEventsSize,
            }).setFill(calWidget.colors.keyEventsColor);
          
          calWidget.keyEventsShapeList.push(circle);
          calWidget.keyEventsShapePositionList.push(linkX)
          
          var hitbox = calWidget.graphSurface.createRect({x: linkX - 10, y: linkY, width: 20, height: 20}).setStroke("transparent").setFill("transparent");
          
          calWidget.addKeyEventsHoverState(hitbox, circle);
          
          calWidget.keyEventsShapeList.push(hitbox);
          calWidget.keyEventsShapePositionList.push(linkX);
          
          hitbox.rawNode.setAttribute("class", "timelineCalendarClickable");
          hitbox.rawNode.setAttribute("tabindex", "0");
          hitbox.rawNode.setAttribute("focusable", "true");
          hitbox.rawNode.setAttribute("role", "button");
          
          var keyEventButtonAriaLabel = calWidget.bundle.getProperty("Calendar.keyEvents.aria", [LOCALIZED_MONTH_NAMES[i]]);
          hitbox.rawNode.setAttribute("aria-label", keyEventButtonAriaLabel);
          
          
          var title = document.createElementNS("http://www.w3.org/2000/svg", "title");
          title.textContent = keyEventButtonAriaLabel;
          title.setAttribute("role", "presentation");
          title.setAttribute("aria-hidden", "true");
          hitbox.rawNode.appendChild(title);
          
          calWidget.addKeyEventsListener(hitbox, i);
        }
      }
    },
    
    addKeyEventsHoverState : function(hitbox, shape){
         // Add hover/focus state - the arrow image changes when either touch area or arrow are hovered/focused
        on(hitbox, "mouseover, focus", function(e) {
            shape.setFill(calWidget.colors.keyEventsHoverColor);
        });
        on(hitbox, "mouseout, blur", function(e) {
            shape.setFill(calWidget.colors.keyEventsColor);
        });
    },
    
    addKeyEventsListener : function(circle, index){
      on(circle, 'click', function(e) {
        calWidget.openKeyEventsOverlay(circle, calWidget.keyEvents[index]);
      });

      on(circle, 'keydown', function(e) {
        if (e.keyCode == 13 || e.keyCode == 32) {
          calWidget.openKeyEventsOverlay(circle, calWidget.keyEvents[index]);
        }
      });
    },
    
    openKeyEventsOverlay : function(shape, keyEvents) {
      shape.rawNode.setAttribute("aria-expanded", "true");
      
      var keyEventsTitle = calWidget.bundle.getProperty("Calendar.keyEvents.title");
      var closeButtonText = calWidget.bundle.getProperty("Calendar.button.close");
      
      calWidget.keyEventsDialog.title.innerText =  keyEventsTitle;
      calWidget.keyEventsDialog.title.setAttribute("title", keyEventsTitle);
      calWidget.keyEventsDialog.close.setAttribute("title", closeButtonText);
      
      //Clear the key events
      while (calWidget.keyEventsDialog.keyEventsList.hasChildNodes()) {
        calWidget.keyEventsDialog.keyEventsList.removeChild(calWidget.keyEventsDialog.keyEventsList.firstChild);
      }
      
      keyEvents.forEach(function(currentValue, index, array){
        var div = document.createElement('div');
        
        div.classList.add('timelineCalendarKeyEvent');
        div.setAttribute("role", "listitem");
        div.setAttribute("tabindex", "0");
        div.setAttribute("focusable", "true");
        div.setAttribute("title", currentValue);
        
        var text = document.createTextNode(currentValue);
        div.appendChild(text);
        calWidget.keyEventsDialog.keyEventsList.appendChild(div);
      });
      
      popup.open({
        parent : calWidget,
        popup : calWidget.keyEventsDialog,
        around : shape.rawNode,
        orient : [ "below-centered", "above-centered" ],
        onCancel : function() {
          popup.close(calWidget.keyEventsDialog);
          shape.rawNode.setAttribute("aria-expanded", "false");
          shape.rawNode.focus();
        }
      });

      calWidget.keyEventsDialog.close.focus();
    },

    /*
     * highlights the current month, which includes coloring the month title &
     * the space below it
     */
    drawHighlightCurrentMonth : function() {

      if (calWidget.RTL) {
        var offsetXNameFieldOnLeft = 0;
        var month = 11 - calWidget.month;
      }
      else {
        var offsetXNameFieldOnLeft = calWidget.calendarDefaults.nameFieldWidth;
        var month = calWidget.month;
      }

      for (var i = 0; i < calWidget.calendarDefaults.noMonthsColumns; i++) {

        if (i === month) {
          var offsetY = calWidget.barCalculation.height;
          var offsetX = offsetXNameFieldOnLeft + (i * calWidget.calendarDefaults.blockWidth);

          var rect = calWidget.graphSurface.createRect({
            x : offsetX,
            y : offsetY,
            width : calWidget.calendarDefaults.blockWidth,
            height : calWidget.calendarDefaults.barHeight + calWidget.calendarDefaults.barGap
          }).setFill(calWidget.colors.highlightInner).setStroke({
            color : calWidget.colors.highlightOuter,
            width : calWidget.calendarDefaults.greyBorderWidth
          });

          calWidget.highlightShapeList.push(rect);

          rect.rawNode.setAttribute("class", "highlight-header");

          var rect2 = calWidget.graphSurface.createRect({
            x : offsetX,
            y : offsetY + calWidget.calendarDefaults.barHeight + calWidget.calendarDefaults.barGap + calWidget.calendarDefaults.greyBorderWidth + 1,
            width : calWidget.calendarDefaults.blockWidth,
            height : calWidget.graphDimensions.height - (calWidget.calendarDefaults.barHeight * 2 + calWidget.calendarDefaults.barGap * 2 + calWidget.calendarDefaults.greyBorderWidth * 2)
          }).setStroke({
            color : calWidget.colors.highlightOuter,
            width : calWidget.calendarDefaults.greyBorderWidth
          }).setFill(calWidget.colors.highlightInner);

          calWidget.highlightShapeList.push(rect2);

          rect2.rawNode.setAttribute("class", "highlight");
        }
      }
    },

    /*
     * Draws all timelines
     */
    drawTimelines : function(rowArray) {
      var index = 2;

      for (var i = 0; i < rowArray.length; i++) {
        var row = rowArray[i];

        for (var j = 0; j < row.timelines.length; j++) {
          var timeline = row.timelines[j];
          calWidget.drawTimeline(timeline.timeline, index);
          index++;
        }

        if (row.timelines.length === 0) {
          index++;
        }
      }
    },

    /*
     * Recursively adjusts the length of the text so that it fits within the
     * given rectWidth. The elipses are added if the text is shortened at all
     */
    adjustTextLength : function(text, ellipses, shape, rectWidth) {

      var bbox = shape.getBoundingBox();
      var width = bbox.width;
      var ellipsesLength = ellipses.length;
      var textLength = text.length;

      if (width > rectWidth) {

        if (textLength > 0) {
          var textLength = textLength - 1;
          text = text.substring(0, textLength);
        }

        if (textLength === 0 && ellipsesLength > 0) {
          var ellipsesLength = ellipsesLength - 1;
          ellipses = ellipses.substring(0, ellipsesLength);

        }
        shape.setShape({
          text : text.trim() + ellipses
        });

        if (ellipsesLength > 0) {
          calWidget.adjustTextLength(text, ellipses, shape, rectWidth);
        }
      }
    },

    /*
     * Adjusts the start date to withing the calendar year. Returns the first
     * day of the year, if the start date isn't set or is before the start of
     * the year
     */
    adjustStartDateToCalendarYear : function(startDate) {
      if (!startDate || startDate < calWidget.firstDayOfYear) {
        return calWidget.firstDayOfYear;
      }
      return startDate;
    },

    /*
     * Adjusts the end date to withing the calendar year. Returns the last day
     * of the year, if the start date isn't set or is after the last day of the
     * year
     */
    adjustEndDateToCalendarYear : function(endDate) {
      if (!endDate || endDate > calWidget.lastDayOfYear) {
        return calWidget.lastDayOfYear;
      }
      return endDate;
    },

    /*
     * Returns the given outer color for that ID if it is set in the
     * configuration interval colors map Otherwise, returns the default 
     * color
     */
    getIntervalColor : function(id) {
      var intervalConfigColors = calWidget.config.intervalColors[id];

      var outerColor = calWidget.colors.defaultIntervalColor;

      if (intervalConfigColors) {
        if (intervalConfigColors.outerColor) {
          outerColor = intervalConfigColors.outerColor;
        }
      }
      return outerColor;
    },

    /*
     * Adds the tooltip to the given shape using an on click handler. Popup is
     * positioned automatically.
     */
    addTooltipDialog : function(shape, startDate, endDate, title, subTitle, additionalInfo) {
      var formattedFromDate = '';
      if (startDate) {
        // this returns a string formated in the given pattern
        formattedFromDate = locale.format(startDate, {
          datePattern : jsDF,
          selector : 'date'
        });
      }

      var formattedToDate = '';
      if (endDate) {
        // this returns a string formated in the given pattern
        var formattedToDate = locale.format(endDate, {
          datePattern : jsDF,
          selector : 'date'
        });
      }

      var toolTipDateString = calWidget.bundle.getProperty("Calendar.Popup.Dates", [ formattedFromDate, formattedToDate ]); 
      
      on(shape, "click, keydown", function(e) {
        if (e.type === "keydown" && (e.keyCode === 32 || e.keyCode === 13)) {
          calWidget.openOverlay(shape, title, subTitle, toolTipDateString, additionalInfo);
        }
        else if (e.type === "click") {
          calWidget.openOverlay(shape, title, subTitle, toolTipDateString, additionalInfo);
        }
      });
    },

    openOverlay : function(shape, title, subTitle, toolTipDateString, additionalInfo) {
      shape.rawNode.setAttribute("aria-expanded", "true");
      
      var closeButtonAria = calWidget.bundle.getProperty("Calendar.button.close");
      var popupAria = calWidget.bundle.getProperty("Calendar.Popup.aria");
      
      calWidget.tooltipDialog.timelineCalendarTooltipPopup.setAttribute("aria-label", popupAria);
      
      calWidget.tooltipDialog.close.setAttribute("aria-label", closeButtonAria);
      calWidget.tooltipDialog.close.setAttribute("title", closeButtonAria);

      calWidget.tooltipDialog.title.innerText = title;
      calWidget.tooltipDialog.title.setAttribute("title", title);
      
      if(subTitle){
        calWidget.tooltipDialog.subTitle.innerText = subTitle;
        calWidget.tooltipDialog.subTitle.setAttribute("title", subTitle);
        calWidget.tooltipDialog.subTitle.setAttribute("tabindex", "0");
      } else {
        calWidget.tooltipDialog.subTitle.innerText = "";
        calWidget.tooltipDialog.subTitle.setAttribute("title", "");
        calWidget.tooltipDialog.subTitle.setAttribute("tabindex", "-1");
      }
      
      calWidget.tooltipDialog.additionalInfo.innerHTML = '';
      domStyle.set(calWidget.tooltipDialog.additionalInfo, {"display":"none"}); 
      var table = calWidget.tooltipDialog.additionalInfo;

      if (calWidget.RTL) {
        calWidget.tooltipDialog.dates.innerText = toolTipDateString;
        calWidget.tooltipDialog.dates.setAttribute("title", toolTipDateString);
      }
      else {
        calWidget.tooltipDialog.dates.innerText = toolTipDateString;
        calWidget.tooltipDialog.dates.setAttribute("title", toolTipDateString);
      }

      for ( var key in additionalInfo) {
        if (!additionalInfo.hasOwnProperty(key)) {
          continue;
        }

        calWidget.tooltipDialog.additionalInfo.setAttribute("aria-label", popupAria);
        
        var row = table.insertRow(-1);
        row.setAttribute("role", "row");

        var th = document.createElement("th");
        th.classList.add('key');
        //th.setAttribute("aria-label", key);
        th.setAttribute("tabindex", "0");
        th.setAttribute("focusable", "true");
        th.setAttribute("role", "rowheader");
        th.setAttribute("title", key);
        th.innerText = key;
        row.appendChild(th);
        
        var value = additionalInfo[key];
        var tr = row.insertCell(-1);
        tr.classList.add('value');
        //tr.setAttribute("aria-label", additionalInfo[key]);
        tr.setAttribute("tabindex", "0");
        tr.setAttribute("focusable", "true");
        tr.setAttribute("title", value);
        tr.innerText = value;
      }

      if(table.rows.length > 0){
    	  domStyle.set(calWidget.tooltipDialog.additionalInfo, {"display":"block"});
      }

      popup.open({
    	parent : calWidget,
        popup : calWidget.tooltipDialog,
        around : shape.children[0].rawNode,
        orient : [ "below-centered", "above-centered" ],
        onCancel : function() {
          popup.close(calWidget.tooltipDialog);
          shape.rawNode.setAttribute("aria-expanded", "false");
          shape.rawNode.focus();
        }
      });
      calWidget.tooltipDialog.domNode.setAttribute("aria-labelledby", "timelineCalendarDialogTitle")

      calWidget.tooltipDialog.close.focus();
    },

    /*
     * Reduces the year, resets the first & last days fo the year & rerenders
     * the graph
     */
    decrementYear : function() {
      calWidget.year--;
      calWidget.setFirstAndLastDayOfYear();
      calWidget.fetchDataAndRender();
    },

    /*
     * Increases the year, resets the first & last days fo the year & rerenders
     * the graph
     */
    incrementYear : function() {
      calWidget.year++;
      calWidget.setFirstAndLastDayOfYear();
      calWidget.fetchDataAndRender();
    },

    /*
     * Similar to loadData(), however this distroys the current graph & closes
     * the pop up before rerendering the graph
     */
    fetchDataAndRender : function() {

      xhr(calWidget.servletPath, {
        handleAs : "json",
        query : {
          year : calWidget.year,
          caseID : calWidget.caseID,
          concernRoleID : calWidget.concernRoleID,
        }
      }).then(function(data) {

        calWidget.destroyWidget();
        popup.close(calWidget.tooltipDialog);
        popup.close(calWidget.keyEventsDialog);

        if (data.error) {
          calWidget.error = data.error;
          calWidget.handleError();
        }
        else {
          calWidget.data = data.data;
          calWidget.config = data.config;
          if(data.keyEvents){
            calWidget.keyEvents = data.keyEvents.KeyEvents;
          }
          calWidget.setDimensionConfigurations();
          calWidget.calculateGraphDimensions();
          calWidget.render();
        }

      }, function(err) {
        console.error("An error occurred: " + err);
      });
    },

    /*
     * Destroys the surface & sets all shape & text arrays to 0
     */
    destroyWidget : function() {

      calWidget.graphSurface.destroy();

      calWidget.intervalTextShapePositionList.length = 0;
      calWidget.intervalTextShapeList.length = 0;
      calWidget.intervalTextList.length = 0;
      calWidget.intervalBoxShapeList.length = 0;

      calWidget.monthAndYearTextPositionList.length = 0;
      calWidget.monthAndYearTextList.length = 0;

      calWidget.rowAndCalendarTitleTextList.length = 0;
      calWidget.rowAndCalendarTitleShapePositionList.length = 0;
      calWidget.rowAndCalendarTitleShapeList.length = 0;

      calWidget.highlightShapeList.length = 0;
      
      calWidget.keyEventsShapeList.length = 0;
      calWidget.keyEventsShapePositionList.length = 0;
    },

    /*
     * Sets the resize listener for the widget. Checks to see if the width has
     * changed, before rerendering
     */
    setResize : function() {
      calWidget._curamBorderContainer.resize = function() {

        domStyle.set(calWidget._graphView, {
          width : "100%"
        });

        // if the width of the graph has changed, rerender
        calWidget.graphViewPosition.width = domGeom.position(calWidget._graphView.domNode).w;
        if (calWidget.graphViewPosition.width !== calWidget.graphDimensions.width) {

          // calWidget.resizeIE8();
          calWidget.resize();
        }
      };
    },

    /*
     * Sets the dimensions of the graph & scales all the shapes that need
     * scaling by the calculated ratio. I.E. text & arrow shapes
     */
    resize : function() {

      calWidget.graphDimensions.width = calWidget.graphViewPosition.width;
      calWidget.graphSurface.setDimensions(calWidget.graphDimensions.width, calWidget.graphDimensions.height);

      // Scale all the text shapes
      var ratio = calWidget.originalRenderedWidth / calWidget.graphDimensions.width;
      calWidget.scaleIntervals(ratio);
      calWidget.scaleMonthsAndYear(ratio);
      calWidget.scaleLeftArrow(ratio);
      calWidget.scaleRightArrow(ratio);
      calWidget.scaleRowAndCalendarTitle(ratio);
      calWidget.scaleHighlights(ratio);
      calWidget.scaleKeyEvents(ratio);

      popup.close(calWidget.tooltipDialog);
      popup.close(calWidget.keyEventsDialog);
    },

    /*
     * Scales the intervals using the given ratio. The interval text is adjusted
     * to fit inside the shape
     */
    scaleIntervals : function(ratio) {

      var pxPerChar = calWidget.calendarDefaults.pixelsPerCharacter * ratio;

      for (var i = 0; i < calWidget.intervalTextShapeList.length; i++) {
        var oldPosition = calWidget.intervalTextShapePositionList[i];
        var difference = (oldPosition * ratio) - oldPosition;
        calWidget.intervalTextShapeList[i].setTransform({
          xx : ratio,
          xy : 0,
          dx : -difference,
          yx : 0,
          yy : 1,
          dy : 0
        });

        var text = calWidget.intervalTextList[i];

        var rectangle = calWidget.intervalBoxShapeList[i];
        var rectangleWidth = rectangle.getBoundingBox().width / ratio;

        calWidget.intervalTextShapeList[i].setShape({
          text : text
        });
        calWidget.adjustTextLength(text, "...", calWidget.intervalTextShapeList[i], rectangleWidth);
      }
    },

    /*
     * Scales the months & year text using the given ratio.
     */
    scaleMonthsAndYear : function(ratio) {
      for (var i = 0; i < calWidget.monthAndYearTextList.length; i++) {
        var oldPosition = calWidget.monthAndYearTextPositionList[i];
        var difference = (oldPosition * ratio) - oldPosition;
        calWidget.monthAndYearTextList[i].setTransform({
          xx : ratio,
          xy : 0,
          dx : -difference,
          yx : 0,
          yy : 1,
          dy : 0
        });
      }
    },

    /*
     * Scales the rows & calendar title text using the given ratio. The text is
     * adjusted to fit inside its area
     */
    scaleRowAndCalendarTitle : function(ratio) {
      if (calWidget.RTL) {
        for (var i = 0; i < calWidget.rowAndCalendarTitleShapeList.length; i++) {
          var oldPosition = calWidget.rowAndCalendarTitleShapePositionList[i];
          var difference = (oldPosition * ratio) - oldPosition;
          calWidget.rowAndCalendarTitleShapeList[i].setTransform({
            xx : ratio,
            xy : 0,
            dx : -difference,
            yx : 0,
            yy : 1,
            dy : 0
          });
        }
      }
      else {
        for (var i = 0; i < calWidget.rowAndCalendarTitleShapeList.length; i++) {
          calWidget.rowAndCalendarTitleShapeList[i].setTransform({
            xx : ratio,
            xy : 0,
            dx : 0,
            yx : 0,
            yy : 1,
            dy : 0
          });

          var text = calWidget.rowAndCalendarTitleTextList[i];
          calWidget.rowAndCalendarTitleShapeList[i].setShape({
            text : text
          });
          calWidget.rowAndCalendarTitleShapeList[i].setStroke(null);
          calWidget.adjustTextLength(text, "...", calWidget.rowAndCalendarTitleShapeList[i], (calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.titlePadding) / ratio);
        }
      }
    },

    /*
     * Scales the left arrow using the given ratio.
     */
    scaleLeftArrow : function(ratio) {
      if (calWidget.RTL) {
        var nameFieldOnLeftWidth = 0;
      }
      else {
        var nameFieldOnLeftWidth = calWidget.calendarDefaults.nameFieldWidth;
      }

      var difference = (nameFieldOnLeftWidth * ratio) - nameFieldOnLeftWidth;
      calWidget.leftArrow.setTransform({
        xx : ratio,
        xy : 0,
        dx : -difference,
        yx : 0,
        yy : 1,
        dy : 0
      });
      calWidget.leftTouchArea.setTransform({
        xx : ratio,
        xy : 0,
        dx : -difference,
        yx : 0,
        yy : 1,
        dy : 0
      });
    },

    /*
     * Scales the right arrow using the given ratio.
     */
    scaleRightArrow : function(ratio) {
      if (calWidget.RTL) {
        var nameFieldOnLeftWidth = 0;
      }
      else {
        var nameFieldOnLeftWidth = calWidget.calendarDefaults.nameFieldWidth;
      }

      var totalWidth = nameFieldOnLeftWidth + calWidget.barCalculation.totalMonthsWidth;
      var difference = ((totalWidth * ratio) - totalWidth);
      calWidget.rightArrow.setTransform({
        xx : ratio,
        xy : 0,
        dx : -difference,
        yx : 0,
        yy : 1,
        dy : 0
      });
      calWidget.rightTouchArea.setTransform({
        xx : ratio,
        xy : 0,
        dx : -difference,
        yx : 0,
        yy : 1,
        dy : 0
      });
    },

    /*
     * Scales border around the highlight shapes
     */
    scaleHighlights : function(ratio) {
      for (var i = 0; i < calWidget.highlightShapeList.length; i++) {
        calWidget.highlightShapeList[i].setStroke({
          color : calWidget.colors.highlightOuter,
          width : ratio
        });
      }
    },
    
    /*
     * Scales key events shapes
     */
    scaleKeyEvents : function(ratio) {
      for (var i = 0; i < calWidget.keyEventsShapeList.length; i++) {
        var oldPosition = calWidget.keyEventsShapePositionList[i];
        var difference = (oldPosition * ratio) - oldPosition;
        calWidget.keyEventsShapeList[i].setTransform({
          xx : ratio,
          xy : 0,
          dx : -difference,
          yx : 0,
          yy : 1,
          dy : 0
        });
      }
    },

    /*
     * Destroys the graph & re-renders it
     */
    resizeIE8 : function() {

      calWidget.destroyWidget();
      popup.close(calWidget.tooltipDialog);
      popup.close(calWidget.keyEventsDialog);
      calWidget.render();
    },

    /*
     * Checks in the given start & end date overlap with the year
     */
    isIntervalEligibleThisYear : function(startDate, endDate) {
      if (!startDate) {
        startDate = calWidget.firstDayOfYear;
      }
      if (!endDate) {
        endDate = calWidget.lastDayOfYear
      }

      if ((startDate <= calWidget.lastDayOfYear) && (endDate >= calWidget.firstDayOfYear)) {
        return true;
      }
      return false;
    },

    drawRows : function(rows) {
      var rows = calWidget.rowArray
      var numRows = rows.length;
      var numRowsDrawn = 2;

      for (var i = 0; i < numRows; i++) {
        numRowsDrawn = calWidget.drawRow(rows[i], numRowsDrawn);
      }
    },

    drawRow : function(row, rowNumber) {

      if (calWidget.config.showRowTitle !== false) {
        calWidget.drawRowTitle(row, rowNumber);
      }

      var numTimelinesDrawn = calWidget.drawTimelines(row, rowNumber);
      return numTimelinesDrawn;
    },

    drawRowTitle : function(row, rowNumber) {

      var index = rowNumber;

      if (calWidget.RTL) {
        var offsetX = calWidget.barCalculation.totalMonthsWidth + calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.titlePadding;
        var offsetXRect = calWidget.barCalculation.totalMonthsWidth;
        var offsetXLine = 0;
      }
      else {
        var offsetX = calWidget.calendarDefaults.titlePadding;
        var offsetXRect = 0;
        var offsetXLine = calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.greyBorderWidth;
      }

      var numTimelines = row.timelines.length;
      if (numTimelines === 0) {
        numTimelines = 1;
      }

      var offsetY = (numTimelines * 5) + (calWidget.barCalculation.height) * index;

      offsetY = offsetY + ((numTimelines * calWidget.calendarDefaults.barHeight) / 1.3);

      // ...Create display text string
      var displayText = row.title;

      // ...Render text
      var text = calWidget.graphSurface.createText({
        x : offsetX,
        y : offsetY,
        text : displayText,
        align : "left"
      }).setFont({
        family : calWidget.calendarDefaults.fontFamily,
        size : calWidget.calendarDefaults.fontSize,
        weight: "normal"
      }).setFill(calWidget.colors.textColor);

      var group = calWidget.graphSurface.createGroup();
      group.add(text);

      var rowHeaderDescription = calWidget.bundle.getProperty("Calendar.row.title", [ displayText ]);
      group.rawNode.setAttribute("aria-label", rowHeaderDescription);
      group.rawNode.setAttribute("tabindex", "0");
      group.rawNode.setAttribute("focusable", "true");
      group.rawNode.setAttribute("role", "heading");

      // Add hover title to name
      // title created using SVG namespace
      var title = document.createElementNS("http://www.w3.org/2000/svg", "title");
      title.textContent = displayText;
      title.setAttribute("role", "presentation");
      title.setAttribute("aria-hidden", "true");
      text.rawNode.appendChild(title);

      calWidget.rowAndCalendarTitleShapeList.push(text);
      calWidget.rowAndCalendarTitleShapePositionList.push(offsetX);

      calWidget.rowAndCalendarTitleTextList.push(displayText);

      calWidget.adjustTextLength(displayText, "...", text, calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.titlePadding);

      var height = ((calWidget.barCalculation.height) * numTimelines);

      // Draw grey border
      var offsetY = calWidget.barCalculation.height * index;
      var rect = calWidget.graphSurface.createRect({
        x : offsetXRect,
        y : offsetY,
        height : height,
        width : calWidget.calendarDefaults.nameFieldWidth - calWidget.calendarDefaults.greyBorderWidth
      }).setStroke({
        color : calWidget.colors.borderColor,
        width : calWidget.calendarDefaults.greyBorderWidth
      });

      rect.rawNode.setAttribute("class", "line");

      var line = calWidget.graphSurface.createLine({
        x1 : offsetXLine,
        y1 : offsetY + height,
        x2 : calWidget.calendarDefaults.nameFieldWidth + calWidget.calendarDefaults.noMonthsColumns * calWidget.calendarDefaults.blockWidth,
        y2 : offsetY + height
      }).setStroke({
        color : calWidget.colors.borderColor,
        width : calWidget.calendarDefaults.greyBorderWidth
      });

      line.rawNode.setAttribute("class", "line");

      // ...Increment index (used for offsetY) by numProducts
      index = index + numTimelines;

    },

    drawTimelines : function(row, rowNumber) {
      var index = rowNumber;

      for (var j = 0; j < row.timelines.length; j++) {
        var timeline = row.timelines[j];
        calWidget.drawTimeline(timeline.timeline, index);
        index++;
      }

      if (row.timelines.length === 0) {
        index++;
      }

      return index;
    },

    /*
     * Draws an individual timeline. Index is the position in height in which
     * the timeline should be drawn. Every bounded interval within the timeline
     * is checked to see if it is eligible ths year. An on click listener is set
     * on every interval drawn. This causes the pop up to open
     */
    drawTimeline : function(timeline, rowNumber) {

      for (var i = 0; i < timeline.boundedIntervals.length; i++) {

        var offsetX;
        var offsetY = (rowNumber * (calWidget.barCalculation.height)) + calWidget.calendarDefaults.barGap / 2;
        var width;
        var dateFrom = undefined;
        var dateTo = undefined;
        var monthTo;
        var monthFrom;
        var outerColor;
        var adjustedDateFrom;
        var adjustedDateTo;

        var interval = timeline.boundedIntervals[i];

        if (interval.startDate) {
            dateFrom = new Date(interval.startDate.value);
            
            // remove the timezone offset
            var timezoneOffSet = dateFrom.getTimezoneOffset()*60*1000;
            dateFrom = new Date(dateFrom.getTime() + timezoneOffSet);
          }

          if (interval.endDate) {
            dateTo = new Date(interval.endDate.value);
            
            // remove the timezone offset
            var timezoneOffSet = dateTo.getTimezoneOffset()*60*1000;
            dateTo = new Date(dateTo.getTime() + timezoneOffSet);
          }

        if (calWidget.isIntervalEligibleThisYear(dateFrom, dateTo) && interval.value) {

          var id = interval.value.id;
          var title = interval.value.title;
          var subTitle = interval.value.subTitle;
          var additionalInfo = interval.value.additionalInformation;

          adjustedDateFrom = calWidget.adjustStartDateToCalendarYear(dateFrom);
          adjustedDateTo = calWidget.adjustEndDateToCalendarYear(dateTo);

          monthFrom = adjustedDateFrom.getMonth();
          monthTo = adjustedDateTo.getMonth();

          var intervalColor = calWidget.getIntervalColor(id);

          if (calWidget.RTL) {
            var RTLmonthTo = 11 - monthTo;
            var daysInMonth = date.getDaysInMonth(adjustedDateTo);
            offsetX = (calWidget.calendarDefaults.blockWidth * (RTLmonthTo + ((daysInMonth - adjustedDateTo.getDate()) / (daysInMonth)))) + 1;
          }
          else {
            offsetX = calWidget.calendarDefaults.nameFieldWidth + (calWidget.calendarDefaults.blockWidth * (monthFrom + ((adjustedDateFrom.getDate() - 1) / (date.getDaysInMonth(adjustedDateFrom)))));
          }

          width = (monthTo - monthFrom - ((adjustedDateFrom.getDate() - 1) / (date.getDaysInMonth(adjustedDateFrom))) + ((adjustedDateTo.getDate()) / (date.getDaysInMonth(adjustedDateTo)))) * calWidget.calendarDefaults.blockWidth - 1;
          
          var rectangleOuter = calWidget.graphSurface.createRect({
            x : offsetX,
            y : offsetY,
            width : width,
            height : calWidget.calendarDefaults.barHeight,
            r: 5
          }).setFill(intervalColor);
          
          // Add hover title to rectangle
          // title created using SVG namespace
          var rectHoverTitle = document.createElementNS("http://www.w3.org/2000/svg", "title");
          rectHoverTitle.textContent = title;
          rectHoverTitle.setAttribute("role", "presentation");
          rectHoverTitle.setAttribute("aria-hidden", "true");
          rectangleOuter.rawNode.appendChild(rectHoverTitle);
          // Add class to outer rectangle
          rectangleOuter.rawNode.setAttribute("class", "timelineCalendarClickable line");

          var rectangleWidth = width - calWidget.calendarDefaults.borderStep * 4;

          calWidget.intervalBoxShapeList.push(rectangleOuter);

          var adjustedTitle = title;

          calWidget.intervalTextList.push(title);

          // Draw text
          var text = calWidget.graphSurface.createText({
            x : offsetX + (width / 2),
            y : offsetY + (calWidget.calendarDefaults.barHeight / 1.5),
            text : adjustedTitle,
            align : "middle"
          }).setFont({
            family : calWidget.calendarDefaults.fontFamily,
            size : calWidget.calendarDefaults.fontSize,
            weight : "bold"
          }).setFill(calWidget.colors.rowTextColor);

          // Add hover title to text
          // title created using SVG namespace
          var textHoverTitle = document.createElementNS("http://www.w3.org/2000/svg", "title");
          textHoverTitle.textContent = title;
          textHoverTitle.setAttribute("role", "presentation");
          textHoverTitle.setAttribute("aria-hidden", "true");
          text.rawNode.appendChild(textHoverTitle);

          calWidget.adjustTextLength(title, "...", text, width);

          // Add class to text
          text.rawNode.setAttribute("class", "timelineCalendarClickable");

          var group = calWidget.graphSurface.createGroup();
          group.add(rectangleOuter);
          group.add(text);

          calWidget.intervalTextShapeList.push(text);
          calWidget.intervalTextShapePositionList.push(offsetX + (width / 2));

          var formattedFromDate = locale.format(dateFrom, {
            datePattern : jsDF,
            selector : 'date'
          });
          var formattedToDate = locale.format(dateTo, {
            datePattern : jsDF,
            selector : 'date'
          });

          var intervalDescription = calWidget.bundle.getProperty("Calendar.interval.description", [ title, formattedFromDate, formattedToDate ]);

          // Set the tab index of the group so its focusable
          // group.rawNode.tabIndex = 0;
          group.rawNode.setAttribute("tabindex", "0");
          group.rawNode.setAttribute("role", "button");
          group.rawNode.setAttribute("aria-label", intervalDescription);
          group.rawNode.setAttribute("aria-expanded", "false");
          group.rawNode.setAttribute("aria-controls", "timelineCalendarTooltip");
          group.rawNode.setAttribute("focusable", "true");

          // Add tooltip to rectangle
          calWidget.addTooltipDialog(group, dateFrom, dateTo, title, subTitle, additionalInfo);
        }
      }
    }
  });
});