define(["dojo/_base/declare", "idx/widget/Menu"],
       function(declare, IdxMenu) {

  /**
   * The idx/widget/Menu is used in our "MegaMenu" using its "column layout"
   * feature (See http://idx.ibm.com/v1.4/jsDocs/symbols/idx.widget.Menu.html).
   * 
   * However, when this is used, the left and right arrow keys don't work and
   * instead trigger JavaScript errors. The up and down arrows work correctly. 
   * 
   * This class provides a "quick" fix by sub-classing idx/widget/Menu and
   * having the right and left arrow keys focus on the next and previous items
   * respectively. Essentially they behave the same as the up and down keys.
   * 
   * With the column layout, the left and right keys should move across the
   * columns, but given time constraints that is not achievable right now. 
   * 
   * Note that the method overrides below are not from IDX. They are from
   * "dijit/DropDownMenu" which the "idx/widget/Menu" depends on. Therefore,
   * this quick fix is susceptible to changes in Dojo or the IDX menu itself.
   * However, because keyboard navigation is broken we fail our
   * accessibility\VPAT requirements and therefore this fix has to be made. 
   */
  return declare("curam.widget.menu.MegaMenu", [IdxMenu], {
    
    _onRightArrow: function() {
      this.focusNext();
    },

    _onLeftArrow: function() {
      this.focusPrev();
    }
    
  });
})
