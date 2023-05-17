/*
 * Merative Confidential
 * Merative US L.P. 2022,2023.
 *
 */

  /*
   * Modification History
   * --------------------
   * 19-Jan-2023  COC [SPM-126270] Renamed FormEventsAPI to CuramFormsAPI.
   * 29-nov-2022  AT [SPM-126127] Remove onchange suppression when setting data.
   * 1-nov-2022   AT [SPM-125827] Initial version, FormEventsAPI integration.
   */

/**
 * This file provides javascript functionality that is required by the Rich Text
 * Editor Renderer.
 */

/** CuramFormsAPI Integration */

define("curam/util/ui/form/renderer/RichTextEditorFormEventsAdapter", ["dojo/_base/declare", "curam/util/ui/form/renderer/GenericRendererFormEventsAdapter"], function(declare, GenericRendererFormEventsAdapter) {


  /**
   * @namespace Functions specific to the Rich Text Editor integration with CuramFormsAPI.
   */


  var RichTextEditorFormEventsAdapter = declare("curam.util.ui.form.renderer.RichTextEditorFormEventsAdapter", GenericRendererFormEventsAdapter,
      /** @lends curam.util.ui.form.renderer.RichTextEditorFormEventsAdapter */ {

	  // The instance of the CKEditor that is integrating with the CuramFormsAPI.
      editor: "",

      /**
       * Constructor
       * 
       * @param id form element ID (see GenericRendererFormEventsAdapter)
       * @param pathID bopi path (see GenericRendererFormEventsAdapter)
       * @param editor instance of the CKEditor
       */
      constructor: function (id, pathID, editor) {
        this.editor = editor;
      },   

      /**
       * Invoke callbackForOnChangeEvent when the CKEditor content changes.
       * 
       * @param callbackForOnChangeEvent Callback to invoke on change.
       * 
       */
      addChangeListener : function ( callbackForOnChangeEvent) {
        this.editor.on('change', function(event) {        

          event.target = {
            id : this.element.getId()        		  
          };
          
          event.currentTarget = {
            value : this.getData()
          };

          event.stopPropagation = function() {
            event.stop();
          };
          event.preventDefault = function() {
            event.cancel();
          };
          
          callbackForOnChangeEvent(event);  
        }); 
      },  

      /**
       * @return the content of the CKEditor.
       */
      getFormElementValue : function () {
        return this.editor.getData();  
      },

      /**
       * Set the content of the CKEditor.
       * 
       * This will trigger the change event, not strictly necessary but decided to be safer than trying to suppress.
       * 
       * @param value to set
       */
      setFormElementValue : function (value) {
        this.editor.setData(value);      
      } 

    });

  return RichTextEditorFormEventsAdapter;
});