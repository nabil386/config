package curam.ca.gc.bdm.correspondenceframework.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMCCTSUBMITOPT;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLE;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLETYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.communication.impl.BDMCorrespondenceConstants;
import curam.ca.gc.bdm.communication.impl.BDMCorrespondenceProcessingCenters;
import curam.ca.gc.bdm.entity.bdmcct.fact.BDMCCTTemplateFactory;
import curam.ca.gc.bdm.entity.bdmcct.struct.BDMCCTTemplateName;
import curam.ca.gc.bdm.entity.communication.fact.BDMConcernRoleCommunicationFactory;
import curam.ca.gc.bdm.entity.communication.fact.BDMCorrespondenceStagingFactory;
import curam.ca.gc.bdm.entity.communication.intf.BDMCorrespondenceStaging;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMConcernRoleCommunicationKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDateAndTemplateName;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtls;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingDtlsList;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingJobID;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKey;
import curam.ca.gc.bdm.entity.communication.struct.BDMCorrespondenceStagingKeySearchDetails;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.communication.struct.BDMConcernRoleIDEvidenceIDAndNameDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMTemplateSearchDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOCCT;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl.BDMGenerateCorrespondenceMapper;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.AddressType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ClientType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ContactInformationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.DateType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.IdentificationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.LanguageType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ObjectFactory;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.PersonNameType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.RecipientType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.TextType;
import curam.ca.gc.bdm.sl.communication.impl.BDMCommunication;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.BDMCCTOutboundInterfaceImpl;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceRequest;
import curam.ca.gc.bdm.sl.interfaces.bdmcct.impl.outboundpojos.BDMCCTCreateCorrespondenceResponse;
import curam.ca.gc.bdm.util.impl.BDMDateUtil;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.COMMUNICATIONSTATUS;
import curam.codetable.CORRESPONDENCEJOBSTATUS;
import curam.codetable.FREQUENCYCODE;
import curam.codetable.LOCALE;
import curam.codetable.RECORDSTATUS;
import curam.codetable.VERIFICATIONSTATUS;
import curam.core.facade.infrastructure.struct.PersonAndEvidenceTypeList;
import curam.core.fact.PersonFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.sl.entity.struct.CaseParticipantRoleKey;
import curam.core.sl.entity.struct.ParticipantRoleIDAndNameDetails;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.RelatedIDStatusAndEvidenceTypeKey;
import curam.core.sl.intf.CaseParticipantRole;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonDtls;
import curam.core.struct.PersonKey;
import curam.core.struct.UniqueIDKeySet;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.RecordNotFoundException;
import curam.util.internal.resources.fact.AppResourceFactory;
import curam.util.internal.resources.struct.NSResourceDetailsList;
import curam.util.internal.resources.struct.ResourceName;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.resources.Trace;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.verification.sl.infrastructure.entity.fact.VDIEDLinkFactory;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkIDAndDataItemIDDetails;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkIDAndDataItemIDDetailsList;
import curam.verification.sl.infrastructure.entity.struct.VDIEDLinkKey;
import curam.verification.sl.infrastructure.entity.struct.VerificationStatusDetails;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;

public class BDMCorrespondenceTriggerUtil {

  @Inject
  BDMCommunicationHelper helper;

  @Inject
  BDMCorrespondenceProcessingCenters processingCenters;

  public BDMCorrespondenceTriggerUtil() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   *
   * Generates the staging records that will be processed by the
   * BDMGenerateCorrespondenceBatch batch.
   *
   * @param templateName
   * @param frequencyCode
   * @param correspondenceRecipientClientInput
   * @param inputData
   * @throws Exception
   */
  public List<Long> generateCorrespondenceForBatch(
    final java.lang.String templateName, final java.lang.String frequencyCode,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final Object inputData) {

    final List<Long> returnStagingIDList = new ArrayList<>();
    List<Long> toRecipientConcernRoleIDList = new ArrayList<>();

    long correspondenceStagingID = 0;
    BDMLetterTemplateConfig configDtls = null;
    String countryCode = "";
    try {
      // Get the list of recipients
      if (correspondenceRecipientClientInput.clientConcernRoleID != 0) {
        toRecipientConcernRoleIDList = getRecipientsList(
          correspondenceRecipientClientInput.clientConcernRoleID);
      }

      // If there are no recipients
      if (toRecipientConcernRoleIDList != null
        && toRecipientConcernRoleIDList.isEmpty()) {
        final String errorMessage = "Client is excluded " + templateName;
        Trace.kTopLevelLogger.error(errorMessage);
        returnStagingIDList.add(correspondenceStagingID);
        return returnStagingIDList;
      }

      configDtls = getTemplateConfiguration(templateName);

      if (!isEnabled(configDtls)) {
        final String errorMessage =
          "Template is not enabled for automated correspondence : "
            + templateName;
        Trace.kTopLevelLogger.error(errorMessage);
        returnStagingIDList.add(correspondenceStagingID);
        return returnStagingIDList;
      }

      InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();

      validateTemplateContext(configDtls, correspondenceRecipientClientInput,
        informationalManager);

      // Get the data through the configured sql or through the configured handler
      final Object dataObject = getData(configDtls, inputData);

      final BDMLetterTemplateTombstoneData tombstoneData =
        new BDMLetterTemplateTombstoneData();

      setTombstoneDataForClientForBatch(
        correspondenceRecipientClientInput.clientConcernRoleID,
        tombstoneData);

      // Check for missing mandatory fields and other validations
      // Note: dataObject could be null if only tombstone data is required
      // The handler should make appropriate checks if the template requires data or
      // not
      informationalManager = validateData(configDtls, dataObject,
        correspondenceRecipientClientInput,
        TransactionInfo.getInformationalManager());

      // TODO Loop toRecipientConcernRoleIDList
      for (final long toRecipientConcernRoleID : toRecipientConcernRoleIDList) {

        // Common validations
        // Set the country code
        countryCode =
          getCountryCode(toRecipientConcernRoleID, informationalManager);

        if (informationalManager.operationHasInformationals()) {

          final StringBuilder validationMessages = new StringBuilder();
          for (final InformationalElement informationalElement : informationalManager
            .getMessagesForField("")) {

            validationMessages.append(informationalElement
              .getElementLocalisableString().getMessage(LOCALE.ENGLISH));
            validationMessages.append(
              BDMCorrespondenceConstants.kValidationMessagesSeperator);
            validationMessages.append(informationalElement
              .getElementLocalisableString().getMessage(LOCALE.FRENCH));
            validationMessages.append(BDMConstants.kNewLine);
          }

          // If missing fields are present
          // Create a failed record
          correspondenceStagingID =
            createOrUpdateStagedRecord(configDtls, frequencyCode,
              correspondenceRecipientClientInput, toRecipientConcernRoleID,
              CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO, null, inputData,
              validationMessages.toString(), countryCode);

          final String errorMessage =
            "Correspondence staging ID: " + correspondenceStagingID
              + ", Error message: " + validationMessages;
          Trace.kTopLevelLogger.error(errorMessage);

          // reset informational manager
          TransactionInfo.setInformationalManager();
          returnStagingIDList.add(correspondenceStagingID);
          return returnStagingIDList;
        }

        // Get the xml template from configuration
        String finalXML = getTemplateXML(configDtls.templateName);

        setTombstoneDataForRecipient(toRecipientConcernRoleID, tombstoneData);

        if (dataObject != null) {
          finalXML =
            BDMCorrespondenceTriggerUtil.mapToXML(dataObject, finalXML);
        }

        // Now replace the tombstone attributes in the finalXML
        finalXML =
          BDMCorrespondenceTriggerUtil.mapToXML(tombstoneData, finalXML);

        correspondenceStagingID = createOrUpdateStagedRecord(configDtls,
          frequencyCode, correspondenceRecipientClientInput,
          toRecipientConcernRoleID, CORRESPONDENCEJOBSTATUS.PENDING, finalXML,
          inputData, "", countryCode);
        returnStagingIDList.add(correspondenceStagingID);

      }
    } catch (final AppException | InformationalException e) {
      Trace.kTopLevelLogger.info("Automated correspondence failed");

      try {
        // Create a failed record
        correspondenceStagingID = createOrUpdateStagedRecord(configDtls,
          frequencyCode, correspondenceRecipientClientInput,
          correspondenceRecipientClientInput.toRecipientConcernRoleID,
          CORRESPONDENCEJOBSTATUS.FAILED, null, inputData,
          e.getLocalizedMessage(), countryCode);
        returnStagingIDList.add(correspondenceStagingID);
        final String errorMessage =
          "For the correspondence staging ID: " + correspondenceStagingID
            + ", the error is: " + e.getLocalizedMessage();
        Trace.kTopLevelLogger.error(errorMessage);

      } catch (final AppException | InformationalException expection) {
        expection.printStackTrace();
      }
      e.printStackTrace();
    }

    return returnStagingIDList;
  }

  /**
   * Based on the client concern role ID derive the recipients.
   *
   * @param clientConcernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private List<Long> getRecipientsList(final long clientConcernRoleID)
    throws AppException, InformationalException {

    final List<Long> toRecipientConcernRoleIDList = new ArrayList<>();

    final List<Long> trusteeConcernRoleIDList = new ArrayList<>();
    final List<Long> executorConcernRoleIDList = new ArrayList<>();
    final List<Long> thirdPartyConcernRoleIDList = new ArrayList<>();

    final List<Long> organizationConcernRoleIDList = new ArrayList<>();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();

    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    final PersonAndEvidenceTypeList personKey =
      new PersonAndEvidenceTypeList();
    personKey.concernRoleID = clientConcernRoleID;
    personKey.evidenceTypeList = CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
    pdcEvidenceList =
      pdcPersonObj.listCurrentParticipantEvidenceByTypes(personKey);

    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {

      final BDMConcernRoleIDEvidenceIDAndNameDetails concernRoleIDAndNameDetails =
        new BDMConcernRoleIDEvidenceIDAndNameDetails();

      // Check if the evidence is verified
      // Get the evidence descriptor id from evidence id
      final RelatedIDStatusAndEvidenceTypeKey relatedIDStatusAndEvidenceTypeKey =
        new RelatedIDStatusAndEvidenceTypeKey();
      relatedIDStatusAndEvidenceTypeKey.relatedID =
        pdcEvidenceList.list.item(i).evidenceID;
      relatedIDStatusAndEvidenceTypeKey.evidenceType =
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
      relatedIDStatusAndEvidenceTypeKey.statusCode = RECORDSTATUS.NORMAL;

      final RelatedIDAndEvidenceTypeKey relatedIDAndEvidenceTypeKey =
        new RelatedIDAndEvidenceTypeKey();

      relatedIDAndEvidenceTypeKey.relatedID =
        pdcEvidenceList.list.item(i).evidenceID;
      relatedIDAndEvidenceTypeKey.evidenceType =
        CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT;
      final EvidenceDescriptorDtlsList descriptorDtlsList =
        EvidenceDescriptorFactory.newInstance()
          .searchByRelatedIDAndEvidenceType(relatedIDAndEvidenceTypeKey);

      for (final EvidenceDescriptorDtls evidenceDescriptorDtls : descriptorDtlsList.dtls) {

        // Get the VDIEDLinkID from evidence descriptor id
        final EvidenceDescriptorKey evidenceDescriptorKey =
          new EvidenceDescriptorKey();
        evidenceDescriptorKey.evidenceDescriptorID =
          evidenceDescriptorDtls.evidenceDescriptorID;
        final VDIEDLinkIDAndDataItemIDDetailsList vdiedLinkIDAndDataItemIDDetailsList =
          VDIEDLinkFactory.newInstance()
            .readByEvidenceDescriptorID(evidenceDescriptorKey);

        // Check if the evidence is verified based on VDIEDLinkID
        for (final VDIEDLinkIDAndDataItemIDDetails vdiedLinkIDAndDataItemIDDetails : vdiedLinkIDAndDataItemIDDetailsList.dtls) {
          final VDIEDLinkKey vdiedLinkKey = new VDIEDLinkKey();
          vdiedLinkKey.VDIEDLinkID =
            vdiedLinkIDAndDataItemIDDetails.vDIEDLinkID;
          final VerificationStatusDetails verificationStatusDetails =
            curam.verification.sl.infrastructure.fact.VerificationFactory
              .newInstance().getVerificationStatus(vdiedLinkKey);

          if (VERIFICATIONSTATUS.VERIFIED
            .equalsIgnoreCase(verificationStatusDetails.verificationStatus)) {

            final String fromDateStr = BDMEvidenceUtil.getDynEvdAttrValue(
              pdcEvidenceList.list.item(i).evidenceID,
              CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
              BDMConstants.kBDMDynEvdAttrNameFrom);
            final String toDateStr = BDMEvidenceUtil.getDynEvdAttrValue(
              pdcEvidenceList.list.item(i).evidenceID,
              CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
              BDMConstants.kBDMDynEvdAttrNameTo);
            final Date fromDate = fromDateStr.isEmpty() ? Date.kZeroDate
              : Date.getDate(fromDateStr);
            final Date toDate =
              toDateStr.isEmpty() ? Date.kZeroDate : Date.getDate(toDateStr);

            // Check if the record is within the date range
            if (BDMDateUtil.isDateBetween(Date.getCurrentDate(), fromDate,
              toDate)) {

              // Get the case participant role id
              final String thirdPartyCaseParticipantRoleID = BDMEvidenceUtil
                .getDynEvdAttrValue(pdcEvidenceList.list.item(i).evidenceID,
                  CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
                  BDMConstants.kBDMDynEvdAttrNameThirdPartyCaseParticipantRole);

              final CaseParticipantRole caseParticipantRoleObj =
                CaseParticipantRoleFactory.newInstance();

              final CaseParticipantRoleKey caseParticipantRoleKey =
                new CaseParticipantRoleKey();
              caseParticipantRoleKey.caseParticipantRoleID =
                Long.parseLong(thirdPartyCaseParticipantRoleID);

              // Get the concernrole ID and concernrole name
              final ParticipantRoleIDAndNameDetails participantRoleIDAndNameDetails =
                caseParticipantRoleObj.readParticipantRoleIDAndParticpantName(
                  caseParticipantRoleKey);

              concernRoleIDAndNameDetails.concernRoleName =
                participantRoleIDAndNameDetails.name;
              concernRoleIDAndNameDetails.concernRoleID =
                participantRoleIDAndNameDetails.participantRoleID;

              final String role = BDMEvidenceUtil.getDynEvdAttrValue(
                pdcEvidenceList.list.item(i).evidenceID,
                CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
                BDMConstants.kBDMDynEvdAttrNameRole);

              final String roleType = BDMEvidenceUtil.getDynEvdAttrValue(
                pdcEvidenceList.list.item(i).evidenceID,
                CASEEVIDENCE.BDM_THIRD_PARTY_CONTACT,
                BDMConstants.kBDMDynEvdAttrNameRoleType);

              if (BDMTHIRDPARTYROLE.EXECUTOR.equalsIgnoreCase(role)) {
                executorConcernRoleIDList
                  .add(concernRoleIDAndNameDetails.concernRoleID);

              } else if (BDMTHIRDPARTYROLE.BP_PT.equalsIgnoreCase(role)) {
                trusteeConcernRoleIDList
                  .add(concernRoleIDAndNameDetails.concernRoleID);

              } else if (BDMTHIRDPARTYROLETYPE.ORGANIZATION
                .equalsIgnoreCase(roleType)) {
                // TODO This might have to be read from BDMThirdPartyCorrespondence

                organizationConcernRoleIDList
                  .add(concernRoleIDAndNameDetails.concernRoleID);

              } else {
                thirdPartyConcernRoleIDList
                  .add(concernRoleIDAndNameDetails.concernRoleID);

              }
            }
          }
        }
      }
    }

    final boolean isDeceased = isDeceased(clientConcernRoleID);
    final boolean isClientOnMailExclusionList =
      isClientOnMailExclusionList(clientConcernRoleID);
    final boolean isClientOnFraudList =
      isClientOnMailExclusionList(clientConcernRoleID);

    final boolean isClientHavingExecutor =
      !executorConcernRoleIDList.isEmpty() ? true : false;
    final boolean isClientHavingTrustee =
      !trusteeConcernRoleIDList.isEmpty() ? true : false;
    final boolean isThirdPartyRoleTypeOrganization =
      !organizationConcernRoleIDList.isEmpty() ? true : false;

    final boolean isClientRequestCorrespondenceForClientAndThirdParty =
      isClientRequestCorrespondenceForClientAndThirdParty(
        clientConcernRoleID);
    final boolean isClientRequestCorrespondenceForThirdPartyOnly =
      isClientRequestCorrespondenceForThirdPartyOnly(clientConcernRoleID);
    final boolean isClientRequestCorrespondenceForClientOnly =
      isClientRequestCorrespondenceForClientOnly(clientConcernRoleID);

    if (isDeceased && !isClientHavingExecutor) {
      // Exclude
      return toRecipientConcernRoleIDList;

    } else if (isDeceased && isClientHavingExecutor
      && !isClientOnMailExclusionList && !isClientOnFraudList) {
      for (final long executorConcernRoleID : executorConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(executorConcernRoleID);
      }

    } else if (isClientOnMailExclusionList || isClientOnFraudList) {
      // Exclude
      return toRecipientConcernRoleIDList;

    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && !isThirdPartyRoleTypeOrganization
      && !isClientRequestCorrespondenceForClientAndThirdParty
      && isClientRequestCorrespondenceForThirdPartyOnly) {
      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);

    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList
      // && !isClientHavingExecutor
      && isClientHavingTrustee && !isThirdPartyRoleTypeOrganization) {
      // Client is trustee
      for (final long trusteeConcernRoleID : trusteeConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(trusteeConcernRoleID);
      }
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && isThirdPartyRoleTypeOrganization
      && isClientRequestCorrespondenceForClientAndThirdParty
      && !isClientRequestCorrespondenceForThirdPartyOnly
      && !isClientRequestCorrespondenceForClientOnly) {
      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);

      // Third party is recipient
      for (final long thirdPartyConcernRoleID : thirdPartyConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(thirdPartyConcernRoleID);
      }
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && isThirdPartyRoleTypeOrganization
      && !isClientRequestCorrespondenceForClientAndThirdParty
      && isClientRequestCorrespondenceForThirdPartyOnly
      && !isClientRequestCorrespondenceForClientOnly) {

      // Third party is recipient
      for (final long thirdPartyConcernRoleID : thirdPartyConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(thirdPartyConcernRoleID);
      }
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && isThirdPartyRoleTypeOrganization
      && !isClientRequestCorrespondenceForClientAndThirdParty
      && !isClientRequestCorrespondenceForThirdPartyOnly
      && isClientRequestCorrespondenceForClientOnly) {

      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);

    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && !isThirdPartyRoleTypeOrganization
      && isClientRequestCorrespondenceForClientAndThirdParty
      && !isClientRequestCorrespondenceForThirdPartyOnly
      && !isClientRequestCorrespondenceForClientOnly) {

      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);

      // Third party is recipient
      for (final long thirdPartyConcernRoleID : thirdPartyConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(thirdPartyConcernRoleID);
      }
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && !isThirdPartyRoleTypeOrganization
      && isClientRequestCorrespondenceForClientAndThirdParty
      && isClientRequestCorrespondenceForThirdPartyOnly
      && !isClientRequestCorrespondenceForClientOnly) {

      // Third party is recipient
      for (final long thirdPartyConcernRoleID : thirdPartyConcernRoleIDList) {
        toRecipientConcernRoleIDList.add(thirdPartyConcernRoleID);
      }
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && !isThirdPartyRoleTypeOrganization
      && !isClientRequestCorrespondenceForClientAndThirdParty
      && !isClientRequestCorrespondenceForThirdPartyOnly
      && isClientRequestCorrespondenceForClientOnly) {

      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);
    } else if (!isDeceased && !isClientOnMailExclusionList
      && !isClientOnFraudList && !isClientHavingExecutor
      && !isClientHavingTrustee && !isThirdPartyRoleTypeOrganization) {

      // This is added extra if no other conditions are met and if there is not Third
      // party correspondence present
      // Client is recipient
      toRecipientConcernRoleIDList.add(clientConcernRoleID);
    }
    return toRecipientConcernRoleIDList;
  }

  private boolean isClientRequestCorrespondenceForClientOnly(
    final long clientConcernRoleID) {

    // TODO Auto-generated method stub
    return false;
  }

  private boolean isClientRequestCorrespondenceForClientAndThirdParty(
    final long clientConcernRoleID) {

    // TODO Auto-generated method stub
    return false;
  }

  private boolean isClientRequestCorrespondenceForThirdPartyOnly(
    final long clientConcernRoleID) {

    // TODO Auto-generated method stub
    return false;
  }

  private boolean
    isClientOnMailExclusionList(final long clientConcernRoleID) {

    // TODO Auto-generated method stub
    return false;
  }

  private boolean isDeceased(final long clientConcernRoleID)
    throws AppException, InformationalException {

    // TODO check if this is right or if we have to use dynamic evidence
    boolean isDeceased = false;
    final NotFoundIndicator nfInd = new NotFoundIndicator();
    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = clientConcernRoleID;
    final PersonDtls personDtls =
      PersonFactory.newInstance().read(nfInd, personKey);
    if (!nfInd.isNotFound() && personDtls.dateOfDeath != null
      && !personDtls.dateOfDeath.isZero()) {
      isDeceased = true;
    }
    return isDeceased;
  }

  private String getCountryCode(final long toRecipientConcernRoleID,
    final InformationalManager informationalManager)
    throws InformationalException {

    String countryCode = "";
    try {
      final long addressID =
        helper.getAddressIDforConcern(toRecipientConcernRoleID, false);
      final Map<String, String> addressElmMap =
        processingCenters.getAddressElementMap(addressID);

      countryCode = addressElmMap.get(BDMConstants.kADDRESSELEMENT_COUNTRY);

    } catch (final AppException e) {

      informationalManager.addInformationalMsg(e, "",
        InformationalElement.InformationalType.kError);
    }
    return countryCode;
  }

  private void setTombstoneDataForClientForBatch(
    final long clientConcernRoleID,
    final BDMLetterTemplateTombstoneData tombstoneData)
    throws AppException, InformationalException {

    final BDMCorrespondenceDetails corresDetails =
      new BDMCorrespondenceDetails();
    corresDetails.concernRoleID = clientConcernRoleID;
    new BDMCommunication().setClientMandatoryFields(corresDetails);
    tombstoneData.clientXML = getTombstoneDataClientXML(corresDetails);
  }

  public void setTombstoneDataForRecipient(final long recipientConcernRoleID,
    final BDMLetterTemplateTombstoneData tombstoneData)
    throws AppException, InformationalException {

    tombstoneData.toCorrespondenceRecipientXML =
      getToCorrespondentXML(recipientConcernRoleID);
  }

  public static void setTombstoneDataForDocumentIdentification(
    final Long trackingNumber,
    final BDMLetterTemplateTombstoneData tombstoneData) {

    tombstoneData.documentIdentificationXML =
      getDocumentIdentificationXML(trackingNumber);

  }

  private static java.lang.String
    getDocumentIdentificationXML(final Long trackingNumber) {

    JAXBContext context;
    java.lang.String mappedData = "";
    try {
      context = JAXBContext.newInstance(IdentificationType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING,
        BDMCorrespondenceConstants.kJAXBEncoding);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      // root element
      final IdentificationType documentIdentification =
        new ObjectFactory().createIdentificationType();
      final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String identificationID =
        new curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String();
      // BUG-91513, Start
      // Set the tracking number for document identification
      identificationID.setValue(Long.toString(trackingNumber));
      documentIdentification.getIdentificationID().add(identificationID);
      final TextType identificationCategoryText = new TextType();
      identificationCategoryText
        .setValue(BDMConstants.kGenerateCorrespondenceTrackingNumber);
      // BUG-91513, End
      documentIdentification.getIdentificationCategoryText()
        .add(identificationCategoryText);

      final StringWriter sw = new StringWriter();
      final JAXBElement<IdentificationType> je =
        new JAXBElement<>(new QName("", "ns2:DocumentIdentification"),
          IdentificationType.class, documentIdentification);
      marshaller.marshal(je, sw);
      mappedData = sw.toString();
    } catch (final JAXBException e) {
      e.printStackTrace();
    }
    return mappedData;

  }

  /**
   * Check if it is enabled at the top level property and at template level
   *
   * @param configDtls
   * @return
   */
  public static boolean isEnabled(final BDMLetterTemplateConfig configDtls) {

    boolean isEnabled = true;
    final boolean propertyLevelEnabledInd = Configuration
      .getBooleanProperty(EnvVars.BDM_AUTOMATED_CORRESPONDENCE_ENABLED);
    if (!propertyLevelEnabledInd || !configDtls.enabledInd) {
      isEnabled = false;
    }
    return isEnabled;

  }

  /*
   * Re trigger the generate correspondence for a particular staged record.
   * This could be because of change of circumstance or it could have failed the
   * initial validation.
   *
   */
  public void
    reTriggerGenerateCorrespondence(final long correspondenceStagingID) {

    try {
      // Read the staging record
      final BDMCorrespondenceStaging correspondenceStagingObj =
        BDMCorrespondenceStagingFactory.newInstance();

      final BDMCorrespondenceStagingKey stagingKey =
        new BDMCorrespondenceStagingKey();
      stagingKey.correspondenceStagingID = correspondenceStagingID;
      final BDMCorrespondenceStagingDtls correspondenceStagingDtls =
        correspondenceStagingObj.read(stagingKey);

      final java.lang.String templateConfigJSON =
        getTemplateConfigJSON(correspondenceStagingDtls.templateName);

      final ObjectMapper mapper = new ObjectMapper();
      final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls =
        mapper.readValue(templateConfigJSON,
          curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig.class);

      final Object sqlInput =
        mapper.readValue(correspondenceStagingDtls.inputData,
          Class.forName(configDtls.inputClassName));

      final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
        new BDMCorrespondenceRecipientClientInput();
      correspondenceRecipientClientInput.caseID =
        correspondenceStagingDtls.caseID;
      correspondenceRecipientClientInput.toRecipientConcernRoleID =
        correspondenceStagingDtls.recipientConcernRoleID;
      correspondenceRecipientClientInput.clientConcernRoleID =
        correspondenceStagingDtls.clientConcernRoleID;

      InformationalManager informationalManager =
        TransactionInfo.getInformationalManager();
      validateTemplateContext(configDtls, correspondenceRecipientClientInput,
        informationalManager);

      // Get the data
      final Object dataObject = getData(configDtls, sqlInput);

      // Check for missing mandatory fields and other validations
      // Note: dataObject could be null if only tombstone data is required
      // The handler should make appropriate checks if the template requires data or
      // not
      informationalManager = validateData(configDtls, dataObject,
        correspondenceRecipientClientInput,
        TransactionInfo.getInformationalManager());

      if (informationalManager.operationHasInformationals()) {
        final StringBuilder validationMessages = new StringBuilder();
        for (final InformationalElement informationalElement : informationalManager
          .getMessagesForField("")) {

          validationMessages.append(informationalElement
            .getElementLocalisableString().getMessage(LOCALE.ENGLISH));
          validationMessages
            .append(BDMCorrespondenceConstants.kValidationMessagesSeperator);
          validationMessages.append(informationalElement
            .getElementLocalisableString().getMessage(LOCALE.FRENCH));
          validationMessages.append(BDMConstants.kNewLine);
        }
        // If missing fields are present update to FAILED
        if (!correspondenceStagingDtls.status
          .equalsIgnoreCase(CORRESPONDENCEJOBSTATUS.FAILED)) {
          correspondenceStagingDtls.payLoad = null;
          correspondenceStagingDtls.status =
            CORRESPONDENCEJOBSTATUS.MISSINGMANDATORYINFO;
          correspondenceStagingDtls.validationMessages =
            validationMessages.toString();
          correspondenceStagingObj.modify(stagingKey,
            correspondenceStagingDtls);
        }
        final String errorMessage =
          "For the correspondence staging ID: " + correspondenceStagingID
            + ", these are error messages: " + validationMessages.toString();
        Trace.kTopLevelLogger.error(errorMessage);

        // reset informational manager
        TransactionInfo.setInformationalManager();

      } else if (!informationalManager.operationHasInformationals()) {

        final BDMLetterTemplateTombstoneData tombstoneData =
          new BDMLetterTemplateTombstoneData();
        setTombstoneDataForClientForBatch(
          correspondenceRecipientClientInput.clientConcernRoleID,
          tombstoneData);

        // Get the xml template from configuration
        String finalXML = getTemplateXML(configDtls.templateName);

        setTombstoneDataForRecipient(
          correspondenceRecipientClientInput.toRecipientConcernRoleID,
          tombstoneData);

        if (dataObject != null) {
          BDMCorrespondenceTriggerUtil.mapToXML(dataObject, finalXML);
        }

        // Now replace the tombstone attributes in the finalXML
        finalXML =
          BDMCorrespondenceTriggerUtil.mapToXML(tombstoneData, finalXML);

        final BDMCorrespondenceStaging bdmoasCorrespondenceStagingObj =
          BDMCorrespondenceStagingFactory.newInstance();

        // Modify the staged record
        correspondenceStagingDtls.validationMessages = "";
        correspondenceStagingDtls.payLoad = finalXML;
        correspondenceStagingDtls.status = CORRESPONDENCEJOBSTATUS.PENDING;
        correspondenceStagingDtls.triggerDate = DateTime.getCurrentDateTime();

        bdmoasCorrespondenceStagingObj.modify(stagingKey,
          correspondenceStagingDtls);

      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info("Automated correspondence failed");
      e.printStackTrace();
    }
  }

  /**
   * Called from the wizard for manual letters
   *
   * @param templateName
   * @param frequencyCode
   * @param concernRoleIDCaseIDKey
   * @param inputData
   * @throws Exception
   */
  public static BDMTemplateDetails generateCorrespondenceForRealTime(
    final java.lang.String templateName,
    final BDMCorrespondenceDetails corresDetails, final Object inputData)
    throws AppException, InformationalException {

    final BDMLetterTemplateConfig configDtls =
      getTemplateConfiguration(templateName);

    if (configDtls == null) {
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput =
      new BDMCorrespondenceRecipientClientInput();
    correspondenceRecipientClientInput.caseID = corresDetails.caseID;
    correspondenceRecipientClientInput.toRecipientConcernRoleID =
      corresDetails.toParticipantRoleID;
    correspondenceRecipientClientInput.toRecipientThirdPartyContactConcernRoleID =
      corresDetails.toContactThirdPartyConcernRoleID;
    correspondenceRecipientClientInput.toRecipientUnverifiedContactConcernRoleID =
      corresDetails.toContactConcernRoleID;

    correspondenceRecipientClientInput.clientConcernRoleID =
      corresDetails.concernRoleID;
    correspondenceRecipientClientInput.ccRecipientConcernRoleID =
      corresDetails.ccThirdPartyContactConcernRoleID;
    InformationalManager informationalManager =
      validateTemplateContext(configDtls, correspondenceRecipientClientInput,
        TransactionInfo.getInformationalManager());

    informationalManager.failOperation();

    final Object dataObject = getData(configDtls, inputData);

    informationalManager =
      validateData(configDtls, dataObject, correspondenceRecipientClientInput,
        TransactionInfo.getInformationalManager());
    final BDMCCTCreateCorrespondenceRequest request =
      new BDMCCTCreateCorrespondenceRequest();

    informationalManager.failOperation();

    request.setAutoCloseEditor(false);
    request.setCommunity(
      Configuration.getProperty(EnvVars.BDM_CCT_API_COMMUNITY_VALUE));

    request.setDataMapName(BDMConstants.kCCTDataMapName);
    request.setTemplateID(corresDetails.templateID);
    request.setTemplateFullPath(corresDetails.templatePath);

    // insert correspondence record in Curam database
    final BDMCommunication bdmCommunication = new BDMCommunication();
    corresDetails.communicationID = bdmCommunication
      .insertCorrespondenceDetails(corresDetails, COMMUNICATIONSTATUS.DRAFT);

    final BDMConcernRoleCommunicationKey bdmCCKey =
      new BDMConcernRoleCommunicationKey();
    bdmCCKey.communicationID = corresDetails.communicationID;
    final BDMConcernRoleCommunicationDtls bdmCCDtls =
      BDMConcernRoleCommunicationFactory.newInstance().read(bdmCCKey);
    corresDetails.trackingNumber = bdmCCDtls.trackingNumber;

    try {

      final BDMLetterTemplateTombstoneData tombstoneData =
        getTombstoneDataForRealTime(corresDetails);

      final java.lang.String templateXML = getTemplateXML(templateName);
      final StringBuilder xmlBuilder = new StringBuilder();
      xmlBuilder.append(
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

      // dataObject could be null if we need only tombstone data
      if (dataObject != null) {
        xmlBuilder.append(
          BDMCorrespondenceTriggerUtil.mapToXML(dataObject, templateXML));
      } else {
        xmlBuilder.append(templateXML);
      }
      // Now replace the tombstone attributes in the finalXML
      final String finalDataXML = BDMCorrespondenceTriggerUtil
        .mapToXML(tombstoneData, xmlBuilder.toString());
      request.setDataXML(finalDataXML);
    } catch (final Exception e) {

      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }

    request.setInitialAssigneeName("bryan.misch");
    request.setRedirectToAwaitingDelivery(true);

    request.setSubmitOnCreate(false);
    request.setDeliveryOptionName(BDMConstants.kDeliveryOptionLocalPrint);
    request.setOnlyArchive(corresDetails.isDigitalInd);
    request.setEditorRedirectURL(BDMConstants.EMPTY_STRING);
    if (BDMCCTSUBMITOPT.MODIFY.equalsIgnoreCase(corresDetails.submitOpt)) {

      request.setCorrespondenceMode(BDMConstants.kEdit);

    } else {

      request.setCorrespondenceMode(BDMConstants.kReview);
    }

    // set the UserName - should be in the format of CURAM-<loggedInUserName>
    request.setUserID(BDMConstants.kEventSource + BDMConstants.gkHypen
      + TransactionInfo.getProgramUser());

    // call the interface to send the request
    final BDMCCTCreateCorrespondenceResponse response =
      new BDMCCTOutboundInterfaceImpl().createCorrespondence(request);

    if (response != null) {
      bdmCommunication.saveCCTCorrespondenceResponce(response, corresDetails);
      final BDMTemplateDetails bdmTemplateDetails = new BDMTemplateDetails();
      bdmTemplateDetails.cctUrl = response.getWorkItemURL();
      bdmTemplateDetails.workItemID = response.getWorkItemID();
      // BUG-91984, Start
      final String cctStatus = response.getStatus();
      if (!StringUtil.isNullOrEmpty(cctStatus)) {
        bdmTemplateDetails.cctStatus = cctStatus;
      }
      // BUG-91984, End
      return bdmTemplateDetails;
    } else {
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }

  }

  private static BDMLetterTemplateTombstoneData getTombstoneDataForRealTime(
    final BDMCorrespondenceDetails corresDetails) throws AppException {

    final BDMLetterTemplateTombstoneData tombstoneData =
      new BDMLetterTemplateTombstoneData();
    tombstoneData.toCorrespondenceRecipientXML =
      getToCorrespondenceRecipientXMLForRealTime(corresDetails);

    // For real time only verified third party contact can be set as CC
    if (corresDetails.ccThirdPartyContactConcernRoleID != 0) {
      tombstoneData.ccCorrespondenceRecipientXML =
        getCCCorrespondenceRecipientXMLForRealTime(corresDetails);
    }

    tombstoneData.clientXML = getTombstoneDataClientXML(corresDetails);

    tombstoneData.documentIdentificationXML =
      getDocumentIdentificationXML(corresDetails.trackingNumber);

    return tombstoneData;
  }

  private static String getCCCorrespondenceRecipientXMLForRealTime(
    final BDMCorrespondenceDetails bdmCorrespondenceDetails) {

    JAXBContext context;
    java.lang.String mappedData = "";
    try {
      context = JAXBContext.newInstance(RecipientType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING,
        BDMCorrespondenceConstants.kJAXBEncoding);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      final RecipientType recipientCC =
        new ObjectFactory().createRecipientType();
      final AddressType recipientCCAddressType =
        BDMGenerateCorrespondenceMapper.getAddress(
          bdmCorrespondenceDetails.ccDetails.ccAddressLineOne,
          bdmCorrespondenceDetails.ccDetails.ccAddressLineTwo,
          bdmCorrespondenceDetails.ccDetails.ccAddressLineThree,
          bdmCorrespondenceDetails.ccDetails.ccAddressLineFour,
          bdmCorrespondenceDetails.ccDetails.ccUnparsedAddressInd);

      recipientCC.setRecipientAddress(recipientCCAddressType);

      final TextType recipientCCCategoryCode = new TextType();
      recipientCCCategoryCode
        .setValue(BDMConstants.kGenerateCorrespondenceXMLCC);
      recipientCC.setRecipientCategoryCode(recipientCCCategoryCode);
      final TextType recipientCCContactCode = new TextType();
      recipientCCContactCode
        .setValue(bdmCorrespondenceDetails.ccRecipientContactCode);
      recipientCC.setRecipientContactCode(recipientCCContactCode);
      final TextType recipientCCName = new TextType();
      recipientCCName
        .setValue(bdmCorrespondenceDetails.ccCorrespondentName.toUpperCase());
      recipientCC.setRecipientName(recipientCCName);

      final LanguageType recipientCCLanguage = new LanguageType();
      final TextType langNameCC = new TextType();
      langNameCC
        .setValue(bdmCorrespondenceDetails.ccContactPreferredLanguage);
      recipientCCLanguage.getLanguageName().add(langNameCC);
      recipientCC.setRecipientPreferredLanguage(recipientCCLanguage);

      final StringWriter sw = new StringWriter();
      marshaller.marshal(recipientCC, sw);
      mappedData = sw.toString();
    } catch (final Exception e) {

      e.printStackTrace();

    }
    return mappedData;
  }

  private static String getToCorrespondenceRecipientXMLForRealTime(
    final BDMCorrespondenceDetails corresDetails) {

    JAXBContext context;
    java.lang.String mappedData = "";
    try {
      context = JAXBContext.newInstance(RecipientType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING,
        BDMCorrespondenceConstants.kJAXBEncoding);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      // root element
      // Client
      // CorrespondenceRecipient TO
      final RecipientType recipientTo =
        new ObjectFactory().createRecipientType();
      final AddressType recipientToAddressType =
        BDMGenerateCorrespondenceMapper.getAddress(
          corresDetails.toDetails.addressLineOne,
          corresDetails.toDetails.addressLineTwo,
          corresDetails.toDetails.addressLineThree,
          corresDetails.toDetails.addressLineFour,
          corresDetails.toDetails.unparsedAddressInd);

      recipientTo.setRecipientAddress(recipientToAddressType);
      final TextType recipientToCategoryCode = new TextType();
      recipientToCategoryCode
        .setValue(BDMConstants.kGenerateCorrespondenceXMLTo);
      recipientTo.setRecipientCategoryCode(recipientToCategoryCode);
      final TextType recipientToContactCode = new TextType();
      recipientToContactCode.setValue(corresDetails.toRecipientContactCode);
      recipientTo.setRecipientContactCode(recipientToContactCode);
      final TextType recipientToName = new TextType();
      // TASK-94808, Start
      // Make names upper case in the payload
      recipientToName
        .setValue(corresDetails.toCorrespondentName.toUpperCase());
      recipientTo.setRecipientName(recipientToName);

      final LanguageType recipientLanguage = new LanguageType();
      final TextType langName = new TextType();
      langName.setValue(corresDetails.toParticipantPreferredLanguage);
      recipientLanguage.getLanguageName().add(langName);
      recipientTo.setRecipientPreferredLanguage(recipientLanguage);

      final StringWriter sw = new StringWriter();
      final JAXBElement<RecipientType> je =
        new JAXBElement<>(new QName("", "ns3:CorrespondenceRecipient"),
          RecipientType.class, recipientTo);
      marshaller.marshal(je, sw);

      mappedData = sw.toString();
    } catch (final Exception e) {

      e.printStackTrace();

    }
    return mappedData;
  }

  private static java.lang.String getTombstoneDataClientXML(
    final BDMCorrespondenceDetails bdmCorrespondenceDetails)
    throws AppException {

    JAXBContext context;
    java.lang.String mappedData = "";
    try {
      context = JAXBContext.newInstance(ClientType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING,
        BDMCorrespondenceConstants.kJAXBEncoding);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      // root element
      final ClientType client = new ObjectFactory().createClientType();

      // ClientIdentification 1
      if (bdmCorrespondenceDetails.toClientIsCorrespondent
        || CuramConst.gkZero != bdmCorrespondenceDetails.toContactThirdPartyConcernRoleID) {

        final IdentificationType clientIdentification1 =
          new IdentificationType();
        final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String identificationID1 =
          new curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String();
        identificationID1
          .setValue(bdmCorrespondenceDetails.clientDtls.clientID);
        clientIdentification1.getIdentificationID().add(identificationID1);
        final TextType identificationCategoryText1 = new TextType();
        identificationCategoryText1.setValue("Client ID");
        clientIdentification1.getIdentificationCategoryText()
          .add(identificationCategoryText1);

        client.getClientIdentification().add(clientIdentification1);
      }

      // set person names
      final PersonNameType personNameType = new PersonNameType();
      final TextType fullName = new TextType();
      fullName.setValue(bdmCorrespondenceDetails.clientName.toUpperCase());
      personNameType.getPersonFullName().add(fullName);
      client.getPersonName().add(personNameType);

      // set person contact information
      final ContactInformationType contactInformation =
        new ContactInformationType();
      final AddressType addressType = BDMGenerateCorrespondenceMapper
        .getAddress(bdmCorrespondenceDetails.toDetails.addressLineOne,
          bdmCorrespondenceDetails.toDetails.addressLineTwo,
          bdmCorrespondenceDetails.toDetails.addressLineThree,
          bdmCorrespondenceDetails.toDetails.addressLineFour,
          bdmCorrespondenceDetails.toDetails.unparsedAddressInd);

      contactInformation.getContactMailingAddress().add(addressType);

      client.getPersonContactInformation().add(contactInformation);

      // PersonBirthDate
      final DateType dob = new DateType();
      final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.Date datete =
        new curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.Date();
      datete.setValue(BDMGenerateCorrespondenceMapper
        .getXMLDate(bdmCorrespondenceDetails.clientDtls.dateOfBirth));
      dob.getDate().add(datete);
      client.setPersonBirthDate(dob);

      // SIN
      if (CuramConst.gkZero != bdmCorrespondenceDetails.clientDtls.sin) {
        final IdentificationType sinIdentificationType =
          new IdentificationType();
        final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String sinNumber =
          new curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String();
        sinNumber
          .setValue(Long.toString(bdmCorrespondenceDetails.clientDtls.sin));
        sinIdentificationType.getIdentificationID().add(sinNumber);
        client.setPersonSINIdentification(sinIdentificationType);
      }

      // PersonGenderText
      final TextType gender = new TextType();
      gender.setValue(bdmCorrespondenceDetails.clientDtls.gender);
      client.setPersonGenderText(gender);

      // PersonMaritalStatusCode
      final TextType maritalStatusCode = new TextType();
      maritalStatusCode.setValue("Married");
      client.getPersonMaritalStatusCode().add(maritalStatusCode);

      // PersonPerferredLanguage
      final LanguageType language = new LanguageType();
      final TextType lagNam = new TextType();
      // Retain the language at document level as well.
      if (!StringUtil.isNullOrEmpty(
        bdmCorrespondenceDetails.clientDtls.preferredLanguage)) {
        lagNam
          .setValue(bdmCorrespondenceDetails.clientDtls.preferredLanguage);
      } else {
        lagNam
          .setValue(bdmCorrespondenceDetails.toParticipantPreferredLanguage);
      }
      language.getLanguageName().add(lagNam);
      client.setPersonPreferredLanguage(language);

      // PersonDeathDate
      final DateType dod = new DateType();
      final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.Date dateOfDeath =
        new curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.Date();
      if (!bdmCorrespondenceDetails.clientDtls.dateOfDeath.isZero()) {
        dateOfDeath.setValue(BDMGenerateCorrespondenceMapper
          .getXMLDate(bdmCorrespondenceDetails.clientDtls.dateOfDeath));
      } else {
        dateOfDeath.setValue(BDMGenerateCorrespondenceMapper
          .getXMLDate(BDMGenerateCorrespondenceMapper.getNullDateForCCT()));
      }

      dod.getDate().add(dateOfDeath);
      client.setPersonDeathDate(dod);

      final StringWriter sw = new StringWriter();
      final JAXBElement<ClientType> je = new JAXBElement<>(
        new QName("", "ns3:Client"), ClientType.class, client);
      marshaller.marshal(je, sw);
      mappedData = sw.toString();
    } catch (final Exception e) {

      e.printStackTrace();
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
        e);
    }
    return mappedData;
  }

  public static BDMLetterTemplateConfig getTemplateConfiguration(
    final String templateName) throws AppException, InformationalException {

    final java.lang.String templateConfigJSON =
      getTemplateConfigJSON(templateName);

    final ObjectMapper mapper = new ObjectMapper();
    curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls =
      null;
    try {
      if (!templateConfigJSON.isEmpty()) {
        configDtls = mapper.readValue(templateConfigJSON,
          curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig.class);
      }
    } catch (final JsonProcessingException e) {
      e.printStackTrace();
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
    }
    return configDtls;
  }

  private static Object getData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object inputData) throws AppException, InformationalException {

    Object returnObject = null;
    // Check config and use default implementation or invoke the handler
    if (inputData != null && !configDtls.dataHandlerClassName.isEmpty()) {

      try {
        final Class<?> dataHandlerClass =
          Class.forName(configDtls.dataHandlerClassName);
        final Method method = dataHandlerClass.getDeclaredMethod("getData",
          curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig.class,
          Object.class);
        Object dataHandlerClassInstance;
        dataHandlerClassInstance = dataHandlerClass.newInstance();
        returnObject =
          method.invoke(dataHandlerClassInstance, configDtls, inputData);
      } catch (final Exception e) {
        e.printStackTrace();
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
          e);
      }

    } else if (inputData != null && !configDtls.sql.isEmpty()) {
      final BDMCorrespondenceTemplateDefaultDataHandler defaultHandler =
        new BDMCorrespondenceTemplateDefaultDataHandler();
      returnObject = defaultHandler.getData(configDtls, inputData);
    }

    return returnObject;
  }

  private java.lang.String
    getToCorrespondentXML(final long recipientConcernRoleID)
      throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = recipientConcernRoleID;

    final BDMCorrespondenceDetails corresDetails =
      new BDMCorrespondenceDetails();
    final boolean isThirdPartyContactInd = false;
    corresDetails.toAddressID = helper.getAddressIDforConcern(
      concernRoleKey.concernRoleID, isThirdPartyContactInd);
    final String[] splittedAddress =
      helper.getAddressMapper(corresDetails.toAddressID);

    corresDetails.toDetails.addressLineOne = splittedAddress[0];
    corresDetails.toDetails.addressLineTwo = splittedAddress[1];
    corresDetails.toDetails.addressLineThree = splittedAddress[2];
    corresDetails.toDetails.addressLineFour = splittedAddress[3];
    if (!StringUtil.isNullOrEmpty(splittedAddress[4])
      && BDMConstants.kBDMUNPARSE.equalsIgnoreCase(splittedAddress[4])) {
      corresDetails.toDetails.unparsedAddressInd = true;
    }

    // creating jaxb objects
    JAXBContext context;
    java.lang.String mappedData = "";
    try {
      context = JAXBContext.newInstance(RecipientType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING,
        BDMCorrespondenceConstants.kJAXBEncoding);
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      // root element
      final RecipientType recipientTo =
        new ObjectFactory().createRecipientType();

      final AddressType recipientToAddressType =
        BDMGenerateCorrespondenceMapper.getAddress(
          corresDetails.toDetails.addressLineOne,
          corresDetails.toDetails.addressLineTwo,
          corresDetails.toDetails.addressLineThree,
          corresDetails.toDetails.addressLineFour,
          corresDetails.toDetails.unparsedAddressInd);

      recipientTo.setRecipientAddress(recipientToAddressType);
      final TextType recipientToCategoryCode = new TextType();
      recipientToCategoryCode
        .setValue(BDMConstants.kGenerateCorrespondenceXMLTo);
      recipientTo.setRecipientCategoryCode(recipientToCategoryCode);
      final TextType recipientToContactCode = new TextType();
      recipientToContactCode
        .setValue(BDMConstants.kGenerateCorrespondenceXMLClient);
      recipientTo.setRecipientContactCode(recipientToContactCode);
      final TextType recipientToName = new TextType();
      // Make names upper case in the payload
      recipientToName.setValue(
        helper.getConcernRoleName(corresDetails.concernRoleID).toUpperCase());
      recipientTo.setRecipientName(recipientToName);

      final LanguageType recipientLanguage = new LanguageType();
      final TextType langName = new TextType();
      langName
        .setValue(helper.getPreferredLanguage(concernRoleKey.concernRoleID));
      recipientLanguage.getLanguageName().add(langName);
      recipientTo.setRecipientPreferredLanguage(recipientLanguage);

      final StringWriter sw = new StringWriter();
      final JAXBElement<RecipientType> je =
        new JAXBElement<>(new QName("", "ns3:CorrespondenceRecipient"),
          RecipientType.class, recipientTo);
      marshaller.marshal(je, sw);
      mappedData = sw.toString();

    } catch (final JAXBException e) {
      e.printStackTrace();
    }
    return mappedData;

  }

  /**
   * This method gets the template xml from Appresource and checks if the
   * attributes defined in the xml has a value in the pojoObject
   *
   * @param configDtls
   * @param pojoObject
   * @return
   * @throws AppException
   * @throws InformationalException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static InformationalManager validateData(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final Object pojoObject,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    InformationalManager informationalManager)
    throws AppException, InformationalException {

    // Check config and use default implementation or invoke the handler
    if (!configDtls.validationHandlerClassName.isEmpty()) {

      try {
        final Class<?> validationHandlerClass =
          Class.forName(configDtls.validationHandlerClassName);
        final Method method = Class
          .forName(configDtls.validationHandlerClassName)
          .getDeclaredMethod("validateData",
            curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig.class,
            Object.class, BDMCorrespondenceRecipientClientInput.class,
            InformationalManager.class);

        final Object validationHandlerClassInstance =
          validationHandlerClass.newInstance();

        informationalManager =
          (InformationalManager) method.invoke(validationHandlerClassInstance,
            configDtls, pojoObject, correspondenceRecipientClientInput,
            informationalManager);

      } catch (final Exception e) {
        e.printStackTrace();
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
          e);
      }
    } else if (!configDtls.mandatoryFieldsList.isEmpty()) {
      // Invoke the default handler only if the mandatory field list is not empty
      final BDMCorrespondenceValidationDefaultHandler defaultHandler =
        new BDMCorrespondenceValidationDefaultHandler();
      informationalManager = defaultHandler.validateData(configDtls,
        pojoObject, correspondenceRecipientClientInput, informationalManager);
    }

    return informationalManager;
  }

  /**
   * This method gets the template xml from Appresource and checks if the
   * attributes defined in the xml has a value in the pojoObject
   *
   * @param configDtls
   * @param pojoObject
   * @return
   * @throws AppException
   * @throws InformationalException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static InformationalManager validateTemplateContext(
    final curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig configDtls,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    InformationalManager informationalManager)
    throws AppException, InformationalException {

    // Check config and use default implementation or invoke the handler
    if (!configDtls.validationHandlerClassName.isEmpty()) {

      try {
        final Class<?> validationHandlerClass =
          Class.forName(configDtls.validationHandlerClassName);
        final Method method = Class
          .forName(configDtls.validationHandlerClassName)
          .getDeclaredMethod("validateTemplateContext",
            curam.ca.gc.bdm.correspondenceframework.impl.BDMLetterTemplateConfig.class,
            BDMCorrespondenceRecipientClientInput.class,
            InformationalManager.class);

        Object validationHandlerClassInstance = null;

        validationHandlerClassInstance = validationHandlerClass.newInstance();

        informationalManager =
          (InformationalManager) method.invoke(validationHandlerClassInstance,
            configDtls, correspondenceRecipientClientInput,
            informationalManager);

      } catch (final Exception e) {
        e.printStackTrace();
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE);
      }
    } else if (!configDtls.mandatoryFieldsList.isEmpty()) {
      // Invoke the default handler only if the mandatory field list is not empty
      final BDMCorrespondenceValidationDefaultHandler defaultHandler =
        new BDMCorrespondenceValidationDefaultHandler();
      informationalManager = defaultHandler.validateTemplateContext(
        configDtls, correspondenceRecipientClientInput, informationalManager);
    }

    return informationalManager;
  }

  private long createOrUpdateStagedRecord(
    final BDMLetterTemplateConfig configDtls, final String frequency,
    final BDMCorrespondenceRecipientClientInput correspondenceRecipientClientInput,
    final long recipientConcernRoleID, final String status,
    final Object finalData, final Object inputData,
    final String validationMessages, final String countryCode)
    throws AppException, InformationalException {

    final BDMCorrespondenceStaging bdmoasCorrespondenceStagingObj =
      BDMCorrespondenceStagingFactory.newInstance();

    final BDMCorrespondenceStagingDtls correspondenceStagingDtls =
      new BDMCorrespondenceStagingDtls();
    correspondenceStagingDtls.correspondenceStagingID =
      UniqueIDFactory.newInstance().getNextID();

    correspondenceStagingDtls.caseID =
      correspondenceRecipientClientInput.caseID;

    correspondenceStagingDtls.recipientConcernRoleID = recipientConcernRoleID;
    correspondenceStagingDtls.clientConcernRoleID =
      correspondenceRecipientClientInput.clientConcernRoleID;

    correspondenceStagingDtls.frequency = frequency;
    // Frequency Mapping to date
    if (FREQUENCYCODE.DAILY.equalsIgnoreCase(frequency)) {
      correspondenceStagingDtls.dateForSending = Date.getCurrentDate();
    }

    final ObjectMapper mapper = new ObjectMapper();

    try {
      if (finalData != null) {
        correspondenceStagingDtls.payLoad = finalData.toString();
      }

      correspondenceStagingDtls.inputData =
        mapper.writeValueAsString(inputData);
    } catch (final JsonProcessingException e) {
      e.printStackTrace();
      throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
        e);
    }

    correspondenceStagingDtls.status = status;
    correspondenceStagingDtls.templateName = configDtls.templateName;
    correspondenceStagingDtls.triggerDate = DateTime.getCurrentDateTime();
    correspondenceStagingDtls.validationMessages = validationMessages;
    // Set the country code
    correspondenceStagingDtls.countryCode = countryCode;

    // Get the region code after case management confirms on where to pull the
    // information from
    correspondenceStagingDtls.regionCode = "";

    final BDMCorrespondenceStagingDateAndTemplateName dateAndTemplateName =
      new BDMCorrespondenceStagingDateAndTemplateName();
    dateAndTemplateName.dateForSending =
      correspondenceStagingDtls.dateForSending;
    dateAndTemplateName.templateName = configDtls.templateName;
    BDMCorrespondenceStagingJobID correspondenceStagingJobID =
      new BDMCorrespondenceStagingJobID();
    try {
      correspondenceStagingJobID = bdmoasCorrespondenceStagingObj
        .readJobIDByTemplateNameDateForSending(dateAndTemplateName);
    } catch (final RecordNotFoundException e) {
      // Do nothing
    }
    if (correspondenceStagingJobID.jobID == 0) {
      final UniqueIDKeySet keySet = new UniqueIDKeySet();
      keySet.keySetName =
        BDMCorrespondenceConstants.kCorrespondenceJobIDKeySet;
      correspondenceStagingDtls.jobID = curam.core.fact.UniqueIDFactory
        .newInstance().getNextIDFromKeySet(keySet);
    } else {
      correspondenceStagingDtls.jobID = correspondenceStagingJobID.jobID;
    }

    final BDMCorrespondenceStagingKeySearchDetails key =
      new BDMCorrespondenceStagingKeySearchDetails();
    key.clientConcernRoleID = correspondenceStagingDtls.clientConcernRoleID;
    key.frequency = correspondenceStagingDtls.frequency;
    key.recipientConcernRoleID =
      correspondenceStagingDtls.correspondenceStagingID;
    key.status = correspondenceStagingDtls.status;
    key.templateName = correspondenceStagingDtls.templateName;

    final BDMCorrespondenceStagingDtlsList correspondenceStagingDtlsList =
      bdmoasCorrespondenceStagingObj
        .searchByTemplateRecipientClientStatusFrequency(key);

    // if configDtls.shouldUpdate is true then check if existing record is
    // present and update the existing record
    if (configDtls.shouldUpdateInd
      && !correspondenceStagingDtlsList.dtls.isEmpty()) {
      for (final BDMCorrespondenceStagingDtls existingCorrespondenceStagingDtls : correspondenceStagingDtlsList.dtls) {

        final BDMCorrespondenceStagingKey stagingKey =
          new BDMCorrespondenceStagingKey();
        stagingKey.correspondenceStagingID =
          existingCorrespondenceStagingDtls.correspondenceStagingID;
        correspondenceStagingDtls.correspondenceStagingID =
          existingCorrespondenceStagingDtls.correspondenceStagingID;
        bdmoasCorrespondenceStagingObj.modify(stagingKey,
          correspondenceStagingDtls);
      }
    } else {
      correspondenceStagingDtls.correspondenceStagingID =
        UniqueIDFactory.newInstance().getNextID();
      bdmoasCorrespondenceStagingObj.insert(correspondenceStagingDtls);
    }

    return correspondenceStagingDtls.correspondenceStagingID;
  }

  /**
   * Match the attributes from the pojoObject to the placeholders defined in the
   * templateXML using the attribute prefix and replace the values.
   *
   * @param pojoObject
   * @param templateXML
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws AppException
   * @throws InformationalException
   */
  public static String mapToXML(final Object pojoObject, String templateXML)
    throws AppException {

    final Class<?> clazz = pojoObject.getClass();
    final Map<java.lang.String, Object> fieldElementsMap = new HashMap<>();
    for (final Field field : clazz.getDeclaredFields()) {
      try {
        final Class<?> typeClass = field.getType();
        if (typeClass.getName().equalsIgnoreCase(Date.class.getName())) {
          final Date date = (Date) field.get(pojoObject);
          fieldElementsMap.put(field.getName(),
            BDMGenerateCorrespondenceMapper.getXMLDate(date));
        } else {
          fieldElementsMap.put(field.getName(), field.get(pojoObject));
        }

      } catch (final IllegalArgumentException | IllegalAccessException
        | DatatypeConfigurationException e) {
        e.printStackTrace();
        throw new AppException(BDMBPOCCT.ERR_UNABLE_TO_CREATE_CORRESPONDENCE,
          e);
      }
    }

    for (final Entry<String, Object> entry : fieldElementsMap.entrySet()) {
      final String attributeName = entry.getKey();

      final String textToFind =
        BDMCorrespondenceConstants.kTemplateAttributePrefix + attributeName;
      final String textToReplace =
        entry.getValue() != null ? entry.getValue().toString() : "";
      templateXML = templateXML.replaceAll(textToFind, textToReplace);
    }

    return templateXML;
  }

  private static String getTemplateConfigJSON(final String templateName)
    throws AppException, InformationalException {

    return getFromAppresource(templateName
      + BDMCorrespondenceConstants.kAppresourceNameTemplateConfigSuffix);
  }

  public static String getTemplateXML(final String templateName)
    throws AppException, InformationalException {

    return getFromAppresource(templateName
      + BDMCorrespondenceConstants.kAppresourceNameTemplateXMLSuffix);
  }

  private static String getFromAppresource(final String appResourceName)
    throws AppException, InformationalException {

    final ResourceName resoureceName = new ResourceName();
    resoureceName.name = appResourceName;
    try {

      final NSResourceDetailsList resourceDetails =
        AppResourceFactory.newInstance().readByName(resoureceName);
      if (!resourceDetails.dtls.isEmpty()) {
        return new String(resourceDetails.dtls.get(0).content.copyBytes());
      }

    } catch (final RecordNotFoundException rnfe) {
      Trace.kTopLevelLogger.info("AppResouce NOT FOUND : " + appResourceName);
      return "";
    }
    return "";
  }

  public static BDMCorrespondenceDetails setTemplateDetailsAndSubject(
    final String templateName, final BDMCorrespondenceDetails corresDetails)
    throws AppException, InformationalException {

    // get the template details including category and sub-category names
    final BDMCCTTemplateName templateKey = new BDMCCTTemplateName();
    templateKey.templateName = templateName;

    final BDMTemplateSearchDetails templateDetails = BDMCCTTemplateFactory
      .newInstance().readTemplateDetailsByTemplateName(templateKey);

    // set the template details
    corresDetails.templateID = templateDetails.templateID;
    corresDetails.templateName = templateDetails.templateName;
    corresDetails.templatePath = templateDetails.templatePath;

    // set the category and sub-category as subject
    if (!templateDetails.subCategory.isEmpty()) {
      corresDetails.subject = templateDetails.category + CuramConst.gkDash
        + templateDetails.subCategory;
    } else {
      corresDetails.subject = templateDetails.category;
    }
    return corresDetails;
  }

}
