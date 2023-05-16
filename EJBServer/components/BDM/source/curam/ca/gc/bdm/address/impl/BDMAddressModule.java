package curam.ca.gc.bdm.address.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import curam.codetable.ADDRESSLAYOUTTYPE;
import curam.core.address.impl.AddressFormat;
import curam.workspaceservices.applicationprocessing.impl.AddressMappingStrategy;

/**
 * binds event listeners or hookpoints so that even handlers become aware that
 * there are more than one even listener.
 */
public class BDMAddressModule extends AbstractModule {

  @Override
  protected void configure() {

    bindAddressFormat();

    // Address Mapping Strategy for UA address to map into Curam
    bind(AddressMappingStrategy.class)
      .to(BDMINTLAddressMappingStrategy.class);
  }

  protected void bindAddressFormat() {

    final MapBinder<String, AddressFormat> addressFormatBinder =
      MapBinder.newMapBinder(binder(), String.class, AddressFormat.class);
    addressFormatBinder.addBinding(ADDRESSLAYOUTTYPE.BDMINTL)
      .toInstance(new BDMAddressFormatINTL());
  }

}
