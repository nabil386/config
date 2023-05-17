function show_hide(tblid, show) {
				if (document.getElementById("th2span2").className == 'toggle-image-ci') {
				document.getElementById("th2span2").className = document.getElementById("th2span2").className.replace(/\btoggle-image-ci\b/,'toggle-image-contract-ci');
				document.getElementById('tr2').style.display='none';
			  document.getElementById('tr3').style.display='none';
			  document.getElementById('tr1').style.display='none';
			  document.getElementById('tr4').style.display='none';
			  }
			  else {
      	document.getElementById("th2span2").className = document.getElementById("th2span2").className.replace(/\btoggle-image-contract-ci\b/,'toggle-image-ci');
				document.getElementById('tr2').style.display='';
			  document.getElementById('tr3').style.display='';
			  document.getElementById('tr1').style.display='';
			  document.getElementById('tr4').style.display='';
      	}
    		
 }