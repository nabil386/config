/**
 * 
 */
package curam.ca.gc.bdm.facade.productdelivery.impl;

import com.google.inject.Inject;
import curam.core.sl.struct.CaseIDKey;
import curam.core.sl.struct.ProductDeliveryTabDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;

/**
 * @author sivakumar.kalyanasun
 * @version 1.0 Task 19383 - Urgent Flag for PDC Context Panel This process
 *          class provides the functionality for the Product Delivery Case
 *          section tab details service layer.
 */
public class BDMProductDeliveryTab extends curam.ca.gc.bdm.facade.productdelivery.base.BDMProductDeliveryTab {

	@Inject
	private BDMProductDeliveryTabHelper bdmProductDeliveryTabHelper;

	public BDMProductDeliveryTab() {

		GuiceWrapper.getInjector().injectMembers(this);
	}

	/**
	 * 
	 */
	@Override
	public ProductDeliveryTabDetails readProductDeliveryTabDetails(CaseIDKey key)
			throws AppException, InformationalException {

		ProductDeliveryTabDetails productDeliveryTabDetails = bdmProductDeliveryTabHelper
				.readProductDeliveryTabDetails(key);
		return productDeliveryTabDetails;
	}

}
