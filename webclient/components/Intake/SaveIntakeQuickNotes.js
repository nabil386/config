/*Z*//* Copyright 2009-2010 Curam Software Ltd. All rights reserved. */
dojo.require("curam.util.UimDialog");
dojo.require("curam.tab");

var quicknotes = {

  /*
   * Function to save a quick note. Takes the page that will do the actual save, the type of note,
   * the related id and  a reference to the id of the html fields containing the data.
   */
  save:function(uimID,noteType, subjectTextID,EditorID, relatedID) {

    var subject = dojo.byId(subjectTextID).value;

    if (subject.length == 0) {
      var url = "QuickNoteNoSubject_errorPage.do?o3frame=modal";
      quicknotes.constructAndOpenModalURL(url);

      return;
    }

    /* Get the CKEditor instance and retrieve the data */
    var editorIDContent = CKEDITOR.instances[EditorID].getData();

    /* Synchronous post to the specified uim page which will do the actual save */
    dojo.xhrPost({
     url: uimID,
     sync: true,
     content: {Param0: noteType, Param1: subject, Param2: editorIDContent, Param3: relatedID},
     mimetype: "text/json",
     load: function(data) {
       /* Clear the fields if the post was successful */
       quicknotes.reset(subjectTextID, EditorID);
     },
     error: function(error) {
       /* Pop up an error if the post failed */
      var url = "QuickNote_errorPage.do?o3frame=modal";
      curam.util.UimDialog.openUrl(url);
     }
    });
  },

  /* Function to clear the data entry fields */
  reset:function(subjectTextID, EditorID){
   dojo.byId(subjectTextID).value="";
   CKEDITOR.instances[EditorID].setData("", null);
  },

   /* This function was introduced in 6.0 SP1 EP1 but is now redundant 
     please use the following function curam.util.UimDialog.openUrl()*/
     
  /* Constructs a modal url with a return parameter of the 
     current page in the content frame and then opens the url in a modal. */
  constructAndOpenModalURL:function(url) {
  
    /*
     * Get a handle to the iframe that is in the content pane
     */
    var iframe = curam.tab.getContentPanelIframe();
    
    /*
     * Get the page targeted in the href of the iframe
     */
    var rpuValue = quicknotes.getLastPathSegmentWithQueryString(
      iframe.contentWindow.location.href);
      
    /*
     * Construct the target URL from the URL passed in (the 
     * target page) with a return value of the page in the frame
     */
    var targetURL = url + "&__o3rpu="
      + encodeURIComponent(rpuValue);
    
    /*
     * Open the page in the modal dialog
     */
    curam.util.UimDialog.openUrl(targetURL);
  },
  
   /* This function was introduced in 6.0 SP1 EP1 but is now redundant 
     please use the following function curam.util.UimDialog.openUrl()*/
     
  getLastPathSegmentWithQueryString:function(url) {
    var pathAndParams = url.split("?");
    var pathComponents = pathAndParams[0].split("/");
    return pathComponents[pathComponents.length - 1]
      + (pathAndParams[1] ? "?" + pathAndParams[1] : "");
  }
  

};
