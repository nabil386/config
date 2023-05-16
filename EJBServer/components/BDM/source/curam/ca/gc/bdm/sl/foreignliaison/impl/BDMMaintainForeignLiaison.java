package curam.ca.gc.bdm.sl.foreignliaison.impl;

import curam.ca.gc.bdm.application.impl.BDMUtil;
import curam.ca.gc.bdm.codetable.BDMATTACHMENTLINKTYPE;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPSTATUS;
import curam.ca.gc.bdm.codetable.BDMFOREIGNAPPTYPE;
import curam.ca.gc.bdm.codetable.BDMLIAISONCHECKLIST;
import curam.ca.gc.bdm.codetable.BDMLIAISONDELREASON;
import curam.ca.gc.bdm.codetable.BDMLIAISONDIRECTION;
import curam.ca.gc.bdm.codetable.BDMYESNO;
import curam.ca.gc.bdm.entity.fact.BDMFEAttachmentLinkFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignApplicationFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonFactory;
import curam.ca.gc.bdm.entity.fact.BDMForeignLiaisonHistFactory;
import curam.ca.gc.bdm.entity.fact.BDMForgnLsnAttLnkHstFactory;
import curam.ca.gc.bdm.entity.fec.fact.BDMFECaseFactory;
import curam.ca.gc.bdm.entity.fec.intf.BDMFECase;
import curam.ca.gc.bdm.entity.fec.struct.BDMFAAttachmentNoticationTaskDetails;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseDtls;
import curam.ca.gc.bdm.entity.fec.struct.BDMFECaseKey;
import curam.ca.gc.bdm.entity.intf.BDMFEAttachmentLink;
import curam.ca.gc.bdm.entity.intf.BDMForeignApplication;
import curam.ca.gc.bdm.entity.intf.BDMForeignLiaison;
import curam.ca.gc.bdm.entity.intf.BDMForeignLiaisonHist;
import curam.ca.gc.bdm.entity.struct.BDMFAKeyStruct3;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtls;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKey;
import curam.ca.gc.bdm.entity.struct.BDMFEAttachmentLinkKeyStruct1;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistDtls;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistDtlsList;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonHistKey;
import curam.ca.gc.bdm.entity.struct.BDMForeignLiaisonKey;
import curam.ca.gc.bdm.entity.struct.BDMForgnLsnAttLnkHstDtls;
import curam.ca.gc.bdm.entity.struct.BDMReadByFrgnLiasnIDKey;
import curam.ca.gc.bdm.entity.struct.BDMReadFLAtchmntDetails;
import curam.ca.gc.bdm.entity.struct.BDMReadFLAtchmntDetailsList;
import curam.ca.gc.bdm.entity.struct.BDMReadFLByCaseIDKey;
import curam.ca.gc.bdm.entity.struct.BDMSearchByForeignLiaisonHistoryIDKey;
import curam.ca.gc.bdm.entity.struct.BDMSearchFLAttachmentsKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFAConcernRoleKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMFACountryCodeKey;
import curam.ca.gc.bdm.facade.fec.struct.BDMForeignOffice;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfForeignIDs;
import curam.ca.gc.bdm.facade.fec.struct.BDMListOfOffices;
import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.message.BDMFEC;
import curam.ca.gc.bdm.sl.attachment.fact.BDMMaintainAttachmentFactory;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetails;
import curam.ca.gc.bdm.sl.attachment.struct.BDMCaseAttachmentAndLinkDetailsList;
import curam.ca.gc.bdm.sl.fec.fact.BDMMaintainForeignEngagementCaseFactory;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFECaseIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFLAttachmentDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFLAttachmentDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMForeignLiaisonDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnAppDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnAppDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnAttLnkIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnDeleteKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnHistIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnIDKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnRefDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnRefDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnViewDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMFrgnLiasnWizardDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMLiasnChecklistCodeDetails;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMLiasnChecklistCodeDetailsList;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDetailsKey;
import curam.ca.gc.bdm.sl.foreignliaison.struct.BDMReadDispDetails;
import curam.codetable.BUSINESSOBJECTTYPE;
import curam.codetable.DOCUMENTTYPE;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.fact.ConcernRoleAlternateIDFactory;
import curam.core.fact.CaseHeaderFactory;
import curam.core.fact.UniqueIDFactory;
import curam.core.fact.UsersFactory;
import curam.core.impl.CuramConst;
import curam.core.intf.CaseHeader;
import curam.core.intf.UniqueID;
import curam.core.sl.entity.fact.ExternalPartyOfficeFactory;
import curam.core.sl.entity.struct.ExternalPartyOfficeDtls;
import curam.core.sl.entity.struct.ExternalPartyOfficeKey;
import curam.core.sl.infrastructure.impl.ValidationManagerFactory;
import curam.core.sl.struct.WizardStateID;
import curam.core.struct.AddressKey;
import curam.core.struct.AttachmentCaseID;
import curam.core.struct.CaseHeaderKey;
import curam.core.struct.ConcernRoleAlternateIDDtls;
import curam.core.struct.ConcernRoleAlternateIDKey;
import curam.core.struct.ReadParticipantRoleIDDetails;
import curam.core.struct.UniqueIDKeySet;
import curam.core.struct.UsersDtls;
import curam.core.struct.UsersKey;
import curam.events.TASK;
import curam.util.administration.fact.CodeTableAdminFactory;
import curam.util.administration.intf.CodeTableAdmin;
import curam.util.administration.struct.CodeTableItemDetails;
import curam.util.administration.struct.CodeTableItemDetailsList;
import curam.util.events.impl.EventService;
import curam.util.events.struct.Event;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.exception.LocalisableString;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.Date;
import curam.util.type.DateTime;
import curam.util.type.NotFoundIndicator;
import curam.util.type.StringList;
import curam.util.workflow.fact.BizObjAssociationFactory;
import curam.util.workflow.intf.BizObjAssociation;
import curam.util.workflow.struct.BizObjAssocSearchDetails;
import curam.util.workflow.struct.BizObjAssocSearchDetailsList;
import curam.util.workflow.struct.BizObjAssociationDtls;
import curam.util.workflow.struct.BizObjAssociationDtlsList;
import curam.util.workflow.struct.BizObjectTypeKey;
import curam.util.workflow.struct.TaskKey;
import curam.wizard.util.impl.CodetableUtil;
import curam.wizardpersistence.impl.WizardPersistentState;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

public class BDMMaintainForeignLiaison
  extends curam.ca.gc.bdm.sl.foreignliaison.base.BDMMaintainForeignLiaison {

  static final Set<String> FL_RELATED_DOC_TYPES = new HashSet<>();
  static {
    FL_RELATED_DOC_TYPES
      .addAll(Arrays.asList(DOCUMENTTYPE.FOREIGN_APPLICATION,
        DOCUMENTTYPE.FOREIGN_APPLICATION_ADDITIONAL_DOCUMENTS,
        DOCUMENTTYPE.FOREIGN_LIAISON,
        DOCUMENTTYPE.FOREIGN_LIAISON_ADDITIONAL_DOCUMENTS, DOCUMENTTYPE.BESS,
        DOCUMENTTYPE.BESS_ADDITIONAL_DOCUMENTS));
  }

  BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

  private final BDMUtil bdmUtil = new BDMUtil();

  /**
   * Method to list the foreign liaisons.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFrgnLiasnViewDetailsList viewDetailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetailsList listForeignLiaisons(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMFrgnLiasnViewDetailsList flViewDetailsList =
      new BDMFrgnLiasnViewDetailsList();
    final curam.ca.gc.bdm.entity.intf.BDMForeignLiaison flObj =
      BDMForeignLiaisonFactory.newInstance();

    final BDMReadFLByCaseIDKey readFLByCaseIDKey = new BDMReadFLByCaseIDKey();
    readFLByCaseIDKey.caseID = key.caseID;

    final BDMForeignLiaisonDtlsList flDtlsList =
      flObj.readForeignLiaisonByCaseID(readFLByCaseIDKey);

    BDMForeignLiaisonDtls bdmForeignLiaisonDtls = null;
    BDMFrgnLiasnViewDetails bdmFrgnLiasnViewDetails = null;
    final int flDtlsListSize = flDtlsList.dtls.size();

    for (int i = 0; i < flDtlsListSize; i++) {
      bdmForeignLiaisonDtls = new BDMForeignLiaisonDtls();
      bdmFrgnLiasnViewDetails = new BDMFrgnLiasnViewDetails();
      bdmForeignLiaisonDtls = flDtlsList.dtls.item(i);
      bdmFrgnLiasnViewDetails.assign(bdmForeignLiaisonDtls);

      bdmFrgnLiasnViewDetails.liaisonRefDesc =
        contructLiaisonDirectionAndRefDesc(bdmForeignLiaisonDtls.direction,
          bdmForeignLiaisonDtls.liaisonReference);

      if (!StringUtil
        .isNullOrEmpty(bdmForeignLiaisonDtls.foreignAppIDs.trim())) {

        bdmFrgnLiasnViewDetails.frgnAppRefDesc =
          contructFApplicationAndRefDesc(
            bdmForeignLiaisonDtls.foreignAppIDs.trim());
      }

      flViewDetailsList.dtls.addRef(bdmFrgnLiasnViewDetails);
    }
    flViewDetailsList.isIOClientContact = BDMUtil.isIOClientContact();
    return flViewDetailsList;
  }

  /**
   * This is utility method to construct liaison direction and liaison reference
   * in to a string separated by a hyphen
   *
   * @param directionCd
   * @param liaisonReference
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String contructLiaisonDirectionAndRefDesc(final String directionCd,
    final String liaisonReference)
    throws AppException, InformationalException {

    final String strDirtnAndFLRef =
      CodetableUtil.getCodetableDescription(BDMLIAISONDIRECTION.TABLENAME,
        directionCd) + CuramConst.gkDash + liaisonReference;

    return strDirtnAndFLRef;
  }

  /**
   * This is an utility method, to construct a string as "<Foreign Application
   * Type>-<Foreign Application Reference Number>" separated by a comma if
   * multiple foreign applications are linked to a foreign liaison.
   *
   * @param directionCd
   * @param liaisonReference
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String contructFApplicationAndRefDesc(final String fApplicationIDs)
    throws AppException, InformationalException {

    String strFApplicationsAndFARef = "";

    final String strFAIdsArr[] =
      fApplicationIDs.trim().split(CuramConst.gkTabDelimiter);

    long fApplicationID = CuramConst.kLongZero;

    BDMForeignApplicationKey bdmfaKey = null;

    BDMForeignApplicationDtls bdmfaDtls = null;

    final StringBuffer strBuffFATypCdAndFARef = new StringBuffer();

    for (int i = 0; i < strFAIdsArr.length; i++) {

      fApplicationID = Long.parseLong(strFAIdsArr[i].toString());

      bdmfaKey = new BDMForeignApplicationKey();

      bdmfaKey.fApplicationID = fApplicationID;

      bdmfaDtls = BDMForeignApplicationFactory.newInstance().read(bdmfaKey);

      strBuffFATypCdAndFARef.append(CodetableUtil.getCodetableDescription(
        BDMFOREIGNAPPTYPE.TABLENAME, bdmfaDtls.typeCode));

      strBuffFATypCdAndFARef.append(CuramConst.gkDash);

      strBuffFATypCdAndFARef.append(bdmfaDtls.faReferenceNumber);

      strBuffFATypCdAndFARef.append(CuramConst.gkComma);

    }

    final int lastIndex = strBuffFATypCdAndFARef.toString().lastIndexOf(',');

    if (lastIndex != -1) {
      strFApplicationsAndFARef = strBuffFATypCdAndFARef.toString()
        .substring(CuramConst.gkZero, lastIndex);
    }

    return strFApplicationsAndFARef;

  }

  /**
   * Method to view the foreign liaison details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFrgnLiasnViewDetails viewDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetails viewForeignLiaisonDetails(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    BDMFrgnLiasnViewDetails flViewDetails = new BDMFrgnLiasnViewDetails();

    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = key.foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls = flObj.read(flKey);

    flViewDetails.assign(flDtls);

    // Bugfix #81327: missing Office Name and BESS
    if (flDtls.fOfficeID != 0) {
      final BDMReadDetailsKey dKey = new BDMReadDetailsKey();
      dKey.caseID = flDtls.caseID;
      dKey.foreignLiaisonID = flDtls.foreignLiaisonID;

      // Get country code.
      final BDMFECase BDMfeCase = BDMFECaseFactory.newInstance();
      final BDMFECaseKey BDMfeCaseKey = new BDMFECaseKey();
      BDMfeCaseKey.caseID = dKey.caseID;
      final BDMFECaseDtls BDMfeCaseDtls = BDMfeCase.read(BDMfeCaseKey);

      final BDMFACountryCodeKey cntryCodeKey = new BDMFACountryCodeKey();
      cntryCodeKey.countryCode = BDMfeCaseDtls.countryCode;

      // Get list of foreign offices.
      final BDMListOfOffices BDMListOfOffices =
        BDMMaintainForeignEngagementCaseFactory.newInstance()
          .getListOfForeignOffices(cntryCodeKey);

      final int officeListSz = BDMListOfOffices.bdmFO.size();

      if (officeListSz != 0) {
        BDMForeignOffice[] frgnOfficeArray =
          new BDMForeignOffice[officeListSz];
        frgnOfficeArray = BDMListOfOffices.bdmFO.items();

        for (int i = 0; i < frgnOfficeArray.length; i++) {
          if (frgnOfficeArray[i].externalPartyOfficeID == flDtls.fOfficeID) {
            flViewDetails.foreignOfficeAddressData =
              frgnOfficeArray[0].officeName;
          }
        }
      }
    }
    // Get BESS
    if (flDtls.bessInd) {
      flViewDetails.bessIndStr = CodetableUtil
        .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
    } else {
      flViewDetails.bessIndStr = CodetableUtil
        .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    }

    if (!StringUtil.isNullOrEmpty(flDtls.relatedFrgnLisnIDs)) {

      flViewDetails.liaisonRefDesc =
        contructRelatedFLiaisonAndRefDesc(flDtls.relatedFrgnLisnIDs);

    }

    if (!StringUtil.isNullOrEmpty(flDtls.liaisonChkStrList)) {

      flViewDetails = buildCheckListStrings(flDtls.liaisonChkStrList,
        flDtls.lisnChkLstOthrDesc, flViewDetails);

    }

    if (flDtls.fOfficeID != 0) {
      final ExternalPartyOfficeKey ofKey = new ExternalPartyOfficeKey();
      ofKey.externalPartyOfficeID = flDtls.fOfficeID;
      final NotFoundIndicator ofNfi = new NotFoundIndicator();
      final ExternalPartyOfficeDtls ofDtls =
        ExternalPartyOfficeFactory.newInstance().read(ofNfi, ofKey);

      flViewDetails.foreignOfficeName = ofDtls.name;

      if (!ofNfi.isNotFound() && ofDtls.primaryAddressID != 0) {
        final AddressKey addrKey = new AddressKey();
        addrKey.addressID = ofDtls.primaryAddressID;
        final BDMUtil bdmsUtil = new BDMUtil();
        flViewDetails.foreignOfficeAddressData =
          bdmsUtil.getFormattedAddress(addrKey);
      }
    }

    return flViewDetails;
  }

  /**
   * This is an utility method, which will build the check list responses as:
   * Yes/No.
   *
   * @param liaisonChkStrList
   * @param flViewDetails
   * @return
   */
  private BDMFrgnLiasnViewDetails buildCheckListStrings(
    final String liaisonChkStrList, final String lisnChkLstOthrDesc,
    final BDMFrgnLiasnViewDetails flViewDetails) {

    flViewDetails.chkLstAddrStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstAppealedStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstApplicationStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstCndnBnftRtStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstCntribtnStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstDcsnApprvlFBStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstDcsnDenialFBStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstDOBStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstDODStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstDORStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstFrgnPnsnRateStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstMdclAppmntStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstMdclDocmntnStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstNxtOfKinStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstPrtctveDtStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);
    flViewDetails.chkLstRsdncStr =
      CodetableUtil.getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.NO);

    final String strCheckListCodesArr[] =
      liaisonChkStrList.trim().split(CuramConst.gkTabDelimiter);

    String checkListCodeTableCode = "";

    for (int i = 0; i < strCheckListCodesArr.length; i++) {
      checkListCodeTableCode = strCheckListCodesArr[i].toString();

      if (checkListCodeTableCode.equals(BDMLIAISONCHECKLIST.ADDRESS)) {
        flViewDetails.chkLstAddrStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode.equals(BDMLIAISONCHECKLIST.APPEALED)) {
        flViewDetails.chkLstAppealedStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.APPLICATION)) {
        flViewDetails.chkLstApplicationStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.APPROVALOFFOREIGNBENEFIT)) {
        flViewDetails.chkLstDcsnApprvlFBStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.CANADIANBENEFITRATES)) {
        flViewDetails.chkLstCndnBnftRtStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.CONTRIBUTIONS)) {
        flViewDetails.chkLstCntribtnStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.DATEOFBIRTH)) {
        flViewDetails.chkLstDOBStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.DATEOFDEATH)) {
        flViewDetails.chkLstDODStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.DATEOFRECEIPT)) {
        flViewDetails.chkLstDORStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.DENIALOFFOREIGNBENEFIT)) {
        flViewDetails.chkLstDcsnDenialFBStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.FOREIGNPENSIONRATES)) {
        flViewDetails.chkLstFrgnPnsnRateStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.MEDICALAPPOINTMENT)) {
        flViewDetails.chkLstMdclAppmntStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.MEDICALDOCUMENTATION)) {
        flViewDetails.chkLstMdclDocmntnStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode.equals(BDMLIAISONCHECKLIST.NEXTOFKIN)) {
        flViewDetails.chkLstNxtOfKinStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode
        .equals(BDMLIAISONCHECKLIST.PROTECTIVEDATE)) {
        flViewDetails.chkLstPrtctveDtStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode.equals(BDMLIAISONCHECKLIST.RESIDENCE)) {
        flViewDetails.chkLstRsdncStr = CodetableUtil
          .getCodetableDescription(BDMYESNO.TABLENAME, BDMYESNO.YES);
      }

      else if (checkListCodeTableCode.equals(BDMLIAISONCHECKLIST.OTHER)) {
        flViewDetails.lisnChkLstOthrDesc = lisnChkLstOthrDesc;
      }

    }

    return flViewDetails;
  }

  /**
   * This is an utility method, to construct a string as "<Foreign Liaison
   * Direction>-<Foreign Liaison Reference Number>" delimited by a tab space, if
   * multiple foreign liaisons are linked to a foreign liaison.
   *
   * @param directionCd
   * @param liaisonReference
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String

    contructRelatedFLiaisonAndRefDesc(final String relatedForeignLiaisonIDs)
      throws AppException, InformationalException {

    String strFLiaisonsAndFLiaisonRefNumbers = "";

    final String strFAIdsArr[] =
      relatedForeignLiaisonIDs.trim().split(CuramConst.gkTabDelimiter);

    long fLiaisonID = CuramConst.kLongZero;

    BDMForeignLiaisonKey bdmForeignLiaisonKey = null;

    BDMForeignLiaisonDtls bdmForeignLiaisonDtls = null;

    final StringBuffer strBuffFLDirectionCdAndFLRefNumbers =
      new StringBuffer();

    for (int i = 0; i < strFAIdsArr.length; i++) {

      fLiaisonID = Long.parseLong(strFAIdsArr[i].toString());

      bdmForeignLiaisonKey = new BDMForeignLiaisonKey();

      bdmForeignLiaisonKey.foreignLiaisonID = fLiaisonID;

      bdmForeignLiaisonDtls =
        BDMForeignLiaisonFactory.newInstance().read(bdmForeignLiaisonKey);

      strBuffFLDirectionCdAndFLRefNumbers.append(
        CodetableUtil.getCodetableDescription(BDMLIAISONDIRECTION.TABLENAME,
          bdmForeignLiaisonDtls.direction));

      strBuffFLDirectionCdAndFLRefNumbers.append(CuramConst.gkDash);

      strBuffFLDirectionCdAndFLRefNumbers
        .append(bdmForeignLiaisonDtls.liaisonReference);

      strBuffFLDirectionCdAndFLRefNumbers.append(CuramConst.gkComma);

    }

    final int lastIndex =
      strBuffFLDirectionCdAndFLRefNumbers.toString().lastIndexOf(',');

    if (lastIndex != -1) {
      strFLiaisonsAndFLiaisonRefNumbers = strBuffFLDirectionCdAndFLRefNumbers
        .toString().substring(CuramConst.gkZero, lastIndex);
    }

    return strFLiaisonsAndFLiaisonRefNumbers;

  }

  /**
   * Method to view the foreign liaison history details.
   *
   * @param BDMFrgnLiasnHistIDKey key
   * @return BDMFrgnLiasnViewDetails viewHistoryDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetails
    viewForeignLiaisonHistoryDetails(final BDMFrgnLiasnHistIDKey key)
      throws AppException, InformationalException {

    BDMFrgnLiasnViewDetails flViewHistDetails = new BDMFrgnLiasnViewDetails();

    final BDMForeignLiaisonHist flHistObj =
      BDMForeignLiaisonHistFactory.newInstance();

    final BDMForeignLiaisonHistKey flHistKey = new BDMForeignLiaisonHistKey();
    flHistKey.frgnLsnHstID = key.frgnLsnHstID;

    final BDMForeignLiaisonHistDtls flHistDtls = flHistObj.read(flHistKey);

    flViewHistDetails.assign(flHistDtls);

    if (!StringUtil.isNullOrEmpty(flHistDtls.relatedFrgnLisnIDs)) {

      flViewHistDetails.liaisonRefDesc =
        contructRelatedFLiaisonAndRefDesc(flHistDtls.relatedFrgnLisnIDs);

    }

    if (!StringUtil.isNullOrEmpty(flHistDtls.liaisonChkStrList)) {

      flViewHistDetails = buildCheckListStrings(flHistDtls.liaisonChkStrList,
        flHistDtls.lisnChkLstOthrDesc, flViewHistDetails);

    }

    return flViewHistDetails;
  }

  /**
   * Method to list the foreign liaison's history.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFrgnLiasnViewDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnViewDetailsList listFoerignLiaisonHistory(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMFrgnLiasnViewDetailsList flHistDetailsList =
      new BDMFrgnLiasnViewDetailsList();
    final BDMForeignLiaisonHist flHistObj =
      BDMForeignLiaisonHistFactory.newInstance();

    final BDMReadByFrgnLiasnIDKey readByFLIDKey =
      new BDMReadByFrgnLiasnIDKey();
    readByFLIDKey.foreignLiaisonID = key.foreignLiaisonID;

    final BDMForeignLiaisonHistDtlsList flHistDtlsList =
      flHistObj.readByForeignLiaisonID(readByFLIDKey);

    // sort by reverse lastUpdatedOn
    Collections.sort(flHistDtlsList.dtls,
      (a, b) -> -a.lastUpdatedOn.compareTo(b.lastUpdatedOn));

    BDMFrgnLiasnViewDetails bdmFrgnLiasnViewDetails = null;

    for (final BDMForeignLiaisonHistDtls bdmForeignLiaisonHistDtls : flHistDtlsList.dtls) {
      bdmFrgnLiasnViewDetails = new BDMFrgnLiasnViewDetails();
      bdmFrgnLiasnViewDetails.assign(bdmForeignLiaisonHistDtls);

      bdmFrgnLiasnViewDetails.liaisonRefDesc =
        contructLiaisonDirectionAndRefDesc(
          bdmForeignLiaisonHistDtls.direction,
          bdmForeignLiaisonHistDtls.liaisonReference);

      if (!StringUtil
        .isNullOrEmpty(bdmForeignLiaisonHistDtls.foreignAppIDs.trim())) {

        bdmFrgnLiasnViewDetails.frgnAppRefDesc =
          contructFApplicationAndRefDesc(
            bdmForeignLiaisonHistDtls.foreignAppIDs.trim());
      }

      bdmFrgnLiasnViewDetails.lastUpdatedByAndOnStr =
        constructLastUpdatedByAndOn(bdmForeignLiaisonHistDtls.lastUpdatedBy,
          bdmForeignLiaisonHistDtls.lastUpdatedOn);

      flHistDetailsList.dtls.addRef(bdmFrgnLiasnViewDetails);
    }

    return flHistDetailsList;
  }

  /**
   * This is utility method to construct a string combined of last update by and
   * last updated on fields.
   *
   * @param lastUpdatedBy
   * @param lastUpdatedOn
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private String constructLastUpdatedByAndOn(final String lastUpdatedBy,
    final DateTime lastUpdatedOn)
    throws AppException, InformationalException {

    String lastUpdatedByAndOnStr = "";

    if (!StringUtil.isNullOrEmpty(lastUpdatedBy)) {

      final UsersKey usersKey = new UsersKey();
      usersKey.userName = lastUpdatedBy;
      final UsersDtls usersDtls = UsersFactory.newInstance().read(usersKey);

      final LocalisableString text =
        new LocalisableString(BDMFEC.INF_FRGN_LIASN_HIST_UPDATED_BY_TEXT);
      text.arg(usersDtls.fullName);
      text.arg(lastUpdatedOn);

      lastUpdatedByAndOnStr =
        text.getMessage(TransactionInfo.getProgramLocale());

      if (lastUpdatedByAndOnStr.endsWith("_fr")) {
        lastUpdatedByAndOnStr = lastUpdatedByAndOnStr
          .substring(CuramConst.gkZero, lastUpdatedByAndOnStr.length() - 3);
      }
    }

    return lastUpdatedByAndOnStr;
  }

  /**
   * Method to list the foreign liaison's attachments.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList listForeignLiaisonAttachments(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMFLAttachmentDetailsList flAttDetailsList =
      new BDMFLAttachmentDetailsList();

    // Get caseID
    final BDMForeignLiaisonDtls flDtls =
      this.readForeignLiaison(key.foreignLiaisonID);

    final BDMFEAttachmentLink flAttObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMSearchFLAttachmentsKey searchFLAttachmentsKey =
      new BDMSearchFLAttachmentsKey();
    searchFLAttachmentsKey.foreignLiaisonID = key.foreignLiaisonID;
    searchFLAttachmentsKey.attachmentLinkTypCd =
      BDMATTACHMENTLINKTYPE.FLATTACHMENTLINK;

    final BDMReadFLAtchmntDetailsList flAttLnkDtlsList =
      flAttObj.searchForeignLiaisonAttachments(searchFLAttachmentsKey);

    BDMFLAttachmentDetails bdmflAttachmentDetails = null;

    for (final BDMReadFLAtchmntDetails bdmForeignLsnAttLnkDtls : flAttLnkDtlsList.dtls) {
      bdmflAttachmentDetails = new BDMFLAttachmentDetails();
      bdmflAttachmentDetails.assign(bdmForeignLsnAttLnkDtls);

      bdmflAttachmentDetails.caseID = flDtls.caseID;

      flAttDetailsList.dtls.addRef(bdmflAttachmentDetails);
    }

    return flAttDetailsList;
  }

  /**
   * Method to list the foreign liaison history attachments.
   *
   * @param BDMFrgnLiasnHistIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList
    listForeignLiaisonHistAtchmnts(final BDMFrgnLiasnHistIDKey key)
      throws AppException, InformationalException {

    final BDMFLAttachmentDetailsList flAttDetailsList =
      new BDMFLAttachmentDetailsList();

    final List<BDMReadFLAtchmntDetails> flAttLnkHistDtlsList =
      this.getForeignLiaisonHistoryRecords(key.frgnLsnHstID);

    for (final BDMReadFLAtchmntDetails bdmForgnLsnAttLnkHstDtls : flAttLnkHistDtlsList) {
      final BDMFLAttachmentDetails bdmflAttachmentDetails =
        new BDMFLAttachmentDetails();

      bdmflAttachmentDetails.assign(bdmForgnLsnAttLnkHstDtls);

      flAttDetailsList.dtls.addRef(bdmflAttachmentDetails);
    }

    return flAttDetailsList;
  }

  /**
   * Method to list the foreign applications.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFrgnAppDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnAppDetailsList listForeignAppsByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMFrgnAppDetailsList frgnAppDetailsList =
      new BDMFrgnAppDetailsList();

    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    final BDMFAKeyStruct3 bdmfaKeyStruct3 = new BDMFAKeyStruct3();
    bdmfaKeyStruct3.caseID = key.caseID;

    final BDMForeignApplicationDtlsList bdmfaDtlsList =
      bdmfa.readByCaseID(bdmfaKeyStruct3);

    BDMFrgnAppDetails fgnAppDetails = null;
    for (final BDMForeignApplicationDtls faDtls : bdmfaDtlsList.dtls) {
      if (!faDtls.recordStatus.equals(RECORDSTATUS.CANCELLED)) {
        fgnAppDetails = new BDMFrgnAppDetails();
        fgnAppDetails.assign(faDtls);

        final String appTypeDesc = CodeTable.getOneItemForUserLocale(
          BDMFOREIGNAPPTYPE.TABLENAME, fgnAppDetails.typeCode);

        fgnAppDetails.frgnAppRefDesc =
          appTypeDesc + " - " + fgnAppDetails.faReferenceNumber;

        frgnAppDetailsList.dtls.addRef(fgnAppDetails);
      }
    }

    return frgnAppDetailsList;
  }

  /**
   * Method to list all the foreign liaisons of the foreign benefit.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFrgnLiasnRefDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnRefDetailsList listLiaisonReferencesByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMFrgnLiasnRefDetailsList flRefDetailsList =
      new BDMFrgnLiasnRefDetailsList();

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMReadFLByCaseIDKey readFLByCaseIDKey = new BDMReadFLByCaseIDKey();
    readFLByCaseIDKey.caseID = key.caseID;

    final List<BDMForeignLiaisonDtls> activeFlList =
      flObj.readForeignLiaisonByCaseID(readFLByCaseIDKey).dtls.stream()
        .filter(dtls -> !RECORDSTATUS.CANCELLED.equals(dtls.recordStatus))
        .collect(toList());

    for (final BDMForeignLiaisonDtls foreignLiaisonDtls : activeFlList) {
      final BDMFrgnLiasnRefDetails frgnLiasnRefDetails =
        new BDMFrgnLiasnRefDetails();
      frgnLiasnRefDetails.assign(foreignLiaisonDtls);

      final String liasnDirecrtionDesc = CodeTable.getOneItemForUserLocale(
        BDMLIAISONDIRECTION.TABLENAME, frgnLiasnRefDetails.direction);

      frgnLiasnRefDetails.liaisonRefDesc =
        liasnDirecrtionDesc + " - " + frgnLiasnRefDetails.liaisonReference;

      flRefDetailsList.dtls.addRef(frgnLiasnRefDetails);
    }

    return flRefDetailsList;
  }

  /**
   * Method to list the liaison checklist codes.
   *
   * @param
   * @return BDMLiasnChecklistCodeDetailsList wizardID.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMLiasnChecklistCodeDetailsList listAllLiasnChecklistCodes()
    throws AppException, InformationalException {

    final BDMLiasnChecklistCodeDetailsList chklistCdDetailsList =
      new BDMLiasnChecklistCodeDetailsList();

    // Codetable maintenance business process object
    final CodeTableAdmin codeTableAdminObj =
      CodeTableAdminFactory.newInstance();

    // CodeTableItemDetails struct
    BDMLiasnChecklistCodeDetails codeTableItemDetails = null;

    final String codetableName = BDMLIAISONCHECKLIST.TABLENAME;

    final String locale = TransactionInfo.getProgramLocale();
    final CodeTableItemDetailsList codeTableItemList = codeTableAdminObj
      .listAllItemsForLocaleAndLanguage(codetableName, locale);

    for (final CodeTableItemDetails ctDetails : codeTableItemList.dtls
      .items()) {
      codeTableItemDetails = new BDMLiasnChecklistCodeDetails();
      codeTableItemDetails.assign(ctDetails);

      chklistCdDetailsList.dtls.addRef(codeTableItemDetails);
    }

    return chklistCdDetailsList;
  }

  /**
   * Method to list the FECase attachments.
   *
   * @param BDMFECaseIDKey key
   * @return BDMFLAttachmentDetailsList detailsList.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFLAttachmentDetailsList listAttachmentsForFLByCaseID(
    final BDMFECaseIDKey key) throws AppException, InformationalException {

    final BDMFLAttachmentDetailsList flAttDetailsList =
      new BDMFLAttachmentDetailsList();

    final curam.ca.gc.bdm.sl.attachment.intf.BDMMaintainAttachment bdmmaintainAttachmentObj =
      BDMMaintainAttachmentFactory.newInstance();

    final AttachmentCaseID attachmentCaseID = new AttachmentCaseID();
    attachmentCaseID.caseID = key.caseID;

    final BDMCaseAttachmentAndLinkDetailsList caseAttLnkDetailsList =
      bdmmaintainAttachmentObj.searchIntegCaseAttachments(attachmentCaseID);
    BDMFLAttachmentDetails flAttDetails = null;
    for (final BDMCaseAttachmentAndLinkDetails caseAttLinkDetails : caseAttLnkDetailsList.dtls) {
      if (!RECORDSTATUS.CANCELLED.equals(caseAttLinkDetails.dtls.statusCode)
        && FL_RELATED_DOC_TYPES.contains(caseAttLinkDetails.documentType)) {
        flAttDetails = new BDMFLAttachmentDetails();
        flAttDetails.assign(caseAttLinkDetails.dtls);
        flAttDetails.documentType = caseAttLinkDetails.documentType;
        flAttDetails.fileSource = caseAttLinkDetails.fileSource;
        flAttDetails.dateReceipt = caseAttLinkDetails.dateReceipt;

        flAttDetailsList.dtls.addRef(flAttDetails);
      }
    }

    return flAttDetailsList;
  }

  /**
   * Method to delete the foreign liaison.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void deleteForeignLiaison(final BDMFrgnLiasnDeleteKey key)
    throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = key.foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls = flObj.read(flKey);

    // Validate
    this.validateDeleteFL(flDtls);

    // Cancel the FL record.
    flDtls.recordStatus = RECORDSTATUS.CANCELLED;
    flDtls.deleteReason = BDMLIAISONDELREASON.ENTEREDINERROR;
    flDtls.versionNo = key.versionNo;

    flObj.modify(flKey, flDtls);
  }

  /**
   * Method to validate the deletion of foreign liaison.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private void validateDeleteFL(final BDMForeignLiaisonDtls flDtls)
    throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (flDtls.recordStatus.equals(RECORDSTATUS.CANCELLED)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_RECORD_ALREADY_DELETED), "",
        InformationalType.kError);
    }

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMReadFLByCaseIDKey readFLByCaseIDKey = new BDMReadFLByCaseIDKey();
    readFLByCaseIDKey.caseID = flDtls.caseID;

    final BDMForeignLiaisonDtlsList flDtlsList =
      flObj.readForeignLiaisonByCaseID(readFLByCaseIDKey);

    for (final BDMForeignLiaisonDtls bdmForeignLiaisonDtls : flDtlsList.dtls) {
      if (bdmForeignLiaisonDtls.foreignLiaisonID != flDtls.foreignLiaisonID) {

        final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
        flKey.foreignLiaisonID = bdmForeignLiaisonDtls.foreignLiaisonID;
        if (RECORDSTATUS.CANCELLED.equals(flObj.read(flKey).recordStatus)) {
          continue;
        }

        final String relatedFLsIDList =
          bdmForeignLiaisonDtls.relatedFrgnLisnIDs;

        final StringList flIDList =
          StringUtil.tabText2StringListWithTrim(relatedFLsIDList);

        boolean isRelated = false;
        for (int i = 0; i < flIDList.size(); i++) {
          final String foreignLiaisonID = flIDList.item(i);
          if (flDtls.foreignLiaisonID == Long.valueOf(foreignLiaisonID)) {
            isRelated = true;
            break;
          }
        }

        if (isRelated) {
          ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
            new AppException(BDMFEC.ERR_FRGN_LIASN_RECORD_CANNOT_BE_DELETED),
            "", InformationalType.kError);
        }
      }
    }

    // Fail for business validations.
    informationalManager.failOperation();
  }

  /**
   * Method to unlink the foreign liaison.
   *
   * @param BDMFrgnLiasnAttLnkIDKey key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void unlinkFrgnLiasnAttachment(final BDMFrgnLiasnAttLnkIDKey key)
    throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttLnkObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMFEAttachmentLinkKey flAttLnkKey = new BDMFEAttachmentLinkKey();
    flAttLnkKey.feAttachmentLinkID = key.feAttachmentLinkID;

    flAttLnkObj.remove(flAttLnkKey);
  }

  /**
   * Method to read the foreign liaison details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMForeignLiaisonDetails details..
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMForeignLiaisonDetails readForeignLiaisonDetails(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMForeignLiaisonDetails flReadDetails =
      new BDMForeignLiaisonDetails();

    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = key.foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls = flObj.read(flKey);

    flReadDetails.assign(flDtls);
    flReadDetails.frgnAppIDTabList = flDtls.foreignAppIDs;
    flReadDetails.flChkLstCdTabList = flDtls.liaisonChkStrList;
    flReadDetails.frgnLiasnIDTabList = flDtls.relatedFrgnLisnIDs;

    return flReadDetails;
  }

  /**
   * Method to create the foreign liaison.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    createForeignLiaisonFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    // validate input details.
    this.validateFLDetailsFromWizard(key);

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.wizardStateID = key.wizardStateID;
    readWizDetails.dtls.receiveDate = key.dtls.receiveDate;
    readWizDetails.dtls.direction = key.dtls.direction;
    readWizDetails.dtls.foreignIdntifier = key.dtls.foreignIdntifier;
    readWizDetails.dtls.fOfficeID = key.dtls.fOfficeID;
    readWizDetails.dtls.externalPartyOfficeID =
      key.dtls.externalPartyOfficeID;
    readWizDetails.dtls.frgnLiasnIDTabList = key.dtls.frgnLiasnIDTabList;
    readWizDetails.dtls.frgnAppIDTabList = key.dtls.frgnAppIDTabList;
    readWizDetails.dtls.comments = key.dtls.comments;
    readWizDetails.dtls.bessInd = key.dtls.bessInd;
    readWizDetails.dtls.caseID = key.dtls.caseID;

    if (key.actionIDProperty.equals(BDMConstants.kStep1Next)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kStep1SaveAndClose)) {
      this.createForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to validate the foreign liaison.
   *
   * @param bdmFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  protected void
    validateFLDetailsFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (BDMUtil.isIOClientContact()
      && key.dtls.direction.equalsIgnoreCase(BDMLIAISONDIRECTION.OUTGOING)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_SUFFICIENT_PRIVILEGE_OUTGOING_LIAISON),
        "", InformationalType.kError);
    }

    if (key.dtls.receiveDate.isZero()) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_DATE_MUST_BE_ENTERED), "",
        InformationalType.kError);
    }

    if (!key.dtls.receiveDate.isZero()
      && key.dtls.receiveDate.after(Date.getCurrentDate())) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_DATE_CANNOT_BE_IN_FUTURE), "",
        InformationalType.kError);
    }

    if (StringUtil.isNullOrEmpty(key.dtls.direction)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_DIRECTION_MUST_BE_ENTERED), "",
        InformationalType.kError);
    }

    if (key.dtls.direction.equals(BDMLIAISONDIRECTION.OUTGOING)
      && key.dtls.bessInd) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_BESS_CANNOT_BE_ENTERED), "",
        InformationalType.kError);
    }

    validateForeignIdentifierMatch(key.dtls);

    // Fail for business validations.
    informationalManager.failOperation();
  }

  /**
   * Validate if the Foreign Identifier selected matches
   * the Foreign Identifier of the selected foreign application(s).
   *
   * @param dtls
   * @throws AppException
   * @throws InformationalException
   */
  protected void
    validateForeignIdentifierMatch(final BDMForeignLiaisonDetails dtls)
      throws AppException, InformationalException {

    if (!StringUtil.isNullOrEmpty(dtls.foreignIdntifier)
      && !StringUtil.isNullOrEmpty(dtls.frgnAppIDTabList)) {

      final StringList appIDList =
        StringUtil.tabText2StringListWithTrim(dtls.frgnAppIDTabList);

      for (final String appID : appIDList.items()) {
        final BDMForeignApplicationKey faKey = new BDMForeignApplicationKey();
        faKey.fApplicationID = Long.parseLong(appID);
        final BDMForeignApplicationDtls faDtls =
          BDMForeignApplicationFactory.newInstance().read(faKey);

        if (faDtls.fIdentifier != 0) {
          final ConcernRoleAlternateIDKey altIDKey =
            new ConcernRoleAlternateIDKey();
          altIDKey.concernRoleAlternateID = faDtls.fIdentifier;
          final ConcernRoleAlternateIDDtls altIDDtls =
            ConcernRoleAlternateIDFactory.newInstance().read(altIDKey);

          if (!dtls.foreignIdntifier.equals(altIDDtls.alternateID)) {
            ValidationManagerFactory.getManager()
              .addInfoMgrExceptionWithLookup(
                new AppException(BDMFEC.ERR_FRGN_IDENTIFIER_DOES_NOT_MATCH),
                "", InformationalType.kWarning);
            break;
          }
        }
      }
    }
  }

  /**
   * Method to validate the foreign liaison.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private void
    validateFLChecklistFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final InformationalManager informationalManager =
      TransactionInfo.getInformationalManager();

    if (StringUtil.isNullOrEmpty(key.dtls.flChkLstCdTabList)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_CHECKLIST_MUST_BE_ENTERED), "",
        InformationalType.kError);
    }

    final StringList checklistCdStrList =
      StringUtil.tabText2StringListWithTrim(key.dtls.flChkLstCdTabList);

    boolean isOtherCdSelected = false;
    for (int i = 0; i < checklistCdStrList.size(); i++) {
      final String checkListCd = checklistCdStrList.item(i);

      if (checkListCd.equals(BDMLIAISONCHECKLIST.OTHER)) {
        isOtherCdSelected = true;
      }
    }

    if (isOtherCdSelected
      && StringUtil.isNullOrEmpty(key.dtls.lisnChkLstOthrDesc)) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_DESCRIPTION_MUST_BE_ENTERED),
        "", InformationalType.kError);
    }

    if (!StringUtil.isNullOrEmpty(key.dtls.lisnChkLstOthrDesc)
      && !isOtherCdSelected) {
      ValidationManagerFactory.getManager().addInfoMgrExceptionWithLookup(
        new AppException(BDMFEC.ERR_FRGN_LIASN_OTHER_MUST_BE_SELECTED), "",
        InformationalType.kError);
    }

    // Fail for business validations.
    informationalManager.failOperation();
  }

  /**
   * Method to create the foreign liaison.
   *
   * @param BDMFrgnLiasnWizardDetails wizDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private void
    createForeignLiaison(final BDMFrgnLiasnWizardDetails wizardDetails)
      throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();

    final BDMForeignLiaisonDtls flDtls = new BDMForeignLiaisonDtls();
    flDtls.assign(wizardDetails.dtls);
    flDtls.foreignAppIDs = wizardDetails.dtls.frgnAppIDTabList;
    flDtls.liaisonChkStrList = wizardDetails.dtls.flChkLstCdTabList;
    flDtls.relatedFrgnLisnIDs = wizardDetails.dtls.frgnLiasnIDTabList;
    flDtls.fOfficeID = wizardDetails.dtls.externalPartyOfficeID;

    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;
    flDtls.foreignLiaisonID = uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);

    uniqueIDKeySet.keySetName = BDMConstants.kBDMFLKEYSET;
    flDtls.liaisonReference =
      String.valueOf(uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet));
    flDtls.recordStatus = RECORDSTATUS.NORMAL;
    flObj.insert(flDtls);

    // Create foreign liaison history record.
    final BDMForeignLiaisonHistDtls flHistDtls =
      this.createForeignLiaisonHistory(flDtls);

    // Create foreign liaison attachments and attachments history.
    this.createForeignLiaisonAttachments(
      wizardDetails.dtls.flSelectedAttIDList, flHistDtls.foreignLiaisonID);

    this.createForeignLiaisonHistoryAttachments(
      wizardDetails.dtls.flSelectedAttIDList, flHistDtls);

    // Task 92502: DEV: TASK-01 - Foreign Application Task-R1-S8
    raiseAndCloseOpenAttachmentNotificationTask(wizardDetails);

  }

  /**
   * Task 92502: DEV: TASK-01 - Foreign Application Task-R1-S8
   * Auto Close when the outgoing liaison is attached to the foreign Application
   *
   * @param wizardDetails
   * @throws AppException
   * @throws InformationalException
   */
  private void raiseAndCloseOpenAttachmentNotificationTask(
    final BDMFrgnLiasnWizardDetails wizardDetails)
    throws AppException, InformationalException {

    final BDMFAAttachmentNoticationTaskDetails attachmentNoticationTaskDetails =
      new BDMFAAttachmentNoticationTaskDetails();
    BDMForeignApplicationDtls bdmfaDtls = null;
    final BDMForeignApplication bdmfa =
      BDMForeignApplicationFactory.newInstance();

    if (wizardDetails.dtls.frgnAppIDTabList.length() > CuramConst.gkZero) {

      final String strFAIdsArr[] = wizardDetails.dtls.frgnAppIDTabList.trim()
        .split(CuramConst.gkTabDelimiter);

      for (int i = 0; i < strFAIdsArr.length; i++) {

        if (wizardDetails.dtls.direction
          .equals(BDMLIAISONDIRECTION.OUTGOING)) {

          attachmentNoticationTaskDetails.fApplicationID =
            Long.parseLong(strFAIdsArr[i]);

          // START BUG 99047 -
          final curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey bdmfaKey =
            new curam.ca.gc.bdm.entity.struct.BDMForeignApplicationKey();
          bdmfaKey.fApplicationID =
            attachmentNoticationTaskDetails.fApplicationID;
          bdmfaDtls = bdmfa.read(bdmfaKey);
          bdmfaDtls.status = BDMFOREIGNAPPSTATUS.COMPLETED;
          bdmfa.modify(bdmfaKey, bdmfaDtls);
          // END BUG 99047

          // START BUG 103181 -
          final BizObjAssociation bizObjAssociation =
            BizObjAssociationFactory.newInstance();

          final BizObjectTypeKey bizObjectTypeKey = new BizObjectTypeKey();
          bizObjectTypeKey.bizObjectID = bdmfaDtls.caseID;
          bizObjectTypeKey.bizObjectType = BUSINESSOBJECTTYPE.CASE;

          final BizObjAssocSearchDetailsList bizObjAssocSearchDetailsList =
            bizObjAssociation.searchByBizObjectTypeAndID(bizObjectTypeKey);

          final TaskKey taskKey = new TaskKey();
          for (final BizObjAssocSearchDetails assocSearchDetails : bizObjAssocSearchDetailsList.dtls) {

            if (assocSearchDetails.taskID != CuramConst.gkZero) {

              taskKey.taskID = assocSearchDetails.taskID;

              final BizObjAssociationDtlsList bizObjAssociationDtlsList =
                bizObjAssociation.searchByTaskID(taskKey);

              for (final BizObjAssociationDtls bizObjAssociationDtls : bizObjAssociationDtlsList.dtls) {

                if (bizObjAssociationDtls.bizObjectType
                  .equals(BUSINESSOBJECTTYPE.BDMFOREIGNAPPLICATION)
                  || bizObjAssociationDtls.bizObjectType
                    .equals(BUSINESSOBJECTTYPE.BDMATTACHMENT)) {

                  final Event closeTaskEvent = new Event();
                  closeTaskEvent.eventKey.eventClass = TASK.CLOSED.eventClass;
                  closeTaskEvent.eventKey.eventType = TASK.CLOSED.eventType;
                  closeTaskEvent.primaryEventData =
                    bizObjAssociationDtls.taskID;
                  EventService.raiseEvent(closeTaskEvent);
                }
              }

            }
          }
          // END BUG 103181 -
        }
      }
    }
  }

  /**
   * Method to create the foreign liaison.
   *
   * @param BDMForeignLiaisonDtls key
   * @return BDMForeignLiaisonHistDtls wizardID.
   * @throws AppException
   * @throws InformationalException
   */
  private BDMForeignLiaisonHistDtls
    createForeignLiaisonHistory(final BDMForeignLiaisonDtls liasnDtls)
      throws AppException, InformationalException {

    final BDMForeignLiaisonHist flHistObj =
      BDMForeignLiaisonHistFactory.newInstance();

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMForeignLiaisonHistDtls flHistDtls =
      new BDMForeignLiaisonHistDtls();
    flHistDtls.assign(liasnDtls);
    flHistDtls.frgnLsnHstID = uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);

    flHistObj.insert(flHistDtls);

    return flHistDtls;
  }

  /**
   * Method to create the foreign liaison attachments.
   *
   * @param String flSelectedAttIDList, long foreignLiaisonID
   * @return void.
   * @throws AppException
   * @throws InformationalException
   */
  private void createForeignLiaisonAttachments(
    final String flSelectedAttIDList, final long foreignLiaisonID)
    throws AppException, InformationalException {

    final StringList attachmentIDStrList =
      StringUtil.tabText2StringListWithTrim(flSelectedAttIDList);

    // Creating foreign liaison attachments.
    for (int i = 0; i < attachmentIDStrList.size(); i++) {
      final String attachmentID = attachmentIDStrList.item(i);
      if (attachmentID.length() > 0) {
        this.insertIntoFLAttLnk(foreignLiaisonID,
          Long.parseLong(attachmentID));
      }
    }
  }

  /**
   * Method to create the foreign liaison history attachments.
   *
   * @param String flSelectedAttIDList, BDMForeignLiaisonHistDtls flHistDtls
   * @return void.
   * @throws AppException
   * @throws InformationalException
   */
  void createForeignLiaisonHistoryAttachments(
    final String flSelectedAttIDList,
    final BDMForeignLiaisonHistDtls flHistDtls)
    throws AppException, InformationalException {

    final curam.ca.gc.bdm.entity.intf.BDMForgnLsnAttLnkHst flAttLnkHistObj =
      BDMForgnLsnAttLnkHstFactory.newInstance();

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    BDMForgnLsnAttLnkHstDtls flAttLnkHistDtls = null;

    final StringList attachmentIDStrList =
      StringUtil.tabText2StringListWithTrim(flSelectedAttIDList);

    // Creating foreign liaison attachments.
    for (int i = 0; i < attachmentIDStrList.size(); i++) {
      final String attachmentID = attachmentIDStrList.item(i);
      if (attachmentID.length() > 0) {
        flAttLnkHistDtls = new BDMForgnLsnAttLnkHstDtls();
        flAttLnkHistDtls.frgnLsnAttLnkHstID =
          uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);
        flAttLnkHistDtls.frgnLsnHstID = flHistDtls.frgnLsnHstID;
        flAttLnkHistDtls.attachmentID = Long.parseLong(attachmentID);
        flAttLnkHistDtls.recordStatus = RECORDSTATUS.NORMAL;
        flAttLnkHistObj.insert(flAttLnkHistDtls);
      }
    }
  }

  /**
   * Method to create the foreign liaison attachments.
   *
   * @param String flSelectedAttIDList, long foreignLiaisonID
   * @return void.
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyForeignLiaisonAttachments(
    final String flSelectedAttIDList, final long foreignLiaisonID)
    throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttLnkObj =
      BDMFEAttachmentLinkFactory.newInstance();

    BDMFEAttachmentLinkKey attLnkKey = null;

    final StringList attachmentIDStrList =
      StringUtil.tabText2StringListWithTrim(flSelectedAttIDList);

    final Set<Long> currentAttIds = new HashSet<>();

    final BDMFEAttachmentLinkDtlsList flAttLnkDtlsLst =
      this.getFrgnLiasnAttIDList(foreignLiaisonID);

    for (final BDMFEAttachmentLinkDtls attLnkDtls : flAttLnkDtlsLst.dtls) {
      currentAttIds.add(attLnkDtls.attachmentID);
      if (!attachmentIDStrList
        .contains(String.valueOf(attLnkDtls.attachmentID))) {
        // Remove the attachment linked record.
        attLnkKey = new BDMFEAttachmentLinkKey();
        attLnkKey.feAttachmentLinkID = attLnkDtls.feAttachmentLinkID;
        flAttLnkObj.remove(attLnkKey);
      }
    }

    // Creating foreign liaison attachments.
    for (int i = 0; i < attachmentIDStrList.size(); i++) {
      final String attachmentID = attachmentIDStrList.item(i);
      if (attachmentID.length() > 0
        && !currentAttIds.contains(Long.valueOf(attachmentID))) {
        this.insertIntoFLAttLnk(foreignLiaisonID,
          Long.parseLong(attachmentID));
      }
    }
  }

  /**
   * Method to insert the attachment link record.
   *
   * @param long foreignLiaisonID, long attachmentID
   * @return void.
   * @throws AppException
   * @throws InformationalException
   */
  private void insertIntoFLAttLnk(final long foreignLiaisonID,
    final long attachmentID) throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttLnkObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final UniqueID uniqueIDObj = UniqueIDFactory.newInstance();
    final UniqueIDKeySet uniqueIDKeySet = new UniqueIDKeySet();
    uniqueIDKeySet.keySetName = BDMConstants.kBDMDEFKEYSET;

    final BDMFEAttachmentLinkDtls flAttLnkDtls =
      new BDMFEAttachmentLinkDtls();
    flAttLnkDtls.feAttachmentLinkID =
      uniqueIDObj.getNextIDFromKeySet(uniqueIDKeySet);
    flAttLnkDtls.relatedID = foreignLiaisonID;
    flAttLnkDtls.attachmentLinkTypCd = BDMATTACHMENTLINKTYPE.FLATTACHMENTLINK;
    flAttLnkDtls.recordStatus = RECORDSTATUS.NORMAL;
    flAttLnkDtls.attachmentID = attachmentID;
    flAttLnkObj.insert(flAttLnkDtls);

  }

  /**
   * Method to create the wizard.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return void.
   * @throws AppException
   * @throws InformationalException
   */
  private long createWizard(final BDMFrgnLiasnWizardDetails key)
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();
    final long wizardStateID = wizardPersistentState.create(key);

    return wizardStateID;
  }

  /**
   * Method to read the wizard details.
   *
   * @param wizardStateID
   * @return BDMFrgnLiasnWizardDetails wizDetails.
   * @throws AppException
   * @throws InformationalException
   */
  private BDMFrgnLiasnWizardDetails readWizardDetails(
    final long wizardStateID) throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    final BDMFrgnLiasnWizardDetails wizDetails =
      (BDMFrgnLiasnWizardDetails) wizardPersistentState.read(wizardStateID);

    return wizDetails;
  }

  /**
   * Method to modify the wizard details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return void
   * @throws AppException
   * @throws InformationalException
   */
  private void modifyWizard(final long wizardStateID,
    final BDMFrgnLiasnWizardDetails readWizDetails)
    throws AppException, InformationalException {

    final WizardPersistentState wizardPersistentState =
      new WizardPersistentState();

    wizardPersistentState.modify(wizardStateID, readWizDetails);
  }

  /**
   * Method to modify the foreign liaison details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyForeignLiaisonFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    // validate input details.
    this.validateFLDetailsFromWizard(key);

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.dtls.foreignLiaisonID = key.dtls.foreignLiaisonID;
    readWizDetails.dtls.receiveDate = key.dtls.receiveDate;
    readWizDetails.dtls.direction = key.dtls.direction;
    readWizDetails.dtls.foreignIdntifier = key.dtls.foreignIdntifier;
    readWizDetails.dtls.fOfficeID = key.dtls.fOfficeID;
    readWizDetails.dtls.externalPartyOfficeID =
      key.dtls.externalPartyOfficeID;
    readWizDetails.dtls.frgnLiasnIDTabList = key.dtls.frgnLiasnIDTabList;
    readWizDetails.dtls.frgnAppIDTabList = key.dtls.frgnAppIDTabList;
    readWizDetails.dtls.comments = key.dtls.comments;
    readWizDetails.dtls.bessInd = key.dtls.bessInd;
    readWizDetails.dtls.caseID = key.dtls.caseID;

    if (key.actionIDProperty.equals(BDMConstants.kStep1Next)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kStep1SaveAndClose)) {
      this.modifyForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to create the foreign liaison.
   *
   * @param wizardDetails
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  private void
    modifyForeignLiaison(final BDMFrgnLiasnWizardDetails wizardDetails)
      throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMForeignLiaisonKey frgnLiasnKey = new BDMForeignLiaisonKey();
    frgnLiasnKey.foreignLiaisonID = wizardDetails.dtls.foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls = flObj.read(frgnLiasnKey);
    flDtls.assign(wizardDetails.dtls);
    flDtls.foreignAppIDs = wizardDetails.dtls.frgnAppIDTabList;
    flDtls.liaisonChkStrList = wizardDetails.dtls.flChkLstCdTabList;
    flDtls.relatedFrgnLisnIDs = wizardDetails.dtls.frgnLiasnIDTabList;
    flDtls.fOfficeID = wizardDetails.dtls.externalPartyOfficeID;
    flDtls.lisnChkLstOthrDesc = wizardDetails.dtls.lisnChkLstOthrDesc;

    flObj.modify(frgnLiasnKey, flDtls);

    // Create foreign liaison history record.
    final BDMForeignLiaisonHistDtls flHistDtls =
      this.createForeignLiaisonHistory(flDtls);

    // Create foreign liaison attachments and attachments history.
    this.modifyForeignLiaisonAttachments(
      wizardDetails.dtls.flSelectedAttIDList, flHistDtls.foreignLiaisonID);

    this.createForeignLiaisonHistoryAttachments(
      wizardDetails.dtls.flSelectedAttIDList, flHistDtls);
  }

  /**
   * Method to read foreign liaison wizard details.
   *
   * @param WizardStateID key
   * @return BDMFrgnLiasnWizardDetails wizDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMFrgnLiasnWizardDetails readForeignLiaisonWizardDetails(
    final WizardStateID key) throws AppException, InformationalException {

    final WizardStateID wizStateID = new WizardStateID();
    BDMFrgnLiasnWizardDetails readWizDetails =
      new BDMFrgnLiasnWizardDetails();

    if (key.wizardStateID == 0) {
      // Create empty wizard.
      wizStateID.wizardStateID = this.createWizard(readWizDetails);
      readWizDetails.wizardStateID = wizStateID.wizardStateID;
    } else {
      readWizDetails = this.readWizardDetails(key.wizardStateID);
      readWizDetails.wizardStateID = key.wizardStateID;
    }

    return readWizDetails;
  }

  /**
   * Method to create and populate foreign liaison wizard details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return WizardStateID wizardID.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public WizardStateID createAndPopulateFrgnLiasnWizard(
    final BDMFrgnLiasnIDKey key) throws AppException, InformationalException {

    final WizardStateID wizStateID = new WizardStateID();
    final BDMFrgnLiasnWizardDetails flWizardDetails =
      new BDMFrgnLiasnWizardDetails();

    if (key.foreignLiaisonID != 0) {
      final BDMForeignLiaisonDtls flDtls =
        this.readForeignLiaison(key.foreignLiaisonID);

      flWizardDetails.dtls.assign(flDtls);
      flWizardDetails.dtls.frgnAppIDTabList = flDtls.foreignAppIDs;
      flWizardDetails.dtls.flChkLstCdTabList = flDtls.liaisonChkStrList;
      flWizardDetails.dtls.frgnLiasnIDTabList = flDtls.relatedFrgnLisnIDs;
      flWizardDetails.dtls.externalPartyOfficeID = flDtls.fOfficeID;

      // get attachmentIDs list.
      final String flAttIDsList =
        this.getFLAttIDTabDelmStrList(key.foreignLiaisonID);
      flWizardDetails.dtls.flSelectedAttIDList = flAttIDsList;
    }

    // Creating empty wizard.
    wizStateID.wizardStateID = this.createWizard(flWizardDetails);

    return wizStateID;
  }

  /**
   * Method to create the foreign liaison checklist.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void createFLChecklistFromWizard(final BDMFrgnLiasnWizardDetails key)
    throws AppException, InformationalException {

    key.dtls.lisnChkLstOthrDesc = key.dtls.lisnChkLstOthrDesc.trim();
    // Validate input details.
    this.validateFLChecklistFromWizard(key);

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.wizardStateID = key.wizardStateID;
    readWizDetails.dtls.flChkLstCdTabList = key.dtls.flChkLstCdTabList;
    readWizDetails.dtls.lisnChkLstOthrDesc = key.dtls.lisnChkLstOthrDesc;

    if (key.actionIDProperty.equals(BDMConstants.kStep2Next)
      || key.actionIDProperty.equals(BDMConstants.kStep2Back)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kStep2SaveAndClose)) {
      this.createForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to create the foreign liaison attachments.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    createFLAttachmentsFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.wizardStateID = key.wizardStateID;
    readWizDetails.dtls.flSelectedAttIDList = key.dtls.flSelectedAttIDList;

    if (key.actionIDProperty.equals(BDMConstants.kStep3Back)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kSave)) {

      // Create Forign Liaison.
      this.createForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to modify the foreign liaison checklist details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void modifyFLChecklistFromWizard(final BDMFrgnLiasnWizardDetails key)
    throws AppException, InformationalException {

    key.dtls.lisnChkLstOthrDesc = key.dtls.lisnChkLstOthrDesc.trim();
    // Validate input details.
    this.validateFLChecklistFromWizard(key);

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.wizardStateID = key.wizardStateID;
    readWizDetails.dtls.flChkLstCdTabList = key.dtls.flChkLstCdTabList;
    readWizDetails.dtls.lisnChkLstOthrDesc = key.dtls.lisnChkLstOthrDesc;

    if (key.actionIDProperty.equals(BDMConstants.kStep2Next)
      || key.actionIDProperty.equals(BDMConstants.kStep2Back)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kStep2SaveAndClose)) {
      this.modifyForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to modify the foreign liaison attachments details.
   *
   * @param BDMFrgnLiasnWizardDetails key
   * @return
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public void
    modifyFLAttachmentsFromWizard(final BDMFrgnLiasnWizardDetails key)
      throws AppException, InformationalException {

    final BDMFrgnLiasnWizardDetails readWizDetails =
      this.readWizardDetails(key.wizardStateID);
    readWizDetails.wizardStateID = key.wizardStateID;
    readWizDetails.dtls.flSelectedAttIDList = key.dtls.flSelectedAttIDList;

    if (key.actionIDProperty.equals(BDMConstants.kStep3Back)) {
      this.modifyWizard(key.wizardStateID, readWizDetails);
    } else if (key.actionIDProperty.equals(BDMConstants.kSave)) {
      // Modify Forign Liaison.
      this.modifyForeignLiaison(readWizDetails);
    }
  }

  /**
   * Method to get the foreign liaison attachment ID list.
   *
   * @param foreignLiaisonID
   * @return String attIDList.
   * @throws AppException
   * @throws InformationalException
   */
  private BDMFEAttachmentLinkDtlsList getFrgnLiasnAttIDList(
    final long foreignLiaisonID) throws AppException, InformationalException {

    final BDMFEAttachmentLink flAttObj =
      BDMFEAttachmentLinkFactory.newInstance();

    final BDMReadByFrgnLiasnIDKey readByFLIDKey =
      new BDMReadByFrgnLiasnIDKey();
    readByFLIDKey.foreignLiaisonID = foreignLiaisonID;

    final BDMFEAttachmentLinkKeyStruct1 bdmfeAttachmentLinkKeyStruct1 =
      new BDMFEAttachmentLinkKeyStruct1();
    bdmfeAttachmentLinkKeyStruct1.relatedID = foreignLiaisonID;

    final BDMFEAttachmentLinkDtlsList flAttLnkDtlsList =
      flAttObj.readByRelatedID(bdmfeAttachmentLinkKeyStruct1);

    return flAttLnkDtlsList;
  }

  /**
   * Method to get the foreign liaison attachment ID tab delimited string list.
   *
   * @param foreignLiaisonID
   * @return String attIDList.
   * @throws AppException
   * @throws InformationalException
   */
  private String getFLAttIDTabDelmStrList(final long foreignLiaisonID)
    throws AppException, InformationalException {

    String frgnLiasnAttIDList = "";

    final BDMFEAttachmentLinkDtlsList flAttLnkDtlsList =
      this.getFrgnLiasnAttIDList(foreignLiaisonID);

    BDMFEAttachmentLinkDtls bdmForeignLsnAttLnkDtls = null;

    final int attListSize = flAttLnkDtlsList.dtls.size();
    for (int i = 0; i < attListSize; i++) {
      bdmForeignLsnAttLnkDtls = flAttLnkDtlsList.dtls.item(i);
      if (i == attListSize - 1) {
        frgnLiasnAttIDList += bdmForeignLsnAttLnkDtls.attachmentID;

      } else {
        frgnLiasnAttIDList +=
          bdmForeignLsnAttLnkDtls.attachmentID + CuramConst.gkTabDelimiter;
      }
    }

    return frgnLiasnAttIDList;
  }

  /**
   * Method to read the foreign liaison details.
   *
   * @param BDMFrgnLiasnIDKey key
   * @return BDMForeignLiaisonDetails details..
   * @throws AppException
   * @throws InformationalException
   */
  private BDMForeignLiaisonDtls readForeignLiaison(
    final long foreignLiaisonID) throws AppException, InformationalException {

    final BDMForeignLiaison flObj = BDMForeignLiaisonFactory.newInstance();

    final BDMForeignLiaisonKey flKey = new BDMForeignLiaisonKey();
    flKey.foreignLiaisonID = foreignLiaisonID;

    final BDMForeignLiaisonDtls flDtls = flObj.read(flKey);

    return flDtls;
  }

  /**
   * Method to read the foreign liaison attachment details.
   *
   * @param long foreignLiaisonHistoryID
   * @return list of attachment history records
   * @throws AppException
   * @throws InformationalException
   */
  List<BDMReadFLAtchmntDetails>
    getForeignLiaisonHistoryRecords(final long foreignLiaisonHistoryID)
      throws AppException, InformationalException {

    final List<BDMReadFLAtchmntDetails> list = new java.util.ArrayList<>();

    final BDMSearchByForeignLiaisonHistoryIDKey searchKey =
      new BDMSearchByForeignLiaisonHistoryIDKey();

    searchKey.frgnLsnHstID = foreignLiaisonHistoryID;

    final BDMReadFLAtchmntDetailsList detailsList =
      BDMForgnLsnAttLnkHstFactory.newInstance()
        .searchByForeignLiaisonHistoryID(searchKey);

    list.addAll(detailsList.dtls);

    return list;
  }

  /**
   * Method to read details for creating/modifying liaison.
   *
   * @param WizardStateID key
   * @return BDMFrgnLiasnWizardDetails wizDetails.
   * @throws AppException
   * @throws InformationalException
   */
  @Override
  public BDMReadDispDetails readDetailsForForeignLiaison(
    final BDMReadDetailsKey key) throws AppException, InformationalException {

    final BDMReadDispDetails readDetails = new BDMReadDispDetails();

    // Get concernRoleID
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseHeaderKey caseHeaderKey = new CaseHeaderKey();
    caseHeaderKey.caseID = key.caseID;

    final ReadParticipantRoleIDDetails readParticipantRoleIDDetails =
      caseHeaderObj.readParticipantRoleID(caseHeaderKey);

    // Get list of foreign identifiers.
    final BDMFAConcernRoleKey croleKey = new BDMFAConcernRoleKey();
    croleKey.concernRoleID = readParticipantRoleIDDetails.concernRoleID;

    final BDMListOfForeignIDs bdmListOfForeignIDs =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .getListOfForeignIdentifiers(croleKey);
    readDetails.fidDetails = bdmListOfForeignIDs;

    // Get country code.
    final BDMFECase bdmfeCase = BDMFECaseFactory.newInstance();
    final BDMFECaseKey bdmfeCaseKey = new BDMFECaseKey();
    bdmfeCaseKey.caseID = key.caseID;
    final BDMFECaseDtls bdmfeCaseDtls = bdmfeCase.read(bdmfeCaseKey);
    readDetails.countryCode = bdmfeCaseDtls.countryCode;

    final BDMFACountryCodeKey cntryCodeKey = new BDMFACountryCodeKey();
    cntryCodeKey.countryCode = bdmfeCaseDtls.countryCode;

    // Get list of foreign offices.
    final BDMListOfOffices bdmListOfOffices =
      BDMMaintainForeignEngagementCaseFactory.newInstance()
        .getListOfForeignOffices(cntryCodeKey);
    readDetails.foDetails = bdmListOfOffices;

    // Get list of foreign applications.
    final BDMFECaseIDKey fecCaseIDKey = new BDMFECaseIDKey();
    fecCaseIDKey.caseID = key.caseID;
    final BDMFrgnAppDetailsList frgnAppDetalisList =
      this.listForeignAppsByCaseID(fecCaseIDKey);
    readDetails.faDetails = frgnAppDetalisList;

    // Get list of foreign liaison references.
    final BDMFrgnLiasnRefDetailsList flRefDetailsList =
      this.listLiaisonReferencesByCaseID(fecCaseIDKey);

    BDMFrgnLiasnRefDetailsList filteredFLRefDetailsList = null;

    if (key.fromEditInd) {
      filteredFLRefDetailsList = new BDMFrgnLiasnRefDetailsList();
      for (final BDMFrgnLiasnRefDetails frgnLiasnRefDetails : flRefDetailsList.dtls) {
        if (key.foreignLiaisonID != frgnLiasnRefDetails.foreignLiaisonID) {
          filteredFLRefDetailsList.dtls.addRef(frgnLiasnRefDetails);
        }
      }
      readDetails.lrDetails = filteredFLRefDetailsList;
    } else {
      readDetails.lrDetails = flRefDetailsList;
    }

    return readDetails;
  }
}
