/*
 *  Licensed Materials - Property of IBM
 *  
 *  PID 5725-H26
 *  
 *  Copyright IBM Corporation 2017,2021. All rights reserved.
 *  
 *  US Government Users Restricted Rights - Use, duplication or disclosure
 *  restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define(["dojo/_base/declare",
    "dijit/_Widget", 
    "dijit/_TemplatedMixin", 
        "dojo/text!./templates/BusinessObjectTimeline.html",
        "dojo/on",
        "curam/util/ResourceBundle",
        "dojo/domReady!"
        ], function(declare, 
              _Widget, 
              _TemplatedMixin, 
              template,
              on,
              ResourceBundle) {
  
  return declare('curam.aes.BusinessObjectTimeline', [ _Widget, _TemplatedMixin ], { 

  /**
   * The HTML template to use for this widget.
   */
  templateString: template,
  
  /**
   * dates setter.
   * Sets the dates attribute & updates the timeline.
   * @param dates Array of dates in milliseconds.
   */
  _setDatesAttr: function(dates) {
    this._set('dates', dates);
  },
  
  /**
   * datesAndActionInds setter
   * @param datesAndActionInds Array of Objects containing a Date & a boolean
   */
  _setDatesAndActionIndsAttr: function(datesAndActionInds) {
    this._set('datesAndActionInds', datesAndActionInds);
  },

  /**
   * ActiveDate setter
   * @param activeDateArray Active date
   */
  _setActiveDateAttr: function(activeDate) {
    this._set('activeDate', activeDate);
    this._calculateNumberPages();
    this._updateVisiblePageWithActiveDate();
    this._updateButtons();
    this._updateTimeline();
  },
  
  /**
   * VisiblePage setter
   * @param pageNumber Page number
   */
  _set_visiblePageAttr: function(pageNumber) {
    this._set('_visiblePage', pageNumber);
    this._updateButtons();
    this._updateTimeline();
  },
  
  /**
   * @constructor
   */
  constructor: function(args) {
    this.inherited(arguments);
  
    // Set up instance variables
    this.dates = [];
    this.datesAndActionInds = [];
    this.activeDate = null;
    this._points = [];
    this._pointEvents = [];
    this._maxPoints = 3;
    this._visiblePage = 1;
    this._maxPages = 1;
    this.type = args.type || "";
  },

  /**
   * Final method called in widget life cycle.
   */
  startup : function() {
    this.inherited(arguments);
    this._calculateNumberPages();
    this._updateTimeline();
    this._updateButtons();
  },
  
  postMixInProperties: function() {
     this.bundle = new ResourceBundle("IncomingEvidenceComparison");
	 this.leftNavDisplayText = "&#9664;";
	 this.rightNavDisplayText = "&#9654;";
	 
	 if (this.isLeftToRight()) {
	   this.rightButtonTitle = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Button.Next');
	   this.leftButtonTitle = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Button.Previous');
	 } else {
	   this.leftButtonTitle = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Button.Next');
	   this.rightButtonTitle = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Button.Previous');
	 }
   },
   
   /**
    * Calculates & sets the max number of pages available on the time line & the initial page to show.
    */
   _calculateNumberPages: function() {
     this._maxPages = Math.ceil(this.dates.length / this._maxPoints);
     this._visiblePage =  this._maxPages;
   },
   
  /**
   * Add points to the time line, if they are to appear on the currently visible page.
   */
   _addPoints: function() {
     if (this.activeDate) {
       var datesAndActionIndLength = this.datesAndActionInds.length;
       for (var i = 0; i < datesAndActionIndLength; i++) {
         if (this._isOnVisiblePage(datesAndActionIndLength, i, this._visiblePage)) {
           var position = this._getPointPosition(datesAndActionIndLength, i);
           var datesAndActionInd = this.datesAndActionInds[i];
           this._addPoint(datesAndActionInd, position);
         }
       }
     }
   },
  
  /**
   * Add a single point to the timeline
   * @param datesAndActionInd Array of Objects containing a Date & a boolean
   * @param positionPercentage Position percentage
   */
   _addPoint: function(datesAndActionInd, positionPercentage) {
	 var date = datesAndActionInd.date;
	 if (!this.isLeftToRight()) {
       positionPercentage = 100 - positionPercentage;
     }
    
     var point = document.createElement('li');
     point.classList.add('business-object-timeline__point');
	 point.style.left = positionPercentage + '%';
     var displayableData = this._getDisplayableData(date, datesAndActionInd, point);
     point.setAttribute('data-date', displayableData);
     point.setAttribute('tabindex', '0');
     point.title = displayableData;

     var div = document.createElement('div');
     div.classList.add('business-object-timeline__spanWrapper');
     
     var span = document.createElement('span');
     span.innerText=displayableData;
     span.classList.add('business-object-timeline__span');
     span.setAttribute('role', 'button');
     
     div.appendChild(span);
     point.appendChild(div);
     
     // Add chevron svg and aria-label to span
     if (date.getTime() === this.activeDate.getTime()) {
        point.classList.add('business-object-timeline__point-active');
        point.insertBefore(this._createChevronIcon(), div);
        span.setAttribute('aria-label',  this.bundle.getProperty('BusinessObjectTimeline.Navigation.Point.SelectedDate') + displayableData);
     }

     this.pointNode.appendChild(point);
     this._points.push(point);
     this._addPointEventListener(point, date);
   },
  
   /**
    * Return displayable date with preceding text if missing information on 
    * business object, or conflict exists.
    * @param dateToFormat date to format
    * @param datesAndActionInd Object containing missing data boolean indicator.
    * @param point svg circle node.
    */
    _getDisplayableData: function(dateToFormat, datesAndActionInd, point) {
	
      var date = curam.date.formatDate(dateToFormat, jsDF);

	  if (this.type === "Incoming" && datesAndActionInd.missingData) {
	    point.classList.add('business-object-timeline__point-missingData');
	    point.appendChild(this._createExclaimIcon());
	    
		var missingDataText = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Point.MissingData');
		var dateWithMissingData = '(' + missingDataText + ') ' + date;
		return dateWithMissingData;
		  
      } else if (datesAndActionInd.conflictInd) {
	    point.classList.add('business-object-timeline__point-conflict');
        point.appendChild(this._createExclaimIcon());
	    
        var conflictText = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Point.Conflict');
	    var dateWithConflictText = '(' + conflictText + ') ' + date;
		return dateWithConflictText;
		
	  } else {
		point.appendChild(this._createTimelinePointIcon());
	    return date;	
	  }
	},

  /**
   * Adds an click & enter/space key down event listeners to the point. 
   * The event emits a new event with the date as a detail.
   * @param point svg circle node
   * @param date Date object to add to the newly emitted event.
   */
  _addPointEventListener: function(point, date) {
    var _self = this;
    var eventToken = on(point, 'click, keydown', function(e) {
	  if (_self.activeDate.getTime() !== date.getTime()) {
	    if (e.type === 'keydown' && (e.keyCode === 32 || e.keyCode === 13) || e.type === 'click') {
	      _self.emit('date', {bubbles: true, detail: date});
	    }
	  }
    })
    this._pointEvents.push(eventToken);
  },
  
  /**
   * Re-renders the timeline.
   */
  _updateTimeline: function() {
    this._removePoints();
    this._addPoints();
  },
  
  /**
   * Removes all points from the timeline.
   */
  _removePoints: function(){
    var pointsLength = this._points.length;
    for(var i = 0; i < pointsLength; i++){
      var point = this._points[i];
      this.pointNode.removeChild(point);
    }
    this._points.length = 0;
    
    var pointEventsLength = this._pointEvents.length;
    for(var i = 0; i < pointEventsLength; i++){
      var pointEvent = this._pointEvents[i];
      pointEvent.remove();
    }
    this._pointEvents.length = 0;
  },

  /**
   * Updates visiblePage variable with active date.
   */
  _updateVisiblePageWithActiveDate: function(){
    if (this.activeDate) {
      var datesAndActionIndLength = this.datesAndActionInds.length;
      var dateIndexWithActiveDate = -1;
      for (var i=0; i< datesAndActionIndLength; i++) {
	   if (this.activeDate.getTime() === this.datesAndActionInds[i].date.getTime()) {
	     dateIndexWithActiveDate = i;
		 break;
	     }
       }
    
      if (dateIndexWithActiveDate >-1) {
        for (var k=1; k<=this._maxPages; k++) {
	      if (this._isOnVisiblePage(datesAndActionIndLength, dateIndexWithActiveDate, k)) {
	        //set the visible page on this timeline now to k
		    this.set('_visiblePage', k);
		    break;
	      } 
        }
      }
    }
  },
  
  /**
   * Called when left button is clicked.
   */
  _leftNavClick: function() {
    if (this.isLeftToRight()) {
	  this.set('_visiblePage', this._visiblePage - 1);
	} else {
	  this.set('_visiblePage', this._visiblePage + 1);
	}
   },
  
  /**
   * Called when right button is clicked.
   */
   _rightNavClick: function() {
    if (this.isLeftToRight()) {
      this.set('_visiblePage', this._visiblePage + 1);
	} else {
	  this.set('_visiblePage', this._visiblePage - 1);
	}
   },
   
   /**
    * Updates the disabled property of the next & previous buttons 
    * based on whether there are effective dates in the year previous/next.
    */
   _updateButtons: function() {
	 var nextDisabled = !this._isNextPageNavigatable();
	 var previousDisabled = !this._isPreviousPageNavigatable();
	 if (this.isLeftToRight() ){
	   this.rightButtonNode.disabled = nextDisabled;
	   this.leftButtonNode.disabled = previousDisabled;
	 } else {
	   this.leftButtonNode.disabled = nextDisabled;
	   this.rightButtonNode.disabled = previousDisabled;
	 }
   },

   /**
    * Return true if current business object date is visible on timeline.
    */
   _isOnVisiblePage: function(datesLength, dateIndex, visiblePageIndex) {
     var endNumber = datesLength - 1 - ((this._maxPages - visiblePageIndex) * this._maxPoints);
     var startNumber = endNumber - (this._maxPoints - 1);
     return (dateIndex >= startNumber && dateIndex <= endNumber);
   },
   
   /**
    * Gets the percentage position of the date on the timeline based on its index.
    */
   _getPointPosition: function(datesLength, dateIndex){
     var endNumber = datesLength - 1 - ((this._maxPages - this._visiblePage) * this._maxPoints);
     var startNumber = endNumber - (this._maxPoints - 1);
     var positionIndex = dateIndex - startNumber + 1;
     var HundredPercent = 100;
     var distanceBetweenPoints = HundredPercent / (this._maxPoints + 1);
	
     if (this._maxPoints >=3 ){
	   if (positionIndex==1 ) {
		 return 15;
	   } else if (positionIndex == this._maxPoints) {
		 return 85;
	   } else {
		 return positionIndex * distanceBetweenPoints;
	   }
     }
	 return positionIndex * distanceBetweenPoints;
    
   },
   
   /**
    * Determines if there is a previous page available.
    */
   _isPreviousPageNavigatable: function(){
     return this._visiblePage !== 1;
   },
   
   /**
    * Determines if there is a next page available.
    */
   _isNextPageNavigatable: function(){
     return this._visiblePage !== this._maxPages;
   },
   
   /**
    * Creates an exclaimation alert svg icon.
    */
   _createExclaimIcon: function(){

     var xmlns = "http://www.w3.org/2000/svg";
     var boxWidth = 20;
     var boxHeight = 20;

     var svgElem = document.createElementNS(xmlns, "svg");
     svgElem.setAttributeNS(null, "viewBox", "0 0 " + boxWidth + " " + boxHeight);
     svgElem.setAttributeNS(null, "width", boxWidth);
     svgElem.setAttributeNS(null, "height", boxHeight);
     svgElem.setAttributeNS(null, "class", "warning-icon");
     svgElem.style.display = "block";
     var path1 = document.createElementNS(xmlns, "path");
     path1.setAttributeNS(null, 'd', "M10,1 C5,1 1,5 1,10 C1,15 5,19 10,19 C15,19 19,15 19,10 C19,5 15,1 10,1 Z M9.2,5 L10.7,5 L10.7,12 L9.2,12 L9.2,5 Z M10,16 C9.4,16 9,15.6 9,15 C9,14.4 9.4,14 10,14 C10.6,14 11,14.4 11,15 C11,15.6 10.6,16 10,16 Z");
     path1.setAttributeNS(null, 'fill', "#0F62Fe");
     svgElem.appendChild(path1);

     var path2 = document.createElementNS(xmlns, "path");
     path2.setAttributeNS(null, 'id', "Compound_Path");
     path2.setAttributeNS(null, 'd', "M9.2,5 L10.7,5 L10.7,12 L9.2,12 L9.2,5 Z M10,16 C9.4,16 9,15.6 9,15 C9,14.4 9.4,14 10,14 C10.6,14 11,14.4 11,15 C11,15.6 10.6,16 10,16 Z");
     path2.setAttributeNS(null, 'fill', "#FFF");
  
     svgElem.appendChild(path2);
   
     return svgElem;
   },
   
   /**
    * Creates circle svg icon.
    */
   _createTimelinePointIcon: function(){

     var xmlns = "http://www.w3.org/2000/svg";
     var boxWidth = 20;
     var boxHeight = 20;

     var svgElem = document.createElementNS(xmlns, "svg");
     svgElem.setAttributeNS(null, "viewBox", "0 0 " + boxWidth + " " + boxHeight);
     svgElem.setAttributeNS(null, "width", boxWidth);
     svgElem.setAttributeNS(null, "height", boxHeight);
     svgElem.setAttributeNS(null, "class", "timeline-icon");
     svgElem.style.display = "block";
     var circle = document.createElementNS(xmlns, "circle");
     circle.setAttributeNS(null, 'cx', "10");
     circle.setAttributeNS(null, 'cy', "10");
     circle.setAttributeNS(null, 'r', "8");
     circle.setAttributeNS(null, 'stroke', "#0F62FE");
     circle.setAttributeNS(null, 'stroke-width', "2");
     circle.setAttributeNS(null, 'fill', "#FFFFFF");
     svgElem.appendChild(circle);
   
     return svgElem;
   },

   /**
    * Creates a chevron svg icon.
    */
   _createChevronIcon: function(){

     var xmlns = "http://www.w3.org/2000/svg";
     var boxWidth = 20;
     var boxHeight = 20;

     var svgElem = document.createElementNS(xmlns, "svg");
     svgElem.setAttributeNS(null, "viewBox", "0 0 " + boxWidth + " " + boxHeight);
     svgElem.setAttributeNS(null, "width", boxWidth);
     svgElem.setAttributeNS(null, "height", boxHeight);
     svgElem.setAttributeNS(null, "class", "chevron");
     svgElem.setAttributeNS(null, 'role', 'img')
  
     var polygon = document.createElementNS(xmlns, "polygon");
     polygon.setAttributeNS(null, 'points', "1,10 10,2 19,10");
     polygon.setAttributeNS(null, 'fill', "white");
     polygon.setAttributeNS(null, 'stroke', "rgb(224, 224, 224)");
     polygon.setAttributeNS(null, 'stroke-width', "1.5");
     svgElem.appendChild(polygon);

     var line = document.createElementNS(xmlns, "line");
     line.setAttributeNS(null, 'x1', "0");
     line.setAttributeNS(null, 'y1', "10");
     line.setAttributeNS(null, 'x2', "20");
     line.setAttributeNS(null, 'y2', "10");
     line.setAttributeNS(null, 'stroke', "#FFF");
     line.setAttributeNS(null, 'stroke-width', "2");
     svgElem.appendChild(line);
   
     return svgElem;
   }
  });
});