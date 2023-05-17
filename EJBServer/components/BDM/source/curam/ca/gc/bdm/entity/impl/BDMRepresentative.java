package curam.ca.gc.bdm.entity.impl;

import curam.codetable.BANKACCOUNTSTATUS;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.CONCERNROLEADDRESSTYPE;
import curam.codetable.CONCERNROLESTATUS;
import curam.codetable.CONCERNROLETYPE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.RECORDSTATUS;
import curam.codetable.SENSITIVITY;
import curam.core.fact.AddressFactory;
import curam.core.fact.ConcernFactory;
import curam.core.fact.ConcernRolePhoneNumberFactory;
import curam.core.fact.MaintainConcernRoleBankAcFactory;
import curam.core.fact.PhoneNumberFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.intf.Address;
import curam.core.intf.Concern;
import curam.core.intf.ConcernRolePhoneNumber;
import curam.core.intf.MaintainConcernRoleBankAc;
import curam.core.intf.PhoneNumber;
import curam.core.intf.UniqueID;
import curam.core.sl.entity.struct.RepresentativeDtls;
import curam.core.sl.entity.struct.RepresentativeRegistrationDetails;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorInsertDtls;
import curam.core.sl.infrastructure.impl.EIEvidenceInsertDtls;
import curam.core.struct.AddressDtls;
import curam.core.struct.BankAccountDetails;
import curam.core.struct.ConcernDtls;
import curam.core.struct.ConcernRoleAddressDtls;
import curam.core.struct.ConcernRoleDtls;
import curam.core.struct.ConcernRolePhoneNumberDtls;
import curam.core.struct.MaintainBankAccountKey;
import curam.core.struct.PhoneNumberDtls;
import curam.message.BPOCONCERNROLE;
import curam.message.BPOREPRESENTATIVE;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;

public class BDMRepresentative
  extends curam.ca.gc.bdm.entity.base.BDMRepresentative {

  @Override
  protected void preinsert(final RepresentativeDtls details,
    final RepresentativeRegistrationDetails representativeRegistrationDetails)
    throws AppException, InformationalException {

    // unique id generator
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    // invoke validation
    validateRegisterRepresentative(details);

    // BEGIN, CR00061099, POH
    // EvidenceController business object
    final curam.core.sl.infrastructure.impl.EvidenceControllerInterface evidenceControllerObj =
      (curam.core.sl.infrastructure.impl.EvidenceControllerInterface) curam.core.sl.infrastructure.fact.EvidenceControllerFactory
        .newInstance();

    // Evidence descriptor details
    EvidenceDescriptorInsertDtls evidenceDescriptorInsertDtls =
      new EvidenceDescriptorInsertDtls();

    // Evidence Interface details
    EIEvidenceInsertDtls eiEvidenceInsertDtls = new EIEvidenceInsertDtls();
    // END, CR00061099

    // variables used to insert address
    final Address addressObj = AddressFactory.newInstance();
    final AddressDtls addressDtls = new AddressDtls();

    // variables used to insert phone number
    final PhoneNumber phoneNumberObj = PhoneNumberFactory.newInstance();
    final PhoneNumberDtls phoneNumberDtls = new PhoneNumberDtls();

    // Bank account maintenance object and key
    final MaintainConcernRoleBankAc maintainConcernRoleBankAcObj =
      MaintainConcernRoleBankAcFactory.newInstance();
    final MaintainBankAccountKey maintainBankAccountKey =
      new MaintainBankAccountKey();

    // Bank Account Details Struct
    final BankAccountDetails bankAccountDetails = new BankAccountDetails();

    // variables used to insert concern
    final Concern concernObj = ConcernFactory.newInstance();
    final ConcernDtls concernDtls = new ConcernDtls();

    // variables used to insert concern role
    final ConcernRoleDtls concernRoleDtls = new ConcernRoleDtls();

    // variables used to insert concern role address
    final ConcernRoleAddressDtls concernRoleAddressDtls =
      new ConcernRoleAddressDtls();

    // variables used to insert concern role phone number
    final ConcernRolePhoneNumber concernRolePhoneNumberObj =
      ConcernRolePhoneNumberFactory.newInstance();
    final ConcernRolePhoneNumberDtls concernRolePhoneNumberDtls =
      new ConcernRolePhoneNumberDtls();

    // current date
    final Date currentDate = Date.getCurrentDate();

    // BEGIN, CR00099256, KH
    Date startDate = representativeRegistrationDetails.startDate;

    // If the start date has not been specified, set it to today
    if (startDate.equals(Date.kZeroDate)) {
      startDate = currentDate;
    }
    // END, CR00099256

    // START - BUG - 100608 - if residential address data is provided then only
    // create the record

    addressDtls.addressData = representativeRegistrationDetails.addressData;

    // insert address
    addressObj.insert(addressDtls);

    // registration date must be supplied
    if (representativeRegistrationDetails.registrationDate
      .equals(Date.kZeroDate)) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOCONCERNROLE.ERR_CONCERNROLE_FV_REGISTRATIONDATE_EMPTY),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          1);
    }

    // registration date must be later than today
    if (representativeRegistrationDetails.registrationDate
      .after(currentDate)) {

      curam.core.sl.infrastructure.impl.ValidationManagerFactory.getManager()
        .throwWithLookup(
          new AppException(
            BPOREPRESENTATIVE.ERR_REPRESENTATIVE_FV_REGDATE_AFTER_TODAY),
          curam.core.sl.infrastructure.impl.ValidationManagerConst.kSetOne,
          0);
    }

    if (representativeRegistrationDetails.phoneNumber.length() > 0) {

      // need to generate uniqueID as pre insert is not setting this (yet)
      phoneNumberDtls.phoneNumberID = uniqueIDObj.getNextID();
      phoneNumberDtls.phoneAreaCode =
        representativeRegistrationDetails.phoneAreaCode;
      phoneNumberDtls.phoneCountryCode =
        representativeRegistrationDetails.phoneCountryCode;
      phoneNumberDtls.phoneNumber =
        representativeRegistrationDetails.phoneNumber;
      phoneNumberDtls.phoneExtension =
        representativeRegistrationDetails.phoneExtension;

      // insert phone number
      phoneNumberObj.insert(phoneNumberDtls);
    }

    // need to generate uniqueID as pre insert is not setting this (yet)
    concernDtls.concernID = uniqueIDObj.getNextID();
    // BEGIN, CR00099256, KH
    concernDtls.creationDate = TransactionInfo.getSystemDate();
    // END, CR00099256
    concernDtls.name = details.representativeName;

    // insert concern
    concernObj.insert(concernDtls);

    // need to generate uniqueID as pre insert is not setting this (yet)
    concernRoleDtls.concernRoleID = uniqueIDObj.getNextID();
    concernRoleDtls.concernID = concernDtls.concernID;
    concernRoleDtls.concernRoleName = details.representativeName;
    concernRoleDtls.concernRoleType = CONCERNROLETYPE.REPRESENTATIVE;
    concernRoleDtls.prefCommFromDate = currentDate;
    concernRoleDtls.prefCommMethod =
      representativeRegistrationDetails.preferredCommunication;
    concernRoleDtls.primaryAddressID = addressDtls.addressID;
    concernRoleDtls.primaryAlternateID = details.alternateID;
    concernRoleDtls.primaryPhoneNumberID = phoneNumberDtls.phoneNumberID;
    concernRoleDtls.registrationDate =
      representativeRegistrationDetails.registrationDate;
    concernRoleDtls.regUserName = TransactionInfo.getProgramUser();
    concernRoleDtls.sensitivity = SENSITIVITY.DEFAULTCODE;
    // BEGIN, CR00099256, KH
    concernRoleDtls.creationDate = TransactionInfo.getSystemDate();
    concernRoleDtls.startDate = startDate;
    concernRoleDtls.endDate = representativeRegistrationDetails.endDate;
    // END, CR00099256
    concernRoleDtls.statusCode = CONCERNROLESTATUS.DEFAULTCODE;

    // insert concern role
    // BEGIN, CR00061193, POH
    // Call the EvidenceController object and insert evidence
    // Evidence descriptor details
    evidenceDescriptorInsertDtls = new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID =
      concernRoleDtls.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType = CASEEVIDENCE.CONCERNROLE;
    evidenceDescriptorInsertDtls.receivedDate = currentDate;

    // Evidence Interface details
    eiEvidenceInsertDtls = new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID =
      concernRoleDtls.concernRoleID;
    eiEvidenceInsertDtls.evidenceObject = concernRoleDtls;
    eiEvidenceInsertDtls.descriptor.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    // Insert the evidence
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

    // END, CR00061193

    details.concernRoleID = concernRoleDtls.concernRoleID;

    // need to generate uniqueID as pre insert is not setting this (yet)
    concernRoleAddressDtls.concernRoleAddressID = uniqueIDObj.getNextID();
    concernRoleAddressDtls.addressID = addressDtls.addressID;
    concernRoleAddressDtls.concernRoleID = concernRoleDtls.concernRoleID;
    concernRoleAddressDtls.startDate = concernRoleDtls.startDate;
    concernRoleAddressDtls.statusCode = RECORDSTATUS.NORMAL;
    concernRoleAddressDtls.typeCode = CONCERNROLEADDRESSTYPE.MAILING;

    // insert concern role address
    // BEGIN, CR00061099, POH
    // Call the EvidenceController object and insert evidence
    // Evidence descriptor details
    evidenceDescriptorInsertDtls = new EvidenceDescriptorInsertDtls();
    evidenceDescriptorInsertDtls.participantID =
      concernRoleAddressDtls.concernRoleID;
    evidenceDescriptorInsertDtls.evidenceType =
      CASEEVIDENCE.CONCERNROLEADDRESS;
    evidenceDescriptorInsertDtls.receivedDate = currentDate;

    // Evidence Interface details
    eiEvidenceInsertDtls = new EIEvidenceInsertDtls();

    eiEvidenceInsertDtls.descriptor.assign(evidenceDescriptorInsertDtls);
    eiEvidenceInsertDtls.descriptor.participantID =
      concernRoleAddressDtls.concernRoleID;
    eiEvidenceInsertDtls.evidenceObject = concernRoleAddressDtls;
    eiEvidenceInsertDtls.descriptor.statusCode =
      EVIDENCEDESCRIPTORSTATUS.ACTIVE;

    // Insert the evidence
    evidenceControllerObj.insertEvidence(eiEvidenceInsertDtls);

    // END, CR00061099

    if (representativeRegistrationDetails.phoneNumber.length() > 0) {

      // need to generate uniqueID as pre insert is not setting this (yet)
      concernRolePhoneNumberDtls.concernRolePhoneNumberID =
        uniqueIDObj.getNextID();
      concernRolePhoneNumberDtls.concernRoleID =
        concernRoleDtls.concernRoleID;
      concernRolePhoneNumberDtls.phoneNumberID =
        phoneNumberDtls.phoneNumberID;
      concernRolePhoneNumberDtls.startDate = concernRoleDtls.startDate;
      concernRolePhoneNumberDtls.typeCode =
        representativeRegistrationDetails.phoneType;

      // insert concern role phone number
      concernRolePhoneNumberObj.insert(concernRolePhoneNumberDtls);
    }

    if (representativeRegistrationDetails.bankAccountName.length()
      + representativeRegistrationDetails.bankAccountNumber.length()
      + representativeRegistrationDetails.bankSortCode.length()
      + representativeRegistrationDetails.bankAccountType.length() > 0) {

      // Get concern role ID of newly created concern role
      maintainBankAccountKey.concernRoleID = concernRoleDtls.concernRoleID;
      // END, HARP, 48136

      bankAccountDetails.name =
        representativeRegistrationDetails.bankAccountName;
      bankAccountDetails.accountNumber =
        representativeRegistrationDetails.bankAccountNumber;
      bankAccountDetails.bankSortCode =
        representativeRegistrationDetails.bankSortCode;
      bankAccountDetails.typeCode =
        representativeRegistrationDetails.bankAccountType;
      bankAccountDetails.startDate = concernRoleDtls.startDate;
      bankAccountDetails.primaryBankInd = true;
      bankAccountDetails.bankAccountStatus = BANKACCOUNTSTATUS.DEFAULTCODE;
      bankAccountDetails.statusCode = RECORDSTATUS.DEFAULTCODE;
      bankAccountDetails.jointAccountInd =
        representativeRegistrationDetails.jointAccountInd;
      // BEGIN, CR00372248, GA
      bankAccountDetails.bicOpt = representativeRegistrationDetails.bicOpt;
      bankAccountDetails.ibanOpt = representativeRegistrationDetails.ibanOpt;
      // END, CR00372248

      // insert concern role bank account and bank account
      maintainConcernRoleBankAcObj.createBankAccount(maintainBankAccountKey,
        bankAccountDetails);
    }
    // BEGIN, CR00198529, ASN
    details.upperRepresentativeName =
      details.representativeName.toUpperCase();
    // END, CR00198529
  }

}
