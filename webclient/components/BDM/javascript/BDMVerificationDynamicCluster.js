/*
 * Task 23374 DEV - Agent portal verification changes by Siva 05/03/2022
 */

function rejectionReasonEnabled() {
	
	if ((curam.dcl.getField('statusRef') == 'Rejected')||(curam.dcl.getField('statusRef') == 'BDMVIS8001')) {
		return curam.dcl.CLUSTER_SHOW;
	}
     
	return curam.dcl.CLUSTER_HIDE;
}
function otherCommentsEnabled() {
		if ((curam.dcl.getField('rejectionReasonRef') == 'Other')||(curam.dcl.getField(	'rejectionReasonRef') == 'BDMVRR8004')) {
				if ((curam.dcl.getField('statusRef') == 'Rejected')||(curam.dcl.getField('statusRef') == 'BDMVIS8001')) {
					return curam.dcl.CLUSTER_SHOW;
				}
	}
	
	return curam.dcl.CLUSTER_HIDE;
}

function clearCommentField(value) {
  var comments = dojo.byId("o3_pb");
  comments.value = value;
  return comments;
}