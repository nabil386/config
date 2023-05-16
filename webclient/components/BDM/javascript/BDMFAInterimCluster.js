function isInterimSelected() {
	if ((curam.dcl.getField('applicationTypeRef') == 'Interim')||(curam.dcl.getField('applicationTypeRef') == 'FATYP61')) {		
		return curam.dcl.CLUSTER_SHOW;
	}     
	return curam.dcl.CLUSTER_HIDE;
}

function isInterimSelectedAndCountryisUS() {	
	if ((curam.dcl.getField('countryRef') == 'US') &&
		((curam.dcl.getField('applicationTypeRef') == 'Interim')||(curam.dcl.getField('applicationTypeRef') == 'FATYP61'))) {
			return curam.dcl.CLUSTER_SHOW;
	}		
	return curam.dcl.CLUSTER_HIDE;
}