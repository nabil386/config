package curam.ca.gc.bdm.sl.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.sl.tab.impl.BDMTabLoaderConsts;
import curam.ca.gc.bdm.sl.tab.task.impl.BDMTaskHomeMenuLoader;
import curam.ca.gc.bdm.test.BDMBaseTest;
import curam.core.impl.EnvVars;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.resources.Configuration;
import curam.util.tab.impl.MenuState;
import curam.util.tab.impl.MenuStateFactory;
import curam.util.transaction.TransactionInfo;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.ir.annotations.Ignore;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

@RunWith(JMockit.class)
@Ignore
public class BDMTaskHomeMenuLoaderTest extends BDMBaseTest {

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMTaskHomeMenuLoaderTest() {

    super();

  }

  @Test
  public void testloadMenuState()
    throws AppException, InformationalException {

    final MenuState menuState = MenuStateFactory.getMenuStateInstance();
    final Map<String, String> pageParameters = new HashMap<String, String>();
    final String[] idsToUpdate = {"123", "345", "value" };

    final boolean pdcEnabled =
      Configuration.getBooleanProperty(EnvVars.ENV_PDC_ENABLED);
    menuState.setVisible(!pdcEnabled, TabLoaderConst.kPersonEdit);
    menuState.setVisible(pdcEnabled, TabLoaderConst.kPersonEditPDC);

    menuState.setVisible(false, TabLoaderConst.kPersonNewTriage);
    menuState.setVisible(false, TabLoaderConst.kPersonNewScreening);
    menuState.setVisible(false, TabLoaderConst.kReferralApplicationCase);

    pageParameters.put(BDMTabLoaderConsts.kTaskID, "256");

    final BDMTaskHomeMenuLoader personHomeMenuLoader =
      new BDMTaskHomeMenuLoader();
    personHomeMenuLoader.loadMenuState(menuState, pageParameters,
      idsToUpdate);
    assertTrue(menuState.getTestResult().length > 0);

    // System.out.println(menuState.getTestResult()[4].split(":"));

  }

  public void mockUserObjects() throws AppException, InformationalException {

    new MockUp<TransactionInfo>() {

      @Mock
      public String getProgramUser()
        throws AppException, InformationalException {

        return "caseworker";

      }

    };

  }

}
