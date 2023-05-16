package curam.ca.gc.bdm.rest.bdmcorrespondenceapi.impl;

import com.google.inject.Inject;
import curam.ca.gc.bdm.codetable.BDMBENEFITPROGRAMTYPE;
import curam.ca.gc.bdm.communication.impl.BDMCommunicationHelper;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMSSACountryAgreementFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.intf.BDMSSACountryAgreement;
import curam.ca.gc.bdm.entity.struct.BDMCountryAgrmntDetails;
import curam.ca.gc.bdm.entity.struct.BDMCountryCodeAndStatusKey;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.facade.communication.struct.BDMCaseCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMCorrespondenceDetails;
import curam.ca.gc.bdm.facade.communication.struct.BDMForeignApplicationDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.AddressType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.BenefitApplicationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.BenefitType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ClientType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CodeType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ContactInformationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CorrespondenceContentType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CorrespondenceType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.CountryType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.DateType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.IdentificationType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.LanguageType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ObjectFactory;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.PersonNameType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ProgramType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.ProvinceType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.RecipientType;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String;
import curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.TextType;
import curam.codetable.CASETYPECODE;
import curam.codetable.COUNTRY;
import curam.codetable.PRODUCTCATEGORY;
import curam.codetable.RECORDSTATUS;
import curam.core.fact.CaseHeaderFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.sl.entity.fact.ExternalPartyOfficeFactory;
import curam.core.sl.entity.intf.ExternalPartyOffice;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.struct.AddressKey;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseHeaderKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class BDMGenerateCorrespondenceMapper {

  @Inject
  BDMCommunicationHelper helper;

  public BDMGenerateCorrespondenceMapper() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  public java.lang.String correspondenceMapper(
    final BDMCorrespondenceDetails bdmCorrespondenceDetails)
    throws javax.xml.bind.JAXBException, AppException,
    InformationalException {

    // creating jaxb objects
    JAXBContext context;
    java.lang.String mappedData = new java.lang.String();
    try {
      context = JAXBContext.newInstance(CorrespondenceType.class);
      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

      // root element
      final CorrespondenceType correspondenceType =
        new ObjectFactory().createCorrespondenceType();

      // CorrespondenceRecipient TO
      final RecipientType recipientTo = new RecipientType();
      final AddressType recipientToAddressType =
        getAddress(bdmCorrespondenceDetails.toDetails.addressLineOne,
          bdmCorrespondenceDetails.toDetails.addressLineTwo,
          bdmCorrespondenceDetails.toDetails.addressLineThree,
          bdmCorrespondenceDetails.toDetails.addressLineFour,
          bdmCorrespondenceDetails.toDetails.unparsedAddressInd);

      recipientTo.setRecipientAddress(recipientToAddressType);
      final TextType recipientToCategoryCode = new TextType();
      recipientToCategoryCode
        .setValue(BDMConstants.kGenerateCorrespondenceXMLTo);
      recipientTo.setRecipientCategoryCode(recipientToCategoryCode);
      final TextType recipientToContactCode = new TextType();
      recipientToContactCode
        .setValue(bdmCorrespondenceDetails.toRecipientContactCode);
      recipientTo.setRecipientContactCode(recipientToContactCode);
      final TextType recipientToName = new TextType();
      // TASK-94808, Start
      // Make names upper case in the payload
      recipientToName
        .setValue(bdmCorrespondenceDetails.toCorrespondentName.toUpperCase());
      recipientTo.setRecipientName(recipientToName);

      final LanguageType recipientLanguage = new LanguageType();
      final TextType langName = new TextType();
      langName
        .setValue(bdmCorrespondenceDetails.toParticipantPreferredLanguage);
      recipientLanguage.getLanguageName().add(langName);
      recipientTo.setRecipientPreferredLanguage(recipientLanguage);

      correspondenceType.getCorrespondenceRecipient().add(recipientTo);

      // BUG-90640, Start
      // Add CC details only if cc is selected.
      // CorrespondenceRecipient CC
      if (bdmCorrespondenceDetails.ccContactConcernRoleID != 0
        || bdmCorrespondenceDetails.ccThirdPartyContactConcernRoleID != 0
        || bdmCorrespondenceDetails.ccParticipantRoleID != 0
        || bdmCorrespondenceDetails.ccClientIsCorrespondent) {

        final RecipientType recipientCC = new RecipientType();
        final AddressType recipientCCAddressType =
          getAddress(bdmCorrespondenceDetails.ccDetails.ccAddressLineOne,
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
        recipientCCName.setValue(
          bdmCorrespondenceDetails.ccCorrespondentName.toUpperCase());
        recipientCC.setRecipientName(recipientCCName);

        final LanguageType recipientCCLanguage = new LanguageType();
        final TextType langNameCC = new TextType();
        langNameCC
          .setValue(bdmCorrespondenceDetails.ccContactPreferredLanguage);
        recipientCCLanguage.getLanguageName().add(langNameCC);
        recipientCC.setRecipientPreferredLanguage(recipientCCLanguage);

        correspondenceType.getCorrespondenceRecipient().add(recipientCC);
      }

      // BUG-90640, End

      final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String categoryID =
        new String();
      categoryID.setValue("ISS-0552");
      correspondenceType.setDocumentCategoryID(categoryID);

      final CorrespondenceContentType documentContent =
        new CorrespondenceContentType();

      // SETTING CASE DETAILS

      // BenefitApplication
      final BenefitApplicationType benefitApplicationType =
        new BenefitApplicationType();

      // List of Foreign Benefits
      if (0 != bdmCorrespondenceDetails.caseID) {
        final BDMCaseCorrespondenceDetails bdmCaseCorrespondenceDetails =
          setCaseSpecificDetails(bdmCorrespondenceDetails);

        // List of foreign Benefits
        for (int i =
          0; i < bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
            .size(); i++) {

          // BenefitApplicationBenefit
          final BenefitType benefitApplicationBenefit = new BenefitType();
          // BenefitAbbreviationText
          final TextType benefitcode = new TextType();
          // BUG-96515, Start
          // For R1, Send only OAS
          benefitcode.setValue(BDMBENEFITPROGRAMTYPE.OASBENEFIT);
          // BUG-96515, End
          benefitApplicationBenefit.setBenefitAbbreviationText(benefitcode);
          // BenefitCountry
          final CountryType countryType1 = new CountryType();
          final CodeType countryName1 = new CodeType();
          countryName1.setValue(bdmCaseCorrespondenceDetails.countryName);
          countryType1.getCountryCode().add(countryName1);
          benefitApplicationBenefit.setBenefitCountry(countryType1);
          // BenefitName
          final TextType benefitName = new TextType();
          benefitName
            .setValue(bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).nameOfForeignBenefit);
          benefitApplicationBenefit.setBenefitName(benefitName);

          // BenefitApplicationCountry
          final CountryType countryType2 = new CountryType();
          final CodeType countryCode = new CodeType();
          countryCode.setValue(bdmCaseCorrespondenceDetails.countryName);
          countryType2.getCountryCode().add(countryCode);
          final TextType countryName = new TextType();
          countryName
            .setValue(bdmCaseCorrespondenceDetails.nameOfGovernmentCountry);
          countryType2.getCountryName().add(countryName);
          benefitApplicationType.setBenefitApplicationCountry(countryType2);
          // CountryOfAgrrementCode
          final TextType countryOfAgreementCode = new TextType();
          countryOfAgreementCode
            .setValue(bdmCaseCorrespondenceDetails.countryOfAgreement);
          benefitApplicationType
            .setCountryOfAgreementCode(countryOfAgreementCode);

          // Foreign Office Address
          final AddressType foAddress = getAddress(
            bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).addressLineOne,
            bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).addressLineTwo,
            bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).addressLineThree,
            bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).addressLineFour,
            bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
              .get(i).unparsedAddressInd);
          benefitApplicationBenefit.getContactMailingAddress().add(foAddress);
          benefitApplicationType.getBenefitApplicationBenefit()
            .add(benefitApplicationBenefit);
        }
        documentContent.setBenefitApplication(benefitApplicationType);

        // Program Benefit Name
        final ProgramType programType = new ProgramType();
        final TextType programName = new TextType();
        programName.setValue(bdmCaseCorrespondenceDetails.programBenefitName);
        programType.setProgramName(programName);
        documentContent.setProgram(programType);

      }

      // Client
      final ClientType client = new ClientType();

      // ClientIdentification 1
      if (bdmCorrespondenceDetails.toClientIsCorrespondent
        || CuramConst.gkZero != bdmCorrespondenceDetails.toContactThirdPartyConcernRoleID) {

        final IdentificationType clientIdentification1 =
          new IdentificationType();
        final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String identificationID1 =
          new String();
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
      final AddressType addressType =
        getAddress(bdmCorrespondenceDetails.toDetails.addressLineOne,
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
      try {
        datete.setValue(
          getXMLDate(bdmCorrespondenceDetails.clientDtls.dateOfBirth));
      } catch (final DatatypeConfigurationException e) {
        e.printStackTrace();
      }
      dob.getDate().add(datete);
      client.setPersonBirthDate(dob);

      // SIN
      if (CuramConst.gkZero != bdmCorrespondenceDetails.clientDtls.sin) {
        final IdentificationType sinIdentificationType =
          new IdentificationType();
        final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String sinNumber =
          new String();
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

      try {
        if (!bdmCorrespondenceDetails.clientDtls.dateOfDeath.isZero()) {
          dateOfDeath.setValue(
            getXMLDate(bdmCorrespondenceDetails.clientDtls.dateOfDeath));
        } else {
          dateOfDeath.setValue(getXMLDate(getNullDateForCCT()));
        }
      } catch (final DatatypeConfigurationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      dod.getDate().add(dateOfDeath);
      client.setPersonDeathDate(dod);

      documentContent.setClient(client);

      // set the document content
      correspondenceType.setDocumentContent(documentContent);

      final IdentificationType documentIdentification =
        new IdentificationType();
      final curam.ca.gc.bdm.rest.bdmcorrespondenceapi.jaxb.impl.String identificationID =
        new String();
      // BUG-91513, Start
      // Set the tracking number for document identification
      identificationID
        .setValue(Long.toString(bdmCorrespondenceDetails.trackingNumber));
      documentIdentification.getIdentificationID().add(identificationID);
      final TextType identificationCategoryText = new TextType();
      identificationCategoryText
        .setValue(BDMConstants.kGenerateCorrespondenceTrackingNumber);
      // BUG-91513, End
      documentIdentification.getIdentificationCategoryText()
        .add(identificationCategoryText);
      correspondenceType.setDocumentIdentification(documentIdentification);

      final StringWriter sw = new StringWriter();
      marshaller.marshal(correspondenceType, sw);
      mappedData = sw.toString();

    } catch (final JAXBException e) {
      e.printStackTrace();
    }

    return mappedData;

  }

  private AddressType getAddress(final java.lang.String addressLineOne,
    final java.lang.String addressLineTwo,
    final java.lang.String addressLineThree,
    final java.lang.String addressLineFour, final boolean isUnparsedAddress) {

    // TASK-94808, Start
    // Make address upper case
    final AddressType addressType = new AddressType();
    final TextType recipientToAddressLine1 = new TextType();
    recipientToAddressLine1.setValue(addressLineOne.toUpperCase());
    addressType.getAddressFullText().add(recipientToAddressLine1);
    final TextType recipientToAddressLine2 = new TextType();
    recipientToAddressLine2.setValue(addressLineTwo.toUpperCase());
    addressType.getAddressFullText().add(recipientToAddressLine2);
    final TextType recipientToAddressLine3 = new TextType();
    recipientToAddressLine3.setValue(addressLineThree.toUpperCase());
    addressType.getAddressFullText().add(recipientToAddressLine3);
    final TextType recipientToAddressLine4 = new TextType();
    recipientToAddressLine4.setValue(addressLineFour.toUpperCase());
    addressType.getAddressFullText().add(recipientToAddressLine4);

    // line 4 would be country code
    if (COUNTRY.CA.equalsIgnoreCase(addressLineFour) && !isUnparsedAddress) {

      final java.lang.String province = getProvince(addressLineThree);
      final ProvinceType recipientToProvinceType = new ProvinceType();
      final TextType recipientToProvinceCode = new TextType();
      recipientToProvinceCode.setValue(province.toUpperCase());
      recipientToProvinceType.setProvinceCode(recipientToProvinceCode);
      addressType.getAddressProvince().add(recipientToProvinceType);

      final java.lang.String postalCode = getPostalCode(addressLineThree);
      final String recipientToPostalCode = new String();
      recipientToPostalCode.setValue(postalCode.toUpperCase());
      addressType.getAddressPostalCode().add(recipientToPostalCode);
    }
    // TASK-94808, End
    return addressType;
  }

  private java.lang.String
    getProvince(final java.lang.String addressLineThree) {

    if (addressLineThree.isEmpty()) {
      return "";
    }
    final java.lang.String[] addressData = addressLineThree.split(" ");
    return addressData[addressData.length - 4];
  }

  private java.lang.String
    getPostalCode(final java.lang.String addressLineThree) {

    if (addressLineThree.isEmpty()) {
      return "";
    }
    final java.lang.String[] addressData = addressLineThree.split("  ");
    return addressData[addressData.length - 1];
  }

  private XMLGregorianCalendar getXMLDate(final curam.util.type.Date conDate)
    throws DatatypeConfigurationException {

    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.set(Calendar.YEAR,
      conDate.getCalendar().get(Calendar.YEAR));
    gregorianCalendar.set(Calendar.MONTH,
      conDate.getCalendar().get(Calendar.MONTH));
    gregorianCalendar.set(Calendar.DATE,
      conDate.getCalendar().get(Calendar.DATE));

    final DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
    final XMLGregorianCalendar dobDate =
      dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    dobDate.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
    dobDate.setHour(DatatypeConstants.FIELD_UNDEFINED);
    dobDate.setMinute(DatatypeConstants.FIELD_UNDEFINED);
    dobDate.setSecond(DatatypeConstants.FIELD_UNDEFINED);
    dobDate.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
    return dobDate;
  }

  public BDMCaseCorrespondenceDetails
    setCaseSpecificDetails(final BDMCorrespondenceDetails corresDetails)
      throws AppException, InformationalException {

    // return struct
    final BDMCaseCorrespondenceDetails bdmCaseCorrespondenceDetails =
      new BDMCaseCorrespondenceDetails();

    // Country Name
    final BDMFECase bdmFECase = BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmFECaseKey = new BDMFECaseKey();
    bdmFECaseKey.caseID = corresDetails.caseID;
    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();
    try {
      bdmfeCaseDtls = bdmFECase.read(bdmFECaseKey);
      bdmCaseCorrespondenceDetails.countryName = bdmfeCaseDtls.countryCode;
    } catch (final Exception e) {
      bdmCaseCorrespondenceDetails.countryName = BDMConstants.EMPTY_STRING;
    }

    // ApplicationType
    final CaseHeader ch = CaseHeaderFactory.newInstance();
    final CaseHeaderKey key = new CaseHeaderKey();
    key.caseID = corresDetails.caseID;
    final CaseHeaderDtls caseHeaderDtls = ch.read(key);
    bdmCaseCorrespondenceDetails.applicationType =
      caseHeaderDtls.integratedCaseType;

    // Program Benefit Name
    java.lang.String programBenefitName = BDMConstants.EMPTY_STRING;
    if (CASETYPECODE.INTEGRATEDCASE
      .equalsIgnoreCase(caseHeaderDtls.caseTypeCode)) {
      programBenefitName = CodeTable.getOneItem(PRODUCTCATEGORY.TABLENAME,
        caseHeaderDtls.integratedCaseType);
    }
    bdmCaseCorrespondenceDetails.programBenefitName = programBenefitName;

    // List of Foreign Benefits
    final BDMForeignApplication bdmForeignApplication =
      BDMForeignApplicationFactory.newInstance();
    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
    bdmfaKeyStruct3.caseID = corresDetails.caseID;
    final BDMForeignApplicationDtlsList bdmForeignApplicationDtlsList =
      bdmForeignApplication.readByCaseID(bdmfaKeyStruct3);

    // List Foreign Agency Office Addresses
    for (int i = 0; i < bdmForeignApplicationDtlsList.dtls.size(); i++) {

      // set application name for each entry
      final BDMForeignApplicationDetails bdmForeignBenefitApplicationDetails =
        new BDMForeignApplicationDetails();
      bdmForeignBenefitApplicationDetails.nameOfForeignBenefit =
        bdmForeignApplicationDtlsList.dtls.get(i).typeCode;

      BDMForeignApplicationDtls bdmfaDtls = new BDMForeignApplicationDtls();
      final BDMForeignApplicationKey bdmForeignApplicationKey =
        new BDMForeignApplicationKey();
      bdmForeignApplicationKey.fApplicationID =
        bdmForeignApplicationDtlsList.dtls.get(i).fApplicationID;

      bdmfaDtls = BDMForeignApplicationFactory.newInstance()
        .read(bdmForeignApplicationKey);

      final AddressKey addressKey = new AddressKey();
      final ExternalPartyOffice externalPartyOffice =
        ExternalPartyOfficeFactory.newInstance();
      final ExternalPartyOfficeKey externalPartyOfficeKey =
        new ExternalPartyOfficeKey();
      ExternalPartyOfficeDtls externalPartyOfficeDtls =
        new ExternalPartyOfficeDtls();

      if (bdmfaDtls.fOfficeID != CuramConst.gkZero) {
        externalPartyOfficeKey.externalPartyOfficeID = bdmfaDtls.fOfficeID;

        externalPartyOfficeDtls =
          externalPartyOffice.read(externalPartyOfficeKey);

        addressKey.addressID = externalPartyOfficeDtls.primaryAddressID;

        if (addressKey.addressID != CuramConst.kLongZero) {
          // set foreign office address for each application
          final java.lang.String[] fourLinesOfAddress =
            helper.getAddressMapper(addressKey.addressID);
          bdmForeignBenefitApplicationDetails.addressLineOne =
            fourLinesOfAddress[0];
          bdmForeignBenefitApplicationDetails.addressLineTwo =
            fourLinesOfAddress[1];
          bdmForeignBenefitApplicationDetails.addressLineThree =
            fourLinesOfAddress[2];
          bdmForeignBenefitApplicationDetails.addressLineFour =
            fourLinesOfAddress[3];
          if (!StringUtil.isNullOrEmpty(fourLinesOfAddress[4])
            && BDMConstants.kBDMUNPARSE
              .equalsIgnoreCase(fourLinesOfAddress[4])) {
            bdmForeignBenefitApplicationDetails.unparsedAddressInd = true;
          }
        }
      }

      bdmCaseCorrespondenceDetails.foreignApplicationDtls.dtls
        .add(bdmForeignBenefitApplicationDetails);
    }

    // Country of Agreement
    final BDMSSACountryAgreement bdmssaCountryAgreement =
      BDMSSACountryAgreementFactory.newInstance();
    final BDMCountryCodeAndStatusKey bdmCountryCodeAndStatusKey =
      new BDMCountryCodeAndStatusKey();
    bdmCountryCodeAndStatusKey.countryCode = bdmfeCaseDtls.countryCode;
    bdmCountryCodeAndStatusKey.recordStatus = RECORDSTATUS.DEFAULTCODE;
    // BUG-96131, Start
    // Check for record by adding RNFE
    BDMCountryAgrmntDetails bdmCountryAgrmntDetails = null;
    try {
      bdmCountryAgrmntDetails = bdmssaCountryAgreement
        .readAgrmntByCountryCodeAndStatus(bdmCountryCodeAndStatusKey);
    } catch (final RecordNotFoundException rnfe) {
      // Do nothing since we are populating based on null check
    }
    if (null != bdmCountryAgrmntDetails) {
      // Name of Government Country
      bdmCaseCorrespondenceDetails.nameOfGovernmentCountry =
        bdmCountryAgrmntDetails.countryName;
      // Country of Agreement
      bdmCaseCorrespondenceDetails.countryOfAgreement =
        bdmCountryAgrmntDetails.agreementNumber;
    }
    // BUG-96131, End

    // CC Indicator
    if (corresDetails.ccClientIsCorrespondent
      || 0 != corresDetails.ccContactConcernRoleID
      || 0 != corresDetails.ccParticipantRoleID) {
      bdmCaseCorrespondenceDetails.isCCInd = true;
    } else {
      bdmCaseCorrespondenceDetails.isCCInd = false;
    }

    return bdmCaseCorrespondenceDetails;

  }

  /**
   * For CCT, a null date would be 01-01-9998.
   *
   * This has to be done so that CCT can handle nulls.
   *
   * @return Date
   */
  private Date getNullDateForCCT() {

    Date nullDate = new Date();
    nullDate = Date.fromISO8601("99980101");
    return nullDate;
  }

}
