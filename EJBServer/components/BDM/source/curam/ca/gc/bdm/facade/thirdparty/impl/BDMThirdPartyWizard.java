package curam.ca.gc.bdm.facade.thirdparty.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMRECEIVEDFROM;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYPROGRAM;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLE;
import curam.ca.gc.bdm.codetable.BDMTHIRDPARTYROLETYPE;
import curam.ca.gc.bdm.evidence.util.impl.BDMEvidenceUtil;
import curam.ca.gc.bdm.facade.thirdparty.struct.BDMThirdPartyProgramCodeDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.BDMThirdPartyProgramCodeDetailsList;
import curam.ca.gc.bdm.facade.thirdparty.struct.IndividualsWithinOrgDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.OfficeDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.OfficeDetailsTab;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyAdditionalDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyDetails;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardDetailsResult;
import curam.ca.gc.bdm.facade.thirdparty.struct.ThirdPartyEvidenceWizardKey;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMTHIRDPARTYCONTACT;
import curam.codetable.CASEPARTICIPANTROLETYPE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.WizardProperties;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernRoleAddressFactory;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.ExternalPartyOfficeFactory;
import curam.core.sl.entity.fact.ExternalPartyOfficeMemberFactory;
import curam.core.sl.entity.struct.ExternalPartyKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtlsList;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeMemberDtlsList;
import curam.core.sl.fact.CaseParticipantRoleFactory;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.CaseIDAndParticipantRoleIDKey;
import curam.core.sl.struct.CaseParticipantRoleIDStruct;
import curam.core.sl.struct.RepresentativeRegistrationDetails;
import curam.core.sl.struct.ReturnEvidenceDetails;
import curam.core.struct.AddressConcernRoleTypeStatusDtlsList;
import curam.core.struct.AddressConcernRoleTypeStatusKey;
import curam.core.struct.AddressKey;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.OtherAddressData;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;
import curam.util.type.StringList;
import curam.wizardpersistence.impl.WizardPersistentState;

/**
 *
 * Feature Task-94325 DEV: Manage Third Party
 * This class is used to handler manage third party related activities
 *
 * @author prashant.raut
 *
 */
public class BDMThirdPartyWizard
  extends curam.ca.gc.bdm.facade.thirdparty.base.BDMThirdPartyWizard {

  WizardPersistentState wizardPersistentState = null;

  public BDMThirdPartyWizard() {

    GuiceWrapper.getInjector().injectMembers(this);
    wizardPersistentState = new WizardPersistentState();

  }

  final BDMUtil bdmUtil = new BDMUtil();

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method is used to initialize wizard and return existing state of
   * wizard.
   */
  @Override
  public ThirdPartyEvidenceWizardDetailsResult
    getThirdParytEvidenceWizardDetails(
      final ThirdPartyEvidenceWizardKey wizardKey)
      throws AppException, InformationalException {

    ThirdPartyEvidenceWizardDetailsResult thirdPartyEvidenceWizardResult =
      null;
    if (wizardKey.wizardStateID != 0) {
      thirdPartyEvidenceWizardResult =
        (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
          .read(wizardKey.wizardStateID);
      thirdPartyEvidenceWizardResult.wizardKey.wizardStateID =
        wizardKey.wizardStateID;
      thirdPartyEvidenceWizardResult.wizardProperties.wizardMenu =
        "BDMCreateThirdPartyEvidenceWizard.properties";

      thirdPartyEvidenceWizardResult.thirdPartyDetails.displayAdditionalInfoCluster =
        thirdPartyEvidenceWizardResult.thirdPartyDetails.thirdPartyParticipantRoleID == 0;

    } else {

      thirdPartyEvidenceWizardResult =
        new ThirdPartyEvidenceWizardDetailsResult();

      final WizardPersistentState wizardPersistentState =
        new WizardPersistentState();
      final long wizardStateID =
        wizardPersistentState.create(thirdPartyEvidenceWizardResult);

      // setting from date as system date
      thirdPartyEvidenceWizardResult.thirdPartyDetails.fromDate =
        TransactionInfo.getSystemDate();
      thirdPartyEvidenceWizardResult.wizardKey.wizardStateID = wizardStateID;
      final WizardProperties wizardProperties = new WizardProperties();
      wizardProperties.wizardMenu =
        "BDMCreateThirdPartyEvidenceWizard.properties";
      thirdPartyEvidenceWizardResult.wizardProperties = wizardProperties;

    }
    return thirdPartyEvidenceWizardResult;
  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method is used to save additional third party details and submit third
   * party wizard
   */
  @Override
  public void saveThirdPartyAdditionalDetails(
    final ThirdPartyAdditionalDetails thirdPartyAdditionalDetails,
    final ThirdPartyEvidenceWizardKey wizardKey)
    throws AppException, InformationalException {

    final BDMUtil util = new BDMUtil();
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);

    validateStep4(thirdPartyEvd, thirdPartyAdditionalDetails);

    thirdPartyEvd.thirdPartyAdditionalDetails = thirdPartyAdditionalDetails;
    long representativeConcernRoleID = 0;
    // BUG 99723 - START

    if (thirdPartyEvd.thirdPartyDetails.roleType
      .equalsIgnoreCase(BDMTHIRDPARTYROLETYPE.INDIVIDUAL)
      && thirdPartyEvd.thirdPartyDetails.thirdPartyParticipantRoleID != 0) {
      representativeConcernRoleID =
        thirdPartyEvd.thirdPartyDetails.thirdPartyParticipantRoleID;
      thirdPartyEvd.thirdPartyDetails.thirdPartyCaseParticipantRoleID =
        util.insertCaseParticipantRole(thirdPartyEvd.thirdPartyDetails.caseID,
          representativeConcernRoleID, CASEPARTICIPANTROLETYPE.THIRDPARTY);
    } else if (thirdPartyEvd.thirdPartyDetails.roleType
      .equalsIgnoreCase(BDMTHIRDPARTYROLETYPE.INDIVIDUAL)) {
      final RepresentativeRegistrationDetails representativeRegistrationDetails =
        util.registerRepresentative(thirdPartyEvd);
      representativeConcernRoleID =
        representativeRegistrationDetails.representativeDtls.concernRoleID;

      thirdPartyEvd.thirdPartyDetails.thirdPartyCaseParticipantRoleID =
        util.insertCaseParticipantRole(thirdPartyEvd.thirdPartyDetails.caseID,
          representativeConcernRoleID, CASEPARTICIPANTROLETYPE.THIRDPARTY);
    } else if (thirdPartyEvd.thirdPartyDetails.roleType
      .equalsIgnoreCase(BDMTHIRDPARTYROLETYPE.ORGANIZATION)) {

      final curam.core.sl.intf.CaseParticipantRole caseParticipantRoleObj =
        CaseParticipantRoleFactory.newInstance();

      final CaseIDAndParticipantRoleIDKey caseIDAndParticipantRoleIDKey =
        new CaseIDAndParticipantRoleIDKey();
      caseIDAndParticipantRoleIDKey.caseID =
        thirdPartyEvd.thirdPartyDetails.caseID;
      caseIDAndParticipantRoleIDKey.participantRoleID =
        thirdPartyEvd.thirdPartyDetails.thirdPartyParticipantRoleID;

      final CaseParticipantRoleIDStruct caseParticipantRoleIDStruct =
        caseParticipantRoleObj
          .readCaseParticipantRoleIDByParticipantRoleIDAndCaseID(
            caseIDAndParticipantRoleIDKey);

      if (caseParticipantRoleIDStruct.caseParticipantRoleID == 0l) {

        thirdPartyEvd.thirdPartyDetails.thirdPartyCaseParticipantRoleID = util
          .insertCaseParticipantRole(thirdPartyEvd.thirdPartyDetails.caseID,
            thirdPartyEvd.thirdPartyDetails.thirdPartyParticipantRoleID,
            CASEPARTICIPANTROLETYPE.THIRDPARTY);
      } else {
        thirdPartyEvd.thirdPartyDetails.thirdPartyCaseParticipantRoleID =
          caseParticipantRoleIDStruct.caseParticipantRoleID;
      }
      // BUG 99723 - END

    }

    wizardPersistentState.modify(wizardKey.wizardStateID, thirdPartyEvd);
    final ReturnEvidenceDetails evidenceKey =
      new BDMEvidenceUtil().createThirdPartyContactEvidence(thirdPartyEvd);

  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method will save individual details in wizard state.
   */
  @Override
  public void saveIndividualDetails(
    final IndividualsWithinOrgDetails individuals,
    final ThirdPartyEvidenceWizardKey wizardKey)
    throws AppException, InformationalException {

    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);
    validateStep3(thirdPartyEvd, individuals);
    thirdPartyEvd.selectedIndividual = individuals;
    wizardPersistentState.modify(wizardKey.wizardStateID, thirdPartyEvd);
  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method will save third party details in wizard state.
   */
  @Override
  public void saveThirdPartyDetails(final ThirdPartyDetails thirdPartyDetails,
    final ThirdPartyEvidenceWizardKey wizardKey)
    throws AppException, InformationalException {

    validateStep1(thirdPartyDetails);
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);
    thirdPartyEvd.thirdPartyDetails = thirdPartyDetails;
    final curam.core.intf.Address address = AddressFactory.newInstance();
    final long thidPartyParticipantRoleID =
      thirdPartyDetails.thirdPartyParticipantRoleID;
    if (0 != thidPartyParticipantRoleID) {
      final ExternalPartyKey externalPartyKey = new ExternalPartyKey();
      externalPartyKey.concernRoleID = thidPartyParticipantRoleID;
      final ExternalPartyOfficeDtlsList externalPartyOffDtlsList =
        ExternalPartyOfficeFactory.newInstance()
          .searchByExternalParty(externalPartyKey);
      if (null != externalPartyOffDtlsList
        && !externalPartyOffDtlsList.dtls.isEmpty()) {
        for (final ExternalPartyOfficeDtls extOfficeDtls : externalPartyOffDtlsList.dtls) {
          final OfficeDetails officeDetails = new OfficeDetails();
          final AddressKey addressKey = new AddressKey();
          addressKey.addressID = extOfficeDtls.primaryAddressID;
          final OtherAddressData otherAddressData =
            address.readAddressData(addressKey);
          address.getOneLineAddressString(otherAddressData);
          if (null != otherAddressData) {
            officeDetails.officeAddress = otherAddressData.addressData;
          }
          officeDetails.officeID = extOfficeDtls.externalPartyOfficeID;
          officeDetails.officeName = extOfficeDtls.name;
          thirdPartyEvd.dtls.officeDetails.add(officeDetails);
        }
      } else {
        //// START Bug 110516: Unable to add an existing Person/Prospect Person
        //// as Third Party Contact

        final OtherAddressData mailingAddressotherData =
          bdmUtil.getAddressDataByConcernRole(thidPartyParticipantRoleID,
            CONCERNROLEADDRESSTYPE.PRIVATE);

        final OfficeDetails officeDetails = new OfficeDetails();

        if (null != mailingAddressotherData) {
          officeDetails.officeAddress = mailingAddressotherData.addressData;
        }

        if (thirdPartyEvd.dtls.officeDetails.size() == CuramConst.gkZero) {
          thirdPartyEvd.dtls.officeDetails.add(officeDetails);
        }

      }

    }
    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION
      .equals(thirdPartyDetails.roleType)) {
      thirdPartyDetails.displayAdditionalInfoCluster = false;
    } else {
      thirdPartyDetails.displayAdditionalInfoCluster = true;
    }
    wizardPersistentState.modify(wizardKey.wizardStateID, thirdPartyEvd);
  }

  /**
   * Feature Task-94325 DEV: Manage Third Party
   * This method will save office details
   */
  @Override
  public void saveOfficeDetails(final OfficeDetailsTab officeDetails,
    final ThirdPartyEvidenceWizardKey wizardKey)
    throws AppException, InformationalException {

    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd =
      (ThirdPartyEvidenceWizardDetailsResult) wizardPersistentState
        .read(wizardKey.wizardStateID);
    validateStep2(thirdPartyEvd, officeDetails);

    final curam.core.sl.entity.intf.ExternalPartyOfficeMember extOfficeMember =
      ExternalPartyOfficeMemberFactory.newInstance();
    final curam.core.facade.intf.ConcernRole concernRole =
      curam.core.facade.fact.ConcernRoleFactory.newInstance();

    final StringList officeDetailsStringList = StringUtil
      .tabText2StringListWithTrim(officeDetails.officeIDetailsTABList);
    // Fix BUG-98040 Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    thirdPartyEvd.individuallList.clear();
    thirdPartyEvd.userSelectedOffices.dtls.clear();
    final IndividualsWithinOrgDetails naIndWithinOrg =
      new IndividualsWithinOrgDetails();
    naIndWithinOrg.concernRoleName =
      TransactionInfo.getProgramLocale().equals(BDMConstants.gkLocaleEN)
        ? BDMConstants.kNotApplicable : BDMConstants.kNotApplicable_fr;
    thirdPartyEvd.individuallList.add(naIndWithinOrg);
    if (!officeDetailsStringList.isEmpty()) {
      for (final String officeDetailsList : officeDetailsStringList) {
        final StringList eachOfficeDetails =
          StringUtil.delimitedText2StringList(officeDetailsList,
            CuramConst.gkPipeDelimiterChar);
        if (eachOfficeDetails != null && !eachOfficeDetails.isEmpty()) {
          final String officeID = eachOfficeDetails.get(0);
          final String officeAddress = eachOfficeDetails.get(1);
          final OfficeDetails userSelOffice = new OfficeDetails();
          userSelOffice.officeID = Long.valueOf(officeID);
          userSelOffice.officeAddress = officeAddress;
          thirdPartyEvd.userSelectedOffices.dtls.add(userSelOffice);
          final ExternalPartyOfficeKey key = new ExternalPartyOfficeKey();
          key.externalPartyOfficeID = Long.valueOf(officeID);
          final ExternalPartyOfficeMemberDtlsList extPartyOfficeMemberList =
            extOfficeMember.searchByExternalPartyOfficeID(key);
          if (!extPartyOfficeMemberList.dtls.isEmpty()) {
            for (final ExternalPartyOfficeMemberDtls extPartyOfficeMember : extPartyOfficeMemberList.dtls) {
              final IndividualsWithinOrgDetails indWithinOrg =
                new IndividualsWithinOrgDetails();
              final NotFoundIndicator nfInd = new NotFoundIndicator();
              final curam.core.struct.ConcernRoleKey concernKey =
                new curam.core.struct.ConcernRoleKey();
              concernKey.concernRoleID = extPartyOfficeMember.concernRoleID;
              final ConcernRoleDtls concernRoleDtls =
                concernRole.readConcernRole(concernKey);
              // BUG 99723 - we should also pass member's concernRoleID instead
              indWithinOrg.concernRoleID = extPartyOfficeMember.concernRoleID;
              indWithinOrg.concernRoleName = concernRoleDtls.concernRoleName;
              indWithinOrg.officeAddress = officeAddress;
              indWithinOrg.officeMemberID =
                extPartyOfficeMember.officeMemberID;
              thirdPartyEvd.individuallList.add(indWithinOrg);
            }
          }
        }
      }
    }
    wizardPersistentState.modify(wizardKey.wizardStateID, thirdPartyEvd);
  }

  /**
   * Validate inputs for wizard step 1.
   *
   * @param thirdPartyDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep1(final ThirdPartyDetails thirdPartyDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)) {

      if (thirdPartyDetails.poaClass.isEmpty()) {
        // BR-1
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_POA_CLASS_FOR_ROLE_POA),
          "", InformationalType.kError);
      }

      if (thirdPartyDetails.poaType.isEmpty()) {
        // BR-2
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_POA_TYPE_FOR_ROLE_POA),
          "", InformationalType.kError);
      }
    }
    // BUG CR 97309 An External Party should be selected as Third Party Contact
    // if Role Type is 'Organization' in order to proceed
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && (thirdPartyDetails.firstName.isEmpty()
        || thirdPartyDetails.lastName.isEmpty())
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0) {
      // BR-3
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_ORGNIZATION_OR_REPRESENTATIVE_DETAILS),
          "", InformationalType.kError);
    }

    // BUG CR 97309 An External Party should be selected as Third Party Contact
    // if Role Type is 'Organization' in order to proceed
    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0) {
      // BR-17
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_ORGNIZATION_OR_REPRESENTATIVE_DETAILS),
          "", InformationalType.kError);
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {
      // BR-5
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_ORGANIZATION),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PERSON.equals(dtls.concernRoleType)
        || CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {
        // BR-6
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_INDIVIDUAL_WHEN_PERSON_ALREADY_EXISTS),
            "", InformationalType.kError);
      }
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !(thirdPartyDetails.firstName.isEmpty()
        && thirdPartyDetails.lastName.isEmpty()
        && thirdPartyDetails.middleName.isEmpty()
        && thirdPartyDetails.title.isEmpty())) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-7
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_NAME_NOT_EMPTY_FOR_ROLE_TYPE_ORGANIZATION_WHEN_EXTERNAL_PARTY_ALREAY_EXISTS),
            "", InformationalType.kError);
      }
    }

    if (!(BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && (BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role)))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {

        // look up mailing address
        if (!hasMailingAddress(
          thirdPartyDetails.thirdPartyParticipantRoleID)) {
          // BR-9
          ValidationManagerFactory.getManager()
            .addInfoMgrExceptionWithLookup(new AppException(
              BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_1),
              "", InformationalType.kError);
        }
      }
    }

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {

        // look up mailing address
        if (!hasMailingAddress(
          thirdPartyDetails.thirdPartyParticipantRoleID)) {
          // BR-11
          ValidationManagerFactory.getManager()
            .addInfoMgrExceptionWithLookup(new AppException(
              BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_3),
              "", InformationalType.kError);
        }
      }
    }

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.PERSON.equals(dtls.concernRoleType)
        || CONCERNROLETYPE.PROSPECTPERSON.equals(dtls.concernRoleType)) {

        // BR-15
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_CANNOT_SELECT_ROLE_TYPE_ORGANIZATION),
          "", InformationalType.kError);
      }
    }

    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-16
        ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
          new AppException(
            BDMTHIRDPARTYCONTACT.ERR_CANNOT_SELECT_ROLE_TYPE_INDIVIDUAL),
          "", InformationalType.kError);
      }
    }

    // Date received cannot be in future.
    if (thirdPartyDetails.receivedDate.after(Date.getCurrentDate())) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECEIVEDDATE_CANNOT_BE_IN_FUTURE),
        "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Look up mailing address for a concern role.
   *
   * @param concernRoleID
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected boolean hasMailingAddress(final long concernRoleID)
    throws AppException, InformationalException {

    boolean hasMailAddress = false;

    final AddressConcernRoleTypeStatusKey addressSearchKey =
      new AddressConcernRoleTypeStatusKey();
    addressSearchKey.concernRoleID = concernRoleID;
    addressSearchKey.statusCode = RECORDSTATUS.NORMAL;
    addressSearchKey.typeCode = CONCERNROLEADDRESSTYPE.MAILING;
    final AddressConcernRoleTypeStatusDtlsList addressList =
      ConcernRoleAddressFactory.newInstance()
        .searchAddressByConcernRoleTypeStatus(addressSearchKey);

    if (!addressList.dtls.isEmpty()) {
      hasMailAddress = true;
    }

    return hasMailAddress;
  }

  /**
   * Validate inputs for wizard step 2.
   *
   * @param thirdPartyEvd
   * @param officeDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep2(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final OfficeDetailsTab officeDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ThirdPartyDetails thirdPartyDetails =
      thirdPartyEvd.thirdPartyDetails;

    if (BDMTHIRDPARTYROLETYPE.ORGANIZATION.equals(thirdPartyDetails.roleType)
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && officeDetails.officeIDetailsTABList.isEmpty()) {

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyDetails.thirdPartyParticipantRoleID;
      final ConcernRoleDtls dtls =
        ConcernRoleFactory.newInstance().read(crKey);

      if (CONCERNROLETYPE.EXTERNALPARTY.equals(dtls.concernRoleType)) {
        // BR-8
        ValidationManagerFactory.getManager()
          .addInfoMgrExceptionWithLookup(new AppException(
            BDMTHIRDPARTYCONTACT.ERR_MISSING_OFFICE_FOR_ROLE_TYPE_ORGANIZATION),
            "", InformationalType.kError);
      }
    }

    informationalManager.failOperation();
  }

  /**
   * Validate inputs for wizard step 3.
   *
   * @param thirdPartyEvd
   * @param individuals
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep3(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final IndividualsWithinOrgDetails individuals)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Bug fix 98040 - Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    if (null != thirdPartyEvd.userSelectedOffices
      && thirdPartyEvd.userSelectedOffices.dtls.size() > 1
      && individuals.concernRoleName.equals(BDMConstants.kNotApplicable)) {
      // BR-14
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MORE_THAN_ONE_OFFICE_SELECTED_FOR_EXTERNAL_PARTY),
          "", InformationalType.kError);
    }
    // Fix BUG-98040 Incorrect error message and N/A option on screen 2 and 3
    // when adding an Organization as Third Party
    // BUG 99723 - we should check member's concernRoleID instead of
    // externalParty's ID
    if (thirdPartyEvd.individuallList.size() == 0
      || StringUtil.isNullOrEmpty(individuals.concernRoleName)) {
      // BR-18
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_NEED_TO_SELECT_INDIVIDUAL_IN_ORGANIZATION_OR_NA),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Validate inputs for wizard step 4.
   *
   * @param thirdPartyEvd
   * @param additionalDetails
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateStep4(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd,
    final ThirdPartyAdditionalDetails additionalDetails)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ThirdPartyDetails thirdPartyDetails =
      thirdPartyEvd.thirdPartyDetails;

    final OtherAddressData mailingAddressData = new OtherAddressData();
    mailingAddressData.addressData = additionalDetails.mailingAddress;

    final OtherAddressData residentialAddressData = new OtherAddressData();
    residentialAddressData.addressData = additionalDetails.residentialAddress;

    final BDMUtil bdmUtil = new BDMUtil();
    // BR-08
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID == 0
      && bdmUtil.isAddressEmpty(mailingAddressData, residentialAddressData,
        additionalDetails.isMailingAddSame)) {
      // BR-10
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_2),
          "", InformationalType.kError);
    }

    // BR-09
    if (BDMTHIRDPARTYROLETYPE.INDIVIDUAL.equals(thirdPartyDetails.roleType)
      && !(BDMTHIRDPARTYROLE.POA.equals(thirdPartyDetails.role)
        || BDMTHIRDPARTYROLE.PERSONAL.equals(thirdPartyDetails.role))
      && thirdPartyDetails.thirdPartyParticipantRoleID != 0
      && !hasMailingAddress(thirdPartyDetails.thirdPartyParticipantRoleID)) {
      // BR-10
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_MISSING_MAILING_ADDR_WHEN_REGISTER_3RD_PARTY_CONTACT_2),
          "", InformationalType.kError);
    }

    if (BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (additionalDetails.modeOfReceipt.isEmpty()
        || additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-12
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_MUST_BE_SELECTED),
          "", InformationalType.kError);
    }

    if (!BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (!additionalDetails.modeOfReceipt.isEmpty()
        || !additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-13
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_CANNOT_BE_SELECTED),
          "", InformationalType.kError);
    }

    final boolean isPhoneDtlsEntered =
      !additionalDetails.phoneAreaCode.trim().isEmpty()
        || !additionalDetails.phoneCountryCode.trim().isEmpty()
        || !additionalDetails.phoneExtension.trim().isEmpty()
        || !additionalDetails.phoneNumber.trim().isEmpty();

    final boolean isAltPhoneDtlsEntered =
      !additionalDetails.altPhoneAreaCode.trim().isEmpty()
        || !additionalDetails.altPhoneCountryCode.trim().isEmpty()
        || !additionalDetails.altPhoneExtension.trim().isEmpty()
        || !additionalDetails.altPhoneNumber.trim().isEmpty();

    if (isPhoneDtlsEntered && additionalDetails.phoneType.trim().isEmpty()
      || isAltPhoneDtlsEntered
        && additionalDetails.altPhoneType.trim().isEmpty()) {
      // BR-18
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(
          BDMTHIRDPARTYCONTACT.ERR_PHONE_TYPE_MISSING_FOR_PHONE_NUMBER),
        "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * Validate before modification.
   *
   * @param thirdPartyEvd
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateModification(
    final ThirdPartyEvidenceWizardDetailsResult thirdPartyEvd)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final ThirdPartyAdditionalDetails additionalDetails =
      thirdPartyEvd.thirdPartyAdditionalDetails;

    if (BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (additionalDetails.modeOfReceipt.isEmpty()
        || additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-12
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_MUST_BE_SELECTED),
          "", InformationalType.kError);
    }

    if (!BDMRECEIVEDFROM.FOREIGN_GOVERNMENT
      .equals(additionalDetails.receivedFrom)
      && (!additionalDetails.modeOfReceipt.isEmpty()
        || !additionalDetails.receivedFromCountry.isEmpty())) {
      // BR-13
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_RECV_FROM_COUNTRY_AND_MODE_OF_RECPT_CANNOT_BE_SELECTED),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();

  }

  /**
   * Validate before delete an office member.
   *
   * @throws AppException
   * @throws InformationalException
   */
  protected void validateDeleteOfficeMember()
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // TODO: need to figure this out
    final boolean isOfficeMemberIndividualWithinOrg = true;

    if (isOfficeMemberIndividualWithinOrg) {
      // BR-17
      ValidationManagerFactory.getManager()
        .addInfoMgrExceptionWithLookup(new AppException(
          BDMTHIRDPARTYCONTACT.ERR_CANNOT_DELETE_OFFICE_MEMBER_WHO_IS_THIRD_PARTY_CONTACT),
          "", InformationalType.kError);
    }

    informationalManager.failOperation();
  }

  /**
   * return program code list
   */
  @Override
  public BDMThirdPartyProgramCodeDetailsList listBDMThirdPartyProgramCodes()
    throws AppException, InformationalException {

    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();

    final BDMThirdPartyProgramCodeDetailsList programCodeDetailsList =
      new BDMThirdPartyProgramCodeDetailsList();

    // CodeTableItemDetails struct
    BDMThirdPartyProgramCodeDetails codeTableItemDetails = null;
    final String codetableName = BDMTHIRDPARTYPROGRAM.TABLENAME;

    final String locale = TransactionInfo.getProgramLocale();
    final CodeTableItemDetailsList codeTableItemList = codeTableAdminObj
      .listAllItemsForLocaleAndLanguage(codetableName, locale);

    for (final CodeTableItemDetails ctDetails : codeTableItemList.dtls
      .items()) {
      codeTableItemDetails = new BDMThirdPartyProgramCodeDetails();
      codeTableItemDetails.assign(ctDetails);

      programCodeDetailsList.thirdPartyPrograms.add(codeTableItemDetails);
    }
    return programCodeDetailsList;
  }

}
