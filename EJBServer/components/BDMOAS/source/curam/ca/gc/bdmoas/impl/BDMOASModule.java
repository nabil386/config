/**
 *
 */
package curam.ca.gc.bdmoas.impl;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import curam.ca.gc.bdmoas.productdelivery.impl.BDMOASProductDeliveryLifeCycleEvents;
import curam.core.impl.ProductDeliveryLifeCycleEvents;

/**
 * Bind custom implementations to OOTB interfaces/functionalities.
 *
 * @author donghua.jin
 *
 */
public class BDMOASModule extends AbstractModule {

  /**
   * Entry point.
   */
  @Override
  protected void configure() {

    super.configure();

    bindProductDeliveryLifeCycleEvents();

  }

  /**
   * Binding added between the OOTB Class ProductDeliveryLifeCycleEvents
   * and custom BDMOASProductDeliveryLifeCycleEvents class.
   */
  private void bindProductDeliveryLifeCycleEvents() {

    final Multibinder<ProductDeliveryLifeCycleEvents> productDeliveryLifeCycleEvents =
      Multibinder.newSetBinder(binder(),
        ProductDeliveryLifeCycleEvents.class);

    productDeliveryLifeCycleEvents.addBinding()
      .to(BDMOASProductDeliveryLifeCycleEvents.class);

  }

}
