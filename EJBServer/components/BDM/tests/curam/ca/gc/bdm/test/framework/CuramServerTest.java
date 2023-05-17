package curam.ca.gc.bdm.test.framework;

import curam.core.fact.CachedCaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.fact.PositionHolderLinkFactory;
import curam.core.sl.entity.intf.PositionHolderLink;
import curam.core.sl.entity.struct.PositionHolderLinkDtls;
import curam.core.sl.entity.struct.PositionHolderLinkDtlsList;
import curam.core.struct.UsersKey;
import curam.util.cache.CacheInvalidationMessage;
import curam.util.cache.CacheManagerEjb;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.resources.ProgramLocale;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.StringHelper;
import junit.framework.TestCase;

/**
 * The abstract base class from which all Curam Server Process Tests inherit.
 */
public abstract class CuramServerTest extends TestCase {

  /**
   * The locale that is associated with the user whose access is being simulated
   * by this test class.
   */
  // BEGIN, CR00278762, PB
  protected static String kLocaleForTransaction;

  // END, CR00278762

  // BEGIN, CR00476719, SH
  // Allows a test to be skipped when run in "developer-mode"
  // Set to true to skip the test
  protected boolean developerMode = false;

  public boolean isDeveloperMode() {

    return developerMode;
  }

  // END, CR00476719

  /**
   * Constructor for CuramServerTest
   */
  public CuramServerTest(final String arg0) {

    super(arg0);

    // BEGIN, CR00356379, CL
    // curam.util.resources.Configuration.setProperty(EnvVars.ENV_PDC_ENABLED,
    // curam.core.impl.EnvVars.ENV_VALUE_NO);
    // END, CR00356379

    checkConfiguration();
    if (kLocaleForTransaction == null) {
      kLocaleForTransaction = ProgramLocale.getDefaultServerLocale();
    }
  }

  /**
   * User Assigning to PositionHolder
   */
  private void checkPositionHolderLink()
    throws AppException, InformationalException {

    final String userName = "unauthenticated";
    final PositionHolderLink positionHolderLink =
      PositionHolderLinkFactory.newInstance();
    final UsersKey key = new UsersKey();

    key.userName = userName;
    final PositionHolderLinkDtlsList PositionHolderLinkDtlsList =
      positionHolderLink.searchByUserName(key);

    if (PositionHolderLinkDtlsList.dtls.isEmpty()) {
      final PositionHolderLinkDtls dtls = new PositionHolderLinkDtls();
      dtls.userName = userName;
      dtls.organisationStructureID = 1;
      dtls.positionID = 80043;
      dtls.fromDate = Date.getCurrentDate();
      positionHolderLink.insert(dtls);

    }

  }

  // BEGIN, CR00238842, PF
  protected curam.util.transaction.TransactionInfo ti = null;

  // END, CR00238842

  // BEGIN, CR00050173, MR
  // BEGIN, HARP 63664, ANK
  protected boolean mQuietMode = true;

  // END, HARP 63664
  // END, CR00050173

  @Override
  protected final void setUp() {

    if (!mQuietMode) {
      System.out.println(this.getName());
    }

    // BEGIN, CR00050173, MR
    // BEGIN, HARP 63664, ANK
    begin();
    // END, HARP 63664
    // END, CR00050173

    try {
      curam.core.impl.SecurityImplementationFactory.register();
    } catch (final curam.util.exception.AppException e) {
      rollback();
      throw new curam.util.exception.AppRuntimeException(e);
    } catch (final curam.util.exception.InformationalException e) {
      rollback();
      throw new curam.util.exception.AppRuntimeException(e);
    }
    try {
      checkPositionHolderLink();
    } catch (AppException | InformationalException e1) {

      e1.printStackTrace();
    }
    try {
      setUpCuramServerTest();
    } catch (final Error e) {
      // covers errors such as assertion failures
      rollback();
      throw e;
    } catch (final RuntimeException e) {
      // covers errors such as database exceptions
      rollback();
      throw e;
    }

  }

  protected void setUpCuramServerTest() {// empty - may be overridden by

    // subclasses
  }

  @Override
  protected final void tearDown() throws Exception {

    try {
      tearDownCuramServerTest();

      // BEGIN, CR00476719, SH
      // ESDC TODO - removed to avoid compilation error.
      // RegisterPersonHelper.reset();
      // END, CR00476719

      // BEGIN, CR00455400, BD
      // SPMP-14584. The currently cached case header may not be valid for
      // subsequent unit tests, so empty the cache.
      CachedCaseHeaderFactory.newInstance().clearCache();
      // END, CR00455400
    } catch (final Error e) {
      // covers errors such as assertion failures
      rollback();
      throw e;
    } catch (final Exception e) {
      // covers errors such as database exceptions
      rollback();
      throw e;
    }

    // always rollback - no database modifications may endure beyond this test

    // BEGIN, CR00050173, MR
    if (shouldCommit()) {
      commit();
    } else {
      // END, CR00050173

      rollback();

      // BEGIN, CR00050173, MR
    }
    // END, CR00050173
  }

  protected void rollback() {

    // BEGIN, CR00050173, MR
    // BEGIN, HARP 63664, ANK
    clearCaches();
    // END, HARP 63664
    // END, CR00050173

    // CR00479630 Begin. Get current Transaction
    ti = TransactionInfo.getInfo();
    // CR00479630 End.

    if (ti != null) {
      ti.rollback();
      // BEGIN, CR00103631, PDN
      ti.closeConnection();
      // END, CR00103631

      if (!mQuietMode) {
        System.out.println("  Transaction rolled back");
      }

      ti = null;
    }
  }

  protected void tearDownCuramServerTest() {// empty - may be overridden by

    // subclasses
  }

  // convenience functions to retrieve and manipulate test dates
  protected static curam.util.type.Date getToday() {

    return curam.util.type.Date.getCurrentDate();
  }

  protected static curam.util.type.Date getTomorrow() {

    return getToday().addDays(1);
  }

  protected static curam.util.type.Date getYesterday() {

    return getToday().addDays(-1);
  }

  // BEGIN, CR00296724, GD
  protected void configureOracleConnectionPool() {

    // If this is Oracle, then caching should be on
    final String cachingOn = curam.util.resources.Configuration
      .getStaticProperty("curam.db.oracle.connectioncache.enabled");

    if (StringHelper.isEmpty(cachingOn)) {
      System.setProperty("curam.db.oracle.connectioncache.enabled", "true");
      System.setProperty("curam.db.oracle.connectioncache.initiallimit", "0");
      System.setProperty("curam.db.oracle.connectioncache.minlimit", "12");
      System.setProperty("curam.db.oracle.connectioncache.maxlimit", "48");
    }
  }

  // END, CR00296724

  /**
   * Checks that the system is configured correctly prior to initializing the
   * test
   */
  private void checkConfiguration() {

    // BEGIN, CR00296724, GD
    if (curam.util.resources.Configuration.getStaticProperty("curam.db.type")
      .compareToIgnoreCase("ORA") == 0) {
      configureOracleConnectionPool();
    }
    // END, CR00296724

    // The key repository must be switched on in order for tests to work -
    // Check that this is the case and if it is not then inform the user and
    // exit
    if (!curam.util.resources.Configuration
      .getBooleanProperty("curam.test.store.entitykeys")) {

      curam.util.resources.Configuration
        .setProperty("curam.test.store.entitykeys", "true");

    }

    // In order for testing of security to succeed the value of the variable
    // curam.databasedsecurity.caching.disabled needs to be set to true
    if (!curam.util.resources.Configuration
      .getBooleanProperty("curam.databasedsecurity.caching.disabled")) {

      curam.util.resources.Configuration
        .setProperty("curam.databasedsecurity.caching.disabled", "true");

    }

    // In order for tests that access application functionality that 'creates'
    // tasks
    // to complete successfully workflow process enactment needs to be disabled.

    // BEGIN, CR00234067, PM
    Configuration.setProperty(EnvVars.ENV_ENACT_WORKFLOW_PROCESS_DISABLED,
      CuramConst.kNO);
    // END, CR00234067

    // Configuration.setProperty("curam.test.disable.enactment", "true");
  }

  public void setQuietFlag(final boolean quietFlag) {

    mQuietMode = quietFlag;
  }

  // BEGIN, CR00050173, MR
  // BEGIN, HARP 63664, ANK
  /**
   * Hook point for subclasses to decide whether database updates should be
   * committed. Default behavior is to rollback.
   *
   * @return whether databases should be updated
   */
  protected boolean shouldCommit() {

    // rollback by default
    return false;
  }

  /**
   * Hook point for subclasses to commit the database updates.
   *
   * @return
   */
  protected final void commit() {

    clearCaches();

    // CR00479630 Begin. Get current Transaction
    ti = TransactionInfo.getInfo();
    // CR00479630 End.

    if (ti != null) {
      ti.commit();
      // BEGIN, CR00103631, PDN
      ti.closeConnection();
      // END, CR00103631

      if (!mQuietMode) {
        System.out.println("  Transaction committed");
      }
    }
  }

  /**
   * To be used when clear caches is not sufficient to clear dynamically
   * published codes.
   *
   * @throws InformationalException
   * @throws AppException
   */
  protected void resetCodeTableCacheVersion()
    throws AppException, InformationalException {

    // BEGIN, CR00238842, PF
    final curam.util.administration.intf.CacheAdmin cacheAdminObj =
      curam.util.administration.fact.CacheAdminFactory.newInstance();

    cacheAdminObj.resetCodeTableCacheVersion();
    // END, CR00238842
  }

  /**
   * Hook point for subclasses to clearCaches after the database commit.
   *
   * @return
   */
  protected void clearCaches() {

    // BEGIN, 190521, SH
    final CacheInvalidationMessage<Integer> cacheInvalidationMessage =
      new CacheInvalidationMessage<Integer>("RuleSetGroupCache", null);
    CacheManagerEjb.postInvalidationMessage(cacheInvalidationMessage);
    // END, 190521

    // Clearing unique id and code table caches so they do not affect the
    // next
    // test run

    final curam.util.administration.intf.CacheAdmin reloadCacheObj =
      curam.util.administration.fact.CacheAdminFactory.newInstance();

    try {
      reloadCacheObj.reloadCodetableCache();
    } catch (final curam.util.exception.AppException e) {

      fail("CodeTables failed to reload" + e.toString());

    } catch (final curam.util.exception.InformationalException e) {

      fail("CodeTables failed to reload" + e.toString());

    }
  }

  /**
   * Hook point for subclasses to begin the database transaction.
   *
   * @return
   */
  protected final void begin() {

    // clear out cache of entity keys
    curam.util.dataaccess.KeyRepository.reset();

    curam.util.type.Date.undoOverrideDate();
    curam.util.type.DateTime.undoOverrideDateTime();
    // BEGIN, CR00069114, POH
    curam.util.transaction.TransactionInfo.setInformationalManager();
    // END, CR00069114
    // BEGIN, CR00089425, MPB
    if (!curam.util.resources.Configuration
      .getBooleanProperty("curam.test.stubdeferredprocessing")
      || !curam.util.resources.Configuration.getBooleanProperty(
        "curam.test.stubdeferredprocessinsametransaction")) {

      curam.util.resources.Configuration
        .setProperty("curam.test.stubdeferredprocessing", "true");

      curam.util.resources.Configuration.setProperty(
        "curam.test.stubdeferredprocessinsametransaction", "true");
      // END, CR00089425, MPB
    }

    // so create the transaction information and start the transaction
    class MyBizTransaction implements curam.util.internal.BizTransaction {

      @Override
      public boolean transactional() {

        return true;
      }

      @Override
      public java.lang.String getName() {

        return "CuramServerTest transaction";
      }
    }

    ti = curam.util.transaction.TransactionInfo.setTransactionInfo(
      curam.util.transaction.TransactionInfo.TransactionType.kOnline,
      new MyBizTransaction(), null, kLocaleForTransaction);

    ti.begin();

    if (!mQuietMode) {
      System.out.println("  Transaction started");
    }

  }

  // END, HARP 63664

  // END, CR00050173

  // BEGIN, CR00194305, AMD
  // ___________________________________________________________________________
  /**
   * This method returns the component in which the test is running (e.g. core,
   * ServicePlans, EvidenceBroker etc.). The default component is "core", which
   * means that any test class that is not running in the "core" component
   * should override this method and return the correct value.
   *
   * @return The component where the test class is located. The default is
   * "core".
   */
  protected String getTestComponent() {

    return "core";
  }

  // END, CR00194305

  // BEGIN, CR00219096, PF
  /**
   * This method should be used to determine if the test is being run against a
   * database with limited lock semantics (in particular H2). Which means that
   * certain tests would fail or deadlock due to database locking issues.
   */
  public boolean databaseWithLimitedLockingSemantics() {

    // Check the system configuration, for H2
    if (curam.util.resources.Configuration.getProperty("curam.db.type")
      .compareToIgnoreCase("H2") == 0) {

      // Check to make sure this setting hasn't been bypassed.
      if (!curam.util.resources.Configuration.getBooleanProperty(
        "curam.test.bypassdatabaselimitedlocksemanticscheck")) {
        // Return true
        return true;
      }

    }

    return false;

  }
  // END, CR00219096
}
