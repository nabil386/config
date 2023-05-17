<script>

<jsp:scriptlet>
	//This is JSP code that generates Javascript code.
	//It is initializing a JS variable based on the value of a request header. 
	String refererHeaderValue=request.getHeader("Referer");
	out.println("var refererHeaderValue = \"" + refererHeaderValue + "\";");
</jsp:scriptlet>


function signalCommit(event) {
  dojo.stopEvent(dojo.fixEvent(event));
  var commitBtn = curam.util.getTopmostWindow().document.getElementById("commitDocChangesBtnIE");
  document.WordApplet.interimSave();
  dojo.addClass(commitBtn, "disabled-button");
  if (commitBtn.removeEventListener) {
    commitBtn.removeEventListener('click', signalCommit);
  } else {
    commitBtn.detachEvent('onclick', signalCommit);
  }
}
function prepareUnload() {
  //empty for applet based control
}
dojo.addOnLoad(function() {
  require(['dojo/dom-style'],
    function(domStyle)  {
      domStyle.set('commitDocChangesBtnIE', 'display', '');
      if (getConfig('fileedit.confirmation') == "false") {
        fnSavePrompt = function() {
          return (getConfig('fileedit.autoconfirm') == "true");
        }
      }

  });
  var localSaveListener =
  dojo.subscribe("curam/fileedit/localsave",
    function() {
      var commitBtn = curam.util.getTopmostWindow().document.getElementById("commitDocChangesBtnIE");
      dojo.removeClass(commitBtn, "disabled-button");
      if (commitBtn.addEventListener) {
	    commitBtn.addEventListener('click', signalCommit);
	  } else {
	    commitBtn.attachEvent('onclick', signalCommit);
	  }
  });
  var docCloseListener = 
  dojo.subscribe("curam/fileedit/docclose", function() {
    var delay = setInterval(function() {
      clearInterval(delay);
      lsRef = dojo.unsubscribe(localSaveListener);
      dcRef = dojo.unsubscribe(docCloseListener);
      lsRef = dcRef = null;
      var commitBtn = curam.util.getTopmostWindow().document.getElementById("commitDocChangesBtnIE");
      if (commitBtn.removeEventListener) {
        commitBtn.removeEventListener('click', signalCommit);
      } else {
        commitBtn.detachEvent('onclick', signalCommit);
      }
      if (document.WordApplet) {
        try {
          if (fnSavePrompt()) {
            document.WordApplet.finalCommit();
          } else {
            if (document.WordApplet) {
              document.WordApplet.wrapUp();
            }
          }
          fnSavePrompt = function() { return false; }
        } catch (e) {
          console.warn(e.message);
        }        
      }
      return false;
    },100);
  });
  dojo.subscribe("/curam/clip/selected", function(text) {
    document.WordApplet.insertText(text);
    if (fedRefs.searchPageRef) {
      var doc = fedRefs.searchPageRef.iframe.contentDocument;
      var list = doc.getElementById('error-messages');
      if (!list) {
        var holder = doc.getElementById('error-messages-container');
        list = doc.createElement('ul');
        list.id = 'error-messages';
        list.className = 'messages';
        holder.appendChild(list);
     }
     var txtDiv = doc.createElement('div');
     txtDiv.innerHTML = getMessage("Msg.Pasted.Val") + text;
     var item = doc.createElement('li');
     item.className = "level-2";
     item.appendChild(txtDiv);
     list.appendChild(item);
  }
});

});
</script>
<div id="content" class="title-exists" tabIndex="-1"> <jsp:scriptlet>curam.util.client.jsp.FileEditUtil.outputFileEditWidget(pageContext, "ie");</jsp:scriptlet> <div id="wraperDiv" style="margin-top:15px;margin-left:15px; width:560px;height:230px;"> <script>
	      var attributes = {id:'WordApplet', name:'WordApplet',
	                      code:'curam.util.tools.fileedit.applet.FileEditApplet.class',
	                      codebase:'../applet-lib',
	                      archive:'WordIntegrationApplet.jar, jacob-1.20.jar, commons-codec-1.14.jar',
	                      width:560, height:230};
	      var tkn = curam.dialog.getParentWindow(window).getToken();
	      dojo.mixin(wfeParams, { t: tkn, r: refererHeaderValue, codebase_lookup:'false', separate_jvm: 'true', agent: curam.util._getBrowserName()});
	      deployJava.runApplet(attributes, wfeParams, '1.7');
	    </script> </div> </div>