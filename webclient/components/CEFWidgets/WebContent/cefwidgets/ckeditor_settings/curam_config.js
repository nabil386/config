/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corporation 2014,2022
 *
 * The source code for this program is not published or otherwise divested of
 * its trade secrets, irrespective of what has been deposited with the US
 * Copyright Office.
 */
/* Modification History
 * ====================
 * 20 Oct 2022 AT [SPM-125885] Comments added to warn of need to sync changes with curam_readonly_config.js
 * 20 May 2021 ZV [RTC270924] Add styles customisation
 * 22 Nov 2019 SH [RTC251087] Allow pasting of MSWord styles
 * 08 Dec 2016 SD [177399] Removed the iframe and ibmpasteiframe to prevent iframe based XSS issues
 * 14 Sep 2016 AB [162522] Change to v7 skin, move language select and
                           spellchecker icon to same line as font & font size.
 * 14 May 2015 EC  [CR00462270] Allow paste of tables from word and excel
 * 1 May 2015 DG  [CR00462247] Allow paste of tables from word and excel
 * 3 Dec 2014 JAY [CR00455239] Adapt configurations to CKEditor v.4.4.5
 * 9 Sept 2013 DM [CR00395981]   Provide the ability to select the spellcheck language 
 								 by adding a new 'LangOption' custom plugin and populating it
 								 with values coming from a codetable.
 * 29 May 2013 DM [CR00384800]   The 'lotusspellchecker' plugin has been enabled within CKEditor
 *
 * 19 Apr 2013 SD   [CR00381444]  The 'Curam' context has been removed from the
 *                                 skin settings.
 * 04 Jan 2013 SD   [CR00362536]  Remove 'Curam' context from config path for CSS settings.
 * 22 Aug 2012 ELG  [CR00338893]  Update related to locale setting. This update
 *                                is needed for the CKEditor localisation to work properly.
 * 21 Jun 2012 DG   [CR00329440]  Update to set the locale.
 * 13 Mar 2012 BD   [CR00310120]  Updates to facilitate the migration to the IBM
 *                                FCKEditor. Some plugins have been removed to enable
 *                                the working of the FCKEditor. TODO. Revisit the
 *                                the removal of these plugins to check if it was 
 *                                necessary.
 * 01 Nov 2010 PDN  [CR00224597]  Created for the CKEditor Rich Text Editor.
 */


/*
 * This function defines the default configuration for the rich text editor
 * 
 * 
 * IMPORTANT
 * =========
 * 
 * Aside from the read-only configuration, this file should be in-sync with curam_readonly_config.js.
 * 
 * 
 */
CKEDITOR.editorConfig = function( config )
{
    //config.uiColor = '#FFFFFF';
    
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
} ;