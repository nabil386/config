package curam.ca.gc.bdm.facade.pdcperson.impl;

import com.google.inject.Inject;
import com.google.inject.Provider;
import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMAGREEATTESTATION;
import curam.ca.gc.bdm.codetable.BDMSOURCECOUNTRY;
import curam.ca.gc.bdm.creole.impl.Statics;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.person.struct.BDMPersonDtls;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMConcernRoleIDAndWizardStateIDKey;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPDCEvidenceDetailsList;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonDemographicPageDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonIdentificationDetails;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMPersonKey;
import curam.ca.gc.bdm.facade.pdcperson.struct.BDMReadPersonDemographicDetails;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMBPOADDRESS;
import curam.ca.gc.bdm.message.BDMEVIDENCE;
import curam.ca.gc.bdm.message.BDMIDENTIFICATIONINFO;
import curam.ca.gc.bdm.sl.fact.BDMPDCPersonFactory;
import curam.cefwidgets.sl.impl.CuramConst;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.DUPLICATESTATUS;
import curam.codetable.EVIDENCECHANGEREASON;
import curam.codetable.LOCATIONACCESSTYPE;
import curam.codetable.PRODUCTCATEGORY;
import curam.core.facade.fact.CaseHeaderFactory;
import curam.core.facade.pdt.struct.InformationalMessage;
import curam.core.facade.pdt.struct.InformationalMessageList;
import curam.core.facade.struct.CaseHeaderDetails;
import curam.core.facade.struct.CaseReference;
import curam.core.sl.struct.ParticipantSecurityCheckKey;
import curam.core.struct.ConcernRoleIDStatusCodeKey;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.PersonKey;
import curam.pdc.facade.fact.PDCPersonFactory;
import curam.pdc.facade.intf.PDCPerson;
import curam.pdc.facade.struct.PDCCaseDetails;
import curam.pdc.facade.struct.PDCCaseDetailsList;
import curam.pdc.facade.struct.PDCEvidenceDetailsList;
import curam.pdc.facade.struct.PersonDemographicPageDetails;
import curam.pdc.facade.struct.PersonHomePageDetails;
import curam.pdc.facade.struct.ReadPersonDemographicDetails;
import curam.pdc.facade.struct.ReadPersonKey;
import curam.pdc.impl.PDCParticipantHelper;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.exception.RecordNotFoundException;
import curam.util.persistence.GuiceWrapper;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.Date;
import curam.wizard.util.impl.CodetableUtil;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import static java.net.URLDecoder.decode;

/**
 *
 * @author rajat.soni
 *
 */
public class BDMPDCPerson
  extends curam.ca.gc.bdm.facade.pdcperson.base.BDMPDCPerson {

  private final BDMUtil bdmUtil = new BDMUtil();

  private static final int SIN_NUMBER_CHAR_DISPLAYED = 4;

  @Inject
  protected Provider<PDCParticipantHelper> pdcParticipantHelper;

  @Inject(optional = true)

  public BDMPDCPerson() {

    GuiceWrapper.getInjector().injectMembers(this);
  }

  /**
   * Read person demographics details
   */
  @Override
  public BDMReadPersonDemographicDetails readBDMNonPDCDemographicData(
    final PersonKey key) throws AppException, InformationalException {

    final BDMReadPersonDemographicDetails bdmReadPersonDemographicDetails =
      new BDMReadPersonDemographicDetails();

    ReadPersonDemographicDetails readPersonDemographicDetails =
      new ReadPersonDemographicDetails();
    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    readPersonDemographicDetails =
      pdcPersonObj.readNonPDCDemographicData(key);

    bdmReadPersonDemographicDetails.assign(readPersonDemographicDetails);

    final BDMPersonDtls bdmPersonDtls =
      bdmUtil.readPersonDemographicsDetails(key);

    bdmReadPersonDemographicDetails.educationLevel =
      bdmPersonDtls.educationLevel;
    bdmReadPersonDemographicDetails.indigenousPersonType =
      bdmPersonDtls.indigenousPersonType;
    bdmReadPersonDemographicDetails.memberMinorityGrpType =
      bdmPersonDtls.memberMinorityGrpType;

    return bdmReadPersonDemographicDetails;
  }

  /**
   * This method reads the person details for modify. Checks if the payment
   * frequency is null or empty if true then sets it to a default value of
   * weekly.
   *
   * @author vishal.madichetty
   * @since TASK - 19193
   */
  @Override
  public PersonHomePageDetails readHomePageDetailsForModify(
    final ReadPersonKey key) throws AppException, InformationalException {

    final ParticipantSecurityCheckKey participantSecurityCheckKey =
      new ParticipantSecurityCheckKey();

    participantSecurityCheckKey.participantID = key.dtls.concernRoleID;
    participantSecurityCheckKey.type = LOCATIONACCESSTYPE.READ;

    this.pdcParticipantHelper.get()
      .checkParticipantSecurity(participantSecurityCheckKey);

    final curam.pdc.intf.PDCPerson pdcPersonObj =
      curam.pdc.fact.PDCPersonFactory.newInstance();
    final PersonHomePageDetails personHomePageDetails =
      new PersonHomePageDetails();

    final curam.pdc.struct.ReadPersonKey personKey =
      new curam.pdc.struct.ReadPersonKey();

    personKey.assign(key);
    personHomePageDetails.dtls =
      pdcPersonObj.readPersonHomePageDetails(personKey);
    if (StringUtil
      .isNullOrEmpty(personHomePageDetails.dtls.paymentFrequency)) {
      personHomePageDetails.dtls.paymentFrequency =
        BDMConstants.kPaymentFrequencyWeekly_Fri;
    }
    return personHomePageDetails;
  }

  /**
   * Modify person demographics details
   */
  @Override
  public void modifyBDMNonPDCDemographicData(
    final BDMPersonDemographicPageDetails details)
    throws AppException, InformationalException {

    final PersonDemographicPageDetails personDemographicPageDetails =
      new PersonDemographicPageDetails();

    personDemographicPageDetails.assign(details);

    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    pdcPersonObj.modifyNonPDCDemographicData(personDemographicPageDetails);

    bdmUtil.modifyPersonDemographicsDetails(details);

  }

  /**
   * This method updates and/or creates the SIN Identification evidence, Date of
   * Birth and the Names.
   * TASK 26338
   *
   */

  @Override
  public void changeIdentificationInfoDetails(
    final BDMPersonIdentificationDetails details)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    // Validation for Identification Change

    if (!details.dtls.sinNumber.isEmpty()) {

      // Bug 89706 : Unable to add 900 series SIN to persons - Commented
      // validation for sin starting with number 9
      if (Statics.validateSINMod10(null, details.dtls.sinNumber)) { // ||
                                                                    // Statics.isSINNumberStartsWith9Series(null,
                                                                    // details.dtls.sinNumber)

        final LocalisableString localisableString =
          new LocalisableString(BDMIDENTIFICATIONINFO.SIN_NUMBER_NOT_VALID);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }

      if (details.dtls.sinIdentificationStartDate.isZero()) {

        final LocalisableString localisableString = new LocalisableString(
          BDMIDENTIFICATIONINFO.START_DATE_OF_NEW_SIN_MUST_BE_ENTERED);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }

      if (!details.dtls.sinIdentificationStartDate.isZero()
        && details.dtls.sinIdentificationStartDate
          .after(Date.getCurrentDate())) {

        final LocalisableString localisableString = new LocalisableString(
          BDMIDENTIFICATIONINFO.START_DATE_MUST_BE_TODAY_OR_THE_PAST);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }

    }

    if (!details.dtls.sinNumber.isEmpty()
      && !details.dtls.existingSINNumber.isEmpty()) {

      if (!details.dtls.sinIdentificationExistingEndDate.isZero()
        && details.dtls.sinIdentificationExistingEndDate
          .after(Date.getCurrentDate())) {

        final LocalisableString localisableString = new LocalisableString(
          BDMIDENTIFICATIONINFO.END_DATE_MUST_BE_TODAY_OR_DATE_IN_THE_PAST);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }

      if (!details.dtls.sinIdentificationExistingEndDate.isZero()
        && !details.dtls.sinIdentificationStartDate.isZero()
        && !details.dtls.sinIdentificationStartDate
          .after(details.dtls.sinIdentificationExistingEndDate)) {

        final LocalisableString localisableString = new LocalisableString(
          BDMIDENTIFICATIONINFO.START_DATE_OF_THE_NEW_SIN_MUST_BE_AFTER_END_DATE_OF_AN_EXISTING_SIN);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);

      }

      if (!details.dtls.sinIdentificationEndDate.isZero()
        && !details.dtls.sinIdentificationStartDate.isZero()
        && !details.dtls.sinIdentificationEndDate
          .after(details.dtls.sinIdentificationStartDate)) {

        final LocalisableString localisableString = new LocalisableString(
          BDMIDENTIFICATIONINFO.END_DATE_OF_THE_NEW_SIN_MUST_BE_AFTER_START_DATE_OF_THE_NEW_SIN);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
      }

      informationalManager.failOperation();

    }
    if (EVIDENCECHANGEREASON.REPORTEDBYEXTERNALPARTY
      .equals(details.dtls.evidenceChangeReason)
      || EVIDENCECHANGEREASON.REPORTED_BYCLIENT_LIFE_EVENT
        .equals(details.dtls.evidenceChangeReason)
      || EVIDENCECHANGEREASON.REPORTEDBYCLIENT
        .equals(details.dtls.evidenceChangeReason)) {

      // Throw error if error if attestation agree is not selected
      if (!BDMAGREEATTESTATION.YES.equals(details.dtls.attestationAgree)) {
        final LocalisableString localisableString = new LocalisableString(
          BDMEVIDENCE.ERR_ATTESTATION_CHECKBOX_MANDATORY);

        informationalManager.addInformationalMsg(localisableString, "",
          InformationalElement.InformationalType.kError);
        informationalManager.failOperation();
      }
    }

    // setting the FAcadeScope

    TransactionInfo.setFacadeScopeObject("guidedChange", true);

    // Calling the Service Layer method
    BDMPDCPersonFactory.newInstance()
      .changeIdentificationInfoDetails(details.dtls);
  }

  /**
   * This method reads the Person Identification related data.
   * TASK 26338
   */

  @Override
  public BDMPersonIdentificationDetails readPersonIdentificationDetails(
    final BDMPersonKey key) throws AppException, InformationalException {

    final BDMPersonIdentificationDetails identificationDetails =
      new BDMPersonIdentificationDetails();

    identificationDetails.dtls = BDMPDCPersonFactory.newInstance()
      .readPersonIdentificationDetails(key.dtls);

    return identificationDetails;
  }

  /**
   * This method reads the PDCPerson Evidence list for the screen:
   * PDCEvidence_listEvidence.uim
   * Task 59306: DEV; Implement Mask SIN- Evidence List page
   *
   */
  @Override
  public BDMPDCEvidenceDetailsList
    listEvidenceForParticipant(final BDMConcernRoleIDAndWizardStateIDKey key)
      throws AppException, InformationalException {

    final BDMPDCEvidenceDetailsList bdmpdcEvidenceDetailsList =
      new BDMPDCEvidenceDetailsList();

    PDCEvidenceDetailsList pdcEvidenceList = new PDCEvidenceDetailsList();

    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    final PersonKey personKey = new PersonKey();
    personKey.concernRoleID = key.concernRoleID;

    pdcEvidenceList = pdcPersonObj.listEvidenceForParticipant(personKey);

    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {
      String decodedAddressData;
      try {
        decodedAddressData = decode(pdcEvidenceList.list.get(i).description,
          StandardCharsets.UTF_8.toString());
        pdcEvidenceList.list.get(i).description = decodedAddressData;
      } catch (final UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }

    }

    final ConcernRoleKey concernRoleKey = new ConcernRoleKey();
    concernRoleKey.concernRoleID = key.concernRoleID;

    // Set an indicator if this concern has duplicates
    final ConcernRoleIDStatusCodeKey concernRoleIDStatusCodeKey =
      new ConcernRoleIDStatusCodeKey();

    // Set the concern role id and status
    concernRoleIDStatusCodeKey.concernRoleID = key.concernRoleID;
    concernRoleIDStatusCodeKey.statusCode = DUPLICATESTATUS.UNMARKED;

    bdmpdcEvidenceDetailsList.pdcEvdnDetails = pdcEvidenceList;

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    final InformationalMessageList informationalMessageList =
      new InformationalMessageList();

    boolean residentialAddressEndedFoundInd = false;

    final BDMUtil bdmUtil = new BDMUtil();

    // BEGIN - Task 60485: DEV: Implement- Informational message
    for (int i = 0; i < pdcEvidenceList.list.size(); i++) {

      if (pdcEvidenceList.list.get(i).evidenceType.equals(
        CASEEVIDENCE.BDMADDRESS) && !residentialAddressEndedFoundInd) {

        if (BDMUtil.foundOnlyResidentialAddressEnded(
          pdcEvidenceList.list.get(i).evidenceDescriptorID)) {
          final LocalisableString localString = new LocalisableString(
            BDMBPOADDRESS.ERR_CURRRENT_RESIDENTIAL_END_DATED_ADD_NEW_RESIDENTIAL_ADDRESS);

          informationalManager.addInformationalMsg(localString, "",
            InformationalElement.InformationalType.kWarning);
          residentialAddressEndedFoundInd = true;
        }

        if (!bdmUtil.isResidentialAndMailingAddressMatching(
          pdcEvidenceList.list.get(i).evidenceDescriptorID)) {

          final LocalisableString localString =
            new LocalisableString(BDMEVIDENCE.ERR_ADDRESS_MISMATCH);

          informationalManager.addInformationalMsg(localString, "",
            InformationalElement.InformationalType.kWarning);
          residentialAddressEndedFoundInd = true;

        }

      }
    }

    final String[] warnings =
      informationalManager.obtainInformationalAsString();

    for (int i = 0; i < warnings.length; i++) {
      final InformationalMessage informationalMessage =
        new InformationalMessage();
      // Add Warning Message to informational Message
      informationalMessage.message = warnings[i];
      informationalMessageList.dtls.addRef(informationalMessage);
      bdmpdcEvidenceDetailsList.msgDtls = informationalMessageList;

    }
    // END - Task 60485: DEV: Implement- Informational message

    return bdmpdcEvidenceDetailsList;
  }

  @Override
  public PDCCaseDetailsList listCurrentCases(final ConcernRoleKey key)
    throws AppException, InformationalException {

    PDCCaseDetailsList pdcCaseDetailsList = new PDCCaseDetailsList();

    final PDCPerson pdcPersonObj = PDCPersonFactory.newInstance();

    pdcCaseDetailsList = pdcPersonObj.listCurrentCases(key);

    int listSize = 0;

    listSize = pdcCaseDetailsList.dtlsList.size();

    PDCCaseDetails pdcCaseDetails = new PDCCaseDetails();

    final String fecDescription = CodetableUtil.getCodetableDescription(
      PRODUCTCATEGORY.TABLENAME, PRODUCTCATEGORY.BDM_IC_FEC_PROGRAM);

    CaseReference caseReference = new CaseReference();
    CaseHeaderDetails caseHeaderDetails = new CaseHeaderDetails();
    BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    BDMFECaseDtls bdmfeCaseDtls = new BDMFECaseDtls();

    String countryCodeDesc = CuramConst.gkEmpty;

    for (int i = 0; i < listSize; i++) {

      pdcCaseDetails = new PDCCaseDetails();
      pdcCaseDetails = pdcCaseDetailsList.dtlsList.get(i);

      if (pdcCaseDetails.typeDescription.equals(fecDescription)) {

        caseHeaderDetails = new CaseHeaderDetails();
        caseReference = new CaseReference();
        caseReference.dtls.caseReference = pdcCaseDetails.reference;
        caseHeaderDetails =
          CaseHeaderFactory.newInstance().readByCaseReference(caseReference);

        bdmfeCaseKey = new BDMFECaseKey();
        bdmfeCaseKey.caseID = caseHeaderDetails.dtls.caseID;
        bdmfeCaseDtls = new BDMFECaseDtls();

        try {

          bdmfeCaseDtls = BDMFECaseFactory.newInstance().read(bdmfeCaseKey);

          if (!bdmfeCaseDtls.countryCode.equals(CuramConst.gkEmpty)) {

            pdcCaseDetails.typeDescription = CuramConst.gkEmpty;

            countryCodeDesc = CodetableUtil.getCodetableDescription(
              BDMSOURCECOUNTRY.TABLENAME, bdmfeCaseDtls.countryCode);

            pdcCaseDetails.typeDescription =
              fecDescription + " - " + countryCodeDesc;

          }

        } catch (final RecordNotFoundException rnfe) {
          // do nothing
        }

      }

    }

    return pdcCaseDetailsList;
  }

}
