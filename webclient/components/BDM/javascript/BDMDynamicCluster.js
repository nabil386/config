function countryIsCA() {
	if ( curam.dcl.getField('addressSelectedRef')== "false" && curam.dcl.getField('countryRef') == 'CA') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}
function countryIsUS() {
	if (curam.dcl.getField('countryRef') == 'US') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}
function countryIsIntl() {
	if (curam.dcl.getField('countryRef') != 'CA'&& curam.dcl.getField('countryRef') != 'US') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}
function addressIsReadOnly() {
	if (curam.dcl.getField('addressSelectedRef')== "true"  &&curam.dcl.getField('countryRef') == 'CA') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}

function searchByPostalCodeEnabled() {
	if (curam.dcl.getField('countryRef') == 'CA') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}

function addressIsResidential(){
	if (curam.dcl.getField('addressTypeRef') == 'AT1') {
		return curam.dcl.CLUSTER_SHOW;
	}
	return curam.dcl.CLUSTER_HIDE;
}