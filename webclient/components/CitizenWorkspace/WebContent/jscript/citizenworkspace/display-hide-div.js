dojo.require("dojo.fx");

var initialMapWidth;
var initialGovServiceWidth;

// show/hide toggle the div identified by the ID passed
function toggleDiv(flipperID, divID) {

	var div = dojo.byId(divID);

	var flipper = dojo.byId(flipperID);

	var display = div.getAttribute('displayed');

	if(display == '' || display == null) {
		display = 'on';
	}

	if (display == 'on') {
			dojo.fx.wipeOut({
				node: divID, duration: 300
			}).play();
			display = 'off';
			var img = '../Images/arrow_contract.png';
			flipper.src = img;
	} else {
			dojo.fx.wipeIn({
				node: divID, duration: 300
			}).play();
			display = 'on';
			var img = '../Images/arrow_expanded.png';
			flipper.src = img;
	}
	// remember the toggle state
	div.setAttribute('displayed', display);
}

var recommendations = {


	// hides the service description, called on page load
	hideServiceDescriptions: function() {
		dojo.forEach(
			dojo.query('[displayed]'),
			function(div) {
				dojo.fx.wipeOut({
					node: div.id, duration: 1
				}).play();
				div.setAttribute('displayed', 'off');
			}
		);
		
		initialMapWidth = dojo.marginBox(dojo.byId('curam_map_div_id')).w;
		initialGovServiceWidth = 230;
	},
	
	mapOnly: function(govServicesDivID, mapDivID) {
		
		// work out the new widths
		var govServicesBox = dojo.marginBox(dojo.byId(govServicesDivID));
		var mapBox = dojo.marginBox(dojo.byId(mapDivID));
		
		var newMapWidth = initialGovServiceWidth + mapBox.w - 28;
		
		//alert('gov box: ' + govServicesBox.w + 'map box: ' + mapBox.w + 'map width: ' + newMapWidth);
		
		var anim = dojo.fx.wipeOut({
			node: govServicesDivID, duration: 100
		});
		
		dojo.connect(anim, "onEnd", function(){
			
			dojo.animateProperty({
				  node: mapDivID, duration: 100,
				  properties: {
					  width: { end: newMapWidth, unit:'px' }
				  }
			}).play();
			
			dojo.byId('gov_services_div_collapsed').style.display = 'inline';
			
		});
		
		anim.play();
	},
	
	restoreGovServices: function(govServicesDivID, mapDivID) {
		
		
		var anim = dojo.fx.wipeOut({
			node: 'gov_services_div_collapsed', duration: 50
		});
		
		dojo.connect(anim, "onEnd", function() {
			
			dojo.animateProperty({
				  node: mapDivID, duration: 100,
				  properties: {
					  width: { end: initialMapWidth - 1, unit:'px' }
				  },
				  onEnd: function() {
					  dojo.byId(mapDivID).style.width = '';
					  
						dojo.fx.wipeIn({
							node: govServicesDivID, duration: 100
						}).play();					  
				  }
			}).play();			
			
			dojo.byId('gov_services_div_expanded').style.height = '450px';
			
		});

		anim.play();
		
	}

}

dojo.addOnLoad(recommendations.hideServiceDescriptions);