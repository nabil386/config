package curam.ca.gc.bdm.facade.resource.impl;

import curam.ca.gc.bdm.codetable.BDMEXTERNALLIABILITYTYPE;
import curam.ca.gc.bdm.entity.deduction.fact.BDMDeductionFactory;
import curam.ca.gc.bdm.entity.deduction.intf.BDMDeduction;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDetails;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionDtls;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionExternalLiabilityKey;
import curam.ca.gc.bdm.entity.deduction.struct.BDMDeductionKey;
import curam.ca.gc.bdm.entity.deduction.struct.BDMRecordStatusExternalLiabilityKey;
import curam.ca.gc.bdm.facade.resource.struct.BDMCreateDeductionDtls;
import curam.ca.gc.bdm.facade.resource.struct.BDMExternalLiabilityType;
import curam.ca.gc.bdm.facade.resource.struct.BDMExternalLiabilityTypeList;
import curam.ca.gc.bdm.facade.resource.struct.BDMModifyDeductionDetails;
import curam.ca.gc.bdm.facade.resource.struct.BDMReadDeductionDtls;
import curam.ca.gc.bdm.message.BDMDEDUCTIONS;
import curam.codetable.DEDUCTIONCATEGORYCODE;
import curam.codetable.DEDUCTIONNAME;
import curam.codetable.RECORDSTATUS;
import curam.core.facade.struct.CreateDeductionDtls;
import curam.core.facade.struct.DeductionKey;
import curam.core.facade.struct.ModifyDeductionDetails;
import curam.core.facade.struct.ReadDeductionDtls;
import curam.core.fact.ConcernRoleFactory;
import curam.core.impl.CuramConst;
import curam.core.sl.entity.fact.DeductionFactory;
import curam.core.sl.entity.struct.DeductionDtls;
import curam.core.sl.struct.NextPriorityInd;
import curam.core.struct.ConcernRoleKey;
import curam.core.struct.Count;
import curam.core.struct.IndicatorStruct;
import curam.core.struct.VersionNumberDetails;
import curam.util.exception.AppException;
import curam.util.exception.InformationalElement.InformationalType;
import curam.util.exception.InformationalException;
import curam.util.exception.InformationalManager;
import curam.util.resources.StringUtil;
import curam.util.transaction.TransactionInfo;
import curam.util.type.CodeTable;
import curam.util.type.CodeTableItemIdentifier;
import curam.util.type.NotFoundIndicator;

public class BDMResource
  extends curam.ca.gc.bdm.facade.resource.base.BDMResource {

  @Override
  public void createDeduction(final BDMCreateDeductionDtls key)
    throws AppException, InformationalException {

    validateBDMDeductionDetails(key.dtls.externalLiabilityType,
      key.dtls.category, key.dtls.thirdPartyConcernRoleID, 0L);

    final CreateDeductionDtls createDeductionKey = new CreateDeductionDtls();
    final IndicatorStruct indStruct = new IndicatorStruct();
    indStruct.changeAllIndicator = key.changeAllIndicator;
    createDeductionKey.dtls.applyPriorityChange = indStruct;

    createDeductionKey.dtls.changeAllOverlappingIndOpt =
      key.changeAllOverlappingIndOpt;

    final NextPriorityInd nextPriorityInd = new NextPriorityInd();
    nextPriorityInd.applyNextPriorityInd = key.applyNextPriorityInd;

    createDeductionKey.dtls.nextPriority = nextPriorityInd;

    createDeductionKey.dtls.dtls.assign(key.dtls);
    curam.core.facade.fact.ResourceFactory.newInstance()
      .createDeduction(createDeductionKey);

    final BDMDeductionDtls bdmDeductionKey = new BDMDeductionDtls();
    bdmDeductionKey.deductionID = createDeductionKey.dtls.dtls.deductionID;
    bdmDeductionKey.managedBy = key.dtls.managedBy;
    bdmDeductionKey.externalLiabilityType = key.dtls.externalLiabilityType;
    bdmDeductionKey.thirdPartyConcernRoleID =
      key.dtls.thirdPartyConcernRoleID;
    bdmDeductionKey.deductionType = key.dtls.deductionType;
    BDMDeductionFactory.newInstance().insert(bdmDeductionKey);

  }

  @Override
  public BDMReadDeductionDtls readDeduction(final DeductionKey key)
    throws AppException, InformationalException {

    final BDMReadDeductionDtls bdmReadDeductionDtls =
      new BDMReadDeductionDtls();
    final ReadDeductionDtls readDeductionDtls =
      curam.core.facade.fact.ResourceFactory.newInstance().readDeduction(key);

    final BDMDeductionKey deductionIdKey = new BDMDeductionKey();
    deductionIdKey.deductionID = readDeductionDtls.dtls.dtls.deductionID;

    final BDMDeductionDetails bdmDeductionDtls =
      BDMDeductionFactory.newInstance().readByDeductionID(deductionIdKey);

    bdmReadDeductionDtls.dtls = readDeductionDtls;
    bdmReadDeductionDtls.managedBy = bdmDeductionDtls.managedBy;
    bdmReadDeductionDtls.externalLiabilityType =
      bdmDeductionDtls.externalLiabilityType;
    bdmReadDeductionDtls.thirdPartyConcernRoleID =
      bdmDeductionDtls.thirdPartyConcernRoleID;
    bdmReadDeductionDtls.deductionType = bdmDeductionDtls.deductionType;
    return bdmReadDeductionDtls;
  }

  @Override
  public void modifyDeduction(final BDMModifyDeductionDetails arg1)
    throws AppException, InformationalException {

    validateBDMDeductionDetails(arg1.externalLiabilityType,
      arg1.dtls.dtls.dtls.category, arg1.thirdPartyConcernRoleID,
      arg1.dtls.dtls.dtls.deductionID);

    final ModifyDeductionDetails modifyDeductionKey =
      new ModifyDeductionDetails();
    modifyDeductionKey.dtls = arg1.dtls.dtls;
    modifyDeductionKey.key = arg1.dtls.key;
    curam.core.facade.fact.ResourceFactory.newInstance()
      .modifyDeduction(modifyDeductionKey);

    final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();
    final BDMDeductionKey bdmDeductionKey = new BDMDeductionKey();
    bdmDeductionKey.deductionID = arg1.dtls.key.key.deductionID;
    final VersionNumberDetails versionNumberDetails =
      bdmDeductionObj.readVersionNo(bdmDeductionKey);
    final BDMDeductionDetails bdmDeductionDtls = new BDMDeductionDetails();
    bdmDeductionDtls.deductionID = arg1.dtls.key.key.deductionID;
    bdmDeductionDtls.managedBy = arg1.managedBy;
    bdmDeductionDtls.externalLiabilityType = arg1.externalLiabilityType;
    bdmDeductionDtls.thirdPartyConcernRoleID = arg1.thirdPartyConcernRoleID;
    bdmDeductionDtls.deductionType = arg1.deductionType;
    bdmDeductionDtls.versionNo = versionNumberDetails.versionNo;
    bdmDeductionObj.modifyByDeductionID(bdmDeductionKey, bdmDeductionDtls);

  }

  /**
   * Lists all possible external liability types
   */
  @Override
  public BDMExternalLiabilityTypeList listExternalLiabilityTypes()
    throws AppException, InformationalException {

    final BDMExternalLiabilityTypeList externalLiabilityList =
      new BDMExternalLiabilityTypeList();

    final String programLocale = TransactionInfo.getProgramLocale();
    final String[] allExternalLiabilityTypes = CodeTable
      .getAllCodes(BDMEXTERNALLIABILITYTYPE.TABLENAME, programLocale);

    for (final String type : allExternalLiabilityTypes) {
      final BDMExternalLiabilityType externalLiabilityType =
        new BDMExternalLiabilityType();
      externalLiabilityType.externalLiabilityType = type;
      externalLiabilityList.dtls.add(externalLiabilityType);
    }

    return externalLiabilityList;
  }

  /**
   * Validates that if it is a 3rd party deduction, a valid third party concern
   * role has been added.
   * Validates that if it is linked to an external liability, no other
   * deductions are also linked to it.
   *
   * @param externalLiabilityType
   * @param category
   * @param thirdPartyConcernRoleID
   * @param deductionID
   * @throws InformationalException
   * @throws AppException
   */
  public void validateBDMDeductionDetails(final String externalLiabilityType,
    final String category, final long thirdPartyConcernRoleID,
    final long deductionID) throws InformationalException, AppException {

    final InformationalManager infoManager =
      TransactionInfo.getInformationalManager();

    // makes sure there is a third party concern role set if it is a third party
    // deduction
    if (category.equals(DEDUCTIONCATEGORYCODE.THIRDPARTYDEDUCTION)
      && thirdPartyConcernRoleID == 0L) {
      infoManager.addInformationalMsg(
        new AppException(BDMDEDUCTIONS.ERR_THIRD_PARTY_CONCERN_ROLE_MISSING),
        CuramConst.gkEmpty, InformationalType.kError);
    }
    // makes sure that the concern role ID is valid
    if (thirdPartyConcernRoleID != 0L) {
      final NotFoundIndicator nfIndicator = new NotFoundIndicator();

      final ConcernRoleKey crKey = new ConcernRoleKey();
      crKey.concernRoleID = thirdPartyConcernRoleID;
      ConcernRoleFactory.newInstance().read(nfIndicator, crKey);

      if (nfIndicator.isNotFound()) {
        infoManager.addInformationalMsg(
          new AppException(
            BDMDEDUCTIONS.ERR_THIRD_PARTY_CONCERN_ROLE_INVALID),
          CuramConst.gkEmpty, InformationalType.kError);
      }
    }

    // makes sure that no other deductions are linked to the given external
    // liability
    if (!StringUtil.isNullOrEmpty(externalLiabilityType)) {

      // try to find a deduction ID linked to the external liability
      final BDMDeductionExternalLiabilityKey deductionExtLbyKey =
        new BDMDeductionExternalLiabilityKey();

      deductionExtLbyKey.externalLiabilityType = externalLiabilityType;
      deductionExtLbyKey.deductionID = deductionID;
      deductionExtLbyKey.recordStatus = RECORDSTATUS.NORMAL;

      final BDMDeduction bdmDeductionObj = BDMDeductionFactory.newInstance();

      final Count deductionCount = bdmDeductionObj
        .countDeductionsWithExternalLiabilityType(deductionExtLbyKey);

      // if one is found and it is not the same deduction ID (in the case of a
      // modification), then throw an error
      if (deductionCount.numberOfRecords > 0) {

        final BDMRecordStatusExternalLiabilityKey extLbyKey =
          new BDMRecordStatusExternalLiabilityKey();
        extLbyKey.externalLiabilityType = externalLiabilityType;
        extLbyKey.recordStatus = RECORDSTATUS.NORMAL;

        // get the deduction associated with the liability type
        final BDMDeductionDetails bdmDeductionDetails =
          BDMDeductionFactory.newInstance()
            .readActiveDeductionLinkedToExternalLiabilityType(extLbyKey);

        final curam.core.sl.entity.struct.DeductionKey linkedDeductionDtls =
          new curam.core.sl.entity.struct.DeductionKey();

        linkedDeductionDtls.deductionID = bdmDeductionDetails.deductionID;

        final DeductionDtls deductionDtls =
          DeductionFactory.newInstance().read(linkedDeductionDtls);

        final AppException e = new AppException(
          BDMDEDUCTIONS.ERR_LIABILITY_SET_FOR_ANOTHER_DEDUCTION);
        e.arg(new CodeTableItemIdentifier(DEDUCTIONNAME.TABLENAME,
          deductionDtls.deductionName));

        infoManager.addInformationalMsg(e, CuramConst.gkEmpty,
          InformationalType.kError);
      }

    }

    if (infoManager.operationHasInformationals()) {
      infoManager.failOperation();
    }
  }

}
