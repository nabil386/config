package curam.ca.gc.bdm.evidence.impl;

import com.google.inject.Singleton;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMATTESTATIONTYPE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.impl.CASEEVIDENCEEntry;
import curam.core.impl.EnvVars;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorModifyDtls;
import curam.core.sl.infrastructure.impl.StandardEvidenceCallOuts;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.infrastructure.struct.EIEvidenceKey;
import curam.core.sl.infrastructure.struct.EIEvidenceKeyList;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.dynamicevidence.validation.impl.DynamicEvidenceCallOutsImpl;
import curam.evidence.impl.DynamicEvidenceTypeImpl;
import curam.pdc.impl.PDCConst;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.internal.resources.fact.AppResourceFactory;
import curam.util.internal.resources.intf.AppResource;
import curam.util.internal.resources.struct.NSResourceDetails;
import curam.util.internal.resources.struct.NSResourceKey;
import curam.util.message.CatEntry;
import curam.util.resources.Configuration;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @since TASK-19844
 *
 */
@SuppressWarnings("restriction")
@Singleton
public class BDMDynamicEvidenceTypeImpl extends DynamicEvidenceTypeImpl {

  /*
   * (non-Javadoc)
   *
   * @see
   * curam.evidence.impl.DynamicEvidenceTypeImpl#getStandardEvidenceCallOuts(
   * curam.codetable.impl.CASEEVIDENCEEntry)
   */
  @Override
  public StandardEvidenceCallOuts
    getStandardEvidenceCallOuts(final CASEEVIDENCEEntry evidenceType) {

    if (PDCConst.PDCBANKACCOUNT.equals(evidenceType.getCode())) {

      return new DynamicEvidenceCallOutsImpl() {

        @Override
        public void preCreate(final EvidenceDescriptorInsertDtls descriptor,
          final EIEvidenceKeyList parentKeyList, final Object evidenceObject)
          throws AppException, InformationalException {

          fixUpDynamicEvidenceDataDetails(
            (DynamicEvidenceDataDetails) evidenceObject);
          super.preCreate(descriptor, parentKeyList, evidenceObject);
        }

        @Override
        public void preModify(final EIEvidenceKey key,
          final EvidenceDescriptorModifyDtls mDescriptor,
          final Object evidenceObject, final EIEvidenceKey parentKey)
          throws AppException, InformationalException {

          fixUpDynamicEvidenceDataDetails(
            (DynamicEvidenceDataDetails) evidenceObject);
          super.preModify(key, mDescriptor, evidenceObject, parentKey);
        }

      };
    } else if (BDMConstants.ATTESTATION_EVIDENCE_TYPE
      .equals(evidenceType.getCode())) {

      return new DynamicEvidenceCallOutsImpl() {

        @Override
        public void preCreate(final EvidenceDescriptorInsertDtls descriptor,
          final EIEvidenceKeyList parentKeyList, final Object evidenceObject)
          throws AppException, InformationalException {

          addAttestationDynamicEvidenceComments(
            (DynamicEvidenceDataDetails) evidenceObject);
          super.preCreate(descriptor, parentKeyList, evidenceObject);
        }
      };

    } else if (CASEEVIDENCE.BDMDEPENDANT.equals(evidenceType.getCode())
      || CASEEVIDENCE.BDMINCOME.equals(evidenceType.getCode())) {

      return new DynamicEvidenceCallOutsImpl() {

        @Override
        public void preCreate(final EvidenceDescriptorInsertDtls descriptor,
          final EIEvidenceKeyList parentKeyList, final Object evidenceObject)
          throws AppException, InformationalException {

          validateAttestationDetailsData(evidenceObject, "CREATE");
          super.preCreate(descriptor, parentKeyList, evidenceObject);
        }

        @Override
        public void preModify(final EIEvidenceKey key,
          final EvidenceDescriptorModifyDtls mDescriptor,
          final Object evidenceObject, final EIEvidenceKey parentKey)
          throws AppException, InformationalException {

          if (EVIDENCECHANGEREASON.REPORTEDBYEXTERNALPARTY
            .equals(mDescriptor.changeReason)
            || EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT
              .equals(mDescriptor.changeReason)
            || EVIDENCECHANGEREASON.REPORTEDBYCLIENT
              .equals(mDescriptor.changeReason)) {

            validateAttestationDetailsData(evidenceObject, "MODIFY");

          }

          super.preModify(key, mDescriptor, evidenceObject, parentKey);
        }
      };
    } else if (PDCConst.PDCBIRTHANDDEATH.equals(evidenceType.getCode())) {
      return new DynamicEvidenceCallOutsImpl() {

        @Override
        public void preModify(final EIEvidenceKey key,
          final EvidenceDescriptorModifyDtls mDescriptor,
          final Object evidenceObject, final EIEvidenceKey parentKey)
          throws AppException, InformationalException {

          boolean result = false;

          if (TransactionInfo
            .getFacadeScopeObject(BDMConstants.IS_MERGE_PROSPECT) != null) {
            result = (Boolean) TransactionInfo
              .getFacadeScopeObject(BDMConstants.IS_MERGE_PROSPECT);
          }
          if (!result) {
            if (EVIDENCECHANGEREASON.REPORTEDBYEXTERNALPARTY
              .equals(mDescriptor.changeReason)
              || EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT
                .equals(mDescriptor.changeReason)
              || EVIDENCECHANGEREASON.REPORTEDBYCLIENT
                .equals(mDescriptor.changeReason)) {

              validateAttestationDetailsData(evidenceObject, "MODIFY");

            }
          }

          super.preModify(key, mDescriptor, evidenceObject, parentKey);
        }
      };
    } else {

      return super.getStandardEvidenceCallOuts(evidenceType);
    }
  }

  /**
   * The method to validate attestation details and set attesation date to
   * the income/incarceration/dependant evidence.
   *
   * @param evidenceObject evidence data details
   * @param actionString
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */

  private void validateAttestationDetailsData(final Object evidenceObject,
    final String actionString) throws InformationalException, AppException {

    final String bdmAgreeAttestation = getAttributeValue(
      (DynamicEvidenceDataDetails) evidenceObject, "bdmAgreeAttestation");
    final String attestee = getAttributeValue(
      (DynamicEvidenceDataDetails) evidenceObject, "attesteeCaseParticipant");
    final ArrayList<CatEntry> attestationErrorMessages =
      new ArrayList<CatEntry>();
    if (StringUtil.isNullOrEmpty(bdmAgreeAttestation)
      || !BDMAGREEATTESTATION.YES.equals(bdmAgreeAttestation)) {
      if (actionString.equals("CREATE")) {

        attestationErrorMessages
          .add(BDMEVIDENCE.ERR_ATTESTATION_CHECKBOX_MANDATORY_WHEN_INSERT);

      }
      if (actionString.equals("MODIFY")) {
        attestationErrorMessages
          .add(BDMEVIDENCE.ERR_ATTESTATION_CHECKBOX_MANDATORY);
      }
    }
    if (StringUtil.isNullOrEmpty(attestee) || "0".equals(attestee)) {
      if (actionString.equals("CREATE")) {
        attestationErrorMessages
          .add(BDMEVIDENCE.ERR_ATTESTEE_IS_MANDATORY_WHEN_INSERT);
      }
      if (actionString.equals("MODIFY")) {
        attestationErrorMessages.add(BDMEVIDENCE.ERR_ATTESTEE_IS_MANDATORY);
      }
    }
    if (!attestationErrorMessages.isEmpty()) {
      for (final CatEntry attestationError : attestationErrorMessages) {
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(attestationError), null, InformationalType.kError,
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetNine,
          0);
      }
      TransactionInfo.getInformationalManager().failOperation();
    }

    DynamicEvidenceTypeConverter
      .setAttribute(((DynamicEvidenceDataDetails) evidenceObject)
        .getAttribute("attestationDate"), Date.getCurrentDate());
  }

  /**
   *
   * @param newEvidenceObject
   * @param attributeName
   * @return
   */
  private String getAttributeValue(
    final DynamicEvidenceDataDetails newEvidenceObject,
    final String attributeName) {

    final DynamicEvidenceDataAttributeDetails attribute =
      newEvidenceObject.getAttribute(attributeName);
    String value = "";

    if (attribute != null) {

      value = attribute.getValue();
    }

    return value;
  }

  /**
   * The method to add attestation text to the attestation evidence comments.
   * The properties for the attestation text displayed on the application
   * attestation page are read from the application resource to get the
   * text.
   *
   * @param dynamicEvidenceDataDetails evidence data details
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  static void addAttestationDynamicEvidenceComments(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    // load properties for the attestation properties
    final Properties props = new Properties();
    final StringBuilder attestationText = new StringBuilder();

    try {
      // Read the application attestation properties from the appresource for
      // user
      // locale
      final NSResourceKey nsResourceKey = new NSResourceKey();
      nsResourceKey.name = BDMConstants.ATTESTATION_PROP_RESOURCE_NAME;
      nsResourceKey.localeIdentifier = TransactionInfo.getProgramLocale();
      nsResourceKey.localeIsNull = true;

      final AppResource appResource = AppResourceFactory.newInstance();
      final NSResourceDetails nsResDetails =
        appResource.readByNameAndLocale(nsResourceKey);

      props
        .load(new StringReader(new String(nsResDetails.content.copyBytes())));

      String propKeys = null;
      // dynamicEvidenceDataDetails.getAttribute(BDMConstants.ATTESTATION_TYPE);
      final String attestationType =
        dynamicEvidenceDataDetails.getAttribute("attestationType").getValue();
      if (attestationType
        .equals(BDMATTESTATIONTYPE.CLIENT_PORTAL_APPLICATION)) {
        // read the attestation text for application submitted in client portal
        // in application properties
        propKeys =
          Configuration.getProperty(EnvVars.BDM_ATTESTATION_TEXT_PROPERTIES);
      } else if (attestationType
        .equals(BDMATTESTATIONTYPE.AGENT_PORTAL_APPLICATION)) {
        // read the attestation text for application submitted in agent portal
        // in application properties
        propKeys = Configuration
          .getProperty(EnvVars.BDM_ATTESTATION_AP_TEXT_PROPERTIES);

      }

      if (propKeys == null) {
        return;
      }

      // get properties text for configured properties
      Stream.of(propKeys.split(","))
        .filter(propKey -> props.getProperty(propKey) != null)
        .forEach(prop -> {
          attestationText
            .append(props.getProperty(prop) + System.lineSeparator());
        });

    } catch (final Exception e) {

      e.printStackTrace();
    }

    dynamicEvidenceDataDetails.getAttribute(BDMConstants.kCOMMENTS)
      .setValue(attestationText.toString());
  }

  /**
   * Sets the bank account sort code evidence data attribute concatenating
   * financial institution number and transit number.
   *
   * @param dynamicEvidenceDataDetails evidence data details
   *
   * @throws AppException Generic Exception Signature
   * @throws InformationalException Generic Exception Signature
   */
  static void fixUpDynamicEvidenceDataDetails(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails)
    throws AppException, InformationalException {

    if (BDMEvidenceUtil
      .hasBDMBankAccountCustomizations(dynamicEvidenceDataDetails)) {

      final DynamicEvidenceDataAttributeDetails financialInstNumber =
        dynamicEvidenceDataDetails
          .getAttribute(BDMEvidenceUtil.kFINANCIAL_INSTITUTION_NUMBER);
      final DynamicEvidenceDataAttributeDetails transitNumber =
        dynamicEvidenceDataDetails
          .getAttribute(BDMEvidenceUtil.kBRANCH_TRANSIT_NUMBER);

      String finInstitutionNumberValue = null;
      String transitValue = null;

      if (financialInstNumber != null
        && (finInstitutionNumberValue =
          financialInstNumber.getValue()) != null
        && !finInstitutionNumberValue.trim().isEmpty()
        && transitNumber != null
        && (transitValue = transitNumber.getValue()) != null
        && !transitValue.trim().isEmpty()) {

        dynamicEvidenceDataDetails.getAttribute(BDMEvidenceUtil.kSORT_CODE)
          .setValue(finInstitutionNumberValue + transitValue);
      }
    }
  }

}
