package curam.ca.gc.bdm.test.rest.bdmrfrapi.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.test.concern.person.RegisterPerson;
import curam.ca.gc.bdm.test.junit4.CuramServerTestJUnit4;
import curam.citizenworkspace.appeals.impl.AppealDatastorePrepopulator;
import curam.citizenworkspace.codetable.IntakeClientType;
import curam.citizenworkspace.codetable.impl.OnlineScriptTypeEntry;
import curam.citizenworkspace.facade.struct.SetupAppealResult;
import curam.citizenworkspace.rest.facade.fact.UAUserAccountAPIFactory;
import curam.citizenworkspace.rest.facade.intf.UAUserAccountAPI;
import curam.citizenworkspace.rest.facade.struct.UAUserAccount;
import curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfoDAO;
import curam.citizenworkspace.security.fact.IntakeClientFactory;
import curam.citizenworkspace.security.struct.IntakeClientDtls;
import curam.citizenworkspace.security.struct.IntakeClientKey;
import curam.citizenworkspace.security.struct.IntakeClientUserNameKey;
import curam.codetable.impl.METHODOFDELIVERYEntry;
import curam.core.impl.EnvVars;
import curam.core.onlineappealrequest.impl.OnlineAppealRequest;
import curam.core.onlineappealrequest.impl.OnlineAppealRequestDAO;
import curam.core.struct.PersonDtls;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.codetable.impl.IEGSCRIPTSTATUSEntry;
import curam.ieg.definition.impl.IEGScriptIdentifier;
import curam.ieg.entity.fact.IEGScriptInfoFactory;
import curam.ieg.entity.intf.IEGScriptInfo;
import curam.ieg.entity.struct.IEGScriptInfoDtls;
import curam.ieg.entity.struct.IEGScriptInfoDtlsList;
import curam.ieg.entity.struct.SearchByScriptIDAndStatusKey;
import curam.ieg.impl.IEGScriptExecution;
import curam.ieg.impl.IEGScriptExecutionFactory;
import curam.participant.impl.ConcernRoleDAO;
import curam.participant.person.impl.PersonDAO;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.security.Authentication;
import curam.workspaceservices.util.impl.DatastoreHelper;
import java.rmi.RemoteException;
import javax.security.auth.login.FailedLoginException;
import org.junit.Before;

public class BDMUARequestForReconsiderationAPITest
  extends CuramServerTestJUnit4 {

  @Inject
  private CitizenScriptInfoDAO citizenScriptInfoDAO;

  @Inject
  private OnlineAppealRequestDAO onlineAppealRequestDAO;

  @Inject
  private ConcernRoleDAO concernRoleDAO;

  @Inject
  private Provider<AppealDatastorePrepopulator> dsPrepopulator;

  @Inject
  private PersonDAO personDAO;

  long concernRoleID;

  final String externalUserName = "joeysmithjunit";

  Authentication authentication;

  /**
   * Before running any tests, register joesmith as an externalUser
   *
   * @throws AppException
   * @throws InformationalException
   */
  @Before
  public void before() throws AppException, InformationalException,
    FailedLoginException, RemoteException {

    GuiceWrapper.getInjector().injectMembers(this);

    // Set up necessary info to register a external person
    final UAUserAccountAPI externalUser =
      UAUserAccountAPIFactory.newInstance();
    final UAUserAccount uaUserAccount = new UAUserAccount();
    uaUserAccount.username = externalUserName;
    uaUserAccount.first_name = "Joe";
    uaUserAccount.surname = "Smith";
    uaUserAccount.password = "password123";
    uaUserAccount.confirm_password = "password123";
    uaUserAccount.is_terms_and_cond_accepted = true;
    uaUserAccount.secret_question_type = "SQT1";
    uaUserAccount.secret_question_answer = "Joe";

    // Create external user
    externalUser.createUserAccount(uaUserAccount);

    final IntakeClientUserNameKey intakeClientUserNameKey =
      new IntakeClientUserNameKey();
    intakeClientUserNameKey.userName = uaUserAccount.username;

    final IntakeClientDtls intakeClientDtls = IntakeClientFactory
      .newInstance().readByUserName(intakeClientUserNameKey);
    intakeClientDtls.type = IntakeClientType.LINKED;

    final IntakeClientKey intakeClientKey = new IntakeClientKey();
    intakeClientKey.intakeClientID = intakeClientDtls.intakeClientID;

    IntakeClientFactory.newInstance().modify(intakeClientKey,
      intakeClientDtls);

    final RegisterPerson registerPersonObj = new RegisterPerson("");
    final PersonDtls personDtls = registerPersonObj
      .registerDefault("Demo Test", METHODOFDELIVERYEntry.CHEQUE);
    this.concernRoleID = personDtls.concernRoleID;

  }

  public SetupAppealResult appealSetupHelper()
    throws AppException, InformationalException {

    final curam.participant.impl.ConcernRole role =
      this.concernRoleDAO.get(this.concernRoleID);

    final OnlineAppealRequest onlineAppealRequest =
      this.onlineAppealRequestDAO.newInstance();
    onlineAppealRequest.setCreatedBy(role);
    onlineAppealRequest.insert();

    final SetupAppealResult result = new SetupAppealResult();

    final String scriptID =
      Configuration.getProperty(EnvVars.ENV_CW_APPEALS_SCRIPT_ID);
    final String schemaName =
      Configuration.getProperty(EnvVars.ENV_CW_APPEALS_DATASTORE_SCHEMA);

    final long datastoreID = createAppealsDataStore(schemaName);

    final IEGScriptIdentifier scriptIdentifier =
      createScriptIdentifier(scriptID);

    final IEGScriptExecution execution =
      createScriptExecution(datastoreID, schemaName, scriptIdentifier);

    final curam.citizenworkspace.scriptinfo.impl.CitizenScriptInfo citizenScriptInfo =
      this.citizenScriptInfoDAO.newInstance();

    citizenScriptInfo.insert(execution.getExecutionID(),
      onlineAppealRequest.getID().longValue(), OnlineScriptTypeEntry.APPEAL,
      datastoreID, schemaName, externalUserName);

    final OnlineAppealRequest readOnlineAppeal =
      this.onlineAppealRequestDAO.get(onlineAppealRequest.getID());
    final Datastore datastore = DatastoreHelper.openDatastore(schemaName);
    final Entity rootEntity = datastore.readEntity(datastoreID);
    readOnlineAppeal.start(rootEntity, readOnlineAppeal.getVersionNo(),
      execution/* 318 */ .getExecutionID());

    result.iegExecutionID = execution.getExecutionID();

    return result;

  }

  private IEGScriptExecution createScriptExecution(final long datastoreID,
    final String schemaName, final IEGScriptIdentifier scriptIdentifier)
    throws AppException, InformationalException {

    final IEGScriptExecution scriptExecution =
      IEGScriptExecutionFactory.getInstance()
        .createScriptExecutionObject(scriptIdentifier, datastoreID);

    scriptExecution.setSchemaName(schemaName);

    IEGScriptExecutionFactory.getInstance()
      .saveScriptExecutionObject(scriptExecution);

    return scriptExecution;
  }

  private IEGScriptIdentifier createScriptIdentifier(final String scriptID)
    throws AppException, InformationalException {

    IEGScriptIdentifier scriptIdentifier;
    final IEGScriptInfo iegScriptInfo = IEGScriptInfoFactory.newInstance();
    final SearchByScriptIDAndStatusKey searchByScriptIDAndStatusKey =
      new SearchByScriptIDAndStatusKey();
    searchByScriptIDAndStatusKey.scriptID = scriptID;
    searchByScriptIDAndStatusKey/* 433 */ .status =
      IEGSCRIPTSTATUSEntry.ACTIVE.getCode();
    final IEGScriptInfoDtlsList scriptInfoDtlsList =
      iegScriptInfo.searchByScriptIDAndStatus(searchByScriptIDAndStatusKey);
    if (scriptInfoDtlsList.dtls.size() > 0) {
      final IEGScriptInfoDtls scriptInfoDtls =
        scriptInfoDtlsList.dtls.item(0);
      scriptIdentifier = new IEGScriptIdentifier(scriptID,
        scriptInfoDtls.type, scriptInfoDtls.scriptVersion);
    } else {
      scriptIdentifier = new IEGScriptIdentifier(scriptID, "Appeal", "V1");
    }
    return scriptIdentifier;
  }

  private long createAppealsDataStore(final String schemaName)
    throws InformationalException, AppException {

    final Datastore datastore = DatastoreHelper.openDatastore(schemaName);
    final long datastoreID = DatastoreHelper.createRootEntity(datastore);
    final Entity rootEntity = datastore.readEntity(datastoreID);
    final curam.participant.impl.ConcernRole role =
      this.concernRoleDAO.get(this.concernRoleID);

    this.dsPrepopulator.get().populate(rootEntity, role);
    final Entity[] people =
      rootEntity.getChildEntities(datastore.getEntityType("Person"));
    final Entity metadataEntity = datastore.newEntity("Metadata");
    metadataEntity.setTypedAttribute("hasMultipleCandidates",
      Boolean.valueOf(people.length > 1));
    rootEntity.addChildEntity(metadataEntity);

    return datastoreID;
  }
}
