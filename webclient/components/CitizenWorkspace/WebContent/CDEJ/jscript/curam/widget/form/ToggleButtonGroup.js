define(["dojo/_base/declare",
        "dojo/_base/connect",
        "dijit/form/ToggleButton"],
        function(declare,
        		connect,
        		toggleButton) {
	return declare("curam.widget.form.ToggleButtonGroup",[toggleButton], 
{
		/**
		 * An extension of a toggle button that allows one button at a time to be 
		 * selected with in a group.
		 */
		
		_connectHandler : null,
		_unselectChannel : null,
        groupName: 'toggleButtonGroup', 
        
        postMixInProperties:function(){ 
                this.inherited(arguments); 
                this._unselectChannel = '/toggleButtonGroup%$££!|WE/' + this.groupName;
                this._connectHandler = connect.subscribe(this._unselectChannel, this, '_unselect'); 
        },
        
        /**
         * Unselect other buttons with in the group
         */
        _unselect: function(/*Object*/button) { 
                if (button !== this && this.checked) { 
                        this.set('checked', false); 
                } 
        }, 
        
        /**
         * handler function that will call the
         * internal select function
         */
        _onClick: function(e) { 
                if (this.disabled) { 
                        return false; 
                } 
                if (!this.checked) { 
                        this._select(); 
                } 
                return this.onClick(e);
        }, 
        
        /**
         * Publish to other buttons that they need
         * to unselect
         */
        _select: function() {
                dojo.publish(this._unselectChannel, [this]); 
                this.set('checked', true); 
        },
        
        /**
         * Handler that will up date the other buttons if this 
         * buttons value gets changed
         */
    	_setCheckedAttr: function(/*Boolean*/ value, /*Boolean?*/ priorityChange){
    		
    		dojo.publish(this._unselectChannel, [this]); 
			this.inherited(arguments);
    	},
		
		/**
		 * Clean up functions
		 */
		destroy: function() {
			try
			{
				connect.disconnect(this._connectHandler);				
			}	
	  		catch(err){
				console.error(err);
			}

			this.inherited(arguments);
		}
	});
});
