/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * PID 5725-H26
 *
 * Copyright IBM Corporation 2014,2023
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the US 
 * Copyright Office
 *
 * Copyright 2010 Curam Software Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Curam
 * Software, Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Curam Software.
 */

/* Modification History
 * ====================
 * 19-Jan-2023 COC [SPM-126270] Renamed FormEventsAPI to CuramFormsAPI.
 * 16 Jan 2023 AT [SPM-126131] CKEdtor Read-only configuration - Accessibility updates.
 * 29 Oct 2022 AT [SPM-125885] CKEdtor Read-only configuration.
 * 27 Oct 2022 AT [SPM-125827]  Added initializeAndRegisterWithFormEventsAPI to register Rich Text Editor with FormEventsAPI.
 * 1 Nov 2010 PDN   [CR00224597]  Created to support the Rich Text Editor.
 */

/**
 * This file provides javascript functionality that is required by the Rich Text
 * Editor Renderer.
 */

/**
 * The initialize method instantiate's a rich text editor by replacing an
 * existing field specified with a target id.
 * 
 * @param targetID the id of the field that will be replaced by the editor.
 * @param configFile the name of the CKEditor config file.
 * @param height the height of the editors content area in pixels.
 * @param width the width of the editor widget in pixels. 
 * @param focusCKE boolean indicating whether focus should be on CKEditor when loading the page.
 * 
 * @return editor CKEditor.editor instance.
 */
 function initialize(targetID, configFile, height, width, focusCKE){
	 
	let editor = CKEDITOR.replace(targetID, {customConfig : '../ckeditor_settings/' + 
		configFile, height: height, width: width, startupFocus : focusCKE});

	// The editor is set to read-only by specifying the configuration file curam_readonly_config.js 
	// in the CONFIG attribute of the UIM field. However, some functionality still remains which 
	// must be disabled dynamically after the editor loads.
	editor.on('instanceReady', function(){
        if (editor.config.readOnly) {
            editor.commands['print'].disable();
            editor.commands['copy'].disable();
            editor.commands['copy'].removeAllListeners();
            /**
             * the following code is an accessibility fix that applies to the contact log edit screen when the contact log is locked by another user. 
             * if other screens use the read only ck editor in the future this code will not work with them if the class does not match "cke_editor_Append".
             */
            //set the tab index on the div rather than the iframe
            document.querySelector('[class*="cke_editor_Append"]').tabIndex = 0;
            document.querySelector("#cke_1_contents > iframe").tabIndex = -1;
            //set aria disabled on the div and set a title so that the screen reader reads it out as disabled
            document.querySelector('[class*="cke_editor_Append"]').ariaDisabled = true;
            var title = document.querySelector('[id^="cke_Append"][id$="_arialbl"]').textContent;
            document.querySelector('[class*="cke_editor_Append"]').title = title;
        }
    }); 

	return editor;
}

/**
 * The initialize method instantiate's a rich text editor by replacing an
 * existing field specified with a target id then registers the editor with the CuramFormsAPI. 
 * 
 * @param targetID the id of the field that will be replaced by the editor.
 * @param configFile the name of the CKEditor config file.
 * @param height the height of the editors content area in pixels.
 * @param width the width of the editor widget in pixels. 
 * @param focusCKE boolean indicating whether focus should be on CKEditor when loading the page.
 * @param targetIDFieldPath the BOPI path of the field being rendered as a rich text 
 * editor.
 * 
 * @return editor CKEditor.editor instance.
 */
function initializeAndRegisterWithCuramFormsAPI(targetID, configFile, height, width, focusCKE, targetIDFieldPath){
	 
    let editor = initialize(targetID, configFile, height, width, focusCKE);

    require(["dojo/ready"], function(ready){
        ready(function(){
        	if (curam.util.getTopmostWindow().CURAM_FORMS_API_ENABLED === 'true') {
        		curam.util.ui.form.CuramFormsAPI.registerComponent(
        				new curam.util.ui.form.renderer.RichTextEditorFormEventsAdapter(targetID, targetIDFieldPath, editor));
        	}
        });
    });     
	
    return editor;
}

 
 