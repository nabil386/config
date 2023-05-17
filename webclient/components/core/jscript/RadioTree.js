/* 
 * Licensed Materials - Property of IBM
 * 
 * Copyright IBM Corporation 2012,2013. All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/** Modifications
 * ==============
 * 04-Oct-2012 BD [CR00345902] Upgrade code for Dojo 1.7
 *
 */
dojo.provide("radioTree.RadioTree");

require(["dijit/Tree","dijit/form/RadioButton"]);

dojo.declare("radioTree._RadioTreeNode", dijit._TreeNode, {
_radioId:'',

constructor: function(arguments, radioId){
	this.radioId=radioId;
	},

	
postCreate: function(){
    this.createRadio();
    this.inherited(arguments);
    
},

createRadio: function(){
    if (this.item.name != undefined) {
        dojo.place("<input type=radio name='"+this.radioId+"' value='"+ this.item.value+"' />", this.expandoNode, 'after');
    }
}
	
});

dojo.declare("radioTree.RadioTree", dijit.Tree, {
	radioId:'',
	postCreate: function(){
    this.inherited(arguments);
},
_createTreeNode: function(args){

    return new radioTree._RadioTreeNode(args, this.radioId);
},
_onClick: function(/*TreeNode*/nodeWidget, /*Event*/ e){
    var domElement = e.target;
    
    if (domElement.type != 'radio') {
        return this.inherited(arguments);
    }
    dojo.publish("cef/RadioTree/selected", [nodeWidget.item.value]);
}

});
