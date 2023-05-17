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
        "dojo/text!./templates/BusinessObjectViewer.html",
        "curam/aes/EvidenceViewer",
        "dojo/dom-style",
        "dijit/registry",
        "curam/util/ResourceBundle",
        "dijit/popup",
		"dojo/on",
        "curam/aes/BusinessObjectTimeline",
        "dojo/domReady!"
        ], function(declare,
              _Widget,
              _TemplatedMixin,
              template,
              EvidenceViewer,
              domStyle,
              registry,
              ResourceBundle,
              popup,
 			  on,
              EvidenceTimeline) {

  return declare('curam.aes.BusinessObjectViewer', [ _Widget, _TemplatedMixin ], {

  /**
   * The HTML template to use for this widget.
   */
  templateString: template,

  /**
   * title setter.
   * Sets the Business Object Title.
   */
  _setTitleAttr: {node: 'titleNode', type: 'innerHTML'},

  /**
   * description setter.
   * Sets the Business Object description.
   * @param description Text content of description of business object.
   */
  _setDescriptionAttr: function(description){
	domStyle.set(this.timelineTitleContainerNode, 'height', 'auto');
	this.timelineDescriptionNode.innerText = description;
  },

  /**
   * informational setter.
   * Sets the informational text & displays the informational if text exists/
   * @param informational Text content of the informational message.
   */
  _setInformationalAttr: function(informational){
    this._set('informational', informational);
    if (this.type!='Incoming' && informational.length>0) {
       domStyle.set(this.informationalContainerNode, 'visibility', 'visible');
	   domStyle.set(this.informationalContainerNode, 'display', 'block');
       this.informationalNode.innerText = informational;

		//If no evidence exists hide the following
		if (!this.businessObjects.length>0) {
			domStyle.set(this.timelineContainerNode, 'display', 'none');
			domStyle.set(this.evidenceNode, 'border', 'none');
		}
     } else {
       domStyle.set(this.informationalContainerNode, 'display', 'none');
     }
  },

  /**
   * source case text.
   * Sets the Business Object source case information text.
   */
  _setSourceCaseDescriptionAttr: function(sourceCaseDescription) {
	this.sourceCaseNode.innerText = sourceCaseDescription;
  },

  /**
   * _businessObjectsIndex setter.
   */
  _set_businessObjectsIndexAttr: function(_businessObjectsIndex) {
    this._set('_businessObjectsIndex', _businessObjectsIndex);
    this.set('activeDate', this._getBusinessObjectsLatestStartDate());
  },

  /**
   * activeDate setter.
   */
  _setActiveDateAttr: function(activeDate) {
    this._set('activeDate', activeDate);
    this._updatedBusinessObject();
  },

  /**
   * @constructor
   */
  constructor: function(args) {
    this.inherited(arguments);
    // Reference to a button which fires an event to move to the previous business object in the list.
    this.navigationPreviousNode = null;
    // Reference to a button which fires an event to move to the next business object in the list.
    this.navigationNextNode = null;
    this.businessObjects = args.businessObjects || [];
    this._businessObjectsIndex = args._businessObjectsIndex || 0;
    // String representing an informational message. Container is hidden if message is blank.
    this.informational = args.informational || "";
    // Boolean which indicts if there is missing information on the Incoming business object timeline.
    this.missingInfoOnIncomingTimeline = args.missingInfoOnIncomingTimeline || false;
    // Title of widget.
    this.title = args.title || "";
    this.type = args.type || "";
    // Effective date being used to display evidences
    this.activeDate = args.activeDate || this._getBusinessObjectsLatestStartDate();
	// Actions button on business object
	this.actions = [];
    //Flag for timeline point selected
	this.timelineDateSelected = true;

    if (this.businessObjects) {
      this._timeline = new EvidenceTimeline({
        dates: this._getAllEvidencePeriodStartDatesInCurrentBusinessObject(),
        activeDate: this.activeDate,
        type: this.type,
        datesAndActionInds: this._getAllEvidencePeriodStartDatesAndActionableIndicatorInCurrentBusinessObject()
      });
    }
  },

   startup : function() {
     this.inherited(arguments);
     this._updatedBusinessObject();

     if (this.getCurrentBusinessObject()) {
       this._addBusinessObjectTimeline();
     }

     if (this.type!="Incoming") {
	   domStyle.set(this.addNode, 'display', 'none');
	   domStyle.set(this.ignoreNode, 'display', 'none');
	 }
   },

   postMixInProperties: function() {
       this.bundle = new ResourceBundle("IncomingEvidenceComparison");
       if (this.isLeftToRight()) {
           this.previousArrowText = "&larr;";
           this.nextArrowText = "&rarr;";
         } else {
           this.nextArrowText = "&larr;";
           this.previousArrowText = "&rarr;";
       }
       this.nextButtonTitle = this.bundle.getProperty('BusinessObjectViewer.Navigation.Button.Next');
       this.previousButtonTitle = this.bundle.getProperty('BusinessObjectViewer.Navigation.Button.Previous');
       this.addButtonText = this.bundle.getProperty('BusinessObjectViewer.Button.AddAsNew');
       this.ignoreButtonText = this.bundle.getProperty('BusinessObjectViewer.Button.Ignore');
       this.timelineTitleText = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Title');
       this.timelineDescription = this.bundle.getProperty('BusinessObjectTimeline.Navigation.Description');
       this.pointDescription = this.bundle.getProperty('BusinessObjectTimeline.Navigation.PointDescription');
  
	   if (this.activeDate) {
         this.noMatchingEvidenceText = this.bundle.getProperty('BusinessObjectViewer.Info.NoMatchingEvidence', [curam.date.formatDate(this.activeDate, jsDF)]);
       } else {
    	 this.noMatchingEvidenceText = "";
       }
   },


    postCreate: function() {
	  this.inherited(arguments);
      var _self = this;
      var actions=this._getAllEvidenceActions();
 	 
      if (this.type==="Incoming") {
 	
	    // Check if Add To Case or Ignore action exists on evidence
     	actions.forEach(function(evidenceAction){
		  if (evidenceAction.action === "AddToCase") {
		    if (evidenceAction.disabledIndOpt) {
			  _self.addNode.disabled = true;
			} else {
			  // Add click event listener to Add button
			  _self._addButtonEventListener(evidenceAction.action);
			}
		  }

		  if (evidenceAction.action === "Dismiss") {
		    if (evidenceAction.disabledIndOpt) {
			  _self.ignoreNode.disabled = true;
			} else {
			  // Add click event listener to Ignore button
			  _self._addButtonEventListener(evidenceAction.action);
			}
		  }
		});
	}

	 // Disable the actions button if no actions are available
	 var count = 0;
	 actions.forEach(function(action){
	   if (action.disabledIndOpt === true) {
         count++;
	   }
	 });

	 if (actions.length === count) {
	   _self.ignoreNode.disabled = true;
	   _self.addNode.disabled = true;

	 }
	},


   /**
    * Creates & renders an evidence viewer widget for each evidence in the Business Object.
    */
   _showEvidenceViewers: function() {
     var _self = this;
	 var evidencesActiveOnDate=this._getCurrentBusinessObjectEvidencesOnActiveDate();

     if (evidencesActiveOnDate.length>0) {
		evidencesActiveOnDate.forEach(function(evidence){
       	  evidence.type = _self.type;
          var evidenceViewer = new EvidenceViewer(evidence);
          evidenceViewer.placeAt(_self.evidenceNode);
     	});
		domStyle.set(this.noComparisonNode, 'display', 'none');
		
	 } else {
		if (this.businessObjects.length>0) {
			var noMatchingBusinessObjectText ="";
			if (this.activeDate) {
				noMatchingBusinessObjectText = this.bundle.getProperty('BusinessObjectViewer.Info.NoMatchingEvidence', [curam.date.formatDate(this.activeDate, jsDF)]);
			}
			this.noComparisonTextNode.innerText = noMatchingBusinessObjectText;

			if (this.type!="Incoming") {
		      this.noMatchingExistingEvidenceNode1.innerText = this.bundle.getProperty('BusinessObjectViewer.Info.NoMatchingExistingEvidence1');
			  this.noMatchingExistingEvidenceNode2.innerText = this.bundle.getProperty('BusinessObjectViewer.Info.NoMatchingExistingEvidence2');
			}
			domStyle.set(this.noComparisonNode, 'display', 'block');
		}
	 }
    },

   /**
    * Removes all evidence viewer widgets.
    */
   _removeEvidenceViewers: function() {
     var evidenceViewers = this.getEvidenceViewers();
     evidenceViewers.forEach(function(evidenceViewer) {
       evidenceViewer.destroyRecursive();
     });
   },

   /**
    * Adds the BusinessObjectTimeline widget.
    */
   _addBusinessObjectTimeline: function() {
     this._timeline.placeAt(this.timelineNode);
     var _self = this;
       this.own(
       this._timeline.on('date', function(e){
           _self.set('activeDate', e.detail);
         })
      );
   },

   /**
    * Removes the BusinessObjectTimeline widget.
    */
   _removeEvidenceTimeline: function() {
     this._timeline.destroyRecursive();
   },

   /**
    * Updates navigation text.
    */
   _updateNavigationMessage: function() {
     var displayIndex = this._businessObjectsIndex + 1;
     var displayTotal = this.businessObjects.length;
     var navText = this.bundle.getProperty('BusinessObjectViewer.Navigation.Message', [displayIndex, displayTotal]);
     this.navigationTextNode.innerText = navText;
   },


   /**
    * Enables/Disables the next button depending on we are on the last Business Object.
    */
   _updateNextButton: function() {
     if((this._businessObjectsIndex + 1) === this.businessObjects.length){
       this.navigationNextNode.disabled = true;
     } else {
       this.navigationNextNode.disabled = false;
     }
   },

   /**
    * Enables/Disables the previous button depending on we are on the first Business Object.
    */
   _updatePreviousButton: function() {
     if(this._businessObjectsIndex === 0){
       this.navigationPreviousNode.disabled = true;
     } else {
       this.navigationPreviousNode.disabled = false;
     }
   },

   /**
    * Emits events to let parents know the business object being display has been updated.
    * Meaning either the a different business object is being displayed or
    * the effective date used to display evidences within the business object has been changed.
    */
   _emitUpdatedEvent: function() {
     this.emit('updated', {bubbles: true});
   },

   /**
    * @returns an array of evidence viewer children, currently attached to this widget.
    */
   getEvidenceViewers: function() {
     return registry.findWidgets(this.evidenceNode);
   },

   /**
    * Gets the currently displayed business object.
    * @returns A business object.
    */
   getCurrentBusinessObject: function() {
     return this.businessObjects[this._businessObjectsIndex];
   },

   /**
    * Updates the evidence viewers being displayed & emits event to tell parent.
    */
   _updatedBusinessObject: function() {
    this._removeEvidenceViewers();
    this._showEvidenceViewers();
    this._updateTimeline();
    this._emitUpdatedEvent();
   },

   /**
    * Updates the timeline being displayed.
    */
   _updateTimeline: function(){
     this._timeline.set('datesAndActionInds', this._getAllEvidencePeriodStartDatesAndActionableIndicatorInCurrentBusinessObject());
     this._timeline.set('dates', this._getAllEvidencePeriodStartDatesInCurrentBusinessObject());
     this._timeline.set('activeDate', this.activeDate);
     this._timeline.set('type', this.type);
   },

  /**
   * Set the timelineDateSelected variable to true and set activeDate attribute.
   */
  _setTimelineDateSelectedFlag: function(activeDate) {
	this._set('timelineDateSelected', true);
	this._setActiveDateAttr(activeDate);
  },

  /**
    * Gets an Array of successions which are relevant on a given date.
    * @return Array of Evidences who's period contains the given date.
    */
   _getCurrentBusinessObjectEvidencesOnActiveDate: function() {
    var evidencesActiveOnDate= [];
    var businessObject = this.getCurrentBusinessObject();
    if (businessObject && businessObject.evidences) {
      var evidenceLength = businessObject.evidences.length;
      for (var i = 0; i < evidenceLength; i++) {
        var evidence = businessObject.evidences[i];
		if (this.timelineDateSelected && this._isEvidenceStartDateSameAsActiveDate(evidence)) {
	      evidencesActiveOnDate.push(evidence);
		} else if (this._isEvidenceActive(evidence) && !this.timelineDateSelected) {
          evidencesActiveOnDate.push(evidence);
		}
      }
    }
    
    return evidencesActiveOnDate;
   },

   /**
    * Checks if an evidence is active on the activeDate.
    * @returns true if it start on or before the active date, and end on or after the active date, false otherwise.
    */
   _isEvidenceActive: function(evidence){
    var startDate = this._getDate(evidence.startDate);
    var endDate = this._getDate(evidence.endDate);
    if ((startDate.getTime() <= this.activeDate.getTime()) && (endDate.getTime() === this._getZeroDate().getTime() || endDate.getTime() >= this.activeDate.getTime())) {
      return true;
    }
    return false;
   },

   /**
    * Checks if an evidence has same date as activeDate.
    *
    */
   _isEvidenceStartDateSameAsActiveDate: function(evidence){
    var startDate = this._getDate(evidence.startDate);
	var effectiveDate  = this._getDate(evidence.effectiveDate);

    if ((startDate.getTime() === this.activeDate.getTime()) || (effectiveDate.getTime() === this.activeDate.getTime())) {
      return true;
    }
    return false;
   },

   /**
    * Gets an Array of all Evidence period start dates on a business object.
    */
    _getAllEvidencePeriodStartDatesInCurrentBusinessObject: function() {
      var startDates = [];
      var businessObject = this.getCurrentBusinessObject();
      if (businessObject && businessObject.evidences) {
        var evidenceLength = businessObject.evidences.length;
        for (var i = 0; i < evidenceLength; i++) {
          var evidence = businessObject.evidences[i];
          var startDate = this._getDate(evidence.startDate);
          if (!this._isDateInArray(startDate, startDates)) {
            startDates.push(startDate);
          }
        }
      }
      this._sortDatesNewestToOldest(startDates);
      return startDates;
    },

    /**
     * Gets an Array of all Evidence period start dates on a business object.
     */
    _getAllEvidencePeriodStartDatesAndActionableIndicatorInCurrentBusinessObject: function() {
      var startDatesAndActionableInd = [];
      var businessObject = this.getCurrentBusinessObject();
      if (businessObject && businessObject.evidences) {
        var evidenceLength = businessObject.evidences.length;
        for (var i = 0; i < evidenceLength; i++) {
          var evidence = businessObject.evidences[i];
          var startDate = this._getDate(evidence.startDate);
          var isActionable = this._isEvidenceActionable(evidence);
		  var isMissingData = this._isEvidenceMissingData(evidence);
          var isConflictingData = this._isEvidenceConflictingData(evidence);
          var dateAndActionInd = this._getDateAndActionIndObjectFromArray(startDate, startDatesAndActionableInd);
          if (!dateAndActionInd) {
            startDatesAndActionableInd.push({date: startDate, actionable: isActionable, missingData: isMissingData, conflictInd: isConflictingData});
          } else {
            dateAndActionInd.actionable = dateAndActionInd.actionable || isActionable;
            dateAndActionInd.missingData = dateAndActionInd.missingData || isMissingData;
            dateAndActionInd.conflictInd =  dateAndActionInd.conflictInd || isConflictingData;
          }
        }
      }
      this._sortDatesAndActionIndNewestToOldest(startDatesAndActionableInd);
      return startDatesAndActionableInd;
    },

    /**
     * Returns true if evidence contains an attribute that is mandatory and missing a value.
     * @param evidence
     */
	_isEvidenceMissingData: function(evidence){
	  if (this.type==="Incoming") {
		var attributeLength = evidence.attributes.length;
        for (var i = 0; i < attributeLength; i++) {
		  if (evidence.attributes[i].mandatory && (!evidence.attributes[i].value || evidence.attributes[i].value==="0.00")) {
            return true;
		  }
	    }
	  }
	  return false;
	},
	 
	/**
     * Check if evidence has no missing information and is conflicting 
     * data on the timeline.
     * @return true if evidence not missing information and has a conflict, 
     * otherwise return false.
     */
	_isEvidenceConflictingData: function(evidence){
      
	  if(!this.missingInfoOnIncomingTimeline && evidence.conflictIndOpt){
	    return  true;
	  }
      return false;
	},
	
    /**
    * Gets all the evidence actions in the business object, and returns
    * array of sorted actions.
    * @return array of evidence actions
    */
	_getAllEvidenceActions: function() {

	  var actions = [];
      var businessObject = this.getCurrentBusinessObject();

      if (businessObject && businessObject.evidences) {
  	    businessObject.evidences.forEach(function (evidence){
  		  evidence.actions.forEach(function (evAction) {

  	      if (evAction.action == "Edit") {
  	        return;
  	      }

  	      var found = false;
  	      actions.forEach(function (menuItem) {
  		    if (menuItem.action == evAction.action) {
  		      found = true;
  		      if ( !evAction.disabledIndOpt && menuItem.disabledIndOpt != evAction.disabledIndOpt ){
  			    var index = actions.indexOf(menuItem);
  				actions[index] = evAction;
  		      }
  		    }
  		  });

  	      if (!found) {
		    actions.push(evAction);
  	      }
  	    });
      });
     }

	 // Sort out business object actions dropdown menu items
	 var order = ["AddToCase", "Dismiss"];
	 return this._sortActions(actions, order, 'action');

	},

    /**
    * Sort the actions menu items to order given
    * @return sorted actions
    */
 	_sortActions: function(actions, order, key) {

  	  actions.sort( function (a, b) {
        if (order.indexOf(a[key]) > order.indexOf(b[key]))
          return 1;
        else
          return -1;
    });

    return actions;
    },

    /**
    * Checks if an evidence has actions available
    * @return true if actionable, false otherwise
    */
   _isEvidenceActionable: function(evidence){
     return evidence.actions.length > 0;
   },

    /**
     * Sorts an array of dates newest to oldest
     * @param dates Array of Date Objects
     */
    _sortDatesNewestToOldest: function(dates){
      dates.sort(function(d1, d2){
        return d1.getTime() - d2.getTime();
      });
    },

    /**
     * Sorts an array of dates newest to oldest
     * @param dates Array of Date Objects
     */
    _sortDatesAndActionIndNewestToOldest: function(dateAndActionInds){
      dateAndActionInds.sort(function(d1, d2){
        return d1.date.getTime() - d2.date.getTime();
      });
    },

    /**
     * Checks if the given date is in the given Array.
     * @param date Date object
     * @param dateArray Array of Date objects
     */
    _isDateInArray: function(date, dateArray){
      for (var i = 0; i < dateArray.length; i++) {
        var dateInArray = dateArray[i];
        if (date.getTime() === dateInArray.getTime()) {
          return true;
        }
      }
      return false;
    },

    /**
     * Checks if the given date is in the given Array.
     * @param date Date object
     * @param dateArray Array of Date objects
     */
    _getDateAndActionIndObjectFromArray: function(date, dateAndActionIndArray){
      for (var i = 0; i < dateAndActionIndArray.length; i++) {
        var dateAndActionIndObject = dateAndActionIndArray[i];
        var dateInArray = dateAndActionIndObject.date;
        if (date.getTime() === dateInArray.getTime()) {
          return dateAndActionIndObject;
        }
      }
      return null;
    },

   /**
    * Gets the earliest start date of an evidence's period in a business object.
    * @param business object containing an array of evidence which has a start attribute.
    * @return earliest start date of an evidence's period in the business object.
    */
    _getBusinessObjectsEarliestStartDate: function(){
      var earliestStartDate;
      var businessObject = this.getCurrentBusinessObject();
      if (businessObject && businessObject.evidences) {
        var evidenceLength = businessObject.evidences.length;
        for (var i = 0; i < evidenceLength; i++) {
          var evidence = businessObject.evidences[i];
          var startDate = this._getDate(evidence.startDate);
          if (!earliestStartDate || startDate < earliestStartDate) {
            earliestStartDate = startDate;
          }
        }
      }
      return earliestStartDate;
     },

     /**
      * Gets the latest start date of an evidence's period in a business object.
      * @param business object containing an array of evidence which has a start attribute.
      * @return latest start date of an evidence's period in the business object.
      */
     _getBusinessObjectsLatestStartDate: function(){
      var latestStartDate;
      var businessObject = this.getCurrentBusinessObject();
      if (businessObject && businessObject.evidences) {
        var evidenceLength = businessObject.evidences.length;
        for (var i = 0; i < evidenceLength; i++) {
          var evidence = businessObject.evidences[i];
          var startDate = this._getDate(evidence.startDate);
          if (!latestStartDate || startDate > latestStartDate) {
            latestStartDate = startDate;
          }
        }
      }
      return latestStartDate;
     },

     /**
      * Converts a date string YYYY-MM-DD into a JavaScript date
      * which will takes takes the time zone into account so that
      * when the JavaScript date is converted back into YYYY-DD-MM,
      * it displays the original dateString value.
      */
     _getDate: function(dateString) {
      var localisedDate = new Date(dateString);
      return new Date(localisedDate.getTime() + localisedDate.getTimezoneOffset() * 60000);
     },

     /**
      * Gets Zero Date.
      */
     _getZeroDate: function(){
      return this._getDate("0001-01-01");
     },

   /**
    * Adds an click & enter/space key down event listeners to the 'Add to case' button. 
    */ 
  	_addButtonEventListener: function(event) {
      var _self = this;
	  var targetButton = _self.ignoreNode;
	  if (event==="AddToCase") {
		targetButton = _self.addNode;
	  } 
		
      on(targetButton, 'click, keydown', function(e) {
	    if (e.type === 'keydown' && (e.keyCode === 32 || e.keyCode === 13) || e.type === 'click') {
	    _self.emit(event, {bubbles: true, detail: _self.params});
	     }
      })
    }
  });
});
