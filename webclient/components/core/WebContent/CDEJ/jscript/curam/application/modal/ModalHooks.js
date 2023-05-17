/*
 *  IBM Confidential
 *  
 *  OCO Source Materials
 *  
 *  Copyright IBM Corporation 2022.
 *  
 *  The source code for this program is not published or otherwise divested
 *  of its trade secrets, irrespective of what has been deposited with the 
 *  US Copyright Office
 */

/**
 * @name curam.application.modal.ModalHooks
 * @namespace Provides the ability to create hook points in
 * modal dialogs.
 */
define(["curam/define"], function(define) {
 
define.singleton("curam.application.modal.ModalHooks",

{
  /**
   * Enables draggable modal dialogs across the application.  
   *
   * @param {Node} modalRoot The root node of a modal dialog.
   *
   */
  enableDraggableModals: function(modalRoot) {
    // console.log("Drggable modals implementation");

    // The 'modalRoot' node can be updated to make modal dialogs across the application
    // draggable. 
    // One of the ways to do this, is by setting the style attribute on the node,
    // with 'top' and 'left' CSS properties, as the user drags the modal.
  }
});

return curam.application.modal.ModalHooks;
});
