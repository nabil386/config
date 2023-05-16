function mailingAddressSame() {
	if (curam.dcl.getField('mailBooleanRef') == true) {
		return curam.dcl.CLUSTER_HIDE;
	}
	return curam.dcl.CLUSTER_SHOW;
}

function isRestrictedCountryAndForignGovtSelected() {
	if ( (curam.dcl.getField('receivedFromRef') == 'RF4') 
			&& ( (curam.dcl.getField('countryRef') == 'US') || (curam.dcl.getField('countryRef') == 'AT')
				|| (curam.dcl.getField('countryRef') == 'JP') || (curam.dcl.getField('countryRef') == 'JM')) ) {
		return curam.dcl.CLUSTER_HIDE;
	}
	return curam.dcl.CLUSTER_SHOW;
}
