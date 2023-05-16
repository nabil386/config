package curam.ca.gc.bdm.client.domain.convert.financial.impl;

import curam.util.client.domain.convert.ConversionException;
import curam.util.client.domain.convert.SvrInt32Converter;

public class BDMIntStringConverter extends SvrInt32Converter {

	@Override
	public String format(Object data) throws ConversionException {
		return data.toString();
	}

}
