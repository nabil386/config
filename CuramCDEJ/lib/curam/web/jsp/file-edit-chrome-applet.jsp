<script>
var baseUrl = jsBaseURL +"/servlet/FileEdit";
var reqParams = "";
var chromeFileParams = "";
var chromeParamsReady = false;
var fContents = 'wia_00001.curamdoc';
var fDtls = 'wia_00001.dtls';
var lFiles = null;
var fSystem  = null;

function getChomreFileParams() {
	if(!chromeParamsReady) {
		getChomreFileParams();
	}
	return chromeFileParams;
}

function reportFailure(failureMsg){
	console.info(failureMsg);
}

function getContents() {
  require(["dojo/request/xhr"], function(xhr) {
    xhr.get(baseUrl, {
      query: "c=ct" + reqParams,
      headers: { "X-token": curam.dialog.getParentWindow(window).getToken() },
      handleAs: "arraybuffer",
      preventCache:true
    }).then(function(data){
      var byteArray = new Uint8Array(data);
      var blob = new Blob(byteArray, {type:"application/octet-stream"});
      writeDoc(byteArray);
    }).otherwise(function(err){
      reportFailure(getMessage('Log.Write.Failure'));
    });
  });
}

function getDetails() {
  require(["dojo/request/xhr"], function(xhr) {
    xhr.get(baseUrl, {
      query: "c=dt" + reqParams,
      headers: { "X-token": curam.dialog.getParentWindow(window).getToken() },
      handleAs: "arraybuffer",
      preventCache:true
    }).then(function(data){
      writeDetails(data);
    }).otherwise(function(err){
      reportFailure(getMessage('Log.Write.Failure'));
    });
  });
}

function writeDetails(fData) {
     window.webkitRequestFileSystem(window.PERMANENT, 640000,
         function(fs){
          lFiles = fs; 
          fSystem = fs.root;
          fs.root.getFile('wia_00001.dtls', {create:true}, function(fileEntry) {
             fileEntry.createWriter(function(fileWriter){
               fileWriter.onwriteend = function(e) {
           		 chromeFileParams = chromeFileParams + e.timeStamp + '|' + e.total + '|';
                 getContents();
               },
               fileWriter.onerror = function(err) {
                 reportFailure('Failed to write details');
               }
               var blob = new Blob([fData], {type:'text/plain'});
               fileWriter.write(blob);
             }, function(err) {
               reportFailure('Failed to get writer for details');
             });
           });
         },
         function(err){
           reportFailure("Browser File System Failed! " + err.toString());
         });
   }

function writeDoc(fData) {
     lFiles.root.getFile('wia_00001.curamdoc', {create:true}, function(fileEntry) {
       fileEntry.createWriter(function(fileWriter){
         fileWriter.onwriteend = function(e) {
           chromeFileParams = chromeFileParams + e.timeStamp + '|' + e.total;
           chromeParamsReady = true;
         },
         fileWriter.onerror = function(e) {
           reportFailure('Failed to write contents');
         }
         var blob = new Blob([fData], {type:'text/plain'});
         fileWriter.write(blob);
       }, function(err) {
         reportFailure('Failed to get writer for contents');
       });
     }); 
   }
   
function readAndCommitDoc(lastCommit) {
	var chkTimeout = setInterval(function(){
		clearInterval(chkTimeout);
		var commitSuccess = false;
	    fSystem.getFile(fContents, {},
	      function(fEntry) {
	        fEntry.file(
			    function(file) {
	            var reader = new FileReader();
	            reader.onloadend = function(e) {
	              var base64Contents = btoa(this.result);
	              var urlencContents = encodeURIComponent(base64Contents);
	              commitSuccess = commitDoc(urlencContents, lastCommit);
	              return commitSuccess;
	            };
	            reader.readAsBinaryString(file);
	          },
	          function(err){
	        	  console.info(err);
	            reportFailure('Could not load the file to commit');
	          });
	      },
	      function(err) {
	    	console.info(err);
	        reportFailure('Could not read the file to commit!');
	      });
	}, 200);
};

function removeDoc(fName, lastAction) {
      fSystem.getFile(fName, {create: false},
        function(fEntry) {
          fEntry.remove(
            function() {
              if (lastAction) {
                if (getParent() != null) {
                  fedRefs.parentRef.prepareDialogClose(); 
                }
                curam.dialog.closeModalDialog();
              }
            },
            function(err) {
              if (lastAction) {
                if (getParent() != null) {
                  fedRefs.parentRef.dojo.publish("curam/fileedit/makecloseable");
                }
              }
           });
        });
};
    function cleanUp() {
      window.webkitRequestFileSystem(window.PERMANENT, 640000,
        function(fs){
          fSystem = fs.root;
          removeDoc(fContents);
          removeDoc(fDtls);
        });
    }   
    
    function commitDoc(encodedDocData, lastCommit) {
    	
    	var coommitSuccess = false;
        require(["dojo/request/xhr"], function(xhr) {
          xhr.post(baseUrl, {
            data: "c=tg" + reqParams + "&contents=" + encodedDocData,
            headers: { "X-token": tkn, "Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},
            sync:true
          }).then(function(respData){
            if (respData == "COMMIT_SUCCESS") {
            	coommitSuccess = true;
            }
            if (lastCommit) {
              removeDoc('wia_00001.curamdoc', true);
            }
            return success;
          }).otherwise(function(err){
              reportFailure(getMessage('Log.Write.Failure'));
              return coommitSuccess;
          })
        });
      };    

dojo.addOnLoad(function() {
          for (p in wfeParams) {
              if (p != 'pNames') {
                reqParams += "&" + p + "=" + wfeParams[p];
          	  }
          }
    	  getDetails();
});
</script>