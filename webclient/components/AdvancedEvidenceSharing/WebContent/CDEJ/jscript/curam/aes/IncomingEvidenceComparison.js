/*
 * Licensed Materials - Property of IBM
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2017,2022. All rights reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define(["dojo/_base/declare",
		"dijit/_Widget",
		"dijit/_TemplatedMixin",
        "dojo/text!./templates/IncomingEvidenceComparison.html",
		"dojo/dom-style",
        "curam/aes/BusinessObjectViewer",
        "curam/util/UimDialog",
        "curam/util/ResourceBundle",
		"dojo/on",
        "dojo/domReady!"
        ], function(declare,
        			_Widget,
        			_TemplatedMixin,
        			template,
					domStyle,
        			BusinessObjectViewer,
        			uimDialog,
        			ResourceBundle,
					on) {

  return declare('curam.aes.IncomingEvidenceComparison', [ _Widget, _TemplatedMixin ], {

	/**
	 * The HTML template to use for this widget.
	 */
	templateString: template,

	/**
	 * _height setter.
	 * Sets the height & updates the height of the containing iFrame.
	 */
	_set_heightAttr: function(height){
		var previousHeight = this._height;
		var frameHeight = frameElement.offsetHeight;
		var difference = frameHeight - previousHeight;
		this._set('_height', height);
		frameElement.style.height = height + difference + 'px';
	},

	/**
	 * @constructor
	 */
	constructor: function() {
		this.inherited(arguments);

		// Evidence comparison json string
		this.json = "";
		// Reference to incoming viewer
		this._incomingBusinessObjectViewer = null;
		// Reference to existing viewer
		this._existingBusinessObjectViewer = null;
		// The height of this widget
		this._height = 0;
		// The existing business objects
		this._businessObjectsIndex = 0;
		// The active date selected on the timeline
		this._activeDate = null;
		// The conflict index
		this._conflictIndex = 0;
		// Flag for updating using existing
		this._useExistingInd = false;
		// Flag for updating using incoming
		this._useIncomingInd = false
		//The missing info index
		this._missingInfoIndex = 0;
		// Flag for single/multiple maintenance pattern 
		this._maintenancePatternInd = false;
	},

	/**
	 * Final life cycle method.
	 */
    startup: function() {
    	this.inherited(arguments);
    	this._createBusinessObjectViewers();
		this._updateNavigationMessage();
		this._updateNavigationButtons();
		this._displayMissingInformationCard();
		this._displayUpdateCard();
		this._displayNavigationCard();
		this._setInformationalMessage();
   	   	this.set('_height', this.domNode.offsetHeight);
    },

    postMixInProperties: function() {
       this.bundle = new ResourceBundle("IncomingEvidenceComparison");
       this.nextButtonTitle = this.bundle.getProperty('BusinessObjectViewer.Navigation.Button.Next');
       this.previousButtonTitle = this.bundle.getProperty('BusinessObjectViewer.Navigation.Button.Previous');
       this.actionButtonText = this.bundle.getProperty('EvidenceViewer.Button.Actions');
       this.updateButtonText = this.bundle.getProperty('IncomingComparison.Button.Update');
       this.informationMissingText = this.bundle.getProperty('IncomingComparison.MissingInformation.Description');
	   this.conflictText = this.bundle.getProperty('IncomingComparison.Conflict.Description');	    
       this.updateExistingEvidence = this.bundle.getProperty('IncomingComparison.Update.ExistingEvidence');
       this.updateExistingEvidenceLine2 = this.bundle.getProperty('IncomingComparison.Update.TextLine2');
       this.incomingRadioText = this.bundle.getProperty('IncomingComparison.Update.IncomingRadioButton');
	   this.existingRadioText = this.bundle.getProperty('IncomingComparison.Update.ExistingRadioButton');
	   this.navigationTitle = this.bundle.getProperty('BusinessObjectViewer.Existing.NavigationTitle');
 	
    },

    /**
     * Create both incoming & existing business object viewers
     * and high the differences between evidence attributes.
     */
    _createBusinessObjectViewers: function() {
    	var data = JSON.parse(this.json);
    	var incomingBusinessObject = data.incomingBusinessObject;
    	var existingBusinessObjectList = data.existingBusinessObjects;

    	this._createIncomingBusinessObjectViewer(incomingBusinessObject);
    	this._checkForMissingInformation();
    	this._createExistingBusinessObjectViewer(existingBusinessObjectList);
   
    	this._checkForConflicts();
    	if (this._conflictIndex == 0) {
    		this._set('_activeDate', this._incomingBusinessObjectViewer.activeDate);
    		this._existingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
    	}
		this._highlightAttributeDifferences();
		this._updateBusinessObjectTimelines();
		this._updateTitleContainerHeights();
		this._updateTimelineContainerHeights();
		
    },
    
    /**
     * Update the timelines on the incoming and existing business object viewers.
     */
    _updateBusinessObjectTimelines: function() {
		var businessObjectViewers = [this._incomingBusinessObjectViewer, this._existingBusinessObjectViewer];
		
		businessObjectViewers.forEach(function(businessObjectViewer){
			
			 businessObjectViewer._updateTimeline();
			});
	},
	
	/**
     * Update the timelines container heights to be the same on the incoming and existing business object viewers.
     */
    _updateTimelineContainerHeights: function() {
	  var x = this._incomingBusinessObjectViewer.timelineTitleContainerNode.offsetHeight;
	  var y = this._existingBusinessObjectViewer.timelineTitleContainerNode.offsetHeight;
	  var maxHeight = Math.max(x, y);
      domStyle.set(this._incomingBusinessObjectViewer.timelineTitleContainerNode, 'height', maxHeight + 'px');
      domStyle.set(this._existingBusinessObjectViewer.timelineTitleContainerNode, 'height', maxHeight + 'px');
	},

	/**
     * Update the title container padding so has to be the same height on the incoming and existing business object viewers.
     */
    _updateTitleContainerHeights: function() {
	  var x = this._incomingBusinessObjectViewer.titleContainerNode.offsetHeight;
	  var y = this._existingBusinessObjectViewer.titleContainerNode.offsetHeight;
	  var maxHeight = Math.max(x, y);
	  if (maxHeight==x) {
	    domStyle.set(this._existingBusinessObjectViewer.titleContainerNode, 'paddingBottom', (x-y) + 15 + 'px');
	  } else {
	    domStyle.set(this._incomingBusinessObjectViewer.titleContainerNode, 'paddingBottom', (y-x) + 15 + 'px');
	  }
	},
	
    /**
     * If there are actions enabled, display the information message.
     */
    _setInformationalMessage: function(){
     if (this._incomingBusinessObjectViewer._getAllEvidenceActions().length === 0) {
       domStyle.set(this.informationalContainerNode, 'display', 'block');
	   domStyle.set(this.incomingEvidenceComparisonHeaderNode, 'display', 'none');
	   domStyle.set(this.informationMissingNode, 'display', 'none');
	   domStyle.set(this.updateCardNode, 'display', 'none');
	   var informationalNode = this.informationalNode;
	   var content = this.bundle.getProperty('IncomingComparison.Informational.EvidenceResolved');
	   setTimeout(function(){informationalNode.innerHTML = content;}, 10);

     }
    },

    /**
     * Reloads the page this widget is contained on.
     */
    _reloadPage: function() {
      location.reload();
    },

    /**
     * Creates the Incoming Business Object viewer.
     */
    _createIncomingBusinessObjectViewer: function(incomingBusinessObject) {
      var incomingBusinessObjects = [incomingBusinessObject];
      this._incomingBusinessObjectViewer = new BusinessObjectViewer({
        title: this.bundle.getProperty('BusinessObjectViewer.Incoming.Title'),
    	informational: '',
    	businessObjects: incomingBusinessObjects,
    	type: 'Incoming',
		description: incomingBusinessObject.description,
		sourceCaseDescription: this.bundle.getProperty('BusinessObjectViewer.Incoming.SourceCaseDescription.From') + " " + incomingBusinessObject.caseDescription
      });
      this._incomingBusinessObjectViewer.placeAt(this.incomingEvidenceComparisonNode);
      this._incomingBusinessObjectViewer.domNode.classList.add('business-object-incoming');
      this._addBusinessObjectViewerEventListeners(this._incomingBusinessObjectViewer);
    },

    /**
     * Creates the Existing Business Object viewer.
     */
    _createExistingBusinessObjectViewer: function(existingBusinessObjects) {
      var existingBusinessObjectsViewerTitle = this.bundle.getProperty('BusinessObjectViewer.Existing.Title');
      if (existingBusinessObjects.length > 0) {
	    this._existingBusinessObjectViewer = new BusinessObjectViewer({
	      title: existingBusinessObjectsViewerTitle,
		  informational: '',
		  missingInfoOnIncomingTimeline: (this._missingInfoIndex>0),
	      businessObjects: existingBusinessObjects,
		  _businessObjectsIndex: this._businessObjectsIndex,
		  description: existingBusinessObjects[this._businessObjectsIndex].description,
		  sourceCaseDescription: this.bundle.getProperty('BusinessObjectViewer.Existing.CaseDescription'),
	      type: 'Existing'
	   });
      } else {
    	this._existingBusinessObjectViewer = new BusinessObjectViewer({
	      title: existingBusinessObjectsViewerTitle,
	      informational: this.bundle.getProperty('BusinessObjectViewer.Info.NoExistingBusinessObjects'),
	      type: 'Existing'
	    });
		
		domStyle.set(this.navigationNode, 'display', 'none');	
     }
      
     this._existingBusinessObjectViewer.placeAt(this.incomingEvidenceComparisonNode);
     this._addBusinessObjectViewerEventListeners(this._existingBusinessObjectViewer);
    },

    /**
     * Listens to the events on a business object viewer.
     */
    _addBusinessObjectViewerEventListeners: function(businessObjectViewer) {
    	var _self = this;
    	// DOJO lower cases all events emitted
        this.own(
    		businessObjectViewer.on('updated', function(e) {
    			_self.set('_height', _self.domNode.offsetHeight);
    						
    		}),
    		businessObjectViewer.on('addtocase', function(e) {
    			var evidence = e.detail;
    			_self._onAddToCase(evidence);
    		}),
    		businessObjectViewer.on('edit', function(e) {
    			var evidence = e.detail;
    			_self._onEdit(evidence);
    		}),
    		businessObjectViewer.on('markasduplicate', function(e) {
    			_self._onMarkAsDuplicate(e.detail);
    		}),
    		businessObjectViewer.on('dismiss', function(e) {
    			_self._onDismiss();
    		}),
    		businessObjectViewer.on('updatewithincoming', function(e) {
    			_self._onUpdateWithIncoming(e.detail);
    		}),
    		businessObjectViewer.on('retainexisting', function(e) {
    			_self._onRetainExisting(e.detail);
    		}),
    		businessObjectViewer.on('removeexisting', function(e) {
    			_self._onRemoveExisting(e.detail);
    		}),
    		businessObjectViewer.on('heightupdated', function(e) {
    			_self.set('_height', _self.domNode.offsetHeight);
    		}),
			businessObjectViewer._timeline.on('date', function(e) {
				_self.set('_activeDate', e.detail);
			})
        )
    },
    
    /**
     * Checks for conflicts between incoming and existing evidences.
     */
    _checkForConflicts: function() {
    	
    	var incomingEvidence = this._incomingBusinessObjectViewer.businessObjects[0].evidences;   	
    	if (this._existingBusinessObjectViewer.businessObjects.length > 0) {
	    	var existingEvidence = this._existingBusinessObjectViewer.getCurrentBusinessObject().evidences;
	    	this._conflictIndex = this._checkForEvidenceAttributeConflictsOnIncomingEvidence(incomingEvidence, existingEvidence);
    	} else {
    		this._conflictIndex = 0;
    	}
    },
    
    /**
     * Checks for conflicts between incoming and existing evidences.
     */
    _checkForEvidenceAttributeConflictsOnIncomingEvidence: function(incomingEvidenceArray, existingEvidenceArray) {
     	var numOfConflicts = 0;
     	var conflictingStartDateAndEvidType = [];
    	var evidenceLength = incomingEvidenceArray.length;
    	for (var i = 0; i < evidenceLength; i++) {
    		var incomingEvidence = incomingEvidenceArray[i];
    		var conflictExistsWithinTimelineDate = false;
			if (existingEvidenceArray.length>0 && !incomingEvidence.pendingRemovalInd) {
		    	var targetEvidencesOfTypeAndDate = this._filterEvidenceByTypeAndDateOrMaintenancePattern(incomingEvidence.evidenceType, incomingEvidence.startDate, incomingEvidence.participantID, existingEvidenceArray);
		    	 
		    	if (targetEvidencesOfTypeAndDate.length>0) {
		    		 var attributeLength = incomingEvidence.attributes.length;
		    	 
		    	for (var k = 0; k < attributeLength; k++) {
		    		var attribute = incomingEvidence.attributes[k];
		    		var existingEvidence = this._checkAttributeInEvidenceArray(attribute, targetEvidencesOfTypeAndDate);
	  						if (existingEvidence) {
			
	  							existingEvidence.conflictIndOpt = true;
	  							incomingEvidence.conflictIndOpt = true;
	  							var startDate = this._getDate(incomingEvidence.startDate);
	  						    var newConflictDateInd = this._checkIfStartDateExistsInArray(startDate, conflictingStartDateAndEvidType);
	  				            if (!newConflictDateInd) {
	  				              // Conflict number is per timeline start/effective date
	  					          conflictingStartDateAndEvidType.push({date: startDate, type: incomingEvidence.evidenceType});
	  					          conflictExistsWithinTimelineDate = true;
	  					        }
	  						}
		    		}
		    	}
			}
			if (conflictExistsWithinTimelineDate) {
			  numOfConflicts++;
			} else {
			  incomingEvidence.conflictIndOpt = false;
			}
			
		}
    	this._sortDatesNewestToOldest(conflictingStartDateAndEvidType);
        if (numOfConflicts > 0) {
        	this._set('_activeDate', conflictingStartDateAndEvidType[conflictingStartDateAndEvidType.length - 1].date);		
    		this._incomingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
    		this._existingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
        }
    	return numOfConflicts;
    	
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
     * Sorts an array of dates newest to oldest
     * @param dates Array of Date Objects
     */
    _sortDatesNewestToOldest: function(dates){
      dates.sort(function(d1, d2){
        return d1.date.getTime() - d2.date.getTime();
      });
    },

    /**
     * Checks if the given date is in the given Array.
     * @param date Date object
     * @param dateAndEvdTypeArray Array of Date objects
     */
    _checkIfStartDateExistsInArray: function(date, dateAndEvdTypeArray){
      for (var i = 0; i < dateAndEvdTypeArray.length; i++) {
        var dateAndActionIndObject = dateAndEvdTypeArray[i];
        var dateInArray = dateAndActionIndObject.date;
        if (date.getTime() === dateInArray.getTime()) {
          return true;
        }
      }
      return false;
    },
    
    /**
     * Sets the highlight attribute to <code>true</code> on each evidence viewer's attribute
     * who's information is not present on the existing business object, <code>false</code> otherwise.
     */
    _highlightAttributeDifferences: function() {
    
      if (this._missingInfoIndex===0) {
	      if (this._conflictIndex>0) {
	    	var incomingEvidenceViewers = this._incomingBusinessObjectViewer.getEvidenceViewers();
	    	var existingEvidenceViewers = this._existingBusinessObjectViewer.getEvidenceViewers();
	    	this._highlightEvidenceViewerAttributeDifferences(incomingEvidenceViewers, existingEvidenceViewers);
	    	this._highlightEvidenceViewerAttributeDifferences(existingEvidenceViewers, incomingEvidenceViewers);
	      } else{
	    	  //remove any existing conflict attribute highlights
	    	  var incomingEvidenceViewers = this._incomingBusinessObjectViewer.getEvidenceViewers();
	          for (var i = 0; i < incomingEvidenceViewers.length; i++) {
	      		var evidenceViewer = incomingEvidenceViewers[i];
	      		evidenceViewer.getAttributeViewers().forEach(function(attributeViewer){
	      			attributeViewer.set('conflict', false);
	   			});
	          }
	      }
      }
    },
   
    /**
     * Sets the highlight property on all attributes contained in each evidence in the businessObjectToHighlight,
     * depending on whether an exact attribute match is found with businessObjectToCheckForAttributes.
     * @param businessObjectToHighlight Object whose attributes will have their highlight property set.
     * @param businessObjectToCheckForAttributes Object to check for exact attribute matches.
     */
    _highlightEvidenceViewerAttributeDifferences: function(evidenceViewersToHighlight, evidenceViewersToCheckForAttributes) {
    	var evidenceLength = evidenceViewersToHighlight.length;
    	for (var i = 0; i < evidenceLength; i++) {
    		var evidenceViewer = evidenceViewersToHighlight[i];
			if (evidenceViewersToCheckForAttributes.length>0) {
    		this._highlightEvidenceAttributesNotEvidenceArray(evidenceViewer, evidenceViewersToCheckForAttributes);
			}
    	}
    },

    /**
     * Sets the highlight property on each Attribute in the evidence to <code>false</code>,
     * if that attribute has an exact match in the given businessObject, <code>true</code> otherwise.
     * @param evidence Object containing an Array of Attribute objects.
     * @param businessObject Object containing an Array of evidence objects.
     */
    _highlightEvidenceAttributesNotEvidenceArray: function(evidenceViewer, targetEvidencesViewers) {
    	var targetEvidencesViewersOfType = this._filterEvidenceByTypeAndDateOrMaintenancePattern(evidenceViewer.evidenceType, evidenceViewer.startDate, evidenceViewer.participantID, targetEvidencesViewers);
    	var attributeViewers = evidenceViewer.getAttributeViewers();
    	var attributeLength = attributeViewers.length;
    	for (var i = 0; i < attributeLength; i++) {
    		var attributeViewer = attributeViewers[i];
    		if (this._isAttributeInEvidences(attributeViewer, targetEvidencesViewersOfType)) {
				attributeViewer.set('conflict', false);
    		} else if (targetEvidencesViewersOfType.length > 0) {
				attributeViewer.set('conflict', true);

    		}
    	}
    },

    /**
     * Filters the evidence by evidence type.
     * @param evidenceType String evidence type CodeTable value.
     * @param businessObject Object containing an array of evidences.
     * @return a list of evidences with given type.
     */
    _filterEvidenceByTypeAndDateOrMaintenancePattern: function(evidenceType, startDate, participantID, evidences){
    	var filteredEvidences = [];
    	var evidenceLength = evidences.length;
    	for (var i = 0; i < evidenceLength; i++) {

    		var evidence = evidences[i];
    		if (evidence.evidenceType === evidenceType && evidence.participantID === participantID && (evidence.startDate === startDate || evidence.maintenancePattern === "EMP38003" || evidence.maintenancePattern === "EMP38002")) {
    			filteredEvidences.push(evidence);
    		}
    	}
    	return filteredEvidences;
    },

    /**
     * Checks if an attributes information is contained within an array of evidence.
     * @param attribute Object containing a name & a value.
     * @param evidences Array of evidence objects
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    _isAttributeInEvidences: function(attribute, evidences) {
    	var evidenceLength = evidences.length;
    	for (var i = 0; i < evidenceLength; i++) {

    		var evidence = evidences[i];
    		if (this._isAttributeInEvidence(attribute, evidence)) {
    			return true;
    		}
    	}
    	return false;
    },

    /**
     * Checks if an attributes information is contained within an evidence.
     * @param attribute Attribute object containing a name & a value
     * @param evidence Evidence object containing an array of attributes
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    _isAttributeInEvidence: function(attribute, evidenceViewer) {
    	var attributeViewers = evidenceViewer.getAttributeViewers();
    	var evidenceAttributeLength = attributeViewers.length;
    	for (var i = 0; i < evidenceAttributeLength; i++) {
    		var evidenceAttribute = attributeViewers[i];
    		if (this._isAttributeEqual(attribute, evidenceAttribute)) {
    			return true;
    		}
    	}
    	return false;
    },

    /**
     * Checks if an attributes information is contained within an array of evidence.
     * @param attribute Object containing a name & a value.
     * @param evidences Array of evidence objects
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    _checkAttributeInEvidenceArray: function(attribute, evidences) {
    	var evidenceLength = evidences.length;
    	for (var i = 0; i < evidenceLength; i++) {

    		var evidence = evidences[i];
    		if (!this._isAttributeInEvidence(attribute, evidence)) {
    			return evidence;
    		}
    	}
    	return null;
    },

    /**
     * Checks if an attribute exists in a list of attributes in evidence.
     * @param attribute Object containing a name & a value.
     * @param evidence Evidence object.
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    _isAttributeInEvidence: function(attribute, evidence) { 	
    	var attributeLength = evidence.attributes.length;
    	for (var i = 0; i < attributeLength; i++) {
    		var evidenceAttribute = evidence.attributes[i];
    		if (this._isAttributeEqual(attribute, evidenceAttribute)) {
    			return true;
    		}
    	}
    	return false;
    },
    /**
     * Checks if the attributes have the same name and value.
     * @param attribute1 Attribute object containing a name & a value
     * @param attribute2 Attribute object containing a name & a value
     * @return <code>true</code> if attributes have the same name and value, <code>false</code> otherwise.
     */
    _isAttributeEqual: function(attribute1, attribute2) {
    	return attribute1.name === attribute2.name && attribute1.value == attribute2.value;
    },
	 
	 /**
	  * Opens Evidence Modal & refreshes page when its closed.
	  */
	 _openModal: function(page, pageParams) {
       var _self = this;
	   var dialogObject =  uimDialog.open(page, pageParams);
     },
   
     /**
	  * Opens curam modal to add this evidence to the case.
	  */
	 _onAddToCase: function(evidence){
	   var commaSeperatedIncomingEvidenceDescriptorIDs = this. _createCommaSeperatedListOfEvidenceDescriptorIDs(this._incomingBusinessObjectViewer.params.businessObjects[0]);
	   this._openModal('AdvancedEvidenceSharing_addBusinessObjectToCasePage.do', {
		 incomingCommaSeperatedEvidenceDescriptorIDs : commaSeperatedIncomingEvidenceDescriptorIDs
	   });
	 },

	 /**
	  * Opens curam modal to edit this evidence to the case.
	  */
	 _onEdit: function(evidence){
		 this._openModal('AdvancedEvidenceSharing_editIncomingEvidencePage.do',
				{ 
			 		caseID : evidence.caseID,
			 		evidenceID : evidence.evidenceID,
			 		evidenceType: evidence.evidenceType,
			 		evidenceDescriptorID : evidence.evidenceDescriptorID
			 	});
	 },
	 
	 /**
	  * Opens the dismiss incoming evidence record modal.
	  */
	 _onDismiss: function(){	
		 var commaSeperatedIncomingEvidenceDescriptorIDs = this._createCommaSeperatedListOfEvidenceDescriptorIDs(this._incomingBusinessObjectViewer.params.businessObjects[0]);	 
		 this._openModal('AdvancedEvidenceSharing_dismissInBulkIncomingEvidencePage.do', 
				{
			 		incomingCommaSeperatedEvidenceDescriptorIDs : commaSeperatedIncomingEvidenceDescriptorIDs
			 	});
	 },
	 
	 /**
	  * Opens the mark as duplicate incoming evidence record modal.
	  */
	 _onMarkAsDuplicate: function(evidence){		 
		 this._openModal('AdvancedEvidenceSharing_duplicateIncomingEvidencePage.do', 
				{
			 		evidenceDescriptorID : evidence.evidenceDescriptorID
			 	});
	 },
	 
	 /**
	  * Opens the remove existing evidence record modal.
	  */
	 _onRemoveExisting: function(evidence){
		 var commaSeperatedIncomingEvidenceDescriptorIDs = this. _createCommaSeperatedListOfEvidenceDescriptorIDs(this._incomingBusinessObjectViewer.params.businessObjects[0]);
		 this._openModal('AdvancedEvidenceSharing_removeExistingEvidencePage.do', 
				 {
			 		existingEvidenceDescriptorID : evidence.evidenceDescriptorID, 
			 		incomingCommaSeperatedEvidenceDescriptorIDs : commaSeperatedIncomingEvidenceDescriptorIDs
			 	});
	 },
	 
	 /**
	  * Opens Update with Incoming Wizard.
	  */
	 _onUpdateWithIncoming: function(evidence) {
		 var incomingActiveDate = this._incomingBusinessObjectViewer.activeDate;
		 this._openModal('AdvancedEvidenceSharing_updateWithIncomingResolveStartPage.do',
				 {
			 		evidenceDescriptorID : evidence.evidenceDescriptorID,
			 		activeDate: curam.date.formatDate(incomingActiveDate, jsDF),
			 		incomingEvidenceDescriptorIDs: this._createCommaSeperatedListOfEvidenceDescriptorIDs(this._incomingBusinessObjectViewer.params.businessObjects[0])
				 });
	 },
	 
	 /**
	  * Opens Update uim page.
	  */
	 _onUpdate: function(evidence) {
			 
		 if (this._conflictIndex > 0 && !this._useExistingInd && !this._useIncomingInd) {
		  domStyle.set(this.conflictInfoContainerNode, 'display', 'block');   
		  var conflictInformationalNode = this.conflictInformationalNode;
		  var content = this.bundle.getProperty('IncomingComparison.Informational.RadioButtonNotSelected');
		  setTimeout(function(){conflictInformationalNode.innerHTML = content;}, 10);

		 }else{
			
			
			var updateData = {
			    existingEvidenceDescriptorIDs: [],
                incomingEvidenceDescriptorIDs: []
			};

		   var existingBusinessObject = this._existingBusinessObjectViewer.getCurrentBusinessObject();
		   var numEvidence = existingBusinessObject.evidences.length;
		   
		   for (var i = 0; i < numEvidence; i++) {
			 var evidence = existingBusinessObject.evidences[i];
		     updateData.existingEvidenceDescriptorIDs.push({
			     "evidenceDescriptorID" : evidence.evidenceDescriptorID,
                 "conflictIndOpt"  : evidence.conflictIndOpt
      
		     });
		
		   }
	
			var incomingBusinessOBject = this._incomingBusinessObjectViewer.params.businessObjects[0];		
		   for (var i = 0; i < incomingBusinessOBject.evidences.length; i++) {
			 var evidence = incomingBusinessOBject.evidences[i];
			   updateData.incomingEvidenceDescriptorIDs.push({
			     "evidenceDescriptorID" : evidence.evidenceDescriptorID,
                 "conflictIndOpt"  : evidence.conflictIndOpt
      
		     });
		 }
	

			 this._openModal('AdvancedEvidenceSharing_updatePage.do',
					 {
					    existingEvidenceDescriptorIDs: JSON.stringify(updateData.existingEvidenceDescriptorIDs),
				 		incomingEvidenceDescriptorIDs: JSON.stringify(updateData.incomingEvidenceDescriptorIDs),
						useExistingInd: this._useExistingInd,
						useIncomingInd: this._useIncomingInd
				});
		 }
	 },

	 /**
	  * Opens Retain Existin Wizard.
	  */
	 _onRetainExisting: function(evidence) {
		 var incomingActiveDate = this._incomingBusinessObjectViewer.activeDate;
		 this._openModal('AdvancedEvidenceSharing_retainExistingResolveStartPage.do',
				 {
			 		evidenceDescriptorID : evidence.evidenceDescriptorID,
			 		activeDate: curam.date.formatDate(incomingActiveDate, jsDF),
			 		incomingEvidenceDescriptorIDs: this._createCommaSeperatedListOfEvidenceDescriptorIDs(this._incomingBusinessObjectViewer.params.businessObjects[0])
				 });
	 },

	/**
	 * _height setter.
	 * Sets the height & updates the height of the containing iFrame.
	 */
	_set_activeDateAttr: function(activeDate){
		this._set('_activeDate', activeDate);		
		this._incomingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
		this._existingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
		this._highlightAttributeDifferences();
		
	},
	
	 /**
	  * Creates a comma separated list of evidence descriptor IDs.
	  */
	 _createCommaSeperatedListOfEvidenceDescriptorIDs: function(businessObject){
		 var commaSeparatedList = "";
		 var numEvidence = businessObject.evidences.length;
		 for (var i = 0; i < numEvidence; i++) {
			 var evidence = businessObject.evidences[i];
			 commaSeparatedList += evidence.evidenceDescriptorID;
			 commaSeparatedList += ',';
		 }
		 return commaSeparatedList;
	 },
 
   /**
    * Called when previous navigation button is clicked.
    * Emits an event to let parent know the previous business object is to be displayed.
    */
   _previousNavClick: function() {
    if (this._businessObjectsIndex > 0) {
        this.set('_businessObjectsIndex', this._businessObjectsIndex - 1);
      }
   },
  
  /**
    * Called when next navigation button is clicked.
    * Emits an event to let parent know the next business object is to be displayed.
    */
   _nextNavClick: function() {
    if ((this._businessObjectsIndex + 1) < this._existingBusinessObjectViewer.businessObjects.length) {
        this.set('_businessObjectsIndex', this._businessObjectsIndex + 1);
      }
   },

   /**
    * Set _useExistingInd to false and _useIncomingInd to true.
    */
   _incomingRadioSelected: function() {
	  this._useIncomingInd = true;
	  this._useExistingInd = false;
    },
	  
   /**
    * Set _useExistingInd to true and _useIncomingInd to false.
    */
    _existingRadioSelected: function() {
      this._useExistingInd = true;
      this._useIncomingInd = false;
      
	 },
   
  /**
   * _businessObjectsIndex setter.
   */
  _set_businessObjectsIndexAttr: function(_businessObjectsIndex) {
    this._set('_businessObjectsIndex', _businessObjectsIndex);
    this._checkForMissingInformation();
	this._existingBusinessObjectViewer._set_businessObjectsIndexAttr(this._businessObjectsIndex);
	this._updateCaseDescription();
	this._updateNavigationMessage();
    this._updateNavigationButtons();
    this._updateTitleContainerHeights();
    this._updateTimelineContainerHeights();
    this._checkForConflicts();
    if (this._conflictIndex == 0) {
	  this._set('_activeDate', this._incomingBusinessObjectViewer._getBusinessObjectsLatestStartDate());
	  this._existingBusinessObjectViewer._setActiveDateAttr(this._activeDate);
	  this._incomingBusinessObjectViewer._setActiveDateAttr(this._activeDate);
	  this._existingBusinessObjectViewer._setTimelineDateSelectedFlag(this._activeDate);
    }
	this._highlightAttributeDifferences();
	this._updateBusinessObjectTimelines();
	this._displayUpdateCard();

  },

  /**
   * Updates description of current existing business object case.
   */
  _updateCaseDescription: function() {
	var caseDescription = this._existingBusinessObjectViewer.businessObjects[this._businessObjectsIndex].description;
    this._existingBusinessObjectViewer._setDescriptionAttr(caseDescription);
  },
	
  /**
   * Updates next button to navigate existing business objects.
   */
   _updateNextButton: function() {
	
     if ((this._businessObjectsIndex + 1) === this._existingBusinessObjectViewer.businessObjects.length || this._incomingBusinessObjectViewer._getAllEvidenceActions().length === 0) {
       this.navigationNextNode.disabled = true;
     } else {
       this.navigationNextNode.disabled = false;
     }
   },
   
   /**
    * Updates previous button to navigate existing business objects.
    */
   _updatePreviousButton: function() {
     if (this._businessObjectsIndex === 0 || this._incomingBusinessObjectViewer._getAllEvidenceActions().length === 0) {
       this.navigationPreviousNode.disabled = true;
     } else {
       this.navigationPreviousNode.disabled = false;
     }
   },

   /**
    * Updates navigation text.
    */
   _updateNavigationMessage: function() {
     var displayIndex = this._businessObjectsIndex + 1;
     var displayTotal = this._existingBusinessObjectViewer.businessObjects.length;
     var navText = this.bundle.getProperty('BusinessObjectViewer.Navigation.Message', [displayIndex, displayTotal]);
     if (displayTotal > 0) {
       this.navigationTextNode.innerText = navText + ": " + this._existingBusinessObjectViewer.businessObjects[this._businessObjectsIndex].description;
     }
   },

   /**
    * Enables/Disables next/previous buttons based on which Business Object in the list is showing.
    */
   _updateNavigationButtons: function() {
     this._updateNextButton();
     this._updatePreviousButton();
   },

   /**
    * Add Update button event listener.
    */ 
  	_addUpdateButtonEventListener: function(event) {
      var _self = this;
      var updateButton = _self.updateNode;     

      on(updateButton, 'click, keydown', function(e) {
	    if (e.type === 'keydown' && (e.keyCode === 32 || e.keyCode === 13) || e.type === 'click') {
	
			_self._onUpdate(e.detail);
	     }  
      })
    },

   /**
    * Display the Existing Business Object navigation card by default. Hide the Navigation card if only one
    * existing business object exists or there's no actions enabled.
    */ 
	_displayNavigationCard: function(){
		if (this._existingBusinessObjectViewer.businessObjects.length<2 || this._incomingBusinessObjectViewer._getAllEvidenceActions().length === 0) {
			domStyle.set(this.navigationNode, 'display', 'none');
		}
	},
	
   /**
    * Update _missingInfoIndex variable with number of dates on timeline that contain records missing values in attributes.
    */ 
	_checkForMissingInformation: function(){

	  this._missingInfoIndex = 0;
	  var incomingTimeline = this._incomingBusinessObjectViewer._timeline;
	  var timelinePoints = incomingTimeline.datesAndActionInds;
		  
	  for (var i =0; i < timelinePoints.length; i++) {
	    if (timelinePoints[i].missingData) {
		  this._missingInfoIndex++;
	    }
      }
	},
	   
   /**
    * Displays the Missing Information card when Incoming business object is missing values in attributes.
    */ 
	_displayMissingInformationCard: function(){
		
	  if (this._missingInfoIndex>0) {
	    domStyle.set(this.informationMissingNode, 'display', 'block');
	    if (this._missingInfoIndex>1) {
	      var text = this.bundle.getProperty('IncomingComparison.MissingInformations.Title', [this._missingInfoIndex]);
	    } else {
	      var text = this.bundle.getProperty('IncomingComparison.MissingInformation.Title', [this._missingInfoIndex]);
	    }
     	this.informationMissingTitleNode.innerText = text;
     	
     	//disable the Add to Case button
     	this._incomingBusinessObjectViewer.addNode.disabled = true;
      }
    },

   /**
    * Displays the Update card when Incoming business object is not missing any information, and update actions are enabled.
    * If there are conflicts, display the Update card with conflict information.
    */
    _displayUpdateCard: function() {
	  _self = this;
	  if (this._missingInfoIndex===0) {
	    var existingBusinessActions=this._existingBusinessObjectViewer._getAllEvidenceActions();
        // Update the title of Update card
        this.updateExistingEvidenceTextNode.innerText = this.bundle.getProperty('IncomingComparison.Update.TextLine1', [this._incomingBusinessObjectViewer.businessObjects[0].caseDescription]);
         
        //Show the conflict update card if conflicts exist
	    if (this._conflictIndex>0) {
	      var numOfConflicts = this._conflictIndex;
	      if (this._conflictIndex>1) {
		    this.conflictTitle.innerText = this.bundle.getProperty('IncomingComparison.Conflicts.Title', [numOfConflicts]);
	      } else {
		    this.conflictTitle.innerText = this.bundle.getProperty('IncomingComparison.Conflict.Title', [numOfConflicts]);	
	      }
          domStyle.set(this.conflictNode, 'display', 'block');
		
	    } else {
		  domStyle.set(this.conflictNode, 'display', 'none');
	    }
      
        var updateButtonActive = false;

	    // Check if Update action exists on evidence
        var actionsLength = existingBusinessActions.length;
        for (var i = 0; i < actionsLength; i++) {
          var evidenceAction = existingBusinessActions[i];

		  if (!evidenceAction.disabledIndOpt && (evidenceAction.action === "UpdateWithIncoming" || evidenceAction.action === "RetainExisting")) {
		  // Add click event listener to Update button
		  _self._addUpdateButtonEventListener(evidenceAction.action);
          updateButtonActive = true;
		  break;
		}	
	  }

	  // Hide the Update card if no actions available or the update button not enabled
	  if (existingBusinessActions.length === 0) {
	    domStyle.set(this.updateCardNode, 'display', 'none');
	  } else if (!updateButtonActive) {
		domStyle.set(this.updateCardNode, 'display', 'none');
	  } else {
		domStyle.set(this.updateCardNode, 'display', 'block');
	  }
	} else {
	  domStyle.set(this.updateCardNode, 'display', 'none');
	}
  }
	
  });
});