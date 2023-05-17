

var treeTable = {

  displayChildren: function(event) {

    event = dojo.fixEvent(event);
    
    // Toggle the plus and minus button
    var theImg = event.target.src;
    
    var x = theImg.split("/");
    var t = x.length-1;
    var y = x[t];
    
    if (y=='arrow_open_16x16.png')
    event.target.src = '../Images/arrow_closed_16x16.png';
    else
    event.target.src = '../Images/arrow_open_16x16.png';

    // Get the div node that has been clicked
    var node = cm.getParentByClass(event.target, "list-toggle");

    if (!node){return;}

    // Determine if this is a collapsed row
    var show = dojo.hasClass(event.target, "collapsedRow");

    // Toggle the class collapsedRow
    dojo[show ? "removeClass" : "addClass"](event.target, "collapsedRow");

    // Get the level attribute 
    var level = dojo.attr(node, "level");

    // Get the current row node
    var row = node.parentNode.parentNode;

    // Get the child of current node
    var nextRow = row.nextSibling;
    
    // While there is another row
    while (nextRow!=null) {
      
      var toggler = dojo.query("td div.has-level",nextRow)[0];
      if(toggler == null) break;
      if(dojo.hasAttr(toggler, "level")){
        var rowLevel = dojo.attr(toggler, "level");
      }

      // If the row level is greater than level then this is a child
      if (rowLevel > level) {
        // Toggle the styling on the row to hide or show it and remove  existing styling
        dojo.removeClass(nextRow, "odd");
        dojo.removeClass(nextRow, "even");
        dojo[show ? "removeClass" : "addClass"](nextRow, "collapsed-child-row");
        nextRow = nextRow.nextSibling;
      }  
      // If there are no more children then stop the loop 
      if( rowLevel == level) {
        break;
      }
      
    }
    
    treeTable.alternateColor(row);

  },


 // Function that sets up styling to alternate background colour 
 // of the rows of the list.
 alternateColor: function (row){ 
   var body = row.parentNode;
   var rows = body.getElementsByTagName("tr"); 
   
   var visibleRows=[];
   
        // Filter out the rows that are not visible
        for(i = 0; i < rows.length; i++){
         
         if(!dojo.hasClass(rows[i], "collapsed-child-row")){
           visibleRows.push(rows[i]);
         }
        }
        
        // Add appropriate css styling class to each row
        for(j = 0; j < visibleRows.length; j++){
            var currentClass = visibleRows[j].className;
            if(j % 2 == 0){ 
              dojo.removeClass(visibleRows[j], "even");
              dojo.addClass(visibleRows[j], "odd");}
            else {
               dojo.removeClass(visibleRows[j], "odd");
               dojo.addClass(visibleRows[j], "even");
            }
            
        }
    
    },

     // Function that sets up styling to do the stripes
     initializeListStripes: function(){
        var retnode = [];
        var myclass = new RegExp('\\b'+'list-toggle-child'+'\\b');
        var elem = document.getElementsByTagName('*');
        for (var i = 0; i < elem.length; i++) {
          var classes = elem[i].className;
          if (myclass.test(classes)) retnode.push(elem[i]);
        }
        
        if(!retnode){
          return;
        }
        treeTable.alternateColor(retnode[0].parentNode.parentNode);
    }
    
    
};

dojo.addOnLoad(treeTable.initializeListStripes);




