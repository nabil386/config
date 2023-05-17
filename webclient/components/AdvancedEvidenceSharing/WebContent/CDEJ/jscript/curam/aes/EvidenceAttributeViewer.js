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
        "dojo/text!./templates/EvidenceAttributeViewer.html",
		"curam/util/ResourceBundle",
        "dojo/domReady!"
        ], function(declare, 
        			_Widget, 
        			_TemplatedMixin, 
        			template,
					ResourceBundle) {
  
  return declare('curam.aes.EvidenceAttributeViewer', [ _Widget, _TemplatedMixin ], { 
    
	/**
	 * The HTML template to use for this widget.
	 */
	templateString: template,
	
	/**
	 * mandatory setter.
	 * Sets mandatory property & either adds or removes mandatory-label class.
	 */
	_setMandatoryAttr: function(mandatory){
		this._set('mandatory', mandatory);
		if (mandatory) {
			if (!this.value && Number(this.value.replace(/[^0-9\.-]+/g,""))===0) {
				this._set('missingData', true);
				this.valueContainerNode.classList.add('information-missing');
				this.bundle = new ResourceBundle("IncomingEvidenceComparison");
      			this.valueNode.innerText=this.bundle.getProperty('EvidenceViewer.Attribute.InformationMissing'); 
			}
		}
	},
	
    /**
	 * conflict setter.
	 * Sets the conflict property & either adds or removes the conflict class.
	 */
	_setConflictAttr: function(conflict){
		this._set('conflict', conflict);
		if(conflict){
			this.valueContainerNode.classList.add('evidence-attribute-viewer__value-conflict');
		} else {
			this.valueContainerNode.classList.remove('evidence-attribute-viewer__value-conflict');
		}
	},
	
	/**
	 * @constructor
	 */
	constructor: function(){
		this.inherited(arguments);
		
		// Display Name of the attribute
		this.label = "";
		// Value of the attribute
		this.value = "";
		// Flag to control mandatory indicator
		this.mandatory = false;
		// Flag to control missing mandatory data indicator
		this.missingData = false;
		// Flag to control conflict highlighting
		this.conflict = false;
	},
	
	 /**
	   * Last life cycle method called before rendering.
	   * Sets up properties needed by the template.
	   */
	  postMixInProperties: function() {
	    this.bundle = new ResourceBundle("IncomingEvidenceComparison");
	    this.conflictIconTitle = this.bundle.getProperty('EvidenceViewer.Icon.ConflictTitle');  
	}
  });
});