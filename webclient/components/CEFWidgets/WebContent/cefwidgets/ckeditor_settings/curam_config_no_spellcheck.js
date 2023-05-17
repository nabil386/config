/*
Copyright (c) 2003-2010 CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/* Modification History
 * ====================
 * 14 May 2015 EC  [CR00462270] Allow paste of tables from word and excel
 * 04 Jan 2013 SD   [CR00362536]  Remove 'Curam' context from config path for CSS settings.
 * 22 Aug 2012 ELG  [CR00338893]  Update related to locale setting. This update
 *                                is needed for the CKEditor localisation to work properly.
 * 13 Mar 2012 BD   [CR00310120]  Updates to facilitate the migration to the IBM
 *                                FCKEditor. Some plugins have been removed to enable
 *                                the working of the FCKEditor. TODO. Revisit the
 *                                the removal of these plugins to check if it was 
 *                                necessary.
 */
CKEDITOR.editorConfig = function( config )
{
  // Define changes to default configuration here. For example:
  // config.language = 'fr';
  // config.uiColor = '#AADC6E';
  
    config.uiColor = '#FFFFFF';
    
    // BEGIN, CR00338893, ELG
    // Set the locale
    config.language = dojo.locale;
    // END, CR00338893
    
    config.contentsCss = '../cefwidgets/ckeditor_settings/curam_contents.css';
    
    config.scayt_autoStartup = false;
    config.height = 130;

    config.toolbar = 'NotesToolbar';

    config.removePlugins = 'elementspath|scayt|wsc|div';
        
    config.toolbarCanCollapse = false;
    config.resize_enabled = false;
    
    //Allow paste of tables from word and excel
    config.allowedContent = true;	

    config.toolbar_NotesToolbar = 
    [
      ['Cut','Copy','Paste','PasteText','Print'],
      ['Undo','Redo','Find','Replace','SelectAll','RemoveFormat','Bold','Italic','Underline','Strike'],
      ['Subscript','Superscript','NumberedList','BulletedList','Outdent','Indent'],
      ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
      ['Link','Unlink'],
      ['Table','HorizontalRule','Smiley','SpecialChar', 'PageBreak'],
      ['TextColor','BGColor'],
      ['Maximize'],
      ['Styles','Format','Font','FontSize']
    ];
    
    config.skin = 'office2003';

    // Setup whitelist plugin if it is enabled
    if (enableWhitelistPlugin === 'true') {
     config.extraPlugins+=',whitelist'; 
     config.entities = false;
    }

}    