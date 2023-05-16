define(["dojo/_base/declare",
        "dojo/on",
        "dijit/_Widget",
        "dojo/dom-construct",
        "dojo/dom-geometry",
        "dojo/dom-style",
        "dojo/dom-class",
        "dojo/dom-attr"],
        function(declare,
                        on,
                        _Widget,
                        domConstruct,
                        domGeom,
                        domStyle,
                        domClass,
                        domAttr) {
        return declare("curam.widget.componentWrappers.ListWraper",[_Widget], 
{
                baseClass : "navMenu",
                
                
                _listTypeUnordered : "ul",
                _listTypeOrdered : "ol",
                
                listType : this._listTypeOrdered,               
                baseClass : "listWrapper",
                
                itemClass : null,
                itemStyle : null,
                
                role  : null,
                
                buildRendering : function() {
                  
                        if(this.listType == this._listTypeUnordered)
                        {
                                this.domNode = domConstruct.create("ul");
                        }
                        else
                        {
                                this.domNode = domConstruct.create("ol");
                        }
                        
                        if(this.role != null)
                        {
                                domAttr.set(this.domNode, "role", this.role);
                                domAttr.set(this.domNode, "aria-label", "NavMenu");
                        }

                        this.inherited(arguments);
                },
                
                _setItemAttr : function(item, possion)
                {
                        if(possion == null)
                        {
                                possion = "last";
                        }
                                
                        
                        var listItem = domConstruct.create("li", null, this.domNode, possion);
                        
                        this._doBeforeItemSet(item, listItem);
                        
                        domConstruct.place(item.domNode ? item.domNode : item, listItem);
                        
                        this._doAfterItemSet(item, listItem);

                        if(this.itemStyle){
                                domStyle.set(listItem, this.itemStyle);
                        }
                        
                        if(this.itemClass){
                                domClass.add(listItem, this.itemClass);
                        }
                },
                
                _doBeforeItemSet : function(item, listItem)
                {
                        
                },
                
                _doAfterItemSet : function(item, listItem)
                {
                        
                },
                
                _getItemCountAttr : function()
                {
                        return this.domNode.children.length;
                },
                
                _getContainerHeightAttr : function()
                {
                        var container = domGeom.getContentBox(this.domNode);
                        return container.h;
                },
                
                getChildElament : function(index)
                {
                        var elament = this.domNode.childNodes[index];
                        
                        return elament;
                },
                
                placeItemToPostion : function(item, index)
                {
                        var elament = this.domNode.childNodes[index];
                        domConstruct.place(elament, item);
                },
                
                deleteChild : function(index)
                {
                        var elament = this.getChildElament(index);
                        
                        domConstruct.destroy(elament);
                },
                
                deleteAllChildern : function()
                {
                        while(this.domNode.children.length > 0)
                        {
                                this.deleteChild(0);
                        }
                }
        
        });
});