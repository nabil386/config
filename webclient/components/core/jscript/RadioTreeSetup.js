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
// BEGIN, CR00399746, MV
dojo.registerModulePath("radioTree", "../..");
// END, CR00399746
require(["dojo/data/ItemFileWriteStore","radioTree/RadioTree","dijit/tree/ForestStoreModel"]);

dojo.global.createTree = function(domLocation, jsonString, targetId){
    var store = new dojo.data.ItemFileWriteStore({
        data: jsonString
    });
    
    var model = new dijit.tree.ForestStoreModel({
        store: store,
        query : {type : 'Category'},
        rootLabel: 'Category'
    });
    
    var tree = new radioTree.RadioTree({
        model: model,
        id: "MenuTree",
        showRoot: false,
        radioId: targetId
    });
    tree.placeAt(domLocation);
    dojo.subscribe("cef/RadioTree/selected", function(storeItem) {
        //alert(storeItem + " got selected..");
       });
    
}