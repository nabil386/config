/**
 *
 */
package curam.ca.gc.bdm.facade.integratedcase.impl;

import com.google.inject.Inject;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.IntegratedCaseTabDetail;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * @author sivakumar.kalyanasun
 * @version 1.0 Task 19383 - Urgent Flag for IC Context Panel This process class
 *          provides the functionality for the Integrated Case section tab
 *          details service layer.
 */
public class BDMIntegratedCaseTab extends curam.ca.gc.bdm.facade.integratedcase.base.BDMIntegratedCaseTab {

	@Inject
	private BDMIntegratedCaseTabHelper bdmIntegratedCaseTabHelper;

	/**
	 *
	 */
	public BDMIntegratedCaseTab() {

		GuiceWrapper.getInjector().injectMembers(this);
	}

	@Override
	public IntegratedCaseTabDetail readIntegratedCaseTabDetail(final CaseIDKey key)
			throws AppException, InformationalException {

		final IntegratedCaseTabDetail integratedCaseTabDetail = bdmIntegratedCaseTabHelper
				.readIntegratedCaseTabDetail(key);
		return integratedCaseTabDetail;
	}

}
