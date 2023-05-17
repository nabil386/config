package curam.ca.gc.bdm.util.rest.impl;

import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.impl.EnvVars;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import org.junit.Test;

/**
 * Suite of tests for the BDMInterfaceLogger class.
 */
public class BDMInterfaceLoggerTest extends CuramServerTestJUnit4 {

  /**
   * Test for logRequest() method.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogRequest() throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logRequest("test()", "1", new BDMInterfaceLogger());

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logRequest() method where logger is not enabled.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogRequest_loggerDisabled()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();

    testSubject.logRequest("test()", "1", new BDMInterfaceLogger());
  }

  /**
   * Test for logRequest() method where the object passed in is a String.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogRequest_stringObject()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logRequest("test()", "1", "TEST");

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logResponse() method.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogResponse() throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logResponse("test()", "1", new BDMInterfaceLogger());

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logResponse() method where object passed in is null.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogResponse_nullObject()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logResponse("test()", "1", null);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logResponse() method where log is not enabled.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogResponse_loggerDisabled()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();

    testSubject.logResponse("test()", "1", new BDMInterfaceLogger());
  }

  /**
   * Test for logResponse() method where the object passed in is a String.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogResponse_stringObject()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logResponse("test()", "1", "TEST");

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logRestAPIPerf() method.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogRestAPIPerf()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();
    final String property =
      Configuration.getProperty(EnvVars.ENV_BDMREST_LOG_ENABLED);

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, "true");

    testSubject.logRestAPIPerf("test()", 56, "1");

    Configuration.setProperty(EnvVars.ENV_BDMREST_LOG_ENABLED, property);
  }

  /**
   * Test for logRestAPIPerf() method where logger is not enabled.
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Test
  public void testLogRestAPIPerf_loggerDisabled()
    throws AppException, InformationalException {

    final BDMInterfaceLogger testSubject = new BDMInterfaceLogger();

    testSubject.logRestAPIPerf("test()", 56, "1");
  }
}
