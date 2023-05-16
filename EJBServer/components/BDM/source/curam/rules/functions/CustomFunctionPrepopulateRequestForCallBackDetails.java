package curam.rules.functions;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMPHONECOUNTRY;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMEmailEvidenceVO;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidence;
import curam.ca.gc.bdm.lifeevent.impl.BDMPhoneEvidenceVO;
import curam.citizenaccount.impl.CitizenAccountHelper;
import curam.datastore.impl.Datastore;
import curam.datastore.impl.Entity;
import curam.dynamicevidence.impl.DynamicEvidenceDataDetails;
import curam.ieg.impl.IEG2Context;
import curam.participant.person.impl.Person;
import curam.participant.person.impl.PersonDAO;
import curam.pdc.impl.PDCConst;
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
import curam.util.type.Date;
import java.util.List;

public class CustomFunctionPrepopulateRequestForCallBackDetails
  extends BDMFunctor {

  @Inject
  private PersonDAO personDAO;

  @Inject
  private CitizenAccountHelper citizenAccountHelper;

  public CustomFunctionPrepopulateRequestForCallBackDetails() {

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

      setPersonEntityAttributes(personEntity, datastore);

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
  private void setPersonEntityAttributes(final Entity personEntity,
    final Datastore datastore) throws AppException, InformationalException {

    final long concernRoleID =
      (long) personEntity.getTypedAttribute("personID");

    personEntity.setTypedAttribute("personID", concernRoleID);

    final Person person = personDAO.get(concernRoleID);

    personEntity.setTypedAttribute("firstName", person.getFirstName());
    personEntity.setTypedAttribute("lastName", person.getLastName());

    final BDMEvidenceUtil bdmEvidenceUtil = new BDMEvidenceUtil();

    final List<DynamicEvidenceDataDetails> contactEvidence =
      bdmEvidenceUtil.getEvdDtlsByConcernroleIDandEvidenceType(concernRoleID,
        PDCConst.PDCCONTACTPREFERENCES);
    if (contactEvidence.size() != 0) {
      final String languagePref = contactEvidence.get(0)
        .getAttribute(BDMConstants.kpreferredOralLanguage).getValue();
      if (!curam.util.resources.StringUtil.isNullOrEmpty(languagePref)) {
        personEntity.setTypedAttribute("contactLanguage", languagePref);
      }
    }

    boolean isExistingPhoneAvailable = false;
    final List<BDMPhoneEvidenceVO> phoneEvidenceList =
      new BDMPhoneEvidence().getEvidenceValueObject(concernRoleID);
    for (final BDMPhoneEvidenceVO phoneEvidenceVO : phoneEvidenceList) {
      // only retrieve phone number with "from date" is equal to today or in
      // the past; "to date" is blank or in the future
      if (!phoneEvidenceVO.getFromDate().after(Date.getCurrentDate())
        && (phoneEvidenceVO.getToDate() == null
          || phoneEvidenceVO.getToDate().isZero()
          || phoneEvidenceVO.getFromDate().after(Date.getCurrentDate()))) {
        isExistingPhoneAvailable = true;
        final Entity phoneNumberEntity = datastore.newEntity("PhoneNumber");
        personEntity.addChildEntity(phoneNumberEntity);
        final String phoneAreaCodeAndNumber =
          phoneEvidenceVO.getPhoneAreaCode()
            + phoneEvidenceVO.getPhoneNumber();
        final String phoneExt = phoneEvidenceVO.getPhoneExtension();
        final CodeTableAdmin codeTableAdminObj =
          CodeTableAdminFactory.newInstance();
        final CodeTableItemUniqueKey codeTableItemUniqueKey =
          new CodeTableItemUniqueKey();
        codeTableItemUniqueKey.code = phoneEvidenceVO.getPhoneCountryCode();
        codeTableItemUniqueKey.locale = TransactionInfo.getProgramLocale();
        codeTableItemUniqueKey.tableName = BDMPHONECOUNTRY.TABLENAME;
        final StringBuffer existingPhoneNumber = new StringBuffer();
        existingPhoneNumber
          .append(codeTableAdminObj.readCTIDetailsForLocaleOrLanguage(
            codeTableItemUniqueKey).annotation.trim())
          .append(BDMConstants.kStringSpace)
          .append(phoneAreaCodeAndNumber
            .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"))
          .append(BDMConstants.kStringSpace);
        if (phoneExt != null) {
          existingPhoneNumber.append("ext").append(phoneExt)
            .append(BDMConstants.kStringSpace);
        }
        phoneNumberEntity.setTypedAttribute("existingPhoneNumber",
          existingPhoneNumber.toString());
        phoneNumberEntity.update();
      }
    }

    if (isExistingPhoneAvailable) {
      personEntity.setTypedAttribute("isExistingPhoneAvailable", "true");
    } else {
      personEntity.setTypedAttribute("isExistingPhoneAvailable", "false");
    }
    personEntity.update();

    boolean isExistingEmailAvailable = false;
    final List<BDMEmailEvidenceVO> bdmEmailEvidenceVOList =
      new BDMEmailEvidence().getEvidenceValueObject(concernRoleID);
    for (final BDMEmailEvidenceVO bdmEmailEvidenceVO : bdmEmailEvidenceVOList) {
      // only retrieve email address with "from date" is equal to today or in
      // the past; "to date" is blank or in the future
      if (!bdmEmailEvidenceVO.getFromDate().after(Date.getCurrentDate())
        && (bdmEmailEvidenceVO.getToDate() == null
          || bdmEmailEvidenceVO.getToDate().isZero()
          || bdmEmailEvidenceVO.getFromDate().after(Date.getCurrentDate()))) {
        isExistingEmailAvailable = true;
        final String existingEmailAdr = bdmEmailEvidenceVO.getEmailAddress();
        final Entity emailAddressEntity = datastore.newEntity("EmailAddress");
        personEntity.addChildEntity(emailAddressEntity);
        emailAddressEntity.setTypedAttribute("existingEmailAdr",
          existingEmailAdr);
        emailAddressEntity.update();
      }
    }

    if (isExistingEmailAvailable) {
      personEntity.setTypedAttribute("isExistingEmailAvailable", "true");
    } else {
      personEntity.setTypedAttribute("isExistingEmailAvailable", "false");
    }

    personEntity.update();
  }
}
