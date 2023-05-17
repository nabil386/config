package curam.ca.gc.bdm.sl.address.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMMODEOFRECEIPT;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.message.impl.BDMEVIDENCEExceptionCreator;
import curam.ca.gc.bdm.sl.address.struct.BDMParticipantAddressDetails;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.impl.EnvVars;
import curam.core.intf.Address;
import curam.core.intf.ConcernRole;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceTypeKey;
import curam.core.sl.infrastructure.fact.EvidenceControllerFactory;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.sl.infrastructure.impl.EvidenceControllerInterface;
import curam.core.struct.AddressDtls;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleAddressKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PrimaryAddressDetails;
import curam.creole.value.CodeTableItem;
import curam.dynamicevidence.definition.impl.EvidenceTypeDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeDefDAO;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDef;
import curam.dynamicevidence.definition.impl.EvidenceTypeVersionDefDAO;
import curam.dynamicevidence.impl.DynamicEvidenceDataAttributeDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetailsFactory;
import curam.dynamicevidence.type.impl.DynamicEvidenceTypeConverter;
import curam.participant.impl.ConcernRoleDAO;
import curam.pdc.fact.PDCUtilFactory;
import curam.pdc.impl.PDCConst;
import curam.pdc.intf.PDCUtil;
import curam.pdc.struct.PDCCaseIDCaseParticipantRoleID;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Configuration;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;

public class BDMPDCAddress
extends curam.ca.gc.bdm.sl.address.base.BDMPDCAddress {

  private static final String PARTICIPANT = "participant";

  private static final String ADDRESS = "address";

  private static final String FROM_DATE = "fromDate";

  private static final String TO_DATE = "toDate";

  private static final String ADDRESS_TYPE = "addressType";

  private static final String PREFERRED_IND = "preferredInd";

  private static final String COMMENTS = "comments";

  public static final String RECEIVED_FROM = "bdmReceivedFrom";

  public static final String RECEIVED_FROM_COUNTRY = "bdmReceivedFromCountry";

  public static final String MODE_OF_RECEIPT = "bdmModeOfReceipt";

  @Inject
  private EvidenceTypeDefDAO evidenceTypeDefDAO;

  @Inject
  ConcernRoleDAO concernRoleDAO;

  @Inject
  private EvidenceTypeVersionDefDAO evidenceTypeVersionDefDAO;

  public BDMPDCAddress() {

    GuiceWrapper.getInjector().injectMembers(this);

  }

  @Override
  public void createAddress(final BDMParticipantAddressDetails details)
    throws AppException, InformationalException {

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = details.concernRoleID;
    final PDCUtil pdcUtil = PDCUtilFactory.newInstance();
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID =
      pdcUtil.getPDCCaseIDCaseParticipantRoleID(concernRoleKey);

    final AddressDtls addressDtls = this.insertAddress(details);

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      this.getDynamicEvidenceDataDetails();

    assignEvidenceDetails(pdcCaseIDCaseParticipantRoleID, details,
      dynamicEvidenceDataDetails, addressDtls);

    this.insertEvidence(dynamicEvidenceDataDetails, details,
      pdcCaseIDCaseParticipantRoleID);

  }

  @Override
  public void validateSourceFields(final BDMParticipantAddressDetails details)
    throws AppException, InformationalException {

    if (!details.bdmReceivedFrom.equals(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)
      && (!details.bdmModeOfReceipt.isEmpty()
        || !details.bdmReceivedFromCountry.isEmpty())) {
      throw BDMEVIDENCEExceptionCreator.ERR_FOREIGN_GOVT_EXCLUSIVE_FIELDS();
    }

    if (details.bdmReceivedFrom.equals(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)
      && (details.bdmModeOfReceipt.isEmpty()
        || details.bdmReceivedFromCountry.isEmpty())) {
      throw BDMEVIDENCEExceptionCreator.ERR_FOREIGN_GOVT_REQUIRED_FIELDS();
    }

    // Task 90981 - Remove this validation for Prospect Person - Register Client
    // and Create FEC feature

    if (!isProspectPerson(details.concernRoleID)
      && details.bdmReceivedFrom.equals(BDMRECEIVEDFROM.FOREIGN_GOVERNMENT)
      && details.bdmModeOfReceipt.equals(BDMMODEOFRECEIPT.LIAISON)) {

      final String[] restrictedCountries =
        Configuration.getProperty(EnvVars.BDM_ENV_RESTRICTED_COUNTRY_LIST)
        .split(CuramConst.gkComma);

      for (final String country : restrictedCountries) {
        if (country.trim().equals(details.bdmReceivedFromCountry)) {
          throw BDMEVIDENCEExceptionCreator.ERR_RESTRICTED_COUNTRY();
        }
      }

    }



  }


  private void assignEvidenceDetails(
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID,
    final BDMParticipantAddressDetails details,
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final AddressDtls addressDtls)
      throws AppException, InformationalException {

    final DynamicEvidenceDataAttributeDetails participant =
      dynamicEvidenceDataDetails.getAttribute(PARTICIPANT);

    DynamicEvidenceTypeConverter.setAttribute(participant,
      Long.valueOf(pdcCaseIDCaseParticipantRoleID.caseParticipantRoleID));

    final DynamicEvidenceDataAttributeDetails address =
      dynamicEvidenceDataDetails.getAttribute(ADDRESS);

    DynamicEvidenceTypeConverter.setAttribute(address,
      Long.valueOf(addressDtls.addressID));

    final DynamicEvidenceDataAttributeDetails startDate =
      dynamicEvidenceDataDetails.getAttribute(FROM_DATE);

    if (!details.startDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(startDate, details.startDate);
    } else {
      startDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails endDate =
      dynamicEvidenceDataDetails.getAttribute(TO_DATE);

    if (!details.endDate.isZero()) {
      DynamicEvidenceTypeConverter.setAttribute(endDate, details.endDate);
    } else {
      endDate.setValue("");
    }

    final DynamicEvidenceDataAttributeDetails addressType =
      dynamicEvidenceDataDetails.getAttribute(ADDRESS_TYPE);
    DynamicEvidenceTypeConverter.setAttribute(addressType,
      new CodeTableItem(CONCERNROLEADDRESSTYPE.TABLENAME, details.typeCode));

    final DynamicEvidenceDataAttributeDetails bdmReceivedFrom =
      dynamicEvidenceDataDetails.getAttribute(RECEIVED_FROM);
    bdmReceivedFrom.setValue(details.bdmReceivedFrom);

    final DynamicEvidenceDataAttributeDetails bdmReceivedFromCountry =
      dynamicEvidenceDataDetails.getAttribute(RECEIVED_FROM_COUNTRY);
    bdmReceivedFromCountry.setValue(details.bdmReceivedFromCountry);

    final DynamicEvidenceDataAttributeDetails bdmModeOfReceipt =
      dynamicEvidenceDataDetails.getAttribute(MODE_OF_RECEIPT);
    bdmModeOfReceipt.setValue(details.bdmModeOfReceipt);

    final DynamicEvidenceDataAttributeDetails comments =
      dynamicEvidenceDataDetails.getAttribute(COMMENTS);
    DynamicEvidenceTypeConverter.setAttribute(comments, details.comments);

    final DynamicEvidenceDataAttributeDetails preferredInd =
      dynamicEvidenceDataDetails.getAttribute(PREFERRED_IND);
    DynamicEvidenceTypeConverter.setAttribute(preferredInd,
      Boolean.valueOf(false));

    if (details.primaryAddressInd) {

      DynamicEvidenceTypeConverter.setAttribute(preferredInd,
        Boolean.valueOf(true));

    } else if (details.concernRoleAddressID != 0L) {

      final ConcernRoleAddressKey concernRoleAddressKey =
        new ConcernRoleAddressKey();

      concernRoleAddressKey.concernRoleAddressID =
        details.concernRoleAddressID;

      final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();

      final ConcernRoleAddressDtls storedConcernRoleAddressDtls =
        ConcernRoleAddressFactory.newInstance().read(notFoundIndicator,
          concernRoleAddressKey);

      if (!notFoundIndicator.isNotFound()) {

        final ConcernRoleKey concernRoleKey = new ConcernRoleKey();

        concernRoleKey.concernRoleID = details.concernRoleID;
        final ConcernRole concernRoleObj = ConcernRoleFactory.newInstance();
        final PrimaryAddressDetails primaryAddressDetails =
          concernRoleObj.readPrimaryAddress(concernRoleKey);

        if (primaryAddressDetails.primaryAddressID == storedConcernRoleAddressDtls.addressID) {
          DynamicEvidenceTypeConverter.setAttribute(preferredInd,
            Boolean.valueOf(true));
        }

      }

    }

  }

  private AddressDtls
  insertAddress(final BDMParticipantAddressDetails details)
    throws AppException, InformationalException {

    final Address addressObj = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();
    addressDtls.addressData = details.addressData;
    addressObj.insert(addressDtls);
    return addressDtls;

  }

  private DynamicEvidenceDataDetails getDynamicEvidenceDataDetails() {

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceTypeDef evidenceTypeDef = this.evidenceTypeDefDAO
      .readActiveEvidenceTypeDefByTypeCode(evidenceTypeKey.evidenceType);

    final EvidenceTypeVersionDef evidenceTypeVersionDef =
      this.evidenceTypeVersionDefDAO.getActiveEvidenceTypeVersionAtDate(
        evidenceTypeDef, Date.getCurrentDate());

    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails =
      DynamicEvidenceDataDetailsFactory.newInstance(evidenceTypeVersionDef);

    return dynamicEvidenceDataDetails;

  }

  private void insertEvidence(
    final DynamicEvidenceDataDetails dynamicEvidenceDataDetails,
    final BDMParticipantAddressDetails details,
    final PDCCaseIDCaseParticipantRoleID pdcCaseIDCaseParticipantRoleID)
      throws AppException, InformationalException {

    final EvidenceTypeKey evidenceTypeKey = new EvidenceTypeKey();
    evidenceTypeKey.evidenceType = PDCConst.PDCADDRESS;

    final EvidenceControllerInterface evidenceControllerObj =
      (EvidenceControllerInterface) EvidenceControllerFactory.newInstance();

    final EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    evidenceDescriptorInsertDtls.participantID = details.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = evidenceTypeKey.evidenceType;
    evidenceDescriptorInsertDtls.receivedDate = Date.getCurrentDate();
    evidenceDescriptorInsertDtls.caseID =
      pdcCaseIDCaseParticipantRoleID.caseID;

    final EIEvidenceInsertDtls eiEvidenceInsertDtls =
      new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID = details.concernRoleID;
    eiEvidenceInsertDtls.descriptor.changeReason =
      EVIDENCECHANGEREASON.REPORTEDBYCLIENT;
    eiEvidenceInsertDtls.evidenceObject = dynamicEvidenceDataDetails;

    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

  }

  /**
   * Method checks for the Prospect Person
   *
   * @param concernRoleID
   * @return
   */
  private boolean isProspectPerson(final long concernRoleID) {

    boolean isProspectPerson = false;

    if (concernRoleDAO.get(concernRoleID).getConcernRoleType().getCode()
      .equals(CONCERNROLETYPE.PROSPECTPERSON)) {
      isProspectPerson = true;
    }

    return isProspectPerson;
  }

}
