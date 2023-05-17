package curam.ca.gc.bdm.test.rules.functions;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.util.resources.Configuration;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.rules.functor.CustomFunctor;
import curam.util.type.Date;
import curam.util.type.Money;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;

/**
 * The Class CustomFunctionTestJunit4 provides useful methods for unit testing
 * CustomFunctions for IEG .
 */
public abstract class CustomFunctionTestJunit4 extends CuramServerTestJUnit4 {

  /**
   * Instantiates a new custom function test junit 4.
   */
  public CustomFunctionTestJunit4() {

    super();
  }

  /**
   * Expectations parameters, add the expected params to the tested function.
   *
   * @param params the params
   */
  protected void expectationsParameters(final Object... params) {

    final List<Adaptor> parameters = new ArrayList<>();
    for (final Object p : params) {
      if (p instanceof String) {
        parameters.add(AdaptorFactory.getStringAdaptor((String) p));
      } else if (p instanceof Integer) {
        parameters.add(AdaptorFactory.getIntegerAdaptor((Integer) p));
      } else if (p instanceof Date) {
        parameters.add(AdaptorFactory.getDateAdaptor((Date) p));
      } else if (p instanceof Money) {
        parameters.add(AdaptorFactory.getMoneyAdaptor((Money) p));
      } else if (null == p) {
        parameters.add(null);
      } else if (p instanceof Boolean) {
        parameters.add(AdaptorFactory.getBooleanAdaptor((Boolean) p));
      } else if (p instanceof Long) {
        parameters.add(AdaptorFactory.getLongAdaptor((Long) p));
      }

    }

    new MockUp<CustomFunctor>() {

      @Mock
      public List<Adaptor> getParameters() {

        return parameters;
      }

    };
  }

  /**
   * Expectations for environment variables.
   *
   * @param envVarKey the env var key
   * @param envVarValue the env var value
   */
  public void expectationsEnvVars(final String envVarKey,
    final Boolean envVarValue) {

    new Expectations(Configuration.class) {

      {
        Configuration.getBooleanProperty(envVarKey);
        result = envVarValue.booleanValue();
      }
    };

  }

}
