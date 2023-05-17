<jsp:directive.page import="curam.omega3.util.CDEJResources"/>
<jsp:directive.page import="curam.util.common.util.JavaScriptEscaper"/>
<jsp:scriptlet> pageContext.setAttribute("initMessage", CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Msg.Init.Session")); pageContext.setAttribute("extnFailed", CDEJResources.getProperty("curam.omega3.i18n.FileEdit", "Msg.No.Extension"));
</jsp:scriptlet>
<style>
  #paster {
    border: none;
    background: white;
  }
</style>
<script>
require(['dojo/dom','dojo/dom-attr','dojo/dom-style','dojo/dom-class','dojo/dom-construct', 'dojo/request/xhr'],
	    (dom, attr, style, domClass, htmlGen, xhr) => {

	var baseUrl = jsBaseURL +'/servlet/FileEdit';
	var fSystem, doArgs = null;
	var readyCmd = {type:'PAGE_2_EXTN', cmd:'DATA_READY'};
	var quitCmd = {type: 'PAGE_2_EXTN', cmd: 'QUIT_ACTION'};
    var immediateMessages = {}, immediateMessagesFallback = {};
	var errorMessages = [];
	var wordHasQuit;
  	const userAgentStr=window.navigator.userAgent;
  	const userAgentStrMatchesEdge=userAgentStr.match('Edg/.*?');
  	const browserType=(userAgentStrMatchesEdge!=null && userAgentStrMatchesEdge.length>=1 ? 'EDGE_CHROMIUM':'CHROME');
  	const fContents = 'wia_00001.curamdoc',
	        fDtls = 'wia_00001.dtls',
	          commitID = 'commitDocChangesBtnIE';
	const keys = ['Word.Status.Init.Notification',
	              'Word.Status.Save.Notification',
	              'Window.WFE.Doc.Title',
	              'fileedit.log.on',
	              'fileedit.taskbar.messages',
	              'client.browser'];
	const fallbackVals = ['WordInitialized', 'DocumentSaved',
	                      'DocumentTitle', 'false', '#',browserType];
	
	const display = (stl, msg) => {
	  console.log('FECP: ' + stl + ': ' + msg);
      
	  let sDisplay = dom.byId('statusDisplay');
      let order = sDisplay.getElementsByTagName('div')[0]
  		                 ? 'first':'last';
	  // store errors to prevent modal from closing if errors exist.					   
	  if (stl == 'ECL') {
		if (!wordHasQuit || (wordHasQuit && msg !== getMessage('Msg.Generic.Error'))) {
			errorMessages.push(msg);
		} 
	  } 
	  htmlGen.create('div', {'class':'record '+stl,innerHTML:msg},
  								         sDisplay, order);
    },
    doubleGet = mKey => {
	  let first = getConfig(mKey);
      return (first==='#') ? getMessage(mKey) : first;
	},
	report = fKey => {
		  immediateMessages && immediateMessages['fileedit.log.on']
					      ? display('ECL', getMessage(fKey))
					      : display('ECL', getMessage('Msg.Generic.Error'));
    },
    getXArgs = (tk,pReq,cmd,sData) => {
      let fObj = {},
        heads = {headers:{'X-token': tk}},
         cm = 'c=' + cmd + pReq;
      if (sData) {
    	fObj['data'] = cm + "&contents=" + sData;
    	fObj['sync'] = true;
    	heads.headers['Content-Type']='application/x-www-form-urlencoded;charset=UTF-8';
      } else {
        fObj['query'] = cm;
        fObj['preventCache'] = true;
        fObj['handleAs']='arraybuffer';
      }
      return dojo.mixin(heads, fObj);
    },
    prepArgs = (tk, pReq) => { return (cmd, sData) => getXArgs(tk, pReq, cmd, sData) },
	signalCommit = e => {
	  dojo.stopEvent(dojo.fixEvent(e));
	  // clear any previous error messages stored at this point
	  errorMessages.length = 0;
      let cBtn = curam.util.getTopmostWindow().document.getElementById(commitID);
      readAndCommitDoc(false);
      domClass.add(cBtn, 'disabled-button')
      cBtn.removeEventListener('click', signalCommit);
    },
   /**
	* Gets the timestamp for an event. The bridge expects the timestamp to be the number of milliseconds since the epoch.
	*
	* If no Dojo, use Date.now().
	* If Chrome version < 49, use event timestamp.
	* If Chrome version >= 49, use performance infrastructure to calculate timestamp rounded to the nearest millisecond.
	*/
	getTimestamp = e => {
      let start = dojo ? (dojo.isChrome > 49 ? performance.timing.navigationStart : 0.01) : false;
      return start ? Math.round(start + e.timeStamp) : Date.now();
	},
    getContents = _ => {
	  xhr.get(baseUrl, doArgs('ct',false))
	      .then(data =>{
	         let byteArray = new Uint8Array(data);
             let blob = new Blob(byteArray, {type:'application/octet-stream'});
             writeDoc(byteArray);
	       })
	      .otherwise(err => report('Log.Write.Failure'));
    },	
    getDetails = _ => {
	  xhr.get(baseUrl, doArgs('dt',false))
		   .then(data => writeDetails(data))
	       .otherwise(err=>report('Log.Write.Failure'));
   },
   commitDoc = (encodedDocData, lastCommit) => {
     xhr.post(baseUrl, doArgs('tg',encodedDocData))
      .then(respData => {
	    /COMMIT_SUCCESS/.test(respData)
	        ? display('ICL', getMessage('Msg.Save.Success'))
            : report('Log.Save.Failed');
		if (lastCommit) {
			removeDoc(fContents, true);
		} else {
			let cBtn = curam.util.getTopmostWindow().document.getElementById(commitID);
      		domClass.remove(cBtn, 'disabled-button')
      		cBtn.addEventListener('click', signalCommit);
		}
      }, function(err){
		console.log('FECP: Servlet error ' + err.response.status + ': ' + err.message);
		report('Log.Save.Failed');
      });
    },
    writeDetails = (fData) => {
	  window.webkitRequestFileSystem(window.PERMANENT, 640000,
        fs => {
		  window.fSystem = fs.root; 
          window.fSystem.getFile(fDtls, {create:true}, fEntry => {
            fEntry.createWriter(
			  fWriter => {
			    fWriter.onwriteend = e => {
			      readyCmd = dojo.mixin(readyCmd, {'tst1': getTimestamp(e), 'tsz1':e.total});
			      getContents();
			    };
			    fWriter.onerror = err => report('Failed to write details');
			    let blob = new Blob([fData], {type:'text/plain'});
			    fWriter.write(blob);
			  },
			  err => report('Failed to get writer for details'));
          });
        },
        err => report('File System Failed! ' + err.toString()));
    },
    writeDoc = fData => {
	  window.fSystem.getFile(
	    fContents,
	    {create:true},
        fEntry => {
          fEntry.createWriter(
            fWriter => {
			  fWriter.onwriteend = 
				  e => {
				    window.postMessage(dojo.mixin(readyCmd, {'tst2': getTimestamp(e), 'tsz2':e.total}), '*');
				  }
			  fWriter.onerror = e => report('Failed to write contents');
			  let blob = new Blob([fData], {type:'text/plain'});
			  fWriter.write(blob);
			},
			err => report('Failed to get writer for contents'));
        }); 
    },
    readAndCommitDoc = lastCommit => {
      window.fSystem.getFile(
        fContents, {},
		fEntry => {
		  fEntry.file(
		    f => {
			  var reader = new FileReader();
			  reader.onloadend = e => {
			    let base64Contents = btoa(reader.result);
			    let urlencContents = encodeURIComponent(base64Contents);
			    display('ICL', getMessage('Msg.Wait.Commit'));
			    commitDoc(urlencContents, lastCommit);
			  };
			  reader.readAsBinaryString(f);
			},
			err => report('Could not load the file to commit'));
         },
		 err => report('Could not read the file to commit!'));
    },
    removeDoc = (fName, lastAction) => {
      let pb = lastAction && getParent();
      window.fSystem.getFile(fName, {create: false},
        fEntry => {
          fEntry.remove(_ => {
			    if (lastAction) {
			      display('ICL', getMessage('Msg.End.Sesssion'));
				  // If we've no errors on the panel, prepare the dialog for close
				  // and then close it automatically.
				  if (errorMessages && errorMessages.length == 0) {
					pb && fedRefs.parentRef.prepareDialogClose(); 
			        curam.dialog.closeModalDialog();
				  }
				  // If we do have errors on the panel, make the close button visible
				  // and then prepare the dialog for close (this loads the underlying UIM in the background).
				  // The user will manually close the dialog. 
				  else {
					pb && fedRefs.parentRef.dojo.publish('curam/fileedit/makecloseable');
					pb && fedRefs.parentRef.prepareDialogClose(); 
				  }
			    }
			  },
			  err => pb && fedRefs.parentRef.dojo.publish('curam/fileedit/makecloseable'));
        });
   },
   cleanUp = _ => {
     window.webkitRequestFileSystem(window.PERMANENT, 640000,
       fs => {
         window.fSystem = fs.root;
         removeDoc(fContents);
         removeDoc(fDtls);
       });
	};
	
	// Send the QUIT_ACTION command when a tab or window is closed 
	window.addEventListener('beforeunload', e => {
		console.log('FECP quitSession');
		window.postMessage(quitCmd, "*");
	});

    window.addEventListener('message', e => {
      if (e.source != window) return;
	  e.stopPropagation();
	  var data;
	  if (e.data.message) {
	    // testing flow - TestCafe Hammerhead shunts the OOTB data object into data.message
	    data = e.data.message;
	  } else {
	    // production flow
	    data = e.data;
	  }
	  let {type, text} = data, p = getParent();
      if (type && (type == 'EXTN_2_PAGE')) {
        if (text == 'LOCALSAVE') {
		  dojo.publish('curam/fileedit/localsave');
		} else if (text == 'COMMIT_PROMPT') {
          let cBtn = curam.util.getTopmostWindow().document.getElementById(commitID);
          cBtn.removeEventListener('click', signalCommit);
		  wordHasQuit = true;
		  console.log('FECP: Word has exited.');
		  readAndCommitDoc(true);
        } else if (text == 'CLEARDATA') {
          p && fedRefs.parentRef.dojo.publish('curam/fileedit/cleardata');
        } else if(text == 'STOP_CLOSEABLE') {
          display('ICL',getMessage('Msg.Word.Opening'));
          p && fedRefs.parentRef.dojo.publish('curam/fileedit/stopcloseable');
          dojo.publish('curam/fileedit/enablesearch');
        } else if (text == "SESSION_STARTED") {
          removeDoc(fDtls);
        } else if (text == 'MAKE_CLOSEABLE') {
          p && fedRefs.parentRef.dojo.publish('curam/fileedit/makecloseable');
        } else if (text == 'REPORT_PASTED') {
          dojo.publish('curam/fileedit/reportpasted');
        } else if (text == 'REPORT_PASTE_FAIL') {
          dojo.publish('curam/fileedit/reportpastefail');
        }
      }
    });

	let kickOff = setInterval(
	  _ => {
        clearInterval(kickOff);

		// Callback function to execute when mutations are observed. See 276234.
		let statusDisplayCallback = function(mutationsList) {
			for(var mutation of mutationsList) {
				if (mutation.type == 'childList' && mutation.addedNodes.length) {
					// there will only be one message node added to the div at a time
					let messageNode = mutation.addedNodes[0];
					// check that the message is an error
					if (messageNode.classList.contains('ECL')) {
						// only add to the errorMessages array if Word is still active, 
						// or Word has quit but the message is genuine/valuable.
						if (!wordHasQuit || (wordHasQuit && messageNode.innerHTML !== getMessage('Msg.Generic.Error'))) {
							errorMessages.push(messageNode.innerHTML);
						} 
						else {
							// We deem these false negative errors, so hide them. 
							messageNode.style.display = 'none';
						}
					}
				}
			}
		};
		// Handle changes made to the statusDisplay div
		let observer = new MutationObserver(statusDisplayCallback);
		observer.observe(dom.byId('statusDisplay'), { childList: true });

		let checkDiv = dom.byId('checkmsg');
		let errMsg = getMessage('Msg.No.Extension');
		if (checkDiv.innerHTML.trim() == errMsg.trim()) {
		  display('ECL', errMsg);
        } else {
		  if (getParent() != null) {
            display('ICL', getMessage('Msg.Extension.Success'));
			let reqParams = Object.keys(wfeParams)
			  .filter(nm => nm != 'pNames')
			  .reduce((acc,curr)=>{
		        return acc + '&' + curr + '=' + wfeParams[curr]; 
			  },'');
			doArgs = prepArgs(fedRefs.parentRef.getToken(), reqParams);
			cleanUp();
			getDetails();
          } else {
            report('Page initialization failed!');
		  }
        }
      }, 200);
			      
    dojo.addOnLoad(function() {
	  dojo.subscribe('curam/fileedit/localsave',
        _ => {
		  let cBtn = curam.util.getTopmostWindow().document.getElementById(commitID);
		  domClass.remove(cBtn, 'disabled-button');
      });
	  //interim save support
	  dojo.subscribe('curam/fileedit/localsave',
	    _ => {
	      let cBtn = curam.util.getTopmostWindow().document.getElementById(commitID);
	      domClass.remove(cBtn, 'disabled-button');
	      cBtn.addEventListener('click', signalCommit);
	      display('ICL', getMessage('Msg.Local.Save'));
	  });
      let regularPromise = 
	    new Promise((resolve, reject) => setTimeout(_=>{        				 	  
			    	      keys.forEach(key => {
			    	       	if(key==='client.browser'){
			    	      		immediateMessages[key] = browserType;
			    	      	}else{
			    	    		let value = doubleGet(key);
			    	    		value && (immediateMessages[key] = encodeURIComponent(value));
			    	    	}		    	
			    	      });
			    	      if (Object.keys(immediateMessages).length == 6) {
			    	        resolve(immediateMessages);
			    	      }
			    	    }, 100));
       let fallbackPromise = new Promise((resolve,reject) => setTimeout(_=>{
			    	    	keys.forEach((key, idx) => immediateMessagesFallback[key] = fallbackVals[idx]);
			    	        resolve(immediateMessagesFallback);
			    	    }, 3000));
        Promise.race([fallbackPromise, regularPromise])
			    	      .then(msgs => window.postMessage({ type:'PAGE_2_EXTN', messages:msgs}, '*'))
			    	      .catch(err => console.error('Failed to initialize messages for the bridge', err.message));
	});
});

  // copy - paste support     
  dojo.subscribe('/curam/clip/selected', text => {
    var paster = dojo.byId('paster');
    if (paster) {
      paster.value = encodeURIComponent(text);
      paster.click();
    }
  });
  dojo.subscribe('curam/fileedit/reportpasted', _ => {
    if (searchPageOpen && fedRefs.searchPageRef) {
      let ifr = fedRefs.searchPageRef.iframe,
        doc = ifr.contentDocument,
          d = ifr.contentWindow.dojo,
            list = d.byId('error-messages');
      if (!list) {
        var holder = d.byId('error-messages-container');
        list = doc.createElement('ul');
        list.id = 'error-messages';
        list.className = 'messages';
        holder.appendChild(list);
      }
      var txtDiv = doc.createElement('div');
      var paster = dojo.byId('paster');
      txtDiv.innerHTML = getMessage('Msg.Pasted.Val') + decodeURIComponent(paster.value);
      var item = doc.createElement('li');
      item.className = "level-2";
      item.appendChild(txtDiv);
      list.appendChild(item);
      doc = null;
      paster.value = '';
    }
  });
</script>
<jsp:scriptlet>curam.util.client.jsp.FileEditUtil.outputFileEditWidget(pageContext, "chrome");</jsp:scriptlet>
<div id="content" class="title-exists" tabIndex="-1"> <div id='statusDisplay'> <div class='record ICL'>${pageScope.initMessage}</div> </div>
</div>
<div id="checkmsg" style="display:none">${pageScope.extnFailed}</div>