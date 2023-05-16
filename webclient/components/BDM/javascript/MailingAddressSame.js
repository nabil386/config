function mailingAddressSame() {
	if (curam.dcl.getField('mailBooleanRef') == true) {
		return curam.dcl.CLUSTER_HIDE;
	}
	return curam.dcl.CLUSTER_SHOW;
}