/*
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/* Modification History
 * ====================
 * 28 Jan 2011 MJC   Created configuration of CKEditor for use with Quick 
 * Notes.
 * 5 July 2017 AH   Change quick notes skins version from 1 to 4
 */


/*
 * This function defines the default configuration for the rich text editor
 * 
 */
CKEDITOR.editorConfig = function( config )
{
    config.uiColor = '#FFFFFF';
    
    // The css file this is used for the text area
    config.contentsCss = '/Curam/cefwidgets/ckeditor_settings/curam_contents.css';
    
    // The spell check as you type function
    config.scayt_autoStartup = false;

    config.removePlugins = 'elementspath|scayt|wsc|div';
    config.toolbarCanCollapse = false;
    config.resize_enabled = false;
     
    // Define the Toolbar and its items
    config.toolbar = 'CEFToolbar';

    config.toolbar_CEFToolbar =     
    [
        ['Cut', 'Copy', 'Paste', 'Bold', 'Italic', 'Underline', 'TextColor'] 
    ];
    
    // Define the styling of the widget
    config.skin = 'curamv4,/Curam/cefwidgets/ckeditor_settings/skins/curamv4/';
    
    // Define locale
    config.language = dojo.locale;
}    
//BEGIN,CR00266959,ZT
CKEDITOR.on( 'instanceReady', function( ev )
    {
        // To format the output of the CKEditor to be "\r\n" when breaking the line.
        ev.editor.dataProcessor.writer.lineBreakChars = '\r\n';      
});  
//END,CR00266959