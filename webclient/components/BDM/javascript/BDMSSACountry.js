function isExtPartyTypeSSACountry(){
	if (curam.dcl.getField('ssaCountryTypeRef') == 'OASC0001') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}