/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2022
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */
/* Modification History
 * ====================
 * 20 Oct 2022 AT [SPM-125885] CKEditor read-only option
 */


/*
 * This function defines the default configuration for the rich text editor, it 
 * is a copy of curam_config.js adding in the read only option as the last line.
 * 
 * 
 * IMPORTANT
 * =========
 * 
 * Aside from the read-only configuration, this file should be in-sync with curam_config.js.
 * 
 */

CKEDITOR.editorConfig = function( config )
{
    
    // BEGIN, CR00338893, ELG
    // Set the locale
    config.language = dojo.locale;
    // END, CR00338893
        
    // The css file this is used for the text area
    config.contentsCss = '../cefwidgets/ckeditor_settings/curam_contents.css';
    
    // The spell check as you type function
    config.scayt_autoStartup = false;

    //config.removePlugins = 'elementspath|scayt|wsc|div';
    //Begin WI 177399
    config.removePlugins = 'iframe,ibmpasteiframe';
    //END WI 177399
        
    config.toolbarCanCollapse = false;
    config.resize_enabled = false;
    
    // Allow paste of tables from word and excel
    config.allowedContent = true;
    // BEGIN, 251087, SH
    // Allow paste of styles from word
    config.pasteFromWordRemoveStyles=false;
    config.pasteFromWordRemoveFontStyles = false;
    // END, 251087
        
    // Define the Toolbar and its items
    config.toolbar = 'CEFToolbar';
    
    // Define the styling of the widget
    config.skin = 'curamv4,../../cefwidgets/ckeditor_settings/skins/curamv4/';

    //v.4 requires this setting
    config.extraAllowedContent='';
    
    config.autoGrow_maxHeight='260px';
     
    if (enableSpellcheckPlugin === 'true') {
     config.toolbar_CEFToolbar = 
            [
                ['Cut','Copy','Paste','PasteText','Print'],
                ['Link','Unlink'],
                ['Undo','Redo','SelectAll','RemoveFormat','Bold','Italic','Underline'],
                ['TextColor','BGColor'],
                ['Subscript','Superscript','NumberedList','BulletedList','Outdent','Indent'],
            ['Font','FontSize','LangOption','IbmSpellChecker']
            ];
    
        //Lotus Spell Checker config.
        config.extraPlugins += ',ibmcustomdialogs,ibmspellchecker,langoption,autogrow';
        config.ibmSpellChecker = {
            service:'XTAF',
            restUrl:'/spellbridge/spellcheck/rest/spell',
            lang:spellcheckDefaultLocale[0],
            suggestions:'5',
            format:'json',
            highlight: { element : 'span', styles : { 'background-color' : 'yellow', 'color' : 'black' } },
            preventCache: true,
            maxLength: 100
        };

        // This will disable browser spell checker
        config.disableNativeSpellChecker=true;
    } else {
     config.toolbar_CEFToolbar = 
        [
            ['Cut','Copy','Paste','PasteText','Print'],
            ['Link','Unlink'],
            ['Undo','Redo','SelectAll','RemoveFormat','Bold','Italic','Underline'],
            ['TextColor','BGColor'],
            ['Subscript','Superscript','NumberedList','BulletedList','Outdent','Indent'],
            ['Font','FontSize']
        ];
        config.extraPlugins += ',ibmcustomdialogs,autogrow';

        // This will enable browser spell checker
        config.disableNativeSpellChecker=false;
     }
     if (isBidi == 'true') {
        var bidiButtons = ['BidiLtr','BidiRtl'];
        config.toolbar_CEFToolbar[4] = config.toolbar_CEFToolbar[4].concat (bidiButtons);
        config.extraPlugins += ',ibmbidi';
     }
     
   // Setup whitelist plugin if it is enabled
   if (enableWhitelistPlugin === 'true') {
     config.extraPlugins+=',whitelist'; 
     config.entities = false;
   }
   // BEGIN, SPM-125885, AT
   config.readOnly = true;
   // END, SPM-125885, AT
};
