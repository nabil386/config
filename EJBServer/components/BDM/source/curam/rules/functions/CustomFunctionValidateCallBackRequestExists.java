package curam.rules.functions;

import curam.ca.gc.bdm.entity.fact.BDMRequestForCallBackFactory;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackDtls;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMRequestForCallBackKeyStruct1;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.workflow.impl.ProcessInstanceAdmin;
import curam.util.workflow.struct.ProcessInstanceFullDetails;

public class CustomFunctionValidateCallBackRequestExists extends BDMFunctor {

  public CustomFunctionValidateCallBackRequestExists() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Custom function to pre populate the application details.
   *
   * @param rulesParameters the rules parameters
   */
  @Override
  public Adaptor getAdaptorValue(final RulesParameters rulesParameters)
    throws AppException, InformationalException {

    try {

      // get the datastore root entity identifier from the rules parameters
      final String programType = ((StringAdaptor) getParameters().get(0))
        .getStringValue(rulesParameters);

      // get the datastore root entity identifier from the rules parameters
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);

      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);

      final Entity personEntity = applicationEntity
        .getChildEntities(datastore.getEntityType("Person"))[0];

      final BDMRequestForCallBackKeyStruct1 requestForCallBackKeyStruct1 =
        new BDMRequestForCallBackKeyStruct1();
      requestForCallBackKeyStruct1.participantRoleID =
        Long.parseLong(personEntity.getAttribute("personID").toString());
      requestForCallBackKeyStruct1.programType = programType;

      final BDMRequestForCallBackDtlsList list =
        BDMRequestForCallBackFactory.newInstance()
          .searchByParticipantAndProgramType(requestForCallBackKeyStruct1);

      for (final BDMRequestForCallBackDtls requestForCallBackDtls : list.dtls) {
        final curam.util.workflow.struct.ProcessInstanceID processInstanceID =
          new curam.util.workflow.struct.ProcessInstanceID();

        processInstanceID.processInstanceID =
          requestForCallBackDtls.processInstanceID;
        final ProcessInstanceFullDetails processInstanceFullDetails =
          ProcessInstanceAdmin.readProcessInstance(processInstanceID);

        if ("INPROGRESS".equals(processInstanceFullDetails.status)) {
          return AdaptorFactory.getBooleanAdaptor(Boolean.FALSE);
        }
      }

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating application details - "
        + e.getMessage());
      e.printStackTrace();
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);

  }

}
