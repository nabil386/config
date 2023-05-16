/*
 *  Licensed Materials - Property of IBM
 *  
 *  PID 5725-H26
 *  
 *  Copyright IBM Corporation 2017,2021. All rights reserved.
 *  
 *  US Government Users Restricted Rights - Use, duplication or disclosure
 *  restricted by GSA ADP Schedule Contract with IBM Corp.
 *
*/
define(["dojo/_base/declare",
    "dijit/_Widget", 
    "dijit/_TemplatedMixin",
    "dijit/_WidgetsInTemplateMixin",
        "dojo/text!./templates/EvidenceViewer.html",
        "curam/aes/EvidenceAttributeViewer",
        "curam/util/ResourceBundle",
		"dojo/dom-style",
        "dojo/on",
        "dijit/_Container",
        "dojo/domReady!"
        ], function(declare, 
              _Widget, 
              _TemplatedMixin,
              _WidgetsInTemplateMixin,
              template,
              EvidenceAttributeViewer,
              ResourceBundle,
			  domStyle,
              on,
              _Container) {
  
  return declare('curam.aes.EvidenceViewer', [ _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, _Container ], { 
    
  /**
   * The HTML template to use for this widget.
   */
  templateString: template,
  
  /**
   * @constructor
   */
  constructor: function() {
    this.inherited(arguments);
    
    // Display name of the evidence
    this.name = "";
    // Status CodeTable code
    this.statusCode = "";
    this.evidenceType = "";
    this.caseID = 0;
    // Array of Evidence attributes
    this.attributes = [];
    this.actions = [];
    this.attributesCollapsed = false;
    this.attributesHeight = 0;
  },
  
  /**
   * Last life cycle method called before rendering.
   * Sets up properties needed by the template.
   */
  postMixInProperties: function() {
       this.bundle = new ResourceBundle("IncomingEvidenceComparison");
       this.actionButtonText = this.bundle.getProperty('EvidenceViewer.Button.Actions');
	   this.editButtonText = this.bundle.getProperty('EvidenceViewer.Button.Edit');
       this.deleteExistingButtonText = this.bundle.getProperty('EvidenceViewer.Button.DeleteExisting');
	   this.evidenceToggleText = this.bundle.getProperty('EvidenceViewer.Button.Toggle', [this.name]);

	   // Create evidence label to display next to evidence name
       // Use effective date if it exists, else use the evidence start date
       if (this.effectiveFrom) {
	      var effectiveDateFormatted = curam.date.formatDate((this._getDate(this.startDate)), jsDF);
	      this.evidenceDateText = this.bundle.getProperty('EvidenceViewer.Label.ChangedOn', [effectiveDateFormatted]);
	   } else {
		 var startDateFormatted = curam.date.formatDate((this._getDate(this.startDate)), jsDF);
	     this.evidenceDateText = this.bundle.getProperty('EvidenceViewer.Label.StartedOn', [startDateFormatted]);
	   
	   }
   },
   
   postCreate: function() {
	 this.inherited(arguments);
     var _self = this;
  	 var editActionExists = false;
	
     if (this.type==="Incoming") {

		// Check if Edit action exists on evidence
     	this.actions.forEach(function(evidenceAction){
			if(evidenceAction.action === "Edit" && !evidenceAction.disabledIndOpt){
				editActionExists = true;
				
				// Add click event listener to Edit button
				_self._addEditButtonEventListener(evidenceAction.action);
			}
		});
	  
		// Disable the Edit button if doesn't exist on actions list
      	if (!editActionExists) {
 		  this.editNode.disabled = true;
		}
		
		// Hide the 'Deleted' description tag on Incoming evidence
		if (!this.pendingRemovalInd) {
	  		domStyle.set(this.pendingRemovalDesc, 'display', 'none');
		}
		
		// Hide the evidence level action button for Incoming evidence
		domStyle.set(this.deleteExistingNode, 'display', 'none')

	} else {
		// Hide the evidence level action buttons for Existing evidence
		domStyle.set(this.editNode, 'display', 'none');
		
		// Hide the 'Pending Deletion' tag on existing evidence
		// if flag is set to false and not evidence not in pending 
		// deletion status
		if (!this.pendingRemovalInd) {
	  		domStyle.set(this.pendingRemovalDesc, 'display', 'none');
		} else if (!this.pendingDeletionDescIndOpt){
			domStyle.set(this.pendingRemovalDesc, 'display', 'none');
		}
		
		// Delete Existing button
        // Check if Delete Existing action exists on evidence
        this.actions.forEach(function(evidenceAction){
	      if (evidenceAction.action === "RemoveExisting") {
		    if (evidenceAction.disabledIndOpt) {
		      _self.deleteExistingNode.disabled = true;
		    } else {
			  // Add click event listener to Delete existing button
			  _self._addDeleteButtonEventListener(evidenceAction.action);
		    }
		  }
	    });
	  }

      if (this.pendingRemovalInd) {
		//Remove the start/effective date description
		domStyle.set(this.evidenceDateTextNode, 'display', 'none');
		
		if (this.type === "Incoming") {
			this.pendingRemovalDesc.innerText = this.bundle.getProperty('EvidenceViewer.Description.Deleted');
	    } else {
			var content = this.bundle.getProperty('EvidenceViewer.Description.PendingDeletion');
			var pendingRemovalDescNode = this.pendingRemovalDesc;
			setTimeout(function(){pendingRemovalDescNode.innerHTML = content;}, 10);
		}
	  } else {
		// Hide the deleted/pending removal description
	    this.pendingRemovalText = "";
	  }
      
   },
   
   startup: function() {
	   this.inherited(arguments);
	   var _self = this;
	   this.attributes && this.attributes.forEach(function(attribute){
	       var attributeViewer = new EvidenceAttributeViewer(attribute);
	       _self.addChild(attributeViewer);
	     });
	   
	  this.attributesHeight =  this.containerNode.offsetHeight;
   },
   
   /**
    * Returns an array of Attribute Viewers within this.
    */
   getAttributeViewers: function() {
	   return this.getChildren();
   },
   
   /**
    * Toggles displaying the Evidence attributes.
    */
   _toggleAttributeCollapse: function() {
     
     if(this.attributesCollapsed){
       this.containerNode.style.maxHeight = this.attributesHeight + "px";
       this.collapseNode.classList.remove("evidence-viewer__collapse-collapsed");
       this.collapseNode.setAttribute("aria-expanded", true);
     } else {
       this.collapseNode.classList.add("evidence-viewer__collapse-collapsed");
       this.containerNode.style.maxHeight = "0";
       this.collapseNode.setAttribute("aria-expanded", false);
     }
     this.attributesCollapsed = !this.attributesCollapsed;
   },
   
   /**
    * Emits heightupdated event when the Attributes containerNode's tranisition ends.
    */
   _handleAttributeTransitionEnd: function(){
	 this.emit('heightupdated', {bubbles: true});
   },

  /**
   * Adds an click & enter/space key down event listeners to the Edit button. 
   * The event opens the edit modal for the evidence.
   * @param evidence evidence object to edit.
   */
  _addEditButtonEventListener: function(event) {
    var _self = this;
    on(_self.editNode, 'click, keydown', function(e) {
	  if(e.type === 'keydown' && (e.keyCode === 32 || e.keyCode === 13) || e.type === 'click') {
	    _self.emit(event, {bubbles: true, detail: _self.params});
	   }  
    })
  },

  /**
   * Adds an click & enter/space key down event listeners to the Delete Existing button. 
   * The event opens the Delete Existing modal for the evidence.
   * @param evidence evidence object to delete.
   */
  _addDeleteButtonEventListener: function(event) {
    var _self = this;
    on(_self.deleteExistingNode, 'click, keydown', function(e) {
	  if(e.type === 'keydown' && (e.keyCode === 32 || e.keyCode === 13) || e.type === 'click') {
	    _self.emit(event, {bubbles: true, detail: _self.params});
	   }  
    })
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
     }
  });
});