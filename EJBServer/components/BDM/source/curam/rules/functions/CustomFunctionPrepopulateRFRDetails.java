package curam.rules.functions;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.codetable.impl.BDMLANGUAGEEntry;
import curam.ca.gc.bdm.entity.fact.BDMPhoneNumberFactory;
import curam.ca.gc.bdm.entity.intf.BDMPhoneNumber;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberDetails;
import curam.ca.gc.bdm.entity.struct.ReadBDMPhoneNumberKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMContactPreferenceEvidenceVO;
import curam.citizenaccount.impl.CitizenAccountHelper;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.intf.ConcernRoleAddress;
import curam.core.struct.AddressKey;
import curam.core.struct.AddressReadMultiDtls;
import curam.core.struct.AddressReadMultiDtlsList;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.ieg.impl.IEG2Context;
import curam.participant.impl.ConcernRole;
import curam.participant.impl.PhoneNumber;
import curam.piwrapper.impl.Address;
import curam.piwrapper.impl.AddressDAO;
import curam.piwrapper.impl.EmailAddress;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemUniqueKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.Trace;
import curam.util.rules.RulesParameters;
import curam.util.rules.functor.Adaptor;
import curam.util.rules.functor.AdaptorFactory;
import curam.util.transaction.TransactionInfo;
import curam.util.type.NotFoundIndicator;
import java.util.List;

/**
 * Custom function to pre-populate the application details.
 *
 * @since ADO-23496
 *
 */
public class CustomFunctionPrepopulateRFRDetails extends BDMFunctor {

  @Inject
  private AddressDAO addressDAO;

  @Inject
  BDMContactPreferenceEvidence bdmContactPreferenceEvidence;

  @Inject
  private CitizenAccountHelper citizenAccountHelper;

  public CustomFunctionPrepopulateRFRDetails() {

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
      final IEG2Context ieg2context = (IEG2Context) rulesParameters;
      final long rootEntityID = ieg2context.getRootEntityID();

      final IEG2ExecutionContext ieg2ExecutionContext =
        new IEG2ExecutionContext(rulesParameters);

      final Datastore datastore = ieg2ExecutionContext.getDatastore();

      final Entity applicationEntity = datastore.readEntity(rootEntityID);

      final Entity personEntity = applicationEntity
        .getChildEntities(datastore.getEntityType("Person"))[0];

      setPersonEntityAttributes(personEntity);
      addAddressDetails(applicationEntity, datastore, personEntity);

    } catch (final Exception e) {

      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating application details - "
        + e.getMessage());
      e.printStackTrace();
    }

    return AdaptorFactory.getBooleanAdaptor(Boolean.TRUE);
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void setPersonEntityAttributes(final Entity personEntity)
    throws AppException, InformationalException {

    final ConcernRole concernRole =
      this.citizenAccountHelper.getLoggedInConcernRole();

    final long concernRoleID = concernRole.getID();

    personEntity.setTypedAttribute("isPrimaryAppellantInd", "true");
    personEntity.setTypedAttribute("personID", concernRoleID);

    // Pre-populate Email Address
    final EmailAddress emailAddress = concernRole.getEmailAddress();
    if (emailAddress != null) {
      try {
        personEntity.setTypedAttribute(BDMConstants.PREFERRED_EMAIL_ADDRESS,
          emailAddress.getEmail());
      } catch (final Exception e) {
        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + " ERROR while prepopulating email Address - " + e.getMessage());
        e.printStackTrace();
        personEntity.setTypedAttribute(BDMConstants.PREFERRED_EMAIL_ADDRESS,
          BDMConstants.DATA_NOT_PROVIDED);
      }
    } else {
      personEntity.setTypedAttribute(BDMConstants.PREFERRED_EMAIL_ADDRESS,
        BDMConstants.DATA_NOT_PROVIDED);
    }

    // Pre-populate Phone Number
    addPhoneNumber(concernRole, personEntity);

    /**
     * <xsd:attribute name="languageCommunication" type="CT_LANGUAGE" />
     */
    // Pre-populate language of communication
    try {
      final List<BDMContactPreferenceEvidenceVO> bdmContactPreferenceEvidenceVOList =
        bdmContactPreferenceEvidence.getEvidenceValueObject(concernRoleID);
      if (!bdmContactPreferenceEvidenceVOList.isEmpty()) {
        // language needs to come from the oral language the client selected in
        // the application
        personEntity.setTypedAttribute(BDMConstants.LANGUAGE_COMMUNICATION,
          BDMLANGUAGEEntry.get(bdmContactPreferenceEvidenceVOList.get(0)
            .getPreferredOralLanguage()).toUserLocaleString());
      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating language communication - "
        + e.getMessage());
      e.printStackTrace();
    }

    personEntity.update();
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private void addAddressDetails(final Entity applicationEntity,
    final Datastore datastore, final Entity personEntity) {

    // Pre-populate address
    try {
      final ConcernRole concernRole =
        this.citizenAccountHelper.getLoggedInConcernRole();

      final ConcernRoleAddress concernroleAddressObj =
        ConcernRoleAddressFactory.newInstance();
      final AddressKey addressKey = new AddressKey();

      final ConcernRoleIDStatusCodeKey concernRoleIDStatusKey =
        new ConcernRoleIDStatusCodeKey();

      concernRoleIDStatusKey.concernRoleID = concernRole.getID();
      concernRoleIDStatusKey.statusCode = RECORDSTATUS.NORMAL;

      final AddressReadMultiDtlsList concernRoleAddressList =
        concernroleAddressObj
          .searchAddressesByConcernRoleIDAndStatus(concernRoleIDStatusKey);

      for (final AddressReadMultiDtls concernRoleAddressDtls : concernRoleAddressList.dtls) {

        addressKey.addressID = concernRoleAddressDtls.addressID;

        // Mailing Address - AT4
        if (concernRoleAddressDtls.typeCode
          .equals(CONCERNROLEADDRESSTYPE.MAILING)) {

          final Address addressObj = addressDAO.get(addressKey.addressID);
          personEntity.setTypedAttribute("mailingAddress",
            addressObj.getOneLineAddressString());
          personEntity.update();
        }

      }
    } catch (final Exception e) {
      Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
        + " ERROR while prepopulating address - " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   *
   * @param personEntity
   * @throws AppException
   * @throws InformationalException
   */
  private static void addPhoneNumber(final ConcernRole concernRole,
    final Entity personEntity) {

    // Pre-populate Phone Number
    final PhoneNumber phoneNumber = concernRole.getPrimaryPhoneNumber();
    if (phoneNumber != null) {
      try {
        final CodeTableAdmin codeTableAdminObj =
          CodeTableAdminFactory.newInstance();
        final CodeTableItemUniqueKey codeTableItemUniqueKey =
          new CodeTableItemUniqueKey();

        // Get Phone number country code
        final NotFoundIndicator nfIndicator = new NotFoundIndicator();
        final ReadBDMPhoneNumberKey readBDMPhoneNumberKey =
          new ReadBDMPhoneNumberKey();
        readBDMPhoneNumberKey.phoneNumberID = phoneNumber.getID();
        final BDMPhoneNumber bdmPhoneNumber =
          BDMPhoneNumberFactory.newInstance();
        final ReadBDMPhoneNumberDetails readBDMPhoneNumberDetails =
          bdmPhoneNumber.readBDMPhoneNumber(nfIndicator,
            readBDMPhoneNumberKey);
        String phoneCountry = "";
        if (!nfIndicator.isNotFound()) {
          codeTableItemUniqueKey.code =
            readBDMPhoneNumberDetails.phoneCountryCode;
          codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
          codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;
          phoneCountry = codeTableAdminObj.readCTIDetailsForLocaleOrLanguage(
            codeTableItemUniqueKey).annotation.trim();
        }

        final String phoneAreaCode = phoneNumber.getAreaCode();
        final String phoneNumberVal = phoneNumber.getNumber();
        final String phoneExt = phoneNumber.getExtension();
        final String phoneAreaCodeAndNumber = phoneAreaCode + phoneNumberVal;
        final StringBuffer fullPhoneNumber = new StringBuffer();
        fullPhoneNumber.append(phoneCountry).append(BDMConstants.kStringSpace)
          .append(phoneAreaCodeAndNumber
            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"))
          .append(BDMConstants.kStringSpace);
        if (!phoneExt.isEmpty()) {
          fullPhoneNumber.append("ext").append(phoneExt)
            .append(BDMConstants.kStringSpace);
        }
        personEntity.setTypedAttribute(BDMConstants.PREFERRED_PHONE_NUMBER,
          fullPhoneNumber.toString());
      } catch (final Exception e) {
        Trace.kTopLevelLogger.info(BDMConstants.BDM_LOGS_PREFIX
          + " ERROR while prepopulating phone Number - " + e.getMessage());
        e.printStackTrace();
        personEntity.setTypedAttribute(BDMConstants.PREFERRED_PHONE_NUMBER,
          BDMConstants.DATA_NOT_PROVIDED);
      }
    } else {
      personEntity.setTypedAttribute(BDMConstants.PREFERRED_PHONE_NUMBER,
        BDMConstants.DATA_NOT_PROVIDED);
    }
    personEntity.update();
  }

}
