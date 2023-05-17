package curam.ca.gc.bdm.sl.participant.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.participant.impl.BDMPersonHomeMenuLoader;
import curam.ca.gc.bdm.sl.address.fact.BDMPDCAddressFactory;
import curam.ca.gc.bdm.sl.address.intf.BDMPDCAddress;
import curam.ca.gc.bdm.test.evidence.BDMEvidenceUtilsTest;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.core.facade.struct.ConcernRoleIDKey;
import curam.core.impl.EnvVars;
import curam.core.sl.tab.impl.TabLoaderConst;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.pdc.struct.PDCPersonDetails;
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

@RunWith(JMockit.class)
@Ignore
public class BDMPersonHomeMenuLoaderTest extends CuramServerTestJUnit4 {

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMPersonHomeMenuLoaderTest() {

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

    final ConcernRoleIDKey key = new ConcernRoleIDKey();

    final BDMPDCAddress address = BDMPDCAddressFactory.newInstance();
    final BDMEvidenceUtilsTest bdmEvidenceUtilsTest =
      new BDMEvidenceUtilsTest();
    final PDCPersonDetails pdcPersonDetails =
      bdmEvidenceUtilsTest.createPDCPerson();
    final long concernRoleID = pdcPersonDetails.concernRoleID;

    pageParameters.put("concernRoleID", String.valueOf(concernRoleID));

    final BDMPersonHomeMenuLoader personHomeMenuLoader =
      new BDMPersonHomeMenuLoader();
    personHomeMenuLoader.loadMenuState(menuState, pageParameters,
      idsToUpdate);

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
